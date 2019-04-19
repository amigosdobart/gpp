package com.brt.gpp.aplicacoes.promocao.entidade;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargasComparator;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.aplicacoes.recarregar.RecargaComparator;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe que representa as informacoes do assinante que esta em alguma Promocao Pula-Pula. A classe possui as
 *	informacoes comuns as promocoes, em adicao as informacoes referentes as ligacoes recebidas pelo assinante e o seu
 *	saldo de bonus a receber.
 * 
 *	@author	Daniel Ferreira
 *	@since	19/09/2005
 */
public class AssinantePulaPula extends PromocaoAssinante
{

	/**
	 *	Constante para data de credito do bonus.
	 */
	public static final int DATA_CREDITO = 5;
	
	/**
	 *	Constante para bonus agendados.
	 */
	public static final int BONUS_AGENDADOS = 6;
	
	/**
	 *	Constante para bonus concedidos.
	 */
	public static final int BONUS_CONCEDIDOS = 7;
	
	/**
	 *	Constante mes de execucao.
	 */
	public static final int MES_EXECUCAO = 8;
	
	/**
	 *	Datas de credito de bonus do assinante para os varios tipos de execucao.
	 */
	private Map datasCredito;
    
	/**
	 *	Totalizacao de ligacoes recebidas pelo assinante.
	 */
	private TotalizacaoPulaPula totalizacao;
	
	/**
	 *	Totalizacao de recargas efetuadas pelo assinante.
	 */
	private TotalizacaoRecargas totalRecargas;
	
	/**
	 *	Totalizacao do limite fale de graça à noite.
	 */
	private TotalizacaoFaleGratis totalizacaoFGN;
	
	/**
	 *	Informacoes de valores de bonus Pula-Pula por Codigo Nacional do assinante.
	 */
	private BonusPulaPula bonusCn;
	
	/**
	 *	Objeto contendo informacoes de valor de bonus Pula-Pula a ser concedido ao assinante.
	 */
	private	SaldoPulaPula saldo;
	
	/**
	 *	Lista de bonus Pula-Pula agendados na Fila de Recargas para o assinante.
	 */
	private Collection bonusAgendados;
	
	/**
	 *	Lista de bonus Pula-Pula concedidos ao assinante.
	 */
	private Collection bonusConcedidos;
	
	/**
	 *	Construtor da classe.
	 */
	public AssinantePulaPula()
	{
	    super();
	    
	    this.datasCredito		= null;
		this.totalizacao		= null;
		this.totalRecargas		= null;
		this.bonusCn			= null;
		this.saldo				= null;
		this.bonusAgendados		= null;
		this.bonusConcedidos	= null;
	}
	
	/**
	 *	Retorna as datas de credito do bonus Pula-Pula para o assinante.
	 * 
	 *	@return     Datas de credito do bonus Pula-Pula.
	 */
	public Collection getDatasCredito()
	{
	    Collection result = new ArrayList();
	    
	    if(this.datasCredito != null)
	        result.addAll(this.datasCredito.values());
	    
	    return result;
	}
	
	/**
	 *	Retorna as datas de credito do bonus Pula-Pula para o assinante.
	 * 
	 *	@param		tipoExecucao			Tipo de execucao da promocao.
	 *	@return     Data de credito do bonus Pula-Pula.
	 */
	public Date getDataCredito(String tipoExecucao)
	{
	    if(this.datasCredito != null)
	        return (Date)this.datasCredito.get(tipoExecucao);
	    
	    return null;
	}
	
	/**
	 *	Retorna as informacoes referentes ao tempo de ligacoes recebidas pelo assinante.
	 * 
	 *	@return     Tempo de ligacoes recebidas pelo assinante.
	 */
	public TotalizacaoPulaPula getTotalizacao() 
	{
		return this.totalizacao;
	}
	
	/**
	 *	Retorna as informacoes referentes as recargas efetuadas pelo assinante.
	 * 
	 *	@return     Recargas efetuadas pelo assinante.
	 */
	public TotalizacaoRecargas getTotalRecargas() 
	{
		return this.totalRecargas;
	}
	
	/**
	 *	Retorna o bonus Pula-Pula a ser aplicado no calculo do valor a ser concedido ao assinante. Este bonus e
	 *	determinado em funcao de seu Codigo Nacional.
	 * 
	 *	@return     Bonus Pula-Pula do codigo nacional do assinante.
	 */
	public BonusPulaPula getBonusCn() 
	{
		return this.bonusCn;
	}
	
	/**
	 *	Retorna o saldo de ligacoes recebidas a ser concedido ao assinante.
	 * 
	 *	@return     Saldo de ligacoes a ser concedido ao assinante.
	 */
	public SaldoPulaPula getSaldo()
	{
		return this.saldo;
	}
	
	/**
	 *	Retorna os bonus na Fila de Recargas a serem concedidos ao assinante.
	 * 
	 *	@return     Bonus a serem concedidos ao assinante.
	 */
	public Collection getBonusAgendados()
	{
	    ArrayList result = new ArrayList();
	    
	    if(this.bonusAgendados != null)
	        result.addAll(this.bonusAgendados);
	    
		return result;
	}
	
	/**
	 *	Retorna os bonus na Fila de Recargas a serem concedidos ao assinante pela execucao da promocao.
	 * 
	 *	@param		tipoExecucao			Tipo de execucao da promocao.
	 *	@return     Lista com os bonus a serem concedidos ao assinante.
	 */
	public Collection getBonusAgendados(String tipoExecucao)
	{
	    ArrayList result = new ArrayList();
	    
	    //Percorre a lista de todos os tipos de transacao da promocao do assinante associados ao tipo de execucao.
	    for(Iterator itTiposTransacao = super.getTiposTransacao(tipoExecucao).iterator(); itTiposTransacao.hasNext();)
	    {
	    	PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)itTiposTransacao.next();
	    	
	    	for(Iterator itBonusAgendado = this.getBonusAgendados().iterator(); itBonusAgendado.hasNext();)
	    	{
	    		FilaRecargas bonusAgendado = (FilaRecargas)itBonusAgendado.next();
	    		
	    		if(bonusAgendado.getTipTransacao().equals(tipoTransacao.getOrigem().getTipoTransacao()))
	    			result.add(bonusAgendado);
	    	}
	    }
	    
		return result;
	}

	/**
	 *	Retorna o bonus na Fila de Recarga a ser concedido ao assinante pela execucao da promocao.
	 * 
	 *	@param		tipoTransacao			Tipo de transacao do bonus.
	 *	@return     Bonus a ser concedido ao assinante.
	 */
	public FilaRecargas getBonusAgendado(PromocaoTipoTransacao tipoTransacao)
	{
		for(Iterator iterator = this.getBonusAgendados().iterator(); iterator.hasNext();)
		{
			FilaRecargas result = (FilaRecargas)iterator.next();
			
			if(result.getTipTransacao().equals(tipoTransacao.getOrigem().getTipoTransacao()))
				return result;
		}
		
		return null;
	}
	
	/**
	 *	Retorna os bonus ja concedidos ao assinante no mes.
	 * 
	 *	@return     Bonus concedidos ao assinante.
	 */
	public Collection getBonusConcedidos()
	{
	    ArrayList result = new ArrayList();
	    
	    if(this.bonusConcedidos != null)
	        result.addAll(this.bonusConcedidos);
	    
		return result;
	}
	
	/**
	 *	Retorna os bonus ja concedidos ao assinante no mes pela execucao da promocao.
	 * 
	 *	@param		tipoExecucao			Tipo de execucao da promocao.
	 *	@return     Lista com os bonus concedidos ao assinante.
	 */
	public Collection getBonusConcedidos(String tipoExecucao)
	{
	    ArrayList result = new ArrayList();
	    
	    //Percorre a lista de todos os tipos de transacao da promocao do assinante associados ao tipo de execucao.
	    for(Iterator itTiposTransacao = super.getTiposTransacao(tipoExecucao).iterator(); itTiposTransacao.hasNext();)
	    {
	    	PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)itTiposTransacao.next();
	    	
	    	for(Iterator itBonusConcedido = this.getBonusConcedidos().iterator(); itBonusConcedido.hasNext();)
	    	{
	    		Recarga bonusConcedido = (Recarga)itBonusConcedido.next();
	    		
	    		if(bonusConcedido.getTipTransacao().equals(tipoTransacao.getOrigem().getTipoTransacao()))
	    			result.add(bonusConcedido);
	    	}
	    }
	    
		return result;
	}
	
	/**
	 *	Atribui as datas de credito do bonus Pula-Pula.
	 * 
	 *	@param		datasCredito			Datas de credito do bonus Pula-Pula.
	 */
	public void setDatasCredito(Map datasCredito)
	{
	    this.datasCredito = datasCredito;
	}
	
	/**
	 *	Atribui as informacoes referentes ao tempo de ligacoes recebidas pelo assinante.
	 * 
	 *	@param		totalizacao				Tempo de ligacoes recebidas pelo assinante.
	 */
	public void setTotalizacao(TotalizacaoPulaPula totalizacao) 
	{
		this.totalizacao = totalizacao;
	}
		
	/**
	 *	Atribui as informacoes referentes as recargas efetuadas pelo assinante.
	 * 
	 *	@param     totalRecargas			Recargas efetuadas pelo assinante.
	 */
	public void setTotalRecargas(TotalizacaoRecargas totalRecargas) 
	{
		this.totalRecargas = totalRecargas;
	}
	
	/**
	 * @return Totalizacao do limite fale de graça à noite.
	 */
	public TotalizacaoFaleGratis getTotalizacaoFGN() 
	{
		return this.totalizacaoFGN;
	}

	/**
	 * @param totalizacaoFGN Totalizacao do limite fale de graça à noite.
	 */
	public void setTotalizacaoFGN(TotalizacaoFaleGratis totalizacaoFGN) 
	{
		this.totalizacaoFGN = totalizacaoFGN;
	}

	/**
	 *	Atribui o bonus Pula-Pula a ser aplicado no calculo do valor a ser concedido ao assinante. Este bonus e
	 *	determinado em funcao de seu Codigo Nacional.
	 * 
	 *	@param		bonusCN					Bonus Pula-Pula do codigo nacional do assinante.
	 */
	public void setBonusCn(BonusPulaPula bonusCn) 
	{
		this.bonusCn = bonusCn;
	}
	
	/**
	 *	Atribui o saldo de ligacoes recebidas a ser concedido ao assinante.
	 * 
	 *	@param		saldo					Saldo de ligacoes a ser concedido pelo assinante.
	 */
	public void setSaldo(SaldoPulaPula saldo)
	{
		this.saldo = saldo;
	}
	
	/**
	 *	Atribui os bonus na Fila de Recargas a serem concedidos ao assinante.
	 * 
	 *	@param		bonusAgendados			Bonus a serem concedidos ao assinante.
	 */
	public void setBonusAgendados(Collection bonusAgendados)
	{
		this.bonusAgendados = bonusAgendados;
	}
	
	/**
	 *	Atribui os bonus ja concedidos ao assinante no mes.
	 * 
	 *	@param		bonusConcedidos			Bonus concedidos ao assinante.
	 */
	public void setBonusConcedidos(Collection bonusConcedidos)
	{
		this.bonusConcedidos = bonusConcedidos;
	}

    /**
     *	Bloqueia as informacoes referentes a concessao de bonus Pula-Pula do assinante para consulta.
     */
    public void bloquearConsulta()
    {
    	this.setDatExecucao(null);
    	this.setDatasCredito(new TreeMap());
    	this.setTotalizacao(null);
    	this.setTotalRecargas(null);
    	this.setBonusCn(null);
    	this.setBonusAgendados(new ArrayList());
    	this.setBonusConcedidos(new ArrayList());
    	this.setSaldo(new SaldoPulaPula());
    }
	
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna String vazia. 
	 *	OBS: No caso de data de credito o valor retornado sera de acordo com a execucao default.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return     Valor no formato String.
	 */
	public String format(int campo)
	{
		Date dataCredito = null;
		
	    switch(campo)
	    {
	    	case AssinantePulaPula.DATA_CREDITO:
	    	    dataCredito = this.getDataCredito(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT);
	    		SimpleDateFormat conversorDate = new SimpleDateFormat("dd/MM/yyyy");
	    	    return (dataCredito != null) ? conversorDate.format(dataCredito) : "";
	    	case AssinantePulaPula.MES_EXECUCAO:
	    		SimpleDateFormat conversorDatMes = new SimpleDateFormat("yyyyMM");
	    	    dataCredito = this.getDataCredito(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT);
	    		if(dataCredito != null)
	    			return conversorDatMes.format(dataCredito);
	    		if(this.datExecucao != null)
	    			return conversorDatMes.format(this.datExecucao);
	    		Calendar calMes = Calendar.getInstance();
	    		calMes.set(Calendar.DAY_OF_MONTH, calMes.getActualMinimum(Calendar.DAY_OF_MONTH));
	    		calMes.add(Calendar.MONTH, 1);
	    		return conversorDatMes.format(calMes.getTime());
	    	default: return super.format(campo); 
	    }
	}
	
	/**
	 *	Ordena e retorna a lista selecionada. Se a lista for invalida, nao faz nada.
	 * 
	 *	@param		lista					Lista selecionada. Se a lista for invalida, retorna NULL.
	 *	@return		Lista ordenada.						
	 */
	public Collection sort(int lista)
	{
	    ArrayList result = new ArrayList();
	    Comparator comparator = null;
	    
	    switch(lista)
	    {
	    	case AssinantePulaPula.BONUS_AGENDADOS:
	    	    if(this.bonusAgendados != null)
	    	    {
	    	        result.addAll(this.bonusAgendados);
	    	        comparator = new FilaRecargasComparator();
	    	    }
	    	    break;
	    	case AssinantePulaPula.BONUS_CONCEDIDOS:
	    	    if(this.bonusConcedidos != null)
	    	    {
	    	        result.addAll(this.bonusConcedidos);
	    	        comparator = new RecargaComparator();
	    	    }
	    	    break;
	    	default: return null;
	    }
	    
	    Collections.sort(result, comparator);
	    return result;
	}
	
	/**
	 *	Cria e retorna a lista de objetos FilaRecargas contendo os bonus da promocao Pula-Pula do assinante.
	 *
	 *	@param		tipoExecucao			Tipo de execucao da concessao.
	 *	@return		Lista de objetos FilaRecargas contendo os bonus da promocao Pula-Pula do assinante.
	 */
	public List toFilaRecargas(String tipoExecucao)
	{
		ArrayList result = new ArrayList();
		
		//Percorrendo a lista de todos os tipos de transacao aplicaveis ao tipo de execucao da promocao do 
		//assinante. Cada um destes tipos de transacao tem um tipo de bonificacao associado. Os tipos de transacao 
		//definem os lancamentos, enquanto que os tipos de bonificacao definem os valores.
		for(Iterator iterator = super.getTiposTransacao(tipoExecucao).iterator(); iterator.hasNext();)
		{
			//Obtendo o tipo de transacao.
			PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)iterator.next();
			//Obtendo a bonificacao.
			BonificacaoPulaPula bonificacao = this.getSaldo().getBonificacao(tipoTransacao.getTipoBonificacao());
			//Obtendo as informacoes para envio de SMS.
			PromocaoInfosSms infosSms = super.getInfosSms(tipoExecucao, tipoTransacao.getTipoBonificacao());
			
			//Construindo e populando o objeto FilaRecargas
			FilaRecargas bonus = new FilaRecargas();
			
			bonus.setIdtMsisdn(this.getIdtMsisdn());
			bonus.setTipTransacao(tipoTransacao.getOrigem().getTipoTransacao());
			bonus.setDatCadastro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			Date datExecucao = this.getDataCredito(tipoExecucao);
			bonus.setDatExecucao((datExecucao != null) ? new Timestamp(datExecucao.getTime()) : null);
			bonus.setValorCredito(bonificacao.getTipoSaldo(), bonificacao.getValorAReceber());
			bonus.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA));
			bonus.setIndZerarSaldoPeriodico(tipoTransacao.zerarSaldoPeriodico() ? new Integer(1) : new Integer(0));
			bonus.setIndZerarSaldoBonus(tipoTransacao.zerarSaldoBonus() ? new Integer(1) : new Integer(0));
			bonus.setIndZerarSaldoSms(tipoTransacao.zerarSaldoTorpedos() ? new Integer(1) : new Integer(0));
			bonus.setIndZerarSaldoGprs(tipoTransacao.zerarSaldoDados() ? new Integer(1) : new Integer(0));
            
            //Inserindo informacoes referentes ao envio de SMS, caso necessario.
            if(infosSms != null)
            {
            	bonus.setIndEnviaSms(infosSms.enviaSms() ? new Integer(1) : new Integer(0));
            	bonus.setTipSms(infosSms.getTipSms());
            	bonus.setNumPrioridade(new Integer(infosSms.getNumPrioridade()));
            	
            	//Formatando a mensagem.
            	String mensagem = infosSms.getDesMensagem();
            	mensagem = mensagem.replaceAll(Definicoes.PATTERN_PROMOCAO, super.getPromocao().getNomPromocao());
            	mensagem = mensagem.replaceAll(Definicoes.PATTERN_VALOR, 
            								   bonificacao.format(BonificacaoPulaPula.VALOR_A_RECEBER));
            	bonus.setDesMensagem(mensagem);
            }
            
			result.add(bonus);
		}
		
		return result;
	}
	
	/**
	 *	@see		java.lang.Object#toString()						
	 */
	public String toString()
	{
		return super.toString();
	}
	
}
