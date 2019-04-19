package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

public class FiltroRestricaoTempoWig implements FiltroRespostaWig, Serializable
{
	private int codResposta;
	private String anos;
	private String meses;
	private String diasSemana;
	private String dias;
	private String horas;
	
	/**
	 * @return Returns the codResposta.
	 */
	public int getCodResposta()
	{
		return codResposta;
	}
	
	/**
	 * @param codResposta The codResposta to set.
	 */
	public void setCodResposta(int codResposta)
	{
		this.codResposta = codResposta;
	}
	
	/**
	 * @return Returns the dias.
	 */
	public String getDias()
	{
		return dias;
	}
	
	/**
	 * @param dias The dias to set.
	 */
	public void setDias(String dias)
	{
		this.dias = dias;
	}
	
	/**
	 * @return Returns the diasSemana.
	 */
	public String getDiasSemana()
	{
		return diasSemana;
	}
	
	/**
	 * @param diasSemana The diasSemana to set.
	 */
	public void setDiasSemana(String diasSemana)
	{
		this.diasSemana = diasSemana;
	}
	
	/**
	 * @return Returns the horas.
	 */
	public String getHoras()
	{
		return horas;
	}
	
	/**
	 * @param horas The horas to set.
	 */
	public void setHoras(String horas)
	{
		this.horas = horas;
	}
	
	/**
	 * @return Returns the meses.
	 */
	public String getMeses()
	{
		return meses;
	}
	
	/**
	 * @param meses The meses to set.
	 */
	public void setMeses(String meses)
	{
		this.meses = meses;
	}
	
	/**
	 * @return the anos
	 */
	public String getAnos()
	{
		return anos;
	}
	
	/**
	 * @param anos the anos to set
	 */
	public void setAnos(String anos)
	{
		this.anos = anos;
	}

	public String getCabecalhoTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<th>Anos</th>");
		linha.append("<th>Meses</th>");
		linha.append("<th>Dias</th>");
		linha.append("<th>Horas</th>");
		linha.append("</tr>");
		
		return linha.toString();
	}
	
	public String getLinhaTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<td>"+getAnos()+"</td>");
		linha.append("<td>"+getMeses()+"</td>");
		linha.append("<td>"+getDias()+"</td>");
		linha.append("<td>"+getHoras()+"</td>");
		linha.append("</tr>");
		
		return linha.toString();
	}
}