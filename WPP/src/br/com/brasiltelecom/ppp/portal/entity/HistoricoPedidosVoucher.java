/*
 * Created on 06/07/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author André Gonçalves
 * @since 06/07/2004
 */
public class HistoricoPedidosVoucher {

	private long nroPedido;
	private Date dataAtualizacaoStatus;
	private char statusPedido;
	private Integer statusVoucher;
	private String nomeUsuario;
	
	private static Map mapStatusVoucher = new HashMap();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	 
	static
	{
		// Possibilidades de status de voucher
	 	mapStatusVoucher.put("Geração Solicitada", "0");
	 	mapStatusVoucher.put("Criado", "1");
	 	mapStatusVoucher.put("Disponibilizado", "2");
	 	mapStatusVoucher.put("Recebido", "3");
	 	mapStatusVoucher.put("Finalizado", "4");
	 	mapStatusVoucher.put("Ativado", "5");
	 	mapStatusVoucher.put("Concluído", "6");
	}
	
	public String getDataAtualizacaoStatusString()
	{
		return sdf.format(dataAtualizacaoStatus);
	}
	
	public static String getCodigoStatusVoucher( String status )
	{
		return mapStatusVoucher.get(status).toString();
	}
	
	public String getStatusVoucherString()
	{
		return PedidoVoucher.getStatusVoucherString(statusVoucher.toString());
	}
	
	public String getStatusPedidoString()
	{
		return PedidoVoucher.getStatusPedidoString("" + statusPedido);
	}

	/**
	 * @return Returns the dataAtualizacaoStatus.
	 */
	public Date getDataAtualizacaoStatus() {
		return dataAtualizacaoStatus;
	}
	/**
	 * @param dataAtualizacaoStatus The dataAtualizacaoStatus to set.
	 */
	public void setDataAtualizacaoStatus(Date dataAtualizacaoStatus) {
		this.dataAtualizacaoStatus = dataAtualizacaoStatus;
	}
	/**
	 * @return Returns the nomeUsuario.
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	/**
	 * @param nomeUsuario The nomeUsuario to set.
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
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
	 * @return Returns the statusPedido.
	 */
	public char getStatusPedido() {
		return statusPedido;
	}
	/**
	 * @param statusPedido The statusPedido to set.
	 */
	public void setStatusPedido(char statusPedido) {
		this.statusPedido = statusPedido;
	}
	/**
	 * @return Returns the statusVoucher.
	 */
	public Integer getStatusVoucher() {
		return statusVoucher;
	}
	/**
	 * @param statusVoucher The statusVoucher to set.
	 */
	public void setStatusVoucher(Integer statusVoucher) {
		this.statusVoucher = statusVoucher;
	}
}
