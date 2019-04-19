package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>MotivoBloqueioVoucher</code>. Referência: TBL_REC_MOTIVO_BLOQ_VOUCHER
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 11/07/2007
 */
public class MotivoBloqueioVoucher implements Serializable
{
	private String idMotivo;
	private String desMotivo;
		
	public String getDesMotivo() 
	{
		return desMotivo;
	}

	public void setDesMotivo(String desMotivo) 
	{
		this.desMotivo = desMotivo;
	}

	public String getIdMotivo() 
	{
		return idMotivo;
	}

	public void setIdMotivo(String idMotivo) 
	{
		this.idMotivo = idMotivo;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof MotivoBloqueioVoucher))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.idMotivo, ((MotivoBloqueioVoucher)obj).getIdMotivo());
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
		result.append(this.idMotivo);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[MotivoBloqueioVoucher]");
		if (idMotivo != null)  result.append("ID=" + this.idMotivo);
		if (desMotivo != null) result.append(";DESCRICAO=" + this.desMotivo);
	
		return result.toString();
	}

}
