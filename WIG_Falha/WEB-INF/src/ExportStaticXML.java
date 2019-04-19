import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportStaticXML {
	   private static Logger log = Logger.getLogger("TesteJDBC");
	   
	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
	        log.setLevel(Level.ALL);
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        long t0 = System.currentTimeMillis();
	        Connection conpre =  DriverManager.getConnection("jdbc:oracle:oci8:@prepr",
	        "hsid", "hsid252004");
	        
	        Statement stmt = conpre.createStatement();
	        
	        ResultSet rs = stmt.executeQuery("SELECT a.co_servico, a.co_conteudo, decode(a.co_tipo_resposta, 0, 'D', 'R') tipo, " +
	        		"        b.ds_resposta " +
	        		"  FROM wigc_conteudo a, wigc_resposta b " +
	        		"  where " +
	        		"  a.co_resposta = b.co_resposta " +
	        		"  order by 1, 2 ");
	        while(rs.next()){
	        	String fileName = "WEB-INF/xml/"+rs.getString("co_servico")+"_"+rs.getString("co_conteudo")+"_"+ rs.getString("tipo")+".wml";
	        	PrintWriter pr = new PrintWriter(new FileWriter(fileName));
	        	
	        	pr.println(rs.getString("ds_resposta"));
	        	pr.close();
	        	
	        }
	        rs.close();
	        stmt.close();
	        conpre.close();
	}

}
