package br.com.brasiltelecom.ppp.action.consultaBlackList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.DescartaAssinanteHome;
import br.com.brasiltelecom.ppp.portal.entity.DescartaAssinante;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.MultipartParser;

/**
 * Carrega e valida Black List
 * 
 * @author Geraldo Palmeira
 * @since  09/01/2007
 */
public class ValidaBlackListAction extends ActionPortal 
{
	// Atributos da classe
	private String codOperacao = Constantes.COD_ATUALIZACAO_BLACK_LIST;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{		
		logger.info("Carregando Black List");

		String resultado = carregaLista(request, db, response);
		
		if (resultado.equals("success"))
			request.setAttribute(Constantes.MENSAGEM, "Arquivo com dados da Black List foi carregado no Banco de Dados com sucesso.");
		else if (resultado.equals("download")) 
			return null;
		else
			request.setAttribute(Constantes.MENSAGEM, resultado);
								
		return actionMapping.findForward(Constantes.MENSAGEM);
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
	
	public String carregaLista(HttpServletRequest request, Database db, HttpServletResponse response) throws PersistenceException 
	{
		String resultado = "success";
		String numeroErro = null;
		try
		{
			HashMap map = MultipartParser.parseStream(request);
			FileItem file = (FileItem)(map.get("arquivo"));
			
			BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line = "";
			StringBuffer buffer = new StringBuffer();
		
			//Inicia conexao com o banco
			db.begin();
			
			while((line = br.readLine()) != null) 
			{			
				if (line == null) 
					throw new Exception("Especifique um arquivo para upload!");
				
				if (line.length() == 0) 
					throw new Exception("Especifique um arquivo (não vazio) para upload!");
				
				String numero = line;
				numeroErro = numero;
				
				//Verifica se o registro a ser inserido ja existe na tabela
				if (DescartaAssinanteHome.findByMsisdn(db, numero) == null)
				{
					//Os numeros devem começar com 55 e ter 12 posições numericas
					if (numero.matches("^55[0-9]{10}$"))
					{
						
						//Monta o Objeto
						DescartaAssinante descartaAssinante = new DescartaAssinante();
						descartaAssinante.setIdtMsisdn(numero);
						descartaAssinante.setIndMascara(0);
						
						//Insere registro na tabela 
						DescartaAssinanteHome.setDescartaAssinante(db, descartaAssinante);
					}
					else
					{
						//Insere os registros que não passaram pela vaidação num arquivo de saída
						buffer.append(numero + " Numero fora do padrao\n");
					}
				}
				else
				{
					//Insere os registros que não passaram pela vaidação num arquivo de saída
					buffer.append(numero + " Numero ja exite\n");
				}
				
			}
			
			if (buffer.length() > 0)
			{
				//Realiza o download do arquivo de saída
				try 
				{	
					response.setHeader("Content-Description", "File Transfer");
					response.setHeader("Content-Type", "application/force-download");
					response.setHeader("Content-Length", String.valueOf(buffer.length()));
					response.setHeader("Content-Disposition", "attachment; filename=" + Constantes.ARQUIVO_ERRO_BLACK_LIST + ".txt");
				
					response.getOutputStream().write(buffer.toString().getBytes());
					response.getOutputStream().flush();
					
					resultado = "download";
				}
				catch (Exception e)
				{
					resultado = "Erro no processo de download do resultado do processamento.";
				}
			}
		}
		catch (IOException e) 
		{
			resultado = "Erro ao gravar o numero:"+ numeroErro + " no arquivo "+ Constantes.ARQUIVO_ERRO_BLACK_LIST+ " " + e;
		}
		catch (PersistenceException e) 
		{
			resultado = "Erro ao inserir o numero:"+ numeroErro + " " + e;
	    } 
		catch (Exception e) 
		{
			resultado = "Erro: " + e.getMessage();
	    } 
		finally
		{
			db.commit();
			db.close();
		}
		return resultado;
	}
}
