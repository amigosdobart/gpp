// Definicao do Pacote
package com.brt.gpp.gerentesPool;

// Arquivos de Imports de Java/CORBA
import java.util.*;
import java.sql.*;

// Arquivos de Imports do GPP
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

import com.brt.gpp.aplicacoes.enviarSMS.DadosSMS;

import com.brt.gpp.comum.mapeamentos.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

import java.text.SimpleDateFormat;

/**
  *
  * Este arquivo contem a definicao da classe de ProdutorSMS que implementa o Pattern de
  * Producer (producer/consumer) para busca de SMS a ser enviado para o SMSC
  *
  * <P> Versao:        	1.0
  *
  * @Autor:            	Daniel Cintra Abib
  * Data:               23/03/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class ProdutorSMS extends Thread
{
	// Variaveis Membro
	private static ProdutorSMS	instancia 		= null;
	private long				idLog			= 0;
	private boolean				deveProcessar 	= true;

	// Vetores de conexoes
	private LinkedHashSet	vetorPrioridadeMenosUm	= null;
	private LinkedHashSet	vetorPrioridadeZero		= null;
	private LinkedHashSet	vetorPrioridadeUm		= null;
	private LinkedHashSet	vetorSMSEmEnvio			= null;

	// Referencias aos Singlestons
	private static ArquivoConfiguracaoGPP 	arqConfiguracao = null;	// Referencia ao arquivo de configuracao
	private static GerentePoolLog   		Log 			= null;	// Referencia ao gerente de LOG
	private static GerentePoolBancoDados	gerenteBanco	= null; // Referentia ao gerente de Banco de dados

	/**
	 * Metodo...:ProdutorSMS
	 * Descricao:Inicializacao da classe - construtor protegido (protected)
	 */
	protected ProdutorSMS( )
	{
		// Nao faz nada - apenas torna a construcao protegida
	}

	/**
	 * Metodo...:getInstancia
	 * Descricao:Metodo para criar e retornar uma instancia da classe
	 *
	 * @param  long					- Id do processo
	 * @return GerentePoolTecnomen  - Referencia unica do Servant
	 */
    public static ProdutorSMS getInstancia ( long idProcesso )
    {
    	if (instancia == null)
    	{
    		instancia = new ProdutorSMS();

			// Obtem uma instancia dos gerentes de log e arquivo de configuracao
			arqConfiguracao = ArquivoConfiguracaoGPP.getInstance( );  		// Carrega a instancia do arquivo de configuracao
			Log = GerentePoolLog.getInstancia(ProdutorSMS.class );   						// Obtem uma instancia do gerente de LOG
			gerenteBanco = GerentePoolBancoDados.getInstancia(idProcesso);	// Obtem uma instancia do gerente de Banco de dados

			instancia.criaPool(idProcesso);
			instancia.setDeveProcessar(arqConfiguracao.deveConsumirSMS());
			Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Produtor SMS criado.");
    	}

		return instancia;
    }

	/**
	 * Metodo....:criaPool
	 * Descricao.:Cria um o vetor que armazena a producao de SMS
	 * @param  long    - Id do processo
	 * @return boolean - Indica se conseguiu criar ou nao o pool
	 */
    protected boolean criaPool ( long idProcesso )
    {
    	vetorPrioridadeMenosUm 	= new LinkedHashSet();
		vetorPrioridadeZero 	= new LinkedHashSet();
		vetorPrioridadeUm   	= new LinkedHashSet();
		vetorSMSEmEnvio     	= new LinkedHashSet();

		return true;
    }

	/**
	 * Metodo..:setDeveProcessar
	 * Descrica:Marca que o processo deve parar ou nao o envio SMS
	 * @param aDeveProcessar - True para enviar SMS, False parar o processo de envio de SMS
	 */
	public void setDeveProcessar (boolean aDeveProcessar)
	{
		this.deveProcessar = aDeveProcessar;
	}

	/**
	 * Metodo....: getDeveProcessar
	 * Descricao.: Obtem o flag de processamento de SMS
	 * @return	boolean	- True se deve enviar SMS ou false se deve parar o processamento
	 */
	public boolean getDeveProcessar ( )
	{
		return this.deveProcessar;
	}

	/**
	 * Metodo....:run
	 * Descricao.:Processo de execucao de de leitura do banco de dados e populacao
	 *            dos vetores de envio de SMS
	 * @see java.lang.Runnable#run()
	 */
	public void run ( )
	{
		Log.log(this.idLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Inicio do metodo de run - Produtor SMS");

		// Carregando o vetor de SMS
		String sql =
		"SELECT * FROM ( "
		+ "SELECT /*+ INDEX(tbl_ger_envio_sms XIE1TBL_GER_ENVIO_SMS) */ ID_REGISTRO, IDT_MSISDN_ORIGEM, IDT_MSISDN, DES_MENSAGEM, NUM_PRIORIDADE, TO_CHAR(DAT_ENVIO_SMS, 'DD/MM/YYYY HH24:MI:SS'), TIP_SMS "
		+  "FROM TBL_GER_ENVIO_SMS "
		+  "WHERE IDT_STATUS_PROCESSAMENTO = ? "
		+  "ORDER BY NUM_PRIORIDADE, ID_REGISTRO "
		+  ") WHERE "
		+  "ROWNUM <= ? "
		;
		// Adquirindo a informação do número máximo de SMSs a serem produzidos por vez.
		long numMaxSMSProduzirDefinido = 0;
		long numMaxSMSProduzir = 0;
		try
		{
			// Pega referencia para a tabela de configuracao do GPP
			MapConfiguracaoGPP mapConfiguracoes = MapConfiguracaoGPP.getInstancia();
			numMaxSMSProduzirDefinido = Long.parseLong(mapConfiguracoes.getMapValorConfiguracaoGPP(Definicoes.NUMERO_SMS_MAX_PRODUZIR));
		}
		catch (GPPInternalErrorException e)
		{
			Log.log(this.idLog, Definicoes.ERRO, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "ERRO ao obter mapeamento das Configurações.");
		}

		//Início do laço eterno
		while (true)
		{
			if (this.deveProcessar)
			{
				PREPConexao prep = null;

				try
				{
					prep = gerenteBanco.getConexaoPREP(this.idLog);

					numMaxSMSProduzir = numMaxSMSProduzirDefinido - this.vetorPrioridadeMenosUm.size() - this.vetorPrioridadeZero.size() - this.vetorPrioridadeUm.size();

					Log.log(this.idLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Numero de SMS a produzir: " + numMaxSMSProduzir
							+ ", quantidade prioridade menos um: " + this.vetorPrioridadeMenosUm.size()
							+ ", quantidade prioridade zero: " + this.vetorPrioridadeZero.size()
							+ ", quantidade prioridade um: " + this.vetorPrioridadeUm.size());

					Object param[] = {new Integer(Definicoes.SMS_NAO_ENVIADO), new Long (numMaxSMSProduzir)};

					// Busca os dados da tabela TBL_GER_ENVIO_SMS
					ResultSet rs = prep.executaPreparedQuery(sql, param, this.idLog);

					while (rs.next())
					{
						// Cria uma estrutura DadosSMS e armazena os dados
						DadosSMS sms = new DadosSMS();
						sms.setId(rs.getLong(1));
						sms.setMsisdnOrigem(rs.getString(2));
						sms.setMsisdn(rs.getString(3));
						sms.setMensagem(rs.getString(4));
						sms.setPrioridade(rs.getInt(5));
						sms.setData (rs.getString(6));
						sms.setTipo(rs.getString(7));

						// Inclui o SMS no vetor
						this.incluiSMS( sms, this.idLog );

						Log.log(this.idLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Incluindo SMS MSISDN:" + sms.getMsisdn());
					}
					rs.close();

					/*  Apos a inclusao de objetos de SMS nos vetores entao faz uma chamada
					 *  ao Consumidor para que estes "acordem" as threads de envio de SMS
					 */
					ConsumidorSMS.getInstance(this.idLog).notificaThreadsEnvioSMS(this.idLog);
				}
				catch (GPPInternalErrorException e)
				{
					Log.log(this.idLog, Definicoes.ERRO, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "ERRO ao obter conexao com banco de dados.");
				}
				catch (SQLException e)
				{
					Log.log(this.idLog, Definicoes.WARN, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "ERRO(SQL) na execucao de select no banco de dados.");
				}
				finally
				{
					gerenteBanco.liberaConexaoPREP(prep, this.idLog);
				}
			}

			// SEMPRE Dorme para esperar a proxima execução
			try
			{
				Thread.sleep ( arqConfiguracao.getTempoEsperaSMS()*1000 );
			}
			catch (InterruptedException e)
			{
				Log.log(this.idLog, Definicoes.ERRO, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Erro no processo de sleep to Thread");
			}
		}
		//Fim do laço eterno
		//Log.log(this.idLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Fim do metodo de run - Produtor SMS");
	}

	/**
	 * Metodo....:incluiSMS
	 * Descricao.:Inclui um SMS no vetor
	 * @param DadosSMS 	- Objeto contendo informacoes de SMS
	 * @param long		- Identificacao do Processo
	 */
	protected synchronized void incluiSMS ( DadosSMS aDadosSMS, long aIdLog )
	{
		Log.log(this.idLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "incluiSMS", "Inicio");

		if ( (aDadosSMS.getPrioridade() == Definicoes.SMS_PRIORIDADE_MENOS_UM))
		{
			this.vetorPrioridadeMenosUm.add (aDadosSMS);
			Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "incluiSMS", "SMS de prioridade MENOS UM INCLUIDO.");
		}
		else if ( (aDadosSMS.getPrioridade() == Definicoes.SMS_PRIORIDADE_ZERO))
			{
				this.vetorPrioridadeZero.add (aDadosSMS);
				Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "incluiSMS", "SMS de prioridade ZERO INCLUIDO.");
			}
		else
		{
			this.vetorPrioridadeUm.add (aDadosSMS);
			Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "incluiSMS", "SMS de prioridade UM INCLUIDO.");
		}

		Log.log(this.idLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "incluiSMS", "Fim");
	}

	/**
	 * Metodo....:buscaProximoSMS
	 * Descricao.:Busca o proximo SMS a ser enviado
	 *
	 * @param  long		- Identificacao do processo
	 * @return DadosSMS - Dados do SMS a ser enviado
	 */
	protected synchronized DadosSMS buscaProximoSMS ( long aIdLog )
	{
		DadosSMS retorno = null;
		MapConfiguracaoGPP mapConfig = null;

		// Instanciando mapeamento da TBL_GER_CONFIG_GPP
		try
		{
			//Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				Log.log(aIdLog, Definicoes.WARN, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Problemas com mapeamento das Configurações GPP");
		}
		catch (Exception e)
		{
			Log.log(aIdLog, Definicoes.ERRO, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Problemas com mapeamento das Configurações GPP");
		}

		String horaIni = mapConfig.getMapValorConfiguracaoGPP("HORA_INICIO_SMS_1");
		String horaFim = mapConfig.getMapValorConfiguracaoGPP("HORA_FIM_SMS_1");

		SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm");
		String horaAtual = sdf.format(new java.util.Date());

		Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Inicio");

		// Verifica se existe algum SMS de prioridade menos um
		if (this.vetorPrioridadeMenosUm.size() > 0)
		{
			Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Retornando SMS de prioridade MENOS UM.");

			/* Remove o primeiro elemento do Vetor, caso este nao tenha
			 * sido enviado, entao ele e adicionado a um vetor que contem
			 * os SMS em processo de envio. Antes ele altera o status do
			 * objeto para indicar que o mesmo esta sendo enviado
			 */
			retorno = (DadosSMS)vetorPrioridadeMenosUm.iterator().next();
			if (vetorPrioridadeMenosUm.remove(retorno))
			{
				retorno.setStatus(Definicoes.SMS_SENDO_ENVIDADO);
				vetorSMSEmEnvio.add(retorno);
			}
		}
		// Verifica se existe algum SMS de prioridade zero
		else if (this.vetorPrioridadeZero.size() > 0)
		{
			Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Retornando SMS de prioridade ZERO.");

			/* Remove o primeiro elemento do Vetor, caso este nao tenha
			 * sido enviado, entao ele e adicionado a um vetor que contem
			 * os SMS em processo de envio. Antes ele altera o status do
			 * objeto para indicar que o mesmo esta sendo enviado
			 */
			retorno = (DadosSMS)vetorPrioridadeZero.iterator().next();
			if (vetorPrioridadeZero.remove(retorno))
			{
				retorno.setStatus(Definicoes.SMS_SENDO_ENVIDADO);
				vetorSMSEmEnvio.add(retorno);
			}
		}
		// Se nao existe, verifica horario e envia um SMS de prioridade um
		else
		{
			// Se horario for comercial, estabelecido na tabela de Configuracoes do GPP,
			// envia um SMS de prioridade um
			if ( (horaAtual.compareTo(horaIni) >= 0) && (horaAtual.compareTo(horaFim)<=0) )
			{
				if (this.vetorPrioridadeUm.size() > 0)
				{
					Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Retornando SMS de prioridade UM.");
					/* Remove o primeiro elemento do Vetor, caso este nao tenha
					 * sido enviado, entao ele e adicionado a um vetor que contem
					 * os SMS em processo de envio.
					 */
					retorno = (DadosSMS)vetorPrioridadeUm.iterator().next();
					if (vetorPrioridadeUm.remove(retorno))
					{
						retorno.setStatus(Definicoes.SMS_SENDO_ENVIDADO);
						vetorSMSEmEnvio.add(retorno);
					}
				}
			}
			else
			{
				Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Horario nao permite envio de SMS de prioridade UM");
			}
		}

		Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "buscaProximoSMS", "Fim");
		return retorno;
	}

	/**
	 * Metodo....:consomeSMS
	 * Descricao.:Metodo de consumo do vetor de SMS para envio ao SMSC
	 *
	 * @param  long		- Identificacao do processo
	 * @return DadosSMS - Informacoes de SMS encontrada no Pool ou entao nulo
	 *                    quando nenhum objeto foi encontrado no pool
	 */
	public DadosSMS consomeSMS ( long aIdLog )
	{
		return this.buscaProximoSMS ( aIdLog );
	}

	/**
	 * Metodo....:atualizaStatusSMS
	 * Descricao.:Recebe o status do envio do SMS
	 *
	 * @param aDadosSMS	- Dados do SMS que foi enviado
	 * @long			- Identificacao do processo
	 */
	public void atualizaStatusSMS (DadosSMS aDadosSMS, long aIdLog)
	{
		Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "atualizaStatusSMS", "Inicio");

		// Verifica se o SMS foi enviado com sucesso
		if ( aDadosSMS.getStatus() == Definicoes.SMS_ENVIADO )
		{
			PREPConexao prep = null;
			try
			{
				// Atualiza na tabela que o SMS foi enviado
				prep = gerenteBanco.getConexaoPREP(aIdLog);

				String sql = "UPDATE TBL_GER_ENVIO_SMS SET IDT_STATUS_PROCESSAMENTO = ?, DAT_PROCESSAMENTO = SYSDATE "
								+ "WHERE ID_REGISTRO = ?";

				//SimpleDateFormat dataFormatada = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
				//long timeStamp = dataFormatada.parse(aDadosSMS.getTimestamp()).getTime();

				//Object param[] = {new Integer(Definicoes.SMS_ENVIADO), aDadosSMS.getMSISDN(), new java.sql.Timestamp(timeStamp)};
				Object param[] = {new Integer(Definicoes.SMS_ENVIADO), new Long(aDadosSMS.getId())};
				prep.executaPreparedUpdate(sql, param, aIdLog);

				/*Remove o SMS do vetor de em envio*/
				vetorSMSEmEnvio.remove(aDadosSMS);

				Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "atualizaStatusSMS", "SMS de ID: " + aDadosSMS.getId() + " enviado e atualizado.");
			}
			catch (GPPInternalErrorException e)
			{
				Log.log(aIdLog, Definicoes.ERRO, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Erro ao obter conexao com banco de dados.");
			}
			catch (Exception e)
			{
				Log.log(aIdLog, Definicoes.ERRO, Definicoes.CL_PRODUTOR_SMS, "ProdutorSMS", "Erro generico: " + e);
			}
			finally
			{
				gerenteBanco.liberaConexaoPREP ( prep, aIdLog );
			}

			Log.log(aIdLog, Definicoes.DEBUG, Definicoes.CL_PRODUTOR_SMS, "atualizaStatusSMS", "Fim");
		}
		else if(aDadosSMS.getStatus() == Definicoes.SMS_NAO_ENVIADO)
		{
			/*Remove o SMS do vetor de em envio*/
			vetorSMSEmEnvio.remove(aDadosSMS);

			Log.log(aIdLog,Definicoes.DEBUG,Definicoes.CL_PRODUTOR_SMS,"atualizaStatusSMS","SMS: " + aDadosSMS + " Retornou para seu vetor de prioridades original.");
			/* De acordo com a prioridade do SMS ele volta para o vetor original*/
			if (aDadosSMS.getPrioridade() == Definicoes.SMS_PRIORIDADE_MENOS_UM)
				vetorPrioridadeMenosUm.add(aDadosSMS);
			else if (aDadosSMS.getPrioridade() == Definicoes.SMS_PRIORIDADE_ZERO)
				vetorPrioridadeZero.add(aDadosSMS);
			else if (aDadosSMS.getPrioridade() == Definicoes.SMS_PRIORIDADE_UM)
				vetorPrioridadeUm.add(aDadosSMS);
		}
	}
}