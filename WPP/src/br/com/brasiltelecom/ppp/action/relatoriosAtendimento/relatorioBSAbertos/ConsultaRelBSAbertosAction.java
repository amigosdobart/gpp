package br.com.brasiltelecom.ppp.action.relatoriosAtendimento.relatorioBSAbertos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class ConsultaRelBSAbertosAction extends ActionPortal
{
	Logger logger = Logger.getLogger(this.getClass());

	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_BS_ABERTOS;
	}

	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		ActionForward result = null;
		logger.info("Consulta por relatorio gerencial de expurgo/estorno de bonus Pula-Pula");
		String dataInicial = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");

		StringBuffer rel = new StringBuffer("./relatoriosWPP?");
		rel.append("NOME_RELATORIO=RelBSAbertoDelimited");
		rel.append("+ARQUIVO_PROPRIEDADES=/relatorio/Atendimento.xml");
		rel.append("+DATA_INICIAL=").append(dataInicial);
		rel.append("+DATA_FINAL=").append(dataFinal);

		request.setAttribute("endereco",rel.toString());
		request.setAttribute(Constantes.MENSAGEM, "Relatorio BSs abertos gerado com sucesso!");
		result = actionMapping.findForward("redirect");

		return result;
	}
}
