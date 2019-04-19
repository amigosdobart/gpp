package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import br.com.brasiltelecom.wig.entity.*;
import java.util.Iterator;
import java.util.*;

public final class ShowAmigosTodaHora_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"\r\n");
      out.write("\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">\r\n");
      out.write("<wml>\r\n");
      out.write("<card>\r\n");
      out.write("\r\n");
 
HttpSession sessaoATH = request.getSession(true);
BrtVantagem brtVantagem    = (BrtVantagem)sessaoATH.getAttribute("brtVantagem"  );
Collection listaCelNovos   = (Collection )sessaoATH.getAttribute("listaCelNovos");
Collection listaFixNovos   = (Collection )sessaoATH.getAttribute("listaFixNovos");
MensagemRetorno msgRetorno = (MensagemRetorno)sessaoATH.getAttribute("msgRetorno");
String maquinaWIG		   = (String) sessaoATH.getAttribute("maquinaWIG");

Collection listaCelulares  = new ArrayList();
Collection listaFixos  	   = new ArrayList();
Collection listaTodosAtuaisCelular = new ArrayList();
Collection listaTodosAtuaisFixo    = new ArrayList();

String msg = "";
if (msgRetorno != null )
	msg = msgRetorno.getMensagem();

// Seleciona as Collections que serao utilizadas para popular os "inputs"
// Se a lista dos celulares e fixos novos for nula,
// as listas receberao os dados do objeto brtVantagem
if (listaCelNovos == null || listaFixNovos == null)
{
	listaCelulares.addAll(brtVantagem.getAmigosTodaHoraCelular());
	listaFixos.addAll(brtVantagem.getAmigosTodaHoraFixo());
	// Popula a lista de TODOS os acessos atuais do ATH
	listaTodosAtuaisCelular.addAll(brtVantagem.getAmigosTodaHoraCelular());
	listaTodosAtuaisFixo.addAll(brtVantagem.getAmigosTodaHoraFixo());
}
// Se a lista dos celulares e fixos nao for nula,
// as listas receberao as listas dos celulares novos e fixos novos
else
{
	listaCelulares.addAll(listaCelNovos);
	listaFixos.addAll(listaFixNovos);
}

//Define os parametros dos novos numeros a serem enviados 
// cn - Celulares Novos 
// fn - Fixos Novos
StringBuffer cn = new StringBuffer("");
StringBuffer fn = new StringBuffer("");

// Diferenca entre as quantidades de Celulares e Fixos
int diferenca = (brtVantagem.getQtdeAmigosTodaHora() - brtVantagem.getQtdeAmigosTodaHoraFixo());
int qtdeMaxima = brtVantagem.getQtdeAmigosTodaHora();
int qtdeAtual = 1;

// Monta os parametros dos celulares novos
for (int i = 1; i <= diferenca; i++)
	cn.append("$(c"+i+");");
// Monta os parametros dos fixos novos
for (int j = 1; j <= brtVantagem.getQtdeAmigosTodaHoraFixo();    j++)
	fn.append("$(f"+j+");");

      out.write("\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\t");
      out.print(msg);
      out.write('\r');
      out.write('\n');

//Inicializa a posicao do parametro
int posicao = 1;

/* Inicio da montagem dos inputs */
// Se a lista completa dos atuais nao for nula,
// inicia o processo de montagem dos inputs de
// acordo com a quantidade permitida de cel e fixos
if (!listaTodosAtuaisCelular.isEmpty() || !listaTodosAtuaisFixo.isEmpty())
{
	//Collection numerosARemover = new ArrayList();
	
	// Montara os inputs de celulares, ate atingir a diferenca
	for (Iterator i = listaTodosAtuaisCelular.iterator(); i.hasNext(); )
	{
		String valor = (String)i.next();
		
      out.write("<input title=\"Celular ");
      out.print(posicao);
      out.write(":\" value=\"");
      out.print(valor);
      out.write("\" name=\"c");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />");

		//numerosARemover.add(valor);
		qtdeAtual++;
		if (posicao == diferenca)
		{
			//posicao = 1;
			break;
		}
		else
			posicao++;
	}
	// Monta os inputs de Celular em Branco
	while (qtdeAtual <= diferenca)
	{
		
      out.write("<input title=\"Celular ");
      out.print(posicao);
      out.write(":\" name=\"c");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />");

		qtdeAtual++;
		posicao++;
	}
	
	// Reinicia a posicao de 1 para Fixo/Celular
	posicao = 1;
	//listaTodosAtuais.removeAll(numerosARemover);
	//numerosARemover.clear();
	for (Iterator j = listaTodosAtuaisFixo.iterator(); j.hasNext(); )
	{
		String valor = (String)j.next();
		
      out.write("<input title=\"Fixo/Celular ");
      out.print(posicao);
      out.write(":\" value=\"");
      out.print(valor);
      out.write("\" name=\"f");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />");

		//numerosARemover.add(valor);
		qtdeAtual++;
		posicao++;
	}
	//listaTodosAtuais.removeAll(numerosARemover);
	
	// Monta os inputs de Fixo/Celular em Branco
	while (qtdeAtual <= qtdeMaxima)
	{
	
      out.write("<input title=\"Fixo/Celular ");
      out.print(posicao);
      out.write(":\" name=\"f");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />");

		System.out.println("Qtde atual: " + qtdeAtual + " <= " + qtdeMaxima);
		posicao++;
		qtdeAtual++;
	}
}
else
{
	// Inicio do processo para montar os inputs que serao mostrados ao assinante
	for (Iterator i = listaCelulares.iterator(); i.hasNext() ; )
	{
	
      out.write("\t<input title=\"Celular ");
      out.print(posicao);
      out.write(":\" value=\"");
      out.print(i.next());
      out.write("\" name=\"c");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />\r\n");
      out.write("\t");

		posicao ++;
	}
	// Monta os inputs que serao mostrados em branco ao assinante
	for (int i = posicao; i <= diferenca; i++ )
	{
	
      out.write("\t<input title=\"Celular ");
      out.print(posicao);
      out.write(":\" name=\"c");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />\r\n");
      out.write("\t");

		posicao ++;
	}
	
	// Reinicia a contagem da posicao para os fixos
	posicao = 1;
	// Varre os numeros fixos ja cadastrados do ATH
	for (Iterator i = listaFixos.iterator(); i.hasNext() ; )
	{
	
      out.write("\t<input title=\"Fixo/Celular ");
      out.print(posicao);
      out.write(":\" value=\"");
      out.print(i.next());
      out.write("\" name=\"f");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />\r\n");
      out.write("\t");

		posicao ++;
	}
	for (int i = posicao; i <= brtVantagem.getQtdeAmigosTodaHoraFixo(); i++)
	{
	
      out.write("\t<input title=\"Fixo/Celular ");
      out.print(posicao);
      out.write(":\" name=\"f");
      out.print(posicao);
      out.write("\" format=\"*N\" emptyok=\"true\" maxlength=\"10\" />\r\n");
      out.write("\t");

		posicao ++;
	}
}

      out.write("\r\n");
      out.write("\t<setvar name=\"cn\" value=\"");
      out.print(cn.toString());
      out.write("\"/>\r\n");
      out.write("\t<setvar name=\"fn\" value=\"");
      out.print(fn.toString());
      out.write("\"/>\r\n");
      out.write("\t<do type=\"accept\">\r\n");
      out.write("\t\t<go href=\"http://");
      out.print(maquinaWIG);
      out.write("/wig/amigosTodaHoraManutencao?cn=$(cn)&amp;fn=$(fn)\" />\r\n");
      out.write("\t</do>\r\n");
      out.write("\t</p>\r\n");
      out.write("\t</card>\r\n");
      out.write("</wml>");
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
