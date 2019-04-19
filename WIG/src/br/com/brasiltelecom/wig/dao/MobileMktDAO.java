package br.com.brasiltelecom.wig.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.entity.MobileMktPergunta;
import br.com.brasiltelecom.wig.entity.MobileMktPesquisa;
import br.com.brasiltelecom.wig.entity.MobileMktResposta;
import br.com.brasiltelecom.wig.entity.MobileMktResultado;
import br.com.brasiltelecom.wig.entity.MobileMktQuestionario;

public class MobileMktDAO
{
	private static MobileMktDAO instance;
	private HashMap poolDados;
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final String SQL_PESQ = 
						  "select p.id_pesquisa " +
							    ",p.ds_pesquisa " +
							    ",cursor(select id_questionario " +
							                  ",ds_questionario " +
							                  ",ds_texto_explicativo " +
							                  ",ds_mensagem " +
							                  ",cursor(  select id_pergunta " +
							                                  ",ds_pergunta " +
							                                  ",no_titulo " +
							                                  ",cursor ( select id_resposta " +
							                                                  ",ds_resposta " +
							                                                  ",no_referencia_card " +
							                                                  ",id_questionario_referencia " +
							                                              "from mbmkt_resposta r " +
							                                             "where r.id_pesquisa     = pe.id_pesquisa " +
							                                               "and r.id_questionario = pe.id_questionario " +
							                                               "and r.id_pergunta     = pe.id_pergunta " +
							                                             "order by id_resposta " +
							                                           ") respostas " +
							                              "from mbmkt_pergunta pe " +
							                             "where pe.id_pesquisa     = q.id_pesquisa " +
							                               "and pe.id_questionario = q.id_questionario " +
							                            "order by id_pergunta " +
							                          ") perguntas " +
							              "from mbmkt_questionario q " +
							             "where q.id_pesquisa = p.id_pesquisa " +
							            "order by id_questionario " +
							           ") questionarios " +
							 "from mbmkt_pesquisa p " +
							"where p.id_pesquisa = ? ";
	
	private static final String INS_PESQ_RESULTADO = "insert into mbmkt_resultado " +
	                                                 "(id_pesquisa,id_questionario,id_pergunta," +
	                                                  "id_resposta,nu_msisdn,dt_resposta) " +
	                                                  "values (?,?,?,?,?,?)";
	
	private MobileMktDAO()
	{
		poolDados = new HashMap();
	}
	
	/**
	 * Metodo....:limpaCache
	 * Descricao.:Remove todos os elementos do cache
	 */
	public void limpaCache()
	{
		poolDados.clear();
	}
	
	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia desta classe (singleton)
	 * @return MobileMktDAO - Singleton da classe
	 */
	public static MobileMktDAO getInstance()
	{
		if (instance == null)
			instance = new MobileMktDAO();
		
		return instance;
	}
	
	/**
	 * Metodo....:
	 * Descricao.:
	 * @param mbmktResultado
	 * @param dataSource
	 */
	public void insereResultado(MobileMktResultado resultado, DataSource dataSource)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		try
		{
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(INS_PESQ_RESULTADO);
			// Para cada resposta existente no resultado realiza
			// a insercao no banco de dados. Portanto faz uma iteracao
			// nestas respostas sendo que o bind eh modificado para
			// cada insercao.
			for (Iterator i = resultado.getPerguntasRespostas().entrySet().iterator(); i.hasNext();)
			{
				Entry entry = (Entry)i.next();
				// Realiza o bind dos parametros do comando insert
				pstmt.setInt		(1, resultado.getIdPesquisa());
				pstmt.setInt		(2, resultado.getIdQuestionario());
				pstmt.setInt		(3, ((Integer)entry.getKey()).intValue());
				pstmt.setInt		(4, ((Integer)entry.getValue()).intValue());
				pstmt.setString		(5, resultado.getMsisdn());
				pstmt.setTimestamp	(6, new Timestamp(resultado.getDataRespostaPesquisa().getTime()));
				
				pstmt.executeUpdate();
				// Limpa os dados feitos no bind para novo bind com a nova pergunta
				pstmt.clearParameters();
			}
			con.commit();
		}
		catch(Exception e)
		{
			logger.error("Erro ao inserir resultado da pesquisa de marketing id:"+ resultado.getIdPesquisa()+
					     " respondida pelo assinante:"+resultado.getMsisdn(),e);
			try{
				if (con != null)
					con.rollback();
			}catch(Exception se){};
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close();
				
				if (con != null)
					con.close();
			}
			catch(Exception e){};
		}
	}
	
	/**
	 * Metodo....:findQuestionarioByID
	 * Descricao.:Encontra o questionario desejado
	 * @param idPesquisa      - Id da pesquisa a ser utilizado
	 * @param idQuestionario  - Id do questionario
	 * @param datasource	  - Datasource de banco de dados
	 * @return MobileMktQuestionario - Objeto Questionario
	 */
	public MobileMktQuestionario findQuestionarioByID(int idPesquisa, int idQuestionario, DataSource datasource)
	{
		// O mapeamento em memoria mantem os dados baseados na pesquisa, independente do
		// questionario. Somente o questionario informado serah retornado.
		MobileMktPesquisa pesquisa = (MobileMktPesquisa)poolDados.get(new Integer(idPesquisa));
		// Se a pesquisa nao estiver na memoria entao acessa o banco de dados
		// cria o objeto da pesquisa e o armazena no pool.
		if (pesquisa == null)
		{
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try
			{
				con = datasource.getConnection();
				pstmt = con.prepareStatement(SQL_PESQ);
				pstmt.setInt(1, idPesquisa);
				rs =  pstmt.executeQuery();
				if (rs.next())
				{
					// Caso a pesquisa de marketing sendo consultada exista
					// entao busca as informacoes jah inseridos nos objetos
					// correspondentes e adiciona este objeto pesquisa no
					// pool de dados para agilizar proximas pesquisas.
					// 
					// OBS: Caso os dados da pesquisa sejam alterados, entao
					//      o limpaCache devera ser executado
					pesquisa = getPesquisa(rs);
					poolDados.put(new Integer(idPesquisa),pesquisa);
				}
			}
			catch(Exception e)
			{
				logger.error("Erro ao executar consulta da pesquisa de marketing. Erro:",e);
			}
			finally
			{
				try
				{
					if (rs != null)
						rs.close();
					
					if (pstmt != null)
						pstmt.close();
					
					if (con != null)
						con.close();
				}
				catch(Exception e){};
			}
		}
		return pesquisa != null ? pesquisa.getQuestionarioById(idQuestionario) : null;
	}
	
	/**
	 * Metodo....:getPesquisa
	 * Descricao.:Retorna o objeto Pesquisa baseado nas informacoes
	 *            existentes no resultSet de banco de dados
	 * @param rs - ResultSet de banco de dados
	 * @return MobileMktPesquisa - Objeto pesquisa de marketing
	 * @throws Exception
	 */
	private MobileMktPesquisa getPesquisa(ResultSet rs) throws Exception
	{
		MobileMktPesquisa pesquisa = new MobileMktPesquisa();
		pesquisa.setId					(rs.getInt("id_pesquisa"));
		pesquisa.setDescricaoPesquisa	(rs.getString("ds_pesquisa"));
		
		ResultSet questionarios = (ResultSet)rs.getObject("questionarios");
		while (questionarios.next())
			pesquisa.addQuestionario(getQuestionario(questionarios, pesquisa));
		
		return pesquisa;
	}
	
	/**
	 * Metodo....:getQuestionario
	 * Descricao.:Retorna o objeto Questionario a partir do ResultSet de banco de dados
	 * @param rs
	 * @param pesquisa
	 * @return
	 * @throws Exception
	 */
	private MobileMktQuestionario getQuestionario(ResultSet rs, MobileMktPesquisa pesquisa) throws Exception
	{
		MobileMktQuestionario questionario = new MobileMktQuestionario();
		questionario.setId					(rs.getInt("id_questionario"));
		questionario.setMensagem			(rs.getString("ds_mensagem"));
		questionario.setTextoExplicativo	(rs.getString("ds_texto_explicativo"));
		questionario.setMobileMktPesquisa	(pesquisa);
		
		ResultSet perguntas = (ResultSet)rs.getObject("perguntas");
		while (perguntas.next())
			questionario.addPergunta(getPergunta(perguntas, questionario));
		
		return questionario;
	}
	
	/**
	 * Metodo....:getPergunta
	 * Descricao.:Retorna o objeto Pergunta a partir do ResultSet de banco de dados
	 * @param rs
	 * @param questionario
	 * @return
	 * @throws Exception
	 */
	private MobileMktPergunta getPergunta(ResultSet rs, MobileMktQuestionario questionario) throws Exception
	{
		MobileMktPergunta pergunta = new MobileMktPergunta();
		pergunta.setId					 (rs.getInt("id_pergunta"));
		pergunta.setDescricaoPergunta	 (rs.getString("ds_pergunta"));
		pergunta.setTitulo				 (rs.getString("no_titulo"));
		pergunta.setMobileMktQuestionario(questionario);
		
		ResultSet respostas = (ResultSet)rs.getObject("respostas");
		while (respostas.next())
			pergunta.addResposta(getResposta(respostas,pergunta));
		
		return pergunta;
	}
	
	/**
	 * Metodo....:getResposta
	 * Descricao.:Retorna o objeto Resposta a partir do result set de banco de dados
	 * @param rs
	 * @param pergunta
	 * @return
	 * @throws Exception
	 */
	private MobileMktResposta getResposta(ResultSet rs, MobileMktPergunta pergunta) throws Exception
	{
		MobileMktResposta resposta = new MobileMktResposta();
		resposta.setId						(rs.getInt("id_resposta"));
		resposta.setDescricaoResposta		(rs.getString("ds_resposta"));
		resposta.setCardReference			(rs.getString("no_referencia_card"));
		resposta.setIdQuestionarioReferencia(rs.getInt("id_questionario_referencia"));
		resposta.setMobileMktPergunta		(pergunta);
		
		return resposta;
	}
}
