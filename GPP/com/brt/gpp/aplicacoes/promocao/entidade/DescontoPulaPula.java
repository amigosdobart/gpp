package com.brt.gpp.aplicacoes.promocao.entidade;

/**
 *	Entidade da tabela TBL_PRO_DESCONTO_PULA_PULA, responsavel pela definicao dos varios tipos de desconto do
 *	bonus Pula-Pula aplicado as chamadas.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/03/2008
 *	@modify		Primeira versao.
 */
public class DescontoPulaPula implements Comparable
{

	/**
	 *	Constante referente a ligacao normal, sem aplicacao de descontos.
	 */
	public static final short NORMAL = 0;

	/**
	 *	Constante referente ao desconto de ligacao com tarifacao Amigos Toda Hora.
	 */
	public static final short ATH = 1;

	/**
	 *	Constante referente ao desconto de ligacao com tarifacao reduzida por plano noturno.
	 */
	public static final short NOTURNO = 2;

	/**
	 *	Constante referente ao desconto de ligacao com tarifacao reduzida por plano diurno.
	 */
	public static final short DIURNO = 3;

	/**
	 *	Constante referente ao desconto de ligacao com originador na Black List.
	 */
	public static final short BLACK_LIST = 4;

	/**
	 *	Constante referente ao desconto de ligacao por duracao excedida.
	 */
	public static final short DURACAO_EXCEDIDA = 5;

	/**
	 *	Constante referente ao desconto de ligacao indevida expurgada.
	 */
	public static final short EXPURGO = 6;

	/**
	 *	Constante referente ao desconto de ligacao indevida estornada.
	 */
	public static final short ESTORNO = 7;

	/**
	 *	Constante referente ao desconto de ligacao originada por orelhao e com duracao acima de 15 minutos.
	 */
	public static final short TUP = 8;
	
	/**
	 *	Constante referente ao desconto de ligacao com origem igual ao destino.
	 */
	public static final short AIGUALB = 9;
	
	/**
	 *	Constante referente ao desconto de ligacao com o numero originador zerado.
	 */
	public static final short TITULARIDADE_TRANSFERIDA = 10;
	
	/**
	 *	Constante referente ao desconto de ligacao com tarifacao Amigos Toda Hora 2.
	 */
	public static final short ATH2 = 11;

	/**
	 *	Constante referente ao desconto de ligacao originada por movel nao BrT (Off-Net).
	 */
	public static final short MOVEL_OFFNET = 12;

	/**
	 *	Constante referente ao desconto de ligacao originada por Fale de Graca em horario promocional.
	 */
	public static final short FALE_GRACA = 13;
	
	/**
	 *	Constante referente ao desconto de ligacao originada com utilizacao de bonus.
	 */
	public static final short BONUS = 14;
	
	/**
	 *	Constante referente ao desconto de ligacao com tarifacao de originador Controle Total.
	 */
	public static final short CT = 15;

	/**
	 *	Constante referente ao desconto de ligacao com tarifacao de originador na promocao Amigos de Graca.
	 */
	public static final short AMIGOS_GRACA = 16;

	/**
	 *	Identificador do desconto.
	 */
	private short idDesconto;

	/**
	 *	Descricao do desconto.
	 */
	private String desDesconto;

	/**
	 *	Indicador de chamada disponivel para consulta no Extrato Pula-Pula.
	 */
	private boolean indDisponivelExtrato;

	/**
	 *	Indicador de chamada cujo bonus foi retirado por algum motivo. Utilizado para evitar o processamento da
	 *	chamada pelo processo de Expurgo/Estorno de bonus Pula-Pula.
	 */
	private boolean indEstorno;

	/**
	 *	Construtor da classe.
	 */
	public DescontoPulaPula()
	{
		this.idDesconto 			= -1;
		this.desDesconto			= null;
		this.indDisponivelExtrato	= false;
		this.indEstorno				= false;
	}

	/**
	 *	Retorna o identificador do desconto.
	 *
	 *	@return		Identificador do desconto.
	 */
	public short getIdDesconto()
	{
		return this.idDesconto;
	}

	/**
	 *	Retorna a descricao do desconto.
	 *
	 *	@return		Descricao do desconto.
	 */
	public String getDesDesconto()
	{
		return this.desDesconto;
	}

	/**
	 *	Retorna o indicador de chamada disponivel para consulta no Extrato Pula-Pula.
	 *
	 *	@return		Indicador de chamada disponivel para consulta no Extrato Pula-Pula.
	 */
	public boolean getIndDisponivelExtrato()
	{
		return this.indDisponivelExtrato;
	}

	/**
	 *	Indica se a chamada esta disponivel para consulta no Extrato Pula-Pula.
	 *
	 *	@return		True se a chamada esta disponivel e false caso contrario.
	 */
	public boolean isDisponivelExtrato()
	{
		return this.indDisponivelExtrato;
	}

	/**
	 *	Retorna o indicador de bonus da chamada retirado por algum motivo.
	 *
	 *	@return		Indicador de bonus da chamada retirado por algum motivo.
	 */
	public boolean getIndEstorno()
	{
		return this.indEstorno;
	}

	/**
	 *	Indica se o bonus da chamada foi retirado por algum motivo.
	 *
	 *	@return		True se o bonus foi retirado e false caso contrario.
	 */
	public boolean isEstorno()
	{
		return this.indEstorno;
	}

	/**
	 *	Atribui o identificador do desconto.
	 *
	 *	@return		idDesconto				Identificador do desconto.
	 */
	public void setIdDesconto(short idDesconto)
	{
		this.idDesconto = idDesconto;
	}

	/**
	 *	Atribui a descricao do desconto.
	 *
	 *	@return		desDesconto				Descricao do desconto.
	 */
	public void setDesDesconto(String desDesconto)
	{
		this.desDesconto = desDesconto;
	}

	/**
	 *	Atribui o indicador de chamada disponivel para consulta no Extrato Pula-Pula.
	 *
	 *	@param		indDisponivelExtrato	Indicador de chamada disponivel para consulta no Extrato Pula-Pula.
	 */
	public void setIndDisponivelExtrato(boolean indDisponivelExtrato)
	{
		this.indDisponivelExtrato = indDisponivelExtrato;
	}

	/**
	 *	Atribui o indicador de chamada cujo bonus foi retirado por algum motivo.
	 *
	 *	@param		indEstorno				Indicador de chamada cujo bonus foi retirado por algum motivo.
	 */
	public void setIndEstorno(boolean indEstorno)
	{
		this.indEstorno = indEstorno;
	}

	/**
	 *	Indica se tipo de bonificacao aplica-se ao desconto.
	 *
	 *	@param		tipoBonficacao			Tipo de bonificacao do Pula-Pula.
	 *	@return		True se o tipo de bonificacao e aplicavel e false caso contrario.
	 */
	public boolean matches(PromocaoTipoBonificacao tipoBonificacao)
	{
		switch(tipoBonificacao.getIdTipoBonificacao())
		{
			case PromocaoTipoBonificacao.PULA_PULA_UNICO:
				return true;
			case PromocaoTipoBonificacao.PULA_PULA_ONNET:
				return (this.getIdDesconto() != DescontoPulaPula.MOVEL_OFFNET);
			case PromocaoTipoBonificacao.PULA_PULA_OFFNET:
				return (this.getIdDesconto() == DescontoPulaPula.MOVEL_OFFNET);
			default: return false;
		}
	}

	public boolean isOffNet()
	{
		return (this.getIdDesconto() == DescontoPulaPula.MOVEL_OFFNET);
	}

	/**
	 *	@see		java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object obj)
	{
		return this.getIdDesconto() - ((DescontoPulaPula)obj).getIdDesconto();
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
		return (this.getClass().getName() + "||" + this.getIdDesconto()).hashCode();
	}

	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Desconto: " + this.getDesDesconto();
	}

}
