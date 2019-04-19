package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapServicoTecnomen;
import com.brt.gpp.comum.mapeamentos.entidade.ServicoTecnomen;

/**
 *	Mapeamento da tabela TBL_APR_SERVICO_TEC_CATEGORIA. Esta tabela define os servicos a serem ativados para cada 
 *	categoria de plano de preco. Ex: Os planos GSM podem ter os servicos de Evento e Dados. Os planos da Fixa podem 
 *	possuir somente eventos de voz.
 *
 * 	@version	1.0		Versao inicial.  
 *	@author		Daniel Ferreira
 */
public class MapServicoTECCategoria extends Mapeamento
{

	/**
	 *	Instancia do singleton. 
	 */
	private static MapServicoTECCategoria instance;
	
	/**
	 *	Construtor da classe. 
	 */
	private MapServicoTECCategoria() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 * 	@return		Instancia do singleton. 
	 */
	public static MapServicoTECCategoria getInstance()
	{
		try
		{
			if(MapServicoTECCategoria.instance == null)
				MapServicoTECCategoria.instance = new MapServicoTECCategoria();
			
			return MapServicoTECCategoria.instance;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load() 
	 */
	protected void load() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery = 
				"SELECT idt_categoria, " +
				"       idt_servico " +
				"  FROM tbl_apr_servico_tec_categoria ";
			
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				Integer idtCategoria = new Integer(registros.getInt("idt_categoria"));
				ServicoTecnomen servico = MapServicoTecnomen.getInstance().getServicoTecnomen(registros.getShort("idt_servico"));
				
				ArrayList servicosTecnomen = (ArrayList)super.values.get(idtCategoria);
				if(servicosTecnomen == null)
				{
					servicosTecnomen = new ArrayList();
					super.values.put(idtCategoria, servicosTecnomen);
				}
				
				servicosTecnomen.add(servico);
			}
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 * 	Retorna os servicos a serem ativados na Tecnomen associados a categoria de planos de preco.
	 * 
	 * 	@param		idtCategoria			Identificador da categoria de planos.
	 *  @return		Servidos a serem ativados na Tecnomen associados a categoria.
	 */
	public Collection getServicosTecnomen(int idtCategoria)
	{
		Collection result = (Collection)super.values.get(new Integer(idtCategoria));
		
		return (result != null) ? result : new ArrayList();
	}
	
}
