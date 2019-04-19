/*
 * Created on 07/07/2005
 * Altered on 21/12/2005
 */
package br.com.brasiltelecom.ppp.portal.entity;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Modela tabela de recargasTFPP 
 * @author Marcelo Alves Araujo
 * @since 07/07/2005
 * 
 * Alteração
 * @author Marcelo Alves Araujo
 * @since 21/12/2005
 */
public class RecargasTFPP implements InterfaceRecargas 
{	
	private String msisdn;
	private String idRecargas;
	private String status;
	private String numCartao;
	private String tipoRecarga;
	private String cpf;
	private String hash_cc;
	private String codLoja;
	private String datContabil;
	private String idTerminal;
	private String tipoTerminal;
	private String nsuInstituicao;
	private String idOrigem;
	private String idSistemaOrigem;
	private String idCanal;
	private String tipoTransacao;
	private String tipoCredito;
	private String operador;
	
	private int diasExpiracaoPrincipal;
	private int diasExpiracaoBonus;
	private int diasExpiracaoGPRS;
	private int diasExpiracaoSMS;
	private int diasExpiracaoPeriodico;
	private int idValor;
	private int numTentativas;
	private int idtErro;
	
	private double saldoFinalPrincipal;
	private double saldoFinalBonus;
	private double saldoFinalSMS;
	private double saldoFinalGPRS;
	private double saldoFinalPeriodico;
	
	private double valorPrincipal;
	private double valorBonus;
	private double valorSMS;
	private double valorGPRS;
	private double valorPeriodico;
	
	private Date datInclusao;
	private Date datRecarga;
	private Date datOrigem;
	private Date datTimestamp;
	
	private Canal canal;
	private Origem origem;
	private SistemaOrigem sistemaOrigem;
	private TiposCreditos tiposCreditos;
	
	private SimpleDateFormat sdF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private DecimalFormat df = new DecimalFormat("#,##0.00"); 
	
	public String getFDatRecarga() 
	{	
		return sdF.format(datRecarga);
	}
	public String getFDatOrigem() 
	{	
		return sdF.format(datOrigem);
	}
	public String getFDatInclusao() 
	{	
		return sdF.format(datInclusao);
	}
	public String getMsisdnString() {
	
		if (msisdn != null && !msisdn.trim().equals("") && msisdn.length() == 12) {
			return "(" + msisdn.substring(2,4) + ") " + msisdn.substring(4,8) + "-" + msisdn.substring(8,12);
		} else {
			return msisdn;
		} 
	}
	public String getValorStringPrincipal()
	{
		return df.format(valorPrincipal);
	}
	public String getValorStringBonus()
	{
		return df.format(valorBonus);
	}
	public String getValorStringSMS()
	{
		return df.format(valorSMS);
	}
	public String getValorStringGPRS()
	{
		return df.format(valorGPRS);
	}
	public String getValorStringPeriodico()
	{
		return df.format(valorPeriodico);
	}
	
	public String getTipoCreditoString()
	{
		if (tipoCredito.equals("00")){
			return "R$";
		} else {
			return tipoCredito;
		}
	}
	
	/**
	 * @return Returns the canal.
	 */
	public Canal getCanal()
	{
		return canal;
	}
	/**
	 * @return Returns the codLoja.
	 */
	public String getCodLoja()
	{
		return codLoja;
	}
	/**
	 * @return Returns the cpf.
	 */
	public String getCpf()
	{
		return cpf;
	}
	/**
	 * @return Returns the datContabil.
	 */
	public String getDatContabil()
	{
		return datContabil;
	}
	/**
	 * @return Returns the datOrigem.
	 */
	public Date getDatOrigem()
	{
		return datOrigem;
	}
	/**
	 * @return Returns the datRecarga.
	 */
	public Date getDatRecarga()
	{
		return datRecarga;
	}
	/**
	 * @return Returns the datInclusao.
	 */
	public Date getDatInclusao()
	{
		return datInclusao;
	}
	/**
	 * @return Returns the datTimestamp.
	 */
	public Date getDatTimestamp()
	{
		return datTimestamp;
	}
	/**
	 * @return Returns the df.
	 */
	public DecimalFormat getDf()
	{
		return df;
	}
	/**
	 * @return Returns the diasExpiracaoBonus.
	 */
	public int getDiasExpiracaoBonus()
	{
		return diasExpiracaoBonus;
	}
	/**
	 * @return Returns the diasExpiracaoGPRS.
	 */
	public int getDiasExpiracaoGPRS()
	{
		return diasExpiracaoGPRS;
	}
	/**
	 * @return Returns the diasExpiracaoPrincipal.
	 */
	public int getDiasExpiracaoPrincipal()
	{
		return diasExpiracaoPrincipal;
	}
	/**
	 * @return Returns the diasExpiracaoSMS.
	 */
	public int getDiasExpiracaoSMS()
	{
		return diasExpiracaoSMS;
	}
	/**
	 * @return Returns the hash_cc.
	 */
	public String getHash_cc()
	{
		return hash_cc;
	}
	/**
	 * @return Returns the idCanal.
	 */
	public String getIdCanal()
	{
		return idCanal;
	}
	/**
	 * @return Returns the idOrigem.
	 */
	public String getIdOrigem()
	{
		return idOrigem;
	}
	/**
	 * @return Returns the idRecargas.
	 */
	public String getIdRecargas()
	{
		return idRecargas;
	}
	/**
	 * @return Returns the idSistemaOrigem.
	 */
	public String getIdSistemaOrigem()
	{
		return idSistemaOrigem;
	}
	/**
	 * @return Returns the idTerminal.
	 */
	public String getIdTerminal()
	{
		return idTerminal;
	}
	/**
	 * @return Returns the idtErro.
	 */
	public int getIdtErro()
	{
		return idtErro;
	}
	/**
	 * @return Returns the idValor.
	 */
	public int getIdValor()
	{
		return idValor;
	}
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	/**
	 * @return Returns the nsuInstituicao.
	 */
	public String getNsuInstituicao()
	{
		return nsuInstituicao;
	}
	/**
	 * @return Returns the numCartao.
	 */
	public String getNumCartao()
	{
		return numCartao;
	}
	/**
	 * @return Returns the numTentativas.
	 */
	public int getNumTentativas()
	{
		return numTentativas;
	}
	/**
	 * @return Returns the operador.
	 */
	public String getOperador()
	{
		return operador;
	}
	/**
	 * @return Returns the origem.
	 */
	public Origem getOrigem()
	{
		return origem;
	}
	/**
	 * @return Returns the saldoFinalBonus.
	 */
	public double getSaldoFinalBonus()
	{
		return saldoFinalBonus;
	}
	/**
	 * @return Returns the saldoFinalGPRS.
	 */
	public double getSaldoFinalGPRS()
	{
		return saldoFinalGPRS;
	}
	/**
	 * @return Returns the saldoFinalPrincipal.
	 */
	public double getSaldoFinalPrincipal()
	{
		return saldoFinalPrincipal;
	}
	/**
	 * @return Returns the saldoFinalSMS.
	 */
	public double getSaldoFinalSMS()
	{
		return saldoFinalSMS;
	}
	/**
	 * @return Returns the sdF.
	 */
	public SimpleDateFormat getSdF()
	{
		return sdF;
	}
	/**
	 * @return Returns the sistemaOrigem.
	 */
	public SistemaOrigem getSistemaOrigem()
	{
		return sistemaOrigem;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * @return Returns the tipoCredito.
	 */
	public String getTipoCredito()
	{
		return tipoCredito;
	}
	/**
	 * @return Returns the tipoRecarga.
	 */
	public String getTipoRecarga()
	{
		return tipoRecarga;
	}
	/**
	 * @return Returns the tiposCreditos.
	 */
	public TiposCreditos getTiposCreditos()
	{
		return tiposCreditos;
	}
	/**
	 * @return Returns the tipoTerminal.
	 */
	public String getTipoTerminal()
	{
		return tipoTerminal;
	}
	/**
	 * @return Returns the tipoTransacao.
	 */
	public String getTipoTransacao()
	{
		return tipoTransacao;
	}
	/**
	 * @return Returns the valorBonus.
	 */
	public double getValorBonus()
	{
		return valorBonus;
	}
	/**
	 * @return Returns the valorGPRS.
	 */
	public double getValorGPRS()
	{
		return valorGPRS;
	}
	/**
	 * @return Returns the valorPrincipal.
	 */
	public double getValorPrincipal()
	{
		return valorPrincipal;
	}
	/**
	 * @return Returns the valorSMS.
	 */
	public double getValorSMS()
	{
		return valorSMS;
	}
	/**
	 * @param canal The canal to set.
	 */
	public void setCanal(Canal canal)
	{
		this.canal = canal;
	}
	/**
	 * @param codLoja The codLoja to set.
	 */
	public void setCodLoja(String codLoja)
	{
		this.codLoja = codLoja;
	}
	/**
	 * @param cpf The cpf to set.
	 */
	public void setCpf(String cpf)
	{
		this.cpf = cpf;
	}
	/**
	 * @param datContabil The datContabil to set.
	 */
	public void setDatContabil(String datContabil)
	{
		this.datContabil = datContabil;
	}
	/**
	 * @param datOrigem The datOrigem to set.
	 */
	public void setDatOrigem(Date datOrigem)
	{
		this.datOrigem = datOrigem;
	}
	/**
	 * @param datRecarga The datRecarga to set.
	 */
	public void setDatRecarga(Date datRecarga)
	{
		this.datRecarga = datRecarga;
	}
	/**
	 * @param datInclusao The datInclusao to set.
	 */
	public void setDatInclusao(Date datInclusao)
	{
		this.datInclusao = datInclusao;
	}
	/**
	 * @param datTimestamp The datTimestamp to set.
	 */
	public void setDatTimestamp(Date datTimestamp)
	{
		this.datTimestamp = datTimestamp;
	}
	/**
	 * @param df The df to set.
	 */
	public void setDf(DecimalFormat df)
	{
		this.df = df;
	}
	/**
	 * @param diasExpiracaoBonus The diasExpiracaoBonus to set.
	 */
	public void setDiasExpiracaoBonus(int diasExpiracaoBonus)
	{
		this.diasExpiracaoBonus = diasExpiracaoBonus;
	}
	/**
	 * @param diasExpiracaoGPRS The diasExpiracaoGPRS to set.
	 */
	public void setDiasExpiracaoGPRS(int diasExpiracaoGPRS)
	{
		this.diasExpiracaoGPRS = diasExpiracaoGPRS;
	}
	/**
	 * @param diasExpiracaoPrincipal The diasExpiracaoPrincipal to set.
	 */
	public void setDiasExpiracaoPrincipal(int diasExpiracaoPrincipal)
	{
		this.diasExpiracaoPrincipal = diasExpiracaoPrincipal;
	}
	/**
	 * @param diasExpiracaoSMS The diasExpiracaoSMS to set.
	 */
	public void setDiasExpiracaoSMS(int diasExpiracaoSMS)
	{
		this.diasExpiracaoSMS = diasExpiracaoSMS;
	}
	/**
	 * @param hash_cc The hash_cc to set.
	 */
	public void setHash_cc(String hash_cc)
	{
		this.hash_cc = hash_cc;
	}
	/**
	 * @param idCanal The idCanal to set.
	 */
	public void setIdCanal(String idCanal)
	{
		this.idCanal = idCanal;
	}
	/**
	 * @param idOrigem The idOrigem to set.
	 */
	public void setIdOrigem(String idOrigem)
	{
		this.idOrigem = idOrigem;
	}
	/**
	 * @param idRecargas The idRecargas to set.
	 */
	public void setIdRecargas(String idRecargas)
	{
		this.idRecargas = idRecargas;
	}
	/**
	 * @param idSistemaOrigem The idSistemaOrigem to set.
	 */
	public void setIdSistemaOrigem(String idSistemaOrigem)
	{
		this.idSistemaOrigem = idSistemaOrigem;
	}
	/**
	 * @param idTerminal The idTerminal to set.
	 */
	public void setIdTerminal(String idTerminal)
	{
		this.idTerminal = idTerminal;
	}
	/**
	 * @param idtErro The idtErro to set.
	 */
	public void setIdtErro(int idtErro)
	{
		this.idtErro = idtErro;
	}
	/**
	 * @param idValor The idValor to set.
	 */
	public void setIdValor(int idValor)
	{
		this.idValor = idValor;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	/**
	 * @param nsuInstituicao The nsuInstituicao to set.
	 */
	public void setNsuInstituicao(String nsuInstituicao)
	{
		this.nsuInstituicao = nsuInstituicao;
	}
	/**
	 * @param numCartao The numCartao to set.
	 */
	public void setNumCartao(String numCartao)
	{
		this.numCartao = numCartao;
	}
	/**
	 * @param numTentativas The numTentativas to set.
	 */
	public void setNumTentativas(int numTentativas)
	{
		this.numTentativas = numTentativas;
	}
	/**
	 * @param operador The operador to set.
	 */
	public void setOperador(String operador)
	{
		this.operador = operador;
	}
	/**
	 * @param origem The origem to set.
	 */
	public void setOrigem(Origem origem)
	{
		this.origem = origem;
	}
	/**
	 * @param saldoFinalBonus The saldoFinalBonus to set.
	 */
	public void setSaldoFinalBonus(double saldoFinalBonus)
	{
		this.saldoFinalBonus = saldoFinalBonus;
	}
	/**
	 * @param saldoFinalGPRS The saldoFinalGPRS to set.
	 */
	public void setSaldoFinalGPRS(double saldoFinalGPRS)
	{
		this.saldoFinalGPRS = saldoFinalGPRS;
	}
	/**
	 * @param saldoFinalPrincipal The saldoFinalPrincipal to set.
	 */
	public void setSaldoFinalPrincipal(double saldoFinalPrincipal)
	{
		this.saldoFinalPrincipal = saldoFinalPrincipal;
	}
	/**
	 * @param saldoFinalSMS The saldoFinalSMS to set.
	 */
	public void setSaldoFinalSMS(double saldoFinalSMS)
	{
		this.saldoFinalSMS = saldoFinalSMS;
	}
	/**
	 * @param sdF The sdF to set.
	 */
	public void setSdF(SimpleDateFormat sdF)
	{
		this.sdF = sdF;
	}
	/**
	 * @param sistemaOrigem The sistemaOrigem to set.
	 */
	public void setSistemaOrigem(SistemaOrigem sistemaOrigem)
	{
		this.sistemaOrigem = sistemaOrigem;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	/**
	 * @param tipoCredito The tipoCredito to set.
	 */
	public void setTipoCredito(String tipoCredito)
	{
		this.tipoCredito = tipoCredito;
	}
	/**
	 * @param tipoRecarga The tipoRecarga to set.
	 */
	public void setTipoRecarga(String tipoRecarga)
	{
		this.tipoRecarga = tipoRecarga;
	}
	/**
	 * @param tiposCreditos The tiposCreditos to set.
	 */
	public void setTiposCreditos(TiposCreditos tiposCreditos)
	{
		this.tiposCreditos = tiposCreditos;
	}
	/**
	 * @param tipoTerminal The tipoTerminal to set.
	 */
	public void setTipoTerminal(String tipoTerminal)
	{
		this.tipoTerminal = tipoTerminal;
	}
	/**
	 * @param tipoTransacao The tipoTransacao to set.
	 */
	public void setTipoTransacao(String tipoTransacao)
	{
		this.tipoTransacao = tipoTransacao;
	}
	/**
	 * @param valorBonus The valorBonus to set.
	 */
	public void setValorBonus(double valorBonus)
	{
		this.valorBonus = valorBonus;
	}
	/**
	 * @param valorGPRS The valorGPRS to set.
	 */
	public void setValorGPRS(double valorGPRS)
	{
		this.valorGPRS = valorGPRS;
	}
	/**
	 * @param valorPrincipal The valorPrincipal to set.
	 */
	public void setValorPrincipal(double valorPrincipal)
	{
		this.valorPrincipal = valorPrincipal;
	}
	/**
	 * @param valorSMS The valorSMS to set.
	 */
	public void setValorSMS(double valorSMS)
	{
		this.valorSMS = valorSMS;
	}
	/**
	 * @return the diasExpiracaoPeriodico
	 */
	public int getDiasExpiracaoPeriodico()
	{
		return diasExpiracaoPeriodico;
	}
	/**
	 * @param diasExpiracaoPeriodico the diasExpiracaoPeriodico to set
	 */
	public void setDiasExpiracaoPeriodico(int diasExpiracaoPeriodico)
	{
		this.diasExpiracaoPeriodico = diasExpiracaoPeriodico;
	}
	/**
	 * @return the saldoFinalPeriodico
	 */
	public double getSaldoFinalPeriodico()
	{
		return saldoFinalPeriodico;
	}
	/**
	 * @param saldoFinalPeriodico the saldoFinalPeriodico to set
	 */
	public void setSaldoFinalPeriodico(double saldoFinalPeriodico)
	{
		this.saldoFinalPeriodico = saldoFinalPeriodico;
	}
	/**
	 * @return the valorPeriodico
	 */
	public double getValorPeriodico()
	{
		return valorPeriodico;
	}
	/**
	 * @param valorPeriodico the valorPeriodico to set
	 */
	public void setValorPeriodico(double valorPeriodico)
	{
		this.valorPeriodico = valorPeriodico;
	}	
}
