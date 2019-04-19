package com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity;

import java.util.Date;

/**
  *
  * Esta classe contem a armazenagem de informacoes sobre os itens de
  * pedido de criacao de voucher
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

public class VoucherPedidoItem implements Comparable
{
	private long			numItem;
	private long			qtdeCartoes;
	private String			estampa;
	private long			numCaixaLoteInicial;
	private long			numCaixaLoteFinal;
	private long			qtdePorBatch;
	private long			qtdeBatchPorCaixa;

	private double			valorFace;
	private double			valorFaceBonus;
	private double			valorFaceSm;
	private double			valorFaceDados;
	
	private Date			expiracao;
	private Date			expiracaoBonus;
	private Date			expiracaoSm;
	private Date			expiracaoDados;
	
	private VoucherPedidoItem	subItem;
	private VoucherPedido		pedido;

	public VoucherPedidoItem(VoucherPedido pedido,int numItem)
	{
		setPedido(pedido);
		setNumItem(numItem);
	}
	
	/**
	 * Metodo....:getEstampa
	 * Descricao.:Retorna a identificao da estampa
	 * @return String - Identificacao da estampa
	 */
	public String getEstampa() 
	{
		return estampa;
	}

	/**
	 * Metodo....:getNumCaixaLoteFinal
	 * Descricao.:Retorna o numero da caixa do lote final
	 * @return long - Final do Numero da caixa do lote
	 */
	public long getNumCaixaLoteFinal() 
	{
		return numCaixaLoteFinal;
	}

	/**
	 * Metodo....:getNumCaixaLoteInicial
	 * Descricao.:Retorna o numero da caixa do lote inicial
	 * @return long - Inicio do Numero da caixa do lote
	 */
	public long getNumCaixaLoteInicial() 
	{
		return numCaixaLoteInicial;
	}

	/**
	 * Metodo....:getNumItem
	 * Descricao.:Retorna a identificacao do item
	 * @return long - Numero do Item (sequencial)
	 */
	public long getNumItem() 
	{
		return numItem;
	}

	/**
	 * Metodo....:getPedido
	 * Descricao.:Retorna o pedido
	 * @return VoucherPedido - Pedido
	 */
	public VoucherPedido getPedido() 
	{
		return pedido;
	}

	/**
	 * Metodo....:getQtdeCartoes
	 * Descricao.:Retorna o numero de cartoes a serem criados para este item
	 * @return long - Quantidade de cartoes a serem criados
	 */
	public long getQtdeCartoes()
	{
		return qtdeCartoes;
	}

	/**
	 * Metodo....:getValorFace
	 * Descricao.:Retorna o valor de face do cartao
	 * @return double - Valor Face
	 */
	public double getValorFace()
	{
		return valorFace;
	}

	/**
	 * Metodo....:getQtdePorBatch
	 * Descricao.:Retorna a quantidade de cartoes por batch
	 * @return long - Qtde por Batch
	 */
	public long getQtdePorBatch()
	{
		return qtdePorBatch;
	}

	/**
	 * Metodo....:getQtdeBatchPorCaixa
	 * Descricao.:Retorna a quantidade de batchs em uma caixa
	 * @return long - Qtde de batches
	 */
	public long getQtdeBatchPorCaixa()
	{
		return qtdeBatchPorCaixa;
	}

	/**
	 * Metodo....:getExpiracao
	 * Descricao.:Retorna o valor da expiracao para o pedido de cartoes
	 * @return Date - Data de expiracao
	 */
	public Date getExpiracao()
	{
		return expiracao;
	}

	/**
	 * Metodo....:getExpiracaoBonus
	 * Descricao.:Retorna o valor da expiracao para o saldo de bonus do pedido de cartoes
	 * @return Date - Data de expiracao de bonus
	 */
	public Date getExpiracaoBonus()
	{
		return expiracaoBonus;
	}

	/**
	 * Metodo....:getExpiracaoDados
	 * Descricao.:Retorna o valor da expiracao para o saldo de dados do pedido de cartoes
	 * @return Date - Data de expiracao de dados
	 */
	public Date getExpiracaoDados()
	{
		return expiracaoDados;
	}

	/**
	 * Metodo....:getExpiracaoSm
	 * Descricao.:Retorna o valor da expiracao para o sado de sm do pedido de cartoes
	 * @return Date - Data de expiracao de SMS
	 */
	public Date getExpiracaoSm()
	{
		return expiracaoSm;
	}

	/**
	 * Metodo....:getValorFaceBonus
	 * Descricao.:Retorna o valor de face do valor do saldo de bonus
	 * @return double - Valor de face de bonus
	 */
	public double getValorFaceBonus()
	{
		return valorFaceBonus;
	}

	/**
	 * Metodo....:getValorFaceDados
	 * Descricao.:Retorna o valor de face do valor do saldo de dados
	 * @return double - Valor de face de dados
	 */
	public double getValorFaceDados()
	{
		return valorFaceDados;
	}

	/**
	 * Metodo....:getValorFaceSm
	 * Descricao.:Retorna o valor de face do valor do saldo de sms
	 * @return double - Valor de face de sms
	 */
	public double getValorFaceSm()
	{
		return valorFaceSm;
	}

	/**
	 * Metodo....:getSubItem
	 * Descricao.:Retorna o subItem deste item
	 * OBS: Um item pode conter no maximo 1 (um) sub-item
	 */
	public VoucherPedidoItem getSubItem()
	{
		return subItem;
	}

	/**
	 * Metodo....:setEstampa
	 * Descricao.:Atribui um valor de identificacao de estampa
	 * @param idtEstampa - Identificador da estampa
	 */
	public void setEstampa(String idtEstampa) 
	{
		this.estampa = idtEstampa;
	}

	/**
	 * Metodo....:setNumCaixaLoteFinal
	 * Descricao.:Atribui um numero final de caixa de lote para o item
	 * @param numLoteFinal - Numero Final do lote
	 */
	public void setNumCaixaLoteFinal(long numLoteFinal) 
	{
		this.numCaixaLoteFinal = numLoteFinal;
	}

	/**
	 * Metodo....:setNumCaixaLoteInicial
	 * Descricao.:Atribui um numero inicial de caixa de lote para o item
	 * @param numLoteInicial - Numero inicial do lote
	 */
	public void setNumCaixaLoteInicial(long l) 
	{
		numCaixaLoteInicial = l;
	}

	/**
	 * Metodo....:setNumItem
	 * Descricao.:Atribui um numero de item
	 * @param numItem - Numero do item
	 */
	private void setNumItem(long numItem) 
	{
		this.numItem = numItem;
	}

	/**
	 * Metodo....:setPedido
	 * Descricao.:Atribui o pedido deste item
	 * @param pedido - Pedido
	 */
	private void setPedido(VoucherPedido pedido) 
	{
		this.pedido = pedido;
	}

	/**
	 * Metodo....:setQtdeCartoes
	 * Descricao.:Atribui uma quantidade de cartoes para o item
	 * @param qtdCartoes - Quantidade de cartoes
	 */
	public void setQtdeCartoes(long qtdCartoes) 
	{
		this.qtdeCartoes = qtdCartoes;
	}

	/**
	 * Metodo....:setValorFace
	 * Descricao.:Atribui valor de face aos cartoes
	 * @param vlrFace - Valor de face dos cartoes
	 */
	public void setValorFace(double vlrFace) 
	{
		this.valorFace = vlrFace;
	}

	/**
	 * Metodo....:setQtdePorBatch
	 * Descricao.:Atribui valor de quantidade de cartoes por batch
	 * @param qtdBatch - Quantidade de Cartoes por Batch
	 */
	public void setQtdePorBatch(long qtdBatch) 
	{
		this.qtdePorBatch = qtdBatch;
	}

	/**
	 * Metodo....:setQtdeBatchPorCaixa
	 * Descricao.:Atribui valor de quantidade de batchs em uma caixa
	 * @param qtdBatch - Quantidade de Batches
	 */
	public void setQtdeBatchPorCaixa(long qtdBatch) 
	{
		this.qtdeBatchPorCaixa = qtdBatch;
	}

	/**
	 * Metodo....:setExpiracao
	 * Descricao.:Atribui valor da data de expiracao do cartao
	 * @param expiracao - Data de expiracao
	 */
	public void setExpiracao(Date expiracao)
	{
		this.expiracao = expiracao;
	}

	/**
	 * Metodo....:setExpiracaoBonus
	 * Descricao.:Atribui valor da data de expiracao do saldo de bonus do cartao
	 * @param expiracaoBonus - Data de expiracao de bonus
	 */
	public void setExpiracaoBonus(Date expiracaoBonus)
	{
		this.expiracaoBonus = expiracaoBonus;
	}

	/**
	 * Metodo....:setExpiracaoDados
	 * Descricao.:Atribui valor da data de expiracao do saldo de dados do cartao
	 * @param expiracaoDados - Data de expiracao de dados
	 */
	public void setExpiracaoDados(Date expiracaoDados)
	{
		this.expiracaoDados = expiracaoDados;
	}

	/**
	 * Metodo....:setExpiracaoSm
	 * Descricao.:Atribui valor da data de expiracao do saldo de sms do cartao
	 * @param expiracaoSm - Data de expiracao de SMS
	 */
	public void setExpiracaoSm(Date expiracaoSm)
	{
		this.expiracaoSm = expiracaoSm;
	}
	
	/**
	 * Metodo....:setValorFaceBonus
	 * Descricao.:Atribui valor de face dos cartoes para o saldo de bonus
	 * @param valorFaceBonus - Valor de face do saldo de bonus
	 */
	public void setValorFaceBonus(double valorFaceBonus)
	{
		this.valorFaceBonus = valorFaceBonus;
	}

	/**
	 * Metodo....:setValorFaceDados
	 * Descricao.:Atribui valor de face dos cartoes para o saldo de dados
	 * @param valorFaceDados - Valor de face do saldo de dados
	 */
	public void setValorFaceDados(double valorFaceDados)
	{
		this.valorFaceDados = valorFaceDados;
	}

	/**
	 * Metodo....:setValorFaceSm
	 * Descricao.:Atribui valor de face dos cartoes para o saldo de Sms
	 * @param valorFaceSms - Valor de face do saldo de sms
	 */
	public void setValorFaceSm(double valorFaceSm)
	{
		this.valorFaceSm = valorFaceSm;
	}

	/**
	 * Metodo....:setSubItem
	 * Descricao.:Define o sub-item deste item
	 * @param subItem - Sub-item
	 */
	public void setSubItem(VoucherPedidoItem subItem)
	{
		this.subItem = subItem;
	}

	public int hashCode()
	{
		/* Concatena os dois valores .. NAO E SOMA */
		String hash = String.valueOf(getPedido().getNumPedido()) + String.valueOf(getNumItem());
		return Integer.parseInt(hash);
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof VoucherPedidoItem))
			return false;

		long numPedido 		= this.getPedido().getNumPedido();
		long numPedidoObj	= ((VoucherPedidoItem)obj).getPedido().getNumPedido(); 
		if ( (numPedido != numPedidoObj) || 
			 (numPedido == numPedidoObj && ((VoucherPedidoItem)obj).getNumItem() != this.getNumItem())
		   )
			 return false;

		return true;
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof VoucherPedidoItem) )
			throw new ClassCastException("Objeto nao e da classe VoucherPedidoItem.");
		
		int compare=0;
		if ( ((VoucherPedidoItem)obj).getPedido().getNumPedido() == getPedido().getNumPedido() )
			if ( ((VoucherPedidoItem)obj).getNumItem() > getNumItem() )
				compare = -1;
			else if ( ((VoucherPedidoItem)obj).getNumItem() < getNumItem() )
				compare = 1;
		else
			return ((VoucherPedidoItem)obj).getPedido().compareTo(this);
			
		return compare;
	}
	
	public String toString()
	{
		return "Pedido numero:" + getPedido().getNumPedido() + " Item Numero:" + getNumItem();
	}
}
