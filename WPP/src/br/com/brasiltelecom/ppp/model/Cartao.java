package br.com.brasiltelecom.ppp.model;

import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Modela as informações de Cartão
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 * 
 * Modificado por: Marcos C. Magalhães
 * 28/11/2005. Modificacao: inclusao do atributo e dos metodos de get e set do codigoSeguranca (PIN)
 * 
 */
public class Cartao 
{
	private int    idCartao;
	private String msisdn;
	private short  tipoVoucher; 
	private Date   dataAtivacao;
	private String status;
	private double valorFace;
	private String codigoRetorno;
	private String codStatus;
    private String motivoBloqueio;
    private String desMotivoBloqueio;
    private String codigoSeguranca; //PIN

	private double valorFaceBonus;
	private double valorFaceGPRS;
	private double valorFaceSM;

	private Date dataExpiracaoCredPrincipal;
	private Date dataExpiracaoCredBonus;
	private Date dataExpiracaoCredSm;
	private Date dataExpiracaoCredDados;
	
	private String canceladoPor;
	private Date   dataCancelamento;
    
	private SimpleDateFormat sdF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private DecimalFormat dF = new DecimalFormat("##0.00");
    
	/**
	 * @return Returns the valorFaceBonus.
	 */
	public double getValorFaceBonus() {
		return valorFaceBonus;
	}
	/**
	 * @param valorFaceBonus The valorFaceBonus to set.
	 */
	public void setValorFaceBonus(double valorFaceBonus) {
		this.valorFaceBonus = valorFaceBonus;
	}
	/**
	 * @return Returns the valorFaceGPRS.
	 */
	public double getValorFaceGPRS() {
		return valorFaceGPRS;
	}
	/**
	 * @param valorFaceGPRS The valorFaceGPRS to set.
	 */
	public void setValorFaceGPRS(double valorFaceGPRS) {
		this.valorFaceGPRS = valorFaceGPRS;
	}
	/**
	 * @return Returns the valorFaceSM.
	 */
	public double getValorFaceSM() {
		return valorFaceSM;
	}
	/**
	 * @param valorFaceSM The valorFaceSM to set.
	 */
	public void setValorFaceSM(double valorFaceSM) {
		this.valorFaceSM = valorFaceSM;
	}

	public String getFdata() {	
		if (dataAtivacao != null) {
			return sdF.format(dataAtivacao);
		} else {
			return "";
		}		
	}
	
	public String getFmsisdn() {	
		try{
			if (msisdn != null && !msisdn.trim().equals("")) {
				return "(" + msisdn.substring(2,4) + ") " + msisdn.substring(4,8) + "-" + msisdn.substring(8,12);
			} else {
				return "";
			}
		}
		catch(Exception e){
			return msisdn;					
		}
	}
	
	public String getFvalorFace() {	
		return "R$" + dF.format(valorFace);
	}	
	
	public String getFvalorFaceBonus() {	
		return "R$" + dF.format(valorFaceBonus);   
	}	
	public String getFvalorFaceGPRS() {	
		return "R$" + dF.format(valorFaceGPRS);   
	}	
	public String getFvalorFaceSM() {	
		return "R$" + dF.format(valorFaceSM);   
	}	

	/**
	 * @roseuid 4043769201F4
	 */
	public Cartao() 
	{
		
	}
	
	/**
	 * Access method for the idCartao property.
	 * 
	 * @return   the current value of the idCartao property
	 */
	public int getIdCartao() 
	{
		return idCartao;
		}
	
	/**
	 * Sets the value of the idCartao property.
	 * 
	 * @param aIdCartao the new value of the idCartao property
	 */
	public void setIdCartao(int aIdCartao) 
	{
		idCartao = aIdCartao;
		}
	
	/**
	 * Access method for the dataAtivacao property.
	 * 
	 * @return   the current value of the dataAtivacao property
	 */
	public java.util.Date getDataAtivacao() 
	{
		return dataAtivacao;
		}
	
	/**
	 * Sets the value of the dataAtivacao property.
	 * 
	 * @param aDataAtivacao the new value of the dataAtivacao property
	 */
	public void setDataAtivacao(java.util.Date aDataAtivacao) 
	{
		dataAtivacao = aDataAtivacao;
		}
	

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
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
	public double getValorFace() {
		return valorFace;
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
	public void setValorFace(double vlrFace) {
		valorFace = vlrFace;
	}

	/**
	 * @return
	 */
	public String getCodigoRetorno() {
		return codigoRetorno;
	}

	/**
	 * @param string
	 */
	public void setCodigoRetorno(String string) {
		codigoRetorno = string;
	}

	/**
	 * @return
	 */
	public String getCodStatus() {
		return codStatus;
	}

	/**
	 * @return
	 */
	public String getDesMotivoBloqueio() {
		return desMotivoBloqueio;
	}

	/**
	 * @return
	 */
	public String getCodigoSeguranca() {
		return codigoSeguranca;
	}
	
	
	/**
	 * @return
	 */
	public String getMotivoBloqueio() {
		return motivoBloqueio;
	}

	/**
	 * @return
	 */
	public SimpleDateFormat getSdF() {
		return sdF;
	}

	/**
	 * @param string
	 */
	public void setCodStatus(String string) {
		codStatus = string;
	}

	/**
	 * @param string
	 */
	public void setDesMotivoBloqueio(String string) {
		desMotivoBloqueio = string;
	}

	/**
	 * @param string
	 */
	public void setCodigoSeguranca(String string) {
		codigoSeguranca = string;
	}
		
	/**
	 * @param string
	 */
	public void setMotivoBloqueio(String string) {
		motivoBloqueio = string;
	}

	/**
	 * @param format
	 */
	public void setSdF(SimpleDateFormat format) {
		sdF = format;
	}

	/**
	 * @return Returns the dataExpiracaoCredBonus.
	 */
	public Date getDataExpiracaoCredBonus() {
		return dataExpiracaoCredBonus;
	}
	/**
	 * @param dataExpiracaoCredBonus The dataExpiracaoCredBonus to set.
	 */
	public void setDataExpiracaoCredBonus(Date dataExpiracaoCredBonus) {
		this.dataExpiracaoCredBonus = dataExpiracaoCredBonus;
	}
	/**
	 * @return Returns the dataExpiracaoCredDados.
	 */
	public Date getDataExpiracaoCredDados() {
		return dataExpiracaoCredDados;
	}
	/**
	 * @param dataExpiracaoCredDados The dataExpiracaoCredDados to set.
	 */
	public void setDataExpiracaoCredDados(Date dataExpiracaoCredDados) {
		this.dataExpiracaoCredDados = dataExpiracaoCredDados;
	}
	/**
	 * @return Returns the dataExpiracaoCredPrincipal.
	 */
	public Date getDataExpiracaoCredPrincipal() {
		return dataExpiracaoCredPrincipal;
	}
	/**
	 * @param dataExpiracaoCredPrincipal The dataExpiracaoCredPrincipal to set.
	 */
	public void setDataExpiracaoCredPrincipal(Date dataExpiracaoCredPrincipal) {
		this.dataExpiracaoCredPrincipal = dataExpiracaoCredPrincipal;
	}
	/**
	 * @return Returns the dataExpiracaoCredSm.
	 */
	public Date getDataExpiracaoCredSm() {
		return dataExpiracaoCredSm;
	}
	/**
	 * @param dataExpiracaoCredSm The dataExpiracaoCredSm to set.
	 */
	public void setDataExpiracaoCredSm(Date dataExpiracaoCredSm) {
		this.dataExpiracaoCredSm = dataExpiracaoCredSm;
	}
	/**
	 * @return Returns the tipoVoucher.
	 */
	public short getTipoVoucher() {
		return tipoVoucher;
	}
	/**
	 * @param tipoVoucher The tipoVoucher to set.
	 */
	public void setTipoVoucher(short tipoVoucher) {
		this.tipoVoucher = tipoVoucher;
	}
	/**
	 * @return Returns the canceladoPor.
	 */
	public String getCanceladoPor() {
		return canceladoPor;
	}
	/**
	 * @param canceladoPor The canceladoPor to set.
	 */
	public void setCanceladoPor(String canceladoPor) {
		this.canceladoPor = canceladoPor;
	}
	/**
	 * @return Returns the dataCancelamento.
	 */
	public Date getDataCancelamento() {
		return dataCancelamento;
	}
	/**
	 * @param dataCancelamento The dataCancelamento to set.
	 */
	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}
}
