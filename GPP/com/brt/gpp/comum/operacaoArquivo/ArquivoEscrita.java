package com.brt.gpp.comum.operacaoArquivo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Magno Batista Corrêa
 * @since 2006/06/28 YYYY/MM/DD
 * Arquivo responsável por operações em arquivos de escrita.
 * A partir de um path do arquivo, esta classe gera um arquivo 
 * e disponibiliza uma interface para escrita.
 */
public class ArquivoEscrita 
{
	private OutputStreamWriter osw;
	private String path; 
	/**
	 * 
	 * @param path
	 * @param codificacao
	 * @throws IOException
	 */
	public ArquivoEscrita(String path, String codificacao) throws IOException 
	{
		this.path = path;
		abrir(path,codificacao);
	}
	public ArquivoEscrita(String path) throws IOException
	{
		this.path = path;
		abrir(path,OperadorDeArquivo.CODIFICACAO_PADRAO);
	}
	
	/**
	 * 
	 * @param arquivo
	 * @param codificacao
	 * @throws IOException
	 */
	public ArquivoEscrita(File arquivo, String codificacao) throws IOException 
	{
		this.path = arquivo.getPath();
		abrir(arquivo, codificacao);
	}
	public ArquivoEscrita(File arquivo) throws IOException
	{
		this.path = arquivo.getPath();
		abrir(arquivo,OperadorDeArquivo.CODIFICACAO_PADRAO);
	}
	
	/**
	 * Abre ou cria um arquivo determinado pelo path e retorna um um arquivo.
	 * Se o arquivo existir, só abre, senão o cria.
	 * @param path				O Caminho do arquivo a ser aberto
	 * @return File				O arquivo a ser usado
	 * @throws IOException 
	 */
	private void abrir(String path, String codificacao) throws IOException 
	{
		this.path = path;
		File arquivo = new File(path);
		abrir(arquivo, codificacao);
	}
	
	private void abrir(File arquivo, String codificacao) throws IOException 
	{
		this.path = arquivo.getPath();
		if(!arquivo.exists())
		{
				arquivo.createNewFile();
		}
		if(arquivo.exists())
		{
			osw = new OutputStreamWriter(new FileOutputStream(arquivo), codificacao);
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void fechar() throws IOException
	{
		osw.flush();
		osw.close();
	}
	
	/**
	 * Este mátodo é Tread safe para o caso de todas as treads referenciarem o mesmo ArquivoEscrita.
	 * Caso várias treads abram vários ArquivoEscrita, vai dar zica!
	 * Então, é recomendado abrir um ArquivoEscrita num master, e os escravos receberiam somente uma
	 * referência e não instanciariam novos ArquivoEscrita.
	 * @param texto
	 * @throws IOException
	 */
	public synchronized void escrever(String texto) throws IOException
	{
		osw.write(texto);
	}
	/**
	 * @return Retorna o path.
	 */
	public String getPath() {
		return path;
	}
}
