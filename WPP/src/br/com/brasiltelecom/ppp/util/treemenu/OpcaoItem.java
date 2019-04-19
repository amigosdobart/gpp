package br.com.brasiltelecom.ppp.util.treemenu;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Representa um item do tipo opcao (campo checkbox).
 * 
 * Essa classe é um apoio para geração do XML a ser
 * interpretado pelo Javascript TreeMenu
 * 
 * @author Bernardo Vergne Dias
 * @since 17/01/2007
 */
public class OpcaoItem extends TreeMenuItem 
{
	private String texto;
	private String nomeCampo;
	private String valorCampo;
	private boolean ativo;

	public OpcaoItem(String texto, String nomeCampo, String valorCampo, boolean ativo) 
	{
		this.texto = texto;
		this.nomeCampo = nomeCampo;
		this.valorCampo = valorCampo;
		this.ativo = ativo;
	}
	
	public String getTexto() 
	{
		return texto;
	}

	public void setTexto(String texto) 
	{
		this.texto = texto;
	}

	public boolean isAtivo() 
	{
		return ativo;
	}

	public void setAtivo(boolean ativo) 
	{
		this.ativo = ativo;
	}

	public String getNomeCampo() 
	{
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) 
	{
		this.nomeCampo = nomeCampo;
	}

	public String getValorCampo() 
	{
		return valorCampo;
	}

	public void setValorCampo(String valorCampo) 
	{
		this.valorCampo = valorCampo;
	}
	
	public String toXML() {
		String ativo = (this.ativo == true ? "ativo=\"true\" " : "");
		
		String buff = "<opcao texto=\"" + this.escape(texto) + "\" nome=\"" 
			+ this.escape(nomeCampo) + "\" valor=\"" + this.escape(valorCampo) + "\" " + ativo + "/>";

		return buff;
	}
}
