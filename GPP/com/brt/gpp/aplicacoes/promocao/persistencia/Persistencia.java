package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.sql.Timestamp;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pelas operacoes de persistencia relacionadas as promocoes.
 *
 *	@author	Daniel Ferreira
 *	@since	15/08/2005
 */
public abstract class Persistencia extends Aplicacoes
{
    
    /**
     *	Construtor da classe.
     *
     *	@param		idProcesso				Identificador do processo.
     *	@param		nomeClasse				Nome da classe.
     */
    protected Persistencia(long idProcesso, String nomeClasse)
    {
        super(idProcesso, nomeClasse);
    }
    
    /**
     *	Insere os eventos relacionados a promocoes na TBL_APR_EVENTOS.
     *
     *	@param		datAprovisionamento		Data e hora do evento.
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		tipOperacao				Nome da operacao executada.
     *	@param		idtAntigoCampo			Valor relacionado a operacao antes de ocorrer.
     *	@param		idtNovoCampo			Valor relacionado a operacao depois de ocorrer.
     *	@param		idtTarifa				Valor cobrado pela execucao da operacao.
     *	@param		idtMotivo				Identificador do motivo da operacao.
     *	@param		nomOperador				Nome do operador que executou a operacao.
     *	@param		desStatus				Status de execucao da operacao.
     *	@param		codRetorno				Codigo de retorno da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		True se operacao foi executada com sucesso e false caso contrario.
     */
	public void insereEvento(Date datAprovisionamento, 
							 String idtMsisdn, 
							 String tipOperacao, 
							 String idtAntigoCampo, 
							 String idtNovoCampo, 
							 Double idtTarifa, 
							 Integer idtMotivo, 
							 String nomOperador, 
							 String desStatus, 
							 Integer codRetorno, 
							 PREPConexao conexaoPrep)
	{
	    try
	    {
	        String sqlInsert =	"INSERT INTO TBL_APR_EVENTOS " +
						        "(DAT_APROVISIONAMENTO , " + 
						        " IDT_MSISDN           , " + 
						        " TIP_OPERACAO         , " + 
						        " IDT_ANTIGO_CAMPO     , " + 
						        " IDT_NOVO_CAMPO       , " + 
						        " IDT_TARIFA           , " + 
						        " IDT_MOTIVO           , " + 
						        " NOM_OPERADOR         , " + 
						        " DES_STATUS           , " + 
						        " COD_RETORNO)           " + 
	        					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	        
	        Object[] parametros =
	        {
	        	new Timestamp(datAprovisionamento.getTime()),
	        	idtMsisdn,
	        	tipOperacao,
	        	idtAntigoCampo,
	        	idtNovoCampo,
	        	idtTarifa,
	        	idtMotivo,
	        	nomOperador,
	        	desStatus,
	        	codRetorno
	        };
	        
	        conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId);
	    }
	    catch(Exception e)
	    {
	        super.log(Definicoes.WARN, 
	        		  "insereEvento", 
					  "MSISDN: " + idtMsisdn               + 
					  " - Operacao: " + tipOperacao        +
					  " - Valor antigo: " + idtAntigoCampo +
					  " - Valor novo: "   + idtNovoCampo   +
					  " - Motivo: "       + idtMotivo      +
					  " - Operador: "     + nomOperador    +
					  " - Retorno: "      + codRetorno     +
					  " - Nao foi possivel inserir o evento. Excecao: " + e);
	    }
	}

	 /**
     *	Insere os eventos relacionados a promocoes na TBL_PRO_EVENTOS.
     *
     *	@param		datAprovisionamento		Data e hora do evento.
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		tipOperacao				Nome da operacao executada.
     *	@param		idtMotivo				Identificador do motivo da operacao.
     *	@param		tipDocumento			Tipo de documento.
     *	@param		numDocumento			Numero do documento.     
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		result					True se operacao foi executada com sucesso e false caso contrario.
     */
	public boolean inserePromocaoEvento(Date datAprovisionamento, 
										String idtMsisdn, 
										String tipOperacao, 
	                            		Integer idtMotivo, 
										Integer tipDocumento, 
										String numDocumento, 
	                            		PREPConexao conexaoPrep)
	{
	    boolean result = false;
	    
	    super.log(Definicoes.DEBUG, "inserePromocaoEvento", "Inicio");
	    
	    try
	    {
	        String sqlInsert =	"INSERT INTO TBL_PRO_EVENTOS " +
						        " (IDT_MSISDN          , " + 
						        " TIP_DOCUMENTO        , " + 
						        " NUM_DOCUMENTO        , " + 
						        " IDT_MOTIVO           , " + 
						        " DAT_APROVISIONAMENTO , " +
						        " TIP_OPERACAO)           " +
						        "VALUES (?, ?, ?, ?, ?, ?) ";
	        
	        Object[] parametros =
	        {
	        	idtMsisdn,
	        	tipDocumento,
	        	numDocumento,
	        	idtMotivo,
	        	new Timestamp(datAprovisionamento.getTime()),
	        	tipOperacao
	        };
	        
	        result = (conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId) > 0) ? true : false;
	    }
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "insereEvento", "Excecao: " + e);
	        result = false;
	    }
	    finally
	    {
	        super.log(Definicoes.DEBUG, "inserePromocaoEvento", "Fim retorno " + result);
	    }
	    
	    return result;
	}
	
}
