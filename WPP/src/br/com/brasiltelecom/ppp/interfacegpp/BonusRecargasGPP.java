/*
 * Created on 04/08/2004
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

//import org.omg.CORBA.ORB;

//import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
//import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga;

/**
 * @author André Gonçalves
 * @since 04/08/2004
 */
public class BonusRecargasGPP {
	
	/**
	 * Consulta o GPP por bônus por recargas existentes
	 * 
	 * @param servidor servidor do GPP
	 * @param porta porta do GPP
	 * @return array de informações sobre o bônus por recargas
	 * @throws Exception
	 */
	public static retornoConsultaBonusPorRecarga consultaBonusRecargasExistentes(String servidor, String porta) throws Exception 
	{
		/*	
		retornoConsultaBonusPorRecarga dados;
		ORB orb = GerenteORB.getORB(servidor, porta);
		
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			dados = pPOA.consultaMapeamentoBonusRecarga();
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		
		return dados;
		*/
		return null;
	}
	
	
	
	
	
	public static short adicionaBonusRecarga(String servidor, String porta, dadosBonusPorRecarga dados) throws Exception 
	{
	/*
	short resposta=0;
	
	ORB orb = GerenteORB.getORB(servidor, porta);
	
	byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
	
	aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

	try
	{
		resposta = pPOA.insereMapeamentoBonusRecarga(dados);
		
	}
	catch (Exception e) 
	{
		throw new Exception("Erro ao conectar no GPP");
	}
	
	return resposta;
	*/
		return 0;
	}
	
	public static short retiraBonusRecarga(String servidor, String porta, short numeroRecargas) throws Exception 
	{
	/*	
	short resposta=0;
	
	ORB orb = GerenteORB.getORB(servidor, porta);
	
	byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
	
	aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

	try
	{
		resposta = pPOA.removeMapeamentoBonusRecarga(numeroRecargas);
		
	}
	catch (Exception e) 
	{
		throw new Exception("Erro ao conectar no GPP");
	}
	
	return resposta;
	*/
		return 0;
	}
	
	public static short editaBonusRecarga(String servidor, String porta, dadosBonusPorRecarga dados) throws Exception 
	{
	/*
	short resposta;
	
	ORB orb = GerenteORB.getORB(servidor, porta);
	
	byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
	
	aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

	try
	{
		resposta = pPOA.atualizaMapeamentoBonusRecarga(dados);
		
	}
	catch (Exception e) 
	{
		throw new Exception("Erro ao conectar no GPP");
	}
	
	return resposta;
	*/
		return 0;
	}

}
