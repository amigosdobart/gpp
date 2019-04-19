package com.brt.gpp.aplicacoes.enviarMensagemTangram.dao;

import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tangram.entidade.ConteudoMensagem;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Interface de acesso ao cadastro de ConteudoMensagem do Tangram.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/09/2007
 */
public class ConteudoMensagemDAO extends Aplicacoes
{
	public ConteudoMensagemDAO(long logId) 
	{
		//#################################
		super(logId, "Definicoes.CL_CONTEUDO_MENSAGEM_DAO");
	}
	
	/**
	 * Salva um ConteudoMensagem no banco de dados.
	 * 
	 * @param conexao PREPConexao
	 * @param conteudo ConteudoMensagem
	 * @param idRequisicao Chave para a Requisicao
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	public void incluirConteudo(PREPConexao conexao, ConteudoMensagem conteudo,
			int idRequisicao) throws GPPInternalErrorException, SQLException
	{
		// Grava o conteudo

		String sqlInsert =	"INSERT INTO 	" +
		"	TBL_TAN_CONTEUDO_MENSAGEM 		" +
        "	(ID_CONTEUDO,                   " +
        "    IND_BINARIO,					" +
        "    IND_TRUNCAMENTO,				" + 
        " 	 TEXTO_CONTEUDO,				" + 
        " 	 ID_REQUISICAO)					" + 
		"VALUES (TTCM_SEQ.NEXTVAL,?,?,?,?)  ";

		Object[] parametros =
		{
			(conteudo.getIndBinario() != null) ? (conteudo.getIndBinario().booleanValue() ? 
					new Integer(1) : new Integer(0)) : null,
			(conteudo.getIndTruncamento() != null) ? (conteudo.getIndTruncamento().booleanValue() ? 
					new Integer(1) : new Integer(0)) : null,	
			conteudo.getTextoConteudo(),
			new Integer(idRequisicao)
		};
		
		conexao.executaPreparedUpdate(sqlInsert, parametros, super.logId);
	}
}
