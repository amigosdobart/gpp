package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Interface responsavel pelas operacoes de acesso a dados e ajuste no processo de Estorno de Bonus Pula-Pula por Fraude. 
 * 
 *	@author		Daniel Ferreira
 *	@since		20/11/2006
 */
public interface OperacoesEstornoPulaPula 
{
	
	/**
	 *	Retorna a conexao com o banco de dados.
	 *
	 *	@return		Conexao com o banco de dados.
	 */
	public PREPConexao getConexao();
	
	/**
	 *	Realiza as operacoes de finalizacao do objeto.
	 */
	public void close();
	
	/**
	 *	Configura o objeto para executar transacoes auto-commit ou nao. Caso a flag indicadora seja true, o processo
	 *	sera auto-commit e o contrario no caso de ser false.
	 *
	 * 	@param		autoCommit				Flag indicadora de transacao auto-commit.
	 * 	@throws		Exception   
	 */
	public void setAutoCommit(boolean autoCommit) throws Exception;
	
	/**
	 *	Confirma as alteracoes efetuadas pela transacao. Caso a transacao tenha sido configurada como auto-commit, o
	 *	metodo nao tera efeito.
	 *
	 * 	@throws		Exception   
	 */
	public void commit() throws Exception;
	
	/**
	 *	Desfaz as alteracoes efetuadas pela transacao. Caso a transacao tenha sido configurada como auto-commit, o
	 *	metodo nao tera efeito e as alteracoes ja terao sido confirmadas no momento da execucao.
	 *
	 * 	@throws		Exception   
	 */
	public void rollback() throws Exception;
	
	/**
	 *	Atualiza as informacoes de requisicao de Estorno de Bonus Pula-Pula por Fraude configuradas na tabela de interface.
	 *
	 *	@param		dataReferencia			Data em que as ligacoes foram efetuadas (A para B).
	 *	@param		msisdn					MSISDN do assinante que recebeu as ligacoes (B).
	 *	@param		numeroOrigem			Numero originador das ligacoes (A).
	 *	@param		estorno					Objeto com as informacoes da requisicao de estorno atualizadas.
	 * 	@throws		Exception   
	 */
	public void atualizarInterfaceEstornoPulaPula(Date dataReferencia, String msisdn, String numeroOrigem, InterfaceEstornoPulaPula estorno) throws Exception;
	
	/**
	 *	Atualiza a marcacao de desconto Pula-Pula do CDR.
	 *
     *	@param		subId					MSISDN do assinante.
     *	@param		timestamp				Data/hora do CDR.
     *	@param		startTime				Horario de inicio do CDR.
     *	@param		callId					Identificador do outro numero que executou a ligacao.
     *	@param		transactionType			Tipo de transacao do CDR.
     *	@param		ffDiscount				Valor da flag do FFDiscount, utilizado para determinar o tipo de desconto da chamada recebida.
	 * 	@throws		Exception   
	 */
	public void atualizarFFDiscountCDR(String subId, Date timestamp, long startTime, String callId, long transactionType, int ffDiscount) throws Exception;
	
    /**
     *	Atualiza o registro de sumarizacao de informacoes para o calculo do bonus da promocao Pula-Pula para o assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		mes						Mes de analise de ligacoes recebidas.
     *	@param		totalizacao				Informacoes para o calculo do bonus para o assinante.
     *	@throws		Exception
     */
	public void atualizarTotalizacaoPulaPula(String msisdn, String mes, TotalizacaoPulaPula totalizacao) throws Exception;
	
    /**
     *	Insere registro de historico de estorno de bonus Pula-Pula para o assinante.
     *
     *	@param		estorno					Informacoes de historico de estorno.
     *	@throws		Exception
     */
	public void inserirPromocaoEstornoPulaPula(PromocaoEstornoPulaPula estorno) throws Exception;
	
    /**
     *	Atualiza o registro de bonus agendado na fila de recargas de acordo com o objeto passado por parametro. Devido
     *	a possiveis problemas de concorrencia com o processo de consumo da fila de recargas, o metodo NAO atualiza
     *	o status de processamento nem o codigo de retorno. Caso contrario, e possivel que o metodo volte o status
     *	do registro na fila de recargas para nao processado, gerando duplicacao de concessao de bonus.
     *
     *	@param		msisdn						MSISDN do assinante.
     *	@param		tipoTransacao				Tipo de Transacao da recarga.
     *	@param		dataCadastro				Data de cadastro do registro na fila de recargas.
     *	@param		bonusAgendado				Objeto representando o registro do bonus agendado na fila de recargas.
     *	@throws		Exception
     */
	public void atualizarFilaRecargas(String msisdn, String tipoTransacao, Date dataCadastro, FilaRecargas bonusAgendado) throws Exception;
	
    /**
     *	Executa o estorno (ajuste de debito) do saldo do assinante referente ao bonus recebido com ligacoes indevidas.
     *
     *	@param		pAssinante				Informacoes referentes a promocao Pula-Pula do assinante.
     *	@param		valorBonus				Valor de bonus a ser concedido.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@return		Codigo de retorno da operacao.
     */
	public short estornar(AssinantePulaPula pAssinante, double valorBonus, Date dataProcessamento);
	
}
