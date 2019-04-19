package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.resposta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Inclui uma resposta Wig.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 19/04/2007
 */
public class InclusaoRespostaWigAction extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisição HTTP que originou a execução dessa ação.
	 * @param response			Resposta HTTP a ser encaminhada ao usuário.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		ActionForward forward = actionMapping.findForward("success");
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        validaCaracteresEspeciais("Conteúdo WML", request.getParameter("descricao"));
	
	        RespostaWig respostaWig = new RespostaWig();
	        respostaWig.setDescricaoResposta(request.getParameter("descricao"));
	        respostaWig.setNomeResposta(request.getParameter("nome"));
	        RespostaWigDAO.incluirResposta(session, respostaWig);
			
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Resposta WIG cadastrada com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao cadastrar Resposta WIG. <br><br>" + e);
			logger.error("Erro ao cadastrar Resposta WIG. " + e.getMessage());	
			forward = actionMapping.findForward("error");
			if (session != null) session.getTransaction().rollback();
		}
		
		return forward;
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
	public void validaCaracteresEspeciais(String campo, String valor) throws Exception
	{
		String caracteres = "áâàãçéêíóôõúüÁÂÀÃÇÉÊÍÓÔÕÚÜ";
		int pos;
		char c;
		
		for (int i = 0; i < caracteres.length(); i++)
		{
			c = caracteres.charAt(i);
			pos = valor.indexOf(c);
			if (pos != -1)
			{
				throw new Exception("Caractere '" + c + "' encontrado na posição " + (pos + 1) + " " +
						"do campo '" + campo + "'. Não é permitido o uso dos seguintes caracteres: " + caracteres);
			}
		}
	}

}
 
