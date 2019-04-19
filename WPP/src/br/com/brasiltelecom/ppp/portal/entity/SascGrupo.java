package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Classe que contem os atributos de Grupo do SASC 
 *  
 * @author JOAO PAULO GALVAGNI 
 * @since  06/02/2007 
 *
 */
public class SascGrupo implements Comparable
{
	private int    codGrupo;
	private String nomeGrupo;
	
	/**
	 * @return the codGrupo
	 */
	public int getCodGrupo()
	{
		return codGrupo;
	}
	
	/**
	 * @param codGrupo the codGrupo to set
	 */
	public void setCodGrupo(int codGrupo)
	{
		this.codGrupo = codGrupo;
	}
	
	/**
	 * @return the nomeGrupo
	 */
	public String getNomeGrupo()
	{
		return nomeGrupo;
	}
	
	/**
	 * @param nomeGrupo the nomeGrupo to set
	 */
	public void setNomeGrupo(String nomeGrupo)
	{
		this.nomeGrupo = nomeGrupo;
	}

	public int compareTo(Object o)
	{
		if ( !(o instanceof SascGrupo) )
			throw new ClassCastException(o.getClass().getName());
		
		return this.getNomeGrupo().compareTo( ((SascGrupo)o).getNomeGrupo() );
	}
}
 
