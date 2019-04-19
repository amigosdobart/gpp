package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 *	Mapeamento do relacionamento entre Sistemas de Origem e Origens de Recarga. Este mapeamento representa quais 
 *	Origens de Recarga sao permitidas para cada Sistema de Origem. 
 *
 *	@version	1.0		30/04/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class MapOrigemSistema extends Mapeamento 
{

	/**
	 *	Instancia do singleton. 
	 */
	private static MapOrigemSistema instance;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException 
	 */
	private MapOrigemSistema() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton. 
	 */
	public static MapOrigemSistema getInstance()
	{
		try
		{
			if(MapOrigemSistema.instance == null)
				MapOrigemSistema.instance = new MapOrigemSistema();
			
			return MapOrigemSistema.instance;
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
		PREPConexao conexaoPrep = null;
		
		try
		{
			String sqlQuery = 
				"SELECT id_canal, " +
				"       id_origem, " +
				"       id_sistema_origem " +
				"  FROM tbl_rec_origem_sistema " +
				" ORDER BY id_sistema_origem, " +
				"          id_canal, " +
				"          id_origem ";
			
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				String	idCanal			= registros.getString("id_canal");
				String	idOrigem		= registros.getString("id_origem");
				String	idSistemaOrigem	= registros.getString("id_sistema_origem");
				
				OrigemRecarga origem = MapRecOrigem.getInstance().getOrigemRecarga(idCanal + idOrigem);
				
				Collection listaOrigens = (Collection)super.values.get(idSistemaOrigem);
				if(listaOrigens == null)
				{
					listaOrigens = Collections.synchronizedCollection(new ArrayList());
					super.values.put(idSistemaOrigem, listaOrigens);
				}
				
				listaOrigens.add(origem);
			}
			
			registros.close();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}

	
	/**
	 *	Retorna a lista de origens de recarga permitidas para o sistema de origem.
	 *
	 *	@param		idSistemaOrigem			Identificador do sistema de origem. 
	 *	@return		Lista de origens permitidas. 
	 */
	public Collection getListaOrigensRecarga(String idSistemaOrigem)
	{
		Collection result = (Collection)super.values.get(idSistemaOrigem);
		return (result != null) ? result : new ArrayList();
	}
	
}
