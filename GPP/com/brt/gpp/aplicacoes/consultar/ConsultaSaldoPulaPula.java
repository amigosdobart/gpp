package com.brt.gpp.aplicacoes.consultar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

public class ConsultaSaldoPulaPula extends Aplicacoes 
{

    private ControlePulaPula controle;

	/**
	 *	Construtor da classe.
	 *
	 *	@param		aIdProcesso				Identificador de log.
	 */
	public ConsultaSaldoPulaPula(long aIdProcesso)
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_GERAR_EXTRATO_PULA_PULA);
		
		//Obtem referencia do gerente de conexoes do Banco de Dados
		this.controle = new ControlePulaPula(aIdProcesso);
	}
	
	/**
	 *	Retorna o valor do saldo Pula-Pula do assinante para o mes desejado.
	 *
     *	@param		msisdn					MSISDN do assinante.
	 *	@param		mesPesquisa				Mes para a pesquisa do valor do saldo.
	 *	@return								Valor do saldo pula pula para o mes informado desse assinante.
	 *	@throws		GPPInternalErrorException
	 */
	public double getSaldoPulaPula(String msisdn, int mesPesquisa) throws GPPInternalErrorException
	{
		double		result		= 0.0;
		PREPConexao	conexaoPrep	= null;
		
        super.log(Definicoes.INFO, "getSaldoPulaPula", "Inicio MSISDN " + msisdn + " Mes de pesquisa " + mesPesquisa);
		
		try
		{
		    //Obtendo conexao com o banco de dados.
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    
			//Obtendo as informacoes consolidadas referentes as ligacoes recebidas pelo assinante.
			//Utiliza uma instancia do Calendar para definir uma data com o mes
			//passado como parametro. Essa data sera utilizada para formatacao
			//do ano e mes que sera utilizado na consulta.
			//OBS:	O mes deveria ser decrementado de 1 devido a api do Calendar precisar
			//		dessa subtracao, mas como o mes de referencia para a
		    //		consulta da promocao Pula-Pula eh relativo a data de execucao do assinante,
		    //		sendo o mes de analise o anterior ao mes da data de execucao, esta conversao
		    //		nao e necessaria. Por exemplo, para consultar o saldo no mes 10/2005, seria 
		    //		necessario passar como parametro o mes 11/2005 (mes de execucao do assinante)
		    //		na promocao. Ao atribuir o mes ao objeto Calendar, automaticamente e calculado
		    //		o mes como sendo 12/2005, sendo a consulta executada corretamente.
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, mesPesquisa);
			SimpleDateFormat conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
			String mes = conversorDatMes.format(cal.getTime());
			//Truncando a data de referencia para a consulta da promocao Pula-Pula.
			cal.setTime(conversorDatMes.parse(mes));
			Date dataReferencia = cal.getTime();
				
			//Executando a consulta da promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_AGENDADOS,
                ControlePulaPula.SALDO_PULA_PULA
            };
			AssinantePulaPula pAssinante = this.controle.consultaPromocaoPulaPula(msisdn, 
																				  detalhes, 
																				  dataReferencia, 
																				  false, 
																				  false, 
																				  conexaoPrep);
			
			if((pAssinante != null) || (pAssinante.getSaldo() != null))
			{
			    result = pAssinante.getSaldo().getValorTotal();
			}
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "getSaldoPulaPula", "Excecao interna do GPP: " + e);
			throw e;
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "getSaldoPulaPula", "Excecao: " + e);
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
		    //Liberando a conexao com o banco de dados.
		    super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		    
	        super.log(Definicoes.INFO, "getSaldoPulaPula", "Fim");
		}
		
		return result;
	}

	/**
	 *	Retorna o valor do saldo Pula-Pula do assinante para o mes atual.
	 *
     *	@param		String					msisdn						MSISDN do assinante.
	 *	@return		String					result						Valor do saldo pula pula para o mes informado desse assinante.
	 *	@throws		GPPInternalErrorException
	 */
	public String getSaldoPulaPula(String msisdn) throws GPPInternalErrorException
	{
		double		result		= 0.0;
		PREPConexao	conexaoPrep	= null;
		
        super.log(Definicoes.INFO, "getSaldoPulaPula", "Inicio MSISDN " + msisdn);
		
		try
		{
		    //Obtendo conexao com o banco de dados.
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    
		    //Esta consulta eh realizada pela URA sendo que o mes atual deve ser utilizado
		    //para identificar se o assinante jah recebeu o bonus relativo ao ultimo mes.
		    //Caso este ainda nao tenha recebido entao deve ser mostrado o saldo Pula-Pula a 
		    //ser recebido no proximo mes.
			//Executando a consulta da promocao Pula-Pula do assinante.
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_AGENDADOS,
                ControlePulaPula.SALDO_PULA_PULA
            };
		    AssinantePulaPula pAssinante = this.controle.consultaPromocaoPulaPula(msisdn, 
		    																	  detalhes, 
		    																	  false, 
		    																	  false, 
		    																	  conexaoPrep);
			
			if(pAssinante != null)
			{
			    //Obtendo as informacoes referentes as datas de execucao e concessao de bonus para o assinante 
			    //para o tipo de execucao parcial. Caso a promocao do assinante nao possua execucao parcial, ou o
			    //periodo de concessao parcial para o mes atual ainda nao ocorreu, sera necessario apresentar as 
			    //informacoes referentes a execucao default.
			    Calendar calExecucao = Calendar.getInstance();
			    PromocaoDiaExecucao diaExecucao = pAssinante.getDiaExecucao(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL);
			    if((diaExecucao != null) && (diaExecucao.getNumDiaExecucao() != null) &&
			       (diaExecucao.getNumDiaExecucao().intValue() <= calExecucao.get(Calendar.DAY_OF_MONTH)))
			    {
			        result = this.getSaldoPulaPula(pAssinante, Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL);
			    }
			    else
			    {
			        result = this.getSaldoPulaPula(pAssinante, Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT);
			    }
			}
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "getSaldoPulaPula", "Excecao interna do GPP: " + e);
			throw e;
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "getSaldoPulaPula", "Excecao: " + e);
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
		    //Liberando a conexao com o banco de dados.
		    super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		    
	        super.log(Definicoes.INFO, "getSaldoPulaPula", "Fim");
		}
		
		//Formata a saida do valor do bonus esperado pela consulta via URA
		DecimalFormat conversorUra = new DecimalFormat(Definicoes.MASCARA_URA);
		conversorUra.setMinimumFractionDigits(0);
		conversorUra.setMaximumFractionDigits(0);

		return conversorUra.format(result * 1000);
	}

	/**
	 *	Retorna o saldo pula pula acumulado pelo cliente no mes atual, para o tipo de execucao passado por parametro.
	 *
     *	@return		AssinantePulaPula		result						Informacoes da Promocao Pula-Pula do assinante.
     *	@param		String					tipoExecucao				Tipo de execucao (DEFAULT, PARCIAL).
	 *	@return		String					result						Saldo Pula-pula acumulado ate o momento.
	 * 	@throws		GPPInternalErrorException
	 */
	public double getSaldoPulaPula(AssinantePulaPula pAssinante, String tipoExecucao) throws GPPInternalErrorException
	{
		double result = 0.0;
		
        super.log(Definicoes.INFO, "getSaldoPulaPula", "Inicio");
        
		try
		{
		    if(pAssinante != null)
		    {
		        //Verificando se o assinante ja recebeu bonus no periodo. Caso tenha recebido, o valor retornado sera
		        //sera positivo. Caso o assinante nao tenha recebido ou o bonus ainda estiver agendado, o valor 
		        //retornado sera negativo.
		        Collection bonusConcedidos = pAssinante.getBonusConcedidos(tipoExecucao);
		        Collection bonusAgendados  = pAssinante.getBonusAgendados (tipoExecucao);
		        if(bonusConcedidos.size() > 0)
		        {
			        Recarga bonusConcedido = (Recarga)bonusConcedidos.iterator().next();
			        result = (bonusConcedido.getVlrCreditoBonus() != null) ? bonusConcedido.getVlrCreditoBonus().doubleValue() : 0.0;
		        }
		        else if(bonusAgendados.size() > 0)
		        {
			        FilaRecargas bonusAgendado = (FilaRecargas)bonusAgendados.iterator().next();
			        result = (bonusAgendado.getVlrCreditoBonus() != null) ? -bonusAgendado.getVlrCreditoBonus().doubleValue() : 0.0;
		        }
		        else
		        {
			    	//Caso o assinante ainda nao tenha recebido o bonus no periodo, apresentar o saldo a receber.
		            //Independentemente do tipo de execucao, o valor retornado sera o valor a receber, uma vez que
		            //se o assinante ainda nao recebeu concessao parcial, o valor a receber sera igual ao valor total,
		            //e correspondera ao valor a ser concedido ao assinante em seu periodo de concessao parcial. Uma
		            //vez recebido, este valor sera automaticamente informado pelo valor parcial no objeto
		            //SaldoPulaPula, e o valor restante a ser recebido pelo assinante na execucao default sera 
		            //informado pelo atribuito valor a receber do objeto, sendo este a diferenca entre o valor total e
		            //o valor parcial.
				    result = (pAssinante.getSaldo() != null) ? -pAssinante.getSaldo().getValorAReceber() : 0.0;
		        }
		    }
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "getSaldoPulaPula", "Excecao: " + e);
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
	        super.log(Definicoes.INFO, "getSaldoPulaPula", "Fim");
		}
		
		return result;
	}
	
}
