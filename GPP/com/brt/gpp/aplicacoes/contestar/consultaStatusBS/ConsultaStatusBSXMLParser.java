package com.brt.gpp.aplicacoes.contestar.consultaStatusBS;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.brt.gpp.aplicacoes.contestar.entidade.Contestacao;
import com.brt.gpp.aplicacoes.contestar.entidade.ProtocoloNativo;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;

/**
 * Classe abstrata responsavel por conter os Parsers de XML
 * relacionados a Contestacao (Protocolo Unico)
 * 
 * @author João Paulo Galvagni
 * @since  07/03/2007
 */
public abstract class ConsultaStatusBSXMLParser
{
	static SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
	static SimpleDateFormat sdfInterno = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * Metodo....: parseXMLConsulta
	 * Descricao.: Realiza o parse do XML de consulta de status de BS,
	 * 			   selecionando os BSs para execucao da consulta
	 * 
	 * @param  xmlConsulta	- XML a ser realizado o parse
	 * @return contestacao	- Objeto contendo as informacoes do XML
	 */
	public static Contestacao parseXMLConsulta(String xmlConsulta) throws Exception
	{
		// Cria a lista de BS's a ser retornada
		Contestacao contestacao = new Contestacao();
		
		// Arvore do XML esperado ([@] -> repeticao)
		// mensagem;conteudo;root;consulta-status-servico-realizado[@];protocolo-nativo
		
		try
		{
			// Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlConsulta));
			Document doc = parse.parse(is);
			
			// Cria um NodeList da <mensagem> e do <cabecalho>
			NodeList mensagem  = ((Element) doc.getElementsByTagName("mensagem").item(0)).getChildNodes();
			NodeList cabecalho = ((Element)mensagem).getElementsByTagName("cabecalho").item(0).getChildNodes();
			
			// Seta o nome do processo e o identificador da requisicao: TAG <processo> e <identificador_requisicao>
			if (((Element)cabecalho).getElementsByTagName("processo").item(0) != null && 
				((Element)cabecalho).getElementsByTagName("processo").item(0).getChildNodes().item(0) != null )
				contestacao.setProcesso(((Element)cabecalho).getElementsByTagName("processo").item(0).getChildNodes().item(0).getNodeValue());
			if (((Element)cabecalho).getElementsByTagName("identificador_requisicao").item(0) != null && 
				((Element)cabecalho).getElementsByTagName("identificador_requisicao").item(0).getChildNodes().item(0) != null )
				contestacao.setIdentificador(((Element)cabecalho).getElementsByTagName("identificador_requisicao").item(0).getChildNodes().item(0).getNodeValue());
			
			// Cria um NodeList do <conteudo>
			NodeList conteudo  = ((Element)mensagem).getElementsByTagName("conteudo").item(0).getChildNodes();
			
			// Realiza o Parse do XML contido na TAG <![CDATA[ xml ]]>
			NodeList root = parseXML(conteudo.item(0).getNodeValue(), "root");
			
			for (int i = 0; i < root.getLength(); i++)
			{
				String valorNoProtocoloNativo = ((Element)root.item(i)).getElementsByTagName("protocolo-nativo").item(0).getChildNodes().item(0).getNodeValue();
				
				// Valida se o valor do no nao eh null e diferente de ""
				if (valorNoProtocoloNativo != null && !"".equals(valorNoProtocoloNativo))
				{
					ProtocoloNativo protocoloNativo = new ProtocoloNativo();
					
					protocoloNativo.setProtocoloNativo(valorNoProtocoloNativo);
					contestacao.addListaProtocolosNativos(protocoloNativo);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		// Retorna o objeto populado
		return contestacao;
	}
	
	/**
	 * Metodo....: getXMLRetornoConsulta
	 * Descricao.: Retorna o XML resultante da consulta
	 * 
	 * @param  contestacao 			- Objeto contendo o resultado da consulta
	 * @return retornoConsultaXML	- XML de retorno da consulta
	 */
	public static String getXMLRetornoConsulta(Contestacao contestacao)
	{
		StringBuffer retornoConsultaXML = new StringBuffer(getCabecalhoXML(contestacao.getProcesso(), contestacao.getIdentificador(), null));
		
		GerarXML gerador = new GerarXML("root");
		
		//retornoConsultaXML.append("<root>");
		ProtocoloNativo protocolo = new ProtocoloNativo();
		
		// Varre a lista de BS's e monta um ProtocoloNativo para cada BS
		for (Iterator i = contestacao.getListaProtocolosNativos().iterator(); i.hasNext(); )
		{
			// Seleciona os dados do protocolo
			protocolo = (ProtocoloNativo)i.next();
			
			// Codigo original para a consulta de status de BS
			// Verifica se o mesmo nao eh null
			if (protocolo != null)
			{
				gerador.abreNo("status-servico-realizado"); // Abre a tag status-servico-realizado
				
				gerador.adicionaTag("protocolo-nativo", protocolo.getProtocoloNativo());
				gerador.adicionaTag("status-servico", protocolo.getStatus());
					
				if (protocolo.getMsgErro() != null && !"".equals(protocolo.getMsgErro()))
					gerador.adicionaTag("msg-erro", protocolo.getMsgErro());
				else
					gerador.adicionaTag("fechado", (protocolo.isFechado() ? "F" : "A"));
				
				// Fecha a tag <status-servico-realizado>
				gerador.fechaNo();
			}
			else
				gerador.adicionaTag("status-servico-realizado", null);
		}
		
		// Inclui o rodape do XML
		retornoConsultaXML.append(gerador.getXML()).append(getRodapeXML());
		
		// Retorna o XML devidamente montado
		return retornoConsultaXML.toString();
	}
	
	/**
	 * Metodo....: parseXML
	 * Descricao.: Realiza o parse apenas para obter o 
	 * 			   conteudo interno da tag [CDATA[]]
	 * 
	 * @param  xml			- XML completo a partir do [CDATA[]]
	 * @param  tagPrincipal	- Nome da tag principal contida no [CDATA[]] default: root
	 * @return NodeList 	- NodeList da tag interna ao [CDATA[]]
	 * @throws Exception 
	 */
	public static NodeList parseXML(String xml, String tagPrincipal) throws Exception
	{
		try
		{
			// Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = parse.parse(is);
			
			// Cria um NodeList da tag principal <tagPrincipal>
			NodeList root = ((Element) doc.getElementsByTagName(tagPrincipal).item(0)).getChildNodes();
			
			return root;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * Metodo....: getCabecalhoXML
	 * Descricao.: Retorna o Cabecalho do XML
	 * 
	 * @param  processo		 - Processo
	 * @param  identificador - Identificador da requisicao
	 * @return cabecalhoXML	 - Cabecalho do XML devidamente montado
	 */
	public static String getCabecalhoXML(String processo, String identificador, String numeroProtocolo)
	{
		StringBuffer cabecalhoXML = new StringBuffer(Definicoes.XML_PROLOG+"<mensagem>");
		if (identificador == null || "".equals(identificador))
			identificador = sdf.format(Calendar.getInstance().getTime()) + Definicoes.SO_GPP + numeroProtocolo;
		
		GerarXML gerador = new GerarXML("cabecalho"); // Abre o no <cabecalho>
		gerador.adicionaTag("empresa", Definicoes.EMPRESA_GSM);
		gerador.adicionaTag("sistema", Definicoes.GPP_OPERADOR);
		if (processo != null && !"".equals(processo))
			gerador.adicionaTag("processo", processo);
		else
			gerador.adicionaTag("processo", null);
		gerador.adicionaTag("data", sdfInterno.format(Calendar.getInstance().getTime()));
		gerador.adicionaTag("identificador_requisicao", identificador);
		
		cabecalhoXML.append(gerador.getXML());
		cabecalhoXML.append("<conteudo><![CDATA[").append(Definicoes.XML_PROLOG);
		
		// Retorna o XML ja montado
		return cabecalhoXML.toString();
	}
	
	/**
	 * Metodo....: getRodapeXML
	 * Descricao.: Retorna o rodape do XML
	 * 
	 * @return rodapeXML - Rodape do XML
	 */
	public static String getRodapeXML()
	{
		StringBuffer rodapeXML = new StringBuffer("]]>");
			rodapeXML.append("</conteudo>");
		rodapeXML.append("</mensagem>");
		
		return rodapeXML.toString();
	}
}