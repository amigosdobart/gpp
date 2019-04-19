package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Mapeamento da tabela TBL_APR_TIPO_SALDO, que define os tipos de saldo de assinantes na plataforma Tecnomen. Cada 
 *	tipo de saldo tem um mapeamento com a categoria de plano de preco do assinante, definindo quais os saldos que a 
 *	categoria pode possuir.
 * 
 *	@author		Daniel Ferreira
 *	@since		14/03/2007
 */
public class MapTipoSaldo extends Mapeamento 
{
	
	/**
	 *	Instancia do singleton.
	 */
	private static MapTipoSaldo instance;
	
	/**
	 *	Construtor da classe.
	 *	
	 *	@throws		GPPInternalErrorException
	 */
	private MapTipoSaldo() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *	
	 *	@return		Instancia do singleton.
	 */
	public static MapTipoSaldo getInstance()
	{
		try
		{
			if(MapTipoSaldo.instance == null)
				MapTipoSaldo.instance = new MapTipoSaldo();
			
			return MapTipoSaldo.instance;
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
				"SELECT idt_tipo_saldo, " +
				"       nom_tipo_saldo, " +
				"       ind_disp_ativacao, " +
				"       idt_tipo_saldo_voucher " +
				"  FROM tbl_apr_tipo_saldo ";
			
			registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				short idtTipoSaldo = registros.getShort("idt_tipo_saldo");
				String nomTipoSaldo = registros.getString("nom_tipo_saldo");
				boolean indDispAtivacao = !(registros.getInt("ind_disp_ativacao") == 0);
				short idtTipoSaldoVoucher = (registros.getObject("idt_tipo_saldo_voucher") == null) ? -1 :
											 registros.getShort("idt_tipo_saldo_voucher");
	
				TipoSaldo tipo = new TipoSaldo(idtTipoSaldo, nomTipoSaldo, indDispAtivacao, idtTipoSaldoVoucher);
				super.values.put(new Short(idtTipoSaldo), tipo);
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
	 *	@param		idtTipoSaldo			Identificador do tipo de saldo.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public TipoSaldo getTipoSaldo(short idtTipoSaldo)
	{
		return (TipoSaldo)super.values.get(new Short(idtTipoSaldo));
	}
	
	/**
	 *	Retorna objeto correspondente a um registro da tabela.
	 *
	 *	@param		idtTipoSaldoVcucher		Identificador do tipo de saldo referente à tabela TPECreditAmountMap da Technomen.
	 *	@return		Objeto correspondente a um registro da tabela.
	 */
	public TipoSaldo getByIdTipoSaldoVoucher(short idtTipoSaldoVcucher)
	{
		for (Iterator it = super.values.values().iterator(); it.hasNext(); )
		{
			TipoSaldo tipoSaldo = (TipoSaldo)it.next();
			if (tipoSaldo.getIdtTipoSaldoVoucher() == idtTipoSaldoVcucher)
				return tipoSaldo;
		}
		
		return null;
	}
	
	/**
	 * Sobrescreve o getAll() de Mapeamento, de forma a permitir
	 * o retorno de todas as entidades do mapeamento, independente
	 * delas serem ou não derivadas de Entidade.
	 */
	public Collection getAll() 
	{
		return super.values.values();
	}
	
}
