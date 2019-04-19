package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.parser;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Cabecalho;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Detalhe;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.ExtratoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pela conversao em XML do extrato Pula-Pula.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/03/2008
 *	@modify		Primeira versao.
 */
public abstract class ExtratoPulaPulaXMLParser 
{

	/**
	 *	Retorna o extrato Pula-Pula em formato XML.
	 *	
	 *	@param		extrato					Extrato Pula-Pula.
	 *	@return		Extrato Pula-Pula em formato XML.
	 */
	public static String format(ExtratoPulaPula extrato)
	{
		try
		{
			StringWriter writer = new StringWriter();
			ExtratoPulaPulaXMLParser.write(extrato, writer);

			return writer.toString();
		}
		catch(Exception e)
		{
			return e.toString();
		}
	}
	
	/**
	 *	Escreve o extrato Pula-Pula em formato XML no output.
	 *	
	 *	@param		extrato					Extrato Pula-Pula.
	 *	@param		writer					Output do extrato Pula-Pula.
	 *	@throws		ParserConfigurationException, TransformerException, TransformerFactoryConfigurationError 
	 */
	public static void write(ExtratoPulaPula extrato, Writer writer) throws ParserConfigurationException, 
																			TransformerFactoryConfigurationError, 
																			TransformerException
	{
		Document	document	= ExtratoPulaPulaXMLParser.newDocument();
		Element		elmRoot		= ExtratoPulaPulaXMLParser.toElement(extrato, document);

		ExtratoPulaPulaXMLParser.write(elmRoot, writer);
	}
	
	/**
	 *	Retorna o extrato Pula-Pula em formato XML, com informacoes do erro lancado durante a construcao do
	 *	extrato.
	 *	
	 *	@param		exception				Excecao lancada durante a construcao do extrato.
	 *	@return		Extrato Pula-Pula em formato XML com as informacoes da excecao lancada.
	 */
	public static String format(Exception exception)
	{
		try
		{
			StringWriter writer = new StringWriter();
			ExtratoPulaPulaXMLParser.write(exception, writer);

			return writer.toString();
		}
		catch(Exception e)
		{
			return e.toString();
		}
	}
	
	/**
	 *	Escreve o extrato Pula-Pula em formato XML com as informacoes da excecao no output.
	 *	
	 *	@param		exception				Excecao lancada durante a construcao do Extrato Pula-Pula.
	 *	@param		writer					Output do extrato Pula-Pula.
	 *	@throws		ParserConfigurationException, TransformerException, TransformerFactoryConfigurationError 
	 */
	public static void write(Exception exception, Writer writer) throws ParserConfigurationException, 
																		TransformerFactoryConfigurationError, 
																		TransformerException
	{
		Document	document	= ExtratoPulaPulaXMLParser.newDocument();
		Element		elmRoot		= ExtratoPulaPulaXMLParser.toElement(exception, document);

		ExtratoPulaPulaXMLParser.write(elmRoot, writer);
	}
	
	/**
	 *	Retorna o extrato Pula-Pula em formato XML, com informacoes do codigo de erro gerado durante a construcao 
	 *	do extrato. 
	 *	
	 *	@param		retorno					Codigo de retorno da operacao.
	 *	@return		Extrato Pula-Pula em formato XML com as informacoes do codigo de erro.
	 */
	public static String format(short retorno)
	{
		try
		{
			StringWriter writer = new StringWriter();
			ExtratoPulaPulaXMLParser.write(retorno, writer);

			return writer.toString();
		}
		catch(Exception e)
		{
			return e.toString();
		}
	}
	
	/**
	 *	Escreve o extrato Pula-Pula em formato XML com as informacoes do codigo de erro no output.
	 *	
	 *	@param		retorno					Codigo de retorno da operacao.
	 *	@param		writer					Output do extrato Pula-Pula.
	 *	@throws		ParserConfigurationException, TransformerException, TransformerFactoryConfigurationError 
	 */
	public static void write(short retorno, Writer writer) throws ParserConfigurationException, 
																  TransformerFactoryConfigurationError, 
																  TransformerException
	{
		Document	document	= ExtratoPulaPulaXMLParser.newDocument();
		Element		elmRoot		= ExtratoPulaPulaXMLParser.toElement(retorno, document);

		ExtratoPulaPulaXMLParser.write(elmRoot, writer);
	}
	
	/**
	 *	Escreve o XML no output.
	 *	
	 *	@param		elmRoot					XML em formato DOM.
	 *	@param		write					Output do extrato Pula-Pula.
	 *	@throws		TransformerFactoryConfigurationError, TransformerException 
	 */
	private static void write(Element elmRoot, Writer writer) throws TransformerFactoryConfigurationError, 
																	 TransformerException
	{
		DOMSource		source		= new DOMSource(elmRoot);
		StreamResult	stream		= new StreamResult(writer);
		Transformer		transformer	= TransformerFactory.newInstance().newTransformer();

		transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
		transformer.transform(source, stream);
	}
	
	/**
	 *	Cria e retorna um novo documento DOM para criacao de XML.
	 *	
	 *	@return		Documento DOM para criacao de XML.
	 *	@throws		ParserConfigurationException
	 */
	private static Document newDocument() throws ParserConfigurationException
	{
		DocumentBuilderFactory	factory	= DocumentBuilderFactory.newInstance();
		DocumentBuilder			builder	= factory.newDocumentBuilder();

		return builder.newDocument();
	}
	
	/**
	 *	Retorna o XML resultante do extrato Pula-Pula do assinante.
	 *	
	 *	@param		extrato					Extrato Pula-Pula.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		XML resultante do extrato Pula-Pula do assinante.
	 */
	public static Element toElement(ExtratoPulaPula extrato, Document document)
	{
		//Criando o elemento root.
		Element result = document.createElement("GPPExtratoPulaPula");
		
		//Criando o elemento referente ao cabecalho.
		result.appendChild(ExtratoPulaPulaXMLParser.criarCabecalho(extrato.getCabecalho(), document));
		//Criando o elemento referente aos detalhes do extrato.
		result.appendChild(ExtratoPulaPulaXMLParser.criarDetalhes(extrato.getDetalhes(), document));
		//Criando o elemento referente a sumarizacao do extrato.
		result.appendChild(ExtratoPulaPulaXMLParser.criarSumarizacao(extrato.getSumarizacao(), document));
		//Criando o elemento referente ao saldo Pula-Pula do extrato.
		result.appendChild(ExtratoPulaPulaXMLParser.criarSaldoPulaPula(extrato.getSaldo(), document));
		
		return result;
	}
	
	/**
	 *	Retorna o XML resultante da excecao lancada durante a construcao do extrato Pula-Pula do assinante.
	 *	
	 *	@param		exception				Excecao lancada durante a construcao do Extrato Pula-Pula.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		XML resultante do extrato Pula-Pula do assinante.
	 */
	public static Element toElement(Exception exception, Document document)
	{
		ExtratoPulaPula extrato 	= new ExtratoPulaPula();
		Cabecalho		cabecalho	= new Cabecalho();
		
		cabecalho.setRetorno((short)Definicoes.RET_ERRO_TECNICO);
		cabecalho.setMensagem(exception.toString());
		extrato.setCabecalho(cabecalho);
		
		return ExtratoPulaPulaXMLParser.toElement(extrato, document);
	}
	
	/**
	 *	Retorna o XML resultante do codigo de erro retornado durante a construcao do extrato Pula-Pula do assinante.
	 *	
	 *	@param		retorno					Codigo de retorno da operacao.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		XML resultante do extrato Pula-Pula do assinante.
	 */
	public static Element toElement(short retorno, Document document)
	{
		ExtratoPulaPula extrato 	= new ExtratoPulaPula();
		Cabecalho		cabecalho	= new Cabecalho();
		
		cabecalho.setRetorno(retorno);
		extrato.setCabecalho(cabecalho);
		
		return ExtratoPulaPulaXMLParser.toElement(extrato, document);
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes do cabecalho do extrato Pula-Pula.
	 *	
	 *	@param		cabecalho				Cabecalho do extrato.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes do cabecalho do extrato Pula-Pula.
	 */
	private static Element criarCabecalho(Cabecalho cabecalho, Document document)
	{
		Element				result			= document.createElement("cabecalho");
		SimpleDateFormat	conversorDate	= new SimpleDateFormat(Definicoes.MASCARA_DATE);
		
		if(cabecalho != null)
		{
			//Criando o elemento referente ao codigo de retorno.
			Element elmRetorno = document.createElement("retorno");
			result.appendChild(elmRetorno);
			elmRetorno.appendChild(document.createTextNode(String.valueOf(cabecalho.getRetorno())));
			//Criando o elemento referente ao MSISDN.
			Element elmMsisdn = document.createElement("msisdn");
			result.appendChild(elmMsisdn);
			String msisdn = (cabecalho.getMsisdn() != null) ? cabecalho.getMsisdn() : "";
			elmMsisdn.appendChild(document.createTextNode(msisdn));
			//Criando o elemento referente ao identificador da promocao.
			Element elmPromocao = document.createElement("promocao");
			result.appendChild(elmPromocao);
			String promocao = (cabecalho.getPromocao() != -1) ? String.valueOf(cabecalho.getPromocao()) : "";
			elmPromocao.appendChild(document.createTextNode(promocao));
			//Criando o elemento referente a data inicial de consulta.
			Element elmDataIni = document.createElement("dataIni");
			result.appendChild(elmDataIni);
			String dataIni = (cabecalho.getDataIni() != null) ? 
								conversorDate.format(cabecalho.getDataIni()) : "";
			elmDataIni.appendChild(document.createTextNode(dataIni));
			//Criando o elemento referente a data final de consulta.
			Element elmDataFim = document.createElement("dataFim");
			result.appendChild(elmDataFim);
			String dataFim = (cabecalho.getDataFim() != null) ? 
								conversorDate.format(cabecalho.getDataFim()) : "";
			elmDataFim.appendChild(document.createTextNode(dataFim));
			//Criando o elemento referente a mensagem informativa.
			Element elmMensagem = document.createElement("mensagem");
			result.appendChild(elmMensagem);
			elmMensagem.appendChild(document.createTextNode(cabecalho.getMensagem()));
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes dos detalhes do extrato Pula-Pula.
	 *	
	 *	@param		detalhes				Detalhes do extrato.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes dos detalhes do extrato Pula-Pula.
	 */
	private static Element criarDetalhes(Collection detalhes, Document document)
	{
		Element				result				= document.createElement("detalhes");
		SimpleDateFormat	conversorTimestamp	= new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
		
		for(Iterator iterator = detalhes.iterator(); iterator.hasNext();)
		{
			Detalhe	detalhe		= (Detalhe)iterator.next();
			Element	elmDetalhe	= document.createElement("detalhe");
			
			//Criando o atributo referente ao identificador de desconto.
			String idDesconto = (detalhe.getDesconto() != null) ?
									String.valueOf(detalhe.getDesconto().getIdDesconto()) : "";
			elmDetalhe.setAttribute("idDesconto", idDesconto);
			//Criando o atributo referente ao indicador de evento.
			elmDetalhe.setAttribute("indEvento", String.valueOf(detalhe.isEvento()));
			//Criando o elemento referente ao numero originador.
			String originador = detalhe.getOriginador();
			Element elmOriginador = document.createElement("originador");
			elmDetalhe.appendChild(elmOriginador);
			elmOriginador.appendChild(document.createTextNode((originador != null) ? originador : ""));
			//Criando o elemento referente a data do evento.
			Element elmTimestamp = document.createElement("timestamp");
			elmDetalhe.appendChild(elmTimestamp);
			String timestamp = (detalhe.getTimestamp() != null) ? 
								conversorTimestamp.format(detalhe.getTimestamp()) : "";
			elmTimestamp.appendChild(document.createTextNode(timestamp));
			//Criando o elemento referente a descricao do detalhe.
			String descricao = detalhe.getDescricao();
			Element elmDescricao = document.createElement("descricao");
			elmDetalhe.appendChild(elmDescricao);
			elmDescricao.appendChild(document.createTextNode((descricao != null) ? descricao : ""));
			//Criando o elemento referente a duracao.
			Element elmDuracao = document.createElement("duracao");
			elmDetalhe.appendChild(elmDuracao);
			elmDuracao.appendChild(document.createTextNode(String.valueOf(detalhe.getDuracao())));
			//Criando o elemento referente ao bonus concedido pela ligacao.
			Element elmBonus = document.createElement("bonus");
			elmDetalhe.appendChild(elmBonus);
			elmBonus.appendChild(document.createTextNode(String.valueOf(detalhe.getBonus())));
			
			result.appendChild(elmDetalhe);
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes da sumarizacao do extrato Pula-Pula.
	 *	
	 *	@param		sumarizacao				Sumarizacao do extrato.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes da sumarizacao do extrato Pula-Pula.
	 */
	private static Element criarSumarizacao(Map sumarizacao, Document document)
	{
		Element result = document.createElement("sumarizacao");
		
		for(Iterator iterator = sumarizacao.values().iterator(); iterator.hasNext();)
		{
			Detalhe	detalhe		= (Detalhe)iterator.next();
			Element	elmDetalhe	= document.createElement("detalhe");
			
			//Criando o atributo referente ao identificador de desconto.
			elmDetalhe.setAttribute("idDesconto", String.valueOf(detalhe.getDesconto().getIdDesconto()));
			//Criando o elemento referente a duracao.
			Element elmDuracao = document.createElement("duracao");
			elmDetalhe.appendChild(elmDuracao);
			elmDuracao.appendChild(document.createTextNode(String.valueOf(detalhe.getDuracao())));
			//Criando o elemento referente ao bonus concedido pela ligacao.
			Element elmBonus = document.createElement("bonus");
			elmDetalhe.appendChild(elmBonus);
			elmBonus.appendChild(document.createTextNode(String.valueOf(detalhe.getBonus())));
			
			result.appendChild(elmDetalhe);
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes do bonus Pula-Pula a ser concedido ao assinante.
	 *	
	 *	@param		saldo					Informacoes do saldo Pula-Pula a ser concedido ao assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes do bonus Pula-Pula a ser concedido ao assinante.
	 */
	private static Element criarSaldoPulaPula(SaldoPulaPula saldo, Document document)
	{
		Element result = document.createElement("saldo");
		
		if(saldo != null)
		{
			//Criando o elemento referente ao valor total.
			Element elmValorTotal = document.createElement("valorTotal");
			result.appendChild(elmValorTotal);
			String valorTotal = saldo.toString(SaldoPulaPula.VALOR_TOTAL);
			elmValorTotal.appendChild(document.createTextNode(valorTotal));
			//Criando o elemento referente ao valor parcial.
			Element elmValorParcial = document.createElement("valorParcial");
			result.appendChild(elmValorParcial);
			String valorParcial = saldo.toString(SaldoPulaPula.VALOR_PARCIAL);
			elmValorParcial.appendChild(document.createTextNode(valorParcial));
			//Criando o elemento referente ao valor a receber.
			Element elmValorAReceber = document.createElement("valorAReceber");
			result.appendChild(elmValorAReceber);
			String valorAReceber = saldo.toString(SaldoPulaPula.VALOR_A_RECEBER);
			elmValorAReceber.appendChild(document.createTextNode(valorAReceber));
			//Criando o elemento referente as bonificacoes.
			result.appendChild(ExtratoPulaPulaXMLParser.criarBonificacoes(saldo.getBonificacoes(), document));
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes das bonificacoes Pula-Pula a serem concedidas ao assinante.
	 *	
	 *	@param		bonificacoes			Lista de bonificacoes Pula-Pula para o assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes das bonificacoes Pula-Pula a serem concedidas ao assinante.
	 */
	private static Element criarBonificacoes(Collection bonificacoes, Document document)
	{
		Element result = document.createElement("bonificacoes");
		
		for(Iterator iterator = bonificacoes.iterator(); iterator.hasNext();)
		{
			BonificacaoPulaPula	bonificacao		= (BonificacaoPulaPula)iterator.next();
			Element				elmBonificacao	= document.createElement("bonificacao");
			
			//Insere o atributo referente ao tipo de bonificacao.
			String idTipoBonificacao = String.valueOf(bonificacao.getTipoBonificacao().getIdTipoBonificacao());
			elmBonificacao.setAttribute("tipoBonificacao", idTipoBonificacao);
			//Criando o elemento referente ao valor total.
			Element elmValorTotal = document.createElement("valorTotal");
			elmBonificacao.appendChild(elmValorTotal);
			String valorTotal = bonificacao.toString(BonificacaoPulaPula.VALOR_TOTAL);
			elmValorTotal.appendChild(document.createTextNode(valorTotal));
			//Criando o elemento referente ao valor parcial.
			Element elmValorParcial = document.createElement("valorParcial");
			elmBonificacao.appendChild(elmValorParcial);
			String valorParcial = bonificacao.toString(BonificacaoPulaPula.VALOR_PARCIAL);
			elmValorParcial.appendChild(document.createTextNode(valorParcial));
			//Criando o elemento referente ao valor a receber.
			Element elmValorAReceber = document.createElement("valorAReceber");
			elmBonificacao.appendChild(elmValorAReceber);
			String valorAReceber = bonificacao.toString(BonificacaoPulaPula.VALOR_A_RECEBER);
			elmValorAReceber.appendChild(document.createTextNode(valorAReceber));
			//Criando o elemento referente ao limite.
			Element elmLimite = document.createElement("limite");
			elmBonificacao.appendChild(elmLimite);
			String limite = bonificacao.toString(BonificacaoPulaPula.LIMITE);
			elmLimite.appendChild(document.createTextNode(limite));
			//Criando o elemento referente ao flag de limite da bonificacao ultrapassado.
			Element elmLimiteUltrapassado = document.createElement("limiteUltrapassado");
			elmBonificacao.appendChild(elmLimiteUltrapassado);
			boolean isLimiteUltrapassado = bonificacao.isLimiteUltrapassado();
			elmLimiteUltrapassado.appendChild(document.createTextNode(String.valueOf(isLimiteUltrapassado)));
			
			result.appendChild(elmBonificacao);
		}
			
		return result;
	}
	
}