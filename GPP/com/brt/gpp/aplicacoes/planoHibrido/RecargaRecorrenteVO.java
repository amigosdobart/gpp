package com.brt.gpp.aplicacoes.planoHibrido;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;

/**
 * Entidade representativa da tabela TBL_INT_RECARGA_RECORRENTE
 * com adaptacoes
 * 
 * @author Joao Paulo Galvagni Jr.
 * @since  01/04/2008
 * @modify Alteracoes para contemplar a nova Promocao do Dia das Maes 2008
 *
 */
public class RecargaRecorrenteVO
{
    private String 	msisdn;
    private String 	tipoRecarga;
    private double 	valorRecarga;
    private Date 	dataRecarga;
    private int 	codRecarga;
    private Date 	dataSolicitacao;
    private int 	tipoControle;
    private String 	contrato;
    
    private CodFaturamento codFaturamento;
    private double 		   vlrBonusFaleMais;
    private int 		   qtdMinutos;
    
    private double	valorBonusOnNet; // Valor do Bonus Unico eh setado aqui tb
    private double	valorBonusOffNet;
    private double	valorBonusRecarga;
    
    public String toString()
    {
    	return "[msisdn:"+msisdn+"];" +
    		   "[codRecarga:"+codRecarga+"];" +
    		   "[tipoControle:"+(tipoControle==0?"Safra Antiga":"Safra Nova") + "];" +
    		   "[vlrBonusOnNet:"+valorBonusOnNet+"];" +
    		   "[vlrBonusOffNet:"+valorBonusOffNet+"];" +
    		   "[vlrBonusRecarga:"+valorBonusRecarga+"];" +
    		   "[vlrBonusUnico:"+codFaturamento.getValorNovoBonus()+"]";
    }
    
    /**
     * @return Returns the codRecarga.
     */
    public int getCodRecarga()
    {
        return codRecarga;
    }
    
    /**
     * @param codRecarga The codRecarga to set.
     */
    public void setCodRecarga(int codRecarga)
    {
        this.codRecarga = codRecarga;
    }
    
    /**
     * @return Returns the dataRecarga.
     */
    public Date getDataRecarga()
    {
        return dataRecarga;
    }
    
    /**
     * @param dataRecarga The dataRecarga to set.
     */
    public void setDataRecarga(Date dataRecarga)
    {
        this.dataRecarga = dataRecarga;
    }
    
    /**
     * @return Returns the dataSolicitacao.
     */
    public Date getDataSolicitacao()
    {
        return dataSolicitacao;
    }
    
    /**
     * @param dataSolicitacao The dataSolicitacao to set.
     */
    public void setDataSolicitacao(Date dataSolicitacao)
    {
        this.dataSolicitacao = dataSolicitacao;
    }
    
    /**
     * @return Returns the valorRecarga.
     */
    public double getValorRecarga()
    {
        return valorRecarga;
    }

    /**
     * @param valorRecarga The valorRecarga to set.
     */
    public void setValorRecarga(double valorRecarga)
    {
        this.valorRecarga = valorRecarga;
    }
    
    /**
     * @return Returns the mSISDN.
     */
    public String getMsisdn()
    {
        return msisdn;
    }
    
    /**
     * @param msisdn The mSISDN to set.
     */
    public void setMsisdn(String msisdn)
    {
        this.msisdn = msisdn;
    }
    
    /**
     * @return Returns the tipoRecarga.
     */
    public String getTipoRecarga()
    {
        return tipoRecarga;
    }
    
    /**
     * @param tipoRecarga The tipoRecarga to set.
     */
    public void setTipoRecarga(String tipoRecarga)
    {
        this.tipoRecarga = tipoRecarga;
    }
    
    /**
     * @return Returns the tipoControle.
     */
    public int getTipoControle()
    {
        return tipoControle;
    }
    
    /**
     * @param tipoRecarga The tipoRecarga to set.
     */
    public void setTipoControle(int tipoControle)
    {
        this.tipoControle = tipoControle;
    }
    
	/**
     * @return contrato contrato SAC
     */
    public String getContrato()
    {
    	return contrato;
    }
    
	/**
     * @param contrato contrato SAC
     */
    public void setContrato(String contrato)
    {
    	this.contrato = contrato;
    }
    
	public CodFaturamento getCodFaturamento()
	{
		return codFaturamento;
	}
	
	public void setCodFaturamento(CodFaturamento codFaturamento)
	{
		this.codFaturamento = codFaturamento;
	}
	
	public int getQtdMinutos()
	{
		return qtdMinutos;
	}
	
	public void setQtdMinutos(int qtdMinutos)
	{
		this.qtdMinutos = qtdMinutos;
	}
	
	public double getVlrBonusFaleMais()
	{
		return vlrBonusFaleMais;
	}
	
	public void setVlrBonusFaleMais(double vlrBonusFaleMais)
	{
		this.vlrBonusFaleMais = vlrBonusFaleMais;
	}
	
	/**
	 * @return the nsuInstituicao
	 */
	public String getNsuInstituicao()
	{
		if (this.contrato != null && !"".equals(this.contrato))
			return this.getContrato()+ "|" + this.getCodRecarga();
		
		return "";
	}
	
	/**
	 * @return the valorBonusOffNet
	 */
	public double getValorBonusOffNet()
	{
		return valorBonusOffNet;
	}
	
	/**
	 * @param valorBonusOffNet the valorBonusOffNet to set
	 */
	public void setValorBonusOffNet(double valorBonusOffNet)
	{
		this.valorBonusOffNet = valorBonusOffNet;
	}
	
	/**
	 * @param valorBonusOffNet the valorBonus to sum
	 */
	public void somaValorBonusOffNet(double valorBonus)
	{
		this.valorBonusOffNet += valorBonus;
	}
	
	/**
	 * @return the valorBonusOnNet
	 */
	public double getValorBonusOnNet()
	{
		return valorBonusOnNet;
	}
	
	/**
	 * @param valorBonusOnNet the valorBonusOnNet to set
	 */
	public void setValorBonusOnNet(double valorBonusOnNet)
	{
		this.valorBonusOnNet = valorBonusOnNet;
	}
	
	/**
	 * @param valorBonusOnNet the valorBonus to sum
	 */
	public void somaValorBonusOnNet(double valorBonus)
	{
		this.valorBonusOnNet += valorBonus;
	}
	
	/**
	 * @return the valorBonusRecarga
	 */
	public double getValorBonusRecarga()
	{
		return valorBonusRecarga;
	}
	
	/**
	 * @param valorBonusRecarga the valorBonusRecarga to set
	 */
	public void setValorBonusRecarga(double valorBonusRecarga)
	{
		this.valorBonusRecarga = valorBonusRecarga;
	}
	
	/**
	 * @param valorBonusRecarga the valorBonus to sum
	 */
	public void somaValorBonusRecarga(double valorBonus)
	{
		this.valorBonusRecarga += valorBonus;
	}
	
	public void somaValorBonusByTipoBonificacao(BonificacaoPulaPula bonificacao, double valorBonificacao)
	{
		switch(bonificacao.getTipoBonificacao().getIdTipoBonificacao())
		{
			case PromocaoTipoBonificacao.PULA_PULA_UNICO:
			{
				this.somaValorBonusOnNet(valorBonificacao);
				this.codFaturamento.setValorNovoBonus(valorBonificacao);
				break;
			}
			case PromocaoTipoBonificacao.PULA_PULA_ONNET:
			{
				this.somaValorBonusOnNet(valorBonificacao);
				break;
			}
			case PromocaoTipoBonificacao.PULA_PULA_OFFNET:
			{
				this.somaValorBonusOffNet(valorBonificacao);
				break;
			}
			case PromocaoTipoBonificacao.PULA_PULA_RECARGA:
			{
				this.somaValorBonusRecarga(valorBonificacao);
				break;
			}
		}
	}
}