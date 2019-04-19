package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Grupo de Serviços do SFA
 * @author Alberto Magno 
 * @since 30/07/2004
 */
public class GrupoServicoSFA {

	private int idtGrupoServico;
	private int idtClasseServico;
	private String desGrupoServico;
	/**
	 * @return
	 */
	public String getDesGrupoServico()
	{
		return desGrupoServico;
	}

	/**
	 * @return
	 */
	public int getIdtClasseServico()
	{
		return idtClasseServico;
	}

	/**
	 * @return
	 */
	public int getIdtGrupoServico()
	{
		return idtGrupoServico;
	}

	/**
	 * @param string
	 */
	public void setDesGrupoServico(String string)
	{
		desGrupoServico = string;
	}

	/**
	 * @param i
	 */
	public void setIdtClasseServico(int i)
	{
		idtClasseServico = i;
	}

	/**
	 * @param i
	 */
	public void setIdtGrupoServico(int i)
	{
		idtGrupoServico = i;
	}

}
