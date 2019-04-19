package br.com.brasiltelecom.wig.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TesteServlet extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		request.getRequestDispatcher("/index.jsp").forward(request,response);
		request.getRequestDispatcher("/index.jsp").forward(request,response);
		
		out.println("Se a teoria do Galvagni estiver certa, voce nao vera essa mensagem");
		out.flush();
		out.close();
	}

}
