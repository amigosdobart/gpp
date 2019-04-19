package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

//Imports GPP.

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Esta classe armazena os valores da da tabela de modulacao por plano de preco
 * 
 * @author Joao Carlos
 * Data..: 20/10/2005
 *
 */
public class ModulacaoPlanoPreco implements Entidade
{
	private int 	planoPreco;
	private long	periodoInicial;
	private long	periodoFinal;
	private int		diaSemana;
	private String	modulacao;
	
	public ModulacaoPlanoPreco(int plano)
	{
		setPlanoPreco(plano);
	}

	public String getModulacao()
	{
		return modulacao;
	}
	
	public void setModulacao(String modulacao)
	{
		this.modulacao = modulacao;
	}
	
	public long getPeriodoFinal()
	{
		return periodoFinal;
	}
	
	public void setPeriodoFinal(long periodoFinal)
	{
		this.periodoFinal = periodoFinal;
	}
	
	public long getPeriodoInicial()
	{
		return periodoInicial;
	}
	
	public void setPeriodoInicial(long periodoInicial)
	{
		this.periodoInicial = periodoInicial;
	}
	
	public int getPlanoPreco()
	{
		return planoPreco;
	}
	
	private void setPlanoPreco(int planoPreco)
	{
		this.planoPreco = planoPreco;
	}

	public void setDiaSemana(int diaSemana)
	{
		this.diaSemana = diaSemana;
	}
	
	public int getDiaSemana()
	{
		return this.diaSemana;
	}

	public boolean estaNoHorario(long horario)
	{
		return (horario >= getPeriodoInicial() && horario <= getPeriodoFinal());
	}
	
	//Implementacao de Entidade.
	
	public Object clone()
	{
	    ModulacaoPlanoPreco result = new ModulacaoPlanoPreco(this.planoPreco);
	    
	    result.setPeriodoInicial(this.periodoInicial);
	    result.setPeriodoFinal(this.periodoFinal);
	    result.setDiaSemana(this.diaSemana);
	    result.setModulacao(this.modulacao);
	    
	    return result;
	}

	public String toString()
	{
		return "Plano:"+getPlanoPreco()+" Ini:"+getPeriodoInicial()+" Fim:"+getPeriodoFinal()+" DiaSemana:"+getDiaSemana();
	}

	public boolean equals(Object obj)
	{
		if ( !(obj instanceof ModulacaoPlanoPreco) )
			return false;
		
		ModulacaoPlanoPreco mod = (ModulacaoPlanoPreco)obj;
		if (mod.getPlanoPreco()     == this.getPlanoPreco()     &&
			mod.getPeriodoInicial() == this.getPeriodoInicial() &&
			mod.getPeriodoFinal()   == this.getPeriodoFinal()	&&
			mod.getDiaSemana()		== this.getDiaSemana()
			)
			return true;
		
		return false;
	}
	
	public int hashCode()
	{
		return String.valueOf(getPlanoPreco()+""+getPeriodoInicial()+""+getPeriodoFinal()+""+getDiaSemana()).hashCode();
	}
}
