package com.brt.gpp.aplicacoes.promocao.gerenteFaleGratis;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Classe responsavel por conter os dados referentes a
 * Promocao Fale Gratis, especificamente o gerenciamento
 * dos limites e Plano de Preco dos assinantes da Promocao
 * 
 * @author João Paulo Galvagni
 * @since  31/05/2007
 * 
 * @change Inclusao do atributo statusPromocao para gerenciamento 
 * 		   da retirada/insercao na promocao Fale Gratis a Noite
 * @author João Paulo Galvagni
 * @since  11/07/2007
 */
public class GerFaleGratisVO implements Entidade
{
	private String msisdn;
	private String periodo;
	
	private long   numSegundosTotalizados;
	private int    diaEntradaPromocao;
	
	private int	   idPlanoFaleGratis;
	private int	   idPlanoPulaPula;
	private int	   idLimiteSMS;
	
	private int	   idPromocao;
	private String tipoEspelhamento;
	
	private int	   statusPromocao;
	
	/**
	 * @return the diaEntradaPromocao
	 */
	public int getDiaEntradaPromocao()
	{
		return diaEntradaPromocao;
	}
	
	/**
	 * @param diaEntradaPromocao the diaEntradaPromocao to set
	 */
	public void setDiaEntradaPromocao(int diaEntradaPromocao)
	{
		this.diaEntradaPromocao = diaEntradaPromocao;
	}
	
	/**
	 * @return the idLimiteSMS
	 */
	public int getIdLimiteSMS()
	{
		return idLimiteSMS;
	}
	
	/**
	 * @param idLimiteSMS the idLimiteSMS to set
	 */
	public void setIdLimiteSMS(int idLimiteSMS)
	{
		this.idLimiteSMS = idLimiteSMS;
	}
	
	/**
	 * @return the idPlanoFaleGratis
	 */
	public int getIdPlanoFaleGratis()
	{
		return idPlanoFaleGratis;
	}
	
	/**
	 * @param idPlanoFaleGratis the idPlanoFaleGratis to set
	 */
	public void setIdPlanoFaleGratis(int idPlanoFaleGratis)
	{
		this.idPlanoFaleGratis = idPlanoFaleGratis;
	}
	
	/**
	 * @return the idPlanoPulaPula
	 */
	public int getIdPlanoPulaPula()
	{
		return idPlanoPulaPula;
	}
	
	/**
	 * @param idPlanoPulaPula the idPlanoPulaPula to set
	 */
	public void setIdPlanoPulaPula(int idPlanoPulaPula)
	{
		this.idPlanoPulaPula = idPlanoPulaPula;
	}
	
	/**
	 * @return the idPromocao
	 */
	public int getIdPromocao()
	{
		return idPromocao;
	}
	
	/**
	 * @param idPromocao the idPromocao to set
	 */
	public void setIdPromocao(int idPromocao)
	{
		this.idPromocao = idPromocao;
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
	 * @return the numSegundosTotalizados
	 */
	public long getNumSegundosTotalizados()
	{
		return numSegundosTotalizados;
	}
	
	/**
	 * @param numSegundosTotalizados the numSegundosTotalizados to set
	 */
	public void setNumSegundosTotalizados(long numSegundosTotalizados)
	{
		this.numSegundosTotalizados = numSegundosTotalizados;
	}
	
	/**
	 * @return the periodo
	 */
	public String getPeriodo()
	{
		return periodo;
	}
	
	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(String periodo)
	{
		this.periodo = periodo;
	}
	
	/**
	 * @return the tipoEspelhamento
	 */
	public String getTipoEspelhamento()
	{
		return tipoEspelhamento;
	}
	
	/**
	 * @param tipoEspelhamento the tipoEspelhamento to set
	 */
	public void setTipoEspelhamento(String tipoEspelhamento)
	{
		this.tipoEspelhamento = tipoEspelhamento;
	}
	
	/**
	 * Metodo....: clone
	 * Descricao.: Retorna um objeto identico ao atual
	 * 
	 * @return limiteSegundos - Objeto identico ao atual
	 */
	public Object clone()
	{
		GerFaleGratisVO vo = new GerFaleGratisVO();
		
		vo.setMsisdn(this.msisdn);
		vo.setPeriodo(this.periodo);
		
		vo.setNumSegundosTotalizados(this.numSegundosTotalizados);
		vo.setDiaEntradaPromocao(this.diaEntradaPromocao);
		
		vo.setIdPlanoFaleGratis(this.idPlanoFaleGratis);
		vo.setIdPlanoPulaPula(this.idPlanoPulaPula);
		vo.setIdLimiteSMS(this.idLimiteSMS);
		
		vo.setIdPromocao(this.idPromocao);
		vo.setTipoEspelhamento(this.tipoEspelhamento);
		
		vo.setStatusPromocao(this.statusPromocao);
		
		return vo;
	}
	
	/**
	 * @return the statusPromocao
	 */
	public int getStatusPromocao()
	{
		return statusPromocao;
	}
	
	/**
	 * @param statusPromocao the statusPromocao to set
	 */
	public void setStatusPromocao(int statusPromocao)
	{
		this.statusPromocao = statusPromocao;
	}
}