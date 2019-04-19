package com.brt.gpp.comum.conexoes.tecnomen.holder;

import org.omg.CORBA.ORB;

import TINC.FundTransferOpPackage.eResultSequence;
import TINC.tincRecord;
import TINC.tincSeqHolder;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DataTEC;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.FundTransferResults;
import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Classe que encapsula informacoes referentes ao resultado das operacoes de recargas/ajustes. Recebe o objeto de 
 *	saida do metodo fundTransfer do Payment Engine e o converte em informacoes do assinante para o GPP.
 *
 *	OBS: No momento da implementacao a classe e utilizada somente para passagem de parametro de um objeto a ser 
 *	preenchido pela Tecnomen. O resultado nao e passado para o GPP.
 * 
 *	@author		Daniel Ferreira
 *	@since		02/04/2007
 */
public class FundTransferResultsHolder extends EntidadeGPPHolder 
{

	/**
	 *	Constante interna da Tecnomen referente ao status do assinante.
	 */
	public static final int STATUS = eResultSequence._r_AccountStatus;
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo Principal do assinante.
	 */
	public static final int SALDO_PRINCIPAL = eResultSequence._r_AccountBalance;
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo Periodico do assinante.
	 */
	public static final int SALDO_PERIODICO = eResultSequence._r_PeriodicBalance;
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo de Bonus do assinante.
	 */
	public static final int SALDO_BONUS = eResultSequence._r_BonusBalance;
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo de Torpedos do assinante.
	 */
	public static final int SALDO_TORPEDOS = eResultSequence._r_SmBalance;
	
	/**
	 *	Constante interna da Tecnomen referente ao Saldo de Dados do assinante.
	 */
	public static final int SALDO_DADOS = eResultSequence._r_DataBalance;
	
	/**
	 *	Constante interna da Tecnomen referente a data de expiracao do Saldo Principal do assinante.
	 */
	public static final int EXPIRACAO_PRINCIPAL = eResultSequence._r_ExpiryDate;
	
	/**
	 *	Constante interna da Tecnomen referente a data de expiracao do Saldo Periodico do assinante.
	 */
	public static final int EXPIRACAO_PERIODICO = eResultSequence._r_PeriodicExpiry;
	
	/**
	 *	Constante interna da Tecnomen referente a data de expiracao do Saldo de Bonus do assinante.
	 */
	public static final int EXPIRACAO_BONUS = eResultSequence._r_BonusExpiry;
	
	/**
	 *	Constante interna da Tecnomen referente a data de expiracao do Saldo de Torpedos do assinante.
	 */
	public static final int EXPIRACAO_TORPEDOS = eResultSequence._r_SmExpiry;
	
	/**
	 *	Constante interna da Tecnomen referente a data de expiracao do Saldo de Dados do assinante.
	 */
	public static final int EXPIRACAO_DADOS = eResultSequence._r_DataExpiry;
	
	/**
	 *	Entidade do GPP com o resultado da operacao de recarga/ajuste.
	 */
	private FundTransferResults resultado;
	
	/**
	 *	Entidade da Tecnomen com as informacoes do usuario.
	 */
	private tincSeqHolder entidadeTEC;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		resultado				Entidade do GPP com o resultado da operacao de recarga/ajuste. 
	 */
	public FundTransferResultsHolder(ORB orb, Object resultado)
	{
		super(orb);
		
		this.resultado		= (FundTransferResults)resultado;
		this.entidadeTEC	= new tincSeqHolder(new tincRecord[0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		entidadeTEC				Entidade da Tecnomen com informacoes de usuario.
	 */
	public FundTransferResultsHolder(ORB orb, tincSeqHolder entidadeTEC)
	{
		super(orb);
		
		this.resultado		= new FundTransferResults();
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
				case FundTransferResultsHolder.STATUS:
					this.resultado.setStatusAssinante(atributo[i].value.extract_short());
					break;
				case FundTransferResultsHolder.SALDO_PRINCIPAL:
					this.resultado.setSaldoPrincipal((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case FundTransferResultsHolder.SALDO_PERIODICO:
					this.resultado.setSaldoPeriodico((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case FundTransferResultsHolder.SALDO_BONUS:
					this.resultado.setSaldoBonus((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case FundTransferResultsHolder.SALDO_TORPEDOS:
					this.resultado.setSaldoTorpedos((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case FundTransferResultsHolder.SALDO_DADOS:
					this.resultado.setSaldoDados((double)atributo[i].value.extract_long()/Definicoes.TECNOMEN_MULTIPLICADOR);
					break;
				case FundTransferResultsHolder.EXPIRACAO_PRINCIPAL:
					DataTEC dataExpPrincipal = new DataTEC();
					dataExpPrincipal.extrair(atributo[i].value);
					this.resultado.setDataExpPrincipal(dataExpPrincipal.toDate());
					break;
				case FundTransferResultsHolder.EXPIRACAO_PERIODICO:
					DataTEC dataExpPeriodico = new DataTEC();
					dataExpPeriodico.extrair(atributo[i].value);
					this.resultado.setDataExpPeriodico(dataExpPeriodico.toDate());
					break;
				case FundTransferResultsHolder.EXPIRACAO_BONUS:
					DataTEC dataExpBonus = new DataTEC();
					dataExpBonus.extrair(atributo[i].value);
					this.resultado.setDataExpBonus(dataExpBonus.toDate());
					break;
				case FundTransferResultsHolder.EXPIRACAO_TORPEDOS:
					DataTEC dataExpTorpedos = new DataTEC();
					dataExpTorpedos.extrair(atributo[i].value);
					this.resultado.setDataExpTorpedos(dataExpTorpedos.toDate());
					break;
				case FundTransferResultsHolder.EXPIRACAO_DADOS:
					DataTEC dataExpDados = new DataTEC();
					dataExpDados.extrair(atributo[i].value);
					this.resultado.setDataExpDados(dataExpDados.toDate());
					break;
				default: break;
			}
		
		return this.resultado;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.entidade.EntidadeGPPHolder#toEntidadeTEC()
	 */
	public tincSeqHolder toEntidadeTEC()
	{
		//Como este e um objeto de saida da operacao de recarga/ajuste na Tecnomen, nao e necessario montar o objeto 
		//com as informacoes do assinante.
		return this.entidadeTEC;
	}
	
}
