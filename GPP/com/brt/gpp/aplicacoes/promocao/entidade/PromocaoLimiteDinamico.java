package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_LIMITE_DINAMICO, que mapeia as regras dos calculos de limite 
 *	de bonus a ser concedido ao assinante pela promocao.
 * 
 *	@author	Daniel Ferreira
 *	@since	09/11/2005
 */
public class PromocaoLimiteDinamico implements Entidade
{

	//Constantes internas.
	
	/**
	 *	Constante para data inicial de vigencia.
	 */
	public static final int DAT_INI_PERIODO = 0;
	
	/**
	 *	Constante para data final de vigencia.
	 */
	public static final int DAT_FIM_PERIODO = 1;
	
	/**
	 *	Constante para threshold inferior.
	 */
	public static final int VLR_THRESHOLD_INFERIOR = 2;
	
	/**
	 *	Constante para threshold superior.
	 */
	public static final int VLR_THRESHOLD_SUPERIOR = 4;
	
	/**
	 *	Formatador de valor.
	 */
	private static final DecimalFormat conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, 
	                                                                       new DecimalFormatSymbols(new Locale("pt",
	                                                                                                           "BR")));
	
	/**
	 *	Formatador de data.
	 */
	private static final SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
	
	//Atributos.
	
	/**
	 *	Identificador do limite dinamico.
	 */
	private int idtLimiteDinamico;
	
	/**
	 *	Data inicial de vigencia.
	 */
	private Date datIniPeriodo;
	
	/**
	 *	Data final de vigencia.
	 */
	private Date datFimPeriodo;
	
	/**
	 *	Descricao do limite dinamico.
	 */
	private String desLimiteDinamico;
	
	/**
	 *	Threshold inferior. Valor minimo para ativacao do limite dinamico.
	 */
	private double vlrThresholdInferior;
	
	/**
	 *	Threshold superior. Valor maximo de atuacao do limite dinamico.
	 */
	private double vlrThresholdSuperior;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public PromocaoLimiteDinamico()
	{
		this.idtLimiteDinamico		= -1;
		this.datIniPeriodo			= null;
		this.datFimPeriodo			= null;
		this.desLimiteDinamico		= null;
		this.vlrThresholdInferior	= -1.0;
		this.vlrThresholdSuperior	= -1.0;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador do calculo do limite.
	 * 
	 *	@return								Identificador do calculo do limite.
	 */
	public int getIdtLimiteDinamico() 
	{
		return this.idtLimiteDinamico;
	}
	
	/**
	 *	Retorna a data inicial de vigencia.
	 * 
	 *	@return								Data inicial de vigencia.
	 */
	public Date getDatIniPeriodo() 
	{
		return this.datIniPeriodo;
	}
	
	/**
	 *	Retorna a data final de vigencia.
	 * 
	 *	@return								Data final de vigencia.
	 */
	public Date getDatFimPeriodo() 
	{
		return this.datFimPeriodo;
	}
	
	/**
	 *	Retorna a descricao do calculo do limite.
	 * 
	 *	@return								Descricao do calculo do limite.
	 */
	public String getDesLimiteDinamico()
	{
		return this.desLimiteDinamico;
	}
	
	/**
	 *	Retorna o valor de referencia inferior para o calculo do limite.
	 * 
	 *	@return								Valor de referencia inferior.
	 */
	public double getVlrThresholdInferior()
	{
		return this.vlrThresholdInferior;
	}
	
	/**
	 *	Retorna o valor de referencia superior para o calculo do limite.
	 * 
	 *	@return								Valor de referencia superior.
	 */
	public double getVlrThresholdSuperior()
	{
		return this.vlrThresholdSuperior;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador do calculo do limite.
	 * 
	 *	@param		idtLimiteDinamico		Identificador do calculo do limite.
	 */
	public void setIdtLimiteDinamico(int idtLimiteDinamico) 
	{
		this.idtLimiteDinamico = idtLimiteDinamico;
	}
	
	/**
	 *	Atribui a data inicial de vigencia.
	 * 
	 *	@param		datIniPeriodo			Data inicial de vigencia.
	 */
	public void setDatIniPeriodo(Date datIniPeriodo) 
	{
		this.datIniPeriodo = datIniPeriodo;
	}
	
	/**
	 *	Atribui a data final de vigencia.
	 * 
	 *	@param		datFimPeriodo			Data final de vigencia.
	 */
	public void setDatFimPeriodo(Date datFimPeriodo) 
	{
		this.datFimPeriodo = datFimPeriodo;
	}
	
	/**
	 *	Atribui a descricao do calculo do limite.
	 * 
	 *	@param		desLimiteDinamico		Descricao do calculo do limite.
	 */
	public void setDesLimiteDinamico(String desLimiteDinamico)
	{
		this.desLimiteDinamico = desLimiteDinamico;
	}
	
	/**
	 *	Atribui o valor de referencia inferior para o calculo do limite.
	 * 
	 *	@param		vlrThresholdInferior	Valor de referencia inferior.
	 */
	public void setVlrThresholdInferior(double vlrThresholdInferior)
	{
		this.vlrThresholdInferior = vlrThresholdInferior;
	}
	
	/**
	 *	Atribui o valor de referencia supderior para o calculo do limite.
	 * 
	 *	@param		vlrThresholdSuperior	Valor de referencia superior.
	 */
	public void setVlrThresholdSuperior(double vlrThresholdSuperior)
	{
		this.vlrThresholdSuperior = vlrThresholdSuperior;
	}
	
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoLimiteDinamico result = new PromocaoLimiteDinamico();		
		
		result.setIdtLimiteDinamico(this.idtLimiteDinamico);
		result.setDatIniPeriodo((this.datIniPeriodo != null) ? new Date(this.datIniPeriodo.getTime()) : null);
		result.setDatFimPeriodo((this.datFimPeriodo != null) ? new Date(this.datFimPeriodo.getTime()) : null);
		result.setDesLimiteDinamico((this.desLimiteDinamico != null) ? new String(this.desLimiteDinamico) : null);
		result.setVlrThresholdInferior(this.vlrThresholdInferior);
		result.setVlrThresholdSuperior(this.vlrThresholdSuperior);
		
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
		
		if(!this.getClass().isInstance(object))
		{
			return false;
		}
		
		if(this.hashCode() != object.hashCode())
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
		result.append(String.valueOf(this.idtLimiteDinamico));
		result.append("||");
		result.append(this.format(PromocaoLimiteDinamico.DAT_INI_PERIODO));
		
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
	
		result.append("Descricao: ");
		result.append((this.desLimiteDinamico != null) ? this.desLimiteDinamico : "NULL");
		result.append(" - ");
		result.append("Inicio: ");
		result.append(this.format(PromocaoLimiteDinamico.DAT_INI_PERIODO));
		result.append(" - ");
		result.append("Fim: ");
		result.append(this.format(PromocaoLimiteDinamico.DAT_FIM_PERIODO));
		
		return result.toString();
	}
	
	//Outros metodos.
	
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
	    	case PromocaoLimiteDinamico.DAT_INI_PERIODO:
	    	    return (this.datIniPeriodo != null) ? PromocaoLimiteDinamico.conversorDate.format(this.datIniPeriodo) : null;
	    	    
	    	case PromocaoLimiteDinamico.DAT_FIM_PERIODO:
	    	    return (this.datFimPeriodo != null) ? PromocaoLimiteDinamico.conversorDate.format(this.datFimPeriodo) : null;
	    	    
	    	case PromocaoLimiteDinamico.VLR_THRESHOLD_INFERIOR:
	    	    return PromocaoLimiteDinamico.conversorDouble.format(this.vlrThresholdInferior);
	    	    
	    	case PromocaoLimiteDinamico.VLR_THRESHOLD_SUPERIOR:
	    	    return PromocaoLimiteDinamico.conversorDouble.format(this.vlrThresholdSuperior);
	    	    
	    	default: return null;
	    }
    }
	
	/**
	 *	Indica se o registro esta vigente ou nao.
	 * 
	 *	@return								True se esta vigente e false caso contrario.
	 */
    public boolean isVigente(Date datProcessamento)
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
    
}
