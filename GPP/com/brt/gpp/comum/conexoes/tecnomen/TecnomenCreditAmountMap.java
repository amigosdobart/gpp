package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.ArrayList;
import java.util.Collection;

import TINC.AdminEngine_Factory;
import TINC.AdminEngine_FactoryHelper;
import TINC.CreditAmountMap;
import TINC.Pi_exception;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorExcecaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DadosConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.holder.ColecaoEntidadeGPPHolder;
import com.brt.gpp.comum.conexoes.tecnomen.holder.ValorVoucherHolder;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;
import com.brt.gpp.gerentesPool.GerenteAutenticadorTecnomen;

/**
 *	Conexao com o Admin Server da Tecnomen, utilizando o engine CreditAmountMap. 
 *	Este servico e responsavel pelas operacoes de valores de voucher na plataforma. 
 *
 *	@author		Bernardo Vergne Dias
 *	@since 		02/07/2007
 */
public class TecnomenCreditAmountMap extends ConexaoTecnomen
{
	
	/**
	 *	Fabrica de interfaces com o Admin Server.
	 */
	private AdminEngine_Factory fabrica;
	
	/**
	 *	Interface de System Config.
	 */
	private CreditAmountMap engine;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenCreditAmountMap()
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
			this.engine = fabrica.getCreditAmountMap(autenticador.getChave(), autenticador.getIdUsuario());
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
				this.fabrica.releaseCreditAmountMap(autenticador.getChave(), this.engine.engineId());
			}
			catch(Pi_exception e)
			{
				super.log(Definicoes.WARN, "fechar", "Conexao com Credit Amount Map do Admin Server " +
						"nao fechada corretamente: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
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
	 *	Consulta uma coleção de <code>ValorVoucher</code> para o tipo de saldo especificado
	 *  na entidade valorVoucher passada como argumento do método.
	 *  
	 *	@param		valorVoucher		Instancia de <code>ValorVoucher</code>.
	 *	@return		Lista de valores de voucher disponibilizados na plataforma Tecnomen.
	 *	@throws		GPPTecnomenException 
	 */
	public Collection getValoresVoucher(ValorVoucher valorVoucher) throws GPPTecnomenException
	{
		ArrayList result = new ArrayList();
		
		try
		{
			int idServico = ((DadosConexaoTecnomen)this.getDadosConexao()).getIdServico();
			Autenticador autenticador = GerenteAutenticadorTecnomen.getInstance().getAutenticador(idServico);
					
			// Cria o Holder para valorVoucher
			
			ValorVoucherHolder valorVoucherHolder = new ValorVoucherHolder(this.getOrb(), valorVoucher);
			valorVoucherHolder.setOperador(autenticador.getIdUsuario());
			
			// Cria a coleção de valorVoucher. Esse holder irá interpretar 
			// o retorno de getCreditAmountMap (coleção do tipo recordSeq)
			
			ColecaoEntidadeGPPHolder valoresVoucherHolder = new ColecaoEntidadeGPPHolder(
					super.getOrb(), ValorVoucherHolder.class);
			
			super.log(Definicoes.INFO, "getValoresVoucher", valorVoucher + " - Operacao: AdminInterface.getCreditAmountMap <Parametros> " + valorVoucherHolder);
			//Executando a consulta getCreditAmountMap no Admin Server (interface SystemConfig).
			short retorno = this.engine.getCreditAmountMap(
					valorVoucherHolder.toEntidadeTEC(), valoresVoucherHolder.toColecaoEntidadeTEC());
			
			switch(retorno)
			{
				case Definicoes.RET_OPERACAO_OK:
					result.addAll(valoresVoucherHolder.toColecaoEntidadeGPP());
					break;
				default:
					throw ConversorExcecaoTecnomen.newGPPTecnomenException(retorno); 
			}
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "getValoresVoucher", valorVoucher.toString() + " - Excecao Tecnomen Admin Interface: " + e);
			throw ConversorExcecaoTecnomen.toGPPTecnomenException(e);
		}

		return result;
	}
	
}