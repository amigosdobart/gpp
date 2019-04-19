package com.brt.gpp.comum.mapeamentos.entidade;

import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorAjuste;
import com.brt.gpp.comum.mapeamentos.entidade.ValorCredito;

/**
 *	Entidade da tabela TBL_REC_VALOR.
 *
 *	@version	1.0		30/04/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ValorRecarga extends ValorCredito
{

	/**
	 *	Identificador do valor.
	 */
	private double idValor;
	
	/**
	 *	Informacoes da categoria de plano de preco.
	 */
	private Categoria categoria;
	
	/**
	 *	Data de inicio de vigencia.
	 */
	private Date datIniVigencia;
	
	/**
	 *	Data de fim de vigencia.
	 */
	private Date datFimVigencia;
	
	/**
	 *	Indicador de valor de face.
	 */
	private boolean indValorFace;
	
	/**
	 *	Valor efetivo pago.
	 */
	private double vlrEfetivoPago;
	
	/**
	 *	Valor de bonus da recarga.
	 */
	private double vlrBonus;
	
	/**
	 *	Numero de dias de expiracao.	
	 */
	private short numDiasExpiracao;
	
    public ValorRecarga()
    {
    }
    
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idValor					Identificador do valor.
	 *	@param		categoria				Informacoes da categoria de plano de preco.
	 *	@param		tipoSaldo				Informacoes do tipo de saldo.
	 *	@param		datIniVigencia			Data de inicio de vigencia.
	 *	@param		datFimVigencia			Data de fim de vigencia.
	 *	@param		indValorFace			Indicador de valor de face.
	 *	@param		vlrEfetivoPago			Valor efetivo pago.
	 *	@param		vlrCredito				Valor de credito da recarga.
	 *	@param		vlrBonus				Valor de bonus da recarga.
	 *	@param		numDiasExpiracao		Numero de dias de expiracao.
	 */
	public ValorRecarga(double idValor, 
						Categoria categoria, 
						TipoSaldo tipoSaldo, 
						Date datIniVigencia, 
						Date datFimVigencia,
						boolean indValorFace,
						double vlrEfetivoPago,
						double vlrCredito,
						double vlrBonus,
						short numDiasExpiracao)
	{
		super(tipoSaldo, vlrCredito);
		
		this.idValor			= idValor;
		this.categoria			= categoria;
		this.datIniVigencia		= datIniVigencia;
		this.datFimVigencia		= datFimVigencia;
		this.indValorFace		= indValorFace;
		this.vlrEfetivoPago		= vlrEfetivoPago;
		this.vlrBonus			= vlrBonus;
		this.numDiasExpiracao	= numDiasExpiracao;
	}
	
	/**
	 *	Retorna o identificador do valor.
	 *
	 *	@return		Identificador do valor.
	 */
	public double getIdValor()
	{
		return this.idValor;
	}
	
	/**
	 *	Retorna as informacoes da categoria de plano de preco.
	 *
	 *	@return		Informacoes da categoria de plano de preco.
	 */
	public Categoria getCategoria()
	{
		return this.categoria;
	}
	
	/**
	 *	Retorna a data de inicio de vigencia.
	 *
	 *	@return		Data de inicio de vigencia.
	 */
	public Date getDatIniVigencia()
	{
		return this.datIniVigencia;
	}
	
	/**
	 *	Retorna a data de fim de vigencia.
	 *
	 *	@return		Data de fim de vigencia.
	 */
	public Date getDatFimVigencia()
	{
		return this.datFimVigencia;
	}
	
	/**
	 *	Indica se corresponde a um valor de face.
	 *
	 *	@return		True se corresponder a um valor de face e false caso contrario.
	 */
	public boolean isValorFace()
	{
		return this.indValorFace;
	}
	
	/**
	 *	Retorna o valor efetivo pago.
	 *
	 *	@return		Valor efetivo pago.
	 */
	public double getVlrEfetivoPago()
	{
		return this.vlrEfetivoPago;
	}
	
	/**
	 *	Retorna o valor de bonus da recarga.
	 *
	 *	@return		Valor de bonus da recarga.
	 */
	public double getVlrBonus()
	{
		return this.vlrBonus;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao.
	 *
	 *	@return		Numero de dias de expiracao.	
	 */
	public short getNumDiasExpiracao()
	{
		return this.numDiasExpiracao;
	}
    
	public boolean isIndValorFace()
    {
        return indValorFace;
    }

    public void setIndValorFace(boolean indValorFace)
    {
        this.indValorFace = indValorFace;
    }

    public void setCategoria(Categoria categoria)
    {
        this.categoria = categoria;
    }

    public void setDatFimVigencia(Date datFimVigencia)
    {
        this.datFimVigencia = datFimVigencia;
    }

    public void setDatIniVigencia(Date datIniVigencia)
    {
        this.datIniVigencia = datIniVigencia;
    }

    public void setIdValor(double idValor)
    {
        this.idValor = idValor;
    }

    public void setNumDiasExpiracao(short numDiasExpiracao)
    {
        this.numDiasExpiracao = numDiasExpiracao;
    }

    public void setVlrBonus(double vlrBonus)
    {
        this.vlrBonus = vlrBonus;
    }

    public void setVlrEfetivoPago(double vlrEfetivoPago)
    {
        this.vlrEfetivoPago = vlrEfetivoPago;
    }

    /**
	 *	Indica se o valor de recarga esta vigente em relacao a data de processamento informada.
	 *
	 *	@param		dataExecucao			Data de processamento da operacao.
	 *	@return		True se esta vigente e false caso contrario.	
	 */
	public boolean isVigente(Date dataExecucao)
	{
		if((this.datIniVigencia == null) || (dataExecucao == null))
			return false;
		
		if(this.datIniVigencia.compareTo(dataExecucao) > 0)
			return false;
		
		if(this.datFimVigencia == null)
			return true;
		
		return (this.datFimVigencia.compareTo(dataExecucao) >= 0);
	}
	
	/**
	 *	Extrai objeto de valor de ajuste para concessao do bonus da recarga. Se a recarga nao possuir bonus para o 
	 *	saldo, retorna NULL.
	 *
	 *	@return		Objeto ValorAjuste para concessao de bonus da recarga.	
	 */
	public ValorAjuste extrairBonus()
	{
		if(this.vlrBonus > 0.0)
			return new ValorAjuste(super.getTipoSaldo(), this.vlrBonus, Definicoes.TIPO_AJUSTE_CREDITO, null);
		
		return null;
	}
	
	/**
	 *	@see		java.lang.Object#equals(java.lang.Object)	
	 */
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return (this.hashCode() == obj.hashCode());
	}
	
	/**
	 *	@see		java.lang.Object#hashCode()	
	 */
	public int hashCode()
	{
		StringBuffer buffer = new StringBuffer(this.getClass().getName());
		buffer.append("||");
		buffer.append(String.valueOf(this.getIdValor()));
		buffer.append("||");
		buffer.append(this.getCategoria());
		buffer.append("||");
		buffer.append(super.getTipoSaldo());
		buffer.append("||");
		buffer.append(this.getDatIniVigencia());
		
		return buffer.toString().hashCode();
	}
	
	/**
	 *	@see		java.lang.Object#toString()	
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer(super.toString());
		
		result.append(" - Bonus: " + this.vlrBonus);
		result.append(" - Dias de Expiracao: " + this.numDiasExpiracao);
		
		return result.toString();
	}
	
}
