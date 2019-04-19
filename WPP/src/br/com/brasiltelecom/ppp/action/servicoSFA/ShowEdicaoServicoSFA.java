package br.com.brasiltelecom.ppp.action.servicoSFA;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CodigoServicoSFADAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;

/**
 * Mostra a p�gina de edi��o de ServicoSFA.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class ShowEdicaoServicoSFA extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_SERVICO_SFA;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "edicaoServicoSFA.vm";
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        String modo = (String)request.getAttribute("modo");
	        String filtroCodSFA = request.getParameter("filtroCodSFA");
	        String recuperarEstado = request.getParameter("recuperarEstado");
	        
	        CodigoServicoSFA codigoServicoSFA = null;
	        
	        if (recuperarEstado != null && recuperarEstado.toLowerCase().equals("true"))
				processarRequest(codigoServicoSFA, request, session);
			else
			{
				if (filtroCodSFA != null && !filtroCodSFA.equals(""))
				{
		        	codigoServicoSFA = CodigoServicoSFADAO.findById(session, Integer.parseInt(filtroCodSFA));
				
					if (codigoServicoSFA == null)
					{
						modo = "inclusao";
						codigoServicoSFA = new CodigoServicoSFA();
						codigoServicoSFA.setIdtCodigoServicoSFA(Integer.parseInt(filtroCodSFA));
						
						Map operacoes = (Map)context.get("operacoes");
						if (operacoes.get("CADASTRAR_SERVICO_SFA") != null)
							request.setAttribute("mensagem", "N�O EXISTE um Servi�o SFA para o C�digo informado!<br>" +
									"Preencha os dados abaixo para cadastrar um novo Servi�o SFA.");
						else
							request.setAttribute("mensagem", "N�O EXISTE um Servi�o SFA para o C�digo informado!");
	
					}
					else
					{
						modo = "alteracao";
					}
				}
			}
			
			context.put("entidade", codigoServicoSFA);  
			context.put("modo", modo);  
			context.put("categorias", CodigoServicoSFADAO.findListaCategorias(session));
			context.put("tiposRegistro", CodigoServicoSFADAO.findListaTiposRegistro(session));
			context.put("tiposServico", CodigoServicoSFADAO.findListaTiposServico(session));
			
	        session.getTransaction().rollback();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar Servi�o SFA. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar CodigoServicoSFA. " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * Transfere os par�metros do request (caso existam) para uma entidade. 
	 */
	public static void processarRequest(CodigoServicoSFA codigoServicoSFA,
			HttpServletRequest request, Session session) throws Exception
	{
		String codSFA = request.getParameter("codSFA");
		String descricao = request.getParameter("descricao");
		String tipoServico = request.getParameter("tipoServico");
		String tipoRegistro = request.getParameter("tipoRegistro");
		String categoria = request.getParameter("categoria");
		
		if (codSFA != null && !codSFA.equals(""))
			codigoServicoSFA.setIdtCodigoServicoSFA(Integer.parseInt(codSFA));
	
		if (descricao != null)
			codigoServicoSFA.setDesCodigoServico(descricao);
		
		if (tipoServico != null)
			codigoServicoSFA.setIdtTipoServico(tipoServico);
		
		if (tipoRegistro != null)
			codigoServicoSFA.setTipoRegistro(tipoRegistro);
		
		if (categoria != null)
			codigoServicoSFA.setIdtCategoria(categoria);			
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
