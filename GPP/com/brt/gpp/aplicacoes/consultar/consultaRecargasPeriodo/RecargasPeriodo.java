//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaRecargasPeriodo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;

/**
  *
  * Este arquivo contem a defini��o da classe de RecargasPeriodo
  * Cont�m os campos extra�dos e retornados na consulta de recargas por periodo
  * <P> Versao:        	1.0
  *
  * @Autor:            	Marcelo Alves Araujo
  * Data:               23/05/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class RecargasPeriodo
{
	// Vari�veis Membro
	private String	empresa;
	private String	sistemaOrigem;
	private String	processo;
	private String	data;
	private String	idRequisicao;
	private String	cdata;
	private String	evento;
	private String	msisdn;
	private int 	periodo;
	private int		codigoErro;
	private String	descricaoErro;
	private double	totalRecargas;
	
	
	/**
	 * <p><b>M�todo...:</b> Assinante
	 * <p><b>Descri��o:</b> Construtor que inicializa todos seus atributos
	 */
	public RecargasPeriodo()
	{
		this.empresa 		= null;
		this.sistemaOrigem	= null;
		this.processo 		= null;
		this.data 			= null;
		this.idRequisicao	= null;
		this.cdata 			= null;
		this.evento 		= null;
		this.msisdn 		= null;
		this.periodo 		= 0;
		this.codigoErro 	= Definicoes.RET_OPERACAO_OK;
		this.descricaoErro 	= null;
		this.totalRecargas 	= 0;
	}
	
	// M�todos set

	/**
	 * <p><b>M�todo...:</b> setEmpresa
	 * <p><b>Descri��o:</b> Seta C�digo da Empresa
	 * @param <b>String</b>	aEmpresa	C�digo da Empresa
	 */
	public void setEmpresa (String aEmpresa)
	{
		this.empresa = aEmpresa;
	}
	
	/**
	 * <p><b>M�todo...:</b> setSistemaOrigem
	 * <p><b>Descri��o:</b> Seta C�digo do Sistema de Origem
	 * @param <b>String</b>	aSistemaOrigem	C�digo do Sistema de Origem
	 */
	public void setSistemaOrigem (String aSistemaOrigem)
	{
		this.sistemaOrigem = aSistemaOrigem;
	}
	
	/**
	 * <p><b>M�todo...:</b> setDescRetorno
	 * <p><b>Descri��o:</b> Seta C�digo do Processo
	 * @param <b>String</b>	aProcesso	C�digo do Processo
	 */
	public void setProcesso(String aProcesso) 
	{
		this.processo = aProcesso;
	}
	
	/**
	 * <p><b>M�todo...:</b> setData
	 * <p><b>Descri��o:</b> Seta a data da consulta
	 * @param <b>String</b>	aData		Data da consulta
	 */
	public void setData (String aData)
	{
		this.data = aData;	
	}

	/**
	 * <p><b>M�todo...:</b> setIDRequisicao
	 * <p><b>Descri��o:</b> Seta o identificador da requisi��o
	 * @param <b>String</b>	aIDRequisicao	Identificador da Requisi��o
	 */
	public void setIDRequisicao (String aIDRequisicao)
	{
		this.idRequisicao = aIDRequisicao;	
	}
	
	/**
	 * <p><b>M�todo...:</b> setCDATA
	 * <p><b>Descri��o:</b> Seta o conte�do do campo CDATA do XML
	 * @param <b>String</b>	aCDATA		Conte�do do campo CDATA
	 */
	public void setCDATA (String aCDATA)
	{
		this.cdata = aCDATA;	
	}

	/**
	 * <p><b>M�todo...:</b> setEvento
	 * <p><b>Descri��o:</b> Seta o evento realizado pela consulta
	 * @param <b>String</b>	aEvento		Evento da Consulta
	 */
	public void setEvento (String aEvento)
	{
		this.evento = aEvento;
	}
	
	/**
	 * <p><b>M�todo...:</b> setMSISDN
	 * <p><b>Descri��o:</b> Seta o MSISDN do assinante
	 * @param <b>String</b>	aMSISDN		MSISDN do Assinante
	 */
	public void setMSISDN (String aMSISDN)
	{
		this.msisdn = aMSISDN;
	}
	
	/**
	 * <p><b>M�todo...:</b> setPeriodo
	 * <p><b>Descri��o:</b> Seta o per�odo a ser consultado
	 * @param <b>int</b>	aPeriodo	Per�odo Consultado
	 */
	public void setPeriodo (int aPeriodo)
	{
		this.periodo = aPeriodo;	
	}

	/**
	 * <p><b>M�todo...:</b> setCodigoErro
	 * <p><b>Descri��o:</b> Seta o c�digo do erro retornado
	 * @param <b>int</b>	aCodigoErro		C�digo do Erro
	 */
	public void setCodigoErro (int aCodigoErro)
	{
		this.codigoErro = aCodigoErro;	
	}

	/**
	 * <p><b>M�todo...:</b> setDescricaoErro
	 * <p><b>Descri��o:</b> Seta a descri��o do erro retornado
	 * @param <b>String</b>	aDescricaoErro	Descri��o do Erro Retornado
	 */
	public void setDescricaoErro (String aDescricaoErro)
	{
		this.descricaoErro = aDescricaoErro;	
	}

	/**
	 * <p><b>M�todo...:</b> setTotalRecargas
	 * <p><b>Descri��o:</b> Seta o valor total de recargas realizadas pelo assinante
	 * @param <b>double</b>	aTotalRecargas	Valor Total de Recargas
	 */
	public void setTotalRecargas (double aTotalRecargas)
	{
		this.totalRecargas = aTotalRecargas;	
	}

	// M�todos get

	/**
	 * <p><b>M�todo...:</b> getEmpresa
	 * <p><b>Descri��o:</b> Retorna o C�digo da Empresa
	 * @return <b>String</b>	empresa		C�digo da Empresa
	 */
	public String getEmpresa ()
	{
		return this.empresa;
	}
	
	/**
	 * <p><b>M�todo...:</b> getSistemaOrigem
	 * <p><b>Descri��o:</b> Retorna o C�digo do Sistema de Origem
	 * @return <b>String</b>	sistemaOrigem	C�digo do Sistema de Origem
	 */
	public String getSistemaOrigem ()
	{
		return this.sistemaOrigem;
	}
	
	/**
	 * <p><b>M�todo...:</b> getDescRetorno
	 * <p><b>Descri��o:</b> Retorna o C�digo do Processo
	 * @return <b>String</b>	processo	C�digo do Processo
	 */
	public String getProcesso() 
	{
		return this.processo;
	}
	
	/**
	 * <p><b>M�todo...:</b> getData
	 * <p><b>Descri��o:</b> Retorna a data da consulta
	 * @return <b>String</b>	data		Data da consulta
	 */
	public String getData ()
	{
		return this.data;	
	}

	/**
	 * <p><b>M�todo...:</b> getIDRequisicao
	 * <p><b>Descri��o:</b> Retorna o identificador da requisi��o
	 * @return <b>String</b>	idRequisicao	Identificador da Requisi��o
	 */
	public String getIDRequisicao ()
	{
	    return this.idRequisicao;	
	}
	
	/**
	 * <p><b>M�todo...:</b> getCDATA
	 * <p><b>Descri��o:</b> Retorna o conte�do do campo CDATA do XML
	 * @return <b>String</b>	cdata		Conte�do do campo CDATA
	 */
	public String getCDATA ()
	{
	    return this.cdata;	
	}

	/**
	 * <p><b>M�todo...:</b> getEvento
	 * <p><b>Descri��o:</b> Retorna o Evento da Consulta
	 * @return <b>String</b>	evento		Evento da Consulta
	 */
	public String getEvento ()
	{
	    return this.evento;
	}
	
	/**
	 * <p><b>M�todo...:</b> getMSISDN
	 * <p><b>Descri��o:</b> Retorna o MSISDN do assinante
	 * @return <b>String</b>	msisdn		MSISDN do Assinante
	 */
	public String getMSISDN ()
	{
	    return this.msisdn;
	}
	
	/**
	 * <p><b>M�todo...:</b> getPeriodo
	 * <p><b>Descri��o:</b> Retorna o per�odo a ser consultado
	 * @return <b>short</b>	periodo		Per�odo Consultado
	 */
	public int getPeriodo ()
	{
	    return this.periodo;	
	}

	/**
	 * <p><b>M�todo...:</b> getCodigoErro
	 * <p><b>Descri��o:</b> Retorna o c�digo do erro
	 * @return <b>short</b>	codigoErro	C�digo do Erro
	 */
	public int getCodigoErro ()
	{
	    return this.codigoErro;	
	}

	/**
	 * <p><b>M�todo...:</b> getDescricaoErro
	 * <p><b>Descri��o:</b> Retorna a descri��o do erro retornado
	 * @return <b>String</b>	descricaoErro		Descri��o do Erro Retornado
	 */
	public String getDescricaoErro ()
	{
		return this.descricaoErro;	
	}

	/**
	 * <p><b>M�todo...:</b> getTotalRecargas
	 * <p><b>Descri��o:</b> Retorna o valor total de recargas realizadas pelo assinante
	 * @return <b>double</b>	totalRecargas		Valor Total de Recargas
	 */
	public double getTotalRecargas ()
	{
		return this.totalRecargas;	
	}
	
	/**
	 * <p><b>Metodo...:</b> getRecargasPeriodoXML
	 * <p><b>Descri��o:</b> Retorna o XML com a resposta da consulta de recargas
	 * @return <b>String</b>	retorno				XML com a Resposta � Consulta
	 * Formato do XML
	 * 	<root>
	 * 		<dados>
	 * 			<evento>RqConsultaRecargaPeriodoRsp</evento>
	 * 			<msisdn>6184018401</msisdn>
	 * 			<periodo>120</periodo>
	 * 		</dados>
	 * 		<retorno>	
	 * 			<valor_inserido>150.00</valor_inserido>
	 * 		</retorno>
	 * 	</root>
	 */
	public String getRecargasPeriodoXML ()
	{
	    // 
	    GerarXML geradorCDATA 	= new GerarXML("root");
		
		DecimalFormat df = new DecimalFormat("#####0.00", new DecimalFormatSymbols(Locale.FRENCH));

		geradorCDATA.abreNo("dados");
		geradorCDATA.adicionaTag("evento" , this.evento);
		geradorCDATA.adicionaTag("msisdn" , this.msisdn);
		geradorCDATA.adicionaTag("periodo", String.valueOf(this.periodo));
		geradorCDATA.fechaNo();
		
		geradorCDATA.abreNo("retorno");
		geradorCDATA.adicionaTag("valor_inserido", df.format(this.totalRecargas));
		geradorCDATA.fechaNo();
		
		return this.cdata = geradorCDATA.getXML();  
	}
}