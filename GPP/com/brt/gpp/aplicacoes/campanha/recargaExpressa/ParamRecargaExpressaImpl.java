package com.brt.gpp.aplicacoes.campanha.recargaExpressa;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ParamRecargaExpressaImpl implements ParametroInscricao
{
	private Map parametros;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private RecargaExpressaDAO recargaExpressaDAO	= new RecargaExpressaDAO();
	private GerentePoolLog log = GerentePoolLog.getInstancia(this.getClass());
	private boolean fezRecarga = false;
	
	public ParamRecargaExpressaImpl(Campanha campanha)
	{
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#podeSerInscrito(com.brt.gpp.aplicacoes.aprovisionar.Assinante)
	 */
	public boolean podeSerInscrito(Assinante assinante, PREPConexao conexaoPrep)
	{
		int valorRecarga = 0;
		ResultSet rs = null;
		try 
		{
			rs = recargaExpressaDAO.fezRecargaUltimosDias(assinante, conexaoPrep, 1);			
			if (rs.next())
			{
				valorRecarga = rs.getInt("valor");
				this.fezRecarga = true;
			}
			else
			{
				this.fezRecarga = false;
			}
			rs.close();
		}
		catch (Exception e) 
		{
			log.log(0,Definicoes.ERRO,"ParamRecargaExpressaImpl","podeSerInscrito"
					 ,"Erro ao verificar se o assinante pode ser inscrito. Erro:"+e);
		}
		parametros = new HashMap();
		parametros.put("msisdn", assinante.getMSISDN());
		parametros.put("dataEntradaStatus2", sdf.format(Calendar.getInstance().getTime()));
		parametros.put("valorRecarga", String.valueOf(valorRecarga));
		parametros.put("fezRecarga",Boolean.toString(this.fezRecarga));
		
		return true;
	}

	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#getParametros()
	 */
	public Map getParametros()
	{
		return parametros;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#enviaSMS()
	 */
	public boolean enviaSMS(Assinante assinante, PREPConexao conexaoPrep)
	{
		return !fezRecarga;
	}
}
