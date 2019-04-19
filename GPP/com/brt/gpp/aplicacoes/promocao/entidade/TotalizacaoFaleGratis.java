package com.brt.gpp.aplicacoes.promocao.entidade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorProFaleGratis;

public class TotalizacaoFaleGratis implements Totalizado, Serializable
{
	private String  			datMes;
	private String  			msisdn;
	private long				numSegundos;
	private Date				datRetiradaFGN;
	private SimpleDateFormat	sdf = new SimpleDateFormat("yyyyMM");
	TotalizadorProFaleGratis  totFGN = new TotalizadorProFaleGratis();
	
	
	public TotalizacaoFaleGratis(String datMes)
	{
		this.numSegundos = 0;
		this.datMes      = datMes;
	}
	
	public TotalizacaoFaleGratis()
	{
		this.numSegundos = 0;
		this.datMes 	 = sdf.format(Calendar.getInstance().getTime());
	}
	
	public TotalizacaoFaleGratis(Date data)
	{
		this.datMes 	 = getPeriodo(data);
		this.numSegundos = 0;
	}

	/**
	 * Metodo....:getmsisdn
	 * Descricao.:Retorna o valor de msisdn
	 * @return msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * Metodo....:setmsisdn
	 * Descricao.:Define o valor de msisdn
	 * @param msisdn o valor a ser definido para msisdn
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	/**
	 * Metodo....:getnumSegundos
	 * Descricao.:Retorna o valor de numSegundos
	 * @return numSegundos.
	 */
	public long getNumSegundos()
	{
		return numSegundos;
	}

	/**
	 * Metodo....:setnumSegundos
	 * Descricao.:Define o valor de numSegundos
	 * @param numSegundos o valor a ser definido para numSegundos
	 */
	public void setNumSegundos(long numSegundos)
	{
		this.numSegundos = numSegundos;
	}

	/**
	 * Metodo....:addSegundos
	 * Descricao.:Adiciona a quantidade de segundos ao campo numSegundos
	 * @param numSegundos - Numero de segundos a ser adicionado
	 */
	public void addSegundos(long numSegundos)
	{
		this.numSegundos += numSegundos;
	}
	
	/**
	 * Metodo....:getdatMes
	 * Descricao.:Retorna o valor de datMes
	 * @return datMes.
	 */
	public String getDatMes()
	{
		return datMes;
	}
	
	/**
	 * Metodo....:setDataMes
	 * Descricao.:Define o periodo da totalizacao
	 * @param datMes - Perido no format YYYYMM
	 */
	public void setDatMes(String datMes)
	{
		this.datMes = datMes;
	}
	
	
	
	/**
	 * Metodo....:getDataRetiradaFGN
	 * Descricao.:Retorna a data de retirada do FGN
	 */
	public Date getDatRetiradaFGN() 
	{
		return datRetiradaFGN;
	}

	/**
	 * Metodo....:setDataRetiradaFGN
	 * Descricao.:Define a data de retirada do FGN
	 * @param dataRetiradaFGN - Data de retirada do FGN
	 */
	public void setDatRetiradaFGN(Date datRetiradaFGN) 
	{
		this.datRetiradaFGN = datRetiradaFGN;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return (getDatMes()+getMsisdn()).hashCode();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if (! (obj instanceof TotalizacaoFaleGratis) )
			return false;
		
		TotalizacaoFaleGratis tot = (TotalizacaoFaleGratis)obj;
		if ( this.getDatMes().equals(tot.getDatMes()) && this.getMsisdn().equals(tot.getMsisdn()) )
				return true;
		
		return false;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getDatMes()+" - "+getMsisdn()+" - "+getNumSegundos();
	}
	
	/**
	 * Metodo....:getPeriodo
	 * Descricao.:Retorna o perido no qual a data do CDR estah envolvido
	 * @param dataCdr - Data do CDR a ser verificado o periodo
	 * @return - String no formato que deve ser o periodo da promocao
	 */
	public String getPeriodo(Date dataCdr)
	{
		return sdf.format(dataCdr);
	}
	
	/*
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado#possuiMesmoPeriodo(com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR)
	 */
	public boolean possuiMesmoPeriodo(ArquivoCDR cdr)
	{
		// Utiliza o metodo definido na classe TotalizadorFaleGratis para
		// retornar o periodo baseado na informacao de data do CDR comparada
		// ao dia de entrada na promocao do assinante
		return getDatMes().equals(totFGN.getPeriodo(cdr.getSubId(), cdr.getTimestamp()));
	}
}
