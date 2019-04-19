package br.com.brasiltelecom.wig.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import br.com.brasiltelecom.wig.entity.MultPartContent;

/**
 * Esta classe realiza o parse do conteudo multpart que estah sendo enviado
 * na requisicao http. Nste conteudo tambem eh feito o parse dos campos de
 * formulario, sendo incluidos num hash para posterior pesquisa.
 *  
 * @author joao.lemgruber
 *
 */
public class MultPartContentParser
{
	/**
	 * Metodo....:parse
	 * Descricao.:Realiza o parse do conteudo sendo enviado para o servidor
	 *            separando a informacao do arquivo que estah sendo feito
	 *            o upload dos parametros de formulario que veem no mesmo
	 *            request http
	 * @param request	- Request http
	 * @return MultPartContent - Objeto contendo a separacao dos campos
	 * @throws Exception
	 */
	public static MultPartContent parse(HttpServletRequest request) throws Exception
	{
		MultPartContent content = new MultPartContent();
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
				    	// Atualiza o objeto MultPart para inserir o valor do conteudo enviado
				    	content.setStreamArquivo(new BufferedReader(new InputStreamReader(file)));
				    	file.close();
			    	}
			    }
			    //Pega a mensagem a ser enviada
			    else
			    	// Atualiza a informacao de campo de formulario
			    	// no objeto MultPartContent sendo criado
			    	content.addCampo(item.getFieldName(), item.getString());
			}
		}				
		return content;
	}
}
