//Definicao do Pacote
package com.brt.gpp.gerentesPool;

// Arquivos de Imports de Java/CORBA
import java.sql.SQLException;
import java.util.*;

// Arquivos de Imports do GPP
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.*;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.*;

/**
  *
  * Este arquivo contem a definicao da classe de gerente do Pool de conexoes com o banco de dados Oracle. 
  * Implementa a clase Singleton para o gerenciamento de conexoes.
  *
  * <P> Versao:        	1.0
  *
  * @Autor:            	Daniel Cintra Abib
  * Data:               28/02/2002
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class GerentePoolBancoDados
{
	private static final String GPP_CONEXOES_BANCO_MAXIMO = "GPP_CONEXOES_BANCO_MAXIMO";

	// Variaveis Membro
	private static GerentePoolBancoDados instancia = null;

	private static long	idLog					 = 0;
	private static int	numeroConexoesBancoDados = 0;
	
	// Vetores de conexoes
	private Vector  poolConexoesDisponiveis = null;
	private Vector  poolConexoesEmUso       = null;
	
	// Referencias a Servantes
	private static ArquivoConfiguracaoGPP 	arqConfiguracao = null;	// Referencia ao arquivo de configuracao
	private static GerentePoolLog   		gerenteLog 		= null;	// Referencia ao gerente de LOG


	/**
	 * Metodo...: GerentePoolBancoDados
	 * Descricao: Construtor 
	 * @param	
	 * @return	
	 */
    protected GerentePoolBancoDados()
    {
		// Cria e inicializa o vetor para armazenar as referencias das conexoes
		this.poolConexoesDisponiveis = new Vector();
		this.poolConexoesEmUso       = new Vector();
		
		gerenteLog      = GerentePoolLog.getInstancia(this.getClass());
		arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();		
    }

	/**
	 * Metodo...: GerentePoolBancoDados
	 * Descricao: Metodo para criar e retornar uma instancia da classe
	 * @param 	aIdProcesso				- Identificador do processo
	 * @return 	GerentePoolBancoDados  	- Referencia Unica para a classe
	 * @throws
	 */
	public static GerentePoolBancoDados getInstancia( long aIdProcesso )
	{
		if ( instancia == null )
			instancia = new GerentePoolBancoDados();
		
		return instancia;
	}

	/**
	 * Metodo...: GerentePoolBancoDados
	 * Descricao: Libera a instancia da memoraia
	 * 			  Atencao: Se uma instancia for liberada (existe apenas uma), nao sera possivel chamar
	 * 					   qualquer metodo... Qualquer chamada causara um NULL POINT EXCEPTION e por isso este metodo
	 * 					   so podera ser chamado pelo gerente do GPP
	 * @param 	
	 * @return 	
	 * @throws
	 */
	public void liberaInstancia()
	{
		// Libera o vertor de conexoes
		this.liberaPool();
	  	
		// Libera a instancia para que o garbage collector "limpe" a memoria
		instancia = null;
	}

	/**
	 * Metodo...: criaPool
	 * Descricao: Cria um pool de conexoes de banco de dados
	 * @param 	
	 * @return 	
	 * @throws	GPPInternalErrorException
	 */
	public void criaPool() throws GPPInternalErrorException
	{
		int conexoesASeremCriadas = arqConfiguracao.getNumeroConexoesBanco();
		boolean conBancoCriadas = false;
		
		try 	
		{	
			gerenteLog.log(idLog, Definicoes.INFO, Definicoes.GP_BANCO_DADOS, "criaPool", "Criando o Pool de conexoes de banco de dados (PREP): " + arqConfiguracao.getNumeroConexoesBanco() +" conexoes.");
			for ( int i = 0; i < conexoesASeremCriadas; i++ )
				criaConexaoBancoDados(idLog);
			conBancoCriadas = true;
			
			gerenteLog.log(idLog, Definicoes.INFO, Definicoes.GP_BANCO_DADOS, "criaPool", "Criado o Pool de conexoes de banco de dados com " + arqConfiguracao.getNumeroConexoesBanco() +" conexoes.");
		}
		catch ( GPPInternalErrorException e )
		{
			gerenteLog.log(idLog, Definicoes.FATAL, Definicoes.GP_BANCO_DADOS, "getInstancia", "ERRO no metodo criaPool:" + e );
 			throw new GPPInternalErrorException("Erro Criacao Pool Banco de Dados: "+e);
		}
		finally
		{
			// Se não foram criadas TODAS as conexões com o Banco de Dados
			if(!conBancoCriadas)
			{
				// Libera as conexões criadas
				this.destroiPool(idLog);
			}
		}
	}

	/**
	 * Metodo...: getNumeroConexoesBancoDados
	 * Descricao: Retorna o numero de conexoes de banco de dados
	 * @param 	
	 * @return 	int  - numero de conexoes de banco de dados
	 * @throws
	*/
	public int getNumeroConexoesBancoDados()
	{
		return numeroConexoesBancoDados;
	}

	/**
	 * Metodo...: getNumeroConexoesBancoDadosDisponiveis
	 * Descricao: Retorna o numero de conexoes disponiveis de banco de dados
	 * @param 	
	 * @return 	int  - numero de conexoes disponiveis de banco de dados
	 * @throws
	*/
	public int getNumeroConexoesBancoDadosDisponiveis()
	{
		return this.poolConexoesDisponiveis.size();
	}

	/**
	 * Metodo...: liberaPool
	 * Descricao: Libera as conexoes de banco de dados
	 * @param 	
	 * @return 	
	 * @throws
	 */
	public void liberaPool()
	{
		gerenteLog.log(idLog, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaPool", "Inicio");
		poolConexoesDisponiveis.addAll(poolConexoesEmUso);
		poolConexoesEmUso.removeAllElements();
		gerenteLog.log(idLog, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaPool", "Fim (conexoes liberadas)");
	}

	/**
	 * Metodo...: liberaPool
	 * Descricao: Retorna uma conexao de PREP
	 * @param 	aIdProcesso	- Identificador do processo
	 * @return  PREPConexao	- Uma conexao de banco de dados PREP
	 * @throws 	GPPInternalErrorException
	 */
	public PREPConexao getConexaoPREP(long aIdProcesso) throws GPPInternalErrorException
	{
		gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "getConexaoPREP", "getConexaoPREP");
		try
		{
			return (PREPConexao) this.getConexaoBancoDados(Definicoes.CO_TIPO_BANCO_DADOS_PREP, aIdProcesso);
		}
		catch ( GPPInternalErrorException e )                                                                            
		{			                                                                                           
			gerenteLog.log(aIdProcesso, Definicoes.ERRO, Definicoes.GP_BANCO_DADOS, "getConexaoPREP", "Erro no metodo getConexaoPREP: " + e);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);	
		}	
	}
	
	/**
	 * Remove uma conexao do pool
	 * @param aIdProcesso
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private Object removerConexaoDoPool(long aIdProcesso) throws GPPInternalErrorException
	{
		Object retConexao = poolConexoesDisponiveis.remove(0);
		if (retConexao != null)
		{
			/*  Verifica se a conexao disponivel esta viva, caso contrario 
			 *  tenta reconectar com o banco de dados.
			 */
			if ( !((PREPConexao)retConexao).estouVivo(aIdProcesso) )
				/* Caso a tentativa de reconectar a base de dados falhe
				 * entao uma excecao sera disparada para abandonar o processo
				 * porem a conexao (mesmo invalida) volta para o pool para posterior
				 * tentativa
				 */
				if ( !((PREPConexao)retConexao).reconecta(aIdProcesso) )
				{
					poolConexoesDisponiveis.add(retConexao);
					gerenteLog.log(aIdProcesso, Definicoes.WARN, Definicoes.GP_BANCO_DADOS, "getConexaoBancoDados", "Nao foi possivel reconectar com a base de dados.");
					throw new GPPInternalErrorException("Nao foi possivel reconectar com a base de dados.");
				}

			/* Ao conseguir a conexao, entao armazena o objeto no pool de 
			 * conexoes em Uso.
			 */
			try
			{
				((PREPConexao)retConexao).setAutoCommit(true);
			}
			catch(SQLException e)
			{
				// Nao ha tratamento para este erro.
			}

			poolConexoesEmUso.add(retConexao);
			((PREPConexao)retConexao).setIdProcesso(aIdProcesso);
			gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "getConexaoBancoDados", "Conexao retornada:" + retConexao + " para o Processo: " + aIdProcesso);
		}
		return retConexao;

	}
	/**
	 * Metodo...: getConexaoBancoDados
	 * Descricao: Pega uma conexao com o base de dados
	 * @param  tipoConexao	- Tipo de conexao
	 * @param  aIdProcesso	- Identificador do processo
	 * @return Object 		- Objeto de conexao a base de dados
	 * @throws GPPInternalErrorException
	 */
	protected synchronized Object getConexaoBancoDados( int tipoConexao, long aIdProcesso) throws GPPInternalErrorException
	{
		gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "getConexaoBancoDados", "getConexaoBancoDados");
		int  conTentativas = 0;
		int  nroTentativas = arqConfiguracao.getNumeroTentativasConexao();
		long tempoEspera   = arqConfiguracao.getTempoEspera() * 1000;
		Object retConexao  = null;

		while (conTentativas < nroTentativas)
		{
			if (poolConexoesDisponiveis.size() > 0)
			{
				retConexao = this.removerConexaoDoPool(aIdProcesso);
				if (retConexao != null)
					return retConexao;
			}
			else // Nao ah mais coneccoes disponiveis
			{
				this.criaConexaoBancoDados(aIdProcesso);
				if (poolConexoesDisponiveis.size() > 0)
				{
					retConexao = this.removerConexaoDoPool(aIdProcesso);
					if (retConexao != null)
						return retConexao;
				}
			}

			try
			{
				gerenteLog.log(aIdProcesso, Definicoes.WARN, Definicoes.GP_BANCO_DADOS, "getConexaoBancoDados", "Processo: " + aIdProcesso + " nao conseguiu conexao - Vai dormir... -> Processos utilizando conexoes: " + getStringListaProcessos(aIdProcesso));
				this.wait(tempoEspera);
				conTentativas++;
			}
			catch ( java.lang.InterruptedException javaException )
			{
				gerenteLog.log(aIdProcesso, Definicoes.FATAL, Definicoes.GP_BANCO_DADOS, "getConexaoBancoDados", "Erro em Thread Java - Metodo Thread.slepp " + javaException );
			}	
		}
		if (retConexao == null)
		{
			gerenteLog.log(aIdProcesso, Definicoes.WARN, Definicoes.GP_BANCO_DADOS, "getConexaoBancoDados", "Nao ha conexao de Banco de Dados disponivel.");
			throw new GPPInternalErrorException ("Nao ha conexao de Banco de Dados disponivel.");
		}	
		return retConexao;
	}
	
	/**
	 * Metodo...: liberaConexao
	 * Descricao: Pega uma conexao com a base de dados
	 * @param  aTipoConexao	- Tipo de conexao
	 * @param  aIdProcesso	- Identificador do processo
	 * @param  aConexao		- Instancia da conexao com a base de dados
	 * @return boolean		- True se liberou a conexao com sucesso, false caso contrario
	 * @throws
	 */
	private synchronized boolean liberaConexao( int aTipoConexao, Object aConexao, long aIdProcesso )
	{
		gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaConexao", "Inicio TIPCONEXAO "+aTipoConexao);
		boolean liberouConexao = poolConexoesEmUso.remove(aConexao);
		if (liberouConexao)
		{
			// Faz a liberacao do codigo do processo no objeto de conexao
			// e fecha as referencias aos objetos Statement (cursores) antes
			// de liberar a conexao
			((PREPConexao)aConexao).setIdProcesso(-1);
			((PREPConexao)aConexao).liberaConexao(aIdProcesso);
			
			poolConexoesDisponiveis.add(aConexao);
			this.notify();
			gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaConexao", "Conexao de banco de dados PREP liberada. " + aConexao + " para o Processo: " + aIdProcesso);
		}
		else
			gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaConexao", "Conexao de banco de dados PREP Nao foi liberada para o Processo: " + aIdProcesso);

		gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaConexao", "Fim");
		return liberouConexao; 
	}
	
	/**
	 * Metodo...: liberaConexaoPREP
	 * Descricao: Libera uma conexao de banco de dados do tipo PREP
	 * @param  aConexao 	- Objeto de Conexao a ser liberado
	 * @param  aIdProcesso	- Id do processo que esta liberando a conexao
	 * @return boolean		- True se conseguiu liberar a conexao, false caso contrario
	 * @throws
	 */
	public boolean liberaConexaoPREP ( Object aConexao, long aIdProcesso )
	{
		return this.liberaConexao( Definicoes.CO_TIPO_BANCO_DADOS_PREP, aConexao, aIdProcesso );
	}

	/**
	 * Metodo...: destroiConexaoPREP
	 * Descricao: Destroi uma conexao com de banco de dados (PREP)
	 * @param  aIdProcesso	- Id do processo que esta destroindo a conexao
	 * @return boolean		- True se conseguiu destroir a conexao, false caso contrario
	 * @throws
	 */
	public boolean destroiConexaoPREP (long aIdProcesso)
	{
		return this.destroiConexao(Definicoes.CO_TIPO_BANCO_DADOS_PREP, aIdProcesso);
	}
	
	/**
	 * Metodo...: destroiConexao
	 * Descricao: Este metodo destroi uma conexao disponivel do pool
	 * @param aIdProcesso 	- Id do processo que deseja destruir a conexao
	 * @param aTipConexao 	- Tipo da conexao
	 * @return boolean		- True se conseguiu destroir a conexao, false caso contrario
	 * @throws
	 */
	private synchronized boolean destroiConexao (long aTipConexao, long aIdProcesso)
	{
		boolean conseguiu = false;
		ConexaoBancoDados conBD = (ConexaoBancoDados)poolConexoesDisponiveis.remove(0);
		if ( conBD != null )
		{
			conBD.liberaConexao(aIdProcesso);
			gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "destroiConexao", "Conexao " + conBD + " de banco de dados foi destruida pelo processo " + aIdProcesso);
			conseguiu = true;
			numeroConexoesBancoDados--;	
		}
		return conseguiu;
	}

	/**
	 * Metodo...: destroiPool
	 * Descricao: Destroi todo o pool de conexoes de banco de dados
	 * @param aIdProcesso 	- Id do processo que deseja destruir o pool de conexoes
	 * @return boolean		- True se conseguiu destroir o pool, false caso contrario
	 * @throws
	 */
	public synchronized boolean destroiPool (long aIdProcesso)
	{
		boolean retorno = true;
		gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "destroiPool", "Inicio");

		// Realiza primeiro a liberacao de todas as conexoes em uso
		this.liberaPool();
		for (Iterator i = poolConexoesDisponiveis.iterator(); i.hasNext();)
		{
			ConexaoBancoDados conBD = (ConexaoBancoDados)i.next();
			conBD.liberaConexao(aIdProcesso);
			i.remove();
			numeroConexoesBancoDados--;
			gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "destroiPool", "Conexao " + conBD + " destruida.");
			continue;
		}

		if (getNumeroConexoesBancoDados()!=0)
			retorno = false;

		gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "destroiPool", "Fim - Numero de conexões apos liberacao: " + getNumeroConexoesBancoDados());
		return retorno;
	}

	/**
	 * Metodo...: criaConexaoBancoDados
	 * Descricao: Cria uma conexao de Banco de Dados (PREP)
	 * @param  aIdProcesso 	- Id do processo que deseja criar conexao
	 * @return boolean		- True se conseguiu criar a conexao, false caso contrario
	 * @throws GPPInternalErrorException
	 */
	public synchronized boolean criaConexaoBancoDados (long aIdProcesso) throws GPPInternalErrorException
	{
		boolean conseguiu=false;
		ArquivoConfiguracaoGPP configuracao = ArquivoConfiguracaoGPP.getInstance();
		int numMaxConexoesBanco   = configuracao.getInt(GPP_CONEXOES_BANCO_MAXIMO);

		ConexaoBancoDados conexao = null;
		if(this.getNumeroConexoesBancoDados() >= numMaxConexoesBanco) // Caso nao estourou o numero maximo de conexoes
			return false; // Atingui limite maximo de coneccoes, portanto nao conseguiu
		else
			conexao = new PREPConexao (aIdProcesso);
		
		if (conexao != null)
		{
			gerenteLog.log(aIdProcesso, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "criaConexao", "Conexao de Banco de Dados (PREP) CRIADA:" + conexao);
			poolConexoesDisponiveis.add(conexao);
			numeroConexoesBancoDados++;
			conseguiu=true;
		}
		else
			gerenteLog.log(aIdProcesso, Definicoes.ERRO, Definicoes.GP_BANCO_DADOS, "criaConexaoBancoDados", "ERRO ao criar a conexao com banco de dados.");

		return conseguiu;
	}
	
	/**
	 *	Retorna uma lista com informacoes sobre todas as conexoes. OBS: NAO e Thread-safe.
	 *
	 *	@return		Lista com informacoes por conexao. 
	 */
	public Collection getDadosConexoes()
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = this.poolConexoesDisponiveis.iterator(); iterator.hasNext();)
			result.add(((ConexaoBancoDados)iterator.next()).getDadosConexao());

		for(Iterator iterator = this.poolConexoesEmUso.iterator(); iterator.hasNext();)
			result.add(((ConexaoBancoDados)iterator.next()).getDadosConexao());

		return result;	
	}
	
	/**
	 * Metodo...: getListaProcessos
	 * Descricao: Metodo que retorna uma string contendo os numeros dos Ids de processo 
	 * 			  que estao utilizando conexoes juntamente com a data inicial de uso dessas.
	 * @param  aIdProcesso 			- Id do processo que deseja obter a lista
	 * @return IdProcessoConexao[] 	- Array de valores contendo a lista de numeros dos Ids de processo que estao utilizando conexoes 
	 * @throws GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessos(long aIdProcesso) throws GPPInternalErrorException
	{
		java.text.SimpleDateFormat dataFormatada = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Collection listaProcessos = new Vector();
		try
		{
			/* Monta a string resultado a partir da iteracao no pool de conexao em uso
			 * buscando para cada conexao qual o valor do Id do processo e da data
			 * inicial de uso dessa conexao
			 */ 
			for (Iterator i = poolConexoesEmUso.iterator(); i.hasNext();)
			{
				ConexaoBancoDados conBD = (ConexaoBancoDados)i.next();
				listaProcessos.add(new IdProcessoConexao((short)conBD.getIdProcesso(),
														 dataFormatada.format(conBD.getDataInicialUso())) );
			}
		}
		catch(Exception e)
		{
			gerenteLog.log(aIdProcesso, Definicoes.ERRO, Definicoes.GP_BANCO_DADOS, "getListaProcessos", "Erro Interno GPP: " + e);
			throw new GPPInternalErrorException(e.getMessage());
		}
		return (IdProcessoConexao[])listaProcessos.toArray(new IdProcessoConexao[0]);	
	}
	
	/**
	 * Metodo...: getListaIdsProcessos
	 * Descricao: Metodo que retorna a lista de todos os Id's de processos que estao utilizando conexoes
	 * @param  aIdProcesso 	- Id do processo que deseja obter a lista
	 * @return long[] 		- Array de valores contendo a lista de ids que estao utilizando conexoes
	 * @throws GPPInternalErrorException
	 */
	public long[] getListaIdsProcessos(long aIdProcesso) throws GPPInternalErrorException
	{
		long[] lista = new long[poolConexoesEmUso.size()];
		try
		{
			int count=0;
			//Busca somente o Id do processo de cada conexao no pool de conexoes em Uso
			for (Iterator i=poolConexoesEmUso.iterator(); i.hasNext();)
				lista[count++] = ((ConexaoBancoDados)i.next()).getIdProcesso();
		}
		catch(Exception e)
		{
			gerenteLog.log(aIdProcesso, Definicoes.ERRO, Definicoes.GP_BANCO_DADOS, "getListaProcessos", "Erro Interno GPP: " + e);
			throw new GPPInternalErrorException(e.getMessage());
		}
		return lista;
	}

	/**
	 * Metodo...: getStringListaProcessos
	 * Descricao: Metodo para listar os ids de processos que estao utilizando 
	 * 			  conexoes em um formato string. Ex: [1,2,3]
	 * @param  aIdProcesso 	- Id do processo que deseja obter a lista
	 * @return String 		- Lista dos ids de processos que utilizam conexoes no momento
	 * @throws GPPInternalErrorException
	 */	
	public String getStringListaProcessos(long aIdProcesso) throws GPPInternalErrorException
	{
		String resultado = "[";
		long[] lista = getListaIdsProcessos(aIdProcesso);
		for (int i=0; i < lista.length; i++)
			resultado += lista[i] + (i != lista.length-1 ? "," : "");

		resultado += "]";
		return resultado;
	}
	
	/**
	 * Metodo....:liberarConexaoEmUso
	 * Descricao.:Metodo que força a liberação de conexoes de BD usadas por um processo
	 * @param idProcesso Identificador do processo que terá suas conexoes liberadas
	 */
	public synchronized void liberarConexaoEmUso(long idProcesso)
	{
		boolean retorno = false;
		Object[] conexoesEmUso = poolConexoesEmUso.toArray();
		for(int i = 0; i < conexoesEmUso.length; i++)
		{
			ConexaoBancoDados conexao = (ConexaoBancoDados) conexoesEmUso[i];
			if(conexao.getIdProcesso() == idProcesso)
			{
				retorno = liberaConexao(Definicoes.CO_TIPO_BANCO_DADOS_PREP, conexao, idProcesso);
			}
		}
		if(!retorno)
			gerenteLog.log(idLog, Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, "liberaConexaoEmUso", "Nao ha conexao de banco de dados a ser liberada para o processo: " + idProcesso);
	}
}