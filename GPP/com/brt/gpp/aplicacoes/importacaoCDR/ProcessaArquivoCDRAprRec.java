package com.brt.gpp.aplicacoes.importacaoCDR;


import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRAprRec;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerenteArquivosCDR;
import java.io.File;

/**
  * Esta classe realiza a importacao dos arquivos de CDR de Dados/Voz
  * disponibilizados pelo GerenteArquivosCDR
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

public class ProcessaArquivoCDRAprRec extends Thread
{
	private MapConfiguracaoGPP 		configGPP;
	private GerenteArquivosCDR 		gerArquivosCDR;
	private GerentePoolLog			gerLog;
	private long					idProcesso;
	private static boolean			deveExecutar;
	
	/**
	 * Metodo....:ProcessaArquivoCDRAprRec
	 * Descricao.:Construtor da classe. Inicializa instancia
	 * @param idProcessamento	- Id do processo que iniciou a instancia da classe
	 * @throws GPPInternalErrorException
	 */
	public ProcessaArquivoCDRAprRec(long idProcesso) throws GPPInternalErrorException
	{
		// Instancia as referencias necessarias para o processamento
		configGPP 		= MapConfiguracaoGPP.getInstancia();
		gerArquivosCDR	= GerenteArquivosCDR.getInstance(idProcesso);
		gerLog			= GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso	= idProcesso;
		deveExecutar    = true;
	}

	/**
	 * Metodo....:reiniciaProcessamento
	 * Descricao.:Reincia execucao da thread
	 *
	 */
	public synchronized void reiniciaProcessamento()
	{
		notify();
	}

	/**
	 * Metodo....:destroy
	 * Descricao.:Marca a thread para nao mais ser executada
	 *  (non-Javadoc)
	 * @see java.lang.Thread#destroy()
	 */
	public void destroy()
	{
		synchronized(this)
		{
			deveExecutar=false;
		}
	}

	/**
	 * Metodo....:run
	 * Descricao.:Executa o procedimento para importacao do CDR
	 *            de Dados/Voz
	 * @see java.lang.Thread.run()
	 */
	public void run()
	{
		// Cria referencia para o parse de arquivos de CDR para Eventos/Recarga
		ArquivoCDRAprRec arqCDRParser = new ArquivoCDRAprRec(idProcesso);

		// Cria referencia para a classe que realiza a importacao do Arquivo
		ImportaArquivoDados impCDR = new ImportaArquivoDados(idProcesso);

		// Cria referencia para realizar a verificacao da integridade do arquivo
		BatimentoArquivoCDR batCDR = new BatimentoArquivoCDR(idProcesso);

		// Define o intervalo de espera caso nenhum arquivo esteja pendente de importacao
		long espera = Long.parseLong(configGPP.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_TEMPO_ESPERA_PROC_ARQUIVOS));

		// Fica processando ate que uma solicitacao para finalizacao da thread
		// seja executada. Em certo intervalo de tempo a thread fica em espera por
		// alguns segundos
		while(deveExecutar)
		{
			// Faz a tentativa de processar um arquivo
			File arquivoCDR=null;
			try
			{
				// Busca no pool de arquivos o proximo a ser processado
				arquivoCDR = gerArquivosCDR.getArquivoAprRec();
				if (arquivoCDR != null)
				{
					gerLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_PROCESSA_ARQUIVO_CDR_APRREC,"run","Processando Arquivo:"+arquivoCDR);

					// Verifica a integridade do arquivo em relacao ao numero de linhas
					if (batCDR.verificaNumeroLinhas(arquivoCDR))
						// Envia o arquivo para processamento juntamente com o parser
						// correspondente
						impCDR.importaArquivo(arquivoCDR,arqCDRParser);
	
					// Se a importacao foi executada, entao retira o arquivo do diretorio
					// de trabalho e entao move-o para o diretorio historico
					// caso o arquivo tenha sua integridade divergente mesmo assim
					// move o arquivo para o historico porem sem nenhuma importacao
					gerArquivosCDR.liberaArquivoDadosVoz(arquivoCDR);
					gerLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_PROCESSA_ARQUIVO_CDR_APRREC,"run","Arquivo:"+arquivoCDR+" processado.");
				}
				else
				{
					synchronized(this)
					{
						// Apos o processamento a Thread entra em espera por um periodo
						// configurado no GPP em segundos
						wait(espera*1000);
					}
				}
			}
			catch(Exception e)
			{
				gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_PROCESSA_ARQUIVO_CDR_APRREC,"run","Erro ao processar arquivo de CDR:"+arquivoCDR + " Erro:"+e);
			}
		}
	}
}
