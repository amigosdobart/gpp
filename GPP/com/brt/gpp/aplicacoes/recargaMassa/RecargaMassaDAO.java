package com.brt.gpp.aplicacoes.recargaMassa;

import java.sql.ResultSet;
import java.sql.Timestamp;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
 *	Classe responsavel pelas operacoes de persistencia relacionadas 'a recarga em massa.
 *
 *	@author	Bernardo Vergne Dias
 *  09/08/2007
 */
public class RecargaMassaDAO extends Aplicacoes
{	
	public RecargaMassaDAO(long aLogId) 
	{
		super(aLogId, Definicoes.CL_RECARGA_MASSA_DAO);
	}

	/**
     *	Retorna o status do assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		-1 se o assinante nao existir.
     *	@throws		Exception
     */
    public int getStatusAssinante(String idtMsisdn, PREPConexao conexaoPrep) throws Exception
    {
        int status = -1;
        ResultSet result = null;
        
        try
        {
            String sqlQuery =	"SELECT idt_status " +
            					"FROM tbl_apr_assinante " +
            					"WHERE idt_msisdn = ?";
            
            Object[] parametros = 
            {
            	idtMsisdn
            };
            
            result = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);
            
            if(result.next())
            {
            	status = result.getInt("idt_status");
            }
        }
        catch (Exception e)
        {
	        super.log(Definicoes.ERRO, "getStatusAssinante", "Erro ao consultar status de assinante. " + e);
	        throw e;
	    }
        finally
        {
            if(result != null) 
                result.close();
        }
        
        return status;
    }
    
    /**
     *	Grava na tabela TBL_INT_SOLICITACAO_RECARGA um registro do arquivo de lote
     *  de recarga em massa.
     *
     *	@param		registro				Registro a ser gravado.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@throws		Exception
     */
    public void insereSolicitacaoRecarga(RegistroLote registro, PREPConexao conexaoPrep) throws Exception
    {
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	
    	try
        {
    		MapConfiguracaoGPP config 	= MapConfiguracaoGPP.getInstance();
    		
        	Integer prioridade = new Integer(config.getMapValorConfiguracaoGPP("RECARGA_MASSA_NUM_PRIORIDADE"));
        	Integer diasExpiracao = new Integer(config.getMapValorConfiguracaoGPP("RECARGA_MASSA_DIAS_EXPIRACAO"));
        	
        	String sql = "INSERT INTO TBL_INT_SOLICITACAO_RECARGA (" +
						" num_lote, " +
						" idt_msisdn," +
						" dat_cadastro, " +
						" des_mensagem, " +
						" ind_envia_sms, " +
						" idt_status_processamento," +
						" vlr_credito_bonus," +
						" vlr_credito_sms," +
						" vlr_credito_gprs," +
						" num_dias_exp_bonus," +
						" num_dias_exp_sms, " +
						" num_dias_exp_gprs," +
						" num_prioridade)" +
						" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
			Object[] params = {
					registro.getLote(), 
					registro.getMsisdn(), 
					now,
					(registro.getMensagemSMS() == null) ? "" : registro.getMensagemSMS(),
					(registro.getMensagemSMS() == null) ? new Integer(0) : new Integer(1),
					new Integer(Definicoes.STATUS_RECARGA_MASSA_PENDENTE),
					registro.getVlrBonus(),
					registro.getVlrSm(),
					registro.getVlrDados(),
					diasExpiracao,
					diasExpiracao,
					diasExpiracao,
					prioridade};
	
			int res = conexaoPrep.executaPreparedUpdate(sql, params, super.logId);
		    if (res  <= 0) throw new Exception("Retorno de executaPreparedUpdate: " + res);

        }
        catch (Exception e)
        {
	        super.log(Definicoes.ERRO, "getStatusAssinante", "Erro ao inserir solicitacao de recarga em massa. " + e);
	        throw e;
	    }
    }
    
    public void rejeitarLote(String numLote, String usuario, PREPConexao conexaoPrep) throws Exception
	{
		try
		{
			String sql = " UPDATE TBL_INT_SOLICITACAO_RECARGA " +
					     " SET idt_status_processamento = ?," +
					     "     dat_processamento = ?," +
					     "     id_usuario = ? " +
					     " WHERE num_lote = ? AND idt_status_processamento = ? ";
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			Object[] params = {
					new Integer(Definicoes.STATUS_RECARGA_MASSA_REJEITADO),
					now,
					usuario,
					numLote,
					new Integer(Definicoes.STATUS_RECARGA_MASSA_PENDENTE)};
			
			int res = conexaoPrep.executaPreparedUpdate(sql, params, super.logId);
			if (res  <= 0) throw new Exception("retorno de executaPreparedUpdate = " + res);
			conexaoPrep.commit();
		}
		catch(Exception e)
		{
			String erro = (e.getMessage() == null) ? "" : e.getMessage();
			super.log(Definicoes.ERRO, "execute", "Erro ao rejeitar lote de recarga em massa. Lote: " + numLote
					+ ". Erro: " + erro);
			conexaoPrep.rollback();
			throw e;
		}
	}
    
    public void aprovarLote(String numLote, String usuario, PREPConexao conexaoPrep) throws Exception
	{
    	try
		{
    		MapConfiguracaoGPP config 	= MapConfiguracaoGPP.getInstance();
    		
        	String tipTransacao = config.getMapValorConfiguracaoGPP("RECARGA_MASSA_TIP_TRANSACAO");
        	String tipSMS = config.getMapValorConfiguracaoGPP("RECARGA_MASSA_TIP_SMS");
        	
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			// Insere o lote na fila de recargas
			String sql = " INSERT INTO TBL_REC_FILA_RECARGAS  (" +
						 "	 IDT_MSISDN,                                             	" +
						 "	 TIP_TRANSACAO,                                          	" +
						 "	 DAT_CADASTRO,                                           	" +
						 "	 DAT_EXECUCAO,                                           	" +
						 "	 VLR_CREDITO_BONUS,                                      	" +
						 "	 VLR_CREDITO_SMS,                                        	" +
						 "	 VLR_CREDITO_GPRS,                                       	" +
						 "	 NUM_DIAS_EXP_BONUS,                                     	" +
						 "	 NUM_DIAS_EXP_SMS,                                       	" +
						 "	 NUM_DIAS_EXP_GPRS,                                      	" +
						 "	 DES_MENSAGEM,                                           	" +
						 "	 TIP_SMS,                                                	" +
						 "	 IND_ENVIA_SMS,                                         	" +
						 "	 NUM_PRIORIDADE,                                         	" +
						 "	 IDT_STATUS_PROCESSAMENTO,                               	" +
						 "	 IND_ZERAR_SALDO_BONUS,                                  	" +
						 "	 IND_ZERAR_SALDO_SMS,                                    	" +
						 "	 IND_ZERAR_SALDO_GPRS                                    	" +
						 "	)(                                                          " +
						 "  SELECT 														" +
						 "   idt_msisdn,												" +
						 "   ?,															" +
						 "   ?,															" +
						 "   ?,															" +
						 "   vlr_credito_bonus,											" +
						 "   vlr_credito_sms,											" +
						 "   vlr_credito_gprs,											" +
						 "   num_dias_exp_bonus,										" +
						 "   num_dias_exp_sms, 											" +
						 "   num_dias_exp_gprs,											" +
						 "   des_mensagem, 												" +
						 "   ?, 														" +
						 "   ind_envia_sms, 											" +
						 "   num_prioridade, 											" +	
						 "   ?,															" +
						 "   0, 														" +
						 "   0, 														" +
						 "   0 															" +
						 " FROM TBL_INT_SOLICITACAO_RECARGA 							" +
						 " WHERE num_lote = ? AND idt_status_processamento = ? )		";

			Object[] params = {tipTransacao, now, now, tipSMS,
					new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA), 
					numLote, new Integer(Definicoes.STATUS_RECARGA_MASSA_PENDENTE) };
			
			int res = conexaoPrep.executaPreparedUpdate(sql, params, super.logId);
			
			Integer novoStatus = new Integer(Definicoes.STATUS_RECARGA_MASSA_APROVADO);
				
			// Verifica se ocorreu erro ao tentar inserir na fila de recarga
			if (res < 0)
				novoStatus = new Integer(Definicoes.STATUS_RECARGA_MASSA_ERRO);
			
			// Atualiza o status de processamento
			sql = " UPDATE TBL_INT_SOLICITACAO_RECARGA " +
					     " SET idt_status_processamento = ?," +
					     "     dat_processamento = ?," +
					     "     id_usuario = ? " +
					     " WHERE num_lote = ? AND idt_status_processamento = ? ";
			
			params = new Object[] {novoStatus, now, usuario, numLote,
					new Integer(Definicoes.STATUS_RECARGA_MASSA_PENDENTE)};
			
			res = conexaoPrep.executaPreparedUpdate(sql, params, super.logId);
			if (res <= 0) throw new Exception("retorno de executaPreparedUpdate = " + res);
			
			conexaoPrep.commit();
		}
		catch(Exception e)
		{
			String erro = (e.getMessage() == null) ? "" : e.getMessage();
			super.log(Definicoes.ERRO, "execute", "Erro ao aprovar lote de recarga em massa. Lote: " + numLote
					+ ". Erro: " + erro);
			conexaoPrep.rollback();
			throw e;
		}
	}
    
}
