<%@ page import="java.io.*, java.util.*,java.sql.*,javax.sql.*,javax.naming.*"  %>
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
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
	
	String msisdn,timestamp,resposta;
	String[] valores=request.getParameter("FILE").split("\\."); 
	msisdn=valores[0];
	timestamp=valores[1];
   
 	StringBuffer sb = new StringBuffer();
        String[] bytes = request.getParameter("IMEI").split(" ");
        bytes[0] = bytes[0].substring(0,1);

        for (int bcount = 0 ; bcount< bytes.length;bcount++)
        {
                bytes[bcount] = bytes[bcount].substring(1)+ bytes[bcount].substring(0,1);
                sb.append(bytes[bcount]);
        }
	
	StringBuffer c = new StringBuffer();	   	
	StringBuffer l = new StringBuffer();		
	String mascara = "######-##-######-#";
	java.util.regex.Pattern p = java.util.regex.Pattern.compile("(#+)");		
	java.util.regex.Matcher m = p.matcher(mascara);		
	
	int pos = 0;		
	int i = 0;		
	c.append("^");		
	while(m.find()) 
	{			
		c.append("(\\d{" + (m.end()-m.start()) + "})");			
		l.append(mascara.substring(pos,m.start()) + "$" + ++i);			
		pos = m.end();		
	}		
	c.append("$");		
	if (pos<mascara.length()) l.append(mascara.substring(pos));		
	java.util.regex.Pattern template = java.util.regex.Pattern.compile(c.toString());		

	java.util.regex.Matcher capture = template.matcher(sb.toString());
	
        sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        if (request.getParameter("LOC") != null)
        {
	        bytes = request.getParameter("LOC").split(" ");
	        try{
	                sb = new StringBuffer(bytes[0].substring(1)+bytes[0].substring(0,1));
	                sb.append(bytes[1].substring(1)+"/"+bytes[2].substring(1)+bytes[2].substring(0,1));//+bytes[1].substring(0,1));
	                sb.append("_"+Integer.parseInt(bytes[3]+bytes[4], 16));
	                sb.append("_"+Integer.parseInt(bytes[5]+bytes[6], 16));
	        }catch(Exception e){}
        }
        else sb = new StringBuffer("-_-_-");

        String[] sinal = (request.getParameter("NMR") != null) ? request.getParameter("NMR").split(" ") : new String[]{"0"};
        int nmr= Integer.parseInt(sinal[0],16);
        nmr = nmr & 0x3F;
         
        resposta=sdf.format(new java.util.Date())+"#"+capture.replaceAll(l.toString())+"#"+sb.toString()+"#"+nmr+"#"+request.getParameter("NMR");
         
        PreparedStatement prepInsert = null;
        
       	DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_GPP");
 		Connection con = null;
		try
		{
			String sqlinsert = "INSERT INTO tbl_ger_trace_assinante(IDT_MSISDN,IDT_TIMESTAMP_TRACE,DES_RESPOSTA) "+
					           "values (?,?,?)";
			
			con = ds.getConnection();
			prepInsert = con.prepareStatement(sqlinsert);
			prepInsert.setString(1,msisdn);
			prepInsert.setLong(2,Long.parseLong(timestamp));
			prepInsert.setString(3,resposta);
			prepInsert.executeUpdate();
			con.commit();
		}
		catch(SQLException e)
		{
			%>
				Ocorreu um erro: <%=e%>
			<%
		}
		finally
		{
			if (prepInsert != null)
				prepInsert.close();
			if (con != null)
				con.close();
		}  
 %>
<wml>
<card id=\"dummy\"><p class=\"delay\"></p></card>
</wml>

