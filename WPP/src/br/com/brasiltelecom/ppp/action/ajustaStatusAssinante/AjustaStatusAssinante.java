package br.com.brasiltelecom.ppp.action.ajustaStatusAssinante;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.CodigosRetornoDao;
import br.com.brasiltelecom.ppp.interfacegpp.AprovisionamentoGPP;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class AjustaStatusAssinante extends ActionPortalHibernate 
{
	private String codOperacao = Constantes.COD_AJUSTA_STATUS_ASSINANTE;
	public String getTela() 
	{
		return null;
	}

	public String getOperacao() 
	{
		return codOperacao;
	}

	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory)
			throws Exception 
	{
		Logger logger = Logger.getLogger(AjustaStatusAssinante.class);
		String msisdn = "55" + request.getParameter("msisdn");
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		Usuario operador = (Usuario)request.getSession().getAttribute(Constantes.LOGIN);
		
		if(request.getParameter("acao")!=null)
		{
			StringBuffer mensagem = new StringBuffer();
			StringBuffer msglog = new StringBuffer();
			Session session = null;
			try
			{
				session = sessionFactory.getCurrentSession();
				session.beginTransaction();
				
				String dataAssinante = request.getParameter("dataAssinante");
				String dataAssinanteAntigo = request.getParameter("dataAssinanteAntigo");
				String dataPeriodico = request.getParameter("dataPeriodico");
				String dataPeriodicoAntigo = request.getParameter("dataPeriodicoAntigo");
				String statusAssinante = request.getParameter("statusAssinante");
				String statusAssinanteAntigo = request.getParameter("statusAssinanteAntigo");
				String statusPeriodico = request.getParameter("statusPeriodico");
				String statusPeriodicoAntigo = request.getParameter("statusPeriodicoAntigo");
				if(dataAssinante!=null && dataAssinanteAntigo!=null && statusAssinante!=null && statusAssinanteAntigo!=null)
				{
					if(!dataAssinante.equals(dataAssinanteAntigo) || !statusAssinante.equals(statusAssinanteAntigo))
					{
						short resultado = AprovisionamentoGPP.alteraStatusAssinante(msisdn, Short.parseShort(statusAssinante), dataAssinante, operador.getMatricula(), servidor, porta);
						if(resultado!=0)
						{
							CodigosRetorno codigoRetorno = CodigosRetornoDao.findByVlr(session, String.valueOf(resultado));
							mensagem.append(codigoRetorno.getDescRetorno());
							mensagem.append(".\\n");
						}
						else
						{
							msglog.append("Status do assinante modificado.(MSISDN:");
							msglog.append(msisdn);
							msglog.append(",StatusNovo:");
							msglog.append(statusAssinante);
							msglog.append(",DataExpiraçãoNovo:");
							msglog.append(dataAssinante);
							msglog.append(",StatusAntigo:");
							msglog.append(statusAssinanteAntigo);
							msglog.append(",DataExpiraçãoAntigo:");
							msglog.append(dataAssinanteAntigo);
							msglog.append("). ");
							logger.info(msglog.toString());
							mensagem.append("Status do assinante modificado com sucesso.\\n");
						}
					}
				}
				
				if(dataPeriodico!=null && dataPeriodicoAntigo!=null && statusPeriodico!=null && statusPeriodicoAntigo!=null)
				{
					if(!dataPeriodico.equals(dataPeriodicoAntigo) || !statusPeriodico.equals(statusPeriodicoAntigo))
					{
						short resultado = AprovisionamentoGPP.alteraStatusPeriodico(msisdn, Short.parseShort(statusPeriodico), dataPeriodico, operador.getMatricula(), servidor, porta);
						if(resultado!=0)
						{
							CodigosRetorno codigoRetorno = CodigosRetornoDao.findByVlr(session, String.valueOf(resultado));
							mensagem.append(codigoRetorno.getDescRetorno());
							mensagem.append(".");
						}
						else
						{
							StringBuffer msg = new StringBuffer();
							msg.append("Status do saldo periódico modificado.(MSISDN:");
							msg.append(msisdn);
							msg.append(",StatusNovo:");
							msg.append(statusPeriodico);
							msg.append(",DataExpiraçãoNovo:");
							msg.append(dataPeriodico);
							msg.append(",StatusAntigo:");
							msg.append(statusPeriodicoAntigo);
							msg.append(",DataExpiraçãoAntigo:");
							msg.append(dataPeriodicoAntigo);
							msg.append("). ");
							msglog.append(msg);
							logger.info(msg.toString());
							mensagem.append("Status do saldo de franquia modificado com sucesso.");
						}
					}
				}
		
				if(mensagem.length()!=0)
				{
					request.setAttribute("mensagemAlerta", mensagem.toString());
				}
				if(msglog.length()!=0)
				{
					request.setAttribute("mensagem", msglog.toString());
				}
				
				session.getTransaction().commit();
			}
			catch(Exception e)
			{
				if(session!=null)
					session.getTransaction().rollback();
				logger.error("Erro na atualização dos status do assinante.",e);
				request.setAttribute("mensagem","Falha na mudança do status do assinante.");
			}
		}

		Assinante assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn, servidor, porta);
		request.setAttribute("assinante", assinante);
		
		return actionMapping.findForward("redirect");
	}

}
