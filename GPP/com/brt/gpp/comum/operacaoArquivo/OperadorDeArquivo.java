package com.brt.gpp.comum.operacaoArquivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.brt.gpp.comum.Definicoes;

/**
 * @author Magno Batista Corrêa
 * @since 2006/06/28 yyy/mm/dd
 *  Esta classe é responsável por todas as operações direamente sobre arquivos.
 * 
 */
public class OperadorDeArquivo 
{
	public final static String ENTER = "\n";
	public final static String CODIFICACAO_PADRAO = "UTF-8";

	/**
	 * Copia todo um arquivo de um path para outro com a codificação padrão
	 * @param pathEntrada
	 * @param pathSaida
	 * @throws IOException
	 */
	public static void copiaArquivo(String pathEntrada, String pathSaida ) throws IOException
	{
		copiaArquivo(pathEntrada,OperadorDeArquivo.CODIFICACAO_PADRAO,
				pathSaida,OperadorDeArquivo.CODIFICACAO_PADRAO );
	}

	/**
	 * Copia todo um arquivo de um path para outro com a codificação dada
	 * @param pathEntrada
	 * @param codificacaoEntrada
	 * @param pathSaida
	 * @param codificacaoSaida
	 * @throws IOException
	 */
	public static void copiaArquivo(String pathEntrada,String codificacaoEntrada,
			String pathSaida,String codificacaoSaida ) throws IOException
	{
		File arquivoEntrada = new File(pathEntrada);
		File arquivoSaida = new File(pathSaida);
		copiaArquivo(arquivoEntrada, codificacaoEntrada,
					 arquivoSaida, codificacaoSaida);
	}

	/**
	 * Copia todo um arquivo de um path para outro com a codificação padrão
	 * @param arquivoEntrada
	 * @param arquivoSaida
	 * @throws IOException
	 */
	public static void copiaArquivo(File arquivoEntrada, File arquivoSaida ) throws IOException
	{
		copiaArquivo(arquivoEntrada,OperadorDeArquivo.CODIFICACAO_PADRAO,
				arquivoSaida,OperadorDeArquivo.CODIFICACAO_PADRAO );
	}
	/**
	 * Copia todo um arquivo de um path para outro com a codificação dada
	 * @param arquivoEntrada
	 * @param codificacaoEntrada
	 * @param arquivoSaida
	 * @param codificacaoSaida
	 * @throws IOException
	 */
	public static void copiaArquivo(File arquivoEntrada, String codificacaoEntrada,
								File arquivoSaida, String codificacaoSaida ) throws IOException
	{
		ArquivoLeitura arqEntrada = new ArquivoLeitura(arquivoEntrada, codificacaoEntrada);
		ArquivoEscrita arqSaida = new ArquivoEscrita(arquivoSaida, codificacaoSaida);
		copiaArquivo( arqEntrada, arqSaida );
	}
	
	/**
	 * Copia todo um arquivo para outro
	 * @param arqEntrada
	 * @param arqSaida
	 * @throws IOException
	 */
	public static void copiaArquivo(ArquivoLeitura arqEntrada, ArquivoEscrita arqSaida ) throws IOException
	{
		while (!arqEntrada.isEof()) 
		{
			String linha = arqEntrada.lerLinha();
			if(linha != null)
			{
				arqSaida.escrever(linha);
			}
		}
		arqEntrada.fechar();
		arqSaida.fechar();
	}

	/**
	 * Concatena uma série de arquivos na codificação padrão para um mesmo arquivo na codificação padrão.
	 * A ordem respeitada é a ordem na qual os arquivos foram colocados no array.
	 * @param arquivosEntrada
	 * @param arquivoSaida
	 * @throws IOException
	 */
	public static void concatenaArquivos(File[] arquivosEntrada, File arquivoSaida, boolean ignoraPrimeirasLinha, int numLinhasIgnoradas) throws IOException
	{
		concatenaArquivos(arquivosEntrada, OperadorDeArquivo.CODIFICACAO_PADRAO,
				arquivoSaida, OperadorDeArquivo.CODIFICACAO_PADRAO, ignoraPrimeirasLinha, numLinhasIgnoradas );

	}
	
	/**
	 * 
	 * @param arquivosEntrada
	 * @param codificacaoEntrada
	 * @param arquivoSaida
	 * @param codificacaoSaida
	 * @throws IOException
	 */
	public static void concatenaArquivos(File[] arquivosEntrada, String codificacaoEntrada,
			File arquivoSaida, String codificacaoSaida)
			throws IOException
	{
		concatenaArquivos(arquivosEntrada, codificacaoEntrada, arquivoSaida, codificacaoSaida, false, 0);

	}

	/**
	/**
	 * Concatena uma série de arquivos de uma mesma codificação para um mesmo arquivo.
	 * A ordem respeitada é a ordem na qual os arquivos foram colocados no array.
	 * @param arquivosEntrada
	 * @param codificacaoEntrada
	 * @param arquivoSaida
	 * @param codificacaoSaida
	 * @param ignoraPrimeirasLinha
	 * @param numLinhasIgnoradas
	 * @throws IOException
	 */
	public static void concatenaArquivos(File[] arquivosEntrada, String codificacaoEntrada,
			File arquivoSaida, String codificacaoSaida, boolean ignoraPrimeirasLinha, int numLinhasIgnoradas )
			throws IOException
	{
		ArquivoEscrita out = new ArquivoEscrita(arquivoSaida, codificacaoSaida);
		int size = arquivosEntrada.length;
		for(int i = 0; i < size; i++)
		{
			File arquivoEntrada = arquivosEntrada[i];
			ArquivoLeitura in = new ArquivoLeitura(arquivoEntrada, codificacaoEntrada);
			if(ignoraPrimeirasLinha)
			{
				int numLinhasIgnoradasAcc = 0;
				while ((!in.isEof()) && (numLinhasIgnoradas > numLinhasIgnoradasAcc)) 
				{
					in.lerLinha();
					numLinhasIgnoradasAcc ++;
					
				}
			}
			while (!in.isEof()) 
			{
				String linha = in.lerLinha();
				if(linha != null)
				{
					out.escrever(linha);
				}
			}
			in.fechar();
		}
		out.fechar();
	}
	
	/**
	 * 
	 * @param pathDiretorio
	 * @param mascara
	 * @param pathArquivoSaida
	 * @throws IOException
	 */
	public static void concatenaArquivos(String pathDiretorio, String mascara,
			String pathArquivoSaida) throws IOException
	{
		concatenaArquivos(pathDiretorio, mascara, pathArquivoSaida, false, 0);
	}
	/**
	 * 
	 * @param pathDiretorio
	 * @param mascara
	 * @param pathArquivoSaida
	 * @param ignoraPrimeirasLinha
	 * @param numLinhasIgnoradas
	 * @throws IOException
	 */
	public static void concatenaArquivos(String pathDiretorio, String mascara,
			String pathArquivoSaida, boolean ignoraPrimeirasLinha, int numLinhasIgnoradas) throws IOException
	{
			concatenaArquivos(pathDiretorio, mascara, OperadorDeArquivo.CODIFICACAO_PADRAO,
				pathArquivoSaida,  OperadorDeArquivo.CODIFICACAO_PADRAO, ignoraPrimeirasLinha, numLinhasIgnoradas);
	
	}
	/**
	 * Concatena os arquivos de um dado diretório de acordo com uma máscara em expressão regular
	 * @param pathDiretorio
	 * @param mascara
	 * @param codificacaoEntrada
	 * @param pathArquivoSaida
	 * @param codificacaoSaida
	 * @throws IOException
	 */
	public static void concatenaArquivos(String pathDiretorio, String mascara, String codificacaoEntrada,
			String pathArquivoSaida, String codificacaoSaida, boolean ignoraPrimeirasLinha, int numLinhasIgnoradas ) throws IOException
	{
		File diretorio = new File(pathDiretorio);
		File arquivoSaida = new File(pathArquivoSaida);
		concatenaArquivos( diretorio, mascara, codificacaoEntrada, arquivoSaida, codificacaoSaida, ignoraPrimeirasLinha, numLinhasIgnoradas );
	}
	
	/**
	 * 
	 * @param diretorio
	 * @param mascara
	 * @param arquivoSaida
	 * @throws IOException
	 */
	public static void concatenaArquivos(File diretorio, String mascara, File arquivoSaida, boolean ignoraPrimeirasLinha, int numLinhasIgnoradas)
						throws IOException
	{
		concatenaArquivos(diretorio, mascara, OperadorDeArquivo.CODIFICACAO_PADRAO,
				arquivoSaida,OperadorDeArquivo.CODIFICACAO_PADRAO, ignoraPrimeirasLinha, numLinhasIgnoradas);

	}
	/**
	 * Concatena os arquivos de um dado diretório de acordo com uma máscara em expressão regular
	 * @param diretorio
	 * @param mascara
	 * @param codificacaoEntrada
	 * @param arquivoSaida
	 * @param codificacaoSaida
	 * @throws IOException
	 */
	public static void concatenaArquivos(File diretorio, String mascara, String codificacaoEntrada,
			File arquivoSaida, String codificacaoSaida, boolean ignoraPrimeirasLinha, int numLinhasIgnoradas ) throws IOException
	{
		File[] arquivosEntrada = listaArquivosDiretorio(diretorio, mascara);
		concatenaArquivos(arquivosEntrada, codificacaoEntrada, arquivoSaida, codificacaoSaida, ignoraPrimeirasLinha, numLinhasIgnoradas);
	}

	/**
	 * Lê todos os nomes dos arquivos de um dado diretório de acordo com uma dada máscara em espressão regular
	 * @param path		Path do diretório a ser lido
	 * @param mascara	Máscara em espressão regular a ser obdecida
	 * @return
	 */
	public static String[] listaNomeArquivosDiretorio(String path, String mascara)
	{
	      File diretorio = new File(path);
	      return listaNomeArquivosDiretorio(diretorio, mascara);
	}

	/**
	 * Lê todos os nomes dos arquivos de um dado diretório de acordo com uma dada máscara em espressão regular
	 * @param diretorio
	 * @param mascara
	 * @return
	 */
	public static String[] listaNomeArquivosDiretorio(File diretorio, String mascara)
	{
	      String[] arquivos = null;
	      if ((diretorio != null)&& diretorio.exists() && diretorio.isDirectory())
	      {
	    	  arquivos = diretorio.list(new FiltroDeArquivo(mascara));
	      }
	      return arquivos;
	}

	/**
	 * Lê todos os arquivos de um dado diretório de acordo com uma dada máscara em espressão regular
	 * @param path		Path do diretório a ser lido
	 * @param mascara	Máscara em espressão regular a ser obdecida
	 * @return
	 */
	public static File[] listaArquivosDiretorio(String path, String mascara)
	{
	      File diretorio = new File(path);
	      return listaArquivosDiretorio(diretorio, mascara);
	}

	/**
	 * Lê todos os arquivos de um dado diretório de acordo com uma dada máscara em espressão regular
	 * @param diretorio
	 * @param mascara
	 * @return
	 */
	public static File[] listaArquivosDiretorio(File diretorio, String mascara)
	{
		File[] arquivos = null;
	    if ((diretorio != null) && diretorio.exists() && diretorio.isDirectory())
	    {
	    	arquivos = diretorio.listFiles(new FiltroDeArquivo(mascara));
	    }
	    return arquivos;
	}

	/**
	 * Lê um registro que está no formato delimitado.
	 * Mais rápido que o CSV, porém não dá suporte a múltiplas linhas nem ao fato do delimitador ser parte do dado
	 * @param arquivo
	 * @param delimitador
	 * @return
	 * @throws IOException
	 */
	public static String[] leRegistroDelimitado(ArquivoLeitura arquivo, String delimitador) throws IOException
	{
		String linha =arquivo.lerLinha();
		if (linha != null)
		{
			return linha.replaceAll("\n","").split(delimitador);
			
		}
		return null;
	}

	/**
	 *  Gera uma COlection de StringBuffers
	 * @param arquivo
	 * @return
	 * @throws IOException
	 */
	public static Collection leRegistroCSV(ArquivoLeitura arquivo) throws IOException
	{
		arquivo.getLineNumberReader();
		StringBuffer linha = null;
		ArrayList arrayColunas = null;
		if (!arquivo.isEof())
		{
			String linhaTmp = arquivo.lerLinha();
			if(linhaTmp != null)
			{
				linha  = new StringBuffer(linhaTmp);

				if (linha.length() > 0) 
				{
					StringTokenizer st = new StringTokenizer(linha.toString(), ";");
					arrayColunas = new ArrayList();
					while (st.hasMoreElements())
					{
						StringBuffer campo = new StringBuffer(st.nextToken());
						if (campo.charAt(0) == '"')
						{
							campo.replace(0, 1, ""); // Retrirando o aspas inicial
							boolean isFimTexto = false;
							while (!arquivo.isEof() && !isFimTexto)
							{										//Se a linha não tiver aspar de fila,
																	//coloca um null no fim do campo
								linha = new StringBuffer(arquivo.lerLinha());
								int posicaoAspas = linha.indexOf("\"");
								
								// tem que conferir se há duas aspas consecutivas
								// se houver, então é um aspas, senão, é o fim do campo
								if (posicaoAspas >= 0)
								{
									if (linha.indexOf("\"", posicaoAspas) >= 0)
									{
										isFimTexto = true;
									}
									linha.replace(posicaoAspas, posicaoAspas + 1, "");
								}
								campo.append(linha);
								campo.append(ENTER);
							}
						}
						arrayColunas.add(campo);
					}
				}
			}
		}
		return arrayColunas;
	}
	
	/**
	 * 
	 * @param arquivo
	 * @return
	 * @throws IOException
	 */
	public static Collection leArquivoCSV(ArquivoLeitura arquivo) throws IOException
	{
		ArrayList saida = new ArrayList(); 
		while(!arquivo.isEof())
		{
			saida.add(leRegistroCSV(arquivo));
		}
		return saida;
	}
	
	/**
	 * Escreve um registro delimitado no arquivo dado
	 * @param arquivo
	 * @param entrada
	 * @param delimitador
	 * @throws IOException
	 */
	public static synchronized void escreveRegistroDelimitado(ArquivoEscrita arquivo, String[] entrada, String delimitador) throws IOException
	{
		int size = entrada.length;
		for (int i = 0 ; i < size ; i++)
		{
			arquivo.escrever(entrada[i]);
			arquivo.escrever(delimitador);
		}
		arquivo.escrever(ENTER);
	}

	/**
	 * 
	 * @param pathEntrada			O path de um arquivo ou diretório a ser movido
	 * @param pathDiretorioDestino	O diretório para qual o arquivo ou diretório será movido
	 * @throws IOException
	 */
	public static void moverArquivo(String pathEntrada, String pathDiretorioDestino) throws IOException {
        File file = new File(pathEntrada);
        File dir = new File(pathDiretorioDestino);
        if(dir.isDirectory())
        {
	        // Movendo para o novo diretório
	        boolean status = file.renameTo(new File(dir, file.getName()));
	        if (!status) {
	            // Não foi movido com sucesso
	        	throw new IOException("Arquivo não movido com sucesso");
	        }
        }
        else
        {
        	throw new IOException("O path de destino deve representar um diretório");
        }
    }
	public static void moverArquivo(String path,String mascara, String pathDiretorioDestino) throws IOException
	{
		String[] arquivos = listaNomeArquivosDiretorio(path,mascara);
		int size = arquivos.length;
		for (int i = 0 ; i < size ; i ++)
		{
			moverArquivo(path+arquivos[i],pathDiretorioDestino);
		}
	}
	
	/**
	 * Compacta um arquivo para o diretório de saida
	 * @param path 	Endereço do arquivo
	 * @throws Exception
	 */
	public static void compactaArquivo(String pathEntrada, String pathDiretorioDestino) throws Exception
	{
	    byte[] buf = new byte[1024];
	    
	    try 
	    {
	        // Cria o arquivo ZIP
	        String outFilename = pathEntrada + Definicoes.EXTENSAO_ZIP;
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
	    
            FileInputStream in = new FileInputStream(pathEntrada);
    
            // Adiciona o ZIP ao output stream.
            File file = new File(pathEntrada);
            out.putNextEntry(new ZipEntry(file.getName()));
    
            // Transfere bytes para o aquivo ZIP
            int len;
            while ((len = in.read(buf)) > 0) 
            {
                out.write(buf, 0, len);
            }
    
            // Finaliza a Entrada
            out.closeEntry();
            in.close();
	    
	        // Finaliza o arquivo ZIP
	        out.close();
	        
	        // Move o arquivo para o path de SAIDA
	        OperadorDeArquivo.moverArquivo(outFilename,pathDiretorioDestino);
	    }
		catch (Exception ex) 
		{
			ex.printStackTrace();
			throw new Exception("O path de destino deve representar um diretório");
		}
	}
}
