package br.com.brasiltelecom.ppp.action.enviaSMS;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Carrega a lista de destinatários para a mensagem de sms 
 * 
 * @author Marcelo ALves Araujo
 * @since 14/07/2005
 */
public class CarregaListaDestinosAction extends ActionPortal 
{
	/********Atributos da Classe*******/
	private String codOperacao = Constantes.COD_BROADCAST_SMS;
	Logger logger = Logger.getLogger(this.getClass());
	/**********************************/

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{		
		logger.info("Carregando Lista de Destinatários");

		String resultado = "success";
		
		if((resultado = carregaLista(request)) == "success")
			request.setAttribute(Constantes.MENSAGEM, "Arquivo com dados dos destinatarios carregado no servidor!");
		else
			request.setAttribute(Constantes.MENSAGEM, "Arquivo vazio ou inexistente!");
								
		return actionMapping.findForward(resultado);
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
	
	public String carregaLista(HttpServletRequest request)
	{
		String resultado = "success";
		try
		{
			//Testa se o arquivo existe
			if(FileUpload.isMultipartContent(request))
			{
				//Carrega o arquivo
				DiskFileUpload upload = new DiskFileUpload();
				List lista = upload.parseRequest(request);
				//Percorre a lista de itens do arquivo
				Iterator i = lista.iterator();
				while (i.hasNext()) 
				{
					FileItem item = (FileItem) i.next();
				    // Salva em outro arquivo o que foi lido
				    if(!item.isFormField())
				    {
				    	InputStream file = item.getInputStream();
				    	if (file.available() > 0)
				    	{
					    	FileOutputStream output = new FileOutputStream(servlet.getServletContext().getRealPath("/WEB-INF")+java.io.File.separator+"smsDestino.txt");
					    	
					    	byte buffer[] = new byte[1024];
					    	int c = 0;
					    	// Grava o arquivo no servidor para leitura
					    	while((c = file.read(buffer)) > 0)
					    		output.write(buffer, 0, c);
					    	
					    	file.close();
					    	output.close();
				    	}
				    	else
				    		resultado = "error";
				    }
				    //Pega a mensagem a ser enviada
				    else
				    	if(item.getFieldName().equals("sms"))
				    		request.setAttribute("sms",item.getString());
				}
			}				
		}
		catch (FileUploadException e) 
		{
			resultado = "error";
		} 
		catch (IOException e) 
		{
			resultado = "error";
		}
		
		return resultado;
	}
}
