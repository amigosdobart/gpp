package com.brt.gpp.aplicacoes.planoHibrido.expiracaoSaldoControle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
*
* Executa a consulta dos assinantes cuja expiração da 
* franquia ou do bônus expira na data de referência. 
* 
* @version	1.0
* @author	Marcelo Alves Araujo
* @since	08/06/2006
*
* @author	Joao Paulo Galvagni Junior
* @since	29/03/2008
* @modify	Alteracao da expiracao dos assinantes do Plano Controle,
* 			contemplando os novos lancamentos da Promocao do Dia das 
* 			Maes 2008 (Bonus On-Net, Off-Net e de Recarga)
*/
public class ExpiracaoSaldoControleProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	private int			numRegistros;
	private String		dataProcessamento;
	private String		statusProcesso;
	private PREPConexao conexaoBanco;
	private ResultSet	rsExpira;
	
	/**
	 * Construtor
	 * @param long - ID de log do processo
	 */
	public ExpiracaoSaldoControleProdutor(long idLog) 
	{
		super(idLog, Definicoes.CL_EXPIRACAO_SALDO_CONTROLE);
		numRegistros = 0;
		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	}
	
	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_EXPIRACAO_SALDO_CONTROLE;
	}
	
	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso() 
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return "Foram expirados " + numRegistros + " assinantes";
        
        return "Erro na execucao";
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
		return dataProcessamento;
	}
    
    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao() 
	{
		return conexaoBanco;
	}
    
    /**
	 * Este metodo realiza a verificacao da data
	 * @param String[] - Data de regerência
	 * @throws GPPInternalErrorException
	 */
	private void validarData(String[] params) throws GPPInternalErrorException
	{
		if (params == null || params.length == 0 || params[0] == null)
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try
		{
			sdf.parse(params[0]);
			dataProcessamento = params[0];
		}
		catch(ParseException pe)
		{
			super.log(Definicoes.ERRO, super.nomeClasse, "Data invalida ou nao esta no formato (dd/mm/aaaa). Erro: "+pe);
			throw new GPPInternalErrorException("Data invalida ou nao esta no formato (dd/mm/aaaa). Valor:"+params[0]);
		}
	}
	
    /**
     * Consulta os assinantes que expiram a franquia ou bônus na data de referência
     * @param String[] - Data de referência para consulta
     * @throws GPPInternalErrorException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
    public void startup(String[] params) throws GPPInternalErrorException
	{
    	// Valida o formato da data como dd/mm/aaaa
    	validarData(params);
    	
    	conexaoBanco = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    	
    	// Consulta as recargas 04005 concedidas há 60 dias (Controle Antigo) ou 30 dias (Controle Novo)
    	// Consulta os bônus 08004 (Bônus Controle) e 08003 (Bônus Bumerangue) que foram concedidos há 30 dias
		String sqlExpiracao	= "SELECT rr.idt_msisdn AS msisdn                                   			 " // Parametros
							+ "      ,? AS saldo_expirado                                      				 " // 1
							+ "      ,rr.vlr_saldo_final_principal-rr.vlr_credito_principal AS saldo_inicial " 
							+ "      ,ass.account_balance/100000 AS saldo_final                              "
							+ "      ,to_char(rr.dat_origem,'dd/mm/yyyy hh24:mi:ss') as data_recarga		 "
							+ "      ,nvl(aph.ind_novo_controle,1) as ind_novo_controle						 "
							+ "      ,rr.tip_transacao as tipo_transacao		 							 "
							+ "  FROM tbl_rec_recargas rr                                      				 "
							+ "      ,tbl_ger_plano_preco gpp                                  				 "
							+ "      ,tbl_apr_plano_hibrido aph                                              " 
							+ "      ,tbl_apr_assinante_tecnomen ass                         				 "
							+ " WHERE gpp.idt_categoria = ?                                    				 " // 2
							+ "   AND ass.sub_id = rr.idt_msisdn                                             " 
							+ "   AND ass.dat_importacao = TO_DATE(?,'dd/mm/yyyy')-1                         " // 3
							+ "   AND gpp.idt_plano_preco = rr.idt_plano_preco                               "
							+ "   AND aph.idt_msisdn = rr.idt_msisdn                                         "
							+ "   AND aph.ind_novo_controle = ?                                              " // 4
							+ "   AND rr.tip_transacao = ?                                                   " // 5
							+ "   AND rr.dat_origem >= TO_DATE(?,'dd/mm/yyyy')-?                             " // 6, 7
							+ "   AND rr.dat_origem <  TO_DATE(?,'dd/mm/yyyy')-?+1                           " // 8, 9
							+ "UNION ALL                                                                     "
							+ "SELECT rr.idt_msisdn AS msisdn                                                "
							+ "      ,? AS saldo_expirado                                                    " // 10
							+ "      ,rr.vlr_saldo_final_principal-rr.vlr_credito_principal AS saldo_inicial "
							+ "      ,ass.account_balance/100000 AS saldo_final                              "
							+ "      ,to_char(rr.dat_origem,'dd/mm/yyyy hh24:mi:ss') as data_recarga		 "
							+ "      ,nvl(aph.ind_novo_controle,1) as ind_novo_controle						 "
							+ "      ,rr.tip_transacao as tipo_transacao		 							 "
							+ "  FROM tbl_rec_recargas rr                                      				 "
							+ "      ,tbl_ger_plano_preco gpp                                  				 "
							+ "      ,tbl_apr_plano_hibrido aph                                              " 
							+ "      ,tbl_apr_assinante_tecnomen ass                         				 "
							+ " WHERE gpp.idt_categoria = ?                                                  " // 11
							+ "   AND ass.sub_id = rr.idt_msisdn                                             " 
							+ "   AND ass.dat_importacao = TO_DATE(?,'dd/mm/yyyy')-1                         " // 12
							+ "   AND gpp.idt_plano_preco = rr.idt_plano_preco                               "
							+ "   AND aph.idt_msisdn = rr.idt_msisdn                                         "
							+ "   AND aph.ind_novo_controle = ?                                              " // 13
							+ "   AND rr.tip_transacao = ?                                                   " // 14
							+ "   AND rr.dat_origem >= TO_DATE(?,'dd/mm/yyyy')-?                             " // 15, 16
							+ "   AND rr.dat_origem <  TO_DATE(?,'dd/mm/yyyy')-?+1                           " // 17, 18
							+ "UNION ALL                                                                     "
							+ "SELECT rr.idt_msisdn AS msisdn                                                       		"
							+ "      ,CASE                                                                          		"
							+ "         WHEN aph.ind_novo_controle = ? AND rr.tip_transacao = ? THEN 						" // 19, 20
							+ "         			?																		" // 21
							+ "         WHEN aph.ind_novo_controle = ? AND rr.tip_transacao = ? THEN						" // 22, 23
							+ "         			?																		" // 24
							+ "         ELSE                                                                        		"
							+ "         			?																		" // 25
							+ "       END AS saldo_expirado                                                         		"
							+ "      ,CASE                                                                          		"
							+ "         WHEN aph.ind_novo_controle = ? AND rr.tip_transacao = ? THEN 						" // 26, 27
							+ "         			NVL(rr.vlr_saldo_final_principal, 0)-NVL(rr.vlr_credito_principal, 0) 	"
							+ "         WHEN aph.ind_novo_controle = ? AND rr.tip_transacao = ? THEN						" // 28, 29
							+ "         			NVL(rr.vlr_saldo_final_periodico, 0)-NVL(rr.vlr_credito_periodico, 0) 	"
							+ "         ELSE                                                                        		"
							+ "            	NVL(rr.vlr_saldo_final_bonus, 0)-NVL(rr.vlr_credito_bonus, 0)           		"
							+ "         END AS saldo_inicial                                                        		"
							+ "      ,CASE                                                                          		"
							+ "         WHEN aph.ind_novo_controle = ? AND rr.tip_transacao = ? THEN 						" // 30, 31
							+ "         			NVL(ass.account_balance, 0)/100000										"
							+ "         WHEN aph.ind_novo_controle = ? AND rr.tip_transacao = ? THEN						" // 32, 33
							+ "         			NVL(ass.periodic_balance, 0)/100000										"
							+ "         ELSE                                                                        		"
							+ "         			NVL(ass.bonus_balance, 0)/100000										"
							+ "         END AS saldo_final                                                          		"
							+ "      ,TO_CHAR(rr.dat_origem,'dd/mm/yyyy hh24:mi:ss') as data_recarga                		"
							+ "      ,NVL(aph.ind_novo_controle, 1) as ind_novo_controle                            		"
							+ "      ,rr.tip_transacao as tipo_transacao                                            		"
							+ "  FROM tbl_rec_recargas rr                                      				 				"
							+ "      ,tbl_ger_plano_preco gpp                                  				 				"
							+ "      ,tbl_apr_plano_hibrido aph                                              				"
							+ "      ,tbl_apr_assinante_tecnomen ass                         				 				"
							+ " WHERE gpp.idt_categoria = ?																	" // 34
							+ "   AND ass.sub_id = rr.idt_msisdn                                                    		"
							+ "   AND ass.dat_importacao = TO_DATE(?, 'dd/mm/yyyy')-1										" // 35
							+ "   AND gpp.idt_plano_preco = rr.idt_plano_preco                                      		"
							+ "   AND aph.idt_msisdn = rr.idt_msisdn                                                		"
							+ "   AND rr.tip_transacao IN (?, ?, ?, ?, ?)													" // 36, 37, 38, 39, 40
							+ "   AND rr.dat_origem >= TO_DATE(?, 'dd/mm/yyyy')-?											" // 41, 42
							+ "   AND rr.dat_origem <  TO_DATE(?, 'dd/mm/yyyy')-?+1                							" ;// 43, 44
    	
		MapConfiguracaoGPP mapConfig = MapConfiguracaoGPP.getInstancia();
		if (mapConfig == null)
			super.log(Definicoes.WARN, "Produtor.startup", "Problemas Mapeamento das Configuracoes GPP.");
		
		Object[] param = 	{Definicoes.EXPIRA_SALDO_PRINCIPAL										  // 1
							,new Integer(Definicoes.CATEGORIA_HIBRIDO)								  // 2 
							,dataProcessamento                                                        // 3 
							,new Integer(Definicoes.IND_CONTROLE_NOVO)                                // 4 
							,Definicoes.RECARGA_FRANQUIA                                              // 5 
							,dataProcessamento                                                        // 6 
							,mapConfig.getMapValorConfiguracaoGPP("DIAS_EXPIRACAO_NOVO_CONTROLE")     // 7 
							,dataProcessamento                                                        // 8 
							,mapConfig.getMapValorConfiguracaoGPP("DIAS_EXPIRACAO_NOVO_CONTROLE")     // 9 
							,Definicoes.EXPIRA_SALDO_PRINCIPAL                                        // 10
							,new Integer(Definicoes.CATEGORIA_HIBRIDO)                                // 11
							,dataProcessamento                                                        // 12
							,new Integer(Definicoes.IND_CONTROLE_ANTIGO)                              // 13
							,Definicoes.RECARGA_FRANQUIA                                              // 14
							,dataProcessamento                                                        // 15
							,mapConfig.getMapValorConfiguracaoGPP("DIAS_EXPIRACAO_FRANQUIA_CONTROLE") // 16
							,dataProcessamento                                                        // 17
							,mapConfig.getMapValorConfiguracaoGPP("DIAS_EXPIRACAO_FRANQUIA_CONTROLE") // 18
							,new Integer(Definicoes.IND_CONTROLE_ANTIGO)                              // 19
							,Definicoes.RECARGA_FRANQUIA_BONUS                                        // 20
							,Definicoes.EXPIRA_SALDO_PRINCIPAL                                        // 21
							,new Integer(Definicoes.IND_CONTROLE_NOVO)								  // 22
							,Definicoes.AJUSTE_BONUS_OFF_NET										  // 23
							,Definicoes.EXPIRA_SALDO_PERIODICO										  // 24
							,Definicoes.EXPIRA_SALDO_BONUS										  	  // 25
							,new Integer(Definicoes.IND_CONTROLE_ANTIGO)                              // 26
							,Definicoes.RECARGA_FRANQUIA_BONUS                                        // 27
							,new Integer(Definicoes.IND_CONTROLE_NOVO)								  // 28
							,Definicoes.AJUSTE_BONUS_OFF_NET										  // 29										  
							,new Integer(Definicoes.IND_CONTROLE_ANTIGO)                              // 30
							,Definicoes.RECARGA_FRANQUIA_BONUS                                        // 31
							,new Integer(Definicoes.IND_CONTROLE_NOVO)								  // 32
							,Definicoes.AJUSTE_BONUS_OFF_NET										  // 33										  
							,new Integer(Definicoes.CATEGORIA_HIBRIDO)                                // 34
							,dataProcessamento														  // 35
							,Definicoes.RECARGA_FRANQUIA_BONUS                                        // 36
							,Definicoes.RECARGA_BONUS_CSP14                                           // 37
							,Definicoes.AJUSTE_BONUS_RECARGA										  // 38
							,Definicoes.AJUSTE_BONUS_ON_NET											  // 39
							,Definicoes.AJUSTE_BONUS_OFF_NET										  // 40
							,dataProcessamento														  // 41
							,mapConfig.getMapValorConfiguracaoGPP("DIAS_EXPIRACAO_NOVO_CONTROLE") 	  // 42
							,dataProcessamento														  // 43
							,mapConfig.getMapValorConfiguracaoGPP("DIAS_EXPIRACAO_NOVO_CONTROLE") 	  // 44
							};
	    
		rsExpira = conexaoBanco.executaPreparedQuery(sqlExpiracao, param, super.getIdLog());		
	}
    
    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws SQLException
	{
    	ExpiracaoSaldoControleVO expiracao = null;
        if(rsExpira.next())
        {
        	expiracao = new ExpiracaoSaldoControleVO();
        	
        	expiracao.setMsisdn(rsExpira.getString("msisdn"));
        	expiracao.setTipoSaldoExpirado(rsExpira.getString("saldo_expirado"));
        	expiracao.setSaldoInicial(rsExpira.getDouble("saldo_inicial"));
        	expiracao.setSaldoFinal(rsExpira.getDouble("saldo_final"));
        	expiracao.setDataRecarga(rsExpira.getString("data_recarga"));
        	expiracao.setIndNovoControle(rsExpira.getInt("ind_novo_controle"));
        	expiracao.setTipoTransacao(rsExpira.getString("tipo_transacao"));
        	expiracao.setDataReferencia(dataProcessamento);
        	
        	numRegistros++;
        }
        
		return expiracao;
	}
    
    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws SQLException
	{
    	rsExpira.close();
    	super.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.getIdLog());
	}
    
    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException() 
	{
	}
}