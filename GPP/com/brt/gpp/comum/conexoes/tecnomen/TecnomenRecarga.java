package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.Date;

import TINC.Pi_exception;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DirectFundTransfer;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.OnlineFundTransfer;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Este arquivo inclui a definicao da classe de conexao com a plataforma de pre-pago Tecnomen para as funcinalidades 
 * de Recarga. Contem a definicao dos metodos de conexao fornecidos pela IDL da Tecnomen. 
 *
 *	@version		1.0		25/02/2004		Primeira versao.
 *	@author			Daniel Cintra Abib
 *                       
 *	@version		2.0		14/10/2004		Implementação do Múltiplo Saldo.
 *	@author			Alberto Magno
 * 
 *	@version		3.0		02/05/2007		Padronizacao das conexoes com a Tecnomen e implementacao do Controle Total.
 *	@author			Daniel Ferreira
 */
public class TecnomenRecarga extends ConexaoTecnomen
{
	/**
	 * Referencia a classe que implementa a engine de recarga 
	 */
	private TecnomenEngineRecarga	engine;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenRecarga()
	{
		super(ConexaoTecnomen.RECARGA);
		this.engine = TecnomenRecargaFactory.getInstance().newTecnomenRecarga();
	}
    
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if (!this.engine.isAtivo())
			this.engine.conectar();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#releaseEngine()
	 */
	public void releaseEngine(Autenticador autenticador)
	{
		this.engine.fechar();
	}

	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#isAtivo()
	 */
	public boolean isAtivo()
	{
		return this.engine.isAtivo();
	}

	/**
	 *	Realiza recarga nos multiplos saldos dos assinantes.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Objeto contendo valores e numero de dias de expiracao da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short executarRecarga(Assinante assinante, ValoresRecarga valores)
	{
		short retorno;
		retorno = this.engine.executarRecarga(assinante, new OnlineFundTransfer(assinante.getMSISDN(), valores));
		//Se o assinante nao possuir status de Franquia, atualizar a data de expiracao deste saldo.
		if(!MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco()).possuiStatusPeriodico())
		{
			//A expiracao do Status periodico caminha com a do prinicipal
			Date dataExpPeriodico = assinante.newDataExpiracao(TipoSaldo.PRINCIPAL, valores.getNumDiasExpPrincipal());
			
			DirectFundTransfer dft = new DirectFundTransfer();
			dft.setMsisdn(assinante.getMSISDN());
			dft.setDataExpPeriodico(dataExpPeriodico);
			dft.setValorPeriodico(valores.getSaldoPeriodico());
			
			this.executarRecargaFranquia(assinante,dft);
		}
		return retorno;
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
		return this.engine.executarRecarga(assinante, valores);
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
		return this.engine.executarRecargaFranquia(assinante, valores);
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
		return this.engine.executarRecargaFranquia(assinante, valores);
	}
	
	/**
	 *	Realiza ajuste nos multiplos saldos dos assinantes.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Objeto contendo valores e datas de expiracao para o ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short executarAjuste(Assinante assinante, ValoresRecarga valores)
	{
		return this.engine.executarAjuste(assinante, new DirectFundTransfer(assinante.getMSISDN(), valores));
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
		return this.engine.executarAjuste(assinante, valores);
	}
	
	/**
	 * Metodo....:consultarAssinante
	 * Descricao.:Retorna o objeto Assinante a partir de consulta utilizando a PaymentInterface
	 * @param numeroAssinante	- Numero do assinante MSISDN
	 * @return Assinante		- Objeto Assinante com dados retornados da plataforma Tecnomen
	 * @throws Pi_exception
	 * @throws GPPTecnomenException
	 */
	public Assinante consultarAssinante(String numeroAssinante) throws GPPTecnomenException
	{
		return this.engine.consultarAssinante(numeroAssinante);
	}
	
	/**
	 * Metodo....:atualizarStatusAssinante
	 * @param msisdn - Numero do MSISDN do assinante 
	 * @param statusAssinante	- status do assinante
	 * @param expPrincipal		- Data de expiração do Saldo Principal
	 * @param statusPeriodico	- status periódico do assinante
	 * @param expPeriodico		- Data de expiração do Saldo Periódico
	 * @param expBonus			- Data de expiração do Saldo de Bônus
	 * @param expSMS			- Data de expiração do Saldo de Torpedos
	 * @param expDados			- Data de expiração do Saldo de Dados
	 * @return short			- indica se atualização foi bem sucedida ou não
	 * @throws Pi_exception
	 * @throws GPPInternalErrorException
	 */	
	public short atualizarStatusAssinante (String msisdn, short statusAssinante, Date expPrincipal, short statusPeriodico,
			Date expPeriodico, Date expBonus, Date expSMS, Date expDados)
	{
		return this.engine.atualizarStatusAssinante(msisdn, statusAssinante, expPrincipal, 
				statusPeriodico,expPeriodico, expBonus, expSMS, expDados);
	}	
	
}