package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;

//Imports GPP.

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.TarifaTrocaMSISDN;

/**
 *	Mapeamento de tarifas para troca de MSISDN.
 *
 *	@author 	Daniel Ferreira
 *	@since 		29/03/2006
 */

public final class MapTarifaTrocaMSISDN extends Mapeamento 
{
    
	private static MapTarifaTrocaMSISDN instance = null;
	
	/**
	 *	Construtor da classe.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	private MapTarifaTrocaMSISDN() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapTarifaTrocaMSISDN								Instancia do singleton.
	 *	@throws 	GPPInternalErrorException
	 */
	public static MapTarifaTrocaMSISDN getInstancia() throws GPPInternalErrorException 
	{
		if (MapTarifaTrocaMSISDN.instance == null)
		{
		    MapTarifaTrocaMSISDN.instance = new MapTarifaTrocaMSISDN();
		}
		
		return MapTarifaTrocaMSISDN.instance;
	}
	
	/**
	 *	Carrega o mapeamento em memoria.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			String sqlQuery =	"SELECT " +
								"	ID_TARIFA, " +
								"	DES_TARIFA, " +
								"	NVL(TIP_TRANSACAO, ' ') AS TIP_TRANSACAO " +
								"FROM TBL_APR_TARIFA_TROCA_MSISDN";
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(registros.next())
			{
			    TarifaTrocaMSISDN tarifa = new TarifaTrocaMSISDN();
			    
			    Integer idTarifa = new Integer(registros.getInt("ID_TARIFA"));
			    tarifa.setIdTarifa(idTarifa);
			    
			    String desTarifa = registros.getString("DES_TARIFA");
			    tarifa.setDesTarifa(desTarifa);
			    
			    String tipTransacao = registros.getString("TIP_TRANSACAO");
			    tarifa.setTipTransacao(tipTransacao);
			    
			    super.values.put(idTarifa, tarifa);
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
	*	Retorna a descricao da tarifa da troca de MSISDN.
	*
	*	@param  String						idTarifa					Identificador da tarifa.
	*	@return String													Descricao da tarifa da troca de MSISDN. 
	*/
	public String getMapDescTarifa(String idTarifa)
	{
	    TarifaTrocaMSISDN tarifa = (TarifaTrocaMSISDN)super.get(new Object[]{new Integer(idTarifa)});
	    
	    if(tarifa != null)
	    {
	        return tarifa.getDesTarifa();
	    }
	    
	    return null;
	}
	
	/**
	 *	Retorna o tipo de transacao associado a tarifa da troca de MSISDN.
	 *
	 *	@param		String					idTarifa					Identificador da tarifa.
	 *	@return 	String 												Tipo de transacao associado a tarifa.
	 */
	public String getMapTipoTransacao(String idTarifa)
	{
	    TarifaTrocaMSISDN tarifa = (TarifaTrocaMSISDN)super.get(new Object[]{new Integer(idTarifa)});
	    
	    if(tarifa != null)
	    {
	        return tarifa.getTipTransacao();
	    }
	    
	    return null;
	}
	
}