package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Mapeamento de registros da tabela TBL_PRO_LIMITE_SEGUNDOS em memoria.
 *
 *	@author João Paulo Galvagni
 *  @since  31/05/2007
 */
public final class MapPromocaoLimiteSegundos extends Mapeamento
{
	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocaoLimiteSegundos instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoLimiteSegundos() throws GPPInternalErrorException 
	{
	    super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return	 Mapeamento da TBL_PRO_LIMITE_PROMOCAO.
	 *	@throws	 GPPInternalErrorException
	 */
	public static MapPromocaoLimiteSegundos getInstance() throws GPPInternalErrorException 
	{
		if(MapPromocaoLimiteSegundos.instance == null) 
			MapPromocaoLimiteSegundos.instance = new MapPromocaoLimiteSegundos();
		
		return MapPromocaoLimiteSegundos.instance;
	}
	
	/**
	 * Metodo....: load
	 * Descricao.: Carrega o mapeamento em memoria
	 * 
	 * @throws GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			//Obtendo conexao com o banco de dados.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			MapPromocao mapPromocao = MapPromocao.getInstancia();
			
			String sqlQuery = "SELECT idt_promocao,				" +
							  "		  idt_limite, 				" +
							  "	      num_segundos,				" +
							  "		  ind_limite_maximo 		" +
							  "  FROM tbl_pro_limite_segundos	" ;
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(rs.next())
			{
				PromocaoLimiteSegundos limite = new PromocaoLimiteSegundos();
				
				Integer idtPromocao = new Integer(rs.getInt("idt_promocao"));
				int idLimite = rs.getInt("idt_limite");
				limite.setIdLimite(idLimite);
				long numSegundos = rs.getLong("num_segundos");
				limite.setNumSegundos(numSegundos);
				int indLimiteMaximo = rs.getInt("ind_limite_maximo");
				if (indLimiteMaximo == 1)
					limite.setLimiteMaximo(true);
				else
					limite.setLimiteMaximo(false);
				
				limite.setPromocao(mapPromocao.getPromocao((idtPromocao)));
				
				Collection limites = (Collection)super.values.get(idtPromocao);
				if (limites == null)
					limites = new TreeSet();
				
				limites.add(limite);
				super.values.put(idtPromocao, limites);
			}
			
			rs.close();
			rs = null;
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
	 * Metodo....: getLimiteMaximo
	 * Descricao.: Retorna o limite maximo da promocao informada
	 * 
	 * @param idPromocao - Identificador da promocao
	 * @return long		 - Limite maxim
	 */
	public long getLimiteMaximo(int idPromocao)
	{
		Collection limites = (Collection)super.values.get(new Integer(idPromocao));
		
		if (limites != null)
			for (Iterator i = limites.iterator(); i.hasNext();)
			{
				PromocaoLimiteSegundos pLimite = (PromocaoLimiteSegundos)i.next();
				if (pLimite.isLimiteMaximo())
					return pLimite.getNumSegundos();
			}
		
		return 0;
	}
	
	/**
	 * Metodo....: getIdLimiteMaximo
	 * Descricao.: Retorna ID do limite maximo da promocao informada
	 * 
	 * @param idPromocao - Identificador da promocao
	 * @return long		 - Limite maxim
	 */
	public long getIdLimiteMaximo(int idPromocao)
	{
		Collection limites = (Collection)super.values.get(new Integer(idPromocao));
		
		if (limites != null)
			for (Iterator i = limites.iterator(); i.hasNext();)
			{
				PromocaoLimiteSegundos pLimite = (PromocaoLimiteSegundos)i.next();
				if (pLimite.isLimiteMaximo())
					return pLimite.getIdLimite();
			}
		
		return 0;
	}
	
	/**
	 * Metodo....: getLimitesByIdPromocao
	 * Descricao.: Retorna a lista dos limites para a promocao em questao 
	 * 
	 * @param  idPromocao	- Id da promocao
	 * @return TreeSet		- Lista dos limites ordenada
	 */
	public TreeSet getLimitesByIdPromocao(int idPromocao)
	{
		return (TreeSet)super.values.get(new Integer(idPromocao));
	}
	
	/**
	 * Metodo....: getPromocaoLimiteSegundos
	 * Descricao.; Retorna a lista com o historico dos valores do limite da promocao.
	 * 
	 * @param  idtPromocao - Identificador da promocao
	 * @return	Objeto Promocao correspondente
	 */
	public Promocao getPromocaoLimiteSegundos(int idtPromocao) throws GPPInternalErrorException
	{
	    MapPromocao mapPromocao = MapPromocao.getInstancia();
		
		return mapPromocao.getPromocao(new Integer(idtPromocao));
	}
}