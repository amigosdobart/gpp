<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN"
"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd">
<wml>
<card>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" 
import="br.com.brasiltelecom.wig.entity.* , java.util.Iterator , java.util.* "%>
<% 
// Pega os atributos do objeto BrtVantagem, listaCelNovos, listaFixNovos e msgRetorno
HttpSession sessaoATH = request.getSession(true);
BrtVantagem brtVantagemATH   = (BrtVantagem)sessaoATH.getAttribute("brtVantagem");
Collection listaNumerosNovos = (Collection )sessaoATH.getAttribute("listaNumerosNovos");
Collection listaNumerosAtuais= brtVantagemATH.getAmigosTodaHoraCelular();
MensagemRetorno msgRetorno   = (MensagemRetorno)sessaoATH.getAttribute("msgRetorno");
String maquinaWIG		     = (String)sessaoATH.getAttribute("maquinaWIG");

// Recebimento da mensagem, caso haja
String msg = "";
if (msgRetorno != null )
	msg = msgRetorno.getMensagem();

StringBuffer cn = new StringBuffer("");
// Monta os parametros dos celulares novos
for (int i = 1; i <= brtVantagemATH.getQtdeAmigosTodaHora(); i++)
	cn.append("$(c"+i+");");
%>
		<p>
		<%=msg%>
<%
// Inicia a posicao dos inputs
int posicao = 1;
// Inicio da montagem dos inputs
if (listaNumerosNovos != null && !listaNumerosNovos.isEmpty())
{
	// Caso alguma validacao tenha falhado, o assinante
	// recebe um novo formulario com os numeros digitados
	// Monta os inputs com os numeros digitados pelo assinante
	for (Iterator i = listaNumerosNovos.iterator(); i.hasNext(); )
	{
		String valor = (String) i.next();
		%><input title="Amigo <%=posicao%>:" value="<%=valor%>" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%
		posicao++;
	}
}
else 
	if (!listaNumerosAtuais.isEmpty())
	{
		for (Iterator i = listaNumerosAtuais.iterator(); i.hasNext(); )
		{
			String valor = (String) i.next();
			%><input title="Amigo <%=posicao%>:" value="<%=valor%>" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%="\n"%><%
			posicao++;
		}
	}
// Monta os inputs restantes em branco, caso haja
while (posicao <= brtVantagemATH.getQtdeAmigosTodaHora())
{
	%><input title="Amigo <%=posicao%>:" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%="\n"%><%
	posicao++;
}
%>
	<setvar name="cn" value="<%=cn.toString()%>"/>
	<do type="accept">
		<go href="http://<%=maquinaWIG%>/wig/novoAmigosTodaHoraManutencao?cn=$(cn)" />
	</do>
	</p>
	</card>
</wml>