<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"
    import="java.sql.*, javax.sql.*, javax.rmi.*, javax.naming.*, br.com.brasiltelecom.*" 
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
	  String msisdn = "";
	  String modelo = "";
	  String fabricante = "";
	  String data = "";
	  String imei = "";
	  String configdm = "";
	  		
    msisdn = request.getParameter("num");	
		
		ResultSet recordset = null;
		Statement stmt = null;
  	
		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");


	if (msisdn == null) 
	{
		%>
		<wml>
		  <card id="Main">
		    <p>
		    Informe o MSISDN completo (ex: 556184017962).
		    <input title="MSISDN:" name="m" type="text" format="*N" maxlength="12" emptyok="false" />
		     <go href="!SBRTIN01!?num=$(m)" />
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
							
							recordset = stmt.executeQuery("select no_modelo, no_fabricante, to_char(dt_inclusao,'dd/mm/rrrr hh24:mi:ss'), "+
									                             "nu_imei, nvl(in_configuracao_dm,'N') "+
									                        "from hsid_cliente a, hsid_modelo b, " +
									                             "hsid_fabricante c, hsid_modelo_tac d " +
									                       "where d.co_modelo = b.co_modelo "+
									                         "and b.co_fabricante = c.co_fabricante "+
									                         "and substr(nu_imei,1,6) = d.co_tac (+) "+
									                         "and nu_msisdn = '" + msisdn + "'");

						String separador = "";
					    while (recordset.next())
					    {
					    	if (!recordset.isFirst())
					    		separador = br.com.brasiltelecom.Modelo.SEPARADOR;
					    	
							modelo += separador+recordset.getString(1);
							fabricante = recordset.getString(2);
							data = recordset.getString(3);
							imei = recordset.getString(4);
							configdm = recordset.getString(5);
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
						MSISDN nao encontrado.
					<%
					}
					else
					{
					%>
						Modelo: <%=modelo%> <br/>
						Fabricante: <%=fabricante%> <br/>
						Imei: <%=imei%> <br/>
						Identificado em: <%=data%> <br/>
						Config. OTA: <%=configdm%> <br/>
					<%
					}
					%>
					</p>
				</card>
			</wml>
		<%
	}
%>