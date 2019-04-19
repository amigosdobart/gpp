package com.brt.gpp.aplicacoes.campanha.dao;

import java.sql.ResultSet;
import java.util.Date;

import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteTSD;
import com.brt.gpp.aplicacoes.campanha.entidade.NPGInfosBonificacao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
 *	Implementacao de consultas ao banco de dados referentes a promocao Natal Pague e Ganhe.
 *
 *	@author		Daniel Ferreira
 *	@since		03/11/2006
 */
public abstract class NatalPagueGanheDAO
{
	
	//Constantes internas.
	
	/**
	 *	Statement SQL para verificacao de subida de TSD a partir do MSISDN.
	 */
	private static final String SQL_TSD_MSISDN = "SELECT 1 " +
										   		 "  FROM hsid.hsid_cliente_historico " +
												 " WHERE nu_msisdn = ? " +
												 "   AND dt_inclusao >= ? " +
												 "   AND dt_inclusao < ? ";
	
	/**
	 *	Statement SQL para verificacao de subida de TSD a partir do IMEI.
	 */
	private static final String SQL_TSD_IMEI = "SELECT 1 " +
											   "  FROM hsid.hsid_cliente_historico " +
											   " WHERE nu_imei = ? " +
											   "   AND dt_inclusao < ? ";
	
	/**
	 *	Statement SQL para verificacao de subida de TSD a partir do ICCID.
	 */
	private static final String SQL_TSD_ICCID = "SELECT 1 " +
												"  FROM hsid.hsid_cliente_historico " +
												" WHERE nu_iccid = ? " +
												"   AND dt_inclusao < ? ";
	
	/**
	 *	Statement SQL para verificacao de recebimento do bonus da promocao Natal Pague e Ganhe.
	 */
	private static final String SQL_ASS_BONUS = "SELECT dat_origem " +
												"  FROM tbl_rec_recargas " +
												" WHERE idt_msisdn = ? " +
												"   AND tip_transacao = ? " +
												"   AND dat_origem >= ? ";
	
	/**
	 *	Statement SQL para verificar se o assinante efetuou o montante minimo em recargas.
	 */
	private static final String SQL_ASS_RECARGA = "SELECT count(1) as qtd_recargas, " +
												  "       sum(vlr_pago) as vlr_pago " +
												  "  FROM tbl_rec_recargas " +
												  " WHERE idt_msisdn = ? " +
												  "   AND id_tipo_recarga = ? " +
												  "   AND dat_origem >= ? ";
	
	/**
	 *	Statement SQL para obtencao de informacoes referentes a bonificacao do assinante e ao preco do aparelho.
	 */
	private static String SQL_INFOS_BONIFICACAO = "SELECT p.co_sap, " +
												  "       m.co_modelo, " +
												  "       p.vl_preco, " +
												  "       m.no_fabricante, " +
												  "       m.no_modelo " +
												  "  FROM hsid.hsid_modelo_tac t, " +
												  "       hsid.hsid_modelo_sap m, " +
												  "       hsid.hsid_preco_aparelho p " +
												  " WHERE t.co_modelo = m.co_modelo " +
												  "   AND m.co_sap = p.co_sap " +
												  "   AND ? >= p.dt_ini_validade " +
												  "   AND (p.dt_fim_validade IS NULL OR ? <= p.dt_fim_validade) " +
												  "   AND p.co_nacional = ? " +
												  "   AND t.co_tac = ? " +
												  " ORDER BY p.vl_preco ";

	//Metodos.
	
	/**
	 *	Retorna se o MSISDN ja subiu TSD entre as datas de corte.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		dataInicio				Data inicial de corte. A consulta procura pelos eventos posteriores a esta data.
	 *	@param		dataFim					Data final de corte. A consulta procura pelos eventos anteriores a esta data.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se o MSISDN ja subiu TSD e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean msisdnSubiuTSD(String msisdn, Date dataInicio, Date dataFim, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		Object[] parametros =
		{
			msisdn,
			new java.sql.Timestamp(dataInicio.getTime()),
			new java.sql.Timestamp(dataFim.getTime())
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(NatalPagueGanheDAO.SQL_TSD_MSISDN, parametros, conexaoPrep.getIdProcesso());
			return registros.next();
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
	}
	
	/**
	 *	Retorna se o IMEI pertence a um aparelho que ja subiu TSD antes da data de corte.
	 *
	 *	@param		imei					Numero serial (IMEI) do aparelho.
	 *	@param		dataFim					Data de corte. A consulta procura pelos eventos anteriores a esta data.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se o aparelho ja subiu TSD e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean aparelhoSubiuTSD(String imei, Date dataFim, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		Object[] parametros =
		{
			imei,
			new java.sql.Timestamp(dataFim.getTime())
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(NatalPagueGanheDAO.SQL_TSD_IMEI, parametros, conexaoPrep.getIdProcesso());
			return registros.next();
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
	}
	
	/**
	 *	Retorna se o ICCID pertence a um SIMCard que ja subiu TSD antes da data de corte.
	 *
	 *	@param		iccid					Numero serial (ICCID) do SIMCard.
	 *	@param		dataFim					Data de corte. A consulta procura pelos eventos anteriores a esta data.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se o SIMCard ja subiu TSD e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean simcardSubiuTSD(String iccid, Date dataFim, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		Object[] parametros =
		{
			iccid,
			new java.sql.Timestamp(dataFim.getTime())
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(NatalPagueGanheDAO.SQL_TSD_ICCID, parametros, conexaoPrep.getIdProcesso());
			return registros.next();
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
	}
	
	/**
	 *	Retorna se assinante ja recebeu o bonus da promocao Natal Pague e Ganhe.
	 *
	 *	@param		infosBonificacao		Informacoes referentes a bonificacao do assinante.
	 *	@param		tipoTransacao			Tipo de transacao do bonus.
	 *	@param		dataInicio				Data de corte. O processo procura por bonus a partir da data especificada.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se o assinante ja recebeu o bonus e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean assinanteRecebeuBonus(NPGInfosBonificacao infosBonificacao, String tipoTransacao, Date dataInicio, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		Object[] parametros =
		{
			infosBonificacao.getMsisdn(),
			tipoTransacao,
			new java.sql.Timestamp(dataInicio.getTime())
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(NatalPagueGanheDAO.SQL_ASS_BONUS, parametros, conexaoPrep.getIdProcesso());
			
			if(registros.next())
			{
				infosBonificacao.setDataRecebimento(registros.getTimestamp("dat_origem"));
				return true;
			}
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		
		return false;
	}
	
	/**
	 *	Retorna se assinante realizou o montante minimo em recargas para receber o bonus da promocao Natal Pague e Ganhe.
	 *
	 *	@param		infosBonificacao		Informacoes referentes a bonificacao do assinante.
	 *	@param		valorMinimo				Valor minimo pago em recargas para o recebimento do bonus.
	 *	@param		dataInicio				Data da subida do TSD. 
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		True se o assinante realizou o montante minimo e false caso contrario.
	 *	@throws		Exception
	 */
	public static boolean assinanteRealizouRecarga(NPGInfosBonificacao infosBonificacao, double valorMinimo, Date dataInicio, PREPConexao conexaoPrep) throws Exception
	{
		ResultSet registros = null;
		
		Object[] parametros =
		{
			infosBonificacao.getMsisdn(),
			Definicoes.TIPO_RECARGA,
			new java.sql.Date(dataInicio.getTime())
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(NatalPagueGanheDAO.SQL_ASS_RECARGA, parametros, conexaoPrep.getIdProcesso());
			if(registros.next())
			{
				infosBonificacao.setNumRecargas  (registros.getInt   ("qtd_recargas"));
				infosBonificacao.setValorRecargas(registros.getDouble("vlr_pago"    ));
				return (infosBonificacao.getValorRecargas() >= valorMinimo);
			}
			
			return false;
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
	}
	
	/**
	 *	Retorna as informacoes referentes a bonificacao do assinante. Estas informacoes abrangem a descricao e preco
	 *	do aparelho e status da bonificacao.
	 *
	 *	@param		assinante				Informacoes de subida de TSD e do assinante na plataforma.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@return		Objeto contendo as informacoes da bonificacao do assinante.
	 *	@throws		Exception
	 */
	public static NPGInfosBonificacao getNPGInfosBonificacao(AssinanteTSD assinante, PREPConexao conexaoPrep) throws Exception
	{
		NPGInfosBonificacao result = new NPGInfosBonificacao();
		result.setDataSubidaTSD(assinante.getDataSubidaTSD());
		result.setMsisdn(assinante.getMSISDN());
		result.setICCID(assinante.getICCID());
		result.setIMEI(assinante.getIMEI());
		
		ResultSet registros = null;
		Object[] parametros = 
		{
			new java.sql.Timestamp(assinante.getDataSubidaTSD().getTime()),
			new java.sql.Timestamp(assinante.getDataSubidaTSD().getTime()),
			assinante.getMSISDN().substring(2, 4),
			assinante.getIMEI().substring(0, 8)
		};
		
		try
		{
			registros = conexaoPrep.executaPreparedQuery(NatalPagueGanheDAO.SQL_INFOS_BONIFICACAO, parametros, conexaoPrep.getIdProcesso());
			
			if(registros.next())
			{
				result.setCodSAP(registros.getString("co_sap"));
				result.setDescFabricante(registros.getString("no_fabricante"));
				result.setCodModelo(registros.getString("co_modelo"));
				result.setDescModelo(registros.getString("no_modelo"));
				result.setValorAparelho(registros.getDouble("vl_preco"));
			}
			else 
			{
				MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
				String valorAparelho = mapConfiguracao.getMapValorConfiguracaoGPP("CAMPANHA_NPG_BONUS_DEFAULT");
				result.setValorAparelho(Double.valueOf(valorAparelho).doubleValue());
			}
		}
		finally
		{
			if(registros != null)
				registros.close();
		}
		
		return result;
	}
	
}
