package br.com.brasiltelecom.ppp.portal.relatorio.consulta;

//Imports Java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

//Imports do Castor
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;

/**
 *	Classe que implementa a interface ResultSet de consultas de relatorios 
 *	e encapsula a interface QueryResults
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class CastorResultSet implements ResultSet 
{
	
	private QueryResults result;
	private Object registro;
	
	/**
	 *	Construtor da classe
	 * 
	 *	@param		QueryResults		result		ResultSet obtido a partir de consulta OQL do Castor	
	 */
	public CastorResultSet(QueryResults result)
	{
		this.registro = null;
		this.result = result; 
	}
	
	/**
	 *	Retorna o ResultSet encapsulado
	 * 
	 *	@return		QueryRsults			result		ResultSet obtido a partir de consulta OQL do Castor	
	 */
	public QueryResults getResultSet()
	{
		return this.result;
	}
	
	/**
	 *	Atribui o parametro ao ResultSet encapsulado
	 * 
	 *	@param		QueryResults		result		ResultSet obtido a partir de consulta OQL do Castor	
	 */
	public void setResultSet(QueryResults result)
	{
		this.result = result; 
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		boolean						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public boolean getBoolean(String campo) throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return Boolean.valueOf(String.valueOf(result)).booleanValue();
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		char						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public char getChar(String campo) throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return String.valueOf(result).charAt(0);
			}
			else
			{
				return 0;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		Date						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public Date getDate(String campo)	throws RelatorioException
	{
		try
		{
			SimpleDateFormat conversorData = new SimpleDateFormat();
			Object result = this.invoke(campo);
			if(result != null)
			{
				return conversorData.parse(String.valueOf(result));
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		double						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public double getDouble(String campo)	throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return Double.valueOf(String.valueOf(result)).doubleValue();
			}
			else
			{
				return 0.0;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		float						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public float getFloat(String campo)	throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return Float.valueOf(String.valueOf(result)).floatValue();
			}
			else
			{
				return 0;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		int							Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public int getInt(String campo)	throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return Integer.valueOf(String.valueOf(result)).intValue();
			}
			else
			{
				return 0;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}

	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		long						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public long getLong(String campo)	throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return Long.valueOf(String.valueOf(result)).longValue();
			}
			else
			{
				return 0;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
		
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		String						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public String getString(String campo)	throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return String.valueOf(result);
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		Timestamp					Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public Timestamp getTimestamp(String campo)	throws RelatorioException
	{
		try
		{
			Object result = this.invoke(campo);
			if(result != null)
			{
				return Timestamp.valueOf(String.valueOf(result));
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao: " + e);
		}
	}
	
	/**
	 *	Move o cursor para o proximo registro
	 *
	 *	@return		boolean		True se ainda existia registros a serem percorridos ou false caso contrario
	 *	@throws		RelatorioException		
	 */
	public boolean next() throws RelatorioException
	{
		try
		{
			if(this.result.hasMore())
			{
				this.registro = this.result.next();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(PersistenceException e)
		{
			throw new RelatorioException("Excecao de Persistencia do Castor: " + e);
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
			this.result.close();
		}
		catch(Exception e)
		{
			throw new RelatorioException("Excecao : " + e);
		}
	}
	
	/**
	 *	Invoca o metodo get para retornar o campo passado por parametro
	 *
	 *	@param		String					campo					Nome do campo a ser retornado
	 *	@return		String											Campo a ser retornado com metodo get
	 *	@throws		RelatorioException		
	 */
	private Object invoke(String campo) throws RelatorioException
	{
		//Object result = null;
		
		if(campo != null)
		{
			try
			{
				//Obtendo o nome do metodo get. Nao deve possuir parametro.				
				String nomeMetodo = "get" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
				Method getCampo = this.registro.getClass().getMethod(nomeMetodo, null);
				
				return getCampo.invoke(this.registro, null);
			}
			catch(IllegalAccessException e)
			{
				throw new RelatorioException("Excecao de Acesso Ilegal: " + e);
			}
			catch(InvocationTargetException e)
			{
				throw new RelatorioException("Excecao de Invocacao: " + e);
			}
			catch(NoSuchMethodException e)
			{
				throw new RelatorioException("Metodo nao encontrado: " + e);
			}
		}
		else
		{
			throw new RelatorioException("Campo nao informado");
		}
	}
		
}
