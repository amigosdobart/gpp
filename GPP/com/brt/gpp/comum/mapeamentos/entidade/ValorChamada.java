package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>ValorChamada</code>. Referência: TBL_TAR_VALOR_CHAMADA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/06/2007
 */
public class ValorChamada implements Serializable
{
	private int idValorChamada; 
	private OperadoraLD operadoraLD;
	private PlanoTarifacao planoTarifacao;
	
	private TipoChamada tipoChamada;
	private String area; 	
	private ValorDeslocamento valorDeslocamento; 
	private Modulacao modulacao; 	
	private int duracaoChamada;
	private int valorChamada; 
	private int valorConexao;
	private int numSegundosInicial; 
	private int numSegundosPulso;

	/**
	 * @return Código de área (vide GER_CODIGO_NACIONAL)
	 */
	public String getArea() 
	{
		return area;
	}

	/**
	 * @param area Código de área (vide GER_CODIGO_NACIONAL)
	 */
	public void setArea(String area) 
	{
		this.area = area;
	}

	/**
	 * @return Duracao da chamada
	 */
	public int getDuracaoChamada() 
	{
		return duracaoChamada;
	}

	/**
	 * @param duracaoChamada Duracao da chamada
	 */
	public void setDuracaoChamada(int duracaoChamada) 
	{
		this.duracaoChamada = duracaoChamada;
	}

	/**
	 * @return ID do Valor de Chamada
	 */
	public int getIdValorChamada() 
	{
		return idValorChamada;
	}

	/**
	 * @param idValorChamada ID do Valor de Chamada
	 */
	public void setIdValorChamada(int idValorChamada) 
	{
		this.idValorChamada = idValorChamada;
	}

	/**
	 * @return Instancia de <code>Modulacao</code>
	 */
	public Modulacao getModulacao() 
	{
		return modulacao;
	}

	/**
	 * @param modulacao Instancia de <code>Modulacao</code>
	 */
	public void setModulacao(Modulacao modulacao) 
	{
		this.modulacao = modulacao;
	}

	/**
	 * @return Quantidade de segundos do pulso
	 */
	public int getNumSegundosPulso() 
	{
		return numSegundosPulso;
	}

	/**
	 * @param numSegundosPulso Quantidade de segundos do pulso
	 */
	public void setNumSegundosPulso(int numSegundosPulso) 
	{
		this.numSegundosPulso = numSegundosPulso;
	}

	/**
	 * @return Quantidade de segundos inicial
	 */
	public int getNumSegundosInicial() 
	{
		return numSegundosInicial;
	}

	/**
	 * @param numSegundosInicial Quantidade de segundos inicial
	 */
	public void setNumSegundosInicial(int numSegundosInicial) 
	{
		this.numSegundosInicial = numSegundosInicial;
	}

	/**
	 * @return Instancia de <code>OperadoraLD</code>
	 */
	public OperadoraLD getOperadoraLD() 
	{
		return operadoraLD;
	}

	/**
	 * @param operadoraLD Instancia de <code>OperadoraLD</code>
	 */
	public void setOperadoraLD(OperadoraLD operadoraLD) 
	{
		this.operadoraLD = operadoraLD;
	}

	/**
	 * @return Instancia de <code>PlanoTarifacao</code>
	 */
	public PlanoTarifacao getPlanoTarifacao() 
	{
		return planoTarifacao;
	}

	/**
	 * @param planoTarifacao Instancia de <code>PlanoTarifacao</code>
	 */
	public void setPlanoTarifacao(PlanoTarifacao planoTarifacao) 
	{
		this.planoTarifacao = planoTarifacao;
	}

	/**
	 * @return Instancia de <code>TipoChamada</code>
	 */
	public TipoChamada getTipoChamada() 
	{
		return tipoChamada;
	}

	/**
	 * @param tipoChamada Instancia de <code>TipoChamada</code>
	 */
	public void setTipoChamada(TipoChamada tipoChamada) 
	{
		this.tipoChamada = tipoChamada;
	}

	/**
	 * @return Valor da chamada
	 */
	public int getValorChamada() 
	{
		return valorChamada;
	}

	/**
	 * @param valorChamada Valor da chamada
	 */
	public void setValorChamada(int valorChamada) 
	{
		this.valorChamada = valorChamada;
	}

	/**
	 * Retorna o Valor da chamada em formado double. 
	 * O resultado é o valor inteiro (cadastrado no banco) dividido do 10^5.
	 * 
	 * @return 
	 */
	public double getDblValorChamada()
	{
		return (double)valorChamada / 100000.0;
	}
	
	/**
	 * Grava o Valor da chamada em formado double. 
	 * O argumento é convertido para int fazendo-se a multiplicacao
	 * por 10^5 e arredondando-se o resultado.
	 */
	public void setDblValorChamada(double valorChamada)
	{
		this.valorChamada = (int)(valorChamada * 100000);
	}
	
	/**
	 * @return Valor da conexão
	 */
	public int getValorConexao() 
	{
		return valorConexao;
	}

	/**
	 * @param valorConexao Valor da conexão
	 */
	public void setValorConexao(int valorConexao) 
	{
		this.valorConexao = valorConexao;
	}
	
	/**
	 * Retorna o Valor de Conexao em formado double. 
	 * O resultado é o valor inteiro (cadastrado no banco) dividido do 10^5.
	 * 
	 * @return 
	 */
	public double getDblValorConexao()
	{
		return (double)valorConexao / 100000.0;
	}
	
	/**
	 * Grava o Valor de Conexao em formado double. 
	 * O argumento é convertido para int fazendo-se a multiplicacao
	 * por 10^5 e arredondando-se o resultado.
	 */
	public void setDblValorConexao(double valorConexao)
	{
		this.valorConexao = (int)(valorConexao * 100000);
	}

	/**
	 * @return Instancia de <code>ValorDeslocamento</code>
	 */
	public ValorDeslocamento getValorDeslocamento() 
	{
		return valorDeslocamento;
	}

	/**
	 * @param valorDeslocamento Instancia de <code>ValorDeslocamento</code>
	 */
	public void setValorDeslocamento(ValorDeslocamento valorDeslocamento) 
	{
		this.valorDeslocamento = valorDeslocamento;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof ValorChamada))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;		
		equal &= (this.idValorChamada == ((ValorChamada)obj).getIdValorChamada());
		equal &= isEqual(this.planoTarifacao, 	((ValorChamada)obj).getPlanoTarifacao());
		equal &= isEqual(this.operadoraLD, 		((ValorChamada)obj).getOperadoraLD());;
		return equal;
	}
	
	private boolean isEqual(Object obj1, Object obj2)
	{
		if (obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		if (obj1 == null && obj2 == null)
			return true;
		return false;
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.getIdValorChamada());
		if(planoTarifacao != null)	result.append(this.planoTarifacao.getIdPlanoTarifacao());
		if(operadoraLD != null)	result.append(this.operadoraLD.getCsp());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[ValorChamada]");
		result.append("ID=" + this.idValorChamada);
		if (planoTarifacao != null) result.append(";PLANO_TARIFACAO=" + this.planoTarifacao.getDesPlanoTarifacao());
		if (operadoraLD != null) result.append(";OPERADORA_LD=" + this.operadoraLD.getNomeOperadora());
		if (modulacao != null) result.append(";MODULACAO=" + this.modulacao.getDesModulacao());
		if (valorDeslocamento != null) result.append(";VALOR_DESLOCAMENTO=" + this.valorDeslocamento.getDesDeslocamento());
		result.append(";VALOR_CHAMADA=" + this.valorChamada);
		result.append(";VALOR_CONEXAO=" + this.valorConexao);
		if (tipoChamada != null) result.append(";TIPO_CHAMADA=" + this.tipoChamada.getDesOperacao());
		result.append(";DURACAO_CHAMADA=" + this.duracaoChamada);
		result.append(";NUM_SEGUNDOS_INICIAL=" + this.numSegundosInicial);
		result.append(";NUM_SEGUNDOS_PULSO=" + this.numSegundosPulso);
		if (area != null) result.append(";AREA=" + this.area);
		return result.toString();
	}

}
