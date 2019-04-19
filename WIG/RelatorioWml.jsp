<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"
    import="java.sql.*, javax.sql.*, javax.rmi.*, javax.naming.*" 
    errorPage="" %>
<%!
	private InitialContext ictx = null;

	public void jspInit(){
		try{
		  ictx = new InitialContext();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
%>
<%
	  String mensagem = "";
	  String servico = "";
	  String msisdn = "";
	  int count = 0 ;
	
		
    msisdn = request.getParameter("MSISDN");	
    servico = request.getParameter("menu");


		
		ResultSet recordset = null;
		Statement stmt = null;
  	
		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");


if (servico == null) 
{
 servico = "inicio";
}

	if (servico.equals("inicio")) 
	{
		%>
			<wml>
				<card>
					<p>
		        <select title="Relatorio TSD:" name="RESP">
		            <option value="dia">Por Dia</option>
		            <option value="ddd">Por DDD</option>
		        </select>
		        <go href="!SBRTINTSD!?menu=$(RESP)" />
					</p>
				</card>
			</wml>
		<%
	}
	
	
	
	if (servico.equals("dia"))
	{
		%>
			<wml>
				<card>
					<p>
						DATA - Total <br/>
						<%
						Connection con = null;
						try
						{
							
							con = ds.getConnection();
							stmt = con.createStatement();
							
							recordset = stmt.executeQuery("select to_char(dt_inclusao, 'dd/mm/rr'), tt_registros from v_hsid_relat_data where dt_inclusao >= sysdate - 10");
					    
					    while (recordset.next())
					    {
					    %>
					    	<%if(count==5){%>
							</p><p>
						<%}%>
						
						<%=recordset.getString(1)%> - <%=recordset.getLong(2)%> <br/>
						<% count++;

					    }
						}
						catch(SQLException e)
						{
							%>
								Ocorreu um erro: <%=e%>
							<%
						}
						finally
						{
							recordset.close();
							stmt.close();
							con.close();
						}
					
						%>
	
					</p>
				</card>
			</wml>
		<%
	}
	if (servico.equals("ddd"))
	{
		%>
			<wml>
				<card>
					<p>
					DDD - TOTAL: <br/>
		<%
		Connection con = null;
		try
		{
			
			con = ds.getConnection();
			stmt = con.createStatement();
			
			recordset = stmt.executeQuery("select nu_ddd, tt_registro from v_hsid_relat_ddd");
	    
	    while (recordset.next())
	    {
	    %>
	    	<%=recordset.getString(1)%> - <%=recordset.getLong(2)%> <br/>
	    <%
	    }
		}
		catch(SQLException e)
		{
			%>
				Ocorreu um erro: <%=e%>
			<%
		}
		finally{
			con.close();
		}
	
		%>
					</p>
				</card>
			</wml>
		<%
	}
%>