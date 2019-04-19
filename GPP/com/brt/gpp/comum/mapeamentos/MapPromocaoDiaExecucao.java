package com.brt.gpp.comum.mapeamentos;

// Java Imports
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// GPP Imports
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
  *	Este arquivo contem a classe que faz o mapeamento dos dias de execucao das Promocoes em funcao dos dias de 
  *	entrada dos assinantes existentes no BD Oracle em memoria no GPP.
  *
  *	@author		Daniel Ferreira
  *	@since 		15/08/2005
  */
public final class MapPromocaoDiaExecucao extends Mapeamento
{

	private static 			MapPromocaoDiaExecucao	instance					= null;
	private	static	final	String					MAX_DIA_EXECUCAO_RECARGA	= "MAX_DIA_EXECUCAO_RECARGA";
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoDiaExecucao() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapPromocaoDiaExecucao	instance					Mapeamento da TBL_PRO_DIA_EXECUCAO.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoDiaExecucao getInstancia () throws GPPInternalErrorException 
	{
		if(MapPromocaoDiaExecucao.instance == null) 
		{
		    MapPromocaoDiaExecucao.instance = new MapPromocaoDiaExecucao();
		}
		
		return MapPromocaoDiaExecucao.instance;
	}	

	/**
	 *	Carrega a HashTable de Mapeamento com os valores do BD.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		try
		{
			//Seleciona conexao do pool Prep Conexao
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			//Seleciona os dias de execucao validos no BD do GPP
			String sql = "SELECT						" +
						 "  IDT_PROMOCAO,				" +
						 "  TIP_EXECUCAO,				" +
						 "  NUM_DIA_ENTRADA,			" +
						 "  NUM_DIA_EXECUCAO,			" +
						 "  NUM_DIA_EXECUCAO_RECARGA,	" +
						 "  NUM_HORA_EXECUCAO_RECARGA	" +
			             "FROM TBL_PRO_DIA_EXECUCAO		";

			ResultSet resultDia = conexaoPrep.executaPreparedQuery(sql, null,0);
			
			while (resultDia.next())
			{
				PromocaoDiaExecucao diaExecucao = new PromocaoDiaExecucao();
				
				Integer idtPromocao = new Integer(resultDia.getInt("IDT_PROMOCAO"));
				diaExecucao.setIdtPromocao(idtPromocao);
				
				String tipExecucao = resultDia.getString("TIP_EXECUCAO");
				diaExecucao.setTipExecucao(tipExecucao);
				
				Integer numDiaEntrada = new Integer(resultDia.getInt("NUM_DIA_ENTRADA"));
				diaExecucao.setNumDiaEntrada(numDiaEntrada);
				
				int numDiaExecucao = resultDia.getInt("NUM_DIA_EXECUCAO");
				diaExecucao.setNumDiaExecucao((resultDia.wasNull()) ? null : new Integer(numDiaExecucao));
				
				int numDiaExecucaoRecarga = resultDia.getInt("NUM_DIA_EXECUCAO_RECARGA");
				diaExecucao.setNumDiaExecucaoRecarga((resultDia.wasNull()) ? null : new Integer(numDiaExecucaoRecarga));
				
				int numHoraExecucaoRecarga = resultDia.getInt("NUM_HORA_EXECUCAO_RECARGA");
				diaExecucao.setNumHoraExecucaoRecarga((resultDia.wasNull()) ? null : new Integer(numHoraExecucaoRecarga));
				
				//Insere o valor no objeto Hash.
				Map mapPromocao = (Map)super.values.get(idtPromocao);
				Map mapExecucao = null;
				if(mapPromocao == null)
				{
				    mapPromocao = Collections.synchronizedMap(new HashMap());
				    mapExecucao = Collections.synchronizedMap(new HashMap());
				    mapPromocao.put(tipExecucao, mapExecucao);
					super.values.put(idtPromocao, mapPromocao);
				}
				else
				{
				    mapExecucao = (Map)mapPromocao.get(tipExecucao);
				    if(mapExecucao == null)
				    {
					    mapExecucao = Collections.synchronizedMap(new HashMap());
					    mapPromocao.put(tipExecucao, mapExecucao);
				    }
				}
				//Atualizando o objeto que possui o ultimo dia de execucao de recarga para a promocao e tipo de execucao.
				PromocaoDiaExecucao maxRecarga = (PromocaoDiaExecucao)mapExecucao.get(MapPromocaoDiaExecucao.MAX_DIA_EXECUCAO_RECARGA);
				int maxDiaRecarga = ((maxRecarga != null) && (maxRecarga.getNumDiaExecucaoRecarga() != null)) ?
				    maxRecarga.getNumDiaExecucaoRecarga().intValue() : 0;
				int diaRecarga = (diaExecucao.getNumDiaExecucaoRecarga() != null) ?
				    diaExecucao.getNumDiaExecucaoRecarga().intValue() : 0;
				if(diaRecarga > maxDiaRecarga)
				{
				    mapExecucao.put(MapPromocaoDiaExecucao.MAX_DIA_EXECUCAO_RECARGA, diaExecucao);
				}
				mapExecucao.put(numDiaEntrada, diaExecucao);
			}
			
			resultDia.close();
			resultDia = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException ("Excecao SQL: " + e);			 
		}
		finally
		{
			//Libera conexao com do pool PREP
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Este metodo retorna um objeto contendo o objeto PromocaoDiaExecucao para um dado valor de chave (id).
	 *
	 *	@param		int						idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		String					tipExecucao					Tipo de execucao da promocao.
	 *	@param		int						numDiaEntrada				Dia de entrada do assinante na promocao.
	 *	@return		PromocaoDiaExecucao									Objeto PromocaoDiaExecucao correspondente.
	 */
	public PromocaoDiaExecucao getPromocaoDiaExecucao(int idtPromocao, String tipExecucao, int numDiaEntrada)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		Map mapPromocao = (Map)super.values.get(new Integer(idtPromocao));
		
		if(mapPromocao != null)
		{
		    Map mapExecucao = (Map)mapPromocao.get(tipExecucao);
		    
		    if(mapExecucao != null)
		    {
		        PromocaoDiaExecucao result = (PromocaoDiaExecucao)mapExecucao.get(new Integer(numDiaEntrada));
		        
		        if(result != null)
		        {
		            return (PromocaoDiaExecucao)result.clone();
		        }
		    }
		}
		
		return null;
	}

	/**
	 *	Este metodo retorna um objeto contendo o objeto PromocaoDiaExecucao para um dado valor de chave (id).
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		String					tipExecucao					Tipo de execucao da promocao.
	 *	@param		Integer					numDiaEntrada				Dia de entrada do assinante na promocao.
	 *	@return		PromocaoDiaExecucao									Objeto PromocaoDiaExecucao correspondente.
	 */
	public PromocaoDiaExecucao getPromocaoDiaExecucao(Integer idtPromocao, String tipExecucao, Integer numDiaEntrada)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		if((idtPromocao != null) && (numDiaEntrada != null))
		{
			Map mapPromocao = (Map)super.values.get(idtPromocao);
		
			if(mapPromocao != null)
			{
			    Map mapExecucao = (Map)mapPromocao.get(tipExecucao);
			    
			    if(mapExecucao != null)
			    {
			        PromocaoDiaExecucao result = (PromocaoDiaExecucao)mapExecucao.get(numDiaEntrada);
				
			        if(result != null)
			        {
			            return (PromocaoDiaExecucao)result.clone();
			        }
			    }
			}
		}
		
		return null;
	}

	/**
	 *	Este metodo retorna um objeto contendo o objeto PromocaoDiaExecucao para um dado valor de chave (id).
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		String					tipExecucao					Tipo de execucao da promocao.
	 *	@param		Date					datEntradaPromocao			Data de entrada do assinante na promocao.
	 *	@return		PromocaoDiaExecucao									Objeto PromocaoDiaExecucao correspondente.
	 */
	public PromocaoDiaExecucao getPromocaoDiaExecucao(Integer idtPromocao, String tipExecucao, Date datEntradaPromocao)
	{
		// Retorna o objeto contido no hash dado o valor correspondente da chave
		if((idtPromocao != null) && (datEntradaPromocao != null))
		{
			Map mapPromocao = (Map)super.values.get(idtPromocao);
		
			if(mapPromocao != null)
			{
			    Map mapExecucao = (Map)mapPromocao.get(tipExecucao); 
			    
			    if(mapExecucao != null)
			    {    
			        Calendar calDiaEntrada = Calendar.getInstance();
			        calDiaEntrada.setTime(datEntradaPromocao);
			        Integer numDiaEntrada = new Integer(calDiaEntrada.get(Calendar.DAY_OF_MONTH));
			        PromocaoDiaExecucao result = (PromocaoDiaExecucao)mapExecucao.get(numDiaEntrada);
			        
			        if(result != null)
			        {
			            return (PromocaoDiaExecucao)result.clone();
			        }
			    }
			}
		}
		
		return null;
	}

	/**
	 *	Retorna uma lista contendo os objetos PromocaoDiaExecucao para a promocao passada por parametro.
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		String					tipExecucao					Tipo de execucao da promocao.
	 *	@return		Collection				result						Lista de objetos PromocaoDiaExecucao.
	 */
	public Collection getPromocaoDiasExecucao(Integer idtPromocao, String tipExecucao)
	{
	    ArrayList result = new ArrayList();
	    
		if(idtPromocao != null)
		{
			Map mapPromocao = (Map)super.values.get(idtPromocao);
		
			if(mapPromocao != null)
			{
			    Map mapExecucao = (Map)mapPromocao.get(tipExecucao);
			    
			    if(mapExecucao != null)
			    {
			        for(Iterator iterator = mapExecucao.values().iterator(); iterator.hasNext();)
			        {
			            result.add(((PromocaoDiaExecucao)iterator.next()).clone());
			        }
			    }
			}
		}
		
		return result;
	}

	/**
	 *	Retorna uma lista contendo os objetos PromocaoDiaExecucao para a promocao passada por parametro.
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		Date					datEntradaPromocao			Tipo de execucao da promocao.
	 *	@return		Collection				result						Lista de objetos PromocaoDiaExecucao.
	 */
	public Collection getPromocaoDiasExecucao(Integer idtPromocao, Date datEntradaPromocao)
	{
	    ArrayList result = new ArrayList();
	    
		if((idtPromocao != null) && (datEntradaPromocao != null))
		{
			Map mapPromocao = (Map)super.values.get(idtPromocao);
		
			if(mapPromocao != null)
			{
			    for(Iterator iterator = mapPromocao.values().iterator(); iterator.hasNext();)
			    {
			        Map mapExecucao = (Map)iterator.next();
			        
			        Calendar calDiaEntrada = Calendar.getInstance();
			        calDiaEntrada.setTime(datEntradaPromocao);
			        Integer numDiaEntrada = new Integer(calDiaEntrada.get(Calendar.DAY_OF_MONTH));
			        
			        PromocaoDiaExecucao diaExecucao = (PromocaoDiaExecucao)mapExecucao.get(numDiaEntrada);
			        if(diaExecucao != null)
			        {
			            result.add(diaExecucao.clone());
			        }
			    }
			}
		}
		
		return result;
	}

	/**
	 *	Retorna o mapeamento do ultimo dia de execucao de recarga para a promocao passada por parametro.
	 *
	 *	@param		Integer					idtPromocao					Chave a ser utilizada para localizar a Promocao.
	 *	@param		String					tipExecucao					Tipo de execucao da promocao.
	 *	@return		PromocaoDiaExecucao		result						Lista de objetos PromocaoDiaExecucao.
	 */
	public PromocaoDiaExecucao getMaxDiaExecucaoRecarga(Integer idtPromocao, String tipExecucao)
	{
	    PromocaoDiaExecucao result = null;
	    
		if(idtPromocao != null)
		{
			Map mapPromocao = (Map)super.values.get(idtPromocao);
		
			if(mapPromocao != null)
			{
			    Map mapExecucao = (Map)mapPromocao.get(tipExecucao);
			    
			    if(mapExecucao != null)
			    {
			        PromocaoDiaExecucao diaExecucao = (PromocaoDiaExecucao)mapExecucao.get(MapPromocaoDiaExecucao.MAX_DIA_EXECUCAO_RECARGA);
			        
			        if(diaExecucao != null)
			        {
			            result = (PromocaoDiaExecucao)diaExecucao.clone();
			        }
			    }
			}
		}
		
		return result;
	}

}
