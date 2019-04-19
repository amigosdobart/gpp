package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Imports GPP.

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCodigoNacional;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
  *	Este arquivo contem a classe que faz o mapeamento dos codigos nacionais relacionados a cada uma das promocoes 
  *	em memoria no GPP, ou seja, para cada promocao, quais os codigos nacionais podem ter a promocao.
  *
  *	@author		Daniel Ferreira
  *	@since 		15/08/2005
  */
public final class MapPromocaoCodigoNacional extends Mapeamento
{

	private static MapPromocaoCodigoNacional instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoCodigoNacional() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Cria uma instancia ou refere-se a instancia ja existente de MapPromocao.
	 *
	 *	@return		MapPromocao					instance					Mapeamento da TBL_PRO_CODIGO_NACIONAL.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoCodigoNacional getInstancia() throws GPPInternalErrorException 
	{
		if(MapPromocaoCodigoNacional.instance == null) 
		{
		    MapPromocaoCodigoNacional.instance = new MapPromocaoCodigoNacional();
		}
		
		return MapPromocaoCodigoNacional.instance;
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
						 "  IDT_CODIGO_NACIONAL " +
			             "FROM TBL_PRO_CODIGO_NACIONAL ";

			ResultSet resultCodigo = conexaoPrep.executaPreparedQuery(sql, null,0);
			
			while (resultCodigo.next())
			{
				PromocaoCodigoNacional codigoNacional = new PromocaoCodigoNacional();
				
				Integer idtPromocao = new Integer(resultCodigo.getInt("IDT_PROMOCAO"));
				codigoNacional.setIdtPromocao(idtPromocao);
				
				Integer idtCodigoNacional = new Integer(resultCodigo.getInt("IDT_CODIGO_NACIONAL"));
				codigoNacional.setIdtCodigoNacional(idtCodigoNacional);
				
				Map mapCodigoNacional = (Map)super.values.get(idtPromocao);
				if(mapCodigoNacional == null)
				{
				    mapCodigoNacional = Collections.synchronizedMap(new HashMap());
					super.values.put(idtPromocao, mapCodigoNacional);
				}
				mapCodigoNacional.put(idtCodigoNacional, codigoNacional);
			}
			
			resultCodigo.close();
			resultCodigo = null;
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
	 *	Este metodo retorna um objeto contendo o objeto PromocaoCodigoNacional para um dado valor de chave (id).
	 *
	 *	@param		int						idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		int						idtCodigoNacional			Chave a ser utilizada para localizar o Codigo Nacional.
	 *	@return		PromocaoCodigoNacional								Objeto PromocaoCodigoNacional correspondente.
	 */
	public PromocaoCodigoNacional getPromocaoCodigoNacional(int idtPromocao, int idtCodigoNacional)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		Map mapCodigoNacional = (Map)super.values.get(new Integer(idtPromocao));
		
		if(mapCodigoNacional != null)
		{
			PromocaoCodigoNacional result = (PromocaoCodigoNacional)mapCodigoNacional.get(new Integer(idtCodigoNacional));
			if(result != null)
			{
				return (PromocaoCodigoNacional)result.clone();
			}
		}
		
		return null;
	}

	/**
	 *	Este metodo retorna um objeto contendo o objeto PromocaoCodigoNacional para um dado valor de chave (id).
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		Integer					idtCodigoNacional			Chave a ser utilizada para localizar o Codigo Nacional.
	 *	@return		PromocaoCodigoNacional								Objeto PromocaoCodigoNacional correspondente.
	 */
	public PromocaoCodigoNacional getPromocaoCodigoNacional(Integer idtPromocao, Integer idtCodigoNacional)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		if((idtPromocao != null) && (idtCodigoNacional != null))
		{
			Map mapCodigoNacional = (Map)super.values.get(idtPromocao);
		
			if(mapCodigoNacional != null)
			{
				PromocaoCodigoNacional result = (PromocaoCodigoNacional)mapCodigoNacional.get(idtCodigoNacional);
				if(result != null)
				{
					return (PromocaoCodigoNacional)result.clone();
				}
			}
		}
		
		return null;
	}

}
