package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.ORB;

import TINC.Pi_exception;
import TINC.TDateTime;
import TINC.TDateTimeHelper;
import TINC.TVMInterface;
import TINC.TVMJobMgr;
import TINC.TVM_VOUCHER_NO_DOES_NOT_EXIST;
import TINC.recordSeqHolder;
import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.TVMActivateBatchRangePSeqPackage.TVMActivateBatchRangePSeqAttributes;
import TINC.TVMActivateOrderRangePSeqPackage.TVMActivateOrderRangePSeqAttributes;
import TINC.TVMCreateOrderPSeqPackage.TVMCreateOrderPSeqAttributes;
import TINC.TVMCreateOrderRSeqPackage.TVMCreateOrderRSeqAttributes;
import TINC.TVMGetBatchesPerBoxPSeqPackage.TVMGetBatchesPerBoxPSeqAttributes;
import TINC.TVMGetBatchesPerBoxRSeqPackage.TVMGetBatchesPerBoxRSeqAttributes;
import TINC.TVMGetBoxesPerOrderItemPSeqPackage.TVMGetBoxesPerOrderItemPSeqAttributes;
import TINC.TVMGetBoxesPerOrderPSeqPackage.TVMGetBoxesPerOrderPSeqAttributes;
import TINC.TVMGetBoxesPerOrderRSeqPackage.TVMGetBoxesPerOrderRSeqAttributes;
import TINC.TVMGetJobStatsPSeqPackage.TVMGetJobStatsPSeqAttributes;
import TINC.TVMGetJobStatsRSeqPackage.TVMGetJobStatsRSeqAttributes;
import TINC.TVMGetNewOrderNoPSeqPackage.TVMGetNewOrderNoPSeqAttributes;
import TINC.TVMGetOrderDetailsPSeqPackage.TVMGetOrderDetailsPSeqAttributes;
import TINC.TVMGetOrderDetailsRSeqPackage.TVMGetOrderDetailsRSeqAttributes;
import TINC.TVMGetVoucherDetailsPSeqPackage.TVMGetVoucherDetailsPSeqAttributes;
import TINC.TVMGetVoucherDetailsRSeqPackage.TVMGetVoucherDetailsRSeqAttributes;
import TINC.TVMIssueOrderRangePSeqPackage.TVMIssueOrderRangePSeqAttributes;
import TINC.TVMReceiveOrderPSeqPackage.TVMReceiveOrderPSeqAttributes;
import TINC.TVMUpdateBatchPSeqPackage.TVMUpdateBatchPSeqAttributes;
import TINC.TVMUpdateVoucherPSeqPackage.TVMUpdateVoucherPSeqAttributes;

import com.brt.gpp.aplicacoes.consultar.Voucher;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdem;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemCaixa;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemLote;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedidoItem;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenVoucher;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DataTEC;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;
import com.brt.gpp.comum.mapeamentos.MapValoresVoucher;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;
import com.brt.gpp.gerentesPool.GerenteAutenticadorTecnomen;
import com.brt.gpp.gerentesPool.GerenteORB;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 * 	Esta classe e a responsavel por abstrair os metodos da API do voucher management.
 * Atraves dessa classe as operacoes de criacao de ordem, finalizaca, ativacao e recebimento
 * da ordem de voucher podem ser executadas abstraindo todo a complexidade da criacao
 * de objetos para interface via CORBA com a plataforma Tecnomen.
 * 	Esta classe utiliza a conexao TecnomenVoucherMgmt para executar efetivamente na plataforma
 * as chamadas de funcao.
 * 
 * @author Joao Carlos
 * Data..: 20-Janeiro-2005
 *
 * Adaptado para controle total por: Bernardo Vergne Dias
 * 06/07/2007
 */
public class OperacoesVoucher
{
	private TecnomenVoucher 	conexaoVoucher	 	= null;
	private GerentePoolLog		log					= null;
	private long				idProcesso			= 0;
	private ORB					orb					= null;
	private GerentePoolTecnomen	gerPoolTec			= null;
	
	/**
	 * Metodo....:OperacoesVoucher
	 * Descricao.:Construtor da classe
	 * @param idProcesso	- Id do processo que esta construindo a classe
	 */
	public OperacoesVoucher(long idProcesso)
	{
		this.idProcesso = idProcesso;
		this.log		= GerentePoolLog.getInstancia(this.getClass());
		orb				= GerenteORB.getInstance().getOrb();
		gerPoolTec		= GerentePoolTecnomen.getInstancia(idProcesso);
	}

	/**
	 *	Obtem as informacoes de autenticacao da conexao de voucher, que sao utilizadas durante o processo de criacao 
	 *	e ativacao de vouchers.
	 * 
	 *	@return		Informacoes de autenticacao da conexao de voucher.
	 */
	private Autenticador getAutenticador() 
	{
		return GerenteAutenticadorTecnomen.getInstance().getAutenticador(ConexaoTecnomen.VOUCHER);
	}
	
	/**
	 * Metodo.....:getTVMInterface
	 * Descricao..:Obtem a referencia do Voucher Management atraves da conexao realizada 
	 * @return TVMInterface	- Referencia para executar as operacoes de gerenciamento de voucher
	 * @throws GPPInternalErrorException
	 */
	private TVMInterface getTVMInterface() throws GPPInternalErrorException
	{
		TVMInterface retorno = null;
		try
		{
			conexaoVoucher = gerPoolTec.getTecnomenVoucher(idProcesso);
			retorno = conexaoVoucher.getTvmInterface();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException(e.toString());
		}
		return retorno;
	}
	
	/**
	 * Metodo....:reconecta
	 * Descricao.:Realiza a reconexao com o voucher server
	 * @return boolean - Indica se coneguiu ou nao a reconexao
	 */
	private boolean reconecta()
	{
		try
		{
			if (conexaoVoucher != null)
				conexaoVoucher.reconectar();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	/**
	 * Metodo....:getTVMJobMgr
	 * Descricao.:Retorna uma referencia para as consultas a job na plataforma tecnomen
	 * @return TVMJobMgr - Referencia para a job manager
	 * @throws GPPInternalErrorException
	 */
	private TVMJobMgr getTVMJobMgr() throws GPPInternalErrorException
	{
		TVMJobMgr retorno = null;
		try
		{
			conexaoVoucher = gerPoolTec.getTecnomenVoucher(idProcesso);
			retorno = conexaoVoucher.getTvmJobMgr();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException(e.toString());
		}
		return retorno;
	}

	/**
	 * Metodo....:releaseTVMInterface
	 * Descricao.:Devolve a interface de operacoes de voucher ao pool de conexoes disponiveis
	 * OBS: Este metodo deve ser executado para cada operacao
	 *
	 */
	private void releaseTVMInterface()
	{
		if (conexaoVoucher != null)
			gerPoolTec.liberaConexaoVoucher(conexaoVoucher,idProcesso);
	}

	/**
	 * Metodo.....:getProximoNumeroOrdem
	 * Descricao..:Este metodo realiza a busca na plataforma pelo proximo numero
	 *             de ordem disponivel. Esse metodo e sincronizado para evitar
	 *             que dois processos peguem o mesmo numero de ordem
	 * @return long - Numero de ordem disponivel para criacao
	 * @throws GPPInternalErrorException
	 */
	public static synchronized long getProximoNumeroOrdem(long idProcesso) throws GPPInternalErrorException
	{
		long numOrdem = 0;
		OperacoesVoucher opVoucher    = new OperacoesVoucher(idProcesso);
		Autenticador     autenticador = opVoucher.getAutenticador();
		GerentePoolLog	 log	      = GerentePoolLog.getInstancia(OperacoesVoucher.class);
		try
		{
			ORB orb = GerenteORB.getInstance().getOrb();
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMGetNewOrderNoPSeqAttributes._LastOne]);
			//Numero da ordem.
			pSeq.value[0] = new tincRecord(TVMGetNewOrderNoPSeqAttributes._OrderNo, orb.create_any());
			//ID do operador. Obtido a partir do autenticador.
			pSeq.value[1] = new tincRecord(TVMGetNewOrderNoPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
			//ID do login. Igual ao ID do operador.
			pSeq.value[2] = new tincRecord(TVMGetNewOrderNoPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());
			int result = opVoucher.getTVMInterface().getNewOrderNo(pSeq);
			
			numOrdem = pSeq.value[0].value.extract_long();
			opVoucher.releaseTVMInterface();
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getProximoNumeroOrdem","Erro ao buscar proximo numero de ordem. Erro:"+pi.getMessage());
			throw new GPPInternalErrorException("Erro de conexao a tecnomen. Erro:"+pi);
		}		
		return numOrdem;
	}
	
	/**
	 * Metodo....:getTDateTime
	 * Descricao.:Retorna um objeto TDateTime a partir de uma data
	 * @param data - Data desejada a ser transformada no objeto TDateTime
	 * @return TDateTime - Data transformada para este objeto
	 */
	private TDateTime getTDateTime(Date data)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		TDateTime retorno = new TDateTime((short)cal.get(Calendar.DAY_OF_MONTH)
				 						 ,(short)(cal.get(Calendar.MONTH)+1)
										 ,(short)cal.get(Calendar.YEAR)
										 ,(short)cal.get(Calendar.MINUTE)
										 ,(short)cal.get(Calendar.SECOND)
										 ,(short)cal.get(Calendar.HOUR)
				                         );
		return retorno;
	}

	/**
	 * Metodo....:criaOrdemVoucher
	 * Descricao.:Cria a ordem de voucher na plataforma tecnomen
	 * @param pedido - Objeto VoucherPedido contendo as informacoes do pedido de voucher
	 * @return int - Codigo do Job que esta executando a tarefa ou entao um codigo de erro
	 */
	public int criaOrdemVoucher(VoucherPedido pedido)
	{
		int codigoRetorno = -1;
		try
		{
			Autenticador autenticador = this.getAutenticador();
			MapConfiguracaoGPP confGpp = MapConfiguracaoGPP.getInstancia();
			// Prepara parametros para a criacao do cabecalho da ordem
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMCreateOrderPSeqAttributes._LastOne]);
			// Numero da Ordem
			pSeq.value[0] = new tincRecord(TVMCreateOrderPSeqAttributes._OrderNo, orb.create_any());
			pSeq.value[0].value.insert_long((int)pedido.getNumOrdem());
			// Nome do arquivo
			pSeq.value[1] = new tincRecord(TVMCreateOrderPSeqAttributes._FileName, orb.create_any());
			pSeq.value[1].value.insert_string(confGpp.getMapValorConfiguracaoGPP("NOM_ARQUIVOS_ORDENS_VOUCHER"));
			// Parametro Opcional - FileKey
			pSeq.value[2] = new tincRecord(TVMCreateOrderPSeqAttributes._FileKey, orb.create_any());
			pSeq.value[2].value.insert_string(confGpp.getMapValorConfiguracaoGPP("NOM_ARQUIVOS_ORDENS_VOUCHER"));
			// Data de criacao
			pSeq.value[3] = new tincRecord(TVMCreateOrderPSeqAttributes._DueDate, orb.create_any());
			TDateTimeHelper.insert(pSeq.value[3].value, getTDateTime(pedido.getDataCriacao()));
			// Parametro Opctional - Data de criacao
			pSeq.value[4] = new tincRecord(TVMCreateOrderPSeqAttributes._OrderedDate, orb.create_any());
			TDateTimeHelper.insert(pSeq.value[4].value, getTDateTime(pedido.getDataCriacao()));
			// Login do Usuario
			pSeq.value[5] = new tincRecord(TVMCreateOrderPSeqAttributes._OrderedBy, orb.create_any());
			pSeq.value[5].value.insert_string(autenticador.getLogin());
			// Parametro Opcional - Data de inicio do Job
			Calendar dtAtual = Calendar.getInstance();
			pSeq.value[6] = new tincRecord(TVMCreateOrderPSeqAttributes._JobWindowStartTime, orb.create_any());
			TDateTimeHelper.insert(pSeq.value[6].value, getTDateTime(dtAtual.getTime()));
			// Parametro Opcional - Data final do job
			dtAtual.add(Calendar.MINUTE,1);
			pSeq.value[7] = new tincRecord(TVMCreateOrderPSeqAttributes._JobWindowEndTime, orb.create_any());
			TDateTimeHelper.insert(pSeq.value[7].value, getTDateTime(dtAtual.getTime()));
			// Parametro Opcional - Operator ID
			pSeq.value[8] = new tincRecord(TVMCreateOrderPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[8].value.insert_long(autenticador.getIdUsuario());
			// Parametro Opcional - Login ID. Igual ao Operator ID.
			pSeq.value[9] = new tincRecord(TVMCreateOrderPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[9].value.insert_long(autenticador.getIdUsuario());
			// Parametro Opcional - Origin ID. Nao utilizado.
			pSeq.value[10] = new tincRecord(TVMCreateOrderPSeqAttributes._OriginId, orb.create_any());
			pSeq.value[10].value.insert_long(0);
			
			// Define a data de expiracao dos cartoes a serem criados
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dataExpiracao = sdf.parse(confGpp.getMapValorConfiguracaoGPP("DATA_EXPIRACAO_CARTOES"));
			// Neste ponto todos os itens do pedido sao lidos para que
			// seja gerado os parametros para a criacao dos itens da ordem
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[pedido.getNumElementosParaOrdem()][TVMCreateOrderRSeqAttributes._LastOne]);
			// Realiza a criacao dos parametros para cada item do pedido
			int numLinha = 0;
			for (Iterator i=pedido.getItensPedido().iterator(); i.hasNext();)
			{
				// Adiciona o item a lista de valores para criar a ordem
				VoucherPedidoItem item = (VoucherPedidoItem)i.next();
				rSeq.value[numLinha] = getValoresItem(item, numLinha, dataExpiracao);
				// Caso o item tenha sub-item entao para a criacao de ordem
				// e como se fosse uma nova linha
				numLinha++;
				if (item.getSubItem() != null)
				{
					rSeq.value[numLinha] = getValoresItem(item.getSubItem(), numLinha, dataExpiracao);
					// Caso contenha sub-item entao a numeracao de linha e novamente
					// acrescida de um para contemplar o proximo item
					numLinha++;
				}
			}
	
			codigoRetorno = getTVMInterface().createOrder(pSeq,rSeq);
		}
		catch(ParseException pe)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","criaOrdemVoucher","Erro na conversao da data de expiracao dos cartoes");
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","criaOrdemVoucher","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","criaOrdemVoucher","Erro ao criar a ordem numero:"+pedido.getNumOrdem()+" do pedido numero:"+pedido.getNumPedido()+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","criaOrdemVoucher","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codigoRetorno;
	}
	
	/**
	 * Metodo....:getValoresItem
	 * Descricao.:Retorna o objeto CORBA para armazenamento dos valores do item
	 * @param item			- Item de pedido
	 * @param numLinha		- Numero da linha a ser criado no item do pedido
	 * @param dataExpiracao	- Data de expiracao a ser utilizada nos cartoes
	 * @return tincRecord[] - Array de valores contendo os atributos do item
	 */
	private tincRecord[] getValoresItem(VoucherPedidoItem item, int numLinha, Date dataExpiracao)
	{
		Autenticador autenticador = this.getAutenticador();
		
		// OBS Importante.
		// Os valores de face para cada saldo esta no objeto VoucherPedidoItem com os valores
		// a serem inseridos no credito, porem ao cadastrar na tecnomen a ordem de cartoes
		// devera ser utilizado um ID assim como no GPP para identificar quais valores serao
		// recarregados em cada saldo. Esta caracteristica na tecnomen chama-se Voucher
		// Denominations e portanto caso algum valor seja alterado ou criado portanto nessa
		// tabela da tecnomen tambem devera ser atualizada. Na criacao da ordem o valor de face
		// e utilizado em todos os valores de saldo devido ser o id desejado
		
		// Alteracao em 27-Mar-2006 por Joao Carlos.
		// O valor do SM FACE VALUE serah diferente para os vouchers FISICOS e LIGMIX,VIRTUAL
		// o valor serah zero para o fisico e igual ao FACE VALUE default para os demais
		// 
		tincRecord tRec[] = new tincRecord[TVMCreateOrderRSeqAttributes._LastOne];
		// Numero da Ordem
		tRec[0] = new tincRecord(TVMCreateOrderRSeqAttributes._OrderNo, orb.create_any());
		tRec[0].value.insert_long((int)item.getPedido().getNumOrdem());
		// Numero do Item
		tRec[1] = new tincRecord(TVMCreateOrderRSeqAttributes._ItemNo, orb.create_any());
		tRec[1].value.insert_long(numLinha+1);
		// Quantidade
		tRec[2] = new tincRecord(TVMCreateOrderRSeqAttributes._ItemQty, orb.create_any());
		tRec[2].value.insert_long((int)item.getQtdeCartoes());
		// Tipo do voucher
		// Se o tipo do cartao for LigMix entao o VoucherType utilizado eh outro diferente
		// dos tipos Virtuais e Fisicos
		short tipVoucher = Definicoes.TIPO_CARTAO_LIGMIX.equals(item.getPedido().getTipCartao()) ? 
				           Definicoes.VOUCHER_TYPE_LIGMIX : Definicoes.VOUCHER_TYPE;
		tRec[3] = new tincRecord(TVMCreateOrderRSeqAttributes._VoucherType, orb.create_any());
		tRec[3].value.insert_short(tipVoucher);
		//SubVoucherType. Nao utilizado.
		tRec[4] = new tincRecord(TVMCreateOrderRSeqAttributes._SubVoucherType, orb.create_any());
		tRec[4].value.insert_short((short)0);
		// Qtde por caixa
		tRec[5] = new tincRecord(TVMCreateOrderRSeqAttributes._PerBoxQty, orb.create_any());
		tRec[5].value.insert_long((int)item.getQtdeBatchPorCaixa());
		// Qtde por lote
		tRec[6] = new tincRecord(TVMCreateOrderRSeqAttributes._PerBatchQty, orb.create_any());
		tRec[6].value.insert_long((int)item.getQtdePorBatch());
		// Valor de face
		tRec[7] = new tincRecord(TVMCreateOrderRSeqAttributes._FaceValue, orb.create_any());
		tRec[7].value.insert_long((int)item.getValorFace());
		// Codigo da moeda
		tRec[8] = new tincRecord(TVMCreateOrderRSeqAttributes._CurrencyCode, orb.create_any());
		tRec[8].value.insert_short(Definicoes.VOUCHER_CURRENCY_CODE);
		// Data de expiracao
		tRec[9] = new tincRecord(TVMCreateOrderRSeqAttributes._ExpiryDate, orb.create_any());
		TDateTimeHelper.insert(tRec[9].value, getTDateTime(dataExpiracao));
		// Art Code
		tRec[10] = new tincRecord(TVMCreateOrderRSeqAttributes._ArtCode, orb.create_any());
		tRec[10].value.insert_short(Definicoes.VOUCHER_ART_CODE);
		// TariffPlanId
		tRec[11] = new tincRecord(TVMCreateOrderRSeqAttributes._TariffPlanId, orb.create_any());
		tRec[11].value.insert_short(Definicoes.VOUCHER_TARIFF_PLAN_ID);
		// Valor de bonus
		tRec[12] = new tincRecord(TVMCreateOrderRSeqAttributes._BonusFaceValue, orb.create_any());
		tRec[12].value.insert_long((int)item.getValorFace());
		// Data de expiracao de bonus
		//tRec[12] = new tincRecord(TVMCreateOrderRSeqAttributes._BonusExpiryDate, orb.create_any());
		//TDateTimeHelper.insert(tRec[12].value, getTDateTime(dataExpiracao));
		// Valor de Dados
		tRec[13] = new tincRecord(TVMCreateOrderRSeqAttributes._DataFaceValue, orb.create_any());
		tRec[13].value.insert_long((int)item.getValorFace());
		// Valor de Bonus de Dados
		tRec[14] = new tincRecord(TVMCreateOrderRSeqAttributes._DataBonusFaceValue, orb.create_any());
		tRec[14].value.insert_long((int)item.getValorFace());
		// Data Package ID
		tRec[15] = new tincRecord(TVMCreateOrderRSeqAttributes._DataPackageId, orb.create_any());
		tRec[15].value.insert_short((short)0);

		// MODIFICACAO FEITA POR JOAO CARLOS EM 19/04/2006 A PEDIDO DO RENATO PICANCO
		// DEVE SER ENVIADO NO VALOR DE FACE DO SM O VALOR CADADASTRADO + 1 EX: PARA O CARTAO
		// DE R$15 DEVE SER ENVIADO O FACE VALUE SM COMO 16
		tRec[16] = new tincRecord(TVMCreateOrderRSeqAttributes._SmFaceValue, orb.create_any());
		tRec[16].value.insert_long(((int)item.getValorFace())+1);
		// Valor de Bonus de SMS
		tRec[17] = new tincRecord(TVMCreateOrderRSeqAttributes._SmBonusFaceValue, orb.create_any());
		tRec[17].value.insert_long(((int)item.getValorFace())+1);
		// SM Package ID
		tRec[18] = new tincRecord(TVMCreateOrderRSeqAttributes._SmPackageId, orb.create_any());
		tRec[18].value.insert_short((short)0);
		// Operator ID
		tRec[19] = new tincRecord(TVMCreateOrderRSeqAttributes._OperatorId, orb.create_any());
		tRec[19].value.insert_long(autenticador.getIdUsuario());
		// Login ID
		tRec[20] = new tincRecord(TVMCreateOrderRSeqAttributes._LoginId, orb.create_any());
		tRec[20].value.insert_long(autenticador.getIdUsuario());
		// Package ID
		tRec[21] = new tincRecord(TVMCreateOrderRSeqAttributes._PackageId, orb.create_any());
		tRec[21].value.insert_short((short)0);
		// Data de expiracao de Sm
		//tRec[14] = new tincRecord(TVMCreateOrderRSeqAttributes._SmExpiryDate, orb.create_any());
		//TDateTimeHelper.insert(tRec[14].value, getTDateTime(dataExpiracao));
		// Data de expiracao de Dados
		//tRec[16] = new tincRecord(TVMCreateOrderRSeqAttributes._DataExpiryDate, orb.create_any());
		//TDateTimeHelper.insert(tRec[16].value, getTDateTime(dataExpiracao));
		
		return tRec;
	}

	/**
	 * Metodo....:recebeOrdemVoucher
	 * Descricao.:Recebe a ordem passada como parametro retornando o numero do job na plataforma
	 *            que esta processando a requisicao
	 * @param numeroOrdem - Numero da ordem a ser recebida
	 * @return int - Numero do Job na tecnomen ou codigo de erro
	 */
	public int recebeOrdemVoucher(long numeroOrdem)
	{
		int codRetorno = -1;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			tincSeqHolder 	pSeq = new tincSeqHolder(new tincRecord[TVMReceiveOrderPSeqAttributes._LastOne]);
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
			// Os parametros para receber a ordem sao:
			// 0 - Numero da ordem
			// 1 - Login do usuario
			// 2 - ID do operador
			// 3 - ID do login
			// Numero da ordem
			pSeq.value[0] = new tincRecord(TVMReceiveOrderPSeqAttributes._OrderNo, orb.create_any());
			pSeq.value[0].value.insert_long((int)numeroOrdem);
			// Login do usuario
			pSeq.value[1] = new tincRecord(TVMReceiveOrderPSeqAttributes._ReceivedBy, orb.create_any());
			pSeq.value[1].value.insert_string(autenticador.getLogin());
			// ID do Operador.
			pSeq.value[2] = new tincRecord(TVMReceiveOrderPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());
			// ID do Login. Igual ao ID do Operador.
			pSeq.value[3] = new tincRecord(TVMReceiveOrderPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[3].value.insert_long(autenticador.getIdUsuario());
	
			codRetorno = getTVMInterface().receiveOrder(pSeq,rSeq);
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","recebeOrdemVoucher","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","recebeOrdemVoucher","Erro ao receber a ordem numero:"+numeroOrdem+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","recebeOrdemVoucher","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codRetorno;
	}
	
	/**
	 * Metodo....:finalizaOrdemVoucher
	 * Descricao.:Este metodo realiza a finalizacao da ordem (ISSUED) na plataforma
	 * @param numeroOrdem - Numero da ordem a ser finalizada
	 * @return int - Numero do Job na tecnomen ou codigo de erro
	 */
	public int finalizaOrdemVoucher(long numeroOrdem)
	{
		int codRetorno = -1;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMIssueOrderRangePSeqAttributes._LastOne]);
			// Os parametros para passar a ordem para issued sao:
			// 0 - Numero da ordem inicial
			// 1 - Numero da ordem final
			// 2 - Login do usuario
			// 3 - Login do usuario (voucher)
			// 4 - ID do operador
			// 5 - ID do usuario
			// Ordem Inicial
			pSeq.value[0] = new tincRecord(TVMIssueOrderRangePSeqAttributes._OrderNoBegin, orb.create_any());
			pSeq.value[0].value.insert_long((int)numeroOrdem);
			// Ordem Final
			pSeq.value[1] = new tincRecord(TVMIssueOrderRangePSeqAttributes._OrderNoEnd, orb.create_any());
			pSeq.value[1].value.insert_long((int)numeroOrdem);
			// Login do usuario
			pSeq.value[2] = new tincRecord(TVMIssueOrderRangePSeqAttributes._IssuedBy, orb.create_any());
			pSeq.value[2].value.insert_string(autenticador.getLogin());
			// Login do usuario (agent)
			// 18/05/2007 - Daniel Ferreira: Login do usuario mudou de agent para voucher (vgpp).
			pSeq.value[3] = new tincRecord(TVMIssueOrderRangePSeqAttributes._IssuedTo, orb.create_any());
			pSeq.value[3].value.insert_string(autenticador.getLogin());
			// ID do Operador.
			pSeq.value[4] = new tincRecord(TVMIssueOrderRangePSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[4].value.insert_long(autenticador.getIdUsuario());
			// Login do usuario. Igul ao ID do Operador.
			pSeq.value[5] = new tincRecord(TVMIssueOrderRangePSeqAttributes._LoginId, orb.create_any());
			pSeq.value[5].value.insert_long(autenticador.getIdUsuario());
			
			codRetorno = getTVMInterface().issueOrderRange(pSeq);
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","finalizaOrdemVoucher","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","finalizaOrdemVoucher","Erro ao finalizar (ISSUED) a ordem numero:"+numeroOrdem+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","finalizaOrdemVoucher","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codRetorno;
	}
	
	/**
	 * Metodo....:ativaOrdemVoucher
	 * Descricao.:Este metodo realiza a ativacao da ordem na plataforma
	 * @param numeroOrdem - Numero da ordem a ser ativada
	 * @return int - Numero do Job na tecnomen ou codigo de erro
	 */
	public int ativaOrdemVoucher(long numeroOrdem)
	{
		int codRetorno = -1;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMActivateOrderRangePSeqAttributes._LastOne]);
			// Os parametros para passar ativar a ordem sao:
			// 0 - Numero da ordem inicial
			// 1 - Numero da ordem final
			// 2 - Login do usuario
			// 3 - Login do usuario (voucher)
			// 4 - ID do Operador.
			// 5 - ID do Login. 
			// Ordem Inicial
			pSeq.value[0] = new tincRecord(TVMActivateOrderRangePSeqAttributes._OrderNoBegin, orb.create_any());
			pSeq.value[0].value.insert_long((int)numeroOrdem);
			// Ordem Final
			pSeq.value[1] = new tincRecord(TVMActivateOrderRangePSeqAttributes._OrderNoEnd, orb.create_any());
			pSeq.value[1].value.insert_long((int)numeroOrdem);
			// Login do usuario
			pSeq.value[2] = new tincRecord(TVMActivateOrderRangePSeqAttributes._IssuedBy, orb.create_any());
			pSeq.value[2].value.insert_string(autenticador.getLogin());
			// Login do usuario (voucher)
			pSeq.value[3] = new tincRecord(TVMActivateOrderRangePSeqAttributes._IssuedTo, orb.create_any());
			pSeq.value[3].value.insert_string(autenticador.getLogin());
			// ID do Operador.
			pSeq.value[4] = new tincRecord(TVMActivateOrderRangePSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[4].value.insert_long(autenticador.getIdUsuario());
			// ID do Login. Igual ao ID do Operador.
			pSeq.value[5] = new tincRecord(TVMActivateOrderRangePSeqAttributes._LoginId, orb.create_any());
			pSeq.value[5].value.insert_long(autenticador.getIdUsuario());

			codRetorno = getTVMInterface().activateOrderRange(pSeq);
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","ativaOrdemVoucher","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","ativaOrdemVoucher","Erro ao ativar a ordem numero:"+numeroOrdem+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","ativaOrdemVoucher","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codRetorno;
	}
	
	/**
	 * Metodo....:ativaFaixaBatch
	 * Descricao.:Realiza a ativacao de vouchers por faixa de valores de batch
	 * @param numBatchIni - Numero do baatch inicial
	 * @param numBatchFim - Numero do batch final
	 * @return int		  - Numero do job ou codigo de retorno
	 */
	public int ativaFaixaBatch(String numBatchIni, String numBatchFim)
	{
		int codRetorno = -1;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMActivateBatchRangePSeqAttributes._LastOne]);
			// Os parametros para passar ativar a ordem sao:
			// 0 - Numero do batch inicial
			// 1 - Numero do batch final
			// 2 - Login do usuario
			// 3 - Login do usuario (voucher)
			// 4 - ID do operador
			// 5 - ID do login
			// Ordem Inicial
			pSeq.value[0] = new tincRecord(TVMActivateBatchRangePSeqAttributes._BatchBegin, orb.create_any());
			pSeq.value[0].value.insert_string(numBatchIni);
			// Ordem Final
			pSeq.value[1] = new tincRecord(TVMActivateBatchRangePSeqAttributes._BatchEnd, orb.create_any());
			pSeq.value[1].value.insert_string(numBatchFim);
			// Login do usuario
			pSeq.value[2] = new tincRecord(TVMActivateBatchRangePSeqAttributes._IssuedBy, orb.create_any());
			pSeq.value[2].value.insert_string(autenticador.getLogin());
			// Login do usuario (voucher)
			pSeq.value[3] = new tincRecord(TVMActivateBatchRangePSeqAttributes._IssuedTo, orb.create_any());
			pSeq.value[3].value.insert_string(autenticador.getLogin());
			// ID do Operador.
			pSeq.value[4] = new tincRecord(TVMActivateBatchRangePSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[4].value.insert_long(autenticador.getIdUsuario());
			// ID do Login. Igual ao ID do Operador.
			pSeq.value[5] = new tincRecord(TVMActivateBatchRangePSeqAttributes._LoginId, orb.create_any());
			pSeq.value[5].value.insert_long(autenticador.getIdUsuario());

			codRetorno = getTVMInterface().activateBatchRange(pSeq);
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","ativaFaixaBatch","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","ativaFaixaBatch","Erro ao ativar faixa de batch:"+numBatchIni+" a "+ numBatchFim+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","ativaFaixaBatch","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codRetorno;
	}

	/**
	 * Metodo....:atualizaStatusBatch
	 * Descricao.:Atualiza status do batch. Este metodo sera utilizado no gerenciamento de vouchers
	 *            para cancelamento de um lote
	 * @param numeroBatch - Numero do lote
	 * @param statusBatch - Status a ser alterado
	 * @return int		  - Numero do Job gerado pela Tecnomen
	 */
	public int atualizaStatusBatch(String numeroBatch, int statusBatch, String comentario)
	{
		int codRetorno = -1;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMUpdateBatchPSeqAttributes._LastOne]);
			// Os parametros necessarios para atualizar o status do batch sao
			// 0 - Numero do batch
			// 1 - Comentario
			// 2 - Status do batch
			// 3 - IssuedBy - Login do usuario
			// 4 - IssuedTo - Login do usuario (voucher)
			// 5 - ID do operador
			// 6 - ID do Login
			// Numero do batch
			pSeq.value[0] = new tincRecord(TVMUpdateBatchPSeqAttributes._BatchNo,orb.create_any());
			pSeq.value[0].value.insert_string(numeroBatch);
			// Comentario
			pSeq.value[1] = new tincRecord(TVMUpdateBatchPSeqAttributes._Comment,orb.create_any());
			pSeq.value[1].value.insert_string(comentario);
			// Status do batch
			pSeq.value[2] = new tincRecord(TVMUpdateBatchPSeqAttributes._Status,orb.create_any());
			pSeq.value[2].value.insert_short((short)statusBatch);
			// IssuedBy
			pSeq.value[3] = new tincRecord(TVMUpdateBatchPSeqAttributes._IssuedBy,orb.create_any());
			pSeq.value[3].value.insert_string(autenticador.getLogin());
			// IssuedTo
			pSeq.value[4] = new tincRecord(TVMUpdateBatchPSeqAttributes._IssuedTo,orb.create_any());
			pSeq.value[4].value.insert_string(autenticador.getLogin());
			// ID do operador.
			pSeq.value[5] = new tincRecord(TVMUpdateBatchPSeqAttributes._OperatorId,orb.create_any());
			pSeq.value[5].value.insert_long(autenticador.getIdUsuario());
			// Login do operador.
			pSeq.value[6] = new tincRecord(TVMUpdateBatchPSeqAttributes._LoginId,orb.create_any());
			pSeq.value[6].value.insert_long(autenticador.getIdUsuario());

			codRetorno = getTVMInterface().updateBatch(pSeq);
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","atualizaStatusBatch","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","atualizaStatusBatch","Erro ao atualizar status do batch:"+numeroBatch+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","atualizaStatusBatch","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codRetorno;
	}

	/**
	 * Metodo....:atualizaStatusVoucher
	 * Descricao.:Atualiza o status do voucher. Esta funcao sera utilizada principalmente para
	 *            o cancelamento do voucher
	 * @param numeroVoucher - Numero do voucher a ser atualizado
	 * @param statusVoucher	- Status a ser definido
	 * @param comentario 	- Comentario
	 * @return
	 */
	public int atualizaStatusVoucher(String numeroVoucher, int statusVoucher, String comentario)
	{
		int codRetorno = -1;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMUpdateVoucherPSeqAttributes._LastOne]);
			// Os parametros necessarios para atualizar o status do batch sao
			// 0 - Numero do Voucher
			// 1 - Comentario
			// 2 - Status do Voucher
			// 3 - IssuedBy - Login do usuario
			// 4 - IssuedTo - Login do usuario (agent)
			// 5 - Password - Nao utilizado (Utiliza o comentario como password)
			// 6 - ID do operador
			// 7 - ID do login
			// Numero do batch
			pSeq.value[0] = new tincRecord(TVMUpdateVoucherPSeqAttributes._VoucherNo,orb.create_any());
			pSeq.value[0].value.insert_string(numeroVoucher);
			// Comentario
			pSeq.value[1] = new tincRecord(TVMUpdateVoucherPSeqAttributes._Comment,orb.create_any());
			pSeq.value[1].value.insert_string(comentario);
			// Status do batch
			pSeq.value[2] = new tincRecord(TVMUpdateVoucherPSeqAttributes._Status,orb.create_any());
			pSeq.value[2].value.insert_short((short)statusVoucher);
			// IssuedBy
			pSeq.value[3] = new tincRecord(TVMUpdateVoucherPSeqAttributes._IssuedBy,orb.create_any());
			pSeq.value[3].value.insert_string(autenticador.getLogin());
			// IssuedTo
			pSeq.value[4] = new tincRecord(TVMUpdateVoucherPSeqAttributes._IssuedTo,orb.create_any());
			pSeq.value[4].value.insert_string(autenticador.getLogin());
			// Password
			pSeq.value[5] = new tincRecord(TVMUpdateVoucherPSeqAttributes._Password,orb.create_any());
			pSeq.value[5].value.insert_string(comentario);
			// ID do operador.
			pSeq.value[6] = new tincRecord(TVMUpdateVoucherPSeqAttributes._OperatorId,orb.create_any());
			pSeq.value[6].value.insert_long(autenticador.getIdUsuario());
			// ID do Login.
			pSeq.value[7] = new tincRecord(TVMUpdateVoucherPSeqAttributes._LoginId,orb.create_any());
			pSeq.value[7].value.insert_long(autenticador.getIdUsuario());
			
			codRetorno = getTVMInterface().updateVoucher(pSeq);
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","atualizaStatusBatch","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","atualizaStatusBatch","Erro ao atualizar status do batch:"+numeroVoucher+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","atualizaStatusBatch","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return codRetorno;
	}

	/**
	 * Metodo....:getJobTecnomen
	 * Descricao.:Retorna os detalhes de um job na plataforma tecnomen
	 * @param numJob - Numero do Job a ser pesquisado
	 * @return JobTecnomen - Objeto contendo as informacoes do job
	 */
	public JobTecnomen getJobTecnomen(int numJob)
	{
		JobTecnomen  job          = null;
		Autenticador autenticador = this.getAutenticador();
		try
		{
			// Cria objeto para consulta na plataforma
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMGetJobStatsPSeqAttributes._LastOne]);
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
			// Parametro e o numero do Job
			pSeq.value[0] = new tincRecord(TVMGetJobStatsPSeqAttributes._JobNo, orb.create_any());
			pSeq.value[0].value.insert_long(numJob);
			// ID do Operador.
			pSeq.value[1] = new tincRecord(TVMGetJobStatsPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
			// ID do Login. Igual ao ID do Operador.
			pSeq.value[2] = new tincRecord(TVMGetJobStatsPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());
			// Realiza a consulta do Job na plataforma
			getTVMJobMgr().getJobStats(pSeq,rSeq);
			
			if (rSeq.value[0].length != 0)
			{
				// Extrai os resultados
				job = new JobTecnomen(numJob);
				tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[0]);
				job.setWorkTotal(dataSeq.value[TVMGetJobStatsRSeqAttributes._WorkTotal].value.extract_long());
				job.setWorkDone (dataSeq.value[TVMGetJobStatsRSeqAttributes._WorkDone].value.extract_long());
				job.setOpState  (dataSeq.value[TVMGetJobStatsRSeqAttributes._OpState].value.extract_long());
			}
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getJobTecnomen","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getJobTecnomen","Erro ao buscar job tecnomen job numero:"+numJob+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getJobTecnomen","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return job;
	}

	/**
	 * Metodo....:getCaixasDaOrdem
	 * Descricao.:Retorna a numeracao de caixas criadas para uma dada ordem e item
	 * @param numeroOrdem	- Numero da ordem a ser pesquisada
	 * @param numeroItem	- Numero do item da ordem
	 * @return long[]		- Numeros de caixas criadas para esta ordem e item
	 * @throws GPPInternalErrorException
	 */
	public long[] getCaixasDaOrdem(long numeroOrdem, long numeroItem) throws GPPInternalErrorException
	{
		long caixas[] = null;
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		Autenticador autenticador = this.getAutenticador();
		try
		{
			// Cria objeto para consulta na plataforma
			tincSeqHolder   pSeq = new tincSeqHolder(new tincRecord[TVMGetBoxesPerOrderItemPSeqAttributes._LastOne]);
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
			
			// Define os parametros de consulta
			// Numero da Ordem
			pSeq.value[0] = new tincRecord(TVMGetBoxesPerOrderItemPSeqAttributes._OrderNo, orb.create_any());
			pSeq.value[0].value.insert_long((int)numeroOrdem);
			// Numero do item.
			pSeq.value[1] = new tincRecord(TVMGetBoxesPerOrderItemPSeqAttributes._ItemNo, orb.create_any());
			pSeq.value[1].value.insert_long((int)numeroItem);
			// ID do Operador.
			pSeq.value[2] = new tincRecord(TVMGetBoxesPerOrderItemPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());
			// ID do Login.
			pSeq.value[3] = new tincRecord(TVMGetBoxesPerOrderItemPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[3].value.insert_long(autenticador.getIdUsuario());
			
			// Realiza a consulta na plataforma
			int codRetorno = getTVMInterface().getBoxesPerOrderItem(pSeq,rSeq);
			if (codRetorno == Definicoes.RET_OPERACAO_OK)
			{
				// Para cada linha retornada no objeto recordSeqHolder identifica os valores
				// existentes sendo que cada linha e um uma caixa diferente criada para a ordem
				caixas = new long[rSeq.value.length];
				for (int i=0; i < rSeq.value.length; i++)
				{
					// Extrai os valores de numeracao de caixa, sendo que o primeiro elemento
					// e o numero da caixa os outros valores sao irrelevantes nesse processo
					tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[i]);
					caixas[i] = dataSeq.value[0].value.extract_long();
				}
			}
			else
				log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getCaixasDaOrdem","Erro ao identificar numero de caixa da ordem "+numeroOrdem+". Erro:"+mapRetorno.getDescricao(codRetorno));
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getCaixasDaOrdem","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getCaixasDaOrdem","Erro ao pesquisar numero de caixa pela ordem. Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getCaixasDaOrdem","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return caixas;
	}
	
	/**
	 * Metodo....:getLotesDaCaixa
	 * Descricao.:Retorna a numeracao de batches existentes para uma determinada caixa
	 * @param numeroCaixa	- Numero da caixa
	 * @return String[] 	- Numeracao das caixas
	 * @throws GPPInternalErrorException
	 */
	public String[] getLotesDaCaixa(long numeroCaixa) throws GPPInternalErrorException
	{
		String lotes[] = null;
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		Autenticador autenticador = this.getAutenticador();
		try
		{
			// Cria objeto para consulta na plataforma
			tincSeqHolder pSeq = new tincSeqHolder(new tincRecord[TVMGetBatchesPerBoxPSeqAttributes._LastOne]);
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
			
			// Define os parametros de consulta
			// Numero da caixa
			pSeq.value[0] = new tincRecord(TVMGetBatchesPerBoxPSeqAttributes._BoxNo, orb.create_any());
			pSeq.value[0].value.insert_long((int)numeroCaixa);
			// ID do Operador.
			pSeq.value[1] = new tincRecord(TVMGetBatchesPerBoxPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
			// ID do Login.
			pSeq.value[2] = new tincRecord(TVMGetBatchesPerBoxPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());
			
			int codRetorno = getTVMInterface().getBatchesPerBox(pSeq,rSeq);
			if (codRetorno == Definicoes.RET_OPERACAO_OK)
			{
				// Para cada linha do objeto recordSeqHolder estao os dados dos lotes
				// sendo que o primeiro valor do vetor e a informacao do numero do lote (batch)
				lotes = new String[rSeq.value.length];
				for (int i=0; i < rSeq.value.length; i++)
				{
					tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[i]);
					lotes[i] = dataSeq.value[0].value.extract_string();
				}
			}
			else
				log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getLotesDaCaixa","Erro ao identificar faixa de valores de batch da caixa "+numeroCaixa+". Erro:"+mapRetorno.getDescricao(codRetorno));
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getLotesDaCaixa","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getLotesDaCaixa","Erro ao pesquisar numero de caixa pela ordem. Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getLotesDaCaixa","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return lotes;
	}
	
	/**
	 * Realiza consulta de vouchers. 
	 * Retorna objeto somente em caso de sucesso. Null nos demais casos.
	 * 
	 * @param numVoucher 	Numero do voucher
	 * @return Voucher		Objeto voucher contendo as informacoes da pesquisa
	 */
	public Voucher getInformacoesVoucher(String numVoucher)
	{
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		Voucher voucher = new Voucher();
		Autenticador autenticador = this.getAutenticador();
		try
		{
			// O array sera construido sem a definicao do campo ExactFlag. Mais informacoes abaixo.
			tincSeqHolder   pSeq = new tincSeqHolder(new tincRecord[TVMGetVoucherDetailsPSeqAttributes._LastOne - 1]);
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
			
			// Define os parametros de consulta
			pSeq.value[0] = new tincRecord(TVMGetVoucherDetailsPSeqAttributes._VoucherNo, orb.create_any());
			pSeq.value[0].value.insert_string(numVoucher);
			pSeq.value[1] = new tincRecord(TVMGetVoucherDetailsPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
			pSeq.value[2] = new tincRecord(TVMGetVoucherDetailsPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());
			
			// 21/05/2007 - Daniel Ferreira. Com a 4.4.6 foi definido o parametro ExactFlag, que tem o seguinte 
			// dominio:
			//	Nao definido: Nenhuma validacao e feita.
			//  0: O numero do voucher nao deve possuir o PIN.
			//  1: O numero do voucher deve possuir o PIN.
			// Como o processo nao necessita de validacao, nenhum parametro sera passado.
			//pSeq.value[3] = new tincRecord(TVMGetVoucherDetailsPSeqAttributes._ExactFlag, orb.create_any());
			//pSeq.value[3].value.insert_long(1);

			// Consulta o voucher na plataforma definindo seus atributos pelo resultado
			int codRetorno = getTVMInterface().getVoucherDetails(pSeq,rSeq);			
			if (codRetorno == Definicoes.RET_OPERACAO_OK)
			{
				voucher = new Voucher();
				// O código de retorno está sendo preenchido com constantes da classe Definicoes.
				voucher.setCodRetorno   		(Definicoes.RET_S_OPERACAO_OK);
				voucher.setNumeroVoucher		(numVoucher);
				tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[0]);
				voucher.setCodStatusVoucher		(dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._Status].value.extract_short());
				voucher.setNumeroBatch			(dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._BatchNo].value.extract_string());
				voucher.setCodigoSeguranca      (dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._SecurityCode].value.extract_string());
				voucher.setTipoVoucher			(dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._VoucherType].value.extract_short());
				
				int valorFacePrincipal	=	dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._FaceValue].value.extract_long();
				int valorFaceBonus		=	dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._BonusFaceValue].value.extract_long();
				int valorFaceSm			=	dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._SmFaceValue].value.extract_long();
				int valorFaceDados		=	dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._DataFaceValue].value.extract_long();
				
				voucher.setValoresVoucherPrincipal(new HashMap());
				voucher.setValoresVoucherBonus(new HashMap());
				voucher.setValoresVoucherSm(new HashMap());
				voucher.setValoresVoucherDados(new HashMap());
				
				insereValoresVoucher(voucher.getValoresVoucherPrincipal(), 	TipoSaldo.PRINCIPAL, valorFacePrincipal, voucher.getTipoVoucher());
				insereValoresVoucher(voucher.getValoresVoucherBonus(), 		TipoSaldo.BONUS,	 valorFaceBonus,	 voucher.getTipoVoucher());
				insereValoresVoucher(voucher.getValoresVoucherSm(), 		TipoSaldo.TORPEDOS,	 valorFaceSm, 		 voucher.getTipoVoucher());
				insereValoresVoucher(voucher.getValoresVoucherDados(), 		TipoSaldo.DADOS,	 valorFaceDados, 	 voucher.getTipoVoucher());
					
				voucher.setUsadoPor      (dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._UsedBy].value.extract_string());
				
				DataTEC data = new DataTEC();
				
				data.extrair(dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._UsedDate].value);
				voucher.setDataUtilizacao(data.toDate());
				
				data.extrair(dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._ExpiryDate].value);
				voucher.setDataExpiracao(data.toDate());
				
				data.extrair(dataSeq.value[TVMGetVoucherDetailsRSeqAttributes._TimeStamp].value);
				voucher.setDataUltimaAtualizacao(data.toDate());
			}
			else if (codRetorno == TVM_VOUCHER_NO_DOES_NOT_EXIST.value)
			{
				voucher.setCodRetorno((short)Definicoes.RET_VOUCHER_NAO_EXISTE);
				log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getInformacoesVoucher","Erro ao pesquisar informacoes do voucher numero:"+numVoucher+". Erro:"+mapRetorno.getDescricao(codRetorno));
			}
			else 
			{
				voucher.setCodRetorno ((short)Definicoes.RET_ERRO_GENERICO_TECNOMEN);
				log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getInformacoesVoucher","Erro ao pesquisar informacoes do voucher numero:"+numVoucher+". Erro:"+mapRetorno.getDescricao(codRetorno));
			}
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getInformacoesVoucher","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getInformacoesVoucher","Erro ao pesquisar informacoes do voucher numero:"+numVoucher+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getInformacoesVoucher","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return voucher;
	}
	
	/**
	 * Esse método insere no map 'destino' todas as instancias de ValorVoucher (obtidas
	 * de MapValoresVoucher) de acordo com três dados informados: idTipoSaldo, valorFace e tipoVoucher.
	 * 
	 * A chave do map é a categoriaTEC
	 * 
	 * @throws GPPInternalErrorException
	 */
	private void insereValoresVoucher(Map destino, int idTipoSaldo, int valorFace, int tipoVoucher)
		throws GPPInternalErrorException
	{
		Collection valoresVoucher = MapValoresVoucher.getInstancia().getAll();
		if (valoresVoucher == null) return;
		
		for (Iterator it = valoresVoucher.iterator(); it.hasNext(); )
		{	
			ValorVoucher vv = (ValorVoucher)it.next();
			if (vv.getTipoSaldo() != null && vv.getTipoSaldo().getIdtTipoSaldo() == idTipoSaldo &&
				vv.getValorFace() == valorFace && vv.getTipoVoucher() == tipoVoucher &&
				vv.getCategoriaTEC() != null)
			{
				destino.put(vv.getCategoriaTEC(), vv);
			}
		}
	}
	
	/**
	 * Metodo....:insereCancelamentoVoucher
	 * Descricao.:Insere as informações referentes ao cancelamento de vouchers via portal na tbl_rec_voucher_cancelado
	 * @param numeroVoucher - Numero do voucher
	 * @param comentario    - Usuário que cancelou o voucher
	 * @return retorno  	- Flag de erro
	 * @throws SQLException 
	 */
	public void insereCancelamentoVoucher(String numeroVoucher, String comentario) throws SQLException 
	{		
		PREPConexao	 conexaoPrep = null;
		
		try
		{
			//	Pega conexão com Banco de Dados
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			
			//	Insere Candelamento na tabela  tbl_rec_voucher_cancelado	 
			String query = " insert into tbl_rec_voucher_cancelado " +
						   " (NUM_CARTAO, NOM_OPERADOR, DAT_CANCELAMENTO) " +
						   " values ( ? , ? , sysdate ) ";			

			log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","insereCancelamentoVoucher","inserindo cancelamneto na tbl_rec_voucher_cancelado");
			
			Object[] params = {	numeroVoucher,	// Parametro 0
								comentario,		// Parametro 1
							   };
			conexaoPrep.executaPreparedUpdate(query,params,idProcesso);
			
		}
		catch (Exception e)
		{
			log.log(idProcesso,Definicoes.ERRO,"OperacoesVoucher","insereCancelamentoVoucher","Erro ao inserir cancelamento na tbl_rec_voucher_cancelado: "+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
	
	/**
	 * Metodo....:getInformacoesOrdem
	 * Descricao.:Retorna as informacoes da Ordem de Voucher cadastrada na Tecnomen. Este metodo
	 *            realiza a pesquisa de Boxes e Batches associados a esta ordem.
	 * @param numOrdem - Numero da Ordem
	 * @return VoucherOrdem - Informacoes da ordem
	 */
	public VoucherOrdem getInformacoesOrdem(long numOrdem)
	{
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		VoucherOrdem ordem = new VoucherOrdem(numOrdem);
		Autenticador autenticador = this.getAutenticador();
		
		try
		{
			tincSeqHolder   pSeq = new tincSeqHolder(new tincRecord[TVMGetOrderDetailsPSeqAttributes._LastOne]);
			recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
			
			// Define os parametros de consulta
			// Numero da Ordem
			pSeq.value[0] = new tincRecord(TVMGetOrderDetailsPSeqAttributes._OrderNo, orb.create_any());
			pSeq.value[0].value.insert_long((int)numOrdem);
			// ID do Operador.
			pSeq.value[1] = new tincRecord(TVMGetOrderDetailsPSeqAttributes._OperatorId, orb.create_any());
			pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
			// ID do Login.
			pSeq.value[2] = new tincRecord(TVMGetOrderDetailsPSeqAttributes._LoginId, orb.create_any());
			pSeq.value[2].value.insert_long(autenticador.getIdUsuario());

			// Consulta a Ordem na plataforma definindo seus atributos pelo resultado
			TVMInterface tvm = getTVMInterface();
			int codRetorno = tvm.getOrderDetails(pSeq,rSeq);
			if (codRetorno == Definicoes.RET_OPERACAO_OK)
			{
				tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[0]);
				ordem.setStatus(dataSeq.value[TVMGetOrderDetailsRSeqAttributes._Status].value.extract_short());
				
				// Define as caixas (Boxes) existentes para esta ordem
				Collection caixas = getInformacoesCaixaOrdem(ordem, tvm);
				for (Iterator i = caixas.iterator(); i.hasNext(); )
					ordem.addCaixaOrdem((VoucherOrdemCaixa)i.next());
			}
			else
				log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getInformacoesOrdem","Erro ao pesquisar informacoes da ordem de voucher numero:"+numOrdem+". Erro:"+mapRetorno.getDescricao(codRetorno));
		}
		catch(GPPInternalErrorException ge)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getInformacoesOrdem","Erro na conexao de voucher com a Tecnomen. Erro:"+ge);
		}
		catch(Pi_exception pi)
		{
			log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getInformacoesOrdem","Erro ao pesquisar informacoes da ordem de voucher numero:"+numOrdem+". Erro:"+pi.getMessage());
		}
		catch(OBJECT_NOT_EXIST oje)
		{
			// Caso o erro seja de CORBA_NOT_EXIST significa que a conexao de voucher caiu, portanto
			// tenta reconectar, neste ponto a excecao e enviada mesmo assim ,porem as proximas
			// chamadas terao sucesso
			if (!reconecta())
				log.log(idProcesso,Definicoes.WARN,"OperacoesVoucher","getInformacoesOrdem","VoucherServer nao esta disponivel, nao foi possivel reconexao.");
		}
		finally
		{
			releaseTVMInterface();
		}
		return ordem;
	}
	
	/**
	 * Metodo....:getInformacoesCaixaOrdem
	 * Descricao.:Retorna as informacoes das caixas associadas a uma ordem de voucher
	 * @param numOrdem 	- Numero da ordem
	 * @param tvm		- Instancia da conexao de voucher
	 * @return Collection - Lista contendo as informacoes das caixas
	 * @throws Exception
	 */
	private Collection getInformacoesCaixaOrdem(VoucherOrdem ordem, TVMInterface tvm) throws Pi_exception,OBJECT_NOT_EXIST
	{
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		Collection listaCaixas = new ArrayList();
		Autenticador autenticador = this.getAutenticador();
		tincSeqHolder   pSeq = new tincSeqHolder(new tincRecord[TVMGetBoxesPerOrderPSeqAttributes._LastOne]);
		recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
		
		// Define os parametros de consulta
		// Numero da Ordem
		pSeq.value[0] = new tincRecord(TVMGetBoxesPerOrderPSeqAttributes._OrderNo, orb.create_any());
		pSeq.value[0].value.insert_long((int)ordem.getNumeroOrdem());
		// ID do Operador.
		pSeq.value[1] = new tincRecord(TVMGetBoxesPerOrderPSeqAttributes._OperatorId, orb.create_any());
		pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
		// ID do Login.
		pSeq.value[2] = new tincRecord(TVMGetBoxesPerOrderPSeqAttributes._LoginId, orb.create_any());
		pSeq.value[2].value.insert_long(autenticador.getIdUsuario());

		int codRetorno = tvm.getBoxesPerOrder(pSeq,rSeq);
		if (codRetorno == Definicoes.RET_OPERACAO_OK)
		{
			// Realiza a iteracao no array de resultados, sendo cada
			// linha do array uma informacao de uma caixa. Portanto
			// para cada iteracao eh criado o objeto VoucherOrdemCaixa
			// que irah armazenar as informacoes dos Boxes
			for (int i=0; i < rSeq.value.length; i++)
			{
				tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[i]);
				VoucherOrdemCaixa caixa = new VoucherOrdemCaixa(ordem, dataSeq.value[TVMGetBoxesPerOrderRSeqAttributes._BoxNo].value.extract_long());
				
				caixa.setQtdePorCaixa	(dataSeq.value[TVMGetBoxesPerOrderRSeqAttributes._PerBoxQty].value.extract_long());
				caixa.setQtdePorBatch	(dataSeq.value[TVMGetBoxesPerOrderRSeqAttributes._PerBatchQty].value.extract_long());
				caixa.setStatus			(dataSeq.value[TVMGetBoxesPerOrderRSeqAttributes._Status].value.extract_short());
				
				Collection lotes = getInformacoesLoteCaixa(caixa, tvm);
				for (Iterator j = lotes.iterator(); j.hasNext(); )
					caixa.addLoteCaixa((VoucherOrdemLote)j.next());
					
				listaCaixas.add(caixa);
			}
		}
		else
			log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getInformacoesCaixaOrdem","Erro ao pesquisar informacoes das caixas da ordem de voucher numero:"+ordem.getNumeroOrdem()+". Erro:"+mapRetorno.getDescricao(codRetorno));

		return listaCaixas;
	}
	
	private Collection getInformacoesLoteCaixa(VoucherOrdemCaixa caixa, TVMInterface tvm) throws Pi_exception,OBJECT_NOT_EXIST
	{
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		Collection listaLotes = new ArrayList();
		Autenticador autenticador = this.getAutenticador();
		tincSeqHolder   pSeq = new tincSeqHolder(new tincRecord[TVMGetBatchesPerBoxPSeqAttributes._LastOne]);
		recordSeqHolder rSeq = new recordSeqHolder(new tincRecord[][]{});
		
		// Define os parametros de consulta
		// Numero da Ordem
		pSeq.value[0] = new tincRecord(TVMGetBatchesPerBoxPSeqAttributes._BoxNo, orb.create_any());
		pSeq.value[0].value.insert_long((int)caixa.getNumeroCaixa());
		// ID do Operador.
		pSeq.value[1] = new tincRecord(TVMGetBatchesPerBoxPSeqAttributes._OperatorId, orb.create_any());
		pSeq.value[1].value.insert_long(autenticador.getIdUsuario());
		// ID do Login.
		pSeq.value[2] = new tincRecord(TVMGetBatchesPerBoxPSeqAttributes._LoginId, orb.create_any());
		pSeq.value[2].value.insert_long(autenticador.getIdUsuario());

		int codRetorno = tvm.getBatchesPerBox(pSeq,rSeq);
		if (codRetorno == Definicoes.RET_OPERACAO_OK)
		{
			// Realiza a iteracao no array de resultados, sendo cada
			// linha do array uma informacao de um lote. Portanto
			// para cada iteracao eh criado o objeto VoucherOrdemLote
			// que irah armazenar as informacoes dos lotes
			for (int i=0; i < rSeq.value.length; i++)
			{
				tincSeqHolder dataSeq = new tincSeqHolder(rSeq.value[i]);
				VoucherOrdemLote lote = new VoucherOrdemLote(caixa, dataSeq.value[TVMGetBatchesPerBoxRSeqAttributes._BatchNo].value.extract_string());
				
				lote.setStatus		(dataSeq.value[TVMGetBatchesPerBoxRSeqAttributes._Status].value.extract_short());
				lote.setValorFace	(dataSeq.value[TVMGetBatchesPerBoxRSeqAttributes._FaceValue].value.extract_long());
				
				listaLotes.add(lote);
			}
		}
		else
			log.log(idProcesso,Definicoes.INFO,"OperacoesVoucher","getInformacoesLoteCaixa",
					 "Erro ao pesquisar informacoes dos lotes de caixas da ordem de voucher numero:"+caixa.getOrdem().getNumeroOrdem()+". Erro:"+mapRetorno.getDescricao(codRetorno));

		return listaLotes;
	}
}
