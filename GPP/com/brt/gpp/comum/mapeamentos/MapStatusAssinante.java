package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;

//Imports GPP.

import com.brt.gpp.comum.Definicoes;
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
public final class MapStatusAssinante extends Mapeamento
{
    
	private static MapStatusAssinante instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapStatusAssinante() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapStatusAssinante getInstancia() 
	{
		try
		{
			if(MapStatusAssinante.instance == null)
				MapStatusAssinante.instance = new MapStatusAssinante();
		}
		catch(Exception e)
		{
			return null;
		}
		
		return MapStatusAssinante.instance;
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
			
			Object parametros[] = {"STATUS_ASSINANTE"};
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
	public String getMapDescStatusAssinante(String idConfiguracao)
	{
	    ConfiguracaoAssinante configuracao = (ConfiguracaoAssinante)super.get(new Object[]{new Integer(idConfiguracao)});

	    if(configuracao != null)
	    {
	        return configuracao.getDesConfiguracao();
	    }
	    
	    return null;
	}
	
	/**
	 *	Retorna o status do assinante no formato String. 
	 *
	 *	@param		status					Identificador do status do assinante.
	 *	@return		Status do assinante no formato string.
	 */
	public static String getStatusAssinanteAsString(short status)
	{
		switch(status)
		{
			case Definicoes.STATUS_FIRST_TIME_USER:
				return Definicoes.TIPO_APR_STATUS_FIRSTIME;
			case Definicoes.STATUS_NORMAL_USER:
				return Definicoes.TIPO_APR_STATUS_NORMAL;
			case Definicoes.STATUS_RECHARGE_EXPIRED:
				return Definicoes.TIPO_APR_STATUS_RECHARGE_EXPIRED;
			case Definicoes.STATUS_DISCONNECTED:
				return Definicoes.TIPO_APR_STATUS_DISCONNECTED;
			case Definicoes.STATUS_SHUTDOWN:
				return Definicoes.TIPO_APR_SHUTDOWN;
			default: return null;
		}
	}
	
}