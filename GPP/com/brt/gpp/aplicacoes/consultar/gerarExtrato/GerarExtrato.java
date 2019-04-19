// Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.gerarExtrato;

//Arquivos de java
import java.sql.*;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.text.ParseException;
//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.aplicacoes.*;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
//import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.aplicacoes.consultar.gerarExtrato.Detalhe;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Exceções GPP
import com.brt.gpp.comum.gppExceptions.*;

// Arquivos TECNOMEN
//import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
//import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
//import TINC.Pi_exception;

/**
  *
  * Este arquivo refere-se a classe GerarExtrato, responsavel pela implementacao das
  * logicas de busca e formatacao dos dados do comprovante de servico
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				28/02/2004
  *
  * Modificado por:		Daniel Abib
  * Data:				26/04/2004
  * Razao:				Correcoes e padronizacao de codigo
  * 
  * Modificado por: Alberto Magno
  * Data: 15/10/2004
  * Razao: Múltiplo Saldo.
  * 
  * Modificado por : Luciano Vilela
  * Data : 13/04/2005
  * Razao : Todas as strings de sql foram declaradas como final static 
  * com o objetivo de melhorar a utilizacao de memoria
  *
  */
public class GerarExtrato extends Aplicacoes 
{
	private final static String superQueryStatic =  "SELECT " +
												"numeroOrigem," +//1
												"data," +//2
												"hora," +//3
												"tipo," +//4
												"operacao," +//5
												"regiaoOrigem," +//6
												"regiaoDestino, "+//7
												"numeroDestino," +//8
												"duracao," +//9
												"valor," +//10
												"saldo,"+//11
												"bonus," +//12
												"bonus_saldo," +//13
												"GPRS," +//14
												"GPRS_saldo," +//15
												"SMS," +//16
												"SMS_saldo," +//17
												"periodico, " +//18
												"periodico_saldo, "+//19
												"total," +//20
												"total_saldo,"+//21
												"TIPO_REGISTRO,"+//22
												"csp, " +//23
												"descricaoComplemento"; //24

	
		private final static String queryCDRStatic = "SELECT " + 
						"substr(A.SUB_ID,3) as numeroOrigem, " + //1numero origem
						"to_char(A.TIMESTAMP,'DD/MM/YYYY') as data, " + //2data
						"to_char(A.TIMESTAMP,'HH24:MI:SS') as hora, " +//3hora
						"C.DES_MODULACAO as tipo, " +//4tipo
						"decode(TIP_DESLOCAMENTO,'"+Definicoes.IND_SEM_DESLOCAMENTO+"',DES_OPERACAO,DES_OPERACAO||'"+Definicoes.EM_ROAMING+"') as operacao, " +//5operacao
						"replace(substr(nvl(A.CELL_NAME,'"+Definicoes.CAMPO_VAZIO+"'),0,5),'_','-') as regiaoOrigem, " +//6regiaoOrigem
						"nvl(A.DESTINATION_NAME,'"+Definicoes.CAMPO_VAZIO+"') as regiaoDestino," +//7regiaoDestino
						"decode(a.tip_cdr, 'MMS', a.recipient_address, A.CALL_ID) as numeroDestino, " + //8numeroDestino
						"decode(B.TIP_RATE, '" + Definicoes.TIP_RATE_KILOBYTES + "', to_char(A.CALL_DURATION*B.VLR_FATOR_CONVERSAO), " + 
						"       replace(to_char(trunc((A.CALL_DURATION/(60*60))),'00')||':'||to_char(trunc(mod(A.CALL_DURATION,60*60)/60),'00')||':'||to_char(trunc(mod(mod(A.CALL_DURATION,60*60),60)),'00'),' ')) as duracao, " +//9Duracao/Volume de Dados 
						"nvl(A.account_balance_delta/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as valor, " +//10valor
						"nvl(A.FINAL_ACCOUNT_BALANCE/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as saldo, " +//11saldo
						"nvl(bonus_balance_delta/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as bonus, " +//12bonus
						"nvl(bonus_balance/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as bonus_saldo, " +//13bonus_saldo
						"nvl(data_balance_delta/"+Definicoes.TECNOMEN_MULTIPLICADOR+" ,0) as GPRS, " +//14GPRS  
						"nvl(data_balance/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as GPRS_saldo," +//15GPRS_saldo 
						"nvl(sm_balance_delta/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as SMS, " +//16SMS
						"nvl(sm_balance/"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as SMS_saldo, " +//17SMS_saldo
						"nvl(periodic_balance_delta /"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as periodico, " +//18 periodico
						"nvl(periodic_balance /"+Definicoes.TECNOMEN_MULTIPLICADOR+",0) as periodico_saldo, " +//19 periodico_saldo
						"(nvl(account_balance_delta,0)+nvl(bonus_balance_delta,0)+nvl(sm_balance_delta,0)+nvl(data_balance_delta,0)+nvl(periodic_balance_delta, 0))/100000 as total, " +//20 total
						"(nvl(FINAL_ACCOUNT_BALANCE,0)+nvl(bonus_balance,0)+nvl(sm_balance,0)+nvl(data_balance,0)+nvl(periodic_balance, 0))/100000 as total_saldo, "+//21 total_saldo
						"A.TIP_CHAMADA AS TIPO_REGISTRO, "+	//22 TIPO_REGISTRO
						"nvl(A.NUM_CSP,'--') AS CSP, "+	// 23 CSP
						"TTT.DES_COMPLEMENTO as descricaoComplemento  "+// 24 descricaoComplemento
				" FROM TBL_GER_CDR A, TBL_GER_RATING B, TBL_GER_MODULACAO C, TBL_GER_TIP_TRANSACAO_TECNOMEN TTT WHERE " +
				"A.TIMESTAMP <= to_date(?, 'dd/mm/yyyy hh24:mi:ss') " +
				"AND A.TIMESTAMP >= to_date(?, 'dd/mm/yyyy hh24:mi:ss') " +
				"AND A.TIP_CHAMADA = B.RATE_NAME AND C.IDT_MODULACAO = A.IDT_MODULACAO " +
				"AND TTT.TRANSACTION_TYPE = A.TRANSACTION_TYPE "+
				"AND A.SUB_ID = ? " +
				"AND TTT.IND_IMPRESSAO_COMP_CHAMADAS = ? " +	// Descarta chamadas cujos TT esteja configurado na TBL_GER_TIP_TRANSACAO_TECNOMEN para não serem mostradas	
				"AND NOT (TTT.IDT_SENTIDO=? AND nvl(A.account_balance_delta,0)=0 AND " +
				"nvl(bonus_balance_delta,0) = 0 AND nvl(sm_balance_delta,0) = 0 AND nvl(data_balance_delta,0) = 0 AND nvl(periodic_balance_delta, 0) = 0)  ";// Descarta a chamada entrante com valor zero

	private final static String queryRecargaStatic = "SELECT " +
				"substr(A.IDT_MSISDN,3) as numeroOrigem, " +//1numeroOrigem
				"to_char(A.DAT_RECARGA,'DD/MM/YYYY') as data, " +//2data
				"to_char(A.DAT_RECARGA,'HH24:MI:SS') as hora, " + //3hora
				"'"+Definicoes.CAMPO_VAZIO+"' as tipo, "+//4tipo
				"decode(ID_TIPO_RECARGA,'"+Definicoes.TIPO_RECARGA+"','Recarga',DES_ORIGEM) as operacao, "+//operacao
				"'"+Definicoes.CAMPO_VAZIO+"' as regiaoOrigem, "+//6regiaoOrigem
				"'"+Definicoes.CAMPO_VAZIO+"' as regiaoDestino, "+//7regiaoDestino
				"'"+Definicoes.CAMPO_VAZIO+"' as numeroDestino, "+//8numeroDestino
				"'"+Definicoes.CAMPO_VAZIO+"' as duracao, "+//9duracao
				"nvl(A.vlr_credito_principal,0) as valor, " +//10valor
				"nvl(A.vlr_saldo_final_principal,0) as saldo, " +//11saldo
				"nvl(a.vlr_credito_bonus,0) as bonus," +//12bonus
				"nvl(a.vlr_saldo_final_bonus,0) as bonus_saldo, " +//13bonus_saldo
				"nvl(a.vlr_credito_gprs,0) as GPRS, " +//14GPRS
				"nvl(a.vlr_saldo_final_gprs,0) as GPRS_saldo, " +//15GPRS_saldo
				"nvl(a.vlr_credito_sms,0) as SMS," +//16SMS
				"nvl(a.vlr_saldo_final_sms,0) as SMS_saldo, " +//17SMS_saldo
				"nvl(a.vlr_credito_periodico, 0) AS periodico, " +//18 periodico
				"nvl(a.vlr_saldo_final_periodico, 0) AS periodico_saldo, " +//19 periodico_saldo
				"nvl(A.vlr_credito_principal,0)+nvl(a.vlr_credito_bonus,0)+nvl(a.vlr_credito_sms,0)+nvl(a.vlr_credito_gprs,0)+nvl(a.vlr_credito_periodico, 0) as total, " + //20 total
				"nvl(a.vlr_saldo_final_principal,0)+nvl(a.vlr_saldo_final_bonus,0)+nvl(a.vlr_saldo_final_sms,0)+nvl(a.vlr_saldo_final_gprs,0)+nvl(a.vlr_saldo_final_periodico, 0) as total_saldo, "+ //21 total_saldo
				"A.TIP_TRANSACAO AS TIPO_REGISTRO, "+// 22 TIPO_REGISTRO
				"'--' AS CSP, " +// 23 CSP
				"NULL as descricaoComplemento " +// 24 descricaoComplemento
		"FROM TBL_REC_RECARGAS A, TBL_REC_ORIGEM B WHERE " +
		"A.ID_ORIGEM = B.ID_ORIGEM AND A.ID_CANAL = B.ID_CANAL AND " +
		"DAT_ORIGEM <= to_date(?,'dd/mm/yyyy hh24:mi:ss') AND DAT_ORIGEM >= to_date(?,'dd/mm/yyyy hh24:mi:ss') AND IDT_MSISDN=? AND " +
		" NOT (nvl(A.VLR_CREDITO_principal,0) = 0 AND nvl(A.vlr_credito_bonus,0) = 0 AND nvl(A.vlr_credito_gprs,0) = 0 AND nvl(A.vlr_credito_sms,0) = 0 AND nvl(a.vlr_credito_periodico, 0) = 0) ";

	private final static String queryEventoStatic = "SELECT B.DES_EVENTO, A.DAT_APROVISIONAMENTO " + 
			"FROM TBL_APR_EVENTOS A, TBL_APR_DESCRICAO_EVENTOS B " + 
			"WHERE (A.DAT_APROVISIONAMENTO <= ? AND A.DAT_APROVISIONAMENTO >= ?) " +
			"AND A.IDT_MSISDN = ? " +
			"AND A.TIP_OPERACAO = B.TIP_OPERACAO " +
			"AND A.COD_RETORNO = 0 " +
			" order by DAT_APROVISIONAMENTO";
	private final static String queryCabecalhoStatic =  "SELECT NOM_CLIENTE, DES_ENDERECO, DES_COMPLEMENTO, DES_BAIRRO, "
			+ " DES_CIDADE, DES_UF, DES_CEP FROM TBL_GER_DADOS_COMPROVANTE "
			+ "WHERE IDT_MSISDN = ? "
			+ "AND DAT_REQUISICAO = ?"		
			+ "AND IDT_STATUS_PROCESSAMENTO = ?";
	private final  static String queryDataAtivacaoStatic = "SELECT MAX(DAT_APROVISIONAMENTO) AS DATA " 
						+ "FROM TBL_APR_EVENTOS "
						+ "WHERE TIP_OPERACAO = ? AND IDT_MSISDN = ? ";
	private final static String queryDestinationNameStatic = "SELECT DES_DESTINO " 
										 + "FROM TBL_GER_RATING_DESTINATION "
										+ "WHERE DESTINATION_NAME = ? ";
	
	
	GerentePoolBancoDados gerenteBanco;
	//Assinante infoAssinante = null;
	int planoAssinante = -1;
	DecimalFormat dFormat = new DecimalFormat("###0.00");
	SimpleDateFormat extraiData = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat extraiHora = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	// Dados referentes ao Cabeçalho
	Cabecalho cabecalho = new Cabecalho();
	
	// Dados referentes ao Detalhe
	Vector linhaDetalhe = new Vector();
	
	// Dados referentes aos Totais
	Total totais[] = new Total[5];
	
	//Dados referentes ao Demonstrativo de Eventos
	Vector linhaEvento = new Vector();

	/**
	 * Metodo...: GerarExtrato
	 * Descricao: Construtor 
	 * @param	aIdProcesso - Identificador do processo
	 * @return									
	 */
	public GerarExtrato(long aIdProcesso)
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_GERAR_EXTRATO);
		
		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
	}
	
	/**
	 * Metodo...: comporExtrato
	 * Descricao: Cria o XML com os dados do Assinante gravados em tabela
	 * @param nomeCliente			- Nome do assinante
	 * @param endereco				- Endereco do assinante
	 * @param msisdn				- Numero do assinante
	 * @param plano					- Plano de precos
	 * @param dataAtivacao 			- Data de ativacao do assinante (formato dd/mm/aaaa)	
	 * @param inicioPeriodo 		- Periodo inicial do comprovante de servico - extrato (formato dd/mm/aaaa)
	 * @param finalPeriodo 			- Periodo final do comprovante de servico - extrato (formato dd/mm/aaaa)
	 * @param eComprovanteServico	- Flag se indica se eh para ser impresso ou nao
	 * @param dataRequisicao		- Data de requisicao do comprovante
	 * @return String 				- XML com informações do extrato
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	public String comporExtrato( String msisdn, String inicioPeriodo,String finalPeriodo, boolean eComprovanteServico, Timestamp dataRequisicao) throws GPPInternalErrorException, GPPTecnomenException
	{
		PREPConexao DBConexao = null;
		
		try		
		{
			super.log(Definicoes.INFO, "comporExtrato", "Inicio MSISDN "+msisdn);

			// Pega conexão com banco de dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());
			
			// Construir o Cabeçalho
			this.gerarCabecalho(msisdn, inicioPeriodo, finalPeriodo, eComprovanteServico, dataRequisicao, DBConexao);
			
			// Construir o Detalhe
			this.gerarDetalhe(msisdn, inicioPeriodo, finalPeriodo, DBConexao);
		
			// Construir os Eventos
			this.gerarEventos(msisdn, inicioPeriodo, finalPeriodo, DBConexao);
		
			// Construir Totalização
			this.gerarTotais();
			
			// Gerar arquivo no formato a ser impresso
			return this.gerarXML(eComprovanteServico, msisdn);										
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO,"comporExtrato","Erro:"+e);
			throw new GPPInternalErrorException("Erro GPP:"+e);
		}
		catch (GPPTecnomenException e)
		{
			super.log(Definicoes.ERRO,"comporExtrato","Erro TECNOMEN:"+e);
			throw new GPPTecnomenException("Erro Tecnomen:"+e);
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"comporExtrato","Erro:"+e);
			throw new GPPInternalErrorException("Erro GPP:"+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.INFO, "comporExtrato", "Fim");
		}
	}
	
	/**
	 * Metodo...: gerarDetalhe
	 * Descricao: Busca e valida dados do detalhe de chamadas e recargas para o comprovante de servico
	 * @param  msisdn		- Numero do assinante a buscar os detalhes 
	 * @param  dataInicial 	- Data inicial da busca de detalhes (formato DD/MM/YYYY)
	 * @param  dataFinal 	- Data final da busca de detalhes (formato DD/MM/YYYY)
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private void gerarDetalhe(String msisdn, String dataInicial, String dataFinal, PREPConexao DBConexao) throws GPPInternalErrorException
	{
		// Inicialização de Variáveis
		ResultSet rsChamadasRecargas = null;
		dFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));

		super.log(Definicoes.DEBUG, "gerarDetalhe", "Inicio MSISDN "+msisdn);
		
		// Acerto da data final a fim de selecionar os detalhes do dia todo
		dataFinal = dataFinal.substring(0, 10) + " " + Definicoes.HORA_FINAL_DIA;
		
		try
		{
			// Super query
			//String superQuery = superQueryStatic;
			// Busca chamadas para o número no período em questão
			//String query1 = queryCDRStatic;			
			// Busca recargas/ajustes para o número no período em questão			
			//String query2 = queryRecargaStatic;

			Object params[] = 
				   {dataFinal,
					dataInicial,
					msisdn, Definicoes.TT_INCLUIR_EXTRATO, Definicoes.IND_CHAMADA_RECEBIDA,
					dataFinal,
					dataInicial,
					msisdn};

//			Object params[] = {	new java.sql.Timestamp(sdf.parse(dataFinal).getTime()),
//								new java.sql.Date(extraiData.parse(dataInicial).getTime()),
//								msisdn, Definicoes.TT_INCLUIR_EXTRATO, Definicoes.IND_CHAMADA_RECEBIDA,
//								new java.sql.Timestamp(sdf.parse(dataFinal).getTime()),
//								new java.sql.Date(extraiData.parse(dataInicial).getTime()),
//								msisdn};
								
			StringBuffer query = new StringBuffer(superQueryStatic).append(" from (").append(queryCDRStatic).append(" union ").append(queryRecargaStatic).append(") order by to_date(data,'DD/MM/YYYY'), to_date(hora,'HH24:MI:SS') ");
			rsChamadasRecargas = DBConexao.executaPreparedQuery1(query.toString(), params, super.getIdLog());										

			// Verificar aqui.....
			String operacao;
			String descricaoComplemento;
			while(rsChamadasRecargas.next())
			{
				// Cria linhas de detalhe de chamadas
				String tipoChamada = rsChamadasRecargas.getString("TIPO_REGISTRO");
				boolean chamadaRecebida = tipoChamada.startsWith("R");
				descricaoComplemento = rsChamadasRecargas.getString("descricaoComplemento");
				
				// Coloca o csp na descrição
				String csp = rsChamadasRecargas.getString("CSP");
				if(	csp.equals("--")||csp.equals("00"))
				{
					operacao = rsChamadasRecargas.getString("operacao");
				}
				else
				{
					operacao = rsChamadasRecargas.getString("operacao") + " csp "+ csp;
				}
				
				// Coloca complemento na descrição
				if(descricaoComplemento != null)
					operacao = operacao + " - " + descricaoComplemento;

				// Caso seja o tipo de chamada comece com "SM " ou "MM", devemos replicar no 
				// saldo final de bonus e sms o saldo final do registro anterior, pois a Tecnomen
				// está nos mandando esses saldos nulos
				double saldoFinalSms = rsChamadasRecargas.getDouble("SMS_saldo");
				double saldoFinalBonus = rsChamadasRecargas.getDouble("bonus_saldo");
				double saldoFinalPrincipal = rsChamadasRecargas.getDouble("saldo");
				double saldoFinalDados = rsChamadasRecargas.getDouble("GPRS_saldo");
				double saldoFinalPeriodico = rsChamadasRecargas.getDouble("periodico_saldo");
				
				if((tipoChamada.startsWith("SM ") || tipoChamada.startsWith("MM")) &&
					(!linhaDetalhe.isEmpty()))		// Se a anomalia ocorrer no primeiro elemento, não há lastElement()
				{
					saldoFinalBonus = ((Detalhe)linhaDetalhe.lastElement()).getBonus_saldo();
					saldoFinalSms = ((Detalhe)linhaDetalhe.lastElement()).getSMS_saldo();
				}
				
				linhaDetalhe.add(new Detalhe
						(
								String.valueOf(rsChamadasRecargas.getRow()), //0numeroLinha
								rsChamadasRecargas.getString("numeroOrigem"),//1numeroOrigem
								rsChamadasRecargas.getString("data"), 		//2data
								rsChamadasRecargas.getString("hora"),		//3hora
								rsChamadasRecargas.getString("tipo"),		//4Tipo
								operacao,	//5Operação
								chamadaRecebida?Definicoes.CAMPO_VAZIO:rsChamadasRecargas.getString("regiaoOrigem"),//6regiaoOrigem
								chamadaRecebida?rsChamadasRecargas.getString("regiaoOrigem"):getDestinationName(rsChamadasRecargas.getString("regiaoDestino"), DBConexao),							//7regiaoDestino
								rsChamadasRecargas.getString("numeroDestino"),//8numeroDestino
								rsChamadasRecargas.getString("duracao"),//9duracao
								
								rsChamadasRecargas.getDouble("valor"),//10valor principal
								//rsChamadasRecargas.getDouble("saldo"),//11saldo principal
								saldoFinalPrincipal,
								//********************* MultiploSaldo
								rsChamadasRecargas.getDouble("bonus"),//12valor bonus
								//rsChamadasRecargas.getDouble("bonus_saldo"), //13saldo bonus
								saldoFinalBonus,
								rsChamadasRecargas.getDouble("GPRS"),//14valor GPRS
								//rsChamadasRecargas.getDouble("GPRS_saldo"), //15saldo gprs
								saldoFinalDados,
								rsChamadasRecargas.getDouble("SMS"),//16valor SMS
								//rsChamadasRecargas.getDouble("SMS_saldo"), //17saldo sms
								saldoFinalSms,
								rsChamadasRecargas.getDouble("periodico"),//18valor periodico
								//rsChamadasRecargas.getDouble("periodico_saldo"), //19saldo periodico
								saldoFinalPeriodico,
								rsChamadasRecargas.getDouble("total"),//20valor Total Delta
								saldoFinalPrincipal + saldoFinalBonus + saldoFinalSms + saldoFinalDados + saldoFinalPeriodico
								//rsChamadasRecargas.getDouble("total_saldo") //21saldo total
								//Em função da solução temporária que adotamos (enquanto a Tecnomen continuar
								//nos enviando saldo total sms/bonus zerados para SM e MM) 
								//ignoramos o valor total vindo da query e refazemos a conta
								
								//********************* 								
						));	
			}
			
			rsChamadasRecargas.close();

		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "gerarDetalhe", "Erro:"+e);
			throw new GPPInternalErrorException("Erro GPP:"+e);
		}
		finally
		{
			super.log(Definicoes.DEBUG, "gerarDetalhe", "Fim");
		}		
	}
	
	/**
	 * Metodo...: gerarEventos
	 * Descricao: Busca os dados de eventos para o comprovante de servico
	 * @param  msisdn		- Numero do assinante a buscar os detalhes 
	 * @param  dataInicial 	- Data inicial da busca de detalhes (formato DD/MM/YYYY)
	 * @param  dataFinal 	- Data final da busca de detalhes (formato DD/MM/YYYY)
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private void gerarEventos(String msisdn, String dataInicial, String dataFinal, PREPConexao DBConexao) throws GPPInternalErrorException
	{
		// Inicialização de Variáveis
		ResultSet rsEventos = null;

		super.log(Definicoes.DEBUG, "gerarEventos", "Inicio MSISDN "+msisdn);
		
		// Acerto da data final a fim de selecionar os detalhes do dia todo
		dataFinal = dataFinal + " " + Definicoes.HORA_FINAL_DIA;		
				
		try
		{
			// Busca eventos de aprovisionamento para o número no período em questão
			//String query1 =  queryEventoStatic;// Buscar apenas os eventos que foram gerados com sucesso
		
			Object params1[] = {new java.sql.Timestamp(sdf.parse(dataFinal).getTime()),
								new java.sql.Date(extraiData.parse(dataInicial).getTime()),
								msisdn};
			
			rsEventos = DBConexao.executaPreparedQuery1(queryEventoStatic, params1, super.getIdLog());
													
			while(rsEventos.next())
			{
				// Cria linhas de eventos de aprovisionamento
				linhaEvento.add(new Evento(
							String.valueOf(rsEventos.getRow()), //numeroLinha
							rsEventos.getString("DES_EVENTO"), 	//descrição do evento
							extraiData.format(rsEventos.getDate("DAT_APROVISIONAMENTO")), 	//data
							extraiHora.format(rsEventos.getTimestamp("DAT_APROVISIONAMENTO"))
							)
						);		//hora
			}
			rsEventos.close();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "gerarDetalhe", "Erro: " + e);
			throw new GPPInternalErrorException("Erro GPP: " + e);
		}
		finally
		{
			super.log(Definicoes.DEBUG, "gerarEventos", "Fim");
		}				
	}
	
	/**
	 * Metodo...: gerarTotais
	 * Descricao: Calcula os totais do comprovante de servico
	 * @param 
	 * @return
	 * @throws
	 */
	private void gerarTotais()
	{
		double acumuladorDebitos = 0;
		double acumuladorCreditos = 0;
		double valor = 0;

		double acumuladorDebitosBonus = 0;
		double acumuladorCreditosBonus = 0;
		double valorBonus = 0;

		double acumuladorDebitosSMS = 0;
		double acumuladorCreditosSMS = 0;
		double valorSMS = 0;

		double acumuladorDebitosGPRS = 0;
		double acumuladorCreditosGPRS = 0;
		double valorGPRS = 0;

		double acumuladorDebitosPeriodico = 0;
		double acumuladorCreditosPeriodico = 0;
		double valorPeriodicos = 0;

		dFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		
		super.log(Definicoes.DEBUG, "gerarTotais", "Inicio");

		// Verifica se há detalhes para se gerar os totais
		if(!linhaDetalhe.isEmpty())
		{
			for(int i=0; i < linhaDetalhe.size(); i++)
			{
				valor 		= ((Detalhe)linhaDetalhe.get(i)).getValor();
				valorBonus 	= ((Detalhe)linhaDetalhe.get(i)).getBonus();
				valorSMS 	= ((Detalhe)linhaDetalhe.get(i)).getSMS();
				valorGPRS 	= ((Detalhe)linhaDetalhe.get(i)).getGPRS();
				valorPeriodicos = ((Detalhe)linhaDetalhe.get(i)).getPeriodico();
				if ( valor >= 0 )
					acumuladorCreditos += valor;
				else
					acumuladorDebitos += valor;
				if ( valorBonus >= 0 )
					acumuladorCreditosBonus += valorBonus;
				else
					acumuladorDebitosBonus += valorBonus;
				if ( valorSMS >= 0 )
					acumuladorCreditosSMS += valorSMS;
				else
					acumuladorDebitosSMS += valorSMS;
				if ( valorGPRS >= 0 )
					acumuladorCreditosGPRS += valorGPRS;
				else
					acumuladorDebitosGPRS += valorGPRS;
				if ( valorPeriodicos >= 0 )
					acumuladorCreditosPeriodico += valorPeriodicos;
				else
					acumuladorDebitosPeriodico += valorPeriodicos;

				
			}
			
			super.log(Definicoes.DEBUG, "gerarTotais", "Totais Calculados");
		
			totais[0] = new Total(	new Double(dFormat.format(((Detalhe)linhaDetalhe.get(0)).getSaldo()-((Detalhe)linhaDetalhe.get(0)).getValor())).doubleValue(),		//saldo no início do período
									new Double(dFormat.format(Math.abs(acumuladorDebitos))).doubleValue(),		// total de débitos
									new Double(dFormat.format(acumuladorCreditos)).doubleValue(),		//total de créditos
									new Double(dFormat.format(((Detalhe)linhaDetalhe.get(linhaDetalhe.size()-1)).getSaldo())).doubleValue());		// saldo no final do período
			
			totais[1] = new Total(	new Double(dFormat.format(((Detalhe)linhaDetalhe.get(0)).getBonus_saldo()-((Detalhe)linhaDetalhe.get(0)).getBonus())).doubleValue(),		//saldo no início do período
									new Double(dFormat.format(Math.abs(acumuladorDebitosBonus))).doubleValue(),		// total de débitos
									new Double(dFormat.format(acumuladorCreditosBonus)).doubleValue(),		//total de créditos
									new Double(dFormat.format(((Detalhe)linhaDetalhe.get(linhaDetalhe.size()-1)).getBonus_saldo())).doubleValue());		// saldo no final do período
			
			totais[2] = new Total(	new Double(dFormat.format(((Detalhe)linhaDetalhe.get(0)).getSMS_saldo()-((Detalhe)linhaDetalhe.get(0)).getSMS())).doubleValue(),		//saldo no início do período
									new Double(dFormat.format(Math.abs(acumuladorDebitosSMS))).doubleValue(),		// total de débitos
									new Double(dFormat.format(acumuladorCreditosSMS)).doubleValue(),		//total de créditos
									new Double(dFormat.format(((Detalhe)linhaDetalhe.get(linhaDetalhe.size()-1)).getSMS_saldo())).doubleValue());		// saldo no final do período
			
			totais[3] = new Total(	new Double(dFormat.format(((Detalhe)linhaDetalhe.get(0)).getGPRS_saldo()-((Detalhe)linhaDetalhe.get(0)).getGPRS())).doubleValue(),		//saldo no início do período
									new Double(dFormat.format(Math.abs(acumuladorDebitosGPRS))).doubleValue(),		// total de débitos
									new Double(dFormat.format(acumuladorCreditosGPRS)).doubleValue(),		//total de créditos
									new Double(dFormat.format(((Detalhe)linhaDetalhe.get(linhaDetalhe.size()-1)).getGPRS_saldo())).doubleValue());		// saldo no final do período
			
			totais[4] = new Total(	new Double(dFormat.format(((Detalhe)linhaDetalhe.get(0)).getPeriodico_saldo()-((Detalhe)linhaDetalhe.get(0)).getPeriodico())).doubleValue(),		//saldo no início do período
									new Double(dFormat.format(Math.abs(acumuladorDebitosPeriodico))).doubleValue(),		// total de débitos
									new Double(dFormat.format(acumuladorCreditosPeriodico)).doubleValue(),		//total de créditos
									new Double(dFormat.format(((Detalhe)linhaDetalhe.get(linhaDetalhe.size()-1)).getPeriodico_saldo())).doubleValue());		// saldo no final do período

			
		}
		else
		{
			// Caso não haja detalhes, anular objeto totais
			totais = null;
		}
		super.log(Definicoes.DEBUG, "gerarTotais", "Fim");
	}

	/**
	 * Metodo...: gerarXML
	 * Descricao: Gera o XML com os dados do comprovante de servico
	 * @param msisdn 
	 * @param 
	 * @return  String - XML do extrato
	 * @throws
	 */
	private String gerarXML(boolean eComprovanteServico, String msisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "gerarXML", "Inicio");

		try
		{
			SimpleDateFormat formataAno = new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat formatoPadrao = new SimpleDateFormat("dd/MM/yyyy");
			GerarXML geradorXML = new GerarXML("GPPExtrato");
			DecimalFormat df = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(Locale.ENGLISH));
		
			// Mensagem do Comprovante
			geradorXML.adicionaTag("mensagemComprovante","Comprovante de Prestação de Serviços");
		
			// Geração da Sessão de Cabecalho - Dados Cadastrais - do XML
			geradorXML.abreNo("dadosCadastrais");
			geradorXML.adicionaTag("nome", cabecalho.getNomeCliente());
			geradorXML.adicionaTag("msisdn", cabecalho.getMsisdn());
			//geradorXML.adicionaTag("dataAtivacao",cabecalho.getDataAtivacao());
			geradorXML.adicionaTag("plano",cabecalho.getPlano());
			geradorXML.adicionaTag("periodoInicial",cabecalho.getInicioPeriodo());
			geradorXML.adicionaTag("periodoFinal",cabecalho.getFinalPeriodo());
			geradorXML.adicionaTag("dataHoraImpressao", cabecalho.getDataHora());
			geradorXML.fechaNo();

			// Geração da Sessão de Cabecalho - Dados Correspondência - do XML
			geradorXML.abreNo("dadosCorrespondencia");
			geradorXML.adicionaTag("endereco", cabecalho.getEndereco());
			geradorXML.adicionaTag("complemento", cabecalho.getComplemento());
			geradorXML.adicionaTag("cidade", cabecalho.getCidade());
			geradorXML.adicionaTag("bairro", cabecalho.getBairro());
			geradorXML.adicionaTag("uf", cabecalho.getUf());
			geradorXML.adicionaTag("cep", cabecalho.getCep());
			geradorXML.fechaNo();
			
			//if(!eComprovanteServico) //Verificacao se assinante eh ligmix em algumas funcionalidades do portal e gpp depende dessa tag 
			//{
				// Geração da Sessão de Controle (Mensagens para o Portal)
				geradorXML.abreNo("dadosControle");
				Aprovisionar aprovisionar = new Aprovisionar(this.getIdLog());
				geradorXML.adicionaTag("numLigMix", aprovisionar.ehLigMix(new Integer(planoAssinante).intValue()) ? "1" : "0");
				Assinante assinante = aprovisionar.consultaAssinante(msisdn);
				if (assinante != null)
				{
					geradorXML.adicionaTag("planoPreco", (new Integer(assinante.getPlanoPreco())).toString());
				}
				else
				{
					geradorXML.adicionaTag("planoPreco", "0");
				}
				geradorXML.fechaNo();
			//}

			//Mensagem de Serviços
			geradorXML.adicionaTag("mensagemServicos", "Comprovante de Serviços de Telecomunicações");

			// Geração da sessão de Detalhes do XML
			if(linhaDetalhe.isEmpty())
			{
				geradorXML.abreNo("detalhe");
				geradorXML.fechaNo();
			}
			else
			{
				//Criação das Tags de Detalhe do XML
				for(int j=0;j<linhaDetalhe.size();j++)
				{
					Detalhe itemDetalhe = (Detalhe) linhaDetalhe.get(j);
				
					geradorXML.abreNo("detalhe");
					geradorXML.adicionaTag("numeroLinha", itemDetalhe.getNumeroLinha());
					geradorXML.adicionaTag("numeroOrigem", itemDetalhe.getNumeroOrigem());
					geradorXML.adicionaTag("data", formataAno.format(formatoPadrao.parse(itemDetalhe.getData())));
					geradorXML.adicionaTag("hora", itemDetalhe.getHora());
					geradorXML.adicionaTag("tipo", itemDetalhe.getTipo());
					geradorXML.adicionaTag("operacao", itemDetalhe.getOperacao());
					geradorXML.adicionaTag("regiaoOrigem", itemDetalhe.getRegiaoOrigem());
					geradorXML.adicionaTag("regiaoDestino", itemDetalhe.getRegiaoDestino());
					geradorXML.adicionaTag("numeroDestino", itemDetalhe.getNumeroDestino());
					geradorXML.adicionaTag("duracao", itemDetalhe.getDuracao());
						geradorXML.abreNo("valores");
							geradorXML.adicionaTag("valorPrincipal", df.format(itemDetalhe.getValor()));
							geradorXML.adicionaTag("valorBonus", df.format(itemDetalhe.getBonus()));
							geradorXML.adicionaTag("valorSMS", df.format(itemDetalhe.getSMS()));
							geradorXML.adicionaTag("valorGPRS", df.format(itemDetalhe.getGPRS()));
							geradorXML.adicionaTag("valorPeriodico", df.format(itemDetalhe.getPeriodico()));
							geradorXML.adicionaTag("valorTotal", df.format(itemDetalhe.getTotal()));
						geradorXML.fechaNo();
						geradorXML.abreNo("saldos");
							geradorXML.adicionaTag("saldoPrincipal", df.format(itemDetalhe.getSaldo()));
							geradorXML.adicionaTag("saldoBonus", df.format(itemDetalhe.getBonus_saldo()));
							geradorXML.adicionaTag("saldoSMS", df.format(itemDetalhe.getSMS_saldo()));
							geradorXML.adicionaTag("saldoGPRS", df.format(itemDetalhe.getGPRS_saldo()));
							geradorXML.adicionaTag("saldoPeriodico", df.format(itemDetalhe.getPeriodico_saldo()));
							geradorXML.adicionaTag("saldoTotal", df.format(itemDetalhe.getTotal_saldo()));
						geradorXML.fechaNo();
					geradorXML.fechaNo();
				}
			}

			//Mensagem de Totais
			geradorXML.adicionaTag("mensagemTotais",null);

			// Criação das tags de Total do XML
			geradorXML.abreNo("totais");
		
			// Verifica se ha totais a serem mostrados
			if (totais != null)
			{
				double total = 0D;
				geradorXML.abreNo("saldoInicial");
					geradorXML.adicionaTag("totalSaldoPrincipal", Double.toString(totais[0].getSaldoInicialPeriodo()));
					geradorXML.adicionaTag("totalSaldoBonus", Double.toString(totais[1].getSaldoInicialPeriodo()));
					geradorXML.adicionaTag("totalSaldoSMS", Double.toString(totais[2].getSaldoInicialPeriodo()));
					geradorXML.adicionaTag("totalSaldoGPRS", Double.toString(totais[3].getSaldoInicialPeriodo()));
					geradorXML.adicionaTag("totalSaldoPeriodico", Double.toString(totais[4].getSaldoInicialPeriodo()));
					for (int i=0;i<totais.length;i++)total += totais[i].getSaldoInicialPeriodo();
					//geradorXML.adicionaTag("saldoTotalInicial", Double.toString(total));
					geradorXML.adicionaTag("saldoTotalInicial",df.format(total));
				geradorXML.fechaNo();
				
				total = 0D;
				geradorXML.abreNo("totalDebitos");
					geradorXML.adicionaTag("totalDebitosPrincipal", Double.toString(totais[0].getTotalDebitos()));
					geradorXML.adicionaTag("totalDebitosBonus", Double.toString(totais[1].getTotalDebitos()));
					geradorXML.adicionaTag("totalDebitosSMS", Double.toString(totais[2].getTotalDebitos()));
					geradorXML.adicionaTag("totalDebitosGPRS", Double.toString(totais[3].getTotalDebitos()));
					geradorXML.adicionaTag("totalDebitosPeriodico", Double.toString(totais[4].getTotalDebitos()));
					for (int i=0;i<totais.length;i++)total += totais[i].getTotalDebitos();
					//geradorXML.adicionaTag("debitosTotais", Double.toString(total));
					geradorXML.adicionaTag("debitosTotais", df.format(total));
			    geradorXML.fechaNo();
			    
			    total = 0D;
			    geradorXML.abreNo("totalCreditos");
			    	geradorXML.adicionaTag("totalCreditosPrincipal", Double.toString(totais[0].getTotalCreditos()));
			    	geradorXML.adicionaTag("totalCreditosBonus", Double.toString(totais[1].getTotalCreditos()));
			    	geradorXML.adicionaTag("totalCreditosSMS", Double.toString(totais[2].getTotalCreditos()));
			    	geradorXML.adicionaTag("totalCreditosGPRS", Double.toString(totais[3].getTotalCreditos()));
			    	geradorXML.adicionaTag("totalCreditosPeriodico", Double.toString(totais[4].getTotalCreditos()));
					for (int i=0;i<totais.length;i++)total += totais[i].getTotalCreditos();
					//geradorXML.adicionaTag("creditosTotais", Double.toString(total));
					geradorXML.adicionaTag("creditosTotais", df.format(total));
		        geradorXML.fechaNo();
		        
		        total = 0D;
				geradorXML.abreNo("saldoFinalPeriodo");
					geradorXML.adicionaTag("totalSaldoFinalPrincipal", Double.toString(totais[0].getSaldoFinalPeriodo()));
					geradorXML.adicionaTag("totalSaldoFinalBonus", Double.toString(totais[1].getSaldoFinalPeriodo()));
					geradorXML.adicionaTag("totalSaldoFinalSMS", Double.toString(totais[2].getSaldoFinalPeriodo()));
					geradorXML.adicionaTag("totalSaldoFinalGPRS", Double.toString(totais[3].getSaldoFinalPeriodo()));
					geradorXML.adicionaTag("totalSaldoFinalPeriodico", Double.toString(totais[4].getSaldoFinalPeriodo()));
					for (int i=0;i<totais.length;i++)total += totais[i].getSaldoFinalPeriodo();
					//geradorXML.adicionaTag("saldoTotalFinal", Double.toString(total));
					geradorXML.adicionaTag("saldoTotalFinal", df.format(total));
			    geradorXML.fechaNo();
			}

			// Fecha o tag de totais
			geradorXML.fechaNo();

			//Mensagem de Eventos
			geradorXML.adicionaTag("mensagemEventos", "Demonstrativo de Eventos");

			// Geração da sessão de eventos do XML
			if(!linhaEvento.isEmpty())
			{
				// Criação das tags de evento do XML
				for(int k=0; k < linhaEvento.size(); k++)
				{
					// Criação das tags de eventos do XML
					geradorXML.abreNo("evento");
		
					geradorXML.adicionaTag("numeroLinha",((Evento)linhaEvento.get(k)).getNumeroLinha());
					geradorXML.adicionaTag("descricao",((Evento)linhaEvento.get(k)).getDescricao());
					geradorXML.adicionaTag("data",formataAno.format(formatoPadrao.parse(((Evento)linhaEvento.get(k)).getData())));
					geradorXML.adicionaTag("hora",((Evento)linhaEvento.get(k)).getHora());

					geradorXML.fechaNo();
				}
			}
			else
			{
				// Criação das tags de eventos do XML
				geradorXML.abreNo("evento");

				// Fecha o tag de eventos
				geradorXML.fechaNo();
			}

			//Mensagem Informativa
			geradorXML.adicionaTag("mensagemInformativo", "Esse comprovante é emitido gratuitamente apenas uma vez ao mes");

			super.log(Definicoes.DEBUG, "gerarXML", "Fim");
		
			//Retorno do XML completo
			return geradorXML.getXML();			
		}
		catch (ParseException e)
		{
			super.log(Definicoes.WARN, "gerarXML", "ERRO PARSER:"+e);
			throw new GPPInternalErrorException("Erro de Parse: "+e);
		}

	}
	
	

	/**
	 * Metodo...: gerarCabecalho
	 * Descricao: Busca os dados de cabecalho para o comprovante de servico / extrato
	 * @param msisdn				- Numero do assinante a buscar os detalhes 
	 * @param dataInicial 			- Data inicial da busca de detalhes (formato DD/MM/YYYY)
	 * @param dataFinal 			- Data final da busca de detalhes (formato DD/MM/YYYY)
	 * @param eComprovanteServico	- Flag que verifica se eh para ser impresso ou nao
	 * @param dataRequisicao		- Data da requisicao do comprovante
	 * @return
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	private void gerarCabecalho(String msisdn, String inicioPeriodo,String finalPeriodo, boolean eComprovanteServico, Timestamp dataRequisicao, PREPConexao conexaoPrep) throws GPPInternalErrorException, GPPTecnomenException
	{
		// Inicializa variáveis para conexão com Tecnomen
		//GerentePoolTecnomen gerenteRecarga = GerentePoolTecnomen.getInstancia(super.getIdLog());;
		//TecnomenRecarga tr = null;
		//String sql;
		ResultSet rs;
		MapPlanoPreco mapPlanoPreco = MapPlanoPreco.getInstancia();
		
		super.log(Definicoes.DEBUG, "gerarCabecalho", "Inicio MSISDN "+msisdn);

		try
		{
			// Faz a consulta ao Assinante e armazena seus dados num objeto da classe Assinante
			//tr = gerenteRecarga.getTecnomenRecarga(super.getIdLog());
			//infoAssinante = tr.consultaAssinante (msisdn, super.getIdLog());
			

			if (eComprovanteServico)
			{			
				//	Seleciona dados para gerar comprovante
				//sql = queryCabecalhoStatic;
				
				Object param[] = {msisdn, dataRequisicao, "N"};
				rs = conexaoPrep.executaPreparedQuery(queryCabecalhoStatic, param, super.getIdLog());
			
				if (rs.next())
				{
					cabecalho.setNomeCliente(rs.getString(1));
					cabecalho.setEndereco(rs.getString(2));
					cabecalho.setComplemento(rs.getString(3));
					cabecalho.setBairro(rs.getString(4));
					cabecalho.setCidade(rs.getString(5));
					cabecalho.setUf(rs.getString(6));
					cabecalho.setCep(rs.getString(7));
					cabecalho.setDataHora(GPPData.dataCompletaForamtada());
				}
				rs.close();
				rs = null;
			}
			
			// Preenche informações do cabeçalho
			cabecalho.setMsisdn(msisdn.substring(2));
			cabecalho.setDataAtivacao(this.getDataAtivacao(msisdn, conexaoPrep));
			cabecalho.setInicioPeriodo(inicioPeriodo);
			cabecalho.setFinalPeriodo(finalPeriodo);
			
			// Verificando se o assinante existe
			rs = conexaoPrep.executaPreparedQuery("select fnc_plano_assinante(?,?) from dual",new Object[]{new Timestamp(System.currentTimeMillis()),msisdn}, super.getIdLog());
			if(rs.next())
				planoAssinante = rs.getInt(1); 
			
			if (planoAssinante!=-1) //infoAssinante != null)
			{
				cabecalho.setPlano(mapPlanoPreco.getMapDescPlanoPreco(new Integer(planoAssinante).toString()));
						//Short.toString(infoAssinante.getPlanoPreco())));									
			}
			else
			{
				cabecalho.setPlano ("");
				super.log(Definicoes.WARN, "comporExtrato", "Gerando extrato de conta para usuario nao cadastrado no GPP.");
			}
		}
		/*catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "gerarCabecalho", "Erro tecnomen:" + e);
			throw new GPPTecnomenException("Erro Tecnomen:" + e);
		}*/
		catch(SQLException e)
		{
			super.log(Definicoes.WARN, "gerarCabecalho", "Erro SQL:" + e);
			throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		finally
		{
			// Libera conexao com do pool Recargas
			//gerenteRecarga.liberaConexaoRecarga(tr, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "gerarCabecalho", "Fim");
		}
	}

	/**
	 * Metodo...: getDataAtivacao
	 * Descricao: Obtem a data de ativacao do assinante
	 * @param 	msisdn	- Numero do acesso do assinante a ser pesquisado
	 * @return 	String 	- Data da ultima ativacao
	 * @throws  GPPInternalErrorException
	 */
	private String getDataAtivacao(String msisdn, PREPConexao DBConexao) throws GPPInternalErrorException
	{
		String dataAtivacao = "";
		String retorno = null;
			
		super.log(Definicoes.DEBUG, "getDadosAtivacao", "Inicio MSISDN "+msisdn);

		try
		{
			// Procura pela data de ativação entre os eventos de aprovisionamento
			//String query =  queryDataAtivacaoStatic;
			
			Object params1[] = {Definicoes.TIPO_APR_ATIVACAO, msisdn};
			ResultSet rsAtivacao = DBConexao.executaPreparedQuery1(queryDataAtivacaoStatic, params1, super.getIdLog());
	
			if(rsAtivacao.next())
			{
				Date auxData = rsAtivacao.getDate("DATA");
				if (auxData != null)
				{
					super.log(Definicoes.DEBUG, "getDadosAtivacao", "Data de ativacao de assinante encontrada");
					dataAtivacao = extraiData.format(auxData);
				}
				rsAtivacao.close();
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "getDadosAtivacao", "Erro:" + e);
		}
		
		if ( (dataAtivacao==null) || (dataAtivacao.equals("")) )
		{
			retorno = dataAtivacao;
			super.log(Definicoes.WARN, "getDadosAtivacao", "Nao ha registro de ativacao desto assinante: " + msisdn);
		}
		else
		{
			retorno = dataAtivacao.substring(0,10);
		}
		
		super.log(Definicoes.DEBUG, "getDadosAtivacao", "Fim");

		return retorno;
	}
	
	/**
	 * Metodo...: getDestinationName
	 * Descricao: Retorna o valor da descricao do destination name da tabela de chamadas
	 * @param 	destinationName		- Valor do destination name na tabela de chamadaso
	 * @return  String				- Valor da descricao do destination name
	 * @throws  GPPInternalErrorException
	 */
	private String getDestinationName(String destinationName, PREPConexao DBConexao) throws GPPInternalErrorException
	{
		String retorno = "-";
			
		super.log(Definicoes.DEBUG, "getDestinationName", "Inicio");

		try
		{
			// Procura pela da descricao do destination name
			//String query =  queryDestinationNameStatic;
			
			Object param[] = {destinationName};
			ResultSet rsDestination = DBConexao.executaPreparedQuery2(queryDestinationNameStatic, param, super.getIdLog());
	
			if(rsDestination.next())
			{
				// descricao do destination name de retorno
				retorno = rsDestination.getString(1);
				//Pega os 5 primeiros caracteres e substitui o underscore por ifem
				retorno = retorno.replace('_', '-');
			}
			rsDestination.close();
			rsDestination = null;
		}
		catch (SQLException e)
		{
			super.log(Definicoes.WARN, "getDestinationName", "Excecao SQL:"+ e);
			throw new GPPInternalErrorException ("Excecao GPP:" + e);				
		}

		finally
		{
			super.log(Definicoes.DEBUG, "getDestinationName", "Fim");
		}
		return retorno;
	}
}
