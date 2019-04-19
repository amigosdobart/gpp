//Definicao do Pacote
package com.brt.gpp.comum.conexoes.tecnomen;

// Arquivos de Imports da Tecnomen
import TINC.*;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorExcecaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.gppExceptions.*;

/**
 *	Esta classe realiza a implementacao e abstracao de conexao com a plataforma tecnomen para realizar as tarefas de 
 *	administracao de vouchers (Voucher Management). Nesta somente o trabalho de conexao e realizado, sendo que os 
 *	metodos de acoes como ativacao, criacao de ordem e realizado pela classe OperacoesVoucher. 
 *
 *	<P> Versao:	1.0
 *	@author		Joao Carlos
 *	@since		25/01/2005
 *
 *	<P> Versao:	2.0	Migracao para a Tecnomen 4.4.5
 *	@author		Daniel Ferreira
 *	@since		07/03/2007
 */

public class TecnomenVoucher extends ConexaoTecnomen
{

	/**
	 *	Fabrica de interfaces com o Voucher Management Server.
	 */
	private TVMFactory fabrica;
	
	/**
	 *	Interface com o Voucher Management Server.
	 */
	private TVMInterface tvmInterface;
	
	/**
	 *	Interface com o Voucher Job Manager.
	 */
	private TVMJobMgr tvmJobMgr;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenVoucher()
	{
		super(ConexaoTecnomen.VOUCHER);
		
		this.fabrica 		= null;
		this.tvmInterface	= null;
		this.tvmJobMgr		= null;
	}
    
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if((autenticador == null) || (autenticador.getIor() == null))
			throw new GPPTecnomenException("Voucher Management Server nao encontrado");
		
		org.omg.CORBA.Object objPPServer = super.getOrb().string_to_object(autenticador.getIor());
		TVMFactory fabrica = TVMFactoryHelper.narrow(objPPServer);

		try
		{
			this.tvmInterface	= fabrica.getTVMInterface(autenticador.getChave(), autenticador.getIdUsuario());
			this.tvmJobMgr		= fabrica.getJobMgr(autenticador.getChave());
		}
		catch(Pi_exception e)
		{
			super.log(Definicoes.DEBUG, "TecnomenVoucher.setEngine", "Excecao: " + e);
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
				this.fabrica.releaseTVMInterface(autenticador.getChave(), this.tvmInterface.engineId());
			}
			catch(Pi_exception e)
			{
				super.log(Definicoes.WARN, "fechar", "Conexao com Voucher Management Interface nao fechada corretamente: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
			}
		}
		
		this.fabrica		= null;
		this.tvmInterface	= null;
		this.tvmJobMgr		= null;
	}

	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#isAtivo()
	 */
	public boolean isAtivo()
	{
		if(this.tvmInterface == null)
			return false;
		
		try
		{
			this.tvmInterface.version();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Metodo....:getTvmInterface
	 * Descricao.:Retorna uma referencia para a TVMInterface
	 * @return Returns the tvmInterface.
	 */
	public TVMInterface getTvmInterface()
	{
		return this.tvmInterface;
	}
	
	/**
	 * Metodo....:getTvmJobMgr
	 * Descricao.:Retorna uma referencia para a Job Management
	 * @return Returns the tvmJobMgr.
	 */
	public TVMJobMgr getTvmJobMgr()
	{
		return this.tvmJobMgr;
	}
	
}