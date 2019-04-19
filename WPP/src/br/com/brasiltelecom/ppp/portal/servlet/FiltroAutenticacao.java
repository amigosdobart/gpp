/*
 * Created on 20/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;



/**
 * @author Luciano Vilela
 * Filtro utilizado para realizar a autenticação do Usuario
 * 
 * 
 */
public class FiltroAutenticacao implements Filter {
	//private FilterConfig fcfg = null;
	private String[] byPass = {
		"/showLogon.do",
		"/showAlteraSenha.do",
		"/alteraSenha.do",
		"/logon.do"
	};
	

	/** 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		//fcfg = arg0;

	}

	/** 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain chain)
		throws IOException, ServletException {
		
		HttpSession session  = ((HttpServletRequest)  request).getSession();
		Usuario usuario = (Usuario) session.getAttribute(Constantes.USUARIO);
		String path = ((HttpServletRequest) request).getServletPath();
		if(usuario == null && !byPass(path)){
			((HttpServletResponse)  response).sendRedirect("showLogon.do");
			
		}
		else{
			chain.doFilter(request, response);
		}
		
		
		
	}

	/** (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

	private boolean byPass(String path){
		if(path == null){
			return true;
		}
		for(int i=0; i<byPass.length; i++){
			if(path.equals(byPass[i])){
				return true;
			}
		}
		return false;
	}

}
