package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_CATEGORIA. Cada promocao pertence a uma categoria. Por exemplo,
 *	apesar de cada promocao Pula-Pula ter caracteristicas proprias (limite de bonus, necessidade de recarga para 
 *	entrar em promocao, periodo de concessao), todas pertencem a categoria Pula-Pula.
 * 
 *	@author	Daniel Ferreira
 *	@since	08/08/2005
 */
public class PromocaoCategoria implements Entidade
{
    
    //Atributos.
    
	/**
	 *	Identificador da categoria da promocao.
	 */
	private	int idtCategoria;
	
	/**
	 *	Nome da categoria da promocao.
	 */
	private	String nomCategoria;
	
	/**
	 *	Indicador de cadastro exclusivo. Caso o indicador esteja setado, significa que o assinante podera possuir 
	 *	somente uma promocao da categoria. Caso contrario, podera ter mais de uma promocao.
	 */
	private int indCadAssinanteExclusivo;
	
	//Construtores.
	
	/**
	 * Construtor da classe.
	 */
	public PromocaoCategoria()
	{
		this.idtCategoria				= -1;
		this.nomCategoria				= null;
		this.indCadAssinanteExclusivo	= -1;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador da categoria de promocao.
	 * 
	 *	@return								Identificador da categoria de promocao.
	 */
	public int getIdtCategoria() 
	{
		return this.idtCategoria;
	}
	
	/**
	 *	Retorna o nome da categoria.
	 * 
	 *	@return								Nome da categoria.
	 */
	public String getNomCategoria() 
	{
		return this.nomCategoria;
	}

	/**
	 *	Retorna o indicador de cadastro exclusivo do assinante em promocoes da categoria. Se este flag estiver 
	 *	setado para 1, significa que o assinante podera possuir no maximo uma promocao da mesma categoria.
	 * 
	 *	@return								Indicador de cadastro exclusivo.
	 */
	public int getIndCadAssinanteExclusivo() 
	{
		return this.indCadAssinanteExclusivo;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador da categoria.
	 * 
	 *	@param		idtCategoria			Identificador da categoria.
	 */
	public void setIdtCategoria(int idtCategoria) 
	{
		this.idtCategoria = idtCategoria;
	}
	
	/**
	 *	Atribui o nome da categoria.
	 * 
	 *	@param		nomCategoria			Nome da categoria.
	 */
	public void setNomCategoria(String nomCategoria) 
	{
		this.nomCategoria = nomCategoria;
	}
	
	/**
	 *	Atribui o indicador de cadastro exclusivo do assinante em promocoes da categoria. Se o indicador estiver 
	 *	setado para 1, significa que o assinante podera possuir no maximo uma promocao da mesma categoria.
	 * 
	 *	@param		indCadAssinanteExclusivo Indicador de cadastro exclusivo.
	 */
	public void setIndCadAssinanteExclusivo(int indCadAssinanteExclusivo) 
	{
		this.indCadAssinanteExclusivo = indCadAssinanteExclusivo;
	}
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoCategoria result = new PromocaoCategoria();		
		
		result.setIdtCategoria(this.idtCategoria);
		result.setNomCategoria(this.nomCategoria);
		result.setIndCadAssinanteExclusivo(this.indCadAssinanteExclusivo);
		
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
		
		if(!this.getClass().isInstance(object))
		{
			return false;
		}
		
		if(this.hashCode() != object.hashCode())
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
		result.append(String.valueOf(this.idtCategoria));
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		return "Categoria: " + ((this.nomCategoria != null) ? this.nomCategoria : "NULL");
	}
	
	//Outros metodos.
	
	/**
	 *	Retorna se o cadastro do assinante em promocoes da categoria é exclusivo. Em caso positivo, significa que 
	 *	o assinante podera possuir no maximo uma promocao da mesma categoria.
	 * 
	 *	@return		boolean												True se o cadastro e excluivo e 
	 *																	false caso contrario.
	 */
	public boolean cadastroExclusivo()
	{
		return (this.indCadAssinanteExclusivo > 0);
	}
	
}
