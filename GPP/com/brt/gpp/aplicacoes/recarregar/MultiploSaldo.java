//Definicao do Pacote
package com.brt.gpp.aplicacoes.recarregar;

/**
  *
  * Este arquivo contem a definicao da classe de Dados de Recarga 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Daniel Cintra Abib
  * Data:               07/09/2002
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class MultiploSaldo
{
	// Valores da recarga
	double	creditoPrincipal;
	double	creditoBonus;
	double	creditoSms;
	double	creditoDados;

	// Valor efetivamente pago
	double	valorPago;

	// Dias de expiracao
	short	diasExpPrincipal;
	short	diasExpBonus;
	short	diasExpSms;
	short	diasExpDados;
	
	// Datas de expiracao
	String	dataExpPrincipal;
	String	dataExpBonus;
	String	dataExpSms;
	String	dataExpDados;
	
	/**
	 * Metodo...: MultiploSaldo
	 * Descricao: Construtor que inicializa os creditos/dias de expiração com zero e 
	 * as datas de expiração com null
	 */
	public MultiploSaldo()
	{
		this.creditoPrincipal = 0;
		this.creditoBonus = 0;
		this.creditoSms = 0;
		this.creditoDados = 0;
		
		this.diasExpPrincipal = 0;
		this.diasExpBonus = 0;
		this.diasExpSms = 0;
		this.diasExpDados = 0;
		
		// Datas de expiracao
		this.dataExpPrincipal = null;
		this.dataExpBonus = null;
		this.dataExpSms = null;
		this.dataExpDados = null;
	}
	
	public void setMultiploSaldo ( MultiploSaldo aMultiploSaldo )
	{
		this.setCreditoPrincipal(aMultiploSaldo.getCreditoPrincipal());
		this.setCreditoBonus(aMultiploSaldo.getCreditoBonus());
		this.setCreditoSms(aMultiploSaldo.getCreditoSms());
		this.setCreditoDados(aMultiploSaldo.getCreditoDados());

		// Valor efetivamente pago
		this.setValorPago (aMultiploSaldo.getValorPago());

		// Dias de expiracao
		this.setDiasExpPrincipal(aMultiploSaldo.getDiasExpPrincipal());
		this.setDiasExpBonus(aMultiploSaldo.getDiasExpBonus());
		this.setDiasExpSms(aMultiploSaldo.getDiasExpSms());
		this.setDiasExpDados(aMultiploSaldo.getDiasExpDados());
		
		// Datas de expiracao
		this.setDataExpPrincipal(aMultiploSaldo.getDataExpPrincipal());
		this.setDataExpBonus(aMultiploSaldo.getDataExpBonus());
		this.setDataExpSms(aMultiploSaldo.getDataExpSms());
		this.setDataExpDados(aMultiploSaldo.getDataExpDados());;
	}
	/**
	 * @return Returns the creditoBonus.
	 */
	public double getCreditoBonus() 
	{
		return creditoBonus;
	}
	
	/**
	 * @param creditoBonus The creditoBonus to set.
	 */
	public void setCreditoBonus(double creditoBonus) 
	{
		this.creditoBonus = creditoBonus;
	}
	
	/**
	 * @return Returns the creditoDados.
	 */
	public double getCreditoDados() 
	{
		return creditoDados;
	}
	
	/**
	 * @param creditoDados The creditoGprs to set.
	 */
	public void setCreditoDados(double creditoDados) 
	{
		this.creditoDados = creditoDados;
	}
	
	/**
	 * @return Returns the creditoPrincipal.
	 */
	public double getCreditoPrincipal() 
	{
		return creditoPrincipal;
	}
	
	/**
	 * @param creditoPrincipal The creditoPrincipal to set.
	 */
	public void setCreditoPrincipal(double creditoPrincipal) 
	{
		this.creditoPrincipal = creditoPrincipal;
	}
	
	/**
	 * @return Returns the creditoSms.
	 */
	public double getCreditoSms() 
	{
		return creditoSms;
	}
	
	/**
	 * @param creditoSms The creditoSms to set.
	 */
	public void setCreditoSms(double creditoSms) 
	{
		this.creditoSms = creditoSms;
	}
	
	/**
	 * @return Returns the dataExpBonus.
	 */
	public String getDataExpBonus() 
	{
		return dataExpBonus;
	}
	
	/**
	 * @param dataExpBonus The dataExpBonus to set.
	 */
	public void setDataExpBonus(String dataExpBonus) 
	{
		this.dataExpBonus = dataExpBonus;
	}
	
	/**
	 * @return Returns the dataExpDados.
	 */
	public String getDataExpDados() 
	{
		return dataExpDados;
	}
	
	/**
	 * @param dataExpDados The dataExpDados to set.
	 */
	public void setDataExpDados(String dataExpDados) 
	{
		this.dataExpDados = dataExpDados;
	}
	
	/**
	 * @return Returns the dataExpPrincipal.
	 */
	public String getDataExpPrincipal() 
	{
		return dataExpPrincipal;
	}
	
	/**
	 * @param dataExpPrincipal The dataExpPrincipal to set.
	 */
	public void setDataExpPrincipal(String dataExpPrincipal) 
	{
		this.dataExpPrincipal = dataExpPrincipal;
	}
	
	/**
	 * @return Returns the dataExpSms.
	 */
	public String getDataExpSms() 
	{
		return dataExpSms;
	}
	
	/**
	 * @param dataExpSms The dataExpSms to set.
	 */
	public void setDataExpSms(String dataExpSms) 
	{
		this.dataExpSms = dataExpSms;
	}
	
	/**
	 * @return Returns the diasExpBonus.
	 */
	public short getDiasExpBonus() 
	{
		return diasExpBonus;
	}
	
	/**
	 * @param diasExpBonus The diasExpBonus to set.
	 */
	public void setDiasExpBonus(short diasExpBonus) 
	{
		this.diasExpBonus = diasExpBonus;
	}
	
	/**
	 * @return Returns the diasExpDados.
	 */
	public short getDiasExpDados() 
	{
		return diasExpDados;
	}
	
	/**
	 * @param diasExpDados The diasExpDados to set.
	 */
	public void setDiasExpDados(short diasExpDados) 
	{
		this.diasExpDados = diasExpDados;
	}
	
	/**
	 * @return Returns the diasExpPrincipal.
	 */
	public short getDiasExpPrincipal() 
	{
		return diasExpPrincipal;
	}
	
	/**
	 * @param diasExpPrincipal The diasExpPrincipal to set.
	 */
	public void setDiasExpPrincipal(short diasExpPrincipal) 
	{
		this.diasExpPrincipal = diasExpPrincipal;
	}
	
	/**
	 * @return Returns the diasExpSms.
	 */
	public short getDiasExpSms() 
	{
		return diasExpSms;
	}
	
	/**
	 * @param diasExpSms The diasExpSms to set.
	 */
	public void setDiasExpSms(short diasExpSms) 
	{
		this.diasExpSms = diasExpSms;
	}
	
	/**
	 * @return Returns the valorPago.
	 */
	public double getValorPago() 
	{
		return valorPago;
	}
	
	/**
	 * @param valorPago The valorPago to set.
	 */
	public void setValorPago(double valorPago) 
	{
		this.valorPago = valorPago;
	}
}