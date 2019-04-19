package br.com.brasiltelecom.ppp.model;

import java.util.ArrayList;
import java.util.Collection;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;

/**
 *	Classe que representa as informacoes de extrato gerado pelo GPP.
 *
 *	@author	Daniel Ferreira
 *	@since	10/10/2005
 */
public class RetornoExtratoPulaPula
{
	private String		retorno;
	private Promocao	promocao;
	private Collection	extratos;
	private Collection	sumarizacao;
	private Collection	bonificacoes;
	private Collection  bonusFuturos;
	private String		totalBonus;
	private String		totalParcial;
	private String		totalAReceber;
	private String		totalBonusAgendado;
	private String 		dataInicioBonificacao;
	private String		mensagem;

	//Construtoes.

	/**
	 *	Construtor da classe.
	 */
	public RetornoExtratoPulaPula()
	{
		this.retorno				= null;
		this.promocao				= null;
	    this.extratos				= new ArrayList();
	    this.sumarizacao			= new ArrayList();
	    this.bonificacoes			= new ArrayList();
	    this.totalBonus				= null;
	    this.totalParcial			= null;
	    this.totalAReceber			= null;
	    this.mensagem				= null;
	}

	/**
	 * Retorna coleção de bonificações
	 * @return Collection bonificacoes
	 */
	public Collection getBonificacoes()
	{
		return bonificacoes;
	}

	/**
	 * Atribui a coleção de bonificações
	 * @param bonificacoes
	 */
	public void setBonificacoes(Collection bonificacoes)
	{
		this.bonificacoes = bonificacoes;
	}

	/**
	 * Retorna coleção de extratos
	 * @return Collection extratos
	 */
	public Collection getExtratos()
	{
		return extratos;
	}

	/**
	 * Atribui a coleção de extratos
	 * @param extratos
	 */
	public void setExtratos(Collection extratos)
	{
		this.extratos = extratos;
	}

	/**
	 * Retorna mensagem
	 * @return String mensagem
	 */
	public String getMensagem()
	{
		return mensagem;
	}

	/**
	 * Atribui mensagem
	 * @param mensagem
	 */
	public void setMensagem(String mensagem)
	{
		this.mensagem = mensagem;
	}

	/**
	 * Retorna promoção
	 * @return Promocao promocao
	 */
	public Promocao getPromocao()
	{
		return promocao;
	}

	/**
	 * Atribui a promoção
	 * @param promocao
	 */
	public void setPromocao(Promocao promocao)
	{
		this.promocao = promocao;
	}

	/**
	 * Retorna código de retorno
	 * @return String retorno
	 */
	public String getRetorno()
	{
		return retorno;
	}

	/**
	 * Atribui o código de retorno
	 * @param retorno
	 */
	public void setRetorno(String retorno)
	{
		this.retorno = retorno;
	}

	/**
	 * Retorna coleção de sumarização
	 * @return Collection sumarizacao
	 */
	public Collection getSumarizacao()
	{
		return sumarizacao;
	}

	/**
	 * Atribui coleção de sumarização
	 * @param sumarizacao
	 */
	public void setSumarizacao(Collection sumarizacao)
	{
		this.sumarizacao = sumarizacao;
	}

	/**
	 * Retorna total de bônus a receber
	 * @return String totalAReceber
	 */
	public String getTotalAReceber()
	{
		return totalAReceber;
	}

	/**
	 * Atribui total de bônus a receber
	 * @param totalAReceber
	 */
	public void setTotalAReceber(String totalAReceber)
	{
		this.totalAReceber = totalAReceber;
	}

	/**
	 * Retorna total de bônus
	 * @return String totalBonus
	 */
	public String getTotalBonus()
	{
		return totalBonus;
	}

	/**
	 * Atribui o total de bônus
	 * @param totalBonus
	 */
	public void setTotalBonus(String totalBonus)
	{
		this.totalBonus = totalBonus;
	}

	/**
	 * Retorna total parcial de bônus
	 * @return String totalParcial
	 */
	public String getTotalParcial()
	{
		return totalParcial;
	}

	/**
	 * Atribui o total parcial de bônus
	 * @param totalParcial
	 */
	public void setTotalParcial(String totalParcial)
	{
		this.totalParcial = totalParcial;
	}

	/**
	 * Retorna colecao de bonus futuros.
	 * @return
	 */
	public Collection getBonusFuturos() 
	{
		return bonusFuturos;
	}

	/**
	 * Atribui colecao de bonus futuros.
	 * @param bonusFuturos
	 */
	public void setBonusFuturos(Collection bonusFuturos) 
	{
		this.bonusFuturos = bonusFuturos;
	}

	/**
	 * Retorna data de inicio das bonificacoes.
	 * @return
	 */
	public String getDataInicioBonificacao() 
	{
		return dataInicioBonificacao;
	}

	/**
	 * Atribui a data de inicio das bonificacoes.
	 * @param dataInicioBonificacao
	 */
	public void setDataInicioBonificacao(String dataInicioBonificacao) 
	{
		this.dataInicioBonificacao = dataInicioBonificacao;
	}

	/**
	 * Retorna total de bonus agendado.
	 * @return
	 */
	public String getTotalBonusAgendado() 
	{
		return totalBonusAgendado;
	}

	/**
	 * Atribui o total de bonus agendado.
	 * @param totalBonusAgendado
	 */
	public void setTotalBonusAgendado(String totalBonusAgendado) 
	{
		this.totalBonusAgendado = totalBonusAgendado;
	}
}