package com.brt.gpp.comum.conexoes.tecnomen.holder;

import org.omg.CORBA.ORB;

import TINC.tincSeqHolder;

/**
 *	Classe base para a implementacao de conversores de entidades do GPP para estruturas utilizadas pelos servidores da 
 *	Tecnomen.
 *
 *	@author		Daniel Ferreira
 *	@since		01/03/2007
 */
public abstract class EntidadeGPPHolder 
{

	/**
	 *	Referencia ao servico da Tecnomen para estabelecimento de conexoes (Object Request Broker).
	 */
	private ORB orb;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 */
	protected EntidadeGPPHolder(ORB orb)
	{
		this.orb = orb;
	}
	
	/**
	 *	Retorna a referencia ao servico da Tecnomen para estabelecimento de conexoes (Object Request Broker).
	 *
	 *	@return		Referencia ao servico da Tecnomen para estabelecimento de conexoes (Object Request Broker).
	 */
	protected ORB getOrb()
	{
		return this.orb;
	}
	
	/**
	 *	Retorna a entidade do GPP.
	 *
	 *	@return		Entidade do GPP.
	 */
	public abstract Object toEntidadeGPP();
	
	/**
	 *	Retorna a estrutura utilizada pelos servidores da Tecnomen.
	 *
	 *	@return		Estrutura utilizada pelo servidor da Tecnomen.
	 */
	public abstract tincSeqHolder toEntidadeTEC();
	
    /**
     *    @see        java.lang.Object#toString()
     */
	public String toString()
	{
    	StringBuffer	result		= new StringBuffer("Holder: " + this.getClass().getName() + " [");
    	tincSeqHolder	holder		= this.toEntidadeTEC();

    	for(int i = 0; i < holder.value.length; i++)
    	{
    		result.append("[ID: ");
    		result.append(holder.value[i].id);
    		result.append(" - Valor: ");
    		result.append(holder.value[i].value + "]");
    	}
          
    	result.append("]");

    	return result.toString();
	}
	
}
