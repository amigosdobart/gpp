// Definicao do Pacote
package com.brt.gpp.aplicacoes.promocao;

// Arquivo de Imports de Gerentes do GPP 
//import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
//import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
//import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.Definicoes;
//import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.mapeamentos.*;
import com.brt.gpp.aplicacoes.Aplicacoes;
//import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
//import com.brt.gpp.aplicacoes.recarregar.*;

//import com.brt.gpp.comum.gppExceptions.*;

//import java.sql.*;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;

//import TINC.Pi_exception;

/**
  *
  * Este arquivo refere-se a classe RecargaGeneva, responsavel pela implementacao da
  * logica de leitura e execucao de recargas de assinantes provenientes do Genevade
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Luciano Vilela
  * Data: 				26/03/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  * @deprecated
  */
public final class PromocaoPrepago extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolTecnomen 	gerenteTecnomen = null; // Gerente de conexoes Tecnomen
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	private MapConfiguracaoGPP	mapConfig = null;				// Mapeamento da TBL_GER_CONFIG_GPP	
	//private long aIdProcesso;
	//private Map promocoes = null;
	//private SimpleDateFormat dateFormat;
	//private DecimalFormat numberFormat = new DecimalFormat("###,##0.00");
	
		     
	/**
	 * Metodo...: RecargaRecorrente
	 * Descricao: Contrutor
	 * @param long	logId	Id do Log
	 */
	 public PromocaoPrepago (long logId)
	 {
		super(logId, Definicoes.CL_RECARGA_RECORRENTE);
		
	    
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
		try
		{
			//Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				super.log(Definicoes.WARN, "PromocaoPrepago", "Problemas com Mapeamento das Configurações GPP");		
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "PromocaoPrepago", "Problemas com Mapeamento das Configurações GPP");
		}

		// Instanciando mapeamento da TBL_GER_CONFIG_GPP
		//aIdProcesso = super.getIdLog(); 
		//promocoes = new HashMap();
		//carregaListaPromocoes();
		//dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	 }

	/**
	 * Metodo...: efetuaRecargaRecorrente
	 * Descricao: Faz a leitura dos registros que devem receber recarga e efetua essas
	 * 			  recargas na plataforma da Tecnomen
	 * @return	boolean - TRUE caso a execucao do processo seja com sucesso, senao FALSE	
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
//	 public boolean processaPromocao (String data) throws GPPInternalErrorException, GPPTecnomenException, Pi_exception, SQLException
//	 {
//		 super.log(Definicoes.INFO, "processaPromocao", "Inicio DATA "+data);
//	
//		 // Inicializa variaveis do metodo
//		 long aIdProcesso = super.getIdLog(); 
//		 boolean retorno = false;
//		 
//		 String sql;
//		 ResultSet rs = null;
//		 
//		 String MSISDN;
//		 int promocao;
//		 Date dataExecucao;
//		 Date dataInicioAnalise;
//		 Date dataFimAnalise;
//		 int cont = 0;
//		 String dataInicial = GPPData.dataCompletaForamtada();
//		 PREPConexao conexaoPrep = null; 
//		 
//		
//		 try
//		 {			
//			java.util.Date aData = dateFormat.parse(data);
//			//Seleciona conexao do pool Prep Conexao
//			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
//			
//			//Seleciona os registros que devem receber recarga			
//			sql = "SELECT "+
//			"   A.IDT_MSISDN , A.IDT_PROMOCAO , A.DAT_EXECUCAO , A.DAT_INICIO_ANALISE , A.DAT_FIM_ANALISE "+
//			" FROM "+
//			"   TBL_GER_PROMOCAO_ASSINANTE A "+
//			" WHERE "+
//			" a.IDT_PROMOCAO = 1 AND " + 
//			"     (A.DAT_EXECUCAO <=TO_DATE('"+ data +"', 'DD/MM/YYYY')) "+
//			" AND (A.IND_SUSPENSO =0)";
//			rs = conexaoPrep.executaQuery(sql, aIdProcesso);
//			
//			while (rs.next())
//			{
//				Promocao aPromocao=null;
//				boolean fazRecarga = false;
//				double valorCredito = 0.0d;
//				String retRecarga = "";
//				MSISDN = rs.getString(1);
//					
//				super.log(Definicoes.DEBUG, "processaPromocao", "MSISDN: " + MSISDN);
//				promocao = rs.getInt(2);
//				dataExecucao = rs.getDate(3);
//				dataInicioAnalise = rs.getDate(4);
//				// Valida Assinante e Valor Recarga
//				dataFimAnalise = rs.getDate(5);
//				short codRetorno = validaAssinante(MSISDN,  conexaoPrep);
//				boolean atualizarCliente = false;
//				if ( codRetorno == Definicoes.RET_OPERACAO_OK ){
//
//						sql = " SELECT sum(a.call_duration)/60, sum(a.call_value)/10000 "+
//							  " FROM tbl_ger_cdr a "+
//							  " where "+
//							  " a.sub_id = ? and "+
//							  " a.timestamp between ? and ? and "+
//							  " a.transaction_type in (select transaction_type from  "+
//							  "                       tbl_ger_promocao_transaction  "+
//							  "                       where idt_promocao = 1) and "+
//							  " a.tip_chamada in (select rate_name from  "+
//							  "                  tbl_ger_promocao_rate_name "+ 
//							  "                  where idt_promocao = 1) and "+
//							  " a.num_csp in ('00', '14')"; 
//
//						Object param[] = {MSISDN, dataInicioAnalise, dataFimAnalise};
//						ResultSet rs2 = conexaoPrep.executaPreparedQuery(sql, param, aIdProcesso);
//						aPromocao = (Promocao) promocoes.get(new Integer(promocao));
//						if(rs2.next()){
//								
//							if(aPromocao.getTipo() == 2 ){
//								valorCredito = rs2.getDouble(1) * aPromocao.getValorPorMinuto();
//							}
//							else if(aPromocao.getTipo() == 1){
//								valorCredito = rs2.getDouble(2);
//							}
//							fazRecarga = true;
//						}
//				}
//				// Se o assinante e o valor da recarga nao sao validos
//				else{
//					if (codRetorno == Definicoes.RET_MSISDN_NAO_ATIVO)
//						super.log(Definicoes.WARN, "processaPromocao", "O assinante " + MSISDN + " nao existe na Tecnomen.");				
//					else if (codRetorno == Definicoes.RET_STATUS_MSISDN_INVALIDO)
//						super.log(Definicoes.WARN, "processaPromocao", "O status do assinante " + MSISDN + " nao e valido.");
//					else if (codRetorno == Definicoes.RET_MSISDN_BLOQUEADO)
//						super.log(Definicoes.WARN, "processaPromocao", "O servico do assinante " + MSISDN + " esta bloqueado.");
//					else if (codRetorno == Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO)
//						super.log(Definicoes.WARN, "processaPromocao", "O limite do credito foi ultrapassado.");
//					else if (codRetorno == Definicoes.RET_CREDITO_INSUFICIENTE)
//						super.log(Definicoes.WARN, "processaPromocao", "O assinante " + MSISDN + "com saldo insuficiente.");
//					atualizarCliente = true;
//					//efetua recarga para a tabela de not ok
//					//retRecarga = efetuaRecarga (MSISDN, valorCredito,  conexaoPrep);
//
//				}
//				
//				super.log(Definicoes.DEBUG, "processaPromocao", "Faz Recarga? " + fazRecarga);
//				// Se a recarga deve ser efetuada
//				
//				if (fazRecarga && valorCredito > 0)
//				{
//					//efetua recarga para a tabela de ok
//					retRecarga = efetuaRecarga (MSISDN, valorCredito,  conexaoPrep);
//					if(Definicoes.IDT_REC_RECORRENTE_OK.equals(retRecarga)){
//						atualizarCliente = true;
//					}
//					ConsumidorSMS consumidorSMS = ConsumidorSMS.getInstance(super.getIdLog());		
//					if(consumidorSMS.gravaMensagemSMS (MSISDN, "Voce recebeu R$ " + numberFormat.format(valorCredito) + " pela Promocao Pula-Pula", Definicoes.SMS_PRIORIDADE_ZERO, Definicoes.SMS_RECARGA,super.getIdLog()))
//						super.log(Definicoes.INFO, "processamentoPromocao", "Envio do SMS com sucesso.");
//					else
//						super.log(Definicoes.WARN, "processamentoPromocao", "Erro no envio do SMS.");
//
//				}
//				if(valorCredito <= 0.0){
//					atualizarCliente = true;
//				}
//				super.log(Definicoes.DEBUG, "processaPromocao", "Resposta do efetuaRecarga: " + retRecarga);
//
//				if(atualizarCliente){
//					java.util.Date dataAtualizacao=null;
//					java.util.Date dataInicio=null;
//					java.util.Date dataFim=null;
//					if(aPromocao.isMesFechado()){
//						dataFim = getUltimoDiaProximoMes(aData);
//						Calendar cal = Calendar.getInstance();
//						cal.setTime(dataFim);
//						cal.add(Calendar.DAY_OF_MONTH, 5);
//						dataAtualizacao = cal.getTime(); 
//						dataInicio = getPrimeiroDiaProximoMes(aData);
//					}
//					else{
//						Calendar cal = Calendar.getInstance();
//						cal.setTime(aData);
//						cal.add(Calendar.DATE, aPromocao.getNumDias());
//						dataAtualizacao = cal.getTime();
//						dataFim = dataAtualizacao;
//						cal.setTime(aData);
//						cal.add(Calendar.DATE, 1);
//						dataInicio = cal.getTime();
//					}
//					atualizaDataProximaExecucao(MSISDN,dataAtualizacao, dataInicio, dataFim, conexaoPrep);
//				}				
//				
//				
//				cont = cont + 1;
//			}
//			rs.close();
//			rs = null;		
//			
//			super.log(Definicoes.DEBUG, "processaPromocao", "Fim dos registros selecionados.");
//
//			retorno = true;
//		}
//		catch (ParseException e) {
//			retorno = false;
//			super.log(Definicoes.WARN, "processaPromocao", "Excecao (PARSER):"+ e);				
//			
//			// Libera conexao do banco de dados
//			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
//			
//			throw new GPPInternalErrorException ("Excecao GPP:" + e);			 
//		}
//		catch (GPPInternalErrorException e1)
//		{
//			retorno = false;
//			super.log(Definicoes.ERRO, "processaPromocao", "Excecao:"+ e1);				
//			
//			// Libera conexao do banco de dados
//			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
//			
//			throw new GPPInternalErrorException ("Excecao GPP:" + e1);			 
//		}
//		finally
//		{
//			// Libera conexao com do pool PREP
//			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, aIdProcesso);
//			
//			String dataFinal = GPPData.dataCompletaForamtada();
//			String descricao = "Foram processados " + cont + " registros.";
//			String statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
//
//			// Chama a funcao para gravar no historico o Processo em questao
//			super.gravaHistoricoProcessos(38, dataInicial, dataFinal, statusProcesso, descricao, dataInicial);
//			
//		 }		 
//		super.log(Definicoes.INFO, "processaPromocao", "Fim RETORNO "+retorno);
//		return retorno;
//	 }	 
//	 
//
//	 
//	/**
//	 * Metodo...: efetuaRecarga
//	 * Descricao: Chama a funcao de recarga para creditar o valor de recarga no MSISDN
//	 * @param	String		aMSISDN			- Numero do Assinante
//	 * @param	short		aValorRecarga	- Valor da Recarga
//	 * @param	String		aTipoRecarga	- Tipo de Recarga (F ou P)
//	 * @param	PREPConexao	aConexaoPrep 	- Conexao PREP a ser utilizada
//	 * @return	boolean 					- TRUE se for hibrido ou FALSE se nao for
//	 * @throws GPPTecnomenException
//	 * @throws GPPInternalErrorException
//	 */
//	 public String efetuaRecarga (String aMSISDN, double aValorRecarga,  PREPConexao aConexaoPrep)
//									throws GPPTecnomenException, GPPInternalErrorException, SQLException
//	 {
//		super.log(Definicoes.DEBUG, "efetuaRecarga", "Inicio MSISDN "+aMSISDN + " VLRRECARGA "+aValorRecarga);
//	
//		String retorno = Definicoes.IDT_REC_RECORRENTE_NAO_PROCESSADO;
//		
//		String sql;
//		String sql_data;
//		ResultSet rs1;
//		ResultSet rs_data;
//		
//		String tipoTransacao="";		 
//		String identificacaoRecarga="0";
//		String data_ultima_recarga_processada = "0";
//		String dataHora;
//		String parteDia;
//		String parteMes;
//		String parteAno;
//		String parteHora;
//		String parteMinuto;
//		String parteSegundo;
//		String tipoCredito = Definicoes.TIPO_CREDITO_REAIS;
//		String sistemaOrigem = Definicoes.SO_GPP;
//		String operador = Definicoes.GPP_OPERADOR;
//		
//		short respostaRecarga;
//
//		try
//		{
//		 		
//			//	Seleciona a dataHora do sistema
//			dataHora =  GPPData.dataCompletaForamtada();
//			
//			//Transforma o formato da data para yyyymmdd
//			parteDia = dataHora.substring(0,2);
//			parteMes = dataHora.substring(3,5);
//			parteAno = dataHora.substring(6,10);
//			parteHora = dataHora.substring(11,13);
//			parteMinuto = dataHora.substring(14,16);
//			parteSegundo = dataHora.substring(17,19);
//			dataHora = parteAno +  parteMes + parteDia + parteHora + parteMinuto + parteSegundo;
//								 	
//			short retAjuste;
//			Ajustar ajustar = new Ajustar(super.logId);
//			
//			retAjuste = ajustar.executaAjuste(	aMSISDN, 
//												"08001", 
//												tipoCredito, 
//												aValorRecarga, 
//												Definicoes.TIPO_AJUSTE_CREDITO, 
//												dataHora, 
//												sistemaOrigem, 
//												operador, 
//												"",
//												null, "",
//												Definicoes.AJUSTE_NORMAL);
//			
//
//			super.log(Definicoes.DEBUG, "efetuaRecarga","DataHora: " + dataHora+ "\n Resposta Ajuste: " + retAjuste);		
//			
//			//	Se a recarga foi efetuada com sucesso, retorna sucesso
//			if ( retAjuste == Definicoes.RET_OPERACAO_OK)
//			{
//				retorno = Definicoes.IDT_REC_RECORRENTE_OK;
//			}
//			//	Se a recarga foi efetuada com erro, retorna erro
//			else
//			{
//				retorno = Definicoes.IDT_REC_RECORRENTE_ERRO;
//			}
//		}
//		catch (GPPTecnomenException e)
//		{
//			retorno = Definicoes.IDT_REC_RECORRENTE_NAO_PROCESSADO;			 
//			super.log(Definicoes.ERRO, "efetuaRecarga", "Excecao TECNOMEN:"+ e);				
//			throw new GPPTecnomenException ("Excecao Tecnomen:" + e);
//		}
//		catch (GPPInternalErrorException e1)
//		{
//			retorno = Definicoes.IDT_REC_RECORRENTE_NAO_PROCESSADO;			 
//			super.log(Definicoes.ERRO, "efetuaRecarga", "Excecao GPP:"+ e1);				
//			throw new GPPInternalErrorException ("Excecao GPP:" + e1);	
//		}
//		super.log(Definicoes.DEBUG, "efetuaRecarga", "Fim RETORNO "+retorno);
//		return retorno;
//	 }
//	 
//	/**
//	 * Metodo...: validaAssinante
//	 * Descricao: Chama a funcao de consulta pre-recarga
//	 * @param	String		aMSISDN			- Numero do Assinante
//	 * @param 	PREPConexao	aConexaoPrep	- Conexão com BD	
//	 * @return	short 						- identificacao do codigo sucesso ou erro	 * @throws GPPTecnomenException
//	 * @throws GPPInternalErrorException
//	 */
//	 public short validaAssinante (String aMSISDN, PREPConexao aConexaoPrep) throws GPPTecnomenException, GPPInternalErrorException, Pi_exception, SQLException
//	 {
//		super.log(Definicoes.DEBUG, "validaAssinante", "Inicio MSISDN "+aMSISDN );
//	
//		short retorno = Definicoes.RET_OPERACAO_OK;
//		TecnomenRecarga ta = null;
//		Assinante dadosAssinante = null;
//		short saldoMaximo = 0;
//		String sql = "";				 
//		ResultSet rs1;
//		double valorRecarga=0;
//				
//		try
//		{			
//			//Seleciona conexao do pool de recarga da Tecnomen
//			ta = gerenteTecnomen.getTecnomenRecarga(super.logId);
//			
//			//Efetua a consulta dos dados do assinante recebido como parametro
//			dadosAssinante = ta.consultaAssinante(aMSISDN, super.logId);
//			
//			//Se o assinante existe
//			if (dadosAssinante != null)
//			{
//				// Seleciona valor maximo de saldo
//				saldoMaximo = Short.parseShort(mapConfig.getMapValorConfiguracaoGPP("SALDO_MAXIMO"));
//				
//				super.log(Definicoes.DEBUG, "validaAssinante", "Status do assinante " + dadosAssinante.getStatusAssinante()+"\n Status do servico do assinante " + dadosAssinante.getStatusServico()+"\n Saldo Maximo permitido: " + saldoMaximo);			
//				// valida status do assinante
//				if (dadosAssinante.getStatusAssinante()== Definicoes.SHUT_DOWN ||
//					dadosAssinante.getStatusAssinante()== Definicoes.RECHARGE_EXPIRED ||
//					dadosAssinante.getStatusAssinante()== Definicoes.DISCONNECTED )
//					retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
//				// valida status do servico
//				else if (dadosAssinante.getStatusServico() == Definicoes.SERVICO_BLOQUEADO)
//					retorno = Definicoes.RET_MSISDN_BLOQUEADO;
//				// valida valor do saldo maximo
//				/*
//				else if ( ( aValorRecarga + dadosAssinante.getSaldoCreditos() ) > saldoMaximo )
//					retorno = Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO;
//				// valida valor de recarga
//				else if (aValorRecarga != valorRecarga)
//					retorno = Definicoes.RET_VALOR_CREDITO_INVALIDO;
//				*/
//				
//				//else if (dadosAssinante.getSaldoCreditos() <= 0.00 )
//				//	retorno = Definicoes.RET_CREDITO_INSUFICIENTE;
//			}
//			else
//			{
//				super.log(Definicoes.WARN, "validaAssinante", "Assinante NAO existe na plataforma da Tecnomen.");
//				retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
//			}			
//		}
//		catch (GPPInternalErrorException e1)
//		{
//			super.log(Definicoes.ERRO, "validaAssinante", "Excecao GPP:"+ e1);				
//			throw new GPPInternalErrorException ("Excecao GPP:" + e1);	
//		}
//		finally
//		{
//			// Libera conexao com do pool de recarga da Tecnomen
//			gerenteTecnomen.liberaConexaoRecarga (ta, super.logId);
//		}		
//		super.log(Definicoes.DEBUG, "validaAssinante", "Fim RETORNO "+retorno);
//		return retorno;
//	}
//	
//	
//	private void atualizaDataProximaExecucao(String aMSISDN, java.util.Date dataExecucao,
//							java.util.Date dataInicio, java.util.Date dataFim, PREPConexao aConexaoPrep)throws GPPInternalErrorException, Pi_exception, SQLException{
//		super.log(Definicoes.DEBUG, "atualizaDataProximaExecucao", "Inicio MSISDN "+aMSISDN+" DATAEXEC "+dataExecucao+" PERIODO "+dataInicio+"-"+dataFim);
//	
//		short retorno = Definicoes.RET_OPERACAO_OK;
//		Assinante dadosAssinante = null;
//		short saldoMaximo = 0;
//		ResultSet rs;
//		
//		double valorRecarga=0;
//				
//		try
//		{			
//			//aConexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
//			String sql = "UPDATE  TBL_GER_PROMOCAO_ASSINANTE  "+
//						" SET "+
//						" 	 DAT_EXECUCAO = ? "+
//						" 	, DAT_INICIO_ANALISE = ? "+
//						" 	, DAT_FIM_ANALISE = ? "+
//						" where "+
//						"	IDT_MSISDN = ? ";
//			Object[] parametros = { new java.sql.Date(dataExecucao.getTime()),
//									new java.sql.Date( dataInicio.getTime() ),
//									new java.sql.Date( dataFim.getTime() ),
//									 aMSISDN };
//				
//			aConexaoPrep.executaPreparedUpdate(sql, parametros, aIdProcesso);			
//					
//		}
//		catch (GPPInternalErrorException e1)
//		{
//			super.log(Definicoes.ERRO, "atualizaDataProximaExecucao", "Excecao GPP:"+ e1);				
//			throw new GPPInternalErrorException ("Excecao GPP:" + e1);	
//		}
//		super.log(Definicoes.DEBUG, "atualizaDataProximaExecucao", "Fim RETORNO "+retorno);
//		
//		
//	}
//	private void carregaListaPromocoes() {
//		   super.log(Definicoes.DEBUG, "carregaListaPromocoes", "Inicio");
//
//		   String sql =    " SELECT a.idt_promocao, a.nom_promocao,  a.dat_inicio, "+
//					   " a.dat_fim, a.ind_hibrido, a.dat_inicio_validade, "+
//					   " a.dat_fim_validade, a.ind_tipo, b.val_minuto, a.ind_mes_fechado, a.val_dias, " +
//					   " b.idt_plano_preco "+
//					   " FROM tbl_ger_promocao a, TBL_GER_PROMOCAO_PLANO_PRECO b " +
//					   " where a.dat_inicio <= trunc(sysdate) and "+
//					   " a.dat_fim >= trunc(sysdate) and " +
//					   " A.IDT_PROMOCAO = B.IDT_PROMOCAO";
//		
//	   PREPConexao aConexaoPrep = null; 
//	   ResultSet rs1 = null;
//	   try{
//		   aConexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
//		   rs1 = aConexaoPrep.executaQuery(sql, aIdProcesso);
//		   while(rs1.next()){
//			   Promocao promo = new Promocao();
//			   promo.setId(rs1.getLong(1));
//			   promo.setNome(rs1.getString(2));
//			   promo.setInicioEntrada(rs1.getDate(3));
//			   promo.setFimEntrada(rs1.getDate(4));
//			   promo.setHibrido(rs1.getInt(5));
//			   promo.setInicioValidade(rs1.getDate(6));
//			   promo.setFimValidade(rs1.getDate(7));
//			   promo.setTipo(rs1.getInt(8));
//			   promo.setValorPorMinuto(rs1.getDouble(9));
//			   promo.setMesFechado(rs1.getInt(10));
//			   promo.setNumDias(rs1.getInt(11));
//			   promocoes.put(new Integer(rs1.getInt(12)), promo);
//		   }
//	   }
//	   catch (GPPInternalErrorException e)
//	   {
//		   super.log(Definicoes.ERRO, "carregaListaPromocoes", "Excecao GPP:"+ e);				
//		   //throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
//	   }
//	   catch (Exception e1)
//	   {
//		   super.log(Definicoes.ERRO, "carregaListaPromocoes", "Excecao GPP:"+ e1);				
//		   //throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);			 
//	   }
//	   finally
//	   {
//		   //Libera conexao com do pool de recarga da Tecnomen
//		   gerenteBancoDados.liberaConexaoPREP(aConexaoPrep, super.logId);
//	   }				
//	   super.log(Definicoes.DEBUG, "carregaListaPromocoes", "Fim");
//	}
//	private java.util.Date getUltimoDiaProximoMes(java.util.Date data){
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(data);
//		//cal.roll(Calendar.MONTH, 1);
//		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//		
//		return cal.getTime();
//		
//	}
//	private java.util.Date getPrimeiroDiaProximoMes(java.util.Date data){
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(data);
//		//cal.roll(Calendar.MONTH, 1);
//		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),1);
//		
//		return cal.getTime();
//		
//		
//	}


}