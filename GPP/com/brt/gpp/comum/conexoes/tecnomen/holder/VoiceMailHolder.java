package com.brt.gpp.comum.conexoes.tecnomen.holder;

import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;

import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Classe que encapsula informacoes informacoes de Voice Mail para a Tecnomen. Este objeto deve ser utilizado no 
 *	processo de ativacao do assinante. Ainda nao utilizada pela Tecnomen. Portanto o objeto deve produzir uma lista 
 *	vazia para a Tecnomen e NULL para o GPP. Para mais informacoes vide documentacao Tecnomen PP 4.4 Provisioning 
 *	Server Interface 5035617_1.pdf.
 * 
 *	@author		Daniel Ferreira
 *	@since		27/03/2007
 */
public class VoiceMailHolder extends EntidadeGPPHolder 
{

	/**
	 *	Entidade da Tecnomen com as informacoes do usuario.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		obj						Entidade do GPP. Deve ser atualizado quando o parametro de Voice Mail 
	 *										comecar a ser utilizado pelo Provision Server da Tecnomen. 
	 */
	public VoiceMailHolder(ORB orb, Object obj)
	{
		super(orb);
		
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen.
	 */
	public VoiceMailHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.entidadeTEC	= entidadeTEC;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		return null; 
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		return this.entidadeTEC;
	}
	
}
