package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.ArquivoComparatorDataCriacao;
import br.com.brasiltelecom.ppp.util.ArquivoComparatorNome;
import br.com.brasiltelecom.ppp.util.ArquivoComparatorTamanho;
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;

/**
 * 
 * Mostra a página de gerenciamento de arquivos do Estorno/Expurgo de Bonus Pula-Pula.
 * 
 * Nessa página o usuario pode fazer upload de arquivo de lotes e visualizar:
 * 
 * 	-> Arquivos disponibilizados (mas nao processados pelo GPP)
 * 	-> Arquivos processados
 *  -> Erros (erros no processo de carga dos lotes do DB)
 *  -> Previas (resultados da previa de estorno/expurgo)
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class ShowArquivosEstornoExpurgoPPAction extends ShowAction 
{	
	
	/* Constantes de ordenamento */
	public static final String ORDENAR_NOME 		= "nome";
	public static final String ORDENAR_DATA_CRIACAO = "data";
	public static final String ORDENAR_TAMANHO 		= "tamanho";
	
	private String ordenarPor;				// campo de ordenação da listagem de arquivos
	private boolean ordenarAscendente;		// ordem ascendente da listagem de arquivos
	private String pasta;
	
	private boolean permissaoUpload;
	private boolean permissaoDownload;
	
	private String codOperacao = Constantes.MENU_GERENCIAMENTO_ARQ;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método para pegar a Tela Principal do Usuário.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaArquivosEstornoExpurgoPP.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) throws Exception
	{
		
		// Salva mensagem oriunda de um redirecionamento (upload, por exemplo)
		context.put("mensagem2", (String) request.getAttribute("mensagem"));
		request.removeAttribute("mensagem");
		
		recuperaCamposRequest(context, request);
		recuperaPermissoes(context, request);
		listaArquivos(context, request);
		
		// Salva algumas constantes
		context.put("ordenarPorNome", ORDENAR_NOME);
		context.put("ordenarPorTam", ORDENAR_TAMANHO);
		context.put("ordenarPorData", ORDENAR_DATA_CRIACAO);
		context.put("pastaEntrada", Constantes.PATH_ESTORNO_EXPURGO_PP_ENTRADA);
		context.put("pastaSaida", Constantes.PATH_ESTORNO_EXPURGO_PP_SAIDA);
		context.put("pastaErros", Constantes.PATH_ESTORNO_EXPURGO_PP_ERROS);
		context.put("pastaPrevias", Constantes.PATH_ESTORNO_EXPURGO_PP_PREVIAS);
		context.put("pastaEfetivado", Constantes.PATH_ESTORNO_EXPURGO_PP_EFETIVADO);
		context.put("pastaHistorico", Constantes.PATH_ESTORNO_EXPURGO_PP_HISTORICO);
	}
	
	/**
	 * Recupera os campos de formularios passados no request (GET/POST)
	 */
	 private void recuperaCamposRequest(VelocityContext context, HttpServletRequest request)
	 {
		 ordenarPor = ORDENAR_NOME;
		 if (request.getParameter("ordenarPor") != null) 
			 ordenarPor = request.getParameter("ordenarPor");
		 
		 ordenarAscendente = true;
		 if (request.getParameter("ordenarAscendente") != null) 
			 ordenarAscendente = request.getParameter("ordenarAscendente").equals("true") ? true : false;
			
		 pasta = Constantes.PATH_ESTORNO_EXPURGO_PP_ENTRADA;
		 if (request.getParameter("pasta") != null) 
			pasta = request.getParameter("pasta");

		 // Salva os parametros
		 context.put("pasta", pasta);
		 context.put("ordenarPor",""+ ordenarPor);
		 context.put("ordenarAscendente", (ordenarAscendente ? "true" : "false"));
	 }

    /**
     * Determina as permissoes do usuario para Upload e Download
     */
    private void recuperaPermissoes(VelocityContext context, HttpServletRequest request)
    {
    	//  Busca o usuário do portal
		Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));	
		
		permissaoUpload = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_CARREGAR_ARQUIVO_DB) != null);
		permissaoDownload = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_OBTER_ARQUIVO) != null);
	
		context.put("permissaoUpload", (permissaoUpload == true) ? "true" : "false");
		context.put("permissaoDownload", (permissaoDownload == true) ? "true" : "false");
    }
	    
    /**
     * Lista os arquivos para exibição na VM
     */
    private void listaArquivos(VelocityContext context, HttpServletRequest request)
    {
    	try 
		{
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_ESTORNO_EXPURGO_PP));
			String dir = raiz + java.io.File.separator + pasta;
				
			ArrayList arquivos = GerenciadorArquivo.listaArquivos(dir);
			
			if (ordenarPor.equals(ORDENAR_NOME))
				Collections.sort(arquivos, new ArquivoComparatorNome());
				
			if (ordenarPor.equals(ORDENAR_DATA_CRIACAO))
				Collections.sort(arquivos, new ArquivoComparatorDataCriacao());
				
			if (ordenarPor.equals(ORDENAR_TAMANHO))
				Collections.sort(arquivos, new ArquivoComparatorTamanho());
			
			if (!ordenarAscendente) Collections.reverse(arquivos);
	
			context.put("arquivos", arquivos);
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "Erro ao acessar o sistema de arquivos.");
			logger.error("Erro ao listar os arquivos.");	
		}
    }
    
	/**
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
