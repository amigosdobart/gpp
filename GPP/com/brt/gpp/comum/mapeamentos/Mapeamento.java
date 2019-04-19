package com.brt.gpp.comum.mapeamentos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamentos;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * 	Classe abstrata que serve como base para construcao de mapeamentos. As entidades de banco de dados mapeaveis devem
 *	ser representadas por objetos retornados por estes mapeamentos. 
 *
 *	@author		Daniel Ferreira
 *	@since		23/11/2005
 */
public abstract class Mapeamento
{

    protected 	GerentePoolBancoDados	gerenteBancoDados;
	protected 	Map						values;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected Mapeamento() throws GPPInternalErrorException
	{
		this.gerenteBancoDados	= GerentePoolBancoDados.getInstancia(0);
		this.values     		= Collections.synchronizedMap(new HashMap());
		this.load();
		
		//Registrando o mapeamento no gerente de mapeamentos.
		Mapeamentos.getInstance().registrar(this);
	}

	/**
	 *	Carrega o mapeamento em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected abstract void load() throws GPPInternalErrorException; 
	
	/**
	 *	Atualiza o mapeamento em memoria, limpando as informacoes antigas.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	public void reload() throws GPPInternalErrorException 
	{
	    this.values.clear();			
		this.load();
	}

	/**
	 *	Atualiza o mapeamento em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	public void refresh() throws GPPInternalErrorException 
	{
		this.load();
	}

	/**
	 *	Retorna objeto representando registro de tabela de banco de dados mapeado em memoria.
	 *
	 *	@param		Object[]				key							Identificador unico do registro na tabela.
	 *	@return		Entidade											Objeto representando registro mapeado em memoria.
	 */
	public Entidade get(Object[] key)
	{
	    Map values = null;
	    
	    if((key != null) && (key.length > 0))
	    {
	        for(int i = 0; i < key.length; i++)
	        {
	            if(i == key.length - 1)
	            {
	                Entidade entidade = (Entidade)((values != null) ? values.get(key[i]) : this.values.get(key[i])); 
	                
	                if(entidade != null)
	                {
	                    return (Entidade)entidade.clone();
	                }
	            }
	            
		        values = (Map)this.values.get(key[i]);
	        }
	    }
	    
	    return null;
	}
	
	/**
	 *	Retorna uma lista com todos os registros da tabela mapeados como objetos em memoria.
	 *
	 *	@return		Collection											Lista com os objetos mapeados em memoria.
	 */
	public Collection getAll()
	{
	    ArrayList result = new ArrayList();
	    
	    if((this.values != null) && (this.values.size() > 0))
	    {
		    Iterator iterator = this.values.values().iterator();
	        Stack stack = new Stack();
	        
		    while(true)
		    {
		        try
		        {
			        Object value = iterator.next();
			        
			        if(value instanceof Map)
			        {
			            stack.push(iterator);
			            iterator = ((Map)value).values().iterator();
			        }
			        
			        if(value instanceof Collection)
			        {
			            stack.push(iterator);
			            iterator = ((Collection)value).iterator();
			        }
			        
			        if(value instanceof Entidade)
			        {
				        result.add(((Entidade)value).clone());
			        }
		        }
		        catch(NoSuchElementException e)
		        {
			        if(stack.isEmpty())
			        {
			            break;
			        }
			        
		            iterator = (Iterator)stack.pop();
		        }
		    }
	    }
	    
	    return result;
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		return this.values.values().toString();
	}
}
