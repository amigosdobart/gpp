package br.com.brasiltelecom.ppp.interfacegpp;

//Imports Java.

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

//Imports WPP.

import br.com.brasiltelecom.ppp.model.EstornoPulaPula;
import br.com.brasiltelecom.ppp.model.RetornoEstornoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

//Imports GPP.

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 *	Efetua a conexao com o GPP a fim de obter a consulta de Estorno de Bonus Pula-Pula por Fraude.
 * 
 *	@author Daniel Ferreira
 *	@since	07/03/2006
 */
public class ConsultaEstornoPulaPulaGPP 
{

	/**
	 *	Obtem o XML da consulta de estorno vindo do GPP.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial de pesquisa.
	 *	@param		String					dataFinal					Data final de pesquisa.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@return		String												XML com o extrato Pula-Pula do assinante.
	 *	@throws		Exception
	 */
	public static String getXml(String msisdn, String dataInicial, String dataFinal, String servidor, String porta) throws Exception
	{
		String ret = "";

		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		try
		{
			ret = pPOA.consultaEstornoPulaPula(msisdn, dataInicial, dataFinal);
		}
		catch (Exception e) 
		{
			throw new Exception("Erro durante a consulta de Estorno de Bonus Pula-Pula por Fraude.");
		}
				
		return ret;
	}
	
	/**
	 *	Retorna a consulta de Estorno de Bonus Pula-Pula por Fraude.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial da consulta.
	 *	@param		String					dataFinal					Data final da consulta.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@return		RetornoEstornoPulaPula								Consulta de Estorno de Bonus Pula-Pula do assinante.
	 *	@throws Exception
	 */
	public static RetornoEstornoPulaPula getEstornos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta) throws Exception
	{
	    String xmlEstorno = ConsultaEstornoPulaPulaGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta);
	    return ConsultaEstornoPulaPulaGPP.parse(xmlEstorno);
	}
			
	/**
	 *	Retorna a consulta de estorno Pula-Pula, escrevendo-a em arquivo para paginacao.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial da consulta.
	 *	@param		String					dataFinal					Data final da consulta.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao.
	 *	@return		RetornoEstornoPulaPula								Consulta de Estorno Pula-Pula do assinante.
	 *	@throws Exception
	 */
	public static RetornoEstornoPulaPula getEstornos(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, Collection diretorios, String sessionId) throws Exception
	{
	    String xmlEstorno = ConsultaEstornoPulaPulaGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta);
	    ConsultaEstornoPulaPulaGPP.saveToFile(xmlEstorno, diretorios, sessionId);
	    return ConsultaEstornoPulaPulaGPP.parse(xmlEstorno);
	}
	
	/**
	 *	Retorna a consulta de estorno Pula-Pula a partir de arquivo ja criado. Caso o arquivo nao exista, executa a 
	 *	consulta a partir de interface com o GPP.
	 * 
	 *	@param		String					msisdn						MSISDN do assinante.
	 *	@param		String					dataInicial					Data inicial da consulta.
	 *	@param		String					dataFinal					Data final da consulta.
	 *	@param		String					servidor					IP do servidor.
	 *	@param		String					porta						Porta do servidor.
	 *	@param		ArrayList				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao.
	 *	@return		RetornoEstornoPulaPula								Consulta de estorno Pula-Pula do assinante.
	 *	@throws Exception
	 */
	public static RetornoEstornoPulaPula getEstornosPulaPulaFromFile(String msisdn, String dataInicial, String dataFinal, String servidor, String porta, Collection diretorios, String sessionId) throws Exception
	{
	    String xmlEstorno = ConsultaEstornoPulaPulaGPP.readFromFile(diretorios, sessionId);
	    if(xmlEstorno == null)
	    {
	        return ConsultaEstornoPulaPulaGPP.getEstornos(msisdn, dataInicial, dataFinal, servidor, porta, diretorios, sessionId);
	    }
	    
	    return ConsultaEstornoPulaPulaGPP.parse(xmlEstorno);
	}
			
	
			
	/**
	 *	Interpreta o XML da consulta de estorno Pula-Pula.
	 * 
	 *	@param		String					xmlEstorno					XML contendo a consulta de estorno Pula-Pula do assinante.
	 *	@return		RetornoEstorno			result						Objeto contendo os informacoes da consulta de estorno Pula-Pula.
	 *	@throws		Exception
	 */
	public static RetornoEstornoPulaPula parse(String xmlExtrato) throws Exception
	{
		RetornoEstornoPulaPula result = new RetornoEstornoPulaPula();
	
	    //Executa o parse do XML de acordo com o modelo DOM.
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xmlExtrato));
		Document doc = parse.parse(is);

		//Obtendo a raiz.
		Element elmGppEstorno = (Element)doc.getElementsByTagName("GPPConsultaEstornoPulaPula").item(0);
		//Obtendo o codigo de retorno.
		if((elmGppEstorno.getElementsByTagName("retorno").getLength() > 0) &&
		   (elmGppEstorno.getElementsByTagName("retorno").item(0).getChildNodes().getLength() > 0))
		{
		    String codigoRetorno = elmGppEstorno.getElementsByTagName("retorno").item(0).getChildNodes().item(0).getNodeValue();
		    result.setCodigoRetorno(Short.parseShort(codigoRetorno));
		}
		//Obtendo o MSISDN do assinante.
		if((elmGppEstorno.getElementsByTagName("msisdn").getLength() > 0) &&
		   (elmGppEstorno.getElementsByTagName("msisdn").item(0).getChildNodes().getLength() > 0))
		{
		    String msisdn = elmGppEstorno.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue();
		    result.setMsisdn(msisdn);
		}
		//Obtendo a data inicial da consulta.
		if((elmGppEstorno.getElementsByTagName("dataInicio").getLength() > 0) &&
		   (elmGppEstorno.getElementsByTagName("dataInicio").item(0).getChildNodes().getLength() > 0))
		{
		    String dataInicio = elmGppEstorno.getElementsByTagName("dataInicio").item(0).getChildNodes().item(0).getNodeValue();
		    result.setDataInicio(new SimpleDateFormat(Constantes.DATA_FORMATO).parse(dataInicio));
		}
		//Obtendo a data final da consulta.
		if((elmGppEstorno.getElementsByTagName("dataFim").getLength() > 0) &&
		   (elmGppEstorno.getElementsByTagName("dataFim").item(0).getChildNodes().getLength() > 0))
		{
		    String dataFim = elmGppEstorno.getElementsByTagName("dataFim").item(0).getChildNodes().item(0).getNodeValue();
		    result.setDataFim(new SimpleDateFormat(Constantes.DATA_FORMATO).parse(dataFim));
		}
		
		//Obtendo os detalhes de cada estorno.
		ArrayList listaEstornos = new ArrayList();
		Element elmListaEstornos = (Element)elmGppEstorno.getElementsByTagName("listaEstornos").item(0);
		NodeList ndlstEstornos = elmListaEstornos.getElementsByTagName("estorno");
		for(int i = 0; i < ndlstEstornos.getLength(); i++)
		{
			EstornoPulaPula estorno = new EstornoPulaPula();
			Element elmEstorno = (Element)ndlstEstornos.item(i);

			//Data de referencia.
			if((elmEstorno.getElementsByTagName("dataReferencia").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("dataReferencia").item(0).getChildNodes().getLength() > 0))
			{	
			    String dataReferencia = elmEstorno.getElementsByTagName("dataReferencia").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setDataReferencia(dataReferencia);
			}
			//MSISDN do assinante.
			if((elmEstorno.getElementsByTagName("msisdn").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("msisdn").item(0).getChildNodes().getLength() > 0))
			{
			    String msisdn = elmEstorno.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setMsisdn(msisdn);
			}
			//Promocao.
			if((elmEstorno.getElementsByTagName("promocao").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("promocao").item(0).getChildNodes().getLength() > 0))
			{	
			    String promocao = elmEstorno.getElementsByTagName("promocao").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setPromocao(Integer.parseInt(promocao));
			}
			//Numero de origem.
			if((elmEstorno.getElementsByTagName("numeroOrigem").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("numeroOrigem").item(0).getChildNodes().getLength() > 0))
			{	
			    String numeroOrigem = elmEstorno.getElementsByTagName("numeroOrigem").item(0).getChildNodes().item(0).getNodeValue(); 
			    estorno.setNumeroOrigem(numeroOrigem);
			}
			//Origem do estorno.
			if((elmEstorno.getElementsByTagName("origem").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("origem").item(0).getChildNodes().getLength() > 0))
			{	
			    String origem = elmEstorno.getElementsByTagName("origem").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setOrigem(origem);
			}
			//Data de processamento.
			if((elmEstorno.getElementsByTagName("dataProcessamento").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("dataProcessamento").item(0).getChildNodes().getLength() > 0))
			{	
			    String dataProcessamento = elmEstorno.getElementsByTagName("dataProcessamento").item(0).getChildNodes().item(0).getNodeValue(); 
			    estorno.setDataProcessamento(new SimpleDateFormat(Constantes.DATA_HORA_FORMATO).parse(dataProcessamento));
			}
			//Valor de expurgo.
			if((elmEstorno.getElementsByTagName("valorExpurgo").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("valorExpurgo").item(0).getChildNodes().getLength() > 0))
			{	
			    String valorExpurgo = elmEstorno.getElementsByTagName("valorExpurgo").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setValorExpurgo(Double.parseDouble(valorExpurgo));
			}
			//Valor de expurgo com o limite da promocao do assinante ultrapassado.
			if((elmEstorno.getElementsByTagName("valorExpurgoSaturado").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("valorExpurgoSaturado").item(0).getChildNodes().getLength() > 0))
			{	
			    String valorExpurgoSaturado = elmEstorno.getElementsByTagName("valorExpurgoSaturado").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setValorExpurgoSaturado(Double.parseDouble(valorExpurgoSaturado));
			}
			//Valor de estorno.
			if((elmEstorno.getElementsByTagName("valorEstorno").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("valorEstorno").item(0).getChildNodes().getLength() > 0))
			{	
			    String valorEstorno = elmEstorno.getElementsByTagName("valorEstorno").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setValorEstorno(Double.parseDouble(valorEstorno));
			}
			//Valor efetivamente estornado.
			if((elmEstorno.getElementsByTagName("valorEstornoEfetivo").getLength() > 0) &&
			   (elmEstorno.getElementsByTagName("valorEstornoEfetivo").item(0).getChildNodes().getLength() > 0))
			{	
			    String valorEstornoEfetivo = elmEstorno.getElementsByTagName("valorEstornoEfetivo").item(0).getChildNodes().item(0).getNodeValue();
			    estorno.setValorEstornoEfetivo(Double.parseDouble(valorEstornoEfetivo));
			}
			
			listaEstornos.add(estorno);
		}
		
		result.setListaEstornos(listaEstornos);
		
		//Obtendo os valores totais.
		Element elmListaTotais = (Element)elmGppEstorno.getElementsByTagName("listaTotais").item(0);
		//Total de expurgos.
		if((elmListaTotais.getElementsByTagName("totalExpurgo").getLength() > 0) &&
		   (elmListaTotais.getElementsByTagName("totalExpurgo").item(0).getChildNodes().getLength() > 0))
		{	
		    String totalExpurgo = elmListaTotais.getElementsByTagName("totalExpurgo").item(0).getChildNodes().item(0).getNodeValue();
		    result.setTotalExpurgo(Double.parseDouble(totalExpurgo));
		}
		//Total de expurgos com limite ultrapassado.
		if((elmListaTotais.getElementsByTagName("totalExpurgoSaturado").getLength() > 0) &&
		   (elmListaTotais.getElementsByTagName("totalExpurgoSaturado").item(0).getChildNodes().getLength() > 0))
		{	
		    String totalExpurgoSaturado = elmListaTotais.getElementsByTagName("totalExpurgoSaturado").item(0).getChildNodes().item(0).getNodeValue();
		    result.setTotalExpurgoSaturado(Double.parseDouble(totalExpurgoSaturado));
		}
		//Total de estornos.
		if((elmListaTotais.getElementsByTagName("totalEstorno").getLength() > 0) &&
		   (elmListaTotais.getElementsByTagName("totalEstorno").item(0).getChildNodes().getLength() > 0))
		{	
		    String totalEstorno = elmListaTotais.getElementsByTagName("totalEstorno").item(0).getChildNodes().item(0).getNodeValue();
		    result.setTotalEstorno(Double.parseDouble(totalEstorno));
		}
		//Total efetivamente estornado.
		if((elmListaTotais.getElementsByTagName("totalEstornoEfetivo").getLength() > 0) &&
		   (elmListaTotais.getElementsByTagName("totalEstornoEfetivo").item(0).getChildNodes().getLength() > 0))
		{	
		    String totalEstornoEfetivo = elmListaTotais.getElementsByTagName("totalEstornoEfetivo").item(0).getChildNodes().item(0).getNodeValue();
		    result.setTotalEstornoEfetivo(Double.parseDouble(totalEstornoEfetivo));
		}
		
		return result;
	}
	
	/**
	 *	Le o XML da consulta de estorno Pula-Pula retornado pelo GPP no arquivo criado na sessao.
	 * 
	 *	@param		Collection				diretorios					Lista de diretorios para geracao de arquivo.
	 *																	A lista deve ser criada devido as diferencas 
	 *																	de diretorios temporarios em cada maquina 
	 *																	rodando o IAS e o GPP.
	 *	@param		String					sessionId					Identificador da sessao do usuario.
	 *	@return		String					result						XML com as informacoes da consulta de estorno Pula-Pula do assinante.
	 */
	private static String readFromFile(Collection diretorios, String sessionId)
	{
	    StringBuffer result = new StringBuffer();
	    
	    try
	    {
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
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	    
	    return (result.length() > 0) ? result.toString() : null;
	}

	/**
	 *	Grava o XML da consulta de estorno Pula-Pula retornado pelo GPP em arquivo.
	 * 
	 *	@param		String					xmlExtrato					XML com as informacoes da consulta de estorno Pula-Pula do assinante.
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
