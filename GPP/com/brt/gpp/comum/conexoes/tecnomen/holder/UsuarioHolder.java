package com.brt.gpp.comum.conexoes.tecnomen.holder;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.UserAttributesPackage.eUserAtt;

import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Classe que encapsula informacoes pessoais de usuarios para a Tecnomen. Este objeto deve ser utilizado no processo 
 *	de ativacao do assinante apesar de possuir somente o Login do assinante, que corresponde ao seu MSISDN. Isto 
 *	porque o GPP nao possui informacoes cadastrais de assinantes.
 * 
 *	@author		Daniel Ferreira
 *	@since		06/03/2007
 */
public class UsuarioHolder extends EntidadeGPPHolder 
{

	/**
	 *	Constante interna da Tecnomen referente ao Login do assinante.
	 */
	public static final int LOGIN = eUserAtt._Login;
	
	/**
	 *	Constante interna da Tecnomen referente ao tipo de usuario.
	 */
	public static final int TIPO = eUserAtt._UserType;
	
	/**
	 *	Constante referente a assinantes como tipo de usuario. Nao ha uma constante da Tecnomen especifica para este 
	 *	valor, portanto e definido a constante "1".
	 */
	public static final short TIPO_ASSINANTE = 1;
	
	/**
	 *	Login do assinante. Corresponde ao seu MSISDN.
	 */
	private MSISDN login;
	
	/**
	 *	Entidade da Tecnomen com as informacoes do usuario.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		login					Login (MSISDN) do assinante. 
	 */
	public UsuarioHolder(ORB orb, Object login)
	{
		super(orb);
		
		this.login			= new MSISDN((String)login);
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com informacoes de usuario.
	 */
	public UsuarioHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.login			= null;
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
				case UsuarioHolder.LOGIN:
					this.login = new MSISDN(atributo[i].value.extract_string());
					return this.login.toMsisdnGPP();
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
		Any anyLogin = orb.create_any();
		anyLogin.insert_string(this.login.toMsisdnTEC());
		tincRecord login = new tincRecord(UsuarioHolder.LOGIN, anyLogin);

		//Atribuindo o tipo de usuario. Este valor e obrigatorio, porem comum a todos os assinantes. O valor a ser 
		//informado corresponde ao tipo de usuario "Subscriber".
		Any anyTipo = orb.create_any();
		anyTipo.insert_short(UsuarioHolder.TIPO_ASSINANTE);
		tincRecord tipo = new tincRecord(UsuarioHolder.TIPO, anyTipo);
		
		this.entidadeTEC.value = new tincRecord[]{login, tipo};
		return this.entidadeTEC;
	}
	
}
