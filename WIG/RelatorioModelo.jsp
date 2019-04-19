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
	  String fabricante = "";
	  String query = "";
		
    msisdn = request.getParameter("MSISDN");	
    servico = request.getParameter("menu");
    fabricante = request.getParameter("fab");


		
		ResultSet recordset = null;
		Statement stmt = null;
  	
		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");


if (servico == null) 
{
 servico = "inicio";
}

	if (servico.equals("inicio")) 
	{
		%><wml><card><p><select title="Relat. Modelo" name="RESP"><option value="fab">Por Fabricante</option></select><go href="!SBRTIN03!?menu=$(RESP)" /></p></card></wml><%
	}
	
	
	
	if (servico.equals("fab"))
	{
		%><wml><card><p><select title="Fabricante" name="RESP"><%
								Connection con = null;
								try
								{
									
									con = ds.getConnection();
									stmt = con.createStatement();

									query = "select co_fabricante, no_fabricante, tt_registros, vl_Percentual  from hsid_relat_modelo_fab_vw where tt_registros >= 50 order by 3 desc";
									recordset = stmt.executeQuery(query);
							    int contador=0;
							    int contger=0;
							    
							    while (recordset.next())
							    {
								    contador++;
								    contger++;
								    if (contador==5)
								    {
								    contador = 0;
										%><option onpick="#card<%=contger%>">Proximos ...</option></select><go href="!SBRTIN03!?menu=mod&fab=$(RESP)" /></p></card><card id="card<%=contger%>"><p><select title="Fabricante" name="RESP"><%    
								    }
								    %><option value="<%=recordset.getString(1)%>"><%=recordset.getString(2)%>:<%=recordset.getLong(3)%>(<%=recordset.getLong(4)%>%)</option><%
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
							
								%></select><go href="!SBRTIN03!?menu=mod&fab=$(RESP)"/></p></card></wml><%
	}
	if (servico.equals("mod"))
	{
		%><wml><card><p>Esse relatorio mostra no max 25 aparelhos.</p><p>Modelo:TOTAL<br/><%
		Connection con = null;
		try
		{
			
			con = ds.getConnection();
			stmt = con.createStatement();

			query = " select substr(no_modelo,1,8), tt_registros, vl_Percentual from hsid_relat_modelo_vw where co_fabricante = " + fabricante + " order by 2 desc ";
			
			recordset = stmt.executeQuery(query);
	    int contador=0;
		  int contger=0;
							    
	    while ( (recordset.next()) && (contger < 25))
	    {
		    contador++;
				contger++;
				if (contador==6)
				{
					contador = 1;
					%></p><p><%
				}
	    %><%=recordset.getString(1)%>:<%=recordset.getLong(2)%>(<%=recordset.getLong(3)%>%)<br/><%
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
	
		%></p></card></wml><%
	}
%>