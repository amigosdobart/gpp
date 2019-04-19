// Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar;

// Imports Internos
import com.brt.gpp.aplicacoes.consultar.SolicitacaoRecarga;

// Imports Java
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
  *
  * Este arquivo contem a definicao da classe de DadosConsultaRecarga 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Camile Cardoso Couto
  * Data:               25/03/2002
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class DadosConsultaRecarga
{
	private Collection	recargas;		// Utilizado para armazenar um conjunto de objetos da classe SolicitacaoRecarga
	private String 	cpf;
	private short 	categoria;
	private String 	hashCartaoCredito;
	private String 	sistemaOrigem;
	private String  tipoTransacao;
	
	/**
	 * Metodo...: DadosConsultaRecarga
	 * Descricao: Construtor que inicializa o vetor de recargas
	 */
	public DadosConsultaRecarga()
	{
		this.recargas = new ArrayList();
		this.cpf = null;
		this.categoria = -1;
		this.hashCartaoCredito = null;
		this.sistemaOrigem = null;
	}
	
	/**
	 * Metodo...: getQtdMsisdnDistintos
	 * Descricao: Retorna o número de msisdn's distintos que estão dentro do vetor de recargas
	 * @return	int			Número de msisdn's distintos
	 */
	public int getQtdMsisdnDistintos()
	{
		HashMap listaMsisdn = new HashMap();
		
		// Os msisdn's repetidos serão todos sobrepostos pelo algoritmo de inserção do .put
		Iterator it = recargas.iterator();
		while(it.hasNext())
		{
			listaMsisdn.put(((SolicitacaoRecarga)it.next()).getMsisdn(),null);
		}
		
		// A quantidade de elementos do map será igual à quantidade de msisdn's distintos na consulta pré-recarga
		return listaMsisdn.size();
	}
	
	// Métodos Get                             
	/**
	 * Metodo...: getCpf
	 * Descricao: Busca o cpf para a consulta de recarga
	 * @param 
	 * @return	String 	- Retorna o cpf para a consulta de recarga
	 */
	public String getCpf() 
	{
		return this.cpf;
	}

	/**
	 * Metodo...: getCategoria
	 * Descricao: Busca a categoria para a consulta de recarga
	 * @param 
	 * @return	String 	- Retorna a categoria para a consulta de recarga
	 */
	public short getCategoria() 
	{
		return this.categoria;
	}
	
	/**
	 * Metodo...: getHashCartaoCredito
	 * Descricao: Busca o hash do Cartao de Credito para a consulta de recarga
	 * @param 
	 * @return	String 	- Retorna o hash do Cartao de Credito para a consulta de recarga
	 */
	public String getHashCartaoCredito() 
	{
		return this.hashCartaoCredito;
	}
	
	/**
	 * Metodo...: getSistemaOrigem
	 * Descricao: Busca o Sistema de Origem para a consulta de recarga
	 * @param 
	 * @return	String 	- Retorna o Sistema de Origem para a consulta de recarga
	 */
	public String getSistemaOrigem() 
	{
		return this.sistemaOrigem;
	}

	/**
	 * Metodo...: getTipoTransacao
	 * Descricao: Busca o Tipo de Transacao para a consulta de recarga
	 * @return Retorna o tipoTransacao.
	 */
	public String getTipoTransacao() 
	{
		return tipoTransacao;
	}
	

	// Métodos Set
	/**
	 * Metodo...: setCpf
	 * Descricao: Armazena o cpf para a consulta de recarga
	 * @param aCPF - O cpf para a consulta de recarga
	 * @return 
	 */
	public void setCpf(String aCPF) 
	{
		this.cpf = aCPF;
	}
	
	/**
	 * Metodo...: setCategoria
	 * Descricao: Armazena a categoria para a consulta de recarga
	 * @param aCategoria - A categoria para a consulta de recarga
	 * @return 
	 */
	public void setCategoria(short aCategoria) 
	{
		this.categoria = aCategoria;
	}

	/**
	 * Metodo...: setHashCartaoCredito
	 * Descricao: Armazena o hash do Cartao de Credito para a consulta de recarga
	 * @param aHashCC - O hash do Cartao de Credito para a consulta de recarga
	 * @return 
	 */
	public void setHashCartaoCredito(String aHashCC) 
	{
		this.hashCartaoCredito = aHashCC;
	}
	
	/**
	 * Metodo...: setSistemaOrigem
	 * Descricao: Armazena o sistema de Origem para a consulta de recarga
	 * @param aSistemaOrigem - O sistema de Origem para a consulta de recarga
	 * @return 
	 */
	public void setSistemaOrigem(String aSistemaOrigem) 
	{
		this.sistemaOrigem = aSistemaOrigem;
	}
	
	/**
	 * Metodo...: setTipoTransacao
	 * Descricao: Armazena o Tipo de Transacao para a consulta de recarga
	 * @param tipoTransacao - tipo de transacao para a consulta de recarga
	 */
	public void setTipoTransacao(String tipoTransacao)
	{
		this.tipoTransacao = tipoTransacao;
	}
	
	/**
	 * Metodo...: getRecarga
	 * Descricao: Retorna uma solicitação de recarga do vetor de solicitações
	 * @param 	int		indice		Índice da solicitação no Vector
	 * @return	SolicitacaoRecarga	Objeto com dados da solicitação de Recargas
	 */
	public SolicitacaoRecarga getRecarga(double indice)
	{
		Iterator it = recargas.iterator();
		SolicitacaoRecarga sol = null;
		while(it.hasNext())
		{
			if((sol = (SolicitacaoRecarga)it.next()).getIdValor() == indice)
				return sol;
		}
		return sol;		
	}
	
	/**
	 * Metodo...: setRecarga
	 * Descricao: Cria uma solicitação de recarga no Vetor de Solicitações
	 * @param 	long		idValorRecarga		Identificador da Recarga
	 * @param 	String		msisdn				Msisdn do receptor da recarga
	 */
	public void setRecarga(double idValorRecarga, String msisdn)
	{
		recargas.add(new SolicitacaoRecarga(idValorRecarga, msisdn));
	}
	
	
	/**
	 * @return Returns the recargas.
	 */
	public ArrayList getRecargas() 
	{
		return (ArrayList)recargas;
	}
	
	/**
	 * Metodo...: getMSISDN
	 * Descricao: Retorna o MSISDN da solicitacao de recarga de indice index no ArrayList recargas
	 * @param 	int			index				Identificador da Recarga
	 * @return 	String		msisdn				Msisdn do receptor da recarga
	 */
	public String getMSISDN(double index)
	{
		Iterator it = recargas.iterator();
		String msisdn = null;
		while(it.hasNext())
		{
			SolicitacaoRecarga sol = null;
			if((sol = (SolicitacaoRecarga)it.next()).getIdValor() == index)
				return sol.getMsisdn();
		}
		return msisdn;
	}
	
	/**
	 * Metodo...: getValorRecarga
	 * Descricao: Retorna o MSISDN da solicitacao de recarga de indice index no ArrayList recargas
	 * @param 	int			index				Identificador da Recarga
	 * @return 	String		msisdn				Msisdn do receptor da recarga
	 */
	public double getValorRecarga(double index)
	{
		Iterator it = recargas.iterator();
		double valor = 0.0;
		while(it.hasNext())
		{
			if((valor = ((SolicitacaoRecarga)it.next()).getIdValor()) == index)
				return valor;
		}
		return valor;
	}
}