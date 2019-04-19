package com.brt.gpp.aplicacoes.promocao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pelo gerenciamento das threads de execucao do processo de concessao de bonus Pula-Pula.
 *
 *	@version	1.0		04/10/2005		Primeira versao.
 *	@author		Daniel Ferreira
 *
 *	@version	2.0		26/09/2007		Adaptacao para o modelo Produtor-Consumidor.
 *	@author		Daniel Ferreira
 */
public class GerentePulaPula extends Aplicacoes implements ProcessoBatchProdutor
{
	
    /**
     *	Statement de obtencao de registros para processamento em modo DEFAULT.
     */
    private static final String SQL_DEFAULT =
        "SELECT /*+ index(a tpas_i1) */ " +
        "       a.idt_msisdn AS idt_msisdn " +
        "  FROM tbl_pro_assinante a, " +
        "       tbl_pro_promocao p " +
        " WHERE a.idt_promocao = p.idt_promocao " +
        "   AND a.dat_execucao >= ? " +
        "   AND a.dat_execucao <= ? " +
        "   AND p.idt_categoria = ? " +
        "   AND p.id_processo_batch = ? ";
    
    /**
     *	Statement de obtencao de registros para processamento em modo REBARBA.
     */
    private static final String SQL_REBARBA =
        "SELECT a.idt_msisdn AS idt_msisdn " +
        "  FROM tbl_pro_assinante a " +
        " WHERE a.idt_promocao = ? " +
        "   AND NOT EXISTS(SELECT 1 " +
        "                    FROM tbl_rec_fila_recargas f, " +
        "                         tbl_pro_tipo_transacao t " +
        "                   WHERE f.idt_msisdn = a.idt_msisdn " +
        "                     AND t.idt_promocao = a.idt_promocao " +
        "                     AND f.tip_transacao = t.tip_transacao " +
        "                     AND t.tip_execucao = ? " +
        "                     AND f.dat_execucao >= ? " +
        "                     AND f.dat_execucao <  ? " +
        "                     AND f.idt_status_processamento <= ?) ";
    
    /**
     *	Statement de obtencao de registros para processamento em modo PARCIAL.
     */
    private static final String SQL_PARCIAL =
        "SELECT /*+ index(a xpktbl_pro_assinante)*/ " +
        "       a.idt_msisdn as idt_msisdn " +
        "  FROM tbl_pro_assinante a, " +
        "       tbl_pro_promocao p," +
        "       tbl_pro_tipo_transacao t, " +
        "       tbl_pro_dia_execucao d " +
        " WHERE a.idt_promocao = p.idt_promocao " +
        "   AND t.idt_promocao = p.idt_promocao " +
        "   AND d.idt_promocao = p.idt_promocao " +
        "   AND d.tip_execucao = t.tip_execucao " +
        "   AND d.num_dia_entrada = TO_NUMBER(TO_CHAR(a.dat_entrada_promocao, 'DD')) " +
        "   AND p.idt_categoria = ? " +
        "   AND t.tip_execucao = ? " +
        "   AND d.num_dia_execucao <= ? " +
        "   AND p.id_processo_batch = ? ";

    /**
     *	Conexao com o banco de dados.
     */
	private PREPConexao conexaoPrep;

    /**
     *	Result set de registros para processamento.
     */
	private ResultSet registros;

    /**
     *	Status de processamento.
     */
	private String status;
	
    /**
     *	Data de referencia da execucao do processo.
     */
	private Date dataReferencia;
	
    /**
     *	Tipo de execucao do processo de concessao de bonus Pula-Pula.
     */
	private String tipoExecucao;
	
    /**
     *	Numero de registros processados com sucesso.
     */
    private int numSucesso;
    
    /**
     *	Numero de registros processados com erro funcional ou tecnico.
     */
    private int numErro;
    
    /**
     *	Numero de excecoes lancadas.
     */
    private int numExcecoes;
    
    /**
     *	Construtor da classe.
     *
     *	@param		idProcesso					Identificador do processo.
     */
	public GerentePulaPula(long idProcesso)
	{
		super(idProcesso, Definicoes.CL_PROMOCAO_GERENTE_PULA_PULA);
		
		this.conexaoPrep	= null;
		this.registros		= null;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.dataReferencia	= null;
		this.tipoExecucao	= null;
		this.numSucesso		= 0;
		this.numErro		= 0;
		this.numExcecoes	= 0;
	}

	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
	 */
	public void startup(String params[]) throws Exception
	{
		super.log(Definicoes.INFO, 
				  "GerentePulaPula.startup", 
				  "Tipo de Execucao: " + params[0] + 
				  " - Data: " + params[1] + 
				  ((params.length >= 3) ? " - Promocao: " + params[2] : ""));
		
		//Obtendo os parametros de entrada.
		this.tipoExecucao	= params[0];
		this.dataReferencia	= new SimpleDateFormat(Definicoes.MASCARA_DATE).parse(params[1]);
		String promocao		= this.tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA) ? params[2] : null;
		
		//Obtendo a conexao com o banco de dados.
		this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		
	    //Obtendo os objetos necessarios para a criacao dos parametros da consulta.
	    String sqlQuery = null;
	    Object[] parametros = null;
	    
        Calendar calMesAtual = Calendar.getInstance();
        calMesAtual.setTime(this.dataReferencia);
        Integer diaAtual = new Integer(calMesAtual.get(Calendar.DAY_OF_MONTH));
        calMesAtual.set(Calendar.DAY_OF_MONTH, calMesAtual.getActualMinimum(Calendar.DAY_OF_MONTH));
        
        Calendar calProximoMes = Calendar.getInstance();
        calProximoMes.setTime(this.dataReferencia);
        calProximoMes.set(Calendar.DAY_OF_MONTH, calProximoMes.getActualMinimum(Calendar.DAY_OF_MONTH));
        calProximoMes.add(Calendar.MONTH, 1);

        //Obtendo os parametros, que sao diferentes em funcao do tipo de execucao.
        if(this.tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT))
        {
    	    sqlQuery = GerentePulaPula.SQL_DEFAULT;
    	    
    	    parametros = new Object[4];
    	    parametros[0] = new java.sql.Date(calMesAtual.getTimeInMillis());
    	    parametros[1] = new java.sql.Date(this.dataReferencia.getTime());
    	    parametros[2] = new Integer(Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA);
    	    parametros[3] = new Integer(Definicoes.IND_GERENTE_PULA_PULA);
        }
        else if(this.tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA))
        {
    	    sqlQuery = GerentePulaPula.SQL_REBARBA;
    	    
    	    parametros = new Object[5];
    	    parametros[0] = promocao;
    	    parametros[1] = Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA;
    	    parametros[2] = new java.sql.Date(calMesAtual.getTimeInMillis());
    	    parametros[3] = new java.sql.Date(calProximoMes.getTimeInMillis());
    	    parametros[4] = new Integer(Definicoes.STATUS_RECARGA_ZERAR_SALDOS_CONCLUIDO);
        }
        else if(this.tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL))
        {
    	    sqlQuery = GerentePulaPula.SQL_PARCIAL;
	        
    	    parametros = new Object[4];
    	    parametros[0] = new Integer(Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA);
    	    parametros[1] = this.tipoExecucao;
    	    parametros[2] = diaAtual;
    	    parametros[3] = new Integer(Definicoes.IND_GERENTE_PULA_PULA);
        }
        else
        {
        	throw new GPPInternalErrorException("Opcao invalida");
        }
        
        this.registros = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.getIdLog());
	}

	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */	
	public Object next() throws Exception
	{
		if(this.registros.next())
			return this.registros.getString("idt_msisdn");
		
		return null;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		try
		{
			if(this.registros != null)
				this.registros.close();
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.getIdLog());
		}
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{
		this.status = Definicoes.PROCESSO_ERRO;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_GERENTE_PULA_PULA;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		String mensagem = "Tipo de Excecucao: ";
		mensagem += this.tipoExecucao;
		
		if(this.status.equals(Definicoes.PROCESSO_SUCESSO))
		{
			mensagem += " - Numero de assinantes processados:";
			mensagem += " SUCESSO: "	+ String.valueOf(this.numSucesso);
			mensagem += " ERRO: "		+ String.valueOf(this.numErro);
			mensagem += " EXCECOES: "	+ String.valueOf(this.numExcecoes);
		}
		else
			mensagem += " - Erro no processo por lancamento de excecao";
		
		return mensagem;
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
		return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(this.dataReferencia);
	}
		
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}

    /**
     *	Retorna a data de referencia da execucao do processo.
     *
     *	@return		Data de referencia da execucao do processo.
     */
	public Date getDataReferencia()
	{
		return this.dataReferencia;
	}
	
    /**
     *	Retorna o tipo de execucao do processo de concessao de bonus Pula-Pula.
     *
     *	@return		Tipo de execucao do processo de concessao de bonus Pula-Pula.
     */
	public String getTipoExecucao()
	{
		return this.tipoExecucao;
	}
	
	/**
	 *	Notifica ao produtor o resultado de sucesso para o consumo.
	 */
	public synchronized void notificarSucesso()
	{
		this.numSucesso++;
	}
	
	/**
	 *	Notifica ao produtor o resultado de erro para o consumo.
	 */
	public synchronized void notificarErro()
	{
		this.numErro++;
	}
	
	/**
	 *	Notifica ao produtor o lancamento de excecao durante o consumo.
	 */
	public synchronized void notificarExcecao()
	{
		this.numExcecoes++;
	}
	
}