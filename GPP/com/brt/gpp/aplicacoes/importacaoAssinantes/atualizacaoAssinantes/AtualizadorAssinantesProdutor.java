package com.brt.gpp.aplicacoes.importacaoAssinantes.atualizacaoAssinantes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapCodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pela atualizacao da tabela TBL_APR_ASSINANTE
 *  de acordo com a tabela TTBL_APR_SALDO_TECNOMEN.
 *  
 *  Parametro de entrada 0: data de importacao, formato dd/mm/yyyy								[opcional]
 *
 *	@author	Bernardo Vergne Dias
 *	Data:	27/08/2007
 */
public class AtualizadorAssinantesProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private int 		numSucessos;		//	CNs processadas com sucesso.
	private int 		numErros;			//	CNs processadas com sucesso.
    private String 		status;				//	Status de processamento.
    private String 		mensagem;			//	Mensagem sobre o resultado do processamento.
    private Date 		dataImportacao;		//  Data de referencia da importacao de assinante
    private LinkedList	prefixos;			//  Prefixos de MSISDN a serem processados pelo consumidor

    /**
     *	Construtor da classe.
     *
     *	@param idLog Identificador de LOG.
     */
	public AtualizadorAssinantesProdutor(long logId)
	{
		super(logId, Definicoes.CL_ATUALIZADOR_ASSINANTES_PRODUTOR);		
		this.numSucessos			= 0;
		this.numErros				= 0;
		this.status					= Definicoes.PROCESSO_SUCESSO;
		this.mensagem				= "";
		this.dataImportacao			= null;		
		this.prefixos				= new LinkedList();
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
     */
	public void startup(String[] params) throws Exception
	{
		try
		{
			/**
			 * Gera os prefixos de CN a serem processados por cada consumidor.
			 */
			
			if (MapCodigoNacional.getInstance() == null)
				throw new Exception("Mapeamento de Codigos Nacionais retornou null");
			
			CodigoNacional cn = null;
			
			for (Iterator it = MapCodigoNacional.getInstance().getAll().iterator(); it.hasNext(); )
			{
				cn = (CodigoNacional)it.next();
				if (cn.getIndRegiaoBrt() != null && cn.getIndRegiaoBrt().intValue() == 1)
				{
					prefixos.add("55" + cn.getIdtCodigoNacional() + "%");
				}
			}
			
			/**
			 * Obtém a data de importação a ser considerada nesse processo batch.
			 */
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			dataImportacao = new java.sql.Date(cal.getTime().getTime());
			
			if (params.length >= 1)
			{
				dataImportacao = new java.sql.Date(sdf.parse(params[0]).getTime());
			}
			
			mensagem = "Atualizacao de assinantes em andamento.";
		}
		catch(Exception e)
		{
		    mensagem = "Erro ao iniciar o processo. Excecao: " + e + ".";
		    this.status = Definicoes.PROCESSO_ERRO;
		    super.log(Definicoes.ERRO, "startup", mensagem);
		    throw e;
		}
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
	public synchronized Object next() throws Exception
	{
		List list = new ArrayList();
		
		if (prefixos.size() > 0)
		{
			list.add(dataImportacao);
			list.add(prefixos.getFirst());
			prefixos.removeFirst();
			return list;
		}
		else
		{
			return null;
		}

	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
		mensagem = "Concluido.";
		super.log(Definicoes.INFO, "finish", getDescricaoProcesso());
	}
		
	/**
	 * Incrementa o contador de assinantes processados com sucesso
	 *
	 */
	public synchronized void incrementaNumSucessos()
	{
		numSucessos++;
	}
	
	/**
	 * Retorna o contador de sucessos
	 */
	public synchronized int getNumSucessos()
	{
		return numSucessos;
	}
	
	/**
	 * Incrementa o contador de assinantes processados com erro
	 *
	 */
	public synchronized void incrementaNumErros()
	{
		numErros++;
	}
	
	/**
	 * Retorna o contador de erros
	 */
	public synchronized int getNumErros()
	{
		return numErros;
	}
	
	/**
    *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
    */
	public void handleException(){ }
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_ATUALIZADOR_ASSINANTES;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem + " [SUCESSOS: " + numSucessos + ", ERROS: " + numErros + "]";
	}
		
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(String)
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
	public String getDataProcessamento()
	{
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
	public PREPConexao getConexao()
	{
	    return null;
	}
	
}
