//Definicao do Pacote
package com.brt.gpp.aplicacoes.importacaoUsuarioPortalNDS;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de mapeamentos
//import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;
import java.sql.*;

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
  * Data: 				17/05/2004
  *
  * Modificado por: Joao Carlos
  * Data:15/09/2004
  * Razao:Comentando a linha que insere o grupo inicial (Perfil realizado pelo I-Chain)
  *       e utilizacao do mapeamento de configuracao do GPP ao inves de select na tabela
  *
  */

public final class ImportarUsuarioPortalNDS extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	protected GerentePoolTecnomen gerenteTecnomen = null; // Gerente de conexoes Tecnomen
		     
	/**
	 * Metodo...: ImportarUsuarioPortalNDS
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public ImportarUsuarioPortalNDS (long logId)
	 {
		super(logId, Definicoes.CL_IMPORTACAO_USUARIOS);

		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: executaImportacaoUsuarioPortalNDS
	 * Descricao: Envia os dados da importacao de usuários (NDS)
	 *			  e o sistema Portal Web do Pré-pago 
	 * @param		  
	 * @return	short		Sucesso(0) ou erro (diferente de 0)	
	 * @throws GPPBadXMLFormatException
	 * @throws GPPInternalErrorException
	 * @throws IOException
	 * @throws GPPTecnomenException							
	 */
	public short importaUsuarioPortalNDS ( ) throws GPPBadXMLFormatException, GPPInternalErrorException, IOException, GPPTecnomenException
	{
		//Inicializa variaveis do metodo
		short retorno = 0;
		long aIdProcesso = super.getIdLog();
		
		PREPConexao conexaoPrep = null;
		ResultSet rs_pesquisa;

		int cont = 0;
		int id_processamento;
		String statusProcesso = "";
		String status_xml = "";
					
		DadosUsuario dadosUsuario = null;

		String dataInicial = GPPData.dataCompletaForamtada();

		super.log(Definicoes.INFO, "executaImportacaoUsuarioPortalNDS", "Inicio");
				
		try
		{			
			// Pega a data da execucao do processo
			String dataProcessamento = GPPData.dataCompletaForamtada();
					
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);

			String idt_processamento_notok = Definicoes.IDT_PROCESSAMENTO_NOT_OK;
			String idt_evento_negocio = Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_IN;
				
			// Faz a pesquisa no banco para saber os dados da importacao de usuarios 
			String sql_pesquisa = "SELECT ID_PROCESSAMENTO, XML_DOCUMENT FROM TBL_INT_PPP_IN  " +
								  " WHERE IDT_EVENTO_NEGOCIO = ? AND IDT_STATUS_PROCESSAMENTO = ?" ;
			
			Object paramPesquisa[] = {idt_evento_negocio,idt_processamento_notok};
	
			rs_pesquisa = conexaoPrep.executaPreparedQuery(sql_pesquisa,paramPesquisa, super.logId);

			if (rs_pesquisa.next())
			{
				do
				{
					// mapeia o tipo Clob
					Clob c_lob;
					Reader chr_instream;
					char chr_buffer[];
//					String out_buffer;
					
					// pega o id do processamento
					id_processamento = rs_pesquisa.getInt(1);
					// pega o xml
					c_lob = rs_pesquisa.getClob(2);

					// cria buffers para pegar o xml e converter numa srting
					chr_buffer = new char[(int)c_lob.length()];
					chr_instream = c_lob.getCharacterStream();
					chr_instream.read(chr_buffer);
					String msg_Retorno = new String(chr_buffer);
					
					// faz um parse no XML
					dadosUsuario = parseImportacaoUsuarioXML(msg_Retorno);
					
					//pegar os valores dos campos do XML
//					long id_evento = dadosUsuario.getid_evento();
//					String evento = dadosUsuario.getevento();
//					String modify_stmp = dadosUsuario.getmodify_stmp();
//					String sistema = dadosUsuario.getsistema();
//					String acao = dadosUsuario.getacao();
//					String idUsuario = dadosUsuario.getidUsuario();
//					String nomeUsuario = dadosUsuario.getnomeUsuario();
//					String deptoUsuario = dadosUsuario.getdeptoUsuario();
//					String cargoUsuario = dadosUsuario.getcargoUsuario();
//					String emailUsuario = dadosUsuario.getemailUsuario();
					
					// atualiza a tabela de Importacao de Usuarios
					int cod_erro = atualizaTabelaUsuarios(conexaoPrep, dadosUsuario);
											
					// verifica a descricao do erro
					String desc_erro = verificaDescErro(conexaoPrep, cod_erro);
					
					// analisa qual status do xml de saida
					if (cod_erro == Definicoes.RET_OPERACAO_OK)
						status_xml = Definicoes.RET_IMPORTACAO_USUARIO_OK;
					else
						status_xml = Definicoes.RET_IMPORTACAO_USUARIO_NOK;
						
					// atualiza o registro processado
					atualizaIndProcessamento(idt_evento_negocio, conexaoPrep);

					// Troca o evento de negocio para o TAG de envio de dados
					idt_evento_negocio = Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_OUT;
					dadosUsuario.setevento(Definicoes.IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_OUT);
					
					// gerar o XML de saída 
					String xmlRetorno = dadosUsuario.getRetornoImportacaoUsuarioXML(desc_erro, cod_erro, status_xml);
		
					// gravar os campos na tabela de saída
					gravaXmlSaida(conexaoPrep, id_processamento, dataProcessamento, idt_evento_negocio, xmlRetorno);					
					
					cont = cont + 1;
					
				} while (rs_pesquisa.next());
			
			}
			rs_pesquisa.close();
			rs_pesquisa = null;
			
			String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "Foram tratados " + cont + " XMLs de importacao de usuarios.";
			statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_IMPORTACAO_USUARIOS, dataInicial, dataFinal, statusProcesso, descricao, dataProcessamento);

		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "executaImportacaoUsuarioPortalNDS", "Excecao Interna GPP: "+ e);
			retorno = 1;				
		}

		catch (SQLException e1)
		{
			super.log(Definicoes.WARN, "executaImportacaoUsuarioPortalNDS", "Excecao SQL: "+ e1);
			throw new GPPInternalErrorException ("Excecao GPP: " + e1);				
		}

		super.log(Definicoes.INFO, "executaImportacaoUsuarioPortalNDS", "Fim");
		return retorno;
		
	}

	/**
	 * Metodo...: parseImportacaoUsuarioXML
	 * Descricao: Faz um parse do XML de entrada e retorna os valores internos do XML 
	 * @param  GPPContestacao		- Dados do XML 
	 * @return DadosContestacao 	- Dados do XML dentro de uma classe do tipo DadosUsuario
	 * @throws GPPBadXMLFormatException
	 * @throws GPPInternalErrorException
	 */
	public DadosUsuario parseImportacaoUsuarioXML ( String XMLImportacaoUsuario ) throws GPPBadXMLFormatException, GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"parseImportacaoUsuarioXML","Inicio");
		
		DadosUsuario dadosUsuario = null;
		
		try
		{
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML informado dentro e um InputSource
			InputSource is = new InputSource(new StringReader(XMLImportacaoUsuario));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG root
			Element serviceElement = (Element) doc.getElementsByTagName( "root" ).item(0);
			NodeList rootNode = serviceElement.getChildNodes();			

			// Procura a TAG CASE
			Element serviceCaseElement = (Element) doc.getElementsByTagName("CASE").item(0);
			NodeList caseNode = serviceCaseElement.getChildNodes();		

			dadosUsuario = new DadosUsuario();
			
			if (rootNode != null)
			{
				// Faz um cast para uma estutura Element
				Element auxCaseNode = (Element)caseNode;

				// XML - <root><CASE><id_evento>000001</id_evento><evento>RqAcessoColaborador</evento><modify_stmp>12/12/12 12:12:12</modify_stmp><sistema>WPPP</sistema><acao>ATUA</acao><ID_USUARIO/><NOME/><DEPARTAMENTO/><CARGO/><EMAIL/></CASE></root>
				
				// Armazena os dados da TAG cabecalho
				dadosUsuario.setid_evento((Long.parseLong(auxCaseNode.getElementsByTagName("id_evento").item(0).getChildNodes().item(0).getNodeValue().trim())));
				dadosUsuario.setevento(auxCaseNode.getElementsByTagName("evento").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setmodify_stmp(auxCaseNode.getElementsByTagName("modify_stmp").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setsistema(auxCaseNode.getElementsByTagName("sistema").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setacao(auxCaseNode.getElementsByTagName("acao").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setidUsuario(auxCaseNode.getElementsByTagName("ID_USUARIO").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setnomeUsuario(auxCaseNode.getElementsByTagName("NOME").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setdeptoUsuario(auxCaseNode.getElementsByTagName("DEPARTAMENTO").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setcargoUsuario(auxCaseNode.getElementsByTagName("CARGO").item(0).getChildNodes().item(0).getNodeValue().trim());
				dadosUsuario.setemailUsuario(auxCaseNode.getElementsByTagName("EMAIL").item(0).getChildNodes().item(0).getNodeValue().trim());
			}
		}
		catch (SAXException e) 
		{
			super.log(Definicoes.WARN, "parseImportacaoUsuarioXML", "Erro(SAX) formato XML:" + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro de formato de XML:" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			super.log(Definicoes.WARN, "parseImportacaoUsuarioXML", "Erro(NULLPOINTER) formato XML: " + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro de formato de XML:" + e.getMessage());
		}			
		catch (IOException e1) 
		{
			super.log(Definicoes.WARN, "parseImportacaoUsuarioXML", "Erro(IO) formato XML: " + e1.getMessage());
			throw new GPPInternalErrorException ("Erro interno no GPP:" + e1.getMessage());
		}
		catch (ParserConfigurationException e2)
		{
			super.log(Definicoes.WARN, "parseImportacaoUsuarioXML", "Erro(PARSER) formato XML: " + e2.getMessage());
			throw new GPPInternalErrorException ("Erro interno no GPP:" + e2.getMessage());
		}

		super.log(Definicoes.DEBUG,"parseImportacaoUsuarioXML","Fim");
		
		return dadosUsuario;		
	}
	
	/**
	 * Metodo...: atualizaIndProcessamento
	 * Descricao: Atualiza o indicador de processamento da tabela de entrada
	 * @param 	conexaoPrep			- Conexao com o banco de dados
	 * @param 	idt_evento_negocio	- Identificador do evento de negócio
	 * @return										
	 * @throws  GPPInternalErrorException
	 */
	private void atualizaIndProcessamento(String idt_evento_negocio, PREPConexao conexaoPrep)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "atualizaIndProcessamento", "Inicio EVENTONEGOCIO "+idt_evento_negocio);
//		long aIdProcesso = super.getIdLog();

		try
		{
			String idt_processamento_ok = Definicoes.IDT_PROCESSAMENTO_OK;

			// Atualiza o indicador de processamento da tabela de entrada
			String sql_atualiza = "UPDATE TBL_INT_PPP_IN SET IDT_STATUS_PROCESSAMENTO = ? " +
								" WHERE IDT_EVENTO_NEGOCIO = ? ";

			Object paramMsg[] = { idt_processamento_ok, idt_evento_negocio};
	
			if (conexaoPrep.executaPreparedUpdate(sql_atualiza, paramMsg, super.logId) > 0)
			{
				super.log(Definicoes.DEBUG, "atualizaIndProcessamento", "Foi atualizado o indicador do processamento com sucesso.");
			}
			else
			{
				super.log(Definicoes.WARN, "atualizaIndProcessamento", "Ocorreu um erro na atualizacao do indicador do processamento.");
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "atualizaIndProcessamento", "Excecao Interna GPP ocorrida: "+ e);
		}
		super.log(Definicoes.DEBUG, "atualizaIndProcessamento", "Fim");
		
	}

	/**
	 * Metodo...: gravaXmlSaida
	 * Descricao: Grava o xml de Saída para o Vitria
	 * @param 	conexaoPrep			- Conexao com o banco de dados
	 * @param 	id_processamento	- Identificador do processamento da importacao
	 * @param	dataProcessamento	- Data do processamento da importacao
	 * @param 	idt_evento_negocio	- Identificador do evento de negócio
	 * @param	xmlRetorno			- Xml de retorno
	 * @return										
	 * @throws  GPPInternalErrorException
	 */
	private void gravaXmlSaida(PREPConexao conexaoPrep, int id_processamento, String dataProcessamento, String idt_evento_negocio, String xmlRetorno) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "gravaXmlSaida", "Inicio DATAPROC "+dataProcessamento+" EVENTONEGOC."+idt_evento_negocio);
		try
		{
			// gravar os campos na tabela de saída
			String sql_grava = "INSERT INTO TBL_INT_PPP_OUT  " +
							  " (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
							  " XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) VALUES " +
							  " ( ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ? )";
	
			Object paramInsert[] = {new Integer(id_processamento), dataProcessamento, idt_evento_negocio, xmlRetorno, Definicoes.IDT_PROCESSAMENTO_NOT_OK};
								
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
			super.log(Definicoes.ERRO, "gravaXmlSaida", "Excecao Interna GPP ocorrida: "+ e);
		}
		super.log(Definicoes.DEBUG, "gravaXmlSaida", "Fim");
	}
	
	/**
	 * Metodo...: atualizaTabelaUsuarios
	 * Descricao: Atualiza a tabela de Usuarios
	 * @param conexaoPrep 		- Conexao com o banco de dados para inclusao/exclusao/alteracado de dados do usuario
	 * @param dadosUsuario		- Dados do usuario a incluir/excluir/alterar 		
	 * @return					- OK em sucesso, ou o erro funcional / tecnico em caso de falha
	 * @throws GPPInternalErrorException
	 */
	private int atualizaTabelaUsuarios(PREPConexao conexaoPrep, DadosUsuario dadosUsuario) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "atualizaTabelaUsuarios", "Inicio");

		int retorno = Definicoes.RET_ERRO_TECNICO;
//		int ret_grupo;

		// Obtem a acao que deve ser realizada no usuario (inclusao, exclusao, alteracao)
		String acao = dadosUsuario.getacao();
		
		// caso a acao seja de inclusao
		if (acao.equals(Definicoes.IND_INCLUSAO_USUARIO))
		{
			String sql_inclusao = "INSERT INTO TBL_PPP_USUARIO " +
				                  " (ID_USUARIO, NOM_USUARIO, NOM_DEPARTAMENTO, " +
				                  " DES_CARGO, IDT_EMAIL) VALUES ( ?, ?, ?, ?, ? )";	
				
			Object paramInclusao[] = {dadosUsuario.getidUsuario(), dadosUsuario.getnomeUsuario(), dadosUsuario.getdeptoUsuario(), dadosUsuario.getcargoUsuario(), dadosUsuario.getemailUsuario()};

			try
			{
				int numLinhas = conexaoPrep.executaPreparedUpdate(sql_inclusao, paramInclusao, super.logId);

				if (numLinhas > 0)
				{
					// Inicializa o grupo para o usuario novo
					// 15/09/2004
					// Comentada a inicializacao do grupo do usuario porque a associacao
					// do perfil e realizada pelo I-Chain
					//setGrupoInicial (conexaoPrep, dadosUsuario.getidUsuario());
					
					// Inclusao com sucesso
					super.log(Definicoes.DEBUG, "atualizaTabelaUsuarios", "Acao de Inserir os dados do usuario executada com sucesso.");
					retorno = Definicoes.RET_OPERACAO_OK;
				}
				else
				{
					// Nunca sera alcancado
				}
			}
			catch (GPPInternalErrorException e)
			{
				if (conexaoPrep.getCodigoErro() == 1)
				{
					// Usuario ja existe
					super.log(Definicoes.WARN, "atualizaTabelaUsuarios", "Usuario ja existe no banco de dados.");
					retorno = Definicoes.RET_INCLUSAO_USUARIO_EXISTENTE;
				}
				else
				{
					// Erro tecnico
					super.log(Definicoes.ERRO, "atualizaTabelaUsuarios", "Erro tecnico:" + e);
					retorno = Definicoes.RET_ERRO_TECNICO;
				}
			}
		}
		else
		{
			// caso a acao seja de alteracao
			if (acao.equals(Definicoes.IND_ALTERACAO_USUARIO))
			{
				String sql_alteracao = "UPDATE TBL_PPP_USUARIO SET " +
									   " NOM_USUARIO=?, NOM_DEPARTAMENTO=?, DES_CARGO=?, " +
									   " IDT_EMAIL=? WHERE ID_USUARIO=?";
					
				Object paramaAlteracao[] = {dadosUsuario.getnomeUsuario(), dadosUsuario.getdeptoUsuario(), dadosUsuario.getcargoUsuario(), dadosUsuario.getemailUsuario(), dadosUsuario.getidUsuario()};
				
				try
				{
					int numLinhas = conexaoPrep.executaPreparedUpdate(sql_alteracao,paramaAlteracao, super.logId);
					
					if (numLinhas > 0)
					{
						// Atualizacao com sucesso
						super.log(Definicoes.DEBUG, "atualizaTabelaUsuarios", "Acao de alterar os dados do usuario executado com sucesso.");
						retorno = Definicoes.RET_OPERACAO_OK;
					}
					else
					{
						// Erro na alteracao de usuario
						super.log(Definicoes.WARN, "atualizaTabelaUsuarios", "Usuario nao existe.");
						retorno = Definicoes.RET_ALTERACAO_USUARIO_INEXISTENTE;
					}
					
				}
				catch (GPPInternalErrorException e)
				{
					// Erro tecnico
					super.log(Definicoes.ERRO, "atualizaTabelaUsuarios", "Erro tecnico:" + e);
					retorno = Definicoes.RET_ERRO_TECNICO;
				}
			}
			else
			{
				// caso a acao seja de exclusao
				if (acao.equals(Definicoes.IND_EXCLUSAO_USUARIO))
				{
					String sql_exclusao = "DELETE FROM TBL_PPP_USUARIO WHERE ID_USUARIO=?";
					
					// Deleta os usuarios na tabela de grupo
					String sql_deleta = "DELETE FROM TBL_PPP_GRUPO_USUARIO WHERE ID_USUARIO=?";
					
					Object paramExclusao[] = {dadosUsuario.getidUsuario()};

					try
					{						
						int numGrupos = conexaoPrep.executaPreparedUpdate(sql_deleta,paramExclusao, super.logId);

						int numLinhas = conexaoPrep.executaPreparedUpdate(sql_exclusao,paramExclusao, super.logId);

						if ( numGrupos > 0)
						{
							// Exclusao do grupo com sucesso
							super.log(Definicoes.DEBUG, "atualizaTabelaUsuarios", "Acao de delecao do grupo inicial executada com sucesso.");
							
							if ( numLinhas > 0)
							{
								// Exclusao do usuario com sucesso
								super.log(Definicoes.DEBUG, "atualizaTabelaUsuarios", "Acao de Excluir os dados do usuario executada com sucesso.");
								retorno = Definicoes.RET_OPERACAO_OK;
							}
							else
							{
								// Erro na exclusao do usuario
								super.log(Definicoes.WARN, "atualizaTabelaUsuarios", "Usuario nao existe.");
								retorno = Definicoes.RET_EXCLUSAO_USUARIO_INEXISTENTE;
							}
						}
						else
						{
							// Erro na exclusao do grupo
							super.log(Definicoes.WARN, "setGrupoInicial", "Acao de Excluir o grupo inicial executado com erro.");
						}
					}
					catch (GPPInternalErrorException e)
					{
						// Erro tecnico
						super.log(Definicoes.ERRO, "atualizaTabelaUsuarios", "Erro tecnico:" + e);
						retorno = Definicoes.RET_ERRO_TECNICO;
					}
				} // fim if
			} // fim if
		}
		super.log(Definicoes.DEBUG, "atualizaTabelaUsuarios", "Fim");
		
		return retorno;
	}

	/**
	 * Metodo...: verificaDescErro
	 * Descricao: Verifica a descricao do Erro
	 * @param 	conexaoPrep	- Conexao com o banco de dados
	 * @param 	cod_erro	- Codigo de erro
	 * @return	String		- Descricao do erro refernte ao codigo passado como parametro	
	 * @throws GPPInternalErrorException					
	 */
	private String verificaDescErro(PREPConexao conexaoPrep, int cod_erro)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "checaDescErro", "Inicio CODERRO "+cod_erro);
//		long aIdProcesso = super.getIdLog();
		String retorno = "";
		ResultSet rs_verifica;

		try
		{
			// Busca a descricao do erro na tabela de acordo com o codigo passado
			String sql_verifica = "SELECT DES_RETORNO FROM TBL_GER_CODIGOS_RETORNO " +
								" WHERE VLR_RETORNO = ? ";

			Object paramMsg[] = {new Integer(cod_erro)};
			
			rs_verifica = conexaoPrep.executaPreparedQuery2(sql_verifica, paramMsg, super.logId);
	
			if (rs_verifica.next())
			{
				retorno = rs_verifica.getString(1);
				super.log(Definicoes.DEBUG, "checaDescErro", "Pesquisa feita com sucesso.");
			}
			else
			{
				super.log(Definicoes.ERRO, "checaDescErro", "Ocorreu um erro na pesquisa.");
			}
			rs_verifica.close();
			rs_verifica = null;

		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "checaDescErro", "Excecao Interna GPP ocorrida: "+ e);
		}
		super.log(Definicoes.DEBUG, "checaDescErro", "Fim");
		return retorno;
	}

	/**
	 * Metodo...: setGrupoInicial
	 * Descricao: Inicializa o grupo inicial definido na tabela de configuracao do sistema
	 *            ao usuario sendo importado. O grupo inicial deve existir previamente cadastrado
	 *            no sistema 
	 * @param 	conexaoPrep	- Conexao com o banco de dados
	 * @param 	idUsusario	- Identificador do usuario
	 * @throws  GPPInternalErrorException					
	 */
//	private void setGrupoInicial(PREPConexao conexaoPrep, String idUsuario)throws GPPInternalErrorException
//	{
//		super.log(Definicoes.DEBUG,"setGrupoInicial","Inicio USUARIO "+idUsuario);
//		int idGrupo=0;
//		try
//		{
//			MapConfiguracaoGPP configGPP = MapConfiguracaoGPP.getInstancia();
//			idGrupo = Integer.parseInt("GRUPO_INICIAL");
//
//			String sqlInsert = "INSERT INTO TBL_PPP_GRUPO_USUARIO (ID_GRUPO,ID_USUARIO) VALUES (?,?)";
//			Object paramInsert[] = {new Integer(idGrupo),idUsuario};
//			conexaoPrep.executaPreparedUpdate(sqlInsert,paramInsert,super.getIdLog());
//
//			super.log(Definicoes.DEBUG,"setGrupoInicial","Grupo:"+idGrupo+" associado ao usuario:"+idUsuario);
//		}
//		catch (Exception e)
//		{
//			super.log(Definicoes.ERRO, "setGrupoInicial", "Excecao Interna GPP: "+ e);
//			throw new GPPInternalErrorException("Erro ao adicionar grupo do usuario. Usuario:"+idUsuario+" Grupo:"+idGrupo+" Erro:"+e);
//		}
//		super.log(Definicoes.DEBUG,"setGrupoInicial","Fim");
//	}
	
	/**
	 * Metodo...: deletaGrupo
	 * Descricao: Deleta o grupo associado ao usuario
	 * @param 	conexaoPrep	- Conexao com o banco de dados
	 * @param 	idUsusario	- Identificador do usuario
	 * @return	
	 * @throws  GPPInternalErrorException					
	 */
//	private void deletaGrupo(PREPConexao conexaoPrep, String idUsuario)throws GPPInternalErrorException
//	{
//		super.log(Definicoes.DEBUG, "deletaGrupo", "Inicio USUARIO "+ idUsuario);
//		long aIdProcesso = super.getIdLog();
//		int cod_grupo;
//		int retorno = Definicoes.RET_OPERACAO_OK;
//		ResultSet rs_deleta;
//
//		try
//		{
//			// Deleta os usuarios na tabela de grupo
//			String sql_deleta = "DELETE FROM TBL_PPP_GRUPO_USUARIO WHERE ID_USUARIO=?";
//
//			Object param_deleta[] = {idUsuario};
//			
//			if(conexaoPrep.executaPreparedUpdate(sql_deleta, param_deleta, super.logId)>0 )
//			{
//				super.log(Definicoes.DEBUG, "deletaGrupo", "Acao de delecao do grupo inicial executada com sucesso.");
//			}
//			else
//			{
//				super.log(Definicoes.WARN, "deletaGrupo", "Acao de delecao do grupo inicial executada com erro.");
//			}
//		}
//		catch (Exception e)
//		{
//			super.log(Definicoes.ERRO, "deletaGrupo", "Excecao Interna GPP: "+ e);
//		}
//		super.log(Definicoes.DEBUG, "deletaGrupo", "Fim");
//	}

}

