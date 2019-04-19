package com.brt.gppLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GPPLogCache
{
	private File parentFile;					//Arquivo origem da pesquisa
	private File file;							//Arquivo associado
	private Vector lines;						//Dados da indexação
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private int queryHash = 0;					//Assinatura da Query
	private int size = 0; 						//Tamanho do resultado da pesquisa
	private Collection ultimaConsulta = null;	//Resultado da última consulta.
	
	public void setQueryHash(int hash)
	{
		queryHash = hash;
	}

	public boolean sameSearch(Map parametros) throws ParseException, NumberFormatException, IOException
	{
		return queryHash==(parametros.hashCode());
	}
	
	// Método faz leitura por bloco do arquivo atraves de memoria mapeada em memoria
	// Joga resultado da janela na collection.
	
	
	private static Collection loadBlock(GPPLogCache cache, File f, long inicio, long fim)
	{
		cache.ultimaConsulta = new ArrayList();
		try
		{
			Charset cs = Charset.forName("UTF-8"); 		//Codificação de Caracteres UNIX 
			CharsetDecoder decoder = cs.newDecoder(); 

			FileInputStream fis = new FileInputStream(f); 
			FileChannel fc = fis.getChannel(); 
			int fs = (int)fc.size();
			
			//fim = (fim+inicio)>fs?fs:fim; //Controle de saturação- Fim: segurança no acesso ByteMAP 
			fim = Math.min((long)fim,(long)fs-(long)inicio); //Controle de saturação- Fim: segurança no acesso ByteMAP 

			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, inicio, fim); 
			CharBuffer cb = decoder.decode(mbb); 
			cb.isReadOnly();

			String[] linhas = cb.toString().split("\n");
			fc.close();
			GPPLog auxLog = null;
			
			for (int l=0; l<linhas.length; l++)
			{
				GPPLog log = GPPLogParser.parseLinha(linhas[l]);
				if (log==null&&l>0&&auxLog!=null) //Caso seja uma linha de continuação de erro.
				{
					auxLog.setMensagem(auxLog.getMensagem()+GPPLogParser.getMensagem(linhas[l]));
				}
				else
				{
					if(log!=null) cache.ultimaConsulta.add( log  ) ; // Adiciona novo evento de log.
					auxLog = log;
				}
			}
		}
		catch (FileNotFoundException e)
		{
		} catch (IOException e)
		{
		}
		return cache.ultimaConsulta;
	}
	

	public static GPPLogCache buscaRegistros(Map parametros,File pFile, int window, int page) throws ParseException, NumberFormatException, IOException
	{
		long 	start 		= 0; 	//Onde vai começar o bloco de pesquisa.
		long 	end 		= 0; 	//Qtos bytes vão ser lidos a partir do start. 
		GPPLogCache ret		= null; //LogCache de retorno.
		
		// Volta um arquivo gerado como parâmetro ou o original se nao houve parâmetro.
		ret = pesquisa(parametros,pFile); 
		
		// ********************* Calculo do Seek dentro do Arquivo **********************************
		for(int l=0; l<(page-1)*window;l++)
			start = start+ ((Integer)ret.lines.elementAt(l)).intValue();
		for(int l=(page-1)*window;l<page*(window)&&l<ret.lines.size();l++)
			end  = end+((Integer)ret.lines.elementAt(l)).intValue();
		// ******************************************************************************************
		
		loadBlock(ret, ret.file, start, end); // Le bloco de dados da consulta
		
		return ret;
	}
	
	private static GPPLogCache pesquisa(Map parametros, File pFile) throws ParseException, NumberFormatException, IOException
	{
		GPPLogCache ret = null; //Collection de Retorno. O mesmo caso não haja mudança na pesquisa.
		
		// Se é uma pesquisa com parâmetros (assinatura diferente), gera outro arquivo de pesquisa;
		File file = GPPLogParser.parseFilter(pFile,parametros); 	//Gera outro arquivo de pesquisa

		ret = GPPLogCache.getCache(file); 				//Cria índice desse arquivo
		// Guarda assinatura da pesquisa
		ret.setQuery(parametros);
		// Set parent
		 ret.setParentFile(pFile);

		return ret;
	}

	// Destrutor que limpa o cache de arquivos caso arquivo de log temporário.
	protected void finalize()
	{
		if (file.getName().indexOf("tmp")>0) //Só deleta arquivo temporário de consulta, i.e. o nome termina com "tmp";
			file.delete();
	}
	
	// Retorna última consulta do sistema
	public Collection getUltimaConsulta()
	{
		return ultimaConsulta;
	}
	
	public static GPPLogCache getCache(String file) throws IOException
	{
		File pFile = new File(file);
		GPPLogCache cache =  GPPLogParser.parse(pFile);
		cache.setParentFile(pFile);
		return cache; 
	}
	

	public void nextPage(Map parametros, int window, int page) throws ParseException, NumberFormatException, IOException
	{
		long 	start 		= 0; 	//Onde vai começar o bloco de pesquisa.
		long 	end 		= 0; 	//Qtos bytes vão ser lidos a partir do start. 
		
		// Volta um arquivo gerado como parâmetro ou o original se nao houve parâmetro.
		
		// ********************* Calculo do Seek dentro do Arquivo **********************************
		for(int l=0; l<(page-1)*window;l++)
			start = start+ ((Integer)lines.elementAt(l)).intValue();
		for(int l=(page-1)*window;l<page*(window)&&l<lines.size();l++)
			end  = end+((Integer)lines.elementAt(l)).intValue();
		// ******************************************************************************************
		setQuery(parametros);
		
		loadBlock(this, this.file, start, end); // Le bloco de dados da consulta
		
	}
	
	
	public static GPPLogCache getCache(File file) throws IOException
	{
		return GPPLogParser.parse(file); 
	}
	/**
	 * @return Returns the totalLineNumber.
	 */
	public int getTotalLineNumber() {
		return size;
	}
	/**
	 * @return Returns the total.
	 */
	public int getTotal() {
		return size;
	}
	/**
	 * @return Returns the file.
	 */
	public  File getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public  void setFile(File file) {
		this.file = file;
	}

	public  void setQuery(Map parameters) {
		this.queryHash = parameters.hashCode();
	}

	public  void setParentFile(File file) {
		this.parentFile = file;
	}

	public  File getParentFile() {
		return this.parentFile;
	}

	public GPPLogCache(Collection l)
	{
		lines = (Vector)l;
		size = lines.size();
	}
	
	/*
	 * Metodo para realizar um "append" na mensagem do ultimo log no cache
	 * @param String mensagem que será concatenada à mensagem existente no log
	 */
	 public void appendMensagem(String msg, Collection block)
	 {
	 	GPPLog log = (GPPLog)((ArrayList)block).get(getTamanhoCache()-1);
	 	if (log!=null)
	 		appendMensagem(msg,log);
	 }
	/*
	 * Metodo para realizar um "append" na mensagem de um Log
	 * @param String mensagem que será concatenada à mensagem existente no log
	 * @param GPPLog log que será feito o append na mensagem 
	 */	
	 public void appendMensagem(String msg, GPPLog log)
	 {
	 	log.setMensagem(log.getMensagem()+" "+msg);
	 }
	 
	/*
	 * Metodo para retornar o tamanho (nro de objetos) no cache
	 * @return int numero de elementos no cache
	 */
	public int getTamanhoCache()
	{
		return size;
	}

}
