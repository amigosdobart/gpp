package com.brt.gpp.aplicacoes.promocao.entidade;

import java.util.Date;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Classe responsavel por conter as informacoes
 * referentes a Mensagem do Bonus para Incentivo
 * 
 * @author João Paulo Galvagni
 * @since  16/11/2007
 */
public class MensagemBonusIncentivoRecarga implements Entidade
{
	private String 	mensagem;
	private int		numDiasAntecedeBonus;
	private int 	prioridade;
	
	public Object clone()
	{
		MensagemBonusIncentivoRecarga clone = new MensagemBonusIncentivoRecarga();
		
		clone.setMensagem(this.mensagem);
		clone.setNumDiasAntecedeBonus(this.numDiasAntecedeBonus);
		clone.setPrioridade(this.prioridade);
		
		return clone;
	}
	
	public String toString()
	{
		return "mensagem: " + mensagem +
			   " - numDiasAntecedeBonus: " + numDiasAntecedeBonus +
			   " - prioridade: " + prioridade;
	}
	
	public MensagemBonusIncentivoRecarga()
	{
		mensagem		= null;
		prioridade		= Definicoes.SMS_PRIORIDADE_ZERO;
	}
	
	/**
	 * @return the numDiasAntecedeBonus
	 */
	public int getNumDiasAntecedeBonus()
	{
		return numDiasAntecedeBonus;
	}
	
	/**
	 * @param numDiasAntecedeBonus the numDiasAntecedeBonus to set
	 */
	public void setNumDiasAntecedeBonus(int numDiasAntecedeBonus)
	{
		this.numDiasAntecedeBonus = numDiasAntecedeBonus;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem()
	{
		return mensagem;
	}
	
	/**
	 * @param mensagem the mensagem to set
	 */
	public void setMensagem(String mensagem)
	{
		this.mensagem = mensagem;
	}
	
	/**
	 * @return the prioridade
	 */
	public int getPrioridade()
	{
		return prioridade;
	}
	
	/**
	 * @param prioridade the prioridade to set
	 */
	public void setPrioridade(int prioridade)
	{
		this.prioridade = prioridade;
	}
}
