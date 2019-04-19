package br.com.brasiltelecom.ppp.action.cadastroPrecoAparelho;

import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Salva o arquivo 
 * @author Marcelo Alves Araujo
 * @since 13/11/2006
 */
public class SalvaArquivoErroAction extends ActionPortal
{
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CADASTRAR_PRECO_APARELHO;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		FileInputStream arquivoErro = new FileInputStream(servlet.getServletContext().getRealPath("/WEB-INF")+java.io.File.separator + Constantes.ARQUIVO_ERRO_IMPORTACAO + Constantes.EXTENSAO_TXT);
		
		if(arquivoErro.available() > 0)
		{
			// Configura os parâmetros para o arquivo
			String mimeSettings = "application/zip; name=\"" + Constantes.ARQUIVO_ERRO_IMPORTACAO + Constantes.EXTENSAO_TXT + "\"";
			response.setHeader("Content-disposition", "attachment; filename=" + Constantes.ARQUIVO_ERRO_IMPORTACAO + Constantes.EXTENSAO_ZIP);
			response.setContentType(mimeSettings);
			
			// Cria o arquivo zip
			ZipOutputStream bufferSaida = new ZipOutputStream(response.getOutputStream());
			ZipEntry entry = new ZipEntry(Constantes.ARQUIVO_ERRO_IMPORTACAO + Constantes.EXTENSAO_TXT);
			bufferSaida.putNextEntry(entry);
			
			byte buffer[] = new byte[1024];
	    	int c = 0;
	    	
	    	// Salva o arquivo para o usuário
	    	while((c = arquivoErro.read(buffer)) > 0)
	    		bufferSaida.write(buffer, 0, c);
			
	    	bufferSaida.flush();
	    	bufferSaida.close();			
			arquivoErro.close();
			
			return actionMapping.findForward(Constantes.STATUS_SUCESSO);
		}
			
		request.setAttribute(Constantes.MENSAGEM, "Lista de Preços Carregada com Sucesso!");		
		request.setAttribute("imagem", "img/tit_cadastro_preco_aparelho.gif");
		
		return actionMapping.findForward(Constantes.STATUS_ERRO);
	}	
}