<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN"
"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd">
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" 
import="br.com.brasiltelecom.wig.entity.*,java.util.*" %>
<%
MobileMktQuestionario questionario = (MobileMktQuestionario)request.getAttribute("questionarioMkt");
String wigContainer = (String)request.getAttribute("wigContainer");
boolean possuiFF = false;
%>
<wml><%
	// Se o questionario nao possui perguntas associadas entao deve
	// ser retornado para o assinante um CARD vazio que nao realiza
	// nenhuma operacao. Este card possui um plugin chamado noresponse
	if (questionario != null && (questionario.getMobileMktPerguntas() != null || questionario.getMobileMktPerguntas().size() != 0))
	{
		String href = wigContainer + "/mobileMktAction?pe="+questionario.getMobileMktPesquisa().getId()+"&amp;qt="+questionario.getId();
		String hrefPergs = "";
		// Define o id do proximo questionario quando o fluxo deste sendo respondido
		// deve ser continuado. Caso o questionario tenha que terminar entao as respostas
		// devem indicar o card FF para este fim. Entao o parametro de proximo questionario
		// vai preenchido somente se o fluxo deve ser continuado. O proximo questionario
		// sempre serah o de id atual acrescido de 1. Se nao houver o tratamento deve ser
		// feito nos cards como mencionado acima.
		String hrefProxQuest = "&amp;pq="+ (questionario.getId()+1);
		
		// Realiza uma iteracao entre todas as perguntas definidas para este questionario
		// Para cada pergunta o CARD eh montado sendo que alem disso eh identificado tambem
		// quais serao as perguntas a serem envidas no card em que registra o envio destas
		// respostas para o banco de dados
		for (Iterator i = questionario.getMobileMktPerguntas().iterator(); i.hasNext();)
		{
			MobileMktPergunta pergunta = (MobileMktPergunta)i.next();
			hrefPergs += "&amp;pg"+pergunta.getId()+"=$(p"+pergunta.getId()+")";%>
			<card id="p<%=pergunta.getId()%>"><%
			// Para a primeira pergunta do questionario, insere uma tag
			// para ser utilizada como explicativo da pesquisa. Caso
			// nao exista este texto ou nao seja a primeira pergunta entao
			// somente a pergunta eh visualizada.
			if (pergunta.getId() == 1 && questionario.getTextoExplicativo() != null)
			{%>
				<p>
				<setvar name="m1" value="<%=questionario.getTextoExplicativo()%>"/>
				<plugin name="DITR" destvar="t" params="\x04$(m1)"/>
				</p><%
			}%>
			<p>
			<%=pergunta.getDescricaoPergunta() != null ? pergunta.getDescricaoPergunta() : ""%>
			<select name="p<%=pergunta.getId()%>" title="<%=pergunta.getTitulo() != null ? pergunta.getTitulo() : ""%>"><%
				// Realiza uma iteracao entre todas as respostas da pergunta para
				// montar a tag SELECT das opcoes a serem respondidas pelo assinante
				for (Iterator j = pergunta.getMobileMktRespostas().iterator(); j.hasNext();)
				{
					MobileMktResposta resposta = (MobileMktResposta)j.next();
					// Verifica se o Card Reference desta resposta indica o card FF.
					// Se sim, entao define uma variavel para indicar que este card
					/// deve ser construido no final do WML
					possuiFF = "ff".equals(resposta.getCardReference()) ? true : possuiFF;%>
					<option value="<%=resposta.getId()%>" onpick="#<%=resposta.getCardReference()%>"><%=resposta.getDescricaoResposta()%></option><%
				}%>
			</select>
			</p>
			</card><%
		}
		
		// Realiza a criacao dos CARDS finais do questionario. Esses CARDS serao criados da seguinte forma:
		// - Todo questionario possui o card FC. Este card indica o Fim de um questionario, porem existe a
		//   Continuidade. Portanto provavelmente um novo questionario serah retornado para o assinante
		//   OBS: Este card sempre estarah presente, ou seja pelo menos uma resposta deve chamar a execucao
		//        deste.
		// - Um card chamado FF indica o final da resposta do questionario sendo que nao haverah continuidade
		//   ou seja, mesmo se existe um questionario a ser enviado posteriormente, se a resposta chamar tal
		//   card este irah enviar as respostas por href porem irah liberar o telefone para o assinante, geralmente
		//   agradecendo ao assinante enviando a mensagem definida no objeto Questionario
		if (possuiFF)
		{%>
			<card id="ff">
			<p>
				<do type="accept">
					<go enterwait="false" href="<%=href+hrefPergs%>"/>
				</do>
			<br/><%=questionario.getMensagem()%>
			</p>
			</card><%
		}%>
		<card id="fc">
			<p>
			<do type="accept">
				<go href="<%=href+hrefPergs+hrefProxQuest%>"/>
			</do>
			</p>
		</card><%
	}
	else
	{%>
		<wigplugin name="noresponse"/>
		<card id="v">
		</card><%
	}%>
</wml>