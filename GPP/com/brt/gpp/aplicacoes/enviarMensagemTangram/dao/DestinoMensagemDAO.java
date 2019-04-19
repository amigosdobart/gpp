package com.brt.gpp.aplicacoes.enviarMensagemTangram.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tangram.entidade.DestinoMensagem;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Interface de acesso ao cadastro de DestinoMensagem do Tangram.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/09/2007
 */
public class DestinoMensagemDAO extends Aplicacoes
{
	public DestinoMensagemDAO(long logId) 
	{
		//#################################
		super(logId, "Definicoes.CL_DESTINO_MENSAGEM_DAO");
	}
	
	/**
	 * Salva um DestinoMensagem no banco de dados.
	 * 
	 * É gerado um insert para cada idMensagem.
	 * 
	 * @param conexao PREPConexao
	 * @param destino DestinoMensagem
	 * @param idRequisicao Chave para a Requisicao 
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	public void incluirDestino(PREPConexao conexao, DestinoMensagem destino,
			int idRequisicao) throws GPPInternalErrorException, SQLException
	{
		// Grava o Destino

		String sqlInsert =	"INSERT INTO 	" +
		"	TBL_TAN_DESTINO_MENSAGEM 		" +
        "	(ID_DESTINO_MENSAGEM,           " +
        "    IDT_MSISDN_DESTINO,			" +
        "    ID_MENSAGEM,					" + 
        " 	 ID_REQUISICAO)					" + 
		"VALUES (TTDM_SEQ.NEXTVAL,?,?,?)";

		Collection idsMensagem = destino.getIdsMensagem();
		
		if (idsMensagem != null)
		{
			for (Iterator it = idsMensagem.iterator(); it.hasNext();)
			{
				Object[] parametros =
				{
					destino.getIdtMsisdnDestino(),
					(String)it.next(),
					new Integer(idRequisicao)
				};
				
				conexao.executaPreparedUpdate(sqlInsert, parametros, super.logId);
			}
		}
		else
		{
			Object[] parametros = {	destino.getIdtMsisdnDestino(),	null, new Integer(idRequisicao)};
			conexao.executaPreparedUpdate(sqlInsert, parametros, super.logId);
		}
	}
}
