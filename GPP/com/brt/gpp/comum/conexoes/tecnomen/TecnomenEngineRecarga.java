package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.Date;

import TINC.PE_AccountDetails;
import TINC.PaymentEngine;
import TINC.PaymentEngine_Factory;
import TINC.PaymentEngine_FactoryHelper;
import TINC.Pi_exception;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.totalizadorTecnomen.TotalizadorAPITecnomen;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorExcecaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorRetornoRecarga;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.AccountQueryResults;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DataTEC;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DirectFundTransfer;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.FundTransferResults;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.MSISDN;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.OnlineFundTransfer;
import com.brt.gpp.comum.conexoes.tecnomen.holder.DirectFundTransferHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.FundTransferResultsHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.OnlineFundTransferHolder;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;

/**
 * 
 */
public class TecnomenEngineRecarga extends ConexaoTecnomen
{
	
	/**
	 *	Fabrica de interfaces com o Payment Interface.
	 */
	private PaymentEngine engine;
	
	/**
	 *	Interface com o Payment Interface.
	 */
	private PaymentEngine_Factory fabrica;
	
	/**
	 * Construtor da classe
	 * @param idServico
	 */
	public TecnomenEngineRecarga(int idServico)
	{
		super(idServico);
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if((autenticador == null) || (autenticador.getIor() == null))
			throw new GPPTecnomenException("Payment Interface nao encontrado");
		
		//Obtendo a referencia ao Factory do Payment Interface da Tecnomen.
		org.omg.CORBA.Object objPayment = super.getOrb().string_to_object(autenticador.getIor());
		PaymentEngine_Factory fabrica = PaymentEngine_FactoryHelper.narrow(objPayment);

		try
		{
			//Obtendo a referencia ao Payment Interface da Tecnomen.
			this.engine = fabrica.getPaymentEngine(autenticador.getChave());
		}
		catch(Pi_exception e)
		{
			throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
		}
		
		//Atualizando o autenticador e a fabrica.
		this.fabrica = fabrica;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#releaseEngine()
	 */
	public void releaseEngine(Autenticador autenticador)
	{
		if(this.isAtivo())
		{
			try
			{
				this.fabrica.releasePaymentEngine(autenticador.getChave(), this.engine.engineId());
			}
			catch(Pi_exception e)
			{
				super.log(Definicoes.DEBUG, "setEngine", "Excecao: " + e);
				super.log(Definicoes.WARN, "fechar", "Conexao com Payment Interface nao fechada corretamente: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			}
		}
		
		this.fabrica	= null;
		this.engine		= null;
	}

	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#isAtivo()
	 */
	public boolean isAtivo()
	{
		if(this.engine == null)
			return false;
		
		try
		{
			this.engine.version();
			return true;
		}
		catch(Exception e)
		{
			super.log(Definicoes.DEBUG, "isAtivo", "Conexao " + this.idServico + " nao ativa. Erro: " + e);
			return false;
		}
	}

	/**
	 *	Realiza recarga nos multiplos saldos dos assinantes.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Objeto contendo valores e numero de dias de expiracao para a recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short executarRecarga(Assinante assinante, OnlineFundTransfer valores)
	{
		//Criando o holder de passagem de parametro para a operacao onlineFundTransfer do Payment Interface.
		OnlineFundTransferHolder holder = new OnlineFundTransferHolder(super.getOrb(), valores);
		holder.setIdOperador(Definicoes.GPP_OPERATOR_ID);
		
		//Criando o holder de recebimento de informacoes de assinantes apos a operacao de recarga.
		FundTransferResultsHolder resultHolder = new FundTransferResultsHolder(super.getOrb(), new FundTransferResults());  
		
		try
		{
			super.log(Definicoes.INFO, "executarRecarga", assinante + " - Operacao: PaymentInterface.fundTransfer <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPaymentEngine("fundTransfer");
			//Executando a operacao de insercao de creditos.
			int retorno = this.engine.fundTransfer(holder.toEntidadeTEC().value, resultHolder.toEntidadeTEC()).value();
			
			//No caso de operacao OK, e necessario atualizar o assinante a partir do resultado fornecido pela
			//Tecnomen. O holder fornece o objeto que possui o resultado.
			if(ConversorRetornoRecarga.toRetornoGPP(retorno) == Definicoes.RET_OPERACAO_OK)
				((FundTransferResults)resultHolder.toEntidadeGPP()).atualizarAssinante(assinante);
			else
				super.log(Definicoes.WARN, "executarRecarga", "MSISDN: " + valores.getMsisdn() + " - Retorno Tecnomen Payment Interface: " + retorno);
			
			return ConversorRetornoRecarga.toRetornoGPP(retorno);
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "executarRecarga", "MSISDN: " + valores.getMsisdn() + " - Excecao Tecnomen Payment Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoRecarga.toRetornoGPP(e.error_code);
		}
	}
	
	/**
	 *	Realiza recarga de franquia no Saldo Periodico do assinante. Recebe como parametro o objeto ValoresRecarga e
	 *	constroi um objeto DirectFundTransfer a realizacao da operacao junto a Tecnomen. O metodo NAO atualiza as 
	 *	datas de expiracao.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Objeto contendo valores e numero de dias de expiracao da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short executarRecargaFranquia(Assinante assinante, ValoresRecarga valores)
	{
		//Construindo o objeto DirectFundTransfer.
		DirectFundTransfer fundTransfer = new DirectFundTransfer();
		
		fundTransfer.setMsisdn          (assinante.getMSISDN      ());
		fundTransfer.setValorPrincipal  (valores.getSaldoPrincipal());
		fundTransfer.setValorPeriodico  (valores.getSaldoPeriodico());
		fundTransfer.setValorBonus      (valores.getSaldoBonus    ());
		fundTransfer.setValorTorpedos   (valores.getSaldoSMS      ());
		fundTransfer.setValorDados      (valores.getSaldoGPRS     ());
		
		//Executando a operacao de recarga de franquia.
		return this.executarRecargaFranquia(assinante, fundTransfer);
	}
	
	/**
	 *	Realiza recarga de franquia no Saldo Periodico do assinante.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Objeto contendo valores e numero de dias de expiracao para a recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short executarRecargaFranquia(Assinante assinante, DirectFundTransfer valores)
	{
		//Criando o holder de passagem de parametro para a operacao onlineFundTransfer do Payment Interface. A recarga de 
		//franquia insere creditos no Saldo Periodico. A operacao OFT
		DirectFundTransferHolder holder = new DirectFundTransferHolder(super.getOrb(), valores);
		holder.setIdOperador(Definicoes.GPP_OPERATOR_ID);
		
		//Criando o holder de recebimento de informacoes de assinantes apos a operacao de recarga.
		FundTransferResultsHolder resultHolder = new FundTransferResultsHolder(super.getOrb(), new FundTransferResults());  
		
		try
		{
			super.log(Definicoes.INFO, "executarRecargaFranquia", assinante + " - Operacao: PaymentInterface.fundTransfer <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPaymentEngine("fundTransfer");
			//Executando a operacao de insercao de creditos.
			int retorno = this.engine.fundTransfer(holder.toEntidadeTEC().value, resultHolder.toEntidadeTEC()).value();
			
			//No caso de operacao OK, e necessario atualizar o assinante a partir do resultado fornecido pela
			//Tecnomen. O holder fornece o objeto que possui o resultado.
			if(ConversorRetornoRecarga.toRetornoGPP(retorno) == Definicoes.RET_OPERACAO_OK)
				((FundTransferResults)resultHolder.toEntidadeGPP()).atualizarAssinante(assinante);
			else
				super.log(Definicoes.WARN, "executarRecargaFranquia", "MSISDN: " + valores.getMsisdn() + " - Retorno Tecnomen Payment Interface: " + retorno);
			
			return ConversorRetornoRecarga.toRetornoGPP(retorno);
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "executarRecargaFranquia", "MSISDN: " + valores.getMsisdn() + " - Excecao Tecnomen Payment Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoRecarga.toRetornoGPP(e.error_code);
		}
	}

	/**
	 *	Realiza ajuste nos multiplos saldos dos assinantes.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Objeto contendo valores e datas de expiracao para o ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short executarAjuste(Assinante assinante, DirectFundTransfer valores)
	{
		//Criando o holder de passagem de parametro para a operacao directFundTransfer do Payment Interface.
		DirectFundTransferHolder holder = new DirectFundTransferHolder(super.getOrb(), valores);
		holder.setIdOperador(Definicoes.GPP_OPERATOR_ID);
		
		//Criando o holder de recebimento de informacoes de assinantes apos a operacao de ajuste.
		FundTransferResultsHolder resultHolder = new FundTransferResultsHolder(super.getOrb(), new FundTransferResults());  
		
		try
		{
			super.log(Definicoes.INFO, "executarAjuste", assinante + " - Operacao: PaymentInterface.fundTransfer <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPaymentEngine("fundTransfer");
			//Executando a operacao de insercao de creditos.
			int retorno = this.engine.fundTransfer(holder.toEntidadeTEC().value, resultHolder.toEntidadeTEC()).value();
			
			//No caso de operacao OK, e necessario atualizar o assinante a partir do resultado fornecido pela
			//Tecnomen. O holder fornece o objeto que possui o resultado.
			if(ConversorRetornoRecarga.toRetornoGPP(retorno) == Definicoes.RET_OPERACAO_OK)
				((FundTransferResults)resultHolder.toEntidadeGPP()).atualizarAssinante(assinante);
			else
				super.log(Definicoes.WARN, "executarAjuste", "MSISDN: " + valores.getMsisdn() + " - Retorno Tecnomen Payment Interface: " + retorno);
			
			return ConversorRetornoRecarga.toRetornoGPP(retorno);
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "executarAjuste", "MSISDN: " + valores.getMsisdn() + " - Excecao Tecnomen Payment Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoRecarga.toRetornoGPP(e.error_code);
		}
	}
	
	/**
	 * Metodo....:consultarAssinante
	 * Descricao.:Retorna o objeto Assinante a partir de consulta utilizando a PaymentInterface
	 * @param numeroAssinante	- Numero do assinante MSISDN
	 * @return Assinante		- Objeto Assinante com dados retornados da plataforma Tecnomen
	 * @throws Pi_exception
	 * @throws GPPInternalErrorException
	 */
	public Assinante consultarAssinante(String msisdn) throws GPPTecnomenException
	{
		try
		{
			TotalizadorAPITecnomen.getInstance().incrementarPaymentEngine("accountQuery");
			PE_AccountDetails retorno = this.engine.accountQuery(new MSISDN(msisdn).toMsisdnTEC(), Definicoes.GPP_OPERATOR_ID);
						
			//Interpretando o retorno da operacao.
			switch(ConversorRetornoRecarga.toRetornoGPP(retorno.result))
			{
				case Definicoes.RET_OPERACAO_OK:
					super.log(Definicoes.INFO, "consultarAssinante", "MSISDN: " + msisdn + " - Operacao: PaymentInterface.accountQuery <Retorno> " + AccountQueryResults.toString(retorno));
					return AccountQueryResults.newAssinante(msisdn, retorno);
				case Definicoes.RET_MSISDN_NAO_ATIVO:
					return null;
				default:
					throw ConversorExcecaoTecnomen.newGPPTecnomenException(retorno.result); 
			}			
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.DEBUG, "consultarAssinante", "MSISDN: " + msisdn + " - Excecao: " + e);
			throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
		}
	}
	
	/**
	 * Metodo....:atualizarAssinante
	 * Descricao.:Atualiza informacoes do assinante
	 * @param msisdn : Número do MSISDN do Assinante
	 * @param statusAssinante : Status do Assinante
	 * @param expPrincipal : Data de Expiração do saldo Principal 
	 * @param statusPeriodico : Status Periódico do Assinante
	 * @param expPeriodico : Data de Expiração do Saldo Periódico
	 * @param expBonus : Data de Expiração do Saldo de Bônus
	 * @param expSMS : Data de Expiração do Saldo de SMS
	 * @param expDados : Data de Expiração do Saldo de Dados
	 * @return short : identificador da correta atualização do cliente
	 */
	public short atualizarStatusAssinante(String msisdn, short statusAssinante, Date dataExpPrincipal, short statusPeriodico,
			Date dataExpPeriodico, Date dataExpBonus, Date dataExpSMS, Date dataExpDados)
	{
		try 
		{
			TotalizadorAPITecnomen.getInstance().incrementarPaymentEngine("accountUpdate");
			PE_AccountDetails retornoConsulta = 
				this.engine.accountUpdate(new MSISDN(msisdn).toMsisdnTEC()
										, statusAssinante
										, new DataTEC(dataExpPrincipal).toTDateTime()
										, (statusPeriodico == Definicoes.STATUS_PERIODICO_NAO_APLICAVEL) ? Definicoes.STATUS_PERIODICO_FIRST_TIME_USER : statusPeriodico
										, new DataTEC(dataExpPeriodico).toTDateTime()
										, new DataTEC(dataExpBonus).toTDateTime()
										, new DataTEC(dataExpSMS).toTDateTime()
										, new DataTEC(dataExpDados).toTDateTime()
										, (short)0
										, Definicoes.GPP_OPERATOR_ID);
			
			if(ConversorRetornoRecarga.toRetornoGPP(retornoConsulta.result) != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "atualizarStatusAssinante", "MSISDN: " + msisdn + " - Retorno Tecnomen Payment Interface: " + retornoConsulta.result);
			
			return ConversorRetornoRecarga.toRetornoGPP(retornoConsulta.result);
			
		} 
		catch (Pi_exception e) 
		{
			super.log(Definicoes.ERRO, "atualizarAssinante", "MSISDN: " + msisdn + " - Excecao Tecnomen Payment Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoRecarga.toRetornoGPP(e.error_code);
		}		
	}
	
}