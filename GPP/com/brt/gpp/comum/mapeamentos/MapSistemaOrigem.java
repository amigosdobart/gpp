package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.Collection;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapOrigemSistema;
import com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem;

/**
 *	Mapeamento da tabela TBL_REC_SISTEMA_ORIGEM.
 *
 *	@version	1.0		29/03/2006		Primeira versao.
 *	@author		Daniel Ferreira
 *
 *	@version	1.1		30/4/2007		Inclusao de lista de origens de recarga validos para o sistema de origem.
 *	@author		Daniel Ferreira
 */
public final class MapSistemaOrigem extends Mapeamento 
{
    
	/**
	 *	Instancia do singleton.
	 */
	private static MapSistemaOrigem instance = null;
	
	/**
	 *	Construtor da classe.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	private MapSistemaOrigem() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 * 
	 *	@return		Instancia do singleton.
	 */
	public static MapSistemaOrigem  getInstancia() 
	{
		try
		{
			if (MapSistemaOrigem.instance == null)
			    MapSistemaOrigem.instance = new MapSistemaOrigem();
			
			return MapSistemaOrigem.instance;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load()
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			String sqlQuery =	"SELECT id_sistema_origem, " +
								"       des_sistema_origem, " +
								"       ind_valida_sistema_origem_cc, " +
								"       ind_valida_saldo_maximo " +
								"  FROM tbl_rec_sistema_origem ";
			
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
			    String		idSistemaOrigem			= registros.getString("id_sistema_origem");
			    String		desSistemaOrigem		= registros.getString("des_sistema_origem");
			    boolean		indValidaCc				= (registros.getInt("ind_valida_sistema_origem_cc") != 0);
			    boolean		indValidaSaldoMaximo	= (registros.getInt("ind_valida_saldo_maximo") != 0);
			    Collection	listaOrigens			= MapOrigemSistema.getInstance().getListaOrigensRecarga(idSistemaOrigem);

			    SistemaOrigem sistema = new SistemaOrigem(idSistemaOrigem,
			    										  desSistemaOrigem,
														  indValidaCc,
														  indValidaSaldoMaximo,
														  listaOrigens);
			    
			    super.values.put(idSistemaOrigem, sistema);
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
	 *	Retorna as informacoes do Sistema de Origem.
	 *
	 *	@param		idSistemaOrigem			Identificador do sistema de origem.
	 *	@return		Informacoes do Sistema de Origem. 
	 */
	public SistemaOrigem getSistemaOrigem(String idSistemaOrigem)
	{
		return (SistemaOrigem)super.values.get(idSistemaOrigem);
	}
	
}