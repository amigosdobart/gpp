package com.brt.gpp.aplicacoes.promocao.entidade;

import java.util.Date;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Classe que contem os atributos referentes ao Bonus que
 * devera ser concedido/agendado para o assinante
 * 
 * @author João Paulo Galvagni
 * @since  16/11/2007
 */
public class BonusIncentivoRecarga implements Entidade
{
	private int								idBonusIncentivo;
	private String 							dataInicioConcessaoBonus;
	private double 							valorConcessaoBonus;
	private double 							valorRecarga;
	private TipoSaldo 						tipoSaldo;
	private MensagemBonusIncentivoRecarga 	mensagemBonusIncentivo;
	
	public Object clone()
	{
		BonusIncentivoRecarga clone = new BonusIncentivoRecarga();
		
		clone.setDataInicioConcessaoBonus(this.dataInicioConcessaoBonus);
		clone.setValorConcessaoBonus(this.valorConcessaoBonus);
		clone.setValorRecarga(this.valorRecarga);
		clone.setTipoSaldo(this.tipoSaldo);
		
		return clone;
	}
	
	public BonusIncentivoRecarga()
	{
		dataInicioConcessaoBonus 		= null;
		valorConcessaoBonus 	= 0.0;
		valorRecarga			= 0.0;
		tipoSaldo				= null;
		mensagemBonusIncentivo 	= null;
	}
	
	public String toString()
	{
		return "idBonusIncentivo: " + idBonusIncentivo + 
			   " - valorRecarga: " + valorRecarga + 
			   " - valorConcessaoBonus: " + valorConcessaoBonus +
			   " - tipoSaldo: " + tipoSaldo.toString();
	}
	
	/**
	 * @return the mensagemBonusIncentivo
	 */
	public MensagemBonusIncentivoRecarga getMensagemBonusIncentivo()
	{
		return mensagemBonusIncentivo;
	}
	
	/**
	 * @param mensagemBonusIncentivo the mensagemBonusIncentivo to set
	 */
	public void setMensagemBonusIncentivo(MensagemBonusIncentivoRecarga mensagemBonusIncentivo)
	{
		this.mensagemBonusIncentivo = mensagemBonusIncentivo;
	}
	
	/**
	 * @return the dataInicioConcessaoBonus
	 */
	public String getDataInicioConcessaoBonus()
	{
		return dataInicioConcessaoBonus;
	}
	
	/**
	 * @param dataInicioConcessaoBonus the dataInicioConcessaoBonus to set
	 */
	public void setDataInicioConcessaoBonus(String dataInicioConcessaoBonus)
	{
		this.dataInicioConcessaoBonus = dataInicioConcessaoBonus;
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
	
	/**
	 * @return the valorConcessaoBonus
	 */
	public double getValorConcessaoBonus()
	{
		return valorConcessaoBonus;
	}
	
	/**
	 * @param valorConcessaoBonus the valorConcessaoBonus to set
	 */
	public void setValorConcessaoBonus(double valorConcessaoBonus)
	{
		this.valorConcessaoBonus = valorConcessaoBonus;
	}
	
	/**
	 * @return the valorRecarga
	 */
	public double getValorRecarga()
	{
		return valorRecarga;
	}
	
	/**
	 * @param valorRecarga the valorRecarga to set
	 */
	public void setValorRecarga(double valorRecarga)
	{
		this.valorRecarga = valorRecarga;
	}
	
	/**
	 * @return the idBonusIncentivo
	 */
	public int getIdBonusIncentivo()
	{
		return idBonusIncentivo;
	}
	
	/**
	 * @param idBonusIncentivo the idBonusIncentivo to set
	 */
	public void setIdBonusIncentivo(int idBonusIncentivo)
	{
		this.idBonusIncentivo = idBonusIncentivo;
	}
}