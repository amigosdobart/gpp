/*
 * Created on 10/10/2004
 *
  */
package br.com.brasiltelecom.ppp.portal.servlet;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.PersistenceException;

import com.brt.gpp.comum.Definicoes;

import br.com.brasiltelecom.ppp.home.ContingenciaCrmHome;
import br.com.brasiltelecom.ppp.home.PlanoPrecoHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.interfacegpp.DesbloqueioContingenciaGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm;
import br.com.brasiltelecom.ppp.portal.entity.DadosCadastraisCrm;
//import br.com.brasiltelecom.ppp.portal.entity.Usuario;

import java.io.IOException;
//import java.text.SimpleDateFormat;


/**
 * @author Luciano Vilela
 * 
 */
public class Desbloqueio extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	Logger logger = Logger.getLogger(this.getClass());
	//private String codOperacao = Constantes.COD_DESBLOQUEIO_CONTINGENCIA;
	//private static SimpleDateFormat dat = new SimpleDateFormat("dd/MM/yyyy");
	 /** Fim dos Atributos **/
	private ServletContext servletContext = null;

	protected void service(HttpServletRequest request, HttpServletResponse response){
		JDO jdo = null;
		Database db = null;
		try {
	
				 jdo = (JDO) servletContext.getAttribute(Constantes.JDO);
				 db = jdo.getDatabase();
				
				//HttpSession session = request.getSession();
				db.begin();
				//String telContato=null;
				
				String msisdn =   (String)request.getParameter("telefone");
				if(msisdn == null){
					msisdn = (String)request.getParameter("TELEFONE");
				}
				logger.info("Desbloqueio de toolbar solicitado : " + msisdn);
				//Usuario usuario = (Usuario) session.getAttribute(Constantes.USUARIO);
			
				String msisdnFormatado = "("+msisdn.substring(0,2)+")"+msisdn.substring(2);
				msisdn= "55"+msisdn;
				
				int codHotline = jaHouveDesbloqueioHotline(db,msisdn);
				
				if(codHotline>0)
				{
					//String motivo = request.getParameter("motivoBloqueio");
					// Início do Código para bloqueio de servicos e consulta de idAtividade ao GPP
					String servidor  = (String) servletContext.getAttribute(Constantes.GPP_NOME_SERVIDOR);
					String porta     = (String) servletContext.getAttribute(Constantes.GPP_PORTA_SERVIDOR);
					//String categoria = (String) request.getParameter("produtos");
					
					long idAtividade = 0;
					Assinante assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn, servidor, porta);
					/*
					 * Se o assinante existir na base da Tecnomen desativar Hotline como pré-pago F2,
					 * caso contrário como pós-pago
					 */
					String categoriaAssinante;
					String planoPrecoAssinante;
					int retorno = Integer.parseInt(assinante.getRetorno());
					if(retorno != 2)
					{
						// Usuario nao existe na tecnomen entao o plano de preco utilizado e o plano basico
						// para um assinante Pos-Pago 
						//categoriaAssinante="F1";
						//planoPrecoAssinante=Constantes.PLANO_PRECO_BASICO_POS_PAGO;
						//else{
							//Usuario existe na tecnomen
							categoriaAssinante="F2";
							planoPrecoAssinante=assinante.getPlanoPreco();
	
						// Envia o bloqueio atraves do GPP
						idAtividade = DesbloqueioContingenciaGPP.desastivarHotline(servidor, porta, msisdn, categoriaAssinante);
						
						// Fim do código de acesso ao GPP
						if(idAtividade>0){
							ContingenciaCrm contingenciaCrm = ContingenciaCrmHome.findByAtividade(db,idAtividade);
							if (contingenciaCrm == null){
								contingenciaCrm = new ContingenciaCrm();
								contingenciaCrm.setIdAtividade(idAtividade);
								contingenciaCrm.setIdOperacao("06");
								contingenciaCrm.setMsisdn(msisdn);
								contingenciaCrm.setDatAtividade(new java.util.Date());
								contingenciaCrm.setIdAtendente(request.getHeader(Constantes.LOGIN_HEADER)==null?"administrador":request.getHeader(Constantes.LOGIN_HEADER));
								contingenciaCrm.setIdStatus(ContingenciaCrmHome.findByBloqueioStatusId(db, Definicoes.STATUS_BLOQUEIO_SOLICITADO));
								contingenciaCrm.setIdPlanoPreco(PlanoPrecoHome.findByID(db,Integer.parseInt(planoPrecoAssinante)));
								db.create(contingenciaCrm);	
								// inicio do codigo para insercao de dados cadastrais do requisitante de bloqueio.
								DadosCadastraisCrm dadosCadastrais = new DadosCadastraisCrm();
								dadosCadastrais.setIdAtividade(idAtividade);
								dadosCadastrais.setNomeUsuario("ToolBar");
								dadosCadastrais.setNumCpf("11111111111");
								dadosCadastrais.setNumRg("1111");
								dadosCadastrais.setNumTelefContato("11111");
								dadosCadastrais.setNomLogradouro("toolbar");
								dadosCadastrais.setNomCidade("toolbar");
								dadosCadastrais.setNomBairro("toolbar");
								dadosCadastrais.setNumCep(0);
								dadosCadastrais.setIdUf("DF");
	
	
								db.create(dadosCadastrais);
								logger.info("Assinante "+msisdnFormatado+ " desbloqueado com sucesso");
								request.setAttribute(Constantes.MENSAGEM, "Assinante "+msisdnFormatado+ " desbloqueado com sucesso");	
							}
							
							else{
								 logger.info(msisdnFormatado+" já se encontra no Banco de Dados");
								 request.setAttribute(Constantes.MENSAGEM, msisdn+" já se encontra no Banco de Dados"); 
								}
						}
						else{
							logger.info("Assinante "+msisdnFormatado+ " não está no status pré-ativo");
							request.setAttribute(Constantes.MENSAGEM, "Assinante "+msisdnFormatado+ " não está no status pré-ativo");
						}
					}
					else{
						logger.info("Assinante "+msisdnFormatado+ " não é um assinante pré-pago");
						request.setAttribute(Constantes.MENSAGEM, "Assinante "+msisdnFormatado+ " não é um assinante pré-pago");
					}
				}
				else{
					if (codHotline==-1){
						logger.info("Solicitação de desbloqueio do assinante "+msisdnFormatado+ " já efetuada (CONFIRMADO).");
						request.setAttribute(Constantes.MENSAGEM, "Solicitação de desbloqueio do assinante "+msisdnFormatado+ " já efetuada (CONFIRMADO).");
					}
					else{
						if (codHotline==-2){
							logger.info("Solicitação de desbloqueio do assinante "+msisdnFormatado+ " já efetuada (EM ANDAMENTO).");
							request.setAttribute(Constantes.MENSAGEM, "Solicitação de desbloqueio do assinante "+msisdnFormatado+ " já efetuada (EM ANDAMENTO).");
						}
					}
				}
				db.commit();
				
		}
		catch (Exception e)
		{
				logger.error("Não foi possível desbloquear o assinante em modo de contingência (" +
								e.getMessage() + ")");
				request.setAttribute(Constantes.MENSAGEM,"Não foi possível desbloquear o assinante em modo de contingência (" +
					e.getMessage() + ")");
		}
		finally{
			if(db != null && !db.isClosed()){
				try {
					db.close();
				} catch (PersistenceException e1) {
					request.setAttribute(Constantes.MENSAGEM,"Não foi possível desbloquear o assinante em modo de contingência (" +
					e1.getMessage() + ")");
				}
			}
		}
		mostraMensagem(request, response);	
			
	}
	
	private int jaHouveDesbloqueioHotline(Database db, String msisdn) throws Exception
	{ 
		int ret = 1; // Retorno ok
		ContingenciaCrm contingenciaCrm = ContingenciaCrmHome.findAssinanteOperacao(db,msisdn,Constantes.CONTINGENCIA_DESATIVACAO_HOTLINE);
		if (contingenciaCrm!=null)
		{
			if (contingenciaCrm.getIdStatus().getIdStatusBloqueio().equals(Constantes.CONTINGENCIA_BLOQUEIO_CONFIRMADO) )
					//&& contingenciaCrm.getIdStatusProc().equals(Constantes.CONTINGENCIA_BLOQUEIO_OK))
				ret = -1;
			else 
				if (contingenciaCrm.getIdStatus().getIdStatusBloqueio().equals(Constantes.CONTINGENCIA_BLOQUEIO_SOLICITADO))
					ret = -2;
		}
		return ret;
	}

	public void init(ServletConfig scfg) throws ServletException {
		servletContext = scfg.getServletContext();		

	}
	private void mostraMensagem(HttpServletRequest request, HttpServletResponse response){
		try {
			ServletOutputStream out = response.getOutputStream();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Portal Web Pr&eacute; Pago </title>");
			out.println("<link rel=\"STYLESHEET\" type=\"text/css\" href=\"dealer.css\">");

			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
			out.println("</head>");
			out.println("<body>");

			out.println("<br><br>");
			out.println("<center>");
			out.println("	  <table width=\"520\" border=\"0\" cellspacing=\"1\" cellpadding=\"4\">");
			out.println("		<tr> ");
			out.println("		  <td bgcolor=\"#BFE7FF\" class=\"txtazulescBold\">Mensagem</td>");
			out.println("		</tr>");
			out.println("		<tr> ");
			out.println("		  <td bgcolor=\"#E2F4FB\" align=\"center\"><SPAN CLASS=\"txtpreto\">" + request.getAttribute(Constantes.MENSAGEM)+"<br><br></SPAN></td>");
			out.println("		</tr>");
			out.println("	  </table>");
			out.println("</center>");			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"#F2F2F2\">");
			out.println("	<tr>");
			out.println("		<td><img src=\"imagens/fals.gif\" border=0 alt=\"\" width=1 height=24></td>");
			out.println("		<td><img src=\"imagens/fals.gif\" border=0 alt=\"\" width=1 height=1></td>");
			out.println("	</tr>");
			out.println("</table>");

			out.println("</body>");
			out.println("</html>");
			out.flush();
	
		} catch (IOException e) {
			logger.error("Erro ao retonar resposta", e);
		}
				
		
	}
}
