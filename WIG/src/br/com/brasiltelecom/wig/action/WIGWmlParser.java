package br.com.brasiltelecom.wig.action;

import java.io.StringReader;

import java.util.LinkedList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Esta classe sera utilizada para realizar o parse do WML buscando
 * os valores principalmente do elemento <card></card> que sera anexado
 * no conteudo
 *  
 * @author Joao Carlos
 * Data..: 08/06/2005
 *
 */
public class WIGWmlParser
{
	/**
	 * Metodo....:getDocument
	 * Descricao.:Realiza o parse do WML retornando um objeto Document
	 * @param wmlOrigem - WML de origem a ser realizado o parse
	 * @return Document	- Objeto Document contendo as informacoes do WML
	 * @throws Exception
	 */
	private static Document getDocument(String wmlOrigem) throws Exception
	{
		// Realiza o parse do WML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		docBuilder.setValidating(false);
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		parse.setEntityResolver(new EntityResolver() 
							{ 
								private  InputSource is = new InputSource(new StringReader("")); 
								public InputSource resolveEntity(String publicId, String systemId)
								{ 
									return is;
								} 
							} 
								);
		InputSource is = new InputSource(new StringReader(wmlOrigem.trim()));
		
		return parse.parse(is);
	}

	/**
	 * Metodo....:getCardsFromWml
	 * Descricao.:Retorna todos os elementos <CARD> existentes no WML
	 * @param wmlOrigem - WML a ser analisado
	 * @return String[] - Array de strings sendo que cada elemento eh um card
	 */
	public static String[] getCardsFromWml(String wmlOrigem)
	{
		Collection listaCards = new LinkedList();
		try
		{
			Document doc = getDocument(wmlOrigem);
			Element serviceElement = (Element) doc.getElementsByTagName("wml").item(0);
			NodeList itemNodes = serviceElement.getChildNodes();
			for (int i=0; i < itemNodes.getLength(); i++)
			{
				Node no = itemNodes.item(i);
				if (no.getNodeType() == Node.ELEMENT_NODE)
					listaCards.add(no.toString());
			}
		}
		catch(Exception e)
		{
			listaCards.clear();
			listaCards.add("<card id=\"erro\"><p>"+e.getMessage()+"</p></card>");
		}
		return (String[])listaCards.toArray(new String[0]);
	}

	/**
	 * Metodo....:getParagraphsFromCard
	 * Descricao.:Retorna as tags <p> definidas nesse card
	 * @param card - Card a ser analisado
	 * @return lista de valores contendo os paragrafos
	 */
	public static String[] getParagraphsFromCard(String card)
	{
		Collection lista = new LinkedList();
		try
		{
			Document doc = getDocument(card);
			Element serviceElement = (Element) doc.getElementsByTagName("card").item(0);
			NodeList itemNodes = serviceElement.getChildNodes();
			for (int i=0; i < itemNodes.getLength(); i++)
			{
				Node no = itemNodes.item(i);
				if (no.getNodeType() == Node.ELEMENT_NODE)
					lista.add(no.toString());
			}
		}
		catch(Exception e)
		{
			lista.clear();
			lista.add("<p>"+e.getMessage()+"</p>");
		}
		return (String[])lista.toArray(new String[0]);		
	}

	/**
	 * Metodo....:getElementoSelect
	 * Descricao.:Retorna os valores da tag SELECT existente em um determinado card
	 * @param card    - Card a ser realizado o parse
	 * @return String[] - Valores contidos na tag SELECT
	 */
	public static String[] getElementoSelect(String card, String option)
	{
		Collection listaTags = new LinkedList();
		try
		{
			Document doc = getDocument(card);
			Element serviceElement = (Element) doc.getElementsByTagName("p").item(0);
			NodeList itemNodes = serviceElement.getChildNodes();
			//itemNodes = ((Element)itemNodes).getElementsByTagName("p").item(0).getChildNodes();
			// Realiza uma iteracao em todos os elementos apos a tag <p>. Somente a tag <select>
			// sera modificada qualquer outra informacao que existir deve ser retornado normalmente
			for (int i=0; i < itemNodes.getLength(); i++)
			{
				Node no = itemNodes.item(i);
				if (no.getNodeType() == Node.ELEMENT_NODE)
					// Se o No for da tag select entao reconstroi a tag inserindo o valor Option
					// passado como parametro. Na reconstrucao da tag leva em consideracao os atributos
					// da tag
					if ("select".equals(no.getNodeName()))
					{
						String selecTag = "<select";
						NamedNodeMap map = no.getAttributes();
						for (int j=0; j < map.getLength(); j++)
							selecTag += " "+map.item(j).getNodeName()+"=\""+map.item(j).getNodeValue()+"\"";

						listaTags.add(selecTag+">");
						NodeList nodesSelect = no.getChildNodes();
						for (int j=0; j < nodesSelect.getLength(); j++)
						{
							Node noSelect = nodesSelect.item(j);
							if (noSelect.getNodeType() == Node.ELEMENT_NODE)
								listaTags.add(noSelect.toString());
						}
						// Adiciona a(s) tag(s) option a serem incluidos
						listaTags.add(option);
						listaTags.add("</select>");
					}
					else
						listaTags.add(no.toString());
			}
		}
		catch(Exception e)
		{
			listaTags.clear();
		}
		return (String[])listaTags.toArray(new String[0]);
	}

	/**
	 * Metodo....:getElementoWml
	 * Descricao.:Realiza o parse do wml retornando todos os valores contidos dentro da tag <WML></WML>
	 * @param wmlOrigem - WML de origem
	 * @return String	- Valores contidos dentro da tag
	 */
	public static String getElementoWml(String wmlOrigem)
	{
		StringBuffer buff = new StringBuffer();
		String cards[] = getCardsFromWml(wmlOrigem);
		for (int i=0; i < cards.length; i++)
			buff.append(cards[i]+"\n");
		
		return buff.toString();
	}

	/**
	 * Metodo....:getElementoCard
	 * Descricao.:Realiza o parse do wml retornando todos os valores contidos dentro da tag <CARD></CARD>
	 * @param cardOrigem - Card de origem
	 * @return String	- Valores contidos dentro da tag
	 */
	public static String getElementoCard(String cardOrigem)
	{
		StringBuffer elementoCard = new StringBuffer();
		try
		{
			String parags[] = WIGWmlParser.getParagraphsFromCard(cardOrigem);
			for (int i=0; i < parags.length; i++)
				elementoCard.append(parags[i]);
		}
		catch(Exception e)
		{
			return "<p>"+e.getMessage()+"</p>";
		}
		return elementoCard.toString();
	}
}
