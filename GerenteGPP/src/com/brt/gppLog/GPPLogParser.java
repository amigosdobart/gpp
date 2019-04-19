package com.brt.gppLog;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;


public class GPPLogParser 
{
	private static final String TAG_SERVIDOR   = "<Servidor>";
	private static final String TAG_MENSAGEM   = "<Mensagem>";
	private static final String TAG_METODO     = "<ME>";
	private static final String TAG_CLASSE     = "<CL>";
	private static final String TAG_COMPONENTE = "<CN>";
	private static final String TAG_ID         = "<ID>";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	
	private static String[] loadBlock(File f, long inicio, long fim)
	{
		String[] linhas = null;
		try
		{
			Charset cs = Charset.forName("UTF-8"); 		//Codificação de Caracteres UNIX 
			CharsetDecoder decoder = cs.newDecoder(); 

			FileInputStream fis = new FileInputStream(f); 
			FileChannel fc = fis.getChannel(); 
			long fs = fc.size();
			if (inicio<=fs)
			{
				fim = Math.min((long)fim,(long)fs-(long)inicio); //Controle de saturação- Fim: segurança no acesso ByteMAP 
				
				MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, inicio, fim);
				CharBuffer cb = decoder.decode(mbb); 
				cb.isReadOnly();
	
				linhas = cb.toString().split("\n");
				fc.close();
			}
		}
		catch (FileNotFoundException e)
		{
		} catch (IOException e)
		{
		}
		return linhas;
	}
	
	/*
	 * Este metodo executa indexação do Arquivo
	 * @param File arquivo a ser feito o parse
	 */
	public static GPPLogCache parse(File aArquivo) throws IOException
	{
		Vector linhas = new Vector();

		long p=0L,window=10000L;

		String[] blockLines = loadBlock(aArquivo,p,window);
		while (blockLines!=null&&blockLines.length>0)
		{
			int lidas = blockLines.length;
			for (int l=0;l<lidas-1;l++) // Loop para indexação do Arquivo
			{
				GPPLog log = parseLinha(blockLines[l]);
				if (log != null)
					linhas.add(new Integer(blockLines[l].getBytes("UTF-8").length+1));
				else
					if (!blockLines[l].equals("")&&linhas.size()>0)
						linhas.set(linhas.size()-1,new Integer(((Integer)linhas.lastElement()).intValue()+blockLines[l].getBytes("UTF-8").length+1));
			}
			p = (p-blockLines[lidas-1].getBytes("UTF-8").length)+window;
			blockLines = loadBlock(aArquivo,p,window);
		}		
		GPPLogCache	logCache = new GPPLogCache(linhas); //Cria cache com novo indice
		logCache.setFile(aArquivo);						//Seta o arquivo de trabalho
		return logCache;
	}
	

	/*
	 * Este metodo executa o parse do arquivo com filtro e 
	 * @param File arquivo a ser feito o parse e parametros de pesquisa
	 * @return Arquivo que deve ser parseado.
	 */
	public static File parseFilter(File aArquivo, Map parametros) throws IOException
	{
		//BufferedReader 	buffer 	= new BufferedReader(new FileReader(aArquivo));
		File fileOut =  File.createTempFile(String.valueOf(System.currentTimeMillis()),".tmp");
		//PrintWriter 	writer 	= new PrintWriter(new FileWriter(fileOut));
		FileWriter writer = new FileWriter(fileOut);
		
		long p=0L,window=10000L;

		String[] blockLines = loadBlock(aArquivo,p,window);
		while (blockLines!=null&&blockLines.length>0)
		{
			StringBuffer sb = new StringBuffer();
			//String linha;
			int lidas = blockLines.length;
			for (int l=0;l<lidas-1;l++) // Loop para indexação do Arquivo
			//while ( (linha=buffer.readLine()) != null)
			{
				if (!blockLines[l].trim().equals(""))
				{
					GPPLog log = parseLinha(blockLines[l]);
					if(filter(log,parametros))
					{
		                //writer.println(blockLines[l]);
						sb.append(blockLines[l]+'\n');
					}
				}
			}
			p = (p-blockLines[lidas-1].getBytes("UTF-8").length)+window;
			blockLines = loadBlock(aArquivo,p,window);
			writer.write(sb.toString());
		}
		//buffer.close();
		writer.close();
		
		return fileOut;
	}
	
	
	// Crivo para seleção dos eventos de log de acordo com o critério depesquisa
	private static boolean filter(GPPLog log, Map parametros) 
	{
		boolean ret = false;
		if (log!=null)
		{
			try {
				long	idProcesso 	=((String)parametros.get("idProcesso")).equals("")?-999L:Long.parseLong((String)parametros.get("idProcesso"));
				String 	componente	=(String)parametros.get("componente");
				String 	classe		=(String)parametros.get("nomeClasse");
				String	severidade	=(String)parametros.get("severidade");
				String  mensagem	=(String)parametros.get("mensagem");
				Date 	dataInicial	=((String)parametros.get("dataInicial")).equals("")?null:sdf.parse((String)parametros.get("dataInicial"));
				Date 	dataFinal	=((String)parametros.get("dataFinal")).equals("")?null:sdf.parse((String)parametros.get("dataFinal"));
	
				ret = 	(idProcesso==-999	    ||log.getIdProcesso()==idProcesso) 						&&
						(componente.equals("")	||(log.getComponente()!=null&&log.getComponente().equals(componente)))	&&
						(classe.equals("")		||(log.getClasse()!=null&&log.getClasse().equals(classe)))		&&
						(severidade.equals("")	||(log.getSeveridade()!=null&&log.getSeveridade().equals(severidade)))	&&
						(mensagem.equals("")	||(log.getMensagem()!=null&&log.getMensagem().lastIndexOf(mensagem)>=0))		&&
						(dataInicial==null	||(log.getDataHoraRegistro()!=null&&log.getDataHoraRegistro().compareTo(dataInicial)>0)) 	&&
						(dataFinal==null	||(log.getDataHoraRegistro()!=null&&log.getDataHoraRegistro().compareTo(dataFinal)<0));
				
						
			} catch (NumberFormatException e) {
				ret = false;
			} catch (ParseException e) {
				ret = false;
			}
		}
		else ret = true;
		
		return ret;
	}
	
	
	/*
	 * Este Metodo executa o parse de uma linha do arquivo
	 * @param String linha do arquivo o qual será feito o parse para o objeto de Log
	 * @return GPPLog objeto contendo os valores da linha do arquivo
	 */
	public static GPPLog parseLinha(String aLinha)
	{
		GPPLog gppLog = null;
		
		Date date = getDataHoraRegistro(aLinha);
		if (date!=null)
		{
			gppLog = new GPPLog();
			gppLog.setIdProcesso(getIdProcesso(aLinha));
			gppLog.setDataHoraRegistro(date);
			gppLog.setMensagem(getMensagem(aLinha));
			gppLog.setMetodo(getMetodo(aLinha));
			gppLog.setClasse(getClasse(aLinha));
			gppLog.setComponente(getComponente(aLinha));
			gppLog.setSeveridade(getSeveridade(aLinha));
			gppLog.setNomeServidor(getServidor(aLinha));
		}
		return gppLog;
	}
	
	/*
	 * Metodo que valida se a string é uma data válida
	 * @param String data no formato dd/MM/yyyy HH:mm:ss
	 * @return boolean indica se a data é válida ou não
	 */
	private boolean isDataValida(String dataStr)
	{
		try
		{
			Date dataValida = sdf.parse(dataStr);
			return true;
		}
		catch(ParseException pe)
		{
			return false;
		}
	}
	
	/*
	 * Este método faz o parse para a propriedade IdProcesso
	 * @param String linha a ser analisada
	 * @return long id do processo
	 */
	private static long getIdProcesso(String aLinha)
	{
		int posInicial 	= aLinha.indexOf(GPPLogParser.TAG_ID) + 1;
		int posFinal 	= aLinha.indexOf("<",posInicial) - 1;
		
		if (posInicial > -1)
			try {
				return Long.parseLong(aLinha.substring(posInicial+GPPLogParser.TAG_ID.length(),posFinal).trim());
			} catch (NumberFormatException e) {
				return -1;
			} catch (StringIndexOutOfBoundsException e) {
				return -1;
			}
		else return -1;
	}
	
	/*
	 * Este método faz o parse para a propriedade DataHoraRegistro
	 * @param String linha a ser analisada
	 * @return Date data e hora do registro do log
	 */
	private static Date getDataHoraRegistro(String aLinha)
	{
		Date dataReg=null;		
		int posInicial = 0;
		int posFinal   = aLinha.indexOf("[") - 1;
		try
		{
			dataReg = sdf.parse(aLinha.substring(posInicial,posFinal).trim());
		}
		catch(ParseException pe)
		{
			dataReg = null; 
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
		return dataReg;
	}
	
	/*
	 * Este metodo faz o parse para a propriedade Mensagem
	 * @param String linha a ser analisada
	 * @return String mensagem escrita no log
	 */
	public static String getMensagem(String aLinha)
	{
		int posInicial = aLinha.indexOf(GPPLogParser.TAG_MENSAGEM);
		if (posInicial > -1)
			try {
				return aLinha.substring(posInicial+GPPLogParser.TAG_MENSAGEM.length()).trim();
			} catch (StringIndexOutOfBoundsException e) {
				return aLinha;
			}
		else return aLinha;
	}
	
	/*
	 * Este método faz o parse para a propriedade Metodo
	 * @param String linha a ser analisada
	 * @return String nome do método que escreveu o log
	 */
	private static String getMetodo(String aLinha)
	{
		int posInicial = aLinha.indexOf(GPPLogParser.TAG_METODO);
		int posFinal   = aLinha.indexOf(GPPLogParser.TAG_MENSAGEM) - 1;
		
		if (posInicial > -1)
			try {
				return aLinha.substring(posInicial+GPPLogParser.TAG_METODO.length(),posFinal).trim();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
		else return null;
	}

	/*
	 * Este método faz o parse para a propriedade Classe
	 * @param String linha a ser analisada
	 * @return String nome da classe que escreveu o log
	 */	
	private static String getClasse(String aLinha)
	{
		int posInicial = aLinha.indexOf(GPPLogParser.TAG_CLASSE);
		int posFinal   = aLinha.indexOf(GPPLogParser.TAG_METODO) - 1;
		
		if (posInicial > -1)
			try {
				return aLinha.substring(posInicial+GPPLogParser.TAG_CLASSE.length(),posFinal).trim();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
		else return null;
	}
	
	/*
	 * Este método faz o parse para a propriedade Componente
	 * @param String linha a ser analisada
	 * @return String nome do componente que escreveu o log
	 */	
	private static String getComponente(String aLinha)
	{
		int posInicial = aLinha.indexOf(GPPLogParser.TAG_COMPONENTE);
		int posFinal   = aLinha.indexOf(GPPLogParser.TAG_MENSAGEM) - 1;
		
		if (posInicial > -1)
			try {
				return aLinha.substring(posInicial+GPPLogParser.TAG_COMPONENTE.length(),posFinal).trim();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
		else return null;
	}
	
	/*
	 * Este método faz o parse para a propriedade Servidor
	 * @param String linha a ser analisada
	 * @return String identificacao do servidor do Log
	 */
	private static String getServidor(String aLinha)
	{
		int posInicial = aLinha.indexOf(GPPLogParser.TAG_SERVIDOR);
		int posFinal   = aLinha.indexOf(GPPLogParser.TAG_ID) - 1;
		
		if (posInicial > -1)
			try {
				return aLinha.substring(posInicial+GPPLogParser.TAG_SERVIDOR.length(),posFinal).trim();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
		else return null;
	}

	/*
	 * Este método faz o parse para a propriedade Severidade
	 * @param String linha a ser analisada
	 * @return String identificacao da severidade do Log
	 */
	private static String getSeveridade(String aLinha)
	{
		int posInicial = aLinha.indexOf("[");
		int posFinal   = aLinha.indexOf("]");
		
		if (posInicial > -1)
			try {
				return aLinha.substring(posInicial,posFinal+1).trim();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
		else return null;
	}
}
