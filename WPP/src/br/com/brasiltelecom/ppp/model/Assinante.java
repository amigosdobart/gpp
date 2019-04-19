/*
 * Created on 20/04/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 * Modela as informações do assinante 
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Assinante {

	private String retorno;
	private String msisdn;
	private String planoPreco;
	private String descPlanoPreco;
	private String statusAssinante;
	private String descStatusAssinante;
	private String statusServico;
	private String statusPeriodico;
	private String descStatusServico;
	private String descStatusPeriodico;
	private String saldoPrincipal;
	private String saldoPeriodico;
	private String saldoBonus;
	private String saldoSms;
	private String saldoDados;
	private String imsi;
	private String dataExpiracaoPrincipal;
	private String dataExpiracaoPeridico;
	private String dataExpiracaoBonus;
	private String dataExpiracaoSms;
	private String dataExpiracaoDados;
	private String[] friendFamily;
	private Collection saldoAssinante;
	private String dataAtivacao;
	private boolean indPossuiAth;
	private boolean indPossuiImsi;
	
	
	private PhoneNumberFormat pf = new PhoneNumberFormat();
	private DecimalFormat df = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(new Locale("pt","BR")));
	private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formataData2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * @return
	 */
	public String getDescPlanoPreco() {
		return descPlanoPreco;
	}

	/**
	 * @return
	 */
	public String getDescStatusAssinante() {
		return descStatusAssinante;
	}

	/**
	 * @return
	 */
	public String getDescStatusServico() {
		return descStatusServico;
	}

	/**
	 * @return
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @return
	 */
	public String getPlanoPreco() {
		return planoPreco;
	}

	/**
	 * @return
	 */
	public String getRetorno() {
		return retorno;
	}

	/**
	 * @return
	 */
	public String getStatusAssinante() {
		return statusAssinante;
	}

	/**
	 * @return
	 */
	public String getStatusServico() {
		return statusServico;
	}

	/**
	 * @param string
	 */
	public void setDescPlanoPreco(String string) {
		descPlanoPreco = string;
	}

	/**
	 * @param string
	 */
	public void setDescStatusAssinante(String string) {
		descStatusAssinante = string;
	}

	/**
	 * @param string
	 */
	public void setDescStatusServico(String string) {
		descStatusServico = string;
	}

	/**
	 * @param string
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

	/**
	 * @param string
	 */
	public void setPlanoPreco(String string) {
		planoPreco = string;
	}

	/**
	 * @param string
	 */
	public void setRetorno(String string) {
		retorno = string;
	}

	/**
	 * @param string
	 */
	public void setStatusAssinante(String string) {
		statusAssinante = string;
	}

	/**
	 * @param string
	 */
	public void setStatusServico(String string) {
		statusServico = string;
	}

	/**
	 * @return Returns the dataExpiracaoDados.
	 */
	public String getDataExpiracaoDados() {
		return dataExpiracaoDados;
	}
	
	/**
	 * @param dataExpiracaoDados The dataExpiracaoDados to set.
	 */
	public void setDataExpiracaoDados(String dataExpiracaoDados) {
		this.dataExpiracaoDados = dataExpiracaoDados;
	}
	
	/**
	 * @return Returns the dataExpiracaoPrincipal.
	 */
	public String getDataExpiracaoPrincipal() {
		return dataExpiracaoPrincipal;
	}
	
	/**
	 * @param dataExpiracaoPrincipal The dataExpiracaoPrincipal to set.
	 */
	public void setDataExpiracaoPrincipal(String dataExpiracaoPrincipal) {
		this.dataExpiracaoPrincipal = dataExpiracaoPrincipal;
	}
	
	/**
	 * @return Returns the dataExpiracaoBonus.
	 */
	public String getDataExpiracaoBonus() {
		return dataExpiracaoBonus;
	}
	
	/**
	 * @param dataExpiracaoBonus The dataExpiracaoBonus to set.
	 */
	public void setDataExpiracaoBonus(String dataExpiracaoBonus) {
		this.dataExpiracaoBonus = dataExpiracaoBonus;
	}
	
	/**
	 * @return Returns the dataExpiracaoSms.
	 */
	public String getDataExpiracaoSms() {
		return dataExpiracaoSms;
	}
	
	/**
	 * @param dataExpiracaoSms The dataExpiracaoSms to set.
	 */
	public void setDataExpiracaoSms(String dataExpiracaoSms) {
		this.dataExpiracaoSms = dataExpiracaoSms;
	}
	
	/**
	 * @return Returns the imsi.
	 */
	public String getImsi() {
		return imsi;
	}
	
	/**
	 * @param imsi The imsi to set.
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	
	/**
	 * @return Returns the saldoBonus.
	 */
	public String getSaldoBonus() {
		return df.format(Double.parseDouble(saldoBonus));
	}
	
	/**
	 * @param saldoBonus The saldoBonus to set.
	 */
	public void setSaldoBonus(String saldoBonus) {
		this.saldoBonus = saldoBonus;
	}
	
	/**
	 * @return Returns the saldoDados.
	 */
	public String getSaldoDados() {
		return df.format(Double.parseDouble(saldoDados));
	}
	
	/**
	 * @param saldoDados The saldoDados to set.
	 */
	public void setSaldoDados(String saldoDados) {
		this.saldoDados = saldoDados;
	}
	
	/**
	 * @return Returns the saldoPrincipal.
	 */
	public String getSaldoPrincipal() {
		return df.format(Double.parseDouble(saldoPrincipal));
	}
	
	public double getSaldoPrincipalDouble() {
			return saldoPrincipal != null ? Double.parseDouble(saldoPrincipal) : 0;
	}
	/**
	 * @param saldoPrincipal The saldoPrincipal to set.
	 */
	public void setSaldoPrincipal(String saldoPrincipal) {
		this.saldoPrincipal = saldoPrincipal;
	}
	
	/**
	 * @return Returns the saldoSms.
	 */
	public String getSaldoSms() {
		return df.format(Double.parseDouble(saldoSms));
	}
	
	/**
	 * @param saldoSms The saldoSms to set.
	 */
	public void setSaldoSms(String saldoSms) {
		this.saldoSms = saldoSms;
	}
	
	/**
	 * 
	 * @param ff Recebe Friends & Friendly
 	*/
	public void setFriendFamily(String ff)
	{
		friendFamily = ff.split(";");
	}
	/**
	 * 
	 * @return F&F
	 */
	public String[] getFriendFamily()
	{
		return friendFamily;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getFriendFamilyFormatado()
	{
		
		String[] formatados = new String[friendFamily.length];
		for (int i=0;i<friendFamily.length;i++)
		 formatados[i] = new String(pf.format(friendFamily[i])); 
			
		return formatados;
	}
	
	public String getSaldoString(String saldo)
	{
		return df.format(Double.parseDouble(saldo));
	}

	/**
	 * @return the dataExpiracaoPeridico
	 */
	public String getDataExpiracaoPeridico()
	{
		return dataExpiracaoPeridico;
	}

	/**
	 * @param dataExpiracaoPeridico the dataExpiracaoPeridico to set
	 */
	public void setDataExpiracaoPeridico(String dataExpiracaoPeridico)
	{
		this.dataExpiracaoPeridico = dataExpiracaoPeridico;
	}

	/**
	 * @return the descStatusPeriodico
	 */
	public String getDescStatusPeriodico()
	{
		return descStatusPeriodico;
	}

	/**
	 * @param descStatusPeriodico the descStatusPeriodico to set
	 */
	public void setDescStatusPeriodico(String descStatusPeriodico)
	{
		this.descStatusPeriodico = descStatusPeriodico;
	}

	/**
	 * @return the statusPeriodico
	 */
	public String getStatusPeriodico()
	{
		return statusPeriodico;
	}

	/**
	 * @param statusPeriodico the statusPeriodico to set
	 */
	public void setStatusPeriodico(String statusPeriodico)
	{
		this.statusPeriodico = statusPeriodico;
	}

	/**
	 * @return the saldoAssinante
	 */
	public Collection getSaldoAssinante()
	{
		return saldoAssinante;
	}

	/**
	 * @param saldoAssinante the saldoAssinante to set
	 */
	public void setSaldoAssinante(Collection saldoAssinante)
	{
		this.saldoAssinante = saldoAssinante;
	}

	/**
	 * @return the saldoPeriodico
	 */
	public double getSaldoPeriodicoDouble()
	{
		return saldoPeriodico != null ? Double.parseDouble(saldoPeriodico) : 0;
	}
	/**
	 * @return Returns the saldoPrincipal.
	 */
	public String getSaldoPeriodico() 
	{
		return df.format(Double.parseDouble(saldoPeriodico));
	}
	
	/**
	 * @param saldoPeriodico the saldoPeriodico to set
	 */
	public void setSaldoPeriodico(String saldoPeriodico)
	{
		this.saldoPeriodico = saldoPeriodico;
	}
	
	public Date getDataExpiracaoPrincipalDate() throws ParseException
	{
		return dataExpiracaoPrincipal != null ? formataData.parse(dataExpiracaoPrincipal) : null;
	}
	
	public Date getDataExpiracaoPeridicoDate() throws ParseException
	{
		return dataExpiracaoPeridico != null ? formataData.parse(dataExpiracaoPeridico) : null;
	}
	
	public Date getDataExpiracaoBonusDate() throws ParseException
	{
		return dataExpiracaoBonus != null ? formataData.parse(dataExpiracaoBonus) : null;
	}
	
	public Date getDataExpiracaoSmsDate() throws ParseException
	{
		return dataExpiracaoSms != null ? formataData.parse(dataExpiracaoSms) : null;
	}
	
	public Date getDataExpiracaoDadosDate() throws ParseException
	{
		return dataExpiracaoDados != null ? formataData.parse(dataExpiracaoDados) : null;
	}

	public Date getDataAtivacaoDate() throws ParseException
	{
		return dataAtivacao != null ? formataData2.parse(dataAtivacao) : null;
	}
	
	public double getSaldoBonusDouble()
	{
		return saldoBonus != null ? Double.parseDouble(saldoBonus) : 0;
	}

	public double getSaldoDadosDouble()
	{
		return saldoDados != null ? Double.parseDouble(saldoDados) : 0;
	}

	public double getSaldoSmsDouble()
	{
		return saldoSms != null ? Double.parseDouble(saldoSms) : 0;
	}

	/**
	 * @return the indPossuiAth
	 */
	public boolean isIndPossuiAth()
	{
		return indPossuiAth;
	}

	/**
	 * @param indPossuiAth the indPossuiAth to set
	 */
	public void setIndPossuiAth(boolean indPossuiAth)
	{
		this.indPossuiAth = indPossuiAth;
	}

	/**
	 * @return the indPossuiImsi
	 */
	public boolean isIndPossuiImsi()
	{
		return indPossuiImsi;
	}

	/**
	 * @param indPossuiImsi the indPossuiImsi to set
	 */
	public void setIndPossuiImsi(boolean indPossuiImsi)
	{
		this.indPossuiImsi = indPossuiImsi;
	}

	/**
	 * @return the dataAtivacao
	 */
	public String getDataAtivacao() 
	{
		return dataAtivacao;
	}

	/**
	 * @param dataAtivacao the dataAtivacao to set
	 */
	public void setDataAtivacao(String dataAtivacao) 
	{
		this.dataAtivacao = dataAtivacao;
	}
	
	
}
