//Definicao do Pacote
package com.brt.gpp.aplicacoes.contestar;

import java.util.Calendar;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;
//import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;

import java.sql.*;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.*;


/**
  *
  * Este arquivo refere-se a classe Contestacao, responsavel pela 
  * implementacao da logica de ajuste da contestacao das cobrancas 
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Vanessa Andrade
  * Data: 				12/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class Contestar extends Aplicacoes
{
	
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	protected GerentePoolTecnomen gerenteTecnomen = null; // Gerente de conexoes Tecnomen
	private static int tamCampoDescrição = 999;
	
	/**
	 * Metodo...: Contestar
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public Contestar (long logId)
	 {
		super(logId, Definicoes.CL_CONTESTAR_COBRANCA);

		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: executaContestacao
	 * Descricao: Envia os dados de contestacao de cobranca para o fechamento de um BS 
	 * @param	aData	- Data do processamento do batch (formato DD/MM/AAAA)		  
	 * @return	short	- Sucesso(0) ou erro (diferente de 0)									
	 * @throws GPPBadXMLFormatException									
	 * @throws GPPInternalErrorException									
	 * @throws IOException 									
	 * @throws GPPTecnomenException									
	 */
	public short executaContestacao (String aData) 
							throws GPPBadXMLFormatException, GPPInternalErrorException, 
							IOException, GPPTecnomenException
	{
		//Inicializa variaveis do metodo
		short retorno = 99;
		long aIdProcesso = super.getIdLog();
		
		PREPConexao conexaoPrep = null;
		ResultSet rs_pesquisa;

		String dataProcessamento = aData;
		int cont = 0;
		int id_processamento = -1;
		String statusProcesso = Definicoes.TIPO_OPER_ERRO;
		String status_xml = "";
		String descricao = "";
		//DecimalFormat dFormat = new DecimalFormat(".##");
					
		DadosContestacao dadosContestacao = null;

		String dataInicial = GPPData.dataCompletaForamtada();

		super.log(Definicoes.INFO, "executaContestacaoCobranca", "Inicio DATA "+aData);
				
		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);

							
			// Faz a pesquisa no banco para saber os dados do item contestado
			String sql_pesquisa = "SELECT ID_PROCESSAMENTO, XML_DOCUMENT FROM TBL_INT_PPP_IN  " +
								  " WHERE IDT_EVENTO_NEGOCIO = ? AND IDT_STATUS_PROCESSAMENTO = ?" ;
			
			Object paramPesquisa[] = {Definicoes.IDT_EVT_NEGOCIO_CONTESTACAO_RQ,Definicoes.IDT_PROCESSAMENTO_NOT_OK};
	
			rs_pesquisa = conexaoPrep.executaPreparedQuery(sql_pesquisa,paramPesquisa, super.logId);

			while (rs_pesquisa.next())
			{
				// mapeia o tipo Clob
				Clob c_lob;
				Reader chr_instream;
				char chr_buffer[];
				String idtStatusProcessamento = null;
				String cod_erro = null;
					
				id_processamento = rs_pesquisa.getInt(1);
				// pega o xml
				c_lob = rs_pesquisa.getClob(2);
				
				// cria buffers para pegar o xml e converter numa srting
				chr_buffer = new char[(int)c_lob.length()];
				chr_instream = c_lob.getCharacterStream();
				chr_instream.read(chr_buffer);
				String msg_Retorno = new String(chr_buffer);
				
				// faz um parse no XML
				dadosContestacao = parseContestacaoXML(msg_Retorno);
				
				if(dadosContestacao.getCodigoRetorno() == Definicoes.RET_CONTESTACAO_OK)
				{
					//pegar os valores dos campos do XML
					String numeroBS = dadosContestacao.getnumeroBS();
					String status = dadosContestacao.getstatus();
					String datahoraFechamento = dadosContestacao.getdatahoraFechamento();
					String operador = dadosContestacao.getoperador();
					String msisdnOrigem = dadosContestacao.getmsisdnOrigem();
					String msisdnDestino = dadosContestacao.getmsisdnDestino();
					String datahoraServico = dadosContestacao.getdatahoraServico();
					String descricaoAnalise = dadosContestacao.getdescricaoAnalise();
					double valorRetornado 		= dadosContestacao.getvalorRetornadoPrincipal()/Definicoes.TECNOMEN_MULTIPLICADOR;
					double valorRetornadoBonus 	= dadosContestacao.getvalorRetornadoBonus()/Definicoes.TECNOMEN_MULTIPLICADOR;
					double valorRetornadoSm		= dadosContestacao.getvalorRetornadoSm()/Definicoes.TECNOMEN_MULTIPLICADOR;
					double valorRetornadoDados	= dadosContestacao.getvalorRetornadoDados()/Definicoes.TECNOMEN_MULTIPLICADOR;
					
					// analisa o valorRetornado
					//double valor = dFormat.format(valorRetornado/Definicoes.TECNOMEN_MULTIPLICADOR);
				
					// atualiza a tabela de Contestacao
					cod_erro = atualizaItemContestacao(conexaoPrep, numeroBS, status, operador, descricaoAnalise, 
													   datahoraFechamento, msisdnOrigem, msisdnDestino, valorRetornado, 
													   valorRetornadoBonus, valorRetornadoSm, valorRetornadoDados, datahoraServico);
				}
				else
				{
					cod_erro = dadosContestacao.getCodigoRetorno();
				}
				
				// verifica a descricao do erro
				String desc_erro = verificaDescErro(conexaoPrep, cod_erro);
					
				// analisa qual status do xml de saida e qual o status de processamento do registro na tabela de entrada
				if (cod_erro == Definicoes.RET_CONTESTACAO_ATUALIZADA)
				{
					status_xml = Definicoes.RET_CONTESTACAO_OK;
					idtStatusProcessamento = Definicoes.IDT_PROCESSAMENTO_OK;
				}
				else
				{
					status_xml = Definicoes.RET_CONTESTACAO_NOT_OK;
					idtStatusProcessamento = Definicoes.IDT_PROCESSAMENTO_ERRO;
				}
						
				// atualiza o indicador do processo
				atualizaIndProcessamento(id_processamento, idtStatusProcessamento, conexaoPrep);
					
				// gerar o XML de saída 
				String xmlRetorno = dadosContestacao.getRetornoItemContestacaoXML(desc_erro, cod_erro, status_xml);
		
				// gravar os campos na tabela de saída
				gravaXmlSaida(conexaoPrep, id_processamento, dataProcessamento, Definicoes.IDT_EVT_NEGOCIO_CONTESTACAO_FN, xmlRetorno);					
					
				cont = cont + 1;
			}
			
			rs_pesquisa.close();
			rs_pesquisa = null;
			
			descricao = "Foram tratados " + cont + " XMLs de Contestacao.";
			statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
			retorno = 0;
		}
		catch (SQLException e)
		{
			super.log(Definicoes.WARN, "executaContestacaoCobranca", "Excecao SQL: "+ e.getMessage());
			descricao = e.getMessage();
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "executaContestacao", "Excecao interna do GPP: " + e.getMessage());
			descricao = e.getMessage();
		}
		finally
		{
			String dataFinal = GPPData.dataCompletaForamtada();
			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_CONTESTACAO, dataInicial, dataFinal, statusProcesso, descricao, dataProcessamento);
			
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.INFO, "executaContestacaoCobranca", "Fim");
		}
		
		return retorno;
	}

	/**
	 * Metodo...: parseContestacaoXML
	 * Descricao: Faz um parse do XML de entrada e retorna os valores internos do XML 
	 * @param  GPPContestacao		- Dados do XML 
	 * @return DadosContestacao 	- Dados do XML dentro de uma classe do tipo DadosContestacao
	 */
	public DadosContestacao parseContestacaoXML ( String XMLContestacao ) 
	{
		DadosContestacao dadosContestacao = new DadosContestacao();
		
		super.log(Definicoes.DEBUG, "parseContestacaoXML", "Inicio");

		try
		{
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML informado dentro e um InputSource
			InputSource is = new InputSource(new StringReader(XMLContestacao));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG root
			Element serviceElement = (Element) doc.getElementsByTagName( "root" ).item(0);
			NodeList rootNode = serviceElement.getChildNodes();			

			// Procura a TAG cabecalho
			Element serviceCabecalhoElement = (Element) doc.getElementsByTagName( "header" ).item(0);
			NodeList cabecalhoNode = serviceCabecalhoElement.getChildNodes();			

			// Procura a TAG USER
			Element serviceUsuarioElement = (Element) doc.getElementsByTagName( "USER" ).item(0);
			NodeList usuarioNode = serviceUsuarioElement.getChildNodes();			

			if (rootNode != null)
			{
				
				// Verifica todas as TAGS do cabecalhoNode
				for (int i = 0; i < (cabecalhoNode.getLength()); i++) 
				{
					if (cabecalhoNode.item(i).getChildNodes().item(0).getNodeValue() == null)
					{
						throw new GPPBadXMLFormatException ("Erro de formato de XML: Falta parametro na tag cabecalho");
					}
				}

				// Armazena os dados da TAG cabecalho
				dadosContestacao.setid_evento((new Integer(((Element)cabecalhoNode).getElementsByTagName("idEvento").item(0).getChildNodes().item(0).getNodeValue())).intValue());
				dadosContestacao.setevento(((Element)cabecalhoNode).getElementsByTagName("evento").item(0).getChildNodes().item(0).getNodeValue());
				
				
				// Verifica todas as TAGS do usuarioNode
				for (int i = 0; i < (usuarioNode.getLength()); i++) 
				{
					if (usuarioNode.item(i).getChildNodes().item(0).getNodeValue() == null)
					{
						throw new GPPBadXMLFormatException ("Erro de formato de XML: Falta parametro na tag usuario");
					}
				}

				// Armazena os dados da TAG usuario
				dadosContestacao.setnumeroBS(((Element)usuarioNode).getElementsByTagName("numeroBS").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setstatus(((Element)usuarioNode).getElementsByTagName("status").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setdatahoraFechamento(((Element)usuarioNode).getElementsByTagName("datahoraFechamento").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setoperador(((Element)usuarioNode).getElementsByTagName("operador").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setmsisdnOrigem("55"+((Element)usuarioNode).getElementsByTagName("msisdnOrigem").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setmsisdnDestino(((Element)usuarioNode).getElementsByTagName("msisdnDestino").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setdatahoraServico(((Element)usuarioNode).getElementsByTagName("datahoraServico").item(0).getChildNodes().item(0).getNodeValue());
				dadosContestacao.setdescricaoAnalise(((Element)usuarioNode).getElementsByTagName("descricaoAnalise").item(0).getChildNodes().item(0).getNodeValue().trim());
				// Apesar da contestacao possuir os valores de cada saldo, somente um unico valor retornara no XML
				// gerado pelo SFA sendo esse o valor do saldo principal 
				String valorRetornado = ((Element)usuarioNode).getElementsByTagName("valorRetornado").item(0).getChildNodes().item(0).getNodeValue();
				dadosContestacao.setvalorRetornadoPrincipal(Double.parseDouble("".equals(valorRetornado.trim())?"0":valorRetornado.trim()));
			}
			
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_OK);
		}
		catch (SAXException e) 
		{
			super.log(Definicoes.WARN, "parseContestacaoXML", "Erro(SAX) formato XML: " + e.getMessage());
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_CAMPO_INVALIDO);
		}
		catch (NullPointerException e)
		{
			super.log(Definicoes.WARN, "parseContestacaoXML", "Erro(NULLPOINTER) formato XML: " + e.getMessage());
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_CAMPO_INVALIDO);
		}			
		catch (IOException e) 
		{
			super.log(Definicoes.WARN, "parseContestacaoXML", "Erro(IO) formato XML: " + e.getMessage());
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_ERRO_TECNICO);
		}
		catch (ParserConfigurationException e)
		{
			super.log(Definicoes.WARN, "parseContestacaoXML", "Erro(PARSER) formato XML: " + e.getMessage());
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_ERRO_TECNICO);
		}
		catch(GPPBadXMLFormatException e)
		{
			super.log(Definicoes.WARN, "parseContestacaoXML", "Erro(PARSER) formato XML: " + e.getMessage());
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_CAMPO_INVALIDO);
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "parseContestacaoXML", "Excecao: " + e.getMessage());
			dadosContestacao.setCodigoRetorno(Definicoes.RET_CONTESTACAO_ERRO_TECNICO);
		}
		finally
		{
			super.log(Definicoes.DEBUG, "parseContestacaoXML", "Fim");
		}
		
		return dadosContestacao;		
	}
	
	/**
	 * Metodo...: atualizaIndProcessamento
	 * Descricao: Atualiza o indicador de processamento da tabela de entrada
	 * @param 	PREPConexao				conexaoPrep	 			- Conexao com o Banco de Dados
	 * @param 	int						id_processamento		- Identificador do processo batch na tabela TBL_INT_PPP_IN
	 * @param	String					idtStatusProcessamento	- Identificador do status do processo
	 */
	private void atualizaIndProcessamento(int id_processamento, String idtStatusProcessamento, PREPConexao conexaoPrep)
	{
		super.log(Definicoes.DEBUG, "atualizaIndProcessamento", "Inicio");
		
		try
		{
			// Atualiza o indicador de processamento da tabela de entrada
			String sql_atualiza = "UPDATE TBL_INT_PPP_IN SET IDT_STATUS_PROCESSAMENTO = ? " +
								" WHERE ID_PROCESSAMENTO = ? ";

			Object paramMsg[] = {idtStatusProcessamento, new Integer(id_processamento)};
	
			if (conexaoPrep.executaPreparedUpdate(sql_atualiza, paramMsg, super.logId) > 0)
			{
				super.log(Definicoes.DEBUG, "atualizaIndProcessamento", "Foi atualizado o indicador do processamento com sucesso.");
			}
			else
			{
				super.log(Definicoes.WARN, "atualizaIndProcessamento", "Ocorreu um erro na atualizacao o indicador do processamento com sucesso.");
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "atualizaIndProcessamento", "Excecao Interna GPP: "+ e);
		}
		finally
		{
			super.log(Definicoes.DEBUG, "atualizaIndProcessamento", "Fim");
		}
	}

	/**
	 * Metodo...: gravaXmlSaida
	 * Descricao: Grava o xml de Saída para o Vitria
	 * @param conexaoPrep			- Conexao com o Banco de Dados
	 * @param id_processamento		- Identificador do processamento
	 * @param dataProcessamento		- Data do processamento
	 * @param idt_evento_negocio	- Identificador do evento de negocio
	 * @param xmlRetorno			- Xml de retorno
	 */
	private void gravaXmlSaida(PREPConexao conexaoPrep, int id_processamento, String dataProcessamento, 
								String idt_evento_negocio, String xmlRetorno)
	{
		super.log(Definicoes.DEBUG, "gravaXmlSaida", "INICIO");
		try
		{
			String ind_enviado_vitria = Definicoes.IDT_PROCESSAMENTO_NOT_OK;
  			 
			// gravar os campos na tabela de saída
			String sql_grava = "INSERT INTO TBL_INT_PPP_OUT  " +
							  " (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
							  " XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) VALUES " +
							  " ( ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ? )";
	
			Object paramInsert[] = {new Integer(id_processamento), dataProcessamento, idt_evento_negocio, xmlRetorno, ind_enviado_vitria};
								
			if (conexaoPrep.executaPreparedUpdate(sql_grava, paramInsert, super.logId) > 0)
			{
				super.log(Definicoes.DEBUG, "gravaXmlSaida", "Gravou o xml com sucesso.");
			}
			else
			{
				super.log(Definicoes.WARN, "gravaXmlSaida", "Erro na gravacao do xml.");
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "gravaXmlSaida", "Excecao Interna GPP: "+ e);
		}
		super.log(Definicoes.DEBUG, "gravaXmlSaida", "FIM");
		
	}

	/**
	 * Metodo...: atualizaItemContestacao
	 * Descricao: Atualiza a tabela de Contestacao 
	 * @param conexaoPrep			- Conexao com o banco de dados
	 * @param numeroBS				- Numero do BS da Contestaco
	 * @param status				- Status do BS
	 * @param operador				- Nome do operador que realizou o BS
	 * @param descricaoAnalise		- Descricao da analise
	 * @param datahoraFechamento	- Data e hora do fechamento do BS
	 * @param msisdnOrigem			- Msisdn de origem
	 * @param msisdnDestino			- Msisdn de destino
	 * @param valorRetornado		- Valor Retornado da contestacao dos itens do BS
	 * @param datahoraServico		- Data e hora do servico do BS 
	 * @return	String				- Codigo de retorno correspondente a atualizacao do item de contestacao
	 * @throws GPPInternalErrorException
	 */
	private String atualizaItemContestacao(PREPConexao conexaoPrep, String numeroBS,
											String status, String operador, 
											String descricaoAnalise, String datahoraFechamento, 
											String msisdnOrigem, String msisdnDestino,
											double valorRetornado, double valorRetornadoBonus,
											double valorRetornadoSm, double valorRetornadoDados, String datahoraServico) throws GPPInternalErrorException
	{
		String retorno = "";
		ResultSet rs_checa;
		
		super.log(Definicoes.DEBUG, "atualizaItemContestacao", "INICIO");
		
		try
		{
			// verifica se existe o BS
			String sql_checa = "SELECT * FROM TBL_GER_CONTESTACAO WHERE IDT_NU_BS = ?" ;
							
			Object paramCheca[] = {numeroBS};
			
			rs_checa = conexaoPrep.executaPreparedQuery1(sql_checa,paramCheca, super.logId);
							
			if (rs_checa.next())
			{
				if (status == null || operador == null || descricaoAnalise== null || 
					datahoraFechamento == null || msisdnOrigem == null || msisdnDestino == null)
				{
					retorno = Definicoes.RET_CONTESTACAO_CAMPO_INVALIDO;
					super.log(Definicoes.DEBUG, "atualizaItemContestacao", "Algum campo invalido, erro na atualizacao.");
				}
				else
				{

					// atualiza a tabela de contestacao
					String sql_atualiza = " UPDATE TBL_GER_ITEM_CONTESTACAO SET " + 
					" IDT_STATUS_ITEM_CONTESTACAO = ? ,IDT_OPERADOR_SFA = ? " +
					" ,DES_PARECER = ? ,DAT_FECHAMENTO =to_date(?, 'YYYYMMDDHH24MISS') " +
					" ,VLR_AJUSTADO_PRINCIPAL = ?, VLR_AJUSTADO_BONUS = ?" +
					" ,VLR_AJUSTADO_SMS = ?, VLR_AJUSTADO_GPRS = ? " +
					//Substitui todas as * e # antes de pesquisar nos Itens da contestacao
					" WHERE  IDT_NU_BS = ? AND SUB_ID = ? AND FNC_FORMATAMSISDNDESTINOCST(CALL_ID) = FNC_FORMATAMSISDNDESTINOCST(?) " +
					" AND TIMESTAMP = to_date(?, 'YYYYMMDDHH24MISS') ";
					
					
					Object paramAtualiza[] = {status, operador, 
							descricaoAnalise.length()>tamCampoDescrição?descricaoAnalise.substring(0,tamCampoDescrição).trim():descricaoAnalise.trim(), 
											datahoraFechamento,
											new Double(valorRetornado),
											new Double(valorRetornadoBonus),
											new Double(valorRetornadoSm),
											new Double(valorRetornadoDados),
											numeroBS,  msisdnOrigem, 
											msisdnDestino, 
											datahoraServico};
											
					if (conexaoPrep.executaPreparedUpdate(sql_atualiza, paramAtualiza, super.logId) > 0)
					{
						super.log(Definicoes.DEBUG, "atualizaItemContestacao", "Atualizacao da tabela de Contestacao Ok.");
						retorno = Definicoes.RET_CONTESTACAO_ATUALIZADA;
						
						// verifica se todos os itens de um determinado BS foi atualizado					
						analisaItens(numeroBS);					
					}
					else
					{
						super.log(Definicoes.WARN, "atualizaItemContestacao", "Erro na atualizacao da tabela de Contestacao.");
						retorno = Definicoes.RET_CONTESTACAO_ERRO_TECNICO;
					}

				}
								
			}
			else
			{
				retorno = Definicoes.RET_CONTESTACAO_BS_INVALIDO;
			}
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "atualizaItemContestacao", "Excecao Interna GPP: "+ e);
			retorno = Definicoes.RET_CONTESTACAO_ERRO_TECNICO;
		}

		catch (SQLException e1)
		{
			super.log(Definicoes.ERRO, "atualizaItemContestacao", "Excecao de SQL: "+ e1);
			retorno = Definicoes.RET_CONTESTACAO_ERRO_TECNICO;
			throw new GPPInternalErrorException ("Excecao GPP: " + e1);
		}
		/*
		catch (ParseException e2)
		{
			super.log(Definicoes.ERRO, "atualizaItemContestacao", "Excecao de Parse ocorrida: "+ e2);
			retorno = Definicoes.RET_CONTESTACAO_ERRO_TECNICO;
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e2);
		}
		*/
		super.log(Definicoes.DEBUG, "atualizaItemContestacao", "FIM");
		return retorno;

	}

	/**
	 * Metodo...: verificaDescErro
	 * Descricao: Verifica a descricao do Erro
	 * @param 	conexaoPrep	- Conexao com o banco de dados
	 * @param 	cod_erro	- Codigo de erro correspondente a acao do item da contescao
	 * @return	String 		- Descricao de erro correspondente a acao do item da contescao									
	 */
	private String verificaDescErro(PREPConexao conexaoPrep, String cod_erro)
	{
		super.log(Definicoes.DEBUG, "checaDescErro", "INICIO");
		String retorno = "";
		ResultSet rs_verifica;

		try
		{
			// Atualiza o indicador de processamento da tabela de entrada
			String sql_verifica = "SELECT DES_RETORNO FROM TBL_GER_CODIGOS_RETORNO " +
								" WHERE VLR_RETORNO = ? ";

			Object paramMsg[] = {cod_erro};
			
			rs_verifica = conexaoPrep.executaPreparedQuery2(sql_verifica, paramMsg, super.logId);
	
			if (rs_verifica.next())
			{
				retorno = rs_verifica.getString(1);
				super.log(Definicoes.DEBUG, "checaDescErro", "Pesquisa feita com sucesso.");
			}
			else
			{
				super.log(Definicoes.WARN, "checaDescErro", "Ocorreu um erro na pesquisa.");
			}
			rs_verifica.close();
			rs_verifica = null;

		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "checaDescErro", "Excecao Interna GPP:"+ e);
		}
		super.log(Definicoes.DEBUG, "checaDescErro", "FIM");
		return retorno;
	}
	
	
	/**
	 * Metodo...: analisaItens
	 * Descricao: Verifica se todos os itens de um determinado BS foi atualizado
	 * @param 	conexaoPrep	- Conexao com o banco de dados
	 * @param 	numeroBS	- Numero do BS da Contestacao
	 * @return	short   	- RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 * @throws 	GPPInternalErrorException									
	 */
	private short analisaItens( String numeroBS)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "analisaItens", "INICIO NUMEROBS "+numeroBS);

		long aIdProcesso = super.getIdLog();
//		ResultSet rs_analisa;
		ResultSet rs_chamadas;
		ResultSet rs_itensOk;
		ResultSet rs_Msisdn;
		
		// inicializacao das variaveis
		short retorno = Definicoes.RET_OPERACAO_OK;
		String dataFechamento;
//		String statusItem;
//		int valorAjustado;
		double somaValorPrincipal;
		double somaValorBonus;
		double somaValorSm;
		double somaValorDados;
		int totalItens;
		int totalItensProcedentes;
		String statusBS;
		String msisdn;
		short resposta;
		PREPConexao conexaoPrep = null;	
		try
		{
			// verifica quais itens foram processados
			String sql_chamadas = "SELECT * FROM TBL_GER_ITEM_CONTESTACAO " +
								" WHERE IDT_NU_BS = ? AND DAT_FECHAMENTO IS NULL " ;
										
			Object paramChamadas[] = {numeroBS};
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
			rs_chamadas = conexaoPrep.executaPreparedQuery(sql_chamadas,paramChamadas, super.logId);
					
			// se algum item possui data de fechamento nula, o BS ainda esta aberto
			if (rs_chamadas.next())
			{
				super.log(Definicoes.DEBUG, "analisaItens", "Alguns itens nao foram processados ainda.");
			}
			
			// se nenhum item possui data de fechamento nula, o BS pode ser fechado e o valor ajustado
			else
			{
				// verifica a soma dos valores ajustados e os status dos itens
				String sql_itensOk = " SELECT SUM(DECODE(IDT_STATUS_ITEM_CONTESTACAO,?,VLR_AJUSTADO_PRINCIPAL,0)), " +
				 					         "SUM(DECODE(IDT_STATUS_ITEM_CONTESTACAO,?,VLR_AJUSTADO_BONUS,0)), " +
				 					         "SUM(DECODE(IDT_STATUS_ITEM_CONTESTACAO,?,VLR_AJUSTADO_SMS,0)), " +
				 					         "SUM(DECODE(IDT_STATUS_ITEM_CONTESTACAO,?,VLR_AJUSTADO_GPRS,0)), " +
									" COUNT(*), SUM(DECODE(IDT_STATUS_ITEM_CONTESTACAO,?,1,0)) " +
									" FROM TBL_GER_ITEM_CONTESTACAO WHERE IDT_NU_BS = ? ";
				
				Object paramitensOk[] = {Definicoes.STATUS_CONTESTACAO_PROCEDENTE,
										 Definicoes.STATUS_CONTESTACAO_PROCEDENTE,
						                 Definicoes.STATUS_CONTESTACAO_PROCEDENTE,
						                 Definicoes.STATUS_CONTESTACAO_PROCEDENTE,
						                 Definicoes.STATUS_CONTESTACAO_PROCEDENTE, numeroBS};

				rs_itensOk = conexaoPrep.executaPreparedQuery1(sql_itensOk,paramitensOk, super.logId);
				
				if (rs_itensOk.next())
				{
					somaValorPrincipal		= rs_itensOk.getDouble(1);
					somaValorBonus			= rs_itensOk.getDouble(2);
					somaValorSm				= rs_itensOk.getDouble(3);
					somaValorDados			= rs_itensOk.getDouble(4);
					totalItens				= rs_itensOk.getInt(5);
					totalItensProcedentes	= rs_itensOk.getInt(6);

					// verifica o msisdn do BS para fazer o ajuste
					String sql_msisdn = " SELECT SUB_ID FROM TBL_GER_ITEM_CONTESTACAO WHERE IDT_NU_BS = ? ";
					
					Object paramMsisdn[] = {numeroBS};

					rs_Msisdn = conexaoPrep.executaPreparedQuery2(sql_msisdn,paramMsisdn, super.logId);
				
					if (rs_Msisdn.next())
					{
						msisdn = rs_Msisdn.getString(1);

						if (totalItens == totalItensProcedentes)
							statusBS = Definicoes.STATUS_CONTESTACAO_PROCEDENTE;
						else
						{
							if (totalItensProcedentes == 0)
								statusBS = Definicoes.STATUS_CONTESTACAO_IMPROCEDENTE;
							else
								statusBS = Definicoes.STATUS_CONTESTACAO_PARCIALMENTE_PROCEDENTE;	
						}
						
						// data de fechamento do BS
						dataFechamento = GPPData.dataFormatada();
						
						SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
											
						// ajusta o status do BS
						String sql_updateBS = "UPDATE TBL_GER_CONTESTACAO SET " + 
									" IDT_STATUS_ANALISE = ? ,DAT_FECHAMENTO = ? WHERE IDT_NU_BS = ? " ;
						
						Object paramupdateBS[] = {statusBS, 
							new Date(dataFormat.parse(dataFechamento).getTime()),
							numeroBS};
												
						if (conexaoPrep.executaPreparedUpdate(sql_updateBS, paramupdateBS, super.logId) > 0)
						{
							super.log(Definicoes.DEBUG, "analisaItens", "Atualizacao do status do BS.");

							if(somaValorPrincipal!=0 || somaValorBonus!=0 || somaValorSm!=0 || somaValorDados!=0)
							{
								// Cria uma referencia para o objeto ValoresRecarga que sera utilizado para o
								// ajuste dos itens procedentes da contestacao
								ValoresRecarga vlrRecarga =  new ValoresRecarga(somaValorPrincipal,0.0,somaValorBonus,somaValorSm,somaValorDados);

								// Fazer um ajuste para atualizar os valores da contestacao
								Ajustar recarregar = new Ajustar (super.logId);		
								resposta = recarregar.executarAjuste(msisdn, 
																	 Definicoes.AJUSTE_CONTESTACAO_PROCEDENTE, 
																	 Definicoes.TIPO_CREDITO_REAIS, 
																	 vlrRecarga, 
																	 Definicoes.TIPO_AJUSTE_CREDITO, 
																	 Calendar.getInstance().getTime(), 
																	 Definicoes.SO_GPP, 
																	 Definicoes.GPP_OPERADOR, 
																	 null, 
																	 null,
																	 Definicoes.AJUSTE_NORMAL,
                                                                     null);
					
								// Validacao do ajuste da recarga
								if (resposta == 0)
									super.log(Definicoes.DEBUG, "analisaItens", "Ajuste dos valores da contestacao ok.");
								else
									super.log(Definicoes.WARN, "analisaItens", "Erro no ajuste dos valores da contestacao.");
							}
							
							String data = GPPData.dataFormatada();
							String aMensagem = gravaMsg(conexaoPrep, numeroBS, statusBS, data, somaValorPrincipal+
									                                                           somaValorBonus    +
																							   somaValorSm       +
																							   somaValorDados);						
							// Envio de SMS
							ConsumidorSMS consumidorSMS = ConsumidorSMS.getInstance(super.getIdLog());
							if(consumidorSMS.gravaMensagemSMS (msisdn, aMensagem, Definicoes.SMS_PRIORIDADE_UM, Definicoes.SMS_CONTESTACAO,super.getIdLog()))
								super.log(Definicoes.DEBUG, "analisaItens", "Envio do SMS com sucesso.");
							else
								super.log(Definicoes.WARN, "analisaItens", "Erro no envio do SMS.");								
						}
						else
							super.log(Definicoes.WARN, "analisaItens", "Erro Atualizacao ddo status do BS.");					
					}// fim do if rs_Msisdn				
				}// fim do if rs_itensOk
			}// fim do else rs_chamadas
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "analisaItens", "Excecao Interna GPP:"+ e);
		}
		finally
		{
			// libera conexão de banco de dados
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "analisaItens", "Fim");

		return retorno;
	}
	
	
	/**
	 * Metodo...: gravaMsg
	 * Descricao: Grava a msg do envio do SMS
	 * @param 	numeroBS	- Numero do BS da contestacao
	 * @param 	statusBS	- Status da contestacao
	 * @param 	data		- Data do ajuste da contestacao
	 * @param 	somaValor	- Valor do ajuste
	 * @return	String  	- Mensagem do SMS		
	 * @throws							
	 */
	private String gravaMsg(PREPConexao conexaoPrep, String numeroBS, String statusBS, String data, double somaValor)
	{
	   super.log(Definicoes.DEBUG, "gravaMsg", "INICIO");
		String msgSMS = "";
		DecimalFormat d = new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
		try
		{
			// Busca a instancia do mapeamento de configuracao do sistema
			// e neste caso nao retorna o valor da configuracao e sim a descricao
		    
		    String tipoMensagem = null; 
		    if(Definicoes.STATUS_CONTESTACAO_PROCEDENTE.equals(statusBS) || (Definicoes.STATUS_CONTESTACAO_PARCIALMENTE_PROCEDENTE.equals(statusBS)))
		    {
		        tipoMensagem = Definicoes.TIPO_MSG_SMS_CONTESTACAO_PROCEDENTE;
		    }
		    else
		    {
		        tipoMensagem = Definicoes.TIPO_MSG_SMS_CONTESTACAO_IMPROCEDENTE;
		    }
		            
			MapConfiguracaoGPP configGPP = MapConfiguracaoGPP.getInstancia();
			msgSMS = configGPP.getMapValorConfiguracaoGPP(tipoMensagem);

			if (msgSMS != null)
			{
			   String texto1 = substituiTexto("%1", msgSMS, numeroBS);
			   String texto2 = substituiTexto("%2", texto1, statusBS);
			   String texto3 = substituiTexto("%3", texto2, data);
			   String texto4 = substituiTexto("%4", texto3, d.format(somaValor));
			   
			   msgSMS = texto4;
			   super.log(Definicoes.DEBUG, "gravaMsg", "Texto1:" + texto1+" Texto2:" + texto2+" Texto3:" + texto3+" Msg:" + msgSMS);
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "analisaItens", "Excecao Interna GPP:"+ e);
		}
		super.log(Definicoes.DEBUG, "gravaMsg", "FIM");
		return msgSMS;
	}
	
	/**
	 * Metodo...: substituiTexto
	 * Descricao: Substitui as variaveis da msg de texto SMS
	 * @param 	var			- Variaveis do texto na tabela
	 * @param 	texto		- Texto na tabela
	 * @param 	novaString	- Valor da string que será substituida
	 * @return	String  	- Mensagem do SMS		
	 * @throws							
	 */
	private String substituiTexto(String var, String texto, String novaString)
	{
		// Cria um padrão para as variaveis
		Pattern padrao = Pattern.compile(var);

		// o Matcher casa seqüências de caracteres com uma dada string
		Matcher m = padrao.matcher(texto);
	  
		StringBuffer sb = new StringBuffer();
	 
		// trocar as ocorrências das variaveis pela novaString
		while(m.find()) 
		{
		   m.appendReplacement(sb, novaString);
		}
		m.appendTail(sb);
	 
		return sb.toString();
	}
	
}

