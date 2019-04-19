package com.brt.gpp.comum.mapeamentos.entidade;

//Imports GPP.

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_GER_PLANO_ESPELHO, que mapeia os planos de preco com os respectivos
 *	planos espelhos de acordo com o tipo de espelhamento.
 * 
 *	@author	Daniel Ferreira
 *	@since	03/10/2005
 */
public class PlanoEspelho implements Entidade
{

    //Constantes.
    
    /**
     *  Tipo de espelhamento de ativacao de assinante.
     */
    public static final String ATIVACAO = "ATIVACAO";
    
    /**
     *  Tipo de espelhamento de troca de plano de assinante.
     */
    public static final String TROCA_PLANO = "TROCA_PLANO";
    
    /**
     *  Tipo de espelhamento para gerenciamento de promocoes.
     */
    public static final String PROMOCAO = "PROMOCAO";
    
    /**
     *  Caractere coringa para passagem de parametro na informacao de planos de preco. Significa que qualquer plano
     *  se aplica.
     */
    public static final String CORINGA = "*";

    /**
     *  Caractere coringa para passagem de parametro na informacao de categoria de plano de preco.
     *  Significa que qualquer plano, de uma dada categoria, se aplica.
     */
    public static final String CORINGA_CATEGORIA_DE_PLANO = "C";
    
    //Atributos.
    
    /**
     *  Tipo de espelhamento para calculo do plano base e do plano espelho.
     */
	private String tipEspelhamento;
    
    /**
     *  Identificador do plano fornecido pelo processo. Utilizado, por exemplo, em eventos de ativacao e troca de
     *  plano, em que sistemas externos fornecem os planos que fazem parte da raiz da arvore de espelhamento.
     */
	private String idtPlanoPreco;
    
    /**
     *  Identificador do plano base ou original.
     */
	private String idtPlanoBase;
    
    /**
     *  Identificador do plano espelho.
     */
	private String idtPlanoEspelho;
	
    /**
     *  Identificador do plano atual do assinante. Alguns processos (ex: troca de plano) exigem o plano atual do 
     *  assinante para o calculo do plano base e do plano espelho.
     */
    private String idtPlanoAssinante;
    
	//Construtores.
	
	/**
	 * Construtor da classe.
	 */
	public PlanoEspelho()
	{
		this.tipEspelhamento	= null;
		this.idtPlanoPreco		= null;
		this.idtPlanoBase		= null;
		this.idtPlanoEspelho	= null;
        this.idtPlanoAssinante  = null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o tipo de espelhamento. O tipo de espelhamento define o motivo da existencia de um plano espelho para
	 *	o plano atual. Por exemplo, o tipo de espelhamento "PULAPULA" define os planos espelhos para as promocoes
	 *	Pula-Pula, indicando quais planos podem fazer LDN com utilizacao de saldo de bonus e quais nao podem.
	 * 
	 *	@return     Tipo de espelhamento.
	 */
	public String getTipEspelhamento() 
	{
		return this.tipEspelhamento;
	}
	
	/**
	 *	Retorna o identificador do plano de preco.
	 * 
	 *	@return     Identificador do plano de preco.
	 */
	public String getIdtPlanoPreco() 
	{
		return this.idtPlanoPreco;
	}
	
	/**
	 *	Retorna o identificador do plano base.
	 * 
	 *	@return     Identificador do plano base.
	 */
	public String getIdtPlanoBase() 
	{
		return this.idtPlanoBase;
	}
	
	/**
	 *	Retorna o identificador do plano espelho.
	 * 
	 *	@return     Identificador do plano espelho.
	 */
	public String getIdtPlanoEspelho() 
	{
		return this.idtPlanoEspelho;
	}
	
    /**
     *  Retorna o identificador do plano do assinante.
     * 
     *  @return     Identificador do plano do assinante.
     */
    public String getIdtPlanoAssinante() 
    {
        return this.idtPlanoAssinante;
    }
    
	//Setters.
	
	/**
	 *	Atribui o tipo de espelhamento. O tipo de espelhamento define o motivo da existencia de um plano espelho para
	 *	o plano atual. Por exemplo, o tipo de espelhamento "PULAPULA" define os planos espelhos para as promocoes
	 *	Pula-Pula, indicando quais planos podem fazer LDN com utilizacao de saldo de bonus e quais nao podem.
	 * 
	 *	@param      tipEspelhamento			Tipo de espelhamento.
	 */
	public void setTipEspelhamento(String tipEspelhamento) 
	{
		this.tipEspelhamento = tipEspelhamento;
	}
	
	/**
	 *	Atribui o identificador do plano de preco.
	 * 
	 *	@param      idtPlanoPreco			Identificador do plano de preco.
	 */
	public void setIdtPlanoPreco(String idtPlanoPreco) 
	{
		this.idtPlanoPreco = idtPlanoPreco;
	}
	
	/**
	 *	Atribui o identificador do plano base.
	 * 
	 *	@param      idtPlanoBase			Identificador do plano base.
	 */
	public void setIdtPlanoBase(String idtPlanoBase) 
	{
		this.idtPlanoBase = idtPlanoBase;
	}
	
	/**
	 *	Atribui o identificador do plano espelho.
	 * 
	 *	@param      idtPlanoEspelho			Identificador do plano espelho.
	 */
	public void setIdtPlanoEspelho(String idtPlanoEspelho) 
	{
		this.idtPlanoEspelho = idtPlanoEspelho;
	}
	
    /**
     *  Atribui o identificador do plano do assinante.
     * 
     *  @param      idtPlanoAssinante       Identificador do plano do assinante.
     */
    public void setIdtPlanoAssinante(String idtPlanoAssinante) 
    {
        this.idtPlanoAssinante = idtPlanoAssinante;
    }
    
	//Implentacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return     Copia do objeto.
	 */
	public Object clone()
	{
		PlanoEspelho result = new PlanoEspelho();		
		
		result.setTipEspelhamento(this.tipEspelhamento);
		result.setIdtPlanoPreco(this.idtPlanoPreco);
		result.setIdtPlanoBase(this.idtPlanoBase);
		result.setIdtPlanoEspelho(this.idtPlanoEspelho);
        result.setIdtPlanoAssinante(this.idtPlanoAssinante);
		
		return result;
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param      object					Objeto a ser comparado com o atual.
	 *	@return     True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof PlanoEspelho))
		{
			return false;
		}
		
		if(this.hashCode() != ((PlanoEspelho)object).hashCode())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return     Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append((this.tipEspelhamento != null) ? this.tipEspelhamento : "NULL");
		result.append("||");
		result.append((this.idtPlanoPreco != null) ? this.idtPlanoPreco : "NULL");
        result.append("||");
        result.append((this.idtPlanoAssinante != null) ? this.idtPlanoAssinante : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return     Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("Tipo de Espelhamento: ");
		result.append((this.tipEspelhamento != null) ? this.tipEspelhamento : "NULL");
		result.append(" - ");
		result.append("Plano de Preco: ");
		result.append((this.idtPlanoPreco != null) ? this.idtPlanoPreco : "NULL");
        result.append(" - ");
        result.append("Plano do Assinante: ");
        result.append((this.idtPlanoAssinante != null) ? this.idtPlanoAssinante : "NULL");
		result.append(" - ");
		result.append("Plano Base: ");
		result.append((this.idtPlanoBase != null) ? this.idtPlanoBase : "NULL");
		result.append(" - ");
		result.append("Plano Espelho: ");
		result.append((this.idtPlanoEspelho != null) ? this.idtPlanoEspelho : "NULL");
		
		return result.toString();
	}
    
    //Outros metodos.
    
    /**
     *  Verifica se o plano do assinante informado se aplica aos planos configurados no registro de plano espelho.
     * 
     *  @param      planoAssinante          Plano do assinante informado.
     *  @return     True se aplica e false caso contrario.
     *  @throws     Exception
     */
    public boolean matches(String planoAssinante) throws Exception
    {
        if((this.idtPlanoAssinante == null) || (planoAssinante == null))
            return false;
        
        if(this.idtPlanoAssinante.equals(PlanoEspelho.CORINGA))
            return true;
        
        String opcoes[] = this.idtPlanoAssinante.split(";");
        for(int i = 0; i < opcoes.length; i++)
        {
            String ranges[] = opcoes[i].split("-");
            
            if(ranges.length == 1)
            {
                if(Integer.parseInt(ranges[0]) == Integer.parseInt(planoAssinante))
                    return true;
            }
            else
                for(int j = Integer.parseInt(ranges[0]); j <= Integer.parseInt(ranges[1]); j++)
                    if(j == Integer.parseInt(planoAssinante))
                        return true;
        }
        
        return false;
    }
	
}
