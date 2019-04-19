package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Classe Singleton responsavel pela consulta e obtencao de informacao de assinantes da promocao Pula-Pula.
 *
 *	@author	Daniel Ferreira
 *	@since	22/09/2005
 */
public class SelecaoPulaPula
{

    /**
     *	Instancia do singleton.
     */
    private static SelecaoPulaPula instance;
    
    /**
     *	Identificador do processo alocador do objeto.
     */
    private long idProcesso;

    /**
     *	Result set com os registros para processamento.
     */
    private ResultSet registros;
    
    /**
     *	Sincronizador de acesso aos dados internos.
     */
    private Object mutex;
    
    /**
     *	Statement de obtencao de registros para processamento em modo DEFAULT.
     */
    private static final String SQL_DEFAULT =
        "SELECT assinante.idt_msisdn AS idt_msisdn " +
        "  FROM tbl_pro_assinante assinante, " +
        "       tbl_pro_promocao promocao," +
        "       tbl_pro_tipo_transacao tt_execucao " +
        " WHERE assinante.idt_promocao = promocao.idt_promocao" +
        "   AND tt_execucao.idt_promocao = promocao.idt_promocao " +
        "   AND SUBSTR(assinante.idt_msisdn,3,2) = ? " +
        "   AND assinante.dat_execucao <= ? " +
        "   AND promocao.idt_categoria = ? " +
        "   AND tt_execucao.tip_execucao = ? " +
        "   AND promocao.id_processo_batch = ? " +
		//PONTO DE ATENCAO: Verificar pela fila de recargas e nao pela tabela de recargas devido aos
		//agendamentos (Se a execucao na fila de recargas ainda nao tiver sido realizada, o registro na
		//fila sera duplicado).                                                      
        "   AND NOT EXISTS (SELECT 1 " +
        "                     FROM tbl_rec_fila_recargas bonus " +
        "                    WHERE bonus.idt_msisdn = assinante.idt_msisdn " +
        "                      AND bonus.tip_transacao = tt_execucao.tip_transacao " +
        "                      AND bonus.dat_execucao >= ? " +
        "                      AND bonus.dat_execucao <  ? " +
        "                      AND bonus.idt_status_processamento in ('" + Definicoes.STATUS_RECARGA_NAO_PROCESSADA	+ 
                                                                   "','" + Definicoes.STATUS_RECARGA_CONCLUIDA + 
                                                                   "','" + Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS + 
                                                                   "','" + Definicoes.STATUS_RECARGA_TESTE_PULA_PULA + 
                                                                   "')) ";
    
    /**
     *	Statement de obtencao de registros para processamento em modo REBARBA.
     */
    private static final String SQL_REBARBA =
        "SELECT assinante.idt_msisdn AS idt_msisdn " +
        "  FROM tbl_pro_assinante assinante, " +
        "       tbl_pro_promocao promocao, " +
        "       tbl_pro_tipo_transacao tt_execucao " +
        " WHERE assinante.idt_promocao = promocao.idt_promocao " +
        "   AND tt_execucao.idt_promocao = promocao.idt_promocao " +
        "   AND SUBSTR(assinante.idt_msisdn,3,2) = ? " +
        "   AND assinante.idt_promocao = ? " +
        "   AND tt_execucao.tip_execucao = ? " +
        "   AND promocao.id_processo_batch = ? " +
		//PONTO DE ATENCAO: Verificar pela fila de recargas e nao pela tabela de recargas devido aos
        //agendamentos (Se a execucao na fila de recargas ainda nao tiver sido realizada, o registro na
        //fila sera duplicado).                                                          
        "   AND NOT EXISTS (SELECT 1 " +
        "                     FROM tbl_rec_fila_recargas bonus " +
        "                    WHERE bonus.idt_msisdn = assinante.idt_msisdn " +
        "                      AND bonus.tip_transacao = tt_execucao.tip_transacao " +
        "                      AND bonus.dat_execucao >= ? " +
        "                      AND bonus.dat_execucao <  ? " +
        "                      AND bonus.idt_status_processamento in ('" + Definicoes.STATUS_RECARGA_NAO_PROCESSADA	+ 
                                                                   "','" + Definicoes.STATUS_RECARGA_CONCLUIDA + 
                                                                   "','" + Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS + 
                                                                   "','" + Definicoes.STATUS_RECARGA_TESTE_PULA_PULA + 
                                                                   "')) ";
    
    /**
     *	Statement de obtencao de registros para processamento em modo PARCIAL.
     */
    private static final String SQL_PARCIAL =
        "SELECT assinante.idt_msisdn as idt_msisdn " +
        "  FROM tbl_pro_assinante assinante, " +
        "       tbl_pro_promocao promocao, " +
        "       tbl_pro_tipo_transacao tt_execucao, " +
        "       tbl_pro_dia_execucao dia_execucao " +
        " WHERE assinante.idt_promocao = promocao.idt_promocao " +
        "   AND tt_execucao.idt_promocao = promocao.idt_promocao " +
        "   AND dia_execucao.idt_promocao = promocao.idt_promocao " +
        "   AND dia_execucao.tip_execucao = tt_execucao.tip_execucao " +
        "   AND dia_execucao.num_dia_entrada = TO_NUMBER(TO_CHAR(assinante.dat_entrada_promocao, 'DD')) " +
        "   AND SUBSTR(assinante.idt_msisdn,3,2) = ? " +
        "   AND promocao.idt_categoria = ? " +
        "   AND tt_execucao.tip_execucao = ? " +
        "   AND dia_execucao.num_dia_execucao <= ? " +
        "   AND promocao.id_processo_batch = ? " +
		//PONTO DE ATENCAO: Verificar pela fila de recargas e nao pela tabela de recargas devido aos           
        //agendamentos (Se a execucao na fila de recargas ainda nao tiver sido realizada, o registro na        
        //fila sera duplicado).                                                                                
        "   AND NOT EXISTS (SELECT 1 " +
        "                     FROM tbl_rec_fila_recargas bonus " +
        "                    WHERE bonus.idt_msisdn = assinante.idt_msisdn " +
        "                      AND bonus.tip_transacao = tt_execucao.tip_transacao " +
        "                      AND bonus.dat_execucao >= ? " +
        "                      AND bonus.dat_execucao <  ? " +
        "                      AND bonus.idt_status_processamento in ('" + Definicoes.STATUS_RECARGA_NAO_PROCESSADA	+ 
                                                                   "','" + Definicoes.STATUS_RECARGA_CONCLUIDA + 
                                                                   "','" + Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS + 
                                                                   "','" + Definicoes.STATUS_RECARGA_TESTE_PULA_PULA + 
                                                                   "')) ";

    
    /**
     *	Construtor da classe.
     */
    private SelecaoPulaPula()
    {
        this.idProcesso	= -1;
        this.registros	= null;
        this.mutex		= new Object();
    }
    
    /**
     *	Retorna a instancia do singleton.
     *
     *	@return		Instancia do singleton.
     */
	public static SelecaoPulaPula getInstance()
	{
	    if(SelecaoPulaPula.instance == null)
	        SelecaoPulaPula.instance = new SelecaoPulaPula();
	    
	    return SelecaoPulaPula.instance;
	}

    /**
     *	Realiza a alocacao do objeto para o processo requisitante. Esta acao e feita atraves da atualizacao do 
     *	identificador do processo. Caso seja diferente de -1 e diferente do valor informado por parametro, 
     *	significa que o objeto ja esta alocado para outro processo. E lancada entao a excecao informando que o 
     *	processo ja se encontra em execucao.
     *
     *	@param		idProcesso				Identificador do processo requisitante.
     *	@throws		GPPInternalErrorException
     */
	public void alocar(long idProcesso) throws GPPInternalErrorException
	{
		synchronized(this.mutex)
		{
			if((this.idProcesso != -1) && (this.idProcesso != idProcesso))
				throw new GPPInternalErrorException("Processo ja em execucao");
			
			this.idProcesso = idProcesso;
		}
	}
    
    /**
     *	Libera o objeto para novas alocacoes. Somente o processo que alocou o objeto pode desaloca-lo. Caso seja 
     *	invocado por um processo que nao o tenha alocado anteriormente, o metodo nao faz nada. 
     *
     *	@param		idProcesso				Identificador do processo requisitante.
     */
	public void liberar(long idProcesso)
	{
		synchronized(this.mutex)
		{
			if(this.idProcesso == idProcesso)
				this.idProcesso = -1;
		}
	}
	
    /**
     *	Realiza a autenticacao do processo requisitante, atraves do seu identificador, para verificar se o objeto 
     *	esta alocado para ele. Caso contrario, e lancada excecao indicando de acesso negado. 
     *
     *	@param		idProcesso				Identificador do processo requisitante.
     *	@throws		GPPInternalErrorException
     */
	private void autenticar(long idProcesso) throws GPPInternalErrorException
	{
		if(idProcesso != this.idProcesso)
			throw new GPPInternalErrorException("Acesso negado");
	}
	
    /**
     *	Executa a consulta no banco de dados pelos assinantes selecionados.
     *
     *	@param		tipoExecucao			Tipo de selecao. Os tipos possiveis sao:
     *											DEFAULT: Tipo de execucao default.
     *											REBARBA: Tipo de execucao default para os assinantes que ficaram para o ultimo dia de execucao.
     *											PARCIAL: Adiantamento do bonus para assinantes aptos a receberem no mes seguinte.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@param		promocao				Identificador da promocao. Valido somente para o tipo de execucao REBARBA. 
     *										Para outros tipo de execucao o parametro nao precisa ser informado.
     *	@param		idProcesso				Identificador do processo requisitante.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@throws		Exception
     */
	public void execute(String tipoExecucao, Date dataReferencia, int promocao, long idProcesso, PREPConexao conexaoPrep) throws Exception
	{
		//Executando a autenticacao.
		this.autenticar(idProcesso);
        
	    //Obtendo os objetos necessarios para a criacao dos parametros da consulta.
	    String sqlQuery = null;
	    Object[] parametros = null;
	    
        Calendar calMesAtual = Calendar.getInstance();
        calMesAtual.setTime(dataReferencia);
        Integer diaAtual = new Integer(calMesAtual.get(Calendar.DAY_OF_MONTH));
        calMesAtual.set(Calendar.DAY_OF_MONTH, calMesAtual.getActualMinimum(Calendar.DAY_OF_MONTH));
        
        Calendar calProximoMes = Calendar.getInstance();
        calProximoMes.setTime(dataReferencia);
        calProximoMes.set(Calendar.DAY_OF_MONTH, calProximoMes.getActualMinimum(Calendar.DAY_OF_MONTH));
        calProximoMes.add(Calendar.MONTH, 1);

        //Obtendo os parametros, que sao diferentes em funcao do tipo de execucao.
        if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT))
        {
    	    sqlQuery =	SelecaoPulaPula.SQL_DEFAULT;
    	    
    	    parametros = new Object[3];
    	    parametros[0] = new java.sql.Date(dataReferencia.getTime());
    	    parametros[1] = new Integer(Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA);
    	    parametros[2] = new Integer(Definicoes.IND_GERENTE_PULA_PULA);
        }
        else if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA))
        {
    	    sqlQuery =	SelecaoPulaPula.SQL_REBARBA;
    	    
    	    parametros = new Object[3];
    	    parametros[0] = new Integer(promocao);
    	    parametros[1] = new java.sql.Date(calMesAtual.getTimeInMillis());
    	    parametros[2] = new java.sql.Date(calProximoMes.getTimeInMillis());
        }
        else if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL))
        {
    	    sqlQuery =	SelecaoPulaPula.SQL_PARCIAL;
	        
    	    parametros = new Object[4];
    	    parametros[0] = new Integer(Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA);
    	    parametros[1] = tipoExecucao;
    	    parametros[2] = diaAtual;
    	    parametros[3] = new Integer(Definicoes.IND_GERENTE_PULA_PULA);
        }
        else
        {
        	throw new GPPInternalErrorException("Opcao invalida");
        }
        
        this.registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, this.idProcesso);
	}
    
    /**
     *	Fecha a consulta.
     *
     *	@param		idProcesso				Identificador do processo requisitante.
     *	@throws		Exception
     */
	public void close(long idProcesso) throws Exception
	{
		//Executando a autenticacao.
		this.autenticar(idProcesso);
        
		if(this.registros != null)
			this.registros.close();
	}
    
    /**
     *	Busca o proximo assinante na lista.
     *
     *	@param		idProcesso				Identificador do processo requisitante.
     *	@return		result					MSISDN do assinante.
     *	@throws		Exception
     */
	public synchronized String next(long idProcesso) throws Exception
	{
		//Executando a autenticacao.
		this.autenticar(idProcesso);
        
		if(this.registros.next())
			return this.registros.getString("idt_msisdn");
    	    
		return null;
	}
    
}
