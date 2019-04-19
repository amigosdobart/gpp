package br.com.brasiltelecom;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;

import br.com.brasiltelecom.wig.entity.cadpre.Endereco;

public class APIConsultaCEP { 
   public static String resposta;
   		public static void main(String[] args) {
			
 			String cep=args[0];
			System.out.println(consultacep(cep));
		}
	 public static Endereco consultacep(String cep){

 	 	String Resposta = "";
 	 	Endereco Retorno = null;
				 	 	 
	   try {

					SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
				  Date currentTime_1 = new Date();
 				  String dateString = formatter.format(currentTime_1);
 		
	        // Create a socket to the host
	        int port = 8106;
	        String addr = "entirex.brasiltelecom.com.br";
	        Socket socket = new Socket(addr, port);

	        // Send header
	        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
					StringBuffer data = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					data.append ("<mensagem>");
					data.append ("<cabecalho>");
					data.append ("<empresa>BRG</empresa>");
					data.append ("<sistema>SIB</sistema>");
					data.append ("<processo>GESCONLOGRAD</processo>");
					data.append ("<data>").append(dateString).append("</data>");
					data.append ("<identificador_requisicao>").append(cep ).append("</identificador_requisicao>");
					data.append ("</cabecalho>");
					data.append ("<conteudo><![CDATA[<?xml version=\"1.0\"?>");
					data.append ("<root>");
					data.append ("<num_cep>" ).append( cep ).append("</num_cep>");
					data.append ("</root>]]>");
					data.append ("</conteudo>");
					data.append ("</mensagem>\n");
	   
	        // Send data
					wr.write(data.toString());
	        wr.flush();
	    
	        // Get response
	        InputStream rd = socket.getInputStream();
	        byte line[] =new byte[1024];
	        resposta = "";
	        int cont = 0;
	        while ((cont = rd.read(line,0 ,  1024)) > 0) {
						Resposta = Resposta + new String(line, 0, cont);
					}
	        /*
          if ( (Resposta.indexOf("<cod_retorno>0</cod_retorno>")>=0) || (Resposta.indexOf("<cod_retorno>1</cod_retorno>")>=0) || (Resposta.indexOf("<cod_retorno>2</cod_retorno>")>=0) )
          {
          	Retorno = true;
          }
          */
	        Retorno = new Endereco();
	        Retorno.loadFromXml(Resposta);
					
	        wr.close();
	        rd.close();

	    } catch (Exception e) {
	    	Resposta = e.toString();
	    }

		return Retorno;

	}

}