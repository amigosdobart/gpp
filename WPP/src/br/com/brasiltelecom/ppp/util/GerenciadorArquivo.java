package br.com.brasiltelecom.ppp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;


/**
 * 
 * Classe para tratamento de arquivos
 * 
 * @author Bernardo Vergne Dias
 * @since 18/12/2006
 */
public class GerenciadorArquivo 
{
	public static final String ORDENAR_NOME 		= "nome";
	public static final String ORDENAR_DATA_CRIACAO = "data";
	public static final String ORDENAR_TAMANHO 		= "tamanho";
	
	/**
	 * Método reponsável pelo upload de arquivo.
	 * 
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param formFieldName nome do campo no formulario
	 * @param nomeArquivo nome do arquivo a ser salvo no sistema de arquivos (quando <code>null</code> o nome
	 * do arquivo sera o mesmo do arquivo original do formulario)
	 * @param pastaDestino nome completo da pasta onde sera salvo o arquivo no sistema de arquivos
	 * @throws java.lang.Exception
	 */
	public static void upload(HttpServletRequest request, String formFieldName, String pastaDestino, String nomeArquivo) 
	throws Exception
	{
		
		HashMap params = MultipartParser.parseStream(request);
		
		if (params.get(formFieldName) != null)
		{
			FileItem arquivo = (FileItem)(params.get(formFieldName));
			upload(request, arquivo, pastaDestino, nomeArquivo);
		} 
		else 
		{
			if (request.getAttribute("mensagem") == null)
			{
				request.setAttribute("mensagem", "Erro ao enviar o arquivo especificado!");
			}
			throw new Exception("Erro ao enviar o arquivo especificado!");
		}
		
	}

	/**
	 * Método reponsável pelo upload de arquivo. Use-o dentro de um ActionPortal
	 * 
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param arquivo campo do formulario que contem o arquivo a ser salvo
	 * @param pastaDestino nome completo da pasta onde sera salvo o arquivo no sistema de arquivos
	 * @throws java.lang.Exception
	 */
	public static void upload(HttpServletRequest request, FileItem arquivo, String pastaDestino, String nomeArquivo) 
	throws Exception
	{
		
		try 
		{
		
			if (arquivo == null) 
			{
				request.setAttribute("mensagem", "Especifique um arquivo para upload!");
				throw new Exception();
			}
			
			if (arquivo.getSize() == 0) 
			{
				request.setAttribute("mensagem", "Especifique um arquivo (não vazio) para upload!");
				throw new Exception();
			}

			String mensagem = "";
			
			if (nomeArquivo == null) 
			{
				nomeArquivo = getFileName(arquivo.getName());
				mensagem = "Arquivo '" + getFileName(arquivo.getName())
		           + "' transferido com sucesso!";
			}
			else 
			{
				mensagem = "Arquivo transferido e salvo com o nome '" + nomeArquivo + "'";
			}

			// Le os dados do formulario e gera o arquivo
			byte[] dados = new byte[(int) (arquivo.getSize())];
			InputStream input = arquivo.getInputStream();

			FileOutputStream output = new FileOutputStream(new File(pastaDestino + java.io.File.separator + nomeArquivo));
			input.read(dados);
			output.write(dados);
			output.flush();
			input.close();
			output.close();

			request.setAttribute("mensagem", mensagem);

		} 
		catch (Exception e) 
		{
			if (request.getAttribute("mensagem") == null)
			{
				request.setAttribute("mensagem", "Erro ao enviar o arquivo especificado!");
			}
			throw new Exception("Erro ao enviar o arquivo especificado!");
		}
	}
	
	
	/**
	 * Método reponsável pelo download de arquivo.
	 * 
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param nomeArquivo nome do arquivo no sistema de arquivos
	 * @param pastaOrigem nome completo da pasta onde localiza-se o arquivo para download
	 * @throws java.lang.Exception
	 */
	public static void download(HttpServletResponse response, String pastaOrigem, String nomeArquivo)
	throws Exception 
	{
		download(response, pastaOrigem + java.io.File.separator + nomeArquivo);
	}
	
	/**
	 * Método reponsável pelo download de arquivo.
	 * 
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param urlOrigem enredeco completo do arquivo para download
	 * @throws java.lang.Exception
	 */
	public static void download(HttpServletResponse response, String urlOrigem)
	throws Exception 
	{
		
		// realiza o download
		try 
		{	
			File file = new File(urlOrigem);
			FileInputStream input = new FileInputStream(file);
			
			byte[] dados = new byte[(int)(file.length())];
			input.read(dados);
			input.close();
			
			response.setHeader("Content-Description", "File Transfer");
			response.setHeader("Content-Type", "application/force-download");
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		
			response.getOutputStream().write(dados);
			response.getOutputStream().flush();
		}
		catch (Exception e)
		{
			throw new Exception("Não foi possível ler o arquivo especificado.");
		}
	}
		
	/**
	 * Lista os arquivos, ordena e grava mensagem de erro no request (se for o caso).
	 * 
	 * @param dir 		Diretório
	 * @param request 	Parâmetro do tipo HttpServletRequest.
	 * @param ordem		Ordem (veja constantes na classe)
	 * @param ascendente	true|false
	 */
	public static ArrayList listaArquivos(String dir, HttpServletRequest request, String ordem, boolean ascendente) 
	throws Exception
	{
		ArrayList arquivos = GerenciadorArquivo.listaArquivos(dir, request);
		
		if (ordem.equals(ORDENAR_NOME))
			Collections.sort(arquivos, new ArquivoComparatorNome());
			
		if (ordem.equals(ORDENAR_DATA_CRIACAO))
			Collections.sort(arquivos, new ArquivoComparatorDataCriacao());
			
		if (ordem.equals(ORDENAR_TAMANHO))
			Collections.sort(arquivos, new ArquivoComparatorTamanho());
		
		if (!ascendente) Collections.reverse(arquivos);
		
		return arquivos;
	}
	
	/**
	 * Lista os arquivos e grava mensagem de erro no request (se for o caso).
	 * 
	 * Ordenação padrão: por nome de arquivo, ascendente
	 * 
	 * @param dir 		Diretório
	 * @param request 	Parâmetro do tipo HttpServletRequest.
	 */
	public static ArrayList listaArquivos(String dir, HttpServletRequest request) 
	throws Exception
	{
		try 
		{
			return listaArquivos(dir);
		} catch (Exception e) {
			request.setAttribute("mensagem", "Erro ao acessar o sistema de arquivos.");
			throw e;
		}
	}
	
	/**
	 * Lista os arquivos
	 * 
	 * Ordenação padrão: por nome de arquivo, ascendente
	 * 
	 * @param dir 	Diretório
	 */
	public static ArrayList listaArquivos(String dir) 
	throws Exception
	{
		try 
		{
			File[] dirList = (new File(dir)).listFiles();

			TreeSet lista = new TreeSet();
			for (int i = 0; i < dirList.length; i++) 
			{
				lista.add(new Arquivo(dirList[i].toURI()));
				dirList[i] = null;
			}
			
			return new ArrayList(lista);			
		} catch (Exception e) {
			throw new Exception("Erro ao listar os arquivos.");
		}
	}
	
	/**
	 * Método que extrai o nome do arquivo dado seu endereço completo.
	 * 
	 * @param url
	 *            Endereço completo do arquivo,
	 */
	public static String getFileName(String url) {
		int index = url.lastIndexOf("\\");
		if (index < 0)
			index = url.lastIndexOf("/");
		if (index < 0)
			return url;

		return url.substring(index + 1);
	}	
}
