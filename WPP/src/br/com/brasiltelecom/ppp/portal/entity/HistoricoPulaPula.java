/*
 * Created on 28/03/2005
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 *	Entidade HistoricoPulaPula, que modela a tabela de historico de execucao da promocao Pula-Pula, conforme definido
 *	no banco de dados do GPP.
 * 
 * @author	Daniel Ferreira
 * @since	28/03/2005
 */

public class HistoricoPulaPula 
{
	private String idtMsisdn;
	private Integer idtPromocao;
	private Date datExecucao;
	private String desStatusExecucao;
	private String idtCodigoRetorno;
	//Descricao do Codigo de Retorno, conforme definido na tabela TBL_PRO_CODIGOS_RETORNO
	private String operador;
	private Integer motivo;
	private Double vlrCreditoBonus;
	private String desCodigoRetorno;
	
	/**
	 * @return Returns the desCodigoRetorno.
	 */
	public String getDesCodigoRetorno() 
	{
		return desCodigoRetorno;
	}
	/**
	 * @param desCodigoRetorno The desCodigoRetorno to set.
	 */
	public void setDesCodigoRetorno(String desCodigoRetorno) 
	{
		this.desCodigoRetorno = desCodigoRetorno;
	}
		
	/**
	 * @return Returns the operador.
	 */
	public String getOperador() 
	{
		return operador;
	}
	/**
	 * @param operador The operador to set.
	 */
	public void setOperador(String operador) 
	{
		this.operador = operador;
	}
	
	/**
	 * @return Returns the operador.
	 */
	public Integer getMotivo() 
	{
		return motivo;
	}
	/**
	 * @param operador The operador to set.
	 */
	public void setMotivo(Integer motivo) 
	{
		this.motivo = motivo;
	}
	/**
	 * @return Returns the datExecucao.
	 */
	public Date getDatExecucao() 
	{
		return datExecucao;
	}
	/**
	 * @param datExecucao The datExecucao to set.
	 */
	public void setDatExecucao(Date datExecucao) 
	{
		this.datExecucao = datExecucao;
	}
	/**
	 * @return Returns the desStatusExecucao.
	 */
	public String getDesStatusExecucao() 
	{
		return desStatusExecucao;
	}
	/**
	 * @param desStatusExecucao The desStatusExecucao to set.
	 */
	public void setDesStatusExecucao(String desStatusExecucao) 
	{
		this.desStatusExecucao = desStatusExecucao;
	}
	/**
	 * @return Returns the idtCodigoRetorno.
	 */
	public String getIdtCodigoRetorno() 
	{
		return idtCodigoRetorno;
	}
	/**
	 * @param idtCodigoRetorno The idtCodigoRetorno to set.
	 */
	public void setIdtCodigoRetorno(String idtCodigoRetorno) 
	{
		this.idtCodigoRetorno = idtCodigoRetorno;
	}
	/**
	 * @return Returns the idtMsisdn.
	 */
	public String getIdtMsisdn() 
	{
		return idtMsisdn;
	}
	/**
	 * @param idtMsisdn The idtMsisdn to set.
	 */
	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}
	/**
	 * @return Returns the idtPromocao.
	 */
	public Integer getIdtPromocao() 
	{
		return idtPromocao;
	}
	/**
	 * @param idtPromocao The idtPromocao to set.
	 */
	public void setIdtPromocao(Integer idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
	/**
	 * @return Returns the vlrCreditoBonus.
	 */
	public Double getVlrCreditoBonus() 
	{
		return vlrCreditoBonus;
	}
	/**
	 * @param vlrCreditoBonus The vlrCreditoBonus to set.
	 */
	public void setVlrCreditoBonus(Double vlrCreditoBonus) 
	{
		this.vlrCreditoBonus = vlrCreditoBonus;
	}
	
}
