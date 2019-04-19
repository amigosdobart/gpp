package br.com.brasiltelecom.ppp.portal.entity;

/**
 *	Entidade que representa os registros da tabela TBL_PRO_STATUS_ASSINANTE.
 *
 *	@author		Daniel Ferreira
 *	@since		12/06/2006
 */
public class PromocaoStatusAssinante 
{

    //Atributos.
    
	/**
	 *	Identificador do status da promocao do assinante.
	 */
	private int idtStatus;
	
	/**
	 *	Descricao do status da promocao do assinante.
	 */
	private String desStatus;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoStatusAssinante()
	{
	    this.idtStatus	= -1;
	    this.desStatus	= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador do status da promocao do assinante.
	 *
	 *	@return		Identificador do status da promocao do assinante.
	 */
	public int getIdtStatus()
	{
	    return this.idtStatus;
	}
	
	/**
	 *	Retorna a descricao do status da promocao do assinante.
	 *
	 *	@return		Descricao do status da promocao do assinante.
	 */
	public String getDesStatus()
	{
	    return this.desStatus;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador do status da promocao do assinante.
	 *
	 *	@param		idtStatus				Identificador do status da promocao do assinante.
	 */
	public void setIdtStatus(int idtStatus)
	{
	    this.idtStatus = idtStatus;
	}
	
	/**
	 *	Atribui a descricao do status da promocao do assinante.
	 *
	 *	@param		desStatus				Descricao do status da promocao do assinante.
	 */
	public void setDesStatus(String desStatus)
	{
	    this.desStatus = desStatus;
	}
	
}
