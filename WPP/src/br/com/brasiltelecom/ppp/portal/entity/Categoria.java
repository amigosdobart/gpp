
package br.com.brasiltelecom.ppp.portal.entity;
import java.util.*;
/**
 * Modela a tabela de categoria
 * @author 
 */
public class Categoria {
	private String idCategoria;
	private String desCategoria;
	private String desMascaraMsisdn;
    private Collection planos;
    
	/**
	 * @return Returns the desCategoria.
	 */
	public String getDesCategoria() 
	{
		return desCategoria;
	}
	/**
	 * @param desCategoria The desCategoria to set.
	 */
	public void setDesCategoria(String desCategoria) 
	{
		this.desCategoria = desCategoria;
	}
	/**
	 * @return Returns the idCategoria.
	 */
	public String getIdCategoria() 
	{
		return idCategoria;
	}
	/**
	 * @param idCategoria The idCategoria to set.
	 */
	public void setIdCategoria(String idCategoria) 
	{
		this.idCategoria = idCategoria;
	}
	
	/**
	 * @return the desMascaraMsisdn
	 */
	public String getDesMascaraMsisdn()
	{
		return desMascaraMsisdn;
	}
	
	/**
	 * @param desMascaraMsisdn the desMascaraMsisdn to set
	 */
	public void setDesMascaraMsisdn(String desMascaraMsisdn)
	{
		this.desMascaraMsisdn = desMascaraMsisdn;
	}
	/**
	 * @return Returns the planos.
	 */
	public Collection getPlanos() {
		return planos;
	}
	/**
	 * @param planos The planos to set.
	 */
	public void setPlanos(Collection planos) {
		this.planos = planos;
	}
	
	public int hashCode()
	{
		return this.getIdCategoria().hashCode();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Categoria) )
			return false;
		
		if ( ((Categoria)obj).getIdCategoria().equals(this.getIdCategoria()) )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getDesCategoria();
	}

}
