package com.brt.gpp.aplicacoes.enviarSMS;

import com.brt.gpp.comum.Definicoes;

  /**
  * Este arquivo contem a definicao da classe de DadosSMS
  * <hr>
  * <b>Modificado por:</b> Leone Parise Vieira da Silva<br>
  * <b>Data:</b> 14/08/2007<br>
  * <b>Razao:</b>
  * <ul>
  *   <li>Encapsulamento de Campos
  *   <li>Adocao de padroes de nomenclatura
  *   <li>Inclusao de novo argumento <code>msisdnOrigem</code>
  * </ul>
  * <hr>
  *
  * @author: Daniel Cintra Abib
  * @version: 1.0, 15/03/2004
  */

public class DadosSMS implements Comparable
{
	private long	id;
	private String	msisdn;
	private String	msisdnOrigem;
	private String 	mensagem;
	private int		prioridade;
	private String	data;
	private int		status;
	private String  tipo;

	/**
	 * Define o valor padrao do atributo <code>status</code>
	 * como <code>Definicoes.SMS_NAO_ENVIADO</code>
	 *
	 * @see com.brt.gpp.comum.Definicoes#SMS_NAO_ENVIADO
	 */
	public DadosSMS ()
	{
		/*Define um valor Default para o status como sendo um SMS nao enviado*/
		status = Definicoes.SMS_NAO_ENVIADO;
	}

	public long getId()
	{
	    return id;
	}

	public void setId(long id)
	{
	    this.id = id;
	}

	public String getMensagem()
	{
	    return mensagem;
	}

	public void setMensagem(String mensagem)
	{
	    this.mensagem = mensagem;
	}

	public String getMsisdn()
	{
	    return msisdn;
	}

	public void setMsisdn(String msisdn)
	{
	    this.msisdn = msisdn;
	}

	public String getMsisdnOrigem()
	{
	    return msisdnOrigem;
	}

	public void setMsisdnOrigem(String msisdnOrigem)
	{
	    this.msisdnOrigem = msisdnOrigem;
	}

	public int getPrioridade()
	{
	    return prioridade;
	}

	public void setPrioridade(int prioridade)
	{
	    this.prioridade = prioridade;
	}

	public int getStatus()
	{
	    return status;
	}

	public void setStatus(int status)
	{
	    this.status = status;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getTipo()
	{
	    return tipo;
	}

	public void setTipo(String tipo)
	{
	    this.tipo = tipo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String desStatus = this.status == Definicoes.SMS_ENVIADO     ? "SMS_ENVIADO" :
					this.status == Definicoes.SMS_NAO_ENVIADO ? "SMS_NAO_ENVIADO" : "SMS_SENDO_ENVIADO";

		StringBuffer sb = new StringBuffer("[DadosSMS]");
		sb.append("Id: ");
		sb.append(this.id);
		sb.append(" - MSISDN de Origem: ");
		sb.append(this.msisdnOrigem);
		sb.append(" - MSISDN de Destino: ");
		sb.append(this.msisdn);
		sb.append(" - Prioridade: ");
		sb.append(this.prioridade);
		sb.append(" - Data: ");
		sb.append(this.data);
		sb.append(" - Status: ");
		sb.append(desStatus);

		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new Long(getId()).hashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object o)
	 */
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof DadosSMS)
			return ((DadosSMS)obj).getId() == this.id;

		return false;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object o)
	 */
	public int compareTo(Object obj)
	{
		if (!(obj instanceof DadosSMS))
			throw new IllegalArgumentException("Classe de dados "+ obj.getClass().getName()+ " Nao pode ser comparada.");

		if ( this.id > ((DadosSMS)obj).getId() )
			return 1;
		else
			if ( this.id < ((DadosSMS)obj).getId() )
				return -1;
			else return 0;
	}
}
