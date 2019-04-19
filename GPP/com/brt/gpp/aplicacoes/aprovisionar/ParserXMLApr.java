package com.brt.gpp.aplicacoes.aprovisionar;

// Imports de GPP
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.Definicoes;

// Imports de java
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.Collection;
import java.util.Vector;

public class ParserXMLApr 
{
	public ParserXMLApr()
	{
		// Construtor
	}
	 
	/**
	 * Metodo... parseXMLBloqueio
	 * Descricao: Faz o parse do xml e devolve uma Collection de objetos ElementoXMLApr
	 * @param 	String	xmlBloqueio		String XML retornada pelo ASAP
	 * @return	Collection				Collection de objetos ElementoXMLApr
	 * @throws GPPInternalErrorException
	 * @throws GPPBadXMLFormatException
	 */
	public Collection parseXMLBloqueio(String xmlBloqueio) throws GPPInternalErrorException, GPPBadXMLFormatException
	{
//		int n = 0;		// índice do array de retorno
		int qtdServicos = 0;
		String servico = null;
		String acao = null;
		String codRetorno = null;
		String msisdn = null;
		
		try
		{
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML DEBUGrmado dentro e um InputSource
			InputSource is = new InputSource(new StringReader(xmlBloqueio));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG GPPBloqueioServico
			Element serviceElement = (Element) doc.getElementsByTagName( "root" ).item(0);
			NodeList itemNodes = serviceElement.getChildNodes();			

			// Pega o msisdn no xml
			msisdn = ((Element)itemNodes).getElementsByTagName("msisdn_novo").item(0).getChildNodes().item(0).getNodeValue();
			
			// Verifica qual a quantidade de serviços
			NodeList serviceNodes = ((Element)itemNodes).getElementsByTagName("provision").item(0).getChildNodes();
			qtdServicos = serviceNodes.getLength();
			
			// Cria vetor de retorno com um item a menos que a quantidade de serviços no XML
			// pois o serviço de INFO_SIMCARD será desprezado
			Vector retorno = new Vector();

			// Índice usado para apontar a posição de inserção no array de retorno
//			int j = 0;
			// Pega as informações retornadas para cada um dos serviços (excluindo o serviço ELM_INFO_SIMCARD)
			for(int k = 0; k < qtdServicos; k++)
			{
				// Pega o nome do serviço
				String servicoXML = ((Element)serviceNodes.item(k).getChildNodes()).getElementsByTagName("macro_servico").item(0).getChildNodes().item(0).getNodeValue();
				
				// Identifica o processo de retorno
				((Element)serviceNodes.item(k).getChildNodes()).getElementsByTagName("operacao").item(0).getChildNodes().item(0).getNodeValue();
				
				// Verificar se a informação do camo de simcard deverá ser retornada
				// Ela deve ser retornada apenas no caso do processo de contingência ao CRM
				if(servicoXML.equals(Definicoes.SERVICO_INFO_SIMCARD) || servicoXML.equals(Definicoes.SERVICO_BLOQ_HOTLINE))
				{
					servico = servicoXML;
				}
				else
				{
					// Já no caso de um serviço não ser INFO_SIMCARD nem retirada de hotline
					if(!servicoXML.equals(Definicoes.SERVICO_INFO_SIMCARD))
					{
						// Elimina o _GPP_ do meio do nome do serviço
						servico = servicoXML.substring(0,4)+servicoXML.substring(8);
					}
				}
				acao = ((Element)serviceNodes.item(k).getChildNodes()).getElementsByTagName("operacao").item(0).getChildNodes().item(0).getNodeValue();

				// Pega o retorno do ASAP
				codRetorno = ((Element)serviceNodes.item(k).getChildNodes()).getElementsByTagName("status").item(0).getChildNodes().item(0).getNodeValue();
			
				//	Armazena os dados do XML em um objeto da classe SolicitacaoBloqueio
				retorno.add(new ElementoXMLApr(servico, acao, codRetorno, msisdn));
			}
			return retorno;
		}
		catch (SAXException e) 
		{
			throw new GPPInternalErrorException ("Erro de formato de XML:" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			throw new GPPBadXMLFormatException ("Erro de formato de XML:" + e.getMessage());
		}			
		catch (IOException e1) 
		{
			throw new GPPInternalErrorException ("Erro interno no GPP:" + e1.getMessage());
		}
		catch (ParserConfigurationException e2)
		{
			throw new GPPInternalErrorException ("Erro interno no GPP:" + e2.getMessage());
		}
	}
	
	/**
	 * Metodo....:parseCabecalhoXMLBloqueio
	 * Descricao.:Este metodo realiza o parse do xml de retorno do bloqueio de servicos
	 *            para retornar o cabecalho deste
	 * @param xmlBloqueio		- XML a ser feito a analise de parse
	 * @return CabecalhoXMLApr	- Objeto cabecalho do XML contendo os valores encontrados
	 * @throws GPPInternalErrorException
	 * @throws GPPBadXMLFormatException
	 */
	public CabecalhoXMLApr parseCabecalhoXMLBloqueio(String xmlBloqueio) throws GPPInternalErrorException, GPPBadXMLFormatException
	{
		try
		{
			// Inicia uma instancia do cabecalho.
			CabecalhoXMLApr cabecalhoXML = new CabecalhoXMLApr();
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			// Carrega o XML DEBUGrmado dentro e um InputSource
			InputSource is = new InputSource(new StringReader(xmlBloqueio));
			// Faz o parse do XML
			Document doc = parse.parse(is);
			// Procura a TAG GPPBloqueioServico		
			Element serviceElement = (Element) doc.getElementsByTagName( "root" ).item(0);
			NodeList itemNodes = serviceElement.getChildNodes();
			// Prepara as propriedades do objeto com os valores da tag
			// do XML
			cabecalhoXML.setCaseSubType 		( ((Element)itemNodes).getElementsByTagName("case_sub_type").item(0).getChildNodes().item(0).getNodeValue() );
			cabecalhoXML.setCaseType   			( ((Element)itemNodes).getElementsByTagName("case_type").item(0).getChildNodes().item(0).getNodeValue() );
			cabecalhoXML.setCategoria  			( ((Element)itemNodes).getElementsByTagName("categoria").item(0).getChildNodes().item(0).getNodeValue() );
			cabecalhoXML.setIdOs        		( ((Element)itemNodes).getElementsByTagName("id_os").item(0).getChildNodes().item(0).getNodeValue() );
			cabecalhoXML.setMsisdn       		( ((Element)itemNodes).getElementsByTagName("msisdn_novo").item(0).getChildNodes().item(0).getNodeValue() );
			cabecalhoXML.setOrderPriority		( ((Element)itemNodes).getElementsByTagName("order_priority").item(0).getChildNodes().item(0).getNodeValue() );

			// Realiza a busca em todos os primeiros itens da tag ROOT
			// as tags que possuem o nome codigo_erro e status. 
			// OBS: Este codigo e devido a estes nomes de tags poderem
			// existir em outro ponto da arvore XML sendo que o metodo
			// getElementsByTagName entao nao serve para o processo
			for (int i=0; i < itemNodes.getLength(); i++)
			{
				Node item = itemNodes.item(i);
				if ( "codigo_erro".equals(item.getNodeName()) )
				{
					if (item.getChildNodes().item(0) != null)
						cabecalhoXML.setCodigoErro(item.getChildNodes().item(0).getNodeValue());
				}
				else if ( "status".equals(item.getNodeName()) )
				{
					if (item.getChildNodes().item(0) != null)
						cabecalhoXML.setStatusProcessamento(item.getChildNodes().item(0).getNodeValue());
				}
			}

			return cabecalhoXML;
		}
		catch (SAXException e) 
		{
			throw new GPPInternalErrorException ("Erro de formato de XML:" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			throw new GPPBadXMLFormatException ("Erro de formato de XML:" + e.getMessage());
		}			
		catch (IOException e1) 
		{
			throw new GPPInternalErrorException ("Erro interno no GPP:" + e1.getMessage());
		}
		catch (ParserConfigurationException e2)
		{
			throw new GPPInternalErrorException ("Erro interno no GPP:" + e2.getMessage());
		}
	}
}
 