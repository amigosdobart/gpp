package br.com.brasiltelecom.wig.entity;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta classe representa a definicao de todos os campos de formulario e arquivo
 * que sao enviados ao servidor como conteudo mult-part. Este conteudo eh dividido para
 * facilitar a utilizacao e reaproveitamento
 * 
 * @author joao.lemgruber
 * @data   10/10/2006
 */
public class MultPartContent
{
	private Map camposFormulario;
	private BufferedReader streamArquivo;
	
	public MultPartContent()
	{
		camposFormulario = new HashMap();
	}
	
	/**
	 * Metodo....:addCampo
	 * Descricao.:Adiciona um campo de formulario ao mapeamento do MultPart
	 * @param nome	- Nome do campo de formulario
	 * @param valor - Valor do campo
	 */
	public void addCampo(String nome, String valor)
	{
		camposFormulario.put(nome, valor);
	}

	public BufferedReader getStreamArquivo()
	{
		return streamArquivo;
	}

	public void setStreamArquivo(BufferedReader streamArquivo)
	{
		this.streamArquivo = streamArquivo;
	}
	
	/**
	 * Metodo....:getValorCampo
	 * Descricao.:Retorna o valor do campo existente no mapeamento do MultPartContent
	 * @param nomeCampo - Nome do campo a ser pesquisado
	 * @return String   - Valor correspondente do campo de formulario informado
	 */
	public String getValorCampo(String nomeCampo)
	{
		return (String)camposFormulario.get(nomeCampo);
	}
}
