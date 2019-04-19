package com.brt.gpp.aplicacoes.promocao.estornoExpurgoPulaPula;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.ManipuladorArquivos;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPromocaoOrigemEstorno;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pela carga dos arquivos de lotes (Estorno de Bonus Pula-Pula)
 *  para o banco de dados
 *  
 *  Parametro de entrada 0: endereço completo do arquivo de lote									 [obrigatorio]
 *  Parametro de entrada 1: endereço completo do arquivo de log de erro  								[opcional]
 *  Parametro de entrada 2: destino do arquivo de lote processado com sucesso (pasta+nome_do_arquivo)	[opcional]
 *  Parametro de entrada 3: destino do arquivo de lote processado com erro    (pasta+nome_do_arquivo)	[opcional]
 *  
 *  Obs:
 *  a) Parametros especificados na forma de string vaiza ("") sao desconsiderados
 *  b) Os parametros nao informados são preenchidos com os dados de configuração do Banco
 *  
 *  VIDE SINTAXE DO ARQUIVO DE LOTES NA CLASSE CargaLotesConsumidor.
 *
 *	@author	Bernardo Vergne Dias
 *	@since	02/01/2007
 *	@review_author 
 *	@review_date 09/05/2007
 */
public class CargaLotesProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private ManipuladorArquivos arquivoLote;
	private ManipuladorArquivos arquivoErroTemp;

	private String urlArquivoErro; 
	private String urlArquivoErroTemp; 
	private String urlArquivoLote; 
	private String urlArquivoLoteSucesso; 
	private String urlArquivoLoteErro; 
	
	/**
	 * Informa se houve erro no processo de carga do arquivo de lotes.
	 * Se valer 'true' o produtor executa 'rollback' no banco de dados
     * no final do processamento.
	 */
	private boolean erro;
	
	/**
     *	Conexão com o banco.
     */
	private PREPConexao conexaoPrep;
	
    /**
     *	Numero de registros processados.
     */
    private int numRegistros;
    
    /**
     *	Status de processamento.
     */
    private String status;
    
    /**
     *	Mensagem informativa sobre o resultado do processamento.
     */
    private String mensagem;
    
    /**
     *	Construtor da classe.
     *
     *	@param idLog Identificador de LOG.
     */
	public CargaLotesProdutor(long logId)
	{
		super(logId, Definicoes.CL_CARGA_LOTES_PRODUTOR);
		
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
		this.conexaoPrep 	= null;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");
		
		try
		{
			// configura os endereços dos arquivos de entrada/saída (atributos de nome urlArquivo*)
			determinaEnderecosArquivos(params);
			
			// seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    conexaoPrep.setAutoCommit(false);	
		    
		    // atualiza o mapping de OrigemEstorno
		    MapPromocaoOrigemEstorno.getInstance().reload();
		    
		    // abre o arquivo de lote
		    arquivoLote = new ManipuladorArquivos(urlArquivoLote, false, super.getIdLog()); 
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao ao iniciar o processo de carga de lotes: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao ao iniciar o processo de carga de lotes: " + e;
		    addMensagemErro("Ocorreu um erro interno no servidor. Não foi possível iniciar o processo de carga de lote.");
		    throw e;
		}
		
		super.log(Definicoes.INFO, "startup", "Fim");
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
	public synchronized Object next() throws Exception
	{
	    CargaLotesVO result = null;
	    String linha = null;
		
		try
		{
			linha = arquivoLote.leLinha();
			if (linha != null)
				result = new CargaLotesVO(linha, ++numRegistros);
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "next", "Excecao no processo de carga de lotes: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao no processo de carga de lotes: " + e;
		    addMensagemErro("Ocorreu um erro interno no servidor. O processo de carga de lote parou inesperadamente.");
		    return null;
		}
		
		return result;
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "finish", "Inicio");
		
		// Finaliza a transação com o banco de dados
		try
		{
			if (erro)	conexaoPrep.rollback();
			else		conexaoPrep.commit();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "finish", "Excecao ao finalizar o processo de carga de lotes: " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Excecao ao finalizar o processo de carga de lotes: " + e;
			addMensagemErro("Ocorreu um erro interno no servidor. Não foi possível finalizar o processo de carga de lote.");
		}

		try
		{
			if (arquivoLote != null) arquivoLote.fechaArquivo();
			if (arquivoErroTemp != null) arquivoErroTemp.fechaArquivo();
			
			if (erro == true)
			{
				// Move o arquivo de lote para a pasta de erros
				// e move o relatorio de erros (arquivo temporario) para a pasta de erros
				moverArquivo(urlArquivoLote, urlArquivoLoteErro);
				moverArquivo(urlArquivoErroTemp, urlArquivoErro);
			}
			else
			{
				// Move o arquivo de lote para a pasta de sucesso
				moverArquivo(urlArquivoLote, urlArquivoLoteSucesso);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "finish", "Excecao ao finalizar o processo de carga de lotes: " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Excecao ao finalizar o processo de carga de lotes: " + e;
		}
		
		try
		{
			// Libera a conexao
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "finish", "Excecao ao liberar conexao com o banco: " + e);
		}
		
		super.log(Definicoes.INFO, "finish", "Fim");
	}
   
	/**
	 * Acrescenta uma linha no buffer de erro
	 * @param str Frase
	 */
	public synchronized void addMensagemErro(String str)
	{
		erro = true;
		if (urlArquivoErroTemp == null) return;
		
		try 
		{
			if (arquivoErroTemp == null) criarArquivoErro(urlArquivoErroTemp);
			arquivoErroTemp.escreveLinha(str);
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "addMensagemErro", 
					"Erro ao tentar escrever no arquivo '" + urlArquivoErroTemp + "': " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Erro ao tentar escrever no arquivo '" + urlArquivoErroTemp + "': " + e;
		}
	}
	
	private void criarArquivoErro(String url) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try 
		{
			removerArquivo(url);
		    arquivoErroTemp = new ManipuladorArquivos(url,true,super.getIdLog()); 
		    super.log(Definicoes.INFO, "criarArquivoErro", "Arquivo criado: '"+url+"'");
		    
		    arquivoErroTemp.escreveLinha("LOG DE ERRO " + sdf.format(Calendar.getInstance().getTime()) 
		    		+ "\nReferência: " + getFileName(urlArquivoLote));
		}
		catch (Exception e)
		{
			
			throw e;
		}
	}
	
	private void removerArquivo(String url)
	{
		File f = new File(url);
		if (f.exists())
		{
			if (f.delete())
				super.log(Definicoes.INFO, "removerArquivo", "Arquivo removido: '"+url+"'");
			else
				super.log(Definicoes.WARN, "removerArquivo", "Nao foi possivel remover arquivo: '"+url+"'");
		}
	}
	
	private void moverArquivo(String origem, String destino)
	{
		try
		{
			removerArquivo(destino);
			if (ManipuladorArquivos.moveArquivo(new File(origem), destino) == 0)
				super.log(Definicoes.INFO, "moverArquivo", "Arquivo '"+origem+"' movido para '"+destino+"'.");
			else
				super.log(Definicoes.WARN, "moverArquivo", "Nao foi possivel mover '"+origem+"' para '"+destino+"'.");
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "moverArquivo", "Erro ao mover '"+origem+"' para '"+destino+"'. Excecao: " + e);
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Erro ao mover '"+origem+"' para '"+destino+"'. " + e;
		}
	}
	
	/**
	 * Determina os endereços completo dos arquivos de entrada e saída
	 * de acordo com os paramentros passados na inicializacao do processo
	 */
	private void determinaEnderecosArquivos(String[] params) throws Exception
	{
		MapConfiguracaoGPP config = MapConfiguracaoGPP.getInstance();
		
		String estornoExpurgoRaiz = config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_RAIZ");
		String estornoExpurgoSaida = config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_SAIDA");
		String estornoExpurgoErros = config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_ERROS");
		String estornoExpurgoTemp = config.getMapValorConfiguracaoGPP("ESTORNO_EXPURGO_TEMP");
		
		arquivoLote = null;
		arquivoErroTemp = null;
		
		// Endereço completo do arquivo de lote
		urlArquivoLote = params[0];
		
		// Endereço completo do arquivo de log de erro
		if ((params.length >= 2) && !(params[1].equals("")))
		{
			urlArquivoErro = params[1];
		}
		else
		{
			String nomeArquivoLote = getFileName(urlArquivoLote);	
			String nomeArquivoErroDefault = nomeArquivoLote.substring(0, nomeArquivoLote.lastIndexOf("."))
											+ "_Erros.txt";
			urlArquivoErro = estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoErros 
							+ java.io.File.separator + nomeArquivoErroDefault;
		}
		
		// Endereço completo do arquivo de log de erro temporario
		urlArquivoErroTemp = estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoTemp 
							 + java.io.File.separator + getFileName(urlArquivoErro);
		
		// Destino do arquivo de lote processado com sucesso
		if ((params.length >= 3) && !(params[2].equals("")))
		{
			urlArquivoLoteSucesso = params[2];
		}
		else
		{
			urlArquivoLoteSucesso = estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoSaida 
			+ java.io.File.separator + getFileName(urlArquivoLote);
		}
		
		// Destino do arquivo de lote processado com erro
		if ((params.length >= 4) && !(params[3].equals("")))
		{
			urlArquivoLoteErro = params[3];
		}
		else
		{
			urlArquivoLoteErro = estornoExpurgoRaiz + java.io.File.separator + estornoExpurgoErros 
			+ java.io.File.separator + getFileName(urlArquivoLote);
		}
	}
	
	/**
	 * Método que extrai o nome do arquivo dado seu endereço completo.
	 * 
	 * @param url
	 *            Endereço completo do arquivo,
	 */
	private String getFileName(String url) {
		int index = url.lastIndexOf("\\");
		if (index < 0)
			index = url.lastIndexOf("/");
		if (index < 0)
			return url;

		return url.substring(index + 1);
	}
	
	/**
    *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
    */
	public void handleException()
	{
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_CARGA_ARQUIVO_LOTES;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
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
	    return conexaoPrep;
	}
	
	/**
	 * @param val Erro
	 */
	public void setErro(boolean val)
	{
		erro = val;
	}
	
}
