package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
 *	Mapeamento da tabela TBL_GER_CATEGORIA, que corresponde a definicao das categorias de planos.
 * 
 *	@author		Daniel Ferreira
 *	@since		15/03/2007
 *
 *	@version	1.1
 *	@author		Vitor Murilo Dias
 *	@since		30/03/2008
 */
public class MapCategoria extends Mapeamento
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapCategoria instance;
	private static MapCategoriaTEC mapCategoriaTEC;
	private static MapSaldoCategoria mapTiposSaldo;
	private static MapServicoTECCategoria mapServicosTecnomen;
	private static MapOrigemCategoria mapOrigensRecarga;
	
	/**
	 *	Construtor da classe.
	 */
	private MapCategoria() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static synchronized MapCategoria getInstance()
	{
		try
		{
			if(MapCategoria.instance == null)
			{
				MapCategoria.instance = new MapCategoria();
				MapCategoria.loadRelacionamentosCategoria();		
			}
			
			return MapCategoria.instance;
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
		PREPConexao conexaoPrep = null;
		mapCategoriaTEC = MapCategoriaTEC.getInstance();
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery =
				"SELECT idt_categoria, " +
				"       des_categoria, " +
				"       idt_categoria_tec, " +
				"       des_mascara_msisdn, " +
				"		ind_possui_imsi, " +
				"		ind_possui_ath, " +
				"       idt_status_periodico_ativacao, " +
				"       IND_POSSUI_STATUS_PERIODICO    " +
				"  FROM tbl_ger_categoria ";
			
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				int				idtCategoria				= registros.getInt("idt_categoria");
				String			desCategoria				= registros.getString("des_categoria");
				String			desMascaraMsisdn			= registros.getString("des_mascara_msisdn");
				boolean			possuiImsi					= (registros.getInt("ind_possui_imsi") != 0);
				boolean			possuiAth					= (registros.getInt("ind_possui_ath") != 0);
				short			idtStatusPeriodicoAtivacao	= registros.getShort("idt_status_periodico_ativacao");
				boolean			possuiStatusPeriodico		= (registros.getInt("IND_POSSUI_STATUS_PERIODICO") != 0);
				
				CategoriaTEC categoriaTEC = mapCategoriaTEC.getCategoriaTEC(registros.getShort("idt_categoria_tec"));
				Categoria categoria = new Categoria(idtCategoria, 
													desCategoria,
													categoriaTEC,
													desMascaraMsisdn,
													possuiImsi,
													possuiAth,
													idtStatusPeriodicoAtivacao,
													Collections.synchronizedCollection(new ArrayList())/*tiposSaldo*/,
													Collections.synchronizedCollection(new ArrayList())/*servicosTecnomen*/,
													Collections.synchronizedCollection(new ArrayList())/*origensRecarga*/,
													Collections.synchronizedCollection(new ArrayList()),
													possuiStatusPeriodico);
				
				super.values.put(new Integer(idtCategoria), categoria);
			}
			
			registros.close();
			//Obtendo o mapeamento de categorias novas categorias permitidas em uma troca de plano para cada categoria.
			this.obterNovasCategorias(conexaoPrep);
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
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#reload()
	 */
	public void reload() throws GPPInternalErrorException
	{
		super.values.clear();
		this.load();
		MapCategoria.loadRelacionamentosCategoria();
	}

	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#refresh
	 */
	public void refresh() throws GPPInternalErrorException
	{
		this.load();
		MapCategoria.loadRelacionamentosCategoria();
	}

	/**
	 * Adquire todas os planos de uma dada categoria
	 * @param categoria
	 * @return
	 */
	public Collection getPlanoDeCategoria(Categoria categoria)
	{
		ArrayList saida = new ArrayList();
		MapPlanoPreco map= MapPlanoPreco.getInstance();
		Iterator planosPreco =map.getAll().iterator();
		for (Iterator iPlano = planosPreco; iPlano.hasNext(); )
		{
			PlanoPreco tmpPlano = (PlanoPreco)iPlano.next();;
			if (tmpPlano.getIdtCategoria().intValue()== categoria.getIdCategoria())
			{
				saida.add(tmpPlano);
			}
		}
		return saida;
	}
	
	/**
	 * Carrega o mapeamento MapSaldoCategoria.
	 */
	private static void loadRelacionamentosCategoria()
	{
		mapTiposSaldo = MapSaldoCategoria.getInstance();
		mapServicosTecnomen	= MapServicoTECCategoria.getInstance();
		mapOrigensRecarga = MapOrigemCategoria.getInstance();
		
		for (Iterator i=MapCategoria.getInstance().values.keySet().iterator();i.hasNext();)
		{
			Integer chave = (Integer)i.next();
			Categoria categoria = (Categoria)MapCategoria.getInstance().values.get(chave);
			
			Collection tiposSaldo = mapTiposSaldo.getTiposSaldo(chave.intValue());
			Collection origensRecarga = mapOrigensRecarga.getListaOrigensRecarga(chave.intValue());
			Collection servicosTecnomen	= mapServicosTecnomen.getServicosTecnomen(chave.intValue());
			
			categoria.setTiposSaldo(tiposSaldo);
			categoria.setOrigensRecarga(origensRecarga);
			categoria.setServicosTecnomen(servicosTecnomen);
		}
	}

	/**
	 *	Carrega o mapeamento de novas categorias permitidas para cada categoria em uma troca de plano.
	 *
	 *	@param		conexaoPrep				Conexao com o banco de dados.		
	 */
	private void obterNovasCategorias(PREPConexao conexaoPrep)
	{
		try
		{
			String sqlQuery = 
				"SELECT idt_categoria, " +
				"       idt_categoria_nova " +
				"  FROM tbl_apr_troca_categoria " +
				" ORDER by idt_categoria, idt_categoria_nova ";
			
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				Categoria categoria = this.getCategoria(registros.getInt("idt_categoria"));
				Collection novasCategorias = categoria.getNovasCategorias();
				novasCategorias.add(this.getCategoria(registros.getInt("idt_categoria_nova")));
			}
			
			registros.close();
		}
		catch(Exception ignore){}
	}
	
	/**
	 *	Retorna objeto correspondente a um registro da tabela.
	 *
	 *	@param		idtCategoria			Identificador da categoria de plano de preco.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public Categoria getCategoria(int idtCategoria)
	{
		return (Categoria)super.values.get(new Integer(idtCategoria));
	}
	
}
