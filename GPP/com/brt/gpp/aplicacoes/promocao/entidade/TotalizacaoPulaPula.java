package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;

/**
 *	Classe TotalizacaoPulaPula, que representa a entidade da tabela TBL_PRO_TOTALIZACAO_PULA_PULA. Esta tabela 
 *	contem o tempo total de ligacoes recebidas mensalmente por cada assinante.
 * 
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		13/06/2005
 *	@modify		Primeira versao.
 *
 *	@version	1.1
 *	@author		Bernardo Vergne Dias
 *	@date		25/10/2007
 *	@modify		Adicionado Controle Total.
 *
 *	@version	1.2
 *	@author		Daniel Ferreira
 *	@date		26/03/2008
 *	@modify		Inclusao de metodos para recebimento on e off-net e bonus retirado.
 */
public class TotalizacaoPulaPula implements Totalizado
{
	
	//OBS: Constantes definidas para que os valores de numeros de segundos recebidos correspondam a configuracao da
	//tabela TBL_PRO_DESCONTO_PULA_PULA.

	/**
	 *	Constante referente ao recebimento total de ligacoes com bonus retirado.
	 */
	public static final int NUM_SEGUNDOS_BONUS_RETIRADO = -5;
	
	/**
	 *	Constante referente ao recebimento total de ligacoes off-net.
	 */
	public static final int NUM_SEGUNDOS_OFFNET = -4;
	
	/**
	 *	Constante referente ao recebimento total de ligacoes on-net.
	 */
	public static final int NUM_SEGUNDOS_ONNET = -3;
	
	/**
	 *	Constante referente ao mes de recebimento das chamadas.
	 */
	public static final int DAT_MES = -2;
	
	/**
	 *	Constante referente ao recebimento total em ligacoes.
	 */
	public static final int NUM_SEGUNDOS_TOTAL = -1;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com bonificacao normal.
	 */
	public static final int NUM_SEGUNDOS_NORMAL = DescontoPulaPula.NORMAL;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com tarifacao Amigos Toda Hora (Family & Friends).
	 */
	public static final int NUM_SEGUNDOS_FF = DescontoPulaPula.ATH;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com tarifacao de plano noturno.
	 */
	public static final int NUM_SEGUNDOS_NOTURNO = DescontoPulaPula.NOTURNO;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com tarifacao de plano diurno.
	 */
	public static final int NUM_SEGUNDOS_DIURNO = DescontoPulaPula.DIURNO;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com originador na Black List.
	 */
	public static final int NUM_SEGUNDOS_NAOBONIFICADO = DescontoPulaPula.BLACK_LIST;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com duracao excedida.
	 */
	public static final int NUM_SEGUNDOS_DURACAOEXCEDIDA = DescontoPulaPula.DURACAO_EXCEDIDA;
	
	/**
	 *	Constante referente ao recebimento de ligacoes expurgadas.
	 */
	public static final int	NUM_SEGUNDOS_EXPURGO_FRAUDE	= DescontoPulaPula.EXPURGO;
	
	/**
	 *	Constante referente ao recebimento de ligacoes estornadas.
	 */
	public static final int	NUM_SEGUNDOS_ESTORNO_FRAUDE	= DescontoPulaPula.ESTORNO;
	
	/**
	 *	Constante referente ao recebimento de ligacoes originadas por orelhao e duracao acima de 15 minutos.
	 */
	public static final int NUM_SEGUNDOS_TUP = DescontoPulaPula.TUP;
	
	/**
	 *	Constante referente ao recebimento de ligacoes a origem igual ao destino.
	 */
	public static final int NUM_SEGUNDOS_AIGUALB =  DescontoPulaPula.AIGUALB;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com o numero originador zerado.
	 */
	public static final int NUM_SEGUNDOS_TITULAR_TRANSF = DescontoPulaPula.TITULARIDADE_TRANSFERIDA;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com tarifacao Amigos Toda Hora 2 (Family & Friends).
	 */
	public static final int NUM_SEGUNDOS_AMIGOSTODAHORA = DescontoPulaPula.ATH2;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com origem movel nao BrT.
	 */
	public static final int NUM_SEGUNDOS_MOVELNAOBRT = DescontoPulaPula.MOVEL_OFFNET;
	
	/**
	 *	Constante referente ao recebimento de ligacoes originadas por Fale de Graca em horario promocional.
	 */
	public static final int NUM_SEGUNDOS_FALEGRATIS = DescontoPulaPula.FALE_GRACA;
	
	/**
	 *	Constante referente ao recebimento de ligacoes originadas com utilizacao de bonus.
	 */
	public static final int NUM_SEGUNDOS_BONUS = DescontoPulaPula.BONUS;
	
	/**
	 *	Constante referente ao recebimento de ligacoes com originador Controle Total.
	 */
	public static final int NUM_SEGUNDOS_CT = DescontoPulaPula.CT;
	
	/**
	 *	MSISDN do assinante.
	 */
	private	String idtMsisdn;
	
	/**
	 *	Mes de recebimento das ligacoes.
	 */
	private	String datMes;

	/**
	 *	Recebimento total de ligacoes em segundos.
	 */
	private	Long numSegundosTotal;

	/**
	 *	Recebimento de ligacoes com tarifacao Amigos Toda Hora (Family and Friends) em segundos.
	 */
	private	Long numSegundosFF;

	/**
	 *	Recebimento de ligacoes com tarifacao de plano noturno em segundos.
	 */
	private Long numSegundosNoturno;

	/**
	 *	Recebimento de ligacoes com tarifacao de plano noturno em segundos.
	 */
	private Long numSegundosDiurno;

	/**
	 *	Recebimento de ligacoes com originador na Black List em segundos.
	 */
	private Long numSegundosNaoBonificado;

	/**
	 *	Recebimento de ligacoes duracao excedida em segundos.
	 */
	private Long numSegundosDuracaoExcedida;

	/**
	 *	Recebimento de ligacoes expurgadas em segundos.
	 */
	private Long numSegundosExpurgoFraude;

	/**
	 *	Recebimento de ligacoes estornada em segundos.
	 */
	private Long numSegundosEstornoFraude;

	/**
	 *	Recebimento de ligacoes originadas por TUP e duracao acima de 15 minutos em segundos.
	 */
	private Long numSegundosTup;

	/**
	 *	Recebimento de ligacoes com originador igual ao destino em segundos.
	 */
	private Long numSegundosAIgualB;

	/**
	 *	Recebimento de ligacoes de titular antigo, ou seja, ligacoes recebidas antes de um proesso de
	 *	transferencia de titularidade.
	 */
	private Long numSegundosTitularTransf;

	/**
	 *	Recebimento de ligacoes com tarifacao de Amigos Toda Hora 2 (Family and Friends) em segundos.
	 */
	private	Long numSegundosATH;

	/**
	 *	Recebimento de ligacoes originadas por movel nao BrT em segundos.
	 */
	private Long numSegundosMovelNaoBrt;

	/**
	 *	Recebimento de ligacoes com tarifacao de acesso Fale de Graca a Noite em horario promocional em segundos.
	 */
	private Long numSegundosFaleGratis;

	/**
	 *	Recebimento de ligacoes com utilizacao de bonus em segundos.
	 */
	private Long numSegundosBonus;

	/**
	 *	Recebimento de ligacoes originadas por terminal Controle Total em segundos.
	 */
	private Long numSegundosCT;

	/**
	 *	Conversor de mes.
	 */
	private	SimpleDateFormat conversorDatMes;
	
	/**
	 *	Construtor da classe
	 */
	public TotalizacaoPulaPula()
	{
		this.reset();
		
		this.conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
	}
	
	/**
	 *	Retorna o MSISDN.	
	 *
	 *	@return		MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o mes de concessao em formato yyyymm.
	 * 
	 *	@return		Mes de concessao.
	 */
	public String getDatMes() 
	{
		return this.datMes;
	}
	
	/**
	 *	Retorna o numero total de segundos recebidos pelo assinante.
	 * 
	 *	@return		Numero total de segundos recebidos.
	 */
	public Long getNumSegundosTotal() 
	{
		return this.numSegundosTotal;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos pelo assinante com tarifacao diferenciada Friends and Family.
	 *
	 *	@return		Numero de segundos com tarifacao diferenciada.
	 */
	public Long getNumSegundosFF() 
	{
		return this.numSegundosFF;
	}

	/**
	 *	Retorna o numero de segundos recebidos pelo assinante com tarifacao diferenciada plano noturno.
	 *
	 *	@return		Numero de segundos com tarifacao diferenciada.
	 */
	public Long getNumSegundosNoturno() 
	{
		return this.numSegundosNoturno;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos pelo assinante com tarifacao diferenciada plano diurno.
	 *
	 *	@return		Numero de segundos com tarifacao diferenciada.
	 */
	public Long getNumSegundosDiurno() 
	{
		return this.numSegundosDiurno;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos pelo assinante nao totalizados.
	 *
	 *	@return		Numero de segundos nao totalizados.
	 */
	public Long getNumSegundosNaoBonificado() 
	{
		return this.numSegundosNaoBonificado;
	}

	/**
	 *	Retorna o numero de segundos recebidos pelo assinante nao totalizados por duracao excessiva.
	 *
	 *	@return		Numero de segundos nao totalizados por duracao.
	 */
	public Long getNumSegundosDuracaoExcedida() 
	{
		return this.numSegundosDuracaoExcedida;
	}

	/**
	 *	Retorna o numero de segundos recebidos pelo assinante nao totalizados por fraude.
	 *
	 *	@return		Numero de segundos nao totalizados por fraude.
	 */
	public Long getNumSegundosExpurgoFraude() 
	{
		return this.numSegundosExpurgoFraude;
	}

	/**
	 *	Retorna o numero de segundos recebidos pelo assinante nao totalizados por fraude e estornados do assinante.
	 *
	 *	@return		Numero de segundos nao totalizados por fraude e estornados do assinante.
	 */
	public Long getNumSegundosEstornoFraude() 
	{
		return this.numSegundosEstornoFraude;
	}

	/**
	 *	Retorna o numero de segundos recebidos pelo assinante nao totalizados por ligacoes TUP
	 *
	 *	@return		Numero de segundos nao totalizados por ligacoes TUP
	 */
	public Long getNumSegundosTup() 
	{
		return this.numSegundosTup;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos pelo assinante nao totalizados devido ao numero de origem ser igual ao de destino.
	 *
	 *	@return		Numero de segundos com numero de origem igual ao de destino.
	 */
	public Long getNumSegundosAIgualB() 
	{
		return this.numSegundosAIgualB;
	}
	
	/**
	 *	Retorna o recebimento de ligacoes de titular antigo.
	 *
	 *	@return		Recebimento de ligacoes de titular antigo.
	 */
	public Long getNumSegundosTitularTransf() 
	{
		return this.numSegundosTitularTransf;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos que foram diferenciados pelo novo BrasilVantagens
	 *
	 *	@return		Numero de segundos zerados devido a transferência de titularidade.
	 */
	public Long getNumSegundosATH() 
	{
		return this.numSegundosATH;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de um movel nao brt
	 *
	 *	@return		Numero de segundos por uma ligacao proveniente de um movel nao brt.
	 */
	public Long getNumSegundosMovelNaoBrt() 
	{
		return this.numSegundosMovelNaoBrt;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de um acesso da promocao fale gratis
	 *
	 *	@return		Numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de um acesso da promocao fale gratis
	 */
	public Long getNumSegundosFaleGratis() 
	{
		return this.numSegundosFaleGratis;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente da utilizacao do saldo de bonus
	 *
	 *	@return		Numero de segundos recebidos que foram diferenciados por uma ligacao utilizando o saldo de bonus
	 */
	public Long getNumSegundosBonus() 
	{
		return this.numSegundosBonus;
	}
	
	/**
	 *	Retorna o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de terminal fixo Controle Total
	 *
	 *	@return		Numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de terminal fixo Controle Total
	 */
	public Long getNumSegundosCT() 
	{
		return this.numSegundosCT;
	}
	
	/**
	 *	Retorna o total de ligacoes On-Net recebidas pelo assinante.
	 *
	 *	@return		Total de ligacoes On-Net recebidas pelo assinante.
	 */
	public long getNumSegundosOnNet()
	{
		long result = 0;
		
		result += (this.numSegundosTotal != null) ? this.numSegundosTotal.longValue() : 0;
		result -= this.getNumSegundosOffNet();
		result -= this.getNumSegundosBonusRetirado();
		
		return (result >= 0) ? result : 0;
	}
	
	/**
	 *	Retorna o total de ligacoes Off-Net recebidas pelo assinante.
	 *
	 *	@return		Total de ligacoes Off-Net recebidas pelo assinante.
	 */
	public long getNumSegundosOffNet()
	{
		long result = 0;
		
		result += (this.numSegundosMovelNaoBrt != null) ? this.numSegundosMovelNaoBrt.longValue() : 0;
		
		return result;
	}
	
	/**
	 *	Retorna o total de ligacoes recebidas pelo assinante cujo bonus foi retirado automaticamente. O metodo nao 
	 *	considera as ligacoes estornadas uma vez que o bonus ja foi concedido ao assinante e foi retirado por 
	 *	processo externo (Expurgo/Estorno).
	 *
	 *	@return		Total de ligacoes recebidas pelo assinante cujo bonus foi retirado automaticamente.
	 */
	public long getNumSegundosBonusRetirado()
	{
		long result = 0;
		
        result += (this.numSegundosNaoBonificado	!= null) ? this.numSegundosNaoBonificado.longValue()	: 0;
        result += (this.numSegundosDuracaoExcedida	!= null) ? this.numSegundosDuracaoExcedida.longValue()	: 0;
        result += (this.numSegundosExpurgoFraude	!= null) ? this.numSegundosExpurgoFraude.longValue()	: 0;
        result += (this.numSegundosTup				!= null) ? this.numSegundosTup.longValue()				: 0;
        result += (this.numSegundosAIgualB			!= null) ? this.numSegundosAIgualB.longValue()			: 0;
        result += (this.numSegundosTitularTransf	!= null) ? this.numSegundosTitularTransf.longValue()	: 0;
        result += (this.numSegundosFaleGratis		!= null) ? this.numSegundosFaleGratis.longValue()		: 0;
        result += (this.numSegundosBonus			!= null) ? this.numSegundosBonus.longValue()			: 0;
		
		return result;
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
	 *	Atribui o mes de concessao. Deve estar em formato yyyyMM.
	 * 
	 *	@param		datMes					Mes de concessao.
	 */
	public void setDatMes(String datMes) 
	{
		this.datMes = datMes;
	}
	
	/**
	 *	Atribui o mes de concessao.
	 * 
	 *	@param		datMes					Mes de concessao.
	 */
	public void setDatMes(Date datMes) 
	{
		this.datMes = this.conversorDatMes.format(datMes);
	}
	
	/**
	 *	Atribui o numero total de segundos recebidos pelo assinante.
	 * 
	 *	@param		numSegundosTotal		Numero total de segundos recebidos.
	 */
	public void setNumSegundosTotal(Long numSegundosTotal) 
	{
		this.numSegundosTotal = numSegundosTotal;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos com tarifacao diferenciada.
	 * 
	 *	@param		numSegundosFF			Numero de segundos com tarifacao diferenciada.
	 */
	public void setNumSegundosFF(Long numSegundosFF) 
	{
		this.numSegundosFF = numSegundosFF;
	}

	/**
	 *	Atribui o numero de segundos recebidos com tarifacao diferenciada.
	 * 
	 *	@param		numSegundosNoturno		Numero de segundos com tarifacao diferenciada.
	 */
	public void setNumSegundosNoturno(Long numSegundosNoturno) 
	{
		this.numSegundosNoturno = numSegundosNoturno;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos com tarifacao diferenciada.
	 * 
	 *	@param		numSegundosDiurno		Numero de segundos com tarifacao diferenciada.
	 */
	public void setNumSegundosDiurno(Long numSegundosDiurno) 
	{
		this.numSegundosDiurno = numSegundosDiurno;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos nao totalizados.
	 * 
	 *	@param		numSegundosNaoBonificado Numero de segundos nao totalizados.
	 */
	public void setNumSegundosNaoBonificado(Long numSegundosNaoBonificado) 
	{
		this.numSegundosNaoBonificado = numSegundosNaoBonificado;
	}

	/**
	 *	Atribui o numero de segundos recebidos nao totalizados por duracao excessiva.
	 * 
	 *	@param		numSegundosDuracaoExcedida Numero de segundos nao totalizados por duracao excessiva.
	 */
	public void setNumSegundosDuracaoExcedida(Long numSegundosDuracaoExcedida) 
	{
		this.numSegundosDuracaoExcedida = numSegundosDuracaoExcedida;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos pelo assinante nao totalizados por fraude.
	 *
	 *	@param		numSegundosExpurgoFraude Numero de segundos nao totalizados por fraude.
	 */
	public void setNumSegundosExpurgoFraude(Long numSegundosExpurgoFraude) 
	{
		this.numSegundosExpurgoFraude = numSegundosExpurgoFraude;
	}

	/**
	 *	Atribui o numero de segundos recebidos pelo assinante nao totalizados por fraude e estornados do assinante.
	 *
	 *	@param		numSegundosEstornoFraude Numero de segundos nao totalizados por fraude e estornados do assinante.
	 */
	public void setNumSegundosEstornoFraude(Long numSegundosEstornoFraude) 
	{
		this.numSegundosEstornoFraude = numSegundosEstornoFraude;
	}

	/**
	 *	Atribui o numero de segundos recebidos pelo assinante nao totalizados por ligacoes TUP
	 *
	 *	@param		numSegundosTup			Numero de segundos nao totalizados por ligacoes TUP
	 */
	public void setNumSegundosTup(Long numSegundosTup) 
	{
		this.numSegundosTup = numSegundosTup;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos pelo assinante nao totalizados devido ao numero de origem ser igual ao de destino.
	 *
	 *	@param		numSegundosAIgualB		Numero de segundos com numero de origem igual ao de destino.
	 */
	public void setNumSegundosAIgualB(Long numSegundosAIgualB) 
	{
		this.numSegundosAIgualB = numSegundosAIgualB;
	}
	
	/**
	 *	Atribui o recebimento de ligacoes de titular antigo.
	 *
	 *	@return		numSegundosTitularTransf Recebimento de ligacoes de titular antigo.
	 */
	public void setNumSegundosTitularTransf(Long numSegundosTitularTransf) 
	{
		this.numSegundosTitularTransf = numSegundosTitularTransf;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos pelo assinante nao totalizados devido ao novo amigos toda hora
	 *
	 *	@param		numSegundosZerados		Numero de segundos zerados devido a transferência de titularidade.
	 */
	public void setNumSegundosATH(Long numSegundosATH) 
	{
		this.numSegundosATH = numSegundosATH;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de um movel nao brt
	 *
	 *	@param		numSegundosMovelNaoBrt	Numero de segundos por uma ligacao proveniente de um movel nao brt
	 */
	public void setNumSegundosMovelNaoBrt(Long numSegundosMovelNaoBrt) 
	{
		this.numSegundosMovelNaoBrt = numSegundosMovelNaoBrt;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de um acesso da promocao fale gratis
	 *
	 *	@param		numSegundosMovelFaleGratis Numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de um acesso da promocao fale gratis
	 */
	public void setNumSegundosFaleGratis(Long numSegundosFaleGratis) 
	{
		this.numSegundosFaleGratis = numSegundosFaleGratis;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de uma ligacao utilizando o saldo de bonus
	 *
	 *	@param		numSegundosBonus		Numero de segundos recebidos que foram diferenciados por uma ligacao proveniente utilizando o saldo de bonus
	 */
	public void setNumSegundosBonus(Long numSegundosBonus) 
	{
		this.numSegundosBonus = numSegundosBonus;
	}
	
	/**
	 *	Atribui o numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de terminal fixo Controle Total
	 *
	 *	@param		numSegundosCT			Numero de segundos recebidos que foram diferenciados por uma ligacao proveniente de terminal fixo Controle Total
	 */
	public void setNumSegundosCT(Long numSegundosCT) 
	{
		this.numSegundosCT = numSegundosCT;
	}
	
	/** 
	 *	Converte o valor para String, sem formatacao. Se o valor for NULL, retorna o valor inicializado.
	 *
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor em String, sem formatacao.
	 */
	public String toString(int campo)
	{
	    switch(campo)
	    {
    		case TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS_RETIRADO:
    			return String.valueOf(this.getNumSegundosBonusRetirado());
    		case TotalizacaoPulaPula.NUM_SEGUNDOS_OFFNET:
    			return String.valueOf(this.getNumSegundosOffNet());
    		case TotalizacaoPulaPula.NUM_SEGUNDOS_ONNET:
    			return String.valueOf(this.getNumSegundosOnNet());
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TOTAL:
	    	    return (this.numSegundosTotal != null) ? 
	    	    			this.numSegundosTotal.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_FF:
	    	    return (this.numSegundosFF != null) ? 
	    	    			this.numSegundosFF.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_NOTURNO:
	    	    return (this.numSegundosNoturno != null) ? 
	    	    			this.numSegundosNoturno.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_DIURNO:
	    	    return (this.numSegundosDiurno != null) ? 
	    	    			this.numSegundosDiurno.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_NAOBONIFICADO:
	    	    return (this.numSegundosNaoBonificado != null) ? 
	    	    			this.numSegundosNaoBonificado.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_DURACAOEXCEDIDA:
	    	    return (this.numSegundosDuracaoExcedida != null) ? 
	    	    			this.numSegundosDuracaoExcedida.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE:
	    	    return (this.numSegundosExpurgoFraude != null) ? 
	    	    			this.numSegundosExpurgoFraude.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE:
	    	    return (this.numSegundosEstornoFraude != null) ? 
	    	    			this.numSegundosEstornoFraude.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TUP:
	    	    return (this.numSegundosTup != null) ? 
	    	    			this.numSegundosTup.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_AIGUALB:
	    	    return (this.numSegundosAIgualB != null) ? 
	    	    			this.numSegundosAIgualB.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TITULAR_TRANSF:
	    	    return (this.numSegundosTitularTransf != null) ? 
	    	    			this.numSegundosTitularTransf.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_AMIGOSTODAHORA:
	    	    return (this.numSegundosATH != null) ? 
	    	    			this.numSegundosATH.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_MOVELNAOBRT:
	    	    return (this.numSegundosMovelNaoBrt != null) ? 
	    	    			this.numSegundosMovelNaoBrt.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_FALEGRATIS:
	    	    return (this.numSegundosFaleGratis != null) ? 
	    	    			this.numSegundosFaleGratis.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS:
	    	    return (this.numSegundosBonus != null) ? 
	    	    			this.numSegundosBonus.toString() : new Long(0).toString();
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_CT:
	    	    return (this.numSegundosCT != null) ? 
	    	    			this.numSegundosCT.toString() : new Long(0).toString();
	    	default: return null;
	    }
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
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS_RETIRADO:
	    			return GPPData.segundosParaHoras(this.getNumSegundosBonusRetirado());
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_OFFNET:
    			return GPPData.segundosParaHoras(this.getNumSegundosOffNet());
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_ONNET:
    			return GPPData.segundosParaHoras(this.getNumSegundosOnNet());
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TOTAL:
	    	    if(this.numSegundosTotal != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosTotal.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_FF:
	    	    if(this.numSegundosFF != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosFF.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_NOTURNO:
	    	    if(this.numSegundosFF != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosNoturno.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_DIURNO:
	    	    if(this.numSegundosFF != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosDiurno.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_NAOBONIFICADO:
	    	    if(this.numSegundosFF != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosNaoBonificado.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_DURACAOEXCEDIDA:
	    	    if(this.numSegundosFF != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosDuracaoExcedida.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE:
	    	    if(this.numSegundosExpurgoFraude != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosExpurgoFraude.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE:
	    	    if(this.numSegundosEstornoFraude != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosEstornoFraude.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TUP:
	    	    if(this.numSegundosTup != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosTup.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_AIGUALB:
	    	    if(this.numSegundosAIgualB != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosAIgualB.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TITULAR_TRANSF:
	    	    if(this.numSegundosTitularTransf != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosTitularTransf.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_AMIGOSTODAHORA:
	    	    if(this.numSegundosATH != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosATH.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_MOVELNAOBRT:
	    	    if(this.numSegundosMovelNaoBrt != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosMovelNaoBrt.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_FALEGRATIS:
	    	    if(this.numSegundosFaleGratis != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosFaleGratis.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS:
	    	    if(this.numSegundosBonus != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosBonus.longValue());
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_CT:
	    	    if(this.numSegundosCT != null)
	    	        return GPPData.segundosParaHoras(this.numSegundosCT.longValue());
	    	    break;
	    	default: return null;
	    }
	    
	    return null;
    }
	
	/**
	 *	Adiciona o campo com o numero de segundos. No caso da adicao do numero de segundos com tarifacao diferenciada 
	 *	Friends and Family, o metodo NAO adiciona o numero total de segundos.
	 *
	 *	@param		campo					Campo selecionado.
	 *	@param		segundos				Numero de segundos a ser adicionado.
	 */
	public void add(int campo, long segundos)
	{
	    switch(campo)
	    {
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TOTAL:
	    	    if(this.numSegundosTotal != null)
	    	        this.numSegundosTotal = new Long(this.numSegundosTotal.longValue() + segundos);
	    	    else
	    	        this.numSegundosTotal = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_FF:
	    	    if(this.numSegundosFF != null)
	    	        this.numSegundosFF = new Long(this.numSegundosFF.longValue() + segundos);
	    	    else
	    	        this.numSegundosFF = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_NOTURNO:
	    	    if(this.numSegundosNoturno != null)
	    	        this.numSegundosNoturno = new Long(this.numSegundosNoturno.longValue() + segundos);
	    	    else
	    	        this.numSegundosNoturno = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_DIURNO:
	    	    if(this.numSegundosDiurno != null)
	    	        this.numSegundosDiurno = new Long(this.numSegundosDiurno.longValue() + segundos);
	    	    else
	    	        this.numSegundosDiurno = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_NAOBONIFICADO:
	    	    if(this.numSegundosNaoBonificado != null)
	    	        this.numSegundosNaoBonificado = new Long(this.numSegundosNaoBonificado.longValue() + segundos);
	    	    else
	    	        this.numSegundosNaoBonificado = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_DURACAOEXCEDIDA:
	    	    if(this.numSegundosDuracaoExcedida != null)
	    	        this.numSegundosDuracaoExcedida = new Long(this.numSegundosDuracaoExcedida.longValue() + segundos);
	    	    else
	    	        this.numSegundosDuracaoExcedida = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE:
	    	    if(this.numSegundosExpurgoFraude != null)
	    	        this.numSegundosExpurgoFraude = new Long(this.numSegundosExpurgoFraude.longValue() + segundos);
	    	    else
	    	        this.numSegundosExpurgoFraude = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE:
	    	    if(this.numSegundosEstornoFraude != null)
	    	        this.numSegundosEstornoFraude = new Long(this.numSegundosEstornoFraude.longValue() + segundos);
	    	    else
	    	        this.numSegundosEstornoFraude = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TUP:
	    	    if(this.numSegundosTup != null)
	    	        this.numSegundosTup = new Long(this.numSegundosTup.longValue() + segundos);
	    	    else
	    	        this.numSegundosTup = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_AIGUALB:
	    	    if(this.numSegundosAIgualB != null)
	    	        this.numSegundosAIgualB = new Long(this.numSegundosAIgualB.longValue() + segundos);
	    	    else
	    	        this.numSegundosAIgualB = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_TITULAR_TRANSF:
	    	    if(this.numSegundosTitularTransf != null)
	    	        this.numSegundosTitularTransf = new Long(this.numSegundosTitularTransf.longValue() + segundos);
	    	    else
	    	        this.numSegundosTitularTransf = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_AMIGOSTODAHORA:
	    	    if(this.numSegundosATH != null)
	    	        this.numSegundosATH = new Long(this.numSegundosATH.longValue() + segundos);
	    	    else
	    	        this.numSegundosATH = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_MOVELNAOBRT:
	    	    if(this.numSegundosMovelNaoBrt != null)
	    	        this.numSegundosMovelNaoBrt = new Long(this.numSegundosMovelNaoBrt.longValue() + segundos);
	    	    else
	    	        this.numSegundosMovelNaoBrt = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_FALEGRATIS:
	    	    if(this.numSegundosFaleGratis != null)
	    	        this.numSegundosFaleGratis = new Long(this.numSegundosFaleGratis.longValue() + segundos);
	    	    else
	    	        this.numSegundosFaleGratis = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS:
	    	    if(this.numSegundosBonus != null)
	    	        this.numSegundosBonus = new Long(this.numSegundosBonus.longValue() + segundos);
	    	    else
	    	        this.numSegundosBonus = new Long(segundos);
	    	    break;
	    	case TotalizacaoPulaPula.NUM_SEGUNDOS_CT:
	    	    if(this.numSegundosCT != null)
	    	        this.numSegundosCT = new Long(this.numSegundosCT.longValue() + segundos);
	    	    else
	    	        this.numSegundosCT = new Long(segundos);
	    	    break;
	    	default: break;
	    }
	}
	
	/**
	 *	Reseta o objeto.
	 */
	public void reset()
	{
	    this.idtMsisdn					= null;
	    this.datMes						= null;
	    this.numSegundosTotal			= new Long(0);
	    this.numSegundosFF				= new Long(0);
	    this.numSegundosDiurno			= new Long(0);
	    this.numSegundosNoturno			= new Long(0);
	    this.numSegundosNaoBonificado	= new Long(0);
	    this.numSegundosDuracaoExcedida	= new Long(0);
	    this.numSegundosExpurgoFraude	= new Long(0);
	    this.numSegundosEstornoFraude	= new Long(0);
	    this.numSegundosTup				= new Long(0);
	    this.numSegundosAIgualB			= new Long(0);
	    this.numSegundosTitularTransf	= new Long(0);
	    this.numSegundosATH				= new Long(0);
	    this.numSegundosMovelNaoBrt		= new Long(0);
	    this.numSegundosFaleGratis		= new Long(0);
	    this.numSegundosBonus			= new Long(0);
	    this.numSegundosCT				= new Long(0);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado#getNomeTotalizado()
	 */
	public boolean possuiMesmoPeriodo(ArquivoCDR arqCDR)
	{
		return this.getDatMes().equals(this.conversorDatMes.format(arqCDR.getTimestamp()));
	}
	
	/**
	 *	@see		java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if(object == null)
			return false;
		
		if(!(object instanceof TotalizacaoPulaPula))
			return false;
		
		if(this.hashCode() != ((TotalizacaoPulaPula)object).hashCode())
			return false;
		
		return true;
	}
	
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idtMsisdn);
		result.append("||");
		result.append(this.datMes);
		
		return result.toString().hashCode();
	}
	
	/** 
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		result.append("MSISDN: ");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		result.append(" - ");
		result.append("Mes de Concessao: ");
		result.append((this.datMes != null) ? this.datMes : "NULL");
		result.append(" - ");
		result.append("Ligacoes recebidas: ");
		result.append((this.numSegundosTotal != null) ? this.format(TotalizacaoPulaPula.NUM_SEGUNDOS_TOTAL) : "NULL");
		
		return result.toString();
	}
	
}
