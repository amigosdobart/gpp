package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports GPP.

import java.io.Serializable;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Classe PromocaoDiaExecucao, que representa a entidade da tabela TBL_PRO_DIA_EXECUCAO. Esta tabela 
 * contem o mapeamento da data de execucao pelo processo da promocao (ex: ClientePulaPulaGPP) e da data em que o 
 * registro sera consumido pela fila de recargas, em funcao da promocao e do dia do mes em que o assinante entrou 
 * na promocao.
 * 
 * @author	Daniel Ferreira
 * @since	13/06/2005
 *
 */
public class PromocaoDiaExecucao implements Entidade, Serializable
{
	
	private	Integer	idtPromocao;
	private String	tipExecucao;
	private	Integer	numDiaEntrada;
	private	Integer	numDiaExecucao;
	private	Integer	numDiaExecucaoRecarga;
	private Integer numHoraExecucaoRecarga;
		
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public PromocaoDiaExecucao()
	{
		this.idtPromocao			= null;
		this.tipExecucao			= null;
		this.numDiaEntrada			= null;
		this.numDiaExecucao			= null;
		this.numDiaExecucaoRecarga	= null;
		this.numHoraExecucaoRecarga	= null;
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
	 *	Retorna o tipo de execucao. O tipo de execucao faz parte da chave primaria da tabela e define o dia de 
	 *	execucao no processo da promocao e o dia de execucao na fila de recargas.
	 *	
	 *	@return		String					tipExecucao					Tipo de execucao.
	 */
	public String getTipExecucao() 
	{
		return this.tipExecucao;
	}
		
	/**
	 *	Retorna o dia de entrada na promocao.
	 *	
	 *	@return		Integer					numDiaEntrada				Dia de entrada na promocao.
	 */
	public Integer getNumDiaEntrada() 
	{
		return this.numDiaEntrada;
	}
		
	/**
	 *	Retorna o dia de execucao pelo processo da promocao.
	 *	
	 *	@return		Integer					numDiaExecucao				Dia de execucao.
	 */
	public Integer getNumDiaExecucao() 
	{
		return this.numDiaExecucao;
	}
		
	/**
	 *	Retorna o dia de execucao do bonus concedido pela promocao.
	 *	
	 *	@return		Integer					numDiaExecucaoRecarga		Dia de execucao da recarga.
	 */
	public Integer getNumDiaExecucaoRecarga() 
	{
		return this.numDiaExecucaoRecarga;
	}
	
	/**
	 *	Retorna a hora de execucao do bonus concedido pela promocao.
	 *	
	 *	@return		Integer					numHoraExecucaoRecarga		Hora de execucao da recarga.
	 */
	public Integer getNumHoraExecucaoRecarga() 
	{
		return this.numHoraExecucaoRecarga;
	}
	
	//Setters.
		
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
	 *	Atribui o tipo de execucao. O tipo de execucao faz parte da chave primaria da tabela e define o dia de 
	 *	execucao no processo da promocao e o dia de execucao na fila de recargas.
	 *	
	 *	@param		String					tipExecucao					Tipo de execucao.
	 */
	public void setTipExecucao(String tipExecucao) 
	{
		this.tipExecucao = tipExecucao;
	}
		
	/**
	 *	Atribui o dia de entrada na promocao.
	 *	
	 *	@param		Integer					numDiaEntrada				Dia de entrada na promocao.
	 */
	public void setNumDiaEntrada(Integer numDiaEntrada) 
	{
		this.numDiaEntrada = numDiaEntrada;
	}
	
	/**
	 *	Atribui o dia de execucao pelo processo da promocao.
	 *	
	 *	@param		Integer					numDiaExecucao				Dia de execucao.
	 */
	public void setNumDiaExecucao(Integer numDiaExecucao) 
	{
		this.numDiaExecucao = numDiaExecucao;
	}
	
	/**
	 *	Atribui o dia de execucao do bonus concedido pela promocao.
	 *	
	 *	@param		Integer					numDiaExecucaoRecarga		Dia de execucao da recarga.
	 */
	public void setNumDiaExecucaoRecarga(Integer numDiaExecucaoRecarga) 
	{
		this.numDiaExecucaoRecarga = numDiaExecucaoRecarga;
	}
		
	/**
	 *	Atribui a hora de execucao do bonus concedido pela promocao.
	 *	
	 *	@param		Integer					numHoraExecucaoRecarga		Hora de execucao da recarga.
	 */
	public void setNumHoraExecucaoRecarga(Integer numHoraExecucaoRecarga) 
	{
		this.numHoraExecucaoRecarga = numHoraExecucaoRecarga;
	}
		
	//Implementacao de Entidade.
		
	/** 
	 *	Retorna uma copia do objeto
	 *
	 *	@return		Object												Copia do objeto
	 */
	public Object clone()
	{
		PromocaoDiaExecucao result = new PromocaoDiaExecucao();
		
		result.setIdtPromocao((this.idtPromocao != null) ? new Integer(this.idtPromocao.intValue()) : null);
		result.setTipExecucao((this.tipExecucao != null) ? new String(this.tipExecucao) : null);
		result.setNumDiaEntrada((this.numDiaEntrada != null) ? new Integer(this.numDiaEntrada.intValue()) : null);
		result.setNumDiaExecucao((this.numDiaExecucao != null) ? new Integer(this.numDiaExecucao.intValue()) : null);
		result.setNumDiaExecucaoRecarga((this.numDiaExecucaoRecarga != null) ? new Integer(this.numDiaExecucaoRecarga.intValue()) : null);
		result.setNumHoraExecucaoRecarga((this.numHoraExecucaoRecarga != null) ? new Integer(this.numHoraExecucaoRecarga.intValue()) : null);
		
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
		
		if(!(object instanceof PromocaoDiaExecucao))
		{
			return false;
		}
		
		if(this.hashCode() != ((PromocaoDiaExecucao)object).hashCode())
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
		result.append((this.idtPromocao != null) ? String.valueOf(this.idtPromocao.intValue()) : "NULL");
		result.append("||");
		result.append((this.numDiaEntrada != null) ? String.valueOf(this.numDiaEntrada.intValue()) : "NULL");
		result.append("||");
		result.append((this.tipExecucao != null) ? this.tipExecucao : "NULL");
		
		return result.toString().hashCode();
	}
	
	/** 
	 *	Retorna uma representacao do Objeto em String
	 *
	 *	@return		String	Representacao do Objeto em formato String
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
			
		result.append("Promocao: ");
		result.append((this.idtPromocao != null) ? String.valueOf(this.idtPromocao.intValue()) : "NULL");
		result.append(" - ");
		result.append("Tipo de Execucao: ");
		result.append((this.tipExecucao != null) ? this.tipExecucao : "NULL");
		result.append(" - ");
		result.append("Dia de Entrada: ");
		result.append((this.numDiaEntrada != null) ? String.valueOf(this.numDiaEntrada.intValue()) : "NULL");
		result.append(" - ");
		result.append("Dia de Execucao: ");
		result.append((this.numDiaExecucao != null) ? String.valueOf(this.numDiaExecucao.intValue()) : "NULL");
		result.append(" - ");
		result.append("Dia de Recarga: ");
		result.append((this.numDiaExecucaoRecarga != null) ? String.valueOf(this.numDiaExecucaoRecarga.intValue()) : "NULL");
		result.append(" - ");
		result.append("Hora de Recarga: ");
		result.append((this.numHoraExecucaoRecarga != null) ? String.valueOf(this.numHoraExecucaoRecarga.intValue()) : "NULL");
			
		return result.toString();
	}
		
}
