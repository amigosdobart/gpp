package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.Definicoes;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.dao.FiltroConteudoWigDAO;
import br.com.brasiltelecom.ppp.dao.VariavelOTAWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de cadastro das informacoes de anexo de resposta WIG
 * 
 * @author Joao Carlos
 * @since 17/11/2005
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 12/03/2007
 */
public class ShowCadastroAnexoRespostaWigAction extends ShowActionHibernate
{
	private String codOperacao = Constantes.COD_CADASTRAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "cadastroAnexoRespostaWig.vm";
	}
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		// Apesar de ser realizado a pesquisa de todos os conteudos existentes
		// na base para que o usuario escolha qual o menu sendo incluido irah
		// executar, o valor do conteudo que estah sendo alterado eh visualizado
		// transferindo os dados da tela anterior para a atual
		int 	codConteudo  = request.getParameter("codConteudo") != null ? Integer.parseInt(request.getParameter("codConteudo")) : 0;
		int 	codResposta  = request.getParameter("codResposta") != null ? Integer.parseInt(request.getParameter("codResposta")) : 0;
		int 	numFiltros   = request.getParameter("numFiltros")  != null ? Integer.parseInt(request.getParameter("numFiltros"))  : 0;
	
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        // Pesquisa o conteudo que estah sendo incluido o anexo de resposta
			ConteudoWig conteudo = ConteudoWigDAO.findByCodigo(session, codConteudo);
			
			// Busca no ConteudoWig todos os filtros existentes para a Resposta
			// que estah sendo pesquisada pelo usuario. Para a comparacao eh utilizado
			// um objeto RespostaWig preenchido somente o codResposta, atributo utilizado
			// para a comparacao.
			RespostaWig resposta = new RespostaWig();
			resposta.setCodResposta(codResposta);
			FiltroConteudoWig filtroConteudo = null;
			Collection filtros = conteudo.getFiltrosPorResposta(resposta);
			
			// Para os filtros correspondentes, realiza a pesquisa dos valores
			// existentes para serem adicionados ao filtro e utilizado na tela
			// que exibirah os detalhes.
			for (Iterator i = filtros.iterator(); i.hasNext();)
			{
				FiltroConteudoWig filtroConteudoWig = (FiltroConteudoWig)i.next();
				filtroConteudoWig.addFiltroResposta(
						FiltroConteudoWigDAO.findFiltrosResposta(
								session, filtroConteudoWig.getResposta().getCodResposta(), filtroConteudoWig.getFiltro().getNomeClasse()));
				
				// Atualiza tambem o objeto filtro conteudo. Apesar de utilizar o ultimo
				// listado na colecao, os valores que estao sendo utilizados na tela
				// sao somente para os valores que sao comuns a todos eles. Isso eh devido
				// a um problema da modelagem onde permite dados comuns a mais de um filtro
				// sendo que todos eles se referem a mesma resposta anexa
				filtroConteudo = filtroConteudoWig;
			}
	        
	        context.put("conteudoWig"    , conteudo);
			context.put("filtrosConteudo", filtros);
			context.put("filtroConteudo" , filtroConteudo);
			context.put("variaveis"      , VariavelOTAWigDAO.findAll(session));
			context.put("conteudos"      , ConteudoWigDAO.findAll(session));
			context.put("categorias"     , CategoriaDAO.findAll(session));
			context.put("numeroFiltros"  , String.valueOf(numFiltros + 1));
			context.put("mascaraMovel"   , Definicoes.MASCARA_MSISDN_MOVEL);
			
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar o cadastro de ConteudoWig. <br><br>" + e);
			logger.error("Erro ao consultar o cadastro de ConteudoWig. " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	

}