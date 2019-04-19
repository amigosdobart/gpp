package com.brt.gpp.gerentesPool;

import com.brt.gpp.aplicacoes.importacaoCDR.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.Aplicacoes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;

/**
  * Esta classe e a responsavel pelo gerenciamento de threads para processamento
  * de arquivos de CDR. Nesta dois tipos de threads sao iniciadas: Threads para consumo
  * de CDRs dados/voz e threads para consumo de CDRs eventos/recargas.
  * O numero de threads a serem iniciadas estao definidos na tabela de configuracao
  * do sistema podendo dinamicamente serem alteradas adicionando ou removendo mais
  * threads ao pool para o processamento.
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos Lemgruber Junior
  * Data: 				09/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class GerenteProcessamentoCDR extends Aplicacoes
{
	private static 	GerenteProcessamentoCDR instance;
	private 		MapConfiguracaoGPP			confGPP;
	
	private Collection	poolProcCDRDadosVoz;
	private Collection	poolProcCDRAprRec;
	
	/**
	 * Metodo....:GerenteArquivosCDR
	 * Descricao.:Construtor da classe. Inicializa os vetores
	 *            que irao armazenar as referencias para os
	 *            arquivos de CDR
	 *
	 */
	private GerenteProcessamentoCDR(long idProcesso) throws GPPInternalErrorException
	{
		super(idProcesso, Definicoes.CL_GERENCIADOR_ARQUIVOS_CDR);
		
		// Inicializa referencia com a classe de configuracao do GPP e
		// inicia os vetores a serem utilizados para controle das Threads
		confGPP = MapConfiguracaoGPP.getInstancia();
		
		poolProcCDRDadosVoz = new LinkedList();
		poolProcCDRAprRec	= new LinkedList();
		
		int numThreadsDadosVoz 	= Integer.parseInt(confGPP.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_NUM_THREADS_CDR_DADOSVOZ));
		int numThreadsAprRec 	= Integer.parseInt(confGPP.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_NUM_THREADS_CDR_APRREC));
		
		inicializaPoolDadosVoz(numThreadsDadosVoz);
		inicializaPoolAprRec  (numThreadsAprRec);
	}

	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia dessa classe (Singleton)
	 * @param idProcesso	- Id do processo que buscou a instancia
	 * @return GerenciadorProcessamentoCDR - Instancia da classe
	 * @throws GPPInternalErrorException
	 */	
	public static GerenteProcessamentoCDR getInstance(long idProcesso) throws GPPInternalErrorException
	{
		// Se a instancia do sistema GPP permitir a importacao de CDRs, entao o cache
		// eh construido e inicializado, senao nada eh feito e a referencia para o objeto
		// retorna nulo
		ArquivoConfiguracaoGPP conf = ArquivoConfiguracaoGPP.getInstance();
		if (conf.deveImportarCDR())
			if (instance == null)
				instance = new GerenteProcessamentoCDR(idProcesso);
			
		return instance;
	}

	/**
	 * Metodo....:inicializaPoolDadosVoz
	 * Descricao.:Este metodo inicializa o pool de objetos que irao
	 *            realizar o processamento dos arquivos de CDR de
	 *            Dados/Voz
	 * 
	 * @param numThreads	- Numero de Threads iniciais
	 * @throws GPPInternalErrorException
	 */
	public void inicializaPoolDadosVoz(int numThreads) throws GPPInternalErrorException
	{
		// Cria uma thread da classe de processamento de CDRs de Dados/Voz
		// de acordo com o numero especificado no parametro
		for (int i=0; i < numThreads; i++)
		{
			ProcessaArquivoCDRDadosVoz procCDR = new ProcessaArquivoCDRDadosVoz(super.getIdLog());
			procCDR.start();
			poolProcCDRDadosVoz.add(procCDR);
		}
		super.log(Definicoes.INFO,"GerenteProcessamentoCDR","Pool de processamento de arquivos de CDR foi inicializado. Dados/Voz:"+numThreads+" Threads");
	}
	
	/**
	 * Metodo....:inicializaPoolAprRec
	 * Descricao.:Este metodo inicializa o pool de objetos que irao
	 *            realizar o processamento dos arquivos de CDR de
	 *            Aprovisionamento/Recarga
	 * 
	 * @param numThreads	- Numero de Threads iniciais
	 * @throws GPPInternalErrorException
	 */
	public void inicializaPoolAprRec(int numThreads) throws GPPInternalErrorException
	{
		// Cria uma thread da classe de processamento de CDRs de Apr/Rec
		// de acordo com o numero especificado no parametro
		for (int i=0; i < numThreads; i++)
		{
			ProcessaArquivoCDRAprRec procCDR = new ProcessaArquivoCDRAprRec(super.getIdLog());
			procCDR.start(); 
			poolProcCDRAprRec.add(procCDR);
		}
		super.log(Definicoes.INFO,"GerenteProcessamentoCDR","Pool de processamento de arquivos de CDR foi inicializado. Aprovis./Recarga:"+numThreads+" Threads");
	}

	/**
	 * Metodo....:addThreadProcCDRDadosVoz
	 * Descricao.:Este metodo adiciona uma thread de processamento de CDRs ao pool correspondente
	 * @return boolean - Indica se conseguiu adicionar a thread ou nao
	 * @throws GPPInternalErrorException
	 */	
	public synchronized boolean addThreadProcCDRDadosVoz() throws GPPInternalErrorException
	{
		ProcessaArquivoCDRDadosVoz procCDR = new ProcessaArquivoCDRDadosVoz(super.getIdLog());
		procCDR.start();
		return poolProcCDRDadosVoz.add(procCDR);
	}
	
	/**
	 * Metodo....:addThreadProcCDRAprRec
	 * Descricao.:Este metodo adiciona uma thread de processamento de CDRs ao pool correspondente
	 * @return boolean - Indica se conseguiu adicionar a thread ou nao
	 * @throws GPPInternalErrorException
	 */	
	public synchronized boolean addThreadProcCDRAprRec() throws GPPInternalErrorException
	{
		ProcessaArquivoCDRAprRec procCDR = new ProcessaArquivoCDRAprRec(super.getIdLog());
		procCDR.start(); 
		return poolProcCDRAprRec.add(procCDR);
	}
	
	/**
	 * Metodo....:removeThreadProcCDRDadosVoz
	 * Descricao.:Este metodo remove uma thread de processamento de CDRs ao pool correspondente
	 * @return boolean - Indica se conseguiu remover a thread ou nao
	 * @throws GPPInternalErrorException
	 */	
	public synchronized boolean removeThreadProcCDRDadosVoz() throws GPPInternalErrorException
	{
		ProcessaArquivoCDRDadosVoz procCDR = (ProcessaArquivoCDRDadosVoz)poolProcCDRDadosVoz.iterator().next();
		return poolProcCDRDadosVoz.remove(procCDR);
	}
	
	/**
	 * Metodo....:removeThreadProcCDRAprRec
	 * Descricao.:Este metodo remove uma thread de processamento de CDRs ao pool correspondente
	 * @return boolean - Indica se conseguiu remover a thread ou nao
	 * @throws GPPInternalErrorException
	 */	
	public synchronized boolean removeThreadProcCDRAprRec() throws GPPInternalErrorException
	{
		ProcessaArquivoCDRAprRec procCDR = (ProcessaArquivoCDRAprRec)poolProcCDRAprRec.iterator().next();
		return poolProcCDRAprRec.remove(procCDR);
	}

	/**
	 * Metodo....:notificaThreadsDadosVoz
	 * Descricao.:Faz a notificacao das threads para processamento dos CDRs de Dados/Voz
	 *
	 */
	public void notificaThreadsDadosVoz()
	{
		// Para cada Thread do pool faz-se a notificacao para que
		// cada uma volte a realizar o processamento
		for (Iterator i=poolProcCDRDadosVoz.iterator(); i.hasNext();)
		{
			ProcessaArquivoCDRDadosVoz procCDR = (ProcessaArquivoCDRDadosVoz)i.next();
			procCDR.reiniciaProcessamento();
		}
	}
	
	/**
	 * Metodo....:notificaThreadsAprRec
	 * Descricao.:Faz a notificacao das threads para processamento dos CDRs de Apr/Rec
	 *
	 */
	public void notificaThreadsAprRec()
	{
		// Para cada Thread do pool faz-se a notificacao para que
		// cada uma volte a realizar o processamento
		for (Iterator i=poolProcCDRAprRec.iterator(); i.hasNext();)
		{
			ProcessaArquivoCDRAprRec procCDR = (ProcessaArquivoCDRAprRec)i.next();
			procCDR.reiniciaProcessamento();
		}
	}
	
	/**
	 * Metodo....:getNumThreadsAtivasDadosVoz
	 * Descricao.:Retorna o numero de threads existentes no pool de threads
	 * @return int - Numero de threads ativas para processamento de arquivos de CDR
	 */
	public int getNumThreadsAtivasDadosVoz()
	{
		return poolProcCDRDadosVoz.size();
	}
	
	/**
	 * Metodo....:getNumThreadsAtivasEvtRec
	 * Descricao.:Retorna o numero de threads existentes no pool de threads
	 * @return int - Numero de threads ativas para processamento de arquivos de CDR
	 */
	public int getNumThreadsAtivasEvtRec()
	{
		return poolProcCDRAprRec.size();
	}
	
	/**
	 * Metodo....:removeThreadsDadosVoz
	 * Descricao.:Remove as threads de importacao de arquivos de Dados e Voz
	 *
	 */
	public void removeThreadsDadosVoz()
	{
		// Realiza a iteracao entre todas as threads atuais e marca para 
		// que estas nao mais sejam executadas
		for (Iterator i=poolProcCDRDadosVoz.iterator(); i.hasNext();)
		{
			ProcessaArquivoCDRDadosVoz procCDR = (ProcessaArquivoCDRDadosVoz)i.next();
			procCDR.destroy();
		}
		poolProcCDRDadosVoz.clear();
		super.log(Definicoes.INFO,"GerenteProcessamentoCDR","Pool de processamento de arquivos de CDR Dados/Voz foi removido.");
	}
	
	/**
	 * Metodo....:removeThreadsEvtRec
	 * Descricao.:Remove as threads de importacao de Eventos e Recargas
	 *
	 */
	public void removeThreadsEvtRec()
	{
		// Realiza a iteracao entre todas as threads atuais e marca para 
		// que estas nao mais sejam executadas
		for (Iterator i=poolProcCDRAprRec.iterator(); i.hasNext();)
		{
			ProcessaArquivoCDRAprRec procCDR = (ProcessaArquivoCDRAprRec)i.next();
			procCDR.destroy();
		}
		poolProcCDRAprRec.clear();
		super.log(Definicoes.INFO,"GerenteProcessamentoCDR","Pool de processamento de arquivos de CDR Eventos/Recargas foi removido.");
	}
}
