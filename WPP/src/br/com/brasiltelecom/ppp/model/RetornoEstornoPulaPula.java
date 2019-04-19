package br.com.brasiltelecom.ppp.model;

//Imports Java.

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

//Imports WPP.

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 *	Classe que representa as informacoes da consulta de estorno de bonus Pula-Pula geradas pelo GPP. 
 * 
 *	@author	Daniel Ferreira
 *	@since	10/10/2005
 */
public class RetornoEstornoPulaPula 
{

	private 				short				codigoRetorno;
	private 				String				msisdn;
	private 				Date				dataInicio;
	private 				Date				dataFim;
	private 				Collection			listaEstornos;
	private					double				totalExpurgo;
	private					double				totalExpurgoSaturado;
	private					double				totalEstorno;
	private					double				totalEstornoEfetivo;
	private	static	final	DecimalFormat		conversorCodigo	= new DecimalFormat(Constantes.CODIGO_RETORNO_FORMATO);
	private	static	final	DecimalFormat		conversorDouble	= new DecimalFormat(Constantes.DOUBLE_FORMATO);
	private	static	final	PhoneNumberFormat	conversorPhone	= new PhoneNumberFormat();
	private	static	final	SimpleDateFormat	conversorDate	= new SimpleDateFormat(Constantes.DATA_FORMATO);
	
	//Constantes internas.
	
	public	static	final	int	CODIGO_RETORNO			= 0;
	public	static	final	int	MSISDN					= 1;
	public	static	final	int	DATA_INICIO				= 2;
	public	static	final	int	DATA_FIM				= 3;
	public	static	final	int	TOTAL_EXPURGO			= 4;
	public	static	final	int	TOTAL_EXPURGO_SATURADO	= 5;
	public	static	final	int	TOTAL_ESTORNO			= 6;
	public	static	final	int	TOTAL_ESTORNO_EFETIVO	= 7;
	public	static	final	int	TOTAL_EXPURGOS			= 8;
	
	//Construtoes.
	
	/**
	 *	Construtor da classe.
	 */
	public RetornoEstornoPulaPula()
	{
	    this.codigoRetorno			= -1;
	    this.msisdn					= null;
	    this.dataInicio				= null;
	    this.dataFim				= null;
	    this.listaEstornos			= null;
	    this.totalExpurgo			= 0.0;
	    this.totalExpurgoSaturado	= 0.0;
	    this.totalEstorno			= 0.0;
	    this.totalEstornoEfetivo	= 0.0;
	}
	
	//Getters.
	
	/**
	 *	Retorna o codigo de retorno da geracao do extrato.
	 *
	 *	@return		short					codigoRetorno				Codigo de retorno da geracao do extrato.
	 */
	public short getCodigoRetorno()
	{
	    return this.codigoRetorno;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		String					msisdn						MSISDN do assinante.
	 */
	public String getMsisdn()
	{
	    return this.msisdn;
	}
	
	/**
	 *	Retorna a data inicial da consulta.
	 *
	 *	@return		Date					dataInicio					Data inicial da consulta.
	 */
	public Date getDataInicio()
	{
	    return this.dataInicio;
	}
	
	/**
	 *	Retorna a data final da consulta.
	 *
	 *	@return		Date					dataFinal					Data final da consulta.
	 */
	public Date getDataFim()
	{
	    return this.dataFim;
	}
	
	/**
	 *	Retorna a lista de estornos.
	 *
	 *	@return		Collection				listaEstornos				Lista de estornos.
	 */
	public Collection getListaEstornos()
	{
	    return this.listaEstornos;
	}
	
	/**
	 *	Retorna o valor total expurgado da consulta.
	 *
	 *	@return		double					totalExpurgo				Valor total expurgado.
	 */
	public double getTotalExpurgo()
	{
	    return this.totalExpurgo;
	}
	
	/**
	 *	Retorna o valor total expurgado da consulta com o limite da promocao do assinante ultrapassado.
	 *
	 *	@return		double					totalExpurgoSaturado		Valor total expurgado com limite ultrapassado.
	 */
	public double getTotalExpurgoSaturado()
	{
	    return this.totalExpurgoSaturado;
	}
	
	/**
	 *	Retorna o valor total de estorno da consulta.
	 *
	 *	@return		double					totalEstorno				Valor total de estorno.
	 */
	public double getTotalEstorno()
	{
	    return this.totalEstorno;
	}
	
	/**
	 *	Retorna o valor total efetivamente estornado do assinante.
	 *
	 *	@return		double					totalEstornoEfetivo			Valor total efetivamente estornado.
	 */
	public double getTotalEstornoEfetivo()
	{
	    return this.totalEstornoEfetivo;
	}
	
	//Setters.
	
	/**
	 *	Atribui o codigo de retorno da geracao do extrato.
	 *
	 *	@param		short					codigoRetorno				Codigo de retorno da geracao do extrato.
	 */
	public void setCodigoRetorno(short codigoRetorno)
	{
	    this.codigoRetorno = codigoRetorno;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		String					msisdn						MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn)
	{
	    this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui a data inicial da consulta.
	 *
	 *	@param		Date					dataInicio					Data inicial da consulta.
	 */
	public void setDataInicio(Date dataInicio)
	{
	    this.dataInicio = dataInicio;
	}
	
	/**
	 *	Atribui a data final da consulta.
	 *
	 *	@param		Date					dataFinal					Data final da consulta.
	 */
	public void setDataFim(Date dataFim)
	{
	    this.dataFim = dataFim;
	}
	
	/**
	 *	Atribui a lista de estornos.
	 *
	 *	@param		Collection				listaEstornos				Lista de estornos.
	 */
	public void setListaEstornos(Collection listaEstornos)
	{
	    this.listaEstornos = listaEstornos;
	}
	
	/**
	 *	Atribui o valor total expurgado da consulta.
	 *
	 *	@param		double					totalExpurgo				Valor total expurgado.
	 */
	public void setTotalExpurgo(double totalExpurgo)
	{
	    this.totalExpurgo = totalExpurgo;
	}
	
	/**
	 *	Atribui o valor total expurgado da consulta com o limite da promocao do assinante ultrapassado.
	 *
	 *	@param		double					totalExpurgoSaturado		Valor total expurgado com limite ultrapassado.
	 */
	public void setTotalExpurgoSaturado(double totalExpurgoSaturado)
	{
	    this.totalExpurgoSaturado = totalExpurgoSaturado;
	}
	
	/**
	 *	Atribui o valor total de estorno da consulta.
	 *
	 *	@param		double					totalEstorno				Valor total de estorno.
	 */
	public void setTotalEstorno(double totalEstorno)
	{
	    this.totalEstorno = totalEstorno;
	}
	
	/**
	 *	Atribui o valor total efetivamente estornado do assinante.
	 *
	 *	@param		double					totalEstornoEfetivo			Valor total efetivamente estornado.
	 */
	public void setTotalEstornoEfetivo(double totalEstornoEfetivo)
	{
	    this.totalEstornoEfetivo = totalEstornoEfetivo;
	}
	
	//Outros metodos.
	
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		int						campo						Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		String												Valor no formato String.
	 */
	public String format(int campo)
	{
	    switch(campo)
	    {
	    	case RetornoEstornoPulaPula.CODIGO_RETORNO:
	    	{
	    	    return RetornoEstornoPulaPula.conversorCodigo.format(this.codigoRetorno); 
	    	}
	    	case RetornoEstornoPulaPula.MSISDN:
	    	{
	    	    return (this.msisdn != null) ? RetornoEstornoPulaPula.conversorPhone.format(this.msisdn) : null; 
	    	}
	    	case RetornoEstornoPulaPula.DATA_INICIO:
	    	{
	    	    return (this.dataInicio != null) ? RetornoEstornoPulaPula.conversorDate.format(this.dataInicio) : null; 
	    	}
	    	case RetornoEstornoPulaPula.DATA_FIM:
	    	{
	    	    return (this.dataFim != null) ? RetornoEstornoPulaPula.conversorDate.format(this.dataFim) : null; 
	    	}
	    	case RetornoEstornoPulaPula.TOTAL_EXPURGO:
	    	{
	    	    return RetornoEstornoPulaPula.conversorDouble.format(this.totalExpurgo);
	    	}
	    	case RetornoEstornoPulaPula.TOTAL_EXPURGO_SATURADO:
	    	{
	    	    return RetornoEstornoPulaPula.conversorDouble.format(this.totalExpurgoSaturado);
	    	}
	    	case RetornoEstornoPulaPula.TOTAL_ESTORNO:
	    	{
	    	    return RetornoEstornoPulaPula.conversorDouble.format(this.totalEstorno);
	    	}
	    	case RetornoEstornoPulaPula.TOTAL_ESTORNO_EFETIVO:
	    	{
	    	    return RetornoEstornoPulaPula.conversorDouble.format(this.totalEstornoEfetivo);
	    	}
	    	case RetornoEstornoPulaPula.TOTAL_EXPURGOS:
	    	{
	    	    return RetornoEstornoPulaPula.conversorDouble.format(this.totalExpurgo + this.totalExpurgoSaturado);
	    	}
	    	default: return null;
	    }
	}
	
}
