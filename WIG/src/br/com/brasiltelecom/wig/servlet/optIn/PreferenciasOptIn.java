package br.com.brasiltelecom.wig.servlet.optIn;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.dao.OptInDAO;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.filtrosResposta.FiltroERB;
import br.com.brasiltelecom.wig.util.Definicoes;

public class PreferenciasOptIn extends HttpServlet
{
	private String wigContainer = null;
	private String wigControl = null;
	private Context initContext = null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void init(ServletConfig arg0)
	{
		try
		{
			initContext		= new InitialContext();
			wigContainer	= (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
			wigControl    	= (String)arg0.getServletContext().getAttribute("VariavelWIGControl");
		}
		catch (NamingException e)
		{
			logger.error(e);
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		Connection con  = null;
		String msisdn = "";
		// Nova instancia do objeto mensagemRetorno para mostrar a mensagem ao assinante
		MensagemRetorno retorno = new MensagemRetorno();
		
		try
		{
			// Recebe os seguintes parametros
			// 1 - msisdn
			// 2 - Categoria CD/DVD/Livros
			// 3 - Categoria Moda Masculina
			// 4 - Categoria Moda Feminina
			// 5 - Categoria Eletronicos
			// 6 - Categoria Show/Eventos
			// 7 - Categoria Bares&Rest.
			// 8 - Codigo do conteudo
			// 9 - loc (Lac e CellId) na hora da requisicao
			// 10- mandatorio (false se for nulo)
			// 11- fidelizacao (0 se for nulo)
			// 12- imei
			
			msisdn 	  = request.getParameter("MSISDN");
			
			int cdDvdLivros	  = (request.getParameter("c1").equalsIgnoreCase("sim") ? 1 : 0);
			int modaMasculina = (request.getParameter("c2").equalsIgnoreCase("sim") ? 1 : 0);
			int modaFeminina  = (request.getParameter("c3").equalsIgnoreCase("sim") ? 1 : 0);
			int eletronicos   = (request.getParameter("c4").equalsIgnoreCase("sim") ? 1 : 0);
			int showEventos   = (request.getParameter("c5").equalsIgnoreCase("sim") ? 1 : 0);
			int baresRest 	  = (request.getParameter("c6").equalsIgnoreCase("sim") ? 1 : 0);
			
			int coConteudo	   = (request.getParameter("btc") == null ? 0 : Integer.parseInt(request.getParameter("btc")) );
			String loc 		   = request.getParameter ("loc");
			boolean mandatorio = (request.getParameter("mt" ) == null ? false : true);
			int fidelizacao    = (request.getParameter("fid") != null ? Integer.parseInt(request.getParameter("fid")) : 0);
			String imei		   = request.getParameter ("i"  );
			
			logger.debug("Parametros recebidos com sucesso para o assinante " + msisdn + "\n" +
						 "Preferencias: CD/DVD/Livros: " + cdDvdLivros + "\nModa Masculina: " + modaMasculina +
						 "\nModa Feminina: " + modaFeminina + "\nEletronicos: " + eletronicos + 
						 "\nShow/Eventos: " + showEventos + "\nBares&Rest.: " + baresRest);
			
			// Pega uma conexao com o Banco de Dados do WIG
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/WPP_WIGC");
			con = ds.getConnection();
			
			// Cria uma nova instancia da classe OptInDAO
			OptInDAO optInDAO = new OptInDAO();
			
			// Solicita o OptIn ativo, caso haja
			OptIn optIn = optInDAO.getOptIn(msisdn, con);
			
			if (optIn == null || optIn.getDataOptOut() != null)
			{
				// Verifica se o assinante ja existe na tabela OptIn
				boolean assinanteJaExiste = false;
				if (optIn != null)
					assinanteJaExiste = true;
				
				logger.debug("Assinante " + msisdn + " nao possui OptIn ativo.");
				
				// Cria um novo objeto OptIn, sobreescrevendo o anterior
				optIn = new OptIn();
				
				// Seta o coModelo do objeto optIn de acordo com o msisdn
				optIn.setCoModelo(optInDAO.getCoModelo(msisdn, con));
				// Seta o restante dos atributos do objeto optIn
				optIn.setMsisdn(msisdn);
				optIn.setLac(FiltroERB.getLac(loc));
				optIn.setCellId(FiltroERB.getCellId(loc));
				optIn.setMandatorio(mandatorio);
				optIn.setCoConteudo(coConteudo);
				
				logger.debug("Enviando requisicao para inserir os dados do assinante no OptIn / Historico...");
				
				// Caso o assinante possuia o OptIn e realizaou o OptOut, o mesmo sera
				// atualizado nas tabelas OptIn / OptInHistorico.
				// Se o assinante jamais ativou o OptIn, o mesmo sera inserido 
				// nas referidas tabelas
				if (assinanteJaExiste)
					optInDAO.atualizarOptIn(optIn, con);
				else
					optInDAO.inserirOptIn(optIn, con);
			}
			else
			{
				// Seta o coModelo do objeto optIn de acordo com o msisdn
				optIn.setCoModelo(optInDAO.getCoModelo(msisdn, con));
				// Seta o restante dos atributos do objeto optIn
				optIn.setMsisdn(msisdn);
				optIn.setLac(FiltroERB.getLac(loc));
				optIn.setCellId(FiltroERB.getCellId(loc));
				optIn.setMandatorio(mandatorio);
				optIn.setCoConteudo(coConteudo);
				
				// Realiza a atualizacao dos dados do assinante no OptIn
				optInDAO.atualizarOptIn(optIn, con);
			}
			
			// Deleta todas as categorias do assinante, caso haja
			optInDAO.deletaTodasCategoriasAssinante(msisdn, con);
			
			// Caso o assinante ja possua o OptIn, ou tenha sido ativado
			// eh realizada a inclusao apenas das categorias que o assinante escolheu
			if (cdDvdLivros == 1)
				optInDAO.insereCategoriaAssinante(msisdn, con, Definicoes.WIG_ID_CATEGORIA_CD_DVD_LIVROS);
			if (modaMasculina == 1)
				optInDAO.insereCategoriaAssinante(msisdn, con, Definicoes.WIG_ID_CATEGORIA_MODA_MASCULINA);
			if (modaFeminina == 1)
				optInDAO.insereCategoriaAssinante(msisdn, con, Definicoes.WIG_ID_CATEGORIA_MODA_FEMININA);
			if (eletronicos == 1)
				optInDAO.insereCategoriaAssinante(msisdn, con, Definicoes.WIG_ID_CATEGORIA_ELETRONICOS);
			if (showEventos == 1)
				optInDAO.insereCategoriaAssinante(msisdn, con, Definicoes.WIG_ID_CATEGORIA_SHOWS_EVENTOS);
			if (baresRest == 1)
				optInDAO.insereCategoriaAssinante(msisdn, con, Definicoes.WIG_ID_CATEGORIA_BARES_REST);
			
			// Construcao do SMS a ser enviado junto com a requisicao
			retorno.setTagSMS(msisdn, Definicoes.WIG_SMS_OPTIN_SUCESSO);
			
			// Setando o atributo MensagemRetorno, com o respectivo objeto
			request.setAttribute("mensagemRetorno", retorno);
			
			logger.debug("Redirecionando a requisicao para mostrar a mensagem ao assinante " + msisdn);
			
			// Redireciona a requisicao para o ShowMensagemOptIn
			request.getRequestDispatcher("/ShowMensagemOptIn.jsp").forward(request, response);
			
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro nas preferencias do assinante " + msisdn, e);
			
			// Mostra uma mensagem de erro ao cliente
			out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Tente novamente em alguns instantes."));
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch(Exception e)
			{
				logger.error(e);
			}
		}
		
		out.flush();
		out.close();
	}
}
