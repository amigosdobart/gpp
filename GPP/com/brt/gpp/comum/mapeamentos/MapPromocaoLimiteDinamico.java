package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteDinamico;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
 *	Mapeamento de registros da tabela TBL_PRO_LIMITE_DINAMICO.
 *
 *	@author		Daniel Ferreira
 *	@since 		09/11/2005
 */
public class MapPromocaoLimiteDinamico extends Mapeamento
{

	private static MapPromocaoLimiteDinamico instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoLimiteDinamico() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Cria uma instancia ou refere-se a instancia ja existente de MapPromocaoLimiteDinamico.
	 *
	 *	@return								Mapeamento da TBL_PRO_LIMITE_DINAMICO.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoLimiteDinamico getInstance() throws GPPInternalErrorException 
	{
		if(MapPromocaoLimiteDinamico.instance == null) 
		{
			MapPromocaoLimiteDinamico.instance = new MapPromocaoLimiteDinamico();
		}

		return MapPromocaoLimiteDinamico.instance;
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
			
			String sql = "SELECT idt_limite_dinamico, " +
			 			 "       dat_ini_periodo, " +
			 			 "       dat_fim_periodo, " +
			 			 "       des_limite_dinamico, " +
			 			 "       vlr_threshold_superior, " +
			 			 "       vlr_threshold_inferior " +
			 			 "  FROM tbl_pro_limite_dinamico ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sql, null,0);
			
			while (registros.next())
			{
				PromocaoLimiteDinamico limite = new PromocaoLimiteDinamico();
				
				limite.setIdtLimiteDinamico(registros.getInt("IDT_LIMITE_DINAMICO"));
				limite.setDatIniPeriodo(registros.getDate("DAT_INI_PERIODO"));
				limite.setDatFimPeriodo(registros.getDate("DAT_FIM_PERIODO"));
				limite.setDesLimiteDinamico(registros.getString("DES_LIMITE_DINAMICO"));
				limite.setVlrThresholdInferior(registros.getDouble("VLR_THRESHOLD_INFERIOR"));
				limite.setVlrThresholdSuperior(registros.getDouble("VLR_THRESHOLD_SUPERIOR"));
				
				Map mapLimite = (Map)super.values.get(new Integer(limite.getIdtLimiteDinamico()));
				if(mapLimite == null)
				{
				    mapLimite = Collections.synchronizedMap(new HashMap());
				    super.values.put(new Integer(limite.getIdtLimiteDinamico()), mapLimite);
				}
				
				mapLimite.put(new Integer(limite.hashCode()), limite);
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
	 *	Retorna uma lista com o historico de valores de limite dinamico.
	 *
	 *	@param		idtLimiteDinamico		Identificador de limite dinamico.
	 *	@return								Objeto PromocaoLimiteDinamico correspondente.
	 */
	public Collection getPromocaoLimiteDinamico(int idtLimiteDinamico)
	{
	    return this.getPromocaoLimiteDinamico(new Integer(idtLimiteDinamico));
	}

	/**
	 *	Retorna uma lista com o historico de valores de limite dinamico.
	 *
	 *	@param		idtLimiteDinamico		Identificador de limite dinamico.
	 *	@return								Objeto PromocaoLimiteDinamico correspondente.
	 */
	public Collection getPromocaoLimiteDinamico(Integer idtLimiteDinamico)
	{
	    ArrayList result = new ArrayList();

	    Map mapLimite = (Map)super.values.get(idtLimiteDinamico);
	    if(mapLimite != null)
	    {
	        for(Iterator iterator = mapLimite.values().iterator(); iterator.hasNext();)
	        {
	            result.add(((PromocaoLimiteDinamico)iterator.next()).clone());
	        }
	    }
	    
	    return result;
	}

	/**
	 *	Retorna um objeto PromocaoLimiteDinamico representando um registro da tabela.
	 *
	 *	@param		idtLimiteDinamico		Identificador de limite dinamico.
	 *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return								Objeto PromocaoLimiteDinamico correspondente.
	 */
	public PromocaoLimiteDinamico getPromocaoLimiteDinamico(int idtLimiteDinamico, Date datProcessamento)
	{
	    return this.getPromocaoLimiteDinamico(new Integer(idtLimiteDinamico), datProcessamento);
	}

	/**
	 *	Retorna um objeto PromocaoLimiteDinamico representando um registro da tabela.
	 *
	 *	@param		idtLimiteDinamico		Identificador de limite dinamico.
	 *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return								Objeto PromocaoLimiteDinamico correspondente.
	 */
	public PromocaoLimiteDinamico getPromocaoLimiteDinamico(Integer idtLimiteDinamico, Date datProcessamento)
	{
	    Map mapLimite = (Map)super.values.get(idtLimiteDinamico);
	    
	    if(mapLimite != null)
	    {
	        for(Iterator iterator = mapLimite.values().iterator(); iterator.hasNext();)
	        {
	            PromocaoLimiteDinamico limite = (PromocaoLimiteDinamico)iterator.next();
	            
	            if(limite.isVigente(datProcessamento))
	            {
	                return (PromocaoLimiteDinamico)limite.clone();
	            }
	        }
	    }
		
		return null;
	}

}
