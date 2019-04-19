package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

public class FiltroModeloWig implements FiltroRespostaWig, Serializable
{
	private int codResposta;
	private Modelo modelo;
		
	public int getCodResposta()
	{
		return codResposta;
	}
	
	public void setCodResposta(int codResposta)
	{
		this.codResposta = codResposta;
	}
	
	public Modelo getModelo()
	{
		return modelo;
	}
	
	public void setModelo(Modelo modelo)
	{
		this.modelo = modelo;
	}

	public String getCabecalhoTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<th>Codigo</th>");
		linha.append("<th>Modelo</th>");
		linha.append("<th>Fabricante</th>");
		linha.append("</tr>");
		
		return linha.toString();
	}
	
	public String getLinhaTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<td>"+getModelo().getCodigoModelo()+"</td>");
		linha.append("<td>"+(getModelo() != null ? getModelo().getNomeModelo() : "&nbsp;")+"</td>");
		linha.append("<td>"+(getModelo() != null ? getModelo().getFabricante().getNomeFabricante() : "&nbsp;")+"</td>");
		linha.append("</tr>");
		
		return linha.toString();
	}
}
