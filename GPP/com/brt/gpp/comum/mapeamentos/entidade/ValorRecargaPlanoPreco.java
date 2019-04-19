package com.brt.gpp.comum.mapeamentos.entidade;

//Imports Java.

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

//Imports GPP.

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_REC_VALORES_PLANO_PRECO.
 *
 *	@author 	Daniel Ferreira
 *	@since 		29/03/2006
 */
public class ValorRecargaPlanoPreco implements Entidade
{
	private					Double			idValor;
	private					Long 			idtPlanoPreco;
	private	static	final	DecimalFormat	conversorDouble	= new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR")));
	
	//Constantes internas.
	
	public static final int ID_VALOR		= 0;
	public static final int	IDT_PLANO_PRECO	= 1;

	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public ValorRecargaPlanoPreco()
	{
		this.idValor = null;
		this.idtPlanoPreco = null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador do valor da recarga.
	 *
	 *	@return 	Double					idValor						Identificador do valor da recarga.
	 */
	public Double getIdValor()
	{
		return this.idValor;
	}
	
	/**
	 *	Retorna o identificador do plano de preco.
	 *
	 *	@return 	Long					idtPlanoPreco				Identificador do plano de preco.
	 */
	public Long getIdtPlanoPreco()
	{
		return this.idtPlanoPreco;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador do valor da recarga.
	 *
	 *	@param 	Double					idValor						Identificador do valor da recarga.
	 */
	public void setIdValor(Double idValor)
	{
		this.idValor = idValor;
	}
	
	/**
	 *	Atribui o identificador do plano de preco.
	 *
	 *	@param 	Long					idtPlanoPreco				Identificador do plano de preco.
	 */
	public void setIdtPlanoPreco(Long idtPlanoPreco)
	{
		this.idtPlanoPreco = idtPlanoPreco;
	}
	
	//Implementacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		ValorRecargaPlanoPreco result = new ValorRecargaPlanoPreco();	
		
		result.setIdValor((this.idValor != null) ? new Double(this.idValor.doubleValue()) : null);
		result.setIdtPlanoPreco((this.idtPlanoPreco != null) ? new Long(this.idtPlanoPreco.intValue()) : null);
		
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
		
		if(!(object instanceof ValorRecargaPlanoPreco))
		{
			return false;
		}
		
		if(this.hashCode() != ((ValorRecargaPlanoPreco)object).hashCode())
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
		result.append((this.idValor != null) ? String.valueOf(this.idValor.doubleValue()) : "NULL");
		result.append((this.idtPlanoPreco != null) ? String.valueOf(this.idtPlanoPreco.longValue()) : "NULL");
		
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
	
		result.append("Plano de Preco: ");
		result.append((this.idtPlanoPreco != null) ? String.valueOf(this.idtPlanoPreco.longValue()) : "NULL");
		result.append(" - ");
		result.append("Identificacao do Valor: ");
		result.append((this.idValor != null) ? ValorRecargaPlanoPreco.conversorDouble.format(this.idValor.doubleValue()) : "NULL");
		
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
	    	case ValorRecargaPlanoPreco.ID_VALOR:
	    	{
	    	    if(this.idValor != null)
	    	    {
		    	    return ValorRecargaPlanoPreco.conversorDouble.format(this.idValor.doubleValue());
	    	    }
	    	    
	    	    return null;
	    	}
	    	case ValorRecargaPlanoPreco.IDT_PLANO_PRECO:
	    	{
	    	    if(this.idtPlanoPreco != null)
	    	    {
	    	        return String.valueOf(this.idtPlanoPreco.longValue());
	    	    }
	    	}
	    	default: return null;
	    }
	}
	
}
