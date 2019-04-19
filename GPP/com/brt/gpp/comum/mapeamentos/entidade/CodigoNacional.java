package com.brt.gpp.comum.mapeamentos.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_GER_CODIGO_NACIONAL.
 * 
 *	@author	Daniel Ferreira
 *	@since	23/08/2005
 *
 *  @author Bernardo Vergne Dias
 *  Desde: 22/02/2007
 */
public class CodigoNacional implements Entidade
{

	/**
	 *	Codigo Nacional.
	 */
	private Integer	idtCodigoNacional;
	
	/**
	 *	Identificador da filial.
	 */
	private String	idtFilial;
	
	/**
	 *	Identificador da regiao da Anatel.
	 */
	private String	idtRegiaoAnatel;
	
	/**
	 *	Indicador de regiao da Brasil Telecom.
	 */
	private Integer	indRegiaoBrt;
	
	/**
	 *	Identificador do fuso horario do Codigo Nacional.
	 */
	private Integer	idtFuso;

	/**
	 *	Unidade da Federacao.
	 */
	private String idtUf;

	/**
	 *	Identificador da regiao de origem.
	 */
	private Integer idtRegiaoOrigem;
	
	/**
	 * Construtor da classe
	 */
	public CodigoNacional()
	{
		this.idtCodigoNacional	= null;
		this.idtFilial			= null;
		this.indRegiaoBrt		= null;
		this.idtFuso			= null;
		this.idtUf				= null;
		this.idtRegiaoOrigem	= null;
		this.idtRegiaoAnatel	= null;
	}
	
	/**
	 *	Retorna o Codigo Nacional.
	 * 
	 *	@return		Codigo Nacional.
	 */
	public Integer getIdtCodigoNacional() 
	{
		return this.idtCodigoNacional;
	}
	
	/**
	 *	Retorna a Unidade da Federacao.
	 * 
	 *	@return		Unidade da Federacao.
	 */
	public String getIdtUf() 
	{
		return this.idtUf;
	}
	
	/**
	 *	Retorna o identificador da filial.
	 * 
	 *	@return		Identificador da filial.
	 */
	public String getIdtFilial() 
	{
		return this.idtFilial;
	}
	
	/**
	 *	Retorna o indicador de regiao da BrT.
	 * 
	 *	@return		Indicador de regiao da BrT.
	 */
	public Integer getIndRegiaoBrt() 
	{
		return this.indRegiaoBrt;
	}
	
	/**
	 *	Retorna o identificador do fuso horario do codigo nacional.
	 * 
	 *	@return		Identificador do fuso horario.
	 */
	public Integer getIdtFuso() 
	{
		return this.idtFuso;
	}
	
	/**
	 *	Retorna o identificador da regiao de origem.
	 *
	 *	@return		Identificador da regiao de origem.
	 */
	public Integer getIdtRegiaoOrigem()
	{
		return this.idtRegiaoOrigem;
	}
	
	/**
	 *	Retorna o identificador da regiao da Anatel.
	 *
	 *	@return		Identificador da regiao da Anatel.
	 */
	public String getIdtRegiaoAnatel()
	{
		return this.idtRegiaoAnatel;
	}
	
	/**
	 *	Atribui o Codigo Nacional.
	 * 
	 *	@param		idtCodigoNacional		Codigo Nacional.
	 */
	public void setIdtCodigoNacional(Integer idtCodigoNacional) 
	{
		this.idtCodigoNacional = idtCodigoNacional;
	}
	
	/**
	 *	Atribui o identificador da Unidade da Federacao.
	 * 
	 *	@param		idtUf					Identificador da UF.
	 */
	public void setIdtUf(String idtUf) 
	{
		this.idtUf = idtUf;
	}
	
	/**
	 *	Atribui o identificador da filial.
	 * 
	 *	@param		idtFilial				Identificador da filial.
	 */
	public void setIdtFilial(String idtFilial) 
	{
		this.idtFilial = idtFilial;
	}
	
	/**
	 *	Atribui o identificador da regiao da Anatel.
	 *
	 *	@param		idtRegiaoAnatel			Identificador da regiao da Anatel.
	 */
	public void setIdtRegiaoAnatel(String idtRegiaoAnatel)
	{
		this.idtRegiaoAnatel = idtRegiaoAnatel;
	}
	
	/**
	 *	Atribui o indicador de regiao da BrT.
	 * 
	 *	@param		indRegiaoBrt			Indicador de regiao da BrT.
	 */
	public void setIndRegiaoBrt(Integer indRegiaoBrt) 
	{
		this.indRegiaoBrt = indRegiaoBrt;
	}
	
	/**
	 *	Atribui o identificador do fuso horario do codigo nacional.
	 * 
	 *	@param		idtFuso					Identificador do fuso horario.
	 */
	public void setIdtFuso(Integer idtFuso) 
	{
		this.idtFuso = idtFuso;
	}
			
	/**
	 *	Atribui o identificador da regiao de origem.
	 *
	 *	@param		Identificador da regiao de origem.
	 */
	public void setIdtRegiaoOrigem(Integer idtRegiaoOrigem)
	{
		this.idtRegiaoOrigem = idtRegiaoOrigem;
	}
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Copia do objeto.
	 */
	public Object clone()
	{
		CodigoNacional result = new CodigoNacional();	
		result.setIdtCodigoNacional((this.idtCodigoNacional != null) ? new Integer(this.idtCodigoNacional.intValue()) : null);
		result.setIdtFilial(this.idtFilial);
		result.setIdtRegiaoAnatel(this.idtRegiaoAnatel);
		result.setIndRegiaoBrt((this.indRegiaoBrt != null) ? new Integer(this.indRegiaoBrt.intValue()) : null);
		result.setIdtFuso((this.idtFuso != null) ? new Integer(this.idtFuso.intValue()) : null);
		result.setIdtRegiaoOrigem(this.idtRegiaoOrigem);
		result.setIdtUf(this.idtUf);

		return result;
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		object					Objeto a ser comparado com o atual.
	 *	@return		True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof CodigoNacional))
		{
			return false;
		}
		
		if(this.hashCode() != ((CodigoNacional)object).hashCode())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append((this.idtCodigoNacional != null) ? String.valueOf(this.idtCodigoNacional.intValue()) : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("Codigo Nacional: ");
		result.append((this.idtCodigoNacional != null) ? String.valueOf(this.idtCodigoNacional.intValue()) : "NULL");
		
		return result.toString();
	}
		
	/**
	 *	Retorna se o codigo nacional e regiao da BrT.
	 * 
	 *	@return		True se for regiao da BrT e false caso contrario.
	 */
	public boolean regiaoBrt() 
	{
		return !((this.indRegiaoBrt == null) || (this.indRegiaoBrt.intValue() == 0));
	}

}
