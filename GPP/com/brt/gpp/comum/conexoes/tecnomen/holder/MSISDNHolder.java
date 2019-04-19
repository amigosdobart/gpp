package com.brt.gpp.comum.conexoes.tecnomen.holder;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.CreateSubscriberAttributesPackage.eSubIdSeqAtt;

import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Conversor de entidades MSISDN para estrutura utilizada pelos servicos da Tecnomen.
 *
 *	@author		Daniel Ferreira
 *	@since		02/03/2007
 */
public class MSISDNHolder extends EntidadeGPPHolder 
{

	/**
	 *	Constante interna da Tecnomen referente ao MSISDN do assinante.
	 */
	public static final int MSISDN = eSubIdSeqAtt._SubId;
	
	/**
	 *	Entidade do GPP com as informacoes de um MSISDN.
	 */
	private MSISDN msisdn;
	
	/**
	 *	Entidade da Tecnomen com as informacoes de um assinante.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		msisdn					MSISDN do assinante.
	 */
	public MSISDNHolder(ORB orb, Object msisdn)
	{
		super(orb);
		
		this.msisdn			= new MSISDN((String)msisdn);
		this.entidadeTEC	= new tincSeqHolder();
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com as informacoes de um assinante.
	 */
	public MSISDNHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.msisdn			= null;
		this.entidadeTEC	= entidadeTEC;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		tincRecord[] atributo = (this.entidadeTEC != null) ? this.entidadeTEC.value : null;
		for(int i = 0; ((atributo != null) && (i < atributo.length)); i++)
			switch(atributo[i].id)
			{
				case MSISDNHolder.MSISDN:
					this.msisdn = new MSISDN(atributo[i].value.extract_string());
					return this.msisdn.toMsisdnGPP();
				default: break;
			}
		
		return null; 
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		ORB orb = super.getOrb();
		
		//Atribuindo o login do usuario. Este valor e chave do usuario, portanto sempre sera obrigatorio.
		Any anyMsisdn = orb.create_any();
		anyMsisdn.insert_string((this.msisdn != null) ? this.msisdn.toMsisdnTEC() : "");
		tincRecord msisdn = new tincRecord(MSISDNHolder.MSISDN, anyMsisdn);
		
		this.entidadeTEC.value = new tincRecord[]{msisdn};
		return this.entidadeTEC;
	}
	
}
