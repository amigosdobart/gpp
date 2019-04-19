package com.brt.gpp.aplicacoes.totalizadorTecnomen;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

/**
 * Esta classe realiza o gerenciamento dos objetos de API em memoria para a totalizacao de chamadas
 * de metodos por um determinado periodo. Esta classe tambem eh responsavel pela persistencia dessas
 * informacoes em banco de dados.
 * 
 * @author Joao Carlos
 * Data..: 13-Set-2005
 *
 */
public class TotalizadorAPITecnomen implements Runnable
{
	
	/**
	 *	Instancia do singleton.
	 */
	private static TotalizadorAPITecnomen instance;

	/**
	 *	Sincronizador de criacao da instancia da classe.
	 */
	private static Object mutex = new Object();
	
	/**
	 *	Totalizador do Payment Engine.
	 */
	private Map totalizadorPaymentEngine;
	
	/**
	 *	Totalizador do Provision Server.
	 */
	private Map totalizadorProvisionServer;
	
	/**
	 *	Totalizador do Prepaid Server.
	 */
	private Map totalizadorPPServer;
	
	/**
	 *	Periodo entre as leituras.
	 */
	private int periodo;
	
	/**
	 *	Gerente de log.
	 */
	private GerentePoolLog logger;
	
	/**
	 *	Identificador do processo.
	 */
	private long idProcesso;
	
	/**
	 *	Formatador de data/hora.
	 */
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
	
	/**
	 * Metodo....:TotalizadorAPITecnomen
	 * Descricao.:Construtor da classe
	 * @param idProcesso - Id do processo que estah construindo o objeto
	 */
	private TotalizadorAPITecnomen(long idProcesso)
	{
		this.logger 	= GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso	= idProcesso;
		// Inicializa mapeamento das APIs que serao totalizadas
		this.totalizadorPaymentEngine	= Collections.synchronizedMap(new HashMap());
		this.totalizadorProvisionServer	= Collections.synchronizedMap(new HashMap());
		this.totalizadorPPServer		= Collections.synchronizedMap(new HashMap());
		
		try
		{
			// Busca o valor do periodo cadastrado na tabela de configuracao do GPP configurado em minutos
			MapConfiguracaoGPP conf = MapConfiguracaoGPP.getInstancia();
			this.periodo = Integer.parseInt(conf.getMapValorConfiguracaoGPP("PERIODO_TOTALIZACAO_API_TECNOMEN"));
		}
		catch(Exception e)
		{
			this.logger.log(idProcesso,Definicoes.ERRO,"TotalizadorAPITecnomen","inicializaValores","Erro ao Inicializar o Hash de Totalizacao de chamadas das API's Tecnomen");
		}
	}
	
	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia da classe (Singleton)
	 * @return TotalizadorAPITecnomen singleton
	 */
	public static TotalizadorAPITecnomen getInstance()
	{
		if(TotalizadorAPITecnomen.instance != null)
			return TotalizadorAPITecnomen.instance;
		
		synchronized(TotalizadorAPITecnomen.mutex)
		{
			if (instance == null)
			{
				instance = new TotalizadorAPITecnomen(0);
				//instance.start();
				new Thread(new ThreadGroup("TotalizadorTecnomen"), instance, "TotalizadorTecnomen").start();
			}
		}
		
		return instance;
	}
	
	/**
	 * Metodo....:incrementarPaymentEngine
	 * Descricao.:Incrementa o totalizador de chamadas de api tecnomen. Este metodo atualiza
	 *            exclusivamente metodos que sao de responsabilidade da payment engine
	 * @param metodo - Nome do metodo da api da payment engine
	 */
	public synchronized void incrementarPaymentEngine(String metodo)
	{
		if(this.totalizadorPaymentEngine.get(metodo) == null)
			this.totalizadorPaymentEngine.put(metodo, this.iniciaTotalizadorAPI());

		// Independente se for a primeira chamada e o objeto estiver nulo,
		// apos preenchido o mesmo deve ser totalizado
		this.incrementaTotalizador(this.totalizadorPaymentEngine, metodo);
	}

	/**
	 *	Incrementa o totalizador de chamadas de API do Provision Server. Este metodo atualiza exclusivamente metodos 
	 *	que sao de responstabilidade do Provision Server.
	 *
	 *	@param		metodo					Nome do metodo da API do Provision Server.
	 */
	public synchronized void incrementarProvisionServer(String metodo)
	{
		if(this.totalizadorProvisionServer.get(metodo) == null)
			this.totalizadorProvisionServer.put(metodo, this.iniciaTotalizadorAPI());

		// Independente se for a primeira chamada e o objeto estiver nulo,
		// apos preenchido o mesmo deve ser totalizado
		this.incrementaTotalizador(this.totalizadorProvisionServer, metodo);
	}

	/**
	 *	Incrementa o totalizador de chamadas de API do Prepaid Server. Este metodo atualiza exclusivamente metodos 
	 *	que sao de responstabilidade do Prepaid Server.
	 *
	 *	@param		metodo					Nome do metodo da API do Prepaid Server.
	 */
	public synchronized void incrementarPPServer(String metodo)
	{
		if(this.totalizadorPPServer.get(metodo) == null)
			this.totalizadorPPServer.put(metodo, this.iniciaTotalizadorAPI());

		// Independente se for a primeira chamada e o objeto estiver nulo,
		// apos preenchido o mesmo deve ser totalizado
		this.incrementaTotalizador(this.totalizadorPPServer, metodo);
	}

	/**
	 * Metodo....:incrementaTotalizador
	 * Descricao.:Realiza o incremento da API buscando o intervalo correto
	 * @param totalizador	- Map do totalizador a ser utilizado
	 * @param metodo		- Nome do metodo a ser atualizado
	 */
	private void incrementaTotalizador(Map totalizador, String metodo)
	{
		// A data atual serah a orientacao para sabermos em qual periodo do
		// mapeamento deve ser totalizado. Portanto a data inicio da pesquisa
		// eh a data e hora atual sendo que os minutos da hora sao "zerados"
		// para que seja feita a pesquisa para identificar o intervalo
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		// Segura uma variavel para definir a hora atual da totalizacao
		Calendar horaAtual = Calendar.getInstance();
		// Entao agora realiza um incremento nessa data definida ateh que
		// seja descoberto o intervalo. O intervalo serah encontrado quando
		// a subtracao da data atual pela data definida acima for menor que
		// o periodo configurado.
		
		while ((horaAtual.getTimeInMillis()-cal.getTimeInMillis())/1000/60 > this.periodo)
			cal.add(Calendar.MINUTE, this.periodo);
		
		// Apos o loop o intervalo que deve ser totalizado foi descoberto
		// portanto atualiza-o. Portanto primeiro busca o valor contido no
		// mapeamento e caso estiver nulo eh pq a chave nao foi ainda criada
		// portanto realiza a criacao da chave tambem
		Map valores = (HashMap)totalizador.get(metodo);
		if (valores == null)
			valores = new HashMap();
		long total = 0;
		Long valor;
		if ((valor = (Long) valores.get(this.formatter.format(cal.getTime()))) != null)
			total = valor.longValue();

		valores.put(this.formatter.format(cal.getTime()),new Long(total+1));
	}

	/**
	 * Metodo....:iniciaTotalizadorAPI
	 * Descricao.:Este metodo inicia os valores de uma api para os periodos do dia
	 *            Atraves da configuracao do periodo eh definido a partir das 0hs do
	 *            dia os intervalos de tempo que serao a chave para o mapeamento da
	 *            totalizacao da api
	 * @return Map - Valores a serem utilizados pelo map das api's 
	 */
	private Map iniciaTotalizadorAPI()
	{
		// Cria o map que irah armazenar os valores dos periodos com seus respectivos totalizadores
		// OBS: Nesse momento todo o conjunto de periodos contidos em um dia serao criados. Posteriormente
		//      a medida que novos periodos vao sendo necessarios entao estes vao sendo criados
		Map valores = new HashMap();
		// A variavel de data irah ser incrementada pelo periodo definido na configuracao do sistema, sendo
		// entao que cada hora respectiva ao perido sera armazenada como o valor chava do hash para a API
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		Calendar horaFinal = Calendar.getInstance();
		// Entao realiza o incremento do periodo iniciando na hora atual porem com
		// os minutos zerados. E entao o intervalo eh incrementado pelo periodo
		// Para cada iteracao eh criado uma chave no hash que ira conter o totalizado
		// daquele periodo para a API
		while (cal.getTime().before(horaFinal.getTime()))
		{
 			valores.put(this.formatter.format(cal.getTime()),new Long(0));
			cal.add(Calendar.MINUTE, this.periodo);
		}
		
		return valores;
	}

	/**
	 * Metodo....:persisteValores
	 * Descricao.:Este metodo realiza a persistencia dos valores do hash que ainda
	 *            nao foram armazenados em banco de dados
	 * @param nomeModulo	- Nome do modulo da Tecnomen
	 * @param nomeMetodo	- Nome do metodo da api que contem os valores
	 * @param valores		- Map contendos os intervalos e totalizacao desse metodo
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 */
	private void persisteValores(String nomeModulo, String nomeMetodo, Map valores, PREPConexao conexaoPrep)
	{
		Date horaAtual = Calendar.getInstance().getTime();
		String sqlIns = "insert into tbl_rel_medicao_servicos " +
						"(vlr_coletado,nom_servico,nom_canal,dat_coleta) values (?,?,?,?)";

		String sqlUpd = "update tbl_rel_medicao_servicos " +
		                   "set vlr_coletado = vlr_coletado + ? " +
		                 "where nom_servico = ? " +
		                   "and nom_canal   = ? " +
		                   "and dat_coleta  = ?";
		// Faz uma iteracao em todos as chaves do mapeamento para identificar quais deverao 
		// ser persistidos. A persistencia serah efetuada verificando a data (base da chave)
		// Somente os valores antes data data atual poderao ser persistidos lembrando que
		// o ultimo periodo ainda estah em totalizacao e portanto este tambem nao deve ser
		// armazenado no banco de dados
		Object[] chaves = valores.keySet().toArray();
		//for (Iterator i=valores.keySet().iterator(); i.hasNext();)
		for(int i = 0; i < chaves.length; i++)
		{
			try
			{
				String chave = (String) chaves[i];
				//String chave = i.next();
				// Define a data do intervalo representando a chave
				Calendar dataIntervalo = Calendar.getInstance();
				dataIntervalo.setTime(this.formatter.parse(chave));
				// Somente intervalos antes do ultimo periodo existente serao processados
				if((horaAtual.getTime()-dataIntervalo.getTimeInMillis())/1000/60 > this.periodo)
				{
					//Long valor = (Long)valores.get(formatter.format(dataIntervalo.getTime()));
					Long valor = (Long)valores.get(chave);
					//if(valor.longValue() > 0)
					//{
						// Executa o update na tabela. Se o a linha nao for encontrada entao
						// realiza o insert
						Object param[] = {valor
								         ,nomeMetodo
								         ,nomeModulo
								         ,new Timestamp(dataIntervalo.getTimeInMillis())
								         };

						int numLinhas = conexaoPrep.executaPreparedUpdate(sqlUpd,param, this.idProcesso);
						if(numLinhas == 0)
							// Entao realiza o insert
							conexaoPrep.executaPreparedUpdate(sqlIns,param, this.idProcesso);
						
						// Apos o armazenamento em banco de dados o objeto eh alterado para conter
						// o valor 0 (zero) evitando entao que este seja armazenado novamente
						//valores.put(formatter.format(dataIntervalo.getTime()),new Long(0));
						valores.remove(chave);
					//}
				}
			}
			catch(Exception e)
			{
				this.logger.log(this.idProcesso
						       ,Definicoes.ERRO
						       ,"TotalizadorAPITecnomen"
						       ,"persisteValores"
						       ,"Erro ao Salvar Totalizacao de chamadas do Modulo:"+nomeModulo+" Metodo:"+nomeMetodo+". Erro:"+e);
			}
		}
	}

	/**
	 * Metodo....:persisteModulos
	 * Descricao.:Realiza a persistencia dos objetos mapeados em memoria
	 */
	public void persisteModulos()
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
			// Realiza a iteracao entre todos as apis definidas para o modelo da PaymentEngine
			// Cada metodo existente no mapeamento eh enviado para o metodo que irah realizar
			// a persitencia de cada valor totalizado
			for (Iterator i = this.totalizadorPaymentEngine.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry entrada = (Map.Entry)i.next();
				// Apos ter os valores do totalizador entao para cada 
				// um realiza a persistencia especifica do modulo
				persisteValores("PaymentInterface",(String)entrada.getKey(),(Map)entrada.getValue(),conexaoPrep);
			}
			// Realiza a iteracao entre todos as apis definidas para o modelo do Provision Server.
			for(Iterator i = this.totalizadorProvisionServer.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry entrada = (Map.Entry)i.next();
				// Apos ter os valores do totalizador entao para cada 
				// um realiza a persistencia especifica do modulo
				persisteValores("ProvisionInterface",(String)entrada.getKey(),(Map)entrada.getValue(),conexaoPrep);
			}
			// Realiza a iteracao entre todos as API's definidas para o modelo do Prepaid Server.
			for(Iterator i = this.totalizadorPPServer.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry entrada = (Map.Entry)i.next();
				// Apos ter os valores do totalizador entao para cada 
				// um realiza a persistencia especifica do modulo
				persisteValores("PrepaidInterface",(String)entrada.getKey(),(Map)entrada.getValue(),conexaoPrep);
			}
		}
		catch(Exception e)
		{
			this.logger.log(this.idProcesso,Definicoes.ERRO,"TotalizadorAPITecnomen","run","Erro ao Salvar Totalizacao de chamadas das API's Tecnomen. Erro:"+e);			
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(conexaoPrep, this.idProcesso);
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		while(true)
		{
			// Realiza a persistencia dos modulos totalizados em memoria
			this.logger.log(this.idProcesso,Definicoes.INFO,"TotalizadorAPITecnomen","run","Iniciando persistencia das totalizacoes de chamadas de API Tecnomen");
			persisteModulos();
			// Apos um termino de processamento da persistencia o processo
			// irah esperar durante um tempo para persistir novamente
			try
			{
				Thread.sleep(this.periodo*60*1000);
			}
			catch(Exception e){};
		}
	}
}
