package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

//Imports GPP.

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Esta classe armazena as informacoes de um feriado nacional
 * Esta informacao serah utilizada principalmente na importacao
 * de CDRs de totalizacao pula pula para identificar se a chamada
 * deve foi realizada utilizando modulacao reduzida.
 * 
 * A data do feriado eh separado em dia, mes e ano para facilitar a repeticao
 * do feriado. 
 * Exemplos: 
 * Caso seja necessario cadastrar um feriado que se repete
 * na mesma data todos os anos a informacao ficaria da seguinte forma:
 * Dia:02
 * Mes:11
 * Ano:*
 * O caracter '*' indica qq valor ou seja no caso acima todos os dias 2 de novembro
 * de qualquer ano serao considerados feriados.
 * 
 * Caso um valor seja somente em uma data entao todo o valor deverah ser preenchido
 * Ex:
 * Dia:28
 * Mes:03
 * Ano:2005
 * 
 * Isto indica que somente o dia 28 de marco de 2005 serah feriado
 * 
 * @author Joao Carlos
 * Data..: 27/10/2005
 *
 */
public class Feriado implements Entidade
{
	private 		String 			dia;
	private 		String 			mes;
	private 		String 			ano;
	private 		String 			descricao;
	
	public Feriado(String dia, String mes, String ano)
	{
		setDia(dia);
		setMes(mes);
		setAno(ano);
	}

	public String getAno()
	{
		return ano;
	}

	private void setAno(String ano)
	{
		this.ano = ano;
	}

	public String getDescricao()
	{
		return descricao;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public String getDia()
	{
		return dia;
	}

	private void setDia(String dia)
	{
		this.dia = dia;
	}

	public String getMes()
	{
		return mes;
	}

	private void setMes(String mes)
	{
		this.mes = mes;
	}

	//Implementacao de Entidade.
	
	/**
	 * @see com.brt.gpp.comum.mapeamentos.entidade.Entidade#clone()
	 */
	public Object clone()
	{
	    Feriado result = new Feriado(this.dia, this.mes, this.ano);
	    result.setDescricao(this.descricao);
	    return result;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return (getAno()+getMes()+getDia()).hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getDia()+"/"+getMes()+"/"+getAno()+": "+getDescricao();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Feriado) )
			return false;
		
		return ((Feriado)obj).toString().equals(this.toString());
	}
	
}
