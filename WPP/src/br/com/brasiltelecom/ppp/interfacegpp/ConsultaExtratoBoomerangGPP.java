/*
 * Created on 08/04/2004
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.StringReader;
//import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.brasiltelecom.ppp.model.ExtratoBoomerang;
import br.com.brasiltelecom.ppp.model.RetornoExtratoBoomerang;


import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 * Efetua a conexão com o GPP a fim de obter extrato do Boomerang
 * 
 * @author Henrique Canto
 * @since 22/11/2004
 */
public class ConsultaExtratoBoomerangGPP {
	

	/**
	 * Obtém o XML de extrato vindo do GPP
	 * 
	 * @param msisdn msisdn
	 * @param dataInicial data de início
	 * @param dataFinal data final
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return código XML
	 * @throws Exception
	 */
	public static String getXml(String msisdn,String dataInicial, String dataFinal,String servidor, String porta)
		throws Exception{
				String ret = "";
			
		

				ORB orb = GerenteORB.getORB(servidor, porta);
		
				byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		
				consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
				try
				{
					ret = pPOA.consultaExtratoBoomerang(msisdn,dataInicial,dataFinal);
				}
				catch (Exception e) 
				{
					throw new Exception("Erro ao conectar no GPP");
				}
				
				return ret;
			}
			
			/**
			 * Retorna um extrato
			 * 
			 * @param msisdn msisdn
			 * @param dataInicial data de início
			 * @param dataFinal data final
			 * @param servidor endereço do servidor
			 * @param porta porta do servidor
			 * @return extrato
			 * @throws Exception
			 */
			public static RetornoExtratoBoomerang getExtratos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta) throws Exception{
				
				RetornoExtratoBoomerang ret = new RetornoExtratoBoomerang();
			
			try
			{
				// Busca uma instancia de um DocumentBuilder
				DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();

				// Cria um parse de XML
				DocumentBuilder parse = docBuilder.newDocumentBuilder();

				// Carrega o XML informado dentro e um InputSource
				InputSource is = new InputSource(new StringReader(getXml(msisdn,dataInicial,dataFinal, servidor, porta)));

				// Faz o parse do XML
				Document doc = parse.parse(is);

				
				
				Vector v = new Vector();
			
								
				//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				//Obter o índice de recarga
				Element elmDadosControle = (Element)doc.getElementsByTagName("dadosControle").item(0);
				NodeList nlDadosControle = elmDadosControle.getChildNodes();
				if(nlDadosControle.getLength() > 0)
				{
					ret.setIndicieRecarga(nlDadosControle.item(0).getChildNodes().item(0).getNodeValue());
				}
				//Obter os dados do detalhe das chamadas
				for (int i=0; i < doc.getElementsByTagName( "detalhe" ).getLength(); i++){
					Element serviceElement = (Element) doc.getElementsByTagName( "detalhe" ).item(i);
					NodeList itemNodes = serviceElement.getChildNodes();

					if (itemNodes.getLength() > 0){	
						ExtratoBoomerang extrato = new ExtratoBoomerang();
						extrato.setNumeroDestino(itemNodes.item(0).getChildNodes().item(0).getNodeValue());
						extrato.setData(itemNodes.item(1).getChildNodes().item(0).getNodeValue());
						extrato.setHoraChamada(itemNodes.item(2).getChildNodes().item(0).getNodeValue());
						extrato.setOperacao(itemNodes.item(3).getChildNodes().item(0).getNodeValue());
						extrato.setRegiaoOrigem(itemNodes.item(4).getChildNodes().item(0).getNodeValue());
						extrato.setRegiaoDestino(itemNodes.item(5).getChildNodes().item(0)!=null?itemNodes.item(5).getChildNodes().item(0).getNodeValue():"");
						extrato.setDuracaoChamada(itemNodes.item(6).getChildNodes().item(0).getNodeValue());
					    extrato.setValorBoomerang(itemNodes.item(7).getChildNodes().item(0).getNodeValue());
						v.add(extrato);
					}
				}
				
				ret.setExtratos(v);
				
				Element serviceElement = (Element) doc.getElementsByTagName( "total" ).item(0);
				NodeList itemNodes = serviceElement.getChildNodes();
				if (itemNodes.getLength() > 0)
				{
					ret.setNumeroChamadas(itemNodes.item(0).getChildNodes().item(0).getNodeValue());
					ret.setNumeroMinutos(itemNodes.item(1).getChildNodes().item(0).getNodeValue());
					ret.setTotalBoomerang(itemNodes.item(2).getChildNodes().item(0).getNodeValue());					
				 }

			}
			catch (Exception e) 
			{
				throw new Exception(e.getMessage());
			}
			return ret;
			}
}
