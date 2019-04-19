package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.entidade.HistoricoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pelas operacoes de persistencia relacionadas as promocoes.
 *
 *	@author	Daniel Ferreira
 *	@since	18/08/2005
 */
public class Operacoes extends Persistencia
{

    //Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
    public Operacoes(long idLog)
    {
        super(idLog, Definicoes.CL_PROMOCAO_PERSISTENCIA_OPERACOES);
    }
    
    //Metodos relacionados a promocoes de assinantes.
    
    /**
     *	Insere uma promocao de assinante na tabela TBL_PRO_ASSINANTE.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean inserePromocaoAssinante(PromocaoAssinante pAssinante, PREPConexao conexaoPrep) throws Exception
	{
        if(pAssinante != null)
        {
	        String sqlInsert =	"INSERT INTO 						" +
	        					"	TBL_PRO_ASSINANTE 				" +
						        "	(IDT_PROMOCAO,					" + 
						        " 	 IDT_MSISDN,					" + 
						        " 	 DAT_EXECUCAO,					" + 
						        " 	 DAT_ENTRADA_PROMOCAO,			" + 
						        " 	 DAT_INICIO_ANALISE,			" + 
						        " 	 DAT_FIM_ANALISE,				" + 
						        " 	 IND_ISENTO_LIMITE,				" + 
						        " 	 IDT_STATUS,					" +
						        "	 DAT_ULTIMO_BONUS)				" + 
	        					"VALUES (?,?,?,?,?,?,?,?,?)			";
	        
	        Object[] parametros =
	        {
	            new Integer(pAssinante.getPromocao().getIdtPromocao()),
	        	pAssinante.getIdtMsisdn(),
	        	(pAssinante.getDatExecucao() != null) ? new java.sql.Date(pAssinante.getDatExecucao().getTime()) : null,
	        	(pAssinante.getDatEntradaPromocao() != null) ? new java.sql.Timestamp(pAssinante.getDatEntradaPromocao().getTime()) : null,
	        	(pAssinante.getDatInicioAnalise() != null) ? new java.sql.Date(pAssinante.getDatInicioAnalise().getTime()) : null,
	    	    (pAssinante.getDatFimAnalise() != null) ? new java.sql.Date(pAssinante.getDatFimAnalise().getTime()) : null,
	    	    new Short(pAssinante.getIndIsentoLimite()),
	    	    new Integer(pAssinante.getStatus().getIdtStatus()),
	    	    (pAssinante.getDatUltimoBonus() != null) ? new java.sql.Timestamp(pAssinante.getDatUltimoBonus().getTime()) : null
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId) > 0) ? true : false;
        }
        
        return false;
	}
    
    /**
     *	Retira uma promocao do assinante da tabela TBL_PRO_ASSINANTE.
     *
     *	@param		idtPromocao				Identificador da promocao do assinante.
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean retiraPromocaoAssinante(Integer idtPromocao, String idtMsisdn, PREPConexao conexaoPrep) throws Exception
	{
	    String sqlDelete =	"DELETE FROM " +
	        				"  TBL_PRO_ASSINANTE " +
	        				"WHERE" +
	        				"  IDT_PROMOCAO = ? AND " +
	        				"  IDT_MSISDN = ? ";
	        
	    Object[] parametros =
	    {
	        idtPromocao,
	    	idtMsisdn
	    };
	        
	    return (conexaoPrep.executaPreparedUpdate(sqlDelete, parametros, super.logId) > 0) ? true : false;
	}
    
    /**
     *	Retira todas as promocoes do assinante da tabela TBL_PRO_ASSINANTE.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@throws		Exception
     */
	public boolean retiraPromocoesAssinante(String idtMsisdn, PREPConexao conexaoPrep) throws Exception
	{
        String sqlDelete =	"DELETE FROM " +
							"  TBL_PRO_ASSINANTE " +
							"WHERE" +
							"  IDT_MSISDN = ? ";

        Object[] parametros =
        {
            idtMsisdn
        };

        return (conexaoPrep.executaPreparedUpdate(sqlDelete, parametros, super.logId) > 0) ? true : false;
	}
    
    /**
     *	Atualiza o registro da promocao do assinante com os valores atualizados contidos no objeto.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		idtPromocao				Identificador da promocao do assinante.
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean atualizaPromocaoAssinante(String idtMsisdn, Integer idtPromocao, PromocaoAssinante pAssinante, PREPConexao conexaoPrep) throws Exception
	{
        if(pAssinante != null)
        {
	        String sqlUpdate =	"UPDATE                      " +
	        					"  TBL_PRO_ASSINANTE         " +
	        					"SET                         " +
	        					"  IDT_MSISDN           = ?, " +
	        					"  IDT_PROMOCAO         = ?, " +
	        					"  DAT_EXECUCAO         = ?, " +
	        					"  DAT_ENTRADA_PROMOCAO = ?, " +
	        					"  DAT_INICIO_ANALISE   = ?, " +
	        					"  DAT_FIM_ANALISE      = ?, " +
	        					"  IND_ISENTO_LIMITE    = ?, " +
	        					"  IDT_STATUS           = ?, " +
	        					"  DAT_ULTIMO_BONUS     = ?  " +
	        					"WHERE                       " +
	        					"  IDT_MSISDN   = ? AND      " +
	        					"  IDT_PROMOCAO = ?          ";
	        
	        Object[] parametros =
	        {
	            pAssinante.getIdtMsisdn(),
	            new Integer(pAssinante.getPromocao().getIdtPromocao()),
	            (pAssinante.getDatExecucao() != null) ? new java.sql.Date(pAssinante.getDatExecucao().getTime()) : null,
	            (pAssinante.getDatEntradaPromocao() != null) ? new java.sql.Timestamp(pAssinante.getDatEntradaPromocao().getTime()) : null,
	            (pAssinante.getDatInicioAnalise() != null) ? new java.sql.Date(pAssinante.getDatInicioAnalise().getTime()) : null,
	            (pAssinante.getDatFimAnalise() != null) ? new java.sql.Date(pAssinante.getDatFimAnalise().getTime()) : null,
	            new Short(pAssinante.getIndIsentoLimite()),
	    	    new Integer(pAssinante.getStatus().getIdtStatus()),
	    	    (pAssinante.getDatUltimoBonus() != null) ? new java.sql.Timestamp(pAssinante.getDatUltimoBonus().getTime()) : null,
	        	idtMsisdn,
	        	idtPromocao		        	
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
        }
	    
	    return false;
	}
		
    /**
     *	Troca o MSISDN das promocoes do assinante.
     *
     *	@param		idtMsisdnNovo			MSISDN novo do assinante.
     *	@param		idtMsisdnAntigo			MSISDN antigo do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean trocaMsisdnPromocoesAssinante(String idtMsisdnNovo, String idtMsisdnAntigo, PREPConexao conexaoPrep) throws Exception
	{
	    String sqlUpdate =	"UPDATE					" +
	    					"  TBL_PRO_ASSINANTE	" +
	    					"SET					" +
	    					"  IDT_MSISDN = ? 		" +
	    					"WHERE					" +
	    					"  IDT_MSISDN = ?		";
		        
	    Object[] parametros =
	    {
	        idtMsisdnNovo,
	        idtMsisdnAntigo
	    };
		        
	    return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
	}
		
    //Metodos relacionados a operacoes de recargas para o assinante.
	
    /**
     *	Atualiza o registro de bonus agendado na fila de recargas de acordo com o objeto passado por parametro. Devido
     *	a possiveis problemas de concorrencia com o processo de consumo da fila de recargas, o metodo NAO atualiza
     *	o status de processamento nem o codigo de retorno. Caso contrario, e possivel que o metodo volte o status
     *	do registro na fila de recargas para nao processado, gerando duplicacao de concessao de bonus.
     *
     *	@param		idtMsisdn					MSISDN do assinante.
     *	@param		tipTransacao				Tipo de Transacao da recarga.
     *	@param		datCadastro					Data de cadastro do registro na fila de recargas.
     *	@param		bonusAgendado				Objeto representando o registro do bonus agendado na fila de recargas.
     *	@param		conexaoPrep					Conexao com o banco de dados para execucao da operacao.
     *	@return									True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean atualizaFilaRecargas(String idtMsisdn, String tipTransacao, Timestamp datCadastro, FilaRecargas recarga, PREPConexao conexaoPrep) throws Exception
	{
	    if(recarga != null)
	    {
	        String sqlUpdate =
	            "UPDATE															" +
	            "	TBL_REC_FILA_RECARGAS										" +
	            "SET															" +
	        	"	IDT_MSISDN                 	=	?,							" +
	        	"	TIP_TRANSACAO              	=	?,							" +
	        	"	DAT_CADASTRO               	=	?,							" +
	        	"	DAT_EXECUCAO               	=	?,							" +
	        	"	DAT_PROCESSAMENTO          	=	?,							" +
	        	"	DES_MENSAGEM               	=	?,							" +
	        	"	TIP_SMS                    	=	?,							" +
	        	"	IND_ENVIA_SMS              	=	?,							" +
	        	"	VLR_CREDITO_PRINCIPAL      	=	?,							" +
	        	"	VLR_CREDITO_BONUS          	=	?,							" +
	        	"	VLR_CREDITO_SMS            	=	?,							" +
	        	"	VLR_CREDITO_GPRS           	=	?,							" +
	        	"	NUM_DIAS_EXP_PRINCIPAL     	=	?,							" +
	        	"	NUM_DIAS_EXP_BONUS         	=	?,							" +
	        	"	NUM_DIAS_EXP_SMS           	=	?,							" +
	        	"	NUM_DIAS_EXP_GPRS          	=	?,							" +
	        	"	IND_ZERAR_SALDO_BONUS      	=	?,							" +
	        	"	IND_ZERAR_SALDO_SMS        	=	?,							" +
	        	"	IND_ZERAR_SALDO_GPRS       	=	? 							" +
	            "WHERE															" +
	            "	IDT_MSISDN		=	?									AND	" +
	            "	TIP_TRANSACAO	=	?									AND	" +
	            "	DAT_CADASTRO	=	TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')		";		        
	        Object[] parametros =
	        {
	            recarga.getIdtMsisdn(),
	            recarga.getTipTransacao(),
	            recarga.getDatCadastro(),
	            recarga.getDatExecucao(),
	            recarga.getDatProcessamento(),
	            recarga.getDesMensagem(),
	            recarga.getTipSms(),
	            recarga.getIndEnviaSms(),
	            recarga.getVlrCreditoPrincipal(),
	            recarga.getVlrCreditoBonus(),
	            recarga.getVlrCreditoSms(),
	            recarga.getVlrCreditoGprs(),
	            recarga.getNumDiasExpPrincipal(),
	            recarga.getNumDiasExpBonus(),
	            recarga.getNumDiasExpSms(),
	            recarga.getNumDiasExpGprs(),
	            recarga.getIndZerarSaldoBonus(),
	            recarga.getIndZerarSaldoSms(),
	            recarga.getIndZerarSaldoGprs(),
	            idtMsisdn,
	            tipTransacao,
	            (datCadastro != null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datCadastro) : null 
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
	    }
	    
	    return false;
	}
	
    /**
     *	Retira o registro da fila de recargas.
     *
     *	@param		bonusAgendado			Objeto representando o registro do bonus agendado na fila de recargas.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean retiraFilaRecargas(FilaRecargas recarga, PREPConexao conexaoPrep) throws Exception
	{
	    if(recarga != null)
	    {
	        String sqlDelete =
	            "DELETE FROM tbl_rec_fila_recargas " +
	            " WHERE idt_msisdn = ? " +
	            "   AND tip_transacao = ? " +
	            "   AND dat_cadastro = to_date(?, 'DD/MM/YYYY HH24:MI:SS') ";		        
	        Object[] parametros =
	        {
	            recarga.getIdtMsisdn(),
	            recarga.getTipTransacao(),
	            new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(recarga.getDatCadastro())
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlDelete, parametros, super.logId) > 0) ? true : false;
	    }
	    
	    return false;
	}
	
	//Metodos relacionados a operacoes em tabelas da Promocao Pula-Pula.
	
    /**
     *	Insere um registro no historico da promocao Pula-Pula.
     *
     *	@param		historico				Historico da execucao da promocao Pula-Pula para o assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean insereHistoricoPulaPula(HistoricoPulaPula historico, PREPConexao conexaoPrep) throws Exception
	{
        if(historico != null)
        {
	        String sqlInsert =	"INSERT INTO                    " +
	        					"	TBL_PRO_HISTORICO_PULA_PULA " +
	        					"	(                           " +
	        					"	 IDT_MSISDN,                " +
	        					"	 IDT_PROMOCAO,              " +
	        			        "	 DAT_EXECUCAO,              " +
	        			        "	 NOM_OPERADOR,       		" +
	        			        "	 IDT_MOTIVO,        		" +
	        			        "	 DES_STATUS_EXECUCAO,       " +
	        			        "	 IDT_CODIGO_RETORNO,        " +
	        			        "	 VLR_CREDITO_BONUS          " +
	        			        "	)                           " +
	        			        "VALUES                         " +
	        			        "	(?, ?, ?, ?, ?, ?, ?, ?)	";
	        
	        Object[] parametros =
	        {
	            historico.getIdtMsisdn(),
	            historico.getIdtPromocao(),
	            historico.getDatExecucao(),
	            historico.getNomOperador(),
	            historico.getIdtMotivo(),
	            historico.getDesStatusExecucao(),
	            historico.getIdtCodigoRetorno(),
	            historico.getVlrCreditoBonus()
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId) > 0) ? true : false;
        }
	    
	    return false;
	}

    /**
     *	Insere registro de historico de estorno de bonus Pula-Pula para o assinante.
     *
     *	@param		estorno					Informacoes de historico de estorno.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean inserePromocaoEstornoPulaPula(PromocaoEstornoPulaPula estorno, PREPConexao conexaoPrep) throws Exception
	{
        if(estorno != null)
        {
	        String sqlInsert =
	            "INSERT INTO tbl_pro_estorno_pula_pula (idt_lote, " +
	            "                                       dat_referencia, " +
	            "                                       idt_msisdn, " +
	            "                                       idt_promocao, " +
	            "	                                    idt_numero_origem, " +
	            "                                       idt_origem, " +
	            "                                       dat_processamento, " +
	            "                                       vlr_expurgo, " +
	            "                                       vlr_expurgo_saturado, " +
	            "                                       vlr_estorno, " +
	            "                                       vlr_estorno_efetivo) " +
	            "VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
	        
	        Object[] parametros =
	        {
	        	estorno.getIdtLote(),
	            (estorno.getDatReferencia() != null) ? new java.sql.Date(estorno.getDatReferencia().getTime()) : null,
	            estorno.getIdtMsisdn(),
	            new Integer(estorno.getIdtPromocao()),
	            estorno.getIdtNumeroOrigem(),
	            estorno.getIdtOrigem(),
	            (estorno.getDatProcessamento() != null) ? new java.sql.Timestamp(estorno.getDatProcessamento().getTime()) : null,
	            new Double(estorno.getVlrExpurgo()),
	            new Double(estorno.getVlrExpurgoSaturado()),
	            new Double(estorno.getVlrEstorno()),
	            new Double(estorno.getVlrEstornoEfetivo())
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId) > 0) ? true : false;
        }
	    
	    return false;
	}

    /**
     *	Atualiza o registro de sumarizacao de informacoes para o calculo do bonus da promocao Pula-Pula para o assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datMes					Mes de analise de ligacoes recebidas.
     *	@param		totalizacao				Informacoes para o calculo do bonus para o assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean atualizarTotalizacaoPulaPula(String				idtMsisdn, 
												String				datMes, 
												TotalizacaoPulaPula	totalizacao, 
												PREPConexao			conexaoPrep) throws Exception
	{
        if(totalizacao != null)
        {
	        String sqlUpdate =
	            "UPDATE tbl_pro_totalizacao_pula_pula " +
	            "   SET idt_msisdn = ?, " +
	            "       dat_mes = ?, " +
	            "       num_segundos_total = NVL(?,0), " +
	            "       num_segundos_ff = NVL(?,0), " +
	            "       num_segundos_plano_noturno = NVL(?,0), " +
	            "       num_segundos_plano_diurno = NVL(?,0), " +
	            "       num_segundos_nao_bonificado = NVL(?,0), " +
	            "       num_segundos_durac_excedida = NVL(?,0), " +
	            "       num_segundos_expurgo_fraude = NVL(?,0), " +
	            "       num_segundos_estorno_fraude = NVL(?,0), " +
	            "       num_segundos_tup = NVL(?,0), " +
	            "       num_segundos_aigualb = NVL(?,0), " +
	            "       num_segundos_titular_transf = NVL(?,0), " +
	            "       num_segundos_ath = NVL(?,0), " +
	            "       num_segundos_movel_nao_brt = NVL(?,0), " +
	            "       num_segundos_falegratis = NVL(?,0), " +
	            "       num_segundos_bonus = NVL(?,0), " +
	            "       num_segundos_ct = NVL(?,0) " +
	            " WHERE idt_msisdn = ? " +
	            "   AND dat_mes = ? ";
	        
	        Object[] parametros =
	        {
	            totalizacao.getIdtMsisdn(),
	            totalizacao.getDatMes(),
	            totalizacao.getNumSegundosTotal(),
	            totalizacao.getNumSegundosFF(),
	            totalizacao.getNumSegundosNoturno(),
	            totalizacao.getNumSegundosDiurno(),
	            totalizacao.getNumSegundosNaoBonificado(),
	            totalizacao.getNumSegundosDuracaoExcedida(),
	            totalizacao.getNumSegundosExpurgoFraude(),
	            totalizacao.getNumSegundosEstornoFraude(),
	            totalizacao.getNumSegundosTup(),
	            totalizacao.getNumSegundosAIgualB(),
	            totalizacao.getNumSegundosTitularTransf(),
	            totalizacao.getNumSegundosATH(),
	            totalizacao.getNumSegundosMovelNaoBrt(),
	            totalizacao.getNumSegundosFaleGratis(),
	            totalizacao.getNumSegundosBonus(),
	            totalizacao.getNumSegundosCT(),
	            idtMsisdn,
	            datMes
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
        }
	    
	    return false;
	}

    /**
     *	Atualiza o registro de sumarizacao de informacoes para o calculo do bonus da promocao Pula-Pula para o assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datMes					Mes de analise de ligacoes recebidas.
     *	@param		totalizacao				Informacoes para o calculo do bonus para o assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean adicionarTotalizacaoPulaPula(String idtMsisdn, String datMes, TotalizacaoPulaPula totalizacao, PREPConexao conexaoPrep) throws Exception
	{
        if(totalizacao != null)
        {
	        String sqlUpdate =
	            "UPDATE tbl_pro_totalizacao_pula_pula " +
	            "   SET idt_msisdn = ?, " +
	            "       dat_mes = ?, " +
	            "       num_segundos_total = NVL(num_segundos_total,0) + NVL(?,0), " +
	            "       num_segundos_ff = NVL(num_segundos_ff,0) + NVL(?,0), " +
	            "       num_segundos_plano_noturno = NVL(num_segundos_plano_noturno,0) + NVL(?,0), " +
	            "       num_segundos_plano_diurno = NVL(num_segundos_plano_diurno,0) + NVL(?,0), " +
	            "       num_segundos_nao_bonificado = NVL(num_segundos_nao_bonificado,0) + NVL(?,0), " +
	            "       num_segundos_durac_excedida = NVL(num_segundos_durac_excedida,0) + NVL(?,0), " +
	            "       num_segundos_expurgo_fraude = NVL(num_segundos_expurgo_fraude,0) + NVL(?,0), " +
	            "       num_segundos_estorno_fraude = NVL(num_segundos_estorno_fraude,0) + NVL(?,0), " +
	            "       num_segundos_tup = NVL(num_segundos_tup,0) + NVL(?,0), " +
	            "       num_segundos_aigualb = NVL(num_segundos_aigualb,0) + NVL(?,0), " +
	            "       num_segundos_titular_transf = NVL(num_segundos_titular_transf,0) + NVL(?,0), " +
	            "       num_segundos_ath = NVL(num_segundos_ath,0) + NVL(?,0), " +
	            "       num_segundos_movel_nao_brt = NVL(num_segundos_movel_nao_brt,0) + NVL(?,0), " +
	            "       num_segundos_falegratis = NVL(num_segundos_falegratis,0) + NVL(?,0), " +
	            "       num_segundos_bonus = NVL(num_segundos_bonus,0) + NVL(?,0), " +
	            "       num_segundos_ct = NVL(num_segundos_ct,0) + NVL(?,0) " +
	            " WHERE idt_msisdn = ? " +
	            "   AND dat_mes = ? ";
	        
	        Object[] parametros =
	        {
	            totalizacao.getIdtMsisdn(),
	            totalizacao.getDatMes(),
	            totalizacao.getNumSegundosTotal(),
	            totalizacao.getNumSegundosFF(),
	            totalizacao.getNumSegundosNoturno(),
	            totalizacao.getNumSegundosDiurno(),
	            totalizacao.getNumSegundosNaoBonificado(),
	            totalizacao.getNumSegundosDuracaoExcedida(),
	            totalizacao.getNumSegundosExpurgoFraude(),
	            totalizacao.getNumSegundosEstornoFraude(),
	            totalizacao.getNumSegundosTup(),
	            totalizacao.getNumSegundosAIgualB(),
	            totalizacao.getNumSegundosTitularTransf(),
	            totalizacao.getNumSegundosATH(),
	            totalizacao.getNumSegundosMovelNaoBrt(),
	            totalizacao.getNumSegundosFaleGratis(),
	            totalizacao.getNumSegundosBonus(),
	            totalizacao.getNumSegundosCT(),
	            idtMsisdn,
	            datMes
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
        }
	    
	    return false;
	}

	/**
	 *	Atualiza a totalizacao Pula-Pula do assinante, marcando-a com informacoes de titularidade transferida.
	 *
	 *	@param		idtMsisdn				MSISDN do assinante.
	 *	@param		datMes					Mes de recebimento das ligacoes.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se a totalizacao foi atualizada e false caso contrario.
	 *	@throws		Exception
	 */
	public boolean transferirTitularidade(String idtMsisdn, String datMes, PREPConexao conexaoPrep) throws Exception
	{
		Consulta			consulta	= new Consulta(super.getIdLog());
		TotalizacaoPulaPula	totalizacao	= consulta.getTotalizacaoPulaPula(idtMsisdn, datMes, conexaoPrep);
		
		if(totalizacao != null)
			return this.transferirTitularidade(totalizacao, conexaoPrep);
		
		return false;
	}
	
	/**
	 *	Atualiza a totalizacao Pula-Pula do assinante, marcando-a com informacoes de titularidade transferida.
	 *
	 *	@param		totalizacao				Informacoes da totalizacao da promocao Pula-Pula do assinante.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se a totalizacao foi atualizada e false caso contrario.
	 *	@throws		Exception
	 */
	public boolean transferirTitularidade(TotalizacaoPulaPula totalizacao, PREPConexao conexaoPrep) throws Exception
	{
		String	idtMsisdn	= totalizacao.getIdtMsisdn();
		String	datMes		= totalizacao.getDatMes();
		Long	total		= totalizacao.getNumSegundosTotal();
		
		totalizacao.reset();
		totalizacao.setIdtMsisdn(idtMsisdn);
		totalizacao.setDatMes(datMes);
		totalizacao.setNumSegundosTotal(total);
		totalizacao.setNumSegundosTitularTransf(total);
		
		return this.atualizarTotalizacaoPulaPula(idtMsisdn, datMes, totalizacao, conexaoPrep);
	}
	
    /**
     *	Atualiza o registro de requisicao de estorno de bonus Pula-Pula por fraude para o assinante.
     *
     *	@param		datReferencia			Data de referencia das ligacoes recebidas.
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		idtNumeroOrigem			Numero de origem das ligacoes.
     *	@param		estorno					Informacoes de estorno de bonus Pula-Pula por fraude.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean atualizaInterfaceEstornoPulaPula(Date datReferencia, String idtMsisdn, String idtNumeroOrigem, InterfaceEstornoPulaPula estorno, PREPConexao conexaoPrep) throws Exception
	{
        if(estorno != null)
        {
	        String sqlUpdate =
	            "UPDATE tbl_int_estorno_pula_pula " +
	            "   SET idt_lote = ?, " +
	            "       dat_referencia = ?, " +
	            "       idt_msisdn = ?, " +
	            "       idt_numero_origem = ?, " +
	            "       idt_origem = ?, " +
	            "       dat_cadastro = ?, " +
	            "       dat_processamento = ?, " +
	            "       idt_status_processamento = ?, " +
	            "       idt_codigo_retorno = ? " +
	            " WHERE dat_referencia = to_date(?,'dd/mm/yyyy') " +
	            "   AND idt_msisdn = ? " +
	            "   AND idt_numero_origem = ? ";
	        
	        Object[] parametros =
	        {
	        	estorno.getIdtLote(),
	            (estorno.getDatReferencia() != null) ? new java.sql.Date(estorno.getDatReferencia().getTime()) : null,
	            estorno.getIdtMsisdn(),
	            estorno.getIdtNumeroOrigem(),
	            estorno.getIdtOrigem(),
	            estorno.getDatCadastro(),
	            estorno.getDatProcessamento(),
	            estorno.getIdtStatusProcessamento(),
	            new Integer(estorno.getIdtCodigoRetorno()),
	            (datReferencia != null) ? new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datReferencia) : null,
	            idtMsisdn,
	            idtNumeroOrigem
	        };
	        
	        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
        }
	    
	    return false;
	}

    //Metodos relacionados a CDR's de assinantes.
	
    /**
     *	Atualiza o registro de CDR de ligacao recebida pelo assinante.
     *
     *	@param		subId					MSISDN do assinante.
     *	@param		timestamp				Data/hora do CDR.
     *	@param		startTime				Horario de inicio do CDR.
     *	@param		callId					Identificador do outro numero que executou a ligacao.
     *	@param		transactionType			Tipo de transacao do CDR.
     *	@param		ffDiscount				Valor da flag do FFDiscount, utilizado para determinar o tipo de desconto da chamada recebida.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean atualizaFFDiscountCDR(String subId, Date timestamp, Long startTime, String callId, Long transactionType, Integer ffDiscount, PREPConexao conexaoPrep) throws Exception
	{
        String sqlUpdate =
            "UPDATE																" +
            "	TBL_GER_CDR														" +
            "SET																" +
            "	FF_DISCOUNT			=	?										" +
            "WHERE																" +
            "	SUB_ID				=	?									AND	" +
            "	TIMESTAMP			=	TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')	AND	" +
            "	START_TIME			=	?									AND	" +
            "	CALL_ID				=	?									AND	" +
            "	TRANSACTION_TYPE	=	?										";
        
        Object[] parametros =
        {
            ffDiscount,
            subId,
            (timestamp != null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(timestamp) : null,
            startTime,
            callId,
            transactionType
        };
        
        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
	}
	
    /**
     *	Atualiza o tipo de bonificacao dos CDR's de ligacoes bonificaveis para o assinante.
     *
     *	@param		subId					MSISDN do assinante.
     *	@param		idtPromocao				Identificador da promocao do assinante.
     *	@param		datInicio				Data inicial das ligacoes, inclusive.
     *	@param		datFim					Data final das ligacoes, NAO inclusive.
     *	@param		ffDiscount				Valor da flag do FFDiscount, utilizado para determinar o tipo de desconto da chamada recebida.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da operacao.
     *	@return								True se operacao foi executada com sucesso e false caso contrario.
     *	@throws		Exception
     */
	public boolean atualizaFFDiscountCDR(String subId, Integer idtPromocao, Date datInicio, Date datFim, Integer ffDiscount, PREPConexao conexaoPrep) throws Exception
	{
        String sqlUpdate =
            "UPDATE tbl_ger_cdr	c " +
            "   SET ff_discount	=	? " +
            " WHERE sub_id		=	? " +
            "   AND timestamp	>=	to_date(?, 'DD/MM/YYYY HH24:MI:SS') " +
            "   AND timestamp	<	to_date(?, 'DD/MM/YYYY HH24:MI:SS') " +
            "   AND EXISTS (SELECT 1 " +
            "                 FROM tbl_pro_transaction t " +
            "                WHERE t.transaction_type = c.transaction_type " +
            "                  AND t.idt_promocao = ?) " +
            "   AND EXISTS (SELECT 1 " +
            "                 FROM tbl_pro_rate_name r " +
            "                WHERE r.rate_name = c.tip_chamada " +
            "                  AND r.idt_promocao = ?) ";
        
        Object[] parametros =
        {
            ffDiscount,
            subId,
            new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datInicio),
            new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datFim),
            idtPromocao,
            idtPromocao
        };
        
        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
	}
	
	/**
	 * Atualiza somente o status do assinante em todas as promocoes pula pula existentes na qual
	 * este pode pertencer.
	 * @param msisdn
	 * @param status
	 * @param conexaoPrep
	 * @return
	 * @throws Exception
	 */
	public boolean atualizaStatus(String msisdn, int status, PREPConexao conexaoPrep) throws Exception
	{
		String sqlUpdate = "UPDATE TBL_PRO_ASSINANTE A " +
			                  "SET IDT_STATUS=? " +
			                "WHERE IDT_MSISDN=? " +
			                  "AND IDT_PROMOCAO IN (SELECT IDT_PROMOCAO " +
			                                         "FROM TBL_PRO_PROMOCAO P " +
			                                        "WHERE P.IDT_CATEGORIA = ?)";
		
		Object parametros[] = {new Integer(status), msisdn, new Integer(Definicoes.CATEGORIA_PULA_PULA)};
		return (conexaoPrep.executaPreparedUpdate(sqlUpdate, parametros, super.logId) > 0) ? true : false;
	}
	
	/**
	 * Por: Bernardo Vergne Dias
	 * Aprova um lote de estorno/expurgo pula-pula.
	 * @param lote
	 * @param conexaoPrep
	 * @return
	 * @throws Exception
	 */
	public boolean aprovarLote(String lote, PREPConexao conexaoPrep) throws Exception
	{
	    String sql = "UPDATE tbl_int_estorno_pula_pula "
				   + "SET idt_status_processamento = ? "
				   + "WHERE idt_lote = ? AND idt_status_processamento = ?";
	    
	    Object parametros[] = {"N",lote,"V"}; //###########################
	    return (conexaoPrep.executaPreparedUpdate(sql, parametros, super.logId) > 0) ? true : false;
	}
	

	/**
	 * Por: Bernardo Vergne Dias
	 * Rejeita um lote de estorno/expurgo pula-pula.
	 * @param lote
	 * @param conexaoPrep
	 * @return
	 * @throws Exception
	 */
	public boolean rejeitarLote(String lote, PREPConexao conexaoPrep) throws Exception
	{
	    String sql = "DELETE from tbl_int_estorno_pula_pula "
			   + "WHERE idt_lote = ? AND idt_status_processamento = ?";
	    
	    Object parametros[] = {lote,"V"}; //###########################
	    return (conexaoPrep.executaPreparedUpdate(sql, parametros, super.logId) > 0) ? true : false;
	}
}
