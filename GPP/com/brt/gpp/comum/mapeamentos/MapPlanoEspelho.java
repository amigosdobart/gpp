package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//Imports GPP.

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoEspelho;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
  *	Este arquivo contem a classe que faz o mapeamento dos planos espelhos de cada plano em relacao a um certo tipo de
  *	espelhamento.
  *
  *	@author		Daniel Ferreira
  *	@since 		03/10/2005
  */
public final class MapPlanoEspelho extends Mapeamento
{

	private static MapPlanoEspelho instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPlanoEspelho() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Cria uma instancia ou refere-se a instancia ja existente.
	 *
	 *	@return     Mapeamento da TBL_GER_PLANO_ESPELHO.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPlanoEspelho getInstancia() throws GPPInternalErrorException 
	{
		if(MapPlanoEspelho.instance == null) 
		{
		    MapPlanoEspelho.instance = new MapPlanoEspelho();
		}
		
		return MapPlanoEspelho.instance;
	}	

	/**
	 *	Carrega o mapeamento em memoria.
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
			//Seleciona planos de precos validos no BD do GPP
			String sqlQuery = "SELECT TIP_ESPELHAMENTO, " +
			                  "       IDT_PLANO_PRECO, " +
						      "       IDT_PLANO_BASE, " +
						      "       IDT_PLANO_ESPELHO, " +
						      "       IDT_PLANO_ASSINANTE " +
						      "FROM TBL_GER_PLANO_ESPELHO ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null,0);
			
			while (registros.next())
			{
				PlanoEspelho plano = new PlanoEspelho();
				
				String tipEspelhamento = registros.getString("TIP_ESPELHAMENTO");
				plano.setTipEspelhamento(tipEspelhamento);
				
				String idtPlanoPreco = registros.getString("IDT_PLANO_PRECO");
				plano.setIdtPlanoPreco(idtPlanoPreco);
				
				String idtPlanoBase = registros.getString("IDT_PLANO_BASE");
				plano.setIdtPlanoBase(idtPlanoBase);
				
				String idtPlanoEspelho = registros.getString("IDT_PLANO_ESPELHO");
				plano.setIdtPlanoEspelho(idtPlanoEspelho);
                
                String idtPlanoAssinante = registros.getString("IDT_PLANO_ASSINANTE");
                // Se tem caracter coringa de Categoria de Plano
                if(idtPlanoAssinante.indexOf(PlanoEspelho.CORINGA_CATEGORIA_DE_PLANO)!=-1)
                {
                	// Criar a estrutura de armazenamento.
                	StringBuffer planoAssinanteExpandido = new StringBuffer();
                	
                	MapCategoria mapCategoria = MapCategoria.getInstance();
                    String opcoes[] = idtPlanoAssinante.split(";");
                    for(int i = 0; i < opcoes.length; i++)
                    {
                    	String opcao = opcoes[i];
                    	// Se tem o caracter coringa na dado opcao
                    	int posicaoCorigaCategoria = opcao.indexOf(PlanoEspelho.CORINGA_CATEGORIA_DE_PLANO);
                    	if(posicaoCorigaCategoria!=-1)
                    	{
                    		int idCategoria  = -1;
                    		try
                    		{
                        		idCategoria = Integer.parseInt(opcao.substring(posicaoCorigaCategoria+1));
                    		}
                    		catch(Exception e)
                    		{
                    			throw new GPPInternalErrorException("Configuracao da Tabela de espelhamento errada."+tipEspelhamento+";idtPlanoAssinante."
                    					+e.getMessage());
                    		}
                			Categoria categoria = mapCategoria.getCategoria(idCategoria);
                    		Collection planos = mapCategoria.getPlanoDeCategoria(categoria);
                    		for (Iterator iPlano = planos.iterator(); iPlano.hasNext(); )
                    		{
                        		PlanoPreco tmpPlano = (PlanoPreco)iPlano.next();
                        		planoAssinanteExpandido.append(tmpPlano.getIdtPlanoPreco());
                        		planoAssinanteExpandido.append(";");
                    		}
                    	}
                    	else
                    	{// Caso nao tenha o caracater coringa de Categoria de plano, coloca o original
                    		planoAssinanteExpandido.append(opcao);
                    		planoAssinanteExpandido.append(";");
                    	}
                    }
                }
                plano.setIdtPlanoAssinante(idtPlanoAssinante);
				
				Map mapEspelhamento = (Map)super.values.get(tipEspelhamento);
				if(mapEspelhamento == null)
				{
				    mapEspelhamento = Collections.synchronizedMap(new HashMap());
					super.values.put(tipEspelhamento, mapEspelhamento);
				}
                
                Map mapPlanoPreco = (Map)mapEspelhamento.get(idtPlanoPreco);
                if(mapPlanoPreco == null)
                {
                    mapPlanoPreco = Collections.synchronizedMap(new HashMap());
                    mapEspelhamento.put(idtPlanoPreco, mapPlanoPreco);
                }
                
				mapPlanoPreco.put(idtPlanoAssinante, plano);
			}
			
			registros.close();
			registros = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Este metodo retorna o objeto PlanoEspelho para um dado valor de chave (id).
	 *
     *  @param      tipoEspelhamento        Tipo de espelhamento que define o calculo dos planos.
	 *	@param      idtPlanoPreco			Identificador do plano de preco informado pelo processo.
     *  @param      idtPlanoAssinante       Identificador do plano de preco do assinante.
	 *	@return     Objeto PlanoEspelho correspondente.
     *  @throws     Exception
	 */
	public PlanoEspelho getPlanoEspelho(String tipEspelhamento, String idtPlanoPreco, String idtPlanoAssinante) throws Exception
	{
		Map mapEspelhamento = (Map)super.values.get(tipEspelhamento);
		
		if(mapEspelhamento != null)
		{
            Map mapPlanoPreco = (Map)mapEspelhamento.get(idtPlanoPreco);
            
            //Se nao retornar espelhamento para o plano informado, verificar se ha espelhamento que nao necessite de
            //informacao de plano de preco.
            if(mapPlanoPreco == null)
                mapPlanoPreco = (Map)mapEspelhamento.get(PlanoEspelho.CORINGA);
            
            if(mapPlanoPreco != null)
            {
                for(Iterator iterator = mapPlanoPreco.values().iterator(); iterator.hasNext();)
                {
                    PlanoEspelho result = (PlanoEspelho)iterator.next();
                    
                    if(result.matches(idtPlanoAssinante))
                        return (PlanoEspelho)result.clone();
                }
            }
            
		}
		
		return null;
	}

}
