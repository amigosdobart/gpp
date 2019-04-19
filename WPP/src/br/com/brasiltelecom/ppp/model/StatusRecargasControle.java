package br.com.brasiltelecom.ppp.model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusRecargasControle
{
	Date	dataRecarga;
	String	statusRecarga;
	String	decricaoRetorno;
	double	valorPrincipal;
    double  valorBonusOnnet;
    double  valorBonusOffnet;
    double  valorBonusRecarga;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private DecimalFormat df = new DecimalFormat("#,##0.00"); 
	
	// Gets
	public Date getDataRecarga()
	{
		return dataRecarga;
	}
	public String getDecricaoRetorno()
	{
		return decricaoRetorno;
	}
	public String getStatusRecarga()
	{
		return statusRecarga;
	}
	public double getValorBonusOnnet()
	{
		return valorBonusOnnet;
	}
    public double getValorBonusOffnet()
    {
        return valorBonusOffnet;
    }
    public double getValorBonusRecarga()
    {
        return valorBonusRecarga;
    }
	public double getValorPrincipal()
	{
		return valorPrincipal;
	}	
	public String getSValorBonusOnnet()
	{
		return df.format(valorBonusOnnet);
	}
    public String getSValorBonusOffnet()
    {
        return df.format(valorBonusOffnet);
    }
    public String getSValorBonusRecarga()
    {
        return df.format(valorBonusRecarga);
    }
	public String getSValorPrincipal()
	{
		return df.format(valorPrincipal);
	}
	public String getSDataRecarga()
	{
		return sdf.format(dataRecarga);
	}
	
	// Sets
	public void setDataRecarga(Date dataRecarga)
	{
		this.dataRecarga = dataRecarga;
	}
	public void setDecricaoRetorno(String decricaoRetorno)
	{
		this.decricaoRetorno = decricaoRetorno;
	}
	public void setStatusRecarga(String statusRecarga)
	{
		this.statusRecarga = statusRecarga;
	}
	public void setValorBonusOnnet(double valorBonusOnnet)
	{
		this.valorBonusOnnet = valorBonusOnnet;
	}
    public void setValorBonusOffnet(double valorBonusOffnet)
    {
        this.valorBonusOffnet = valorBonusOffnet;
    }
    public void setValorBonusRecarga(double valorBonusRecarga)
    {
        this.valorBonusRecarga = valorBonusRecarga;
    }
	public void setSValorBonusOnnet(String valorBonusOnnet) throws ParseException
	{
		this.valorBonusOnnet = df.parse(valorBonusOnnet).doubleValue();
	}
    public void setSValorBonusOffnet(String valorBonusOffnet) throws ParseException
    {
        this.valorBonusOffnet= df.parse(valorBonusOffnet).doubleValue();
    }
    public void setSValorBonusRecarga(String valorBonusRecarga) throws ParseException
    {
        this.valorBonusRecarga = df.parse(valorBonusRecarga).doubleValue();
    }
	public void setValorPrincipal(double valorPrincipal)
	{
		this.valorPrincipal = valorPrincipal;
	}	
}