//Definicao do Pacote
package com.brt.gpp.aplicacoes.importacaoAssinantes;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
//import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.aplicacoes.importacaoCDR.*;

//Arquivos de Import Internos
import com.brt.gpp.comum.*;
//import com.brt.gpp.comum.arquivoConfiguracaoGPP.*;
import com.brt.gpp.comum.mapeamentos.*;
import java.io.*;

/**
  *
  * Este arquivo refere-se a classe ImportaAssinantes, responsavel 
  * pela implementacao da logica de importacao de assinantes
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				17/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
 public final class ImportaAssinantes extends Aplicacoes
 {
//	private GerentePoolBancoDados 	gerenteBancoDados 	= null; // Gerente de conexoes Banco Dados
//	private ArquivoConfiguracaoGPP  arqConfiguracao     = null; // Arquivo de configuração do GPP
	private MapConfiguracaoGPP		mapConfig			= null; // Mapeamanto da Tabela TBL_GER_CONFIG_GPP
	
	/**
	 * Metodo...: ImportaAssinantes
	 * Descricao: Construtor 
	 * @param	aLogId	- logId (Identificador do Processo para Log)
	 * @return									
	 */
	 public ImportaAssinantes(long aLogId)
	{
		super(aLogId, Definicoes.CL_IMPORTACAO_ASSINANTES);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
//		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(aLogId);
//		this.arqConfiguracao   = ArquivoConfiguracaoGPP.getInstance();
		
		// Instanciando mapeamento da TBL_GER_CONFIG_GPP
		try
		{
			//Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				super.log(Definicoes.WARN, "ImportaAssinantes", "Problemas com Mapeamento das Configurações GPP");		
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "ImportaAssinantes", "Problemas com Mapeamento das Configurações GPP");
		}
	}

	/**
	 * Metodo...: getFiles
	 * Descricao: Este metodo retorna um array de arquivos que possuem como parte do nome
	 *  o token utilizado como parametro em aNomeArq. 
	 * 	O parametro aDiretorio é o nome do diretório onde residem os arquivos de assinantes 
	 *  a serem internalizados na base de dados
	 * @param  aDiretorio 	- Diretório onde os arquivos residem
	 * @param  aNomeArq 	- Parte constante do nome dos arquivos referentes a assinantes
	 * @return File[] 		- Array de arquivos que serão processados como arquivos de assinantes
	 * @throws GPPInternalErrorException
	 */
	private File[] getFiles(String aDiretorio, String aNomeArq) throws GPPInternalErrorException
	{
//		AssinanteFileFilter filter = new AssinanteFileFilter(aNomeArq);

		File f = new File(aDiretorio);
		if (!f.isDirectory())
			throw new GPPInternalErrorException (aDiretorio + " não é um diretório válido.");
					
		File arquivos[] = f.listFiles(new AssinanteFileFilter(aNomeArq));
		return arquivos;
	}

	/**
	 * Metodo...: moveArquivoParaHistorico
	 * Descricao: Este metodo tem como objetivo mover os arquivos processados, para um diretório 
	 *            histórico para posterior conferencia, sendo que o programa tem sempre como objetivo 
	 *            processar os arquivos contidos no diretório este passo é de fundamental importância 
	 *            para evitar que um mesmo arquivo seja processado mais de  uma vez.
	 * @param 	f	- Arquivo de importação que será importado pelo processo e será
	 * 				  posteriormente transferido para o diretório de histórico
	 * @return	int - Indicando se o método conseguiu mover os arquivos para o diretório
	 * 				  de histórico (0-verdadeiro, 1-falso)
	 * @throws GPPInternalErrorException
	 */
	private int moveArquivoParaHistorico(File f) throws GPPInternalErrorException
	{
		int retorno = 0;
		File fDest = new File(mapConfig.getMapValorConfiguracaoGPP(Definicoes.IMPASS_DIR_HISTORICO) +
							  System.getProperty("file.separator") +  
							  f.getName());

		super.log(Definicoes.DEBUG, "moveArquivoParaHistorico", "Movendo Arquivo : " + f + " para o diretório historico: " + fDest);
		if (!f.renameTo(fDest))
		{
			super.log(Definicoes.ERRO, "moveArquivoParaHistorico", "Não foi possível mover o arquivo "+ f);
			retorno = 1;
		}
		return retorno;
	}
	
	/**
	 * Metodo...: processaImportacaoAssinantes
	 * Descricao: Este metodo realiza a importacao dos arquivos de assinantes
	 * @param
	 * @return short indica se o processamento executou com sucesso ou não.
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	public short processaImportacaoAssinantes() throws GPPInternalErrorException,GPPTecnomenException
	{
		short retorno = Definicoes.RET_OPERACAO_OK;	
		super.log(Definicoes.INFO, "processaImportacaoAssinantes", "Inicio");
		
		// Cria referencia para a classe de importacao de arquivos
		ImportaArquivoDados impArquivo = new ImportaArquivoDados(super.getIdLog());
		
		// Cria referencia para o parser do arquivo de assinantes
		ArquivoAssinante arqAssinante = new ArquivoAssinante();

		// Busca os arquivos de assinantes a serem processados		
		File arqAss[] = getFiles(mapConfig.getMapValorConfiguracaoGPP(Definicoes.IMPASS_DIR_IMPORTACAO),
								 mapConfig.getMapValorConfiguracaoGPP(Definicoes.IMPASS_NOM_ARQUIVOS));

		// Para cada arquivo realiza a importacao utilizando a classe de importacao
		// de dados e a referencia para realizar o parse do arquivo
		if (arqAss.length != 0)
			for (int i=0; i < arqAss.length; i++)
			{
				super.log(Definicoes.DEBUG, "ImportaAssinantes", "Processamento de importação do arquivo: " + arqAss[i].getName());
				
				// Define a data de importacao a ser utilizada para todos os registros do arquivo
				// Esta data e encontrada no nome do arquivo entre os caracters ".". 
				// Ex: Subscriber.10-10-2004.csv logo a data e 10-10-2004
				int posIni = arqAss[i].getName().indexOf(".");
				int posFim = arqAss[i].getName().indexOf(".",posIni+1);
				arqAssinante.setDataImportacao( arqAss[i].getName().substring(posIni+1,posFim), "dd-MM-yyyy" );

				// Realiza a importacao do Arquivo. Neste metodo o arquivo
				// e lido feito o parse e inserido na tabela correspondente
				// O registro do processo e feito neste metodo tambem nao sendo
				// realizado portanto na classe de importacao de assinantes
				impArquivo.importaArquivo(arqAss[i],arqAssinante);
				
				// Se nao houver erro ao importar o arquivo entao
				// move-o para o diretorio de historico para salvaguardar
				// a informacao original
				moveArquivoParaHistorico(arqAss[i]);
				retorno = 0;
			}

		super.log(Definicoes.INFO, "processaImportacaoAssinantes", "Fim");
		return retorno;
	}
		
	/**  
	 * Metodo...: processaImportacao
	 * Descricao: Este método é o controle principal do programa onde tem por finalidade gerenciar
	 *  		  todo o fluxo de chamadas para o processamento de importação dos arquivos de assinantes.
	 * @param
	 * @return	short RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */	
	public short processaImportacao() throws GPPInternalErrorException,GPPTecnomenException
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		if ((retorno=processaImportacaoAssinantes()) != 0)
			return retorno;

		return retorno;
	}
}
