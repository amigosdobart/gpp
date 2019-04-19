package com.brt.gpp.gerentesPool.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Stack;

/**
 *	Classe que implementa um pool de objetos. Os objetos podem ser inseridos ou retirados do pool. A partir da 
 *	insercao, o objeto representa um recurso que pode ser alocado. Quando um processo aloca um recurso, este nao pode
 *	ser alocado por outro processo, podendo ser utilizando somente pelo processo que o alocou. Apos a utilizacao, o
 *	objeto deve ser liberado, voltando a estar disponivel para outras alocacoes. 
 *
 *	E necessario observar que o metodo toCollection() retorna uma colecao com todos os objetos do pool, tornando-os 
 *	disponiveis. Este metodo deve ser utilizado com cuidado. Por exemplo, devera ser utilizado somente como 
 *	contingencia para liberar objetos presos, ou seja, objetos alocados e nao liberados por erro de implementacao.
 *
 *	Esta classe nao e thread-safe. Num ambiente multi-threaded deve ser utilizado um objeto SynchronizedPool, que 
 *	encapsula um objeto Pool e garante a exclusao mutua.
 * 
 *	@author		Daniel Ferreira
 *	@since		26/02/2007
 */
public class Pool 
{
	
	/**
	 *	Container de objetos do pool.
	 */
	private HashSet container; 
	
	/**
	 *	Lista de objetos disponiveis.
	 */
	private Stack disponiveis; 
	
	/**
	 *	Construtor da classe. Cria um pool vazio.
	 */
	public Pool()
	{
		this.container		= new HashSet();
		this.disponiveis	= new Stack();
	}
	
	/**
	 *	Construtor da classe. Cria um pool com os objetos fornecidos pela colecao. Caso haja objetos repetidos na
	 *	colecao, serao ignorados porque a implementacao de Set o garante.
	 *
	 *	@param		objetos					Colecao inicial de objetos do pool.
	 */
	public Pool(Collection objetos)
	{
		super();
		
		if(objetos != null)
		{
			objetos.remove(null);
			this.container.addAll(objetos);
			this.disponiveis.addAll(this.container);
		}
	}
	
	/**
	 *	Adiciona um objeto ao Pool.
	 *
	 *	@param		objeto					Objeto a ser adicionado que representa um recurso disponivel para alocacao.
	 */
	public void adicionar(Object objeto)
	{
		if(objeto != null)
		{
			this.container.add(objeto);
			
			if(!this.disponiveis.contains(objeto))
				this.disponiveis.push(objeto);
		}
	}
	
	/**
	 *	Retira o objeto do Pool. Caso o objeto nao seja integrante, o pool nao e alterado.
	 *
	 *	@param		objeto					Objeto integrante do pool a ser retirado.
	 */
	public void retirar(Object objeto)
	{
		this.container.remove(objeto);
		this.disponiveis.remove(objeto);
	}

	/**
	 *	Aloca o objeto para utilizacao como recurso por um processo.
	 *
	 *	@return		Objeto integrante do pool a ser utilizado.
	 */
	public Object alocar()
	{
		if(this.disponiveis.size() > 0)
			return this.disponiveis.pop();
		
		return null;
	}
	
	/**
	 *	Libera o objeto apos utilizacao como recurso por um processo. O pool nao e alterado se o objeto nao e integrante.
	 *
	 *	@return		Objeto integrante do pool a ser liberado.
	 */
	public void liberar(Object objeto)
	{
		if((this.container.contains(objeto)) && (!this.disponiveis.contains(objeto)))
			this.disponiveis.push(objeto);
	}
	
	/**
	 *	Indica se o objeto e um recurso do pool.
	 *
	 *	@param		objeto					Objeto a ser verificado.
	 *	@return		True se o objeto e um recurso do pool e false caso contrario.
	 */
	public boolean contem(Object objeto)
	{
		return this.container.contains(objeto);
	}
	
	/**
	 *	Indica se o objeto e um recurso do pool em uso.
	 *
	 *	@param		objeto					Objeto a ser verificado.
	 *	@return		True se o objeto e um recurso alocado do pool e false caso nao esteja ou se nao for um recurso do pool.
	 */
	public boolean isEmUso(Object objeto)
	{
		return (this.contem(objeto) && (!this.disponiveis.contains(objeto)));
	}
	
	/**
	 *	Retorna o numero de objetos integrantes do pool.
	 *
	 *	@return		Numero de objetos integrantes do pool.
	 */
	public int getTamanho()
	{
		return this.container.size();
	}
	
	/**
	 *	Retorna o numero de objetos disponiveis no pool.
	 *
	 *	@return		Numero de objetos disponiveis no pool.
	 */
	public int getTamanhoDisponiveis()
	{
		return this.disponiveis.size();
	}
	
	/**
	 *	Retorna o numero de objetos integrantes do pool que estao alocados por algum processo.
	 *
	 *	@return		Numero de objetos integrantes do pool que estao alocados por algum processo.
	 */
	public int getTamanhoEmUso()
	{
		return this.getTamanho() - this.getTamanhoDisponiveis();
	}

	/**
	 *	Retorna uma lista com todos os objetos integrantes do pool. E necessario observar que este metodo torna todos
	 *	os objetos do pool disponiveis. Este metodo deve ser utilizado com cuidado. Por exemplo, devera ser utilizado 
	 *	somente como contingencia para liberar objetos presos, ou seja, objetos alocados e nao liberados por erro de 
	 *	implementacao.
	 *
	 *	@return		Lista com todos os objetos do pool.
	 */
	public Collection toCollection()
	{
		return new ArrayList(this.container);
	}

}
