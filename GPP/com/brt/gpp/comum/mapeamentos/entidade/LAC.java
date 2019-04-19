package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>LAC</code>. Referência: TBL_TAR_LAC
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 11/04/2007
 */
public class LAC implements Serializable
{
	private String idLAC;
	private OperadoraGSM operadoraGSM;
	private RegiaoLAC regiaoLAC;
	
	/**
	 * @return número LAC
	 */
	public String getIdLAC() 
	{
		return idLAC;
	}

	/**
	 * @param idLAC número LAC
	 */
	public void setIdLAC(String idLAC) 
	{
		this.idLAC = idLAC;
	}

	/**
	 * @return Instancia de <code>OperadoraGSM</code>
	 */
	public OperadoraGSM getOperadoraGSM() 
	{
		return operadoraGSM;
	}

	/**
	 * @param operadoraGSM Instancia de <code>OperadoraGSM</code>
	 */
	public void setOperadoraGSM(OperadoraGSM operadoraGSM) 
	{
		this.operadoraGSM = operadoraGSM;
	}

	/**
	 * @return Instancia de <code>RegiaoLAC</code>
	 */
	public RegiaoLAC getRegiaoLAC() 
	{
		return regiaoLAC;
	}

	/**
	 * @param regiaoLAC Instancia de <code>RegiaoLAC</code>
	 */
	public void setRegiaoLAC(RegiaoLAC regiaoLAC) 
	{
		this.regiaoLAC = regiaoLAC;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof LAC))
			return false;
		
		if (obj == this)
			return true;

		boolean equal = true;	
		equal &= isEqual(this.idLAC, 		((LAC)obj).getIdLAC());
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
		result.append(this.idLAC);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[LAC]");
		if (idLAC != null) 		result.append("ID=" + this.idLAC);
		
		if ( (operadoraGSM != null) && (operadoraGSM.getNomeOperadoraGSM() != null) )
			result.append(";OPERADORA_GSM=" + this.operadoraGSM.getNomeOperadoraGSM());
		
		if (regiaoLAC != null) 		
		{
			if ( (regiaoLAC.getPais() != null) && (regiaoLAC.getPais().getNomePais() != null)) 
				result.append(";PAIS=" + this.regiaoLAC.getPais().getNomePais());
	
			if ( (regiaoLAC.getCodigoNacional() != null) && (regiaoLAC.getCodigoNacional().getIdtUf() != null))
				result.append(";CN=" + this.regiaoLAC.getCodigoNacional().getIdtCodigoNacional());
				
			if ( (regiaoLAC.getCnAbrangencia() != null) && (regiaoLAC.getCnAbrangencia().getIdtUf() != null))
				result.append(";CN_ABRANGECIA=" + this.regiaoLAC.getCnAbrangencia().getIdtCodigoNacional());
			
			if ( (regiaoLAC.getValorDeslocamento() != null) && (regiaoLAC.getValorDeslocamento().getDesDeslocamento() != null))
				result.append(";VALOR_DESLOCAMENTO=" + this.regiaoLAC.getValorDeslocamento().getDesDeslocamento());
		}
		
		return result.toString();
	}

	
}
