package com.brt.gpp.aplicacoes.promocao;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.GerentePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela execucao do processo de concessao de bonus Pula-Pula.
 *
 *	@version	1.0		04/10/2005		Primeira versao.
 *	@author		Daniel Ferreira
 *
 *	@version	2.0		26/09/2007		Adaptacao para o modelo Produtor-Consumidor.
 *	@author		Daniel Ferreira
 */
public class ThreadPulaPula extends ControlePulaPula implements ProcessoBatchConsumidor
{
	
    /**
     *	Produtor dos registros para processamento da concessao de bonus Pula-Pula.
     */
	private GerentePulaPula gerente;
	
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
    /**
     *	Tipo de execucao do processo de concessao do Pula-Pula.
     */
    private String tipoExecucao;
    
    /**
     *	Data de referencia da execucao.
     */
    private Date dataReferencia;
    
    /**
     *	Construtor da classe.
     */
	public ThreadPulaPula()
	{
		super(GerentePoolLog.getInstancia(GerentePulaPula.class).getIdProcesso(Definicoes.CL_PROMOCAO_GERENTE_PULA_PULA), 
			  Definicoes.CL_PROMOCAO_THREAD_PULA_PULA);
		
		this.gerente		= null;
		this.conexaoPrep	= null;
		this.tipoExecucao	= null;
		this.dataReferencia	= null;
	}

	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
	}

	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		this.startup();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(Object)
	 */	
	public void execute(Object obj) throws Exception
	{
		short result = -1;
		
		try
		{
		    //Executando a concessao.
		    result = super.executaConcessao(this.tipoExecucao, 
		    							    (String)obj, 
											this.dataReferencia,
											Definicoes.GPP_OPERADOR,
											Definicoes.CTRL_PROMOCAO_MOTIVO_CONCESSAO_PULA_PULA,
											this.conexaoPrep);
		    
		    //Notificando o resultado.
		    if(result == Definicoes.RET_OPERACAO_OK)
		    	this.gerente.notificarSucesso();
		    else
		    	this.gerente.notificarErro();
		    
		    super.log(Definicoes.DEBUG, "ThreadPulaPula.execute", "MSISDN: " + obj + " - Retorno: " + result);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "ThreadPulaPula.execute", "MSISDN: " + obj + " - Excecao: " + e);
			this.gerente.notificarExcecao();
		}
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.getIdLog());
	}
	
    /**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.startup((Produtor)produtor);
		
		this.gerente		= (GerentePulaPula)produtor;
		this.tipoExecucao	= this.gerente.getTipoExecucao();
		this.dataReferencia	= this.gerente.getDataReferencia();
	}
	
}