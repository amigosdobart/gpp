package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Mapeamento da tabela TBL_APR_SALDO_CATEGORIA. Esta tabela define os tipos de saldo para cada categoria de plano de 
 *	preco. Em outras palavras, define quais os saldos que uma categoria de plano pode ter. Ex: Os planos GSM podem ter 
 *	o Saldo Principal, Saldo de Bonus, Saldo de Torpedos e Saldo de Dados. O LigMix tem somente o Saldo Principal. Os 
 *	planos Controle Total possuem o Saldo Principal e o Saldo Periodico.
 *
 * 	@version	1.0		Versao inicial.  
 *	@author		Daniel Ferreira
 *
 *  @version    1.1     Adaptação para o SaldoCategoria
 *  @author     Vitor Murilo Dias
 */
public class MapSaldoCategoria extends Mapeamento
{

	/**
	 *	Instancia do singleton. 
	 */
	private static MapSaldoCategoria instance;
	
	/**
	 *	Construtor da classe. 
	 */
	private MapSaldoCategoria() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 * 	@return		Instancia do singleton. 
	 */
	public static MapSaldoCategoria getInstance()
	{
		try
		{
			if(MapSaldoCategoria.instance == null)
				MapSaldoCategoria.instance = new MapSaldoCategoria();
			
			return MapSaldoCategoria.instance;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load() 
	 */
	protected synchronized void load() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery = 
				"SELECT idt_categoria, 					 " +
				"       idt_tipo_saldo,					 " +
				"		nom_tipo_saldo					 " +
				"  FROM tbl_apr_saldo_categoria 		 " +
				"  ORDER BY idt_categoria, idt_tipo_saldo" ;
			
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				SaldoCategoria saldoCategoria = new SaldoCategoria();
				Categoria categoria = MapCategoria.getInstance().getCategoria(registros.getInt("idt_categoria"));
				TipoSaldo tipoSaldo = MapTipoSaldo.getInstance().getTipoSaldo(registros.getShort("idt_tipo_saldo"));
				
				saldoCategoria.setCategoria(categoria);
				saldoCategoria.setTipoSaldo(tipoSaldo);
				saldoCategoria.setNomSaldo(registros.getString("nom_tipo_saldo"));
				
				ArrayList saldos = (ArrayList)super.values.get(new Integer(categoria.getIdCategoria()));
								
				if(saldos == null)
				{
					saldos = new ArrayList();
					super.values.put(new Integer(categoria.getIdCategoria()), saldos);
				}
				
				saldos.add(saldoCategoria);
			}
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
	 * 	Retorna os tipos de saldo associados a categoria de planos de preco.
	 * 
	 * 	@param		idtCategoria			Identificador da categoria de planos.
	 *  @return		Tipos de saldo associados a categoria.
	 */
	public Collection getTiposSaldo(int idtCategoria)
	{
		Collection result = (Collection)(super.values.get(new Integer(idtCategoria)));
		
		return (result != null) ? result : new ArrayList();
	}
	
	/**
	 * Método responsável por retornar o nome do saldo.
	 * 
	 * @param idtCategoria
	 * @param idtTipoSaldo
	 * @return String
	 */
	public String getNomeSaldo(int idtCategoria, int idtTipoSaldo)
	{
		Collection saldoCategoriaCol = (Collection)super.values.get(new Integer(idtCategoria));
		Iterator i = saldoCategoriaCol.iterator();
		
		while (i.hasNext())
		{
			SaldoCategoria sdCategoria = (SaldoCategoria)i.next();
			
			if (sdCategoria.getTipoSaldo().getIdtTipoSaldo() == idtTipoSaldo)
				return sdCategoria.getNomSaldo();				
		}
			
		return null;		
		
	}
	
}
