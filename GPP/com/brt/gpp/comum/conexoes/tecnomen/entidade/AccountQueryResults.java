package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import TINC.PE_AccountDetails;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe responsável por preencher os dados da consulta ao
 *  assinante num objeto do tipo Assinante.
 *
 *	@author		Vitor Murilo
 *	@since		07/12/2007
 */
public abstract class AccountQueryResults 
{
	
	/**
	 *	Cria e retorna um objeto Assinante a partir do retorno da consulta do PaymentInterface.accountQuery. 
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		retornoConsulta			Objeto com o conteudo do resultado da consulta.
	 *	@return		Objeto Assinante preenchido com o resultado da consulta.
	 */
	public static Assinante newAssinante(String msisdn, PE_AccountDetails retornoConsulta) throws GPPTecnomenException
	{
		if((retornoConsulta.result == Definicoes.RET_OPERACAO_OK) && ((retornoConsulta.profileId == 0) || (retornoConsulta.accountStatus == 0)))
			throw new GPPTecnomenException("Instabilidade no Payment Interface. Retorno inconsistente na consulta de assinantes.");
		
		Assinante assinante = new Assinante();
		assinante.setRetorno				((short) Definicoes.RET_OPERACAO_OK);
		assinante.setMSISDN					(msisdn);
		assinante.setPlanoPreco				(retornoConsulta.profileId);
		assinante.setStatusAssinante		(retornoConsulta.accountStatus);
		assinante.setStatusServico			(retornoConsulta.serviceStatus);
		assinante.setSaldoCreditosPrincipal	(retornoConsulta.accountBalance/(double)Definicoes.TECNOMEN_MULTIPLICADOR);
		assinante.setDataExpiracaoPrincipal	(new DataTEC(retornoConsulta.expiryDate).toDate());
		assinante.setSaldoCreditosBonus		(retornoConsulta.bonusBalance / (double)Definicoes.TECNOMEN_MULTIPLICADOR);
		assinante.setDataExpiracaoBonus		(new DataTEC(retornoConsulta.bonusExpiry).toDate());
		assinante.setSaldoCreditosDados		(retornoConsulta.dataBalance  / (double)Definicoes.TECNOMEN_MULTIPLICADOR);
		assinante.setDataExpiracaoDados		(new DataTEC(retornoConsulta.dataExpiry).toDate());
		assinante.setSaldoCreditosSMS		(retornoConsulta.smBalance    / (double)Definicoes.TECNOMEN_MULTIPLICADOR);
		assinante.setDataExpiracaoSMS		(new DataTEC(retornoConsulta.smExpiry).toDate());
		assinante.setSaldoCreditosPeriodico	(retornoConsulta.periodicBalance / (double)Definicoes.TECNOMEN_MULTIPLICADOR);
		assinante.setDataExpiracaoPeriodico	(new DataTEC(retornoConsulta.periodicExpiry).toDate());
		assinante.setStatusPeriodico		(retornoConsulta.periodicStatus);
		
		//Se nao possui status periodico para configurado (contornando limitacao da Plataforma
		// quanto a inexistencia de status nao aplicavel
		if(!MapPlanoPreco.getInstance().getPlanoPreco(retornoConsulta.profileId).possuiStatusPeriodico())
			assinante.setStatusPeriodico(Definicoes.STATUS_PERIODICO_NAO_APLICAVEL);
				
		return assinante;		
	}
	
	/**
	 *	Retorna uma representacao em formado String do retorno da consulta do PaymentInterface.accountQuery. 
	 *
	 *	@param		retornoConsulta			Objeto com o conteudo do resultado da consulta.
	 *	@return		Representacao em formado String do retorno da consulta do PaymentInterface.accountQuery.
	 */
	public static String toString(PE_AccountDetails retornoConsulta)
	{
		StringBuffer result = new StringBuffer("PE_AccountDetails [");
		
		result.append("[result: "             + retornoConsulta.result                          + "]");
		result.append("[transferResult: "     + retornoConsulta.transferResult                  + "]");
		result.append("[accountBalance: "     + retornoConsulta.accountBalance                  + "]");
		result.append("[accountStatus: "      + retornoConsulta.accountStatus                   + "]");
		result.append("[periodicStatus: "     + retornoConsulta.periodicStatus                  + "]");
		result.append("[serviceStatus: "      + retornoConsulta.serviceStatus                   + "]");
		result.append("[expiryDate: "         + new DataTEC(retornoConsulta.expiryDate)         + "]");
		result.append("[profileId: "          + retornoConsulta.profileId                       + "]");
		result.append("[subOptions: "         + retornoConsulta.subOptions                      + "]");
		result.append("[ivrQueryExpiryDate: " + new DataTEC(retornoConsulta.ivrQueryExpiryDate) + "]");
		result.append("[ivrQueryCounter: "    + retornoConsulta.ivrQueryCounter                 + "]");
		result.append("[amountBalance: "      + retornoConsulta.amountBalance                   + "]");
		result.append("[lastRechargeDate: "   + new DataTEC(retornoConsulta.lastRechargeDate)   + "]");
		result.append("[periodicBalance: "    + retornoConsulta.periodicBalance                 + "]");
		result.append("[periodicExpiry: "     + new DataTEC(retornoConsulta.periodicExpiry)     + "]");
		result.append("[bonusBalance: "       + retornoConsulta.bonusBalance                    + "]");
		result.append("[bonusExpiry: "        + new DataTEC(retornoConsulta.bonusExpiry)        + "]");
		result.append("[smBalance: "          + retornoConsulta.smBalance                       + "]");
		result.append("[smExpiry: "           + new DataTEC(retornoConsulta.smExpiry)           + "]");
		result.append("[dataBalance: "        + retornoConsulta.dataBalance                     + "]");
		result.append("[dataExpiry: "         + new DataTEC(retornoConsulta.dataExpiry)         + "]");
		result.append("[overdraftBalance: "   + retornoConsulta.overdraftBalance                + "]");
		
		result.append("]");
		
		return result.toString();
	}
	
}
