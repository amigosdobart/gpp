package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoAssinante;

/**
*	Mapeamento de status de servico de assinantes.
*
*	@author 	Daniel Ferreira
*	@since 		29/03/2006
*
*/
public final class MapStatusServico extends Mapeamento
{
    
	private static MapStatusServico instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapStatusServico() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapStatusServico getInstancia() 
	{
		try
		{
			if(MapStatusServico.instance == null)
				MapStatusServico.instance = new MapStatusServico();
		}
		catch(Exception e)
		{
			return null;
		}
		
		return MapStatusServico.instance;
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
			
			Object parametros[] = {"STATUS_SERVICO"};
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
	public String getMapDescStatusServico(String idConfiguracao)
	{
	    ConfiguracaoAssinante configuracao = (ConfiguracaoAssinante)super.get(new Object[]{new Integer(idConfiguracao)});

	    if(configuracao != null)
	    {
	        return configuracao.getDesConfiguracao();
	    }
	    
	    return null;
	}
	
}