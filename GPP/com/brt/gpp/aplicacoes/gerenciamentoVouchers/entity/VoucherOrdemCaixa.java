package com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity;

import java.util.Collection;
import java.util.TreeSet;

/**
  *
  * Esta classe contem os atributos de armazenagem de informacoes
  * sobre a caixa da ordem criada pela tecnomen do Pedido de criacao de Vouchers 
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

public class VoucherOrdemCaixa implements Comparable
{
	private long 			numeroCaixa;
	private long 			qtdePorBatch;
	private long 			qtdePorCaixa;
	private VoucherOrdem	ordem;
	private int				status;
	
	private Collection      lotesCaixa;

	public VoucherOrdemCaixa(VoucherOrdem ordem,long numeroCaixa)
	{
		setOrdem(ordem);
		setNumeroCaixa(numeroCaixa);
		
		this.lotesCaixa = new TreeSet();
	}
	
	/**
	 * Metodo....:getStatus
	 * Descricao.:Retorna o status desta caixa
	 * @return int - status da caixa
	 */
	public int getStatus()
	{
		return status;
	}
	
	/**
	 * Metodo....:setStatus
	 * Descricao.:Define o status da caixa
	 * @param status - status da caixa
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	/**
	 * Metodo....:getNumeroCaixa
	 * Descricao.:Retorna o numero da caixa
	 * @return long - Numero da caixa
	 */
	public long getNumeroCaixa() 
	{
		return numeroCaixa;
	}

	/**
	 * Metodo....:getOrdem
	 * Descricao.:Retorna a ordem associada a caixa
	 * @return VoucherOrdem - Ordem que esta caixa esta associada
	 */
	public VoucherOrdem getOrdem() 
	{
		return ordem;
	}

	/**
	 * Metodo....:getQtdePorBatch
	 * Descricao.:Retorna a quantidade de vouchers por batch da caixa
	 * @return long - Quantidade de vouchers por batch da caixa
	 */
	public long getQtdePorBatch() 
	{
		return qtdePorBatch;
	}

	/**
	 * Metodo....:getQtdePorCaixa
	 * Descricao.:Retorna a quantidade de batchs desta caixa
	 * @return long - Quantidade de batchs da caixa
	 */
	public long getQtdePorCaixa() 
	{
		return qtdePorCaixa;
	}

	/**
	 * Metodo....:setNumeroCaixa
	 * Descricao.:Atribui um numero de caixa
	 * @param numCaixa - Numero da caixa
	 */
	private void setNumeroCaixa(long numCaixa) 
	{
		this.numeroCaixa = numCaixa;
	}

	/**
	 * Metodo....:setOrdem
	 * Descricao.:Atribui a ordem a esta caixa
	 * @param ordem - Ordem a ser associada
	 */
	private void setOrdem(VoucherOrdem ordem) 
	{
		this.ordem = ordem;
	}

	/**
	 * Metodo....:setQtdePorBatch
	 * Descricao.:Atribui o valor de quantidade de cartoes existem no batch
	 * @param qtdeCartoes - Quantidade de cartoes existentes no batch
	 */
	public void setQtdePorBatch(long qtdeCartoes) 
	{
		this.qtdePorBatch = qtdeCartoes;
	}

	/**
	 * Metodo....:setQtdePorCaixa
	 * Descricao.:Atribui um valor de quantidade de batchs por caixa
	 * @param qtdeBatchs - Quantidade de batchs da caixa
	 */
	public void setQtdePorCaixa(long qtdeBatchs) 
	{
		this.qtdePorCaixa = qtdeBatchs;
	}

	/**
	 * Metodo....:addLoteCaixa
	 * Descricao.:Adiciona um objeto VoucherOrdemLote associada a esta caixa
	 * @param loteCaixa 	- Lote a ser associada a esta caixa
	 * @return boolean		- Indica se conseguiu ou nao adicionar o lote
	 */	
	public boolean addLoteCaixa(VoucherOrdemLote loteCaixa)
	{
		boolean adicionou=false;
		try
		{
			adicionou=lotesCaixa.add(loteCaixa);
		}
		catch(Exception e)
		{
			adicionou=false;
		}
		return adicionou;
	}

	/**
	 * Metodo....:removeLoteCaixa
	 * Descricao.:Remove um objeto VoucherOrdemLote associada a esta caixa
	 * @param loteCaixa 	- Lote a ser removida desta caixa
	 * @return boolean		- Indica se conseguiu ou nao remover o lote
	 */		
	public boolean removeLoteCaixa(VoucherOrdemLote loteCaixa)
	{
		return lotesCaixa.remove(loteCaixa);
	}
	
	/**
	 * Metodo....:getLotesCaixa
	 * Descricao.:Retorna os lotes associados a esta caixa
	 * @return Collection - Lista de lotes
	 */
	public Collection getLotesCaixa()
	{
		return lotesCaixa;
	}	
	
	public int hashCode()
	{
		/* Concatena os dois valores .. NAO E SOMA */
		String hash = String.valueOf(getOrdem().getNumeroOrdem()) + String.valueOf(getNumeroCaixa());
		return Integer.parseInt(hash);
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof VoucherOrdemCaixa))
			return false;

		long numOrdem 		= this.getOrdem().getNumeroOrdem();
		long numOrdemObj	= ((VoucherOrdemCaixa)obj).getOrdem().getNumeroOrdem(); 
		if ( (numOrdem != numOrdemObj) || 
			 (numOrdem == numOrdemObj && ((VoucherOrdemCaixa)obj).getNumeroCaixa() != this.getNumeroCaixa())
		   )
			 return false;
			
		return true;
	}
	
	public String toString()
	{
		return "Ordem numero:" + getOrdem().getNumeroOrdem() + " Caixa Numero:" + getNumeroCaixa();
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof VoucherOrdemCaixa) )
			throw new ClassCastException("Objeto nao e da classe VoucherOrdemCaixa.");
		
		int compare=0;
		if ( ((VoucherOrdemCaixa)obj).getOrdem().getNumeroOrdem() == getOrdem().getNumeroOrdem() )
			if ( ((VoucherOrdemCaixa)obj).getNumeroCaixa() > getNumeroCaixa() )
				compare = -1;
			else if ( ((VoucherOrdemCaixa)obj).getNumeroCaixa() < getNumeroCaixa() )
				compare = 1;
		else
			return ((VoucherOrdemCaixa)obj).getOrdem().compareTo(this);
			
		return compare;
	}
}
