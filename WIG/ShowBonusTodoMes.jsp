<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN"
"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd">
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" 
import="br.com.brasiltelecom.wig.entity.*" %>
<%
HttpSession sessaoBTM      = request.getSession(true);
BrtVantagem brtVantagem    = (BrtVantagem)sessaoBTM.getAttribute("brtVantagem");
String bonusNovo 		   = (String) sessaoBTM.getAttribute("bonusNovo");
MensagemRetorno msgRetorno = (MensagemRetorno) sessaoBTM.getAttribute("msgRetorno");
String maquinaWIG		   = (String) sessaoBTM.getAttribute("maquinaWIG");
String msg = "";
if (msgRetorno != null )
	msg = msgRetorno.getMensagem();
%><wml>
<card>
	<p>
	<%=msg%>
	<input title="Bonus Todo Mes:" value="<%=(brtVantagem.getMsisdnBonusTodoMes() == null ?  "" : brtVantagem.getMsisdnBonusTodoMes())%>" name="f1" format="*N" emptyok="true" maxlength="10" />
	<do type="accept">
		<go href="http://<%=maquinaWIG%>/wig/bonusTodoMesManutencao?fn=$(f1)" />
	</do>
	</p>
</card>
</wml>