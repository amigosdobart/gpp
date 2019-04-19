package com.brt.gpp.aplicacoes.promocao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pela sumarizacao dos valores pagos pelas recargas efetuadas pelos assinantes para utilizacao
 *	no calculo de bonus de promocoes com limite dinamico. O processo deve executar uma e somente uma vez por dia,
 *	sumarizando os registros que entrantes na TBL_REC_RECARGAS no periodo.
 *
 *	@author 	Daniel Ferreira
 *	@since 		10/11/2005
 */
public final class SumarizacaoRecargasAssinantes extends Aplicacoes implements ProcessoBatchProdutor
{
	
    /**
     *	Data de processamento.
     */
	private Date dataProcessamento;
	
    /**
     *	Mensagem informativa referente a execucao do processo.
     */
	private String mensagem;
    
    /**
     *	Status de processamento.
     */
	private String status;
	
    /**
     *	Numero de registros processados.
     */
	private int numAssinantes;
	
    /**
     *	Statement SQL para verificacao de dia ja processado.
     */
	private static final String SQL_QUERY = 
		"SELECT 1 " +
		"  FROM tbl_ger_historico_proc_batch " +
		" WHERE id_processo_batch        = ? " +
		"   AND idt_status_execucao      = ? " +
		"   AND trunc(dat_processamento) = ? ";
		
    /**
     *	Statement SQL para execucao do merge do processo.
     */
	private static final String SQL_MERGE = 
		"MERGE /*+ index(tabela xpktbl_pro_total_recargas)*/                                                          "+
		" INTO tbl_pro_totalizacao_recargas tabela                                                                    "+
		"USING (SELECT to_char(dat_origem,'YYYYMM')                   AS DAT_MES,			    		              "+
		"	           idt_msisdn                                     AS IDT_MSISDN,			    		          "+
		"      	       MAX(DECODE(ordem, 1, dat_origem, null))        AS DAT_ULTIMA_RECARGA,				          "+
		"              MAX(DECODE(ordem, 1, vlr_pago, null))          AS VLR_ULTIMA_RECARGA,				          "+
		"	           COUNT(*)                                       AS QTD_RECARGAS,					              "+
		"              SUM(vlr_pago)                                  AS VLR_PAGO				    		          "+
		"	      FROM (SELECT 										                            		              "+
		"	            idt_msisdn                                  AS idt_msisdn,					                  "+
		"               dat_origem                                  AS dat_origem,					                  "+
		"	            vlr_pago                                    AS vlr_pago,					                  "+
		"	            RANK() OVER(partition by idt_msisdn ORDER BY rec.dat_origem DESC) AS ordem	                  "+
		"	       FROM tbl_rec_recargas rec , 														                  "+
		"	            tbl_ger_plano_preco pl														                  "+
		"	      WHERE rec.idt_plano_preco =  pl.idt_plano_preco									                  "+
		"	        AND rec.id_tipo_recarga  = ?													                  "+
		"	        AND rec.dat_inclusao    >= (to_date(?,'DD/MM/YYYY'))			    			                  "+
		"	        AND rec.dat_inclusao    <  (to_date(?,'DD/MM/YYYY'))							                  "+
		"	        AND pl.idt_categoria     = ?													                  "+
		"	     )														    						                  "+
		"   GROUP BY to_char(dat_origem,'YYYYMM'), idt_msisdn) consulta                                               "+
		"   ON (tabela.idt_msisdn = consulta.idt_msisdn                                                               "+
		"  AND  tabela.dat_mes    = consulta.dat_mes)                                                                 "+
		" WHEN MATCHED THEN UPDATE                                                                                    "+
		"                      SET tabela.qtd_recargas = nvl(tabela.qtd_recargas, 0) + nvl(consulta.qtd_recargas, 0), "+
		"                          tabela.vlr_pago     = nvl(tabela.vlr_pago    , 0) + nvl(consulta.vlr_pago    , 0), "+
		"                          tabela.vlr_ultima_recarga = consulta.vlr_ultima_recarga,                           "+
		"                          tabela.dat_ultima_recarga = consulta.dat_ultima_recarga                            "+
		" WHEN NOT MATCHED THEN INSERT (idt_msisdn,                                                                   "+
		"                               dat_mes,                                                                      "+
		"                               qtd_recargas,                                                                 "+
		"                               vlr_pago,                                                                     "+
		"                               dat_ultima_recarga,                                                           "+
		"                               vlr_ultima_recarga)                                                           "+
		"                       VALUES (consulta.idt_msisdn,                                                          "+
		"                               consulta.dat_mes,                                                             "+
		"                               consulta.qtd_recargas,                                                        "+
		"                               consulta.vlr_pago,                                                            "+
		"                               consulta.dat_ultima_recarga,                                                  "+
		"								consulta.vlr_ultima_recarga)                                                  ";
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public SumarizacaoRecargasAssinantes(long logId)
	{
		super(logId, Definicoes.CL_PROMOCAO_SUMARIZACAO_RECARGAS);
		
		this.dataProcessamento	= null;
		this.mensagem			= null;
		this.status				= Definicoes.PROCESSO_ERRO;
		this.numAssinantes		= 0;
	 }
	 
	 //Implementacao de Produtor.
	 
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
	 */
	public void startup(String[] params) throws Exception
	{
	 	PREPConexao conexaoPrep = null;
	 	
	 	try
		{
	 		super.log(Definicoes.INFO, "startup", "Processando sumarizacao de recargas de assinantes. Data de Referencia: " + params[0]);
	 		
		 	//Extraindo a data de processamento a partir da data de referencia. A data de referencia deve ser a data em
		 	//que o processo normalmente roda (a nao ser que haja uma execucao retroativa). A data de processamento 
		 	//corresponde ao dia anterior.
		 	SimpleDateFormat	conversorDate	= new SimpleDateFormat(Definicoes.MASCARA_DATE);
		 	Calendar			calReferencia	= Calendar.getInstance();
		 	calReferencia.setTime(conversorDate.parse(params[0]));
		 	calReferencia.add(Calendar.DAY_OF_MONTH, -1);
		 	this.dataProcessamento = calReferencia.getTime();
		 	
		 	//Obtendo conexao com o banco de dados.
		 	conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		 	
		 	if(!this.diaProcessado(this.dataProcessamento, conexaoPrep))
		 	{
		 		this.numAssinantes = this.executarMerge(this.dataProcessamento, conexaoPrep);
		 		this.status = Definicoes.PROCESSO_SUCESSO;
		 		this.mensagem = "Numero de assinantes processados: " + this.numAssinantes;
		 	}
		 	else
		 		this.mensagem = "Data " + this.getDataProcessamento() + " ja processada.";
		}
	 	catch(Exception e)
		{
	 		this.mensagem = "Excecao: " + e;
	 		throw e;
		}
	 	finally
		{
	 		//Liberando a conexao com o banco de dados.
	 		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		}
	}
	 
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		return null;
	}

	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
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
	    return Definicoes.IND_SUMARIZACAO_RECARGAS_ASSINANTES;
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
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(this.dataProcessamento);
	}
	
    /**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
	public PREPConexao getConexao()
	{
	    return null;
	}
	
	//Outros metodos.
	
	/**
	 *	Verifica se a data ja foi processada.
	 *	
	 *	@param		dataProcessamento			Data de processamento.
	 *	@param		conexaoPrep					Conexao com o banco de dados.  
	 *	@return		True se ja houve processamento referente a data e false caso contrario.
	 *	@throws		Exception
	 */
	private boolean diaProcessado(Date dataReferencia, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet result = null;
		
		try
		{
			Object[] parametros =
			{
				new Integer(Definicoes.IND_SUMARIZACAO_RECARGAS_ASSINANTES),
				Definicoes.PROCESSO_SUCESSO,
				new java.sql.Date(dataReferencia.getTime())
			};
			
			result = conexaoPrep.executaPreparedQuery(SumarizacaoRecargasAssinantes.SQL_QUERY, parametros, this.getIdLog());
			
			return result.next();
		}
		finally
		{
			if(result != null)
				result.close();
		}
	}
	
	/**
	 *	Sumariza os valores pagos em recargas pelos assinantes que entraram na TBL_REC_RECARGAS. O metodo consiste
	 *	na chamada de um merge para execucao pelo banco de dados, retornando o numero de assinantes processados.
	 *	OBSERVACAO: O processo sumariza somente recargas de assinantes pre-pagos.
	 * 
	 *	@param		dataProcessamento		Data de referencia para sumarizacao.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		Numero de assinantes processados.
	 *	@throws		Exception
	 */
	private int executarMerge(Date dataProcessamento, PREPConexao conexaoPrep) throws Exception
	{
		//Obtendo as datas de analise para pesquisa na tabela de recargas.
		Date		dataInicio	= dataProcessamento;
		Date		dataFim		= null;
		Calendar	calAnalise	= Calendar.getInstance();
		calAnalise.setTime(dataProcessamento);
		calAnalise.add(Calendar.DAY_OF_MONTH, 1);
		dataFim = calAnalise.getTime();
		
		//Obtendo formatador para passagem de parametro de datas em formato string, uma vez que a tabela de recargas
		//e particionada e pode haver problemas de performance com a passagem de objetos java.sql.Date como parametro.
		SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
		
		Object parametros[] = 
		{
		    Definicoes.TIPO_RECARGA,
			conversorDate.format(dataInicio),
			conversorDate.format(dataFim),
			new Integer(Definicoes.CATEGORIA_PREPAGO)
		};
		
		return conexaoPrep.executaPreparedUpdate(SumarizacaoRecargasAssinantes.SQL_MERGE, parametros, super.logId);
	}
	
}