package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Imports GPP.

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.ValorRecargaPlanoPreco;

/**
 *	Mapeamento do relacionamento entre planos de preco e valores de recarga em memoria.
 *
 *	@author 	Daniel Ferreira
 *	@since 		29/03/2006
 */
public final class MapValoresRecargaPlanoPreco extends Mapeamento
{
	private static MapValoresRecargaPlanoPreco instance = null;
	
	/**
	 *	Construtor da classe.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	private MapValoresRecargaPlanoPreco() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 * 
	 *	@return		MapSistemaOrigem									Instancia do singleton.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapValoresRecargaPlanoPreco getInstancia() throws GPPInternalErrorException 
	{
		if (MapValoresRecargaPlanoPreco.instance == null)
		{
		    MapValoresRecargaPlanoPreco.instance = new MapValoresRecargaPlanoPreco();
		}

		return MapValoresRecargaPlanoPreco.instance;
	}	

	/**
	 *	Carrega o mapeamento em memoria.
	 *
	 *	@throws GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			String sqlQuery =	"SELECT ID_VALOR, IDT_PLANO_PRECO " +
			             		"FROM TBL_REC_VALORES_PLANO_PRECO ";
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null,0);
			while(registros.next())
			{
				ValorRecargaPlanoPreco valor = new ValorRecargaPlanoPreco();
				
				Double idValor = new Double(registros.getDouble("ID_VALOR"));
				valor.setIdValor(idValor);
				
				Long idtPlanoPreco = new Long(registros.getLong("IDT_PLANO_PRECO"));
				valor.setIdtPlanoPreco(idtPlanoPreco);

				Map mapPlanos = (Map)super.values.get(idtPlanoPreco);
				if(mapPlanos == null)
				{
				    mapPlanos = Collections.synchronizedMap(new HashMap());
				    super.values.put(idtPlanoPreco, mapPlanos);
				}
				mapPlanos.put(idValor, valor);
			}
			
			registros.close();
			registros = null;
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException ("Excecao: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Retorna o relacionamento entre valores de recarga e planos de preco.
	 *
	 *	@param		double					idValor						Identificador de valor de recarga.
	 *	@param 		long					idtPlanoPreco				Identificador do plano de preco.
	 *	@return		ValorRecargaPlanoPreco								Relacionamento entre valores de recarga e planos de preco.
	 */
	public ValorRecargaPlanoPreco getValorRecargaPlanoPreco(double idValor, long idtPlanoPreco)
	{
	    return (ValorRecargaPlanoPreco)super.get(new Object[]{new Long(idtPlanoPreco), new Double(idValor)});
	}

	/**
	 *	Procura na lista por um objeto de relacionamento entre valores de recarga e planos de preco.
	 *
	 *	@param		double					idValor						Identificador de valor de recarga.
	 *	@param		long					idtPlanoPreco				Identificador do plano de preco.
	 *	@return		boolean					True se o objeto existir na lista e false caso contrario.
	 */
	public boolean existeValorRecargaPlanoPreco(double idValor, long idtPlanoPreco)
	{
	    return (super.get(new Object[]{new Long(idtPlanoPreco), new Double(idValor)}) != null);
	}
}
