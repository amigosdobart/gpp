package com.brt.gpp.aplicacoes.campanha.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import com.brt.gpp.aplicacoes.campanha.entidade.CondIncentivoRecargas;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class CondIncentivoRecargasDAO 
{
	private final static String SQL_COND = "SELECT VLR_RECARGA " +
	                                              ",VLR_BONUS " +
												  ",VLR_BONUS_SM " +
												  ",VLR_BONUS_DADOS " +
											  "FROM TBL_CAM_COND_INCENT_RECARGAS " +
											 "WHERE ID_CAMPANHA = ?";
	
	private final static String SQL_PESQ_SOMA = "select vlr_recarga, vlr_bonus, vlr_bonus_sm, vlr_bonus_dados " +
												  "from (select vlr_recarga " +
												              ",lead(vlr_recarga, 1, 0) over (order by vlr_recarga) as prox " +
												              ",vlr_bonus " +
												              ",vlr_bonus_sm " +
												              ",vlr_bonus_dados " +
												          "from tbl_cam_cond_incent_recargas " + 
												         "where id_campanha = ?) " +
												 "where (? >= vlr_recarga and ? < prox) or (? >= vlr_recarga and prox = 0)";
	
	/**
	 * Metodo....:getCondIncentivoRecargas
	 * Descricao.:Retorna o objeto contendo os valores de concessao de credito para a campanha
	 *            de incentivo de recargas
	 * @param rs - ResultSet
	 * @return CondIncentivoRecargas
	 * @throws SQLException
	 */
	private static CondIncentivoRecargas getCondIncentivoRecargas(ResultSet rs) throws SQLException
	{
		CondIncentivoRecargas cond = new CondIncentivoRecargas();
		cond.setValorRecarga	(rs.getDouble("VLR_RECARGA")	);
		cond.setValorBonus		(rs.getDouble("VLR_BONUS")		);
		cond.setValorBonusSM	(rs.getDouble("VLR_BONUS_SM")	);
		cond.setValorBonusDados	(rs.getDouble("VLR_BONUS_DADOS"));

		return cond;
	}
	
	/**
	 * Metodo.....:getCondsIncentivoRecargas
	 * Descricao.:Retorna a lista de valores de condicoes de aplicacao da 
	 * CondicaoConcessao para a campanha de incentivo a recargas
	 * @param campanha
	 * @return Collection
	 */
	public static Collection getCondsIncentivoRecargas(Campanha campanha) 
	{
		// Define a variavel que irah armazenar a lista de valores contendo
		// as condicoes de concessao de creditos para a campanha
		Collection conds = new TreeSet();
		
		long idProcesso = GerentePoolLog.getInstancia(CondIncentivoRecargasDAO.class).getIdProcesso("CondIncentivoRecargasDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep    = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			Object param[] = {new Long(campanha.getId())};
			ResultSet rs   = conexaoPrep.executaPreparedQuery(SQL_COND,param,idProcesso);
			while (rs.next())
				conds.add(getCondIncentivoRecargas(rs));
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(CondIncentivoRecargasDAO.class).log(idProcesso,Definicoes.ERRO
					                                          ,"CondIncentivoRecargasDAO"
					                                          ,"getCondsIncentivoRecargas"
					                                          ,"Erro ao pesquisar condicoes de concessao para a campanha de incentivo de recargas. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return conds;
	}
	
	/**
	 * Metodo....:getCondsIncentivoRecargas
	 * Descricao.:Retorna as condicoes de incentivo de recargas
	 *            em um mapeamento onde a chave eh o valor de
	 *            recarga
	 * @param campanha - Campanha a ser pesquisada os valores de bonus
	 * @return Map - Mapeamento contendo os valores
	 */
	public static Map getCondsIncentivoRecargasMap(Campanha campanha)
	{
		Map conds = new HashMap();
		for (Iterator i = getCondsIncentivoRecargas(campanha).iterator(); i.hasNext();)
		{
			CondIncentivoRecargas cond = (CondIncentivoRecargas)i.next();
			conds.put(new Double(cond.getValorRecarga()),cond);
		}
		return conds;
	}
	
	/**
	 * Metodo....:getCondIncentivoRecargasAprox
	 * Descricao.:Retorna a condicao de recarga mais aproximada do valor
	 *            de somatorio informado como parametro.
	 *            EX: Lista de valores recargas
	 *                20
	 *                30
	 *                50
	 *                
	 *                Soma de recargas,  Valor correspondente
	 *                15					nenhum
	 *                25					20
	 *                30					30
	 *                acima 50				50
	 * @param campanha		- Campanha a ser pesquisada os valores
	 * @param valor			- Somatorio de recargas a ser utilizado como parametro
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @return CondIncentivoRecargas - Condicao correspondente aproximada ao valor do somatorio
	 */
	public static CondIncentivoRecargas getCondIncentivoRecargasAprox(Campanha campanha, double valor, PREPConexao conexaoPrep)
	{
		GerentePoolLog logger = GerentePoolLog.getInstancia(CondIncentivoRecargasDAO.class);
		
		boolean conexaoPropria = false;
		CondIncentivoRecargas cond = null;
		try
		{
			// Verifica se o parametro de conexao de banco de dados estah diferente
			// de nulo. Caso nulo o metodo entao busca uma conexao no pool para ser
			// utilizada. Como o metodo realiza somente uma pesquisa nenhuma preocupacao
			// com a transacao eh feita neste lugar.
			if (conexaoPrep == null)
			{
				conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
				conexaoPropria = true;
			}
			
			Object params[] = {new Long(campanha.getId()), new Double(valor), new Double(valor), new Double(valor)};
			ResultSet rs = conexaoPrep.executaPreparedQuery(SQL_PESQ_SOMA, params, 0);
			
			if (rs.next())
				cond = getCondIncentivoRecargas(rs);
		}
		catch(Exception e)
		{
			logger.log(0, Definicoes.ERRO, "CondIncentivoRecargasDAO", "getCondIncentivoRecargasAprox", "Erro ao pesquisar informacoes de bonificacao por valor de recarga:"+e);
		}
		finally
		{
			// Se entao o metodo buscou uma conexao no pool entao deve devolve-la
			if (conexaoPropria)
				GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep, 0);
		}
		return cond;
	}
}
