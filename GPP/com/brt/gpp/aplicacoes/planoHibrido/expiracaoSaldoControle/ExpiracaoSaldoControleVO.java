package com.brt.gpp.aplicacoes.planoHibrido.expiracaoSaldoControle;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;

public class ExpiracaoSaldoControleVO 
{
	private String msisdn;
	private String tipoSaldoExpirado;
	private String dataReferencia;
	private String dataRecarga;
	private double saldoInicial;
	private double saldoFinal;
	private	int	   indNovoControle;
	private String tipoTransacao;
	
	public ValoresRecarga getValoresRecarga(double valor)
	{
		if (valor <= 0.1)
			return null;
		
		ValoresRecarga valoresRecarga = null;
		if (tipoSaldoExpirado.equals(Definicoes.EXPIRA_SALDO_PRINCIPAL))
			valoresRecarga = new ValoresRecarga(valor, 0, 0, 0, 0);
		else if (tipoSaldoExpirado.equals(Definicoes.EXPIRA_SALDO_PERIODICO))
			valoresRecarga = new ValoresRecarga(0, valor, 0, 0, 0);
		else if (tipoSaldoExpirado.equals(Definicoes.EXPIRA_SALDO_BONUS))
			valoresRecarga = new ValoresRecarga(0, 0, valor, 0, 0);
		
		return valoresRecarga;
	}
	
	public String getTipoTransacaoAjuste()
	{
		if (tipoSaldoExpirado.equals(Definicoes.EXPIRA_SALDO_BONUS))
			return Definicoes.AJUSTE_EXPIRACAO_BONUS_CONTROLE;
		else if (tipoSaldoExpirado.equals(Definicoes.EXPIRA_SALDO_PERIODICO))
			return Definicoes.AJUSTE_EXPIRACAO_BONUS_CONTROLE;
		else if (tipoTransacao.equals(Definicoes.RECARGA_FRANQUIA_BONUS))
			return Definicoes.AJUSTE_EXPIRACAO_BONUS_CONTROLE;
		else 
			return Definicoes.AJUSTE_EXPIRACAO_FRANQUIA_CONTROLE;
	}
	
	public String getMsisdn() 
	{
		return msisdn;
	}
	public String getTipoSaldoExpirado() 
	{
		return tipoSaldoExpirado;
	}
	public String getDataReferencia() 
	{
		return dataReferencia;
	}
	public String getDataRecarga() 
	{
		return dataRecarga;
	}
	public double getSaldoInicial() 
	{
		return saldoInicial;
	}
	public double getSaldoFinal() 
	{
		return saldoFinal;
	}
	public int getIndNovoControle() 
	{
		return indNovoControle;
	}
	public String getTipoTransacao() 
	{
		return tipoTransacao;
	}
	
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	public void setTipoSaldoExpirado(String tipoSaldoExpirado) 
	{
		this.tipoSaldoExpirado = tipoSaldoExpirado;
	}
	public void setDataReferencia(String dataReferencia) 
	{
		this.dataReferencia = dataReferencia;
	}
	public void setDataRecarga(String dataRecarga) 
	{
		this.dataRecarga = dataRecarga;
	}
	public void setSaldoInicial(double saldoInicial) 
	{
		this.saldoInicial = saldoInicial;
	}
	public void setSaldoFinal(double saldoFinal) 
	{
		this.saldoFinal = saldoFinal;
	}
	public void setIndNovoControle(int indNovoControle) 
	{
		this.indNovoControle = indNovoControle;
	}
	public void setTipoTransacao(String tipoTransacao) 
	{
		this.tipoTransacao = tipoTransacao;
	}
}