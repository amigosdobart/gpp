package com.brt.gpp.comum.produtorConsumidor;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * @author Luciano Vilela
 * Adaptacao Joao Carlos
 *
 */
public class ProdutorConsumidorGPP extends ProdutorConsumidorDelegate
{
	private GerentePoolLog 	gerLog;
	private long			idProcessamento;
	
	/**
	 * Metodo....:ProdutorConsumidorGPP
	 * Descricao.:Construtor da classe
	 * @param idProcessamento - Id do processo que esta utilizando o ProdutorConsumidor
	 */
	public ProdutorConsumidorGPP(long idProcessamento)
	{
		gerLog 					= GerentePoolLog.getInstancia(this.getClass());
		this.idProcessamento 	= idProcessamento;
	}

	/**
	 * Metodo....:ProdutorConsumidorGPP
	 * Descricao.:Construtor da classe
	 * @param idProcessamento 	- Id do processo que esta utilizando o ProdutorConsumidor
	 * @param tempoEspera		- Tempo de espera entre a verificacao de threads em segundos
	 */
	public ProdutorConsumidorGPP(long idProcessamento, int tempoEspera)
	{
		super(tempoEspera);
		gerLog 					= GerentePoolLog.getInstancia(this.getClass());
		this.idProcessamento 	= idProcessamento;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProdutorConsumidorDelegate#handleException(java.lang.Exception)
	 */
	protected void handleException(Exception e)
	{
		// Em caso de erro nesse ponto entao as threads consumidoras retornaram
		// erro portanto as demais threads devem ser interrompidas paralizando
		// o flag de execucao (feito na superclasse). No caso de processos batch
		// o GPP alem de registrar o erro no log tambem marca o status no produtor
		// para indicar que nao houve sucesso
		super.handleException(e);
		gerLog.log(idProcessamento,Definicoes.ERRO,"ProdutorConsumidorGPP","consumidor","Erro na execucao das threads consumidoras. Erro:"+e);
		((ProcessoBatchProdutor)getProdutor()).setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
	}

	/**
	 * Metodo....:executaProcesso
	 * Descricao.:Este metodo executa o processo batch utilizando o esquema de Produtor Consumidor
	 *            e gerenciando a gravacao de historico no final do processo
	 * @param qtdConsumidor	- Numero de threads a serem utilizadas no consumidor
	 * @param produtor		- Qual a instancia da classe produtor a ser utilizado (Tem que ser um ProcessoBatchProdutor)
	 * @param params		- Parametros a serem passados para o produtor
	 * @param consumidor	- Qual a classe que sera utilizada como consumidor
	 * @throws Exception
	 */
	public long exec (int qtdConsumidor, ProcessoBatchProdutor produtor, String[] params, Class consumidor) throws Exception
	{
		// Define a data inicial de processamento
		String dataInicialProc = GPPData.dataCompletaForamtada();
		try
		{
			// Define qual serah o produtor a ser executado e inicializa. Nesse ponto
			// os dados de insumo do processo jah serao lidos para posterior retorno
			// Em caso de erro um tratamento para essa parte eh utilizado
	    	iniciaProdutor(produtor,params);
			// Realiza o processamento da fabrica de threads
	        // Cria as threads consumidoras e as inicializa
			// Em caso de erro em alguma thread que seja necessario
			// abortar o processo entao o tratamento de erros realiza
			// esse tratamento
	    	inicializaConsumidores(qtdConsumidor,consumidor);
	    	// O processo agora fica em espera ateh o fim de todas
	    	// as threads de consumo. Apos o termino entao os procedimentos
	    	// de finish do produtor serao executados
	    	esperaTerminoConsumidores();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Erro na execucao do processo batch. Erro:"+e);
		}
		finally
		{
	        // Realiza os procedimentos de finalizacao do processo produtor
			// independente se tenha acontecido algum erro
	    	produtor.finish();
	    	
			// Define a data final apos todo o processamento
			String dataFinalProc = GPPData.dataCompletaForamtada();
			// Cria uma instancia da classe Aplicacoes simplesmente
			// soh para registrar o historico de execucao do processamento
			// sendo as informacoes do processo fornecidas pelo proprio
			// produtor somente as datas sao controladas aqui
			Aplicacoes ap = new Aplicacoes(idProcessamento,"ProdutorConsumidorGPP");
			ap.gravaHistoricoProcessos(produtor.getIdProcessoBatch()
					                  ,dataInicialProc
									  ,dataFinalProc
									  ,produtor.getStatusProcesso()
									  ,produtor.getDescricaoProcesso()
									  //,GPPData.dataFormatada()
									  ,produtor.getDataProcessamento()
									  );
		}
		return contGeral;
	}
}
