package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;

//Imports GPP.

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.ServicoAssinante;

/**
 *	Mapeamento dos servicos para os assinantes. 
 *
 *	@author 		Daniel Ferreira
 *	@since 			29/03/2006
 */

public final class MapServicosAssinante extends Mapeamento
{

	private static MapServicosAssinante instance = null;
	
	/**
	 *	Construtor da classe.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	private MapServicosAssinante() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 * 
	 *	@return		MapSistemaOrigem									Instancia do singleton.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapServicosAssinante getInstancia() throws GPPInternalErrorException 
	{
		if (MapServicosAssinante.instance == null)
		{
		    MapServicosAssinante.instance = new MapServicosAssinante();
		}
		
		return MapServicosAssinante.instance;
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
			String sqlQuery =	"SELECT ID_SERVICO, DES_SERVICO, IND_SERVICO_EXCLUSIVO " +
								"FROM TBL_APR_SERVICOS ";
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);

			while(registros.next())
			{
			    ServicoAssinante servico = new ServicoAssinante();
			    
			    String idServico = registros.getString("ID_SERVICO");
			    servico.setIdServico(idServico);
			    
			    String desServico = registros.getString("DES_SERVICO");
			    servico.setDesServico(desServico);
			    
			    String indServicoExclusivo = registros.getString("IND_SERVICO_EXCLUSIVO");
			    servico.setIndServicoExclusivo(indServicoExclusivo);
			    
			    super.values.put(idServico, servico);
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
	 *	Retorna sua descricao do servico.
	 *
	 *	@param		String					idServico					Identificador do servico.
	 *	@return		String												Descricao do servico.
	 */
	public String getMapDescServico(String idServico)
	{
	    ServicoAssinante servico = (ServicoAssinante)super.get(new Object[]{idServico});
	    
	    return (servico != null) ? servico.getDesServico() : null;
	}

	/**
	 *	Retorna o indicador de servico exclusivo do GPP.
	 *
	 *	@param		String					idServico					Identificador do servico.
	 *	@return		String												Indicador de servico exclusivo do GPP.
	 */
	public String getMapServicoExclusivo(String idServico)
	{
	    ServicoAssinante servico = (ServicoAssinante)super.get(new Object[]{idServico});
	    
	    return (servico != null) ? servico.getIndServicoExclusivo() : null;
	}
	
}