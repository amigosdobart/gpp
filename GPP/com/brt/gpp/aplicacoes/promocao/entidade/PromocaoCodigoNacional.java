package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports GPP.

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_CODIGO_NACIONAL, que mapeia os codigos nacionais de assinantes
 *	que podem ser cadastrados na promocao.
 * 
 *	@author	Daniel Ferreira
 *	@since	09/08/2005
 */
public class PromocaoCodigoNacional implements Entidade
{

	private	Integer	idtPromocao;
	private Integer	idtCodigoNacional;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public PromocaoCodigoNacional()
	{
		this.idtPromocao = null;
		this.idtCodigoNacional = null;
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
	 *	Retorna o Codigo Nacional do assinante.
	 * 
	 *	@return		Integer					idtCodigoNacional			Codigo Nacional do assinante.
	 */
	public Integer getIdtCodigoNacional() 
	{
		return this.idtCodigoNacional;
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
	 *	Atribui o Codigo Nacional do assinante.
	 * 
	 *	@param		Integer					idtCodigoNacional			Codigo Nacional do assinante.
	 */
	public void setIdtCodigoNacional(Integer idtCodigoNacional) 
	{
		this.idtCodigoNacional = idtCodigoNacional;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoCodigoNacional result = new PromocaoCodigoNacional();	
		
		result.setIdtPromocao((this.idtPromocao != null) ? new Integer(this.idtPromocao.intValue()) : null);
		result.setIdtCodigoNacional((this.idtCodigoNacional != null) ? new Integer(this.idtCodigoNacional.intValue()) : null);
		
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
		
		if(!(object instanceof PromocaoCodigoNacional))
		{
			return false;
		}
		
		if(this.hashCode() != ((PromocaoCodigoNacional)object).hashCode())
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
		result.append((this.idtCodigoNacional != null) ? String.valueOf(this.idtCodigoNacional.intValue()) : "NULL");
		
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
		result.append((this.idtPromocao != null) ? String.valueOf(this.idtPromocao.intValue()) : "NULL");
		result.append(" - ");
		result.append("Codigo Nacional: ");
		result.append((this.idtCodigoNacional != null) ? String.valueOf(this.idtCodigoNacional.intValue()) : "NULL");
		
		return result.toString();
	}
	
}
