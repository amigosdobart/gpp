package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_DESCARTA_ASSINANTE.
 * 
 *	@author	Daniel Ferreira
 *	@since	23/08/2005
 */
public class PromocaoDescartaAssinante implements Entidade
{

	private String	idtMsisdn;
	private Integer	indMascara;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public PromocaoDescartaAssinante()
	{
		this.idtMsisdn	= null;
		this.indMascara	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o MSISDN do assinante.
	 * 
	 *	@return		String					idtMsisdn					MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o indicador de mascara.
	 * 
	 *	@return		Integer					indMascara					Indicador de mascara.
	 */
	public Integer getIndMascara() 
	{
		return this.indMascara;
	}
	
	//Setters.
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		String					idtMsisdn					MSISDN do assinante.
	 */
	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	/**
	 *	Atribui o indicador de mascara.
	 * 
	 *	@param		Integer					indMascara					Indicador de mascara.
	 */
	public void setIndMascara(Integer indMascara) 
	{
		this.indMascara = indMascara;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoDescartaAssinante result = new PromocaoDescartaAssinante();	
		
		result.setIdtMsisdn(this.idtMsisdn);
		result.setIndMascara((this.indMascara != null) ? new Integer(this.indMascara.intValue()) : null);
		
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
		
		if(!(object instanceof PromocaoDescartaAssinante))
		{
			return false;
		}
		
		if(this.hashCode() != ((PromocaoDescartaAssinante)object).hashCode())
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
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		
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
	
		result.append("MSISDN: ");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		
		return result.toString();
	}
	
	//Outros metodos.
	
	/**
	 *	Retorna se o MSISDN representa uma mascara.
	 * 
	 *	@return		boolean												True se representa uma mascara e false caso contrario.
	 */
	public boolean mascara() 
	{
		return ((this.indMascara != null) && (this.indMascara.intValue() == 1));
	}
	
}
