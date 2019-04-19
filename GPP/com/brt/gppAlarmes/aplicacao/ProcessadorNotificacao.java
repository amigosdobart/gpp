package com.brt.gppAlarmes.aplicacao;

import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.entity.Evento;

/**
 * 
 * @author Joao Carlos
 * Data..: 05-Abr-2005
 *
 */
public class ProcessadorNotificacao
{
	private Alarme				alarme;
	private Evento				ultimoEvento;
	private NotificacaoPorEMail	notificacaoEMail;
	private NotificacaoPorSMS	notificacaoSMS;

	public ProcessadorNotificacao(Alarme alarme, Evento ultimoEvento)
	{
		this.alarme 			= alarme;
		this.ultimoEvento		= ultimoEvento;
		this.notificacaoEMail	= new NotificacaoPorEMail();
		this.notificacaoSMS		= new NotificacaoPorSMS();
	}

	/**
	 * Metodo....:processaNotificacaoAlerta
	 * Descricao.:Processa o envio de notificacao no caso de configuracao do alarme
	 *            indicar que o tipo Alerta deve ser notificado
	 */
	private void processaNotificacaoAlerta()
	{
		// Verifica se no status corrente a notificacao deve ser enviada por e-mail
		if (alarme.isEnviaEMailAlerta())
			notificacaoEMail.enviaNotificacao(alarme,ultimoEvento);
		
		// Verifica se no status corrente a notificacao deve ser enviada por SMS
		if (alarme.isEnviaSMSAlerta())
			notificacaoSMS.enviaNotificacao(alarme,ultimoEvento);		
	}
	
	/**
	 * Metodo....:processaNotificacaoFalha
	 * Descricao.:Processa o envio de notificacao no caso de configuracao do alarme
	 *            indicar que o tipo Falha deve ser notificado
	 */
	private void processaNotificacaoFalha()
	{
		// Verifica se no status corrente a notificacao deve ser enviada por e-mail
		if (alarme.isEnviaEMailFalha())
			notificacaoEMail.enviaNotificacao(alarme,ultimoEvento);
		
		// Verifica se no status corrente a notificacao deve ser enviada por SMS
		if (alarme.isEnviaSMSFalha())
			notificacaoSMS.enviaNotificacao(alarme,ultimoEvento);
	}
	
	/**
	 * Metodo....:processaNotificacaoOk
	 * Descricao.:Processa o envio de notificacao no caso de configuracao do alarme
	 *            indicar que o tipo Ok deve ser notificado
	 */
	private void processaNotificacaoOk()
	{
		// Para o status OK a notificacao deve sempre ser enviada, caso a notificacao
		// de falha ou alerta tenha sido previamente enviada.
		if (alarme.isEnviaEMailAlerta() || alarme.isEnviaEMailFalha())
			notificacaoEMail.enviaNotificacao(alarme,ultimoEvento);
		
		// Para o status OK a notificacao deve sempre ser enviada, caso a notificacao
		// de falha ou alerta tenha sido previamente enviada.
		if (alarme.isEnviaSMSAlerta() || alarme.isEnviaSMSFalha())
			notificacaoSMS.enviaNotificacao(alarme,ultimoEvento);
	}

	/**
	 * Metodo....:processaNotificacao
	 * Descricao.:Este metodo realiza os testes necessarios para a notificacao
	 *            do alarme nos diferentes tipos de comunicacao (E-Mail,SMS e Trap SNMP)
	 */
	public void processaNotificacao()
	{
		if (alarme.getStatus().equals(Alarme.ALARME_ALERTA))
			processaNotificacaoAlerta();
		
		if (alarme.getStatus().equals(Alarme.ALARME_FALHA))
			processaNotificacaoFalha();
		
		if (alarme.getStatus().equals(Alarme.ALARME_OK))
			processaNotificacaoOk();
	}
}
