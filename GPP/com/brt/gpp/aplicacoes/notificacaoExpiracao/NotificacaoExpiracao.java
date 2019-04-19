package com.brt.gpp.aplicacoes.notificacaoExpiracao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
  *
  * Este arquivo refere-se a classe NotificacaoExpiracao, responsavel por notificar via mensagem
  * SMS assinantes cuja data de expiração dos saldos está proxima
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Marcos Castelo Magalhães
  * Data: 				04/08/2005
  *
  * Modificado por:		Magno Batista Corrêa
  * Data:				2006/09/06
  * Razao:				Evolução para novas tags de modificação; Corrigir select de assinantes
  *
  */
public final class NotificacaoExpiracao extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolTecnomen 	gerenteTecnomen = null; 	// Gerente de conexoes Tecnomen
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	private MapConfiguracaoGPP	mapConfig = null;				// Mapeamento da TBL_GER_CONFIG_GPP	
			     
	/**
	 * Metodo...: NotificacaoExpiracao
	 * Descricao: Contrutor
	 * @param long	logId	Id do Log
	 */
	 public NotificacaoExpiracao (long logId)
	 {
		super(logId, Definicoes.CL_NOTIFICACAO_EXPIRACAO);
		
		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
		
		//Obtem referencia ao gerente de Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		
		try
		{
			//Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				super.log(Definicoes.WARN, "NotificacaoExpiracao", "Problemas Mapeamento das Configurações GPP");
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "NotificacaoExpiracao", "Problemas Mapeamento das Configurações GPP");
		}
		
	 }

/**
	 * Metodo...: GravaNotificacaoExpiracao
	 * Descricao: Grava registro de notificacao de mensagem de SMS a ser enviada aos assinantes com saldos a expirar
	 * @return	boolean		- TRUE (Gravou mensagem) ou FALSE (Nao gravou mensagem)	
	 * @throws GPPInternalErrorException							
	 */
	public boolean GravaNotificacaoExpiracao (long idProcesso) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"GravaNotificacaoExpiracao","Inicio do método GravaNotificacaoExpiracao");
		boolean gravouMsg=true;
		PREPConexao conexaoPrep = null;
		int numLinhas = 0;
		int contador = 0;
		Date datExpPrincipal = null;
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
		boolean jaExecutado = false;
		String dataInicial = GPPData.dataCompletaForamtada(); // Data para registrar inicio do processo
		
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(idProcesso);
				
			datExpPrincipal = formatoData.parse(GPPData.dataCompletaForamtada());
		
			String sqle = " select * from tbl_ger_historico_proc_batch " +
			" where id_processo_batch = 53 " +
			" and idt_status_execucao = 'SUCESSO' " +
			" and dat_inicial_execucao between trunc(sysdate) and trunc(sysdate + 1) ";
			
			Object param[] = {};
			
			ResultSet rse = conexaoPrep.executaPreparedQuery(sqle, param, super.logId);
			
			if (!rse.next())
			{
						
				String sql = " SELECT IDT_MSISDN, DAT_EXPIRACAO_PRINCIPAL, VLR_SALDO_PRINCIPAL, VLR_SALDO_BONUS, VLR_SALDO_SM, VLR_SALDO_DADOS, EX.DES_MENSAGEM as MENSAGEM, EX.NUM_DIAS as N_DIAS, EX.NUM_PRIORIDADE as PRIORIDADE " +
				 " FROM TBL_APR_ASSINANTE ass, TBL_GER_NOTIFICACAO_EXPIRACAO ex, TBL_GER_PLANO_PRECO pl" +
				 " WHERE IDT_STATUS = ex.IDT_STATUS_ASSINANTE " +

				 " AND DAT_EXPIRACAO_PRINCIPAL >= trunc(SYSDATE + ex.NUM_DIAS) " +
				 " AND DAT_EXPIRACAO_PRINCIPAL <  trunc(SYSDATE + ex.NUM_DIAS + 1 ) " +

				 " AND ex.IND_ATIVO = 1 " +
				 " AND pl.IDT_PLANO_PRECO = ass.IDT_PLANO_PRECO " +
				 " AND pl.IDT_CATEGORIA = ?";
				
				Object parame[] = {new Integer(Definicoes.CATEGORIA_PREPAGO)};
				
	//			verifica quais msisdns estao com saldo a expirar
				ResultSet rs = conexaoPrep.executaPreparedQuery(sql, parame, super.logId);
							
				while (rs.next())
				{
					datExpPrincipal = rs.getDate("DAT_EXPIRACAO_PRINCIPAL");
				
					//substitui variaveis % da mensagem 
					String notificacaoSMSTemplate = rs.getString("MENSAGEM");
					String texto1 = GPPData.substituiTexto("%1", notificacaoSMSTemplate, rs.getString("N_DIAS"));
					String texto2 = GPPData.substituiTexto("%2", texto1, new SimpleDateFormat("dd/MM/yyyy").format(datExpPrincipal));
					String texto3 = GPPData.substituiTexto("%3", texto2, rs.getString("VLR_SALDO_PRINCIPAL"));
					String texto4 = GPPData.substituiTexto("%4", texto3, rs.getString("VLR_SALDO_BONUS"));
					String texto5 = GPPData.substituiTexto("%5", texto4, rs.getString("VLR_SALDO_SM"));
					
					String texto6 = GPPData.substituiTexto("%6", texto5, rs.getString("VLR_SALDO_DADOS"));
					String texto7 = GPPData.substituiTexto("%7", texto6, new SimpleDateFormat("dd/MM").format(datExpPrincipal));

					String msgNotificacaoSMS = texto7;
	
					String sql2 = " INSERT INTO TBL_GER_ENVIO_SMS " +
								" VALUES (SEQ_ENVIO_SMS.NEXTVAL, ?, ?, ?, sysdate, 1, null, ?) ";
						
					Object parametros[] = {rs.getString("IDT_MSISDN"), msgNotificacaoSMS, rs.getString("PRIORIDADE"), Definicoes.SMS_INF_STATUS};
					
					numLinhas = conexaoPrep.executaPreparedUpdate(sql2, parametros, super.logId);
					contador = contador + 1;
				}
	
				conexaoPrep.commit();
					if (numLinhas < 1)
						{
						gravouMsg = false;
						}			
			}
			else
			{
				super.log(Definicoes.WARN, "notificacaoExpiracao", "ja havia sido executado hoje com sucesso");
				statusProcesso = Definicoes.TIPO_OPER_ERRO;
				jaExecutado = true;
			}
		
		}
		catch(SQLException e)
		{
			super.log(Definicoes.ERRO,"gravaNotificacaoSMS","Erro interno. Excecao:"+e);
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			throw new GPPInternalErrorException(e.getMessage());
		} 
		
		catch (ParseException e) 
		{
			super.log(Definicoes.ERRO,"gravaNotificacaoSMS","Erro no Parse da Data. Excecao:"+e);
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			throw new GPPInternalErrorException(e.getMessage());
		}

		finally
		{
			String dataFinal = GPPData.dataCompletaForamtada();
			
			String descricao = (jaExecutado == true) ? ("Processo ja havia sido executado hoje") : ("Foram enviadas " + contador + " mensagens de notificacao de expiracao");

			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, idProcesso);
					
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			sdf.format(datExpPrincipal);
			
			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_NOTIFICACAO_EXPIRACAO, dataInicial, dataFinal, statusProcesso, descricao, dataFinal);
			super.log(Definicoes.INFO, "notificacaoExpiracao", "Fim");
		}

		super.log(Definicoes.INFO,"gravaNotificacaoSMS","Fim do metodo gravaNotificacaoSMS");
		return gravouMsg;
	}
}