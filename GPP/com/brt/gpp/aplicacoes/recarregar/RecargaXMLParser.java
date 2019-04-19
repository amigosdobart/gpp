package com.brt.gpp.aplicacoes.recarregar;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoRetorno;

/**
 *	Classe responsavel pela criacao e parse de XML's com informacoes referentes a recargas.
 *
 *	@version	1.0		10/04/2007
 *	@author		Daniel Ferreira
 */
public abstract class RecargaXMLParser 
{

	/**
	 *	Cria o XML com as informacoes de resultado da Consulta Pre-Recarga.
	 *
	 *	@param		retorno					Codigo de retorno da Consulta Pre-Recarga.
	 *	@return		XML com as informacoes de resultado da Consulta Pre-Recarga.
	 */
	public static String getXMLRetornoConsultaPreRecarga(int retorno)
	{
		//Obtendo o XML de retorno.
		GerarXML xml = new GerarXML("GPPRetornoConsultaPreRecarga");

		//Codigo de retorno.
		DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
		xml.adicionaTag("retorno", conversorRetorno.format(retorno));
		
		//Descricao do codigo de retorno.
		MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance();
		CodigoRetorno entidade = mapRetorno.getRetorno(retorno);
		String descRetorno = (entidade != null) ? entidade.getDescRetorno() : null;
		xml.adicionaTag("descRetorno", descRetorno);
		
		return "<?xml version=\"1.0\"?>" + xml.getXML();
	}

	/**
	 *	Retorna os parametros de recarga definidos no XML enviado pelos sistemas externos.
	 * 
	 *	@param		xmlRecarga				XML com os parametros de recarga.
	 *	@return		Informacoes de recarga.
	 *	@throws		GPPBadXMLFormatException
	 */
	public static ParametrosRecarga parseXMLRecarga(String xmlRecarga) throws GPPBadXMLFormatException
	{
		ParametrosRecarga result = new ParametrosRecarga();
		
		try
		{
			//Criando os objetos necessarios para realizar o parse do XML.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource input = new InputSource(new StringReader(xmlRecarga));
			//Realizando o parse do XML.
			Document document = builder.parse(input);
			Element elmRoot = (Element)document.getElementsByTagName("root").item(0);
			Element elmGppRecarga = (Element)elmRoot.getElementsByTagName("GPPRecarga").item(0);

			//Obtendo os parametros de recarga a partir do documento criado.			
			//Elementos obrigatorios.
			if((elmGppRecarga.getElementsByTagName("msisdn").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("msisdn").item(0).getChildNodes().getLength() > 0))
			{
				result.setMSISDN(elmGppRecarga.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("tipoTransacao").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("tipoTransacao").item(0).getChildNodes().getLength() > 0))
			{
				result.setTipoTransacao(elmGppRecarga.getElementsByTagName("tipoTransacao").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("identificacaoRecarga").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("identificacaoRecarga").item(0).getChildNodes().getLength() > 0))
			{
				result.setIdentificacaoRecarga(elmGppRecarga.getElementsByTagName("identificacaoRecarga").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("dataHora").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("dataHora").item(0).getChildNodes().getLength() > 0))
			{
				SimpleDateFormat conversorDataHoraGPP = new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
				result.setDatOrigem(conversorDataHoraGPP.parse(elmGppRecarga.getElementsByTagName("dataHora").item(0).getChildNodes().item(0).getNodeValue()));
			}
			if((elmGppRecarga.getElementsByTagName("valor").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("valor").item(0).getChildNodes().getLength() > 0))
			{
				result.setIdValor(new Double(elmGppRecarga.getElementsByTagName("valor").item(0).getChildNodes().item(0).getNodeValue()).doubleValue());
			}
			if((elmGppRecarga.getElementsByTagName("tipoCredito").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("tipoCredito").item(0).getChildNodes().getLength() > 0))
			{
				result.setTipoCredito(elmGppRecarga.getElementsByTagName("tipoCredito").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("sistemaOrigem").getLength() > 0) &&
		       (elmGppRecarga.getElementsByTagName("sistemaOrigem").item(0).getChildNodes().getLength() > 0))
			{
				result.setSistemaOrigem(elmGppRecarga.getElementsByTagName("sistemaOrigem").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("operador").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("operador").item(0).getChildNodes().getLength() > 0))
			{
				result.setOperador(elmGppRecarga.getElementsByTagName("operador").item(0).getChildNodes().item(0).getNodeValue());
			}
			//Elementos nao obrigatorios.
			if((elmGppRecarga.getElementsByTagName("nsuInstituicao").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("nsuInstituicao").item(0).getChildNodes().getLength() > 0))
			{
				result.setNsuInstituicao(elmGppRecarga.getElementsByTagName("nsuInstituicao").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("hashCc").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("hashCc").item(0).getChildNodes().getLength() > 0))
			{
				result.setHash_cc(elmGppRecarga.getElementsByTagName("hashCc").item(0).getChildNodes().item(0).getNodeValue());
			}
			if((elmGppRecarga.getElementsByTagName("cpfCnpj").getLength() > 0) &&
			   (elmGppRecarga.getElementsByTagName("cpfCnpj").item(0).getChildNodes().getLength() > 0))
			{
				result.setCpf(elmGppRecarga.getElementsByTagName("cpfCnpj").item(0).getChildNodes().item(0).getNodeValue());
			}
			
			// @since  14/04/2008
			// @modify Inclusao dos atributos abaixo por default, pois esse metodo
			//  	   realiza o parse para execucao de Recargas
			// @autor  Joao Paulo Galvagni
			result.setIndCreditoDebito(Definicoes.TIPO_AJUSTE_CREDITO);
			result.setIdOperacao(Definicoes.TIPO_RECARGA);
		}
		catch(Exception e)
		{
			throw new GPPBadXMLFormatException(e.toString());
		}
		
		return result;
	}
	
}
