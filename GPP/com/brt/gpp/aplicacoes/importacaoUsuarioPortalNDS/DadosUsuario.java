//Definicao do Pacote
package com.brt.gpp.aplicacoes.importacaoUsuarioPortalNDS;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
  * Este arquivo refere-se � classe que ser� instanciada para se pegar os dados
  * dos usu�rios no processo de importa��o de Usu�rios Portal NDS
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Vanessa Andrade
  * Data: 				18/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class DadosUsuario   
{
	// Atributos da Classe
	private long   id_evento;
	private int	   id_processamento;
	private String evento;
	private String modify_stmp;
	private String sistema;
	private String acao;
	private String idUsuario;
	private String nomeUsuario;
	private String deptoUsuario;
	private String cargoUsuario;
	private String emailUsuario;

	/**
	 * Metodo...: DadosUsuarios
	 * Descricao: Construtor Padr�o
	 * @param	
	 * @return									
	 */
	public DadosUsuario()
	{
	}
	
	// M�todos Get                             
	/**
	 * Metodo...: getid_evento
	 * Descricao: Busca o idEvento para a importa��o de Usu�rios Portal NDS
	 * @param	
	 * @return 	long	- Retorna oidEvento
	 */
	public long getid_evento() 
	{
		return this.id_evento;
	}

	/**
	 * Metodo...: getevento
	 * Descricao: Busca o evento para a importa��o de Usu�rios Portal NDS
	 * @param	
	 * @return String	- Retorna o evento
	 */
	public String getevento() 
	{
		return this.evento;
	}

	/**
	 * Metodo...: getmodify_stmp
	 * Descricao: Busca o modify_stmp para a importa��o de Usu�rios Portal NDS
	 * @param	
	 * @return String	- Retorna o modify_stmp
	 */
	public String getmodify_stmp() 
	{
		return this.modify_stmp;
	}

	/**
	 * Metodo...: getsistema
	 * Descricao: Busca o sistema para a importa��o de Usu�rios Portal NDS
	 * @param	
	 * @return String	- Retorna o sistema
	 */
	public String getsistema() 
	{
		return this.sistema;
	}

	/**
	 * Metodo...: getacao
	 * Descricao: Busca o sistema para a importa��o de Usu�rios Portal NDS
	 * @param	
	 * @return String	- Retorna a acao
	 */
	public String getacao() 
	{
		return this.acao;
	}

	/**
	 * Metodo...: getidUsuario
	 * Descricao: Busca o identificador do Usu�rio para a importa��o no Portal NDS
	 * @param	
	 * @return	String	- Retorna o identificador do Usu�rio
	 */
	public String getidUsuario() 
	{
		return idUsuario;
	}

	/**
	 * Metodo...: getnomeUsuario
	 * Descricao: Busca o nome do usu�rio para a importa��o no Portal NDS
	 * @param	
	 * @return	String	- Retorna o nome do usu�rio 
	 */
	public String getnomeUsuario() 
	{
		return nomeUsuario;
	}

	/**
	 * Metodo...: getdeptoUsuario
	 * Descricao: Busca o departamento do usu�rio para a importa��o no Portal NDS
	 * @param	
	 * @return	String	- Retorna o departamento do usu�rio 
	 */
	public String getdeptoUsuario() 
	{
		return deptoUsuario;
	}

	/**
	 * Metodo...: getcargoUsuario
	 * Descricao: Busca o cargo do usu�rio para a importa��o no Portal NDS
	 * @param	
	 * @return	String	- Retorna o cargo do usu�rio 
	 */
	public String getcargoUsuario() 
	{
		return cargoUsuario;
	}
	
	/**
	 * Metodo...: getemailUsuario
	 * Descricao: Busca o e-mail do usu�rio para a importa��o no Portal NDS
	 * @param	
	 * @return	String	- Retorna o e-mail do usu�rio 
	 */
	public String getemailUsuario() 
	{
		return emailUsuario;
	}
	/**
	 * Metodo...: getid_processamento
	 * Descricao: Busca o idProcessamento para a importa��o de Usu�rios Portal NDS
	 * @param	
	 * @return 	int	- Retorna o idProcessamento
	 */
	public int getid_processamento(){
		return id_processamento;
	}

	// M�todos Set
	/**
	 * Metodo...: setid_evento
	 * Descricao: Atribui o id_evento � importa��o de Usu�rios Portal NDS
	 * @param aid_evento		- Identificador do evento da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setid_evento(long aid_evento) 
	{
		this.id_evento = aid_evento;
	}

	/**
	 * Metodo...: setevento
	 * Descricao: Atribui o evento a importa��o de Usu�rios Portal NDS
	 * @param aevento	- Evento da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setevento(String aevento) 
	{
		this.evento = aevento;
	}

	/**
	 * Metodo...: setmodify_stmp
	 * Descricao: Atribui o modify_stmp a importa��o de Usu�rios Portal NDS
	 * @param amodify_stmp	- Modify_stmp da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setmodify_stmp(String amodify_stmp) 
	{
		this.modify_stmp = amodify_stmp;
	}

	/**
	 * Metodo...: setsistema
	 * Descricao: Atribui o sistema a importa��o de Usu�rios Portal NDS
	 * @param asistema		- Sistema da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setsistema(String asistema) 
	{
		this.sistema = asistema;
	}

	/**
	 * Metodo...: setacao
	 * Descricao: Atribui a a��o � importa��o de Usu�rios Portal NDS
	 * @param aacao 	- Acao da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setacao(String aacao) 
	{
		this.acao = aacao;
	}

	/**
	 * Metodo...: setidUsuario
	 * Descricao: Atribui o identificador do usuario a importa��o de Usu�rios Portal NDS
	 * @param 	aId		- Identificador do usuario da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setidUsuario(String aId) 
	{
		idUsuario = aId;
	}

	/**
	 * Metodo...: setnomeUsuario
	 * Descricao: Atribui o nome do usu�rio a importa��o de Usu�rios Portal NDS
	 * @param 	aNome 	- Nome do usuario da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setnomeUsuario(String aNome) 
	{
		nomeUsuario = aNome;
	}


	/**
	 * Metodo...: setdeptoUsuario
	 * Descricao: Atribui o departamento do usu�rio a importa��o de Usu�rios Portal NDS
	 * @param aDepto 	- Departamento do usu�rio da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setdeptoUsuario(String aDepto) 
	{
		deptoUsuario = aDepto;
	}
	
	/**
	 * Metodo...: setcargoUsuario
	 * Descricao: Atribui o cargo do usu�rio a importa��o de Usu�rios Portal NDS
	 * @param 	aCargo 	- Cargo do usuario da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setcargoUsuario(String aCargo) 
	{
		cargoUsuario = aCargo;
	}
	
	/**
	 * Metodo...: setemailUsuario
	 * Descricao: Atribui o e-mail do usu�rio a importa��o de Usu�rios Portal NDS
	 * @param aEmail	- E-mail do usu�rio  da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setemailUsuario(String aEmail) 
	{
		emailUsuario = aEmail;
	}
	/**
	 * Metodo...: setid_processamento
	 * Descricao: Atribui o id_processamento � importa��o de Usu�rios Portal NDS
	 * @param id_processamento		- Identificador do processamento da importa��o de Usu�rios Portal NDS
	 * @return 
	 */
	public void setid_processamento(int id_processamento){
		this.id_processamento=id_processamento;
	}

	/**
	 * Metodo...: getRetornoImportacaoUsuarioXML
	 * Descricao: Monta o XML de retorno
	 * @param 	desc_erro	- Descri��o do erro
	 * @param 	cod_erro	- C�digo do erro
	 * @param 	status_xml	- Status do xml
	 * @return	String		- Retorna o xml de retorno da importa��o de Usu�rios Portal NDS
	 */
	public String getRetornoImportacaoUsuarioXML(String desc_erro, int cod_erro, String status_xml) 
	{
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\"?>");
        buffer.append("<root><case>");
        buffer.append(   "<evento>" +      this.getevento() +      "</evento>");
        buffer.append(   "<id_evento>" +   this.getid_evento() +   "</id_evento>");
        buffer.append(   "<status>" +      status_xml +            "</status>");
        buffer.append(   "<desc_erro>" +   desc_erro +             "</desc_erro>");
        buffer.append(   "<cod_erro>" +    cod_erro +              "</cod_erro>");
        buffer.append(   "<modify_stmp>" + this.getmodify_stmp() + "</modify_stmp>");
        buffer.append(   "<sistema>" +     this.getsistema() +     "</sistema>");
        buffer.append("</case></root>");
    
		return buffer.toString();  
	}
    
    /**
     * Metodo...: parseImportacaoUsuarioXML
     * Descricao: Faz um parse do XML de entrada e retorna os valores internos do XML 
     * @param  GPPContestacao       - Dados do XML 
     * @return DadosContestacao     - Dados do XML dentro de uma classe do tipo DadosUsuario
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     * @throws GPPBadXMLFormatException
     * @throws GPPInternalErrorException
     */
    public static DadosUsuario parseImportacaoUsuarioXML (int idProcessamento, String XMLImportacaoUsuario) 
        throws ParserConfigurationException, SAXException, IOException 
    {
        DadosUsuario dadosUsuario = new DadosUsuario();
        dadosUsuario.setid_processamento(idProcessamento);
        
        // Busca uma instancia de um DocumentBuilder
        DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
        
        // Cria um parse de XML
        DocumentBuilder parse = docBuilder.newDocumentBuilder();
        
        // Carrega o XML informado dentro e um InputSource
        InputSource is = new InputSource(new StringReader(XMLImportacaoUsuario));
        
        // Faz o parse do XML
        Document doc = parse.parse(is);

        // Procura a TAG root
        Element serviceElement = (Element) doc.getElementsByTagName( "root" ).item(0);
        NodeList rootNode = serviceElement.getChildNodes();         

        // Procura a TAG CASE
        Element serviceCaseElement = (Element) doc.getElementsByTagName("CASE").item(0);
        NodeList caseNode = serviceCaseElement.getChildNodes();     

        
        if (rootNode != null)
        {
            // Faz um cast para uma estutura Element
            Element auxCaseNode = (Element)caseNode;

            // <root><CASE><id_evento>000001</id_evento><evento>RqAcessoColaborador</evento><modify_stmp>
            // 12/12/12 12:12:12</modify_stmp><sistema>WPPP</sistema><acao>ATUA</acao><ID_USUARIO/><NOME/><DEPARTAMENTO/>
            // <CARGO/><EMAIL/></CASE></root>
            
            // Armazena os dados da TAG cabecalho
            dadosUsuario.setid_evento((Long.parseLong(auxCaseNode.getElementsByTagName("id_evento").item(0).getChildNodes().item(0).getNodeValue().trim())));
            dadosUsuario.setevento(auxCaseNode.getElementsByTagName("evento").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setmodify_stmp(auxCaseNode.getElementsByTagName("modify_stmp").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setsistema(auxCaseNode.getElementsByTagName("sistema").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setacao(auxCaseNode.getElementsByTagName("acao").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setidUsuario(auxCaseNode.getElementsByTagName("ID_USUARIO").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setnomeUsuario(auxCaseNode.getElementsByTagName("NOME").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setdeptoUsuario(auxCaseNode.getElementsByTagName("DEPARTAMENTO").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setcargoUsuario(auxCaseNode.getElementsByTagName("CARGO").item(0).getChildNodes().item(0).getNodeValue().trim());
            dadosUsuario.setemailUsuario(auxCaseNode.getElementsByTagName("EMAIL").item(0).getChildNodes().item(0).getNodeValue().trim());
        }
        
        return dadosUsuario;        
    }

}
