package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 *	Classe que representa o Value Object do Produtor-Consumidor do Pula-Pula.
 * 
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/10 (yyyy/mm/dd)
 *
 *  Atualizado por Bernardo Dias
 *  Data: 15/10/2007
 */
public class VOFabrica
{
    /**
     * Um array contendo os parâmetros de processamento.
     * Os parâmetros já estão na ordem de processamento.
     */
    private Object[]			parametros;

    public VOFabrica()
	{
	}
	
	/**
	 * Construtor da classe
	 * @param parametros
	 * @param datProcessamento
	 */
	public VOFabrica(Object[] parametros)
	{
		this.parametros = parametros;
	}

	/**
	 * Torna o VO uma <code>String</code> no formato xml
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer saida = new StringBuffer();
		saida.append("<");
		saida.append(this.getClass().getName());
		saida.append(">");
		Field[] fields = this.getClass().getDeclaredFields();
		int size = fields.length; 
		for(int i = 0 ; i < size ; i++)
		{
			Field tmp = fields[i];
			saida.append("<");
			saida.append(tmp.getName());
			saida.append(">");
			try
			{
				Object tmpObj = tmp.get(this);
				Class tmpClass = tmpObj.getClass();
				if(tmpClass.isArray())
				{
					int sizeArray = Array.getLength(tmpObj);
					for(int j = 0 ; j < sizeArray ; j++)
					{
						saida.append(Array.get(tmpObj,j).toString());
					}
				}
				else
				{
					saida.append(tmp.get(this).toString());
				}
			}
			catch (Exception e)
			{
				saida.append("NULL");
			}

			saida.append("</");
			saida.append(tmp.getName());
			saida.append(">");
			
		}
		saida.append("</");
		saida.append(this.getClass().getName());
		saida.append(">");
		
		return saida.toString();

	}

	/**
	 * Retorna parametros
	 */
	public Object[] getParametros() 
	{
		return parametros;
	}
	
	/** 
	 * Modifica parametros
	 */
	public void setParametros(Object[] parametros) 
	{
		this.parametros = parametros;
	}
}
