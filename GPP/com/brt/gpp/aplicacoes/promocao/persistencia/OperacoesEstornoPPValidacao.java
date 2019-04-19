package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.OperacoesEstornoPPFactory;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;

/**
 *	Implementacao das operacoes em modo de validacao do processo de Estorno de Bonus Pula-Pula por Fraude. A classe
 *	nao executa integralmente o Expurgo/Estorno para os assinantes, mas constroi um arquivo de resultado para 
 *	posterior validacao e aprovacao pelo usuario.
 * 
 *	@author		Daniel Ferreira
 *	@since		20/11/2006
 */
public class OperacoesEstornoPPValidacao implements OperacoesEstornoPulaPula
{
	
	/**
	 *	Interface para operacoes no arquivo de escrita.
	 */
	protected ArquivoEscrita arquivo;

    /**
     *	Conexao com o banco de dados.
     */
	protected PREPConexao conexaoPrep;
    
    /**
     *	Factory de operacoes do processo de Estorno de Bonus Pula-Pula por Fraude.
     */
	protected OperacoesEstornoPPFactory factory;
    
    /**
     *	Construtor da classe.
     *
     *	@param		arquivo					Interface para operacoes no arquivo de escrita.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@param		factory					Factory de operacoes do processo de Estorno de Bonus Pula-Pula por Fraude.
     */
    public OperacoesEstornoPPValidacao(ArquivoEscrita arquivo, PREPConexao conexaoPrep, OperacoesEstornoPPFactory factory)
    {
    	this.arquivo		= arquivo;
    	this.conexaoPrep	= conexaoPrep;
    	this.factory		= factory;
    }
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#getConexao()
     */
    public PREPConexao getConexao()
    {
    	return this.conexaoPrep;
    }
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#close()
     */
    public void close()
    {
    	this.factory.close(this);
    }
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean autoCommit) throws Exception
	{
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#commit()
     */
    public void commit() throws Exception
    {
    }
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#rollback()
     */
    public void rollback() throws Exception
	{
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarInterfaceEstornoPulaPula(Date, String, String, InterfaceEstornoPulaPula)
     */
    public void atualizarInterfaceEstornoPulaPula(Date dataReferencia, String msisdn, String numeroOrigem, InterfaceEstornoPulaPula estorno) throws Exception
	{
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarFFDiscountCDR(String, Date, long, String, long, int)
     */
    public void atualizarFFDiscountCDR(String subId, Date timestamp, long startTime, String callId, long transactionType, int ffDiscount) throws Exception
	{
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarTotalizacaoPulaPula(String, String, TotalizacaoPulaPula)
     */
    public void atualizarTotalizacaoPulaPula(String msisdn, String mes, TotalizacaoPulaPula totalizacao) throws Exception
	{
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#inserirPromocaoEstornoPulaPula(PromocaoEstornoPulaPula)
     */
    public void inserirPromocaoEstornoPulaPula(PromocaoEstornoPulaPula estorno) throws Exception
	{
    	if(this.arquivo != null)
    		this.arquivo.escrever(estorno.toString() + "\n");
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#atualizarFilaRecargas(String, String, Date, FilaRecargas)
     */
    public void atualizarFilaRecargas(String msisdn, String tipoTransacao, Date dataCadastro, FilaRecargas bonusAgendado) throws Exception
	{
	}
    
    /**
     *	@see		com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula#estornar(AssinantePulaPula, double, Date)
     */
    public short estornar(AssinantePulaPula pAssinante, double valorBonus, Date dataProcessamento)
    {
    	return Definicoes.RET_OPERACAO_OK;
    }
    
}
