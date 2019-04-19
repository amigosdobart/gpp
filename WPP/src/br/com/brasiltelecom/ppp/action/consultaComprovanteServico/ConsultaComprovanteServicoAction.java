/*
 * Created on 19/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaComprovanteServico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AdministrativoTipoExtratoHome;
import br.com.brasiltelecom.ppp.home.ConsultaHistoricoExtratoHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaExtratoGPP;
import br.com.brasiltelecom.ppp.model.RetornoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.EventoAprovisionamento;
import br.com.brasiltelecom.ppp.portal.entity.HistoricoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.TipoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera comprovantes de serviço
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaComprovanteServicoAction extends ActionPortal 
{

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{
		ActionForward result = null;
		db.begin();
		this.codOperacao = Constantes.COD_CONSULTA_COMPROVANTE;

		//Obtendo os objetos necessarios para a consulta pelo Comprovante de Servicos.
		String msisdn			= request.getParameter("msisdn");
		String msisdnFormatado	= request.getParameter("msisdnFormatado");
		String tipo				= request.getParameter("tipo");
		String tipoPeriodo		= request.getParameter("tipoPeriodo");
		
		//Obtendo o diretorio e o nome para a criacao do arquivo que ira conter o XML do comprovante.
		ArrayList diretorios	= (ArrayList)servlet.getServletContext().getAttribute(Constantes.DIRETORIO_COMPROVANTES);
		String sessionId		= request.getSession().getId();
		
		logger.info("Consulta a um comprovante de serviço solicitada: " + msisdn);
			
		Date dataInicial = null;
		Date dataFinal = new Date();
		if (tipoPeriodo != null)
		{
			if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo")))
			{
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
				dataInicial = c.getTime();
			} 
			else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal")))
			{
				dataInicial = sdf.parse(request.getParameter("dataInicial"));
				dataFinal = sdf.parse(request.getParameter("dataFinal"));
			}
		}
		
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		RetornoExtrato resultSet = ConsultaExtratoGPP.getExtratos(msisdn, sdf.format(dataInicial), sdf.format(dataFinal), servidor, porta, diretorios, sessionId);
		
		boolean ehLigMix = false; 
		if (Integer.parseInt(resultSet.getIndAssinanteLigMix())== 1)
		{
			ehLigMix = true;
		}
			
		if("pdf".equalsIgnoreCase(tipo))
		{
			String url = "MS=" + msisdn + 
								"+DI=" + sdf.format(dataInicial) + 
								"+DF=" + sdf.format(dataFinal) + 
								"+CS=+" + 
								"+NM=+" + 
								"+EM=+" + 
								"+LG=+" + 
								"+CP=+" + 
								"+BR=+" +
								"+CD=+" +  
								"+UF=+" + 
								"+CE=+" ;
									
			if (!ehLigMix) // TODO: ligmix - Marcos C. Magalhães 11/07/2005
			{
				request.setAttribute("endereco","../reports/rwservlet?CRM+"+url);
			}
			else
			{
				request.setAttribute("endereco","../reports/rwservlet?CRML+"+url);
			}
					
			return actionMapping.findForward("redirect");					
		}

		
		if (resultSet.getExtratos().size() > 0)
		{
			Usuario u = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);

			HistoricoExtrato he = new HistoricoExtrato();
			he.setMsisdn(msisdn);
			he.setCobrado(0);
			he.setData(new Date());
			he.setEmissor("PPP");
			he.setUsuario(u.getMatricula());
			
			try
			{
				TipoExtrato te = AdministrativoTipoExtratoHome.findByID(db,4);
				he.setTipoExtrato(te);				
				ConsultaHistoricoExtratoHome.criarOrigem(he,db);
				
				db.commit();
				db.begin();
				
				EventoAprovisionamento ea = new EventoAprovisionamento();
				ea.setMsisdn(msisdn);
				ea.setDataAprovisionamento(new Date());
				ea.setTipoOperacao("EMISSAO_EXTRATO");
				ea.setCodigoRetorno(new Integer(0));
				ea.setNomeOperador(u.getNome().length()>30?u.getNome().substring(0,30):u.getNome());
				ea.setDescricaoStatus("SUCESSO");
				if ( he.getCobrado() != 0 ) //caso haja tarifação
				{
					ea.setValorCreditoInicial( Double.doubleToLongBits(te.getValor()) );
				}
				else
				{
					ea.setValorCreditoInicial(0);
				}
				
				ConsultaHistoricoExtratoHome.criarEventoAprovisionamento(ea, db);
				
			}
			catch (Exception e)
			{
				logger.error("Não foi possível realizar a consulta de comprovante de serviços, problemas na conexão com o banco de dados (" +
								e.getMessage() + ")");
				throw e;
			}
			
			String mensagem = "Consulta ao comprovante de serviços para o número de acesso '" + msisdnFormatado + "' realizada com sucesso!";
			
			request.setAttribute(Constantes.RESULT	, resultSet);
			request.setAttribute("msisdn"			, msisdn);
			request.setAttribute("msisdnFormatado"	, msisdnFormatado);
			request.setAttribute("dataInicial"		, sdf.format(dataInicial));
			request.setAttribute("dataFinal"		, sdf.format(dataFinal));
			request.setAttribute(Constantes.MENSAGEM, mensagem);
			
			result = actionMapping.findForward("success");
		}
		else 
		{
			request.setAttribute("imagem", "img/tit_ger_comp_serv.gif");
			request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para o número de acesso: "+ msisdnFormatado);
			result = actionMapping.findForward("nenhumRegistro");	
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
