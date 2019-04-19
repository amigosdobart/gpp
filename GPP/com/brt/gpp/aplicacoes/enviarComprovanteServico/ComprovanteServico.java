//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarComprovanteServico;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.consultar.gerarExtrato.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Arquivos de Import Internos
import java.sql.*;

/**
  *
  * Este arquivo refere-se a classe ComprovanteServico, responsavel pela implementacao da
  * logica de envio de comprovantes de servicos de assinantes para o DOC1
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Camile Cardoso Couto
  * Data: 				19/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class ComprovanteServico extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	
	/**
	 * Metodo...: ComprovanteServico
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public ComprovanteServico (long logId)
	 {
		super(logId, Definicoes.CL_COMPROVANTE_SERVICO);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	 /**
	  * Metodo...: geraComprovanteServico
	  * Descricao: Le os registros de assinantes que solicitaram o comprovante de servico,
	  * 			chama o processo de extracao de dados e grava XML de saida em tabela
	  * @param
	  * @return	boolean	- TRUE (processamento com sucesso) ou FALSE (se nao processou com sucesso)
	  * @throws GPPInternalErrorException									
	  */
	 public boolean geraComprovanteServico () throws GPPInternalErrorException, GPPTecnomenException
	 {
	 	super.log(Definicoes.INFO, "geraComprovanteServico", "Inicio");
	 	
	 	// Inicializa variaveis do metodo
		boolean retorno = true;		 
		long aIdProcesso = super.getIdLog();
				
		String sql;
		ResultSet rs;
		PREPConexao conexaoPrep = null;
		
		Timestamp dataRequisicao;
		String msisdn;
		String dataInicial;
		String dataFinal;
		String xmlExtrato;
		String statusProcessamentoRegistro;
		String statusProcesso = Definicoes.TIPO_OPER_ERRO;
		int cont = 0;
		
		String dataInicioProcessamento = GPPData.dataCompletaForamtada();
		  
		try
		{
			// Seleciona conexao do pool Prep Conexao
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
					
			// Seleciona dados para gerar comprovante
			sql = "SELECT DAT_REQUISICAO, IDT_MSISDN, TO_CHAR(DAT_PERIODO_INICIAL, 'DD/MM/YYYY HH24:MI:SS'), ";
			sql = sql + " TO_CHAR(DAT_PERIODO_FINAL, 'DD/MM/YYYY HH24:MI:SS') ";
			sql = sql + " FROM TBL_GER_DADOS_COMPROVANTE ";
			sql = sql + "WHERE IDT_STATUS_PROCESSAMENTO = ? ";
			sql = sql + "ORDER BY DAT_REQUISICAO";			
			Object param[] = {"N"};
			rs = conexaoPrep.executaPreparedQuery(sql, param, aIdProcesso);
			
			while (rs.next())
			{
				// contador de registros processados
				cont = cont + 1;
				
				statusProcessamentoRegistro = "N";
				xmlExtrato = null;
				
				// seta variaveis com dados selecionados
				dataRequisicao = rs.getTimestamp(1);
				msisdn = rs.getString(2);
				dataInicial = rs.getString(3);
				dataFinal = rs.getString(4);
				
				super.log(Definicoes.INFO, "geraComprovanteServico", "Data requisicao: " + dataRequisicao + " MSISDN: " + msisdn + " Data Ini.: " + dataInicial + " Data Fim: " + dataFinal);
			
				// chama processo de extrato passando true na variavel eComprovanteServico
				GerarExtrato extrato = new GerarExtrato(aIdProcesso);
				xmlExtrato = extrato.comporExtrato(msisdn, dataInicial, dataFinal, true, dataRequisicao);
				
				super.log(Definicoes.DEBUG, "geraComprovanteServico", "XMLExtrato: " + xmlExtrato);
				
				// Se conseguir gerar xml de extrato, insere registro na tabela de saida
				if (xmlExtrato != null)
				{
					// grava dados na tabela de saida
					if (gravaComprovanteServico(msisdn, xmlExtrato))
					{
						super.log(Definicoes.INFO, "gravaMensagemSMS", "Gravou registro");
						statusProcessamentoRegistro = "S";
					}	
					else
					{
						super.log(Definicoes.WARN, "gravaMensagemSMS", "Nao gravou registro");
						statusProcessamentoRegistro = "E";
					}
				}
				else
				{
					statusProcessamentoRegistro = "E";
				}
				
				// atualiza tabela original com resultado do processamento do registro
				atualizaProcessamentoRegistro(msisdn, dataRequisicao, statusProcessamentoRegistro);
				
				super.log(Definicoes.INFO, "gravaMensagemSMS", "Atualizou registro");						
			}
			rs.close();
			rs = null;
			
			// Deleta da tabela de saida todos os registros ja processados pelo Vitria
			//String sqlDelete = "DELETE FROM TBL_GER_DADOS_COMPROVANTE WHERE IDT_STATUS_PROCESSAMENTO = ?";
			//Object param1[] = {"S"};
			//conexaoPrep.executaPreparedUpdate(sqlDelete, param1, aIdProcesso);			
			
			statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
		}		
		catch (GPPInternalErrorException e1)
		{
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO, "geraComprovanteServico", "Excecao Interna GPP:"+ e1);			
			retorno = false;						
			throw new GPPInternalErrorException ("Erro Interno GPP: " + e1);	
		} catch (SQLException e2) {
			
			super.log(Definicoes.WARN, "geraComprovanteServico", "Excecao SQL:"+ e2);			
			retorno = false;						
			throw new GPPInternalErrorException ("Erro SQL Exception:" + e2);	
		}
		finally
		{
			// Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, aIdProcesso);
			
		 	
			String dataFimProcessamento = GPPData.dataCompletaForamtada();
			String descricao = "Foram processados " + cont + " registros.";			
						
			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_COMPROVANTE_SERVICO, dataInicioProcessamento, dataFimProcessamento, statusProcesso, descricao, dataInicioProcessamento);

			// Deleta todos os registros processados com sucesso 
			//limpaProcessoOk();

			
			super.log(Definicoes.INFO, "geraComprovanteServico", "Fim");
		}
		return retorno;
	}
	
	/**
	  * Metodo...: gravaComprovanteServico
	  * Descricao: Insere na tabela o registro processado a ter o comprovante impresso
	  * @param	aComprovante 	- Estrutura com dados do comprovante processado
	  * @return	boolean			- TRUE (Gravou registro) ou FALSE (Nao gravou registro)									
	  * @throws GPPInternalErrorException									
	  */
	private boolean gravaComprovanteServico(String aMSISDN, String aXMLExtrato) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "gravaComprovanteServico", "Inicio MSISDN "+aMSISDN);
	
		// Inicializa variaveis do metodo
		boolean retorno = true;
		String sql;
		PREPConexao conexaoPrep = null;
		
		try
		{	
			//	Seleciona conexao do pool Prep Conexao
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			// Grava registro no banco de dados
			sql = "INSERT INTO TBL_INT_COMPROVANTE_OUT " +
				" (DAT_CADASTRO, IDT_MSISDN, XML_DOCUMENT, IND_PROCESSADO) " +
				" VALUES " +
				" (SYSDATE, ?, ?, ?)";
			
			Object param[] = {aMSISDN, aXMLExtrato, "N"};
			conexaoPrep.executaPreparedUpdate(sql, param, super.logId);							
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "gravaComprovanteServico", "Excecao:"+ e);				
			throw new GPPInternalErrorException ("Excecao GPP:" + e);			 
		}	
		finally
		{
			// Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "gravaComprovanteServico", "Fim");
		}
		return retorno;
	}
	
	/**
	  * Metodo...: atualizaProcessamentoRegistro
	  * Descricao: Atualiza o registro processado com o status do seu processamento
	  * @param	aMSISDN			- Numero do MSISDN processado
	  * @param	aDataRequisicao - Data em que foi solicitado o comprovante
	  * @param 	aStatus			- Status do processamento
	  * @return
	  * @throws GPPInternalErrorException									
	  */
	public void atualizaProcessamentoRegistro(String aMSISDN, Timestamp aDataRequisicao, String aStatus) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "gravaComprovanteServico", "Inicio MSISDN "+aMSISDN);
	
		// Inicializa variaveis do metodo
		String sqlUpdate;
		PREPConexao conexaoPrep = null;
		
		try
		{	
			//	Seleciona conexao do pool Prep Conexao
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			// Atualiza registro no banco de dados
			sqlUpdate = "UPDATE TBL_GER_DADOS_COMPROVANTE";
			sqlUpdate = sqlUpdate +	" SET IDT_STATUS_PROCESSAMENTO = ?";
			sqlUpdate = sqlUpdate + " WHERE IDT_MSISDN = ? AND DAT_REQUISICAO = ?";
			sqlUpdate = sqlUpdate + " AND IDT_STATUS_PROCESSAMENTO = ?";			
			Object param[] = {aStatus, aMSISDN, aDataRequisicao, "N"};
			conexaoPrep.executaPreparedUpdate(sqlUpdate, param, super.logId);							
		}	
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "atualizaProcessamentoRegistro", "Excecao:"+ e);				
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		
		finally
		{
			// Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "atualizaProcessamentoRegistro", "Fim");
		}
	}
	
	/**
	 * Metodo...: limpaProcessoOk
	 * Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	 * 			  ou seja, os registros que já foram processados pelo ETI.
	 * @param
	 * @return	void										
	  * @throws GPPInternalErrorException									
	 */
	
	public void limpaProcessoOk () throws GPPInternalErrorException
	{

		super.log(Definicoes.DEBUG, "limpaProcessoOk", "Inicio");

		try
		{
			// Busca uma conexao de banco de dados		
			PREPConexao conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
	
			String ind_processado = Definicoes.IND_PROCESSADO;
				
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
			
			// Deleta da tabela os registros que já foram processados pelo ETI
			String sql_processo = "DELETE FROM TBL_INT_COMPROVANTE_OUT " +
								  " WHERE IND_PROCESSADO = ? " +
								  " and dat_cadastro < (sysdate - ?)";
	
			Object paramProcesso[] = {ind_processado,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
	
			super.log(Definicoes.DEBUG, "verificaRegistrosProcessados", conexaoPREP.executaPreparedUpdate(sql_processo,paramProcesso, super.logId)+" registro(s) deletado(s) com sucesso.");
 	
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "verificaRegistrosProcessados", "Erro: Nao conseguiu pegar conexao com banco de dados.");
		}
		finally
		{
			super.log(Definicoes.DEBUG, "limpaProcessoOk", "Fim");
		}
	}

}