package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe Singleton responsavel pela consulta e obtencao de informacao de ligacoes a serem anuladas ou estornadas
 *	por suspeita de fraude.
 *
 *	@author			Daniel Ferreira
 *	@since			15/12/2005
 *	@review_author	Magno Batista Corrêa
 *	@review_date	16/05/2006
 */
public class SelecaoEstornoPulaPula
{
	
	/**
	 *	Statement SQL para obtencao de registros em status de validacao.
	 */
	private static final String SQL_SELECAO = 
		"SELECT idt_lote, " +
		"	    dat_referencia, " +
		"	    idt_msisdn, " +
		"	    idt_numero_origem, " +
		"	    idt_origem, " +
		"	    dat_cadastro, " +
		"	    dat_processamento, " +
		"	    idt_status_processamento, " +
		"	    idt_codigo_retorno " +
		"  FROM tbl_int_estorno_pula_pula " +
		" WHERE idt_status_processamento = ? " +
		"    OR (idt_status_processamento = ? " +
		"   AND	 idt_codigo_retorno IN (?,?,?,?,?)) " +
		" ORDER BY idt_msisdn ";
    
	/**
	 *	Statement SQL para obtencao de registros para processamento.
	 */
    private static final String SQL_VALIDACAO = 
    	"SELECT idt_lote, " +
    	"       dat_referencia, " +
    	"       idt_msisdn, " +
    	"       idt_numero_origem, " +
    	"       idt_origem, " +
    	"       dat_cadastro, " +
    	"       dat_processamento, " +
    	"       idt_status_processamento, " +
    	"       idt_codigo_retorno " +
    	"  FROM tbl_int_estorno_pula_pula " +
    	" WHERE idt_status_processamento = ? ";
    
	/**
	 *	Instancia do singleton.
	 */
    private static SelecaoEstornoPulaPula instance;
    
	/**
	 *	Identificacao da classe.
	 */
    private String nomeClasse;
    
	/**
	 *	Gerente de Pool de Banco de Dados para obtencao de conexoes.
	 */
    private GerentePoolBancoDados gerenteBancoDados;
    
	/**
	 *	Gerente de Pool de Log para operacoes de log.
	 */
    private GerentePoolLog logger;
    
	/**
	 *	Conexao com o banco de dados.
	 */
    private PREPConexao conexaoPrep;
    
	/**
	 *	Identificador do processo para log.
	 */
    private long idLog;

	/**
	 *	Result Set com registros para processamento.
	 */
    private ResultSet registros;
    
	/**
	 *	Flag interna do objeto para indicar se o Result Set esta aberto ou nao.
	 */
    private boolean isOpen;
    
	/**
	 *	Objeto sincronizador para garantir a exclusao mutua no acesso aos objetos internos.
	 */
    private Object mutex;

	/**
	 *	Registro auxiliar na geracao de lista para processamento por assinante.
	 */
    private InterfaceEstornoPulaPula estornoArmazenado = null;
    
    /**
     *	Construtor da classe.
     */
    private SelecaoEstornoPulaPula()
    {
        this.nomeClasse			= Definicoes.CL_PROMOCAO_PERSIST_SELECAO_ESTORNO;
        this.logger				= GerentePoolLog.getInstancia(SelecaoPulaPula.class);
        this.gerenteBancoDados	= GerentePoolBancoDados.getInstancia(0);
        this.conexaoPrep		= null;
        this.idLog				= 0;
        this.registros			= null;
        this.isOpen				= false;
        this.mutex				= new Object();
    }
    
    /**
     *	Retorna a instancia do singleton.
     *
     *	@return		Instancia do singleton.
     */
	public static SelecaoEstornoPulaPula getInstance()
	{
	    if(SelecaoEstornoPulaPula.instance == null)
	    {
	        SelecaoEstornoPulaPula.instance = new SelecaoEstornoPulaPula();
	    }
	    
	    return SelecaoEstornoPulaPula.instance;
	}
	
    /**
     *	Executa a consulta no banco de dados pelos assinantes selecionados.
     *
     *	@param		params					Lista de parametros para execucao da consulta.
     *	@param		idLog					Identificador de LOG.
     *	@throws		Exception
     */
	public synchronized void execute(String[] params, long idLog) throws Exception
	{
	    synchronized(this.mutex)
	    {
		    if(!this.isOpen)
		    {
		        this.idLog = idLog;
			    String sqlQuery = null;
			    this.log(Definicoes.DEBUG, "execute", "Inicio");
			    
			    try
			    {
			        //Obtendo os parametros e o statement da consulta.
			        Object[] parametros = null;
					if((params.length >= 1) && (params[0].equalsIgnoreCase(Definicoes.IDT_PROCESSAMENTO_VALIDACAO)))
					{
						parametros = new Object[]{Definicoes.IDT_PROCESSAMENTO_VALIDACAO};
						sqlQuery = SelecaoEstornoPulaPula.SQL_VALIDACAO;
					}
					else
					{
						sqlQuery = SelecaoEstornoPulaPula.SQL_SELECAO;
						parametros = new Object[]{Definicoes.IDT_PROCESSAMENTO_NOT_OK,
								  				  Definicoes.IDT_PROCESSAMENTO_ERRO,
												  new Integer(Definicoes.RET_ERRO_TECNICO),
												  new Integer(Definicoes.RET_CREDITO_INSUFICIENTE),
												  new Integer(Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO),
												  new Integer(Definicoes.RET_PROMOCAO_ASSINANTE_NAO_ATIVO),
												  new Integer(Definicoes.RET_PROMOCAO_PENDENTE_RECARGA)};
					}
			        
			        //Executando a consulta.
			        this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(this.idLog);
			        this.registros = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, this.idLog);
			        this.isOpen = true;
			    }
			    catch(Exception e)
			    {
			        this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, this.idLog);
			        throw e;
			    }
			    finally
			    {
			        this.log(Definicoes.DEBUG, "execute", "Fim");
			    }
		    }
		    else
		    {
		        throw new GPPInternalErrorException("Consulta ja esta aberta.");
		    }
	    }
	}
    
    /**
     *	Fecha a consulta e a conexao com o banco de dados.
     *
     *	@throws		Exception
     */
	public synchronized void close() throws Exception
	{
        synchronized(this.mutex)
        {
    	    this.log(Definicoes.DEBUG, "close", "Inicio");
    	    
    	    try
    	    {
    	        if(this.registros != null) 
    	        	this.registros.close();
    	    }
    	    finally
    	    {
    	        this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, this.idLog);
    	        this.log(Definicoes.DEBUG, "close", "Fim");
    	        this.idLog	= 0;
	            this.isOpen	= false;
    	    }
        }
	}
    
    /**
     *	Busca os proximos dados do MSISDN a ser processado.
     *
     *	@return		Registros de solicitacao de expurgo/estorno de ligacoes recebidas pelo assinante.
     *	@throws		Exception
     */
	public synchronized Object next() throws Exception
	{
        synchronized(this.mutex)
        {
        	ArrayList					result	= null;
        	InterfaceEstornoPulaPula	estorno	= null;
        	
        	this.log(Definicoes.DEBUG, "next", "Inicio");
        	
    	    try
    	    {
    	        if((this.isOpen))
    	        {
					if(this.estornoArmazenado != null)
					{
	    	        	result = new ArrayList();
						result.add(this.estornoArmazenado);
					}
					
    	        	while(this.registros.next())
    	            {
    	            	estorno = fillInterfaceEstornoPulaPula();
    	            	
    	            	if(this.estornoArmazenado == null)
    	            	{
    	    	        	result = new ArrayList();
    	    	        	this.estornoArmazenado = estorno;
    	            	}
    	            	
    	            	if(this.estornoArmazenado.getIdtMsisdn().equals(estorno.getIdtMsisdn()))
        		        	 result.add(estorno);
    	            	else
    	            	{
    	            		this.estornoArmazenado = estorno;
    	    	            return result;
    	            	}
    	            }
    	        	
    	        	this.estornoArmazenado = null;
    	        	
    	        	//Retorna uma Collection com a serie de entradas de um mesmo MSISDN.
    	            return result;
    	        }
    	    }
    	    finally
    	    {
    	    	this.log(Definicoes.DEBUG, "next", "Fim");
    	    }
    	    
    	    return null;
        }
	}
	
	/**
	 *	Preenche uma InterfaceEstornoPulaPula com os dados correntes no result (ResultSet).
	 * 
	 *	@return		InterfaceEstornoPulaPula com os dados do result
	 *	@throws		SQLException
	 */
    private InterfaceEstornoPulaPula fillInterfaceEstornoPulaPula() throws SQLException
	{
        InterfaceEstornoPulaPula estorno = new InterfaceEstornoPulaPula();
        
        estorno.setIdtLote(this.registros.getString("idt_lote"));
    	estorno.setDatReferencia(this.registros.getDate("dat_referencia"));
    	estorno.setIdtMsisdn(this.registros.getString("idt_msisdn"));
    	estorno.setIdtNumeroOrigem(this.registros.getString("idt_numero_origem"));
    	estorno.setIdtOrigem(this.registros.getString("idt_origem"));
    	estorno.setDatCadastro(this.registros.getTimestamp("dat_cadastro"));
    	estorno.setDatProcessamento(this.registros.getTimestamp("dat_processamento"));
    	estorno.setIdtStatusProcessamento(this.registros.getString("idt_status_processamento"));
    	estorno.setIdtCodigoRetorno(this.registros.getInt("idt_codigo_retorno"));
        
    	return estorno;
    }
    
    /**
     *	Indica se a consulta esta aberta.
     *
     *	@return		True se a consulta esta aberta e false caso contrario.
     */
	public boolean isOpen()
	{
	    return this.isOpen;
	}
	
    /**
     *	Registra mensagem de LOG.
     *
     *	@param		tipo					Tipo de mensagem.
     *	@param		metodo					Nome do metodo.
     *	@param		mensagem				Mensagem a ser registrada.
     */
	private void log(int tipo, String metodo, String mensagem)
	{
		this.logger.log(this.idLog, tipo, this.nomeClasse, metodo, mensagem);
	}
	
}
