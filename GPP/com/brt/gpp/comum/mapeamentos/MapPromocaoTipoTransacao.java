package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoTransacao;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPromocao; 
import com.brt.gpp.comum.mapeamentos.MapPromocaoTipoBonificacao; 
import com.brt.gpp.comum.mapeamentos.MapRecOrigem; 
import com.brt.gpp.comum.mapeamentos.chave.ChaveHashMap;

/**
 *	Mapeamento de entidades PromocaoTipoTransacao em memoria. 
 *
 *	@version	1.0		28/09/2005		Primeira versao.
 *	@author		Daniel Ferreira
 *
 *	@version	2.0		14/03/2008		Adaptacao para multiplas bonificacoes.
 *	@author		Daniel Ferreira
 */
public final class MapPromocaoTipoTransacao extends Mapeamento
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocaoTipoTransacao instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoTipoTransacao() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapPromocaoTipoTransacao getInstance() 
	{
		try
		{
			if(MapPromocaoTipoTransacao.instance == null) 
			    MapPromocaoTipoTransacao.instance = new MapPromocaoTipoTransacao();
		}
		catch(Exception e)
		{
			return null;
		}
		
		return MapPromocaoTipoTransacao.instance;
	}	

	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load()
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao					conexaoPrep			= null;
		MapPromocao					mapPromocao			= MapPromocao.getInstancia();
		MapPromocaoTipoBonificacao	mapTipoBonificacao	= MapPromocaoTipoBonificacao.getInstance();
		MapRecOrigem				mapOrigemRecarga	= MapRecOrigem.getInstance();
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);

			String sqlQuery = 
				"SELECT idt_promocao, " +
				"       tip_execucao, " +
				"       id_tipo_bonificacao, " +
				"       tip_transacao, " +
				"       num_ordem, " +
				"       ind_zerar_saldo_periodico, " +
				"       ind_zerar_saldo_bonus, " +
				"       ind_zerar_saldo_torpedo, " +
				"       ind_zerar_saldo_dados " +
				"  FROM tbl_pro_tipo_transacao ";
				
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(registros.next())
			{
				PromocaoTipoTransacao tipoTransacao = new PromocaoTipoTransacao();

				Promocao promocao = mapPromocao.getPromocao(registros.getInt("idt_promocao"));
				tipoTransacao.setPromocao(promocao);
				
				String tipExecucao = registros.getString("tip_execucao");
				tipoTransacao.setTipExecucao(tipExecucao);
				
				PromocaoTipoBonificacao tipoBonificacao = mapTipoBonificacao.getPromocaoTipoBonificacao(registros.getShort("id_tipo_bonificacao"));
				tipoTransacao.setTipoBonificacao(tipoBonificacao);
				
				tipoTransacao.setOrigem(mapOrigemRecarga.getOrigemRecarga(registros.getString("tip_transacao")));
				tipoTransacao.setNumOrdem(registros.getShort("num_ordem"));
				tipoTransacao.setIndZerarSaldoPeriodico(registros.getShort("ind_zerar_saldo_periodico") != 0);
				tipoTransacao.setIndZerarSaldoBonus(registros.getShort("ind_zerar_saldo_bonus") != 0);
				tipoTransacao.setIndZerarSaldoTorpedos(registros.getShort("ind_zerar_saldo_torpedo") != 0);
				tipoTransacao.setIndZerarSaldoDados(registros.getShort("ind_zerar_saldo_dados") != 0);

				ChaveHashMap chave = new ChaveHashMap(new Object[]{promocao, tipExecucao, tipoBonificacao});
				super.values.put(chave, tipoTransacao);
			}
			
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
	 *	Retorna objeto PromocaoTipoTransacao a partir da chave.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		tipExecucao				Tipo de execucao da concessao.
	 *	@param		tipoBonificacao			Informacoes do tipo de bonificacao.
	 *	@return		Objeto PromocaoTipoTransacao obtido a partir da chave.
	 */
	public PromocaoTipoTransacao getPromocaoTipoTransacao(Promocao promocao, String tipExecucao, PromocaoTipoBonificacao tipoBonificacao)
	{
		ChaveHashMap chave = new ChaveHashMap(new Object[]{promocao, tipExecucao, tipoBonificacao});
		
		return (PromocaoTipoTransacao)super.values.get(chave);
	}

	/**
	 *	Retorna um container de objetos PromocaoTipoTransacao associados a promocao informada.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@return		Container de objetos PromocaoTipoTransacao associados a promocao.
	 */
	public Collection getPromocaoTiposTransacao(Promocao promocao)
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
		{
			PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)iterator.next();
			
			if(tipoTransacao.getPromocao().equals(promocao))
				result.add(tipoTransacao);
		}
		
		return result;
	}
	
}
