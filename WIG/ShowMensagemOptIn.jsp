<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN"
"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd">
<wml>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" 
import = "br.com.brasiltelecom.wig.entity.MensagemRetorno, javax.naming.InitialContext" %>
<%!
	private InitialContext ictx = null;
	private static final long serialVersionUID = 1L;
	public void jspInit()
	{
		try
		{
			ictx = new InitialContext();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
%><%
String sms = "";
String mensagem = "Parabens! Voce esta cadastrado para receber ofertas promocionais.";
if ( request.getAttribute("mensagemRetorno") != null )
{
	MensagemRetorno mensagemRetorno = (MensagemRetorno) request.getAttribute("mensagemRetorno");
	mensagem = (mensagemRetorno.getMensagem() == null ? mensagem : mensagemRetorno.getMensagem() );
	sms 	 = (mensagemRetorno.getTagSMS() == null ? "" : mensagemRetorno.getTagSMS() );
}
%><%=sms%>
<card>
<p>
     <%=mensagem%>
</p>
</card>
</wml>