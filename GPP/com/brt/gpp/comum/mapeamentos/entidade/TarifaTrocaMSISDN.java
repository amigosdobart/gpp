package com.brt.gpp.comum.mapeamentos.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_APR_TARIFA_TROCA_MSISDN.
 * 
 *	@author	Daniel Ferreira
 *	@since	29/03/2006
 */
public class TarifaTrocaMSISDN implements Entidade
{

	private Integer	idTarifa;
	private String	desTarifa;
	private String	tipTransacao;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public TarifaTrocaMSISDN()
	{
		this.idTarifa		= null;
		this.desTarifa		= null;
		this.tipTransacao	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador da tarifa.
	 * 
	 *	@return		Integer					idTarifa					Identificador da tarifa.
	 */
	public Integer getIdTarifa() 
	{
		return this.idTarifa;
	}
	
	/**
	 *	Retorna a descricao da tarifa.
	 * 
	 *	@return		String					desTarifa					Descricao da tarifa.
	 */
	public String getDesTarifa() 
	{
		return this.desTarifa;
	}
	
	/**
	 *	Retorna o tipo de transacao associado a tarifa.
	 * 
	 *	@return		String					tipTransacao				Tipo de transacao associado a tarifa.
	 */
	public String getTipTransacao() 
	{
		return this.tipTransacao;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador da tarifa.
	 * 
	 *	@param		Integer					idTarifa					Identificador da tarifa.
	 */
	public void setIdTarifa(Integer idTarifa) 
	{
		this.idTarifa = idTarifa;
	}
	
	/**
	 *	Atribui a descricao da tarifa.
	 * 
	 *	@param		String					desTarifa					Descricao da tarifa.
	 */
	public void setDesTarifa(String desTarifa) 
	{
		this.desTarifa = desTarifa;
	}
	
	/**
	 *	Atribui o tipo de transacao associado a tarifa.
	 * 
	 *	@param		String					tipTransacao				Tipo de transacao associado a tarifa.
	 */
	public void setTipTransacao(String tipTransacao) 
	{
		this.tipTransacao = tipTransacao;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		TarifaTrocaMSISDN result = new TarifaTrocaMSISDN();	
		
		result.setIdTarifa((this.idTarifa != null) ? new Integer(this.idTarifa.intValue()) : null);
		result.setDesTarifa(this.desTarifa);
		result.setTipTransacao(this.tipTransacao);
		
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
		
		if(!(object instanceof TarifaTrocaMSISDN))
		{
			return false;
		}
		
		if(this.hashCode() != ((TarifaTrocaMSISDN)object).hashCode())
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
		result.append((this.idTarifa != null) ? String.valueOf(this.idTarifa.intValue()) : "NULL");
		
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
	
		result.append("Tarifa: ");
		result.append((this.desTarifa != null) ? this.desTarifa : "NULL");
		result.append(" - ");
		result.append("Tipo de Transacao: ");
		result.append((this.tipTransacao != null) ? this.tipTransacao : "NULL");
		
		return result.toString();
	}
	
}
