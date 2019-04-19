package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.Collection;

import TINC.Pi_exception;
import TINC.TUPEngine_Factory;
import TINC.TUPEngine_FactoryHelper;
import TINC.TUPSyncEngine;

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
import com.brt.gpp.comum.conexoes.tecnomen.holder.MSISDNHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.SaldoAssinanteHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.ServicoTECAssinanteHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.UsuarioHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.VoiceMailHolder;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

/**
 *	Conexao com o Provision Server da Tecnomen. Este servico e responsavel pela ativacao e desativacao de assinantes.
 *
 *	@author		Daniel Ferreira
 *	@since		23/02/2007
 */
public class TecnomenProvisionServer extends ConexaoTecnomen
{

	/**
	 *	Fabrica de interfaces com o Provision Server.
	 */
	private TUPEngine_Factory fabrica;
	
	/**
	 *	Interface com o Provision Server.
	 */
	private TUPSyncEngine engine;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenProvisionServer()
	{
		super(ConexaoTecnomen.PROVISION);
		
		this.fabrica 		= null;
		this.engine			= null;
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if((autenticador == null) || (autenticador.getIor() == null))
			throw new GPPTecnomenException("Provision Server nao encontrado");

		org.omg.CORBA.Object objProvServer = super.getOrb().string_to_object(autenticador.getIor());
		TUPEngine_Factory fabrica = TUPEngine_FactoryHelper.narrow(objProvServer);
	
		try
		{
			this.engine = fabrica.getSyncEngine(autenticador.getChave(), autenticador.getIdUsuario());
		}
		catch(Pi_exception e)
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
				this.fabrica.releaseSyncEngine(autenticador.getChave(), this.engine.engineId());
			}
			catch(Pi_exception e)
			{
				super.log(Definicoes.WARN, "releaseEngine", "Conexao com Provision Server nao fechada corretamente: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			}
		}
		
		this.fabrica		= null;
		this.engine			= null;
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
	 *	Ativa um assinante na platforma Tecnomen.
	 *
	 *	@param		assinante				Informacoes do assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short ativarAssinante(Assinante assinante)
	{
		//Criando os holders de passagem de parametro para o metodo de ativacao do Provision Server.
		MSISDNHolder	msisdnHolder	= new MSISDNHolder(super.getOrb(), assinante.getMSISDN());
		AssinanteHolder	subHolder		= new AssinanteHolder(super.getOrb(), assinante);
		UsuarioHolder	userHolder		= new UsuarioHolder(super.getOrb(), assinante.getMSISDN());
		VoiceMailHolder	voiceMailHolder	= new VoiceMailHolder(super.getOrb(), (Object)null);
		
		//Atribuindo o MSISDN e o Login do holder de informacoes do assinante. Devido ao fato de a Tecnomen exigir que 
		//seja passado o Login, e utilizado este metodo.
		subHolder.setLogin(assinante.getMSISDN());
		subHolder.setMsisdn(assinante.getMSISDN());
		
		//Criando a colecao de holders para a passagem dos saldos do assinante.
		Collection saldos = SaldoAssinante.newSaldosAssinante(assinante);
		ColecaoEntidadeGPPHolder colSaldos = new ColecaoEntidadeGPPHolder(super.getOrb(), SaldoAssinanteHolder.class, saldos);

		//Criando a colecao de holders para a passagem dos servicos disponiveis. Os servicos disponiveis dependem da 
		//categoria de plano do assinante.
		Collection servicos = ServicoTECAssinante.newServicosTECAssinante(assinante);
		ColecaoEntidadeGPPHolder colServicos = new ColecaoEntidadeGPPHolder(super.getOrb(), ServicoTECAssinanteHolder.class, servicos);
		
		try
		{
			super.log(Definicoes.INFO, "ativarAssinante", assinante + " - Operacao: ProvisioningInterface.createSubscriber <Parametros> " + msisdnHolder);
			super.log(Definicoes.INFO, "ativarAssinante", assinante + " - Operacao: ProvisioningInterface.createSubscriber <Parametros> " + userHolder);
			super.log(Definicoes.INFO, "ativarAssinante", assinante + " - Operacao: ProvisioningInterface.createSubscriber <Parametros> " + subHolder);
			super.log(Definicoes.INFO, "ativarAssinante", assinante + " - Operacao: ProvisioningInterface.createSubscriber <Parametros> " + colSaldos);
			super.log(Definicoes.INFO, "ativarAssinante", assinante + " - Operacao: ProvisioningInterface.createSubscriber <Parametros> " + colServicos);
			super.log(Definicoes.INFO, "ativarAssinante", assinante + " - Operacao: ProvisioningInterface.createSubscriber <Parametros> " + voiceMailHolder);
			
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarProvisionServer("createSubscriber");
			//Ativando o assinante na plataforma Tecnomen.
			int retorno = this.engine.createSubscriber(msisdnHolder.toEntidadeTEC(), 
													   userHolder.toEntidadeTEC(),
													   subHolder.toEntidadeTEC(),
													   colSaldos.toColecaoEntidadeTEC(),
													   colServicos.toColecaoEntidadeTEC(),
													   voiceMailHolder.toEntidadeTEC());

			if(ConversorRetornoAprovisionamento.toRetornoGPP(retorno) != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "ativarAssinante", "MSISDN: " + assinante.getMSISDN() + " - Retorno Tecnomen Provisioning Interface: " + retorno);
			
			//No caso de retorno especifico de problemas de sincronizacao entre o engine sincrono e assincrono do 
			//Provision Server da Tecnomen, e necessario, antes de retornar, desativar o assinante. Isto porque este 
			//problema pode ativar o assinante "pela metade" (ou seja, usuario criado na CC_DB..INUser mas assinante 
			//nao criado na PP_DB..Subscriber). Apos a desativacao e retornado erro especifico.
			if(ConversorRetornoAprovisionamento.toRetornoGPP(retorno) == Definicoes.RET_ERRO_SINCRONIZACAO_TECNOMEN)
				this.desativarAssinante(assinante.getMSISDN());
				
			return ConversorRetornoAprovisionamento.toRetornoGPP(retorno);
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "ativarAssinante", assinante.toString() + " - Excecao Tecnomen Provisioning Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoAprovisionamento.toRetornoGPP(e.error_code);
		}
	}
	
	/**
	 *	Desativa um assinante da platforma Tecnomen.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short desativarAssinante(String msisdn)
	{
		//Criando o holder do MSISDN para passagem de parametro no metodo de desativacao do Provision Server.
		MSISDNHolder holder = new MSISDNHolder(super.getOrb(), msisdn);
		
		try
		{
			super.log(Definicoes.INFO, "desativarAssinante", "MSISDN: " + msisdn + " - Operacao: ProvisioningInterface.deleteSubscriber <Parametros> " + holder);
			//Incrementando o totalizador de operacoes da Tecnomen.
			TotalizadorAPITecnomen.getInstance().incrementarProvisionServer("deleteSubscriber");
			//Desativando o assinante na plataforma Tecnomen.
			int retorno = this.engine.deleteSubscriber(holder.toEntidadeTEC());
			
			if(ConversorRetornoAprovisionamento.toRetornoGPP(retorno) != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "desativarAssinante", "MSISDN: " + msisdn + " - Retorno Tecnomen Provisioning Interface: " + retorno);
			
			return ConversorRetornoAprovisionamento.toRetornoGPP(retorno);
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "desativarAssinante", "MSISDN: " + msisdn + " - Excecao Tecnomen Provisioning Interface: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			return ConversorRetornoAprovisionamento.toRetornoGPP(e.error_code);
		}
	}
	
}
