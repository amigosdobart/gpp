package br.com.brasiltelecom.ppp.action.consultaStatusAssinanteOptIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

/**
 * Manda a requisição para o wig fazer opt-out do assinante
 * @author	Geraldo Palmeira
 * @since	11/09/2006
 */
public class DesativaAssinanteOptIn extends ActionPortal
{
	Logger logger = Logger.getLogger(this.getClass());
	private ActionForward result;

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		try
		{
			// Busca informacoes do servidor WIG configurados
			ServletContext context = getServlet().getServletContext();
			String servidorWig      = (String)context.getAttribute(Constantes.WIG_NOME_SERVIDOR);
			String portaServidorWig = (String)context.getAttribute(Constantes.WIG_PORTA_SERVIDOR);
			
			String msisdn = request.getParameter("msisdn");
			
			// Busca o operador do portal
			HttpSession session = request.getSession(true);
			Usuario usuario = (Usuario)session.getAttribute(Constantes.USUARIO);
			
			// Manda a requisição de opt-out para o WIG 
			String urlStr =  "http://" + servidorWig + ":" + portaServidorWig + "/wig/desativarOptIn?MSISDN=" 
									   + msisdn + "&loc=00000000&btc=7059&op=" + usuario.getMatricula();
			URL url = new URL(urlStr);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			
			// Monta o WML de retorno do WIG
			String linha;
			StringBuffer wmlRetorno = new StringBuffer(); 
			while ( (linha = br.readLine()) != null )
				wmlRetorno.append(linha+"\n");
			
			// Faz o parse do wml e mostra a mensagem no portal
			String retorno = parseWml(wmlRetorno.toString());
			request.setAttribute(Constantes.MENSAGEM,retorno);
			request.setAttribute("msisdn",msisdn);
			
			result = actionMapping.findForward("mensagem");
			
			br.close();
		}
		catch(Exception e)
		{
			logger.error("Erro ao desativar opt-in do assinante (" +
					e.getMessage() + ")");
		}
		
		return result;
	}
	
	/**
	 * Metodo....: parseWml
	 * Descricao.: Realiza o parse do WML da desativação de opt-in
	 * 
	 * @param wmlRetorno
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public String parseWml (String wmlRetorno) throws SAXException, IOException
	{
		String mensagem = "";
		try
		{
			// Obtendo os objetos necessarios para a execucao do parse do wml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(wmlRetorno));
			Document doc = parse.parse(is);
			
			NodeList tagWml = ((Element) doc.getElementsByTagName("wml").item(0)).getChildNodes();
			
			NodeList tagCard = (((Element) tagWml).getElementsByTagName("card").item(0)).getChildNodes();
			
			if (((Element)tagCard).getElementsByTagName("p").item(0) != null && 
				((Element)tagCard).getElementsByTagName("p").item(0).getChildNodes().item(0) != null)
				mensagem = (((Element)tagCard).getElementsByTagName("p").item(0).getChildNodes().item(0).getNodeValue());
		}
		catch (Exception e)
		{
			logger.error("Erro ao desativar opt-in do assinante (" +
					e.getMessage() + ")");
		}
		
		return mensagem;
	}
	
	/**
	 * Retorna o código da operação
	 * @return String
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASS_OPTIN;
	}
}
