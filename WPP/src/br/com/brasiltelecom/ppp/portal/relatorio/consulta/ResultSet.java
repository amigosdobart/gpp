package br.com.brasiltelecom.ppp.portal.relatorio.consulta;

//Imports Java
import java.sql.Timestamp;
import java.util.Date;

//Imports do GPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;

/**
 *	Interface que abstrai as operacoes de result sets gerados pelas consultas 
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public interface ResultSet 
{
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		boolean						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public boolean getBoolean(String campo)	throws RelatorioException;
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		char							Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public char getChar(String campo)	throws RelatorioException;
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		int							Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public Date getDate(String campo)	throws RelatorioException;
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		double						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public double getDouble(String campo)	throws RelatorioException;
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		float						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public float getFloat(String campo)	throws RelatorioException;
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		int							Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public int getInt(String campo)	throws RelatorioException;

	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		long						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public long getLong(String campo)	throws RelatorioException;
		
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		String						Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public String getString(String campo)	throws RelatorioException;
	
	/**
	 *	Retorna o valor do campo passado por parametro
	 * 
	 *	@param		String		campo			Campo de onde se obter o valor	
	 *	@return		Timestamp					Valor obtido do campo
	 *	@throws		RelatorioException		
	 */
	public Timestamp getTimestamp(String campo)	throws RelatorioException;
	
	/**
	 *	Move o cursor para o proximo registro
	 *
	 *	@return		boolean		True se ainda existia registros a serem percorridos ou false caso contrario
	 *	@throws		RelatorioException		
	 */
	public boolean next()	throws RelatorioException;
	
	/**
	 *	Fecha o ResultSet
	 *
	 *	@throws		RelatorioException		
	 */
	public void close()	throws RelatorioException;
		
}
