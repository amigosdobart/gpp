package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.interfacegpp.ProcessosBatchGPP;

import com.brt.gpp.comum.Definicoes;

/**
 * Thread que realiza o processamento de previa de estorno/expurgo pula-pula.
 * 
 * @author Bernardo Vergne Dias
 * @since 09/01/2007
 */
public class BatchPrevia implements Runnable {
	
	private String servGPP 	= null;
	private String portGPP 	= null;
	private Logger logger 	= null;

	public BatchPrevia(String servGPP, String portGPP, Logger logger)
	{
		this.servGPP = servGPP;
		this.portGPP = portGPP;
		this.logger = logger;
	}
	
	public void run() 
	{
		// Executa o processo batch		
		try
		{
			// Requisicao CORBA ao processo batch do GPP
			ProcessosBatchGPP.iniciarProcessoBatch(servGPP, portGPP, 
					Definicoes.IND_ESTORNO_BONUS_PULA_PULA_FRAUDE, new String[] {Definicoes.IDT_PROCESSAMENTO_VALIDACAO});
		}
		catch (Exception e)
		{
			logger.error("Erro ao iniciar prévia de estorno/expurgo.");	
		}

	}

}

