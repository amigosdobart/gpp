/*
 * Created on 01/09/2004
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;

/**
 * @author HEnrique Canto
 * @return idAtividade(long), -1 para assinante inexistente, -2 para assinante ja desbloqueado
 */
public class DesbloqueioContingenciaGPP 
{
		public static long desastivarHotline (String servidor, String porta, String msisdn, String categoria) throws Exception 
		{				
			long idAtividade = 0;

			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			try
			{
				idAtividade = pPOA.desativarHotLine(msisdn,categoria);
			}
			catch (Exception e) 
			{
				throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
			}
			return idAtividade;
		}
}
