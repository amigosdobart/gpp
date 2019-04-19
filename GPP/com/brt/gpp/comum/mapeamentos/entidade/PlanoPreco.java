package com.brt.gpp.comum.mapeamentos.entidade;

import java.util.Collection;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe que representa a entidade da tabela TBL_GER_PLANO_PRECO, que representa as informacoes referentes aos
 *	planos de preco dos assinantes.
 * 
 *	@author	Daniel Ferreira
 *	@since	28/03/2006
 *
 *  Atualizado por: Bernardo Vergne Dias
 *  Data: 14/02/2007
 *  
 *  Atualizado por: Vitor Murilo Dias
 *  data: 28/01/2008
 *  
 */
public class PlanoPreco implements Entidade
{

	private String	  idtPlanoPreco;
	private String	  desPlanoPreco;
	private String	  idtCategoriaAsap;
	private Integer   idtPlanoAnatel;
	private Categoria categoria;
	private String    idtPlanoPrecoSAG;
	private short     idtPlanoTarifarioPeriodico;
    private Collection motivosContestacao;
	
	/**
	 * Construtor da classe
	 */
	public PlanoPreco()
	{
		this.idtPlanoPreco				= null;
		this.desPlanoPreco				= null;
		this.categoria					= null;
		this.idtCategoriaAsap			= null;
		this.idtPlanoAnatel				= null;
		this.idtPlanoPrecoSAG			= null;
		this.idtPlanoTarifarioPeriodico = 0;
		
	}
	
	
	/**
	 *	Retorna o identificador do plano de preco.
	 * 
	 *	@return		String					idtPlanoPreco				Identificador do plano de preco.
	 */
	public String getIdtPlanoPreco() 
	{
		return this.idtPlanoPreco;
	}
	
	/**
	 *	Retorna a descricao do plano de preco.
	 * 
	 *	@return		String					desPlanoPreco				Descricao do plano de preco.
	 */
	public String getDesPlanoPreco() 
	{
		return this.desPlanoPreco;
	}
	
	/**
	 *	Retorna o identificador da categoria do plano de preco.
	 * 
	 *	@return		Integer					idtCategoria				Identificador da categoria do plano de preco.
	 */
	public Integer getIdtCategoria() 
	{
		return (this.categoria != null) ? new Integer(this.categoria.getIdCategoria()) : null;
	}
	
	/**
	 *	Retorna o identificador da categoria do ASAP do plano de preco.
	 * 
	 *	@return		String					idtCategoriaAsap			Identificador da categoria do ASAP do plano de preco.
	 */
	public String getIdtCategoriaAsap() 
	{
		return this.idtCategoriaAsap;
	}
	
	/**
	 *	Retorna o identificador do plano de preco para a Anatel.
	 * 
	 *	@return		Integer					idtPlanoAnatel				Identificador do plano de preco para a Anatel.
	 */
	public Integer getIdtPlanoAnatel() 
	{
		return this.idtPlanoAnatel;
	}
		
	/**
	 * @return Retorna o idtPlanoPrecoSAG.
	 */
	public String getIdtPlanoPrecoSAG()
	{
		return this.idtPlanoPrecoSAG;
	}
	
	/**
	 * @return Retorna o idtPlanoTarifarioPeriodico.
	 */
	public short getIdtPlanoTarifarioPeriodico()
	{
		return this.idtPlanoTarifarioPeriodico;
	}

	/**
	 *	Atribui o identificador do plano de preco.
	 * 
	 *	@param		String					idtPlanoPreco				Identificador do plano de preco.
	 */
	public void setIdtPlanoPreco(String idtPlanoPreco) 
	{
		this.idtPlanoPreco = idtPlanoPreco;
	}
	
	/**
	 *	Atribui a descricao do plano de preco.
	 * 
	 *	@param		String					desPlanoPreco				Descricao do plano de preco.
	 */
	public void setDesPlanoPreco(String desPlanoPreco) 
	{
		this.desPlanoPreco = desPlanoPreco;
	}
	
	/**
	 *	Atribui o identificador da categoria do plano de preco.
	 * 
	 *	@param		Integer					idtCategoria				Identificador da categoria do plano de preco.
	 */
	public void setIdtCategoria(Integer idtCategoria) 
	{
		this.categoria = new Categoria();
		this.categoria.setIdCategoria(idtCategoria.intValue());
	}
	
	/**
	 *	Atribui o identificador da categoria do ASAP do plano de preco.
	 * 
	 *	@param		String					idtCategoriaAsap			Identificador da categoria do ASAP do plano de preco.
	 */
	public void setIdtCategoriaAsap(String idtCategoriaAsap) 
	{
		this.idtCategoriaAsap = idtCategoriaAsap;
	}
	
	/**
	 *	Atribui o identificador do plano de preco para a Anatel.
	 * 
	 *	@param		Integer					idtPlanoAnatel				Identificador do plano de preco para a Anatel.
	 */
	public void setIdtPlanoAnatel(Integer idtPlanoAnatel) 
	{
		this.idtPlanoAnatel = idtPlanoAnatel;
	}

	/**
	 * @param idtPlanoPrecoSAG O idtPlanoPrecoSAG para alterar.
	 */
	public void setIdtPlanoPrecoSAG(String idtPlanoPrecoSAG)
	{
		this.idtPlanoPrecoSAG = idtPlanoPrecoSAG;
	}
	
	/**
	 * @param idtPlanoTarifarioPeriodico O idtPlanoTarifarioPeriodico para alterar.
	 */
	public void setIdtPlanoTarifarioPeriodico(short idtPlanoTarifarioPeriodico)
	{
		this.idtPlanoTarifarioPeriodico = idtPlanoTarifarioPeriodico;
	}	
    
    /**
     * @return lista de motivos de contestacao <code>MotivoContestacao</code>.
     */
	public Collection getMotivosContestacao()
    {
        return motivosContestacao;
    }

    /**
     * @param motivosContestacao Lista de <code>MotivoContestacao</code>
     */
    public void setMotivosContestacao(Collection motivosContestacao)
    {
        this.motivosContestacao = motivosContestacao;
    }

    /**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		PlanoPreco result = new PlanoPreco();		
		
		result.setIdtPlanoPreco(this.idtPlanoPreco);
		result.setDesPlanoPreco(this.desPlanoPreco);
		result.setIdtCategoriaAsap(this.idtCategoriaAsap);
		result.setIdtPlanoAnatel((this.idtPlanoAnatel != null) ? new Integer(this.idtPlanoAnatel.intValue()) : null);
		
		if (this.categoria != null)
		{
			result.categoria = new Categoria();
			result.categoria.setIdCategoria(this.categoria.getIdCategoria());
			result.categoria.setDesCategoria(this.categoria.getDesCategoria());
		}
		
		result.setIdtPlanoPrecoSAG(this.idtPlanoPrecoSAG);
		
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
		
		if(!(object instanceof PlanoPreco))
		{
			return false;
		}
		
		if(this.hashCode() != ((PlanoPreco)object).hashCode())
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
		result.append((this.idtPlanoPreco != null) ? this.idtPlanoPreco : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("Plano de Preco: ");
		result.append((this.idtPlanoPreco != null) ? this.idtPlanoPreco : "NULL");
		
		return result.toString();
	}

	/**
	 * @return Instancia de <code>Categoria</code>
	 */
	public Categoria getCategoria() 
	{
		return this.categoria;
	}

	/**
	 * @param categoria Instancia de <code>Categoria</code>
	 */
	public void setCategoria(Categoria categoria) 
	{
		this.categoria = categoria;
	}

	/**
	 * 	Indica se o plano do assinante aceita o formato do MSISDN informado.
	 * 
	 * 	@param		msisdn					MSISDN verificado.
	 *  @return		True se o plano aceita o MSISDN e false caso contrario.
	 */
	public boolean aceitaMsisdn(String msisdn)
	{
		if(this.categoria != null)
			return this.categoria.aceitaMsisdn(msisdn);
			
		return false;
	}
	
	/**
	 *	Indica se os assinantes do plano possuem IMSI.
	 *
	 *	@return		True se os assinantes do plano possuem IMSI e false caso contrario. 
	 */
	public boolean possuiImsi()
	{
		if(this.categoria != null)
			return this.categoria.possuiImsi();
		
		return false;
	}
	
	/**
	 *	Indica se os assinantes do plano possuem lista de Amigos Toda Hora.
	 *
	 *	@return		True se os assinantes do plano possuem lista de ATH e false caso contrario. 
	 */
	public boolean possuiAth()
	{
		if(this.categoria != null)
			return this.categoria.possuiAth();
		
		return false;
	}
	
	/**
	 * 	Indica se o plano do assinante possui o saldo informado.
	 * 
	 * 	@param		tipoSaldo				Informacoes do tipo de saldo.
	 *  @return		True se o plano possui o saldo e false caso contrario.
	 */
	public boolean possuiSaldo(TipoSaldo tipoSaldo)
	{
		if(this.categoria != null)
			return this.categoria.possuiSaldo(tipoSaldo);
			
		return false;
	}
	
	/**
	 * 	Indica se a origem de recarga e permitida para o plano de preco.
	 * 
	 * 	@param		origem					Origem de recarga.
	 *  @return		True se a origem e permitida e false caso contrario.
	 */
	public boolean isPermitida(OrigemRecarga origem)
	{
		if(this.categoria != null)
			return this.categoria.isPermitida(origem);
			
		return false;
	}
	
	/**
	 * 	Indica se a troca para o novo plano e permitida.
	 * 
	 * 	@param		plano					Novo plano de precos.
	 *  @return		True se a troca e permitida e false caso contrario.
	 */
	public boolean isTrocaPermitida(PlanoPreco plano)
	{
		if((this.categoria != null) && (plano != null))
			return this.categoria.isTrocaPermitida(plano.getCategoria());
		
		return false;
	}
	
	/**
	 * 	Verifica se o Plano de preco possui status periodico.
	 * 
	 *  @return
	 */
	public boolean possuiStatusPeriodico()
	{
		if((this.categoria != null))
			return this.categoria.possuiStatusPeriodico();
		
		return false;
	}
}
