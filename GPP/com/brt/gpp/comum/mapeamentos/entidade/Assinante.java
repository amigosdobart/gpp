package com.brt.gpp.comum.mapeamentos.entidade;

import java.util.Collection;
import java.util.Date;

/**
 *  Classe que representa a entidade da tabela TBL_APR_ASSINANTE.
 * 
 *  @author Bernardo Vergne Dias
 *  Data: 21/02/2008
 *  
 */
public class Assinante
{
    private String msisdn;
    private Date dataAtivacao;    
    private Date dataUltimaRecarga;
    private Date dataCongelamento;    
    private String imisi;
    private PlanoPreco planoPreco;
    
    /** lista de <code>SaldoAssinante</code> */
    private Collection saldosAssinante;

    public Date getDataAtivacao()
    {
        return dataAtivacao;
    }

    public void setDataAtivacao(Date dataAtivacao)
    {
        this.dataAtivacao = dataAtivacao;
    }

    public Date getDataCongelamento()
    {
        return dataCongelamento;
    }

    public void setDataCongelamento(Date dataCongelamento)
    {
        this.dataCongelamento = dataCongelamento;
    }

    public Date getDataUltimaRecarga()
    {
        return dataUltimaRecarga;
    }

    public void setDataUltimaRecarga(Date dataUltimaRecarga)
    {
        this.dataUltimaRecarga = dataUltimaRecarga;
    }

    public String getMsisdn()
    {
        return msisdn;
    }

    public void setMsisdn(String msisdn)
    {
        this.msisdn = msisdn;
    }

    public String getImisi()
    {
        return imisi;
    }

    public void setImisi(String imisi)
    {
        this.imisi = imisi;
    }

    public PlanoPreco getPlanoPreco()
    {
        return planoPreco;
    }

    public void setPlanoPreco(PlanoPreco planoPreco)
    {
        this.planoPreco = planoPreco;
    }

    public Collection getSaldosAssinante()
    {
        return saldosAssinante;
    }

    public void setSaldosAssinante(Collection saldosAssinante)
    {
        this.saldosAssinante = saldosAssinante;
    } 
    
    
    
}
