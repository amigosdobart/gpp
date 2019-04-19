package com.brt.gpp.aplicacoes.gerenciarSMS;

import com.brt.gpp.comum.conexoes.smpp.ConexaoSMPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.gerentesPool.*;

import java.sql.*;

/**
*
* Esta classe é uma thread realiza o envio de SMS, verificação do recebimento da mensagem pelo assinante
* e recebimento da resposta do usuário, para a verificação do funcionamento do serviço de determinado 
* assinante. A cada etapa completada no processo uma nova linha é inserida na tabela de status do processo.
*   
* <P> Versao:        	1.0
*
* @Autor:            	Marcelo Alves Araujo
* Data:                 27/06/2005
*
* Modificado por:
* Data:
* Razao:
*
*/

public class GerenciadorSMS extends Thread
{
	// Instância para o Gerente
	private static 	GerenciadorSMS	instancia;
	// Gerente de LOG
	private 		GerentePoolLog	gerenteLog;
	// ID do processo
	private	static	long			idProcesso;
	// Objeto para sincronismo das conexões com o SMSC
	private			Object			conexaoSMS;
	// Endereço IP da SMSC
	private			String			endereco;
	// Porta de conexão da SMSC
	private			int				porta;
	
	/**
	 * Metodo....:GerenciadorSMS
	 * Descricao.:Construtor (inicializador da thread)
	 * @throws GPPInternalErrorException
	 */
	private GerenciadorSMS()
	{
		this.gerenteLog = GerentePoolLog.getInstancia(this.getClass());

		idProcesso = this.gerenteLog.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
		
		this.conexaoSMS = new Object();
		
		this.gerenteLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_GERENTE_TESTE_SMS,"GerenciadorSMS","Inicializado o teste do servico de SMS.");
		
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		
		this.porta = arqConf.getPortaMaquinaSMPP();
		
		this.endereco = arqConf.getEnderecoMaquinaSMPP();
		
		// Inicializa processamento
		// Desabilitado por não ser utilizado
		//this.start();
	}
	
	/**
	 * Metodo....:getInstancia
	 * Descricao.:Retorna uma instancia do gerente de SMS
	 * @return GerenciadorSMS	- Instancia da classe
	 * @throws GPPInternalErrorException
	 */
	public static GerenciadorSMS getInstancia()
	{
		GerentePoolLog gerenteLog = GerentePoolLog.getInstancia(GerenciadorSMS.class);
		
		gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"GerenteTesteSMS","GET_INSTANCIA");
		
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		
		// Se o teste deve ser executado		
		if (arqConf.deveGerenciarSMS())
			if (instancia == null)
				instancia = new GerenciadorSMS();
		else
			gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"GerenteTesteSMS","O Gerenciador de Testes de SMS nao foi iniciado corretamente.");
		
		return instancia;
	}

	/**
	 * Metodo....:enviaSMS
	 * Descricao.:Este método envia o SMS e armazena na tabela o status 1(mensagem enviada) na tabela            
	 * @param mensagem	- Mensagem a ser enviada ao Assinante
	 * @param destino	- MSISDN do assinante a ser testado
	 * @throws GPPInternalErrorException
	 */
	public void enviaSMS(String mensagem, String destino) throws GPPInternalErrorException
	{
		ConsumidorSMS sms = ConsumidorSMS.getInstance(idProcesso);
		
		if(destino.matches(Definicoes.MASCARA_GSM_BRT))
		{
			if(sms.gravaMensagemSMS(destino,mensagem,Definicoes.SMS_PRIORIDADE_MENOS_UM, Definicoes.SMS_BROADCAST,idProcesso))
				insereBroadcastLog(destino, Definicoes.MENSAGEM_ENVIADA, Definicoes.GPP_OPERADOR, mensagem);
			else
				insereBroadcastLog(destino, Definicoes.MENSAGEM_NAO_ENVIADA, Definicoes.GPP_OPERADOR, mensagem);
		}
		
		this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMSMulti","Envio das mensagens");
		
		/*
		synchronized (this.conexaoSMS)
		{	
			ConexaoSMPP sms = new ConexaoSMPP(idProcesso);
			String msgID = null;
			
			try 
			{
				// Busca o originador da chamada
				MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
				String smsOriginador = confGPP.getMapValorConfiguracaoGPP("ORIGINADOR_CHAMADA_SMS");
				
				// Conecta ao SMSC para envio
				sms.conectar(this.endereco,this.porta,Definicoes.SMPP_TRANSMITIR);
	
				// Cria que contem dado para o envio da mensagem
	            SubmitSM requisicao = new SubmitSM();
	            
				// Envia a mensagem e recebe o ID
				msgID = sms.enviar(mensagem, smsOriginador, destino, requisicao);
								
				if(msgID != null)
					insereTabela(destino,Definicoes.STATUS_ENVIADO,msgID);		
			} 
			catch (Exception e) 
			{
				this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMS","Erro ao conectar ao SMSC");
				throw new GPPInternalErrorException("Erro SMSC: " + e);
			} 
			finally
			{
				// Fecha a conexão com o SMSC
				sms.desconectar();
				
				this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMS","Envio da mensagem " + msgID);
			}
		}*/
	}
	
	/**
	 * Metodo....:enviaSMSMulti
	 * Descricao.:Este método envia o SMS        
	 * @param mensagem	- Mensagem a ser enviada ao Assinante
	 * @param destinos	- Lista de MSISDN dpara os quais são enviadas as mensagens
	 * @param operador	- Operador responsável pelo envio do broadcast
	 * @throws GPPInternalErrorException
	 */
	public void enviaSMSMulti(String mensagem, String destinos, String operador) throws GPPInternalErrorException
	{
		ConsumidorSMS sms = ConsumidorSMS.getInstance(idProcesso);
				
		// Envia as mensagens
		String [] listaMsisdn = destinos.split(";");
		for(int i=0; i<listaMsisdn.length; i++)
		{
			if(listaMsisdn[i].matches(Definicoes.MASCARA_GSM_BRT))
			{
				if(sms.gravaMensagemSMS(listaMsisdn[i],mensagem,Definicoes.SMS_PRIORIDADE_MENOS_UM, Definicoes.SMS_BROADCAST,idProcesso))
					insereBroadcastLog(listaMsisdn[i], Definicoes.MENSAGEM_ENVIADA, operador, mensagem);
				else
					insereBroadcastLog(listaMsisdn[i], Definicoes.MENSAGEM_NAO_ENVIADA, operador, mensagem);
			}
		}
		
		this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMSMulti","Envio das mensagens");
		/*
		ConexaoSMPP sms = new ConexaoSMPP(idProcesso);
		
		try 
		{
			// Busca o originador da chamada
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			String smsOriginador = confGPP.getMapValorConfiguracaoGPP("ORIGINADOR_BROADCAST_SMS");
			
			// Conecta ao SMSC para envio
			sms.conectar(endereco,porta,Definicoes.SMPP_TRANSMITIR);
			
			// Cria que contem dado para o envio da mensagem
			SubmitSM requisicao = new SubmitSM();
            
			// Envia as mensagens
			String [] lista = destinos.split(";");
			for(int i=0; i<lista.length; i++)
			{
				if(sms.enviar(mensagem, smsOriginador, lista[i], requisicao) != null)
					insereBroadcastLog(lista[i], Definicoes.MENSAGEM_ENVIADA, operador, mensagem);
				else
					insereBroadcastLog(lista[i], Definicoes.MENSAGEM_NAO_ENVIADA, operador, mensagem);
			}
		} 
		catch (Exception e) 
		{
			this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMSMulti","Erro ao conectar ao SMSC: "+e);
			throw new GPPInternalErrorException("Erro SMSC: " + e);
		} 
		finally
		{
			// Fecha a conexão com o SMSC
			sms.desconectar();
			
			this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMSMulti","Envio das mensagens");
		}*/
	}
			
	/**
	 * Metodo....:recebeSMS
	 * Descricao.:Este método recebe msisdn do assinante que respondeu à mensagem 
	 * 			e armazena na tabela o status 3(mensagem respondida) na tabela
	 */
	public void recebeSMS()
	{
		synchronized (this.conexaoSMS)
		{		
			ConexaoSMPP sms = new ConexaoSMPP(idProcesso);
			
			try 
			{
				// Busca o tempo de espera entre as execucoes de producao de recargas
				MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
				int timeOut = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("VERIFICACAO_SMS_ESPERA"));
				
				// Conecta ao SMSC para envio
				sms.conectar(endereco,porta,Definicoes.SMPP_RECEBER);
				Thread.sleep(timeOut*1000);				
			} 
			catch (InterruptedException e) 
			{
				this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"recebeSMS","Erro no sleep da thread: " + e);
			} 
			catch (Exception e) 
			{
				this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"recebeSMS","Erro ao conectar ao SMSC: " + e);
			}
			finally
			{
				sms.desconectar();
			}
		}
	}
	
	/**
	 * Metodo....:getStatusProcesso
	 * Descricao.:Busca na tabela o status atual da verificação
	 * @return String - Status do processo de verificação
	 */
	public String getStatusProcesso(String msisdn)
	{
		String status = null;
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sqlStatus = 	"SELECT " +
								" maximo AS status " +
								"FROM " +
								" (SELECT " +
								"  sms.idt_msisdn AS msisdn, " +
								"  sms.id_mensagem AS mensagem, " +
								"  MAX(sms.idt_status_processamento) AS maximo " +
								" FROM " +
								"  tbl_ger_verificacao_sms sms " +
								" WHERE " +
								"  sms.idt_msisdn = ? " +
								" GROUP BY " +
								"  sms.idt_msisdn, sms.id_mensagem)  " +
								"WHERE maximo < 3";
			Object parametro[] = {msisdn};
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlStatus,parametro,idProcesso);
			
			if(rs.next())
				status = rs.getString("status");
			
		}
		catch(SQLException e)
		{
			this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"getStatusProcesso","Erro na consulta de status: " + e);
		}
		catch(GPPInternalErrorException e)
		{
			this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"getStatusProcesso","Erro na consulta: "+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return status;
	}
	
	/**
	 * Metodo....:getMsgID
	 * Descricao.:Busca na tabela o ID da mensagem de verificação
	 * @return String - ID da mensagem de verificação 
	 */
	public String getMsgID(String msisdn)
	{
		String msgID = null;
		
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sqlMsg = "SELECT " +
							" mensagem AS msgid " +
							"FROM " +
							" (SELECT " +
							"  sms.idt_msisdn AS msisdn, " +
							"  sms.id_mensagem AS mensagem, " +
							"  MAX(sms.idt_status_processamento) AS maximo " +
							" FROM " +
							"  tbl_ger_verificacao_sms sms " +
							" WHERE " +
							"  sms.idt_msisdn = ? " +
							" GROUP BY " +
							"  sms.idt_msisdn, sms.id_mensagem)  " +
							"WHERE maximo < 3";
			
			Object parametro[] = {msisdn};
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlMsg,parametro,idProcesso);
			
			if(rs.next())
				msgID = rs.getString("msgid");
		}
		catch(SQLException e)
		{
			this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"getMsgID","Erro na consulta do ID da mensagem: "+e);
		}
		catch(GPPInternalErrorException e)
		{
			this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"getMsgID","Erro na consulta: "+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return msgID;
	}
	
	/**
	 * Metodo....:validaEnvio
	 * Descricao.:Este método recebe a ID da mensagem e verifica a se o assinante recebeu a mensagem 
	 * 			e armazena na tabela o status 2(mensagem recebida) na tabela
	 */
	public void validaEnvio()
	{
		synchronized (this.conexaoSMS)
		{
			PREPConexao conexaoPrep = null;
			ConexaoSMPP sms = new ConexaoSMPP(idProcesso);
			
			try
			{
				conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
				String sqlMsg =	"SELECT " +
								" sms.idt_msisdn AS msisdn, " +
								" sms.id_mensagem AS msgid, " +
								" MAX(sms.idt_status_processamento) AS status, " +
								" MAX(sms.dat_processamento) AS data " +
								"FROM " +
								" tbl_ger_verificacao_sms sms " +
								"GROUP BY " +
								" sms.idt_msisdn, " +
								" sms.id_mensagem";
				
				ResultSet rs = conexaoPrep.executaPreparedQuery(sqlMsg,null,idProcesso);
				
				// Busca o originador da chamada
				MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
				String smsOriginador = confGPP.getMapValorConfiguracaoGPP("ORIGINADOR_CHAMADA_SMS");
				
				sms.conectar(endereco,porta,Definicoes.SMPP_TRANSMITIR);
				
				while(rs.next())
					if(rs.getString("status").equals(Definicoes.STATUS_ENVIADO))
						if(sms.validaEnvio(rs.getString("msgid"),smsOriginador))
							insereTabela(rs.getString("msisdn"),Definicoes.STATUS_RECEBIDO,rs.getString("msgid"));
			} 
			catch(SQLException e)
			{
				this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"validaEnvio","Erro na consulta de status: "+e);
			}
			catch(GPPInternalErrorException e)
			{
				this.gerenteLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"validaEnvio","Erro na consulta de status: "+e);
			}
			catch (Exception e) 
			{
				this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"validaEnvio","Erro ao conectar ao SMSC: "+e);
			} 
			finally
			{
				sms.desconectar();
				GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
			}
		}
	}
	
	/**
	 * Metodo....:insereTabela
	 * Descricao.:Insere um registro na tabela tbl_ger_verificacao_sms
	 * @param msisdn	- MSISDN do assinante
	 * @param status 	- Status da verificação de SMS
	 * @param msgID 	- ID da mensagem enviadaDados da Recarga
	 */
	public boolean insereTabela(String msisdn, String status, String msgID)
	{
		boolean sucesso = false;
		PREPConexao conexaoPrep = null;
		try
	    {
	        // Seleciona conexão do pool Prep Conexão
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			
		    // Query que retorna o total de ligações locais, 0800 e à cobrar recebidas pelo cliente durante o ciclo
		    String sqlInsere = 	"INSERT INTO " +
								" tbl_ger_verificacao_sms " +
								" (idt_msisdn,dat_processamento,idt_status_processamento,id_mensagem) " +
								"VALUES " +
								" (?,SYSDATE,?,?)";
		    
		    Object parametros[] = {msisdn,new Integer(status),msgID};
		    
		    // Executa a query, incluindo o registro da atualização no Banco
		    if (conexaoPrep.executaPreparedUpdate(sqlInsere, parametros, idProcesso) > 0)
		    	sucesso = true;	
		    conexaoPrep.commit();
		}
	    catch (GPPInternalErrorException e)
		{
	    	this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"insereTabela","Erro ao inserir dodos na tabela: "+e);
		} 
	    catch (SQLException e) 
	    {
	    	this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"insereTabela","Erro ao atualizar tabela: "+e);
		} 
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}		
		return sucesso;
	}
	
	/**
	 * Metodo....:insereBroadcastLog
	 * Descricao.:Insere um registro na tabela tbl_ger_broadcast_sms
	 * @param msisdn	- MSISDN do assinante
	 * @param status 	- Status da mensagem S ou N
	 * @param operador	- Operador que realizou o broadcast de sms
	 * @param mensagem	- Mensagem a enviada
	 */
	public boolean insereBroadcastLog(String msisdn, String status, String operador, String mensagem)
	{
		boolean sucesso = false;
		
		PREPConexao conexaoPrep = null;
		
		try
	    {
	        // Seleciona conexão do pool Prep Conexão
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			
		    // Query que retorna o total de ligações locais, 0800 e à cobrar recebidas pelo cliente durante o ciclo
		    String sqlInsere = 	"INSERT INTO " +
								" tbl_ger_broadcast_sms " +
								" (dat_envio,idt_msisdn,idt_status_envio,nom_operador,des_mensagem) " +
								"VALUES " +
								" (SYSDATE,?,?,?,?)";
		    
		    Object parametros[] = {msisdn,status,operador,mensagem};
		    
		    // Executa a query, incluindo o registro da atualização no Banco
		    if (conexaoPrep.executaPreparedUpdate(sqlInsere, parametros, idProcesso) > 0)
		    	sucesso = true;	
		    
		    conexaoPrep.commit();
		}
	    catch (GPPInternalErrorException e)
		{
	    	this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"insereBroadcastLog","Erro ao inserir dado na tabela: "+e);
		}
	    catch (SQLException e) 
	    {
	    	this.gerenteLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"insereBroadcastLog","Erro ao atualizar tabela: "+e);
		} 
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return sucesso;
	}
		
	/**
	 * Metodo....:run
	 * Descricao.:Este metodo e o tratamento de execucao da thread de recarga
	 * @see run
	 */
	public void run()
	{
		// Fica em processamento buscando os proximos registros a serem processados
		while(true)
		{
			validaEnvio();
			recebeSMS();
		}
	}
}