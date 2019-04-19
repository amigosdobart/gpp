package com.brt.gpp.aplicacoes.aprovisionar.mms;

import java.sql.Timestamp;

public class ImportacaoAssinantesMMSVO
{
    private String msisdn;
    private Timestamp dataInclusao;
    private String modelo;
    
    
    /**
     * @return Returns the dataInclusao.
     */
    public Timestamp getDataInclusao()
    {
        return dataInclusao;
    }
    /**
     * @param dataInclusao The dataInclusao to set.
     */
    public void setDataInclusao(Timestamp dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }
    /**
     * @return Returns the modelo.
     */
    public String getModelo()
    {
        return modelo;
    }
    /**
     * @param modelo The modelo to set.
     */
    public void setModelo(String modelo)
    {
        this.modelo = modelo;
    }
    /**
     * @return Returns the msisdn.
     */
    public String getMsisdn()
    {
        return msisdn;
    }
    /**
     * @param msisdn The msisdn to set.
     */
    public void setMsisdn(String msisdn)
    {
        this.msisdn = msisdn;
    }
}