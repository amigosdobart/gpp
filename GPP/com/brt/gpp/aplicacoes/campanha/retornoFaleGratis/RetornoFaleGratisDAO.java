package com.brt.gpp.aplicacoes.campanha.retornoFaleGratis;

import java.sql.ResultSet;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe responsavel por consultar a condicao para que
 * o assinante receba o SMS apos retornar para a Promocao
 * Fale de Graca a Noite
 * 
 * @author Joao Paulo Galvagni
 * @since  25/09/2007
 */
public class RetornoFaleGratisDAO
{
	private GerentePoolLog log;
	
	/**
	 * Metodo....: statusAssinanteAtivo
	 * Descricao.: Realiza uma consulta na tabela de Aprovisionamento,
	 * 			   considerando o status do assinante Normal
	 * 
	 * @param  assinante	- Instancia de <code>AssinanteCampanha</code>
	 * @param  conexaoPrep	- Conexao com o Banco de Dados
	 * @return rs			- Retorna o resultado preenchido apenas se assinante 
	 * 						  estiver com status NormalUser
	 */
	protected ResultSet statusAssinanteAtivo(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		ResultSet rs = null;
		
		String SQL_PESQ = "SELECT 1						" +
						  "  FROM TBL_APR_ASSINANTE A 	" +
						  " WHERE A.IDT_MSISDN = ? 		" +
						  "   AND A.IDT_STATUS = ? 		" ;
		
		try
		{
			// Criacao dos parametros
			Object param[] = {assinante.getMsisdn(), 
							  new Integer(Definicoes.STATUS_NORMAL_USER) };
			
			// Execucao da consulta
			return conexaoPrep.executaPreparedQuery(SQL_PESQ, param, 0);
		}
		catch (Exception e)
		{
			log.log(0, Definicoes.ERRO, "RetornoFaleGratisDAO", "statusAssinanteAtivo", e.getMessage());
		}
		
		// Retorna false caso o assinante nao esteja com status Normal User
		return rs;
	}
}