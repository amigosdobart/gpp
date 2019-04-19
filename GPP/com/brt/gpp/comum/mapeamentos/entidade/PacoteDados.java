package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Classe que contem as informacoes referentes a
 * tabela TBL_PRO_PACOTE_DADOS
 * 
 * @author João Paulo Galvagni
 * @since  31/08/2007
 */
public class PacoteDados
{
	private int				 idtPacoteDados;
	private ServicoAssinante servicoAssinante;
    private int 			 numDias;
    private String  		 desValorPacote;
    private String  		 desPacote;
    private boolean 		 habilitado;
	
	/**
	 * @return the desPacote
	 */
	public String getDesPacote()
	{
		return desPacote;
	}
	
	/**
	 * @param desPacote the desPacote to set
	 */
	public void setDesPacote(String desPacote)
	{
		this.desPacote = desPacote;
	}
	
	/**
	 * @return the desValorPacote
	 */
	public String getDesValorPacote()
	{
		return desValorPacote;
	}
	
	/**
	 * @param desValorPacote the desValorPacote to set
	 */
	public void setDesValorPacote(String desValorPacote)
	{
		this.desValorPacote = desValorPacote;
	}
	
	/**
	 * @return the habilitado
	 */
	public boolean isHabilitado()
	{
		return habilitado;
	}
	
	/**
	 * @param habilitado the habilitado to set
	 */
	public void setHabilitado(boolean habilitado)
	{
		this.habilitado = habilitado;
	}
	
	/**
	 * @return the servicoAssinante
	 */
	public ServicoAssinante getServicoAssinante()
	{
		return servicoAssinante;
	}
	
	/**
	 * @param servicoAssinante the servicoAssinante to set
	 */
	public void setServicoAssinante(ServicoAssinante servicoAssinante)
	{
		this.servicoAssinante = servicoAssinante;
	}

	/**
	 * @return the numDias
	 */
	public int getNumDias()
	{
		return numDias;
	}
	
	/**
	 * @param numDias the numDias to set
	 */
	public void setNumDias(int numDias)
	{
		this.numDias = numDias;
	}
	
	/**
	 * @return the idtPacoteDados
	 */
	public int getIdtPacoteDados()
	{
		return idtPacoteDados;
	}
	
	/**
	 * @param idtPacoteDados the idtPacoteDados to set
	 */
	public void setIdtPacoteDados(int idtPacoteDados)
	{
		this.idtPacoteDados = idtPacoteDados;
	}
}
