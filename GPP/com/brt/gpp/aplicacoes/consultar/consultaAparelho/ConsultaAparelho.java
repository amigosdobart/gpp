//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaAparelho;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Import Internos
import java.sql.*;
import java.util.Iterator;
import java.io.*;

// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
  *
  * A classe ConsultaAparelho implementa a consulta de características 
  * do aparelho dos assinante
  * 
  * <P> Versao:			1.0
  *
  * @Autor: 			Marcelo Alves Araujo
  * Data: 				16/06/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  * 
  */

public final class ConsultaAparelho extends Aplicacoes
{
    // Gerente de conexoes Banco Dados
    private String sucesso;
        
    /**
	 * <p><b>Método...:</b> ConsultaAparelho
	 * <p><b>Descrição:</b> Construtor 
	 * @param	logId	(long) - Identificador do Processo para Log
	 * @return									
	 */
    public ConsultaAparelho (long logId)
	{
        super(logId, Definicoes.CL_CONSULTA_RECARGAS_PERIODO);
		
		// Cria Referência para Banco de Dados
		GerentePoolBancoDados.getInstancia(logId);
		this.sucesso = Definicoes.RET_S_ERRO_TECNICO;
	}

    /**
	 * <p><b>Método...:</b> executaConsultaAparelho
	 * <p><b>Descrição:</b> Consulta o conjunto de características do aparelho de uma assinante
	 * @param	msisdn		(String)	- Numero do telefone a ser consultado
	 * @return	retornoXML	(String)	- Conjunto de características do aparelho 
	 * @throws GPPInternalErrorException		
     * @throws GPPBadXMLFormatException
	 */
	public String executaConsultaAparelho(String msisdn) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG, "executaConsultaAparelho", "Inicio.");
	    
	    // Dados do Aparelho do Assinante
	    AparelhoAssinante dados	= new AparelhoAssinante();
	    
	    try
	    {
		    // Executa a consulta às características do aparelho
		    dados = this.consultaAparelho(msisdn);
		}
	    catch(GPPInternalErrorException e)
	    {
	        super.log(Definicoes.ERRO, "executaConsultaAparelho", "Erro GPP: " + e);
	        throw new GPPInternalErrorException("Erro: " + e);
	    } 
	    
	    super.log(Definicoes.DEBUG, "executaConsultaAparelho", "Fim");
	    
	    // Retorna o XML
	    return gerarXML(dados);	    
	}
	
	/**
	 * <p><b>Método...:</b> consultaAparelho
	 * <p><b>Descrição:</b> Busca o total de recargas do assinante no banco
	 * @param	msisdn		(String)			- Número do telefone a ser consultado
	 * @return	aparelho	(AparelhoAssinante)	- Dados do Aparelho
	 * @throws 	GPPInternalErrorException
	 */
	private AparelhoAssinante consultaAparelho(String msisdn) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG,"consultaAparelho","Inicio");
	    
		PREPConexao conexaoPrep = null;
		// Dados do aparelho
		AparelhoAssinante aparelho = new AparelhoAssinante();
	    
		try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    // TODO: Voltar consulta ao normal
		    // Query para buscar os dados do aparelho do assinante
		    String sqlBusca =	"SELECT " +
		    					" d.nu_msisdn AS cliente, " +
		    					" b.no_fabricante AS marca, " +
		    					" a.no_modelo AS modelo, " +
		    					" e.no_capacidade AS capacidade, " +
		    					" e.ds_capacidade AS descricao, " +
		    					" c.ds_caracteristica AS valor, " +
		    					" e.ds_caracteristica AS adicionais " +
		    					"FROM " +
		    					" hsid.hsid_modelo a, " +
		    					" hsid.hsid_fabricante b, " +
		    					" hsid.hsid_modelo_capacidade c, " +
		    					" hsid.hsid_cliente d, " +
		    					" hsid.hsid_capacidade e " +
		    					"WHERE " +
		    					" d.co_modelo = a.co_modelo AND " +
		    					" a.co_fabricante = b.co_fabricante AND " +
		    					" d.co_modelo = c.co_modelo AND " +
		    					" c.co_capacidade = e.co_capacidade AND " +
		    					" d.nu_msisdn = ?" +
		    					"ORDER BY " +
		    					" e.ds_capacidade";
		    
		    // Parâmetro da consulta
		    Object parametros[] = {msisdn};
		    // Executa a consulta
		    rs = conexaoPrep.executaPreparedQuery(sqlBusca, parametros, super.getIdLog());
		    
		    // Coloca o MSISDN do assinante no objeto
		    aparelho.setMsisdn(msisdn);
	        
		    while(rs.next())
		    {
		        // Seta os Dados do Aparelho
		        aparelho.setMarca(rs.getString("MARCA"));
		        aparelho.setModelo(rs.getString("MODELO"));
		        
		        // Pega as características do aparelho
		        Caracteristica carac = new Caracteristica(rs.getString("CAPACIDADE"),rs.getString("DESCRICAO"),rs.getString("VALOR"),rs.getString("ADICIONAIS"));
		        
		        aparelho.setCaracteristica(carac);	
		        
		        this.sucesso = Definicoes.RET_S_OPERACAO_OK;
		    }		        		        		        
		    
		    rs.close();
		    rs = null;
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "consultaAparelho", "Erro(SQL): " + e);
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		catch(GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "consultaAparelho", "Erro interno GPP:" + e);
		}
		finally
		{
		    // Libera conexao com pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG,"consultaAparelho","Fim");
		}
		
		return aparelho;
	}
	
	/**
	 * <p><b>Método...:</b> parseXMLConsultaPeriodo
	 * <p><b>Descrição:</b> Le um XML de recarga única e seta os campos da estrutura DadosConsultaRecarga 
	 * @param 	 aXML		(String)			- XML Recebido
	 * @return 	 retorno	(AparelhoAssinante)	- Estrutura AparelhoAssinante populada
	 * @throws 	 GPPBadXMLFormatException
	 * @throws 	 GPPInternalErrorException
	 */
	public static AparelhoAssinante parseXMLConsultaAparelho(String aXML) throws GPPInternalErrorException
	{
	    AparelhoAssinante retorno = new AparelhoAssinante();		
		
		try
		{
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML informado dentro de um InputSource
			InputSource is = new InputSource(new StringReader(aXML));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG GPPConsultaAparelho
			Element consultaRecarga = (Element)doc.getElementsByTagName("GPPConsultaAparelho").item(0);
			
			// Pega o conteúdo da TAG GPPConsultaAparelho
			NodeList itemNodes = consultaRecarga.getChildNodes();			

			// Transforma a o conteúdo da TAG em um novo elemento
			Element auxElement = (Element)itemNodes;
			
			// Pega os valores contidos nas TAGs msisdn, marca e modelo
			retorno.setMsisdn(auxElement.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
			retorno.setMarca(auxElement.getElementsByTagName("marca").item(0).getChildNodes().item(0).getNodeValue());
			retorno.setModelo(auxElement.getElementsByTagName("modelo").item(0).getChildNodes().item(0).getNodeValue());
			
			// Pega os nós com nome característica
			itemNodes = ((Element)itemNodes).getElementsByTagName("carcateristica");
			
			for (int i=0; i < itemNodes.getLength(); i++)
			{
			    NodeList lista = itemNodes.item(i).getChildNodes();
			    
			    auxElement = (Element)lista;
			    String capac = null;
			    String descr = null;
			    String valor = null;
			    String adici = null;
			    
			    if(auxElement.getElementsByTagName("capacidade").item(0).getChildNodes().item(0) != null)
			        capac = auxElement.getElementsByTagName("capacidade").item(0).getChildNodes().item(0).getNodeValue();
			    if(auxElement.getElementsByTagName("descricao").item(0).getChildNodes().item(0) != null) 
			        descr = auxElement.getElementsByTagName("descricao").item(0).getChildNodes().item(0).getNodeValue();
	            if(auxElement.getElementsByTagName("valor").item(0).getChildNodes().item(0) != null)
			    	valor = auxElement.getElementsByTagName("valor").item(0).getChildNodes().item(0).getNodeValue();
	            if(auxElement.getElementsByTagName("adicional").item(0).getChildNodes().item(0) != null)
	            	adici = auxElement.getElementsByTagName("adicional").item(0).getChildNodes().item(0).getNodeValue();
	            
	            Caracteristica carac = new Caracteristica(capac, descr, valor, adici);
	            
	            retorno.setCaracteristica(carac);
		    }
		}
		catch (SAXException e) 
		{
		    System.out.println ("Erro(SAX) formato XML:" + e.getMessage());
		}
		catch (IOException e) 
		{
		    System.out.println ("Erro(IO) interno GPP:" + e.getMessage());
		}
		catch (ParserConfigurationException e)
		{
		    System.out.println ("Erro(PARSER) interno GPP:" + e.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e);
		}
		catch (DOMException e) 
		{
		    System.out.println ("Erro(DOM) formato XML:" + e.getMessage());
		}
		catch (Exception e) 
		{
		    System.out.println ("Erro interno GPP:" + e.getMessage());
		}
		
		return retorno;		
	}
	
	/**
	 * <p><b>Método...:</b> parseXMLRetorno
	 * <p><b>Descrição:</b> Le o retorno de sucesso ou insucesso da operação 
	 * @param 	 aXML		(String)	- XML Recebido
	 * @return 	 retorno	(String)	- Indica "0000" sucesso, "0099" erro
	 * @throws 	 GPPBadXMLFormatException
	 * @throws 	 GPPInternalErrorException
	 */
	public static String parseXMLRetorno(String aXML) throws GPPInternalErrorException
	{
	    String retorno = null;		
		
		try
		{
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML informado dentro de um InputSource
			InputSource is = new InputSource(new StringReader(aXML));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG GPPConsultaAparelho
			Element consultaRecarga = (Element)doc.getElementsByTagName("GPPConsultaAparelho").item(0);
			
			// Pega o conteúdo da TAG GPPConsultaAparelho
			NodeList itemNodes = consultaRecarga.getChildNodes();			

			// Transforma a o conteúdo da TAG em um novo elemento
			Element auxElement = (Element)itemNodes;
			
			// Pega os valores contidos nas TAGs msisdn, marca e modelo
			retorno = auxElement.getElementsByTagName("retorno").item(0).getChildNodes().item(0).getNodeValue();
		}
		catch (SAXException e) 
		{
		    System.out.println ("Erro(SAX) formato XML:" + e.getMessage());
		}
		catch (IOException e) 
		{
		    System.out.println ("Erro(IO) interno GPP:" + e.getMessage());
		}
		catch (ParserConfigurationException e)
		{
		    System.out.println ("Erro(PARSER) interno GPP:" + e.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e);
		}
		catch (DOMException e) 
		{
		    System.out.println ("Erro(DOM) formato XML:" + e.getMessage());
		}
		catch (Exception e) 
		{
		    System.out.println ("Erro interno GPP:" + e.getMessage());
		}
		
		return retorno;		
	}
	
	/**
	 * <p><b>Método...:</b> gerarXML
	 * <p><b>Descrição:</b> Monta o xml de retorno a partir do objeto Aparelho Assinante
	 * @param	dadosConsulta	AparelhoAssinante	Dados do assinante
	 * @return	respostaXML		String				XML de retorno de consulta por aparelho
	 * Formato do XML:
	 *	<GPPConsultaAparelho>	
	 *		<msisdn>556184066666</msisdn>	
	 *		<marca>Nokia</marca>	
	 *		<modelo>1100</modelo>
	 *		<retorno>0000</retorno>	
	 *		<caracteristicas>		
	 *			<capacidade>Bateria</capacidade>		
	 *			<descricao>Tipo de Bateria</descricao>		
	 *			<valor>850mAh Li-ion</valor>		
	 *			<adicional></adicional>	
	 *		</caracteristicas>
	 *	</GPPConsultaAparelho>
	 */
	private String gerarXML(AparelhoAssinante dadosConsulta)
	{
	    super.log(Definicoes.DEBUG,"gerarXML","Inicio");
		
	    GerarXML geradorXML		= new GerarXML("GPPConsultaAparelho");
	    
	    // Compõe o conteúdo da Tag cabecalho
	    geradorXML.adicionaTag("msisdn",dadosConsulta.getMsisdn());
	    geradorXML.adicionaTag("marca",dadosConsulta.getMarca());
	    geradorXML.adicionaTag("modelo",dadosConsulta.getModelo());
	    geradorXML.adicionaTag("retorno",this.sucesso);
	    
	    Iterator it = dadosConsulta.getCaracteristica().iterator();
	    
	    while(it.hasNext())
	    {
	        Caracteristica carac = (Caracteristica)it.next();
	        geradorXML.abreNo("carcateristica");
		    geradorXML.adicionaTag("capacidade",carac.getCapacidade());
		    geradorXML.adicionaTag("descricao",carac.getDescricao());
		    geradorXML.adicionaTag("valor",carac.getValor());
		    geradorXML.adicionaTag("adicional",carac.getAdicionais());
		    geradorXML.fechaNo();
	    }
	    
	    super.log(Definicoes.DEBUG,"gerarXML","Fim");
		
		return ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + geradorXML.getXML());
	}
}