package com.brt.gpp.aplicacoes.campanha.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;

public class ParametroCampanhaXMLParser
{
	/**
	 * Metodo....:getXML
	 * Descricao.:Retorna o XML da campanha utilizando os parametros a serem cadastrados
	 * @param campanha		- Campanha a ser incluida
	 * @param propriedades	- Parametros da campanha a serem incluidos
	 * @return String		- XML constuido a partir das informacoes
	 */
	public static StringBuffer getXML(Campanha campanha, Map propriedades)
	{
		StringBuffer xml = new StringBuffer("<?xml version=\"1.0\"?>");
		xml.append("<GPPCampanha>");
			xml.append("<idCampanha>" + campanha.getId() + "</idCampanha>");
			xml.append("<nomeCampanha>" + campanha.getNomeCampanha() + "</nomeCampanha>");
			xml.append("<parametros>");
			for (Iterator i = propriedades.entrySet().iterator(); i.hasNext();)
			{
				Entry entry = (Entry)i.next();
				xml.append("<parametro nome=\"" + entry.getKey() + "\">" + entry.getValue() + "</parametro>");
			}
			xml.append("</parametros>");
		xml.append("</GPPCampanha>");
		return xml;
	}
	
	/**
	 * Metodo....:parseXMLCampanha
	 * Descricao.:Realiza o parse do XML dos parametros da campanha para este assinante
	 * @param xml - XML a ser realizado o parse
	 * @return Map - Valores dos parametros existentes no XML
	 */
	public static Map parseXMLCampanha(String xml)
	{
		Map parametros = new HashMap();
		try
		{
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = parse.parse(is);
			
			NodeList gppcamp = ((Element) doc.getElementsByTagName("GPPCampanha").item(0)).getChildNodes();
			NodeList param   = ((Element)gppcamp).getElementsByTagName("parametros").item(0).getChildNodes();
			for (int i=0; i < param.getLength(); i++)
			{
				Node no = param.item(i);
				if (no.getNodeType() == Node.ELEMENT_NODE)
					parametros.put(no.getAttributes().getNamedItem("nome").getNodeValue()
							      ,no.getChildNodes().item(0).getNodeValue()
							      );
			}
		}
		catch(Exception e)
		{
			parametros.clear();
		}
		return parametros;
	}
}
