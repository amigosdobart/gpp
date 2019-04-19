package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao.VoucherDAO;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.util.ManipuladorXMLVoucher;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Esta classe e a responsavel por "varrer" os registros de ativacao
 * de vouchers aonde o numero do job indica qual processo deve ser verificado
 * na plataforma para saber se a tarefa terminou
 * 
 * @author Joao Carlos
 * Data..: 30-Jan-2005
 *
 */
public class VerificaJobAtivacao
{
	private long 			idProcesso;
	private GerentePoolLog	poolLog;
	
	/**
	 * Metodo....:VerificaJobAtivacao
	 * Descricao.:Construtor da classe
	 * @param idProcesso - Id do processo que construiu a instancia
	 */
	public VerificaJobAtivacao(long idProcesso)
	{
		this.idProcesso = idProcesso;
		this.poolLog	= GerentePoolLog.getInstancia(this.getClass());
	}

	

	/**
	 * Metodo....:finalizaProcessoAtivacao
	 * Descricao.:Finaliza o processamento da requisicao de ativacao
	 * @param conexaoPrep		- Conexao de banco de dados a ser utilizada
	 * @param job				- Job tecnomen para verificacao do status
	 * @param idProcessamento	- Id do processamento da requisicao
	 * @param evtNegocio		- Evento de negocio da requisicao
	 * @throws GPPInternalErrorException
	 */
	private void finalizaProcessoAtivacao(PREPConexao conexaoPrep, JobTecnomen job, long idProcessamento, String evtNegocio)
			throws GPPInternalErrorException, SQLException
	{
		// Para a finalizacao do processo realiza-se tres etapas
		// 1 - Atualiza a informacao de job para indicar termino do processamento 
		// 2 - Atualiza a informacao na tabela de interface de entrada indicando termino
		// 3 - Atualiza a informacao na tabela de interface de saida contendo o XML de retorno
		
		// 1 Etapa - Atualiza as informacoes da tabela de job
		VoucherDAO.atualizaStatusJob(conexaoPrep, idProcessamento, job, idProcessamento, evtNegocio);

		// 2 Etapa - Atualiza as informacoes na interface de entrada
		VoucherDAO.atualizaStatusInterface(conexaoPrep, idProcesso, Definicoes.IDT_PROCESSAMENTO_OK, idProcessamento, evtNegocio);
		
		// 3 Etapa - Atualiza as informacoes na interface de saida
		VoucherDAO.atualizaInterfaceSaida(conexaoPrep, idProcesso, job, getSolicitacao(idProcessamento,evtNegocio));
	}

	/**
	 * Metodo....:verificaJobs
	 * Descricao.:Verifica os jobs de ativacao de voucher na plataforma tecnomen
	 *
	 */
	public void verificaJobs()
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			conexaoPrep.setAutoCommit(false);

			String sqlPesquisa = "SELECT  ID_PROCESSAMENTO " +
			                            ",IDT_EVENTO_NEGOCIO " +
										",NUM_JOB_TECNOMEN " +
								   "FROM TBL_GER_STATUS_JOB_TECNOMEN " +
								  "WHERE COD_RETORNO_EXECUCAO = -1";

			OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
			// Executa a consulta para verificar quais registros possuem job a ser verificado
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,null,idProcesso);
			
			while (rs.next())
			{
				long 	idProcessamento = rs.getLong  ("ID_PROCESSAMENTO");
				int		numJobTecnomen  = rs.getInt   ("NUM_JOB_TECNOMEN");
				String	eventoNegocio	= rs.getString("IDT_EVENTO_NEGOCIO");
			
				JobTecnomen job = null;
				if (numJobTecnomen != 0)
					job = opVoucher.getJobTecnomen(numJobTecnomen);
				else
				{
					// Cria um job fake para indicar que o processamento ok
					// para os registros que estiverem com numeracao de job iguais a zero
					job = new JobTecnomen(0);
					job.setOpState(Definicoes.COD_RET_JOB_PROCESSADO_OK);
					job.setWorkDone(0);
					job.setWorkTotal(0);
				}
				
				// Devido a problemas na plataforma Tecnomen, algumas vezes o job de ativacao
				// eh perdido deixando as requisicoes em um estado inconsistente pois nada pode
				// ser feito. Portanto se o job nao existe (igual a nulo) entao todo o processamento
				// eh reiniciando mudando o status do registro na tabela de interface e removendo
				// o registro da tabela de job jah que este nao serve para mais nada
				// OBS Importante. A parte do codigo abaixo e uma solucao temporaria para resolver a problematica
				// da numeracao de vouchers vindas do SAP devido o tratamento incorreto de caixa-lote por esse 
				// sistema e entao ao mandar as ativacoes as mesmas entram em conflito na numeracao de batch
				// Erro 3304 e status invalido de batch.
				if(job == null || (job != null && job.getOpState() == 3304))
				{
					// Apaga o registro de job na tabela pois o mesmo sera reprocessado
					VoucherDAO.removeStatusJob(conexaoPrep, idProcesso, idProcessamento,eventoNegocio);
					// Atualiza o status na tabela de interface para indicar que o mesmo
					// deve ser reprocessado
					VoucherDAO.atualizaStatusInterface(conexaoPrep, idProcesso, Definicoes.IDT_PROCESSAMENTO_NOT_OK, idProcessamento, eventoNegocio);
//atualizaStatusInterface(conexaoPrep,Definicoes.IDT_PROCESSAMENTO_NOT_OK,idProcessamento,eventoNegocio);
				}
				else
					// Caso o job tenha terminado entao independente do resultado,
					// finaliza o processamento junto a tabela de interface para
					// retorno do resultado ao sistema SAP. Caso nao tenha terminado
					// nao faz nada tentando verificar da proxima vez
					if(job != null && (job.jobTerminadoSucesso() || job.jobTerminadoErro()))
						// Finaliza o registro de requisicao de ativacao de voucher
						finalizaProcessoAtivacao(conexaoPrep, job, idProcessamento, eventoNegocio);

				// Efetiva a transacao para cada linha processada da tabela de job
				conexaoPrep.commit();
			}
		}
		catch(Exception e)
		{
			poolLog.log(idProcesso,Definicoes.WARN,"VerificaJobAtivacao","verificaJobs","Erro ao listar jobs pendentes da ativacao. Erro"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
	
	/**
	 * Metodo....:getSolicitacao
	 * Descricao.:Retorna um objeto solicitacao a partir de sua identificacao unica
	 * @param idProcessamento	- Id processamento da requisicao
	 * @param eventoNegocio		- Evento de negocio dessa requisicao
	 * @return SolicitacaoAtivacao	- Objeto contendo as informacoes da requisicao de ativacao
	 */
	public SolicitacaoAtivacao getSolicitacao(long idProcessamento, String eventoNegocio)
	{
		SolicitacaoAtivacao solicitacao = null;
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sql = "SELECT ID_PROCESSAMENTO, XML_DOCUMENT " +
						   "FROM TBL_INT_PPP_IN " +
						  "WHERE ID_PROCESSAMENTO = ? " +
						  	"AND IDT_EVENTO_NEGOCIO = ? ";
			Object param[] = {new Long(idProcessamento), eventoNegocio};
			ResultSet rs = conexaoPrep.executaPreparedQuery(sql, param, idProcesso);
			if (rs.next())
				solicitacao = getDadosSolicitacao(rs.getLong("ID_PROCESSAMENTO"),rs.getClob("XML_DOCUMENT"));
		}
		catch(Exception e)
		{
			poolLog.log(idProcesso, Definicoes.WARN, "VerificaJobAtivacao", "getSolicitacao", "Erro ao buscar informacoes da solicitacao. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return solicitacao;
	}
	
	/**
	 * Metodo....:getDadosSolicitacao
	 * Descricao.:Retorna os dados da solicitacao de ativacao de voucher
	 * @param idProcessamento	- Id da requisicao para a tabela de interface
	 * @param xmlRequisicao		- Dados do XML da requisicao
	 * @return SolicitacaoAtivacao - Dados da solicitacao
	 */
	private SolicitacaoAtivacao getDadosSolicitacao(long idProcessamento, Clob xmlRequisicao)
	{
		SolicitacaoAtivacao solicitacao = null;
		try
		{
			// Realiza a leitura do objeto CLOB para posteriormente executar
			// o parse do XML de requisicao afim de identificar as propriedades
		    Reader chr_instream;
		    char chr_buffer[] = new char[(int)xmlRequisicao.length()];
			chr_instream = xmlRequisicao.getCharacterStream();
			chr_instream.read(chr_buffer);
			
			solicitacao = ManipuladorXMLVoucher.parseXMLRequisicao(idProcesso, new String(chr_buffer));
			solicitacao.setIdRequisicao (idProcessamento);
			solicitacao.setEventoNegocio(Definicoes.IDT_EVT_NEGOCIO_VOUCHER);
		}
		catch(Exception se)
		{
			
			poolLog.log(idProcesso, Definicoes.WARN, "VerificaJobAtivacao", "getDadosSolicitacao", "Erro ao realizar o parse das informacoes da requisicao. Erro:"+se);
		}
		return solicitacao;
	}
}
