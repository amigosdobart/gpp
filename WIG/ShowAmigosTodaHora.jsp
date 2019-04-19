<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN"
"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd">
<wml>
<card>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" 
import="br.com.brasiltelecom.wig.entity.* , java.util.Iterator , java.util.* "%>
<% 
HttpSession sessaoATH = request.getSession(true);
BrtVantagem brtVantagem    = (BrtVantagem)sessaoATH.getAttribute("brtVantagem"  );
Collection listaCelNovos   = (Collection )sessaoATH.getAttribute("listaCelNovos");
Collection listaFixNovos   = (Collection )sessaoATH.getAttribute("listaFixNovos");
MensagemRetorno msgRetorno = (MensagemRetorno)sessaoATH.getAttribute("msgRetorno");
String maquinaWIG		   = (String) sessaoATH.getAttribute("maquinaWIG");

Collection listaCelulares  = new ArrayList();
Collection listaFixos  	   = new ArrayList();
Collection listaTodosAtuaisCelular = new ArrayList();
Collection listaTodosAtuaisFixo    = new ArrayList();

String msg = "";
if (msgRetorno != null )
	msg = msgRetorno.getMensagem();

// Seleciona as Collections que serao utilizadas para popular os "inputs"
// Se a lista dos celulares e fixos novos for nula,
// as listas receberao os dados do objeto brtVantagem
if (listaCelNovos == null || listaFixNovos == null)
{
	listaCelulares.addAll(brtVantagem.getAmigosTodaHoraCelular());
	listaFixos.addAll(brtVantagem.getAmigosTodaHoraFixo());
	// Popula a lista de TODOS os acessos atuais do ATH
	listaTodosAtuaisCelular.addAll(brtVantagem.getAmigosTodaHoraCelular());
	listaTodosAtuaisFixo.addAll(brtVantagem.getAmigosTodaHoraFixo());
}
// Se a lista dos celulares e fixos nao for nula,
// as listas receberao as listas dos celulares novos e fixos novos
else
{
	listaCelulares.addAll(listaCelNovos);
	listaFixos.addAll(listaFixNovos);
}

//Define os parametros dos novos numeros a serem enviados 
// cn - Celulares Novos 
// fn - Fixos Novos
StringBuffer cn = new StringBuffer("");
StringBuffer fn = new StringBuffer("");

// Diferenca entre as quantidades de Celulares e Fixos
int diferenca = (brtVantagem.getQtdeAmigosTodaHora() - brtVantagem.getQtdeAmigosTodaHoraFixo());
int qtdeMaxima = brtVantagem.getQtdeAmigosTodaHora();
int qtdeAtual = 1;

// Monta os parametros dos celulares novos
for (int i = 1; i <= diferenca; i++)
	cn.append("$(c"+i+");");
// Monta os parametros dos fixos novos
for (int j = 1; j <= brtVantagem.getQtdeAmigosTodaHoraFixo();    j++)
	fn.append("$(f"+j+");");
%>
		<p>
		<%=msg%>
<%
//Inicializa a posicao do parametro
int posicao = 1;

/* Inicio da montagem dos inputs */
// Se a lista completa dos atuais nao for nula,
// inicia o processo de montagem dos inputs de
// acordo com a quantidade permitida de cel e fixos
if (!listaTodosAtuaisCelular.isEmpty() || !listaTodosAtuaisFixo.isEmpty())
{
	//Collection numerosARemover = new ArrayList();
	
	// Montara os inputs de celulares, ate atingir a diferenca
	for (Iterator i = listaTodosAtuaisCelular.iterator(); i.hasNext(); )
	{
		String valor = (String)i.next();
		%><input title="Celular <%=posicao%>:" value="<%=valor%>" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%
		//numerosARemover.add(valor);
		qtdeAtual++;
		if (posicao == diferenca)
		{
			//posicao = 1;
			break;
		}
		else
			posicao++;
	}
	// Monta os inputs de Celular em Branco
	while (qtdeAtual <= diferenca)
	{
		%><input title="Celular <%=posicao%>:" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%
		qtdeAtual++;
		posicao++;
	}
	
	// Reinicia a posicao de 1 para Fixo/Celular
	posicao = 1;
	//listaTodosAtuais.removeAll(numerosARemover);
	//numerosARemover.clear();
	for (Iterator j = listaTodosAtuaisFixo.iterator(); j.hasNext(); )
	{
		String valor = (String)j.next();
		%><input title="Fixo/Celular <%=posicao%>:" value="<%=valor%>" name="f<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%
		//numerosARemover.add(valor);
		qtdeAtual++;
		posicao++;
	}
	//listaTodosAtuais.removeAll(numerosARemover);
	
	// Monta os inputs de Fixo/Celular em Branco
	while (qtdeAtual <= qtdeMaxima)
	{
	%><input title="Fixo/Celular <%=posicao%>:" name="f<%=posicao%>" format="*N" emptyok="true" maxlength="10" /><%
		System.out.println("Qtde atual: " + qtdeAtual + " <= " + qtdeMaxima);
		posicao++;
		qtdeAtual++;
	}
}
else
{
	// Inicio do processo para montar os inputs que serao mostrados ao assinante
	for (Iterator i = listaCelulares.iterator(); i.hasNext() ; )
	{
	%>	<input title="Celular <%=posicao%>:" value="<%=i.next()%>" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" />
	<%
		posicao ++;
	}
	// Monta os inputs que serao mostrados em branco ao assinante
	for (int i = posicao; i <= diferenca; i++ )
	{
	%>	<input title="Celular <%=posicao%>:" name="c<%=posicao%>" format="*N" emptyok="true" maxlength="10" />
	<%
		posicao ++;
	}
	
	// Reinicia a contagem da posicao para os fixos
	posicao = 1;
	// Varre os numeros fixos ja cadastrados do ATH
	for (Iterator i = listaFixos.iterator(); i.hasNext() ; )
	{
	%>	<input title="Fixo/Celular <%=posicao%>:" value="<%=i.next()%>" name="f<%=posicao%>" format="*N" emptyok="true" maxlength="10" />
	<%
		posicao ++;
	}
	for (int i = posicao; i <= brtVantagem.getQtdeAmigosTodaHoraFixo(); i++)
	{
	%>	<input title="Fixo/Celular <%=posicao%>:" name="f<%=posicao%>" format="*N" emptyok="true" maxlength="10" />
	<%
		posicao ++;
	}
}
%>
	<setvar name="cn" value="<%=cn.toString()%>"/>
	<setvar name="fn" value="<%=fn.toString()%>"/>
	<do type="accept">
		<go href="http://<%=maquinaWIG%>/wig/amigosTodaHoraManutencao?cn=$(cn)&amp;fn=$(fn)" />
	</do>
	</p>
	</card>
</wml>