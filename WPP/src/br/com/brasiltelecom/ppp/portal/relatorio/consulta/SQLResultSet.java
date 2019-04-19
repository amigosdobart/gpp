package br.com.brasiltelecom.ppp.portal.relatorio.consulta;

//Imports Java
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;

/**
 *	Classe que implementa a interface ResultSet de consultas de relatorios 
 *	e encapsula a interface java.sql.ResultSet
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class SQLResultSet implements ResultSet 
{

	private PreparedStatement statement;
	private java.sql.ResultSet result;
	
	/**
	 *	Construtor da classe
	 * 
	 *	@param		PreparedStatement		statement		Objeto representando a instrucao SQL que gera o ResultSet	
	 *	@param		java.sql.ResultSet		result			ResultSet obtido a partir de consulta SQL atraves de JDBC	
	 */
	public SQLResultSet(PreparedStatement statement, java.sql.ResultSet result)
	{
		this.statement = statement;
		this.result = result; 
	}
	
	/**
	 *	Retorna o PreparedStatement encapsulado
	 * 
	 *	@return		PreparedStatement		result			Objeto representando a instrucao SQL que gera o ResultSet	
	 */
	public PreparedStatement getStatement()
	{
		return this.statement;
	}
	
	/**
	 *	Retorna o ResultSet encapsulado
	 * 
	 *	@return		java.sql.ResultSet		result			ResultSet obtido a partir de consulta SQL atraves de JDBC	
	 */
	public java.sql.ResultSet getResultSet()
	{
		return this.result;
	}
	
	/**
	 *	Atribui o parametro ao PreparedStatement encapsulado
	 * 
	 *	@param		PreparedStatement		statement		Objeto representando a instrucao SQL que gera o ResultSet	
	 */
	public void setStatement(PreparedStatement statement)
	{
		this.statement = statement; 
	}
	
	/**
	 *	Atribui o parametro ao ResultSet encapsulado
	 * 
	 *	@param		java.sql.ResultSet		result			ResultSet obtido a partir de consulta SQL atraves de JDBC	
	 */
	public void setResultSet(java.sql.ResultSet result)
	{
		this.result = result; 
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		boolean									Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public boolean getBoolean(String campo) throws RelatorioException
	{
		try
		{
			return this.result.getBoolean(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		char									Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public char getChar(String campo) throws RelatorioException
	{
		try
		{
			String result = this.result.getString(campo);
			if(result != null)
			{
				return result.charAt(0);
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		int										Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public Date getDate(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getDate(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		double									Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public double getDouble(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getDouble(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		float									Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public float getFloat(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getFloat(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		int										Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public int getInt(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getInt(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}

	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		long									Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public long getLong(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getLong(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
		
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		String									Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public String getString(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getString(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String					campo			Campo de onde se obter o valor	
	 *	@return		Timestamp								Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public Timestamp getTimestamp(String campo)	throws RelatorioException
	{
		try
		{
			return this.result.getTimestamp(campo);
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Move o cursor para o proximo registro
	 *
	 *	@return		boolean									True se ainda existia registros a serem percorridos ou 
	 *														false caso contrario
	 *	@throws		RelatorioException		
	 */
	public boolean next()	throws RelatorioException
	{
		try
		{
			return this.result.next();
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
	
	/**
	 *	Fecha o ResultSet
	 *
	 *	@throws		RelatorioException		
	 */
	public void close()	throws RelatorioException
	{
		try
		{
			if(this.result != null) 
			{
				this.result.close();
			}
			if(this.statement != null) 
			{
				this.statement.close();
			}
		}
		catch(SQLException e)
		{
			throw new RelatorioException("Excecao SQL: " + e);
		}
	}
		
}
