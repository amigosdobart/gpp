package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;
 
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.operacaoArquivo.OperadorDeArquivo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Produtor de arquivos.
 *	<BR>Encontra uma série de arquivos de acordo com uma dada máscara em um dado path.</BR>
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/10 (yyyy/mm/dd)
 *
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descrição: Reestruturação completa, vários fixes
 *  Data: 15/10/2007
 */
public class ProdutorArquivo extends Aplicacoes implements ProcessoBatchProdutor
{

	/**
	 * Select para buscar o Path de trabalho, Mascara de arquivos, e o delimitador do
	 * arquivo de entrada.
	 */
    private static final String sql = 
							    "SELECT " +
								"	path_trabalho AS PATH, " +
								"	mascara_arquivo AS MASCARA, " +
								"	delimitador_arquivo AS DELIMITADOR " +
									"FROM TBL_REL_FABRICA_RELATORIO " +
								"WHERE	IDT_RELATORIO = ?";
	private int							numArquivos;
    private String						status;
    private	String						mensagem;
    private PREPConexao					conexaoPrep;

    private	String						idRelatorio;
    private	String						path;
    private	String						pathSaida;
    private	String						mascara;
    private String						delimitador;
    private String[]					arquivosEntrada;
	private String						pathEntrada;
	private String						pathHistorico;
	private String						pathLocal;
	private String						pathErro;
	private String						pathTemp;
    
	static ArquivoConfiguracaoGPP 			arquivoConfiguracao;

	public ProdutorArquivo(long logId)
	{
		super(logId, Definicoes.CL_PRODUTOR_ARQUIVO);
		
		this.numArquivos	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
		
		arquivoConfiguracao = ArquivoConfiguracaoGPP.getInstance();
	}
	
	/**
	 * Adquire os parâmetros de Delimitador, Máscara e path do banco de dados
	 * de acordo com o sql em <code>this.sql</code> 
	 * Este método adquire e retorna uma coneção com o banco de dados, pois só
	 * ele precida da conecção, nem mesmo os clientes.
	 */
	private void pegaParametrosDoBanco(String idtRelatorio) throws Exception
	{
		this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
		Object[] parametroSql ={idtRelatorio};
		ResultSet registros		= this.conexaoPrep.executaPreparedQuery(sql, parametroSql , 0);
		
		while (registros.next())
		{
			 this.delimitador = registros.getString("DELIMITADOR");
			 this.mascara = registros.getString("MASCARA");
			 this.pathLocal = registros.getString("PATH");
		}
		
		registros.close();
		this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep,super.logId);
	}
	
    /**
     *	Com base nos dados de entrada lista todos os arquivos dentro do path
     *	de entrada que respeitam a máscara de entrada.
     *	Monta também o <code>path</code>, <code>pathEntrada</code>,
     *	<code>pathHistorico</code>,<code>pathErro</code>,<code>pathSaida</code>.
     *	@param		params	Lista de parametros.[0]idRelatorio; [1]delimitador; [2]mascara;
     */
	public void startup(String[] params) throws Exception 
	{
		String fileSeparator = System.getProperty("file.separator");
		String entrada = "ENTRADA" + fileSeparator;
		String saida = "SAIDA" + fileSeparator;
		String historico = "HISTORICO" + fileSeparator;
		String erro = "ERRO" + fileSeparator;
		String temp = "TEMP" + fileSeparator;

		super.log(Definicoes.INFO, "ProdutorArquivo.startup", "Inicio");
		this.idRelatorio 	= params[0];
		pegaParametrosDoBanco(this.idRelatorio);
		StringBuffer pathTmp = new StringBuffer();
		pathTmp.append(arquivoConfiguracao.getDirRootFabricaRelatorio());
		pathTmp.append(fileSeparator);
		pathTmp.append(this.pathLocal);
		pathTmp.append(fileSeparator);
		
		this.path			= pathTmp.toString(); 
		this.pathEntrada	= this.path + entrada;
		this.pathHistorico 	= this.path + historico;
		this.pathErro 		= this.path + erro;
		this.pathSaida 		= this.path + saida;
		this.pathTemp		= this.path + temp;
		
		this.arquivosEntrada = OperadorDeArquivo.listaNomeArquivosDiretorio(this.pathEntrada,mascara);
	}
	
    /**
 	 * Retorna para as threads consumidoras o proximo registro a ser processado.
	 */
	public Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "ProdutorArquivo.next", "Inicio");
		String[] VO = null;
		if(this.numArquivos < this.arquivosEntrada.length)
		{
			
			VO = new String[8];
			VO[0] = this.idRelatorio;
			VO[1] = this.pathEntrada + this.arquivosEntrada[this.numArquivos];					// arquivo de entrada
			VO[2] = this.idRelatorio + "." + this.arquivosEntrada[this.numArquivos];			// nome do arquivo de saida
			VO[3] = this.idRelatorio + "." + this.arquivosEntrada[this.numArquivos] + ".xml";	// nome do arquivo de erro
			VO[4] = this.pathSaida; 															// pasta de saida
			VO[5] = this.pathErro; 																// pasta de erro
			VO[6] = this.pathTemp;																// pasta temporaria
			VO[7] = this.delimitador;

			this.numArquivos++;
	    }
		return VO;
	}
	
    /**
     * 	Finalização do processo
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "ProdutorArquivo.finish", "Inicio");
		try
		{
		    this.finalizacoes();
		    this.mensagem = this.mensagem.concat(String.valueOf(this.numArquivos));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "ProdutorArquivo.finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		finally
		{
			//Liberando a conexao
			// A conecção foi liberada no próprio método que a adquiriu,
			//pois os consumidores não fazem acesso ao banco de dados.
			//this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep,super.logId);
		    super.log(Definicoes.INFO, "ProdutorArquivo.finish", "Fim");
		}
	}

	/**
     *	Realiza as finalizacoes necessarias dentro do finish.
     *	Nao acrescenta as informacoes de log nos devidos arquivos.
     *	Move os arquivos processado do path de entrada para o path de histórioco.
     */
	private void finalizacoes() throws Exception
	{
		int size = this.arquivosEntrada.length;
		for (int i = 0 ; i < size ; i ++)
		{
			removerArquivo(this.pathHistorico + this.arquivosEntrada[i]);
			OperadorDeArquivo.moverArquivo(this.pathEntrada+this.arquivosEntrada[i],this.pathHistorico);
		}
	}
	
   /**
     *	Trata excecoes lancadas pelo produtor. 
     */
	public void handleException()
	{
	}
	
    /**
     *	Retorna o identificador do processo batch. 
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_FABRICA_ARQUIVO;
	}
	
    /**
     *	Retorna a mensagem informativa sobre a execucao do processo batch. 
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}
	
    /**
     *	Retorna o status da execucao do processo. 
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}
	
    /**
     *	Atribui o status da execucao do processo. 
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}
	
    /**
     *	Retorna a data de processamento (data de referencia).
     *  O processo retorna a data efetiva de execucao. 
     */
	public String getDataProcessamento()
	{
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
    /**
     *	Retorna a conexao PREP para os consumidores.
     */
	public PREPConexao getConexao()
	{
	    return this.conexaoPrep;
	}
    
	private void removerArquivo(String url)
	{
		File f = new File(url);
		if (f.exists())	f.delete();
	}
}