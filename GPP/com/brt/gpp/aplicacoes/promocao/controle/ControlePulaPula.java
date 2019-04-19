package com.brt.gpp.aplicacoes.promocao.controle;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPulaFactory;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.CalculoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.HistoricoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoOrigemEstorno;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoTransacao;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.parser.PromocaoPulaPulaXMLParser;
import com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargasDAO;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
 *	Classe responsavel pela logica de negocio das operacoes relacionadas a Promocao Pula-Pula.
 *
 *	@version	1.0		19/09/2005		Primeira versao.
 *	@author		Daniel Ferreira
 *
 *  Atualizado por: Bernardo Vergne Dias (totalização FGN)
 *  12/07/2007
 *  
 *  Atualizado por: Bernardo Vergne Dias (bonificacao diferenciada CT)
 *  07/11/2007
 *  
 *	@version	1.4		14/03/2008		Diferenciacao de bonificacoes on-net e off-net.
 *	@author		Daniel Ferreira
 */
public class ControlePulaPula extends ControlePromocao
{
    
    //Constantes internas. Utilizadas para definir quais detalhes da promocao Pula-Pula do assinante devem ser
    //pesquisadas. E importante que as opcoes sejam escolhidas nesta ordem, uma vez que algumas opcoes dependem
    //da execucao de outras.
    
    /**
     *	Datas de credito de bonus Pula-Pula para todos os tipos de execucao.
     */
    public static final int	DATAS_CREDITO = 0;
    
    /**
     *	Totalizacao de:
     *   - ligacoes recebidas que geram bonus Pula-Pula
     *   - recargas efetuadas pelo assinante
     *   - Fale de Graca a Noite   
     */
    public static final int	TOTALIZACAO = 1;
    
    /**
     *	Valor de bonus Pula-Pula por minuto.
     */
    public static final int	BONUS_PULA_PULA = 2;
    
    /**
     *	Bonus Pula-Pula concedido no periodo.
     */
    public static final int	BONUS_CONCEDIDOS_PERIODO = 3;
    
    /**
     *	Bonus Pula-Pula agendados no periodo, se existir.
     */
    public static final int	BONUS_AGENDADOS_PERIODO = 4;
    
    /**
     *	Bonus Pula-Pula agendados em qualquer periodo.
     */
    public static final int	BONUS_AGENDADOS = 5;
    
    /**
     *	Valor de bonus Pula-Pula a ser concedido ao assinante.
     */
    public static final int	SALDO_PULA_PULA = 6;
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
    public ControlePulaPula(long idLog)
    {
        super(idLog, Definicoes.CL_PROMOCAO_CONTROLE_PULA_PULA);
    }
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     *	@param		nomeClasse				Nome da classe para identificacao de LOG.
     */
    public ControlePulaPula(long idLog, String nomeClasse)
    {
        super(idLog, nomeClasse);
    }
    
    /**
     *	Insere assinantes em promocoes Pula-Pula.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocao				Identificador da promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@return								Codigo de retorno da operacao.
     */
    public short inserePulaPula(String msisdn, int promocao, Date dataProcessamento, String operador, int motivo)
    {
        short		result		= -1;
        PREPConexao	conexaoPrep	= null;

        try
        {
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            //Executando consulta de assinante no GPP.
            Assinante assinante = super.consulta.getAssinanteGPPTecnomen(msisdn);
            Integer indRecarga = super.consulta.getIndRecarga(msisdn, conexaoPrep);
            assinante.setNumRecargas((indRecarga != null) ? indRecarga.intValue() : 0);
            //Obtendo objeto PromocaoAssinante para passagem como parametro para metodo de insercao.
            Integer idtPromocao = new Integer(promocao);
            Integer idtCodigoNacional = new Integer(msisdn.substring(2, 4));
            Integer idtPlanoPreco = new Integer(assinante.getPlanoPreco());
            PromocaoAssinante pAssinante = new PromocaoAssinante();
            pAssinante.setIdtMsisdn(msisdn);
            //Obtendo o objeto com as informacoes da promocao Pula-Pula.
            Promocao promocaoNova = super.consulta.getPromocao(idtPromocao);
            if((promocaoNova != null) && (promocaoNova.getCategoria() != null) &&  
               (promocaoNova.getCategoria().getIdtCategoria() == Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA))
            {
                pAssinante.setPromocao(promocaoNova);
            }
            pAssinante.setDatEntradaPromocao(dataProcessamento);
            pAssinante.setAssinante(assinante);
            pAssinante.setCodigoNacional(super.consulta.getPromocaoCodigoNacional(idtPromocao, idtCodigoNacional));
            pAssinante.setPlanoPreco(super.consulta.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco));
            pAssinante.setDiasExecucao(super.consulta.getPromocaoDiasExecucao(idtPromocao, dataProcessamento));
            //Obtendo as promocoes do assinante.
            Collection promocoesAssinante = super.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            
            result = super.inserePromocaoAssinante(pAssinante, promocoesAssinante, dataProcessamento, conexaoPrep);
            
            //Se o assinante ja possuir o Pula-Pula, deve ser retornado OK. Se o assinante possuir um Pula-Pula 
            //diferente da enviada pelo Clarify, o GPP deve efetuar uma troca de Pula-Pula para manter a consistencia
            //de bases.
            switch(result)
            {
            	case Definicoes.RET_PROMOCAO_ASSINANTE_JA_EXISTE:
            	    result = Definicoes.RET_OPERACAO_OK;
            	    break;
            	case Definicoes.RET_PROMOCAO_CATEGORIA_EXCLUSIVA:
            		super.log(Definicoes.WARN, 
            				  "inserePulaPula",
							  "MSISDN: "      + msisdn   + 
							  " - Promocao: " + promocao + 
							  " - Assinante ja possui promocao Pula-Pula. Efetuando troca para a nova promocao.");
            	    result = this.trocaPulaPula(msisdn, 
            	    							promocao, 
												dataProcessamento, 
												operador, 
												Definicoes.CTRL_PROMOCAO_MOTIVO_PROMOCAO_NOK, 
												conexaoPrep);
            	    break;
            	default: break;
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "inserePulaPula", 
					  "MSISDN: " + msisdn + " - Promocao: " + promocao + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.operacoes.insereEvento(dataProcessamento,
            							 msisdn,
										 Definicoes.TIPO_APR_PROMOCAO_ENTRADA,
										 null,
										 String.valueOf(promocao),
										 new Double(0.0),
										 new Integer(motivo),
										 operador,
										 ((result == Definicoes.RET_OPERACAO_OK) || 
										  (result == Definicoes.RET_PROMOCAO_PENDENTE_RECARGA)) ? 
										  	Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
										 new Integer(result),
										 conexaoPrep);
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.INFO, 
            		  "inserePulaPula", 
					  "MSISDN: "    + msisdn     + 
					  " - Promocao: " + promocao + 
					  " - Operador: " + operador + 
					  " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /**
     *	Retira a promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@return		Codigo de retorno da operacao.
     */
    public short retiraPulaPula(String msisdn, Date dataProcessamento, String operador, int motivo)
    {
        short		result		= -1;
        int			promocao	= -1;
        PREPConexao	conexaoPrep	= null;
        
        try
        {
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            //Obtendo a promocao Pula-Pula do assinante.
            Collection promocoesAssinante = super.consulta.getPromocoesAssinante(msisdn, null, conexaoPrep);
            for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
            {
                PromocaoAssinante pAssinante = (PromocaoAssinante)iterator.next();
                Promocao promocaoAntiga = (pAssinante != null) ? pAssinante.getPromocao() : null;
                if((promocaoAntiga != null) && (promocaoAntiga.getCategoria() != null))
                {
                    //Se a promocao for Pula-Pula, atualizar o parametro para passagem da promocao na chamada do metodo de retirada.
                    int categoria = promocaoAntiga.getCategoria().getIdtCategoria();
                    if(categoria == Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA)
                    {
                        promocao = promocaoAntiga.getIdtPromocao();
                        break;
                    }
                }
            }
            
            result = super.retiraPromocaoAssinante(msisdn, promocao, dataProcessamento, operador, motivo, conexaoPrep);
            //Caso o assinante nao possua Pula-Pula, e necessario retornar operacao OK.
            if(result == Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE)
            {
            	super.log(Definicoes.WARN,
            			  "retiraPulaPula",
						  "MSISDN: " + msisdn + " - Assinante nao possui promocao Pula-Pula.");
                result = Definicoes.RET_OPERACAO_OK;
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "retiraPulaPula", 
					  "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.INFO, 
            		  "retiraPulaPula", 
					  "MSISDN: "      + msisdn   + 
					  " - Operador: " + operador + 
					  " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /**
     *	Efetua a troca de promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocaoNova			Identificador da nova promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocaPulaPula(String msisdn, int promocaoNova, Date dataProcessamento, String operador, int motivo)
    {
        short		result		= -1;
        PREPConexao	conexaoPrep	= null;
        
        super.log(Definicoes.DEBUG, "trocaPulaPula", "MSISDN: " + msisdn + " - Promocao Nova: " + promocaoNova);
        
        try
        {
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            result = this.trocaPulaPula(msisdn, promocaoNova, dataProcessamento, operador, motivo, conexaoPrep);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPulaPula", 
					  "MSISDN: " + msisdn + " - Promocao Nova: " + promocaoNova + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.INFO, 
            		  "trocaPulaPula", 
            		  "MSISDN: "           + msisdn       + 
					  " - Promocao Nova: " + promocaoNova + 
					  " - Operador: "      + operador     + 
					  " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }

    /**
     *	Efetua a troca de promocao Pula-Pula do assinante a partir do PPP.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocaoNova			Identificador da nova promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		tipoDocumento			Tipo de documento.
     *	@param		numDocumento			Numero do documento.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocaPulaPula(String msisdn, 
    						   int promocaoNova, 
							   Date dataProcessamento, 
							   String operador, 
							   int motivo, 
							   int tipoDocumento, 
							   String numeroDocumento)
    {
        short		result		= -1;
        PREPConexao	conexaoPrep	= null;
        
        try
        {
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            result = this.trocaPulaPula(msisdn, promocaoNova, dataProcessamento, operador, motivo, conexaoPrep);
            super.operacoes.inserePromocaoEvento(dataProcessamento, 
            									 msisdn, 
												 Definicoes.TIPO_APR_PROMOCAO_TROCA, 
												 new Integer(motivo), 
												 new Integer(tipoDocumento), 
												 numeroDocumento, 
												 conexaoPrep);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPulaPula", 
					  "MSISDN: " + msisdn + " - Promocao Nova: " + promocaoNova + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.INFO, 
          		  	  "trocaPulaPula", 
					  "MSISDN: "           + msisdn       + 
					  " - Promocao Nova: " + promocaoNova + 
					  " - Operador: "      + operador     + 
					  " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /**
     *	Efetua a troca de promocao Pula-Pula do assinante para a versao atual.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		dataReferencia			Data de referencia para obtencao da promocao Pula-Pula.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocaPulaPula(String msisdn, Date dataReferencia, Date dataProcessamento, String operador, int motivo)
    {
        short		result		= -1;
        int			idtPromocao	= -1;
        PREPConexao	conexaoPrep	= null;
        
        try
        {
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            //Obtendo a lista de promocoes para a data de referencia.
            Collection promocoes = super.consulta.getPromocoes(dataReferencia);

            //Percorrendo a lista em busca da promocao Pula-Pula.
            for(Iterator iterator = promocoes.iterator(); iterator.hasNext();)
            {
                Promocao promocao = (Promocao)iterator.next();
                
                if((promocao != null) && (promocao.getCategoria() != null) &&
                   (promocao.getCategoria().getIdtCategoria() == Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA))
                {
                    idtPromocao = promocao.getIdtPromocao();
                    break;
                }
            }
            
            result = this.trocaPulaPula(msisdn, idtPromocao, dataProcessamento, operador, motivo, conexaoPrep);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPulaPula", 
					  "MSISDN: " + msisdn + " - Data de Referencia: " + dataReferencia + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.DEBUG, 
            		  "trocaPulaPula", 
            		  "MSISDN: "                + msisdn         + 
					  " - Data de Referencia: " + dataReferencia + 
					  " - Operador: "           + operador       + 
					  " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }

    /**
     *	Efetua a troca de promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocaoNova			Identificador da nova promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocaPulaPula(String msisdn, 
    						   int promocaoNova, 
							   Date dataProcessamento, 
							   String operador, 
							   int motivo, 
							   PREPConexao conexaoPrep)
    {
        short				result				= -1;
        Assinante			assinante			= null;
        PromocaoAssinante	pAssinanteNova		= null;
        PromocaoAssinante	pAssinanteAntiga	= null;
        Integer				idtPromocaoNova		= null;
        Integer				idtPromocaoAntiga	= null;
        
        try
        {
            //Executando consulta do assinante na plataforma.
            assinante = super.consulta.getAssinanteGPPTecnomen(msisdn);
            Integer indRecarga = this.consulta.getIndRecarga(msisdn, conexaoPrep);
            assinante.setNumRecargas((indRecarga != null) ? indRecarga.intValue() : 0);

            //Obtendo os objetos necessarios para a consulta de informacoes da promocao do assinante.
            idtPromocaoNova				= new Integer(promocaoNova);
            idtPromocaoAntiga			= new Integer(-1);
            Integer	idtCodigoNacional	= Integer.valueOf(msisdn.substring(2, 4));
            Integer idtPlanoPreco		= new Integer(assinante.getPlanoPreco());
            
            //Obtendo a lista de promocoes do assinante.
            Collection promocoesAssinante = super.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            
            //Percorrendo a lista de promocoes do assinante para localizar a promocao antiga. A localizacao da promocao
            //antiga e necessaria para que a validacao da troca de promocao desconsidere a promocao a ser trocada na
            //lista de promocoes do assinate.
            for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
            {
                PromocaoAssinante pAssinante = (PromocaoAssinante)iterator.next();
                Promocao promocao = (pAssinante != null) ? pAssinante.getPromocao() : null;
                if((promocao != null) && (promocao.getCategoria() != null))
                {
                    //Se a promocao for Pula-Pula ou Pendente de Primeira Recarga, atualizar a promocao antiga para 
                    //passagem de parametro na chamada do metodo de troca.
                    int categoria = promocao.getCategoria().getIdtCategoria();
                    if(categoria == Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA)
                    {
                        pAssinanteAntiga = pAssinante;
                        idtPromocaoAntiga = new Integer(promocao.getIdtPromocao());
                        break;
                    }
                }
            }
            
            //Gerando o objeto PromocaoAssinante para operacoes de validacao e inclusao do assinante na promocao.
            //OBS: O objeto PromocaoDiaExecucao nao e setado aqui porque como o objeto representando a promocao antiga
            //do assinante pode ser NULL, e o metodo de consulta depende da data de entrada na promocao antiga, 
            //pode haver a possibilidade de NullPointerException.
            pAssinanteNova = new PromocaoAssinante();
            pAssinanteNova.setIdtMsisdn(msisdn);
            //Obtendo o objeto com as informacoes da promocao Pula-Pula.
            Promocao promocao = super.consulta.getPromocao(idtPromocaoNova);
            if((promocao != null) && 
               (promocao.getCategoria() != null) &&  
               (promocao.getCategoria().getIdtCategoria() == Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA))
                pAssinanteNova.setPromocao(promocao);
            pAssinanteNova.setAssinante(assinante);
            pAssinanteNova.setCodigoNacional(super.consulta.getPromocaoCodigoNacional(idtPromocaoNova, idtCodigoNacional));
            pAssinanteNova.setPlanoPreco(super.consulta.getPromocaoPlanoPreco(idtPromocaoNova, idtPlanoPreco));
            
            result = super.trocaPromocaoAssinante(pAssinanteNova, pAssinanteAntiga, promocoesAssinante, dataProcessamento, conexaoPrep);
            
            //Se o assinante ja possuir o Pula-Pula, deve ser retornado OK. Caso o assinante nao possua nenhum Pula-Pula,
            //deve ser executada uma insercao.
            switch(result)
            {
            	case Definicoes.RET_PROMOCAO_ASSINANTE_JA_EXISTE:
                    result = Definicoes.RET_OPERACAO_OK;
                    break;
            	case Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE:
            		super.log(Definicoes.WARN, 
            				  "inserePulaPula",
							  "MSISDN: "           + msisdn       + 
							  " - Promocao Nova: " + promocaoNova + 
							  " - Assinante nao possui promocao Pula-Pula. Efetuando insercao da promocao.");
            	    result = this.inserePulaPula(msisdn, 
            	    							 promocaoNova, 
												 dataProcessamento, 
												 operador, 
												 Definicoes.CTRL_PROMOCAO_MOTIVO_PROMOCAO_NOK);
            	    break;
            	default: break;
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPulaPula", 
					  "MSISDN: " + msisdn + " - Promocao Nova: " + promocaoNova + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.operacoes.insereEvento(dataProcessamento,
            							 msisdn,
										 Definicoes.TIPO_APR_PROMOCAO_TROCA,
										 String.valueOf(idtPromocaoAntiga.intValue()),
										 String.valueOf(idtPromocaoNova.intValue()),
										 new Double(0.0),
										 new Integer(motivo),
										 operador,
										 (result == Definicoes.RET_OPERACAO_OK) ? 
										 	Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
										 new Integer(result),
										 conexaoPrep);
        }
        
        return result;
    }
    
    /**
     *	Efetua a troca da promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		novoStatus				Identificador do novo status da promocao Pula-Pula do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Codigo de retorno da operacao.
     */
    public int trocarStatusPulaPula(String msisdn, int novoStatus, PREPConexao conexaoPrep)
    {
    	try
		{
    		AssinantePulaPula pAssinante = this.consultaPromocaoPulaPula(msisdn, 
    																	 null, 
    																	 null,
    																	 false,
    																	 conexaoPrep);
    		PromocaoStatusAssinante status = super.consulta.getPromocaoStatusAssinante(new Integer(novoStatus));
    		return super.trocarStatusPromocaoAssinante(pAssinante, status, Calendar.getInstance().getTime(), conexaoPrep);
		}
    	catch(Exception e)
		{
    		super.log(Definicoes.ERRO, "trocarStatusPulaPula", "Excecao: " + e);
    		return Definicoes.RET_ERRO_TECNICO;
		}
    }
    
    /**
     *	Atualiza o status da promocao Pula-Pula apos a execucao do processo de concessao de bonus. As regras para a 
     *	atualizacao do status sao as seguintes:
     *		1. O tipo de execucao deve ser DEFAULT ou REBARBA.
     *		2. A concessao nao pode ser retroativa, ou seja, a concessao deve ter a data de referencia com o mes atual.
     *		3. A operacao deve ser Operacao OK ou Ligacoes nao recebidas no periodo.
     *		4. Caso a regra (3) nao seja satisfeita, o status deve ser atualizado caso o tipo de execucao for REBARBA.
     *
     *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
     *	@param		tipoExecucao			Tipo de execucao do processo de concessao de bonus.
     *	@param		retorno					Codigo de retorno da operacao.
     *	@param		dataReferencia			Data de referencia para a concessao do bonus.
     *	@param		dataProcessamento		Data de processamento da operacao.
     */
    private void atualizarStatusPulaPula(AssinantePulaPula pAssinante, 
    									 String tipoExecucao,
										 short retorno,
										 Date dataReferencia,
										 Date dataProcessamento)
    {
    	//Objeto utilizado para converter as datas em meses. Desta forma os meses podem ser comparados.
    	SimpleDateFormat conversorMes = new SimpleDateFormat("yyyyMM");
    	
		//Regra: O status deve ser atualizado somente se o processamento atual nao representar uma concessao 
		//retroativa. A concessao e considerada retroativa se o mes da data de referencia for menor que o da data de 
		//processamento.
		if(conversorMes.format(dataReferencia).compareTo(conversorMes.format(dataProcessamento)) >= 0)
			try
			{
		    	//Regra: Os codigos de retorno em que o status da promocao do assinante deve ser atualizado sao: 
				//Operacao OK, e assinante nao recebeu ligacoes. Neste caso os tipos de execucao deve ser DEFAULT e 
				//REBARBA. Caso contrario, o status deve ser atualizado no caso de tipo de execucao REBARBA. Caso o 
				//bonus ja tenha sido concedido, nao e realizado.
				switch(retorno)
				{
					case Definicoes.RET_OPERACAO_OK:
					case Definicoes.RET_PROMOCAO_LIGACOES_NOK:
						if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT) ||
						   tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA))
							pAssinante.setStatus(pAssinante.getStatus().next());
						break;
					case Definicoes.RET_PROMOCAO_BONUS_CONCEDIDO:
						break;
					default: 
						if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA))
							pAssinante.setStatus(pAssinante.getStatus().next());
				}
			}
			catch(Exception e)
			{
				super.log(Definicoes.WARN,
						  "atualizarStatusPulaPula",
						  pAssinante + " - Excecao: " + e);
			}
    }
    
    /**
     *	Trata as informacoes da promocao Pula-Pula do assinante no processo de transferencia de titularidade.
     *
     *	@param		assinante				Informacoes do assinante na plataforma.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Codigo de retorno da operacao.
     */
    public short trataTransferencia(Assinante assinante, Date dataProcessamento, PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        try
        {
            String msisdn = assinante.getMSISDN();
            
            //A data de concessao deve ser a data de processamento. Isto porque deve ser verificado se o assinante ja 
            //recebeu o bonus. Se nao recebeu, deve ser retirada a totalizacao do mes anterior tambem.
            //Executando a consulta da promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.BONUS_AGENDADOS
            };
            AssinantePulaPula pAssinante = this.consultaPromocaoPulaPula(msisdn, 
            															 detalhes, 
            															 dataProcessamento, 
            															 assinante, 
            															 false,
            															 conexaoPrep);
            
            if(pAssinante != null)
            {
                //Atualizando a data de entrada do assinante na promocao devido a tranferencia de titularidade.
                pAssinante.setDatEntradaPromocao(dataProcessamento);
                Integer promocao = new Integer(pAssinante.getPromocao().getIdtPromocao());
                super.operacoes.atualizaPromocaoAssinante(msisdn, promocao, pAssinante, conexaoPrep);
                //Retirando a totalizacao do assinante.
                this.retiraTotalizacao(pAssinante, dataProcessamento, conexaoPrep);
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trataTransferencia", 
					  assinante + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.log(Definicoes.DEBUG, 
            		  "trataTransferencia", 
					  assinante + " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }

    /**
     *	Retira a sumarizacao das ligacoes recebidas pelo assinante para o processo de transferencia de titularidade.
     *
     *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@throws		Exception
     */
    public void retiraTotalizacao(AssinantePulaPula pAssinante, Date dataProcessamento, PREPConexao conexaoPrep) throws Exception
    {
        String msisdn = pAssinante.getIdtMsisdn();
        
        //Obtendo o mes de retirada da totalizacao Pula-Pula do assinante.
        SimpleDateFormat conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
        String mes = conversorDatMes.format(dataProcessamento);
        
        //Inicializando as datas de analise e obtendo objeto Calendar para manipulacao do periodo de ligacoes 
        //recebidas expurgadas.  
        Date dataInicio = null;
        Date dataFim = dataProcessamento;
        Calendar calAnalise = Calendar.getInstance();
        calAnalise.setTime(dataProcessamento);
        calAnalise.set(Calendar.DAY_OF_MONTH, calAnalise.getActualMinimum(Calendar.DAY_OF_MONTH));
        calAnalise.set(Calendar.HOUR_OF_DAY , 0);
        calAnalise.set(Calendar.MINUTE      , 0);
        calAnalise.set(Calendar.SECOND      , 0);
        
        //Retirando a totalizacao do assinante para o mes atual.
        super.operacoes.transferirTitularidade(msisdn, mes, conexaoPrep);
        
        //Verificando se o assinante recebeu bonus no periodo. Se o assinante nao recebeu, deve ser expurgado
        //das ligacoes do mes anterior.
        Collection bonusConcedidos = pAssinante.getBonusConcedidos(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT);
        if(bonusConcedidos.size() == 0)
        {
            calAnalise.add(Calendar.MONTH, -1);
            mes = conversorDatMes.format(calAnalise.getTime());
            super.operacoes.transferirTitularidade(msisdn, mes, conexaoPrep);
        }
        
        //Obtendo os parametros para marcar os CDR's do assinante.
        dataInicio = calAnalise.getTime();
        Integer idtPromocao = new Integer(pAssinante.getPromocao().getIdtPromocao());
        Integer ffDiscount = new Integer(DescontoPulaPula.TITULARIDADE_TRANSFERIDA);
        //Marcando os CDR's de ligacoes recebidas e bonificaveis do assinante.
        super.operacoes.atualizaFFDiscountCDR(msisdn, idtPromocao, dataInicio, dataFim, ffDiscount, conexaoPrep);
        
        //Verificando se o assinante possui bonus agendados na fila de recargas. Se tiver, devem ser retirados.
        Collection bonusAgendados = pAssinante.getBonusAgendados();
        for(Iterator iterator = bonusAgendados.iterator(); iterator.hasNext();)
            super.operacoes.retiraFilaRecargas((FilaRecargas)iterator.next(), conexaoPrep);
    }
    
    /**
     *	Consulta as informacoes da Promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		detalhes				Lista de opcoes de detalhes a serem consultados.
     *	@param		consultarTec			Flag indicando se a consulta de assinante deve ser feita na plataforma Tecnomen.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Informacoes da Promocao Pula-Pula do assinante.
     *	@throws		Exception
     */
    public AssinantePulaPula consultaPromocaoPulaPula(String		msisdn, 
    												  int[]			detalhes, 
    												  boolean		consultarTec, 
    												  boolean		isConsultaCheia,
    												  PREPConexao	conexaoPrep) throws Exception
    {
        //Obtendo a promocao Pula-Pula do assinante.
        Collection promocoesAssinante = super.consultaPromocoesAssinante(msisdn, 
        																 Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA, 
        																 consultarTec, 
        																 conexaoPrep);
        PromocaoAssinante pAssinante = (promocoesAssinante.size() > 0) ? 
        	(PromocaoAssinante)promocoesAssinante.iterator().next() : null;

        //O mes de referencia para obtencao dos detalhes da promocao Pula-Pula e obtido a partir da data de 
        //execucao do assinante. Caso nao exista, e passado o mes seguinte ao atual.
    	Date dataReferencia = (pAssinante != null) ? pAssinante.getDatExecucao() : null;
    	
        if(dataReferencia == null)
        {
        	Calendar calReferencia = Calendar.getInstance();
        	calReferencia.set(Calendar.DAY_OF_MONTH, calReferencia.getActualMinimum(Calendar.DAY_OF_MONTH));
        	calReferencia.add(Calendar.MONTH, 1);
        	dataReferencia = calReferencia.getTime();
        }
        
        //Obtendo o factory para consulta de detalhes do Pula-Pula do assinante.
        ConsultorDetalhePulaPulaFactory factory = new ConsultorDetalhePulaPulaFactory(super.getIdLog());

        return factory.consultarDetalhesPulaPula(pAssinante, detalhes, dataReferencia, isConsultaCheia, conexaoPrep);
    }

    /**
     *	Consulta as informacoes da Promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		detalhes				Lista de opcoes de detalhes a serem consultados.
     *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Informacoes da Promocao Pula-Pula do assinante.
     *	@throws		Exception
     */
    public AssinantePulaPula consultaPromocaoPulaPula(String 		msisdn, 
    												  int[] 		detalhes, 
    												  Assinante		assinante,
    												  boolean		isConsultaCheia,
    												  PREPConexao	conexaoPrep) throws Exception
    {
        //Obtendo a promocao Pula-Pula do assinante.
        Collection promocoesAssinante = super.consultaPromocoesAssinante(msisdn, 
        																 Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA, 
        																 assinante, 
        																 conexaoPrep);
        PromocaoAssinante pAssinante = (promocoesAssinante.size() > 0) ? 
        	(PromocaoAssinante)promocoesAssinante.iterator().next() : null;
 
        //O mes de referencia para obtencao dos detalhes da promocao Pula-Pula e obtido a partir da data de 
        //execucao do assinante. Caso nao exista, e passado o mes seguinte ao atual.
    	Date dataReferencia = (pAssinante != null) ? pAssinante.getDatExecucao() : null;
    	
        if(dataReferencia == null)
        {
        	Calendar calReferencia = Calendar.getInstance();
        	calReferencia.set(Calendar.DAY_OF_MONTH, calReferencia.getActualMinimum(Calendar.DAY_OF_MONTH));
        	calReferencia.add(Calendar.MONTH, 1);
        	dataReferencia = calReferencia.getTime();
        }
        
        //Obtendo o factory para consulta de detalhes do Pula-Pula do assinante.
        ConsultorDetalhePulaPulaFactory factory = new ConsultorDetalhePulaPulaFactory(super.getIdLog());

        return factory.consultarDetalhesPulaPula(pAssinante, detalhes, dataReferencia, isConsultaCheia, conexaoPrep);
    }

    /**
     *	Consulta as informacoes da Promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		detalhes				Lista de opcoes de detalhes a serem consultados.
     *	@param		dataReferencia			Data de referencia para a concessao do bonus.
     *	@param		consultarTec			Flag indicando se a consulta de assinante deve ser feita na plataforma Tecnomen.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Informacoes da Promocao Pula-Pula do assinante.
     *	@throws		Exception
     */
    public AssinantePulaPula consultaPromocaoPulaPula(String		msisdn, 
    												  int[] 		detalhes, 
    												  Date			dataReferencia, 
    												  boolean		consultarTec,
    												  boolean		isConsultaCheia,
    												  PREPConexao	conexaoPrep) throws Exception
    {
        //Obtendo a promocao Pula-Pula do assinante.
        Collection promocoesAssinante = super.consultaPromocoesAssinante(msisdn, 
        																 Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA, 
        																 consultarTec, 
        																 conexaoPrep);
        PromocaoAssinante pAssinante = (promocoesAssinante.size() > 0) ? 
        	(PromocaoAssinante)promocoesAssinante.iterator().next() : null;

        //Obtendo o factory para consulta de detalhes do Pula-Pula do assinante.
        ConsultorDetalhePulaPulaFactory factory = new ConsultorDetalhePulaPulaFactory(super.getIdLog());

        return factory.consultarDetalhesPulaPula(pAssinante, detalhes, dataReferencia, isConsultaCheia, conexaoPrep);
    }

    /**
     *	Consulta as informacoes da Promocao Pula-Pula do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		detalhes				Lista de opcoes de detalhes a serem consultados.
     *	@param		dataReferencia			Data de referencia para a concessao do bonus.
     *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Informacoes da Promocao Pula-Pula do assinante.
     *	@throws		Exception
     */
    public AssinantePulaPula consultaPromocaoPulaPula(String		msisdn, 
    												  int[]			detalhes, 
    												  Date			dataReferencia, 
    												  Assinante		assinante, 
    												  boolean		isConsultaCheia,
    												  PREPConexao	conexaoPrep) throws Exception
    {
        //Obtendo a promocao Pula-Pula do assinante.
        Collection promocoesAssinante = super.consultaPromocoesAssinante(msisdn, 
        																 Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA, 
        																 assinante, 
        																 conexaoPrep);
        PromocaoAssinante pAssinante = (promocoesAssinante.size() > 0) ? 
        	(PromocaoAssinante)promocoesAssinante.iterator().next() : null;

        //Obtendo o factory para consulta de detalhes do Pula-Pula do assinante.
        ConsultorDetalhePulaPulaFactory factory = new ConsultorDetalhePulaPulaFactory(super.getIdLog());

        return factory.consultarDetalhesPulaPula(pAssinante, detalhes, dataReferencia, isConsultaCheia, conexaoPrep);
    }
    
    /**
     *	Consulta a Promocao Pula-Pula do assinante, retornando as informacoes em formato XML.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@return		result					XML com as informacoes da Promocao Pula-Pula do assinante.
     */
    public String consultaPromocaoPulaPulaXML(String msisdn)
    {
        String				result			= null;
        AssinantePulaPula	pAssinante		= null;
        short				validacao		= Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE;
        String 				tipoExecucao	= Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT;
        Date				dataReferencia	= null;
        PREPConexao			conexaoPrep		= null;

        try
        {
            //Obtendo a data de referencia para validacao do assinante no periodo. Utilizada caso o assinante 
        	//nao possua data de execucao. A data de referencia corresponde ao proximo mes, que corresponde a 
        	//concessao do mes atual.
            Calendar calReferencia = Calendar.getInstance();
            calReferencia.set(Calendar.DAY_OF_MONTH, calReferencia.getActualMinimum(Calendar.DAY_OF_MONTH));
            calReferencia.add(Calendar.MONTH, 1);
            dataReferencia = calReferencia.getTime();
        	
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            //Consultando a promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.BONUS_AGENDADOS,
                ControlePulaPula.SALDO_PULA_PULA
            };
            
            Assinante assinante = this.consulta.getAssinanteGPP(msisdn, false, conexaoPrep);
            pAssinante = this.consultaPromocaoPulaPula(msisdn, detalhes, assinante, false, conexaoPrep);
            
            if(pAssinante != null)
            {
            	if(!pAssinante.getStatus().isDisponivelConsulta())
            		pAssinante.bloquearConsulta();
            	
            	//Caso o assinante tenha data de execucao definida, a data de referencia deve corresponder a
            	//sua data de execucao.
            	if(pAssinante.getDatExecucao() != null)
            		dataReferencia = pAssinante.getDatExecucao();
            	
            	validacao = this.validaAssinante(tipoExecucao, pAssinante, dataReferencia);
            }
            else if(assinante != null && assinante.isHibrido())
        		validacao = Definicoes.RET_HIBRIDO_PROMOCAO_ANTIGA;
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "consultaPromocaoPulaPulaXML", 
					  "MSISDN: " + msisdn + " - Excecao: " + e);
            validacao = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            result = PromocaoPulaPulaXMLParser.format(msisdn, pAssinante, dataReferencia, validacao);
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.DEBUG, 
            		  "consultaPromocaoPulaPulaXML", 
					  "XML de retorno: [" + result + "]");
        }
        
        return result;
    }
    
    /**
     *	Consulta a Promocao Pula-Pula do assinante no mes informado, retornando as informacoes em formato XML.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		mes						Mes de referencia para a concessao do bonus, no formato YYYYMM.
     *	@return		XML com as informacoes da Promocao Pula-Pula do assinante.
     */
    public String consultaPromocaoPulaPulaXML(String msisdn, String mes)
    {
        String				result			= null;
        AssinantePulaPula	pAssinante		= null;
        short				validacao		= Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE;
        String				tipoExecucao	= Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT;
        Date				dataReferencia	= null;
        PREPConexao			conexaoPrep		= null;

        try
        {
            //Obtendo a data de referencia a partir do mes informado.
            SimpleDateFormat conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
            dataReferencia = conversorDatMes.parse(mes);
            
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
           //Consultando a promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.BONUS_AGENDADOS,
                ControlePulaPula.SALDO_PULA_PULA
            };
            
            //Executando a consulta da promocao Pula-Pula do assinante.
            Assinante assinante = this.consulta.getAssinanteGPP(msisdn, false, conexaoPrep);
            pAssinante = this.consultaPromocaoPulaPula(msisdn, detalhes, dataReferencia, assinante, false, conexaoPrep);
            
            if(pAssinante != null)
            {
            	if(!pAssinante.getStatus().isDisponivelConsulta())
            		pAssinante.bloquearConsulta();
            	
            	validacao = this.validaAssinante(tipoExecucao, pAssinante, dataReferencia);
            }
            else if(assinante != null && assinante.isHibrido())
        		validacao = Definicoes.RET_HIBRIDO_PROMOCAO_ANTIGA;
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "consultaPromocaoPulaPulaXML", 
					  "MSISDN: " + msisdn + " - Mes de Concessao: " + mes + " - Excecao: " + e);
            validacao = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            result = PromocaoPulaPulaXMLParser.format(msisdn, pAssinante, dataReferencia, validacao);
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.DEBUG, 
            		  "consultaPromocaoPulaPulaXML", 
					  "XML de retorno: [" + result + "]");
        }
        
        return result;
    }
    
    /**
     *	Executa a concessao de bonus Pula-Pula para o assinante.
     *
     *	@param		tipoExecucao			Tipo de execucao da concessao.
     *	@param		msisdn					MSISDN do assinante.
     *	@param		dataReferencia			Data de referencia para a concessao do bonus.
     *	@param		operador				Nome do operador que executou a operacao.
     *	@param		motivo					Identificador do motivo da operacao.
     *	@return		Codigo de retorno da operacao.
     */
    public short executaConcessao(String tipoExecucao, String msisdn, Date dataReferencia, String operador, int motivo)
    {
        PREPConexao conexaoPrep = null;
        
    	try
		{
            //Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            return this.executaConcessao(tipoExecucao, msisdn, dataReferencia, operador, motivo, conexaoPrep);
		}
    	catch(Exception e)
		{
            super.log(Definicoes.ERRO, "executaConcessao", "MSISDN: " + msisdn + " - Excecao: " + e);
            return Definicoes.RET_ERRO_TECNICO;
		}
    	finally
		{
            //Liberando a conexao com o banco de dados.
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		}
    }
    
    /**
     *	Executa a concessao de bonus Pula-Pula para o assinante. 
     *
     *	@param		tipoExecucao			Tipo de execucao da concessao.
     *	@param		msisdn					MSISDN do assinante.
     *	@param		dataReferencia			Data de referencia para a concessao do bonus.
     *	@param		operador				Nome do operador que executou a operacao.
     *	@param		motivo					Identificador do motivo da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Codigo de retorno da operacao.
     */
    public short executaConcessao(String tipoExecucao, 
    							  String msisdn, 
								  Date dataReferencia, 
								  String operador, 
								  int motivo, 
								  PREPConexao conexaoPrep)
    {
        short				result		= -1;
        AssinantePulaPula	pAssinante	= null;

        try
        {
            //Obtendo o mes de referencia. Por definicao o mes de referencia corresponde ao mes de execucao default do 
            //Pula-Pula. Se o tipo de execucao for PARCIAL, uma vez que a concessao deste tipo de execucao ocorre
            //no mes de recebimento das ligacoes, a data de referencia deve ser atualizada para o primeiro dia do mes
            //seguinte.
            Calendar calMes = Calendar.getInstance();
            calMes.setTime(dataReferencia);
            if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL))
            {
                calMes.add(Calendar.MONTH, 1);
                calMes.set(Calendar.DAY_OF_MONTH, calMes.getActualMinimum(Calendar.DAY_OF_MONTH));
            }
            
            //Executando a consulta da promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.BONUS_AGENDADOS_PERIODO,
                ControlePulaPula.SALDO_PULA_PULA
            };
            pAssinante = this.consultaPromocaoPulaPula(msisdn, detalhes, calMes.getTime(), false, false, conexaoPrep);
            
            //Executando o processo de concessao de bonus Pula-Pula para o assinante.
            result = this.executaConcessao(tipoExecucao, 
            							   pAssinante, 
										   dataReferencia, 
										   new Date(), 
										   operador, 
										   motivo, 
										   conexaoPrep); 
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "executaConcessao", 
            		  "MSISDN: "                + msisdn         +
					  " - Tipo de execucao: "   + tipoExecucao   +
					  " - Data de Referencia: " + dataReferencia + 
					  " - Excecao: "            + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            super.log(Definicoes.INFO, 
            		  "executaConcessao", 
					  "MSISDN: "                + msisdn         + 
					  " - Tipo de execucao: "   + tipoExecucao   +
					  " - Data de Referencia: " + dataReferencia +
					  " - Operador: "           + operador       +
					  " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /**
     *	Executa a concessao de bonus Pula-Pula para o assinante.
     *
     *	@param		tipoExecucao			Tipo de execucao da concessao.
     *	@param		pAssinante				Informacoes referentes a promocao Pula-Pula do assinante.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador que executou a operacao.
     *	@param		motivo					Identificador do motivo da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Codigo de retorno da operacao.
     */
    public short executaConcessao(String tipoExecucao, 
    							  AssinantePulaPula pAssinante, 
								  Date dataReferencia, 
                                  Date dataProcessamento, 
								  String operador, 
								  int motivo, 
								  PREPConexao conexaoPrep)
    {
        short result = -1;

        try
        {
            //Validando o assinante.
            result = this.validaAssinante(tipoExecucao, pAssinante, dataReferencia);

            //Se o assinante foi validado corretamente ou nao recebeu ligacoes no periodo, inserir registro na fila
            //de recargas. Mesmo que o assinante nao tenha recebido ligacoes no periodo, seu registro deve ser 
            //inserido na fila de recargas para que os saldos configurados para serem zerados na TBL_PRO_PROMOCAO 
            //sejam executados pelo processo de consumo da fila.
            if((result == Definicoes.RET_OPERACAO_OK) || (result == Definicoes.RET_PROMOCAO_LIGACOES_NOK))
            {
                short resultInsert = this.insereFilaRecargas(result, tipoExecucao, pAssinante, dataProcessamento, conexaoPrep);
                result = (resultInsert == Definicoes.RET_OPERACAO_OK) ? result : resultInsert;
            }
            
            //Atualizando a data de ultimo bonus, caso o tipo de execucao seja DEFAULT ou REBARBA com operacao OK.
            if(((tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT)   || 
                 tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA))) &&
               (result == Definicoes.RET_OPERACAO_OK))
                pAssinante.setDatUltimoBonus(pAssinante.getDataCredito(tipoExecucao));
            
            //Atualizando o status da promocao do assinante.
            this.atualizarStatusPulaPula(pAssinante,
            							 tipoExecucao,
										 result,
										 dataReferencia,
										 dataProcessamento);
            
            //Atualizando a data de execucao do assinante.
            super.gerenciadorData.atualizarDataExecucao(pAssinante, 
            											tipoExecucao, 
														result, 
														dataReferencia, 
														dataProcessamento);
            
            //Atualizando as informacoes do assinante no banco.
            String	msisdn		= pAssinante.getIdtMsisdn();
            Integer	promocao	= new Integer(pAssinante.getPromocao().getIdtPromocao());
            super.operacoes.atualizaPromocaoAssinante(msisdn, promocao, pAssinante, conexaoPrep);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
          		  "executaConcessao", 
          		  pAssinante +
				  " - Tipo de execucao: "   + tipoExecucao   +
				  " - Data de Referencia: " + dataReferencia +
				  " - Operador: "           + operador       +
				  " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.insereHistoricoPulaPula(result, pAssinante, dataProcessamento, operador, motivo, conexaoPrep);
        }
        
        return result;
    }
    
    /**
     *	Executa o processo de estorno de bonus Pula-Pula por fraude.
     *
     *	@param		estorno					Informacoes de estorno de bonus Pula-Pula para o assinante.
     *	@param		operacoes				Interface para execucao de operacoes referentes ao processo de Expurgo/Estorno de Bonus Pula-Pula.
     *	@return		Codigo de retorno da operacao.
     *	@throws		Exception
     */
    public short executaEstornoPulaPula(InterfaceEstornoPulaPula estorno, OperacoesEstornoPulaPula operacoes) throws Exception
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        try
        {
            //Obtendo informacoes referentes a origem do estorno.
            PromocaoOrigemEstorno origem = super.consulta.getPromocaoOrigemEstorno(estorno.getIdtOrigem());
            if(origem == null)
            {
            	result = Definicoes.RET_PROMOCAO_ORIGEM_NAO_EXISTE;
            	return result;
            }
            
            //Obtendo o mes de referencia para a consulta da promocao Pula-Pula do assinante.
            Calendar calReferencia = Calendar.getInstance();
            calReferencia.setTime(estorno.getDatReferencia());
            calReferencia.add(Calendar.MONTH, 1);
            SimpleDateFormat conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
            String mesReferencia = conversorDatMes.format(calReferencia.getTime());
            Date dataReferencia = conversorDatMes.parse(mesReferencia);
            
            String msisdn = estorno.getIdtMsisdn();
            //Executando a consulta da promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.BONUS_AGENDADOS_PERIODO
            };
            AssinantePulaPula pAssinante = this.consultaPromocaoPulaPula(msisdn, 
            															 detalhes, 
            															 dataReferencia, 
            															 true,
            															 false,
            															 operacoes.getConexao());
            
            //Validando o assinante.
            result = this.validaAssinanteEstornoPulaPula(pAssinante);
            if(result != Definicoes.RET_OPERACAO_OK)
                return result;
            
            //Obtendo as datas de inicio e fim de analise para obtencao dos CDR's do assinante a serem expurgados ou 
            //estornados, sendo que estas datas devem ser ajustadas de acordo com a data de entrada do assinante.
            Date[] datasAnalise = this.calculaDatasEstorno(pAssinante, estorno, origem);
            Date   dataInicio   = datasAnalise[0];
            Date   dataFim      = datasAnalise[1];
            
            //Obtendo a lista de CDR's de ligacoes recebidas pelo assinante a serem expurgadas ou estornadas.
            Integer promocao = new Integer(pAssinante.getPromocao().getIdtPromocao());
            String numeroOrigem = estorno.getIdtNumeroOrigem();
            Collection listaCdrs = super.consulta.getCdrsEstorno(msisdn, promocao, numeroOrigem, dataInicio, dataFim, operacoes.getConexao());
            
            //Verificando se ha CDR's disponiveis para execucao.
            if(listaCdrs.size() <= 0)
            {
            	result = Definicoes.RET_SEM_CHAMADAS_A_B;
            	return result;
            }
                
            //Calculando os valores de expurgo e estorno para os CDR's.
            CalculoEstornoPulaPula calculo = this.calcularEstornoPulaPula(pAssinante, listaCdrs, operacoes.getConexao());
            
            //Atualizando o status dos CDR's.
            for(Iterator iterator = calculo.getListaCdrs().iterator(); iterator.hasNext();)
            {
                ArquivoCDR cdr = (ArquivoCDR)iterator.next();
                String subId = cdr.getSubId();
                Date timestamp = cdr.getTimestamp();
                long startTime = cdr.getStartTime();
                String callId = cdr.getCallId();
                long transactionType = cdr.getTransactionType();
                int ffDiscount = new Long(cdr.getFfDiscount()).intValue();
                operacoes.atualizarFFDiscountCDR(subId, timestamp, startTime, callId, transactionType, ffDiscount);
            }
            
            //Atualizando as informacoes sumarizadas de ligacoes recebidas pelo assinante.
            TotalizacaoPulaPula totalizacao = calculo.getTotalizacao();
            operacoes.atualizarTotalizacaoPulaPula(msisdn, totalizacao.getDatMes(), totalizacao);
            
            //Inserindo registro de historico do processamento do estorno das ligacoes recebidas pelo assinante.
            PromocaoEstornoPulaPula historico = new PromocaoEstornoPulaPula();
            historico.setIdtLote(estorno.getIdtLote());
            historico.setDatReferencia(estorno.getDatReferencia());
            historico.setIdtMsisdn(msisdn);
            historico.setIdtPromocao(promocao.intValue());
            historico.setIdtNumeroOrigem(estorno.getIdtNumeroOrigem());
            historico.setIdtOrigem(estorno.getIdtOrigem());
            historico.setDatProcessamento(estorno.getDatProcessamento());
            historico.setVlrExpurgo(calculo.getValorExpurgo());
            historico.setVlrExpurgoSaturado(calculo.getValorExpurgoSaturado());
            historico.setVlrEstorno(calculo.getValorEstorno());
            historico.setVlrEstornoEfetivo(calculo.getValorEstornoEfetivo());
            operacoes.inserirPromocaoEstornoPulaPula(historico);
            
            //Executando expurgo com deducao do bonus referente a concessao default agendado na fila de recargas, 
            //caso seja necessario.
            if(calculo.getValorExpurgoAgendadoDefault() > 0.0)
                this.estornarBonusAgendado(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT, pAssinante, calculo.getValorExpurgoAgendadoDefault(), operacoes);
            
            //Executando expurgo com deducao do bonus referente a concessao parcial agendado na fila de recargas, 
            //caso seja necessario.
            if(calculo.getValorExpurgoAgendadoParcial() > 0.0)
                this.estornarBonusAgendado(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL, pAssinante, calculo.getValorExpurgoAgendadoParcial(), operacoes);
                    
            //Executando o estorno de bonus ja concedido, caso seja necessario.
            if(calculo.getValorEstornoEfetivo() > 0.0)
                result = operacoes.estornar(pAssinante, calculo.getValorEstornoEfetivo(), estorno.getDatProcessamento());
        }
        catch (Exception e)
        {
        	result = Definicoes.RET_ERRO_TECNICO;
        	super.log(Definicoes.ERRO, "executaEstornoPulaPula", estorno + ". Excecao: " + e);
        	
        }
        finally
        {
        	super.log(Definicoes.INFO, "executaEstornoPulaPula", estorno + " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /**
     *	Calcula as datas de analise para obtencao de CDR's de ligacoes recebidas que devem ser expurgadas/estornadas
     *	por fraude. O calculo depende de dois fatores: se o tipo de analise e mensal ou diario, e da data de entrada
     *	do assinante no Pula-Pula. Devido a regra em que o assinante nao pode ter visibilidade de eventos anteriores
     *	a sua entrada na promocao, as datas devem ser ajustadas de forma a nao obter CDR's do antigo titular, caso
     *	exista. Porem, para compensar o problema dos CDR's atrasados, o processo deve ajustar a data de inicio de
     *	analise conforme necessario.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		estorno					Informacoes referentes a requisicao de expurgo/estorno.
     *	@param		origem					Informacoes referentes a origem da requisicao.
     *	@return		Datas de analise, onde result[0] corresponde a data inicial e result[1] a data final.
     */
    private Date[] calculaDatasEstorno(AssinantePulaPula pAssinante, InterfaceEstornoPulaPula estorno, PromocaoOrigemEstorno origem)
    {
        Date[] result = new Date[2];
        
        Calendar calAnalise = Calendar.getInstance();
        calAnalise.setTime(estorno.getDatReferencia());
        Date dataInicio  = null;
        Date dataFim     = null;
        Date dataEntrada = pAssinante.getDatEntradaPromocao();
        Calendar calEntrada = Calendar.getInstance();
        calEntrada.setTime(dataEntrada);
        if(origem.getTipAnalise().equals(PromocaoOrigemEstorno.TIP_ANALISE_DIARIO))
        {
            dataInicio = calAnalise.getTime();
            calAnalise.add(Calendar.DAY_OF_MONTH, 1);
            dataFim = calAnalise.getTime();
    	    if(dataInicio.compareTo(dataEntrada) < 0)
    	    {
    	        calAnalise.setTime(dataInicio);
    	        if((calAnalise.get(Calendar.YEAR ) != calEntrada.get(Calendar.YEAR )) ||
    	           (calAnalise.get(Calendar.MONTH) != calEntrada.get(Calendar.MONTH)))
    	        {
    		        dataInicio = dataEntrada;
    		        dataFim    = dataEntrada;
    	        }
    	    }
        }
        else if(origem.getTipAnalise().equals(PromocaoOrigemEstorno.TIP_ANALISE_MENSAL))
        {
            calAnalise.set(Calendar.DAY_OF_MONTH, calAnalise.getActualMinimum(Calendar.DAY_OF_MONTH));
            dataInicio = calAnalise.getTime();
    	    if((dataInicio.compareTo(dataEntrada) < 0) &&
    	       ((calAnalise.get(Calendar.YEAR ) != calEntrada.get(Calendar.YEAR )) ||
     	        (calAnalise.get(Calendar.MONTH) != calEntrada.get(Calendar.MONTH))))
    	        dataInicio = dataEntrada; 

            calAnalise.add(Calendar.MONTH, 1);
            dataFim = calAnalise.getTime();
    	    if(dataFim.compareTo(dataEntrada) < 0)
    	        dataFim = dataEntrada;
        }
        
        result[0] = dataInicio;
        result[1] = dataFim;
        
        return result;
    }
    
    /**
     *	Estorna o valor passado por parametro do bonus Pula-Pula agendado na fila de recargas para o tipo de execucao
     *	informado. Este valor deve ser estornado devido ao recebimento de ligacoes fraudulentas.
     *
     *	@param		tipoExecucao			Tipo de execucao (DEFAULT, REBARBA, PARCIAL).
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		valorEstorno			Valor a ser estornado do bonus agendado.
     *	@param		operacoes				Interface para execucao de operacoes referentes ao processo de Expurgo/Estorno de Bonus Pula-Pula.
     *	@throws		Exception
     */
    private void estornarBonusAgendado(String					tipoExecucao, 
    								   AssinantePulaPula		pAssinante, 
    								   double					valorEstorno, 
    								   OperacoesEstornoPulaPula	operacoes) throws Exception
    {
    	PromocaoTipoTransacao tipoTransacao = 
    		(PromocaoTipoTransacao)pAssinante.getTiposTransacao(tipoExecucao).iterator().next();
    	
        //Obtendo objeto representando o registro na fila de recargas.
        FilaRecargas bonusAgendado = pAssinante.getBonusAgendado(tipoTransacao);
        
        //Atualizando o valor de credito de bonus.
        double valorCreditoBonus = bonusAgendado.getVlrCreditoBonus().doubleValue();
        valorCreditoBonus = (valorCreditoBonus - valorEstorno >= 0) ? valorCreditoBonus - valorEstorno : 0.0;
        bonusAgendado.setVlrCreditoBonus(new Double(valorCreditoBonus));
        
        //Atualizando a mensagem de SMS a ser enviada ao assinante. 
        PromocaoInfosSms infosSms = pAssinante.getInfosSms(tipoExecucao, tipoTransacao.getTipoBonificacao());
        if(infosSms != null)
        {
        	String mensagem = infosSms.getDesMensagem();
        	
            mensagem = mensagem.replaceAll(Definicoes.PATTERN_VALOR, bonusAgendado.format(FilaRecargas.VLR_CREDITO_BONUS));
            mensagem = mensagem.replaceAll(Definicoes.PATTERN_PROMOCAO, pAssinante.getPromocao().getNomPromocao());
            
            bonusAgendado.setDesMensagem(mensagem);
        }
        
        operacoes.atualizarFilaRecargas(bonusAgendado.getIdtMsisdn(), 
        								bonusAgendado.getTipTransacao(), 
        								bonusAgendado.getDatCadastro(), 
        								bonusAgendado);
    }
    
    /**
     *	Calcula as informacoes de estorno de bonus Pula-Pula por fraude para o assinante.
     *
     *	@param		pAssinante				Informacoes referentes a promocao Pula-Pula do assinante.
     *	@param		listaCdrs				Lista de CDR's a serem expurgados ou estornados.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Resulta do analise de ligacoes recebidas que deve ser expurgadas/estornadas.
     *	@throws		Exception
     */
    private CalculoEstornoPulaPula calcularEstornoPulaPula(AssinantePulaPula	pAssinante, 
    													   Collection			listaCdrs, 
    													   PREPConexao			conexaoPrep) throws Exception
    {
        CalculoEstornoPulaPula result = new CalculoEstornoPulaPula();
        
        //Obtendo o historico da promocao Pula-Pula do assinante.
        Collection listaHistorico = super.consulta.getHistoricoPulaPula(pAssinante.getIdtMsisdn(), pAssinante.getDatEntradaPromocao(), conexaoPrep);
        
        TotalizacaoPulaPula totalizacao = pAssinante.getTotalizacao();
        TotalizacaoPulaPula resultTotalizacao = new TotalizacaoPulaPula();
        resultTotalizacao.setDatMes(totalizacao.getDatMes());
        resultTotalizacao.setIdtMsisdn(totalizacao.getIdtMsisdn());
        BonusPulaPula bonusCn = pAssinante.getBonusCn();
        
        //Obtendo o objeto necessario para o calculo do saldo de bonus Pula-Pula para o assinante.
        ConsultorDetalhePulaPulaFactory factory = new ConsultorDetalhePulaPulaFactory(super.getIdLog());
        ConsultorDetalhePulaPula consultor = factory.newConsultorDetalhePulaPula(ControlePulaPula.SALDO_PULA_PULA);
        
        //Percorrendo os CDR's a serem expurgados ou estornados.
        for(Iterator iterator = listaCdrs.iterator(); iterator.hasNext();)
        {
            //Determinando o tipo de estorno.
            ArquivoCDR cdr = (ArquivoCDR)iterator.next();
            
            Calendar datReferencia = Calendar.getInstance();
            datReferencia.setTime(cdr.getTimestamp());
            datReferencia.add(Calendar.MONTH, 1);

            //Recalculando o bonus Pula-Pula do assinante.
            consultor.consultarDetalhePulaPula(pAssinante, datReferencia.getTime(), false, null);
            
            int tipoEstorno = this.calculaTipoEstorno(cdr, pAssinante, listaHistorico);
            
            //Marcando os CDR's, atualizando a sumarizacao e atualizando o valor de expurgo e estorno.
            switch(tipoEstorno)
            {
            	case CalculoEstornoPulaPula.EXPURGO:
            	    result.add(CalculoEstornoPulaPula.EXPURGO, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    totalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    resultTotalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    totalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    resultTotalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    cdr.setFfDiscount(String.valueOf(DescontoPulaPula.EXPURGO));
            	    break;
            	case CalculoEstornoPulaPula.EXPURGO_SATURADO:
            	    result.add(CalculoEstornoPulaPula.EXPURGO_SATURADO, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    totalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    resultTotalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    totalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    resultTotalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    cdr.setFfDiscount(String.valueOf(DescontoPulaPula.EXPURGO));
            	    break;
            	case CalculoEstornoPulaPula.EXPURGO_AGENDADO_DEFAULT:
            	    result.add(CalculoEstornoPulaPula.EXPURGO, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    result.add(CalculoEstornoPulaPula.EXPURGO_AGENDADO_DEFAULT, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    totalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    resultTotalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    totalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    resultTotalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    cdr.setFfDiscount(String.valueOf(DescontoPulaPula.EXPURGO));
            	    break;
            	case CalculoEstornoPulaPula.EXPURGO_AGENDADO_PARCIAL:
            	    result.add(CalculoEstornoPulaPula.EXPURGO, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    result.add(CalculoEstornoPulaPula.EXPURGO_AGENDADO_PARCIAL, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    totalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    resultTotalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE, cdr.getCallDuration());
            	    totalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    resultTotalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    cdr.setFfDiscount(String.valueOf(DescontoPulaPula.EXPURGO));
            	    break;
            	case CalculoEstornoPulaPula.ESTORNO:
            	    result.add(CalculoEstornoPulaPula.ESTORNO, BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn));
            	    totalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE, cdr.getCallDuration());
            	    resultTotalizacao.add(TotalizacaoPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE, cdr.getCallDuration());
            	    totalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    resultTotalizacao.add(new Long(cdr.getFfDiscount()).intValue(), -cdr.getCallDuration());
            	    cdr.setFfDiscount(String.valueOf(DescontoPulaPula.ESTORNO));
            	    break;
            	default: break;
            }
        }
        
        //Obtendo o valor efetivo de estorno.
        Assinante assinante = pAssinante.getAssinante();
        double valorEstornoEfetivo = (result.getValorEstorno() <= assinante.getCreditosBonus()) ? 
        								result.getValorEstorno() : assinante.getCreditosBonus();
        result.setValorEstornoEfetivo(valorEstornoEfetivo);
        
        //Atribuindo as informacoes de totalizacao e os CDR's atualizados.
        result.setTotalizacao(resultTotalizacao);
        result.setListaCdrs(listaCdrs);
        
        return result;
    }
    
    /**
     *	Determina o tipo de estorno a ser executado para o CDR.
     *
     *	@param		cdr						Informacoes da ligacao recebida.
     *	@param		pAssinante				Informacoes referentes a promocao Pula-Pula do assinante.
     *	@param		listaHistorico			Lista com o historico do Pula-Pula para o assinante.
     *	@return		Identificador do tipo de estorno, conforme definido na classe CalculoEstornoPulaPula.
     */
    private int calculaTipoEstorno(ArquivoCDR cdr, AssinantePulaPula pAssinante, Collection listaHistorico)
    {
        int tipo = -1;
        
        //Se o assinante nao recebeu bonus no periodo, ou se o seu bonus nao esta agendado, o CDR deve ser expurgado.
        if(this.bonusRecebido(cdr, listaHistorico, pAssinante.getBonusConcedidos()))
            tipo = CalculoEstornoPulaPula.ESTORNO;
        else if(this.bonusRecebido(cdr, listaHistorico, pAssinante.getBonusAgendados(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT)))
            tipo = CalculoEstornoPulaPula.EXPURGO_AGENDADO_DEFAULT;
        else if(this.bonusRecebido(cdr, listaHistorico, pAssinante.getBonusAgendados(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL)))
            tipo = CalculoEstornoPulaPula.EXPURGO_AGENDADO_PARCIAL;
        else
            tipo = CalculoEstornoPulaPula.EXPURGO;
        
        //Se o assinante ultrapassou o limite de sua promocao, e necessario calcular o valor bruto do bonus Pula-Pula
        //para determinar se o CDR tem influencia sobre o valor liquido. Se influenciar, e necessario realizar o
        //estorno. Caso contrario, e necessario somente o expurgo.
        SaldoPulaPula saldo = pAssinante.getSaldo();
        if(saldo.isLimiteUltrapassado())
        {
            BonusPulaPula	bonusCn		= pAssinante.getBonusCn();
            double			bonusCdr	= BonificadorPulaPula.calcularBonusPulaPula(cdr, bonusCn);
            double			bonusBruto	= saldo.getValorBruto();
            double			limite		= saldo.getLimite();
            
            if((bonusBruto - limite - bonusCdr) >= 0)
                tipo = CalculoEstornoPulaPula.EXPURGO_SATURADO;
        }
        
        return tipo;
    }
    
    /**
     *	Verifica se o assinante recebeu bonus pela ligacao. Para esta verificacao e necessario primeiro comparar a
     *	data de importacao do CDR com a data de execucao do processo do Pula-Pula para o assinante. Apos esta
     *	comparacao e necessario verificar se o assinante efetivamente recebeu o bonus no periodo, ou seja, se existe 
     *	uma recarga referente ao bonus na lista de bonus concedidos ao assinante, ou se o bonus esta agendado na fila
     *	de recargas. E considerado como permissa do metodo que os bonus concedidos ao assinante correspondem ao mesmo 
     *	periodo de analise do CDR.
     *
     *	@param		cdr						Informacoes da ligacao recebida.
     *	@param		listaHistorico			Lista com o historico do Pula-Pula para o assinante.
     *	@param		listaBonus				Lista com os bonus concedidos ou agendados ao assinante no periodo.
     *	@return		True se o assinante recebeu bonus e false caso contrario.
     */
    private boolean bonusRecebido(ArquivoCDR cdr, Collection listaHistorico, Collection listaBonus) 
    {
        //Percorrendo a lista de historico do assinante.
        for(Iterator iteratorHistorico = listaHistorico.iterator(); iteratorHistorico.hasNext();)
        {
            HistoricoPulaPula historico = (HistoricoPulaPula)iteratorHistorico.next();
            
            //Verificando se a data de importacao do CDR e anterior a data de execucao do processo. Em caso
            //positivo, significa que o CDR ja havia sido sumarizado na totalizacao e foi contabilizado para o 
            //calculo do bonus.
            if((historico.getIdtCodigoRetorno().intValue() == Definicoes.RET_OPERACAO_OK) && 
               (historico.getDatExecucao().compareTo(cdr.getDatImportacaoCdr()) > 0))
                for(Iterator iteratorBonus = listaBonus.iterator(); iteratorBonus.hasNext();)
                {
                    Object bonus = iteratorBonus.next();
                    Date dataBonus = null;
                    
                    if(bonus instanceof Recarga)
                        dataBonus = ((Recarga)bonus).getDatInclusao();
                    else if(bonus instanceof FilaRecargas)
                        dataBonus = ((FilaRecargas)bonus).getDatCadastro();
                    
                    if(dataBonus.compareTo(historico.getDatExecucao()) >= 0)
                        return true;
                }
        }
        
        return false;
    }
    
    /**
     *	Valida o assinante para determinar se esta apto a receber o bonus.
     *
     *	@param		tipoExecucao			Tipo de execucao (DEFAULT, REBARBA, PARCIAL).
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@return		Codigo de retorno da validacao.
     *	@throws		Exception
     */
    protected short validaAssinante(String tipoExecucao, AssinantePulaPula pAssinante, Date dataReferencia) throws Exception
    {
        //Validacao: As informacoes da promocao do assinante sao validas.
        if(pAssinante == null) 
            return Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE;
        //Validacao: A promocao e valida.
        Promocao promocao = pAssinante.getPromocao();
        if(promocao == null)
        	return Definicoes.RET_PROMOCAO_NAO_EXISTE;
        if(!pAssinante.getStatus().isAtivo())
            return pAssinante.getStatus().validarStatus(true);
        //Validacao: A categoria promocao e valida.
        PromocaoCategoria categoria = promocao.getCategoria();
        if((categoria == null) && (categoria.getIdtCategoria() != Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA))
            return Definicoes.RET_PROMOCAO_CATEGORIA_INVALIDA;
		//Validacao: A promocao esta dentro do prazo de validade. 
		if(!promocao.isValidadeVigente(dataReferencia))
			return Definicoes.RET_PROMOCAO_VALIDADE_NOK;
        //Validacao: O assinante esta ativo na plataforma.
        Assinante assinante = pAssinante.getAssinante();
        if(assinante == null)
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        if(assinante.getRetorno() != Definicoes.RET_OPERACAO_OK)
            return assinante.getRetorno();
        //Validacao: O status do assinante e valido.
        int status = assinante.getStatusAssinante();
        if(status != Definicoes.STATUS_NORMAL_USER) 
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        //Validacao: O tipo de execucao e valido para a promocao do assinante. Para executar esta validacao e feita 
        //a verificacao de existencia do tipo de transacao para o tipo de execucao (vide TBL_PRO_TIPO_TRANSACAO).
        if(pAssinante.getTiposTransacao(tipoExecucao).isEmpty())
        	return Definicoes.RET_PROMOCAO_TIPO_EXEC_INVALIDO;
        //Validacao: O status do assinante esta previsto para mudar para invalido antes do ultimo dia de concessao
        //de bonus da sua promocao.
        //Independentemente do tipo de execucao, o assinante deve estar apto a receber o ate o final do proximo
        //periodo de concessao da sua promocao. No caso da concessao parcial, o assinante deve estar apto a 
        //receber o bonus no proximo periodo de concessao, ou seja, no mes seguinte. No caso da concessao default,
        //os creditos do assinante nao podem expirar antes do ultimo dia de concessao de bonus. Ja no caso da
        //rebarba, basta que a data de expiracao do assinante seja maior que a data de referencia para concessao
        //do bonus passada por parametro, uma vez que o processo e executado apos o periodo de concessao default
        //da promocao. Neste caso, a data de referencia e utilizada pelo objeto calRecarga porque apos o periodo 
        //de concessao default a data de execucao do assinante e atualizada para o proximo mes.
        Integer idtPromocao = new Integer(promocao.getIdtPromocao());
        Calendar calRecarga = Calendar.getInstance();
        calRecarga.setTime(dataReferencia);
        if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL))
            calRecarga.add(Calendar.MONTH, 1);
        
        //Por definicao o calculo de previsao de expiracao do saldo principal antes do ultimo dia de concessao 
        //e feito sempre em funcao do tipo de execucao default.
        PromocaoDiaExecucao maxDiaRecarga = super.consulta.getPromocaoMaxDiaExecucaoRecarga(idtPromocao, Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT);
        //Se o assinante nao possuir configuracoes de datas de execucao, nao deve ser validado por previsao de
        //mudanca para status invalido, uma vez que nao possui periodo de concessao.
        if(maxDiaRecarga != null)
        {
            calRecarga.set(Calendar.DAY_OF_MONTH, maxDiaRecarga.getNumDiaExecucaoRecarga().intValue());
            SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
            //De acordo com alinhamento com Daniel Abib, Daniel Ferreira, Nelson Yoshida e Tayna, o processo deve 
            //conceder o bonus mesmo quando a data de expiracao do assinante for igual ao ultimo dia de concessao 
            //para o tipo de execucao PARCIAL.
            if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL))
            {
                if(calRecarga.getTime().compareTo(conversorDate.parse(assinante.getDataExpiracaoPrincipal())) > 0)
                    return Definicoes.RET_PROMOCAO_PREVISAO_MUD_STATUS;
            }
            else
            {
                if(calRecarga.getTime().compareTo(conversorDate.parse(assinante.getDataExpiracaoPrincipal())) >= 0)
                    return Definicoes.RET_PROMOCAO_PREVISAO_MUD_STATUS;
            }
        }
        //Validacao: O status de servico do assinante e valido.
        int statusServico = assinante.getStatusServico();
        if(statusServico != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        //Validacao: O assinante ja recebeu o bonus no periodo. Verificar na lista de ha algum bonus concedido ou a 
        //conceder no mes da execucao. 
        Collection bonusConcedidos = pAssinante.getBonusConcedidos(tipoExecucao);
        Collection bonusAgendados = pAssinante.getBonusAgendados(tipoExecucao);
        Calendar calReferencia = Calendar.getInstance();
        calReferencia.setTime(dataReferencia);
        if((bonusConcedidos != null) && (bonusConcedidos.size() > 0)) 
            for(Iterator iterator = bonusConcedidos.iterator(); iterator.hasNext();)
            {
                Recarga recarga = (Recarga)iterator.next();
                calRecarga.setTime(recarga.getDatRecarga());
                if(calReferencia.get(Calendar.MONTH) == calRecarga.get(Calendar.MONTH))
                    return Definicoes.RET_PROMOCAO_BONUS_CONCEDIDO;
            }
        if((bonusAgendados != null) && (bonusAgendados.size() > 0))
            for(Iterator iterator = bonusAgendados.iterator(); iterator.hasNext();)
            {
                FilaRecargas recarga = (FilaRecargas)iterator.next();
                if(recarga.getDatExecucao() == null)
                    return Definicoes.RET_PROMOCAO_BONUS_CONCEDIDO;
                calRecarga.setTime(recarga.getDatExecucao());
                if(calReferencia.get(Calendar.MONTH) == calRecarga.get(Calendar.MONTH))
                    return Definicoes.RET_PROMOCAO_BONUS_CONCEDIDO;
            }
        //Validacao: O assinante recebeu ligacoes no periodo.
        //OBS: Esta validacao deve ser a ultima porque tambem insere registros na fila de recargas.
        TotalizacaoPulaPula totalizacao = pAssinante.getTotalizacao();
        SaldoPulaPula saldo = pAssinante.getSaldo();
        if((totalizacao == null) || (totalizacao.getNumSegundosTotal() == null) || (totalizacao.getNumSegundosTotal().longValue() == 0) ||
           ((saldo != null) && (saldo.getValorAReceber() == 0)))
            return Definicoes.RET_PROMOCAO_LIGACOES_NOK;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /**
     *	Valida o assinante para o processo de estorno de bonus Pula-Pula por fraude.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@return		Codigo de retorno da validacao.
     */
    private short validaAssinanteEstornoPulaPula(AssinantePulaPula pAssinante)
    {
        if(pAssinante == null)
            return Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE;
        if(pAssinante.getPromocao() == null)
            return Definicoes.RET_PROMOCAO_NAO_EXISTE;
        if(pAssinante.getAssinante() == null)
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        if(pAssinante.getAssinante().getRetorno() != Definicoes.RET_OPERACAO_OK)
            return pAssinante.getAssinante().getRetorno();
        
        return pAssinante.getStatus().validarStatus(true);
    }
    
    /**
     *	Insere o bonus concedido ao assinante pela Promocao Pula-Pula na fila de recargas.
     *
     *	@param		resultValidacao			Codigo de retorno da validacao do assinante.
     *	@param		tipoExecucao			Tipo de execucao (DEFAULT, PARCIAL).
     *	@param		pAssinante				Informacoes referentes a promocao Pula-Pula do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Codigo de retorno da operacao.
     */
    private short insereFilaRecargas(short				validacao, 
    								 String				tipoExecucao, 
									 AssinantePulaPula	pAssinante, 
                                     Date				dataProcessamento, 
									 PREPConexao		conexaoPrep)
    {
        short			result		= Definicoes.RET_OPERACAO_OK;
        FilaRecargasDAO bonusDAO	= new FilaRecargasDAO(conexaoPrep);
        
        try
        {
        	//Identificador de dependencia de processamento na Fila de Recargas. Precisa ser definido durante a 
        	//concessao de bonus Pula-Pula para que somente a primeira bonificacao possa zerar os saldos. 
        	Integer idRegistroDependencia = null;
        	
        	for(Iterator iterator = pAssinante.toFilaRecargas(tipoExecucao).iterator(); iterator.hasNext();)
        	{
        		FilaRecargas bonusAgendado = (FilaRecargas)iterator.next();
        		
        		//Caso o codigo de retorno nao seja Operacao OK, nao deve ser enviado SMS. O caso em que o bonus 
        		//e inserido na Fila de Recargas mesmo quando o assinante nao foi validade e quando o assinante nao 
        		//recebeu ligacoes. A insercao garante que o bonus seja expirado apos o periodo de concessao.
        		if(validacao != Definicoes.RET_OPERACAO_OK)
        			bonusAgendado.setIndEnviaSms(null);
                //Marcando as flags para zerar saldos de acordo com o status da promocao do assinante.
                pAssinante.getStatus().marcarZerarSaldos(bonusAgendado);
                //Atribuindo o identificador da dependencia de consumo. Caso o idRegistro seja null, significa que 
                //e o primeiro registro a ser processado. Os registros seguintes dependem da correta execucao dele.
            	bonusAgendado.setIdRegistro(bonusDAO.newIdRegistro());
            	bonusAgendado.setIdRegistroDependencia(idRegistroDependencia);
            	idRegistroDependencia = bonusAgendado.getIdRegistro();
                
                //Atualiza o numero de dias de expiracao do Saldo Periodico na Fila de Recargas, uma vez que as 
                //recargas de cartao nao atualizam a data de expiracao deste saldo.
                MapConfiguracaoGPP mapConf = MapConfiguracaoGPP.getInstance();
                int numDiasExpPeriodico = 
                	Integer.parseInt(mapConf.getMapValorConfiguracaoGPP("PULA_PULA_NUM_DIAS_EXP_PERIODICO"));
                bonusAgendado.setNumDiasExpPeriodico(new Integer(numDiasExpPeriodico));
                
                //Inserindo registro na fila de recargas.
                bonusDAO.insereRecargaNaFila(bonusAgendado);
        	}
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "insereFilaRecargas", pAssinante + " - Excecao: " + e);
            result = Definicoes.RET_PROMOCAO_VALIDACAO_OK_REC_NOK;
        }
        
        return result;
    }
    
    /**
     *	Insere registro no historico de execucao da promocao Pula-Pula do assinante.
     *
     *	@param		result					Codigo de retorno da operacao.
     *	@param		pAssinante				Informacoes referentes a promocao Pula-Pula do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador que executou a operacao.
     *	@param		motivo					Identificador do motivo da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     */
    private void insereHistoricoPulaPula(short result, 
    									 AssinantePulaPula pAssinante, 
										 Date dataProcessamento, 
										 String operador, 
										 int motivo, 
										 PREPConexao conexaoPrep)
    {
        try
        {
            //Obtendo os valores do registro de historico.
            String idtMsisdn = (pAssinante != null) ? pAssinante.getIdtMsisdn() : null;
            Promocao promocao = (pAssinante != null) ? pAssinante.getPromocao() : null; 
            Integer idtPromocao = (promocao != null) ? new Integer(promocao.getIdtPromocao()) : null;
            String desStatusExecucao = (result == Definicoes.RET_OPERACAO_OK) ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO;
            Integer idtCodigoRetorno = new Integer(result);
            SaldoPulaPula saldo = (pAssinante != null) ? pAssinante.getSaldo() : null;
            double valorBonus = (saldo != null) ? saldo.getValorAReceber() : 0.0;
            Double vlrCreditoBonus = (result == Definicoes.RET_OPERACAO_OK) ? new Double(valorBonus) : new Double (0.0);
            
            //Obtendo objeto HistoricoPulaPula para insercao no historico.
            HistoricoPulaPula historico = new HistoricoPulaPula();
            historico.setIdtMsisdn(idtMsisdn);
            historico.setIdtPromocao(idtPromocao);
            historico.setDatExecucao(new Timestamp(dataProcessamento.getTime()));
            historico.setNomOperador(operador);
            historico.setIdtMotivo(new Integer(motivo));
            historico.setDesStatusExecucao(desStatusExecucao);
            historico.setIdtCodigoRetorno(idtCodigoRetorno);
            historico.setVlrCreditoBonus(vlrCreditoBonus);
            
            //Inserindo o historico.
            if(!super.operacoes.insereHistoricoPulaPula(historico, conexaoPrep))
                super.log(Definicoes.WARN, 
                		  "insereHistoricoPulaPula", 
                		  pAssinante + " - Historico do assinante nao inserido.");
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "insereHistoricoPulaPula", pAssinante + " - Excecao: " + e);
            super.log(Definicoes.WARN, 
            		  "insereHistoricoPulaPula",
            		  pAssinante + " - Historico do assinante nao inserido.");
        }
    }

    /**
     *	Consulta o credito Pula Pula do assinante.
     *
     *	@param		msisdn					Recebe o msisdn do assinante.
     *	@param		mes					    Recebe a data atual.
     *	@return		Valor do crédito do assinante.
     */
    public double consultarCreditoPulaPula(String msisdn, String mes) throws Exception
    {
    	PREPConexao conexaoPrep = null;
	    
    	try 
    	{
    		//Obtendo conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            SimpleDateFormat	conversorDatMes	= new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
            Date				dataReferencia	= conversorDatMes.parse(mes);
            
            int[] detalhes = 
            {
            	ControlePulaPula.BONUS_PULA_PULA,
            	ControlePulaPula.TOTALIZACAO,
            	ControlePulaPula.SALDO_PULA_PULA,
            };
            
            AssinantePulaPula pAssinante = this.consultaPromocaoPulaPula(msisdn, 
            															 detalhes, 
            															 dataReferencia, 
            															 false, 
            															 false, 
            															 conexaoPrep);
            
            if(pAssinante != null)
            	return pAssinante.getSaldo().getValorBruto();
    	}
    	finally
    	{
        	super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
    	}
    	
    	return 0.0;    	
    }
    
}
