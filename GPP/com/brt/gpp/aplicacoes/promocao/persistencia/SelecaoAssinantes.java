package com.brt.gpp.aplicacoes.promocao.persistencia;

//Imports Java.

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

//Imports GPP.

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Classe responsavel pela consulta e obtencao de informacao de assinantes no banco de dados para execucao do
 *	Gerente Promocao. Todos os assinantes selecionados para todos os tipos de execucao sao obtidos atraves de
 *	chamadas de metodos desta classe. 
 *
 *	@author	Daniel Ferreira
 *	@since	13/09/2005
 */
public class SelecaoAssinantes extends Aplicacoes
{

    private int			type;
    private PREPConexao	conexaoPrep;
    private ResultSet	result;
    
    //Statements SQL.
    
    private static final String SQL_ATIVACAO =
        "SELECT																			" +
        "	STATUS.MSISDN					AS MSISDN,									" +
        "	STATUS.DATA_ENTRADA				AS DATA_ENTRADA,							" +
        "	STATUS.STATUS_HOJE				AS STATUS,									" +
        "	STATUS.PLANO					AS PLANO,									" +
		"	FNC_FEZ_RECARGA(STATUS.MSISDN)	AS IND_RECARGA								" +
        "FROM                                                                           " +
        "	(                                                                           " +
        "		SELECT															        " +
        "			ASSINANTE_HOJE.SUB_ID 					AS MSISDN,			        " +
        "			ASSINANTE_HOJE.DAT_IMPORTACAO - 1		AS DATA_ENTRADA,	        " +
        "			ASSINANTE_HOJE.ACCOUNT_STATUS			AS STATUS_HOJE,	            " +
        "			NVL(ASSINANTE_ONTEM.ACCOUNT_STATUS, 0)	AS STATUS_ONTEM,            " +
        "			ASSINANTE_HOJE.PROFILE_ID				AS PLANO                    " +
        "		FROM															        " +
        "			TBL_APR_ASSINANTE_TECNOMEN	ASSINANTE_ONTEM,				        " +
        "			TBL_APR_ASSINANTE_TECNOMEN	ASSINANTE_HOJE					        " +
        "		WHERE															        " +
        "			ASSINANTE_ONTEM.SUB_ID (+)			=	ASSINANTE_HOJE.SUB_ID	AND	" +
        "			ASSINANTE_ONTEM.DAT_IMPORTACAO (+)	=	?						AND	" +
        "			ASSINANTE_HOJE.DAT_IMPORTACAO		=	?							" +
        "	)	   STATUS                                                               " +
        "WHERE                                                                          " +
        "	STATUS.STATUS_ONTEM	<=	?	AND												" + 
        "	STATUS.STATUS_HOJE	>=	?		                                             ";
    
    private static final String SQL_PENDENTE_RECARGA =
        "SELECT /*	+INDEX(P_ASSINANTE XPK1TBL_PRO_ASSINANTE)			" +
		"			USE_NL(ASSINANTE, P_ASSINANTE)*/					" + 
    	"	P_ASSINANTE.IDT_MSISDN					AS MSISDN,			" +
		"	P_ASSINANTE.DAT_INICIO_ANALISE			AS DATA_ENTRADA,	" +
		"	ASSINANTE.IDT_STATUS					AS STATUS,			" +
		"	ASSINANTE.IDT_PLANO_PRECO				AS PLANO,			" +
		"	FNC_FEZ_RECARGA(P_ASSINANTE.IDT_MSISDN)	AS IND_RECARGA		" +
		"FROM															" +
		"	TBL_APR_ASSINANTE	ASSINANTE,								" +
		"	TBL_PRO_ASSINANTE	P_ASSINANTE								" +
		"WHERE															" +
		"	P_ASSINANTE.IDT_MSISDN = ASSINANTE.IDT_MSISDN	AND			" +
		"	P_ASSINANTE.IDT_PROMOCAO = ?								";
    
    private static final String SQL_TROCA_PLANO_HIB_PRE =
		"SELECT															" +
        "	ASSINANTE.IDT_MSISDN					AS MSISDN,			" +
        "	ASSINANTE.DAT_ATIVACAO					AS DATA_ENTRADA,	" +
		"	ASSINANTE.IDT_STATUS					AS STATUS,			" +
		"	ASSINANTE.IDT_PLANO_PRECO				AS PLANO,			" +
		"	FNC_FEZ_RECARGA(ASSINANTE.IDT_MSISDN)	AS IND_RECARGA		" +
        "FROM															" +
        "	TBL_APR_ASSINANTE   ASSINANTE,								" +
        "	TBL_APR_EVENTOS     EVENTO,									" +
        "	TBL_GER_PLANO_PRECO PLANO_ANTIGO,							" +
        "	TBL_GER_PLANO_PRECO PLANO_NOVO								" +
        "WHERE															" +
        "	EVENTO.TIP_OPERACAO = ?									AND " +
		"	EVENTO.COD_RETORNO = ?									AND " +
        "	ASSINANTE.IDT_MSISDN = EVENTO.IDT_MSISDN				AND " +
        "	EVENTO.IDT_ANTIGO_CAMPO = PLANO_ANTIGO.IDT_PLANO_PRECO	AND " +
        "	EVENTO.IDT_NOVO_CAMPO = PLANO_NOVO.IDT_PLANO_PRECO		AND " +
        "	PLANO_ANTIGO.IDT_CATEGORIA = ?							AND " +
        "	PLANO_NOVO.IDT_CATEGORIA = ?							AND " +
        "	EVENTO.DAT_APROVISIONAMENTO >= ?						AND " +
        "	EVENTO.DAT_APROVISIONAMENTO <  ?							";
    
    //Constantes internas.
    
    public static final int ATIVACAO			= 0;
    public static final int PENDENTE_RECARGA	= 1;
    public static final int TROCA_PLANO_HIB_PRE	= 2;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		int						type						Tipo de selecao. Os tipos possiveis sao:
     *																		ATIVACAO: Assinantes que mudaram do status First Time User para Normal User na data de aprovisionamento.
     *																		PENDENTE_RECARGA: Assinantes que estao pendentes de primeira recarga para entrar na promocao.
     *																		TROCA_PLANO_HIB_PRE: Assinantes que trocaram de plano Hibrido para Pre-Pago na data de aprovisionamento.
     *	@param		long					idLog						Identificador de LOG.
     */
    public SelecaoAssinantes(int type, long idLog)
    {
        super(idLog, Definicoes.CL_PROMOCAO_PERSISTENCIA_SELECAO);
        
        this.type			= type;
        this.conexaoPrep	= null;
        this.result			= null;
    }
    
    /**
     *	Executa a consulta no banco de dados pelos assinantes selecionados.
     *
     *	@param		Date					dataExecucao				Data da ativacao.
     *	@throws		GPPInternalErrorException
     */
	public void execute(Date dataExecucao) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG, "execute", "Inicio");
	    
	    String sqlQuery = null;
	    Date diaAnterior = null;
	    Object[] parametros = null;
	    
	    try
	    {
	        if(dataExecucao != null)
	        {
	            //Calculando o dia anterior, para passagem de parametro em consultas com data.
	            Calendar calExecucao = Calendar.getInstance();
	            calExecucao.setTime(dataExecucao);
	            calExecucao.add(Calendar.DAY_OF_MONTH, -1);
	            diaAnterior = calExecucao.getTime();
	        }
		    
	        switch(this.type)
	        {
	        	case SelecaoAssinantes.ATIVACAO:
	        	{
	        	    sqlQuery =	SelecaoAssinantes.SQL_ATIVACAO;
	        
	        	    parametros		= new Object[4];
	    	        parametros[0]	= new java.sql.Date(diaAnterior.getTime());
	    	        parametros[1]	= new java.sql.Date(dataExecucao.getTime());
	    	        parametros[2]	= new Integer(Definicoes.FIRST_TIME_USER);
	    	        parametros[3]	= new Integer(Definicoes.NORMAL);
	    	        
	    	        break;
	        	}
	        	case SelecaoAssinantes.PENDENTE_RECARGA:
	        	{
	        	    sqlQuery =	SelecaoAssinantes.SQL_PENDENTE_RECARGA;
	        
	        	    parametros = new Object[1];
	        	    parametros[0] = new Integer(Definicoes.CTRL_PROMOCAO_PENDENTE_RECARGA);
	    	        
	    	        break;
	        	}
	        	case SelecaoAssinantes.TROCA_PLANO_HIB_PRE:
	        	{
	        	    sqlQuery =	SelecaoAssinantes.SQL_TROCA_PLANO_HIB_PRE;
	        
	        	    parametros		= new Object[6];
	    	        parametros[0]	= Definicoes.TIPO_APR_TROCA_PLANO;
	    	        parametros[1]	= new Integer(Definicoes.RET_OPERACAO_OK);
	    	        parametros[2]	= new Integer(Definicoes.CATEGORIA_HIBRIDO);
	    	        parametros[3]	= new Integer(Definicoes.CATEGORIA_PREPAGO);
	    	        parametros[4]	= new java.sql.Date(diaAnterior.getTime());
	    	        parametros[5]	= new java.sql.Date(dataExecucao.getTime());
	    	        
	    	        break;
	        	}
	        	default: throw new GPPInternalErrorException("Opcao invalida");
	        }
	        
	        this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
	        this.result = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);
	    }
	    catch(GPPInternalErrorException e)
	    {
	        super.log(Definicoes.ERRO, "execute", "Excecao interna do GPP: " + e);
	        super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
	        throw e;
	    }
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
	        super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
	        throw new GPPInternalErrorException("Excecao: " + e);
	    }
	    finally
	    {
	        super.log(Definicoes.DEBUG, "execute", "Fim");
	    }
	}
    
    /**
     *	Fecha a consulta e a conexao com o banco de dados.
     *
     *	@throws		GPPInternalErrorException
     */
	public void close() throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG, "close", "Inicio");
	    
	    try
	    {
	        if(this.result != null)
	        {
	            this.result.close();
	        }
	    }
	    catch(SQLException e)
	    {
	        super.log(Definicoes.ERRO, "close", "Excecao SQL: " + e);
	        throw new GPPInternalErrorException("Excecao SQL: " + e);
	    }
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "close", "Excecao: " + e);
	        throw new GPPInternalErrorException("Excecao: " + e);
	    }
	    finally
	    {
	        super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
	        super.log(Definicoes.DEBUG, "close", "Fim");
	    }
	}
    
    /**
     *	Busca o proximo assinante na lista.
     *
     *	@return		Assinante				result						Informacoes do assinante selecionado.
     *	@throws		GPPInternalErrorException
     */
	public Assinante next() throws GPPInternalErrorException
	{
        Assinante result = null;
        
	    super.log(Definicoes.DEBUG, "next", "Inicio");
	    
	    try
	    {
	        if(this.result.next())
	        {
	        	result = new Assinante();
	        	result.setMSISDN			(this.result.getString("MSISDN"));
	        	result.setStatusAssinante	(this.result.getShort("STATUS"));
	        	result.setPlanoPreco		(this.result.getShort("PLANO"));
	        	result.setNumRecargas		(this.result.getInt("IND_RECARGA"));
	        	result.setDataAtivacao		(this.result.getDate("DATA_ENTRADA"));
	        }
	    }
	    catch(SQLException e)
	    {
	        super.log(Definicoes.ERRO, "next", "Excecao SQL: " + e);
	        throw new GPPInternalErrorException("Excecao SQL: " + e);
	    }
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "next", "Excecao: " + e);
	        throw new GPPInternalErrorException("Excecao: " + e);
	    }
	    finally
	    {
	        super.log(Definicoes.DEBUG, "next", "Fim");
	    }
	    
	    return result;
	}
    
}
