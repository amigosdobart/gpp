package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_ORIGEM_ESTORNO.
 * 
 *	@author	Daniel Ferreira
 *	@since	03/03/2006
 */
public class PromocaoOrigemEstorno implements Entidade
{

	private String	idtOrigem;
	private String	desOrigem;
	private String	tipAnalise;
	
	//Constantes internas.
	
	public static final String	TIP_ANALISE_DIARIO	= "DIARIO";
	public static final String	TIP_ANALISE_MENSAL	= "MENSAL";
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public PromocaoOrigemEstorno()
	{
		this.idtOrigem	= null;
		this.desOrigem	= null;
		this.tipAnalise	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador da origem do estorno.
	 * 
	 *	@return		String					idtOrigem					Identificador da origem do estorno.
	 */
	public String getIdtOrigem() 
	{
		return this.idtOrigem;
	}
	
	/**
	 *	Retorna a descricao da origem do estorno.
	 * 
	 *	@return		String					desOrigem					Descricao da origem do estorno.
	 */
	public String getDesOrigem() 
	{
		return this.desOrigem;
	}
	
	/**
	 *	Retorna o tipo de calculo para o periodo de analise.
	 * 
	 *	@return		String					tipAnalise					Tipo de analise.
	 */
	public String getTipAnalise() 
	{
		return this.tipAnalise;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador da origem do estorno.
	 * 
	 *	@param		String					idtOrigem					Identificador da origem do estorno.
	 */
	public void setIdtOrigem(String idtOrigem) 
	{
		this.idtOrigem = idtOrigem;
	}
	
	/**
	 *	Atribui a descricao da origem do estorno.
	 * 
	 *	@param		String					desOrigem					Descricao da origem do estorno.
	 */
	public void setDesOrigem(String desOrigem) 
	{
		this.desOrigem = desOrigem;
	}
	
	/**
	 *	Atribui o tipo de calculo para o periodo de analise.
	 * 
	 *	@param		String					tipAnalise					Tipo de analise.
	 */
	public void setTipAnalise(String tipAnalise) 
	{
		this.tipAnalise = tipAnalise;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoOrigemEstorno result = new PromocaoOrigemEstorno();	
		
		result.setIdtOrigem(this.idtOrigem);
		result.setDesOrigem(this.desOrigem);
		result.setTipAnalise(this.tipAnalise);
		
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
		
		if(!(object instanceof PromocaoOrigemEstorno))
		{
			return false;
		}
		
		if(this.hashCode() != ((PromocaoOrigemEstorno)object).hashCode())
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
		result.append((this.idtOrigem != null) ? this.idtOrigem : "NULL");
		
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
	
		result.append("Origem: ");
		result.append((this.desOrigem != null) ? this.desOrigem : "NULL");
		
		return result.toString();
	}
	
}
