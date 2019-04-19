package br.com.brasiltelecom.ppp.model;

//Imports Java.

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//Imports WPP.

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 *	Classe que representa as informacoes sumarizadas de um registro de estorno de bonus Pula-Pula por fraude.
 *
 *	@author		Daniel Ferreira
 *	@since		06/03/2006
 */
public class EstornoPulaPula 
{
    
    private					String				dataReferencia;
    private					String				msisdn;
    private					int					promocao;
	private					String				numeroOrigem;
	private					String				origem;
	private					Date				dataProcessamento;
	private					double				valorExpurgo;
	private					double				valorExpurgoSaturado;
	private					double				valorEstorno;
	private					double				valorEstornoEfetivo;
	private static	final	DecimalFormat		conversorDouble		= new DecimalFormat(Constantes.DOUBLE_FORMATO);
	private	static	final	SimpleDateFormat	conversorTimestamp	= new SimpleDateFormat(Constantes.DATA_HORA_FORMATO);
	private	static	final	PhoneNumberFormat	conversorPhone		= new PhoneNumberFormat();
	
	//Constantes internas.
	
	public	static	final	int	MSISDN					= 0;
	public	static	final	int	NUMERO_ORIGEM			= 1;
	public	static	final	int	DATA_PROCESSAMENTO		= 2;
	public	static	final	int	VALOR_EXPURGO			= 3;
	public	static	final	int	VALOR_EXPURGO_SATURADO	= 4;
	public	static	final	int	VALOR_ESTORNO			= 5;
	public	static	final	int	VALOR_ESTORNO_EFETIVO	= 6;
	public	static	final	int	VALOR_EXPURGOS			= 7;

	//Construtores.

	/**
	 *	Construtor da classe.
	 */
	public EstornoPulaPula() 
	{
	    this.dataReferencia		= null;
	    this.msisdn 			= null;
	    this.promocao 			= -1;
	    this.numeroOrigem		= null;
	    this.origem				= null;
	    this.dataProcessamento	= null;
	    this.valorExpurgo		= 0.0;
	    this.valorEstorno		= 0.0;
	}

	//Getters.
	
	/**
	 *	Retorna a data de referencia das ligacoes fraudulentas.
	 *
	 *	@return		String					dataReferencia				Data de referencia das ligacoes fraudulentas.
	 */
	public String getDataReferencia()
	{
		return this.dataReferencia; 
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		String					msisdn						MSISDN do assinante.
	 */
	public String getMsisdn() 
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna o identificador da promocao do assinante.
	 *
	 *	@return		int						promocao					Identificador da promocao do assinante.
	 */
	public int getPromocao()
	{
		return this.promocao; 
	}
	
	/**
	 *	Retorna o numero de origem.
	 *
	 *	@return		String					numeroOrigem				Numero de origem.
	 */
	public String getNumeroOrigem()
	{
		return this.numeroOrigem; 
	}
	
	/**
	 *	Retorna o tipo de estorno. O tipo de estorno define a natureza e a origem das ligacoes. Esta relacionada a
	 *	equipe de investigacao que gera a lista para processamento do estorno.
	 *
	 *	@return		String					origem						Origem do estorno.
	 */
	public String getOrigem() 
	{
		return this.origem;
	}

	/**
	 *	Retorna a data de processamento do estorno.
	 *
	 *	@return		Date					dataProcessamento			Data de processamento do estorno.
	 */
	public Date getDataProcessamento() 
	{
		return this.dataProcessamento;
	}
	
	/**
	 *	Retorna o valor de expurgo.
	 *
	 *	@return		double					valorExpurgo				Valor de expurgo.
	 */
	public double getValorExpurgo() 
	{
		return this.valorExpurgo;
	}
	
	/**
	 *	Retorna o valor de expurgo com limite da promocao do assinante ultrapassado.
	 *
	 *	@return		double					valorExpurgoSaturado		Valor de expurgo com limite ultrapassado.
	 */
	public double getValorExpurgoSaturado() 
	{
		return this.valorExpurgoSaturado;
	}
	
	/**
	 *	Retorna o valor de estorno.
	 *
	 *	@return		double					valorEstorno				Valor de estorno.
	 */
	public double getValorEstorno()
	{
		return this.valorEstorno; 
	}

	/**
	 *	Retorna o valor efetivamente estornado.
	 *
	 *	@return		double					valorEstornoEfetivo			Valor efetivamente estornado.
	 */
	public double getValorEstornoEfetivo()
	{
		return this.valorEstornoEfetivo; 
	}

	//Setters.

	/**
	 *	Atribui a data de referencia das ligacoes fraudulentas.
	 *
	 *	@param		String					dataReferencia				Data de referencia das ligacoes fraudulentas.
	 */
	public void setDataReferencia(String dataReferencia)
	{
		this.dataReferencia = dataReferencia; 
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		String					msisdn						MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui o identificador da promocao do assinante.
	 *
	 *	@param		int						promocao					Identificador da promocao do assinante.
	 */
	public void setPromocao(int promocao)
	{
		this.promocao = promocao; 
	}
	
	/**
	 *	Atribui o numero de origem.
	 *
	 *	@param		String					numeroOrigem				Numero de origem.
	 */
	public void setNumeroOrigem(String numeroOrigem)
	{
		this.numeroOrigem = numeroOrigem; 
	}
	
	/**
	 *	Atribui o tipo de estorno. O tipo de estorno define a natureza e a origem das ligacoes. Esta relacionada a
	 *	equipe de investigacao que gera a lista para processamento do estorno.
	 *
	 *	@param		String					origem						Origem do estorno.
	 */
	public void setOrigem(String origem) 
	{
		this.origem = origem;
	}

	/**
	 *	Atribui a data de processamento do estorno.
	 *
	 *	@param		Date					dataProcessamento			Data de processamento do estorno.
	 */
	public void setDataProcessamento(Date dataProcessamento) 
	{
		this.dataProcessamento = dataProcessamento;
	}
	
	/**
	 *	Atribui o valor de expurgo.
	 *
	 *	@param		double					valorExpurgo				Valor de expurgo.
	 */
	public void setValorExpurgo(double valorExpurgo) 
	{
		this.valorExpurgo = valorExpurgo;
	}
	
	/**
	 *	Atribui o valor de expurgo com limite da promocao do assinante ultrapassado.
	 *
	 *	@param		double					valorExpurgoSaturado		Valor de expurgo com limite ultrapassado.
	 */
	public void setValorExpurgoSaturado(double valorExpurgoSaturado) 
	{
		this.valorExpurgoSaturado = valorExpurgoSaturado;
	}
	
	/**
	 *	Atribui o valor de estorno.
	 *
	 *	@param		double					valorEstorno				Valor de estorno.
	 */
	public void setValorEstorno(double valorEstorno)
	{
		this.valorEstorno = valorEstorno; 
	}

	/**
	 *	Atribui o valor efetivamente estornado.
	 *
	 *	@param		double					valorEstornoEfetivo			Valor efetivamente estornado.
	 */
	public void setValorEstornoEfetivo(double valorEstornoEfetivo)
	{
		this.valorEstornoEfetivo = valorEstornoEfetivo; 
	}

	//Outros metodos.
	
	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		int						campo						Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		String												Valor no formato String.
	 */
	public String format(int campo)
	{
	    switch(campo)
	    {
	    	case EstornoPulaPula.MSISDN:
	    	{
	    	    return (this.msisdn != null) ? EstornoPulaPula.conversorPhone.format(this.msisdn) : null; 
	    	}
	    	case EstornoPulaPula.NUMERO_ORIGEM:
	    	{
	    	    return (this.numeroOrigem != null) ? EstornoPulaPula.conversorPhone.format(this.numeroOrigem) : null; 
	    	}
	    	case EstornoPulaPula.DATA_PROCESSAMENTO:
	    	{
	    	    return (this.dataProcessamento != null) ? EstornoPulaPula.conversorTimestamp.format(this.dataProcessamento) : null; 
	    	}
	    	case EstornoPulaPula.VALOR_EXPURGO:
	    	{
	    	    return EstornoPulaPula.conversorDouble.format(this.valorExpurgo); 
	    	}
	    	case EstornoPulaPula.VALOR_ESTORNO:
	    	{
	    	    return EstornoPulaPula.conversorDouble.format(this.valorEstorno); 
	    	}
	    	case EstornoPulaPula.VALOR_ESTORNO_EFETIVO:
	    	{
	    	    return EstornoPulaPula.conversorDouble.format(this.valorEstornoEfetivo); 
	    	}
	    	case EstornoPulaPula.VALOR_EXPURGOS:
	    	{
	    	    return EstornoPulaPula.conversorDouble.format(this.valorExpurgo + this.valorExpurgoSaturado); 
	    	}
	    	default: return null;
	    }
	}
	
}
