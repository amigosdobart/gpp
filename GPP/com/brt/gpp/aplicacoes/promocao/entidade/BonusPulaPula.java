package com.brt.gpp.aplicacoes.promocao.entidade;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_BONUS_PULA_PULA. <br>
 *  Essa tabela mapeia o valor de bonus por minuto a ser concedido para assinantes de cada Codigo Nacional.
 * 
 *	@author	Daniel Ferreira
 *	@since	09/08/2005
 *
 *  Atualizado por: Bernardo Vergne Dias
 *  Data: 15/02/2007, 06/11/2007
 */
public class BonusPulaPula implements Entidade, Serializable
{
    private static final long serialVersionUID = 1285414799576995745L;
    
    private CodigoNacional	codigoNacional;
	private PlanoPreco		planoPreco;
	private Date			datIniPeriodo;
	private Date			datFimPeriodo;
	private	Double			vlrBonusMinuto;
	private	Double			vlrBonusMinutoFF;
	private Double			vlrBonusMinutoNoturno;
	private Double			vlrBonusMinutoDiurno;
	private	Double			vlrBonusMinutoATH;
    private Double          vlrBonusMinutoCT;
	
	private static	DecimalFormat		conversorDouble;
	private static	SimpleDateFormat	conversorDate;
		
	public static final int DAT_INI_PERIODO				= 0;
	public static final int DAT_FIM_PERIODO				= 1;
	public static final int VLR_BONUS_MINUTO			= 2;
	public static final int VLR_BONUS_MINUTO_FF			= 3;
	public static final int VLR_BONUS_MINUTO_NOTURNO	= 4;
	public static final int VLR_BONUS_MINUTO_DIURNO		= 5;
	public static final int VLR_BONUS_MINUTO_ATH		= 6;
    public static final int VLR_BONUS_MINUTO_CT         = 7;
		
	/**
	 * Construtor da classe
	 */
	public BonusPulaPula()
	{
		this.codigoNacional			= null;
		this.planoPreco				= null;
		this.datIniPeriodo			= null;
		this.datFimPeriodo			= null;
		this.vlrBonusMinuto			= null;
		this.vlrBonusMinutoFF		= null;
		this.vlrBonusMinutoNoturno	= null;
		this.vlrBonusMinutoDiurno	= null;
		this.vlrBonusMinutoATH		= null;
        this.vlrBonusMinutoCT       = null;
		
		BonusPulaPula.conversorDouble	= new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
		BonusPulaPula.conversorDate		= new SimpleDateFormat(Definicoes.MASCARA_DATE);
	}
		
	/**
	 *	Retorna o Codigo Nacional do assinante.
	 * 
	 *	@return		Integer					idtCodigoNacional			Codigo Nacional do assinante.
	 */
	public Integer getIdtCodigoNacional() 
	{
		return (this.codigoNacional != null) ? this.codigoNacional.getIdtCodigoNacional() : null;
	}
	
	/**
	 *	Retorna o plano de preco do assinante.
	 * 
	 *	@return		Integer					idtPlanoPreco				Plano de preco do assinante.
	 */
	public Integer getIdtPlanoPreco() 
	{
		return (this.planoPreco != null) ? new Integer(this.planoPreco.getIdtPlanoPreco()) : null;
	}
	
	/**
	 *	Retorna a data inicial de vigencia.
	 * 
	 *	@return		Date					datIniPeriodo				Data inicial de vigencia.
	 */
	public Date getDatIniPeriodo() 
	{
		return this.datIniPeriodo;
	}
	
	/**
	 *	Retorna a data final de vigencia.
	 * 
	 *	@return		Date					datFimPeriodo				Data final de vigencia.
	 */
	public Date getDatFimPeriodo() 
	{
		return this.datFimPeriodo;
	}
	
	/**
	 *	Retorna o valor de bonus por minuto para ligacoes sem tarifacao diferenciada.
	 * 
	 *	@return		Double					vlrBonusMinuto				Bonus por minuto em ligacoes sem tarifacao diferenciada.
	 */
	public Double getVlrBonusMinuto()
	{
		return this.vlrBonusMinuto;
	}
	
	/**
	 *	Retorna o valor de bonus por minuto para ligacoes com tarifacao diferenciada Friends and Family.
	 * 
	 *	@return		Double					vlrBonusMinutoFF			Bonus por minuto em ligacoes com tarifacao diferenciada Friends and Family.
	 */
	public Double getVlrBonusMinutoFF()
	{
		return this.vlrBonusMinutoFF;
	}
	
	/**
	 *	Retorna o valor de bonus por minuto para ligacoes com tarifacao diferenciada Noturna.
	 * 
	 *	@return		Double					vlrBonusMinutoNoturno		Bonus por minuto em ligacoes com tarifacao diferenciada Noturna.
	 */
	public Double getVlrBonusMinutoNoturno()
	{
		return this.vlrBonusMinutoNoturno;
	}
	
	/**
	 *	Retorna o valor de bonus por minuto para ligacoes com tarifacao diferenciada Diurna.
	 * 
	 *	@return		Double					vlrBonusMinutoDiurno		Bonus por minuto em ligacoes com tarifacao diferenciada Diurna.
	 */
	public Double getVlrBonusMinutoDiurno()
	{
		return this.vlrBonusMinutoDiurno;
	}
	
	/**
	 *	Retorna o valor de bonus por minuto para ligacoes com tarifacao diferenciada Novo Friends and Family.
	 * 
	 *	@return		Double					vlrBonusMinutoATH			Bonus por minuto em ligacoes com tarifacao diferenciada Novo Friends and Family.
	 */
	public Double getVlrBonusMinutoATH()
	{
		return this.vlrBonusMinutoATH;
	}
    
    /**
     *  Retorna o valor de bonus por minuto para ligacoes com tarifacao diferenciada Controle Total.
     * 
     *  @return     Double                  vlrBonusMinutoCT            Bonus por minuto em ligacoes com tarifacao diferenciada Controle Total.
     */
    public Double getVlrBonusMinutoCT()
    {
        return this.vlrBonusMinutoCT;
    }
	
	/**
	 *	Atribui o Codigo Nacional do assinante.
	 * 
	 *	@param		Integer					idtCodigoNacional			Codigo Nacional do assinante.
	 */
	public void setIdtCodigoNacional(Integer idtCodigoNacional) 
	{
		this.codigoNacional = new CodigoNacional();
		this.codigoNacional.setIdtCodigoNacional(idtCodigoNacional);
	}
		
	/**
	 *	Atribui o plano de preco do assinante.
	 * 
	 *	@param		Integer					idtPlanoPreco				Plano de preco do assinante.
	 */
	public void setIdtPlanoPreco(Integer idtPlanoPreco) 
	{
		this.planoPreco = new PlanoPreco();
		this.planoPreco.setIdtPlanoPreco(idtPlanoPreco.toString());
	}
	
	/**
	 *	Atribui a data inicial de vigencia.
	 * 
	 *	@param		Date					datIniPeriodo				Data inicial de vigencia.
	 */
	public void setDatIniPeriodo(Date datIniPeriodo) 
	{
		this.datIniPeriodo = datIniPeriodo;
	}
	
	/**
	 *	Atribui a data final de vigencia.
	 * 
	 *	@param		Date					datFimPeriodo				Data final de vigencia.
	 */
	public void setDatFimPeriodo(Date datFimPeriodo) 
	{
		this.datFimPeriodo = datFimPeriodo;
	}
	
	/**
	 *	Atribui o valor de bonus por minuto para ligacoes sem tarifacao diferenciada.
	 * 
	 *	@param		Double					vlrBonusMinuto				Bonus por minuto em ligacoes sem tarifacao diferenciada.
	 */
	public void setVlrBonusMinuto(Double vlrBonusMinuto)
	{
		this.vlrBonusMinuto = vlrBonusMinuto;
	}
	
	/**
	 *	Atribui o valor de bonus por minuto para ligacoes com tarifacao diferenciada Friends and Family.
	 * 
	 *	@param		Double					vlrBonusMinutoFF			Bonus por minuto em ligacoes com tarifacao diferenciada Friends and Family.
	 */
	public void setVlrBonusMinutoFF(Double vlrBonusMinutoFF)
	{
		this.vlrBonusMinutoFF = vlrBonusMinutoFF;
	}
	
	/**
	 *	Atribui o valor de bonus por minuto para ligacoes com tarifacao diferenciada Noturna.
	 * 
	 *	@param		Double					vlrBonusMinutoNoturno		Bonus por minuto em ligacoes com tarifacao diferenciada Noturna.
	 */
	public void setVlrBonusMinutoNoturno(Double vlrBonusMinutoNoturno)
	{
		this.vlrBonusMinutoNoturno = vlrBonusMinutoNoturno;
	}
	
	/**
	 *	Atribui o valor de bonus por minuto para ligacoes com tarifacao diferenciada Diurna.
	 * 
	 *	@param		Double					vlrBonusMinutoDiurno		Bonus por minuto em ligacoes com tarifacao diferenciada Diurna.
	 */
	public void setVlrBonusMinutoDiurno(Double vlrBonusMinutoDiurno)
	{
		this.vlrBonusMinutoDiurno = vlrBonusMinutoDiurno;
	}
	
	/**
	 *	Atribui o valor de bonus por minuto para ligacoes com tarifacao diferenciada Novo Friends and Family.
	 * 
	 *	@param		Double					vlrBonusMinutoATH			Bonus por minuto em ligacoes com tarifacao diferenciada Novo Friends and Family.
	 */
	public void setVlrBonusMinutoATH(Double vlrBonusMinutoATH)
	{
		this.vlrBonusMinutoATH = vlrBonusMinutoATH;
	}
	
    /**
     *  Atribui o valor de bonus por minuto para ligacoes com tarifacao diferenciada Controle Total.
     * 
     *  @param      Double                  vlrBonusMinutoCT           Bonus por minuto em ligacoes com tarifacao diferenciada Controle Total.
     */
    public void setVlrBonusMinutoCT(Double vlrBonusMinutoCT)
    {
        this.vlrBonusMinutoCT = vlrBonusMinutoCT;
    }
    
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		BonusPulaPula result = new BonusPulaPula();		
		
		result.setDatIniPeriodo((this.datIniPeriodo != null) ? new Date(this.datIniPeriodo.getTime()) : null);
		result.setDatFimPeriodo((this.datFimPeriodo != null) ? new Date(this.datFimPeriodo.getTime()) : null);
		result.setVlrBonusMinuto((this.vlrBonusMinuto != null) ? new Double(this.vlrBonusMinuto.doubleValue()) : null);
		result.setVlrBonusMinutoFF((this.vlrBonusMinutoFF != null) ? new Double(this.vlrBonusMinutoFF.doubleValue()) : null);
		result.setVlrBonusMinutoNoturno((this.vlrBonusMinutoNoturno != null) ? new Double(this.vlrBonusMinutoNoturno.doubleValue()) : null);
		result.setVlrBonusMinutoDiurno((this.vlrBonusMinutoDiurno != null) ? new Double(this.vlrBonusMinutoDiurno.doubleValue()) : null);
		result.setVlrBonusMinutoATH((this.vlrBonusMinutoATH != null) ? new Double(this.vlrBonusMinutoATH.doubleValue()) : null);
        result.setVlrBonusMinutoCT((this.vlrBonusMinutoCT != null) ? new Double(this.vlrBonusMinutoCT.doubleValue()) : null);
		
		if (this.codigoNacional != null)
		{
			result.codigoNacional = (CodigoNacional)(this.codigoNacional.clone());
		}
		
		if (this.planoPreco != null)
		{
			result.planoPreco = (PlanoPreco)(this.planoPreco.clone());
		}

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
		
		if(!(object instanceof BonusPulaPula))
		{
			return false;
		}
		
		if(this.hashCode() != ((BonusPulaPula)object).hashCode())
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
		result.append((this.codigoNacional != null) ? String.valueOf(this.codigoNacional.getIdtCodigoNacional().intValue()) : "NULL");
		result.append("||");
		result.append((this.planoPreco != null) ? this.planoPreco.getIdtPlanoPreco() : "NULL");
		result.append("||");
		result.append((this.datIniPeriodo != null) ? BonusPulaPula.conversorDate.format(this.datIniPeriodo) : "NULL");
		result.append("||");
		result.append((this.datFimPeriodo != null) ? BonusPulaPula.conversorDate.format(this.datFimPeriodo) : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("Codigo Nacional: ");
		result.append((this.codigoNacional != null) ? String.valueOf(this.codigoNacional.getIdtCodigoNacional().intValue()) : "NULL");
		result.append(" - ");
		result.append("Plano de Preco: ");
		result.append((this.planoPreco != null) ? this.planoPreco.getIdtPlanoPreco() : "NULL");
		result.append(" - ");
		result.append("Data inicial: ");
		result.append((this.datIniPeriodo != null) ? BonusPulaPula.conversorDate.format(this.datIniPeriodo) : "NULL");
		result.append(" - ");
		result.append("Data Final: ");
		result.append((this.datFimPeriodo != null) ? BonusPulaPula.conversorDate.format(this.datFimPeriodo) : "NULL");
		result.append(" - ");
		result.append("Valor de Bonus: ");
		result.append((this.vlrBonusMinuto != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinuto) : "NULL");
		
		return result.toString();
	}
		
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		int						campo						Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		String												Valor no formato String.
	 */
    public String format(int campo)
    {
	    switch(campo)
	    {
	    	case BonusPulaPula.DAT_INI_PERIODO:
	    	{
	    	    return (this.datIniPeriodo != null) ? BonusPulaPula.conversorDate.format(this.datIniPeriodo) : null;
	    	}
	    	case BonusPulaPula.DAT_FIM_PERIODO:
	    	{
	    	    return (this.datFimPeriodo != null) ? BonusPulaPula.conversorDate.format(this.datFimPeriodo) : null;
	    	}
	    	case BonusPulaPula.VLR_BONUS_MINUTO:
	    	{
	    	    return (this.vlrBonusMinuto != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinuto) : null;
	    	}
	    	case BonusPulaPula.VLR_BONUS_MINUTO_FF:
	    	{
	    	    return (this.vlrBonusMinutoFF != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinutoFF) : null;
	    	}
	    	case BonusPulaPula.VLR_BONUS_MINUTO_NOTURNO:
	    	{
	    	    return (this.vlrBonusMinutoNoturno != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinutoNoturno) : null;
	    	}
	    	case BonusPulaPula.VLR_BONUS_MINUTO_DIURNO:
	    	{
	    	    return (this.vlrBonusMinutoDiurno != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinutoDiurno) : null;
	    	}
	    	case BonusPulaPula.VLR_BONUS_MINUTO_ATH:
	    	{
	    	    return (this.vlrBonusMinutoATH != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinutoATH) : null;
	    	}
            case BonusPulaPula.VLR_BONUS_MINUTO_CT:
            {
                return (this.vlrBonusMinutoCT != null) ? BonusPulaPula.conversorDouble.format(this.vlrBonusMinutoCT) : null;
            }
	    	default: return null;
	    }
    }
	
	/**
	 *	Indica se o registro esta vigente ou nao.
	 * 
	 *	@return		boolean												True se esta vigente e false caso contrario.
	 */
    public boolean vigente(Date datProcessamento)
    {
        if(datProcessamento == null)
        {
            return false;
        }
        
        if(this.datIniPeriodo == null)
        {
            return false;
        }
        
        if(this.datIniPeriodo.compareTo(datProcessamento) > 0)
        {
            return false;
        }
        
        if(this.datFimPeriodo == null)
        {
            return true;
        }
        
        return (this.datFimPeriodo.compareTo(datProcessamento) >= 0);
    }

	/**
	 * @return Instancia de <code>CodigoNacional</code>
	 */
	public CodigoNacional getCodigoNacional() 
	{
		return codigoNacional;
	}

	/**
	 * @param codigoNacional Instancia de <code>CodigoNacional</code>
	 */
	public void setCodigoNacional(CodigoNacional codigoNacional) 
	{
		this.codigoNacional = codigoNacional;
	}

	/**
	 * @return Instancia de <code>PlanoPreco</code>
	 */
	public PlanoPreco getPlanoPreco() 
	{
		return planoPreco;
	}

	/**
	 * @param planoPreco Instancia de <code>PlanoPreco</code>
	 */
	public void setPlanoPreco(PlanoPreco planoPreco) 
	{
		this.planoPreco = planoPreco;
	}
    
}
