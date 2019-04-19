package com.brt.gpp.gerentesPool;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *
 * Este arquivo refere-se a classe ConsumidorSMS, responsavel pela implementacao
 * da logica de gerenciamento de varias Threads de envio de SMS
 *
 * <P>
 * Versao: 1.0
 *
 * @Autor: Joao Carlos Lemgruber Junior Data: 16/06/2004
 *
 * Modificado por: Data: Razao:
 *
 */

public class ConsumidorSMS
{
	private GerentePoolLog gerLog = null; // Referencia para o objeto de Log do GPP
	private long idProcesso;
	private static ConsumidorSMS consumidorSMS;

	private ConsumidorSMS(long idProcesso)
	{
		gerLog = GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso = idProcesso;
	}

	public static ConsumidorSMS getInstance(long idProcesso)
	{
		if(consumidorSMS == null)
			consumidorSMS = new ConsumidorSMS(idProcesso);

		return consumidorSMS;
	}

	/**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param msisdn		MSISDN de Destino.
     * @param mensagem		Mensagem a ser enviada.
     * @param prioridade	Prioridade de envio.
     * @param tipo			Tipo de SMS a ser enviado.
     * @param conexaoPrep	Conexao com o banco de dados.
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso;<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
	public boolean gravarMensagemSMS(String msisdn, String mensagem, int prioridade, String tipo, PREPConexao conexaoPrep)
			throws GPPInternalErrorException
	{
		DadosSMS sms = new DadosSMS();
		sms.setIdtMsisdn(msisdn);
		sms.setDesMensagem(mensagem);
		sms.setNumPrioridade(prioridade);
		sms.getTipoSMS().setIdtTipoSMS(tipo);

		return gravarMensagemSMS(sms, conexaoPrep, conexaoPrep.getIdProcesso());
	}

	/**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param msisdn		MSISDN de Destino.
     * @param mensagem		Mensagem a ser enviada.
     * @param prioridade	Prioridade de envio.
     * @param tipo			Tipo de SMS a ser enviado.
     * @param idProcesso	Numero do processo corrente.<br>
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso;<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
	public boolean gravaMensagemSMS (String msisdn, String mensagem, int prioridade, String tipo, long idProcesso) throws GPPInternalErrorException
    {
		boolean gravouMsg=true;
		GerentePoolBancoDados gerDB = GerentePoolBancoDados.getInstancia(idProcesso);
		PREPConexao conexaoPrep = null;

		try
		{
			conexaoPrep = gerDB.getConexaoPREP(idProcesso);

			DadosSMS sms = new DadosSMS();
			sms.setIdtMsisdn(msisdn);
			sms.setDesMensagem(mensagem);
			sms.setNumPrioridade(prioridade);
			sms.getTipoSMS().setIdtTipoSMS(tipo);

			gravouMsg = gravarMensagemSMS(sms, conexaoPrep, idProcesso);

			conexaoPrep.commit();

		}
		catch(java.sql.SQLException e)
		{
			gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_CONSUMIDOR_SMS,"gravaMensagemSMS","Erro interno. Excecao:"+e);
			throw new GPPInternalErrorException(e.getMessage());
		}
		finally
		{
			gerDB.liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return gravouMsg;
    }

	/**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param msisdn		MSISDN de Destino.
     * @param mensagem		Mensagem a ser enviada.
     * @param prioridade	Prioridade de envio.
     * @param tipo			Tipo de SMS a ser enviado.
     * @param conexaoPrep	Conexao com o banco de dados.
     * @param idProcesso	Numero do processo corrente.<br>
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso;<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
	public boolean gravaMensagemSMS(String msisdn, String mensagem, int prioridade, String tipo, PREPConexao conexaoPrep, long idProcesso)
			throws GPPInternalErrorException
	{
		boolean gravouMsg = true;
		try
		{
			DadosSMS sms = new DadosSMS();
			sms.setIdtMsisdn(msisdn);
			sms.setDesMensagem(mensagem);
			sms.setNumPrioridade(prioridade);
			sms.getTipoSMS().setIdtTipoSMS(tipo);

			gravouMsg = gravarMensagemSMS(sms, conexaoPrep, idProcesso);

			conexaoPrep.commit();
		}
		catch (java.sql.SQLException e)
		{
			gerLog.log(idProcesso, Definicoes.ERRO, Definicoes.CL_CONSUMIDOR_SMS, "gravaMensagemSMS", "Erro interno. Excecao:" + e);
			throw new GPPInternalErrorException(e.getMessage());
		}
		return gravouMsg;
	}

	/**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param sms			Dados do SMS a ser enviado.
     * @param conexaoPrep	Conexao com o banco de dados.
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso;<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
	public boolean gravarMensagemSMS(DadosSMS sms, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		return gravarMensagemSMS(sms, conexaoPrep, conexaoPrep.getIdProcesso());
	}

	/**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param sms			Dados do SMS a ser enviado.
     * @param idProcesso	Numero do processo corrente.<br>
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso;<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
	public boolean gravarMensagemSMS(DadosSMS sms, long idProcesso) throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
		boolean retorno = false;

		try
		{
			retorno = gravarMensagemSMS(sms, conexaoPrep, idProcesso);
		}
		finally
		{
			conexaoPrep.liberaConexao(idProcesso);
		}

		return retorno;
	}

	/**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param sms			Dados do SMS a ser enviado.
     * @param conexaoPrep	Conexao com o banco de dados.
     * @param idProcesso	Numero do processo corrente.<br>
     * 						Geralmente utilizado para controle de transacao
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
	public boolean gravarMensagemSMS(DadosSMS sms, PREPConexao conexaoPrep,	long idProcesso) throws GPPInternalErrorException
	{
		EnvioSMSDAO dao = new EnvioSMSDAO(idProcesso, conexaoPrep);

		return (dao.inserirSMS(sms) > 0);
	}
}
