

/*
 * Created on 10/06/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioFINBaseUsuarios;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório de Ativação de Cartões Físicos Virtuais
 * 
 * @author Marcos C. Magalhães
 * @since 10/06/2005
 */

public class ConsultaRelFINBaseUsuariosAction extends ActionPortal {

	SimpleDateFormat formatoDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	//SimpleDateFormat formatoDDMMMYYYY = new SimpleDateFormat("dd-MMM-yyyy"); 
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
	
				ActionForward result = null;
				
				db.begin();
				
				logger.info("Consulta por relatório de Ativação de Base Usuarios");
				
				
				//String cn = request.getParameter("codNac");         // obtem os codigos nacionais
				//String sa = request.getParameter("statusAss"); // obtem os status de assinante
				//String ss = request.getParameter("statusServ");  // obtem os status de serviço 
				
				
				Date dataPesq = null;
				
				if (!"".equals(request.getParameter("dataPesq")))
				   {
					dataPesq = formatoDDMMYYYY.parse(request.getParameter("dataPesq"));
				   }
					
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Ativação de Cartões Físicos Virtuais realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_BASE_USUARIOS;
			
				String endereco = "../reports/rwservlet?ppp+FINBaseUsuarios.rdf+"+request.getParameter("DESFORMAT")+"+dataImportacao="+formatoDDMMYYYY.format(dataPesq)+"+P_CN="+request.getParameter("P_CN")+"+P_SA="+request.getParameter("P_SA")+"+P_SS="+request.getParameter("P_SS")+"+categoria="+request.getParameter("categoria");
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = endereco.concat("+DELIMITED_HDR=NO+DELIMITER=\t+DES_TYPE=LOCALFILE+DES_NAME=.xls");
													
				request.setAttribute("endereco",endereco);

				result = actionMapping.findForward("redirect");
				
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}


