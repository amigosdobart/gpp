package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *	Classe que representa a entidade da tabela TBL_CON_PERIODO_CONTABIL.
 * 
 *	@author	Magno Batista Correa, Bernardo Vergne Dias
 *	@since	2007/11/23 (yyyy/mm/dd)
 */
public class PeriodoContabil implements Entidade, Serializable
{
    private static final long serialVersionUID = 1937760704023840535L;
    
    private String	idtPeriodoContabil;
	private Date	datInicioPeriodo;
	private Date	datFinalPeriodo;
	private boolean	indFechado;
	
	public PeriodoContabil()
	{
		this.reset();
	}
	
	/**
	 *	Inicializa o objeto com valores vazios.
	 */
	public void reset()
	{
		this.idtPeriodoContabil = null;
		this.datInicioPeriodo   = null;
		this.datFinalPeriodo	= null;
		this.indFechado			= false;
	}
    
	/**
     * @return Fim do periodo contabil
	 */
	public Date getDatFinalPeriodo()
    {
        return datFinalPeriodo;
    }

    /**
     * @param datFinalPeriodo Fim do periodo contabil
     */
    public void setDatFinalPeriodo(Date datFinalPeriodo)
    {
        this.datFinalPeriodo = datFinalPeriodo;
    }

    /**
     * @return Inicio do periodo contabil
     */
    public Date getDatInicioPeriodo()
    {
        return datInicioPeriodo;
    }

    /**
     * @param datInicioPeriodo Inicio do periodo contabil
     */
    public void setDatInicioPeriodo(Date datInicioPeriodo)
    {
        this.datInicioPeriodo = datInicioPeriodo;
    }

    /**
     * @return Identificacao do periodo (mm/yyyy)
     */
    public String getIdtPeriodoContabil()
    {
        return idtPeriodoContabil;
    }

    /**
     * @param idtPeriodoContabil Identificacao do periodo (mm/yyyy)
     */
    public void setIdtPeriodoContabil(String idtPeriodoContabil)
    {
        this.idtPeriodoContabil = idtPeriodoContabil;
    }

    /**
     * @return true se o periodo esta fechado
     */
    public boolean isIndFechado()
    {
        return indFechado;
    }

    /**
     * @param indFechado true se o periodo esta fechado
     */
    public void setIndFechado(boolean indFechado)
    {
        this.indFechado = indFechado;
    }
    
    /**
     * Indica se uma dada data pertence ao periodo contabil
     * @param dataReferencia
     * @return
     */
    public boolean isVigente(Date dataReferencia)
    {
    	if (this.getDatFinalPeriodo() != null && this.getDatInicioPeriodo() != null)
            if (dataReferencia.getTime() >= this.getDatInicioPeriodo().getTime() &&
                    dataReferencia.getTime() <= this.getDatFinalPeriodo().getTime())
                return true;

    	return false;
    }
		
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PeriodoContabil result = new PeriodoContabil();	
		
        result.setDatFinalPeriodo((this.datFinalPeriodo != null) ? new Date(this.datFinalPeriodo.getTime()) : null);
        result.setDatInicioPeriodo((this.datInicioPeriodo != null) ? new Date(this.datInicioPeriodo.getTime()) : null);
        result.setIdtPeriodoContabil((this.idtPeriodoContabil != null) ? new String(this.idtPeriodoContabil) : null);
        result.setIndFechado(this.indFechado);
		
		return result;
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		Object					object						Objeto a ser comparado com o atual.
	 *	@return		boolean												True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof PeriodoContabil))
		{
			return false;
		}
		
		if(this.hashCode() != ((PeriodoContabil)object).hashCode())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		int													Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append((this.idtPeriodoContabil != null) ? this.idtPeriodoContabil : "NULL");

		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
		StringBuffer result = new StringBuffer();
	
		result.append("Periodo Contabil: ");
		result.append((this.idtPeriodoContabil != null) ? this.idtPeriodoContabil : "NULL");
		result.append(", Data Inicio: ");
		result.append((this.datInicioPeriodo != null) ? sdf.format(this.datInicioPeriodo) : "NULL");
        result.append(", Data Fim: ");
        result.append((this.datFinalPeriodo != null) ? sdf.format(this.datFinalPeriodo) : "NULL");
        result.append(", Ind Fechado: ");
        result.append("" + this.indFechado);
		
		return result.toString();
	}
	
}
