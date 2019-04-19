package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 *	Classe que representa a entidade da tabela TBL_REC_SISTEMA_ORIGEM.
 *
 *	@version	1.0		29/03/2006		Primeira versao.
 *	@author		Daniel Ferreira
 *
 *	@version	1.1		30/04/2007		Inclusao de lista de origens de recarga permitidas para o sistema de origem
 *										para validacao de recargas e ajustes.
 *	@author		Daniel Ferreira
 */
public class SistemaOrigem implements Serializable
{

	/**
	 *	Identificador do sistema de origem.
	 */
	private String idSistemaOrigem;
	
	/**
	 *	Descricao do sistema de origem.
	 */
	private String desSistemaOrigem;
	
	/**
	 *	Indicacao de que o sistema de origem realiza validacao de utilizacao de cartao de credito.
	 */
	private boolean	indValidaCc;
	
	/**
	 *	Indicacao de que o sistema de origem realiza validacao de saldo maximo.
	 */
	private boolean	indValidaSaldoMaximo;
	
	/**
	 *	Lista de origens de recarga permitidas ao sistema de origem.
	 */
	private Collection listaOrigens;
	
	public SistemaOrigem ()
	{
		
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idSistemaOrigem			Identificador do sistema de origem.
	 *	@param		desSistemaOrigem		Descricao do sistema de origem.
	 *	@param		indValidaCc				Indicacao de que o sistema de origem valida cartao de credito.
	 *	@param		indValidaSaldoMaximo	Indicacao de que o sistema de origem valida saldo maximo.
	 *	@param		listaOrigens			Lista de origens de recarga permitidas ao sistema de origem.
	 */
	public SistemaOrigem(String idSistemaOrigem, 
						 String desSistemaOrigem, 
						 boolean indValidaCc, 
						 boolean indValidaSaldoMaximo,
						 Collection listaOrigens)
	{
		this.idSistemaOrigem		= idSistemaOrigem;
		this.desSistemaOrigem		= desSistemaOrigem;
		this.indValidaCc			= indValidaCc;
		this.indValidaSaldoMaximo	= indValidaSaldoMaximo;
		this.listaOrigens			= listaOrigens;
	}
	
	/**
	 *	Retorna o identificador do sistema de origem.
	 * 
	 *	@return		Identificador do sistema de origem.
	 */
	public String getIdSistemaOrigem() 
	{
		return this.idSistemaOrigem;
	}
	
	/**
	 *	Retorna a descricao do sistema de origem.
	 * 
	 *	@return		Descricao do sistema de origem.
	 */
	public String getDesSistemaOrigem() 
	{
		return this.desSistemaOrigem;
	}
	
	/**
	 *	Indica se e feita a validacao de cartao de credito para o sistema de origem.
	 * 
	 *	@return		True se e feita a validacao e false caso contrario.
	 */
	public boolean validaCc()
	{
	    return this.indValidaCc;
	}
	
	/**
	 *	Indica se e feita a validacao de saldo maximo para o sistema de origem.
	 * 
	 *	@return		True se e feita a validacao e false caso contrario.
	 */
	public boolean validaSaldoMaximo()
	{
	    return this.indValidaSaldoMaximo;
	}
	
	/**
	 *	Retorna a lista de origens de recarga permitidas ao sistema de origem.
	 *
	 *	@return		Lista de origens de recarga permitidas ao sistema de origem.
	 */
	public Collection getListaOrigens()
	{
		return this.listaOrigens;
	}

	/**
	 *	Indica se a origem de recarga informada e permitida para o sistema de origem.
	 * 
	 *	@return		True se a origem e permitida e false caso contrario.
	 */
	public synchronized boolean isPermitida(OrigemRecarga origem)
	{
		if((this.listaOrigens == null) || (origem == null))
			return false;
		
		for(Iterator iterator = this.listaOrigens.iterator(); iterator.hasNext();)
			if(origem.equals(iterator.next()))
				return true;
			
		return false;
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("Sistema de Origem: ");
		result.append((this.idSistemaOrigem != null) ? this.idSistemaOrigem : "NULL");
		result.append(" - ");
		result.append("Descricao: ");
		result.append((this.desSistemaOrigem != null) ? this.desSistemaOrigem : "NULL");
		
		return result.toString();
	}

	/**
	 * @return the indValidaCc
	 */
	public boolean isIndValidaCc() 
	{
		return indValidaCc;
	}

	/**
	 * @param indValidaCc the indValidaCc to set
	 */
	public void setIndValidaCc(boolean indValidaCc) 
	{
		this.indValidaCc = indValidaCc;
	}

	/**
	 * @return the indValidaSaldoMaximo
	 */
	public boolean isIndValidaSaldoMaximo() 
	{
		return indValidaSaldoMaximo;
	}

	/**
	 * @param indValidaSaldoMaximo the indValidaSaldoMaximo to set
	 */
	public void setIndValidaSaldoMaximo(boolean indValidaSaldoMaximo) 
	{
		this.indValidaSaldoMaximo = indValidaSaldoMaximo;
	}

	/**
	 * @param desSistemaOrigem the desSistemaOrigem to set
	 */
	public void setDesSistemaOrigem(String desSistemaOrigem) 
	{
		this.desSistemaOrigem = desSistemaOrigem;
	}

	/**
	 * @param idSistemaOrigem the idSistemaOrigem to set
	 */
	public void setIdSistemaOrigem(String idSistemaOrigem) 
	{
		this.idSistemaOrigem = idSistemaOrigem;
	}

	/**
	 * @param listaOrigens the listaOrigens to set
	 */
	public void setListaOrigens(Collection listaOrigens) 
	{
		this.listaOrigens = listaOrigens;
	}
}
