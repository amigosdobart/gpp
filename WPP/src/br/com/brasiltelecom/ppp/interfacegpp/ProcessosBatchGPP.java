/*
 * Created on 01/09/2004
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.processosBatch.orb.processosBatch;
import com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper;
import com.brt.gpp.comum.Definicoes;

/**
 * @author Bernardo Vergne Dias
 * @since 03/01/2007
 */
public class ProcessosBatchGPP 
{
		public static void iniciarProcessoBatch (String servidor, String porta, int ID,  String[] args) throws Exception 
		{				
			
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
			processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			try
			{  
				pPOA.executaProcessoBatch(ID, args);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
			}
		}
}
