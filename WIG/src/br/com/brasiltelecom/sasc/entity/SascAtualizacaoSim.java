package br.com.brasiltelecom.sasc.entity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Objeto responsavel por conter as informacoes  
 * necessarias para atualizacao dos servicos no 
 * simcard atraves do SASC
 *  
 * @author JOAO PAULO GALVAGNI 
 * @since  12/10/2006 
 *
 */
public class SascAtualizacaoSim
{
	private String msisdn;
	private String iccid;
	private int    status;
	private int    qtdeTentativas;
	
	private Collection servicosBlackList;
	
	public SascAtualizacaoSim()
	{
		servicosBlackList = new ArrayList();
	}
	
	/**
	 * @return the servicosBlackList
	 */
	public Collection getServicosBlackList()
	{
		return servicosBlackList;
	}
	
	/**
	 * @param servicoBlackList the servicoBlackList to add
	 */
	public void setServicosBlackList(Collection servicoBlackList)
	{
		this.servicosBlackList.addAll(servicoBlackList);
	}

	/**
	 * @return the iccid
	 */
	public String getIccid()
	{
		return iccid;
	}
	
	/**
	 * @param iccid the iccid to set
	 */
	public void setIccid(String iccid)
	{
		this.iccid = iccid;
	}
	
	/**
	 * @return the msisdn
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 * @return the qtdeTentativas
	 */
	public int getQtdeTentativas()
	{
		return qtdeTentativas;
	}
	
	/**
	 * @param qtdeTentativas the qtdeTentativas to set
	 */
	public void setQtdeTentativas(int qtdeTentativas)
	{
		this.qtdeTentativas = qtdeTentativas;
	}
	
	/**
	 * @return the status
	 */
	public int getStatus()
	{
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}
	
		
}
 
