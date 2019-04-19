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
	  String msisdn = "";
	  String tipoatualizacao = "";
	  		
    msisdn = request.getParameter("num");	
    tipoatualizacao = request.getParameter("tp");	
		

	if (msisdn == null) 
	{
		%><wml>
			<card id="Main">
			<p>
			<select name="t" title="SASC">
			<option value="1">Atualizacao</option>
			<option value="2">Reset</option>
			<option value="3">Reload</option>
			</select>
			Informe o MSISDN completo (ex: 556184017962).
			<input title="MSISDN:" name="m" type="text" format="*N" maxlength="12" emptyok="false" />
			<go href="http://10.61.176.118:7778/wig/SascApp.jsp?num=$(m)&tp=$(t)" />
			</p>
			</card>
			</wml><%
	}
	else
	{
  		DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");
			Connection con = null;
			try{
				con = ds.getConnection();

				CallableStatement procedure = con.prepareCall("call SASCPACAPP.SASC_ATUALIZA_SIM(?,?,?)");
				procedure.setString(1,msisdn);
				procedure.setString(2,tipoatualizacao);
				procedure.registerOutParameter(3, Types.VARCHAR);
				procedure.execute();
				String retorno = procedure.getString(3);
				
				if(retorno.equals("0")){
				%><wml><card><p>Solicitacao enviada para processamento.</p></card></wml><%
				}
				else {
				%><wml><card><p>Ocorreu uma falha no envio. Tente mais tarde novamente.</p></card></wml><%
				}
				
			}
			catch(SQLException e){
				%><wml><card><p>Ocorreu uma falha no envio.Erro: <%=e%></p></card></wml><%			
			}
			finally{
				con.close();
			}
	}
	
%>