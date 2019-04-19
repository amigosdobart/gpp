package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Esta classe armazena as informacoes relativas a Histórico de Conessão de Bônus
 * 
 * @author Bernardo Dias
 * @since 03/05/2007
 */
public class HistoricoConcessao 
{
	private long   idCampanha;
	private String msisdn;
	private Date dataSatisfacao;
	private String nomeCondicao;
	private double bonus;
	private double bonusSM;
	private double bonusDados;

	private Campanha campanha;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * @return the bonus
	 */
	public double getBonus() 
	{
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(double bonus) 
	{
		this.bonus = bonus;
	}

	/**
	 * @return the bonusDados
	 */
	public double getBonusDados() 
	{
		return bonusDados;
	}

	/**
	 * @param bonusDados the bonusDados to set
	 */
	public void setBonusDados(double bonusDados) 
	{
		this.bonusDados = bonusDados;
	}

	/**
	 * @return the bonusSM
	 */
	public double getBonusSM() 
	{
		return bonusSM;
	}

	/**
	 * @param bonusSM the bonusSM to set
	 */
	public void setBonusSM(double bonusSM) 
	{
		this.bonusSM = bonusSM;
	}

	/**
	 * @return the dataSatisfacao
	 */
	public Date getDataSatisfacao() 
	{
		return dataSatisfacao;
	}

	public String getDataSatisfacaoStr()
	{
		if (getDataSatisfacao() != null)
			return sdf.format(getDataSatisfacao());
		return "";
	}
	
	/**
	 * @param dataSatisfacao the dataSatisfacao to set
	 */
	public void setDataSatisfacao(Date dataSatisfacao) 
	{
		this.dataSatisfacao = dataSatisfacao;
	}

	/**
	 * @return the nomeCondicao
	 */
	public String getNomeCondicao() 
	{
		return nomeCondicao;
	}

	/**
	 * @param nomeCondicao the nomeCondicao to set
	 */
	public void setNomeCondicao(String nomeCondicao) 
	{
		this.nomeCondicao = nomeCondicao;
	}

	/**
	 * @return   the current value of the msisdn property
	 */
	public String getMsisdn() 
	{
		return msisdn;    
	}
	
	/**
	 * @param aMsisdn the new value of the msisdn property
	 */
	public void setMsisdn(String aMsisdn) 
	{
		msisdn = aMsisdn;    
	}
	
	/**
	 * @return Retorna o msisdn Formatado (XX) 84XX-XXXX.
	 */
	public String getFMsisdn() 
	{
		return "(" + msisdn.substring(2,4) + ") " + msisdn.substring(4,8) + "-"+ msisdn.substring(8,12);
	}

	/**
	 * @return   the current value of the campanha property
	 */
	public Campanha getCampanha() 
	{
		return campanha;    
	}
	
	/**
	 * @param aCampanha the new value of the campanha property
	 */
	public void setCampanha(Campanha aCampanha) 
	{
		campanha = aCampanha;
	}

	/**
	 * @return Returns the idCampanha.
	 */
	public long getIdCampanha() 
	{
		return idCampanha;
	}

	/**
	 * @param idCampanha The idCampanha to set.
	 */
	public void setIdCampanha(long aIdCampanha) 
	{
		idCampanha = aIdCampanha;
	}
}
