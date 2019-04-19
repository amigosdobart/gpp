package com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.text.DecimalFormat;

/**
  *
  * Esta classe contem os atributos de armazenagem de informacoes
  * sobre a ordem criada pela tecnomen do Pedido de criacao de Vouchers 
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

public class VoucherOrdem implements Comparable
{
	private long 		numeroOrdem;
	private Collection	itensOrdem;
	private Collection  caixasOrdem;
	private int			status;

	private static final long 	FATOR_NUMERO_INICIAL	=1000;
	private static final char	FLAG_NUMERACAO_INICIAL	='I';
	private static final char	FLAG_NUMERACAO_FINAL	='F';
	
	/**
	 * Metodo....:VoucherOrdem
	 * Descricao.:Construtor da classe
	 * @param numeroOrdem - Numero da ordem a ser criada
	 */
	public VoucherOrdem(long numeroOrdem)
	{
		this.numeroOrdem = numeroOrdem;
		this.itensOrdem  = new TreeSet();
		this.caixasOrdem = new TreeSet();
	}

	/**
	 * Metodo....:getNumeroOrdem
	 * Descricao.:Retorna o numero da ordem
	 * @return long	- Numero da ordem
	 */
	public long getNumeroOrdem() 
	{
		return numeroOrdem;
	}

	/**
	 * Metodo....:addItemOrdem
	 * Descricao.:Adiciona um objeto VoucherOrdemItem associada a esta ordem
	 * @param itemOrdem - Item de ordem a ser associada a esta ordem
	 * @return boolean	- Indica se conseguiu ou nao adicionar o item
	 */	
	public boolean addItemOrdem(VoucherOrdemItem itemOrdem)
	{
		boolean adicionou=false;
		try
		{
			adicionou=itensOrdem.add(itemOrdem);
		}
		catch(Exception e)
		{
			adicionou=false;
		}
		return adicionou;
	}

	/**
	 * Metodo....:getStatus
	 * Descricao.:Retorna o status da ordem
	 * @return int - Status da ordem
	 */
	public int getStatus()
	{
		return status;
	}
	
	/**
	 * Metodo....:setStatus
	 * Descricao.:Define o status da ordem
	 * @param status - Status da ordem
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	/**
	 * Metodo....:removeItemOrdem
	 * Descricao.:Remove um objeto VoucherOrdemItem associada a esta ordem
	 * @param itemOrdem - Item de ordem a ser removida desta ordem
	 * @return boolean	- Indica se conseguiu ou nao remover o item
	 */		
	public boolean removeItemOrdem(VoucherOrdemItem itemOrdem)
	{
		return itensOrdem.remove(itemOrdem);
	}

	/**
	 * Metodo....:addCaixaOrdem
	 * Descricao.:Adiciona um objeto VoucherOrdemCaixa associada a esta ordem
	 * @param caixaOrdem 	- Caixa a ser associada a esta ordem
	 * @return boolean		- Indica se conseguiu ou nao adicionar a caixa
	 */	
	public boolean addCaixaOrdem(VoucherOrdemCaixa caixaOrdem)
	{
		boolean adicionou=false;
		try
		{
			adicionou=caixasOrdem.add(caixaOrdem);
		}
		catch(Exception e)
		{
			adicionou=false;
		}
		return adicionou;
	}

	/**
	 * Metodo....:removeCaixaOrdem
	 * Descricao.:Remove um objeto VoucherOrdemCaixa associada a esta ordem
	 * @param caixaOrdem 	- Caixa a ser removida desta ordem
	 * @return boolean		- Indica se conseguiu ou nao remover a caixa
	 */		
	public boolean removeCaixaOrdem(VoucherOrdemCaixa caixaOrdem)
	{
		return caixasOrdem.remove(caixaOrdem);
	}
	
	/**
	 * Metodo....:getNumeroItens
	 * Descricao.:Retorna o numero de itens da ordem
	 * @return int - Numero de itens da ordem
	 */
	public int getNumeroItens()
	{
		return itensOrdem.size();
	}
	
	/**
	 * Metodo....:getItensOrdem
	 * Descricao.:Retorna os itens da ordem
	 * @return Collection - Itens da ordem
	 */
	public Collection getItensOrdem()
	{
		return itensOrdem;
	}

	/**
	 * Metodo....:getItemOrdem
	 * Descricao.:Retorna o item da ordem dado o numero deste item
	 *            Obs: Os itens comecam a partir do indice 0 
	 * @param numItem 				- Numero do item desejado
	 * @return	VoucherOrdemItem	- Item da ordem
	 */
	public VoucherOrdemItem getItemOrdem(long numItem)
	{
		VoucherOrdemItem itens[] = (VoucherOrdemItem[])itensOrdem.toArray(new VoucherOrdemItem[0]);
		return itens[(int)numItem];
	}

	/**
	 * Metodo....:getCaixasOrdem
	 * Descricao.:Retorna as caixas dessa ordem
	 * @return Collection - Caixas da ordem
	 */
	public Collection getCaixasOrdem()
	{
		return caixasOrdem;
	}
	
	/**
	 * Metodo....:getNumeroCaixasPorItem
	 * Descricao.:Retorna o numero de caixas necessarias para o item
	 * @param itemOrdem	- Item da ordem
	 * @return 			- Numero de caixas para o item
	 */
	public long getNumeroCaixasPorItem(VoucherOrdemItem itemOrdem)
	{
		return itemOrdem.getQtdeCartoes()/itemOrdem.getQtdePorCaixa();
	}

	/**
	 * Metodo....:getNumeroLote
	 * Descricao.:Retorna o numero do lote baseado no item
	 * @param flag		- Indica se o numero e inicial ou final
	 * @param itemOrdem	- Item da ordem a ser verificado a numeracao de lote
	 * @return long		- Numero do lote (inicial ou final)
	 */
	private long getNumeroLote(char flag, VoucherOrdemItem itemOrdem)
	{
		long posPrimeiraCaixa=0;
		long posUltimaCaixa=0;
		DecimalFormat numFormat = new DecimalFormat("000");
		
		/* Cria um array contendo todas as caixas em suas devidas posicoes */
		VoucherOrdemCaixa caixas[] = (VoucherOrdemCaixa[])getCaixasOrdem().toArray(new VoucherOrdemCaixa[0]);

		/* Faz a varredura dos itens para identificar se o item e o desejado
		 * e tambem para calcular a posicao inicial e final das caixas corretas
		 * para tal item
		 */
		for (Iterator i=getItensOrdem().iterator(); i.hasNext();)
		{
			VoucherOrdemItem item = (VoucherOrdemItem)i.next();
			posUltimaCaixa = posPrimeiraCaixa+getNumeroCaixasPorItem(item)-1;
			if (!item.equals(itemOrdem))
				posPrimeiraCaixa=posUltimaCaixa+1;
			else break;
		}
		/* O numero inicial e multiplicado pelo fator 1000 */
		long numeroInicial = caixas[(int)posPrimeiraCaixa].getNumeroCaixa()*VoucherOrdem.FATOR_NUMERO_INICIAL;
		/* O numero final tem que ser retirado um da soma porque o inicio esta incluso */
		long numeroFinal   = Long.parseLong( String.valueOf(caixas[(int)posUltimaCaixa].getNumeroCaixa()) + 
		                                     numFormat.format((itemOrdem.getQtdePorCaixa())-1) );

		if (flag == VoucherOrdem.FLAG_NUMERACAO_INICIAL)
			return numeroInicial;
		
		/*Else implicito*/
		return numeroFinal;
	}
	
	/**
	 * Metodo....:getNumeroInicialLote
	 * Descricao.:Retorna o numero inicial do lote do item
	 * @param 	itemOrdem	- Item da ordem
	 * @return	long		- Numero inicial do lote
	 */	
	public long getNumeroInicialLote(VoucherOrdemItem itemOrdem)
	{
		return getNumeroLote(VoucherOrdem.FLAG_NUMERACAO_INICIAL,itemOrdem);
	}

	/**
	 * Metodo....:getNumeroFinalLote
	 * Descricao.:Retorna o numero final do lote do item
	 * @param 	itemOrdem	- Item da ordem
	 * @return	long		- Numero final do lote
	 */	
	public long getNumeroFinalLote(VoucherOrdemItem itemOrdem)
	{
		return getNumeroLote(VoucherOrdem.FLAG_NUMERACAO_FINAL,itemOrdem);
	}
	
	public int hashCode()
	{
		return (int)getNumeroOrdem();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof VoucherOrdem) )
			return false;
			
		if ( !( ((VoucherOrdem)obj).getNumeroOrdem() == getNumeroOrdem()) )
			return false;
			
		return true;
	}
	
	public String toString()
	{
		return "Ordem numero:" + getNumeroOrdem();
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof VoucherOrdem) )
			throw new ClassCastException("Objeto nao e da classe VoucherOrdem.");
	
		int compare=0;		
		if ( ((VoucherOrdem)obj).getNumeroOrdem() > getNumeroOrdem() )
			compare = -1;
		else if ( ((VoucherOrdem)obj).getNumeroOrdem() < getNumeroOrdem() )
				compare = 1;
		
		return compare;
	}

}