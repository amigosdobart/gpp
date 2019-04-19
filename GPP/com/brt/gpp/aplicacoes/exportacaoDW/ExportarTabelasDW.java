package com.brt.gpp.aplicacoes.exportacaoDW;

import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.*;
import com.brt.gpp.comum.conexoes.bancoDados.*; 
import com.brt.gpp.gerentesPool.*;
import com.brt.gpp.aplicacoes.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
  *
  * Este arquivo refere-se a classe ExportarTabelasDW, responsavel 
  * pela implementacao da logica de exportacao dos dados para o Data
  * Warehouse atraves de tabelas de interface
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				10/06/2004
  *
  * Modificado por: Bernardo Vergne Dias
  * Correções nos comandos SQL (tratamento de datas)
  * Data: 25/01/2008
  *
  */
public class ExportarTabelasDW extends Aplicacoes
{
	private DefinicoesSQLDW definicoesDW;
	private String mensagemHistorico;
	
	/**
	 * Metodo....: ExportarTabelasDW
	 * Descricao.: Construtor
	 * @param aLogId - A id do processo
	 */
	public ExportarTabelasDW(long aLogId)
	{
		super(aLogId, Definicoes.CL_EXPORTACAO_DADOS_DW);
		definicoesDW = new DefinicoesSQLDW();
		mensagemHistorico = "Exportacao DW -> ";
	}

	private void registraHistoricoBatch(String aDatInicial, 
	                                    String aDatFinal, 
	                                    String aDatProcessamento, 
	                                    String aStatus) throws GPPInternalErrorException
	{ 
		super.gravaHistoricoProcessos(Definicoes.IND_EXPORTACAO_DADOS_DW,aDatInicial,aDatFinal,aStatus,mensagemHistorico,aDatProcessamento);
	}

	/**
	 * Metodo....: adicionaMensagem
	 * Descricao.: Faz o "append" de uma mensagem a propriedade mensagem historico que
	 *             sera utilizada para registrar a execucao do processo batch
	 * @param msg - Mensagem a ser adicionada
	 */
	private void adicionaMensagem(String msg)
	{
		mensagemHistorico += msg;
	}

	/**
	 * Metodo....: executaExportacao
	 * Descricao.: Executa a exportacao de uma tabela definida na classe de definicoes
	 *             de SQLDW pela execucao do comando existente nessa classe
	 * @param nomeTabela - Nome da tabela a ser executado o comando
     * @param dataProcessamento - Data no formato dd/MM/yyyy HH:mm:ss
	 * @return short     - Indica se o processamento foi ok.
	 * @throws GPPInternalErrorException
	 */
	private void executaExportacao(String nomeTabela, String dataProcessamento) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "executaExportacao", "Inicio TABELA "+nomeTabela);
		GerentePoolBancoDados gerenteBancoDados = GerentePoolBancoDados.getInstancia(super.getIdLog());
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlASerExecutada = definicoesDW.getSQL(nomeTabela);
            Object[] parametrosSQL = definicoesDW.getParametros(nomeTabela, dataProcessamento);
			int nroLinhas = conexaoPrep.executaPreparedUpdate(sqlASerExecutada, parametrosSQL, super.getIdLog());
			conexaoPrep.commit();
			adicionaMensagem(" Tabela " + nomeTabela + " : " + nroLinhas);
		}
		catch(java.sql.SQLException e)
		{
			super.log(Definicoes.WARN, "executaExportacao", "Excecao SQL: "+ e);
			throw new GPPInternalErrorException("Excecao GPP: " + e);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "executaExportacao", "Fim");
	}

	/**
	 * Metodo....: registraExportacao
	 * Descricao.: Regitra a exportacao da tabela em uma tabela de parametros do DW
	 * @param nomeTabela - Nome da tabela a ser registrada a exportacao dos dados 
     * @param dataProcessamento - Data no formato dd/MM/yyyy HH:mm:ss
	 */
	private void registraExportacao(String nomeTabela, String dataProcessamento) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "registraExportacao", "Fim");
		GerentePoolBancoDados gerenteBancoDados = GerentePoolBancoDados.getInstancia(super.getIdLog());
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlRegistro = "INSERT INTO TBL_EXT_DW_PARAMETRO_CARGA " +
			                     "(NOM_TABELA, " +
			                     " DAT_PROCESSAMENTO, " +
			                     " IDT_SISTEMA, " +
			                     " IDT_STATUS " +
			                     ")VALUES (?,to_date(?, 'dd/mm/yyyy hh24:mi:ss'),?,?)";

			Object parametros[] = {nomeTabela, 
                                   dataProcessamento,
				                   Definicoes.SO_GPP,
				                   new Integer(Definicoes.STATUS_OK_DADOS_DW_GPP)}; 
			conexaoPrep.executaPreparedUpdate(sqlRegistro,parametros,super.getIdLog());
			conexaoPrep.commit();
		}
		catch(java.sql.SQLException e)
		{
			super.log(Definicoes.WARN, "executaExportacao", "Excecao SQL:"+ e);
			throw new GPPInternalErrorException("Excecao GPP:" + e);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "registraExportacao", "Fim");
	}

	/**
	 * Metodo....: exportarTabelas
	 * Descricao.: Exporta todas as tabelas definidas na classe de DefinicoesSQLDW
	 * @return short - Numero indicando se o processo foi executado com sucesso ou nao
	 * @throws GPPInternalErrorException
	 */
	public short exportarTabelas() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO, "exportarTabelas", "Inicio");

		short codRetorno = Definicoes.RET_OPERACAO_OK;
		
        SimpleDateFormat sdf      = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		String aDataInicial       = sdf.format(Calendar.getInstance().getTime());
		String aDataProcessamento = GPPData.dataFormatada();
		String aStatus            = Definicoes.TIPO_OPER_SUCESSO;
		String aDataFinal;
		
		/* Para cada tabela existe nas definicoes, o processo de exportacao
		 * e executado e em seguida registrado tal exportacao */
		String nomeTabelas[] = definicoesDW.getListaTabelasExportacao();
		for (int i=0; i < nomeTabelas.length; i++)
		{
			try
			{
				// Executa a exportacao pela execucao do comando
				super.log(Definicoes.INFO, "exportarTabelas", "Iniciando processo exportacao para tabela:" + nomeTabelas[i]);
				executaExportacao(nomeTabelas[i], aDataInicial);
				
				super.log(Definicoes.INFO, "exportarTabelas", "Registrando exportacao da tabela:" + nomeTabelas[i]);
				registraExportacao(nomeTabelas[i], aDataInicial);
			}
			catch(GPPInternalErrorException e)
			{
				codRetorno = 1;
				aDataFinal = sdf.format(Calendar.getInstance().getTime());
				aStatus    = Definicoes.TIPO_OPER_ERRO;
				adicionaMensagem(" Processo descontinuado.. erro:"+e.getMessage());
				registraHistoricoBatch(aDataInicial,aDataFinal,aDataProcessamento,aStatus);
				throw new GPPInternalErrorException("Erro Interno do GPP:"+e);
			}
		}
		aDataFinal = sdf.format(Calendar.getInstance().getTime());
		registraHistoricoBatch(aDataInicial,aDataFinal,aDataProcessamento,aStatus);

		super.log(Definicoes.INFO, "exportarTabelas", "Fim");
		return codRetorno;
	}
}