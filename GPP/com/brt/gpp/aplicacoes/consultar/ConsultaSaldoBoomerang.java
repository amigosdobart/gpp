package com.brt.gpp.aplicacoes.consultar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoBoomerang.GerarExtratoBoomerang;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang;

import java.util.Date;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 * 
 * @author Joao Carlos
 * Data..: 20-Abril-2005
 *
 */
public class ConsultaSaldoBoomerang extends Aplicacoes
{
	public ConsultaSaldoBoomerang(long idProcesso)
	{
		super(idProcesso,"ConsultaSaldoBoomerang");
	}
	
	/**
	 * Metodo....:getDataInicialPeriodo
	 * Descricao.:Retorna a data inicial do periodo da promocao Boomerang
	 * @param mes - Mes a ser utilizado como parametro
	 * @return Date - Data inicial do Periodo para o mes informado
	 */
	private Date getDataInicialPeriodo(int mes)
	{
		// OBS. A definicao do mes na classe Calendar deve ser sempre considerada com um valor a menos
		// ex: Mes Janeiro nao deve ser informado como mes 1 e sim como mes 0(zero)
		// Define a data inicial
		Calendar dtIni = Calendar.getInstance();
		dtIni.set(Calendar.DAY_OF_MONTH	,28);
		dtIni.set(Calendar.MONTH		,mes-2);
		dtIni.set(Calendar.HOUR_OF_DAY	,0);
		dtIni.set(Calendar.MINUTE		,0);
		dtIni.set(Calendar.SECOND		,0);
		
		return dtIni.getTime();
	}
	
	/**
	 * Metodo....:getDataFinalPeriodo
	 * Descricao.:Retorna a data final do periodo da promocao Boomerang
	 * @param mes - Mes a ser utilizado como parametro
	 * @return Date - Data final do Periodo para o mes informado
	 */
	private Date getDataFinalPeriodo(int mes)
	{
		// Define a data inicial
		Calendar dtFim = Calendar.getInstance();
		dtFim.set(Calendar.DAY_OF_MONTH	,27);
		dtFim.set(Calendar.MONTH		,mes-1);
		dtFim.set(Calendar.HOUR_OF_DAY	,23);
		dtFim.set(Calendar.MINUTE		,59);
		dtFim.set(Calendar.SECOND		,59);
		
		return dtFim.getTime();
	}
	
	/**
	 * Metodo....:getSaldoBoomerang
	 * Descricao.:Retorna o valor do saldo recebido pelo assinante da promocao Boomerang 14
	 * @param msisdn		- Msisdn do assinante
	 * @param mes			- Mes para a pesquisa. O Boomerang e realizado para o perido do dia 28 de um mes
	 *                        ate o dia 28 do proximo mes 
	 * @return SaldoBoomerang - Objeto que armazena o valor do bonus recebido ou a receber pela promocao e
	 *                          um identificador para dizer se o assinante fez a recarga devida para receber
	 *                          o valor da promocao
	 * @throws GPPInternalErrorException
	 */
	public SaldoBoomerang getSaldoBoomerang(String msisdn, int mes) throws GPPInternalErrorException
	{
		SaldoBoomerang saldoBoomerang = new SaldoBoomerang();
		saldoBoomerang.msisdn = msisdn;
		PREPConexao conexaoPrep = null;
		try
		{
			// Define o periodo de consulta do boomerang14 sendo que este e o dia 28 do mes escolhido
			// e o dia 28 do proximo mes
			Timestamp dataIni = new Timestamp(getDataInicialPeriodo(mes).getTime());
			Timestamp dataFim = new Timestamp(getDataFinalPeriodo  (mes).getTime());
			
			// Busca uma conexao no pool de banco de dados para ser passada como parametro
			// para o programa que realiza a pesquisa nos CDRs elegiveis para a promocao
			// boomerang 14
			conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
			// Cria instancia da classe que ira realizar a pesquisa e busca o ResultSet
			// da consulta executada por este programa
			GerarExtratoBoomerang extrato = new GerarExtratoBoomerang(super.getIdLog());
			ResultSet valoresExtrato = extrato.getInfoExtratoBoomerang(msisdn,dataIni,dataFim,conexaoPrep);
			// Para cada registro retornado acumula o valor do bonus concedido
			while (valoresExtrato.next())
				saldoBoomerang.valorRecebido += valoresExtrato.getDouble("BONUS_RECEBIDO");
			
			// Acerta informacao se o usuario fez a recarga no mesmo periodo da analise da promocao
			saldoBoomerang.fezRecarga = extrato.usuarioFezRecarga(msisdn,dataIni,dataFim);
		}
		catch(Exception se)
		{
			super.log(Definicoes.ERRO,"getSaldoBoomerang","Erro ao ler o resultado da consulta do extrato boomerang.Erro"+se);
			throw new GPPInternalErrorException("Erro no processamento do extrato boomerang pra retorno do valor concedido."+se);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		return saldoBoomerang;
	}
}
