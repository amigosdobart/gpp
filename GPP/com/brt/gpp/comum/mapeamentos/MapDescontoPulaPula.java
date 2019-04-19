package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
 *	Mapeamento de registros da tabela TBL_PRO_DESCONTO_PULA_PULA.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/03/2008
 *	@modify		Primeira versao.
 */
public final class MapDescontoPulaPula extends Mapeamento
{
    
	private static MapDescontoPulaPula instance = null;
	
	/**
	 *	Construtor da classe.
	 *	
	 *	@throws		GPPInternalErrorException
	 */
	private MapDescontoPulaPula() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static synchronized MapDescontoPulaPula getInstance() 
	{
		try
		{
			if(MapDescontoPulaPula.instance == null) 
				MapDescontoPulaPula.instance = new MapDescontoPulaPula();

			return MapDescontoPulaPula.instance;
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
				"SELECT id_desconto, " +
				"       des_desconto, " +
				"       ind_disponivel_extrato, " +
				"       ind_estorno " +
				"  FROM tbl_pro_desconto_pula_pula ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(registros.next())
			{
				DescontoPulaPula desconto = new DescontoPulaPula();
				
				short idDesconto = registros.getShort("id_desconto");
				desconto.setIdDesconto(idDesconto);
				
				desconto.setDesDesconto(registros.getString("des_desconto"));
				desconto.setIndDisponivelExtrato(registros.getShort("ind_disponivel_extrato") != 0);
				desconto.setIndEstorno(registros.getShort("ind_estorno") != 0);
				
				super.values.put(new Short(idDesconto), desconto);
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
	 *	@param		idDesconto				Identificador do desconto Pula-Pula.
	 *	@return		Objeto PromocaoCategoria correspondente.
	 */
	public DescontoPulaPula getDescontoPulaPula(short idDesconto)
	{
	    return (DescontoPulaPula)super.values.get(new Short(idDesconto));
	}

}
