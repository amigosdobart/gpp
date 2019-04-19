package br.com.brasiltelecom.wig.dao;

import br.com.brasiltelecom.wig.entity.Conteudo;
import br.com.brasiltelecom.wig.entity.RespostaFiltro;
import br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class RespostaFiltroDAO
{
	private	static	RespostaFiltroDAO	instance;
	private 		HashMap 			poolResposta;
	private static 	String				sqlConteudoResposta =   "select ds_classe_filtro,in_aplica_paralelo,tp_resposta_filtro,nu_ordem " +
																  "from wigc_filtros_conteudo " +
																 "where co_conteudo = ? " +
																   "and co_resposta = ? ";
	
	private static	String				sqlConteudo			=   "select distinct co_resposta,nu_ordem " +
																  "from wigc_filtros_conteudo " +
																 "where co_conteudo = ? " +
																"order by nu_ordem,co_resposta ";
	
	private RespostaFiltroDAO()
	{
		poolResposta = new HashMap();
	}
	
	public static RespostaFiltroDAO getInstance()
	{
		if (instance == null)
			instance = new RespostaFiltroDAO();
		
		return instance;
	}
	
	/**
	 * Metodo....:limpaCache
	 * Descricao.:Remove todos os elementos do cache
	 */
	public void limpaCache()
	{
		poolResposta.clear();
	}
	
	/**
	 * Metodo....:findByConteudo
	 * Descricao.:Retorna para um determinado conteudo uma lista de objetos RespostaFiltro
	 *            que deverao ser utilizados
	 * @param conteudo 	- Conteudo desejado
	 * @param con		- Conexao de banco de dados a ser utilizada
	 * @return Collection - Lista de respostas a serem tratadas na resposta para o assinante
	 * @throws SQLException
	 */
	public Collection findByConteudo(Conteudo conteudo, Connection con) throws SQLException
	{
		// Na colecao de respostas utiliza o LinkedHashSet pois a ordem
		// na qual os objetos foram inseridos deve ser a ordem que os
		// objetos devem ser lidos no Iterator para montar o anexo de
		// resposta ao aplicar os filtros. Assim a ordem eh definida
		// no banco de dados pelo usuario ao inserir os filtros desta resposta
		// anexa
		Collection listaRespostas = new LinkedHashSet();
		Collection listaCodigos   = new ArrayList();
		// Executa o comando para buscar todos os valores possiveis de resposta
		// do conteudo desejado. Para cada valor existente no resultset este eh
		// adicionado a uma collection para depois ser utilizado o metodo que
		// pesquisa para da resposta os filtros existentes
		PreparedStatement pstmt = null;
		ResultSet 		  rs    = null;
		try
		{
			pstmt = con.prepareStatement(sqlConteudo);
			pstmt.setInt(1,conteudo.getCodigoConteudo());
			rs = pstmt.executeQuery();
			while (rs.next())
				listaCodigos.add(new Integer(rs.getInt(1)));
		}
		catch(SQLException e){
			throw e;
		}
		finally{
			try{
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
			}catch(Exception e){};
		}
		// Realiza a iteracao nos codigos de resposta para o conteudo
		// e para cada um realiza a pesquisa criando o objeto RespostaFiltro
		// que irah armazenar a associacao entre conteudo X resposta X filtros
		// que serao utilizados para modificar a resposta final
		for (Iterator i=listaCodigos.iterator(); i.hasNext();)
		{
			Integer codResposta = (Integer)i.next();
			RespostaFiltro respFiltro = findByConteudoResposta(conteudo,codResposta.intValue(),RespostaDAO.RESPOSTA_TIPO_WML,con);
			if (respFiltro != null)
				listaRespostas.add(respFiltro);
		}
		return listaRespostas;
	}

	/**
	 * Metodo....:findByConteudoResposta
	 * Descricao.:Retorna um objeto RespostaFiltro para o determinado conteudo e resposta. Todos os filtros
	 *            aplicaveis a essa combinacao ficam sob a gerencia desse objeto
	 * @param Conteudo			- Conteudo a ser utilizado para a pesquisa
	 * @param codigoResposta	- Codigo da resposta desejado
	 * @param con				- Conexao de banco de dados a ser utilizada
	 * @return	RespostaFiltro	- Objeto contendo a relacao resposta e filtros a serem aplicaveis para um determinado
	 * 							  conteudo
	 * @throws SQLException
	 */
	public RespostaFiltro findByConteudoResposta(Conteudo conteudo,
			                                     int codResposta,
			                                     int codTipoResposta,
			                                     Connection con) throws SQLException
	{
		// Verifica se o objeto jah existe no cache de dados, senao cria-o
		RespostaFiltro respFiltro = (RespostaFiltro)poolResposta.get(new Integer(conteudo.getCodigoConteudo()));
		if (respFiltro==null)
		{
			PreparedStatement pstmt = null;
			ResultSet 		  rs    = null;
			try
			{
				pstmt = con.prepareStatement(sqlConteudoResposta);
				pstmt.setInt(1,conteudo.getCodigoConteudo());
				pstmt.setInt(2,codResposta);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					// Para o primeiro registro do resultset cria a instancia do objeto RespostaFiltro
					if (rs.isFirst())
					{
						respFiltro = new RespostaFiltro();
						respFiltro.setTipoRespostaFiltro(rs.getInt(3));
						respFiltro.setOrdem				(rs.getInt(4));
						respFiltro.setAplicaEmParalelo	(rs.getInt(2)==1 ? true : false);
					}
					// Independente, realiza a adicao dos filtros de conteudo instanciando um objeto
					// filtro e adicionando a colecao de filtros para o conteudo
					try
					{
						Object filtro = Class.forName(rs.getString(1)).newInstance();
						if (filtro instanceof FiltroConteudo)
							respFiltro.addFiltro((FiltroConteudo)filtro);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				// Se o objeto foi criado entao define como resposta, o objeto
				// contendo o valor a ser utilizado, utilizando o DAO respectivo
				if (respFiltro != null)
					respFiltro.setResposta(RespostaDAO.getInstance().findByCodigo(codResposta,codTipoResposta,con));
			}
			catch(SQLException e){
				throw e;
			}
			finally{
				try{
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
				}catch(Exception e){};
			}
		}
		return respFiltro;
	}
}
