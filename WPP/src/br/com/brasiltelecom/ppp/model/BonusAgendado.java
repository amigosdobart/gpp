package br.com.brasiltelecom.ppp.model;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 * Modela um BonusAgendado (Fila de Recargas)
 * 
 * @author Bernardo
 *
 */
public class BonusAgendado 
{
	private String bonusAgendado;

	private OrigemRecarga origemRecarga;

	private String valorAgendado;

	private String dataExecucaoAgendada;

	public OrigemRecarga getOrigemRecarga() {
		return origemRecarga;
	}

	public void setOrigemRecarga(OrigemRecarga origemRecarga) {
		this.origemRecarga = origemRecarga;
	}

	public String getBonusAgendado() {
		return bonusAgendado;
	}

	public void setBonusAgendado(String bonusAgendado) {
		this.bonusAgendado = bonusAgendado;
	}

	public String getValorAgendado() {
		return valorAgendado;
	}

	public void setValorAgendado(String valorAgendado) {
		this.valorAgendado = valorAgendado;
	}

	public String getDataExecucaoAgendada() {
		return dataExecucaoAgendada;
	}

	public void setDataExecucaoAgendada(String dataExecucaoAgendada) {
		this.dataExecucaoAgendada = dataExecucaoAgendada;
	}
	
	
}
