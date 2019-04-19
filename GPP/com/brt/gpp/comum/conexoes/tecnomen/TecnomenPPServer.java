package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import TINC.Pe_exception;
import TINC.TPPServer_Factory;
import TINC.TPPServer_FactoryHelper;
import TINC.TPPSubscriberMgr;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.aplicacoes.aprovisionar.ServicoTECAssinante;
import com.brt.gpp.aplicacoes.totalizadorTecnomen.TotalizadorAPITecnomen;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorExcecaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorRetornoAprovisionamento;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.holder.AssinanteHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.ColecaoEntidadeGPPHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.SaldoAssinanteHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.ServicoTECAssinanteHolder;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

/**
 *	Conexao com o Prepaid Interface da Tecnomen. Este servico e responsavel pela consulta e atualizacao de assinantes.
 *
 *	@author		Daniel Ferreira
 *	@since		26/02/2007
 */
public class TecnomenPPServer extends ConexaoTecnomen 
{

	/**
	 *	Fabrica de interfaces com o Prepaid Interface.
	 */
	private TPPServer_Factory fabrica;
	
	/**
	 *	Interface com o Provision Server.
	 */
	private TPPSubscriberMgr engine;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenPPServer()
	{
		super(ConexaoTecnomen.PP_SERVER);
		
		this.fabrica	= null;
		this.engine		= null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idServico				Identificador do servico da Tecnomen
	 */
	public TecnomenPPServer(int idServico)
	{
		super(idServico);
		
		this.fabrica 	= null;
		this.engine 	= null;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if((autenticador == null) || (autenticador.getIor() == null))
			throw new GPPTecnomenException("Prepaid Interface nao encontrado");
		
		org.omg.CORBA.Object objPPServer = super.getOrb().string_to_object(autenticador.getIor());
		TPPServer_Factory fabrica = TPPServer_FactoryHelper.narrow(objPPServer);

		try
		{
			this.engine = fabrica.getTPPSubscriberMgr(autenticador.getChave(), autenticador.getIdUsuario());
		}
		catch(Pe_exception e)
		{
			super.log(Definicoes.DEBUG, "setEngine", "Excecao: " + e);
			throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
		}
		
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
				this.fabrica.releaseTPPSubscriberMgr(autenticador.getChave(), this.engine.engineId());
			}
			catch(Pe_exception e)
			{
				super.log(Definicoes.WARN, "fechar", "Conexao com Prepaid Interface nao fechada corretamente: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
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
			return false;
		}
	}
	
	/**
	 *	Executa a consulta de um assinante na plataforma Tecnomen.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@return		Informacoes do assinante na plataforma Tecnomen.
	 *	@throws		GPPTecnomenException 
	 */
	public Assinante getAssinante(String msisdn) throws GPPTecnomenException
	{
		//Criando o holder para conversao do objeto Assinante em entidade da Tecnomen.
		AssinanteHolder holder = new AssinanteHolder(super.getOrb());
		holder.setMsisdn(msisdn);
		try
		{
			super.log(Definicoes.DEBUG, "getAssinante", "MSISDN: " + msisdn + " - Operacao: PrepaidInterface.getSuscriberDetails <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPPServer("getSubscriberDetails");
			//Executando a consulta do assinante no Prepaid Interface.
			short retorno = this.engine.getSubscriberDetails(holder.toEntidadeTEC());
			
			//Interpretando o retorno da operacao.
			switch(ConversorRetornoAprovisionamento.toRetornoGPP(retorno))
			{
				case Definicoes.RET_OPERACAO_OK:
					Assinante result = (Assinante)holder.toEntidadeGPP();
					result.extrairSaldos(this.getSaldosAssinante(result));
					return result;
				case Definicoes.RET_MSISDN_NAO_ATIVO:
					return null;
				default:
					throw ConversorExcecaoTecnomen.newGPPTecnomenException(retorno); 
			}
		}
		catch(Pe_exception e)
		{
			super.log(Definicoes.DEBUG, "getAssinante", "MSISDN: " + msisdn + " - Excecao Tecnomen Prepaid Interface: " + e);
			throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
		}
	}
	
	/**
	 *	Executa a consulta dos saldos de um assinante na plataforma Tecnomen.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma Tecnomen.
	 *	@return		Lista de informacoes dos saldos do assinante na plataforma Tecnomen.
	 *	@throws		GPPTecnomenException 
	 */
	public Collection getSaldosAssinante(Assinante assinante) throws GPPTecnomenException
	{
		ArrayList result = new ArrayList();
		
		//Obtendo os saldos associados a categoria do assinante.
		Collection saldos = SaldoAssinante.newSaldosAssinante(assinante);
		for(Iterator iterator = saldos.iterator(); iterator.hasNext();)
		{
			//Criando o holder para passagem de parametro na chamada do metodo de consulta de saldos do assinante. 
			SaldoAssinanteHolder holder = new SaldoAssinanteHolder(super.getOrb(), iterator.next());
			//Criando o holder para recebimento da lista de saldos do assinante.
			ColecaoEntidadeGPPHolder colSaldos = new ColecaoEntidadeGPPHolder(super.getOrb(), SaldoAssinanteHolder.class);

			try
			{
				super.log(Definicoes.DEBUG, "getSaldosAssinante", assinante + " - Operacao: PrepaidInterface.getSuscriberAccountDetails <Parametros> " +  holder);
				//Incrementando o totalizador de operacoes da Tecnomen.
				TotalizadorAPITecnomen.getInstance().incrementarPPServer("getSubscriberAccountDetails");
				//Executando a consulta do saldo do assinante no Prepaid Interface.
				short retorno = this.engine.getSubscriberAccountDetails(holder.toEntidadeTEC(), colSaldos.toColecaoEntidadeTEC());
				
				switch(retorno)
				{
					case Definicoes.RET_OPERACAO_OK:
						result.addAll(colSaldos.toColecaoEntidadeGPP());
						break;
					case Definicoes.RET_MSISDN_NAO_ATIVO:
						break;
					default:
						throw ConversorExcecaoTecnomen.newGPPTecnomenException(retorno); 
				}
			}
			catch(Pe_exception e)
			{
				super.log(Definicoes.DEBUG, "getSaldosAssinante", assinante.toString() + " - Excecao Tecnomen Prepaid Interface: " + e);
				throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
			}
		}

		return result;
	}
	
	/**
	 *	Executa a consulta dos servicos disponibilizados a um assinante na plataforma Tecnomen. Exemplos de servicos 
	 *	sao: Voice Mail, SMMO, SMMT, GPRS, MMS, etc. Tais servicos devem ter sido devidamente ativados  ao assinante 
	 *	(ver ativarAssinante() em TecnomenProvisionServer) para que possam ser consultados. 
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma Tecnomen.
	 *	@return		Lista de servicos disponibilizados ao assinante na plataforma Tecnomen.
	 *	@throws		GPPTecnomenException 
	 */
	public Collection getServicosAssinante(Assinante assinante) throws GPPTecnomenException
	{
		ArrayList result = new ArrayList();
		
		//Obtendo os servicos da Tecnomen associados a categoria do assinante.
		Collection servicos = ServicoTECAssinante.newServicosTECAssinante(assinante);
		for(Iterator iterator = servicos.iterator(); iterator.hasNext();)
		{
			//Criando o holder para passagem de parametro na chamada do metodo de consulta de saldos do assinante. 
			ServicoTECAssinanteHolder holder = new ServicoTECAssinanteHolder(super.getOrb(), iterator.next());
			//Criando o holder para recebimento da lista de saldos do assinante.
			ColecaoEntidadeGPPHolder colServicos = new ColecaoEntidadeGPPHolder(super.getOrb(), ServicoTECAssinanteHolder.class);
			
			try
			{
				super.log(Definicoes.DEBUG, "getServicosAssinante", assinante + " - Operacao: PrepaidInterface.getSuscriberServiceDetails <Parametros> " +  holder);
				//Incrementando o totalizador de operacoes da Tecnomen.
				TotalizadorAPITecnomen.getInstance().incrementarPPServer("getSubscriberServiceDetails");
				//Executando a consulta do servico do assinante no Prepaid Interface.
				short retorno = this.engine.getSubscriberServiceDetails(holder.toEntidadeTEC(), colServicos.toColecaoEntidadeTEC());
				
				switch(retorno)
				{
					case Definicoes.RET_OPERACAO_OK:
						result.addAll(colServicos.toColecaoEntidadeGPP());
						break;
					case Definicoes.RET_MSISDN_NAO_ATIVO:
						break;
					default:
						throw ConversorExcecaoTecnomen.newGPPTecnomenException(retorno); 
				}
			}
			catch(Pe_exception e)
			{
				super.log(Definicoes.DEBUG, "getServicosAssinante", assinante.toString() + " - Excecao Tecnomen Prepaid Interface: " + e);
				throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
			}
		}

		return result;
	}
	
	/**
	 *	Executa a atualizacao de um assinante na plataforma Tecnomen.
	 *
	 *	@param		assinante				MSISDN do assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short atualizarAssinante(Assinante assinante)
	{
		//Criando o holder para conversao do objeto Assinante em entidade da Tecnomen.
		AssinanteHolder holder = new AssinanteHolder(super.getOrb(), assinante);
		holder.setMsisdn(assinante.getMSISDN());
		
		try
		{
			super.log(Definicoes.INFO, "atualizarAssinante", assinante + " - Operacao: PrepaidInterface.updateSuscriberDetails <Parametros> " +  holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPPServer("updateSubscriberDetails");
			//Executando a atualizacao do assinante.
			short retorno = this.engine.updateSubscriberDetails(holder.toEntidadeTEC());
			
			if(ConversorRetornoAprovisionamento.toRetornoGPP(retorno) != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "atualizarAssinante", assinante + " - Retorno Tecnomen Prepaid Interface: " + retorno);
			
			return ConversorRetornoAprovisionamento.toRetornoGPP(retorno);
		}
		catch(Pe_exception e)
		{
			super.log(Definicoes.ERRO, "atualizarAssinante", "MSISDN: " + assinante.getMSISDN() + " - Excecao Tecnomen Prepaid Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoAprovisionamento.toRetornoGPP(e.error_code);
		}
	}
	
	/**
	 *	Executa a atualizacao do saldo de um assinante na plataforma Tecnomen.
	 *
	 *	@param		saldo					Informacoes de saldo de um assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short atualizarSaldoAssinante(SaldoAssinante saldo)
	{
		//Criando o holder para conversao de objetos SaldoAssinante em entidade da Tecnomen.
		SaldoAssinanteHolder holder = new SaldoAssinanteHolder(super.getOrb(), saldo);
		
		try
		{
			super.log(Definicoes.INFO, "atualizarSaldoAssinante", "MSISDN: " + saldo.getMsisdn() + " - Operacao: PrepaidInterface.getSuscriberAccountDetails <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPPServer("updateSubscriberAccountDetails");
			//Executando a atualizacao do saldo do assinante.
			short retorno = this.engine.updateSubscriberAccountDetails(holder.toEntidadeTEC());
			
			if(ConversorRetornoAprovisionamento.toRetornoGPP(retorno) != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "atualizarSaldoAssinante", "MSISDN: " + saldo.getMsisdn() + " - Retorno Tecnomen Prepaid Interface: " + retorno);
			
			return ConversorRetornoAprovisionamento.toRetornoGPP(retorno);
		}
		catch(Pe_exception e)
		{
			super.log(Definicoes.ERRO, "atualizarSaldoAssinante", "MSISDN: " + saldo.getMsisdn() + " - Excecao Tecnomen Prepaid Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoAprovisionamento.toRetornoGPP(e.error_code);
		}
	}
	
	/**
	 *	Executa a atualizacao do servico (Voice Mail, MMS, GPRS, etc.) disponibilizado a um assinante na plataforma 
	 *	Tecnomen.
	 *
	 *	@param		servico					Informacoes do servico disponibilizado a um assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short atualizarServicoAssinante(ServicoTECAssinante servico)
	{
		//Criando o holder para conversao de objetos ServicoTECAssinante em entidade da Tecnomen.
		ServicoTECAssinanteHolder holder = new ServicoTECAssinanteHolder(super.getOrb(), servico);
		
		try
		{
			super.log(Definicoes.INFO, "atualizarServicoAssinante", "MSISDN: " + servico.getMsisdn() + " - Operacao: PrepaidInterface.getSuscriberServiceDetails <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarPPServer("updateSubscriberServiceDetails");
			//Executando a atualizacao do servico disponibilizado ao assinante.
			short retorno = this.engine.updateSubscriberServiceDetails(holder.toEntidadeTEC());
			
			if(ConversorRetornoAprovisionamento.toRetornoGPP(retorno) != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "atualizarServicoAssinante", "MSISDN: " + servico.getMsisdn() + " - Retorno Tecnomen Prepaid Interface: " + retorno);
			
			return ConversorRetornoAprovisionamento.toRetornoGPP(retorno);
		}
		catch(Pe_exception e)
		{
			super.log(Definicoes.ERRO, "atualizarServicoAssinante", "MSISDN: " + servico.getMsisdn() + " - Excecao Tecnomen Prepaid Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoAprovisionamento.toRetornoGPP(e.error_code);
		}
	}
	
}
