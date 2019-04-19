/*
 * Criado em  16/06/2005
 */
package br.com.brasiltelecom.ppp.interfacegpp;


// Corba
import org.omg.CORBA.ORB;

// Importa��es do GPP
import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.aplicacoes.consultar.consultaAparelho.*;

/**
 * Efetua a conex�o com o GPP a fim de obter as caracter�sticas
 * do assinante 
 * @author 	Marcelo Alves Araujo
 * @since 	16/06/2005
 */
public class ConsultaCaracteristicaAssinanteGPP
{	
	/**
	 * Obt�m o XML das caracter�sticas do assinante vindo do GPP
	 * 
	 * @param msisdn 	- MSISDN do assinante
	 * @param servidor 	- Endere�o do servidor
	 * @param porta 	- Porta do servidor
	 * @return c�digo XML
	 * @throws Exception
	 */
	public static String getXml(String msisdn,String servidor, String porta) throws Exception
	{			
		String respostaXML = "";
		

		ORB orb = GerenteORB.getORB(servidor, porta);
	
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
	
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			respostaXML = pPOA.consultaAparelho(msisdn);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
		return respostaXML;
	}
	
	/**
	 * Retorna dados de um assinante
	 *
	 * @param msisdn msisdn
	 * @param servidor endere�o do servidor
	 * @param porta porta do servidor
	 * @return dados do aparelho do assinante
	 * @throws Exception
	 */
	public static AparelhoAssinante getAparelho(String msisdn,String servidor, String porta) throws Exception
	{
	    AparelhoAssinante aparelho = new AparelhoAssinante();
		
		try
		{
		    // Armazena os dados do assinante no objeto aparelho
		    aparelho = ConsultaAparelho.parseXMLConsultaAparelho(ConsultaCaracteristicaAssinanteGPP.getXml(msisdn,servidor,porta));
		}
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}
		return aparelho;
	}
	
	/**
	 * Retorna o status da consulta
	 *
	 * @param msisdn msisdn
	 * @param servidor endere�o do servidor
	 * @param porta porta do servidor
	 * @return dados do aparelho do assinante
	 * @throws Exception
	 */
	public static String getRetorno(String msisdn,String servidor, String porta) throws Exception
	{
	    String retorno = null;
		
		try
		{
		    // Pega os dados da consulta
		    retorno = ConsultaAparelho.parseXMLRetorno(ConsultaCaracteristicaAssinanteGPP.getXml(msisdn,servidor,porta));
		}
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}
		return retorno;
	}
}