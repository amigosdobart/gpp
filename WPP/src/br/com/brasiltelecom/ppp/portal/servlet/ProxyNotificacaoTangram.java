package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.dao.NotificacaoDAO;
import br.com.brasiltelecom.ppp.dao.ParametrosNotificacaoDAO;
import com.brt.gpp.comum.conexoes.tangram.ParserTangram;
import com.brt.gpp.comum.conexoes.tangram.entidade.Notificacao;

/**
 *  Proxy para recebimendo de notificações do Tangram.<br>
 *  
 *  Esse proxy é utilizado pela API do GPP 'EnvioMensagemTangram' para fazer
 *  persistencia das informações de notificação enviadas pelo Tangram.<br>
 *  
 *  Essa servlet, após concluir seu processamento, encaminha a notificação
 *  para a URL informada inicialmente pelo usuário (essa URL está cadastrada
 *  em banco de dados (TBL_TAN_REQUISICAO).<br>
 *  
 *  Para obter o registro correspondente na TBL_TAN_REQUISICAO, essa servlet
 *  primeiro identifica um destino de mensagem (TBL_TAN_DESTINO_MENSAGEM) ao 
 *  qual pertence essa notificacao. Dado o destino, obtemos pela FK a requisicao
 *  que originou o processo.<br>
 *  
 *  Observações:<br>
 *  
 *  <blockquote>
 *  - Essa servlet suporta encaminhamento por HTTP_POST apenas;<br>
 *  - Caso não seja encontrado pelo menos um destino de mensagem, essa
 *    notificacao é descartada e nenhuma persistencia é realizada;
 *  </blockquote>
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 21/09/2007
 */
public class ProxyNotificacaoTangram extends HttpServlet 
{
	private static final long serialVersionUID = -6844557413678264233L;
	
	private Logger logger;
	private ServletContext context;
	
	/**
	 *	Construtor da classe
	 */	
	public ProxyNotificacaoTangram()
	{
		this.logger = Logger.getLogger(this.getClass());
		this.context = null;
	}
	
	/**
	 *	Inicializa o servlet.
	 *
	 *	@param	config	Configuracoes do Servlet
	 *	@throws	ServletException
	 */	
	public void init(ServletConfig config) throws ServletException 
	{
		this.context = config.getServletContext();
	}
	
	/**
	 *	Grava a notificação e encaminha a requisição
	 *
	 *	@param		HttpServletRequest		request			Requisicao HTTP
	 *	@param		HttpServletResponse		response		Resposta HTTP
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		Session session = null;
		SessionFactory sessionFactory = null;
		
		try 
		{	
			// Captura a conexao com o banco
			sessionFactory = (SessionFactory)context.getAttribute(Constantes.HIBERNATE);
			if (sessionFactory == null)
				throw new Exception("Hibernate SessionFactory nulo.");
			
			session = sessionFactory.getCurrentSession();
			if (session == null)
				throw new Exception("Hibernate Session nulo. Problemas ao iniciar conexao pelo Hibernate.");
			
			// Inicia a transacao
	        session.beginTransaction();
	        
	        // Captura os dados enviados pelo Tangram
	        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
	        StringBuffer buffer = new StringBuffer();
	        while ((line = reader.readLine()) != null) 
	        {
	        	buffer.append(line);
	        }
	        reader.close();
	        
	        // Interpreta os dados enviados pelo Tangram
	        Notificacao notificacao = ParserTangram.processarXmlNotificacao(buffer.toString());
	        
	        // Grava a notificacao no banco
	        try
	        {
	        	NotificacaoDAO.incluirNotificacao(session, notificacao);
		        session.getTransaction().commit();
	        }
	        catch (Exception e)
	        {
	        	this.logger.warn("Excecao: " + e);
	        }
	        
	        // Gera retorno (acknowledge) para o Tangram
	        notificacao.setCodRetornoAplicacao(new Integer(0));
	        notificacao.setDescRetornoAplicacao("[WPP] Notificacao processada com sucesso no ProxyNotificacaoTangram");
	        String xmlRetornoNotificacao = ParserTangram.gerarXmlRetornoNotificacao(notificacao);
	        response.getOutputStream().write(xmlRetornoNotificacao.getBytes());
	        response.flushBuffer();
	        
	        // Consulta os parametros de notificacao da requisicao que originou a notificacao corrente
	        String urlNotificacao = ParametrosNotificacaoDAO.findEnderecoByDestinoMensagem(
	        		session, notificacao.getIdtMsisdnDestino(), notificacao.getIdMensagem());
	        
	        // Encaminha a notificacao de acordo com os parametros acima (multithread) 
	        if (urlNotificacao != null)
	        {
		        Thread thread = new Thread(new EncaminhaNotificacao(notificacao, urlNotificacao));
		        thread.start();
	        }	        
		}	
		catch(Exception e) 
		{
			this.logger.error("ProxyNotificacaoTangram. Excecao: " + e);
			
			if (session != null && session.getTransaction().isActive())
				session.getTransaction().rollback();
		}
	}
	
	/**
	 *  Thread para envio da notificacao
	 */
	private class EncaminhaNotificacao implements Runnable
	{
		private Notificacao notificacao = null;
		private String urlNotificacao = null;
		
		public EncaminhaNotificacao(Notificacao notificacao, String url) 
		{
			this.notificacao = notificacao;
			this.urlNotificacao = url;
		}
		
		public void run() 
		{
			notificacao.setCodRetornoAplicacao(null);
    		notificacao.setDescRetornoAplicacao(null);
    		
    		// Sempre HTTP_POST (restrição da API do GPP)
    		try
    		{
    			URL url = new URL(urlNotificacao);
        		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
        		httpConnection.setDoInput(false);
        		httpConnection.setDoOutput(true);	
        		DataOutputStream output = new DataOutputStream(httpConnection.getOutputStream());
        		output.writeBytes(ParserTangram.gerarXmlNotificacao(notificacao));
        		output.flush();
        		output.close();
    		} catch (Exception e)
    		{}
		}
	}
}
