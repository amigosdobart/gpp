package com.brt.gpp.aplicacoes.bloquear;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.ElementoXMLApr;

/**
* Esta classe � respons�vel por implementar o singleton respons�vel por
* coordenar o trabalho das threads
*    
* <P> Versao:        	1.0
*
* @Autor:            	Denys Oliveira
* Data:                 15/03/2005
*
* Modificado por:
* Data:
* Razao:
*
*/
public class AcumularSolicitacoesBloqueio 
{
	private int qtdBloqueios;			// quantidade de bloqueios efetuados
	private int qtdDesbloqueios;		// quantidade de desbloqueios efetuados
	private int qtdImpossivel;			// quantidade de solicita��es invi�veis
	private TreeMap tmAcoes;			// solicita��es que devem ser encaminhadas ao Aprovisionamento
	private HashMap hashBloqueios;		// hash com TODAS as solicita��es
	private Iterator itPonteiroHash;	// Ponteiro para a solicita��o a ser processada no hash
	private static AcumularSolicitacoesBloqueio acumuladorSolicitacoesBloqueio = null;		// referencia do pr�prio singleton
	private int numThreadsAtivas;		// N�mero de Threads ativas no momento
	private long qtdOsEnviadasHoje;		// N�mero de OS's enviadas hoje para o ASAP (at� o momento)
	private long limiteOsDesprezoDesbloqueio;	// Limite a partir do qual n�o mais ser�o enviadas OS's de desbloqueio
	
	/**
	 * Metodo...: AcumularSolicitacoesBloqueio
	 * Descricao: Construtor
	 */
	public AcumularSolicitacoesBloqueio()
	{
		// O construtor simplesmente inicializa os atributos dessa classe
		this.qtdBloqueios = 0;
		this.qtdDesbloqueios = 0;
		this.qtdImpossivel = 0;
		this.tmAcoes = new TreeMap();
		this.hashBloqueios = new HashMap();
		this.numThreadsAtivas = 0;
	}
	
	/**
	 * Metodo...: getInstancia
	 * Descricao: Retorna uma instancia desse Singleton
	 * @return	AcumularSolicitacaoBloqueio	Singleton dessa classe
	 */
	public static AcumularSolicitacoesBloqueio getInstancia()
	{
		// Se j� houver uma instancia dessa classe, n�o fazer outra
		if(acumuladorSolicitacoesBloqueio == null)
		{
			acumuladorSolicitacoesBloqueio = new AcumularSolicitacoesBloqueio();
		}
		return acumuladorSolicitacoesBloqueio;
	}
	
	/**
	 * Metodo...: getNumThreadsAtivas
	 * Descricao: Retorno o n�mero de threads ativas no momento
	 * @return	int		N�mero de threads ativas no momento
	 */
	public synchronized int getNumThreadsAtivas()
	{
		return this.numThreadsAtivas;
	}
	
	/**
	 * Metodo...: notificaInicioProcessamento
	 * Descricao: Quando uma thread come�a seu trabalho, esse m�todo � evocado
	 * 				afim de incrementar o atributo de n�mero de threads ativas 
	 */
	public synchronized void notificaInicioProcessamento()
	{
		this.numThreadsAtivas++;
	}
	
	/***
	 * Metodo...: notificaFimProcessamento
	 * Descricao: Quando uma Thread termina seu trabalho, esse m�todo � evocado
	 *				afim de decrementar o atributo de n�mero de Threads ativas
	 */
	public synchronized void notificaFimProcessamento()
	{
		this.numThreadsAtivas--;
	}
	
	/**
	 * Metodo...: getProximaSolicitacaoBloqueio
	 * Descricao: Retorna a pr�xima solicita��o de bloqueio/desbloqueio do hash de bloqueios
	 * @return		SolicitacaoBloqueio		solicita��o de bloqueio/desbloqueio
	 */
	public synchronized SolicitacaoBloqueio getProximaSolicitacaoBloqueio()
	{
		SolicitacaoBloqueio retorno = null;
		
		// Verifica se ainda h� alguma solicita��o a ser mandada
		if	( this.itPonteiroHash.hasNext() )
		{
			// Verifica se o limite de OS's j� foi ultrapassado
			if(this.qtdBloqueios + this.qtdDesbloqueios + this.qtdOsEnviadasHoje <= this.limiteOsDesprezoDesbloqueio)
			{
				retorno = (SolicitacaoBloqueio) this.itPonteiroHash.next();
			}
			else
			{
				// Se o limite foi ultrapassado, somente passar�o OS's de bloqueio
				retorno = (SolicitacaoBloqueio) this.itPonteiroHash.next();
				
				while(retorno.getAcao().equals("Desbloquear"))
				{
					if(this.itPonteiroHash.hasNext())
					{
						retorno = (SolicitacaoBloqueio) this.itPonteiroHash.next();	
					}
					else
					{
						retorno = null;
					}
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: setHashBloqueios
	 * Descricao: Carrega o Hash de Solicita��es Bloqueios/Desbloqueio para o singleton
	 * @param 	HashMap		hashBloqueios		Hash a ser carregado
	 */
	public void setHashBloqueios(HashMap hashBloqueios)
	{
		this.hashBloqueios = hashBloqueios;
		this.itPonteiroHash = hashBloqueios.values().iterator();
	}
	
	/**
	 * Metodo...: incrementaBloqueios
	 * Descri��o: Para cada bloqueio realizado com sucesso, incrementa o atributo que
	 * 				controla essa quantidade
	 */
	public synchronized void incrementaBloqueios()
	{
		this.qtdBloqueios++;
	}
	
	/**
	 * Metodo...: incrementaDesbloqueios
	 * Descri��o: Para cada desbloqueio realizado com sucesso, incrementa o atributo que
	 * 				controla essa quantidade
	 */
	public synchronized void incrementaDesbloqueios()
	{
		this.qtdDesbloqueios++;
	}
	
	/**
	 * Metodo...: incrementaImpossiveis
	 * Descri��o: Para cada solicita��o bloqueio/desbloqueio invi�vel , incrementa o atributo que
	 * 				controla essa quantidade
	 */
	public synchronized void incrementaImpossiveis()
	{
		this.qtdImpossivel++;
	}
	
	/**
	 * Metodo...: getQtdBloqueios
	 * Descricao: Retorna a Quantidade de Bloqueios realizados com sucesso at� o momento
	 * @return	int		Quantidade de Bloqueios realizados com sucesso at� o momento
	 */
	public synchronized int getQtdBloqueios()
	{
		return this.qtdBloqueios;
	}
	
	/**
	 * Metodo...: getQtdDesbloqueios
	 * Descricao: Retorna a Quantidade de Desbloqueios realizados com sucesso at� o momento
	 * @return	int		Quantidade de Desbloqueios realizados com sucesso at� o momento
	 */
	public synchronized int getQtdDesbloqueios()
	{
		return this.qtdDesbloqueios;
	}
	
	/**
	 * Metodo...: getQtdImpossiveis
	 * Descricao: Retorna a Quantidade de bloqueios/desbloqueios invi�veis at� o momento
	 * @return	int		Quantidade de bloqueios/desbloqueios invi�veis at� o momento
	 */
	public synchronized int getQtdImpossiveis()
	{
		return this.qtdImpossivel;
	}
	
	/**
	 * Metodo...: enfiaNoHash
	 * Descricao: Coloca No TreeMap a solicita��o que deve ser enviada para o Aprovisionamento
	 * @param 	String	msisdn		Msisdn cujo servi�o ser� bloqueado/desbloqueado
	 * @param 	String	servico		Servi�o que ser� bloqueado/desbloqueado	
	 * @param 	String	operacao	Indica se � um bloqueio/desbloqueio
	 */
	public synchronized void enfiaNoHash(String msisdn, String servico, String operacao)
	{
		tmAcoes.put(msisdn+servico, new ElementoXMLApr(servico, operacao, null, msisdn));
	}
	
	/**
	 * Metodo...: resetaSingleton
	 * Descricao: Limpa todos os atributos do singleton
	 * @param:	long[]		[0]: N�mero de OS's enviadas ao ASAP hoje at� o momento
	 * 						[1]: Limite para que as OS's de desbloqueio sejam desprezadas
	 */
	public void resetaSingleton(long[] limites)
	{
		this.tmAcoes.clear();
		this.hashBloqueios.clear();
		this.qtdBloqueios = 0;
		this.qtdDesbloqueios = 0;
		this.qtdImpossivel = 0;
		this.qtdOsEnviadasHoje = limites[0];
		this.limiteOsDesprezoDesbloqueio = limites[1];
	}
	
	/**
	 * Metodo...: getMapAcoes
	 * Descricao: Retorna o treeMap que acumula as solicita�oes a serem enviadas para o
	 * 				Aprovisionamento
	 * @return	TreeMap		TreeMap com as solicita��es a serem enviadas apra o ASAP
	 */
	public TreeMap getMapAcoes()
	{
		return this.tmAcoes;
	}
	/**
	 * @param limiteOsDesprezoDesbloqueio The limiteOsDesprezoDesbloqueio to set.
	 */
	public void setLimiteOsDesprezoDesbloqueio(long limiteOsDesprezoDesbloqueio) {
		this.limiteOsDesprezoDesbloqueio = limiteOsDesprezoDesbloqueio;
	}
	/**
	 * @param qtdOsEnviadasHoje The qtdOsEnviadasHoje to set.
	 */
	public void setQtdOsEnviadasHoje(long qtdOsEnviadasHoje) {
		this.qtdOsEnviadasHoje = qtdOsEnviadasHoje;
	}
}