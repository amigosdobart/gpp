/*
 * Created on 14/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.brt.gppLog;

import java.util.*;
import java.text.*;
import com.brt.gpp.comum.*;

public class GPPLog 
{
	private long 	idProcesso;
	private Date 	dataHoraRegistro;
	private String 	severidade;
	private String  nomeServidor;
	private String	componente;
	private String  classe;
	private String	metodo;
	private String	mensagem;
	private String  nomeFiguraSeveridade;
	
	private static final String NOME_FIGURA_INFO 	= "img/bola_verde.gif";
	private static final String NOME_FIGURA_DEGUB 	= "img/bola_preta.gif";
	private static final String NOME_FIGURA_ERRO 	= "img/bola_vermelha.gif";
	private static final String NOME_FIGURA_WARN 	= "img/bola_amarela.gif";
	private static final String NOME_FIGURA_FATAL 	= "img/bola_vermelha.gif";

	/**
	 * @return data e hora do registro 
	 */
	public Date getDataHoraRegistro()
	{
		return dataHoraRegistro;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataHoraFormatada(String formato)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		if (getDataHoraRegistro() == null)
			return null;
		return sdf.format(getDataHoraRegistro());
	}
	
	/**
	 * @return id do processo
	 */
	public long getIdProcesso()
	{
		return idProcesso;
	}

	/**
	 * @return mensagem do log
	 */
	public String getMensagem()
	{
		return mensagem;
	}

	/**
	 * @return nome do metodo que gerou o log
	 */
	public String getMetodo()
	{
		return metodo;
	}

	/**
	 * @return serveridade do log [DEBUG,INFO,ERRO,WARN]
	 */
	public String getSeveridade()
	{
		return severidade;
	}

	/**
	 * @return nome do componente que gerou o log
	 */
	public String getComponente()
	{
		return componente;
	}

	/**
	 * @return
	 */
	public String getClasse() {
		return classe;
	}

	/**
	 * @return String
	 */
	public String getNomeFiguraSeveridade() {
		return nomeFiguraSeveridade;
	}

	/**
	 * @return String
	 */
	public String getNomeServidor()
	{
		return nomeServidor;
	}

	/**
	 * @param date 
	 */
	public void setDataHoraRegistro(Date aDataRegistro)
	{
		//if (aDataRegistro == null)
		//	throw new IllegalArgumentException("Data de registro invalida (null)");
			
		dataHoraRegistro = aDataRegistro;
	}

	/**
	 * @param String 
	 */
	public void setDataHoraRegistro(String aDataRegistro)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date aData=null;
		if (aDataRegistro != null)
			try
			{
				aData = sdf.parse(aDataRegistro);
			}
			catch(ParseException pe)
			{
				aData = null;
			}

		dataHoraRegistro = aData;
	}

	/**
	 * @param l
	 */
	public void setIdProcesso(long aIdProcesso)
	{
		idProcesso = aIdProcesso;
	}

	/**
	 * @param string
	 */
	public void setMensagem(String aMensagem)
	{
		//if (aMensagem == null)
		//	throw new IllegalArgumentException("Mensagem invalida (null)");

		mensagem = aMensagem;
	}

	/**
	 * @param string
	 */
	public void setMetodo(String aMetodo)
	{
		metodo = aMetodo;
	}

	/**
	 * @param string
	 */
	public void setComponente(String aComponente)
	{
		componente = aComponente;
	}

	/**
	 * @param string
	 */
	public void setClasse(String aClasse) {
		if (aClasse != null)
			aClasse.trim();

		classe = aClasse;
	}

	/**
	 * @param string
	 */
	public void setNomeServidor(String aServidor) {
		if (aServidor != null)
			aServidor.trim();

		nomeServidor = aServidor;
	}
	
	/**
	 * @param string
	 */
	public void setSeveridade(String aSeveridade)
	{
		String nomeFigura=null;
		boolean severidadeValida=false;

		if (aSeveridade == null)
		{
			nomeFigura = GPPLog.NOME_FIGURA_ERRO;
			severidadeValida=true;
		}else
			//throw new IllegalArgumentException("Severidade invalida (null)");
		
		if (aSeveridade.toUpperCase().equals(Definicoes.LINFO.trim()))
		{
			nomeFigura = GPPLog.NOME_FIGURA_INFO;
			severidadeValida=true;
		}
		else if (aSeveridade.toUpperCase().equals(Definicoes.LDEBUG.trim()))
			{
				nomeFigura = GPPLog.NOME_FIGURA_DEGUB;
				severidadeValida=true;
			}
		else if (aSeveridade.toUpperCase().equals(Definicoes.LERRO.trim()))
		{
			nomeFigura = GPPLog.NOME_FIGURA_ERRO;
			severidadeValida=true;
		}
		else if (aSeveridade.toUpperCase().equals(Definicoes.LFATAL.trim()))
		{
			nomeFigura = GPPLog.NOME_FIGURA_FATAL;
			severidadeValida=true;
		}
		else if (aSeveridade.toUpperCase().equals(Definicoes.LWARN.trim()))
		{
			nomeFigura = GPPLog.NOME_FIGURA_WARN;
			severidadeValida=true;
		}
		
		if (severidadeValida)
		{
			if (aSeveridade==null)
				severidade = "->";
			else 
				severidade = aSeveridade.toUpperCase().trim();
			nomeFiguraSeveridade = nomeFigura;
		}
		else
			throw new IllegalArgumentException("Severidade invalida. " + aSeveridade);		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		if (getMensagem() == null)
			return "Mensagem Nula";
		return getMensagem();
	}
}
