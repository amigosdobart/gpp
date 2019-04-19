package com.brt.gpp.aplicacoes.gerenciamentoVouchers.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.DetalheVoucherXML;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.SolicitacaoAtivacao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe é responsável pelo parsing e por montar XMLs relativas ao 
 * processo de gerenciamento de vouchers.
 * 
 * @author Gustavo Gusmao
 * @since 24/03/2006
 *
 */
public class ManipuladorXMLVoucher
{
	private static GerentePoolLog gerenteLog = gerenteLog = GerentePoolLog.getInstancia(ManipuladorXMLVoucher.class);
	
	/**
	 * Metodo....:getDadosSolicitacao
	 * Descricao.:Retorna os dados da solicitacao de ativacao de voucher
	 * @param idProcessamento	- Id da requisicao para a tabela de interface
	 * @param xmlRequisicao		- Dados do XML da requisicao
	 * @return SolicitacaoAtivacao - Dados da solicitacao
	 */
	public static SolicitacaoAtivacao getDadosSolicitacao(long idProcesso, long idProcessamento, Clob xmlRequisicao)
	{
		SolicitacaoAtivacao solicitacao = null;
		try
		{
			// Realiza a leitura do objeto CLOB para posteriormente executar
			// o parse do XML de requisicao afim de identificar as propriedades
		    Reader chr_instream;
		    char chr_buffer[] = new char[(int)xmlRequisicao.length()];
			chr_instream = xmlRequisicao.getCharacterStream();
			chr_instream.read(chr_buffer);
			
			solicitacao = parseXMLRequisicao(idProcesso, new String(chr_buffer));
			solicitacao.setIdRequisicao(idProcessamento);
			solicitacao.setEventoNegocio(Definicoes.IDT_EVT_NEGOCIO_VOUCHER);
		}
		catch(Exception se)
		{
			gerenteLog.log(idProcesso, Definicoes.WARN, "ManipuladorXML", "getDadosSolicitacao","Erro ao realizar o parse das informacoes da requisicao. Erro:"+se);
		}
		return solicitacao;
	}
	
	/**
	 * Metodo....:parseXMLRequisicao
	 * Descricao.:Realiza o parse do xml de entrada da requisicao de ativacao
	 *            de voucher fisico, retornando um objeto contendo as propriedades
	 *            lidas no arquivo
	 * @param xmlEntrada	- XML de entrada da requisicao
	 * @return SolicitacaoAtivacao	- Objeto contendo as informacoes da requisicao
	 * @throws GPPBadXMLFormatException
	 * @throws GPPInternalErrorException
	 * @throws IOException
	 */
	public static SolicitacaoAtivacao parseXMLRequisicao(long idProcesso, String xmlEntrada) throws GPPBadXMLFormatException, GPPInternalErrorException, IOException
	{
		SolicitacaoAtivacao solicitacao = null;
		try
		{
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML DEBUGrmado dentro e um InputSource
			InputSource is = new InputSource(new StringReader(xmlEntrada));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG GPPSolicitacaoVoucher
			Element serviceElement = (Element) doc.getElementsByTagName( "root" ).item(0);
			NodeList itemNodes = serviceElement.getChildNodes();

			// Cria a instancia do objeto que ira conter os valores do XML
			// Popula as informacoes a partir do parse dos elementos do arquivo
			solicitacao = new SolicitacaoAtivacao();
			solicitacao.setInterface(((Element)itemNodes).getElementsByTagName("interface").item(0).getChildNodes().item(0).getNodeValue());
			solicitacao.setRemessa(((Element)itemNodes).getElementsByTagName("remessa").item(0).getChildNodes().item(0).getNodeValue());
			solicitacao.setAno_Operacao(((Element)itemNodes).getElementsByTagName("ano_operacao").item(0).getChildNodes().item(0).getNodeValue());
			solicitacao.setCod_Atividade(((Element)itemNodes).getElementsByTagName("cod_atividade").item(0).getChildNodes().item(0).getNodeValue());
			solicitacao.setCod_Ativacao((new Short(((Element)itemNodes).getElementsByTagName("cod_ativacao").item(0).getChildNodes().item(0).getNodeValue())).shortValue());
			solicitacao.setCod_Motivo(((Element)itemNodes).getElementsByTagName("cod_motivo").item(0).getChildNodes().item(0)==null?"":itemNodes.item(5).getChildNodes().item(0).getNodeValue());
			solicitacao.setNumero_Caixa(((Element)itemNodes).getElementsByTagName("numero_caixa").item(0).getChildNodes().item(0).getNodeValue());
			solicitacao.setNum_Inicial_Lote(((Element)itemNodes).getElementsByTagName("num_inicial_lote").item(0).getChildNodes().item(0).getNodeValue());
			solicitacao.setNum_Final_Lote(((Element)itemNodes).getElementsByTagName("num_final_lote").item(0).getChildNodes().item(0).getNodeValue());
		}
		catch (SAXException e)
		{
			gerenteLog.log(idProcesso, Definicoes.WARN, "ManipuladorXML", "parseXMLRequisicao", "Erro(SAX) formato XML: " + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro formato XML:" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			gerenteLog.log(idProcesso, Definicoes.WARN, "ManipuladorXML", "parseXMLRequisicao", "Erro(NULLPOINTER) formato XML: " + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro formato XML:" + e.getMessage());
		}
		catch (ParserConfigurationException e2)
		{
			gerenteLog.log(idProcesso, Definicoes.WARN, "ManipuladorXML", "parseXMLRequisicao", "Erro(PARSER) formato XML: " + e2.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e2.getMessage());
		}
		return solicitacao;
	}
	
	/**
	 * Metodo....:getXMLRetorno
	 * Descricao.:Retorna o XML que sera gravado na tabela de interface para saida
	 * @param solicitacao	- Dados da solicitacao de ativacao
	 * @param descMotivo	- Descricao do motivo
	 * @return	String		- XML formatado para retorno
	 */
	public static String getXMLRetorno(SolicitacaoAtivacao solicitacao, Collection detalhes)
	{
		GerarXML geradorXML = new GerarXML("root");
		
		// Geração da Sessão de Cabecalho do XML
		geradorXML.abreNo("cabecalho");
		geradorXML.adicionaTag("interface", 		solicitacao.getInterface()						);
		geradorXML.adicionaTag("remessa", 			solicitacao.getRemessa()						);
		geradorXML.adicionaTag("ano_operacao", 		solicitacao.getAno_Operacao()					);
		geradorXML.adicionaTag("cod_atividade", 	solicitacao.getCod_Atividade()					);
		geradorXML.adicionaTag("cod_ativacao", 		Integer.toString(solicitacao.getCod_Ativacao())	);
		geradorXML.adicionaTag("cod_motivo", 		solicitacao.getCod_Motivo()						);
		geradorXML.adicionaTag("numero_caixa", 		solicitacao.getNumero_Caixa()					);
		geradorXML.adicionaTag("num_inicial_lote", 	solicitacao.getNum_Inicial_Lote()				);
		geradorXML.adicionaTag("num_final_lote",	solicitacao.getNum_Final_Lote()					);
		geradorXML.fechaNo();

		// Realiza a iteracao em todos os elementos de detalhe para gerar no xml as informacoes
		// correspondentes aos codigos de ativacao/cancelamento de todos as faixas de voucher
		for (Iterator i = detalhes.iterator(); i.hasNext();)
		{
			DetalheVoucherXML detVoucher = (DetalheVoucherXML)i.next();
			geradorXML.abreNo("detalhe");
			geradorXML.adicionaTag("cod_ativacao",		String.valueOf(detVoucher.getCod_Ativacao()));
			geradorXML.adicionaTag("numero_caixa",		detVoucher.getNumero_Caixa()				);
			geradorXML.adicionaTag("num_inicial_lote",	detVoucher.getNum_Inicial_Lote()			);
			geradorXML.adicionaTag("num_final_lote",	detVoucher.getNum_Final_Lote()				);
			geradorXML.adicionaTag("msg_retorno",		detVoucher.getMsgRetorno()					);
			geradorXML.fechaNo();
		}
		return new String(geradorXML.getXML());
	}
}