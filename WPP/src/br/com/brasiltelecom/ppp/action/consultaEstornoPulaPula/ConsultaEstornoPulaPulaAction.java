package br.com.brasiltelecom.ppp.action.consultaEstornoPulaPula;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.CodigosRetornoHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaEstornoPulaPulaGPP;
import br.com.brasiltelecom.ppp.model.RetornoEstornoPulaPula;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Executa a consulta pelos registros de Estorno de Bonus Pula-Pula por Fraude.
 * 
 *	@author	Daniel Ferreira
 *	@since	06/03/2006
 */
public class ConsultaEstornoPulaPulaAction extends ActionPortal 
{

	private	String	codOperacao	= Constantes.COD_CONSULTA_ESTORNO_PULA;
	private	Logger	logger		= Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm,	HttpServletRequest request, HttpServletResponse response, Database db) 	throws Exception 
	{
		ActionForward result = null;
		db.begin();
		
		String	msisdn		= request.getParameter("msisdn");
		String	dataInicio	= request.getParameter("dataInicio");
		String	dataFim		= request.getParameter("dataFim");
		
		logger.info("Consulta de estorno de bonus Pula-Pula para o MSISDN: " + msisdn);
			
		//Obtendo o diretorio e o nome para a criacao do arquivo que ira conter o XML do comprovante.
		Collection	diretorios	= (Collection)servlet.getServletContext().getAttribute(Constantes.DIRETORIO_COMPROVANTES);
		String		sessionId	= request.getSession().getId();
		
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		//Busca o extrato do pula-pula (ligacoes recebidas) do GPP
		RetornoEstornoPulaPula resultSet = ConsultaEstornoPulaPulaGPP.getEstornos(msisdn, dataInicio, dataFim, servidor, porta, diretorios, sessionId);

		//extrato retornado com sucesso
		switch(resultSet.getCodigoRetorno())
		{
			case Constantes.GPP_RET_OPERACAO_OK:
			{
				String mensagem = "Consulta Extrato Pula-Pula para o número de acesso '" + resultSet.format(RetornoEstornoPulaPula.MSISDN) + "' realizada com sucesso!";
				request.setAttribute(Constantes.RESULT	, resultSet);				
				request.setAttribute(Constantes.MENSAGEM, mensagem);
				result = actionMapping.findForward("success");
				break;
			}
			case Constantes.GPP_RET_NENHUM_REGISTRO:
			{
			    request.setAttribute("imagem", "img/tit_consulta_estorno_pula_pula.gif");
			    request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado.");
			    result = actionMapping.findForward("nenhumRegistro");
			    break;
			}
			default:
			{
			    CodigosRetorno retorno = CodigosRetornoHome.findByVlrRetorno(String.valueOf(resultSet.getCodigoRetorno()), db);
			    String mensagem = (retorno != null) ? retorno.getDescRetorno() : "";
			    request.setAttribute(Constantes.MENSAGEM, mensagem);
			    result = actionMapping.findForward("error");
			}
		}

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
