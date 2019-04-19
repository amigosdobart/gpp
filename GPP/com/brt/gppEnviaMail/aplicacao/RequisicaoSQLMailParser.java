package com.brt.gppEnviaMail.aplicacao;

import java.io.StringReader;
import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class RequisicaoSQLMailParser
{
	private String xmlRequisicao;

	public RequisicaoSQLMailParser(String xml)
	{
		this.xmlRequisicao = xml;
	}
	
	public RequisicaoSQLMail getRequisicao() throws SAXException,ParserConfigurationException,IOException
	{
		RequisicaoSQLMail requisicao = new RequisicaoSQLMail();
		// Busca uma instancia de um DocumentBuilder
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		
		// Cria um parse de XML
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		
		// Carrega o XML DEBUGrmado dentro e um InputSource
		InputSource is = new InputSource(new StringReader(xmlRequisicao));
		
		// Faz o parse do XML
		Document doc = parse.parse(is);
		
		// Procura a TAG Inicial
		Element serviceElement = (Element) doc.getElementsByTagName("requisicaoSQLMail").item(0);
		NodeList itemNodes = serviceElement.getChildNodes();
		
		requisicao.setAssuntoMail	 		( getNodeValueByTag(itemNodes,"assunto").trim()				);
		requisicao.setMensagem		 		( getNodeValueByTag(itemNodes,"mensagem").trim()			);
		requisicao.setEnderecosDestino		( getNodeValueByTag(itemNodes,"enderecosDestino").split(","));
		requisicao.setNomeArquivoCompactado	( getNodeValueByTag(itemNodes,"nomeArquivoCompactado").trim());
		requisicao.setCompactarArquivos		( Boolean.valueOf(getNodeValueByTag(itemNodes,"compactarArquivos").toUpperCase()).booleanValue());
		
		// Faz a iteracao nos elementos da tag de comandos SQL
		NodeList comandos = ((Element)itemNodes).getElementsByTagName("sql");
		for (int i=0; i < comandos.getLength(); i++)
		{
			Node no = comandos.item(i);
			if (no != null)
			{
				ComandoSQL comando = new ComandoSQL(getNodeValueByTag(no.getChildNodes(),"statementSQL").trim()	);
				comando.setNomeArquivo		 (getNodeValueByTag(no.getChildNodes(),"nomeArquivo").trim()		);
				comando.setProcessadorArquivo(getNodeValueByTag(no.getChildNodes(),"processadorArquivo").trim()	);
				comando.setSeparador		 (getNodeValueByTag(no.getChildNodes(),"separador").trim()			);
				comando.setEnviarComoAnexo	 (Boolean.valueOf(getNodeValueByTag(no.getChildNodes(),"enviarComoAnexo" ).toUpperCase()).booleanValue());
				comando.setCompactaArquivo	 (Boolean.valueOf(getNodeValueByTag(no.getChildNodes(),"compactarArquivo").toUpperCase()).booleanValue());

				requisicao.addComandoSQL(comando);
			}
		}
		return requisicao;
	}
	
	private String getNodeValueByTag(NodeList itemNodes, String tagName)
	{
		Node no = ((Element)itemNodes).getElementsByTagName(tagName).item(0).getChildNodes().item(0);
		return no != null ? no.getNodeValue() : "";
	}
}
