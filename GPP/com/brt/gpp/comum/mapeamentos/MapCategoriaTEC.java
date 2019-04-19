package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC;

/**
 *	Mapeamento da tabela TBL_TEC_CATEGORIA, que corresponde a definicao pela Tecnomen das categorias de planos.
 * 
 *	@author		Daniel Ferreira
 *	@since		12/08/2007
 */
public class MapCategoriaTEC extends Mapeamento
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapCategoriaTEC instance;
	
	/**
	 *	Construtor da classe.
	 */
	private MapCategoriaTEC() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapCategoriaTEC getInstance()
	{
		try
		{
			if(MapCategoriaTEC.instance == null)
				MapCategoriaTEC.instance = new MapCategoriaTEC();
			
			return MapCategoriaTEC.instance;
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
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery =
				"SELECT idt_categoria, " +
				"       des_categoria, " +
				"       idt_dialeto, " +
				"       idt_status_senha, " +
				"		num_csp_default, " +
				"		ind_chamada_acobrar, " +
				"		ind_reset_senha " +
				"  FROM tbl_tec_categoria ";
			
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				short	idtCategoria		= registros.getShort("idt_categoria");
				String	desCategoria		= registros.getString("des_categoria");
				short	idtDialeto			= registros.getShort("idt_dialeto");
				short	idtStatusSenha		= registros.getShort("idt_status_senha");
				short	numCspDefault		= registros.getShort("num_csp_default");
				boolean	indChamadaACobrar	= (registros.getShort("ind_chamada_acobrar") != 0);
				boolean	indResetSenha		= (registros.getShort("ind_reset_senha"    ) != 0);
				
				CategoriaTEC categoria = new CategoriaTEC(idtCategoria, 
														  desCategoria, 
														  idtDialeto, 
														  idtStatusSenha, 
														  numCspDefault,
														  indChamadaACobrar,
														  indResetSenha);
				
				super.values.put(new Short(idtCategoria), categoria);
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
	 *	Retorna objeto correspondente a um registro da tabela.
	 *
	 *	@param		idtCategoria			Identificador da categoria de plano de preco.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public CategoriaTEC getCategoriaTEC(short idtCategoria)
	{
		return (CategoriaTEC)super.values.get(new Short(idtCategoria));
	}
	
}
