package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoCredito;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Mapeamento das entidades da tabela TBL_REC_TIPOS_CREDITO em memoria.
 *
 *	@version		1.0		25/08/2005		Primeira versao.
 *	@author			Marcos C. Magalhaes
 *
 *	@version		2.0		04/05/2007		Atualizacao para o modelo de mapeamentos.
 *	@author			Daniel Ferreira
 */
public class MapTipoCredito extends Mapeamento 
{
	
	/**
	 *	Instancia do singleton.
	 */
	private static MapTipoCredito instance;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapTipoCredito() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapTipoCredito getInstance()
	{
		try
		{
			if(MapTipoCredito.instance == null)
				MapTipoCredito.instance = new MapTipoCredito();
			
			return MapTipoCredito.instance;
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
			String sqlQuery = 
				"SELECT id_tipo_credito, " +
				"       des_tipo_credito, " +
				"       idt_tipo_saldo " +
				"  FROM tbl_rec_tipos_creditos " +
				" ORDER BY id_tipo_credito ";
			
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				String		idTipoCredito	= registros.getString("id_tipo_credito");
				String		desTipoCredito	= registros.getString("des_tipo_credito");
				TipoSaldo	tipoSaldo		= MapTipoSaldo.getInstance().getTipoSaldo(registros.getShort("idt_tipo_saldo"));
				
				TipoCredito tipoCredito = new TipoCredito(idTipoCredito, desTipoCredito, tipoSaldo);
				super.values.put(idTipoCredito, tipoCredito);
			}
			
			registros.close();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Retorna o objeto com as informacoes do tipo de credito.
	 *
	 *	@param		idTipoCredito			Identificador do tipo de credito.
	 *	@return		Informacoes do tipo de credito.
	 */
	public TipoCredito getTipoCredito(String idTipoCredito)
	{
		return (TipoCredito)super.values.get(idTipoCredito);
	}
	
}