package com.brt.gpp.comum.conexoes.tecnomen.conversor;

import TINC.Pe_exception;
import TINC.Pi_exception;

import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

/**
 *	Conversor de excecoes da Tecnomen para excecoes do GPP com mensagens apropriadas.
 *
 *	@author		Daniel Ferreira
 *	@since		23/02/2007
 */
public abstract class ConversorExcecaoTecnomen 
{

	/**
	 *	Cria e retorna excecao Tecnomen/GPP com a mensagem apropriada.
	 *
	 *	@param		retorno					Codigo de retorno da operacao executada pelo servidor da Tecnomen.
	 *	@return		Excecao Tecnomen/GPP com a mensagem apropriada.
	 */
	public static GPPTecnomenException newGPPTecnomenException(int retorno)
	{
		return new GPPTecnomenException("Codigo: " + String.valueOf(retorno));
	}
	
	/**
	 *	Retorna excecao Tecnomen/GPP com a mensagem apropriada.
	 *
	 *	@param		exception				Excecao lancada pelo servidor da Tecnomen.
	 *	@return		Excecao Tecnomen/GPP com a mensagem apropriada.
	 */
	public static GPPTecnomenException toGPPTecnomenException(Pi_exception exception)
	{
		return new GPPTecnomenException("Codigo: " + exception.error_code + " - Status: " + exception.status);
	}

	/**
	 *	Retorna excecao Tecnomen/GPP com a mensagem apropriada.
	 *
	 *	@param		exception				Excecao lancada pelo servidor da Tecnomen.
	 *	@return		Excecao Tecnomen/GPP com a mensagem apropriada.
	 */
	public static GPPTecnomenException toGPPTecnomenException(Pe_exception exception)
	{
		return new GPPTecnomenException("Codigo: " + exception.error_code + " - Status: " + exception.status + " - Extra: " + exception.extra);
	}

}
