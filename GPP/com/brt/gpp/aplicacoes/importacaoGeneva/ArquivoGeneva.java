package com.brt.gpp.aplicacoes.importacaoGeneva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArquivoGeneva
{
	private String tipoRegistro;
	private String codigoNacional;
	private String prefixo;
	private String mcdu;
	private String numContrato;
	private String nomAssinante;
	private Double vlrCredito;
	private String codigoServico;
	private String dataAtivacao;
	private String dataEncerramento;
	private boolean removerAssinante;
	private SimpleDateFormat formato;
	
	
	public ArquivoGeneva()
	{
		tipoRegistro = null;
		codigoNacional = null;
		prefixo = null;
		mcdu = null;
		numContrato = null;
		nomAssinante = null;
		vlrCredito = null;
		codigoServico = null;
		dataAtivacao = null;
		dataEncerramento = null;
		removerAssinante = false;
		formato = new SimpleDateFormat("yyyyMMdd");
	}


	public String getCodigoNacional()
	{
		return codigoNacional;
	}


	public String getCodigoServico()
	{
		return codigoServico;
	}


	public String getDataAtivacao()
	{
		return dataAtivacao;
	}

	
	public Date getFDataAtivacao() throws ParseException 
	{
		return formato.parse(dataAtivacao);
	}

	
	public String getDataEncerramento()
	{
		return dataEncerramento;
	}
	
	
	public Date getFDataEncerramento() throws ParseException
	{
		return formato.parse(dataEncerramento);
	}


	public String getMcdu()
	{
		return mcdu;
	}


	public String getNomAssinante()
	{
		return nomAssinante;
	}


	public String getNumContrato()
	{
		return numContrato;
	}


	public String getPrefixo()
	{
		return prefixo;
	}


	public String getTipoRegistro()
	{
		return tipoRegistro;
	}


	public Double getVlrCredito()
	{
		return vlrCredito;
	}


	public boolean getRemoverAssinante()
	{
		return removerAssinante;
	}
	
	
	public String getMsisdn()
	{
		return "55"+this.codigoNacional+this.prefixo+this.mcdu;
	}
	
	
	public void setCodigoNacional(String codigoNacional)
	{
		this.codigoNacional = codigoNacional;
	}


	public void setCodigoServico(String codigoServico)
	{
		this.codigoServico = codigoServico;
	}


	public void setDataAtivacao(String dataAtivacao)
	{
		this.dataAtivacao = dataAtivacao;
	}


	public void setDataEncerramento(String dataEncerramento)
	{
		this.dataEncerramento = dataEncerramento;
	}


	public void setMcdu(String mcdu)
	{
		this.mcdu = mcdu;
	}


	public void setNomAssinante(String nomAssinante)
	{
		this.nomAssinante = nomAssinante;
	}


	public void setNumContrato(String numContrato)
	{
		this.numContrato = numContrato;
	}


	public void setPrefixo(String prefixo)
	{
		this.prefixo = prefixo;
	}


	public void setTipoRegistro(String tipoRegistro)
	{
		this.tipoRegistro = tipoRegistro;
	}


	public void setVlrCredito(Double vlrCredito)
	{
		this.vlrCredito = vlrCredito;
	}

	
	public void setRemoverAssinante(boolean removerAssinante)
	{
		this.removerAssinante = removerAssinante;
	}
	
	
	public void parse (String linha)
	{
		this.setTipoRegistro(linha.substring(0, 2));
		this.setCodigoNacional(linha.substring(12, 14));
		this.setPrefixo(linha.substring(14, 18));
		this.setMcdu(linha.substring(18, 22));
		this.setNumContrato(linha.substring(22, 32));
		this.setNomAssinante(linha.substring(32, 72).trim());
		this.setVlrCredito(new Double(linha.substring(81,83)+"."+linha.substring(83,85)));
		this.setCodigoServico(linha.substring(129,134));
		this.setDataAtivacao(linha.substring(134,142));
		if("2".equals(linha.substring(142,143)))
		{
			this.setDataEncerramento(linha.substring(142,150));
			this.setRemoverAssinante(true);
		}
	}
	
	public String toString()
	{
		return "Msisdn: "+this.getMsisdn()+"Contrato: "+this.getNumContrato();
	}
}
