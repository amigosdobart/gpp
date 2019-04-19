package br.com.brasiltelecom.ppp.action.relatorioMKTPacoteDados;

import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;
import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatorio da Oferta de Pacote de Dados informada
 * 
 * @author Joao Paulo Galvagni
 * @since  21/08/2007
 */
public class ConsultaRelMKTPacoteDadosAction extends ActionPortalHibernate 
{
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private String codOperacao = Constantes.COD_CONSULTAR_PCT_DADOS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
									   ActionForm actionForm, HttpServletRequest request,
									   HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		ActionForward result = null;
		logger.info("Consulta as informacoes dos Pacote de Dados");
		
		String idtOferta = request.getParameter("idtOferta");
		
		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTRelPacoteDados+" +
				   		  "ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+" +
				   		  "IDT_OFERTA="+idtOferta;
		
		request.setAttribute("endereco",endereco);
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório da Oferta de Pacote de Dados realizada com sucesso!");
		
		result = actionMapping.findForward("redirect"); 
 		
		return result;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}