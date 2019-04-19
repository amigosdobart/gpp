package com.brt.gpp.aplicacoes.campanha.retornoFaleGratis;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe responsavel pela condicao em que o assinante devera receber
 * um SMS de aviso apos ter realizado uma recarga e retornado a Promocao FGN
 * 
 * @author João Paulo Galvagni
 * @since  25/09/2007
 */
public class CondRetornoFaleGratisImpl implements CondicaoConcessao
{
	private RetornoFaleGratisDAO 	 retornoFaleGratisDAO	= new RetornoFaleGratisDAO();
	private GerentePoolLog 			 log;
	
	/**
	 * Construtor da Classe
	 * 
	 * @param campanha - Instancia de <code>Campanha</code>
	 */
	public CondRetornoFaleGratisImpl(Campanha campanha)
	{
		log = GerentePoolLog.getInstancia(this.getClass());
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#deveSerBonificado(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha)
	 */
	public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		boolean deveBonificar 	= false;
		ResultSet rs	  		= null;
		
		try
		{
			retornoFaleGratisDAO = new RetornoFaleGratisDAO();
			
			rs = retornoFaleGratisDAO.statusAssinanteAtivo(assinante, conexaoPrep);
			
			if (rs.next())
				deveBonificar = true;
		}
		catch(Exception e)
		{
			log.log(0, Definicoes.ERRO, "CondRetornoFaleGratisImpl", "deveSerBonificado",
				    "Erro ao verificar se o assinante " + assinante.getMsisdn() + " deve receber um SMS de alerta por voltar ao FGN II. Erro:"+e);
		}
		
		return deveBonificar;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getNomeCondicao()
	 */
	public String getNomeCondicao()
	{
		return null;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederBonus()
	 */
	public double getValorConcederBonus()
	{
		// Retorna 0, pois nao havera bonificacao
		return 0.0;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederSM()
	 */
	public double getValorConcederSM()
	{
		// Retorna 0, pois nao havera bonificacao
		return 0.0;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederDados()
	 */
	public double getValorConcederDados()
	{
		// Retorna 0, pois nao havera bonificacao
		return 0.0;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getDataSatisfacaoCondicao()
	 */
	public Date getDataSatisfacaoCondicao()
	{
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#executarPosBonificacao(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha, com.brt.gpp.comum.conexoes.bancoDados.PREPConexao)
	 */
	public void executarPosBonificacao(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
	}
}