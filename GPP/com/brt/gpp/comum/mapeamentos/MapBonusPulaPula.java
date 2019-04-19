package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Map;

import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
  *
  * Este arquivo contem a classe que faz o mapeamento dos valores de Bonus Pula-Pula
  * existentes no BD Oracle em memoria no GPP.
  * <P> Version:		1.0
  *
  * @Autor: 		Daniel Ferreira
  * Data: 			27/04/2005
  * 
  * Atualizado por: Bernardo Vergne Dias
  *  Data: 15/02/2007, 06/11/2007
  */
public final class MapBonusPulaPula extends Mapeamento
{

	private static MapBonusPulaPula instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapBonusPulaPula() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instacia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapBonusPulaPula getInstancia() 
	{
		if(MapBonusPulaPula.instance == null)
		{
		    try
		    {
		    	MapBonusPulaPula.instance = new MapBonusPulaPula();
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
		}
			
		return MapBonusPulaPula.instance;
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

			String sql = "SELECT " +
			 			 "  IDT_CODIGO_NACIONAL, " +
			 			 "	IDT_PLANO_PRECO, " +
			 			 "	DAT_INI_PERIODO, " +
			 			 "	DAT_FIM_PERIODO, " +
			 			 "  VLR_BONUS_MINUTO, " +
			 			 "  VLR_BONUS_MINUTO_FF, " +
			 			 "  VLR_BONUS_MINUTO_NOTURNO, " +
			 			 "  VLR_BONUS_MINUTO_DIURNO, " +
			 			 "  VLR_BONUS_MINUTO_ATH,  " +
                        "   VLR_BONUS_MINUTO_CT  " +
			 			 "FROM " +
			 			 "  TBL_PRO_BONUS_PULA_PULA ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sql, null,0);
			
			while(registros.next())
			{
				
				BonusPulaPula bonus = new BonusPulaPula();
				
				Integer idtCodigoNacional = new Integer(registros.getInt("IDT_CODIGO_NACIONAL"));
				bonus.setIdtCodigoNacional(idtCodigoNacional);
				
				Integer idtPlanoPreco = new Integer(registros.getInt("IDT_PLANO_PRECO"));
				bonus.setIdtPlanoPreco(idtPlanoPreco);
				
				Date datIniPeriodo = registros.getDate("DAT_INI_PERIODO");
				bonus.setDatIniPeriodo(datIniPeriodo);
				
				Date datFimPeriodo = registros.getDate("DAT_FIM_PERIODO");
				bonus.setDatFimPeriodo(datFimPeriodo);
				
				double vlrBonusMinuto = registros.getDouble("VLR_BONUS_MINUTO");
				bonus.setVlrBonusMinuto(registros.wasNull() ? null : new Double(vlrBonusMinuto));
				
				double vlrBonusMinutoFF = registros.getDouble("VLR_BONUS_MINUTO_FF");
				bonus.setVlrBonusMinutoFF(registros.wasNull() ? null : new Double(vlrBonusMinutoFF));
				
				double vlrBonusMinutoNoturno = registros.getDouble("VLR_BONUS_MINUTO_NOTURNO");
				bonus.setVlrBonusMinutoNoturno(registros.wasNull() ? null : new Double(vlrBonusMinutoNoturno));
				
				double vlrBonusMinutoDiurno = registros.getDouble("VLR_BONUS_MINUTO_DIURNO");
				bonus.setVlrBonusMinutoDiurno(registros.wasNull() ? null : new Double(vlrBonusMinutoDiurno));
				
				double vlrBonusMinutoATH = registros.getDouble("VLR_BONUS_MINUTO_ATH");
				bonus.setVlrBonusMinutoATH(registros.wasNull() ? null : new Double(vlrBonusMinutoATH));
                
                double vlrBonusMinutoCT = registros.getDouble("VLR_BONUS_MINUTO_CT");
                bonus.setVlrBonusMinutoCT(registros.wasNull() ? null : new Double(vlrBonusMinutoCT));
				
				Map mapCn = (Map)super.values.get(idtCodigoNacional);
				if(mapCn == null)
				{
				    mapCn = Collections.synchronizedMap(new HashMap());
				    super.values.put(idtCodigoNacional, mapCn);
				}
				
				Map mapPlano = (Map)mapCn.get(idtPlanoPreco);
				if(mapPlano == null)
				{
				    mapPlano = Collections.synchronizedMap(new HashMap());
				    mapCn.put(idtPlanoPreco, mapPlano);
				}
				
				mapPlano.put(new Integer(bonus.hashCode()), bonus);
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
	 *	Retorna o valor de bonus Pula-Pula.
	 *
	 *	@param		int						idtCodigoNacional			Codigo Nacional do assinante.
	 *	@param		int						idtPlanoPreco				Plano de Preco do assinante.
	 *	@param		Date					datProcessamento			Data de processamento da operacao.
	 *	@return		BonusPulaPula										Objeto Bonus Pula-Pula correspondente.
	 */
	public BonusPulaPula getBonusPulaPula(int idtCodigoNacional, int idtPlanoPreco, Date datProcessamento)
	{
		return this.getBonusPulaPula(new Integer(idtCodigoNacional), new Integer(idtPlanoPreco), datProcessamento);
	}

	/**
	 *	Retorna o valor de bonus Pula-Pula.
	 *
	 *	@param		Integer					idtCodigoNacional			Codigo Nacional do assinante.
	 *	@param		Integer					idtPlanoPreco				Plano de Preco do assinante.
	 *	@param		Date					datProcessamento			Data de processamento da operacao.
	 *	@return		BonusPulaPula										Objeto Bonus Pula-Pula correspondente.
	 */
	public BonusPulaPula getBonusPulaPula(Integer idtCodigoNacional, Integer idtPlanoPreco, Date datProcessamento)
	{
	    Map mapCn = (Map)super.values.get(idtCodigoNacional);
	    
	    if(mapCn != null)
	    {
	        Map mapPlano = (Map)mapCn.get(idtPlanoPreco);
	        
	        if(mapPlano != null)
	        {
	            Collection listaBonus = mapPlano.values();
	            
	            for(Iterator iterator = listaBonus.iterator(); iterator.hasNext();)
	            {
	                BonusPulaPula bonus = (BonusPulaPula)iterator.next();
	                
	                if(bonus.vigente(datProcessamento))
	                {
	                    return (BonusPulaPula)bonus.clone();
	                }
	            }
	        }
	    }
		
		return null;
	}

}
