package com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity;

import com.brt.gpp.comum.Definicoes;

/**
  *
  * Esta classe contem os atributos de armazenagem de informacoes
  * sobre o item da ordem criada pela tecnomen do Pedido de criacao de Vouchers 
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

public class VoucherOrdemItem implements Comparable
{
	private long numItem;
	private long qtdeCartoes;
	private long valorFace;
	private long qtdePorBatch;
	private long qtdePorCaixa;
	private VoucherOrdem ordem;

	/**
	 * Metodo....:VoucherOrdemItem
	 * Descricao.:Construtor da classe
	 * @param ordem		- Ordem de referencia do item
	 * @param numItem	- Numero do item
	 */
	public VoucherOrdemItem(VoucherOrdem ordem, long numItem)
	{
		setOrdem(ordem);
		setNumItem(numItem);
	}

	/**
	 * Metodo....:getOrdem
	 * Descricao.:Retorna a ordem na qual este item esta associado
	 * @return VoucherOrdem - Ordem
	 */
	public VoucherOrdem getOrdem() 
	{
		return ordem;
	}

	/**
	 * Metodo....:getNumItem
	 * Descricao.:Retorna o numero do item dessa ordem
	 * @return long - Numero do item
	 */
	public long getNumItem()
	{
		return numItem;
	}

	/**
	 * Metodo....:getQtdeCartoes
	 * Descricao.:Retorna o numero de cartoes deste item da ordem
	 * @return long - Numero de cartoes do item
	 */
	public long getQtdeCartoes() 
	{
		return qtdeCartoes;
	}

	/**
	 * Metodo....:getQtdePorBatch
	 * Descricao.:Retorna o numero de cartoes que cada bach possui
	 * @return long - Numero de cartoes em um batch
	 */
	public long getQtdePorBatch() 
	{
		return qtdePorBatch;
	}

	/**
	 * Metodo....:getQtdePorCaixa
	 * Descricao.:Retorna o numero de batchs existentes em uma caixa
	 * @return long - Numero de batchs em uma caixa
	 */
	public long getQtdePorCaixa() 
	{
		return qtdePorCaixa;
	}

	/**
	 * Metodo....:getValorFace
	 * Descricao.:Retorna o valor de face dos cartoes
	 * @return long - Valor de face dos cartoes
	 */
	public long getValorFace() 
	{
		return valorFace;
	}

	/**
	 * Metodo....:getNumeroCaixas
	 * Descricao.:Retorna o numero de caixas do item
	 * @return long - Numero de caixas deste item
	 */
	public long getNumeroCaixas()
	{
		long numCaixas = 0;
		if (getQtdePorCaixa() < Definicoes.NUMERO_BATCHES_POR_CAIXA)
			numCaixas = 1;
		else 
		{
			numCaixas =  qtdeCartoes / getQtdePorCaixa();
			if (qtdeCartoes % getQtdePorCaixa() > 0)
				numCaixas++;
		}
		return numCaixas;
	}

	/**
	 * Metodo....:setQtdeCartoes
	 * Descricao.:Atribui a quantidade de cartoes para o item
	 * @param qtdCartoes - Numero de cartoes
	 */
	public void setQtdeCartoes(long qtdCartoes)
	{
		this.qtdeCartoes = qtdCartoes;
	}

	/**
	 * Metodo....:setQtdePorBatch
	 * Descricao.:Atribui o quantidade de cartoes por batch
	 * @param qtdCartoesBatch - Quantidade de cartoes por batch
	 */
	public void setQtdePorBatch(long qtdCartoesBatch) 
	{
		this.qtdePorBatch = qtdCartoesBatch;
	}

	/**
	 * Metodo....:setQtdePorCaixa
	 * Descricao.:Atribui a quantidade de batchs por caixa
	 * @param qtdBatchCaixa - Quantidade de batchs por caixa
	 */
	public void setQtdePorCaixa(long qtdBatchCaixa)
	{
		this.qtdePorCaixa = qtdBatchCaixa;
	}

	/**
	 * Metodo....:setValorFace
	 * Descricao.:Atribui um valor de face para os cartoes
	 * @param vlrFace - Valor de face dos cartoes
	 */
	public void setValorFace(long vlrFace)
	{
		this.valorFace = vlrFace;
	}

	/**
	 * Metodo....:setNumItem
	 * Descricao.:Atribui o numero do item
	 * @param numItem - Numero do Item
	 */
	private void setNumItem(long numItem)
	{
		this.numItem = numItem;
	}
	
	/**
	 * Metodo....:setOrdem
	 * Descricao.:Atribui a ordem a este item
	 * @param VoucherOrdem - Ordem do item
	 */
	private void setOrdem(VoucherOrdem ordem)
	{
		this.ordem = ordem;
	}
	
	public int hashCode()
	{
		/* Concatena os dois valores .. NAO E SOMA */
		String hash = String.valueOf(getOrdem().getNumeroOrdem()) + String.valueOf(getNumItem());
		return Integer.parseInt(hash);
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof VoucherOrdemItem))
			return false;
		
		long numOrdem 		= this.getOrdem().getNumeroOrdem();
		long numOrdemObj	= ((VoucherOrdemItem)obj).getOrdem().getNumeroOrdem(); 
		if ( (numOrdem != numOrdemObj) || 
		     (numOrdem == numOrdemObj && ((VoucherOrdemItem)obj).getNumItem() != this.getNumItem())
		   )
			 return false;

		return true;
	}
	
	public String toString()
	{
		return "Ordem numero:" + getOrdem().getNumeroOrdem() + " Item Numero:" + getNumItem();
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof VoucherOrdemItem) )
			throw new ClassCastException("Objeto nao e da classe VoucherOrdemItem.");
		
		int compare=0;
		if ( ((VoucherOrdemItem)obj).getOrdem().getNumeroOrdem() == getOrdem().getNumeroOrdem() )
			if ( ((VoucherOrdemItem)obj).getNumItem() > getNumItem() )
				compare = -1;
			else if ( ((VoucherOrdemItem)obj).getNumItem() < getNumItem() )
				compare = 1;
		else
			return ((VoucherOrdemItem)obj).getOrdem().compareTo(this);
			
		return compare;
	}
}
