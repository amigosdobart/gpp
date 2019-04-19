package com.brt.gpp.aplicacoes.campanha.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.SMSCampanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe realiza o tratamento de acesso a banco de dados relativo as 
 * informacoes de Campanhas promocionais.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class CampanhaDAO 
{
	
	private static final String SQL_VIGENTES = "SELECT ID_CAMPANHA," +
													  "NOM_CAMPANHA," +
													  "DAT_INICIAL_VALIDADE," +
													  "DAT_FIM_VALIDADE, " +
													  "DAT_INI_VAL_CONCESSAO, " +
													  "DAT_FIM_VAL_CONCESSAO, " +
													  "TIP_TRANSACAO, " +
													  "NOM_CLASSE_PRODUTOR, " +
													  "CURSOR(SELECT NOM_CLASSE " +
													           "FROM TBL_CAM_PARAMETROS_CAMPANHA P " +
													          "WHERE P.ID_CAMPANHA = C.ID_CAMPANHA " +
													        ") AS PARAMETROS, " +
													  "CURSOR(SELECT NOM_CLASSE " +
													           "FROM TBL_CAM_CONDICOES_CAMPANHA B " +
													          "WHERE B.ID_CAMPANHA = C.ID_CAMPANHA " +
													        ") AS CONDICOES, " +
													  "CURSOR(SELECT DES_MENSAGEM,NUM_DIAS_PERIODICIDADE,TIP_SMS_CAMPANHA " +
													           "FROM TBL_CAM_SMS_CAMPANHA S " +
													          "WHERE S.ID_CAMPANHA = C.ID_CAMPANHA " +
													         ") AS SMS " +
	                                             "FROM TBL_CAM_CAMPANHA C " +
	                                            "WHERE DAT_INICIAL_VALIDADE <= ? " +
	                                              "AND (DAT_FIM_VALIDADE >= ? OR DAT_FIM_VALIDADE IS NULL)";
	
	/**
	 * Metodo....:getCampanha
	 * Descricao.:Retorna o objeto campanha baseado nas informacoes do resultSet
	 * @param rs - ResultSet disponivel
	 * @return Campanha - Objeto campanha preenchido
	 * @throws SQLException
	 */
	private static Campanha getCampanha(ResultSet rs) throws SQLException
	{
		Campanha campanha = new Campanha();
		campanha.setId					(rs.getLong		("ID_CAMPANHA")			 );
		campanha.setNomeCampanha		(rs.getString	("NOM_CAMPANHA")		 );
		campanha.setValidadeInicial		(rs.getDate		("DAT_INICIAL_VALIDADE") );
		campanha.setValidadeFinal		(rs.getDate		("DAT_FIM_VALIDADE")	 );
		campanha.setValidadeIniConcessao(rs.getDate		("DAT_INI_VAL_CONCESSAO"));
		campanha.setValidadeFimConcessao(rs.getDate		("DAT_FIM_VAL_CONCESSAO"));
		campanha.setTipoTransacao       (rs.getString	("TIP_TRANSACAO")        );
		campanha.setProdutorCampanha	(rs.getString	("NOM_CLASSE_PRODUTOR")	 );
		
		// Adiciona os nomes das classes de parametros baseado no cursor
		// que vem no resultSet da consulta de campanhas
		ResultSet parametros = (ResultSet)rs.getObject("PARAMETROS");
		while (parametros.next())
			campanha.addParametro(parametros.getString(1));
		// Adiciona os nomes das classes de condicoes de concessao baseado
		// no cursor que vem no resultSet da consulta de campanhas
		ResultSet condicoes = (ResultSet)rs.getObject("CONDICOES");
		while (condicoes.next())
			campanha.addCondicao(condicoes.getString(1));
		
		// Adiciona as mensagens de SMS a serem enviadas para a campanha
		ResultSet mensagens = (ResultSet)rs.getObject("SMS");
		while (mensagens.next())
		{
			SMSCampanha sms = new SMSCampanha();
			sms.setMensagemSMS		(mensagens.getString(1));
			sms.setDiasPeriodicidade(mensagens.getInt(2));
			sms.setTipoSmsCampanha	(mensagens.getString(3));
			
			campanha.addSmCampanha(sms);
		}
		
		return campanha;
	}

	/**
	 * Este metodo realiza a pesquisa de campanhas que estao vigentes na data atual. 
	 * Retorna uma colecao de objetos Campanha para serem utilizados no processamento 
	 * de inscricao e concessao de creditos.
	 * 
	 * @return Collection - Lista de campanhas vigentes
	 */
	public static Collection getCampanhasVigentes() 
	{
		// Define a variavel que irah armazenar as campanhas vigentes
		// Caso algum erro ocorra ou nao existam campanhas vigentes entao
		// eh retornado a lista vazia
		Collection campanhas = new ArrayList();
		
		long idProcesso = GerentePoolLog.getInstancia(CampanhaDAO.class).getIdProcesso("CampanhaDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			// Executa a busca de campanhas vigentes e preenche os objetos a serem retornados
			Object param[] = {new Timestamp(Calendar.getInstance().getTimeInMillis())
					         ,new Timestamp(Calendar.getInstance().getTimeInMillis())
					         };
			ResultSet rs = conexaoPrep.executaPreparedQuery(SQL_VIGENTES,param,idProcesso);
			// Executa a iteracao entre todos os registros de campanhas disponiveis
			// para cada campanha um objeto eh criado para armazenar as informacoes
			// e entao inserido na lista a ser devolvida
			while (rs.next())
				campanhas.add(getCampanha(rs));
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(CampanhaDAO.class).log(idProcesso,Definicoes.ERRO
					                                          ,"CampanhaDAO"
					                                          ,"getCampanhasVigentes"
					                                          ,"Erro ao pesquisar campanhas vigentes. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return campanhas;
	}
}
