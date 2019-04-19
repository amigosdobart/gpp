package com.brt.gppAlarmes.entity;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Date;

/**
 * Essa classe realiza a implementacao do armazenamento dos dados
 * relativos a informacao sobre o alarme.
 * 
 * @author Joao Carlos
 * Data..: 16-Marco-2005
 *
 */

public class Alarme
{
	private String 		idAlarme;
	private String 		nomeAlarme;
	private String		status;
	private Date		dataUltimaExecucao;
	private boolean		enviaTrapSNMPAlerta;
	private boolean		enviaTrapSNMPFalha;
	private boolean		enviaSMSAlerta;
	private boolean		enviaSMSFalha;
	private boolean		enviaEMailAlerta;
	private boolean		enviaEMailFalha;
	private double		valorMinAlerta;
	private double		valorMaxAlerta;
	private double		valorMinFalha;
	private double		valorMaxFalha;
	private long		atrasoMaxAlerta;
	private long		atrasoMaxFalha;
	private Agendamento	agendamento;
	private Collection	eventos;
	private Collection	listaEMail;
	private Collection	listaAssinantes;
	private String		sqlBuscaContador;
	private String		motivoAlarme;
	private int			numDiasHistorico;
	
	public static final String ALARME_FALHA		= "FALHA";
	public static final String ALARME_ALERTA	= "ALERTA";
	public static final String ALARME_OK		= "OK";
	
	public static final int CODIGO_RETORNO_OK	= 0;
	
	public static final String MOTIVO_ATRASO	= "Por Atraso na Execucao";
	public static final String MOTIVO_VALOR_MAX	= "Por Valor Maximo Alcancado";
	public static final String MOTIVO_VALOR_MIN	= "Por Valor Minimo Alcancado";
	public static final String MOTIVO_RETORNO	= "Por Codigo de Retorno";
	
	public Alarme(String idAlarme)
	{
		setIdAlarme(idAlarme);
		agendamento		= new Agendamento();
		listaEMail		= new LinkedHashSet();
		listaAssinantes	= new LinkedHashSet();
	}

	public long getAtrasoMaxFalha()
	{
		return atrasoMaxFalha;
	}
	
	public long getAtrasoMaxAlerta()
	{
		return atrasoMaxAlerta;
	}
	
	public boolean isEnviaSMSAlerta()
	{
		return enviaSMSAlerta;
	}
	
	public boolean isEnviaSMSFalha()
	{
		return enviaSMSFalha;
	}
	
	public boolean isEnviaTrapSNMPAlerta()
	{
		return enviaTrapSNMPAlerta;
	}
	
	public boolean isEnviaTrapSNMPFalha()
	{
		return enviaTrapSNMPFalha;
	}

	public boolean isEnviaEMailFalha()
	{
		return enviaEMailFalha;
	}

	public boolean isEnviaEMailAlerta()
	{
		return enviaEMailAlerta;
	}

	public String getIdAlarme()
	{
		return idAlarme;
	}
	
	public String getNomeAlarme()
	{
		return nomeAlarme;
	}
	
	public String getStatus()
	{
		return status;
	}
	public double getValorMaxAlerta()
	{
		return valorMaxAlerta;
	}
	
	public double getValorMaxFalha()
	{
		return valorMaxFalha;
	}
	
	public double getValorMinAlerta()
	{
		return valorMinAlerta;
	}
	
	public double getValorMinFalha()
	{
		return valorMinFalha;
	}

	public String getSQLBuscaContador()
	{
		return sqlBuscaContador;
	}

	public Agendamento getAgendamento()
	{
		return agendamento;
	}

	public Collection getEventos()
	{
		return eventos;
	}

	public Collection getListaEMail()
	{
		return listaEMail;
	}

	public Collection getListaAssinantes()
	{
		return listaAssinantes;
	}
	
	public Date getDataUltimaExecucao()
	{
		return dataUltimaExecucao;
	}

	public String getMotivoAlarme()
	{
		return motivoAlarme;
	}

	public int getNumDiasHistorico()
	{
		return numDiasHistorico;
	}

	public void setAtrasoMaxFalha(long atrasoMaxFalha)
	{
		this.atrasoMaxFalha = atrasoMaxFalha;
	}
	
	public void setAtrasoMaxAlerta(long atrasoMaxAlerta)
	{
		this.atrasoMaxAlerta = atrasoMaxAlerta;
	}
	
	public void setEnviaSMSAlerta(boolean enviaSMSAlerta)
	{
		this.enviaSMSAlerta = enviaSMSAlerta;
	}
	
	public void setEnviaSMSFalha(boolean enviaSMSFalha)
	{
		this.enviaSMSFalha = enviaSMSFalha;
	}
	
	public void setEnviaTrapSNMPAlerta(boolean enviaTrapSNMPAlerta)
	{
		this.enviaTrapSNMPAlerta = enviaTrapSNMPAlerta;
	}
	
	public void setEnviaTrapSNMPFalha(boolean enviaTrapSNMPFalha)
	{
		this.enviaTrapSNMPFalha = enviaTrapSNMPFalha;
	}

	public void setEnviaEMailFalha(boolean enviaEMailFalha)
	{
		this.enviaEMailFalha = enviaEMailFalha;
	}

	public void setEnviaEMailAlerta(boolean enviaEMailAlerta)
	{
		this.enviaEMailAlerta = enviaEMailAlerta;
	}

	private void setIdAlarme(String idAlarme)
	{
		if (idAlarme == null || idAlarme.equals(""))
			throw new IllegalArgumentException("Id invalido para o Alarme");

		this.idAlarme = idAlarme;
	}
	
	public void setNomeAlarme(String nomeAlarme)
	{
		this.nomeAlarme = nomeAlarme;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public void setValorMaxAlerta(double valorMaxAlerta)
	{
		this.valorMaxAlerta = valorMaxAlerta;
	}
	
	public void setValorMaxFalha(double valorMaxFalha)
	{
		this.valorMaxFalha = valorMaxFalha;
	}
	
	public void setValorMinAlerta(double valorMinAlerta)
	{
		this.valorMinAlerta = valorMinAlerta;
	}
	
	public void setValorMinFalha(double valorMinFalha)
	{
		this.valorMinFalha = valorMinFalha;
	}

	public void setSQLBuscaContador(String sql)
	{
		this.sqlBuscaContador = sql;
	}

	public void setAgendamento(Agendamento agendamento)
	{
		this.agendamento = agendamento;
	}

	public void setEventos(Collection eventos)
	{
		this.eventos = eventos;
	}

	public void setListaEMail(Collection listaEMail)
	{
		this.listaEMail = listaEMail;
	}

	public void setListaAssinantes(Collection listaAssinantes)
	{
		this.listaAssinantes = listaAssinantes;
	}

	public void setDataUltimaExecucao(Date dataUltimaExecucao)
	{
		this.dataUltimaExecucao = dataUltimaExecucao;
	}

	public void setMotivoAlarme(String motivoAlarme)
	{
		this.motivoAlarme = motivoAlarme;
	}

	public void setNumDiasHistorico(int numDiasHistorico)
	{
		this.numDiasHistorico = numDiasHistorico;
	}

	public int hashCode()
	{
		return getIdAlarme().hashCode();
	}
	
	public String toString()
	{
		return getNomeAlarme();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Alarme) )
			return false;
		
		return this.getIdAlarme().equals(((Alarme)obj).getIdAlarme());
	}
}
