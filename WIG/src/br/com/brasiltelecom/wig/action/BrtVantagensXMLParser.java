package br.com.brasiltelecom.wig.action;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.brasiltelecom.wig.entity.AlteracaoBrtVantagem;
import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.RetornoAtualizacaoGPP;
import br.com.brasiltelecom.wig.util.Definicoes;

/**
 * @author JOAO PAULO GALVAGNI
 * @since 03/04/2006
 * 
 */
public class BrtVantagensXMLParser 
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Metodo....: parseXML
	 * Descricao.: Realiza o parse apenas para obter o 
	 * 			   conteudo interno da tag [CDATA[]]
	 * 
	 * @param xml		- XML completo
	 * @return NodeList - NodeList da tag interna ao CDATA
	 * @throws Exception 
	 */
	public NodeList parseXML(String xml) throws Exception
	{
		try
		{
			// Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = parse.parse(is);
			
			// Cria um NodeList da <mensagem> e do <conteudo>
			NodeList root = ((Element) doc.getElementsByTagName("root").item(0)).getChildNodes();
			
			return root;
		}
		catch(Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro no parse do XML de Retorno. " + e + "\nXML: \n" + xml);
			throw e;
		}
	}
	
	/**
	 * Metodo....: parseMsisdn
	 * Descricao.: Realiza o parse do MSISDN, retirando os digitos 55 do inicio
	 * 
	 * @param msisdn	- MSISDN original com 10 ou 12 digitos
	 * @return String	- MSISDN formatado apenas com 10 digitos
	 */
	public static String parseMsisdn(String msisdn)
	{
		if (msisdn.length() == 12)
			return msisdn.substring(2);
		else
			return msisdn;
	}
	
	/**
	 * Metodo....: parseBrtVantagens
	 * Descricao.: Realiza o parse do XML de 
	 * 			   retorno da consulta junto ao Clarify
	 * 
	 * @param  xml			- XML de retorno da consulta do Clarify
	 * @return BrtVantagem	- Objeto contendo os dados do cliente
	 * @throws Exception
	 */
	public BrtVantagem parseBrtVantagens(String xml) throws Exception
	{
		// Objeto BrtVantagem que contera os dados do cliente
		BrtVantagem result = new BrtVantagem();
		
		// Colecoes para conter os numeros cadastrados
		// na promocao do Cliente
		Collection listaATHCelular = new ArrayList();
		Collection listaATHFixo = new ArrayList();
		try
		{
			logger.debug("Iniciando o parse do XML da consulta ao Clarify. ");
			
			// Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = parse.parse(is);
			
			// Cria um NodeList da <mensagem> e do <cabecalho>
			NodeList mensagem  = ((Element) doc.getElementsByTagName("mensagem").item(0)).getChildNodes();
			NodeList cabecalho = null;
			NodeList conteudo  = null;
			NodeList root	   = null;
			
			if (((Element)mensagem).getElementsByTagName("cabecalho").item(0) != null && 
				((Element)mensagem).getElementsByTagName("cabecalho").item(0).getChildNodes().item(0) != null )
				cabecalho = ((Element)mensagem).getElementsByTagName("cabecalho").item(0).getChildNodes();
			
			// Seta o Msisdn do cliente que fez a requisicao TAG: <identificador_requisicao>
			if (((Element)cabecalho).getElementsByTagName("identificador_requisicao").item(0) != null && 
				((Element)cabecalho).getElementsByTagName("identificador_requisicao").item(0).getChildNodes().item(0) != null )
				result.setMsisdn(((Element)cabecalho).getElementsByTagName("identificador_requisicao").item(0).getChildNodes().item(0).getNodeValue());
			
			// Cria um NodeList do <conteudo>
			if (((Element)mensagem).getElementsByTagName("conteudo").item(0) != null && 
				((Element)mensagem).getElementsByTagName("conteudo").item(0).getChildNodes().item(0) != null )
				conteudo  = ((Element)mensagem).getElementsByTagName("conteudo").item(0).getChildNodes();
			
			// Realiza o Parse do XML contido na TAG <![CDATA[ xml ]]>
			if (conteudo != null)
				root = parseXML(conteudo.item(0).getNodeValue());
			
			// Seta o CodRetorno da requisicao TAG: <cod_retorno>
			if (((Element)root).getElementsByTagName("cod_retorno").item(0) != null && 
				((Element)root).getElementsByTagName("cod_retorno").item(0).getChildNodes().item(0) != null )
				result.setCodRetorno(((Element)root).getElementsByTagName("cod_retorno").item(0).getChildNodes().item(0).getNodeValue());
			
			// Seta a categoria (plano) do assinante TAG: <categoria_primaria>
			if (((Element)root).getElementsByTagName("categoria_primaria").item(0) != null &&
				((Element)root).getElementsByTagName("categoria_primaria").item(0).getChildNodes().item(0) != null)
				result.setPlano(((Element)root).getElementsByTagName("categoria_primaria").item(0).getChildNodes().item(0).getNodeValue());
			
			// Seta o isAtivo da requisicao TAG: <status_acesso>
			if (((Element)root).getElementsByTagName("status_acesso").item(0) != null && 
				((Element)root).getElementsByTagName("status_acesso").item(0).getChildNodes().item(0) != null)
				result.setAtivo(((Element)root).getElementsByTagName("status_acesso").item(0).getChildNodes().item(0).getNodeValue());
			
			// Valida se o NodeList do Amigos Toda Hora eh nao nulo
			if (((Element)root).getElementsByTagName("amigos_toda_hora").item(0) != null)
			{
				// Cria um NodeList do <amigos_toda_hora> 
				NodeList noATH = ((Element)root).getElementsByTagName("amigos_toda_hora").item(0).getChildNodes();
				
				// Seta a QtdeAmigosTodaHoraCelular TAG: <qtd_amigos_toda_hora_cel>
				if (((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora_cel").item(0) != null && 
					((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora_cel").item(0).getChildNodes().item(0) != null)
					result.setQtdeAmigosTodaHoraCelular(Integer.parseInt(((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora_cel").item(0).getChildNodes().item(0).getNodeValue()));
				
				// Seta a QtdeAmigosTodaHoraFixa TAG: <qtd_amigos_toda_hora_fixa>
				if (((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora_fixa").item(0) != null && 
					((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora_fixa").item(0).getChildNodes().item(0) != null)
					result.setQtdeAmigosTodaHoraFixo(Integer.parseInt(((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora_fixa").item(0).getChildNodes().item(0).getNodeValue()));
				
				// Seta a QtdeAmigosTodaHoraFixa TAG: <qtd_amigos_toda_hora>
				if (((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora").item(0) != null && 
					((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora").item(0).getChildNodes().item(0) != null)
					result.setQtdeAmigosTodaHora(Integer.parseInt(((Element)noATH).getElementsByTagName("qtd_amigos_toda_hora").item(0).getChildNodes().item(0).getNodeValue()));
				
				// Seta a DiasAtualizacaoAmigosTodaHora <dias_atualizacao_irmaos14>
				if (((Element)noATH).getElementsByTagName("dias_atualizacao_irmaos14").item(0) != null && 
					((Element)noATH).getElementsByTagName("dias_atualizacao_irmaos14").item(0).getChildNodes().item(0) != null)
					result.setDiasAtualizacaoAmigosTodaHora(Integer.parseInt(((Element)noATH).getElementsByTagName("dias_atualizacao_irmaos14").item(0).getChildNodes().item(0).getNodeValue()));
	
				// Seta a DiasUltAlteracaoAmigosTodaHora <dias_ultima_alteracao_irmaos14>
				if (((Element)noATH).getElementsByTagName("dias_ultima_alteracao_irmaos14").item(0) != null && 
					((Element)noATH).getElementsByTagName("dias_ultima_alteracao_irmaos14").item(0).getChildNodes().item(0) != null)
					result.setDiasUltAlteracaoAmigosTodaHora(Integer.parseInt(((Element)noATH).getElementsByTagName("dias_ultima_alteracao_irmaos14").item(0).getChildNodes().item(0).getNodeValue()));
				
				// Seta se havera cobranca para alteracao no Amigos Toda Hora <cobranca>
				if (((Element)noATH).getElementsByTagName("cobranca").item(0) != null && 
					((Element)noATH).getElementsByTagName("cobranca").item(0).getChildNodes().item(0) != null)
					result.setEfetuaCobrancaATH(((Element)noATH).getElementsByTagName("cobranca").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta se eh o Novissimo Brasil Vantagens <servico>
				if (((Element)noATH).getElementsByTagName("servico").item(0) != null && 
					((Element)noATH).getElementsByTagName("servico").item(0).getChildNodes().item(0) != null)
					result.setNovissimoATH(((Element)noATH).getElementsByTagName("servico").item(0).getChildNodes().item(0).getNodeValue());
				
				// Cria um NodeList do <acesso_movel>
				NodeList noAcessoMovel = ((Element)noATH).getElementsByTagName("acesso_movel").item(0).getChildNodes();
				
				if (result.isNovissimoATH())
				{
					// Varre o noAcessoMovel e seta os Msisdn's cadastrados TAG: <msisdn>
					for (int i=0; i < noAcessoMovel.getLength(); i++)
					{
						Node no = noAcessoMovel.item(i);
						if (no.getNodeType() == Node.ELEMENT_NODE && no.getChildNodes().item(0) != null)
						{
							listaATHCelular.add(no.getChildNodes().item(0).getNodeValue());
							result.setQtdeAmigosTodaHora(result.getQtdeAmigosTodaHora()+1);
						}
					}
					// Seta os AmigosTodaHoraCelular TAGs: <msisdn> da TAG pai: <acesso_movel>
					result.setAmigosTodaHoraCelular(listaATHCelular);
				}
				else
				{
					// Varre o noAcessoMovel e seta os Msisdn's cadastrados TAG: <msisdn>
					for (int i=0; i < noAcessoMovel.getLength(); i++)
					{
						Node no = noAcessoMovel.item(i);
						if (no.getNodeType() == Node.ELEMENT_NODE && no.getChildNodes().item(0) != null)
						{
							listaATHCelular.add(no.getChildNodes().item(0).getNodeValue());
							result.setQtdeAmigosTodaHoraCelular(result.getQtdeAmigosTodaHoraCelular()+1);
							result.setQtdeAmigosTodaHora(result.getQtdeAmigosTodaHora()+1);
						}
					}
					// Seta os AmigosTodaHoraCelular TAGs: <msisdn> da TAG pai: <acesso_movel>
					result.setAmigosTodaHoraCelular(listaATHCelular);
					
					// Cria um NodeList do <terminal>
					NodeList noTerminal = ((Element)noATH).getElementsByTagName("terminal").item(0).getChildNodes();
					
					// Varre o noTerminal e seta os Msisdn`s cadastrados TAG: <msisdn>
					for (int i=0; i < noTerminal.getLength(); i++)
					{
						Node no = noTerminal.item(i);
						if (no.getNodeType() == Node.ELEMENT_NODE && no.getChildNodes().item(0) != null)
						{
							listaATHFixo.add(no.getChildNodes().item(0).getNodeValue());
							result.setQtdeAmigosTodaHoraFixo(result.getQtdeAmigosTodaHoraFixo()+1);
							result.setQtdeAmigosTodaHora(result.getQtdeAmigosTodaHora()+1);
						}
					}
					// Seta os AmigosTodaHoraFixo TAGs: <msisdn> da TAG pai: <terminal>
					result.setAmigosTodaHoraFixo(listaATHFixo);
				}
			}
			
			// Valida se existe o NodeList do Bonus Todo Mes
			if (((Element)root).getElementsByTagName("bonus_todo_mes").item(0) != null)
			{
				// Cria um NodeList do <bonus_todo_mes>
				NodeList noBTM = ((Element)root).getElementsByTagName("bonus_todo_mes").item(0).getChildNodes();
				
				// Seta quantos DiasUltAlteracaoBonusTodoMes TAG: <dias_ultima_alteracao_bonus>
				if (((Element)noBTM).getElementsByTagName("dias_ultima_alteracao_bonus").item(0) != null && 
					((Element)noBTM).getElementsByTagName("dias_ultima_alteracao_bonus").item(0).getChildNodes().item(0) != null)
					result.setDiasUltAlteracaoBonusTodoMes(Integer.parseInt(((Element)noBTM).getElementsByTagName("dias_ultima_alteracao_bonus").item(0).getChildNodes().item(0).getNodeValue()));
				
				// Seta quantos DiasAtualizacaoBonusTodoMes TAG: <dias_atualizacao_bonus>
				if (((Element)noBTM).getElementsByTagName("dias_atualizacao_bonus").item(0) != null && 
					((Element)noBTM).getElementsByTagName("dias_atualizacao_bonus").item(0).getChildNodes().item(0) != null)
					result.setDiasAtualizacaoBonusTodoMes(Integer.parseInt(((Element)noBTM).getElementsByTagName("dias_atualizacao_bonus").item(0).getChildNodes().item(0).getNodeValue()));
				
				// Seta a tag de cobranca do Bonus Todo Mes TAG: <cobranca>
				if (((Element)noBTM).getElementsByTagName("cobranca").item(0) != null && 
					((Element)noBTM).getElementsByTagName("cobranca").item(0).getChildNodes().item(0) != null)
					result.setEfetuaCobrancaBTM(((Element)noBTM).getElementsByTagName("cobranca").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta se eh o Novissimo Bonus Todo Mes <servico>
				if (((Element)noBTM).getElementsByTagName("servico").item(0) != null && 
					((Element)noBTM).getElementsByTagName("servico").item(0).getChildNodes().item(0) != null)
					result.setNovissimoBTM(((Element)noBTM).getElementsByTagName("servico").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta o Msisdn do Bonus Todo Mes TAG: <msisdn>
				if (((Element)noBTM).getElementsByTagName("msisdn").item(0) != null && 
					((Element)noBTM).getElementsByTagName("msisdn").item(0).getChildNodes().item(0) != null)
					result.setMsisdnBonusTodoMes(((Element)noBTM).getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
			}
			
			// Valida se existe o NodeList do Bumerangue 14
			if (((Element)root).getElementsByTagName("bumerangue_14").item(0) != null)
			{
				// Cria um NodeList do <bumerangue_14>
				NodeList noB14 = ((Element)root).getElementsByTagName("bumerangue_14").item(0).getChildNodes();
				
				// Seta se o servico Bumerangue14 esta ativo <ativo>
				if (((Element)noB14).getElementsByTagName("ativo").item(0) != null && 
					((Element)noB14).getElementsByTagName("ativo").item(0).getChildNodes().item(0) != null)
					result.setBumerangueAtivo(((Element)noB14).getElementsByTagName("ativo").item(0).getChildNodes().item(0).getNodeValue());
			}
			
			logger.debug("Parse do XML realizado com sucesso.");
		}
		catch(Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro no parse do XML do BrtVantagens. \nXML: \n" + xml, e);
			throw e;
		}
		//Retorna o objeto BrtVantagem
		return result;
	}
	
	/**
	 * Metodo....:parseXMLRetorno
	 * Descricao.:Realiza o parse no xml de retorno da validacao do numero a ser
	 *            cadastrado no BrtVantagens
	 *            
	 * @param xml - XML resultado da validacao
	 * @return MensagemRetorno - Objeto contendo os valores de retorno
	 * @throws Exception 
	 */
	public MensagemRetorno parseXMLRetorno(String xml) throws Exception
	{
		MensagemRetorno msg = new MensagemRetorno();
		
		try
		{
			logger.debug("Iniciando o parse do XML de retorno do SAC...");
			// Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = parse.parse(is);
			
			// Cria um NodeList da <mensagem>
			NodeList mensagem = ((Element) doc.getElementsByTagName("mensagem").item(0)).getChildNodes();
			/*NodeList cabecalho = ((Element)mensagem).getElementsByTagName("cabecalho").item(0).getChildNodes();*/
			
			// Cria um NodeList do <conteudo>
			NodeList conteudo  = ((Element)mensagem).getElementsByTagName("conteudo").item(0).getChildNodes();
			
			// Realiza o Parse do XML contido na TAG <![CDATA[xml]]>
			NodeList root = parseXML(conteudo.item(0).getNodeValue());
			
			// Seta o codigo de retorno da consulta ao SAC TAG: <cod_retrono>
			if (((Element)root).getElementsByTagName("cod_retorno").item(0) != null &&
				((Element)root).getElementsByTagName("cod_retorno").item(0).getChildNodes().item(0) != null)
			{
				msg.setCodRetorno(((Element)root).getElementsByTagName("cod_retorno").item(0).getChildNodes().item(0).getNodeValue());
				msg.setCodRetornoNum(Integer.parseInt(((Element)root).getElementsByTagName("cod_retorno").item(0).getChildNodes().item(0).getNodeValue()));
			}
			
			// Seta a mensagem de retorno da consulta ao SAC TAG: <mensagem>
			if (((Element)root).getElementsByTagName("mensagem").item(0) != null &&
				((Element)root).getElementsByTagName("mensagem").item(0).getChildNodes().item(0) != null)
				msg.setMensagem(((Element)root).getElementsByTagName("mensagem").item(0).getChildNodes().item(0).getNodeValue());
			
			// Seta o codigo de erro da consulta ao SAC TAG: <cod_erro>
			if (((Element)root).getElementsByTagName("cod_erro").item(0) != null &&
				((Element)root).getElementsByTagName("cod_erro").item(0).getChildNodes().item(0) != null)
				msg.setCodErroSAC(((Element)root).getElementsByTagName("cod_erro").item(0).getChildNodes().item(0).getNodeValue());
			
			logger.debug("Parse do XML de retorno do SAC realizado com sucesso.");
		}
		catch(Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro no parse do XML de Retorno. " + e + "\nXML: \n" + xml);
			throw e;
		}
		//Retorna o objeto MensagemRetorno
		return msg;
	}
	
	/**
	 * Metodo....: parseXMLRetornoGPP
	 * Descricao.: Realiza o parse do XML de retorno apos atualizacao / 
	 * 			   tarifacao da promocao Amigos Toda Hora no GPP
	 * 
	 * @param 	xml						- XML originado pelo GPP
	 * @return 	RetornoAtualizacaoGPP	- Objeto populado com os dados do XML
	 * @throws 	Exception
	 */
	public RetornoAtualizacaoGPP parseXMLRetornoGPP (String xml) throws Exception
	{
		RetornoAtualizacaoGPP msg = new RetornoAtualizacaoGPP();
		
		try
		{
			logger.debug("Iniciando o parse do XML de retorno do GPP...");
			// Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = parse.parse(is);
			
			// Cria um NodeList da <mensagem>
			NodeList mensagem = ((Element) doc.getElementsByTagName("mensagem").item(0)).getChildNodes();
			
			// Cria um NodeList do <conteudo>
			NodeList conteudo  = ((Element)mensagem).getElementsByTagName("conteudo").item(0).getChildNodes();
			
			// Realiza o Parse do XML contido na TAG <![CDATA[xml]]>
			NodeList root = parseXML(conteudo.item(0).getNodeValue());
			
			if (((Element)root).getElementsByTagName("GPPServico").item(0) != null )
			{
				// Cria um NodeList do <GPPServico>
				NodeList GPPServico = ((Element)root).getElementsByTagName("GPPServico").item(0).getChildNodes();
				
				// Seta a operacao que foi realizada
				if (((Element)GPPServico).getElementsByTagName("operacao").item(0) != null &&
					((Element)GPPServico).getElementsByTagName("operacao").item(0).getChildNodes().item(0) != null)
					msg.setOperacao(((Element)GPPServico).getElementsByTagName("operacao").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta o Codigo de Retorno <codRetorno>
				if (((Element)GPPServico).getElementsByTagName("codRetorno").item(0) != null &&
					((Element)GPPServico).getElementsByTagName("codRetorno").item(0).getChildNodes().item(0) != null)
					msg.setCodRetorno(((Element)GPPServico).getElementsByTagName("codRetorno").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta a descricao do Retorno <descRetorno>
				if (((Element)GPPServico).getElementsByTagName("descRetorno").item(0) != null &&
					((Element)GPPServico).getElementsByTagName("descRetorno").item(0).getChildNodes().item(0) != null)
					msg.setDescRetorno(((Element)GPPServico).getElementsByTagName("descRetorno").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta o codigo de servico <codServico>
				if (((Element)GPPServico).getElementsByTagName("codServico").item(0) != null &&
					((Element)GPPServico).getElementsByTagName("codServico").item(0).getChildNodes().item(0) != null)
					msg.setCodServico(((Element)GPPServico).getElementsByTagName("codServico").item(0).getChildNodes().item(0).getNodeValue());
				
				// Seta a descricao do Servico <descServico>
				if (((Element)GPPServico).getElementsByTagName("descServico").item(0) != null &&
					((Element)GPPServico).getElementsByTagName("descServico").item(0).getChildNodes().item(0) != null)
					msg.setDescServico(((Element)GPPServico).getElementsByTagName("descServico").item(0).getChildNodes().item(0).getNodeValue());

				// Seta a descricao do Servico <descServico>
				if (((Element)GPPServico).getElementsByTagName("valor").item(0) != null &&
					((Element)GPPServico).getElementsByTagName("valor").item(0).getChildNodes().item(0) != null)
					msg.setValor(((Element)GPPServico).getElementsByTagName("valor").item(0).getChildNodes().item(0).getNodeValue());
				
				// Verifica se a tag assinante eh nula
				if (((Element)GPPServico).getElementsByTagName("assinante").item(0) != null )
				{
					// Cria um NodeList do <assinante>
					NodeList assinante = ((Element)GPPServico).getElementsByTagName("assinante").item(0).getChildNodes();
					
					// Seta o msisdn do assinante <msisdn>
					if (((Element)assinante).getElementsByTagName("msisdn").item(0) != null &&
						((Element)assinante).getElementsByTagName("msisdn").item(0).getChildNodes().item(0) != null)
						msg.setMsisdn(((Element)assinante).getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
					
					// Seta os saldos do assinante
					// Saldo Principal: <saldoPrincipal>
					// Saldo de Bonus : <saldoBonus>
					// Saldo de SMS   : <saldoSMS>
					// Saldo de Dados : <saldoDados>
					if (((Element)assinante).getElementsByTagName("saldoPrincipal").item(0) != null &&
						((Element)assinante).getElementsByTagName("saldoPrincipal").item(0).getChildNodes().item(0) != null)
						msg.setSaldoPrincipal(( (Element)assinante ).getElementsByTagName("saldoPrincipal").item(0).getChildNodes().item(0).getNodeValue());
					if (((Element)assinante).getElementsByTagName("saldoBonus").item(0) != null &&
						((Element)assinante).getElementsByTagName("saldoBonus").item(0).getChildNodes().item(0) != null)
						msg.setSaldoBonus(( (Element)assinante ).getElementsByTagName("saldoBonus").item(0).getChildNodes().item(0).getNodeValue());
					if (((Element)assinante).getElementsByTagName("saldoSMS").item(0) != null &&
						((Element)assinante).getElementsByTagName("saldoSMS").item(0).getChildNodes().item(0) != null)
						msg.setSaldoSMS(( (Element)assinante ).getElementsByTagName("saldoSMS").item(0).getChildNodes().item(0).getNodeValue());
					if (((Element)assinante).getElementsByTagName("saldoDados").item(0) != null &&
						((Element)assinante).getElementsByTagName("saldoDados").item(0).getChildNodes().item(0) != null)
						msg.setSaldoDados(( (Element)assinante ).getElementsByTagName("saldoDados").item(0).getChildNodes().item(0).getNodeValue());
				}
			}
			
			logger.debug("Parse do XML de retorno do GPP realizado com sucesso.");
		}
		catch(Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro no parse do XML de Retorno. " + e + "\nXML: \n" + xml);
			throw e;
		}
		
		//Retorna o objeto MensagemRetorno
		return msg;
	}
	
	/**
	 * Metodo....:getXMLConsBrtVantagens
	 * Descricao.:Retorna o XML a ser utilizado para a consulta dos numeros
	 *            cadastrados no BrtVantagens para o assinante
	 * @param msisdn - Assinante a ser pesquisado
	 * @return String - XML de consulta
	 */
	public String getXMLConsBrtVantagens(String msisdn)
	{
		logger.debug("Iniciando a construcao do XML para envio ao Clarify.");
		StringBuffer xml = new StringBuffer(Definicoes.WIG_XML_PROLOG);
		xml.append("<mensagem>");
		xml.append("<cabecalho>");
		xml.append("<empresa>").append(Definicoes.WIG_XML_EMPRESA).append("</empresa>");
		xml.append("<sistema>").append(Definicoes.WIG_XML_SISTEMA).append("</sistema>");
		xml.append("<processo>").append(Definicoes.WIG_XML_PROCESSO_CLY).append("</processo>");
		xml.append("<data>").append(sdf.format(Calendar.getInstance().getTime())).append("</data>");
		xml.append("<identificador_requisicao>").append(parseMsisdn(msisdn)).append("</identificador_requisicao>");
		xml.append("</cabecalho>");
		xml.append("<conteudo>");
		xml.append("<![CDATA[").append(getXMLConsultaPorNumero(msisdn)).append("]]>");
		xml.append("</conteudo>");
		xml.append("</mensagem>");
		
		logger.debug("XML construido com sucesso\n" + xml.toString());
		return xml.toString();
	}
	
	/**
	 * Metodo....:getXMLConsultaPorNumero
	 * Descricao.:Retorna o xml de consulta por assinante
	 * @param msisdn - Assinante a ser pesquisado
	 * @return String - XML
	 */
	private String getXMLConsultaPorNumero(String msisdn)
	{
		StringBuffer xml = new StringBuffer(Definicoes.WIG_XML_PROLOG);
		xml.append("<root>");
		xml.append("<msisdn>").append(parseMsisdn(msisdn)).append("</msisdn>");
		xml.append("</root>");

		return xml.toString();
	}
	
	/**
	 * Metodo....:getXMLValidaBrtVantagens
	 * Descricao.:Retorna o XML a ser utilizado para validacao do BrtVantagens
	 * @param msisdn - Msisdn a ser verificado
	 * @param regra  - Regra a ser verificada (BTM ou ATH)
	 * @param plano	 - Plano do assinante (Pre-pago, Pos-pago ou Hibrido)
 	 * @return String xml a ser utilizado na pesquisa
	 */
	public String getXMLValidaBrtVantagens(String msisdn, String regra, String plano)
	{
		StringBuffer xml = new StringBuffer(Definicoes.WIG_XML_PROLOG);
		xml.append("<mensagem>");
		xml.append("<cabecalho>");
		xml.append("<empresa>").append(Definicoes.WIG_XML_EMPRESA).append("</empresa>");
		xml.append("<sistema>").append(Definicoes.WIG_XML_SISTEMA).append("</sistema>");
		xml.append("<processo>").append(Definicoes.WIG_XML_PROCESSO_SAC).append("</processo>");
		xml.append("<data>").append(sdf.format(Calendar.getInstance().getTime())).append("</data>");
		xml.append("<identificador_requisicao>").append(parseMsisdn(msisdn)).append("</identificador_requisicao>");
		xml.append("</cabecalho>");
		xml.append("<conteudo><![CDATA[").append(getXMLValidaPorNumero(msisdn,regra, plano)).append("]]>");
		xml.append("</conteudo>");
		xml.append("</mensagem>");
		
		return xml.toString();
	}
	
	/**
	 * Metodo....: getXMLValidaPorNumero
	 * Descricao.: Retorna o XML incluindo o numero a ser pesquisado
	 * 
	 * @param msisdn - Msisdn a ser pesquisado
	 * @param regra  - Regra a ser inserida (BTH ou ATH)
	 * @return XML	 - Conteudo do CDATA para o SAC
	 * @deprecated   - Alteracoes para envio do plano
	 */
	private String getXMLValidaPorNumero(String msisdn, String regra)
	{
		StringBuffer xml = new StringBuffer(Definicoes.WIG_XML_PROLOG);
		xml.append("<root>");
		xml.append("<operadora>").append(Definicoes.WIG_XML_COD_OPERADORA).append("</operadora>");
		xml.append("<msisdn>").append(parseMsisdn(msisdn)).append("</msisdn>");
		xml.append("<regra>").append(regra).append("</regra>");
		xml.append("</root>");

		return xml.toString();
	}
	
	/**
	 * Metodo....: getXMLValidaPorNumero
	 * Descricao.: Retorna o XML incluindo o numero a ser pesquisado
	 * 
	 * @param msisdn - Msisdn a ser pesquisado
	 * @param regra  - Regra a ser inserida (BTH ou ATH)
	 * @param plano	 - Plano do assinante (Pos-pago, Pre-pago ou Hibrido)
	 * @return XML
	 */
	private String getXMLValidaPorNumero(String msisdn, String regra, String plano)
	{
		StringBuffer xml = new StringBuffer(Definicoes.WIG_XML_PROLOG);
		xml.append("<root>");
		xml.append("<operadora>").append(Definicoes.WIG_XML_COD_OPERADORA).append("</operadora>");
		xml.append("<msisdn>").append(parseMsisdn(msisdn)).append("</msisdn>");
		xml.append("<regra>").append(regra).append("</regra>");
		if ( (plano != null) && (!"".equals(plano)) )
			xml.append("<plano>").append(plano).append("</plano>");
		xml.append("</root>");
		
		return xml.toString();
	}
	
	/**
	 * Metodo....: getXMLAlteracaoPorNumero
	 * Descricao.: Retorna a tag do XML para alteracao tanto 
	 * 			   do Amigos Toda Hora quanto do Bonus Todo Mes
	 * 
	 * @param alteracaoBrtVantagem	- objeto contendo os valores antigo, novo e acao a ser tomada
	 * @return String				- TAG do XML pronta para alteracao
	 */
	public String getXMLAlteracaoPorNumero(AlteracaoBrtVantagem alteracaoBrtVantagem)
	{
		return "<valor_antigo>"+alteracaoBrtVantagem.getValorAntigo()+"</valor_antigo>" +
			   "<valor_novo>"+alteracaoBrtVantagem.getValorNovo()+"</valor_novo>" 		+
			   "<acao>"+alteracaoBrtVantagem.getAcao()+"</acao>"						;
	}
	
	/**
	 * Metodo....: getXMLBrtVantagensAlt
	 * Descricao.: Recebe os numeros a serem alterados, seja do Amigos Toda Hora
	 * 			   ou o Bonus Todo Mes
	 * 
	 * @param  celularesATH		- Colecao contendo objetos AlteracaoBrtVantagem dos numeros celulares
	 * @param  fixosATH			- Colecao contendo objetos AlteracaoBrtVantagem dos numeros fixos
	 * @param  fixoBTM			- Objeto AlteracaoBrtVantagem da promocao BTM
	 * @param  acaoBumerangue14	- Acao a ser tomada da promocao Bumerangue14
	 * @return String			- XML pronto para ser inserido no Banco (INTERFACE_VITRIA_OUT)
	 */
	public String getXMLBrtVantagensAlt(String msisdn, Collection celularesATH, Collection fixosATH, AlteracaoBrtVantagem fixoBTM, String acaoBumerangue14)
	{
		StringBuffer xml = new StringBuffer(Definicoes.WIG_XML_PROLOG);
		xml.append("<mensagem>");
		xml.append("<cabecalho>");
		xml.append("<empresa>").append(Definicoes.WIG_XML_EMPRESA).append("</empresa>");
		xml.append("<sistema>").append(Definicoes.WIG_XML_SISTEMA).append("</sistema>");
		xml.append("<processo>").append(Definicoes.WIG_XML_PROCESSO_OS).append("</processo>");
		xml.append("<data>").append(sdf.format(Calendar.getInstance().getTime())).append("</data>");
		xml.append("<identificador_requisicao>").append(parseMsisdn(msisdn)).append("</identificador_requisicao>");
		xml.append("</cabecalho>");
		xml.append("<conteudo>");
		xml.append("<![CDATA[").append(Definicoes.WIG_XML_PROLOG);
		xml.append("<root>");
		xml.append("<msisdn>").append(parseMsisdn(msisdn)).append("</msisdn>");
		
		xml.append("<lista_amigos_toda_hora>");
		
		if (celularesATH != null)
		{
			xml.append("<lista_acesso_movel>");
			for (Iterator i = celularesATH.iterator() ; i.hasNext() ; )
			{
				xml.append("<amigos_toda_hora>");
				xml.append(getXMLAlteracaoPorNumero((AlteracaoBrtVantagem)i.next()));
				xml.append("</amigos_toda_hora>");
			}
			xml.append("</lista_acesso_movel>");
		}
		else
			xml.append("<lista_acesso_movel/>");
		
		if (fixosATH != null)
		{
			xml.append("<lista_terminal>");
			for (Iterator i = fixosATH.iterator() ; i.hasNext() ; )
			{
				xml.append("<amigos_toda_hora>");
				xml.append(getXMLAlteracaoPorNumero((AlteracaoBrtVantagem)i.next()));
				xml.append("</amigos_toda_hora>");
			}
			xml.append("</lista_terminal>");
		}
		else
			xml.append("<lista_terminal/>");
		
		xml.append("</lista_amigos_toda_hora>");
		
		xml.append("<lista_bonus_todo_mes>");
		if (fixoBTM != null)
		{
			xml.append("<bonus_todo_mes>");
			xml.append(getXMLAlteracaoPorNumero(fixoBTM));
			xml.append("</bonus_todo_mes>");
		}
		else
			xml.append("<bonus_todo_mes/>");
		xml.append("</lista_bonus_todo_mes>");
		
		if ("".equalsIgnoreCase(acaoBumerangue14) || acaoBumerangue14 == null)
			xml.append("<bumerangue_14/>");
		else
		{
			xml.append("<bumerangue_14>");
			xml.append("<acao>").append(acaoBumerangue14).append("</acao>");
			xml.append("</bumerangue_14>");
		}
		
		xml.append("</root>");
		
		xml.append("]]>");
		xml.append("</conteudo>");
		xml.append("</mensagem>");
		
		return xml.toString();
	}
}
