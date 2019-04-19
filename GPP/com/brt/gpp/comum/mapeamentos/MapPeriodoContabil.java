package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.PeriodoContabil;

/**
 *	Mapeamento dos periodos contabeis 
 *
*	@author	Magno Batista Correa, Bernardo Vergne Dias
 *	@since	2007/11/23 (yyyy/mm/dd)
 */
public class MapPeriodoContabil extends Mapeamento 
{
	/**
	 *	Instancia do singleton. 
	 */
	private static MapPeriodoContabil instance;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException 
	 */
	private MapPeriodoContabil() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton. 
	 */
	public static MapPeriodoContabil getInstance()
	{
		try
		{
			if(MapPeriodoContabil.instance == null)
				MapPeriodoContabil.instance = new MapPeriodoContabil();
			
			return MapPeriodoContabil.instance;
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
				" SELECT IDT_PERIODO_CONTABIL,                     " +
				"        DAT_INICIO_PERIODO,                       " +
				"        DAT_FINAL_PERIODO,                        " +
				"        IND_FECHADO                               " +
				"   FROM TBL_CON_PERIODO_CONTABIL                  " +
				" ORDER BY TO_DATE(IDT_PERIODO_CONTABIL,'MM/YYYY') " ;

			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				String	idtPeriodoContabil  = registros.getString("IDT_PERIODO_CONTABIL");
				Date	datInicioPeriodo    = registros.getDate  ("DAT_INICIO_PERIODO");
				Date	datFinalPeriodo     = registros.getDate  ("DAT_FINAL_PERIODO");
				boolean	indFechado          = registros.getBoolean("IND_FECHADO");
                
                PeriodoContabil periodo = new PeriodoContabil();
                periodo.setIdtPeriodoContabil(idtPeriodoContabil);
                periodo.setDatInicioPeriodo(datInicioPeriodo);
                periodo.setDatFinalPeriodo(datFinalPeriodo);
                periodo.setIndFechado(indFechado);
                
                super.values.put(idtPeriodoContabil, periodo);
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
     *  Retorna um PeriodoContabil de acordo com o ID (mm/yyyy).
     *
     *  @param  idtPeriodoContabil Identificacao do periodo (mm/yyyy)
     *  @return Instancia de <code>PeriodoContabil</code>
     */
    public PeriodoContabil getPeriodoContabil(String idtPeriodoContabil)
    {
        return (PeriodoContabil)super.values.get(idtPeriodoContabil);
    }
    
    /**
     * Retorna um PeriodoContabil cujo periodo envolve a data especificada.
     * 
     * @param dataReferencia Data de referencia (sem hora, minuto e segundos)
     * @return Instancia de <code>PeriodoContabil</code>
     */
    public PeriodoContabil getPeriodoContabil(Date dataReferencia)
    {
        for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
        {
            PeriodoContabil periodo = (PeriodoContabil)iterator.next();
            if (periodo.isVigente(dataReferencia))
            	return periodo;
        }
        
        return null;
    }
    
    /**
     * Retorna true se o periodo contabil esta fechado.
     * 
     * @param  idtPeriodoContabil Identificacao do periodo (mm/yyyy)
     * @return true se o periodo esta fechado
     * @throws Exception se o periodo contabil nao existir
     */
    public boolean isPeriodoFechado(String idtPeriodoContabil) throws Exception
    {
        PeriodoContabil periodo = getPeriodoContabil(idtPeriodoContabil);
        
        if (periodo == null)
            throw new IllegalArgumentException("Erro ao executar MapPeriodoContabil.isPeriodoFechado(String). " +
                    "Nao existe periodo para o idtPeriodoContabil informado.");
        
        return periodo.isIndFechado();       
    }
    
    /**
     * Retorna true se o periodo contabil esta fechado.<br>
     * 
     * O periodo considerado eh aquele cujo periodo envolve a data especificada.
     * 
     * @param dataReferencia Data de referencia (sem hora, minuto e segundos)
     * @return true se o periodo esta fechado
     * @throws Exception se o periodo contabil nao existir
     */
    public boolean isPeriodoFechado(Date dataReferencia) throws Exception
    {
        PeriodoContabil periodo = getPeriodoContabil(dataReferencia);
        
        if (periodo == null)
            throw new IllegalArgumentException("Erro ao executar MapPeriodoContabil.isPeriodoFechado(Date). " +
                    "Nao existe periodo para a data informada.");
        
        return periodo.isIndFechado();       
    }
    
    /**
     * Retorna uma lista de todas as datas do periodo contabil informado.
     * 
     * @param  idtPeriodoContabil Identificacao do periodo (mm/yyyy)
     * @return Lista de datas
     * @throws Exception se o periodo contabil nao existir
     */
    public List getListaDatas(String idtPeriodoContabil) throws Exception
    {
        PeriodoContabil periodo = getPeriodoContabil(idtPeriodoContabil);

        if (periodo == null)
            throw new IllegalArgumentException("Erro ao executar MapPeriodoContabil.getListaDatas(String). " +
                    "Nao existe periodo para a data informada.");

        return getListaDatasAteReferencia(periodo.getDatFinalPeriodo());       
    }
    
    /**
     * Retorna uma lista de todas as datas do periodo contabil informado.<br>
     * 
     * O periodo considerado eh aquele cujo periodo envolve a data especificada.
     * 
     * @param dataReferencia Data de referencia (sem hora, minuto e segundos)
     * @return Lista de datas
     * @throws Exception se o periodo contabil nao existir
     */
    public List getListaDatas(Date dataReferencia) throws Exception
    {
        PeriodoContabil periodo = getPeriodoContabil(dataReferencia);
        
        if (periodo == null)
            throw new IllegalArgumentException("Erro ao executar MapPeriodoContabil.getListaDatas(Date). " +
                    "Nao existe periodo para a data informada.");
        
        return getListaDatas(periodo.getIdtPeriodoContabil());       
    }

    /**
     * Retorna todos os dias de um dado periodo contabil ateh a data de referencia.
     * @param dataReferencia
     * @return
     * @throws Exception
     */
    public List getListaDatasAteReferencia(Date dataReferencia) throws Exception
    {
    	PeriodoContabil periodo = getPeriodoContabil(dataReferencia);

    	if (periodo == null)
            throw new IllegalArgumentException("Erro ao executar MapPeriodoContabil.getListaDatasAteReferencia(Date). " +
                    "Nao existe periodo para o idtPeriodoContabil informado.");
        
        List list = new ArrayList();
        
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(periodo.getDatInicioPeriodo());
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);
        
        while (inicio.getTime().compareTo(dataReferencia) <= 0)
        {
            list.add(inicio.getTime());
            inicio.add(Calendar.DAY_OF_MONTH, 1);
        }
                
        return list;       
    }
}
