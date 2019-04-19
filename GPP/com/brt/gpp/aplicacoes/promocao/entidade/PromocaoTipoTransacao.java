package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_TIPO_TRANSACAO, que mapeia os tipos de transacao de recargas ou
 *	bonus concedidos aos assinantes pela promocao.
 * 
 *	@author	Daniel Ferreira
 *	@since	26/09/2005
 */
public class PromocaoTipoTransacao implements Comparable
{

	/**
	 *	Promocao ao qual o tipo de transacao se refere.
	 */
	private Promocao promocao;
	
	/**
	 *	Tipo de execucao, que faz parte da chave primaria da tabela e define o tipo de transacao da recarga ou 
	 *	bonus concedido no processo da promocao.
	 */
	private String tipExecucao;

	/**
	 *	Tipo de bonificacao da concessao.
	 */
	private PromocaoTipoBonificacao tipoBonificacao;
	
	/**
	 *	Origem da recarga ou ajuste.
	 */
	private OrigemRecarga origem;
	
	/**
	 *	Numero da ordem de bonificacao. Estabelece uma dependencia entre os bonus concedidos pelo processo.
	 */
	private short numOrdem;
	
	/**
	 *	Flag indicativo de necessidade de zerar o Saldo Periodico durante a concessao.
	 */
	private boolean indZerarSaldoPeriodico;
	
	/**
	 *	Flag indicativo de necessidade de zerar o Saldo de Bonus durante a concessao.
	 */
	private boolean indZerarSaldoBonus;
	
	/**
	 *	Flag indicativo de necessidade de zerar o Saldo de Torpedos durante a concessao.
	 */
	private boolean indZerarSaldoTorpedos;
	
	/**
	 *	Flag indicativo de necessidade de zerar o Saldo de Dados durante a concessao.
	 */
	private boolean indZerarSaldoDados;
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoTipoTransacao()
	{
		this.promocao				= null;
		this.tipExecucao			= null;
		this.origem					= null;
		this.numOrdem				= -1;
		this.indZerarSaldoPeriodico	= false;
		this.indZerarSaldoBonus		= false;
		this.indZerarSaldoTorpedos	= false;
		this.indZerarSaldoDados		= false;
	}
	
	/**
	 *	Retorna a promocao ao qual o tipo de transacao se refere.
	 * 
	 *	@return		Promocao ao qual o tipo de transacao se refere.
	 */
	public Promocao getPromocao() 
	{
		return this.promocao;
	}
	
	/**
	 *	Retorna o tipo de execucao.
	 *	
	 *	@return		Tipo de execucao.
	 */
	public String getTipExecucao() 
	{
		return this.tipExecucao;
	}
	
	/**
	 *	Retorna o tipo de bonificacao da concessao.
	 *
	 *	@return		Tipo de bonificacao da concessao.
	 */
	public PromocaoTipoBonificacao getTipoBonificacao()
	{
		return this.tipoBonificacao;
	}
	
	/**
	 *	Retorna a origem da recarga ou ajuste.
	 *	
	 *	@return		Origem da recarga ou ajuste.
	 */
	public OrigemRecarga getOrigem() 
	{
		return this.origem;
	}
	
	/**
	 *	Retorna o numero da ordem de bonificacao.
	 *
	 *	@return		Numero da ordem de bonificacao.
	 */
	public short getNumOrdem()
	{
		return this.numOrdem;
	}
	
	/**
	 *	Indica a necessidade de zerar o Saldo Periodico durante a concessao.
	 *
	 *	@return		True se ha necessidade de zerar o saldo e false caso contrario.
	 */
	public boolean zerarSaldoPeriodico()
	{
		return this.indZerarSaldoPeriodico;
	}
	
	/**
	 *	Indica a necessidade de zerar o Saldo de Bonus durante a concessao.
	 *
	 *	@return		True se ha necessidade de zerar o saldo e false caso contrario.
	 */
	public boolean zerarSaldoBonus()
	{
		return this.indZerarSaldoBonus;
	}
	
	/**
	 *	Indica a necessidade de zerar o Saldo de Torpedos durante a concessao.
	 *
	 *	@return		True se ha necessidade de zerar o saldo e false caso contrario.
	 */
	public boolean zerarSaldoTorpedos()
	{
		return this.indZerarSaldoTorpedos;
	}
	
	/**
	 *	Indica a necessidade de zerar o Saldo de Dados durante a concessao.
	 *
	 *	@return		True se ha necessidade de zerar o saldo e false caso contrario.
	 */
	public boolean zerarSaldoDados()
	{
		return this.indZerarSaldoDados;
	}
	
	/**
	 *	Atribui a promocao ao qual o tipo de transacao se refere.
	 * 
	 *	@param		promocao				Promocao ao qual o tipo de transacao se refere.
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
	 *	Atribui o tipo de bonificacao da concessao.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao da concessao.
	 */
	public void setTipoBonificacao(PromocaoTipoBonificacao tipoBonificacao)
	{
		this.tipoBonificacao = tipoBonificacao;
	}
	
	/**
	 *	Atribui a origem da recarga ou ajuste.
	 *	
	 *	@param		origem					Origem da recarga ou ajuste.
	 */
	public void setOrigem(OrigemRecarga origem) 
	{
		this.origem = origem;
	}
	
	/**
	 *	Atribui o numero da ordem de bonificacao.
	 *
	 *	@param		numOrdem				Numero da ordem de bonificacao.
	 */
	public void setNumOrdem(short numOrdem)
	{
		this.numOrdem = numOrdem;
	}
	
	/**
	 *	Atribui o flag indicativo de necessidade de zerar o Saldo Periodico durante a concessao.
	 *
	 *	@param		indZerarSaldoPeriodico	Flag indicativo de zerar o saldo durante a concessao.
	 */
	public void setIndZerarSaldoPeriodico(boolean indZerarSaldoPeriodico)
	{
		this.indZerarSaldoPeriodico = indZerarSaldoPeriodico;
	}
	
	/**
	 *	Atribui o flag indicativo de necessidade de zerar o Saldo de Bonus durante a concessao.
	 *
	 *	@param		indZerarSaldoBonus		Flag indicativo de zerar o saldo durante a concessao.
	 */
	public void setIndZerarSaldoBonus(boolean indZerarSaldoBonus)
	{
		this.indZerarSaldoBonus = indZerarSaldoBonus;
	}
	
	/**
	 *	Atribui o flag indicativo de necessidade de zerar o Saldo de Torpedos durante a concessao.
	 *
	 *	@param		indZerarSaldoTorpedos	Flag indicativo de zerar o saldo durante a concessao.
	 */
	public void setIndZerarSaldoTorpedos(boolean indZerarSaldoTorpedos)
	{
		this.indZerarSaldoTorpedos = indZerarSaldoTorpedos;
	}
	
	/**
	 *	Atribui o flag indicativo de necessidade de zerar o Saldo de Dados durante a concessao.
	 *
	 *	@param		indZerarSaldoDados		Flag indicativo de zerar o saldo durante a concessao.
	 */
	public void setIndZerarSaldoDados(boolean indZerarSaldoDados)
	{
		this.indZerarSaldoDados = indZerarSaldoDados;
	}
	
	/**
	 *	@see		java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object obj)
	{
		int						result			= 0;
		PromocaoTipoTransacao	tipoTransacao	= (PromocaoTipoTransacao)obj;
		
		result = this.getPromocao().getIdtPromocao() - tipoTransacao.getPromocao().getIdtPromocao();

		if(result != 0)
			return result;
		
		result = this.getTipExecucao().compareTo(tipoTransacao.getTipExecucao());

		if(result != 0)
			return result;
		
		result = this.getNumOrdem() - tipoTransacao.getNumOrdem();
		
		return result;
	}
	
	/**
	 *	@see		java.lang.Object#equals(Object obj)
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
		result.append(this.getOrigem());
		
		return result.toString();
	}
	
}
