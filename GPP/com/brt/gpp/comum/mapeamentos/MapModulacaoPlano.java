package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;

//Imports GPP.

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ModulacaoPlanoPreco;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
  *  Esta classe realiza o gerencimanto dos objetos que indicam a modulacao
  *  por plano de precos em memoria. 
  *  
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				20/10/2005
  *
  * Modificado por:		Daniel Ferreira
  * Data:				30/03/2006
  * Razao:				Alteracao para modelo de mapeamentos.
  *
  */

public final class MapModulacaoPlano extends Mapeamento
{
    
	private static MapModulacaoPlano instance;

	/**
	 *	Construtor da classe.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	private MapModulacaoPlano() throws GPPInternalErrorException
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapModulacaoPlano									Instancia do singleton.
	 */
	public static MapModulacaoPlano getInstance() 
	{
		if(MapModulacaoPlano.instance == null)
		{
		    try
		    {
				MapModulacaoPlano.instance = new MapModulacaoPlano();
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
		}
			
		return MapModulacaoPlano.instance;
	}

	/**
	 *	Carrega o mapeamento em memoria.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	protected synchronized void load()
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			// Busca uma conexao com o banco de dados no pool de conexoes
			// Realiza a pesquisa de todos os valores existentes na tabela
			// e entao para cada linha cria o objeto ModulacaoPlanoPreco
			// a ser inserido no cache em memoria
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			String sql = "select idt_plano_preco,idt_hora_inicio,idt_hora_fim,idt_dia_semana,idt_modulacao " +
				           "from tbl_ger_modulacao_plano";
			ResultSet rs = conexaoPrep.executaQuery(sql,0);
			
			while (rs.next())
			{
			    int plano = rs.getInt(1);
				ModulacaoPlanoPreco modPlano = new ModulacaoPlanoPreco(plano);
				long periodoInicial = rs.getLong(2);	
				modPlano.setPeriodoInicial(periodoInicial);
				long periodoFinal = rs.getLong(3);
				modPlano.setPeriodoFinal(periodoFinal);
				int diaSemana = rs.getInt(4);
				modPlano.setDiaSemana(diaSemana);
				String modulacao = rs.getString(5);
				modPlano.setModulacao(modulacao);
				
				//Inserindo o objeto na lista. O mapeamento segue uma estutura de arvore hierarquica com a seguinte
				//ordem: plano - dia - hora.
				Map mapPlano = (Map)super.values.get(new Integer(plano));
				if(mapPlano == null)
				{
				    mapPlano = Collections.synchronizedMap(new HashMap());
				    super.values.put(new Integer(plano), mapPlano);
				}
				Map mapDiaSemana = (Map)mapPlano.get(new Integer(diaSemana));
				if(mapDiaSemana == null)
				{
				    mapDiaSemana = Collections.synchronizedMap(new HashMap());
				    mapPlano.put(new Integer(diaSemana), mapDiaSemana);
				}
				String key = String.valueOf(periodoInicial) + "-" + String.valueOf(periodoFinal);
				mapDiaSemana.put(key, modPlano);
			}
			
			rs.close();
		}
		catch(Exception e)
		{
			// Erro na atualização do Mapeamento
		}
		finally
		{
			// Libera a conexao do banco dados de volta para o pool
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,0);
		}
	}
	
	/**
	 * Metodo....:getModulacao
	 * Descricao.:Retorna o valor da modulacao para o plano de preco
	 *            atraves da pesquisa em que o horario desejado deve
	 *            estar entre os horarios definidos para esse plano
	 * @param planoPreco	- Plano de preco desejado para pesquisa
	 * @param horario		- Numero de segundos desde a meia noite a ser utilizado para pesquisa
	 * @return String		- Valor da modulacao para os parametros desejados
	 */
	public String getModulacao(int planoPreco, Date dataChamada, long horario)
	{
		Map mapPlano = (Map)super.values.get(new Integer(planoPreco));
		if(mapPlano != null)
		{
			// Independente do horario se o plano do assinante estiver cadastrado no
			// mapeamento entao verifica se a data da chamada foi realizada em um
			// feriado nacional, em caso positivo a modulacao eh considerada reduzida
			// e nenhuma analise no horario da chamada eh necessario
			if(MapFeriados.getInstance().isFeriado(dataChamada))
			{
				return Definicoes.IND_MODULACAO_REDUZIDA;
			}
			
		    Calendar calChamada = Calendar.getInstance();
		    calChamada.setTime(dataChamada);
		    
		    Map mapDiaSemana = (Map)mapPlano.get(new Integer(calChamada.get(Calendar.DAY_OF_WEEK)));
		    if(mapDiaSemana != null)
		    {
		        for(Iterator iterator = mapDiaSemana.values().iterator(); iterator.hasNext();)
		        {
		            ModulacaoPlanoPreco modulacao = (ModulacaoPlanoPreco)iterator.next();
		            if(modulacao.estaNoHorario(horario))
		            {
		                return modulacao.getModulacao();
		            }
		        }
		    }
		}
		
		return Definicoes.IND_MODULACAO_FLAT;
	}

}
