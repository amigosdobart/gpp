package com.brt.gpp.aplicacoes.bloquear;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conex�o com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

//Arquivos de Import Internos
import com.brt.gpp.comum.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.aplicacoes.aprovisionar.GerarXMLAprovisionamento;
import com.brt.gpp.aplicacoes.aprovisionar.CabecalhoXMLApr;
import com.brt.gpp.aplicacoes.aprovisionar.ElementoXMLApr;
import com.brt.gpp.comum.Definicoes;

//Arquivos Java
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;


/**
* Este arquivo refere-se � classe BloquearPorSaldo, responsavel pela implementacao 
* do processo de Bloqueio dos servi�os de Free Call e Identificador de Chamadas
* dos usu�rios que tenham passado para o status de Recharge Expired
* ou cujo saldo tenha atingido um limite m�nimo estabelecido pela BrT
*
* <P> Versao:			1.0
*
* @Autor: 			Denys Oliveira
* Data: 				22/07/2004
*
* Modificado por: Joao Carlos
* Data:15/09/2004
* Razao: Necessidade de que os servicos a serem bloqueados/desbloqueados sao diferentes se o 
*        o bloqueio e por saldo minimo ou por status de Recharge Expired 
* 
* Modificado por: Denys Oliveira
* Data: 11/10/2004
* Razao: Redefini��o da l�gica de sele��o dos usu�rio a terem seus servi�os bloqueados/desbloqueados
*
*/
public class BloquearServico extends Aplicacoes 
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	private MapConfiguracaoGPP mapConfiguracaoGPP;
	
	// Instancia de Acumulador de Solicitacoes
	AcumularSolicitacoesBloqueio acumuladorSolicitacoes;
	
	/**
	 * Metodo...: CalcularIndiceBonificacao
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public BloquearServico (long logId)
	 {
		super(logId, Definicoes.CL_BLOQUEIO_POR_SALDO);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		
		// Obtem uma instancia do acumulador de bloqueios
		this.acumuladorSolicitacoes = AcumularSolicitacoesBloqueio.getInstancia();
	 }
	 
	 /**
	  * Metodo...: bloquearServicosBatch
	  * Descricao: Bloqueia/Desbloqueia Servi�os em Massa
	  * @param 	String	dataReferencia		Data para efeitos de hist�ricos
	  * @param 	boolean	verificaRE			Diz se os usu�rios que passaram para status RE devem ser verificados
	  * @return	short	0, ok; !0, nok
	  * @throws GPPInternalErrorException
	  */
	 public short bloquearServicosBatch(String dataReferencia) throws GPPInternalErrorException
	 {
		String status = null;
		PREPConexao conexaoPrep = null; 
		short retorno = 0;
		int qtdIndicadores[] = {0,0,0};
		java.sql.Timestamp dataHora = null;
		
		try
		{
			super.log(Definicoes.INFO,"bloquearServicosBatch","Inicio do Bloquear Servicos");
			
			// Pega a data de in�cio de execu��o
			Calendar chronos = Calendar.getInstance();
			dataHora = new java.sql.Timestamp(chronos.getTimeInMillis());
			
			//	Pega conex�o com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());

			// Busca Limite M�nimo de Saldo
			this.mapConfiguracaoGPP = MapConfiguracaoGPP.getInstancia();
			
			// Atualiza tabela de bloqueios com as informa��es retornadas pelo ASAP
			super.log(Definicoes.INFO,"bloquearServicosBatch", "Atualizando Ultimos Lancamentos");
			this.atualizaUltimosLancamentos(conexaoPrep);
			
			// Gera solicita��es de bloqueio/desbloqueio a partir do arquivo gerado na Tecnomen
			GerarBloqueiosDesbloqueios geradorBloqueiosDesbloqueios = new GerarBloqueiosDesbloqueios(this.mapConfiguracaoGPP.getMapValorConfiguracaoGPP("DIR_ARQUIVOS_BLOQUEIO"), super.getIdLog());
			HashMap hashBloqueiosDesbloqueios = new HashMap();
			hashBloqueiosDesbloqueios = geradorBloqueiosDesbloqueios.getSolicitacoes();
			
			// que deve ser a data dos registros de bloqueio/desbloqueio
			super.log(Definicoes.INFO,"bloquearServicosBatch","Solicitando os Bloqueios/Desbloqueios");
			qtdIndicadores = this.administraSolicitacoesBloqueioDesbloqueio(hashBloqueiosDesbloqueios, dataHora, conexaoPrep);
			
			// Insere os XMLs na TBL_INT_PPP_OUT
			super.log(Definicoes.INFO, "bloquearServicosBatch","Enviando Bloqueios/Desbloqueios para Aprovisionamento");
			this.enviaAcoesInterface(dataHora);
			
			// String no hist�rico apresentar� sucesso
			status = Definicoes.TIPO_OPER_SUCESSO;
		}
		catch(SQLException eSQL)
		{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.WARN, "bloquearServicosBatch", "Erro SQL: "+ eSQL);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + eSQL);					
		}
		catch(Exception eG)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO,"bloquearServicosBatch","Erro GPP: "+eG);
		}
		finally
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			super.log(Definicoes.INFO, "bloquearServicosBatch", "Fim");
			
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Chama a funcao para gravar no historico o Processo em quest�o
			String descricao = "Realizados "+qtdIndicadores[0]+" bloqueios," + qtdIndicadores[1] + " desbloqueios, "+qtdIndicadores[2]+" solicitacoes inviaveis";
			super.gravaHistoricoProcessos(Definicoes.IND_BLOQUEIO_POR_SALDO, sdf.format(dataHora), GPPData.dataCompletaForamtada(), status, descricao, dataReferencia);
		}
		return retorno;	 	 
	}
	
	/**
	 * Metodo...: enviaAcoesInterface
	 * Descricao: Percorre as a��es acumuladas e envia solicita��es de bloqueio/desbloqueio de servi�os
	 * 				por msisdn para a TBL_INT_PPP_OUT
	 * @param 	Timestamp	data		Data de execu��o do processo
	 * @throws GPPInternalErrorException
	 */
	private void enviaAcoesInterface(java.sql.Timestamp data) throws GPPInternalErrorException
	{
		String xmlBloqueio = null;
		PREPConexao conexaoPrep = null;
		ElementoXMLApr sB = new ElementoXMLApr();
		String msisdn = null;
		String nroOS;
		String sqlInsertInterface = "INSERT INTO TBL_INT_PPP_OUT " +
			"(ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) VALUES " +
			"(?,?,?,?,?)";
		
		String idProcessamento = this.mapConfiguracaoGPP.getMapValorConfiguracaoGPP("ID_PROC_INT_DEH");
		Long.parseLong(this.mapConfiguracaoGPP.getMapValorConfiguracaoGPP("QTD_MAX_OSS_INTERACAO"));
		
		try
		{			
			// Pega conex�o com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
				
			// Pega o treeMap acumulador de acoes
			TreeMap tmAcoes = this.acumuladorSolicitacoes.getMapAcoes();
			
			// Cria um vector para receber os valores da tree map
			Vector vcAcoesInterface = new Vector();
			super.log(Definicoes.INFO,"enviaAcoesInterface","Vetorizando solicitacoes");
			vcAcoesInterface.addAll(tmAcoes.values());
	
			super.log(Definicoes.INFO,"enviaAcoesInterface","Montando e Inserindo os xml's na ppp_out");
			// Verifica se h� solicita��es
			if(!vcAcoesInterface.isEmpty())
			{
				// Pega Primeira Solicita��o
				int j=0;
				sB = (ElementoXMLApr) vcAcoesInterface.get(j);
			
				// Indicador de que n�o se chegou no �ltimo elemento
				boolean chegouNoUltimo = false;		
	
				// Processa cada uma das solicita��es de bloqueio/desbloqueio do vetor
				super.log(Definicoes.INFO, "enviaAcoesInterface","Inserindo XML's na TBL_INT_PPP_OUT");
				long qtdOs = 0;
				//while(j < vcAcoesInterface.size() && qtdOs <= qtdMaximaOSs )
				while(j < vcAcoesInterface.size() )
				{
					nroOS = GPPData.formataNumero(new Integer(this.getNumeroOS(conexaoPrep)).intValue(),13);
					CabecalhoXMLApr cab = new CabecalhoXMLApr(	Definicoes.SO_GPP + nroOS,
																Definicoes.XML_OS_CASE_SUB_TYPE, 
																Definicoes.XML_OS_CASE_SUB_TYPE, 
																//Definicoes.XML_OS_CATEGORIA,
																this.getCategoria(sB.getMsisdn(), conexaoPrep),
																Definicoes.XML_OS_ORDER_PRIORITY, 
																sB.getMsisdn());
					ElementoXMLApr elmApr;
					Vector listaElmApr = new Vector();
				
					// Cria objeto para n� do servi�o "ELM_INFO_SIMCARD"
					elmApr = new ElementoXMLApr(Definicoes.SERVICO_INFO_SIMCARD,Definicoes.XML_OS_X_TIPO_SIMCARD, Definicoes.XML_OS_OP_SIMCARD, Definicoes.XML_OS_STATUS);
					listaElmApr.add(elmApr);
						
					// Monta os n�s de solicita��es do xml para esse msisdn
					do
					{
						// Adiciona os tags do campo ELM_xxx dependendo do servi�o
						elmApr = new ElementoXMLApr(sB.getMacroServico(), Definicoes.XML_OS_X_TIPO, Definicoes.XML_OS_STATUS, sB.getOperacao());
						listaElmApr.add(elmApr);
					
						// Pega pr�ximo servi�o, caso exista, para ver se continua sendo o mesmo msisdn
						if(++j < vcAcoesInterface.size())
							sB = (ElementoXMLApr) vcAcoesInterface.get(j);
						else
							chegouNoUltimo = true;
					}while(sB.getMsisdn().equals(msisdn) && !chegouNoUltimo);

					GerarXMLAprovisionamento geradorXml = new GerarXMLAprovisionamento(cab, listaElmApr);
					xmlBloqueio = geradorXml.getAprXML();

					// Insere registro com XML na TBL_INT_PPP_OUT
					Object Params[] = 	{	nroOS,
											data,
											Definicoes.IDT_EVENTO_NEGOCIO_BLOQUEIO,
											xmlBloqueio,
											idProcessamento
										};
					conexaoPrep.executaPreparedUpdate(sqlInsertInterface,Params,super.getIdLog());
					qtdOs++;
				}	
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "enviaAcoesInterface", "Erro Interno GPP: "+e);						
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);			
		}
		finally
		{
			//	Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());			
		}
	}
	
	/**
	 * Metodo...: getNumeroOS
	 * Descricao: retorna um n�mero de OS para o XML do provision (pega numa sequence do banco de dados)
	 * @return	String	identificador da OS do provision
	 * @throws GPPInternalErrorException
	 */
	private String getNumeroOS(PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		String retorno = null;
		ResultSet rsSeq = null;
		
		String sqlSequence = "SELECT SEQ_OS_PROVISION.NEXTVAL AS ID_OS FROM DUAL";
		
		try
		{
			// Retorna o pr�ximo elemento da sequence do Banco de Dados
			rsSeq = conexaoPrep.executaQuery1(sqlSequence,super.getIdLog());
			rsSeq.next();
			retorno = rsSeq.getString("ID_OS");			
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.WARN, "getNumeroOS", "Erro SQL: "+ sqlE);
			throw new GPPInternalErrorException("Erro GPP: "+ sqlE);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: administraSolicitacoesBloqueioDesbloqueio
	 * Descricao: A partir do resultSet contendo os assinantes que devem ser bloqueados/desbloqueados,
	 * 				construir as estruturas e tomar as a��es necess�rias para efetivar esse bloqueio/desbloqueio
	 * @param 	Timestamp		dataProcessoBatch		Data do processo batch
	 * @param 	ResultSet		rsListaBloqueio			ResultSet com a lista de assinantes que ter�o servi�os bloqueados/desbloqueados
	 * @return	int[]									[0] - qtd de Bloqueios Efetuados
	 * 													[1] - qtd de Bloqueios Pendentes
	 * 													[2] - qtd de Desbloqueios Efetuados
	 * 													[3] - qtd de Solicita��es Imposs�veis	
	 */
	public int[] administraSolicitacoesBloqueioDesbloqueio(HashMap hashSolicitacoes, Timestamp dataHora, PREPConexao conexaoPrep) throws SQLException, GPPInternalErrorException
	{
		// Passa o hash de solicita��es para o singleton comum
		AcumularSolicitacoesBloqueio acumuladorSolicitacoes = AcumularSolicitacoesBloqueio.getInstancia();
		acumuladorSolicitacoes.resetaSingleton(this.getLimitesDesprezoDesbloqueios(conexaoPrep));
		acumuladorSolicitacoes.setHashBloqueios(hashSolicitacoes);
		
		// Inicializa as threads consumidoras de Solicita��es de Bloqueio/Desbloqueio
		int numThreads = Integer.parseInt(this.mapConfiguracaoGPP.getMapValorConfiguracaoGPP("NUM_THREADS_BLOQUEIO"));
		for(int j=1;j<=numThreads;j++)
		{
			Runnable run = null;
			run = new ConsumoBloqueio(dataHora);
			
			Thread t = new Thread(run);
			t.start();
		}
		
		try
		{
			// Espera que todas as threads terminem seu trabalho
			while (acumuladorSolicitacoes.getNumThreadsAtivas() != 0)
			{
				Thread.sleep(5000);
			}
		}
		catch(InterruptedException iE)
		{
			super.log(Definicoes.ERRO,"administraSolicitacoesBloqueioDesbloqueio","Erro ao esperar processamento de Threads");
		}
		
		int[] retorno = {	acumuladorSolicitacoes.getQtdBloqueios(), 
							acumuladorSolicitacoes.getQtdDesbloqueios(),
							acumuladorSolicitacoes.getQtdImpossiveis()
						};
		return retorno;
	}
	
	/**
	 * M�todo...: getLimitesDesprezoDesbloqueios
	 * Descricao: Verifica quantas OS's j� foram enviadas para o ASAP no dia atual e o limite a partir
	 * 			do qual devem ser enviados somente bloqueios
	 * @param 	PREPConexao 	conexaoPrep		Conex�o com Banco de Dados
	 * @return	long[]			[0]: N�mero de OS's j� enviadas hoje para o ASAP
	 * 							[1]: Limite a partir do qual os desbloqueios ser�o desprezados
	 */
	private long[] getLimitesDesprezoDesbloqueios(PREPConexao conexaoPrep)
	{
		long[] retorno = {0,0};
		
		String sqlTotalOs = "select "+
				"sum(substr(des_observacao,12,instr(des_observacao,' bloqueios')-12) + "+
				"substr(des_observacao, instr(des_observacao,'bloqueios,')+10, "+ 
				"instr(des_observacao, 'desbloqueios')-instr(des_observacao,' bloqueios,')-11)) as OSs "+
				"from tbl_ger_historico_proc_batch where id_processo_batch = 30 "+
				"and dat_inicial_Execucao >= trunc(sysdate) and dat_inicial_execucao < trunc(sysdate+1)";
		
		try
		{
			ResultSet rs = conexaoPrep.executaQuery(sqlTotalOs, super.getIdLog());
			
			if(rs.next())
			{
				retorno[0] = rs.getLong("OSs");
			}
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"getLimitesDesprezoDesbloqueios","Erro Banco Dados: "+sqlE);
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"getLimitesDesprezoDesbloqueios","Erro Banco Dados: "+gppE);
		}

		retorno[1] = Long.parseLong(mapConfiguracaoGPP.getMapValorConfiguracaoGPP("LIMITE_OS_DESPREZO_DESBLOQUEIO_DEH"));
		
		return retorno;
	}
	
	/**
	 * Metodo...: atualizaUltimosLancamentos
	 * Descricao: A partir dos novos registros que foram disponibilizados na TBL_INT_PPP_IN,
	 * 			atualiza o status do bloqueio na TBL_APR_BLOQUEIO_SERVICO ou apaga o registro, no
	 * 			caso de um desbloqueio conclu�do
	 * @param 	java.sql.Timestamp	data	Data do processamento batch
	 * @return	0 se ok; 
	 * 			int Definicoes.RET_BLOQDESBLOQ_NAO_SOLICITADO caso o bloqueio/desbloqueio da PPP_IN n�o 
	 * 				tenha sido solicitado 
	 * @throws GPPInternalErrorException
	 */
	private short atualizaUltimosLancamentos(PREPConexao conexaoPrep) throws GPPInternalErrorException, GPPBadXMLFormatException
	{
		short retorno = 0;
		
//		// Extrai todos os registros que foram processados com sucesso da TBL_INT_PPP_IN
//		String sqlNovosLancamentos = "SELECT ID_PROCESSAMENTO, XML_DOCUMENT FROM TBL_INT_PPP_IN "+
//			"WHERE IDT_EVENTO_NEGOCIO = ? AND IDT_STATUS_PROCESSAMENTO = ? ORDER BY DAT_CADASTRO";
			
		// Atualiza PPP_IN para cada XML (cada registro lido da PPP_IN
		String sqlPPPIn = "UPDATE TBL_INT_PPP_IN SET IDT_STATUS_PROCESSAMENTO = ? WHERE IDT_EVENTO_NEGOCIO = ? AND IDT_STATUS_PROCESSAMENTO = 'N'";
		Object sqlPPPInParams[] = {Definicoes.IDT_PROCESSAMENTO_OK, Definicoes.IDT_EVENTO_NEGOCIO_BLOQUEIO};
		conexaoPrep.executaPreparedUpdate(sqlPPPIn,sqlPPPInParams,super.getIdLog());
		
		return retorno;
	}
	
	/**
	 * Metodo...: getCategoria
	 * Descricao: Indica se � um pr�-pago ou h�brido
	 * @param 	String	msisdn	Msisdn do acesso
	 * @return	String	F2 para pr�-pago e F3 para h�brido
	 */
	private String getCategoria(String msisdn, PREPConexao conexaoPrep)
	{
		String retorno = Definicoes.XML_OS_CATEGORIA_PREPAGO;
		
		try
		{
			// Verifica se o usu�rio consta na tbl_apr_plano_hibrido
			String sqlCategoria = "SELECT 1 FROM TBL_APR_PLANO_HIBRIDO WHERE IDT_MSISDN = ?";
			Object[] paramCategoria = {msisdn};
			ResultSet rsCategoria = conexaoPrep.executaPreparedQuery(sqlCategoria, paramCategoria, super.getIdLog());
			
			if(rsCategoria.next())
			{
				retorno = Definicoes.XML_OS_CATEGORIA_HIBRIDO;
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"getCategoria","Problema Banco de Dados: "+e);
		}
		return retorno;
	}
}
