package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoOrigemEstorno;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoOrigemEstorno;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_ESTORNO_PULA_PULA.
 *
 *	@author	Daniel Ferreira
 *	@since	21/12/2005
 */
public class PromocaoEstornoPulaPula
{
		
	/**
	 *	Constante referente a data de ocorrencia das ligacoes indevidas.
	 */
	public static final int DAT_REFERENCIA = 0;
	
	/**
	 *	Constante referente ao identificador do assinante que recebeu as ligacoes indevidas.
	 */
	public static final int IDT_PROMOCAO = 1;
	
	/**
	 *	Constante referente a data de processamento do expurgo/estorno.
	 */
	public static final int DAT_PROCESSAMENTO = 2;

	/**
	 *	Constante referente ao valor total de expurgo.
	 */
	public static final int VLR_EXPURGO = 3;
	
	/**
	 *	Constante referente ao valor total de expurgo com limite da promocao Pula-Pula do assinante ultrapassado. 
	 *	Este valor nao representa recuperacao da perda de receita da Brasil Telecom.
	 */
	public static final int VLR_EXPURGO_SATURADO = 4;
	
	/**
	 *	Constante referente ao valor total de estorno (ajuste de debito) no saldo de bonus do assinante.
	 */
	public static final int VLR_ESTORNO = 5;
	
	/**
	 *	Constante referente ao valor total de estorno efetivo. Corresponde ao valor total que o processo conseguiu 
	 *	debitar do saldo de bonus do assinante.
	 */
	public static final int VLR_ESTORNO_EFETIVO = 6;
	
	/**
	 *	Lista dos nomes dos atributos da classe em formato delimitado.
	 */
	public static final String NOMES_ATRIBUTOS_DELIMITADO = 
		"Lote;Data Referencia;MSISDN;Promocao;Originador;Origem;Data Processamento;Expurgo;Expurgo limite ultrapassado;Estorno;Estorno Efetivo;";
	
	/**
	 *	Identificador do lote de requisicao de expurgo/estorno.
	 */
	private String idtLote;
	
	/**
	 *	Data de referencia de execucao das ligacoes indevidas.
	 */
	private Date datReferencia;
	
	/**
	 *	MSISDN do assinante que recebeu as ligacoes indevidas.
	 */
	private String idtMsisdn;
	
	/**
	 *	Identificador do assinante que recebeu as ligacoes indevidas.
	 */
	private int idtPromocao;
	
	/**
	 *	Numero originador das ligacoes indevidas.
	 */
	private String idtNumeroOrigem;
	
	/**
	 *	Identificador da natureza de origem das ligacoes indevidas.
	 */
	private String idtOrigem;
	
	/**
	 *	Data de processamento do expurgo/estorno.
	 */
	private Date datProcessamento;
	
	/**
	 *	Valor total de expurgo.
	 */
	private double vlrExpurgo;
	
	/**
	 *	Valor total de expurgo com limite da promocao Pula-Pula do assinante ultrapassado. Este valor nao representa 
	 *	recuperacao da perda de receita da Brasil Telecom.
	 */
	private double vlrExpurgoSaturado;
	
	/**
	 *	Valor total de estorno (ajuste de debito) no saldo de bonus do assinante.
	 */
	private double vlrEstorno;
	
	/**
	 *	Valor total de estorno efetivo. Corresponde ao valor total que o processo conseguiu debitar do saldo de bonus 
	 *	do assinante.
	 */
	private double vlrEstornoEfetivo;
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoEstornoPulaPula()
	{
		this.idtLote			= null;
	    this.datReferencia		= null;
		this.idtMsisdn			= null;
		this.idtPromocao		= -1;
		this.idtNumeroOrigem	= null;
		this.idtOrigem			= null;
		this.datProcessamento	= null;
		this.vlrExpurgo			= 0.0;
		this.vlrExpurgoSaturado	= 0.0;
		this.vlrEstorno			= 0.0;
		this.vlrEstornoEfetivo	= 0.0;
	}
	
	/**
	 *	Retorna o identificador do lote de requisicao de expurgo/estorno.
	 * 
	 *	@return		Identificador do lote de requisicao de expurgo/estorno.
	 */
	public String getIdtLote() 
	{
		return this.idtLote;
	}
	
	/**
	 *	Retorna a data de referencia das chamadas.
	 * 
	 *	@return		Data de referencia das chamadas.
	 */
	public Date getDatReferencia() 
	{
		return this.datReferencia;
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
	 *	Retorna o identificador da promocao do assinante na epoca da execucao.
	 *	
	 *	@return		Identificador da promocao do assinante.
	 */
	public int getIdtPromocao() 
	{
		return this.idtPromocao;
	}
	/**
	 *	Retorna o numero de origem da ligacao.
	 * 
	 *	@return		Numero de origem da ligacao.
	 */
	public String getIdtNumeroOrigem() 
	{
		return this.idtNumeroOrigem;
	}
	
	/**
	 *	Retorna o tipo de estorno. O tipo de estorno define a natureza e a origem das ligacoes. Esta relacionada a
	 *	equipe de investigacao que gera a lista para processamento do estorno.
	 * 
	 *	@return		Tipo de estorno.
	 */
	public String getIdtOrigem() 
	{
		return this.idtOrigem;
	}
	
	/**
	 *	Retorna a data de processamento da requisicao do estorno.
	 * 
	 *	@return		Data de processamento.
	 */
	public Date getDatProcessamento() 
	{
		return this.datProcessamento;
	}
	
	/**
	 *	Retorna o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito).
	 * 
	 *	@return		Valor de expurgo.
	 */
	public double getVlrExpurgo() 
	{
		return this.vlrExpurgo;
	}
	
	/**
	 *	Retorna o valor expurgado (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) devido
	 *	ao limite de bonus Pula-Pula ultrapassado para o assinante. Quando o assinante ultrapassa o limite de seu 
	 *	Pula-Pula, as suas ligacoes fraudulentas devem ser expurgadas ate que seja atingido o limite da promocao.
	 *	A partir deste ponto as ligacoes devem ser estornadas, uma vez que nao ha mais ligacoes legitimas que
	 *	"compensem" as ligacoes fraudulentas.
	 * 
	 *	@return		Valor expurgado por limite ultrapassado.
	 */
	public double getVlrExpurgoSaturado() 
	{
		return this.vlrExpurgoSaturado;
	}
	
	/**
	 *	Retorna o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito).
	 * 
	 *	@return		Valor de estorno.
	 */
	public double getVlrEstorno() 
	{
		return this.vlrEstorno;
	}
	
	/**
	 *	Retorna o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito) efetivo.
	 * 
	 *	@return		Valor de estorno efetivo.
	 */
	public double getVlrEstornoEfetivo() 
	{
		return this.vlrEstornoEfetivo;
	}
	
	/**
	 *	Atribui o identificador do lote de requisicao de expurgo/estorno.
	 * 
	 *	@param		idtLote					Identificador do lote de requisicao de expurgo/estorno.
	 */
	public void setIdtLote(String idtLote) 
	{
		this.idtLote = idtLote;
	}
	
	/**
	 *	Atribui a data de referencia das chamadas.
	 * 
	 *	@param		datReferencia			Data de referencia das chamadas.
	 */
	public void setDatReferencia(Date dataReferencia) 
	{
		this.datReferencia = dataReferencia;
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
	 *	Atribui o identificador da promocao do assinante na epoca da execucao.
	 *	
	 *	@param		idtPromocao				Identificador da promocao do assinante.
	 */
	public void setIdtPromocao(int idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
	/**
	 *	Atribui o numero de origem da ligacao.
	 * 
	 *	@param		idtNumeroOrigem			Numero de origem da ligacao.
	 */
	public void setIdtNumeroOrigem(String idtNumeroOrigem) 
	{
		this.idtNumeroOrigem = idtNumeroOrigem;
	}
	
	/**
	 *	Atribui o tipo de estorno. O tipo de estorno define a natureza e a origem das ligacoes. Esta relacionada a
	 *	equipe de investigacao que gera a lista para processamento do estorno.
	 * 
	 *	@param		idtOrigem				Tipo de estorno.
	 */
	public void setIdtOrigem(String idtOrigem) 
	{
		this.idtOrigem = idtOrigem;
	}
	
	/**
	 *	Atribui a data de processamento da requisicao do estorno.
	 * 
	 *	@param		datProcessamento		Data de processamento.
	 */
	public void setDatProcessamento(Date datProcessamento) 
	{
		this.datProcessamento = datProcessamento;
	}
	
	/**
	 *	Atribui o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito).
	 * 
	 *	@param		vlrExpurgo				Valor de expurgo.
	 */
	public void setVlrExpurgo(double vlrExpurgo) 
	{
		this.vlrExpurgo = vlrExpurgo;
	}
	
	/**
	 *	Atribui o valor expurgado (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) devido
	 *	ao limite de bonus Pula-Pula ultrapassado para o assinante. Quando o assinante ultrapassa o limite de seu 
	 *	Pula-Pula, as suas ligacoes fraudulentas devem ser expurgadas ate que seja atingido o limite da promocao.
	 *	A partir deste ponto as ligacoes devem ser estornadas, uma vez que nao ha mais ligacoes legitimas que
	 *	"compensem" as ligacoes fraudulentas.
	 * 
	 *	@param		vlrExpurgoSaturado		Valor expurgado por limite ultrapassado.
	 */
	public void setVlrExpurgoSaturado(double vlrExpurgoSaturado) 
	{
		this.vlrExpurgoSaturado = vlrExpurgoSaturado;
	}
	
	/**
	 *	Atribui o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito).
	 * 
	 *	@param		vlrEstorno				Valor de estorno.
	 */
	public void setVlrEstorno(double vlrEstorno) 
	{
		this.vlrEstorno = vlrEstorno;
	}
	
	/**
	 *	Atribui o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito) efetivo.
	 * 
	 *	@param		vlrEstornoEfetivo		Valor de estorno efetivo.
	 */
	public void setVlrEstornoEfetivo(double vlrEstornoEfetivo) 
	{
		this.vlrEstornoEfetivo = vlrEstornoEfetivo;
	}
	
	/**
	 *	Retorna as informacoes referentes ao resultado do processamento da requisicao de expurgo/estorno de bonus
	 *	Pula-Pula em formato delimitado.
	 *
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		DecimalFormat conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR")));
		
		result.append((this.idtLote != null) ? this.idtLote : "");
		result.append(";");
		result.append((this.datReferencia != null) ? new SimpleDateFormat(Definicoes.MASCARA_DATE).format(this.datReferencia) : "");
		result.append(";");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "");
		result.append(";");
		try
		{
			result.append(MapPromocao.getInstancia().getPromocao(this.idtPromocao).getNomPromocao());
		}
		catch(Exception e)
		{
			result.append("");
		}
		result.append(";");
		result.append((this.idtNumeroOrigem != null) ? this.idtNumeroOrigem : "");
		result.append(";");
		result.append((this.idtOrigem != null) ? this.idtOrigem : "");
		result.append(";");
		result.append((this.datProcessamento != null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(this.datProcessamento) : "");
		result.append(";");
		result.append(conversorDouble.format(this.vlrExpurgo));
		result.append(";");
		result.append(conversorDouble.format(this.vlrExpurgoSaturado));
		result.append(";");
		result.append(conversorDouble.format(this.vlrEstorno));
		result.append(";");
		result.append(conversorDouble.format(this.vlrEstornoEfetivo));
		result.append(";");
		
		return result.toString();
	}
	
	/**
	 *	Retorna o valor em formato String.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor no formato String.
	 */
	public String toString(int campo)
	{
	    switch(campo)
	    {
	    	case PromocaoEstornoPulaPula.DAT_REFERENCIA:
	    	{
	    	    if((this.idtOrigem != null) && (this.datReferencia != null))
	    	    {
	    	        try
	    	        {
		    	        if(MapPromocaoOrigemEstorno.getInstance().getTipAnalise(this.idtOrigem).equals(PromocaoOrigemEstorno.TIP_ANALISE_DIARIO))
		    	            return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(this.datReferencia);
	    	        }
	    	        catch(Exception e)
	    	        {
	    	            return null;
	    	        }

	    	        return new SimpleDateFormat(Definicoes.MASCARA_MES).format(this.datReferencia);
	    	    }
	    	    
	    	    return null;
	    	}
	    	case PromocaoEstornoPulaPula.IDT_PROMOCAO:
	    	    return String.valueOf(this.idtPromocao);
	    	case PromocaoEstornoPulaPula.DAT_PROCESSAMENTO:
	    	{
	    		SimpleDateFormat conversorTimestamp = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
	    	    return (this.datProcessamento != null) ? conversorTimestamp.format(this.datProcessamento) : null;
	    	}
	    	case PromocaoEstornoPulaPula.VLR_EXPURGO:
	    	    return String.valueOf(this.vlrExpurgo);
	    	case PromocaoEstornoPulaPula.VLR_EXPURGO_SATURADO:
	    	    return String.valueOf(this.vlrExpurgoSaturado);
	    	case PromocaoEstornoPulaPula.VLR_ESTORNO:
	    	    return String.valueOf(this.vlrEstorno);
	    	case PromocaoEstornoPulaPula.VLR_ESTORNO_EFETIVO:
	    	    return String.valueOf(this.vlrEstornoEfetivo);
	    	default: return null;
	    }
	}
	
}
