package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 * Efetua a conexão com o GPP a fim de obter o cartão
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 * 
 * Adaptado para controle total por: Bernardo Vergne Dias
 * 06/07/2007
 */
public class ConsultaVoucherGPP 
{
		
	/**
	 * Obtém o XML dos dados do cartão vindo do GPP
	 * 
	 * @param idCartao número do cartão
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return código XML
	 * @throws Exception
	 */
	public static String getXml(String idCartao, String servidor, String porta) throws Exception
	{
		String ret = "";
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			ret = pPOA.consultaVoucher (idCartao);
		}
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}

		return ret;
	}
	
}
