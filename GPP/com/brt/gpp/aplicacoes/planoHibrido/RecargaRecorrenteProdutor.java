package com.brt.gpp.aplicacoes.planoHibrido;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *
 * Este arquivo refere-se a classe RecargaGeneva, responsavel pela implementacao da
 * logica de leitura e execucao de recargas de assinantes provenientes do Genevade
 *
 * <P> Versao:		1.0
 *
 * @autor  Camile Cardoso Couto
 * @since  26/03/2004
 * 
 * @modify Remodelagem produtor-consumidor
 * @autor  Gustavo Gusmao
 * @since  11/11/2005
 * 
 * @modify Inclusao da recarga para o Controle Natal 2007 (+ bonus SMS)
 * @author Joao Paulo Galvagni
 * @since  01/11/2007
 * 
 * @modify Alteracoes da bonificacao para o Dia Das Maes 2008, contemplando
 * 		   o Bonus On-Net, Off-Net e Bonus de Recarga. Para tanto, foi criada
 * 		   uma nova 'letra' (M) para identificar tais bonificacoes e alterada
 * 		   a tabela de interface da Recarga Recorrente para o envio desses
 * 		   valores de bonificacao de volta para o SAG 
 * @author Joao Paulo Galvagni
 * @since  25/03/2008
 */
public class RecargaRecorrenteProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	// Recursos utilizados no produtor
	private PREPConexao conexaoPrep;
	private ResultSet rsRecarga;
    
    public RecargaRecorrenteProdutor(long aLogId)
    {
        super(aLogId, Definicoes.CL_RECARGA_RECORRENTE);
    }
    
    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
    public void startup(String[] params) throws GPPInternalErrorException, SQLException
    {
        super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio do Processo");
		
        try
		{
		    //Seleciona conexao do pool Prep Conexao
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
		    // Atualiza TBL_APR_PLANO_HIBRIDO com acessos cujos CDRs de ativação (status 1->2)
		    // porventura não tenham chegado ao GPP
		    atualizaTabelaHibridos();
			
		    //Seleciona os registros que devem receber recarga			
		    String sqlRecarga = 
                " SELECT /*+RULE*/ IDT_MSISDN_PRE, TIP_ENVIO,        " +
                "        VLR_RECARGA, DAT_RECARGA, COD_FATURAMENTO,  " +
                "  COD_RECARGA, DAT_PROCESSAMENTO,                   " +
                "        nvl(b.ind_novo_controle,1) as TIP_CONTROLE, " +
		    	"        a.num_contrato as CONTRATO                  " +
		    	"   FROM TBL_INT_RECARGA_RECORRENTE a,               " +
                "        tbl_apr_plano_hibrido b                     " +
				"  WHERE IDT_STATUS_PROCESSAMENTO = ?                " +
				"    AND DAT_RECARGA <= TRUNC(SYSDATE)               " +
				"    AND a.idt_msisdn_pre = b.idt_msisdn (+)         " +
		    	"    AND A.TIP_ENVIO IN(?, ?, ?, ?, ?, ?)            " ;
		    
		    Object[] param = {Definicoes.IND_LINHA_DISPONIBILIZADA,
		                      Definicoes.TIPO_REC_RECORRENTE_PROGRAMADA,
		                      Definicoes.TIPO_REC_RECORRENTE_FRANQUIA,
		                      Definicoes.TIPO_REC_RECORRENTE_BONUS,
		                      Definicoes.TIPO_REC_RECORRENTE_FRANQUIA_BONUS,
		                      Definicoes.TIPO_REC_RECORRENTE_BONUS_SMS,
		                      Definicoes.TIPO_REC_RECORRENTE_MAES
		                      };
		    
		    rsRecarga = conexaoPrep.executaPreparedQuery(sqlRecarga, param, super.getIdLog());		     
		}
		catch (GPPInternalErrorException eGPP)
		{
			super.log(Definicoes.ERRO, "Produtor.startup", "Excecao Interna GPP: " + eGPP);
			this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException(eGPP.toString()); 
		}
    }
    
    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws Exception
    {
        RecargaRecorrenteVO recRecorrente = null;
        if(rsRecarga.next())
        {
			recRecorrente = new RecargaRecorrenteVO();
			recRecorrente.setCodRecarga(rsRecarga.getInt("COD_RECARGA"));
			recRecorrente.setCodFaturamento(new CodFaturamento(rsRecarga.getString("COD_FATURAMENTO")));
			recRecorrente.setDataRecarga(rsRecarga.getDate("DAT_RECARGA"));
			recRecorrente.setDataSolicitacao(rsRecarga.getDate("DAT_PROCESSAMENTO"));
			recRecorrente.setValorRecarga(rsRecarga.getDouble("VLR_RECARGA"));
			recRecorrente.setMsisdn(rsRecarga.getString("IDT_MSISDN_PRE"));
			recRecorrente.setTipoRecarga(rsRecarga.getString("TIP_ENVIO"));
			recRecorrente.setTipoControle(rsRecarga.getInt("TIP_CONTROLE"));
			recRecorrente.setContrato(rsRecarga.getString("CONTRATO"));
			numRegistros++;
        }
        return(recRecorrente);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws Exception
    {
    	try
    	{
    		this.limpaProcessoOk();
            rsRecarga.close();
    	}
        catch(Exception e)
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
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
    	// Exceções não tratadas
    }
    
    /**
	 * Metodo...: atualizaTabelaHibridos
	 * Descricao: Enquanto os CDRs de status 1 -> 2 não estiverem chegando normalmente,
	 * 				esse método de acochambração será rodado
     * @throws GPPInternalErrorException 
     * @throws SQLException 
	 */
    private void atualizaTabelaHibridos () throws GPPInternalErrorException, SQLException
	{		
		// BUSCA OS NOVOS HÍBRIDOS
		String sqlNovosHibridos = 	"SELECT ass.idt_msisdn as msisdn,ass.vlr_saldo_principal as saldo,ass.dat_ativacao as ativacao " +
									"  FROM tbl_apr_assinante ass, tbl_ger_plano_preco gpp " +
									" WHERE ass.idt_plano_preco = gpp.idt_plano_preco " +
									"   AND gpp.idt_categoria = 1 " +
									"   AND ass.idt_status IN ('2','3','4') " +
									"   AND NOT EXISTS (SELECT 1 " +
									"                     FROM tbl_apr_plano_hibrido " +
									"                    WHERE idt_msisdn = ass.idt_msisdn) ";
		
		ResultSet novosHibridos = conexaoPrep.executaPreparedQuery(sqlNovosHibridos, null, super.getIdLog());
		
		ControlePromocao controle = new ControlePromocao(super.getIdLog());
		
    	// Insere novos registros na tabela de assinantes híbridos
		String sqlInsereHibrido = 	"INSERT INTO tbl_apr_plano_hibrido " +
									"(idt_msisdn, vlr_cred_fatura, vlr_cred_carry_over, " +
									" vlr_saldo_inicial, dat_ciclo, num_mes_execucao, " +
									" dat_ultima_recarga_processada, ind_drop, dat_ativacao_gpp, " +
									" dat_ativacao_geneva, num_contrato, ind_novo_controle) "+
									"values " +
									"(?,34.9,0,?,null,0,sysdate,0,trunc(?),null,null,1)";
		
		Object[]  paramInsert = new Object[3];
		Date      dataAtual   = new Date();
		Timestamp dataTAtual  = new Timestamp(dataAtual.getTime());
		
		while(novosHibridos.next())
		{
			// Insere a promoção Pula-Pula Controle
			controle.inserePromocoesAssinante(novosHibridos.getString("msisdn"), dataAtual, dataTAtual, Definicoes.GPP_OPERADOR, Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO,conexaoPrep);
			
			// Parâmetros para inserção
			paramInsert[0] = novosHibridos.getString("msisdn");
			paramInsert[1] = new Double(novosHibridos.getDouble("saldo"));
			paramInsert[2] = novosHibridos.getDate("ativacao");
			// Insere o assinante na tabela de híbridos
			conexaoPrep.executaPreparedUpdate(sqlInsereHibrido,paramInsert,super.getIdLog());
		}
		novosHibridos.close();
	}
    
    /**
	 * Metodo...: limpaProcessoOk
	 * Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	 * 			  ou seja, os usuario que foram extraidos pelo ETI
	 * @param								
	 * @throws 	GPPInternalErrorException
     * @throws SQLException 
	 */	
	private void limpaProcessoOk() throws GPPInternalErrorException, SQLException
	{
		MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

		// Deleta da tabela os usuarios processados com sucesso
		String sqlLimpaRecargas =	"DELETE FROM TBL_INT_RECARGA_RECORRENTE " +
									" WHERE IDT_STATUS_PROCESSAMENTO = ? " +
									" AND dat_processamento < (sysdate - ?)";
		
		Object paramLimpaRecargas[] = {Definicoes.IDT_REC_RECORRENTE_PROCESSADO,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
		int numRegistro = conexaoPrep.executaPreparedUpdate(sqlLimpaRecargas,paramLimpaRecargas, super.getIdLog());
		
		// Seleciona os assinantes que foram excluídos da base de assinantes
		String sqlLimpaHibridos = 	"SELECT APH.IDT_MSISDN as msisdn FROM tbl_apr_plano_hibrido aph " +
									" WHERE NOT EXISTS (SELECT 1 " +
									"                     FROM tbl_apr_assinante ass " +
									"                     WHERE ass.idt_msisdn = aph.idt_msisdn)";

		ControlePromocao controle = new ControlePromocao(super.getIdLog());
		
		ResultSet antigosHibridos = conexaoPrep.executaPreparedQuery(sqlLimpaHibridos, null, super.getIdLog());
		
		// Retira a promoção Pula-Pula Controle
		while(antigosHibridos.next())
			controle.retiraPromocaoAssinante(antigosHibridos.getString("msisdn"), Definicoes.PROMOCAO_PULA_PULA_CONTROLE, new Timestamp (new Date().getTime()), Definicoes.GPP_OPERADOR, Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO,conexaoPrep);
		
		antigosHibridos.close();
		
		String removeHibridos = "DELETE FROM tbl_apr_plano_hibrido aph " +
								" WHERE NOT EXISTS (SELECT 1 " +
								"                     FROM tbl_apr_assinante ass " +
								"                     WHERE ass.idt_msisdn = aph.idt_msisdn)";
		
		// Remove o assinante da tabela de híbridos
		conexaoPrep.executaPreparedUpdate(removeHibridos,null,super.getIdLog());
		
		super.log(Definicoes.DEBUG, "Produtor.limpaProcessosOk", numRegistro + " registro(s) processado(s) pelo ETI deletado(s)");
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
    public int getIdProcessoBatch()
    {
        return Definicoes.IND_RECARGA_RECORRENTE;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso()
    {
        if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return "Foram processados " + numRegistros + " registros";
        
        return "Erro durante o processo";
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
    public String getStatusProcesso()
    {
        return statusProcesso;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
     */
    public void setStatusProcesso(String status)
    {
        statusProcesso = status;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
        return null;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
}