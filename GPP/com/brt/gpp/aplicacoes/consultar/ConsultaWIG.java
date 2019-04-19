package com.brt.gpp.aplicacoes.consultar;

import java.util.Date;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
//import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
*
* Consulta WIG 
* 
*
* <P> Versao:			1.0
*
* @Autor: 			Alberto Magno (Acc)
* Data: 				30/12/04
*
* Modificado por:
* Data:
* Razao:
*
*/

public final class ConsultaWIG extends Aplicacoes
{
	private static long pausa = 5000L; //Pausa de 5 segundos para tentar ler aquivo de resposta WIG ou ler resposta
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public ConsultaWIG (long logId)
	 {
		super(logId, Definicoes.CL_CONSULTA_WIG);
	 }
	
	public String consultaWIGInformacoesAparelho(String MSISDN)
	{
		 super.log(Definicoes.INFO, "consultaWIGInformacoesAparelho", "Inicio MSISDN "+MSISDN);
		 //Connecta no WIG
		 
		 //Post da Consulta
		 ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();
		 
			 try {
			 	long timestamp = System.currentTimeMillis(); 

			 	super.log(Definicoes.DEBUG, "consultaWIGInformacoesAparelho", "Conectando:\nSERVIDOR:"
			 			+config.getServidorWIG()+"\nPORTA:"+config.getPortaWIG());

			 	Socket sk = new Socket(config.getServidorWIG(), config.getPortaWIG());
				PrintStream ps = new PrintStream(sk.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(sk.getInputStream()));

				
				System.setProperty("line.separator","\n\r");
				
				
				StringBuffer linhaBuffer = new StringBuffer("<providelocalinfo cmdqualifier=\"imei\" destvar=\"ID\"/>\n"
				 + "<providelocalinfo cmdqualifier=\"location\" destvar=\"LOC\"/>\n"
				 + "<providelocalinfo cmdqualifier=\"nmr\" destvar=\"NMR\"/>\n"
				 + "<convert type=\"bin-to-hexbin\" srcvar=\"ID\" destvar=\"ID2\"/>\n"
				 + "<convert type=\"bin-to-hexbin\" srcvar=\"LOC\" destvar=\"LOC2\"/>\n"
				 + "<convert type=\"bin-to-hexbin\" srcvar=\"NMR\" destvar=\"NMR2\"/>\n"
				 + "<go enterwait=\"false\" href=\"").append(config.getUrlRetornoPesquisaAparelhoWIG()).append("?IMEI=$(ID2)&amp;LOC=$(LOC2)&amp;NMR=$(NMR2)&amp;FILE=")
				 .append(MSISDN).append(".").append(timestamp).append("\" />\n");
				
				
				Integer size = new Integer(658 + linhaBuffer.length() + 1);
				ps.println("POST /PushSender.cgi HTTP/1.1");
				ps.println("Host: "+config.getServidorWIG()+":"+config.getPortaWIG());
				ps.println("Accept: text/*");
				ps.println("Accept-Charset: iso-8859-1");
				ps.println("Accept-Encoding:identity");
				ps.println("User-Agent: WDZ PushSender");
				ps.println("Connection: close");
				ps.println("Content-Type: multipart/related; boundary=\"asdlfkjiurwghasf\"; type=\"application/xml\"");
				ps.println("");
				ps.println("Content-Length: " + size.toString() );
				ps.println("");
				ps.println("");
				ps.println("--asdlfkjiurwghasf");
				ps.println("");
				ps.println("Content-Type: application/xml");
				ps.println("<?xml version=\"1.0\"?>");
				ps.println("<!DOCTYPE pap PUBLIC \"-//WAPFORUM//DTD PAP 2.0//EN\" \"http://www.wapforum.org/DTD/pap_2.0.dtd\"><pap>");
				ps.println("<push-message ");
				ps.println("push-id=\"WDZ_Test_Push\" ppg-notify-requested-to=\"http://\">");
				ps.println("<address ");
				ps.println("address-value=\""+MSISDN+"\" /><quality-of-service ");
				ps.println("delivery-method=\"confirmed\" /></push-message>");
				ps.println("</pap>");
				ps.println("");
				ps.println("");
				ps.println("--asdlfkjiurwghasf"); 
				ps.println("");
				ps.println("Content-Type: text/vnd.wap.wml");
				ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				ps.println("<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"");
				ps.println("\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">");
				ps.println("");
				ps.println("<wml>");
				ps.println("<card id=\"Main\">");
				ps.println("<p>");
				ps.println(linhaBuffer.toString());
				ps.println("</p>");
				ps.println("</card>");
				ps.println("</wml>");
				ps.println("");
				ps.println("--asdlfkjiurwghasf");
				ps.println("");
				ps.println("");
				
				
				 
				 // Analise do Retorno
				String linha = "";
				StringBuffer r = new StringBuffer();
				while((linha = in.readLine())!=null)
					r.append(linha);
				
				 if (r.toString().indexOf("Push message OK")<0)
				 {
				 	super.log(Definicoes.DEBUG, "consultaWIGInformacoesAparelho", "RETORNO WIG:\n"+r);
				 	return montaXMLretorno(sdf.format(new Date()),MSISDN,"-","-_-_-","-","-","ERRO NA CONSULTA (TCP PUSH)");
				 }
			//Retorno da Consulta
				 
				 long contador = pausa;
				 while(config.getTimeOutWIG()>(int)contador)
				 {
						Thread.sleep(pausa);
						String xmlRetorno="";
						String linhaRegistro;
						if ( ( linhaRegistro=consultarRetorno(MSISDN,timestamp)) != null)
						{
							if (!"".equals(linhaRegistro))
							{
								String[] ret = linhaRegistro.split("#");
								super.log(Definicoes.DEBUG, "consultaWIGInformacoesAparelho", 
										"IMEI:"+ret[1]+"LOC:"+ret[2]+"SINAL:"+ret[3]);
								xmlRetorno = montaXMLretorno(sdf.format(new Date()),MSISDN,ret[1],ret[2],ret[3],ret[4],"CONSULTA REALIZADA COM SUCESSO");
							}
							
							return xmlRetorno;
						}
						contador += pausa;
				 }
				if(sk==null) sk.close();
				
			}catch (InterruptedException e) 
			{
				super.log(Definicoes.ERRO, "consultaWIGInformacoesAparelho", "ERRO (Interrupcao THREAD):"+e);
			}
			catch (FileNotFoundException e) 
			{
				super.log(Definicoes.ERRO, "consultaWIGInformacoesAparelho", "ERRO (FILENOTFOUND):"+e);
			}
			catch (IOException e) 
			{
				super.log(Definicoes.ERRO, "consultaWIGInformacoesAparelho", "ERRO (IO):"+e);
			}
			catch(Exception e)
			{
				super.log(Definicoes.WARN,"consultarRetorno","ERRO :"+e);
			}
		 
	    super.log(Definicoes.INFO, "consultaWIGInformacoesAparelho", "Fim");
		return montaXMLretorno(sdf.format(new Date()),MSISDN,"-","-_-_-","-","-","ERRO NA CONSULTA (WIG TIMEOUT)");

	}
	/**
	 * Metodo...: consultaRetorno
	 * Descricao: Consulta dados de retorno
	 * @param	msisdn 	- Número do cliente
	 * @param   timestamp - registro do tempo
	 * @return	String 		- dados para a montagem do xml.
	 * @throws Exception
	 */
	public String consultarRetorno(String msisdn,long timestamp)throws Exception {
		
		PREPConexao conexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		ResultSet rsconsulta=null;
		String retorno=null;
		try 
		{
			
			String sql = "SELECT des_resposta FROM tbl_ger_trace_assinante " +
			" WHERE  idt_msisdn = ?" +
			" AND idt_timestamp_trace = ? "; 
			Object[] params = {msisdn, new Long(timestamp)};
			rsconsulta = conexao.executaPreparedQuery(sql, params, super.getIdLog());
			if(rsconsulta.next())
				retorno =rsconsulta.getString("DES_RESPOSTA");
			
		}catch(Exception e)
		{
			super.log(Definicoes.WARN, "ConsultaWIG.consultarRetorno", "Erro SQL: " + e);
			throw new GPPInternalErrorException(e.toString());
		}finally 
		{
			if (rsconsulta != null)
				rsconsulta.close();
			
			super.gerenteBancoDados.liberaConexaoPREP(conexao, super.getIdLog());
		}
		return retorno;
	}
	
	private static String montaXMLretorno(String data,String MSISDN, String IMEI, String LOC, String SINAL, String SINALBRUTO, String RETORNO)
	{
		String[] loc = LOC.split("_");
		return "<ConsultaWIGAparelho><DATA>" +data+"</DATA>"+
				"<MSISDN>"+MSISDN+"</MSISDN>"+
		"<IMEI>"+IMEI+"</IMEI>"+
		"<LOC><MCC-MNC>"+loc[0]+"</MCC-MNC><LAC>"+loc[1]+"</LAC><CELL_ID>"+loc[2]+"</CELL_ID></LOC>"+
		"<NIVELSINAL>"+SINAL+"</NIVELSINAL>"+
		"<SINAL>"+SINALBRUTO+"</SINAL>"+
		"<RETORNO>"+RETORNO+"</RETORNO></ConsultaWIGAparelho>";
	}
	
	
}