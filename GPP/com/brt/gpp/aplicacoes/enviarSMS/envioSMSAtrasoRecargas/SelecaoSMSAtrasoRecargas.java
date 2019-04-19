package com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas;

//Imports Java.

import java.sql.ResultSet;
import java.util.TreeMap;
import java.util.Map;

//Imports GPP.

import com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas.AtrasoRecarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
 *	Classe Singleton responsavel pela consulta e obtencao de registros com atraso para consumo de recargas.
 *
 *	@author	Daniel Ferreira
 *	@since	16/03/2006
 */
public class SelecaoSMSAtrasoRecargas
{

    private static	SelecaoSMSAtrasoRecargas	instance;
    private 		ResultSet					result;
    private 		boolean						isOpen;
    private 		Object						mutex;
    
    //Statements SQL.
    
    private static final String SQL_RECARGAS =
        "(SELECT /*+ index(f xie2tbl_rec_fila_recargas)*/ " +
        "        rowid                                      AS row_id, " +
        "        to_char(idt_status_processamento)          AS idt_status_processamento, " +
        "        '" + Definicoes.PATTERN_FILA_RECARGAS + "' AS idt_origem, " +
        "        idt_msisdn                                 AS idt_msisdn, " +
        "        tip_transacao                              AS tip_transacao, " +
        "        null                                       AS cod_recarga, " +
        "        vlr_credito_bonus                          AS vlr_recarga " +
        "   FROM tbl_rec_fila_recargas f " +
        "  WHERE dat_execucao < sysdate " +
        "    AND tip_transacao in (" + Definicoes.PATTERN_TT_FILA_RECARGAS + ") " +
        "    AND idt_status_processamento = ? " +
        "    AND vlr_credito_bonus > 0 " +
        "    AND ind_envia_sms = 1) " +
        "UNION ALL " +
        "(SELECT /*+ index(r tbl_int_recarga_idx)*/ " +
        "        rowid                                    AS row_id, " +
        "        idt_status_processamento                 AS idt_status_processamento, " +
        "        '" + Definicoes.PATTERN_CONTROLE + "'    AS idt_origem, " +
        "        idt_msisdn_pre                           AS idt_msisdn, " +
        "        " + Definicoes.PATTERN_TT_CONTROLE + "   AS tip_transacao, " +
        "        cod_recarga                              AS cod_recarga, " +
        "        vlr_recarga                              AS vlr_recarga " +
        "   FROM tbl_int_recarga_recorrente r " +
        "  WHERE dat_processamento < sysdate " +
        "    AND idt_status_processamento = ?) ";
    
    private static final String SQL_CONFIRMACAO_ATRASO_FILA_RECARGAS = 
        "SELECT 1 " +
        "  FROM tbl_rec_fila_recargas " +
        " WHERE rowid = ? " +
        "   AND idt_status_processamento = ? ";
    
    private static final String SQL_CONFIRMACAO_ATRASO_CONTROLE = 
        "SELECT 1 " +
        "  FROM tbl_int_recarga_recorrente " +
        " WHERE rowid = ? " +
        "   AND idt_status_processamento = ? ";
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     */
    private SelecaoSMSAtrasoRecargas()
    {
        this.result	= null;
        this.isOpen	= false;
        this.mutex	= new Object();
    }
    
    //Metodos.
    
    /**
     *	Retorna a instancia do singleton.
     *
     *	@return		SelecaoSMSAtrasoRecargas instancia					Instancia do singleton.
     */
	public static SelecaoSMSAtrasoRecargas getInstance()
	{
	    if(SelecaoSMSAtrasoRecargas.instance == null)
	    {
	        SelecaoSMSAtrasoRecargas.instance = new SelecaoSMSAtrasoRecargas();
	    }
	    
	    return SelecaoSMSAtrasoRecargas.instance;
	}
	
    /**
     *	Executa a consulta no banco de dados pelas recargas em atraso.
     *
     *	@param		PREPConexao				conexaoPrep					Conexao com o banco de dados.
     *	@throws		Exception
     */
	public synchronized void execute(PREPConexao conexaoPrep) throws Exception
	{
	    synchronized(this.mutex)
	    {
		    if(this.isOpen)
		    {
		        //Se a consulta ja estiver aberta, significa que o processo esta em andamento e nao e permitido o
		        //inicio de uma nova instancia.
		        throw new GPPInternalErrorException("Consulta ja esta aberta.");
		    }
		    
		    //Obtendo o statement SQL.
		    String sqlQuery = this.getSqlQuery();
		    //Obtendo os parametros para a consulta.
			Object[] parametros =
			{
			    new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA),
			    Definicoes.IND_LINHA_DISPONIBILIZADA
			};
		        
			this.result = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, conexaoPrep.getIdProcesso());
			this.isOpen = true;
	    }
	}
    
    /**
     *	Fecha o Result Set de registros de recarga com atraso.
     *
     *	@throws		Exception
     */
	public synchronized void close() throws Exception
	{
        synchronized(this.mutex)
        {
    	    try
    	    {
    	        if(this.result != null)
    	        {
    	            this.result.close();
    	        }
    	    }
    	    finally
    	    {
	            this.isOpen = false;
    	    }
        }
	}
    
    /**
     *	Busca o proximo registro na lista.
     *
     *	@return		AtrasoRecarga			result						Registro com informacoes de atraso de recarga.
     *	@throws		Exception
     */
	public synchronized AtrasoRecarga next() throws Exception
	{
        synchronized(this.mutex)
        {
            AtrasoRecarga result = null;
            
	        if(this.isOpen)
	        {
    	        if(this.result.next())
    	        {
                    result = new AtrasoRecarga();
                    result.setRowId(this.result.getString("row_id"));
                    result.setIdtStatusProcessamento(this.result.getString("idt_status_processamento"));
                    result.setIdtOrigem(this.result.getString("idt_origem"));
                    result.setIdtMsisdn(this.result.getString("idt_msisdn"));
                    result.setTipTransacao(this.result.getString("tip_transacao"));
                    result.setCodRecarga(this.result.getString("cod_recarga"));
                    result.setVlrRecarga(this.result.getDouble("vlr_recarga"));
    	        }
	        }
    	    
    	    return result;
        }
	}
	
    /**
     *	Confirma se o consumo da recarga ainda esta em atraso.
     *
     *	@param		AtrasoRecarga			atraso						Informacoes referentes a recarga com atraso.
     *	@param		PREPConexao				conexaoPrep					Conexao com o banco de dados.
     *	@return		boolean												True se o atraso for confirmado e false caso contrario.
     */
	public static boolean confirmaAtraso(AtrasoRecarga atraso, PREPConexao conexaoPrep) throws Exception
	{
	    ResultSet resultConfirmacao = null;
	    
	    try
	    {
		    String sqlQuery = null;
		    
		    if(atraso.getIdtOrigem().equals(Definicoes.PATTERN_FILA_RECARGAS))
		    {
		        sqlQuery = SelecaoSMSAtrasoRecargas.SQL_CONFIRMACAO_ATRASO_FILA_RECARGAS;
		    }
		    else if(atraso.getIdtOrigem().equals(Definicoes.PATTERN_CONTROLE))
		    {
		        sqlQuery = SelecaoSMSAtrasoRecargas.SQL_CONFIRMACAO_ATRASO_CONTROLE;
		    }
		    
		    Object[] parametros =
		    {
		        atraso.getRowId(),
		        atraso.getIdtStatusProcessamento(),
		    };
		    
		    resultConfirmacao = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, conexaoPrep.getIdProcesso());
		    
		    return resultConfirmacao.next();
	    }
	    finally
	    {
	        if(resultConfirmacao != null)
	            resultConfirmacao.close();
	    }
	}
    
    /**
     *	Indica se a consulta esta aberta.
     *
     *	@return		boolean					isOpen						True se a consulta esta aberta e 
     *																	false caso contrario.
     */
	public boolean isOpen()
	{
	    return this.isOpen;
	}
	
    /**
     *	Retorna a instrucao SQL para execucao de consulta no banco de dados pelos registro de recarga com atraso.
     *
     *	@return		String					result						 Instrucao SQL para consulta.
     *	@throws		Exception
     */
	private String getSqlQuery() throws Exception
	{
	    String result = SelecaoSMSAtrasoRecargas.SQL_RECARGAS;
	    
	    //Configurando a consulta.
	    MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
	    Map tiposTransacao = this.parse(mapConfiguracao.getMapValorConfiguracaoGPP("ENVIO_SMS_ATRASO_RECARGAS_TT"));
	    result = result.replaceAll(Definicoes.PATTERN_TT_FILA_RECARGAS, (String)tiposTransacao.get(Definicoes.PATTERN_TT_FILA_RECARGAS));
	    result = result.replaceAll(Definicoes.PATTERN_TT_CONTROLE, (String)tiposTransacao.get(Definicoes.PATTERN_TT_CONTROLE));
	    
	    return result;
	}
	
    /**
     *	Interpreta a configuracao do GPP e retorna os tipos de transacao para a consulta, de acordo com a origem da recarga.
     *
     *	@param		String					configuracao				Configuracao do GPP contendo os tipos de transacao.
     *	@return		Map						result						Tipos de transacao de acordo com a origem da recarga.
     *	@throws		Exception
     */
	private Map parse(String configuracao) throws Exception
	{
	    TreeMap result = new TreeMap();
	    
	    String[] origens = configuracao.split("#");
	    
	    for(int i = 0; i < origens.length; i++)
	    {
	        String[] tiposTransacao = origens[i].split("::");
	        result.put(tiposTransacao[0], tiposTransacao[1]);
	    }
	    
	    return result;
	}
	
}
