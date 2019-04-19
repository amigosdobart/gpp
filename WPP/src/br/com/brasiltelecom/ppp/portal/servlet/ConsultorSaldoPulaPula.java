package br.com.brasiltelecom.ppp.portal.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.dao.HibernateHelper;
import br.com.brasiltelecom.ppp.home.CodigosRetornoHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaPromocaoPulaPulaGPP;
import br.com.brasiltelecom.ppp.model.RetornoPulaPula;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

import java.io.IOException;

/**
 *
 * @author Joao Carlos
 * Data..: 25/04/2005
 *
 */
public class ConsultorSaldoPulaPula extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private ServletContext servletContext = null;

	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		Database db = null;
		Session sessionHibernate = null;
		try
		{
			// Verifica se o usuario foi autenticado, caso negativo entao dispara um erro
			HttpSession session = request.getSession(true);
			Usuario user = (Usuario)session.getAttribute(Constantes.USUARIO);
			if (user == null)
				throw new Exception("Usuario nao autenticado.");

			// Identifica os valores dos parametros para a consulta
			String msisdn 	= request.getParameter("msisdn");
			String mes		= request.getParameter("mes");

			// Realiza a busca nas configuracoes do servidor de aplicacao para identificar
			// os valores a serem utilizados para a conexao com o sistema GPP
			String servidor  = (String) servletContext.getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta     = (String) servletContext.getAttribute(Constantes.GPP_PORTA_SERVIDOR);
			sessionHibernate = HibernateHelper.getSession();
			sessionHibernate.beginTransaction();
			
			// Realiza a consulta da promocao pulapula do cliente
			RetornoPulaPula saldos = ConsultaPromocaoPulaPulaGPP.getPromocao(msisdn,mes,servidor,porta,sessionHibernate);

			String retorno = "";

			if ( saldos.getStatus() != null && saldos.getStatus().equals("1"))
				retorno = "0099;0;0;0;Efetue uma recarga para ter direito ao bonus pula-pula.";
			else if ( !saldos.getRetorno().equals("0000") && !saldos.getRetorno().equals("0082") )
				{
					JDO jdo = (JDO) session.getServletContext().getAttribute(Constantes.JDO);

					db = jdo.getDatabase();
					db.begin();
					saldos.setMensagemRetorno(CodigosRetornoHome.findByVlrRetorno(saldos.getRetorno(), db).getDescRetorno());
					db.commit();
					retorno = saldos.getRetorno() + ";" + saldos.getValorTotal() + ";" +
					  		  saldos.getValorParcial() + ";" + saldos.getValorAReceber() + ";" + saldos.getMensagemRetorno();
				}
				else
				{
					// Concatena os valores a receber
					retorno = saldos.getRetorno() + ";" + saldos.getValorTotal() + ";" +
							  saldos.getValorParcial() + ";" + saldos.getValorAReceber();
				}
			sessionHibernate.getTransaction().commit();
			// Formata o valor do resultado para ser devolvido para o chamador dessa servlet
			retornarDados(request,response,retorno);
		}
		catch (Exception e)
		{
			if(sessionHibernate!=null)
			{
				sessionHibernate.getTransaction().rollback();
			}
			logger.error("Erro na consulta do saldo Pula Pula",e);
			if (request.getAttribute(Constantes.MENSAGEM)!=null)
				retornarDados(request,response,(String)request.getAttribute(Constantes.MENSAGEM));
			else
				retornarDados(request,response,"Erro na consulta do saldo Pula Pula. "+e.getMessage());
		}
		finally
		{
			try
			{
				if (db != null)
					db.close();
			}
			catch (Exception e)
			{
				logger.error(e);
			}
		}
	}

	public void init(ServletConfig scfg) throws ServletException
	{
		servletContext = scfg.getServletContext();
	}

	private void retornarDados(HttpServletRequest request, HttpServletResponse response,String retorno)
	{
		try
		{
			ServletOutputStream out = response.getOutputStream();
			out.println(retorno);
			out.flush();
		} catch (IOException e) {
			logger.error("Erro na escrita do resultado", e);
			new Exception("Erro de Comunicação");
		}
	}

}
