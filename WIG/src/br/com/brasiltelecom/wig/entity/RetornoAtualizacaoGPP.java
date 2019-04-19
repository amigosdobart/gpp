package br.com.brasiltelecom.wig.entity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author JOAO PAULO GALVAGNI
 * @since  21/08/2006
 */
public class RetornoAtualizacaoGPP
{
	private static DecimalFormat df = new DecimalFormat("##,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	
	private String msisdn;
	private String operacao;
	private String codRetorno;
	private String descRetorno;
	private String codServico;
	private String descServico;
	
	private String valor;
	private String saldoPrincipal;
	private String saldoBonus;
	private String saldoSMS;
	private String saldoDados;
	
	/**
	 * @return Returns the codRetorno.
	 */
	public String getCodRetorno()
	{
		return codRetorno;
	}
	/**
	 * @return Returns the codServico.
	 */
	public String getCodServico()
	{
		return codServico;
	}
	/**
	 * @return Returns the descRetorno.
	 */
	public String getDescRetorno()
	{
		return descRetorno;
	}
	/**
	 * @return Returns the descServico.
	 */
	public String getDescServico()
	{
		return descServico;
	}
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	/**
	 * @return Returns the operacao.
	 */
	public String getOperacao()
	{
		return operacao;
	}
	/**
	 * @return Returns the saldoBonus.
	 */
	public String getSaldoBonus()
	{
		return df.format(saldoBonus);
	}
	/**
	 * @return Returns the saldoDados.
	 */
	public String getSaldoDados()
	{
		return df.format(saldoDados);
	}
	/**
	 * @return Returns the saldoPrincipal.
	 */
	public String getSaldoPrincipal()
	{
		return df.format(saldoPrincipal);
	}
	/**
	 * @return Returns the saldoSMS.
	 */
	public String getSaldoSMS()
	{
		return df.format(saldoSMS);
	}
	/**
	 * @return Returns the valor.
	 */
	public String getValor()
	{
		return df.format(valor);
	}
	/**
	 * @param codRetorno The codRetorno to set.
	 */
	public void setCodRetorno(String codRetorno)
	{
		this.codRetorno = codRetorno;
	}
	/**
	 * @param codServico The codServico to set.
	 */
	public void setCodServico(String codServico)
	{
		this.codServico = codServico;
	}
	/**
	 * @param descRetorno The descRetorno to set.
	 */
	public void setDescRetorno(String descRetorno)
	{
		this.descRetorno = descRetorno;
	}
	/**
	 * @param descServico The descServico to set.
	 */
	public void setDescServico(String descServico)
	{
		this.descServico = descServico;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	/**
	 * @param operacao The operacao to set.
	 */
	public void setOperacao(String operacao)
	{
		this.operacao = operacao;
	}
	/**
	 * @param saldoBonus The saldoBonus to set.
	 */
	public void setSaldoBonus(String saldoBonus)
	{
		this.saldoBonus = saldoBonus;
	}
	/**
	 * @param saldoDados The saldoDados to set.
	 */
	public void setSaldoDados(String saldoDados)
	{
		this.saldoDados = saldoDados;
	}
	/**
	 * @param saldoPrincipal The saldoPrincipal to set.
	 */
	public void setSaldoPrincipal(String saldoPrincipal)
	{
		this.saldoPrincipal = saldoPrincipal;
	}
	/**
	 * @param saldoSMS The saldoSMS to set.
	 */
	public void setSaldoSMS(String saldoSMS)
	{
		this.saldoSMS = saldoSMS;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor)
	{
		this.valor = valor;
	}
	
	
}
