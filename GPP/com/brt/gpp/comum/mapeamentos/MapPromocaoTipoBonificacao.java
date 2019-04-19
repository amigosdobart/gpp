package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Mapeamento de entidades PromocaoTipoBonificacao em memoria. 
 *
 *	@version	1.0		28/09/2005		Primeira versao.
 *	@author		Daniel Ferreira
 */
public final class MapPromocaoTipoBonificacao extends Mapeamento
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocaoTipoBonificacao instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoTipoBonificacao() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapPromocaoTipoBonificacao getInstance() 
	{
		try
		{
			if(MapPromocaoTipoBonificacao.instance == null) 
			    MapPromocaoTipoBonificacao.instance = new MapPromocaoTipoBonificacao();
		}
		catch(Exception e)
		{
			return null;
		}
		
		return MapPromocaoTipoBonificacao.instance;
	}	

	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load()
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);

			String sqlQuery = 
				"SELECT tptb_id_tipo_bonificacao, " +
				"       tptb_des_tipo_bonificacao " +
				"  FROM tbl_pro_tipo_bonificacao ";
				
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(registros.next())
			{
				PromocaoTipoBonificacao tipoBonificacao = new PromocaoTipoBonificacao();

				short idTipoBonificacao = registros.getShort("tptb_id_tipo_bonificacao");
				tipoBonificacao.setIdTipoBonificacao(idTipoBonificacao);
				
				tipoBonificacao.setNomTipoBonificacao(registros.getString("tptb_des_tipo_bonificacao"));

				super.values.put(new Short(idTipoBonificacao), tipoBonificacao);
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
	 *	Retorna objeto PromocaoTipoBonificacao obtido a partir da chave.
	 *
	 *	@param		idTipoBonificacao		Identificador do tipo de bonificacao.
	 *	@return		Objeto PromocaoTipoBonificacao obtido a partir da chave.
	 */
	public PromocaoTipoBonificacao getPromocaoTipoBonificacao(short idTipoBonificacao)
	{
		return (PromocaoTipoBonificacao)super.values.get(new Short(idTipoBonificacao));
	}

}
