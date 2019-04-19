package com.brt.gpp.aplicacoes.promocao.parser;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimite;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pela conversao em XML da consulta da promocao Pula-Pula.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		25/03/2008
 *	@modify		Primeira versao.
 */
public abstract class PromocaoPulaPulaXMLParser 
{

	/**
	 *	Cria e retorna o XML da consulta da promocao Pula-Pula em formato String.
	 *	
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia da concessao. Corresponde a data cujo mes acontece 
	 *										a concessao do bonus ao assinante.
	 *	@param		validacao				Codigo de retorno da validacao do assinante.
	 *	@return		Objeto para escrita do XML em String.
	 */
	public static String format(String msisdn, AssinantePulaPula pAssinante, Date dataReferencia, short validacao)
	{
		try
		{
			StringWriter writer = new StringWriter();
			PromocaoPulaPulaXMLParser.write(msisdn, pAssinante, dataReferencia, validacao, writer);
			
			return writer.toString();
		}
		catch(Exception e)
		{
			return e.toString();
		}
	}
	
	/**
	 *	Escreve o XML da consulta da promocao Pula-Pula no output.
	 *	
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia da concessao. Corresponde a data cujo mes acontece 
	 *										a concessao do bonus ao assinante.
	 *	@param		validacao				Codigo de retorno da validacao do assinante.
	 *	@param		writer					Output para escrita do XML.
	 *	@throws		ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException 
	 */
	public static void write(String				msisdn, 
							 AssinantePulaPula	pAssinante, 
							 Date				dataReferencia, 
							 short				validacao,
							 StringWriter		writer) throws ParserConfigurationException, 
							 								   TransformerFactoryConfigurationError, 
							 								   TransformerException
	{
		Document		document	= PromocaoPulaPulaXMLParser.newDocument();
		Element			elmRoot		= PromocaoPulaPulaXMLParser.toElement(msisdn, 
																		  pAssinante, 
																		  dataReferencia, 
																		  validacao, 
																		  document);		
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
	 *	Retorna o XML resultante da consulta da promocao Pula-Pula do assinante.
	 *	
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia da concessao. Corresponde a data cujo mes acontece 
	 *										a concessao do bonus ao assinante.
	 *	@param		validacao				Codigo de retorno da validacao do assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		XML resultante da consulta da promocao Pula-Pula do assinante.
	 */
	public static Element toElement(String				msisdn, 
									AssinantePulaPula	pAssinante, 
									Date				dataReferencia,
									short				validacao,
									Document			document)
	{
		//Criando o elemento root.
		Element result = document.createElement("GPPConsultaPromocaoPulaPula");
        //Criando o elemento referente ao codigo de retorno. Caso o assinante exista e nao tenha havido erro
        //tecnico, o retorno da operacao deve ser OK.
		Element elmRetorno = document.createElement("retorno");
		result.appendChild(elmRetorno);
		DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
        String retorno = ((validacao != Definicoes.RET_ERRO_TECNICO                 ) && 
        				  (validacao != Definicoes.RET_MSISDN_NAO_ATIVO             ) &&
        				  (validacao != Definicoes.RET_PROMOCAO_NAO_EXISTE          ) &&
        				  (validacao != Definicoes.RET_HIBRIDO_PROMOCAO_ANTIGA      ) &&
        				  (validacao != Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE) &&
        				  (validacao != Definicoes.RET_PROMOCAO_PENDENTE_RECARGA    ) &&
        				  (validacao != Definicoes.RET_PROMOCAO_ASS_BLOQ_CONSULTA   )) ?
        					conversorRetorno.format(Definicoes.RET_OPERACAO_OK) : conversorRetorno.format(validacao);
        elmRetorno.appendChild(document.createTextNode(retorno));
        //Criando o elemento referente ao MSISDN do assinante.
        Element elmMsisdn = document.createElement("msisdn");
        result.appendChild(elmMsisdn);
        elmMsisdn.appendChild(document.createTextNode(msisdn));
        //Criando o elemento referente a promocao Pula-Pula do assinante.
        result.appendChild(PromocaoPulaPulaXMLParser.criarPromocao(pAssinante, dataReferencia, validacao, document));
        
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes da promocao Pula-Pula do assinante.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia da concessao. Corresponde a data cujo mes acontece 
	 *										a concessao do bonus ao assinante.
	 *	@param		validacao				Codigo de retorno da validacao do assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes da totalizacao Pula-Pula do assinante.
	 */
	private static Element criarPromocao(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia, 
										 short				validacao, 
										 Document			document)
	{
		Element	result	= document.createElement("promocoes");
		
		if(pAssinante != null)
		{
			//Criando o elemento referente a promocao.
			Element elmPromocao = document.createElement("promocao");
			result.appendChild(elmPromocao);
			//Criando o elemento referente ao retorno da validacao.
			Element elmRetornoValidacao = document.createElement("retornoValidacao");
			elmPromocao.appendChild(elmRetornoValidacao);
			String retornoValidacao = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO).format(validacao);
			elmRetornoValidacao.appendChild(document.createTextNode(retornoValidacao));
			//Criando o elemento referente ao identificador da promocao.
			Element elmIdentificador = document.createElement("identificador");
			elmPromocao.appendChild(elmIdentificador);
			String identificador = String.valueOf(pAssinante.getPromocao().getIdtPromocao());
			elmIdentificador.appendChild(document.createTextNode(identificador));
			//Criando o elemento referente ao nome da promocao.
			Element elmNome = document.createElement("nome");
			elmPromocao.appendChild(elmNome);
			String nome = pAssinante.getPromocao().getNomPromocao();
			elmNome.appendChild(document.createTextNode(nome));
			//Criando o elemento referente ao identificador categoria da da promocao.
			Element elmCategoria = document.createElement("categoria");
			elmPromocao.appendChild(elmCategoria);
			String categoria = String.valueOf(pAssinante.getPromocao().getCategoria().getIdtCategoria());
			elmCategoria.appendChild(document.createTextNode(categoria));
			//Criando o elemento referente a data de execucao.
			Element elmDataExecucao = document.createElement("dataExecucao");
			elmPromocao.appendChild(elmDataExecucao);
			String dataExecucao = pAssinante.format(PromocaoAssinante.DAT_EXECUCAO);
			elmDataExecucao.appendChild(document.createTextNode(dataExecucao));
			//Criando o elemento referente a data de credito.
			Element elmDataCredito = document.createElement("dataCredito");
			elmPromocao.appendChild(elmDataCredito);
			String dataCredito = pAssinante.format(AssinantePulaPula.DATA_CREDITO);
			elmDataCredito.appendChild(document.createTextNode(dataCredito));
			//Criando o elemento referente a data de entrada na promocao.
			Element elmDataEntrada = document.createElement("dataEntrada");
			elmPromocao.appendChild(elmDataEntrada);
			String dataEntrada = pAssinante.format(PromocaoAssinante.DAT_ENTRADA_PROMOCAO);
			elmDataEntrada.appendChild(document.createTextNode(dataEntrada));
			//Criando o elemento referente a data de credito.
			Element elmDataInicioAnalise = document.createElement("dataInicioAnalise");
			elmPromocao.appendChild(elmDataInicioAnalise);
			String dataInicioAnalise = pAssinante.format(PromocaoAssinante.DAT_INICIO_ANALISE);
			elmDataInicioAnalise.appendChild(document.createTextNode(dataInicioAnalise));
			//Criando o elemento referente a data final de analise.
			Element elmDataFimAnalise = document.createElement("dataFimAnalise");
			elmPromocao.appendChild(elmDataFimAnalise);
			String dataFimAnalise = pAssinante.format(PromocaoAssinante.DAT_FIM_ANALISE);
			elmDataFimAnalise.appendChild(document.createTextNode(dataFimAnalise));

			//Obtendo as informacoes referentes ao limite dinamico da promocao. A informacao depende do mes de 
			//analise, que corresponde ao mes anterior a data de referencia.
			Calendar calAnalise = Calendar.getInstance();
			calAnalise.setTime(dataReferencia);
			calAnalise.add(Calendar.MONTH, -1);
			
			//Criando o elemento referente ao flag de limite dinamico.
			Element elmLimiteDinamico = document.createElement("limiteDinamico");
			elmPromocao.appendChild(elmLimiteDinamico);
			String limiteDinamico = String.valueOf(pAssinante.temLimiteDinamico(calAnalise.getTime()));
			elmLimiteDinamico.appendChild(document.createTextNode(limiteDinamico));
			//Criando o elemento referente ao status da promocao do assinante.
			Element elmStatus = document.createElement("status");
			elmPromocao.appendChild(elmStatus);
			String status = String.valueOf(pAssinante.getStatus().getIdtStatus());
			elmStatus.appendChild(document.createTextNode(status));
			//Adicionando os elementos referentes a totalizacao FGN.
			List elementosFGN = PromocaoPulaPulaXMLParser.criarTotalizacaoFGN(pAssinante, document);
			for(Iterator iterator = elementosFGN.iterator(); iterator.hasNext();)
				elmPromocao.appendChild((Element)iterator.next());
	        //Criando o elemento referente a totalizacao Pula-Pula do assinante.
			elmPromocao.appendChild(PromocaoPulaPulaXMLParser.criarTotalizacaoPulaPula(pAssinante, document));
	        //Criando o elemento referente ao bonus Pula-Pula do assinante.
	        elmPromocao.appendChild(PromocaoPulaPulaXMLParser.criarSaldoPulaPula(pAssinante, dataReferencia, document));
	        //Criando o elemento referente aos bonus Pula-Pula agendados do assinante.
			elmPromocao.appendChild(PromocaoPulaPulaXMLParser.criarBonusAgendados(pAssinante, document));
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes da totalizacao FGN do assinante.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes da totalizacao FGN do assinante.
	 */
	private static List criarTotalizacaoFGN(AssinantePulaPula pAssinante, Document document)
	{
		ArrayList result = new ArrayList();
		
		String	segundosGastos		= "";
    	String	limiteSegundos		= "";
		String	diaVencimento		= "";
    	String	dataLimiteAlcancado	= "";
		
		//Obtendo as informacoes da promocao FGN do assinante, caso exista.
    	PromocaoDiaExecucao diaExecucao = 
    		pAssinante.getDiaExecucao(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_FALEGRATIS);
    	if(diaExecucao != null)
    	{
    		//Dia de vencimento.
    		diaVencimento = diaExecucao.getNumDiaExecucao().toString();
    		
    		//Segundos gastos e data em que o limite foi alcancado, caso tenha sido.
        	TotalizacaoFaleGratis totalizacao = pAssinante.getTotalizacaoFGN();
        	if(totalizacao != null)
        	{
            	segundosGastos = String.valueOf(totalizacao.getNumSegundos());
            	
            	if(totalizacao.getDatRetiradaFGN() != null)
            	{
            		SimpleDateFormat conversorTimestamp = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
            		dataLimiteAlcancado = conversorTimestamp.format(totalizacao.getDatRetiradaFGN());
            	}
            	
            	//Limite da utilizacao do FGN em segundos.
            	PromocaoLimiteSegundos limiteMaximo = pAssinante.getPromocao().getLimiteSegundosMaximo(); 
            	if(limiteMaximo != null)
            		limiteSegundos = String.valueOf(limiteMaximo.getNumSegundos());
        	}
    	}
    	
		//Criando o elemento referente ao segundos de FGN gastos pelo assinante.
		Element elmSegundosGastos = document.createElement("segundosGastos");
		result.add(elmSegundosGastos);
		elmSegundosGastos.appendChild(document.createTextNode(segundosGastos));
		//Criando o elemento referente ao limite de segundos de utilizacao do FGN para o assinante.
		Element elmLimiteSegundos = document.createElement("limiteSegundos");
		result.add(elmLimiteSegundos);
		elmLimiteSegundos.appendChild(document.createTextNode(limiteSegundos));
		//Criando o elemento referente ao dia de vencimento da promocao FGN do assinante.
		Element elmDiaVencimento = document.createElement("diaVencimento");
		result.add(elmDiaVencimento);
		elmDiaVencimento.appendChild(document.createTextNode(diaVencimento));
		//Criando o elemento referente a data em que o limite de utilizacao do FGN do assinante foi alcancado.
		Element elmDataLimiteAlcancado = document.createElement("dataLimiteAlcancado");
		result.add(elmDataLimiteAlcancado);
		elmDataLimiteAlcancado.appendChild(document.createTextNode(dataLimiteAlcancado));
    	
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes da totalizacao Pula-Pula do assinante.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes da totalizacao Pula-Pula do assinante.
	 */
	private static Element criarTotalizacaoPulaPula(AssinantePulaPula pAssinante, Document document)
	{
		Element				result		= document.createElement("totalizacao");
		TotalizacaoPulaPula	totalizacao = pAssinante.getTotalizacao();
		
		if(totalizacao != null)
		{
			//Criando o elemento referente ao recebimento total de ligacoes.
			Element elmRecebimentoTotal = document.createElement("recebimentoTotal");
			result.appendChild(elmRecebimentoTotal);
			String recebimentoTotal = totalizacao.toString(TotalizacaoPulaPula.NUM_SEGUNDOS_TOTAL);
			elmRecebimentoTotal.appendChild(document.createTextNode(recebimentoTotal));
			//Criando o elemento referente ao recebimento de ligacoes On-Net.
			Element elmRecebimentoOnNet = document.createElement("recebimentoOnNet");
			result.appendChild(elmRecebimentoOnNet);
			String recebimentoOnNet = totalizacao.toString(TotalizacaoPulaPula.NUM_SEGUNDOS_ONNET);
			elmRecebimentoOnNet.appendChild(document.createTextNode(recebimentoOnNet));
			//Criando o elemento referente ao recebimento de ligacoes Off-Net.
			Element elmRecebimentoOffNet = document.createElement("recebimentoOffNet");
			result.appendChild(elmRecebimentoOffNet);
			String recebimentoOffNet = totalizacao.toString(TotalizacaoPulaPula.NUM_SEGUNDOS_OFFNET);
			elmRecebimentoOffNet.appendChild(document.createTextNode(recebimentoOffNet));
			//Criando o elemento referente ao recebimento de ligacoes com bonus retirado.
			Element elmRecebimentoBonusRetirado = document.createElement("recebimentoBonusRetirado");
			result.appendChild(elmRecebimentoBonusRetirado);
			String recebimentoBonusRetirado = totalizacao.toString(TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS_RETIRADO);
			elmRecebimentoBonusRetirado.appendChild(document.createTextNode(recebimentoBonusRetirado));
			//Criando o elemento referente as recargas efetuadas.
			result.appendChild(PromocaoPulaPulaXMLParser.criarTotalizacaoRecargas(pAssinante, document));
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes da totalizacao de recargas efetuadas pelo assinante.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes da totalizacao de recargas efetuadas pelo assinante.
	 */
	private static Element criarTotalizacaoRecargas(AssinantePulaPula pAssinante, Document document)
	{
		Element				result			= document.createElement("recargasEfetuadas");
		TotalizacaoRecargas	totalRecargas	= pAssinante.getTotalRecargas();
		
		if(totalRecargas != null)
		{
			//Criando o elemento referente as recargas efetuadas.
			String recargasEfetuadas = totalRecargas.toString(TotalizacaoRecargas.VLR_PAGO);
			result.appendChild(document.createTextNode(recargasEfetuadas));
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes do bonus Pula-Pula a ser concedido ao assinante.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia da concessao. Corresponde a data cujo mes acontece 
	 *										a concessao do bonus ao assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes do bonus Pula-Pula a ser concedido ao assinante.
	 */
	private static Element criarSaldoPulaPula(AssinantePulaPula pAssinante, Date dataReferencia, Document document)
	{
		Element			result	= document.createElement("saldo");
		SaldoPulaPula	saldo	= pAssinante.getSaldo();
		
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
			result.appendChild(PromocaoPulaPulaXMLParser.criarBonificacoes(pAssinante, dataReferencia, document));
		}
		
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes das bonificacoes Pula-Pula a serem concedidas ao assinante.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia da concessao. Corresponde a data cujo mes acontece 
	 *										a concessao do bonus ao assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes das bonificacoes Pula-Pula a serem concedidas ao assinante.
	 */
	private static Element criarBonificacoes(AssinantePulaPula pAssinante, Date dataReferencia, Document document)
	{
		Element		result			= document.createElement("bonificacoes");
		Collection	bonificacoes	= pAssinante.getSaldo().getBonificacoes();
		
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

			//Obtendo as informacoes referentes ao limite da promocao. A informacao depende do mes de analise, que 
			//corresponde ao mes anterior a data de referencia.
			Calendar calAnalise = Calendar.getInstance();
			calAnalise.setTime(dataReferencia);
			calAnalise.add(Calendar.MONTH, -1);
			
			//Criando o elemento referente ao flag de permissao de isencao de limite da bonificacao pela promocao.
			Element elmPermiteIsencao = document.createElement("permiteIsencao");
			elmBonificacao.appendChild(elmPermiteIsencao);
			PromocaoLimite pLimite = pAssinante.getLimite(bonificacao.getTipoBonificacao(), calAnalise.getTime());
			boolean permiteIsencao = (pLimite != null) ? pLimite.permiteIsencaoLimite() : true;
			elmPermiteIsencao.appendChild(document.createTextNode(String.valueOf(permiteIsencao)));
			//Criando o elemento referente ao flag de insencao de limite da bonificacao.
			Element elmIsentoLimite = document.createElement("isentoLimite");
			elmBonificacao.appendChild(elmIsentoLimite);
			boolean isentoLimite = pAssinante.isIsentoLimite(bonificacao.getTipoBonificacao(), calAnalise.getTime());
			elmIsentoLimite.appendChild(document.createTextNode(String.valueOf(isentoLimite)));
			//Criando o elemento referente ao flag de limite da bonificacao ultrapassado.
			Element elmLimiteUltrapassado = document.createElement("limiteUltrapassado");
			elmBonificacao.appendChild(elmLimiteUltrapassado);
			boolean isLimiteUltrapassado = bonificacao.isLimiteUltrapassado();
			elmLimiteUltrapassado.appendChild(document.createTextNode(String.valueOf(isLimiteUltrapassado)));
			
			result.appendChild(elmBonificacao);
		}
			
		return result;
	}
	
	/**
	 *	Cria e retorna o no do XML com as informacoes dos bonus do assinante agendados na Fila de Recargas.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		document				Documento XML com o resultado da formatacao (DOM).
	 *	@return		No do XML com as informacoes dos bonus agendados.
	 */
	private static Element criarBonusAgendados(AssinantePulaPula pAssinante, Document document)
	{
		Element		result			= document.createElement("bonusAgendados");
		Collection	bonusAgendados	= pAssinante.getBonusAgendados();

		for(Iterator iterator = bonusAgendados.iterator(); iterator.hasNext();)
		{
			FilaRecargas	bonusAgendado	= (FilaRecargas)iterator.next();
			Element			elmBonus		= document.createElement("bonus");
			
			//Insere o elemento referente ao tipo de transacao.
			Element elmTipoTransacao = document.createElement("tipoTransacao");
			elmBonus.appendChild(elmTipoTransacao);
			String tipoTransacao = bonusAgendado.getTipTransacao();
			elmTipoTransacao.appendChild(document.createTextNode(tipoTransacao));
			//Insere o elemento referente ao valor.
			Element elmValor = document.createElement("valor");
			elmBonus.appendChild(elmValor);
			double	valorCreditoBonus		= (bonusAgendado.getVlrCreditoBonus() != null) ?
												bonusAgendado.getVlrCreditoBonus().doubleValue() : 0.0;
			double	valorCreditoPeriodico	= (bonusAgendado.getVlrCreditoPeriodico() != null) ?
												bonusAgendado.getVlrCreditoPeriodico().doubleValue() : 0.0;
			String valor = String.valueOf(valorCreditoBonus + valorCreditoPeriodico);
			elmValor.appendChild(document.createTextNode(valor));
			//Insere o elemento referente a data de execucao.
			Element elmDataExecucao = document.createElement("dataExecucao");
			elmBonus.appendChild(elmDataExecucao);
			String dataExecucao = "";
			if(bonusAgendado.getDatExecucao() != null)
				dataExecucao = new SimpleDateFormat(Definicoes.MASCARA_DATE).format(bonusAgendado.getDatExecucao());
			elmDataExecucao.appendChild(document.createTextNode(dataExecucao));
			
			result.appendChild(elmBonus);
		}
		
		return result;
	}
	
}