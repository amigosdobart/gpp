//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarSMS;

import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.conexoes.sms.ConexaoMiddlewareSMSC;
import com.brt.gpp.gerentesPool.ProdutorSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.aplicacoes.enviarSMS.DadosSMS;

/**
  *
  * Este arquivo refere-se a classe EnvioSMS, responsavel pela implementacao da
  * logica de armazenamento e envio de mensagens de SMS para assinantes
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Camile Cardoso Couto
  * Data: 				23/03/2004
  *
  * Modificado por: Joao Carlos
  * Data:  16/06/2004
  * Razao: Envio de SMS sera um thread que ficara consumindo
  *        da classe ProdutorSMS os SMS a serem enviados
  *
  */

public class EnvioSMS extends Thread implements InterfaceEnvioSMS
{
	private long 					idProcesso;
	private GerentePoolLog			gerLog;
	private ConexaoMiddlewareSMSC	conexaoSMSC;

	/**
	 * Metodo...: EnvioSMS
	 * Descricao: Construtor
	 * @param	logId	- Identificador do Processo para Log
	 * @return
	 */
	public EnvioSMS (long logId) throws GPPInternalErrorException
	{
	 	idProcesso 	= logId;
	 	conexaoSMSC	= new ConexaoMiddlewareSMSC(idProcesso);
	 	gerLog		= GerentePoolLog.getInstancia(this.getClass());
	 	gerLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_ENVIO_SMS,"Construtor","Inicializando Conexao Envio SMS -> " + toString());
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.aplicacoes.enviarSMS.InterfaceEnvioSMS#run()
	 */
	public synchronized void run()
	{
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
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

	/* (non-Javadoc)
	 * @see com.brt.gpp.aplicacoes.enviarSMS.InterfaceEnvioSMS#enviaMensagemSMS()
	 */
	public void enviaMensagemSMS ()
	{
		try
		{
			/* Obtem uma instancia do Produtor de SMS */
			ProdutorSMS produtorSMS = ProdutorSMS.getInstancia(idProcesso);

			/* Enquanto houver SMS a ser processado */
			DadosSMS dadosSMS;
			while (	(dadosSMS = produtorSMS.consomeSMS(idProcesso)) != null )
			{
				/* Envia SMS: Em caso de sucesso entao atualiza o status do mesmo */
				if ( conexaoSMSC.enviaSMS(dadosSMS.getMsisdn(), dadosSMS.getMensagem()) )
				{
					dadosSMS.setStatus(Definicoes.SMS_ENVIADO);
					produtorSMS.atualizaStatusSMS(dadosSMS, idProcesso);
					gerLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS","SMS - " + dadosSMS + " enviado com sucesso pela thread - " + toString());
				}
				else
				{
					/*Acerta o status do SMS voltando para SMS NAO ENVIADO*/
					dadosSMS.setStatus(Definicoes.SMS_NAO_ENVIADO);
					produtorSMS.atualizaStatusSMS(dadosSMS, idProcesso);
					gerLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS","Erro no envio de SMS " + dadosSMS + " pela thread - " + toString());
					break;
				}
			}
		}
		catch (GPPInternalErrorException e)
		{
			gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS",toString() + " Excecao Interna GPP ocorrida: "+ e);
		}
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.aplicacoes.enviarSMS.InterfaceEnvioSMS#reiniciaConsumoSMS()
	 */
	public synchronized void reiniciaConsumoSMS()
	{
		notify();
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.aplicacoes.enviarSMS.InterfaceEnvioSMS#close()
	 */
	public void close()
	{
		/*  Deixa as variaveis utilizadas para referencias com as classes de Log
		 *  e conexao SMSC nulas para serem recolhidas pelo Garbage Colector
		 *  e faz a finalizacao da execucao da thread
		 */
		 gerLog 	 = null;
		 conexaoSMSC = null;
		 interrupt();
		 gerLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_ENVIO_SMS,"enviaMensagemSMS", " Thread " + toString() + " finalizada.");
	}
}