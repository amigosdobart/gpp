package com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity;

import java.util.Date;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Iterator;
import java.text.SimpleDateFormat;

/**
  *
  * Esta classe contem os atributos de armazenagem de informacoes
  * sobre o Pedido de criacao de Vouchers 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Joao Carlos
  * Data:               24/06/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class VoucherPedido implements Comparable
{
	private long 		numPedido;
	private String 		nomeRequisitante;
	private String      eMailRequisitante;
	private String		idRequisitante;
	private Date		dataPedido;
	private Date		dataCriacao;
	private Date		dataEnvioEmail;
	private String		tipCartao;
	private String		nomeArquivo;
	private String		statusPedido;
	private int         statusVoucher;
	private long		numOrdem;
	private int			numJobTecnomen;
	private Collection	voucherItens;

	/**
	 * Metodo...:VoucherPedido
	 * Descricao:Construtor da classe
	 * @param numPedido - Numero do Pedido a ser criado (Chave Primaria)
	 */
	public VoucherPedido(long numPedido)
	{
		setNumPedido(numPedido);
		voucherItens = new TreeSet();
	}

	/**
	 * Metodo....:getDataCriacao
	 * Descricao.:Retorna a data de criacao dos vouchers deste pedido
	 * @return Date	- Data de criacao dos vouchers deste pedido 
	 */
	public Date getDataCriacao() 
	{
		return dataCriacao;
	}

	/**
	 * Metodo....:getDataPedido
	 * Descricao.:Retorna a data de criacao do pedido
	 * @return Date	- Data de criacao do pedido
	 */
	public Date getDataPedido() 
	{
		return dataPedido;
	}

	/**
	 * Metodo....:getDataPedidoFormatada
	 * Descricao.:Retorna a data do pedido formatada na mascara dd/mm/yyyy hh:mm:ss
	 * @return String - Data do Pedido formatada
	 */
	public String getDataPedidoFormatada()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(getDataPedido());
	}

	/**
	 * Metodo....:getNomeArquivo
	 * Descricao.:Retorna o nome do arquivo criptgrafado com os dados de vouchers
	 * @return String - Nome do Arquivo que contém informacoes dos PINs de vouchers
	 */
	public String getNomeArquivo() 
	{
		return nomeArquivo;
	}

	/**
	 * Metodo....:getNomeRequisitante
	 * Descricao.:Retorna o nome do requisitante da criacao de pedidos
	 * @return String - Nome do requisitante
	 */
	public String getNomeRequisitante() 
	{
		return nomeRequisitante;
	}

	/**
	 * Metodo....:getNumPedido
	 * Descricao.:Retorna o numero do Pedido
	 * @return long - Numero do Pedido
	 */
	public long getNumPedido() 
	{
		return numPedido;
	}

	/**
	 * Metodo....:getTipCartao
	 * Descricao.:Retorna o tipo dos vouchers a serem criados (Fisicos ou Virtuais)
	 * @return String - Tipo de cartoes a serem criados (Fisicos ou Virtuais)
	 */
	public String getTipCartao() 
	{
		return tipCartao;
	}

	/**
	 * Metodo....:getStatusPedido
	 * Descricao.:Retorna o status do pedido
	 * @return String - Status do pedido
	 */
	public String getStatusPedido() 
	{
		return statusPedido;
	}

	/**
	 * Metodo....:getNumOrdem
	 * Descricao.:Retorna o numero da ordem desse pedido
	 * @return long - Numero da ordem
	 */
	public long getNumOrdem() 
	{
		return numOrdem;
	}

	/**
	 * Metodo....:getStatusVoucher
	 * Descricao.:Retorna o status do voucher deste pedido
	 * @return int - Status do Voucher
	 */
	public int getStatusVoucher() 
	{
		return statusVoucher;
	}

	/**
	 * Metodo....:getEMailRequisitante
	 * Descricao.:Retorna o e-mail do requisitante
	 * @return String - EMail do requisitante
	 */
	public String getEMailRequisitante() 
	{
		return eMailRequisitante;
	}

	/**
	 * Metodo....:getIdRequisitante
	 * Descricao.:Retorna o Id do requisitante
	 * @return long - Id do requisitante
	 */
	public String getIdRequisitante()
	{
		return idRequisitante;
	}

	/**
	 * Metodo....:getNumJobTecnomen
	 * Descricao.:Retorna o numero do job sendo executado para este pedido na tecnomen
	 * @return int - Numero do Job na Tecnomen
	 */
	public int getNumJobTecnomen()
	{
		return numJobTecnomen;
	}

	/**
	 * Metodo....:getDataEnvioEmail
	 * Descricao.:Retorna a data de envio da ordem por e-mail
	 */
	public Date getDataEnvioEmail()
	{
		return dataEnvioEmail;
	}

	/**
	 * Metodo....:setDataCriacao
	 * Descricao.:Atribui uma data de criacao para o pedido de vouchers
	 * @param dataCriacao - Data de criacao dos vouchers deste pedido
	 */
	public void setDataCriacao(Date dataCriacao) 
	{
		this.dataCriacao = dataCriacao;
	}

	/**
	 * Metodo....:setDataPedido
	 * Descricao.:Atribui uma data de criacao do pedido
	 * @param dataPedido - Data de criacao do pedido
	 */
	public void setDataPedido(Date dataPedido) 
	{
		this.dataPedido = dataPedido;
	}

	/**
	 * Metodo....:setNomeArquivo
	 * Descricao.:Atribui o nome de arquivo ao pedido
	 * @param nomeArquivo - Nome do arquivo criptografado de vouchers
	 */
	public void setNomeArquivo(String nomeArquivo) 
	{
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * Metodo....:setNomeRequisitante
	 * Descricao.:Atribui o nome do requisitante da criacao dos vouchers
	 * @param nomeRequisitante - Nome do requisitante
	 */
	public void setNomeRequisitante(String nomeRequisitante) 
	{
		this.nomeRequisitante = nomeRequisitante;
	}

	/**
	 * Metodo....:setEMailRequisitante
	 * Descricao.:Atribui o email do requisitante da criacao dos vouchers
	 * @param eMailRequisitante - EMail do requisitante
	 */
	public void setEMailRequisitante(String eMailRequisitante) 
	{
		this.eMailRequisitante = eMailRequisitante;
	}

	/**
	 * Metodo....:setNumPedido
	 * Descricao.:Atribui o numero de pedido
	 * 
	 * Obs: Este metodo e privado, pois o numero so podera ser atribuido na criacao
	 *      do objeto
	 * @param numPedido - Numero do Pedido
	 */
	private void setNumPedido(long numPedido)
	{
		this.numPedido = numPedido;
	}

	/**
	 * Metodo....:setTipCartao
	 * Descricao.:Atribui um tipo de cartao ao pedido
	 * @param tipCartao - Identificado do tipo de cartao (F-Fisico ou V-Virtual)
	 */
	public void setTipCartao(String tipCartao) 
	{
		this.tipCartao = tipCartao;
	}

	/**
	 * Metodo....:setStatusPedido
	 * Descricao.:Atribui um status para o pedido
	 * @param statusPed - Status a ser atribuido
	 */
	public void setStatusPedido(String statusPed) 
	{
		this.statusPedido = statusPed;
	}

	/**
	 * Metodo....:setStatusVoucher
	 * Descricao.:Atribui um status para o voucher
	 * @param statusVou - Status a ser atribuido
	 */
	public void setStatusVoucher(int statusVou) 
	{
		this.statusVoucher = statusVou;
	}

	/**
	 * Metodo....:setNumOrdem
	 * Descricao.:Atribui o numero da ordem desse pedido
	 * 
	 * @param numOrdem - Numero da Ordem
	 */
	public void setNumOrdem(long numOrdem)
	{
		this.numOrdem = numOrdem;
	}

	/**
	 * Metodo....:setIdRequisitante
	 * Descricao.:Atribui id do requisitante ao pedido
	 * 
	 * @param idRequisitante - Id do Requisitante
	 */
	public void setIdRequisitante(String idRequisitante)
	{
		this.idRequisitante = idRequisitante;
	}

	/**
	 * Metodo....:setNumJobTecnomen
	 * Descricao.:Define o numero do job da tecnomen sendo executado para este pedido
	 * @param numJobTecnomen - Numero do Job na Tecnomen
	 */
	public void setNumJobTecnomen(int numJobTecnomen)
	{
		this.numJobTecnomen = numJobTecnomen;
	}

	/**
	 * Metodo....:setDataEnvioEmail
	 * Descricao.:Define a data de envio do email da ordem
	 */
	public void setDataEnvioEmail(Date dataEnvioEmail)
	{
		this.dataEnvioEmail = dataEnvioEmail;
	}

	/**
	 * Metodo....:addVoucherItem
	 * Descricao.:Adiciona um objeto de iten de pedido do voucher a colecao
	 *            que o cabecalho do pedido tem dominio
	 * @param itemVoucher 	- Item de pedido de criacao do Voucher a ser adicionado
	 * @return boolean 		- Indica se foi possivel adicionar ou nao o elemento
	 */	
	public boolean addVoucherItem(VoucherPedidoItem itemVoucher)
	{
		return voucherItens.add(itemVoucher);
	}

	/**
	 * Metodo....:removeVoucherItem
	 * Descricao.:Remove um objeto de item de pedido do voucher da colecao
	 *            de itens que o cabecalho do pedido tem dominio
	 * @param itemVoucher	- Item do pedido de criacao de voucher a ser removido
	 * @return				- Indica se foi possivel ou nao remover o item.
	 */
	public boolean removeVoucherItem(VoucherPedidoItem itemVoucher)
	{
		return voucherItens.remove(itemVoucher);
	}

	/**
	 * Metodo....:getNumeroItens
	 * Descricao.:Retorna o numero de itens do pedido
	 * @return int - Numero de itens do pedido
	 */
	public int getNumeroItens()
	{
		return voucherItens.size();
	}

	/**
	 * Metodo....:getItensPedido
	 * Descricao.:Retorna os itens do pedido
	 * @return Collection - Itens do pedido
	 */
	public Collection getItensPedido()
	{
		return voucherItens;
	}

	/**
	 * Metodo....:getItemPedido
	 * Descricao.:Retorna o item de pedido dado o numero deste item
	 *            Obs: Os itens comecam a partir do indice 0 
	 * @param numItem 				- Numero do item desejado
	 * @return	VoucherPedidoItem	- Item do pedido
	 */
	public VoucherPedidoItem getItemPedido(long numItem)
	{
		VoucherPedidoItem itens[] = (VoucherPedidoItem[])voucherItens.toArray(new VoucherPedidoItem[0]);
		return itens[(int)numItem];
	}

	/**
	 * Metodo....:getNumElementosParaOrdem
	 * Descricao.:Retorna o numero de elementos para a criacao da ordem
	 * @return int - Numero de elementos
	 */
	public int getNumElementosParaOrdem()
	{
		int numeroTotalElementos = getItensPedido().size();
		for (Iterator i=getItensPedido().iterator(); i.hasNext();)
		{
			VoucherPedidoItem item = (VoucherPedidoItem)i.next();
			if (item.getSubItem() != null)
				numeroTotalElementos++;
		}
		return numeroTotalElementos;
	}
	
	/**
	 * Metodo....:toHTML
	 * Descricao.:Retorna os dados do Pedido em formato HTML
	 * @return String
	 */
	public String toHTML()
	{
		String paginaHTML =
		"<html>" +
		"<head>" +
		"	<title>Pedido de Cartões SMP BrasilTelecom</title>" +
		"	<metahttp-equiv=Content-Typecontent=text/html;charset=iso-8859-1>" +
		"</head>" +
		"<body bgcolor=#E7E8EC>" +
		"<BR>" +
		"<style type=text/css>" +
		"<!--" +
		".fontverde3 {color:#497D32; font-size:12px; font-weight:bold;}" +
		".fontverde {color:#16360C; font-size:12px;}" +
		"-->" +
		"</style>" +
		"<table width=100% border=0 bordercolor=#C0E0AF cellspacing=0 cellpadding=2 style=border-collapse: collapse bgcolor=#FFFFFF>" +
		"<tr>" +
		"	<td width=150 class=fontverde align=left bgcolor=#E7E8EC><b>Detalhes do Pedido</b></td>" +
		"	<td bgcolor=#E7E8EC></td>" +
		"</tr>" +
		"<tr>" +
		"	<td width=150 class=fontverde align=left>Numero do Pedido:</td>" +
		"	<td><fontverde3><b>"+getNumPedido()+"</b></fontverde3></td>" +
		"</tr>" +
		"<tr>" +
		"	<td width=150 class=fontverde aling=left>Data da Solicitacao:</td>" +
		"	<td><fontverde3><b>"+getDataPedidoFormatada()+"</b></td>" +
		"</tr>" +
		"<tr>" +
		"	<td width=150 class=fontverde aling=left>Nome do Arquivo:</td>" +
		"	<td><fontverde3><b>"+getNomeArquivo()+"</b></td>" +
		"</tr>" +
		"</table>" +
		"<table border=1 width=100% bordercolor=#C0E0AF cellspacing=0 cellpadding=5 style=border-collapse: collapse bgcolor=#FFFFFF>" +
		"	<tr>" +
		"		<td volspan=2 bgcolor=#E1EA99 align=center class=fontverde3>Item</th>" +
		"		<td bgcolor=#E1EA99 align=center class=fontverde3>Qtde Cartoes</th>" +
		"		<td bgcolor=#E1EA99 align=center class=fontverde3>Valor Face</th>" +
		"		<td bgcolor=#E1EA99 align=center class=fontverde3>Estampa</th>" +
		"		<td bgcolor=#E1EA99 align=center class=fontverde3>Numero Lote Inicial</th>" +
		"		<td bgcolor=#E1EA99 align=center class=fontverde3>Numero Lote Final</th>" +
		"	</tr>";

		for (Iterator i=getItensPedido().iterator(); i.hasNext();)
		{
			VoucherPedidoItem item = (VoucherPedidoItem)i.next();
			String estampa = item.getEstampa() == null ? "&nbsp" : item.getEstampa();
			paginaHTML += 	"	  <tr>" +
							"	    <td class=fontverde align=center >"+item.getNumItem()+"</td>" +
							"	    <td class=fontverde align=center >"+item.getQtdeCartoes()+"</td>" +
							"	    <td class=fontverde align=center > R$ "+item.getValorFace() +"</td>	" +
							"	    <td class=fontverde align=center >"+estampa+"</td>" +
							"	    <td class=fontverde align=center >"+item.getNumCaixaLoteInicial()+"</td>" +
							"	    <td class=fontverde align=center >"+item.getNumCaixaLoteFinal()+"</td>" +
							"	  </tr>";
		}
		
		paginaHTML +=
		"</table>" +
		"</body>" +
		"</html>";
		
		return paginaHTML;
	}

	public int hashCode()
	{
		return (int)getNumPedido();
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof VoucherPedido))
			return false;
			
		if ( ((VoucherPedido)obj).getNumPedido() != this.getNumPedido() )
			 return false;

		return true;
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof VoucherPedido) )
			throw new ClassCastException("Objeto nao e da classe VoucherPedido.");
	
		int compare=0;		
		if ( ((VoucherPedido)obj).getNumPedido() > getNumPedido() )
			compare = -1;
		else if ( ((VoucherPedido)obj).getNumPedido() < getNumPedido() )
				compare = 1;
		
		return compare;
	}
	
	public String toString()
	{
		return "Pedido numero:" + getNumPedido();
	}
}
