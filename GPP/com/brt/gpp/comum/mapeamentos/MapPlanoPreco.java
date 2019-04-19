package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
 *
 *	Este arquivo contem a classe que faz o mapeamento dos planos de preco 
 *	existentes em memoria no GPP
 *	<P> Version:		1.0
 *
 *	@author 	Daniel Ferreira
 *	@since 		28/03/2006
 *
 *  Atualizado por: Bernardo Vergne Dias
 *  Data: 15/02/2007
 */

public final class MapPlanoPreco extends Mapeamento 
{
    
	private static MapPlanoPreco instance = null;	
	private static Map valores = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPlanoPreco () throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 * @return		Instancia do singleton.
	 */
	public static MapPlanoPreco getInstancia()  
	{
		return MapPlanoPreco.getInstance();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 * @return		Instancia do singleton.
	 */
	public static MapPlanoPreco getInstance()
	{
		if(MapPlanoPreco.instance == null)
		{
		    try
		    {
		    	MapPlanoPreco.instance = new MapPlanoPreco();
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
		}
			
		return MapPlanoPreco.instance;
	}
	
	/**
	 *	Carrega o mapeamento em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException 
	{
	    // Para o PlanoPreco do SAG. 
		// Nao pode ficar no construtor pois o super chama o load, entao ter-se-ah null_point_exception
		MapPlanoPreco.valores = Collections.synchronizedMap(new HashMap());

		PREPConexao conexaoPrep = null; 
					
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);

			String sqlQuery =	"SELECT idt_plano_preco, " 				+
								"       des_plano_preco, " 				+
								"       idt_categoria_asap, " 			+
								"       idt_plano_anatel, " 			+
								"       idt_categoria, " 				+
								"       idt_plano_preco_sag, " 			+
								"       idt_plano_tarifario_periodico " +
								"  FROM tbl_ger_plano_preco "			;
			
			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				PlanoPreco plano = new PlanoPreco();
			    
			    String idtPlanoPreco = registros.getString("idt_plano_preco");
			    plano.setIdtPlanoPreco(idtPlanoPreco);
			    
			    String desPlanoPreco = registros.getString("des_plano_preco");
			    plano.setDesPlanoPreco(desPlanoPreco);
				
			    String idtCategoriaAsap = registros.getString("idt_categoria_asap");
			    plano.setIdtCategoriaAsap(idtCategoriaAsap);

			    String idtPlanoPrecoSAG = registros.getString("idt_plano_preco_sag");
			    plano.setIdtPlanoPrecoSAG(idtPlanoPrecoSAG);
			    
			    short idtPlanoTarifarioPeriodico = registros.getShort("idt_plano_tarifario_periodico");
			    plano.setIdtPlanoTarifarioPeriodico(idtPlanoTarifarioPeriodico);
			    
			    int idtPlanoAnatel = registros.getInt("idt_plano_anatel");
			    plano.setIdtPlanoAnatel(registros.wasNull() ? null : new Integer(idtPlanoAnatel));
			    
			    plano.setCategoria(MapCategoria.getInstance().getCategoria(registros.getInt("idt_categoria")));
			    
			    super.values.put(idtPlanoPreco, plano);
			    
			    // Indexa pelo PlanoPreco do SAG.
			    if((idtPlanoPrecoSAG != null) && (!idtPlanoPrecoSAG.equals("")))
				    	MapPlanoPreco.valores.put(idtPlanoPrecoSAG, plano);
			}

			registros.close();
			registros = null;
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException ("Excecao: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}

	/**
	 *	Retorna objeto correspondente a um registro da tabela.
	 *
	 *	@param		idtPlanoPreco			Identificador do plano de preco.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public PlanoPreco getPlanoPreco(int idtPlanoPreco)
	{
		return (PlanoPreco)super.values.get(String.valueOf(idtPlanoPreco));
	}
	
   /**
	*	Retorna a descricao do plano de preco.
	* 
	*	@param		String					idtPlanoPreco				Identificador do plano de preco.
	*	@return		String 												Descricao do plano de preco. 
	*/
	public String getMapDescPlanoPreco(String idtPlanoPreco)
	{
	    PlanoPreco plano = (PlanoPreco)super.get(new Object[]{idtPlanoPreco});
	    
	    if(plano != null)
	        return plano.getDesPlanoPreco();
	    
	    return null;
	}
	
   /**
	* Descricao: Esse metodo retorna se um plano de preco passado como parametro e hibrido ou nao.
	* 
	*	@param		String					idtPlanoPreco				Identificador do plano de preco.
	*	@return		String 												1 se o plano for hibrido e 0 caso contrario. 
	*/
	public String getMapHibrido(String idtPlanoPreco)
	{
	    PlanoPreco plano = (PlanoPreco)super.get(new Object[]{idtPlanoPreco});

	    if((plano != null) && (plano.getIdtCategoria().intValue() == Definicoes.CATEGORIA_HIBRIDO))
	        return "1";
	    
	    return "0";
	}
	
	/**
	 *	Consulta a categoria do assinante a partir do codigo do plano de preco.
	 * 
	 *	@param		int						idtPlanoPreco				Identificador do plano de preco.
	 *	@return		int													Categoria do plano de preco.
	 */
	public int consultaCategoria(int idtPlanoPreco)
	{
	    PlanoPreco plano = (PlanoPreco)super.get(new Object[]{String.valueOf(idtPlanoPreco)});
	    
	    if(plano != null)
	        return plano.getIdtCategoria().intValue();
	    
	    return -1;
	}

	/**
	 *	Retorna objeto correspondente a um registro da tabela com base no PlanoPreco do SAG.
	 *
	 *	@param		idtPlanoPrecoSAG			Identificador do plano de preco do SAG.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public PlanoPreco getPlanoPreco(String idtPlanoPrecoSAG)
	{
		return (PlanoPreco)MapPlanoPreco.valores.get(idtPlanoPrecoSAG);
	}

}