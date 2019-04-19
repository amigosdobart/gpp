package br.com.brasiltelecom.ppp.home;

/*
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroRespostaWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
*/

/**
 * ------------------------------------------------------------------------
 * 
 * DEPRECATED  
 * 
 * O mapeamento de FiltroConteudoWig foi movido para o Hibernate.
 * Utilize a classe <code>br.com.brasiltelecom.ppp.dao.FiltroConteudoWigDAO</code>
 * 
 * Bernardo Dias, 08/03/2007
 * 
 * ------------------------------------------------------------------------
 */
public class FiltroConteudoWigHome
{
	/**
	 * Metodo....:findByConteudo
	 * Descricao.:Retorna uma lista de filtros de conteudo por um determinado conteudo especificado.
	 *            OBS: Este metodo nao eh chamado na instanciacao de um conteudo pois o relacionamento
	 *                 entidade-objeto realizado pelo Castor jah faz esta operacao. O metodo existe
	 *                 somente por comodidade para um outro uso.
	 * @param conteudo - Conteudo sendo pesquisado
	 * @param db	   - Banco de dados
	 * @return Collection - Lista de filtros que deverao ser aplicados neste conteudo
	 * @throws PersistenceException
	 *
	public static Collection findByConteudoResposta(int codConteudo, int codResposta, Database db, boolean readOnly) throws PersistenceException
	{
		Logger logger = Logger.getLogger(FiltroConteudoWigHome.class);
		OQLQuery query = null;
		QueryResults rs = null;
		Collection listaByConteudo = new ArrayList();
		try
		{
			query = db.getOQLQuery("select a from "+
								   "br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig a "+
								   "where a.codConteudo = $1 " +
								   "and a.codResposta = $2");
			query.bind(codConteudo);
			query.bind(codResposta);
			rs = query.execute(readOnly ? Database.ReadOnly : Database.Shared);
			while (rs.hasMore())
				listaByConteudo.add((FiltroConteudoWig)rs.next());
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar informacoes de FiltroConteudoWig. Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return listaByConteudo;
	}
	
	/**
	 * Metodo....:alteraAnexoResposta
	 * Descricao.:Realiza a alteracao do anexo de resposta
	 * @param conteudo - Conteudo onde o anexo eh inserido
	 * @param resposta - Valor da resposta sendo alterada
	 * @param filtros  - Filtros de resposta que serao alterados
	 * @param db
	 * @throws PersistenceException
	 *
	public static void alteraAnexoResposta(ConteudoWig conteudo
			                              ,RespostaWig resposta
			                              ,Collection filtros
			                              ,Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(FiltroConteudoWigHome.class);
		try
		{
			// Para o tratamento da alteracao do cadastro de anexo de resposta
			// o tratamento eh realizado da seguinte forma:
			// Primeiro altera as informacoes de anexo de resposta (RespostaWig)
			// depois realiza a exclusao dos dados de filtro de anexo de resposta
			// pois nao eh possivel a alteracao destes, e por ultimo realiza a
			// inclusao dos filtros cadastrados na tela pelo usuario
			db.begin();
			
			// Realiza a pesquisa dos filtros existentes para a resposta
			// que estah sendo alterada
			Collection filtrosWig = FiltroConteudoWigHome.findByConteudoResposta(conteudo.getCodConteudo(), resposta.getCodResposta(), db, false);
			
			// Para os filtros correspondentes, realiza a pesquisa dos valores
			// existentes para serem adicionados ao filtro e utilizado na tela
			// que exibirah os detalhes.
			for (Iterator i = filtrosWig.iterator(); i.hasNext();)
			{
				FiltroConteudoWig filtro = (FiltroConteudoWig)i.next();
				Collection valoresFiltro = FiltroConteudoWigHome.findFiltroResposta(resposta.getCodResposta(), filtro.getFiltro().getNomeClasse(), db, false);
				for (Iterator j = valoresFiltro.iterator(); j.hasNext();)
					db.remove((FiltroRespostaWig)j.next());
				
				// Remove tambem o filtro apos remover os valores existentes para este filtro
				db.remove(filtro);
			}
			db.commit();
			// OBS: Devido a problemas com o CASTOR, nao foi possivel inserir os filtros
			// cadastrados pela tela apos remover na mesma transacao. Dessa forma foi 
			// decidido iniciar outra transacao para incluir os filtros e acertar os
			// dados da resposta.
			// Caso esta segunda transacao falhe, entao a primeira jah terah sido executada
			// causando grandes problemas pois irah existir um anexo de resposta que nao
			// possui nenhum filtro cadastrado.
			db.begin();
			// Realiza a insercao dos filtros de resposta
			insereFiltrosResposta(conteudo,resposta,filtros,db);

			RespostaWig respostaAtu = (RespostaWig)db.load(RespostaWig.class,new Integer(resposta.getCodResposta()));
			respostaAtu.setDescricaoResposta(resposta.getDescricaoResposta());
			respostaAtu.setNomeResposta		(resposta.getNomeResposta());
			
			db.commit();
		}
		catch(PersistenceException e)
		{
			if (db.isActive())
				db.rollback();

			logger.error("Erro ao inserir as informacoes de Anexo de Resposta Wig. Erro:"+e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Metodo....:insereAnexoResposta
	 * Descricao.:Insere o anexo de resposta na base de dados, incluindo todas as configuracoes e
	 *            valores para os filtros desejados pelo usuario
	 * @param conteudo	- Conteudo de referencia onde o anexo deve ser incluido
	 * @param resposta	- Valor do anexo de resposta a ser incluido no conteudo original
	 * @param filtros   - Filtros que devem ser aplicados para que o anexo possa ser incluido
	 * @param db        - Banco de dados
	 * @throws PersistenceException
	 *
	public static void insereAnexoResposta(ConteudoWig conteudo
			                              ,RespostaWig resposta
			                              ,Collection filtros
			                              ,Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(FiltroConteudoWigHome.class);
		// Realiza a inclusao do filtro sendo cadastrado realizando
		// a inclusao de acordo com os seguintes passos:
		// 1 - Realiza a inclusao do anexo de resposta (RespostaWig)
		//     Apos a inclusao o objeto RespostaWig vem com a identificacao
		//     da chave (codResposta) preenchido, que entao serah definido
		//     para os outros objetos dos filtros que serao aplicados para
		//     esta resposta.
		// 2 - Realiza a inclusao dos filtros
		//     2.1 - Inclui o registro de FiltroConteudoWig que define a configuracao
		//           deste filtro (Msisdn, Modelo, ERB, etc..)
		//     2.2 - Realiza um loop em todos os valores possiveis para o filtro
		//           inserindo entao na tabela correspondente todos os registros
		// 3 - Efetiva a transacao somente no final do processamento.
		try
		{
			db.begin();
			// Insere entao o anexo de resposta
			db.create(resposta);
			// Realiza a insercao dos filtros de resposta
			insereFiltrosResposta(conteudo,resposta,filtros,db);
			
			db.commit();
		}
		catch(PersistenceException e)
		{
			if (db.isActive())
				db.rollback();

			logger.error("Erro ao inserir as informacoes de Anexo de Resposta Wig. Erro:"+e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Metodo....:insereFiltrosResposta
	 * Descricao.:Realiza a insercao somente dos filtros de resposta
	 * @param conteudo - Conteudo Wig
	 * @param resposta - Resposta Wig (Resposta anexa)
	 * @param filtros  - Filtros da resposta anexa
	 * @param db       - Objeto de banco de dados
	 * @throws PersistenceException
	 *
	private static void insereFiltrosResposta(ConteudoWig conteudo
											 ,RespostaWig resposta
											 ,Collection filtros
											 ,Database db) throws PersistenceException
	{
		// Realiza agora a alteracao do codigo de resposta no restante dos objetos
		for (Iterator i = filtros.iterator(); i.hasNext();)
		{
			FiltroConteudoWig filtro = (FiltroConteudoWig)i.next();
			// A verificacao se o filtro eh diferente de nulo eh realizada
			// pois como ainda nao se sabe quais os filtros o usuario decidiu inserir
			// Na servlet de coleta dos dados se o filtro nao foi preenchido entao ele
			// eh inserido nulo na colecao.
			if (filtro != null)
			{
				filtro.setCodResposta(resposta.getCodResposta());
				filtro.setResposta(resposta);
				
				// Insere as informacoes do filtro do anexo de resposta (ainda sem os valores)
				db.create(filtro);
				
				// Faz uma varredura em todos os valores existentes para este 
				// filtro de resposta para entao inserir um por um.
				for (Iterator j = filtro.getFiltrosResposta().iterator(); j.hasNext();)
				{
					FiltroRespostaWig filtroResposta = (FiltroRespostaWig)j.next();
					filtroResposta.setCodResposta(resposta.getCodResposta());
					
					db.create(filtroResposta);
				}
			}
		}
	}
	/**
	 * Metodo....:findFiltroResposta
	 * Descricao.:Retorna uma lista contendo os valores do filtro sendo pesquisado
	 * @param resposta		- Anexo de Resposta
	 * @param nomeClasse    - Nome da classe do filtro sendo pesquisado
	 * @param db 			- Banco de dados
	 * @return Collection 	- Lista de valores existentes para o filtro sendo pesquisado
	 * @throws PersistenceException
	 *
	public static Collection findFiltroResposta(int codResposta, String nomeClasse, Database db, boolean readOnly ) throws PersistenceException
	{
		Logger logger = Logger.getLogger(FiltroConteudoWigHome.class);
		Collection listaValores = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			// Para a pesquisa de qual classe utilizar, eh realizado entao um De-Para da classe
			// que estah cadastrada no banco de dados (Classe para uso no G-OTA) da classe que
			// representa esta tabela no banco de dados (Classe DB Castor). Portanto realiza
			// este De-Para sendo que as classes DB todas implementam a interface FiltroResposta
			// utilizado posteriormente para a visualizacao
			query = db.getOQLQuery("select a from " + FiltroConteudoWig.getNomeClasseCastor(nomeClasse) + " a " + "where a.codResposta = $1");
			query.bind(codResposta);
			
			rs = query.execute(readOnly ? Database.ReadOnly: Database.Shared);
			while (rs.hasMore())
				listaValores.add(rs.next());
		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao pesquisar as informacoes de valores de Filtros. Classe:"+nomeClasse+". Erro:"+e.getMessage());
			throw e;
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return listaValores;
	}
	
	/**
	 * Metodo....:removerFiltrosConteudo
	 * Descricao.:Remove uma lista de filtros conteudo e seus valores respectivos.
	 * @param listaFiltros
	 * @param db
	 * @throws PersistenceException
	 *
	public static void removerFiltrosConteudo(Collection listaFiltros, Database db) throws PersistenceException
	{
		Logger logger = Logger.getLogger(FiltroConteudoWigHome.class);
		try
		{
			Collection anexosARemover = new LinkedHashSet();
			// Para remover os filtros de conteudo (Anexos de Resposta) eh necessario,
			// alem de remover os dados da tabela de configuracao dos filtros, remover
			// os dados da tabela de Resposta (onde efetivamente fica o anexo de resposta).
			// Porem eh possivel mais de um filtro por anexo, entao ao remover os valores
			// e as configuracoes de filtro, os valores de resposta sao adicionados em uma
			// lista para evitar duplicidades e removido no fim do metodo.
			for (Iterator i = listaFiltros.iterator(); i.hasNext(); )
			{
				FiltroConteudoWig filtro = (FiltroConteudoWig)i.next();
				Collection valoresFiltro = FiltroConteudoWigHome.findFiltroResposta(filtro.getResposta().getCodResposta(), filtro.getFiltro().getNomeClasse(), db, false);
				// Remove primeiro os valores devido as constraints
				// existentes no banco de dados
				for (Iterator j = valoresFiltro.iterator(); j.hasNext(); )
				{
					FiltroRespostaWig valor = (FiltroRespostaWig)j.next();
					db.remove(valor);
				}
				// Apos remover os valores entao agora eh possivel remover o filtro.
				// Porem antes adiciona a Resposta na lista de anexos a serem removidos
				anexosARemover.add(filtro.getResposta());
				db.remove(filtro);
			}
			// Apos remover os filtros entao remove todos os objetos na lista
			// de anexos a serem removidos.
			for (Iterator i = anexosARemover.iterator(); i.hasNext();)
				db.remove((RespostaWig)i.next());

		}
		catch(PersistenceException e)
		{
			logger.error("Erro ao remover Anexos de Resposta Wig."+ listaFiltros +" Erro:"+e.getMessage());
			throw e;
		}
	}
	*/
}
