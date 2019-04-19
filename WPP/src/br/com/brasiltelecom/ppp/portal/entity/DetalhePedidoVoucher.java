/*
 * Created on 05/07/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.text.DecimalFormat;

/**
 * @author André Gonçalves
 * @since 05/07/2004
 */
public class DetalhePedidoVoucher {
	
	private long nroPedido;
	private long nroItem;
	private long quantidadeCartoes;
	private double valorFace;
	private double valorFaceBonus;
	private double valorFaceSm;
	private double valorFaceDados;
	private String idEstampa;
	private long nroCaixaLoteInicial;
	private long nroCaixaLoteFinal;
	
	private DecimalFormat valor = new DecimalFormat("#,##0.00");
	private DecimalFormat quantidade = new DecimalFormat ("#,##0");
	
	public String getQuantidadeCartoesString()
	{
		return quantidade.format(quantidadeCartoes);
	}
	
	public String getValorFaceString()
	{
		return valor.format(valorFace);
	}

	public String getValorBonusString()
	{
		return valor.format(valorFaceBonus);
	}
	
	public String getValorSmString()
	{
		return valor.format(valorFaceSm);
	}

	public String getValorDadosString()
	{
		return valor.format(valorFaceDados);
	}

	/**
	 * @return Returns the idEstampa.
	 */
	public String getIdEstampa() {
		return idEstampa;
	}
	/**
	 * @param idEstampa The idEstampa to set.
	 */
	public void setIdEstampa(String idEstampa) {
		this.idEstampa = idEstampa;
	}
	/**
	 * @return Returns the nroCaixaLoteFinal.
	 */
	public long getNroCaixaLoteFinal() {
		return nroCaixaLoteFinal;
	}
	/**
	 * @param nroCaixaLoteFinal The nroCaixaLoteFinal to set.
	 */
	public void setNroCaixaLoteFinal(long nroCaixaLoteFinal) {
		this.nroCaixaLoteFinal = nroCaixaLoteFinal;
	}
	/**
	 * @return Returns the nroCaixaLoteInicial.
	 */
	public long getNroCaixaLoteInicial() {
		return nroCaixaLoteInicial;
	}
	/**
	 * @param nroCaixaLoteInicial The nroCaixaLoteInicial to set.
	 */
	public void setNroCaixaLoteInicial(long nroCaixaLoteInicial) {
		this.nroCaixaLoteInicial = nroCaixaLoteInicial;
	}
	/**
	 * @return Returns the nroItem.
	 */
	public long getNroItem() {
		return nroItem;
	}
	/**
	 * @param nroItem The nroItem to set.
	 */
	public void setNroItem(long nroItem) {
		this.nroItem = nroItem;
	}
	/**
	 * @return Returns the nroPedido.
	 */
	public long getNroPedido() {
		return nroPedido;
	}
	/**
	 * @param nroPedido The nroPedido to set.
	 */
	public void setNroPedido(long nroPedido) {
		this.nroPedido = nroPedido;
	}
	/**
	 * @return Returns the quantidadeCartoes.
	 */
	public long getQuantidadeCartoes() {
		return quantidadeCartoes;
	}
	/**
	 * @param quantidadeCartoes The quantidadeCartoes to set.
	 */
	public void setQuantidadeCartoes(long quantidadeCartoes) {
		this.quantidadeCartoes = quantidadeCartoes;
	}
	/**
	 * @return Returns the valorFace.
	 */
	public double getValorFace() {
		return valorFace;
	}
	/**
	 * @param valorFace The valorFace to set.
	 */
	public void setValorFace(double valorFace) {
		this.valorFace = valorFace;
	}
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
	 * @return Returns the valorFaceData.
	 */
	public double getValorFaceDados() {
		return valorFaceDados;
	}
	/**
	 * @param valorFaceData The valorFaceData to set.
	 */
	public void setValorFaceDados(double valorFaceDados) {
		this.valorFaceDados = valorFaceDados;
	}
	/**
	 * @return Returns the valorFaceSm.
	 */
	public double getValorFaceSm() {
		return valorFaceSm;
	}
	/**
	 * @param valorFaceSm The valorFaceSm to set.
	 */
	public void setValorFaceSm(double valorFaceSm) {
		this.valorFaceSm = valorFaceSm;
	}
}

