package br.com.brasiltelecom.ppp.portal.entity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe de mapeamento da tabela tbl_int_recarga_recorrente
 * 
 * @author 	Marcelo Alves Araujo
 * @since	12/04/2006
 *
 */
public class InterfaceRecargaRecorrente
{
	private Date dataProcessamento;
	private String msisdn;	 
	private char tipoEnvio;	 
	private Double valorRecarga;	 
	private Date dataRecarga;	 
	private char statusProcessamento;	 
	private String codigoRetorno;	 
	private String desRetorno;	 
	private String filial;	 
	private long contrato;	 
	private int numRecarga;
	private String codigoFaturamento;
    private Double valorBonusOnnet;
    private Double valorBonusOffnet;
    private Double valorBonusRecarga;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private DecimalFormat df = new DecimalFormat("#,##0.00"); 
	
	//Get
	public String getCodigoRetorno()
	{
		return codigoRetorno;
	}
	public long getContrato()
	{
		return contrato;
	}
	public Date getDataProcessamento()
	{
		return dataProcessamento;
	}
	public Date getDataRecarga()
	{
		return dataRecarga;
	}
	public String getFDataRecarga()
	{
		return sdf.format(dataRecarga);
	}
	public String getDesRetorno()
	{
		return desRetorno;
	}
	public String getFilial()
	{
		return filial;
	}
	public String getMsisdn()
	{
		return msisdn;
	}
	public int getNumRecarga()
	{
		return numRecarga;
	}
	public char getStatusProcessamento()
	{
		return statusProcessamento;
	}
	public char getTipoEnvio()
	{
		return tipoEnvio;
	}
	public Double getValorRecarga()
	{
		return valorRecarga;
	}
	public String getFValorRecarga()
	{
		return df.format(valorRecarga);
	}
	public String getCodigoFaturamento()
	{
		return codigoFaturamento;
	}
	
	// Set
	public void setCodigoRetorno(String codigoRetorno)
	{
		this.codigoRetorno = codigoRetorno;
	}
	public void setContrato(long contrato)
	{
		this.contrato = contrato;
	}
	public void setDataProcessamento(Date dataProcessamento)
	{
		this.dataProcessamento = dataProcessamento;
	}
	public void setDataRecarga(Date dataRecarga)
	{
		this.dataRecarga = dataRecarga;
	}
	public void setDesRetorno(String desRetorno)
	{
		this.desRetorno = desRetorno;
	}
	public void setFilial(String filial)
	{
		this.filial = filial;
	}
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	public void setNumRecarga(int numRecarga)
	{
		this.numRecarga = numRecarga;
	}
	public void setStatusProcessamento(char statusProcessamento)
	{
		this.statusProcessamento = statusProcessamento;
	}
	public void setTipoEnvio(char tipoEnvio)
	{
		this.tipoEnvio = tipoEnvio;
	}
	public void setValorRecarga(Double valorRecarga)
	{
		this.valorRecarga = valorRecarga;
	}	
	public void setCodigoFaturamento(String codigoFaturamento)
	{
		this.codigoFaturamento = codigoFaturamento;
	}
    public Double getValorBonusOffnet()
    {
        return valorBonusOffnet;
    }
    public void setValorBonusOffnet(Double valorBonusOffnet)
    {
        this.valorBonusOffnet = valorBonusOffnet;
    }
    public Double getValorBonusOnnet()
    {
        return valorBonusOnnet;
    }
    public void setValorBonusOnnet(Double valorBonusOnnet)
    {
        this.valorBonusOnnet = valorBonusOnnet;
    }
    public Double getValorBonusRecarga()
    {
        return valorBonusRecarga;
    }
    public void setValorBonusRecarga(Double valorBonusRecarga)
    {
        this.valorBonusRecarga = valorBonusRecarga;
    }
    
}
