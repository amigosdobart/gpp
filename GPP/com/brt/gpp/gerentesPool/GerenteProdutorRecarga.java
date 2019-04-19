package com.brt.gpp.gerentesPool;

import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargasDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerenteConsumidorRecarga;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 *	Esta classe e um daemon (thread) que ira realizar a leitura dos dados em tabela da fila
 *	de recargas a serem processadas armazenando os registros em memoria (pool) para serem processados
 *	de forma paralela por varias threads de consumo destas recargas. Assim que uma recarga e armazenada
 *	no pool as threads de consumo podem "pega-las" para processamento sendo de responsabilidade desta
 *	classe indicar quais registros estao em processamento e tambem de acertar o status do registro fisicamente
 *	na tabela apos concluido o processo.
 *
 *	<P> Versao:        	1.0
 *
 *	@Autor:            	Joao Carlos
 *	Data:               07/12/2004
 *
 *	Modificado por:
 *	Data:
 *	Razao:
 *
 */

public class GerenteProdutorRecarga extends Thread
{

	/**
	 *	Instancia do Produtor de Recargas (singleton).
	 */
	private static GerenteProdutorRecarga instancia;

	/**
	 *	Instancia do Gerente de Pool de Log para operacoes de log.
	 */
	private GerentePoolLog gerenteLog;

	/**
	 *	Identificador de log do processo.
	 */
	private long idProcesso;

	/**
	 *	Conexao com o banco de dados.
	 */
	private PREPConexao conexaoPrep;

	/**
	 *	Lista de Result Sets contendo os registros de recargas.
	 */
	private LinkedHashMap listaRecargas;

	/**
	 *	Lista de recargas em processamento pelas threads consumidoras.
	 */
	private HashMap recargasAtivas;

	/**
	 *	Lista de status de recargas que podem ser processados.
	 */
	private String[] listaStatus;

	/**
	 *	Mutex para sincronizacao de informacoes compartilhadas entre produtor e consumidores.
	 */
	private Object token;

	/**
	 * Metodo....:GerenteProdutorRecarga
	 * Descricao.:Construtor (inicializador da thread)
	 */
	private GerenteProdutorRecarga()
	{
		this.gerenteLog		= GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso		= this.gerenteLog.getIdProcesso(Definicoes.CN_RECARGA);
		this.listaRecargas	= new LinkedHashMap();
		this.recargasAtivas = new HashMap();
		this.conexaoPrep	= null;
		this.token		  	= new Object();

		this.gerenteLog.log(this.idProcesso,Definicoes.INFO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"GerenteProdutorRecarga","Inicializado o Produtor da Fila de Recargas.");

		//Inicializa o processamento.
		start();
	}

	/**
	 * Metodo....:getInstancia
	 * Descricao.:Retorna uma instancia do gerente produtor de recarga e SMS
	 *            a serem processados
	 * @return GerenteProdutorRecarga	- Instancia da classe
	 */
	public static GerenteProdutorRecarga getInstancia()
	{
		GerentePoolLog gerenteLog = GerentePoolLog.getInstancia(GerenteProdutorRecarga.class);
		gerenteLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"GerenteProdutorRecarga","GET_INSTANCIA");
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		// Se o flag indicando que o sistema iniciado nao deve processar a fila de
		// recargas entao a instancia nao e inicializada

		if (arqConf.deveProcessarFilaRecarga())
		{
			if (instancia == null)
				instancia = new GerenteProdutorRecarga();
		}
		else
		{
			gerenteLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"GerenteProdutorRecarga","O Produtor da Fila de Recargas Nao sera Inicializado.");
		}

		return instancia;
	}

	/**
	 * Metodo....:getProximaRecarga
	 * Descricao.:Este metodo retorna a proxima recarga a ser processada. O metodo faz o controle
	 *            das recargas em processamento retirando essa do pool de disponiveis e armazenando
	 *            em uma outra lista indicando tal processamento
	 * @return DadosRecarga - Informacoes da recarga a ser processada
	 * @throws GPPInternalErrorException
	 */
	public synchronized FilaRecargas getProximaRecarga()
	{
		FilaRecargas	filaRecargas	= null;
		ResultSet		registros		= null;

		synchronized(this.token)
		{
			for(Iterator iterator = this.listaRecargas.values().iterator(); iterator.hasNext();)
			{
				registros = (ResultSet)iterator.next();

				try
				{
					// Caso o ResultSet esteja populado e disponivel entao busca as informacoes da linha populando
					// em um objeto de recarga a ser devolvido para a thread de consumo que requisitou. Caso o result
					// esteja nulo ou nao mais possui registros entao e devolvido um objeto vazio para a thread de
					// consumo e novos registros sao pesquisados para as proximas requisicoes
					if(registros.next())
					{
						// Cria o objeto que ira conter os valores da recarga.
						filaRecargas = new FilaRecargas();

                        if (registros.getObject("ID_REGISTRO") != null)
                            filaRecargas.setIdRegistro(new Integer(registros.getInt("ID_REGISTRO")));
                        
                        if (registros.getObject("ID_REGISTRO_DEPENDENCIA") != null)
                            filaRecargas.setIdRegistroDependencia(new Integer(registros.getInt("ID_REGISTRO_DEPENDENCIA")));                        
                        
						filaRecargas.setRowId(registros.getString("ROWID"));
						filaRecargas.setTipOperacao(registros.getString("TIP_OPERACAO"));
						filaRecargas.setIdtMsisdn(registros.getString("IDT_MSISDN"));
						filaRecargas.setTipTransacao(registros.getString("TIP_TRANSACAO"));

						filaRecargas.setDatCadastro(new Timestamp(registros.getDate("DAT_CADASTRO").getTime()));
						filaRecargas.setDatExecucao(new Timestamp(registros.getDate("DAT_EXECUCAO").getTime()));
						filaRecargas.setDatProcessamento(new Timestamp(new Date().getTime()));

						filaRecargas.setVlrCreditoPrincipal(new Double(registros.getDouble("VLR_CREDITO_PRINCIPAL")));
						filaRecargas.setVlrCreditoBonus(new Double(registros.getDouble("VLR_CREDITO_BONUS")));
						filaRecargas.setVlrCreditoSms(new Double(registros.getDouble("VLR_CREDITO_SMS")));
						filaRecargas.setVlrCreditoGprs(new Double(registros.getDouble("VLR_CREDITO_GPRS")));
						filaRecargas.setVlrCreditoPeriodico(new Double(registros.getDouble("VLR_CREDITO_PERIODICO")));

						filaRecargas.setNumDiasExpPrincipal(new Integer(registros.getInt("NUM_DIAS_EXP_PRINCIPAL")));
						filaRecargas.setNumDiasExpBonus(new Integer(registros.getInt("NUM_DIAS_EXP_BONUS")));
						filaRecargas.setNumDiasExpSms(new Integer(registros.getInt("NUM_DIAS_EXP_SMS")));
						filaRecargas.setNumDiasExpGprs(new Integer(registros.getInt("NUM_DIAS_EXP_GPRS")));
						filaRecargas.setNumDiasExpPeriodico(new Integer(registros.getInt("NUM_DIAS_EXP_PERIODICO")));

						filaRecargas.setDesMensagem(registros.getString("DES_MENSAGEM"));
						filaRecargas.setTipSms(registros.getString("TIP_SMS"));
						filaRecargas.setIndEnviaSms(new Integer(registros.getInt("IND_ENVIA_SMS")));
						filaRecargas.setNumPrioridade(new Integer(registros.getInt("NUM_PRIORIDADE")));

						filaRecargas.setIdtStatusProcessamento(new Integer(registros.getInt("IDT_STATUS_PROCESSAMENTO")));
						filaRecargas.setIdtCodigoRetorno(new Integer(Definicoes.RET_OPERACAO_OK));
						filaRecargas.setIndZerarSaldoPeriodico(new Integer(registros.getInt("IND_ZERAR_SALDO_PERIODICO")));
						filaRecargas.setIndZerarSaldoBonus(new Integer(registros.getInt("IND_ZERAR_SALDO_BONUS")));
						filaRecargas.setIndZerarSaldoSms(new Integer(registros.getInt("IND_ZERAR_SALDO_SMS")));
						filaRecargas.setIndZerarSaldoGprs(new Integer(registros.getInt("IND_ZERAR_SALDO_GPRS")));

						filaRecargas.setIdtRecarga(registros.getString("IDT_RECARGA"));
						filaRecargas.setIdtNsuInstituicao(registros.getString("IDT_NSU_INSTITUICAO"));
						filaRecargas.setDatContabil(registros.getString("DAT_CONTABIL"));
						filaRecargas.setNumHashCC(registros.getString("NUM_HASH_CC"));
						filaRecargas.setIdtCpf(registros.getString("IDT_CPF"));
						filaRecargas.setIdtTerminal(registros.getString("IDT_TERMINAL"));
						filaRecargas.setTipTerminal(registros.getString("TIP_TERMINAL"));

						/*
						dadosRecarga.setMSISDN              (registros.getString("IDT_MSISDN")                     				);
						dadosRecarga.setTipoTransacao       (registros.getString("TIP_TRANSACAO")                  				);
						dadosRecarga.setValorPrincipal      (registros.getDouble("VLR_CREDITO_PRINCIPAL")          				);
						dadosRecarga.setValorBonus          (registros.getDouble("VLR_CREDITO_BONUS") 							);
						dadosRecarga.setValorSm             (registros.getDouble("VLR_CREDITO_SMS") 							);
						dadosRecarga.setValorDados          (registros.getDouble("VLR_CREDITO_GPRS") 							);
						dadosRecarga.setNumDiasExpPrincipal (registros.getInt("NUM_DIAS_EXP_PRINCIPAL") 						);
						dadosRecarga.setNumDiasExpBonus     (registros.getInt("NUM_DIAS_EXP_BONUS") 							);
						dadosRecarga.setNumDiasExpDados     (registros.getInt("NUM_DIAS_EXP_GPRS") 								);
						dadosRecarga.setNumDiasExpSm        (registros.getInt("NUM_DIAS_EXP_SMS") 								);
						dadosRecarga.setDataHora            (this.sdf.format(Calendar.getInstance().getTime()) 					);
						dadosRecarga.setOperador            ("ConsumidorRecarga"                                       			);
						dadosRecarga.setSistemaOrigem       (Definicoes.SO_GPP                                         			);
						dadosRecarga.setTipoCredito         (Definicoes.TIPO_CREDITO_REAIS                             			);
						dadosRecarga.setPrioridade          (registros.getInt("NUM_PRIORIDADE")                             	);
						dadosRecarga.setMensagem            (registros.getString("DES_MENSAGEM")                   				);
						dadosRecarga.setTipoSMS             (registros.getString("TIP_SMS")                        				);
						dadosRecarga.setEnviaSMS            (registros.getInt("IND_ENVIA_SMS") 				== 1 ? true : false	);
						dadosRecarga.setZerarSaldoBonus		(registros.getInt("IND_ZERAR_SALDO_BONUS")		== 1 ? true : false );
						dadosRecarga.setZerarSaldoSms		(registros.getInt("IND_ZERAR_SALDO_SMS")		== 1 ? true : false );
						dadosRecarga.setZerarSaldoGprs		(registros.getInt("IND_ZERAR_SALDO_GPRS")		== 1 ? true : false );
						dadosRecarga.setStatusRecarga       (registros.getInt("IDT_STATUS_PROCESSAMENTO")   					);
						dadosRecarga.setCodigoErro          (Definicoes.RET_OPERACAO_OK                                			);
						dadosRecarga.setIdentificacaoRecarga(registros.getString("ROWID")                          				);
						*/

						// Atualiza o status para em processamento somente se o status for de recarga nao processada
						if(filaRecargas.getIdtStatusProcessamento() == null ||
						   filaRecargas.getIdtStatusProcessamento().intValue() == Definicoes.STATUS_RECARGA_NAO_PROCESSADA)
						{
							filaRecargas.setIdtStatusProcessamento(new Integer(Definicoes.STATUS_RECARGA_EM_PROCESSAMENTO));
							this.atualizarStatusRecarga(filaRecargas, this.conexaoPrep);
						}

						//Insere a recarga na lista de recargas ativas.
						this.recargasAtivas.put(filaRecargas.getRowId(), filaRecargas);

						return filaRecargas;
					}

					//Uma vez que nao ha mais registros para serem processados no status de processamento atual, fecha
					//o ResultSet para que na proxima execucao os registros na fila sejam processados.
					registros.close();
					registros = null;
					iterator.remove();
				}
				catch(Exception e)
				{
					//Em caso de excecao e necessario parar o consumo. Desta forma nenhuma recarga e retornada e a excecao
					//e logada.
					this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,
							            "getProximaRecarga","Excecao :" + e);

					//Fechando o result set.
					if(registros != null)
					{
						try
						{
							registros.close();
						}
						catch(Exception e1)
						{
							this.gerenteLog.log(this.idProcesso, Definicoes.WARN, Definicoes.CL_GERENTE_PRODUTOR_RECARGA,
						                        "getProximaRecarga","Nao foi possivel fechar o Result Set.");
						}

						registros = null;
						iterator.remove();
					}
				}
			}
		}

		return null;
	}

	/**
	 *	Este metodo atualiza o status da recarga na fila de recargas trocando seu status para indicar que o processo
	 *	ja executou a recarga e se necessario ja enviou o SMS.
	 *
	 *	@param		recarga						Dados da Recarga
	 *	@param		conexaoPrep					Conexao com o banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public void atualizarStatusRecarga(FilaRecargas recarga) throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;

		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
			this.atualizarStatusRecarga(recarga, conexaoPrep);
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(conexaoPrep, this.idProcesso);
		}
	}

	/**
	 *	Este metodo atualiza o status da recarga na fila de recargas trocando seu status para indicar que o processo
	 *	ja executou a recarga e se necessario ja enviou o SMS.
	 *
	 *	@param		recarga						Dados da Recarga
	 *	@param		conexaoPrep					Conexao com o banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public void atualizarStatusRecarga(FilaRecargas filaRecargas, PREPConexao conexaoPrep) throws Exception
	{
		String sqlUpdate = "UPDATE TBL_REC_FILA_RECARGAS " +
		                   "SET IDT_STATUS_PROCESSAMENTO = ? ," +
						   "    IDT_CODIGO_RETORNO       = ? ," +
						   "    DAT_PROCESSAMENTO        = ?  " +
						   "WHERE ROWID = ? ";
		// Se o status da recarga indicar processamento ou erro entao a data de processamento e
		// atualizada com a data atual senao ela fica nula
		Timestamp dataProcessamento = null;
		if (filaRecargas.getIdtStatusProcessamento().intValue() >= Definicoes.STATUS_RECARGA_CONCLUIDA)
			dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());

		Object param[] = {filaRecargas.getIdtStatusProcessamento(),
				          filaRecargas.getIdtCodigoRetorno(),
				          dataProcessamento,
						  filaRecargas.getRowId()
						 };

		// Atualiza o status do registro da recarga para indicar que esta ja foi processada.
		conexaoPrep.executaPreparedUpdate(sqlUpdate,param,this.idProcesso);
	}
	
	/**
	 *	Busca pelos proximos registros a serem processados.
	 */
	private void buscarProximosRegistros()
	{
		synchronized(this.token)
		{
            if((this.listaRecargas.isEmpty()) && (this.recargasAtivas.isEmpty()))
			{
				this.gerenteLog.log(this.idProcesso, Definicoes.INFO, Definicoes.CL_GERENTE_PRODUTOR_RECARGA,
			                        "buscarProximosRegistros", "Lista de Result Sets vazia. Buscando por registros para processamento.");

				//Caso a lista esteja vazia, e permitido buscar os Result Sets de registros para todos os status que
				//podem ser processados.
				for(int i = 0; (this.listaStatus != null) && (i < this.listaStatus.length); i++)
				{
					String statusRecarga = this.listaStatus[i];
					ResultSet registros = this.buscarProximosRegistros(statusRecarga);
                    if (registros != null)
                        this.listaRecargas.put(statusRecarga, registros);
				}
			}
			else
			{
				this.gerenteLog.log(this.idProcesso, Definicoes.INFO, Definicoes.CL_GERENTE_PRODUTOR_RECARGA,
                                    "buscarProximosRegistros", "Buscando por registros nao processados.");

				//Caso a lista nao esteja ainda vazia, buscar por registros com status de recarga nao processada, caso
				//o Result Set esteja vazio. Os outros status nao podem ser processados devido a possibilidade de
				//busca de registros ja em processamento pelo produtor devido a demoras no acesso ao banco ou no
				//processo de ajuste.
				String statusRecarga = String.valueOf(Definicoes.STATUS_RECARGA_NAO_PROCESSADA);
				ResultSet registros = (ResultSet)this.listaRecargas.get(statusRecarga);
				if(registros == null)
				{
					registros = this.buscarProximosRegistros(statusRecarga);
                    if (registros != null)
                        this.listaRecargas.put(statusRecarga, registros);
				}
			}
		}
	}
    
    /**
     * Retorna o ResultSet com registros elegiveis para processamento.<br>
     * Caso nenhuma linha retorne entao o ponteiro para o ResultSet fica nulo.
     * 
     * @param      statusRecarga           Status do registro na Fila de Recargas.
     * @return     ResultSet da pesquisa no banco de dados.
     */
    public ResultSet buscarProximosRegistros(String statusRecarga)
    {
        try
        {
            FilaRecargasDAO dao = new FilaRecargasDAO(conexaoPrep);
            return dao.buscarRegistrosByStatus(statusRecarga);
        }
        catch(Exception se)
        {
            //Qualquer erro ao tentar ler os registros e registrado no log. O resultSet continua nulo nao sendo
            //processadas as recargas.
            GerentePoolLog gerenteLog = GerentePoolLog.getInstancia(FilaRecargasDAO.class);
            gerenteLog.log(idProcesso, Definicoes.ERRO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,
                    "buscaProximosRegistros","Erro ao ler registros na fila de recargas. Erro:"+se);

            return null;
        }
    }

	/**
	 *	Retorna a conexao com o banco de dados do produtor.
	 *
	 *	@return		Conexao com o banco de dados.
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}

	/**
	 *	Notifica o termino do consumo da recarga.
	 *
	 *	@param		recarga					Informacoes da recarga para consumo.
	 */
	public void notificarTermino(FilaRecargas recarga)
	{
		synchronized(this.token)
		{
			if(recarga != null)
				this.recargasAtivas.remove(recarga.getRowId());
		}
	}

	/**
	 * Metodo....:run
	 * Descricao.:Este metodo e o tratamento de execucao da thread de recarga
	 * @see run
	 */
	public void run()
	{
		try
		{
			//Obtendo a conexao com o banco de dados.
			if(this.conexaoPrep == null)
				this.conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);
			//Obtendo o mapeamento de configuracoes do GPP.
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			//Busca o tempo de espera entre as execucoes de producao de recargas.
			int timeOut = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("PRODUTOR_RECARGA_TEMPO_ESPERA"));
			//Obtendo os status de registros da Fila de Recargas que devem ser processados. Os status diferentes de
			//recarga nao processada podem ser obtidos pelo produtor somente quando nao houver registros na fila.
			//Isto porque em caso de demora no acesso ao banco de dados algum registro que esteja ja sendo processado
			//por alguma thread pode ser obtida novamente com o status antigo.
			this.listaStatus = confGPP.getMapValorConfiguracaoGPP("PRODUTOR_RECARGA_LISTA_STATUS").split(";");
			//Inicializa as threads de consumo caso nenhuma linha ainda tenha sido adicionada.
			GerenteConsumidorRecarga.getInstancia(this.idProcesso).notificaConsumoRecarga();

			//Fica em processamento buscando os proximos registros a serem processados.
			while(true)
			{
				this.buscarProximosRegistros();
				Thread.sleep(timeOut*1000);
			}
		}
		catch(NumberFormatException e)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"run","Configuracao de status de processamento na Fila de Recargas incorreto.");
		}
		catch(InterruptedException ie)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"run","Sleep de thread do consumidor de recarga interrompido.");
		}
		catch(GPPInternalErrorException ge)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"run","Erro no processamento de producao de recargas. Erro"+ge);
		}
		catch(Exception e)
		{
			this.gerenteLog.log(this.idProcesso,Definicoes.ERRO,Definicoes.CL_GERENTE_PRODUTOR_RECARGA,"run","Excecao: " + e);
		}
		finally
		{
			//Libera a conexao com o banco de dados.
			GerentePoolBancoDados.getInstancia(this.idProcesso).liberaConexaoPREP(this.conexaoPrep, this.idProcesso);
			this.conexaoPrep = null;
		}
	}
}
