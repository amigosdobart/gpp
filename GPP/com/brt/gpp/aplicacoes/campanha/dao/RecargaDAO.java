package com.brt.gpp.aplicacoes.campanha.dao;

import com.brt.gpp.aplicacoes.campanha.entidade.InfoRecarga;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class RecargaDAO
{
	private static final String SQL_SOMATORIO = "select count(1) as qtd_recargas, sum(vlr_pago) as vlr_recargas " +
	                                              "from tbl_rec_recargas " +
	                                             "where idt_msisdn      = ? " +
	                                               "and dat_origem     >= ? " +
	                                               "and dat_origem     <  ? " +
	                                               "and id_tipo_recarga = ?";
	
	/**
	 * Metodo....:getSomatorioRecargas
	 * Descricao.:Retorna o somatorio de recargas efetuadas por um assinante em um determinado periodo
	 * @param msisdn		- Msisdn do assinante
	 * @param dataInicial	- Data inicial a ser pesquisa as recargas
	 * @param dataFinal		- Data final a ser pesquisa as recargas
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada (null significa para buscar outra conexao no pool)
	 * @return InfoRecarga  - Object contendo as informacoes do somatorio de recargas
	 */
	public static InfoRecarga getSomatorioRecargas(String msisdn, Date dataInicial, Date dataFinal, PREPConexao conexaoPrep)
	{
		GerentePoolLog logger = GerentePoolLog.getInstancia(RecargaDAO.class);
		
		boolean conexaoPropria = false;
		InfoRecarga info = null;
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
			
			Object params[] = {msisdn, new Timestamp(dataInicial.getTime()), new Timestamp(dataFinal.getTime()), Definicoes.TIPO_RECARGA};
			ResultSet rs = conexaoPrep.executaPreparedQuery(SQL_SOMATORIO, params, 0);
			
			if (rs.next())
			{
				info = new InfoRecarga();
				info.setMsisdn			(msisdn);
				info.setQtdeRecargas	(rs.getLong("qtd_recargas"));
				info.setValorRecargas	(rs.getDouble("vlr_recargas"));
			}
		}
		catch(Exception e)
		{
			logger.log(0, Definicoes.ERRO, "RecargaDAO", "getSomatorioRecargas", "Erro");
		}
		finally
		{
			// Se entao o metodo buscou uma conexao no pool entao deve devolve-la
			if (conexaoPropria)
				GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep, 0);
		}
		return info;
	}
}
