/*
 * Created on 08/04/2004
 *
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.brasiltelecom.ppp.model.Evento;
import br.com.brasiltelecom.ppp.model.Extrato;
import br.com.brasiltelecom.ppp.model.RetornoExtrato;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 * Efetua a conexão com o GPP a fim de obter extrato
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class ConsultaExtratoGPP 
{
	
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
	public static String getXml(String msisdn, String dataInicial, String dataFinal, String servidor, String porta)
		throws Exception
	{
		String ret = "";
		
		ORB orb = GerenteORB.getORB(servidor, porta);
		
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			ret = pPOA.consultaExtrato (msisdn,dataInicial,dataFinal);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar no GPP");
		}
			
		return ret;
	}
			
	/**
	 *	Retorna um extrato.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial do extrtato.
	 *	@param		String					dataFinal					Data final do extrato.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@return		RetornoExtrato										Extrato do assinante.
	 *	@throws Exception
	 */
	public static RetornoExtrato getExtratos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta) throws Exception
	{
	    String xmlExtrato = ConsultaExtratoGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta);
	    return ConsultaExtratoGPP.parse(xmlExtrato);
	}
			
	/**
	 *	Retorna um extrato, escrevendo-o em arquivo para paginacao.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial do extrtato.
	 *	@param		String					dataFinal					Data final do extrato.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao.
	 *	@return		RetornoExtrato										Extrato do assinante.
	 *	@throws Exception
	 */
	public static RetornoExtrato getExtratos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, ArrayList diretorios, String sessionId) throws Exception
	{
	    String xmlExtrato = ConsultaExtratoGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta);
	    ConsultaExtratoGPP.saveToFile(xmlExtrato, diretorios, sessionId);
	    return ConsultaExtratoGPP.parse(xmlExtrato);
	}
	
	/**
	 *	Retorna um extrato a partir de arquivo ja criado. Caso o arquivo nao exista, obtem o extrato a partir de 
	 *	interface com o GPP.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial do extrtato.
	 *	@param		String					dataFinal					Data final do extrato.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao.
	 *	@return		RetornoExtrato										Extrato do assinante.
	 *	@throws Exception
	 */
	public static RetornoExtrato getExtratosFromFile(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, ArrayList diretorios, String sessionId) throws Exception
	{
	    String xmlExtrato = ConsultaExtratoGPP.readFromFile(diretorios, sessionId);
	    if(xmlExtrato == null)
	    {
	        return ConsultaExtratoGPP.getExtratos(msisdn, dataInicial, dataFinal, servidor, porta, diretorios, sessionId);
	    }
	    
	    return ConsultaExtratoGPP.parse(xmlExtrato);
	}
			
	/**
	 *	Interpreta o XML do extrato.
	 * 
	 *	@param		String					xmlExtrato					XML contendo o extrato do assinante.
	 *	@return		RetornoExtrato			ret							Objeto contendo os informacoes do extrato.
	 *	@throws		Exception
	 */
	private static RetornoExtrato parse(String xmlExtrato) throws Exception
	{
		RetornoExtrato result = new RetornoExtrato();
		
		try
		{					
			//Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlExtrato));
			Document doc = parse.parse(is);

			Vector v = new Vector();
		
			Extrato extrato = null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			//Obter o índice de recarga
			Element elmDadosControle = (Element)doc.getElementsByTagName("dadosControle").item(0);
			NodeList nlDadosControle = elmDadosControle.getChildNodes();
			if(nlDadosControle.getLength() > 0)
			{
				result.setIndAssinanteLigMix(nlDadosControle.item(0).getChildNodes().item(0).getNodeValue());
				result.setPlanoPreco		(nlDadosControle.item(1).getChildNodes().item(0).getNodeValue());
			}
		
			for (int i=0; i < doc.getElementsByTagName( "detalhe" ).getLength(); i++)
			{
				Element serviceElement = (Element) doc.getElementsByTagName( "detalhe" ).item(i);
				NodeList itemNodes = serviceElement.getChildNodes();
				if (itemNodes.getLength() > 0)
				{	
					extrato = new Extrato();
					extrato.setNumeroOrigem(itemNodes.item(1).getChildNodes().item(0).getNodeValue());
					extrato.setData(itemNodes.item(2).getChildNodes().item(0).getNodeValue());
					extrato.setHoraChamada(itemNodes.item(3).getChildNodes().item(0).getNodeValue());
					extrato.setTipoTarifacao(itemNodes.item(4).getChildNodes().item(0).getNodeValue());
					extrato.setOperacao(itemNodes.item(5).getChildNodes().item(0).getNodeValue());
					extrato.setRegiaoOrigem(itemNodes.item(6).getChildNodes().item(0).getNodeValue());
					extrato.setRegiaoDestino(itemNodes.item(7).getChildNodes().item(0)!=null?itemNodes.item(7).getChildNodes().item(0).getNodeValue():"");
					extrato.setNumeroDestino(itemNodes.item(8).getChildNodes().item(0).getNodeValue());
					extrato.setDuracaoChamada(itemNodes.item(9).getChildNodes().item(0).getNodeValue());
					
					extrato.setValorPrincipal	(stringToDouble(itemNodes.item(10).getChildNodes().item(0).getChildNodes().item(0).getNodeValue()));
					extrato.setValorBonus		(stringToDouble(itemNodes.item(10).getChildNodes().item(1).getChildNodes().item(0).getNodeValue()));
					extrato.setValorSMS			(stringToDouble(itemNodes.item(10).getChildNodes().item(2).getChildNodes().item(0).getNodeValue()));
					extrato.setValorGPRS		(stringToDouble(itemNodes.item(10).getChildNodes().item(3).getChildNodes().item(0).getNodeValue()));
					extrato.setValorPeriodico	(stringToDouble(itemNodes.item(10).getChildNodes().item(4).getChildNodes().item(0).getNodeValue()));
					extrato.setValorTotal		(stringToDouble(itemNodes.item(10).getChildNodes().item(5).getChildNodes().item(0).getNodeValue()));

					extrato.setSaldoPrincipal	(stringToDouble(itemNodes.item(11).getChildNodes().item(0).getChildNodes().item(0).getNodeValue()));
					extrato.setSaldoBonus		(stringToDouble(itemNodes.item(11).getChildNodes().item(1).getChildNodes().item(0).getNodeValue()));
					extrato.setSaldoSMS			(stringToDouble(itemNodes.item(11).getChildNodes().item(2).getChildNodes().item(0).getNodeValue()));
					extrato.setSaldoGPRS		(stringToDouble(itemNodes.item(11).getChildNodes().item(3).getChildNodes().item(0).getNodeValue()));
					extrato.setSaldoPeriodico	(stringToDouble(itemNodes.item(11).getChildNodes().item(4).getChildNodes().item(0).getNodeValue()));
					extrato.setSaldoTotal		(stringToDouble(itemNodes.item(11).getChildNodes().item(5).getChildNodes().item(0).getNodeValue()));
					
					v.add(extrato);
				}
			}
			
			result.setExtratos(v);
			
			v= new Vector();
			
			for (int i=0; i < doc.getElementsByTagName( "evento" ).getLength(); i++)
			{
				Element serviceElement = (Element) doc.getElementsByTagName( "evento" ).item(i);
				NodeList itemNodes = serviceElement.getChildNodes();

				if (itemNodes.getLength() > 0)
				{
					Evento evento = new Evento();
					evento.setNome(itemNodes.item(1).getChildNodes().item(0).getNodeValue());
					evento.setData(itemNodes.item(2).getChildNodes().item(0).getNodeValue());
					evento.setHora(itemNodes.item(3).getChildNodes().item(0).getNodeValue());
					v.add(evento);
				}
			}
			
			result.setEventos(v);
			
			Element serviceElement = (Element) doc.getElementsByTagName( "totais" ).item(0);
			NodeList itemNodes = serviceElement.getChildNodes();
			if (itemNodes.getLength() > 0)
			{	
				//                                  SaldoInicial                   Principal
				result.setSaldoInicialPrincipal	(itemNodes.item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
				result.setSaldoInicialBonus		(itemNodes.item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
				result.setSaldoInicialSMS		(itemNodes.item(0).getChildNodes().item(2).getChildNodes().item(0).getNodeValue());
				result.setSaldoInicialGPRS		(itemNodes.item(0).getChildNodes().item(3).getChildNodes().item(0).getNodeValue());
				result.setSaldoInicialPeriodico	(itemNodes.item(0).getChildNodes().item(4).getChildNodes().item(0).getNodeValue());
				result.setSaldoInicialTotal		(itemNodes.item(0).getChildNodes().item(5).getChildNodes().item(0).getNodeValue());

				result.setTotalDebitosPrincipal	(itemNodes.item(1).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
				result.setTotalDebitosBonus		(itemNodes.item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
				result.setTotalDebitosSMS		(itemNodes.item(1).getChildNodes().item(2).getChildNodes().item(0).getNodeValue());
				result.setTotalDebitosGPRS		(itemNodes.item(1).getChildNodes().item(3).getChildNodes().item(0).getNodeValue());
				result.setTotalDebitosPeriodico (itemNodes.item(1).getChildNodes().item(4).getChildNodes().item(0).getNodeValue());
				result.setTotalDebitosTotal		(itemNodes.item(1).getChildNodes().item(5).getChildNodes().item(0).getNodeValue());

				result.setTotalCreditosPrincipal(itemNodes.item(2).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
				result.setTotalCreditosBonus	(itemNodes.item(2).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
				result.setTotalCreditosSMS		(itemNodes.item(2).getChildNodes().item(2).getChildNodes().item(0).getNodeValue());
				result.setTotalCreditosGPRS		(itemNodes.item(2).getChildNodes().item(3).getChildNodes().item(0).getNodeValue());
				result.setTotalCreditosPeriodico(itemNodes.item(2).getChildNodes().item(4).getChildNodes().item(0).getNodeValue());
				result.setTotalCreditosTotal	(itemNodes.item(2).getChildNodes().item(5).getChildNodes().item(0).getNodeValue());

				result.setSaldoFinalPrincipal	(itemNodes.item(3).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
				result.setSaldoFinalBonus		(itemNodes.item(3).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
				result.setSaldoFinalSMS			(itemNodes.item(3).getChildNodes().item(2).getChildNodes().item(0).getNodeValue());
				result.setSaldoFinalGPRS		(itemNodes.item(3).getChildNodes().item(3).getChildNodes().item(0).getNodeValue());
				result.setSaldoFinalPeriodico	(itemNodes.item(3).getChildNodes().item(4).getChildNodes().item(0).getNodeValue());
				result.setSaldoFinalTotal		(itemNodes.item(3).getChildNodes().item(5).getChildNodes().item(0).getNodeValue());

				serviceElement = (Element) doc.getElementsByTagName( "dadosCadastrais" ).item(0);
				itemNodes = serviceElement.getChildNodes();

				if (itemNodes.item(2).getChildNodes().item(0) != null)
				{
					result.setDataAtivacao(itemNodes.item(2).getChildNodes().item(0).getNodeValue());
				}
				
				if (itemNodes.item(3).getChildNodes().item(0) != null)
				{
					result.setPlano(itemNodes.item(3).getChildNodes().item(0).getNodeValue());
				}
			}
		}
		catch(NullPointerException e)
		{
			throw new Exception("Problemas com a geração do Comprovante de Serviços.");
		}
		
		return result;
	}
			
	// Parte para formatação dos dados para emissão do relatorio em PDF pelo REPORTS
	// Alberto Magno - Accenture - 11/05/04
	private static PhoneNumberFormat ph = new PhoneNumberFormat();		

	private static void percorre(Element serviceElement, StringBuffer ret) throws Exception
	{

		NodeList itemNodes = serviceElement.getChildNodes();			
		ret.append("<"+serviceElement.getNodeName()+">");
		for (int i=0; i < itemNodes.getLength(); i++)
		{
			itemNodes = serviceElement.getChildNodes();			
			if (itemNodes.getLength()>1)
				percorre((Element)itemNodes.item(i),ret);
			else
			{
				  String texto = itemNodes.item(0).getNodeValue();
				  // Telefones para formatar
				  if ((serviceElement.getNodeName().equals("msisdn")||serviceElement.getNodeName().equals("numeroOrigem"))&&(texto.length()==10))
					texto = texto.substring(0,2)+"-"+texto.substring(2,6)+"-"+texto.substring(6);
				  if (serviceElement.getNodeName().equals("numeroDestino"))
				    texto = ph.format(texto);							
				  // Formatar milhagem e decimal
				  if (serviceElement.getNodeName().equals("valorPrincipal")||serviceElement.getNodeName().equals("saldoPrincipal")
				  		||serviceElement.getNodeName().equals("valorBonus")||serviceElement.getNodeName().equals("saldoBonus")
				  		||serviceElement.getNodeName().equals("valorGPRS")||serviceElement.getNodeName().equals("saldoGPRS")
				  		||serviceElement.getNodeName().equals("valorSMS")||serviceElement.getNodeName().equals("saldoSMS")
				  		||serviceElement.getNodeName().equals("valorTotal")||serviceElement.getNodeName().equals("saldoTotal")
				  		||serviceElement.getNodeName().equals("saldoInicialPeriodo")
						||serviceElement.getNodeName().equals("saldoFinalPeriodo")
						||serviceElement.getNodeName().equals("totalDebitos")
						||serviceElement.getNodeName().equals("totalCreditos"))
					{
						//Virgula e Ponto
						texto = texto.substring(0,texto.indexOf(".")).replace(',','.')+","+texto.substring(texto.indexOf(".")+1);
						//Milhagem
						for(int point=texto.indexOf(',')-3;point>0&&texto.charAt(point)!='-';point=point-3)
							texto = texto.substring(0,point)+'.'+texto.substring(point,texto.length());   
						//Numeros significativos
						if (texto.length()-texto.indexOf(",")==2) texto = texto + "0";
						//Sinalização do valor
						if (serviceElement.getNodeName().indexOf("valor")>-1&&(texto.charAt(0)!='-')) texto = "+"+texto;
						// Currency Brazil
						//if (!serviceElement.getNodeName().equals("valor")&&!serviceElement.getNodeName().equals("saldo"))
							//texto = "R$ "+texto;
					}
				// Descrição para Caixa Baixa
				/*if (serviceElement.getNodeName().equals("descricao"))
				  texto = texto.substring(0,1)+texto.substring(1,texto.length()).toLowerCase();*/
				
				  ret.append(texto);//texto.replace('_','-'));
			}
				 
		}
		ret.append("</"+serviceElement.getNodeName()+">");
	}
	
	/**
	 * Gera XML para emissão do relatorio em PDF pelo REPORTS
	 * 
	 * @param msisdn msisdn
	 * @param dataInicial data de início
	 * @param dataFinal data final
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return XML do relatório
	 * @throws Exception
	 */
	public static String getXmlRelatorio(String msisdn, String dataInicial, String dataFinal, String servidor, String porta) throws Exception
	{
		StringBuffer ret = new StringBuffer();
	try
	{
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(getXml(msisdn,dataInicial,dataFinal, servidor, porta)));
		Document doc = parse.parse(is);

		Element serviceElement = null;
		NodeList itemNodes = null;
		serviceElement = (Element) doc.getFirstChild();
		percorre(serviceElement,ret);
	}
	catch (Exception e) 
	{
		throw new Exception(e.getMessage());
	}

		return ret.toString();
	}
	
	/**
	 *	Le o XML do extrato retornado pelo GPP no arquivo criado na sessao.
	 * 
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao do usuario.
	 *	@return		String					result						XML com as informacoes do extrato do assinante.
	 */
	private static String readFromFile(ArrayList diretorios, String sessionId)
	{
	    StringBuffer result = new StringBuffer();
	    
	    try
	    {
	        //Verificando na lista de diretorios se o arquivo pode ser encontrado. 
	        for(int i = 0; i < diretorios.size(); i++)
	        {
		        String fileName = (String)diretorios.get(i) + File.separator + sessionId;
		        File fileExtrato = new File(fileName);
		        
		        if(fileExtrato.exists())
		        {
		            //Lendo o XML do arquivo.
		            FileReader reader = new FileReader(fileExtrato);
		            char[] buffer = new char[new Long(fileExtrato.length()).intValue()];
		            int bytesRead = 0;
	        
		            while((bytesRead = reader.read(buffer)) != -1)
		            {
		                result.append(buffer);
		            }
	            
		            reader.close();
		            break;
		        }
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	    
	    return (result.length() > 0) ? result.toString() : null;
	}

	/**
	 *	Grava o XML do extrato retornado pelo GPP em arquivo.
	 * 
	 *	@param		String					xmlExtrato					XML com as informacoes do extrato do assinante.
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao do usuario.
	 */
	private static void saveToFile(String xmlExtrato, ArrayList diretorios, String sessionId)
	{
	    try
	    {
	        //Percorrendo a lista de diretorios e verificando se existe algum valido.
	        for(int i = 0; i < diretorios.size(); i++)
	        {
	            File diretorio = new File((String)diretorios.get(i));
	            if((diretorio.exists()) && (diretorio.isDirectory()))
	            {
	    	        //Salvando o xml em arquivo
	    	        String fileName = diretorios.get(i) + File.separator + sessionId;
	    	        File fileExtrato = new File(fileName);
	    	        FileWriter writer = new FileWriter(fileExtrato);
	    	        writer.write(xmlExtrato);
	    	        writer.close();
	    	        break;
	            }
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	}

	/**
	 * Retorna um numero double a partir de uma string. Espera que o parametros de regionalizacao estejam em
	 * formato ingles.
	 * 
	 * @param numero String representando o numero
	 * @return O valor em formato double
	 */
	
	private static double stringToDouble(String valor) throws ParseException
	{
		DecimalFormat format = new DecimalFormat("#,###,##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
		
		double result = 0.0d;
		try
		{
			if(!valor.equalsIgnoreCase(""))
			{
				result = format.parse(valor).doubleValue();
			}
		}
		catch(ParseException e)
		{
			result = 0;
		}

		return result;
	}
			
}
