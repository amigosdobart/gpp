package com.brt.gpp.aplicacoes.recarregar;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Classe responsavel pelas operacoes de acesso a banco 
 *  de dados referentes 'a fila de recargas.
 *
 *	@author	 Bernardo Vergne Dias
 *
 */
public class FilaRecargasDAO 
{
    private PREPConexao conexao;
    private long idProcesso;
    
	/**
	 *	Select de registros elegíveis para processamento na TBL_REC_FILA_RECARGAS.
	 */
	private static final String SEL_PROXIMOS_REGISTROS = 
        "SELECT /*+index(f xie2tbl_rec_fila_recargas)*/                               " +
        "        f.ROWID                                                 ,            " +
        "        f.ID_REGISTRO               as ID_REGISTRO              ,            " +
        "        f.ID_REGISTRO_DEPENDENCIA   as ID_REGISTRO_DEPENDENCIA  ,            " +
        "        f.IDT_MSISDN                as IDT_MSISDN               ,            " +
        "        f.TIP_TRANSACAO             as TIP_TRANSACAO            ,            " +
        "        f.DAT_CADASTRO              as DAT_CADASTRO             ,            " +
        "        f.DAT_EXECUCAO              as DAT_EXECUCAO             ,            " +
        "        f.DAT_PROCESSAMENTO         as DAT_PROCESSAMENTO        ,            " +
        "        f.VLR_CREDITO_PRINCIPAL     as VLR_CREDITO_PRINCIPAL    ,            " +
        "        f.VLR_CREDITO_BONUS         as VLR_CREDITO_BONUS        ,            " +
        "        f.VLR_CREDITO_SMS           as VLR_CREDITO_SMS          ,            " +
        "        f.VLR_CREDITO_GPRS          as VLR_CREDITO_GPRS         ,            " +
        "        f.NUM_DIAS_EXP_PRINCIPAL    as NUM_DIAS_EXP_PRINCIPAL   ,            " +
        "        f.NUM_DIAS_EXP_BONUS        as NUM_DIAS_EXP_BONUS       ,            " +
        "        f.NUM_DIAS_EXP_SMS          as NUM_DIAS_EXP_SMS         ,            " +
        "        f.NUM_DIAS_EXP_GPRS         as NUM_DIAS_EXP_GPRS        ,            " +
        "        f.DES_MENSAGEM              as DES_MENSAGEM             ,            " +
        "        f.TIP_SMS                   as TIP_SMS                  ,            " +
        "        f.IND_ENVIA_SMS             as IND_ENVIA_SMS            ,            " +
        "        f.IDT_STATUS_PROCESSAMENTO  as IDT_STATUS_PROCESSAMENTO ,            " +
        "        f.IDT_CODIGO_RETORNO        as IDT_CODIGO_RETORNO       ,            " +
        "        f.IND_ZERAR_SALDO_BONUS     as IND_ZERAR_SALDO_BONUS    ,            " +
        "        f.IND_ZERAR_SALDO_SMS       as IND_ZERAR_SALDO_SMS      ,            " +
        "        f.IND_ZERAR_SALDO_GPRS      as IND_ZERAR_SALDO_GPRS     ,            " +
        "        f.IND_ZERAR_SALDO_PERIODICO as IND_ZERAR_SALDO_PERIODICO,            " +
        "        f.NUM_PRIORIDADE            as NUM_PRIORIDADE           ,            " +
        "        f.VLR_CREDITO_PERIODICO     as VLR_CREDITO_PERIODICO    ,            " +
        "        f.NUM_DIAS_EXP_PERIODICO    as NUM_DIAS_EXP_PERIODICO   ,            " +
        "        f.TIP_OPERACAO              as TIP_OPERACAO             ,            " +
        "        f.IDT_RECARGA               as IDT_RECARGA              ,            " +
        "        f.IDT_NSU_INSTITUICAO       as IDT_NSU_INSTITUICAO      ,            " +
        "        f.DAT_CONTABIL              as DAT_CONTABIL             ,            " +
        "        f.NUM_HASH_CC               as NUM_HASH_CC              ,            " +
        "        f.IDT_CPF                   as IDT_CPF                  ,            " +
        "        f.IDT_TERMINAL              as IDT_TERMINAL             ,            " +
        "        f.TIP_TERMINAL              as TIP_TERMINAL                          " +
        "FROM TBL_REC_FILA_RECARGAS f                                                 " +
        "WHERE  f.IDT_STATUS_PROCESSAMENTO = ?                                        " +
        "   AND f.DAT_EXECUCAO <= SYSDATE                                             " +
        "   AND (f.id_registro_dependencia is null                                    " +
        "        OR (f.id_registro_dependencia is not null                            " +
        "            AND (select r.idt_status_processamento                           " +
        "                   from tbl_rec_fila_recargas r                              " +
        "                  where r.id_registro = f.id_registro_dependencia)  = 3      " +
        "           )                                                                 " +
        "       )                                                                     ";
	
	/**
	 *	Insert de registros na TBL_REC_FILA_RECARGAS.
	 */
	private static final String INS_FILA_RECARGAS = 
        "INSERT INTO TBL_REC_FILA_RECARGAS  " +
        "       (IDT_MSISDN               , " + // 1
        "        TIP_OPERACAO             , " + // 2
        "        DAT_CADASTRO             , " + // 3
        "        DAT_EXECUCAO             , " + // 4
        "        DAT_PROCESSAMENTO        , " + // 5
        "        IDT_STATUS_PROCESSAMENTO , " + // 6
        "        IDT_CODIGO_RETORNO       , " + // 7
        "        TIP_TRANSACAO            , " + // 8
        "        VLR_CREDITO_PRINCIPAL    , " + // 9
        "        VLR_CREDITO_BONUS        , " + // 10
        "        VLR_CREDITO_SMS          , " + // 11
        "        VLR_CREDITO_GPRS         , " + // 12
        "        VLR_CREDITO_PERIODICO    , " + // 13
        "        NUM_DIAS_EXP_PRINCIPAL   , " + // 14
        "        NUM_DIAS_EXP_BONUS       , " + // 15
        "        NUM_DIAS_EXP_SMS         , " + // 16
        "        NUM_DIAS_EXP_GPRS        , " + // 17
        "        NUM_DIAS_EXP_PERIODICO   , " + // 18
        "        IND_ZERAR_SALDO_BONUS    , " + // 19
        "        IND_ZERAR_SALDO_SMS      , " + // 20
        "        IND_ZERAR_SALDO_GPRS     , " + // 21
        "        IND_ZERAR_SALDO_PERIODICO, " + // 22
        "        TIP_SMS                  , " + // 23
        "        IND_ENVIA_SMS            , " + // 24
        "        DES_MENSAGEM             , " + // 25
        "        NUM_PRIORIDADE           , " + // 26
        "        IDT_RECARGA              , " + // 27
        "        IDT_NSU_INSTITUICAO      , " + // 28
        "        DAT_CONTABIL             , " + // 29
        "        NUM_HASH_CC              , " + // 30
        "        IDT_CPF                  , " + // 31
        "        IDT_TERMINAL             , " + // 32
        "        TIP_TERMINAL             , " + // 33
        "        ID_REGISTRO              , " + // 34
        "        ID_REGISTRO_DEPENDENCIA  ) " + // 35
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 *	Select de identificadores para um registro da fila de recarga.
	 */
	private static final String QRY_ID_REGISTRO = 
		"SELECT TRFR_SEQ.NEXTVAL AS id_registro " +
		"  FROM dual ";

    /**
     * Instancia da classe <code>FilaRecargasDAO</code> com uma conexao dada.
     * 
     * @param conexao Conexao a ser utilizada pelo DAO
     */
    public FilaRecargasDAO(PREPConexao conexao)
    {
        this.conexao = conexao;
        this.idProcesso = conexao.getIdProcesso();
    }
    
    /**
     * Insere um registro na fila de recargas.
     * 
     * @param  filaRecargas - Instancia de <code>FilaRecargas</code>
     * @throws GPPInternalErrorException
     */
    public void insereRecargaNaFila(FilaRecargas filaRecargas) throws GPPInternalErrorException
    {
        Object param[] = {filaRecargas.getIdtMsisdn()              , // 1
                          filaRecargas.getTipOperacao()            , // 2
                          filaRecargas.getDatCadastro()            , // 3
                          filaRecargas.getDatExecucao()            , // 4
                          filaRecargas.getDatProcessamento()       , // 5
                          filaRecargas.getIdtStatusProcessamento() , // 6
                          null                                     , // 7
                          filaRecargas.getTipTransacao()           , // 8
                          filaRecargas.getVlrCreditoPrincipal()    , // 9
                          filaRecargas.getVlrCreditoBonus()        , // 10
                          filaRecargas.getVlrCreditoSms()          , // 11
                          filaRecargas.getVlrCreditoGprs()         , // 12
                          filaRecargas.getVlrCreditoPeriodico()    , // 13
                          filaRecargas.getNumDiasExpPrincipal()    , // 14
                          filaRecargas.getNumDiasExpBonus()        , // 15
                          filaRecargas.getNumDiasExpSms()          , // 16
                          filaRecargas.getNumDiasExpGprs()         , // 17
                          filaRecargas.getNumDiasExpPeriodico()    , // 18
                          filaRecargas.getIndZerarSaldoBonus()     , // 19
                          filaRecargas.getIndZerarSaldoSms()       , // 20
                          filaRecargas.getIndZerarSaldoGprs()      , // 21
                          filaRecargas.getIndZerarSaldoPeriodico() , // 22
                          filaRecargas.getTipSms()                 , // 23
                          filaRecargas.getIndEnviaSms()            , // 24
                          filaRecargas.getDesMensagem()            , // 25
                          filaRecargas.getNumPrioridade()          , // 26
                          filaRecargas.getIdtRecarga()             , // 27
                          filaRecargas.getIdtNsuInstituicao()      , // 28
                          filaRecargas.getDatContabil()            , // 29
                          filaRecargas.getNumHashCC()              , // 30
                          filaRecargas.getIdtCpf()                 , // 31
                          filaRecargas.getIdtTerminal()            , // 32
                          filaRecargas.getTipTerminal()            , // 33
                          filaRecargas.getIdRegistro()             , // 34
                          filaRecargas.getIdRegistroDependencia()  };// 35
        
        // Executa a insercao na Fila de Recargas
        conexao.executaPreparedUpdate(INS_FILA_RECARGAS,param, idProcesso);
    }
    
    /**
     * Retorna o ResultSet com registros elegiveis para processamento.<br>
     * Caso nenhuma linha retorne entao o ponteiro para o ResultSet fica nulo.<br>
     *  
     * Obs: Esse metodo considera a dependencia entre os registros.
     *
     * @param      statusRecarga           Status do registro na Fila de Recargas.
     * @return     ResultSet da pesquisa no banco de dados.
     * @throws GPPInternalErrorException 
     */
    public ResultSet buscarRegistrosByStatus(String statusRecarga) throws GPPInternalErrorException
    {
        Object params[] = {statusRecarga};
        return conexao.executaPreparedQuery(SEL_PROXIMOS_REGISTROS,params, idProcesso);
    }

	/**
	 *	Retorna o proximo valor da sequence de identificadores da fila de recargas.
	 *
	 *	@return		Proximo valor da sequence de identificadores da fila de recargas.
	 *	@throws		Exception
	 */
	public Integer newIdRegistro() throws Exception
	{
		ResultSet registros = null;
		
		try
		{
			registros = conexao.executaQuery(FilaRecargasDAO.QRY_ID_REGISTRO, idProcesso);

			if(registros.next())
				return new Integer(registros.getInt("id_registro"));
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		
		return null;
	}
	
}
