package com.brt.gpp.comum.mapeamentos;

//Imports Java.
import java.sql.ResultSet;
import java.sql.SQLException;

//Imports GPP.
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoOrigemEstorno;

/**
 *	Mapeamento de registros da tabela TBL_PRO_ORIGEM_ESTORNO em memoria do processo do GPP. 
 *	<P> Version:		1.0
 *
 *	@author		Daniel Ferreira
 *	@since		23/11/2005
 */

public final class MapPromocaoOrigemEstorno extends Mapeamento
{
    
	private static MapPromocaoOrigemEstorno instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoOrigemEstorno() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapPromocaoOrigemEstorno instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoOrigemEstorno getInstance() throws GPPInternalErrorException 
	{
		if(MapPromocaoOrigemEstorno.instance == null) 
		{
		    MapPromocaoOrigemEstorno.instance = new MapPromocaoOrigemEstorno();
		}
		
		return MapPromocaoOrigemEstorno.instance;
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
						 		"	IDT_ORIGEM, " +
						 		"	DES_ORIGEM, " +
						 		"	TIP_ANALISE " +
						 		"FROM TBL_PRO_ORIGEM_ESTORNO ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				PromocaoOrigemEstorno origem = new PromocaoOrigemEstorno();
				
				String idtOrigem = registros.getString("IDT_ORIGEM");
				origem.setIdtOrigem(idtOrigem);
				
				String desOrigem = registros.getString("DES_ORIGEM");
				origem.setDesOrigem(desOrigem);
				
				String tipAnalise = registros.getString("TIP_ANALISE");
				origem.setTipAnalise(tipAnalise);
				
				//Inserindo o registro no Map.
				super.values.put(idtOrigem, origem);
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
	*	Retorna a descricao da origem do estorno para o identificador passado por parametro.
	*
	*	@param		String					idtOrigem					Identificador da origem do estorno.
	*	@return															Descricao da origem do estorno. 
	*/
	public String getDesOrigem(String idtOrigem)
	{
	    PromocaoOrigemEstorno origem = (PromocaoOrigemEstorno)super.values.get(idtOrigem);
	    
	    if(origem != null)
	    {
	        return origem.getDesOrigem();
	    }
	    
	    return null;
	}

	/**
	 *	Retorna o tipo de calculo para o periodo de analise associado ao estorno.
	 *
	 *	@param		String					idtOrigem					Identificador da origem do estorno.
	 *	@return															Tipo de calculo do periodo de analise.
	 */
	 public String getTipAnalise(String idtOrigem)
	 {
		    PromocaoOrigemEstorno origem = (PromocaoOrigemEstorno)super.values.get(idtOrigem);
		    
		    if(origem != null)
		    {
		        return origem.getTipAnalise();
		    }
		    
		    return null;
	 }
	 
}