/*
 * Created on 06/04/2004
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import br.com.brasiltelecom.ppp.model.Ajuste;

import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;

/**
 * Efetua a conexão com o GPP a fim de efetuar um ajuste
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class EfetuaAjusteGPP {
	
	/**
	 * Efetua o ajuste
	 * 
	 * @param ajuste ajuste a ser efetuado
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return 0 -> ajuste efetuado com sucesso, 1 -> ajuste não efetuado com sucesso
	 * @throws Exception
	 */
	public static short doAjuste(Ajuste ajuste, String servidor, String porta) throws Exception{
			
		short ret = 0;
	
		ORB orb = GerenteORB.getORB(servidor, porta);

		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			ret = pPOA.executaAjuste (	ajuste.getMsisdn(), ajuste.getOrigem().getIdCanal() + ajuste.getOrigem().getIdOrigem(), ajuste.getTipoCredito(), ajuste.getValor(), 
													ajuste.getTipoAjuste(), ajuste.getData(), "PPP", ajuste.getUsuario(), ajuste.getDataExpiracaoString());
		}
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}
		return ret;
	}

}
