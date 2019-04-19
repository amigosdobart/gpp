package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante;

/**
 *	Mapeamento de status periodico de assinantes.
 *
 *	@author 	Daniel Ferreira
 *	@since 		09/04/2007
 */
public final class MapStatusPeriodico extends Mapeamento
{
    
	private static MapStatusPeriodico instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapStatusPeriodico() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapStatusPeriodico getInstancia() 
	{
		try
		{
			if(MapStatusPeriodico.instance == null)
				MapStatusPeriodico.instance = new MapStatusPeriodico();
		}
		catch(Exception e)
		{
			return null;
		}
		
		return MapStatusPeriodico.instance;
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
			
			String sqlQuery =
				"SELECT tip_configuracao, " +
				"       id_configuracao, " +
				"       des_configuracao, " +
				"       ind_hibrido " +
				"  FROM tbl_ger_config_assinante " +
				" WHERE tip_configuracao = ? ";
			
			Object parametros[] = {"STATUS_PERIODICO"};
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, 0);
			
			while(registros.next())
			{
			    ConfiguracaoAssinante configuracao = new ConfiguracaoAssinante();
			    
			    String tipConfiguracao = registros.getString("tip_configuracao");
			    configuracao.setTipConfiguracao(tipConfiguracao);
			    
			    Integer idConfiguracao = new Integer(registros.getInt("id_configuracao"));
			    configuracao.setIdConfiguracao(idConfiguracao);
			    
			    String desConfiguracao = registros.getString("des_configuracao");
			    configuracao.setDesConfiguracao(desConfiguracao);
			    
			    int indHibrido = registros.getInt("ind_hibrido");
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
	*	Retorna a descricao do status periodico do assinante.
	*
	*	@param		idConfiguracao			Identificacao do status periodico.
	*	@return		Descricao do status periodico. 
	*/
	public String getMapDescStatusPeriodico(String idConfiguracao)
	{
	    ConfiguracaoAssinante configuracao = (ConfiguracaoAssinante)super.get(new Object[]{new Integer(idConfiguracao)});

	    if(configuracao != null)
	        return configuracao.getDesConfiguracao();
	    
	    return null;
	}
	
}