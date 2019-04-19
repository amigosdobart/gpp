package com.brt.gpp.aplicacoes.campanha.recargaExpressa;

import java.sql.ResultSet;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class RecargaExpressaDAO {
	
	private GerentePoolLog log;
	
	/**
	 * Metodo....:fezRecargaUltimosDias
	 * Descricao.:Chama o método buscaRecargasUltimosDias para retornar as recargas do assinante
	 * @param msisdn 		- Msisdn a ser pesquisado
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @param numDias		- Quantidade de dias a pesquisar
	 * @throws Exception
	 */
	protected ResultSet fezRecargaUltimosDias(AssinanteCampanha assinante, PREPConexao conexaoPrep, int numDias)
	{
		return buscaRecargasUltimosDias(assinante.getMsisdn(),conexaoPrep,numDias);
	}
	
	/**
	 * Metodo....:fezRecargaUltimosDias
	 * Descricao.:Método sobrescrito fezRecargaUltimosDias
	 * @param msisdn 		- Msisdn a ser pesquisado
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @param numDias		- Quantidade de dias a pesquisar
	 * @throws Exception
	 */
	protected ResultSet fezRecargaUltimosDias(Assinante assinante, PREPConexao conexaoPrep, int numDias)
	{
		return buscaRecargasUltimosDias(assinante.getMSISDN(),conexaoPrep,numDias);
	}
	
	/**
	 * Metodo....:buscaRecargasUltimosDias
	 * Descricao.:Retorna um resultset com as recargas dos ultimos dias especificados
	 * @param msisdn 		- Msisdn a ser pesquisado
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @param numDias		- Quantidade de dias a pesquisar
	 * @throws Exception
	 */
	protected ResultSet buscaRecargasUltimosDias(String msisdn, PREPConexao conexaoPrep, int numDias)
	{
		ResultSet rs 		= null;
		String SQL_PESQ		= 	"SELECT  r.idt_msisdn msisdn, r.id_tipo_recarga tipo_recarga, " +
		  						"        r.vlr_pago valor " +
		  						"   FROM tbl_rec_recargas r " +
		  						"  WHERE r.idt_msisdn = ? " +
		  						"    AND r.id_tipo_recarga = ? " +
		  						"    AND r.dat_origem >= TRUNC(SYSDATE - ?) " +
		  						"ORDER BY dat_origem";
		
		try {
			Object param[] = {msisdn
							,Definicoes.TIPO_RECARGA
							,new Integer(numDias)
							};
			rs = conexaoPrep.executaPreparedQuery(SQL_PESQ,param,0);
		}
		catch (Exception e) {
			log.log(0,Definicoes.ERRO,"RecargaExpressaDAO","fezRecargaUltimosDias"
					 ,"Erro ao verificar se o assinante possui recarga. Erro:"+e);
		}
		return rs;
	}
}
