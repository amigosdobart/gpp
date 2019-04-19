package com.brt.gpp.comum.produtorConsumidor;

/**
 * @author Gustavo Gusmao
 */
public class ThreadConsumidor implements Runnable
{
    private Consumidor consumidor;
    private ProdutorConsumidorDelegate delegate;
    private boolean erro;
    private String mensagemExcecao;
    
    public ThreadConsumidor(Consumidor consumidor, ProdutorConsumidorDelegate delegate)
    {
        this.consumidor = consumidor;
        this.delegate = delegate;
        erro = false;
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            // Aqui fica o procedimento da thread onde sera sempre dividido em
			// tres etapas. 
			// Start   - onde os procedimentos de inicializacao sao executados
			// Execute - onde fica o codigo de processamento da informacao
			// Finish  - procedimentos de finalizacao
            Produtor produtor = delegate.getProdutor();
            if(produtor instanceof ProcessoBatchProdutor)
            {
                ((ProcessoBatchConsumidor)consumidor).startup((ProcessoBatchProdutor)delegate.getProdutor());
            }
            else
                consumidor.startup(produtor);
			// A thread de consumo fica processando todos os objetos que o
			// produtor produzir ateh que se esgote ou que esta thread seja
			// descontinuada. Em caso de erro no processamento do consumidor
			// entao a thread eh descontinuada e finalizada. Caso o processo
			// deva continuar entao o processo consumidor deve tratar a excecao
			Object obj = null;
			while ((obj = delegate.next()) != null && delegate.estaAtivo())
				consumidor.execute(obj);
        }
        catch(Exception e)
        {
            delegate.handleException(e);
            erro = true;
            mensagemExcecao = e.toString();
        }
        finally
        {
            // Executa a finalizacao de procedimentos do consumidor mesmo em caso de erro
			// Neste passo os recursos utilizados pelo consumidor sao finalizados 
			// ex: Conexao com Banco de dados
			consumidor.finish();
			delegate.notifyTerminoExecucao();
        }
    }

    /**
     * Metodo....: houveErro
     * Descricao.: Indica se houve erro durante a execucao do Consumidor.
     * @return 	boolean 	- Indicador se houve erro ou nao.
     */
    public boolean houveErro()
    {
        return erro;
    }
    
    /**
     * Metodo....: getMensagem
     * Descricao.: Retorna a mensagem contida na excecao em caso de erro durante a execucao do Consumidor.
     * @return	String
     */
    public String getMensagem()
    {
        return(mensagemExcecao);
    }
}
