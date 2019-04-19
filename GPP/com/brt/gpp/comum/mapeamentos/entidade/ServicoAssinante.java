package com.brt.gpp.comum.mapeamentos.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_APR_SERVICOS.
 * 
 *	@author	Daniel Ferreira
 *	@since	29/03/2006
 */
public class ServicoAssinante implements Entidade
{

	private String	idServico;
	private String	desServico;
	private String	indServicoExclusivo;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public ServicoAssinante()
	{
		this.idServico				= null;
		this.desServico				= null;
		this.indServicoExclusivo	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador do servico.
	 * 
	 *	@return		String					idServico					Identificador do servico.
	 */
	public String getIdServico() 
	{
		return this.idServico;
	}
	
	/**
	 *	Retorna a descricao do servico.
	 * 
	 *	@return		String					desServico					Descricao do servico.
	 */
	public String getDesServico() 
	{
		return this.desServico;
	}
	
	/**
	 *	Retorna o indicador de servico exclusivo.
	 * 
	 *	@return		String					indServicoExclusivo			Indicador de servico exclusivo.
	 */
	public String getIndServicoExclusivo() 
	{
		return this.indServicoExclusivo;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador do servico.
	 * 
	 *	@param		String					idServico					Identificador do servico.
	 */
	public void setIdServico(String idServico) 
	{
		this.idServico = idServico;
	}
	
	/**
	 *	Atribui a descricao do servico.
	 * 
	 *	@param		String					desServico					Descricao do servico.
	 */
	public void setDesServico(String desServico) 
	{
		this.desServico = desServico;
	}
	
	/**
	 *	Atribui o indicador de servico exclusivo.
	 * 
	 *	@param		String					indServicoExclusivo			Indicador de servico exclusivo.
	 */
	public void setIndServicoExclusivo(String indServicoExclusivo) 
	{
		this.indServicoExclusivo = indServicoExclusivo;
	}
	
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		ServicoAssinante result = new ServicoAssinante();	

		result.setIdServico(this.idServico);
		result.setDesServico(this.desServico);
		result.setIndServicoExclusivo(this.indServicoExclusivo);
		
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
		
		if(!(object instanceof ServicoAssinante))
		{
			return false;
		}
		
		if(this.hashCode() != ((ServicoAssinante)object).hashCode())
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
		result.append((this.idServico != null) ? this.idServico : "NULL");
		
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
	
		result.append("Servico: ");
		result.append((this.desServico != null) ? this.desServico : "NULL");
		result.append(" - ");
		result.append("Exclusivo: ");
		result.append((this.indServicoExclusivo != null) ? this.indServicoExclusivo : "NULL");
		
		return result.toString();
	}
	
	//Outros metodos.
	
	/**
	 *	Indica se a configuracao corresponde a plano hibrido.
	 * 
	 *	@return		boolean												True se corresponde a plano hibrido e false caso contrario.
	 */
	public boolean exclusivo()
	{
	    return ((this.indServicoExclusivo != null) && (this.indServicoExclusivo.equalsIgnoreCase("S")));
	}
	
}
