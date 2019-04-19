package com.brt.gpp.comum.produtorConsumidor;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * @author Luciano Vilela
 * Adaptacao Joao Carlos
 *
 */
public class ProdutorConsumidorDelegate
{
    protected 	long 		contGeral = 0;
    private 	boolean 	continua = true;
    private 	Produtor 	produtor;
    private 	long		tempoEspera = 1;
    private 	int			qtdConsumidor = 0;
    private		int 		threadsFinalizadas = 0;
    
    private ThreadGroup threadGroup;
    private  ThreadConsumidor[] listaThreads;
    
    /**
     * Metodo....:ProdutorConsumidorDelegate
     * Descricao.:Construtor do Delegate
     * @param tempoEspera - Tempo de espera no gerenciamento das threads em segundos
     */
    public ProdutorConsumidorDelegate(int tempoEspera)
    {
    	// Define o tempo de espera em segundos
    	this.tempoEspera = tempoEspera;
    	this.threadGroup = new ThreadGroup("Runners");
    }

    public ProdutorConsumidorDelegate()
    {
    	this.threadGroup = new ThreadGroup("Runners");
    }

    /**
     * Metodo....:setProdutor
     * Descricao.:Define o produtor a ser utilizado nesse processo
     * @param produtor - Produtor a ser utilizado
     */
    protected void setProdutor(Produtor produtor)
    {
    	this.produtor = produtor;
    }

    /**
     * Metodo....:getProdutor
     * Descricao.:Retorna o produtor associado a este processamento
     * @return Produtor
     */
    protected Produtor getProdutor()
    {
    	return produtor;
    }

    /**
     * Metodo....:next
     * Descricao.:Este metodo ira retornar o proximo objeto a ser processado utilizando o produtor
     * @return Object - ValueObject a ser processado
     */
    protected synchronized Object next() throws Exception
    {
		contGeral++;
		return produtor.next();
    }

    /**
     * Metodo....:factoryThread
     * Descricao.:Este metodo eh responsavel pela criacao da thread consumidor utilizando
     *            a classe que implementa a interface passada como parametro
     * @param tg				- Thread group onde a thread sera gerenciada
     * @param consumidorClass	- Classe que sera utilizada como consumidora
     * @param delegate
     * @return ThreadConsumidor - Thread para processamento do consumidor
     */
    protected ThreadConsumidor factoryThread(final Class consumidorClass, ProdutorConsumidorDelegate delegate) throws Exception
    {
        // Instancia a classe que sera utilizada como consumidora
		// Caso a classe passada como parametro nao implemente a interface
		// Consumidor entao um erro sera retornado
        Consumidor consumidor = (Consumidor) consumidorClass.newInstance();
        return new ThreadConsumidor(consumidor, delegate);
		//Thread th = new Thread(threadGroup, new ThreadConsumidor(consumidor, getProdutor(), delegate));
		//return th;
    }

    /**
     * Metodo....:handleException
     * Descricao.:Lida com a excecao ocorrida nos consumidores
     * @param e - Excecao a ser tratada
     */
    protected void handleException(Exception e)// throws Exception
    {
    	// Define o flag como false para que as outras threads 
    	// sejam terminadas e retorna a excecao para seu chamador
    	continua = false;
    }

    /**
     * Metodo....:iniciaProdutor
     * Descricao.:Realiza os procedimentos de inicializacao do produtor
     * @param produtor	- Produtor a ser utilizado
     * @param params	- Parametros a serem repassados para o produtor
     * @throws Exception
     */
    protected void iniciaProdutor(Produtor produtor, String params[]) throws Exception
    {
		// Define qual serah o produtor a ser executado e inicializa. Nesse ponto
		// os dados de insumo do processo jah serao lidos para posterior retorno
		setProdutor(produtor);
		produtor.startup(params);
    }

    /**
     * Metodo....:inicializaConsumidores
     * Descricao.:Realiza a criacao das threads consumidoras
     * @param qtdConsumidor	- Qtde de threads consumidoras
     * @param consumidor	- Qual classe consumidor utilizar
     * @param delegate		- Delegate responsavel por invocar este metodo
     * @throws Exception
     */
    protected void inicializaConsumidores(int qtdConsumidor, Class consumidor, ProdutorConsumidorDelegate delegate) throws Exception
    {
    	this.qtdConsumidor = qtdConsumidor;
        listaThreads = new ThreadConsumidor[qtdConsumidor];
        // Cria as threads consumidoras e as inicializa
        for(int i=0; i< qtdConsumidor; i++)
        {
            listaThreads[i] = factoryThread(consumidor, delegate);
			Thread th = new Thread(threadGroup, listaThreads[i]);
			th.start();
        }
    }
    
    /**
     * Metodo....:inicializaConsumidores
     * Descricao.:Realiza a criacao das threads consumidoras
     * @param qtdConsumidor	- Qtde de threads consumidoras
     * @param consumidor	- Qual classe consumidor utilizar
     * @throws Exception
     */
    protected void inicializaConsumidores(int qtdConsumidor, Class consumidor) throws Exception
    {
    	this.qtdConsumidor = qtdConsumidor;
        listaThreads = new ThreadConsumidor[qtdConsumidor];
        // Cria as threads consumidoras e as inicializa
        for(int i = 0; i < qtdConsumidor; i++)
        {
            listaThreads[i] = factoryThread(consumidor, this);
			Thread th = new Thread(threadGroup, listaThreads[i]);
			th.start();
        }
    }
    
    /**
     * Metodo....:esperaTerminoConsumidores
     * Descricao.:Realiza a espera do termino de todas as threads consumidoras
     *
     */
    protected void esperaTerminoConsumidores() throws Exception
    {
		// Verifica o numero de threads consumidoras ativas existem
		// O processo de execucao so termina quando todas as threads
		// estiverem terminado. Portanto se esse numero ainda nao 
		// foi atingido entao o processo fica em espera para fazer
		// nova verificacao
        // while(threadGroup.activeCount() > 0)
    	while(threadsFinalizadas < qtdConsumidor)
        {
            try
			{
            	Thread.sleep(tempoEspera*1000);
            }
        	catch(Exception e)
        	{
        	}
        }
        for(int i = 0; i < listaThreads.length; i++)
        {
            if(listaThreads[i].houveErro())
                throw new Exception(listaThreads[i].getMensagem());
        }
    }

    /**
     * Metodo....:finalizaProdutor
     * Descricao.:Realiza os procedimentos de finalizacao do produtor
     * @param produtor	- Produtor a ser utilizado
     * @throws Exception
     */
    protected void finalizaProdutor(Produtor produtor) throws Exception
    {
    	produtor.finish();
    }

    /**
     * Metodo....:exec
     * Descricao.:Este metodo realiza a execucao dos procedimentos de Produtor e Consumidor
     * @param qtdConsumidor	- Qtde de threads consumidoras que deverao ser criados
     * @param produtor		- Objeto instanciado para agir como produtor de dados
     * @param consumidor    - Classe consumidora que sera utilizada na fabricacao de threads
     * @param params[]		- Array de parametros para ser repassado para o produtor
     * @return long			- Numero de registros processados 
     * @throws GPPInternalErrorException
     */
    public long exec(int qtdConsumidor, Produtor produtor, String[] params, Class consumidor) throws Exception
	{
    	// Inicia o produtor
    	iniciaProdutor(produtor,params);
		// Inicia os consumidores
    	inicializaConsumidores(qtdConsumidor,consumidor);
    	// Fica em espera do termino dos consumidores
    	esperaTerminoConsumidores();
    	finalizaProdutor(produtor);

        return contGeral;
    }
    
    /**
     * Metodo....:finalizaProcessamento
     * Descricao.:Marca um flag para que o processamento seja interrompido
     * OBS: Ao termino do metodo nao significa que todas as threads jah foram finalizadas. O flag
     *      indica que o no proximo objeto a ser processado a thread ira ser terminada, portanto
     *      o que jah estah em processamento continua em processamento
     */
    public void finalizaProcessamento()
    {
    	this.continua = false;
    }
    
    /**
     * Metodo....: estaAtivo
     * Descricao.: Indica se o processamento dos consumidores deve continuar.
     * @return boolean
     */
    public boolean estaAtivo()
    {
       return(continua); 
    }
    
    /**
     * 
     */
    public synchronized void notifyTerminoExecucao()
    {
    	threadsFinalizadas++;
    }
}
