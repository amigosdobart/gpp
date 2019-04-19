package com.brt.gpp.aplicacoes.promocao.gerentePromocao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pelo gerenciamento de retirada de suspensao de assinantes com promocoes pendentes de
 *	primeira recarga. As promocoes que exigem primeira recarga suspendem o recebimento de bonus ate que o assinante
 *	realize uma recarga.
 *
 *	@author		Daniel Ferreira
 *	@since		07/06/2006
 *
 *  Classe alterada para substituição da FNC_FEZ_RECARGA do Banco de Dados por novas rotinas nas classes Java.
 *  @author     Jorge Abreu
 *  @since      28/08/2007 
 */
public class GerenciadorPendenciaRecargaProdutor extends ControlePromocao implements ProcessoBatchProdutor
{
    
    //Constantes.
    
    /**
     *	Statement SQL para obtencao dos registros.
     */
	private static final String SQL_PENDENTE_RECARGA = 
	    "	SELECT a.idt_msisdn								AS IDT_MSISDN,	                     "+
	    "          a.idt_promocao                           AS IDT_PROMOCAO,                     "+
	    "          a.dat_execucao                           AS DAT_EXECUCAO,                     "+
	    "          a.dat_entrada_promocao                   AS DAT_ENTRADA_PROMOCAO,             "+
	    "          a.dat_inicio_analise                     AS DAT_INICIO_ANALISE,               "+
	    "          a.dat_fim_analise                        AS DAT_FIM_ANALISE,                  "+
	    "          a.ind_isento_limite                      AS IND_ISENTO_LIMITE,                "+
	    "          a.idt_status                             AS IDT_STATUS,                       "+
	    "          a.dat_ultimo_bonus                       AS DAT_ULTIMO_BONUS                  "+
	    "	  FROM tbl_pro_assinante a,              										     "+
	    "	       tbl_pro_promocao b										                     "+
	    "	 WHERE b.idt_promocao = a.idt_promocao							                     "+
	    "	   AND a.idt_status = ?											                     "+
	    "	   AND b.ind_primeira_recarga_obr = 0							                     "+
	    "	UNION															                     "+
	    "	SELECT distinct(b.idt_msisdn)					AS IDT_MSISDN,                       "+
	    "          b.idt_promocao                           AS IDT_PROMOCAO,                     "+
	    "          b.dat_execucao                           AS DAT_EXECUCAO,                     "+
	    "          b.dat_entrada_promocao                   AS DAT_ENTRADA_PROMOCAO,             "+
	    "          b.dat_inicio_analise                     AS DAT_INICIO_ANALISE,               "+
	    "          b.dat_fim_analise                        AS DAT_FIM_ANALISE,                  "+
	    "          b.ind_isento_limite                      AS IND_ISENTO_LIMITE,                "+
	    "          b.idt_status                             AS IDT_STATUS,                       "+
	    "          b.dat_ultimo_bonus                       AS DAT_ULTIMO_BONUS                  "+
	    "	  FROM tbl_rec_recargas a,									                         "+
	    "	       tbl_pro_assinante b      								                     "+
	    "	 WHERE a.idt_msisdn = b.idt_msisdn 								                     "+
	    "	   AND b.idt_status = ? 										                     "+
	    "	   AND a.id_tipo_recarga = ?									                     "+
	    "	   AND a.dat_inclusao >= (to_date( ? ,'dd/mm/yyyy') - 1)		                     "+
	    "	   AND a.dat_inclusao < to_date( ? ,'dd/mm/yyyy')				                     ";
     
    
    //Atributos.
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
    private Consulta consulta;
    /**
     *	Result Set contendo os registos a serem processados.
     */
    private ResultSet registros;
    
    /**
     *	Numero de registos processados.
     */
    private int numRegistros;
    
    /**
     *	Status de processamento.
     */
    private String status;
    
    /**
     *	Mensagem informativa para o historico de processos batch.
     */
    private String mensagem;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public GerenciadorPendenciaRecargaProdutor(long logId)
	{
		super(logId, Definicoes.CL_PROMOCAO_GER_PEND_REC_PROD);

		this.conexaoPrep	= null;
		this.registros		= null;
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

	/**
     *	Realiza o parse da data que é passada como parâmetro para o processo.
     *
     *	@param		String param				String contendo a data.
     *	@throws		Exception                    
     */
	private String parseParametro(String param) throws GPPInternalErrorException
	{
		if (param == null || param.length() == 0 )
			throw new GPPInternalErrorException("Parâmetro de data (DD/MM/YYYY) é obrigatório para o processo.");
		
		try
		{
			new SimpleDateFormat("dd/mm/yyyy").parse(param);
			return param;
			
		}catch(Exception e)
		 {
			throw new GPPInternalErrorException("Data não está no formato (DD/MM/YYYY) válido: " + param);
		 }
	}
	
    /**
     *	Inicia a execucao do processo, gerando o Result Set de registros para consumo.
     *
     *	@param		params					Lista de parametros fornecidos em linha de comando.
     *	@throws		Exception
     */
	public void startup(String[] params) throws Exception
	{
		try
		{   //Instancia Classe Consulta
			consulta = new Consulta(super.logId);
			
		    //Obtendo conexao com o banco de dados.
		    this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    
            //Tratando a data recebida como parâmetro
		    String data = parseParametro(params[0]);
		    
            //Executando a consulta dos registros de assinantes pendentes de primeira recarga que já realizaram a recarga.
		    String sqlQuery = GerenciadorPendenciaRecargaProdutor.SQL_PENDENTE_RECARGA;
		    Object[] parametros = 
		    {
		    		new Integer(PromocaoStatusAssinante.STATUS_PENDENTE_RECARGA),
		    		new Integer(PromocaoStatusAssinante.STATUS_PENDENTE_RECARGA),
		    		Definicoes.TIPO_RECARGA,
		    		data,
		    		data
		    };
		    this.registros = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		    throw e;
		}
	}
	
    /**
     *	Retorna para as threads consumidoras o proximo registro a ser processado.
     *
     *	@return		Value Object a ser processado por uma das threads consumidoras.
     *	@throws		Exception
     */
	public synchronized Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "next", "Inicio");
		
		try
		{
			if(this.registros.next())
		    {
		        this.numRegistros++;
		        return montaObjeto(this.registros);   
		    }  
		}
		catch (Exception e) 
		{
			super.log(Definicoes.ERRO, "GerenciadorPendenciaRecargaProdutor.next", "Erro ao montar o objeto PromocaoAssinante: " + e);
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "GerenciadorPendenciaRecargaProdutor.next", "Fim");
		}
		
		return null;
	}
	
	/**
     *  Processa os registros da consulta para cada consumidor montando um objeto PromocaoAssinante
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws NumberFormatException 
     *
     *  @throws    Exception
     */
	public PromocaoAssinante montaObjeto(ResultSet registro) throws Exception
	{
		
			 //Obtendo as informacoes da promocao do assinante.
			Integer idPromocao = new Integer(registro.getString("IDT_PROMOCAO"));
	        Promocao promocao = super.consulta.getPromocao(idPromocao);
	        
	    	//Objeto que será passado para os consumidores
	    	PromocaoAssinante pAssinante = new PromocaoAssinante(); 
	    	
	    	pAssinante = new PromocaoAssinante();
            pAssinante.setPromocao(promocao);
            pAssinante.setIdtMsisdn(registro.getString("IDT_MSISDN"));
            pAssinante.setDatExecucao(registro.getDate("DAT_EXECUCAO"));
            pAssinante.setDatEntradaPromocao(registro.getTimestamp("DAT_ENTRADA_PROMOCAO"));
            pAssinante.setDatInicioAnalise(registro.getDate("DAT_INICIO_ANALISE"));
            pAssinante.setDatFimAnalise(registro.getDate("DAT_FIM_ANALISE"));
            pAssinante.setIndIsentoLimite(registro.getShort("IND_ISENTO_LIMITE"));
            pAssinante.setStatus(consulta.getPromocaoStatusAssinante(new Integer(registro.getInt("IDT_STATUS"))));
            pAssinante.setDatUltimoBonus(registro.getTimestamp("DAT_ULTIMO_BONUS"));
            pAssinante.setDiasExecucao(consulta.getPromocaoDiasExecucao(idPromocao, pAssinante.getDatEntradaPromocao()));
     
			return pAssinante;
	}

    /**
     *	Fecha o Result Set de registros e termina a execucao do processo. 
     *
     *	@throws		Exception
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "finish", "Inicio");
		
		try
		{
		    if(this.registros != null)
		        this.registros.close();
		    
		    this.mensagem = this.mensagem.concat(String.valueOf(this.numRegistros));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		finally
		{
		    super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		    super.log(Definicoes.INFO, "finish", "Fim");
		}
	}

    /**
     *	Trata excecoes lancadas pelo produtor.  
     *
     *	@throws		Exception
     */
	public void handleException()
	{
	    this.mensagem = "Excecao lancada por consumidor. " + this.mensagem;
	}
	
	//Implementacao de ProcessoBatchProdutor.
	
    /**
     *	Retorna o identificador do processo batch. 
     *
     *	@return		Identificador do processo batch.
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_GERENCIAMENTO_PENDENCIA_RECARGA;
	}
	
    /**
     *	Retorna a mensagem informativa sobre a execucao do processo batch. 
     *
     *	@return		Mensagem informativa sobre a execucao.
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}
	
    /**
     *	Retorna o status da execucao do processo. 
     *
     *	@return		Status da execucao do processo.
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}
	
    /**
     *	Atribui o status da execucao do processo. 
     *
     *	@param		status					Status da execucao do processo.
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}
	
    /**
     *	Retorna a data de processamento. Como o processo nao precisa receber uma data passada por parametro, retorna 
     *	a data do sistema.
     *
     *	@param		Data de execucao no formato dd/mm/yyyy.
     */
	public String getDataProcessamento()
	{
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
    /**
     *	Retorna a conexao PREP para os consumidores.
     *
     *	@param		Conexao PREP.
     */
	public PREPConexao getConexao()
	{
	    return this.conexaoPrep;
	}
	
}