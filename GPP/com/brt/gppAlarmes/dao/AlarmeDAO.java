package com.brt.gppAlarmes.dao;

import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.conexoes.GerentePoolConexoes;

import java.io.Reader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Clob;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Essa classe realiza a persistencia de informacoes relativas
 * ao objeto Alarme
 * 
 * @author Joao Carlos
 * Data..: 18-Mar-2005
 *
 */
public class AlarmeDAO
{
	private static 	AlarmeDAO 			instance;
	private 		Logger 				logger;
	private	static 	Map					poolObjetos;
	private static	String				SQL_PESQUISA = "SELECT id_alarme, nom_alarme, idt_status, ind_envio_trap_snmp_alerta, " +
														    "ind_envio_trap_snmp_falha, ind_envio_sms_alerta, " +
														    "ind_envio_sms_falha, idt_dia, idt_mes, idt_ano, idt_dia_semana, " +
														    "idt_hora, idt_minuto, vlr_minimo_alerta, vlr_minimo_falha, " +
														    "vlr_maximo_alerta, vlr_maximo_falha, idt_atraso_maximo_falha, " +
														    "idt_atraso_maximo_alerta, ind_envio_email_falha, ind_envio_email_alerta, " +
															   "sql_busca_contador,des_motivo,num_dias_historico " +
														"FROM tbl_alr_alarmes " +
														"WHERE id_alarme = ?";

	private static	String				SQL_PESQUISA_TODOS = "SELECT id_alarme, nom_alarme, idt_status, ind_envio_trap_snmp_alerta, " +
																    "ind_envio_trap_snmp_falha, ind_envio_sms_alerta, " +
																    "ind_envio_sms_falha, idt_dia, idt_mes, idt_ano, idt_dia_semana, " +
																    "idt_hora, idt_minuto, vlr_minimo_alerta, vlr_minimo_falha, " +
																    "vlr_maximo_alerta, vlr_maximo_falha, idt_atraso_maximo_falha, " +
																    "idt_atraso_maximo_alerta, ind_envio_email_falha, ind_envio_email_alerta, " +
																	   "sql_busca_contador,des_motivo,num_dias_historico " +
																"FROM tbl_alr_alarmes ";
	
	private static String				SQL_APAGA_HISTORICO = "DELETE FROM tbl_alr_eventos_alarme " +
																	"WHERE id_alarme = ? " +
																	"AND dat_execucao < ? ";

	/**
	 * Metodo....:AlarmeDAO
	 * Descricao.:Construtor da classe. Incia a conexao com o banco de dados que 
	 *            esta classe ira utilizar durante seu ciclo de vida
	 */
	private AlarmeDAO() throws Exception
	{
		logger = Logger.getLogger("Alarmes");
		poolObjetos = new HashMap();
	}

	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia dessa classe (Singleton)
	 * @return AlarmeDAO singleton de acesso aos dados da classe Alarme
	 */
	public static AlarmeDAO getInstance() throws Exception
	{
		if(instance==null)
			instance = new AlarmeDAO();

		return instance;
	}

	/**
	 * Metodo....:close
	 * Descricao.:Marca a instancia como nulo
	 *
	 */
	public void close()
	{
		instance = null;
	}

	/**
	 * Metodo....:getAlarme
	 * Descricao.:Cria o objeto alarme para conter os valores existentes no result set
	 * @param 	rs		- Result set contendo as informacoes do alarme
	 * @return	Alarme	- Dados contendo os valores do result set de banco de dados
	 * @throws SQLException
	 */
	private synchronized Alarme getAlarme(ResultSet rs) throws SQLException,IOException
	{
		logger.debug("Criando objeto para o alarme "+rs.getString("ID_ALARME"));
		Alarme alr = (Alarme)poolObjetos.get(rs.getString("ID_ALARME"));
		if (alr == null)
			alr = new Alarme		(rs.getString("ID_ALARME")															);

		alr.setNomeAlarme			(rs.getString("NOM_ALARME")															);
		alr.setStatus				(rs.getString("IDT_STATUS") != null ? rs.getString("IDT_STATUS") :	Alarme.ALARME_OK);
		alr.setAtrasoMaxAlerta		(rs.getLong	 ("IDT_ATRASO_MAXIMO_ALERTA")											);
		alr.setAtrasoMaxFalha		(rs.getLong	 ("IDT_ATRASO_MAXIMO_FALHA")											);
		alr.setValorMaxAlerta		(rs.getDouble("VLR_MAXIMO_ALERTA")													);
		alr.setValorMaxFalha		(rs.getDouble("VLR_MAXIMO_FALHA")													);
		alr.setValorMinAlerta		(rs.getDouble("VLR_MINIMO_ALERTA")													);
		alr.setValorMinFalha		(rs.getDouble("VLR_MINIMO_FALHA")													);
		alr.setMotivoAlarme			(rs.getString("DES_MOTIVO")															);
		alr.setNumDiasHistorico		(rs.getInt   ("NUM_DIAS_HISTORICO")													);

		alr.setEnviaSMSAlerta		(rs.getInt("IND_ENVIO_SMS_ALERTA"		) == 1 ? true : false);
		alr.setEnviaSMSFalha		(rs.getInt("IND_ENVIO_SMS_FALHA" 		) == 1 ? true : false);
		alr.setEnviaTrapSNMPAlerta	(rs.getInt("IND_ENVIO_TRAP_SNMP_ALERTA"	) == 1 ? true : false);
		alr.setEnviaTrapSNMPFalha	(rs.getInt("IND_ENVIO_TRAP_SNMP_FALHA"	) == 1 ? true : false);
		alr.setEnviaEMailFalha		(rs.getInt("IND_ENVIO_EMAIL_FALHA"		) == 1 ? true : false);
		alr.setEnviaEMailAlerta		(rs.getInt("IND_ENVIO_EMAIL_ALERTA"		) == 1 ? true : false);
		
		alr.setListaEMail			(getListaEMail(alr));
		alr.setListaAssinantes		(getListaAssinantes(alr));

		// Define os valores para a configuracao do agendamento
		logger.debug("Definindo agendamento do alarme "+rs.getString("ID_ALARME"));
		alr.getAgendamento().setDias		(rs.getString("IDT_DIA"));
		alr.getAgendamento().setMeses		(rs.getString("IDT_MES"));
		alr.getAgendamento().setAnos		(rs.getString("IDT_ANO"));
		alr.getAgendamento().setDiasSemana	(rs.getString("IDT_DIA_SEMANA"));
		alr.getAgendamento().setHoras		(rs.getString("IDT_HORA"));
		alr.getAgendamento().setMinutos		(rs.getString("IDT_MINUTO"));
		
		// Busca valor definido para sql de execucao da busca dos valores para contagem
		Clob sqlContador = rs.getClob("SQL_BUSCA_CONTADOR");
		if (sqlContador != null)
		{
			logger.debug("Buscando SQL contador do alarme "+rs.getString("ID_ALARME"));
		    char chr_buffer[] = new char[(int)sqlContador.length()];
		    Reader chr_instream = sqlContador.getCharacterStream();
			chr_instream.read(chr_buffer,0,(int)sqlContador.length());
			alr.setSQLBuscaContador(new String(chr_buffer));
		}

		poolObjetos.put(alr.getIdAlarme(),alr);
		return alr;
	}

	/**
	 * Metodo....:findById
	 * Descricao.:Retorna o objeto Alarme utilizando o seu ID para pesquisa
	 * @param idAlarme 	- ID do alarme a ser pesquisado
	 * @return Alarme	- Objeto alarme contendo as informacoes do ID especificado
	 */
	public synchronized Alarme findById(String idAlarme)
	{
		Connection conexao = null;
		Alarme alr = null;
		try
		{
			conexao = GerentePoolConexoes.getInstance().getConexao();
			PreparedStatement pstmt = conexao.prepareStatement(SQL_PESQUISA);
			pstmt.setString(1,idAlarme);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				alr = getAlarme(rs);
			
			rs.close();
			pstmt.close();
		}
		catch(Exception se)
		{
			logger.error("Erro na pesquisa de Alarme por Id. Erro:"+se);
			alr = null;
		}
		finally
		{
			try
			{
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch(Exception e)
			{
			}
		}
		return alr;
	}
	
	/**
	 * Metodo....:findAll
	 * Descricao.:Retorna uma lista contendo todos os alarmes cadastrados
	 * @return Collection - Lista de objetos do tipo Alarme contendo as informacoes cadastradas
	 */
	public synchronized Collection findAll()
	{
		Connection conexao = null;
		logger.info("Pesquisando alarmes cadastrados para analise.");
		try
		{
			conexao = GerentePoolConexoes.getInstance().getConexao();
			PreparedStatement pstmt = conexao.prepareStatement(SQL_PESQUISA_TODOS);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				// O metodo getAlarme apesar de retornar o objeto alarme, o objeto retornado
				// nao sera utilizado pois este mesmo metodo adiciona o objeto a um pool no singleton
				// entao a lista a ser retornada sera a partir desse pool de dados, uma vez que este
				// metodo retorna todos os objetos de alarmes
				getAlarme(rs);
			
			rs.close();
			pstmt.close();
		}
		catch(Exception se)
		{
			logger.error("Erro na pesquisa de todos os Alarmes. Erro:"+se);
			return new LinkedList();
		}
		finally
		{
			try
			{
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch (Exception e)
			{
			}
		}
		return poolObjetos.values();
	}
	
	/**
	 * Metodo....:update
	 * Descricao.:Realiza a persistencia do estado do objeto Alarme em banco de dados
	 * @param alarme - Objeto alarme a ser armazenado
	 * @throws SQLException
	 */
	public synchronized void update(Alarme alarme) throws Exception
	{
		Connection conexao = null;
		PreparedStatement pstmt = null;
		try
		{
			String update = "UPDATE tbl_alr_alarmes " +
		                   "SET idt_status = ?, dat_ultima_execucao = ?, " +
						       "des_motivo = decode(?,'OK',null,?) " +
						 "WHERE id_alarme = ?";
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pstmt = conexao.prepareStatement(update);
			pstmt.setString(1,alarme.getStatus());
			if (alarme.getDataUltimaExecucao() != null)
				pstmt.setTimestamp(2,new Timestamp(alarme.getDataUltimaExecucao().getTime()));
			else
				pstmt.setNull(2,Types.TIMESTAMP);

			pstmt.setString(3,alarme.getStatus());
			pstmt.setString(4,alarme.getMotivoAlarme());
			pstmt.setString(5,alarme.getIdAlarme());

			pstmt.executeUpdate();
			conexao.commit();
		}
		catch(Exception e)
		{
			conexao.rollback();
			throw(e);
		}
		finally
		{
			pstmt.close();
			try
			{
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch (Exception e)
			{
			}
		}
		
	}
	
	/**
	 * Metodo....:getListaEMail
	 * Descricao.:Retorna a lista de enderecos para envio de alertas por e-mail desse alarme
	 * @param alarme 		- Alarme a ser pesquisado lista de enderecos para aviso
	 * @return Collection	- Colecao contendo em cada elemento um endereco de e-mail
	 */
	public synchronized Collection getListaEMail(Alarme alarme)
	{
		Connection conexao = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		logger.debug("Buscando lista de e-mail's para o alarme "+alarme);
		Collection listaEMail = alarme.getListaEMail();
		try
		{
			String sql = "SELECT DISTINCT IDT_ENDERECO_ELETRONICO " +
					       "FROM TBL_ALR_ALARME_EMAIL " +
				          "WHERE ID_ALARME = ?";
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pstmt = conexao.prepareStatement(sql);
			pstmt.setString(1,alarme.getIdAlarme());
			
			rs = pstmt.executeQuery();
			while (rs.next())
				listaEMail.add(rs.getString("IDT_ENDERECO_ELETRONICO"));
		}
		catch(Exception e)
		{
			logger.error("Erro na pesquisa da lista de E-Mails do Alarme "+alarme.getIdAlarme()+". Erro:"+e);
			listaEMail.clear();
		}
		finally
		{
			try
			{
				rs.close();
				pstmt.close();
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch (Exception e)
			{
			}
		}
		return listaEMail;
	}
	
	/**
	 * Metodo....:getListaAssinantes
	 * Descricao.:Retorna a lista de assinantes que serao utilizados para receber as informacoes
	 *            de status do alarme via SMS
	 * @param alarme 		- Alarme a ser pesquisado lista de enderecos para aviso
	 * @return Collection	- Colecao contendo em cada elemento um msisdn valido para envio do SMS
	 */
	public synchronized Collection getListaAssinantes(Alarme alarme)
	{
		Connection conexao = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		logger.debug("Buscando lista de assinantes para o alarme "+alarme);
		// Utiliza a propria lista definido no objeto para incluir os valores. Para isso
		// a implementacao da colecao deve identificar valores repetidos para que nao haja
		// duplicidade de valores
		Collection listaAssinantes = alarme.getListaAssinantes();
		try
		{
			String sql = "SELECT DISTINCT IDT_MSISDN " +
					       "FROM TBL_ALR_ALARME_ASSINANTES " +
				          "WHERE ID_ALARME = ?";
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pstmt = conexao.prepareStatement(sql);
			pstmt.setString(1,alarme.getIdAlarme());
			
			rs = pstmt.executeQuery();
			while (rs.next())
				listaAssinantes.add(rs.getString("IDT_MSISDN"));
			
			
		}
		catch(Exception e)
		{
			logger.error("Erro na pesquisa da lista de Assinantes do Alarme "+alarme.getIdAlarme()+". Erro:"+e);
			listaAssinantes.clear();
		}
		finally
		{
			try
			{
				rs.close();
				pstmt.close();
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch(Exception e)
			{
			}
		}
		return listaAssinantes;
	}
	
	public void limparHistorico(Alarme alarme, java.util.Date data) throws SQLException
	{
		Connection conexao = null;
		PreparedStatement pStatement = null;
		try
		{
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pStatement = conexao.prepareStatement(SQL_APAGA_HISTORICO);
			pStatement.setString(1, alarme.getIdAlarme());
			pStatement.setDate(2, new java.sql.Date(data.getTime()));
			pStatement.executeUpdate();
			conexao.commit();
		}
		catch (Exception e)
		{
		}
		finally
		{
			pStatement.close();
			try
			{
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch (Exception e)
			{
			}
		}
	}
}
