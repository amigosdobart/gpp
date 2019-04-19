package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.ServicoTecnomen;

/**
 *	Mapeamento da tabela TBL_APR_SERVICO_TECNOMEN, que define os servicos a serem ativados na plataforma Tecnomen. 
 *	Cada servico tem um mapeamento com a categoria de plano de preco do assinante, definindo quais os servicos que a  
 *	categoria pode possuir. Os servicos sao definidos no documento PP 4.4 Provisioning Server Interface 5035617_1.pdf 
 *	da Tecnomen.
 *	
 *	@version	1.0 
 *	@author		Daniel Ferreira
 */
public class MapServicoTecnomen extends Mapeamento 
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapServicoTecnomen instance;
	
	/**
	 *	Construtor da classe.
	 *	
	 *	@throws		GPPInternalErrorException
	 */
	private MapServicoTecnomen() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *	
	 *	@return		Instancia do singleton.
	 */
	public static MapServicoTecnomen getInstance()
	{
		try
		{
			if(MapServicoTecnomen.instance == null)
				MapServicoTecnomen.instance = new MapServicoTecnomen();
			
			return MapServicoTecnomen.instance;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load()
	 */
	protected void load() throws GPPInternalErrorException
	{
		PREPConexao	conexaoPrep	= null;
		ResultSet	registros	= null;
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery = 
				"SELECT idt_servico, " +
				"       nom_servico, " +
				"       tip_servico, " +
				"       idt_status_default, " +
				"       idt_status_servico_default " +
				"  FROM tbl_apr_servico_tecnomen ";
			
			registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				short idtServico				= registros.getShort("idt_servico");
				String nomServico				= registros.getString("nom_servico");
				short tipServico				= registros.getShort("tip_servico");
				short idtStatusDefault			= registros.getShort("idt_status_default");
				short idtStatusServicoDefault	= registros.getShort("idt_status_servico_default");
				
				ServicoTecnomen servico = new ServicoTecnomen(idtServico, 
															  nomServico, 
															  tipServico, 
															  idtStatusDefault, 
															  idtStatusServicoDefault);
				
				super.values.put(new Short(idtServico), servico);
			}
			
			registros.close();
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
	 *	Retorna objeto correspondente a um registro da tabela.
	 *
	 *	@param		idtServico				Identificador do servico da Tecnomen.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public ServicoTecnomen getServicoTecnomen(short idtServico)
	{
		return (ServicoTecnomen)super.values.get(new Short(idtServico));
	}
	
}
