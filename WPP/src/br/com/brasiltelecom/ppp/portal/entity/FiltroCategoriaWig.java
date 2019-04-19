package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;

public class FiltroCategoriaWig implements FiltroRespostaWig, Serializable
{
	private int codResposta;
	private Categoria categoria;
	
	public Categoria getCategoria()
	{
		return categoria;
	}
	
	public void setCategoria(Categoria categoria)
	{
		this.categoria = categoria;
	}
		
	public int getCodResposta()
	{
		return codResposta;
	}
	
	public void setCodResposta(int codResposta)
	{
		this.codResposta = codResposta;
	}

	public String getCabecalhoTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<th>Codigo</th>");
		linha.append("<th>Descricao</th>");
		linha.append("</tr>");
		
		return linha.toString();
	}
	
	public String getLinhaTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<td>"+getCategoria().getIdCategoria()+"</td>");
		linha.append("<td>"+getCategoria().getDesCategoria()+"</td>");
		linha.append("</tr>");
		
		return linha.toString();
	}
}
