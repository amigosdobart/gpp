package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.util.ArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import TINC.tincRecord;
import TINC.tincSeqHolder;
import TINC.CreditAmountMapAttributesPackage.eCreditAmountMapAtt;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapCategoriaTEC;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;

/**
 *	Conversor de entidades <code>ValorVoucher</code> para a estrutura utilizada pelos servicos da 
 *	Tecnomen. A entidade ValorVoucher é um DE-PARA 
 *	de: categoria de serviço, valor de face
 *	para: valor de crédito do voucher, data de expiração
 *
 *	@author		Bernardo Vergne Dias
 *	@since		02/07/2007
 */
public class ValorVoucherHolder extends EntidadeGPPHolder
{

	/**
	 *	Constante interna da Tecnomen referente ao identificador do tipo de saldo.
	 */
	public static final int TIPO_SALDO = eCreditAmountMapAtt._AccountType;
	
	/**
	 *	Constante interna da Tecnomen referente ao formatado de moeda 
	 *  para o valor de crédito do voucher (actual_value).
	 */
	public static final int COD_MOEDA = eCreditAmountMapAtt._CurrencyCode;
	
	/**
	 *	Constante interna da Tecnomen referente ao tipo de voucher.
	 */
	public static final int TIPO_VOUCHER = eCreditAmountMapAtt._VoucherType;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor de face do voucher.
	 */
	public static final int VALOR_FACE = eCreditAmountMapAtt._FaceValue;
	
	/**
	 *	Constante interna da Tecnomen referente ao valor de crédito (valor atual) do voucher.
	 */
	public static final int VALOR_ATUAL = eCreditAmountMapAtt._ActualValue;
	
	/**
	 *	Constante interna da Tecnomen referente à expiração do voucher (para o tipo 
	 *  de saldo e categoria de servico em questao)
	 */
	public static final int DIAS_EXPIRACAO = eCreditAmountMapAtt._CreditExpiryTimer;
	
	/**
	 *	Constante interna da Tecnomen referente à categoria de serviço
	 */
	public static final int CATEGORIA_SERVICO = eCreditAmountMapAtt._ServiceId;
		
	/**
	 *	Constante interna da Tecnomen referente ao identificador do operador 
	 *  com acesso a execucao de consulta de creditAmountMap.
	 */
	public static final int OPERADOR = eCreditAmountMapAtt._OperatorId;
	
	/**
	 *	Identificador do operador com acesso a execucao de consulta de creditAmountMap.
	 *  Informação utilizada para gerar o objeto tincSeqHolder
	 */
	private int operador;
	
	/**
	 *	Entidade do GPP com as informacoes de valor de voucher.
	 */
	private ValorVoucher valorVoucher;
	
	/**
	 *	Entidade da Tecnomen com as informacoes de valor de voucher.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		valorVoucher			Informacoes referentes ao valor de crédito de voucher.
	 */
	public ValorVoucherHolder(ORB orb, Object valorVoucher)
	{
		super(orb);
		
		this.valorVoucher	= (ValorVoucher)valorVoucher;
		this.entidadeTEC	= new tincSeqHolder();
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com as informacoes de um assinante.
	 */
	public ValorVoucherHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.valorVoucher	= new ValorVoucher();
		this.entidadeTEC	= entidadeTEC;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeGPP()
	 */
	public Object toEntidadeGPP()
	{
		TipoSaldo		tipoSaldo			= null;
		CategoriaTEC 	categoriaTEC 		= null;
		int 			tipoVoucher			= 0;
		int 			valorFace			= 0;
		double			valorAtual			= 0.0;
		int 			diasExpiracao		= 0;
		
		tincRecord[] atributo = (this.entidadeTEC != null) ? this.entidadeTEC.value : null;
		for(int i = 0; ((atributo != null) && (i < atributo.length)); i++)
			switch(atributo[i].id)
			{
				case ValorVoucherHolder.TIPO_SALDO:
					short idtipoSaldoVoucher = atributo[i].value.extract_short();
					tipoSaldo = MapTipoSaldo.getInstance().getByIdTipoSaldoVoucher(idtipoSaldoVoucher);
					break;
				case ValorVoucherHolder.CATEGORIA_SERVICO:
					short idtCategoriaTEC = atributo[i].value.extract_short();
					categoriaTEC = MapCategoriaTEC.getInstance().getCategoriaTEC(idtCategoriaTEC);
					break;
				case ValorVoucherHolder.TIPO_VOUCHER:
					tipoVoucher = atributo[i].value.extract_short();
					break;
				case ValorVoucherHolder.VALOR_FACE:
					valorFace = atributo[i].value.extract_long();
					break;
				case ValorVoucherHolder.VALOR_ATUAL:
					valorAtual = (double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR;
					break;
				case ValorVoucherHolder.DIAS_EXPIRACAO:
					diasExpiracao = atributo[i].value.extract_long();
					break;
				default: break;
				
			}
		
		this.valorVoucher = new ValorVoucher();
		
		this.valorVoucher.setTipoSaldo(tipoSaldo);
		this.valorVoucher.setCategoriaTEC(categoriaTEC);
		this.valorVoucher.setTipoVoucher(tipoVoucher);
		this.valorVoucher.setValorAtual(valorAtual);
		this.valorVoucher.setValorFace(valorFace);
		this.valorVoucher.setDiasExpiracao(diasExpiracao);
		
		return this.valorVoucher;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		ORB orb = super.getOrb();
		ArrayList atributos = new ArrayList();
		
		/*
		 * Os procedimentos abaixo criam um objeto tincSeqHolder contendo
		 * os dados da entidade valorVoucher: 
		 * operador, tipoVoucher, tipoSaldo, valorFace, valorAtual, diasExpiracao
		 */
		
		
		// Atribuindo o tipoSaldo
		if (this.valorVoucher.getTipoSaldo() != null &&  
			this.valorVoucher.getTipoSaldo().getIdtTipoSaldoVoucher() >= 0)
		{
			Any anyTipoSaldo = orb.create_any();
			anyTipoSaldo.insert_short(this.valorVoucher.getTipoSaldo().getIdtTipoSaldoVoucher());
			tincRecord tipoSaldoRecord = new tincRecord(ValorVoucherHolder.TIPO_SALDO, anyTipoSaldo);
			atributos.add(tipoSaldoRecord);
		}
		
		// Atribuindo o operador (obrigatório)
		Any anyOperador = orb.create_any();
		anyOperador.insert_long(this.operador);
		tincRecord operadorRecord = new tincRecord(ValorVoucherHolder.OPERADOR, anyOperador);
		atributos.add(operadorRecord);

		// Atribuindo a categoria de serviço (serviceId)
		if (this.valorVoucher.getCategoriaTEC() != null)
			{
				Any anyCategoria = orb.create_any();
				anyCategoria.insert_short(this.valorVoucher.getCategoriaTEC().getIdtCategoria());
				tincRecord tipoCategoria = new tincRecord(ValorVoucherHolder.CATEGORIA_SERVICO, anyCategoria);
				atributos.add(tipoCategoria);
			}
		
		// Atribuindo o tipoVoucher
		if (this.valorVoucher.getTipoVoucher() >= 0)
		{
			Any anyTipoVoucher = orb.create_any();
			anyTipoVoucher.insert_short((short)this.valorVoucher.getTipoVoucher());
			tincRecord tipoVoucherRecord = new tincRecord(ValorVoucherHolder.TIPO_VOUCHER, anyTipoVoucher);
			atributos.add(tipoVoucherRecord);
		}
		
		// Atribuindo o valorFace
		if (this.valorVoucher.getValorFace() >= 0)
		{
			Any anyValorFace = orb.create_any();
			anyValorFace.insert_long(this.valorVoucher.getValorFace());
			tincRecord valorFaceRecord = new tincRecord(ValorVoucherHolder.VALOR_FACE, anyValorFace);
			atributos.add(valorFaceRecord);
		}
		
		//Atribuindo o valorAtual
		if (this.valorVoucher.getValorAtual() >= 0)
		{
			Any anyValorAtual = orb.create_any();
			anyValorAtual.insert_long((int)(this.valorVoucher.getValorAtual()*Definicoes.TECNOMEN_MULTIPLICADOR));
			tincRecord valorAtualRecord = new tincRecord(ValorVoucherHolder.VALOR_ATUAL, anyValorAtual);
			atributos.add(valorAtualRecord);
		}
		
		// Atribuindo os dias de Expiracao
		if (this.valorVoucher.getDiasExpiracao() >= 0)
		{
			Any anyDiasExpiracao = orb.create_any();
			anyDiasExpiracao.insert_long(this.valorVoucher.getDiasExpiracao());
			tincRecord diasExpiracaoRecord = new tincRecord(ValorVoucherHolder.DIAS_EXPIRACAO, anyDiasExpiracao);
			atributos.add(diasExpiracaoRecord);
		}

		//Preenchendo o objeto a ser enviado a Tecnomen.
		this.entidadeTEC.value = new tincRecord[atributos.size()];
		
		for(int i = 0; i < atributos.size(); i++)
			this.entidadeTEC.value[i] = (tincRecord)atributos.get(i);
		
		return this.entidadeTEC;
	}

	/**
	 * @return Identificador do operador (usuario Tecnomen).
	 */
	public int getOperador() 
	{
		return this.operador;
	}

	/**
	 * @param operador Identificador do operador (usuario Tecnomen)
	 */
	public void setOperador(int operador) 
	{
		this.operador = operador;
	}
	
	
}
