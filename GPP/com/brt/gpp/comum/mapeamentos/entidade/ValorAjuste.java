package com.brt.gpp.comum.mapeamentos.entidade;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorCredito;

/**
 *	Entidade que representa os valores de ajuste em saldos do assinante na plataforma.
 *
 *	@version	1.0		30/04/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ValorAjuste extends ValorCredito 
{

	/**
	 *	Tipo de ajuste. Indica se o ajuste e de credito ou de debito. Os valores sao definidos na classe Definicoes.
	 */
	private String tipoAjuste;
	
	/**
	 *	Data de expiracao do saldo.
	 */
	private Date dataExpiracao;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoSaldo				Informacoes do tipo de saldo.
	 *	@param		vlrCredito				Valor de credito da recarga.
	 *	@param		tipoAjuste				Tipo de ajuste.
	 *	@param		dataExpiracao			Data de expiracao do saldo.
	 */
	public ValorAjuste(TipoSaldo tipoSaldo, double vlrCredito, String tipoAjuste, Date dataExpiracao)
	{
		super(tipoSaldo, vlrCredito);
		
		this.tipoAjuste		= tipoAjuste;
		this.dataExpiracao	= dataExpiracao;
	}
	
	/**
	 *	Retorna o tipo de ajuste.
	 *
	 *	@return		Tipo de ajuste.
	 */
	public String getTipoAjuste()
	{
		return this.tipoAjuste;
	}

	/**
	 *	Retorna o valor de credito. Executa o override do metodo da superclasse para ajustar o valor de acordo com o 
	 *	tipo de ajuste (credito ou debito).
	 *
	 *	@return		Valor de credito.
	 */
	public double getVlrCredito()
	{
		double result = Math.abs(super.getVlrCredito());
		
		return ((this.tipoAjuste != null) && (this.tipoAjuste.equalsIgnoreCase(Definicoes.TIPO_AJUSTE_DEBITO))) ? -result : result;
	}
	
	/**
	 *	Retorna a data de expiracao do saldo.
	 *
	 *	@return		Data de expiracao do saldo.
	 */
	public Date getDataExpiracao()
	{
		return this.dataExpiracao;
	}
	
	/**
	 *	@see		java.lang.Object#toString()	
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer(super.toString());
		
		if(this.dataExpiracao != null)
		{
			SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
			result.append(" - Data de Expiracao: " + conversorDate.format(this.dataExpiracao));
		}
		
		return result.toString();
	}
	
}
