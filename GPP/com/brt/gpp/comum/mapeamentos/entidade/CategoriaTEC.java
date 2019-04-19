package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Entidade <code>CategoriaTEC</code>. Referência: TBL_GER_CATEGORIA_TEC.
 * 
 *	@version	1.0		12/07/2007		Primeira versao.
 *	@author		Bernardo Vergne Dias
 */
public class CategoriaTEC implements Serializable
{

	/**
	 *	Identificador da categoria definido pela Tecnomen (Service ID).
	 */
	private short idtCategoria;
	
	/**
	 *	Descricao da categoria.
	 */
	private String desCategoria;
	
	/**
	 *	Identificador do dialeto da categoria (Dialect ID).
	 */
	private short idtDialeto;
	
	/**
	 *	Status da senha do assinante (PIN Status).
	 */
	private short idtStatusSenha;
	
	/**
	 *	CSP default para ligacoes de longa distancia (Preferred LDC).
	 */
	private short numCspDefault;
	
	/**
	 *	Indicador de permissao de chamadas a cobrar.
	 */
	private boolean indChamadaACobrar;
	
	/**
	 *	Indicador de reset de senha no momento da primeira chamada.
	 */
	private boolean indResetSenha;
	
	/**
	 *	Construtor da classe.
	 */
	public CategoriaTEC()
	{
		this.idtCategoria		= -1;
		this.desCategoria		= null;
		this.idtDialeto			= -1;
		this.idtStatusSenha		= -1;
		this.numCspDefault		= -1;
		this.indChamadaACobrar	= false;
		this.indResetSenha		= false;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idtCategoria			Identificador da categoria definido pela Tecnomen (Service ID).
	 *	@param		desCategoria			Descricao da categoria.
	 *	@param		idtDialeto				Identificador do dialeto da categoria (Dialect ID).
	 *	@param		idtStatusSenha			Status da senha do assinante (PIN Status).
	 *	@param		numCspDefault			CSP default para ligacoes de longa distancia (Preferred LDC).
	 *	@param		indChamadaACobrar		Indicador de permissao de chamadas a cobrar.
	 *	@param		indResetSenha			Indicador de reset de senha no momento da primeira chamada.
	 */
	public CategoriaTEC(short	idtCategoria, 
						String	desCategoria, 
						short	idtDialeto, 
						short	idtStatusSenha,
						short	numCspDefault,
						boolean	indChamadaACobrar,
						boolean	indResetSenha)
	{
		this.idtCategoria		= idtCategoria;
		this.desCategoria		= desCategoria;
		this.idtDialeto			= idtDialeto;
		this.idtStatusSenha		= idtStatusSenha;
		this.numCspDefault		= numCspDefault;
		this.indChamadaACobrar	= indChamadaACobrar;
		this.indResetSenha		= indResetSenha;
	}
	
	/**
	 *	Retorna o identificador da categoria definido pela Tecnomen (Service ID).
	 *
	 *	@return		Identificador da categoria definido pela Tecnomen (Service ID).
	 */
	public short getIdtCategoria()
	{
		return this.idtCategoria;
	}
	
	/**
	 *	Retorna a descricao da categoria.
	 *
	 *	@return		Descricao da categoria.
	 */
	public String getDesCategoria()
	{
		return this.desCategoria;
	}
	
	/**
	 *	Retorna o identificador do dialeto da categoria (Dialect ID).
	 *
	 *	@return		Identificador do dialeto da categoria (Dialect ID).
	 */
	public short getIdtDialeto()
	{
		return this.idtDialeto;
	}
	
	/**
	 *	Retorna o status da senha do assinante (PIN Status).
	 *
	 *	@return		Status da senha do assinante (PIN Status).
	 */
	public short getIdtStatusSenha()
	{
		return this.idtStatusSenha;
	}
	
	/**
	 *	Retorna o CSP default para ligacoes de longa distancia (Preferred LDC).
	 *
	 *	@return		CSP default para ligacoes de longa distancia (Preferred LDC).
	 */
	public short getNumCspDefault()
	{
		return this.numCspDefault;
	}

	/**
	 *	Retorna o indicador de permissao de chamadas a cobrar.
	 *
	 *	@return		Indicador de permissao de chamadas a cobrar.
	 */
	public boolean permiteChamadaACobrar()
	{
		return this.indChamadaACobrar;
	}
	
	/**
	 *	Retorna o indicador de reset de senha no momento da primeira chamada.
	 *
	 *	@return		Indicador de reset de senha no momento da primeira chamada.
	 */
	public boolean resetarSenha()
	{
		return this.indResetSenha;
	}
	
	/**
	 *	Atribui o identificador da categoria definido pela Tecnomen (Service ID).
	 *
	 *	@param		idtCategoria			Identificador da categoria definido pela Tecnomen (Service ID).
	 */
	public void setIdtCategoria(short idtCategoria)
	{
		this.idtCategoria = idtCategoria;
	}
	
	/**
	 *	Atribui a descricao da categoria.
	 *
	 *	@param		desCategoria			Descricao da categoria.
	 */
	public void setDesCategoria(String desCategoria)
	{
		this.desCategoria = desCategoria;
	}
	
	/**
	 *	Atribui o identificador do dialeto da categoria (Dialect ID).
	 *
	 *	@param		idtDialeto				Identificador do dialeto da categoria (Dialect ID).
	 */
	public void setIdtDialeto(short idtDialeto)
	{
		this.idtDialeto = idtDialeto;
	}
	
	/**
	 *	Atribui o status da senha do assinante (PIN Status).
	 *
	 *	@param		idtStatusSenha			Status da senha do assinante (PIN Status).
	 */
	public void setIdtStatusSenha(short idtStatusSenha)
	{
		this.idtStatusSenha = idtStatusSenha;
	}
	
	/**
	 *	Atribui o CSP default para ligacoes de longa distancia (Preferred LDC).
	 *
	 *	@param		numCspDefault			CSP default para ligacoes de longa distancia (Preferred LDC).
	 */
	public void setNumCspDefault(short numCspDefault)
	{
		this.numCspDefault = numCspDefault;
	}

	/**
	 *	Atribui o indicador de permissao de chamadas a cobrar.
	 *
	 *	@param		indChamadaACobrar		Indicador de permissao de chamadas a cobrar.
	 */
	public void setIndChamadaACobrar(boolean indChamadaACobrar)
	{
		this.indChamadaACobrar = indChamadaACobrar;
	}
	
	/**
	 *	Atribui o indicador de reset de senha no momento da primeira chamada.
	 *
	 *	@param		indResetSenha			Indicador de reset de senha no momento da primeira chamada.
	 */
	public void setIndResetSenha(boolean indResetSenha)
	{
		this.indResetSenha = indResetSenha;
	}
	
	/**
	 *	@see		java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof CategoriaTEC))
			return false;
		
		if (obj == this)
			return true;

		return this.getIdtCategoria() == ((CategoriaTEC)obj).getIdtCategoria();
	}
	
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return this.idtCategoria;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[CategoriaTEC]");
		result.append("SERVICE_ID=" + this.idtCategoria);
		if(this.desCategoria != null)
			result.append(";DESCRICAO=" + this.desCategoria);
	
		return result.toString();
	}

}
