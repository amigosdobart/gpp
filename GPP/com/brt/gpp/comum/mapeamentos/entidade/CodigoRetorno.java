package com.brt.gpp.comum.mapeamentos.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_GER_CODIGO_COBRANCA.
 * 
 *	@author	Marcelo Alves Araujo
 *	@since	03/08/2006
 */
public class CodigoRetorno implements Entidade
{
	private String	idRetorno;
	private String	valorRetorno;
	private String	descRetorno;
		
	public String getDescRetorno() 
	{
		return descRetorno;
	}
	public String getIdRetorno() 
	{
		return idRetorno;
	}
	public String getValorRetorno() 
	{
		return valorRetorno;
	}

	public void setDescRetorno(String descRetorno) 
	{
		this.descRetorno = descRetorno;
	}
	public void setIdRetorno(String idRetorno) 
	{
		this.idRetorno = idRetorno;
	}
	public void setValorRetorno(String valorRetorno) 
	{
		this.valorRetorno = valorRetorno;
	}

	//Implentacao de Entidade.
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		CodigoRetorno result = new CodigoRetorno();	
		
		result.setDescRetorno(descRetorno);
		result.setIdRetorno(idRetorno);
		result.setValorRetorno(valorRetorno);
		
		return result;
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		Object					object						Objeto a ser comparado com o atual.
	 *	@return		boolean												True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof CodigoRetorno))
		{
			return false;
		}
		
		if(this.hashCode() != ((CodigoRetorno)object).hashCode())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		int													Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append((valorRetorno != null) ? valorRetorno : "NULL");
				
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("ID Retorno: ");
		result.append((idRetorno != null) ? idRetorno : "NULL");
		result.append(" - Codigo Retorno: ");
		result.append((valorRetorno != null) ? valorRetorno : "NULL");
		result.append(" - Descricao Retorno: ");
		result.append((descRetorno != null) ? descRetorno : "NULL");
				
		return result.toString();
	}
}
