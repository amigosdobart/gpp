/*
 * Created on 10/10/2004
 *
  */
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
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.model.Assinante;

import java.io.IOException;

import br.com.brasiltelecom.ppp.action.ajusteTransfSaldo.SalvarTransfSaldoAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

/**
 * @author Alberto Magno
 * 
 */
public class TransferidorSaldoBonus extends HttpServlet {

	Logger logger = Logger.getLogger(this.getClass());
	
	private ServletContext servletContext = null;

	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		JDO jdo = null;
		Database db = null;
		
		try 
		{
			 jdo = (JDO) servletContext.getAttribute(Constantes.JDO);
			 db = jdo.getDatabase();
				
				HttpSession session = request.getSession();
				

				String servidor  = (String) servletContext.getAttribute(Constantes.GPP_NOME_SERVIDOR);
				String porta     = (String) servletContext.getAttribute(Constantes.GPP_PORTA_SERVIDOR);
				
				String msisdn = "";
				if (request.getParameter("msisdn").length()==10)
					msisdn = "55";
				msisdn = msisdn+request.getParameter("msisdn"); 

				logger.info("Transferencia de Saldo Solicitado MSISDN " + msisdn);
				
				String logon = (String)request.getParameter("u");
				String senha = ((String)request.getParameter("s")).toLowerCase();
				
				Usuario usuario = null;
				
				if (logon==null && senha ==null)
					throw new Exception("Logon do usuário não informado.");
				else
				{
					db.begin();
					usuario = UsuarioHome.findByID(db,logon);
					if(db.isActive()){
						db.commit();	
					}
					if(!db.isClosed()){
						db.close();	
					}
					if (usuario==null||!senha.equals(usuario.getSenha()))
						throw new Exception("Logon inválido no PPP.");
				}
				

				session.setAttribute(Constantes.USUARIO,usuario);
					
				Assinante assinante = null;
				String operacao = (String) request.getParameter("OP");
				if (operacao.equals("I")) //Operação Inicial de consulta de saldo
				{
					assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn,servidor,porta);
					if(Integer.parseInt(assinante.getRetorno()) != 2)
					{
						////session.setAttribute("ASSINANTE",assinante);
						retornarDados(request,response,"Bonus: R$"+assinante.getSaldoBonus()+
						"<br/>Torpedo: R$"+assinante.getSaldoSms()+
						"<br/>Dados: R$"+assinante.getSaldoDados()+"<br/>");
						logger.info("Pesquisa de Saldo de Bônus Disponível realizada.");
						if (request.getAttribute(Constantes.MENSAGEM)!=null)
							retornarDados(request,response,(String)request.getAttribute(Constantes.MENSAGEM));
						
					}
					else
						retornarDados(request,response,"Funcionalidade nao disponivel.");
				}
				else if (operacao.equals("T"))
				{
					//String valor = (String)request.getParameter("valorTransferencia"); //Pega valor da requisição
					SalvarTransfSaldoAction transferencia = new SalvarTransfSaldoAction();
					request.setAttribute("contextoWIG",servletContext);
					
					db = jdo.getDatabase();
						transferencia.performPortal(null,null,request,response,db);
					if(db.isActive()){
						db.commit();	
					}
					if(!db.isClosed()){
						db.close();	
					}

					assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn,servidor,porta);
					if(Integer.parseInt(assinante.getRetorno()) != 2)
					{
						////session.setAttribute("ASSINANTE",assinante);
						retornarDados(request,response,(String)request.getAttribute(Constantes.MENSAGEM)+
								"<br/> Bonus: R$ "+assinante.getSaldoBonus()+
						"<br/>Torpedo: R$ "+assinante.getSaldoSms()+
						"<br/>Dados: R$ "+assinante.getSaldoDados()+"<br/>");
						logger.info("Tranferencia de Saldo de Bônus realizada.");
					}
}
		}		
		catch (Exception e)
		{
				logger.error("Erro no processo de transferencia de Saldo");
				if (request.getAttribute(Constantes.MENSAGEM)!=null)
					retornarDados(request,response,(String)request.getAttribute(Constantes.MENSAGEM));
				else
					retornarDados(request,response,"Erro no processo de transferencia de Saldo");
		}
		finally
		{
			if(db != null && !db.isClosed())
			{
				try 
				{
					db.rollback();
					db.close();
				} 
				catch (PersistenceException e1) 
				{
				}
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
			out.println(retorno+"<br/>");
			out.flush();
	
		} catch (IOException e) {
			logger.error("Erro retorno TransferenciaSaldoBonus", e);
			new Exception("Erro de Comunicação");
		}
	}

}
