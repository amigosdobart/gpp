package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>OperadoraGSM</code>. Referência: TBL_TAR_OPERADORA_GSM
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 11/04/2007
 */
public class OperadoraGSM implements Serializable
{
	private String nomeOperadoraGSM;
	private String desOperadoraGSM;
	private ValorDeslocamento valorDeslocamento;
		
	/**
	 * @return descrição da Operadora GSM
	 */
	public String getDesOperadoraGSM() 
	{
		return desOperadoraGSM;
	}

	/**
	 * @param desOperadoraGSM descrição da Operadora GSM
	 */
	public void setDesOperadoraGSM(String desOperadoraGSM) 
	{
		this.desOperadoraGSM = desOperadoraGSM;
	}

	/**
	 * @return nome da Operadora GSM
	 */
	public String getNomeOperadoraGSM() 
	{
		return nomeOperadoraGSM;
	}

	/**
	 * @param nomeOperadoraGSM nome da Operadora GSM
	 */
	public void setNomeOperadoraGSM(String nomeOperadoraGSM) 
	{
		this.nomeOperadoraGSM = nomeOperadoraGSM;
	}
	
	/**
	 * @return Instancia de <code>ValorDeslocamento</code>
	 */
	public ValorDeslocamento getValorDeslocamento() 
	{
		return valorDeslocamento;
	}

	/**
	 * @param valorDeslocamento Instancia de <code>ValorDeslocamento</code>
	 */
	public void setValorDeslocamento(ValorDeslocamento valorDeslocamento) 
	{
		this.valorDeslocamento = valorDeslocamento;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof OperadoraGSM))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.nomeOperadoraGSM, ((OperadoraGSM)obj).getNomeOperadoraGSM());
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
		result.append(this.nomeOperadoraGSM);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[OperadoraGSM]");
		if (nomeOperadoraGSM != null) 	result.append("NOME=" + this.nomeOperadoraGSM);
		if (desOperadoraGSM != null) 	result.append(";DESCRICAO=" + this.desOperadoraGSM);
		if (valorDeslocamento != null) 		
		{
			if (valorDeslocamento.getDesDeslocamento() != null) 
				result.append(";VALOR_DESLOCAMENTO=" + this.valorDeslocamento.getDesDeslocamento());
		}

		return result.toString();
	}

}
