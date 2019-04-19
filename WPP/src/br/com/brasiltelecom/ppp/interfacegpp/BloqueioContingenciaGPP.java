/*
 * Created on 31/08/2004
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;

/**
 * @author Henrique Canto
 *
 */
public class BloqueioContingenciaGPP 
{
	public static long bloqueioServicoCrm (String servidor, String porta, String msisdn) throws Exception {
			
	long idAtividade = 0;
	
	ORB orb = GerenteORB.getORB(servidor, porta);
	
	byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
	
	aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

	try
	{
		idAtividade = pPOA.bloquearServicos(msisdn);
		
	}
	catch (Exception e) 
	{
		throw new Exception("Erro ao conectar no GPP: " + e.getMessage());
	}
	
	return idAtividade;
}
}
