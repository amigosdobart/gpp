package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.builder.ExtratoPulaPulaBuilder;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.ExtratoPulaPula;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.parser.ExtratoPulaPulaXMLParser;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoStatusAssinante;

/**
 *	Classe responsavel pela implementacao do extrato contendo os bonus concedidos pelas promocoes Pula-Pula.
 *
 *	@author Daniel Ferreira
 *	@since 	07/10/2005
 */
public final class GerarExtratoPulaPula extends Aplicacoes
{

    /**
     *	Construtor da classe.
     *
     *	@param		long					idLog						Identificador de LOG.
     */
	public GerarExtratoPulaPula(long idLog)
	{
		super(idLog, Definicoes.CL_GERAR_EXTRATO_PULA_PULA);
	}
	
	/**
	 *	Executa a construcao do extrato Pula-Pula do assinante.
	 *
     *	@param		msisdn					MSISDN do assinante.
	 *	@param		dataIni					Data de inicio de analise, no formato dd/MM/yyyy.
	 *	@param		dataFim					Data de fim de analise, no formato dd/MM/yyyy.
	 * 	@param		isConsultaCheia			Indica se a consulta será com o valor de bônus cheio
	 *	@return		result					XML com informacoes do extrato.
	 */
	public String comporExtrato(String msisdn, String dataIni, String dataFim, boolean isConsultaCheia) 
	{
	    String				result			= null;
	    PREPConexao			conexaoPrep		= null;
		SimpleDateFormat	conversorDate	= new SimpleDateFormat(Definicoes.MASCARA_DATE);
	    
		super.log(Definicoes.DEBUG, "comporExtrato", "MSISDN: " + msisdn + " - Periodo: " + dataIni + " a " + dataFim);
		
		try		
		{
		    //Obtendo conexao com o banco de dados.
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			
		    //Obtendo as informacoes da promocao Pula-Pula do assinante.
		    AssinantePulaPula pAssinante = this.getAssinantePulaPula(msisdn, 
		    														 conversorDate.parse(dataIni), 
		    														 isConsultaCheia, 
		    														 conexaoPrep);
		    
		    if(pAssinante.getAssinante().getRetorno() == Definicoes.RET_OPERACAO_OK)
		    {
			    //Caso a promocao do assinante seja o Pula-Pula Fale e Ganhe do Pos-Pago, o resultado do extrato 
			    //deve ser sem limitacoes.
			    if(pAssinante.getPromocao().getIdtPromocao() == Promocao.FALE_GANHE)
			    	isConsultaCheia = true;
			    
			    //Obtendo as datas efetivas de analise.
			    Date[] datasAnalise = this.getDatasAnalise(pAssinante, 
			    										   conversorDate.parse(dataIni), 
			    										   conversorDate.parse(dataFim), 
			    										   isConsultaCheia);
			    
			    //Construindo o extrato Pula-Pula do assinante.
			    ExtratoPulaPulaBuilder builder = new ExtratoPulaPulaBuilder(conexaoPrep);
			    ExtratoPulaPula extrato = builder.newExtratoPulaPula(pAssinante, 
			    													 datasAnalise[0], 
			    													 datasAnalise[1], 
			    													 isConsultaCheia);
			    
			    //Obtendo o XML de retorno.
			    result = ExtratoPulaPulaXMLParser.format(extrato);
		    }
		    else
		    	result = ExtratoPulaPulaXMLParser.format(pAssinante.getAssinante().getRetorno());
		}
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "comporExtrato", "MSISDN: " + msisdn + " - Excecao: " + e);
	        result = ExtratoPulaPulaXMLParser.format(e);
	    }
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			super.log(Definicoes.DEBUG, "comporExtrato", "MSISDN: " + msisdn + " - XML: [" + result + "]");
		}
		
		return result;
	}
	
	/**
	 *	Executa a consulta da promocao Pula-Pula do assinante informado. Caso o assinante nao exista na base do 
	 *	Pre-Pago, ou seja, se nao for Pre-Pago ou Controle, sera considerado Pos-Pago, para que a consulta da 
	 *	promocao Fale e Ganhe seja executada. 
	 *	
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		dataIni					Data inicial da consulta do extrato. Utilizada para obter a data de 
	 *										referencia da consulta, que corresponde ao proximo mes.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		Informacoes da promocao Pula-Pula do assinante.
	 *	@throws		Exception
	 */
	private AssinantePulaPula getAssinantePulaPula(String		msisdn, 
												   Date			dataIni, 
												   boolean		isConsultaCheia,
												   PREPConexao	conexaoPrep) throws Exception
	{
		AssinantePulaPula result = null;
		
		//Obtendo a data de referencia a partir da data inicial de consulta do extrato.
		Calendar calReferencia = Calendar.getInstance();
		calReferencia.setTime(dataIni);
		calReferencia.add(Calendar.MONTH, 1);
		Date dataReferencia = calReferencia.getTime();
		
		//Obtendo as informacoes do assinante na base de dados.
		Assinante assinante = new Consulta(super.getIdLog()).getAssinanteGPPBanco(msisdn, conexaoPrep);
		
		//Caso o assinante seja Pre-Pago ou Controle, e necessario realizar a consulta da promocao Pula-Pula.
		//Caso contrario, e considerado que o assinante seja Pos-Pago.
		if(assinante != null)
		{
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.SALDO_PULA_PULA
            };
            
			result = new ControlePulaPula(super.getIdLog()).consultaPromocaoPulaPula(msisdn, 
																					 detalhes,
																					 dataReferencia,
																					 assinante, 
																					 isConsultaCheia, 
																					 conexaoPrep);
			//Caso a promocao Pula-Pula do assinante nao exista, ou o assinante e hibrido da safra antiga, ou nao 
			//possui promocao Pula-Pula.  
			if(result == null)
			{
				result = new AssinantePulaPula();
				result.setPromocao(new Promocao());
				result.setAssinante(assinante);
				
				if(assinante.isHibrido())
					assinante.setRetorno((short)Definicoes.RET_HIBRIDO_PROMOCAO_ANTIGA);
				else
					assinante.setRetorno((short)Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE);
			}
		}
		else
		{
			assinante = new Assinante();
			assinante.setMSISDN(msisdn);
			result = new AssinantePulaPula();
			result.setIdtMsisdn(msisdn);
			result.setAssinante(assinante);
			result.setPromocao(MapPromocao.getInstancia().getPromocao(Promocao.FALE_GANHE));
			result.setStatus(MapPromocaoStatusAssinante.getInstance().getPromocaoStatusAssinante(PromocaoStatusAssinante.ATIVO));
		}
		
		return result;
	}
	
	/**
	 *	Calcula e retorna as datas efetivas de inicio e fim da consulta do extrato. 
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante
	 *	@param		dataIni					Data inicial da consulta do extrato. 
	 *	@param		dataFim					Data final da consulta do extrato.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@return		Datas efetivas de inicio e fim da consulta do extrato.
	 */
	private Date[] getDatasAnalise(AssinantePulaPula pAssinante, Date dataIni, Date dataFim, boolean isConsultaCheia)
	{
		Date[] result = new Date[2];
		
		//Data de inicio da consulta.
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(dataIni);
		
		//Data final da consulta.
		Calendar calFim = Calendar.getInstance();
		calFim.setTime(dataFim);
		
		//As datas de inicio e fim da consulta devem obrigatoriamente estar no mesmo mes. Caso nao estejam, o mes 
		//da a data de inicio da consulta sera o considerado.
		if((calIni.get(Calendar.YEAR ) != calFim.get(Calendar.YEAR )) || 
		   (calIni.get(Calendar.MONTH) != calFim.get(Calendar.MONTH)))
		{
			calFim.setTime(dataIni);
			calFim.set(Calendar.DAY_OF_MONTH, calFim.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		
	    //Verificando a data de entrada do assinante no Pula-Pula. Caso a data de entrada seja maior que o
	    //periodo de analise, as datas de analise devem ser ajustadas de forma que os eventos anteriores ao 
	    //mes de entrada nao podem ser acessiveis ao novo titular. Teoricamente, se as datas forem menores
	    //que a data de entrada do assinante, deveriam ser ajustadas de forma a ficarem iguais a data de 
	    //entrada. Porem, devido aos atrasos nas importacoes de CDR's, ha a excecao das ligacoes recebidas
	    //no mes da transferencia.
	    if(!isConsultaCheia)
	    {
		    Calendar calEntrada = Calendar.getInstance();
		    calEntrada.setTime(pAssinante.getDatEntradaPromocao());
		    
		    if((calIni.before(calEntrada)) &&
    	       ((calIni.get(Calendar.YEAR ) != calEntrada.get(Calendar.YEAR )) ||
    	     	(calIni.get(Calendar.MONTH) != calEntrada.get(Calendar.MONTH))))
    	        calIni.setTime(calEntrada.getTime()); 

    	    if((calFim.before(calEntrada)) &&
 	    	  ((calFim.get(Calendar.YEAR ) != calEntrada.get(Calendar.YEAR )) ||
 	    	   (calFim.get(Calendar.MONTH) != calEntrada.get(Calendar.MONTH))))
		        calFim.setTime(calEntrada.getTime());
	    }
	    
	    result[0] = calIni.getTime();
	    result[1] = calFim.getTime();
	    
		return result;
	}
	
}
