package com.brt.gpp.aplicacoes.atualizarLimiteCredito;

//Arquivos de JAVA
import com.brt.gpp.aplicacoes.*;
import java.sql.ResultSet;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.*;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;

/**
  *
  * Essa classe refere-se ao processo de envio de informa��es de recargas feitas via banco
  * ou pagas com cart�o de cr�dito para o Sistema de Concilia��o (MCR) 
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				30/03/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class AtualizarLimiteCredito extends Aplicacoes 
{
	GerentePoolBancoDados gerenteBanco = null;
	
	/**
	 * Metodo...: Conciliar
	 * Descricao: Construtor
	 * @param 	long	aIdProcesso		Id Processo para efeitos de log
	 */
	public AtualizarLimiteCredito(long aIdProcesso) 
	{
		//Define par�metros de Log
		super(aIdProcesso, Definicoes.CL_ATT_LIMITE_CREDITO);
		//Cria Refer�ncia para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
	}
	
	/**
	 * Metodo...: enviarRecargas
	 * Descricao: Preenche a tabela com as recargas a serem disponbilizadas para Conciliacao
	 * @param 	String	parametroData 	- data de processamento no formato dd/mm/aaaa
	 * @return	short 					- Sucesso(0) ou erro (diferente de 0)
	 * @throws	Exception
	 */
	public short atualizaLimites() throws Exception
	{
		super.log(Definicoes.INFO,"atualizaLimites","Inicio do atualizadorLimite");

		//Inicializa��o de vari�veis
		short retorno = 0;
		PREPConexao conexaoPrep = null;
		String dataInicialProcesso = null;

		try
		{
			// Pega a data/hora atual no formato DD/MM/AAAA HH:MM:SS
			dataInicialProcesso = GPPData.dataCompletaForamtada();
			
			//Pega conex�o com Banco de Dados
			conexaoPrep = gerenteBanco.getConexaoPREP(super.getIdLog());

			//Query que busca os �ltimos lan�amentos da TBL_INT_LIMITE_CREDITO
			String sqlBuscaInterface = "select cod_revenda_sap, vlr_limite_credito, " +
										"dat_atualizacao_mcr, ind_bloqueio " +
										"from tbl_int_limite_credito where idt_status_processamento = ? order by dat_atualizacao_mcr";
			
			Object[] paramBuscaInterface = {Definicoes.IND_NAO_PROCESSADO};
			ResultSet rsFetchInterface = conexaoPrep.executaPreparedQuery(sqlBuscaInterface, paramBuscaInterface, super.getIdLog());
			
			// Desliga o autocommit
			conexaoPrep.setAutoCommit(false);
			// Faz o update da TBL_REC_LIMITE_CREDITO com os �ltimos lan�amentos do SAP
			while(rsFetchInterface.next())
			{
				double vlrLimiteCredito = rsFetchInterface.getDouble("vlr_limite_credito");
				java.sql.Timestamp datAtualizacaoMcr = rsFetchInterface.getTimestamp("dat_atualizacao_mcr");
				String indBloqueio = rsFetchInterface.getString("ind_bloqueio");
				String codRrevendaSap = rsFetchInterface.getString("cod_revenda_sap");
				
				String sqlAtualizaCredito = "update tbl_rec_limite_credito "+
											"set vlr_limite_credito = ?, "+
											"dat_atualizacao_mcr = ?, "+
											"vlr_utilizado = 0, "+
											"ind_bloqueio = ? "+
											"where cod_revenda_sap = ?";
				
				Object[] paramAtualizaCredito = {	new Double(vlrLimiteCredito),
													datAtualizacaoMcr,
													indBloqueio,
													codRrevendaSap
												};
				conexaoPrep.executaPreparedUpdate(sqlAtualizaCredito, paramAtualizaCredito, super.getIdLog());
				
				// Atualiza status de processamento dos ultimos lan�amentos na interface
				String sqlAtualizaInterface = 	"update tbl_int_limite_credito "+
												"set idt_status_processamento = ? "+
												"where cod_revenda_sap = ? "+
												"and dat_atualizacao_mcr = ? ";
				
				Object[] paramAtualizaInterface = {	Definicoes.IND_PROCESSADO_GPP,
													codRrevendaSap,
													datAtualizacaoMcr
												};
				conexaoPrep.executaPreparedUpdate(sqlAtualizaInterface,paramAtualizaInterface, super.getIdLog());
				
				// Valida a altera��o referente ao novo input na tabela de interface
				conexaoPrep.commit();
			}
			conexaoPrep.setAutoCommit(true);
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			String dataFinalProcesso = GPPData.dataCompletaForamtada();
				
			//Logar inicio processo batch
			super.gravaHistoricoProcessos(Definicoes.IND_ATLZ_LIMITE_CREDITO, dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,e.getMessage(),GPPData.dataFormatada());
			super.log(Definicoes.ERRO,"atualizaLimites","Erro:"+e);
			throw new Exception(e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			//Pega data/hora final do processo batch
			String dataFinalProcesso = GPPData.dataCompletaForamtada();

			//Logar inicio processo batch
			super.gravaHistoricoProcessos(Definicoes.IND_ATLZ_LIMITE_CREDITO, dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Limites de Credito Atualizados",GPPData.dataFormatada());
		}
			
		super.log(Definicoes.INFO,"atualizaLimites","Fim");
		return retorno;
	}
}

