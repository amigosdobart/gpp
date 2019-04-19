<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN" "http://www.smarttrust.com/DTD/WIG-WML4.0.dtd"> 
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" %>
<wml>
	<%if(request.getAttribute("telaInicial") != null){%>
	<%if(request.getAttribute("itemMenu") == null) { %>
	  <wigplugin name="sendserversm"> 
	    <param name="userdata" value="Para se cadastrar, utilize o MenuBrTGSM - PrePago - OutrasOpcoes - IncluiCadastro. Caso nao possua esta opcao, acesse Personalizar - Incluir - PrePago"/> 
	    <param name="destaddress" value="<%= request.getParameter("MSISDN") %>"/> 
	  </wigplugin>
	<%}%>

	<card>
		<p>
			<setvar name="tip" value="1"/>
			<playtone toneid="general-beep" duration="1"/>
Desbloqueie seu celular fazendo o cadastro e ganhe R$10! Clique OK para continuar
			<setvar name="resp" value="S"/>
			<go href="#cpf"/>
		</p>
	</card>
	<% } // Tela Inicial%>
	<% if(request.getAttribute("telaCadastro") != null) { %>
		<% if(request.getAttribute("mensagem") != null) { %>
		<card>
			<p><%= request.getAttribute("mensagem") %>
				<setvar name="tip" value="1"/>
				<setvar name="resp" value="S"/>
				<setvar name="tipo" value=""/>
				<go href="#cpf"/>
			</p>
		</card>
		<% }//Mensagem %>
		<card id="cpf">
			<p>
				<setvar name="try" value="<%= request.getAttribute("try")==null?"0":request.getAttribute("try") %>"/>
				<setvar name="tipo" value="CPF"/>
				<setvar name="cep" value="<%= request.getParameter("cep")==null?"":request.getParameter("cep") %>"/>
				<setvar name="id" value="<%= request.getParameter("id")==null?"":request.getParameter("id") %>"/>
				<setvar name="pdv" value="<%=request.getParameter("pdv")==null?"":request.getParameter("pdv") %>"/>
			</p>
			<p>
				<input title="CPF(Somente numeros):" name="id" value="$(id)"  emptyok="false" format="*N" maxlength="11"/>
				<input title="Cep(Somente numeros):" name="cep" value="$(cep)" emptyok="false" format="*N" maxlength="8"/>
				
				<% /*!-- Inicio da alteracao do PDV 
				Retirar o comentario desse INPUT, do SETVAR logo acima, 
				do 'No. carimbado na NF:' logo abaixo e alterar o valor do parametro na URL para $(pdv) */
				%>
				<input title="No. carimbado na NF:" name="pdv" value="$(pdv)" emptyok="true" format="*N" maxlength="6"/>
				<%/*!-- Final da alteracao do PDV -- */%>
			
			</p>
			<p>
				Confirme:<br/>
				$(tipo): $(id)<br/>
				Cep: $(cep)<br/>
				No. carimbado na NF: $(pdv)
			</p>
			<p>
				<do type="accept">
					<go href="http://<%= application.getAttribute("ServidorWIG")%>:<%= application.getAttribute("PortaServidorWIG")%>/wig/cadpre?pdv=$(pdv)&amp;t=$(tip)&amp;resp=$(resp)&amp;tipo=$(tipo)&amp;id=$(id)&amp;cep=$(cep)&amp;try=$(try)&amp;cdm=<%= (String)request.getAttribute("CDM") %>"/>
				</do>
			</p>
		</card>
	<% } // Tela de cadastro %>
	
	<card id="c">
		<p><%= request.getAttribute("mensagemSucesso") == null?"":request.getAttribute("mensagemSucesso")%>
			<% if("N".equalsIgnoreCase((String)request.getAttribute("CDM"))) {%>

					<plugin name="*PAD" destvar="c" params="U\x10\x04"/>
			<%} else { %>
			<providelocalinfo cmdqualifier="imei" destvar="IMEI"/>
				<do type="accept">
					<go enterwait="false" href="!hsm!?sit=1&amp;I=$(IMEI)"/>
				</do>
			<% } %>
		</p>
	</card>
</wml>
