package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.TINTocsServiceAttributesPackage.eTINTocsServiceAtt;

import com.brt.gpp.aplicacoes.aprovisionar.ServicoTECAssinante;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;
import com.brt.gpp.comum.mapeamentos.MapServicoTecnomen;
import com.brt.gpp.comum.mapeamentos.entidade.ServicoTecnomen;

/**
 *	Conversor de entidade que representam servicos disponiveis aos assinantes em estrutura utilizada pelos servidores 
 *	da Tecnomen. Exemplos de servicos sao: SMO, SMT, MMS, GPRS, WAP, etc. Os servicos que estao disponiveis ao 
 *	assinante dependem da categoria de seu plano. Assinantes GSM tem acesso aos servicos de Torpedos e Dados.
 *	Assinantes da Fixa nao.
 *
 *	@version	1.0		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ServicoTECAssinanteHolder extends EntidadeGPPHolder
{

	/**
	 *	Constante interna da Tecnomen referente ao MSISDN do assinante.
	 */
	public static final int MSISDN = eTINTocsServiceAtt._SubId;
	
	/**
	 *	Constante interna da Tecnomen referente ao identificador do servico ativado para o assinante.
	 */
	public static final int ID_SERVICO = eTINTocsServiceAtt._ServiceSubType;
	
	/**
	 *	Constante interna da Tecnomen referente ao tipo do servico ativado para o assinante.
	 */
	public static final int TIPO_SERVICO = eTINTocsServiceAtt._ServiceType;
	
	/**
	 *	Constante interna da Tecnomen referente ao status do servico ativado para o assinante.
	 */
	public static final int STATUS = eTINTocsServiceAtt._AccountStatus;
	
	/**
	 *	Constante interna da Tecnomen referente ao status de servico do servico ativado para o assinante.
	 */
	public static final int STATUS_SERVICO = eTINTocsServiceAtt._ServiceStatus;
	
	/**
	 *	Entidade do GPP com o servico ativado na Tecnomen para o assinante.
	 */
	private ServicoTECAssinante servicoAssinante;
	
	/**
	 *	Entidade da Tecnomen com as informacoes de um assinante.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		servicoAssinante		Entidade do GPP com o servico ativado na Tecnomen para o assinante.
	 */
	public ServicoTECAssinanteHolder(ORB orb, Object servicoAssinante)
	{
		super(orb);
		
		this.servicoAssinante	= (ServicoTECAssinante)servicoAssinante;
		this.entidadeTEC		= new tincSeqHolder();
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com as informacoes de um assinante.
	 */
	public ServicoTECAssinanteHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.servicoAssinante	= new ServicoTECAssinante();
		this.entidadeTEC		= entidadeTEC;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		String			msisdn			= null;
		ServicoTecnomen	servico			= null;
		short			status			= -1;
		short			statusServico	= -1;

		tincRecord[] atributo = (this.entidadeTEC != null) ? this.entidadeTEC.value : null;
		for(int i = 0; ((atributo != null) && (i < atributo.length)); i++)
			switch(atributo[i].id)
			{
				case ServicoTECAssinanteHolder.MSISDN:
					msisdn = new MSISDN(atributo[i].value.extract_string()).toMsisdnGPP();
					break;
				case ServicoTECAssinanteHolder.ID_SERVICO:
					servico = MapServicoTecnomen.getInstance().getServicoTecnomen(atributo[i].value.extract_short());
					break;
				case ServicoTECAssinanteHolder.STATUS:
					status = atributo[i].value.extract_short();
					break;
				case ServicoTECAssinanteHolder.STATUS_SERVICO:
					statusServico = atributo[i].value.extract_short();
					break;
				default: break;
			}
		
		this.servicoAssinante = new ServicoTECAssinante(msisdn, servico, status, statusServico);
		return this.servicoAssinante;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		ORB orb = super.getOrb();
		ArrayList atributos = new ArrayList();
		
		//Atribuindo o identificador do servico da Tecnomen. Este valor e chave da entidade ServicoTECAssinante, 
		//portanto sempre sera obrigatorio.
		Any anyIdServico = orb.create_any();
		anyIdServico.insert_short(this.servicoAssinante.getServicoTecnomen().getIdtServico());
		tincRecord idServico = new tincRecord(ServicoTECAssinanteHolder.ID_SERVICO, anyIdServico);
		atributos.add(idServico);
		
		//Atribuindo o MSISDN do assinante. Este valor e chave da entidade ServicoTECAssinante, portanto sempre sera 
		//obrigatorio.
		Any anyMsisdn = orb.create_any();
		anyMsisdn.insert_string(new MSISDN(this.servicoAssinante.getMsisdn()).toMsisdnTEC());
		tincRecord msisdn = new tincRecord(ServicoTECAssinanteHolder.MSISDN, anyMsisdn);
		atributos.add(msisdn);
		
		//Atribuindo o tipo do servico da Tecnomen. Este valor e chave da entidade ServicoTECAssinante, portanto 
		//sempre sera obrigatorio.
		Any anyTipoServico = orb.create_any();
		anyTipoServico.insert_short(this.servicoAssinante.getServicoTecnomen().getTipServico());
		tincRecord tipoServico = new tincRecord(ServicoTECAssinanteHolder.TIPO_SERVICO, anyTipoServico);
		atributos.add(tipoServico);
		
		//Atribuindo o status do servico da Tecnomen, caso aplicavel.
		if(this.servicoAssinante.getStatus() > -1)
		{
			Any anyStatus = orb.create_any();
			anyStatus.insert_short(this.servicoAssinante.getStatus());
			tincRecord status = new tincRecord(ServicoTECAssinanteHolder.STATUS, anyStatus);
			atributos.add(status);
		}
		
		//Atribuindo o status do servico da Tecnomen, caso aplicavel.
		if(this.servicoAssinante.getStatusServico() > -1)
		{
			Any anyStatusServico = orb.create_any();
			anyStatusServico.insert_short(this.servicoAssinante.getStatusServico());
			tincRecord statusServico = new tincRecord(ServicoTECAssinanteHolder.STATUS_SERVICO, anyStatusServico);
			atributos.add(statusServico);
		}
		
		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}
	
}
