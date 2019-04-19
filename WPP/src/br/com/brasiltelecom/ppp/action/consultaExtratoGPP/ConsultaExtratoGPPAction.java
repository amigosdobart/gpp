package br.com.brasiltelecom.ppp.action.consultaExtratoGPP;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.brasiltelecom.ppp.dao.HibernateHelper;
import br.com.brasiltelecom.ppp.dao.RequisicaoExtratoDAO;
import br.com.brasiltelecom.ppp.portal.entity.RequisicaoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gppEnviaMail.conexoes.EnviaMail;

public class ConsultaExtratoGPPAction extends Action
{
	private static final String MSISDN_PREFIXO = "55";

	private static final short CS_TELA	  = 1;
	private static final short CS_EMAIL	  = 2;
	private static final short CS_CORREIO = 3;

	Logger logger = Logger.getLogger(this.getClass());

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ServletContext context = servlet.getServletContext();

		String servidor = (String)context.getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String)context.getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		String diretorioTemp = buscarDiretorio((ArrayList)context.getAttribute(Constantes.DIRETORIO_COMPROVANTES));

		diretorioTemp += File.separator;
		// Monta o objeto RequisicaoExtrato
		RequisicaoExtrato reqExtrato = null;
		try
		{
			reqExtrato = gerarRequisicao(request);
		}
		catch(ParseException e)
		{
			logger.error("Formato de data invalido", e);
			request.setAttribute("mensagem", "Formato de data invalido.");
			return actionMapping.findForward("error");
		}
		catch(NullPointerException e)
		{
			logger.error("");
			request.setAttribute("mensagem", "Parametro obrigatorio.");
			return actionMapping.findForward("error");
		}
		catch(Throwable e)
		{
			logger.error("");
			request.setAttribute("mensagem", "Parametro invalido.");
			return actionMapping.findForward("error");
		}

		short canal = Short.parseShort(request.getParameter("CS"), 10);

		String forward = null;
		if(canal == CS_TELA || canal == CS_EMAIL)
		{
			ConsultaExtratoGPP consultaExtrato = new ConsultaExtratoGPP(servidor, porta,reqExtrato);

			if(!consultaExtrato.getRetorno().getVlrRetorno().equals(Integer.toString(Constantes.GPP_RET_OPERACAO_OK)))
			{
				logger.error(consultaExtrato.getRetorno().getDescRetorno());
				request.setAttribute("mensagem", consultaExtrato.getRetorno().getDescRetorno());
				return actionMapping.findForward("error");
			}

			byte[] buffer = null;
			try
			{
				// Inicializa o DataSource com o XML de retorno do GPP.
				JRXmlDataSource relDataSource = new JRXmlDataSource(consultaExtrato.getXmlExtrato());
				// Recupera o diretorio Raiz dos relatorios
				String dir =context.getRealPath("/jasper") + File.separator;
				// Carrega o arquivo jasper
				JasperReport relatorioJasper = (JasperReport)JRLoader.loadObject(new File(dir+"GPPExtrato_Layout.jasper"));
				// Inicializa o parametros do realtorio
				Map relParams = new TreeMap();
				relParams.put("SUBREPORT_DIR", dir);
				// Gera o PDF
				buffer = JasperRunManager.runReportToPdf(relatorioJasper, relParams, relDataSource);
			}
			catch (Exception e)
			{
				logger.error("Erro gerar PDF", e);
				request.setAttribute("mensagem", "Erro ao gerar extrato.");
				return actionMapping.findForward("error");
			}

			// CANAL DE SERVICO ENVIAR POR E-MAIL
			if(canal == CS_EMAIL)
			{
				if(diretorioTemp == null)
				{
					logger.error("Diretorio temporario nao encontrado.");
					request.setAttribute("mensagem", "Problema na configuracao do Portal Pre-pago. " +
							"Consulte o suporte para maiores informacoes.");
					return actionMapping.findForward("error");
				}
				String servidorSMPP = (String)context.getAttribute(Constantes.SERVIDOR_SMTP);
				String endOrigem = (String)context.getAttribute(Constantes.CONTA_EMAIL);
				String data = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				File arqPdf = null;
				try
				{
					arqPdf = new File(diretorioTemp+reqExtrato.getIdtMsisdn()+data+".pdf");
					FileOutputStream outputStream = new FileOutputStream(arqPdf);
					outputStream.write(buffer, 0, buffer.length);
					outputStream.flush();
					outputStream.close();

					EnviaMail email = new EnviaMail(endOrigem, servidorSMPP);
					email.addEnderecoDestino(reqExtrato.getDesEmail());
					email.setTipoMensagem("text/plain");
					email.setAssunto("Extrato Pre-Pago Brasil Telecom.");
					email.setMensagem("O arquivo encontra-se em anexo.");
					email.addArquivoAnexo(arqPdf);
					email.enviaMail();

					request.setAttribute("mensagem", "Extrato enviado com sucesso!");
					forward = "success";
				}
				catch (Exception e)
				{
					logger.error("Erro ao enviar relatorio por e-mail", e);
					request.setAttribute("mensagem", "Erro enviar relatorio por e-mail.");
					return actionMapping.findForward("error");
				}
				finally
				{
					try
					{
						if(arqPdf != null)
							arqPdf.delete();
					}
					catch(Exception e)
					{
						logger.error("Erro ao deletar arquivo temporario", e);
					}
				}
			}
			// CANAL DE SERVICO ENVIAR PARA TELA
			else
			{
				try
				{
					response.setContentType("application/pdf");
					response.setContentLength(buffer.length);
					ServletOutputStream outputStream = response.getOutputStream();
					outputStream.write(buffer, 0, buffer.length);
					outputStream.flush();
					outputStream.close();
				}
				catch (Exception e)
				{
					logger.error("Erro enviar relatorio para visao em tela", e);
					request.setAttribute("mensagem", "Erro ao gerar relatorio.");
					return actionMapping.findForward("error");
				}
			}
		}
		else if(canal == CS_CORREIO)
		{
			Transaction trans = null;
			try
			{
				Session session = HibernateHelper.getSession();
				trans = session.beginTransaction();
				RequisicaoExtratoDAO.incluirRequisicaoExtrato(session, reqExtrato);
				trans.commit();

				request.setAttribute("mensagem", "Extrato enviado com sucesso! " +
						"Sua solicitacao sera processada nas proximas 14 horas.");
				forward = "success";
			}
			catch (Exception e)
			{
				if(trans != null)
					trans.rollback();

				logger.error("Erro gravar requisicao de extrato no banco.", e);
				request.setAttribute("mensagem", "Erro ao enviar extrato por correio.");
				return actionMapping.findForward("error");
			}
		}
		else
		{
			request.setAttribute("mensagem", "Canal de servico invalido!");
			forward = "error";
		}

		return actionMapping.findForward(forward);
	}

	private static RequisicaoExtrato gerarRequisicao(HttpServletRequest request) throws ParseException
	{
		RequisicaoExtrato req = new RequisicaoExtrato();
		// Requisicao do extrato
		if(request.getParameter("MS") == null)
			throw new NullPointerException();

		req.setIdtMsisdn(MSISDN_PREFIXO+request.getParameter("MS"));
		req.setDatRequisicao(Calendar.getInstance().getTime());
		req.setDatPeriodoInicial(new SimpleDateFormat("yyyyMMdd").parse(request.getParameter("DI")));
		req.setDatPeriodoFinal(new SimpleDateFormat("yyyyMMdd").parse(request.getParameter("DF")));
		req.setIdtFilial(Integer.parseInt(request.getParameter("NE")));
		req.setIndCobrarExtrato(Integer.parseInt(request.getParameter("IC") != null ?
				request.getParameter("IC") : "0"));
		// Cadastro do cliente
		req.setNomCliente(		request.getParameter("NM")	!= null ? request.getParameter("NM")  : "");
		req.setDesLogradouro(	request.getParameter("LG")	!= null ? request.getParameter("LG")  : "");
		req.setDesFachada(		request.getParameter("FC")	!= null ? request.getParameter("FC")  : "");
		req.setDesBairro(		request.getParameter("BR")	!= null ? request.getParameter("BR")  : "");
		req.setDesComplemento(	request.getParameter("CM1")	!= null ? request.getParameter("CM1") : "");
		req.setDesComplemento(	request.getParameter("CM2")	!= null ? ";"+request.getParameter("CM2") : "");
		req.setDesCidade(		request.getParameter("CD")	!= null ? request.getParameter("CD") : "");
		req.setDesUf(			request.getParameter("UF")	!= null ? request.getParameter("UF") : "");
		req.setDesCep(			request.getParameter("CE")	!= null ? request.getParameter("CE") : "");
		req.setDesContrato(		request.getParameter("CT")	!= null ? request.getParameter("CT") : "");
		req.setDesEmail(		request.getParameter("EM")	!= null ? request.getParameter("EM") : "");

		return req;
	}

	private static String buscarDiretorio(List diretorios)
	{
		for(Iterator it = diretorios.iterator(); it.hasNext();)
		{
			String path = (String)it.next();
			File f = new File(path);
			if(f.exists())
				return path;
		}

		return null;
	}

	public String getOperacao()
	{
		return null;
	}
}
