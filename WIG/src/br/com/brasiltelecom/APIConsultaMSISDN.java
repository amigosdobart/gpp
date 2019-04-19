package br.com.brasiltelecom;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import br.com.brasiltelecom.wig.entity.cadpre.PessoaClarify;


public class APIConsultaMSISDN { 

	/**
	 * @deprecated
	 * @param msisdn
	 * @param ictx
	 * @return
	 */
	 public PessoaClarify ConsultaMSISDN(String msisdn, InitialContext ictx){

		 	PessoaClarify Resposta =null;
		 	Connection conn = null;
		 	CallableStatement stmt = null;
		 	
			try
			{
			
	 		    SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
			    Date currentTime_1 = new Date();
			    String dateString = formatter.format(currentTime_1);
			  
				StringBuffer inXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				inXml.append("<mensagem><cabecalho><empresa>BRG</empresa><sistema>SIB</sistema><processo>REL000CONCLIENT</processo>");
				inXml.append("<data>" + dateString + "</data><identificador_requisicao>123</identificador_requisicao></cabecalho><conteudo>");
				inXml.append("<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>");
				inXml.append("<msisdn>").append(msisdn).append("</msisdn>");
				inXml.append("</root>]]></conteudo></mensagem>");
				
				DataSource ds = (DataSource)ictx.lookup("java:/comp/env/jdbc/WPP_CLARIFY");
				conn = ds.getConnection();
	            
				stmt = conn.prepareCall( "call SA.intp_consulta_acesso_movel_xml(?,?)");
				stmt.setString(1, inXml.toString());
				stmt.registerOutParameter(2, Types.VARCHAR);
				stmt.execute();
	        	stmt.close();
				
				//Resposta = "1." + stmt.getString(2);
	        	String xml = stmt.getString(2);
	        	Resposta = new PessoaClarify();
	        	Resposta.loadFromXml(xml);
			}
			catch(SQLException e)
			{
			    //Resposta = "2." + e.toString();
			}
			catch(Exception e)
			{
			    //Resposta = "3." + e.toString();
			}
	        finally
	        {
	            try
	            {
	            	if (stmt != null) stmt.close();
	            	if (conn != null) conn.close();
	            }
	            catch(SQLException e1)
	            {
	            	//Resposta = "4." + e1.toString();
	            }
	            catch(Exception e)
	            {
	            	//Resposta = "5." + e.toString();
	            }
	        }

	    //if (Resposta.indexOf("<cod_retorno>101</cod_retorno>")>=0)
	    //{
	    //  Resposta = "OK";
	    //}
		
			return Resposta;

		}

}