package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 *	Entidade <code>Categoria</code>. Referência: TBL_GER_CATEGORIA
 * 
 *	@version	1.0
 *	@author		Bernardo Vergne Dias
 *	Criado em	14/02/2007
 */
public class Categoria implements Serializable, Comparable
{
	private int idCategoria;
	private String desCategoria;
	private Collection planos;
	
	/**
	 *	Identificador do tipo de assinantes cadastrados em plano da categoria e utilizado pela Tecnomen. 
	 *	Este valor e necessario para os processos de aprovisionamento.
	 */
	private CategoriaTEC categoriaTEC;
	
	/**
	 *	Mascara representando os formatos de MSISDN's que a categoria aceita. 
	 */
	private String desMascaraMsisdn;
	
	/**
	 *	Indicador de que os assinantes da categoria possuem IMSI. 
	 */
	private boolean indPossuiImsi;
	
	/**
	 *	Indicador de que os assinantes da categoria possuem lista de Amigos Toda Hora. 
	 */
	private boolean indPossuiAth;
	
	/**
	 *	Identificador do status periodico inicial, definido para o assinante durante o processo de ativacao. 
	 */
	private short idtStatusPeriodicoAtivacao;
	
	/**
	 *	Lista de informacoes referentes aos tipos de saldo que assinantes da categoria podem ter. 
	 */
	private Collection tiposSaldo;
	
	/**
	 *	Lista de servicos da Tecnomen que assinantes da categoria podem ter. 
	 */
	private Collection servicosTecnomen;
	
	/**
	 *	Lista de origens de recarga permitidas para a categoria. 
	 */
	private Collection origensRecarga;
	
	/**
	 *	Lista de categorias aceitas pela categoria no processo de plano de preco. Utilizado para evitar situacoes em 
	 *	que a troca de plano pode atribuir ao assinante uma categoria nao permitida (Ex: Assinante GSM trocando para 
	 *	plano LigMix). 
	 */
	private Collection novasCategorias;
	
	/**
	 *	Indicador de que os assinantes da categoria possuem Status Periodico. 
	 */
	private boolean indPossuiStatusPeriodico;
	
	/**
	 *	Construtor da classe.
	 */
	public Categoria()
	{
		this.idCategoria		= -1;
		this.desCategoria		= null;
		this.categoriaTEC 		= null;
		this.desMascaraMsisdn	= null;
		this.indPossuiImsi		= false;
		this.indPossuiAth		= false;
		this.tiposSaldo			= null;
		this.servicosTecnomen	= null;
		this.origensRecarga		= null;
		this.novasCategorias	= null;
		this.indPossuiStatusPeriodico = false;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idCategoria				ID da categoria.
	 *	@param		desCategoria			Descricao da categoria.
	 *	@param		categoriaTEC			Correspondente Tecnomen da categoria.
	 *	@param		desMascaraMsisdn		Mascara representando os formatos de MSISDN's que a categoria aceita.
	 *	@param		indPossuiImsi			Indicador de que os assinantes da categoria possuem IMSI.
	 *	@param		indPossuiAth			Indicador de que os assinantes da categoria possuem lista de Amigos Toda Hora.
	 *	@param		idtStatusPeriodicoAtiv	Identificador do status periodico inicial.
	 *	@param		tiposSaldo				Lista de tipos de saldo.
	 *	@param		servicosTecnomen		Lista de servicos da Tecnomen associados a categoria.
	 *	@param		origensRecarga			Lista de origens de recarga permitidas para a categoria.
	 *	@param		novasCategorias			Lista de novas categorias permitidas para o assinante durante um processo
	 *										de troca de plano.
	 */
	public Categoria(int			idCategoria, 
					 String			desCategoria,
					 CategoriaTEC	categoriaTEC,
					 String			desMascaraMsisdn,
					 boolean		indPossuiImsi,
					 boolean		indPossuiAth,
					 short 			idtStatusPeriodicoAtivacao,
					 Collection		tiposSaldo,
					 Collection		servicosTecnomen,
					 Collection		origensRecarga,
					 Collection		novasCategorias,
					 boolean		indPossuiStatusPeriodico
)
	{
		this.idCategoria				= idCategoria;
		this.desCategoria				= desCategoria;
		this.categoriaTEC				= categoriaTEC;
		this.desMascaraMsisdn			= desMascaraMsisdn;
		this.indPossuiImsi				= indPossuiImsi;
		this.indPossuiAth				= indPossuiAth;
		this.idtStatusPeriodicoAtivacao	= idtStatusPeriodicoAtivacao;
		this.tiposSaldo					= tiposSaldo;
		this.servicosTecnomen			= servicosTecnomen;
		this.origensRecarga				= origensRecarga;
		this.novasCategorias			= novasCategorias;
		this.indPossuiStatusPeriodico	= indPossuiStatusPeriodico;
	}
	
	/**
	 * @return ID da categoria
	 */
	public int getIdCategoria() 
	{
		return this.idCategoria;
	}
	
	/**
	 * @param categoria ID da categoria
	 */
	public void setIdCategoria(int categoria) 
	{
		this.idCategoria = categoria;
	}
	
	/**
	 * @return Descricao da categoria
	 */
	public String getDesCategoria() 
	{
		return this.desCategoria;
	}
	
	/**
	 * @param desCategoria Descricao da categoria
	 */
	public void setDesCategoria(String desCategoria) 
	{
		this.desCategoria = desCategoria;
	}

	/**
	 *	Retorna o identificador do tipo de assinantes.
	 *
	 * 	@return		Identificador do tipo de assinantes. 
	 */
	public CategoriaTEC getCategoriaTEC()
	{
		return this.categoriaTEC;
	}
	
	/**
	 *	Define o identificador do tipo de assinantes.
	 *
	 * 	@param categoriaTEC		Identificador do tipo de assinantes. 
	 */
	public void setCategoriaTEC(CategoriaTEC categoriaTEC)
	{
		this.categoriaTEC = categoriaTEC;
	}
	
	/**
	 *	Retorna a ascara representando os formatos de MSISDN's que a categoria aceita.
	 *
	 * 	@return		Mascara representando os formatos de MSISDN's que a categoria aceita. 
	 */
	public String getDesMascaraMsisdn()
	{
		return this.desMascaraMsisdn;
	}
	
	/**
	 *	Indica se os assinantes da categoria possuem IMSI.
	 *
	 *	@return		True se os assinantes da categoria possuem IMSI e false caso contrario. 
	 */
	public boolean possuiImsi()
	{
		return this.indPossuiImsi;
	}
	
	/**
	 *	Indica se os assinantes da categoria possuem lista de Amigos Toda Hora.
	 *
	 *	@return		True se os assinantes da categoria possuem lista de ATH e false caso contrario. 
	 */
	public boolean possuiAth()
	{
		return this.indPossuiAth;
	}
	
	/**
	 *	Retorna o identificador do status periodico inicial.
	 *
	 *	@return		Identificador do status periodico inicial. 
	 */
	public short getIdtStatusPeriodicoAtivacao()
	{
		return this.idtStatusPeriodicoAtivacao;
	}
	
	/**
	 *	Retorna a lista de tipos de saldo.
	 *
	 * 	@return		Lista de tipos de saldo associados a categoria. 
	 */
	public Collection getTiposSaldo()
	{
		return this.tiposSaldo;
	}
	
	/**
	 *	Retorna a lista de servicos a serem ativados na Tecnomen e associados a categoria.
	 *
	 * 	@return		Lista de servicos. 
	 */
	public Collection getServicosTecnomen()
	{
		return servicosTecnomen;
	}
	
	public void setServicosTecnomen(Collection servicosTecnomen)
	{
		this.servicosTecnomen = servicosTecnomen;
	}
	
	/**
	 *	Retorna a lista de origens de recarga permitidas para a categoria.
	 *
	 *	@return		Lista de origens de recarga permitidas para a categoria. 
	 */
	public Collection getOrigensRecarga()
	{
		return this.origensRecarga;
	}
	
	/**
	 *	Retorna a lista de categorias permitidas no processo de troca de plano.
	 *
	 *	@return		Lista de categorias permitidas. 
	 */
	public Collection getNovasCategorias()
	{
		return this.novasCategorias;
	}
	
	/**
	 * @return Coleção de <code>PlanoPreco</code>
	 */
	public Collection getPlanos() 
	{
		return this.planos;
	}

	/**
	 * @param planos Coleção de <code>PlanoPreco</code>
	 */
	public void setPlanos(Collection planos) 
	{
		this.planos = planos;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Categoria))
			return false;
		
		if (obj == this)
			return true;

		return this.getIdCategoria() == ((Categoria)obj).getIdCategoria();
	}
	
	public int hashCode() 
	{
		return this.idCategoria;
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Categoria]");
		result.append("ID=" + this.idCategoria);
		if (this.desCategoria != null)
			result.append(";DESCRICAO=" + this.desCategoria);
	
		return result.toString();
	}

	/**
	 *	Indica se o MSISDN informado e aceito pela categoria, ou seja, se corresponde a sua mascara.
	 *
	 * 	@return		True se o MSISDN e aceito e false caso contrario. 
	 */
	public boolean aceitaMsisdn(String msisdn)
	{
		if((this.desMascaraMsisdn == null) || (msisdn == null))
			return false;
		
		return msisdn.matches(this.desMascaraMsisdn);
	}
	
	/**
	 * 	Indica se a categoria do plano do assinante possui o saldo informado.
	 * 
	 * 	@param		tipoSaldo				Informacoes do tipo de saldo.
	 *  @return		True se a categoria possui o saldo e false caso contrario.
	 */
	public synchronized boolean possuiSaldo(TipoSaldo tipoSaldo)
	{
		for(Iterator iterator = this.getTiposSaldo().iterator(); iterator.hasNext();)
			if(((SaldoCategoria)iterator.next()).getTipoSaldo().equals(tipoSaldo))
				return true;
		
		return false;
	}
	
	/**
	 * 	Indica se a origem de recarga e permitida para a categoria.
	 * 
	 * 	@param		origem					Origem de recarga.
	 *  @return		True se a origem e permitida e false caso contrario.
	 */
	public synchronized boolean isPermitida(OrigemRecarga origem)
	{
		if((this.origensRecarga == null) || (origem == null))
			return false;
		
		return this.origensRecarga.contains(origem);
	}
	
	/**
	 * 	Indica se a troca para a nova categoria e permitida.
	 * 
	 * 	@param		categoria				Nova categoria de planos de preco.
	 *  @return		True se a troca e permitida e false caso contrario.
	 */
	public synchronized boolean isTrocaPermitida(Categoria categoria)
	{
		if((this.novasCategorias == null) || (categoria == null))
			return false;
		
		return this.novasCategorias.contains(categoria);
	}

	/**
	 * @return the indPossuiAth
	 */
	public boolean isIndPossuiAth() 
	{
		return this.indPossuiAth;
	}

	/**
	 * @param indPossuiAth the indPossuiAth to set
	 */
	public void setIndPossuiAth(boolean indPossuiAth) 
	{
		this.indPossuiAth = indPossuiAth;
	}

	/**
	 * @param tiposSaldo the tiposSaldo to set
	 */
	public void setTiposSaldo(Collection tiposSaldo) 
	{
		this.tiposSaldo = tiposSaldo;
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof Categoria))
			throw new IllegalArgumentException();
		
		return this.getIdCategoria() - ((Categoria)obj).getIdCategoria();
	}

	/**
	 * @param origensRecarga the origensRecarga to set
	 */
	public void setOrigensRecarga(Collection origensRecarga) 
	{
		this.origensRecarga = origensRecarga;
	}

	/**
	 * @param desMascaraMsisdn the desMascaraMsisdn to set
	 */
	public void setDesMascaraMsisdn(String desMascaraMsisdn) 
	{
		this.desMascaraMsisdn = desMascaraMsisdn;
	}

	/**
	 * @return the indPossuiImsi
	 */
	public boolean isIndPossuiImsi() 
	{
		return this.indPossuiImsi;
	}

	/**
	 * @param indPossuiImsi the indPossuiImsi to set
	 */
	public void setIndPossuiImsi(boolean indPossuiImsi) 
	{
		this.indPossuiImsi = indPossuiImsi;
	}

	/**
	 * Se a categoria possui o status periodico
	 * @return
	 */
	public boolean possuiStatusPeriodico() {
		return this.indPossuiStatusPeriodico;
	}

	/**
	 * Ajusta o indice que configura o status periodico
	 * @param indPossuiStatusPeriodico
	 */
	public void setIndPossuiStatusPeriodico(boolean indPossuiStatusPeriodico) {
		this.indPossuiStatusPeriodico = indPossuiStatusPeriodico;
	}
}
