/*
 * Created on 01/09/2004
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.comum.Definicoes;

/**
 * @author Bernardo Vergne Dias
 * @since 20/12/2006
 */
public class AprovacaoPreviaGPP 
{
		public static void aprovarLote (String servidor, String porta, String lote) throws Exception 
		{				
			
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			try
			{
				if (pPOA.aprovarLote(lote) == Definicoes.RET_ERRO_TECNICO)
				{
					throw new Exception("Erro no processo de aprovação de lote.");
				}
			}
			catch (Exception e) 
			{
				throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
			}
		}
		
		public static void rejeitarLote (String servidor, String porta, String lote) throws Exception 
		{				
			
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			try
			{
				if (pPOA.rejeitarLote(lote) == Definicoes.RET_ERRO_TECNICO)
				{
					throw new Exception("Erro no processo de rejeição de lote.");
				}
			}
			catch (Exception e) 
			{
				throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
			}
		}
}
