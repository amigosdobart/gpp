package com.brt.gpp.aplicacoes.importacaoCDR;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
  * Esta classe realiza o batimento do arquivo de CDR. 
  * Sao implementados diferentes metodos para validacao do arquivo de dados
  * para garantir a integridade do mesmo.
  *  
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				16/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class BatimentoArquivoCDR
{
	private long					idProcesso;
	private GerentePoolLog			gerLog;
	private MapConfiguracaoGPP 		config;
	private ArquivoConfiguracaoGPP 	arqConf;

	/**
	 * Metodo....:BatimentoArquivoCDR
	 * Descricao.:Este metodo e o construtor da classe
	 * @param arquivoCDR	- Arquivo de CDR a ser feito o batimento
	 * @param idProcesso	- Id do processo que esta executando o batimento
	 */	
	public BatimentoArquivoCDR(long idProcesso)
	{
		try
		{
			this.idProcesso = idProcesso;
			this.gerLog		= GerentePoolLog.getInstancia(this.getClass());
			this.config		= MapConfiguracaoGPP.getInstancia();
			this.arqConf 	= ArquivoConfiguracaoGPP.getInstance();
		}
		catch(GPPInternalErrorException e)
		{
			gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_BATIMENTO_ARQUIVO_CDR,"BatimentoArquivoCDR","Mapeamento das configuracoes do sistema nao foram carregadas..");
		}
	}

	/**
	 * Metodo....:verificaNumeroLinhas
	 * Descricao.:Este metodo verifica se o arquivo de CDR passado como parametro
	 *            possui um arquivo com o mesmo nome e a extensao .counter para
	 *            que seja verificado a integridade do numero de linhas existentes
	 *            neste CDR.
	 * 
	 * 			  Caso o arquivo .counter exista entao o processo cria um novo arquivo
	 *            dependendo da situacao.
	 * 			  1) Arquivo CDR possui mesmo numero de linhas que o .counter indica
	 * 				 Entao e criado um arquivo vazio com o mesmo nome do arquivo de 
	 *               CDR com a extensao .recebido
	 * 
	 * 			  2) Arquivo CDR diverge com relacao ao numero de linhas que o .counter indica
	 * 				 Entao e criado um arquivo vazio com o mesmo nome do arquivo de 
	 *  			 CDR com a extensao .erro
	 * 
	 * 			  Ambos as definicoes da extensao estao definidos na classe Definicoes.java
	 * 
	 * @param arquivoCDR	- Arquivo CDR
	 * @return	boolean		- Indica se o arquivo esta ok ou nao com relacao ao numero de linhas
	 * @throws GPPInternalErrorException
	 */
	public boolean verificaNumeroLinhas(File arquivoCDR) throws GPPInternalErrorException
	{
		boolean verificou=true;
		// Busca a referencia para o arquivo .counter que se existir
		// esta no diretorio de dados onde os CDRs sao armazenados.
		// OBS: Os arquivos .counter nao vao para o diretorio de trabalho
		//      pois nao necessitam de nenhum tratamento especial ja que
		//      sao comparados somente quando ha um processamento de um 
		//      arquivo de CDR especifico
		File arquivoCounter = new File(getNomeArquivoCounter(arquivoCDR));
		if (arquivoCounter.exists())
		{
			try
			{
				// Verifica o numero de linhas dos arquivos e entao cria 
				// os arquivos recibos de acordo com o resultado
				if (getNumeroLinhas(arquivoCDR) == getNumLinhasArquivoCounter(arquivoCounter))
				{
					new File(getNomeArquivoReciboOk(arquivoCDR)).createNewFile();
					verificou=true;
				}
				else
				{
					new File(getNomeArquivoReciboNaoOk(arquivoCDR)).createNewFile();
					verificou=false;
				}
			}
			catch(Exception e)
			{
				// Em caso do arquivo .counter existir e algum erro ao listar o 
				// arquivo tenha acontecido, entao o retorno e que a verificacao foi
				// falsa 
				gerLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_BATIMENTO_ARQUIVO_CDR,"verificaNumeroLinhas","Erro ao verificar o arquivo .counter na importacao de CDRs");
				verificou=false;
			}
		}
		return verificou;
	}

	/**
	 * Metodo....:getNomeArquivoReciboOk
	 * Descricao.:Este metodo retorna o nome do arquivo recibo de CDR para indicar
	 *            que o arquivo de CDR foi transferido corretamente
	 * @param f 		- Arquivo de CDR
	 * @return String 	- Nome do arquivo recibo ok
	 */
	private String getNomeArquivoReciboOk(File f)
	{
		String dirImportacao 	= arqConf.getDirImportacaoCdr();
		String extensaoOk  		= config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_EXTENSAO_RECIBO_OK);
		return dirImportacao+System.getProperty("file.separator")+f.getName().replaceAll("[.]cdr",extensaoOk);
	}
	
	/**
	 * Metodo....:getNomeArquivoReciboNaoOk
	 * Descricao.:Este metodo retorna o nome do arquivo recibo de CDR para indicar
	 *            que o arquivo de CDR NAO foi transferido corretamente
	 * @param f			- Arquivo de CDR
	 * @return String	- Nome do arquivo de recibo nao ok
	 */	
	private String getNomeArquivoReciboNaoOk(File f)
	{
		String dirImportacao 	= arqConf.getDirImportacaoCdr();
		String extensaoNaoOk 	= config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_EXTENSAO_RECIBO_NAO_OK);
		return dirImportacao+System.getProperty("file.separator")+f.getName().replaceAll("[.]cdr",extensaoNaoOk);
	}

	/**
	 * Metodo....:getNomeArquivoCounter
	 * Descricao.:Retorna o nome do arquivo .counter do arquivo de CDR especifico
	 *            lembrando que o diretorio de origem e o mesmo diretorio de origem
	 *            do arquivo de CDR sendo alterado somente a extensao do mesmo mantendo
	 *            o mesmo nome
	 * @param  File		- Arquivo de CDR a ser utilizado
	 * @return String	- Nome do arquivo .counter deste CDR
	 * @throws GPPInternalErrorException
	 */
	private String getNomeArquivoCounter(File arquivoCDR) throws GPPInternalErrorException
	{
		String dirImportacao 	= arqConf.getDirImportacaoCdr();
		String extensao			= config.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_EXTENSAO_ARQUIVO_COUNTER);
		return dirImportacao+System.getProperty("file.separator")+arquivoCDR.getName().replaceAll("[.]cdr",extensao);
	}

	/**
	 * Metodo....:getNumeroLinhas
	 * Descricao.:Este metodo retorna o numero de linhas de um arquivo texto
	 *            realizando a leitura do mesmo utilizando a API java portanto
	 *            sendo independente da plataforma
	 * @param arquivo	- Nome do arquivo a ser lido
	 * @return long		- Numero de linhas do arquivo
	 * @throws IOException
	 */	
	private long getNumeroLinhas(File arquivo) throws IOException
	{
		FileReader 		fReader 	= new FileReader(arquivo);
		BufferedReader 	buffReader 	= new BufferedReader(fReader);
		long nroLinhas=0;
		while ( buffReader.readLine() != null)
			nroLinhas++;

		buffReader.close();
		fReader.close();
		return nroLinhas;
	}
	
	/**
	 * Metodo....:getNumLinhasArquivoCounter
	 * Descricao.:Retorna o numero de linhas do arquivo de CDRs que esta
	 *            identificado como a 1º linha do arquivo .counter
	 * @param arquivoCDR	- Arquivo de CDR
	 * @return int			- Numero de linhas do arquivo de CDR contido dentro do arquivo .counter
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private long getNumLinhasArquivoCounter(File arqCounter) throws IOException, FileNotFoundException
	{
		// Inicia buffer para leitura do arquivo
		FileReader 		fReader 	= new FileReader(arqCounter);
		BufferedReader 	buffReader 	= new BufferedReader(fReader);
		String linha=null;
		long nroLinhas=0;
		// Executa a leitura do arquivo
		while ( (linha=buffReader.readLine()) != null) 
		{
			// A identificacao do numero de linhas do arquivo de cdrs
			// vem na 1º linha do arquivo .counter, portanto apos a leitura
			// o processo termina
			nroLinhas = Long.parseLong(linha.trim());
			break; 
		}

		// Fecha buffer de leitura do arquivo
		buffReader.close();
		fReader.close();
		return nroLinhas;
	}
}
