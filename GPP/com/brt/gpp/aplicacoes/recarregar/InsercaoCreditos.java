package com.brt.gpp.aplicacoes.recarregar;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.masc.ConexaoMASC;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;
import com.brt.gpp.comum.mapeamentos.MapSistemaOrigem;
import com.brt.gpp.comum.mapeamentos.MapTipoCredito;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoRetorno;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.RevendaVarejo;
import com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem;
import com.brt.gpp.comum.mapeamentos.entidade.TipoCredito;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 *
 * Este arquivo refere-se a superclasse InsercaoCreditos, que possui métodos/atributos
 * comuns tanto para Recargas quanto para Ajustes
 *
 * <P> Versao:		1.0
 *
 * @Autor: 			Denys Oliveira
 * Data: 			25/03/2004
 *
 * Modificado por: 	Vitor Murilo
 * Data: 05/12/2007			
 * Razao: Consulas e atualizações a assinantes realizados por processos de recarga 
 * passaram a ser realizados pelo PaymentInterface 			
 */
public class InsercaoCreditos extends Aplicacoes 
{
	GerentePoolBancoDados gerenteBanco = null;
	GerentePoolTecnomen gerenteInsercaoCreditos = null;
	
	/*****
	 * Metodo...: InsercaoCreditos
	 * Descricao: Construntor da classe
	 * @param 	aIdProcesso				Id do processo no pool de log
	 * @param	aClasseFilha			Classe filha que esta instanciando o objeto
	 */
	public InsercaoCreditos(long aIdProcesso, String aClasseFilha)
	{
		// Armazena a classe filha
		super(aIdProcesso, aClasseFilha);
		
		// Pega conexões com Banco de Dados e Tecnomen
		this.gerenteInsercaoCreditos = GerentePoolTecnomen.getInstancia(aIdProcesso);
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);
	}
	
	/**
	 *	Executa validacao de consulta pre-recarga.
	 *
	 *	@param		recarga					Parametros da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short validarPreRecarga(ParametrosRecarga recarga)
	{
		short result = this.validarAssinante(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		result = this.validarValorRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;

		result = this.limiteMaximo(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 * Metodo...: validarParametros
	 * Descricao: Valida Parâmetros de Recarga/Ajuste
	 * @param 	ParametrosRecarga	aParametros		Objeto com Parametros de Recarga/Ajuste
	 * @return	short								(0) se ok; (!0) se nok
	 */
	public short validarParametros(ParametrosRecarga parametros)
	{
		short result = Definicoes.RET_OPERACAO_OK;
		
		super.log(Definicoes.DEBUG, "validarParametros", parametros.toString());
		
		//Faz validações aplicáveis tanto a recargas quanto a ajustes
		result = this.validacoesComuns(parametros);
		
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		if(parametros.getIdOperacao().equals(Definicoes.TIPO_RECARGA))
			result = this.validarRecarga(parametros);
		else
			result = this.validarAjuste(parametros);
		
		super.log(Definicoes.DEBUG, "validacoesComuns", parametros.toString() + " - Retorno da validacao: " + result);
		
		return result;
	}
	
	/** 
	 * Metodo...: validacoesComuns
	 * Descricao: Realiza as validacoes comuns de recargas e ajustes
	 * @param	ParametrosRecarga	aParametro		Parametros de Recargas/Ajustes
	 * @return 	short								0 se OK, diferente se erro
	 */
	private short validacoesComuns(ParametrosRecarga recarga)
	{
		short result = Definicoes.RET_OPERACAO_OK;

		//Validacao do Assinante.
		result = this.validarAssinante(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;

		//Validacao do tipo de credito.
		result = this.validarTipoCredito(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;

		//Validacao do valor de recarga.
		result = this.validarValorRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		//Valicacao do Tipo de Transacao e do Sistema de Origem.
		result = this.validarTTSistemaOrigem(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		//Validacao do limite maximo.
		if(recarga.getIdOperacao().equals(Definicoes.TIPO_RECARGA) || recarga.getIdOperacao().equals(Definicoes.TIPO_AJUSTE) && recarga.getIndCreditoDebito().equals(Definicoes.TIPO_AJUSTE_CREDITO))
			result = this.limiteMaximo(recarga);

		return result;
	}
	
	/**
	 * Metodo...: validacoesRecarga
	 * Descrição: Validações Exclusivas para Recargas
	 * @param  	ParametrosRecarga	aParametro	Parâmetros a serem Validados
	 * @return	short							0 se ok; !0 se nok
	 */
	private short validarRecarga(ParametrosRecarga recarga)
	{
		short result = Definicoes.RET_OPERACAO_OK;
/* TRAVA PARA TIPO_TRANSACAO CREDITO_DEBITO		
		//Validacao do tipo de lancamento da origem de recarga.
		result = this.validarTipoTransacaoRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
*/		
		//Validacao do tipo de credito para recargas.
		result = this.validarTipoCreditoRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		//Validacao do identificador de recarga.
		result = this.validaIdRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		//Validacao de cartao de credito.
		result = this.recargaCC(recarga.getTipoTransacao(), 
								recarga.getHash_cc(), 
								recarga.getCpf(), 
								recarga.getSistemaOrigem());
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		//Validacao de singularidade de recarga.
		result = this.validarNovaRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;

		//Validacao de recarga varejo.
		if((recarga.getSistemaOrigem().equals(Definicoes.SO_VAREJO)) || 
		   (recarga.getSistemaOrigem().equals(Definicoes.SO_DEALER)))
			result = this.validarRecargaVarejo(recarga);
		
		return result;
	}
	
	/**
	 *	Executa as validacoes de recargas TFPP.
	 *
	 *	@param		parametros				Parametros da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	protected short validarRecargaMASC(ParametrosRecarga parametros)
	{
		return this.validarNovaRecarga(parametros);
	}
	
	/**
	 * Metodo...: validacoesAjuste
	 * Descricao: Validações Exclusivas para ajustes
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 * @return	short							0, se ok; !0 se nok
	 */
	private short validarAjuste(ParametrosRecarga ajuste)
	{
		short result = Definicoes.RET_OPERACAO_OK;

/* TRAVA PARA TIPO_TRANSACAO CREDITO_DEBITO		
		//Validacao do tipo de lancamento em relacao ao tipo de ajuste.
		result = this.validarTipoTransacaoAjuste(ajuste);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
*/		
		//Validacao de saldo do assinante suficiente para a operacao de debito.
		if(ajuste.getIndCreditoDebito().equals(Definicoes.TIPO_AJUSTE_DEBITO))
			result = this.saldoSuficiente(ajuste);

		return result;
	}
	
	/**
	 *	Valida se o tipo de transacao do ajuste possui tipo de lancamento coerente com o tipo de ajuste, ou seja, 
	 *	se o ajuste for de credito o tipo de lancamento tambem deve ser de credito e vice-versa.
	 * 
	 *	@param		ajuste					Parametros do ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short validarTipoTransacaoAjuste(ParametrosRecarga ajuste)
	{
		try
		{
			OrigemRecarga origem = MapRecOrigem.getInstance().getOrigemRecarga(ajuste.getTipoTransacao());

			if(!origem.getTipLancamento().equals(ajuste.getIndCreditoDebito()))
				return Definicoes.RET_VALOR_CREDITO_INVALIDO;
			
			return Definicoes.RET_OPERACAO_OK;
		}
		catch(Exception e)
		{
			return Definicoes.RET_ERRO_TECNICO;
		}
	}
	
	/**
	 * Metodo...: validaIdRecargaDataHora
	 * Descricao: Valida Identificador de Recarga/Data Hora da Recarga
	 * @param ParametrosRecarga	aParametro
	 * @return	short	
	 */
	private short validaIdRecarga(ParametrosRecarga recarga)
	{
		if((recarga.getIdentificacaoRecarga() == null) || (recarga.getIdentificacaoRecarga().equals("")))
			return Definicoes.RET_ID_RECARGA_AJUSTE_INVALIDO;

		return Definicoes.RET_OPERACAO_OK;
	}

	/***
	 * Metodo...: subscValido
	 * Descricao: Retorno se um assinante é válido ou não
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 * @return	short		0=assinante válido; !0=assinante inválido
	 */
	private short validarAssinante(ParametrosRecarga recarga)
	{
		Assinante assinante = recarga.getAssinante();
		
		if(assinante == null)
			return Definicoes.RET_MSISDN_NAO_ATIVO;

		if(assinante.getRetorno() != Definicoes.RET_OPERACAO_OK)
			return assinante.getRetorno();
		
		if(assinante.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
			return Definicoes.RET_STATUS_MSISDN_INVALIDO;
		
		if(assinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
			return Definicoes.RET_MSISDN_BLOQUEADO;
		
		if((assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NAO_APLICAVEL) &&
		   (assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NORMAL_USER))
			return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
		
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 *	Valida o limite maximo do saldo do assinante durante o processo de insercao de creditos. Para cada saldo do 
	 *	assinante, a quantidade de creditos somado ao valor de insercao nao pode ultrapassar o limite estabelecido 
	 *	para a plataforma.
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e dos valores de recarga.
	 *  
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short limiteMaximo(ParametrosRecarga recarga)
	{
		try
		{
			double			limite		= Double.parseDouble(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("SALDO_MAXIMO"));
			Assinante		assinante	= recarga.getAssinante();
			ValoresRecarga	valores		= recarga.getValores();
			
			if((limite < valores.getSaldoPrincipal() + valores.getValorBonusPrincipal() + assinante.getCreditosPrincipal()) ||
			   (limite < valores.getSaldoPeriodico() + valores.getValorBonusPeriodico() + assinante.getCreditosPeriodico()) ||
			   (limite < valores.getSaldoBonus    () + valores.getValorBonusBonus    () + assinante.getCreditosBonus    ()) ||
			   (limite < valores.getSaldoSMS      () + valores.getValorBonusSMS      () + assinante.getCreditosSms      ()) ||
			   (limite < valores.getSaldoGPRS     () + valores.getValorBonusGPRS     () + assinante.getCreditosDados    ()))
				return Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO;
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "limiteMaximo", "Excecao: " + e);
			return Definicoes.RET_ERRO_TECNICO;
		}

		return Definicoes.RET_OPERACAO_OK;
	}

	/**
	 *	Valida os valores de recarga. 
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e de seu plano de preco. 
	 *
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short validarValorRecarga(ParametrosRecarga recarga)
	{
		Assinante		assinante	= recarga.getAssinante();
		ValoresRecarga	valores		= recarga.getValores();
		
		if(recarga.getValores() == null)
			return Definicoes.RET_VALOR_CREDITO_INVALIDO;
		
		//Verificando se ha algum valor de recarga para um saldo que nao se aplica a categoria do plano do assinante.
		PlanoPreco plano = MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco());
		
		if((valores.getSaldoPrincipal() != 0.0) || (valores.getDataExpPrincipal() != null) || (valores.getNumDiasExpPrincipal() > 0))
		{
			TipoSaldo tipoSaldo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.PRINCIPAL);
			if(!plano.possuiSaldo(tipoSaldo))
				return Definicoes.RET_SALDO_ASSINANTE_INVALIDO;
		}
		
		if((valores.getSaldoPeriodico() != 0.0) || (valores.getDataExpPeriodico() != null) || (valores.getNumDiasExpPeriodico() > 0))
		{
			TipoSaldo tipoSaldo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.PERIODICO);
			if(!plano.possuiSaldo(tipoSaldo))
				return Definicoes.RET_SALDO_ASSINANTE_INVALIDO;
		}
		
		if((valores.getSaldoBonus() != 0.0) || (valores.getDataExpBonus() != null) || (valores.getNumDiasExpBonus() > 0))
		{
			TipoSaldo tipoSaldo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.BONUS);
			if(!plano.possuiSaldo(tipoSaldo))
				return Definicoes.RET_SALDO_ASSINANTE_INVALIDO;
		}
		
		if((valores.getSaldoSMS() != 0.0) || (valores.getDataExpSMS() != null) || (valores.getNumDiasExpSMS() > 0))
		{
			TipoSaldo tipoSaldo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.TORPEDOS);
			if(!plano.possuiSaldo(tipoSaldo))
				return Definicoes.RET_SALDO_ASSINANTE_INVALIDO;
		}
		
		if((valores.getSaldoGPRS() != 0.0) || (valores.getDataExpGPRS() != null) || (valores.getNumDiasExpGPRS() > 0))
		{
			TipoSaldo tipoSaldo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.DADOS);
			if(!plano.possuiSaldo(tipoSaldo))
				return Definicoes.RET_SALDO_ASSINANTE_INVALIDO;
		}
		
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 *	Valida o Tipo de Transacao e o Sistema de Origem da Recarga. A primeira fase da validacao consiste na 
	 *	existencia do Tipo de Transacao e do Sistema de Origem. Depois e necessario verificar se a categoria de plano 
	 *	de preco do assinante tem permissao de utilizacao do Tipo de Transacao. Finalmente o metodo verifica se o 
	 *	Sistema de Origem tem permissao de utilizacao do Tipo de Transacao.
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e de seu plano de preco. 
	 *
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short validarTTSistemaOrigem(ParametrosRecarga recarga)
	{
		try
		{
			OrigemRecarga	origem		= MapRecOrigem.getInstance().getOrigemRecarga(recarga.getTipoTransacao());
			SistemaOrigem	sistema		= MapSistemaOrigem.getInstancia().getSistemaOrigem(recarga.getSistemaOrigem());
			PlanoPreco		plano		= MapPlanoPreco.getInstancia().getPlanoPreco(recarga.getAssinante().getPlanoPreco());
			
			if((origem == null) || (!plano.isPermitida(origem)) || !origem.ativo())
				return Definicoes.RET_TIPO_TRANSACAO_INVALIDO;
			
			if(sistema == null)
				return Definicoes.RET_SISTEMA_INEXISTENTE;
			
			if(!sistema.isPermitida(origem))
				return Definicoes.RET_TIPO_TRANSACAO_INVALIDO;
		}
		catch(Exception e)
		{
			return Definicoes.RET_ERRO_TECNICO;
		}
		
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 *	Valida o tipo de credito da recarga ou ajuste.
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e de seu plano de preco. 
	 *
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short validarTipoCredito(ParametrosRecarga recarga)
	{
		//Obtendo as informacoes do tipo de credito.
		TipoCredito tipoCredito = MapTipoCredito.getInstance().getTipoCredito(recarga.getTipoCredito());
		
		if(tipoCredito != null)
		{
			//Obtendo as informacoes referentes a categoria do assinante.
			PlanoPreco plano = MapPlanoPreco.getInstance().getPlanoPreco(recarga.getAssinante().getPlanoPreco());
			
			//Alem de validar se o tipo de credito esta definido, tambem e necessario verificar se o tipo de credito
			//se aplica a um saldo que a categoria do plano do assinante possua.
			if(plano.possuiSaldo(tipoCredito.getTipoSaldo()))
				return Definicoes.RET_OPERACAO_OK;
		}
		
		return Definicoes.RET_TIPO_CREDITO_INVALIDO;
	}
	
	/**
	 *	Executa validacao de consulta pre-recarga de varejo. Executa as validacoes da consulta pre-recarga. Caso o 
	 *	assinante tenha sido validado, faz a validacao do agente de varejo.
	 *
	 *	@param		recarga					Parametros da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short validarPreRecargaVarejo(ParametrosRecarga recarga)
	{
		short result = this.validarPreRecarga(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		result = this.validarTTSistemaOrigem(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		result = this.validarRecargaVarejo(recarga);
		if(result != Definicoes.RET_OPERACAO_OK)
			return result;
		
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 *	Valida se o tipo de transacao de recarga possui o tipo de lancamento coerente, ou seja, de credito.
	 * 
	 *	@param		recarga					Parametros da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short validarTipoTransacaoRecarga(ParametrosRecarga recarga)
	{
		try
		{
			OrigemRecarga origem = MapRecOrigem.getInstance().getOrigemRecarga(recarga.getTipoTransacao());

			if(!origem.getTipLancamento().equals(Definicoes.TIPO_AJUSTE_CREDITO))
				return Definicoes.RET_VALOR_CREDITO_INVALIDO;
			
			return Definicoes.RET_OPERACAO_OK;
		}
		catch(Exception e)
		{
			return Definicoes.RET_ERRO_TECNICO;
		}
	}
	
	/**
	 *	Valida o tipo de credito para a operacao especifica de recarga. Esta validacao e necessaria para garantir 
	 *	duas coisas: Um valor de credito no Saldo de Franquia esteja definido para recargas de franquia e nenhum 
	 *	valor de franquia esteja definido em recargas normais.
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e de seu plano de preco e da existencia 
	 *	do tipo de credito. 
	 *
	 *	@param		recarga					Parametros da recarga ou ajuste.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short validarTipoCreditoRecarga(ParametrosRecarga recarga)
	{
		//Obtendo as informacoes do tipo de credito.
		TipoCredito tipoCredito = MapTipoCredito.getInstance().getTipoCredito(recarga.getTipoCredito());
		
		//Validando se o tipo de credito e referente a recarga de franquia e o valor de credito no saldo periodico 
		//nao esta definido.
		if((tipoCredito.getIdTipoCredito().equals(Definicoes.TIPO_CREDITO_FRANQUIA)) &&
		   (recarga.getValores().getSaldoPeriodico() <= 0.0))
			return Definicoes.RET_TIPO_CREDITO_INVALIDO;
		
		//Validando se o tipo de credito nao e referente a recarga de franquia mas existe valor ou dias de expiracao
		//definidos para este saldo.
		if((!tipoCredito.getIdTipoCredito().equals(Definicoes.TIPO_CREDITO_FRANQUIA)) &&
		   ((recarga.getValores().getSaldoPeriodico() > 0.0) ||
		   	(recarga.getValores().getNumDiasExpPeriodico() > 0)))
			return Definicoes.RET_TIPO_CREDITO_INVALIDO;
		
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 * Metodo...: recargaCC
	 * Descricao: Verifica se os parametros necessarios para  uma recarga via Cartao de Creditos
	 * 				foram passados devidamente
	 * @param 	String	tipoTransacao	Transaction Type
	 * @param 	String	hashCC			Hash do Cartao de Credito
	 * @param 	String	cpf				CPF do comprador
	 * @param 	String  sistemaOrigem	Sistema origem que esta realizando a venda de creditos
	 * @return	short					0, se ok; 28, se nao ok (parametros insuficientes)
	 */
	private short recargaCC(String tipoTransacao, String hashCC, String cpf, String sistemaOrigem)
	{
		String canal = tipoTransacao.substring(0,2);
		
		// Mapeamento do sistema de origem, para verificar se é necessário validar cpf/hash
		SistemaOrigem sistema = MapSistemaOrigem.getInstancia().getSistemaOrigem(sistemaOrigem);
		
		if (canal.equals(Definicoes.CANAL_CC))
			// Verifica se o CPF e o hasc do cartao foram informados e o sistema de origem nao 
			// e o microsiga - nao eh possivel enviar CPF no microsiga
			if(sistema.validaCc() && ((hashCC == null) || (cpf == null) || hashCC.equals("") || cpf.equals("")))
				return Definicoes.RET_CAMPO_OBRIGATORIO;
		
		return Definicoes.RET_OPERACAO_OK;
	}

	/**
	 * Metodo...: novaRecarga
	 * Descricao: Verifica se a recarga solicitada já foi efetuada
	 *	@param		recarga					Parametros da recarga.
	 * @return	short							0=Recarga Nova; !0=Recarga já efetuada
	 */
	private short validarNovaRecarga(ParametrosRecarga recarga)
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			
			if(recarga.getAssinante().getNaturezaAcesso().equals("GSM"))
			{
				if(!RecargaDAO.isNovaRecargaGSM(recarga.getIdentificacaoRecarga(), 
						recarga.getTipoTransacao(),
						recarga.getDatOrigem(),
						conexaoPrep))
					return Definicoes.RET_RECARGA_JA_EFETUADA;
			}
			else if(recarga.getAssinante().getNaturezaAcesso().equals("TFPP"))
			{
				if(!RecargaDAO.isNovaRecargaTFPP(recarga.getIdentificacaoRecarga(), 
						 recarga.getTipoTransacao(),
						 recarga.getDatOrigem(),
						 conexaoPrep))
					return Definicoes.RET_RECARGA_JA_EFETUADA;
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "validarNovaRecarga", recarga.toString() + " - Excecao: " + e);
			return Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		}
		
		return Definicoes.RET_OPERACAO_OK;
	}

	/**
	 *	Valida o status do agente de varejo e a quantidade ja utilizada em relacao ao seu limite.
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do tipo de transacao e do valor da recarga.
	 * 
	 *	@param		tipoTransacao			Tipo de transacao da recarga, que corresponde ao identificador do agente.
	 *	@param		valores					Informacoes de valores da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short validarRecargaVarejo(ParametrosRecarga recarga)
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			//Obtendo conexao com o banco de dados.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			//Obtendo as informacoes da revenda de varejo.
			RevendaVarejo revenda = RecargaDAO.getRevendaVarejo(recarga.getTipoTransacao(), conexaoPrep);
			
			//Validacao da existencia da revenda.
			if(revenda == null)
				return Definicoes.RET_CODIGO_REVENDA_NAO_CADASTRADO;
			
			//Validacao de status da revenda.
			if(revenda.isBloqueada())
				return Definicoes.RET_REVENDA_BLOQUEADA;
			
			//Validacao do limite da revenda.
			if(revenda.getVlrUtilizado() + recarga.getValores().getValorEfetivoPago() > revenda.getVlrLimite())
				return Definicoes.RET_LIMITE_REVENDA_ULTRAPASSADO;
			
			return Definicoes.RET_OPERACAO_OK;
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "validaRecargaVarejo", recarga.toString() + " - Excecao: " + e);
			return Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			this.gerenteBanco.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
	}
	
	/**
	 * Metodo...: saldoSuficiente
	 * Descricao: Verifica se o Saldo do Cliente é suficiente para se debitar aValor
	 *	@param		ajuste					Parametros da recarga ou ajuste.
	 * @return	short			0=saldo é suficiente; !0=saldo insuficiente
	 */
	private short saldoSuficiente(ParametrosRecarga ajuste)
	{
		Assinante		assinante	= ajuste.getAssinante();
		ValoresRecarga	valores		= ajuste.getValores();
		
		if((assinante.getCreditosPrincipal() - Math.abs(valores.getSaldoPrincipal()) < 0.0) ||
		   (assinante.getCreditosPeriodico() - Math.abs(valores.getSaldoPeriodico()) < 0.0) ||
		   (assinante.getCreditosBonus    () - Math.abs(valores.getSaldoBonus    ()) < 0.0) ||
		   (assinante.getCreditosSms      () - Math.abs(valores.getSaldoSMS      ()) < 0.0) ||
		   (assinante.getCreditosDados    () - Math.abs(valores.getSaldoGPRS     ()) < 0.0))
			return Definicoes.RET_CREDITO_INSUFICIENTE;
	
		return Definicoes.RET_OPERACAO_OK;
	}	

	/**
	 * Metodo...: enviarSMS
	 * Descricao: Preenche tabela com SMS a ser enviado referente à recarga/ajuste
	 * @param 	String	MSISDN				MSISDN do Assinante
	 * @param 	int		prioridadeMensagem	Prioridade da Mensagem
	 * @param 	double	valorRecarga		Valor da Recarga/Ajuste
	 * @param 	String	dataHora			Data/Hora da Recarga
	 * @param 	Double	saldoFinal			Saldo Final do Assinante após a recarga/ajuste
	 * @param 	int		diasExpiracao		Número de Dias de Expiração dos créditos
	 * @return	boolean						true=Inserção ok na tabela; false, caso contrário
	 * @throws GPPTecnomenException
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	public boolean enviarSMS(ParametrosRecarga recarga, int prioridadeMensagem, Date dataHora)
	{
		boolean retorno = true;
		ValoresRecarga valores = recarga.getValores();
		Assinante assinante = recarga.getAssinante();
		ConsumidorSMS direcionadorSMS = ConsumidorSMS.getInstance(super.getIdLog());
		String msgSMSTemplate = null;
		DecimalFormat d = new DecimalFormat("0.00");
		SimpleDateFormat conversorTimestamp = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
		String texto1 = null;
		String texto2 = null;
		String texto3 = null;
		String texto4 = null;
		String texto5 = null;
		String avalorRecarga = null;
		String asaldoFinal = null;
		
		super.log(Definicoes.DEBUG, "enviarSMS", recarga.toString());

		try
		{
			//Caso plano do assinante nao possua o tipo de saldo de SMS, a mensagem nao pode ser enviada.
			PlanoPreco	plano		= MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco());
			TipoSaldo	tipoSaldo	= MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.TORPEDOS);
			if(!plano.possuiSaldo(tipoSaldo))
				return false;
			
			// Intancia mapeamento de tabelas em memória
			super.log(Definicoes.DEBUG,"enviarSMS","Instancia Mapeamento da TBL_GER_CONFIGURACAO_GPP na memoria");
			MapConfiguracaoGPP mapConfig = MapConfiguracaoGPP.getInstancia();
			msgSMSTemplate = mapConfig.getMapDescricaoConfiguracaoGPP("MSG_SMS_RECARGA");

			// Envio do sms informando a recarga no saldo principal
			if(valores.getSaldoPrincipal()!=0)
			{
				texto1 = GPPData.substituiTexto("%1", msgSMSTemplate, conversorTimestamp.format(dataHora));
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto1:" + texto1);
				
				avalorRecarga =  String.valueOf(d.format( valores.getSaldoPrincipal())).replace('.',',');   
				texto2 = GPPData.substituiTexto("%2", texto1, avalorRecarga);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto2:" + texto2);
				   
				asaldoFinal = String.valueOf(d.format(assinante.getCreditosPrincipal() )).replace('.',',');
				texto3 = GPPData.substituiTexto("%3", texto2, asaldoFinal);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto3:" + texto3);
				   
				texto4 = GPPData.substituiTexto("%4", texto3, assinante.getDataExpiracaoPrincipal());
				
				texto5 = GPPData.substituiTexto("%5",texto4,"Principal");
				
				super.log(Definicoes.DEBUG, "enviarSMS", "Msg:" + texto5);
	
				retorno = direcionadorSMS.gravaMensagemSMS(assinante.getMSISDN(),texto5,prioridadeMensagem,Definicoes.SMS_RECARGA,super.getIdLog());
			}
			
			// Envio do sms informando a recarga no saldo periodico
			if(valores.getSaldoPeriodico()!=0)
			{
				texto1 = GPPData.substituiTexto("%1", msgSMSTemplate, conversorTimestamp.format(dataHora));
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto1:" + texto1);
				
				avalorRecarga =  String.valueOf(d.format( valores.getSaldoPeriodico())).replace('.',',');   
				texto2 = GPPData.substituiTexto("%2", texto1, avalorRecarga);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto2:" + texto2);
				   
				asaldoFinal = String.valueOf(d.format(assinante.getCreditosPeriodico() )).replace('.',',');
				texto3 = GPPData.substituiTexto("%3", texto2, asaldoFinal);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto3:" + texto3);
				   
				texto4 = GPPData.substituiTexto("%4", texto3, assinante.getDataExpiracaoPeriodico());
				
				texto5 = GPPData.substituiTexto("%5",texto4,"de Franquia");
				
				super.log(Definicoes.DEBUG, "enviarSMS", "Msg:" + texto5);
	
				retorno = direcionadorSMS.gravaMensagemSMS(assinante.getMSISDN(),texto5,prioridadeMensagem,Definicoes.SMS_RECARGA,super.getIdLog());
			}
			
			// Envio do sms informando a recarga no saldo de bonus
			if(valores.getSaldoBonus() != 0)
			{
				texto1 = GPPData.substituiTexto("%1", msgSMSTemplate, conversorTimestamp.format(dataHora));
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto1:" + texto1);
				
				avalorRecarga =  String.valueOf(d.format( valores.getSaldoBonus())).replace('.',',');   
				texto2 = GPPData.substituiTexto("%2", texto1, avalorRecarga);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto2:" + texto2);
				   
				asaldoFinal = String.valueOf(d.format(assinante.getCreditosBonus() )).replace('.',',');
				texto3 = GPPData.substituiTexto("%3", texto2, asaldoFinal);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto3:" + texto3);
				   
				texto4 = GPPData.substituiTexto("%4", texto3, assinante.getDataExpiracaoBonus());
				
				texto5 = GPPData.substituiTexto("%5",texto4,"de Bonus");
				
				super.log(Definicoes.DEBUG, "enviarSMS", "Msg:" + texto5);

				retorno = retorno && direcionadorSMS.gravaMensagemSMS(assinante.getMSISDN(),texto5,prioridadeMensagem,Definicoes.SMS_RECARGA,super.getIdLog());
			}

			// Envio do sms informando a recarga no saldo de sms
			if(valores.getSaldoSMS() != 0)
			{
				texto1 = GPPData.substituiTexto("%1", msgSMSTemplate, conversorTimestamp.format(dataHora));
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto1:" + texto1);
				
				avalorRecarga =  String.valueOf(d.format( valores.getSaldoSMS())).replace('.',',');   
				texto2 = GPPData.substituiTexto("%2", texto1, avalorRecarga);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto2:" + texto2);
				   
				asaldoFinal = String.valueOf(d.format(assinante.getCreditosSms() )).replace('.',',');
				texto3 = GPPData.substituiTexto("%3", texto2, asaldoFinal);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto3:" + texto3);
				   
				texto4 = GPPData.substituiTexto("%4", texto3, assinante.getDataExpiracaoSms());
				
				texto5 = GPPData.substituiTexto("%5",texto4,"de SMS");
				
				super.log(Definicoes.DEBUG, "enviarSMS", "Msg:" + texto5);

				retorno = retorno && direcionadorSMS.gravaMensagemSMS(assinante.getMSISDN(),texto5,prioridadeMensagem,Definicoes.SMS_RECARGA,super.getIdLog());
			}
			
			// Envio do sms informando a recarga no saldo de gprs
			if(valores.getSaldoGPRS() != 0)
			{
				texto1 = GPPData.substituiTexto("%1", msgSMSTemplate, conversorTimestamp.format(dataHora));
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto1:" + texto1);
				
				avalorRecarga =  String.valueOf(d.format( valores.getSaldoGPRS())).replace('.',',');   
				texto2 = GPPData.substituiTexto("%2", texto1, avalorRecarga);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto2:" + texto2);
				   
				asaldoFinal = String.valueOf(d.format(assinante.getCreditosDados() )).replace('.',',');
				texto3 = GPPData.substituiTexto("%3", texto2, asaldoFinal);
				super.log(Definicoes.DEBUG, "gravaMsg", "Texto3:" + texto3);
				   
				texto4 = GPPData.substituiTexto("%4", texto3, assinante.getDataExpiracaoDados());
				
				texto5 = GPPData.substituiTexto("%5",texto4,"de Dados");
				
				super.log(Definicoes.DEBUG, "enviarSMS", "Msg:" + texto5);

				retorno = direcionadorSMS.gravaMensagemSMS(assinante.getMSISDN(),texto5,prioridadeMensagem,Definicoes.SMS_RECARGA,super.getIdLog());
			}
		}
		catch(GPPInternalErrorException e)
		{
			retorno = false;
		}
		finally
		{
			if(retorno)
				super.log(Definicoes.DEBUG, "enviarSMS", recarga.toString() + " - SMS de notificacao de recarga enviado com sucesso.");
			else
				super.log(Definicoes.WARN , "enviarSMS", recarga.toString() + " - Nao foi possivel enviar o SMS de notificacao de recarga.");
		}
		
		return retorno;
	}	

	/**
	 * Metodo...: constroiXMLRetorno
	 * Descricao: Constrói XML de Retorno da Recarga
	 * @param 	short	codErro		Código de Retorno da Recarga
	 * @return	String				XML com retorno e sua descrição
	 */
	public String constroiXMLRetorno(short codErro)
	{
		DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
		
		super.log(Definicoes.DEBUG,"constroiXMLRetorno","Inicio do constroiXMLRetorno");
		GerarXML geradorXML = new GerarXML("GPPRetornoRecarga");
		geradorXML.adicionaTag("codRetorno", new String(conversorRetorno.format(codErro)));
		geradorXML.adicionaTag("descricao",this.getDescricaoErro(codErro));		
		super.log(Definicoes.DEBUG,"constroiXMLRetorno","Fim do constroiXMLRetorno");
		return geradorXML.getXML();
	}
	
	/***
	 * Metodo...: getDescricaoErro
	 * Descricao: Busca descrição referente ao código de erro no Banco de Dados
	 * @param 	short	codErro	Código do Erro
	 * @return	String			Descrição referente ao código de erro
	 */	
	private String getDescricaoErro(short codErro)
	{
		CodigoRetorno retorno = MapCodigosRetorno.getInstance().getRetorno(codErro);
		
		if(retorno != null)
			return retorno.getDescRetorno();
		
		return null; 
	}

	/**
	 *	Realiza a operacao de recarga na plataforma e insere o registro no banco de dados do GPP. O registro e 
	 *	inserido de forma transacional antes da insercao na plataforma. Caso a insercao tenha resultado OK e realizado 
	 *	o commit. Caso contrario, e realizado o rollback e e inserido registro de erro. No caso de sucesso, o processo
	 *	envia o SMS de notificacao ao assinante. O metodo tem como premissa que o assinante e os parametros da recarga 
	 *	foram previa e devidamente atualizados.
	 *
	 *	@param		parametros				Parametros da recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	protected short executarRecarga(ParametrosRecarga recarga)
	{
		PREPConexao		conexaoPrep		= null;
		TecnomenRecarga	conexaoRecarga	= null;
		short			result			= Definicoes.RET_OPERACAO_OK;
		
		try
		{
			//Obtendo a conexao com o banco de dados para insercao do registro de recarga. A operacao deve ser
			//transactional.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			conexaoPrep.setAutoCommit(false);
			
			//Inserindo o registro de recarga na tabela. Para isto e atualizada a data da recarga na plataforma.
			recarga.setDatRecarga(Calendar.getInstance().getTime());
			RecargaDAO.inserirRecargaGSM(recarga, conexaoPrep);
			
			//Caso o sistema de origem seja revenda varejo, e necessario atualizar o valor utilizado.
			if((recarga.getSistemaOrigem().equalsIgnoreCase(Definicoes.SO_VAREJO)) ||
			   (recarga.getSistemaOrigem().equalsIgnoreCase(Definicoes.SO_DEALER)))
				if(!RecargaDAO.atualizarCreditoCanalVarejo(recarga.getTipoTransacao(), 
														   recarga.getValores().getValorEfetivoPago(),
														   conexaoPrep))
					result = Definicoes.RET_LIMITE_REVENDA_ULTRAPASSADO;
			
			//Executando a operacao de recarga na plataforma.
			if(result == Definicoes.RET_OPERACAO_OK)
			{
				//Obtendo aconexao com o Payment Engine.
				conexaoRecarga = this.gerenteInsercaoCreditos.getTecnomenRecarga(super.logId);
				//Executando a operacao de recarga. Caso seja recarga de franquia, o metodo especifico deve ser
				//invocado. Isto porque a recarga executa a operacao Online Fund Transfer (OFT) da Tecnomen, que nao 
				//permite credito no Saldo Periodico. Alem disso, a operacao Periodic Fund Transfer, especifica para 
				//recarga no Saldo Periodico, atualiza o status do assinante e a data de expiracao do Saldo Principal 
				//se for menor que a data do Saldo Periodico. Por estes motivos sera utilizada a operacao Direct Fund 
				//Transfer (DFT).
				if(recarga.getTipoCredito().equals(Definicoes.TIPO_CREDITO_FRANQUIA))
					result = conexaoRecarga.executarRecargaFranquia(recarga.getAssinante(), recarga.getValores());
				else
					result = conexaoRecarga.executarRecarga(recarga.getAssinante(), recarga.getValores());
			}
			
			//Caso a operacao tenha sido OK, efetuar o commit e atualizar as informacoes do assinante a partir dos
			//valores da recarga. Isto porque, se algum valor no Saldo Principal tenha sido definido, e necessario 
			//atualizar o status do assinante caso a recarga tenha sido de franquia, uma vez que o DFT nao altera 
			//o status de assinante.
			if(result == Definicoes.RET_OPERACAO_OK)
			{
				this.enviarSMS(recarga, Definicoes.SMS_PRIORIDADE_ZERO, recarga.getDatOrigem());
				conexaoPrep.commit();
				
				if((recarga.getValores().getSaldoPrincipal() > 0.0) && 
				   (recarga.getTipoCredito().equals(Definicoes.TIPO_CREDITO_FRANQUIA)))
					new Aprovisionar(super.logId).atualizarStatusAssinante(recarga.getAssinante(), 
																		   (short)Definicoes.STATUS_NORMAL_USER, 
																		   null,
																		   recarga.getOperador());
			}
			else
				conexaoPrep.rollback();
		}
		catch(Exception e)
		{
			try
			{
				conexaoPrep.rollback();
			}
			catch(Exception ignored){}
			super.log(Definicoes.ERRO, "executarRecarga", "MSISDN: " + recarga.getMSISDN() + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			//No caso de recargas com erro, e necessario logar na tabela de recargas nao ok. Para isto o auto-commit 
			//e setado novamente.
			try
			{
				conexaoPrep.setAutoCommit(true);
			}
			catch(Exception ignored){}
			
			//Atualizando os saldos finais no registro de recarga, caso aplicavel. Este passo e necessario uma vez que 
			//o registro e inserido antes da operacao na Tecnomen.
			if((result == Definicoes.RET_OPERACAO_OK) && (!RecargaDAO.atualizarSaldoFinal(recarga, conexaoPrep)))
				super.log(Definicoes.WARN, "executarRecarga", recarga.getMSISDN() + " - Nao foi possivel atualizar os saldos finais no registro de recarga.");
			
			//Liberando as conexoes com o banco de dados e com a Tecnomen.
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
			this.gerenteInsercaoCreditos.liberaConexaoRecarga(conexaoRecarga, super.logId);
		}
		
		return result;
	}
	
	/**
	 *	Realiza a operacao de ajuste na plataforma e insere o registro no banco de dados do GPP. O registro e 
	 *	inserido de forma transacional antes da insercao na plataforma. Caso a insercao tenha resultado OK e realizado 
	 *	o commit. Caso contrario, e realizado o rollback e e inserido registro de erro. O metodo tem como premissa que 
	 *	o assinante e os parametros da recarga foram previa e devidamente atualizados.
	 *
	 *	@param		parametros				Parametros do ajuste.
	 *	@param		inserirCreditos			Indicador para inserir creditos na plataforma Tecnomen.
	 *	@return		Codigo de retorno da operacao.
	 */
	protected short executarAjuste(ParametrosRecarga ajuste, boolean inserirCreditos)
	{
		PREPConexao		 conexaoPrep	 = null;
		TecnomenRecarga	 conexaoRecarga	 = null;
		short			 result			 = Definicoes.RET_OPERACAO_OK;
		
		try
		{
			//Obtendo a conexao com o banco de dados para insercao do registro de recarga. A operacao deve ser
			//transactional.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			conexaoPrep.setAutoCommit(false);
			
			//Inserindo o registro de ajuste na tabela. Para isto, e atualizada a data de insercao do ajuste. Caso nao 
			//seja necessario inserir os creditos na plataforma, o saldo do assinante e atualizado de acordo com os 
			//valores do ajuste.
			if(!inserirCreditos)
				ajuste.getAssinante().atualizarSaldos(ajuste.getValores());
			ajuste.setDatRecarga(Calendar.getInstance().getTime());
			RecargaDAO.inserirRecargaGSM(ajuste, conexaoPrep);
			
			if(inserirCreditos)
			{
				//Obtendo a conexao com o Payment Engine e executando a operacao de ajuste na plataforma.
				conexaoRecarga = this.gerenteInsercaoCreditos.getTecnomenRecarga(super.logId);
				result = conexaoRecarga.executarAjuste(ajuste.getAssinante(), ajuste.getValores());
			}
			
			//Caso a operacao tenha sido OK, efetuar o commit.
			if(result == Definicoes.RET_OPERACAO_OK)
			{
				conexaoPrep.commit();
				
				// Apos a transacao de ajuste de credito, eh necessario
				// atualizar o status do assinante, pois suas datas de
				// expiracao foram tambem atualizadas. Um erro no processo
				// de retorno para o ciclo 2 da regua nao eh impeditivo
				// para o fim do processo de ajuste
				if(ajuste.deveRetornarCicloDois())
					conexaoRecarga.atualizarStatusAssinante(ajuste.getMSISDN(), 
														    (short)Definicoes.STATUS_NORMAL_USER,
														    ajuste.getAssinante().getDataExpPrincipal(), 
														    ajuste.getAssinante().getStatusPeriodico(), 
														    ajuste.getAssinante().getDataExpPeriodico(), 
														    ajuste.getAssinante().getDataExpBonus(), 
														    ajuste.getAssinante().getDataExpTorpedos(), 
														    ajuste.getAssinante().getDataExpDados());				
			}
			else
				conexaoPrep.rollback();
		}
		catch(Exception e)
		{
			try
			{
				conexaoPrep.rollback();
			}
			catch(Exception ignored){}
			super.log(Definicoes.ERRO, "executarAjuste", "MSISDN: " + ajuste.getMSISDN() + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			//No caso de recargas com erro, e necessario logar na tabela de recargas nao ok. Para isto o auto-commit 
			//e setado novamente.
			try
			{
				conexaoPrep.setAutoCommit(true);
			}
			catch(Exception ignored){}
			
			//Atualizando os saldos finais no registro do ajuste, caso aplicavel. Este passo e necessario uma vez que 
			//o registro e inserido antes da operacao na Tecnomen.
			if((result == Definicoes.RET_OPERACAO_OK) && (inserirCreditos) && (!RecargaDAO.atualizarSaldoFinal(ajuste, conexaoPrep)))
				super.log(Definicoes.WARN, "executarAjuste", ajuste.toString() + " - Nao foi possivel atualizar os saldos finais no registro do ajuste.");
			
			//Liberando as conexoes com o banco de dados e com a Tecnomen.
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
			this.gerenteInsercaoCreditos.liberaConexaoRecarga(conexaoRecarga, super.logId);
		}
		
		return result;
	}
	
	/**
	 * Metodo...: insereCreditosTFPP
	 * Descricao: Envia uma recarga/ajuste para o MASC efetuá-lo na Plataforma TFPP
	 * @param String		nroAssinante		Número do terminal Pré-Pago
	 * @param double		vlrBonus			Valor do bônus (ajuste)
	 * @param int			diasExpiracao		Número de Dias de Prorrogação da Expiração
	 * @param String		idRecarga			Identificador do Bônus/ajuste
	 * @return	DadosRecarga	Dados referentes ao assinante após a Recarga/ajuste
	 */
	public DadosRecarga insereCreditosTFPP(String nroAssinante, double vlrBonus, int diasExpiracao, String idRecarga) throws Exception
	{
		DadosRecarga retorno = null;
		short retornoMASC = Definicoes.RET_ERRO_GENERICO_MASC;
		try
		{
			ConexaoMASC conexaoMASC = new ConexaoMASC (super.getIdLog());
			
			// Persiste na recarga se ela retornar erro técnico
			MapConfiguracaoGPP mapConfig = MapConfiguracaoGPP.getInstancia();
			int numTentativas = Integer.parseInt(mapConfig.getMapValorConfiguracaoGPP("NUM_TENTATIVAS_RECARGA_MASC"));
			while (retornoMASC == Definicoes.RET_ERRO_GENERICO_MASC && numTentativas>0)
			{
				retorno = conexaoMASC.enviaRecargaMASC(nroAssinante, vlrBonus, diasExpiracao, idRecarga);
				retornoMASC = (short)retorno.getCodigoErro();
				numTentativas--;
			}
			if(retornoMASC == Definicoes.RET_ERRO_TIMEOUT_MASC)
			{
				return retorno;
			}
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"insereCreditosTFPP", "Erro na Insercao de Creditos via MASC: "+gppE);
			throw new Exception("Erro interno do GPP: " + gppE);
		}
		
		return retorno;
	}
	
}