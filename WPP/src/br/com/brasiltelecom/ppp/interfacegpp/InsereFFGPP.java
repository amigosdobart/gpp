/*
 * Criado em  29/06/2005
 */
package br.com.brasiltelecom.ppp.interfacegpp;


import java.util.Collection;
import java.util.Iterator;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.comum.Definicoes;

/**
 * Insere ou atualiza a lista de Amigos
 * Toda Hora do assinante 
 * @author 	Marcelo Alves Araujo
 * @since 	15/12/2005
 */
public class InsereFFGPP
{	
	/**
	 * Método para atualização de amigos toda hora
	 * 
	 * @param msisdn 	- MSISDN do assinante
	 * @param lista		- Lista Amigos Toda Hora do assinante
	 * @param operador	- Operador do portal que realizou a mudança
	 * @param servidor 	- Endereço do servidor
	 * @param porta 	- Porta do servidor
	 * @throws Exception
	 */
	public static short insereFFGPP(String msisdn,Collection lista,String operador,String servidor,String porta) throws Exception
	{			
		short retorno = 0;
		try
		{
			ORB orb = GerenteORB.getORB(servidor, porta);
			
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			
			Iterator it = lista.iterator();
			String listaFF = "";
			
			// Monta a lista separando por ;
			while(it.hasNext())
				listaFF = listaFF + (String)it.next() +";";
			// Inclui ; no final da lista
			if(listaFF.equals(""))
				listaFF = ";";
			
			retorno = pPOA.atualizaFriendsFamilyAssinante(msisdn,listaFF,operador, Definicoes.SERVICO_NOVO_FF);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro ao conectar ao GPP");
		}
		
		return retorno;
	}
}