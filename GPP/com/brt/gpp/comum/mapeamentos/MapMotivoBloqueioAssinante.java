package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;

//Imports GPP.

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante;

/**
*	Mapeamento de status de assinantes.
*
*	@author 	Daniel Ferreira
*	@since 		29/03/2006
*
*/
public final class MapMotivoBloqueioAssinante extends Mapeamento
{
  
	private static MapMotivoBloqueioAssinante instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapMotivoBloqueioAssinante() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapMotivoBloqueioAssinante		instance					Instancia do singleton.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapMotivoBloqueioAssinante getInstancia() throws GPPInternalErrorException 
	{
		if(MapMotivoBloqueioAssinante.instance == null)
		{
			MapMotivoBloqueioAssinante.instance = new MapMotivoBloqueioAssinante();
		}
		
		return MapMotivoBloqueioAssinante.instance;
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
			
			String sqlQuery =	"SELECT " +
								"	TIP_CONFIGURACAO, " +
								"	ID_CONFIGURACAO, " +
								"	DES_CONFIGURACAO, " +
								"	IND_HIBRIDO " +
								"FROM TBL_GER_CONFIG_ASSINANTE " +
								"WHERE TIP_CONFIGURACAO = ?";
			
			Object parametros[] = {"MOTIVO_BLOQUEIO"};
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, 0);
			
			while(registros.next())
			{
			    ConfiguracaoAssinante configuracao = new ConfiguracaoAssinante();
			    
			    String tipConfiguracao = registros.getString("TIP_CONFIGURACAO");
			    configuracao.setTipConfiguracao(tipConfiguracao);
			    
			    Integer idConfiguracao = new Integer(registros.getInt("ID_CONFIGURACAO"));
			    configuracao.setIdConfiguracao(idConfiguracao);
			    
			    String desConfiguracao = registros.getString("DES_CONFIGURACAO");
			    configuracao.setDesConfiguracao(desConfiguracao);
			    
			    int indHibrido = registros.getInt("IND_HIBRIDO");
			    configuracao.setIndHibrido(registros.wasNull() ? null : new Integer(indHibrido));
			    
			    super.values.put(idConfiguracao, configuracao);
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
	*	Retorna a descricao do status do assinante.
	*
	*	@param		String					idConfiguracao				Identificacao do status do assinante.
	*	@return		String 												Descricao do status do assinante. 
	*/
	public String getMapDescMotivoBloqueioAssinante(String idConfiguracao)
	{
	    ConfiguracaoAssinante configuracao = (ConfiguracaoAssinante)super.get(new Object[]{new Integer(idConfiguracao)});

	    if(configuracao != null)
	    {
	        return configuracao.getDesConfiguracao();
	    }
	    
	    return null;
	}
	
}