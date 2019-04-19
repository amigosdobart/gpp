/*
 * Created on 31/01/2005
 */
package com.brt.gppEnviaMail;

/**
 * @author Daniel Ferreira
 * <P>	version 1.0
 * 
 * Esta classe contém as definicoes para a aplicacao de envio de email de resultado
 * de consultas ao banco de dados 
 *
 */
public class GPPEnviaMailDefinicoes 
{

	//Delimitadores
	public static final String DELIMITADOR_DEFAULT = ",";
	
	//Extensoes de Arquivos
	public static final String EXTENSAO_ARQUIVO_COMPACTADO = ".zip";
	public static final String EXTENSAO_DEFAULT_ARQUIVO    = ".htm";

	//Processador de Arquivos
	public static final String PROCESSADOR_DEFAULT_ARQUIVOS = "com.brt.gpp.gppEnviaMail.GerarResultadoHTML";
	
	//Variaveis de substituicao
	public static final String VARIAVEL_DATA = "data";
	public static final String CARACTER_DEFINIDOR_VARIAVEL = "%";
	public static final String CARACTER_DEFINIDOR_PARAM_INI = "(";
	public static final String CARACTER_DEFINIDOR_PARAM_FIM = ")";

	//Tipos de Texto
	public static final String TIPO_TEXTO_PLAIN = "text/plain";
	public static final String TIPO_TEXTO_HTML  = "text/html";
	 	
}
