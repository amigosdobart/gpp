package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>StatusMensagem</code>. Referência: TBL_REC_STATUS_MOTIVO_VOUCHER
 * 
 * @author Bernardo Vergne Dias Criado em: 11/07/2007
 */
public class StatusMotivoVoucher implements Serializable 
{
	private int idStatusVoucher;
	private MotivoBloqueioVoucher motivo;
	private String desMensagem;

	public String getDesMensagem() 
	{
		return desMensagem;
	}

	public MotivoBloqueioVoucher getMotivo() 
	{
		return motivo;
	}

	public int getIdStatusVoucher() 
	{
		return idStatusVoucher;
	}

	public void setDesMensagem(String string) 
	{
		desMensagem = string;
	}

	public void setMotivo(MotivoBloqueioVoucher motivo) 
	{
		this.motivo = motivo;
	}

	public void setIdStatusVoucher(int i) 
	{
		idStatusVoucher = i;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof StatusMotivoVoucher))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.motivo, ((StatusMotivoVoucher)obj).getMotivo());
		equal &= (this.idStatusVoucher == ((StatusMotivoVoucher)obj).getIdStatusVoucher());
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
		if(this.getMotivo() != null && this.getMotivo().getIdMotivo() != null) 
			result.append(this.getMotivo().getIdMotivo());
		result.append(this.getIdStatusVoucher());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[StatusMotivoVoucher]");
		result.append(";ID_STATUS_VOUCHER=" + this.idStatusVoucher);
		if (motivo != null && motivo.getDesMotivo() != null) result.append(";MOTIVO=" + this.motivo.getDesMotivo());
		if (desMensagem != null) result.append(";DESCRICAO=" + this.desMensagem);
	
		return result.toString();
	}
}
