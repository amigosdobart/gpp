package com.brt.gpp.aplicacoes.recarregar;

import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel por fazer a modificação das datas de expiração.
 *
 *	@version		1.0		12/07/2007		Primeira versao.
 *	@author			Geraldo Palmeira
 */
public class InsercaoDatasExpiracao
{
	
	// Variaveis Membro
	// Gerente de LOG
	protected GerentePoolLog log = null; 
	protected long					logId			= 0;
	
	/**
	 * Metodo...: InsercaoDatasExpiracao
	 * Descricao: Construtor 
	 * @param	long	logId (Identificador do Processo para Log)
	 */
	public InsercaoDatasExpiracao()
	{
		this.log = GerentePoolLog.getInstancia(this.getClass());
	}	

	/**
	  * Metodo...: log
	  * Descricao: Chama o metodo log da classe GerentePoolLog
	  * @param	aTipo 		- Tipo da severidade do log	  
	  * @param	aMetodo 	- Metodo que chamou o log	  
	  * @param	aMensagem 	- Mensagem que deve ser escrita no log	
	  * @return
	  * @throws  
	  */
	public void log (int aTipo, String aMetodo, String aMensagem)
	{
		log.log(this.logId, aTipo, "InsercaoDatasExpiracao", aMetodo, aMensagem);
	}
	
	/**
	 *	Executa alteração das datas de expiração.
	 *
	 *	@param		valores					ValoresRecarga.
	 *  @param		msisdn					MSISDN do assinante.
	 *	@return		ValoresRecarga valores.
	 */	
	public ParametrosRecarga modificaDatasExpiracao(ParametrosRecarga ajuste, String tipoTransacao)
	{
		ValoresRecarga valores = ajuste.getValores();
		// Caso o tipo de transacao permita a alteracao das datas de expiracao, e necessario ajusta-las em 
		//funcao da data de expiracao do Saldo Principal, com excecao da do Saldo Periodico, que e 
		//independente.
		try
		{
			OrigemRecarga origem = MapRecOrigem.getInstance().getOrigemRecarga(tipoTransacao);
			// Verifica a necessidade de modificar as datas de expiração
			if((origem != null) && (origem.modificarDataExp()))
			{
				ajuste.setValores(verificaTipoSaldos(valores, ajuste.getAssinante()));
				
				// Valida se o assinante esta com status Recharge Expired ou Disconnected para
				// entao retornar ao status Normal
				if (ajuste.getAssinante().getStatusAssinante() == Definicoes.STATUS_RECHARGE_EXPIRED || 
					ajuste.getAssinante().getStatusAssinante() == Definicoes.STATUS_DISCONNECTED)
					ajuste.setRetornoCicloDois(true);
			}
			else
			{
				valores.setDataExpPrincipal(null);
				valores.setDataExpPeriodico(null);
				valores.setDataExpBonus    (null);
				valores.setDataExpSMS      (null);
				valores.setDataExpGPRS     (null);
			}
		}
		catch(Exception e)
		{
			this.log(Definicoes.ERRO , "InsercaoDatasExpiracao.modificaDatasExpiracao", "MSISDN: " + ajuste.getAssinante().getMSISDN() + " - Excecao: " + e);
		}
		return ajuste;
	}
	
	/**
	 *	Verifica quais as datas de expiração pertencem a Categoria do 
	 *  Assinante e modifica datas de expiração dos saldos
	 *
	 *	@param		valores					ValoresRecarga.
	 *  @param		msisdn					MSISDN do assinante.
	 *	@return		ValoresRecarga valores.
	 */	
	public ValoresRecarga verificaTipoSaldos(ValoresRecarga valores,Assinante assinante)
	{
		try
		{
			// Cria o objeto PlanoPreco e verifica quais tipos de saldos pertencem a categoria do assinante
			MapPlanoPreco map = MapPlanoPreco.getInstancia();
			PlanoPreco plano = map.getPlanoPreco(assinante.getPlanoPreco());
			Collection tipoSaldo = plano.getCategoria().getTiposSaldo();
			// Verifica se a data de expiracao do saldo principal eh maior que a data de expiracao 
			// do valor do ajuste
			// Caso nao seja, utiliza a data de expiração do principal
			if((assinante.getDataExpPrincipal()!= null) &&
			   (valores.getDataExpPrincipal()  != null) &&
			   (assinante.getDataExpPrincipal().after(valores.getDataExpPrincipal()) ) )
				valores.setDataExpPrincipal(assinante.getDataExpPrincipal());
			
			if((assinante.getDataExpPeriodico() != null) &&
			   (valores.getDataExpPeriodico()   != null) &&
			   (assinante.getDataExpPeriodico().after(valores.getDataExpPeriodico()) ) )
				valores.setDataExpPeriodico(assinante.getDataExpPeriodico());
			
			// Para cada tipo de saldo encontrado, associa a data de expiração e o crédito disponível
			// Todas as datas de expiracao sao equacionadas a data do saldo principal
			for(Iterator iterator = tipoSaldo.iterator(); iterator.hasNext();)
			{
				TipoSaldo saldo = ((SaldoCategoria)iterator.next()).getTipoSaldo();
				
				// Verifica quais datas devem ser alteradas
				if(saldo.getIdtTipoSaldo() == TipoSaldo.PERIODICO)
				{
					if(plano.getCategoria().possuiSaldo(saldo))
						valores.setDataExpPeriodico(valores.getDataExpPeriodico());
					else
					{
						valores.setDataExpPeriodico(null);
						valores.setValorBonusPeriodico(0.0d);
					}	
				}
				
				if(saldo.getIdtTipoSaldo() == TipoSaldo.BONUS)
				{
					if(plano.getCategoria().possuiSaldo(saldo))
						valores.setDataExpBonus( valores.getDataExpPrincipal());
					else
					{
						valores.setDataExpBonus(null);
						valores.setValorBonusBonus(0.0d);
					}	
				}
				
				if(saldo.getIdtTipoSaldo() == TipoSaldo.DADOS)
				{
					if(plano.getCategoria().possuiSaldo(saldo))
						valores.setDataExpGPRS ( valores.getDataExpPrincipal());
					else
					{
						valores.setDataExpGPRS(null);
						valores.setValorBonusGPRS(0.0d);
					}
				}
				
				if(saldo.getIdtTipoSaldo() == TipoSaldo.TORPEDOS )
				{
					if(plano.getCategoria().possuiSaldo(saldo))
						valores.setDataExpSMS  ( valores.getDataExpPrincipal());
					else
					{
						valores.setDataExpSMS  ( null);
						valores.setValorBonusSMS(0.0d);
					}
				}
			}
		}
		catch(Exception e)
		{
			this.log(Definicoes.ERRO , "InsercaoDatasExpiracao.verificaTipoSaldos", "MSISDN: " + assinante.getMSISDN() + " - Excecao: " + e);
		}
		return valores;
	}
}