package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
 *	Mapeamento de registros da tabela TBL_PRO_CATEGORIA.
 *
 *	@author		Daniel Ferreira
 *	@since		15/08/2005
 */
public final class MapPromocaoCategoria extends Mapeamento
{
    
	private static MapPromocaoCategoria instance = null;
	
	/**
	 *	Construtor da classe.
	 *	
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoCategoria() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return								Mapeamento da TBL_PRO_CATEGORIA.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoCategoria getInstance () throws GPPInternalErrorException 
	{
		if (MapPromocaoCategoria.instance == null) 
		{
			MapPromocaoCategoria.instance = new MapPromocaoCategoria();
		}
		return instance;
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
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);

			String sql = "SELECT " +
						 "  IDT_CATEGORIA, " +
						 "  NOM_CATEGORIA," +
						 "  IND_CAD_ASSINANTE_EXCLUSIVO " +
			             "FROM TBL_PRO_CATEGORIA ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sql, null, 0);
			while (registros.next())
			{
				PromocaoCategoria categoria = new PromocaoCategoria();
				
				int idtCategoria = registros.getInt("IDT_CATEGORIA");
				categoria.setIdtCategoria(idtCategoria);
				categoria.setNomCategoria(registros.getString("NOM_CATEGORIA"));
				categoria.setIndCadAssinanteExclusivo(registros.getInt("IND_CAD_ASSINANTE_EXCLUSIVO"));
				
				super.values.put(new Integer(idtCategoria), categoria);
			}
			
			registros.close();
			registros = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException ("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Retorna o objeto representando um registro da tabela.
	 *
	 * @param		idtCategoria			Identificador da categoria de promocao.
	 * @return								Objeto PromocaoCategoria correspondente.
	 */
	public PromocaoCategoria getPromocaoCategoria(int idtCategoria)
	{
	    return this.getPromocaoCategoria(new Integer(idtCategoria));
	}

	/**
	 *	Retorna o objeto representando um registro da tabela.
	 *
	 * @param		idtCategoria			Identificador da categoria de promocao.
	 * @return								Objeto PromocaoCategoria correspondente.
	 */
	public PromocaoCategoria getPromocaoCategoria(Integer idtCategoria)
	{
	    PromocaoCategoria categoria = (PromocaoCategoria)super.values.get(idtCategoria);
	    
		if(categoria != null)
		{
			return (PromocaoCategoria)categoria.clone();	
		}
		
		return null;
	}

}
