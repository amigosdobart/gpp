package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

public class FiltroERBWig implements FiltroRespostaWig, Serializable
{
	private int codResposta;
	private int lac;
	private int cellId;
	
	/**
	 * @return Returns the cellId.
	 */
	public int getCellId()
	{
		return cellId;
	}
	
	/**
	 * @param cellId The cellId to set.
	 */
	public void setCellId(int cellId)
	{
		this.cellId = cellId;
	}
	
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
	 * @return Returns the lac.
	 */
	public int getLac()
	{
		return lac;
	}
	
	/**
	 * @param lac The lac to set.
	 */
	public void setLac(int lac)
	{
		this.lac = lac;
	}

	public String getCabecalhoTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<th>LAC</th>");
		linha.append("<th>Cell-ID</th>");
		linha.append("</tr>");
		
		return linha.toString();
	}
	
	public String getLinhaTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<td>"+getLac()+"</td>");
		linha.append("<td>"+getCellId()+"</td>");
		linha.append("</tr>");
		
		return linha.toString();
	}
}
