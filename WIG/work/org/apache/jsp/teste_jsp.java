package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.sql.*;
import javax.naming.*;
import java.sql.*;

public final class teste_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<HTML>\r\n");
      out.write("<HEAD>\r\n");
      out.write("    <TITLE></TITLE>\r\n");
      out.write("</HEAD>\r\n");
      out.write("<BODY>\r\n");

      String dataSource[] = {
                "jdbc/WPP_HSID",
                "jdbc/WPP_DS",
                "jdbc/WPP_GPP",
                "jdbc/WPP_PREPAGO",
                "jdbc/WPP_WIGC",
                "jdbc/WPP_CLARIFYDES",
                "jdbc/WPP_CLARIFY"

      };
      InitialContext ictx = new InitialContext();
      for(int i=0; i<dataSource.length; i++){
          try{
          DataSource ds = (DataSource) ictx.lookup("java:comp/env/"+dataSource[i]);
          Connection con = ds.getConnection();
  
      out.write("\r\n");
      out.write("           ");
      out.print( dataSource[i] );
      out.write(" OK <br>\r\n");
      out.write("  ");

           con.close();
          }
          catch(Exception e){
  
      out.write("\r\n");
      out.write("           ");
      out.print( dataSource[i] );
      out.write(" <Font color=\"#FF0000\">Falha ");
      out.print( e.toString() );
      out.write("</Font> <br>\r\n");
      out.write("  ");

          }
      }
  
      out.write("\r\n");
      out.write("\r\n");
      out.write("</BODY>\r\n");
      out.write("</HTML>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
