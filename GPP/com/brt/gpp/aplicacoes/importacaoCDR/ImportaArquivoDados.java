package com.brt.gpp.aplicacoes.importacaoCDR;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoDados;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadoresCDRDAO;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
  * Esta classe realiza a importacao de um arquivo de CDR
  * utilizando o parse informado como parametro
  * 
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				10/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class ImportaArquivoDados extends Aplicacoes
{
	private GerentePoolBancoDados	gerBancoDados			= null;
	private Map						totalizacaoAssinantes	= null;
	private ModificadorInfoCDR		modificadorCDR			= null;
	private Collection				totalizadoresCDR		= null;
	private Collection				listaAuxTotalizados		= null;
	
	/**
	 * Metodo....:ImportaArquivoCDR
	 * Descricao.:Construtor da classe
	 * @param idProcesso - Id do processo
	 */
	public ImportaArquivoDados(long idProcesso)
	{
		super(idProcesso,Definicoes.CL_IMPORTACAO_DADOS_CDR);
		
		// Busca a instancia do gerente de pool de banco de dados
		gerBancoDados = GerentePoolBancoDados.getInstancia(idProcesso);
		totalizacaoAssinantes = new HashMap();
		// Cria uma instancia do modificador CDR que sera utilizado durante
		// toda a vida util da thread que possui esta instancia para importacao
		modificadorCDR = new ModificadorInfoCDR(idProcesso);
		// Busca a lista de classes que sao totalizadores de CDR
		// Essa busca eh feita na inicializacao da classe para
		// que seja evitado a pesquisa constante a cada processamento
		totalizadoresCDR = TotalizadoresCDRDAO.getInstance().getListaTotalizadores();
		// Inicia a colecao que irah ser utilizada para armazenar os objetos totalizados
		// existentes para cada totalizador. Essa colecao eh reutilizada pois eh necessaria
		// somente para gerar o array de objetos totalizados
		listaAuxTotalizados = new ArrayList();
	}
	
	/**
	 * Metodo....:registraHistorico
	 * Descricao.:Este metodo registra o historico de execucao do processo batch
	 *            No caso desse processamento cada arquivo processado sera armazenado em historico
	 *            indicando o nome do arquivo e o numero de linhas importadas com sucesso e erro.
	 * @param idProcessoBatch	- Id do processo batch a ser registrado
	 * @param dataInicial		- Data inicial de execucao
	 * @param status			- Status final da execucao
	 * @param obs				- Mensagem a ser escrita no log da execucao
	 * @throws GPPInternalErrorException
	 */
	private void registraHistorico(int idProcessoBatch,String dataInicial,String status,String obs) throws GPPInternalErrorException
	{
		String dataFinal = GPPData.dataCompletaForamtada();
		String dataProc  = GPPData.dataFormatada();
		super.gravaHistoricoProcessos(idProcessoBatch,dataInicial,dataFinal,status,obs,dataProc);
	}
	
	/**
	 * Metodo....:totalizaAssinante
	 * Descricao.:Este metodo realiza a insercao da informacao totalizada do assinante
	 *            no arquivo sendo processado. Essa 
	 * @param parser	- Parser 
	 * @throws Exception
	 */
	private void totalizaAssinante(ArquivoDados parser) throws Exception
	{
		// Realiza o CAST do arquivo para processamento de arquivo de CDRs
		// caso o arquivo nao seja desse tipo entao nao realiza nenhum procedimento
		if (parser instanceof ArquivoCDR)
		{
			ArquivoCDR cdr = (ArquivoCDR)parser;
			
            // Obtém o msisdn a ser bonificado. (Corrige o valor no caso de cdr sainte)
            String msisdn  = cdr.getSubId();
            
            try
            {
                if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
                {
                    msisdn = cdr.getCallIdFormatado();
                    Long.parseLong(msisdn);
                }
            }
            catch (Exception ex)
            {
                // Ocorre em caso de CallID invalido (nao pode ser convertido em MSISDN)
                // O CDR eh apenas nao totalizado
                return;
            }
           
            // Verifica no cache de assinantes se o acesso sendo processado
			// jah possui informacoes de totalizacao, caso nao existir entao
			// cria um Map para ser utilizado durante toda a importacao desse
			// arquivo.
			Map totalizados = (Map)totalizacaoAssinantes.get(msisdn);
			
            // Para cada totalizador cadastrado, verifica se nas informacoes
			// de objetos totalizados do assinante existe algum valor utilizando
			// como chave o nome da classe do totalizador. Atraves desse nome
			// pega o array de objetos totalizados e envia para o proprio totalizador
			// alterar, caso nao exista entra cria o objeto novo.
			for (Iterator i=totalizadoresCDR.iterator(); i.hasNext();)
			{
				TotalizadorCDR totalizador = (TotalizadorCDR)i.next();
				try
				{
					// So realiza o processo de totalizacao se o totalizador
					// deve totalizar o CDR sendo processado.
					if (totalizador.deveTotalizar(cdr))
					{
						// Caso o assinante ainda nao tenha entradas no map entao jah
						// cria essa entrada vazia para ser utilizada logo adiante
						if (totalizados == null)
							totalizados = new HashMap();
						// O objeto totalizado no Map do assinante eh um array de objetos para
						// conter possiveis valores principalmente quando ha a troca de periodos
						// no qual cada totalizador considera para sua implementacao
						Totalizado[] totalizado = (Totalizado[])totalizados.get(totalizador.getClass().getName());
						// Atualiza as informacoes de objetos totalizados
						// apos processar a somatoria dos objetos existentes
						// e insere novamente na memoria onde estah sendo armazenado
						totalizados.put(totalizador.getClass().getName(), getTotalizados(cdr,totalizador,totalizado));
					}
				}
				catch(Exception e)
				{
					super.log(Definicoes.WARN, "totalizaAssinante",
							"Nao foi possivel totalizacao do assinante "+msisdn+". Totalizador:"+totalizador +" Erro:"+e);
				}
			}
			// Apos a totalizacao de todos os totalizadores entao insere esses valores para o assinante
			totalizacaoAssinantes.put(msisdn,totalizados);
		}
	}

	/**
	 * Metodo....:getTotalizados
	 * Descricao.:Realiza a totalizacao de todos os objetos existentes para o totalizador
	 *            verificando se deve ser utilizado para o CDR atual ou se deve ser criado
	 *            novo objeto
	 * @param cdr			- CDR sendo processado
	 * @param totalizador	- Totalizador a ser utilizado
	 * @param totalizados	- Objetos totalizados atuais para esse totalizador desse assinante
	 * @return Totalizado[] - Retorna os objetos totalizados atualizados apos a totalizacao
	 */
	private Totalizado[] getTotalizados(ArquivoCDR cdr, TotalizadorCDR totalizador, Totalizado totalizado[])
	{
		// Remove qualquer valor que possa existir na colecao
		listaAuxTotalizados.clear();
		// Realiza a iteracao entre cada objeto totalizado existente
		// e verifica se o periodo de totalizacao eh o mesmo do CDR
		// sendo processado, em caso positivo entao totaliza no mesmo
		// objeto
		boolean existePeriodo = false;
		for (int i=0; totalizado!= null && i < totalizado.length; i++)
		{
			if (totalizado[i].possuiMesmoPeriodo(cdr))
			{
				totalizado[i] = totalizador.getTotalizado(cdr,totalizado[i]);
				existePeriodo = true;
			}
			// Insere na lista mesmo se nao for atualizado para ainda
			// existir posteriormente na memoria
			listaAuxTotalizados.add(totalizado[i]);
		}
		
		// Se nao existir nenhum objeto totalizado para o CDR
		// entao cria um novo objeto totalizado para conter
		// esse perido sendo processado e insere junto com os
		// demais
		if (!existePeriodo)
			listaAuxTotalizados.add(totalizador.getTotalizado(cdr,null));
		
		return (Totalizado[])listaAuxTotalizados.toArray(new Totalizado[0]);
	}

	/**
	 * Metodo....:persisteTotalizacao
	 * Descricao.:Realiza a persistencia dos objetos totalizados em memoria
	 * 
	 * OBS: Este metodo eh sincronizado para evitar que duas ou mais threads de importacao de CDRs
	 *      entrem no erro de deadlock ao inserir os dados no banco de dados
	 *
	 */
	public static synchronized void persisteTotalizacao(Map totalizacaoAssinantes, Collection totalizadoresCDR, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		try
		{
			// Realiza a iteracao entre todos os assinantes em memoria para
			// comecar o processo de persistencia
			for (Iterator i=totalizacaoAssinantes.values().iterator(); i.hasNext();)
			{
				Map totalizados = (Map)i.next();
				if (totalizados != null)
				{
					// Para cada totalizador existente pega o valor dos objetos
					// existentes do assinante para o totalizador e entao realiza
					// a persistencia dos objetos lah existentes
					for (Iterator j=totalizadoresCDR.iterator(); j.hasNext();)
					{
						TotalizadorCDR totalizador = (TotalizadorCDR)j.next();
						// Busca os objetos totalizados desse totalizador
						Totalizado[] totalizado = (Totalizado[])totalizados.get(totalizador.getClass().getName());
						// Para cada objeto realiza a persistencia
						if (totalizado != null)
							for (int k=0; k < totalizado.length; k++)
								totalizador.persisteTotalizado(totalizado[k],conexaoPrep);
					}
				}
			}
		}
		finally
		{
			// Apos a persistencia de todos os valores entao limpa o objeto
			// que armazena as informacoes de assinantes em memoria para
			// o proximo arquivo ser processado
			totalizacaoAssinantes.clear();
		}
	}

	/**
	 * Metodo....:imporaArquivo
	 * Descricao.:Processa a importacao de um arquivo de CDR
	 * @param arquivo	- Arquivo a ser processado
	 * @param parser	- Parser de CDR utilizado
	 * @throws GPPInternalErrorException
	 */
	public void importaArquivo(File arquivo, ArquivoDados parser) throws GPPInternalErrorException
	{
		String dataInicial 	   = GPPData.dataCompletaForamtada();
		String msgHistorico    = "CDR:"+arquivo;
		String status		   = Definicoes.TIPO_OPER_SUCESSO;
		FileWriter arqRejeicao = null; 
		PREPConexao conexaoPrep=null;
		try
		{
			// Busca uma referencia para conexao de banco de dados que sera utilizada
			// para todo o processo de importacao deste arquivo desligando o autoCommit
			// para efetivacao somente apos todo o processamento do arquivo
			conexaoPrep = gerBancoDados.getConexaoPREP(super.getIdLog());
			conexaoPrep.setAutoCommit(false);

			// Inicializa instancias para tratamento da leitura do arquivo
			FileReader		fReader		= new FileReader(arquivo);
			BufferedReader	buffReader	= new BufferedReader(fReader);
			
			// Inicia a leitura do arquivo linha por linha, sendo o processamento
			// executado para a linha lida efetivando ou nao o processamento somente
			// no final do arquivo
			// Um contador de numero de linhas que foram importadas com sucesso ou nao
			// sao atualizados no procedimento abaixo para posteriormente serem registrados
			int numLinhasImportadas=0;
			int numLinhasRejeitadas=0;
			String linha=null;
			while ( (linha=buffReader.readLine()) != null )
			{
				try
				{
					// Utiliza o parser para realizar o parse da linha em processamento
					parser.parse(linha);
					// Realiza as modificacoes necessarias com o CDR caso este parser
					// seja de importacao de CDRs
					if (parser instanceof ArquivoCDR)
						modificadorCDR.modificaCDR((ArquivoCDR)parser,totalizadoresCDR,conexaoPrep);

					// Realiza o processamento da linha atual do arquivo contendo os campos ja definidos
					processaLinha(conexaoPrep,parser);
					numLinhasImportadas++;
					// Apos processar a linha, em caso de sucesso entao armazena na memoria
					// as informacoes totalizadas do assinante para qualquer totalizador existente
					totalizaAssinante(parser);
				}
				catch(Exception e)
				{
					// Caso o ponteiro para o arquivo de rejeicao esteja nulo entao nesse momento cria o arquivo
					// para armazenar a primeira linha rejeitada
					if (arqRejeicao == null)
						arqRejeicao = criaArquivoRejeicao(arquivo.getName());

					// Incrementa o numero de linhas rejeitadas e armazena a linha e o erro no arquivo
					numLinhasRejeitadas++;
					arqRejeicao.write(linha+" Erro:"+e+"\n");
					//super.log(Definicoes.ERRO,"importaArquivo","Erro ao importar linha do arquivo de CDR:"+linha+" Erro:"+e);
				}
			}
			
			// Fecha buffer de leitura do arquivo antes de libera-lo
			buffReader.close();
			fReader.close();

			// Fecha o arquivo de dados rejeitados
			if (arqRejeicao != null)
				arqRejeicao.close();

			// Atualiza a mensagem que sera escrita no LOG de execucao do processo			
			msgHistorico += " Numero de linhas importadas:"+numLinhasImportadas;		
			msgHistorico += " Numero de linhas rejeitadas:"+numLinhasRejeitadas;

			// Persiste em banco de dados todas as atualizacoes existentes em
			// memoria antes de realmente confirmar todas as inclusoes e processamentos
			// realizados pela importacao desse arquivo de CDR
			ImportaArquivoDados.persisteTotalizacao(totalizacaoAssinantes,totalizadoresCDR,conexaoPrep);
			
			// Independente dos possiveis erros encontrados ao processar
			// as linhas, efetiva-se a transacao de todos os registros
			// Em caso de erro do arquivo entao realiza-se o rollback
			conexaoPrep.commit();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"importaArquivo","Erro ao processar arquivo "+arquivo+" Erro: "+ super.log.traceError(e));
			status = Definicoes.TIPO_OPER_ERRO;
			try
			{
				conexaoPrep.rollback();
			}
			catch(java.sql.SQLException se)
			{
				super.log(Definicoes.DEBUG,"importaArquivo","Erro ao executar o ROLLBACK: "+se);
			}
		}
		finally
		{
			try
			{
				conexaoPrep.setAutoCommit(true);
			}
			catch(java.sql.SQLException e)
			{
				super.log(Definicoes.DEBUG,"importaArquivo","Erro ao definir a conexao como AUTOCOMMIT: "+e);
			}
			// Libera a conexao para o gerente do pool de banco de dados
			gerBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}

		//Registra a execucao do processo batch
		registraHistorico(parser.getIdProcessoBatch(),dataInicial,status,msgHistorico);
	}

	/**
	 * Metodo....:processaLinha
	 * Descricao.:Realiza a importacao de uma linha do arquivo de CDR
	 * @param linha			- Linha a ser processada
	 * @param conexaoPrep	- Conexao com o banco de dados a ser utilizada
	 * @param parserCDR		- Parser do CDR a ser utilizado
	 * @throws GPPInternalErrorException
	 */	
	private void processaLinha(PREPConexao conexaoPrep, ArquivoDados parser) throws GPPInternalErrorException
	{
		// Busca os comandos e parametros dos comandos que deverao ser executados 
		// na base de dados
		String comandos[] 	= parser.getComandosSQLInsert(conexaoPrep);
		Object parametros[][] = parser.getParametrosSQLInsert(conexaoPrep);
		
		// Para cada comando encontrado existe um objeto[] parametro correspondente
		// com o mesmo indice do vetor
		for (int i=0; i < comandos.length; i++)
		{
			// Executa o comando na base de dados e caso este tenha algum erro entao
			// o erro e registrado, porem o processamento continua observando que
			// o metodo acima realiza esse processo. Se o comando for nulo, significa
			// que este deve ser ignorado mas observando os proximos elmentos
			if (comandos[i] != null)
				conexaoPrep.executaPreparedUpdate(comandos[i],parametros[i],super.getIdLog()); 
		}
	}
	
	/**
	 * Metodo....:criaArquivoRejeicao
	 * Descricao.:Cria o arquivo de rejeicao de dados
	 * @param nomeArquivo	- Nome do arquivo origem
	 * @return FileWriter	- Ponteiro para escrita do arquivo de rejeicao
	 * @throws IOException
	 */
	private FileWriter criaArquivoRejeicao(String nomeArquivo) throws IOException
	{
		// Busca referencia para o arquivo de configuracao do sistema
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		// Troca a extensao do arquivo para identificacao do arquivo rejeitado
		String nomeArquivoRejeicao = nomeArquivo.replaceAll("[.]cdr",".bad");
	           nomeArquivoRejeicao = nomeArquivoRejeicao.replaceAll("[.]csv",".bad");
		
		// Efetivamente cria o arquivo
		File arquivoRejeicao = new File(arqConf.getDirRejeicaoCdr()+System.getProperty("file.separator")+nomeArquivoRejeicao);
		return new FileWriter(arquivoRejeicao);
	}
}
