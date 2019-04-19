package com.brt.gpp.aplicacoes.notificacaoExpiracao;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;


public class NotificacaoExpiracaoConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{

	private PREPConexao	conexaoPrep;
	private NotificacaoExpiracaoProdutor produtor;
	private long idProcesso = GerentePoolLog.getInstancia(NotificacaoExpiracaoConsumidor.class).getIdProcesso(Definicoes.CL_NOTIFICACAO_EXPIRACAO);


	public NotificacaoExpiracaoConsumidor()
	{
		super(GerentePoolLog.getInstancia(NotificacaoExpiracaoConsumidor.class).getIdProcesso(Definicoes.CL_NOTIFICACAO_EXPIRACAO),Definicoes.CL_NOTIFICACAO_EXPIRACAO);
	}


	public void startup() throws Exception
	{
		conexaoPrep = produtor.getConexao();
	}


	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.produtor = (NotificacaoExpiracaoProdutor) produtor;
		startup();
	}


	public void startup(Produtor produtor) throws Exception
	{
		startup();
	}


	public void execute(Object obj) throws Exception
	{

		NotificacaoExpiracaoVO notifExpiracao = (NotificacaoExpiracaoVO) obj;

		//substitui variaveis % da mensagem
		Date datExpPrincipal = notifExpiracao.getDataExpiracaoPrincipal();

		String notificacaoSMSTemplate = notifExpiracao.getNotificacaoSMS();

		String msgNotificacaoSMS;
		msgNotificacaoSMS = GPPData.substituiTexto("%1", notificacaoSMSTemplate, notifExpiracao.getNDias());
		msgNotificacaoSMS = GPPData.substituiTexto("%2", msgNotificacaoSMS, new SimpleDateFormat("dd/MM/yyyy").format(datExpPrincipal));
		msgNotificacaoSMS = GPPData.substituiTexto("%3", msgNotificacaoSMS, notifExpiracao.getValorSaldoPrincipal());
		msgNotificacaoSMS = GPPData.substituiTexto("%4", msgNotificacaoSMS, notifExpiracao.getValorSaldoBonus());
		msgNotificacaoSMS = GPPData.substituiTexto("%5", msgNotificacaoSMS, notifExpiracao.getValorSaldoSM());
		msgNotificacaoSMS = GPPData.substituiTexto("%6", msgNotificacaoSMS, notifExpiracao.getValorSaldoDados());
		msgNotificacaoSMS = GPPData.substituiTexto("%7", msgNotificacaoSMS, new SimpleDateFormat("dd/MM").format(datExpPrincipal));

		ConsumidorSMS consumidorSMS = ConsumidorSMS.getInstance(idProcesso);

		boolean gravou = consumidorSMS.gravaMensagemSMS(notifExpiracao.getMsisdn(),
				msgNotificacaoSMS, notifExpiracao.getPrioridade(),
				notifExpiracao.getSmsStatus(), conexaoPrep, idProcesso);

		produtor.incrementaContador();

		super.log(Definicoes.DEBUG,"execute","Usuario "+notifExpiracao.getMsisdn() + (gravou ? " incluido na interface" : " nao incluido na interface"));

	}


	public void finish()
	{

	}


}// Fim da classe pública