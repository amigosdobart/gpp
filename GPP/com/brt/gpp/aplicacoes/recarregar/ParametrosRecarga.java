package com.brt.gpp.aplicacoes.recarregar;

import java.util.Calendar;
import java.util.Date;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapCategoria;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapValoresRecarga;
import com.brt.gpp.comum.mapeamentos.MapValoresRecargaTFPP;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;

/**
 * Este arquivo refere-se a classe ParametrosRecarga, responsavel alojar os dados
 * referente a uma recarga/ajuste
 *
 * <P> Versao:			1.0
 *
 * @Autor: 			Denys Oliveira
 * Data: 				25/03/2004
 *
 * Modificado por:		Marcelo Alves Araujo
 * Data:				22/12/2005
 * Razao:				Alteração na tbl_rec_recargas
 *
 */
public class ParametrosRecarga 
{
	//Campos usados em Recarga/Ajuste
	/**
	 *	Informacoes do assinante na plataforma.
	 */
	private Assinante assinante;
	
	private String idOperacao;		//"A" indica ajuste e "R" indica Recarga
	private String MSISDN; 
	private String tipoTransacao; 
	private String identificacaoRecarga; 
	private String tipoCredito;
	//private double valor; -- Modificacao de Multiplo Saldo
	private ValoresRecarga valores;
	private Date datRecarga; 	// Data em que a Recarga entrou na Plataforma Tecnomen
	private Date datOrigem;	// Data em que a Recarga foi paga/solicitada
	private String operador;
	private String sistemaOrigem;
	private short codigoErro;
	
	private boolean retornoCicloDois;
	
	//Campos usados somente em Recarga
	private double idValor;
	private String nsuInstituicao;
	private String codLoja;
	private Date datInclusao;	// Data em que a Recarga foi logada na tbl_rec_recargas
	private Date dataContabil;	// data contábil da recarga (recarga via banco)
	private String idTerminal;
	private String tipoTerminal;
	private String cpf;
	private String descricao;
	private String hash_cc;
	private String observacao;
	//private short diasExpiracao;
	
	//Campos usados somente em Ajustes
	//private String dataExpiracao; -- Modificacao de Multiplo Saldo
	private String indCreditoDebito;		//"C" indica crédito e "D" indica débito
	
	/**
	 * Retorna true caso o assinante precise retornar para
	 * o ciclo 2 da regua
	 * 
	 * @return retornoCicloDois
	 */
	public boolean deveRetornarCicloDois()
	{
		return this.retornoCicloDois;
	}
	
	/**
	 * Seta o atributo retornaCicloDois para true,
	 * fazendo com que o processo retorne o assinante
	 * para o status 2 da regua
	 *
	 */
	public void setRetornoCicloDois(boolean retorno)
	{
		this.retornoCicloDois = retorno;
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
	 *Metodo...: getCodLoja
	 *Descricao: Retorna código da loja
	 *  @return	String	Código da Loja
	 */
	public String getCodLoja() 
	{
		return codLoja;
	}

	/**
	 * Metodo...: getCpf
	 * Descricao: Retorna o CPF do assinante
	 * @return	String	CPF
	 */
	public String getCpf() 
	{
		return cpf;
	}
	
	/**
	 * Metodo...: getDescricao
	 * Descricao: Retorna a descricao do ajuste
	 * @return	String	descricao
	 */
	public String getDescricao() 
	{
		return descricao;
	}

	/**
	 * Metodo...: getDataContabil
	 * Descricao: Retorna a data Contábil
	 * @return String	Data Contábil
	 */
	public Date getDataContabil() 
	{
		return dataContabil;
	}

	/**
	 * Metodo...: getDatRecarga
	 * Descricao: Retorna a Data/Hora em que a Recarga entrou na Plataforma Tecnomen
	 * @return	String		Data/hora em que a Recarga entrou na Plataforma Tecnomen
	 */
	public Date getDatRecarga() 
	{
		return datRecarga;
	}
	
	/**
	 * Metodo...: getDatOrigema
	 * Descricao: Retorna a Data/Hora em que a Recarga foi paga/solicitada
	 * @return	String		Data/hora em que a Recarga foi paga/solicitada
	 */
	public Date getDatOrigem() 
	{
		return datOrigem;
	}

	/**
	 * Metodo...: getDatInclusao
	 * Descricao: Retorna da Data/Hora em que a Recarga foi logada na tbl_rec_recargas
	 * @return	String		Data/Hora em que a Recarga foi logada na tbl_rec_recargas
	 */
	public Date getDatInclusao() 
	{
		return datInclusao;
	}

	/**
	 * Metodo...: getHash_cc
	 * Descricao: Retorna o Hash associado ao cartão de crédito
	 * @return		String		Hash do cartão de crédito
	 */
	public String getHash_cc() 
	{
		return hash_cc;
	}

	/**
	 * Metodo...: getIdentificacaoRecarga
	 * Descricao: Retorna o Identificador da Recarga
	 * @return	String	Identificador da Recarga
	 */
	public String getIdentificacaoRecarga() 
	{
		return identificacaoRecarga;
	}

	/**
	 * Metodo...: getIdTerminal
	 * Descricao: Retorna o Id do Terminal
	 * @return	String		Identificador do Terminal
	 */
	public String getIdTerminal() 
	{
		return idTerminal;
	}

	/**
	 * Metodo...: getMSISDN
	 * Descricao: Retorna o MSISDN que fez a recarga
	 * @return	String		MSISDN
	 */
	public String getMSISDN() 
	{
		return MSISDN;
	}

	/**
	 * Metodo...: getNsuInstituicao
	 * Descricao: Retorna o NSU da Instituição
	 * @return	String	NSU da Instituição
	 */
	public String getNsuInstituicao() 
	{
		return nsuInstituicao;
	}

	/**
	 * Metodo...: getOperator
	 * Descricao: Retorna o Operador que solicitou a recarga/ajuste
	 * @return	String	Identificador do Operador
	 */
	public String getOperador() 
	{
		return operador;
	}

	/**
	 * Metodo...: getSistemaOrigem
	 * Descricao: Retorna o Sistema de Origem
	 * @return	String	Sistema de Origem
	 */
	public String getSistemaOrigem() 
	{
		return sistemaOrigem;
	}

	/**
	 * Metodo...: getTipoCredito
	 * Descricao: Retorna o tipo de crédito
	 * @return	String	Tipo de Crédito
	 */
	public String getTipoCredito() 
	{
		return tipoCredito;
	}

	/**
	 * Metodo...: getTipoTerminal
	 * Descricao: Retorna o Tipo do Terminal
	 * @return	String		Tipo do Terminal
	 */
	public String getTipoTerminal() 
	{
		return tipoTerminal;
	}

	/**
	 * Metodo...: getTipoTransacao
	 * Descricao: Retorna o Transaction Type
	 * @return	String	Transaction Type
	 */
	public String getTipoTransacao() 
	{
		return tipoTransacao;
	}

	/**
	 *	Atribui as informacoes do assinante na plataforma.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma Tecnomen.
	 */
	 public void setAssinante(Assinante assinante)
	 {
	 	this.assinante = assinante;	
	 }

	/**
	 * Metodo...: setCodLoja
	 * Descricao: Seta o Código da Loja
	 * @param Strig	string	Código da Loja 
	 */
	public void setCodLoja(String string) 
	{
		codLoja = string;
	}

	/**
	 * Metodo...: setCpf
	 * Descricao: Atribui valor ao CPF
	 * @param String	string	CPF
	 */
	public void setCpf(String string) 
	{
		cpf = string;
	}
	
	/**
	 * Metodo...: setDescricao
	 * Descricao: Atribui valor a descricao do ajuste
	 * @param String	string	descricao
	 */
	public void setDescricao(String string) 
	{
		descricao = string;
	}
	
	/**
	 * Metodo...: setDataContabil
	 * Descricao: Atribui valor à data contábil do Banco
	 * @param String	string	Data Contábil
	 */
	public void setDataContabil(Date dataContabil) 
	{
		this.dataContabil = dataContabil;
	}

	/**
	 * Metodo...: setDatRecarga
	 * Descricao: Atribui valor à Data/Hora em que a Recarga entrou na Plataforma Tecnomen
	 * @param String	string	Data/hora em que a Recarga entrou na Plataforma Tecnomen
	 */
	public void setDatRecarga(Date datRecarga) 
	{
		this.datRecarga = datRecarga;
	}
	
	/**
	 * Metodo...: setDatOrigem
	 * Descricao: Atribui valor à Data/Hora em que a Recarga foi paga/solicitada
	 * @param String	string	Data/hora em que a Recarga foi paga/solicitada
	 */
	public void setDatOrigem(Date datOrigem) 
	{
		this.datOrigem = datOrigem;
	}

	/**
	 * Metodo...: setDatInclusao
	 * Descricao: Atribui valor à data/hora em que a Recarga foi logada na tbl_rec_recargas
	 * @param String	string	data/hora em que a Recarga foi logada na tbl_rec_recargas
	 */
	public void setDatInclusao(Date datInclusao) 
	{
		this.datInclusao = datInclusao;
	}

	/**
	 * Metodo...: setHash_cc
	 * Descrição: Atribui valor ao Hash do cartão de crédito
	 * @param String	string	Hash do cartão de crédito
	 */
	public void setHash_cc(String string) 
	{
		hash_cc = string;
	}

	/**
	 * Metodo...: setIdentificacaoRecarga
	 * Descricao: Atribui valor ao Id da Recarga
	 * @param String	string	Id da Recarga
	 */
	public void setIdentificacaoRecarga(String string) 
	{
		identificacaoRecarga = string;
	}

	/**
	 * Metodo...: setIdTerminal
	 * Descricao: Atribui valor ao Id do Terminal
	 * @param String	string	ID do Terminal
	 */
	public void setIdTerminal(String string) 
	{
		idTerminal = string;
	}

	/**
	 * Metodo...: setMSISND
	 * Descricao: Atribui valor ao MSISDN
	 * @param String	string	MSISDN
	 */
	public void setMSISDN(String string) 
	{
		MSISDN = string;
	}

	/**
	 * Metodo...: setNsuInstituicao
	 * Descricao: Atribui valor ao NSU da Instituição
	 * @param String	string	NSU da Instituição
	 */
	public void setNsuInstituicao(String string) 
	{
		nsuInstituicao = string;
	}

	/**
	 * Metodo...: setOperador
	 * Descricao: Atribui valor ao Operador
	 * @param String	string	Operador
	 */
	public void setOperador(String string) 
	{
		operador = string;
	}

	/**
	 * Metodo...: setSistemaOrigem
	 * Descricao: Atribui valor ao Sistema de Origem
	 * @param String	string	Sistema de Origem
	 */
	public void setSistemaOrigem(String string) 
	{
		sistemaOrigem = string;
	}

	/**
	 * Metodo...: setTipoCredito
	 * Descricao: Seta o Tipo do Crédito
	 * @param String	string	Tipo do Crédito
	 */
	public void setTipoCredito(String string) 
	{
		tipoCredito = string;
	}

	/**
	 * Metodo...: setTipoTerminal
	 * Descricao: Seta o Tipo de Terminal
	 * @param String	string	Tipo de Terminal
	 */
	public void setTipoTerminal(String string) 
	{
		tipoTerminal = string;
	}

	/**
	 * Metodo...: setTipoTransacao
	 * Descricao: Seta o Tipo de Transação
	 * @param String	string	Transaction Type
	 */
	public void setTipoTransacao(String string) 
	{
		tipoTransacao = string;
	}

	/**
	 * Metodo...: getIdOperacao
	 * Descricao: Retorna o ID da Operação
	 * @return	String	ID da Operação
	 */
	public String getIdOperacao() 
	{
		return idOperacao;
	}

	/**
	 * Metodo...: setIdOperacao
	 * Descricao: SEta o ID da Operacao
	 * @param String	string		ID Operacao
	 */
	public void setIdOperacao(String string) 
	{
		idOperacao = string;
	}

	/**
	 * Metodo...: getCodigoErro
	 * Descricao: Retorna o Código de Erro
	 * @return	short		Código de Erro
	 */
	public short getCodigoErro() 
	{
		return codigoErro;
	}

	/**
	 * Metodo...: setCodigoErro
	 * Descricao: Seta o Código do Erro
	 * @param short	code	Código do Erro	
	 */
	public void setCodigoErro(short code) 
	{
		codigoErro = code;
	}

	/**
	 * Metodo...: getIndCreditoDebito
	 * Descricao: Flag para indicar se a operação é um  crédito ('C') ou débito ('D')
	 * @return	String		'C' para crédito, 'D' para débito
	 */
	public String getIndCreditoDebito() 
	{
		return indCreditoDebito;
	}

	/**
	 * Metodo...: setIndCreditoDebito
	 * Descricao: Seta o Indicador de Crédito/Débito do Objeto
	 * @param string		'C' para crédito; 'D' para débito
	 */
	public void setIndCreditoDebito(String string) 
	{
		indCreditoDebito = string;
	}

	/**
	 *	Retorna os valores de recarga.
	 *
	 *	@return		Valores de recarga.
	 */
	public ValoresRecarga getValores() 
	{
		return this.valores;
	}
	
	/**
	 *	Atribui os valores de recarga.
	 *
	 *	@param		valores					Valores de recarga.
	 */
	public void setValores(ValoresRecarga valores) 
	{
		this.valores = valores;
	}
	
	/**
	 * @return Returns the idValor.
	 */
	public double getIdValor() 
	{
		return idValor;
	}
	
	/**
	 * @param idValor The idValor to set.
	 */
	public void setIdValor(double idValor) 
	{
		this.idValor = idValor;
	}

	/**
	 * @return Returns the observacao.
	 */
	public String getObservacao() 
	{
		return observacao;
	}
	
	/**
	 * @param observacao The observacao to set.
	 */
	public void setObservacao(String observacao) 
	{
		this.observacao = observacao;
	}

	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append((this.getAssinante() != null) ? this.getAssinante().toString() : null);
		buffer.append(" - TT: ");
		buffer.append(this.tipoTransacao);
		buffer.append(" - ");
		buffer.append((this.getValores() != null) ? this.getValores().toString() : null);
		
		return buffer.toString(); 
	}
	
	/**
	 * Metodo....: getParametrosRecarga
	 * Descricao.: Retorna um objeto ParametrosRecarga com os valores
	 * 			   do assinante
	 * 
	 * @param  assinante - Objeto contendo os dados do assinante
	 * @param  idValor	 - Valor da recarga
	 * @return recarga	 - Objeto com os valores do assinante/recarga
	 */
	public static ParametrosRecarga getParametrosRecarga(Assinante assinante, double idValor)
	{
		ParametrosRecarga recarga = getParametros(assinante, idValor);
		recarga.setIdOperacao(Definicoes.TIPO_RECARGA);
		
		return recarga;
	}
	
	/**
	 * Metodo....: getParametrosAjuste
	 * Descricao.: Retorna um objeto ParametrosRecarga com os valores
	 * 			   do assinante
	 * 
	 * @param  assinante - Objeto contendo os dados do assinante
	 * @return recarga	 - Objeto com os valores do assinante/ajuste
	 */
	public static ParametrosRecarga getParametrosAjuste(Assinante assinante)
	{
		/////////// - ATENCAO - ///////////
		// O ValoresRecarga NAO SERA PREENCHIDO para os parametros de ajuste
		// desse metodo, tendo que ser setado no metodo que o invoca.
		// Eh passado o idValor=0 para o metodo getParametros(Assinante, idValor),
		// fazendo com que a entidade ValoresRecarga seja SEMPRE NULL
		ParametrosRecarga recarga = getParametros(assinante, 0);
		recarga.setIdOperacao(Definicoes.TIPO_AJUSTE);
		
		return recarga;
	}
	
	/**
	 * Metodo....: getParametros
	 * Descricao.: Retorna uma entidade ParametrosRecarga preenchida
	 * 
	 * @param assinante - Objeto com as informacoes do assinante
	 * @return recarga	- Objeto com as informacoes da recarga/assinante
	 */
	private static ParametrosRecarga getParametros(Assinante assinante, double idValor)
	{
		ParametrosRecarga recarga = new ParametrosRecarga();
		recarga.setMSISDN(assinante.getMSISDN());
		recarga.setAssinante(assinante);
		recarga.setIdValor(idValor);
		
		if (recarga.getDatOrigem() == null)
			recarga.setDatOrigem(Calendar.getInstance().getTime());
		
		if(assinante.getNaturezaAcesso().equalsIgnoreCase("GSM") ||
		   assinante.getNaturezaAcesso().equalsIgnoreCase("INEXISTENTE"))
		{
			int idCategoria = MapPlanoPreco.getInstance().consultaCategoria(assinante.getPlanoPreco());
			Categoria categoria = MapCategoria.getInstance().getCategoria(idCategoria);
			recarga.setValores(MapValoresRecarga.getInstance().getValoresRecarga(recarga.getIdValor(), categoria, recarga.getDatOrigem()));
		}
		else
			recarga.setValores(MapValoresRecargaTFPP.getInstancia().getValorRecarga(recarga.getIdValor()));
		
		return recarga;
	}
}