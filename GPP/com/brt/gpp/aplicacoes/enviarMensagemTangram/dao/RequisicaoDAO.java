package com.brt.gpp.aplicacoes.enviarMensagemTangram.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tangram.entidade.ConteudoMensagem;
import com.brt.gpp.comum.conexoes.tangram.entidade.DestinoMensagem;
import com.brt.gpp.comum.conexoes.tangram.entidade.ParametrosNotificacao;
import com.brt.gpp.comum.conexoes.tangram.entidade.Requisicao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Interface de acesso ao cadastro de Requisicao do Tangram.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/09/2007
 */
public class RequisicaoDAO extends Aplicacoes
{
	public RequisicaoDAO(long logId) 
	{
		//#################################
		super(logId, "Definicoes.CL_REQUISICAO_DAO");
	}
	
	/**
	 * Salva uma Requisicao no banco de dados.
	 * 
	 * @param conexao PREPConexao
	 * @param requisicao Requisicao
	 * @param proxyAtivo Indica se o proxy estava ativo no momento de envio dessa requisicao
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	public void incluiRequisicao(PREPConexao conexao, Requisicao requisicao,
			boolean proxyAtivo) throws GPPInternalErrorException, SQLException
	{
		// Obtém um ID para a requisicao
		
		String sqlSelect =	"SELECT TTRE_SEQ.nextval as num from dual";
		ResultSet result = conexao.executaPreparedQuery(sqlSelect, null, super.logId);
		result.next();
		int idRequisicao = result.getInt("num");
		
		// Grava a requisicao

		String sqlInsert =	"INSERT INTO 	" +
		"	TBL_TAN_REQUISICAO 				" +
        "	(ID_REQUISICAO,					" +
        "    ID_EMPRESA,					" + 
        " 	 ID_SERVICO,					" + 
        " 	 ID_CANAL,						" + 
        " 	 IDT_ORIGEM,					" + 
        " 	 URL_NOTIFICACAO,				" + 
        " 	 DAT_AGENDAMENTO,				" +
        "	 IND_AGENDAMENTO_REL,			" +
        "	 DAT_REQUISICAO,				" +
        "	 COD_RETORNO,					" +
        "	 APP_SPECIFIC,					" +
        "	 APP_REQUEST_ID,				" +
        "	 IND_PROXY_ATIVO)				" + 
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)	";

		ParametrosNotificacao params = requisicao.getParametrosNotificacao();
		
		Object[] parametros =
		{
			new Integer(idRequisicao),
			new Integer(requisicao.getIdEmpresa()),
			new Integer(requisicao.getIdServico()),
			requisicao.getIdCanal(),
			requisicao.getIdtOrigem(),
			(params != null) ? params.getEnderecoRetorno() : null,	
			(requisicao.getDataAgendamento() != null) ?	
					new java.sql.Timestamp(requisicao.getDataAgendamento().getTime()) : null,
			(requisicao.getIndAgendamentoRelativo() != null) ? 
					(requisicao.getIndAgendamentoRelativo().booleanValue() ? new Integer(1) : new Integer(0) ): null,
			(requisicao.getDataEnvioRequisicao() != null) ? 
					new java.sql.Timestamp(requisicao.getDataEnvioRequisicao().getTime()) : null,
			requisicao.getCodRetorno(),
			requisicao.getAppSpecific(),
			requisicao.getAppRequestId(),
			(proxyAtivo) ? new Integer(1) : new Integer(0)
		};
		
		conexao.executaPreparedUpdate(sqlInsert, parametros, super.logId);

		// Grava os conteudos de mensagem
		
		ConteudoMensagemDAO conteudoMensagemDAO = new ConteudoMensagemDAO(super.logId);
		Collection conteudos = requisicao.getConteudosMensagem();
		if (conteudos != null)
		{
			for (Iterator it = conteudos.iterator(); it.hasNext();)
			{
				ConteudoMensagem conteudo = (ConteudoMensagem)it.next();
				conteudoMensagemDAO.incluirConteudo(conexao, conteudo, idRequisicao);
			}
		}
		
		// Grava os destinos de mensagem
		
		DestinoMensagemDAO destinoMensagemDAO = new DestinoMensagemDAO(super.logId);
		Collection destinos = requisicao.getDestinosMensagem();
		if (destinos != null)
		{
			for (Iterator it = destinos.iterator(); it.hasNext();)
			{
				DestinoMensagem destino = (DestinoMensagem)it.next();
				destinoMensagemDAO.incluirDestino(conexao, destino, idRequisicao);
			}
		}
	}
}
