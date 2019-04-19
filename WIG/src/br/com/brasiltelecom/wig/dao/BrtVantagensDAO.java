package br.com.brasiltelecom.wig.dao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.action.BrtVantagensXMLParser;

import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author JOAO PAULO GALVAGNI
 * @since 03/04/2006
 * 
 */
public class BrtVantagensDAO
{
	private Logger logger = Logger.getLogger(this.getClass());
	private	static 	String insereInterfaceVitriaOut =  "INSERT INTO INTERFACE_VITRIA_OUT	" +
	                                                   "(SEQUENCIAL							" +
	                                                   ",PROCESSO							" +
	                                                   ",ID_SISTEMA							" +
	                                                   ",DATA_INCLUSAO						" +
	                                                   ",DATA_LEITURA						" +
	                                                   ",NR_PRIORIDADE						" +
	                                                   ",XML								" +
	                                                   ",CONTROLE_VITRIA)					" +
	                                                   "VALUES (SEQ_INT_VITRIA_OUT.NEXTVAL	" +
	                                                   "	   ,?							" +
	                                                   "	   ,?							" +
	                                                   "	   ,sysdate						" +
	                                                   "	   ,null						" +
	                                                   "	   ,0							" +
	                                                   "	   ,?							" +
	                                                   "	   ,'N')						";
	
	private	static 	String insereTabelaBackUpVilela =   "INSERT INTO TABELA_BKUP_VILELA		" +
													    "(SEQUENCIAL						" +
													    ",PROCESSO							" +
													    ",ID_SISTEMA						" +
													    ",DATA_INCLUSAO						" +
													    ",DATA_LEITURA						" +
													    ",NR_PRIORIDADE						" +
													    ",XML								" +
													    ",CONTROLE_VITRIA)					" +
													    "VALUES (SEQ_INT_VITRIA_OUT.NEXTVAL	" +
													    "	   ,?							" +
													    "	   ,?							" +
													    "	   ,sysdate						" +
													    "	   ,null						" +
													    "	   ,0							" +
													    "	   ,?							" +
													    "	   ,'N')						";
	
	// XMLs FAKE PARA TESTES DE MODULO
	/*private static String xmlConsultaNumeroSAC ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
												"<mensagem>" +
												"	<cabecalho>" +
												"		<empresa>BRG</empresa>" +
												"		<sistema>SIB</sistema>" +
												"		<processo>REL000CONCLIENT</processo>" +
												"		<data>10/08/2006 15:02:26</data>" +
												"		<identificador_requisicao>6184310528</identificador_requisicao>" +
												"		<codigo_erro>0</codigo_erro>" +
												"		<descricao_erro/>" +
												"	</cabecalho>" +
												"	<conteudo><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
												"	<root>" +
												"<mensagem>" +
												"	<cabecalho>" +
												"		<emrpesa>BRG</emrpesa>" +
												"		<sistema>G-OTA</sistema>" +
												"		<processo>ATH / BTM</processo>" +
												"		<data>2006-03-27 18:00:00</data>" +
												"		<identificador_requisicao>6184310528</identificador_requisicao>" +
												"	</cabecalho>" +
												"	<conteudo>" +
												"		<cod_retorno>00</cod_retorno>" +
												"		<mensagem>OK</mensagem>" +
												"		<cod_erro/>" +
												"	</conteudo>" +
												"</mensagem></root>]]></conteudo>" +
												"</mensagem>";
	
	private static String xmlConsultaContratoClarify =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
														"<mensagem>" +
														"	<cabecalho>" +
														"		<empresa>BRG</empresa>" +
														"		<sistema>SIB</sistema>" +
														"		<processo>REL000CONCLIENT</processo>" +
														"		<data>10/08/2006 15:02:26</data>" +
														"		<identificador_requisicao>6184310528</identificador_requisicao>" +
														"		<codigo_erro>0</codigo_erro>" +
														"		<descricao_erro/>" +
														"	</cabecalho>" +
														"	<conteudo><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
														"<root>" +
														"	<cod_retorno>0</cod_retorno>" +
														"	<descricao_cod_retorno/>" +
														"	<canal_vendas>Contingência Microsiga</canal_vendas>" +
														"	<categoria_primaria>Pre-pago</categoria_primaria>" +
														"	<status_acesso>Ativo</status_acesso>" +
														"	<indic_bloqueio>N</indic_bloqueio>" +
														"	<dt_nasc>01/01/1753</dt_nasc>" +
														"	<tipo_pessoa>Pessoa Física</tipo_pessoa>" +
														"	<amigos_toda_hora>" +
														"		<qtd_amigos_toda_hora_cel>7</qtd_amigos_toda_hora_cel>" +
														"		<qtd_amigos_toda_hora_fixa>2</qtd_amigos_toda_hora_fixa>" +
														"		<qtd_amigos_toda_hora>7</qtd_amigos_toda_hora>" +
														"		<dias_ultima_alteracao_irmaos14>563</dias_ultima_alteracao_irmaos14>" +
														"		<dias_atualizacao_irmaos14>30</dias_atualizacao_irmaos14>" +
														"		<cobranca>N</cobranca>" +
														// Brasil Vantagens
														//"		<servico>ELM_G14_ID0990</servico>" +
														// Brasil Vantagens
														// Novissimo Brasil Vantagens
														"		<servico>ELM_AMGS_TDHORA_V02_ID1885</servico>" +
														// Novissimo Brasil Vantagens
														"		<acesso_movel>" +
														"			<msisdn>6184238505</msisdn>" +
														"			<msisdn>6184022372</msisdn>" +
														"			<msisdn>6184238552</msisdn>" +
														"			<msisdn></msisdn>" +
														"			<msisdn></msisdn>" +
														// Novissimo Brasil Vantagens
														"			<msisdn>6133615190</msisdn>" +
														"			<msisdn>6133672328</msisdn>" +
														// Novissimo Brasil Vantagens
														"		</acesso_movel>" +
														"		<terminal>" +
														// Brasil Vantagens
														//"			<msisdn>6133615190</msisdn>" +
														//"			<msisdn>6133672328</msisdn>" +
														// Brasil Vantagens
														"		</terminal>" +
														"	</amigos_toda_hora>" +
														"	<bonus_todo_mes>" +
														"		<dias_ultima_alteracao_bonus>406</dias_ultima_alteracao_bonus>" +
														"		<dias_atualizacao_bonus>30</dias_atualizacao_bonus>" +
														"		<qtd_bonus_todo_mes>0</qtd_bonus_todo_mes>" +
														"		<cobranca>N</cobranca>" +
														"		<servico>ELM_IRMAO14_ID1014</servico>" +
														"		<msisdn>6133615190</msisdn>" +
														"	</bonus_todo_mes>" +
														// Novissimo Brasil Vantagens
														"	<bumerangue_14>" +
														"		<ativo>S</ativo>" +
														"	</bumerangue_14>" +
														// Novissimo Brasil Vantagens
														"	<dias_ultima_alteracao_brasil_v>30</dias_ultima_alteracao_brasil_v>" +
														"	<dias_atualizacao_brasil_v>30</dias_atualizacao_brasil_v>" +
														"	<bloq_os>RET_OS</bloq_os>" +
														"	<indic_mala_direta>N</indic_mala_direta>" +
														"	<iccid/>" +
														"</root>" +
														"]]></conteudo>" +
														"</mensagem>";
	*/
	/**
	 * Metodo....: getBrtVantagensByMsisdn
	 * Descricao.: Recebe o resultado da consulta de contrato do cliente
	 * 			   e faz uma chamada para o parse do XML de retorno
	 * 
	 * @param msisdn		- Msisdn do cliente que esta realizando a solicitacao
	 * @param conn			- Conexao com a base de dados do Clarify
	 * @return BrtVantagem	- Objeto a ser retornado a classe solicitante
	 * @throws Exception	- Excecao de parse / consulta ao EntireX (Clarify)
	 */
	public BrtVantagem getBrtVantagensByMsisdn(String msisdn, String ip, int porta) throws Exception 
	{
		// Objeto BrtVantagem para conter os dados
		// do cliente apos o Parse do XML
		BrtVantagem brtVantagem = new BrtVantagem();
		// Instancia do BrtVantagensXMLParser
		BrtVantagensXMLParser parseXML = new BrtVantagensXMLParser();
		try 
		{
			// Realiza a consulta via socket para buscar os valores do BrtVantagens
			// para o assinante e em seguida realiza o parse das informacoes
			// Atraves do objeto resultado entao realiza o envio dos dados para ser
			// montado o WML que irah para o usuario
			String xmlConsulta = enviaXMLConsulta(parseXML.getXMLConsBrtVantagens(msisdn), ip, porta);
			// XML FAKE PARA TESTES DE MODULO
			//String xmlConsulta = xmlConsultaContratoClarify;
			
			if (!"".equals(xmlConsulta))
			{
				logger.debug("XML de retorno do EntireX (Clarify): \n" + xmlConsulta);
				brtVantagem = parseXML.parseBrtVantagens(xmlConsulta);
			}
		}
		catch (Exception e) 
		{
			// Grava no LOG uma entrada de ERROR
			logger.error("Erro ao enviar o XML de consulta para o numero "+msisdn+" Servidor: "+ip+":"+porta+"\nErro: ",e);
			throw e;
		}
		// Retorno do objeto brtVantagem ao solicitante
		return brtVantagem;
	}
	
	/**
	 * Metodo	: enviaXMLConsulta
	 * Descricao: Realiza a consulta de dados enviando as informacoes via Socket TCP/IP
	 * 
	 * @param xml 		- XML de consulta
	 * @param ip		- Endereco do servidor
	 * @param porta		- Porta do servidor socket
	 * @return String	- XML de retorno da consulta
	 * @throws Exception- Excecao na consulta do assinante no Clarify
	 */
	public String enviaXMLConsulta(String xml, String ip, int porta) throws Exception
	{
		StringBuffer retorno = new StringBuffer();
		Socket sock = null;
		DataInputStream dis = null;
		BufferedReader bis = null;
		PrintStream ps = null;
		
		
		try
		{
			long t1 = System.currentTimeMillis();
			logger.debug("Inicio da conexao via Socket com o EntireX.");
			
			// Inicializa socket com o servidor para a consulta de dados
			sock = new Socket(ip, porta);
			dis = new DataInputStream(sock.getInputStream());
            byte buffer[] = new byte[4096];
			bis = new BufferedReader(new InputStreamReader(dis));
            ps = new PrintStream(sock.getOutputStream());
            
            logger.debug("Enviando consulta de dados para o servidor: "+ip+":"+porta + "\nXML: " + xml);
            
            ps.println(xml);
            logger.debug("Tempo de envio: " + (System.currentTimeMillis() - t1));
            //System.out.println("Tempo de envio: " + (System.currentTimeMillis() - t1));
            
            t1 = System.currentTimeMillis();
            int l = 0;
            while((l=sock.getInputStream().read(buffer)) > 0)
            	retorno.append(new String(buffer, 0, l));
            
            logger.debug("Tempo de resposta: " + (System.currentTimeMillis() - t1));
            //System.out.println("Tempo de resposta: " + (System.currentTimeMillis() - t1));
		}
		catch(Exception e)
		{
			logger.error("Erro ao enviar dados para a consulta no servidor:"+ip+":"+porta+"\nErro: \n",e);
		}
		finally
		{
			try
			{
				if ( ps != null)
					ps.close();
				if ( bis != null)
					bis.close();
				if ( dis != null)
					dis.close();
				if ( sock != null)
					sock.close();
			}
			catch (Exception e)
			{
				logger.error("Erro no fechamento da conexao socket com EntireX. Erro: ", e);
				throw e;
			}
		}
		
		return retorno.toString();
	}
	
	/**
	 * Metodo....: insereAlteracaoBrtVantagens
	 * Descricao.: Insere no Banco de Dados do G-OTA (WIG) o
	 * 			   XML para posterior leitura pelo Vitria
	 * 
	 * @param con			- Conexao com o Banco de Dados
	 * @param processo		- Codigo do processo
	 * @param id_sistema	- ID do sistema de origem
	 * @param xml			- xml a ser gravado na tabela de saida
	 * @throws SQLException
	 */
	public void insereAlteracaoBrtVantagens(Connection con, String processo, String id_sistema, String xml) throws SQLException
	{
		PreparedStatement pstmt = null;
		try
		{
			pstmt = con.prepareStatement(insereInterfaceVitriaOut);
			pstmt.setString(1,processo);
			pstmt.setString(2,id_sistema);
			pstmt.setString(3,xml);
			if (pstmt.executeUpdate() == 1)
				con.commit();
		}
		catch(SQLException e)
		{
			logger.error("Erro na tentativa de inserir os dados no banco. ERRO: " + e);
			con.rollback();
			throw e;
		}
		finally
		{
			if (pstmt != null) 
				pstmt.close();
		}
	}
	
	/**
	 * Metodo....: validaNumeroBrtVantagem
	 * Descricao.: Valida um numero enviando os dados para o SAC
	 * 
	 * @param msisdn - Msisdn a ser validado
	 * @param regra  - Regra utilizada (ATH ou BTM)
	 * @param ip 	 - Servidor socket
	 * @param porta  - porta do servidor socket
	 * @param plano	 - Plano do assinante (Pre, Pos ou Hibrido)
	 * @return MensagemRetorno
	 */
	public MensagemRetorno validaNumeroBrtVantagem(String msisdn, String regra, String ip, int porta, String plano) throws Exception
	{
		// Cria a instancia da classe que realizara a construcao
		// do XML de consulta e parse do resultado da pesquisa
		try
		{
			BrtVantagensXMLParser parserXML = new BrtVantagensXMLParser();
			
			// Busca entao o XML a ser enviado para a pesquisa e validacao do numero
			// e em seguida realiza o parse do resultado enviando entao ao programa
			// que realizou a consulta
			String xmlRetorno = enviaXMLConsulta(parserXML.getXMLValidaBrtVantagens(msisdn,regra, plano), ip, porta);
			
			// XML FAKE PARA TESTES DE MODULO
			//String xmlRetorno = xmlConsultaNumeroSAC;
			
			logger.debug("XML de retorno apos consulta ao SAC:\n" + xmlRetorno);
			return parserXML.parseXMLRetorno(xmlRetorno);
		}
		catch (Exception e)
		{
			logger.error("Erro na validacao do numero junto ao SAC.", e);
			throw e;
		}
	}
	
	public void insereRegistroAlteracao(long id, String msisdn, String xml)
	{
		// TODO: Gravacao dos dados no Banco: BACK UP DO VILELA
	}
	
	/**
	 * Metodo....: getBrtVantagensByMsisdn
	 * Descricao.: Solicita e recebe a consulta de contrato do cliente
	 * 			   e realiza o parse do XML de retorno
	 * 
	 * @param msisdn		- Msisdn do assinante que esta realizando a solicitacao
	 * @param con			- Conexao com o Banco de Dados do Clarify
	 * @return BrtVantagem	- Objeto a ser retornado com os dados do assinante
	 * @deprecated			- Nao deve ser utilizada, somente atraves do EntireX (Socket)
	 */
	public BrtVantagem getBrtVantagensByMsisdn(String msisdn, Connection con) 
	{
		// Objeto BrtVantagem para conter os dados
		// do cliente apos o Parse do XML
		BrtVantagem brtVantagem = new BrtVantagem();
		// Instancia do BrtVantagensXMLParser
		BrtVantagensXMLParser parseXML = new BrtVantagensXMLParser();
		try 
		{
			// Realiza a consulta via procedure para buscar os valores do BrtVantagens
			// para o assinante e em seguida realiza o parse das informacoes
			// Atraves do objeto resultado entao realiza o envio dos dados para ser
			// montado o WML que irah para o usuario
			String xmlConsulta = enviaXMLConsulta(parseXML.getXMLConsBrtVantagens(msisdn), con);
			
			if (!"".equals(xmlConsulta))
			{
				logger.debug("XML de retorno do Clarify: \n" + xmlConsulta);
				brtVantagem = parseXML.parseBrtVantagens(xmlConsulta);
			}
		}
		catch (Exception e) 
		{
			// Grava no LOG uma entrada de ERROR
			logger.error("Erro ao enviar o XML de consulta para o numero "+msisdn+" Erro: ",e);
		}
		
		// Retorno do objeto brtVantagem ao solicitante
		return brtVantagem;
	}
	
	/**
	 * Metodo....: enviaXMLConsulta
	 * Descricao.: Realiza a consulta do contrato do assinante via Procedure,
	 * 			   diretamente no Clarify
	 * 
	 * @param xml		- XML de entrada da procedure do Clarify
	 * @param con		- Conexao com o Banco de Dados do Clarify
	 * @return	retorno	- String contendo o XML de retorno
	 * @deprecated		- Nao deve ser utilizada, somente atraves do EntireX (Socket)
	 */
	public String enviaXMLConsulta(String xml, Connection con)
	{
		String retorno = "";
		CallableStatement stmt = null;
		
		try
		{
			stmt = con.prepareCall("call SA.intp_consulta_acesso_movel_xml(?,?)");
			stmt.setString(1, xml);
			stmt.registerOutParameter(2, Types.VARCHAR);
			long t1 = System.currentTimeMillis();
			stmt.execute();
			
			retorno = stmt.getString(2);
			logger.debug("Tempo de resposta: " + (System.currentTimeMillis() - t1));
		}
		catch (SQLException e)
		{
			logger.error("Erro na consulta ao Clarify. Erro: ", e);
		}
		finally
		{
			try
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException e)
			{
				logger.error("Erro no fechamento do Statement. Erro: ", e);
			}
		}
		
		return retorno;
	}
}

