package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class cadastro_005fpre_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/xml; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n");
      out.write("<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\" \"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\"> \r\n");
      out.write("\r\n");
      out.write("<wml>\r\n");
      out.write("\t");
if(request.getAttribute("telaInicial") != null){
      out.write('\r');
      out.write('\n');
      out.write('	');
if(request.getAttribute("itemMenu") == null) { 
      out.write("\r\n");
      out.write("\t  <wigplugin name=\"sendserversm\"> \r\n");
      out.write("\t    <param name=\"userdata\" value=\"Para se cadastrar, utilize o MenuBrTGSM - PrePago - OutrasOpcoes - IncluiCadastro. Caso nao possua esta opcao, acesse Personalizar - Incluir - PrePago\"/> \r\n");
      out.write("\t    <param name=\"destaddress\" value=\"");
      out.print( request.getParameter("MSISDN") );
      out.write("\"/> \r\n");
      out.write("\t  </wigplugin>\r\n");
      out.write("\t");
}
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<card>\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\t\t<setvar name=\"tip\" value=\"1\"/>\r\n");
      out.write("\t\t\t<playtone toneid=\"general-beep\" duration=\"1\"/>\r\n");
      out.write("Desbloqueie seu celular fazendo o cadastro e ganhe R$10! Clique OK para continuar\r\n");
      out.write("\t\t\t<setvar name=\"resp\" value=\"S\"/>\r\n");
      out.write("\t\t\t<go href=\"#cpf\"/>\r\n");
      out.write("\t\t</p>\r\n");
      out.write("\t</card>\r\n");
      out.write("\t");
 } // Tela Inicial
      out.write('\r');
      out.write('\n');
      out.write('	');
 if(request.getAttribute("telaCadastro") != null) { 
      out.write("\r\n");
      out.write("\t\t");
 if(request.getAttribute("mensagem") != null) { 
      out.write("\r\n");
      out.write("\t\t<card>\r\n");
      out.write("\t\t\t<p>");
      out.print( request.getAttribute("mensagem") );
      out.write("\r\n");
      out.write("\t\t\t\t<setvar name=\"tip\" value=\"1\"/>\r\n");
      out.write("\t\t\t\t<setvar name=\"resp\" value=\"S\"/>\r\n");
      out.write("\t\t\t\t<setvar name=\"tipo\" value=\"\"/>\r\n");
      out.write("\t\t\t\t<go href=\"#cpf\"/>\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t</card>\r\n");
      out.write("\t\t");
 }//Mensagem 
      out.write("\r\n");
      out.write("\t\t<card id=\"cpf\">\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t\t<setvar name=\"try\" value=\"");
      out.print( request.getAttribute("try")==null?"0":request.getAttribute("try") );
      out.write("\"/>\r\n");
      out.write("\t\t\t\t<setvar name=\"tipo\" value=\"CPF\"/>\r\n");
      out.write("\t\t\t\t<setvar name=\"cep\" value=\"");
      out.print( request.getParameter("cep")==null?"":request.getParameter("cep") );
      out.write("\"/>\r\n");
      out.write("\t\t\t\t<setvar name=\"id\" value=\"");
      out.print( request.getParameter("id")==null?"":request.getParameter("id") );
      out.write("\"/>\r\n");
      out.write("\t\t\t\t<setvar name=\"pdv\" value=\"");
      out.print(request.getParameter("pdv")==null?"":request.getParameter("pdv") );
      out.write("\"/>\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t\t<input title=\"CPF(Somente numeros):\" name=\"id\" value=\"$(id)\"  emptyok=\"false\" format=\"*N\" maxlength=\"11\"/>\r\n");
      out.write("\t\t\t\t<input title=\"Cep(Somente numeros):\" name=\"cep\" value=\"$(cep)\" emptyok=\"false\" format=\"*N\" maxlength=\"8\"/>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t");
 /*!-- Inicio da alteracao do PDV 
				Retirar o comentario desse INPUT, do SETVAR logo acima, 
				do 'No. carimbado na NF:' logo abaixo e alterar o valor do parametro na URL para $(pdv) */
				
      out.write("\r\n");
      out.write("\t\t\t\t<input title=\"No. carimbado na NF:\" name=\"pdv\" value=\"$(pdv)\" emptyok=\"true\" format=\"*N\" maxlength=\"6\"/>\r\n");
      out.write("\t\t\t\t");
/*!-- Final da alteracao do PDV -- */
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t\tConfirme:<br/>\r\n");
      out.write("\t\t\t\t$(tipo): $(id)<br/>\r\n");
      out.write("\t\t\t\tCep: $(cep)<br/>\r\n");
      out.write("\t\t\t\tNo. carimbado na NF: $(pdv)\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t\t<do type=\"accept\">\r\n");
      out.write("\t\t\t\t\t<go href=\"http://");
      out.print( application.getAttribute("ServidorWIG"));
      out.write(':');
      out.print( application.getAttribute("PortaServidorWIG"));
      out.write("/wig/cadpre?pdv=$(pdv)&amp;t=$(tip)&amp;resp=$(resp)&amp;tipo=$(tipo)&amp;id=$(id)&amp;cep=$(cep)&amp;try=$(try)&amp;cdm=");
      out.print( (String)request.getAttribute("CDM") );
      out.write("\"/>\r\n");
      out.write("\t\t\t\t</do>\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t</card>\r\n");
      out.write("\t");
 } // Tela de cadastro 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<card id=\"c\">\r\n");
      out.write("\t\t<p>");
      out.print( request.getAttribute("mensagemSucesso") == null?"":request.getAttribute("mensagemSucesso"));
      out.write("\r\n");
      out.write("\t\t\t");
 if("N".equalsIgnoreCase((String)request.getAttribute("CDM"))) {
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t<plugin name=\"*PAD\" destvar=\"c\" params=\"U\\x10\\x04\"/>\r\n");
      out.write("\t\t\t");
} else { 
      out.write("\r\n");
      out.write("\t\t\t<providelocalinfo cmdqualifier=\"imei\" destvar=\"IMEI\"/>\r\n");
      out.write("\t\t\t\t<do type=\"accept\">\r\n");
      out.write("\t\t\t\t\t<go enterwait=\"false\" href=\"!hsm!?sit=1&amp;I=$(IMEI)\"/>\r\n");
      out.write("\t\t\t\t</do>\r\n");
      out.write("\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t</p>\r\n");
      out.write("\t</card>\r\n");
      out.write("</wml>\r\n");
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
