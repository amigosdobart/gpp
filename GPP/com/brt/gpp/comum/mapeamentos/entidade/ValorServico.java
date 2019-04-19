package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_GER_CODIGO_COBRANCA.
 * 
 *	@author	Marcelo Alves Araujo
 *	@since	21/07/2006
 */
public class ValorServico implements Entidade, Serializable
{
	private String	idServico;
	private Double	valorServico;
	private Integer	codigoNacional;
	private Integer	planoPreco;
	private Date	dataInicioValidade;
	private Date	dataFimValidade;
	private String	idOrigem;
	private String	idCanal;
	private String	operacao;
		
	private static	DecimalFormat		conversorDouble;
	private static	SimpleDateFormat	conversorDate;
	
	public static final int DAT_INI_VALIDADE	= 0;
	public static final int DAT_FIM_VALIDADE	= 1;
	public static final int VLR_SERVICO			= 2;
	
	
	public ValorServico() 
	{
		codigoNacional = null;
		dataFimValidade = null;
		dataInicioValidade = null;
		idServico = null;
		planoPreco = null;
		valorServico = new Double(0.0);
		operacao = null;
		
		ValorServico.conversorDouble	= new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
		ValorServico.conversorDate		= new SimpleDateFormat(Definicoes.MASCARA_DATE);
	
	}
	
	public Integer getCodigoNacional() 
    {
		return codigoNacional;
	}
	public Date getDataFimValidade() 
	{
		return dataFimValidade;
	}
	public Date getDataInicioValidade() 
	{
		return dataInicioValidade;
	}
	public String getIdServico() 
	{
		return idServico;
	}
	public Integer getPlanoPreco() 
	{
		return planoPreco;
	}
	public Double getValorServico() 
	{
		return valorServico;
	}
	public String getIdCanal() 
	{
		return idCanal;
	}
	public String getIdOrigem() 
	{
		return idOrigem;
	}
	public String getOperacao() 
	{
		return operacao;
	}
	
	public void setValorServico(Double valorServico) 
	{
		this.valorServico = valorServico;
	}
	public void setCodigoNacional(Integer codigoNacional) 
	{
		this.codigoNacional = codigoNacional;
	}
	public void setDataFimValidade(Date dataFimValidade) 
	{
		this.dataFimValidade = dataFimValidade;
	}
	public void setDataInicioValidade(Date dataInicioValidade) 
	{
		this.dataInicioValidade = dataInicioValidade;
	}
	public void setIdServico(String idServico) 
	{
		this.idServico = idServico;
	}
	public void setPlanoPreco(Integer planoPreco) 
	{
		this.planoPreco = planoPreco;
	}
	public void setIdCanal(String idCanal) 
	{
		this.idCanal = idCanal;
	}
	public void setIdOrigem(String idOrigem) 
	{
		this.idOrigem = idOrigem;
	}
	public void setOperacao(String operacao) 
	{
		this.operacao = operacao;
	}
	
	//Implentacao de Entidade.
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		ValorServico result = new ValorServico();	
		
		result.setCodigoNacional(codigoNacional);
		result.setDataFimValidade(dataFimValidade);
		result.setDataInicioValidade(dataInicioValidade);
		result.setIdServico(idServico);
		result.setPlanoPreco(planoPreco);
		result.setValorServico(valorServico);
		result.setIdCanal(idCanal);
		result.setIdOrigem(idOrigem);
		result.setOperacao(operacao);
		
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
		
		if(!(object instanceof ValorServico))
		{
			return false;
		}
		
		if(this.hashCode() != ((ValorServico)object).hashCode())
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
		result.append((idServico != null) ? idServico : "NULL");
		result.append("||");
		result.append((codigoNacional != null) ? String.valueOf(codigoNacional.intValue()) : "NULL");
		result.append("||");
		result.append((planoPreco != null) ? String.valueOf(planoPreco.intValue()) : "NULL");
		result.append("||");
		result.append((dataInicioValidade != null) ? ValorServico.conversorDate.format(dataInicioValidade) : "NULL");
		result.append("||");
		result.append((dataFimValidade != null) ? ValorServico.conversorDate.format(dataFimValidade) : "NULL");
		result.append("||");
		result.append((operacao != null) ? operacao : "NULL");
				
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
	
		result.append("Codigo do Servico: ");
		result.append((idServico != null) ? idServico : "NULL");
		result.append(" - Codigo Nacional: ");
		result.append((codigoNacional != null) ? String.valueOf(codigoNacional.intValue()) : "NULL");
		result.append(" - Plano de Preco: ");
		result.append((planoPreco != null) ? String.valueOf(planoPreco.intValue()) : "NULL");
		result.append(" - Data Inicial: ");
		result.append((dataInicioValidade != null) ? ValorServico.conversorDate.format(dataInicioValidade) : "NULL");
		result.append(" - Data Final: ");
		result.append((dataFimValidade != null) ? ValorServico.conversorDate.format(dataFimValidade) : "NULL");
		result.append(" - Valor do Servico: ");
		result.append((valorServico != null) ? ValorServico.conversorDouble.format(valorServico) : "NULL");
		result.append(" - Operacao: ");
		result.append((operacao != null) ? operacao : "NULL");
				
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
	    	case ValorServico.DAT_INI_VALIDADE:
	    	{
	    	    return (dataInicioValidade != null) ? ValorServico.conversorDate.format(dataInicioValidade) : null;
	    	}
	    	case ValorServico.DAT_FIM_VALIDADE:
	    	{
	    	    return (dataFimValidade != null) ? ValorServico.conversorDate.format(dataFimValidade) : null;
	    	}
	    	case ValorServico.VLR_SERVICO:
	    	{
	    	    return (valorServico != null) ? ValorServico.conversorDouble.format(valorServico) : null;
	    	}
	    	default: return null;
	    }
    }
	
	/**
	 *	Indica se o registro esta vigente ou nao.
	 * 
	 *	@return		boolean												True se esta vigente e false caso contrario.
	 */
    public boolean isVigente(Date datProcessamento)
    {
        if(datProcessamento == null)
        {
            return false;
        }
        
        if(dataInicioValidade == null)
        {
            return false;
        }
        
        if(dataInicioValidade.compareTo(datProcessamento) > 0)
        {
            return false;
        }
        
        if(dataFimValidade == null)
        {
            return true;
        }
        
        return (dataFimValidade.compareTo(datProcessamento) >= 0);
    }
}
