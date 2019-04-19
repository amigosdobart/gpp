//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarUsuariosStatusNormal;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;
import java.util.ArrayList;

/**
  *
  * Este arquivo refere-se a classe EnviarUsuariosStatusNormal, responsavel pela implementacao da
  * logica de envio de usuarios com status Normal para o Sistema de Comissionamento
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				26/05/2004
  *
  * Modificado por:		Lawrence Josuá
  * Data:				19/04/2005
  * Razao:				Modificação da query (query antiga comentada)
  * 
  * Modificado por: 	Gustavo Gusmao
  *	Data:				07/07/2005
  * Razao:				Modificacao do processo de comissionamento. O novo comissionamento dar-se-a apenas no momento
  * 					que o assinante realizar uma recarga. Para esta modificação, foi criada a tabela intermediaria
  * 					TBL_TMP_COMISSIONAMENTO. 
  * 
  */

public class EnviarUsuariosStatusNormal extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
		     
	// Dados referentes a cada MSISDN
	ArrayList linhaMsisdn = new ArrayList();

	/**
	 * Metodo...: EnviarUsuariosStatusNormal
	 * Descricao: Construtor 
	 * @param	logId	- Identificador do Processo para Log
	 * @return									
	 */
	 public EnviarUsuariosStatusNormal (long logId)
	 {
		super(logId, Definicoes.CL_ENVIO_USUARIO_NORMAL);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: enviaUsuariosNormal
	 * Descricao: Le os dados da tabela que devem ser enviados para o EAI 
	 * 			  caso o usuario esteja no status Normal
	 * @param 	aData		- Data do processamento do batch (formato DD/MM/AAAA)
	 * @return	short 		- Zero se sucesso ou diferente em caso de falha		
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException													
	 */
	public short enviaUsuariosNormal (String aData) throws GPPInternalErrorException, GPPTecnomenException
	{
		//Inicializa variaveis do metodo
		short retorno = 0;
		long aIdProcesso = super.getIdLog();
		int qtdAtivacoes = 0;

		PREPConexao conexaoPrep = null;
		String statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

		String dataInicial = GPPData.dataCompletaForamtada();
		
		super.log(Definicoes.DEBUG, "enviaUsuariosNormal", "Inicio DATA "+aData);
				
		try
		{			
			// Gera timestamp referente à data parametro
			java.sql.Timestamp data = GPPData.stringToTimestamp(aData);
			//java.sql.Timestamp dataInicio = GPPData.stringToTimestamp(aData);
			//java.sql.Timestamp dataFim = GPPData.stringToTimestamp(aData);
			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
			
			conexaoPrep.setAutoCommit(false);
			
//			String sql =	"INSERT INTO TBL_INT_STATUS_OUT 															" + 
//							"	(DAT_PROCESSAMENTO, IDT_MSISDN, IDT_STATUS_ASSINANTE, IDT_STATUS_PROCESSAMENTO) 		" +
//							"SELECT ?, tblStatus2.sub_id, ? , ?															" +
//							"			 FROM TBL_GER_PLANO_PRECO plano, 												" +
//							"			     (SELECT sub_id, profile_id FROM tbl_apr_assinante_tecnomen 				" +
//							"				   WHERE account_status = '1' AND dat_importacao = (? - 2) 					" +
//							"			       ) tblStatus1, 															" +
//							"			                                                                                " +
//							"			     (SELECT sub_id, profile_id FROM tbl_apr_assinante_tecnomen					" +
//							"				   WHERE account_status = '2' AND dat_importacao = (? - 1) 					" +
//							"				   ) tblStatus2																" +
//							"			                                                                                " +
//							"			 WHERE tblStatus1.sub_id = tblStatus2.sub_id 									" +
//							"			 AND   plano.IDT_PLANO_PRECO = tblStatus2.profile_id 							" +
//							"			 AND   plano.IDT_CATEGORIA = ?													" +
//							"UNION ALL						 															" +
//							"SELECT ?, tblStatus2.sub_id, ? , ?															" +
//							"			 FROM TBL_GER_PLANO_PRECO plano,												" +
//							"			     (SELECT sub_id, profile_id FROM tbl_apr_assinante_tecnomen					" +
//							"				   WHERE account_status = '2' AND dat_importacao = (? - 1) 					" +
//							"				   ) tblStatus2																" +
//							"			                                                                                " +
//							"			 WHERE  not Exists (SELECT sub_id FROM tbl_apr_assinante_tecnomen t				" +
//							"				   		WHERE t.sub_id = tblStatus2.sub_id AND dat_importacao = (? - 2))	" +
//							"			 AND   plano.IDT_PLANO_PRECO = tblStatus2.profile_id 							" +
//							"			 AND   plano.IDT_CATEGORIA = ?  												";

			
			// insere os registros 'elegiveis' para comissionamento na tabela temporaria, ou seja, os assinantes que passaram 
			// de FTU para NORMAL

			String sql =	"INSERT INTO TBL_INT_STATUS_OUT (DAT_PROCESSAMENTO, IDT_MSISDN, IDT_STATUS_ASSINANTE, IDT_STATUS_PROCESSAMENTO)" +
								"select ?, sub_id, ?, ? " +
								"from 	(select sub_id " +
												",nvl((select account_status " +
														"from tbl_apr_assinante_tecnomen b " +
														"where b.dat_importacao = ass.dat_importacao - 1 " +
														"and b.sub_id           = ass.sub_id " +
										"),?) as sta_dia_anterior " +
										"from tbl_apr_assinante_tecnomen ass " +
					                    	",tbl_ger_plano_preco plano " +
					                    "where account_status        = ? " +
					                      "and dat_importacao        = ? - 1 " +
					                      "and plano.idt_plano_preco = profile_id " +
					                      "and plano.idt_categoria   = ? " +
														") " + 
						      "where sta_dia_anterior = ? " +
						      "and not exists (select 1 from tbl_int_status_out where idt_msisdn = sub_id)" ;
		
			
			Object param1[] = {	data, Definicoes.S_NORMAL, Definicoes.IND_LINHA_DISPONIBILIZADA, 
								new Short(Definicoes.FIRST_TIME_USER), new Short(Definicoes.NORMAL), 
								data, new Integer(Definicoes.COD_CAT_PREPAGO), new Short(Definicoes.FIRST_TIME_USER)};
								
			qtdAtivacoes = conexaoPrep.executaPreparedUpdate(sql, param1, super.logId);
			conexaoPrep.commit();		
			
			// Atualiza tbl_int_status_out com os assinantes que fizeram a primeira recarga
			sql = 	"UPDATE TBL_INT_STATUS_OUT tblInt " +
					"SET IDT_STATUS_PROCESSAMENTO = ?, DAT_PROCESSAMENTO = ? " +
					"WHERE IDT_STATUS_PROCESSAMENTO = ? "+ 
					"AND exists "+ 
						"(   select 1 from tbl_rec_recargas where idt_msisdn = tblInt.idt_msisdn "+
						"    and dat_origem > (tblInt.dat_processamento - 3) "+
						"    and id_tipo_recarga = ? "+
						") ";
					
			Object param2[] = {Definicoes.IND_LINHA_NAO_PROCESSADA, data, Definicoes.IND_LINHA_DISPONIBILIZADA, Definicoes.TIPO_RECARGA};
			
			qtdAtivacoes = conexaoPrep.executaPreparedUpdate(sql, param2, super.logId);
			conexaoPrep.commit();
			conexaoPrep.setAutoCommit(true);
		}		
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "enviaUsuariosStatusNormal", "Erro GPP:"+ e);
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			throw new GPPInternalErrorException ("Excecao GPP:" + e);				
		}
		finally
		{
			// Data para histórico de processos batch
			String dataFinal = GPPData.dataCompletaForamtada();
			
			// Descrição para histórico de processos batch
			String descricao = null;
			if(statusProcesso.equals(Definicoes.TIPO_OPER_ERRO))
			{
				descricao = "Erro ao Procurar por Ativacoes";
			}
			else
			{
				descricao = qtdAtivacoes + " Registros de Ativacoes";
			}
			
			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_USUARIO_NORMAL, dataInicial, dataFinal, statusProcesso, descricao, aData);

			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Deleta todos os registros processados com sucesso 
			//limpaProcessoOk();
			super.log(Definicoes.INFO, "enviaUsuariosStatusNormal", "Fim do envio de Usuarios com status Normal");
		}
		return retorno;
	}
	
	/**
	 * Metodo...: limpaProcessoOk
	 * Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	 * 			  ou seja, os usuario que foram extraidos para o EAI
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

			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
			
			// Deleta da tabela os usuarios processados com sucesso
			String sql_processo = "DELETE FROM TBL_INT_STATUS_OUT " +
								  " WHERE IDT_STATUS_PROCESSAMENTO = ? " +
								  "   AND DAT_PROCESSAMENTO < (SYSDATE - ?)";
	
			Object paramProcesso[] = {Definicoes.IDT_PROC_OK,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
	
			super.log(Definicoes.DEBUG, "limpaProcessoOk", conexaoPREP.executaPreparedUpdate(sql_processo,paramProcesso, super.logId)+ " usuario(s) ja processado(s) deletado(s)");
 		
 			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "verificaStatusProcesso", "Erro no processo de status: Nao conseguiu pegar conexao com banco de dados.");
		}
		super.log(Definicoes.DEBUG, "limpaProcessoOk", "Fim");
	}
}
