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
	 * Implementa a l�gica de neg�cio dessa a��o.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param response			Resposta HTTP a ser encaminhada ao usu�rio.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
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
	        
	        validaCaracteresEspeciais("Conte�do WML", request.getParameter("descricao"));
	
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
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
	public void validaCaracteresEspeciais(String campo, String valor) throws Exception
	{
		String caracteres = "��������������������������";
		int pos;
		char c;
		
		for (int i = 0; i < caracteres.length(); i++)
		{
			c = caracteres.charAt(i);
			pos = valor.indexOf(c);
			if (pos != -1)
			{
				throw new Exception("Caractere '" + c + "' encontrado na posi��o " + (pos + 1) + " " +
						"do campo '" + campo + "'. N�o � permitido o uso dos seguintes caracteres: " + caracteres);
			}
		}
	}

}
 
