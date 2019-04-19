package br.com.brasiltelecom.ppp.model;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 * Classe que mapeia os dados de extrato
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class Extrato 
{
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat hdf = new SimpleDateFormat("H:mm:ss");
	private PhoneNumberFormat ph = new PhoneNumberFormat();		
	private static DecimalFormat df = new DecimalFormat("##,##0.00", new DecimalFormatSymbols(new Locale("BR", "pt", "")));

	/**
	* Identificação da Chamada
	*/
	private String id;
	
	/**
	 * Número do Acesso de Origem da Chamada/Cobrança/Recarga
	 */
	private String numeroOrigem;
	
	/**
	 * Data da Chamada/Cobrança/Recarga
	 */
	private String data;
	
	/**
	 * Hora da Chamada
	 */
	private String horaChamada;
		
	/**
	 * Tipo da Tarifação da Chamada
	 */
	private String tipoTarifacao;
	
	/**
	 * Descrição do Item de Chamada ou de Cobrança/Recarga
	 */
	private String operacao;
	
	/**
	 * Região de Origem da Chamada Efetuada
	 */
	private String regiaoOrigem;
	
	/**
	 * Número do Acesso de Destino da Chamada
	 */
	private String numeroDestino;
	
	/**
	 * Duração da Chamada
	 */
	private String duracaoChamada;
	
	/**
	 * Valor da Chamada/Cobrança/Recarga
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
	 * Região de Destino da ligação 
	 */
	private String regiaoDestino;
	
	/**
	 * Construtor padrão de Extrato
	 */
	public Extrato() 
	{		
	}

	/**
	 * Obtém o número do Acesso de Destino da Chamada
	 * @return Número do Acesso de Destino da Chamada
	 */
	public String getNumeroDestino() {
		return numeroDestino;
	}

	/**
	 * Obtém o número do Acesso de Origem da Chamada/Cobrança/Recarga
	 * @return número do Acesso de Origem da Chamada/Cobrança/Recarga
	 */
	public String getNumeroOrigem() {
		return numeroOrigem;
	}

	/**
	 * Obtém a descrição do Item de Chamada ou de Cobrança/Recarga
	 * @return Descrição do Item de Chamada ou de Cobrança/Recarga
	 */
	public String getOperacao() {
		return operacao;
	}

	/**
	 * Obtém a região de Origem da Chamada Efetuada
	 * @return Região de Origem da Chamada Efetuada
	 */
	public String getRegiaoOrigem() {
		return regiaoOrigem;
	}

	/**
	 * Obtém o saldo de Créditos após Cobrança/Recarga
	 * @return Saldo de Créditos após Cobrança/Recarga
	 */
	public double getSaldoPrincipal() {
		return saldoPrincipal;
	}

	/**
	 * Obtém o tipo da Tarifação da Chamada
	 * @return Tipo da Tarifação da Chamada
	 */
	public String getTipoTarifacao() {
		return tipoTarifacao;
	}

	/**
	 * Obtém o valor da Chamada/Cobrança/Recarga
	 * @return Valor da Chamada/Cobrança/Recarga
	 */
	public double getValorPrincipal() {
		return valorPrincipal;
	}

	/**
	 * Seta o número do Acesso de Destino da Chamada
	 * @param string Número do Acesso de Destino da Chamada a se setar
	 */
	public void setNumeroDestino(String string) {
		numeroDestino = string;
	}

	/**
	 * Seta o número do Acesso de Origem da Chamada/Cobrança/Recarga
	 * @param string número do Acesso de Origem da Chamada/Cobrança/Recarga a se setar
	 */
	public void setNumeroOrigem(String string) {
		numeroOrigem = string;
	}

	/**
	 * Seta a descrição do Item de Chamada ou de Cobrança/Recarga
	 * @param string Descrição do Item de Chamada ou de Cobrança/Recarga a se setar
	 */
	public void setOperacao(String string) {
		operacao = string;
	}

	/**
	 * Seta a região de Origem da Chamada Efetuada
	 * @param string Região de Origem da Chamada Efetuada a se setar
	 */
	public void setRegiaoOrigem(String string) {
		regiaoOrigem = string;
	}

	/**
	 * Seta o saldo de Créditos após Cobrança/Recarga
	 * @param d Saldo de Créditos após Cobrança/Recarga a se setar
	 */
	public void setSaldoPrincipal(double d) {
		saldoPrincipal = d;
	}

	/**
	 * Seta o tipo da Tarifação da Chamada
	 * @param string Tipo da Tarifação da Chamada a se setar
	 */
	public void setTipoTarifacao(String string) {
		tipoTarifacao = string;
	}

	/**
	 * Seta o valor da Chamada/Cobrança/Recarga
	 * @param d Valor da Chamada/Cobrança/Recarga a se setar
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
