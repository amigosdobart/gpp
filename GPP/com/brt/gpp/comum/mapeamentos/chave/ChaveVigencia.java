package com.brt.gpp.comum.mapeamentos.chave;

import java.util.Date;

import com.brt.gpp.comum.mapeamentos.chave.ChaveTreeMap;

/**
 *	Chave utilizada para comparacao de vigencia de periodos. Deve ser utilizada para armazenar um par de datas (data 
 *	de inicio e fim de vigencia). Desta forma, e possivel verificar se a chave esta vigente em relacao a uma data 
 *	informada. Isto pode ser feito com o metodo compareTo(Date). Porem, nao e permitido utilizar esta chave com 
 *	objetos que nao sejam java.util.Date. Isto pode causar o lancamento de ClassCastException. Alem disso, a nao 
 *	atribuicao do par de datas (mesmo que a data final de vigencia seja null) pode causar o lancamento de 
 *	ArrayIndexOutOfBoundsException.
 * 
 *	@author		Daniel Ferreira
 *	@since		19/01/2007
 */
public class ChaveVigencia extends ChaveTreeMap
{

	/**
	 *	Construtor da classe.
	 */
	public ChaveVigencia(Object[] objs)
	{
		super(objs);
	}
	
	/**
	 *	@see		java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if(obj instanceof Date)
			return this.compareTo((Date)obj);
		
		return super.compareTo(obj);
	}
	
	/**
	 *	Compara se as datas da chave estao vigentes em relacao a data informada.
	 *
	 *	@param		data		Data comparada.
	 */
	public int compareTo(Date data)
	{
		Object[]	objs	= super.getObjects();
		Date		dataIni	= (Date)objs[0];
		Date		dataFim	= (Date)objs[1];
		
		if((data == null) || (dataIni.compareTo(data) > 0))
			return 1;
		
		if(dataFim == null)
			return 0;
		
		return dataFim.compareTo(data);
	}
	
}
