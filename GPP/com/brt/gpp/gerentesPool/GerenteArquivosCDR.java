package com.brt.gpp.gerentesPool;

import com.brt.gpp.aplicacoes.importacaoCDR.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.Aplicacoes;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
  *
  * Esta classe realiza o tratamento de producao dos arquivos disponiveis
  * para importacao de CDR. Esta classe apos realizar a leitura do diretorio
  * configurado para a importacao de arquivos, insere cada arquivo atraves
  * da classe File para o vetor correspondente ao tipo de CDR.
  * 
  * Esta classe e uma thread sendo que apos a execucao de seu processo esta 
  * fica inativa por um periodo de tempo configuravel e entao retoma novamente
  * seu processamento.
  * 
  * Funcionamento do pool de arquivos:
  * 1) O arquivo ao ser identificado no diretorio de processamento, e inserido
  *    no pool de arquivos disponiveis correspondente ao seu tipo (Dados/Voz ou Apr/Rec)
  * 
  * 2) As Threads que irao processar o arquivo busca o mesmo no pool de disponiveis
  *    e movendo para o pool de utilizados
  * 
  * 3) Apos o processamento estas threads irao liberar o arquivo onde o mesmo entao
  *    e movido para o diretorio historico e removido do pool de utilizados
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos Lemgruber Junior
  * Data: 				09/08/2004
  *
  * Modificado por:		Joao Carlos
  * Data:				23/09/2004
  * Razao:				Troca da configuracao dos diretorios do mapeamento de configuracao
  * 					para a utilizacao do arquivo de configuracoes do sistema
  *
  */

public class GerenteArquivosCDR extends Aplicacoes implements Runnable
{
	private static 	GerenteArquivosCDR 		instance;
	private			Collection				poolArquivosDadosVoz;
	private 		Collection				poolArquivosDadosVozUtilizados;
	private 		Collection				poolArquivosAprRec;
	private 		Collection				poolArquivosAprRecUtilizados;
		
	/**
	 * Metodo....:GerenteArquivosCDR
	 * Descricao.:Construtor da classe. Inicializa os vetores
	 *            que irao armazenar as referencias para os
	 *            arquivos de CDR
	 *
	 */
	private GerenteArquivosCDR(long idProcesso)
	{
		super(idProcesso, Definicoes.CL_GERENCIADOR_ARQUIVOS_CDR);
		
		// Inicia os vetores que irao armazenar as referencias
		// para os arquivos de CDR do diretorio de importacao
		poolArquivosDadosVoz 			= new LinkedHashSet();
		poolArquivosDadosVozUtilizados	= new LinkedHashSet();
		poolArquivosAprRec				= new LinkedHashSet();
		poolArquivosAprRecUtilizados	= new LinkedHashSet();
		try
		{
			inicializaPool();
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO,"GerenteArquivosCDR","NAO foi possivel inicializacao gerenciamento de arquivos de CDR. Erro:"+e.getMessage());
		}
		super.log(Definicoes.DEBUG,"inicializaPool","Pool de gerenciamento de arquivos de CDR INICIALIZADO.");		 
	}

	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia inicializada dessa classe (Singleton)
	 * @param idProcesso				- Id do processo que solicitou a instancia
	 * @return GerenciadorArquivosCDR	- Instancia da classe
	 */	
	public static GerenteArquivosCDR getInstance(long idProcesso)
	{
		// Se a instancia do sistema GPP permitir a importacao de CDRs, entao o cache
		// eh construido e inicializado, senao nada eh feito e a referencia para o objeto
		// retorna nulo
		ArquivoConfiguracaoGPP conf = ArquivoConfiguracaoGPP.getInstance();
		if (conf.deveImportarCDR())
			if (instance == null)
			{
				// Caso a instancia ainda nao tenha sido criada, entao
				// apos definir todos os objetos necessarios a instancia
				// e adicionada a uma Thread para ser executada
				instance = new GerenteArquivosCDR(idProcesso);
				Thread t = new Thread(instance);
				t.start();
			}
			
		return instance;
	}

	/**
	 * Metodo....:inicializaPool
	 * Descricao.:Faz a inicializacao dos arquivos a serem processados
	 *            existentes no diretorio ao iniciar a classe
	 * @throws GPPInternalErrorException
	 */
	private void inicializaPool() throws GPPInternalErrorException
	{
		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();

		// Primeiro move o arquivo para o diretorio de trabalho buscando o diretorio
		// configurado no mapeamento do GPP
		File dirTrabalho = new File(config.getDirTrabalhoCdr());
		if (dirTrabalho == null || !dirTrabalho.isDirectory())
			throw new GPPInternalErrorException("Diretorio "+dirTrabalho.getAbsolutePath()+" nao e um diretorio valido.");

		// Para os dois tipos de arquivos existentes, faz a busca se existe algum no diretorio de
		// trabalho na inicializacao do gerenciador, em caso positivo entao adiciona estes arquivos
		// ao pool de arquivos a serem processados de acordo com seu tipo		
		adicionaArquivosDadosVoz(dirTrabalho);
		adicionaArquivosAprRec(dirTrabalho);
	}

	/**
	 * Metodo....:moveArquivo
	 * Descricao.:Move varios arquivos de CDR para um diretorio qualquer
	 * @param arquivos		- Arquivos de CDR
	 * @param dir			- Diretorio destino
	 * @throws GPPInternalErrorException
	 */
//	private void moveArquivo(File arquivos[], String dir) throws GPPInternalErrorException
//	{
//		// Faz a varredura em todos os arquivos que serao
//		// movidos para o diretorio destino
//		for (int i=0; i < arquivos.length; i++)
//			moveArquivo(arquivos[i],dir);
//	}

	/**
	 * Metodo....:moveArquivo
	 * Descricao.:Move um arquivo de CDR para um diretorio qualquer
	 * @param arquivo	- Arquivo de CDR
	 * @param dir		- Diretorio de destino
	 * @param idProcesso
	 * @return File		- Retorna a referencia para o arquivo renomeado
	 * @throws GPPInternalErrorException
	 */
	public static File moveArquivo(File arquivo, String dir, long idProcesso)
	{
		// Cria a referencia do arquivo para o novo diretorio
		File arqDestino = new File(dir+System.getProperty("file.separator")+arquivo.getName());
		
		// Move arquivo, em caso de erro entao registra no LOG do sistema
		// e retorna uma referencia nula
		boolean moveuArquivo = arquivo.renameTo(arqDestino);
		if (!moveuArquivo)
		{
			// Caso nao seja possivel renomear o arquivo (Ex: particoes diferentes no unix) entao
			// realiza o processo de copia do arquivo, apos a copia o arquivo original entao eh
			// removido. Caso o processo de copia tambem falhe, entao o arquivo permance aonde esta
			// e uma informacao no log eh registrada
			if (copiaArquivo(arquivo,arqDestino))
				arquivo.delete();
			else
			{
				GerentePoolLog.getInstancia(GerenteArquivosCDR.class).log(idProcesso,Definicoes.ERRO,"GerenteArquivosCDR","moveArquivo","Nao foi possivel mover o arquivo:"+arquivo.getAbsolutePath()+" para o diretorio:"+dir);
				return null;
			}
		}

		return arqDestino;
	}

	/**
	 * Metodo....:copiaArquivo
	 * Descricao.:Copia um arquivo para o destino 
	 * @param origem	- Arquivo de origem
	 * @param destino	- Arquivo de destino
	 * @return boolean	- Indica se conseguiu copiar o arquivo
	 */
	private static boolean copiaArquivo(File origem, File destino)
	{
		boolean copiou = false;
		try
		{
			FileInputStream input 	= new FileInputStream(origem);
			FileOutputStream output = new FileOutputStream(destino);
			int sizeBuffer = 2048;
			byte[] buffer = new byte[sizeBuffer];
			int count=0;
			while( (count=input.read(buffer, 0, sizeBuffer)) != -1)
				output.write(buffer,0,count);

			output.close();
			input.close();
			copiou = true;
		}
		catch(IOException e)
		{
			copiou = false;
		}
		return copiou;
	}

	/**
	 * Metodo....:addArquivoAprRec
	 * Descricao.:Adiciona o arquivo de CDR contendo dados de Aprovisionamento e ou Recargas no
	 *            pool correspondente
	 * 
	 * OBS: Primeiro o metodo verifica se foi possivel renomea-lo para o diretorio de trabalho
	 * 
	 * @param arquivo	- Arquivo de CDR
	 * @return boolean	- Indica se adicionou no pool de arquivos ou nao
	 * @throws GPPInternalErrorException
	 */
	private synchronized boolean addArquivoAprRec(File arquivo)
	{
		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();

		// Primeiro move o arquivo para o diretorio de trabalho buscando o diretorio
		// configurado no mapeamento do GPP
		String dirTrabalho = config.getDirTrabalhoCdr();
		File arqDestino = moveArquivo(arquivo,dirTrabalho,super.getIdLog());
		
		// Se a referencia do arquivo for diferente de nulo entao adiciona
		// o mesmo no pool senao indica que o arquivo nao foi previamente
		// movido de diretorio
		boolean adicionou=false;
		if (arqDestino != null)
			adicionou = poolArquivosAprRec.add(arqDestino);
			
		return adicionou;
	}

	/**
	 * Metodo....:addArquivoDadosVoz
	 * Descricao.:Adiciona o arquivo de CDR contendo Dados/Voz no pool correspondente
	 * OBS: Primeiro o metodo verifica se foi possivel renomea-lo para o diretorio
	 *      de trabalho
	 * @param arquivo	- Arquivo de CDR
	 * @return boolean  - Indica se adicionou no pool de arquivos ou nao
	 * @throws GPPInternalErrorException
	 */
	private synchronized boolean addArquivoDadosVoz(File arquivo)
	{
		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();

		// Primeiro move o arquivo para o diretorio de trabalho buscando o diretorio
		// configurado no mapeamento do GPP
		String dirTrabalho = config.getDirTrabalhoCdr();
		File arqDestino = moveArquivo(arquivo,dirTrabalho,super.getIdLog());
		
		// Se a referencia do arquivo for diferente de nulo entao adiciona
		// o mesmo no pool senao indica que o arquivo nao foi previamente
		// movido de diretorio
		boolean adicionou=false;
		if (arqDestino != null)
			return poolArquivosDadosVoz.add(arqDestino);

		return adicionou;
	}

	/**
	 * Metodo....:getArquivoAprRec
	 * Descricao.:Este metodo retorna um arquivo de CDR disponivel
	 *            para processamento
	 * @return File			- Referencia para o arquivo a ser processado
	 */
	public synchronized File getArquivoAprRec()
	{
		File retorno=null;
		// Busca o primeiro elemento do pool de arquivos de Apr/Rec
		// e entao remove-o do pool de arquivos disponiveis adicionando-o
		// ao pool de elementos utilizados
		if (poolArquivosAprRec.size() > 0)
		{
			File arquivo = (File)poolArquivosAprRec.iterator().next();
			if (poolArquivosAprRec.remove(arquivo))
				if (poolArquivosAprRecUtilizados.add(arquivo))
					retorno = arquivo;
				else poolArquivosAprRec.add(arquivo);
		}
		return retorno;
	}

	/**
	 * Metodo....:getArquivoDadosVoz
	 * Descricao.:Este metodo retorna um arquivo de CDR disponivel
	 *            para processamento
	 * @return File			- Referencia para o arquivo a ser processado
	 */
	public synchronized File getArquivoDadosVoz()
	{
		File retorno=null;
		// Busca o primeiro elemento do pool de arquivos de Dados/Voz
		// e entao remove-o do pool de arquivos disponiveis adicionando-o
		// ao pool de elementos utilizados
		if (poolArquivosDadosVoz.size() > 0)
		{
			File arquivo = (File)poolArquivosDadosVoz.iterator().next();
			if (poolArquivosDadosVoz.remove(arquivo))
				if (poolArquivosDadosVozUtilizados.add(arquivo))
					retorno = arquivo;
				else poolArquivosDadosVoz.add(arquivo);
		}
		return retorno;
	}

	/**
	 * Metodo....:liberaArquivoDadosVoz
	 * Descricao.:Remove o arquivo de CDR de Dados/Voz do pool correspondente
	 * 
	 * OBS: Antes de remover do pool, o arquivo e movido para um diretorio de historico
	 *      para posterior consulta se necessario
	 * @param arquivo	- Arquivo de CDR de Dados/Voz
	 * @return boolean  - Indica se conseguiu remover ou nao do pool
	 * @throws GPPInternalErrorException
	 */
	public synchronized boolean liberaArquivoDadosVoz(File arquivo) throws GPPInternalErrorException
	{
		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();

		// Primeiro move o arquivo para o diretorio de historico buscando o nome do
		// diretorio configurado no mapeamento do GPP
		String dirHistorico = config.getDirHistoricoCdr();
		moveArquivo(arquivo,dirHistorico,super.getIdLog());
		
		// Verifica se existe o arquivo .counter deste CDR e entao move-o para o 
		// diretorio de historico tambem
		moveArquivoIntegridade(arquivo);
		
		// Independente se conseguiu mover ou nao o arquivo retira o elemento
		// do pool correspondente
		return poolArquivosDadosVozUtilizados.remove(arquivo);
	}

	/**
	 * Metodo....:liberaArquivoAprRec
	 * Descricao.:Remove o arquivo de CDR de Aprovisionamento/Recarga do pool correspondente
	 * 
	 * OBS: Antes de remover do pool, o arquivo e movido para um diretorio de historico
	 *      para posterior consulta se necessario
	 * @param arquivo	- Arquivo de CDR de Aprovisionamento/Recarga
	 * @return boolean  - Indica se conseguiu remover ou nao do pool
	 * @throws GPPInternalErrorException
	 */
	public synchronized boolean liberaArquivoAprRec(File arquivo) throws GPPInternalErrorException
	{
		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();

		// Primeiro move o arquivo para o diretorio de historico buscando o nome do
		// diretorio configurado no mapeamento do GPP
		String dirHistorico = config.getDirHistoricoCdr();
		moveArquivo(arquivo,dirHistorico,super.getIdLog());

		// Verifica se existe o arquivo .counter deste CDR e entao move-o para o 
		// diretorio de historico tambem
		moveArquivoIntegridade(arquivo);

		// Independente se conseguiu mover ou nao o arquivo retira o elemento
		// do pool correspondente
		return poolArquivosAprRecUtilizados.remove(arquivo);
	}

	/**
	 * Metodo....:moveArquivoIntegridade
	 * Descricao.:Este metodo move o arquivo de integridade (.counter) para o diretorio
	 *            de historico caso ele exista
	 * @param arquivoCDR	- Arquivo de CDR correspondente
	 * @throws GPPInternalErrorException
	 */
	private void moveArquivoIntegridade(File arquivoCDR) throws GPPInternalErrorException
	{
		// Busca a referencia para a classe de mapeamento de
		// configuracoes do sistema GPP
		MapConfiguracaoGPP config = MapConfiguracaoGPP.getInstancia();
		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		
		// Busca o nome do arquivo de integridade e verifica se tal arquivo existe.
		// Em caso de positivo entao ele e movido para o diretorio de historico, senao
		// o processo nao realiza nenhum comando
		String dirImportacao 	= arqConf.getDirImportacaoCdr();
		String dirHistorico     = arqConf.getDirHistoricoCdr();
		String extensao			= config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_EXTENSAO_ARQUIVO_COUNTER);
		String nomeArquivo      = dirImportacao+System.getProperty("file.separator")+arquivoCDR.getName().replaceAll("[.]cdr",extensao);
		File arquivoIntegridade = new File(nomeArquivo);
		
		if (arquivoIntegridade.exists())
			moveArquivo(arquivoIntegridade,dirHistorico,super.getIdLog());
	}

	/**
	 * Metodo....:adicionaArquivosDadosVoz
	 * Descricao.:Este metodo lista os arquivos de Dados/Voz existentes no diretorio
	 *            de importacao e adiciona os arquivos no pool correspondente
	 * @param dirImportacao	- Diretorio de importacao
	 * @throws GPPInternalErrorException
	 */
	private void adicionaArquivosDadosVoz(File dirImportacao) throws GPPInternalErrorException
	{
		// Busca a referencia para a classe de mapeamento dos dados de 
		// configuracao do GPP
		MapConfiguracaoGPP config = MapConfiguracaoGPP.getInstancia();

		// Para o tipo de arquivo (Dados/Voz) o gerenciador popula o vetor
		// correspondente sendo que antes move os arquivos para um diretorio 
		// de trabalho temporario para processamento dos CDRs
		String nomArqDadosVoz = config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_NOM_ARQUIVOS_DADOSVOZ);
		File arqDadosVoz[] = dirImportacao.listFiles(new CDRFileFilter(nomArqDadosVoz));
		for (int i=0; i<arqDadosVoz.length; i++)
			if (!addArquivoDadosVoz(arqDadosVoz[i]))
				super.log(Definicoes.DEBUG,"run","NAO foi possivel adicionar o arquivo ao pool de arquivos de Dados e Voz :"+arqDadosVoz[i]);
	}

	/**
	 * Metodo....:adicionaArquivosAprRec
	 * Descricao.:Este metodo lista os arquivos de AprRec existentes no diretorio
	 *            de importacao e adiciona os arquivos no pool correspondente
	 * @param dirImportacao	- Diretorio de importacao
	 * @throws GPPInternalErrorException
	 */
	private void adicionaArquivosAprRec(File dirImportacao) throws GPPInternalErrorException
	{
		// Busca a referencia para a classe de mapeamento dos dados de 
		// configuracao do GPP
		MapConfiguracaoGPP config = MapConfiguracaoGPP.getInstancia();

		// Para o tipo de arquivo (Apr/Rec) o gerenciador popula o vetor
		// correspondente sendo que antes move os arquivos para um diretorio 
		// de trabalho temporario para processamento dos CDRs
		String nomArqAprRec = config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_NOM_ARQUIVOS_EVENTORECARGA);
		File arqAprRec[] = dirImportacao.listFiles(new CDRFileFilter(nomArqAprRec));
		for (int i=0; i<arqAprRec.length; i++)
			if (!addArquivoAprRec(arqAprRec[i]))
				super.log(Definicoes.DEBUG,"run","NAO foi possivel adicionar o arquivo ao pool de arquivos de Aprovisionamento e Recarga :"+arqAprRec[i]);		
	}

	/**
	 * Metodo....:notificaThreadsProcessamento
	 * Descricao.:Este metodo realiza a notificacao para o gerenciador de threads
	 *            de processamento de CDRs que existem arquivos a serem processados
	 *            no pool.
	 * @throws GPPInternalErrorException
	 */
	private void notificaThreadsProcessamento() throws GPPInternalErrorException
	{
		// Busca a referencia para a instancia da classe de gerenciamento
		// das threads de processamento dos arquivos de CDR 
		GerenteProcessamentoCDR gerCDR = GerenteProcessamentoCDR.getInstance(super.getIdLog());

		// Se o pool de arquivos de Dados/Voz estiver com arquivos ainda nao
		// processados ou em processamento entao notifica as threads de processamento
		// deste tipo de arquivo para acordarem
		if (poolArquivosDadosVoz.size() > 0)
			gerCDR.notificaThreadsDadosVoz();
			
		// Se o pool de arquivos de Aprovisionamento/Recarga estiver com arquivos ainda nao
		// processados ou em processamento entao notifica as threads de processamento
		// deste tipo de arquivo para acordarem
		if (poolArquivosAprRec.size() > 0)
			gerCDR.notificaThreadsAprRec();
	}

	/**
	 * Metodo....:run
	 * Descricao.:Executa o procedimento da thread
	 * @see java.lang.Thread.run()
	 */
	public void run()
	{
		try
		{
			// Busca a referencia para a classe de mapeamento dos dados de 
			// configuracao do GPP
			MapConfiguracaoGPP config = MapConfiguracaoGPP.getInstancia();
			// Busca a referencia para o arquivo de configuracao do sistema GPP
			ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();

			while(true)
			{
				super.log(Definicoes.DEBUG,"run","Buscando arquivos de CDR no diretorio a serem processados...");
	
				// Busca no mapeamento de configuracao qual e o diretorio onde os arquivos
				// sao armazenados
				File dirImportacao = new File(arqConf.getDirImportacaoCdr());
				if (dirImportacao == null || !dirImportacao.isDirectory())
					throw new GPPInternalErrorException("Diretorio "+dirImportacao.getAbsolutePath()+" nao e um diretorio valido.");
	
				// Para cada tipo de arquivo, adiciona-os nos pool de arquivo correspondentes
				adicionaArquivosDadosVoz(dirImportacao);
				adicionaArquivosAprRec(dirImportacao);
	
				// Notifica as threads para processamento de CDR que existem arquivos
				// a serem processados
				notificaThreadsProcessamento();
				
				// Apos o processamento a thread fica em espera para posteriormente
				// repetir o processamento
				long tempoEspera = Long.parseLong(config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_TEMPO_ESPERA_LEITURA_DIRETORIO));
				synchronized(this)
				{
					wait(tempoEspera*1000);
				}
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"run","Excecao ocorrida ao processar arquivos de importacao de CDR:"+e);
		}
	}

	/**
	 * Metodo....:getNumArquivosPendentesDadosVoz
	 * Descricao.:Retorna o numero de arquivos pendentes ou em processamento no pool de arquivos
	 * @return int - Numero de arquivos pendentes de processamento
	 */
	public int getNumArquivosPendentesDadosVoz()
	{
		return poolArquivosDadosVoz.size() + poolArquivosDadosVozUtilizados.size();
	}

	/**
	 * Metodo....:getNumArquivosPendentesEvtRec
	 * Descricao.:Retorna o numero de arquivos pendentes ou em processamento no pool de arquivos
	 * @return int - Numero de arquivos pendentes de processamento
	 */
	public int getNumArquivosPendentesEvtRec()
	{
		return poolArquivosAprRec.size() + poolArquivosAprRecUtilizados.size();
	}
}
