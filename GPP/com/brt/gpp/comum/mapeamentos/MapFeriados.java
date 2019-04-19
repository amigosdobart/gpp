package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

//Imports GPP.

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.Feriado;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Esta classe eh a responsavel por armazenar os valores de feriados em memoria.
 * 
 *	@author Joao Carlos
 *	Data..: 27/10/2005
 *
 */
public final class MapFeriados extends Mapeamento
{
    
	private static	DecimalFormat	conversorDiaMes = new DecimalFormat("00");
	private static	MapFeriados		instance		= null;
	
	/**
	 *	Construtor da classe.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	private MapFeriados() throws Exception
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapFeriados											Instancia do singleton.
	 */
	public static MapFeriados getInstance()
	{
		if(instance == null)
		{
		    try
		    {
				instance = new MapFeriados();
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
		}
		
		return instance;
	}

	/**
	 *	Carrega o mapeamento em memoria.
	 * 
	 *	@throws 	GPPInternalErrorException								
	 */
	protected void load() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			String sql = "select idt_dia,idt_mes,idt_ano,des_feriado from tbl_ger_feriado";
			ResultSet rs = conexaoPrep.executaQuery(sql,0);
			while (rs.next())
			{
				String dia = rs.getString(1);
				String mes = rs.getString(2);
				String ano = rs.getString(3);
				try
				{
					Feriado feriado = new Feriado(dia,mes,ano);
					feriado.setDescricao(rs.getString(4));
					super.values.put(new Integer(feriado.hashCode()),feriado);
				}
				catch(IllegalArgumentException a)
				{
					GerentePoolLog.getInstancia(this.getClass()).log(0,Definicoes.WARN,"MapFeriados","inicializaMapeamento","Data invalida como feriado. Feriado serah ignorado. Data:"+dia+"/"+mes+"/"+ano);		
				}
			}
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(this.getClass()).log(0,Definicoes.WARN,"MapFeriados","inicializaMapeamento","Erro a inicializar o mapeamento de Feriados. Erro"+e);
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,0);
		}
	}

	/**
	 * Metodo....:isFeriado
	 * Descricao.:Identifica se uma determinada data eh considerado feriado
	 *            baseando nos valores que estao em memoria da tabela de feriados
	 * @param data - Data a ser verificada se eh feriado
	 * @return boolean - Indica se eh ou nao feriado
	 */
	public boolean isFeriado(Date data)
	{
		// Para verificar se uma determinada data eh feriado primeiro
		// verifica se a data completa estah cadastrada no hash de feriados
		// caso negativo entao verifica se esta estah cadastrada de forma
		// onde a data eh repetida para todos os anos
		if (super.values.containsKey(MapFeriados.getKey(data, false)[0]))
			return true;

		if (super.values.containsKey(MapFeriados.getKey(data, true)[0]))
			return true;
		
		return false;
	}

	/**
	 *	Calcula o identificador unico da entidade.
	 *
	 *	@param		Date					data						Data utilizada para o calculo do identificador.
	 *	@param		boolean					regular						Indicador de o feriado ser regular ou nao.
	 *	@return		int													Hash do objeto.
	 */
	public static Object[] getKey(Date data, boolean regular)
	{
	    if(data != null)
	    {
	        Calendar calData = Calendar.getInstance();
	        calData.setTime(data);
	        
	        String dia = MapFeriados.conversorDiaMes.format(calData.get(Calendar.DAY_OF_MONTH));
	        String mes = MapFeriados.conversorDiaMes.format(calData.get(Calendar.MONTH) + 1);
	        String ano = regular ? "*" : String.valueOf(calData.get(Calendar.YEAR));
	        
	        Feriado feriado = new Feriado(dia, mes, ano);
	        return new Object[]{new Integer(feriado.hashCode())};
	    }
	        
	    return new Object[]{null};
	}
	
}
