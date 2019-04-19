package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.FundTransferOpPackage.eFundTransferAttribute;
import TINC.tincRecord;
import TINC.tincSeqHolder;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.OnlineFundTransfer;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Classe que encapsula informacoes referentes a recargas para a Tecnomen. E responsavel por converter os objetos 
 *	OnlineFundTransfer em sequencias de Any's utilizados pela Tecnomen. A classe tambem possui metodos para informar o 
 *	MSISDN do assinante durante a operacao de recarga.
 * 
 *	@author		Daniel Ferreira
 *	@since		30/03/2007
 */
public class OnlineFundTransferHolder extends EntidadeGPPHolder 
{

	/**
	 *	Constante interna da Tecnomen referente a operacao (Online Fund Transfer / Direct Fund Transfer).
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
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo Principal.
	 */
	public static final int VALOR_PRINCIPAL = eFundTransferAttribute._p_MainBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao numero de dias a ser aplicado a data de expiracao do Saldo Principal.
	 */
	public static final int DIAS_EXPIRACAO_PRINCIPAL = eFundTransferAttribute._p_MainExpiryDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo Periodico.
	 */
	public static final int VALOR_PERIODICO = eFundTransferAttribute._p_PeriodicBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao numero de dias a ser aplicado a data de expiracao do Saldo Periodico.
	 */
	public static final int DIAS_EXPIRACAO_PERIODICO = eFundTransferAttribute._p_PeriodicExpiryDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo de Bonus.
	 */
	public static final int VALOR_BONUS = eFundTransferAttribute._p_BonusBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao numero de dias a ser aplicado a data de expiracao do Saldo de Bonus.
	 */
	public static final int DIAS_EXPIRACAO_BONUS = eFundTransferAttribute._p_BonusExpiryDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo de Torpedos.
	 */
	public static final int VALOR_TORPEDOS = eFundTransferAttribute._p_SmBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao numero de dias a ser aplicado a data de expiracao do Saldo de Torpedos.
	 */
	public static final int DIAS_EXPIRACAO_TORPEDOS = eFundTransferAttribute._p_SmExpiryDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo de Dados.
	 */
	public static final int VALOR_DADOS = eFundTransferAttribute._p_DataBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente ao numero de dias a ser aplicado a data de expiracao do Saldo de Dados.
	 */
	public static final int DIAS_EXPIRACAO_DADOS = eFundTransferAttribute._p_DataExpiryDelta;
	
	/**
	 *	Identificador do operador com acesso ao Payment Engine para realizar recargas. Este valor corresponde ao campo 
	 *	"Operator ID" dos CDR's de recarga.
	 */
	private int idOperador;
	
	/**
	 *	Entidade do GPP com as informacoes da recarga.
	 */
	private OnlineFundTransfer valores;
	
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
	public OnlineFundTransferHolder(ORB orb, Object valores)
	{
		super(orb);
		
		this.valores		= (OnlineFundTransfer)valores;
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com informacoes de usuario.
	 */
	public OnlineFundTransferHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.valores		= new OnlineFundTransfer(null, new ArrayList());
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
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		tincRecord[] atributo = (this.entidadeTEC != null) ? this.entidadeTEC.value : null;
		for(int i = 0; ((atributo != null) && (i < atributo.length)); i++)
			switch(atributo[i].id)
			{
				case OnlineFundTransferHolder.MSISDN:
					this.valores.setMsisdn(new MSISDN(atributo[i].value.extract_string()).toMsisdnGPP());
					break;
				case OnlineFundTransferHolder.VALOR_PRINCIPAL:
					this.valores.setValorPrincipal((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case OnlineFundTransferHolder.DIAS_EXPIRACAO_PRINCIPAL:
					this.valores.setNumDiasExpPrincipal(atributo[i].value.extract_short());
					break;
				case OnlineFundTransferHolder.VALOR_PERIODICO:
					this.valores.setValorPeriodico((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case OnlineFundTransferHolder.DIAS_EXPIRACAO_PERIODICO:
					this.valores.setNumDiasExpPeriodico(atributo[i].value.extract_short());
					break;
				case OnlineFundTransferHolder.VALOR_BONUS:
					this.valores.setValorBonus((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case OnlineFundTransferHolder.DIAS_EXPIRACAO_BONUS:
					this.valores.setNumDiasExpBonus(atributo[i].value.extract_short());
					break;
				case OnlineFundTransferHolder.VALOR_TORPEDOS:
					this.valores.setValorTorpedos((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case OnlineFundTransferHolder.DIAS_EXPIRACAO_TORPEDOS:
					this.valores.setNumDiasExpTorpedos(atributo[i].value.extract_short());
					break;
				case OnlineFundTransferHolder.VALOR_DADOS:
					this.valores.setValorDados((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case OnlineFundTransferHolder.DIAS_EXPIRACAO_DADOS:
					this.valores.setNumDiasExpDados(atributo[i].value.extract_short());
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
		anyOperacao.insert_short(OnlineFundTransfer.OPERACAO);
		tincRecord operacao = new tincRecord(OnlineFundTransferHolder.OPERACAO, anyOperacao);
		atributos.add(operacao);

		//Atribuindo o MSISDN do assinante. Este valore e chave da operacao, portanto sempre sera obrigatorio.
		Any anyMsisdn = orb.create_any();
		anyMsisdn.insert_string(new MSISDN(this.valores.getMsisdn()).toMsisdnTEC());
		tincRecord msisdn = new tincRecord(OnlineFundTransferHolder.MSISDN, anyMsisdn);
		atributos.add(msisdn);
		
		//Atribuindo o identificador do operador. Este valor e sempre obrigatorio.
		Any anyOperador = orb.create_any();
		anyOperador.insert_long(this.idOperador);
		tincRecord operador = new tincRecord(OnlineFundTransferHolder.OPERADOR, anyOperador);
		atributos.add(operador);
		
		//Atribuindo o tipo de transacao. Apesar de nao ser obrigatorio para a Tecnomen, e obrigatorio para o GPP, uma 
		//vez que corresponde ao campo "External Transaction Type" dos CDR's de recarga. Sistemas externos dependem 
		//deste campo.
		Any anyTipoTransacao = orb.create_any();
		anyTipoTransacao.insert_short(OnlineFundTransfer.TIPO_TRANSACAO);
		tincRecord tipoTransacao = new tincRecord(OnlineFundTransferHolder.TIPO_TRANSACAO, anyTipoTransacao);
		atributos.add(tipoTransacao);
		
		//Atribuindo o valor a ser inserido no Saldo Principal, caso aplicavel.
		if(this.valores.getValorPrincipal() != 0.0)
		{
			Any anyValorPrincipal = orb.create_any();
			anyValorPrincipal.insert_long((int)(this.valores.getValorPrincipal()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorPrincipal = new tincRecord(OnlineFundTransferHolder.VALOR_PRINCIPAL, anyValorPrincipal);
			atributos.add(valorPrincipal);
		}
		
		//Atribuindo o numero de dias de expiracao do Saldo Principal, caso aplicavel.
		if(this.valores.getNumDiasExpPrincipal() > 0)
		{
			Any anyDiasExpPrincipal = orb.create_any();
			anyDiasExpPrincipal.insert_short(this.valores.getNumDiasExpPrincipal());
			tincRecord diasExpPrincipal = new tincRecord(OnlineFundTransferHolder.DIAS_EXPIRACAO_PRINCIPAL, anyDiasExpPrincipal);
			atributos.add(diasExpPrincipal);
		}

		//Atribuindo o valor a ser inserido no Saldo Periodico, caso aplicavel.
		if(this.valores.getValorPeriodico() != 0.0)
		{
			Any anyValorPeriodico = orb.create_any();
			anyValorPeriodico.insert_long((int)(this.valores.getValorPeriodico()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorPeriodico = new tincRecord(OnlineFundTransferHolder.VALOR_PERIODICO, anyValorPeriodico);
			atributos.add(valorPeriodico);
		}
		
		//Atribuindo o numero de dias de expiracao do Saldo Periodico, caso aplicavel.
		if(this.valores.getNumDiasExpPeriodico() > 0)
		{
			Any anyDiasExpPeriodico = orb.create_any();
			anyDiasExpPeriodico.insert_short(this.valores.getNumDiasExpPeriodico());
			tincRecord diasExpPeriodico = new tincRecord(OnlineFundTransferHolder.DIAS_EXPIRACAO_PERIODICO, anyDiasExpPeriodico);
			atributos.add(diasExpPeriodico);
		}

		//Atribuindo o valor a ser inserido no Saldo de Bonus, caso aplicavel.
		if(this.valores.getValorBonus() != 0.0)
		{
			Any anyValorBonus = orb.create_any();
			anyValorBonus.insert_long((int)(this.valores.getValorBonus()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorBonus = new tincRecord(OnlineFundTransferHolder.VALOR_BONUS, anyValorBonus);
			atributos.add(valorBonus);
		}
		
		//Atribuindo o numero de dias de expiracao do Saldo de Bonus, caso aplicavel.
		if(this.valores.getNumDiasExpBonus() > 0)
		{
			Any anyDiasExpBonus = orb.create_any();
			anyDiasExpBonus.insert_short(this.valores.getNumDiasExpBonus());
			tincRecord diasExpBonus = new tincRecord(OnlineFundTransferHolder.DIAS_EXPIRACAO_BONUS, anyDiasExpBonus);
			atributos.add(diasExpBonus);
		}

		//Atribuindo o valor a ser inserido no Saldo de Torpedos, caso aplicavel.
		if(this.valores.getValorTorpedos() != 0.0)
		{
			Any anyValorTorpedos = orb.create_any();
			anyValorTorpedos.insert_long((int)(this.valores.getValorTorpedos()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorTorpedos = new tincRecord(OnlineFundTransferHolder.VALOR_TORPEDOS, anyValorTorpedos);
			atributos.add(valorTorpedos);
		}
		
		//Atribuindo o numero de dias de expiracao do Saldo de Torpedos, caso aplicavel.
		if(this.valores.getNumDiasExpTorpedos() > 0)
		{
			Any anyDiasExpTorpedos = orb.create_any();
			anyDiasExpTorpedos.insert_short(this.valores.getNumDiasExpTorpedos());
			tincRecord diasExpTorpedos = new tincRecord(OnlineFundTransferHolder.DIAS_EXPIRACAO_TORPEDOS, anyDiasExpTorpedos);
			atributos.add(diasExpTorpedos);
		}

		//Atribuindo o valor a ser inserido no Saldo de Dados, caso aplicavel.
		if(this.valores.getValorDados() != 0.0)
		{
			Any anyValorDados = orb.create_any();
			anyValorDados.insert_long((int)(this.valores.getValorDados()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorDados = new tincRecord(OnlineFundTransferHolder.VALOR_DADOS, anyValorDados);
			atributos.add(valorDados);
		}
		
		//Atribuindo o numero de dias de expiracao do Saldo de Dados, caso aplicavel.
		if(this.valores.getNumDiasExpDados() > 0)
		{
			Any anyDiasExpDados = orb.create_any();
			anyDiasExpDados.insert_short(this.valores.getNumDiasExpDados());
			tincRecord diasExpDados = new tincRecord(OnlineFundTransferHolder.DIAS_EXPIRACAO_DADOS, anyDiasExpDados);
			atributos.add(diasExpDados);
		}

		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}
	
}
