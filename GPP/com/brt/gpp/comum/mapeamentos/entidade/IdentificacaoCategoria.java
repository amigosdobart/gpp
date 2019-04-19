package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>IdentificacaoCategoria</code>. Referência: TBL_TAR_IDENTIFICACAO_CATEGORIA
 *  
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class IdentificacaoCategoria implements Serializable
{
	private RegiaoOrigem regiaoOrigem;
	private RegiaoDestino regiaoDestino;
	private RegiaoLAC regiaoLAC;
	private CategoriaChamada categoriaChamada;
	
	/**
	 * @return Instancia de <code>CategoriaChamada</code>
	 */
	public CategoriaChamada getCategoriaChamada() 
	{
		return categoriaChamada;
	}

	/**
	 * @param categoriaChamada Instancia de <code>CategoriaChamada</code>
	 */
	public void setCategoriaChamada(CategoriaChamada categoriaChamada) 
	{
		this.categoriaChamada = categoriaChamada;
	}

	/**
	 * @return Instancia de <code>RegiaoDestino</code>
	 */
	public RegiaoDestino getRegiaoDestino() 
	{
		return regiaoDestino;
	}

	/**
	 * @param regiaoDestino Instancia de <code>RegiaoDestino</code>
	 */
	public void setRegiaoDestino(RegiaoDestino regiaoDestino) 
	{
		this.regiaoDestino = regiaoDestino;
	}

	/**
	 * @return Instancia de <code>RegiaoLAC</code>
	 */
	public RegiaoLAC getRegiaoLAC() 
	{
		return regiaoLAC;
	}

	/**
	 * @param regiaoLAC Instancia de <code>RegiaoLAC</code>
	 */
	public void setRegiaoLAC(RegiaoLAC regiaoLAC) 
	{
		this.regiaoLAC = regiaoLAC;
	}

	/**
	 * @return Instancia de <code>RegiaoOrigem</code>
	 */
	public RegiaoOrigem getRegiaoOrigem() 
	{
		return regiaoOrigem;
	}

	/**
	 * @param regiaoOrigem Instancia de <code>RegiaoOrigem</code>
	 */
	public void setRegiaoOrigem(RegiaoOrigem regiaoOrigem) 
	{
		this.regiaoOrigem = regiaoOrigem;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof IdentificacaoCategoria))
			return false;
		
		if (obj == this)
			return true;

		boolean equal = true;	
		equal &= isEqual(this.regiaoOrigem, 	((IdentificacaoCategoria)obj).getRegiaoOrigem());
		equal &= isEqual(this.regiaoDestino, 	((IdentificacaoCategoria)obj).getRegiaoDestino());
		equal &= isEqual(this.regiaoLAC, 		((IdentificacaoCategoria)obj).getRegiaoLAC());
		
		return equal;
	}
	
	private boolean isEqual(Object obj1, Object obj2)
	{
		if (obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		if (obj1 == null && obj2 == null)
			return true;
		return false;
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		if (this.regiaoOrigem != null) 	result.append(this.regiaoOrigem.getIdRegiaoOrigem());
		if (this.regiaoLAC != null) 	result.append(this.regiaoLAC.getIdRegiaoLAC());
		if (this.regiaoDestino != null) result.append(this.regiaoDestino.getIdRegiaoDestino());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[IdentificacaoCategoria]");
		if (regiaoOrigem != null)		result.append("ID_REGIAO_ORIGEM=" + this.regiaoOrigem.getIdRegiaoOrigem());
		if (regiaoDestino != null)		result.append(";ID_REGIAO_DESTINO=" + this.regiaoDestino.getIdRegiaoDestino());
		if (regiaoLAC != null)			result.append(";ID_REGIAO_LAC=" + this.regiaoLAC.getIdRegiaoLAC());
		if (categoriaChamada != null)	result.append(";ID_CATEGORIA_CHAMADA=" + this.categoriaChamada.getIdCategoriaChamada());
		
		return result.toString();
	}

}
