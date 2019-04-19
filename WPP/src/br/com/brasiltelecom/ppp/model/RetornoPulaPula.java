package br.com.brasiltelecom.ppp.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Modela as informações sobre Retorno da promocao PulaPula do cliente
 *
 * @author João Paulo Galvagni e Marcos Magalhães
 * @since 13/10/2005
 */
public class RetornoPulaPula
{

	private String msisdn;

	private String retorno;

	private String mensagemRetorno;

	private String retornoValidacao;

	private String identificador;

	private String nome;

	private String categoria;

	private String dataExecucao;

	private String dataCredito;

	private String dataEntrada;

	private String dataInicioAnalise;

	private String dataFimAnalise;

	private String limiteDinamico;

	private String permiteIsencao;

	private String isentoLimite;

	private String status;

	private String recebimentoTotal;

	private String recebimentoFF;

	private String recebimentoNoturno;

	private String recebimentoDiurno;

	private String recebimentoNaoBonificado;

	private String recargasEfetuadas;

	private String valorTotal;

	private String valorParcial;

	private String valorAReceber;

	private String limite;

	private String saturado;

	private String saldoConcessaoFracionada;
	
	private String valorTotalBonusAgendado;
	
	private String dataInicioBonificacao;

	private Collection bonificacoes = new ArrayList();
	
	private Collection bonusAgendados = new ArrayList();
	
	private Collection bonusFuturos = new ArrayList();

	public String getRecebimentoDiurno()
	{
		return recebimentoDiurno;
	}

	public void setRecebimentoDiurno(String recebimentoDiurno)
	{
		this.recebimentoDiurno = recebimentoDiurno;
	}

	public String getRecebimentoNaoBonificado()
	{
		return recebimentoNaoBonificado;
	}

	public void setRecebimentoNaoBonificado(String recebimentoNaoBonificado)
	{
		this.recebimentoNaoBonificado = recebimentoNaoBonificado;
	}

	public String getRecebimentoNoturno()
	{
		return recebimentoNoturno;
	}

	public void setRecebimentoNoturno(String recebimentoNoturno)
	{
		this.recebimentoNoturno = recebimentoNoturno;
	}

	public String getCategoria()
	{
		return categoria;
	}

	public void setCategoria(String categoria)
	{
		this.categoria = categoria;
	}

	public String getDataCredito()
	{
		return dataCredito;
	}

	public void setDataCredito(String dataCredito)
	{
		this.dataCredito = dataCredito;
	}

	public String getDataEntrada()
	{
		return dataEntrada;
	}

	public void setDataEntrada(String dataEntrada)
	{
		this.dataEntrada = dataEntrada;
	}

	public String getDataExecucao()
	{
		return dataExecucao;
	}

	public void setDataExecucao(String dataExecucao)
	{
		this.dataExecucao = dataExecucao;
	}

	public String getDataFimAnalise()
	{
		return dataFimAnalise;
	}

	public void setDataFimAnalise(String dataFimAnalise)
	{
		this.dataFimAnalise = dataFimAnalise;
	}

	public String getDataInicioAnalise()
	{
		return dataInicioAnalise;
	}

	public void setDataInicioAnalise(String dataInicioAnalise)
	{
		this.dataInicioAnalise = dataInicioAnalise;
	}

	public String getIdentificador()
	{
		return identificador;
	}

	public void setIdentificador(String identificador)
	{
		this.identificador = identificador;
	}

	public String getIsentoLimite()
	{
		return isentoLimite;
	}

	public void setIsentoLimite(String isentoLimite)
	{
		this.isentoLimite = isentoLimite;
	}

	public String getLimiteDinamico()
	{
		return limiteDinamico;
	}

	public void setLimiteDinamico(String limiteDinamico)
	{
		this.limiteDinamico = limiteDinamico;
	}

	public String getLimite()
	{
		return limite;
	}

	public void setLimite(String limite)
	{
		this.limite = limite;
	}

	public String getMsisdn()
	{
		return msisdn;
	}

	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getPermiteIsencao()
	{
		return permiteIsencao;
	}

	public void setPermiteIsencao(String permiteIsencao)
	{
		this.permiteIsencao = permiteIsencao;
	}

	public String getRecargasEfetuadas()
	{
		return recargasEfetuadas;
	}

	public void setRecargasEfetuadas(String recargasEfetuadas)
	{
		this.recargasEfetuadas = recargasEfetuadas;
	}

	public String getRecebimentoFF()
	{
		return recebimentoFF;
	}

	public void setRecebimentoFF(String recebimentoFF)
	{
		this.recebimentoFF = recebimentoFF;
	}

	public String getRecebimentoTotal()
	{
		return recebimentoTotal;
	}

	public void setRecebimentoTotal(String recebimentoTotal)
	{
		this.recebimentoTotal = recebimentoTotal;
	}

	public String getRetorno()
	{
		return retorno;
	}

	public void setRetorno(String retorno)
	{
		this.retorno = retorno;
	}

	public String getRetornoValidacao()
	{
		return retornoValidacao;
	}

	public void setRetornoValidacao(String retornoValidacao)
	{
		this.retornoValidacao = retornoValidacao;
	}

	public String getSaturado()
	{
		return saturado;
	}

	public void setSaturado(String saturado)
	{
		this.saturado = saturado;
	}

	public String getValorAReceber()
	{
		return valorAReceber;
	}

	public void setValorAReceber(String valorAReceber)
	{
		this.valorAReceber = valorAReceber;
	}

	public String getValorParcial()
	{
		return valorParcial;
	}

	public void setValorParcial(String valorParcial)
	{
		this.valorParcial = valorParcial;
	}

	public String getValorTotal()
	{
		return valorTotal;
	}

	public void setValorTotal(String valorTotal)
	{
		this.valorTotal = valorTotal;
	}

	/**
	 * @return Returns the mensagemRetorno.
	 */
	public String getMensagemRetorno()
	{
		return mensagemRetorno;
	}

	/**
	 * @param mensagemRetorno
	 *            The mensagemRetorno to set.
	 */
	public void setMensagemRetorno(String mensagemRetorno)
	{
		this.mensagemRetorno = mensagemRetorno;
	}

	public String getSaldoConcessaoFracionada()
	{
		return saldoConcessaoFracionada;
	}

	public String getStatus()
	{
		return status;
	}

	public void setSaldoConcessaoFracionada(String saldoConcessaoFracionada)
	{
		this.saldoConcessaoFracionada = saldoConcessaoFracionada;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Collection getBonificacoes()
	{
		return bonificacoes;
	}

	public void setBonificacoes(Collection bonificacoes)
	{
		this.bonificacoes = bonificacoes;
	}

	public Collection getBonusAgendados() 
	{
		return bonusAgendados;
	}

	public void setBonusAgendados(Collection bonusAgendados) 
	{
		this.bonusAgendados = bonusAgendados;
	}
	
	public Collection getBonusFuturos() 
	{
		return bonusFuturos;
	}

	public void setBonusFuturos(Collection bonusFuturos) 
	{
		this.bonusFuturos = bonusFuturos;
	}

	public String getDataInicioBonificacao() 
	{
		return dataInicioBonificacao;
	}

	public void setDataInicioBonificacao(String dataInicioBonificacao) 
	{
		this.dataInicioBonificacao = dataInicioBonificacao;
	}

	public String getValorTotalBonusAgendado() 
	{
		return valorTotalBonusAgendado;
	}

	public void setValorTotalBonusAgendado(String valorTotalBonusAgendado) 
	{
		this.valorTotalBonusAgendado = valorTotalBonusAgendado;
	}
	
	
}
