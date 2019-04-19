//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaRecargasPeriodo;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.ConexaoBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Import Internos
import java.sql.*;

// XML
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.*;

/**
  *
  * A classe ConsultaRecargasPeriodo implementa os m�todos necess�rios � consulta
  * de recargas realizadas por determinado assinante, em um determinado per�odo.
  * Os par�metros para a consulta s�o recebidos no formato XML e o retorno tamb�m
  * est� no formato XML.
  * 
  * <P> Versao:			1.0
  *
  * @Autor: 			Marcelo Alves Araujo
  * Data: 				20/05/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  * 
  */

public final class ConsultaRecargasPeriodo extends Aplicacoes
{
    // Gerente de conexoes Banco Dados
    private GerentePoolBancoDados gerenteBanco;
        
    /**
	 * <p><b>M�todo...:</b> ConsultaAssinante
	 * <p><b>Descri��o:</b> Construtor 
	 * @param	logId	(long) - Identificador do Processo para Log
	 * @return									
	 */
    public ConsultaRecargasPeriodo (long logId)
	{
        super(logId, Definicoes.CL_CONSULTA_RECARGAS_PERIODO);
		
		// Cria Refer�ncia para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(logId);
	}

    /**
	 * <p><b>M�todo...:</b> executaConsultaRecargas
	 * <p><b>Descri��o:</b> Consulta o valor total de recargas realizadas pelo assinante em um per�odo determinado
	 * @param	aXML		(String)	- Numero do telefone a ser consultado
	 * @return	retornoXML	(String)	- Valor total de recargas realizadas no per�odo com duas casas decimais 
	 * @throws GPPInternalErrorException		
     * @throws GPPBadXMLFormatException
	 */
	public String executaConsultaRecargas(String aXML) throws GPPInternalErrorException, GPPBadXMLFormatException
	{
	    super.log(Definicoes.DEBUG, "executaConsultaRecargas", "Inicio.");
	    
	    // Dados contidos no XML de entrada e de retorno
	    RecargasPeriodo dadosXML 	= null;
	    
	    try
	    {
		    // Extrai os dados do XML
		    dadosXML = this.parseXMLConsultaPeriodo(aXML);
		    
		    // Valida o MSISDN como existente e verifica se � pr�-pago
		    dadosXML.setCodigoErro(this.validaPrePago(dadosXML));
		    
		    // Executa a consulta e preenche o dadosXML
		    this.consultaRecargasAssinante(dadosXML);
		    
		    // Busca no banco a descri��o correspondente ao c�digo do erro
		    dadosXML.setDescricaoErro(this.getDescricaoRetorno(dadosXML.getCodigoErro()));
	    }
	    catch(GPPInternalErrorException e)
	    {
	        super.log(Definicoes.ERRO, "executaConsultaRecargas", "Erro GPP: " + e);
	        throw new GPPInternalErrorException("Erro: " + e);
	    } 
	    
	    super.log(Definicoes.DEBUG, "executaConsultaRecargas", "Fim");
	    
	    // Retorna o XML de resposta
	    return this.gerarXML(dadosXML);	    
	}
	
	/**
	 * <p><b>M�todo...:</b> consultaRecargasAssinante
	 * <p><b>Descri��o:</b> Busca o total de recargas do assinante no banco
	 * @param	dadosConsulta	(RecargasPeriodo)	- Retorno das validacoes efetuadas
	 * @return
	 * @throws 	GPPInternalErrorException
	 */
	private void consultaRecargasAssinante(RecargasPeriodo dadosConsulta) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG,"consultaRecargasAssinante","Inicio");
	    
		PREPConexao conexaoPrep = null;
	    
		try
		{
		    // Seleciona conex�o do pool Prep Conex�o
		    conexaoPrep = gerenteBanco.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    // Query para buscar o total de recargas do usu�rio
		    String sqlTotal =	" SELECT" +
								" NVL(SUM(NVL(VLR_CREDITO_PRINCIPAL,0)),0) AS VALOR" +
								" FROM" +
								" TBL_REC_RECARGAS" +
								" WHERE" +
								" DAT_ORIGEM > SYSDATE - ?" +
								" AND IDT_MSISDN = ?" +
								" AND ID_TIPO_RECARGA = 'R'";
		    
		    // Par�metro da consulta
		    Object parametros[] = {new Integer(dadosConsulta.getPeriodo()),dadosConsulta.getMSISDN()};
		    
		    rs = conexaoPrep.executaPreparedQuery(sqlTotal, parametros, super.getIdLog());
		    
		    // Retorna o total de recargas realizadas pelo usu�rio
		    if(rs.next())
		        if(dadosConsulta.getCodigoErro() == 0)
		            dadosConsulta.setTotalRecargas(rs.getDouble("VALOR"));		        		        
		    
		    rs.close();
		    rs = null;
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "consultaRecargasAssinante", "Erro(SQL): " + e);
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		catch(GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "consultaRecargasAssinante", "Erro interno GPP:" + e);
		}
		finally
		{
		    // Libera conexao com pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG,"consultaRecargasAssinante","Fim");
		}		
	}
	
	/**
	 * <p><b>M�todo...:</b> parseXMLConsultaPeriodo
	 * <p><b>Descri��o:</b> Le um XML de recarga �nica e seta os campos da estrutura DadosConsultaRecarga 
	 * @param 	 aXML		(String)			- XML Recebido
	 * @return 	 retorno	(RecargasPeriodo)	- Estrutura RecargasPeriodo populada
	 * @throws 	 GPPBadXMLFormatException
	 * @throws 	 GPPInternalErrorException
	 */
	private RecargasPeriodo parseXMLConsultaPeriodo ( String aXML ) throws GPPBadXMLFormatException, GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG, "parseXMLConsultaPeriodo", "Inicio");

	    RecargasPeriodo retorno = new RecargasPeriodo();		
		
		try
		{
			super.log(Definicoes.DEBUG, "parseXMLConsultaPeriodo", "XML consulta por periodo: " + aXML);
			
			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
			
			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();
			
			// Carrega o XML informado dentro de um InputSource
			InputSource is = new InputSource(new StringReader(aXML));
			
			// Faz o parse do XML
			Document doc = parse.parse(is);
	
			// Procura a TAG mensagem
			Element consultaRecarga = (Element)doc.getElementsByTagName("mensagem").item(0);
			
			// Pega o conte�do da TAG mensagem
			NodeList itemNodes = consultaRecarga.getChildNodes();			

			// Transforma a o conte�do da TAG em um novo elemento
			Element auxElement = (Element)itemNodes;
			
			// Entra na TAG cabacalho
			itemNodes 	= auxElement.getElementsByTagName("cabecalho").item(0).getChildNodes();
			auxElement 	= (Element)itemNodes;
			
			// Pega os valores contidos nas TAGs empresa, sistema, processo, data e identificador_requisicao
			retorno.setEmpresa		(auxElement.getElementsByTagName("empresa"					).item(0).getChildNodes().item(0).getNodeValue());
			retorno.setSistemaOrigem(auxElement.getElementsByTagName("sistema"					).item(0).getChildNodes().item(0).getNodeValue());
			retorno.setProcesso		(auxElement.getElementsByTagName("processo"					).item(0).getChildNodes().item(0).getNodeValue());
			retorno.setData			(auxElement.getElementsByTagName("data"						).item(0).getChildNodes().item(0).getNodeValue());
			retorno.setIDRequisicao	(auxElement.getElementsByTagName("identificador_requisicao"	).item(0).getChildNodes().item(0).getNodeValue());
			
			//Volta ao conte�do da TAG mensagem
			itemNodes	= consultaRecarga.getChildNodes();
			auxElement	= (Element)itemNodes;
			
			// Entra na TAG conteudo
			itemNodes 	= auxElement.getElementsByTagName("conteudo").item(0).getChildNodes();
			auxElement 	= (Element)itemNodes;
			
			// Carrega o XML do campo CDATA dentro de um InputSource
			is 	= new InputSource(new StringReader(auxElement.getChildNodes().item(0).getNodeValue()));

			// Faz o parse do XML
			doc = parse.parse(is);
			
			// Procura a TAG root
			consultaRecarga = (Element)doc.getElementsByTagName("root").item(0);
			
			// Entra na TAG root
			itemNodes 	= consultaRecarga.getChildNodes();			
			auxElement	= (Element)itemNodes;

			// Entra na TAG dados
			itemNodes 	= auxElement.getElementsByTagName("dados").item(0).getChildNodes();
			auxElement 	= (Element)itemNodes;

			// Pega os valores contidos nas TAGs evento, msisdn, periodo
			retorno.setEvento (auxElement.getElementsByTagName("evento").item(0).getChildNodes().item(0).getNodeValue());
			retorno.setMSISDN (auxElement.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
			retorno.setPeriodo(Integer.parseInt(auxElement.getElementsByTagName("periodo").item(0).getChildNodes().item(0).getNodeValue()));			
		}
		catch (SAXException e) 
		{
		    super.log(Definicoes.ERRO, "parseXMLConsultaPeriodo", "Erro(SAX) formato XML:" + e.getMessage());
		    retorno.setCodigoErro(Definicoes.RET_XML_MAL_FORMADO);
		}
		catch (IOException e) 
		{
			super.log(Definicoes.ERRO, "parseXMLConsultaPeriodo", "Erro(IO) interno GPP:" + e.getMessage());
			retorno.setCodigoErro(Definicoes.RET_XML_MAL_FORMADO);
		}
		catch (ParserConfigurationException e)
		{
			super.log(Definicoes.ERRO, "parseXMLConsultaPeriodo", "Erro(PARSER) interno GPP:" + e.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e);
		}
		catch (DOMException e) 
		{
			super.log(Definicoes.ERRO, "parseXMLConsultaPeriodo", "Erro(DOM) formato XML:" + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro formato XML:" + e);
		}
		catch (NumberFormatException e) 
		{
			super.log(Definicoes.ERRO, "parseXMLConsultaPeriodo", "Tag <periodo> nao numerica");
			retorno.setCodigoErro(Definicoes.RET_XML_MAL_FORMADO);
		}
		catch (Exception e) 
		{
			super.log(Definicoes.ERRO, "parseXMLConsultaPeriodo", "Erro interno GPP:" + e.getMessage());
			retorno.setCodigoErro(Definicoes.RET_XML_MAL_FORMADO);
		}
		
		super.log(Definicoes.DEBUG, "parseXMLConsultaPeriodo", "Fim");
		return retorno;		
	}
	
	/**
	 * <p><b>M�todo...:</b> gerarXML
	 * <p><b>Descri��o:</b> Monta o xml de retorno a partir dos retornos de cada uma das consultas
	 * @param	dadosConsulta	RecargasPeriodo	Dados do assinante
	 * @return	respostaXML		String			XML de retorno de consulta por per�odo
	 * Formato do XML:
	 *	<mensagem>
	 *		<cabecalho>
	 *			<empresa>BRG</empresa>
	 *			<sistema>CRM02</sistema>
	 *			<processo>CONSRCRGANTFRD</processo>
	 *			<data>25/05/2005 12:23:00</data> 
	 *			<identificador_requisicao>123</identificador_requisicao>
	 *			<codigo_erro>0</codigo_erro>
	 *			<descricao_erro>Consulta realizada com sucesso</descricao_erro>
	 *		</cabecalho>
	 *		<conteudo>
	 *			<![CDATA[
	 *				<root>
	 *					<dados>
	 *						<evento>RqConsultaRecargaPeriodoRsp</evento>
	 *						<msisdn>6184018401</msisdn>
	 *						<periodo>120</periodo>
	 *					</dados>
	 *					<retorno>
	 *						<valor_inserido>150.00</valor_inserido>
	 *					</retorno>
	 *				</root>
	 *			]]>	
	 *		</conteudo>
	 *	</mensagem>
	 */
	private String gerarXML(RecargasPeriodo dadosConsulta)
	{
	    super.log(Definicoes.DEBUG,"gerarXML","Inicio");
		
	    GerarXML geradorXML		= new GerarXML("mensagem");
	    
	    // Comp�e o conte�do da Tag cabecalho
	    geradorXML.abreNo("cabecalho");
	    geradorXML.adicionaTag("empresa"				 ,dadosConsulta.getEmpresa()      );
	    geradorXML.adicionaTag("sistema"				 ,dadosConsulta.getSistemaOrigem());
	    geradorXML.adicionaTag("processo"				 ,dadosConsulta.getProcesso()     );
	    geradorXML.adicionaTag("data"					 ,dadosConsulta.getData()		  );
	    geradorXML.adicionaTag("identificador_requisicao",dadosConsulta.getIDRequisicao() );
	    geradorXML.adicionaTag("codigo_erro"			 ,String.valueOf(dadosConsulta.getCodigoErro()));
	    geradorXML.adicionaTag("descricao_erro"			 ,dadosConsulta.getDescricaoErro());
	    geradorXML.fechaNo();
	    
	    // Cria a Tag conteudo e inclui o campo CDATA com o XML
	    geradorXML.adicionaTag("conteudo", "<![CDATA[" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + dadosConsulta.getRecargasPeriodoXML() + "]]>");
			    
		super.log(Definicoes.DEBUG,"gerarXML","Fim");
		
		return ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + geradorXML.getXML());
	}
	
    
	/**
	 * <p><b>M�todo...:</b> validaPrePago
	 * <p><b>Descri��o:</b> Verifica se o msisdn � de um pr�-pago e se ele existe
	 * @param	dadosConsulta	(RecargasPeriodo)	- Dados do assinante
	 * @return	retorno			(int)				- C�digo de erro
	 * @throws 	GPPInternalErrorException
	 */
	public int validaPrePago(RecargasPeriodo dadosConsulta) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG,"validaPrePago","Inicio");
	    
		int idRetorno;
		
		// Se o xml for mal formado esse erro ser� mantido
		if (dadosConsulta.getCodigoErro() == Definicoes.RET_XML_MAL_FORMADO)
		    idRetorno = Definicoes.RET_XML_MAL_FORMADO;
		// Sen�o inicia como assinante n�o ativo
		else
		    idRetorno = Definicoes.RET_MSISDN_NAO_ATIVO;
		
		PREPConexao conexaoPrep = null;
	    
		try
		{
		    // Cria objeto aprovisionar para consultar e alterar situa��o do cliente
		    Aprovisionar apr = new Aprovisionar(super.getIdLog());
		    // Objeto que cont�m informa��es retiradas da tecnomen sobre o assinante
		    Assinante assinante = apr.consultaAssinante(dadosConsulta.getMSISDN());
		    
		    if(assinante != null)
		    {
			    // Seleciona conex�o do pool Prep Conex�o
			    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			    
			    ResultSet rs;
	
			    
			    // Query para verificar se o plano � pr�-pago e o assinante existe
			    String sqlHibrido = 	" SELECT" +
			    						"  DISTINCT PLANO.IDT_CATEGORIA AS HIBRIDO" +
			    						" FROM" +
			    						"  TBL_GER_PLANO_PRECO PLANO" +
			    						" WHERE" +
			    						"  PLANO.IDT_PLANO_PRECO = ?";
	
			    
			    Object parametro[] = {new Integer(assinante.getPlanoPreco())};
			    
			    rs = conexaoPrep.executaPreparedQuery(sqlHibrido, parametro, super.getIdLog());
			    
			    // Se a Query retornar: 1 		o assinante � do plano h�brido
			    // 						0 		o assinante � pr�-pago
			    //						2		o assinante � ligmix
			    if(rs.next())
			    {
			        if(rs.getBoolean("HIBRIDO")&& dadosConsulta.getCodigoErro() != Definicoes.RET_XML_MAL_FORMADO)
			            idRetorno = Definicoes.RET_ASSINANTE_HIBRIDO;
			        else if(!rs.getBoolean("HIBRIDO")&& dadosConsulta.getCodigoErro() != Definicoes.RET_XML_MAL_FORMADO)
			            idRetorno = Definicoes.RET_OPERACAO_OK;		        
			    }
			    
			    rs.close();
			    rs = null;	
		    }
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "validaPrePago", "Erro(SQL): " + e);
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		catch(GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "validaPrePago", "Erro interno GPP:" + e);
		}
		finally
		{
		    // Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG,"validaPrePago","Fim");
		}		
		return (idRetorno);
	}
		
	/**
	 * <p><b>M�todo...:</b> getDescricaoErro
	 * <p><b>Descri��o:</b> Retorna a descri��o dos c�digos de erro
	 * @param	codigoRetorno	(int)		- Retorno das validacoes efetuadas
	 * @return	retorno			(String)	- Descri��o do c�digo de erro
	 * @throws 	GPPInternalErrorException
	 */
	public String getDescricaoRetorno (int codigoRetorno)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "getDescricaoRetorno", "Inicio");
		String retorno = null;
		
		// Pega Conex�o com Banco de Dados
		ConexaoBancoDados DBConexao = gerenteBancoDados.getConexaoPREP(super.getIdLog());
	
		try
		{
			// Seleciona descricao do erro
			String sql = " SELECT DES_RETORNO FROM TBL_GER_CODIGOS_RETORNO " +
				  		 " WHERE VLR_RETORNO = ?";
			
			// Passa o c�digo do erro para busca das informa��es
			Object parametro[] = {new Integer(codigoRetorno)};
			
			// Executa a Query
			ResultSet rs = DBConexao.executaPreparedQuery(sql, parametro, super.getIdLog());
			
			// Pega a descri��o do erro
			if (rs.next())
				retorno = rs.getString("DES_RETORNO");
			
			rs.close();
			rs = null;
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "getDescricaoRetorno", "Excecao Interna GPP: "+ e);				
			throw new GPPInternalErrorException ("Excecao interna GPP: " + e);			 
		}
		catch (SQLException e)
		{
			super.log(Definicoes.ERRO, "getDescricaoRetorno", "Excecao SQL: "+ e);				
			throw new GPPInternalErrorException ("Excecao: " + e);			 
		}
		finally
		{
		    // Fecha a conex�o com o banco de dados
			gerenteBancoDados.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.DEBUG, "getDescricaoRetorno", "Fim");
		}			
		return retorno;
	}
}