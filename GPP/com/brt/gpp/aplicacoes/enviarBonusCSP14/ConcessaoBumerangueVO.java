package com.brt.gpp.aplicacoes.enviarBonusCSP14;

import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;

public class ConcessaoBumerangueVO
{
	private String datMes;
	private CodigoNacional cn;
	
	/**
	 * Metodo....:getcn
	 * Descricao.:Retorna o valor de cn
	 * @return cn.
	 */
	public CodigoNacional getCn()
	{
		return cn;
	}
	
	/**
	 * Metodo....:setcn
	 * Descricao.:Define o valor de cn
	 * @param cn o valor a ser definido para cn
	 */
	public void setCn(CodigoNacional cn)
	{
		this.cn = cn;
	}
	
	/**
	 * Metodo....:getdatMes
	 * Descricao.:Retorna o valor de datMes
	 * @return datMes.
	 */
	public String getDatMes()
	{
		return datMes;
	}
	
	/**
	 * Metodo....:setdatMes
	 * Descricao.:Define o valor de datMes
	 * @param datMes o valor a ser definido para datMes
	 */
	public void setDatMes(String datMes)
	{
		this.datMes = datMes;
	}
}
