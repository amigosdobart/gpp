package com.brt.gpp.aplicacoes.aprovisionar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Entidade que representa um saldo do assinante. Utilizado na implementacao da Tecnomen 4.4.5 para os saldos de 
 *	Torpedos e Dados. Os saldos de voz ainda estao definidos na tabela Subscriber e, consequentemente, na entidade 
 *	Assinante do GPP.
 *
 *	@author		Daniel Ferreira
 *	@since		06/03/2007
 */
public class SaldoAssinante 
{

	/**
	 *	MSISDN do assinante. 
	 */
	private String msisdn;
	
	/**
	 *	Informacoes referentes ao tipo de saldo. 
	 */
	private TipoSaldo tipo;
	
	/**
	 *	Quantidade de creditos no saldo. 
	 */
	private double creditos;
	
	/**
	 *	Data de expiracao do saldo. 
	 */
	private Date dataExpiracao;
	
	/**
	 *	Construtor da classe.
	 */
	public SaldoAssinante()
	{
		this.msisdn			= null;
		this.tipo			= null;
		this.creditos		= 0.0;
		this.dataExpiracao	= null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante;
	 * 	@param		tipo					Informacoes referentes ao tipo de saldo.
	 */
	public SaldoAssinante(String msisdn, TipoSaldo tipo)
	{
		this.msisdn			= msisdn;
		this.tipo			= tipo;
		this.creditos		= 0.0;
		this.dataExpiracao	= null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 * 	@param		tipo					Informacoes referentes ao tipo de saldo.
	 * 	@param		creditos				Quantidade de creditos no saldo.
	 * 	@param		dataExpiracao			Data de expiracao do saldo.
	 */
	public SaldoAssinante(String msisdn, TipoSaldo tipo, double creditos, Date dataExpiracao)
	{
		this(msisdn, tipo);
		
		this.creditos		= creditos;
		this.dataExpiracao	= dataExpiracao;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		MSISDN do assinante. 
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna as informacoes referentes ao tipo de saldo.
	 *
	 *	@return		Informacoes referentes ao tipo de saldo. 
	 */
	public TipoSaldo getTipo()
	{
		return this.tipo;
	}
	
	/**
	 *	Retorna a quantidade de creditos no saldo.
	 *
	 *	@return		Quantidade de creditos no saldo. 
	 */
	public double getCreditos()
	{
		return this.creditos;
	}
	
	/**
	 *	Retorna a data de expiracao do saldo.
	 *
	 *	@return		Data de expiracao do saldo. 
	 */
	public Date getDataExpiracao()
	{
		return this.dataExpiracao;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		msisdn					MSISDN do assinante. 
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui a quantidade de creditos no saldo.
	 *
	 *	@param		creditos				Quantidade de creditos no saldo. 
	 */
	public void setCreditos(double creditos)
	{
		this.creditos = creditos;
	}
	
	/**
	 *	Atribui a data de expiracao do saldo.
	 *
	 *	@param		dataExpiracao			Data de expiracao do saldo. 
	 */
	public void setDataExpiracao(Date dataExpiracao)
	{
		this.dataExpiracao = dataExpiracao;
	}
	
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoSaldo tipo) 
	{
		this.tipo = tipo;
	}
	
	/**
	 * 	Constroi e retorna uma lista de saldos de um assinante. Utilizado no processo de ativacao, onde os saldos do 
	 * 	assinante devem ser ativados. Os saldos dependem da categoria a qual o plano do assinante pertence. Por 
	 * 	exemplo, assinantes LigMix poderao ter somente o saldo principal ativado. Assinantes GSM devem ter os saldos 
	 * 	Principal, Bonus, Torpedos e Dados ativados.
	 * 
	 *	@param		assinante				Informacoes do assinante.
	 *  @return		Lista de saldos do assinante.
	 */
	public static Collection newSaldosAssinante(Assinante assinante)
	{
		ArrayList result = new ArrayList();
		
		//Obtendo a categoria do plano de preco definido para o assinante. A categoria possui a lista de saldos 
		//permitidos para o assinante durante a ativacao. 
		Categoria categoria = MapPlanoPreco.getInstancia().getPlanoPreco(assinante.getPlanoPreco()).getCategoria();
		
		for(Iterator iterator = categoria.getTiposSaldo().iterator(); iterator.hasNext();)
		{
			TipoSaldo tipoSaldo = ((SaldoCategoria)iterator.next()).getTipoSaldo();
			
			//Para que o saldo seja ativado, e necessario determinar se esta disponivel para ativacao. Isto porque a 
			//API da Tecnomen nao separou completamente os saldos do assinante. Os saldos de voz encontram-se na 
			//PP_DB..Subscriber e suas informacoes ainda devem ser definidas entre os detalhes do assinante (veja 
			//AssinanteHolder).
			if(tipoSaldo.isDisponivelAtivacao())
				result.add(assinante.getSaldo(tipoSaldo));
		}
		return result;
	}
	/**
	 * Gera uma string com as informacoes do saldo
	 */
	public String toString()
	{
		return "MSISDN:"+this.getMsisdn()+";Tipo:"+this.getTipo()+";Creditos:"+this.getCreditos();
	}
}
