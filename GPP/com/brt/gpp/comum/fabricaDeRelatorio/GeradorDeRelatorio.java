package com.brt.gpp.comum.fabricaDeRelatorio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.ResultSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.fabricaDeRelatorio.entidade.Relatorio;
import com.brt.gpp.comum.operacaoArquivo.ArquivoLeitura;
/**
 * Classe responsável pela geração de um Objeto <code>Relatorio</code>
 * a partir do banco de dados ou de um arquivo dado.
 * @author Magno Batista Corrêa
 *
 */
public class GeradorDeRelatorio
{
	/**
	 * Select para a obteção do XML que contém a definição do Relatório.
	 */
	public static final String sql =
		"SELECT " +
		"	XML AS XML " +
 		"FROM TBL_REL_FABRICA_RELATORIO " +
		"WHERE	IDT_RELATORIO = ?";
	/**
	 * Construtor vazio da classe.
	 *
	 */
	public GeradorDeRelatorio()
	{
	}
	
	/**
	 * Gera um novo Objeto Relatorio a partir do Banco de Dados
	 * @param relatorioNome
	 * @return Relatorio
	 * @throws Exception 
	 */
	public Relatorio novoRelatorioDoBanco(PREPConexao conexaoPrep, String relatorioNome) throws Exception 
	{
		String xml = null;
		Object[] parametroSql = {relatorioNome};
		ResultSet registros = conexaoPrep.executaPreparedQuery(sql, parametroSql, 0);
		
		while (registros.next())
		{
			Clob xmlClob = registros.getClob("XML");
			Reader xmlReader = xmlClob.getCharacterStream();
			StringBuffer xmlTmp = new StringBuffer();
			int charsRead = 0;
			char[] cbuf = new char[1024];

			while ((charsRead = xmlReader.read(cbuf)) != -1) {
				xmlTmp.append(cbuf, 0, charsRead);
			}
			xmlReader.close();

			xml = xmlTmp.toString();
		}
		registros.close();
		return this.parseXml(xml, null); //NULL é o primeiro Relatório
	}
	
	/**
	 * Gera um novo Objeto Relatorio a partir de um arquivo
	 * @param arquivoNome
	 * @param relatorioNome
	 * @return Relatorio
	 * @throws RelatorioException
	 */
	public Relatorio novoRelatorioDeArquivo(String arquivoNome, String relatorioNome) throws RelatorioException
	{
		ArquivoLeitura entrada = null;
		String xml = null;
		try
		{
			entrada = new ArquivoLeitura(arquivoNome);
			xml = entrada.lerTexto();
		} catch (FileNotFoundException e)
		{
			throw new RelatorioException("Arquivo de entrada do XML não encontrado!");
		} catch (UnsupportedEncodingException e)
		{
			throw new RelatorioException("O Formato de codificação de texto não é suportado.");
		} catch (IOException e)
		{
			throw new RelatorioException("Erro de entrada/saida");
		}
		return this.parseXml(xml,relatorioNome);
	}

	/**
	 * Gera um novo relatório com os dados do XML e do tipo relatorioNome
	 * @param xml O XML que descreve o  relatório
	 * @param relatorioNome O nome do relatório dentro de um XML ou NULL para leitura de banco ou o primeiro
	 * @return Relatorio Um novo relatório com os dados do XML e do tipo relatorioNome
	 * @throws RelatorioException
	 */
	public Relatorio parseXml(String xml, String relatorioNome) throws RelatorioException
	{

		if (xml == null)
			return null;
		
		//Criando objetos para executar o parse
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			throw new RelatorioException("Falha na configuração do Parse");
		}
		InputSource input = new InputSource(new StringReader(xml));
			
		//Executando o parse
		Document document;
		try
		{
			document = builder.parse(input);
		} catch (SAXException e)
		{
			throw new RelatorioException("Falha no SAX: " + e.getMessage());
		} catch (IOException e)
		{
			throw new RelatorioException("Falha de entrada/saida: " + e.getMessage());
		}
			
		NodeList ndlstRelatorio = document.getElementsByTagName("Relatorio");
		Element elmRelatorio = null;
		if(ndlstRelatorio == null)
		{
			return null;
		}
		// else
		int size = ndlstRelatorio.getLength();
		for(int i = 0; i < size; i++)
		{
			Element elmRelatorioTmp = (Element)ndlstRelatorio.item(i);
			if(relatorioNome == null || relatorioNome.equalsIgnoreCase(elmRelatorioTmp.getAttribute("nomeInterno")))
			{
				elmRelatorio = elmRelatorioTmp;
			}
		}
		return Relatorio.parseXML(elmRelatorio);
	}
}
