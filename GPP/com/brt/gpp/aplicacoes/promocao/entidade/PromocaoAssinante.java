package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCodigoNacional;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimite;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteDinamico;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoPlanoPreco;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoTransacao;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_ASSINANTE.
 *
 *	@author	Daniel Ferreira
 *	@since	09/08/2005
 */

public class PromocaoAssinante
{
    
	/**
	 *	Constante para data de execucao.
	 */
	public static final int DAT_EXECUCAO = 0;
	
	/**
	 *	Constante para data de entrada em promocao.
	 */
	public static final int	DAT_ENTRADA_PROMOCAO = 1;
	
	/**
	 *	Constante para data de inicial de analise.
	 */
	public static final int	DAT_INICIO_ANALISE = 2;
	
	/**
	 *	Constante para data final de analise.
	 */
	public static final int DAT_FIM_ANALISE = 3;
	
	/**
	 *	Constante para data de ultimo bonus da execucao DEFAULT.
	 */
	public static final int DAT_ULTIMO_BONUS = 4;
	
	/**
	 *	MSISDN do assinante.
	 */
	protected String idtMsisdn;
	
	/**
	 *	Informacoes da promocao do assinante.
	 */
	protected Promocao promocao;
	
	/**
	 *	Data de execucao do assinante na promocao.
	 */
	protected Date datExecucao;
	
	/**
	 *	Data de entrada do assinante na promocao.
	 */
	protected Date datEntradaPromocao;
	
	/**
	 *	Data inicial de analise.
	 */
	protected Date datInicioAnalise;
	
	/**
	 *	Data final de analise.
	 */
	protected Date datFimAnalise;
	
	/**
	 *	Indicador de isencao de limite.
	 */
	protected boolean indIsentoLimite;
	
	/**
	 *	Data de concessao do ultimo bonus para o tipo de execucao DEFAULT.
	 */
	protected Date datUltimoBonus;
	
	/**
	 *	Status do assinante.
	 */
	protected PromocaoStatusAssinante status;
	
	/**
	 *	Mapeamento Promocao / Codigo Nacional.
	 */
	protected PromocaoCodigoNacional codigoNacional;
	
	/**
	 *	Mapeamento Promocao / Plano de Preco.
	 */
	protected PromocaoPlanoPreco planoPreco;
	
	/**
	 *	Lista de dias de execucao do assinante para os varios tipos de execucao.
	 */
	protected Collection diasExecucao;
	
	/**
	 *	Lista de informacoes para envio de SMS para os varios tipos de execucao.
	 */
	protected Collection infosSms;
	
	/**
	 *	Lista de tipos de transacao para os varios tipos de execucao.
	 */
	protected Collection tiposTransacao;
	
	/**
	 *	Informacoes do assinante na plataforma.
	 */
	protected Assinante assinante;
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoAssinante()
	{
		this.idtMsisdn			= null;
		this.promocao			= null;
		this.datExecucao		= null;
		this.datEntradaPromocao	= null;
		this.datInicioAnalise	= null;
		this.datFimAnalise		= null;
		this.indIsentoLimite	= false;
		this.status				= null;
		this.codigoNacional		= null;
		this.planoPreco			= null;
		this.diasExecucao		= new ArrayList();
		this.infosSms			= new ArrayList();
		this.tiposTransacao		= new ArrayList();
		this.assinante			= null;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 * 
	 *	@return		MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna a promocao do assinante.
	 * 
	 *	@return		Promocao do assinante.
	 */
	public Promocao getPromocao() 
	{
		return this.promocao;
	}
	
	/**
	 *	Retorna a data de execucao do assinante na promocao. Esta data de execucao e referente ao tipo de execucao 
	 *	normal (default). Outros dias de execucao devem ser obtidos na tabela TBL_PRO_DIA_EXECUCAO.
	 * 
	 *	@return		Data de execucao da promocao do assinante.
	 */
	public Date getDatExecucao() 
	{
		return this.datExecucao;
	}
	
	/**
	 *	Retorna a data de entrada do assinante na promocao.
	 * 
	 *	@return		Data de entrada do assinante na promocao.
	 */
	public Date getDatEntradaPromocao() 
	{
		return this.datEntradaPromocao;
	}
	
	/**
	 *	Retorna a data de inicio de analise.
	 * 
	 *	@return		Data de inicio de analise.
	 */
	public Date getDatInicioAnalise() 
	{
		return this.datInicioAnalise;
	}
	
	/**
	 *	Retorna a data de fim de analise.
	 * 
	 *	@return		Data de fim de analise.
	 */
	public Date getDatFimAnalise() 
	{
		return this.datFimAnalise;
	}
	
    /**
     *  Retorna uma representacao numerica do indicador de isencao de limite do assinante. Deve ser utilizado 
     *  somente para atualizacao do registro do assinante na tabela do banco de dados.
     *
     *  @return     Representacao numerica do indicador de isencao de limite.
     */
	public short getIndIsentoLimite()
	{
		return (short)((this.indIsentoLimite) ? 1 : 0);
	}
	
    /**
     *  Indica se o assinante e isento do limite da bonificacao da promocao.
     *
     *  @param		tipoBonificacao			Tipo de bonificacao.
     *  @param      data                    Data informada.
     *  @return     True se o assinante e isento ou false caso contrario.
     */
    public boolean isIsentoLimite(PromocaoTipoBonificacao tipoBonificacao, Date data)
    {
        if(!this.getPromocao().temLimite(tipoBonificacao, data))
            return true;
        
        if(!this.getPromocao().getLimite(tipoBonificacao, data).permiteIsencaoLimite())
            return false;
        
        return this.indIsentoLimite;
    }
    
    /**
     *  Indica se o assinante e isento do limite da bonificacao da promocao.
     *
     *  @param		tipoBonificacao			Tipo de bonificacao.
     *  @param      mes                     Mes informado.
     *  @return     True se o assinante e isento ou false caso contrario.
     *  @throws     ParseException
     */
    public boolean isIsentoLimite(PromocaoTipoBonificacao tipoBonificacao, String mes) throws ParseException
    {
        if(!this.getPromocao().temLimite(tipoBonificacao, mes))
            return true;
        
        if(!this.getPromocao().getLimite(tipoBonificacao, mes).permiteIsencaoLimite())
            return false;
        
        return this.indIsentoLimite;
    }
    
	/**
	 *	Retorna a data de ultimo bonus concedido do tipo DEFAULT.
	 * 
	 *	@return		Data de ultimo bonus da concessao DEFAULT.
	 */
	public Date getDatUltimoBonus() 
	{
		return this.datUltimoBonus;
	}
	
	/**
	 *	Retorna o status da promocao do assinante.
	 * 
	 *	@return		Status da promocao do assinante.
	 */
	public PromocaoStatusAssinante getStatus() 
	{
		return this.status;
	}
	
	/**
	 *	Retorna o relacionamento entre a promocao e o codigo nacional do assinante.
	 * 
	 *	@return		Relacionamento Promocao / Codigo Nacional.
	 */
	public PromocaoCodigoNacional getCodigoNacional() 
	{
		return this.codigoNacional;
	}
	
	/**
	 *	Retorna o relacionamento entre a promocao e o plano de preco do assinante.
	 * 
	 *	@return		Relacionamento Promocao / Plano de Preco.
	 */
	public PromocaoPlanoPreco getPlanoPreco() 
	{
		return this.planoPreco;
	}
	
	/**
	 *	Retorna os mapeamentos de dias de execucao da promocao para o assinante.
	 * 
	 *	@return		Mapeamentos Promocao / Dias de Execucao.
	 */
	public Collection getDiasExecucao() 
	{
		return this.diasExecucao;
	}
	
	/**
	 *	Retorna o mapeamento de dia de execucao da promocao para o assinante.
	 * 
	 *	@param		tipExecucao				Tipo de execucao da promocao.
	 *	@return		Mapeamento Promocao / Dia de Execucao.
	 */
	public PromocaoDiaExecucao getDiaExecucao(String tipExecucao) 
	{
	    if(this.diasExecucao != null)
	        for(Iterator iterator = this.diasExecucao.iterator(); iterator.hasNext();)
	        {
	            PromocaoDiaExecucao result = (PromocaoDiaExecucao)iterator.next();
	            
	            if((result != null) && (result.getTipExecucao() != null) &&
	               (result.getTipExecucao().equals(tipExecucao)))
	                return result;
	        }
	    
	    return null;
	}
	
	/**
	 *	Retorna as informacoes de envio de SMS durante a execucao do processo da promocao do assinante.
	 * 
	 *	@return		Informacoes de envio de SMS.
	 */
	public Collection getInfosSms() 
	{
		return this.infosSms;
	}
	
	/**
	 *	Retorna as informacoes de envio de SMS durante a execucao do processo da promocao do assinante.
	 *
	 *	@param		tipExecucao				Tipo de execucao da promocao.
	 *	@return		Informacoes de envio de SMS.
	 */
	public Collection getInfosSms(String tipExecucao) 
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = this.getInfosSms().iterator(); iterator.hasNext();)
		{
			PromocaoInfosSms infosSms = (PromocaoInfosSms)iterator.next();
			
			if(infosSms.getTipExecucao().equals(tipExecucao))
				result.add(infosSms);
		}
		
	    return result;
	}
	
	/**
	 *	Retorna as informacoes de envio de SMS durante a execucao do processo da promocao do assinante.
	 *
	 *	@param		tipExecucao				Tipo de execucao da promocao.
	 *	@param		tipoBonificacao			Tipo de bonificacao.
	 *	@return		Informacoes de envio de SMS.
	 */
	public PromocaoInfosSms getInfosSms(String tipExecucao, PromocaoTipoBonificacao tipoBonificacao) 
	{
		for(Iterator iterator = this.getInfosSms().iterator(); iterator.hasNext();)
		{
			PromocaoInfosSms result = (PromocaoInfosSms)iterator.next();
			
			if(result.getTipExecucao().equals(tipExecucao) && result.getTipoBonificacao().equals(tipoBonificacao))
				return result;
		}
		
	    return null;
	}
	
	/**
	 *	Retorna os tipos de transacao dos bonus concedidos ao assinante pela promocao.
	 * 
	 *	@return		Tipos de transacao dos bonus.
	 */
	public Collection getTiposTransacao() 
	{
		return this.tiposTransacao;
	}
	
	/**
	 *	Retorna os tipos de transacao dos bonus concedidos ao assinante pela promocao.
	 * 
	 *	@param		tipoExecucao			Tipo de execucao da promocao.
	 *	@return		Tipos de transacao dos bonus.
	 */
	public SortedSet getTiposTransacao(String tipoExecucao) 
	{
		TreeSet result = new TreeSet();
		
		for(Iterator iterator = this.getTiposTransacao().iterator(); iterator.hasNext();)
		{
			PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)iterator.next();
			
			if(tipoTransacao.getTipExecucao().equals(tipoExecucao))
				result.add(tipoTransacao);
		}
		
	    return result;
	}
	
	/**
	 *	Retorna o tipo de transacao do bonus concedido ao assinante pela promocao.
	 * 
	 *	@param		tipoExecucao			Tipo de execucao da promocao.
	 *	@param		tipoBonificacao			Tipo de bonificacao.
	 *	@return		Tipo de transacao do bonus.
	 */
	public PromocaoTipoTransacao getTipoTransacao(String tipoExecucao, PromocaoTipoBonificacao tipoBonificacao)
	{
		for(Iterator iterator = this.getTiposTransacao().iterator(); iterator.hasNext();)
		{
			PromocaoTipoTransacao result = (PromocaoTipoTransacao)iterator.next();
			
			if(result.getTipExecucao().equals(tipoExecucao) && result.getTipoBonificacao().equals(tipoBonificacao))
				return result;
		}
		
		return null;
	}

	/**
	 *	Retorna os lancamentos (origens de recarga) de todos os bonus concedidos ao assinante.
	 * 
	 *	@return		Lancamentos dos bonus concedidos ao assinante.
	 */
	public Set getLancamentos()
	{
		HashSet result = new HashSet();
		
		for(Iterator iterator = this.getTiposTransacao().iterator(); iterator.hasNext();)
			result.add(((PromocaoTipoTransacao)iterator.next()).getOrigem());
			
		return result;
	}
	
	/**
	 *	Indica se o lancamento esta definido para o tipo de execucao informado.
	 * 
	 *	@param		lancamento				Lancamento do bonus.
	 *	@param		tipoExecucao			Tipo de execucao da concessao da promocao.
	 *	@return		True se o lancamento estiver definido e false caso contrario.
	 */
	public boolean matches(OrigemRecarga lancamento, String tipoExecucao)
	{
		for(Iterator iterator = this.getTiposTransacao(tipoExecucao).iterator(); iterator.hasNext();)
		{
			PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)iterator.next();
			
			if(tipoTransacao.getOrigem().equals(lancamento))
				return true;
		}
		
		return false;
	}
	
	/**
	 *	Retorna a lista de tipos de bonificacao da promocao do assinante.
	 * 
	 *	@return		Tipos de bonificacao.
	 */
	public Set getTiposBonificacao()
	{
		TreeSet result = new TreeSet();
		
		for(Iterator iterator = this.getTiposTransacao().iterator(); iterator.hasNext();)
			result.add(((PromocaoTipoTransacao)iterator.next()).getTipoBonificacao());
			
		return result;
	}
	
    /**
     *  Retorna as informacoes do limite da promocao do assinante.
     * 
     *  @param		tipoBonificacao			Tipo de bonificacao.
     *  @param		data                    Data informada.
     *  @return		Informacoes do limite da promocao do assinante.
     */
    public PromocaoLimite getLimite(PromocaoTipoBonificacao tipoBonificacao, Date data) 
    {
    	return this.getPromocao().getLimite(tipoBonificacao, data); 
    }
    
    /**
     *  Retorna as informacoes do limite da Promocao Pula-Pula do assinante.
     * 
     *  @param		tipoBonificacao			Tipo de bonificacao.
     *  @return     Informacoes do limite da Promocao Pula-Pula do assinante.
     *  @throws     ParseException
     */
    public PromocaoLimite getLimite(PromocaoTipoBonificacao tipoBonificacao, String mes) throws ParseException 
    {
    	return this.getPromocao().getLimite(tipoBonificacao, mes); 
    }
    
    /**
     *  Retorna as informacoes para calculo de limite dinamico da Promocao Pula-Pula do assinante.
     * 
     *  @param      data                    Data informada.
     *  @return     Informacoes para calculo de limite dinamico da Promocao Pula-Pula do assinante.
     */
    public PromocaoLimiteDinamico getLimiteDinamico(Date data) 
    {
    	return this.getPromocao().getLimiteDinamico(data); 
    }
    
    /**
     *  Retorna as informacoes para calculo de limite dinamico da Promocao Pula-Pula do assinante.
     * 
     *  @param      mes                     Mes informado.
     *  @return     Informacoes para calculo de limite dinamico da Promocao Pula-Pula do assinante.
     *  @throws     ParseException
     */
    public PromocaoLimiteDinamico getLimiteDinamico(String mes) throws ParseException 
    {
    	return this.getPromocao().getLimiteDinamico(mes); 
    }
    
	/**
	 *	Retorna as informacoes do assinante na plataforma.
	 * 
	 *	@return		Informacoes do assinante na plataforma.
	 */
	public Assinante getAssinante() 
	{
		return this.assinante;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		idtMsisdn				MSISDN do assinante.
	 */
    public void setIdtMsisdn(String idtMsisdn)
    {
        this.idtMsisdn = idtMsisdn;
    }
    
	/**
	 *	Atribui a promocao do assinante.
	 * 
	 *	@param		promocao				Promocao do assinante.
	 */
    public void setPromocao(Promocao promocao)
    {
        this.promocao = promocao;
    }
    
	/**
	 *	Atribui a data de execucao do assinante na promocao. Esta data de execucao e referente ao tipo de execucao 
	 *	normal (default). Outros dias de execucao devem ser obtidos na tabela TBL_PRO_DIA_EXECUCAO.
	 * 
	 *	@param		datExecucao				Data de execucao da promocao do assinante.
	 */
    public void setDatExecucao(Date datExecucao)
    {
        this.datExecucao = datExecucao;
    }
    
	/**
	 *	Atribui a data de entrada do assinante na promocao.
	 * 
	 *	@param		datEntradaPromocao		Data de entrada do assinante na promocao.
	 */
    public void setDatEntradaPromocao(Date datEntradaPromocao)
    {
        this.datEntradaPromocao = datEntradaPromocao;
    }
    
	/**
	 *	Atribui a data de inicio de analise.
	 * 
	 *	@param		datInicioAnalise		Data de inicio de analise.
	 */
    public void setDatInicioAnalise(Date datInicioAnalise)
    {
        this.datInicioAnalise = datInicioAnalise;
    }
    
	/**
	 *	Atribui a data de fim de analise.
	 * 
	 *	@param		datFimAnalise			Data de fim de analise.
	 */
    public void setDatFimAnalise(Date datFimAnalise)
    {
        this.datFimAnalise = datFimAnalise;
    }
    
	/**
	 *	Atribui o indicador de isencao de limite da promocao.
	 * 
	 *	@param		indIsentoLimite			Indicador de isencao de limite da promocao.
	 */
    public void setIndIsentoLimite(short indIsentoLimite)
    {
        this.indIsentoLimite = (indIsentoLimite != 0);
    }
    
	/**
	 *	Atribui o indicador de isencao de limite da promocao.
	 * 
	 *	@param		indIsentoLimite			Indicador de isencao de limite da promocao.
	 */
    public void setIndIsentoLimite(boolean indIsentoLimite)
    {
        this.indIsentoLimite = indIsentoLimite;
    }
    
	/**
	 *	Atribui a data de ultimo bonus concedido do tipo DEFAULT.
	 * 
	 *	@param		datUltimoBonus			Data de ultimo bonus da concessao DEFAULT.
	 */
	public void setDatUltimoBonus(Date datUltimoBonus) 
	{
		this.datUltimoBonus = datUltimoBonus;
	}
	
	/**
	 *	Atribui o status da promocao do assinante.
	 * 
	 *	@param		status					Status da promocao do assinante.
	 */
	public void setStatus(PromocaoStatusAssinante status) 
	{
		this.status = status;
	}
	
	/**
	 *	Atribui o relacionamento entre a promocao e o codigo nacional do assinante.
	 * 
	 *	@param		codigoNacional			Relacionamento Promocao / Codigo Nacional.
	 */
    public void setCodigoNacional(PromocaoCodigoNacional codigoNacional)
    {
        this.codigoNacional = codigoNacional;
    }
    
	/**
	 *	Atribui o relacionamento entre a promocao e o plano de preco do assinante.
	 * 
	 *	@param		planoPreco				Relacionamento Promocao / Plano de Preco.
	 */
    public void setPlanoPreco(PromocaoPlanoPreco planoPreco)
    {
        this.planoPreco = planoPreco;
    }
    
	/**
	 *	Atribui os mapeamentos de dias de execucao da promocao para o assinante.
	 * 
	 *	@param		diasExecucao			Mapeamentos Promocao / Dias de Execucao.
	 */
	public void setDiasExecucao(Collection diasExecucao) 
	{
		this.diasExecucao = diasExecucao;
	}
	
	/**
	 *	Atribui as informacoes de envio de SMS durante a execucao do processo da promocao do assinante.
	 * 
	 *	@param		infosSms				Informacoes de envio de SMS.
	 */
	public void setInfosSms(Collection infosSms) 
	{
		this.infosSms = infosSms;
	}
	
	/**
	 *	Atribui os tipos de transacao dos bonus concedidos pela promocao do assinante.
	 * 
	 *	@param		tiposTransacao			Tipos de transacao dos bonus.
	 */
	public void setTiposTransacao(Collection tiposTransacao) 
	{
		this.tiposTransacao = tiposTransacao;
	}
	
	/**
	 *	Atribui as informacoes do assinante na plataforma.
	 * 
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 */
    public void setAssinante(Assinante assinante)
    {
        this.assinante = assinante;
    }    
    
    /**
     *  Indica se a promocao Pula-Pula do assinante tem limite.
     * 
     *  @param		tipoBonificacao			Tipo de bonificacao.
     *  @param      data                    Data informada.
     *  @return     True se o Pula-Pula do assinante tem limite e false caso contrario.
     */
    public boolean temLimite(PromocaoTipoBonificacao tipoBonificacao, Date data)
    {
        return (this.getLimite(tipoBonificacao, data) != null);
    }
    
    /**
     *  Indica se a promocao do assinante tem limite.
     * 
     *  @param		tipoBonificacao			Tipo de bonificacao.
     *  @param      mes                     Mes informado.
     *  @return     True se a promocao do assinante tem limite e false caso contrario.
     *  @throws     ParseException
     */
    public boolean temLimite(PromocaoTipoBonificacao tipoBonificacao, String mes) throws ParseException
    {
        return (this.getLimite(tipoBonificacao, mes) != null);
    }
    
    /**
     *  Indica se o limite da promocao do assinante e dinamico.
     * 
     *  @param      data                    Data informada.
     *  @return     True se a promocao do assinante tem limite e false caso contrario.
     */
    public boolean temLimiteDinamico(Date data)
    {
        return (this.getLimiteDinamico(data) != null);
    }
    
    /**
     *  Indica se o limite da promocao do assinante e dinamico.
     * 
     *  @param      mes                     Data informada.
     *  @return     True se a promocao do assinante tem limite e false caso contrario.
     *  @throws     ParseException
     */
    public boolean temLimiteDinamico(String mes) throws ParseException
    {
        return (this.getLimiteDinamico(mes) != null);
    }
    
    /**
     *  Indica se a promocao do assinante deve zerar algum saldo durante a execucao definida pelo tipo informado.
     * 
     *  @param      tipoExecucao			Tipo de execucao da promocao do assinante.
     *  @return     True se a promocao do assinante deve zerar algum saldo e false caso contrario.
     */
    public boolean zerarSaldo(String tipoExecucao)
    {
    	for(Iterator iterator = this.getTiposTransacao(tipoExecucao).iterator(); iterator.hasNext();)
    	{
    		PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)iterator.next();
    		
    		if(tipoTransacao.zerarSaldoBonus() || tipoTransacao.zerarSaldoTorpedos() || tipoTransacao.zerarSaldoDados())
    			return true;
    	}
    	
    	return false;
    }
    
	/**
	 *	Preenche o objeto passado por parametro com os valores do objeto atual. Desta forma os dois objetos
	 *	compartilham os atributos, ou seja, modificacoes em um objeto influenciarao o estado do outro. Se o objeto 
	 *	passado por parametro for null, o metodo nao faz nada.
	 * 
	 *	@param		PromocaoAssinante		Objeto a ser preenchido com os atributos. 
	 */
    public void fill(PromocaoAssinante pAssinante)
    {
        if(pAssinante != null)
        {
            pAssinante.setIdtMsisdn(this.idtMsisdn);
    		pAssinante.setPromocao(this.promocao);
    		pAssinante.setDatExecucao(this.datExecucao);
    		pAssinante.setDatEntradaPromocao(this.datEntradaPromocao);
    		pAssinante.setDatInicioAnalise(this.datInicioAnalise);
    		pAssinante.setDatFimAnalise(this.datFimAnalise);
    		pAssinante.setIndIsentoLimite(this.indIsentoLimite);
    		pAssinante.setDatUltimoBonus(this.datUltimoBonus);
    		pAssinante.setStatus(this.status);
    		pAssinante.setCodigoNacional(this.codigoNacional);
    		pAssinante.setPlanoPreco(this.planoPreco);
    		pAssinante.setDiasExecucao(this.diasExecucao);
    		pAssinante.setInfosSms(this.infosSms);
    		pAssinante.setTiposTransacao(this.tiposTransacao);
    		pAssinante.setAssinante(this.assinante);
        }
    }
    
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param      Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return     Valor no formato String.
	 */
    public String format(int campo)
    {
    	SimpleDateFormat conversorDate = new SimpleDateFormat("dd/MM/yyyy");
    	
	    switch(campo)
	    {
	    	case PromocaoAssinante.DAT_EXECUCAO:
	    	    return (this.datExecucao != null) ? conversorDate.format(this.datExecucao) : "";
	    	case PromocaoAssinante.DAT_ENTRADA_PROMOCAO:
	    	    return (this.datEntradaPromocao != null) ? conversorDate.format(this.datEntradaPromocao) : "";
	    	case PromocaoAssinante.DAT_INICIO_ANALISE:
	    	    return (this.datInicioAnalise != null) ? conversorDate.format(this.datInicioAnalise) : "";
	    	case PromocaoAssinante.DAT_FIM_ANALISE:
	    	    return (this.datFimAnalise != null) ? conversorDate.format(this.datFimAnalise) : "";
	    	case PromocaoAssinante.DAT_ULTIMO_BONUS:
	    	    return (this.datUltimoBonus != null) ? conversorDate.format(this.datUltimoBonus) : "";
	    	default: return "";
	    }
    }
    
    /**
     *  Extrai uma lista de objetos Promocao a partir do objeto informado, que pode ser da classe PromocaoAssinante
     *  ou uma lista de objetos PromocaoAssinante.
     * 
     *  @param      obj                     Objeto de onde e extraida a lista de objetos Promocao.
     *  @return     Lista de objetos Promocao.
     */
    public static Collection extrairPromocoes(Object obj)
    {
        Collection result = new ArrayList();
        
        if(obj instanceof PromocaoAssinante)
            result.add(((PromocaoAssinante)obj).getPromocao());
        
        if(obj instanceof Collection)
            for(Iterator iterator = ((Collection)obj).iterator(); iterator.hasNext();)
                result.add(((PromocaoAssinante)iterator.next()).getPromocao());
            
        return result;
    }
    
    /**
     *  @see		java.lang.Object#toString()
     */
    public String toString()
    {
    	return "MSISDN: " + this.idtMsisdn + " - " + this.promocao;
    }
    
}
