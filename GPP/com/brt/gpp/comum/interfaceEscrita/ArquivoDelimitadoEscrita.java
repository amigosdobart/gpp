package com.brt.gpp.comum.interfaceEscrita;

import java.io.IOException;

import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;

/**
 * Gerador de Arquivo Delimitado
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 03/10/2007
 */
public class ArquivoDelimitadoEscrita implements InterfaceEscrita
{
	private ArquivoDelimitadoConfig config;
	private ArquivoEscrita          arquivoEscrita;

	private static char ENTER    = '\n';
	public ArquivoDelimitadoEscrita(ArquivoDelimitadoConfig config)
	{
		this.config = config;
	}

	public void abrir() throws IOException
	{
		arquivoEscrita = new ArquivoEscrita((String)config.getAtributo(ArquivoDelimitadoConfig.PATH));
		
		if(config.isMostraTitulo())
		{
			arquivoEscrita.escrever(config.getTitulo());
			arquivoEscrita.escrever(Character.toString(ENTER));
		}

		if(config.isMostraCabecalho())
		{
			StringBuffer sb = new StringBuffer();
			gerarCamposDelimitados(sb, config.getCabecalho());
			sb.append(ENTER);
			arquivoEscrita.escrever(sb.toString());
		}
	}

	public void escrever(String[] campos) throws IOException
	{
		if(campos == null)
			return;

		StringBuffer sb = new StringBuffer();
		gerarCamposDelimitados(sb, campos);
		sb.append(ENTER);
		arquivoEscrita.escrever(sb.toString());
	}

	/**
	 * Armazena uma String de campos separados por um delimitador.<br>
	 *
	 * @param sb				StringBuffer onde sera armazenado a String
	 * @param campos			Campos a serem formatados
	 * @param delimitador   	Delimitador. Ex: <code>';'</code>, <code>','</code>, <code>':'</code>
	 */
	private void gerarCamposDelimitados(StringBuffer sb, String[] campos)
	{
		if(campos == null)
			return;

		for(int i = 0; i < campos.length; i++)
		{
			if(campos[i] == null) campos[i] = "";
			sb.append(campos[i]);
			sb.append(config.getDelimitador());
		}
		
		sb.deleteCharAt(sb.lastIndexOf(config.getDelimitador()));
	}

	public void fechar() throws IOException
	{
		arquivoEscrita.fechar();
	}
}
