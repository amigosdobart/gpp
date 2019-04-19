package com.brt.gpp.aplicacoes.recargaMassa;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.ManipuladorArquivos;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Classe responsavel pela importação dos arquivos de lotes (Recarga em massa) 
 *  para o banco de dados
 *  
 *  Parametro de entrada 0: endereço completo do arquivo de lote									 [obrigatorio]
 *  Parametro de entrada 1: endereço completo do arquivo com registros processados com sucesso			[opcional]
 *  Parametro de entrada 2: endereço completo do arquivo com registros processados com erro				[opcional]
 *  
 *  Obs:
 *  a) Parametros especificados na forma de string vazia ("") sao desconsiderados
 *  b) Os parametros nao informados são preenchidos com os dados padrão
 *      -> Pasta de sucesso/erro: de acordo com a configuração no banco
 *      -> Nome do arquivo: nome original do arquivo de lote + timestamp + extensao txt
 *  
 *  VIDE SINTAXE DO ARQUIVO DE LOTES NA CLASSE ImportacaoRecargaMassaConsumidor.
 *
 *	@author	Bernardo Vergne Dias
 *	Data:	09/08/2007
 */
public class ImportacaoRecargaMassaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Códigos de retorno para o usuario:
	public static final int ERRO_MOVER_ARQUIVO 		= 1;
	public static final int ERRO_REMOVER_ARQUIVO 	= 2;
	public static final int ERRO_CRIAR_ARQUIVO 		= 3;
	public static final int ERRO_LER_ARQUIVO 		= 4;
	public static final int ERRO_BANCO 				= 5;
	
	private ManipuladorArquivos arquivoLote;
	private ManipuladorArquivos arquivoErroTemp;
	private ManipuladorArquivos arquivoSucessoTemp;
	
	private String urlArquivoLote;			// Endereço completo
	private String nomeArquivoLote; 		// Apenas nome do arquivo (sem extensao)
	private String urlArquivoErro; 			// Endereço completo
	private String urlArquivoErroTemp; 		// Endereço completo
	private String urlArquivoSucesso;		// Endereço completo
	private String urlArquivoSucessoTemp;	// Endereço completo
	
	/**
	 * Informa se houve erro grave no processo de carga do arquivo de lotes.
	 * Se valer 'true' o produtor pára o processo batch.
	 */
	private boolean erroGrave;
	
	private PREPConexao conexaoPrep;		//	Conexão com o banco.
	private RecargaMassaDAO dao;		//  Métodos de acesso ao banco.
	private int numRegistros;				//	Numero de registros processados.
    private String status;					//	Status de processamento.
    private String mensagem;				//	Mensagem sobre o resultado do processamento.
    private int codRetorno;					//  Retorno funcional para o usuário.
    
    /**
     *	Construtor da classe.
     *
     *	@param idLog Identificador de LOG.
     */
	public ImportacaoRecargaMassaProdutor(long logId)
	{
		super(logId, Definicoes.CL_IMPORTACAO_RECARGA_MASSA_PRODUTOR);		
		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "";
		this.conexaoPrep 	= null;
		this.dao	= null;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
     */
	public void startup(String[] params) throws Exception
	{
		mensagem = "Processo iniciado. Zero registros processados.";
		
		arquivoLote = null;
		arquivoErroTemp = null;
		arquivoSucessoTemp = null;
		
		// configura os endereços dos arquivos de entrada/saída
		determinaEnderecosArquivos(params);
		
		// Abre o arquivo de lote
		try
		{
			arquivoLote = new ManipuladorArquivos(urlArquivoLote, false, super.getIdLog()); 
		}
		catch(Exception e)
		{
		    this.status = Definicoes.PROCESSO_ERRO;
		    super.log(Definicoes.ERRO, "startup", "Excecao ao iniciar o processo de carga de lotes: " + e);
		    registraErroGrave(true, ERRO_LER_ARQUIVO);
		}
		
		// seleciona conexão do pool Prep Conexão		
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    conexaoPrep.setAutoCommit(false);	
		}
		catch(Exception e)
		{
		    this.status = Definicoes.PROCESSO_ERRO;
		    super.log(Definicoes.ERRO, "startup", "Excecao ao iniciar o processo de carga de lotes: " + e);
		    registraErroGrave(true, ERRO_BANCO);
		}
		
	    dao = new RecargaMassaDAO(super.getIdLog());
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
	public synchronized Object next() throws Exception
	{
	    ImportacaoRecargaMassaVO result = null;
	    String linha = null;
	    
		if (erroGrave) 
			return null;
		
		try
		{
			linha = arquivoLote.leLinha();
			if (linha != null)
				result = new ImportacaoRecargaMassaVO(nomeArquivoLote, linha, ++numRegistros);
			mensagem = "Registros processados: " + numRegistros;
		}
		catch (Exception e)
		{
			this.status = Definicoes.PROCESSO_ERRO;
		    super.log(Definicoes.ERRO, "next", "Erro ao ler linha do arquivo de lote: " + e);
		    registraErroGrave(true, ERRO_LER_ARQUIVO);
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
		mensagem = "Processo concluido. " + numRegistros + " registros processados.";
		
		// Tenta registra um log para o usuario no caso de erro grave
		if (erroGrave)
		{
			try
			{
				if (arquivoErroTemp == null)
					arquivoErroTemp = criarArquivo(urlArquivoErroTemp);
				arquivoErroTemp.escreveLinha("Ocorreu erro no processamento desse lote. Código: " + codRetorno);
			}
			catch (Exception e)
			{
				super.log(Definicoes.ERRO, "finish", "Excecao ao finalizar carga de lotes: " + e);
			}
		}
		
		// Move os arquivos temporarios para seu destino final
		try
		{
			if (arquivoErroTemp != null) 
			{
				arquivoErroTemp.fechaArquivo();
				moverArquivo(urlArquivoErroTemp, urlArquivoErro);
			}
		
			if (arquivoSucessoTemp != null) 
			{
				arquivoSucessoTemp.fechaArquivo();
				moverArquivo(urlArquivoSucessoTemp, urlArquivoSucesso);
			}
		}
		catch (Exception e)	{}
		
		// Remove o arquivo de lotes
		try
		{
			if (arquivoLote != null) 
				arquivoLote.fechaArquivo();
			
			if (!erroGrave) 
				removerArquivo(urlArquivoLote);
		}
		catch (Exception e)	{}
		
		// Libera a conexao com o banco
		try
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "finish", "Excecao ao liberar conexao com o banco: " + e);
		}
		
		super.log(Definicoes.INFO, "finish", "Fim");
	}
	
	/**
	 * Escreve o registro no arquivo de ERRO ou SUCESSO (de acordo com o status de
	 * erro do registro)
	 * 
	 * @param registro
	 * @throws Exception
	 */
	public synchronized void gravarRegistroLote(ImportacaoRecargaMassaVO vo) throws Exception
	{
		if (vo.getMensagemErro() != null)
		{
			if (arquivoErroTemp == null)
				arquivoErroTemp = criarArquivo(urlArquivoErroTemp);
			
			arquivoErroTemp.escreveLinha(vo.toString());
			arquivoErroTemp.flush();
		}
		else
		{
			if (arquivoSucessoTemp == null)
				arquivoSucessoTemp = criarArquivo(urlArquivoSucessoTemp);
			
			arquivoSucessoTemp.escreveLinha(vo.toString());
			arquivoSucessoTemp.flush();
		}
	}
	
	/**
	 * Cria um arquivo (limpa o conteúdo caso o arquivo já existir)
	 * 
	 * @param url 		Endereço completo
	 * @throws Exception
	 */
	private ManipuladorArquivos criarArquivo(String url) throws Exception
	{
		ManipuladorArquivos arq = null;
		removerArquivo(url);
	    
		try 
		{
			arq = new ManipuladorArquivos(url,true,super.getIdLog()); 
		    super.log(Definicoes.INFO, "criarArquivo", "Arquivo criado: '"+url+"'");
		}
		catch (Exception e)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			super.log(Definicoes.ERRO, "criarArquivo", "Erro ao criar arquivo: '"+url+"': " + e);
			registraErroGrave(true, ERRO_CRIAR_ARQUIVO);
			throw e;
		}
		
		return arq;
	}
	
	/**
	 * Remove um arquivo se ele existir.
	 */
	private void removerArquivo(String url) throws Exception
	{
		try
		{
			File f = new File(url);
			if (f.exists())
			{
				f.delete();
				super.log(Definicoes.INFO, "removerArquivo", "Arquivo removido: '"+url+"'");
			}
		}
		catch (Exception e)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			super.log(Definicoes.ERRO, "finish", "Erro ao remover arquivo '" + url + "': " + e);
			registraErroGrave(true, ERRO_REMOVER_ARQUIVO);
			throw e;
		}
	}
	
	/**
	 * Move um arquivo de pasta. Se houver um arquivo com mesmo nome no destino, ele é substituido.
	 */
	private void moverArquivo(String origem, String destino) throws Exception
	{
		removerArquivo(destino);
		
		try
		{
			ManipuladorArquivos.moveArquivo(new File(origem), destino);
			super.log(Definicoes.INFO, "moverArquivo", "Arquivo '"+origem+"' movido para '"+destino+"'.");
		}
		catch (Exception e)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			super.log(Definicoes.ERRO, "moverArquivo", "Erro ao mover '"+origem+"' para '"+destino+"'. " + e);
			registraErroGrave(true, ERRO_MOVER_ARQUIVO);
			throw e;
		}
	}
	
	/**
	 * Determina os endereços completo dos arquivos de entrada e saída
	 * de acordo com os paramentros passados na inicializacao do processo
	 */
	private void determinaEnderecosArquivos(String[] params) throws Exception
	{
		MapConfiguracaoGPP config 	= MapConfiguracaoGPP.getInstance();
		SimpleDateFormat sdf 		= new SimpleDateFormat("yyyyMMddHHmmss");
		String sufixoDataHora 		= sdf.format(Calendar.getInstance().getTime());
		
		String recargaMassaRaiz = config.getMapValorConfiguracaoGPP("RECARGA_MASSA_RAIZ");
		String recargaMassaSaida = config.getMapValorConfiguracaoGPP("RECARGA_MASSA_SAIDA");
		String recargaMassaErros = config.getMapValorConfiguracaoGPP("RECARGA_MASSA_ERROS");
		String recargaMassaTemp = config.getMapValorConfiguracaoGPP("RECARGA_MASSA_TEMP");
		
		// Define endereço do arquivo de LOTE, ERRO e SUCESSO
		nomeArquivoLote =(new File(params[0])).getName();
		urlArquivoLote 		= params[0];
		urlArquivoErro 		= recargaMassaRaiz + java.io.File.separator + recargaMassaErros + java.io.File.separator 
								+ nomeArquivoLote.substring(0, nomeArquivoLote.lastIndexOf("."))
								+ "_Erros_" + sufixoDataHora + ".txt";
		urlArquivoSucesso 	= recargaMassaRaiz + java.io.File.separator + recargaMassaSaida + java.io.File.separator 
								+ nomeArquivoLote.substring(0, nomeArquivoLote.lastIndexOf("."))
								+ "_Sucesso_" + sufixoDataHora + ".txt";
		
		// Vefirica se os parametros 2 e 3 do processo batch foram especificados
		if ((params.length >= 2) && !(params[1].equals("")))
			urlArquivoErro = params[1];

		if ((params.length >= 3) && !(params[2].equals("")))
			urlArquivoSucesso = params[2];
		
		// Define os endereços temporários do arquivo de ERRO e SUCESSO
		
		urlArquivoErroTemp 		= recargaMassaRaiz + java.io.File.separator + recargaMassaTemp 
							 		+ java.io.File.separator + (new File(urlArquivoErro)).getName();
		urlArquivoSucessoTemp 	= recargaMassaRaiz + java.io.File.separator + recargaMassaTemp 
							 		+ java.io.File.separator + (new File(urlArquivoSucesso)).getName();
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
	    return Definicoes.IND_IMPORTACAO_RECARGA_MASSA;
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
	
	public RecargaMassaDAO getRecargaMassaDAO()
	{
		return dao;
	}
	
	/**
	 * Informa se houve erro grave no processo de carga do arquivo de lotes.
	 * Se valer 'true' o produtor pára o processo batch e tenta gravar no
	 * arquivo de erros o codigo de retorno.
	 * 
	 * @param val Erro
	 */
	public void registraErroGrave(boolean erroGrave, int codRetorno)
	{
		// Armazena apenas o codigo do primeiro erro grave
		
		if (!this.erroGrave)
		{
			this.erroGrave = erroGrave;
			this.codRetorno = codRetorno;
		}
	}
	
}
