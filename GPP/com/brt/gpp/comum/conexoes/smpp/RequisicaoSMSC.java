package com.brt.gpp.comum.conexoes.smpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.logica.smpp.Data;
import com.logica.smpp.pdu.CancelSM;
import com.logica.smpp.pdu.DeliverSMResp;
import com.logica.smpp.pdu.DestinationAddress;
import com.logica.smpp.pdu.EnquireLinkResp;
import com.logica.smpp.pdu.QuerySM;
import com.logica.smpp.pdu.SubmitMultiSM;
import com.logica.smpp.pdu.SubmitSM;

/**
 * A classe <code>RequisicaoSMPP</code> e responsavel pelo tratamento das requisicoes
 * enviadas a plataforma SMSC.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 06/12/2007
 */
public class RequisicaoSMSC
{
	private ConexaoSMPP conexaoSMPP;
	// Atributos necessarios para o funcionamento do LOG
	private GerentePoolLog	log;
	private long 			idProcesso;
	private static String 	classe;
	private ArquivoConfiguracaoGPP arqConf;

	// Parametro do arquivo de Configuracao do GPP
	public static final String SMSC_ORIGINADOR = "SMSC_ORIGINADOR";

	public RequisicaoSMSC(ConexaoSMPP conexaoSMPP)
	{
		this.conexaoSMPP = conexaoSMPP;
		// Captura o nome da classe
		StringBuffer sb  = new StringBuffer(this.getClass().getName());
		classe 			 = sb.substring(sb.lastIndexOf(".")+1);
		this.idProcesso  = conexaoSMPP.getIdProcesso();
		this.log	 	 = GerentePoolLog.getInstancia(this.getClass());
		arqConf = ArquivoConfiguracaoGPP.getInstance();
	}
	/**
	 * Envia uma requisicao de <i>Envio de SMS para unico destino</i> a plataforma SMSC.<br>
	 * O numero <code>sequenceNumber</code> deve ser definido para identificacao futura da requisicao na plataforma.
	 *
	 * @param MsisdnOrigem		Numero de origem
	 * @param MsisdnDestino		Numero de destino
	 * @param mensagem			Corpo da mensagem
	 * @param sequenceNumber	Numero definido para identificar a requisicao
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarSubmitSM(String msisdnOrigem, String msisdnDestino, String mensagem, boolean receberResposta, int sequenceNumber) throws Exception
	{
		String metodo = "enviarSubmitSM";
		// Caso a conexao nao esteja aberta. Abrir uma nova conexao.
		conexaoSMPP.abrirConexao();

		conexaoSMPP.timer.tick();

		SubmitSM requisicao = new SubmitSM();
		// MSISDN de Origem
		if(msisdnOrigem == null)
			msisdnOrigem = arqConf.getString(SMSC_ORIGINADOR);
		requisicao.setSourceAddr(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, msisdnOrigem);
		// MSISDN de Destino
		requisicao.setDestAddr(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, msisdnDestino);
		// Escopo da mensagem
		requisicao.setShortMessage(mensagem);
		// Numero sequencial da requisicao
		requisicao.setSequenceNumber(sequenceNumber);
		// A plataforma SMSC deve enviar uma resposta quando mensagem for entregue
		if(receberResposta)
			requisicao.setRegisteredDelivery((byte)(Data.SM_SME_ACK_BOTH_REQUESTED + Data.SM_SMSC_RECEIPT_REQUESTED));
		else
			requisicao.setRegisteredDelivery(Data.SM_SMSC_RECEIPT_NOT_REQUESTED);

		// Mensagem de DEBUG
		StringBuffer debug = new StringBuffer("Enviando requisicao SUBMIT_SM.");
			debug.append(" -Source Addr: ").append(conexaoSMPP.formatAddress(requisicao.getSourceAddr()));
			debug.append(" -Dest Addr: ").append(conexaoSMPP.formatAddress(requisicao.getDestAddr()));
			debug.append(" -Sequence Number: "+requisicao.getSequenceNumber());
			debug.append(" -Shor Message: "+requisicao.getShortMessage());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		try
		{
			conexaoSMPP.getSessao().submit(requisicao);
		}
		catch(IOException e)
		{
			conexaoSMPP.conectado = false;
			throw e;
		}
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Requisicao SUBMIT_SM enviada com sucesso!");
	}
	/**
	 * <hr>
	 * <b>COMANDO DESABILITADO NA PLATAFORMA SMSC</b>
	 * <hr>
	 * Envia uma requisicao de <i>Envio de SMS para multiplos destinos</i> a plataforma SMSC.<br>
	 * O numero <code>sequenceNumber</code> deve ser definido para identificacao futura da requisicao na plataforma.
	 *
	 * @param MsisdnOrigem		Numero de origem
	 * @param MsisdnDestino		Numeros de destino (Array de String)
	 * @param mensagem			Corpo da mensagem
	 * @param sequenceNumber	Numero definido para identificar a requisicao
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarSubmitMulti(String msisdnOrigem, String[] msisdnDestino, String mensagem, boolean receberResposta, int sequenceNumber) throws Exception
	{
		Collection lista = new ArrayList();
		for(int i = 0; i < msisdnDestino.length; i++)
			lista.add(msisdnDestino[i]);
		enviarSubmitMulti(msisdnOrigem, lista, mensagem, receberResposta, sequenceNumber);
	}
	/**
	 * <hr>
	 * <b>COMANDO DESABILITADO NA PLATAFORMA SMSC</b>
	 * <hr>
	 * Envia uma requisicao de <i>Envio de SMS para multiplos destinos</i> a plataforma SMSC.<br>
	 * O numero <code>sequenceNumber</code> deve ser definido para identificacao futura do SMS na plataforma.
	 *
	 * @param MsisdnOrigem		Numero de origem
	 * @param MsisdnDestino		Numeros de destino (Collection de String)
	 * @param mensagem			Corpo da mensagem
	 * @param sequenceNumber	Numero definido para identificar a requisicao
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarSubmitMulti(String msisdnOrigem, Collection msisdnDestino, String mensagem, boolean receberResposta, int sequenceNumber) throws Exception
	{
		String metodo = "enviarSubmitMultiSM";
		// Caso a conexao nao esteja aberta. Abrir uma nova conexaoSMPP.
		conexaoSMPP.abrirConexao();

		conexaoSMPP.timer.tick();

		SubmitMultiSM requisicao = new SubmitMultiSM();
		// MSISDN de Origem
		if(msisdnOrigem == null)
			msisdnOrigem = arqConf.getString(SMSC_ORIGINADOR);
		requisicao.setSourceAddr(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, msisdnOrigem);
		// MSISDNs de Destino
		for(Iterator it = msisdnDestino.iterator(); it.hasNext();)
			requisicao.addDestAddress(new DestinationAddress(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, (String)it.next()));
		// Escopo da mensagem
		requisicao.setShortMessage(mensagem);
		// Numero sequencial da requisicao
		requisicao.setSequenceNumber(sequenceNumber);
		// A plataforma SMSC deve enviar uma resposta quando mensagem for entregue
		if(receberResposta)
			requisicao.setRegisteredDelivery((byte)(Data.SM_SME_ACK_BOTH_REQUESTED + Data.SM_SMSC_RECEIPT_REQUESTED));
		else
			requisicao.setRegisteredDelivery(Data.SM_SMSC_RECEIPT_NOT_REQUESTED);

		// Mensagem de DEBUG
		StringBuffer debug = new StringBuffer("Enviando requisicao SUBMIT_SM_MULTI.");
			debug.append(" -Source Addr: ").append(conexaoSMPP.formatAddress(requisicao.getSourceAddr()));
			debug.append(" -Dest Addr: ");
			for(int i =0; i < requisicao.getNumberOfDests(); i++)
				debug.append(conexaoSMPP.formatAddress(requisicao.getDestAddress(i).getAddress()));
			debug.append(" -Sequence Number: "+requisicao.getSequenceNumber());
			debug.append(" -Shor Message: "+requisicao.getShortMessage());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		try
		{
			conexaoSMPP.getSessao().submitMulti(requisicao);
		}
		catch(IOException e)
		{
			conexaoSMPP.conectado = false;
			throw e;
		}

		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Requisicao SUBMIT_MULTI enviada com sucesso!");
	}
	/**
	 * <hr>
	 * <b>COMANDO DESABILITADO NA PLATAFORMA SMSC</b>
	 * <hr>
	 * Envia uma requisicao de <i>Consulta de SMS</i> a plataforma SMSC.<br>
	 * O id da mensagem <code>idMensagem</code> deve ser o mesmo definido na resposta das
	 * requisicoes <code>enviarSubmitSM</code> ou <code>enviarSubmitMultiSM</code>.<br>
	 * Utilizado quando nao e preciso identificar a requisicao na plataforma.
	 *
	 * @param idMensagem		Id da mensage
	 * @param msisdn			Msisdn de Origem
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarQuerySM(String idMensagem, String msisdnOrigem) throws Exception
	{
		enviarQuerySM(idMensagem, msisdnOrigem, 0);
	}
	/**<hr>
	 * <b>COMANDO DESABILITADO NA PLATAFORMA SMSC</b>
	 * <hr>
	 * Envia uma requisicao de <i>Consulta de SMS</i> a plataforma SMSC.<br>
	 * O id da mensagem <code>idMensagem</code> deve ser o mesmo definido na resposta das
	 * requisicoes <code>enviarSubmitSM<code> ou <code>enviarSubmitMultiSM.
	 *
	 * @param idMensagem		Id da mensage
	 * @param msisdn			Msisdn de Origem
	 * @param sequenceNumber	Utilizado para rotear a requisicao no futuro
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarQuerySM(String idMensagem, String msisdnOrigem, int sequenceNumber) throws Exception
	{
		String metodo = "enviarQuerySM";
		// Caso a conexao nao esteja aberta. Abrir uma nova conexaoSMPP.
		conexaoSMPP.abrirConexao();

		conexaoSMPP.timer.tick();

		QuerySM requisicao = new QuerySM();
		// Id da mensagem na plataforma SMSC
		requisicao.setMessageId(idMensagem);
		// MSISDN de Origem
		requisicao.setSourceAddr(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, msisdnOrigem);
		// Numero sequencial da requisicao
		requisicao.setSequenceNumber(sequenceNumber);

		// Mensagem de DEBUG
		StringBuffer debug = new StringBuffer("Enviando requisicao QUERY_SM.");
			debug.append(" -Source Addr: "+conexaoSMPP.formatAddress(requisicao.getSourceAddr()));
			debug.append(" -Message Id: "+requisicao.getMessageId());
			debug.append(" -Sequence Number: "+requisicao.getSequenceNumber());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		try
		{
			conexaoSMPP.getSessao().query(requisicao);
		}
		catch(IOException e)
		{
			conexaoSMPP.conectado = false;
			throw e;
		}

		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Requisicao QUERY_SM enviada com sucesso!");
	}
	/**
	 * <hr>
	 * <b>COMANDO DESABILITADO NA PLATAFORMA SMSC</b>
	 * <hr>
	 * Envia uma requisicao de <i>Cancelamento de SMS</i> a plataforma SMSC.<br>
	 * O id da mensagem <code>idMensagem</code> deve ser o mesmo definido na resposta das
	 * requisicoes <code>enviarSubmitSM<code> ou <code>enviarSubmitMultiSM.<br>
	 * Utilizado quando nao e preciso identificar a requisicao na plataforma.
	 *
	 * @param idMensagem
	 * @param msisdnOrigem
	 * @throws Exception
	 */
	public void enviarCancelSM(String idMensagem, String msisdnOrigem) throws Exception
	{
		enviarCancelSM(idMensagem, msisdnOrigem, 0);
	}
	/**
	 * <hr>
	 * <b>COMANDO DESABILITADO NA PLATAFORMA SMSC</b>
	 * <hr>
	 * Envia uma requisicao de <i>Cancelamento de SMS</i> a plataforma SMSC.<br>
	 * O id da mensagem <code>idMensagem</code> deve ser o mesmo definido na resposta das
	 * requisicoes <code>enviarSubmitSM<code> ou <code>enviarSubmitMultiSM.
	 *
	 * @param idMensagem		Id da mensage
	 * @param msisdn			Msisdn de Origem
	 * @param sequenceNumber	Utilizado para rotear a requisicao no futuro
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarCancelSM(String idMensagem, String msisdnOrigem, int sequenceNumber) throws Exception
	{
		String metodo = "enviarCancelSM";
		// Caso a conexao nao esteja aberta. Abrir uma nova conexaoSMPP.
		conexaoSMPP.abrirConexao();

		conexaoSMPP.timer.tick();

		CancelSM requisicao = new CancelSM();
		// Id da mensagem na plataforma SMSC
		requisicao.setMessageId(idMensagem);
		// MSISDN de Origem
		requisicao.setSourceAddr(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, msisdnOrigem);
		// Numero sequencial da requisicao
		requisicao.setSequenceNumber(sequenceNumber);

		// Mensagem de DEBUG
		StringBuffer debug = new StringBuffer("Enviando requisicao CANCEL_SM.");
			debug.append(" -Source Addr: "+conexaoSMPP.formatAddress(requisicao.getSourceAddr()));
			debug.append(" -Message Id: "+requisicao.getMessageId());
			debug.append(" -Sequence Number: "+requisicao.getSequenceNumber());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		try
		{
			conexaoSMPP.getSessao().cancel(requisicao);
		}
		catch(IOException e)
		{
			conexaoSMPP.conectado = false;
			throw e;
		}

		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Requisicao CANCEL_SM enviada com sucesso!");
	}
	/**
	 * Envia uma resposta a requisicao <code>DeliverSM</code> recebida da plataforma SMSC.
	 *
	 * @param status			Status da resposta
	 * @param sequenceNumber	Numero sequencial identificador da requisicao
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarDeliverSMResp(int status, int sequenceNumber) throws Exception
	{
		String metodo = "enviarDeliverSMResp";
		// Caso a conexao nao esteja aberta. Abrir uma nova conexaoSMPP.
		conexaoSMPP.abrirConexao();

		conexaoSMPP.timer.tick();

		DeliverSMResp resposta = new DeliverSMResp();
		// Status da resposta
		resposta.setCommandStatus(status);
		// Id da requisicao enviada pela SMSC
		resposta.setSequenceNumber(sequenceNumber);

		// Mensagem de DEBUG
		StringBuffer debug = new StringBuffer("Enviando requisicao DELIVER_SM_RESP.");
			debug.append(" -Status: "+resposta.getCommandStatus());
			debug.append(" -Sequence Number: "+resposta.getSequenceNumber());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		try
		{
			conexaoSMPP.getSessao().respond(resposta);
		}
		catch(IOException e)
		{
			conexaoSMPP.conectado = false;
			throw e;
		}

		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Resposta DELIVER_SM_RESP enviada com sucesso!");
	}
	/**
	 * Envia uma resposta a requisicao <code>EnquireLink</code> recebida da plataforma SMSC.
	 *
	 * @param status			Status da resposta
	 * @param sequenceNumber	Numero sequencial identificador da requisicao
	 * @throws Exception		Caso ocorra algum erro no processo de envio
	 */
	public void enviarEnquireLinkResp(int status, int sequenceNumber) throws Exception
	{
		String metodo = "enviarEnquireLinkResp";
		// Caso a conexao nao esteja aberta. Abrir uma nova conexaoSMPP.
		conexaoSMPP.abrirConexao();

		conexaoSMPP.timer.tick();

		EnquireLinkResp resposta = new EnquireLinkResp();
		// Status da resposta
		resposta.setCommandStatus(status);
		// Id da requisicao enviada pela SMSC
		resposta.setSequenceNumber(sequenceNumber);

		// Mensagem de DEBUG
		StringBuffer debug = new StringBuffer("Enviando requisicao ENQUIRE_LINK_RESP.");
			debug.append(" -Status: "+resposta.getCommandStatus());
			debug.append(" -Sequence Number: "+resposta.getSequenceNumber());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		try
		{
			conexaoSMPP.getSessao().respond(resposta);
		}
		catch(IOException e)
		{
			conexaoSMPP.conectado = false;
			throw e;
		}

		log.log(idProcesso, Definicoes.INFO, classe, metodo, "Resposta ENQUIRE_LINK_RESP enviada com sucesso!");
	}
}
