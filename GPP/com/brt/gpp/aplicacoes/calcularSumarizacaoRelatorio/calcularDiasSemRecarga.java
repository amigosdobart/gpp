//Definicao do Pacote
package com.brt.gpp.aplicacoes.calcularSumarizacaoRelatorio;

//Arquivos de JAVA
import java.text.SimpleDateFormat;
import java.sql.*;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.aplicacoes.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

/**
  * Essa classe refere-se ao processo de atualização da tabela TBL_REL_SEM_RECARGA:
  * a) Exclusão dos Assinantes que não mais se encontram na plataforma
  * b) Inclusão dos novos Assinantes
  * c) Atualização de Status/Plano de Preço dos Assinantes
  * d) Cálculo do Número de Dias há que um Assinante não faz recarga
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				17/05/2004
  *
  * Modificado por: Alberto Magno (Adaptação LIG MIX e otimização nos SQLs)
  * Data: 15-03-2005
  * Razao:
  *
  */
public class calcularDiasSemRecarga extends Aplicacoes {
	
	GerentePoolBancoDados gerenteBanco = null;
		
	/**
	 * Metodo...: calcularDiasSemRecarga
	 * Descricao: Construtor
	 * @param long	aIdProcesso	ID para efeitos de log
	 */
	public calcularDiasSemRecarga(long aIdProcesso) 
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_DIAS_SEM_RECARGA);

		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
	}
	
	/**
	 * Metodo...: atualizaRecargas
	 * Descricao: Atualiza recargas, metodo principal que coordena os outros
	 * @param
	 * @return short 	- Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */
	public short atualizaRecargas() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"atualizaRecargas","Inicio");

		short retorno = 0;
		String errorMsg = Definicoes.PROCESSO_ERRO;
		String dataInicialProcesso = null;
		String dataFinalProcesso = null;
		String descricaoProcesso = null;

		try
		{
			//Registra Início do Processamento
			dataInicialProcesso = GPPData.dataCompletaForamtada();
		
			//Atualiza TBL_REL_SEM_RECARGA com os novos assinantes
			retorno = this.insereNovosAssinantes();
			if(retorno==0)
			{
				//Elimina assinantes inativos da TBL_REL_SEM_RECARGA
				retorno = this.eliminaAssinantesInativos();
				if(retorno==0)
				{
					//Atualiza Plano e Status dos Assinantes
					retorno = this.atualizaPlanoStatus();
					if(retorno==0)
					{
						//Calcular Número de Dias sem Recarga
						retorno = this.atualizaDiasSemRecarga();
						if(retorno==0)
						{
							// Pega data final de execução do processo concluído com êxito
							errorMsg = Definicoes.PROCESSO_SUCESSO;
							descricaoProcesso = "Dias sem Recarga Atualizados";
							dataFinalProcesso = GPPData.dataCompletaForamtada();
						}
						else
						{
							descricaoProcesso = "Erro ao Calcular Número de Dias sem Recarga";
						}	
					}
					else
					{
						descricaoProcesso = "Erro ao Atualizar Plano e Status dos Assinantes";
					}
				}
				else
				{
					descricaoProcesso = "Erro ao Eliminar assinantes inativos da TBL_REL_SEM_RECARGA";
				}	
			}
			else
			{
				descricaoProcesso = "Erro ao Atualizar TBL_REL_SEM_RECARGA com os novos assinantes";
			}	
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
				
			//Logar inicio processo batch
			descricaoProcesso = e.getMessage();
			super.log(Definicoes.ERRO,"atualizaRecargas","Erro Interno GPP: "+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			super.log(Definicoes.INFO,"atualizaRecargas","Fim");
			super.gravaHistoricoProcessos(Definicoes.IND_DIAS_SEM_RECARGA,dataInicialProcesso,dataFinalProcesso,errorMsg,descricaoProcesso,dataFinalProcesso);
		}
		
		return retorno;	
	}

	/**
	 * Metodo...: insereNovosAssinantes
	 * Descricao: Atualiza a TBL_REL_SEM_RECARGAS com os novos assinantes
	 * @param
	 * @return short - Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */
	public short insereNovosAssinantes() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"insereNovosAssinantes","Inicio");
		short retorno = -1;
		ConexaoBancoDados DBConexao = null;
		
		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			// Query para inserir novos usuários
			String queryInsert = "INSERT INTO TBL_REL_SEM_RECARGAS "+ 
				"(IDT_MSISDN, IDT_PLANO_PRECO, IDT_STATUS, DAT_ULTIMA_RECARGA, NUM_DIAS_SEM_RECARGA) "+
				"SELECT A.IDT_MSISDN, P.DES_PLANO_PRECO AS IDT_PLANO_PRECO, S.DES_CONFIGURACAO AS IDT_STATUS, DAT_ATIVACAO, NULL FROM TBL_APR_ASSINANTE A, "+
				"TBL_GER_PLANO_PRECO P, TBL_GER_CONFIG_ASSINANTE S "+ 
				"WHERE "+
				" S.ID_CONFIGURACAO = A.IDT_STATUS AND S.TIP_CONFIGURACAO = ? "+ 
				" AND P.IDT_PLANO_PRECO = A.IDT_PLANO_PRECO  "+ 
				" AND NOT EXISTS (SELECT 1 FROM TBL_REL_SEM_RECARGAS WHERE IDT_MSISDN = A.IDT_MSISDN)";
				
				Object paramRecarga[] = {Definicoes.TIP_CONF_STATUS_ASSINANTE};

				// Popula a tabela TBL_INT_CONCIL_OUT com recargas
				DBConexao.executaPreparedQuery(queryInsert,paramRecarga,super.getIdLog());
				retorno = 0;
				
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"insereNovosAssinantes","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}
		
		super.log(Definicoes.INFO,"insereNovosAssinantes","Fim");
		return retorno;
	}
		
	/**
	 * Metodo...: eliminaAssinantesInativos
	 * Descricao: Atualiza a TBL_REL_SEM_RECARGAS apagando os assinantes inativos
	 * @param
	 * @return short - Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws	GPPInternalErrorException
	 */
	public short eliminaAssinantesInativos() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"eliminaAssinantesInativos","Inicio");

		short retorno = -1;
		ConexaoBancoDados DBConexao = null;
		
		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			// Query para eliminar quem não consta mais na Tecnomen
			String queryDelete = "DELETE FROM TBL_REL_SEM_RECARGAS B WHERE NOT EXISTS "+ 
   				"(SELECT 1 FROM TBL_APR_ASSINANTE a "+
   				"WHERE a.IDT_MSISDN = B.IDT_MSISDN )";
   				
			// Deleta da  TBL_INT_CONCIL_OUT os assinantes inativos
			DBConexao.executaQuery(queryDelete,super.getIdLog());
			retorno = 0;
			
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"eliminaAssinantesInativos","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}	
		super.log(Definicoes.INFO,"eliminaAssinantesInativos","Fim");
		return retorno;
	}
	
	/**
	 * Metodo...: atualizaPlanoStatus
	 * Descricao: Atualiza a TBL_REL_SEM_RECARGAS segundo planos/status dos assinantes
	 * @param
	 * @return short - Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */
	public short atualizaPlanoStatus() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"atualizaPlanoStatus","Inicio");

		short retorno = -1;
		ConexaoBancoDados DBConexao = null;
		
		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			// Query para atualizar planos/status de assinantes
			String queryUpdate ="UPDATE TBL_REL_SEM_RECARGAS SR "+
				"SET (IDT_PLANO_PRECO, IDT_STATUS) = "+
				"(SELECT P.DES_PLANO_PRECO AS IDT_PLANO_PRECO, S.DES_CONFIGURACAO AS IDT_STATUS "+
				"FROM TBL_GER_PLANO_PRECO P, TBL_GER_CONFIG_ASSINANTE S, TBL_APR_ASSINANTE A "+
				"WHERE (P.IDT_PLANO_PRECO = A.IDT_PLANO_PRECO) "+
				"AND (S.ID_CONFIGURACAO = A.IDT_STATUS AND S.TIP_CONFIGURACAO = ?) "+
				"AND A.IDT_MSISDN = SR.IDT_MSISDN)";
				
			Object paramRecarga[] = {Definicoes.TIP_CONF_STATUS_ASSINANTE};

			// Atualiza a tabela TBL_INT_CONCIL_OUT (plano de preço e status dos Assinantes Antigos)
			DBConexao.executaPreparedUpdate(queryUpdate,paramRecarga,super.getIdLog());				
				
			retorno = 0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"atualizaPlanoStatus","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}	
		super.log(Definicoes.INFO,"atualizaPlanoStatus","Fim");
		return retorno;
	}
	

	/**
	 * Metodo...: atualizaDiasSemRecarga
	 * Descricao: Atualiza a TBL_REL_SEM_RECARGAS indicando a quantidade 
	 * 			  de dias que os usuários estão sem fazer recarga
	 * @param
	 * @return short - Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */
	public short atualizaDiasSemRecarga() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"atualizaDiasSemRecarga","Inicio");

		short retorno = -1;
		ConexaoBancoDados DBConexao = null;
		ResultSet rsRecargas = null;
		String dataUltimaRecarga = null;
		String msisdn = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			// Query para encontrar a data da última recarga
			String queryRecargas = "select idt_msisdn, max(dat_recarga) from tbl_rec_recargas "+
			      "where dat_recarga > (select max(dat_final_execucao) from tbl_ger_historico_proc_batch "+
			      "where ID_PROCESSO_BATCH = ?) and id_tipo_recarga=? group by idt_msisdn";
				
			Object paramRecarga[] = {new Integer(Definicoes.IND_DIAS_SEM_RECARGA), Definicoes.TIPO_RECARGA};

			// Atualiza a tabela TBL_REL_SEM_RECARGAS (plano de preço e status dos Assinantes Antigos)
			rsRecargas = DBConexao.executaPreparedQuery1(queryRecargas,paramRecarga,super.getIdLog());
			
			
			while(rsRecargas.next())
			{
				// Lê msisdn e data da última recarga de cada assinante
				msisdn = rsRecargas.getString(1);
				dataUltimaRecarga = rsRecargas.getString(2);
				super.log(Definicoes.INFO,"atualizaDiasSemRecargas","Ultima recarga de "+msisdn+" foi "+dataUltimaRecarga);
				
				//Define e Executa query de atualização da última data de recarga
				String queryAtualizarUltimaRecarga = "UPDATE TBL_REL_SEM_RECARGAS "+
				"SET DAT_ULTIMA_RECARGA = ? WHERE IDT_MSISDN = ?";
				
				Object paramAtualizacao[] = {new java.sql.Timestamp(sdf.parse(dataUltimaRecarga).getTime()), msisdn};
				
				DBConexao.executaPreparedUpdate(queryAtualizarUltimaRecarga,paramAtualizacao,super.getIdLog());
			}
			
			//Atualiza o número de dias sem recarga de cada usuário
			String queryAtualizaDiasSemRecarga = "UPDATE TBL_REL_SEM_RECARGAS "+
				"SET NUM_DIAS_SEM_RECARGA = SYSDATE - DAT_ULTIMA_RECARGA";
				
			DBConexao.executaUpdate(queryAtualizaDiasSemRecarga,super.getIdLog());							
				
			retorno = 0;
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"atualizaPlanoStatus","Erro:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}	
		super.log(Definicoes.INFO,"atualizaDiasSemRecarga","Fim");
		return retorno;
	}
}
