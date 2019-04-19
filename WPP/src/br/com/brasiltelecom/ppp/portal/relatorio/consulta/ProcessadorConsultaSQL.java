package br.com.brasiltelecom.ppp.portal.relatorio.consulta;

//Imports Java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

//Imports DOM
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.SQLResultSet;
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;

/**
 *	Interface utilizada pelos geradores de relatorios para obter o objeto String contendo o resultado do relatorio.
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class ProcessadorConsultaSQL implements ProcessadorConsulta 
{
	
	/**
	 *	Construtor da classe
	 */
	public ProcessadorConsultaSQL()
	{
		super();
	}
	
	/**
	 *	Metodo responsavel pela execucao da consulta SQL pelos dados do relatorio
	 * 
	 *	@param		Map			params			Mapeamento de parametros do relatorio (nome, parametros de consulta)	
	 *	@param		Element		elmConsulta		Objeto DOM que representa as propriedades da consulta
	 *	@param		Object		conexao			Objeto Connection para conexao com o Banco de Dados 
	 *	@return		ResultSet					ResultSet gerado pela execucao da consulta pelos dados do relatorio.
	 *											O objeto e instancia de java.sql.ResultSet
	 *	@throws		RelatorioException		
	 */
	public ResultSet processaConsulta(Map params, Element elmConsulta, Object conexao) 
		throws RelatorioException
	{
		ResultSet result = null;
		Connection connection = (Connection)conexao;
		
		try
		{
			//Executando a consulta e obtendo o ResultSet
			Element elmStatement = (Element)elmConsulta.getElementsByTagName("statement").item(0);
			if(elmStatement != null)
			{
				String sqlQuery = elmStatement.getChildNodes().item(0).getNodeValue();
				PreparedStatement statement = connection.prepareStatement(sqlQuery);
				
				//Obtendo os parametros da consulta
				NodeList ndlstParam = elmConsulta.getElementsByTagName("param");
				if(ndlstParam != null)
				{
					for(int i = 1; i <= ndlstParam.getLength(); i++)
					{
						Element elmParam = (Element)ndlstParam.item(i - 1);
						
						Attr attMapName = elmParam.getAttributeNode("mapName");
						if(attMapName != null)
						{
							Object paramValue = params.get(attMapName.getNodeValue());
							if(paramValue != null)
							{
								if(paramValue instanceof java.util.Date)
								{
									statement.setDate(i, new java.sql.Date(((java.util.Date)paramValue).getTime()));
								}
								else
								{
									statement.setObject(i, paramValue);
								}
							}
						}
					}
				}
				
				result = new SQLResultSet(statement, statement.executeQuery());
			}
			else
			{
				throw new RelatorioException("Propriedade de consulta nao encontrada");
			}
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
		
		return result;
	}
	
}
