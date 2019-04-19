package com.brt.gpp.aplicacoes.enviarSMS;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.smpp.ConexaoSMPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.ProdutorSMS;
import com.logica.smpp.pdu.SubmitSM;

/**
  * Este arquivo refere-se a classe EnvioSMS, responsavel pela implementacao da
  * logica de armazenamento e envio de mensagens de SMS para assinantes
  * <hr>
  * <b>Modificado por:</b> Luciano Vilela<br>
  * <b>Data:</b> 05/05/2006<br>
  * <b>Razao:</b> O processo utilizar o Object GerenciadorSMS para envio dos SMS<br>
  * <p>
  * <b>Modificado por:</b>	Leone Parise<br>
  * <b>Data:</b> 14/08/2007<br>
  * <b>Razao:</b> Tratamento do originador de SMS incluso pela demanda Call me Back.<br>
  * <hr>
  *
  * @author		Camile Cardoso Couto
  * @version	1.0, 23/03/2004
  */
public class EnvioSMSSMPP extends Thread implements InterfaceEnvioSMS
{
	private long 					idProcesso;
	private GerentePoolLog			gerLog;
	private ConexaoSMPP	conexaoSMSC;
	// Endereço IP da SMSC
	private			String			endereco;
	// Porta de conexão da SMSC
	private			int				porta;
	ArquivoConfiguracaoGPP arqConf = null;

	/*
	 * Alteracao
	 * Luciano Vilela
	 * 11/06/2007
	 */


	/**
	 * Metodo...: EnvioSMS
	 * Descricao: Construtor
	 * @param	logId	- Identificador do Processo para Log
	 * @return
	 */
	public EnvioSMSSMPP (long logId) throws GPPInternalErrorException
	{
		arqConf = ArquivoConfiguracaoGPP.getInstance();
	 	idProcesso 	= logId;
		this.porta = arqConf.getPortaMaquinaSMPP();

		this.endereco = arqConf.getEnderecoMaquinaSMPP();
	 	conexaoSMSC	= new ConexaoSMPP(logId);

	 	//conexaoSMSC.start();
	 	gerLog		= GerentePoolLog.getInstancia(this.getClass());

	}

	/**
	 * Metodo....: run
	 * Descricao.: Metodo de execucao da Thread
	 */
	public synchronized void run()
	{
		try
		{
			/* Fica em execucao permanente ate que a Thread seja interrompida */
			while (true)
			{
				/* Fica enviando mensagens SMS enquanto tiver SMS disponiveis
				 * no produtor
				 */
				enviaMensagemSMS();

				/*  Caso o metodo enviaMensagemSMS termine, entao significa
				 *  que nenhum SMS sera enviado, portanto a Thread ficara
				 *  em espera por algum tempo ate tentar novamente consumir SMS
				 */
				gerLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_ENVIO_SMS,"run","Nao ha SMS a serem enviados.thread esperando. Thread:"+this.toString());
				wait(arqConf.getTempoEsperaSMS()*1000);
			}
		}
		catch(InterruptedException e)
		{
			/* Em caso de excecao entao a Thread e interrompida */
			gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_ENVIO_SMS,"run",toString() + " Excecao Interna GPP:"+ e);
		}
	}

	/**
	 * Metodo...: enviaMensagemSMS
	 * Descricao: Le as mensagens que devem ser enviadas,
	 * 			  faz uma comunicao com a plataforma de SMSC e envia o SMS
	 */
	public void enviaMensagemSMS ()
	{
	    /* Obtem uma instancia do Produtor de SMS */
		ProdutorSMS produtorSMS = ProdutorSMS.getInstancia(idProcesso);

		/* Enquanto houver SMS a ser processado */
		DadosSMS dadosSMS;
		while (	(dadosSMS = produtorSMS.consomeSMS(idProcesso)) != null )
		{
			if(!conexaoSMSC.isConectado()){
			 	try {
			 		conexaoSMSC.conectar(this.endereco,this.porta,Definicoes.SMPP_TRANSMITIR);
				 	gerLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_ENVIO_SMS,"Construtor","Inicializando Conexao Envio SMS -> " + toString());
				 	//conectado = true;
			 	} catch (Exception e) {
				 	gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_ENVIO_SMS,"Construtor","Erro ao conectar na SMSC " + toString());
				}
			}
			/* Envia SMS: Em caso de sucesso entao atualiza o status do mesmo */
			try{
				//String smsOriginador = arqConf.getMapValorConfiguracaoGPP("ORIGINADOR_CHAMADA_SMS");
				//conexaoSMSC.conectar(this.endereco,this.porta,Definicoes.SMPP_TRANSMITIR);
				SubmitSM requisicao = new SubmitSM();

				// Envia a mensagem e recebe o ID
				int trys =0;
				String retorno = null;
				do
				{
					/*
					 * POG - Tratar recebimento de SMS para limite de FGN
					 */

					// Caso o MSISDN Originador seja nulo utilizar o Originador padrao.
					if(dadosSMS.getMsisdnOrigem() == null)
						dadosSMS.setMsisdnOrigem(Integer.toString(arqConf.getOriginadorSMSC()));

					if(dadosSMS.getTipo()!= null && dadosSMS.getTipo().equalsIgnoreCase("LIMITE_FGN_MAXIMO"))
						retorno = conexaoSMSC.enviarComResposta(dadosSMS.getMensagem(), dadosSMS.getMsisdnOrigem(), dadosSMS.getMsisdn(), requisicao);
					else
						retorno = conexaoSMSC.enviar(dadosSMS.getMensagem(), dadosSMS.getMsisdnOrigem(), dadosSMS.getMsisdn(), requisicao);

				}
				while(retorno == null && ++trys < 3);
				if(retorno != null)
				{
					dadosSMS.setStatus(Definicoes.SMS_ENVIADO);
					produtorSMS.atualizaStatusSMS(dadosSMS, idProcesso);
					gerLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS","SMS - " + retorno + " enviado com sucesso pela thread - " + toString());
				}
			}
			catch(Exception e)
			{
				/*Acerta o status do SMS voltando para SMS NAO ENVIADO*/
				dadosSMS.setStatus(Definicoes.SMS_NAO_ENVIADO);
				produtorSMS.atualizaStatusSMS(dadosSMS, idProcesso);
				gerLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS","Erro no envio de SMS " + dadosSMS + " " +e.getMessage());
			}
		}
	}

	/**
	 * Metodo...: reiniciaConsumoSMS
	 * Descricao: Notifica a thread para "acordar" e continuar o consumo de SMS
	 *
	 */
	public synchronized void reiniciaConsumoSMS()
	{
		notify();
	}

	/**
	 * Metodo...: close
	 * Descricao: Fecha as referencias para a conexao Middleware SMSC
	 *            e finaliza a execucao da thread
	 */
	public void close()
	{
		/*  Deixa as variaveis utilizadas para referencias com as classes de Log
		 *  e conexao SMSC nulas para serem recolhidas pelo Garbage Colector
		 *  e faz a finalizacao da execucao da thread
		 */
		 if(conexaoSMSC.isConectado())
			 conexaoSMSC.desconectar();

		 gerLog 	 = null;
		 conexaoSMSC = null;

		 interrupt();

		 gerLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS", " Thread " + toString() + " finalizada.");
	}
}