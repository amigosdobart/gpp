package com.brt.gppAlarmes.aplicacao;

import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.entity.Evento;
import com.brt.gppAlarmes.dao.EventoDAO;
import com.brt.gppAlarmes.dao.AlarmeDAO;

import java.util.Date;
import java.util.Calendar;
//import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 * Esta classe e a responsavel pelo processamento exclusivo de um determinado
 * alarme. Esta classe faz a analise do evento e verifica para qual status o
 * alarme devera ser alterado.
 * 
 * @author Joao Carlos 
 * Data..: 21-Mar-2005
 *
 */
//TODO talvez a limpeza venha aqui ou numa nova classe
public class ProcessadorAnalise implements Runnable
{
	private Alarme 				alarme;
	private Logger 				logger;
//	private SimpleDateFormat	sdf;
	
	public ProcessadorAnalise(Alarme alarme)
	{
		if (alarme == null)
			throw new IllegalArgumentException("Alarme a ser processado e nulo");

		this.alarme = alarme;
//		this.sdf	= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		logger = Logger.getLogger("Alarmes");
	}

	/**
	 * Metodo....:getAlertaPorAgendamento
	 * Descricao.:Busca o status do alarme baseado nas configuracoes de agendamento ("scheduling") 
	 * @param ultimoEvento 	- Ultimo Evento
	 * @return String		- Descricao do status de alerta
	 */
	private String getAlertaPorAgendamento(Evento ultimoEvento)
	{
		// Busca o tempo de atraso da ultima execucao com o agendamento especificado
		// Caso o alarme ainda nao possua eventos registrados entao nao considera este
		// para o estudo do tempo de atraso
		Date dtAtual		= Calendar.getInstance().getTime();
		Date dtUltExecucao	= ultimoEvento.getDataExecucao();
		Date dtProxExecucao = alarme.getAgendamento().getProximoAgendamento(dtUltExecucao);
		long atraso = (dtAtual.getTime() - dtProxExecucao.getTime())/1000/60;

		// O alerta padrao na verificacao por agendamento e o status OK, se existir atraso
		// entao verifica a faixa do atraso para identificar a gravidade do alarme (alerta ou falha)
		// porem sempre que considerado o atraso negativo significa que em relacao a data atual
		// o proximo agendamento nao esta atrasado
		String alerta = Alarme.ALARME_OK;

		// Verifica se o atraso se classifica como alerta
		if (atraso > 0 && atraso > alarme.getAtrasoMaxAlerta() && atraso < alarme.getAtrasoMaxFalha())
			alerta = Alarme.ALARME_ALERTA;
		// Verifica se o atraso se classifica como falha
		if (atraso > 0 && atraso > alarme.getAtrasoMaxFalha())
			alerta = Alarme.ALARME_FALHA;

		// Define o motivo do Alarme
		alarme.setMotivoAlarme(Alarme.MOTIVO_ATRASO);

		logger.debug(alarme.getIdAlarme()+" - Atraso:"+atraso);
		return alerta;
	}
	
	/**
	 * Metodo....:getAlertaPorValor
	 * Descricao.:Busca o valor do status do alarme baseado no valor do contador do ultimo evento
	 * @param ultimoEvento 	- Ultimo evento a ser analisado
	 * @return String		- Descricao do status do alarme
	 */
	private String getAlertaPorValor(Evento ultimoEvento)
	{
		// O alerta padrao sera o status OK, dependendo do valor do atraso esse alerta pode ser modificado
		String alerta = Alarme.ALARME_OK;

		// Verifica se o contador classifica como alerta
		double valor = ultimoEvento.getValorContador();
		// Verifica se o contador classifica como alerta pelo valor minimo ou maximo
		if ( valor > alarme.getValorMinFalha()  && valor < alarme.getValorMinAlerta() )
		{
			alerta = Alarme.ALARME_ALERTA;
			alarme.setMotivoAlarme(Alarme.MOTIVO_VALOR_MIN);
		}
		else if ( valor > alarme.getValorMaxAlerta() && valor < alarme.getValorMaxFalha() )
			{
				alerta = Alarme.ALARME_ALERTA;
				alarme.setMotivoAlarme(Alarme.MOTIVO_VALOR_MAX);
			}

		// Verifica se o contador classifica como falha. Devido a hierarquia de erro
		// o teste para falha e feito posterior pois logicamente tem uma gravidade superior
		if ( valor < alarme.getValorMinFalha() )
		{
			alerta = Alarme.ALARME_FALHA;
			alarme.setMotivoAlarme(Alarme.MOTIVO_VALOR_MIN);
		}
		else if ( valor > alarme.getValorMaxFalha() )
			{
				alerta = Alarme.ALARME_FALHA;
				alarme.setMotivoAlarme(Alarme.MOTIVO_VALOR_MAX);
			}

		logger.debug(alarme.getIdAlarme()+" - Contador:"+valor);
		return alerta;
	}

	/**
	 * Metodo....:getAlertaPorStatusExecucao
	 * Descricao.:Retorna o status do alarme baseado no valor do status da ultima execucao
	 * @param ultimoEvento 	- Ultimo evento a ser analisado
	 * @return String		- Descricao do status do alarme
	 */
	private String getAlertaPorStatusExecucao(Evento ultimoEvento)
	{
		// O alerta padrao sera o status atual do alarme, dependendo do valor do atraso esse alerta pode ser modificado
		String alerta = Alarme.ALARME_OK;
		// No caso de status de execucao, o alarme possui somente dois estados
		// Ok ou Nao Ok que entao se classifica como OK ou FALHA
		if (ultimoEvento.getCodigoRetorno() != Alarme.CODIGO_RETORNO_OK)
			alerta = Alarme.ALARME_FALHA;
		
		// Define o motivo do Alarme
		alarme.setMotivoAlarme(Alarme.MOTIVO_RETORNO);

		logger.debug(alarme.getIdAlarme()+" - CodigoRetorno:"+ultimoEvento.getCodigoRetorno());
		return alerta;
	}

	/**
	 * Metodo....:getStatusAlarme
	 * Descricao.:Define o novo status que sera definido para o alarme
	 * @param ultimoEvento - Ultimo evento registrado para o alarme
	 * @return String - Novo status a ser definido dependendo da analise
	 */
	private String getStatusAlarme(Evento ultimoEvento)
	{
		// Se o ultimo evento nao existir para um dado alarme entao o novo status 
		// sera o mesmo status atual definido para o alarme. Portanto a analise sera
		// feita somente se existir um ultimo evento
		String statusAlarme = alarme.getStatus();
		if (ultimoEvento != null)
		{
			// Busca o valor do alarme ALERTA,FALHA ou OK para o alarme em processamento
			// dependendo de suas configuracoes com relacao a "scheduling" (agendamento de datas)
			statusAlarme = getAlertaPorAgendamento(ultimoEvento);
			
			// Se o status for diferente de OK entao ja atualiza este na tabela para indicar o alerta
			// desse alarme. Caso contrario entao as verificacoes por valores e por resposta serao
			// verificados.
			if (statusAlarme.equals(Alarme.ALARME_OK))
			{
				statusAlarme = getAlertaPorValor(ultimoEvento);
				// Caso o status por valor seja ok entao verifica por status da ultima execucao.
				// No caso do status ser diferente de ok entao este ja vai para a tabela
				if (statusAlarme.equals(Alarme.ALARME_OK))
					statusAlarme = getAlertaPorStatusExecucao(ultimoEvento);
			}
		}
		return statusAlarme;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			// Pega o ultimo evento cadastrado para o alarme sendo processado
			Evento ultimoEvento = EventoDAO.getInstance().getUltimaExecucao(alarme);
			if (ultimoEvento != null)
				alarme.setDataUltimaExecucao(ultimoEvento.getDataExecucao());

			// Busca o status que o alarme sera defindo executando as regras
			String novoStatus  = getStatusAlarme(ultimoEvento);
			logger.debug(alarme.getIdAlarme()+" - StatusAtual:"+alarme.getStatus()+" NovoStatus:"+novoStatus);

			// Se o status atual do alarme for igual ao novo status definido pelas regras
			// entao nao ha a necessidade de salvar o estado do objeto
			if (!alarme.getStatus().equals(novoStatus))
			{
				// Atualiza as informacoes no objeto alarme sendo processado
				alarme.setStatus(novoStatus);
				// Atualiza informacoes do alarme em tabela no banco de dados
				AlarmeDAO.getInstance().update(alarme);
				// Realiza o processamento para a notificacao da mudanca de status do alarme
				logger.info("Realizando notificacao do alarme "+alarme);
				new ProcessadorNotificacao(alarme,ultimoEvento).processaNotificacao();
			}
			else
				// Atualiza informacoes do alarme em tabela no banco de dados porem so ira atualizar
				// a data da ultima execucao do evento na tabela de alarme
				AlarmeDAO.getInstance().update(alarme);
			
			//Limpa o historico do alarme
			logger.info("Apagando historico do alarme" + alarme);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			//calendar.clear(Calendar.HOUR);
			calendar.clear(Calendar.MINUTE);
			calendar.clear(Calendar.SECOND);
			calendar.add(Calendar.DATE, -alarme.getNumDiasHistorico());
			AlarmeDAO.getInstance().limparHistorico(alarme, calendar.getTime());
		}
		catch(Exception e)
		{
			// O tratamento de erro de um alarme nao deve impedir o processamento
			// dos demais, por isso em caso de erro simplesmente realiza o log do
			// erro e termina o ciclo de processamento somente desta thread
			logger.error("Erro no processamento do alarme "+alarme+" Erro. "+e);
		}
	}
}
