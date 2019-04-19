/*
 * Created on 03/03/2005
 */
package br.com.brasiltelecom.ppp.model;

//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;

//import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 * @author Henrique Canto
 *
 */
public class ExtratoBoomerang {

	
	//private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//private PhoneNumberFormat ph = new PhoneNumberFormat();		
	//private static DecimalFormat df = new DecimalFormat("#,##0.00");
	private String numeroDestino;
	private String data;
	private String horaChamada;
	private String operacao;
	private String regiaoOrigem;
	private String regiaoDestino;
	private String duracaoChamada;
	private String valorBoomerang;
	
	
	
	/**
	 * @return Returns the data.
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data The data to set.
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return Returns the duracaoChamada.
	 */
	public String getDuracaoChamada() {
		return duracaoChamada;
	}
	/**
	 * @param duracaoChamada The duracaoChamada to set.
	 */
	public void setDuracaoChamada(String duracaoChamada) {
		this.duracaoChamada = duracaoChamada;
	}
	/**
	 * @return Returns the horaChamada.
	 */
	public String getHoraChamada() {
		return horaChamada;
	}
	/**
	 * @param horaChamada The horaChamada to set.
	 */
	public void setHoraChamada(String horaChamada) {
		this.horaChamada = horaChamada;
	}
	/**
	 * @return Returns the numeroDestino.
	 */
	public String getNumeroDestino() {
		return numeroDestino;
	}
	/**
	 * @param numeroDestino The numeroDestino to set.
	 */
	public void setNumeroDestino(String numeroDestino) {
		this.numeroDestino = numeroDestino;
	}
	/**
	 * @return Returns the operacao.
	 */
	public String getOperacao() {
		return operacao;
	}
	/**
	 * @param operacao The operacao to set.
	 */
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	/**
	 * @return Returns the regiaoDestino.
	 */
	public String getRegiaoDestino() {
		return regiaoDestino;
	}
	/**
	 * @param regiaoDestino The regiaoDestino to set.
	 */
	public void setRegiaoDestino(String regiaoDestino) {
		this.regiaoDestino = regiaoDestino;
	}
	/**
	 * @return Returns the regiaoOrigem.
	 */
	public String getRegiaoOrigem() {
		return regiaoOrigem;
	}
	/**
	 * @param regiaoOrigem The regiaoOrigem to set.
	 */
	public void setRegiaoOrigem(String regiaoOrigem) {
		this.regiaoOrigem = regiaoOrigem;
	}
	/**
	 * @return Returns the valorBoomerang.
	 */
	public String getValorBoomerang() {
		return valorBoomerang;
	}
	/**
	 * @param valorBoomerang The valorBoomerang to set.
	 */
	public void setValorBoomerang(String valorBoomerang) {
		this.valorBoomerang = valorBoomerang;
	}
	/**
	 * @return Returns the numeroDestino.
	 */
	public String getNumeroDestinoString()
	{
		if (numeroDestino.charAt(0)=='0')
		{
			numeroDestino = numeroDestino.substring(1);
		}		
		if (numeroDestino != null && !numeroDestino.trim().equals("") && numeroDestino.length() == 10) 
		{
			return "(" + numeroDestino.substring(0,2) + ") " + numeroDestino.substring(2,6) + "-" + numeroDestino.substring(6,10);
		} 
		else if(numeroDestino != null && !numeroDestino.trim().equals("") && numeroDestino.length() == 9)
		{
			return "(" + numeroDestino.substring(0,2) + ") " + numeroDestino.substring(2,5) + "-" + numeroDestino.substring(5,9);
		} 
		return numeroDestino;
	}
	
}
