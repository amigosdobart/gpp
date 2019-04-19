package com.brt.gpp.aplicacoes.enviarUsuariosRechargeExpired;

//Imports Java.
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

//Imports GPP.
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Produtor de registros para gerenciamento dos assinantes em Recharge Expired com Pula-Pula. Se os assinantes em 
 *  Recharged Expiredd estiverem cadastrados nas promocoes Pula-Pula configuradas e a expiracao for igual ou inferior 
 * 	ao minimo de dias tambem configurado (assumindo que o processo rode no final do dia), os assinantes devem passar 
 * 	para o status de Disconnected.
 *
 *	@author	Daniel Ferreira
 *	@since	17/02/2006
 */
public class AssinantesREPulaPulaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

    private	PREPConexao	conexaoPrep;
    private ResultSet	resultAssinantes;
    private int			numRegistros;
    private int			numSucesso;
    private int			maxRegistros;
    private int			maxSucesso;
    private String		status;
    private	String		mensagem;
    private	String		dataProcessamento;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     *
     *	@param		long					idLog						Identificador de LOG.
     */
	public AssinantesREPulaPulaProdutor(long logId)
	{
		super(logId, Definicoes.CL_ASSINANTES_RE_PULA_PULA_PROD);

		this.conexaoPrep		= null;
		this.resultAssinantes	= null;
		this.numRegistros		= 0;
		this.maxRegistros		= -1;
		this.numSucesso			= 0;
		this.maxSucesso			= -1;
		this.status				= Definicoes.PROCESSO_SUCESSO;
		this.mensagem			= "Numero de registros processados: TOTAL: :TOTAL SUCESSO: :SUCESSO";
		this.dataProcessamento	= null;
	}

	//Implementacao de Produtor.
	
    /**
     *	Inicia a execucao do processo de Gerenciamento de assinantes em Recharge Expired com Pula-Pula. O metodo obtem
     *	os registros que sao processados pelas threads consumidoras.
     *
     *	@param		String[]				params						Lista de parametros.
     *	@throws		Exception
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		
		try
		{
		    //Obtendo a data de processamento.
		    this.dataProcessamento = new SimpleDateFormat(Definicoes.MASCARA_DATE).format(new Date());
		    
		    //Obtendo o numero maximo de registros para processamento com sucesso, caso tenha sido passado por parametro.
		    if(params != null)
		    {
		        if(params.length > 0)
		        {
			        this.maxRegistros = Integer.parseInt(params[0]);
		        }
		        if(params.length > 1)
		        {
		            this.maxSucesso = Integer.parseInt(params[1]);
		        }
		    }
		    
		    //Obtendo configuracoes do GPP para execucao do processo.
		    MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstancia();
		    //Numero de dias que faltam para a expiracao do assinante em Recharge Expired. Todos os assinantes tal que
		    //a data de expiracao menos a data de processamento for menor ou igual ao valor configurado serao
		    //selecionados para o processamento.
		    int diasExpiracao = Integer.parseInt(mapConfiguracao.getMapValorConfiguracaoGPP("RE_PULA_PULA_NUM_DIAS"));
		    //Promocoes configuradas.
		    String promocoes = mapConfiguracao.getMapValorConfiguracaoGPP("RE_PULA_PULA_PROMOCOES");
		    //Executando a consulta pelos assinantes.
		    String sqlQuery =	this.getSqlQuery(this.parse(promocoes));
		    Object[] parametros =
		    {
		        new Integer(Definicoes.RECHARGE_EXPIRED),
		        this.dataProcessamento,
		        new Integer(diasExpiracao)
		    };
		    this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    this.resultAssinantes = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		    try
		    {
			    if(this.resultAssinantes != null) this.resultAssinantes.close();
		    }
		    finally
		    {
		        super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		    }
		}
		finally
		{
		    super.log(Definicoes.INFO, "startup", "Fim");
		}
	}
	
    /**
     *	Retorna para as threads consumidoras o proximo registro a ser processado.
     *
     *	@return		Object												MSISDN do assinante.
     *	@throws		Exception
     */
	public synchronized Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "next", "Inicio");
		
		try
		{
		    //Se foi passado por parametro o limite de assinantes a serem processados e a serem processados com 
		    //sucesso, apos ultrapassar este limite deve ser finalizada a execucao.
		    if((this.maxRegistros != -1) && (this.numRegistros > this.maxRegistros - 1))
		    {
		        return null;
		    }
		    if((this.maxSucesso != -1) && (this.numSucesso > this.maxSucesso - 1))
		    {
		        return null;
		    }
		    
		    if(this.resultAssinantes.next())
		    {
		        this.numRegistros++;
		        return resultAssinantes.getString("IDT_MSISDN");
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}
		
		return null;
	}

    /**
     *	Fecha o result set e a conexao com o banco de dados. 
     *
     *	@throws		Exception
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "finish", "Inicio");
		
		try
		{
		    if(this.resultAssinantes != null) this.resultAssinantes.close();
		    this.mensagem = this.mensagem.replaceAll(Definicoes.PATTERN_TOTAL, String.valueOf(this.numRegistros));
		    this.mensagem = this.mensagem.replaceAll(Definicoes.PATTERN_SUCESSO, String.valueOf(this.numSucesso));
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
     *	Trata excecoes lancadas pelos consumidores. 
     *
     *	@throws		Exception
     */
	public void handleException()
	{
	    this.mensagem = "Excecao lancada por consumidor";
	}
	
	//Implementacao de ProcessoBatchProdutor.
	
    /**
     *	Retorna o identificador do processo batch. 
     *
     *	@return		int													Identificador do processo batch.
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_ASSINANTES_RE_PULA_PULA;
	}
	
    /**
     *	Retorna a mensagem informativa sobre a execucao do processo batch. 
     *
     *	@return		String					mensagem					Mensagem informativa sobre a execucao.
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}
	
    /**
     *	Retorna o status da execucao do processo. 
     *
     *	@return		String					status						Status da execucao do processo.
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}
	
    /**
     *	Atribui o status da execucao do processo. 
     *
     *	@param		String					status						Status da execucao do processo.
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}
	
    /**
     *	Retorna a data de processamento. 
     *
     *	@param		String												Data de execucao no formato dd/mm/yyyy.
     */
	public String getDataProcessamento()
	{
	    return this.dataProcessamento;
	}
	
    /**
     *	Retorna a conexao PREP para os consumidores. Nao utilizado pelo processo de Estorno de Bonus Pula-Pula por
     *	Fraude. 
     *
     *	@param		PREPConexao											Conexao PREP.
     */
	public PREPConexao getConexao()
	{
	    return this.conexaoPrep;
	}
	
	//Outros metodos.
	
    /**
     *	Notifica o produtor da execucao de um registro com sucesso. Utilizado pelos consumidores. 
     */
	public synchronized void notificaSucesso()
	{
	    this.numSucesso++;
	}
	
	
    /**
     *	Interpreta a string contendo as promocoes. 
     *
     *	@param		String					promocoes					String contendo as promocoes.
     *	@return		String[]											Vetor com as promocoes.
     */
	private String[] parse(String promocoes)
	{
	    if(promocoes != null)
	    {
	        return promocoes.split(";");
	    }
	    
	    return null;
	}
	
    /**
     *	Retorna a consulta SQL para obtencao dos registros. 
     *
     *	@param		String[]				promocoes					Vetor com as promocoes.
     *	@return		String					result						Instrucao SQL.
     *	@throws		GPPInternalErrorException
     */
	private String getSqlQuery(String[] promocoes) throws GPPInternalErrorException
	{
	    if(promocoes != null)
	    {
		    String result =	"SELECT p_assinante.idt_msisdn " +
							"FROM tbl_apr_assinante assinante, " +
							"     tbl_pro_assinante p_assinante " +
							"WHERE assinante.idt_msisdn = p_assinante.idt_msisdn " +
							"  AND assinante.idt_status = ? " +
							"  AND trunc(assinante.dat_expiracao_principal) - to_date(?, 'dd/mm/yyyy') <= ? " +
							"  AND (:PROMOCOES)";
		    
		    StringBuffer statementBuffer = new StringBuffer();
		    
		    for(int i = 0; i < promocoes.length; i++)
		    {
		        statementBuffer.append("p_assinante.idt_promocao = ");
		        statementBuffer.append(promocoes[i]);
		        if(i < promocoes.length - 1)
		        {
		            statementBuffer.append(" OR ");
		        }
		    }
		    
		    return result.replaceAll(":PROMOCOES", statementBuffer.toString());
	    }
	    
	    //Caso as promocoes nao estejam configuradas deve ser lancada excecao. O processo nao pode continuar.
	    throw new GPPInternalErrorException("Promocoes nao configuradas");
	}
	
}