package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
 *	Mapeamento da tabela TBL_PRO_STATUS_ASSINANTE.
 *
 *	@author		Daniel Ferreira
 *	@since 		06/06/2006
 */
public final class MapPromocaoStatusAssinante extends Mapeamento
{
    
    //Atributos.
    
	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocaoStatusAssinante instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoStatusAssinante() throws GPPInternalErrorException 
	{
		super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 * @return		Instancia do singleton.
	 * @throws		GPPInternalErrorException
	 */
	public static MapPromocaoStatusAssinante getInstance() throws GPPInternalErrorException 
	{
		if(MapPromocaoStatusAssinante.instance == null) 
		{
		    MapPromocaoStatusAssinante.instance = new MapPromocaoStatusAssinante();
		}

		return MapPromocaoStatusAssinante.instance;
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
			    "SELECT idt_status, " +
			    "       des_status " +
			    "  FROM tbl_pro_status_assinante ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null,0);
			
			while (registros.next())
			{
				PromocaoStatusAssinante status = new PromocaoStatusAssinante();
				
				int idtStatus = registros.getInt("IDT_STATUS");
				status.setIdtStatus(idtStatus);
				status.setDesStatus(registros.getString("DES_STATUS"));
				
				super.values.put(new Integer(idtStatus), status);
			}
			
			registros.close();
			registros = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException ("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Retorna objeto PromocaoStatusAssinante representando o registro.
	 *
	 *	@param		idtStatus				Identificador da promocao.
	 *	@return		Objeto PromocaoStatusAssinante correspondente.
	 */
	public PromocaoStatusAssinante getPromocaoStatusAssinante(int idtStatus)
	{
	    return this.getPromocaoStatusAssinante(new Integer(idtStatus));
	}

	/**
	 *	Retorna objeto PromocaoStatusAssinante representando o registro.
	 *
	 *	@param		idtStatus				Identificador da promocao.
	 *	@return		Objeto PromocaoStatusAssinante correspondente.
	 */
	public PromocaoStatusAssinante getPromocaoStatusAssinante(Integer idtStatus)
	{
	    PromocaoStatusAssinante status = (PromocaoStatusAssinante)super.values.get(idtStatus);
		if(status != null)
		{
			return (PromocaoStatusAssinante)status.clone();	
		}
		
		return null;
	}

}
