package br.com.brasiltelecom.ppp.portal.relatorio;

//Imports Java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

//Imports DOM
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.Relatorio;
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ProcessadorConsulta;
import br.com.brasiltelecom.ppp.portal.relatorio.resultado.GeradorResultado;

/**
 *	Fabrica de Geradores de relatorios
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class RelatorioFactory
{

	private static Properties factoryProps = null;
	
	/**
	 *	Construtor da classe. Utiliza o arquivo de propriedades passado por parametro.
	 *
	 *	@param		String					localizador				Nome completo do arquivo de propriedades da
	 *																Fabrica de relatorios.
	 */	
	public RelatorioFactory(String localizador) throws RelatorioException
	{
		this.loadProperties(localizador);
	}
	
	/**
	 *	Retorna um objeto representando o relatorio requisitado por parametro.
	 * 
	 *	@param		String					nomeRelatorio			Identificador do relatorio no XML de propriedades
	 *	@param		String					localizador				Caminho para o arquivo XML de propriedades
	 *	@throws		RelatorioException
	 */	
	public Relatorio newRelatorio(String nomeRelatorio, String localizador) throws RelatorioException
	{
		GeradorRelatorio result = null;
		
		try
		{
			//Obtendo o XML de propriedades
			String xmlPropriedades = this.getXmlPropriedades(localizador);
			
			//Obtendo as propriedades do relatorio
			Element elmRelatorio = this.parseXmlPropriedades(xmlPropriedades, nomeRelatorio);
			
			if(elmRelatorio != null)
			{
				//Objetos necessarios para a execucao da consulta na fonte de dados e para a formatacao do relatorio
				ProcessadorConsulta processador = null;
				GeradorResultado gerador = null;
				String format = null;
				String type = null;
				
				//Obtendo a propriedade de tipo de consulta na fonte de dados
				Element elmConsulta = (Element)elmRelatorio.getElementsByTagName("consulta").item(0);
				if(elmConsulta != null)
				{
					Attr attType = elmConsulta.getAttributeNode("type");
					if(attType != null)
					{
						//Carregando a classe definida pela propriedade
						type = attType.getNodeValue();
						String className = RelatorioFactory.factoryProps.getProperty("PROCESSADOR_CONSULTA_" + type);
						Class loader = Class.forName(className);
						
						//Instanciando o objeto ProcessadorConsulta
						Class[] argTypes = {};
						Constructor constructor = loader.getConstructor(argTypes);
						Object args[] = {};
						processador = (ProcessadorConsulta)constructor.newInstance(args);
					}
					else
					{
						throw new RelatorioException("Atributo de tipo de consulta nao encontrado");
					}
				}
				else
				{
					throw new RelatorioException("Propriedade de consulta nao encontrada");
				}
				
				//Obtendo a propriedade de formato do relatorio
				Attr attFormat = elmRelatorio.getAttributeNode("format");
				if(attFormat != null)
				{
					//Carregando a classe definida pela propriedade
					format = attFormat.getNodeValue();
					String className = RelatorioFactory.factoryProps.getProperty("GERADOR_RESULTADO_" + format);
					Class loader = Class.forName(className);
					
					//Instanciando o objeto GeradorResultado
					Class[] argTypes = {};
					Constructor constructor = loader.getConstructor(argTypes);
					Object args[] = {};
					gerador = (GeradorResultado) constructor.newInstance(args);
				}
				else
				{
					throw new RelatorioException("Atributo de formato de relatorio nao encontrado");
				}
				
				//Obtendo o Gerador de Relatorios
				String className = RelatorioFactory.factoryProps.getProperty("GERADOR_RELATORIO_" + type);
				Class loader = Class.forName(className);
				
				//Instanciando o objeto GeradorResultado
				Class[] argTypes = {};
				Constructor constructor = loader.getConstructor(argTypes);
				Object args[] = {};
				result = (GeradorRelatorio)constructor.newInstance(args);
				
				//Montando o resultado com os as classes adequadas
				result.setProcessadorConsulta(processador);
				result.setGeradorResultado(gerador);				
				result.setProperties(elmRelatorio);
			}
			else
			{
				throw new RelatorioException("Propriedades do relatorio nao encontradas");
			}
		}
		catch(ClassNotFoundException e)
		{
			throw new RelatorioException("Classe nao encontrada: " + e);
		}
		catch(NoSuchMethodException e)
		{
			throw new RelatorioException("Metodo nao encontrado: " + e);
		}
		catch(InvocationTargetException e)
		{
			throw new RelatorioException("Excecao de chamada de metodo: " + e);
		}
		catch(IllegalAccessException e)
		{
			throw new RelatorioException("Acesso ilegal a objeto: " + e);
		}
		catch(InstantiationException e)
		{
			throw new RelatorioException("Erro ao instanciar objeto: " + e);
		}
		catch(FileNotFoundException e)
		{
			throw new RelatorioException("Arquivo Nao Encontrado: " + localizador);
		}
		catch(IOException e)
		{
			throw new RelatorioException("Excecao de I/O: " + e);
		}
		catch(ParserConfigurationException e)
		{
			throw new RelatorioException("Excecao de Configuracao do Parser DOM: " + e);
		}
		catch(SAXException e)
		{
			throw new RelatorioException("Excecao SAX: " + e);
		}

		return result;
	}
	
	/**
	 *	Carrega o arquivo de propriedades da fabrica de relatorios
	 *
	 *	@param		String						localizador			Caminho completo para o arquivo de propriedades
	 *	@throws		RelatorioException
	 */
	private void loadProperties(String localizador) throws RelatorioException
	{
		try
		{
			//Obtendo os objetos responsaveis pela leitura do arquivo de propriedades
			File propriedades = new File(localizador);
			FileInputStream input = new FileInputStream(propriedades);

			//Carregando as propriedades da fabrica
			RelatorioFactory.factoryProps = new Properties();
			RelatorioFactory.factoryProps.load(input);
		}
		catch(FileNotFoundException e)
		{
			throw new RelatorioException("Arquivo de Propriedades da Fabrica de Relatorios nao encontrado: " + localizador);
		}
		catch(IOException e)
		{
			throw new RelatorioException("Excecao de I/O: " + e);
		}
	}
	
	/**
	 *	Le do arquivo informado e retorna o XML de propriedades
	 * 
	 *	@param		String		localizador				Caminho para o arquivo XML de propriedades
	 *	@throws		FileNotFoundException
	 *	@throws		IOException
	 *	@throws		NullPointerException
	 */
	private String getXmlPropriedades(String localizador) 
		throws FileNotFoundException, IOException, NullPointerException
	{
		StringBuffer result = new StringBuffer();
		
		//Obtendo os objetos responsaveis pela leitura do arquivo de propriedades
		File propriedades = new File(localizador);
		FileReader reader = new FileReader(propriedades);
			
		//Obtendo o tamanho do buffer de leitura do arquivo
		String bufferSize = RelatorioFactory.factoryProps.getProperty("BUFFER_SIZE");
		char[] buffer = new char[Integer.parseInt(bufferSize)];
		int bytesRead = 0;
			
		//Lendo do arquivo
		while((bytesRead = reader.read(buffer)) != -1)
		{
			result.append(buffer, 0, bytesRead);
		}
			
		//Fechando o arquivo
		reader.close();
		
		return result.toString();
	}
	
	/**
	 *	Interpreta o XML de propriedades e retorna o elemento correspondente ao relatorio passado por parametro
	 * 
	 *	@param		String		xmlPropriedades				XML de propriedades de relatorios
	 *	@param		String		nomeRelatorio				Nome do relatorio requisitado
	 *	@return		Element									Elemento que corresponde as propriedades do relatorio ou
	 *														NULL caso nao for encontrado
	 *	@throws		ParserConfigurationException
	 *	@throws		SAXException
	 *	@throws		IOException
	 */
	private Element parseXmlPropriedades(String xmlPropriedades, String nomeRelatorio) 
		throws ParserConfigurationException, SAXException, IOException
	{
		Element result = null;
		
		//Criando objetos para executar o parse
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource input = new InputSource(new StringReader(xmlPropriedades));
			
		//Executando o parse
		Document document = builder.parse(input);
			
		//Extraindo informacoes do documento
		Element elmRoot = (Element)document.getElementsByTagName("RelatorioProperties").item(0);
		Element elmRelatorios = (Element)elmRoot.getElementsByTagName("Relatorios").item(0);
			
		//Percorrendo os elementos de relatorio para ate encontrar o elemento especifico
		NodeList ndlstRelatorio = elmRelatorios.getElementsByTagName("Relatorio");
		Element elmRelatorio = null;
		for(int i = 0; (i < ndlstRelatorio.getLength()) && (elmRelatorio == null); i++)
		{
			elmRelatorio = (Element)ndlstRelatorio.item(i);				
			if(elmRelatorio.getAttributeNode("name").getNodeValue().equalsIgnoreCase(nomeRelatorio))
			{
				result = elmRelatorio;
			}
			else
			{
				elmRelatorio = null;
			}
		}

		return result;
	}
	
}
