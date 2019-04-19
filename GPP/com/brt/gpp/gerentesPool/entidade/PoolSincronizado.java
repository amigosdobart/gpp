package com.brt.gpp.gerentesPool.entidade;

import java.util.Collection;

import com.brt.gpp.gerentesPool.entidade.Pool;

/**
 *	Implementacao de Pool com garantia de exclusao mutua. Deve ser utilizado no lugar de Pool em um contexto multi-threaded.
 *
 *	@author		Daniel Ferreira
 *	@since		26/02/2007
 */
public class PoolSincronizado extends Pool  
{

	/**
	 *	Sincronizador para garantia de exclusao mutua.
	 */
	private Object mutex;

	/**
	 *	Construtor da classe. Inicializa o mutex.
	 */
	public PoolSincronizado()
	{
		super();
		
		this.mutex = new Object();
	}
	
	/**
	 *	Construtor da classe. Constroi um objeto Pool a partir da colecao passada por parametro e inicializa o mutex.
	 *
	 *	@param		objetos					Colecao inicial de objetos do pool.
	 */
	public PoolSincronizado(Collection objetos)
	{
		super(objetos);
		
		this.mutex = new Object();
	}

	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#adicionar(Object)
	 */
	public void adicionar(Object objeto)
	{
		synchronized(this.mutex)
		{
			super.adicionar(objeto);
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#retirar(Object)
	 */
	public void retirar(Object objeto)
	{
		synchronized(this.mutex)
		{
			super.retirar(objeto);
		}
	}

	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#alocar()
	 */
	public Object alocar()
	{
		synchronized(this.mutex)
		{
			return super.alocar();
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#liberar(Object)
	 */
	public void liberar(Object objeto)
	{
		synchronized(this.mutex)
		{
			super.liberar(objeto);
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#contem(Object)
	 */
	public boolean contem(Object objeto)
	{
		synchronized(this.mutex)
		{
			return super.contem(objeto);
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#isEmUso(Object)
	 */
	public boolean isEmUso(Object objeto)
	{
		synchronized(this.mutex)
		{
			return super.isEmUso(objeto);
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#getTamanho()
	 */
	public int getTamanho()
	{
		synchronized(this.mutex)
		{
			return super.getTamanho();
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#getTamanhoDisponiveis()
	 */
	public int getTamanhoDisponiveis()
	{
		synchronized(this.mutex)
		{
			return super.getTamanhoDisponiveis();
		}
	}
	
	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#getTamanhoEmUso()
	 */
	public int getTamanhoEmUso()
	{
		synchronized(this.mutex)
		{
			return super.getTamanhoDisponiveis();
		}
	}

	/**
	 *	@see		com.brt.gpp.gerentesPool.entidade.Pool#toCollection()
	 */
	public Collection toCollection()
	{
		synchronized(this.mutex)
		{
			return super.toCollection();
		}
	}
	
}
