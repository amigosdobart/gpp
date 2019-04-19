package com.brt.gpp.aplicacoes.consultar.consultaEstornoPulaPula;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
  *	Classe responsavel consulta pelos registros sumarizados resultantes do processo de Estorno de Bonus Pula-Pula
  *	por Fraude para os assinantes que receberam ligacoes indevidas.
  *
  *	@author Daniel Ferreira
  *	@since 	02/03/2006
  */

public final class ConsultaEstornoPulaPula extends ControlePulaPula 
{

    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public ConsultaEstornoPulaPula(long idLog)
	{
		super(idLog, Definicoes.CL_CONSULTA_ESTORNO_PULA_PULA);
	}
	
	/**
	 *	Executa a construcao do extrato Pula-Pula do assinante.
	 *
     *	@param		msisdn					MSISDN do assinante.
	 *	@param		inicio					Data de inicio de analise, no formato dd/MM/yyyy.
	 *	@param		fim						Data de fim de analise, no formato dd/MM/yyyy.
	 *	@return								XML com informacoes do extrato.
	 */
	public String consultaEstornoPulaPula(String msisdn, String inicio, String fim)
	{
	    String		result			= null;
	    int			codigoRetorno	= -1;
	    Collection	listaEstornos	= null;
	    Date		dataInicio		= null;
	    Date		dataFim			= null;
	    PREPConexao	conexaoPrep		= null;
	    
		super.log(Definicoes.DEBUG, "consultaEstornoPulaPula", "Inicio MSISDN "+ msisdn + " Periodo " + inicio + " - " + fim);
		
		try		
		{
		    //Obtendo conexao com o banco de dados.
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    
			//Convertendo as datas de inicio e fim de analise para objeto Date. Como o metodo de consulta pelos
			//registros no banco de dados exige que a data final seja menor e nao menor ou igual, e necessario 
			//adiciona-la em 1 dia. 
			//Alteracao em 26/04/2006: A consulta deve desconsiderar registros existentes antes da data de entrada
			//do assinante em promocao Pula-Pula.
			AssinantePulaPula pAssinante = super.consultaPromocaoPulaPula(msisdn, null, null, false, conexaoPrep);
			Date dataEntrada = pAssinante.getDatEntradaPromocao();
			SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
			Calendar calAnalise = Calendar.getInstance();
			calAnalise.setTime(conversorDate.parse(inicio));
			//Data de inicio de analise.
			dataInicio = (calAnalise.getTime().compareTo(dataEntrada) >= 0) ? calAnalise.getTime() : dataEntrada;
			//Data Fim de analise.
			calAnalise.setTime(conversorDate.parse(fim));
			calAnalise.add(Calendar.DAY_OF_MONTH, 1);
			dataFim = (calAnalise.getTime().compareTo(dataEntrada) >= 0) ? calAnalise.getTime() : dataEntrada;
			
			//Executando a consulta pelos registros de estorno.
			listaEstornos = super.consulta.getPromocaoEstornoPulaPula(msisdn, dataInicio, dataFim, conexaoPrep);
			
			codigoRetorno = ((listaEstornos != null) && (listaEstornos.size() > 0)) ? Definicoes.RET_OPERACAO_OK : Definicoes.RET_NENHUM_REGISTRO;
		}
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "consultaEstornoPulaPula", "Excecao: " + e);
	        codigoRetorno = Definicoes.RET_ERRO_TECNICO;
	    }
		finally
		{
		    //Liberando a conexao com o banco de dados.
		    super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		    
		    result = this.gerarXml(codigoRetorno, msisdn, inicio, fim, listaEstornos);
		    
			super.log(Definicoes.DEBUG, "consultaEstornoPulaPula", "Fim");
		}
		
		return result;
	}
	
	/**
	 *	Gera o XML com os registros de estorno de bonus Pula-Pula por fraude.
	 *
	 *	@param		int						codigoRetorno				Codigo de retorno da consulta.
     *	@param		String					msisdn						MSISDN do assinante.
	 *	@param 		String					inicio						Data de inicio de analise, no formato dd/MM/yyyy.
	 *	@param 		String					fim							Data de fim de analise, no formato dd/MM/yyyy.
	 *	@param		Collection				listaEstornos				Lista com os registros de estorno.
	 *	@return		String												XML da consulta de estorno de bonus Pula-Pula.
	 */
	private String gerarXml(int codigoRetorno, String msisdn, String inicio, String fim, Collection listaEstornos) 
	{
		GerarXML geradorXML = new GerarXML("GPPConsultaEstornoPulaPula");

		geradorXML.adicionaTag("retorno"	, new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO).format(codigoRetorno));
		geradorXML.adicionaTag("msisdn"		, msisdn);
		geradorXML.adicionaTag("dataInicio"	, inicio);
		geradorXML.adicionaTag("dataFim"	, fim);
		
		double totalExpurgo			= 0.0;
		double totalExpurgoSaturado	= 0.0;
		double totalEstorno			= 0.0;
		double totalEstornoEfetivo	= 0.0;
		
		if((listaEstornos != null) && (listaEstornos.size() > 0))
		{
		    geradorXML.abreNo("listaEstornos");
		    
		    for(Iterator iterator = listaEstornos.iterator(); iterator.hasNext();)
		    {
		        PromocaoEstornoPulaPula estorno = (PromocaoEstornoPulaPula)iterator.next();
		        
		        totalExpurgo 			+= estorno.getVlrExpurgo();
		        totalExpurgoSaturado	+= estorno.getVlrExpurgoSaturado();
		        totalEstorno 			+= estorno.getVlrEstorno();
		        totalEstornoEfetivo 	+= estorno.getVlrEstornoEfetivo();
		        
		        geradorXML.abreNo("estorno");
		        
		        geradorXML.adicionaTag("dataReferencia"			, estorno.toString(PromocaoEstornoPulaPula.DAT_REFERENCIA));
		        geradorXML.adicionaTag("msisdn"					, estorno.getIdtMsisdn());
		        geradorXML.adicionaTag("promocao"				, estorno.toString(PromocaoEstornoPulaPula.IDT_PROMOCAO));
		        geradorXML.adicionaTag("numeroOrigem"			, estorno.getIdtNumeroOrigem());
		        geradorXML.adicionaTag("origem"					, estorno.getIdtOrigem());
		        geradorXML.adicionaTag("dataProcessamento"		, estorno.toString(PromocaoEstornoPulaPula.DAT_PROCESSAMENTO));
		        geradorXML.adicionaTag("valorExpurgo"			, estorno.toString(PromocaoEstornoPulaPula.VLR_EXPURGO));
		        geradorXML.adicionaTag("valorExpurgoSaturado"	, estorno.toString(PromocaoEstornoPulaPula.VLR_EXPURGO_SATURADO));
		        geradorXML.adicionaTag("valorEstorno"			, estorno.toString(PromocaoEstornoPulaPula.VLR_ESTORNO));
		        geradorXML.adicionaTag("valorEstornoEfetivo"	, estorno.toString(PromocaoEstornoPulaPula.VLR_ESTORNO_EFETIVO));
		        
		        geradorXML.fechaNo();
		    }
		    
		    geradorXML.fechaNo();
		}
		else
		{
		    geradorXML.adicionaTag("listaEstornos", null);
		}
		
		geradorXML.abreNo("listaTotais");
		geradorXML.adicionaTag("totalExpurgo"			, String.valueOf(totalExpurgo));
		geradorXML.adicionaTag("totalExpurgoSaturado"	, String.valueOf(totalExpurgoSaturado));
		geradorXML.adicionaTag("totalEstorno"			, String.valueOf(totalEstorno));
		geradorXML.adicionaTag("totalEstornoEfetivo"	, String.valueOf(totalEstornoEfetivo));
		geradorXML.fechaNo();
	
		return geradorXML.getXML();			
	}
	
}
