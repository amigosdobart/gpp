package com.brt.gpp.aplicacoes.aprovisionar;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapStatusAssinante;
import com.brt.gpp.comum.mapeamentos.MapStatusPeriodico;
import com.brt.gpp.comum.mapeamentos.MapStatusServico;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe responsavel pela criacao e parse de XML's com informacoes referentes a assinantes.
 *
 *	@version	1.0		09/04/2007
 *	@author		Daniel Ferreira
 *
 *  Atualizado por: Bernardo V Dias 
 *  Descrição: Alterações no getXML (novas tags e uso da API javax.xml.parsers)
 *  Data: 05/10/2007
 *  
 */
public abstract class AssinanteXMLParser 
{

	/**
	 *	Cria o XML com as informacoes do assinante.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma Tecnomen.
	 *	@return		XML com as informacoes do assinante.
	 */
	public static String getXML(Assinante assinante) throws GPPInternalErrorException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		PlanoPreco planoPreco			= null;
		String	descPlano				= null;
		String	descStatusAssinante		= null;
		String	descStatusPeriodico		= null;
		String	descStatusServico		= null;
		boolean  indPossuiPrincipal		= false;
		boolean  indPossuiPeriodico		= false;
		boolean  indPossuiBonus			= false;
		boolean  indPossuiSms			= false;
		boolean  indPossuiDados			= false;
		
		if(assinante != null)
		{
			// Obtendo os mapeamentos de plano de preco, status de assinante, status periodico e status de servico
			MapPlanoPreco		mapPlano			= MapPlanoPreco.getInstancia();
			MapStatusAssinante	mapStatusAssinante	= MapStatusAssinante.getInstancia();
			MapStatusPeriodico	mapStatusPeriodico	= MapStatusPeriodico.getInstancia();
			MapStatusServico	mapStatusServico	= MapStatusServico.getInstancia();
			
			// Obtendo as descricoes 
			descPlano			= mapPlano.getMapDescPlanoPreco(String.valueOf(assinante.getPlanoPreco()));
			descStatusAssinante	= mapStatusAssinante.getMapDescStatusAssinante(String.valueOf(assinante.getStatusAssinante()));
			descStatusPeriodico	= mapStatusPeriodico.getMapDescStatusPeriodico(String.valueOf(assinante.getStatusPeriodico()));
			descStatusServico	= mapStatusServico.getMapDescStatusServico(String.valueOf(assinante.getStatusServico()));
		
			// Obtendo Plano de preco
			planoPreco 			= mapPlano.getPlanoPreco(assinante.getPlanoPreco());
			
			// Obtendo tipos de saldo aplicaveis ao assinante
			for (Iterator it = planoPreco.getCategoria().getTiposSaldo().iterator(); it.hasNext();)
			{
				TipoSaldo tipoSaldo = ((SaldoCategoria)it.next()).getTipoSaldo();
				if (tipoSaldo.getIdtTipoSaldo() == TipoSaldo.PRINCIPAL)
					indPossuiPrincipal = true;
				
				if (tipoSaldo.getIdtTipoSaldo() == TipoSaldo.PERIODICO)
					indPossuiPeriodico = true;
				
				if (tipoSaldo.getIdtTipoSaldo() == TipoSaldo.BONUS)
					indPossuiBonus = true;
				
				if (tipoSaldo.getIdtTipoSaldo() == TipoSaldo.TORPEDOS)
					indPossuiSms = true;
				
				if (tipoSaldo.getIdtTipoSaldo() == TipoSaldo.DADOS)
					indPossuiDados = true;
			}			
		}
		
		try
		{
			// Cria um Builder XML
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docBuilder.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = doc.createElement("GPPConsultaAssinante");
			
			// Retorno
			DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
			String retorno = (assinante != null) ? conversorRetorno.format(
					assinante.getRetorno()) : conversorRetorno.format(Definicoes.RET_MSISDN_NAO_ATIVO);
			novoElemento(doc, root, "retorno", retorno);
			
			// MSISDN
			novoElemento(doc, root, "msisdn", (assinante != null) ? assinante.getMSISDN() : null);
			
			// Plano de Preco
			novoElemento(doc, root, "planoPreco", String.valueOf((assinante != null) ? assinante.getPlanoPreco(): 0));
			novoElemento(doc, root, "descPlanoPreco", descPlano);
		
			// Saldo principal
			Element tagSaldoPrincipal = novoElemento(doc, root, "saldoCreditosPrincipal", String.valueOf((assinante != null) ? assinante.getCreditosPrincipal(): 0.0));
			tagSaldoPrincipal.setAttribute("aplicavel", indPossuiPrincipal ? "true" : "false");
			
			// Saldo periodico		
			Element tagSaldoPeriodico = novoElemento(doc, root, "saldoCreditosPeriodico", String.valueOf((assinante != null) ? assinante.getCreditosPeriodico(): 0.0));
			tagSaldoPeriodico.setAttribute("aplicavel", indPossuiPeriodico ? "true" : "false");
			
			// Saldo de bonus
			Element tagSaldoBonus = novoElemento(doc, root, "saldoCreditosBonus", String.valueOf((assinante != null) ? assinante.getCreditosBonus(): 0.0));
			tagSaldoBonus.setAttribute("aplicavel", indPossuiBonus ? "true" : "false");
			
			// Saldo de sms
			Element tagSaldoSms = novoElemento(doc, root, "saldoCreditosSms", String.valueOf((assinante != null) ? assinante.getCreditosSms(): 0.0));
			tagSaldoSms.setAttribute("aplicavel", indPossuiSms ? "true" : "false");
			
			// Saldo de dados
			Element tagSaldoDados = novoElemento(doc, root, "saldoCreditosDados", String.valueOf((assinante != null) ? assinante.getCreditosDados(): 0.0));
			tagSaldoDados.setAttribute("aplicavel", indPossuiDados ? "true" : "false");
			
			// Status do assinante
			novoElemento(doc, root, "statusAssinante", String.valueOf((assinante != null) ? assinante.getStatusAssinante(): 0));
			novoElemento(doc, root, "descStatusAssinante", descStatusAssinante);
			
			// Status periodico
			novoElemento(doc, root, "statusPeriodico", String.valueOf((assinante != null) ? assinante.getStatusPeriodico(): 0));
			novoElemento(doc, root, "descStatusPeriodico", descStatusPeriodico);
			
			// Status de servico
			novoElemento(doc, root, "statusServico", String.valueOf((assinante != null) ? assinante.getStatusServico(): 0));
			novoElemento(doc, root, "descStatusServico", descStatusServico);
			
			// Expiracao de saldo principal
			Element tagDataExpiracaoPrincipal = novoElemento(doc, root, "dataExpiracaoPrincipal", (assinante != null) ? assinante.getDataExpiracaoPrincipal(): null);
			tagDataExpiracaoPrincipal.setAttribute("aplicavel", indPossuiPrincipal ? "true" : "false");
			
			// Expiracao de saldo periodico
			Element tagDataExpiracaoPeridico = novoElemento(doc, root, "dataExpiracaoPeridico", (assinante != null) ? assinante.getDataExpiracaoPeriodico(): null);
			tagDataExpiracaoPeridico.setAttribute("aplicavel", indPossuiPeriodico ? "true" : "false");
			
			// Expiracao de saldo de bonus
			Element tagDataExpiracaoBonus = novoElemento(doc, root, "dataExpiracaoBonus", (assinante != null) ? assinante.getDataExpiracaoBonus(): null);
			tagDataExpiracaoBonus.setAttribute("aplicavel", indPossuiBonus ? "true" : "false");
			
			// Expiracao de saldo de sms
			Element tagDataExpiracaoSms = novoElemento(doc, root, "dataExpiracaoSms", (assinante != null) ? assinante.getDataExpiracaoSms(): null);
			tagDataExpiracaoSms.setAttribute("aplicavel", indPossuiSms ? "true" : "false");
			
			// Expiracao de saldo de dados
			Element tagDataExpiracaoDados = novoElemento(doc, root, "dataExpiracaoDados", (assinante != null) ? assinante.getDataExpiracaoDados(): null);
			tagDataExpiracaoDados.setAttribute("aplicavel", indPossuiDados ? "true" : "false");
			
			// Data de ativacao
			novoElemento(doc, root, "dataAtivacao", (assinante != null && assinante.getDataAtivacao() != null) ? sdf.format(assinante.getDataAtivacao()): null);
			
			// IMSI
			Element tagImsi = novoElemento(doc, root, "imsi", (assinante != null) ? assinante.getIMSI(): null);
			tagImsi.setAttribute("aplicavel", (planoPreco != null && planoPreco.possuiImsi()) ? "true" : "false");
			
			// ATH
			Element tagFriendFamily = novoElemento(doc, root, "friendFamily", (assinante != null) ? assinante.getFriendFamily(): null);
			tagFriendFamily.setAttribute("aplicavel", (planoPreco != null && planoPreco.possuiAth()) ? "true" : "false");
			
			// Converte para String
			
			StringWriter stringWriter = new StringWriter();		
			DOMSource domSource = new DOMSource(root);
			StreamResult streamResult = new StreamResult(stringWriter);
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.setOutputProperty(OutputKeys.VERSION,"1.0");
			//serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			//serializer.setOutputProperty(OutputKeys.INDENT,"yes");
			serializer.transform(domSource, streamResult); 
			
			return stringWriter.toString();
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException(e.toString());
		}
	}
	
	/**
	 *	Cria o XML com as informacoes resumidas do assinante.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma Tecnomen.
	 *	@return		XML com as informacoes resumidas do assinante.
	 */
	public static String getXMLSimples(Assinante assinante)
	{
		//Obtendo o XML de retorno.
		GerarXML xml = new GerarXML("GPPConsultaAssinanteSimples");

		//Codigo de retorno.
		DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
		String retorno = (assinante != null) ? conversorRetorno.format(assinante.getRetorno()) : conversorRetorno.format(Definicoes.RET_MSISDN_NAO_ATIVO);
		xml.adicionaTag("retorno", retorno);
		
		//MSISDN.
		xml.adicionaTag("msisdn", (assinante != null) ? assinante.getMSISDN() : null);
		
		//IMSI.
		xml.adicionaTag("imsi", (assinante != null) ? assinante.getIMSI() : null);
		
		//Plano de preco.
		xml.adicionaTag("planoPreco", String.valueOf((assinante != null) ? assinante.getPlanoPreco() : 0));
		
		//Saldo Principal.
		xml.adicionaTag("saldoCreditosPrincipal", String.valueOf((assinante != null) ? assinante.getCreditosPrincipal() : 0.0));
		
		//Saldo Periodico.
		xml.adicionaTag("saldoCreditosPeriodico", String.valueOf((assinante != null) ? assinante.getCreditosPeriodico() : 0.0));

		//Saldo de Bonus.
		xml.adicionaTag("saldoCreditosBonus", String.valueOf((assinante != null) ? assinante.getCreditosBonus() : 0.0));
		
		//Saldo de Torpedos.
		xml.adicionaTag("saldoCreditosSms", String.valueOf((assinante != null) ? assinante.getCreditosSms() : 0.0));

		//Saldo de Dados.
		xml.adicionaTag("saldoCreditosDados", String.valueOf((assinante != null) ? assinante.getCreditosDados() : 0.0));
				
		//Status do assinante.
		xml.adicionaTag("statusAssinante", String.valueOf((assinante != null) ? assinante.getStatusAssinante() : 0));
		
		//Status periodico.
		xml.adicionaTag("statusPeriodico", String.valueOf((assinante != null) ? assinante.getStatusPeriodico() : 0));
		
		//Status de servico.
		xml.adicionaTag("statusServico", String.valueOf((assinante != null) ? assinante.getStatusServico() : 0));
		
		//Data de expiracao do Saldo Principal.
		xml.adicionaTag("dataExpiracaoPrincipal", (assinante != null) ? assinante.getDataExpiracaoPrincipal() : null);
		
		//Data de expiracao do Saldo Periodico.
		xml.adicionaTag("dataExpiracaoPeridico", (assinante != null) ? assinante.getDataExpiracaoPeriodico() : null);
		
		//Data de expiracao do Saldo de Bonus.
		xml.adicionaTag("dataExpiracaoBonus", (assinante != null) ? assinante.getDataExpiracaoBonus() : null);
		
		//Data de expiracao do Saldo de Torpedos.
		xml.adicionaTag("dataExpiracaoSms", (assinante != null) ? assinante.getDataExpiracaoSms() : null);
		
		//Data de expiracao do Saldo de Dados.
		xml.adicionaTag("dataExpiracaoDados", (assinante != null) ? assinante.getDataExpiracaoDados() : null);
		
		//Lista de Family and Friends (Amigos Toda Hora).
		xml.adicionaTag("friendFamily", (assinante != null) ? assinante.getFriendFamily() : null);
		
		return "<?xml version=\"1.0\"?>" + xml.getXML();
	}
	
	/**
	 * Cria um elemento XML contendo um texto.
	 * 
	 * @param doc		Factory de novas tags. Instancia de <code>Document</code>
	 * @param pai		Elemento pai (que conterá o novo elemento)
	 * @param filho		TagName do novo elemento
	 * @param valor		Conteúdo texto da nova
	 * @return	Elemento
	 */
	private static Element novoElemento(Document doc, Element pai, String filho, String valor)
	{
		Element el = doc.createElement(filho);
		el.appendChild(doc.createTextNode((valor != null) ? valor : ""));
		pai.appendChild(el);
		return el;
	}
	
}