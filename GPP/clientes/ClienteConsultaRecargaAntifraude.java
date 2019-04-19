package clientes;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

public class ClienteConsultaRecargaAntifraude 
{
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
					
		try
		{
		    String entrada = "<mensagem><cabecalho><empresa>BRG</empresa><sistema>CRM02</sistema><processo>CONSRCRGANTFRD</processo><data>2005-05-25 12:23:00</data><identificador_requisicao>123</identificador_requisicao></cabecalho><conteudo><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><dados><evento>RqConsultaRecargaPeriodo</evento>"+
		    				 "<msisdn>" + args[2] + "</msisdn>"  +
		    				 "<periodo>"+ args[3] + "</periodo>" +
		    				 "</dados></root>]]></conteudo></mensagem>";
		    String aXML = pPOA.consultaRecargaAntifraude(entrada);
			System.out.println("Resposta:\n" + aXML);
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}									
	}
}
