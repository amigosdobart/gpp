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
	  String tac = "";
	  String modelo = "";
	  String fabricante = "";
	  String configdm = "";
		
    tac = request.getParameter("num");	
		
		ResultSet recordset = null;
		Statement stmt = null;
  	
		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");


	if (tac == null) 
	{
		%>
		<wml>
		  <card id="Main">
		    <p>
		    Informe os 6 primeiros digitos do IMEI (TAC).
		    <input title="TAC (6 dig):" name="m" type="text" format="*N" maxlength="6" emptyok="false" />
		     <go href="!SBRTIN02!?num=$(m)" />
		      </p>
		  </card>
		</wml>
		<%
	}
	else
	{
		%>
		<wml>
				<card>
					<p>
						<%
						Connection con = null;
						try
						{
							con = ds.getConnection();
							stmt = con.createStatement();
							
							recordset = stmt.executeQuery("select no_modelo, no_fabricante, in_configuracao_dm " +
									                        "from hsid_modelo a, hsid_fabricante b, hsid_modelo_tac c " +
									                       "where a.co_fabricante = b.co_fabricante "+
									                         "and a.co_modelo = c.co_modelo "+
									                         "and c.co_tac = '" + tac + "' "+
									                       "order by no_modelo");
					    
							String separador = "";
						    while (recordset.next())
						    {
						    	if (!recordset.isFirst())
						    		separador = br.com.brasiltelecom.Modelo.SEPARADOR;
	
								modelo += separador+recordset.getString(1);
								fabricante = recordset.getString(2);
								configdm = recordset.getString(3);
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
						
					if (modelo.equals(""))
					{
					%>
						TAC nao encontrado.
					<%
					}
					else
					{
					%>
						TAC: <%=tac%> <br/>
						Modelo: <%=modelo%> <br/>
						Fabricante: <%=fabricante%> <br/>
						Config.OTA: <%=configdm%><br/>
					<%
					}
					%>
					</p>
				</card>
			</wml>
		<%
	}
%>