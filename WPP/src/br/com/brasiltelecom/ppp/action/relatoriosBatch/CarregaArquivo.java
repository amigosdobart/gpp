package br.com.brasiltelecom.ppp.action.relatoriosBatch;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.SolicitacoesRelatorioHome;
import br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class CarregaArquivo extends ActionPortal
{	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db)throws Exception 
	{
		String resultado = Constantes.STATUS_SUCESSO;
		String operador = request.getParameter("operador");
		try
		{
			logger.info("Carrega Relatorio Batch");
			
			db.begin();
			// Busca nome do arquivo de acordo com o id
			SolicitacoesRelatorio registro = SolicitacoesRelatorioHome.findByID(db, new Integer(request.getParameter("id")).intValue());
			
			if(registro != null)
			{
				// Testa se operador é igual ao operador que solicitou o relatório
				// Testa se o usuário logado corresponde ao que solicitou o relatório
				if(	operador.equals(registro.getOperador()) && 
					operador.equals(((Usuario)request.getSession().getAttribute(Constantes.LOGIN)).getMatricula()))
				{
					// Conecta à URL para pegar o arquivo
					URL link = new URL(	getServlet().getServletContext().getAttribute(Constantes.DIRETORIO_RELATORIOS)+
										registro.getNomeArquivo()+
										Constantes.EXTENSAO_ZIP);
					URLConnection conn = link.openConnection();
					// Cria um buffer para armazenar o arquivo
					BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
					// Stream para onde salvar o arquivo
					OutputStream out = response.getOutputStream();
					
					byte buffer[] = new byte[1024];
					int cont = 0;
					// Cria uma cópia do arquivo
					while( (cont=in.read(buffer)) > 0)
						out.write(buffer, 0, cont);
					
					out.flush();
					in.close();
					out.close();
					db.commit();
					db.close();
				}
				else
				{
					resultado = Constantes.STATUS_ERRO;
					request.setAttribute(Constantes.MENSAGEM, "O usuário "+ operador + " não tem accesso a este relatório");
					return actionMapping.findForward(resultado);
				}
			}
			else
			{
				resultado = Constantes.STATUS_ERRO;
				request.setAttribute(Constantes.MENSAGEM, "Relatório inexistente.");
				return actionMapping.findForward(resultado);
			}
		} 
		catch (DatabaseNotFoundException e)
		{
			logger.error("Erro ao conectar com o banco de dados (" +e.getMessage() + ")");
			request.setAttribute(Constantes.MENSAGEM, "Erro ao conectar com o banco de dados.");
			resultado = Constantes.STATUS_ERRO;
			return actionMapping.findForward(resultado);
		} 
		catch (PersistenceException e)
		{
			logger.error("Erro ao tentar acessar o banco de dados (" +e.getMessage() + ")");
			request.setAttribute(Constantes.MENSAGEM, "Erro ao tentar acessar o banco de dados.");
			resultado = Constantes.STATUS_ERRO;
			return actionMapping.findForward(resultado);
		}	
		catch (FileNotFoundException e)
		{
			logger.error("Arquivo inexistente (" +e.getMessage() + ")");
			request.setAttribute(Constantes.MENSAGEM, "Erro: O arquivo não existe.");
			resultado = Constantes.STATUS_ERRO;
			return actionMapping.findForward(resultado);
		}
		
		return null;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}