package com.brt.gpp.comum.mapeamentos.chave;

import com.brt.gpp.comum.mapeamentos.chave.Chave;

/**
 *	Chave para inclusao e recuperacao de registros em mapeamentos com containers da classe TreeMap.
 * 
 *	@author		Daniel Ferreira
 *	@since		19/01/2007
 */
public class ChaveTreeMap extends Chave implements Comparable
{

	/**
	 *	Construtor da classe.
	 */
	public ChaveTreeMap(Object[] objs)
	{
		super(objs);
	}
	
	/**
	 *	@see		java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if(obj instanceof ChaveTreeMap)
			return this.compareTo((ChaveTreeMap)obj);
		
		return this.toString().compareTo(obj.toString());
	}
	
	/**
	 *	Executa a comparacao entre objetos que sao instancias desta classe. O processo executa uma comparacao para 
	 *	cada elemento na mesma posicao nos dois arrays, com prioridade para os elementos nas primeiras posicoes. No 
	 *	caso de diferenca de tamanho entre os arrays, ganha o que tiver o maior numero de elementos.
	 *
	 *	@param		chave					Chave comparada.
	 *	@return		Menor que zero se este objeto for menor, zero se forem iguais e maior que zero de o outro for maior.
	 */
	public int compareTo(ChaveTreeMap chave)
	{
		if(chave == null)
			return 1;
		
		Object[] thisObjs = super.getObjects();
		Object[] objs = chave.getObjects();

		if(thisObjs == null)
		{
			if(objs == null)
				return 0;
			
			return -1;
		}
		
		for(int i = 0; i < thisObjs.length; i++)
		{
			int comparator = 0;
				
			if((thisObjs[i] == objs[i]) || (thisObjs[i].equals(objs[i])))
				comparator = 0;
			//Nota: A inversao da comparacao entre thisObjs e objs e feita para permitir a comparacao com objetos
			//que nao permitem que sejam comparados com outros de classes diferentes (ex: java.util.Date). Porem, 
			//devido a inversao, e necessario trocar o sinal da comparacao, uma vez que, se: 
			//	thisObjs[i].compareTo(objs[i] == 1, 
			//entao necessariamente:
			//	objs[i].compareTo(thisObjs[i] = -1.
			else if(objs[i] instanceof Comparable)
				comparator = -((Comparable)objs[i]).compareTo(thisObjs[i]);
			else
				comparator = thisObjs[i].hashCode() - objs[i].hashCode();
			
			if(comparator != 0)
				return comparator;
		}
		
		if(thisObjs.length < objs.length)
			return -1;
		else if(thisObjs.length == objs.length)
			return 0;
		else
			return 1;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer(this.getClass().getName());
		
		Object[] objs = super.getObjects();
		
		if(objs != null)
			for(int i = 0; i < objs.length; i++)
			{
				buffer.append("||");
				buffer.append((objs[i] != null) ? objs[i].toString() : "NULL");
			}
			
		return buffer.toString();
	}
	
}
