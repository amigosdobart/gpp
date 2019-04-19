package com.brt.gpp.aplicacoes.recarregar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerenteProdutorRecarga;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *
 *	Esta classe e a responsavel por realizar o processo de recargas e envio de SMS vinculados.
 *	Esta classe e uma thread que fica pesquisando no pool de recargas disponiveis uma recarga
 *	a ser processada. Caso nenhuma recarga seja encontrada entao a thread e interrompida
 *
 *	<P> Versao:        	1.0
 *
 *	@Autor:            	Joao Carlos
 *	Data:               07/12/2004
 *
 *	Modificado por:		Daniel Ferreira
 *	Data:				20/09/2006
 *	Razao:				Implementar controle de status de recargas na fila.
 *
 */
public class ConsumoRecarga extends Thread
{

	/**
	 *	Gerente de Pool de log.
	 */
	private	GerentePoolLog gerPoolLog;

	/**
	 *	Identificador de log do processo.
	 */
	private	long idProcesso;

	/**
	 *	API de consulta de assinante na plataforma Tecnomen.
	 */
	private	ConsultaAssinante consulta;

	/**
	 *	API de ajuste de saldos na plataforma Tecnomen.
	 */
	private	Ajustar ajuste;
	/**
	 *	API de recarga da plataforma Tecnomen
	 */
	private	Recarregar recarga;
	/**
	 *	Gerente produtor de registros da Fila de Recargas para consumo.
	 */
	private	GerenteProdutorRecarga produtor;

	/**
	 *	Consumidor da Fila de SMS. Utilizado para insercao de registros na fila.
	 */
	private	ConsumidorSMS consumidor;

	/**
	 * Metodo....:ThreadConsumoRecarga
	 * Descricao.:Inicializacao (construtor) da classe
	 *
	 */
	public ConsumoRecarga()
	{
		this.gerPoolLog = GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso = this.gerPoolLog.getIdProcesso(Definicoes.CN_RECARGA);
	}

	/**
	 * Metodo....:notificaConsumo
	 * Descricao.:Acorda a thread para consumo ou seja para continuar seu processamento
	 *
	 */
	public synchronized void notificaConsumo()
	{
		notify();
	}

	/**
	 * Metodo....:consomeRecargas
	 * Descricao.:Este metodo e responsavel por consumir as recargas disponiveis
	 *            no pool do produtor de recargas
	 */
	private void consumirRecargas()
	{
		FilaRecargas	recarga		= null;
		PREPConexao		conexaoPrep	= null;

		try
		{
			//Obtendo conexao com o banco de dados.
			conexaoPrep = this.produtor.getConexao();

			//Enquanto ainda existir recarga a ser processada no pool de dados do produtor entao a thread e executada.
			//Caso nenhuma recarga seja encontrada entao o processo e terminado.
			while((recarga = this.produtor.getProximaRecarga()) != null)
			{
				try
				{
					this.gerPoolLog.log(this.idProcesso, Definicoes.INFO, Definicoes.CL_CONSUMO_RECARGA,
							            "consumirRecargas", "Processando Recarga: " + recarga);

					//Executando consulta de assinante. E necessario devido a possibilidade de limpeza do saldo de
					//bonus. Mesmo nao sendo necessario, o objeto pode ser passado na chamada do metodo de ajuste de
					//concessao de bonus.
					//Metodo anterior: this.consulta.executaConsultaSimplesAssinanteTecnomen(recarga.getIdtMsisdn());
                    Assinante assinante = this.consulta.consultarAssinantePlataforma(recarga.getIdtMsisdn());
                    
					if((assinante != null) && (assinante.getRetorno() == Definicoes.RET_OPERACAO_OK))
					{
						if(recarga.getIdtStatusProcessamento().intValue() == Definicoes.STATUS_RECARGA_EM_PROCESSAMENTO)
							this.zerarSaldos(recarga, assinante, conexaoPrep);
						if((recarga.getIdtStatusProcessamento().intValue() == Definicoes.STATUS_RECARGA_EM_PROCESSAMENTO) ||
						   (recarga.getIdtStatusProcessamento().intValue() == Definicoes.STATUS_RECARGA_ZERAR_SALDOS_CONCLUIDO))
						{
							if("R".equalsIgnoreCase(recarga.getTipOperacao()))
								this.executarRecarga(recarga, assinante, conexaoPrep);
							else
								this.executarAjuste(recarga, assinante, conexaoPrep);
						}
						if(recarga.getIdtStatusProcessamento().intValue() == Definicoes.STATUS_RECARGA_CONCLUIDA)
							this.enviarSms(recarga, conexaoPrep);
					}
					else
					{
						recarga.setIdtCodigoRetorno(new Integer((assinante != null) ? assinante.getRetorno() : Definicoes.RET_MSISDN_NAO_ATIVO));
						this.tratarErro(recarga, conexaoPrep);
					}
				}
				catch(Exception e)
				{
					this.gerPoolLog.log(this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONSUMO_RECARGA,
							            "consumirRecargas", "Recarga: " + recarga + " - Excecao: " + e);
					recarga.setIdtCodigoRetorno(new Integer(Definicoes.RET_ERRO_TECNICO));
					this.tratarErro(recarga, conexaoPrep);
				}
				finally
				{
					//Notifica o termino do consumo da recarga.
					this.produtor.notificarTermino(recarga);
				}
			}
		}
		catch(Exception e)
		{
			this.gerPoolLog.log(this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONSUMO_RECARGA, "consumirRecargas",
					            "Excecao: " + e);
		}
	}

	/**
	 *	Zera os saldos do assinante, conforme a necessidade.
	 *
	 *	@param		recarga					Informacoes referentes a recarga a ser consumida.
	 *	@param		assinante 				Informacoes do assinante na plataforma.
	 *	@param		conexaoPrep				Conexao com banco de dados.
	 *	@return		Codigo de retorno da operacao.
	 *	@throws 	Exception
	 */
	private int zerarSaldos(FilaRecargas recarga, Assinante assinante, PREPConexao conexaoPrep) throws Exception
	{
		int result = Definicoes.RET_OPERACAO_OK;

		//Verificando se e necessario zerar algum saldo do assinante. Caso seja necessario, sera feita uma
		//consulta do assinante para buscar seu saldo e um ajuste de debito neste saldo com o
		//valor obtido na consulta.
		if (((recarga.zerarSaldoBonus()     && assinante.getCreditosBonus()     > 0) ||
			 (recarga.zerarSaldoSms  ()     && assinante.getCreditosSms  ()     > 0) ||
			 (recarga.zerarSaldoGprs ()     && assinante.getCreditosDados()     > 0) ||
             (recarga.zerarSaldoPeriodico() && assinante.getCreditosPeriodico() > 0) ))
		{
			//Cria objeto para conter a configuracao do ajuste de debito em multiplos saldos
			ValoresRecarga debito = new ValoresRecarga();
			debito.setSaldoPrincipal(0.0);
			debito.setSaldoBonus((recarga.zerarSaldoBonus()) ? assinante.getCreditosBonus() : 0.0);
			debito.setSaldoSMS  ((recarga.zerarSaldoSms  ()) ? assinante.getCreditosSms  () : 0.0);
			debito.setSaldoGPRS ((recarga.zerarSaldoGprs ()) ? assinante.getCreditosDados() : 0.0);
            debito.setSaldoPeriodico ((recarga.zerarSaldoPeriodico ()) ? assinante.getCreditosPeriodico() : 0.0);

			//Obtendo a data/hora da retirada, que corresponde a um segundo antes da concessao do ajuste.
			Calendar calRetirada = Calendar.getInstance();
			calRetirada.setTime(recarga.getDatProcessamento());
			calRetirada.add(Calendar.SECOND, -1);

			result = this.ajuste.executarAjuste(recarga.getIdtMsisdn(),
					 		     				Definicoes.AJUSTE_DEBITO_EXPIRACAO_BONUS,
												Definicoes.TIPO_CREDITO_REAIS,
												debito,
												Definicoes.TIPO_AJUSTE_DEBITO,
												calRetirada.getTime(),
												Definicoes.SO_GPP,
												"ConsumidorRecarga",
												assinante,
												null,
												true,
                                                null);
			recarga.setIdtCodigoRetorno(new Integer(result));

			// Se executou o debito entao o status desta fica como Status Zerar Bonus OK
			// caso algum erro aconteceu na execucao entao o status indica Recarga com erro
			if (result == Definicoes.RET_OPERACAO_OK)
			{
				recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_ZERAR_SALDOS_CONCLUIDO));
				if(recarga.zerarSaldoBonus())
					assinante.setSaldoCreditosBonus(0.0);
				if(recarga.zerarSaldoSms())
					assinante.setSaldoCreditosSMS(0.0);
				if(recarga.zerarSaldoGprs())
					assinante.setSaldoCreditosDados(0.0);
                if(recarga.zerarSaldoPeriodico())
                    assinante.setSaldoCreditosPeriodico(0.0);
				this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
			}
			else
				this.tratarErro(recarga, conexaoPrep);
		}
		else
			recarga.setIdtCodigoRetorno(new Integer(result));

		return result;
	}

	/**
	 *	Realiza o ajuste definido no registro da fila de recargas.
	 *
	 *	@param		recarga					Informacoes referentes a recarga a ser consumida.
	 *	@param		assinante 				Informacoes do assinante na plataforma.
	 *	@param		conexaoPrep				Conexao com banco de dados.
	 *	@return		Codigo de retorno da operacao.
	 *	@throws 	Exception
	 */
	private int executarAjuste(FilaRecargas recarga, Assinante assinante, PREPConexao conexaoPrep) throws Exception
	{
		int result = Definicoes.RET_OPERACAO_OK;

		//Verificando se o valor de pelo menos um dos saldos e maior que 0. Caso contrario, nao e necessario realizar
		//a recarga, nem enviar SMS.
		if((recarga.getVlrCreditoPrincipal().doubleValue() != 0.0) || 
		   (recarga.getVlrCreditoPeriodico().doubleValue() != 0.0) ||
		   (recarga.getVlrCreditoBonus    ().doubleValue() != 0.0) ||
		   (recarga.getVlrCreditoSms      ().doubleValue() != 0.0) || 
		   (recarga.getVlrCreditoGprs     ().doubleValue() != 0.0))
		{
			// Cria objeto para conter a configuracao da recarga em multiplos saldos
			ValoresRecarga creditos = new ValoresRecarga();
			creditos.setSaldoPrincipal	(recarga.getVlrCreditoPrincipal().doubleValue());
			creditos.setSaldoPeriodico	(recarga.getVlrCreditoPeriodico().doubleValue());
			creditos.setSaldoBonus    	(recarga.getVlrCreditoBonus    ().doubleValue());
			creditos.setSaldoSMS		(recarga.getVlrCreditoSms      ().doubleValue());
			creditos.setSaldoGPRS  		(recarga.getVlrCreditoGprs     ().doubleValue());
			creditos.setDataExpPrincipal(assinante.newDataExpiracao(TipoSaldo.PRINCIPAL, recarga.getNumDiasExpPrincipal().shortValue()));
			creditos.setDataExpPeriodico(assinante.newDataExpiracao(TipoSaldo.PERIODICO, recarga.getNumDiasExpPeriodico().shortValue()));
			creditos.setDataExpBonus    (assinante.newDataExpiracao(TipoSaldo.BONUS    , recarga.getNumDiasExpBonus    ().shortValue()));
			creditos.setDataExpSMS      (assinante.newDataExpiracao(TipoSaldo.TORPEDOS , recarga.getNumDiasExpSms      ().shortValue()));
			creditos.setDataExpGPRS     (assinante.newDataExpiracao(TipoSaldo.DADOS    , recarga.getNumDiasExpGprs     ().shortValue()));

			MapRecOrigem mapOrigem = MapRecOrigem.getInstancia();
            String tipAjuste = mapOrigem.getMapTipLancamentoRecOrigem(recarga.getTipTransacao());

			result = this.ajuste.executarAjuste(recarga.getIdtMsisdn(),
												recarga.getTipTransacao(),
												Definicoes.TIPO_CREDITO_REAIS,
												creditos,
												tipAjuste,
												new Date(),
												Definicoes.SO_GPP,
												"ConsumidorRecarga",
												assinante,
												null,
												true,
                                                null);
			recarga.setIdtCodigoRetorno(new Integer(result));

			//Se executou a recarga entao atualizar o status para recarga concluida. Caso algum erro aconteceu na
			//execucao entao este erro deve ser tratado.
			if (result == Definicoes.RET_OPERACAO_OK)
			{
				recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_CONCLUIDA));
				this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
			}
			else
				this.tratarErro(recarga, conexaoPrep);
		}
		else
		{
			recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS));
			recarga.setIdtCodigoRetorno(new Integer(Definicoes.RET_OPERACAO_OK));
			this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
		}

		return result;
	}

	/**
	 * Realiza recarga na plataforma Tecnomen
	 *
	 * @param recarga	Parametros da Recarga
	 * @return
	 * @throws ParseException
	 */
	private int executarRecarga(FilaRecargas recarga, Assinante assinante, PREPConexao conexaoPrep) throws Exception
	{
		this.recarga = new Recarregar(this.idProcesso);
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		// Evitar NullPointerException
		if(recarga.getDatContabil() == null)
			recarga.setDatContabil("");
		int result = this.recarga.executarRecarga(recarga.getIdtMsisdn(),
											recarga.getTipTransacao(),
											recarga.getIdtRecarga(),
											Definicoes.TIPO_CREDITO_REAIS,
											recarga.getVlrCreditoPrincipal().doubleValue(),
											recarga.getDatProcessamento(),
											Definicoes.SO_GPP,
											"ConsumidorRecarga",
											recarga.getIdtNsuInstituicao(),
											recarga.getNumHashCC(),
											recarga.getIdtCpf(),
											recarga.getIdtTerminal(),
											recarga.getTipTerminal(),
											sdf.parse(recarga.getDatContabil()),
                                            assinante);
		recarga.setIdtCodigoRetorno(new Integer(result));

		//Se executou a recarga entao atualizar o status para recarga concluida. Caso algum erro aconteceu na
		//execucao entao este erro deve ser tratado.
		if (result == Definicoes.RET_OPERACAO_OK)
		{
			recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_CONCLUIDA));
			this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
		}
		else
			this.tratarErro(recarga, conexaoPrep);

		return result;
	}
	/**
	 *	Envia o SMS de notificacao da recarga, conforme a necessidade.
	 *
	 *	@param		recarga					Informacoes referentes a recarga a ser consumida.
	 *	@param		conexaoPrep				Conexao com banco de dados.
	 *	@return		Codigo de retorno da operacao.
	 *	@throws 	Exception
	 */
	private int enviarSms(FilaRecargas recarga, PREPConexao conexaoPrep) throws Exception
	{
		int result = Definicoes.RET_OPERACAO_OK;

		//Se e necessario enviar SMS entao tenta incluir a mensagem. Caso positivo o status fica como concluido. Senao
		//o erro deve ser tratado.
		if(recarga.getIndEnviaSms().intValue() == 1)
		{
			if(this.consumidor.gravarMensagemSMS(recarga.getIdtMsisdn(),
								  		    	 recarga.getDesMensagem(),
												 recarga.getNumPrioridade().intValue(),
												 recarga.getTipSms(),
												 conexaoPrep))
			{
				recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS));
				recarga.setIdtCodigoRetorno(new Integer(Definicoes.RET_OPERACAO_OK));
				this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
			}
			else
			{
				recarga.setIdtCodigoRetorno(new Integer(Definicoes.RET_ERRO_TECNICO));
				this.tratarErro(recarga, conexaoPrep);
			}
		}
		else
		{
			recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS));
			recarga.setIdtCodigoRetorno(new Integer(Definicoes.RET_OPERACAO_OK));
			this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
		}

		return result;
	}

	/**
	 * Realiza o tratamento de erros no consumo da recarga. O fluxo do tratamento de excecao segue:<br>
	 * Se o status de processamento = 1 <code>--&gt;</code> atualizar o status para 9;<br>
	 * Se o status de processamento = 5 <code>--&gt;</code> manter em 5 para reprocessamento;<br>
	 * Se o status de processamento = 2 <code>--&gt;</code> manter em 2 para reprocessamento;<br>
	 * Atualizar as informacoes de status e codigo de retorno no banco de dados.<br>
	 *
	 * @param		recarga			Informacoes referentes a recarga a ser consumida.
	 * @param		conexaoPrep		Conexao com banco de dados.
	 */
	private void tratarErro(FilaRecargas recarga, PREPConexao conexaoPrep)
	{
		try
		{
			if((recarga.getIdtStatusProcessamento().intValue() == Definicoes.STATUS_RECARGA_EM_PROCESSAMENTO)
			   ||    (recarga.getIdtCodigoRetorno().intValue() == Definicoes.RET_MSISDN_NAO_ATIVO))
				recarga.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_COM_ERRO));

			this.produtor.atualizarStatusRecarga(recarga, conexaoPrep);
		}
		catch(Exception e)
		{
			this.gerPoolLog.log(this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONSUMO_RECARGA, "tratarErro",
					            "Recarga: " + recarga + " - Excecao: " + e);
		}
	}

	/**
	 * Metodo....:run
	 * Descricao.:Este metodo realiza o controle do processamento de consumo da recarga
	 * @see run
	 */
	public void run()
	{
		this.gerPoolLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_CONSUMO_RECARGA,"run","Inicio");
		try
		{
			//Cria uma referencia para o mapeamento de configuracao do GPP.
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			//Cria uma referencia para realizar consulta do saldo do assinante.
			this.consulta = new ConsultaAssinante(this.idProcesso);
			//Cria uma referencia para realizar os ajustes.
			this.ajuste = new Ajustar(this.idProcesso);
			//Obtendo a instancia do produtor da Fila de Recargas.
			this.produtor = GerenteProdutorRecarga.getInstancia();
			//Obtendo a instancia do consumidor da Fila de SMS.
			this.consumidor = ConsumidorSMS.getInstance(this.idProcesso);
			//Define o tempo de espera da thread.
			int tempoEspera = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("THREAD_RECARGA_TEMPO_ESPERA"));

			//Fica em um loop infinito sempre buscando a proxima recarga no pool do produtor de
			//recargas. Quando todas as recargas forem processadas entao esta thread "dorme" e
			//volta a tentar novamente identificar se ha recargas a serem executadas.
			while(true)
			{
				//Consome as recargas disponiveis
				this.consumirRecargas();
				// Dorme alguns segundos ate tentar novamente encontrar recarga a ser realizada
				synchronized(this)
				{
					wait(tempoEspera*1000);
				}
			}
		}
		catch(GPPInternalErrorException ge)
		{
			this.gerPoolLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_CONSUMO_RECARGA,"run","Erro ao buscar instancia das configuracoes GPP");
		}
		catch(InterruptedException ie)
		{
			this.gerPoolLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_CONSUMO_RECARGA,"run","Thread de consumo foi interrompida.");
		}
		this.gerPoolLog.log(this.idProcesso,Definicoes.DEBUG,Definicoes.CL_CONSUMO_RECARGA,"run","Fim");
	}
}
