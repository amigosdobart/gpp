package com.brt.gppAlarmes.aplicacao;

//import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

import com.brt.gppAlarmes.conexoes.Configuracao;
import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.entity.Evento;

import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 *
 * @author Joao Carlos
 * Data..: 05-Abr-2005
 *
 */
public class NotificacaoPorSMS
{
	/**
	 * Metodo....:getTextoMensagem
	 * Descricao.:Este metodo realiza a composicao da mensagem a ser enviada por SMS
	 *            contendo as informacoes do alarme
	 * @param alarme		- Alarme sendo processado
	 * @param ultimoEvento	- Ultimo evento do alarme para ser utilizado na notificacao
	 * @return String		- Mensagem a ser enviada contendo as informacoes do alarme
	 */
	private String getTextoMensagem(Alarme alarme, Evento ultimoEvento)
	{
//		SimpleDateFormat 	sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DecimalFormat 		df 	= new DecimalFormat("######0.00");

		StringBuffer buffer = new StringBuffer(alarme.getIdAlarme());
		buffer.append(" Sit:"+alarme.getStatus());
		if (alarme.getMotivoAlarme()!= null)
			buffer.append(" Mot:"+alarme.getMotivoAlarme());

		if (ultimoEvento != null)
			buffer.append(" Cont:"+df.format(ultimoEvento.getValorContador())+" Ret:"+ultimoEvento.getCodigoRetorno());
		else
			buffer.append(" Nao ha eventos para esse alarme");

		return buffer.toString();
	}

	/**
	 * Metodo....:enviaNotificacao
	 * Descricao.:Envia a notificacao por SMS do novo status do alarme
	 * @param alarme		- Alarme sendo processado
	 * @param ultimoEvento	- Ultimo evento do alarme para ser utilizado na notificacao
	 */
	public void enviaNotificacao(Alarme alarme, Evento ultimoEvento)
	{
		Logger logger = Logger.getLogger("Alarmes");
		// Busca os dados da configuracao para conexao com o Middleware de SMS
		Configuracao conf = Configuracao.getInstance();
		String assinantesDefault[]	= conf.getPropriedade("alarmes.notif.sms.listaAssinantesDefault").split(",");

		GerentePoolBancoDados poolBanco = GerentePoolBancoDados.getInstancia(0);
		PREPConexao conexaoBanco = null;

		try
		{
			conexaoBanco = poolBanco.getConexaoPREP(0);
			EnvioSMSDAO envioSMSDAO = new EnvioSMSDAO(0, conexaoBanco);
			// Busca a lista de assinantes que deverao receber o aviso via SMS para este alarme
			// e adiciona todos os assinantes da lista default a esta colecao para que esta possa
			// ser sofrer a iteracao dos assinantes que irao receber o SMS
			Collection listaAssinantes = alarme.getListaAssinantes();
			for (int i=0; i < assinantesDefault.length; i++)
				listaAssinantes.add(assinantesDefault[i]);

			// Envia SMS para todos os assinantes cadastrados na lista default e na lista de assinantes
			// que receberao informacoes exclusivamente para este alarme
			for (Iterator i=listaAssinantes.iterator(); i.hasNext();)
			{
				DadosSMS sms = new DadosSMS();

				TipoSMS tipoSMS = new TipoSMS();
				tipoSMS.setIdtTipoSMS("GPP_ALARME");

				sms.setIdtMsisdn((String)i.next());
				sms.setDesMensagem(getTextoMensagem(alarme,ultimoEvento));
				sms.setNumPrioridade(-1);
				sms.setTipoSMS(tipoSMS);

				envioSMSDAO.inserirSMS(sms);
			}
		}
		catch(Exception e)
		{
			logger.error("Erro no envio de SMS de informacoes do status do alarme "+alarme);
		}
		finally
		{
			if(conexaoBanco != null)
				poolBanco.liberaConexaoPREP(conexaoBanco, 0);
		}
	}
}
