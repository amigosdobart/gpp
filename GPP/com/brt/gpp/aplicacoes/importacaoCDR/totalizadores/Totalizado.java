package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;

/**
 * Esta interface define um objeto totalizado que serah utilizado para totalizacao
 * de dados provenientes do CDR. Cada thread de importacao eh mantido
 * um arquivo de totalizacao por assinante, ao final os dados totalizados do 
 * arquivo
 * sao persitidos em banco de dados
 */
public interface Totalizado 
{
	
	/**
	 * Metodo.....:possuiMesmoPeriodo
	 * Descricao.:Indica se o objeto totalizado possui o mesmo periodo de processamento
	 *            que o CDR que estah sendo processado
	 * @param arqCDR
	 * @return boolean
	 */
	public boolean possuiMesmoPeriodo(ArquivoCDR arqCDR);
}