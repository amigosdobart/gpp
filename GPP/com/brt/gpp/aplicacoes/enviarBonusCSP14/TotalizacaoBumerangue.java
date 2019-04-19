package com.brt.gpp.aplicacoes.enviarBonusCSP14;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;

public class TotalizacaoBumerangue implements Totalizado
{
	private String  			datMes;
	private String  			msisdn;
	private int					planoPreco;
	private long				numSegundos;
	private SimpleDateFormat	sdf = new SimpleDateFormat("yyyyMM");
	
	public TotalizacaoBumerangue(String datMes)
	{
		this.numSegundos = 0;
		this.datMes      = datMes;
	}
	
	public TotalizacaoBumerangue(Date data)
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
	 * Metodo....:getplanoPreco
	 * Descricao.:Retorna o valor de planoPreco
	 * @return planoPreco.
	 */
	public int getPlanoPreco()
	{
		return planoPreco;
	}

	/**
	 * Metodo....:setplanoPreco
	 * Descricao.:Define o valor de planoPreco
	 * @param planoPreco o valor a ser definido para planoPreco
	 */
	public void setPlanoPreco(int planoPreco)
	{
		this.planoPreco = planoPreco;
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
		if (! (obj instanceof TotalizacaoBumerangue) )
			return false;
		
		TotalizacaoBumerangue tot = (TotalizacaoBumerangue)obj;
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataCdr);
		if (cal.get(Calendar.DAY_OF_MONTH) >= 28)
			cal.add(Calendar.MONTH,1);
		
		return sdf.format(cal.getTime());
	}
	
	/*
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado#possuiMesmoPeriodo(com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR)
	 */
	public boolean possuiMesmoPeriodo(ArquivoCDR cdr)
	{
		return getDatMes().equals(getPeriodo(cdr.getTimestamp()));
	}
}
