package com.brt.gpp.comum.mapeamentos;

//Imports Java.
import java.sql.ResultSet;
import java.sql.SQLException;

//Imports GPP.
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoGPP;

/**
  *
  * Este arquivo contem a classe que faz o mapeamento das configuracaoes do GPP 
  * existentes no BD Oracle em memoria no GPP
  * <P> Version:		2.0
  *
  * @Autor: 		Camile Cardoso Couto
  * Data: 			12/04/2004
  * 
  *	@Alterado por:	Daniel Ferreira
  *	@since			23/11/2005
  *	@Motivo:		Adaptacao para novo modelo de mapeamentos.
  *
  */

public final class MapConfiguracaoGPP extends Mapeamento
{
	//Variaveis Membro
	private static MapConfiguracaoGPP instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapConfiguracaoGPP() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapConfiguracaoGPP		instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapConfiguracaoGPP getInstancia() throws GPPInternalErrorException 
	{
		if(MapConfiguracaoGPP.instance == null) 
		{
		    MapConfiguracaoGPP.instance = new MapConfiguracaoGPP();
		}
		
		return MapConfiguracaoGPP.instance;
	}	

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapConfiguracaoGPP		instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapConfiguracaoGPP getInstance() throws GPPInternalErrorException 
	{
		return MapConfiguracaoGPP.getInstancia();
	}	

	/**
	 *	Carrega o mapeamento de codigos nacionais em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			//Obtem uma conexao com o banco de dados.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			//Obtem o statement SQL.
			String sqlQuery =	"SELECT " +
						 		"	ID_CONFIGURACAO, " +
						 		"	VLR_CONFIGURACAO, " +
						 		"	DES_CONFIGURACAO " +
						 		"FROM TBL_GER_CONFIGURACAO_GPP ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				ConfiguracaoGPP configuracao = new ConfiguracaoGPP();
				
				String idConfiguracao = registros.getString("ID_CONFIGURACAO");
				configuracao.setIdConfiguracao(idConfiguracao);
				
				String vlrConfiguracao = registros.getString("VLR_CONFIGURACAO");
				configuracao.setVlrConfiguracao(vlrConfiguracao);
				
				String desConfiguracao = registros.getString("DES_CONFIGURACAO");
				configuracao.setDesConfiguracao(desConfiguracao);
				
				//Inserindo o registro no Map.
				super.values.put(idConfiguracao, configuracao);
			}
			
			registros.close();
			registros = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
   /**
	*	Retorna o valor da configuracao GPP para o identificador passado por parametro.
	*
	*	@param		idConfiguracao			Identificador da configuracao do GPP.
	*	@return								Valor da configuracao do GPP. 
	*/
	public String getMapValorConfiguracaoGPP(String idConfiguracao)
	{
	    ConfiguracaoGPP configuracao = (ConfiguracaoGPP)super.values.get(idConfiguracao);
	    
	    if(configuracao != null)
	    {
	        return configuracao.getVlrConfiguracao();
	    }
	    
	    return null;
	}

	/**
	 *	Retorna o valor da configuracao GPP para o identificador passado por parametro.
	 *
	 *	@param		idConfiguracao			Identificacao da configuracao do GPP.
	 *	@return								Descricao da configuracao GPP.
	 */
	 public String getMapDescricaoConfiguracaoGPP (String idConfiguracao)
	 {
		    ConfiguracaoGPP configuracao = (ConfiguracaoGPP)super.values.get(idConfiguracao);
		    
		    if(configuracao != null)
		    {
		        return configuracao.getDesConfiguracao();
		    }
		    
		    return null;
	 }
	 
}