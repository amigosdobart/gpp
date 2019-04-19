/*
 * Criado em 15/12/2004
 */
package br.com.brasiltelecom.ppp.action.consultaInformacoesAssinante;

import java.util.Collection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import com.brt.gpp.comum.Definicoes;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.interfacegpp.InsereFFGPP;

/**
 * @author Marcelo Alves Araujo
 */
public class InsereFFAction extends ActionPortal {
	
	private String codOperacao = Constantes.COD_INSERIR_FF;
	Logger logger = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		logger.debug("Insercao de Amigos Toda Hora");
		short retorno = 0;
		String msisdn = request.getParameter("msisdn");
		String plano = request.getParameter("plano");
		String cn = "0"+msisdn.substring(2,4);
		Collection ff = new ArrayList();
		short limiteFF = Constantes.LIMITEFF_PREPAGO;
		
		if (plano.equals("4") || plano.equals("5"))
			limiteFF = Constantes.LIMITEFF_HIBRIDO;
		for (int i = 1; i <= limiteFF; i++)
		{
			String numero = request.getParameter("FF"+i);
			if(numero != null && numero.length() == 8)
				ff.add(cn+numero);
		}
		
		// Busca informações de porta e servidor do GPP
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		
		String operador = login.getMatricula();
		
		try 
		{
			retorno = InsereFFGPP.insereFFGPP(msisdn,ff,operador,servidor,porta);
		}		
		catch (Exception e)
		{
		    logger.error("Excecao: " + e);
			throw new Exception("Não foi possível conectar ao GPP.");
		}
		
		if(retorno == Definicoes.RET_OPERACAO_OK)
		{
			request.setAttribute(Constantes.MENSAGEM, "Atualização realizada com sucesso!");
			request.setAttribute("imagem", "img/tit_consulta_inf_ass.gif");
			return actionMapping.findForward("success");
		}
		else if(retorno == Definicoes.RET_STATUS_MSISDN_INVALIDO)
			request.setAttribute(Constantes.MENSAGEM, "Status do assinante inválido para inclusão de Amigos Toda Hora!");
		else if(retorno == Definicoes.RET_ASSINANTE_LIGMIX)
			request.setAttribute(Constantes.MENSAGEM, "Assinante LigMix!");
		else if(retorno == Definicoes.RET_MSISDN_NAO_ATIVO)
			request.setAttribute(Constantes.MENSAGEM, "Assinante não ativo!");
		
		return actionMapping.findForward("error");
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
