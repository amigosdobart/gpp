package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.PromocaoTipoBonificacaoDao;
import br.com.brasiltelecom.ppp.model.BonusAgendado;
import br.com.brasiltelecom.ppp.model.RetornoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Efetua a conexão com o GPP a fim de obter
 * os dados relativos a Promocao do cliente
 *
 * @author João Paulo Galvagni
 * @since 14/10/2005
 */
public class ConsultaPromocaoPulaPulaGPP
{

	/**
	 * Obtém o XML de promocao vindo do GPP
	 *
	 * @param msisdn msisdn
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return código XML
	 * @throws GPPInternalErrorException 
	 */
	public static String getXml(String msisdn, String mes, String servidor, String porta) throws GPPInternalErrorException
	{
		String ret = "";

		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		ret = pPOA.consultaPulaPulaNoMes(msisdn, mes);

		return ret;
	}

	/**
	 * Obtém o XML de promocao vindo do GPP sem passar o parametro mes
	 *
	 * @param msisdn msisdn
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return código XML
	 * @throws GPPInternalErrorException 
	 */
	public static String getXml(String msisdn, String servidor, String porta) throws GPPInternalErrorException
	{
		String ret = "";

		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		ret = pPOA.consultaPulaPula(msisdn);

		return ret;
	}


	/**
	 *	Retorna o objeto RetornoPulaPula, contendo
	 *	os dados referentes à promocao do cliente, sem passar o parametro mes
	 *
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@return		RetornoPromocao										Promocao do assinante.
	 * @throws SAXException 
	 * @throws GPPInternalErrorException 
	 * @throws ParseException 
	 */
	public static RetornoPulaPula getPromocao(String msisdn, String servidor, String porta, Session session) throws SAXException, GPPInternalErrorException, ParseException
	{
		// Faz o get do XML para manipulacao
	    String xmlPulaPula = ConsultaPromocaoPulaPulaGPP.getXml(msisdn, servidor, porta);
	    
	    try
	    {
	    // Chama o metodo para efetuar o parse do XML, populando o objeto RetornoPulaPula
	    return ConsultaPromocaoPulaPulaGPP.parse(xmlPulaPula, session);
	    }
	    catch(ParserConfigurationException naoAcontece){}
	    catch(IOException naoAcontece){}
	    
	    return null;
	}

	/**
	 *	Retorna o objeto RetornoPulaPula, contendo
	 *	os dados referentes à promocao do cliente
	 *
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@return		RetornoPromocao										Promocao do assinante.
	 * @throws GPPInternalErrorException 
	 * @throws SAXException 
	 * @throws ParseException 
	 */
	public static RetornoPulaPula getPromocao(String msisdn, String mes, String servidor, String porta, Session session) throws GPPInternalErrorException, SAXException, ParseException
	{
		// Faz o get do XML para manipulacao
	    String xmlPulaPula = ConsultaPromocaoPulaPulaGPP.getXml(msisdn, mes, servidor, porta);
	    // Chama o metodo para efetuar o parse do XML, populando o objeto RetornoPulaPula
	    try
	    {
	    	return ConsultaPromocaoPulaPulaGPP.parse(xmlPulaPula, session);
	    }
	    catch(ParserConfigurationException naoAcontece){}
	    catch(IOException naoAcontece) {}
	    
	    return null;
	}

	/**
	 *	Interpreta o XML da promocao PulaPula.
	 *
	 * @param		String					xmlPulaPula					XML contendo as informacoes PulaPula do cliente.
	 * @return		RetornoPulaPula			result						Objeto contendo os informacoes do cliente.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParseException 
	 */
	private static RetornoPulaPula parse(String xmlPulaPula, Session session) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		RetornoPulaPula result = new RetornoPulaPula();
		DecimalFormat conversorDouble = new DecimalFormat(Constantes.DOUBLE_FORMATO, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
		SimpleDateFormat sdfmes = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdffuturo = new SimpleDateFormat("MM/yyyy");
			//Obtendo os objetos necessarios para a execucao do parse do xml
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlPulaPula));
			Document doc = parse.parse(is);

			//Pega o corpo do Elemento GPPConsultaPromocaoPulaPula.
			Element elmPulaPula = (Element)doc.getElementsByTagName("GPPConsultaPromocaoPulaPula").item(0);

			//Pega o codigo de retorno.
			Element elmRetorno = (Element)elmPulaPula.getElementsByTagName("retorno").item(0);
			if((elmRetorno != null) && (elmRetorno.getChildNodes().getLength() > 0))
			{
				result.setRetorno(elmRetorno.getChildNodes().item(0).getNodeValue());
			}

			//Pega o MSISDN.
			Element elmMsisdn = (Element)elmPulaPula.getElementsByTagName("msisdn").item(0);
			if((elmMsisdn != null) && (elmMsisdn.getChildNodes().getLength() > 0))
			{
				result.setMsisdn(elmMsisdn.getChildNodes().item(0).getNodeValue());
			}

			//Obtem o elemento referente a lista de Promocoes do assinante. No caso de promocoes Pula-Pula, deve
			//existir somente um no-filho <promocao/> no elemento <promocoes/>.
			Element elmPromocoes = (Element)elmPulaPula.getElementsByTagName("promocoes").item(0);
			if((elmPromocoes != null) && (elmPromocoes.getChildNodes().getLength() > 0))
			{
			    //Obtem o elemento contendo as informacoes referentes a promocao Pula-Pula do assinante.
				Element elmPromocao = (Element)elmPromocoes.getElementsByTagName("promocao").item(0);
				if((elmPromocao != null) && (elmPromocao.getChildNodes().getLength() > 0))
				{
				    //Obtem o codigo de retorno da validacao do assinante para recebimento do bonus.
				    Element elmRetornoValidacao = (Element)elmPromocao.getElementsByTagName("retornoValidacao").item(0);
				    if((elmRetornoValidacao != null) && (elmRetornoValidacao.getChildNodes().getLength() > 0))
				    {
				        result.setRetornoValidacao(elmRetornoValidacao.getChildNodes().item(0).getNodeValue());
				    }

				    //Obtem o identificador da promocao.
					Element elmIdentificador = (Element)elmPromocao.getElementsByTagName("identificador").item(0);
					if((elmIdentificador != null) && (elmIdentificador.getChildNodes().getLength() > 0))
					{
						result.setIdentificador(elmIdentificador.getChildNodes().item(0).getNodeValue());
					}

					//Obtem o nome da promocao
					Element elmNome = (Element)elmPromocao.getElementsByTagName("nome").item(0);
					if((elmNome != null) && (elmNome.getChildNodes().getLength() > 0))
					{
						result.setNome(elmNome.getChildNodes().item(0).getNodeValue());
					}

					//Obtem o identificador da categoria da promocao.
					Element elmCategoria = (Element)elmPromocao.getElementsByTagName("categoria").item(0);
					if((elmCategoria != null) && (elmCategoria.getChildNodes().getLength() > 0))
					{
						result.setCategoria(elmCategoria.getChildNodes().item(0).getNodeValue());
					}

					//Obtem a data de execucao do assinante pela promocao.
					Element elmDataExecucao = (Element)elmPromocao.getElementsByTagName("dataExecucao").item(0);
					if((elmDataExecucao != null) && (elmDataExecucao.getChildNodes().getLength() > 0))
					{
						result.setDataExecucao(elmDataExecucao.getChildNodes().item(0).getNodeValue());
					}

					//Obtem a data de consumo do bonus pela fila de recargas.
					Element elmDataCredito = (Element)elmPromocao.getElementsByTagName("dataCredito").item(0);
					if((elmDataCredito != null) && (elmDataCredito.getChildNodes().getLength() > 0))
					{
						result.setDataCredito(elmDataCredito.getChildNodes().item(0).getNodeValue());
					}

					//Obtem a data de entrada do assinante na promocao.
					Element elmDataEntrada = (Element)elmPromocao.getElementsByTagName("dataEntrada").item(0);
					if((elmDataEntrada != null) && (elmDataEntrada.getChildNodes().getLength() > 0))
					{
						result.setDataEntrada(elmDataEntrada.getChildNodes().item(0).getNodeValue());
					}

					//Obtem a data de inicio de analise. Nao utilizado pelas promocoes Pula-Pula.
					Element elmDataInicioAnalise = (Element)elmPromocao.getElementsByTagName("dataInicioAnalise").item(0);
					if((elmDataInicioAnalise != null) && (elmDataInicioAnalise.getChildNodes().getLength() > 0))
					{
						result.setDataInicioAnalise(elmDataInicioAnalise.getChildNodes().item(0).getNodeValue());
					}

					//Obtem a data de fim de analise. Nao utilizado pelas promocoes Pula-Pula.
					Element elmDataFimAnalise = (Element)elmPromocao.getElementsByTagName("dataFimAnalise").item(0);
					if((elmDataFimAnalise != null) && (elmDataFimAnalise.getChildNodes().getLength() > 0))
					{
						result.setDataFimAnalise(elmDataFimAnalise.getChildNodes().item(0).getNodeValue());
					}

					//Obtem o indicador de limite dinamico para a promocao do assinante.
					Element elmLimiteDinamico = (Element)elmPromocao.getElementsByTagName("limiteDinamico").item(0);
					if((elmLimiteDinamico != null) && (elmLimiteDinamico.getChildNodes().getLength() > 0))
					{
						result.setLimiteDinamico(elmLimiteDinamico.getChildNodes().item(0).getNodeValue());
					}

					// Obtem o status do assinante referente a pendendia de primeira recarga.
					Element elmStatus = (Element)elmPromocao.getElementsByTagName("status").item(0);
					if ((elmStatus != null) && (elmStatus.getChildNodes().getLength() > 0))
					{
						result.setStatus(elmStatus.getChildNodes().item(0).getNodeValue());
					}

					//Obtem o elemento referente as informacoes de ligacoes recebidas pelo assinante.
					Element elmTotalizacao = (Element)elmPromocao.getElementsByTagName("totalizacao").item(0);
					if((elmTotalizacao != null) && (elmTotalizacao.getChildNodes().getLength() > 0))
					{
					    //Obtem o numero total de segundos em ligacoes recebidos pelo assinante.
						Element elmRecebimentoTotal = (Element)elmTotalizacao.getElementsByTagName("recebimentoTotal").item(0);
						if((elmRecebimentoTotal != null) && (elmRecebimentoTotal.getChildNodes().getLength() > 0))
						{
						    long recebimentoTotal = Long.parseLong(elmRecebimentoTotal.getChildNodes().item(0).getNodeValue());
							result.setRecebimentoTotal(Util.segundosParaHoras(recebimentoTotal));
						}

						/*
						//Obtem o numero de segundos em ligacoes com tarifacao diferenciada Friends and Family
						//recebidos pelo assinante.
						Element elmRecebimentoFF = (Element)elmTotalizacao.getElementsByTagName("recebimentoFF").item(0);
						if((elmRecebimentoFF != null) && (elmRecebimentoFF.getChildNodes().getLength() > 0))
						{
						    long recebimentoFF = Long.parseLong(elmRecebimentoFF.getChildNodes().item(0).getNodeValue());
							result.setRecebimentoFF(Util.segundosParaHoras(recebimentoFF));
						}

						//Obtem o numero de segundos em ligacoes com tarifacao reduzida noturna
						//recebidos pelo assinante.
						Element elmRecebimentoNoturno = (Element)elmTotalizacao.getElementsByTagName("recebimentoNoturno").item(0);
						if((elmRecebimentoNoturno != null) && (elmRecebimentoNoturno.getChildNodes().getLength() > 0))
						{
						    long recebimentoNoturno = Long.parseLong(elmRecebimentoNoturno.getChildNodes().item(0).getNodeValue());
							result.setRecebimentoNoturno(Util.segundosParaHoras(recebimentoNoturno));
						}

						//Obtem o numero de segundos em ligacoes com tarifacao reduzida diurna
						//recebidos pelo assinante.
						Element elmRecebimentoDiurno = (Element)elmTotalizacao.getElementsByTagName("recebimentoDiurno").item(0);
						if((elmRecebimentoDiurno != null) && (elmRecebimentoDiurno.getChildNodes().getLength() > 0))
						{
						    long recebimentoDiurno = Long.parseLong(elmRecebimentoDiurno.getChildNodes().item(0).getNodeValue());
							result.setRecebimentoDiurno(Util.segundosParaHoras(recebimentoDiurno));
						}

						//Obtem o numero de segundos em ligacoes nao bonificaveis recebidos pelo assinante.
						Element elmRecebimentoNaoBonificado = (Element)elmTotalizacao.getElementsByTagName("recebimentoNaoBonificado").item(0);
						if((elmRecebimentoNaoBonificado != null) && (elmRecebimentoNaoBonificado.getChildNodes().getLength() > 0))
						{
						    long recebimentoNaoBonificado = Long.parseLong(elmRecebimentoNaoBonificado.getChildNodes().item(0).getNodeValue());
							result.setRecebimentoNaoBonificado(Util.segundosParaHoras(recebimentoNaoBonificado));
						}
						*/

						//Obtem o valor pago total de recargas efetuadas pelo assinante no periodo.
						Element elmRecargasEfetuadas = (Element)elmTotalizacao.getElementsByTagName("recargasEfetuadas").item(0);
						if((elmRecargasEfetuadas != null) && (elmRecargasEfetuadas.getChildNodes().getLength() > 0))
						{
						    double recargasEfetuadas = Double.parseDouble(elmRecargasEfetuadas.getChildNodes().item(0).getNodeValue());
							result.setRecargasEfetuadas(conversorDouble.format(recargasEfetuadas));
						}
					}
					
					Element elmTotalizacaoFutura = (Element)elmPromocao.getElementsByTagName("totalizacaoFutura").item(0);
					if((elmTotalizacaoFutura != null) && (elmTotalizacaoFutura.getChildNodes().getLength() > 0))
					{
						Element elmTotalizacaoFuturaInt = (Element)elmTotalizacaoFutura.getElementsByTagName("totalizacao").item(0);
						if((elmTotalizacaoFuturaInt != null) && (elmTotalizacaoFuturaInt.getChildNodes().getLength() > 0))
						{
							result.setValorTotalBonusAgendado(elmTotalizacaoFuturaInt.getAttribute("valorTotal"));
							result.setDataInicioBonificacao(elmTotalizacaoFuturaInt.getAttribute("inicioBonificacao"));
							NodeList lstVlrBonus = elmTotalizacaoFuturaInt.getElementsByTagName("valorBonus");
							for(int i = 0; i<lstVlrBonus.getLength(); i++ )
							{
								Element elmVlrBonus = (Element)lstVlrBonus.item(i);
								if((elmVlrBonus != null) && (elmVlrBonus.getChildNodes().getLength() > 0))
								{
									BonusAgendado bonificacaoFutura = new BonusAgendado();
									Calendar c = Calendar.getInstance();
									c.setTime(sdfmes.parse(elmVlrBonus.getAttribute("mesAnalise")));
									c.add(Calendar.MONTH, 1);
									bonificacaoFutura.setDataExecucaoAgendada(sdffuturo.format(c.getTime()));
									bonificacaoFutura.setValorAgendado(conversorDouble.format(Double.parseDouble(elmVlrBonus.getChildNodes().item(0).getNodeValue())));
									
									result.getBonusFuturos().add(bonificacaoFutura);
								}
							}
						}
					}

					//Obtem o elemento referente as informacoes do bonus Pula-Pula a ser concedido ao assinante.
					Element elmSaldo = (Element)elmPromocao.getElementsByTagName("saldo").item(0);
					if((elmSaldo != null) && (elmSaldo.getChildNodes().getLength() > 0))
					{
					    //Obtem o valor total de bonus Pula-Pula a ser concedido ao assinante no periodo.
						Element elmValorTotal = (Element)elmSaldo.getElementsByTagName("valorTotal").item(0);
						if((elmValorTotal != null) && (elmValorTotal.getChildNodes().getLength() > 0))
						{
						    double valorTotal = Double.parseDouble(elmValorTotal.getChildNodes().item(0).getNodeValue());
							result.setValorTotal(conversorDouble.format(valorTotal));
						}

						//Obtem o valor referente a concessao parcial ja concedido ao assinante.
						Element elmValorParcial = (Element)elmSaldo.getElementsByTagName("valorParcial").item(0);
						if((elmValorParcial != null) && (elmValorParcial.getChildNodes().getLength() > 0))
						{
						    double valorParcial = Double.parseDouble(elmValorParcial.getChildNodes().item(0).getNodeValue());
							result.setValorParcial(conversorDouble.format(valorParcial));
						}

						//Obtem o valor remanescente a ser concedido ao assinante no periodo. Este valor e calculado
						//como sendo: valorAReceber = valorTotal - valorParcial.
						Element elmValorAReceber = (Element)elmSaldo.getElementsByTagName("valorAReceber").item(0);
						if((elmValorAReceber != null) && (elmValorAReceber.getChildNodes().getLength() > 0))
						{
						    double valorAReceber = Double.parseDouble(elmValorAReceber.getChildNodes().item(0).getNodeValue());
							result.setValorAReceber(conversorDouble.format(valorAReceber));
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
										
										/* 
										//Obtem o indicador de que a promocao permite isencao de limite.
										Element elmPermiteIsencao = (Element)elmPromocao.getElementsByTagName("permiteIsencao").item(0);
										if((elmPermiteIsencao != null) && (elmPermiteIsencao.getChildNodes().getLength() > 0))
										{
											result.setPermiteIsencao(elmPermiteIsencao.getChildNodes().item(0).getNodeValue());
										}

										//Obtem o indicador de que o assinante e isento do limite da promocao.
										Element elmIsentoLimite = (Element)elmPromocao.getElementsByTagName("isentoLimite").item(0);
										if((elmIsentoLimite != null) && (elmIsentoLimite.getChildNodes().getLength() > 0))
										{
											result.setIsentoLimite(elmIsentoLimite.getChildNodes().item(0).getNodeValue());
										}
										
										//Obtem o limite final da promocao calculado para o assinante.
										Element elmLimite = (Element)elmSaldo.getElementsByTagName("limite").item(0);
										if((elmLimite != null) && (elmLimite.getChildNodes().getLength() > 0))
										{
										    double limite = Double.parseDouble(elmLimite.getChildNodes().item(0).getNodeValue());
											result.setLimite(conversorDouble.format(limite));
										}
				
										//Obtem o indicador de que o assinante ja atingiu o limite da promocao.
										Element elmSaturado = (Element)elmSaldo.getElementsByTagName("saturado").item(0);
										if((elmSaturado != null) && (elmSaturado.getChildNodes().getLength() > 0))
										{
											result.setSaturado(elmSaturado.getChildNodes().item(0).getNodeValue());
										}
										*/
										
										result.getBonificacoes().add(bonificacao);
									}
								}
							}
						}
					}

					//Obtem o elemento referente a lista de bonus agendados na fila de recargas para o assinante.
					//Normalmente, para as promocoes Pula-Pula temos uma lista de nós filhos <bonus/>.
					Element elmBonusAgendados = (Element)elmPromocao.getElementsByTagName("bonusAgendados").item(0);
					if((elmBonusAgendados != null) && (elmBonusAgendados.getChildNodes().getLength() > 0))
					{
						NodeList lstBonus = elmBonusAgendados.getElementsByTagName("bonus");
						for(int i = 0; i<lstBonus.getLength(); i++ )
						{
							Element elmBonus = (Element)lstBonus.item(i);
							if((elmBonus != null) && (elmBonus.getChildNodes().getLength() > 0))
							{
								BonusAgendado bonusAgendado = new BonusAgendado();
								
							    //Obtem a descricao do tipo de transacao do bonus agendado.
								Element elmTipoAgendado = (Element)elmBonus.getElementsByTagName("tipoTransacao").item(0);
								if((elmTipoAgendado != null) && (elmTipoAgendado.getChildNodes().getLength() > 0))
								{
									String tipTransacao = elmTipoAgendado.getChildNodes().item(0).getNodeValue();
									bonusAgendado.setOrigemRecarga(
											OrigemRecargaDAO.findById(session, tipTransacao.substring(0,2), tipTransacao.substring(2,5)));
								}
	
								//Obtem o valor do bonus agendado.
								Element elmValorAgendado = (Element)elmBonus.getElementsByTagName("valor").item(0);
								if((elmValorAgendado != null) && (elmValorAgendado.getChildNodes().getLength() > 0))
								{
								    double valorAgendado = Double.parseDouble(elmValorAgendado.getChildNodes().item(0).getNodeValue());
								    bonusAgendado.setValorAgendado(conversorDouble.format(valorAgendado));
								}
	
								//Obtem a data de execucao do bonus pelo processo de consumo da fila de recargas.
								Element elmDataExecucaoAgendada = (Element)elmBonus.getElementsByTagName("dataExecucao").item(0);
								if((elmDataExecucaoAgendada != null) && (elmDataExecucaoAgendada.getChildNodes().getLength() > 0))
								{
									bonusAgendado.setDataExecucaoAgendada(elmDataExecucaoAgendada.getChildNodes().item(0).getNodeValue());
								}
								
								result.getBonusAgendados().add(bonusAgendado);
							}
						}
					}
				}
			}


		//Retorna o objeto RetornoPulaPula.
		return result;
	}

	/**
	 * Obtém os créditos acumulados pelo cliente
	 *
	 * @param 	msisdn 		Msisdn
	 * @param	mes			Mês a ser consultado
	 * @param 	servidor 	Endereço do servidor
	 * @param 	porta 		Porta do servidor
	 * @return crédito acumulado
	 * @throws Exception
	 */
	public static double getCreditoPulaPula(String msisdn, String mes, String servidor, String porta) throws Exception
	{
		double ret = 0.0;

		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		ret = pPOA.consultarCreditoPulaPula(msisdn, mes);

		return ret;
	}
}