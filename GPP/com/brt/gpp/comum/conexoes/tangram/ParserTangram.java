package com.brt.gpp.comum.conexoes.tangram;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.brt.gpp.comum.conexoes.tangram.entidade.ConteudoMensagem;
import com.brt.gpp.comum.conexoes.tangram.entidade.DestinoMensagem;
import com.brt.gpp.comum.conexoes.tangram.entidade.Notificacao;
import com.brt.gpp.comum.conexoes.tangram.entidade.ParametrosNotificacao;
import com.brt.gpp.comum.conexoes.tangram.entidade.ParametrosPacote;
import com.brt.gpp.comum.conexoes.tangram.entidade.Requisicao;

/**
 *	Classe responsável pela interpretação de requisições e notificações
 *  do Tangram. Suporta tratamento de parametros GET e documentos XML.  
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 19/09/2007
 */
public class ParserTangram 
{

	/**
	 * Gera um XML <code>tangram_request</code> de acordo com os dados 
	 * da entidade <code>Requisicao</code>.
	 * 
	 * Esse XML é utilizado para enviar uma requisição ao Tangram, por HTTP/POST ou FTP.
	 * 
	 * @param requisicao Instancia de uma requisicao Tangram
	 * @return XML tangram_request
	 */
	public static String gerarXmlRequisicao(Requisicao requisicao) throws Exception
	{
		if (requisicao == null)
			return null;
		
		if (requisicao.getDataEnvioRequisicao() == null)
			throw new IllegalArgumentException("Campo 'dataEnvioRequisicao' nao pode ser nulo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmssSSS");
		
		// Cria um Builder XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docBuilder.newDocumentBuilder();
		
		Document doc = builder.newDocument();
	
		// Gera as entidades XML
		
		Element root = doc.createElement("tangram_request");
		root.setAttribute("company_id", "" + requisicao.getIdEmpresa());
		root.setAttribute("service_id", "" + requisicao.getIdServico());
		
		Element tagSend = createNode(doc, root, "send");
		
		if (requisicao.getIndManterSessao() != null)
		{
			tagSend.setAttribute("keep_session", 
				(requisicao.getIndManterSessao().booleanValue()) ? "true" : "false");
		}
		
		if (requisicao.getIdtOrigem() != null)
		{
			Element tagSource = createNode(doc, tagSend, "source");
			setNodeValue(doc, tagSource, requisicao.getIdtOrigem());
		}
			
		if (requisicao.getDestinosMensagem() != null)
		{
			java.util.Iterator it = requisicao.getDestinosMensagem().iterator();
			while (it.hasNext())
			{
				DestinoMensagem destino = (DestinoMensagem)it.next();
				if (destino.getIdtMsisdnDestino() != null)
				{
					Element tagDestination = createNode(doc, tagSend, "destination");
					setNodeValue(doc, tagDestination, destino.getIdtMsisdnDestino().substring(2));
				}
			}			
		}
		
		if (requisicao.getIdCanal() != null)
		{
			Element tagChannelId = createNode(doc, tagSend, "channel_id");
			setNodeValue(doc, tagChannelId, requisicao.getIdCanal().toString());
		}
		
		if (requisicao.getConteudosMensagem() != null)
		{
			java.util.Iterator it = requisicao.getConteudosMensagem().iterator();
			while (it.hasNext())
			{
				ConteudoMensagem conteudo = (ConteudoMensagem)it.next();
				Element tagText = createNode(doc, tagSend, "text");
				
				if (conteudo.getUdh() != null)
					tagText.setAttribute("udh", conteudo.getUdh());
				
				if (conteudo.getIndTruncamento() != null &&
						conteudo.getIndTruncamento().booleanValue())
					tagText.setAttribute("method", "truncate");
				
				if (conteudo.getIndBinario() != null)
					tagText.setAttribute("binary", 
						conteudo.getIndBinario().booleanValue() ? "true" : "false");
				
				if (conteudo.getTextoConteudo() != null)
					setNodeValue(doc, tagText, conteudo.getTextoConteudo());
			}			
		}
		
		createNode(doc, tagSend, "user_data_header"); // OBSOLETO no tangram
		
		if (requisicao.getDataValidade() != null || requisicao.getIndValidadeRelativa() != null)
		{
			Element tagValidity = createNode(doc, tagSend, "validity");
			
			if (requisicao.getDataValidade() != null)
				setNodeValue(doc, tagValidity, sdf.format(requisicao.getDataValidade()));

			if (requisicao.getIndValidadeRelativa() != null)
			{
				tagValidity.setAttribute("relative", 
						requisicao.getIndValidadeRelativa().booleanValue() ? "true" : "false");
			}
		}
		
		if (requisicao.getDataAgendamento() != null || requisicao.getIndAgendamentoRelativo() != null)
		{
			Element tagSchedule = createNode(doc, tagSend, "schedule");
			
			if (requisicao.getDataAgendamento() != null)
				setNodeValue(doc, tagSchedule, sdf.format(requisicao.getDataAgendamento()));
			
			if (requisicao.getIndAgendamentoRelativo() != null)
			{
				tagSchedule.setAttribute("relative", 
						requisicao.getIndAgendamentoRelativo().booleanValue() ? "true" : "false");
			}
		}
		
		if (requisicao.getParametrosNotificacao() != null)
		{
			ParametrosNotificacao notificacao = requisicao.getParametrosNotificacao();
			Element tagNotification = createNode(doc, tagSend, "notification");
			
			if (notificacao.getEnderecoRetorno() != null)
				setNodeValue(doc, tagNotification, notificacao.getEnderecoRetorno());
			
			if (notificacao.getTipoRetorno() != null)
				tagNotification.setAttribute("calltype", notificacao.getTipoRetorno().toString());
			
			if (notificacao.getTiposEvento() != null)
				tagNotification.setAttribute("type", notificacao.getTiposEvento().toString());
		}
		
		if (requisicao.getParametrosPacote() != null)
		{
			ParametrosPacote pacote = requisicao.getParametrosPacote();
			Element tagPackage = createNode(doc, tagSend, "package");
						
			if (pacote.getIdExterno()!= null)
				tagPackage.setAttribute("external_id", pacote.getIdExterno());
			
			if (pacote.getNome() != null)
				tagPackage.setAttribute("name", pacote.getNome());
			
			if (pacote.getDescricao() != null)
				tagPackage.setAttribute("description", pacote.getDescricao());
			
			if (pacote.getInterfaceCliente() != null)
				tagPackage.setAttribute("interface", pacote.getInterfaceCliente().toString());
			
			if (pacote.getIdtMsisdnDono() != null)
				tagPackage.setAttribute("owner_ctn", pacote.getIdtMsisdnDono().substring(2));
			
			if (pacote.getCopyright() != null)
				tagPackage.setAttribute("copyright", pacote.getCopyright());
		}
		
		if (requisicao.getNumTentativasEntrega() != null ||
				requisicao.getIntervaloTentativa() != null)
		{
			Element tagRetries = createNode(doc, tagSend, "retries");
			
			if (requisicao.getNumTentativasEntrega() != null)
				tagRetries.setAttribute("max", requisicao.getNumTentativasEntrega().toString());
			
			if (requisicao.getIntervaloTentativa() != null)
				tagRetries.setAttribute("interval", requisicao.getIntervaloTentativa().toString());			
		}
		
		createNode(doc, tagSend, "service_type"); // OBSOLETO no tangram
		
		if (requisicao.getIdMensagemMO() != null)
		{
			Element tagMOMessageId = createNode(doc, tagSend, "mo_message_id");
			setNodeValue(doc, tagMOMessageId, requisicao.getIdMensagemMO());
		}
		
		if (requisicao.getAppSpecific() != null)
		{
			Element tagAppSpecific = createNode(doc, tagSend, "app_specific");
			setNodeValue(doc, tagAppSpecific, requisicao.getAppSpecific());
		}
		
		if (requisicao.getAppRequestId() != null)
		{
			Element tagAppRequestId = createNode(doc, tagSend, "app_request_id");
			setNodeValue(doc, tagAppRequestId, requisicao.getAppRequestId());
		}
		
		Element tagRequestDatetime = createNode(doc, tagSend, "request_datetime");
		setNodeValue(doc, tagRequestDatetime, sdf.format(requisicao.getDataEnvioRequisicao()));
		
		// Converte para String
	
		StringWriter stringWriter = new StringWriter();
		DOMSource domSource = new DOMSource(root);
		StreamResult streamResult = new StreamResult(stringWriter);
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.VERSION,"1.0");
		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
		serializer.transform(domSource, streamResult); 
		
		return stringWriter.toString();
	}
	
	/**
	 * Gera um XML <code>notification_request</code> de acordo com os dados 
	 * da entidade <code>Notificacao</code>.
	 * 
	 * Esse XML é utilizado para enviar uma notificacao (simulando o Tangram), por HTTP/POST.
	 * 
	 * @param notificacao Instancia de uma Notificacao Tangram
	 * @return XML notification_request
	 */
	public static String gerarXmlNotificacao(Notificacao notificacao) throws Exception
	{
		if (notificacao == null)
			return null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmm");
		
		if (notificacao.getStatusNotificacao() == null)
			throw new IllegalArgumentException("Campo 'statusNotificacao' nao pode ser nulo.");
		
		if (notificacao.getIdEntregador() == null)
			throw new IllegalArgumentException("Campo 'idEntregado' nao pode ser nulo.");
		
		if (notificacao.getIdMensagem() == null)
			throw new IllegalArgumentException("Campo 'idMensagem' nao pode ser nulo.");
		
		if (notificacao.getIdMensagemSMSC() == null)
			throw new IllegalArgumentException("Campo 'idMensagemSMSC' nao pode ser nulo.");
		
		if (notificacao.getIdtOrigem() == null)
			throw new IllegalArgumentException("Campo 'idtOrigem' nao pode ser nulo.");
		
		if (notificacao.getIdtMsisdnDestino() == null)
			throw new IllegalArgumentException("Campo 'idtMsisdnDestino' nao pode ser nulo.");
		
		if (notificacao.getDataEnvioRequisicao() == null)
			throw new IllegalArgumentException("Campo 'dataEnvioRequisicao' nao pode ser nulo.");
		
		if (notificacao.getDataNotificacao() == null)
			throw new IllegalArgumentException("Campo 'dataNotificacao' nao pode ser nulo.");
		
		// Cria um Builder XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docBuilder.newDocumentBuilder();
		
		Document doc = builder.newDocument();
	
		// Gera as entidades XML
		
		Element root = doc.createElement("notification_request");
		root.setAttribute("status", notificacao.getStatusNotificacao().toString());
		root.setAttribute("version", "1");
		
		Element tagDispatcherId = createNode(doc, root, "dispatcher_id");
		setNodeValue(doc, tagDispatcherId, notificacao.getIdEntregador().toString());
		
		Element tagMessageId = createNode(doc, root, "message_id");
		setNodeValue(doc, tagMessageId, notificacao.getIdMensagem());
		
		Element tagSmscMessageId = createNode(doc, root, "smsc_message_id");
		setNodeValue(doc, tagSmscMessageId, notificacao.getIdMensagemSMSC());
		
		Element tagSource = createNode(doc, root, "source");
		setNodeValue(doc, tagSource, notificacao.getIdtOrigem());
		
		Element tagDestination = createNode(doc, root, "destination");
		setNodeValue(doc, tagDestination, notificacao.getIdtMsisdnDestino());
		
		Element tagRequestDatetime = createNode(doc, root, "request_datetime");
		setNodeValue(doc, tagRequestDatetime, sdf.format(notificacao.getDataEnvioRequisicao()));
		
		Element tagNotificationDatetime = createNode(doc, root, "notification_datetime");
		setNodeValue(doc, tagNotificationDatetime, sdf.format(notificacao.getDataNotificacao()));
		
		if (notificacao.getIdPacote() != null || notificacao.getIndicePacote() != null)
		{
			Element tagPackage = createNode(doc, root, "package");
			
			if (notificacao.getIdPacote() != null)
				tagPackage.setAttribute("id", notificacao.getIdPacote().toString());
			
			if (notificacao.getIdPacote() != null)
				tagPackage.setAttribute("index", notificacao.getIndicePacote().toString());
			
		}
		
		if (notificacao.getAppSpecific() != null)
		{
			Element tagAppSpecificId = createNode(doc, root, "app_specific_id");
			setNodeValue(doc, tagAppSpecificId, notificacao.getAppSpecific());
		}

		// Converte para String
	
		StringWriter stringWriter = new StringWriter();
		DOMSource domSource = new DOMSource(root);
		StreamResult streamResult = new StreamResult(stringWriter);
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.VERSION,"1.0");
		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
		serializer.transform(domSource, streamResult); 
		
		return stringWriter.toString();
	}
	
	/**
	 * Gera um XML <code>notification_response</code> de acordo com os dados
	 * de retorno da entidade <code>Notificacao</code>.
	 * 
	 * Esse XML é utilizado para enviar uma resposta de notificação ao Tangram, por HTTP.
	 * 
	 * @param notificacao Instancia de uma notificacao Tangram
	 * @return XML notification_response
	 */
	public static String gerarXmlRetornoNotificacao(Notificacao notificacao) throws Exception
	{
		if (notificacao == null)
			return null;
		
		if (notificacao.getIdMensagem() == null)
			throw new IllegalArgumentException("Campo 'idMensagem' nao pode ser nulo.");
		
		if (notificacao.getCodRetornoAplicacao() == null)
			throw new IllegalArgumentException("Campo 'codRetornoAplicacao' nao pode ser nulo.");
		
		// Cria um Builder XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docBuilder.newDocumentBuilder();
		
		Document doc = builder.newDocument();
	
		// Gera as entidades XML
		
		Element root = doc.createElement("notification_response");
		root.setAttribute("version", "1");
		root.setAttribute("ack", "true");
		
		Element tagMessageId = createNode(doc, root, "message_id");
		setNodeValue(doc, tagMessageId, notificacao.getIdMensagem());
			
		Element tagDescription = createNode(doc, root, "description");
		tagDescription.setAttribute("code", notificacao.getCodRetornoAplicacao().toString());
		
		if (notificacao.getDescRetornoAplicacao() != null)
			setNodeValue(doc, tagDescription, notificacao.getDescRetornoAplicacao());
		
		// Converte para String
		
		StringWriter stringWriter = new StringWriter();
		DOMSource domSource = new DOMSource(root);
		StreamResult streamResult = new StreamResult(stringWriter);
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.VERSION,"1.0");
		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
		serializer.transform(domSource, streamResult); 
		
		return stringWriter.toString();
	}
	
	/**
	 * Gera uma string de parametros GET (Url encoded) de acordo com os dados 
	 * da entidade <code>Requisicao</code>.
	 * 
	 * Essa string é utilizada para enviar uma requisição ao Tangram, por HTTP/GET.
	 * 
	 * @param requisicao Instancia de uma requisicao Tangram
	 * @return String de parametros GET
	 */
	public static String gerarUrlRequisicao(Requisicao requisicao) throws Exception
	{
		if (requisicao == null)
			return null;
		
		if (requisicao.getDataEnvioRequisicao() == null)
			throw new IllegalArgumentException("Campo 'dataEnvioRequisicao' nao pode ser nulo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmssSSS");
		
		// Gera os pares de paramentros (formato de URL)
		
		List params = new ArrayList();
		
		params.add("tangram_request@company_id=" + requisicao.getIdEmpresa());
		params.add("tangram_request@service_id=" + requisicao.getIdServico());
		
		if (requisicao.getIndManterSessao() != null)
			params.add("send@keep_session=" + (requisicao.getIndManterSessao()
					.booleanValue() ? "true" : "false"));
		
		if (requisicao.getIdtOrigem() != null)
			params.add("source=" + requisicao.getIdtOrigem());
			
		if (requisicao.getDestinosMensagem() != null)
		{
			java.util.Iterator it = requisicao.getDestinosMensagem().iterator();
			while (it.hasNext())
			{
				DestinoMensagem destino = (DestinoMensagem)it.next();
				if (destino.getIdtMsisdnDestino() != null)
					params.add("destination=" + requisicao.getIdtOrigem());
			}			
		}
		
		if (requisicao.getIdCanal() != null)
			params.add("channel_id=" + requisicao.getIdCanal().toString());
		
		if (requisicao.getConteudosMensagem() != null)
		{
			java.util.Iterator it = requisicao.getConteudosMensagem().iterator();
			while (it.hasNext())
			{
				ConteudoMensagem conteudo = (ConteudoMensagem)it.next();
				
				if (conteudo.getUdh() != null)
					params.add("text@udh=" + URLEncoder.encode(conteudo.getUdh(),"UTF-8"));
				
				if (conteudo.getIndTruncamento() != null &&
						conteudo.getIndTruncamento().booleanValue())
					params.add("text@method=truncate");
				
				if (conteudo.getIndBinario() != null)
					params.add("text@binary=" +	(conteudo.getIndBinario().booleanValue() ? "true" : "false"));
				
				if (conteudo.getTextoConteudo() != null)
					params.add("text=" +  URLEncoder.encode(conteudo.getTextoConteudo(),"UTF-8"));
			}			
		}
		
		if (requisicao.getDataValidade() != null || requisicao.getIndValidadeRelativa() != null)
		{
			if (requisicao.getIndValidadeRelativa() != null)
			{
				params.add("validity@relative=" + (requisicao.
						getIndValidadeRelativa().booleanValue() ? "true" : "false"));
			}
			
			if (requisicao.getDataValidade() != null)
				params.add("validity=" + sdf.format(requisicao.getDataValidade()));
		}
		
		if (requisicao.getDataAgendamento() != null || requisicao.getIndAgendamentoRelativo() != null)
		{
			if (requisicao.getIndAgendamentoRelativo() != null)
			{
				params.add("schedule@relative=" + (requisicao.
						getIndAgendamentoRelativo().booleanValue() ? "true" : "false"));
			}
			
			if (requisicao.getDataAgendamento() != null)
				params.add("schedule=" + sdf.format(requisicao.getDataAgendamento()));			
		}
		
		if (requisicao.getParametrosNotificacao() != null)
		{
			ParametrosNotificacao notificacao = requisicao.getParametrosNotificacao();
			
			if (notificacao.getTipoRetorno() != null)
				params.add("notification@calltype=" + notificacao.getTipoRetorno().toString());
			
			if (notificacao.getTiposEvento() != null)
				params.add("notification@type=" +  notificacao.getTiposEvento().toString());
			
			if (notificacao.getEnderecoRetorno() != null)
				params.add("notification=" + URLEncoder.encode(notificacao.getEnderecoRetorno(),"UTF-8"));
		}
		
		if (requisicao.getParametrosPacote() != null)
		{
			ParametrosPacote pacote = requisicao.getParametrosPacote();
			
			if (pacote.getIdExterno()!= null)
				params.add("package@external_id=" + URLEncoder.encode(pacote.getIdExterno(),"UTF-8"));
			
			if (pacote.getNome() != null)
				params.add("package@name=" + URLEncoder.encode(pacote.getNome(),"UTF-8"));
			
			if (pacote.getDescricao() != null)
				params.add("package@description=" + URLEncoder.encode(pacote.getDescricao(),"UTF-8"));
			
			if (pacote.getInterfaceCliente() != null)
				params.add("package@interface=" + pacote.getInterfaceCliente().toString());
			
			if (pacote.getIdtMsisdnDono() != null)
				params.add("package@owner_ctn=" + pacote.getIdtMsisdnDono().substring(2));
			
			if (pacote.getCopyright() != null)
				params.add("package@copyright=" + URLEncoder.encode(pacote.getCopyright(),"UTF-8"));
		}
		
		if (requisicao.getNumTentativasEntrega() != null ||
				requisicao.getIntervaloTentativa() != null)
		{
			if (requisicao.getNumTentativasEntrega() != null)
				params.add("retries@max=" + requisicao.getNumTentativasEntrega().toString());
			
			if (requisicao.getIntervaloTentativa() != null)
				params.add("retries@interval=" + requisicao.getIntervaloTentativa().toString());			
		}
		
		if (requisicao.getIdMensagemMO() != null)
			params.add("mo_message_id=" + URLEncoder.encode(requisicao.getIdMensagemMO(),"UTF-8"));
		
		if (requisicao.getAppSpecific() != null)
			params.add("app_specific=" + URLEncoder.encode(requisicao.getAppSpecific(),"UTF-8"));
		
		if (requisicao.getAppRequestId() != null)
			params.add("app_request_id=" + URLEncoder.encode(requisicao.getAppRequestId(),"UTF-8"));
		
		params.add("request_datetime=" + sdf.format(requisicao.getDataEnvioRequisicao()));
		
		// Converte para String
	
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < params.size() - 1; i++)
			buffer.append((String)params.get(i) + "&");
			
		buffer.append((String)params.get(params.size() - 1));
		
		return buffer.toString();
	}
		
	/**
	 * Interpreta os dados de retorno de uma requisição Tangram, gravando-os
	 * na própria entidade <code>Requisicao</code>.
	 * 
	 * Método usado para fazer o parser de retorno de requisição do Tangram,
	 * recebido HTTP ou FTP.
	 * 
	 * @param xml XML tangram_response
	 * @param requisicao Instancia de uma requisicao Tangram
	 */
	public static void processarXmlRetornoRequisicao(String xml, Requisicao requisicao) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmssSSS");
		
		// Cria um Parser XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		Document doc = parser.parse(is);
		
		// Interpreta os dados
		
		Element root = getNode(doc, "tangram_response");
		
		// Lista os destinos de mensagem recebidos no XML
		
		NodeList list = root.getElementsByTagName("destination");
		
		// Cria uma collection de destinos de mensagem caso ela nao
		// exista na requisicao. Isso é necessário para armazenar
		// os dados recebidos do Tangram
		
		if (requisicao.getDestinosMensagem() == null && list.getLength() > 0)
			requisicao.setDestinosMensagem(new ArrayList());
		
		// Atualiza os dados relativos aos destinos de mensagem. Caso o 
		// destino (identificado pelo IdtMsisdnDestino) não exista na 
		// requisicao, é criada uma nova instancia de DestinoMensagem
		
		for (int i = 0; i < list.getLength(); i++)
		{
			Element tagDestination = (Element)list.item(i);
			List idsMensagem = new ArrayList();			
			String idtMsisdnDestino = null;				
			
			// Referencia ao DestinoMensagem correspondente na requisicao
			// Esse é o objeto a ser atualizado por esse parser
			
			DestinoMensagem destinoMensagem = null;		
			
			// Processa os nodos filhos da 'tagDestination' capturando os 
			// dados de IdMensagem e IdtMsisdnDestino
			
			NodeList childs = tagDestination.getChildNodes();
			for (int y = 0; y < childs.getLength(); y++)
			{
				Node child = childs.item(y);
				
				if (child.getNodeType() == Node.ELEMENT_NODE &&
						child.getNodeName().equals("message_id"))
				{
					idsMensagem.add(child.getFirstChild().getNodeValue());
				}
				else if (child.getNodeType() == Node.TEXT_NODE)
				{
					idtMsisdnDestino = "55" + child.getNodeValue();
				}
			}
			
			// Se o Msisdn do destinatário não foi informado pelo Tangram, 
			// uma nova instancia de DestinoMensagem é criada com o atributo
			// IdtMsisdnDestino nulo!! O objetivo é evitar a perda de informações
			// recebidas do Tangram.
			
			if (idtMsisdnDestino == null)
			{
				destinoMensagem = new DestinoMensagem();
				requisicao.getDestinosMensagem().add(destinoMensagem);
			}
			else
			{
				// Localiza a refencia correta desse destino na collection
				// destinosMensagem da requisicao.
				
				destinoMensagem = findDestinoByMsisdn(requisicao, idtMsisdnDestino);
			}
			
			// Atualiza, por fim, os dados de retorno relativos a esse destino
			
			destinoMensagem.setIdsMensagem(idsMensagem);
			
			String code = tagDestination.getAttribute("code");
			if (code != null && !code.equals(""))
				destinoMensagem.setCodRetorno(new Short(code));
			
			String sessionId = tagDestination.getAttribute("session_id");
			if (sessionId != null && !sessionId.equals(""))
				destinoMensagem.setIdSessao(sessionId);
			
			String packageId = tagDestination.getAttribute("package_id");
			if (packageId != null && !packageId.equals(""))
				destinoMensagem.setIdPacote(packageId);
		}
		
		Element tagDescription = (Element)root.getElementsByTagName("description").item(0);
		String code = tagDescription.getAttribute("code");
		if (code != null && !code.equals(""))
			requisicao.setCodRetorno(new Short(code));
		
		requisicao.setDataResposta(sdf.parse(getNodeValue(root, "response_datetime")));
	}
	
	/**
	 * Interpreta os dados de uma notificação recebida do Tangram, gravando-os
	 * em uma nova entidade <code>Notificacao</code>.
	 * 
	 * Método usado para fazer o parser de notificacoes do Tangram, 
	 * recebidas por HTTP/POST.
	 * 
	 * @param xml XML notification_request
	 */
	public static Notificacao processarXmlNotificacao(String xml) throws Exception
	{
		Notificacao notificacao = new Notificacao();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmm");
		
		// Cria um Parser XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		Document doc = parser.parse(is);
		
		// Interpreta os dados
		
		Element root = getNode(doc, "notification_request");
		String status = root.getAttribute("status");
		notificacao.setStatusNotificacao(new Short(status));
		
		notificacao.setIdEntregador(new Integer(getNodeValue(root, "dispatcher_id")));
		notificacao.setIdMensagem(getNodeValue(root, "message_id"));
		notificacao.setIdMensagemSMSC(getNodeValue(root, "smsc_message_id"));
		notificacao.setIdtOrigem(getNodeValue(root, "source"));
		notificacao.setIdtMsisdnDestino("55" + getNodeValue(root, "destination"));
		notificacao.setDataEnvioRequisicao(sdf.parse(getNodeValue(root, "request_datetime")));
		notificacao.setDataNotificacao(sdf.parse(getNodeValue(root, "notification_datetime")));
		
		Element tagPackage = getNode(doc, "package");
		if (tagPackage != null)
		{
			String id = tagPackage.getAttribute("id");
			String index = tagPackage.getAttribute("index");
			
			if (id != null && !id.equals(""))
				notificacao.setIdPacote(id);
			
			if (index != null && !index.equals(""))
				notificacao.setIndicePacote(new Integer(index));			
		}
		
		Element tagAppSpecificId = getNode(doc, "app_specific_id");
		if (tagAppSpecificId != null)
		{
			if (tagAppSpecificId.hasChildNodes())
				notificacao.setAppSpecific(tagAppSpecificId.getFirstChild().getNodeValue());
		}
		
		return notificacao;
	}
	
	/**
	 * Interpreta os dados de uma notificação recebida do Tangram, gravando-os
	 * em uma nova entidade <code>Notificacao</code>.
	 * 
	 * Método usado para fazer o parser de notificacoes do Tangram, 
	 * recebidas por HTTP/GET.
	 * 
	 * @param parametrosUrl Mapa de parâmetros de Url (vide HttpServletRequest)
	 */
	public static Notificacao processarUrlNotificacao(Map parametrosUrl) throws Exception
	{
		Notificacao notificacao = new Notificacao();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmm");
				
		// Interpreta os dados
		
		String status = (String)parametrosUrl.get("notification_request@status");
		notificacao.setStatusNotificacao(new Short(status));
		
		notificacao.setIdEntregador(new Integer((String)parametrosUrl.get("dispatcher_id")));
		notificacao.setIdMensagem((String)parametrosUrl.get("message_id"));
		notificacao.setIdMensagemSMSC((String)parametrosUrl.get("smsc_message_id"));
		notificacao.setIdtOrigem((String)parametrosUrl.get("source"));
		notificacao.setIdtMsisdnDestino("55" + (String)parametrosUrl.get("destination"));
		notificacao.setDataEnvioRequisicao(sdf.parse((String)parametrosUrl.get("request_datetime")));
		notificacao.setDataNotificacao(sdf.parse((String)parametrosUrl.get("notification_datetime")));
		
		String packageId = (String)parametrosUrl.get("package@id");
		String packageIndex = (String)parametrosUrl.get("package@index");
		String appSpecificId = (String)parametrosUrl.get("app_specific_id");
		
		if (packageId != null && !packageId.equals(""))
			notificacao.setIdPacote(packageId);
		
		if (packageIndex != null && !packageIndex.equals(""))
			notificacao.setIndicePacote(new Integer(packageIndex));	
		
		if (appSpecificId != null && !appSpecificId.equals(""))
			notificacao.setAppSpecific(appSpecificId);	
		
		return notificacao;
	}
	
	/**
	 * Retorna o conteúdo texto de um elemento xml.
	 * 
	 * @param parent	Elemento pai. Instancia de <code>Element</code>
	 * @param child		Elemento filho (nome da tag)
	 * @return	Conteúdo texto da tag especificada em 'child'
	 */
	private static String getNodeValue(Element parent, String child)
	{
		Element tag = (Element)parent.getElementsByTagName(child).item(0);
		
		if (tag.hasChildNodes())
			return tag.getFirstChild().getNodeValue();
		
		return null;
	}
	
	/**
	 * Cria um elemento XML.
	 * 
	 * @param doc		Factory de novas tags. Instancia de <code>Document</code>
	 * @param parent	Elemento pai (que conterá o novo elemento).
	 * @param name		TagName do novo elemento
	 * @return	Elemento
	 */
	private static Element createNode(Document doc, Element parent, String name)
	{
		Element el = doc.createElement(name);
		parent.appendChild(el);
		return el;
	}
	
	/**
	 * Define o conteúdo texto de um elemento xml.
	 * 
	 * @param doc		Factory de novas tags. Instancia de <code>Document</code>
	 * @param element	Instancia de <code>Element</code>
	 * @param value		Conteúdo texto da tag especificada em 'element'
	 */
	private static void setNodeValue(Document doc, Element element, String value)
	{
		element.appendChild(doc.createTextNode(value));
	}
	
	/**
	 * Retorna um elemento xml
	 * 
	 * @param from			Instancia de <code>Document</code>
	 * @param nodeName		TagName
	 * @return Element
	 */
	private static Element getNode(Document from, String nodeName)
	{
		return (Element)(from.getElementsByTagName(nodeName).item(0));
	}
	
	/**
	 * Localiza uma instancia de 'DestinoMensagem' dentro de 'requisicao'
	 * de acordo com o 'idtMsisdnDestino' informado. Caso nao seja encontrada,
	 * uma nova instancia de DestinoMensagem é criada e adicionada dentro 
	 * da requisicao.
	 * 
	 * @param requisicao
	 * @param idtMsisdnDestino
	 * @return DestinoMensagem
	 */
	private static DestinoMensagem findDestinoByMsisdn(Requisicao requisicao, String idtMsisdnDestino)
	{
		DestinoMensagem destino = null;
		Iterator it = requisicao.getDestinosMensagem().iterator();
		
		while (it.hasNext())
		{
			DestinoMensagem destinoTeste = (DestinoMensagem)it.next();
			if (destinoTeste.getIdtMsisdnDestino() != null &&
					destinoTeste.getIdtMsisdnDestino().equals(idtMsisdnDestino))
			{
				destino = destinoTeste;
				break;
			}
		}
		
		if (destino == null)
		{
			destino = new DestinoMensagem();
			destino.setIdtMsisdnDestino(idtMsisdnDestino);
			requisicao.getDestinosMensagem().add(destino);
		}
				
		return destino;
	}
	
}
