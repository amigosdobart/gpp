package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator; 
import java.util.TreeMap;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimite;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoTipoBonificacao;
import com.brt.gpp.comum.mapeamentos.chave.ChaveTreeMap;
import com.brt.gpp.comum.mapeamentos.chave.ChaveVigencia;

/**
 *	Mapeamento de registros da tabela TBL_PRO_LIMITE_PROMOCAO em memoria.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date 		01/06/2005
 *	@modify		Primeira versao.
 *
 *	@version	1.1
 *	@author		Daniel Ferreira
 *	@date 		18/03/2008
 *	@modify		Inclusao de multiplas bonificacoes.
 */
public final class MapPromocaoLimite extends Mapeamento
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocaoLimite instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoLimite() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapPromocaoLimite getInstance() 
	{
		try
		{
			if(MapPromocaoLimite.instance == null) 
				MapPromocaoLimite.instance = new MapPromocaoLimite();

			return MapPromocaoLimite.instance;
		}
		catch(Exception e)
		{
			return null;
		}
	}	

	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load()
	 */
	protected void load() throws GPPInternalErrorException 
	{
		TreeMap						values				= new TreeMap();
		PREPConexao 				conexaoPrep 		= null;
		MapPromocao					mapPromocao			= MapPromocao.getInstancia();
		MapPromocaoTipoBonificacao	mapTipoBonificacao	= MapPromocaoTipoBonificacao.getInstance();
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery = 
			    "SELECT idt_promocao, " +
			    "       id_tipo_bonificacao, " +
			    "       dat_ini_periodo, " +
			    "       dat_fim_periodo, " +
			    "       vlr_max_credito_bonus, " +
			    "       ind_permite_isencao_limite " +
			    "  FROM tbl_pro_limite_promocao ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(registros.next())
			{
				PromocaoLimite limite = new PromocaoLimite();
				
				Promocao promocao = mapPromocao.getPromocao(registros.getInt("idt_promocao"));
				limite.setPromocao(promocao);
				
				PromocaoTipoBonificacao tipoBonificacao = mapTipoBonificacao.getPromocaoTipoBonificacao(registros.getShort("id_tipo_bonificacao"));
				limite.setTipoBonificacao(tipoBonificacao);
				
				Date datIniPeriodo = registros.getDate("dat_ini_periodo");
				limite.setDatIniPeriodo(datIniPeriodo);
				
				Date datFimPeriodo = registros.getDate("dat_fim_periodo");
				limite.setDatFimPeriodo(datFimPeriodo);
				
				limite.setVlrMaxCreditoBonus(registros.getDouble("vlr_max_credito_bonus"));
				limite.setIndPermiteIsencaoLimite(registros.getInt("ind_permite_isencao_limite") != 0);
				
				ChaveVigencia vigencia = new ChaveVigencia(new Object[]{datIniPeriodo, datFimPeriodo});
				ChaveTreeMap chave = new ChaveTreeMap(new Object[]{promocao, tipoBonificacao, vigencia});
				values.put(chave, limite);
			}
			
			super.values = Collections.synchronizedMap(values);
			registros.close();
			registros = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Retorna a lista com o historico dos valores do limite da promocao.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@return		Objeto PromocaoLimite correspondente.
	 */
	public Collection getPromocaoLimites(Promocao promocao)
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
		{
			PromocaoLimite limite = (PromocaoLimite)iterator.next();
			
			if(limite.getPromocao().equals(promocao))
				result.add(limite);
		}
		
	    return result;
	}

	/**
	 *	Retorna a lista de limites da promocao, um para cada tipo de bonificacao.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return		Objeto PromocaoLimite correspondente.
	 */
	public Collection getPromocaoLimites(Promocao promocao, Date datProcessamento)
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
		{
			PromocaoLimite limite = (PromocaoLimite)iterator.next();
			
			if((limite.getPromocao().equals(promocao)) && (limite.isVigente(datProcessamento)))
				result.add(limite);
		}
		
	    return result;
	}

}
