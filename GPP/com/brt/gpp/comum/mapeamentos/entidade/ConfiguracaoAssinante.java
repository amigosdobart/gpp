package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_GER_CONFIG_ASSINANTE.
 * 
 *	@author	Daniel Ferreira
 *	@since	29/03/2006
 */
public class ConfiguracaoAssinante implements Entidade, Serializable
{

    private	String	tipConfiguracao;
	private Integer	idConfiguracao;
	private String	desConfiguracao;
	private Integer	indHibrido;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public ConfiguracaoAssinante()
	{
	    this.tipConfiguracao	= null;
		this.idConfiguracao		= null;
		this.desConfiguracao	= null;
		this.indHibrido			= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o tipo de configuracao.
	 * 
	 *	@return		String					tipConfiguracao				Tipo de configuracao.
	 */
	public String getTipConfiguracao() 
	{
		return this.tipConfiguracao;
	}
	
	/**
	 *	Retorna o identificador da configuracao.
	 * 
	 *	@return		Integer					idConfiguracao				Identificador da configuracao.
	 */
	public Integer getIdConfiguracao() 
	{
		return this.idConfiguracao;
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
	
	/**
	 *	Retorna o indicador de plano hibrido.
	 * 
	 *	@return		Integer					indHibrido					Indicador de plano hibrido.
	 */
	public Integer getIndHibrido() 
	{
		return this.indHibrido;
	}
	
	//Setters.
	
	/**
	 *	Atribui o tipo de configuracao.
	 * 
	 *	@param		String					tipConfiguracao				Tipo de configuracao.
	 */
	public void setTipConfiguracao(String tipConfiguracao) 
	{
		this.tipConfiguracao = tipConfiguracao;
	}
	
	/**
	 *	Atribui o identificador da configuracao.
	 * 
	 *	@param		Integer					idConfiguracao				Identificador da configuracao.
	 */
	public void setIdConfiguracao(Integer idConfiguracao) 
	{
		this.idConfiguracao = idConfiguracao;
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
		
	/**
	 *	Atribui o indicador de plano hibrido.
	 * 
	 *	@param		Integer					indHibrido					Indicador de plano hibrido.
	 */
	public void setIndHibrido(Integer indHibrido) 
	{
		this.indHibrido = indHibrido;
	}
	
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		ConfiguracaoAssinante result = new ConfiguracaoAssinante();	

		result.setTipConfiguracao(this.tipConfiguracao);
		result.setIdConfiguracao((this.idConfiguracao != null) ? new Integer(this.idConfiguracao.intValue()) : null);
		result.setDesConfiguracao(this.desConfiguracao);
		result.setIndHibrido((this.indHibrido != null) ? new Integer(this.indHibrido.intValue()) : null);
		
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
		
		if(!(object instanceof ConfiguracaoAssinante))
		{
			return false;
		}
		
		if(this.hashCode() != ((ConfiguracaoAssinante)object).hashCode())
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
		result.append((this.tipConfiguracao != null) ? this.tipConfiguracao : "NULL");
		result.append("||");
		result.append((this.idConfiguracao != null) ? String.valueOf(this.idConfiguracao.intValue()) : "NULL");
		
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
		result.append((this.tipConfiguracao != null) ? this.tipConfiguracao : "NULL");
		result.append(" - ");
		result.append("Descricao: ");
		result.append((this.desConfiguracao != null) ? this.desConfiguracao : "NULL");
		
		return result.toString();
	}
	
	//Outros metodos.
	
	/**
	 *	Indica se a configuracao corresponde a plano hibrido.
	 * 
	 *	@return		boolean												True se corresponde a plano hibrido e false caso contrario.
	 */
	public boolean hibrido()
	{
	    return ((this.indHibrido != null) && (this.indHibrido.intValue() == 1));
	}
	
}
