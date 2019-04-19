package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;

/**
  * Esta interface define dois metodos:
  * 1) Metodo para retorno dos comandos SQL a serem executados
  *    para processamento de uma linha de arquivo
  * 2) Metodo para retorno dos valores relacionados com os comandos
  *    SQL a serem vinculados para a execucao do comando.
  * 
  * OBS: Como sao metodos distintos e importante lembrar que a implementacao
  *      deve retornar em ambos os metodos o mesmo numero de elementos do array
  *      considerando sempre que o comando SQL no arrary em cada posicao
  *      corresponde na mesma posicao os argumentos no segundo array
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				18/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public interface ArquivoDados
{
	public void parse(String linhaArquivo);
	public String[] getComandosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException;
	public Object[][] getParametrosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException;
	public int getIdProcessoBatch();
	public boolean deveTotalizarParaPromocao();
	public TotalizacaoPulaPula getTotalizacaoPulaPula();
}
