package com.brt.gpp.comum.interfaceEscrita;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.comum.StringFormat;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;

/**
 * Gerador de Arquivo de Interface da Brasil Telecom.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 03/10/2007
 */
public class ArquivoInterfaceEscrita implements InterfaceEscrita
{
	private ArquivoInterfaceConfig config;
	private ArquivoEscrita         arquivoEscrita;
	private int 				   totalRegistros;

	private static String HEADER   = "A";
	private static String CORPO    = "B";
	private static String TRAILER  = "Z";

	private static String ENTER    = "\n";

	public ArquivoInterfaceEscrita(ArquivoInterfaceConfig config)
	{
		this.config = config;
	}

	/**
	 * Abre e escreve o HEADER do arquivo
	 */
	public void abrir() throws Exception
	{
		arquivoEscrita = new ArquivoEscrita((String)config.getAtributo(ArquivoInterfaceConfig.PATH));
		StringBuffer sb = new StringBuffer(HEADER);
		String[] campos =  { Integer.toString(config.getCodRemessa()),
				 new SimpleDateFormat("yyyyMMdd").format(new Date()),
				 new DecimalFormat("000000").format(config.getArquivoInterface().getNumeroArquivo()),
				 new StringFormat("30L ").format(config.getArquivoInterface().getNomeInterface()),
				 new StringFormat("10L ").format(config.getSistema()) };

		if(config.isDelimitado())
		{
			sb.append(config.getDelimitador());
			gerarCamposDelimitados(sb, campos);
		}
		else
			for(int i = 0; i < campos.length; i++)
				sb.append(campos[i]);

		sb.append(ENTER);
		arquivoEscrita.escrever(sb.toString());
	}

	/**
	 * Escreve campos separados por um delimitador no CORPO do arquivo
	 */
	public synchronized void escrever(String[] campos) throws Exception
	{
		if(campos == null)
			return;

		StringBuffer sb = new StringBuffer(CORPO);

		if(config.isDelimitado())
			sb.append(config.getDelimitador());

		gerarCamposDelimitados(sb, campos);
		sb.append(ENTER);

		arquivoEscrita.escrever(sb.toString());
		//Incrementa o Total de Registros escritos caso nao aconteca nenhum erro
		totalRegistros++;
	}

	/**
	 * Armazena uma String de campos separados por um delimitador.<br>
	 *
	 * @param sb				StringBuffer onde sera armazenado a String
	 * @param campos			Campos a serem formatados
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

	/**
	 * Escreve o TRAILER e fecha arquivo
	 */
	public void fechar() throws Exception
	{
		StringBuffer sb = new StringBuffer(TRAILER);

		String[] campos =
			{	new StringFormat("54L ").format((String)config.getAtributo(ArquivoInterfaceConfig.OBSERVACAO)),
				new DecimalFormat("00000000").format(totalRegistros)	};

		if(config.isDelimitado())
		{
			sb.append(config.getDelimitador());
			gerarCamposDelimitados(sb, campos);
		}
		else
			for(int i = 0; i < campos.length; i++)
				sb.append(campos[i]);

		arquivoEscrita.escrever(sb.toString());

		arquivoEscrita.fechar();
	}
}
