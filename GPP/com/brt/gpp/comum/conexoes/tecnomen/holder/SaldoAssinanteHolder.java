package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;
import java.util.Date;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.TINTocsAccountAttributesPackage.eTINTocsAccountAtt;

import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DataTEC;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Conversor de entidades que representam um saldo do assinante para a estrutura utilizada pelos servicos da 
 *	Tecnomen. Para a 4.4.5 estao definidos os saldos de SMS e Dados. Os saldos de voz ainda estao definidos na tabela 
 *	Subscriber.
 *
 *	@author		Daniel Ferreira
 *	@since		02/03/2007
 */
public class SaldoAssinanteHolder extends EntidadeGPPHolder
{

	/**
	 *	Constante interna da Tecnomen referente ao MSISDN do assinante.
	 */
	public static final int MSISDN = eTINTocsAccountAtt._SubId;
	
	/**
	 *	Constante interna da Tecnomen referente ao identificador do tipo de saldo.
	 */
	public static final int TIPO_SALDO = eTINTocsAccountAtt._AccountType;
	
	/**
	 *	Constante interna da Tecnomen referente a quantidade de creditos no saldo.
	 */
	public static final int CREDITOS = eTINTocsAccountAtt._BalanceAmount0;
	
	/**
	 *	Constante interna da Tecnomen referente a data de expiracao do saldo.
	 */
	public static final int EXPIRACAO = eTINTocsAccountAtt._BalanceExpiry0;
	
	/**
	 *	Entidade do GPP com as informacoes de um MSISDN.
	 */
	private SaldoAssinante saldo;
	
	/**
	 *	Entidade da Tecnomen com as informacoes de um assinante.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		saldo					Informacoes referentes a um saldo do assinante.
	 */
	public SaldoAssinanteHolder(ORB orb, Object saldo)
	{
		super(orb);
		
		this.saldo			= (SaldoAssinante)saldo;
		this.entidadeTEC	= new tincSeqHolder();
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com as informacoes de um assinante.
	 */
	public SaldoAssinanteHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.saldo			= new SaldoAssinante();
		this.entidadeTEC	= entidadeTEC;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		String		msisdn			= null;
		TipoSaldo	tipo			= null;
		double		creditos		= 0.0;
		Date		dataExpiracao	= null;
		
		tincRecord[] atributo = (this.entidadeTEC != null) ? this.entidadeTEC.value : null;
		for(int i = 0; ((atributo != null) && (i < atributo.length)); i++)
			switch(atributo[i].id)
			{
				case SaldoAssinanteHolder.MSISDN:
					msisdn = new MSISDN(atributo[i].value.extract_string()).toMsisdnGPP();
					break;
				case SaldoAssinanteHolder.TIPO_SALDO:
					tipo = MapTipoSaldo.getInstance().getTipoSaldo(atributo[i].value.extract_short());
					break;
				case SaldoAssinanteHolder.CREDITOS:
					creditos = (double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR;
					break;
				case SaldoAssinanteHolder.EXPIRACAO:
					DataTEC dataExpTEC = new DataTEC();
					dataExpTEC.extrair(atributo[i].value);
					dataExpiracao = dataExpTEC.toDate();
					break;
				default: break;
			}
		
		this.saldo = new SaldoAssinante(msisdn, tipo, creditos, dataExpiracao);
		return this.saldo;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		ORB orb = super.getOrb();
		ArrayList atributos = new ArrayList();
		
		//Atribuindo o MSISDN do assinante. Este valor e chave da entidade SaldoAssinante, portanto sempre sera 
		//obrigatorio.
		Any anyMsisdn = orb.create_any();
		anyMsisdn.insert_string(new MSISDN(this.saldo.getMsisdn()).toMsisdnTEC());
		tincRecord msisdn = new tincRecord(SaldoAssinanteHolder.MSISDN, anyMsisdn);
		atributos.add(msisdn);
		
		//Atribuindo o tipo de saldo em questao. Este valor e chave da entidade SaldoAssinante, portanto sempre sera 
		//obrigatorio.
		Any anyTipo = orb.create_any();
		anyTipo.insert_short(this.saldo.getTipo().getIdtTipoSaldo());
		tincRecord tipo = new tincRecord(SaldoAssinanteHolder.TIPO_SALDO, anyTipo);
		atributos.add(tipo);
		
		//Atribuindo a quantidade de creditos, caso aplicavel.
		if(this.saldo.getCreditos() > 0.0)
		{
			Any anyCreditos = orb.create_any();
			anyCreditos.insert_long((int)(this.saldo.getCreditos()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord creditos = new tincRecord(SaldoAssinanteHolder.CREDITOS, anyCreditos);
			atributos.add(creditos);
		}
		
		//Atribuindo a data de expiracao, caso aplicavel.
		if(this.saldo.getDataExpiracao() != null)
		{
			Any anyDataExp = orb.create_any();
			DataTEC dataExp = new DataTEC(this.saldo.getDataExpiracao());
			dataExp.inserir(anyDataExp);
			tincRecord dataExpiracao = new tincRecord(SaldoAssinanteHolder.EXPIRACAO, anyDataExp);
			atributos.add(dataExpiracao);
		}
		
		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}
	
}
