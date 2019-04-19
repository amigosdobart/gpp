package com.brt.consultas.action.consultaStatus;

public class DadosStatus
{

	private String nome;
	private String conexaoGPP;
	private String conexaoIDLGateway;
	private String conexaoPaymentEngine;
	private String conexaoVoucher;
	private String conexaoBancoDados;
	private String conexaoMiddlewareSMS;
	private String conexaoServidorSocketGPP;
	private String conexaoServidorOracleReports;

	public DadosStatus(String nome,
			 String conexaoGPP,
			 String conexaoIDLGateway,
			 String conexaoPaymentEngine,
			 String conexaoVoucher,
			 String conexaoBancoDados,
			 String conexaoMiddlewareSMS,
			 String conexaoServidorSocketGPP,
			 String conexaoServidorOracleReports
			)
	{
		this.nome=nome;
		this.conexaoGPP=conexaoGPP;
		this.conexaoIDLGateway=conexaoIDLGateway;
		this.conexaoPaymentEngine=conexaoPaymentEngine;
		this.conexaoVoucher=conexaoVoucher;
		this.conexaoBancoDados=conexaoBancoDados;
		this.conexaoMiddlewareSMS=conexaoMiddlewareSMS;
		this.conexaoServidorSocketGPP=conexaoServidorSocketGPP;
		this.conexaoServidorOracleReports=conexaoServidorOracleReports;
		
	}
	/**
	 * @return Returns the conexaoBancoDados.
	 */
	public String getConexaoBancoDados() {
		return conexaoBancoDados;
	}
	/**
	 * @param conexaoBancoDados The conexaoBancoDados to set.
	 */
	public void setConexaoBancoDados(String conexaoBancoDados) {
		this.conexaoBancoDados = conexaoBancoDados;
	}
	/**
	 * @return Returns the conexaoGPP.
	 */
	public String getConexaoGPP() {
		return conexaoGPP;
	}
	/**
	 * @param conexaoGPP The conexaoGPP to set.
	 */
	public void setConexaoGPP(String conexaoGPP) {
		this.conexaoGPP = conexaoGPP;
	}
	/**
	 * @return Returns the conexaoIDLGateway.
	 */
	public String getConexaoIDLGateway() {
		return conexaoIDLGateway;
	}
	/**
	 * @param conexaoIDLGateway The conexaoIDLGateway to set.
	 */
	public void setConexaoIDLGateway(String conexaoIDLGateway) {
		this.conexaoIDLGateway = conexaoIDLGateway;
	}
	/**
	 * @return Returns the conexaoMiddlewareSMS.
	 */
	public String getConexaoMiddlewareSMS() {
		return conexaoMiddlewareSMS;
	}
	/**
	 * @param conexaoMiddlewareSMS The conexaoMiddlewareSMS to set.
	 */
	public void setConexaoMiddlewareSMS(String conexaoMiddlewareSMS) {
		this.conexaoMiddlewareSMS = conexaoMiddlewareSMS;
	}
	/**
	 * @return Returns the conexaoPaymentEngine.
	 */
	public String getConexaoPaymentEngine() {
		return conexaoPaymentEngine;
	}
	/**
	 * @param conexaoPaymentEngine The conexaoPaymentEngine to set.
	 */
	public void setConexaoPaymentEngine(String conexaoPaymentEngine) {
		this.conexaoPaymentEngine = conexaoPaymentEngine;
	}
	/**
	 * @return Returns the conexaoServidorOracleReports.
	 */
	public String getConexaoServidorOracleReports() {
		return conexaoServidorOracleReports;
	}
	/**
	 * @param conexaoServidorOracleReports The conexaoServidorOracleReports to set.
	 */
	public void setConexaoServidorOracleReports(
			String conexaoServidorOracleReports) {
		this.conexaoServidorOracleReports = conexaoServidorOracleReports;
	}
	/**
	 * @return Returns the conexaoServidorSocketGPP.
	 */
	public String getConexaoServidorSocketGPP() {
		return conexaoServidorSocketGPP;
	}
	/**
	 * @param conexaoServidorSocketGPP The conexaoServidorSocketGPP to set.
	 */
	public void setConexaoServidorSocketGPP(String conexaoServidorSocketGPP) {
		this.conexaoServidorSocketGPP = conexaoServidorSocketGPP;
	}
	/**
	 * @return Returns the conexaoVoucher.
	 */
	public String getConexaoVoucher() {
		return conexaoVoucher;
	}
	/**
	 * @param conexaoVoucher The conexaoVoucher to set.
	 */
	public void setConexaoVoucher(String conexaoVoucher) {
		this.conexaoVoucher = conexaoVoucher;
	}
	
	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
}