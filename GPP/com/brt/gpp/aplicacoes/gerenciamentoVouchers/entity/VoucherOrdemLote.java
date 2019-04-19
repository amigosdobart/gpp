package com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity;

public class VoucherOrdemLote
{
	private String numeroLote;
	private long valorFace;
	private int  status;
	private VoucherOrdemCaixa caixa;
	

	public VoucherOrdemLote(VoucherOrdemCaixa caixa, String numeroLote)
	{
		this.numeroLote = numeroLote;
		this.caixa      = caixa;
	}
	
	public VoucherOrdemCaixa getCaixa()
	{
		return caixa;
	}
	
	public void setCaixa(VoucherOrdemCaixa caixa)
	{
		this.caixa = caixa;
	}
	
	public String getNumeroLote()
	{
		return numeroLote;
	}
	
	public void setNumeroLote(String numeroLote)
	{
		this.numeroLote = numeroLote;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public long getValorFace()
	{
		return valorFace;
	}
	
	public void setValorFace(long valorFace)
	{
		this.valorFace = valorFace;
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof VoucherOrdemLote))
			return false;

		long numOrdem 		= this.getCaixa().getOrdem().getNumeroOrdem();
		long numOrdemObj	= ((VoucherOrdemLote)obj).getCaixa().getOrdem().getNumeroOrdem();
		long numCaixa       = this.getCaixa().getNumeroCaixa();
		long numCaixaObj    = ((VoucherOrdemLote)obj).getCaixa().getNumeroCaixa();
		String numLote      = this.getNumeroLote();
		String numLoteObj   = ((VoucherOrdemLote)obj).numeroLote;
		if ( (numOrdem != numOrdemObj) || 
			 (numOrdem == numOrdemObj && numCaixa == numCaixaObj) ||
			 (numOrdem == numOrdemObj && numCaixa == numCaixaObj && (!numLote.equals(numLoteObj)))
		   )
			 return false;
			
		return true;
	}
	
	public String toString()
	{
		return "Ordem numero:" + this.getCaixa().getOrdem().getNumeroOrdem() +
		       " Caixa Numero:" + this.getCaixa().getNumeroCaixa() + 
		       " Lote Numero:" + this.getNumeroLote();
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof VoucherOrdemLote) )
			throw new ClassCastException("Objeto nao e da classe VoucherOrdemLote.");
		
		int compare=0;
		long numLoteObj = Long.parseLong(((VoucherOrdemLote)obj).getNumeroLote());
		long numLote    = Long.parseLong(getNumeroLote());
		if ( ((VoucherOrdemLote)obj).getCaixa().getOrdem().getNumeroOrdem() == getCaixa().getOrdem().getNumeroOrdem() )
			if ( ((VoucherOrdemLote)obj).getCaixa().getNumeroCaixa() > getCaixa().getNumeroCaixa() )
				compare = -1;
			else if ( numLoteObj < numLote )
				compare = 1;
		else
			return ((VoucherOrdemLote)obj).getCaixa().compareTo(this.getCaixa());
			
		return compare;
	}
}
