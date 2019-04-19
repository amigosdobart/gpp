package br.com.brasiltelecom.ppp.action.recargaMassa;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;

/**
 * 
 * Mostra a página de gerenciamento de arquivos da Recarga em Massa.
 * 
 * Nessa página o usuario pode fazer upload de arquivo de lotes e visualizar:
 * 
 * 	-> Arquivos disponibilizados (mas nao processados pelo GPP)
 * 	-> Registros processados com sucesso
 *  -> Registros processados com erro
 * 
 * @author Bernardo Vergne Dias
 * Data: 08/08/2007
 */
public class ShowArquivosRecargaMassa extends ShowActionHibernate 
{		
	private String codOperacao = Constantes.MENU_ARQUIVOS_RECARGA_MASSA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "consultaArquivosRecargaMassa.vm";
	}

	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
			   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		// Salva mensagem oriunda de um redirecionamento (upload, por exemplo)
		context.put("mensagem2", (String) request.getAttribute("mensagem"));
		request.removeAttribute("mensagem");
		
		String pasta = (request.getParameter("pasta") == null) ? Constantes.PATH_RECARGA_MASSA_ENTRADA : request.getParameter("pasta");
		context.put("pasta", pasta);
		
		listaArquivos(context, request, pasta);
		
		// Salva algumas constantes
		context.put("ordenarPorNome", GerenciadorArquivo.ORDENAR_NOME);
		context.put("ordenarPorTam", GerenciadorArquivo.ORDENAR_TAMANHO);
		context.put("ordenarPorData", GerenciadorArquivo.ORDENAR_DATA_CRIACAO);
		context.put("pastaEntrada", Constantes.PATH_RECARGA_MASSA_ENTRADA);
		context.put("pastaSaida", Constantes.PATH_RECARGA_MASSA_SAIDA);
		context.put("pastaErros", Constantes.PATH_RECARGA_MASSA_ERROS);
	}

	/**
     * Lista os arquivos para exibição na VM
     */
    private void listaArquivos(VelocityContext context, HttpServletRequest request, String pasta)
    {
    	try 
		{
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_RECARGA_MASSA));
			String dir = raiz + java.io.File.separator + pasta;
				
			String ordenarPor = (request.getParameter("ordenarPor") == null) ? 
					GerenciadorArquivo.ORDENAR_NOME : request.getParameter("ordenarPor");
			boolean ordenarAscendente = (request.getParameter("ordenarAscendente") == null) ? 
					true : (request.getParameter("ordenarAscendente").equals("true") ? true : false);
			 
			ArrayList arquivos = GerenciadorArquivo.listaArquivos(dir, request, ordenarPor, ordenarAscendente);
			
			context.put("arquivos", arquivos);
			context.put("ordenarPor",""+ ordenarPor);
			context.put("ordenarAscendente", (ordenarAscendente ? "true" : "false"));
		}
		catch (Exception e)
		{
			logger.error("Erro ao listar os arquivos.");	
		}
    }
    
    /**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
