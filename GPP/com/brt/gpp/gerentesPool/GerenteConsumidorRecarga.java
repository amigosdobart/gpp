package com.brt.gpp.gerentesPool;

import com.brt.gpp.aplicacoes.recarregar.ConsumoRecarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.util.Vector;
import java.util.Iterator;

/**
*
* Esta classe e um singleton que gerencia uma lista de threads responsaveis
* pelo processo de recarga e envio de SMS da tabela de fila de recargas. Os dados
* da recarga sao produzidos pelo GerenteProdutorRecarga e armazenados em memoria.
* As threads de consumo realizam o processo e retiram este registro da memoria.
* Esta classe fornece os metodos necessarios para iniciar este pool de threads e
* metodos para o gerenciamento destas, como finalizacao e notificacao
*    
* <P> Versao:        	1.0
*
* @Autor:            	Joao Carlos
* Data:                 07/12/2004
*
* Modificado por:
* Data:
* Razao:
*
*/

public class GerenteConsumidorRecarga 
{
	private static 	GerenteConsumidorRecarga instancia;
	private 		GerentePoolLog			 gerPoolLog;

	private Vector threadsConsumoRecarga;
	
	/**
	 * Metodo....:GerenteConsumidorRecarga
	 * Descricao.:Instanciacao da classe
	 * @param idProcesso - Id do processo que esta iniciando a classe
	 */
	public GerenteConsumidorRecarga(long idProcesso)
	{
		gerPoolLog = GerentePoolLog.getInstancia(this.getClass());

		// Inicializacao do pool de threads
		threadsConsumoRecarga = new Vector();
		inicializaThreads(idProcesso);
	}
	
	/**
	 * Metodo....:getInstancia
	 * Descricao.:Este metodo retorna a instancia do singleton da classe
	 * @param idProcesso - Id do processo que esta buscando a instancia
	 * @return GerenteConsumidorRecarga - Instancia do gerenciador das threads de consumo da recarga
	 */
	public static GerenteConsumidorRecarga getInstancia(long idProcesso)
	{
		if (instancia == null)
			instancia = new GerenteConsumidorRecarga(idProcesso);
		
		return instancia;
	}
	
	/**
	 * Metodo....:inicializaThreads
	 * Descricao.:Este metodo e responsavel por inicializar as threads de consumo de recargas
	 *            e armazena-las no pool de dados
	 */
	private void inicializaThreads(long idProcesso)
	{
		try
		{
			// Busca o numero de threads que devera ser iniciado para o consumo de recargas
			MapConfiguracaoGPP confGpp = MapConfiguracaoGPP.getInstancia();
			int numThreads = Integer.parseInt(confGpp.getMapValorConfiguracaoGPP("NUM_THREADS_CONSUMO_RECARGA"));
			
			// Inicializa as threads incluindo-as no pool
			for (int i=0; i < numThreads; i++)
			{
				ConsumoRecarga consumoRecarga = new ConsumoRecarga();
				consumoRecarga.start();
				threadsConsumoRecarga.add(consumoRecarga);
			}
			gerPoolLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_GERENTE_CONSUMIDOR_RECARGA,"inicializaThreads","Inicializado Pool de Threads de Consumo de Recarga. Numero de Threads:"+numThreads);
			// Apos criar as threads faz-se a notificacao destas para inicializacao do processamento
			notificaConsumoRecarga();
		}
		catch(Exception e)
		{
			gerPoolLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_CONSUMIDOR_RECARGA,"inicializaThreads","Erro ao iniciar threads de consumo de recarga. Erro:"+e);
		}
	}
	
	/**
	 * Metodo....:notificaConsumoRecarga
	 * Descricao.:Este metodo realiza a notificacao das threads de recarga iniciando
	 *            o procedimento de processamento da realizacao da recarga desde que
	 *            esta ja nao esteja sendo executada
	 *
	 */
	public synchronized void notificaConsumoRecarga()
	{
		// Realiza a iteracao nos elementos do vetor de threads e para
		// cada uma realiza a chamada para iniciar o processamento desde
		// que esta thread ja nao esteja executando
		for (Iterator i=threadsConsumoRecarga.iterator(); i.hasNext();)
		{
			ConsumoRecarga consumo = (ConsumoRecarga)i.next();
			consumo.notificaConsumo();
		}
	}
	
	/**
	 * Metodo....:destroiConsumoRecarga
	 * Descricao.:Este metodo e responsavel pela paralizacao do processo de consumo de recarga
	 *            destruindo todas as threads sendo executadas e deixando a instancia do singleton
	 *            nulo
	 * 
	 */
	public synchronized void destroiConsumoRecarga()
	{
		// Realiza a iteracao nos elementos do vetor de threads e finaliza
		// cada uma destas removendo todas do vetor e deixando a instancia
		// do singleton como nulo
		while (threadsConsumoRecarga.size() > 0)
		{
			ConsumoRecarga consumo = (ConsumoRecarga)threadsConsumoRecarga.remove(0);
			consumo.destroy();
			consumo = null;
		}
		instancia = null;
	}
}
