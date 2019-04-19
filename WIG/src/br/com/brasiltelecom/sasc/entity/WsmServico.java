package br.com.brasiltelecom.sasc.entity;

/**
 * Objeto responsavel por conter as informacoes
 * referente aos servicos e a acao a ser tomada
 * para os mesmos
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  12/10/2006
 * 
 */
public class WsmServico
{
	private int idGrupo;
	private int idServico;
	private int statusServico;
	
	private int acao;
	
	/**
	 * @return the acao
	 */
	public int getAcao()
	{
		return acao;
	}
	
	/**
	 * @param acao the acao to set
	 */
	public void setAcao(int acao)
	{
		this.acao = acao;
	}
	
	/**
	 * @return the idGrupo
	 */
	public int getIdGrupo()
	{
		return idGrupo;
	}
	
	/**
	 * @param idGrupo the idGrupo to set
	 */
	public void setIdGrupo(int idGrupo)
	{
		this.idGrupo = idGrupo;
	}
	
	/**
	 * @return the idServico
	 */
	public int getIdServico()
	{
		return idServico;
	}
	
	/**
	 * @param idServico the idServico to set
	 */
	public void setIdServico(int idServico)
	{
		this.idServico = idServico;
	}
	
	/**
	 * @return the statusServico
	 */
	public int getStatusServico()
	{
		return statusServico;
	}
	
	/**
	 * @param statusServico the statusServico to set
	 */
	public void setStatusServico(int statusServico)
	{
		this.statusServico = statusServico;
	}
	
	public int hashCode()
	{
		return this.getIdServico();
	}
	
	public String toString()
	{
		return "Grupo: " + this.getIdGrupo() + "Id: "+this.getIdServico() + "Status: " + this.statusServico;
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof WsmServico) )
			return false;
		
		return ((WsmServico)obj).getIdServico() == this.getIdServico();
	}
}
 
