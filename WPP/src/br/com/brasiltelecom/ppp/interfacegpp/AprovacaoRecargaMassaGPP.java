
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;
import com.brt.gpp.comum.Definicoes;

/**
 * @author Bernardo Vergne Dias
 * Data: 10/08/2007
 */
public class AprovacaoRecargaMassaGPP 
{
		public static void aprovarLoteRecarga (String servidor, String porta, String lote, String usuario) throws Exception 
		{				
			
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosRecarga".getBytes();
			recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			try
			{
				if (pPOA.aprovarLoteRecarga(lote, usuario) == Definicoes.RET_ERRO_TECNICO)
				{
					throw new Exception("Erro no processo de aprovação de lote de recarga em massa.");
				}
			}
			catch (Exception e) 
			{
				throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
			}
		}
		
		public static void rejeitarLoteRecarga (String servidor, String porta, String lote, String usuario) throws Exception 
		{				
			
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosRecarga".getBytes();
			recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			try
			{
				if (pPOA.rejeitarLoteRecarga(lote, usuario) == Definicoes.RET_ERRO_TECNICO)
				{
					throw new Exception("Erro no processo de rejeição de lote de recarga em massa.");
				}
			}
			catch (Exception e) 
			{
				throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
			}
		}
}
