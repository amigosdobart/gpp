package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;

/**
 *	Entidade responsavel pelas informacoes do detalhamento do extrato Pula-Pula de um assinante.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/03/2008
 *	@modify		Primeira versao.
 */
public class Detalhe implements Comparable
{

	/**
	 *	Indicador que o detalhe refere-se a um evento. Os detalhes podem ser, alem de chamadas, eventos tambem. 
	 *	Existem casos em que o detalhe refere-se a uma chamada, a um evento (ex: status da promocao do assinante 
	 *	invalido) ou a uma chamada e a um evento ao mesmo tempo (ex: limite ultrapassado). 
	 */
	private boolean indEvento;
	
	/**
	 *	Numero originador da chamada. 
	 */
	private String originador;
	
	/**
	 *	Data e hora da chamada.
	 */
	private Date timestamp;
	
	/**
	 *	Descricao da chamada. 
	 */
	private String descricao;
	
	/**
	 *	Desconto da promocao Pula-Pula aplicado a chamada. 
	 */
	private DescontoPulaPula desconto;
	
	/**
	 *	Duracao da chamada. 
	 */
	private long duracao;
	
	/**
	 *	Valor de bonus aplicado a chamada. 
	 */
	private double bonus;
	
	/**
	 *	Construtor da classe.
	 */
	public Detalhe()
	{
		this.indEvento	= false;
		this.originador	= "";
		this.timestamp	= null;
		this.descricao	= "";
		this.desconto	= null;
		this.duracao	= 0;
		this.bonus		= 0.0;
	}
	
	/**
	 *	Indica se o detalhe refere-se a um evento.
	 *
	 *	@return		True se o detalhe refere-se a um evento e false caso contrario.
	 */
	public boolean isEvento()
	{
		return this.indEvento;
	}
	
	/**
	 *	Retorna o numero originador da chamada.
	 *
	 *	@return		Numero originador da chamada.
	 */
	public String getOriginador()
	{
		return this.originador;
	}
	
	/**
	 *	Retorna a data e hora da chamada.
	 *
	 *	@return		Data e hora da chamada.
	 */
	public Date getTimestamp()
	{
		return this.timestamp;
	}
	
	/**
	 *	Retorna a descricao da chamada.
	 *
	 *	@return		Descricao da chamada.
	 */
	public String getDescricao()
	{
		return this.descricao;
	}
	
	/**
	 *	Retorna o desconto da promocao Pula-Pula aplicado a chamada.
	 *
	 *	@return		Desconto da promocao Pula-Pula aplicado a chamada.
	 */
	public DescontoPulaPula getDesconto()
	{
		return this.desconto;
	}
	
	/**
	 *	Retorna a duracao da chamada.
	 *
	 *	@return		Duracao da chamada.
	 */
	public long getDuracao()
	{
		return this.duracao;
	}
	
	/**
	 *	Retorna o valor de bonus aplicado a chamada.
	 *
	 *	@return		Valor de bonus aplicado a chamada.
	 */
	public double getBonus()
	{
		return this.bonus;
	}

	/**
	 *	Atribui o indicador de detalhe referente a um evento.
	 *
	 *	@param		indEvento				Indicador de detalhe referente a um evento.
	 */
	public void setIndEvento(boolean indEvento)
	{
		this.indEvento = indEvento;
	}
	
	/**
	 *	Atribui o numero originador da chamada.
	 *
	 *	@param		originador				Numero originador da chamada.
	 */
	public void setOriginador(String originador)
	{
		this.originador = originador;
	}
	
	/**
	 *	Atribui a data e hora da chamada.
	 *
	 *	@param		timestamp				Data e hora da chamada.
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
	
	/**
	 *	Atribui a descricao da chamada.
	 *
	 *	@param		descricao				Descricao da chamada.
	 */
	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}
	
	/**
	 *	Atribui o desconto da promocao Pula-Pula aplicado a chamada.
	 *
	 *	@param		desconto				Desconto da promocao Pula-Pula aplicado a chamada.
	 */
	public void setDesconto(DescontoPulaPula desconto)
	{
		this.desconto = desconto;
	}
	
	/**
	 *	Atribui a duracao da chamada.
	 *
	 *	@param		Duracao da chamada.
	 */
	public void setDuracao(long duracao)
	{
		this.duracao = duracao;
	}
	
	/**
	 *	Adiciona a duracao da chamada.
	 *
	 *	@param		Duracao da chamada.
	 */
	public void addDuracao(long duracao)
	{
		this.duracao += duracao;
	}
	
	/**
	 *	Atribui o valor de bonus aplicado a chamada.
	 *
	 *	@param		Valor de bonus aplicado a chamada.
	 */
	public void setBonus(double bonus)
	{
		this.bonus = bonus;
	}
	
	/**
	 *	Adiciona o valor de bonus aplicado a chamada.
	 *
	 *	@param		Valor de bonus aplicado a chamada.
	 */
	public void addBonus(double bonus)
	{
		this.bonus += bonus;
	}
	
	/**
	 *	Extrai as informacoes da lista de objetos e retorna uma lista de objetos Detalhe.
	 *	
	 *	@param		objs					Lista de objetos.
	 *	@return		Lista de objetos detalhe.
	 *	@throws		GPPInternalErrorException, ClassCastException
	 */
	public static Collection extractAll(Collection objs) throws GPPInternalErrorException
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = objs.iterator(); iterator.hasNext();)
		{
			Object obj = iterator.next();
			
			if(obj instanceof Detalhe)
				result.add(obj);
			else if(obj instanceof Recarga)
				result.add(Detalhe.extract((Recarga)obj));
			else if(obj instanceof PromocaoStatusAssinante)
				result.add(Detalhe.extract((PromocaoStatusAssinante)obj));
			else
				throw new ClassCastException();
		}
			
		return result;
	}
	
	/**
	 *	Extrai as informacoes do objeto Recarga e retorna um objeto Detalhe.
	 *	
	 *	@param		recarga					Informacoes de recarga, ajuste ou bonus.
	 *	@return		Objeto detalhe contendo as informacoes da recarga.
	 *	@throws		GPPInternalErrorException
	 */
	public static Detalhe extract(Recarga recarga) throws GPPInternalErrorException
	{
		Detalhe result = new Detalhe();
		
		result.setIndEvento(true);
		result.setTimestamp(recarga.getDatOrigem());
		result.setDescricao(MapRecOrigem.getInstance().getMapDescRecOrigem(recarga.getTipTransacao()));
		
		double	valorPeriodico	= (recarga.getVlrCreditoPeriodico() != null) ? 
									recarga.getVlrCreditoPeriodico().doubleValue() : 0.0;
		double	valorBonus		= (recarga.getVlrCreditoBonus() != null) ? 
									recarga.getVlrCreditoBonus().doubleValue() : 0.0;
		result.setBonus(valorPeriodico + valorBonus);
		
		return result;
	}
	
	/**
	 *	Extrai as informacoes do objeto PromocaoStatusAssinante e retorna um objeto Detalhe.
	 *	
	 *	@param		status					Informacoes da promocao.
	 *	@return		Objeto detalhe contendo as informacoes do status do assinante.
	 */
	public static Detalhe extract(PromocaoStatusAssinante status)
	{
		Detalhe result = new Detalhe();
		
		result.setIndEvento(true);
		result.setDescricao(status.getDesStatus());
		
		return result;
	}
	
	/**
	 *	@see		java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object obj)
	{
		return this.getTimestamp().compareTo(((Detalhe)obj).getTimestamp());
	}
	
}
