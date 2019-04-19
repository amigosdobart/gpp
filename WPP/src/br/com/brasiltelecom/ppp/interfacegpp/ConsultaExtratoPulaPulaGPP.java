package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.omg.CORBA.ORB;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.brasiltelecom.ppp.dao.DescontoPulaPulaDao;
import br.com.brasiltelecom.ppp.dao.PromocaoDAO;
import br.com.brasiltelecom.ppp.dao.PromocaoTipoBonificacaoDao;
import br.com.brasiltelecom.ppp.model.ExtratoPulaPula;
import br.com.brasiltelecom.ppp.model.RetornoExtratoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

/**
 *	Efetua a conexao com o GPP a fim de obter o Extrato Pula-Pula.
 *
 *	@author Daniel Ferreira
 *	@since	10/10/2005
 *
 *  Atualizado por Bernardo Vernge (tarifacao diferenciada CT)
 *  07/11/2007
 */
public class ConsultaExtratoPulaPulaGPP
{

	/**
	 *	Obtem o XML de extrato vindo do GPP.
	 *
	 * @param		String					msisdn						MSISDN do assinante.
	 * @param		String					dataInicial					Data inicial de pesquisa.
	 * @param		String					dataFinal					Data final de pesquisa.
	 * @param		String					servidor					IP do servidor.
	 * @param		String					porta						Porta do servidor.
	 * @param		boolean					consultaCheia				Indica se a consulta será cheia.
	 * @return		String												XML com o extrato Pula-Pula do assinante.
	 * @throws GPPTecnomenException 
	 * @throws GPPInternalErrorException 
	 */
	public static String getXml(String msisdn,String dataInicial, String dataFinal,String servidor, String porta, boolean consultaCheia) throws GPPInternalErrorException, GPPTecnomenException
	{
		String ret = "";

		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		if(consultaCheia)
			ret = pPOA.consultaExtratoPulaPulaCheio(msisdn,dataInicial,dataFinal);
		else
			ret = pPOA.consultaExtratoPulaPula(msisdn,dataInicial,dataFinal);

		return ret;
	}

	/**
	 *	Retorna um extrato Pula-Pula.
	 *
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial do extrtato.
	 *	@param		String					dataFinal					Data final do extrato.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@param		boolean					consultaCheia				Indica se a consulta será cheia.
	 *	@return		RetornoExtratoPulaPula								Extrato Pula-Pula do assinante.
	 * @throws ParseException 
	 * @throws SAXException 
	 * @throws DOMException 
	 * @throws ParseException 
	 * @throws GPPTecnomenException 
	 * @throws GPPInternalErrorException 
	 */
	public static RetornoExtratoPulaPula getExtratos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, boolean consultaCheia, Session session) throws DOMException, SAXException, ParseException, GPPInternalErrorException, GPPTecnomenException
	{
	    String xmlExtrato = ConsultaExtratoPulaPulaGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta, consultaCheia);
	    try
	    {
	    	return ConsultaExtratoPulaPulaGPP.parse(xmlExtrato, session);
	    }
	    catch(IOException naoAcontece){}
	    catch(ParserConfigurationException naoAcontece){}
	    return null;
	}

	/**
	 *	Retorna um extrato Pula-Pula, escrevendo-o em arquivo para paginacao.
	 *
	 * @param		String					msisdn						MSISDN do assinante.
	 * @param		String					dataInicial					Data inicial do extrtato.
	 * @param		String					dataFinal					Data final do extrato.
	 * @param		String					servidor					IP do servidor.
	 * @param		String					porta						Porta do servidor.
	 * @param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas
	 *																	de diretorios temporarios em cada maquina
	 *																	rodando o IAS e o GPP.
	 * @param		String					sessionId					Identificador da sessao.
	 * @param		boolean					consultaCheia				Indica se a consulta será cheia.
	 * @return		RetornoExtratoPulaPula								Extrato Pula-Pula do assinante.
	 * @throws GPPTecnomenException 
	 * @throws GPPInternalErrorException 
	 * @throws ParseException 
	 * @throws SAXException 
	 * @throws DOMException 
	 */
	public static RetornoExtratoPulaPula getExtratos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, Collection diretorios, String sessionId, boolean consultaCheia, Session session) throws GPPInternalErrorException, GPPTecnomenException, DOMException, SAXException, ParseException
	{
	    String xmlExtrato = ConsultaExtratoPulaPulaGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta, consultaCheia);
	    ConsultaExtratoPulaPulaGPP.saveToFile(xmlExtrato, diretorios, sessionId);
	    try
	    {
	    	return ConsultaExtratoPulaPulaGPP.parse(xmlExtrato, session);
	    }
	    catch(IOException naoAcontece){}
	    catch(ParserConfigurationException naoAcontece){}
	    
	    return null;
	}

	/**
	 *	Retorna um extrato Pula-Pula a partir de arquivo ja criado. Caso o arquivo nao exista, obtem o extrato a
	 *	partir de interface com o GPP.
	 *
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial do extrtato.
	 *	@param		String					dataFinal					Data final do extrato.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas
	 *																	de diretorios temporarios em cada maquina
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao.
	 *	@param		boolean					consultaCheia				Indica se a consulta será cheia.
	 *	@return		RetornoExtrato										Extrato do assinante.
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws SAXException 
	 * @throws DOMException 
	 * @throws GPPTecnomenException 
	 * @throws GPPInternalErrorException 
	 * @throws ParserConfigurationException 
	 */
	public static RetornoExtratoPulaPula getExtratosPulaPulaFromFile(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, Collection diretorios, String sessionId, boolean consultaCheia, Session session) throws IOException, GPPInternalErrorException, GPPTecnomenException, DOMException, SAXException, ParseException, ParserConfigurationException
	{
	    String xmlExtrato = ConsultaExtratoPulaPulaGPP.readFromFile(diretorios, sessionId);
	    if(xmlExtrato == null)
	    {
	        return ConsultaExtratoPulaPulaGPP.getExtratos(msisdn, dataInicial, dataFinal, servidor, porta, diretorios, sessionId, consultaCheia, session);
	    }

	    return ConsultaExtratoPulaPulaGPP.parse(xmlExtrato, session);
	}



	/**
	 * Interpreta o XML do extrato Pula-Pula.
	 *
	 * @param		String					xmlExtrato					XML contendo o extrato Pula-Pula do assinante.
	 * @param		boolean					consultaCheia				Indica se a consulta será cheia.
	 * @return		RetornoExtrato			ret							Objeto contendo os informacoes do extrato Pula-Pula.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	public static RetornoExtratoPulaPula parse(String xmlExtrato, Session session) throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException
	{
		RetornoExtratoPulaPula ret = new RetornoExtratoPulaPula();
		DecimalFormat conversorDouble = new DecimalFormat(Constantes.DOUBLE_FORMATO, new DecimalFormatSymbols(new Locale("pt", "BR", "")));

		// Busca uma instancia de um DocumentBuilder
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		// Cria um parse de XML
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		// Carrega o XML informado dentro e um InputSource
		InputSource is = new InputSource(new StringReader(xmlExtrato));
		// Faz o parse do XML
		Document doc = parse.parse(is);

		//Obtendo o nome da promocao
		Element elmGppExtrato		= (Element)doc.getElementsByTagName("GPPExtratoPulaPula").item(0);

		Element elmCabecalho = (Element)elmGppExtrato.getElementsByTagName("cabecalho").item(0);
		if((elmCabecalho != null) && (elmCabecalho.getChildNodes().getLength() > 0))
		{
			Element elmRetorno = (Element)elmCabecalho.getElementsByTagName("retorno").item(0);
			if((elmRetorno != null) && (elmRetorno.getChildNodes().getLength() > 0))
			{
				ret.setRetorno(elmRetorno.getChildNodes().item(0).getNodeValue());
			}

			Element elmPromocao = (Element)elmCabecalho.getElementsByTagName("promocao").item(0);
			if((elmPromocao != null) && (elmPromocao.getChildNodes().getLength() > 0))
			{
				ret.setPromocao(PromocaoDAO.findById(session, Integer.parseInt(elmPromocao.getChildNodes().item(0).getNodeValue())));
			}

			Element elmMesagem = (Element)elmCabecalho.getElementsByTagName("mensagem").item(0);
			if((elmMesagem != null) && (elmMesagem.getChildNodes().getLength() > 0))
			{
				ret.setMensagem(elmMesagem.getChildNodes().item(0).getNodeValue());
			}
		}

		Element elmDetalhes = (Element)elmGppExtrato.getElementsByTagName("detalhes").item(0);
		if((elmDetalhes != null) && (elmDetalhes.getChildNodes().getLength() > 0))
		{
//				Obtendo os detalhes de cada ligacao recebida.
			NodeList ndlstDetalhes = elmDetalhes.getElementsByTagName("detalhe");
			for(int i = 0; i < ndlstDetalhes.getLength(); i++)
			{
				ExtratoPulaPula extrato = new ExtratoPulaPula();
				Element elmDetalhe = (Element)ndlstDetalhes.item(i);

				//Indicador de Evento
				String idDesconto = elmDetalhe.getAttribute("idDesconto");
				if(idDesconto != null && !idDesconto.equals(""))
				{
					extrato.setDesconto(DescontoPulaPulaDao.findByIdDesconto(session, Short.parseShort(idDesconto)));
				}
				extrato.setIndEvento(Boolean.valueOf(elmDetalhe.getAttribute("indEvento")).booleanValue());

				//Originador.
				if((elmDetalhe.getElementsByTagName("originador").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("originador").item(0).getChildNodes().getLength() > 0))
				{
				    extrato.setOriginador(elmDetalhe.getElementsByTagName("originador").item(0).getChildNodes().item(0).getNodeValue());
				}
				//Timestamp.
				if((elmDetalhe.getElementsByTagName("timestamp").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("timestamp").item(0).getChildNodes().getLength() > 0))
				{
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
				    extrato.setTimestamp(formatter.parse(elmDetalhe.getElementsByTagName("timestamp").item(0).getChildNodes().item(0).getNodeValue()));
				}
				//Descrição.
				if((elmDetalhe.getElementsByTagName("descricao").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("descricao").item(0).getChildNodes().getLength() > 0))
				{
				    extrato.setDescricao(elmDetalhe.getElementsByTagName("descricao").item(0).getChildNodes().item(0).getNodeValue());
				}
				//Duração.
				if((elmDetalhe.getElementsByTagName("duracao").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("duracao").item(0).getChildNodes().getLength() > 0))
				{
				    extrato.setDuracao(Integer.parseInt(elmDetalhe.getElementsByTagName("duracao").item(0).getChildNodes().item(0).getNodeValue()));
				}
				//Bônus.
				if((elmDetalhe.getElementsByTagName("bonus").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("bonus").item(0).getChildNodes().getLength() > 0))
				{
				    extrato.setBonus(Double.parseDouble(elmDetalhe.getElementsByTagName("bonus").item(0).getChildNodes().item(0).getNodeValue()));
				}
				ret.getExtratos().add(extrato);
			}
		}

		Element elmSumarizacao = (Element)elmGppExtrato.getElementsByTagName("sumarizacao").item(0);
		if((elmSumarizacao != null) && (elmSumarizacao.getChildNodes().getLength() > 0))
		{
			NodeList ndlstDetalhes = elmSumarizacao.getElementsByTagName("detalhe");
			for(int i = 0; i < ndlstDetalhes.getLength(); i++)
			{
				ExtratoPulaPula extrato = new ExtratoPulaPula();
				Element elmDetalhe = (Element)ndlstDetalhes.item(i);
				extrato.setDesconto(DescontoPulaPulaDao.findByIdDesconto(session, Short.parseShort(elmDetalhe.getAttribute("idDesconto"))));
//					Duração.
				if((elmDetalhe.getElementsByTagName("duracao").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("duracao").item(0).getChildNodes().getLength() > 0))
				{
				    extrato.setDuracao(Integer.parseInt(elmDetalhe.getElementsByTagName("duracao").item(0).getChildNodes().item(0).getNodeValue()));
				}
				//Bônus.
				if((elmDetalhe.getElementsByTagName("bonus").getLength() > 0) &&
				   (elmDetalhe.getElementsByTagName("bonus").item(0).getChildNodes().getLength() > 0))
				{
				    extrato.setBonus(Double.parseDouble(elmDetalhe.getElementsByTagName("bonus").item(0).getChildNodes().item(0).getNodeValue()));
				}
				ret.getSumarizacao().add(extrato);
			}
		}
		// Descomentar caso seja incluido as 12 futuras bonificacoes no extrato.
		/*
		Element elmTotalizacaoFutura = (Element)elmGppExtrato.getElementsByTagName("totalizacaoFutura").item(0);
		if((elmTotalizacaoFutura != null) && (elmTotalizacaoFutura.getChildNodes().getLength() > 0))
		{
			Element elmTotalizacaoFuturaInt = (Element)elmTotalizacaoFutura.getElementsByTagName("totalizacao").item(0);
			if((elmTotalizacaoFuturaInt != null) && (elmTotalizacaoFuturaInt.getChildNodes().getLength() > 0))
			{
				ret.setTotalBonusAgendado(conversorDouble.format(Double.parseDouble(elmTotalizacaoFuturaInt.getAttribute("valorTotal"))));
				ret.setDataInicioBonificacao(elmTotalizacaoFuturaInt.getAttribute("inicioBonificacao"));
				NodeList lstVlrBonus = elmTotalizacaoFuturaInt.getElementsByTagName("valorBonus");
				for(int i = 0; i<lstVlrBonus.getLength(); i++ )
				{
					Element elmVlrBonus = (Element)lstVlrBonus.item(i);
					if((elmVlrBonus != null) && (elmVlrBonus.getChildNodes().getLength() > 0))
					{
						BonusAgendado bonificacaoFutura = new BonusAgendado();
						bonificacaoFutura.setDataExecucaoAgendada(elmVlrBonus.getAttribute("mesAnalise"));
						bonificacaoFutura.setValorAgendado(conversorDouble.format(Double.parseDouble(elmVlrBonus.getChildNodes().item(0).getNodeValue())));
						
						ret.getBonusFuturos().add(bonificacaoFutura);
					}
				}
			}
		}
		*/
//			Obtem o elemento referente as informacoes do bonus Pula-Pula a ser concedido ao assinante.
		Element elmSaldo = (Element)elmGppExtrato.getElementsByTagName("saldo").item(0);
		if((elmSaldo != null) && (elmSaldo.getChildNodes().getLength() > 0))
		{
		    //Obtem o valor total de bonus Pula-Pula a ser concedido ao assinante no periodo.
			Element elmValorTotal = (Element)elmSaldo.getElementsByTagName("valorTotal").item(0);
			if((elmValorTotal != null) && (elmValorTotal.getChildNodes().getLength() > 0))
			{
			    double valorTotal = Double.parseDouble(elmValorTotal.getChildNodes().item(0).getNodeValue());
				ret.setTotalBonus(conversorDouble.format(valorTotal));
			}

			//Obtem o valor referente a concessao parcial ja concedido ao assinante.
			Element elmValorParcial = (Element)elmSaldo.getElementsByTagName("valorParcial").item(0);
			if((elmValorParcial != null) && (elmValorParcial.getChildNodes().getLength() > 0))
			{
			    double valorParcial = Double.parseDouble(elmValorParcial.getChildNodes().item(0).getNodeValue());
				ret.setTotalParcial(conversorDouble.format(valorParcial));
			}

			//Obtem o valor remanescente a ser concedido ao assinante no periodo. Este valor e calculado
			//como sendo: valorAReceber = valorTotal - valorParcial.
			Element elmValorAReceber = (Element)elmSaldo.getElementsByTagName("valorAReceber").item(0);
			if((elmValorAReceber != null) && (elmValorAReceber.getChildNodes().getLength() > 0))
			{
			    double valorAReceber = Double.parseDouble(elmValorAReceber.getChildNodes().item(0).getNodeValue());
				ret.setTotalAReceber(conversorDouble.format(valorAReceber));
			}

			Element elmBonificacoes = (Element)elmSaldo.getElementsByTagName("bonificacoes").item(0);
			if((elmBonificacoes !=  null) && (elmBonificacoes.getChildNodes().getLength() >0))
			{
				NodeList lstBonificacoes = elmBonificacoes.getElementsByTagName("bonificacao");
				for(int i = 0; i<lstBonificacoes.getLength(); i++ )
				{
					Element elmBonificacao = (Element)lstBonificacoes.item(i);
					if((elmBonificacao != null)&&(elmBonificacao.getChildNodes().getLength()>0))
					{
						PromocaoTipoBonificacao tipoBonificacao = PromocaoTipoBonificacaoDao.findById(session, Short.parseShort(elmBonificacao.getAttribute("tipoBonificacao")));
						if(tipoBonificacao.getIdTipoBonificacao() != 0)
						{
							BonificacaoPulaPula bonificacao = new BonificacaoPulaPula(tipoBonificacao);

							Element elmBonValorTotal = (Element)elmBonificacao.getElementsByTagName("valorTotal").item(0);
							if((elmBonValorTotal != null)&&(elmBonValorTotal.getChildNodes().getLength()>0))
							{
								double valorBonTotal = Double.parseDouble(elmBonValorTotal.getChildNodes().item(0).getNodeValue());
								bonificacao.setValorBruto(valorBonTotal);
							}

							Element elmBonValorParcial = (Element)elmBonificacao.getElementsByTagName("valorParcial").item(0);
							if((elmBonValorParcial != null)&&(elmBonValorParcial.getChildNodes().getLength()>0))
							{
								double valorBonParcial = Double.parseDouble(elmBonValorParcial.getChildNodes().item(0).getNodeValue());
								bonificacao.setValorParcial(valorBonParcial);
							}

							ret.getBonificacoes().add(bonificacao);
						}
					}
				}
			}
		}
		
		return ret;
	}

	/**
	 * Le o XML do extrato Pula-Pula retornado pelo GPP no arquivo criado na sessao.
	 *
	 * @param		Collection				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas
	 *																	de diretorios temporarios em cada maquina
	 *																	rodando o IAS e o GPP.
	 * @param		String					sessionId					Identificador da sessao do usuario.
	 * @return		String					result						XML com as informacoes do extrato Pula-Pula do assinante.
	 * @throws IOException 
	 */
	private static String readFromFile(Collection diretorios, String sessionId) throws IOException
	{
	    StringBuffer result = new StringBuffer();

        //Verificando na lista de diretorios se o arquivo pode ser encontrado.
        for(Iterator iterator = diretorios.iterator(); iterator.hasNext();)
        {
	        String fileName = (String)iterator.next() + File.separator + sessionId;
	        File fileExtrato = new File(fileName);

	        if(fileExtrato.exists())
	        {
	            //Lendo o XML do arquivo.
	            FileReader reader = new FileReader(fileExtrato);
	            char[] buffer = new char[new Long(fileExtrato.length()).intValue()];

	            while((reader.read(buffer)) != -1)
	            {
	                result.append(buffer);
	            }

	            reader.close();
	            break;
	        }
        }

	    return (result.length() > 0) ? result.toString() : null;
	}

	/**
	 *	Grava o XML do extrato Pula-Pula retornado pelo GPP em arquivo.
	 *
	 *	@param		String					xmlExtrato					XML com as informacoes do extrato Pula-Pula do assinante.
	 *	@param		Collection				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas
	 *																	de diretorios temporarios em cada maquina
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao do usuario.
	 */
	private static void saveToFile(String xmlExtrato, Collection diretorios, String sessionId)
	{
	    try
	    {
	        //Percorrendo a lista de diretorios e verificando se existe algum valido.
	        for(Iterator iterator = diretorios.iterator(); iterator.hasNext();)
	        {
	            String nomeDiretorio = (String)iterator.next();
	            File diretorio = new File(nomeDiretorio);
	            if((diretorio.exists()) && (diretorio.isDirectory()))
	            {
	    	        //Salvando o xml em arquivo
	    	        String fileName = nomeDiretorio + File.separator + sessionId;
	    	        File fileExtrato = new File(fileName);
	    	        FileWriter writer = new FileWriter(fileExtrato);
	    	        writer.write(xmlExtrato);
	    	        writer.close();
	    	        break;
	            }
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	}

}
