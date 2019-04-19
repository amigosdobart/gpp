package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import java.io.File;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula.BatchPrevia;
import br.com.brasiltelecom.ppp.interfacegpp.ProcessosBatchGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.Definicoes;

/**
 * Thread que realiza o processamento de carga dos arquivos de lotes
 * 
 * @author Bernardo Vergne Dias
 * @since 09/01/2007
 */
public class BatchCargaLotes implements Runnable {
	
	private String servGPP 				= null;
	private String portGPP 				= null;
	private String raizEstornoExpurgo 	= null;
	
	private Logger logger 				= null;
	
	private BatchPrevia batchPrevia		= null;
	
	public BatchCargaLotes(String servGPP, String portGPP, 
			String raizEstornoExpurgo, Logger logger)
	{
		this.servGPP = servGPP;
		this.portGPP = portGPP;
		this.raizEstornoExpurgo = raizEstornoExpurgo;
		this.logger = logger;
	}

	public void run() 
	{
		
		try 
		{
			String arquivo = null;
			String dir = raizEstornoExpurgo + java.io.File.separator + Constantes.PATH_ESTORNO_EXPURGO_PP_ENTRADA;
		
			//	Lista os arquivos de lotes disponibilizados na pasta de entrada
			
			File[] dirList = (new File(dir)).listFiles();
			if (dirList != null)
			{
				for (int i = 0; i < dirList.length; i++) 
				{
					arquivo = dirList[i].getPath();
					
					try
					{
						// Requisicao CORBA ao processo batch do GPP
						ProcessosBatchGPP.iniciarProcessoBatch(servGPP, portGPP, 
								Definicoes.IND_CARGA_ARQUIVO_LOTES, new String[] {arquivo}); 
					}
					catch (Exception e)
					{
						logger.error("Erro ao iniciar carga do arquivo de lotes: " + arquivo);	
					}
				}
			}
		
			//	Executa a previa (na mesma thread)
			batchPrevia = new BatchPrevia(servGPP, portGPP, logger);
			batchPrevia.run();

		} catch (Exception e) {
			logger.error("Erro no processamento de carga de lotes e previa de estorno. " + e.getMessage());
		}

	}

}
