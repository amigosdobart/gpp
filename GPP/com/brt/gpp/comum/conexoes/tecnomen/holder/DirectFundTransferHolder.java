package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.FundTransferOpPackage.eFundTransferAttribute;
import TINC.tincRecord;
import TINC.tincSeqHolder;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DataTEC;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DirectFundTransfer;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Classe que encapsula informacoes referentes a ajustes para a Tecnomen. E responsavel por converter os objetos 
 *	DirectFundTransfer em sequencias de Any's utilizados pela Tecnomen. A classe tambem possui metodos para informar o 
 *	MSISDN do assinante durante a operacao de ajuste.
 * 
 *	@author		Daniel Ferreira
 *	@since		30/03/2007
 */
public class DirectFundTransferHolder extends EntidadeGPPHolder 
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
	 *	Constante interna da Tecnomen referente a nova data de expiracao do Saldo Principal.
	 */
	public static final int DATA_EXPIRACAO_PRINCIPAL = eFundTransferAttribute._p_MainExpiryDate;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo Periodico.
	 */
	public static final int VALOR_PERIODICO = eFundTransferAttribute._p_PeriodicBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente a nova data de expiracao do Saldo Periodico.
	 */
	public static final int DATA_EXPIRACAO_PERIODICO = eFundTransferAttribute._p_PeriodicExpiryDate;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo de Bonus.
	 */
	public static final int VALOR_BONUS = eFundTransferAttribute._p_BonusBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente a nova data de expiracao do Saldo de Bonus.
	 */
	public static final int DATA_EXPIRACAO_BONUS = eFundTransferAttribute._p_BonusExpiryDate;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo de Torpedos.
	 */
	public static final int VALOR_TORPEDOS = eFundTransferAttribute._p_SmBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente a nova data de expiracao do Saldo de Torpedos.
	 */
	public static final int DATA_EXPIRACAO_TORPEDOS = eFundTransferAttribute._p_SmExpiryDate;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor a ser inserido no Saldo de Dados.
	 */
	public static final int VALOR_DADOS = eFundTransferAttribute._p_DataBalanceDelta;
	
	/**
	 *	Constante interna da Tecnomen referente a nova data de expiracao do Saldo de Dados.
	 */
	public static final int DATA_EXPIRACAO_DADOS = eFundTransferAttribute._p_DataExpiryDate;
	
	/**
	 *	Identificador do operador com acesso ao Payment Engine para realizar recargas. Este valor corresponde ao campo 
	 *	"Operator ID" dos CDR's de recarga.
	 */
	private int idOperador;
	
	/**
	 *	Entidade do GPP com as informacoes da recarga/ajuste.
	 */
	private DirectFundTransfer valores;
	
	/**
	 *	Entidade da Tecnomen com as informacoes do usuario.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		valores					Entidade do GPP com as informacoes da recarga/ajuste. 
	 */
	public DirectFundTransferHolder(ORB orb, Object valores)
	{
		super(orb);
		
		this.valores		= (DirectFundTransfer)valores;
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com informacoes de usuario.
	 */
	public DirectFundTransferHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.valores		= new DirectFundTransfer(null, new ArrayList());
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
				case DirectFundTransferHolder.MSISDN:
					this.valores.setMsisdn(new MSISDN(atributo[i].value.extract_string()).toMsisdnGPP());
				case DirectFundTransferHolder.VALOR_PRINCIPAL:
					this.valores.setValorPrincipal(atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case DirectFundTransferHolder.DATA_EXPIRACAO_PRINCIPAL:
					DataTEC dataExpPrincipal = new DataTEC();
					dataExpPrincipal.extrair(atributo[i].value);
					this.valores.setDataExpPrincipal(dataExpPrincipal.toDate());
					break;
				case DirectFundTransferHolder.VALOR_PERIODICO:
					this.valores.setValorPeriodico((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case DirectFundTransferHolder.DATA_EXPIRACAO_PERIODICO:
					DataTEC dataExpPeriodico = new DataTEC();
					dataExpPeriodico.extrair(atributo[i].value);
					this.valores.setDataExpPeriodico(dataExpPeriodico.toDate());
					break;
				case DirectFundTransferHolder.VALOR_BONUS:
					this.valores.setValorBonus((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case DirectFundTransferHolder.DATA_EXPIRACAO_BONUS:
					DataTEC dataExpBonus = new DataTEC();
					dataExpBonus.extrair(atributo[i].value);
					this.valores.setDataExpBonus(dataExpBonus.toDate());
					break;
				case DirectFundTransferHolder.VALOR_TORPEDOS:
					this.valores.setValorTorpedos((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case DirectFundTransferHolder.DATA_EXPIRACAO_TORPEDOS:
					DataTEC dataExpTorpedos = new DataTEC();
					dataExpTorpedos.extrair(atributo[i].value);
					this.valores.setDataExpTorpedos(dataExpTorpedos.toDate());
					break;
				case DirectFundTransferHolder.VALOR_DADOS:
					this.valores.setValorDados((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case DirectFundTransferHolder.DATA_EXPIRACAO_DADOS:
					DataTEC dataExpDados = new DataTEC();
					dataExpDados.extrair(atributo[i].value);
					this.valores.setDataExpDados(dataExpDados.toDate());
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
		anyOperacao.insert_short(DirectFundTransfer.OPERACAO);
		tincRecord operacao = new tincRecord(DirectFundTransferHolder.OPERACAO, anyOperacao);
		atributos.add(operacao);

		//Atribuindo o MSISDN do assinante. Este valore e chave da operacao, portanto sempre sera obrigatorio.
		Any anyMsisdn = orb.create_any();
		anyMsisdn.insert_string(new MSISDN(this.valores.getMsisdn()).toMsisdnTEC());
		tincRecord msisdn = new tincRecord(DirectFundTransferHolder.MSISDN, anyMsisdn);
		atributos.add(msisdn);
		
		//Atribuindo o identificador do operador. Este valor e sempre obrigatorio.
		Any anyOperador = orb.create_any();
		anyOperador.insert_long(this.idOperador);
		tincRecord operador = new tincRecord(DirectFundTransferHolder.OPERADOR, anyOperador);
		atributos.add(operador);
		
		//Atribuindo o tipo de transacao. Apesar de nao ser obrigatorio para a Tecnomen, e obrigatorio para o GPP, uma 
		//vez que corresponde ao campo "External Transaction Type" dos CDR's de recarga. Sistemas externos dependem 
		//deste campo.
		Any anyTipoTransacao = orb.create_any();
		anyTipoTransacao.insert_short(DirectFundTransfer.TIPO_TRANSACAO);
		tincRecord tipoTransacao = new tincRecord(DirectFundTransferHolder.TIPO_TRANSACAO, anyTipoTransacao);
		atributos.add(tipoTransacao);
		
		//Atribuindo o valor a ser inserido no Saldo Principal, caso aplicavel.
		if(this.valores.getValorPrincipal() != 0.0)
		{
			Any anyValorPrincipal = orb.create_any();
			anyValorPrincipal.insert_long((int)(this.valores.getValorPrincipal()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorPrincipal = new tincRecord(DirectFundTransferHolder.VALOR_PRINCIPAL, anyValorPrincipal);
			atributos.add(valorPrincipal);
		}
		
		//Atribuindo a data de expiracao do Saldo Principal, caso aplicavel.
		if(this.valores.getDataExpPrincipal() != null)
		{
			Any anyDataExpPrincipal = orb.create_any();
			DataTEC dataExp = new DataTEC(this.valores.getDataExpPrincipal());
			dataExp.inserir(anyDataExpPrincipal);
			tincRecord dataExpPrincipal = new tincRecord(DirectFundTransferHolder.DATA_EXPIRACAO_PRINCIPAL, anyDataExpPrincipal);
			atributos.add(dataExpPrincipal);
		}
		
		//Atribuindo o valor a ser inserido no Saldo Periodico, caso aplicavel.
		if(this.valores.getValorPeriodico() != 0.0)
		{
			Any anyValorPeriodico = orb.create_any();
			anyValorPeriodico.insert_long((int)(this.valores.getValorPeriodico()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorPeriodico = new tincRecord(DirectFundTransferHolder.VALOR_PERIODICO, anyValorPeriodico);
			atributos.add(valorPeriodico);
		}
		
		//Atribuindo a data de expiracao do Saldo Periodico, caso aplicavel.
		if(this.valores.getDataExpPeriodico() != null)
		{
			Any anyDataExpPeriodico = orb.create_any();
			DataTEC dataExp = new DataTEC(this.valores.getDataExpPeriodico());
			dataExp.inserir(anyDataExpPeriodico);
			tincRecord dataExpPeriodico = new tincRecord(DirectFundTransferHolder.DATA_EXPIRACAO_PERIODICO, anyDataExpPeriodico);
			atributos.add(dataExpPeriodico);
		}
		
		//Atribuindo o valor a ser inserido no Saldo de Bonus, caso aplicavel.
		if(this.valores.getValorBonus() != 0.0)
		{
			Any anyValorBonus = orb.create_any();
			anyValorBonus.insert_long((int)(this.valores.getValorBonus()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorBonus = new tincRecord(DirectFundTransferHolder.VALOR_BONUS, anyValorBonus);
			atributos.add(valorBonus);
		}
		
		//Atribuindo a data de expiracao do Saldo de Bonus, caso aplicavel.
		if(this.valores.getDataExpBonus() != null)
		{
			Any anyDataExpBonus = orb.create_any();
			DataTEC dataExp = new DataTEC(this.valores.getDataExpBonus());
			dataExp.inserir(anyDataExpBonus);
			tincRecord dataExpBonus = new tincRecord(DirectFundTransferHolder.DATA_EXPIRACAO_BONUS, anyDataExpBonus);
			atributos.add(dataExpBonus);
		}
		
		//Atribuindo o valor a ser inserido no Saldo de Torpedos, caso aplicavel.
		if(this.valores.getValorTorpedos() != 0.0)
		{
			Any anyValorTorpedos = orb.create_any();
			anyValorTorpedos.insert_long((int)(this.valores.getValorTorpedos()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorTorpedos = new tincRecord(DirectFundTransferHolder.VALOR_TORPEDOS, anyValorTorpedos);
			atributos.add(valorTorpedos);
		}
		
		//Atribuindo a data de expiracao do Saldo de Torpedos, caso aplicavel.
		if(this.valores.getDataExpTorpedos() != null)
		{
			Any anyDataExpTorpedos = orb.create_any();
			DataTEC dataExp = new DataTEC(this.valores.getDataExpTorpedos());
			dataExp.inserir(anyDataExpTorpedos);
			tincRecord dataExpTorpedos = new tincRecord(DirectFundTransferHolder.DATA_EXPIRACAO_TORPEDOS, anyDataExpTorpedos);
			atributos.add(dataExpTorpedos);
		}
		
		//Atribuindo o valor a ser inserido no Saldo de Dados, caso aplicavel.
		if(this.valores.getValorDados() != 0.0)
		{
			Any anyValorDados = orb.create_any();
			anyValorDados.insert_long((int)(this.valores.getValorDados()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorDados = new tincRecord(DirectFundTransferHolder.VALOR_DADOS, anyValorDados);
			atributos.add(valorDados);
		}
		
		//Atribuindo a data de expiracao do Saldo de Dados, caso aplicavel.
		if(this.valores.getDataExpDados() != null)
		{
			Any anyDataExpDados = orb.create_any();
			DataTEC dataExp = new DataTEC(this.valores.getDataExpDados());
			dataExp.inserir(anyDataExpDados);
			tincRecord dataExpDados = new tincRecord(DirectFundTransferHolder.DATA_EXPIRACAO_DADOS, anyDataExpDados);
			atributos.add(dataExpDados);
		}
		
		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}
	
}
