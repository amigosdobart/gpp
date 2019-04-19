package br.com.brasiltelecom.ppp.portal.relatorio.resultado;

//Imports Java
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

//Classes DOM para parse e geracao dos XML's
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.resultado.FormatadorCampo;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;

/**
 *	Gerador de resultado de relatorio em formato delimitado para escrita em paginas web.
 * 
 *	@author		Daniel Ferreira
 *	@since		27/07/2005
 */
public class GeradorResultadoTextHTML implements GeradorResultado 
{

	/**
	 *	Construtor da classe
	 */
	public GeradorResultadoTextHTML()
	{
		super();
	}
	
	/**
	 *	Monta o relatorio atraves da execucao do parse do XML de propriedades de relatorios em formato delimited.
	 *	Esta versao e propria para relatorios delimitados que sao escritos em paginas HTML. 
	 *  O XML de propriedades e um guia para a geracao do formato do relatorio. O elemento representando o
	 *	relatorio (cujo nome deve estar presente no cabecalho) deve estar presente neste XML. Caso contrario uma
	 *	excecao e lancada.
	 * 
	 *	@param		Map					params			Objeto contendo as informacoes do cabecalho do relatorio
	 *	@param		ResultSet			dados			Objeto contendo o resultado da pesquisa 
	 *	@param		Element				elmRelatorio	Objeto DOM que corresponde as propriedades do relatorio
	 *	@param		OutputStream		output			Objeto representado a saida para o relatorio. Os valores
	 *													formatados obtidos pela consulta sao escritos no OutputStream
	 *													passado por parametro
	 *	@throws		RelatorioException
	 */
	public void geraResultado(Map params, ResultSet dados, Element elmRelatorio, OutputStream output) 
		throws RelatorioException
	{
		boolean showHeader = false;
		boolean delimitedHdr = false;
		
		//Verificando se o elemento de propriedades do relatorio foi encontrado
		if(elmRelatorio != null)
		{
			try
			{
				output.write("<table border=0>".getBytes());
			
				//Verificando a necessidade de mostrar o cabecalho do relatorio. Se o atributo nao existir no XML,
				//assumir o valor como falso.
				Attr attShowHeader = elmRelatorio.getAttributeNode("showHeader");
				if(attShowHeader != null)
				{
					showHeader = Boolean.valueOf(attShowHeader.getNodeValue()).booleanValue();
					//Se for necessario mostrar o cabecalho, obter suas propriedades
					if(showHeader)
					{
						Element elmCabecalho = (Element)elmRelatorio.getElementsByTagName("cabecalho").item(0);
						if(elmCabecalho != null)
						{
							this.montaCabecalho(params, elmCabecalho, output);
						}
						else
						{
							throw new RelatorioException("Propriedades obrigatorias do cabecalho nao encontradas");
						}
					}
				}				
			
				//Verificando a necessidade de mostrar o cabecalho dos registros (corpo do relatorio). Se o atributo
				//nao existir, assumir o valor como falso.
				Attr attDelimitedHdr = elmRelatorio.getAttributeNode("delimitedHdr");
				if(attDelimitedHdr != null)
				{
					delimitedHdr = Boolean.valueOf(attDelimitedHdr.getNodeValue()).booleanValue();
				}
			
				//Gerando o corpo do relatorio
				this.montaCorpoRelatorio(dados, elmRelatorio, delimitedHdr, output);
			
				output.write("</table>".getBytes());
			}
			catch(IOException e)
			{
				throw new RelatorioException("Excecao de I/O: " + e);
			}
		}
		else
		{	
			throw new RelatorioException("Propriedades do relatorio nao encontradas");
		}
}

	/**
	 *	Monta o cabecalho do relatorio utilizando as propriedades informadas no XML de propriedades.
	 * 
	 *	@param		Map				cabecalho		Objeto contendo as informacoes do cabecalho do relatorio
	 *	@param		Element			elmCabecalho	Elemento DOM que corresponde as propriedades do cabecalho do 
	 *												relatorio
	 *	@param		OutputStream	output			Saida para gravacao do cabecalho do relatorio
	 *	@throws		RelatorioException
	 */
	private void montaCabecalho(Map params, Element elmCabecalho, OutputStream output)
		throws RelatorioException
	{
		try
		{
			//Obtendo o titulo do cabecalho
			Attr title = elmCabecalho.getAttributeNode("title");
			if(title != null)
			{
				output.write("<tr><td>".getBytes());
				output.write(title.getNodeValue().getBytes());
				output.write("</td></tr>".getBytes());
			}
		
			//Obtendo os valores do cabecalho
			StringBuffer headerBuffer = new StringBuffer();
			StringBuffer valueBuffer = new StringBuffer();
		
			NodeList ndlstCampo = elmCabecalho.getElementsByTagName("campo");
			for(int i = 0; i < ndlstCampo.getLength(); i++)
			{
				Element elmCampo = (Element)ndlstCampo.item(i);

				Attr attShowName = elmCampo.getAttributeNode("showName");
				if(attShowName != null)
				{
					headerBuffer.append("<td>");
					headerBuffer.append(attShowName.getNodeValue());
					headerBuffer.append("</td>");
				}
			
				Attr attMapName = elmCampo.getAttributeNode("mapName");
				if(attMapName != null)
				{
					String value = (String)params.get(attMapName.getNodeValue());
					if(value != null)
					{
						valueBuffer.append("<td>");
						valueBuffer.append(value);
						valueBuffer.append("</td>");
					}
				}
			}
		
			//Montando o resultado final
			output.write("<tr>".getBytes());
			output.write(headerBuffer.toString().getBytes());
			output.write("</tr><tr>".getBytes());
			output.write(valueBuffer.toString().getBytes());
			output.write("</tr><tr/>".getBytes());
			output.flush();
		}
		catch(IOException e)
		{
			throw new RelatorioException("Excecao de I/O: " + e);
		}
	}

	/**
	 *	Monta o corpo do relatorio utilizando o ResultSet da consulta SQL e as propriedades no XML de propriedades.
	 * 
	 *	@param		ResultSet			dados			Objeto contendo o resultado da pesquisa 
	 *	@param		Element				elmDados		Elemento DOM que corresponde as propriedades do corpo do 
	 *													relatorio
	 *	@param		boolean				delimitedHdr	Flag indicando se o cabecalho dos registros deve ser 
	 *													apresentado
	 *	@param		OutputStream		output			Objeto que grava os dados no arquivo de relatorio
	 *	@throws		RelatorioException
	 */
	private void montaCorpoRelatorio(ResultSet dados, Element elmRelatorio, boolean delimitedHdr, OutputStream output)
		throws RelatorioException
	{
		try
		{
			Element elmDados = (Element)elmRelatorio.getElementsByTagName("dados").item(0);
			if(elmDados != null)
			{
				Element elmRegistro = (Element)elmDados.getElementsByTagName("registro").item(0);
				if(elmRegistro != null)
				{
					NodeList ndlstCampo = elmRegistro.getElementsByTagName("campo");
				
					//Apresentar o cabecalho dos registros, caso necessario
					if(delimitedHdr)
					{
						output.write("<tr>".getBytes());
						
						for(int i = 0; i < ndlstCampo.getLength(); i++)
						{
							output.write("<td>".getBytes());
							
							Element elmCampo = (Element)ndlstCampo.item(i);
							
							Attr attShowName = elmCampo.getAttributeNode("showName");
							if(attShowName != null)
							{
								output.write(attShowName.getNodeValue().getBytes());
							}
						
							output.write("</td>".getBytes());
						}
						
						output.write("</tr>".getBytes());
					}
					
					output.flush();
				
					//Montando os dados resultantes da consulta
					while(dados.next())
					{
						output.write("<tr>".getBytes());
						
						for(int i = 0; i < ndlstCampo.getLength(); i++)
						{
							output.write("<td>".getBytes());
							Element elmCampo = (Element)ndlstCampo.item(i);
							Attr attName = elmCampo.getAttributeNode("name");
							if(attName != null)
							{
								//Obtendo o tipo de dados definido nas propriedades e a mascara a ser aplicada.
								String name = attName.getNodeValue();
								String mask = null;
								Attr attMask = elmCampo.getAttributeNode("mask");
								if(attMask != null)
								{
									mask = attMask.getNodeValue();
								}
								Attr attType = elmCampo.getAttributeNode("type");
								if((attType != null) && (attType.getNodeValue() != null))
								{
									String type = attType.getNodeValue();
									output.write(FormatadorCampo.formataCampo(dados, name, type, mask).getBytes());
								}
								else
								{
									output.write(dados.getString(attName.getNodeValue()).getBytes());
								}
							}
							output.write("</td>".getBytes());
						}
						output.write("</tr>".getBytes());
					}
					output.flush();
				}
				else
				{
					throw new RelatorioException("Propriedades de registros da consulta nao encontradas");
				}
			}
			else
			{
				throw new RelatorioException("Propriedades do corpo do relatorio nao encontradas");
			}
			
			output.write("<tr/>".getBytes());
			output.flush();
		}
		catch(IOException e)
		{
			throw new RelatorioException("Excecao de I/O: " + e);
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
}
