package br.com.brasiltelecom.ppp.portal.entity;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RespostaWig implements Comparable,org.exolab.castor.jdo.TimeStampable
{
	private int codResposta;
	private String descricaoResposta;
	private String nomeResposta;
	private long jdoTimestamp;
	
	public int getCodResposta()
	{
		return codResposta;
	}
	
	public void setCodResposta(int codigoResposta)
	{
		this.codResposta = codigoResposta;
	}
	
	public String getDescricaoResposta()
	{
		return descricaoResposta;
	}
	
	public void setDescricaoResposta(String descricaoResposta)
	{
		this.descricaoResposta = descricaoResposta;
	}
	
	public String getNomeResposta()
	{
		return nomeResposta;
	}
	
	public void setNomeResposta(String nomeResposta)
	{
		this.nomeResposta = nomeResposta;
	}
	
	// Metodos necessarios para implementacao 
	public long jdoGetTimeStamp()
	{
		return jdoTimestamp;
	}
	
	public void jdoSetTimeStamp(long jdoTimestamp)
	{
		this.jdoTimestamp = jdoTimestamp;
	}
	
	public int hashCode()
	{
		return getCodResposta();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof RespostaWig) )
			return false;
		
		if ( ((RespostaWig)obj).getCodResposta() == this.getCodResposta() )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getNomeResposta();
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof RespostaWig) )
			return 0;
		
		if ( this.getCodResposta() > ((RespostaWig)obj).codResposta )
			return 1;
		
		if ( this.getCodResposta() < ((RespostaWig)obj).codResposta )
			return -1;
		
		return 0;
	}
	
	// Os metodos abaixo serao utilizados
	// para leitura da resposta quando esta
	// for uma tag <select> do WML contendo
	// a opcao do menu caso os filtros sejam
	// aplicaveis.
	// Nestes metodos serao identificados os
	// valores de descricao, e o direcionamento
	// caso o usuario escolha esta opcao (onvalue e onpick)
	public String getDescricaoOpcao()
	{
		String descricao = null;
		try
		{
			if (getDescricaoResposta().startsWith("<option"))
				descricao = getTextNode(getDescricaoResposta(),"option");
		}
		catch(Exception e){};
		return descricao;
	}
	
	public String getValorOnValue()
	{
		String descricao = null;
		try
		{
			if (getDescricaoResposta().startsWith("<option"))
				descricao = getAttribute(getDescricaoResposta(),"option","value");
		}
		catch(Exception e){};
		return descricao;
	}
	
	public String getValorOnPick()
	{
		String descricao = null;
		try
		{
			if (getDescricaoResposta().startsWith("<option"))
				descricao = getAttribute(getDescricaoResposta(),"option","onpick");
		}
		catch(Exception e){};
		return descricao;
	}
	
	public String getValorMensagem()
	{
		String descricao = null;
		try
		{
			if (getDescricaoResposta().startsWith("<p>"))
				descricao = getTextNode(getDescricaoResposta(),"p");
		}
		catch(Exception e){};
		return descricao;
	}
	
	private NodeList getItemNodes(String xml, String tagName) throws Exception
	{
		Document doc = getDocument(xml);
		Element serviceElement = (Element) doc.getElementsByTagName(tagName).item(0);
		return serviceElement.getChildNodes();
	}
	
	private String getTextNode(String xml, String tagName) throws Exception
	{
		NodeList itemNodes = getItemNodes(xml,tagName);
		for (int i=0; i < itemNodes.getLength(); i++)
		{
			Node no = itemNodes.item(i);
			if (no.getNodeType() == Node.TEXT_NODE)
				//return no.toString();
				return no.getNodeValue();
		}
		return null;
	}
	
	private String getAttribute(String xml, String tagName, String attrName) throws Exception
	{
		NodeList itemNodes = getItemNodes(xml,tagName);
		return ((Node)itemNodes).getAttributes().getNamedItem(attrName).getNodeValue();
	}
	
	private static Document getDocument(String wmlOrigem) throws Exception
	{
		// Realiza o parse do WML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		docBuilder.setValidating(false);
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(wmlOrigem.trim()));
		
		return parse.parse(is);
	}
}
