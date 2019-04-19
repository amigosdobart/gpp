package com.brt.gpp.aplicacoes.inserirRegistrosConfirmacaoPush;

/**
* @Autor: 		   Geraldo Palmeira
* Data: 		   01/03/2007
* 
*/
public class InserirRegistrosConfirmacaoPushVO 
{
	public InserirRegistrosConfirmacaoPushVO() 
	{		
	}
	
	private String    msisdn;
	private String    nome;
	private String    cpf;
	private String    idPush;
	private String    cod;
	private String    promocao;
	private String    bonus;
	
	/**
	 * @return the cod
	 */
	public String getCod()
	{
		return cod;
	}
	/**
	 * @param cod the cod to set
	 */
	public void setCod(String cod)
	{
		this.cod = cod;
	}
	/**
	 * @return the cpf
	 */
	public String getCpf()
	{
		return cpf;
	}
	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf)
	{
		this.cpf = cpf;
	}
	/**
	 * @return the msisdn
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	/**
	 * @return the nome
	 */
	public String getNome()
	{
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	/**
	 * @return the idPush
	 */
	public String getIdPush()
	{
		return idPush;
	}
	/**
	 * @param idPush the idPush to set
	 */
	public void setIdPush(String idPush)
	{
		this.idPush = idPush;
	}
	/**
	 * @return the bonus
	 */
	public String getBonus()
	{
		return bonus;
	}
	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(String bonus)
	{
		this.bonus = bonus;
	}
	/**
	 * @return the promocao
	 */
	public String getPromocao()
	{
		return promocao;
	}
	/**
	 * @param promocao the promocao to set
	 */
	public void setPromocao(String promocao)
	{
		this.promocao = promocao;
	}
}


