package br.com.brasiltelecom.ppp.portal.entity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.brt.gpp.comum.Definicoes;

/**
 * Modela a tabela pedido voucher
 * @author Alberto Magno
 * @since 29/06/2004
 */
public class PedidoVoucher 
{
	 private long nroPedido;
	 private Date dataPedido;
	 private char tipoCartao;
	 private Long nroOrdem;
	 private char statusPedido;
	 private Integer statusVoucher;
	 private long nroJobTecnomen;
	 private String statusJob;
	 private RequisitanteVoucher requisitante;
	 
	 private static Map mapTipoCartao = new HashMap();
	 private static Map mapStatusPedido = new HashMap();
	 private static Map mapStatusVoucher = new HashMap();
	 
	 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	 
	 static
	 {
 		// Possibilidades de tipos de cartao
	 	mapTipoCartao.put(Definicoes.TIPO_CARTAO_FISICO,"Físico");
	 	mapTipoCartao.put(Definicoes.TIPO_CARTAO_VIRTUAL,"Virtual");
	 	mapTipoCartao.put(Definicoes.TIPO_CARTAO_LIGMIX,"LigMix");

	 	// Possibilidades de status de pedido
	 	mapStatusPedido.put(Definicoes.STATUS_PEDIDO_VOUCHER_SOLICITADO,"Solicitado");	 	
	 	mapStatusPedido.put(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,"Em Processo de geração");
	 	mapStatusPedido.put(Definicoes.STATUS_PEDIDO_VOUCHER_TRANSMITIDO,"Transmitido");
	 	mapStatusPedido.put(Definicoes.STATUS_PEDIDO_VOUCHER_PROCESSADO,"Processado");
	 	mapStatusPedido.put(Definicoes.STATUS_PEDIDO_VOUCHER_ERRO,"Erro");
	 	
	 	// Possibilidades de status de voucher
	 	mapStatusVoucher.put("0","Geração Solicitada");
	 	mapStatusVoucher.put("1","Criado");
	 	mapStatusVoucher.put("2","Disponibilizado");
	 	mapStatusVoucher.put("3","Recebido");
	 	mapStatusVoucher.put("4","Finalizado");
	 	mapStatusVoucher.put("5","Ativado");
	 	mapStatusVoucher.put("6","Concluído");
	 }
	 
	 public String getDataPedidoString()
	 {
	 	return sdf.format(dataPedido);
	 }
	 
	 public String getTipoCartaoString()
	 {
	 	return mapTipoCartao.get(Character.toString(tipoCartao)).toString();
	 }
	 
	 public String getStatusPedidoString()
	 {
	 	return mapStatusPedido.get(Character.toString(statusPedido)).toString();
	 }
	 
	 public static String getStatusPedidoString(String codigo)
	 {
	 	return mapStatusPedido.get(codigo).toString();
	 }
	 
	 public String getStatusVoucherString()
	 {
	 	return mapStatusVoucher.get(statusVoucher.toString()).toString();
	 }
	 
	 public static String getStatusVoucherString(String status)
	 {
	 	return mapStatusVoucher.get(status).toString();
	 }
	 
	/**
	 * @return Returns the dataPedido.
	 */
	public Date getDataPedido() {
		return dataPedido;
	}
	/**
	 * @param dataPedido The dataPedido to set.
	 */
	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}
	/**
	 * @return Returns the nroOrdem.
	 */
	public Long getNroOrdem() {
		return nroOrdem;
	}
	/**
	 * @param nroOrdem The nroOrdem to set.
	 */
	public void setNroOrdem(Long nroOrdem) {
		this.nroOrdem = nroOrdem;
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
	/**
	 * @return Returns the tipoCartao.
	 */
	public char getTipoCartao() {
		return tipoCartao;
	}
	/**
	 * @param tipoCartao The tipoCartao to set.
	 */
	public void setTipoCartao(char tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	/**
	 * @return Returns the requisitante.
	 */
	public RequisitanteVoucher getRequisitante() {
		return requisitante;
	}
	/**
	 * @param requisitante The requisitante to set.
	 */
	public void setRequisitante(RequisitanteVoucher requisitante)
	{
		this.requisitante = requisitante;
	}
	/**
	 * @return Returns the nroJobTecnomen.
	 */
	public long getNroJobTecnomen()
	{
		return nroJobTecnomen;
	}
	/**
	 * @param nroJobTecnomen The nroJobTecnomen to set.
	 */
	public void setNroJobTecnomen(long nroJobTecnomen)
	{
		this.nroJobTecnomen = nroJobTecnomen;
	}

	public String getStatusJob()
	{
		return statusJob;
	}

	public void setStatusJob(String statusJob)
	{
		this.statusJob = statusJob;
	}
}
