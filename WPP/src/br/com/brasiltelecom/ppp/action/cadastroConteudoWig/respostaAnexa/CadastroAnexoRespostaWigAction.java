package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.respostaAnexa;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.FiltroConteudoWigDAO;
import br.com.brasiltelecom.ppp.dao.FiltroWigDAO;
import br.com.brasiltelecom.ppp.dao.ModeloDAO;
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroCategoriaWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroConteudoWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroERBWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroModeloWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroMsisdnWig;
import br.com.brasiltelecom.ppp.portal.entity.FiltroRestricaoTempoWig;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Joao Carlos
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 12/03/2007
 */
public class CadastroAnexoRespostaWigAction extends ActionPortalHibernate
{
	private String codOperacao = Constantes.COD_CADASTRAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisição HTTP que originou a execução dessa ação.
	 * @param response			Resposta HTTP a ser encaminhada ao usuário.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		ActionForward forward = actionMapping.findForward("success");
		Session session = null;
		
		String msgRetorno = "Resposta incluída com sucesso!";
		int    codRetorno = 0;

		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        // Identifica os primeiros valores relativos a qual conteudo o anexo estah sendo incluido.
			ConteudoWig conteudo = new ConteudoWig();
			conteudo.setCodConteudo(Integer.parseInt(request.getParameter("codConteudo")));
			 
			boolean aplicaParalelo 	= new Boolean(request.getParameter("aplica")).booleanValue();
			int 	tipoResposta 	= Integer.parseInt(request.getParameter("tpFiltro"));
			int 	ordem        	= (request.getParameter("ordem")      != null && !"".equals(request.getParameter("ordem"))) 	  ? Integer.parseInt(request.getParameter("ordem")) : 0;
			boolean novaResposta    = ("".equals(request.getParameter("codResposta"))) ? true : false;	
			
			// Identifica os valores da resposta que estah sendo incluida.
			// Apesar de um conteudo poder conter mais de uma resposta, esta
			// tela permite que somente uma seja feita por vez
			RespostaWig resposta = null;
			
			if (novaResposta)
				resposta = new RespostaWig();
			else
				resposta = RespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codResposta")));
	
			resposta.setNomeResposta		(request.getParameter("nomeResposta"));
			resposta.setDescricaoResposta	(getDescricaoResposta(request,tipoResposta));
			
			// O filtro eh adicionado a uma lista pois um anexo de resposta pode
			// possuir varios filtros. 
			Collection filtros = new ArrayList();
			
			// Define o FiltroPorMsisdn, buscando alem da configuracao da aplicacao
			// do filtro, os valores de mascara msisdn que deverao ser utilizadas.
			FiltroConteudoWig[] filtroConteudoWig = new FiltroConteudoWig[5];
			
			filtroConteudoWig[0] = getFiltroPorMsisdn			(session,request,conteudo,resposta,tipoResposta,aplicaParalelo,ordem);
			filtroConteudoWig[1] = getFiltroPorCategoria		(session,request,conteudo,resposta,tipoResposta,aplicaParalelo,ordem);
			filtroConteudoWig[2] = getFiltroPorERB      		(session,request,conteudo,resposta,tipoResposta,aplicaParalelo,ordem);
			filtroConteudoWig[3] = getFiltroPorModelo   		(session,request,conteudo,resposta,tipoResposta,aplicaParalelo,ordem);
			filtroConteudoWig[4] = getFiltroPorRestricaoTempo	(session,request,conteudo,resposta,tipoResposta,aplicaParalelo,ordem);
			
			for (int i = 0; i < 5; i++)
				if (filtroConteudoWig[i] != null) filtros.add(filtroConteudoWig[i]);

			// Caso a lista de filtros esteja vazia entao uma mensagem de erro eh retornada
			// caso contrario entao tenta inserir as informacoes no banco de dados
			
			if (filtros.size() == 0)
			{
				codRetorno = 1;
				msgRetorno = "É obrigatorio o cadastro de pelo menos 1 filtro.";
			}
			else
			{
				// Verifica se o codResposta estah diferente de zero. Isto indica que o
				// filtro estah sendo alterado e portanto a resposta eh alterada mas os
				// filtros sao excluidos e incluidos novamente pelos valores informados na tela
				if (!novaResposta)
				{
					FiltroConteudoWigDAO.alteraAnexoResposta(session, conteudo, resposta, filtros);
					msgRetorno = "Resposta alterada com sucesso.";
				}
				else 
					FiltroConteudoWigDAO.insereAnexoResposta(session, conteudo, resposta, filtros);
				
				// limpaCache();  // O limpaCache é chamado pelo usuario via menu.
			}
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			codRetorno = 1;
			msgRetorno = "Erro ao cadastrar os filtros. " + e;
			logger.error("Erro ao cadastrar os filtros de resposta anexa. " + e.getMessage());		
			if (session != null) session.getTransaction().rollback();
		}
		
		request.setAttribute("codRetorno",new Integer(codRetorno));
		request.setAttribute("msgRetorno",msgRetorno);
		return forward;
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
	/**
	 * Apos a alteracao ou inclusao do anexo de resposta eh preciso
	 * reiniciar o cache do servidor WIG que armazena estas informacoes
	 * para tentar minimizar o impacto desta limpeza estar como procedimento
	 * apenas entao o programa tenta executar a URL no servidor responsavel por esta limpeza
	 */
	/*
	private void limpaCache()
	{
		try
		{
			ServletContext context 	= getServlet().getServletContext();
			String servidor  		= (String)context.getAttribute(Constantes.WIG_NOME_SERVIDOR);
			String porta     		= (String)context.getAttribute(Constantes.WIG_PORTA_SERVIDOR);
			URL url 				= new URL("http://"+servidor+":"+porta+"/wig/limpaCache");	
			HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
			
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setUseCaches(false);
			conn.getContent();
		}
		catch(Exception e)
		{
			logger.error("Erro ao limpar cache do servidor wig.",e);
		}
	}
	*/
	
	/**
	 * Metodo....:getDescricaoResposta
	 * Descricao.:Este metodo monta o WML que serah utilizado como anexo de resposta
	 *            deste filtro sendo incluido. O WML depende dos valores passados como
	 *            parametro para identificar se eh parte de um menu de opcoes ou uma mensagem.
	 * @param request
	 * @return
	 */
	private String getDescricaoResposta(HttpServletRequest request, int tipoResposta)
	{
		String wmlResposta = null;
		
		// Busca os parametros necessarios para identificacao da resposta
		// se eh uma opcao para o menu de opcoes do conteudo ou se eh uma
		// mensagem que deve ser anexada ao primeiro card do WML existente
		// na resposta padrao do ConteudoWig
		String onvalue    = request.getParameter("onvalue");
		String onpick     = request.getParameter("onpick");
		String opcao      = request.getParameter("descricaoResposta");
		String mensagem   = request.getParameter("mensagem");
		
		// Caso o tipo do filtro indica uma opcao entao a tag <option> do WML
		// eh construida e inserida como descricao. Caso a opcao indique uma
		// mensagem entao eh construido somente um paragrafo para conter a mensagem
		if (tipoResposta == 1)
			wmlResposta = getWmlOption(opcao, onvalue, onpick);
		else
			wmlResposta = "<p>" + mensagem + "</p>";
		
		return wmlResposta.toString();
	}
	
	/**
	 * Metodo....:getWmlOption
	 * Descricao.:Retorna a tag <option> do WML da resposta caso o tipo seja para uma opcao
	 * @param opcao		- Valor da opção
	 * @param onvalue	- valor do atributo onvalue - Conteudo no qual a opcao irah ser direcionada
	 * @param onpick	- valor do atributo onpick  - Card ou referencia externa possivel para a opcao
	 * @return String   - Tag <option> do WML
	 */
	private String getWmlOption(String opcao, String onvalue, String onpick)
	{
		return "<option value=\"" + onvalue + "\" onpick=\"" + onpick + "\" >" + opcao + "</option>";
	}
	
	/**
	 * Metodo....:getFiltroConteudo
	 * Descricao.:Monta o objeto FiltroConteudo que serah utilizado em todos os metodos de filtros
	 *            disponiveis (msisdn, categoria, erb, tempo, etc...)
	 * @param conteudo		- ConteudoWig sendo adicionado a resposta
	 * @param resposta		- Resposta a ser anexa dependendo da aplicacao do filtro
	 * @param tipoResposta	- Tipo de resposta (mensagem ou opcao)
	 * @param classeFiltro	- Nome da classe que serah utilizada para a aplicacao do filtro
	 * @return FiltroConteudoWig - Objeto contendo as informacoes do filtro
	 */
	private FiltroConteudoWig getFiltroConteudo(Session session
											   ,ConteudoWig conteudo
			                                   ,RespostaWig resposta
			                                   ,int tipoResposta
			                                   ,String classeFiltro
			                                   ,boolean aplicaParalelo
			                                   ,int ordem)
	{
		FiltroConteudoWig filtroConteudo = new FiltroConteudoWig();
		filtroConteudo.setResposta(resposta);
		filtroConteudo.setCodConteudo(conteudo.getCodConteudo());
		filtroConteudo.setFiltro(FiltroWigDAO.findByNomeClasse(session, classeFiltro));
		filtroConteudo.setAplicaEmParalelo(aplicaParalelo);
		filtroConteudo.setOrdem(ordem);
		filtroConteudo.setTipoRespostaFiltro(tipoResposta);
		
		return filtroConteudo;
	}
	
	/**
	 * Metodo....:getFiltroPorMsisdn
	 * Descricao.:Retorna o filtro que serah aplicado com relacao ao msisdn
	 *            OBS: Nesse filtro eh a configuracao de aplicacao do filtro
	 *                 os possiveis valores de msisdn estao inerentes a este
	 *                 objeto como uma lista de msisdn's possiveis
	 * @param request		- HttpRequest
	 * @param conteudo		- Conteudo sendo adicionado a resposta 
	 * @param resposta		- Resposta a ser anexa dependendo da aplicacao do filtro
	 * @param tipoResposta	- Tipo de resposta (mensagem ou opcao)
	 * @return FiltroConteudoWig - Valores de configuracao da resposta anexa e dos valores de msisdns a serem utilizados
	 */
	private FiltroConteudoWig getFiltroPorMsisdn(Session session
			   									,HttpServletRequest request
			                                    ,ConteudoWig conteudo
			                                    ,RespostaWig resposta
			                                    ,int tipoResposta
			                                    ,boolean aplicaParalelo
			                                    ,int ordem)
	{
		FiltroConteudoWig filtroConteudo = null;
		
		// Busca o parametro de filtro para o filtro por msisdn.
		// Somente realiza a criacao dos objetos e leitura de demais
		// parametros se este dado estiver preenchido pelo menos com
		// uma linha
		String msisdns[]     = request.getParameterValues("mascmsisdn");
		String excludentes[] = request.getParameterValues("indExcludente");
		if (msisdns != null && msisdns.length > 0)
		{
			filtroConteudo = getFiltroConteudo(session,conteudo,resposta,tipoResposta,FiltroConteudoWig.CLASSE_FILTRO_MSISDN,aplicaParalelo,ordem);
			
			// Para cada linha da tabela que foi preenchida com as informacoes
			// de filtro, cria-se um objeto da classe FiltroMsisdnWig para ser
			// inserido no banco de dados os valores da aplicacao deste
			// para o anexo de resposta
			for (int i=0; i < msisdns.length; i++)
			{
				FiltroMsisdnWig filtroMsisdn = new FiltroMsisdnWig();
				filtroMsisdn.setCodResposta(resposta.getCodResposta());
				filtroMsisdn.setMascaraMsisdn(msisdns[i]);
				filtroMsisdn.setExcludente(excludentes[i].equals("S") ? true : false);
				
				filtroConteudo.addFiltroResposta(filtroMsisdn);
			}
		}
		return filtroConteudo;
	}

	/**
	 * Metodo....:getFiltroPorCategoria
	 * Descricao.:Retorna o filtro que serah aplicado com relacao a categoria do assinante (pos,pre ou hibrido)
	 *            OBS: Nesse filtro eh a configuracao de aplicacao do filtro
	 *                 os possiveis valores de categorias estao inerentes a este
	 *                 objeto como uma lista de categorias possiveis
	 * @param request		- HttpRequest
	 * @param conteudo		- Conteudo sendo adicionado a resposta 
	 * @param resposta		- Resposta a ser anexa dependendo da aplicacao do filtro
	 * @param tipoResposta	- Tipo de resposta (mensagem ou opcao)
	 * @return FiltroConteudoWig - Valores de configuracao da resposta anexa e dos valores de categorias a serem utilizados
	 */
	private FiltroConteudoWig getFiltroPorCategoria(Session session
			   									   ,HttpServletRequest request
			                                       ,ConteudoWig conteudo
			                                       ,RespostaWig resposta
			                                       ,int tipoResposta
				                                   ,boolean aplicaParalelo
				                                   ,int ordem)
	{
		FiltroConteudoWig filtroConteudo = null;
		
		// Busca o parametro de filtro para o filtro por categoria.
		// Somente realiza a criacao dos objetos e leitura de demais
		// parametros se este dado estiver preenchido pelo menos com
		// uma linha
		String categorias[] = request.getParameterValues("categoria");
		if (categorias != null && categorias.length > 0)
		{
			filtroConteudo = getFiltroConteudo(session,conteudo,resposta,tipoResposta,FiltroConteudoWig.CLASSE_FILTRO_CATEGORIA,aplicaParalelo,ordem);
			
			// Para cada linha da tabela que foi preenchida com as informacoes
			// de filtro, cria-se um objeto da classe FiltroCategoriaWig para ser
			// inserido no banco de dados os valores da aplicacao deste
			// para o anexo de resposta
			for (int i=0; i < categorias.length; i++)
			{
				FiltroCategoriaWig filtroCategoria = new FiltroCategoriaWig();
				filtroCategoria.setCodResposta(resposta.getCodResposta());
				filtroCategoria.setCategoria(CategoriaDAO.findByCodigo(session, Integer.parseInt(categorias[i])));
				
				filtroConteudo.addFiltroResposta(filtroCategoria);
			}
		}
		return filtroConteudo;
	}

	/**
	 * Metodo....:getFiltroPorModelo
	 * Descricao.:Retorna o filtro que serah aplicado com relacao ao modelo do aparelho do assinante
	 *            OBS: Nesse filtro eh a configuracao de aplicacao do filtro
	 *                 os possiveis valores de modelos estao inerentes a este
	 *                 objeto como uma lista de modelos possiveis
	 * @param request		- HttpRequest
	 * @param conteudo		- Conteudo sendo adicionado a resposta 
	 * @param resposta		- Resposta a ser anexa dependendo da aplicacao do filtro
	 * @param tipoResposta	- Tipo de resposta (mensagem ou opcao)
	 * @return FiltroConteudoWig - Valores de configuracao da resposta anexa e dos valores de categorias a serem utilizados
	 */
	private FiltroConteudoWig getFiltroPorModelo(Session session
			   									,HttpServletRequest request
			                                    ,ConteudoWig conteudo
			                                    ,RespostaWig resposta
			                                    ,int tipoResposta
			                                    ,boolean aplicaParalelo
			                                    ,int ordem)
	{
		FiltroConteudoWig filtroConteudo = null;
		
		// Busca o parametro de filtro para o filtro por modelo.
		// Somente realiza a criacao dos objetos e leitura de demais
		// parametros se este dado estiver preenchido pelo menos com
		// uma linha
		String modelos[] = request.getParameterValues("coModelo");
		if (modelos != null && modelos.length > 0)
		{
			filtroConteudo = getFiltroConteudo(session,conteudo,resposta,tipoResposta,FiltroConteudoWig.CLASSE_FILTRO_MODELO,aplicaParalelo,ordem);
			
			// Para cada linha da tabela que foi preenchida com as informacoes
			// de filtro, cria-se um objeto da classe FiltroModeloWig para ser
			// inserido no banco de dados os valores da aplicacao deste
			// para o anexo de resposta
			for (int i=0; i < modelos.length; i++)
			{
				FiltroModeloWig filtroModelo = new FiltroModeloWig();
				filtroModelo.setCodResposta(resposta.getCodResposta());
				filtroModelo.setModelo(ModeloDAO.findByCodigo(session, Integer.parseInt(modelos[i])));
				
				filtroConteudo.addFiltroResposta(filtroModelo);
			}
		}
		return filtroConteudo;
	}
	
	/**
	 * Metodo....:getFiltroPorERB
	 * Descricao.:Retorna o filtro que serah aplicado com relacao a ERB onde o assinante estah localizado
	 *            OBS: Nesse filtro eh a configuracao de aplicacao do filtro
	 *                 os possiveis valores de ERBs estao inerentes a este
	 *                 objeto como uma lista de ERBs possiveis
	 * @param request		- HttpRequest
	 * @param conteudo		- Conteudo sendo adicionado a resposta 
	 * @param resposta		- Resposta a ser anexa dependendo da aplicacao do filtro
	 * @param tipoResposta	- Tipo de resposta (mensagem ou opcao)
	 * @return FiltroConteudoWig - Valores de configuracao da resposta anexa e dos valores de categorias a serem utilizados
	 */
	private FiltroConteudoWig getFiltroPorERB(Session session
			   								 ,HttpServletRequest request
			                                 ,ConteudoWig conteudo
			                                 ,RespostaWig resposta
			                                 ,int tipoResposta
			                                 ,boolean aplicaParalelo
			                                 ,int ordem)
	{
		FiltroConteudoWig filtroConteudo = null;
		
		// Busca o parametro de filtro para o filtro por ERB.
		// Somente realiza a criacao dos objetos e leitura de demais
		// parametros se este dado estiver preenchido pelo menos com
		// uma linha
		String lacs[]    = request.getParameterValues("lac");
		String cellids[] = request.getParameterValues("cellid");
		if (lacs != null && lacs.length > 0)
		{
			filtroConteudo = getFiltroConteudo(session,conteudo,resposta,tipoResposta,FiltroConteudoWig.CLASSE_FILTRO_ERB,aplicaParalelo,ordem);
			
			// Para cada linha da tabela que foi preenchida com as informacoes
			// de filtro, cria-se um objeto da classe FiltroERBWig para ser
			// inserido no banco de dados os valores da aplicacao deste
			// para o anexo de resposta
			for (int i=0; i < lacs.length; i++)
			{
				FiltroERBWig filtroERB = new FiltroERBWig();
				filtroERB.setCodResposta(resposta.getCodResposta());
				filtroERB.setLac(Integer.parseInt(lacs[i]));
				filtroERB.setCellId(Integer.parseInt(cellids[i]));
				
				filtroConteudo.addFiltroResposta(filtroERB);
			}
		}
		return filtroConteudo;
	}
	
	/**
	 * Metodo....:getFiltroPorRestricaoTempo
	 * Descricao.:Retorna o filtro que serah aplicado com relacao ao tempo 
	 *            OBS: Nesse filtro eh a configuracao de aplicacao do filtro
	 *                 os possiveis valores de (ano,mes,dia,hora) estao inerentes a este
	 *                 objeto como uma lista de (ano,mes,dia,hora) possiveis
	 * @param request		- HttpRequest
	 * @param conteudo		- Conteudo sendo adicionado a resposta 
	 * @param resposta		- Resposta a ser anexa dependendo da aplicacao do filtro
	 * @param tipoResposta	- Tipo de resposta (mensagem ou opcao)
	 * @return FiltroConteudoWig - Valores de configuracao da resposta anexa e dos valores de categorias a serem utilizados
	 */
	private FiltroConteudoWig getFiltroPorRestricaoTempo(Session session
														,HttpServletRequest request
			                                            ,ConteudoWig conteudo
			                                            ,RespostaWig resposta
			                                            ,int tipoResposta
					                                    ,boolean aplicaParalelo
					                                    ,int ordem)
	{
		FiltroConteudoWig filtroConteudo = null;
		
		// Busca o parametro de filtro para o filtro por restricao de tempo.
		// Somente realiza a criacao dos objetos e leitura de demais
		// parametros se este dado estiver preenchido pelo menos com
		// uma linha
		String anos[]		= request.getParameterValues("anos");
		String meses[]      = request.getParameterValues("meses");
		String diasSemana[] = request.getParameterValues("diasSemana");
		String dias[]       = request.getParameterValues("dias");
		String horas[]      = request.getParameterValues("horas");
		if (anos != null && anos.length > 0)
		{
			filtroConteudo = getFiltroConteudo(session,conteudo,resposta,tipoResposta,FiltroConteudoWig.CLASSE_FILTRO_TEMPO,aplicaParalelo,ordem);
			
			// Para cada linha da tabela que foi preenchida com as informacoes
			// de filtro, cria-se um objeto da classe FiltroRestricaoTempoWig para ser
			// inserido no banco de dados os valores da aplicacao deste
			// para o anexo de resposta
			for (int i=0; i < anos.length; i++)
			{
				FiltroRestricaoTempoWig filtroTempo = new FiltroRestricaoTempoWig();
				filtroTempo.setCodResposta(resposta.getCodResposta());
				filtroTempo.setAnos(anos[i]);
				filtroTempo.setMeses(meses[i]);
				filtroTempo.setDiasSemana(diasSemana[i]);
				filtroTempo.setDias(dias[i]);
				filtroTempo.setHoras(horas[i]);
				
				filtroConteudo.addFiltroResposta(filtroTempo);
			}
		}
		return filtroConteudo;
	}

}
