package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Esta interface define os metodos a serem implementados por qualquer classe que
 * precise ser um totalizador de informacoes de CDR. A classe totalizador irah ter 
 * a responsabilidade de fornecer um objeto totalizado, baseado nas informacoes do
 * arquivo de CDR e persitir tais informacoes apos a importacao de um arquivo
 */
public interface TotalizadorCDR 
{
	
	/**
	 * Metodo.....:deveTotalizar
	 * Descricao.:Informa se baseado nas informacoes do CDR se esse deve ou nao ser 
	 * totalizado
	 * @param arqCDR
	 * @return boolean
	 */
	public boolean deveTotalizar(ArquivoCDR arqCDR);
	
	/**
	 * Metodo.....:getTotalilzado
	 * Descricao.:Retorna o objeto totalizado com as informacoes de CDR passado como 
	 * parametro
	 * @param arqCDR
	 * @param totalizado
	 * @return com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado
	 */
	public Totalizado getTotalizado(ArquivoCDR arqCDR, Totalizado totalizado);
	
	/**
	 * Metodo.....:persisteTotalizado
	 * Descricao.:Este metodo realiza o trabalho de inserir um objeto totalizado no 
	 * 			  banco de dados
	 *            E de responsabilidade do totalizador a concorrencia e a 
	 * 			  atualizacao dos dados corretamento na tabela totalizada
	 * 
	 * OBS: Este metodo nao eh sincronizado. Caso seja necessario sincronizar a atualizacao
	 *      em banco de dados entao o metodo da implementacao deve realizar essa tarefa
	 * @param totalizado
	 * @return com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado
	 */
	public void persisteTotalizado(Totalizado totalizado, PREPConexao conexaoPrep) throws GPPInternalErrorException;
}
