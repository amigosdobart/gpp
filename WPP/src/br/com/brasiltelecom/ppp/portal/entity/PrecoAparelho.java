package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * Classe que implementa a entidade que mapeia
 * a tabela HSID_PRECO_APARELHO
 * 
 * @author	Marcelo Alves Araujo
 * @since	10/11/2006
 */
public class PrecoAparelho
{
	private int		codigoSAP;
	private int		codigoNacional;
	private Date	dataInicioValidade;
	private Date	dataFimValidade;
	private double	valorPreco;
	
	/**
     * @return codigoNacional Código Nacional
     */
    public int getCodigoNacional()
    {
    	return codigoNacional;
    }
    
	/**
     * @param codigoNacional Código Nacional
     */
    public void setCodigoNacional(int codigoNacional)
    {
    	this.codigoNacional = codigoNacional;
    }
    
	/**
     * @return codigoSAP Código SAP
     */
    public int getCodigoSAP()
    {
    	return codigoSAP;
    }
    
	/**
     * @param codigoSAP Código SAP
     */
    public void setCodigoSAP(int codigoSAP)
    {
    	this.codigoSAP = codigoSAP;
    }
    
	/**
     * @return dataFimValidade Data final de vigência do valor
     */
    public Date getDataFimValidade()
    {
    	return dataFimValidade;
    }
    
	/**
     * @param dataFimValidade Data final de vigência do valor
     */
    public void setDataFimValidade(Date dataFimValidade)
    {
    	this.dataFimValidade = dataFimValidade;
    }
    
	/**
     * @return dataInicioValidade Data inicial de vigência do valor
     */
    public Date getDataInicioValidade()
    {
    	return dataInicioValidade;
    }
    
	/**
     * @param dataInicioValidade Data inicial de vigência do valor
     */
    public void setDataInicioValidade(Date dataInicioValidade)
    {
    	this.dataInicioValidade = dataInicioValidade;
    }
    
	/**
     * @return valorPreco Preço do aparelho
     */
    public double getValorPreco()
    {
    	return valorPreco;
    }
    
	/**
     * @param valorPreco Preço do aparelho
     */
    public void setValorPreco(double valorPreco)
    {
    	this.valorPreco = valorPreco;
    }	
}