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

import br.com.brasiltelecom.ppp.interfacegpp.ConsultaSaldoPromocoesGPP;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * 
 * @author Joao Carlos
 * Data..: 25/04/2005
 * 
 */
public class ConsultorSaldoBoomerang extends HttpServlet 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private ServletContext servletContext = null;
	private DecimalFormat df = new DecimalFormat("##,###0.00",new DecimalFormatSymbols(Locale.FRANCE));

	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		try 
		{
			// Verifica se o usuario foi autenticado, caso negativo entao dispara um erro
			HttpSession session = request.getSession(true);
			Usuario user = (Usuario)session.getAttribute(Constantes.USUARIO);
			if (user == null)
				throw new Exception("Usuario nao autenticado.");
			
			// Identifica os valores dos parametros para a consulta
			String msisdn 	= request.getParameter("msisdn");
			int mes			= Integer.parseInt(request.getParameter("mes"));

			// Realiza a busca nas configuracoes do servidor de aplicacao para identificar
			// os valores a serem utilizados para a conexao com o sistema GPP
			String servidor  = (String) servletContext.getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta     = (String) servletContext.getAttribute(Constantes.GPP_PORTA_SERVIDOR);
			
			// Cria uma instancia da classe que ira realizar a consulta no sistema GPP
			SaldoBoomerang valorSaldo = ConsultaSaldoPromocoesGPP.getValorConcedidoBoomerang(msisdn,mes,servidor,porta);
			// Formata o valor do resultado para ser devolvido para o chamador dessa servlet
			retornarDados(request,response,"R$"+df.format(valorSaldo.valorRecebido)+"@"+valorSaldo.fezRecarga);
		}		
		catch (Exception e)
		{
			logger.error("Erro na consulta do saldo boomerang");
			if (request.getAttribute(Constantes.MENSAGEM)!=null)
				retornarDados(request,response,(String)request.getAttribute(Constantes.MENSAGEM));
			else
				retornarDados(request,response,"Erro na consulta do saldo boomerang. "+e.getMessage());
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
