package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Classe que contem os atributos de Servico do SASC 
 *  
 * @author JOAO PAULO GALVAGNI 
 * @since  06/02/2007 
 *
 */
public class SascServico implements Comparable
{ 
	private int 	   codServico;
	private String 	   nomeServico;
	private boolean    blackList;
	private SascGrupo  grupo;
	private SascPerfil perfil;
	
	/**
	 * @return the isBlackList
	 */
	public boolean isBlackList()
	{
		return blackList;
	}
	
	/**
	 * @param isBlackList the isBlackList to set
	 */
	public void setBlackList(boolean isBlackList)
	{
		this.blackList = isBlackList;
	}
	
	/**
	 * @return the codServico
	 */
	public int getCodServico()
	{
		return codServico;
	}
	
	/**
	 * @param codServico the codServico to set
	 */
	public void setCodServico(int codServico)
	{
		this.codServico = codServico;
	}
	
	/**
	 * @return the grupo
	 */
	public SascGrupo getGrupo()
	{
		return grupo;
	}
	
	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(SascGrupo grupo)
	{
		this.grupo = grupo;
	}
	
	/**
	 * @return the nomeServico
	 */
	public String getNomeServico()
	{
		return nomeServico;
	}
	
	/**
	 * @param nomeServico the nomeServico to set
	 */
	public void setNomeServico(String nomeServico)
	{
		this.nomeServico = nomeServico;
	}
	
	/**
	 * @return the perfil
	 */
	public SascPerfil getPerfil()
	{
		return perfil;
	}
	
	/**
	 * @param perfil the perfil to set
	 */
	public void setPerfil(SascPerfil perfil)
	{
		this.perfil = perfil;
	}
	
	public int compareTo(Object o)
	{
		if ( !(o instanceof SascServico) )
			throw new ClassCastException(o.getClass().getName());
		
		SascServico objRef = (SascServico)o;
		
		if ( this.getGrupo().compareTo(objRef.getGrupo()) != 0 )
			return this.getGrupo().compareTo(objRef.getGrupo());
		
		return this.getNomeServico().compareTo(objRef.getNomeServico());
	}
}