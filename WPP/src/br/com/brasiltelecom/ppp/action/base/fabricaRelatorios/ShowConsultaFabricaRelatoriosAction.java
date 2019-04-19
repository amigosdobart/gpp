package br.com.brasiltelecom.ppp.action.base.fabricaRelatorios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.OperacaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.entity.Operacao;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.Arquivo;
import br.com.brasiltelecom.ppp.util.ArquivoComparatorDataCriacao;
import br.com.brasiltelecom.ppp.util.ArquivoComparatorNome;
import br.com.brasiltelecom.ppp.util.ArquivoComparatorTamanho;
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;

/**
 * 
 * Essa classe é apenas uma engine 
 * genéria para consulta de arquivos gerados pela Fábrica de Relatórios
 * 
 * Mostra a página de acesso os relatórios
 * possibilitando ao usuário fazer download (ENTRADA / SAIDA) 
 * e upload (ENTRADA).
 * 
 * É necessário criar uma classe derivada com os parâmetros
 * do relário em consulta. 
 * 
 * @author Bernardo Vergne Dias
 * @since 04/12/2006
 */
public abstract class ShowConsultaFabricaRelatoriosAction extends ShowAction 
{	

	/* Constantes de ordenamento */
	public static final String ORDENAR_NOME 		= "nome";
	public static final String ORDENAR_DATA_CRIACAO = "data";
	public static final String ORDENAR_TAMANHO 		= "tamanho";
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private String pastaRelatorio;		// pasta relativa do relatorio
	private String pastaInterna;		// pasta interna do relatorio (ENTRADA/SAIDA)
	
	private String ordenarPor;				// campo de ordenação da listagem de arquivos
	private boolean ordenarAscendente;	// ordem ascendente da listagem de arquivos
	
	private boolean downloadEntrada;	// indica se o usuario tem permissao para download na pasta entrada
	private boolean downloadSaida;		// indica se o usuario tem permissao para download na pasta saida
	private boolean uploadEntrada;		// indica se o usuario tem permissao para upload na pasta entrada
	
	/**
	 * Método para pegar a Tela Principal do Usuário.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaFabricaRelatorios.vm";
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
		
		try
		{
			recuperaCamposRequest(context, request, db);
		}
		catch (PersistenceException e2)
		{
			request.setAttribute("mensagem", "Erro ao acessar o banco de dados.");
			logger.error(e2.getMessage());	
		}
		
		recuperaPermissoes(context, request);
		listaArquivos(context, request);
		
		// Salva alguns parametros gerais e constantes
		request.getSession().setAttribute("parametrosFabricaRelatorios", getParametros());
		request.getSession().setAttribute("codOperacao", getOperacao());
		context.put("parametros", getParametros());
		context.put("ordenarPorNome", ORDENAR_NOME);
		context.put("ordenarPorTam", ORDENAR_TAMANHO);
		context.put("ordenarPorData", ORDENAR_DATA_CRIACAO);
		context.put("pastaInternaEntrada", Constantes.PATH_FABRICA_REL_ENTRADA);
		context.put("pastaInternaSaida", Constantes.PATH_FABRICA_REL_SAIDA);
		
	}

	/**
	 * Recupera os campos de formularios passados no request (GET/POST)
	 */
	 private void recuperaCamposRequest(VelocityContext context, HttpServletRequest request, Database db) throws PersistenceException
	 {
		 ordenarPor = ORDENAR_NOME;
		 if (request.getParameter("ordenarPor") != null) 
			 ordenarPor = request.getParameter("ordenarPor");
		 
		 ordenarAscendente = true;
		 if (request.getParameter("ordenarAscendente") != null) 
			 ordenarAscendente = request.getParameter("ordenarAscendente").equals("true") ? true : false;
			
		 pastaInterna = Constantes.PATH_FABRICA_REL_ENTRADA;
		 if (request.getParameter("pastaInterna") != null) 
				pastaInterna = request.getParameter("pastaInterna");
		 
		 /*
	     * Determina o diretorio do relatorio
	     * Primeiro verifica se a pasta do relatorio foi passada por POST a partir da 
		 * página que lista os relatorios disponiveis.
		 * Se o POST nao existir o metodo determinaPastaRelatorio() fará a busca
		 * dessa informacao na sessao e/ou no banco de dados
	     */
		 if (request.getParameter("pastaRelatorio") != null) 
			pastaRelatorio = request.getParameter("pastaRelatorio");
		 else
			pastaRelatorio = determinaPastaRelatorio(request, db);
		 
		 // Salva os parametros
		 context.put("pastaInterna", pastaInterna);
		 context.put("ordenarPor",""+ ordenarPor);
		 context.put("ordenarAscendente", (ordenarAscendente ? "true" : "false"));
		 request.getSession().setAttribute("urlOperacao", pastaRelatorio);
	 }
	 
    /**
     * Determina as permissoes do usuario para Upload e Download (entrada e saida)
     */
    private void recuperaPermissoes(VelocityContext context, HttpServletRequest request)
    {
    	// Busca o usuário do portal
		Usuario login = (Usuario) request.getSession().getAttribute(Constantes.LOGIN);
		
		// Lista de grupos ao qual pertence o usuário
		Iterator itGrp = login.getGrupos().iterator();
		
		// Captura as acoes permitidas ao usuario
		downloadEntrada = false;
		downloadSaida = false;
		uploadEntrada = false;
		
		while(itGrp.hasNext())
		{
			Iterator itOp = ((Grupo)itGrp.next()).getOperacoes().iterator();
			while(itOp.hasNext())
			{
				Operacao opr = (Operacao)itOp.next();
				if((opr).getNome().equals(getParametros().getDownloadEntrada()))
				{
					downloadEntrada = true;
				}
				if((opr).getNome().equals(getParametros().getDownloadSaida()))
				{
					downloadSaida = true;
				}
				if((opr).getNome().equals(getParametros().getUploadEntrada()))
				{
					uploadEntrada = true;
				}
			}
		}	
		
		context.put("uploadEntrada", (uploadEntrada == true) ? "true" : "false");
    }
    
    /**
     * Tenta recuperar a pasta do Relatorio a partir da sessao 
     * (pois o POST é gravado em sessao).
	 * Se a sessao nao existir, a pasta do relatorio é adquirida 
	 * do banco de dados OPERACOES.
     */
    private String determinaPastaRelatorio(HttpServletRequest request, Database db) throws PersistenceException
    {
    	if (request.getSession().getAttribute("urlOperacao") != null) 
		{
			return (String)(request.getSession().getAttribute("urlOperacao"));
		} 
		else 
		{
			db.begin();
			String temp = (OperacaoHome.findByNome(db, getOperacao())).getUrl();
			db.commit();
			db.close();
			return temp;
		}	
    }

    /**
     * Lista os arquivos para exibição na VM
     */
    private void listaArquivos(VelocityContext context, HttpServletRequest request)
    {
    	try
    	{
			// Verifica permissão para listagem e captura a lista de arquivos
			if ((pastaInterna.equals(Constantes.PATH_FABRICA_REL_ENTRADA) && !downloadEntrada) || 
				(pastaInterna.equals(Constantes.PATH_FABRICA_REL_SAIDA) && !downloadSaida))
			{
				request.setAttribute("mensagem", "Você não tem permissão para acessar os arquivos da pasta " +
					(pastaInterna.equals(Constantes.PATH_FABRICA_REL_ENTRADA) ? "'Arquivos disponibilizados'" : "'Relatórios'") + ".");
			} 
			else	
			{
				//Lista os arquivos para impressao no template			
				String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_FABRICA_REL));
				String dir = raiz + java.io.File.separator + pastaRelatorio + java.io.File.separator + pastaInterna;
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
    	}
    	catch (Exception e)
    	{
    		request.setAttribute("mensagem", "Erro ao acessar o sistema de arquivos.");
    	}
    }
    
	/**
	 * Método abstrato que retorna a lista de parametros do relatorio
	 *
	 * @return Um objeto do tipo ParametrosFabricaRelatorios, que representa os parametros do relatorio.
	 */
    public abstract ParametrosFabricaRelatorios getParametros();
}
