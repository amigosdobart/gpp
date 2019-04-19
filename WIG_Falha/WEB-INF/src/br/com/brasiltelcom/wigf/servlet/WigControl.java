package br.com.brasiltelcom.wigf.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WigControl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3437110929907676261L;

	volatile private ServletContext scfg;

	volatile private Pattern de;
	volatile private String para;

	public void init(ServletConfig cfg) throws ServletException {
		scfg = cfg.getServletContext();
		if(cfg.getInitParameter("de") != null){
			de = Pattern.compile(cfg.getInitParameter("de"));
		}
		para = cfg.getInitParameter("para");

	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servico = request.getParameter("bts");
		String conteudo = request.getParameter("btc");
		String tipoResposta = tipoResposta(servico, conteudo);
		response.setContentType("text/html; charset=iso-8859-1");
		if (tipoResposta != null) {
			String fileName = scfg.getRealPath("WEB-INF/xml") + "/" + servico
					+ "_" + conteudo + "_" + tipoResposta + ".wml";
			FileInputStream xmlIn = new FileInputStream(fileName);
			if (tipoResposta.equals("D")) {
				sendFile(xmlIn, response.getOutputStream());
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						xmlIn));

				response.sendRedirect(parseString(in.readLine()));
			}
			xmlIn.close();

		} else {
			response
					.getOutputStream()
					.println(
							"<wml><card><p>Nenhum servico disponivel.</p></card></wml>");
		}

	}

	/**
	 * Retorna o tipo de resposta do arquivo
	 * 
	 * @param servico
	 * @param conteudo
	 * @return - 'R' - Redirect 'D' - Direto null - Arquivo não encontrado
	 */

	private String tipoResposta(String servico, String conteudo) {
		String result = null;
		String fileName = scfg.getRealPath("WEB-INF/xml") + "/" + servico + "_"
				+ conteudo + "_R.wml";
		if (new java.io.File(fileName).exists()) {
			result = "R";
		}
		fileName = scfg.getRealPath("WEB-INF/xml") + "/" + servico + "_"
				+ conteudo + "_D.wml";
		if (new java.io.File(fileName).exists()) {
			result = "D";
		}

		return result;
	}

	private void sendFile(InputStream entrada, OutputStream saida)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(entrada));
		String linha = null;
		while ((linha = in.readLine()) != null) {
			saida.write(parseString(linha).getBytes());
			saida.write("\n".getBytes());
		}

	}

	private String parseString(String str) {
		
		String result = str;
		if(de != null){
			Matcher m = de.matcher(result);
			if (m != null) {
				result = m.replaceAll(para);
			}
		}
		return result;
	}

}
