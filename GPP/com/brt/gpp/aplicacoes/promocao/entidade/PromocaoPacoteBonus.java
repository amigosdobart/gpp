package com.brt.gpp.aplicacoes.promocao.entidade;

import java.util.Date;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Classe responsavel por conter as informacoes de
 * Bonus relacionada com a promocao
 * 
 * @author Joao Paulo Galvagni
 * @since  01/11/2007
 */
public class PromocaoPacoteBonus
{
	private int 			idBonus;
	private Promocao 		promocao;
	private CodigoNacional 	codigoNacional;
	private TipoSaldo		tipoSaldo;
	private Date			dataInicioVigencia;
	private Date			dataFimVigencia;
	private double			valorBonus;
	private String			idCanal;
	private String			idOrigem;
	
	/**
	 * @return the codigoNacional
	 */
	public CodigoNacional getCodigoNacional()
	{
		return codigoNacional;
	}
	
	/**
	 * @param codigoNacional the codigoNacional to set
	 */
	public void setCodigoNacional(CodigoNacional codigoNacional)
	{
		this.codigoNacional = codigoNacional;
	}
	
	/**
	 * @return the dataFimVigencia
	 */
	public Date getDataFimVigencia()
	{
		return dataFimVigencia;
	}
	
	/**
	 * @param dataFimVigencia the dataFimVigencia to set
	 */
	public void setDataFimVigencia(Date dataFimVigencia)
	{
		this.dataFimVigencia = dataFimVigencia;
	}
	
	/**
	 * @return the dataInicioVigencia
	 */
	public Date getDataInicioVigencia()
	{
		return dataInicioVigencia;
	}
	
	/**
	 * @param dataInicioVigencia the dataInicioVigencia to set
	 */
	public void setDataInicioVigencia(Date dataInicioVigencia)
	{
		this.dataInicioVigencia = dataInicioVigencia;
	}
	
	/**
	 * @return the idBonus
	 */
	public int getIdBonus()
	{
		return idBonus;
	}
	
	/**
	 * @param idBonus the idBonus to set
	 */
	public void setIdBonus(int idBonus)
	{
		this.idBonus = idBonus;
	}
	
	/**
	 * @return the idCanal
	 */
	public String getIdCanal()
	{
		return idCanal;
	}
	
	/**
	 * @param idCanal the idCanal to set
	 */
	public void setIdCanal(String idCanal)
	{
		this.idCanal = idCanal;
	}
	
	/**
	 * @return the idOrigem
	 */
	public String getIdOrigem()
	{
		return idOrigem;
	}
	
	/**
	 * @param idOrigem the idOrigem to set
	 */
	public void setIdOrigem(String idOrigem)
	{
		this.idOrigem = idOrigem;
	}
	
	/**
	 * @return the promocao
	 */
	public Promocao getPromocao()
	{
		return promocao;
	}
	
	/**
	 * @param promocao the promocao to set
	 */
	public void setPromocao(Promocao promocao)
	{
		this.promocao = promocao;
	}
	
	/**
	 * @return the valorBonus
	 */
	public double getValorBonus()
	{
		return valorBonus;
	}
	
	/**
	 * @param valorBonus the valorBonus to set
	 */
	public void setValorBonus(double valorBonus)
	{
		this.valorBonus = valorBonus;
	}
	
	/**
	 * @return the tipoSaldo
	 */
	public TipoSaldo getTipoSaldo()
	{
		return tipoSaldo;
	}
	
	/**
	 * @param tipoSaldo the tipoSaldo to set
	 */
	public void setTipoSaldo(TipoSaldo tipoSaldo)
	{
		this.tipoSaldo = tipoSaldo;
	}
	
	public String getTipoTransacao()
	{
		return this.idCanal + this.idOrigem;
	}
	
	public void setTipoTransacao(String tipoTransacao)
	{
		this.idCanal  = tipoTransacao.substring(0, 2);
		this.idOrigem = tipoTransacao.substring(2);
	}
	
	/**
	 * Compara a data informada com as datas de vigencia
	 * 
	 * @param  datProcessamento	- Data informada
	 * @return true se vigente, false caso contrario
	 */
	public boolean isVigente(Date datProcessamento)
	{
		if(datProcessamento == null || dataInicioVigencia == null)
            return false;
        
        if(dataInicioVigencia.compareTo(datProcessamento) > 0)
            return false;
        
        if(dataFimVigencia == null)
            return true;
        
        return (dataFimVigencia.compareTo(datProcessamento) >= 0);
	}
}