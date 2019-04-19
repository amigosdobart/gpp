// Definicao do Pacote
package com.brt.gpp.comum;

// Arquivos de Import de Java
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import java.text.*;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import TINC.TDateTime;

/**
  *
  * Este arquivo contem a classe abstrata de data do GPP
  * Contem uma abstracao de Calendar e Date do java
  * <P> Version:		1.0
  *
  * @Autor: 		Daniel Cintra Abib
  * Data: 			20/03/2004
  *
  */
abstract public class GPPData
{
	/**
	 * Metodo...: dataFormatada
	 * Descricao: Retorna a data no formato DD/MM/AAAA 
	 * @param	
	 * @return 	String	- Data no formado DD/MM/AAA
	 */
	public static String dataFormatada ( )
	{
		// Calculando a data no formato DD/MM/AAA
		Calendar auxData = Calendar.getInstance();
		String dia = (new Integer(auxData.get(Calendar.DAY_OF_MONTH))).toString();
		String mes = (new Integer(auxData.get(Calendar.MONTH)+1)).toString();
		String ano = (new Integer(auxData.get(Calendar.YEAR))).toString();

		if ( dia.length() == 1 )
			dia = "0" + dia;
		if ( mes.length() == 1 )
			mes = "0" + mes;

		String dataFinal = dia + "/" + mes + "/" + ano;

		return dataFinal;		
	}

	/** 
	 * Metodo...: horaFormatada
	 * Descricao: Retorna a hora no formato HH:MI:SS
	 * @param	
	 * @return 	String	- Data no formado HH:MI:SS
	 */
	public static String horaFormatada ( )
	{
		// Calculando a hora no formato HH:MI:SS
		Calendar auxHora = Calendar.getInstance();

		String hora = (new Integer(auxHora.get(Calendar.HOUR_OF_DAY))).toString();
		String minuto = (new Integer(auxHora.get(Calendar.MINUTE))).toString();
		String segundo = (new Integer(auxHora.get(Calendar.SECOND))).toString();
			
		if (hora.length() == 1)
			hora = "0" + hora;
		if (minuto.length() == 1)
			minuto = "0" + minuto;
		if (segundo.length() == 1)
			segundo = "0" + segundo;

		String horaFinal = hora + ":" + minuto + ":" + segundo;
		
		return horaFinal;
	}

	/** 
	 * Metodo...: dataCompletaForamtada
	 * Descricao: Retorna a data e hora no formato DD/MM/AAA HH:MI:SS
	 * @param
	 * @return 	String	- Data no formado DD/MM/AAA HH:MI:SS
	 */
	public static String dataCompletaForamtada ( )
	{
		String data = GPPData.dataFormatada();
		String hora = GPPData.horaFormatada();
		
		String dataHoraFinal = data + " " + hora;
		
		return dataHoraFinal;
	}
	
	/**
	 * Metodo...: formataDataHora
	 * Descricao: Recebe uma data (String) e retorna essa 
	 * 			  data no formato DD/MM/AAAA HH:MI:SS
	 * @param	aDataHora 	- Data e hora no formato string
	 * @return 	String		- Data no formado DD/MM/AAA HH:MI:SS
	 */
	public static String formataDataHora (String aDataHora)
	{
		String retorno= aDataHora;
		
		int tamDataHora = 0;
		
		int tamParteData = 0;
		int tamParteHora = 0;
		
		int tamParteDia = 0;
		int tamParteMes = 0;
		int tamParteAno = 0;
		
		int tamParteHH = 0;
		int tamParteMI = 0;
		int tamParteSS = 0;
				
		String data="";
		String hora="";
		
		String dia = "";
		String mes = "";
		String ano = "";
		
		String hh = "";
		String mi = "";
		String ss = "";
		
		tamDataHora = aDataHora.length();
		tamParteData = aDataHora.indexOf(" ");
		
		if (tamParteData > 0)
		{	
			data = aDataHora.substring(0,tamParteData);
			hora = aDataHora.substring(tamParteData+1, tamDataHora);
			tamParteHora = tamDataHora;
		}
		else
		{			
			data = aDataHora;
			tamParteData = tamDataHora;
		}			
		
		if (! ((data.trim()).equals("")) )
		{
			tamParteDia = data.indexOf("/");
			dia = data.substring(0, tamParteDia);
			if (dia.length() < 2)
				dia = "0" + dia;
			
			tamParteMes = data.indexOf("/", tamParteDia+1); 
			mes = data.substring(tamParteDia+1,tamParteMes);
			if (mes.length() < 2)
				mes = "0" + mes;
			
			tamParteAno = tamParteData;
			ano = data.substring(tamParteMes+1, tamParteAno);
			
			String dataFormatada = dia + "/" + mes + "/" + ano;
			
			if (! ((hora.trim()).equals("")) )
			{
				tamParteHH = hora.indexOf(":");
				hh = hora.substring(0, tamParteHH);
				if (hh.length() < 2)
					hh = "0" + hh;
				
				tamParteMI = hora.indexOf(":", tamParteHH+1); 
				mi = hora.substring(tamParteHH+1,tamParteMI);
				if (mi.length() < 2)
					mi = "0" + mi;
					
				tamParteSS = tamParteHora-tamParteData-1; 
				ss = hora.substring(tamParteMI+1,tamParteSS);
				if (ss.length() < 2)
					ss = "0" + ss;
				
				String horaFormatada = " " + hh + ":" + mi + ":" + ss;
				dataFormatada =  dataFormatada + horaFormatada;
			}
			
			retorno = dataFormatada;
		}		
		
		return retorno;
	}
	
	/**
	 * Metodo...: mudaFormato
	 * Descricao: Recebe uma data (String) no formato "DD/MM/AAAA"
	 * 			  e retorna essa data no formato AAAAMMDD 
	 * @param
	 * @return 	String	- Data no formado AAAAMMDD
	 */	
	static public String mudaFormato(String data)
	{
		String retorno = null;
		
		data = formataDataHora(data);
		
		String dia = data.substring(0,2);
		String mes = data.substring(3,5);
		String ano = data.substring(6);
		
		retorno = ano+mes+dia; 
		
		return retorno;
	}
	
	/**
	 * Metodo...: mudaFormatoStringToDate
	 * Descricao: Recebe um string yyyymmdd e retorna dd/mm/yyyy
	 * @param 	String	yyyymmdd	Data no formato string yyyymmdd
	 * @return	String				Data no formato dd/mm/yyyy
	 */
	static public String mudaFormatoStringToDate(String yyyymmdd)
	{
		String retorno = null;
		
		if(yyyymmdd != null)
		{
			if(!yyyymmdd.equals(""))
			{
				return yyyymmdd.substring(6) + "/" + yyyymmdd.substring(4,6) + "/" + yyyymmdd.substring(0,4);
			}
		}
		
		return retorno;
	}
	
	/** 
	 * Metodo...: formataDataRecarga
	 * Descricao: Recebe uma data no formato DD/MM/AAA HH:MI:SS
	 * 			  e retorna essa data no formato AAAAMMDDHHMISS
	 * @param
	 * @return 	String	- Data no formado AAAAMMDDHHMISS
	 */	

	static public String formataDataRecarga(String dataHora)
	{
		String retorno = null;
		
		//Acerta formato da string
		dataHora = formataDataHora(dataHora);
		
		// Transforma o formato da data para yyyymmddhhmmss
		String dia = dataHora.substring(0,2);
		String mes = dataHora.substring(3,5);
		String ano = dataHora.substring(6,10);
		String hora = dataHora.substring(11,13);
		String min = dataHora.substring(14,16);
		String seg = dataHora.substring(17,19);
		
		retorno = ano +  mes + dia + hora + min + seg; 
		
		return retorno;
	}
	
	
	/** 
	 * Metodo...: getDataAcrescidaDias
	 * Descricao: Recebe o numero de dias a ser adicionado a uma data
	 * 			  e retorna a data no formato DD/MM/AAAA
	 * @param
	 * @return 	String	- Data no formado DD/MM/AAAA
	 */	

	static public String getDataAcrescidaDias(String dias)
	{
		String retorno = "";
		
		// Funcao que determina a data de expiracao
		GregorianCalendar auxData = new GregorianCalendar();      

		// Adiciona os dias de expiracao na data do sistema                  
		auxData.add( Calendar.DAY_OF_MONTH, Integer.parseInt(dias) );
					
		// Funcao de DateFormat - formatacao de datas
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		// Formata a String adequadamente
		retorno = df.format(auxData.getTime());
		
		return retorno;
	}
	
	/**
	 * Metodo....:getDataAcrescidaDias
	 * Descricao.:Retorna uma data acrescida de uma quantidade de dias
	 * @param numDias	- Numero de dias
	 * @return Date 	- Data acrescida dos dias
	 */
	static public Date getDataAcrescidaDias(int numDias)
	{
		Calendar dataAtual = Calendar.getInstance();
		dataAtual.add(Calendar.DAY_OF_MONTH, numDias);
		return dataAtual.getTime();
	}

	/** 
	 * Metodo...: dataAcrescidaSegundos
	 * Descricao: Recebe o numero de segundos a ser adicionado a uma data
	 * 			  e retorna a data no formato yyyymmddhhmmss
	 * @param	String aDataHora	data/hora (yyyymmddhhmmss) a ser acrescida de segundos
	 * @param	int segundos		número de segundos a serem adicionados à data
	 * @return 	String	- Data no formado yyyymmddhhmmss
	 */	

	static public String dataAcrescidaSegundos(String aDataHora, int segundos)
	{
		String retorno = "";
		
		// Funcao que determina a data de expiracao
		GregorianCalendar auxData = new GregorianCalendar(); 
		
		// Seta auxData com data
		auxData.clear();
		auxData.set(Calendar.DAY_OF_MONTH,new Integer(aDataHora.substring(6,8)).intValue());
		auxData.set(Calendar.MONTH,new Integer(aDataHora.substring(4,6)).intValue()-1);
		auxData.set(Calendar.YEAR,new Integer(aDataHora.substring(0,4)).intValue());  
		auxData.set(Calendar.HOUR_OF_DAY,new Integer(aDataHora.substring(8,10)).intValue());
		auxData.set(Calendar.MINUTE,new Integer(aDataHora.substring(10,12)).intValue());
		auxData.set(Calendar.SECOND,new Integer(aDataHora.substring(12)).intValue());

		// Adiciona os dias de expiracao na data do sistema                  
		auxData.add( Calendar.SECOND, segundos );
					
		// Funcao de DateFormat - formatacao de datas
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		// Formata a String adequadamente
		retorno = df.format(auxData.getTime());
		
		return retorno;
	}

	/** 
	 * Metodo...: dataAcrescidaSegundos
	 * Descricao: Recebe o numero de segundos a ser adicionado a uma data
	 * 			  e retorna a data no formato dd/mm/yyyy hh:mm:ss
	 * @param	Date aDataHora	data/hora 
	 * @param	int segundos		número de segundos a serem adicionados à data
	 * @return 	String	- Data no formado dd/mm/yyyy HH:mm:ss
	 */	

	static public String dataAcrescidaSegundos(Timestamp _dataHora, int segundos)
	{
		String retorno = "";
		
		// Funcao que determina a data de expiracao
		GregorianCalendar auxData = new GregorianCalendar(); 
		
		// Seta auxData com data
		auxData.clear();
		auxData.setTimeInMillis(_dataHora.getTime());
		
		// Adiciona os dias de expiracao na data do sistema                  
		auxData.add( Calendar.SECOND, segundos );
					
		// Funcao de DateFormat - formatacao de datas
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Formata a String adequadamente
		retorno = df.format(auxData.getTime());
		
		return retorno;
	}
	
	/**  
	 * Metodo...: d1GreaterThanD2
	 * Descricao: Recebe duas datas no formato AAAAMMDD
	 * 			  e retorna true se d1>d2
	 * 			  ou retorna false se d1<=d2
	 * @param
	 * @return 	boolean -	True / False
	*/		
	static public boolean d1GreaterThanD2(String d1, String d2)
	{
		boolean retorno;
		
		if (new Long(d1).longValue() > new Long(d2).longValue()) 
			retorno = true;
		else
			retorno = false;

		return retorno;
	}	
	
	/** 
	 * Metodo...: numeroDiasAntesDe
	 * Descricao: Recebe uma data no formato AAAAMMDD e retorna o número de
	 * 			  dias entre hoje e a data fornecida como parâmetro
	 * @param
	 * @return 	short	- Número de dias
 	*/		
	public static short numeroDiasAntesDe(String dataFinal)
	{
		short retorno = 0;
		
		Calendar dataHoje = Calendar.getInstance();
		Calendar dataMaior = Calendar.getInstance();
		
		// Inicializando a Data de Expiração
		dataMaior.clear();
		dataMaior.set(Calendar.DAY_OF_MONTH,new Integer(dataFinal.substring(6)).intValue());
		dataMaior.set(Calendar.MONTH,new Integer(dataFinal.substring(4,6)).intValue()-1);
		dataMaior.set(Calendar.YEAR,new Integer(dataFinal.substring(0,4)).intValue());
		
		//Inicializando a Data de Hoje
		int diaHoje = dataHoje.get(Calendar.DAY_OF_MONTH);
		int mesHoje = dataHoje.get(Calendar.MONTH);
		int anoHoje = dataHoje.get(Calendar.YEAR);
				
		dataHoje.clear();
		dataHoje.set(Calendar.DAY_OF_MONTH,diaHoje);
		dataHoje.set(Calendar.MONTH,mesHoje);		// Janeiro = 0 na classe Calendar
		dataHoje.set(Calendar.YEAR,anoHoje);
		
		double Dretorno = dataMaior.getTimeInMillis() - dataHoje.getTimeInMillis();
	
		// Converte a diferença de datas de milisegundos para dias
		retorno = (short)(Dretorno/(1000*60*60*24));
	
		return retorno;
		
	}	

	/** 
	 * Metodo...: datahoraFormatada
	 * Descricao: Recebe a data no formato DD/MM/AAAA e retorna 
	 * 			  a data no formato DD/MM/AAAA HH:MI:SS
	 * @param
	 * @return 	String	- Data no formado DD/MM/AAAA HH:MI:SS
	 */	

	static public String datahoraFormatada(String data)
	{
		String retorno = "";
		
		String hora = GPPData.horaFormatada();

		retorno = data + " " + hora; 
		
		return retorno;
	}
	
	/**
	 * Metodo...: datahoraFormatada
	 * Descricao: Retorna um String correspondente ao objeto Date
	 * @param adata		- Objeto do tipo Date
	 * @return String 	- Data no formato DD/MM/AAAA HH24:MI:SS
	 */
	static public String DateToString(Date dData)
	{
		String stringData = null;
		Calendar cData = Calendar.getInstance();
		
		cData.setTime(dData);
		
		stringData = "" + cData.get(Calendar.DAY_OF_MONTH)+"/"+(cData.get(Calendar.MONTH)+1)+"/"+cData.get(Calendar.YEAR)+" "+
		cData.get(Calendar.HOUR_OF_DAY)+":"+cData.get(Calendar.MINUTE)+":"+cData.get(Calendar.SECOND);
					
		return formataDataHora(stringData);
	}

	/**
	 * Metodo...: datahoraFormatada
	 * Descricao: Recebe a data no formato yyyymmddhhmmss e retorna 
	 * 			  a data no formato dd/mm/yyyy hh:mm:ss
	 * @param adata		- Objeto do tipo Date
	 * @return String 	- Data no formato DD/MM/AAAA HH24:MI:SS
	 */
	static public String formataStringData(String stringData)
	{
		return 	stringData.substring(6,8)+ "/"+
				stringData.substring(4,6)+ "/"+
				stringData.substring(0,4)+ " "+
				stringData.substring(8,10) + ":"+
				stringData.substring(10,12) + ":"+
				stringData.substring(12);
		
	}	

	/**
	 * Metodo....: formataNumero
	 * Descricao.: Formato um numero em string preenchendo com zeros a esquerda deste
	 * @param numeroSemZeros - Numero que deseja formatar
	 * @param tamanho		 - Tamanho final da 
	 * @param tamanho		 - Tamanho final da string
	 * @return
	 */
	static public String formataNumero (String numeroSemZeros, int tamanho)
	{
		String retorno = numeroSemZeros;
		
		while(retorno.length() < tamanho)
		{
			retorno = "0" + retorno;
		}
		
		return retorno;
	}

	/**
	 * Metodo....: formataNumero
	 * Descricao.: Formato um numero em string preenchendo com zeros a esquerda deste
	 * @param n			- Numero que deseja formatar
	 * @param tamanho	- Tamanho final da string
	 * @return
	 */
	static public String formataNumero(int n, int tamanho)
	{
		String retorno = Integer.toString(n);
		
		while(retorno.length() < tamanho)
		{
			retorno = "0" + retorno;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: primeiroDiaMesSeguinte
	 * Descricao: Retorna uma string representando o primeiro dia do mês seguinte à data passada 
	 * 				como parâmetro no formato DD/MM/AAAA
	 * @param 	String 	data	Data referência	(DD/MM/YYYY)
	 * @return	String			Primeiro dia do mês seguinte no formato DD/MM/AAAA
	 */
	static public String primeiroDiaMesSeguinte(String aData) throws GPPInternalErrorException
	{
//		String retorno = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Inicializa calendário com o primeiro dia da data de referencia
		Calendar data = Calendar.getInstance();
		data.clear();
		
		try
		{
			data.setTime(sdf.parse(aData));
			
			// Desloca o calendário um mês para trás
			data.add(Calendar.MONTH,+1);
			
			// Seta pra o primeiro dia do mes
			data.set(Calendar.DAY_OF_MONTH, 1);
		}
		catch(ParseException pE)
		{
			throw new GPPInternalErrorException("Erro no Formato da Data: "+ pE);
		}

		return sdf.format(data.getTime());
	}
	
	/**
	 * Metodo...: primeiroDiaMesAnterior
	 * Descricao: Retorna uma string representando o primeiro dia do mês anterior à data passada 
	 * 				como parâmetro no formato DD/MM/AAAA
	 * @param 	String 	data	Data referência	(DD/MM/YYYY)
	 * @return	String			Primeiro dia do mês anterior no formato DD/MM/AAAA
	 */
	static public String primeiroDiaMesAnterior(String aData) throws GPPInternalErrorException
	{
//		String retorno = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Inicializa calendário com o primeiro dia da data de referencia
		Calendar data = Calendar.getInstance();
		data.clear();
		
		try
		{
			data.setTime(sdf.parse(aData));
			
			// Desloca o calendário um mês para trás
			data.add(Calendar.MONTH,-1);
			
			// Seta pra o primeiro dia do mes
			data.set(Calendar.DAY_OF_MONTH, 1);
		}
		catch(ParseException pE)
		{
			throw new GPPInternalErrorException("Erro de Parse: "+ pE);
		}

		return sdf.format(data.getTime());
	}
	
	/**
	 * Metodo...: primeiroDiaMes
	 * Descricao: Retorna uma string representando o primeiro dia do mês da data passada 
	 * 				como parâmetro no formato DD/MM/AAAA
	 * @param 	String 	data	Data referência	(DD/MM/YYYY)
	 * @return	String			Primeiro dia do mês anterior no formato DD/MM/AAAA
	 */
	static public String primeiroDiaMes(String aData) throws GPPInternalErrorException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Inicializa calendário com o primeiro dia da data de referencia
		Calendar data = Calendar.getInstance();
		data.clear();
		
		try
		{
			data.setTime(sdf.parse(aData));
			
			data.set(Calendar.DAY_OF_MONTH, 1);
			//data.set(Calendar.MONTH, new Integer(aData.substring(3,5)).intValue()-1);	// janeiro é o mês zero
			//data.set(Calendar.YEAR, new Integer(aData.substring(6)).intValue());
			
			return sdf.format(data.getTime());

			// Retorna a data do primeiro dia do mês anterior
			//retorno = data.get(Calendar.DAY_OF_MONTH) + "/" + data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR);					
		}
		catch (ParseException pE)
		{
			throw new GPPInternalErrorException("Erro de Parser: "+pE);
		}
	}
	
	/**
	 * Metodo...: getTDateTimeObject
	 * Descricao: Retorno um objeto TDateTime a partir de um String DD/MM/YYYY 
	 * @param 	String					aData		Data a ser convertida para Date
	 * @return	TINC.DateTime					Objeto Date referente ao String
	 */
	static public TDateTime getTDateTimeObject(String aData)
	{
		TDateTime retorno = null;
		if(aData != null)
		{
			// Inicializa objeto Date zerado
			retorno = new TDateTime();
			retorno.day = (new Short(aData.substring(0,2)).shortValue());
			retorno.month = (new Short(aData.substring(3,5)).shortValue());
			retorno.year = (new Short(aData.substring(6)).shortValue());
		}

		return retorno;
	}
	
	/**
	 * Metodo...: getDateTimeObject
	 * Descricao: Retorno um objeto DateTime a partir de um String DD/MM/YYYY 
	 * @param 	String					aData		Data a ser convertida para Date
	 * @return	DateTime					Objeto Date referente ao String
	 */
	/*
	static public DateTime getDateTimeObject(String aData)
	{
		DateTime retorno = null;
		if(aData != null)
		{
			// Inicializa objeto Date zerado
			retorno = new DateTime();
			retorno.day = (new Short(aData.substring(0,2)).shortValue());
			retorno.month = (new Short(aData.substring(3,5)).shortValue());
			retorno.year = (new Short(aData.substring(6)).shortValue());
			retorno.hour = 0;
			retorno.minute = 0;
			retorno.second = 0;
		}

		return retorno;
	}
	*/
	
	/**
	 * Metodo...: ultimoDiaDoMesAnterior
	 * Descricao: Dado o parametro aData (DD/MM/YYYY) esse método retorna o último dia do mês anterior a esse
	 * @param 	String	aData	data de referencia no formato DD/MM/YYYY
	 * @return	String			data referente ao último dia do mês anterior à data passada como parâmetro
	 */
	static public String ultimoDiaDoMesAnterior(String aData) throws GPPInternalErrorException
	{
		String retorno = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Inicializa Calendário
		Calendar data = Calendar.getInstance();
		data.clear();
		
		try	
		{
			// Seta o calendário com a data passada como parâmetro
			data.setTime(sdf.parse(aData));
			
			// Desloca o calendário um mês atrás
			data.roll(Calendar.MONTH,-1);
			
			// Determina o último dia do mês anterior
			data.set(Calendar.DAY_OF_MONTH, data.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			// Retorna um string com a data referente ao último dia do mês anterior 
			retorno = sdf.format(data.getTime());
		}
		catch(ParseException e)
		{
			throw new GPPInternalErrorException("Erro de Parser: "+e);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: ultimoDiaDoMes
	 * Descricao: Dado o parametro aData (DD/MM/YYYY) esse método retorna o último dia do mês
	 * @param 	String	aData	data de referencia no formato DD/MM/YYYY
	 * @return	String			data referente ao último dia do mês anterior à data passada como parâmetro
	 */
	static public String ultimoDiaDoMes(String aData) throws GPPInternalErrorException
	{
		String retorno = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Inicializa Calendário
		Calendar data = Calendar.getInstance();
		data.clear();
		
		try	
		{
			// Seta o calendário com a data passada como parâmetro
			data.setTime(sdf.parse(aData));
			
			// Determina o último dia do mês anterior
			data.set(Calendar.DAY_OF_MONTH, data.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			// Retorna um string com a data referente ao último dia do mês anterior 
			retorno = sdf.format(data.getTime());
		}
		catch(ParseException e)
		{
			throw new GPPInternalErrorException("Erro de Parser: "+e);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: acertaData
	 * Descricao: acerta o formato de um String Data para DD/MM/YYYY, 
	 * 				completando o dia e o mês com zeros à esquerda, se necessário.
	 * @param 	String		aData		Data com dia ou mês sem zero à esquerda
	 * @return	String					Data no formato dd/mm/yyyy
	 */
	public static String acertaData(String aData) throws ParseException
	{
		String retorno = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Inicializa Calendário
		Calendar data = Calendar.getInstance();
		data.clear();
		
		// Seta o calendário com a data passada como parâmetro
		data.setTime(sdf.parse(aData));
		
		// Retorna um string com a data referente ao último dia do mês anterior 
		retorno = sdf.format(data.getTime());

		return retorno;		
	}
	
	/**
	 * Metodo...: getDia28MesAnterior
	 * Descricao: Retorna uma string representando o dia 28 do mes anterior	como parâmetro no formato DD/MM/AAAA
	 * @param 	String 				data	Data referência	(DD/MM/YYYY)
	 * @return	java.sql.Timestamp			Dia 28 do mes anterior 
	 */
	static public java.sql.Timestamp getDia28MesAnterior(String aData) throws GPPInternalErrorException
	{
		java.sql.Timestamp retorno ;
		
		try
		{
			// Define mascara de data
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			// Faz o parse da data informada
			Date dataAux = df.parse(aData);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataAux) ;
			
			// Subtrai um mes a data informada
			cal.add(Calendar.MONTH, -1);
			
			// Seta o dia para 27
			cal.set(Calendar.DAY_OF_MONTH, 28);
			
			//retorno = df.format(cal.getTime());
			retorno = new java.sql.Timestamp(cal.getTimeInMillis());
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException("Erro: "+e);
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: getDia27MesAtual
	 * Descricao: Dado o parametro aData (DD/MM/YYYY) esse metodo retorna dia 28 do mes atual
	 * @param 	String				aData	data de referencia no formato DD/MM/YYYY
	 * @return	java.sql.Timestamp			Dia 28 do mes atual
	 */
	static public java.sql.Timestamp getDia28MesAtual(String aData) throws GPPInternalErrorException
	{
		java.sql.Timestamp retorno;
		
		try
		{
			// Define mascara de data
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			// Faz o parse da data informada
			Date dataAux = df.parse(aData);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataAux) ;
			
			// Seta o dia para 27
			cal.set(Calendar.DAY_OF_MONTH, 28);
			
			//retorno = df.format(cal.getTime());
			retorno = new java.sql.Timestamp(cal.getTimeInMillis());
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException("Erro: "+e);			
		}
		return retorno;
	}
	
	/**
	 * Metodo...: segundosParaHoras
	 * Descricao: Converte quantidade de segundos para hh:mm:ss
	 * @param 	numSegundos	- Numero de segundos a ser convertido
	 * @return	String		- Hora no formato HH:MM:SS
	 * @throws
	 */
	static public String segundosParaHoras(long numSegundos)
	{
		// Determina a quantidade de horas, minutos e segundos contidos no parâmetro numSegundos
		int Horas = (int)(numSegundos/(60*60));
		int Minutos = (int)((numSegundos%(60*60))/60);
		int Segundos = (int)(numSegundos%(60*60))%60;
		
		String sHoras = GPPData.formataNumero(Horas, 2);
		String sMinutos = GPPData.formataNumero(Minutos,2);
		String sSegundos = GPPData.formataNumero(Segundos,2);
		
		return sHoras + ":" + sMinutos + ":" + sSegundos;
	}	
	
	/**
	 * Metodo...: segundosParaHoras
	 * Descricao: Converte quantidade de segundos para hh:mm:ss
	 * @param 	numSegundos	- Numero de segundos a ser convertido
	 * @return	String		- Hora no formato HHHMMSS
	 * @throws
	 */
	static public String segundosParaHorasFormatado(long numSegundos)
	{
		// Determina a quantidade de horas, minutos e segundos contidos no parâmetro numSegundos
		int Horas = (int)(numSegundos/(60*60));
		int Minutos = (int)((numSegundos%(60*60))/60);
		int Segundos = (int)(numSegundos%(60*60))%60;
		
		String sHoras = GPPData.formataNumero(Horas, 3);
		String sMinutos = GPPData.formataNumero(Minutos,2);
		String sSegundos = GPPData.formataNumero(Segundos,2);
		
		return sHoras + sMinutos + sSegundos;
	}
	
	/**
	 * Metodo...: stringToTimestamp
	 * Descricao: Converte string dd/mm/yyyy para timestamp
	 * @param 	String	aData	Data no formato dd/mm/yyyy
	 * @return	Retorna timestamp referente à data
	 */
	static public java.sql.Timestamp stringToTimestamp(String aData) throws ParseException
	{
		// Define mascara de data
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		// Faz o parse da data informada
		Date dataAux = df.parse(aData);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataAux) ;

		return new java.sql.Timestamp(cal.getTimeInMillis());
	}
	
	/***
	 * Método...: getMaiorDasDatas
	 * Descricao: Retorna a maior das datas entre d1, d2, d3 e d4
	 * @param String	data1	formato dd/mm/yyyy
	 * @param String	data2	formato dd/mm/yyyy
	 * @param String	data3	formato dd/mm/yyyy
	 * @param String	data4	formato dd/mm/yyyy
	 * @return	String	A maior das datas (dd/mm/yyyy)
	 */
	static public String getMaiorDasDatas(String data1, String data2, String data3, String data4)
	{
		String retorno = null;
		
		// Insere as quatro datas num treeMap, tendo como chave yyyymmdd
		TreeMap tmDatas = new TreeMap();
		tmDatas.put(GPPData.mudaFormato(data1), data1);
		tmDatas.put(GPPData.mudaFormato(data2), data2);
		tmDatas.put(GPPData.mudaFormato(data3), data3);
		tmDatas.put(GPPData.mudaFormato(data4), data4);
		
		// Pega último valor (maior das datas)
		for(Iterator i = tmDatas.values().iterator();i.hasNext();)
		{
			retorno = (String) i.next();
		}

		return retorno; 
	}
	

	/**
	 * Metodo...: substituiTexto
	 * Descricao: Substitui as variaveis da msg de texto SMS
	 * @param 	var			variaveis do texto na tabela
	 * @param 	texto		texto na tabela
	 * @param 	novaString	valor da string que será substituida
	 * @return	String  	msg do SMS									
	 */
	static public String substituiTexto(String var, String texto, String novaString)
	{
		
		// Cria um padrão para as variaveis
		Pattern padrao = Pattern.compile(var);

		// o Matcher casa seqüências de caracteres com uma dada string
		Matcher m = padrao.matcher(texto);
	  
		StringBuffer sb = new StringBuffer();
	 
		// trocar as ocorrências das variaveis pela novaString
		while(m.find()) 
		{
		   m.appendReplacement(sb, novaString);
		}
		m.appendTail(sb);
	 
		return sb.toString();
	}
	
	/**
	 * Metodo....:retiraCodInterNacional
	 * Descricao.:Retira a informacao de codigo internacional (55) do numero do assinante caso
	 *            esse numero possua esta informacao, senao retorna o numero original
	 * @param msisdn - Numero do assinante a ser removido o codigo internacional
	 * @return String - Msisdn sem o codigo internacional
	 */
	public static String retiraCodInterNacional(String msisdn)
	{
		if (msisdn != null && msisdn.matches(Definicoes.MASCARA_COM_COD_INTERNACIONAL))
			return "0"+msisdn.substring(2);
		
		return msisdn;
	}
	
	/**
	 * Metodo....:insereCodInterNacional
	 * Descricao.:Insere a informacao de codigo internacional (55) no numero do assinante caso
	 *            esse numero nao possua esta informacao, senao retorna o numero original
	 * @param msisdn - Numero do assinante a ser inserido o codigo internacional
	 * @return String - Msisdn com o codigo internacional
	 */
	public static String insereCodInterNacional(String msisdn)
	{
		if (msisdn != null && msisdn.matches(Definicoes.MASCARA_SEM_COD_INTERNACIONAL))
			return "55"+Long.parseLong(msisdn);
		
		return msisdn;
	}
}