package com.brt.gpp.aplicacoes.gerenteSMS;

import com.brt.gpp.comum.conexoes.smpp.TrataSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
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

public class GerenteTesteSMS extends Thread
{
	// Instância para o Gerente
	private static 	GerenteTesteSMS	instancia;
	// Gerente de LOG
	private 		GerentePoolLog	gerenteLog;
	// ID do processo
	private			long			idProcesso;
	// Objeto para sincronismo das conexões com o SMSC
	private			Object			conexaoSMS; 
	
	/**
	 * Metodo....:GerenteTesteSMS
	 * Descricao.:Construtor (inicializador da thread)
	 * @throws GPPInternalErrorException
	 */
	private GerenteTesteSMS()
	{
		this.gerenteLog  = GerentePoolLog.getInstancia(this.getClass());

		this.idProcesso  = this.gerenteLog.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
		
		this.conexaoSMS	= new Object();
		
		this.gerenteLog.log(this.idProcesso,Definicoes.INFO,Definicoes.CL_GERENTE_TESTE_SMS,"GerenteTesteSMS","Inicializado o teste do servico de SMS.");
		
		// Inicializa processamento
		// Desabilitado por não ser utilizado
		//this.start();
	}
	
	/**
	 * Metodo....:getInstancia
	 * Descricao.:Retorna uma instancia do gerente de SMS
	 * @return GerenteTesteSMS	- Instancia da classe
	 * @throws GPPInternalErrorException
	 */
	public static GerenteTesteSMS getInstancia()
	{
		GerentePoolLog gerenteLog = GerentePoolLog.getInstancia(GerenteTesteSMS.class);
		
		gerenteLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"GerenteTesteSMS","GET_INSTANCIA");
		
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		
		// Se o teste deve ser executado		
		if (arqConf.deveGerenciarSMS())
		{
			if (instancia == null)
				instancia = new GerenteTesteSMS();
		}
		else
		{
			gerenteLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"GerenteTesteSMS","O Gerenciador de Testes de SMS não foi iniciado corretamente.");
		}
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
		synchronized (this.conexaoSMS)
		{			
			// Busca o originador da chamada
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			String smsOriginador = confGPP.getMapValorConfiguracaoGPP("ORIGINADOR_CHAMADA_SMS");
			String msgID = null;
			
			// Conecta ao SMSC para envio
			TrataSMS.conectar(Definicoes.SMPP_ENDERECO,Definicoes.SMPP_PORTA,Definicoes.SMPP_TRANSMITIR);
			
			// Envia a mensagem e recebe o ID
			msgID = TrataSMS.enviar(mensagem, smsOriginador, destino);
			
			// Fecha a conexão com o SMSC
			TrataSMS.desconectar();
			
			if(msgID != null)
				insereTabela(destino,Definicoes.STATUS_ENVIADO,msgID);
			
			this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMS","Envio da mensagem " + msgID);
		}
	}
	
	/**
	 * Metodo....:enviaSMSMulti
	 * Descricao.:Este método envia o SMS        
	 * @param mensagem	- Mensagem a ser enviada ao Assinante
	 * @param destinos	- Lista de MSISDN dpara os quais são enviadas as mensagens
	 * @throws GPPInternalErrorException
	 */
	public void enviaSMSMulti(String mensagem, String destinos) throws GPPInternalErrorException
	{
		synchronized (this.conexaoSMS)
		{			
			// Busca o originador da chamada
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			String smsOriginador = confGPP.getMapValorConfiguracaoGPP("ORIGINADOR_CHAMADA_SMS");
			
			// Conecta ao SMSC para envio
			TrataSMS.conectar(Definicoes.SMPP_ENDERECO,Definicoes.SMPP_PORTA,Definicoes.SMPP_TRANSMITIR);
			
			// Envia a mensagem e recebe o ID
			String [] lista = destinos.split(";");
			for(int i=0; i<lista.length; i++)
				TrataSMS.enviar(mensagem, smsOriginador, lista[i]);
			
			// Fecha a conexão com o SMSC
			TrataSMS.desconectar();
			
			this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"enviaSMSMulti","Envio das mensagens");
		}
	}
			
	/**
	 * Metodo....:recebeSMS
	 * Descricao.:Este método recebe msisdn do assinante que respondeu à mensagem 
	 * 			e armazena na tabela o status 3(mensagem respondida) na tabela
	 * @throws GPPInternalErrorException
	 * @throws InterruptedException
	 */
	public void recebeSMS() throws GPPInternalErrorException, InterruptedException
	{
		synchronized (this.conexaoSMS)
		{			

			// Busca o tempo de espera entre as execucoes de producao de recargas
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			int timeOut = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("VERIFICACAO_SMS_ESPERA"));
			
			// Conecta ao SMSC para envio
			TrataSMS.conectar(Definicoes.SMPP_ENDERECO,Definicoes.SMPP_PORTA,Definicoes.SMPP_RECEBER);
			Thread.sleep(timeOut*1000);
			TrataSMS.desconectar();
		}
	}
	
	/**
	 * Metodo....:getStatusProcesso
	 * Descricao.:Busca na tabela o status atual da verificação
	 * @return String - Status do processo de verificação
	 * @throws GPPInternalErrorException
	 */
	public String getStatusProcesso(String msisdn) throws GPPInternalErrorException
	{
		String status = null;
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
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
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlStatus,parametro,this.idProcesso);
			
			if(rs.next())
				status = rs.getString("status");
			
		}
		catch(SQLException e)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"getStatusProcesso","Erro na consulta de status");
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(conexaoPrep,this.idProcesso);
		}
		return status;
	}
	
	/**
	 * Metodo....:getMsgID
	 * Descricao.:Busca na tabela o ID da mensagem de verificação
	 * @return String - ID da mensagem de verificação 
	 * @throws GPPInternalErrorException
	 */
	public String getMsgID(String msisdn) throws GPPInternalErrorException
	{
		String msgID = null;
		
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
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
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlMsg,parametro,this.idProcesso);
			
			if(rs.next())
				msgID = rs.getString("msgid");
		}
		catch(SQLException e)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"getMsgID","Erro na consulta do ID da mensagem");
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(conexaoPrep,this.idProcesso);
		}
		return msgID;
	}
	
	/**
	 * Metodo....:validaEnvio
	 * Descricao.:Este método recebe a ID da mensagem e verifica a se o assinante recebeu a mensagem 
	 * 			e armazena na tabela o status 2(mensagem recebida) na tabela
	 * @throws GPPInternalErrorException
	 */
	public void validaEnvio()
	{
		synchronized (this.conexaoSMS)
		{
			PREPConexao conexaoPrep = null;
			try
			{
				conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
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
				
				ResultSet rs = conexaoPrep.executaPreparedQuery(sqlMsg,null,this.idProcesso);
				
				TrataSMS.conectar(Definicoes.SMPP_ENDERECO,Definicoes.SMPP_PORTA,Definicoes.SMPP_TRANSMITIR);
				
				while(rs.next())
					if(rs.getString("status").equals(Definicoes.STATUS_ENVIADO))
						if(TrataSMS.validaEnvio(rs.getString("msgid"),Definicoes.SMPP_ORIGEM))
							insereTabela(rs.getString("msisdn"),Definicoes.STATUS_RECEBIDO,rs.getString("msgid"));
						
				TrataSMS.desconectar();
			}
			catch(SQLException e)
			{
				this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"validaEnvio","Erro na consulta de status");
			}
			catch(GPPInternalErrorException e)
			{
				this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"validaEnvio","Erro na consulta de status");
			}
			finally
			{
				GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(conexaoPrep,this.idProcesso);
			}
		}
	}
	
	/**
	 * Metodo....:insereTabela
	 * Descricao.:Insere um registro na tabela tbl_ger_verificacao_sms
	 * @param msisdn	- MSISDN do assinante
	 * @param status 	- Status da verificação de SMS
	 * @param msgID 	- ID da mensagem enviadaDados da Recarga
	 * @throws GPPInternalErrorException
	 */
	public boolean insereTabela(String msisdn, String status, String msgID)
	{
		boolean sucesso = false;
		PREPConexao conexaoPrep = null;
		try
	    {
	        // Seleciona conexão do pool Prep Conexão
			conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
			
		    // Query que retorna o total de ligações locais, 0800 e à cobrar recebidas pelo cliente durante o ciclo
		    String sqlInsere = 	"INSERT INTO " +
								" tbl_ger_verificacao_sms " +
								" (idt_msisdn,dat_processamento,idt_status_processamento,id_mensagem) " +
								"VALUES " +
								" (?,SYSDATE,?,?)";
		    
		    Object parametros[] = {msisdn,new Integer(status),msgID};
		    
		    // Executa a query, incluindo o registro da atualização no Banco
		    if (conexaoPrep.executaPreparedUpdate(sqlInsere, parametros, this.idProcesso) > 0)
		    	sucesso = true;			
		}
	    catch (GPPInternalErrorException e)
		{
	    	this.gerenteLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_TESTE_SMS,"insereTabela","Erro na consulta de status");
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(conexaoPrep,this.idProcesso);
		}		
		return sucesso;
	}
		
	/**
	 * Metodo....:run
	 * Descricao.:Este metodo e o tratamento de execucao da thread de recarga
	 * @see run
	 * @deprecated
	 */
	public void run()
	{
		try
		{
			// Fica em processamento buscando os proximos registros a serem processados
			while(true)
			{
				validaEnvio();
				recebeSMS();
			}
		}
		catch(InterruptedException ie)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"run","Sleep de thread interrompido.");
		}
		catch(GPPInternalErrorException ge)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_TESTE_SMS,"run","Erro no teste. Erro"+ge);
		}
	}
}