package br.com.brasiltelecom.ppp.portal.relatorio;

//Imports Java
import java.io.OutputStream;
import java.util.HashMap;

//Imports DOM
import org.w3c.dom.Element;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.Relatorio;
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ProcessadorConsulta;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;
import br.com.brasiltelecom.ppp.portal.relatorio.resultado.GeradorResultado;

/**
 *	Superclasse abstrata de para geracao de relatorios
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public abstract class GeradorRelatorio implements Relatorio
{
	
	protected Element properties;
	protected ProcessadorConsulta processador;
	protected GeradorResultado gerador;
	protected HashMap params;
	protected Object conexao;
	
	/**
	 *	Construtor da classe
	 */
	public GeradorRelatorio()
	{
		this.properties = null;
		this.processador = null;
		this.gerador = null;
		this.params = new HashMap();
		this.conexao = null;
	}
	
	/**
	 *	Construtor da classe
	 * 
	 *	@param		Element					properties		Propriedades do relatorio
	 *	@param		ProcessadorConsulta		processador		Processador da consulta na fonte de dados
	 *	@param		GeradorResultado		gerador			Gerador do formato do relatorio
	 */
	public GeradorRelatorio(Element properties, ProcessadorConsulta processador, GeradorResultado gerador)
	{
		this.properties = properties;
		this.processador = processador;
		this.gerador = gerador;
		this.params = new HashMap();
		this.conexao = null;
		this.conexao = null;
	}
	
	/**
	 *	Inicia a conexao com a fonte de dados. A forma como a conexao e iniciada deve ser implementada pelas classes
	 *	herdeiras.
	 */
	public abstract void iniciaConexao() throws Exception;
	
	/**
	 *	Finaliza a conexao com a fonte de dados. A forma como a conexao e finalizada deve ser implementada pelas 
	 *	classes herdeiras.
	 */
	public abstract void finalizaConexao() throws Exception;
	
	/**
	 *	Retorna as propriedades de relatorio do objeto
	 *
	 *	@return		Element			As propriedades do relatorio 
	 */
	public Element getProperties()
	{
		return this.properties;
	}
	
	/**
	 *	Retorna o processador de consulta do objeto
	 *
	 *	@return		ProcessadorConsulta			O processador de consulta do objeto 
	 */
	public ProcessadorConsulta getProcessadorConsulta()
	{
		return this.processador;
	}
	
	/**
	 *	Retorna o gerador de resultado do objeto 
	 *
	 *	@return		GeradorResultado			O gerador de resultado do objeto 
	 */
	public GeradorResultado getGeradorResultado()
	{
		return this.gerador;
	}
	
	/**
	 *	Atribui as propriedades de relatorio do objeto
	 *
	 *	@param		Element					properties			As propriedades de relatorio a serem atribuidas ao
	 *															objeto 
	 */
	public void setProperties(Element properties)
	{
		this.properties = properties;
	}
	
	/**
	 *	Atribui o processador de consulta do objeto
	 *
	 *	@param		ProcessadorConsulta		processador			O processador de consulta a ser atribuido ao objeto 
	 */
	public void setProcessadorConsulta(ProcessadorConsulta processador)
	{
		this.processador = processador;
	}
	
	/**
	 *	Atribui o gerador de resultado do objeto
	 *
	 *	@param		GeradorResultado		gerador				O gerador de resultado a ser atribuido ao objeto 
	 */
	public void setGeradorResultado(GeradorResultado gerador)
	{
		this.gerador = gerador;
	}

	/**
	 *	Adiciona um parametro de consulta ao relatorio
	 *
	 *	@param		String			paramName				Nome do parametro
	 *	@param		Object			paramValue				Valor do parametro 
	 */
	public void addParam(String paramName, Object paramValue)
	{
		this.params.put(paramName, paramValue);
	}
	
	/**
	 *	Gera o relatorio conforme especificado nas propriedades do relatorio e escreve o resultado no objeto 
	 *	OutputStream passado por parametro 
	 *
	 *	@param		OutputStream			output			Saida do relatorio
	 *	@throws		RelatorioException
	 */
	public void execute(OutputStream output) throws RelatorioException
	{
		try
		{
			//Iniciando a conexao com a fonte de dados
			this.iniciaConexao();
						
			//Obtendo as propriedades da consulta
			Element elmConsulta = (Element)this.properties.getElementsByTagName("consulta").item(0);
			if(elmConsulta != null)
			{
				//Obtendo o ResultSet da consulta na fonte de dados
				ResultSet resultSet = processador.processaConsulta(this.params, elmConsulta, this.conexao);
				
				//Formatando e obtendo o relatorio de acordo com as suas propriedades
				gerador.geraResultado(this.params, resultSet, this.properties, output);
				
				//Fechando o ResultSet
				resultSet.close();
			}
			else
			{
				throw new RelatorioException("Propriedade de consulta nao encontrada");
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException(e.getMessage());
		}
		finally
		{
			try
			{
				this.finalizaConexao();
			}
			catch(Exception e)
			{
				throw new RelatorioException(e.getMessage());
			}
		}
	}

}
