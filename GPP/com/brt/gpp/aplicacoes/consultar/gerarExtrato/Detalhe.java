package com.brt.gpp.aplicacoes.consultar.gerarExtrato;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;

public class Detalhe 
{
	// Atributos da Classe
    
	private String			numeroLinha;
	private String			numeroOrigem;
	private String			data;
	private String			hora;
	private String			tipo;
	private String			operacao;
	private String			regiaoOrigem;
	private String			regiaoDestino;
	private String			numeroDestino;
	private String			duracao;
	private String			desTarifa;
	private int				tipoTarifa;
	private double			valor;
	private double			saldo;
	// Multiplo Saldo
	private double			bonus;
	private double			bonus_saldo;
	private double			GPRS;
	private double			GPRS_saldo;
	private double			SMS;
	private double			SMS_saldo;
	private double			periodico;
	private double			periodico_saldo;
	
	private double			total;
	private double			total_saldo;
	
	private boolean			indRecarga;
	
	private DecimalFormat	conversorDouble;

	//Constantes internas.
	
	public static final int	FORMAT_DURACAO		=  0;
	public static final int	FORMAT_VALOR		=  1;
	public static final int	FORMAT_SALDO		=  2;
	public static final int	FORMAT_BONUS		=  3;
	public static final int	FORMAT_BONUS_SALDO	=  4;
	public static final int	FORMAT_SMS			=  5;
	public static final int	FORMAT_SMS_SALDO	=  6;
	public static final int	FORMAT_GPRS			=  7;
	public static final int	FORMAT_GPRS_SALDO	=  8;
	public static final int	FORMAT_TOTAL		=  9;
	public static final int	FORMAT_TOTAL_SALDO	= 10;
	
	/**
	 *	Construtor da classe.
	 */
	public Detalhe()
	{
	    this.numeroLinha = null;
	    this.numeroOrigem = null;
	    this.data = null;
	    this.hora = null;
	    this.tipo = null;
	    this.operacao = null;
	    this.regiaoOrigem = null;
	    this.regiaoDestino = null;
	    this.numeroDestino = null;
	    this.duracao = null;
	    this.desTarifa = null;
	    this.tipoTarifa = 0;
	    this.valor = 0.0;
	    this.saldo = 0.0;
	    this.bonus = 0.0;
	    this.bonus_saldo = 0.0;
	    this.GPRS = 0.0;
	    this.GPRS_saldo = 0.0;
	    this.SMS = 0.0;
	    this.SMS_saldo = 0.0;
	    this.periodico = 0.0;
	    this.periodico_saldo = 0.0;
	    this.total = 0.0;
	    this.total_saldo = 0.0;
	    this.indRecarga = false;
	    
	    this.conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
	}
	
	/**
	 * Metodo...: Detalhe
	 * Descricao: Construtor usado pela geração de extrato
	 * @param numeroLinha	- Numero da linha
	 * @param numeroOrigem	- Numero que originou a chamada
	 * @param data			- Data da chamada (formato dd/mm/aaaa)
	 * @param hora			- Hora da chamada (formato hh:mm:ss)
	 * @param tipo			- Tipo da chamada
	 * @param operacao		- Operacao da chamada 
	 * @param regiaoOrigem	- Regiao que originou a chamada
	 * @param regiaoDestino	- Regiao de destino da chamada
	 * @param numeroDestino	- Numero de destino da chamada
	 * @param duracao		- Duracao da chamada
	 * @param valor			- Valor da chamada
	 * @param saldo			- Saldo do assinante
	 * @param bonus			- Valor do bonus
	 * @param bonus_saldo	- Saldo de bonus
	 * @param GPRS			- Valor do GPRS
	 * @param GPRS_saldo	- Saldo de GPRS
	 * @param SMS			- Valor do SMS
	 * @param SMS_saldo		- Saldo de SMS
	 * @param periodico		- Valor do periodico
	 * @param periodico_saldo- Saldo de periodico
	 * @param total			- Valor Total
	 * @param total_saldo	- Saldo Total
	 * @return
	 */
	public Detalhe(	
			String numeroLinha, 
			String numeroOrigem,
			String data, 
			String hora,
			String tipo,
			String operacao, 
			String regiaoOrigem, 
			String regiaoDestino,
			String numeroDestino, 
			String duracao, 
			double valor, 
			double saldo,
			double bonus, 
			double bonus_saldo,
			double GPRS, 
			double GPRS_saldo,
			double SMS, 
			double SMS_saldo,
			double periodico,
			double periodico_saldo,
			double total, 
			double total_saldo)
	{
		this.numeroOrigem=numeroOrigem;
		this.data=data;
		this.hora=hora;
		this.tipo=tipo;
		this.operacao=operacao;
		this.regiaoOrigem=regiaoOrigem;
		this.regiaoDestino=regiaoDestino;
		this.numeroDestino=numeroDestino;
		this.duracao=duracao;
		this.desTarifa = null;
		this.tipoTarifa=0;
		this.valor=valor;
		this.saldo=saldo;
		// Multiplo Saldo
		this.numeroLinha=numeroLinha;

		this.bonus			=bonus;
		this.bonus_saldo	=bonus_saldo;
		this.GPRS			=GPRS;
		this.GPRS_saldo		=GPRS_saldo;
		this.SMS			=SMS;
		this.SMS_saldo		=SMS_saldo;
		this.periodico  	=periodico;
		this.periodico_saldo=periodico_saldo;

		this.total		=total;
		this.total_saldo=total_saldo;
	    this.indRecarga = false;
	    
	    this.conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("BR", "pt", "")));
	}
	
	/***
	 * Metodo...: Detalhe
	 * Descricao: Construtor usado pela geração extrato pula-pula
	 * @param 	String		nrOrigem		Número que fez a chamada
	 * @param 	String		data			Data da chamada
	 * @param 	String		hora			Hora da chamada
	 * @param 	Stirng		operacao		Descrição da chamada
	 * @param 	String		regOrigem		Região de origem da chamada
	 * @param 	String		regDestino		Regisão de destino da chamada
	 * @param 	String		nrDestino		Número para o qual se ligou
	 * @param 	String		duracao			Tempo (em segundos) da chamada
	 * @param 	double		bonusRecebido	Valor do bônus pula-pula recebido
	 */
	public Detalhe(	String nrOrigem, String data, String hora, String operacao,
					String regOrigem, String regDestino, String nrDestino, String duracao,
					double bonusRecebido)
	{
		this.numeroOrigem=nrOrigem;
		this.data=data;
		this.hora=hora;
		this.operacao=operacao;
		this.regiaoOrigem=regOrigem;
		this.regiaoDestino=regDestino;
		this.numeroDestino=nrDestino;
		this.duracao=duracao;
		this.desTarifa = null;
		this.tipoTarifa=0;
		this.valor=bonusRecebido;
	    this.indRecarga = false;
	    
	    this.conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("BR", "pt", "")));
	}

	/**
	 * @return Returns the bonus
	 */
	public double getBonus() {
		return bonus;
	}
	/**
	 * @param bonus The bonus to set.
	 */
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	/**
	 * @return Returns the total.
	 */
	public double getTotal() {
		return total;
	}
	/**
	 * @param total The total to set.
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	/**
	 * @return Returns the total_saldo.
	 */
	public double getTotal_saldo() {
		return total_saldo;
	}
	/**
	 * @param total_saldo The total_saldo to set.
	 */
	public void setTotal_saldo(double total_saldo) {
		this.total_saldo = total_saldo;
	}
	// Metodos Get
	/**
	 * Metodo...: getSaldo
	 * Descricao: Retorna o saldo do assinante
	 * @param
	 * @return double	- Saldo do assinante
	 */
	public double getSaldo() {
		return saldo;
	}

	/**
	 * Metodo...: getValor
	 * Descricao: Retorna o valor da chamada
	 * @param
	 * @return double	- Valor da chamada
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * Metodo...: getNumeroOrigem
	 * Descricao: Retorna o numero que originou a chamada
	 * @param
	 * @return String	- Numero que originou a chamada
	 */
	public String getNumeroOrigem() {
		return numeroOrigem;
	}

	/**
	 * Metodo...: getData
	 * Descricao: Retorna a data da chamada
	 * @param
	 * @return String	- Data da chamada (formato dd/mm/aaaa)
	 */
	public String getData() {
		return data;
	}

	/**
	 * Metodo...: getHora
	 * Descricao: Retorna a hora da chamada
	 * @param
	 * @return String	- Hora da chamada (formato hh:mm:ss)
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Metodo...: getDuracao
	 * Descricao: Retorna a duracao da chamada
	 * @param
	 * @return String	- Duracao da chamada
	 */
	public String getDuracao() {
		return duracao;
	}

	/**
	 *	Retorna um valor indicando o tipo de tarifação da chamada.
	 *
	 *	@return		int					tipoTarifa					Indica o tipo de tarifação da chamada.
	 */
	public int getTipoTarifa()
	{
	    return this.tipoTarifa;
	}
	
	/**
	 *	Retorna a flag indicando que o registro refere-se a uma recarga.
	 *
	 *	@return		boolean					indRecarga					Flag de indicacao de recarga.
	 */
	public boolean getIndRecarga()
	{
	    return this.indRecarga;
	}
	
	/**
	 *	Indica se a o registro refere-se a uma recarga.
	 *
	 *	@return		boolean					indRecarga					Flag de indicacao de recarga.
	 */
	public boolean ehRecarga()
	{
	    return this.indRecarga;
	}
	
	/**
	 * Metodo...: getNumeroOrigem
	 * Descricao: Retorna o numero de destino da chamada
	 * @param
	 * @return String	- Numero de destino da chamada
	 */
	public String getNumeroDestino() {
		return numeroDestino;
	}

	/**
	 * Metodo...: getNumeroLinha
	 * Descricao: Retorna o numero da linha da chamada
	 * @param
	 * @return String	- Numero da linha da chamada
	 */
	public String getNumeroLinha() {
		return numeroLinha;
	}

	/**
	 * Metodo...: getOperacao
	 * Descricao: Retorna a operacao da chamada
	 * @param
	 * @return String	- Operacao da chamada
	 */
	public String getOperacao() {
		return operacao;
	}

	/**
	 * Metodo...: getRegiaoOrigem
	 * Descricao: Retorna a regiao que originou a chamada
	 * @param
	 * @return String	- Regiao que originou a chamada
	 */
	public String getRegiaoOrigem() {
		return regiaoOrigem;
	}

	/**
	 * Metodo...: getRegiaoDestino
	 * Descricao: Retorna a regiao de destino da chamada
	 * @param
	 * @return String	- Regiao de destino da chamada
	 */
	public String getRegiaoDestino() {
		return regiaoDestino;
	}

	/**
	 * Metodo...: getTipo
	 * Descricao: Retorna o tipo da chamada
	 * @param
	 * @return String	- Tipo da chamada
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Metodo...: getDesTarifa
	 * Descricao: Retorna a descrição da tarifa
	 * @param
	 * @return String	- Descrição da tarifa
	 */
	public String getDesTarifa() {
		return desTarifa;
	}	
	

	// Metodos Set
	/**
	 * Metodo...: setData
	 * Descricao: Atribui a data da chamada ao detalhe
	 * @param  string	- Data da chamada (formato dd/mm/aaaa)
	 * @return
	 */
	public void setData(String string) {
		data = string;
	}

	/**
	 * Metodo...: setDuracao
	 * Descricao: Atribui a duracao da chamada ao detalhe
	 * @param  string	- Duracao da chamada
	 * @return
	 */
	public void setDuracao(String string) {
		duracao = string;
	}

	/**
	 *	Atribui um valor indicando o tipo de tarifação da chamada.
	 *
	 *	@param		int					tipoTarifa					Valor indicando o tipo de tarifacao
	 */
	public void setTipoTarifa(int tipoTarifa)
	{
	    this.tipoTarifa = tipoTarifa;
	}

	/**
	 *	Atribui uma descriçaõ do tipo de tarifação da chamada.
	 *
	 *	@param		String				desTarifa					Descrição do tipo de tarifa
	 */
	public void setDesTarifa(String desTarifa)
	{
	    this.desTarifa = desTarifa;
	}
	
	/**
	 *	Atribui a flag indicando que o registro refere-se a uma recarga.
	 *
	 *	@param		boolean					indRecarga					Flag de indicacao de recarga.
	 */
	public void setIndRecarga(boolean indRecarga)
	{
	    this.indRecarga = indRecarga;
	}
	
	/**
	 * Metodo...: setHora
	 * Descricao: Atribui a hora da chamada ao detalhe
	 * @param  string	- Hora da chamada (formato hh:mm:ss)
	 * @return
	 */
	public void setHora(String string) {
		hora = string;
	}

	/**
	 * Metodo...: setNumeroDestino
	 * Descricao: Atribui o numero de destino da chamada ao detalhe
	 * @param  string	- Numero de destino da chamada
	 * @return
	 */
	public void setNumeroDestino(String string) {
		numeroDestino = string;
	}

	/**
	 * Metodo...: setNumeroOrigem
	 * Descricao: Atribui o numero que originou a chamada ao detalhe
	 * @param  string	- Numero que originou a chamada
	 * @return
	 */
	public void setNumeroOrigem(String string) {
		numeroOrigem = string;
	}

	/**
	 * Metodo...: setOperacao
	 * Descricao: Atribui a operacao da chamada ao detalhe
	 * @param  string	- Operacao da chamada
	 * @return
	 */
	public void setOperacao(String string) {
		operacao = string;
	}

	/**
	 * Metodo...: setRegiaoOrigem
	 * Descricao: Atribui a regiao que originou a chamada ao detalhe
	 * @param  string	- Regiao que originou a chamada
	 * @return
	 */
	public void setRegiaoOrigem(String string) {
		regiaoOrigem = string;
	}

	/**
	 * Metodo...: setRegiaoDestino
	 * Descricao: Atribui a regiao de destino da chamada ao detalhe
	 * @param  string	- Regiao de destino da chamada
	 * @return
	 */
	public void setRegiaoDestino(String string) {
		regiaoDestino = string;
	}

	/**
	 * Metodo...: setTipo
	 * Descricao: Atribui o tipo da chamada ao detalhe
	 * @param  string	- Tipo da chamada
	 * @return
	 */
	public void setTipo(String string) {
		tipo = string;
	}
	
	/**
	 * Metodo...: setSaldo
	 * Descricao: Atribui o saldo do assinante ao detalhe
	 * @param   x	- Saldo do assinante
	 * @return
	 */
	public void setSaldo(double x) {
		saldo = x;
	}

	/**
	 * Metodo...: setValor
	 * Descricao: Atribui o valor da chamada ao detalhe
	 * @param   x	- Valor da chamada
	 * @return
	 */
	public void setValor(double x) {
		valor = x;
	}

	/**
	 * Metodo...: setNumeroLinha
	 * Descricao: Atribui o numero da linha da chamada ao detalhe
	 * @param   i	- Numero da linha da chamada
	 * @return
	 */
	public void setNumeroLinha(String i) {
		numeroLinha = i;
	}


	/**
	 * @return Returns the bonus_saldo.
	 */
	public double getBonus_saldo() {
		return bonus_saldo;
	}
	/**
	 * @param bonus_saldo The bonus_saldo to set.
	 */
	public void setBonus_saldo(double bonus_saldo) {
		this.bonus_saldo = bonus_saldo;
	}
	/**
	 * @return Returns the gPRS.
	 */
	public double getGPRS() {
		return GPRS;
	}
	/**
	 * @param gprs The gPRS to set.
	 */
	public void setGPRS(double gprs) {
		GPRS = gprs;
	}
	/**
	 * @return Returns the gPRS_saldo.
	 */
	public double getGPRS_saldo() {
		return GPRS_saldo;
	}
	/**
	 * @param gprs_saldo The gPRS_saldo to set.
	 */
	public void setGPRS_saldo(double gprs_saldo) {
		GPRS_saldo = gprs_saldo;
	}
	/**
	 * @return Returns the sMS.
	 */
	public double getSMS() {
		return SMS;
	}
	/**
	 * @param sms The sMS to set.
	 */
	public void setSMS(double sms) {
		SMS = sms;
	}
	/**
	 * @return Returns the sMS_saldo.
	 */
	public double getSMS_saldo() {
		return SMS_saldo;
	}
	/**
	 * @param sms_saldo The sMS_saldo to set.
	 */
	public void setSMS_saldo(double sms_saldo) {
		SMS_saldo = sms_saldo;
	}
	
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor no formato String.
	 */
	public String format(int campo)
	{
	    switch(campo)
	    {
	    	case Detalhe.FORMAT_DURACAO:
	    	{
	    	    if(this.duracao != null)
		    	    return (!this.duracao.equals("-")) ? GPPData.segundosParaHoras(Long.parseLong(this.duracao)) : "-";
	    	    
	    	    return null;
	    	}
	    	
	    	case Detalhe.FORMAT_VALOR:
	    	    return this.conversorDouble.format(this.valor);

	    	case Detalhe.FORMAT_SALDO:
	    	    return this.conversorDouble.format(this.saldo);

	    	case Detalhe.FORMAT_BONUS:
	    	    return this.conversorDouble.format(this.bonus);

	    	case Detalhe.FORMAT_BONUS_SALDO:
	    	    return this.conversorDouble.format(this.bonus_saldo);

	    	case Detalhe.FORMAT_SMS:
	    	    return this.conversorDouble.format(this.SMS);

	    	case Detalhe.FORMAT_SMS_SALDO:
	    	    return this.conversorDouble.format(this.SMS_saldo);

	    	case Detalhe.FORMAT_GPRS:
	    	    return this.conversorDouble.format(this.GPRS);

	    	case Detalhe.FORMAT_GPRS_SALDO:
	    	    return this.conversorDouble.format(this.GPRS_saldo);

	    	case Detalhe.FORMAT_TOTAL:
	    	    return this.conversorDouble.format(this.total);

	    	case Detalhe.FORMAT_TOTAL_SALDO:
	    	    return this.conversorDouble.format(this.total_saldo);

	    	default: return null;
	    }
	}
	
	/**
	 *	Extrai as informacoes do objeto Recarga e retorna um objeto Detalhe.
	 *	
	 *	@param		recarga					Informacoes de recarga, ajuste ou bonus.
	 *	@return		Objeto detalhe contendo as informacoes da recarga.
	 */
	public static Detalhe extract(Recarga recarga)
	{
        Detalhe result = null;
        
        if(recarga != null)
        {
            result = new Detalhe();
            SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
            SimpleDateFormat conversorTime = new SimpleDateFormat(Definicoes.MASCARA_TIME);
            
            result.setIndRecarga(true);
            result.setNumeroOrigem("-");
            result.setData((recarga.getDatRecarga() != null) ? conversorDate.format(recarga.getDatRecarga()) : "-");
            result.setHora((recarga.getDatRecarga() != null) ? conversorTime.format(recarga.getDatRecarga()) : "-");
        	result.setRegiaoOrigem("-");
        	result.setRegiaoDestino("-");
        	result.setDuracao("-");
        	result.setTipoTarifa(-1);
        	result.setDesTarifa("-");
        	result.setBonus((recarga.getVlrCreditoBonus() != null) ? recarga.getVlrCreditoBonus().doubleValue() : 0.0);

        	try
        	{
                result.setOperacao(MapRecOrigem.getInstancia().getMapDescRecOrigem(recarga.getTipTransacao()));
        	}
        	catch(Exception e)
        	{
        	    result.setOperacao(null);
        	}
        }
        
    	return result;
	}
	
	/**
	 *	Extrai as informacoes do objeto Promocao e retorna um objeto Detalhe.
	 *	
	 *	@param		promocao				Informacoes da promocao.
	 *	@return		Objeto detalhe contendo as informacoes da promocao.
	 */
	public static Detalhe extract(Promocao promocao)
	{
        Detalhe result = new Detalhe();
        
        result.setIndRecarga(true);
        result.setNumeroOrigem("-");
        result.setData("-");
        result.setHora("-");
    	result.setRegiaoOrigem("-");
    	result.setRegiaoDestino("-");
    	result.setDuracao("-");
    	result.setTipoTarifa(-1);
    	result.setDesTarifa("-");
    	result.setBonus(0.0);
    	result.setOperacao((promocao != null) ? promocao.getNomPromocao() : null);

    	return result;
	}
	
	/**
	 *	Extrai as informacoes do objeto PromocaoStatusAssinante e retorna um objeto Detalhe.
	 *	
	 *	@param		status					Informacoes da promocao.
	 *	@return		Objeto detalhe contendo as informacoes do status do assinante.
	 */
	public static Detalhe extract(PromocaoStatusAssinante status)
	{
        Detalhe result = new Detalhe();
        
        result.setIndRecarga(true);
        result.setNumeroOrigem("-");
        result.setData("-");
        result.setHora("-");
    	result.setRegiaoOrigem("-");
    	result.setRegiaoDestino("-");
    	result.setDuracao("-");
    	result.setTipoTarifa(-1);
    	result.setDesTarifa("-");
    	result.setBonus(0.0);
    	result.setOperacao((status != null) ? status.getDesStatus() : null);

    	return result;
	}

	/**
	 * @return the periodico
	 */
	public double getPeriodico() {
		return periodico;
	}

	/**
	 * @param periodico the periodico to set
	 */
	public void setPeriodico(double periodico) {
		this.periodico = periodico;
	}

	/**
	 * @return the periodico_saldo
	 */
	public double getPeriodico_saldo() {
		return periodico_saldo;
	}

	/**
	 * @param periodico_saldo the periodico_saldo to set
	 */
	public void setPeriodico_saldo(double periodico_saldo) {
		this.periodico_saldo = periodico_saldo;
	}
	
}
