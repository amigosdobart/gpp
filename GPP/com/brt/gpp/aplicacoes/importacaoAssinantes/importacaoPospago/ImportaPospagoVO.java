package com.brt.gpp.aplicacoes.importacaoAssinantes.importacaoPospago;

import java.sql.Timestamp;

/**
 * Entidade que mapeia os dados gerados pelo produtor
 */
public class ImportaPospagoVO 
{
	private String		msisdn;
	private Timestamp	dataInclusao;
	private long		idtProcessamento;
	private String 		acao;	
	private	int			promocao;

	public long getIdtProcessamento() 
	{
		return idtProcessamento;
	}

	public void setIdtProcessamento(long idtProcessamento) 
	{
		this.idtProcessamento = idtProcessamento;
	}

	/**
	 * @return msisdn	msisdn
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}
	
	/**
	 * @return dataInclusao	dataInclusao
	 */
	public Timestamp getDataInclusao() 
	{
		return dataInclusao;
	}
	
	/**
	 * @return acao	acao
	 */
	public String getAcao() 
	{
		return acao;
	}
	
	/**
	 * @return promocao	promocao
	 */
	public int getPromocao() 
	{
		return promocao;
	}

	/**
	 * @param msisdn	msisdn
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	
	/**
	 * @param dataInclusao	dataInclusao
	 */
	public void setDataInclusao(Timestamp dataInclusao) 
	{
		this.dataInclusao = dataInclusao;
	}
	
	/**
	 * @param acao	acao
	 */
	public void setAcao(String acao) 
	{
		this.acao = acao;
	}
	
	/**
	 * @param promocao	promocao
	 */
	public void setPromocao(int promocao) 
	{
		this.promocao = promocao;
	}
}
