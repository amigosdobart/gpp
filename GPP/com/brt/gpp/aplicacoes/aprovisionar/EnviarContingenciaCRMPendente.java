package com.brt.gpp.aplicacoes.aprovisionar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Esta classe realiza o envio dos bloqueios/desbloqueios de servicos
 * ligados a contingencia CRM que estao pendentes de bloqueio. O Portal PPP
 * realiza o bloqueio on-line, ou seja, enviando os dados atraves do GPP para
 * o servidor socket que por sua vez envia os dados para o sistema de aprovisionamento.
 * Caso por algum motivo o GPP nao consiga enviar o XML entao o registro fica
 * na tabela com o status de bloqueio solicitado. Esta classe realiza a consulta de
 * tais registros e faz o envio novamente
 * 
 * <P> Versao:			1.0
 *
 * @Autor: 				Joao Carlos
 * Data: 				20/09/2004
 *
 * Modificado por:
 * Data:
 * Razao:
 *
 */

public class EnviarContingenciaCRMPendente extends Aplicacoes
{
	private GerentePoolBancoDados gerBancoDados;
	
	/**
	 * Metodo....:EnviarContingenciaCRMPendente
	 * Descricao.:Construtor da classe - Inicializacao de propriedades
	 * @param idProcesso	- Id do processo que esta iniciando a classe
	 */
	public EnviarContingenciaCRMPendente(long idProcesso)
	{
		super(idProcesso,Definicoes.CL_ENVIA_CONTINGENCIA_CRM_PENDENTE);
		gerBancoDados = GerentePoolBancoDados.getInstancia(super.getIdLog());
	}
	
	/**
	 * Metodo....:enviaContingenciaSolicitada
	 * Descricao.:Envia os dados de contingencia CRM pendentes de confirmacao
	 *            novamente para o servidor de aprovisionamento
	 * @throws GPPInternalErrorException
	 */
	public void enviaContingenciaSolicitada() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"enviaContingenciaSolicitada","Inicio");
		String dataInicial = GPPData.dataCompletaForamtada();
		
		// Busca referencia com a classe que realiza efetivamente a criacao
		// do XML e envia os dados para o servidor de aprovisionamento
		ContingenciaCRM contingencia = new ContingenciaCRM(super.getIdLog());
		// Realiza a consulta dos dados de contingencia desde que somente sejam
		// operacoes de bloqueio (parametro true).
		// Para cada elemento da consulta, utiliza-se a classe de envio dos dados
		// da contingencia para bloqueio de servicos
		Collection bloqueados = consultaContingenciaPendente(true);
		super.log(Definicoes.INFO,"enviaContingenciaSolicitada","Qtd. Registros BLOQUEIO:"+bloqueados.size());
		for (Iterator i=bloqueados.iterator(); i.hasNext();)
		{
			Object[] dadosConsulta = (Object[])i.next();
			String msisdn    = (String)dadosConsulta[0];
			Long   idOS      = (Long)dadosConsulta[1];
			String categoria = dadosConsulta[2] != null ? (String)dadosConsulta[2] : contingencia.getCategoriaAssinante(msisdn);

			super.log(Definicoes.DEBUG,"enviaContingenciaSolicitada","Bloqueio Assinante:"+msisdn+" NumOS:"+idOS+" Categ:"+categoria);
			contingencia.bloquearServicos( idOS.longValue(), msisdn, categoria );
		}
		
		// Apos processamento dos bloqueios realiza a mesma consulta de dados
		// da contingencia desque que agora os dados sejam a respeito de desbloqueio
		// (parametro false).
		// Para cada elemento da consulta, utiliza-se a classe de envio dos dados
		// da contingencia para desativacao hot-line
		Collection desbloqueados = consultaContingenciaPendente(false);
		super.log(Definicoes.INFO,"enviaContingenciaSolicitada","Qtd. Registros DESBLOQUEIO:"+desbloqueados.size());
		for (Iterator j=desbloqueados.iterator(); j.hasNext();)
		{
			Object[] dadosConsulta = (Object[])j.next();
			String msisdn     = (String)dadosConsulta[0];
			Long   idOS       = (Long)dadosConsulta[1];
			String categoria  = dadosConsulta[2] != null ? (String)dadosConsulta[2] : contingencia.getCategoriaAssinante(msisdn);
			String idOperacao = (String)dadosConsulta[3];

			// 06/10/2004
			// Alteracao temporaria. Para os servico de Nosso Celular o assinante 
			// e considerado como um PosPago senao todos serao considerados como PrePago
			if (idOperacao != null && idOperacao.equals(Definicoes.ID_OPERACAO_NOSSO_CELULAR))
				categoria = Definicoes.XML_OS_CATEGORIA_POSPAGO;
			else categoria = Definicoes.XML_OS_CATEGORIA_PREPAGO;

			super.log(Definicoes.DEBUG,"enviaContingenciaSolicitada","Desbl. Assinante:"+msisdn+" NumOS:"+idOS+" Categ:"+categoria);
			contingencia.desativarHotLine( idOS.longValue(), msisdn, categoria );
		}

		// Registra a execucao do processo na tabela de historico de processos batch
		String desHistorico = "Nro de registros bloqueados:"+bloqueados.size()+" Nro de registros desbloqueados:"+desbloqueados.size();
		String dataFinal = GPPData.dataCompletaForamtada();
		String dataProc  = GPPData.dataFormatada();
		super.gravaHistoricoProcessos(Definicoes.IND_ENVIA_CONTINGENCIA_CRM,dataInicial,dataFinal,Definicoes.TIPO_OPER_SUCESSO,desHistorico,dataProc);
		super.log(Definicoes.INFO,"enviaContingenciaSolicitada","Fim");
	}

	/**
	 * Metodo....:consultaContingenciaPendente
	 * Descricao.:Realiza a consulta dos registros da contingencia CRM que estao pendentes
	 *            de confirmacao
	 * 
	 * Obs: Os dados retornados dessa consulta sao armazenados em um objeto Collection porem
	 *      as informacoes de cada linha esta em um vetor de dados contendo as colunas
	 * @param bloqueio	- Identifica se busca os registros cujas operacoes sao de bloqueio. Em caso
	 *                    de falso entao busca operacoes que sao ligadas ao desbloqueio
	 * @return Collection - Colecao de vetores a serem enviados com os dados resultantes da pesquisa
	 * @throws GPPInternalErrorException
	 */
	private Collection consultaContingenciaPendente(boolean bloqueio) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"consultaContingenciaPendente","Inicio");
		PREPConexao prepConexao = null;
		Collection resultados = new LinkedList();
		try
		{
			prepConexao = gerBancoDados.getConexaoPREP(super.getIdLog());
			String sqlConsulta =  "SELECT C.IDT_MSISDN    AS IDT_MSISDN," +
							            " C.ID_ATIVIDADE  AS ID_ATIVIDADE," +
							            " P.IDT_CATEGORIA AS IDT_CATEGORIA," +
										   " C.ID_OPERACAO   AS ID_OPERACAO " +
									  "FROM TBL_EXT_OPERACOES        O, " +
									       "TBL_EXT_CONTINGENCIA_CRM C, " +
									       "TBL_GER_PLANO_PRECO      P " +
									 "WHERE P.IDT_PLANO_PRECO (+)  = C.IDT_PLANO_PRECO " +
									   "AND O.IND_BLOQUEIO         = ? " +
									   "AND O.ID_OPERACAO          = C.ID_OPERACAO " +
									   "AND C.IDT_STATUS_ATIVIDADE = ? " +
									  "ORDER BY C.DAT_ATIVIDADE ";
			Object params[] = {bloqueio ? new Integer(1) : new Integer(0), Definicoes.STATUS_BLOQUEIO_SOLICITADO};
			
			ResultSet rs = prepConexao.executaPreparedQuery(sqlConsulta,params,super.getIdLog());
			while (rs.next())
			{
				Object linha[] = {rs.getString("IDT_MSISDN"),
						          new Long(rs.getLong("ID_ATIVIDADE")),
								  rs.getString("IDT_CATEGORIA"),
								  rs.getString("ID_OPERACAO")};
				resultados.add(linha);
			}
		}
		catch(SQLException e)
		{
			super.log(Definicoes.WARN,"consultaContingenciaPendente","Erro SQL:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP:"+e.getMessage());
		}
		finally
		{
			gerBancoDados.liberaConexaoPREP(prepConexao,super.getIdLog());
		}
		super.log(Definicoes.INFO,"consultaContingenciaPendente","Fim");
		return resultados;
	}
}
