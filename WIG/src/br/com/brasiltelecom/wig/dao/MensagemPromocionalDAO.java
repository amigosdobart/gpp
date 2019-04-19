package br.com.brasiltelecom.wig.dao;

import br.com.brasiltelecom.wig.entity.MensagemPromocional;

import java.util.HashMap;
import java.util.Calendar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * @author Joao Carlos
 * Data..:31/05/2005
 *
 */
public class MensagemPromocionalDAO
{
	private static 	MensagemPromocionalDAO 	instance;
	private 		HashMap 				poolMensagens;
	private	static 	String 					sqlPesquisa =  "SELECT CO_PROMOCAO,CO_SERVICO,CO_CONTEUDO,DS_MENSAGEM " +
														     "FROM WIGC_MENSAGEM_PROMOCIONAL " +
														    "WHERE CO_PROMOCAO = ? " +
														    "AND CO_SERVICO = ? " +
														    "AND CO_CONTEUDO = ?";

	private static String					sqlSenha	=  "SELECT CO_SENHA " +
	                                                         "FROM WIGC_MENSAGEM_SENHA " +
	                                                        "WHERE CO_SERVICO = ? " +
	                                                          "AND CO_CONTEUDO = ? " +
	                                                          "AND CO_PROMOCAO = ? " +
	                                                          "AND NU_MSISDN IS NULL";
	
	private static String					sqlUpdSenha	=  "UPDATE WIGC_MENSAGEM_SENHA " +
	                                                          "SET NU_MSISDN = ?, DT_REQUISICAO = ? " +
	                                                        "WHERE CO_SERVICO = ? " +
	                                                          "AND CO_CONTEUDO = ? " +
	                                                          "AND CO_PROMOCAO = ? " +
	                                                          "AND CO_SENHA = ?";

	private MensagemPromocionalDAO()
	{
		poolMensagens = new HashMap();
	}
	
	public static MensagemPromocionalDAO getInstance()
	{
		if (instance == null)
			instance = new MensagemPromocionalDAO();
		
		return instance;
	}
	
	/**
	 * Metodo....:limpaCache
	 * Descricao.:Remove todos os elementos do cache
	 */
	public void limpaCache()
	{
		poolMensagens.clear();
	}

	/**
	 * Metodo....:getMensagemPromocional
	 * Descricao.:Retorna o objeto MensagemPromocional baseado nos valores lidos no resultSet atual
	 * @param rs		- ResultSet de dados contendo as informacoes da mensagem promocional
	 * @return Servico	- Objeto MensagemPromocional com os dados preenchidos
	 * @throws SQLException
	 */
	private MensagemPromocional getMensagemPromocional(ResultSet rs) throws SQLException
	{
		MensagemPromocional msg = new MensagemPromocional(rs.getInt("CO_PROMOCAO")
				                                         ,rs.getInt("CO_SERVICO")
				                                         ,rs.getInt("CO_CONTEUDO"));
		msg.setMensagemPromocional(rs.getString("DS_MENSAGEM"));

		// Armazena a instancia do objeto que foi alterada ou criada no 
		// cache de dados da classe DAO
		poolMensagens.put(new Integer(msg.getCodPromocao()),msg);
		return msg;
	}

	/**
	 * Metodo....:findByCodigo
	 * Descricao.:Realiza a pesquisa da mensagem promocional pelo codigo da promocao
	 * @param codigoPromocao- Codigo da promocao a ser pesquisado
	 * @param con			- Conexao de banco de dados a ser utilizada para a pesquisa
	 * @return MensagemPromocional - Objeto MensgemPromocional encontrado para o codigo passado como parametro
	 * @throws SQLException
	 */
	public synchronized MensagemPromocional findByCodigo(int codPromocao,int codServico,int codConteudo,Connection con) throws SQLException
	{
		// Inicializa variavel que sera o objeto Servico retornado 
		MensagemPromocional msg = (MensagemPromocional)poolMensagens.get(new Integer(codPromocao));
		if (msg == null)
		{
			// Prepara e realiza a consulta. Caso o registro nao exista entao
			// a referencia nula sera retornada senao o valor sera preenchido
			// pelo metodo getServico
			PreparedStatement pstmt = null;
			ResultSet 		  rs    = null;
			try
			{
				pstmt = con.prepareStatement(sqlPesquisa);
				pstmt.setInt(1,codPromocao);
				pstmt.setInt(2,codServico);
				pstmt.setInt(3,codConteudo);
				rs = pstmt.executeQuery();
				if (rs.next())
					msg = getMensagemPromocional(rs);
			}
			catch(SQLException e){
				throw e;
			}
			finally{
				try{// Fecha os objetos de consulta
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
				}catch(Exception e){};
			}
		}
		return msg;
	}
	
	/**
	 * Metodo....:getSenhaPromocional
	 * Descricao.:Retorna a proxima senha promocional disponivel (caso existir) 
	 * @param msgPromo	- Mensagem promocional a ser pesquisada a senha
	 * @param msisdn    - Msisdn do assinante requisitando a senha
	 * @param con		- Conexao de banco de dados utilizada
	 * @return String	- Senha para a mensagem promocional
	 * @throws SQLException
	 */
	public synchronized String getSenhaPromocional(MensagemPromocional msgPromo,String msisdn, Connection con) throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet 		  rs    = null;
		String 			  senha = null;
		try
		{
			pstmt = con.prepareStatement(sqlSenha);
			pstmt.setInt(1,msgPromo.getCodServico());
			pstmt.setInt(2,msgPromo.getCodConteudo());
			pstmt.setInt(3,msgPromo.getCodPromocao());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				// Caso a pesquisa de senha retornou que existe uma proxima disponive
				// entao atualiza o registro indicando qual o assinante que requisitou
				// antes de enviar a senha por sms
				senha = rs.getString("CO_SENHA");
				// Marca agora a utilizacao da senha pelo assinante
				PreparedStatement pstmtUpd = con.prepareStatement(sqlUpdSenha);
				pstmtUpd.setString		(1,msisdn);
				pstmtUpd.setTimestamp	(2,new Timestamp(Calendar.getInstance().getTimeInMillis()));
				pstmtUpd.setInt   		(3,msgPromo.getCodServico());
				pstmtUpd.setInt   		(4,msgPromo.getCodConteudo());
				pstmtUpd.setInt	  		(5,msgPromo.getCodPromocao());
				pstmtUpd.setString		(6,senha);
				
				pstmtUpd.executeUpdate();
				pstmtUpd.close();
			}
		}
		catch(SQLException e){
			throw e;
		}
		finally{
			try{// Fecha os objetos de consulta
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
			}catch(Exception e){};
		}

		return senha;
	}
}
