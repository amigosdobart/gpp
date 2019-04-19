package br.com.brasiltelecom.ppp.model;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 * Classe que mapeia os dados de extrato
 * @author Andr� Gon�alves
 * @since 21/05/2004
 */
public class Extrato 
{
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat hdf = new SimpleDateFormat("H:mm:ss");
	private PhoneNumberFormat ph = new PhoneNumberFormat();		
	private static DecimalFormat df = new DecimalFormat("##,##0.00", new DecimalFormatSymbols(new Locale("BR", "pt", "")));

	/**
	* Identifica��o da Chamada
	*/
	private String id;
	
	/**
	 * N�mero do Acesso de Origem da Chamada/Cobran�a/Recarga
	 */
	private String numeroOrigem;
	
	/**
	 * Data da Chamada/Cobran�a/Recarga
	 */
	private String data;
	
	/**
	 * Hora da Chamada
	 */
	private String horaChamada;
		
	/**
	 * Tipo da Tarifa��o da Chamada
	 */
	private String tipoTarifacao;
	
	/**
	 * Descri��o do Item de Chamada ou de Cobran�a/Recarga
	 */
	private String operacao;
	
	/**
	 * Regi�o de Origem da Chamada Efetuada
	 */
	private String regiaoOrigem;
	
	/**
	 * N�mero do Acesso de Destino da Chamada
	 */
	private String numeroDestino;
	
	/**
	 * Dura��o da Chamada
	 */
	private String duracaoChamada;
	
	/**
	 * Valor da Chamada/Cobran�a/Recarga
	 */
//***********************************************************************************************
	private double valorPrincipal;
	
	private double saldoPrincipal;
	
	private double valorBonus;
	
	private double saldoBonus;

	private double valorGPRS;
	
	private double saldoGPRS;

	private double valorSMS;
	
	private double saldoSMS;

	private double valorPeriodico;

	private double saldoPeriodico;
	
	private double valorTotal;
	
	private double saldoTotal;
//***************************************************************************	

	/**
	 * @return Returns the saldoTotal.
	 */
	public double getSaldoTotal() {
		return saldoTotal;
	}
	/**
	 * @param saldoTotal The saldoTotal to set.
	 */
	public void setSaldoTotal(double saldoTotal) {
		this.saldoTotal = saldoTotal;
	}
	/**
	 * @return Returns the valorTotal.
	 */
	public double getValorTotal() {
		return valorTotal;
	}
	/**
	 * @param valorTotal The valorTotal to set.
	 */
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	/**
	 * Regi�o de Destino da liga��o 
	 */
	private String regiaoDestino;
	
	/**
	 * Construtor padr�o de Extrato
	 */
	public Extrato() 
	{		
	}

	/**
	 * Obt�m o n�mero do Acesso de Destino da Chamada
	 * @return N�mero do Acesso de Destino da Chamada
	 */
	public String getNumeroDestino() {
		return numeroDestino;
	}

	/**
	 * Obt�m o n�mero do Acesso de Origem da Chamada/Cobran�a/Recarga
	 * @return n�mero do Acesso de Origem da Chamada/Cobran�a/Recarga
	 */
	public String getNumeroOrigem() {
		return numeroOrigem;
	}

	/**
	 * Obt�m a descri��o do Item de Chamada ou de Cobran�a/Recarga
	 * @return Descri��o do Item de Chamada ou de Cobran�a/Recarga
	 */
	public String getOperacao() {
		return operacao;
	}

	/**
	 * Obt�m a regi�o de Origem da Chamada Efetuada
	 * @return Regi�o de Origem da Chamada Efetuada
	 */
	public String getRegiaoOrigem() {
		return regiaoOrigem;
	}

	/**
	 * Obt�m o saldo de Cr�ditos ap�s Cobran�a/Recarga
	 * @return Saldo de Cr�ditos ap�s Cobran�a/Recarga
	 */
	public double getSaldoPrincipal() {
		return saldoPrincipal;
	}

	/**
	 * Obt�m o tipo da Tarifa��o da Chamada
	 * @return Tipo da Tarifa��o da Chamada
	 */
	public String getTipoTarifacao() {
		return tipoTarifacao;
	}

	/**
	 * Obt�m o valor da Chamada/Cobran�a/Recarga
	 * @return Valor da Chamada/Cobran�a/Recarga
	 */
	public double getValorPrincipal() {
		return valorPrincipal;
	}

	/**
	 * Seta o n�mero do Acesso de Destino da Chamada
	 * @param string N�mero do Acesso de Destino da Chamada a se setar
	 */
	public void setNumeroDestino(String string) {
		numeroDestino = string;
	}

	/**
	 * Seta o n�mero do Acesso de Origem da Chamada/Cobran�a/Recarga
	 * @param string n�mero do Acesso de Origem da Chamada/Cobran�a/Recarga a se setar
	 */
	public void setNumeroOrigem(String string) {
		numeroOrigem = string;
	}

	/**
	 * Seta a descri��o do Item de Chamada ou de Cobran�a/Recarga
	 * @param string Descri��o do Item de Chamada ou de Cobran�a/Recarga a se setar
	 */
	public void setOperacao(String string) {
		operacao = string;
	}

	/**
	 * Seta a regi�o de Origem da Chamada Efetuada
	 * @param string Regi�o de Origem da Chamada Efetuada a se setar
	 */
	public void setRegiaoOrigem(String string) {
		regiaoOrigem = string;
	}

	/**
	 * Seta o saldo de Cr�ditos ap�s Cobran�a/Recarga
	 * @param d Saldo de Cr�ditos ap�s Cobran�a/Recarga a se setar
	 */
	public void setSaldoPrincipal(double d) {
		saldoPrincipal = d;
	}

	/**
	 * Seta o tipo da Tarifa��o da Chamada
	 * @param string Tipo da Tarifa��o da Chamada a se setar
	 */
	public void setTipoTarifacao(String string) {
		tipoTarifacao = string;
	}

	/**
	 * Seta o valor da Chamada/Cobran�a/Recarga
	 * @param d Valor da Chamada/Cobran�a/Recarga a se setar
	 */
	public void setValorPrincipal(double d) {
		valorPrincipal = d;
	}
	
	public String getDataString(){
		return sdf.format(data); 
	}
	
	public String getHoraChamadaString(){
		return hdf.format(horaChamada);
	}
	public String getDuracaoChamadaString(){
		return hdf.format(duracaoChamada);
	}
//***********************************************************************************************
	public String getValorPrincipalString(){
		return df.format(valorPrincipal);
	}
	public String getSaldoPrincipalString(){
		return df.format(saldoPrincipal);
	}

	public String getValorBonusString(){
		return df.format(valorBonus);
	}
	public String getSaldoBonusString(){
		return df.format(saldoBonus);
	}

	public String getValorGPRSString(){
		return df.format(valorGPRS);
	}
	public String getSaldoGPRSString(){
		return df.format(saldoGPRS);
	}

	public String getValorSMSString(){
		return df.format(valorSMS);
	}
	public String getSaldoSMSString(){
		return df.format(saldoSMS);
	}

	public String getValorPeriodicoString(){
		return df.format(valorPeriodico);
	}
	public String getSaldoPeriodicoString(){
		return df.format(saldoPeriodico);
	}
	
	public String getValorTotalString(){
		return df.format(valorTotal);
	}
	public String getSaldoTotalString(){
		return df.format(saldoTotal);
	}
//	***********************************************************************************************	
	public String getNumeroOrigemString(){
		if (numeroOrigem != null && !numeroOrigem.trim().equals("") && numeroOrigem.length() == 10) {
			return "(" + numeroOrigem.substring(0,2) + ") " + numeroOrigem.substring(2,6) + "-" + numeroOrigem.substring(6,10);
		} else {
			return numeroOrigem;
		} 
	}

	public String getNumeroDestinoString(){
		return ph.format(numeroDestino);
	}	

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return
	 */
	public String getHoraChamada() {
		return horaChamada;
	}

	/**
	 * @param string
	 */
	public void setData(String string) {
		data = string;
	}

	/**
	 * @param string
	 */
	public void setHoraChamada(String string) {
		horaChamada = string;
	}

	/**
	 * @return
	 */
	public String getDuracaoChamada() {
		return duracaoChamada;
	}

	/**
	 * @param string
	 */
	public void setDuracaoChamada(String string) {
		duracaoChamada = string;
	}

	/**
	 * @return
	 */
	public String getRegiaoDestino() {
		return regiaoDestino;
	}

	/**
	 * @param string
	 */
	public void setRegiaoDestino(String string) {
		regiaoDestino = string;
	}

	/**
	 * @return Returns the saldoBonus.
	 */
	public double getSaldoBonus() {
		return saldoBonus;
	}
	/**
	 * @param saldoBonus The saldoBonus to set.
	 */
	public void setSaldoBonus(double saldoBonus) {
		this.saldoBonus = saldoBonus;
	}
	/**
	 * @return Returns the saldoGPRS.
	 */
	public double getSaldoGPRS() {
		return saldoGPRS;
	}
	/**
	 * @param saldoGPRS The saldoGPRS to set.
	 */
	public void setSaldoGPRS(double saldoGPRS) {
		this.saldoGPRS = saldoGPRS;
	}
	/**
	 * @return Returns the saldoSMS.
	 */
	public double getSaldoSMS() {
		return saldoSMS;
	}
	/**
	 * @param saldoSMS The saldoSMS to set.
	 */
	public void setSaldoSMS(double saldoSMS) {
		this.saldoSMS = saldoSMS;
	}
	/**
	 * @return Returns the valorBonus.
	 */
	public double getValorBonus() {
		return valorBonus;
	}
	/**
	 * @param valorBonus The valorBonus to set.
	 */
	public void setValorBonus(double valorBonus) {
		this.valorBonus = valorBonus;
	}
	/**
	 * @return Returns the valorGPRS.
	 */
	public double getValorGPRS() {
		return valorGPRS;
	}
	/**
	 * @param valorGPRS The valorGPRS to set.
	 */
	public void setValorGPRS(double valorGPRS) {
		this.valorGPRS = valorGPRS;
	}
	/**
	 * @return Returns the valorSMS.
	 */
	public double getValorSMS() {
		return valorSMS;
	}
	/**
	 * @param valorSMS The valorSMS to set.
	 */
	public void setValorSMS(double valorSMS) {
		this.valorSMS = valorSMS;
	}
	
	/**
	 * Metodo....:isCredito
	 * Descricao.:Retorna se o lancamento no extrato e considerado um credito (valor positivo)
	 * @return boolean - Indica se o lancamento e um credito
	 */
	public boolean isCredito()
	{
		return (getValorTotal() >= 0);
	}

	/**
	 * Metodo....:isDebito
	 * Descricao.:Retorna se o lancamento no extrato e considerado um credito (valor positivo)
	 * @return boolean - Indica se o lancamento e um credito
	 */
	public boolean isDebito()
	{
		return (getValorTotal() < 0);
	}
	/**
	 * @return the saldoPeriodico
	 */
	public double getSaldoPeriodico()
	{
		return saldoPeriodico;
	}
	/**
	 * @param saldoPeriodico the saldoPeriodico to set
	 */
	public void setSaldoPeriodico(double saldoPeriodico)
	{
		this.saldoPeriodico = saldoPeriodico;
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
