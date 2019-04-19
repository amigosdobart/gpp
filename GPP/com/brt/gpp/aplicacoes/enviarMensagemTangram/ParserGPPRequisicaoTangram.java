package com.brt.gpp.aplicacoes.enviarMensagemTangram;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.brt.gpp.aplicacoes.enviarMensagemTangram.entidades.GPPRequisicaoTangram;
import com.brt.gpp.comum.conexoes.tangram.entidade.Requisicao;

/**
 * Parser para o XML GPPRequisicaoTangram.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/09/2007
 */
public class ParserGPPRequisicaoTangram 
{
	/**
	 * Faz o parser do xml 'GPPRequisicaoTangram' para uma entidade Requisicao.
	 * 
	 * Sintaxe do XML: <br>
	 * 
	 * <GPPRequisicaoTangram>
	 * 		<idServico>5</idServico>
	 * 		<idCanal>5<idCanal>
	 * 		<idtOrigem>20050</idtOrigem>
	 * 		<idtMsisdnDestino>556184001000;556184001001;556184001002</idtOrigem>
	 * 		<textoConteudo>Olá!</textoConteudo>
	 * 		<urlNotificacao>http://dominio:porta/pasta/script</urlNotificacao>	[opcional]
	 *      <dataAgendamento>20071010202020</dataAgendamento>          			[opcional]
	 *      <indAgendamentoRel>true</indAgendamentoRel>							[opcional]
	 *      <numTentativasEntrega>3</numTentativasEntrega>						[opcional]
	 *      <intervaloTentativa>10</intervaloTentativa>							[opcional]
	 *      <appSpecific>Texto qualquer</appSpecific>							[opcional]
	 *      <appRequestId>Numero sequencial</appRequestId>						[opcional]
	 * </GPPRequisicaoTangram> 
	 * 
	 * @param xml GPPRequisicaoTangram
	 * @return Requisicao
	 */
	public static Requisicao parse(String xml) throws Exception
	{
		Requisicao requisicao = new Requisicao();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// Cria um Parser XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		Document doc = parser.parse(is);
		Element root = getNode(doc, "GPPRequisicaoTangram");
		
		// ID SERVICO
		
		String idServico = getNodeValue(root, "idServico");
		if (idServico == null)
			throw new IllegalArgumentException("Campo 'idServico' nao pode ser nulo");
		
		requisicao.setIdServico(Integer.parseInt(idServico));
		
		// ID CANAL
		
		String idCanal = getNodeValue(root, "idCanal");
		if (idCanal == null)
			throw new IllegalArgumentException("Campo 'idCanal' nao pode ser nulo");
		
		requisicao.setIdCanal(new Integer(idCanal));
		
		// IDT ORIGEM
		
		String idtOrigem = getNodeValue(root, "idtOrigem");
		if (idtOrigem == null)
			throw new IllegalArgumentException("Campo 'idtOrigem' nao pode ser nulo");
		
		requisicao.setIdtOrigem(idtOrigem);
		
		// IDT MSISDN DESTINO
		
		String idtMsisdnDestino = getNodeValue(root, "idtMsisdnDestino");
		if (idtMsisdnDestino == null)
			throw new IllegalArgumentException("Campo 'idtMsisdnDestino' nao pode ser nulo");
		
		String[] destinos = idtMsisdnDestino.split(";");
		requisicao.setDestinosMensagem(new ArrayList());
		for (int i = 0; i < destinos.length; i++)
			requisicao.adicionarDestino(destinos[i]);

		// TEXTO CONTEUDO  (define como texto simples, sem truncamento, udh nulo)
		
		String textoConteudo = getNodeValue(root, "textoConteudo");
		if (textoConteudo == null)
			throw new IllegalArgumentException("Campo 'textoConteudo' nao pode ser nulo");
		
		requisicao.adicionarConteudo(textoConteudo);
		 
		// URL NOTIFICACAO (define como HTTP_POST)
	
		String urlNotificacao = getNodeValue(root, "urlNotificacao");
		if (urlNotificacao != null)
			requisicao.setNotificacaoHttp(urlNotificacao);
		
		// DATA AGENDAMENTO
		
		String dataAgendamento = getNodeValue(root, "dataAgendamento");
		if (dataAgendamento != null)
			requisicao.setDataAgendamento(sdf.parse(dataAgendamento));
	
		// AGENDAMENTO RELATIVO
		
		String indAgendamentoRel = getNodeValue(root, "indAgendamentoRel");
		if (indAgendamentoRel != null)
			requisicao.setIndAgendamentoRelativo(
					indAgendamentoRel.toLowerCase().equals("true") ? Boolean.TRUE : Boolean.FALSE);
	
		// NUM TENTATIVAS ENTREGA
		
		String numTentativasEntrega = getNodeValue(root, "numTentativasEntrega");
		if (numTentativasEntrega != null)
			requisicao.setNumTentativasEntrega(new Integer(numTentativasEntrega));
		
		// INTERVALO TENTATIVA
		
		String intervaloTentativa = getNodeValue(root, "intervaloTentativa");
		if (intervaloTentativa != null)
			requisicao.setIntervaloTentativa(new Integer(intervaloTentativa));

		// APP SPECIFIC
		
		requisicao.setAppSpecific(getNodeValue(root, "appSpecific"));
		
		// APP REQUEST ID
		
		requisicao.setAppRequestId(getNodeValue(root, "appRequestId"));

		return requisicao;
	}
	
	/**
	 * Retorna o xml do objeto GPPRequisicaoTangram.
	 * 
	 * @param GPPRequisicaoTangram
	 * @return XML
	 */
	public static String gerarXML(GPPRequisicaoTangram gppRequisicaoTangram) throws Exception
	{
		if (gppRequisicaoTangram == null)
			return null;
		
		//TODO: Criar validacoes dos parametros de entrada 
		if (gppRequisicaoTangram.getIdtMsisdnDestino() == null || 
				gppRequisicaoTangram.getIdtMsisdnDestino().equals(""))
		{
			throw new IllegalArgumentException("Campo 'idtMsisdnDestino' nao pode ser nulo.");
		}
		
		// Cria um Builder XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docBuilder.newDocumentBuilder();
		Document doc = builder.newDocument();
	
		// Gera as entidades XML
		
		Element root = doc.createElement("GPPRequisicaoTangram");
		
		createNode(doc, root, "idServico",				gppRequisicaoTangram.getIdServico());
		createNode(doc, root, "idCanal",				gppRequisicaoTangram.getIdCanal());
		createNode(doc, root, "idtOrigem", 				gppRequisicaoTangram.getIdtOrigem());
		createNode(doc, root, "idtMsisdnDestino", 		gppRequisicaoTangram.getIdtMsisdnDestino());
		createNode(doc, root, "textoConteudo", 			gppRequisicaoTangram.getTextoConteudo());
		createNode(doc, root, "urlNotificacao", 		gppRequisicaoTangram.getUrlNotificacao());
		createNode(doc, root, "dataAgendamento", 		gppRequisicaoTangram.getDataAgendamento());
		createNode(doc, root, "indAgendamentoRel", 		gppRequisicaoTangram.getIndAgendamentoRel());
		createNode(doc, root, "numTentativasEntrega", 	gppRequisicaoTangram.getNumTentativasEntrega());
		createNode(doc, root, "intervaloTentativa", 	gppRequisicaoTangram.getIntervaloTentativa());
		createNode(doc, root, "appSpecific", 			gppRequisicaoTangram.getAppSpecific());
		createNode(doc, root, "appRequestId", 			gppRequisicaoTangram.getAppRequestId());
		
		// Converte para String
	
		StringWriter stringWriter = new StringWriter();
		DOMSource domSource = new DOMSource(root);
		StreamResult streamResult = new StreamResult(stringWriter);
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		//serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.VERSION,"1.0");
		serializer.transform(domSource, streamResult); 
		
		return stringWriter.toString();

	}
	
	/**
	 * Retorna o conteúdo texto de um elemento xml.
	 * 
	 * @param parent	Elemento pai. Instancia de <code>Element</code>
	 * @param child		Elemento filho (nome da tag)
	 * @return Conteúdo texto da tag especificada em 'child'
	 */
	private static String getNodeValue(Element parent, String child)
	{
		Element tag = (Element)parent.getElementsByTagName(child).item(0);
		String valor = null;
		
		if (tag.hasChildNodes())
			valor = tag.getFirstChild().getNodeValue();
		
		if (valor == null || valor.equals(""))
			valor = null;
		
		return valor;
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
	 * Cria um elemento XML contendo um valor
	 * 
	 * @param doc		Factory de novas tags. Instancia de <code>Document</code>
	 * @param parent	Elemento pai (que conterá o novo elemento).
	 * @param name		TagName do novo elemento
	 * @param value 	Conteudo
	 * @return	Elemento
	 */
	private static Element createNode(Document doc, Element parent, String name, String value)
	{
		Element el = doc.createElement(name);
		parent.appendChild(el);
		el.appendChild(doc.createTextNode(value == null ? "" : value));
		return el;
	}
}
