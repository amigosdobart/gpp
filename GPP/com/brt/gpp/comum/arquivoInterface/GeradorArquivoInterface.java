package com.brt.gpp.comum.arquivoInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;
/**
 * Classe responsavel por Gerar Arquivos de Interface entre sistemas da Brasil Telecom.
 *
 * @author Leone Parise Vieira da Silva
 * @since  27/09/2007
 */
public class GeradorArquivoInterface
{
	// Tipo de Registro
	public static final char HEADER	 = 'A';
	public static final char CORPO	 = 'B';
	public static final char TRAILER = 'Z';
	// Caracter Especial
	private static final char ENTER  = '\n';

	public static int REMESSA       = 1;
	public static int RETORNO       = 2;

	private ArquivoEscrita arquivoEscrita;
	private int totalRegistros;

	public ArquivoEscrita getArquivo()
	{
		return this.arquivoEscrita;
	}
	public int getTotalRegistros()
	{
		return this.totalRegistros;
	}
	/**
	 * Instancia um novo GeradorArquivoInterface e abre o arquivo..
	 *
	 * @param path			Path do arquivo.
	 * @throws IOException
	 */
	public GeradorArquivoInterface(String path) throws IOException
	{
		arquivoEscrita = new ArquivoEscrita(path);
	}
	/**
	 * Escreve o Header.
	 *
	 * @param path				Path e nome do arquivo.
	 * @param codRemessa		Codigo da Remessa: 1 - Remessa, 2 Retorno
	 * @paran numeroArquivo		Numero sequencial identificador do arquivo
	 * @param interfaceBrT		Interface da Brasil Telecom
	 * @param sistema			Sistema de Origem (GPP)
	 * @throws IOException
	 * @throws GPPInternalErrorException
	 */
	public void abrirArquivo( int codRemessa, int numeroArquivo, String nomeInterface, String sistema) throws Exception
	{
		SimpleDateFormat data = new SimpleDateFormat("yyyyMMdd");

		String[] campos = {Integer.toString(codRemessa),
				data.format(new Date()),
				redimensionarString(Integer.toString(numeroArquivo), 6, '0', 'I'),
				redimensionarString(nomeInterface, 30, ' ', 'F'),
				redimensionarString(sistema, 10, ' ', 'F')};

		escreverLinha(HEADER, campos);
	}

	/**
	 * Escreve uma serie de parametros numa linha do arquivo divididos por <code>';'</code>.
	 * Se o Tipo de Registro for do tipo <code>CORPO</code> o atributo <code>totalRegistros</code>
	 * sera incrementado.
	 *
	 * @param tipo			Tipo de Registro a ser preenchido <code>'A'</code>(Header),
	 * 						<code>'B'</code>(Corpo) ou <code>'Z'</code>(Trailer).
	 * @param campos		Conjunto de Campos
	 * @throws IOException
	 */
	public void escreverLinha(char tipo, String[] campos) throws IOException
	{
		escreverLinha(tipo, campos, ';');
	}

	/**
	 * Escreve uma serie de campos numa linha divididos por um delimitador definido.
	 * Se o Tipo de Registro for do tipo <code>CORPO</code> o atributo <code>totalRegistros</code>
	 * sera incrementado.
	 *
	 * @param tipo			Tipo de Registro a ser preenchido <code>'A'</code>(Header),
	 * 						<code>'B'</code>(Corpo) ou <code>'Z'</code>(Trailer).
	 * @param params		Conjunto de Campos
	 * @param delimitador   Delimitador. Ex: <code>';'</code>, <code>','</code>, <code>':'</code>
	 * @throws IOException
	 */
	public void escreverLinha(char tipo, String[] campos, char delimitador) throws IOException
	{
		if(campos == null) campos = new String[1];
		StringBuffer sb = new StringBuffer();

		sb.append(tipo);
		sb.append(delimitador);

		sb.append(gerarCamposDelimitados(campos, delimitador));

		escreverLinha(sb.toString());

		if(tipo == CORPO)
			this.totalRegistros++;
	}

	/**
	 * Escreve uma string no arquivo e adiciona <code>'\n'</code> no fim da string.
	 *
	 * @param linha			String a ser escrita no arquivo.
	 * @throws IOException
	 */
	public synchronized void escreverLinha(String linha) throws IOException
	{
		if(linha == null) linha = "";
		StringBuffer sb = new StringBuffer(linha);
		sb.append(ENTER);
		arquivoEscrita.escrever(sb.toString());
	}

	/**
	 * Retorna uma String redimensionada preenchida com caracteres a direita(inicio) ou a esquerda(final).
	 * Caso o comprimento dado seja menor que o comprimento do texto, retorna uma string "recortada" no
	 * inicio ou no final de acordo com a <code>posicao</code>.
	 *
	 * @param str			String inicial
	 * @param comprimento	Tamanho final
	 * @param caracter		Caracter de preenchimento
	 * @param posicao		São aceitos os caracteres 'D', 'F', 'E', 'I', no qual:<br>
	 * 						'<b>D</b>' ou '<b>F</b>' Adciona caracteres a direita(final) da String<br>
	 * 						'<b>E</b>' ou '<b>I</b>' Adciona caracteres a esquerda(inicio) da String.
	 * @return				String preenchida e redimensionada.
	 */
	public static String redimensionarString(String str, int comprimento, char caracter, char posicao)
	{
		if(str == null) str = "";
		StringBuffer sb = new StringBuffer(str);

		if(comprimento <= 0) return sb.toString();

		int diferenca = comprimento - sb.length();
		posicao = Character.toUpperCase(posicao);

		if(diferenca < 0)
		{
			diferenca = Math.abs(diferenca);

			if(posicao == 'D' || posicao == 'F')
				sb.delete(sb.length()-diferenca, sb.length());
			else if(posicao == 'E' || posicao == 'I')
				sb.delete(0, diferenca);
		}
		else
		{
			for(int i = 0; i < diferenca; i++)
			{
				if(posicao == 'D' || posicao == 'F')
					sb.append(caracter);
				else if(posicao == 'E' || posicao == 'I')
					sb.insert(0, caracter);
				else
					break;
			}
		}

		return sb.toString();
	}

	/**
	 * Retorna uma String de campos separados por um delimitador.<br>
	 *
	 * @param campos			Campos a serem formatados
	 * @param delimitador   	Delimitador. Ex: <code>';'</code>, <code>','</code>, <code>':'</code>
	 * @return
	 */
	public static String gerarCamposDelimitados(String[] campos, char delimitador)
	{
		if(campos == null) campos = new String[1];
		StringBuffer sb = new StringBuffer();

		for(int i = 0; i < campos.length; i++){
			if(campos[i] == null) campos[i] = "";
			sb.append(campos[i]);
			sb.append(delimitador);
		}
		sb.deleteCharAt(sb.lastIndexOf(Character.toString(delimitador)));

		return sb.toString();
	}
	/**
	 * Fecha o arquivo e escreve o Trailer.
	 *
	 * @param  observacao Observação associada ao arquivo.
	 * @throws IOException
	 */
	public void fecharArquivo(String observacao) throws Exception
	{
		if(observacao == null) observacao = "";
		String[] campos = {Character.toString(TRAILER),
						   redimensionarString(observacao, 54, ' ', 'F'),
						   redimensionarString(Integer.toString(totalRegistros), 8, '0', 'I')};
		String linha = gerarCamposDelimitados(campos, ';');

		arquivoEscrita.escrever(linha);
		arquivoEscrita.fechar();
	}
}
