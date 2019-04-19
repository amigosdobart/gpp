package com.brt.gpp.aplicacoes.relatorioChurn;

//Imports Java.
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.ResultSet;

//Imports GPP.
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gppEnviaMail.aplicacao.ProcessadorResultadoCSV;

/**
 *	Classe responsavel pelo gerenciamento do processo de geracao do relatório de Churn.
 *
 *	@author	Magno Batista Corrêa
 *	@since	13/06/2006 (dd/mm/yyyy)
 */
public class RelatorioChurnProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private String					status;
	private String					mensagem;
	private PREPConexao				conexaoPrep;
	private static final String SQL = 
		"SELECT                                                                                      "+
		"    ASS.sub_id                                                           AS MSISDN,         "+
		"    TO_CHAR (ASS.activation_date, 'dd/mm/yyyy')                          AS DATA_ATIVACAO,  "+
		"    TO_CHAR (ASS.dat_importacao , 'dd/mm/yyyy')                          AS DATA_CHURN,     "+
		"    CLI.nu_imei                                                          AS IMEI,           "+
		"    FABRICANTE.no_fabricante                                             AS FABRICANTE,     "+
		"    MODELO.no_modelo                                                     AS MODELO,         "+
		"                                                                                            "+
		"    CLI.in_aparelho_proprio                                              AS PROPRIO,        "+
		"        (SELECT NVL (COUNT (1), 0)                                                          "+
		"           FROM tbl_rec_recargas r                                                          "+
		"          WHERE r.idt_msisdn         = ASS.sub_id                                           "+
		"            AND r.id_tipo_recarga    = 'R'                                                  "+
		"            AND r.dat_origem        >= ASS.activation_date)              AS QTD_RECARGAS,   "+
		"    (                                                                                       "+
		"        select                                                                              "+
		"            COUNT(1)                                                                        "+
		"        from                                                                                "+
		"        HSID.HSID_CLIENTE_INICIO    CLI_INI,                                                "+
		"            hsid.hsid_cliente       cliente                                                 "+
		"        where                                                                               "+
		"                CLI_INI.nu_msisdn    = ASS.sub_id                                           "+
		"            AND cliente.nu_imei      = CLI_INI.nu_imei                                      "+
		"            AND cliente.nu_msisdn   <> CLI_INI.nu_msisdn                                    "+
		"    )                                                                    AS SITUACAO        "+
		"FROM                                                                                        "+
		"    tbl_apr_assinante_tecnomen  ASS,                                                        "+
		"    tbl_ger_plano_preco         GER,                                                        "+
		"    HSID.HSID_CLIENTE           CLI,                                                        "+
		"    HSID.HSID_MODELO            MODELO,                                                     "+
		"    HSID.HSID_FABRICANTE        FABRICANTE                                                  "+
		"WHERE                                                                                       "+
		"        ASS.account_status   = ?                                                            "+
		"    AND ASS.profile_id       = GER.idt_plano_preco                                          "+
		"    AND GER.idt_categoria    = 0                                                            "+
		"    AND ASS.service_status   = 1                                                            "+
		"    AND ASS.dat_importacao  >= TO_DATE(?,'DD/MM/YYYY')                                      "+
		"    AND ASS.dat_importacao  <  TO_DATE(?,'DD/MM/YYYY')+1                                    "+
		"    AND EXISTS (                                                                            "+
		"          SELECT 1                                                                          "+
		"            FROM tbl_apr_assinante_tecnomen assinante2                                      "+
		"           WHERE assinante2.sub_id           = ASS.sub_id                                   "+
		"             AND assinante2.account_status  <> ?                                            "+
		"             AND assinante2.dat_importacao   = (ASS.dat_importacao - 1)                     "+
		"    )                                                                                       "+
		"    AND MODELO.co_modelo     (+)= CLI.co_modelo                                             "+
		"    AND MODELO.co_fabricante    = FABRICANTE.co_fabricante                                  "+
		"    AND ASS.sub_id              = CLI.nu_msisdn                                             ";
	private static Integer posicaoAtualNaRegua = new Integer(4);
	private String diaProcessado; 
	
	static ArquivoConfiguracaoGPP 			arquivoConfiguracao;
	private MapConfiguracaoGPP 				config;
	private static final SimpleDateFormat 	conversorDate 			= new SimpleDateFormat(Definicoes.MASCARA_DATE);
	private static final SimpleDateFormat 	conversorDataArquivo 	= new SimpleDateFormat(Definicoes.MASCARA_DAT_DIA);
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		long					idLog						Identificador de LOG.
	 */
	public RelatorioChurnProdutor(long logId)
	{
		super(logId, Definicoes.CL_RELATORIO_CHURN);
		
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Gerado o rel de churn do dia: ";
		// Lendo o arquivo de configuracao do GPP
		arquivoConfiguracao = ArquivoConfiguracaoGPP.getInstance();
	}
	
	//Implementacao de Produtor.
	
	/**
	 *	Inicia a execucao do processo de geração do Relatório de Churn
	 *
	 *	@param		String[]				params						Lista de parametros. Nao utilizado.
	 *	@throws		Exception
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		StringBuffer nomeArquivoCompleto = new StringBuffer();
		
		try
		{
			//Montando os parâmetros de entrada
			this.diaProcessado = conversorDate.format(conversorDate.parse(params[0]));
			Object[] parametros = {posicaoAtualNaRegua,this.diaProcessado,this.diaProcessado,posicaoAtualNaRegua};

			//Adquirindo a conecção
			this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
			// configuracao do GPP
			config = MapConfiguracaoGPP.getInstancia();
			
			//Montando o nome do arquivo
			nomeArquivoCompleto.append(arquivoConfiguracao.getDirRelChurn());				//Path
			nomeArquivoCompleto.append(System.getProperty("file.separator"));				//Separador
			nomeArquivoCompleto.append(config.getMapValorConfiguracaoGPP("NOM_REL_CHURN"));	//Prefixo do Arquivo
			nomeArquivoCompleto.append(conversorDataArquivo.format(conversorDate.parse(this.diaProcessado))); 	//Sufixo do Arquivo
			nomeArquivoCompleto.append(".csv");												//Formato do Arquivo
			
			String separador= ";";
			//Gerando o arquivo
			geraRelatorioEmArquivo(SQL, parametros, nomeArquivoCompleto.toString(), separador);
		}
		catch (Exception e)
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
	 * Gera um arquivo com base no SQL e parâmetros dadis no endereço dados. 
	 * @param SQL			Query a ser processada
	 * @param parametros	Parâmetros de entrada da query
	 * @param nomeArquivo	Path e nome do arquivo a ser salvo
	 * @param separador		Separador de colunas a ser usado no arquivo de saida
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private boolean geraRelatorioEmArquivo(String SQL, Object[] parametros, String nomeArquivo, String separador)
						throws GPPInternalErrorException
	{
		boolean result = false;
		ProcessadorResultadoCSV processador = new ProcessadorResultadoCSV();
		super.log(Definicoes.DEBUG, "ProdutorChurn.geraRelatorioEmArquivo", "Inicio");
		
		try
		{
			ResultSet rs = this.conexaoPrep.executaPreparedQuery(SQL, parametros, super.logId);
			processador.getResultadoComoArquivo(rs, nomeArquivo, separador);
			rs.close();
			result = true;
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "ProdutorChurn.geraRelatorioEmArquivo", "Excecao: " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
			super.log(Definicoes.DEBUG, "ProdutorChurn.geraRelatorioEmArquivo", "Fim retorno " + result);
		}
		
		return result;
	}
	
	/**
	 * Retorna para as threads consumidoras o proximo registro a ser processado. Retorna Nulo pois este é um processo só produtor
	 * 
	 * @param Object
	 *            params Lista de parametros. Nao utilizado.
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		return null;
	}
	
	/**
	 *	Finaliza o processo. 
	 *
	 *	@throws		Exception
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "ProdutorChurn.finish", "Inicio");
		
		try
		{
			this.mensagem = this.mensagem.concat(conversorDate.format(this.diaProcessado));
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "ProdutorChurn.finish", "Excecao: " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Excecao: " + e;
		}
		finally
		{
			//Liberando a conexao
			this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
			super.log(Definicoes.INFO, "Produtor.finish", "Fim");
		}
	}
	
	/**
	 *	Trata excecoes lancadas pelo produtor. Nao utilizado pelo processo. 
	 *
	 *	@throws		Exception
	 *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{
	}
	
	//Implementacao de ProcessoBatchProdutor.
	/**
	 *	Retorna o identificador do processo batch. 
	 *
	 *	@return		int													Identificador do processo batch.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_REL_CHURN;
	}
	
	/**
	 *	Retorna a mensagem informativa sobre a execucao do processo batch. 
	 *
	 *	@return		String					mensagem					Mensagem informativa sobre a execucao.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		return this.mensagem;
	}
	
	/**
	 *	Retorna o status da execucao do processo. 
	 *
	 *	@return		String					status						Status da execucao do processo.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso()
	{
		return this.status;
	}
	
	/**
	 *	Atribui o status da execucao do processo. 
	 *
	 *	@param		String					status						Status da execucao do processo.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status)
	{
		this.status = status;
	}
	
	/**
	 *	Retorna a data de processamento (data de referencia).
	 *  O processo retorna a data efetiva de execucao. 
	 *
	 *	@param		String												Data de execucao no formato dd/mm/yyyy.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
	/**
	 *	Retorna a conexao PREP para os consumidores.
	 *
	 *	@param		PREPConexao											Conexao PREP.
	 *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}
}
