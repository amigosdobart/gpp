package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoTipoBonificacao;
import com.brt.gpp.comum.mapeamentos.chave.ChaveHashMap;

/**
 *	Mapeamento de objetos PromocaoTipoTransacao em memoria. 
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date 		28/09/2005
 *	@modify		Primeira versao.
 *
 *	@version	1.1
 *	@author		Daniel Ferreira
 *	@date 		18/03/2008
 *	@modify		Inclusao de multiplas bonificacoes no processo de concessao.
 */
public final class MapPromocaoInfosSms extends Mapeamento
{

	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocaoInfosSms instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoInfosSms() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapPromocaoInfosSms getInstance() 
	{
		try
		{
			if(MapPromocaoInfosSms.instance == null) 
			    MapPromocaoInfosSms.instance = new MapPromocaoInfosSms();
			
			return MapPromocaoInfosSms.instance;
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
		PREPConexao 				conexaoPrep 		= null;
		MapPromocao					mapPromocao			= MapPromocao.getInstancia();
		MapPromocaoTipoBonificacao	mapTipoBonificacao	= MapPromocaoTipoBonificacao.getInstance();
		
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sqlQuery = 
				"SELECT idt_promocao, " +
				"       tip_execucao, " +
				"       id_tipo_bonificacao, " +
				"       ind_envia_sms, " +
				"       tip_sms, " +
				"       num_prioridade, " +
				"       des_mensagem " +
				"  FROM tbl_pro_infos_sms ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while(registros.next())
			{
				PromocaoInfosSms infosSms = new PromocaoInfosSms();
				
				Promocao promocao = mapPromocao.getPromocao(registros.getInt("idt_promocao"));
				infosSms.setPromocao(promocao);
				
				String tipExecucao = registros.getString("tip_execucao");
				infosSms.setTipExecucao(tipExecucao);
				
				PromocaoTipoBonificacao tipoBonificacao = mapTipoBonificacao.getPromocaoTipoBonificacao(registros.getShort("id_tipo_bonificacao"));
				infosSms.setTipoBonificacao(tipoBonificacao);
				
				infosSms.setIndEnviaSms(registros.getInt("ind_envia_sms") != 0);
				infosSms.setTipSms(registros.getString("tip_sms"));
				infosSms.setNumPrioridade(registros.getShort("num_prioridade"));
				infosSms.setDesMensagem(registros.getString("des_mensagem"));
				
				ChaveHashMap chave = new ChaveHashMap(new Object[]{promocao, tipExecucao, tipoBonificacao});
				super.values.put(chave, infosSms);
			}
			
			registros.close();
			registros = null;
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
	
	/**
	 *	Retorna objeto PromocaoInfosSms a partir da chave.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		tipExecucao				Tipo de execucao da concessao.
	 *	@return		Objeto PromocaoInfosSms correspondente obtido a partir da chave.
	 */
	public PromocaoInfosSms getPromocaoInfosSms(Promocao promocao, String tipExecucao, PromocaoTipoBonificacao tipoBonificacao)
	{
		ChaveHashMap chave = new ChaveHashMap(new Object[]{promocao, tipExecucao, tipoBonificacao});

		return (PromocaoInfosSms)super.values.get(chave);
	}

	/**
	 *	Retorna uma lista de objetos PromocaoInfosSms associados a promocao.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@return		Lista de objetos PromocaoInfosSms associados a promocao.
	 */
	public Collection getPromocaoInfosSms(Promocao promocao)
	{
	    ArrayList result = new ArrayList();
	    
	    for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
	    {
	    	PromocaoInfosSms infosSms = (PromocaoInfosSms)iterator.next();
	    	
	    	if(infosSms.getPromocao().equals(promocao))
	    		result.add(infosSms);
	    }
		
		return result;
	}

}
