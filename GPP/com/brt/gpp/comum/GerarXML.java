//Definicao do Pacote
package com.brt.gpp.comum;

//Arquivos de Import de Java
import java.util.Stack;

/**
  *
  * Este arquivo contem a definicao da classe de GerarXML 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Denys Leite Oliveira
  * Data:               01/04/2002
  *
  * Modificado por:  Daniel Abib
  * Data:			 06/05/2004
  * Razao:			 Modificacao para melhoria de performance - String para StringBuffer
  *
  * Modificado por:  
  * Data:
  * Razao:
  *
  */
public class GerarXML 
{
	public static String PROLOG_XML_VERSAO_1_0_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private StringBuffer XML = new StringBuffer();
	private Stack pilhaTags = new Stack();		// pilha de Tags
		
	/**
	 * Metodo...: GerarXML
	 * Descricao: Construtor 
	 * @param root - Header sem barras ('/')
	 * @return
	 */
	public GerarXML(String root)
	{
		this.XML.append ("<").append(root).append(">");
		pilhaTags.push(root);
	}
	
	/**
	 * Metodo...: getXML
	 * Descricao: Retorna o String XML montado 
	 * @param 
	 * @return String 	- XML montado
	 */
	public String getXML()
	{
		this.fechaNo();		// Inclui o fechamento do root no XML
		return this.XML.toString();
	}
	
	/**
	 * Metodo...: abreNo
	 * Descricao: Abre uma sessao no XML 
	 * @param tag 	- Tag do XML a ser montado
	 * @return 
	 */
	public void abreNo(String tag)
	{
		this.XML.append("<").append(tag).append(">");
		pilhaTags.push(tag);
	}
	
	/**
	 * Metodo...: fechaNo
	 * Descricao: Fecha ultima sessao aberta no XML
	 * @param 
	 * @return 
	 */
	public void fechaNo()
	{
		this.XML.append("</").append(pilhaTags.pop()).append(">");
	}
	
	/**
	 * Metodo...: adicionaTag
	 * Descricao: Adiciona TAG no XML 
	 * @param tag 	- Tag do XML a ser montado
	 * @param valor	- Valor da tag do XML a ser montado
	 * @return 
	 */
	public void adicionaTag(String tag, String valor)
	{
		if(valor!=null)
		{
			this.XML.append("<").append(tag).append(">").append(valor).append("</").append(tag).append(">");
		}
		else
		{
			this.XML.append("<").append(tag).append("/>");
		}
	}
	
	/**
	 * Metodo...: abreTagCDATA
	 * Descricao: Abre o tag CDATA, tag de leitura do Vítria. (TAG: <![CDATA[)
	 * @param
	 * @return
	 */
	public void abreTagCDATA() {
		String tag = "<![CDATA[";
		this.XML.append(tag);
		pilhaTags.push(tag);
	}
	
	/**
	 * Metodo...: fechaTagCDATA
	 * Descricao: Fecha o tag CDATA, tag de leitura do Vítria. (TAG: ]]>)
	 * @param
	 * @return
	 */
	public void fechaTagCDATA() {
		this.XML.append("]]>");
		pilhaTags.pop();
	}
	
	/**
	 * Concatena uma string ao XML original em formação.
	 * Este método deve ser usado com muita cautela, sendo usado principalmente para concatenar subXML
	 * @param subXML String que vai ser concatenada ao XML. Preferencialmente um XML.
	 */
	public void concatenaString(String subXML)
	{
		this.XML.append(subXML);
	}
	

}
