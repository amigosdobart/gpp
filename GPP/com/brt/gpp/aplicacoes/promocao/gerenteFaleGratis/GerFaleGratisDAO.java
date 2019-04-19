package com.brt.gpp.aplicacoes.promocao.gerenteFaleGratis;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.ConsumidorSMS;

/**
 * Classe responsavel por tratar o acesso aos dados referentes
 * a remocao/insercao de assinantes da Promocao Fale Gratis
 * 
 * @author João Paulo Galvagni
 * @since  31/05/2007
 * 
 * @modificacao Inclusao do metodo para duplicar o registro da Totalizacao FGN
 * 			    para o assinante que efetua alteracao de MSISDN, mantendo os
 * 			    dados da totalizacao coerentes com o novo MSISDN
 * @since  		19/10/2007
 */
public class GerFaleGratisDAO
{
	private long idProcesso = 0;
	
	// SQL para selecionar todos os assinantes que fazem aniversario no dia da execucao do processo
	// para posterior verificacao do limite de minutos (segundos) e troca do plano Fale Gratis para o
	// plano Pula-Pula vigente
	// 
	// Modificacao.: Inclusao da clausula do status da TBL_PRO_ASSINANTE.
	//				 Para o processo de remocao da promocao FaleGratis, eh feita a selecao apenas dos assinantes que estejam nos 
	//				 status 5 (Status Suspenso por Limite Alcancado) ou 6 (Status Suspenso por Limite Alcancado e Recarga Expirada)
	//				 da tabela TBL_PRO_STATUS_ASSINANTE
	// Data........: 11/07/2007
	private final String SQL_ASSINANTES_REMOCAO_FALEGRATIS = " SELECT p.IDT_MSISDN, p.NUM_DIA_EXECUCAO, p.PERIODO  			 						" +
															 " 	     ,fg.NUM_SEGUNDOS_TOTAL, p.IDT_PROMOCAO, fg.IDT_LIMITE							" +
															 "		 ,c.IDT_PLANO_PRECO AS IDT_PLANO_FALEGRATIS, fg.IDT_PLANO_PULAPULA				" +
															 "		 ,NULL AS TIP_ESPELHAMENTO, p.IDT_STATUS										" +
															 "   FROM (SELECT pr.IDT_MSISDN, pr.NUM_DIA_EXECUCAO, pr.IDT_PROMOCAO, pr.IDT_STATUS    " +
															 "               ,CASE                                                                  " +
															 "                   WHEN pr.NUM_DIA_EXECUCAO > TO_NUMBER(TO_CHAR(SYSDATE,'dd'))        " +
															 "                   THEN TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')                      " +
															 "                   ELSE TO_CHAR(SYSDATE,'YYYYMM')                                     " +
															 "                END AS PERIODO                                                        " +
															 "           FROM (SELECT pro.IDT_MSISDN                                                " +
															 "                       ,dia.NUM_DIA_EXECUCAO                                          " +
															 "                       ,pro.IDT_PROMOCAO												" +
															 "						 ,pro.IDT_STATUS                                              	" +
															 "                   FROM TBL_PRO_ASSINANTE pro,                                        " +
															 "                        TBL_PRO_DIA_EXECUCAO dia                                      " +
															 "                  WHERE pro.IDT_PROMOCAO = ?                                     		" +
															 "                    AND pro.IDT_PROMOCAO = dia.IDT_PROMOCAO                           " +
															 "                    AND dia.TIP_EXECUCAO = ?                           				" +
															 "					  AND pro.IDT_STATUS  IN (?,?,?)									" +
															 "                    AND TO_CHAR(pro.DAT_ENTRADA_PROMOCAO, 'DD') = dia.NUM_DIA_ENTRADA " +
															 "                ) pr                                                                  " +
															 "         ) p                                                                          " +
															 "       ,TBL_PRO_TOTALIZACAO_FALEGRATIS fg                                             " +
															 "		 ,TBL_APR_ASSINANTE c															" +
															 "  WHERE fg.DAT_MES    = p.PERIODO                                                     " +
															 "	  AND fg.IDT_MSISDN = c.IDT_MSISDN													" +
															 "    AND fg.IDT_MSISDN = p.IDT_MSISDN                                                  " +
															 "    AND fg.IDT_PLANO_PULAPULA IS NULL                                                 " ;
    
	// SQL para selecionar todos os assinantes que sao elegiveis para a troca da promocao
	// Pula-Pula para a promocao Fale Gratis, que haviam sido alterados anteriormente por
	// atingirem o Limite Maximo de minutos durante o periodo anterior
	// 
	// Modificacao.: Inclusao da clausula do status da TBL_PRO_ASSINANTE.
	//				 Para o processo de inclusao na promocao FaleGratis, eh feita a selecao apenas dos assinantes que estejam nos 
	//				 status 0 (Status OK) ou 4 (Status Recarga Expirada) da tabela TBL_PRO_STATUS_ASSINANTE
	// Data........: 11/07/2007
	private final String SQL_ASSINANTES_INSERCAO_FALEGRATIS = " SELECT p.IDT_MSISDN, p.NUM_DIA_EXECUCAO, p.PERIODO  						" +
															  "       ,fg.NUM_SEGUNDOS_TOTAL, p.IDT_PROMOCAO, fg.IDT_LIMITE 				" +
															  "       ,fg.IDT_PLANO_FALEGRATIS, fg.IDT_PLANO_PULAPULA						" +
															  "		  ,d.TIP_ESPELHAMENTO, p.IDT_STATUS										" +
															  "  FROM (SELECT pro.IDT_MSISDN 												" +
															  "              ,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM') AS PERIODO 			" +
															  "              ,dia.NUM_DIA_EXECUCAO  										" +
															  "              ,pro.IDT_PROMOCAO  											" +
															  "				 ,pro.IDT_STATUS												" +
															  "          FROM TBL_PRO_ASSINANTE pro,  										" +
															  "               TBL_PRO_DIA_EXECUCAO dia 										" +
															  "         WHERE pro.IDT_PROMOCAO 	   = ? 										" +
															  "           AND pro.IDT_PROMOCAO 	   = dia.IDT_PROMOCAO 						" +
															  "           AND dia.TIP_EXECUCAO 	   = ? 										" +
															  "           AND dia.NUM_DIA_EXECUCAO = ? 										" +
															  "			  AND pro.IDT_STATUS  IN (?,?)										" +
															  "           AND TO_CHAR(pro.DAT_ENTRADA_PROMOCAO, 'DD') = dia.NUM_DIA_ENTRADA " +
															  "        ) p 																	" +
															  "      ,TBL_PRO_TOTALIZACAO_FALEGRATIS fg  									" +
															  "      ,TBL_PRO_PROMOCAO d  													" +
															  " WHERE fg.DAT_MES     = p.PERIODO  											" +
															  "   AND fg.IDT_MSISDN  = p.IDT_MSISDN  										" +
															  "   AND p.IDT_PROMOCAO = d.IDT_PROMOCAO  										" ;
	
	// SQL utilizado para atualizar o registro da tabela de totalizacao
	// para os assinantes que tiverem seu plano alterado do Fale Gratis
	// para qualquer promocao Pula-Pula vigente
	private final String SQL_ATUALIZA_TROCA_PLANO_FALEGRATIS = "UPDATE TBL_PRO_TOTALIZACAO_FALEGRATIS " +
							    							   "   SET IDT_PLANO_FALEGRATIS = ? 	  " +
							    							   "   	  ,IDT_PLANO_PULAPULA   = ? 	  " +
							    							   "	  ,IDT_LIMITE			= ?	  	  " +
							    							   "	  ,DAT_RETIRADA_FGN		= SYSDATE " +
							    							   " WHERE IDT_MSISDN 		    = ?		  " +
							    							   "   AND DAT_MES 				= ?		  " ;
	
	// SQL utilizado para atualizar o identificador do SMS enviado ao
	// assinante por atingir algum limite estabelecido pra sua promocao
	// Fale Gratis (limite Parcial ou Maximo)
	private final String SQL_ATUALIZA_ID_SMS_ENVIADO = "UPDATE TBL_PRO_TOTALIZACAO_FALEGRATIS " +
															  "   SET IDT_LIMITE = ? 		  " +
															  " WHERE IDT_MSISDN = ?		  " +
															  "   AND DAT_MES    = ?		  " ;
	
	// SQL utilizado para atualizar a tabela TBL_PRO_TOTALIZACAO_FALEGRATIS, 
	// setando o campo DAT_RETORNO_FGN com a data atual 
	private final String SQL_ATUALIZA_RETORNO_PLANO_FALEGRATIS = "UPDATE TBL_PRO_TOTALIZACAO_FALEGRATIS fg " +
																 "   SET fg.DAT_RETORNO_FGN = SYSDATE	   " +
																 " WHERE fg.IDT_MSISDN = ?				   " +
																 "   AND fg.DAT_MES    = ?				   " ;
	
	// SQL utilizado para inserir as informacoes da totalizacao do Fale de Graca a Noite para 
	// assinantes que realizaram a troca do MSISDN. Essa insercao serve para manter a totalizacao
	// do assinante, o que evita a fraude e faz com que o retorno para o FGN seja realizado de forma correta
	// Data........: 19/10/2007
	private final String SQL_INSERCAO_APOS_TROCA_MSISDN = "INSERT INTO tbl_pro_totalizacao_falegratis										" +
														  "       (DAT_MES, IDT_MSISDN, NUM_SEGUNDOS_TOTAL	                                " +
														  "       ,IDT_PLANO_FALEGRATIS, IDT_PLANO_PULAPULA                                 " +
														  "       ,IDT_LIMITE, DAT_RETIRADA_FGN, DAT_RETORNO_FGN)                           " +
														  "SELECT fg.dat_mes, ?, fg.num_segundos_total                                      " +
														  "      ,fg.idt_plano_falegratis, fg.idt_plano_pulapula                            " +
														  "      ,fg.idt_limite, fg.dat_retirada_fgn, fg.dat_retorno_fgn                    " +
														  "  FROM (SELECT pro.idt_msisdn                                                    " +
														  "              ,CASE                                                              " +
														  "                   WHEN dia.NUM_DIA_EXECUCAO > TO_NUMBER(TO_CHAR(SYSDATE,'dd'))  " +
														  "                   THEN TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')                 " +
														  "                   ELSE TO_CHAR(SYSDATE,'YYYYMM')                                " +
														  "               END AS periodo                                                    " +
														  "              ,dia.num_dia_execucao                                              " +
														  "              ,pro.idt_promocao                                                  " +
														  "              ,pro.idt_status                                                    " +
														  "          FROM tbl_pro_assinante pro                                             " +
														  "              ,tbl_pro_dia_execucao dia                                          " +
														  "         WHERE pro.IDT_MSISDN = ?                                                " +
														  "           AND pro.idt_promocao = dia.idt_promocao                               " +
														  "           AND dia.tip_execucao = 'FALEGRATIS'                                   " +
														  "           AND TO_CHAR (pro.dat_entrada_promocao, 'DD') = dia.num_dia_entrada) p," +
														  "       tbl_pro_totalizacao_falegratis fg                                         " +
														  " WHERE fg.dat_mes = p.periodo                                                    " +
														  "   AND fg.idt_msisdn = p.idt_msisdn                                              " ;
	
	/**
	 * Construtor da classe
	 * 
	 * @param novoIdProcesso
	 */
	public GerFaleGratisDAO(long novoIdProcesso)
	{
		idProcesso = novoIdProcesso;
	}
	
	/**
	 * Descricao.: Retorna o objeto (vo) com os valores do Banco
	 * 
	 * @param  rs	- Resultado da consulta
	 * @return vo	- Objeto devidamente populado
	 * @throws SQLException
	 */
	public GerFaleGratisVO getGerFaleGratisVO(ResultSet rs) throws SQLException
	{
		GerFaleGratisVO vo = null;
		
		// Valida se ainda ha registros
		if (rs.next())
		{
			vo = new GerFaleGratisVO();
			
			vo.setMsisdn(rs.getString("IDT_MSISDN"));
			vo.setDiaEntradaPromocao(rs.getInt("NUM_DIA_EXECUCAO"));
			vo.setPeriodo(rs.getString("PERIODO"));
			vo.setNumSegundosTotalizados(rs.getLong("NUM_SEGUNDOS_TOTAL"));
			vo.setIdPromocao(rs.getInt("IDT_PROMOCAO"));
			vo.setIdLimiteSMS(rs.getInt("IDT_LIMITE"));
			vo.setIdPlanoFaleGratis(rs.getInt("IDT_PLANO_FALEGRATIS"));
			vo.setIdPlanoPulaPula(rs.getInt("IDT_PLANO_PULAPULA"));
			vo.setTipoEspelhamento(rs.getString("TIP_ESPELHAMENTO"));
			
			// Inclusao do status da promocao do assinante, para controle
			// entre a retirada/insercao da promocao Fale Gratis, o status
			// da regua pre-pago e o limite alcancado
			vo.setStatusPromocao(rs.getInt("IDT_STATUS"));
		}
		
		// Retorna o objeto devidamente populado
		return vo;
	}
	
	/**
	 * Descricao.: Retorna os assinantes que sao elegiveis a mudanca
	 * 			   do Plano Fale Gratis para o Plano Pula-Pula
	 * 
	 * @param  idPlano		- Id do plano Fale Gratis
	 * @param  conexaoPrep	- Conexao com o Banco de Dados
	 * @return rs			- ResultSet contendo os registros
	 * @throws GPPInternalErrorException
	 */
	public ResultSet getAssinantesParaRemocaoFaleGratis(int idPlano, PREPConexao conexaoPrep)
					 throws GPPInternalErrorException
	{
		try
		{
			// Cria os parametros para a selecao
			Object parametros[] = {new Integer(idPlano)
								  ,Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_FALEGRATIS
								  ,new Integer(PromocaoStatusAssinante.STATUS_ATIVO)
								  ,new Integer(PromocaoStatusAssinante.STATUS_SUSPENSO_RECARGA_EXPIRADA)
								  ,new Integer(PromocaoStatusAssinante.STATUS_PENDENTE_RECARGA) };
			
			// Monta o ResultSet a ser consumido posteriormente
			return conexaoPrep.executaPreparedQuery(SQL_ASSINANTES_REMOCAO_FALEGRATIS,
													parametros,
													idProcesso);
		}
		catch (GPPInternalErrorException e)
		{
			throw e;
		}
	}
	
	/**
	 * Descricao.: Retorna os assinantes elegiveis para retornar ao
	 * 			   Plano Fale Gratis, pois completaram aniversario no
	 * 			   dia da execucao do processo
	 * 
	 * @param  idPlano		- Id do plano Fale Gratis
	 * @param  diaExecucao  - Dia de execucao dos assinantes
	 * @param  conexaoPrep	- Conexao com o Banco de Dados
	 * @return rs			- ResultSet contendo os registros
	 * @throws GPPInternalErrorException
	 */
	public ResultSet getAssinantesParaInsercaoFaleGratis(int idPlano, int diaExecucao, PREPConexao conexaoPrep)
					 throws GPPInternalErrorException
	{
		try
		{
			// Cria os parametros para a selecao
			Object parametros[] = {new Integer(idPlano)
								  ,Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_FALEGRATIS
								  ,new Integer(diaExecucao)
								  ,new Integer(PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO)
								  ,new Integer(PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA) };
			
			// Monta o ResultSet a ser consumido posteriormente
			return conexaoPrep.executaPreparedQuery(SQL_ASSINANTES_INSERCAO_FALEGRATIS,
													parametros,
													idProcesso);
		}
		catch (GPPInternalErrorException e)
		{
			throw e;
		}
	}
	
	/**
	 * Descricao.: Solicita a gravacao dos dados de SMS em Banco
	 * 
	 * @param msisdn	- Numero do assinante
	 * @param mensagem	- Mensagem de texto
	 * @param tipo		- Tipo do SMS (Limite Maximo ou Parcial)
	 * @throws GPPInternalErrorException
	 */
	public void gravaSMSLimiteUltrapassado(String msisdn, String mensagem, String tipo) throws GPPInternalErrorException
	{
		// Seleciona a instancia da Classe responsavel pelo envio de SMS
		ConsumidorSMS consSMS = ConsumidorSMS.getInstance(idProcesso);
		
		// Realiza a gravacao dos dados do SMS na tabela do Banco de Dados
		// para posterior envio ao assinante
		consSMS.gravaMensagemSMS(msisdn, mensagem, Definicoes.SMS_PRIORIDADE_ZERO, tipo, idProcesso );
	}
	
	/**
	 * Descricao.: Realiza a atualizacao na tabela de totalizacao de chamadas,
	 * 			   inserindo os dados da promocao FaleGratis original do assinante
	 * 			   e a promocao a qual o mesmo foi migrado
	 * 
	 * @param  vo			- Objeto contendo os dados para atualizacao
	 * @param  conexaoPrep	- Conexao com o Banco de Dados
	 * @return int			- Resultado da atualizacao
	 * @throws GPPInternalErrorException
	 */
	public int atualizaTrocaPlanoFaleGratis(GerFaleGratisVO vo, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		// Seta os parametros para a atualizacao
		Object params[] = { new Integer(vo.getIdPlanoFaleGratis()),
						    new Integer(vo.getIdPlanoPulaPula()),
						    new Integer(vo.getIdLimiteSMS()),
						    vo.getMsisdn(),
						    vo.getPeriodo() };
		
		// Realiza a atualizacao e retorna o resultado da mesma
		return conexaoPrep.executaPreparedUpdate(SQL_ATUALIZA_TROCA_PLANO_FALEGRATIS, params, idProcesso);
	}
	
	/**
	 * Descricao.: Atualiza o registro do assinante retornou da promocao
	 * 			   Pula-Pula para o Fale Gratis, depois de ter sido retirado
	 * 			   da FG por ter ultrapassado o Limite Maximo da promocao
	 * 
	 * @param  vo			- Objeto populado com as informacoes do assinante
	 * @param  conexaoPrep	- Conexao com o Banco de Dados
	 * @return int			- Resultado da atualizacao
	 * @throws GPPInternalErrorException
	 */
	public int atualizaRetornoPlanoFaleGratis(GerFaleGratisVO vo, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		// Seta os parametros para a atualizacao
		Object params[] = { vo.getMsisdn() , vo.getPeriodo()};
		
		// Realiza a atualizacao e retorna o resultado da mesma
		return conexaoPrep.executaPreparedUpdate(SQL_ATUALIZA_RETORNO_PLANO_FALEGRATIS, params, idProcesso);
	}
	
	/**
	 * Descricao.: Realiza a atualizacao na tabela de totalizacao de chamadas,
	 * 			   inserindo o dado referente a mensagem SMS enviada ao assinante
	 * 			   por atingir algum limite de minutos (parcial ou maximo)
	 * 
	 * @param  vo			- Objeto contendo os dados para atualizacao
	 * @param  conexaoPrep	- Conexao com o Banco de Dados
	 * @return int			- Resultado da atualizacao
	 * @throws GPPInternalErrorException
	 */
	public int atualizaIdLimiteSMS(GerFaleGratisVO vo, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		// Seta os parametros para a atualizacao
		Object params[] = { new Integer(vo.getIdLimiteSMS()),
						    vo.getMsisdn(),
						    vo.getPeriodo() };
		
		// Realiza a atualizacao e retorna o resultado da mesma
		return conexaoPrep.executaPreparedUpdate(SQL_ATUALIZA_ID_SMS_ENVIADO, params, idProcesso);
	}
	
	/**
	 * Descricao.: Realiza a atualizacao do status do assinante apos a remocao
	 * 			   do Plano de Preco da promocao Fale Gratis
	 * 
	 * @param vo			- Objeto contendo as informacoes do assinante/promocao FGN
	 * @param conexaoPrep	- Conexao com o banco de dados
	 */
	public void atualizaStatusAssinanteRemocao(GerFaleGratisVO vo, PREPConexao conexaoPrep)
	{
		ControlePulaPula controlePulaPula = new ControlePulaPula(idProcesso);
		
		// Se status = 0 (Normal) ou 1 (Pendente de primeira recarga)
		// 				:Atualizar o status do assinante para 5 (Limite Alcancado)
		// Se status = 4 (Recarga Expirada) 
		// 				:Atualizar o status do assinante para 6 (Limite Alcancado e Recarga Expirada)
		if (vo.getStatusPromocao() == PromocaoStatusAssinante.STATUS_ATIVO ||
			vo.getStatusPromocao() == PromocaoStatusAssinante.STATUS_PENDENTE_RECARGA )
			controlePulaPula.trocarStatusPulaPula(vo.getMsisdn(),
												  PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO,
												  conexaoPrep);
		else if (vo.getStatusPromocao() == PromocaoStatusAssinante.STATUS_SUSPENSO_RECARGA_EXPIRADA)
			controlePulaPula.trocarStatusPulaPula(vo.getMsisdn(),
												  PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA,
												  conexaoPrep);
	}
	
	/**
	 * Descricao.: Realiza a atualizacao do status do assinante antes da 
	 * 			   insercao do mesmo no Plano de Preco do Fale de Graca a Noite
	 * 
	 * @param vo			- Objeto contendo as informacoes do assinante/promocao FGN
	 * @param conexaoPrep	- Conexao com o banco de dados
	 */
	public void atualizaStatusAssinanteInsercao(GerFaleGratisVO vo, PREPConexao conexaoPrep)
	{
		ControlePulaPula controlePulaPula = new ControlePulaPula(idProcesso);
		
		// Se status = 5 (Limite Alcancado)
		//				:Atualizar o status para 0 (ATIVO)
		// Se status = 6 (Limite Alcancado e Recarga Expirada)
		// 				:Atualizar o status para 4 (Recarga Expirada)
		if (vo.getStatusPromocao() == PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO)
			controlePulaPula.trocarStatusPulaPula(vo.getMsisdn(),
												  PromocaoStatusAssinante.STATUS_ATIVO,
												  conexaoPrep);
		else
			if (vo.getStatusPromocao() == PromocaoStatusAssinante.STATUS_SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA)
				controlePulaPula.trocarStatusPulaPula(vo.getMsisdn(),
													  PromocaoStatusAssinante.STATUS_SUSPENSO_RECARGA_EXPIRADA,
													  conexaoPrep);
	}
	
	/**
	 * Descricao.: Apos a troca de MSISDN, o assinante que participa da promocao
	 * 			   FGN tera as informacoes da totalizacao "clonada" para o novo
	 * 			   MSISDN, mantendo a coerencia na utilizacao da promocao
	 * 
	 * @param  msisdnNovo	Novo MSISDN do assinante
	 * @param  msisdnAntigo MSISDN antigo
	 * @param  conexaoPrep	Conexao com o Banco de Dados
	 * @throws Exception
	 * @since  19/10/2007
	 */
	public void duplicaTotalizacaoFaleGratis(String msisdnNovo, String msisdnAntigo, PREPConexao conexaoPrep) throws Exception
	{
		// Seta os parametros para a atualizacao
		Object params[] = { msisdnNovo,
						    msisdnAntigo };
		
		// Realiza a insercao das informacoes da totalizacao FaleGratis
		conexaoPrep.executaPreparedUpdate(SQL_INSERCAO_APOS_TROCA_MSISDN, params, idProcesso);
	}
}