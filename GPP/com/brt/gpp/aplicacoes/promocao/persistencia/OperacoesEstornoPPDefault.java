package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.sql.Timestamp;
import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.OperacoesEstornoPPFactory;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.Operacoes;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;

/**
 *	Classe que implementa as operacoes do processo de Estorno de Bonus Pula-Pula por Fraude.
 *
 *	@author		Daniel Ferreira
 *	@since		21/11/2006
 */
public class OperacoesEstornoPPDefault extends OperacoesEstornoPPValidacao 
{

	/**
	 *	Objeto para implementacao de logica de negocio referente a promocoes. Contem as regras para execucao do estorno.
	 */
	private ControlePromocao controle;
	
	/**
	 *	DAO para operacoes no banco de dados.
	 */
	private Operacoes operacoes;
	
	
    /**
     *	Construtor da classe.
     *
     *	@param		arquivo					Interface para operacoes no arquivo de escrita.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@param		factory					Factory de operacoes do processo de Estorno de Bonus Pula-Pula por Fraude.
     */
    public OperacoesEstornoPPDefault(ArquivoEscrita arquivo, PREPConexao conexaoPrep, OperacoesEstornoPPFactory factory)
    {
    	super(arquivo, conexaoPrep, factory);
    	
    	this.controle	= new ControlePromocao(conexaoPrep.getIdProcesso());
    	this.operacoes	= new Operacoes(conexaoPrep.getIdProcesso());
    }
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean autoCommit) throws Exception
	{
    	super.conexaoPrep.setAutoCommit(autoCommit);
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#commit()
     */
    public void commit() throws Exception
    {
    	super.conexaoPrep.commit();
    }
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#rollback()
     */
    public void rollback() throws Exception
	{
    	super.conexaoPrep.rollback();
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarInterfaceEstornoPulaPula(Date, String, String, InterfaceEstornoPulaPula)
     */
    public void atualizarInterfaceEstornoPulaPula(Date dataReferencia, String msisdn, String numeroOrigem, InterfaceEstornoPulaPula estorno) throws Exception
	{
    	this.operacoes.atualizaInterfaceEstornoPulaPula(dataReferencia, msisdn, numeroOrigem, estorno, super.conexaoPrep);
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarFFDiscountCDR(String, Date, long, String, long, int)
     */
    public void atualizarFFDiscountCDR(String subId, Date timestamp, long startTime, String callId, long transactionType, int ffDiscount) throws Exception
	{
    	this.operacoes.atualizaFFDiscountCDR(subId, timestamp, new Long(startTime), callId, new Long(transactionType), new Integer(ffDiscount), super.conexaoPrep);
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarTotalizacaoPulaPula(String, String, TotalizacaoPulaPula)
     */
    public void atualizarTotalizacaoPulaPula(String msisdn, String mes, TotalizacaoPulaPula totalizacao) throws Exception
	{
    	this.operacoes.adicionarTotalizacaoPulaPula(msisdn, mes, totalizacao, super.conexaoPrep);
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#inserirPromocaoEstornoPulaPula(PromocaoEstornoPulaPula)
     */
    public void inserirPromocaoEstornoPulaPula(PromocaoEstornoPulaPula estorno) throws Exception
	{
    	this.operacoes.inserePromocaoEstornoPulaPula(estorno, super.conexaoPrep);
    	super.inserirPromocaoEstornoPulaPula(estorno);
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarFilaRecargas(String, String, Date, FilaRecargas)
     */
    public void atualizarFilaRecargas(String msisdn, String tipoTransacao, Date dataCadastro, FilaRecargas bonusAgendado) throws Exception
	{
    	this.operacoes.atualizaFilaRecargas(msisdn, tipoTransacao, new Timestamp(dataCadastro.getTime()), bonusAgendado, super.conexaoPrep);
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#estornar(AssinantePulaPula, double, Date)
     */
    public short estornar(AssinantePulaPula pAssinante, double valorBonus, Date dataProcessamento)
    {
    	return this.controle.executaBonus(pAssinante, Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_FRAUDE, valorBonus, new Timestamp(dataProcessamento.getTime()), null);
    }
    
}
