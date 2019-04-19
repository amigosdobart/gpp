package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Imports GPP.

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoPlanoPreco;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
  *	Este arquivo contem a classe que faz o mapeamento dos planos de preco relacionados a cada uma das promocoes 
  *	em memoria no GPP, ou seja, para cada promocao, quais os planos de preco podem ter a promocao.
  *
  *	@author		Daniel Ferreira
  *	@since 		15/08/2005
  */
public class MapPromocaoPlanoPreco extends Mapeamento
{

	private static MapPromocaoPlanoPreco instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoPlanoPreco() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Cria uma instancia ou refere-se a instancia ja existente.
	 *
	 *	@return		MapPromocaoPlanoPreco				instance					Mapeamento da TBL_PRO_PLANO_PRECO.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoPlanoPreco getInstancia() throws GPPInternalErrorException 
	{
		if(MapPromocaoPlanoPreco.instance == null) 
		{
		    MapPromocaoPlanoPreco.instance = new MapPromocaoPlanoPreco();
		}
		
		return MapPromocaoPlanoPreco.instance;
	}	

	/**
	 *	Carrega o mapeamento em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		try
		{
			//Seleciona conexao do pool Prep Conexao
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			//Seleciona planos de precos validos no BD do GPP
			String sql = "SELECT " +
						 "  IDT_PROMOCAO, " +
						 "  IDT_PLANO_PRECO " +
			             "FROM TBL_PRO_PLANO_PRECO ";

			ResultSet resultPlano = conexaoPrep.executaPreparedQuery(sql, null,0);
			
			while (resultPlano.next())
			{
				PromocaoPlanoPreco plano = new PromocaoPlanoPreco();
				
				Integer idtPromocao = new Integer(resultPlano.getInt("IDT_PROMOCAO"));
				plano.setIdtPromocao(idtPromocao);
				
				Integer idtPlanoPreco = new Integer(resultPlano.getInt("IDT_PLANO_PRECO"));
				plano.setIdtPlanoPreco(idtPlanoPreco);
				
				// Insere o valor no objeto Hash
				Map mapPlanoPreco = (Map)super.values.get(idtPromocao);
				if(mapPlanoPreco == null)
				{
				    mapPlanoPreco = Collections.synchronizedMap(new HashMap());
					super.values.put(idtPromocao, mapPlanoPreco);
				}
				mapPlanoPreco.put(idtPlanoPreco, plano);
			}
			
			resultPlano.close();
			resultPlano = null;
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
	 *	Este metodo retorna um objeto contendo o objeto PromocaoPlanoPreco para um dado valor de chave (id).
	 *
	 *	@param		int						idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		int						idtPlanoPreco				Chave a ser utilizada para localizar o Plano de Preco.
	 *	@return		PromocaoPlanoPreco									Objeto PromocaoPlanoPreco correspondente.
	 */
	public PromocaoPlanoPreco getPromocaoPlanoPreco(int idtPromocao, int idtPlanoPreco)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		Map mapPlanoPreco = (Map)super.values.get(new Integer(idtPromocao));
		
		if(mapPlanoPreco != null)
		{
			PromocaoPlanoPreco result = (PromocaoPlanoPreco)mapPlanoPreco.get(new Integer(idtPlanoPreco));
			if(result != null)
			{
				return (PromocaoPlanoPreco)result.clone();
			}
		}
		
		return null;
	}

	/**
	 *	Este metodo retorna um objeto contendo o objeto PromocaoPlanoPreco para um dado valor de chave (id).
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		Integer					idtPlanoPreco				Chave a ser utilizada para localizar o Plano de Preco.
	 *	@return		PromocaoPlanoPreco									Objeto PromocaoPlanoPreco correspondente.
	 */
	public PromocaoPlanoPreco getPromocaoPlanoPreco(Integer idtPromocao, Integer idtPlanoPreco)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		if((idtPromocao != null) && (idtPlanoPreco != null))
		{
			Map mapPlanoPreco = (Map)super.values.get(idtPromocao);
		
			if(mapPlanoPreco != null)
			{
				PromocaoPlanoPreco result = (PromocaoPlanoPreco)mapPlanoPreco.get(idtPlanoPreco);
				if(result != null)
				{
					return (PromocaoPlanoPreco)result.clone();
				}
			}
		}
		
		return null;
	}

}
