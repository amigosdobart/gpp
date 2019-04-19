package com.brt.gppAlarmes.aplicacao;

import com.brt.gppAlarmes.conexoes.Configuracao;
import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.entity.Evento;

import com.brt.gppEnviaMail.conexoes.EnviaMail;

import org.apache.log4j.Logger;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Joao Carlos
 *
 */
public class NotificacaoPorEMail
{
	/**
	 * Metodo....:getTextoMensagem
	 * Descricao.:Este metodo realiza a composicao da mensagem a ser enviada por e-mail
	 *            contendo as informacoes do alarme
	 * @param alarme		- Alarme sendo processado
	 * @param ultimoEvento	- Ultimo evento do alarme para ser utilizado na notificacao
	 * @return String		- Mensagem a ser enviada contendo as informacoes do alarme
	 */
	private String getTextoMensagem(Alarme alarme, Evento ultimoEvento)
	{
		SimpleDateFormat 	sdf 	= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DecimalFormat 		df 		= new DecimalFormat("######0.00");
		StringBuffer 		buffer 	= new StringBuffer();
		buffer.append("Alarme :"+alarme+"\n");
		buffer.append("Status :"+alarme.getStatus()+"\n\n");
		buffer.append("Motivo :"+alarme.getMotivoAlarme()!= null ? alarme.getMotivoAlarme() : "");
		
		if (ultimoEvento != null)
		{
			buffer.append("Ultima  Execucao :"+sdf.format(ultimoEvento.getDataExecucao())	+"\n");
			buffer.append("Proxima Execucao :"+sdf.format(alarme.getAgendamento().getProximoAgendamento(ultimoEvento.getDataExecucao()))+"\n");
			buffer.append("Retorno Execucao :"+ultimoEvento.getCodigoRetorno()				+"\n");
			buffer.append("Valor Contador   :"+df.format(ultimoEvento.getValorContador())	+"\n");
		}
		else
			buffer.append("Nao ha eventos registrados para esse alarme");

		return buffer.toString();
	}

	/**
	 * Metodo....:enviaNotificacao
	 * Descricao.:Envia a notificacao por e-mail do novo status do alarme
	 * @param alarme		- Alarme sendo processado
	 * @param ultimoEvento	- Ultimo evento do alarme para ser utilizado na notificacao
	 */
	public void enviaNotificacao(Alarme alarme, Evento ultimoEvento)
	{
		Logger logger = Logger.getLogger("Alarmes");
		Configuracao conf = Configuracao.getInstance();
		String enderecoOrigem 	= conf.getPropriedade("alarmes.notif.email.enderecoOrigem");
		String servidorSMTP		= conf.getPropriedade("alarmes.notif.email.servidorSMTP");
		String destinoDefault[] = conf.getPropriedade("alarmes.notif.email.enderecosDestinoDefault").split(",");

		// Cria instancia para envio do e-mail definindo o endereco de origem e o servidor SMTP
		EnviaMail enviaMail = new EnviaMail(enderecoOrigem,servidorSMTP);
		enviaMail.setAssunto(alarme + " - " + alarme.getStatus());
		enviaMail.setTipoMensagem("text/plain");
		enviaMail.setMensagem(getTextoMensagem(alarme,ultimoEvento));

		// Define os enderecos de destino do e-mail sendo a lista de enderecos default
		// Caso nao exista enderecos default cadastrados, entao a excessao tratada
		// nao realiza nenhuma acao, pois os enderecos podem ser cadastrados na tabela
		// do alarme
		try
		{
			for (int i=0; i < destinoDefault.length; i++)
				enviaMail.addEnderecoDestino(destinoDefault[i]);
		}
		catch(IllegalArgumentException ex){};
		
		// Define alem dos enderecos default, a lista de enderecos para envio do e-mail
		// exclusivos do alarme sendo processado
		for (Iterator i=alarme.getListaEMail().iterator(); i.hasNext();)
		{
			String enderecoDestino = null;
			try
			{
				enderecoDestino = (String)i.next();
				enviaMail.addEnderecoDestino(enderecoDestino);
			}
			catch(IllegalArgumentException ex)
			{
				logger.warn("Endereco de destino de e-mail invalido para o alarme:"+ alarme.getIdAlarme() +". e-mail: "+enderecoDestino);
			}
			
			// Realiza o envio do e-mail somente se existir pelo menos um 
			// endereco de destino valido cadastrado
			if (enviaMail.getEnderecosDestino().length > 0)
				enviaMail.enviaMail();
		}
		
		logger.debug("Informacoes de status do alarme "+alarme+" foram enviados por e-mail.");
	}
}
