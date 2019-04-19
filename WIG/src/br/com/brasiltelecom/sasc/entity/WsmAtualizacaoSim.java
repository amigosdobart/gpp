package br.com.brasiltelecom.sasc.entity;

import java.util.ArrayList;
import java.util.Collection;
import com.smarttrust.dp.wsm.api.WsmMenuOrderList;

/**
 * Objeto responsavel por conter as informacoes
 * necessarias para atualizacao dos servicos do
 * simcard atraves do WSM
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  12/10/2006
 * 
 */
public class WsmAtualizacaoSim
{
	private String 			 msisdn;
	private String 			 iccid;
	private Collection 		 acoes;
	private WsmMenuOrderList ordemServicos;
	
	private Collection servicosStatusIndeterminado;
	private Collection servicosDisponiveis;
	private Collection servicosNoSimcard;
	private Collection servicosPendentes;
	
	private Collection listaServicosOTA;
	
	public WsmAtualizacaoSim()
	{
		servicosStatusIndeterminado = new ArrayList();
		servicosDisponiveis 		= new ArrayList();
		servicosNoSimcard 			= new ArrayList();
		servicosPendentes 			= new ArrayList();
		listaServicosOTA			= new ArrayList();
		ordemServicos 				= new WsmMenuOrderList();
		acoes						= new ArrayList();
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
	 * @return the acoes
	 */
	public Collection getAcoes()
	{
		return acoes;
	}
	
	/**
	 * @param acao the acao to set
	 */
	public void setAcoes(Collection acao)
	{
		this.acoes = acao;
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
	 * @return the ordemServicos
	 */
	public WsmMenuOrderList getOrdemServicos()
	{
		return ordemServicos;
	}
	
	/**
	 * @param ordemServicos the ordemServicos to set
	 */
	public void setOrdemServicos(WsmMenuOrderList ordemServicos)
	{
		this.ordemServicos = ordemServicos;
	}
	
	/**
	 * @return the servicosDisponiveis
	 */
	public Collection getServicosDisponiveis()
	{
		return servicosDisponiveis;
	}
	
	/**
	 * @param servicoDisponivel the servicosDisponiveis to add
	 */
	public void addServicoDisponivel(WsmServico servicoDisponivel)
	{
		this.servicosDisponiveis.add(servicoDisponivel);
		this.listaServicosOTA.add(servicoDisponivel);
	}
	
	/**
	 * @return the servicosNoSimcard
	 */
	public Collection getServicosNoSimcard()
	{
		return servicosNoSimcard;
	}
	
	/**
	 * @param servicoNoSimcard the servicosNoSimcard to add
	 */
	public void addServicoNoSimcard(WsmServico servicoNoSimcard)
	{
		this.servicosNoSimcard.add(servicoNoSimcard);
		this.listaServicosOTA.add(servicoNoSimcard);
	}
	
	/**
	 * @return the servicosPendentes
	 */
	public Collection getServicosPendentes()
	{
		return servicosPendentes;
	}
	
	/**
	 * @param servicoPendente the servicosPendentes to add
	 */
	public void addServicoPendente(WsmServico servicoPendente)
	{
		this.servicosPendentes.add(servicoPendente);
	}
	
	/**
	 * @return the servicosStatusIndeterminado
	 */
	public Collection getServicosStatusIndeterminado()
	{
		return servicosStatusIndeterminado;
	}
	
	/**
	 * @param servicoStatusIndeterminado the servicosStatusIndeterminado to add
	 */
	public void addServicoStatusIndeterminado(WsmServico servicoStatusIndeterminado)
	{
		this.servicosStatusIndeterminado.add(servicoStatusIndeterminado);
	}
	
	/**
	 * @return the listaServicosOTA
	 */
	public Collection getListaServicosOTA()
	{
		return listaServicosOTA;
	}
}
 
