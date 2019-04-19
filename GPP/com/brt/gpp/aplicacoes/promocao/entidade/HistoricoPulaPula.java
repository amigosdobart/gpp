package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports Java.

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

//Imports GPP.

import com.brt.gpp.comum.Definicoes;

/**
 *	Classe HistoricoPulaPula, que representa a entidade da tabela TBL_PRO_HISTORICO_PULA_PULA. Esta tabela contem
 *	o historico da execucao do processo da promocao Pula-Pula para cada assinante. 
 * 
 *	@author	Daniel Ferreira
 *	@since	29/09/2005
 */
public class HistoricoPulaPula 
{
	
	private	String				idtMsisdn;
	private Integer				idtPromocao;
	private	Timestamp			datExecucao;
	private String				nomOperador;
	private Integer				idtMotivo;
	private String				desStatusExecucao;
	private Integer				idtCodigoRetorno;
	private Double				vlrCreditoBonus;
	private DecimalFormat		conversorRetorno;
	private	SimpleDateFormat	conversorTimestamp;
	private DecimalFormat		conversorDouble;
	
	//Constantes internas.

	public static final int IDT_CODIGO_RETORNO	= 0;
	public static final int DAT_EXECUCAO		= 1;
	public static final int VLR_CREDITO_BONUS	= 2;
	
	//Construtores.
	
	/**
	 *	Construtor da classe
	 */
	public HistoricoPulaPula()
	{
		this.idtMsisdn			= null;
		this.idtPromocao		= null;
		this.datExecucao		= null;
		this.desStatusExecucao	= null;
		this.idtCodigoRetorno	= null;
		this.vlrCreditoBonus	= null;
		this.nomOperador		= null;
		this.idtMotivo			= null;
		this.conversorRetorno	= new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
		this.conversorTimestamp	= new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
		this.conversorDouble	= new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
	}
	
	//Getters.
	
	/**
	 *	Retorna o MSISDN.	
	 *
	 *	@return		String					idtMsisdn					MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o identificador da promocao.
	 *	
	 *	@return		Integer					idtPromocao					Identificador da promocao.
	 */
	public Integer getIdtPromocao() 
	{
		return this.idtPromocao;
	}
	
	/**
	 *	Retorna a data de execucao do assinante na promocao.
	 * 
	 *	@return		Timestamp				datExecucao					Data de execucao da promocao do assinante.
	 */
	public Timestamp getDatExecucao() 
	{
		return this.datExecucao;
	}
	
	/**
	 *	Retorna o nome ou matricula do operador que executou a operacao.
	 *
     *	@return		String					nomOperador					Nome do operador que executou a operacao.
	 */
	public String getNomOperador() 
	{
		return this.nomOperador;
	}
	
	/**
	 *	Retorna o identificador do motivo da operacao.
	 *
     *	@return		Integer					idtMotivo					Identificador do motivo da operacao.
	 */
	public Integer getIdtMotivo() 
	{
		return this.idtMotivo;
	}
	
	/**
	 *	Retorna a descricao do staus da execucao do assinante na promocao.	
	 *
	 *	@return		String					desStatusExecucao			Descricao do status da execucao.
	 */
	public String getDesStatusExecucao() 
	{
		return this.desStatusExecucao;
	}
	
	/**
	 *	Retorna o codigo de retorno da execucao do assinante na promocao.
	 *
	 * @return		Integer					idtCodigoRetorno			Codigo de retorno da execucao.
	 */
	public Integer getIdtCodigoRetorno() 
	{
		return this.idtCodigoRetorno;
	}
	
	/**
	 *	Retorna o valor do bonus efetivamento concedido ao assinante.
	 *
	 * @return		Double					vlrCreditoBonus				Valor de bonus concedido.
	 */
	public Double getVlrCreditoBonus() 
	{
		return this.vlrCreditoBonus;
	}
	
	//Setters.
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		String					idtMsisdn					MSISDN do assinante.
	 */
	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	/**
	 *	Atribui o identificador da promocao.
	 * 
	 *	@param		Integer					idtPromocao					Identificador da promocao.
	 */
	public void setIdtPromocao(Integer idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
	
	/**
	 *	Atribui a data de execucao do assinante na promocao.
	 * 
	 *	@param		Timestamp				datExecucao					Data de execucao da promocao do assinante.
	 */
    public void setDatExecucao(Timestamp datExecucao)
    {
        this.datExecucao = datExecucao;
    }
	
	/**
	 *	Atribui o nome ou matricula do operador que executou a operacao.
	 *
     *	@param		String					nomOperador					Nome do operador que executou a operacao.
	 */
	public void setNomOperador(String nomOperador) 
	{
		this.nomOperador = nomOperador;
	}
	
	/**
	 *	Atribui o identificador do motivo da operacao.
	 *
     *	@param		Integer					idtMotivo					Identificador do motivo da operacao.
	 */
	public void setIdtMotivo(Integer idtMotivo) 
	{
		this.idtMotivo = idtMotivo;
	}
	
	/**
	 *	Atribui a descricao do staus da execucao do assinante na promocao.	
	 *
	 *	@param		String					desStatusExecucao			Descricao do status da execucao.
	 */
	public void setDesStatusExecucao(String desStatusExecucao) 
	{
		this.desStatusExecucao = desStatusExecucao;
	}
	
	/**
	 *	Atribui o codigo de retorno da execucao do assinante na promocao.
	 *
	 * @param		Integer					idtCodigoRetorno			Codigo de retorno da execucao.
	 */
	public void setIdtCodigoRetorno(Integer idtCodigoRetorno) 
	{
		this.idtCodigoRetorno = idtCodigoRetorno;
	}
	
	/**
	 *	Atribui o valor do bonus efetivamento concedido ao assinante.
	 *
	 * @param		Double					vlrCreditoBonus				Valor de bonus concedido.
	 */
	public void setVlrCreditoBonus(Double vlrCreditoBonus) 
	{
		this.vlrCreditoBonus = vlrCreditoBonus;
	}
	
	//Outros metodos.
	
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
		
		if(!(object instanceof HistoricoPulaPula))
		{
			return false;
		}
		
		if(this.hashCode() != ((HistoricoPulaPula)object).hashCode())
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
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		result.append("||");
		result.append((this.datExecucao != null) ? this.conversorTimestamp.format(this.datExecucao) : "NULL");
		
		return result.toString().hashCode();
	}
	
	/** 
	 *	Retorna uma representacao do Objeto em String.
	 *
	 *	@return		String												Representacao do Objeto em formato String.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		result.append("MSISDN: ");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		result.append(" - ");
		result.append("Data de Execucao: ");
		result.append((this.datExecucao != null) ? this.conversorTimestamp.format(this.datExecucao) : "NULL");
		result.append(" - ");
		result.append("Codigo de Retorno: ");
		result.append((this.idtCodigoRetorno != null) ? this.conversorRetorno.format(this.idtCodigoRetorno) : "NULL");
		result.append(" - ");
		result.append("Valor de Bonus: ");
		result.append((this.vlrCreditoBonus != null) ? this.conversorDouble.format(this.vlrCreditoBonus) : "NULL");
		
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
	    	case HistoricoPulaPula.IDT_CODIGO_RETORNO:
	    	{
	    	    return (this.idtCodigoRetorno != null) ? this.conversorRetorno.format(this.idtCodigoRetorno) : null;
	    	}
    		case HistoricoPulaPula.DAT_EXECUCAO:
    		{
    		    return this.conversorTimestamp.format(this.datExecucao);
    		}
	    	case HistoricoPulaPula.VLR_CREDITO_BONUS:
	    	{
	    	    return (this.vlrCreditoBonus != null) ? this.conversorDouble.format(this.vlrCreditoBonus) : null;
	    	}
	    	default: return null;
	    }
	}
	
}
