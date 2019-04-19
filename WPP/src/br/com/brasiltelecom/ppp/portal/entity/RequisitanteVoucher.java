package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Esta classe e a responsavel por encapsular as propriedades
 * de armazenamento da entidade que define as informacoes de
 * requisitantes de pedido de voucher
 * 
 * @author Joao Carlos
 *
 */
public class RequisitanteVoucher
{
	private String idRequisitante;
	private String descRequisitante;
	private String eMail;
	
	public RequisitanteVoucher()
	{
	}

	/**
	 * @return Returns the descRequisitante.
	 */
	public String getDescRequisitante()
	{
		return descRequisitante;
	}

	/**
	 * @param descRequisitante The descRequisitante to set.
	 */
	public void setDescRequisitante(String descRequisitante)
	{
		this.descRequisitante = descRequisitante;
	}
	
	/**
	 * @return Returns the eMail.
	 */
	public String getEMail()
	{
		return eMail;
	}
	
	/**
	 * @param mail The eMail to set.
	 */
	public void setEMail(String mail)
	{
		eMail = mail;
	}
	
	/**
	 * @return Returns the idRequisitante.
	 */
	public String getIdRequisitante()
	{
		return idRequisitante;
	}
	
	/**
	 * @param idRequisitante The idRequisitante to set.
	 */
	public void setIdRequisitante(String idRequisitante)
	{
		this.idRequisitante = idRequisitante;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return getIdRequisitante().hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getDescRequisitante();
	}
}
