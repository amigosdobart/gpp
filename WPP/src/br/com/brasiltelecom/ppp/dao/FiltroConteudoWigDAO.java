package br.com.brasiltelecom.ppp.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroRespostaWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;

/**
 * Interface de acesso ao cadastro de Filtros de Conteúdo Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 08/03/2007
 */
public class FiltroConteudoWigDAO
{
	
	/**  
	 * Consulta uma lista de Filtros para um determinado Conteúdo e Resposta especificados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param codConteudo			Código do Conteúdo.
	 * @param respostaWig			Instância de <code>RespostaWig</code>.
	 * @return 						Coleção de <code>FiltroConteudoWig</code>.
	 */
	public static List findByConteudoResposta(Session session, int codConteudo, RespostaWig respostaWig)
	{
		Query query = session.createQuery(
				"FROM 	br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig a " +
				"WHERE      (a.codConteudo = ?) " +
				"		AND (a.resposta = ?) ");
	
		query.setInteger(0, codConteudo);
		query.setEntity(1, respostaWig);
		
		return query.list();
	}
	
	/**  
	 * Realiza a alteracao do anexo de resposta.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param conteudoWig			Conteudo onde o anexo é inserido. Instância de <code>ConteudoWig</code>.
	 * @param respostaWig			Resposta sendo alterada. Instância de <code>RespostaWig</code>.
	 * @param filtros  				Filtros de resposta que serão alterados. Coleção de <code>FiltroConteudoWig</code>. 
	 */
	public static void alteraAnexoResposta(Session session, ConteudoWig conteudoWig,
			                               RespostaWig respostaWig, Collection filtros)
	{
		// Procedimento:
		// Primeiro realiza a exclusao dos dados de filtro de anexo de resposta (pois nao eh 
		// possivel a alteracao destes) e depois inclui os filtros cadastrados na tela pelo usuario

		// Realiza a pesquisa dos filtros existentes para a resposta que estah sendo alterada
		Collection filtrosConteudoWig = FiltroConteudoWigDAO.findByConteudoResposta(session, conteudoWig.getCodConteudo(), respostaWig);
		
		// Para os filtros correspondentes, realiza a pesquisa dos valores (FiltroRespostaWig)
		// a serem removidos
		for (Iterator i = filtrosConteudoWig.iterator(); i.hasNext();)
		{
			FiltroConteudoWig filtroConteudoWig = (FiltroConteudoWig)i.next();
			Collection valoresFiltro = FiltroConteudoWigDAO.findFiltrosResposta(session, respostaWig.getCodResposta(), filtroConteudoWig.getFiltro().getNomeClasse());
			for (Iterator j = valoresFiltro.iterator(); j.hasNext();)
			{
				session.delete((FiltroRespostaWig)j.next());
			}
			
			// Remove tambem a referencia na tabela FiltroConteudoWig 
			//apos remover os valores existentes para este filtro
			session.delete(filtroConteudoWig);
		}

		// Realiza a insercao dos filtros de resposta
		insereFiltrosResposta(session, conteudoWig, respostaWig, filtros);  
	}

	/**  
	 * Insere os filtros de resposta. Os filtros são associados
	 * à respostaWig especificada e todos os objetos são inseridos no banco (nas tabelas
	 * de cada filtro e na tabela de associação representada pela entidade FiltroConteudoWig).
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param conteudoWig			Instância de <code>ConteudoWig</code>.
	 * @param respostaWig			Resposta anexa. Instância de <code>RespostaWig</code>.
	 * @param filtros  				Coleção de <code>FiltroConteudoWig</code>. 
	 */
	private static void insereFiltrosResposta(Session session, ConteudoWig conteudoWig,
											  RespostaWig respostaWig, Collection filtros)
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
				filtro.setResposta(respostaWig);
				
				// Insere as informacoes do filtro do anexo de resposta (ainda sem os valores)
				session.save(filtro);
				
				// Faz uma varredura em todos os valores existentes para este 
				// filtro de resposta para entao inserir um por um.
				for (Iterator j = filtro.getFiltrosResposta().iterator(); j.hasNext();)
				{
					FiltroRespostaWig filtroRespostaWig = (FiltroRespostaWig)j.next();
					filtroRespostaWig.setCodResposta(respostaWig.getCodResposta());
					session.save(filtroRespostaWig);
				}
			}
		}
	}
	
	/**  
	 * Consulta uma lista contendo os valores do filtro sendo pesquisado
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param codResposta			Código do Anexo de Resposta.
	 * @param nomeClasse    		Nome da classe do filtro sendo pesquisado.
	 * @return 						Coleção de <code>Object</code>. O tipo é determinado por 'nomeClasse'.
	 */
	public static List findFiltrosResposta(Session session, int codResposta, String nomeClasse)
	{
		// Para a pesquisa de qual classe utilizar, eh realizado entao um De-Para da classe
		// que estah cadastrada no banco de dados (Classe para uso no G-OTA) da classe que
		// representa esta tabela no banco de dados. Portanto realiza
		// este De-Para sendo que as classes DB todas implementam a interface FiltroResposta
		// utilizado posteriormente para a visualizacao
		Query query = session.createQuery(
				"FROM 	" + FiltroConteudoWig.getNomeClasseCastor(nomeClasse) + " a " +
				"WHERE      (a.codResposta = ?) ");
	
		query.setInteger(0, codResposta);	
		return query.list();
	}
	
	/**  
	 * Insere o anexo de resposta, incluindo todas as configuracoes e
	 * valores para os filtros desejados pelo usuario.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param conteudoWig			Conteudo de referencia onde o anexo deve ser incluido. Instância de <code>ConteudoWig</code>.
	 * @param respostaWig			Valor do anexo de resposta a ser incluido no conteudo original. Instância de <code>RespostaWig</code>.
	 * @param filtros  				Coleção de <code>FiltroConteudoWig</code>. 
	 */
	public static void insereAnexoResposta(Session session, ConteudoWig conteudoWig,
			                               RespostaWig respostaWig, Collection filtros)
	{
		// Insere o anexo de resposta
		session.save(respostaWig);
		
		// Realiza a insercao dos filtros de resposta
		insereFiltrosResposta(session, conteudoWig, respostaWig, filtros);
	}
	
	/**  
	 * Remove uma lista de filtros conteudo e seus valores respectivos.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @param filtros  				Coleção de <code>FiltroConteudoWig</code>. 
	 */
	public static void removerFiltrosConteudo(Session session, Collection filtros)
	{
		Collection anexosARemover = new LinkedHashSet();
		
		// Para remover os filtros de conteudo (Anexos de Resposta) eh necessario,
		// alem de remover os dados da tabela de configuracao dos filtros, remover
		// os dados da tabela de Resposta (onde efetivamente fica o anexo de resposta).
		// Porem eh possivel mais de um filtro por anexo, entao ao remover os valores
		// e as configuracoes de filtro, os valores de resposta sao adicionados em uma
		// lista para evitar duplicidades e removido no fim do metodo.
		
		for (Iterator i = filtros.iterator(); i.hasNext(); )
		{
			FiltroConteudoWig filtroConteudoWig = (FiltroConteudoWig)i.next();
			Collection valoresFiltro = FiltroConteudoWigDAO.findFiltrosResposta(session, filtroConteudoWig.getResposta().getCodResposta(), filtroConteudoWig.getFiltro().getNomeClasse());
			
			// Remove primeiro os valores devido as constraints
			// existentes no banco de dados
			for (Iterator j = valoresFiltro.iterator(); j.hasNext(); )
				session.delete((FiltroRespostaWig)j.next());
	
			// Apos remover os valores entao agora eh possivel remover o filtro.
			// Porem antes adiciona a Resposta na lista de anexos a serem removidos
			anexosARemover.add(filtroConteudoWig.getResposta());
			session.delete(filtroConteudoWig);
		}
		
		// Apos remover os filtros entao remove todos os objetos na lista
		// de anexos a serem removidos.
		for (Iterator i = anexosARemover.iterator(); i.hasNext();)
			session.delete((RespostaWig)i.next());
	}
}
