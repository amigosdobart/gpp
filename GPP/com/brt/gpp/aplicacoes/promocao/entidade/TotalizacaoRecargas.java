package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_TOTALIZACAO_RECARGAS.
 *
 *	@author	Daniel Ferreira
 *	@since	28/09/2006
 */
public class TotalizacaoRecargas implements Entidade, Totalizado
{
	
	//Constantes.
	
	/**
	 *	Constante referente a quantidade de recargas efetuadas.
	 */
	public static final int QTD_RECARGAS = 0; 
	
	/**
	 *	Constante referente ao valor total pago pelas recargas.
	 */
	public static final int VLR_PAGO = 1; 
	
	//Atributos.
	
	/**
	 *	Mes de analise no format YYYYMM.
	 */
	private	String				datMes;
	
	/**
	 *	MSISDN do assinante.
	 */
	private	String				idtMsisdn;

	/**
	 *	Quantidade de recargas efetuadas.
	 */
	private int					qtdRecargas;

	/**
	 *	Valor total pago pelas recargas.
	 */
	private double				vlrPago;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public TotalizacaoRecargas()
	{
		this.reset();
	}
	
	//Getters.
	
	/**
	 *	Retorna o MSISDN do assinante.	
	 *
	 *	@return		MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o mes de analise em formato YYYYMM.
	 * 
	 *	@return		Mes de analise.
	 */
	public String getDatMes() 
	{
		return this.datMes;
	}
	
	/**
	 *	Retorna a quantidade de recargas efetuadas.
	 * 
	 *	@return		Quantidade de recargas efetuadas.
	 */
	public int getQtdRecargas() 
	{
		return this.qtdRecargas;
	}
	
	/**
	 *	Retorna o valor total pago pelas recargas.
	 *
	 *	@return		Valor total pago pelas recargas.
	 */
	public double getVlrPago() 
	{
		return this.vlrPago;
	}

	//Setters.
	
	/**
	 *	Atribui o MSISDN do assinante.	
	 *
	 *	@param		idtMsisdn				MSISDN do assinante.
	 */
	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	/**
	 *	Atribui o mes de analise em formato YYYYMM.
	 * 
	 *	@param		datMes					Mes de analise.
	 */
	public void setDatMes(String datMes) 
	{
		this.datMes = datMes;
	}
	
	/**
	 *	Atribui a quantidade de recargas efetuadas.
	 * 
	 *	@param		qtdRecargas				Quantidade de recargas efetuadas.
	 */
	public void setQtdRecargas(int qtdRecargas) 
	{
		this.qtdRecargas = qtdRecargas;
	}
	
	/**
	 *	Atribui o valor total pago pelas recargas.
	 *
	 *	@param		vlrPago					Valor total pago pelas recargas.
	 */
	public void setVlrPago(double vlrPago) 
	{
		this.vlrPago = vlrPago;
	}
	
	//Implementacao de Entidade.
	
	/**
	 * 	@see		com.brt.gpp.comum.mapeamentos.entidade.Entidade#clone() 
	 */
	public Object clone()
	{
	    TotalizacaoRecargas result = new TotalizacaoRecargas();
	    
	    result.setDatMes(this.datMes);
	    result.setIdtMsisdn(this.idtMsisdn);
	    result.setQtdRecargas(this.qtdRecargas);
	    result.setVlrPago(this.vlrPago);
	    
	    return result;
	}

	/**
	 * 	@see		com.brt.gpp.comum.mapeamentos.entidade.Entidade#equals() 
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(this.getClass().isInstance(object)))
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
	 * 	@see		com.brt.gpp.comum.mapeamentos.entidade.Entidade#hashCode() 
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		result.append("||");
		result.append((this.datMes != null) ? this.datMes : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 * 	@see		com.brt.gpp.comum.mapeamentos.entidade.Entidade#toString() 
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		result.append("MSISDN: ");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		result.append(" - ");
		result.append("Mes de Concessao: ");
		result.append((this.datMes != null) ? this.datMes : "NULL");
		result.append(" - ");
		result.append("Quantidade de Recargas: ");
		result.append(this.format(TotalizacaoRecargas.QTD_RECARGAS));
		result.append(" - ");
		result.append("Valor Pago: ");
		result.append(this.format(TotalizacaoRecargas.VLR_PAGO));
		
		return result.toString();
	}
	
	//Implementacao de Totalizado.
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado#getNomeTotalizado()
	 */
	public boolean possuiMesmoPeriodo(ArquivoCDR arqCDR)
	{
		SimpleDateFormat conversorDatMes = new SimpleDateFormat("yyyyMM");
		return this.datMes.equals(conversorDatMes.format(arqCDR.getTimestamp()));
	}
	
	//Outros metodos.
		
	/** 
	 *	Converte o valor para String, sem formatacao. Se o valor for NULL, retorna o valor inicializado.
	 *
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		String sem formatacao.
	 */
	public String toString(int campo)
	{
	    switch(campo)
	    {
	    	case TotalizacaoRecargas.QTD_RECARGAS:
	    	    return String.valueOf(this.qtdRecargas);
	    	case TotalizacaoRecargas.VLR_PAGO:
	    	    return String.valueOf(this.vlrPago);
	    	default: return null;
	    }
	}
	
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor no formato String.
	 */
    public String format(int campo)
    {
	    switch(campo)
	    {
	    	case TotalizacaoRecargas.QTD_RECARGAS:
	    	{
	    		DecimalFormat conversorLong = new DecimalFormat("##,##0");
	    		return conversorLong.format(this.qtdRecargas);
	    	}
	    	case TotalizacaoRecargas.VLR_PAGO:
	    	{
	    		DecimalFormat conversorDouble = new DecimalFormat("##,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	    		return conversorDouble.format(this.vlrPago);
	    	}
	    	default: return null;
	    }
    }
	
	/**
	 *	Adiciona o campo com o valor. 
	 *
	 *	@param		campo					Campo selecionado.
	 *	@param		valor					Valor a ser adicionado ao campo.
	 */
	public void add(int campo, double valor)
	{
	    switch(campo)
	    {
	    	case TotalizacaoRecargas.QTD_RECARGAS:
	    	{
	    		this.qtdRecargas += new Double(valor).intValue();
	    		break;
	    	}	
	    	case TotalizacaoRecargas.VLR_PAGO:
	    	{
	    		this.vlrPago += valor;
	    		break;
	    	}
	    	default: break;
	    }
	}
	
	/**
	 *	Reseta o objeto.
	 */
	public void reset()
	{
		this.idtMsisdn		= null;
		this.datMes			= null;
		this.qtdRecargas	= 0;
		this.vlrPago		= 0.0;
	}
	
}
