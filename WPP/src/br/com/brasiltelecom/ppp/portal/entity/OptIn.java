package br.com.brasiltelecom.ppp.portal.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Modela as informações de Opt-in das Tabelas de Hsid
 * @author Geraldo Palmeira
 * @since 01/09/2006
 */
public class OptIn 
{

	private String msisdn;
	private Date   dataOptIn;
	private Date   dataFidelizacao;
	private Date   dataOptOut;
	private int    lac;
	private int    cellId;
	private Modelo modelo;
	private int    indMandatorio;
	private int    codigoConteudo;
	private Collection optInHistorico;
	private Collection preferencias;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public OptIn()
	{
		optInHistorico = new ArrayList();
	}
	/**
	 * @return Retorna o cellId.
	 */
	public int getCellId() 
	{
		return cellId;
	}
	/**
	 * @param cellId Cell id.
	 */
	public void setCellId(int cellId) 
	{
		this.cellId = cellId;
	}
	/**
	 * @return Retorna o codigoConteudo.
	 */
	public int getCodigoConteudo() 
	{
		return codigoConteudo;
	}
	/**
	 * @param codigoConteudo Código Conteúdo.
	 */
	public void setCodigoConteudo(int codigoConteudo) 
	{
		this.codigoConteudo = codigoConteudo;
	}
	/**
	 * @return Retorna o codigoModelo.
	 */
	public Modelo getModelo() 
	{
		return modelo;
	}
	/**
	 * @param codigoModelo Código Modelo.
	 */
	public void setModelo(Modelo modelo) 
	{
		this.modelo = modelo;
	}
	/**
	 * @return Retorna o dataFidelizacao.
	 */
	public Date getDataFidelizacao() 
	{
		return dataFidelizacao;
	}
	/**
	 * @return Retorna o dataFidelizacao formatada.
	 */
	public String getFDataFidelizacao() 
	{
		if (dataFidelizacao != null)
		{
			return sdf.format(dataFidelizacao);
		}
		else
		{
			return "Não Fidelizado";
		}
	}
	/**
	 * @param dataFidelizacao Data Fidelização.
	 */
	public void setDataFidelizacao(Date dataFidelizacao) 
	{
		this.dataFidelizacao = dataFidelizacao;
	}
	/**
	 * @return Retorna o dataOptIn.
	 */
	public Date getDataOptIn() 
	{
		return dataOptIn;
	}
	/**
	 * @return Retorna o dataOptIn formatada.
	 */
	public String getFDataOptIn() 
	{
		return sdf.format(dataOptIn);
	}
	/**
	 * @param dataOptIn Data de Opt-in.
	 */
	public void setDataOptIn(Date dataOptIn) 
	{
		this.dataOptIn = dataOptIn;
	}
	/**
	 * @return Retorna o dataOptOut formatada.
	 */
	public String getFDataOptOut() 
	{
		if (dataOptOut != null)
		{
			return sdf.format(dataOptOut);
		}
		else
		{
			return "Opt-in Ativo";
		}
	}
	/**
	 * @return Retorna o dataOptOut.
	 */
	public Date getDataOptOut() 
	{
		return dataOptOut;
	}
	/**
	 * @param dataOptOut Data de Opt-Out.
	 */
	public void setDataOptOut(Date dataOptOut) 
	{
		this.dataOptOut = dataOptOut;
	}
	/**
	 * @return Retorna o indMandatorio.
	 */
	public int getIndMandatorio() 
	{
		return indMandatorio;
	}
	/**
	 * @param indMandatorio Indicador de Opt-in Mandatorio.
	 */
	public void setIndMandatorio(int indMandatorio) 
	{
		this.indMandatorio = indMandatorio;
	}
	/**
	 * @return Retorna o lac.
	 */
	public int getLac() 
	{
		return lac;
	}
	/**
	 * @param lac Lac.
	 */
	public void setLac(int lac) 
	{
		this.lac = lac;
	}
	/**
	 * @return Retorna o msisdn.
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}
	/**
	 * @param msisdn Msisdn.
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	/**
	 * @return Retorna o msisdn Formatado (XX) 84XX-XXXX.
	 */
	public String getFMsisdn() 
	{
		return "(" + msisdn.substring(2,4) + ") " + msisdn.substring(4,8) + "-"+ msisdn.substring(8,12);
	}
	/**
	 * @return Retorna a coleção optInHistorico.
	 */
	public Collection getOptInHistorico() 
	{
		return optInHistorico;
	}
	/**
	 * @param optInHistorico  optInHistorico.
	 */
	public void setOptInHistorico(Collection optInHistorico) 
	{
		this.optInHistorico = optInHistorico;
	}
	/**
	 * @return Retorna as preferencias.
	 */
	public Collection getPreferencias() 
	{
		return preferencias;
	}
	/**
	 * @param preferencias preferencias.
	 */
	public void setPreferencias(Collection preferencias) 
	{
		this.preferencias = preferencias;
	}
}
