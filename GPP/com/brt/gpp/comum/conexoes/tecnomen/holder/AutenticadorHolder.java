package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.HashMap;
import java.util.Map;

import TINC.ServiceKeyRec;
import TINC.ServiceKeySeqHolder;
import TINC.UserInfo;

import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;

/**
 *	Conversor de Autenticadores para estruturas utilizadas pelo Servidor de Autenticacao da Tecnomen.
 * 
 *	@author		Daniel Ferreira
 *	@since		23/02/2007
 */
public class AutenticadorHolder 
{
	
	/**
	 *	Lista de autenticadores no formato da Tecnomen.
	 */
	private ServiceKeySeqHolder autenticadores;

	/**
	 *	Construtor da classe.
	 */
	public AutenticadorHolder()
	{
		this.autenticadores = new ServiceKeySeqHolder(new ServiceKeyRec[]{});
	}

	/**
	 *	Converte a lista de autenticadores para o as estruturas utilizadas pelo Servidor de Autenticacao da Tecnomen.
	 * 
	 *	@return		Estrutura com autenticadores utilizada na interface com o Servidor de Autenticacao.
	 */
	public ServiceKeySeqHolder toServiceKeySeqHolder()
	{
		return this.autenticadores;
	}
	
	/**
	 *	Retorna o container de autenticadores por servico.
	 * 
	 *	@param		login					Login do usuario cadastrado no Servidor de Autenticacao.
	 *	@param		info					Informacoes do usuario.
	 *	@return		Container de autenticadores.
	 */
	public Map toMap(String login, UserInfo info)
	{
		HashMap result = new HashMap();

		int		idUsuario	= (info != null) ? info.id			: -1;
		short	tipo		= (info != null) ? info.type		: -1;
		short	idOrigem	= (info != null) ? info.originId	: -1;
		
		ServiceKeyRec[] registros = (this.autenticadores != null) ? this.autenticadores.value : null;
		
		for(int i = 0; ((registros != null) && (i < registros.length)); i++)
		{
			Autenticador autenticador = new Autenticador(registros[i].service,
														 idUsuario,
														 login,
														 tipo,
														 idOrigem,
														 registros[i].locationId,
														 registros[i].key, 
														 registros[i].ior);
			
			result.put(new Integer(registros[i].service), autenticador);
		}
		
		return result;
	}
	
	public String toString()
	{
		StringBuffer	result	= new StringBuffer();
		
		result.append("Holder: " + this.getClass().getName() + "[");
		
		for (int i = 0; i < this.autenticadores.value.length; i++)
		{
			result.append("[Servico #: " + i + " - ");
			result.append("Service ID: " + this.autenticadores.value[i].service + " - ");
			result.append("IOR: " + this.autenticadores.value[i].ior + " - ");
			result.append("Key: " + this.autenticadores.value[i].key + " - ");
			result.append("Location ID: " + this.autenticadores.value[i].locationId + "]");
		}

		result.append("]");
		
		return result.toString();
	}
	
}
