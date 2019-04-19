package com.brt.gpp.aplicacoes.aprovisionar.mms;

public class EnvioAssinantesMMSVO
{
    private String msisdn;
    private String categoria;
    private String operacao;
    
    /**
     * @return Returns the categoria.
     */
    public String getCategoria()
    {
        return categoria;
    }
    /**
     * @param categoria The categoria to set.
     */
    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
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
    /**
     * @return Returns the operacao.
     */
    public String getOperacao()
    {
        return operacao;
    }
    /**
     * @param operacao The operacao to set.
     */
    public void setOperacao(String operacao)
    {
        this.operacao = operacao;
    }
}
