package com.brt.gpp.aplicacoes.enviarSMS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.comum.Cronometro;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.smpp.RequisicaoSMSC;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * A classe <code>EnvioSMSProdutor</code> e responsavel por carregar os dados da
 * tabela <code>TBL_GER_ENVIO_SMS</code> numa fila de SMS ordenada por prioridade
 * e por ordem de chegada.<br>
 * A fila e consumida por varias threads consumidoras que irao enviar os dados do
 * SMS a plataforma SMSC.<br>
 * Encontra-se também na classe <code>EnvioSMSProdutor</code> metodos para <b>iniciar</b>,
 * <b>finalizar</b> e <b>reiniciar</b> o processo.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 27/12/2007
 */
public class EnvioSMSProdutor extends Thread
{
	private static EnvioSMSProdutor	produtor;
	// Lock utilizado para sincronizar as Threads Consumidoras
	private final Object LOCK = new Object();
	// Mapa utilizado para organizar as fila de SMS de acordo com a prioridade.
	private Map						mapa;
	private PREPConexao				conexao;

	private long					idProcesso;
	// Flag que indica se o processo deve processar a fila de SMS
	private boolean					deveConsumirSMS;
	// Flag que indica se o processo ja foi iniciado
	private boolean					processoIniciado;
	// Numero de Threads Consumidoras para enviar SMS
	private int						numThreadsEnvioSMS;
	// Intervalo de tempo que o produtor espera para atualizar as filas de SMS
	private int						tempoEspera;
	// Gerente de log
	private static GerentePoolLog	log;
	// Nome da classe utilizado para gerar log
	public static final String CLASSE = "EnvioSMSProdutor";
	// Arquigo de configuragoes do GPP
	private ArquivoConfiguracaoGPP	arqConf;
	private int tamanhoMaximoFilaSMS;

	// Parametros do arquivo de Configuracoes do GPP
	public static final String GPP_DEVE_CONSUMIR_SMS		= "GPP_DEVE_CONSUMIR_SMS";
	public static final String GPP_NUMERO_THREADS_ENVIO_SMS = "GPP_NUMERO_THREADS_ENVIO_SMS";
	public static final String TEMPO_ESPERA_SMS 			= "TEMPO_ESPERA_SMS";
	// Parametro contido na tabela TBL_GER_CONFIGURACAO_GPP
	public static final String FILA_SMS_TAMANHO_MAXIMO 		= "FILA_SMS_TAMANHO_MAXIMO";

	private EnvioSMSProdutor(long idProcesso)
	{
		this.idProcesso = idProcesso;
		mapa = Collections.synchronizedMap(new TreeMap());
		arqConf = ArquivoConfiguracaoGPP.getInstance();
		deveConsumirSMS = arqConf.getBoolean(GPP_DEVE_CONSUMIR_SMS);
		numThreadsEnvioSMS = arqConf.getInt(GPP_NUMERO_THREADS_ENVIO_SMS);
		tempoEspera = arqConf.getInt(TEMPO_ESPERA_SMS);
		log = GerentePoolLog.getInstancia(this.getClass());
	}
	/**
	 * Metodo <i>singleton</i> responsavel por instanciar a classe <code>EnvioSMSProdutor</code>.
	 *
	 * @param idProcesso numero utilizado para registrar o processo de Consumode recarga.
	 * @return Instancia da classe <code>EnvioSMSProdutor</code>.
	 * @throws GPPInternalErrorException
	 */
	public static synchronized EnvioSMSProdutor getInstancia(long idProcesso)
	{
		if(produtor == null)
		{
			produtor = new EnvioSMSProdutor(idProcesso);
		}

		return produtor;
	}
	/**
	 * Inicia processo de consumo da fila de SMS
	 */
	public void iniciarConsumoSMS()
	{
		if(processoIniciado)
		{
			log.log(produtor.getIdProcesso(), Definicoes.ERRO, CLASSE,
					"iniciarConsumoSMS", "O processo de consumo de SMS ja foi iniciado.");
			return;
		}

		if(!deveConsumirSMS)
		{
			log.log(produtor.getIdProcesso(), Definicoes.WARN, CLASSE,
					"iniciarConsumoSMS", "Atributo deveConsumirSMS deve ser true para iniciar o processo.");
			return;
		}

		try
		{	GerentePoolBancoDados pool = GerentePoolBancoDados.getInstancia(idProcesso);
			conexao = pool.getConexaoPREP(idProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			log.log(produtor.getIdProcesso(), Definicoes.INFO, CLASSE, "iniciarConsumoSMS",
					"Erro ao abrir conexao com o banco de dados." +
					" O processo nao sera iniciado. Erro: "+e.getMessage());
				return;
		}

		try
		{
			MapConfiguracaoGPP conf = MapConfiguracaoGPP.getInstance();
			tamanhoMaximoFilaSMS = Integer.parseInt(conf.getMapValorConfiguracaoGPP("FILA_SMS_TAMANHO_MAXIMO"));
		}
		catch (Exception e)
		{
			log.log(produtor.getIdProcesso(), Definicoes.INFO, CLASSE, "iniciarConsumoSMS",
					"Nao foi possivel recuperar a configuracao FILA_SMS_TAMANHO_MAXIMO da tabela de" +
					"Configuracoes do GPP. Assumindo Tamanho maximo como: 50000. Erro: "+e.getMessage());

			tamanhoMaximoFilaSMS = 50000;
		}

		produtor.start();

		for(int i = 0; i < numThreadsEnvioSMS; i++)
			new EnvioSMSConsumidor(this).start();

		log.log(produtor.getIdProcesso(), Definicoes.INFO, CLASSE, "iniciarConsumoSMS",
				"Consumo de SMS iniciado com sucesso!");

		processoIniciado = true;
	}
	/**
	 * Recarrega a fila de SMS num intervalo de tempo definido no arquivo de Configuracoes.<br>
	 */
	public void run()
	{
		Cronometro cron = new Cronometro();
		while(deveConsumirSMS)
		{
			cron.setInicio();
			int registros = carregarFilaSMS();
			log.log(idProcesso,Definicoes.INFO, CLASSE, "run", "Fila de mensagens pendentes carregada. Num de SMS: "+
					registros + ". Tempo decorrido: "+cron.getTempoDecorrido());
			try
			{
				Thread.sleep(tempoEspera*1000);
			}
			catch(InterruptedException e)
			{
				log.log(idProcesso, Definicoes.ERRO, CLASSE, "run", "Erro ao colocar thread produtora em sleep: "+e.getMessage());
			}
		}
	}
	/**
	 * Metodo utilizado pelas threads consumidoras para processar a fila de SMS.
	 * Caso a fila esteja vazia o metodo <code>getDadosSMS()</code> retorna <code>null</code> e coloca as
	 * threas consumidoras em <code>wait</code>.
	 *
	 * @return Entidade <code>DadosSMS</code> preenchido.<br>
	 *                  <code>null</code> caso as filas estejam vazias ou quando o processo for finalizado.
	 */
	public DadosSMS getDadosSMS()
	{
		if(!deveConsumirSMS)
			return null;

		DadosSMS sms = null;
		Set prioridades = mapa.keySet();

		if(prioridades != null)
		{
			for(Iterator it = prioridades.iterator(); it.hasNext();)
			{
				List filaSMS = (List)mapa.get(it.next());

				if(filaSMS.size() != 0)
				{
					sms = (DadosSMS)filaSMS.get(0);
					filaSMS.remove(0);
					break;
				}
			}
		}
		// Caso as fila de SMS esteja vazia, colocar a thread consumidora em wait
		if(sms == null)
		{
			synchronized(LOCK)
			{
				try
				{
					LOCK.wait();
				}
				catch (InterruptedException e)
				{
					log.log(idProcesso, Definicoes.ERRO, CLASSE, "getDadosSMS", "Erro ao colocar thread consumidora em wait: "+e.getMessage());
				}
			}
		}

		return sms;
	}
	/**
	 * Carrega a fila de SMS no mapa de acordo com a prioridade.
	 */
	private int carregarFilaSMS()
	{
		int registrosCarregados = 0;
		EnvioSMSDAO dao = new EnvioSMSDAO(idProcesso, conexao);
		ResultSet registros = null;

		try
		{
			registros = dao.carregarFilaSMS(tamanhoMaximoFilaSMS);
		}
		catch(Exception e)
		{
			log.log(idProcesso, Definicoes.ERRO, CLASSE , "carregarFilaSMS", "Erro ao carregar fila. Erro: "+log.traceError(e));
		}

		if(registros  == null)
			return 0;

		try
		{
			while(registros.next())
			{
				DadosSMS sms = new DadosSMS();

				sms.setIdRegistro(registros.getLong("ID_REGISTRO"));
				sms.setIdtMsisdn(registros.getString("IDT_MSISDN"));
				sms.setIdtMsisdnOrigem(registros.getString("IDT_MSISDN_ORIGEM"));
				sms.setDesMensagem(registros.getString("DES_MENSAGEM"));
				sms.setNumPrioridade(registros.getInt("NUM_PRIORIDADE"));
				sms.setDatEnvioSMS(registros.getTimestamp("DAT_ENVIO_SMS"));
				sms.setDatProcessamento(registros.getTimestamp("DAT_PROCESSAMENTO"));
				sms.setIdtStatusProcessamento(registros.getInt("IDT_STATUS_PROCESSAMENTO"));
				sms.getTipoSMS().setIdtTipoSMS(registros.getString("TIP_SMS"));
				sms.getTipoSMS().setIndNotificarEntrega(registros.getInt("IND_NOTIFICAR_ENTREGA"));
				sms.getTipoSMS().setIdConexao(registros.getInt("ID_CONEXAO"));

				if(sms.getIdtMsisdnOrigem() == null)
					sms.setIdtMsisdnOrigem(arqConf.getString(RequisicaoSMSC.SMSC_ORIGINADOR));

				adicionarSMS(sms);
				registrosCarregados++;
			}
		}
		catch(Exception e)
		{
			log.log(idProcesso, Definicoes.ERRO,
					CLASSE, "carregarFilaSMS", "Erro ao percorrer o resultset de mensagens pendentes. Erro:"+log.traceError(e));
		}
		finally
		{
			try
			{
				registros.close();
			}
			catch (SQLException e)
			{
				log.log(idProcesso, Definicoes.FATAL,
						CLASSE, "carregarFilaSMS", "Erro ao fechar resultset. Erro: "+log.traceError(e));
			}
		}

		if (registrosCarregados > 0)
		{
			synchronized(LOCK)
			{
				LOCK.notifyAll();
			}
		}

		return registrosCarregados;
	}
	/**
	 * Adcioniona um SMS na fila em memoria organizada por prioridade.
	 *
	 * @param sms Entidade SMS contendo os dados do SMS a ser enviado.
	 */
	public void adicionarSMS(DadosSMS sms)
	{
		Integer prioridade = new Integer(sms.getNumPrioridade());
		try
		{
			// Insercao do SMS na fila de acordo com a prioridade
			if(mapa.containsKey(prioridade))
			{
				List filaSMS = (List)mapa.get(prioridade);
				if(!filaSMS.contains(sms))
					filaSMS.add(sms);
			}
			else
			{
				List novaFila = Collections.synchronizedList(new LinkedList());
				novaFila.add(sms);
				mapa.put(prioridade, novaFila);
			}
		}
		catch(Throwable e)
		{
			log.log(idProcesso, Definicoes.FATAL, CLASSE , "carregarFilaSMS", "Erro ao colocar SMS na fila. "+e.getMessage());
		}
	}
	/**
	 * Finaliza o processo de consumo da fila de SMS
	 */
	public void pararConsumoSMS()
	{
		if(!processoIniciado)
			return;

		synchronized(LOCK)
		{
			deveConsumirSMS = false;
			LOCK.notifyAll();

			GerentePoolBancoDados pool = GerentePoolBancoDados.getInstancia(idProcesso);
			pool.liberaConexaoPREP(conexao, idProcesso);

			processoIniciado = false;
			produtor = null;

			log.log(idProcesso, Definicoes.INFO, CLASSE, "pararConsumoSMS", "Consumo de SMS finalizado com sucesso!");
		}
	}
	/**
	 * Flag utilizada para parar ou continuar o processo
	 *
	 * @return <code>true</code> para continuar o processo, <code>flase</code> caso contrario.
	 */
	public boolean isDeveConsumirSMS()
	{
		return deveConsumirSMS;
	}

	public void setDeveConsumirSMS(boolean deveConsumirSMS)
	{
		this.deveConsumirSMS = deveConsumirSMS;
	}
	/**
	 * Retorna o numero de threads consumidoras
	 *
	 * @return Numero de threads consumidoras
	 */
	public int getNumThreadsEnvioSMS()
	{
		return numThreadsEnvioSMS;
	}
	/**
	 * Retorna a conexao com o banco de dados
	 *
	 * @return Conexao com o banco de dados
	 */
	public PREPConexao getConexao()
	{
		return conexao;
	}
	/**
	 * Retorna o idProcesso utilizado pelo processo de consumo de SMS
	 *
	 * @return idProcesso
	 */
	public long getIdProcesso()
	{
		return idProcesso;
	}
}
