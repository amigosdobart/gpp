package br.com.brasiltelecom.ppp.action.relatoriosControleReceita.expurgoEstornoPulaPula;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Classe responsavel pela geracao de relatorio gerencial de expurgo/estorno de bonus Pula-Pula sumarizado.
 * 
 *	@author		Daniel Ferreira
 *	@since		19/04/2006
 */
public class ConsultaExpurgoEstornoPulaPulaAction extends ActionPortal 
{

	private String codOperacao = Constantes.COD_CONSULTAR_EXPURGO_ESTORNO;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{
		db.begin();
		
		ActionForward result = null;
		
		logger.info("Consulta por relatorio gerencial de expurgo/estorno de bonus Pula-Pula");

		String	dataInicial	= request.getParameter("dataInicial");
		String	dataFinal	= request.getParameter("dataFinal");
		String	cns			= request.getParameter("CODIGOS");
		String	promocao	= request.getParameter("PROMOCAO"); 
		
		String endereco = null;
		
		endereco = "./relatoriosWPP?NOME_RELATORIO=CTRExpurgoEstornoDelimited+ARQUIVO_PROPRIEDADES=/relatorio/ControleReceita.xml+DATA_INICIAL=" + dataInicial + "+DATA_FINAL=" + dataFinal + "+CNS=" + cns + "+PROMOCAO=" + promocao;
						
		request.setAttribute("endereco",endereco);
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatorio gerencial de expurgo/estorno de bonus Pula-Pula efetuada com sucesso!");
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
