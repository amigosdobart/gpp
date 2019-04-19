package com.brt.gpp.comum.mapeamentos.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_GER_CONFIGURACAO_GPP.
 * 
 *	@author	Daniel Ferreira
 *	@since	23/08/2005
 */
public class ConfiguracaoGPP implements Entidade
{

	private String	idConfiguracao;
	private String	vlrConfiguracao;
	private String	desConfiguracao;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public ConfiguracaoGPP()
	{
		this.idConfiguracao		= null;
		this.vlrConfiguracao	= null;
		this.desConfiguracao	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador da configuracao.
	 * 
	 *	@return		String					idConfiguracao				Identificador da configuracao.
	 */
	public String getIdConfiguracao() 
	{
		return this.idConfiguracao;
	}
	
	/**
	 *	Retorna o valor da configuracao.
	 * 
	 *	@return		String					vlrConfiguracao				Valor da configuracao.
	 */
	public String getVlrConfiguracao() 
	{
		return this.vlrConfiguracao;
	}
	
	/**
	 *	Retorna a descricao da configuracao.
	 * 
	 *	@return		String					desConfiguracao				Descricao da configuracao.
	 */
	public String getDesConfiguracao() 
	{
		return this.desConfiguracao;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador da configuracao.
	 * 
	 *	@param		String					idConfiguracao				Identificador da configuracao.
	 */
	public void setIdConfiguracao(String idConfiguracao) 
	{
		this.idConfiguracao = idConfiguracao;
	}
	
	/**
	 *	Atribui o valor da configuracao.
	 * 
	 *	@param		String					vlrConfiguracao				Valor da configuracao.
	 */
	public void setVlrConfiguracao(String vlrConfiguracao) 
	{
		this.vlrConfiguracao = vlrConfiguracao;
	}
	
	/**
	 *	Atribui a descricao da configuracao.
	 * 
	 *	@param		String					desConfiguracao				Descricao da configuracao.
	 */
	public void setDesConfiguracao(String desConfiguracao) 
	{
		this.desConfiguracao = desConfiguracao;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		ConfiguracaoGPP result = new ConfiguracaoGPP();	
		
		result.setIdConfiguracao((this.idConfiguracao != null) ? new String(this.idConfiguracao) : null);
		result.setVlrConfiguracao((this.vlrConfiguracao != null) ? new String(this.vlrConfiguracao) : null);
		result.setDesConfiguracao((this.desConfiguracao != null) ? new String(this.desConfiguracao) : null);
		
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
		
		if(!(object instanceof ConfiguracaoGPP))
		{
			return false;
		}
		
		if(this.hashCode() != ((ConfiguracaoGPP)object).hashCode())
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
		result.append((this.idConfiguracao != null) ? this.idConfiguracao : "NULL");
		
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
	
		result.append("Configuracao: ");
		result.append((this.idConfiguracao != null) ? this.idConfiguracao : "NULL");
		result.append(" - ");
		result.append("Valor: ");
		result.append((this.vlrConfiguracao != null) ? this.vlrConfiguracao : "NULL");
		
		return result.toString();
	}
	
}
