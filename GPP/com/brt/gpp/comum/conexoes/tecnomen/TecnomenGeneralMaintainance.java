package com.brt.gpp.comum.conexoes.tecnomen;

import TINC.AdminEngine_Factory;
import TINC.AdminEngine_FactoryHelper;
import TINC.GeneralMaintenance;
import TINC.Pi_exception;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorExcecaoTecnomen;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

/**
 *	Conexao com o Admin Server da Tecnomen, utilizando o engine General Maintainance. Este servico e responsavel pelas 
 *	operacoes de administrador na plataforma. 
 *
 *	@author		Daniel Ferreira
 *	@since 		07/03/2007
 */
public class TecnomenGeneralMaintainance extends ConexaoTecnomen
{
	
	/**
	 *	Fabrica de interfaces com o Admin Server.
	 */
	private AdminEngine_Factory fabrica;
	
	/**
	 *	Interface de General Maintainance.
	 */
	private GeneralMaintenance engine;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenGeneralMaintainance()
	{
		super(ConexaoTecnomen.ADMIN);
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if((autenticador == null) || (autenticador.getIor() == null))
			throw new GPPTecnomenException("Admin Server nao encontrado");
		
		org.omg.CORBA.Object objPPServer = super.getOrb().string_to_object(autenticador.getIor());
		AdminEngine_Factory fabrica = AdminEngine_FactoryHelper.narrow(objPPServer);

		try
		{
			this.engine = fabrica.getGeneralMaintenance(autenticador.getChave(), autenticador.getIdUsuario());
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.DEBUG, "TecnomenGeneralMaintainance.setEngine", "Excecao: " + e);
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
				this.fabrica.releaseGeneralMaintenance(autenticador.getChave(), this.engine.engineId());
			}
			catch(Pi_exception e)
			{
				super.log(Definicoes.WARN, "TecnomenGeneralMaintainance.fechar", "Conexao com General Maintainance do Admin Server nao fechada corretamente: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
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
	
}