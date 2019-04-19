package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.EstornoExpurgoPulaPulaHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Aprova lote de Estorno/Expurgo de Bonus Pula-Pula.
 * 
 * @author Bernardo Vergne Dias
 * @since 20/12/2006
 */
public class AprovaPreviaEstornoExpurgoAction extends ActionPortal 
{
	private String codOperacao = Constantes.COD_APROVAR_PREVIA;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método principal da classe, reponsável pela aprovação do lote
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception 
	{
		ActionForward result = null;
		result = actionMapping.findForward("success");
		String mensagem = "";
		
		try 
		{	
			// Aprova o lote
			
			EstornoExpurgoPulaPulaHome.aprovarLote(servlet, request.getParameter("lote"));
			mensagem = "Lote '" + request.getParameter("lote") + "' aprovado com sucesso!";
			
			// Gera arquivo de log
			
			try 
			{	
				String raizEstornoExpurgo = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_ESTORNO_EXPURGO_PP));
				Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));	
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				
				String urlArquivo = raizEstornoExpurgo + java.io.File.separator + 
					Constantes.PATH_ESTORNO_EXPURGO_PP_HISTORICO + java.io.File.separator + "historico" +
					sdf.format(Calendar.getInstance().getTime()) + "LoteAprovado.txt";
				
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(urlArquivo)));

				out.println("LOTE:\t" + request.getParameter("lote"));
				out.println("AÇÃO:\tAprovado");
				out.println("USUÁRIO:\t" + usuario.getNome() + " (" + usuario.getMatricula() + ")");
				out.println("DATA:\t" + sdf2.format(Calendar.getInstance().getTime()));
				
				out.flush();
				out.close();

			}
			catch (Exception e)
			{
				logger.error("Erro ao gerar o arquivo de histórico de aprovação de prévia. " + e.getMessage());	
				mensagem += "<br>Erro ao gerar o arquivo de histórico.";
				result = actionMapping.findForward("error");
			} 
		}
		catch (Exception e)
		{
			logger.error("Erro ao aprovar lote de Estorno/Expurgo PulaPula");	
			mensagem = "Erro ao aprovar lote de Estorno/Expurgo Pula-Pula";
			result = actionMapping.findForward("error");
		} 

		request.setAttribute("mensagem", mensagem);
		return result;
		
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

