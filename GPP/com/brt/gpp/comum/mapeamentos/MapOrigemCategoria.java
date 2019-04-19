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
 *	Mapeamento do relacionamento entre Categorias de Planos de Preco e Origens de Recarga. Este relacionamento define 
 *	quais as origens de recarga sao permitidas para cada categoria.
 *
 *	@version	1.0		30/04/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class MapOrigemCategoria extends Mapeamento 
{

	/**
	 *	Instancia do singleton. 
	 */
	private static MapOrigemCategoria instance;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException 
	 */
	private MapOrigemCategoria() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton. 
	 */
	public static MapOrigemCategoria getInstance()
	{
		try
		{
			if(MapOrigemCategoria.instance == null)
				MapOrigemCategoria.instance = new MapOrigemCategoria();
			
			return MapOrigemCategoria.instance;
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
				"       idt_categoria " +
				"  FROM tbl_rec_origem_categoria " +
				" ORDER BY idt_categoria, " +
				"          id_canal, " +
				"          id_origem ";
			
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				String	idCanal			= registros.getString("id_canal");
				String	idOrigem		= registros.getString("id_origem");
				Integer	idtCategoria	= new Integer(registros.getInt("idt_categoria"));
				
				OrigemRecarga origem = MapRecOrigem.getInstance().getOrigemRecarga(idCanal + idOrigem);
				
				Collection listaOrigens = (Collection)super.values.get(idtCategoria);
				if(listaOrigens == null)
				{
					listaOrigens = Collections.synchronizedCollection(new ArrayList());
					super.values.put(idtCategoria, listaOrigens);
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
	 *	Retorna a lista de origens de recarga permitidas para a categoria informada.
	 *
	 *	@param		idtCategoria			Categoria de planos de preco.
	 *	@return		Lista de origens de recarga permitidas para a categoria. 
	 */
	public Collection getListaOrigensRecarga(int idtCategoria)
	{
		Collection result = (Collection)super.values.get(new Integer(idtCategoria));
		return (result != null) ? result : new ArrayList();
	}
	
}
