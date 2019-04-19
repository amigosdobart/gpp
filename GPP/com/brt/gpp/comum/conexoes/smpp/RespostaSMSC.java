package com.brt.gpp.comum.conexoes.smpp;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.logica.smpp.Data;
import com.logica.smpp.ServerPDUEvent;
import com.logica.smpp.ServerPDUEventListener;
import com.logica.smpp.pdu.CancelSMResp;
import com.logica.smpp.pdu.DeliverSM;
import com.logica.smpp.pdu.EnquireLink;
import com.logica.smpp.pdu.GenericNack;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.QuerySMResp;
import com.logica.smpp.pdu.SubmitMultiSMResp;
import com.logica.smpp.pdu.SubmitSMResp;

/**
 * A classe RespostaSMPP e responsavel pelo tratamento das respostas recebidas da plataforma SMSC.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 06/12/2007
 */
public class RespostaSMSC implements ServerPDUEventListener
{
	// CODIGOS DE RETORNO
	public static final short ESME_ROK            = 0x00;  //No Error
	public static final short ESME_RINVMSGLEN     = 0x01;  //Message Length is invalid
	public static final short ESME_RINVCMDLEN     = 0x02;  //Command Length is invalid
	public static final short ESME_RINVCMDID      = 0x03;  //Invalid Command ID
	public static final short ESME_RINVBNDSTS     = 0x04;  //Incorrect BIND Status for given command
	public static final short ESME_RALYBND        = 0x05;  //ESME Already in Bound State
	public static final short ESME_RINVPRTFLG     = 0x06;  //Invalid Priority Flag
	public static final short ESME_RINVREGDLVFLG  = 0x07;  //Invalid Registered Delivery Flag
	public static final short ESME_RSYSERR        = 0x08;  //System Error
	public static final short ESME_RINVSRCADR     = 0x0A;  //Invalid Source Address
	public static final short ESME_RINVDSTADR     = 0x0B;  //Invalid Dest Addr
	public static final short ESME_RINVMSGID      = 0x0C;  //Message ID is invalid
	public static final short ESME_RBINDFAIL      = 0x0D;  //Bind Failed
	public static final short ESME_RINVPASWD      = 0x0E;  //Invalid Password
	public static final short ESME_RINVSYSID      = 0x0F;  //Invalid System ID
	public static final short ESME_RCANCELFAIL    = 0x11;  //Cancel SM Failed
	public static final short ESME_RREPLACEFAIL   = 0x13;  //Replace SM Failed

	// STATUS DO SMS
	public static final int SMS_STATUS_NAO_ENVIADO    = 1;
	public static final int SMS_STATUS_ENVIANDO       = 2;
	public static final int SMS_STATUS_ENVIADO        = 3;
	public static final int SMS_STATUS_ENTREGE        = 4;
	public static final int SMS_STATUS_EXPIRADO       = 5;
	public static final int SMS_STATUS_REMOVIDO       = 6;
	public static final int SMS_STATUS_NAO_ENTREGAVEL = 7;
	public static final int SMS_STATUS_ACEITO         = 8;
	public static final int SMS_STATUS_INVALIDO       = 9;
	public static final int SMS_STATUS_REJEITADO      = 10;

	public static final int SMS_STATUS_ERRO           = 99;


	// ESTADOS DO SMS RETORNADO PELA RESPOSTA DELIVER_SM
	public static final String DELIVERED     = "DELIVRD"; // Message is delivered to destination
	public static final String EXPIRED       = "EXPIRED"; // Message validity period has expired
	public static final String DELETED       = "DELETED"; // Message has been deleted
	public static final String UNDELIVERABLE = "UNDELIV"; // Message is undeliverable
	public static final String ACCEPTED      = "ACCEPTD"; // Message is in accepted state
														  // (i.e. has been manually read on behalf
														  // of the subscriber by customer service)
	public static final String UNKNOWN       = "UNKNOWN"; // Message is in invalid state
	public static final String REJECTED      = "REJECTD"; // Message is in a rejected state

	// Atributos necessarios para o funcionamento do LOG
	private GerentePoolLog		log;
	private long 				idProcesso;
	private static String		classe;
	private ConexaoSMPP			conexaoSMPP;

	private GerentePoolBancoDados poolBanco;
	/**
	 * A classe <code>RespostaSMSC</code> e instanciada e utilizada somente pela classe
	 * <code>ConexaoSMPP</code>.
	 *
	 * @param conexaoSMPP Instancia da classe ConexaoSMPP
	 * @throws GPPInternalErrorException Caso ocorra algum problema ao recuperar uma conexao com
	 *                                   o banco de dados
	 */
	public RespostaSMSC(ConexaoSMPP conexaoSMPP) throws GPPInternalErrorException
	{
		// Captura o nome da classe
		StringBuffer sb  = new StringBuffer(this.getClass().getName());
		classe 			 = sb.substring(sb.lastIndexOf(".")+1);
		// Inicia Sistema de log
		log				 = GerentePoolLog.getInstancia(this.getClass());
		this.conexaoSMPP = conexaoSMPP;
		this.idProcesso  = conexaoSMPP.getIdProcesso();

		poolBanco = GerentePoolBancoDados.getInstancia(idProcesso);
	}

	public void handleEvent(ServerPDUEvent event)
	{
		String metodo = "handleEvent";
		PDU pdu = event.getPDU();

		switch(pdu.getCommandId())
		{
			case Data.SUBMIT_SM_RESP :
			{
				SubmitSMResp resposta = (SubmitSMResp)pdu;

				StringBuffer debug = new StringBuffer("Resposta SUBMIT_SM_RESP recebida.");
					debug.append(" -Status: ").append(Integer.toString(resposta.getCommandStatus(), 16));
					debug.append(" -Sequence Number: ").append(resposta.getSequenceNumber());
					debug.append(" -Message Id: ").append(resposta.getMessageId());
				log.log(idProcesso, Definicoes.DEBUG, classe, metodo,  debug.toString());

				PREPConexao conexaoBanco = null;

				try
				{
					// Recupera do pool uma conexao com banco de dados.
					conexaoBanco = poolBanco.getConexaoPREP(idProcesso);
					EnvioSMSDAO dao = new EnvioSMSDAO(this.idProcesso, conexaoBanco);

					DadosSMS sms = dao.buscarSMSPorIdRegistro(resposta.getSequenceNumber());

					if(sms != null)
					{
						if(resposta.getCommandStatus() == ESME_ROK)
						{
							if(!sms.getTipoSMS().deveNotificarEntrega())
								sms.setIdtStatusProcessamento(SMS_STATUS_ENVIADO);

							sms.setIdRegistroSMSC(new Long(Long.parseLong(resposta.getMessageId(), 16)));
						}
						else
						{
							if(resposta.getCommandStatus() == ESME_RINVMSGLEN ||
									resposta.getCommandStatus() == ESME_RINVSRCADR ||
									resposta.getCommandStatus() == ESME_RINVDSTADR )
							{
								sms.setIdtStatusProcessamento(SMS_STATUS_ERRO);
							}

							sms.setIdtStatusSMSC(new Integer(resposta.getCommandStatus()));
						}

						dao.alterarSMS(sms);
					}
					else
						log.log(idProcesso, Definicoes.WARN, classe, metodo,
								"SUBMIT_SM_RESP - SMS nao encontrado na tabela. idRegistro: "+resposta.getSequenceNumber());
				}
				catch (NumberFormatException e)
				{
					log.log(idProcesso, Definicoes.ERRO, classe, metodo,
							"SUBMIT_SM_RESP - Erro recuperar o id da Mensagem: "+resposta.getMessageId());
				}
				catch (GPPInternalErrorException e)
				{
					log.log(idProcesso, Definicoes.ERRO, classe, metodo,
							"SUBMIT_SM_RESP - Erro ao realizar operacao no banco. Erro:"+log.traceError(e));
				}
				catch (SQLException e)
				{
					log.log(idProcesso, Definicoes.ERRO, classe, metodo,
							"SUBMIT_SM_RESP - Erro ao percorrer resultset. Erro: "+log.traceError(e));
				}
				finally
				{
					if(conexaoBanco != null)
						poolBanco.liberaConexaoPREP(conexaoBanco, idProcesso);
				}
				break;
			}
			case Data.DELIVER_SM :
			{
				DeliverSM resposta = (DeliverSM)pdu;

				StringBuffer debug = new StringBuffer("Requisicao DELIVER_SM recebida.");
					debug.append(" -Status: ").append(Integer.toString(resposta.getCommandStatus(), 16));
					debug.append(" -Sequence Number: ").append(resposta.getSequenceNumber());
					debug.append(" -Source Addr: ").append(conexaoSMPP.formatAddress(resposta.getSourceAddr()));
					debug.append(" -Dest Addr: ").append(conexaoSMPP.formatAddress(resposta.getDestAddr()));
					debug.append(" -Short Message: ").append(resposta.getShortMessage());
				log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

				try
				{
					conexaoSMPP.getRequisicaoSMSC().enviarDeliverSMResp(Data.ESME_ROK, resposta.getSequenceNumber());
				}
				catch (Exception e)
				{
					log.log(idProcesso, Definicoes.ERRO, classe, metodo, "DELIVER_SM - Erro ao enviar DELIVER_SM. "+e.getMessage());
				}

				String mensagem = resposta.getShortMessage();
				// Pattern responsavel por capturar o Id da mensagem
				Pattern idPattern = Pattern.compile("[iI][dD]:(\\d+)");
				Matcher idMatcher = idPattern.matcher(mensagem);
				// Pattern responsavel por capturar o status final da mensagem
				Pattern statPattern = Pattern.compile("[sS][tT][aA][tT]:(\\w{7})");
				Matcher statMatcher = statPattern.matcher(mensagem);

				if(idMatcher.find() && statMatcher.find())
				{
					PREPConexao conexaoBanco = null;

					try
					{
						// Recupera do pool uma conexao com banco de dados.
						conexaoBanco = poolBanco.getConexaoPREP(idProcesso);
						EnvioSMSDAO dao = new EnvioSMSDAO(this.idProcesso, conexaoBanco);

						long idRegistroSMSC = Long.parseLong(idMatcher.group(1));
						DadosSMS sms = dao.buscarSMSPorIdRegistroSMSC(idRegistroSMSC);
						String statusSMS = statMatcher.group(1);

						if(sms != null)
						{
							if(DELIVERED.equals(statusSMS))
								sms.setIdtStatusProcessamento(SMS_STATUS_ENTREGE);
							else if(EXPIRED.equals(statusSMS))
								sms.setIdtStatusProcessamento(SMS_STATUS_EXPIRADO);
							else if(DELETED.equals(statusSMS))
								sms.setIdtStatusProcessamento(SMS_STATUS_REMOVIDO);
							else if(UNDELIVERABLE.equals(statusSMS))
								sms.setIdtStatusProcessamento(SMS_STATUS_NAO_ENTREGAVEL);
							else if(ACCEPTED.equals(statusSMS))
								sms.setIdtStatusProcessamento(SMS_STATUS_ACEITO);
							else if(REJECTED.equals(statusSMS))
								sms.setIdtStatusProcessamento(SMS_STATUS_REJEITADO);
							else
								sms.setIdtStatusProcessamento(SMS_STATUS_INVALIDO);

							dao.alterarSMS(sms);
						}
						else
							log.log(idProcesso, Definicoes.DEBUG, classe, metodo,
									"DELIVER_SM - SMS nao encontrado na tabela. idRegistroSMSC: "+idRegistroSMSC);
					}
					catch (NumberFormatException e)
					{
						log.log(idProcesso, Definicoes.ERRO, classe, metodo,
								"DELIVER_SM - Erro recuperar o id da Mensagem: "+idMatcher.group(1));
					}
					catch (GPPInternalErrorException e)
					{
						log.log(idProcesso, Definicoes.ERRO, classe, metodo,
								"DELIVER_SM - Erro ao inserir ou recuperar dados no banco. Erro:"+log.traceError(e));
					}
					catch (SQLException e)
					{
						log.log(idProcesso, Definicoes.ERRO, classe, metodo,
								"DELIVER_SM - Erro ao percorrer resultset. Erro: "+log.traceError(e));
					}
					finally
					{
						if(conexaoBanco != null)
							poolBanco.liberaConexaoPREP(conexaoBanco, idProcesso);
					}
				}

				break;
			}
			case Data.ENQUIRE_LINK :
			{
				EnquireLink requisicao = (EnquireLink)pdu;

				StringBuffer debug = new StringBuffer("Requisicao ENQUIRE_LINK recebida.");
					debug.append(" -Status: ").append(Integer.toString(requisicao.getCommandStatus(), 16));
					debug.append(" -Sequence Number: ").append(requisicao.getSequenceNumber());
				log.log(idProcesso, Definicoes.INFO, classe, metodo, debug.toString());

				try
				{
					conexaoSMPP.getRequisicaoSMSC().enviarEnquireLinkResp(Data.ESME_ROK, requisicao.getSequenceNumber());
				}
				catch (Exception e)
				{
					log.log(idProcesso, Definicoes.ERRO, classe, metodo, "Erro ao enviar ENQUIRE_LINK. "+e.getMessage());
				}
				break;
			}
			case Data.GENERIC_NACK :
			{
				GenericNack resposta = (GenericNack)pdu;

				StringBuffer debug = new StringBuffer("Resposta GENERIC_NACK recebida.");
					debug.append(" -Error code: "+Integer.toString(resposta.getCommandStatus(), 16));
					debug.append(" -Sequence Number: "+resposta.getSequenceNumber());
				log.log(idProcesso, Definicoes.WARN, classe, metodo, debug.toString());

				break;
			}
			// COMANDO DESABILIDADO NA PLATAFORMA SMSC
			case Data.SUBMIT_MULTI_RESP :
			{
				SubmitMultiSMResp resposta = (SubmitMultiSMResp)pdu;

				StringBuffer debug = new StringBuffer("Resposta SUBMIT_MULTI_SM_RESP recebida.");
					debug.append(" -Status: ").append(Integer.toString(resposta.getCommandStatus(), 16));
					debug.append(" -Sequence Number: ").append(resposta.getSequenceNumber());
					debug.append(" -Message Id: ").append(resposta.getMessageId());
				log.log(idProcesso, Definicoes.INFO, classe, metodo, debug.toString());

				break;
			}
			// COMANDO DESABILIDADO NA PLATAFORMA SMSC
			case Data.CANCEL_SM_RESP :
			{
				CancelSMResp resposta = (CancelSMResp)pdu;

				StringBuffer debug = new StringBuffer("Resposta CANCEL_SM_RESP recebida.");
					debug.append(" -Status: ").append(Integer.toString(resposta.getCommandStatus(), 16));
					debug.append(" -Sequence Number: ").append(resposta.getSequenceNumber());
				log.log(idProcesso, Definicoes.INFO, classe, metodo, debug.toString());

				break;
			}
			// COMANDO DESABILIDADO NA PLATAFORMA SMSC
			case Data.QUERY_SM_RESP :
			{
				QuerySMResp resposta = (QuerySMResp)pdu;

				StringBuffer debug = new StringBuffer("Resposta QUERY_SM_RESP recebida.");
					debug.append(" -Status: ").append(Integer.toString(resposta.getCommandStatus(), 16));
					debug.append(" -Message Id: ").append(resposta.getMessageId());
					debug.append(" -Message State: ").append(resposta.getMessageState());
					debug.append(" -Final Date:  ").append(resposta.getFinalDate());
					debug.append(" -Error Code:  ").append(resposta.getErrorCode());
				log.log(idProcesso, Definicoes.INFO, classe, metodo, debug.toString());

				break;
			}
			default:
			{
				log.log(idProcesso, Definicoes.WARN, classe, metodo, "Resposta/Requisicao invalida. Command Id: "+pdu.getCommandId());
			}
		}
	}

    /**
     * Retorna uma descricao em portugues do status de processamento do SMS. <br>
     */
    public static String getDesStatusProcessamento(int status)
    {
        switch (status)
        {
            case SMS_STATUS_NAO_ENVIADO:     return "Pendente de processamento";
            case SMS_STATUS_ENVIANDO:        return "Enviando para o destinatário";
            case SMS_STATUS_ENVIADO:         return "Enviado para a plataforma de SMS";
            case SMS_STATUS_ENTREGE:         return "Entregue no destinatário";
            case SMS_STATUS_EXPIRADO:        return "Tempo de envio expirado";
            case SMS_STATUS_NAO_ENTREGAVEL:  return "Não entregável";
            case SMS_STATUS_REMOVIDO:        return "Descartado pela plataforma de SMS";
            case SMS_STATUS_ACEITO:          return "Mensagem lida pelo assinante";
            case SMS_STATUS_INVALIDO:        return "Mensagem inválida";
            case SMS_STATUS_REJEITADO:       return "Rejeitado pela plataforma de SMS";
            case SMS_STATUS_ERRO:            return "Erro na plataforma de SMS";
        }
        return "Desconhecido";
    }

    /**
     * Retorna uma descricao em portugues do status de SMSC. <br>
     * Esse retorno somente faz sentido quando o status de processamento eh SMS_STATUS_ERRO
     */
    public static String getDesStatusSMSC(int status)
    {
        switch (status)
        {
            case ESME_ROK:              return "OK";
            case ESME_RINVMSGLEN:       return "Tamanho do SM inválido";
            case ESME_RINVCMDLEN:       return "Comprimento do comando inválido";
            case ESME_RINVCMDID:        return "COMMAND ID inválido";
            case ESME_RINVBNDSTS:       return "BIND STATUS incorreto para o comando";
            case ESME_RALYBND:          return "ESME já está no estado BOUND";
            case ESME_RINVPRTFLG:       return "Flag PRIORITY inválida";
            case ESME_RINVREGDLVFLG:    return "Flag REGISTERED DELIVERY inválida";
            case ESME_RSYSERR:          return "Erro de sistema";
            case ESME_RINVSRCADR:       return "Endereço de origem inválido";
            case ESME_RINVDSTADR:       return "Endereço de destino inválido";
            case ESME_RINVMSGID:        return "MESSAGE ID inválido";
            case ESME_RBINDFAIL:        return "Falha no BIND";
            case ESME_RINVPASWD:        return "Senha inválida";
            case ESME_RINVSYSID:        return "SYSTEM ID inválido";
            case ESME_RCANCELFAIL:      return "Falha no cancelamento de SM";
            case ESME_RREPLACEFAIL:     return "Falha na substituição de SM";
        }
        return "Desconhecido";
    }

    /**
     * @see #getDesStatusSMSC(int)
     */
    public static String getDesStatusSMSC(Integer status)
    {
        if (status == null)
            return null;

        return RespostaSMSC.getDesStatusSMSC(status.intValue());
    }

}
