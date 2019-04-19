package com.brt.gpp.aplicacoes.planoHibrido.gravarContratoSACRecarga;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import java.sql.ResultSet;

/**
 * Classe responsavel por selecionar os acessos Hibrido para
 * a devida gravacao do Contrato SAC e Numero da Recarga na
 * tabela de Recargas
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  22/01/2007
 */
public class GravaContratoSACRecargaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Recursos utilizados no Produtor
	private PREPConexao conexaoPrep;
	private ResultSet 	resultSet;
	
	// Parametros utilizados pelo processo Batch
	private int			numRegistros;
	private String 		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	/**
	 * Construtor da Classe
	 * 
	 * @param aLogId - ID para gravacao no Log
	 */
	public GravaContratoSACRecargaProdutor(long aLogId)
	{
		super(aLogId, Definicoes.CL_GRAVA_CONTRATO_SAC_RECARGA);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicia o processo de selecao dos 
	 * 			   acessos para atualizacao
	 * 
	 */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio do Processo" + params);
		try
		{
			// Seleciona conexao do pool de Conexoes
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			// Monta a instrucao para selecao dos registros a serem atualizados
			String sqlConsulta = " SELECT b.idt_msisdn AS MSISDN, b.num_contrato AS CONTRATO,			" +
								 "        MAX (a.dat_origem) AS DATA_ORIGEM                             " +	
								 "   FROM tbl_rec_recargas a, tbl_apr_plano_hibrido b                   " +
								 "  WHERE a.idt_msisdn = b.idt_msisdn                                   " +
								 "    AND a.dat_origem >= b.dat_ativacao_gpp                            " +
								 "    AND b.num_contrato IS NOT NULL                                    " +
								 "    AND (b.num_mes_execucao IS NULL OR b.num_mes_execucao = 0)        " +
								 "    AND (   (a.tip_transacao = ? AND a.idt_nsu_instituicao IS NULL)  	" +
								 "         OR (    a.tip_transacao = ?                                 	" +
								 "             AND INSTR (a.idt_nsu_instituicao, ';') = 0               " +
								 "             AND a.id_sistema_origem = ?                             	" +
								 "            )                                                         " +
								 "        )                                                             " +
								 "  GROUP BY b.idt_msisdn, b.num_contrato                               " ;
			
			Object parametros[] = { Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL, Definicoes.RECARGA_FRANQUIA,
									Definicoes.GPP_OPERADOR};
			
			// Seta o ResultSet com o resultado da consulta
			resultSet = conexaoPrep.executaPreparedQuery(sqlConsulta, parametros, super.getIdLog());
		}
		catch (GPPInternalErrorException eGPP)
		{
			super.log(Definicoes.ERRO, "Produtor.startup", "Excecao Interna GPP: " + eGPP);
			this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException(eGPP.toString());
		}
	}
	
	/**
	 * Metodo....: next
	 * Descricao.: Resposavel por retornar o proximo registro
	 * 			   da lista para o consumidor solicitante
	 * 
	 */
	public Object next() throws Exception
	{
		// Criacao do objeto a ser populado com as informacoes do Banco
		GravaContratoSACRecargaVO contratoSACRecarga = null;
		
		if (resultSet.next())
		{
			contratoSACRecarga = new GravaContratoSACRecargaVO();
			contratoSACRecarga.setMsisdn(resultSet.getString("MSISDN"));
			contratoSACRecarga.setIdContrato(resultSet.getLong("CONTRATO"));
			contratoSACRecarga.setDataOrigem(resultSet.getTimestamp("DATA_ORIGEM"));
			this.numRegistros++;
		}
		// Retorna o objeto devidamente preenchido
		return contratoSACRecarga;
	}
	
	/**
	 * Metodo....: getConexao
	 * Descricao.: Retorna a conexao com o Banco de Dados
	 * 
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}
	
	/**
	 * Metodo....: getDataProcessamento
	 * Descricao.: Retorna a Data de Processamento
	 * 
	 */
	public String getDataProcessamento()
	{
		return null;
	}
	
	/**
	 * Metodo....: getDescricaoProcesso
	 * Descricao.: Retorna a descricao do processo em questao
	 * 
	 */
	public String getDescricaoProcesso()
	{
		if (Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
			return "Foram processados " + numRegistros + " registros.";
		
		return "Erro durante o processo";
	}
	
	/**
	 * Metodo....: getIdProcessoBatch
	 * Descricao.: Retorna o ID do processo em questao
	 * 
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_GRAVA_CONTRATO_SAC_RECARGA;
	}
	
	/**
	 * Metodo....: getStatusProcesso
	 * Descricao.: Retorna o status de processamento
	 * 
	 */
	public String getStatusProcesso()
	{
		return this.statusProcesso;
	}
	
	/**
	 * Metodo....: setStatusProcesso
	 * Descricao.: Seta o status de processamento
	 * 
	 */
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}
	
	/**
	 * Metodo....: finish
	 * Descricao.: Finaliza o processo em questao
	 * 
	 */
	public void finish() throws Exception
	{
		try
		{
			this.resultSet.close();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			super.log(Definicoes.DEBUG, "Produtor.finish", "Fim do Processo.");
		}
	}
	
	/**
	 * Metodo....: handleException
	 * Descricao.: Trata as excecoes que ocorrem na classe
	 * 
	 */
	public void handleException()
	{
		// Excecoes nao tratadas
	}
}