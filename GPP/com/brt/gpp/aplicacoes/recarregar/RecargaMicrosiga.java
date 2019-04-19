//Definicao do Pacote
package com.brt.gpp.aplicacoes.recarregar;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.recarregar.Recarregar;
import com.brt.gpp.aplicacoes.recarregar.ParametrosRecarga;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

// Arquivos de imports do java
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//Classes DOM para parse e geracao dos XML's
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
  *
  * Este arquivo refere-se a classe RecargaMicrosiga, responsavel pela implementacao das recargas assincronas
  * cujas solicitacoes foram enviadas pelo Microsiga, por intermedio do Vitria
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Daniel Ferreira
  * Data: 				14/03/2005
  *
  * Modificado por:     
  * Data:				
  * Razao:				
  *
  */

public final class RecargaMicrosiga extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	protected long idLog; 										// Armazena o ID do log
		     
	/**
	 * Metodo...: RecargaMicrosiga
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 */
	 public RecargaMicrosiga(long logId)
	 {
		super(logId, Definicoes.CL_RECARGA_MICROSIGA);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);

		this.idLog = logId;
	 }

	/**
	 * Metodo...: executaRecargaMicrosiga
	 * Descricao: Consome as requisicoes de recargas enviadas pelo Microsiga
	 * 
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 */
	public short executaRecargaMicrosiga() throws GPPInternalErrorException 
	{
		long nroRecargasSucesso = 0;
		long nroRecargasFalha = 0;
		short result;
		
		//Obtendo a data inicial do processo
		Calendar calProcesso = Calendar.getInstance();
		Timestamp inicioProcesso = new Timestamp(calProcesso.getTimeInMillis());
		
		super.log(Definicoes.DEBUG, "executaRecargaMicrosiga", "Inicio do processo de recargas Microsiga");
		PREPConexao conexaoPrep = null;

		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		
			// Faz a pesquisa no banco para buscar as requisicoes de recarga enviadas pelo Microsiga
			String sqlQuery = "SELECT " +
							  "  ID_PROCESSAMENTO, " +
							  "  DAT_CADASTRO, " +
							  "  IDT_EVENTO_NEGOCIO, " +
							  "  XML_DOCUMENT, " +
							  "  IDT_STATUS_PROCESSAMENTO " +
							  "FROM " +
							  "  TBL_INT_PPP_IN " +
							  "WHERE " +
							  "  IDT_EVENTO_NEGOCIO = ?       AND " + 
							  "  IDT_STATUS_PROCESSAMENTO = ? " +
							  "ORDER BY DAT_CADASTRO";
			Object parametrosQuery[] = 
			{
				Definicoes.IDT_EVT_NEGOCIO_CR_RECARGA_MIC,
				Definicoes.IDT_PROCESSAMENTO_NOT_OK
			};
			ResultSet resultRecargasMicrosiga = conexaoPrep.executaPreparedQuery(sqlQuery, parametrosQuery, super.getIdLog());
			
			// Para cada registro retornado, realizar a recarga
			while (resultRecargasMicrosiga.next())
			{
				//Obtendo as informacoes do registro
				long idProcessamento = resultRecargasMicrosiga.getLong("ID_PROCESSAMENTO");
				String idtEventoNegocio = resultRecargasMicrosiga.getString("IDT_EVENTO_NEGOCIO");
				Clob xmlDocument = resultRecargasMicrosiga.getClob("XML_DOCUMENT");
				String idtStatusProcessamento = resultRecargasMicrosiga.getString("IDT_STATUS_PROCESSAMENTO");
				
				//Obtendo os parametros de recarga
				ParametrosRecarga pRecarga = parseXmlRecargaMicrosiga(xmlDocument);
				
				//Realizando a recarga
				//short retRecarga = new Short(Definicoes.RET_S_ERRO_TECNICO).shortValue();
				short retRecarga;
				if(pRecarga != null)
				{
					super.log(Definicoes.DEBUG, "executaRecargaMicrosiga",
							  "Realizando recarga do MSISDN: " + pRecarga.getMSISDN());
					
					Recarregar recarregar = new Recarregar(super.logId);
					
					try
					{
						retRecarga = recarregar.executarRecarga(pRecarga.getMSISDN(),
															    pRecarga.getTipoTransacao(),
															    pRecarga.getIdentificacaoRecarga(),
															    pRecarga.getTipoCredito(),
															    pRecarga.getIdValor(),
															    pRecarga.getDatOrigem(),
															    pRecarga.getSistemaOrigem(),
															    pRecarga.getOperador(),
															    pRecarga.getNsuInstituicao(),
															    pRecarga.getHash_cc(),
															    pRecarga.getCpf(),
																null,
																null,
																null,
                                                                null);
			
						nroRecargasSucesso += (retRecarga == (new Short(Definicoes.RET_S_OPERACAO_OK).shortValue())) ? 1: 0;
						nroRecargasFalha   += (retRecarga != (new Short(Definicoes.RET_S_OPERACAO_OK).shortValue())) ? 1: 0;
					}
					catch (Exception e)
					{
						retRecarga = new Short(Definicoes.RET_S_ERRO_TECNICO).shortValue();
						nroRecargasFalha++;
						super.log(Definicoes.ERRO, "executaRecargaMicrosiga", "Excecao durante a operacao de recarga: "+ e);
					}
				}
				else
				{
					super.log(Definicoes.INFO,"executaRecargaMicrosiga","Problema com o XML de Recarga passado para o GPP");
					retRecarga = new Short(Definicoes.RET_S_ERRO_TECNICO).shortValue();
					nroRecargasFalha++;
				}
				
				// Montar xml de saída e atualizar status na ppp_in
				// Somente se não ocorreu erro técnico. Se houve erro técnico, ignora essa entrada
				if(retRecarga != Definicoes.RET_ERRO_TECNICO)
				{
					//Montando o XML de retorno para o Vitria
					String xmlRetorno = getXmlRetorno(pRecarga, retRecarga);
					
					try
					{
					    // Inserindo resulta da tabela de saida para leitura do Vitria
						String sqlInsert = "INSERT INTO TBL_INT_PPP_OUT " +
										   "  (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
										   "   XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) " +
										   "VALUES " +
										   "  (?, ?, ?, ?, ?) ";
						Object[] parametrosInsert =
						{
							new Long(idProcessamento),
							new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()),
							Definicoes.IDT_EVT_NEGOCIO_FN_RECARGA_MIC,
							xmlRetorno,
							Definicoes.IDT_PROCESSAMENTO_NOT_OK
							//(retRecarga == Definicoes.RET_OPERACAO_OK) ? 
								//Definicoes.IDT_PROCESSAMENTO_NOT_OK : Definicoes.IDT_PROCESSAMENTO_ERRO
						};
						conexaoPrep.executaPreparedUpdate(sqlInsert, parametrosInsert, super.getIdLog());
						
						//Determinando o status de processamento para atualizacao do registro
						idtStatusProcessamento = (retRecarga == Definicoes.RET_OPERACAO_OK) ? 
							Definicoes.IDT_PROCESSAMENTO_OK : Definicoes.IDT_PROCESSAMENTO_ERRO;
					}
					catch(Exception e)
					{
						//Status de processamento foi um erro tecnico
						idtStatusProcessamento = Definicoes.IDT_PROCESSAMENTO_ERRO;
						super.log(Definicoes.ERRO, "executaRecargaMicrosiga", 
								  "Nao foi possivel inserir o XML de retorno para o Vitria: " + e);
					}
					finally
					{
						try
						{
							//Atualizando o valor do status de processamento do registro atual
							String sqlUpdate = "UPDATE TBL_INT_PPP_IN " +
											   "SET IDT_STATUS_PROCESSAMENTO = ? " +
											   "WHERE ID_PROCESSAMENTO = ? AND IDT_EVENTO_NEGOCIO = ? ";
							Object[] parametrosUpdate =
							{
								idtStatusProcessamento,
								new Long(idProcessamento),
								idtEventoNegocio
							};
							conexaoPrep.executaPreparedUpdate(sqlUpdate, parametrosUpdate, super.getIdLog());
						}
						catch(Exception e)
						{
							super.log(Definicoes.ERRO, "executaRecargaMicrosiga", 
									  "Nao foi possivel atualizar o registro de recarga: " + e);
						}
					}					
				}
				else
				{
					super.log(Definicoes.INFO,"executaRecargaMicrosiga","Erro Tecnico no Processamento do Pedido/Assinante:"+idProcessamento+"/"+pRecarga.getMSISDN());
				}
			}
			
			resultRecargasMicrosiga.close();
			result = new Short(Definicoes.RET_S_OPERACAO_OK).shortValue();
		}
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "executaRecargaMicrosiga", "Excecao Interna GPP ocorrida: "+ e1);
			result = new Short(Definicoes.RET_S_ERRO_TECNICO).shortValue();
		}
		catch (SQLException e3)
		{
			super.log(Definicoes.ERRO, "executaRecargaMicrosiga", "Erro durante execucao de operacao no Banco de Dados: "+ e3);
			result = new Short(Definicoes.RET_S_ERRO_TECNICO).shortValue();
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			//Obtendo a data final do processo
			calProcesso = Calendar.getInstance();
			Timestamp fimProcesso = new Timestamp(calProcesso.getTimeInMillis());

			String descricao = "Recarga Microsiga. Numero de recargas com sucesso: " + nroRecargasSucesso + 
			                   ". Numero de recargas com erro: " + nroRecargasFalha + ".";
			
			SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			//chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_RECARGA_MICROSIGA, 
					                      conversorData.format(inicioProcesso), 
										  conversorData.format(fimProcesso), 
										  Definicoes.PROCESSO_SUCESSO, 
										  descricao, 
										  conversorData.format(inicioProcesso));
			
			super.log(Definicoes.INFO, "executaRecargaMicrosiga", 
					  "Fim do processo batch de Recargas Microsiga");
		}
		
		return result;
	}
	
	/**
	 * Metodo....: parseXmlRecargaMicrosiga
	 * Descricao.: Retorna os parametros de recarga definidos no XML enviado pelo Microsiga 
	 * @param xml 					- XML com os parametros de Recarga
	 * @return ParametrosRecarga	- Data da ultima execucao do processo
	 */
	private ParametrosRecarga parseXmlRecargaMicrosiga(Clob xml)
	{
		ParametrosRecarga result = new ParametrosRecarga();
		
		try
		{
			//Criando os objetos necessarios para realizar o parse do XML.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource input = new InputSource(xml.getCharacterStream());
			//Realizando o parse do XML.
			Document document = builder.parse(input);
			Element elmRoot = (Element)document.getElementsByTagName("root").item(0);
			Element elmGppRecarga = (Element)elmRoot.getElementsByTagName("GPPRecarga").item(0);

			//Obtendo os parametros de recarga a partir do documento criado.			
			//Elementos obrigatorios.
			if((elmGppRecarga.getElementsByTagName("msisdn").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("msisdn").item(0).getChildNodes().getLength() > 0))
			{
				result.setMSISDN(elmGppRecarga.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("tipoTransacao").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("tipoTransacao").item(0).getChildNodes().getLength() > 0))
			{
				result.setTipoTransacao(elmGppRecarga.getElementsByTagName("tipoTransacao").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("identificacaoRecarga").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("identificacaoRecarga").item(0).getChildNodes().getLength() > 0))
			{
				result.setIdentificacaoRecarga(elmGppRecarga.getElementsByTagName("identificacaoRecarga").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("dataHora").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("dataHora").item(0).getChildNodes().getLength() > 0))
			{
				SimpleDateFormat conversorDataHoraGPP = new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
				result.setDatOrigem(conversorDataHoraGPP.parse(elmGppRecarga.getElementsByTagName("dataHora").item(0).getChildNodes().item(0).getNodeValue()));
			}
			if((elmGppRecarga.getElementsByTagName("valor").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("valor").item(0).getChildNodes().getLength() > 0))
			{
				result.setIdValor(new Double(elmGppRecarga.getElementsByTagName("valor").item(0).getChildNodes().item(0).getNodeValue()).doubleValue());
			}
			if((elmGppRecarga.getElementsByTagName("tipoCredito").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("tipoCredito").item(0).getChildNodes().getLength() > 0))
			{
				result.setTipoCredito(elmGppRecarga.getElementsByTagName("tipoCredito").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("sistemaOrigem").getLength() > 0) &&
		       (elmGppRecarga.getElementsByTagName("sistemaOrigem").item(0).getChildNodes().getLength() > 0))
			{
				result.setSistemaOrigem(elmGppRecarga.getElementsByTagName("sistemaOrigem").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("operador").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("operador").item(0).getChildNodes().getLength() > 0))
			{
				result.setOperador(elmGppRecarga.getElementsByTagName("operador").item(0).getChildNodes().item(0).getNodeValue());
			}
			//Elementos nao obrigatorios.
			if((elmGppRecarga.getElementsByTagName("nsuInstituicao").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("nsuInstituicao").item(0).getChildNodes().getLength() > 0))
			{
				result.setNsuInstituicao(elmGppRecarga.getElementsByTagName("nsuInstituicao").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("hashCc").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("hashCc").item(0).getChildNodes().getLength() > 0))
			{
				result.setHash_cc(elmGppRecarga.getElementsByTagName("hashCc").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("cpfCnpj").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("cpfCnpj").item(0).getChildNodes().getLength() > 0))
			{
				result.setCpf(elmGppRecarga.getElementsByTagName("cpfCnpj").item(0).getChildNodes().item(0).getNodeValue());
			}
		}
		catch(Exception e)
		{
			result = null;
			super.log(Definicoes.ERRO, "parseXmlRecargaMicrosiga", 
					  "Erro durante a interpretacao do XML. XML invalido.");
		}
		
		return result;
	}
	
	/**
	 * Metodo....: getXmlRetorno
	 * Descricao.: Retorna o XML de retorno da operacao para o Vitria 
	 * @param	identificacaoRecarga	- Identificador da recarga fornecido no XML da requisicao da recarga
	 * @param	sistemaOrigem			- Sistema de origem da requisicao da recarga
	 * @param	codigoRetorno			- Codigo de retorno da operacao
	 * @return	String					- XML de retorno para o Vitria
	 */
	private String getXmlRetorno(ParametrosRecarga parametros, short codigoRetorno)
	{
		String result = null;
		DecimalFormat conversorNumero = new DecimalFormat("0000");
		
		//Convertendo o codigo de retorno para formato padronizado pelo GPP
		String strCodigoRetorno = conversorNumero.format(codigoRetorno);
		
		//Obtendo a descricao do codigo de retorno
		String descricaoRetorno = getDescricaoCodigoRetorno(strCodigoRetorno);
		
		//Obtendo a identificacao da recarga e o sistema de origem
		String identificacaoRecarga = null;
		String sistemaOrigem = null;
		if(parametros != null)
		{
			identificacaoRecarga = parametros.getIdentificacaoRecarga();
			sistemaOrigem = parametros.getSistemaOrigem();
		}
		
		//Construindo o XML de retorno
		GerarXML geradorXml = new GerarXML("root");
		geradorXml.abreNo("GPPRetornoRecarga");
		geradorXml.adicionaTag("codRetorno", strCodigoRetorno);
		geradorXml.adicionaTag("descricao", descricaoRetorno);
		geradorXml.adicionaTag("identificacaoRecarga", identificacaoRecarga);
		geradorXml.adicionaTag("sistemaOrigem", sistemaOrigem);
		geradorXml.fechaNo();
		
		result = geradorXml.getXML();
		
		return result;
	}
	
	/**
	 * Metodo....:	getDescricaoCodigoRetorno
	 * Descricao.:	Retorna a descricao do codigo de retorno passado por parametro. Metodo provisorio enquanto nao 
	 * 			 	houver mapeamento dos codigos de retorno
	 * @param	codigoRetorno	- Codigo de retorno da operacao
	 * @return	String			- Descricao do codigo de retorno
	 */
	private String getDescricaoCodigoRetorno(String codigoRetorno)
	{
		String result = null;
		PREPConexao conexaoPrep = null;
		ResultSet resultDescricao = null;
		
		try
		{
			// Busca uma conexao de banco de dados		
			conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			//Realizando consulta no banco de dados para obter a descricao
			String sqlQuery = "SELECT DES_RETORNO FROM TBL_GER_CODIGOS_RETORNO WHERE VLR_RETORNO = ? ";
			Object[] parametros = {codigoRetorno};
			resultDescricao = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.getIdLog());
			
			//Obtendo a descricao
			if(resultDescricao.next())
			{
				result = resultDescricao.getString("DES_RETORNO");
			}
			resultDescricao.close();
		}
		catch(Exception e)
		{
			result = null;
			super.log(Definicoes.ERRO, "getDescricaoCodigoRetorno", 
					  "Nao foi possivel obter a descricao do codigo de retorno: " + e);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		
		return result;
	}
	
}