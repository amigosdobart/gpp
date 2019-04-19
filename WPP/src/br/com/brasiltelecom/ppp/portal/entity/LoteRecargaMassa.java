package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;

/**
 * Entidade <code>LoteRecargaMassa</code>. 
 * 
 * Usada para representar uma consulta GroupBy pelo Hibernate. (vide DAO)
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 09/08/2007
 */
public class LoteRecargaMassa implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String numLote;
	private int totalRegistros;
	private double totalVlrBonus;
	private double totalVlrSm;
	private double totalVlrDados;
	private int statusProcessamento;
	private String desStatusProcessamento;
	private Date dataProcessamento;
	private String usuario;
	
	public String getDesStatusProcessamento() 
	{
		return desStatusProcessamento;
	}

	public void setDesStatusProcessamento(String desStatusProcessamento) 
	{
		this.desStatusProcessamento = desStatusProcessamento;
	}

	public String getNumLote() 
	{
		return numLote;
	}

	public void setNumLote(String numLote) 
	{
		this.numLote = numLote;
	}

	public int getStatusProcessamento() 
	{
		return statusProcessamento;
	}

	public void setStatusProcessamento(int statusProcessamento) 
	{
		this.statusProcessamento = statusProcessamento;
		
		switch (statusProcessamento)
		{
			case Definicoes.STATUS_RECARGA_MASSA_PENDENTE:
				desStatusProcessamento = "Pendente de aprovacao/rejeicao";
				break;
			case Definicoes.STATUS_RECARGA_MASSA_APROVADO:
				desStatusProcessamento = "Lote aprovado";
				break;
			case Definicoes.STATUS_RECARGA_MASSA_REJEITADO:
				desStatusProcessamento = "Lote rejeitado";
				break;
			case Definicoes.STATUS_RECARGA_MASSA_ERRO:
				desStatusProcessamento = "Erro de processamento";
				break;
			default:
				desStatusProcessamento = "Status desconhecido";
				break;
		}
		
	}

	public int getTotalRegistros() 
	{
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) 
	{
		this.totalRegistros = totalRegistros;
	}

	public double getTotalVlrBonus() 
	{
		return totalVlrBonus;
	}

	public void setTotalVlrBonus(double totalVlrBonus) 
	{
		this.totalVlrBonus = totalVlrBonus;
	}

	public double getTotalVlrDados() 
	{
		return totalVlrDados;
	}

	public void setTotalVlrDados(double totalVlrDados) 
	{
		this.totalVlrDados = totalVlrDados;
	}

	public double getTotalVlrSm() 
	{
		return totalVlrSm;
	}

	public void setTotalVlrSm(double totalVlrSm) 
	{
		this.totalVlrSm = totalVlrSm;
	}
	
	public String getUsuario() 
	{
		return usuario;
	}

	public void setUsuario(String usuario) 
	{
		this.usuario = usuario;
	}
	
	public Date getDataProcessamento() 
	{
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) 
	{
		this.dataProcessamento = dataProcessamento;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof LoteRecargaMassa))
			return false;
		
		if (obj == this)
			return true;

		boolean equal = true;	
		equal &= isEqual(this.numLote, 		((LoteRecargaMassa)obj).getNumLote());
		equal &= (this.statusProcessamento == ((LoteRecargaMassa)obj).getStatusProcessamento());
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
		result.append(this.numLote);
		result.append(this.statusProcessamento);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[LoteRecargaMassa]");
		if (numLote != null) 					result.append("NUM_LOTE=" + this.numLote);
		if (desStatusProcessamento != null)	    result.append("DES_STATUS=" + this.desStatusProcessamento);
		
		return result.toString();
	}
}
