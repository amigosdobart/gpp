package com.brt.gpp.aplicacoes.campanha.natalPagueGanhe;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteTSD;
import com.brt.gpp.aplicacoes.campanha.entidade.ProdutorCampanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Produtor da campanha que implementa da promocao Natal Pague e Ganhe. A classe obtem a lista de assinantes que 
 *	subiram TSD no dia para disponibilizacao aos consumidores para verificacao e cadastro.
 *
 *	@author		Daniel Ferreira
 *	@since		03/11/2006
 */
public class NPGProdutor extends Aplicacoes implements ProdutorCampanha
{
	
	//Constantes.
	
	/**
	 *	Statement SQL para obtencao dos registros.
	 */
	private static final String SQL_REGISTROS = "SELECT /*+ index(a xpktbl_apr_assinante)*/ " +
												"       c.nu_msisdn, " +
												"       a.idt_plano_preco, " +
												"       c.nu_imei, " +
												"       c.dt_inclusao, " +
												"       c.nu_iccid " +
												"  FROM hsid.hsid_cliente c, " +
												"       tbl_apr_assinante a " +
												" WHERE a.idt_msisdn = c.nu_msisdn " +
												"   AND dt_inclusao >= to_date(?,'dd/mm/yyyy') " +
												"   AND dt_inclusao <  to_date(?,'dd/mm/yyyy') " +
												"   AND NOT EXISTS (SELECT 1" +
												"                     FROM tbl_cam_assinante_campanha ca" +
												"                    WHERE ca.idt_msisdn = c.nu_msisdn " +
												"                      AND ca.id_campanha = ?) " +
												" ORDER BY c.dt_inclusao ";
	
	//Atributos.
	
	/**
	 *	Conexao com o banco de dados.
	 */
	private PREPConexao conexaoPrep;
	
	/**
	 *	Result Set com a lista de assinantes para verificacao de elegibilidade.
	 */
	private ResultSet registros;
	
	/**
	 *	Data de processamento.
	 */
	private Date dataProcessamento;
	
	/**
	 *	Status do processo.
	 */
	private String status;
	
	/**
	 *	Mensagem informativa sobre a execucao do processo.
	 */
	private String mensagem;
	
	/**
	 *	Numero de registros processados.
	 */
	private int numRegistros;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		logId					Identificador de log.
	 */
	public NPGProdutor(long logId)
	{
		super(logId, NPGProdutor.class.getName());
		
		this.dataProcessamento = null;
		this.status = Definicoes.TIPO_OPER_SUCESSO;
		this.mensagem = "Numero de registros processados: ";
		this.numRegistros = 0;
	}
	
	//Implementacao de Produtor.

	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
	 */
	public void startup(String[] params) throws Exception
	{
		SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
		
		//Executando o parse da data de referencia.
		Date dataReferencia = conversorDate.parse(params[0]);
		
		//Obtendo as datas de analise a partir da data de processamento. As datas de analise sao calculadas de forma
		//que o select retorne as subidas de TSD em um dia, correspondente a data de processamento. A data de 
		//processamento corresponde ao dia anterior a data de referencia.
		Calendar calAnalise = Calendar.getInstance();
		calAnalise.setTime(dataReferencia);
		calAnalise.add(Calendar.DAY_OF_MONTH, -1);
		this.dataProcessamento = calAnalise.getTime();
		String dataInicio = conversorDate.format(this.dataProcessamento);
		calAnalise.add(Calendar.DAY_OF_MONTH, 1);
		String dataFim = conversorDate.format(calAnalise.getTime());
		
		super.log(Definicoes.INFO, "startup", "Inicio - Data de Processamento: " + conversorDate.format(this.dataProcessamento));
		
		//Obtendo a conexao com o banco de dados.
		this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		
		//Executando consulta ao banco de dados para obtencao da lista de subidas de TSD.
		Object[] parametros = 
		{
			dataInicio,
			dataFim,
			params[params.length - 1]
		};
		
		this.registros = this.conexaoPrep.executaPreparedQuery(NPGProdutor.SQL_REGISTROS, parametros, super.logId);
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		if((this.registros != null) && (this.registros.next()))
		{
			AssinanteTSD result = new AssinanteTSD();
			
			result.setMSISDN       (this.registros.getString   ("nu_msisdn"      ));
			result.setPlanoPreco   (this.registros.getShort    ("idt_plano_preco"));
			result.setDataSubidaTSD(this.registros.getTimestamp("dt_inclusao"    ));
			result.setICCID        (this.registros.getString   ("nu_iccid"       ));
			result.setIMEI         (this.registros.getString   ("nu_imei"        ));
			
			super.log(Definicoes.DEBUG, "next", "Processando: " + result.toString());
			
			this.numRegistros++;
			return result;
		}
		
		this.mensagem += this.numRegistros;
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
			{
				this.registros.close();
			}
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		}
	}
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{
		this.status = Definicoes.TIPO_OPER_ERRO;
		this.mensagem = "Numero de registros processados: " + this.numRegistros + ". Excecao lancada por consumidor.";
	}
	
	//Implementacao de ProcessoBatchProdutor.
	
	/**
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_GER_INSCRICAO_ASSINANTES_CAMP;
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
		return this.conexaoPrep;
	}
	
	//Implementacao de ProdutorCampanha.
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.ProdutorCampanha.retornarAssinante()
	 */
	public Assinante retornarAssinante() throws Exception
	{
		return (Assinante)this.next();
	}
	
}
