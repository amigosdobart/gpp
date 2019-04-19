package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Collection;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_REC_ORIGEM.
 * 
 *	@author	Daniel Ferreira
 *	@since	22/12/2005
 */
public class OrigemRecarga implements Entidade, Serializable
{
	/**
	 *	Constante referente ao lancamento de credito.
	 */
	public static final String LANCAMENTO_CREDITO = "C";
	
	/**
	 *	Constante referente ao lancamento de debito.
	 */
	public static final String LANCAMENTO_DEBITO = "D";
	
    private static final long serialVersionUID = -916337818491252927L;
    
    private String	idOrigem;
	private String	desOrigem;
	private Integer	indModificarDataExp;
	private String	idCanal;
	private Integer	indAtivo;
	private String	tipLancamento;
	private String	idtClassificacaoRecarga;
	private Integer	indDisponivelPortal;
    private boolean indDispNovoItemContestacao;
    private Collection sistemasOrigem;
    private Collection categorias;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public OrigemRecarga()
	{
		this.idOrigem					= null;
		this.desOrigem					= null;
		this.indModificarDataExp		= null;
		this.idCanal					= null;
		this.indAtivo					= null;
		this.tipLancamento				= null;
		this.idtClassificacaoRecarga	= null;
		this.indDisponivelPortal		= null;
	}
	
    public boolean isIndDispNovoItemContestacao()
    {
        return indDispNovoItemContestacao;
    }

    public void setIndDispNovoItemContestacao(boolean indDispNovoItemContestacao)
    {
        this.indDispNovoItemContestacao = indDispNovoItemContestacao;
    }

    //Getters.
    
    /**
     * @return Colecao de <code>Categoria</code>
     */
    public Collection getCategorias()
    {
        return categorias;
    }
    
    /**
     * @return Colecao de <code>SistemaOrigem</code>
     */
    public Collection getSistemasOrigem()
    {
        return sistemasOrigem;
    }
    
	/**
	 *	Retorna o identificador da origem da recarga.
	 * 
	 *	@return		String					idOrigem					Identificador da origem da recarga.
	 */
	public String getIdOrigem() 
	{
		return this.idOrigem;
	}
	
	/**
	 *	Retorna a descricao da origem da recarga.
	 * 
	 *	@return		String					desOrigem					Descricao da origem da recarga.
	 */
	public String getDesOrigem() 
	{
		return this.desOrigem;
	}
	
	/**
	 *	Retorna o indicador de modificacao da data de expiracao.
	 * 
	 *	@return		Integer					indModificarDataExp			Indicador de modificacao da data de expiracao.
	 */
	public Integer getIndModificarDataExp() 
	{
		return this.indModificarDataExp;
	}
	
	/**
	 *	Retorna o identificador do canal da recarga.
	 * 
	 *	@return		String					idCanal						Identificador do canal da recarga.
	 */
	public String getIdCanal() 
	{
		return this.idCanal;
	}
	
	/**
	 *	Calcula e retorna o tipo de transacao associada a origem.
	 * 
	 *	@return		Tipo de transacao da recarga ou ajuste, associado a origem.
	 */
	public String getTipoTransacao()
	{
		return this.getIdCanal() + this.getIdOrigem();
	}
	
	/**
	 *	Retorna o indicador de registro ativo.
	 * 
	 *	@return		Integer					indAtivo					Indicador de registro ativo.
	 */
	public Integer getIndAtivo() 
	{
		return this.indAtivo;
	}
	
	/**
	 *	Retorna o tipo de lancamento.
	 * 
	 *	@return		String					tipLancamento				Tipo de lancamento.
	 */
	public String getTipLancamento() 
	{
		return this.tipLancamento;
	}
	
	/**
	 *	Retorna o identificador de classificacao da recarga.
	 * 
	 *	@return		String					idtClassificacaoRecarga		Identificador de classificacao da recarga.
	 */
	public String getIdtClassificacaoRecarga() 
	{
		return this.idtClassificacaoRecarga;
	}
	
	/**
	 *	Retorna o indicador de disponibilidade para o portal.
	 * 
	 *	@return		Integer					indDisponivelPortal			Indicador de disponibilidade para o portal.
	 */
	public Integer getIndDisponivelPortal() 
	{
		return this.indDisponivelPortal;
	}
	
	//Setters.
	
    /**
     * @param categorias Colecao de <code>Categoria</code>
     */
    public void setCategorias(Collection categorias)
    {
        this.categorias = categorias;
    }
    
    /**
     * @param categorias Colecao de <code>SistemaOrigem</code>
     */
    public void setSistemasOrigem(Collection sistemasOrigem)
    {
        this.sistemasOrigem = sistemasOrigem;
    }
    
	/**
	 *	Atribui o identificador da origem da recarga.
	 * 
	 *	@param		String					idOrigem					Identificador da origem da recarga.
	 */
	public void setIdOrigem(String idOrigem) 
	{
		this.idOrigem = idOrigem;
	}
	
	/**
	 *	Atribui a descricao da origem da recarga.
	 * 
	 *	@param		String					desOrigem					Descricao da origem da recarga.
	 */
	public void setDesOrigem(String desOrigem) 
	{
		this.desOrigem = desOrigem;
	}
	
	/**
	 *	Atribui o indicador de modificacao da data de expiracao.
	 * 
	 *	@param		Integer					indModificarDataExp			Indicador de modificacao da data de expiracao.
	 */
	public void setIndModificarDataExp(Integer indModificarDataExp) 
	{
		this.indModificarDataExp = indModificarDataExp;
	}
	
	/**
	 *	Atribui o identificador do canal da recarga.
	 * 
	 *	@param		String					idCanal						Identificador do canal da recarga.
	 */
	public void setIdCanal(String idCanal) 
	{
	    this.idCanal = idCanal;
	}
	
	/**
	 *	Atribui o indicador de registro ativo.
	 * 
	 *	@param		Integer					indAtivo					Indicador de registro ativo.
	 */
	public void setIndAtivo(Integer indAtivo) 
	{
		this.indAtivo = indAtivo;
	}
	
	/**
	 *	Atribui o tipo de lancamento.
	 * 
	 *	@param		String					tipLancamento				Tipo de lancamento.
	 */
	public void setTipLancamento(String tipLancamento) 
	{
		this.tipLancamento = tipLancamento;
	}
	
	/**
	 *	Atribui o identificador de classificacao da recarga.
	 * 
	 *	@param		String					idtClassificacaoRecarga		Identificador de classificacao da recarga.
	 */
	public void setIdtClassificacaoRecarga(String idtClassificacaoRecarga) 
	{
		this.idtClassificacaoRecarga = idtClassificacaoRecarga;
	}
	
	/**
	 *	Atribui o indicador de disponibilidade para o portal.
	 * 
	 *	@return		Integer					indDisponivelPortal			Indicador de disponibilidade para o portal.
	 */
	public void setIndDisponivelPortal(Integer indDisponivelPortal) 
	{
		this.indDisponivelPortal = indDisponivelPortal;
	}
		
    
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto (no momento as collections 'sistemasOrigem' e 'categorias' nao sao clonadas!).
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		OrigemRecarga result = new OrigemRecarga();	
		
		result.setIdOrigem((this.idOrigem != null) ? new String(this.idOrigem) : null);
		result.setDesOrigem((this.desOrigem != null) ? new String(this.desOrigem) : null);
		result.setIndModificarDataExp((this.indModificarDataExp != null) ? new Integer(this.indModificarDataExp.intValue()) : null);
		result.setIdCanal((this.idCanal != null) ? new String(this.idCanal) : null);
		result.setIndAtivo((this.indAtivo != null) ? new Integer(this.indAtivo.intValue()) : null);
		result.setTipLancamento((this.tipLancamento != null) ? new String(this.tipLancamento) : null);
		result.setIdtClassificacaoRecarga((this.idtClassificacaoRecarga != null) ? new String(this.idtClassificacaoRecarga) : null);
		result.setIndDisponivelPortal((this.indDisponivelPortal != null) ? new Integer(this.indDisponivelPortal.intValue()) : null);
		
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
		
		if(!(object instanceof OrigemRecarga))
		{
			return false;
		}
		
		if(this.hashCode() != ((OrigemRecarga)object).hashCode())
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
		result.append((this.idCanal != null) ? this.idCanal : "NULL");
		result.append("||");
		result.append((this.idOrigem != null) ? this.idOrigem : "NULL");
		
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
	
		result.append("Canal: ");
		result.append((this.idCanal != null) ? this.idCanal : "NULL");
		result.append(" - ");
		result.append("Origem: ");
		result.append((this.idOrigem != null) ? this.idOrigem : "NULL");
		
		return result.toString();
	}
	
	//Outros metodos.

    /**
	 *	Indica se a origem da recarga deve modificar data de expiracao.
	 * 
	 *	@return		String												True se deve modificar e false caso contrario.
	 */
	public boolean modificarDataExp()
	{
	    return ((this.indModificarDataExp != null) && (this.indModificarDataExp.intValue() == 1));
	}
	
	/**
	 *	Indica se a origem da recargas esta ativa.
	 * 
	 *	@return		boolean												True se a origem esta ativa e false caso contrario.
	 */
	public boolean ativo()
	{
	    return ((this.indAtivo != null) && (this.indAtivo.intValue() == 1));
	}
	
	/**
	 *	Indica se a origem da recarga esta disponivel para o portal.
	 * 
	 *	@return		boolean												True se a origem esta disponivel e false caso contrario.
	 */
	public boolean disponivelPortal()
	{
	    return ((this.indDisponivelPortal != null) && (this.indDisponivelPortal.intValue() == 1));
	}
	
}
