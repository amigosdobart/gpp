package com.brt.gpp.comum.mapeamentos.entidade;

public class ValorVoucher
{
	private TipoSaldo 		tipoSaldo;
	private CategoriaTEC 	categoriaTEC;
	private int 			tipoVoucher;
	private int 			valorFace;
	private double			valorAtual;
	private int 			diasExpiracao;
	
	/**
	 * Metodo....:getdiasExpiracao
	 * Descricao.:Retorna o valor de diasExpiracao
	 * @return diasExpiracao.
	 */
	public int getDiasExpiracao()
	{
		return diasExpiracao;
	}
	/**
	 * Metodo....:setdiasExpiracao
	 * Descricao.:Define o valor de diasExpiracao
	 * @param diasExpiracao o valor a ser definido para diasExpiracao
	 */
	public void setDiasExpiracao(int diasExpiracao)
	{
		this.diasExpiracao = diasExpiracao;
	}
	/**
	 * Metodo....:gettipoSaldo
	 * Descricao.:Retorna o valor de tipoSaldo
	 * @return tipoSaldo.
	 */
	public TipoSaldo getTipoSaldo()
	{
		return tipoSaldo;
	}
	
	/**
	 * Metodo....:settipoSaldo
	 * Descricao.:Define o valor de tipoSaldo
	 * @param tipoSaldo o valor a ser definido para tipoSaldo
	 */
	public void setTipoSaldo(TipoSaldo tipoSaldo)
	{
		this.tipoSaldo = tipoSaldo;
	}
	
	/**
	 * Metodo....:gettipoVoucher
	 * Descricao.:Retorna o valor de tipoVoucher
	 * @return tipoVoucher.
	 */
	public int getTipoVoucher()
	{
		return tipoVoucher;
	}
	
	/**
	 * Metodo....:settipoVoucher
	 * Descricao.:Define o valor de tipoVoucher
	 * @param tipoVoucher o valor a ser definido para tipoVoucher
	 */
	public void setTipoVoucher(int tipoVoucher)
	{
		this.tipoVoucher = tipoVoucher;
	}
	
	/**
	 * Metodo....:getvalorAtual
	 * Descricao.:Retorna o valor de valorAtual
	 * @return valorAtual.
	 */
	public double getValorAtual()
	{
		return valorAtual;
	}
	
	/**
	 * Metodo....:setvalorAtual
	 * Descricao.:Define o valor de valorAtual
	 * @param valorAtual o valor a ser definido para valorAtual
	 */
	public void setValorAtual(double valorAtual)
	{
		this.valorAtual = valorAtual;
	}
	
	/**
	 * Metodo....:getvalorFace
	 * Descricao.:Retorna o valor de valorFace
	 * @return valorFace.
	 */
	public int getValorFace()
	{
		return valorFace;
	}
	
	/**
	 * Metodo....:setvalorFace
	 * Descricao.:Define o valor de valorFace
	 * @param valorFace o valor a ser definido para valorFace
	 */
	public void setValorFace(int valorFace)
	{
		this.valorFace = valorFace;
	}
	
	/**
	 * @return CategoriaTEC do serviço
	 */
	public CategoriaTEC getCategoriaTEC() 
	{
		return categoriaTEC;
	}
	/**
	 * @param categoriaTEC CategoriaTEC do serviço
	 */
	public void setCategoriaTEC(CategoriaTEC categoriaTEC) 
	{
		this.categoriaTEC = categoriaTEC;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ValorVoucher]TIPO_VOUCHER=" + getTipoVoucher());
		buffer.append(";TIPO_SALDO=" + ((getTipoSaldo() == null) ? "" :  "" + getTipoSaldo().getIdtTipoSaldo()));
		buffer.append(";CATEGORIA_TEC=" + ((getCategoriaTEC() == null) ? "" :  "" + getCategoriaTEC().getIdtCategoria()));
		buffer.append(";VALOR_FACE=" + getValorFace());
		
		return buffer.toString();
	}
	
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof ValorVoucher))
			return false;
		
		if (obj == this)
			return true;
				
		boolean equal = true;	
		equal &= isEqual(this.categoriaTEC, ((ValorVoucher)obj).getCategoriaTEC());
		equal &= isEqual(this.tipoSaldo, 	((ValorVoucher)obj).getTipoSaldo());
		equal &= (this.tipoVoucher == ((ValorVoucher)obj).getTipoVoucher());
		equal &= (this.valorFace == ((ValorVoucher)obj).getValorFace());
		return equal;
	}
	
	private boolean isEqual(Object obj1, Object obj2)
	{
		if (obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		if (obj1 == null && obj2 == null)
			return true;
		return false;
	}
	
}
