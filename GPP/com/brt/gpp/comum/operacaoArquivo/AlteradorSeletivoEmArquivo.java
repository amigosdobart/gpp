package com.brt.gpp.comum.operacaoArquivo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Esta classe altera as tags de um arquivo de entrada para os valores contidos em um outro
 * arquivo de tags, em formato CSV e com duas colunas, sendo a primeira com a tag
 * e o segundo com o valor da tag.
 * @author Magno Batista Corrêa
 * Esta classe não é segura a arquivos grandes. Nesses casos irá consumir muita memória
 *
 */
public class AlteradorSeletivoEmArquivo 
{
	public void executor(File arquivoCSV, String codificacaoCSV,
										File arquivoEntrada, String codificacaoEntrada,
										File arquivoSaida, String codificacaoSaida) throws IOException
	{
		ArquivoLeitura arqCSV = new ArquivoLeitura(arquivoCSV, codificacaoCSV); 
		ArquivoLeitura arqEntrada = new ArquivoLeitura(arquivoEntrada, codificacaoEntrada);
		ArquivoEscrita arqSaida = new ArquivoEscrita(arquivoSaida, codificacaoSaida);
		executor(arqCSV, arqEntrada, arqSaida);
	}
	
	/**
	 * 
	 * @param arqCSV
	 * @param arqEntrada
	 * @param arqSaida
	 * @throws IOException
	 */
	public void executor(ArquivoLeitura arqCSV, ArquivoLeitura arqEntrada, ArquivoEscrita arqSaida) throws IOException
	{
		ArrayList tags = (ArrayList)OperadorDeArquivo.leArquivoCSV(arqCSV);
		String entrada = arqEntrada.lerTexto();
		StringBuffer processando = new StringBuffer();
		int size = entrada.length();
		for(int i = 0; i < size; i++)
		{
			int posicaoMenorQ = entrada.indexOf("&lt;!", i);
			if (posicaoMenorQ < 0)
			{
				processando.append(entrada.substring(i, size));
				break;
			}
			processando.append(entrada.substring(i, posicaoMenorQ));
			i = posicaoMenorQ + 5;
			int posicaoMaiorQ = entrada.indexOf("&gt;", i);
			if (posicaoMaiorQ < 0)
			{
				processando.append(entrada.substring(i, size));
				break;
			}
			String tag = entrada.substring(i, posicaoMaiorQ);
			processando.append(getValTag(tag, tags));
			
			//Colocando o final do texto
			int j = posicaoMaiorQ+3;
			if(j > size)
			{
				processando.append(entrada.substring(i , size));
			}
			else
			{
				i = j;
			}
		}
		arqSaida.escrever(processando.toString());
		arqSaida.fechar();
	}
	
	/**
	 * 
	 * @param tag
	 * @param tags
	 * @return
	 */
	private String getValTag(String tag, ArrayList tags)
	{
		int numTags = tags.size();
		StringBuffer saida = new StringBuffer();
		boolean tagEncontrada = false;
		for(int i = 0; i < numTags ; i++)
		{
			ArrayList linha = (ArrayList)tags.get(i);
			if(tag.equals(((StringBuffer)linha.get(0)).toString()))
			{
				String valorTag = null;
				valorTag  = (linha.size() == 1 ? "" : ((StringBuffer)(linha.get(1))).toString());
				saida.append(valorTag);
				tagEncontrada = true;
				break;
			}
		}
		if(!tagEncontrada)
		{
			saida.append("&lt;!");
			saida.append(tag);
			saida.append("&gt;");
		}
		return saida.toString();
	}
}
