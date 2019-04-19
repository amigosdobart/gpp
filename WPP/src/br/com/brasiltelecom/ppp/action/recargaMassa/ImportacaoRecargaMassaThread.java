package br.com.brasiltelecom.ppp.action.recargaMassa;

import java.io.File;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.interfacegpp.ProcessosBatchGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.Definicoes;

/**
 * Thread que realiza o processamento de carga dos arquivos de lotes
 * 
 * @author Bernardo Vergne Dias
 * Data: 09/08/2007
 */
public class ImportacaoRecargaMassaThread implements Runnable 
{
	
	private String servGPP 				= null;
	private String portGPP 				= null;
	private String raizRecargaMassa 	= null;	
	private Logger logger 				= null;
	
	public ImportacaoRecargaMassaThread(String servGPP, String portGPP, 
			String raizRecargaMassa, Logger logger)
	{
		this.servGPP = servGPP;
		this.portGPP = portGPP;
		this.raizRecargaMassa = raizRecargaMassa;
		this.logger = logger;
	}

	public void run() 
	{
		
		try 
		{
			String arquivo = null;
			String dir = raizRecargaMassa + java.io.File.separator + Constantes.PATH_RECARGA_MASSA_ENTRADA;
		
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
								Definicoes.IND_IMPORTACAO_RECARGA_MASSA, new String[] {arquivo}); 
					}
					catch (Exception e)
					{
						logger.error("Erro ao iniciar importação do lote de recarga em massa: " + arquivo);	
					}
				}
			}
		
		} catch (Exception e) 
		{
			logger.error("Erro na importação da lista de recarga em massa. " + e.getMessage());
		}

	}

}
