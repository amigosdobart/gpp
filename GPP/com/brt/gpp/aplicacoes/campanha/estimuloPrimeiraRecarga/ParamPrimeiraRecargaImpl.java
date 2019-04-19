package com.brt.gpp.aplicacoes.campanha.estimuloPrimeiraRecarga;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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

public class ParamPrimeiraRecargaImpl implements ParametroInscricao
{
	private Map parametros;
	
	private DecimalFormat df = new DecimalFormat("###0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private static final String SQL_PESQ =  "select dat_origem, vlr_pago, vlr_saldo_final_principal " +
											  "from (select r.dat_origem " +
											              ",r.vlr_saldo_final_principal " +
											              ",r.vlr_pago " +
											              ",min(r.dat_origem)over (partition by idt_msisdn) as min_data " +
											          "from tbl_rec_recargas r " +
											         "where r.id_tipo_recarga = ? " +
											           "and r.idt_msisdn      = ? " +
											           "and r.dat_origem     >  ? " +
											           "and r.dat_origem     <= ?) " +
											 "where dat_origem = min_data ";

	public ParamPrimeiraRecargaImpl(Campanha campanha)
	{
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#podeSerInscrito(com.brt.gpp.aplicacoes.aprovisionar.Assinante)
	 */
	public boolean podeSerInscrito(Assinante assinante, PREPConexao conexaoPrep)
	{
		boolean podeSerInscrito = false;
		GerentePoolLog log = GerentePoolLog.getInstancia(this.getClass());
		ResultSet rs = null;
		try
		{
			// Define a data e hora final de pesquisa da recarga
			// sendo que esta deve ser em ateh 48hs apos a data
			// de ativacao do assinante
			Calendar cal = Calendar.getInstance();
			cal.setTime(assinante.getDataAtivacao());
			cal.add(Calendar.HOUR_OF_DAY,48);
			
			Object param[] = {Definicoes.TIPO_RECARGA
					         ,assinante.getMSISDN()
					         ,new Timestamp(assinante.getDataAtivacao().getTime())
					         ,new Timestamp(cal.getTimeInMillis())
					         };
			
			// Executa a pesquisa de recargam em ateh 48hs apos a ativacao do
			// acesso. Se alguma recarga existe entao o assinante pode ser 
			// inscrito na campanha e define os parametros da primeira recarga
			// realizada, caso haja mais de uma.
			rs = conexaoPrep.executaPreparedQuery(SQL_PESQ,param,0);
			if (rs.next())
			{
				// Define os parametros para serem registrados em banco de dados por XML
				parametros = new HashMap();
				parametros.put("dataRecarga",sdf.format(rs.getTimestamp("dat_origem")));
				parametros.put("saldoFinal",df.format(rs.getDouble("vlr_saldo_final_principal")));
				parametros.put("valorRecarga",df.format(rs.getDouble("vlr_pago")));
				// Marca que o assinate pode ser inscrito na campanha
				podeSerInscrito = true;
			}
		}
		catch(Exception e)
		{
			log.log(0,Definicoes.ERRO,"ParamPrimeiraRecargaImpl","podeSerInscrito"
					 ,"Erro ao verificar se o assinante pode ser inscrito na campanha de primeira recarga. Erro:"+e);
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch(Exception e){};
		}
		return podeSerInscrito;
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
	public boolean enviaSMS(Assinante assinante, PREPConexao conexaoPrep) {
		return true;
	}
}
