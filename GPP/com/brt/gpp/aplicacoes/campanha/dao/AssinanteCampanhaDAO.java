package com.brt.gpp.aplicacoes.campanha.dao;

import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.util.ParametroCampanhaXMLParser;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Esta classe eh uma classe utilitaria para a realizacao da persistencia dos 
 * valores da classe AssinanteCampanha. Os metodos sao para tratamento de insercao 
 * e pesquisa.
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class AssinanteCampanhaDAO 
{
	private static final String SQL_INS = 	"INSERT INTO TBL_CAM_ASSINANTE_CAMPANHA A " +
											"SELECT ? AS ID_CAMPANHA " +
											      ",? AS IDT_MSISDN " +
											      ",? AS DAT_INCLUSAO " +
											      ",? AS DAT_RETIRADA_CAMPANHA " +
											      ",? AS DAT_ULT_ENVIO_SMS " +
											      ",? AS XML_DOCUMENT " +
											  "FROM DUAL " +
											 "WHERE NOT EXISTS (SELECT 1 " +
											                     "FROM TBL_CAM_ASSINANTE_CAMPANHA B " +
											                    "WHERE B.ID_CAMPANHA = ? " +
											                      "AND B.IDT_MSISDN  = ?)";

	private static final String SQL_PES = "SELECT IDT_MSISDN " +
	                                            ",DAT_INCLUSAO " +
	                                            ",DAT_RETIRADA_CAMPANHA " +
	                                            ",DAT_ULT_ENVIO_SMS " +
	                                            ",XML_DOCUMENT " +
	                                        "FROM TBL_CAM_ASSINANTE_CAMPANHA " +
	                                       "WHERE ID_CAMPANHA = ? " +
	                                         "AND DAT_RETIRADA_CAMPANHA IS NULL";
	
	private static final String SQL_RET = "UPDATE TBL_CAM_ASSINANTE_CAMPANHA " +
	                                         "SET DAT_RETIRADA_CAMPANHA = ? " +
	                                       "WHERE ID_CAMPANHA = ? " +
	                                         "AND IDT_MSISDN = ?";
	
	private static final String SQL_SMS = "UPDATE TBL_CAM_ASSINANTE_CAMPANHA " +
										     "SET DAT_ULT_ENVIO_SMS = ? " +
										   "WHERE ID_CAMPANHA = ? " +
										     "AND IDT_MSISDN = ?";

	/**
	 *	Statement SQL para obtencao de registros de cadastro de assinantes em campanhas.
	 */
	private static final String SQL_EXISTE_CAM_ASS = "SELECT 1 " +
											  		 "  FROM tbl_cam_assinante_campanha " +
													 " WHERE idt_msisdn = ? " +
													 "   AND id_campanha = ? ";
	
	/**
	 *	Statement DML para atualizacao dos parametros do cadastro do assinante na campanha.
	 */
	private static final String UPT_PARAM_CAM_ASS = "UPDATE tbl_cam_assinante_campanha " +
													"   SET xml_document = ? " +
													" WHERE idt_msisdn = ? " +
													"   AND id_campanha = ? ";
	
	/**
	 * Este metodo realiza a insercao em tabela de um assinate para uma determinada 
	 * campanha.
	 * 
	 * @param msisdn a ser inserido para uma determinada campanha - Assinante a ser registrado na campanha
	 * @param campanha a ser utilizada para inserir o assinante   - Campanha onde o assinante serah registrado
	 */
	public static void insereAssinante(String msisdn, Campanha campanha, Map parametros) 
	{
		long idProcesso = GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).getIdProcesso("AssinanteCampanhaDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			// O comando para inscrever assinantes em campanha realiza um insert select
			// com a verficacao not exists pois como os parametros de inscricao podem
			// levar o assinante a serem elegiveis mais de uma vez para a mesma campanha
			// entao essa verificacao evita que erros de chave primaria violada sejam 
			// mostradas para algo que pode ser natural da campanha
			Timestamp dataAtual = new Timestamp(Calendar.getInstance().getTimeInMillis());
			Long	  idCampanha= new Long(campanha.getId());
			Object param[] = {idCampanha
					         ,msisdn
					         ,dataAtual
					         ,null
					         ,dataAtual
					         ,parametros != null ? ParametroCampanhaXMLParser.getXML(campanha,parametros).toString() : null
					         ,idCampanha
					         ,msisdn
					         };
			conexaoPrep.executaPreparedUpdate(SQL_INS,param,idProcesso);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).log(idProcesso,Definicoes.ERRO
                    										,"AssinanteCampanhaDAO"
                    										,"insereAssinante"
                    										,"Erro ao inserir assinante:"+msisdn+". Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
	
	/**
	 * Este metodo retorna um objeto ResultSet de banco de dados contendo as 
	 * informacoes de assinantes para uma determinada campanha. Este objeto eh 
	 * retornado para ser utilizado no gerente de Concessao de Creditos utilizando o 
	 * modelo  Produtor/Consumidor.
	 * 
	 * @param campanha a ser pesquisada os assinantes - Campanha onde serah pesquisado os assinantes
	 * @param conexaoPrep a ser utilizada na pesquisa
	 * @return ResutlSet - Result contendo as informacoes de assinantes da campanha
	 */
	public static ResultSet getAssinantesCampanha(Campanha campanha, PREPConexao conexaoPrep) 
	{
		// Define a variavel que serah o resultado da pesquisa
		// de assinantes da campanha. O resultSet serah percorrido
		// pelo gerente que requisitou esses assinantes
		ResultSet assinantes = null;
		long idProcesso = GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).getIdProcesso("AssinanteCampanhaDAO");
		try
		{
			Object param[] = {new Long(campanha.getId())};
			assinantes = conexaoPrep.executaPreparedQuery(SQL_PES,param,idProcesso);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).log(idProcesso,Definicoes.ERRO
                    										,"AssinanteCampanhaDAO"
                    										,"getAssinantesCampanha"
                    										,"Erro ao pesquisar assinantes da campanha "+campanha.getNomeCampanha()+". Erro:"+e);
		}
		return assinantes;
	}
	
	/**
	 *	Indica se o assinante esta cadastrado na campanha.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		campanha				Identificador da campanha.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se o assinante esta cadastrado na campanha e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean existeAssinanteCampanha(String msisdn, long campanha, PREPConexao conexaoPrep) throws Exception 
	{
		ResultSet registros = null;
		
		Object[] parametros = 
		{
			msisdn,
			new Long(campanha)
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(AssinanteCampanhaDAO.SQL_EXISTE_CAM_ASS, parametros, conexaoPrep.getIdProcesso());
			return registros.next();
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
	}
	
	/**
	 * Este metodo realiza a atualizacao no registro de AssinanteCampanha para indicar 
	 * que o assinante nao mais serah processado para a campanha.
	 * 
	 * @param assinante a ser retirado do processamento da campanha
	 *  - Assinante a ser retirado da campanha
	 */
	public static void retiraAssinante(AssinanteCampanha assinante) 
	{
		long idProcesso = GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).getIdProcesso("AssinanteCampanhaDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			// Executa o comando para a retirada do assinante da campanha. O comando
			// atualiza o campo dat_retirada_campanha para indicar quando o assinante
			// recebeu o bonus da campanha evitando entao que o mesmo seja processado
			// novamente
			Object param[] = {new Timestamp(Calendar.getInstance().getTimeInMillis())
					         ,new Long(assinante.getCampanha().getId())
					         ,assinante.getMsisdn()};

			conexaoPrep.executaPreparedUpdate(SQL_RET,param,idProcesso);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).log(idProcesso,Definicoes.ERRO
                    										,"AssinanteCampanhaDAO"
                    										,"retiraAssinante"
                    										,"Erro ao retirar o assinante:"+assinante.getMsisdn()+" da campanha. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
	
	/**
	 *	Atualiza os parametros do cadastro do assinante na campanha. 
	 *
	 *	@param		assinante				Informacoes do cadastro do assinante na campanha.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public static void atualizarParamsAssinanteCampanha(AssinanteCampanha assinante, PREPConexao conexaoPrep) throws Exception
	{
		Object[] parametros = 
		{
			(assinante.getParametros() != null) ? 
				ParametroCampanhaXMLParser.getXML(assinante.getCampanha(), assinante.getParametros()).toString() : null,
			assinante.getMsisdn(),
			new Long(assinante.getCampanha().getId())
		};
		
		conexaoPrep.executaPreparedUpdate(AssinanteCampanhaDAO.UPT_PARAM_CAM_ASS, parametros, conexaoPrep.getIdProcesso());
	}
	
	/**
	 * Metodo....:marcaEnvioSMS
	 * Descricao.:Este metodo marca na tabela a data do ultimo envio de SMS de campanha para o assinante
	 * @param assinante - AssinanteCampanha a ser marcado o envio de SMS
	 */
	public static void marcaEnvioSMS(AssinanteCampanha assinante)
	{
		long idProcesso = GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).getIdProcesso("AssinanteCampanhaDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			Object param[] = {new Timestamp(Calendar.getInstance().getTimeInMillis())
					         ,new Long(assinante.getCampanha().getId())
					         ,assinante.getMsisdn()};

			conexaoPrep.executaPreparedUpdate(SQL_SMS,param,idProcesso);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(AssinanteCampanhaDAO.class).log(idProcesso,Definicoes.ERRO
                    										,"AssinanteCampanhaDAO"
                    										,"marcaEnvioSMS"
                    										,"Erro ao marcar data de envio de SMS para o assinante:"+assinante.getMsisdn()+". Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
	
}
