package com.brt.gpp.aplicacoes.promocao.entidade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;

public class TotalizacaoConsumo implements Totalizado, Serializable
{
	private String  			datMes;
	private String  			idtMsisdn;
	private long				numSegundosBonusOffnet;
	private SimpleDateFormat	sdf = new SimpleDateFormat("yyyyMM");
    private SimpleDateFormat    conversorDatMes;
	
    //Constantes internas    
    public static final int NUM_SEGUNDOS_BONUS_OFFNET   = 1;
	
	public TotalizacaoConsumo(String datMes)
	{
		this.datMes               = datMes;
        this.conversorDatMes      = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
	}
	
	public TotalizacaoConsumo()
	{
		this.datMes 	         = sdf.format(Calendar.getInstance().getTime());
        this.conversorDatMes     = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
	}
	
	public TotalizacaoConsumo(Date data)
	{
		this.datMes 	         = getPeriodo(data);
        this.conversorDatMes     = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
	}

	/**
	 * Retorna o valor de msisdn
	 */
	public String getIdtMsisdn()
	{
		return idtMsisdn;
	}

	/**
	 * Define o valor de msisdn
	 * @param msisdn valor a ser definido para msisdn
	 */
	public void setIdtMsisdn(String msisdn)
	{
		this.idtMsisdn = msisdn;
	}

	/**
	 * Retorna o valor de numSegundosBonusOffnet
	 */
	public long getNumSegundosBonusOffnet()
	{
		return numSegundosBonusOffnet;
	}

	/**
	 * Define o valor de numSegundosBonusOffnet
	 * @param numSegundosBonusOffnet valor a ser definido para numSegundosBonusOffnet
	 */
	public void setNumSegundosBonusOffnet(long numSegundosBonusOffnet)
	{
		this.numSegundosBonusOffnet = numSegundosBonusOffnet;
	}

	/**
	 * Retorna o valor de datMes
	 */
	public String getDatMes()
	{
		return datMes;
	}
	
	/**
	 * Define o periodo da totalizacao
	 * @param datMes Perido no format YYYYMM
	 */
	public void setDatMes(String datMes)
	{
		this.datMes = datMes;
	}

    /**
     *  Adiciona o campo com o numero de segundos. 
     *
     *  @param      campo                       Campo selecionado.
     *  @param      segundos                    Numero de segundos a ser adicionado.
     */
    public void add(int campo, long segundos)
    {
        switch(campo)
        {
        case TotalizacaoConsumo.NUM_SEGUNDOS_BONUS_OFFNET:
            this.numSegundosBonusOffnet += segundos;
            break;
        }
    }

    /**
     *  Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
     * 
     *  @param      campo                       Campo selecionado. Se o campo for invalido, retorna NULL.
     *  @return     Valor no formato String.
     */
    public String format(int campo)
    {
        switch(campo)
        {
        case TotalizacaoConsumo.NUM_SEGUNDOS_BONUS_OFFNET:
            return GPPData.segundosParaHoras(this.numSegundosBonusOffnet);
            
        default: return null;
        }
    }
    
	public int hashCode()
	{
	    StringBuffer result = new StringBuffer();
        
        result.append(this.getClass().getName());
        result.append("||");
        result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
        result.append("||");
        result.append((this.datMes != null) ? this.datMes : "NULL");
        
        return result.toString().hashCode();
	}
	
	public boolean equals(Object object)
	{
        if(object == null)
        {
            return false;
        }
        
        if(!(object instanceof TotalizacaoConsumo))
        {
            return false;
        }
        
        if(this.hashCode() != ((TotalizacaoConsumo)object).hashCode())
        {
            return false;
        }
        
        return true;
	}
	
	public String toString()
	{
	    StringBuffer result = new StringBuffer();
        
        result.append("MSISDN: ");
        result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
        result.append(" - ");
        result.append("Mes de Concessao: ");
        result.append((this.datMes != null) ? this.datMes : "NULL");
        result.append(" - ");
        result.append("Consumo de bonus em ligações offnet: ");
        result.append(this.format(TotalizacaoConsumo.NUM_SEGUNDOS_BONUS_OFFNET));
        
        return result.toString();
	}
	
	/**
	 * Retorna o perido no qual a data do CDR estah envolvido
	 * @param dataCdr Data do CDR a ser verificado o periodo
	 * @return String no formato que deve ser o periodo da promocao
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
        return getDatMes().equals(conversorDatMes.format(cdr.getTimestamp()));
	}
}
