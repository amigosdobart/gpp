/*
 * Criado em 22/06/2005
 *
 */
package com.brt.gpp.comum.conexoes.smpp;

import com.brt.gpp.aplicacoes.gerenteSMS.GerenteTesteSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.logica.smpp.*;
import com.logica.smpp.pdu.*;

/**
 * @author Marcelo Alves Araujo
 *
 */
public class TrataSMS extends SmppObject implements ServerPDUEventListener
{
    static private boolean conectado;
    static private TrataSMS pduListener = null;
    static private Session sessao = null;
    
    public TrataSMS()
    {          
        conectado = false;
    }

    public static String conectar(String enderecoIp, int porta, String tipoConexao)
    {
    	String msisdn = null;
        try
        {
            BindRequest requisicao = null;

            // Seta os a porta, o IP para conexão e a porta
            TCPIPConnection conexao = new TCPIPConnection(enderecoIp, porta);
            conexao.setReceiveTimeout(Data.CONNECTION_RECEIVE_TIMEOUT);
                        
            sessao = new Session(conexao);
            
            // Conexão de transmissão ou recepção
            if(tipoConexao.compareToIgnoreCase("t") == 0)
            {
                requisicao = new BindTransmitter();
                
                // Seta os parâmetros a conexão
                requisicao.setSystemId("PF01_");
                requisicao.setPassword("7sTmW3s4");
                requisicao.setSystemType("MMSTECMNCTA");
                requisicao.setInterfaceVersion((byte) 52);
                requisicao.setAddressRange(Data.GSM_TON_INTERNATIONAL,Data.GSM_NPI_ISDN,"11*");
                
                // Conecta ao SMSC
                BindResponse resposta = sessao.bind(requisicao);
                
                if ( resposta.getCommandStatus() == Data.ESME_ROK )
                    conectado = true;
            }
            else if(tipoConexao.compareToIgnoreCase("r") == 0)
            {
                requisicao = new BindReceiver();
                
                // Seta os parâmetros a conexão
                requisicao.setSystemId("PF01_");
                requisicao.setPassword("7sTmW3s4");
                requisicao.setSystemType("MMSTECMNCTA");
                requisicao.setInterfaceVersion((byte) 52);
                requisicao.setAddressRange(Data.GSM_TON_INTERNATIONAL,Data.GSM_NPI_ISDN,"11*");
                
                pduListener = new TrataSMS();
                
                // Conecta ao SMSC
                BindResponse resposta = sessao.bind(requisicao, pduListener);
                
                if ( resposta.getCommandStatus() == Data.ESME_ROK )
                    conectado = true;
            }            
        }
        catch ( Exception e )
        {
            System.out.println(e);
        }
        return msisdn;
    }
    
    public static void desconectar()
    {
        try
        {
            if ( !conectado )
            {
                System.out.println("Nao pode desconectar, nao esta conectado.");
                return;
            }
            
            // Desconecta
            UnbindResp resposta = sessao.unbind();
            
            // Testa se foi desconenctado
            if(resposta.getCommandStatus() == Data.ESME_ROK)
                conectado = false;
        }
        catch ( Exception e )
        {
            System.out.println(e);
        }
    }
    
    public static String enviar( String mensagem, String origem, String destino )
    {
        // TODO: Definir o período na ConfiguracaoGPP para 1 dia
        String periodoValidade = "000001000000000R";
        
        String messageId = null;
        
        try
        {
            // Cria Objeto
            SubmitSM requisicao = new SubmitSM();
            
            // Endereço de envio
            requisicao.setSourceAddr(Data.GSM_TON_NATIONAL, Data.GSM_NPI_ISDN, origem);
            // Telefone de destino
            requisicao.setDestAddr(Data.GSM_TON_NATIONAL, Data.GSM_NPI_ISDN, destino);
            // Mensagem
            requisicao.setShortMessage(mensagem);
            // Tempo que a mensagem espera no SMSC
            requisicao.setValidityPeriod(periodoValidade);
            // O envio de resposta quando o usuário receber e ler a mensagem           
            requisicao.setRegisteredDelivery((byte)(Data.SM_SME_ACK_BOTH_REQUESTED+Data.SM_SMSC_RECEIPT_REQUESTED));
                         
            // Incrementa o número sequencial
            requisicao.assignSequenceNumber(true);
            
            // Envia o SMS e pega o ID da mensagem
            SubmitSMResp resposta = sessao.submit(requisicao);
            
            if(resposta.getCommandStatus() == Data.ESME_ROK)
            {
                //Pega o ID da mensagem
	            messageId = resposta.getMessageId();
            }
        }
        catch ( Exception e )
        {
        	System.out.println(e);
        }
        return messageId;
    }
    
    public static boolean validaEnvio( String messageId, String origem )
    {
        boolean recebeu = false;
        try
        {
            QuerySM requisicao = new QuerySM();
            
            // Parâmetros da mensagem procurada
            requisicao.setMessageId(messageId);
            requisicao.setSourceAddr(Data.GSM_TON_NATIONAL, Data.GSM_NPI_ISDN, origem);
            
            QuerySMResp resposta = sessao.query(requisicao);
            
            // Se o assinante já recebeu a mensagem
            if((resposta.getMessageState() == 0)||(resposta.getMessageState() == Data.SM_STATE_DELIVERED)||(resposta.getMessageState() == Data.SM_STATE_ACCEPTED))
                recebeu = true;
        }
        catch ( Exception e )
        {
        	System.out.println(e);
        }
        return recebeu;
    }
    
    public static boolean cancelar( String messageId, String origem, String destino )
    {
        boolean concluido = false;
        try
        {
            CancelSM requisicao = new CancelSM();
            
            // Seta os parâmetros para cancelar o envio
            requisicao.setMessageId(messageId);
            requisicao.setSourceAddr(origem);
            requisicao.setDestAddr(destino);
            
            CancelSMResp resposta = sessao.cancel(requisicao);
            if(resposta.getCommandStatus() == Data.ESME_ROK)
                concluido = true;
        }
        catch ( Exception e )
        {
        	System.out.println(e);
        }
        return concluido;
    }
    
    public void handleEvent(ServerPDUEvent event)
	{
    	PDU pdu = event.getPDU();
	     if(pdu.isRequest())
	     {
	     	 if( pdu.getCommandId() == Data.DELIVER_SM )
	     	 {
	     	 	try 
				{
	     	 		String msisdn = ((DeliverSM) pdu).getSourceAddr().getAddress();
	     	 		GerenteTesteSMS gerente = GerenteTesteSMS.getInstancia();
					if(gerente.getStatusProcesso(msisdn).equals(Definicoes.STATUS_RECEBIDO))
						gerente.insereTabela(msisdn,Definicoes.STATUS_RESPONDIDO,gerente.getMsgID(msisdn));
				} 
	     	 	catch (GPPInternalErrorException e) 
				{
	     	 		System.out.println(e);
	     	 	}
	     	 }
	     }
	}    
}
