package com.brt.gpp.aplicacoes.enviarUsuariosStatus;
/**
 * Entidade responsavel por transportar os dados do Produtor para o Consumidor
 * no Processo Batch EnvioUsuarioStatus.
 *
 * @author Leone Parise Vieira da Silva
 * @since  27/09/2007
 */
public class UsuariosStatusVO
{
	private String msisdn;
	private String data;
	private String status;

	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public String getMsisdn()
	{
		return msisdn;
	}
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}

}
