package br.com.brasiltelecom.ppp.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

/**
 * 
 * Interpreta um formulario HTML do tipo Multipart
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class MultipartParser {

	/**
	 * Retorna uma collection com a lista dos campos do formulario.
	 * Cada campo é uma instancia de org.apache.commons.fileupload.FormItem.
	 * 
	 * O mapeamento é indexado pelo nome do campo (string).
	 * 
	 * @param request HttpServletRequest
	 * @return Instancia de HashMap
	 */
	public static HashMap parseStream(HttpServletRequest request)
	{
		HashMap map = new HashMap();
		
		try 
		{
			// Testa se o arquivo existe no formulario
			if (FileUpload.isMultipartContent(request)) 
			{
				// Carrega o arquivo
				List lista = (new DiskFileUpload()).parseRequest(request);
		
				// Percorre a lista de itens
				Iterator i = lista.iterator();
				while (i.hasNext()) 
				{
					FileItem item = (FileItem) i.next();
					map.put(item.getFieldName(), item);
				}
			}
		}
		catch (Exception e)
		{
		}
		
		return map;
	}
}
