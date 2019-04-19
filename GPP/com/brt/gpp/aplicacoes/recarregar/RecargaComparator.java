package com.brt.gpp.aplicacoes.recarregar;

//Imports Java.

import java.util.Comparator;
import java.util.Date;

//Imports GPP.

import com.brt.gpp.aplicacoes.recarregar.Recarga;

/**
 *	Classe compara duas recargas de acordo com as suas datas de execucao.
 * 
 *	@author	Daniel Ferreira
 *	@since	07/10/2005
 */
public class RecargaComparator implements Comparator
{

	//Construtores.
	
	/**
	 * Construtor da classe.
	 */
	public RecargaComparator()
	{
	}
	
	//Implementacao de Comparator.
	
	/**
	 *	Compara duas recargas de acordo com a data de execucao.
	 *
	 *	@param		Object					recarga0					Primeira recarga.
	 *	@param		Object					recarga1					Segunda recarga. 
	 *	@return		int													-1 se a primeira recarga e mais recente,
	 *																	 0 se as duas recargas tem a mesma data de execucao e
	 *																	 1 se a segunda recarga e mais recente.
	 *	@throws		ClassCastException
	 */
	public int compare(Object recarga0, Object recarga1) throws ClassCastException
	{
	    if((!(recarga0 instanceof Recarga)) || (!(recarga1 instanceof Recarga)))
	    {
	        throw new ClassCastException();
	    }
	    
	    Date dataExecucao0 = ((Recarga)recarga0).getDatRecarga();
	    Date dataExecucao1 = ((Recarga)recarga1).getDatRecarga();

	    if((dataExecucao0 == null) && (dataExecucao1 == null))
	    {
	        return 0;
	    }
	    
	    if(dataExecucao0 == null)
	    {
	        return 1;
	    }
	    
	    if(dataExecucao1 == null)
	    {
	        return -1;
	    }
	    
	    return dataExecucao0.compareTo(dataExecucao1);
	}
		
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		Object					object						Objeto a ser comparado com o atual.
	 *	@return		boolean												True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof RecargaComparator))
		{
			return false;
		}
		
		return true;
	}
	
}
