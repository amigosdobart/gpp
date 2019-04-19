package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports GPP.

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_PLANO_PRECO, que mapeia os planos de preco de assinantes
 *	que podem ser cadastrados na promocao.
 * 
 *	@author	Daniel Ferreira
 *	@since	09/08/2005
 */
public class PromocaoPlanoPreco implements Entidade
{

	private	Integer	idtPromocao;
	private Integer	idtPlanoPreco;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public PromocaoPlanoPreco()
	{
		this.idtPromocao	= null;
		this.idtPlanoPreco	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador da promocao.
	 * 
	 *	@return		Integer					idtPromocao					Identificador da promocao.
	 */
	public Integer getIdtPromocao() 
	{
		return this.idtPromocao;
	}
	
	/**
	 *	Retorna o mapeamento da promocao de acordo com o plano de preco do assinante.
	 * 
	 *	@return		Integer					idtPlanoPreco				Mapeamento Promocao / Plano de preco.
	 */
	public Integer getIdtPlanoPreco() 
	{
		return this.idtPlanoPreco;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador da promocao.
	 * 
	 *	@param		Integer					idtPromocao					Identificador da promocao.
	 */
	public void setIdtPromocao(Integer idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
	
	/**
	 *	Atribui o mapeamento da promocao de acordo com o plano de preco do assinante. 
	 * 
	 *	@param		Integer					idtPlanoPreco				Mapeamento Promocao / Plano de Preco.
	 */
	public void setIdtPlanoPreco(Integer idtPlanoPreco) 
	{
		this.idtPlanoPreco = idtPlanoPreco;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoPlanoPreco result = new PromocaoPlanoPreco();		
		
		result.setIdtPromocao((this.idtPromocao != null) ? new Integer(this.idtPromocao.intValue()) : null);
		result.setIdtPlanoPreco((this.idtPlanoPreco != null) ? new Integer(this.idtPlanoPreco.intValue()) : null);
		
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
		
		if(!(object instanceof PromocaoPlanoPreco))
		{
			return false;
		}
		
		if(this.hashCode() != ((PromocaoPlanoPreco)object).hashCode())
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
		result.append((this.idtPromocao != null) ? String.valueOf(this.idtPromocao.intValue()) : "NULL");
		result.append("||");
		result.append((this.idtPlanoPreco != null) ? String.valueOf(this.idtPlanoPreco.intValue()) : "NULL");
		
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
	
		result.append("Promocao: ");
		result.append(String.valueOf(this.idtPromocao.intValue()));
		result.append(" - ");
		result.append("Plano de Preco: ");
		result.append(String.valueOf(this.idtPlanoPreco.intValue()));
		
		return result.toString();
	}
	
}
