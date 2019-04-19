//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaRecargasPeriodo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;

/**
  *
  * Este arquivo contem a definição da classe de RecargasPeriodo
  * Contém os campos extraídos e retornados na consulta de recargas por periodo
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
	// Variáveis Membro
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
	 * <p><b>Método...:</b> Assinante
	 * <p><b>Descrição:</b> Construtor que inicializa todos seus atributos
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
	
	// Métodos set

	/**
	 * <p><b>Método...:</b> setEmpresa
	 * <p><b>Descrição:</b> Seta Código da Empresa
	 * @param <b>String</b>	aEmpresa	Código da Empresa
	 */
	public void setEmpresa (String aEmpresa)
	{
		this.empresa = aEmpresa;
	}
	
	/**
	 * <p><b>Método...:</b> setSistemaOrigem
	 * <p><b>Descrição:</b> Seta Código do Sistema de Origem
	 * @param <b>String</b>	aSistemaOrigem	Código do Sistema de Origem
	 */
	public void setSistemaOrigem (String aSistemaOrigem)
	{
		this.sistemaOrigem = aSistemaOrigem;
	}
	
	/**
	 * <p><b>Método...:</b> setDescRetorno
	 * <p><b>Descrição:</b> Seta Código do Processo
	 * @param <b>String</b>	aProcesso	Código do Processo
	 */
	public void setProcesso(String aProcesso) 
	{
		this.processo = aProcesso;
	}
	
	/**
	 * <p><b>Método...:</b> setData
	 * <p><b>Descrição:</b> Seta a data da consulta
	 * @param <b>String</b>	aData		Data da consulta
	 */
	public void setData (String aData)
	{
		this.data = aData;	
	}

	/**
	 * <p><b>Método...:</b> setIDRequisicao
	 * <p><b>Descrição:</b> Seta o identificador da requisição
	 * @param <b>String</b>	aIDRequisicao	Identificador da Requisição
	 */
	public void setIDRequisicao (String aIDRequisicao)
	{
		this.idRequisicao = aIDRequisicao;	
	}
	
	/**
	 * <p><b>Método...:</b> setCDATA
	 * <p><b>Descrição:</b> Seta o conteúdo do campo CDATA do XML
	 * @param <b>String</b>	aCDATA		Conteúdo do campo CDATA
	 */
	public void setCDATA (String aCDATA)
	{
		this.cdata = aCDATA;	
	}

	/**
	 * <p><b>Método...:</b> setEvento
	 * <p><b>Descrição:</b> Seta o evento realizado pela consulta
	 * @param <b>String</b>	aEvento		Evento da Consulta
	 */
	public void setEvento (String aEvento)
	{
		this.evento = aEvento;
	}
	
	/**
	 * <p><b>Método...:</b> setMSISDN
	 * <p><b>Descrição:</b> Seta o MSISDN do assinante
	 * @param <b>String</b>	aMSISDN		MSISDN do Assinante
	 */
	public void setMSISDN (String aMSISDN)
	{
		this.msisdn = aMSISDN;
	}
	
	/**
	 * <p><b>Método...:</b> setPeriodo
	 * <p><b>Descrição:</b> Seta o período a ser consultado
	 * @param <b>int</b>	aPeriodo	Período Consultado
	 */
	public void setPeriodo (int aPeriodo)
	{
		this.periodo = aPeriodo;	
	}

	/**
	 * <p><b>Método...:</b> setCodigoErro
	 * <p><b>Descrição:</b> Seta o código do erro retornado
	 * @param <b>int</b>	aCodigoErro		Código do Erro
	 */
	public void setCodigoErro (int aCodigoErro)
	{
		this.codigoErro = aCodigoErro;	
	}

	/**
	 * <p><b>Método...:</b> setDescricaoErro
	 * <p><b>Descrição:</b> Seta a descrição do erro retornado
	 * @param <b>String</b>	aDescricaoErro	Descrição do Erro Retornado
	 */
	public void setDescricaoErro (String aDescricaoErro)
	{
		this.descricaoErro = aDescricaoErro;	
	}

	/**
	 * <p><b>Método...:</b> setTotalRecargas
	 * <p><b>Descrição:</b> Seta o valor total de recargas realizadas pelo assinante
	 * @param <b>double</b>	aTotalRecargas	Valor Total de Recargas
	 */
	public void setTotalRecargas (double aTotalRecargas)
	{
		this.totalRecargas = aTotalRecargas;	
	}

	// Métodos get

	/**
	 * <p><b>Método...:</b> getEmpresa
	 * <p><b>Descrição:</b> Retorna o Código da Empresa
	 * @return <b>String</b>	empresa		Código da Empresa
	 */
	public String getEmpresa ()
	{
		return this.empresa;
	}
	
	/**
	 * <p><b>Método...:</b> getSistemaOrigem
	 * <p><b>Descrição:</b> Retorna o Código do Sistema de Origem
	 * @return <b>String</b>	sistemaOrigem	Código do Sistema de Origem
	 */
	public String getSistemaOrigem ()
	{
		return this.sistemaOrigem;
	}
	
	/**
	 * <p><b>Método...:</b> getDescRetorno
	 * <p><b>Descrição:</b> Retorna o Código do Processo
	 * @return <b>String</b>	processo	Código do Processo
	 */
	public String getProcesso() 
	{
		return this.processo;
	}
	
	/**
	 * <p><b>Método...:</b> getData
	 * <p><b>Descrição:</b> Retorna a data da consulta
	 * @return <b>String</b>	data		Data da consulta
	 */
	public String getData ()
	{
		return this.data;	
	}

	/**
	 * <p><b>Método...:</b> getIDRequisicao
	 * <p><b>Descrição:</b> Retorna o identificador da requisição
	 * @return <b>String</b>	idRequisicao	Identificador da Requisição
	 */
	public String getIDRequisicao ()
	{
	    return this.idRequisicao;	
	}
	
	/**
	 * <p><b>Método...:</b> getCDATA
	 * <p><b>Descrição:</b> Retorna o conteúdo do campo CDATA do XML
	 * @return <b>String</b>	cdata		Conteúdo do campo CDATA
	 */
	public String getCDATA ()
	{
	    return this.cdata;	
	}

	/**
	 * <p><b>Método...:</b> getEvento
	 * <p><b>Descrição:</b> Retorna o Evento da Consulta
	 * @return <b>String</b>	evento		Evento da Consulta
	 */
	public String getEvento ()
	{
	    return this.evento;
	}
	
	/**
	 * <p><b>Método...:</b> getMSISDN
	 * <p><b>Descrição:</b> Retorna o MSISDN do assinante
	 * @return <b>String</b>	msisdn		MSISDN do Assinante
	 */
	public String getMSISDN ()
	{
	    return this.msisdn;
	}
	
	/**
	 * <p><b>Método...:</b> getPeriodo
	 * <p><b>Descrição:</b> Retorna o período a ser consultado
	 * @return <b>short</b>	periodo		Período Consultado
	 */
	public int getPeriodo ()
	{
	    return this.periodo;	
	}

	/**
	 * <p><b>Método...:</b> getCodigoErro
	 * <p><b>Descrição:</b> Retorna o código do erro
	 * @return <b>short</b>	codigoErro	Código do Erro
	 */
	public int getCodigoErro ()
	{
	    return this.codigoErro;	
	}

	/**
	 * <p><b>Método...:</b> getDescricaoErro
	 * <p><b>Descrição:</b> Retorna a descrição do erro retornado
	 * @return <b>String</b>	descricaoErro		Descrição do Erro Retornado
	 */
	public String getDescricaoErro ()
	{
		return this.descricaoErro;	
	}

	/**
	 * <p><b>Método...:</b> getTotalRecargas
	 * <p><b>Descrição:</b> Retorna o valor total de recargas realizadas pelo assinante
	 * @return <b>double</b>	totalRecargas		Valor Total de Recargas
	 */
	public double getTotalRecargas ()
	{
		return this.totalRecargas;	
	}
	
	/**
	 * <p><b>Metodo...:</b> getRecargasPeriodoXML
	 * <p><b>Descrição:</b> Retorna o XML com a resposta da consulta de recargas
	 * @return <b>String</b>	retorno				XML com a Resposta à Consulta
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