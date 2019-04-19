package com.brt.gppAlarmes.dao;

import com.brt.gppAlarmes.entity.Evento;
import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.conexoes.GerentePoolConexoes;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * Essa classe realiza a persistencia de dados relativos ao objeto Evento
 *  
 * @author Joao Carlos
 * Data..: 18-Mar-2005
 *
 */
public class EventoDAO
{
	private static EventoDAO 	instance;
	private Logger				logger;
	
	/**
	 * Metodo....:EventoDAO
	 * Descricao.:Construtor do objeto Singleton. Realiza a conexao com o banco de dados
	 *            que sera utilizada durante todo o seu ciclo de vida
	 *
	 */
	private EventoDAO() throws Exception
	{ 
		logger = Logger.getLogger("Alarmes");
	}

	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia dessa classe (singleton)
	 * @return EventoDAO - Singleton para acesso as informacoes dos Eventos de Alarmes
	 */
	public static EventoDAO getInstance() throws Exception
	{
		if (instance==null)
			instance = new EventoDAO();
		return instance;
	}

	/**
	 * Metodo....:close
	 * Descricao.:Define a instancia como nulo
	 *
	 */
	public void close()
	{
		instance = null;
	}
	
	/**
	 * Metodo....:getEvento
	 * Descricao.:Retorna um objeto Evento populado com as informacoes obtidas no result set
	 *            de pesquisa no banco de dados
	 * @param rs		- ResultSet de pesquisa do banco de dados
	 * @param alarme	- Alarme na qual o evento pertence
	 * @return Evento	- Objeto Evento populado
	 * @throws SQLException
	 */
	public Evento getEvento(ResultSet rs, Alarme alarme) throws SQLException
	{
		Evento evt = new Evento();
		evt.setAlarme(alarme);
		evt.setDataExecucao	((java.util.Date)rs.getTimestamp("DAT_EXECUCAO"));
		evt.setCodigoRetorno(rs.getInt		("IDT_CODIGO_RETORNO")	);
		evt.setValorContador(rs.getDouble	("VLR_CONTADOR")		);
		return evt;
	}
	
	/**
	 * Metodo....:findByAlarme
	 * Descricao.:Encontra as informacoes de eventos para um determinado Alarme
	 * @param alarme 		- Alarme a ser utilizado para a pesquisa
	 * @return Collection	- Lista de objetos Eventos relacionados ao alarme desejado
	 */
	public Collection findByAlarme(Alarme alarme)
	{
		Connection conexao = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection listaEventos = new LinkedList();
		String sql = "SELECT DAT_EXECUCAO,IDT_CODIGO_RETORNO,ID_ALARME " +
				       "FROM TBL_ALR_EVENTOS_ALARME A" +
					  "WHERE ID_ALARME = ? ";
		try
		{
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pstmt = conexao.prepareStatement(sql);
			pstmt.setString(1,alarme.getIdAlarme());
			rs = pstmt.executeQuery();
			while (rs.next())
				listaEventos.add(getEvento(rs,alarme));
		}
		catch(Exception e)
		{
			// Em caso de erro a lista retornada sera vazia
			listaEventos.clear();
			logger.error("Erro ao pesquisar eventos para o alarme "+alarme+". Erro:"+e);
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
		return listaEventos;
	}

	/**
	 * Metodo....:add
	 * Descricao.:Insere um evento na base de dados
	 * @param evento - Evento a ser inserido
	 * @return boolean - Indica se conseguiu inserir ou nao
	 */
	public boolean add(Evento evento)
	{
		if (evento == null)
			throw new IllegalArgumentException("Nao e possivel inserir um Evento Nulo.");
		Connection conexao = null;
		PreparedStatement pstmt = null;
		boolean inseriu=false;
		String insert = "INSERT INTO TBL_ALR_EVENTOS_ALARME " +
		                "(ID_ALARME,DAT_EXECUCAO,IDT_CODIGO_RETORNO,VLR_CONTADOR) VALUES (?,?,?,?)";
		try
		{
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pstmt = conexao.prepareStatement(insert);
			pstmt.setString		(1,evento.getAlarme().getIdAlarme());
			pstmt.setTimestamp	(2,new Timestamp(evento.getDataExecucao().getTime()));
			pstmt.setInt		(3,evento.getCodigoRetorno());
			pstmt.setDouble		(4,evento.getValorContador());
			
			inseriu = (pstmt.executeUpdate() == 1);
			conexao.commit();
			
		}
		catch(Exception e)
		{
			logger.error("Erro ao inserir evento "+evento+". Erro:"+e);
			inseriu=false;
		}
		finally
		{
			try
			{
				pstmt.close();
				GerentePoolConexoes.getInstance().releaseConexao(conexao);
			}
			catch (Exception e)
			{
			}
		}
		return inseriu;
	}

	/**
	 * Metodo....:getUltimaExecucao
	 * Descricao.:Retorna a ultima execucao (Evento) de um determinado alarme
	 * @param alarme 		- Alarme a ser utilizado na pesquisa
	 * @param dataInicio	- Data de inicio para pesquisa, nulo se independente da data
	 * @return Evento		- Objeto Evento com os resultados da ultima execucao do alarme
	 */
	public Evento getUltimaExecucao(Alarme alarme)
	{
		Connection conexao = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Evento evt = null;
		String sql = 	"select id_alarme,dat_execucao,idt_codigo_retorno,vlr_contador " +
		                  "from tbl_alr_eventos_alarme " +
		                 "where (id_alarme,dat_execucao)=(select id_alarme,max(dat_execucao) " +
		                                                   "from tbl_alr_eventos_alarme " +
		                                                  "where id_alarme=? " +
		                                                 "group by id_alarme)";
		try
		{
			conexao = GerentePoolConexoes.getInstance().getConexao();
			pstmt = conexao.prepareStatement(sql);
			pstmt.setString(1,alarme.getIdAlarme());
			rs = pstmt.executeQuery();
			if (rs.next())
				evt = getEvento(rs,alarme);
		}
		catch(Exception e)
		{
			logger.error("Erro ao pesquisar ultima execucao do alarme "+alarme+". Erro:"+e);
			evt = null;
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
		return evt;
	}
}
