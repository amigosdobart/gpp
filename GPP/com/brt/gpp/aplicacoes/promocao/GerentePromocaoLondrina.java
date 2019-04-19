package com.brt.gpp.aplicacoes.promocao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargasDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
*
* Esta classe e a responsavel por cadastrar os assinantes da promocao do aniversario de londrina
*
* <P> Versao:			1.0
*
* @Autor: 			Joao Carlos
* Data: 			08/12/2004
*
* Modificado por:
* Data:
* Razao:
*
*/

public class GerentePromocaoLondrina extends Aplicacoes
{
	private GerentePoolBancoDados	gerenteBancoDados 	= null;
	private DecimalFormat			df					= null;

	private static final long   ID_PROMOCAO_LONDRINA		 = 3;
	private static final String TIPO_TRANSACAO_CREDITO		 = "08002";
	private static final String CN_LONDRINA					 = "43";

	/**
	 * Metodo....:GerentePromocaoLondrina
	 * Descricao.:Este metodo e o construtor da classe
	 * @param logId - Id do processo que esta iniciando a instancia
	 */
	public GerentePromocaoLondrina(long logId)
	{
		super(logId, "GerentePromocaoLondrina");
		gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		df                = new DecimalFormat("##0.00");
	}

	/**
	 * Metodo....:getPromocao
	 * Descricao.:Busca os dados definidos para a promocao
	 * @param idPromocao	- Codigo da promocao
	 * @return Promocao		- Objeto contendo informacoes da promocao
	 * @throws GPPInternalErrorException
	 */
	public Promocao getPromocao(long idPromocao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"getPromocao","Inicio IDPROMOCAO "+idPromocao);

		PREPConexao conexaoPrep = null;
		Promocao promocao = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlBuscaProm = 	"SELECT  IDT_PROMOCAO,NOM_PROMOCAO,DES_PROMOCAO,DAT_INICIO,DAT_FIM " +
									       ",DAT_INICIO_VALIDADE,DAT_FIM_VALIDADE " +
									       ",VLR_BONUS " +
									  "FROM TBL_PRO_PROMOCAO " +
									 "WHERE IDT_PROMOCAO = ? ";
			Object param[] = {new Long(idPromocao)};

			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlBuscaProm,param,super.getIdLog());
			while (rs.next())
			{
				promocao = new Promocao();
				promocao.setId				(				 rs.getLong		("IDT_PROMOCAO")						);
				promocao.setNome			(				 rs.getString	("NOM_PROMOCAO")						);
				promocao.setDescricao		(				 rs.getString	("DES_PROMOCAO")						);
				//promocao.setTipo			(				 rs.getInt		("IND_TIPO")							);
				//promocao.setValorPorMinuto	(				 rs.getDouble	("VAL_POR_MINUTO")						);
				promocao.setValorBonus      (                rs.getDouble   ("VLR_BONUS")							);
				promocao.setInicioEntrada	((java.util.Date)rs.getTimestamp("DAT_INICIO")							);
				promocao.setFimEntrada		((java.util.Date)rs.getTimestamp("DAT_FIM")								);
				promocao.setInicioValidade	((java.util.Date)rs.getTimestamp("DAT_INICIO_VALIDADE")					);
				promocao.setFimValidade		((java.util.Date)rs.getTimestamp("DAT_FIM_VALIDADE")					);
			//	promocao.setHibrido			(				 rs.getInt		("IND_HIBRIDO")     == 1 ? true : false	);
				//promocao.setMesFechado		(				 rs.getInt		("IND_MES_FECHADO") == 1 ? true : false	);
			}
		}
		catch(SQLException e)
		{
			super.log(Definicoes.WARN,"getPromocao","ERRO SQL:Nao foi possivel pesquisar os dados da promocao "+idPromocao+". Erro"+e);
			throw new GPPInternalErrorException(e.getMessage());
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG,"getPromocao","Fim RETORNO:"+promocao);
		return promocao;
	}

	/**
	 * Metodo....:insereAssinantePromocao
	 * Descricao.:Este metodo realiza o insert do assinante na tabela de promocao
	 * @param msisdn		- Msisdn do assinante
	 * @param promocao		- Dados da promocao a qual o assinante sera incluido
	 * @param conexaoPrep	- Conexao PREP de banco de dados
	 * @throws GPPInternalErrorException
	 */
	private void insereAssinantePromocao(String msisdn, Promocao promocao, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"insereAssinantePromocao","Inicio MSISDN "+msisdn);

		String sqlInsert = "INSERT INTO TBL_PRO_ASSINANTE " +
		                   "(IDT_MSISDN, IDT_PROMOCAO, DAT_EXECUCAO, " +
						   " DAT_ENTRADA_PROMOCAO, IND_ISENTO_LIMITE " +
						   " DAT_INICIO_ANALISE, DAT_FIM_ANALISE) " +
						   "VALUES(?,?,?,?,NULL,?,?)";

		Calendar proxMes = Calendar.getInstance();
		proxMes.add(Calendar.MONTH,1);
		Calendar ultDiaMes = Calendar.getInstance();
		ultDiaMes.set(Calendar.DAY_OF_MONTH,ultDiaMes.getMaximum(Calendar.DAY_OF_MONTH));

		// Os parametros de data sao os seguintes:
		// - Data da execucao e um mes apos o dia do cadastro
		// - Data da entrada na promocao e o dia do cadastro (data atual)
		// - Data de Inicio da analise e a data de cadastro para a primeira analise, apos isso
		//   a atualizacao ira marcar o dia 1 e o ultimo dia de cada mes
		// - Data de fim da analise e o ultimo dia do mes do cadastro, para a atualizacao mensal
		//   sera o ultimo dia do mes em execucao
		Object param[] = { msisdn														// IDT_MSISDN
				          ,new Long(promocao.getId())									// IDT_PROMOCAO
						  ,new java.sql.Date(proxMes.getTimeInMillis())					// DAT_EXECUCAO
						  ,new java.sql.Date(Calendar.getInstance().getTimeInMillis())	// DAT_ENTRADA_PROMOCAO
						  //,new Integer(0)												// IND_SUSPENSO
						  ,new java.sql.Date(Calendar.getInstance().getTimeInMillis())	// DAT_INICIO_ANALISE
						  ,new java.sql.Date(ultDiaMes.getTimeInMillis())				// DAT_FIM_ANALISE
				         };

		conexaoPrep.executaPreparedUpdate(sqlInsert,param,super.getIdLog());
		super.log(Definicoes.DEBUG,"insereAssinantePromocao","Fim");
	}

	/**
	 * Metodo....:cadastraAssinantes
	 * Descricao.:Este metodo realiza a busca dos assinantes elegiveis a serem cadastros na promocao
	 * @param double - Valor do bonus inicial de cadastro da promocao
	 * @throws GPPInternalErrorException
	 */
	public void cadastraAssinantes(double valorBonus) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"cadastraAssinantes","Inicio VLRBONUS "+valorBonus);
		// Busca os dados relativos a promocao
		Promocao promocao = getPromocao(GerentePromocaoLondrina.ID_PROMOCAO_LONDRINA);
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
            FilaRecargasDAO dao = new FilaRecargasDAO(conexaoPrep);
            
			String sqlPesquisa =
				"SELECT DISTINCT E.IDT_MSISDN " +
				  "FROM TBL_APR_EVENTOS E " +
				 "WHERE E.TIP_OPERACAO          = ? " +
				   "AND E.IDT_ANTIGO_CAMPO      = ? " +
				   "AND SUBSTR(E.IDT_MSISDN,3,2)= ? " +
				   "AND E.DAT_APROVISIONAMENTO >= ? " +
				   "AND E.DAT_APROVISIONAMENTO <= ? " +
				   "AND NOT EXISTS (SELECT 1 " +
				                     "FROM TBL_APR_EVENTOS B " +
				                    "WHERE B.IDT_MSISDN       = E.IDT_MSISDN " +
				                      "AND B.TIP_OPERACAO     = ? " +
				                      "AND B.IDT_ANTIGO_CAMPO = ? " +
				                      "AND B.DAT_APROVISIONAMENTO < E.DAT_APROVISIONAMENTO " +
				                      "AND B.DAT_APROVISIONAMENTO > (SELECT MAX(DAT_APROVISIONAMENTO) " +
				                                                      "FROM TBL_APR_EVENTOS C " +
				                                                     "WHERE C.TIP_OPERACAO         = ? " +
				                                                       "AND C.DES_STATUS           = ? " +
				                                                       "AND C.IDT_MSISDN           = E.IDT_MSISDN " +
				                                                       "AND C.DAT_APROVISIONAMENTO < E.DAT_APROVISIONAMENTO " +
				                                                    ") " +
				                   ") " +
				   "AND NOT EXISTS (SELECT 1 " +
				                     "FROM TBL_PRO_ASSINANTE P " +
				                    "WHERE P.IDT_PROMOCAO = ? " +
				                      "AND P.IDT_MSISDN   = E.IDT_MSISDN " +
				                   ") ";

			Object param[] = {Definicoes.TIPO_APR_STATUS_NORMAL
					         ,Definicoes.TIPO_APR_STATUS_FIRSTIME
							 ,GerentePromocaoLondrina.CN_LONDRINA
							 ,promocao.getInicioEntrada()
							 ,promocao.getFimEntrada()
							 ,Definicoes.TIPO_APR_STATUS_NORMAL
					         ,Definicoes.TIPO_APR_STATUS_FIRSTIME
							 ,Definicoes.TIPO_APR_ATIVACAO
							 ,Definicoes.TIPO_OPER_SUCESSO
							 ,new Long(GerentePromocaoLondrina.ID_PROMOCAO_LONDRINA)
							 };

			// Realiza a pesquisa dos assinantes elegiveis e para cada assinante e realizado
			// o insert na tabela de promocoes e tambem o credito inicial (1 parcela)
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,param,super.getIdLog());
			while (rs.next())
			{
				String msisdn = rs.getString("IDT_MSISDN");

				//Realiza a recarga da primeira parcela, juntamente com o envio do SMS
				FilaRecargas filaRecargas = new FilaRecargas();
				filaRecargas.setIdtMsisdn(msisdn);
				filaRecargas.setTipTransacao(GerentePromocaoLondrina.TIPO_TRANSACAO_CREDITO);
				filaRecargas.setDatExecucao(new Timestamp(new Date().getTime()));
				filaRecargas.setVlrCreditoPrincipal(new Double(valorBonus));
				filaRecargas.setVlrCreditoBonus(new Double(0.0));
				filaRecargas.setVlrCreditoSms(new Double(0.0));
				filaRecargas.setVlrCreditoGprs(new Double(0.0));
				filaRecargas.setNumDiasExpPrincipal(new Integer(0));
				filaRecargas.setNumDiasExpBonus(new Integer(0));
				filaRecargas.setNumDiasExpSms(new Integer(0));
				filaRecargas.setNumDiasExpGprs(new Integer(0));
				filaRecargas.setNumPrioridade(new Integer(Definicoes.SMS_PRIORIDADE_UM));
				filaRecargas.setDesMensagem(getMensagemCadastroPromocao(valorBonus));
				filaRecargas.setTipSms("PromocaoLondrina");
				filaRecargas.setIndEnviaSms(new Integer(1));
				filaRecargas.setIndZerarSaldoBonus(new Integer(0));
				filaRecargas.setIndZerarSaldoGprs(new Integer(0));
				filaRecargas.setIndZerarSaldoSms(new Integer(0));

                dao.insereRecargaNaFila(filaRecargas);
				insereAssinantePromocao(msisdn,promocao,conexaoPrep);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"cadastraAssinantes","Nao foi possivel realizar o cadastramento de assinantes. Erro:"+e);
			throw new GPPInternalErrorException(e.toString());
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG,"cadastraAssinantes","Inicio VLRBONUS "+valorBonus);
	}

	/**
	 * Metodo....:getMensagemCadastroPromocao
	 * Descricao.:Mensagem inicial de cadastro da promocao que sera enviada aos assinantes
	 * @param valorBonus	- Valor Inicial do Bonus
	 * @return
	 */
	private String getMensagemCadastroPromocao(double valorBonus)
	{
		return "BrTGSM Informa: Adicionalmente aos creditos iniciais de R$10.00, voce recebeu mais R$ "+df.format(valorBonus)+" da promocao Aniversario de Londrina";
	}

	/**
	 * Metodo....:getMensagemPromocao
	 * Descricao.:Este metodo retorna a mensagem a ser utilizada na promocao
	 * @param valorBonus	- Valor Mensal do bonus
	 * @return String - Mensagem SMS a ser enviada para os assinantes
	 */
	private String getMensagemPromocao(double valorBonus)
	{
		return "BrTGSM Informa. Prezado Cliente: Voce recebeu R$ "+df.format(valorBonus)+" do Bonus Mensal, da promocao Aniversario de Londrina. Nao perca seu bonus faca uma recarga mensal";
	}

	/**
	 * Metodo....:getMensagemAvisoRecarga
	 * Descricao.:Este metodo retorna a mensagem SMS a ser enviada como aviso da falta de recarga
	 * @return String - Mensagem SMS avisando que falta a recarga no periodo
	 */
	private String getMensagemAvisoRecarga()
	{
		return "BrTGSM Informa. Prezado Cliente: Nao identificamos recarga neste mes. Nao perca seu bonus faca uma recarga ate o dia 30 e receba sua parcela do bonus.";
	}

	/**
	 * Metodo....:insereRecargasSMS
	 * Descricao.:Este Metodo realiza o insert na fila de recargas da promocao Londrina
	 * @param dataExecucao	- Data de execucao dos assinantes da promocao
	 * @param promocao		- Dados da Promocao
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @throws SQLException
	 * @throws GPPInternalErrorException
	 */
	private void insereRecargasSMS(Date dataExecucao, Promocao promocao, PREPConexao conexaoPrep) throws SQLException,GPPInternalErrorException
	{
		String sql = "insert into tbl_rec_fila_recargas " +
					"(idt_msisdn,tip_transacao,dat_cadastro,dat_execucao, " +
					 "dat_processamento,vlr_credito_principal,vlr_credito_bonus,vlr_credito_sms," +
					 "vlr_credito_gprs,num_dias_exp_principal,num_dias_exp_bonus,num_dias_exp_sms," +
					 "num_dias_exp_gprs,des_mensagem,tip_sms, " +
					 "ind_envia_sms,idt_status_processamento,idt_codigo_retorno) " +
			        "select pa.idt_msisdn       as idt_msisdn " +
					       ",?                  as tip_transacao " +
					       ",sysdate            as dat_cadastro " +
					       ",sysdate            as dat_execucao " +
					       ",null               as dat_processamento " +
					       ",0                  as vlr_credito_principal " +
						   ",?                  as vlr_credito_bonus " +
						   ",0                  as vlr_credito_sms " +
						   ",0                  as vlr_credito_gprs " +
						   ",0                  as num_dias_exp_principal " +
						   ",0                  as num_dias_exp_bonus " +
						   ",0                  as num_dias_exp_sms " +
						   ",0                  as num_dias_exp_gprs " +
					       ",?                  as des_mensagem " +
					       ",'PromocaoLondrina' as tip_sms " +
					       ",1                  as ind_envia_sms " +
					       ",?                  as idt_status_processamento " +
					       ",null               as idt_codigo_retorno " +
					  "from tbl_pro_assinante pa " +
					 "where pa.idt_promocao       = ? " +
					   //"and pa.ind_suspenso       = 0 " +
					   "and pa.dat_execucao       = ? " +
					  // "and pa.dat_saida_promocao is null " +
					   "and exists (select 1 " +
					                 "from tbl_rec_recargas r " +
					                "where r.id_tipo_recarga = ? " +
					                  "and r.dat_recarga    >= ? " +
					                  "and r.dat_recarga    <= ? " +
					                  "and r.idt_msisdn      = pa.idt_msisdn " +
					               ") ";

		Object param[] = {GerentePromocaoLondrina.TIPO_TRANSACAO_CREDITO,
				          new Double(promocao.getValorBonus()),
						  getMensagemPromocao(promocao.getValorBonus()),
						  new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA),
						  new Long(GerentePromocaoLondrina.ID_PROMOCAO_LONDRINA),
						  new java.sql.Date(dataExecucao.getTime()),
						  Definicoes.TIPO_RECARGA,
						  new java.sql.Date(getDataInicioAnalise(dataExecucao).getTime()),
						  new java.sql.Date(getDataFimAnalise(dataExecucao).getTime())
				         };

		conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
	}

	/**
	 * Metodo....:atualizaDadosPromocao
	 * Descricao.:Este metodo atualiza os dados da promocao para os assinantes definindo as datas
	 *            de analise e execucao futura
	 * @param dataExecucao	- Data de execucao atual
	 * @param promocao		- Dados da promocao
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @throws SQLException
	 * @throws GPPInternalErrorException
	 */
	private void atualizaDadosPromocao(Date dataExecucao, Promocao promocao, PREPConexao conexaoPrep) throws SQLException,GPPInternalErrorException
	{
		// Define a nova data de exeucao, sendo a mesma da de execucao porem no mes posterior
		Calendar novaDataExecucao = Calendar.getInstance();
		novaDataExecucao.setTime(dataExecucao);
		novaDataExecucao.add(Calendar.MONTH,1);

		// Define a nova data inicio de analise sendo o primeiro dia do mes da data de execucao
		Calendar novaDataInicioAnalise = Calendar.getInstance();
		novaDataInicioAnalise.setTime(novaDataExecucao.getTime());
		novaDataInicioAnalise.set(Calendar.DAY_OF_MONTH,novaDataInicioAnalise.getActualMinimum(Calendar.DAY_OF_MONTH));

		// Define a nova data fim de analise sendo o ultimo dia do mes da data de execucao
		Calendar novaDataFimAnalise = Calendar.getInstance();
		novaDataFimAnalise.setTime(novaDataInicioAnalise.getTime());
		novaDataFimAnalise.set(Calendar.DAY_OF_MONTH,novaDataFimAnalise.getActualMaximum(Calendar.DAY_OF_MONTH));

		String sql = "update tbl_pro_assinante " +
		                "set dat_execucao       = ? " +
						   ",dat_inicio_analise = ? " +
						   ",dat_fim_analise    = ? " +
					  "where dat_execucao       = ? " +
					    "and idt_promocao       = ? " ;
					    //"and ind_suspenso       = 0 " +
						//"and dat_saida_promocao is null ";

		Object param[] = {new java.sql.Date(novaDataExecucao.getTimeInMillis()),
				          new java.sql.Date(novaDataInicioAnalise.getTimeInMillis()),
						  new java.sql.Date(novaDataFimAnalise.getTimeInMillis()),
						  new java.sql.Date(dataExecucao.getTime()),
						  new Long(GerentePromocaoLondrina.ID_PROMOCAO_LONDRINA),
				         };

		conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
	}

	/**
	 * Metodo....:executarPromocaoLondrina
	 * Descricao.:Este metodo e o responsavel pela bonificao dos assinantes da promocao Londrina
	 *            A data passada como parametro deve ser uma data dentro do mes a ser processado.
	 *            Todos os assinantes cadastrados na promocao receberao o bonus
	 * @param dataReferencia	- Data de referencia para a execucao
	 * @throws GPPInternalErrorException
	 */
	public void executarPromocaoLondrina(String dataReferencia) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"executarPromocaoLondrina","Inicio DATA "+dataReferencia);
		PREPConexao conexaoPrep = null;
		// Busca os dados relativos a promocao
		Promocao promocao = getPromocao(GerentePromocaoLondrina.ID_PROMOCAO_LONDRINA);
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			conexaoPrep.setAutoCommit(false);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dataExecucao    = sdf.parse(dataReferencia);

			// Realiza a concessao de creditos da promocao, inserindo os dados na fila de recargas
			insereRecargasSMS(dataExecucao, promocao,conexaoPrep);

			// Apos ter realizado as recargas com sucesso entao atualiza as informacoes da promocao
			// principalmente com relacao as datas de proxima execucao e analise
			atualizaDadosPromocao(dataExecucao, promocao,conexaoPrep);

			// Apos ter realizado os dois procedimentos, entao efetiva a gravacao em banco de dados
			conexaoPrep.commit();
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data de referencia esta no formato invalido. Data Informada:"+dataReferencia);
		}
		catch(SQLException ge)
		{
			super.log(Definicoes.WARN,"executarPromocaoLondrina","Erro(SQL) ao realizar a execucao da promocao londrina para a data:"+dataReferencia+". Erro"+ge);
			throw new GPPInternalErrorException(ge.toString());
		}
		finally
		{
			try
			{
				// Executa o rollback no finally pois se uma vez o commit for executado primeiro
				// entao esse comando nao tem nenhum efeito, porem caso o commit nao tenha sido
				// realizado entao esse comando desfaz todas as transacoes efetuadas
				if(conexaoPrep != null)
				{
					conexaoPrep.rollback();
					conexaoPrep.setAutoCommit(true);
				}
			}
			catch(SQLException e)
			{
				throw new GPPInternalErrorException("Nao foi possivel voltar conexao para autocommit true. Erro:"+e);
			}
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG,"executarPromocaoLondrina","Fim");
	}

	/**
	 * Metodo....:avisaRecargaPromocaoLondrina
	 * Descricao.:Este metodo envia um SMS para os assinantes que nao realizaram recarga no periodo
	 *            relativo ao proximo bonus da promocao.
	 * @param dataReferencia	- Data de referencia para execucao
	 * @throws GPPInternalErrorException
	 */
	public void avisaRecargaPromocaoLondrina(String dataReferencia) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"avisaRecargaPromocaoLondrina","Inicio DATA "+dataReferencia);
		PREPConexao conexaoPrep = null;
		try
		{
			// Busca a referencia para a gravacao de mensagem SMS
			ConsumidorSMS enviaSMS = ConsumidorSMS.getInstance(super.getIdLog());

			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlPesquisa =    "select p.idt_msisdn as idt_msisdn " +
									  "from tbl_pro_assinante p " +
									  "where p.idt_promocao       = ? " +
									    //"and p.ind_suspenso       = 0 " +
										"and p.dat_execucao       = ? " +
									    //"and p.dat_saida_promocao is null " +
									    "and not exists (select 1 " +
									                      "from tbl_rec_recargas r " +
									                     "where r.id_tipo_recarga = ? " +
									                       "and r.dat_recarga    >= ? " +
									                       "and r.dat_recarga    <  ? " +
														   "and r.idt_msisdn      = p.idt_msisdn " +
									                    ") ";

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dataExecucao    = sdf.parse(dataReferencia);
			Object param[] = {new Long(GerentePromocaoLondrina.ID_PROMOCAO_LONDRINA),
					          new java.sql.Date(dataExecucao.getTime()),
							  Definicoes.TIPO_RECARGA,
							  new java.sql.Date(getDataInicioAnalise(dataExecucao).getTime()),
							  new java.sql.Date(getDataFimAnalise(dataExecucao).getTime()),
							 };

			// Realiza a pesquisa dos assinantes cadastrados na promocao que nao realizaram
			// nenhuma recarga no periodo vigente de processamento. Esses assinantes irao
			// receber um aviso via SMS para que efetuem uma recarga ou entao nao receberao
			// o bonus.
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,param,super.getIdLog());
			while (rs.next())
				// Grava a mensagem de SMS de aviso da falta de recarga para concessao de bonus
				enviaSMS.gravaMensagemSMS(rs.getString("IDT_MSISDN"),getMensagemAvisoRecarga(),Definicoes.SMS_PRIORIDADE_UM,"PromocaoLondrina",super.getIdLog());
		}
		catch(SQLException se)
		{
			super.log(Definicoes.WARN,"avisaRecargaPromocaoLondrina","Erro(SQL) ao realizar o envio de aviso da falta de recarga para a promocao londrina. Erro"+se);
			throw new GPPInternalErrorException(se.toString());
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data de referencia esta no formato invalido. Data Informada:"+dataReferencia);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG,"avisaRecargaPromocaoLondrina","Fim");
	}

	/**
	 * Metodo....:getDataInicioAnalise
	 * Descricao.:Retorna a data de inicio de analise baseada na data de execucao
	 * @param dataExecucao	- Data de execucao
	 * @return Date - Data de inicio da analise
	 */
	private Date getDataInicioAnalise(Date dataExecucao)
	{
		// Data de Inicio da analise sera baseada no primeiro dia do mes anterior a data de execucao
		Calendar datMesAnterior = Calendar.getInstance();
		datMesAnterior.setTime(dataExecucao);
		datMesAnterior.add(Calendar.MONTH,-1);
		datMesAnterior.set(Calendar.DAY_OF_MONTH,datMesAnterior.getActualMinimum(Calendar.DAY_OF_MONTH));

		return datMesAnterior.getTime();
	}

	/**
	 * Metodo....:getDataFimAnalise
	 * Descricao.:Retorna a data final de analise baseada na data de execucao
	 * @param dataExecucao	- Data de execucao
	 * @return Date - Data final da analise
	 */
	private Date getDataFimAnalise(Date dataExecucao)
	{
		// A data final e o primeiro dia do mes da data atual. Isso e devido a analise ser feita
		// nas recargas utilizar hora entao o periodo final e zero horas do primeiro do dia do proximo
		// mes. Ex: Analise de Dezembro de 2004 vai de 01-dez-2004 (inclusive) a 01-jan-2005 (exclusive)
		Calendar datMesAtual = Calendar.getInstance();
		datMesAtual.setTime(dataExecucao);
		//datMesAtual.add(Calendar.MONTH,-1);
		datMesAtual.set(Calendar.DAY_OF_MONTH,datMesAtual.getActualMinimum(Calendar.DAY_OF_MONTH));

		return datMesAtual.getTime();
	}
}
