package com.brt.gpp.aplicacoes.enviarSMS;


import java.util.Calendar;
import java.util.NoSuchElementException;

import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.smpp.ConexaoSMPP;
import com.brt.gpp.comum.conexoes.smpp.RequisicaoSMSC;
import com.brt.gpp.comum.conexoes.smpp.RespostaSMSC;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolSMPP;

/**
 * A classe <code>EnvioSMSConsumidor</code> e responsavel por consumir os dados da fila de SMS
 * instanciada no produtor <code>EnvioSMSProdutor</code>.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 27/12/2007
 */
public class EnvioSMSConsumidor extends Thread
{
	private EnvioSMSProdutor		produtor;
	private GerentePoolLog			log;
	private GerentePoolSMPP			poolSMPP;
	private GerentePoolBancoDados	poolBanco;
	private long					idProcesso;

	public static final String CLASSE = "EnvioSMSConsumidor";

	public EnvioSMSConsumidor(EnvioSMSProdutor produtor)
	{
		this.produtor = produtor;
		log = GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso = log.getIdProcesso("EnvioSMSConsumidor");
	}

	public void run()
	{
		poolSMPP = GerentePoolSMPP.getInstancia(idProcesso);
		poolBanco = GerentePoolBancoDados.getInstancia(idProcesso);

		while(produtor.isDeveConsumirSMS())
		{
			DadosSMS sms = produtor.getDadosSMS();

			if(sms == null)
				continue;

			try
			{
				ConexaoSMPP conexao = poolSMPP.getConexao(sms.getTipoSMS().getIdConexao());

				if(conexao == null)
					continue;

				RequisicaoSMSC requisicao = conexao.getRequisicaoSMSC();

				log.log(idProcesso, Definicoes.DEBUG, CLASSE, "run",
						"Enviando SubmitSM pela conexao: "+conexao.getConexaoConfig().toString());

				requisicao.enviarSubmitSM(sms.getIdtMsisdnOrigem(), sms.getIdtMsisdn(),
						sms.getDesMensagem(), sms.getTipoSMS().deveNotificarEntrega(), (int)sms.getIdRegistro());
			}
			catch(NoSuchElementException e)
			{
				log.log(idProcesso, Definicoes.ERRO, CLASSE, "run",
						"Erro ao recuperar conexao SMPP do pool.");
				continue;
			}
			catch (Exception e)
			{
				log.log(idProcesso, Definicoes.ERRO, CLASSE, "run",
						"Erro ao enviar SubmitSM a SMSC: "+log.traceError(e));
				continue;
			}

			sms.setIdtStatusProcessamento(RespostaSMSC.SMS_STATUS_ENVIANDO);
			sms.setDatProcessamento(Calendar.getInstance().getTime());

			PREPConexao conexaoBanco = null;

			try
			{
				conexaoBanco = poolBanco.getConexaoPREP(idProcesso);
				EnvioSMSDAO dao = new EnvioSMSDAO(idProcesso, conexaoBanco);

				dao.alterarSMS(sms);
			}
			catch (GPPInternalErrorException e)
			{
				log.log(idProcesso, Definicoes.ERRO, CLASSE, "run",
						"Erro ao atualizar status do SMS: "+sms.getIdRegistro()+"Erro:"+log.traceError(e));
			}
			finally
			{
				if(conexaoBanco != null)
					poolBanco.liberaConexaoPREP(conexaoBanco, idProcesso);
			}
		}
	}
}
