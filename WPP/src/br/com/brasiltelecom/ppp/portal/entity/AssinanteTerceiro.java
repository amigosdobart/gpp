package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Entidade da tabela <code>TBL_APR_ASSINANTE_TERCEIRO</code>
 * 
 * @autor Lucas Mindello de Andrade
 * @since Jul 18, 2008
 */
public class AssinanteTerceiro implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String msisdn;
	private PlanoTerceiro plano;
	private String operador;
	private Date atualizacao;
	
	public AssinanteTerceiro()
	{
	}

	/**
	 * Data de atualização do dado do assinante.
	 * @return
	 */
	public Date getAtualizacao() 
	{
		return atualizacao;
	}

	/**
	 * Atribui a data de atualização do dado do assinante.
	 * @param atualizacao
	 */
	public void setAtualizacao(Date atualizacao) 
	{
		this.atualizacao = atualizacao;
	}

	/**
	 * Recupera informação do operador.
	 * @return
	 */
	public String getOperador() 
	{
		return operador;
	}

	/**
	 * Atribui informação do operador.
	 * @param operador
	 */
	public void setOperador(String operador) 
	{
		this.operador = operador;
	}

	/**
	 * Recupera msisdn do assinante.
	 * @return
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}

	/**
	 * Atribui msisdn do assinante terceiro.
	 * @param msisdn
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}

	/**
	 * Recupera plano terceiro do assinante.
	 * @return
	 */
	public PlanoTerceiro getPlano() 
	{
		return plano;
	}

	/**
	 * Atribui plano terceiro ao assinante.
	 * @param plano
	 */
	public void setPlano(PlanoTerceiro plano)
	{
		this.plano = plano;
	}
}