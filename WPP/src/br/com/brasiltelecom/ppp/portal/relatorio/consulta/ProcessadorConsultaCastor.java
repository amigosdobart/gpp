package br.com.brasiltelecom.ppp.portal.relatorio.consulta;

//Imports Java
import java.util.ArrayList;
import java.util.Map;

//Imports DOM
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//Imports do Castor
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;


//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.CastorResultSet;

/**
 *	Processador de consultas OQL do Castor.
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class ProcessadorConsultaCastor implements ProcessadorConsulta 
{
	
	/**
	 *	Construtor da classe
	 */
	public ProcessadorConsultaCastor()
	{
		super();
	}
	
	/**
	 *	Metodo responsavel pela execucao da consulta SQL pelos dados do relatorio
	 * 
	 *	@param		Map			params			Mapeamento de parametros do relatorio (nome, parametros de consulta)	
	 *	@param		Element		elmConsulta		Objeto DOM que representa as propriedades da consulta
	 *	@param		Object		conexao			Objeto PREPConexao para conexao com o Banco de Dados 
	 *	@return		ResultSet					ResultSet gerado pela execucao da consulta pelos dados do relatorio.
	 *											O objeto e instancia de java.sql.ResultSet
	 *	@throws		RelatorioException		
	 */
	public ResultSet processaConsulta(Map params, Element elmConsulta, Object conexao) throws RelatorioException
	{
		ResultSet result = null;
		Database database = (Database)conexao;
		ArrayList paramList = new ArrayList();
		
		try
		{
			//Obtendo os parametros da consulta
			NodeList ndlstParam = elmConsulta.getElementsByTagName("param");
			if(ndlstParam != null)
			{
				for(int i = 0; i < ndlstParam.getLength(); i++)
				{
					Element elmParam = (Element)ndlstParam.item(i);
					
					Attr attMapName = elmParam.getAttributeNode("mapName");
					if(attMapName != null)
					{
						paramList.add(i, params.get(attMapName.getNodeValue()));
					}
				}
			}
			
			//Executando a consulta e obtendo o ResultSet
			Element elmStatement = (Element)elmConsulta.getElementsByTagName("statement").item(0);
			if(elmStatement != null)
			{
				String query = this.format(elmStatement.getChildNodes().item(0).getNodeValue());
				OQLQuery oqlQuery = database.getOQLQuery(query);
				
				//Inserindo os parametros
				for(int i = 0; i < paramList.size(); i++)
				{
					oqlQuery.bind(paramList.get(i));
				}
				
				result = new CastorResultSet(oqlQuery.execute());
			}
			else
			{
				throw new RelatorioException("Propriedade de consulta nao encontrada");
			}
		}
		catch(PersistenceException e)
		{
			throw new RelatorioException("Excecao de Persistencia do Castor: " + e);
		}
		
		return result;
	}

	/**
	 *	Formata a consulta de forma a retirar caracteres especiais, que podem levar o Castor a lancar excecao
	 * 
	 *	@param		String		query			Consulta a ser executada 
	 *	@return		String						Consulta formatada
	 *	@throws		RelatorioException		
	 */
	private String format(String query)
	{
		String result = null;
		
		if(query != null)
		{
			result = query.replaceAll("\t", " ");
			result = result.replaceAll("\n", " ");
		}
		
		return result.trim();
	}
	
}
