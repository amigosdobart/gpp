package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;

/**
 *	Entidade da tabela TBL_PRO_INFOS_SMS, que mapeia as informacoes de SMS aos assinantes durante o processo de 
 *	concessao de bonus da promocao.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/09/2005
 *	@modify		Primeira versao.
 *
 *	@version	2.0
 *	@author		Daniel Ferreira
 *	@date		18/03/2008
 *	@modify		Inclusao de multiplas bonificacoes. Cada bonificacao durante o processo de concessao deve ter 
 *				informacoes de envio de SMS diferentes.
 */
public class PromocaoInfosSms
{

	/**
	 *	Informacoes da promocao.
	 */
	private Promocao promocao;
	
	/**
	 *	Tipo de execucao do processo de concessao.
	 */
	private String tipExecucao;
	
	/**
	 *	Informacoes do tipo de bonificacao.
	 */
	private PromocaoTipoBonificacao tipoBonificacao;
	
	/**
	 *	Indicador de necessidade de envio de SMS.
	 */
	private boolean indEnviaSms;
	
	/**
	 *	Identificador do tipo de SMS.
	 */
	private String tipSms;
	
	/**
	 *	Grau de prioridade do envio de SMS. Quanto menor o valor, maior a prioridade.
	 */
	private short numPrioridade;
	
	/**
	 *	Mensagem a ser enviada ao assinante no momento da concessao.
	 */
	private String desMensagem;
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoInfosSms()
	{
		this.promocao			= null;
		this.tipExecucao		= null;
		this.tipoBonificacao	= null;
		this.indEnviaSms		= false;
		this.tipSms				= null;
		this.numPrioridade		= 0;
		this.desMensagem		= null;
	}
	
	/**
	 *	Retorna as informacoes da promocao.
	 * 
	 *	@return		Informacoes da promocao.
	 */
	public Promocao getPromocao() 
	{
		return this.promocao;
	}
	
	/**
	 *	Retorna o tipo de execucao da concessao.
	 *	
	 *	@return		Tipo de execucao da concessao.
	 */
	public String getTipExecucao() 
	{
		return this.tipExecucao;
	}
	
	/**
	 *	Retorna as informacoes do tipo de bonificacao.
	 *
	 *	@return		Informacoes do tipo de bonificacao.
	 */
	public PromocaoTipoBonificacao getTipoBonificacao()
	{
		return this.tipoBonificacao;
	}
	
	/**
	 *	Indica se a promocao deve enviar SMS durante a execucao.
	 * 
	 *	@return		True se deve enviar e false caso contrario.
	 */
	public boolean enviaSms()
	{
	    return this.indEnviaSms;
	}

	/**
	 *	Retorna o tipo de SMS a ser enviado.
	 *	
	 *	@return		Tipo de SMS.
	 */
	public String getTipSms() 
	{
		return this.tipSms;
	}
	
	/**
	 *	Retorna a prioridade do envio do SMS.
	 *	
	 *	@return		Prioridade do envio de SMS.
	 */
	public short getNumPrioridade() 
	{
		return this.numPrioridade;
	}
	
	/**
	 *	Retorna o conteudo da mensagem do SMS.
	 *	
	 *	@return		Conteudo da mensagem do SMS.
	 */
	public String getDesMensagem() 
	{
		return this.desMensagem;
	}
	
	/**
	 *	Atribui as informacoes da promocao.
	 * 
	 *	@param		promocao				Informacoes da promocao.
	 */
	public void setPromocao(Promocao promocao) 
	{
		this.promocao = promocao;
	}
	
	/**
	 *	Atribui o tipo de execucao. 
	 *	
	 *	@param		tipExecucao				Tipo de execucao.
	 */
	public void setTipExecucao(String tipExecucao) 
	{
		this.tipExecucao = tipExecucao;
	}
		
	/**
	 *	Atribui as informacoes do tipo de bonificacao.
	 *
	 *	@return		tipoBonificacao			Informacoes do tipo de bonificacao.
	 */
	public void setTipoBonificacao(PromocaoTipoBonificacao tipoBonificacao)
	{
		this.tipoBonificacao = tipoBonificacao;
	}
	
	/**
	 *	Atribui o indicador de envio de SMS, que determina se o SMS deve ser enviado ou nao.
	 *	
	 *	@param		indEnviaSms				Indicador de envio de SMS.
	 */
	public void setIndEnviaSms(boolean indEnviaSms) 
	{
		this.indEnviaSms = indEnviaSms;
	}
	
	/**
	 *	Atribui o tipo de SMS a ser enviado.
	 *	
	 *	@param		tipSms					Tipo de SMS.
	 */
	public void setTipSms(String tipSms) 
	{
		this.tipSms = tipSms;
	}
	
	/**
	 *	Atribui a prioridade do envio do SMS.
	 *	
	 *	@param		numPrioridade			Prioridade do envio de SMS.
	 */
	public void setNumPrioridade(short numPrioridade) 
	{
		this.numPrioridade = numPrioridade;
	}
	
	/**
	 *	Atribui o conteudo da mensagem do SMS.
	 *	
	 *	@param		desMensagem				Conteudo da mensagem do SMS.
	 */
	public void setDesMensagem(String desMensagem) 
	{
		this.desMensagem = desMensagem;
	}
	
	/**
	 *	@see		java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return (this.hashCode() == obj.hashCode());
	}
	
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return (this.getClass().getName() + "||" +
				this.getPromocao().hashCode() + "||" + 
				this.getTipExecucao().hashCode() + "||" +
				this.getTipoBonificacao().hashCode()).hashCode();
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getPromocao());
		result.append(" - ");
		result.append(this.getTipExecucao());
		result.append(" - ");
		result.append(this.getTipoBonificacao());
		result.append(" - ");
		result.append(this.getDesMensagem());
		
		return result.toString();
	}
	
}
