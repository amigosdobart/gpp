package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>DiaSemana</code>. Referência: TBL_TAR_DIA_SEMANA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class DiaSemana implements Serializable
{
	private int idDiaSemana;
	private OperadoraLD operadoraLD;
	private String tipoDiaSemana;
	
	/**
	 * @return Dia da semana
	 */
	public int getIdDiaSemana() 
	{
		return idDiaSemana;
	}

	/**
	 * @param idDiaSemana Dia da semana
	 */
	public void setIdDiaSemana(int idDiaSemana) 
	{
		this.idDiaSemana = idDiaSemana;
	}

	/**
	 * @return Instancia de <code>OperadoraLD</code>
	 */
	public OperadoraLD getOperadoraLD() 
	{
		return operadoraLD;
	}

	/**
	 * @param operadoraLD Instancia de <code>OperadoraLD</code>
	 */
	public void setOperadoraLD(OperadoraLD operadoraLD) 
	{
		this.operadoraLD = operadoraLD;
	}

	/**
	 * @return Tipo do dia da semana
	 */
	public String getTipoDiaSemana() 
	{
		return tipoDiaSemana;
	}

	/**Tipo do dia da semanathe tipoDiaSemana to set
	 */
	public void setTipoDiaSemana(String tipoDiaSemana) 
	{
		this.tipoDiaSemana = tipoDiaSemana;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof DiaSemana))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;
		equal &= this.idDiaSemana == ((DiaSemana)obj).getIdDiaSemana();
		equal &= isEqual(this.operadoraLD, 	((DiaSemana)obj).getOperadoraLD());
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
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idDiaSemana);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[DiaSemana]");
		result.append("ID=" + this.idDiaSemana);
		if (tipoDiaSemana != null) result.append(";TIPO=" + this.tipoDiaSemana);
		if (operadoraLD != null) result.append(";OPERADORA_LD=" + this.operadoraLD.getNomeOperadora());
	
		return result.toString();
	}

}
