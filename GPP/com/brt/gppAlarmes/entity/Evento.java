package com.brt.gppAlarmes.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Esta classe representa as informacoes relativas aos eventos
 * do alarme
 * 
 * @author Joao Carlos
 * Data..: 17-Mar-2005
 *
 */
public class Evento
{
	private Alarme	alarme;
	private Date	dataExecucao;
	private int		codigoRetorno;
	private double	valorContador;

	public Alarme getAlarme()
	{
		return alarme;
	}

	public int getCodigoRetorno()
	{
		return codigoRetorno;
	}

	public Date getDataExecucao()
	{
		return dataExecucao;
	}

	public double getValorContador()
	{
		return valorContador;
	}

	public void setAlarme(Alarme alarme)
	{
		this.alarme = alarme;
	}

	public void setCodigoRetorno(int codigoRetorno)
	{
		this.codigoRetorno = codigoRetorno;
	}

	public void setDataExecucao(Date dataExecucao)
	{
		this.dataExecucao = dataExecucao;
	}

	public void setValorContador(double valorContador)
	{
		this.valorContador = valorContador;
	}
	
	public int hashCode()
	{
		return (getAlarme().getIdAlarme()+getDataExecucao().toString()).hashCode();
	}
	
	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mi:ss");
		return getAlarme() + " " + getDataExecucao() != null ? sdf.format(getDataExecucao()) : "";
	}
}
