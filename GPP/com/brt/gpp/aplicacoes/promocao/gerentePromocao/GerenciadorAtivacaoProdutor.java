package com.brt.gpp.aplicacoes.promocao.gerentePromocao;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pelo cadastro de novos assinantes em promocoes. O processo verifica as mudancas diarias de 
 *	status de 1 para 2 e cadastra os assinantes nas promocoes vigentes. Apesar de o master das promocoes Pula-Pula
 *	ser o Clarify, este processo de contingencia insere os assinantes diariamente em promocoes vigentes para 
 *	evitar que os assinantes fiquem sem promocao. Se o assinante for incluido na promocao via Gerente Promocao
 *	primeiro, no momento da chamada da API o processo ira ajustar a promocao de acordo com a enviada pelo Clarify.
 *	Se o gerente processar o assinante apos a chamada da API, ira retornar erro uma vez que o assinante ja possui o
 *	Pula-Pula. 
 * 
 *	@author		Daniel Ferreira
 *	@since		14/06/2006
 */
public class GerenciadorAtivacaoProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    
    //Constantes.
    
    /**
     *	Statement SQL para obtencao dos registros.
     */
    private static final String SQL_ATIVACAO = 
		"SELECT status.idt_msisdn as idt_msisdn " +
		"  FROM (SELECT assinante_hoje.sub_id                 as idt_msisdn, " +
		"               assinante_hoje.account_status         as idt_status_hoje, " +
		"               nvl(assinante_ontem.account_status,0) as idt_status_ontem " +
		"          FROM tbl_apr_assinante_tecnomen assinante_ontem, " +
		"               tbl_apr_assinante_tecnomen assinante_hoje " +
		"         WHERE assinante_ontem.sub_id (+)         = assinante_hoje.sub_id " +
		"           AND assinante_ontem.dat_importacao (+) = to_date(?,'dd/mm/yyyy') - 1 " +
		"           AND assinante_hoje.dat_importacao      = to_date(?,'dd/mm/yyyy')) status " +
		" WHERE status.idt_status_ontem <= ? " +
		"   AND status.idt_status_hoje  >= ? ";

    //Atributos.
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
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
    
    /**
     *	Data de processamento, no formato dd/mm/yyyy.
     */
    private String dataProcessamento;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public GerenciadorAtivacaoProdutor(long logId)
	{
		super(logId, Definicoes.CL_PROMOCAO_GER_ATIV_PROD);

		this.conexaoPrep		= null;
		this.registros			= null;
		this.numRegistros		= 0;
		this.status				= Definicoes.PROCESSO_SUCESSO;
		this.mensagem			= "Numero de registros processados: ";
		this.dataProcessamento	= null;
	}

	//Implementacao de Produtor.
	
    /**
     *	Inicia a execucao do processo, gerando o Result Set de registros para consumo.
     *
     *	@param		params					Lista de parametros fornecidos em linha de comando.
     *	@throws		Exception
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		
		try
		{
		    //Executando o parse da data de processamento. Este processo e necessario uma vez que se a data estiver
		    //em formato incorreto, uma excecao do tipo ParseException sera lancada. Com o catch desta excecao e
		    //possivel atualizar a data de processamento do produtor para a data do sistema. Se isto nao for feito
		    //nao sera possivel incluir o registro no historico de processos batch.
		    SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
		    this.dataProcessamento = params[0];
		    Date dataProcessamento = conversorDate.parse(params[0]);
		    
		    //Obtendo conexao com o banco de dados.
		    this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    
		    //Executando a consulta pelos registros.
		    String sqlQuery = GerenciadorAtivacaoProdutor.SQL_ATIVACAO;
		    
		    Object[] parametros = 
		    {
		        conversorDate.format(dataProcessamento),
		        conversorDate.format(dataProcessamento),
		        new Integer(Definicoes.STATUS_FIRST_TIME_USER),
		        new Integer(Definicoes.STATUS_NORMAL_USER)
		    };
		    
		    this.registros = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);
		}
		catch(ParseException e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao de Parse: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao de Parse: " + e;
		    //Se ocorrer excecao de parse, e porque a data de processamento foi passada por parametro de forma errada.
		    //Desta forma, a data deve ser atualizada de acodo com a data do sistema.
		    this.dataProcessamento = new SimpleDateFormat(Definicoes.MASCARA_DATE).format(new Date());
		    throw e;
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		    throw e;
		}
		finally
		{
		    super.log(Definicoes.INFO, "startup", "Fim");
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
		        return this.registros.getString("idt_msisdn");
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}
		
		return null;
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
	    return Definicoes.IND_GERENCIAMENTO_ATIVACAO;
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
     *	Retorna a data de processamento. 
     *
     *	@param		Data de execucao no formato dd/mm/yyyy.
     */
	public String getDataProcessamento()
	{
	    return this.dataProcessamento;
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