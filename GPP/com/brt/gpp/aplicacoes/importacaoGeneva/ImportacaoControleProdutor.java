package com.brt.gpp.aplicacoes.importacaoGeneva;

import java.io.File;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Responsável pela seleção dos arquivos enviados pelo Geneva.
 * Esses arquivos contém dados de ativação e desativação feitos no Geneva
 * 
 * @author	Marcelo Alves Araujo
 * @since	05/04/2006
 *
 */
public class ImportacaoControleProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

	private int			numArquivos;
	private String		statusProcesso;
	private PREPConexao	conexaoPrep;
	private File[]		arquivosGeneva;
	
	/**
	 * Construtor
	 * @param aLogId - Id de log do processo
	 */
	public ImportacaoControleProdutor(long aLogId)
	{
		super(aLogId, Definicoes.CL_IMPORTACAO_GENEVA);
		this.statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
		this.numArquivos = 0;
	}

	/**
	 * Retorna o ID do processo batch
	 * @return int - ID do processo batch
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_IMPORTACAO_GENEVA;
	}

	/**
	 * Retorna uma descrição do que ocorreu no processo
	 * @return String - Quantidade de arquivos processados
	 */
	public String getDescricaoProcesso()
	{
		return "Foram processados: " + this.numArquivos + " arquivos.";
	}

	/**
	 * Retorna o status do processo
	 * @return String - Status do processo
	 */
	public String getStatusProcesso()
	{
		return this.statusProcesso;
	}

	/**
	 * Altera o status do processo
	 * @param status - Status do processo
	 */
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}

	/**
	 * Retorna a data de processamento
	 * @return String - Data de processamento
	 */
	public String getDataProcessamento()
	{
		return null;
	}

	/**
	 * Retorna a conexão de banco de dados
	 * @return PREPConexao - Conexão com o banco de dados
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}

	/**
	 * Seleciona a lista de arquivos a processar e inicia uma conexão
	 * @param param - Não utilizado
	 */
	public void startup(String[] params) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio");
		
		this.conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());

		// Busca a referencia para o arquivo de configuracao do sistema GPP
		ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();

		// Acessa o diretório de importação e valida
		File dirGeneva = new File(config.getDirGenevaOrigem());
		if (dirGeneva == null || !dirGeneva.isDirectory())
			throw new GPPInternalErrorException("Diretorio "+dirGeneva.getAbsolutePath()+" nao e um diretorio valido.");

		// Seleciona todos os arquivos no diretório
		arquivosGeneva = selecionaArquivosGeneva(dirGeneva);
		
		super.log(Definicoes.DEBUG, "Produtor.startup", "Arquivos a processar: " + arquivosGeneva.length);
	}

	/**
	 * Passa um arquivo para o consumidor
	 * @return Object - Arquivo a processar
	 */
	public Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.next", "Inicio");
		if (numArquivos < arquivosGeneva.length)
		{
			return arquivosGeneva[numArquivos++];
		}
		super.log(Definicoes.DEBUG, "Produtor.next", "Arquivo: " + numArquivos);
		return null;
	}

	/**
	 * Finaliza a conexão com o banco
	 */
	public void finish() throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim do processo batch: " + this.getIdProcessoBatch());
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}

	/**
	 * Trata as exceções do consumidor
	 */
	public void handleException()
	{
	}
	
	/**
	 * Seleciona a lista de arquivos a processar
	 * @param 	dirImportacao	- Diretorio de importacao
	 * @return	File []			- Lista de arquivos
	 * @throws 	GPPInternalErrorException
	 */
	private File [] selecionaArquivosGeneva(File dirImportacao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "selecionaArquivosGeneva", "Selecao dos arquivos a processar.");
		// Busca a referencia para a classe de mapeamento dos dados de 
		// configuracao do GPP
		MapConfiguracaoGPP config = MapConfiguracaoGPP.getInstancia();

		// Nome padrão dos arquivos
		String nomArqGeneva = config.getMapValorConfiguracaoGPP(Definicoes.IMPORTACAO_GENEVA_PADRAO);
		
		return dirImportacao.listFiles(new GenevaFileFilter(nomArqGeneva));
	}

}
