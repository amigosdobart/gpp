package br.com.brasiltelecom.wig.dao;

import br.com.brasiltelecom.wig.entity.Servico;
import br.com.brasiltelecom.wig.entity.Conteudo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Joao Carlos
 * Data..: 31/05/2005
 *
 */
public class ConteudoDAO
{
	private static 	ConteudoDAO 	instance;
	private 		HashMap 		poolConteudo;
	private static  String			sqlAgrConteudo =  "SELECT CO_CONTEUDO_AGRUPADO " +
														"FROM wigc_agrupamento_conteudo " +
													   "WHERE co_conteudo = ? " +
													  "ORDER BY no_posicao";

	private static  String			sqlPesquisa = "SELECT C.CO_CONTEUDO" +
													    ",C.DS_CONTEUDO" +
													    ",C.CO_TIPO_RESPOSTA" +
													    ",C.CO_RESPOSTA" +
														",C.IN_BLOQUEIO_CONCORRENTE" +
														",C.IN_REGISTRA_ACESSO " +
														",C.NU_BILLING_CODE " +
													    ",V.DS_CLASSE_FILTRO " +
													"FROM wigc_conteudo c " +
													    ",wigc_validadores_conteudo v " +
												   "WHERE v.co_conteudo (+)  = c.co_conteudo " +
													 "AND c.co_conteudo      = ? " +
												  "ORDER BY c.co_conteudo, v.no_prioridade";
	
	private static	String			sqlInsAcesso = 	"insert into wigc_acessos_conteudo "+
													"(co_conteudo,nu_msisdn,nu_iccid,dt_acesso) "+
													"values (?,?,?,sysdate)";

	private ConteudoDAO()
	{
		poolConteudo = new HashMap();
	}
	
	public static ConteudoDAO getInstance()
	{
		if (instance == null)
			instance = new ConteudoDAO();
		
		return instance;
	}

	/**
	 * Metodo....:limpaCache
	 * Descricao.:Remove todos os elementos do cache
	 */
	public void limpaCache()
	{
		poolConteudo.clear();
	}

	/**
	 * Metodo....:getConteudo
	 * Descricao.:Retorna o objeto Conteudo baseado no resultSet passado como parametro
	 * @param rs		- ResultSet de dados de onde sera retirado as informacoes do Conteudo
	 * @param servico	- Objeto Servico relacionado ao Conteudo
	 * @return Conteudo	- Objeto Conteudo preenchido
	 * @throws SQLException
	 */
	private Conteudo getConteudo(ResultSet rs, Servico servico) throws SQLException
	{
		Conteudo conteudo = new Conteudo(servico,rs.getInt("CO_CONTEUDO"));
		conteudo.setDescricaoConteudo	(rs.getString("DS_CONTEUDO"));
		conteudo.setBloqueiaConcorrente	(rs.getString("IN_BLOQUEIO_CONCORRENTE").equals("N") ? false : true );
		conteudo.setRegistraAcesso		(rs.getInt("IN_REGISTRA_ACESSO")==1 ? true : false );
		conteudo.setBillingCode			(rs.getInt("NU_BILLING_CODE"));

		// Armazena a instancia do objeto que foi alterada ou criada no 
		// cache de dados da classe DAO
		poolConteudo.put(new Integer(conteudo.getCodigoConteudo()),conteudo);
		return conteudo;
	}
	
	/**
	 * Metodo....:defineAgrupamentoConteudo
	 * Descricao.:Define os objetos conteudos contidos no agrupamento
	 * @param conteudo		- Objeto conteudo que ira possuir o agrupamento
	 * @param servico		- Objeto servico correspondente aos conteudos
	 * @param con			- Conexao de banco de dados a ser utilizada para a pesquisa
	 * @throws SQLException
	 */
	private void defineAgrupamentoConteudo(Conteudo conteudo, Servico servico, Connection con) throws SQLException
	{
		PreparedStatement 	pstmt 	= null;
		ResultSet 			rs		= null;
		ArrayList 			list 	= new ArrayList();
		try
		{
			pstmt = con.prepareStatement(sqlAgrConteudo);
			pstmt.setInt(1,conteudo.getCodigoConteudo());
			rs = pstmt.executeQuery();
			// Executa a consulta para identificar quais os codigos de conteudo sao agrupados por este
			// conteudo sendo processado. Para cada registro adiciona o codigo do conteudo em uma lista
			// para posteriormente o objeto ser adicionado a lista agregada
			while (rs.next())
				list.add(new Integer(rs.getInt("CO_CONTEUDO_AGRUPADO")));
		}
		catch(SQLException e){
			throw e;
		}
		finally{	// Fecha os objetos de banco de dados
			try{
				if (rs != null)    rs.close();
				if (pstmt != null) pstmt.close();
			}catch(Exception e){};
		}
		// Processa a lista de valores lidos do resultSet instanciando um objeto
		// conteudo para cada valor iteragido sendo adicionado tal objeto na 
		// lista de conteudos agreados do objeto sendo pesquisado
		for (Iterator i=list.iterator(); i.hasNext();)
		{
			int codConteudoAgr = ((Integer)i.next()).intValue();
			Conteudo conteudoAgrupado = findByCodigo(codConteudoAgr,servico,con);
			conteudo.addConteudo(conteudoAgrupado);
		}
	}

	/**
	 * Metodo....:defineValidador
	 * Descricao.:Este metodo define um validador definido em registro na tabela
	 *            instanciando a classe que ira ser o validador em tempo de execucao
	 *            armazenando-a em uma lista no objeto Conteudo 
	 * @param conteudo		- Objeto conteudo no qual sera inserido o validador
	 * @param rs			- Resultset contendo as informacoes do validador
	 * @throws SQLException
	 */
	private void defineValidador(Conteudo conteudo, ResultSet rs) throws SQLException
	{
		if (conteudo != null)
		{
			// Busca no registro qual o nome da classe que sera utilizada como filtro
			String nomeClasse = rs.getString("DS_CLASSE_FILTRO");
			if (nomeClasse != null)
			{
				try
				{
					// Faz a instanciacao em tempo de execucao da classe e a armazena
					// em uma lista dentro do objeto conteudo que sera utilizado posteriormente
					// para a validacao dos filtros cadastrados
					Class 	cls = Class.forName(nomeClasse);
					Object 	obj = cls.newInstance();
					if ( obj instanceof ValidadorConteudo )
						conteudo.addValidador((ValidadorConteudo)obj);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Metodo....:findByCodigo
	 * Descricao.:Realiza a pesquisa do conteudo pelo codigo do mesmo
	 * @param codigoConteudo- Codigo do conteudo
	 * @param con			- Conexao de banco de dados a ser utilizada para a pesquisa
	 * @param Servico		- Objeto servico associado ao conteudo
	 * @return Conteudo		- Objeto Conteudo encontrado para o codigo passado como parametro
	 * @throws SQLException
	 */
	public synchronized Conteudo findByCodigo(int codigoConteudo, Servico servico, Connection con) throws SQLException
	{
		// Busca no pool de objetos mantidos pela classe se pela chave do 
		// conteudo encontra o objeto no cache. Caso nao encontre entao uma
		// nova instancia eh criada
		Conteudo conteudo = (Conteudo)poolConteudo.get(new Integer(codigoConteudo));
		if (conteudo == null)
		{
			int codResposta 	= 0;
			int codTipoResposta = 0;
			// Prepara e realiza a consulta. Caso o registro nao exista entao
			// a referencia nula sera retornada senao o valor sera preenchido
			// pelo metodo getConteudo
			PreparedStatement pstmt = null;
			ResultSet 		  rs    = null;
			try
			{
				pstmt = con.prepareStatement(sqlPesquisa);
				pstmt.setInt(1,codigoConteudo);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					// Se o resultSet estiver no primeiro registro entao cria a instancia
					// do objeto Conteudo senao simplesmente atualiza a lista contendo os
					// filtros cadastrados
					if (rs.isFirst())
					{
						conteudo 		= getConteudo(rs,servico);
						codResposta 	= rs.getInt("CO_RESPOSTA");
						codTipoResposta = rs.getInt("CO_TIPO_RESPOSTA");
					}
					defineValidador(conteudo,rs);
				}
			}
			catch(SQLException e){
				throw e;
			}
			finally{// Fecha os objetos de consulta
				try{
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
				}catch(Exception e){};
			}
			// Busca objeto contendo as informacoes de resposta do Conteudo
			// buscando tambem definicoes de filtros de resposta para o conteudo
			if (conteudo != null)
			{
				conteudo.setResposta(RespostaDAO.getInstance().findByCodigo(codResposta,codTipoResposta,con));
				conteudo.setRespostasFiltros(RespostaFiltroDAO.getInstance().findByConteudo(conteudo,con));
			}

			// Define a lista de conteudos que este conteudo ira agrupar
			// caso o tipo de resposta do mesmo identificar que este devera
			// agrupar os conteudos para a criacao do WML
			if (conteudo != null && conteudo.getResposta().agrupaConteudo())
				defineAgrupamentoConteudo(conteudo,servico,con);
		}
		return conteudo;
	}
	
	/**
	 * Metodo....:insereAcesso
	 * Descricao.:Insere a identificacao do acesso ao conteudo
	 * @param conteudo - Conteudo a ser quantificado o acesso
	 * @param msisdn   - Msisdn do assinante que acessou o conteudo
	 * @param iccid    - ICCID utilizado
	 * @param conn     - Conexao de banco de dados que sera utilizada
	 */
	public synchronized void insereAcesso(Conteudo conteudo, String msisdn, String iccid, Connection conn)
	{
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sqlInsAcesso);
			pstmt.setInt	(1,conteudo.getCodigoConteudo());
			pstmt.setString	(2,msisdn);
			pstmt.setString	(3,iccid);
			
			pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			try{
				if (pstmt != null) pstmt.close();
			}catch(Exception e){};
		}
	}
}