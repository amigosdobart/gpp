package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento; 
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;


/**
 *	Este arquivo contem a classe que faz o mapeamento dos valores possiveis de recarga para acessos TFPP existentes no 
 *	BD Oracle em memoria no GPP.
 *
 *	@version		1.0		08/06/2004		Primeira versao.
 *	@author 		Denys Oliveira
 *
 *	@version		1.1		21/09/2004		Alteracao para mapear objeto ValoresRecarga no hash devido conter varias 
 *											informacoes a respeito do valor
 *	@author			Joao Carlos
 *
 *	@version		2.0		30/4/2007		Adaptacao para o Controle Total.
 *	@author			Daniel Ferreira
 */
public class MapValoresRecargaTFPP extends Mapeamento
{
	
	/**
	 *	Instancia do singleton. 
	 */
	private static MapValoresRecargaTFPP instance;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapValoresRecargaTFPP() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapValoresRecargaTFPP getInstancia()
	{
		return MapValoresRecargaTFPP.getInstance();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapValoresRecargaTFPP getInstance()
	{
		try
		{
			if(MapValoresRecargaTFPP.instance == null)
				MapValoresRecargaTFPP.instance = new MapValoresRecargaTFPP();
			
			return MapValoresRecargaTFPP.instance;
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
		PREPConexao	conexaoPrep	= null;
		
		try
		{
			String sqlQuery = 
				"SELECT id_valor, " +
				"       vlr_bonus, " +
				"       num_dias_expiracao " +
				"  FROM tbl_rec_valores_tfpp " +
				" ORDER BY id_valor ";
			
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				double		idValor			= registros.getDouble("id_valor");
				double		vlrCredito		= registros.getDouble("id_valor");
				double		vlrBonus		= registros.getDouble("vlr_bonus");
				short		numDiasExp		= registros.getShort ("num_dias_expiracao");
				
				ValoresRecarga valor = new ValoresRecarga();
				
				valor.setIdValor                  (idValor);
				valor.setSaldoPrincipal           (vlrCredito);
				valor.setValorBonusPrincipal      (vlrBonus);
				valor.setNumDiasExpiracaoPrincipal(numDiasExp);
				
				super.values.put(new Double(idValor), valor);
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
	 *	Retorna objeto (nao normalizado) contendo os valores de recarga.
	 *
	 *	@param		idValor					Identificador do valor.
	 *	@return		Objeto contendo os valores de recarga.
	 */
	public ValoresRecarga getValorRecarga(double idValor)
	{
		return (ValoresRecarga)super.values.get(new Double(idValor));
	}
	
}
