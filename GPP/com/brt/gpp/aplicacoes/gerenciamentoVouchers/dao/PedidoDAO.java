package com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedidoItem;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe é responsável por fornecer métodos de acesso ao banco de dados
 * para o processo de pedidos de criação de vouchers.
 * 
 * @author Gustavo Gusmao
 * @since 24/03/2006
 *
 */
public class PedidoDAO 
{
	/**
	 * Metodo....:trocaStatusPedido
	 * Descricao.:Troca o status do pedido na tabela de interface para indicar que 
	 *            este esta em processamento
	 * @param conexaoPrep - Conexao de banco de dados a ser utilizada
	 * @throws GPPInternalErrorException
	 */
	public static void trocaStatusPedido(PREPConexao conexaoPrep, long idProcesso, long numPedido) throws GPPInternalErrorException
	{
		String sqlTrocaStatus = "UPDATE TBL_INT_VOUCHER_PEDIDO " +
		                           "SET IDT_STATUS_PEDIDO = ? " +
								 "WHERE NUM_PEDIDO = ?";
		Object param[] = {Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,new Long(numPedido)};
		conexaoPrep.executaPreparedUpdate(sqlTrocaStatus,param,idProcesso);
	}
	
	/**
	 * Metodo....:atualizaNumeroJobPedido
	 * Descricao.:Atualiza informacoes do numero do job do pedido
	 * @param conexaoPrep - Conexao de banco de dados a ser utilizada
	 * @throws GPPInternalErrorException
	 */
	public static void atualizaNumeroJobPedido(PREPConexao conexaoPrep, long idProcesso, int numJobTecnomen, long numPedido) throws GPPInternalErrorException
	{
		String sqlUpdate = "UPDATE TBL_REC_VOUCHER_PEDIDO SET NUM_JOB_TECNOMEN=? WHERE NUM_PEDIDO=?";
		Object param[] = {new Integer(numJobTecnomen), new Long(numPedido)};
		conexaoPrep.executaPreparedUpdate(sqlUpdate,param,idProcesso);
	}
	
	/**
	 * Metodo....:insereDadosPedido
	 * Descricao.:Insere os dados do pedido e de seus itens na tabela definitiva do sistema
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @throws GPPInternalErrorException
	 */
	public static void insereDadosPedido(PREPConexao conexaoPrep, long idProcesso, VoucherPedido pedidoVoucher) throws GPPInternalErrorException
	{
		// Insere os dados do pedido na tabela definitiva.
		// OBS: O campo idt_status_pedido fica com o valor indicando que o mesmo ainda
		//      esta em processamento, sendo que o campo id_status_voucher indica que
		//      apesar de ter sido criada a ordem ainda nao foi confirmado a criacao do
		//      mesmo pela plataforma
		String sqlInsert = "INSERT INTO TBL_REC_VOUCHER_PEDIDO " +
		                   "(NUM_PEDIDO,DAT_PEDIDO,DAT_CRIACAO,TIP_CARTAO,NOM_ARQUIVO,"+
						    "ID_REQUISITANTE,IDT_STATUS_PEDIDO,ID_STATUS_VOUCHER,NUM_ORDEM) " +
						   "VALUES (?,?,?,?,?,?,?,?,?)";
		Object param[] = {new Long(pedidoVoucher.getNumPedido())
				         ,new Timestamp(pedidoVoucher.getDataPedido().getTime())
						 ,null
						 ,pedidoVoucher.getTipCartao()
						 ,pedidoVoucher.getNomeArquivo()
						 ,new String(pedidoVoucher.getIdRequisitante())
						 ,Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO
						 ,new Integer(Definicoes.STATUS_VOUCHER_GERACAO_SOLICITADA)
						 ,new Long(pedidoVoucher.getNumOrdem())
						 };
		conexaoPrep.executaPreparedUpdate(sqlInsert,param,idProcesso);
		// Apos inserir os dados do pedido, insere os itens
		insereDadosItemPedido(conexaoPrep, idProcesso, pedidoVoucher);
	}
	
	/**
	 * Metodo....:insereDadosItemPedido
	 * Descricao.:Insere os dados dos itens do pedido, um a um
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @throws GPPInternalErrorException
	 */
	private static void insereDadosItemPedido(PREPConexao conexaoPrep, long idProcesso, VoucherPedido pedidoVoucher) throws GPPInternalErrorException
	{
		String sqlInsert = "INSERT INTO TBL_REC_VOUCHER_PEDIDO_ITEM " +
		                   "(NUM_PEDIDO,NUM_ITEM,QTD_CARTOES,VLR_FACE,VLR_FACE_BONUS,VLR_FACE_SM," +
						    "VLR_FACE_DADOS,DAT_EXPIRACAO,DAT_EXPIRACAO_BONUS,DAT_EXPIRACAO_SM," +
							"DAT_EXPIRACAO_DADOS,QTD_POR_BATCH,QTD_BATCH_POR_CAIXA,IDT_ESTAMPA) " +
						   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// Para cada item do pedido realiza o insert do mesmo na tabela definitiva de item de pedido
		for (Iterator i = pedidoVoucher.getItensPedido().iterator(); i.hasNext();)
		{
			VoucherPedidoItem item 	  = (VoucherPedidoItem)i.next();
			VoucherPedidoItem subItem = item.getSubItem();
			Object param[] = {new Long(pedidoVoucher.getNumPedido())
					         ,new Long(item.getNumItem())
							 ,new Long(subItem != null ? item.getQtdeCartoes()+subItem.getQtdeCartoes() : item.getQtdeCartoes())
							 ,new Double(item.getValorFace())
							 ,new Double(item.getValorFaceBonus())
							 ,new Double(item.getValorFaceSm())
							 ,new Double(item.getValorFaceDados())
							 ,item.getExpiracao() 		!= null ? new Timestamp(item.getExpiracao().getTime()) 		: null
							 ,item.getExpiracaoBonus() 	!= null ? new Timestamp(item.getExpiracaoBonus().getTime()) : null
							 ,item.getExpiracaoSm()		!= null ? new Timestamp(item.getExpiracaoSm().getTime())	: null
							 ,item.getExpiracaoDados()	!= null ? new Timestamp(item.getExpiracaoDados().getTime())	: null
							 ,new Long(item.getQtdePorBatch())
							 ,new Long(item.getQtdeBatchPorCaixa())
							 ,item.getEstampa()
	         				 };
			
			conexaoPrep.executaPreparedUpdate(sqlInsert, param, idProcesso);
		}
	}
	
	/**
	 * Metodo....:atualizaSituacaoPedido
	 * Descricao.:Este metodo atualiza no banco de dados a situacao e o numero de job para um
	 *            determinado pedido
	 * @param numeroPedido		- Numero do pedido a ser atualizado
	 * @param numeroJob			- Numero do job da tecnomen que esta processando a requisicao
	 * @param idStatusVoucher	- Id do status do voucher deste pedido a ser atualizado
	 * @return boolean			- Indica se conseguiu atualizar os dados do pedido
	 * @throws GPPInternalErrorException
	 */
	public static boolean atualizaSituacaoPedido(long numeroPedido, long numeroJob, long idStatusVoucher)
	{
		boolean retorno = false;
		PREPConexao conexaoPrep = null;
		GerentePoolLog poolLog = GerentePoolLog.getInstancia(PedidoDAO.class);
		long idProcesso = poolLog.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sqlUpdate = "UPDATE TBL_REC_VOUCHER_PEDIDO " +
			                      "SET NUM_JOB_TECNOMEN = ? " +
								     ",ID_STATUS_VOUCHER = ? " +
									 ",DAT_CRIACAO = DECODE(ID_STATUS_VOUCHER,?,SYSDATE,DAT_CRIACAO) " +
								"WHERE NUM_PEDIDO = ?";
			Object param[] = {new Long(numeroJob)
					         ,idStatusVoucher == 0 ? null : new Long(idStatusVoucher)
					         ,new Integer(Definicoes.STATUS_VOUCHER_CRIADO)
					         ,new Long(numeroPedido)
							 };
			conexaoPrep.executaPreparedUpdate(sqlUpdate,param,idProcesso);
			retorno = true;
		}
		catch(GPPInternalErrorException ge)
		{
			poolLog.log(0,Definicoes.WARN,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"atualizaSituacaoPedido","Erro ao atualizar situacao do pedido:"+numeroPedido+" Erro:"+ge);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return retorno;
	}
	
	/**
	 * Metodo....:atualizaStatusJobPedido
	 * Descricao.:Atualiza a descricao do status do job sendo executado para o pedido
	 * @param numeroPedido	- Numero do pedido
	 * @param descStatus	- Descricao do status
	 */
	public static void atualizaStatusJobPedido(long numeroPedido, String idtStatus, String descStatus)
	{
		PREPConexao conexaoPrep = null;
		GerentePoolLog poolLog = GerentePoolLog.getInstancia(PedidoDAO.class);
		long idProcesso = poolLog.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sqlUpdate = "UPDATE TBL_REC_VOUCHER_PEDIDO " +
			                      "SET DES_STATUS_JOB = ? " +
								     ",IDT_STATUS_PEDIDO = ? " +
								"WHERE NUM_PEDIDO = ?";
			Object param[] = {descStatus, idtStatus, new Long(numeroPedido)};
			conexaoPrep.executaPreparedUpdate(sqlUpdate,param,idProcesso);
		}
		catch(GPPInternalErrorException ge)
		{
			poolLog.log(0,Definicoes.WARN,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"atualizaStatusJobPedido","Erro ao atualizar descricao do status do job do pedido:"+numeroPedido+" Erro:"+ge);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
	
	/**
	 * Metodo....:finalizaProcessamentoPedido
	 * Descricao.:Este metodo e o responsavel por atualizar o status nas tabelas de pedido
	 *            e de interface quando a situacao final do pedido for alcancada.
	 * @param numeroPedido	- Numero do pedido a ser finalizado
	 */
	public static void finalizaProcessamentoPedido(long numeroPedido)
	{
		PREPConexao conexaoPrep = null;
		GerentePoolLog poolLog = GerentePoolLog.getInstancia(PedidoDAO.class);
		long idProcesso = poolLog.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			conexaoPrep.setAutoCommit(false);
			// Primeiro realiza o update na tabela de pedido indicando que o mesmo foi processado
			// em todos os estagios de criacao de voucher, em seguida atualiza a tabela de interface
			// para indicar os valores de caixa e lote dos itens de pedido e o marca indicando que o
			// mesmo ja pode ser enviado de volta para o SAP
			String sqlAtualizaStatus = "UPDATE TBL_REC_VOUCHER_PEDIDO " +
			                              "SET IDT_STATUS_PEDIDO = ? " +
										     ",ID_STATUS_VOUCHER = ? " +
											 ",NUM_JOB_TECNOMEN = NULL, DES_STATUS_JOB = NULL " +
									    "WHERE NUM_PEDIDO = ?";
			Object paramAtualizaStatus[] = {Definicoes.STATUS_PEDIDO_VOUCHER_PROCESSADO,
					                        new Integer(Definicoes.STATUS_VOUCHER_CONCLUIDO),
										    new Long(numeroPedido)};
			conexaoPrep.executaPreparedUpdate(sqlAtualizaStatus,paramAtualizaStatus,idProcesso);

			// Atualiza o cabecalho do pedido na tabela de interface
			String sqlAtuPedInt = "UPDATE TBL_INT_VOUCHER_PEDIDO I " +
            						 "SET (I.IDT_STATUS_PEDIDO, I.NOM_ARQUIVO, I.DAT_CRIACAO) = " +
									 			"(SELECT P.IDT_STATUS_PEDIDO, P.NOM_ARQUIVO, P.DAT_CRIACAO " +
												   "FROM TBL_REC_VOUCHER_PEDIDO P " +
												  "WHERE P.NUM_PEDIDO = I.NUM_PEDIDO " +
												 ") " +
								   "WHERE I.NUM_PEDIDO = ?";
			Object paramAtuPedInt[] = {new Long(numeroPedido)};
			conexaoPrep.executaPreparedUpdate(sqlAtuPedInt,paramAtuPedInt,idProcesso);
			
			// Atualiza as informacoes dos itens do pedido na tabela de interface
			String sqlAtuPedItemInt = "UPDATE TBL_INT_VOUCHER_PEDIDO_ITEM I " +
			                             "SET (I.NUM_CAIXA_LOTE_INICIAL, I.NUM_CAIXA_LOTE_FINAL) = " +
						 					      "(SELECT P.NUM_CAIXA_LOTE_INICIAL, P.NUM_CAIXA_LOTE_FINAL " +
						 						     "FROM TBL_REC_VOUCHER_PEDIDO_ITEM P " +
												    "WHERE P.NUM_PEDIDO = I.NUM_PEDIDO " +
													  "AND P.NUM_ITEM   = I.NUM_ITEM " +
												   ") " +
									   "WHERE I.NUM_PEDIDO = ?";
			Object paramAtuPedItemInt[] = {new Long(numeroPedido)};
			conexaoPrep.executaPreparedUpdate(sqlAtuPedItemInt,paramAtuPedItemInt,idProcesso);
			
			// Apos todos os comandos terem sido executados, entao efetiva a transacao
			conexaoPrep.commit();
		}
		catch(Exception ge)
		{
			poolLog.log(0,Definicoes.WARN,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"finalizaProcessamento","Erro ao terminar processamento de pedido:"+numeroPedido+" Erro:"+ge);
			// Em caso de algum erro ocorrer enta a transacao e desfeita
			try
			{
				conexaoPrep.rollback();
			}
			catch(SQLException se)
			{
				poolLog.log(0,Definicoes.WARN,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"finalizaProcessamento","Erro ao desfazer a transacao de processamento do pedido:"+numeroPedido+" Erro:"+se);
			}
		}
		finally
		{
			// Volta a conexao para autocommit
			try
			{
				conexaoPrep.setAutoCommit(true);
			}
			catch(SQLException se)
			{
				poolLog.log(0,Definicoes.WARN,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"finalizaProcessamento","Erro ao voltar a conexao de banco de dados para autocommit. Erro: "+se);				
			}
			finally
			{
				GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
			}
		}
	}

	/**
	 * Metodo....:buscaListaPedidos
	 * Descricao.:Retorna a lista dos pedidos desejados com o status passado como parametro
	 * @param statusVoucher	- Status dos pedidos a serem lidos
	 * @return Collection	- Lista de pedidos correspondentes ao parametro
	 */
	public static Collection buscaListaPedidos(String statusPedido,int statusVoucher)
	{
		Collection lista = new LinkedList();
		PREPConexao conexaoPrep = null;
		GerentePoolLog poolLog = GerentePoolLog.getInstancia(PedidoDAO.class);
		long idProcesso = poolLog.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sqlPedido =  "select p.num_pedido        as num_pedido " +
								      ",p.dat_pedido        as dat_pedido " +
								      ",p.dat_criacao       as dat_criacao " +
								      ",p.tip_cartao        as tip_cartao " +
									  ",p.num_ordem         as num_ordem " +
									  ",p.id_requisitante   as id_requisitante " +
								      ",p.idt_status_pedido as idt_status_pedido " +
									  ",p.num_job_tecnomen  as num_job_tecnomen " +
									  ",p.dat_envio_email   as dat_envio_email " +
								  "from tbl_rec_voucher_pedido p " +
								 "where p.idt_status_pedido = ? " +
								   "and p.id_status_voucher = ? " +
								   "and p.dat_envio_email is null";

			// Busca somente os pedidos em que estejam de acordo com o status solicitado
			// mas desde que ainda estejam em processamento.
			Object param[] = {statusPedido, new Integer(statusVoucher)};
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPedido,param,idProcesso);
			while (rs.next())
			{
				VoucherPedido pedido = new VoucherPedido(rs.getLong("num_pedido"));
				pedido.setDataCriacao	((java.util.Date)	rs.getTimestamp("dat_criacao")		);
				pedido.setDataPedido 	((java.util.Date)	rs.getTimestamp("dat_pedido")		);
				pedido.setTipCartao  	(					rs.getString("tip_cartao")			);
				pedido.setStatusPedido	(					rs.getString("idt_status_pedido")	);
				pedido.setIdRequisitante(					rs.getString("id_requisitante")		);
				pedido.setNumJobTecnomen(					rs.getInt("num_job_tecnomen")		);
				pedido.setNumOrdem		(					rs.getLong("num_ordem")				);
				pedido.setDataEnvioEmail((java.util.Date) 	rs.getTimestamp("dat_envio_email")	);

				lista.add(pedido);
			}
		}
		catch(Exception e)
		{
			poolLog.log(0,Definicoes.WARN,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"buscaListaPedidos","Erro ao listar pedidos com o status igual a "+statusVoucher+" Erro:"+e);			
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return lista;
	}

	/**
	 * Metodo....:buscaPedido
	 * Descricao.:Este metodo realiza a busca de um pedido de voucher e seus itens
	 *            populando um objeto VoucherPedido e VoucherPedidoItem
	 * @param numeroPedido		- Numero do Pedido desejado
	 * @return VoucherPedido	- Objeto contendo as informacoes do pedido e dos itens
	 */
	public static VoucherPedido buscaPedido(long numeroPedido)
	{
		VoucherPedido pedido = null;
		PREPConexao conexaoPrep = null;
		GerentePoolLog poolLog = GerentePoolLog.getInstancia(PedidoDAO.class);
		long idProcesso = poolLog.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sqlPedido =  "select p.num_pedido        as num_pedido " +
								      ",p.dat_pedido        as dat_pedido " +
								      ",p.dat_criacao       as dat_criacao " +
								      ",p.tip_cartao        as tip_cartao " +
									  ",p.id_requisitante   as id_requisitante " +
								      ",p.idt_status_pedido as idt_status_pedido " +
								      ",p.id_status_voucher as id_status_voucher " +
									  ",p.num_ordem         as num_ordem " +
									  ",p.nom_arquivo       as nom_arquivo " +
								      ",i.num_item          as num_item " +
								      ",i.qtd_cartoes       as qtd_cartoes " +
									  ",i.idt_estampa       as idt_estampa " +
								      ",i.vlr_face          as vlr_face " +
								      ",i.vlr_face_bonus    as vlr_face_bonus " +
								      ",i.vlr_face_sm       as vlr_face_sm " +
								      ",i.vlr_face_dados    as vlr_face_dados " +
								      ",i.dat_expiracao          as expiracao " +
								      ",i.dat_expiracao_bonus    as expiracao_bonus " +
								      ",i.dat_expiracao_sm       as expiracao_sm " +
								      ",i.dat_expiracao_dados    as expiracao_dados " +
									  ",i.num_caixa_lote_inicial as num_caixa_lote_inicial " +
									  ",i.num_caixa_lote_final   as num_caixa_lote_final " +
									  ",i.qtd_por_batch          as qtd_por_batch " +
									  ",i.qtd_batch_por_caixa    as qtd_batch_por_caixa " +
									  ",r.idt_email              as idt_email " +
								  "from tbl_rec_reqmail r " +
								      ",tbl_rec_voucher_pedido p " +
								      ",tbl_rec_voucher_pedido_item i " +
								 "where r.id_requisitante = p.id_requisitante " +
								   "and i.num_pedido = p.num_pedido " +
								   "and p.num_pedido = ? ";

			Object param[] = {new Long(numeroPedido)};
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPedido,param,idProcesso);
			while (rs.next())
			{
				if (rs.isFirst())
				{
					pedido = new VoucherPedido(rs.getLong("num_pedido"));
					pedido.setDataCriacao		((java.util.Date)	rs.getTimestamp("dat_criacao")		);
					pedido.setDataPedido 		((java.util.Date)	rs.getTimestamp("dat_pedido")		);
					pedido.setTipCartao  		(					rs.getString("tip_cartao")			);
					pedido.setStatusPedido		(					rs.getString("idt_status_pedido")	);
					pedido.setStatusVoucher		(					rs.getInt("id_status_voucher")		);
					pedido.setIdRequisitante	(					rs.getString("id_requisitante")		);
					pedido.setNumOrdem			(					rs.getLong("num_ordem")				);
					pedido.setEMailRequisitante	(					rs.getString("idt_email")			);
					pedido.setNomeArquivo		(					rs.getString("nom_arquivo")			);
				}
				VoucherPedidoItem item = new VoucherPedidoItem(pedido,rs.getInt("num_item"));
				item.setQtdeCartoes			(					rs.getLong("qtd_cartoes")				);
				item.setQtdePorBatch    	(					rs.getLong("qtd_por_batch")				);
				item.setQtdeBatchPorCaixa	(					rs.getLong("qtd_batch_por_caixa")		);
				item.setEstampa				(					rs.getString("idt_estampa")				);
				item.setValorFace			(					rs.getLong("vlr_face")					);
				item.setValorFaceBonus		(					rs.getLong("vlr_face_bonus")			);
				item.setValorFaceSm			(					rs.getLong("vlr_face_sm")				);
				item.setValorFaceDados		(					rs.getLong("vlr_face_dados")			);
				item.setExpiracao			((java.util.Date)	rs.getTimestamp("expiracao")			);
				item.setExpiracaoBonus		((java.util.Date)	rs.getTimestamp("expiracao_bonus")		);
				item.setExpiracaoSm			((java.util.Date)	rs.getTimestamp("expiracao_sm")			);
				item.setExpiracaoDados		((java.util.Date)	rs.getTimestamp("expiracao_dados")		);
				item.setNumCaixaLoteInicial	(					rs.getLong("num_caixa_lote_inicial")	);
				item.setNumCaixaLoteFinal	(					rs.getLong("num_caixa_lote_final")		);

				pedido.addVoucherItem(item);
			}
		}
		catch(Exception e)
		{
			poolLog.log(idProcesso,Definicoes.DEBUG,"GerenciaPedidosVoucher","buscaPedidos","Erro ao buscar dados do pedido de voucher. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return pedido;
	}
}