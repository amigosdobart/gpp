package com.brt.gpp.gerentesPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAdmin;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenVoucher;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DadosConexaoTecnomen;
import com.brt.gpp.comum.gppExceptions.GPPCorbaException;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.entidade.PoolSincronizado;

/**
 *	Gerente de Conexoes com a Tecnomen. E atraves deste singleton que as classes de aplicacao devem obter as conexoes
 *	no intuito de realizar operacoes relacionadas a assinantes na plataforma. As conexoes disponiveis sao: 
 *	TecnomenAprovisionamento, TecnomenRecarga, TecnomenVoucher, TecnomenAdmin. O numero de conexoes disponiveis esta 
 *	configurado no arquivo ConfiguracaoGPP.cfg. Objetos de cada uma destas classes sao armazenados em um container 
 *	para facilitar a implementacao, sendo o acesso feito atraves de constantes definidas no Gerente. Metodos de acesso 
 *	a objetos de cada uma sao fornecidos para garantir a compatibilidade com as aplicacoes.
 *
 *	A classe implementa Runnable para garantir o fechamento simultaneo das conexoes, sem o risco de finalizacao do GPP
 *	com conexoes disponiveis abertas.
 *
 *	@author		Daniel Ferreira
 *	@since		26/02/2007
 */
public final class GerentePoolTecnomen implements Runnable
{ 
	private static final String TECNOMEN_CONEXOES_APROVISIONAMENTO_MAXIMO = "TECNOMEN_CONEXOES_APROVISIONAMENTO_MAXIMO";
	private static final String TECNOMEN_CONEXOES_RECARGA_MAXIMO          = "TECNOMEN_CONEXOES_RECARGA_MAXIMO";
	private static final String TECNOMEN_CONEXOES_VOUCHER_MAXIMO          = "TECNOMEN_CONEXOES_VOUCHER_MAXIMO";
	private static final String TECNOMEN_CONEXOES_ADMIN_MAXIMO            = "TECNOMEN_CONEXOES_ADMIN_MAXIMO";

	/**
	 *	Instancia do singleton.
	 */
	private static GerentePoolTecnomen instance;
	
	/**
	 *	Container de pools de conexoes.
	 */
	private Map pools;
	
	/**
	 *	Iterator das chaves do container de pools para obtencao do proximo pool. Utilizado no processo de liberacao de 
	 *	conexoes.
	 */
	private Iterator iterator;
	
	/**
	 *	Gerente de Log. Utilizado para geracao de mensagens de log.
	 */
	private GerentePoolLog logger;
	
	/**
	 *	Numero de tentativas de operacoes com as conexoes.
	 */
	private int numTentativas;
	
	/**
	 *	Tempo de espera entre as tentativas de operacoes com as conexoes.
	 */
	private int tempoEspera;
	
	/**
	 *	Construtor da classe.
	 */
	private GerentePoolTecnomen()
	{
		//Criando o container de pools.
		this.pools = Collections.synchronizedMap(new TreeMap());
		this.iterator = null;
		
		//Criando os pools de conexoes.
		PoolSincronizado poolAprov = new PoolSincronizado();
		this.pools.put(new Integer(ConexaoTecnomen.APROVISIONAMENTO), poolAprov);
		PoolSincronizado poolRecarga = new PoolSincronizado();
		this.pools.put(new Integer(ConexaoTecnomen.RECARGA), poolRecarga);
		PoolSincronizado poolVoucher = new PoolSincronizado();
		this.pools.put(new Integer(ConexaoTecnomen.VOUCHER), poolVoucher);
		PoolSincronizado poolAdmin = new PoolSincronizado();
		this.pools.put(new Integer(ConexaoTecnomen.ADMIN), poolAdmin);
		PoolSincronizado poolAgent = new PoolSincronizado();
		this.pools.put(new Integer(ConexaoTecnomen.AGENT), poolAgent);
		
		//Obtendo o numero de tentativas e o tempo de espera.
		ArquivoConfiguracaoGPP configuracao = ArquivoConfiguracaoGPP.getInstance();
		this.numTentativas	= configuracao.getNumeroTentativasConexaoTecnomen();
		this.tempoEspera	= configuracao.getTempoEspera();
		
		this.logger = GerentePoolLog.getInstancia(this.getClass());
		this.criarPool();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 * 	@return		Instancia do singleton. 
	 */
	public static GerentePoolTecnomen getInstance()
	{
		if(GerentePoolTecnomen.instance == null)
			GerentePoolTecnomen.instance = new GerentePoolTecnomen();
		
		return GerentePoolTecnomen.instance;
	}
	
	/**
	 *	Retorna a instancia do singleton. Implementado para garantir compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador de processo. Nao utilizado.
	 * 	@return		Instancia do singleton. 
	 */
	public static GerentePoolTecnomen getInstancia(long idProcesso) 
	{
		return GerentePoolTecnomen.getInstance();
	}
	
	/**
	 *	Inicializa o Gerente, criando as conexoes e conectando com os servicos da Tecnomen.
	 */
	private void criarPool()
	{
		//Obtendo o numero de conexoes do arquivo de configuracoes do GPP.
		ArquivoConfiguracaoGPP configuracao = ArquivoConfiguracaoGPP.getInstance();
		int numConexoesAprov	= configuracao.getNumeroConexoesAprovisionamento();
		int numConexoesRecarga	= configuracao.getNumeroConexoesRecarga();
		int numConexoesVoucher	= configuracao.getNumeroConexoesVoucher();
		int numConexoesAdmin	= configuracao.getNumeroConexoesAdmin();
		
		//Inicializando as conexoes.
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criando Pool conexoes APROVISIONAMENTO: " + numConexoesAprov + " conexoes.");
		this.criarPool(ConexaoTecnomen.APROVISIONAMENTO, numConexoesAprov);
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criado Pool conexoes APROVISIONAMENTO com " + this.getPool(ConexaoTecnomen.APROVISIONAMENTO).getTamanho() +" conexoes.");
		
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criando Pool conexoes RECARGA: " + numConexoesRecarga + " conexoes.");
		this.criarPool(ConexaoTecnomen.RECARGA, numConexoesRecarga);
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criado Pool conexoes RECARGA com " + this.getPool(ConexaoTecnomen.RECARGA).getTamanho() + " conexoes.");
		
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criando Pool conexoes VOUCHER: " + numConexoesVoucher + " conexoes.");
		this.criarPool(ConexaoTecnomen.VOUCHER, numConexoesVoucher);
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criado Pool conexoes VOUCHER com " + this.getPool(ConexaoTecnomen.VOUCHER).getTamanho() + " conexoes.");

		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criando Pool conexoes ADMIN: " + numConexoesAdmin + " conexoes.");
		this.criarPool(ConexaoTecnomen.ADMIN, numConexoesAdmin);
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", 
						"Criado Pool conexoes ADMIN com " + this.getPool(ConexaoTecnomen.ADMIN).getTamanho() + " conexoes.");
		
		//Criando o iterator de obtencao dos pools.
		this.iterator = this.pools.keySet().iterator();
	}
	
	/**
	 *	Cria o pool de conexoes.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@param		quantidade				Numero minimo de conexoes a serem criadas no pool.
	 */
	private void criarPool(int tipo, int quantidade)
	{
		for(int i = 0; i < quantidade; i++)
		{
			try
			{
				this.criarConexao(tipo);
			}
			catch(Exception e)
			{
				this.logger.log(0, Definicoes.ERRO, Definicoes.CL_GERENTE_TECNOMEN, "criarPool", "Excecao ao criar a conexao: " + e);
			}
		}
	}

	/**
	 *	Cria e retorna a conexao de acordo com o tipo informado e a insere no pool.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@return		Conexao com o servico da Tecnomen.
	 *	@throws		GPPInternalErrorException, GPPTecnomenException
 	 */
	public ConexaoTecnomen criarConexao(int tipo) throws GPPInternalErrorException, GPPTecnomenException
	{
		ConexaoTecnomen result = null;
		ArquivoConfiguracaoGPP configuracao = ArquivoConfiguracaoGPP.getInstance();
		int numMaxConexoesAprov   = configuracao.getInt(TECNOMEN_CONEXOES_APROVISIONAMENTO_MAXIMO);
		int numMaxConexoesRecarga = configuracao.getInt(TECNOMEN_CONEXOES_RECARGA_MAXIMO);
		int numMaxConexoesVoucher = configuracao.getInt(TECNOMEN_CONEXOES_VOUCHER_MAXIMO);
		int numMaxConexoesAdmin   = configuracao.getInt(TECNOMEN_CONEXOES_ADMIN_MAXIMO);
		//Criando a conexao.
		switch(tipo)
		{// Caso tenha atingido o limite maximo do dado tipo, nao realiza operacao alguma.
			case ConexaoTecnomen.APROVISIONAMENTO:
				if(this.getNumeroConexoesAprovisionamento() >= numMaxConexoesAprov)
					return null;
				result = new TecnomenAprovisionamento();
				break;
			case ConexaoTecnomen.RECARGA:
				if(this.getNumeroConexoesRecarga() >= numMaxConexoesRecarga)
					return null;
				result = new TecnomenRecarga();
				break;
			case ConexaoTecnomen.VOUCHER:
				if(this.getNumeroConexoesVoucher() >= numMaxConexoesVoucher)
					return null;
				result = new TecnomenVoucher();
				break;
			case ConexaoTecnomen.ADMIN:
				if(this.getNumeroConexoesAdmin() >= numMaxConexoesAdmin)
					return null;
				result = new TecnomenAdmin();
				break;
			default: throw new GPPInternalErrorException("ID Servico Tecnomen invalido");
		}
		
		//Conectando e adicionando ao pool especifico.
		for(int i = 1; i <= this.numTentativas; i++)
		{
			try
			{
				result.conectar();
				this.getPool(tipo).adicionar(result);
				return result;
			}
			catch(Exception e)
			{
				result.fechar();
				
				this.logger.log(0, Definicoes.DEBUG, Definicoes.CL_GERENTE_TECNOMEN, "criarConexao", 
								"ID Servico Tecnomen: " + tipo + " - Excecao: " + e);
				
				//Caso o numero de tentativas tenha se esgotado, e necessario lancar a excecao.
				if((e instanceof GPPTecnomenException) && (i >= this.numTentativas))
					throw (GPPTecnomenException)e;
					
				//Caso nao consiga conectar, o servidor pode ter sido reiniciado. Neste caso e necessario que o 
				//Gerente de Autenticacao faca novamente o login no Servidor de Autenticacao antes de tentar 
				//novamente.
				GerenteAutenticadorTecnomen.getInstance().autenticar();
				
				try
				{
					Thread.sleep(this.tempoEspera*1000);
				}
				catch(Exception ignore){}
			}
		}
		
		//Lancar excecao caso nao haja conexao disponivel.
		throw new GPPTecnomenException("ID Servico Tecnomen: " + tipo + " - Nao foi possivel criar a conexao.");
	}
	
	/**
	 *	Cria uma conexao de Aprovisionamento com a plataforma Tecnomen. Implementado para garantir compatibilidade com 
	 *	versoes antigas.
	 *
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 *	@throws 	GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	 */
	public boolean criaTecnomenAprovisionamento() throws GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	{
		return this.criarConexao(ConexaoTecnomen.APROVISIONAMENTO) != null;
	}

	/**
	 *	Cria uma conexao de Recarga com a plataforma Tecnomen. Implementado para garantir compatibilidade com versoes 
	 *	antigas.
	 *
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 *	@throws 	GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	 */
	public boolean criaTecnomenRecarga() throws GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	{
		this.criarConexao(ConexaoTecnomen.RECARGA);
		return true;
	}
	
	/**
	 *	Cria uma conexao de Voucher com a plataforma Tecnomen. Implementado para garantir compatibilidade com versoes
	 *	antigas.
	 *
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 *	@throws 	GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	 */
	public boolean criaTecnomenVoucher() throws GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	{
		this.criarConexao(ConexaoTecnomen.VOUCHER);
		return true;
	}
	
	/**
	 *	Cria uma conexao de Admin com a plataforma Tecnomen. Implementado para garantir compatibilidade com versoes
	 *	antigas.
	 *
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 *	@throws 	GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	 */
	public boolean criaTecnomenAdmin() throws GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	{
		this.criarConexao(ConexaoTecnomen.ADMIN);
		return true;
	}

	/**
	 *	Cria uma conexao de Agent com a plataforma Tecnomen. Implementado para garantir compatibilidade com versoes
	 *	antigas. 
	 * 
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 *	@throws 	GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	 */
	public boolean criaTecnomenAgent() throws GPPInternalErrorException, GPPTecnomenException, GPPCorbaException
	{
		this.criarConexao(ConexaoTecnomen.AGENT);
		return true;
	}

	/**
	 *	Inicia o processo de liberacao da instancia da memoria. Para garantir que todas as conexoes disponiveis nos 
	 *	pools sejam fechadas, mesmo com a existencia de conexoes presas, sao disparadas threads de liberacao para cada 
	 *	pool.
	 */
	public void liberarInstancia()
	{
		//Criando as threads para liberar as conexoes.
		ArrayList threads = new ArrayList();
		for(int i = 0; i < this.pools.size(); i++)
		{
			Thread thread = new Thread(this, "FinalizacaoGerentePoolTecnomen");
			threads.add(thread);
			thread.start();
		}

		//Esperando a finalizacao das threads.
		while(threads.size() > 0)
		{
			try
			{
				Thread.sleep(this.tempoEspera*1000);
			}
			catch(Exception ignore){}
			
			for(Iterator iterator = threads.iterator(); iterator.hasNext();)
			{
				Thread thread = (Thread)iterator.next();
				
				if(!thread.isAlive())
					iterator.remove();
			}
		}
		
		this.logger.log(0, Definicoes.INFO, Definicoes.CL_GERENTE_TECNOMEN, "liberarPool", "Conexoes com a Tecnomen fechadas");
	}
	
	/**
	 *	Inicia o processo de liberacao da instancia da memoria. Implementado para garantir compatibilidade com versoes 
	 *	antigas.
	 */
	public void liberaInstancia(long idProcesso)
	{
		this.liberarInstancia();
	}
	
	/**
	 *	Destroi o pools de conexoes com a Tecnomen. Implementado para garantir compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 */
	public void destroiPool(long idProcesso)
	{
		this.liberarInstancia();
	}
	
	/**
	 *	Retira as conexoes do pool ate libera-lo completamente.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 */
	private void liberarPool(int tipo)
	{
		PoolSincronizado pool = this.getPool(tipo);
		
		try
		{
			while (pool.getTamanho() > 0)
				this.removerConexao(tipo);
		}
		catch(GPPInternalErrorException e)
		{
			this.logger.log(0, Definicoes.WARN, Definicoes.CL_GERENTE_GPP, "liberarPool", "Nao foi possivel remover todas as conexoes.");
		}
	}
	
	/**
	 *	Fecha uma conexao disponivel e a retira do pool. O metodo retira as conexoes de acordo com a sua 
	 *	disponibilidade, ou seja, fecha-a e a retira do pool quando estiver disponivel. Se houver alguma conexao presa, 
	 *	o Gerente espera que seja disponibilizada para fecha-la. Porem, apos um numero maximo de tentativas, e lancada 
	 *	uma excecao interna do GPP.
	 * 
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@throws		GPPInternalErrorException
	 */
	public void removerConexao(int tipo) throws GPPInternalErrorException 
	{
		ConexaoTecnomen conexao = this.getConexao(tipo, 0);
		PoolSincronizado pool = this.getPool(tipo);
		conexao.desalocarConexao();
		pool.retirar(conexao);
		conexao.fechar();
	}

	/**
	 *	Destroi uma conexao de Aprovisionamento com a plataforma Tecnomen. Implementado para garantir compatibilidade 
	 *	com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 */
	public boolean destroiConexaoTecnomenAprovisionamento(long idProcesso)
	{
		try
		{
			this.removerConexao(ConexaoTecnomen.APROVISIONAMENTO);
			return true;
		}
		catch(GPPInternalErrorException e)
		{
			return false;
		}
	}

	/**
	 *	Destroi uma conexao de Recarga com a plataforma Tecnomen. Implementado para garantir compatibilidade com 
	 *	versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 */
	public boolean destroiConexaoTecnomenRecarga(long idProcesso)
	{
		try
		{
			this.removerConexao(ConexaoTecnomen.RECARGA);
			return true;
		}
		catch(GPPInternalErrorException e)
		{
			return false;
		}
	}
	
	/**
	 *	Destroi uma conexao de Voucher com a plataforma Tecnomen. Implementado para garantir compatibilidade com 
	 *	versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 */
	public boolean destroiConexaoTecnomenVoucher(long idProcesso)
	{
		try
		{
			this.removerConexao(ConexaoTecnomen.VOUCHER);
			return true;
		}
		catch(GPPInternalErrorException e)
		{
			return false;
		}
	}

	/**
	 *	Destroi uma conexao de Admin com a plataforma Tecnomen. Implementado para garantir compatibilidade com versoes 
	 *	antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 */
	public boolean destroiConexaoTecnomenAdmin(long idProcesso)
	{
		try
		{
			this.removerConexao(ConexaoTecnomen.ADMIN);
			return true;
		}
		catch(GPPInternalErrorException e)
		{
			return false;
		}
	}

	/**
	 *	Destroi uma conexao de Agent com a plataforma Tecnomen. Implementado para garantir compatibilidade com versoes 
	 *	antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Indica se conseguiu criar ou nao a conexao.
	 */
	public boolean destroiConexaoTecnomenAgent(long idProcesso)
	{
		try
		{
			this.removerConexao(ConexaoTecnomen.AGENT);
			return true;
		}
		catch(GPPInternalErrorException e)
		{
			return false;
		}
	}
	
	/**
	 *	Executa o metodo de Runnable para o processo de liberacao do pool de conexoes. 
	 */
	public void run()
	{
		Integer chave = null;
		
		synchronized(this)
		{
			if(this.iterator.hasNext())
				chave = (Integer)this.iterator.next();
		}
		
		if(chave != null)
			this.liberarPool(chave.intValue());
	}
	
	/**
	 *	Retorna o pool de conexoes. 
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@return		Pool de conexoes com os servicos da Tecnomen.
	 */
	private PoolSincronizado getPool(int tipo)
	{
		PoolSincronizado result = (PoolSincronizado)this.pools.get(new Integer(tipo));
		
		if(result == null)
		{
			result = new PoolSincronizado();
			this.pools.put(new Integer(tipo), result);
		}
		
		return result;
	}

	/**
	 *	Aloca e retorna uma conexao para utilizacao por um processo de aplicacao. 
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@param		idProcesso				Identificador do processo requisitante.
	 *	@return		Conexoes com o servico da Tecnomen.
	 *	@throws		GPPInternalErrorException
	 */
	public ConexaoTecnomen getConexao(int tipo, long idProcesso) throws GPPInternalErrorException
	{
		PoolSincronizado pool = this.getPool(tipo);
		
		if(pool == null)
			throw new GPPInternalErrorException("ID Servico Tecnomen invalido");
		
		for(int i = 1; i <= this.numTentativas; i++)
		{
			ConexaoTecnomen result = (ConexaoTecnomen)pool.alocar();
			
			try
			{
				if(result != null)
				{
					result.testarConexao();
					result.alocarConexao(idProcesso);
					this.logger.log(idProcesso, Definicoes.DEBUG, Definicoes.CL_GERENTE_GPP, "getConexao", "Processo Requisitante: " + idProcesso + " - Conexao retornada com sucesso.");
					return result;
				}
				else
				{
					this.criarConexao(tipo);
					result = (ConexaoTecnomen)pool.alocar();

					if(result != null)
					{
						result.testarConexao();
						result.alocarConexao(idProcesso);
						this.logger.log(idProcesso, Definicoes.DEBUG, Definicoes.CL_GERENTE_GPP, "getConexao", "Processo Requisitante: " + idProcesso + " - Conexao retornada com sucesso.");
						return result;
					}
					
				}
			}
			catch(Exception e)
			{
				this.liberarConexao(result);
				this.logger.log(idProcesso, Definicoes.DEBUG, Definicoes.CL_GERENTE_GPP, "getConexao", "Processo Requisitante: " + idProcesso + " - Excecao ao testar a conexao: " + e);

				//Caso nao consiga conectar, o servidor pode ter sido reiniciado. Neste caso e necessario que o 
				//Gerente de Autenticacao faca novamente o login no Servidor de Autenticacao antes de tentar 
				//novamente.
				GerenteAutenticadorTecnomen.getInstance().autenticar();
			}
			
			//Caso nenhuma conexao esteja disponivel, e necessario esperar um tempo definido para tentar novamente.
			//Este e o mesmo periodo de espera durante as tentativas de obtencao de conexoes com a Tecnomen.
			this.logger.log(idProcesso, Definicoes.WARN, Definicoes.CL_GERENTE_GPP, "getConexao", "Processo Requisitante: " + idProcesso + " - Nao conseguiu conexao. Tentativa: " + i);
			
			try
			{
				Thread.sleep(this.tempoEspera*1000);
			}
			catch(InterruptedException ignore){}
		}
		
		//Lancar excecao caso nao haja conexao disponivel.
		throw new GPPInternalErrorException("ID Servico Tecnomen: " + tipo + " - Nao ha conexoes disponiveis.");
	}

	/**	
	 *	Retorna uma conexao de Aprovisionamento da Tecnomen. Implementado para garantir compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante.
	 *	@return		Conexao de Aprovisionamento com a Tecnomen.
	 *	@throws		GPPInternalErrorException 
	 */
	public TecnomenAprovisionamento getTecnomenAprovisionamento(long idProcesso) throws GPPInternalErrorException
	{
		return (TecnomenAprovisionamento)this.getConexao(ConexaoTecnomen.APROVISIONAMENTO, idProcesso);
	}
	
	/**	
	 *	Retorna uma conexao de Recarga da Tecnomen. Implementado para garantir compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante.
	 *	@return		Conexao de Recarga com a Tecnomen.
	 *	@throws		GPPInternalErrorException 
	 */
	public TecnomenRecarga getTecnomenRecarga(long idProcesso) throws GPPInternalErrorException
	{
		return (TecnomenRecarga)this.getConexao(ConexaoTecnomen.RECARGA, idProcesso);
	}
	
	/**	
	 *	Retorna uma conexao de Voucher da Tecnomen. Implementado para garantir compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante.
	 *	@return		Conexao de Voucher com a Tecnomen.
	 *	@throws		GPPInternalErrorException 
	 */
	public TecnomenVoucher getTecnomenVoucher(long idProcesso) throws GPPInternalErrorException
	{
		return (TecnomenVoucher)this.getConexao(ConexaoTecnomen.VOUCHER, idProcesso);
	}

	/**	
	 *	Retorna uma conexao de Admin da Tecnomen. Implementado para garantir compatibilidade com versoes antigas.
	 * 
	 *	@param		idProcesso				Identificador do processo requisitante.
	 *	@return		Conexao de Admin com a Tecnomen.
	 *	@throws 	GPPInternalErrorException 
	 */
	public TecnomenAdmin getTecnomenAdmin(long idProcesso) throws GPPInternalErrorException
	{
		return (TecnomenAdmin)this.getConexao(ConexaoTecnomen.ADMIN, idProcesso);
	}
	
	/**
	 *	Libera uma conexao apos utilizacao por um processo de aplicacao, voltando a ficar disponivel no pool. O metodo 
	 *	utiliza os dados da conexao Tecnomen, extraindo o identificador do servico para encontrar o pool de conexoes.
	 *	Caso a conexao nao seja da Tecnomen, sera lancada implicitamente a excecao ClassCastException. 
	 *
	 *	@param		conexao					Conexao com o servico da Tecnomen.
	 */
	public void liberarConexao(ConexaoTecnomen conexao)
	{
		PoolSincronizado pool = null;
		
		if(conexao != null)
			 pool = this.getPool(((DadosConexaoTecnomen)conexao.getDadosConexao()).getIdServico());
		
		if((pool != null) && (pool.contem(conexao)))
		{
			pool.liberar(conexao);
			conexao.desalocarConexao();
			this.logger.log(conexao.getDadosConexao().getIdProcesso(), Definicoes.DEBUG, Definicoes.CL_GERENTE_GPP, 
							"liberarConexao", "Conexao liberada pelo processo");
		}
		else if(conexao != null)
			this.logger.log(conexao.getDadosConexao().getIdProcesso(), 
							Definicoes.WARN, 
							Definicoes.CL_GERENTE_GPP, 
							"liberarConexao", "ID Servico Tecnomen: " + 
								((DadosConexaoTecnomen)conexao.getDadosConexao()).getIdServico() + 
								" - Conexao nao liberada pelo processo.");
	}
	
	/**
	 *	Metodo que forca a liberacao de conexoes da Tecnomen usadas por um processo.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@param		idProcesso				Identificador do processo utilizando as conexoes a serem liberadas.
	 */
	public void liberarConexoesEmUso(int tipo, long idProcesso)
	{
		PoolSincronizado pool = this.getPool(tipo);
		
		if(pool != null)
			for(Iterator iterator = pool.toCollection().iterator(); iterator.hasNext();)
			{
				ConexaoTecnomen conexao = (ConexaoTecnomen)iterator.next();
				
				if(conexao.getDadosConexao().getIdProcesso() == idProcesso)
					this.liberarConexao(conexao);
			}
	}
	
	/**
	 *	Metodo que forca a liberacao de todas conexoes da Tecnomen usadas por um processo.
	 *
	 *	@param		idProcesso				Identificador do processo utilizando as conexoes a serem liberadas.
	 */
	public void liberarConexoesEmUso(long idProcesso)
	{
		this.liberarConexoesEmUso(ConexaoTecnomen.APROVISIONAMENTO, idProcesso);
		this.liberarConexoesEmUso(ConexaoTecnomen.RECARGA         , idProcesso);
		this.liberarConexoesEmUso(ConexaoTecnomen.VOUCHER         , idProcesso);
		this.liberarConexoesEmUso(ConexaoTecnomen.ADMIN           , idProcesso);
		this.liberarConexoesEmUso(ConexaoTecnomen.AGENT           , idProcesso);
	}
	
	/**
	 *	Libera uma conexao de Aprovisionamento da plataforma Tecnomen. Implementado para garantir compatibilidade com 
	 *	versoes antigas.
	 *
	 *	@param		conexao					Conexao com o servico da Tecnomen.
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 */
	public void liberaConexaoAprovisionamento(TecnomenAprovisionamento conexao, long idProcesso)
	{
		this.liberarConexao(conexao);
	}
	   
	/**
	 *	Libera uma conexao de Recarga da plataforma Tecnomen. Implementado para garantir compatibilidade com versoes 
	 *	antigas.
	 *
	 *	@param		conexao					Conexao com o servico da Tecnomen.
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 */
	public void liberaConexaoRecarga(TecnomenRecarga conexao, long idProcesso)
	{
		this.liberarConexao(conexao);
	}

	/**
	 *	Libera uma conexao de Voucher da plataforma Tecnomen. Implementado para garantir compatibilidade com versoes
	 *	antigas.
	 *
	 *	@param		conexao					Conexao com o servico da Tecnomen.
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 */
	public void liberaConexaoVoucher(TecnomenVoucher conexao, long idProcesso)
	{
		this.liberarConexao(conexao);
	}
	
	/**
	 *	Libera uma conexao de Admin da plataforma Tecnomen. Implementado para garantir compatibilidade com versoes
	 *	antigas.
	 *
	 *	@param		conexao					Conexao com o servico da Tecnomen.
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 */
	public void liberaConexaoAdmin(TecnomenAdmin conexao, long idProcesso)
	{
		this.liberarConexao(conexao);
	}	   

	/**
	 *	Retorna o numero de conexoes de Aprovisionamento com a plataforma Tecnomen. Implementado para garantir 
	 *	compatibilidade com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Aprovisionamento com a plataforma Tecnomen.
 	 */
	public int getNumeroConexoesAprovisionamento()
	{
		return this.getPool(ConexaoTecnomen.APROVISIONAMENTO).getTamanho();
	}

	/**
	 *	Retorna o numero de conexoes de Aprovisionamento disponiveis com a plataforma Tecnomen. Implementado para
	 *	garantir compatibilidade com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Aprovisionamento disponiveis com a plataforma Tecnomen.
 	 */
	public int getNumeroConexoesAprovisionamentoDisponiveis()
	{
		return this.getPool(ConexaoTecnomen.APROVISIONAMENTO).getTamanhoDisponiveis();
	}
	
	/**
	 *	Retorna o numero de conexoes de Recarga com a plataforma Tecnomen. Implementado para garantir compatibilidade
	 *	com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Recarga com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesRecarga()
	{
		return this.getPool(ConexaoTecnomen.RECARGA).getTamanho();
	}	

	/**
	 *	Retorna o numero de conexoes de Recarga disponiveis com a plataforma Tecnomen. Implementado para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Recarga disponiveis com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesRecargaDisponiveis()
	{
		return this.getPool(ConexaoTecnomen.RECARGA).getTamanhoDisponiveis();
	}	
	
	/**
	 *	Retorna o numero de conexoes de Voucher com a plataforma Tecnomen. Implementado para garantir compatibilidade 
	 *	com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Voucher com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesVoucher()
	{
		return this.getPool(ConexaoTecnomen.VOUCHER).getTamanho();
	}	

	/**
	 *	Retorna o numero de conexoes de Voucher disponiveis com a plataforma Tecnomen. Implementado para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Voucher disponiveis com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesVoucherDisponiveis()
	{
		return this.getPool(ConexaoTecnomen.VOUCHER).getTamanhoDisponiveis();
	}	

	/**
	 *	Retorna o numero de conexoes Admin com a plataforma Tecnomen. Implementado para garantir compatibilidade com 
	 *	versoes antigas.
	 *
	 *	@return		Numero de conexoes Admin com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesAdmin()
	{
		return this.getPool(ConexaoTecnomen.ADMIN).getTamanho();
	}

	/**
	 *	Retorna o numero de conexoes Admin disponiveis com a plataforma Tecnomen. Implementado para garantir 
	 *	compatibilidade com versoes antigas.
	 *
	 *	@return		Numero de conexoes Admin disponiveis com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesAdminDisponiveis()
	{
		return this.getPool(ConexaoTecnomen.ADMIN).getTamanhoDisponiveis();
	}

	/**
	 *	Retorna o numero de conexoes de Agent com a plataforma Tecnomen. Implementado para garantir compatibilidade 
	 *	com versoes antigas.
	 *
	 *	@return		Numero de conexoes de Agent com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesAgent()
	{
		return this.getPool(ConexaoTecnomen.AGENT).getTamanho();
	}	

	/**
	 *	Retorna o numero de conexoes de Agent disponiveis com a plataforma Tecnomen. Implementado para garantir
	 *	compatibilidade com versoes antigas.

	 *	@return		Numero de conexoes de Agent disponiveis com a plataforma Tecnomen.
	 */
	public int getNumeroConexoesAgentDisponiveis()
	{
		return this.getPool(ConexaoTecnomen.AGENT).getTamanhoDisponiveis();
	}	

	/**
	 *	Verifica se a conexao esta valida. Implementado para garantir compatibilidade com versoes antigas. A nova
	 *	implementacao nao faz nada uma vez que o metodo nao e mais utilizado.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@param		conexao					Conexao com o servico da Tecnomen.
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@throws		GPPInternalErrorException
	 *	@deprecated	Utilizar Conexao.testarConexao()
	 */
	public void validaConexao(int tipo, ConexaoTecnomen conexao, long idProcesso) throws GPPInternalErrorException
	{
	}
	
	/**
	 *	Retorna as informacoes das conexoes em uso por processos.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 */
	public Collection getDadosConexoesEmUso(int tipo)
	{
		ArrayList result = new ArrayList();
		PoolSincronizado pool = this.getPool(tipo);
		
		if(pool != null)
			for(Iterator iterator = pool.toCollection().iterator(); iterator.hasNext();)
			{
				ConexaoTecnomen conexao = (ConexaoTecnomen)iterator.next();
				
				if(pool.isEmUso(conexao))
					result.add(conexao.getDadosConexao());
			}
		
		return result;
	}
	
	/**
	 *	Retorna a lista de processos que possuem conexoes em uso para Aprovisionamento. Implementada para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@param		tipo					Tipo da conexao, definido atraves das constantes da classe ConexaoTecnomen.
	 *	@return		Array de valores contendo a lista de processos que possuem conexoes em uso.
	 */
	public IdProcessoConexao[] getListaProcessosEmUso(int tipo)
	{
		Collection conexoesEmUso = this.getDadosConexoesEmUso(tipo);
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = conexoesEmUso.iterator(); iterator.hasNext();)
			result.add(((DadosConexaoTecnomen)iterator.next()).toIdProcessoConexao());
		
		return (IdProcessoConexao[])result.toArray(new IdProcessoConexao[0]);
	}
	
	/**
	 *	Retorna a lista de processos que possuem conexoes de Aprovisionamento em uso. Implementada para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Array de valores contendo a lista de processos que possuem conexoes em uso.
	 *	@throws		GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosAprovisionamento(long idProcesso) throws GPPInternalErrorException
	{
		return this.getListaProcessosEmUso(ConexaoTecnomen.APROVISIONAMENTO);
	}
	
	/**
	 *	Retorna a lista de processos que possuem conexoes de Recarga em uso. Implementada para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Array de valores contendo a lista de processos que possuem conexoes em uso.
	 *	@throws		GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosRecarga(long idProcesso) throws GPPInternalErrorException
	{
		return this.getListaProcessosEmUso(ConexaoTecnomen.RECARGA);
	}

	/**
	 *	Retorna a lista de processos que possuem conexoes de Voucher em uso. Implementada para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Array de valores contendo a lista de processos que possuem conexoes em uso.
	 *	@throws		GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosVoucher(long idProcesso) throws GPPInternalErrorException
	{
		return this.getListaProcessosEmUso(ConexaoTecnomen.VOUCHER);
	}

	/**
	 *	Retorna a lista de processos que possuem conexoes de Admin em uso. Implementada para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Array de valores contendo a lista de processos que possuem conexoes em uso.
	 *	@throws		GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosAdmin(long idProcesso) throws GPPInternalErrorException
	{
		return this.getListaProcessosEmUso(ConexaoTecnomen.ADMIN);
	}

	/**
	 *	Retorna a lista de processos que possuem conexoes de Agent em uso. Implementada para garantir
	 *	compatibilidade com versoes antigas.
	 *
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado.
	 *	@return		Array de valores contendo a lista de processos que possuem conexoes em uso.
	 *	@throws		GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosAgent(long idProcesso) throws GPPInternalErrorException
	{
		return this.getListaProcessosEmUso(ConexaoTecnomen.AGENT);
	}
	
}