package com.brt.gpp.comum.mapeamentos;

//Imports Java.
import java.sql.ResultSet;
import java.sql.SQLException;

//Imports GPP.
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoRetorno;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Mapeamento dos registros da tabela TBL_GER_CODIGOS_RETORNO em memoria.
 *
 *	@author		Marcelo Alves Araujo
 *	@since 		03/08/2006
 */
public final class MapCodigosRetorno extends Mapeamento
{

    private static MapCodigosRetorno instance = null;
    private static GerentePoolLog	 log      = null;
    
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapCodigosRetorno() throws GPPInternalErrorException 
	{
		super();		
		log = GerentePoolLog.getInstancia(this.getClass());
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapCodigoCobranca		instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapCodigosRetorno getInstance()
	{
		try
		{		
			if(MapCodigosRetorno.instance == null) 
				MapCodigosRetorno.instance = new MapCodigosRetorno();
		}
		catch(GPPInternalErrorException e)
		{
			log.log(0, Definicoes.ERRO,"MapCodigosRetorno","getInstance", "Erro ao carregar codigos de retorno");
		}
		
		return MapCodigosRetorno.instance;
	}	
	
	/**
	 *	Carrega em memória os codigos de retorno.
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
			String sqlQuery =	"SELECT                     	" +
						 		"	ID_RETORNO,             	" +
						 		"	VLR_RETORNO,            	" +
						 		"	DES_RETORNO	    			" +
						 		"FROM tbl_ger_codigos_retorno 	";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				CodigoRetorno codigo = new CodigoRetorno();
				
				String idRetorno = registros.getString("ID_RETORNO");
				codigo.setIdRetorno(idRetorno);
				
				String valorRetorno = registros.getString("VLR_RETORNO");
				codigo.setValorRetorno(valorRetorno);
				
				String descRetorno = registros.getString("DES_RETORNO");
				codigo.setDescRetorno(descRetorno);
				
				//Inserindo o registro no Map.
				super.values.put(valorRetorno, codigo);
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
	 *	Retorna o valor do valor do retorno.
	 *
	 *	@param		String 					valorRetorno				Valor do retorno
	 *	@return		CodigoRetorno										Objeto com os dados do retorno.
	 */
	public CodigoRetorno getRetorno(String valorRetorno)
	{
		CodigoRetorno codigoRetorno = (CodigoRetorno)super.values.get(valorRetorno);
		
		return codigoRetorno;
	}

	
	/**
	 *	Retorna o valor do valor do retorno.
	 *
	 *	@param		int 					valorRetorno				Valor do retorno
	 *	@return		CodigoRetorno										Objeto com os dados do retorno.
	 */
	public CodigoRetorno getRetorno(int valorRetorno)
	{		
		CodigoRetorno codigoRetorno = (CodigoRetorno)super.values.get(valorRetorno+"");
		
		return codigoRetorno;
	}
}