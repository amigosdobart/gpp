

/*
 * Created on 30/08/2004
 *
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ContingenciaCrmHome;
import br.com.brasiltelecom.ppp.interfacegpp.BloqueioContingenciaGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.BloqueioStatus;
import br.com.brasiltelecom.ppp.portal.entity.DadosBoCrm;
import br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import com.brt.gpp.comum.Definicoes;


import java.text.SimpleDateFormat;
/**
 * @author Henrique Canto
 */
public class EfetuaBloqueioAction extends ActionPortal {

	private String codOperacao=Constantes.COD_BLOQUEIO_CONTINGENCIA;
	Logger logger = Logger.getLogger(this.getClass());

	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) 
	
	throws Exception 
	{

		logger.info("Bloqueio de assinante em modo de contingência solicitado");
			
		ActionForward result = null;		
		HttpSession session = request.getSession();
		db.begin();
		String msisdn = "55" + (String)request.getParameter("msisdn");
		Usuario usuario = (Usuario) session.getAttribute(Constantes.USUARIO);
		SimpleDateFormat dat = new SimpleDateFormat("dd/MM/yyyy");

		
		
			try 
			{
				if (request.getParameter("motivoBloqueio") != null && !request.getParameter("motivoBloqueio").toString().equals("")) 
				{
			
				
					// Início do Código para bloqueio de servicos e cOnsulta de idAtividade ao GPP
					String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
					String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
					
					long idAtividade = BloqueioContingenciaGPP.bloqueioServicoCrm(servidor, porta, msisdn);
					// Fim do código de acesso ao GPP
					
					
					ContingenciaCrm contingenciaCrm = ContingenciaCrmHome.findByAtividade(db,idAtividade);
						if (contingenciaCrm == null) 
						{
							contingenciaCrm = new ContingenciaCrm();
							contingenciaCrm.setIdAtividade(idAtividade);
							contingenciaCrm.setIdOperacao(request.getParameter("motivoBloqueio"));
							contingenciaCrm.setMsisdn(msisdn);
							contingenciaCrm.setDatAtividade(new java.util.Date());
							contingenciaCrm.setIdAtendente(usuario.getMatricula());
							BloqueioStatus bloqueioStatus = ContingenciaCrmHome.findByBloqueioStatusId(db,Definicoes.STATUS_BLOQUEIO_SOLICITADO);
							contingenciaCrm.setIdStatus(bloqueioStatus);
							db.create(contingenciaCrm);
												
							// se o motivo de bloqueio for roubo, inserir dados referentes ao BO policial
							if (!request.getParameter("motivoBloqueio").equals("02")) 
							{
								DadosBoCrm dadosBo = new DadosBoCrm();
								dadosBo.setIdAtividade(idAtividade);
								dadosBo.setDatBo(dat.parse(request.getParameter("datBo")));
								dadosBo.setNumBo(request.getParameter("numBo"));
								db.create(dadosBo);
							}
							request.setAttribute(Constantes.MENSAGEM, "Bloqueio realizado com sucesso");
						}
						else request.setAttribute(Constantes.MENSAGEM, ""+request.getParameter("msisdn")+"já se encontra no Banco de Dados");
				}
		}
			
			catch (Exception e)
			{
				logger.error("Não foi possível bloquear o assinante em modo de contingência, problemas na conexão com o banco de dados (" +
										e.getMessage() + ")");
				throw e;
			}
			
			result = actionMapping.findForward("success");
			return result;
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}

}
