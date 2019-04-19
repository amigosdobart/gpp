package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.FundTransferOpPackage.eFundTransferAttribute;
import TINC.tincRecord;
import TINC.tincSeqHolder;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.PeriodicFundTransfer;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Classe que encapsula informacoes referentes a recargas de franquia para a Tecnomen. E responsavel por converter os 
 *	objetos PeriodicFundTransfer em sequencias de Any's utilizados pela Tecnomen. A classe tambem possui metodos para 
 *	informar o MSISDN do assinante durante a operacao de recarga.
 * 
 *	@author		Daniel Ferreira
 *	@since		30/03/2007
 */
public class PeriodicFundTransferHolder extends EntidadeGPPHolder 
{

	/**
	 *	Constante interna da Tecnomen referente a operacao (Periodic Fund Transfer).
	 */
	public static final int OPERACAO = eFundTransferAttribute._p_OperationType;
	
	/**
	 *	Constante interna da Tecnomen referente ao MSISDN do assinante.
	 */
	public static final int MSISDN = eFundTransferAttribute._p_SubId;
	
	/**
	 *	Constante interna da Tecnomen referente ao identificador do operador com acesso a execucao de recargas/ajustes.
	 */
	public static final int OPERADOR = eFundTransferAttribute._p_OperatorId;
	
	/**
	 *	Constante interna da Tecnomen referente ao tipo de transacao definido pelo usuario.
	 */
	public static final int TIPO_TRANSACAO = eFundTransferAttribute._p_TransactionType;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo Periodico.
	 */
	public static final int VALOR_PERIODICO = eFundTransferAttribute._p_PeriodicBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao numero de dias a ser aplicado a data de expiracao do Saldo Periodico.
	 */
	public static final int DIAS_EXPIRACAO_PERIODICO = eFundTransferAttribute._p_PeriodicExpiryDelta;
	
	/**
	 *	Identificador do operador com acesso ao Payment Engine para realizar recargas. Este valor corresponde ao campo 
	 *	"Operator ID" dos CDR's de recarga.
	 */
	private int idOperador;
	
	/**
	 *	Tipo de transacao de usuario (External Transaction Type) a ser enviado para a Tecnomen durante o processo de 
	 *	recarga/ajuste. Este valor corresponde ao campo "External Transaction Type" dos CDR's de recarga. 
	 */
	private short tipoTransacao;
	
	/**
	 *	Entidade do GPP com as informacoes da recarga.
	 */
	private PeriodicFundTransfer valores;
	
	/**
	 *	Entidade da Tecnomen com as informacoes do usuario.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		valores					Entidade do GPP com as informacoes da recarga. 
	 */
	public PeriodicFundTransferHolder(ORB orb, Object valores)
	{
		super(orb);
		
		this.valores		= (PeriodicFundTransfer)valores;
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com informacoes de usuario.
	 */
	public PeriodicFundTransferHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.valores		= new PeriodicFundTransfer(null, new ArrayList());
		this.entidadeTEC	= entidadeTEC;
	}
	
	/**
	 *	Atribui o identificador do operador.
	 *
	 *	@param		operador				Identificador do operador.
	 */
	public void setIdOperador(int idOperador)
	{
		this.idOperador = idOperador;
	}
	
	/**
	 *	Atribui o tipo de transacao.
	 *
	 *	@param		tipoTransacao			Tipo de transacao de usuario informado a Tecnomen.
	 */
	public void setTipoTransacao(short tipoTransacao)
	{
		this.tipoTransacao = tipoTransacao;
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
				case PeriodicFundTransferHolder.MSISDN:
					this.valores.setMsisdn(new MSISDN(atributo[i].value.extract_string()).toMsisdnGPP());
					break;
				case PeriodicFundTransferHolder.VALOR_PERIODICO:
					this.valores.setValorPeriodico((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case PeriodicFundTransferHolder.DIAS_EXPIRACAO_PERIODICO:
					this.valores.setNumDiasExpPeriodico(atributo[i].value.extract_short());
					break;
				default: break;
			}

		return this.valores; 
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		ORB orb = super.getOrb();
		ArrayList atributos = new ArrayList();
		
		//Atribuindo o identificador da operacao. Este valor e chave da operacao, portanto sempre sera obrigatorio.
		Any anyOperacao = orb.create_any();
		anyOperacao.insert_short(PeriodicFundTransfer.OPERACAO);
		tincRecord operacao = new tincRecord(PeriodicFundTransferHolder.OPERACAO, anyOperacao);
		atributos.add(operacao);

		//Atribuindo o MSISDN do assinante. Este valore e chave da operacao, portanto sempre sera obrigatorio.
		Any anyMsisdn = orb.create_any();
		anyMsisdn.insert_string(new MSISDN(this.valores.getMsisdn()).toMsisdnTEC());
		tincRecord msisdn = new tincRecord(PeriodicFundTransferHolder.MSISDN, anyMsisdn);
		atributos.add(msisdn);
		
		//Atribuindo o identificador do operador. Este valor e sempre obrigatorio.
		Any anyOperador = orb.create_any();
		anyOperador.insert_long(this.idOperador);
		tincRecord operador = new tincRecord(PeriodicFundTransferHolder.OPERADOR, anyOperador);
		atributos.add(operador);
		
		//Atribuindo o tipo de transacao. Apesar de nao ser obrigatorio para a Tecnomen, e obrigatorio para o GPP, uma 
		//vez que corresponde ao campo "External Transaction Type" dos CDR's de recarga. Sistemas externos dependem 
		//deste campo.
		Any anyTipoTransacao = orb.create_any();
		anyTipoTransacao.insert_short(this.tipoTransacao);
		tincRecord tipoTransacao = new tincRecord(PeriodicFundTransferHolder.TIPO_TRANSACAO, anyTipoTransacao);
		atributos.add(tipoTransacao);
		
		//Atribuindo o valor a ser inserido no Saldo Periodico, caso aplicavel.
		if(this.valores.getValorPeriodico() != 0.0)
		{
			Any anyValorPeriodico = orb.create_any();
			anyValorPeriodico.insert_long((int)(this.valores.getValorPeriodico()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorPeriodico = new tincRecord(PeriodicFundTransferHolder.VALOR_PERIODICO, anyValorPeriodico);
			atributos.add(valorPeriodico);
		}
		
		//Atribuindo o numero de dias de expiracao do Saldo Periodico, caso aplicavel.
		if(this.valores.getNumDiasExpPeriodico() > 0)
		{
			Any anyDiasExpPeriodico = orb.create_any();
			anyDiasExpPeriodico.insert_short(this.valores.getNumDiasExpPeriodico());
			tincRecord diasExpPeriodico = new tincRecord(PeriodicFundTransferHolder.DIAS_EXPIRACAO_PERIODICO, anyDiasExpPeriodico);
			atributos.add(diasExpPeriodico);
		}

		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}
	
}
