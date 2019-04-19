package br.com.brasiltelecom.wig;
import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FiltroTimeOut implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		final FilterChain c = chain;
		final ServletRequest r = request;
		final ServletResponse res = response;
		
		
		
		// get pool 
		Thread runner = new Thread( new Runnable(){
			public void run(){
				try {
					c.doFilter(r, res);
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServletException e) {
					e.printStackTrace();
				}
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, "runner");
		long s1 = System.currentTimeMillis();
		runner.start();
		// thread.notify();
		while(runner.isAlive() ){
			if(System.currentTimeMillis() - s1 > 15000){
				runner.interrupt();
				//returno pool.
				throw new ServletException("Esse foi o erro do filtro");
			}
			
			Thread.yield();
		}
		
		//if(chain != null) chain.
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
