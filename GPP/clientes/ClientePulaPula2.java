package clientes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
/**
 * 
 * @author Luciano Vilela
 * 
 */
public class ClientePulaPula2 
{
	private ArrayList listaPromocoes = null;
	private ArrayList listaPromocaoDiaExecucao = null;
	private ArrayList listaBonusPulaPula = null;
	private Connection connClientes = null;
	private PreparedStatement prepClientes = null;
	private ResultSet selClientes = null;
	private SimpleDateFormat dfLog = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private volatile String sid;
	private volatile String usuario;
	private volatile String senha;
	private volatile String dataExecucao;
	private volatile int    numThreads;
	private volatile String modoExecucao;
	private volatile String promocao;
	
	private final int ID_PENDENTE_RECARGA			= 0;
	private final int ID_CATEGORIA_PULA_PULA		= 1;
	private final int ID_PROMOCAO_PULA_PULA         = 1;
	private final String TIPO_TRANSACAO_PULA_PULA	= "08001";
	
	private final int STATUS_NORMAL_USER = 2;
	
	//Modos de Execucao (normal ou rebarba)
	private final String MODO_EXECUCAO_NORMAL  = "NORMAL";
	private final String MODO_EXECUCAO_REBARBA = "REBARBA";
	
	//Codigos de retorno da execucao do Pula-Pula
	private final short RET_OPERACAO_OK                   =  0;
	private final short RET_STATUS_MSISDN_INVALIDO        = 10;
	private final short RET_PULA_PULA_PREVISAO_MUD_STATUS = 57;
	private final short RET_PULA_PULA_PROMO_VALIDADE_NOK  = 50;
	private final short RET_PULA_PULA_LIGACOES_NOK        = 51;
	private final short RET_PULA_PULA_PENDENTE_RECARGA    = 58;
	private final short RET_ERRO_TECNICO                  = 99;
	
	//Status de execucao na fila de recargas
	private final int REC_STATUS_NAO_EXECUTADO   = 0;
	private final int REC_STATUS_OK              = 3;
	private final int REC_STATUS_TESTE_PULA_PULA = 4;
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception 
	{
		ClientePulaPula2 pulapula = new ClientePulaPula2();
		pulapula.log("Inicio do processo ClientePulaPula");
		try
		{
			if
			(
				pulapula.init
				(
			   		args[0], // SID
					args[1], // Usuario
					args[2], // Senha
					args[3], // Data de processamento formato dd/mm/yyyy
					Integer.parseInt(args[4]), // Quantidade de threads
					args[5], //Modo de execucao
					(args.length == 7) ? args[6] : null //Codigo da promocao
				)
			)
			{
				pulapula.run();
			
				//Atualiza o historico para assinantes pendentes de recarga
				pulapula.log("Inicio da atualizacao do historico dos assinantes pendentes de recarga");
				pulapula.atualizaHistoricoPendenteRecarga((new SimpleDateFormat("dd/MM/yyyy")).parse(args[3]));
			}
			pulapula.log("Fim do processo ClientePulaPula");
		}
		catch(ParseException pe)
		{
			pulapula.log("Formato correto de data: dd/mm/aaaa");
		}
		catch(SQLException se)
		{
			pulapula.log("Problemas ao conectar com o Banco de Dados: " + se);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void log(String msg) 
	{
		System.out.println(dfLog.format(new Date()) + " - " + msg);
	}

	private boolean init(String sid, String usuario, String senha, String data, int numThreads, String modoExecucao, String promocao) 
		throws Exception 
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		this.sid = sid;
		this.usuario = usuario;
		this.senha = senha;
		this.dataExecucao = data;
		this.numThreads = numThreads;
		this.modoExecucao = modoExecucao;
		this.promocao = promocao;
		
		Connection connLista = null;
		PreparedStatement prepLista = null;
		ResultSet selLista = null;
		
		boolean result = false;
		
		try
		{
			//Obtendo a lista de clientes
			if(initClientes(data))
			{
				//Obtendo a lista de promocoes
				connLista = DriverManager.getConnection("jdbc:oracle:oci8:@" + sid, usuario, senha);
				prepLista = connLista.prepareStatement(getQuerySelecaoPromocoes());
				selLista = prepLista.executeQuery();
				listaPromocoes = atualizaListaPromocoes(selLista);
				selLista.close();
				prepLista.close();
				//Obtendo a lista de dias de execucao das promocoes
				prepLista = connLista.prepareStatement(getQuerySelecaoPromocaoDiaExecucao(data));
				selLista = prepLista.executeQuery();
				listaPromocaoDiaExecucao = atualizaListaPromocaoDiaExecucao(selLista);
				selLista.close();
				prepLista.close();
				//Obtendo a lista de valores do Bonus Pula-Pula
				prepLista = connLista.prepareStatement(getQuerySelecaoBonusPulaPula());
				selLista = prepLista.executeQuery();
				listaBonusPulaPula = atualizaListaBonusPulaPula(selLista);
				result = true;
			}
		}
		finally
		{
			if(selLista != null) selLista.close();
			if(prepLista != null) prepLista.close();
			if(connLista != null) connLista.close();
		}
		
		return result;
	}
	
	private boolean initClientes(String dataExecucao) throws ParseException, SQLException
	{
		//Inicializa a selecao de clientes de acordo com o modo de execucao
		connClientes = DriverManager.getConnection("jdbc:oracle:oci8:@" + sid, usuario, senha);
		
		if(modoExecucao.equals(MODO_EXECUCAO_NORMAL))
		{
			log("Inicio do processo ClientePulaPula em Modo Normal");
			prepClientes = prepareClientes(connClientes, dataExecucao);
		}
		else
		{
			if(modoExecucao.equals(MODO_EXECUCAO_REBARBA))
			{
				log("Inicio do processo ClientePulaPula em Modo Rebarba");
				prepClientes = prepareClientesRebarba(connClientes, dataExecucao, promocao);
			}
			else
			{
				log("Opcoes de Modo de Execucao: " + MODO_EXECUCAO_NORMAL + " | " + MODO_EXECUCAO_REBARBA);
				if(connClientes != null) connClientes.close();
				connClientes = null;
				return false;
			}
		}
		
		selClientes = prepClientes.executeQuery();
		
		return true;
	}

	private PreparedStatement prepareClientes(Connection conn, String data) throws ParseException, SQLException
	{	
		//Retorna o objeto PreparedStatement para selecao de clientes em modo de execucao normal
		
		PreparedStatement result = null;
		
		//Parametros 1 e 6: Data de execucao atual
		SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy");
		Date dataExecucao = conversorData.parse(data);
		//Parametro 2: Ano e mes da execucao
		SimpleDateFormat conversorDatMes = new SimpleDateFormat("yyMM");
		String datMes = conversorDatMes.format(mesAnterior(dataExecucao));
		//Parametro 3: Tipo de transacao da promocao PulaPula
		String tipoTransacao = TIPO_TRANSACAO_PULA_PULA;
		//Parametros 4 e 7: Primeiro dia do mes da execucao
		Date dataInicioMesExecucao = getPrimeiroDiaMes(dataExecucao);
		//Parametro 5: Primeiro dia do mes posterior ao da execucao
		Date dataInicioProxMes = getPrimeiroDiaMes(proximoMes(dataExecucao));
		//Parametro 8: Ultimo dia do mes da execucao
		Date dataFimMesExecucao = getUltimoDiaMes(dataExecucao);

		String sqlClientes = getQuerySelecaoClientes();
		result = conn.prepareStatement(sqlClientes);
		result.setDate  (1, new java.sql.Date(dataExecucao.getTime()));
		result.setString(2, datMes);
		result.setString(3, tipoTransacao);
		result.setDate  (4, new java.sql.Date(dataInicioMesExecucao.getTime()));
		result.setDate  (5, new java.sql.Date(dataInicioProxMes.getTime()));
		result.setDate  (6, new java.sql.Date(dataExecucao.getTime()));
		result.setDate  (7, new java.sql.Date(dataInicioMesExecucao.getTime()));
		result.setDate  (8, new java.sql.Date(dataFimMesExecucao.getTime()));
		
		return result;
	}
	
	private PreparedStatement prepareClientesRebarba(Connection conn, String data, String promocao) 
		throws ParseException, SQLException
	{	
		//Retorna o objeto PreparedStatement para selecao de clientes em modo de execucao normal
		PreparedStatement result = null;		
		SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy");
		Date dataExecucao = conversorData.parse(data);		
		
		//Parametro 1: Promocao (Parametro do metodo)
		//Parametro 2: Ano e mes da execucao
		SimpleDateFormat conversorDatMes = new SimpleDateFormat("yyMM");
		String datMes = conversorDatMes.format(mesAnterior(dataExecucao));
		//Parametros 3, 5 e 7: Primeiro dia do mes da execucao
		Date dataInicioMesExecucao = getPrimeiroDiaMes(dataExecucao);
		//Parametro 4: Tipo de transacao da promocao PulaPula
		String tipoTransacao = TIPO_TRANSACAO_PULA_PULA;
		//Parametro 5: Primeiro dia do mes posterior ao da execucao
		Date dataInicioProxMes = getPrimeiroDiaMes(proximoMes(dataExecucao));
		//Parametro 8: Ultimo dia do mes da execucao
		Date dataFimMesExecucao = getUltimoDiaMes(dataExecucao);

		String sqlClientes = getQuerySelecaoClientesRebarba();
		result = conn.prepareStatement(sqlClientes);		
		result.setString(1, promocao);
		result.setString(2, datMes);
		result.setDate  (3, new java.sql.Date(dataInicioMesExecucao.getTime()));
		result.setString(4, tipoTransacao);
		result.setDate  (5, new java.sql.Date(dataInicioMesExecucao.getTime()));
		result.setDate  (6, new java.sql.Date(dataInicioProxMes.getTime()));
		result.setDate  (7, new java.sql.Date(dataInicioMesExecucao.getTime()));
		result.setDate  (8, new java.sql.Date(dataFimMesExecucao.getTime()));
		
		return result;
	}
	
	private void run() throws Exception 
	{
		ThreadGroup tg = new ThreadGroup("Processador Fila");
		for (int i = 0; i < numThreads; i++) 
		{
			/**
			 * Cria as threads que irao consumir a fila de clientes Pula-pula
			 * cria uma conexao para cada thread
			 * 
			 */
			Thread threads = new Thread(tg, new Runnable() 
			{
				private Connection con = null;
				private PreparedStatement prepInsert = null;
				private PreparedStatement prepUpdate = null;
				private long idThread = this.hashCode();

				public void run() 
				{
					HashMap infoAssinante = null;
					String msisdn = null;
					int indIsentoLimite = -1;
					int idPromocao = -1;
					int numDiaEntrada = -1;
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

					try 
					{
						//Data de Execucao como objeto Date
						Date data = df.parse(dataExecucao);
						//Conexao com o banco de dados para operacoes
						con = DriverManager.getConnection("jdbc:oracle:oci8:@" + sid, usuario, senha);
						//Comando que atualiza a proxima execucao e o periodo de analise do bonus Pula-Pula
						DecimalFormat dfn = new DecimalFormat("#,##0.00");
						short idtCodigoRetorno = RET_OPERACAO_OK;
						
						while ((infoAssinante = nextClient()) != null) 
						{
							
							//Obtem o MSISDN 
							msisdn = (String)infoAssinante.get("IDT_MSISDN");
							//Obtem o indicador de insencao do limite da promocao
							indIsentoLimite = ((Integer)infoAssinante.get("IND_ISENTO_LIMITE")).intValue();
							//Obtem o ID da promocao do assinante
							idPromocao = ((Integer)infoAssinante.get("IDT_PROMOCAO")).intValue();
							//Obtem a data de entrada na promocao
							Date diaEntrada = (Date)infoAssinante.get("DAT_ENTRADA_PROMOCAO");
							Calendar calDiaEntrada = Calendar.getInstance();
							calDiaEntrada.setTime(diaEntrada);
							numDiaEntrada = calDiaEntrada.get(Calendar.DAY_OF_MONTH);
							
							log("Thread: " + idThread + " - MSISDN: " + msisdn + " - Processando");
							
							try
							{
								
								if((idtCodigoRetorno = validaAssinante(infoAssinante, data)) == RET_OPERACAO_OK)
								{
							
									log("Thread: " + idThread + " - MSISDN: " + msisdn + 
										" - Executando consulta do valor do bonus");
								
									//Obtendo o valor de ajuste
									double valorAjuste = getValorAjuste(infoAssinante);
							
									//Obtendo o limite de credito da promocao
									double limiteCredito = getLimiteCreditoPromocao(idPromocao);
								
									//Se o limite de credito for diferente de -1 e o assinante nao for isento de 
									//limite, indica que ha limite de credito
									if((limiteCredito != -1) && (indIsentoLimite != 1))
									{
										if (valorAjuste > limiteCredito) 
										{
											valorAjuste = limiteCredito;
										}
									}
									
									infoAssinante.put("VLR_AJUSTE", new Double(valorAjuste));
								
									log("Thread: " + idThread + " - MSISDN: " + msisdn + 
										" - Valor do Bonus: " + dfn.format(valorAjuste));
									log("Thread: " + idThread + " - MSISDN: " + msisdn + 
										" - Executando insercao na fila de recargas");
								
									//Inserindo o ajuste na fila de recargas
									Date dataExecucaoRecarga = getDataExecucaoRecarga(idPromocao, numDiaEntrada, data);
									infoAssinante.put("DAT_EXECUCAO_RECARGA", dataExecucaoRecarga);
									String insert = getInsertFilaRecarga(infoAssinante);
									prepInsert = con.prepareStatement(insert);
									prepInsert.executeUpdate();

									//Se o assinante nao recebeu ligacoes, inserir o registro na fila de recargas de
									//qualquer forma, para que o consumidor possa zerar o seu saldo. Porem o registro
									//no historico deve indicar a falta de ligacoes.
									if (valorAjuste == 0) 
									{
										idtCodigoRetorno = RET_PULA_PULA_LIGACOES_NOK;
										log("Thread: " + idThread + " - MSISDN: " + msisdn + 
										" - Assinante nao recebeu ligacoes que o fazem apto a receber bonus");
									}
								}
								else
								{
									log("Thread: " + idThread + " - MSISDN: " + msisdn + 
									" - Assinante nao esta apto para receber bonus");
								}
								
								log("Thread: " + idThread + " - MSISDN: " + msisdn + 
									" - Executando atualizacao do assinante");
								
								//Atualizando o registro da promocao do assinante caso a operacao tenha sido OK,
								//o assinante nao tenha recebido ligacoes ou a data seja maior ou igual ao ultimo 
								//dia de concessao de bonus
								Date maxDataExecucaoRecarga = getMaxDataExecucaoRecarga(idPromocao, data);
								if((idtCodigoRetorno == RET_OPERACAO_OK) || 
								   (idtCodigoRetorno == RET_PULA_PULA_LIGACOES_NOK) || 
								   (data.compareTo(maxDataExecucaoRecarga) >= 0))
								{
									Date dataExecucao = getDataExecucao(idPromocao, numDiaEntrada, data);
									infoAssinante.put("DAT_EXECUCAO", dataExecucao);
									String update = getUpdatePromocaoAssinante(infoAssinante);
									prepUpdate = con.prepareStatement(update);
									prepUpdate.executeUpdate();
								}
							}
							catch (Exception e) 
							{
								idtCodigoRetorno = RET_ERRO_TECNICO;
								log("Thread: " + idThread + " - MSISDN: " + msisdn + 
									" - Nao foi possivel efetuar o ajuste");
								log("Thread: " + idThread + " - MSISDN: " + msisdn + 
									" - EXCEPTION : " + e.getMessage());
								e.printStackTrace();
							}
							finally
							{
								log("Thread: " + idThread + " - MSISDN: " + msisdn + 
									" - Atualizando o historico de execucao do Pula-Pula");
								infoAssinante.put("IDT_CODIGO_RETORNO", new Integer(idtCodigoRetorno));
								atualizaHistoricoPulaPula(infoAssinante, data, con);
								
								infoAssinante.clear();
								if(prepInsert != null) prepInsert.close();
								if(prepUpdate != null) prepUpdate.close();								
							}
							
						}
						
						con.close();
					} 
					catch (Exception e) 
					{
						log("Thread: " + idThread + " - MSISDN: " + msisdn + 
							" - EXCEPTION : " + e.getMessage());
						e.printStackTrace();
					}
				}
			});
			threads.start();
		}

		while (tg.activeCount() > 0) 
		{
			try 
			{
				Thread.sleep(1000);
			} 
			catch (Exception ignorado) 
			{
			}
		}
		
		selClientes.close();
		prepClientes.close();
		connClientes.close();
	}

	public synchronized HashMap nextClient() throws SQLException 
	{
		//Retorna um objeto contendo as informacoes do assinante referentes a promocao
		if (selClientes.next()) 
		{
			HashMap result = new HashMap();
			result.put("IDT_MSISDN", selClientes.getString("IDT_MSISDN"));
			result.put("IDT_PROMOCAO", new Integer(selClientes.getInt("IDT_PROMOCAO")));
			result.put("DAT_EXECUCAO", selClientes.getDate("DAT_EXECUCAO"));
			result.put("DAT_ENTRADA_PROMOCAO", selClientes.getDate("DAT_ENTRADA_PROMOCAO"));
			//Para o campo IND_SUSPENSO, verificar se o campo lido foi NULL porque nao pode ser assumido o valor
			//0 como default
			result.put("IND_SUSPENSO", new Integer(selClientes.getInt("IND_SUSPENSO")));
			if(selClientes.wasNull())
			{
				result.put("IND_SUSPENSO", null);
			}
			result.put("IND_ISENTO_LIMITE", new Integer(selClientes.getInt("IND_ISENTO_LIMITE")));
			result.put("IDT_STATUS", new Integer(selClientes.getInt("IDT_STATUS")));
			result.put("DAT_EXPIRACAO_PRINCIPAL", selClientes.getDate("DAT_EXPIRACAO_PRINCIPAL"));
			result.put("IND_ZERAR_SALDO_BONUS", new Integer(selClientes.getInt("IND_ZERAR_SALDO_BONUS")));
			result.put("IND_ZERAR_SALDO_SMS", new Integer(selClientes.getInt("IND_ZERAR_SALDO_SMS")));
			result.put("IND_ZERAR_SALDO_GPRS", new Integer(selClientes.getInt("IND_ZERAR_SALDO_GPRS")));
			//Verifica se os minutos totais sao menores que os minutos com tarifacao diferenciada Friends&Family. Caso 
			//seja verdadeiro (o que indica inconsistencia na base ou perda de CDRs), os minutos FF devem ser ajustados 
			//de forma a ficarem iguais aos minutos totais, conforme alinhamento entre Albervan, Luciano e Daniel
			//Ferreira. Esta decisao foi tomada de modo a diminuir o numero de reclamacoes por nao recebimento de 
			//bonus, ao mesmo tempo em que os registros de ligacoes recebidas no extrato permanecerao consistentes 
			//(o numero total de minutos nas ligacoes detalhadas correspondem ao numero consolidado, sendo estes todos 
			//considerados como tendo tarifacao diferenciada).
			double minCredito = selClientes.getDouble("MIN_CREDITO");
			double minFF = selClientes.getDouble("MIN_FF");
			if(minCredito < minFF)
			{
				minFF = minCredito;
			}
			result.put("MIN_CREDITO", new Double(minCredito));
			result.put("MIN_FF", new Double(minFF));
			
			return result;
		} 
		else 
		{
			return null;
		}
	}
	
	private synchronized String getNomePromocao(int idPromocao)
	{
		//Retorna o nome da promocao. 
		//Se retornar null, indica que a promocao nao possui nome, o que e um erro nao grave.
		HashMap promocao = null;
		
		for(int i = 0; i < listaPromocoes.size(); i++)
		{
			promocao = (HashMap)listaPromocoes.get(i);
			if (((Integer)promocao.get("IDT_PROMOCAO")).intValue() == idPromocao)
			{
				return (String)promocao.get("NOM_PROMOCAO");
			}
		}
		
		return null;
	}
	
	private synchronized Date getDataInicioValidade(int idPromocao)
	{
		//Retorna a data de inicio da aplicacao da promocao. 
		//Se retornar null, indica que a promocao nao possui data de inicio de validade
		HashMap promocao = null;
		
		for(int i = 0; i < listaPromocoes.size(); i++)
		{
			promocao = (HashMap)listaPromocoes.get(i);
			if (((Integer)promocao.get("IDT_PROMOCAO")).intValue() == idPromocao)
			{
				return (Date)promocao.get("DAT_INICIO_VALIDADE");
			}
		}
		
		return null;
	}
	
	private synchronized Date getDataFimValidade(int idPromocao)
	{
		//Retorna a data final da aplicacao da promocao. 
		//Se retornar null, indica que a promocao nao possui data final de validade
		HashMap promocao = null;
		
		for(int i = 0; i < listaPromocoes.size(); i++)
		{
			promocao = (HashMap)listaPromocoes.get(i);
			if (((Integer)promocao.get("IDT_PROMOCAO")).intValue() == idPromocao)
			{
				return (Date)promocao.get("DAT_FIM_VALIDADE");
			}
		}
		
		return null;
	}
	
	private synchronized double getLimiteCreditoPromocao(int idPromocao) throws ParseException
	{
		//Retorna o limite de credito da promocao. 
		//Se retornar -1, indica que a promocao nao possui limite de credito
		HashMap promocao = null;
		
		for(int i = 0; i < listaPromocoes.size(); i++)
		{
			promocao = (HashMap)listaPromocoes.get(i);
			if (((Integer)promocao.get("IDT_PROMOCAO")).intValue() == idPromocao)
			{
				return ((Double)promocao.get("VLR_MAX_CREDITO_BONUS")).doubleValue();
			}
		}
		
		return 0;
	}
	
	private synchronized Date getDataExecucao(int idPromocao, int numDiaEntrada, Date dataExecucao)
	{
		//Retorna o dia de execucao para a promocao no dia especificado por parametro. 
		//Se retornar null, indica que a promocao nao possui dia de execucao para o dia de entrada na promocao  
		//especificado, o que e um erro. Este campo deve ser preenchido.
		
		HashMap promocaoDiaExecucao = null;
		
		for(int i = 0; i < listaPromocaoDiaExecucao.size(); i++)
		{
			promocaoDiaExecucao = (HashMap)listaPromocaoDiaExecucao.get(i);
			if ((((Integer)promocaoDiaExecucao.get("IDT_PROMOCAO")).intValue() == idPromocao) && 
				(((Integer)promocaoDiaExecucao.get("NUM_DIA_ENTRADA")).intValue() == numDiaEntrada))
			{								
				//Determina a data da proxima execucao
				Calendar calDataExecucao = Calendar.getInstance();
				calDataExecucao.setTime(dataExecucao);
				calDataExecucao.add(Calendar.MONTH, 1);
				calDataExecucao.set(Calendar.DAY_OF_MONTH, 
						            ((Integer)promocaoDiaExecucao.get("NUM_DIA_EXECUCAO")).intValue());
				Date result = calDataExecucao.getTime();
				
				return result;
			}
		}
		
		return null;
	}
	
	private synchronized Date getDataExecucaoRecarga(int idPromocao, int numDiaEntrada, Date dataExecucao) 
	{
		//Retorna dia de execucao da recarga de bonus para a promocao no dia especificado por parametro. 
		//Se retornar null, indica que a promocao nao possui dia de execucao para o dia de entrada na promocao  
		//especificado, o que e um erro. Este campo deve ser preenchido.
		HashMap promocaoDiaExecucao = null;
		
		for(int i = 0; i < listaPromocaoDiaExecucao.size(); i++)
		{
			promocaoDiaExecucao = (HashMap)listaPromocaoDiaExecucao.get(i);
			if ((((Integer)promocaoDiaExecucao.get("IDT_PROMOCAO")).intValue() == idPromocao) && 
				(((Integer)promocaoDiaExecucao.get("NUM_DIA_ENTRADA")).intValue() == numDiaEntrada))
			{								
				//Determina a data de execucao na fila de recarga
				Calendar calExecucaoRecarga = Calendar.getInstance();
				calExecucaoRecarga.setTime(dataExecucao);
				calExecucaoRecarga.set(Calendar.DAY_OF_MONTH, 
						               ((Integer)promocaoDiaExecucao.get("NUM_DIA_EXECUCAO_RECARGA")).intValue());
        		
				Date result = calExecucaoRecarga.getTime();
				
				return result;
			}
		}
		
		return null;
	}
	
	private synchronized Date getMaxDataExecucaoRecarga(int idPromocao, Date dataExecucao) 
	{
		//Retorna o ultimo dia de recarga da promocao especificada por parametro 
		//Se retornar null, indica que a promocao nao possui dia de execucao, o que e um erro. 
		//Este campo deve ser preenchido.
		HashMap promocaoDiaExecucao = null;
		int numMaxDiaRecarga = 0;
		
		for(int i = 0; i < listaPromocaoDiaExecucao.size(); i++)
		{
			promocaoDiaExecucao = (HashMap)listaPromocaoDiaExecucao.get(i);
			
			if (((Integer)promocaoDiaExecucao.get("IDT_PROMOCAO")).intValue() == idPromocao)
			{
				int numDiaRecarga = ((Integer)promocaoDiaExecucao.get("NUM_DIA_EXECUCAO_RECARGA")).intValue(); 
				if(numDiaRecarga > numMaxDiaRecarga)
				{
					numMaxDiaRecarga = numDiaRecarga;
				}
			}
		}
		if(numMaxDiaRecarga > 0)	
		{								
			//Constroi a ultima data de execucao de recarga
			Calendar calExecucaoRecarga = Calendar.getInstance();
			calExecucaoRecarga.setTime(dataExecucao);
			calExecucaoRecarga.set(Calendar.DAY_OF_MONTH, numMaxDiaRecarga);

			Date result = calExecucaoRecarga.getTime();
				
			return result;
		}
		
		return null;
	}
	
	private synchronized double getVlrBonusMinuto(int idCodigoNacional)
	{
		//Retorna o valor do Bonus Pula-Pula pro minuto de acordo com o Codigo Nacional especificado por parametro. 
		//Se retornar -1, indica que o codigo nacional nao possui Bonus Pula-Pula.
		HashMap bonusPulaPula = null;
		
		for(int i = 0; i < listaBonusPulaPula.size(); i++)
		{
			bonusPulaPula = (HashMap)listaBonusPulaPula.get(i);
			if(((Integer)bonusPulaPula.get("ID_CODIGO_NACIONAL")).intValue() == idCodigoNacional)
			{
				return ((Double)bonusPulaPula.get("VLR_BONUS_MINUTO")).doubleValue();
			}
		}
		
		return -1;
	}
	
	private synchronized double getVlrBonusMinutoFF(int idCodigoNacional)
	{
		//Retorna o valor do Bonus Pula-Pula pro minuto de acordo com o Codigo Nacional especificado por parametro. 
		//Se retornar -1, indica que o codigo nacional nao possui Bonus Pula-Pula.
		HashMap bonusPulaPula = null;
		
		for(int i = 0; i < listaBonusPulaPula.size(); i++)
		{
			bonusPulaPula = (HashMap)listaBonusPulaPula.get(i);
			if(((Integer)bonusPulaPula.get("ID_CODIGO_NACIONAL")).intValue() == idCodigoNacional)
			{
				return ((Double)bonusPulaPula.get("VLR_BONUS_MINUTO_FF")).doubleValue();
			}
		}
		
		return -1;
	}
	
//	private Integer getNumDiasExpiracaoBonus(HashMap infoAssinante, Date dataExecucao) throws ParseException
//	{
//		//Retorna o numero de dias para a expiracao do saldo de bonus, de forma que a data de expiracao seja igual
//		//ao dia anterior da previsao de execucao na fila de recargas do proximo mes.
//		
//		int idPromocao = ((Integer)infoAssinante.get("IDT_PROMOCAO")).intValue();
//		long millisInicio = 0;
//		long millisFim = 0;
//		
//		Date dataRecarga = (Date)infoAssinante.get("DAT_EXECUCAO_RECARGA");
//		if(dataRecarga.compareTo(dataExecucao) >= 0)
//		{
//			millisInicio = dataRecarga.getTime();
//		}
//		else
//		{
//			millisInicio = dataExecucao.getTime();
//		}
//		
//		//Calculando a data de previsao de concessao de bonus para o proximo mes
//		Calendar calRecargaProxMes = Calendar.getInstance();
//		calRecargaProxMes.setTime(dataRecarga);
//		calRecargaProxMes.add(Calendar.MONTH, 1);
//		calRecargaProxMes.add(Calendar.DAY_OF_MONTH, -1);
//		millisFim = calRecargaProxMes.getTime().getTime();
//		
//		//O numero de dias para expiracao e feito a partir da subtracao do dia anterior a data de concessao do bonus 
//		//para o proximo mes com a data de execucao atual ou com a data de execucao da concessao de bonus do mes atual
//		//(qual for a maior). E feita a extracao das duas datas em milissegundos para a subtracao. O numero 86400000
//		//corresponde ao numero de milissegundos em 1 dia.
//		int result = (int)((millisFim - millisInicio)/86400000);
//		
//		return new Integer(result);
//	}
	
	private short validaAssinante(HashMap infoAssinante, Date dataExecucao)
	{
		//Verifica se o assinante esta apto a receber o bonus
		int statusAssinante = ((Integer)infoAssinante.get("IDT_STATUS")).intValue();
		Date dataExpiracao = (Date)infoAssinante.get("DAT_EXPIRACAO_PRINCIPAL");
		int idPromocao = ((Integer)infoAssinante.get("IDT_PROMOCAO")).intValue();
//		String idtMsisdn = (String)infoAssinante.get("IDT_MSISDN");
//		Integer indSuspenso = ((Integer)infoAssinante.get("IND_SUSPENSO"));
//		Date datEntradaPromocao = (Date)infoAssinante.get("DAT_ENTRADA_PROMOCAO");

		//Validacao: Se o status do assinante nao for normal, o assinante nao esta apto
		if(statusAssinante != STATUS_NORMAL_USER)
		{
			return RET_STATUS_MSISDN_INVALIDO;
		}
		//Validacao: A promocao esta dentro do prazo de validade 
		Date dataInicioValidade = getDataInicioValidade(idPromocao);
		Date dataFimValidade = getDataFimValidade(idPromocao);
		if((dataExecucao.compareTo(dataInicioValidade) < 0) || (dataExecucao.compareTo(dataFimValidade) > 0))
		{
			return RET_PULA_PULA_PROMO_VALIDADE_NOK;
		}
		//Valicacao: Se a data de expiracao do assinante for menor que o ultimo dia de recarga para sua promocao, 
		//nao esta apto
		Date maxDataExecucaoRecarga = getMaxDataExecucaoRecarga(idPromocao, dataExecucao);
		if(dataExpiracao.compareTo(maxDataExecucaoRecarga) <= 0)
		{
			return RET_PULA_PULA_PREVISAO_MUD_STATUS;
		}
		
		return RET_OPERACAO_OK;
	}
	
	private double getValorAjuste(HashMap infoAssinante) throws Exception
	{
		//Calcula e retorna o valor de concessao de bonus
		double result = 0;
		
		String msisdn           = (String)infoAssinante.get("IDT_MSISDN");
		double minCredito       = ((Double)infoAssinante.get("MIN_CREDITO")).doubleValue();
		double minFF            = ((Double)infoAssinante.get("MIN_FF")).doubleValue(); 
		int idtCodigoNacional   = (new Integer(msisdn.substring(2, 4))).intValue();
		double vlrBonusMinuto   = getVlrBonusMinuto(idtCodigoNacional);
		double vlrBonusMinutoFF = getVlrBonusMinutoFF(idtCodigoNacional);
		
		result = ((minCredito - minFF)*vlrBonusMinuto) + (minFF*vlrBonusMinutoFF);
		
		return result;
	}
	
    private ArrayList atualizaListaPromocoes(ResultSet selPromocoes) throws SQLException
    {
    	//Atualiza uma lista de informacoes das promocoes para ser utilizada pelas threads
    	ArrayList result = new ArrayList();
    	
    	while(selPromocoes.next())
    	{
    		HashMap promocao = new HashMap(5);
    		promocao.put("IDT_PROMOCAO", new Integer(selPromocoes.getInt("IDT_PROMOCAO")));
    		promocao.put("NOM_PROMOCAO", selPromocoes.getString("NOM_PROMOCAO"));
    		promocao.put("DAT_INICIO_VALIDADE", selPromocoes.getDate("DAT_INICIO_VALIDADE"));
    		promocao.put("DAT_FIM_VALIDADE", selPromocoes.getDate("DAT_FIM_VALIDADE"));
    		promocao.put("VLR_MAX_CREDITO_BONUS", new Double(selPromocoes.getDouble("VLR_MAX_CREDITO_BONUS")));
		    //Se o valor lido foi NULL, indica que a promocao nao possui limite de credito
    		if(selPromocoes.wasNull()) promocao.put("VLR_MAX_CREDITO_BONUS", new Double(-1));
    		
			result.add(promocao);
    	}
    	
    	return result;
    }
	
    private ArrayList atualizaListaPromocaoDiaExecucao(ResultSet selPromocaoDiaExecucao) throws SQLException
    {
    	//Atualiza uma lista de informacoes de execucao das promocoes para ser utilizada pelas threads
    	ArrayList result = new ArrayList();
    	
    	while(selPromocaoDiaExecucao.next())
    	{
    		HashMap promocao = new HashMap(4);    		
    		promocao.put("IDT_PROMOCAO", new Integer(selPromocaoDiaExecucao.getInt("IDT_PROMOCAO")));
    		promocao.put("NUM_DIA_ENTRADA", new Integer(selPromocaoDiaExecucao.getInt("NUM_DIA_ENTRADA")));
    		promocao.put("NUM_DIA_EXECUCAO", new Integer(selPromocaoDiaExecucao.getInt("NUM_DIA_EXECUCAO")));
    		promocao.put("NUM_DIA_EXECUCAO_RECARGA", 
    				     new Integer(selPromocaoDiaExecucao.getInt("NUM_DIA_EXECUCAO_RECARGA")));
    		
			result.add(promocao);
    	}
    	
    	return result;
    }
    
    private ArrayList atualizaListaBonusPulaPula(ResultSet selBonusPulaPula) throws SQLException
	{
    	//Atualiza uma lista de fatores de conversao para calculo do Bonus Pula-Pula por Codigo Nacional
    	ArrayList result = new ArrayList();
    	
    	while(selBonusPulaPula.next())
    	{
    		HashMap bonusPulaPula = new HashMap(3);    		
    		bonusPulaPula.put("ID_CODIGO_NACIONAL", new Integer(selBonusPulaPula.getInt("ID_CODIGO_NACIONAL")));
    		bonusPulaPula.put("VLR_BONUS_MINUTO", new Double(selBonusPulaPula.getDouble("VLR_BONUS_MINUTO")));
    		bonusPulaPula.put("VLR_BONUS_MINUTO_FF", new Double(selBonusPulaPula.getDouble("VLR_BONUS_MINUTO_FF")));
    		
    		result.add(bonusPulaPula);
    	}
    	
    	return result;
	}

	private String getQuerySelecaoClientes()  
	{
		//Consulta que busca a lista de assinantes a receber o bonus
		String result = 
			"SELECT "
			+	"  P_ASSINANTE.IDT_MSISDN, " 
			+	"  P_ASSINANTE.IDT_PROMOCAO, "
			+	"  P_ASSINANTE.DAT_EXECUCAO, " 
			+	"  P_ASSINANTE.DAT_ENTRADA_PROMOCAO, "
			+	"  P_ASSINANTE.IND_SUSPENSO, "
			+	"  P_ASSINANTE.IND_ISENTO_LIMITE, "
			+	"  ASSINANTE.IDT_STATUS, " 
			+	"  ASSINANTE.VLR_SALDO_PRINCIPAL, " 
			+	"  ASSINANTE.DAT_EXPIRACAO_PRINCIPAL, " 
			+	"  PROMOCAO.IND_ZERAR_SALDO_BONUS, " 
			+	"  PROMOCAO.IND_ZERAR_SALDO_SMS, " 
			+	"  PROMOCAO.IND_ZERAR_SALDO_GPRS, " 
			//A divisao por 60 deve-se a alteracao da carga de CDRs para preencher os valores da 
			//TBL_GER_TOTALIZACAO_PULA_PULA em segundos e nao minutos, de forma a diminuir a perda de precisao.
			+	"  NVL(MINUTOS.MIN_CREDITO, 0)/60 AS MIN_CREDITO, " 
			+	"  NVL(MINUTOS.MIN_FF, 0)/60 AS MIN_FF " 
			+	"FROM "
			+	"  TBL_APR_ASSINANTE             ASSINANTE, "
			+	"  TBL_GER_PROMOCAO_ASSINANTE    P_ASSINANTE, " 
			+	"  TBL_GER_PROMOCAO              PROMOCAO, "
			+	"  TBL_GER_TOTALIZACAO_PULA_PULA MINUTOS "
			+	"WHERE "
			+	"  ASSINANTE.IDT_MSISDN = P_ASSINANTE.IDT_MSISDN           AND "
			+	"  P_ASSINANTE.IDT_PROMOCAO = PROMOCAO.IDT_PROMOCAO        AND "
			+	"  P_ASSINANTE.IDT_MSISDN = MINUTOS.IDT_MSISDN (+)         AND "
			+	"  P_ASSINANTE.DAT_EXECUCAO <= ?                           AND "
			+	"  PROMOCAO.IDT_CATEGORIA = " 
			+		String.valueOf(ID_CATEGORIA_PULA_PULA) + "             AND "
			+	"  MINUTOS.DAT_MES (+) = ?                                 AND "
			//PONTO DE ATENCAO: Verificar pela fila de recargas e nao pela tabela de recargas devido aos
			//agendamentos (Se a execucao na fila de recargas ainda nao tiver sido realizada, o registro na
			//fila sera duplicado)
			+	"  NOT EXISTS "
			+	"  ( "
			+	"    SELECT 1 " 
			+	"    FROM "
			+	"      TBL_REC_FILA_RECARGAS RECARGAS " 
			+	"    WHERE " 
			+	"      RECARGAS.IDT_MSISDN = P_ASSINANTE.IDT_MSISDN        AND "
			+	"      RECARGAS.TIP_TRANSACAO = ?                          AND "
			+	"      RECARGAS.IDT_STATUS_PROCESSAMENTO IN " 
			+	"      (" 
			+             String.valueOf(REC_STATUS_NAO_EXECUTADO)   + ", " 
			+             String.valueOf(REC_STATUS_OK)              + ", "
			+             String.valueOf(REC_STATUS_TESTE_PULA_PULA) 
			+	"      )                                                   AND "
			+	"      RECARGAS.DAT_EXECUCAO >= ?                          AND "
			+	"      RECARGAS.DAT_EXECUCAO <  ? "
			+	"  )                                                       AND "
			//Descartar assinantes que ja foram processados na data de execucao atual ou que nao possuem
			//ligacoes
			+	"  NOT EXISTS "
			+	"  ( "
			+	"    SELECT 1 " 
			+	"    FROM "
			+	"      TBL_GER_HISTORICO_PULA_PULA HISTORICO " 
			+	"    WHERE " 
			+	"      HISTORICO.IDT_MSISDN = P_ASSINANTE.IDT_MSISDN       AND "
			+	"      ( " 
			+	"        HISTORICO.DAT_EXECUCAO = ?                        OR "
			+	"        ( "
			+	"          HISTORICO.DAT_EXECUCAO BETWEEN ? AND ?          AND " 
			+	"          HISTORICO.IDT_CODIGO_RETORNO = " + String.valueOf(RET_PULA_PULA_LIGACOES_NOK) 
			+	"        ) "
			+	"      ) "
			+	"  ) ";
		
		return result;
	}
	
	private String getQuerySelecaoClientesRebarba()
	{
		//Consulta que busca a lista de assinantes que nao obtiveram recargas nas datas de execucao anteriores
		//Esta consulta retorna a selecao de assinantes que nao tiveram registros adicionados na fila de recargas
		//no mes da data de execucao
		String result = 
			"SELECT "
			+	"  P_ASSINANTE.IDT_MSISDN, " 
			+	"  P_ASSINANTE.IDT_PROMOCAO, "
			+	"  P_ASSINANTE.DAT_EXECUCAO, " 
			+	"  P_ASSINANTE.DAT_ENTRADA_PROMOCAO, "
			+	"  P_ASSINANTE.IND_SUSPENSO, "
			+	"  P_ASSINANTE.IND_ISENTO_LIMITE, "
			+	"  ASSINANTE.IDT_STATUS, " 
			+	"  ASSINANTE.VLR_SALDO_PRINCIPAL, " 
			+	"  ASSINANTE.DAT_EXPIRACAO_PRINCIPAL, " 
			+	"  PROMOCAO.IND_ZERAR_SALDO_BONUS, " 
			+	"  PROMOCAO.IND_ZERAR_SALDO_SMS, " 
			+	"  PROMOCAO.IND_ZERAR_SALDO_GPRS, " 
			//A divisao por 60 deve-se a alteracao da carga de CDRs para preencher os valores da 
			//TBL_GER_TOTALIZACAO_PULA_PULA em segundos e nao minutos, de forma a diminuir a perda de precisao.
			+	"  NVL(MINUTOS.MIN_CREDITO, 0)/60 AS MIN_CREDITO, " 
			+	"  NVL(MINUTOS.MIN_FF, 0)/60 AS MIN_FF " 
			+	"FROM "
			+	"  TBL_APR_ASSINANTE          ASSINANTE, "
			+	"  TBL_GER_PROMOCAO           PROMOCAO, " 
			+	"  TBL_GER_PROMOCAO_ASSINANTE P_ASSINANTE, " 
			+	"  TBL_GER_TOTALIZACAO_PULA_PULA MINUTOS "
			+	"WHERE "
			+	"  ASSINANTE.IDT_MSISDN = P_ASSINANTE.IDT_MSISDN           AND "
			+	"  P_ASSINANTE.IDT_PROMOCAO = PROMOCAO.IDT_PROMOCAO        AND "
			+	"  P_ASSINANTE.IDT_MSISDN = MINUTOS.IDT_MSISDN (+)         AND "
			+	"  P_ASSINANTE.IDT_PROMOCAO = ?                            AND "
			//Descartar assinantes que entraram na promocao no mes da execucao
			+	"  MINUTOS.DAT_MES (+) = ?                                 AND "
			+	"  P_ASSINANTE.DAT_ENTRADA_PROMOCAO < ?                    AND "
			+	"  PROMOCAO.IDT_CATEGORIA = " 
			+		String.valueOf(ID_CATEGORIA_PULA_PULA) + "             AND "
			//PONTO DE ATENCAO: Verificar pela fila de recargas e nao pela tabela de recargas devido aos
			//agendamentos (Se a execucao na fila de recargas ainda nao tiver sido realizada, o registro na
			//fila sera duplicado)
			+	"  NOT EXISTS "
			+	"  ( "
			+	"    SELECT 1 " 
			+	"    FROM "
			+	"      TBL_REC_FILA_RECARGAS RECARGAS " 
			+	"    WHERE " 
			+	"      RECARGAS.IDT_MSISDN = P_ASSINANTE.IDT_MSISDN        AND "
			+	"      RECARGAS.TIP_TRANSACAO = ?                          AND "
			+	"      RECARGAS.IDT_STATUS_PROCESSAMENTO IN " 
			+	"      (" 
			+				String.valueOf(REC_STATUS_NAO_EXECUTADO)   + ", " 
			+				String.valueOf(REC_STATUS_OK)              + ", "
			+				String.valueOf(REC_STATUS_TESTE_PULA_PULA) 
			+	"      )                                                   AND "
			+	"      RECARGAS.DAT_EXECUCAO >= ?                          AND "
			+	"      RECARGAS.DAT_EXECUCAO <  ? "
			+	"  )                                                       AND "
			//Descartar assinantes que nao possuem ligacoes
			+	"  NOT EXISTS "
			+	"  ( "
			+	"    SELECT 1 " 
			+	"    FROM "
			+	"      TBL_GER_HISTORICO_PULA_PULA HISTORICO " 
			+	"    WHERE " 
			+	"      HISTORICO.IDT_MSISDN = P_ASSINANTE.IDT_MSISDN       AND "
			+	"      HISTORICO.DAT_EXECUCAO BETWEEN ? AND ?              AND " 
			+	"      HISTORICO.IDT_CODIGO_RETORNO = " + String.valueOf(RET_PULA_PULA_LIGACOES_NOK) 
			+	"  ) ";

		return result;
	}
	
	private String getQuerySelecaoPromocoes()
	{
		//Consulta que busca a lista referente as promocoes Pula-Pula
		String result = 
			"SELECT " +
			"  PROMOCAO.IDT_PROMOCAO, " +
			"  PROMOCAO.NOM_PROMOCAO, " +
			"  PROMOCAO.DAT_INICIO_VALIDADE, " +
			"  PROMOCAO.DAT_FIM_VALIDADE, " +
			"  PROMOCAO.VLR_MAX_CREDITO_BONUS " +
		    "FROM " +
		    "  TBL_GER_PROMOCAO PROMOCAO " + 
		    "WHERE " +
		    "  PROMOCAO.IDT_CATEGORIA = " + String.valueOf(ID_CATEGORIA_PULA_PULA);
		
		return result;
	}
	
	private String getQuerySelecaoPromocaoDiaExecucao(String dataExecucao)
	{
		//Consulta que busca o mapeamento dos dias de execucao e de recarga de acordo com a promocao e o dia de 
		//entrada na promocao
		String result = 
			"SELECT " +
			"  DIA.IDT_PROMOCAO, " +
			"  DIA.NUM_DIA_ENTRADA, " +
			"  DIA.NUM_DIA_EXECUCAO, " +
			"  DIA.NUM_DIA_EXECUCAO_RECARGA " +
		    "FROM " +
		    "  TBL_GER_PROMOCAO_DIA_EXECUCAO DIA, " + 
		    "  TBL_GER_PROMOCAO              PROMOCAO " + 
		    "WHERE " +
		    "  PROMOCAO.IDT_PROMOCAO = DIA.IDT_PROMOCAO                    AND " +
		    "  PROMOCAO.IDT_CATEGORIA = " + String.valueOf(ID_CATEGORIA_PULA_PULA);
		
		return result;	  
	}
	
	private String getQuerySelecaoBonusPulaPula()
	{
		//Consulta que busca os valores de Bonus Pula-Pula por Codigo Nacional
		String result = 
			"SELECT " +
			"  ID_CODIGO_NACIONAL, " +
			"  VLR_BONUS_MINUTO, " +
			"  VLR_BONUS_MINUTO_FF " +
			"FROM " +
			"  TBL_GER_BONUS_PULA_PULA ";
		
		return result;
	}
	
	private String getInsertFilaRecarga(HashMap infoAssinante) 
	  throws ParseException
	{
		SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy");
		String tipoTransacao = TIPO_TRANSACAO_PULA_PULA;
		DecimalFormat conversorValor = new DecimalFormat("#,##0.00");
		int indZerarSaldoBonus = (infoAssinante.get("IND_ZERAR_SALDO_BONUS") != null) ? 
			((Integer)infoAssinante.get("IND_ZERAR_SALDO_BONUS")).intValue() : 0;
		int indZerarSaldoSms = (infoAssinante.get("IND_ZERAR_SALDO_SMS") != null) ? 
			((Integer)infoAssinante.get("IND_ZERAR_SALDO_SMS")).intValue() : 0;
		int indZerarSaldoGprs = (infoAssinante.get("IND_ZERAR_SALDO_GPRS") != null) ? 
			((Integer)infoAssinante.get("IND_ZERAR_SALDO_GPRS")).intValue() : 0;
		
		int promocao = ((Integer)infoAssinante.get("IDT_PROMOCAO")).intValue();
		String nomePromocao = getNomePromocao(promocao);
		String msisdn = (String)infoAssinante.get("IDT_MSISDN");
		double valorAjuste = ((Double)infoAssinante.get("VLR_AJUSTE")).doubleValue();
		Date dataExecucaoRecarga = (Date)infoAssinante.get("DAT_EXECUCAO_RECARGA");
		
		//A mensagem no SMS e diferente para o Pula-Pula 1 em relacao as outras promocoes. Alteracao feita 
		//conforme requisicao da equipe do Marketing (Tayna Oliveira).
		//Alteracao na promocao Pula-Pula comecando em 01/07/2005: Todos os Pula-Pulas com excecao do Pula-Pula 2004
		//devem ter o saldo de bonus zerado. Deve ser preenchida a flag na fila de recargas para que o processo de
		//consumo das recargas limpe o saldo antes de conceder o bonus.
		//Alteracao na promocao Pula-Pula comecando em 06/08/2005: Implementacao para zerar os saldos de SMS e dados
		String mensagemSms = null;
		if(promocao == ID_PROMOCAO_PULA_PULA)
		{
			mensagemSms = "Voce recebeu R$" + conversorValor.format(valorAjuste) + " pela " + nomePromocao + "." +
						  " Consulte seu saldo atraves do Menu BrT do seu celular.";
		}
		else
		{
			mensagemSms = "BrTGSM: Seu bonus Pula-Pula e de R$" + conversorValor.format(valorAjuste) + 
			              " com validade ate a data de recebimento do proximo mes. " +
						  "Garanta seus creditos ativos para o proximo mes." +
						  " Consulte seu saldo atraves do Menu BrT do seu celular.";
		}
		
		//Comando que insere os assinantes e seus bonus na fila de recargas
		String result =
				  "insert into tbl_rec_fila_recargas "
				+ "(idt_msisdn,"
				+ " tip_transacao,"
				+ " dat_cadastro,"
				+ " dat_execucao, "
				+ " dat_processamento,"
				+ " vlr_credito_bonus,"
				+ " des_mensagem,"
				+ " tip_sms, "
				+ " ind_envia_sms,"
				+ " idt_status_processamento,"
				+ " idt_codigo_retorno, "
				+ " ind_zerar_saldo_bonus, "
				+ " ind_zerar_saldo_sms, "
				+ " ind_zerar_saldo_gprs) "
				+ "values ('"
				+ msisdn + "', '"
				+ tipoTransacao +  "', "
				+ " sysdate, "
				//PONTO DE ATENCAO: Recarga deve ser consumida a partir de 1:00 da manha para dar tempo a
				//execucao da regua do Pre-Pago
				+ " to_date('"+ conversorData.format(dataExecucaoRecarga) + " 01:00:00', 'dd/mm/yyyy HH24:MI:SS'),"
				+ " null, "
				+ valorAjuste + ", "
				+ " '" + mensagemSms + "', "
				+ " 'PULAPULA', "
				+ " 1,"
				+ " 0, "
				+ " null, "
				+ " " + String.valueOf(indZerarSaldoBonus) + ","
				+ " " + String.valueOf(indZerarSaldoSms) + ","
				+ " " + String.valueOf(indZerarSaldoGprs) + ")";
		
		return result;
	}
	
	private String getUpdatePromocaoAssinante(HashMap infoAssinante) throws ParseException 
	{
		
		String result = null;
		SimpleDateFormat conversorData = new SimpleDateFormat("dd-MM-yyyy");

		//Promocao do assinante
		Integer idtPromocao = (Integer)infoAssinante.get("IDT_PROMOCAO");
		//Indicador de que a verificacao de recarga deve ser efetuada
		Integer indSuspenso = (Integer)infoAssinante.get("IND_SUSPENSO");
		//Proxima data de execucao
		Date dataExecucao = (Date)infoAssinante.get("DAT_EXECUCAO");

		result = 
			  "UPDATE  TBL_GER_PROMOCAO_ASSINANTE  "
			+ "SET "
			+ "  IND_SUSPENSO = " + ((indSuspenso == null) ? "NULL" : indSuspenso.toString()) + ", "
			+ "  DAT_EXECUCAO = TO_DATE('" + conversorData.format(dataExecucao) + "', 'DD-MM-RRRR') "
			+ "WHERE "
			+ "	 IDT_MSISDN = '" + (String)infoAssinante.get("IDT_MSISDN") + "' AND "
			+ "  IDT_PROMOCAO = " + String.valueOf(idtPromocao.intValue());
		
		return result;
	}
	
	private void atualizaHistoricoPulaPula(HashMap infoAssinante, Date datExecucao, Connection con)
		throws SQLException
	{
		//Atualiza o historico da execucao do Pula-Pula para o assinante em questao

		//Obtendo os parametros necessarios para o Insert
		String idtMsisdn = (String)infoAssinante.get("IDT_MSISDN");
		Integer idtPromocao = (Integer)infoAssinante.get("IDT_PROMOCAO");
		Integer idtCodigoRetorno = (Integer)infoAssinante.get("IDT_CODIGO_RETORNO");
		String desStatusExecucao = (idtCodigoRetorno.intValue() == RET_ERRO_TECNICO) ? "ERRO" : "SUCESSO";
		Double vlrAjuste = (Double)infoAssinante.get("VLR_AJUSTE");
		
		PreparedStatement prepInsert = null;
		String sqlInsert = "INSERT INTO TBL_GER_HISTORICO_PULA_PULA " +
						   "  (IDT_MSISDN, IDT_PROMOCAO, DAT_EXECUCAO, DES_STATUS_EXECUCAO, " +
						   "   IDT_CODIGO_RETORNO, VLR_CREDITO_BONUS) " +
						   "VALUES " +
						   "  (?, ?, ?, ?, ?, ?)";

		//Executando o Insert
		try
		{
			prepInsert = con.prepareStatement(sqlInsert);
			prepInsert.setString(1, idtMsisdn);
			prepInsert.setInt(2, idtPromocao.intValue());
			prepInsert.setDate(3, new java.sql.Date(datExecucao.getTime()));
			prepInsert.setString(4, desStatusExecucao);
			prepInsert.setString(5, (new DecimalFormat("0000")).format(idtCodigoRetorno.intValue()));
			prepInsert.setDouble(6, (vlrAjuste == null) ? 0.0 : vlrAjuste.doubleValue());
			prepInsert.executeUpdate();
		}
		catch(Exception e)
		{
			log("Exception : MSISDN: " + idtMsisdn + " : " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(prepInsert != null) prepInsert.close();
			prepInsert = null;
		}
	}

	private void atualizaHistoricoPendenteRecarga(Date datExecucao) throws SQLException
	{
		//Atualiza o historico dos assinantes que estao na Promocao 0 (Pendentes de primeira recarga)
		
		//Verifica se o dia de execucao e 11 ou 16 
		//(ultimos dias de concessao de bonus para as promocoes Pula-Pula 2 e 3)
		Calendar calExecucao = Calendar.getInstance();
		calExecucao.setTime(datExecucao);
		int diaExecucao = calExecucao.get(Calendar.DAY_OF_MONTH);
		Connection connClientes = null;
//		PreparedStatement prepClientes = null;
		PreparedStatement prepInsert = null;
//		ResultSet resultClientes = null;
		
		//Parametros para consulta no banco de dados de assinantes pendentes de primeira recarga 
		String datEntradaPromocao = null;
		
		switch(diaExecucao)
		{
			case 11:
				datEntradaPromocao = "DAT_ENTRADA_PROMOCAO >= TO_DATE('01/01/2005', 'DD/MM/YYYY') AND " +
									 "DAT_ENTRADA_PROMOCAO <  TO_DATE('09/02/2005', 'DD/MM/YYYY') ";
				break;
			case 16:
				datEntradaPromocao = "DAT_ENTRADA_PROMOCAO >= TO_DATE('09/02/2005', 'DD/MM/YYYY') AND " +
				 					 "DAT_ENTRADA_PROMOCAO <  TO_DATE('01/04/2005', 'DD/MM/YYYY') ";
				break;
			case 21:
				datEntradaPromocao = "DAT_ENTRADA_PROMOCAO >= TO_DATE('01/04/2005', 'DD/MM/YYYY') AND " +
				 					 "DAT_ENTRADA_PROMOCAO <  TO_DATE('01/07/2005', 'DD/MM/YYYY') ";
				break;
			//Caso contrario, nao faz nada e termina a execucao
			default:
				return;
		}
		
		try
		{
			//Inserindo registros no historico
			String sqlInsert = "INSERT INTO TBL_GER_HISTORICO_PULA_PULA " +
		   	   				   "(IDT_MSISDN, IDT_PROMOCAO, DAT_EXECUCAO, DES_STATUS_EXECUCAO, " +
							   " IDT_CODIGO_RETORNO, VLR_CREDITO_BONUS) " +
							   "SELECT IDT_MSISDN, IDT_PROMOCAO, ?, 'SUCESSO', ?, 0 " +
							   "FROM TBL_GER_PROMOCAO_ASSINANTE " +
							   "WHERE IDT_PROMOCAO = " + String.valueOf(ID_PENDENTE_RECARGA) + 
							   " AND " + datEntradaPromocao;
			
			connClientes = DriverManager.getConnection("jdbc:oracle:oci8:@" + sid, usuario,senha);
			prepInsert = connClientes.prepareStatement(sqlInsert);
			prepInsert.setDate  (1, new java.sql.Date(datExecucao.getTime()));
			prepInsert.setString(2, (new DecimalFormat("0000")).format(RET_PULA_PULA_PENDENTE_RECARGA));
			prepInsert.executeUpdate();
		}
		finally
		{
			if(prepInsert != null) prepInsert.close();
			if(connClientes != null) connClientes.close();
		}
	}
	
	private Date mesAnterior(Date data) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	
	private Date proximoMes(Date data)
	{
		Calendar calResult = Calendar.getInstance();
		calResult.setTime(data);
		calResult.add(Calendar.MONTH, 1);
		return calResult.getTime();
	}

	private Date getUltimoDiaMes(Date data) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}
	
	private Date getPrimeiroDiaMes(Date data) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);

		return cal.getTime();
	}

}
