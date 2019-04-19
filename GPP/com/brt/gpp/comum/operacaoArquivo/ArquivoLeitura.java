package com.brt.gpp.comum.operacaoArquivo;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author Magno Batista Corrêa
 * @since 2006/06/28 YYYY/MM/DD
 * Arquivo responsável por operações em arquivos de leitura.
 * A partir de um path do arquivo, esta classe gera um arquivo 
 * e disponibiliza uma stream para a leitura, montada na classe LineNumberReader.
 */
public class ArquivoLeitura
{
	private LineNumberReader lineNumberReader;
	private boolean eof = false;

	/**
	 * Classe que representa um arquivo de leitura,
	 *  construida a partir de um path e uma codificação de escrita desejada
	 * @param path
	 * @param codificacao
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public ArquivoLeitura(String path, String codificacao) throws FileNotFoundException, UnsupportedEncodingException 
	{
		abrir(path, codificacao);
	}
	/**
	 * Cria um novo arquivo de leitura com a codificação padrão.
	 * @param path	O caminho completo do arquivo de leitura a ser criado
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public ArquivoLeitura(String path) throws FileNotFoundException, UnsupportedEncodingException
	{
		abrir(path,OperadorDeArquivo.CODIFICACAO_PADRAO);
	}
	
	/**
	 * Classe que representa um arquivo de leitura,
	 *  construida a partir de um arquivo e uma codificação de escrita desejada
	 * @param arquivo
	 * @param codificacao
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public ArquivoLeitura(File arquivo, String codificacao) throws FileNotFoundException, UnsupportedEncodingException
	{
		abrir(arquivo, codificacao);
	}
	public ArquivoLeitura(File arquivo) throws FileNotFoundException, UnsupportedEncodingException
	{
		abrir(arquivo,OperadorDeArquivo.CODIFICACAO_PADRAO);
	}

	/**
	 * Abre um arquivo determinado pelo path.
	 * Se o arquivo existir, só abre, senão o cria.
	 * @param path				O Caminho do arquivo a ser aberto
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	private void abrir(String path, String codificacao) throws FileNotFoundException, UnsupportedEncodingException
	{
		File arquivo = new File(path);
		abrir(arquivo, codificacao);
	}
	
	/**
	 * Abre um arquivo determinado.
	 * Se o arquivo existir, só abre, senão o cria.
	 * @param path				O Caminho do arquivo a ser aberto
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	private void abrir(File arquivo, String codificacao) throws FileNotFoundException, UnsupportedEncodingException
	{
		FileInputStream fileInputStream;
		InputStreamReader inputStreamReader;

		// Abrindo o arquivo
			fileInputStream = new FileInputStream(arquivo);
			inputStreamReader = new InputStreamReader(fileInputStream, codificacao);
			this.lineNumberReader = new LineNumberReader(inputStreamReader);
	}
	
	/**
	 * 
	 * @return
	 */
	public LineNumberReader getLineNumberReader()
	{
		return this.lineNumberReader;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String lerLinha() throws IOException
	{
		String linha = null;
		if(lineNumberReader.ready())
		{
			linha = this.lineNumberReader.readLine() + OperadorDeArquivo.ENTER;
		}
		else
		{
			this.eof = true;
		}
		return linha;
	}
	
	/**
	 * Lê todo texto de um dado arquivo
	 * @return
	 * @throws IOException
	 */
	public String lerTexto() throws IOException
	{
		StringBuffer saida = new StringBuffer();
		while(!this.eof)
		{
			String linha = lerLinha();
			if(linha != null)
			{
				saida.append(linha);
			}
		}
		return saida.toString();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void fechar() throws IOException
	{
		this.lineNumberReader.close();
	}

	public boolean isEof()
	{
		//Reforço do EOF para o caso de utilizarem o lineNumberReader diretamente
		if(!eof)
		{
			try
			{
				eof = !this.lineNumberReader.ready();
			}
			catch (IOException e)
			{
				eof = false;
			}
		}
		return this.eof;
	}
}
