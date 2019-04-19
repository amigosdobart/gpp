package com.brt.gpp.aplicacoes.campanha.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.brt.gpp.aplicacoes.campanha.entidade.ParamIncentivoRecargas;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ParamIncentivoRecargasDAO 
{
	private final static String SQL_PARAM = "SELECT IDT_STATUS_ASSINANTE " +
	                                              ",NUM_DIAS_EXPIRACAO_INI " +
	                                              ",NUM_DIAS_EXPIRACAO_FIM " +
	                                              ",IND_FEZ_ROLLOUT " +
	                                          "FROM TBL_CAM_PARAM_INCENT_RECARGAS " +
	                                         "WHERE ID_CAMPANHA = ?";
	
	/**
	 * Metodo....:getParamIncentivoRecargas
	 * Descricao.:Retorna o objeto contendo os valores de parametros para a campanha
	 *            de incentivo de recargas
	 * @param rs - ResultSet
	 * @return ParamIncentivoRecargas
	 * @throws SQLException
	 */
	private static ParamIncentivoRecargas getParamIncentivoRecargas(ResultSet rs) throws SQLException
	{
		ParamIncentivoRecargas param = new ParamIncentivoRecargas();
		param.setDiasExpiracaoIni	(rs.getInt("NUM_DIAS_EXPIRACAO_INI"));
		param.setDiasExpiracaoFim	(rs.getInt("NUM_DIAS_EXPIRACAO_FIM"));
		param.setStatusAssinante	(rs.getInt("IDT_STATUS_ASSINANTE"));
		param.setFezRollOut			(rs.getInt("IND_FEZ_ROLLOUT") == 1 ? true : false);
		
		return param;
	}

	/**
	 * Metodo.....:geParamsIncentivoRecargas
	 * Descricao..:Retorna uma lista de parametros contendo os valores necessarios para 
	 *             aplicacao do parametro de incentivo de recargas
	 * @param campanha
	 * @return Collection
	 */
	public static Collection getParamsIncentivoRecargas(Campanha campanha) 
	{
		// Define a variavel que irah armazenar a lista de valores contendo
		// os parametros para a aplicacao do parametro para a campanha
		// de incentivo de recargas
		Collection params = new ArrayList();
		
		long idProcesso = GerentePoolLog.getInstancia(ParamIncentivoRecargasDAO.class).getIdProcesso("ParamIncentivoRecargasDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep    = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			Object param[] = {new Long(campanha.getId())};
			ResultSet rs   = conexaoPrep.executaPreparedQuery(SQL_PARAM,param,idProcesso);
			while (rs.next())
				params.add(getParamIncentivoRecargas(rs));
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(ParamIncentivoRecargasDAO.class).log(idProcesso,Definicoes.ERRO
					                                          ,"ParamIncentivoRecargasDAO"
					                                          ,"getParamsIncentivoRecargas"
					                                          ,"Erro ao pesquisar parametros para a campanha de incentivo de recargas. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return params;
	}
}
