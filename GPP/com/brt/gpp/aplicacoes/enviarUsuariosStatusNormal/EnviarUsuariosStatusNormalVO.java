package com.brt.gpp.aplicacoes.enviarUsuariosStatusNormal;

import java.sql.Timestamp;

/**
 *	Value Object para consumo de registros do processo de EnviarStatusUsuarioNormal.
 * 
 *	@author		Jorge Abreu
 *	@since		01/10/2007
 */
public class EnviarUsuariosStatusNormalVO 
{

	/**
	 *	Data de processamento em que o assinante mudou de Status 1 p/ 2.
	 */
	private Timestamp datProcessamento; 
	
	/**
	 *	MSISDN do assinante.
	 */
	private String idtMsisdn;
	
	/**
	 *  Status do assinante.
	 */
	private String idtStatusAssinante;
	
	/**
	 *  Status do processamento.
	 */
	private String idtStatusProcessamento;
	
	/**
	 *	Construtor da classe.
	 */
	public EnviarUsuariosStatusNormalVO(){	}
	
	public void setDatProcessamento(Timestamp datProcessamento)
	{
		this.datProcessamento = datProcessamento;
	}
	public Timestamp getDatProcessamento()
	{
		return this.datProcessamento;
	}
	public void setIdtMsisdn(String idtMsisdn)
	{
		this.idtMsisdn = idtMsisdn;
	}
	public String getIdtMsisdn()
	{
		return this.idtMsisdn;
	}
	public void setIdtStatusAssinante(String idtStatusAssinante)
	{
		this.idtStatusAssinante = idtStatusAssinante;
	}
	public String getIdtStatusAssinante()
	{
		return this.idtStatusAssinante;
	}
	public void setIdtStatusProcessamento(String idtStatusProcessamento)
	{
		this.idtStatusProcessamento = idtStatusProcessamento;
	}
	public String getIdtStatusProcessamento()
	{
		return idtStatusProcessamento;
	}
	
}
