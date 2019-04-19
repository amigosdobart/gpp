package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimite;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteDinamico;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_PROMOCAO.
 * 
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		08/08/2005
 *	@modify		Primeira versao.
 *
 *	versao de compatibilizacao com o CD do Controle Total
 *  
 *  @version	1.1
 *  @author		Joao Paulo Galvagni
 *  @date		05/11/2007
 *  @modify		Inclusao do atributo promocoesPacoteBonus para bonificacoes extras, como os 30 SMSs da Promocao 
 *  			Oferta de Natal 2007.
 *  
 *  @version	2.0
 *  @author		Daniel Ferreira
 *  @date		19/03/2008
 *  @modify		Adaptacao para promocao nao implementar mais entidade e inclusao de multiplas bonificacoes.
 */
public class Promocao
{
    
	/**
	 *	Constante referente a promocao Fale e Ganhe do Pos-Pago.
	 */
	public static final int FALE_GANHE = 33;

	/**
	 *	Identificador da promocao.
	 */
	private int	idtPromocao;
	
	/**
	 *	Identificador da promocao no CRM.
	 */
	private String idtPromocaoCrm;
	
	/**
	 * Identificador da promocao no SAG
	 */
	private String idtPromocaoSag;
	
	/**
	 *	Identificador do processo batch de concessao.
	 */
	private int idProcessoBatch;
    
	/**
	 *	Categoria da promocao.
	 */
	private PromocaoCategoria categoria;
	
	/**
	 *	Nome da promocao.
	 */
	private String nomPromocao;
	
	/**
	 *	Descricao da promocao.
	 */
	private String desPromocao;
	
	/**
	 *	Data inicial de cadastro de assinantes.
	 */
	private Date datInicio;
	
	/**
	 *	Data final de cadastro de assinantes.
	 */
	private Date datFim;
	
	/**
	 *	Data inicial de validade.
	 */
	private Date datInicioValidade;
	
	/**
	 *	Data final de validade.
	 */
	private Date datFimValidade;
	
	/**
	 *	Tipo de espelhamento de planos de preco.
	 */
	private String tipEspelhamento;
	
	/**
	 *	Valor fixo de bonus.
	 */
	private double vlrBonus;
	
	/**
	 *	Indicador de primeira recarga obrigatoria para recebimento de bonus.
	 */
	private boolean indPrimeiraRecargaObr;
	
    /**
     *  Lista de limites da promocao.
     */
    private Collection limite;
    
    /**
     *  Lista de informacoes para calculo de limite dinamico da promocao.
     */
    private Collection limiteDinamico;

    /**
     * 	Lista de informacoes dos dias de semana disponiveis da promocao
     */
    private Collection diasSemana;
    
    /**
     * 	Lista de informacoes dos tipos de chamada disponiveis da promocao
     */
    private Collection tiposChamada;
    
    /**
     * 	Lista de informacoes das promocoesBonus da promocao
     */
    private Collection promocoesPacoteBonus;
    
    /**
     *  Lista de informacoes de limites de segundos disponiveis da promocao
     */
    private Collection limiteSegundos;
    
	/**
	 *	Construtor da classe.
	 */
	public Promocao()
	{
		this.idtPromocao			= -1;
		this.idtPromocaoCrm			= null;
		this.idtPromocaoSag			= null;
		this.idProcessoBatch		= 0;
		this.categoria				= null;
		this.nomPromocao			= null;
		this.desPromocao			= null;
		this.datInicio				= null;
		this.datFim					= null;
		this.datInicioValidade		= null;
		this.datFimValidade			= null;
		this.tipEspelhamento		= null;
		this.vlrBonus				= 0.0;
		this.indPrimeiraRecargaObr	= false;
        this.limite					= null;
        this.limiteDinamico			= null;
        this.diasSemana				= new ArrayList();
        this.tiposChamada			= new ArrayList();
        this.promocoesPacoteBonus	= new ArrayList();
        this.limiteSegundos			= new ArrayList();
	}
	
	/**
	 *	Retorna o identificador da promocao.
	 *	
	 *	@return     Identificador da promocao.
	 */
	public int getIdtPromocao() 
	{
		return this.idtPromocao;
	}
	
	/**
	 *	Retorna o identificador da promocao usado pelo CRM.
	 *	
	 *	@return     Identificador da promocao usado pelo CRM.
	 */
	public String getIdtPromocaoCrm() 
	{
		return this.idtPromocaoCrm;
	}
	
	/**
	 *	Retorna o identificador da promocao usado pelo SAG.
	 *
	 *	@return		Identificador da promocao usado pelo SAG.
	 */
	public String getIdtPromocaoSag()
	{
		return this.idtPromocaoSag;
	}
	
	/**
	 *	Retorna o id do processo batch concede a promoção.
	 *	
	 *	@return     Identificador do processo batch concede o bonus.
	 */
	public int getIdProcessoBatch() 
	{
		return this.idProcessoBatch;
	}
	
	/**
	 *	Retorna a categoria da promocao.
	 *	
	 *	@return     Categoria da promocao.
	 */
	public PromocaoCategoria getCategoria() 
	{
		return this.categoria;
	}
	
	/**
	 *	Retorna o nome da promocao.
	 *	
	 *	@return     Nome da promocao.
	 */
	public String getNomPromocao() 
	{
		return this.nomPromocao;
	}
	
	/**
	 *	Retorna a descricao da promocao.
	 *	
	 *	@return     Descricao da promocao.
	 */
	public String getDesPromocao() 
	{
		return this.desPromocao;
	}
	
	/**
	 *	Retorna a data inicial de cadastro na promocao.
	 *	
	 *	@return     Data inicial de cadastro na promocao.
	 */
	public Date getDatInicio() 
	{
		return this.datInicio;
	}
	
	/**
	 *	Retorna a data final de cadastro na promocao.
	 *	
	 *	@return     Data final de cadastro na promocao.
	 */
	public Date getDatFim() 
	{
		return this.datFim;
	}
	
	/**
	 *	Retorna a data inicial de validade da promocao.
	 *	
	 *	@return     Data inicial de validade da promocao.
	 */
	public Date getDatInicioValidade() 
	{
		return this.datInicioValidade;
	}
	
	/**
	 *	Retorna a data final de validade da promocao.
	 *	
	 *	@return     Data final de validade da promocao.
	 */
	public Date getDatFimValidade() 
	{
		return this.datFimValidade;
	}
	
	/**
	 *	Retorna o tipo de espelhamento dos planos permitidos para a promocao.
	 *	
	 *	@return     Tipo de espelhamento.
	 */
	public String getTipEspelhamento() 
	{
		return this.tipEspelhamento;
	}
	
	/**
	 *	Retorna o valor de concessao de bonus.
	 *	
	 *	@return     Valor de concessao de bonus.
	 */
	public double getVlrBonus() 
	{
		return this.vlrBonus;
	}
	
	/**
	 *	Indica se a entrada na promocao necessita de primeira recarga.
	 * 
	 *	@return     True se a entrada na promocao necessita de primeira recarga e false caso contrario.
	 */
	public boolean isPrimeiraRecargaObr() 
	{
		return this.indPrimeiraRecargaObr;
	}
	
	/**
	 *	Indica se a entrada na promocao necessita de primeira recarga.
	 * 
	 *	@return     True se a entrada na promocao necessita de primeira recarga e false caso contrario.
	 */
	public boolean exigePrimeiraRecarga()
	{
	    return this.indPrimeiraRecargaObr;
	}
	
	/**
	 *	Retorna a lista de dias da semana associadas a promocao.
	 *	
	 *	@return     Lista de dias da semana associadas a promocao.
	 */
	public Collection getDiasSemana()
	{
		return this.diasSemana;
	}
	
	/**
	 * Retorna uma lista de entidades <code>PromocaoPacoteBonus</code> inerentes
	 * a promocao, ao Codigo Nacional e a data vigente
	 * 
	 * @param  idtCodigoNacional	- Id do Codigo Nacional
	 * @param  dataInformada		- Data para validacao da vigencia
	 * @return promocoesPacoteBonus	- Lista das entidades
	 */
	public Collection getPromocoesPacoteBonus(Integer idtCodigoNacional, Date dataInformada)
	{
		ArrayList promocoesPacoteBonus = new ArrayList();
		
		for (Iterator i = this.promocoesPacoteBonus.iterator(); i.hasNext(); )
		{
			PromocaoPacoteBonus promoPacoteBonus = (PromocaoPacoteBonus)i.next();
			
			if (promoPacoteBonus.getCodigoNacional().getIdtCodigoNacional().equals(idtCodigoNacional))
				if(promoPacoteBonus.isVigente(dataInformada))
					promocoesPacoteBonus.add(promoPacoteBonus);
		}
		
		return promocoesPacoteBonus;
	}
	
	public Collection getTiposChamada()
	{
		return this.tiposChamada;
	}
	
    /**
     *  Retorna a lista de limites da promocao.
     *  
     *  @return     Lista de limites da promocao.
     */
    public Collection getLimite() 
    {
        return this.limite;
    }
	
    /**
     *  Retorna o limite vigente para o tipo de bonificacao na data informada.
     *
     *	@param		tipoBonificacao			Tipo de bonificacao ao qual o limite se aplica.
     *  @param      data                    Data informada.
     *  @return     Limite vigente na data informada.
     */
    public PromocaoLimite getLimite(PromocaoTipoBonificacao tipoBonificacao, Date data) 
    {
        if((this.limite != null) && (data != null))
            for(Iterator iterator = this.limite.iterator(); iterator.hasNext();)
            {
                PromocaoLimite limite = (PromocaoLimite)iterator.next();
                
                if((limite.getTipoBonificacao().equals(tipoBonificacao)) && (limite.isVigente(data)))
                    return limite;
            }
        
        return null;
    }
    
    /**
     *  Retorna o limite vigente no mes informado.
     *
     *	@param		tipoBonificacao			Tipo de bonificacao ao qual o limite se aplica.
     *  @param      mes                     Mes informado.
     *  @return     Limite vigente no mes informado.
     *  @throws     ParseException
     */
    public PromocaoLimite getLimite(PromocaoTipoBonificacao tipoBonificacao, String mes) throws ParseException
    {
        if(mes != null)
        {
        	SimpleDateFormat conversorDatMes = new SimpleDateFormat("yyyyMM");
            return this.getLimite(tipoBonificacao, conversorDatMes.parse(mes));
        }
        
        return null;
    }
    
    /**
     *  Retorna a lista de informacoes para calculo de limite dinamico da promocao.
     *  
     *  @return     Lista de informacoes para calculo de limite dinamico da promocao.
     */
    public Collection getLimiteDinamico() 
    {
        return this.limiteDinamico;
    }
    
    /**
     *  Retorna a informacao vigente para calculo de limite dinamico na data informada.
     *  
     *  @param      data                    Data informada.
     *  @return     Infomacao vigente para calculo de limite dinamico na data informada.
     */
    public PromocaoLimiteDinamico getLimiteDinamico(Date data) 
    {
        if((this.limiteDinamico != null) && (data != null))
            for(Iterator iterator = this.limiteDinamico.iterator(); iterator.hasNext();)
            {
                PromocaoLimiteDinamico limiteDinamico = (PromocaoLimiteDinamico)iterator.next();
                
                if(limiteDinamico.isVigente(data))
                    return (PromocaoLimiteDinamico)limiteDinamico.clone();
            }
        
        return null;
    }
    
    /**
     *  Retorna a informacao vigente para calculo de limite dinamico no mes informado.
     *  
     *  @param      mes                     Mes informado.
     *  @return     Infomacao vigente para calculo de limite dinamico no mes informado.
     *  @throws     ParseException
     */
    public PromocaoLimiteDinamico getLimiteDinamico(String mes) throws ParseException
    {
        if(mes != null)
        {
        	SimpleDateFormat conversorDatMes  = new SimpleDateFormat("yyyyMM");
            return this.getLimiteDinamico(conversorDatMes.parse(mes));
        }
        
        return null;
    }
    
    /**
     *  Retorna a lista de limites de segundos da promocao.
     *  
     *  @return     Lista de limites de segundos da promocao.
     */
    public Collection getLimiteSegundos()
    {
    	return this.limiteSegundos;
    }
    
    /**
     *  Retorna o limite maximo de segundos da promocao.
     *  
     *  @return     Limite maximo de segundos da promocao.
     */
    public PromocaoLimiteSegundos getLimiteSegundosMaximo()
    {
    	for(Iterator iterator = this.getLimiteSegundos().iterator(); iterator.hasNext();)
    	{
    		PromocaoLimiteSegundos result = (PromocaoLimiteSegundos)iterator.next();
    		
    		if(result.isLimiteMaximo())
    			return result; 
    	}
    	
    	return null;
    }
    
	/**
	 *	Atribui o identificador da promocao.
	 * 
	 *	@param		idtPromocao				Identificador da promocao.
	 */
	public void setIdtPromocao(int idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
	
	/**
	 *	Atribui o indicador da promocao usado pelo CRM.
	 *	
	 *	@param		idtPromocaoCrm			Indicador da promocao usado pelo CRM.
	 */
	public void setIdtPromocaoCrm(String idtPromocaoCrm) 
	{
		this.idtPromocaoCrm = idtPromocaoCrm;
	}
	
	/**
	 *	Atribui o indicador da promocao usado pelo SAG.
	 *	
	 *	@param		idtPromocaoCrm			Indicador da promocao usado pelo SAG.
	 */
	public void setIdtPromocaoSag(String idtPromocaoSag)
	{
		this.idtPromocaoSag = idtPromocaoSag;
	}
	
	/**
	 *	Atribui o id do processo batch concede a promoção.
	 *	
	 *	@param		idProcessoBatch			Identificador do processo batch concede o bonus.
	 */
	public void setIdProcessoBatch(int idProcessoBatch) 
	{
		this.idProcessoBatch = idProcessoBatch;
	}

	/**
	 *	Atribui a categoria da promocao.
	 * 
	 *	@param		categoria				Categoria da promocao.
	 */
	public void setCategoria(PromocaoCategoria categoria) 
	{
		this.categoria = categoria;
	}
	
	/**
	 *	Atribui o nome da promocao.
	 *	
	 *	@param		nomPromocao				Nome da promocao.
	 */
	public void setNomPromocao(String nomPromocao) 
	{
		this.nomPromocao = nomPromocao;
	}
	
	/**
	 *	Atribui a descricao da promocao.
	 * 
	 *	@param		desPromocao				Descricao da promocao.
	 */
	public void setDesPromocao(String desPromocao) 
	{
		this.desPromocao = desPromocao;
	}
	
	/**
	 *	Atribui a data final de cadastro na promocao.
	 * 
	 *	@param		datInicialCadastro		Data final de cadastro na promocao.
	 */
	public void setDatInicio(Date datInicio) 
	{
		this.datInicio = datInicio;
	}
	
	/**
	 *	Atribui a data final de cadastro na promocao.
	 * 
	 *	@param		datFim					Data final de cadastro na promocao.
	 */
	public void setDatFim(Date datFim)
	{
		this.datFim = datFim;
	}
	
	/**
	 *	Atribui a data inicial de validade da promocao.
	 * 
	 *	@param		datInicioValidade		Data inicial de validade da promocao.
	 */
	public void setDatInicioValidade(Date datInicioValidade) 
	{
		this.datInicioValidade = datInicioValidade;
	}
	
	/**
	 *	Atribui a data final de validade da promocao.
	 * 
	 *	@param		datFimValidade			Data final de validade da promocao.
	 */
	public void setDatFimValidade(Date datFimValidade) 
	{
		this.datFimValidade = datFimValidade;
	}
	
	/**
	 *	Atribui o tipo de espelhamento dos planos permitidos para a promocao.
	 *	
	 *	@param		tipEspelhamento			Tipo de espelhamento.
	 */
	public void setTipEspelhamento(String tipEspelhamento) 
	{
		this.tipEspelhamento = tipEspelhamento;
	}
	
	/**
	 *	Atribui o valor de concessao de bonus.
	 *	
	 *	@param		vlrBonus				Valor de concessao de bonus.
	 */
	public void setVlrBonus(double vlrBonus) 
	{
		this.vlrBonus = vlrBonus;
	}
	
	/**
	 *	Atribui o indicador de necessidade de primeira recarga para entrar concessao do bonus.
	 *	
	 *	@param		indPrimeiraRecargaObr	Indicador de necessidade de primeira recarga.
	 */
	public void setIndPrimeiraRecargaObr(boolean indPrimeiraRecargaObr) 
	{
		this.indPrimeiraRecargaObr = indPrimeiraRecargaObr;
	}	
	
    /**
     *  Atribui a lista de limites da promocao.
     *  
     *  @param      limite                  Lista de limites da promocao.
     */
    public void setLimite(Collection limite) 
    {
        this.limite = limite;
    }
    
    /**
     *  Atribui a lista de informacoes para calculo de limite dinamico da promocao.
     *  
     *  @param      limiteDinamico          Lista de informacoes para calculo de limite dinamico da promocao.
     */
    public void setLimiteDinamico(Collection limiteDinamico) 
    {
        this.limiteDinamico = limiteDinamico;
    }
    
    /**
     *	Atribui a lista de informacoes de tipos de chamada
     *	
     *	@param 		tiposChamada			Lista de informacoes dos tipos de chamada
     */
    public void setTiposChamada(Collection tiposChamada)
    {
    	this.tiposChamada = tiposChamada;
    }
    
    /**
     *	Atribui a lista de informacoes de tipos de chamada.
     * 
     *	@param 		tiposChamada			Lista de informacoes dos tipos de chamada
     */
    public void setPromocoesPacoteBonus(Collection promocoesPacoteBonus)
    {
    	this.promocoesPacoteBonus = promocoesPacoteBonus;
    }
    
    /**
     * Atribui a lista de informacoes de dias de semana
     * 
     * @param diasSemana Lista de informacoes de dias de semana
     */
    public void setDiasSemana(Collection diasSemana)
    {
    	this.diasSemana = diasSemana;
    }
    
    public void setLimiteSegundos(Collection limite)
    {
    	this.limiteSegundos = limite;
    }
    
    /**
     * Adiciona dia da semana na lista da promocao
     * @param diaSemana Dia da semana a ser incluido
     * @return boolean  Informa se foi possivel adicionar o dia ou nao
     */
    public boolean addDiaSemana(int diaSemana)
    {
    	if (this.diasSemana == null)
    		this.diasSemana = new ArrayList();
    	
    	return this.diasSemana.add(new Integer(diaSemana));
    }
    
    /**
     * Adiciona uma PromocaoBonus na lista de promocoesBonus
     * @param  promoPacoteBonus
     * @return 
     */
    public boolean addPromocaoPacoteBonus(PromocaoPacoteBonus promoPacoteBonus)
    {
    	if (this.promocoesPacoteBonus == null)
    		this.promocoesPacoteBonus = new ArrayList();
    	
    	return this.promocoesPacoteBonus.add(promoPacoteBonus);
    }
    
    /**
     * Adiciona tipo de chamada na lista da promocao
     * @param tipoChamada
     * @return
     */
    public boolean addTipoChamada(String tipoChamada)
    {
    	if (this.tiposChamada == null)
    		this.tiposChamada = new ArrayList();
    	
    	return this.tiposChamada.add(tipoChamada);
    }
    
    /**
     *  Indica se a promocao possui limite vigente na data informada.
     * 
     *  @param      tipoBonificacao			Tipo de bonificacao.
     *  @param      data					Data informada.
     *  @return     True se possui limite vigente e false caso contrario.
     */
    public boolean temLimite(PromocaoTipoBonificacao tipoBonificacao, Date data)
    {
        return (this.getLimite(tipoBonificacao, data) != null);
    }
	
    /**
     *  Indica se a promocao possui limite vigente no mes informado.
     * 
     *  @param      tipoBonificacao			Tipo de bonificacao.
     *  @param      mes                     Mes informado.
     *  @return     True se possui limite vigente e false caso contrario.
     *  @throws     ParseException
     */
    public boolean temLimite(PromocaoTipoBonificacao tipoBonificacao, String mes) throws ParseException
    {
        return (this.getLimite(tipoBonificacao, mes) != null);
    }
    
    /**
     *  Indica se a promocao possui informacoes vigentes para calculo de limite dinamico na data informada.
     * 
     *  @param      data                    Data informada.
     *  @return     True se possui informacoes vigentes e false caso contrario.
     */
    public boolean temLimiteDinamico(Date data)
    {
        return (this.getLimiteDinamico(data) != null);
    }
    
    /**
     *  Indica se a promocao possui informacoes vigentes para calculo de limite dinamico no mes informado.
     * 
     *  @param		mes                     Mes informado.
     *  @return		True se possui informacoes vigentes e false caso contrario.
     *  @throws		ParseException
     */
    public boolean temLimiteDinamico(String mes) throws ParseException
    {
        return (this.getLimiteDinamico(mes) != null);
    }
    
	/**
	 *	Indica se o registro esta vigente ou nao.
	 * 
	 *	@return		True se esta vigente e false caso contrario.
	 */
    public boolean isVigente(Date datProcessamento)
    {
        if(datProcessamento == null)
            return false;
        
        if(this.datInicio == null)
            return false;
        
        if(this.datInicio.compareTo(datProcessamento) > 0)
            return false;
        
        if(this.datFim == null)
            return true;
        
        return (this.datFim.compareTo(datProcessamento) >= 0);
    }
    
	/**
	 *	Indica se a validadade da promocao esta vigente ou nao.
	 * 
	 *	@return		True se esta vigente e false caso contrario.
	 */
    public boolean isValidadeVigente(Date datProcessamento)
    {
        if(datProcessamento == null)
            return false;
        
        if(this.datInicioValidade == null)
            return false;
        
        if(this.datInicioValidade.compareTo(datProcessamento) > 0)
            return false;
        
        if(this.datFimValidade == null)
            return true;
        
        return (this.datFimValidade.compareTo(datProcessamento) >= 0);
    }
    
    /**
     * Verifica se o tipo de chamada desejado eh permitido nesta promocao
     * 
     * @param tipoChamada Tipo de chamada
     * @return boolean
     */
    public boolean permiteTipoChamada(String tipoChamada)
    {
    	return this.getTiposChamada().contains(tipoChamada);
    }
    
    /**
     * Verifica se o dia da semana desejado eh permitido para a promocao
     * @param diaSemana Dia da semana
     * @return boolean
     */
    public boolean permiteDiaSemana(int diaSemana)
    {
    	return this.getDiasSemana().contains(new Integer(diaSemana));
    }
    
    /**
     * Verifica se a promocao permite o dia da semana da data informada
     * @param data Data a ser verificada a permissao do dia da semana
     * @return boolean
     */
    public boolean permiteDiaSemana(Date data)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(data);
    	
    	return permiteDiaSemana(cal.get(Calendar.DAY_OF_WEEK));
    }
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		object					Objeto a ser comparado com o atual.
	 *	@return     True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
			return false;
		
		return (this.hashCode() == object.hashCode());
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
		result.append(String.valueOf(this.getIdtPromocao()));
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return     Representacao em formato String do objeto.
	 */
	public String toString()
	{
		return "Promocao: " + ((this.getNomPromocao() != null) ? this.getNomPromocao() : "NULL");
	}

}