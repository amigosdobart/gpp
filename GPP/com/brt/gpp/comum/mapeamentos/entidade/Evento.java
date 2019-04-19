package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Date;

/**
 * Entidade <code>Evento</code>. Referência: TBL_TAR_EVENTO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class Evento implements Serializable
{
	private Date dataAlteracao;
	private TipoEvento tipoEvento;
	private int codRetorno;
	private String nomeOperador;
	private String xmlAcao;
		
	/**
	 * @return Código de retorno
	 */
	public int getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * @param codRetorno Código de retorno
	 */
	public void setCodRetorno(int codRetorno) 
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * @return Data de alteracao
	 */
	public Date getDataAlteracao() 
	{
		return dataAlteracao;
	}

	/**
	 * @param dataAlteracao Data de alteracao
	 */
	public void setDataAlteracao(Date dataAlteracao) 
	{
		this.dataAlteracao = dataAlteracao;
	}

	/**
	 * @return Nome do operador
	 */
	public String getNomeOperador() 
	{
		return nomeOperador;
	}

	/**
	 * @param nomeOperador Nome do operador
	 */
	public void setNomeOperador(String nomeOperador) 
	{
		this.nomeOperador = nomeOperador;
	}

	/**
	 * @return Tipo de evento
	 */
	public TipoEvento getTipoEvento() 
	{
		return tipoEvento;
	}

	/**
	 * @param tipoEvento Tipo de evento
	 */
	public void setTipoEvento(TipoEvento tipoEvento) 
	{
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return XML de ação
	 */
	public String getXmlAcao() 
	{
		return xmlAcao;
	}

	/**
	 * @param xmlAcao XML de ação
	 */
	public void setXmlAcao(String xmlAcao) 
	{
		this.xmlAcao = xmlAcao;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Evento))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.dataAlteracao,((Evento)obj).getDataAlteracao());
		equal &= isEqual(this.tipoEvento, 	((Evento)obj).getTipoEvento());
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
		if (dataAlteracao != null) 	result.append(this.dataAlteracao);
		if (tipoEvento!= null) 		result.append(this.tipoEvento.getIdTipoEvento());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[TipoEvento]");
		if (dataAlteracao != null)	result.append("DATA_ALTERACAO=" + this.dataAlteracao);
		if (tipoEvento != null)		result.append("TIPO_EVENTO=" + this.tipoEvento.getDesTipoEvento());
		if (nomeOperador != null)	result.append("NOME_OPERADOR=" + this.nomeOperador);
		result.append(";CODIGO_RETORNO=" + this.codRetorno);
		
		return result.toString();
	}

}
