package com.brt.gpp.aplicacoes.promocao.entidade;

import java.io.Serializable;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Classe que representa a entidade da tabela TBL_PRO_LIMITE_SEGUNDOS, contendo as 
 * informacoes referente aoS limiteS de segundos para envio de SMS informativo
 * 
 * @author João Paulo Galvagni
 * @since  31/05/2007
 */
public class PromocaoLimiteSegundos implements Entidade,Comparable,Serializable
{
	private int 	idLimite;
	private long 	numSegundos;
	private boolean isLimiteMaximo;
	
	private Promocao promocao;
	
	/**
	 * Construtor da Classe
	 *
	 */
	public PromocaoLimiteSegundos()
	{
		this.idLimite 	 	= -1;
		this.numSegundos 	= -1;
		this.isLimiteMaximo = false;
		this.promocao 		= null;
	}
	
	/**
	 * @return the idLimite
	 */
	public int getIdLimite()
	{
		return idLimite;
	}
	
	/**
	 * @param idLimite the idLimite to set
	 */
	public void setIdLimite(int idLimite)
	{
		this.idLimite = idLimite;
	}
	
	/**
	 * @return the isLimiteMaximo
	 */
	public boolean isLimiteMaximo()
	{
		return isLimiteMaximo;
	}
	
	/**
	 * @param isLimiteMaximo the isLimiteMaximo to set
	 */
	public void setLimiteMaximo(boolean isLimiteMaximo)
	{
		this.isLimiteMaximo = isLimiteMaximo;
	}
	
	/**
	 * @return the numSegundos
	 */
	public long getNumSegundos()
	{
		return numSegundos;
	}
	
	/**
	 * @param numSegundos the numSegundos to set
	 */
	public void setNumSegundos(long numSegundos)
	{
		this.numSegundos = numSegundos;
	}
	
	/**
	 * @return the promocao
	 */
	public Promocao getPromocao()
	{
		return promocao;
	}
	
	/**
	 * @param promocao the promocao to set
	 */
	public void setPromocao(Promocao promocao)
	{
		this.promocao = promocao;
	}
	
	/**
	 * Metodo....: clone
	 * Descricao.: Retorna um objeto identico ao atual
	 * 
	 * @return limiteSegundos - Objeto identico ao atual
	 */
	public Object clone()
	{
		PromocaoLimiteSegundos limiteSegundos = new PromocaoLimiteSegundos();
		
		limiteSegundos.setIdLimite	  (this.getIdLimite());
		limiteSegundos.setLimiteMaximo(this.isLimiteMaximo());
		limiteSegundos.setNumSegundos (this.getNumSegundos());
		limiteSegundos.setPromocao	  (this.getPromocao());
		
		return limiteSegundos;
	}
	
	/**
	 * Metodo....: equals
	 * Descricao.:Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 * @param  object  - Objeto a ser comparado com o atual
	 * @return boolean - true se for igual e false se for diferente
	 */
	public boolean equals(Object object)
	{
		if ( !(object instanceof PromocaoLimiteSegundos) )
			return false;
		
		PromocaoLimiteSegundos promLimite = (PromocaoLimiteSegundos)object;
		if ( this.getPromocao().equals(promLimite.getPromocao()) && this.getIdLimite() == promLimite.getIdLimite())
				return true;
		
		return false;
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof PromocaoLimiteSegundos))
			throw new ClassCastException();
		
		PromocaoLimiteSegundos promLimite = (PromocaoLimiteSegundos)obj;
		if ( this.getPromocao().equals(promLimite.getPromocao()) )
			return this.getIdLimite() - promLimite.getIdLimite();
		
		return this.getPromocao().getIdtPromocao() - promLimite.getPromocao().getIdtPromocao();
	}
	
	/**
	 * Metodo....: hashCode
	 * Descricao.: Retorna o hash do objeto
	 * 
	 *	@return result - hash do objeto
	 */
	public int hashCode()
	{
		return (String.valueOf(this.getPromocao().getIdtPromocao()) + this.getIdLimite()).hashCode();
	}
	
	/**
	 * Metodo....: toString
	 * Descricao.: Representacao em formato de String do objeto
	 * 
	 * @return result - Objeto descrito em String
	 */
	public String toString()
	{
		return this.getPromocao().toString() + " Limite:"+this.getNumSegundos();
	}
}