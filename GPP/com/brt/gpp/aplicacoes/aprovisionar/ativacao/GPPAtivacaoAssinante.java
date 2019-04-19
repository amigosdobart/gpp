package com.brt.gpp.aplicacoes.aprovisionar.ativacao;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;

/**
 * Esta classe realiza todas as acoes de ativacao de assinante
 * @author Magno Batista Corrêa
 * @since 20080215
 */

public class GPPAtivacaoAssinante {
	private Assinante assinante;
	private String operador;

	public GPPAtivacaoAssinante() 
	{
	}
	public GPPAtivacaoAssinante(Assinante assinante, String operador)
	{
		this.setAssinante(assinante);
		this.setOperador(operador);
	}

	/**                                         
	 * Realiza o parse do xml para a geracao do objeto
     * <GPPAtivacaoAssinante>                    
     *     <assinante>                         
     *         <msisdn>556184111111</msisdn>   
     *         <imsi>12345678790</imsi>        
     *         <planoPreco>1</planoPreco>      
     *         <idioma>4</idioma>              
     *         <status>1</status>              
     *         <creditoInicial>                
     *             <saldo id="0">50.00</saldo> 
     *             <saldo id="1">10.00</saldo> 
     *             <saldo id="2">0.00</saldo>  
     *             <saldo id="3">0.00</saldo>  
     *         </creditoInicial>               
     *     </assinante>                        
     *     <operador>TR00000</operador>        
     * </GPPAtivacaoAssinante>                   
     * @return                                 
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
    */
	public static GPPAtivacaoAssinante parseXML(String xml) throws ParserConfigurationException, SAXException, IOException{
			if (xml == null) 
				return null;
		// Cria um parser de XML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		
		// Executa o parser 
		Document doc = parse.parse(is);
	
		// Interpreta os elementos
		Element root = (Element) doc.getElementsByTagName( "GPPAtivacaoAssinante" ).item(0);
		return parseXML(root);
	}
	
	/**
	 * Realiza o parse do xml para a geracao do objeto
	 * @param root
	 * @return
	 */
	public static GPPAtivacaoAssinante parseXML(Element root)
	{
		GPPAtivacaoAssinante retorno = new GPPAtivacaoAssinante();
		Element elementoAssinante = (Element) root.getElementsByTagName("assinante").item(0);
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(getNodeValue(elementoAssinante, "msisdn"));
		assinante.setIMSI(getNodeValue(elementoAssinante, "imsi"));
		assinante.setPlanoPreco(Short.parseShort(getNodeValue(elementoAssinante, "planoPreco")));
		assinante.setIdioma(Short.parseShort(getNodeValue(elementoAssinante, "idioma")));
		assinante.setStatusAssinante(Short.parseShort(getNodeValue(elementoAssinante, "status",false,String.valueOf(Definicoes.FIRST_TIME_USER))));
		
		retorno.setAssinante(assinante);
		retorno.setOperador(getNodeValue(root, "operador"));
		
		Element creditoInicial = (Element) root.getElementsByTagName("creditoInicial").item(0);
		NodeList saldos =creditoInicial.getElementsByTagName("saldo");
		MapTipoSaldo map = MapTipoSaldo.getInstance();
		for (int i = 0; i < saldos.getLength(); i++)
		{
			Element saldo = (Element)saldos.item(i);
			short id = Short.parseShort(getAtributeValue(saldo,"id"));
			double valor = Double.parseDouble(getNodeValue(saldo,false,"0"));
			SaldoAssinante saldoAssinante = new SaldoAssinante(assinante.getMSISDN(),
					map.getTipoSaldo(id), valor,(Date)null);
			assinante.setSaldo(saldoAssinante);
		}
		return retorno;
	}

	/**
	 * Extrai um atributo de um dado elemento
	 * @param from
	 * @param attributeName
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static String getAtributeValue(Element from, String attributeName)throws IllegalArgumentException
	{
		return getAtributeValue(from,attributeName, true,null); 
	}
	
	/**
	 * Extrai um atributo de um dado elemento
	 * @param from
	 * @param attributeName
	 * @param obrigatorio
	 * @param padrao
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static String getAtributeValue(Element from, String attributeName,boolean obrigatorio, String padrao)throws IllegalArgumentException
	{
		String retorno = from.getAttribute(attributeName);
		if(retorno != null && !retorno.equals(""))
			return retorno;
		else
			if(obrigatorio)
				throw new IllegalArgumentException("Atributo " + attributeName +" nao encontrado!");
			else
				return padrao;
	}

	/**
	 * Extrai um valor de um no  
	 * @param from
	 * @param obrigatorio
	 * @param padrao
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static String getNodeValue(Element from,boolean obrigatorio, String padrao)throws IllegalArgumentException
	{
		String retorno = null;
		if (from.hasChildNodes())
			 retorno = from.getFirstChild().getNodeValue().trim();  
		if(retorno != null && !retorno.equals(""))	
			return retorno;
		if(obrigatorio)
			throw new IllegalArgumentException("Valor nao encontrado!");
		return padrao;
	}

	/**
	 * Extrai um valor de um no  
	 * @param from
	 * @param nodeName
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static String getNodeValue(Element from, String nodeName)throws IllegalArgumentException
	{
		return getNodeValue(from,nodeName,true,null);
	}
	/**
	 * Extrai um valor de um no  
	 * @param from
	 * @param nodeName
	 * @param obrigatorio
	 * @param padrao
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static String getNodeValue(Element from, String nodeName,boolean obrigatorio, String padrao)throws IllegalArgumentException
	{
		Node nodo = from.getElementsByTagName(nodeName).item(0);
		if (nodo != null && nodo.hasChildNodes())
			return nodo.getFirstChild().getNodeValue();
		if(obrigatorio)
			throw new IllegalArgumentException("Tag " + nodeName + " nao encontrada!");
		return padrao;
	}

	public Assinante getAssinante()
	{
		return assinante;
	}

	public void setAssinante(Assinante assinante)
	{
		this.assinante = assinante;
	}

	public String getOperador() 
	{
		return operador;
	}

	public void setOperador(String operador) 
	{
		this.operador = operador;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "assinante:"+this.getAssinante().toString()+";operador:"+this.operador;
	}
}
