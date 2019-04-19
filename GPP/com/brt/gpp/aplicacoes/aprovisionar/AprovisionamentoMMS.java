package com.brt.gpp.aplicacoes.aprovisionar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
*
* Este arquivo refere-se a classe AprovisionamentoMMS, responsavel pela implementacao da
* logica de aprovisionamento/desaprovisionamento de assinantes na plataforma de MMS.
*
* <P> Versao:			1.0
*
* @Author: 			Gustavo Gusmao
* 					co-autor: Joao Carlos Lemgruber
* Data: 				16/08/2005
* 
*/

public class AprovisionamentoMMS extends Aplicacoes
{	
	/**
	 * Metodo...: AprovisionamentoMMS
	 * Descricao: construtor da classe
	 * @param	aLogId			id do processo
	 */	
	public AprovisionamentoMMS(long aLogId)
	{
		super(aLogId, Definicoes.CL_APROVISIONAMENTO_MMS);
	}
	
	/**
	 * Metodo...: importarAssinantesHSID
	 * Descricao: metodo que realiza a importacao dos assinantes cujos TSDs subiram desde a ultima execucao
	 * do processo
	 * @return	boolean			indica o sucesso ou erro do processo
	 * @throws GPPInternalErrorException
	 */
	public boolean importarAssinantesHSID(String aDataFinalParam) throws GPPInternalErrorException
	{
		boolean retorno = true;
		String statusProcesso = Definicoes.PROCESSO_SUCESSO;
		PREPConexao conexao = null;
		int contador = 0;
		String dataInicial = GPPData.dataCompletaForamtada();
		String dataProcessamento = "";
		super.log(Definicoes.INFO, "importarAssinantesHSID", "Inicio da importacao de Assinantes da base do HSID");
		
		try
		{
			conexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			conexao.setAutoCommit(false);
			// seleciona a ultima data na qual o processo foi executado com sucesso
			String sql = " SELECT max(dat_processamento) as data_execucao " +
							" FROM tbl_ger_historico_proc_batch " + 
							" WHERE id_processo_batch = ? " +
							" AND idt_status_execucao = ? ";
			Object[] params = {new Integer(Definicoes.IND_APROVISIONAMENTO_MMS), Definicoes.PROCESSO_SUCESSO};
			ResultSet rsDataInclusao = conexao.executaPreparedQuery(sql, params, super.getIdLog());
			rsDataInclusao.next();
			Timestamp dataExecucao = rsDataInclusao.getTimestamp("data_execucao");
			rsDataInclusao.close();
			ResultSet rsAssinantes = verificarAssinantes(conexao, dataExecucao, aDataFinalParam);
			while(rsAssinantes.next())
			{
				contador++;
				String msisdn = rsAssinantes.getString("msisdn");
				Timestamp novaDataInclusao = rsAssinantes.getTimestamp("datainclusao");
				String novoModelo = rsAssinantes.getString("ind_mms");
				String tipOperacao = null;
				String statusRegistro = null;
				
				if(Definicoes.MMS_MODELO_DESCONHECIDO.equals(novoModelo)) // aparelho novo desconhecido
				{
					statusRegistro = Definicoes.MMS_MODELO_DESCONHECIDO;
				}
				else //aparelho novo reconhecido
				{
					boolean novoAparelhoMMS = Definicoes.MMS_MODELO_COMPATIVEL.equals(novoModelo);
					sql = 	"SELECT decode(his.co_modelo, 0, ?, " +
						  	" 		decode((SELECT count(1) " +
						  	"					FROM hsid.hsid_modelo_capacidade mod " +
						  	" 					WHERE mod.co_modelo = his.co_modelo " +
							" 					AND mod.co_capacidade = ? " +
							" 					AND upper(mod.ds_caracteristica) LIKE 'SIM%'), 0, ?,?)) AS ind_mms " +
							" 	FROM hsid.hsid_cliente_historico his " +
							" 	WHERE his.dt_inclusao < ? " +
							" 	AND his.nu_msisdn = ? " + 
							" 	ORDER BY his.dt_inclusao desc";
					Object[] params1 = {Definicoes.MMS_MODELO_DESCONHECIDO, Definicoes.MMS_CODIGO_CAPACIDADE, Definicoes.MMS_MODELO_NAO_COMPATIVEL, Definicoes.MMS_MODELO_COMPATIVEL, novaDataInclusao,  msisdn};
					ResultSet rsPenultimoRegistro = conexao.executaPreparedQuery(sql, params1, super.getIdLog());
					
					if(rsPenultimoRegistro.next()) //o assinante jah havia mudado de aparelho, ou seja, existe um registro na tabela
					{
						String antigoModelo = rsPenultimoRegistro.getString("ind_mms");
						boolean antigoAparelhoMMS = antigoModelo.equals(Definicoes.MMS_MODELO_COMPATIVEL);
						if(novoAparelhoMMS)
						{
							if(antigoAparelhoMMS) // os 2 aparelhos suportam MMS
							{
								// grava na tabela como concluido
								statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
							}
							else //o aparelho antigo nao suporta MMS
							{
								tipOperacao = Definicoes.XML_OS_DESBLOQUEAR;
								statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
							}
						}
						else // o novo aparelho nao suporta MMS
						{
							if(antigoAparelhoMMS)
							{
								tipOperacao = Definicoes.XML_OS_BLOQUEAR;
								statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
							}
							else // nenhum dos aparelhos suporta MMS
							{
								statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
							}
						}
					}
					else // eh o primeiro registro do assinante na tabela
					{
						if(novoAparelhoMMS)
						{
							tipOperacao = Definicoes.XML_OS_DESBLOQUEAR;
							statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
						}
						else
						{
							statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
						}
					}
					rsPenultimoRegistro.close();
				}
				dataProcessamento = GPPData.DateToString(novaDataInclusao);				
				inserirAssinante(conexao, msisdn, tipOperacao, statusRegistro, novaDataInclusao);
			}
			rsAssinantes.close();
			conexao.commit();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "importarAssinantesHSID", "Erro GPP:"+ e);
			retorno = false;
			statusProcesso = Definicoes.PROCESSO_ERRO;
			try
			{
				conexao.rollback();
			}
			catch(SQLException ex)
			{
				super.log(Definicoes.ERRO, "importarAssinantesHSID", "Erro GPP:"+ ex);
			}
			throw new GPPInternalErrorException ("Excecao GPP:" + e);
		}
		finally
		{
			
			// Data para histórico de processos batch
			String dataFinal = GPPData.dataCompletaForamtada();
			
			// Descrição para histórico de processos batch
			String descricao = null;
			if(retorno)
			{
				descricao = contador + " Assinantes Importados";
			}
			else
			{
				descricao = "Erro ao Importar Assinantes do HSID";
			}
			
			if (dataProcessamento.equals(""))
			{
				dataProcessamento = aDataFinalParam;
			}
			try
			{
				conexao.setAutoCommit(true);
			}
			catch(SQLException e)
			{
				super.log(Definicoes.ERRO, "importarAssinantesHSID", "Erro GPP:"+ e);
			}

			// Libera conexao do banco de dados
			super.gerenteBancoDados.liberaConexaoPREP(conexao, super.getIdLog());
			// Grava o historico do processo
			super.gravaHistoricoProcessos(Definicoes.IND_APROVISIONAMENTO_MMS, dataInicial, dataFinal, statusProcesso, descricao, dataProcessamento);
			
			super.log(Definicoes.INFO, "importarAssinantesHSID", "Fim da importacao de assinantes da base do HSID");
		}		
		return(retorno);
	}
	
	/**
	 * Metodo...: enviarAprovisionamentoMMS
	 * Descricao: metodo que envia os arquivos de XML para a tabela TBL_INT_PPP_OUT, alem de verificar se os modelos
	 * desconhecidos ja foram cadastrados na base do HSID.
	 * @return	boolean			indica o sucesso ou erro do processo
	 * @throws GPPInternalErrorException
	 */	
	public boolean enviarAprovisionamentoMMS() throws GPPInternalErrorException
	{
		boolean retorno = true;
		PREPConexao conexao = null;
		super.log(Definicoes.INFO, "enviarAprovisionamentoMMS", "Inicio do Envio de Registros para o Vitria");
		
		try
		{
			conexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			conexao.setAutoCommit(false);
			ResultSet rsAssinantes = verificarModelosDesconhecidos(conexao);
			while(rsAssinantes.next())
			{
				String rowid = rsAssinantes.getString("id");
				String statusRegistro = rsAssinantes.getString("status");
				String tipOperacao = rsAssinantes.getString("tip_comando");
				atualizarAssinante(conexao, rowid, tipOperacao, statusRegistro);
			}
			rsAssinantes.close();
			
			// seleciona as informacoes (MSISDN, categoria e tipo de operacao) dos assinantes que
			// serao aprovisionados/desaprovisionados
			String sql = 	"SELECT mms.idt_msisdn AS msisdn, mms.tip_operacao AS tip_operacao, " +
								"		decode(ass.idt_msisdn, null, ?, ?) AS categoria " +
								" FROM tbl_apr_mms mms, tbl_apr_assinante ass " +
								" WHERE mms.idt_msisdn = ass.idt_msisdn (+) " +
								" AND mms.idt_status_registro <> ? " +
								" AND mms.tip_operacao IS NOT NULL";

			Object[] params3 = {Definicoes.XML_OS_CATEGORIA_POSPAGO, Definicoes.XML_OS_CATEGORIA_PREPAGO, Definicoes.MMS_REGISTRO_ENVIADO};
			ResultSet rsAprovisionamento = conexao.executaPreparedQuery(sql, params3, super.getIdLog());
			while(rsAprovisionamento.next())
			{
				//montar o xml
				String os = getNumeroOS(conexao);
				String msisdn = rsAprovisionamento.getString("msisdn");
				String categoria = rsAprovisionamento.getString("categoria");
				String operacao = rsAprovisionamento.getString("tip_operacao");				
				sql = "INSERT INTO TBL_INT_PPP_OUT " +
						" (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) VALUES " +
						" (?, SYSDATE, ?, ?, ?)";

				Object[] param = {os, Definicoes.IDT_EVENTO_NEGOCIO_APR_MMS, montarXML(Definicoes.SO_MMS + os, msisdn, categoria, operacao),Definicoes.IND_LINHA_NAO_PROCESSADA};
				conexao.executaPreparedUpdate(sql, param, super.getIdLog());
			}
			rsAprovisionamento.close();
			// atualiza os registros que foram enviados, alterando o campo idt_status_regitro para E
			sql = " UPDATE tbl_apr_mms " +
					" SET idt_status_registro = ? " +
					" WHERE idt_status_registro = ? " +
					" AND tip_operacao is not NULL ";
			Object[] params1 = {Definicoes.MMS_REGISTRO_ENVIADO, Definicoes.MMS_REGISTRO_CONCLUIDO};
			conexao.executaPreparedUpdate(sql, params1, super.getIdLog());
			conexao.commit();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "enviarAprovisionamentoMMS", "Erro GPP:"+ e);
			retorno = false;
			try
			{
				conexao.rollback();
			}
			catch(SQLException ex)
			{
				super.log(Definicoes.ERRO, "enviarAprovisionamentoMMS", "Erro GPP:"+ ex);
			}
			throw new GPPInternalErrorException ("Excecao GPP:" + e);
		}
			try
			{
				conexao.setAutoCommit(true);
			}
			catch(SQLException e)
			{
				super.log(Definicoes.ERRO, "enviarAprovisionamentoMMS", "Erro GPP:"+ e);
			}
			gerenteBancoDados.liberaConexaoPREP(conexao, super.getIdLog());
			super.log(Definicoes.INFO, "enviarAprovisionamentoMMS", "Fim do Envio de Registros para o Vitria");
			
		return(retorno);
	}
	
	
	/**
	 * Metodo...: verificarAssinantes
	 * Descricao: retorna um objeto do tipo ResulSet que contem informacoes sobre os usuarios que subiram TSD
	 * desde a ultima execucao do processo
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * 			dataInclusao	data da ultima execucao do processo
	 * @return	ResultSet		objeto que contem todos as informacoes retornadas pela consulta
	 * @throws GPPInternalErrorException
	 */	
	private ResultSet verificarAssinantes(PREPConexao conexao, Timestamp dataInclusao, String aDataFinalParam) throws GPPInternalErrorException
	{
		ResultSet resultado = null;
		try
		{
			if(aDataFinalParam.equals(""))
			{
				aDataFinalParam = GPPData.getDataAcrescidaDias("1");
			}
			
			
			// seleciona todos o MSISDN, a data de inclusao e a capacidade do aparelho
			// para todos os assinantes que subiram TSD desde a ultima execucao do processo
			String sql = "select cl.nu_msisdn as msisdn ,dt_inclusao as datainclusao " +
							" 		,decode(co_modelo,0,?, " +
							"			decode( " +
							"				(select count(1) " +
							" 					from hsid.hsid_modelo_capacidade cap " +
							" 					where cap.co_modelo = cl.co_modelo " +
							" 					and cap.co_capacidade = ? " + 
							" 					and upper(cap.ds_caracteristica) like 'SIM%' " +
							" 				), 0, ?, ?)) as ind_mms " +
							" from hsid.hsid_cliente cl " +
							" where dt_inclusao > ? " +
							" and dt_inclusao <= to_date(?,'dd/MM/yyyy') " +
							" order by dt_inclusao ";
			
			
			Object[] params = {Definicoes.MMS_MODELO_DESCONHECIDO, Definicoes.MMS_CODIGO_CAPACIDADE, Definicoes.MMS_MODELO_NAO_COMPATIVEL, Definicoes.MMS_MODELO_COMPATIVEL, dataInclusao, aDataFinalParam};
			resultado = conexao.executaPreparedQuery(sql, params, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "verificaAssinantes", "Erro SQL: " + e);
			throw new GPPInternalErrorException("Erro GPP: " + e);
		}
		return(resultado);
	}
	
	/**
	 * Metodo...: inserirAssinante
	 * Descricao: insere um registro na tabela TBL_APR_MMS
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * 			msisdn			MSISDN do assinante
	 * 			operacao		comando que sera enviado a plataforma de MMS (Adicionar/Retirar/null)
	 * 			statusRegistro	status do registro (C = Registro Concluido / D = MSISDN Desconhecido/ E = Registro Enviado)
	 * 			dataInclusao	data na qual o TSD subiu
	 * @return	int				indica o sucesso ou erro da insercao
	 * @throws GPPInternalErrorException
	 */	
	private int inserirAssinante(PREPConexao conexao, String msisdn, String operacao, String statusRegistro, Timestamp dataInclusao) throws GPPInternalErrorException
	{
		int resultado = 0;
		String sql = null;
		try
		{
			sql = "INSERT INTO tbl_apr_mms (idt_msisdn, tip_operacao, idt_status_registro, dat_inclusao_hsid, dat_processamento) " +
					" VALUES (?, ?, ?, ?, SYSDATE) ";
			Object[] params = {msisdn, operacao, statusRegistro, dataInclusao};
			resultado = conexao.executaPreparedUpdate(sql, params, super.getIdLog());					
		}
		catch(Exception sqlE)
		{
			super.log(Definicoes.WARN, "inserirAssinante", "Erro SQL: "+ sqlE);
			throw new GPPInternalErrorException("Erro GPP: "+ sqlE);
		}
		return(resultado);
	}
	
	/**
	 * Metodo...: verificarAssinantes
	 * Descricao: retorna um objeto do tipo ResulSet que contem informacoes sobre os usuarios cujos modelos
	 * foram cadastrados na base do HSID
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * @return	ResultSet		objeto que contem todos as informacoes retornadas pela consulta
	 * @throws GPPInternalErrorException
	 */	
	private ResultSet verificarModelosDesconhecidos(PREPConexao conexao) throws GPPInternalErrorException
	{
		ResultSet resultado = null;
		try
		{
			// seleciona informacoes (rowid do registro, tipo do comando a ser enviado, novo status do registro) 
			// sobre os assinantes que foram inseridos na tabela TBL_APR_MMS como D e que tiveram o modelo cadastrado
			// na base do HSID
			String sql = 	" SELECT id, status, decode(status, ?, ?, null) as tip_comando " +
							" FROM ( " +
							" 			SELECT " +
                            "        			mms.rowid as id, " +
                            "					decode(" +
                            "							(" +
                            "								select count(1) " +
                            " 									FROM hsid.hsid_modelo_capacidade mod " +
                            "	 								WHERE mod.co_modelo = tac.co_modelo " +
                            " 									AND mod.co_capacidade = ? " + 
                            " 									AND upper(mod.ds_caracteristica) like 'SIM%'" +
                            "							), 0, ?, ?) as status " +
							" 				FROM tbl_apr_mms mms, hsid.hsid_cliente cli, hsid.hsid_modelo_tac tac " +
                            " 				WHERE mms.idt_msisdn = cli.nu_msisdn " +
							" 				AND tac.co_tac = to_number(substr(cli.nu_imei,1,6)) " +
							" 				AND mms.idt_status_registro = ? " +
							"	    )";
				
			Object[] params={Definicoes.MMS_REGISTRO_CONCLUIDO, Definicoes.XML_OS_DESBLOQUEAR, new Integer(Definicoes.MMS_CODIGO_CAPACIDADE), 
								Definicoes.MMS_REGISTRO_ENVIADO, Definicoes.MMS_REGISTRO_CONCLUIDO, Definicoes.MMS_MODELO_DESCONHECIDO};
			
			resultado = conexao.executaPreparedQuery(sql, params, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "verificaAssinantes", "Erro SQL: " + e);
			throw new GPPInternalErrorException("Erro GPP: " + e);
		}
		return(resultado);
	}
		
	/**
	 * Metodo...: atualizarAssinante
	 * Descricao: atualiza um registro na tabela TBL_APR_MMS
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * 			rowid			campo rowid do registro a ser atualizado
	 * 			operacao		comando que sera enviado a plataforma de MMS (Adicionar/Retirar/null)
	 * 			statusRegistro	novo status do registro (C = Registro Concluido / D = MSISDN Desconhecido/ E = Registro Enviado)
	 * @return	int				indica o sucesso ou erro da atualizacao
	 * @throws GPPInternalErrorException
	 */
	private int atualizarAssinante(PREPConexao conexao, String rowid, String operacao, String statusRegistro) throws GPPInternalErrorException
	{
		int resultado = 0;
		String sql = null;
		try
		{
			sql = "UPDATE tbl_apr_mms " +
					" SET tip_operacao = ?, idt_status_registro = ? " +
					" WHERE rowid = ? "; 
			Object[] params = {operacao, statusRegistro, rowid};
			resultado = conexao.executaPreparedUpdate(sql, params, super.getIdLog());				
		}
		catch(Exception sqlE)
		{
			super.log(Definicoes.WARN, "atualizarAssinante", "Erro SQL: "+ sqlE);
			throw new GPPInternalErrorException("Erro GPP: "+ sqlE);
		}
		return(resultado);
	}
	
	/**
	 * Metodo...: montarXML
	 * Descricao: metodo que retorna o XML de aprovisionamento/desaprovisionamento
	 * @param 	os			numero da OS
	 * 			msisdn		MSISDN do assinante
	 * 			categoria	categoria do assinante (F1 = pos-pago, F2 = pre-pago/hibrido)
	 * 			operacao	operacao a ser realizada
	 * @return	String		XML de retorno
	 */
	private String montarXML(String os, String msisdn, String categoria, String operacao)
	{
		GerarXML gerador = new GerarXML("root");
		gerador.adicionaTag("id_os", os);
		gerador.adicionaTag("case_id", os);
		gerador.adicionaTag("order_priority", Definicoes.XML_OS_ORDER_LOW_PRIORITY);
		gerador.adicionaTag("categoria", categoria);
		gerador.adicionaTag("categoria_anterior", categoria);
		gerador.adicionaTag("case_type", "IMEI - MMS");
		gerador.adicionaTag("case_sub_type", operacao);
		gerador.abreNo("provision");
		gerador.abreNo("ELM_INFO_SIMCARD");
		gerador.adicionaTag("macro_servico", "ELM_INFO_SIMCARD");
		gerador.adicionaTag("operacao", "nao_alterado");
		gerador.adicionaTag("status", "NAO_FEITO");
		gerador.adicionaTag("x_tipo", "SIMCARD");
		gerador.abreNo("parametros");
		gerador.adicionaTag("simcard_msisdn", msisdn);
		gerador.fechaNo();
		gerador.fechaNo();
		gerador.abreNo("ELM_MMS_IMEI");
		gerador.adicionaTag("macro_servico", "ELM_MMS_IMEI");
		gerador.adicionaTag("operacao", operacao);
		gerador.adicionaTag("status", "NAO_FEITO");
		gerador.fechaNo();
		gerador.fechaNo();
		return(gerador.getXML());
	}
	
	/**
	 * Metodo...: getNumeroOS
	 * Descricao: retorna um número de OS para o XML do provision (pega numa sequence do banco de dados)
	 * @return	String	identificador da OS do provision
	 * @throws GPPInternalErrorException
	 */
	private String getNumeroOS(PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		String retorno = null;
		ResultSet rsSeq = null;
		
		String sqlSequence = "SELECT SEQ_OS_PROVISION.NEXTVAL AS ID_OS FROM DUAL";
		
		try
		{
			// Retorna o próximo elemento da sequence do Banco de Dados
			rsSeq = conexaoPrep.executaQuery(sqlSequence,super.getIdLog());
			rsSeq.next();
			retorno = rsSeq.getString("ID_OS");
			rsSeq.close();
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.WARN, "getNumeroOS", "Erro SQL: "+ sqlE);
			throw new GPPInternalErrorException("Erro GPP: "+ sqlE);
		}
		return retorno;
	}
}