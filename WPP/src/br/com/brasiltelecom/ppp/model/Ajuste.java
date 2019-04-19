//Source file: C:\\PPP\\src\\br.com.brasiltelecom.ppp.model\\Ajuste.java

package br.com.brasiltelecom.ppp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.brasiltelecom.ppp.portal.entity.Origem;

/**
 * Modela ajuste para exibição em tela
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Ajuste 
{
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdfe = new SimpleDateFormat("yyyyMMdd");
	
	private String msisdn;
	private Origem origem;
	private double valor;
	private String tipoCredito = "00";
	private String data = sdf.format(new Date());
	private final String sistemaOrigem = "PBT";
	private String usuario;
	private String tipoAjuste;
	private Date dataExpiracao;
	
		
	/**
	 * @roseuid 4043769400AB
	 */
	public Ajuste() 
	{
		
	}
	/**
	 * @return
	 */
	public String getData() {
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 * @return
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * @return
	 */
	public String getSistemaOrigem() {
		return sistemaOrigem;
	}

	/**
	 * @return
	 */
	public String getTipoCredito() {
		return tipoCredito;
	}

	/**
	 * @param origem
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/**
	 * @return
	 */
	public Date getDataExpiracao() {
		return dataExpiracao;
	}
	
	public String getDataExpiracaoString(){
		return sdfe.format(dataExpiracao);
	}

	/**
	 * @return
	 */
	public String getTipoAjuste() {
		return tipoAjuste;
	}

	/**
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param date
	 */
	public void setDataExpiracao(Date date) {
		dataExpiracao = date;
	}

	/**
	 * @param string
	 */
	public void setTipoAjuste(String string) {
		tipoAjuste = string;
	}

	/**
	 * @param string
	 */
	public void setUsuario(String string) {
		usuario = string;
	}

	/**
	 * @return
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param string
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

	/**
	 * @return
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * @param d
	 */
	public void setValor(double d) {
		valor = d;
	}

	/**
	 * @param tipoCredito The tipoCredito to set.
	 */
	public void setTipoCredito(String tipoCredito) {
		this.tipoCredito = tipoCredito;
	}
}
