<%@ page import="javax.sql.*, javax.naming.*, java.sql.*"%>

<HTML>
<HEAD>
    <TITLE></TITLE>
</HEAD>
<BODY>
<%
      String dataSource[] = {
                "jdbc/WPP_HSID",
                "jdbc/WPP_DS",
                "jdbc/WPP_GPP",
                "jdbc/WPP_PREPAGO",
                "jdbc/WPP_WIGC",
                "jdbc/WPP_CLARIFYDES",
                "jdbc/WPP_CLARIFY",
                 "jdbc/WPP_WIGD"

      };
      InitialContext ictx = new InitialContext();
      for(int i=0; i<dataSource.length; i++){
          try{
          DataSource ds = (DataSource) ictx.lookup("java:comp/env/"+dataSource[i]);
          Connection con = ds.getConnection();
  %>
           <%= dataSource[i] %> OK <br>
  <%
           con.close();
          }
          catch(Exception e){
  %>
           <%= dataSource[i] %> <Font color="#FF0000">Falha <%= e.toString() %></Font> <br>
  <%
          }
      }
  %>

</BODY>
</HTML>