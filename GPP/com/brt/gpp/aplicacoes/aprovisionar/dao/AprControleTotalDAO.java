package com.brt.gpp.aplicacoes.aprovisionar.dao;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Interface de acesso a tabela TBL_APR_CONTROLE_TOTAL.
 * 
 * @author Vitor Murilo Dias
 * Criado em: 05/03/2008
 */
public class AprControleTotalDAO extends Aplicacoes
{
	/**
	 * Construtor da classe AprControleTotalDAO
	 * @param logId
	 */
	public AprControleTotalDAO(long logId) 
	{
		super(logId, "AprControleTotalDAO");
	}
	
	// SQL para apagar o MSISDN antigo da tabela TBL_APR_CONTROLE_TOTAL
	private String atualizaMSISDN = 
		"		UPDATE TBL_APR_CONTROLE_TOTAL		" +
		"          SET IDT_MSISDN = ?        		" +
		"		 WHERE IDT_MSISDN = ?        		" ;
	
	/**
	 * Método responsável por deletar o registro 
	 * do MSISDN antigo da table a TBL_APR_CONTROLE_TOTAL
	 * @param conexao
	 * @param msisdn
	 * @param idProcesso
	 */
	public void atualizarMSISDN (PREPConexao conexao, String numeroAntigo, String numeroNovo, long idProcesso)
	{
		Object[] numeros = new Object[2];
		numeros[0] = numeroNovo;
		numeros[1] = numeroAntigo;
		
		try 
		{
			conexao.executaPreparedQuery(atualizaMSISDN, numeros, idProcesso);
		}
		catch (GPPInternalErrorException e) 
		{
			super.log(Definicoes.ERRO, "deletaAntigoMSISDN", "Erro ao atualizar MSISDN " + numeroAntigo + " na TBL_APR_CONTROLE_TOTAL.");
		}
	}
	
}
