package com.brt.gpp.aplicacoes.aprovisionar;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.ConsumidorSMS;

import java.io.Reader;
import java.sql.ResultSet;
import java.sql.Clob;

public class GerenciadorDesbloqueioHotLine extends Thread
{
	private static GerenciadorDesbloqueioHotLine instance;
	private static boolean						 deveExecutar;
	
	private static final String MENSAGEM = "BrT GSM: O desbloqueio do seu celular foi efetuado com sucesso";
	
	private GerenciadorDesbloqueioHotLine()
	{
		deveExecutar=true;
		// Inicia o processamento das requisicoes
		start();
	}

	public static GerenciadorDesbloqueioHotLine getInstance()
	{
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		if (arqConf.deveGerenciarHotLine())
			if (instance==null)
				instance = new 	GerenciadorDesbloqueioHotLine();
		
		return instance;
	}

	/**
	 * Metodo....:processaRequisicoes
	 * Descricao.:Realiza a verificacao das requisicoes existentes na tabela de interface
	 *            e processas as informacoes. Caso o desbloqueio de hot-line tenha sido 
	 *            concluido entao envia uma mensagem SMS para o usuario indicando a conclusao
	 */
	private void processaRequisicoes()
	{
		PREPConexao conexaoPrep = null;
		long idRequisicao=0;
		try
		{
			// Realiza a pesquisa na tabela de interface pelo evento de negocio
			// especifico sendo que para cada linha realiza o parse do xml
			// verificando se a solicitacao de hot-line foi executada com sucesso
			// sendo entao enviado uma msg caso positivo.
			conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
			String sqlPesquisa = "select id_processamento,xml_document " +
			                       "from tbl_int_ppp_in " +
			                      "where idt_status_processamento = ? " +
			                        "and idt_evento_negocio       = ? ";

			Object params[] = {Definicoes.IDT_PROCESSAMENTO_NOT_OK,Definicoes.IDT_EVENTO_NEGOCIO_BLOQUEIO};
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,params,0);
			while (rs.next())
			{
				idRequisicao = rs.getLong("ID_PROCESSAMENTO");
				// Realiza o parse da requisicao e utiliza o objeto Cabecalho para
				// identificar os valores da requisicao de desbloqueio hot-line
				CabecalhoXMLApr cabecalhoXML = parseSolicitacao(rs.getClob("XML_DOCUMENT"));
				
				// Verifica se a requisicao realmente foi um desbloqueio hot-line
				// e se o status da mesma indica ok no processamento. Se positivo
				// entao marca o status como processado na tabela de interface
				// e envia um sms para o assinante indicando seu desbloqueio
				if ( Definicoes.RET_S_OPERACAO_OK.equals(cabecalhoXML.getCodigoErro()) )
				{
					// Grava a mensagem na tabela que serah utilizada pelo consumidor
					// de SMS enviado para o middleware de SMS
					ConsumidorSMS.getInstance(0).gravaMensagemSMS(cabecalhoXML.getMsisdn(),MENSAGEM,Definicoes.SMS_PRIORIDADE_ZERO,"SMSHotLine",0);
					
					// Acerta o status na tabela de interface
					acertaInterface(idRequisicao,conexaoPrep);
				}
			}
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(this.getClass()).log(0
															,Definicoes.WARN
															,"GerenciadorDesbloqueioHotLine"
															,"processaRequisicoes"
															,"Erro ao processar requisicao:"+idRequisicao+" Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep,0);
		}
	}

	/**
	 * Metodo....:acertaInterface
	 * Descricao.:Acerta o status do registro na tabela de interface
	 * @param conexaoPrep - Conexao com o banco de dados
	 * @throws Exception
	 */
	private void acertaInterface(long idRequisicao,PREPConexao conexaoPrep) throws Exception
	{
		// Acerta o status na tabela de interface
		String upd = "update tbl_int_ppp_in set idt_status_processamento = ? " +
					  "where id_processamento   = ? " +
					    "and idt_evento_negocio = ? ";

		Object paramUpd[]={Definicoes.IDT_PROCESSAMENTO_OK,new Long(idRequisicao),Definicoes.IDT_EVENTO_NEGOCIO_BLOQUEIO};
		conexaoPrep.executaPreparedUpdate(upd,paramUpd,0);
	}

	/**
	 * Metodo....:parseSolicitacao
	 * Descricao.:Realiza o parse do XML de desbloqueio Hot-Line
	 * @param xmlRequisicao - XML do desbloqueio hot-line
	 * @return CabecalhoXMLApr - Objeto contendo os valores do XML de desbloqueio
	 * @throws Exception
	 */
	private CabecalhoXMLApr parseSolicitacao(Clob xmlRequisicao) throws Exception
	{
		// Realiza a leitura do objeto CLOB para posteriormente executar
		// o parse do XML de requisicao afim de identificar as propriedades
	    Reader chr_instream;
	    char chr_buffer[] = new char[(int)xmlRequisicao.length()];
		chr_instream = xmlRequisicao.getCharacterStream();
		chr_instream.read(chr_buffer);

		// Cria uma referencia para a classe que realizara o parse do xml de retorno
		// do sistema de aprovisionamento e retorna uma objeto cabecalho
		// contendo os valores informados do bloqueio/desbloqueio 
		ParserXMLApr xmlParser = new ParserXMLApr();
		return xmlParser.parseCabecalhoXMLBloqueio(new String(chr_buffer));
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			// Identifica o tempo de espera que esse processo irah esperar para o proximo ciclo
			// de processamento. Para cada ciclo é realizada a analise na tabela de interface
			// para a pesquisa de requisicoes de desbloqueio hot-line
			MapConfiguracaoGPP conf = MapConfiguracaoGPP.getInstancia();
			int tempoEspera = Integer.parseInt(conf.getMapValorConfiguracaoGPP("TEMPO_ESPERA_GERENCIADOR_HOT_LINE"));
			while(deveExecutar)
			{
				GerentePoolLog.getInstancia(this.getClass()).log(0
																,Definicoes.INFO
																,"GerenciadorDesbloqueioHotLine"
																,"run"
																,"Pesquisando desbloqueios de hot-line efetivados");

				// Processa as requisicoes e vai dormir ateh proximo processamento
				processaRequisicoes();
				try{
					Thread.sleep(tempoEspera*1000);
				}
				catch(InterruptedException i){};
			}
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(this.getClass()).log(0
															,Definicoes.ERRO
															,"GerenciadorDesbloqueioHotLine"
															,"run"
															,"Erro processando requisicoes de desbloqueio hot-line. Erro:"+e);
		}
	}
}
