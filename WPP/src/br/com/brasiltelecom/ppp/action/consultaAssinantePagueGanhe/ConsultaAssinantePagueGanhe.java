package br.com.brasiltelecom.ppp.action.consultaAssinantePagueGanhe;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import com.brt.gpp.aplicacoes.campanha.entidade.NPGInfosBonificacao;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.CampanhaHome;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteCampanha;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua a consulta de TSD, Campanhas Promocionais, Recarga e Bônus
 * @author	Geraldo Palmeira
 * @since	01/11/2006
 */
public class ConsultaAssinantePagueGanhe extends ActionPortal
{
	Logger logger = Logger.getLogger(this.getClass());
	
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception 
	{
		ActionForward result = null;
		
		logger.info("Consulta status do Assinante Promoção Pague e Ganhe");
		
		try
		{			
			// Consulta as informações dos assinantes campanha
			db.begin();
						
			// Pega o parâmetro passado pela tela do portal
			String msisdn 		= request.getParameter("msisdn");
			long   idCampanha	= Constantes.ID_CAMPANHA_PAGUE_GANHE;	
			
			// Monta o objeto assinanteCampanha
			AssinanteCampanha assinanteCampanha = CampanhaHome.findByAssinanteCampanha(msisdn, idCampanha, db);
			// Monta o objeto atributosCampanha
			NPGInfosBonificacao atributosCampanha = NPGInfosBonificacao.newInstance(assinanteCampanha.getXmlDocument());
			// Monta o objeto configuracao
			Configuracao configuracao = ConfiguracaoHome.findByID(db,Constantes.ID_CONFIG_CAMPANHA_NPG);
			
			// Verifica a campanha ou se o assinante está na campanha 
			if (assinanteCampanha == null)
			{
				result = actionMapping.findForward("error");
				request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para este assinante");
			}
			else
			{
				// Calcula quantos dias faltam para o assinante receber o bônus
				int dias = (int) (((new Date()).getTime()) - (atributosCampanha.getDataSubidaTSD().getTime()));
				int numDiasBonus = Integer.parseInt(configuracao.getVlrConfiguracao()) - (dias / (3600*24*1000));
				Integer diasEsperaBonus = new Integer(numDiasBonus);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				request.setAttribute("assinanteCampanha", assinanteCampanha);
				request.setAttribute("atributosCampanha", atributosCampanha);
				request.setAttribute("conversorData"	, sdf);
				request.setAttribute("diasEsperaBonus"	, diasEsperaBonus);
				result = actionMapping.findForward("success");
			}
			
		} 
		catch (Exception e)
		{
			logger.error("Erro ao consultar status do Assinante Promoção Pague e Ganhe (" +
							e.getMessage() + ")");
			
				
			result = actionMapping.findForward("error");
			
			request.setAttribute(Constantes.MENSAGEM, "Erro ao consultar status do Assinante Promoção Pague e Ganhe (" +
					e.getMessage() + ")");
			
			if (e.getMessage() == null)
			{
				request.setAttribute(Constantes.MENSAGEM, "Nenhum registro encontrado para este assinante");
			}
		}
		
		return result; 
	}
	
	/**
	 * Retorna o código da operação
	 * @return String
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_ASS_NPG;
	}
}