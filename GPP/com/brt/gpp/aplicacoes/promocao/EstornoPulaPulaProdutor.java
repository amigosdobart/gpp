package com.brt.gpp.aplicacoes.promocao;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.EstornoPulaPulaVO;
import com.brt.gpp.aplicacoes.promocao.OperacoesEstornoPPFactory;
import com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.SelecaoEstornoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.ManipuladorArquivos;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pelo gerenciamento do processo de Estorno de Bonus Pula-Pula por Fraude.
 *
 *  Parametro de entrada 1: IDT_STATUS_PROCESSAMENTO 						[opcional]
 *  Parametro de entrada 2: endereço completo do arquivo de log				[opcional]
 *
 *  Por default:
 *  	a) IDT_STATUS_PROCESSAMENTO = Definicoes.IDT_PROCESSAMENTO_NOT_OK	("N")
 *  	b) arquivo de log = ESTORNO_EXPURGO_RAIZ / ESTORNO_EXPURGO_EFETIVADO / estornoExpurgo$Data$Hora.txt
 *  
 *  Obs: se o parametro 1 valer IDT_PROCESSAMENTO_VALIDACAO ("V") e o segundo paramentro nao for
 *       informado, o sistema gera relatorio no seguinte endereço: 
 *       ESTORNO_EXPURGO_RAIZ / ESTORNO_EXPURGO_PREVIAS / previaEstornoExpurgoDataHora.txt
 *  
 *  ->> As pastas ESTORNO_EXPURGO_RAIZ e ESTORNO_EXPURGO_EFETIVADO estao configuradas no banco de dados
 *   
 *	@author	Daniel Ferreira
 *	@since	15/12/2005
 *	@review_author Magno Batista Corrêa
 *	@review_date 16/05/2006 dd/mm/yyyy
 *
 *  Atualização: 30/01/2007
 *  Por Bernardo Vergne Dias
 */
public class EstornoPulaPulaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	
	/**
	 *  Endereço temporário do arquivo de log 
	 */
	private String urlArquivoLogTemp; 
	
	/**
	 *  Endereço definitivo do arquivo de log.
	 *  O arquivo é movido para esse endereço no
	 *  final do processamento
	 */
	private String urlArquivoLog; 

    /**
     *	Result Set de registros para processamento.
     */
    private SelecaoEstornoPulaPula selecao;
    
    /**
     *	Factory de operacoes do processo de Estorno de Bonus Pula-Pula por Fraude.
     */
    private OperacoesEstornoPPFactory factory;
    
    /**
     *	Numero de registros processados.
     */
    private int numRegistros;
    
    /**
     *	Status de processamento.
     */
    private String status;
    
    /**
     *	Mensagem informativa sobre o resultado do processamento.
     */
    private String mensagem;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public EstornoPulaPulaProdutor(long logId)
	{
		super(logId, Definicoes.CL_PROMOCAO_ESTORNO_PULA_PULA_PROD);
		
		this.selecao		= SelecaoEstornoPulaPula.getInstance();
		this.factory		= null;
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

	//Implementacao de Produtor.
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		
		SimpleDateFormat sdf 			= new SimpleDateFormat("yyyyMMddHHmmss");
		String sufixoDataHora 			= sdf.format(Calendar.getInstance().getTime());
		
		MapConfiguracaoGPP config 		= MapConfiguracaoGPP.getInstance();

		String estornoExpurgoRaiz 		= config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_RAIZ");
		String estornoExpurgoEfetivado 	= config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_EFETIVADO");
		String estornoExpurgoPrevias 	= config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_PREVIAS");
		String estornoExpurgoTemp 		= config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_TEMP");
		String tipoOperacao 			= null;
		
		urlArquivoLogTemp 				= estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoTemp 
									    + java.io.File.separator + "estornoExpurgoTemp" + sufixoDataHora + ".txt";
		
		if (params.length >= 1)
			tipoOperacao = params[0];
		else
			tipoOperacao = Definicoes.IDT_PROCESSAMENTO_NOT_OK;
		
		if (params.length >= 2)
			urlArquivoLog = params[1];
		else
		{
			if (tipoOperacao.equals(Definicoes.IDT_PROCESSAMENTO_VALIDACAO))
			{
				urlArquivoLog = estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoPrevias 
			    				+ java.io.File.separator + "previaEstornoExpurgo" + sufixoDataHora + ".txt";
			}
			else
			{
				urlArquivoLog = estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoEfetivado 
								+ java.io.File.separator + "estornoExpurgo" + sufixoDataHora + ".txt";
			}
		}

		try
		{
			//Obtendo o factory de operacoes.
			this.factory = new OperacoesEstornoPPFactory(tipoOperacao, urlArquivoLogTemp);
		    this.selecao.execute(new String[] {tipoOperacao}, super.logId);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		finally
		{
		    super.log(Definicoes.INFO, "startup", "Fim");
		}
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
	public synchronized Object next() throws Exception
	{
	    EstornoPulaPulaVO result = null;
		super.log(Definicoes.DEBUG, "next", "Inicio");
		
		try
		{
		    Collection listaEstorno = (Collection)this.selecao.next();
		    if(listaEstorno != null)
		    {
		    	OperacoesEstornoPulaPula operacoes = this.factory.newOperacoes(super.logId);
		    	result = new EstornoPulaPulaVO(listaEstorno, operacoes);
		        this.numRegistros++;
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}
		
		return result;
		
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "finish", "Inicio");
		
		//Finalizando o factory de operacoes.
		try
		{
			if (this.factory != null)
				this.factory.finish();
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		
		//Finalizando o Result Set.
		try
		{
		    this.selecao.close();
		    this.mensagem = this.mensagem.concat(String.valueOf(this.numRegistros));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
				
		// Move o arquivo de saída para o endereço definitivo
		moverArquivo(urlArquivoLogTemp, urlArquivoLog);
		
		super.log(Definicoes.INFO, "finish", "Fim");
		
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
	public void handleException()
	{
	}
	
	//Implementacao de ProcessoBatchProdutor.
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_ESTORNO_BONUS_PULA_PULA_FRAUDE;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(String)
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
	public String getDataProcessamento()
	{
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
	public PREPConexao getConexao()
	{
	    return null;
	}
	
	private void removerArquivo(String url)
	{
		File f = new File(url);
		if (f.exists())
		{
			if (f.delete())
				super.log(Definicoes.INFO, "removerArquivo", "Arquivo removido: '"+url+"'");
			else
				super.log(Definicoes.WARN, "removerArquivo", "Nao foi possivel remover arquivo: '"+url+"'");
		}
	}
	
	private void moverArquivo(String origem, String destino)
	{
		try
		{
			removerArquivo(destino);
			if (ManipuladorArquivos.moveArquivo(new File(origem), destino) == 0)
				super.log(Definicoes.INFO, "moverArquivo", "Arquivo '"+origem+"' movido para '"+destino+"'.");
			else
				super.log(Definicoes.WARN, "moverArquivo", "Nao foi possivel mover '"+origem+"' para '"+destino+"'.");
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "moverArquivo", "Erro ao mover '"+origem+"' para '"+destino+"'. Excecao: " + e);
		}
	}
	
}