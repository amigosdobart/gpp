package com.brt.gpp.aplicacoes.campanha.incentivoRecargas;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.CondIncentivoRecargas;
import com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.dao.CondIncentivoRecargasDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class CondIncentivoRecargasImpl implements CondicaoConcessao 
{
	private Collection condsIncentivoRecarga;
	private CondIncentivoRecargas condSatisfeita;
	private Date dataRecarga;
	
	public CondIncentivoRecargasImpl(Campanha campanha)
	{
		// Realiza na instanciacao da classe a busca dos valores que serao
		// a base para a verificacao se o assinante satisfez ou nao as condicoes
		// Isto eh realizado na instanciacao para evitar acesso ao banco de 
		// dados inumeras vezes para todos os assinantes inscritos nas campanhas
		// acelerando um pouco o processo
		condsIncentivoRecarga = CondIncentivoRecargasDAO.getCondsIncentivoRecargas(campanha);
	}

	/**
	 * Metodo....:getRecargas
	 * Descricao.:Retorna os valores de Recarga efetuados pelo assinante
	 * @param assinante - Assinante a ser pesquisado
	 */
	public Map getRecargas(AssinanteCampanha assinante)
	{
		Map valoresRecarga = new TreeMap();
		long idProcesso = GerentePoolLog.getInstancia(CondIncentivoRecargasImpl.class).getIdProcesso("CondIncentivoRecargasImpl");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			// Realiza a pesquisa na tabela de Recargas para identificar quais
			// valores o assinante realizou no periodo da promocao. Esses valores
			// serao a base para identificar qual bonus o assinante irah receber
			String sql = "SELECT VLR_PAGO,DAT_ORIGEM " +
			               "FROM TBL_REC_RECARGAS " +
			              "WHERE DAT_ORIGEM > ? " +
			                "AND DAT_ORIGEM < ? " +
			                "AND ID_TIPO_RECARGA = ? " +
			                "AND IDT_MSISDN = ?";
			
			// Acerta a data final da pesquisa como sendo a data
			// final da campanha + 1 dia. Jah que a campanha nao
			// eh cadastrado com a hora entao utiliza-se esse recurso
			Calendar dataFinal = Calendar.getInstance();
			dataFinal.setTime(assinante.getCampanha().getValidadeFimConcessao());
			dataFinal.add(Calendar.DAY_OF_MONTH,1);
			// A data inicial nao tem modificacao
			Object param[] = {new Timestamp(assinante.getCampanha().getValidadeIniConcessao().getTime())
					         ,new Timestamp(dataFinal.getTimeInMillis())
					         ,Definicoes.TIPO_RECARGA
					         ,assinante.getMsisdn()
					         };
			ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,idProcesso);
			while (rs.next())
				valoresRecarga.put(new Double(rs.getDouble("VLR_PAGO")),(Date)rs.getTimestamp("DAT_ORIGEM"));
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(CondIncentivoRecargasDAO.class).log(idProcesso,Definicoes.ERRO
                    ,"CondIncentivoRecargasImpl"
                    ,"getRecargas"
                    ,"Erro ao pesquisar recargas efetuadas pelo assinante:"+assinante.getMsisdn()+". Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return valoresRecarga;
	}

	/**
	 * @param assinante
	 * @return boolean
	 */
	public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep) 
	{
		// Realiza a pesquisa de recargas efetuadas pelo assinante no periodo
		// da campanha promocional. Para cada recarga efetuada entao a comparacao
		// com as condicoes sao efetuadas
		Map recargas = getRecargas(assinante);
		for (Iterator i=condsIncentivoRecarga.iterator(); i.hasNext();)
		{
			CondIncentivoRecargas cond = (CondIncentivoRecargas)i.next();
			// Para cada condicao verifica quais as recargas que o assinante
			// realizou e portanto compara para verificar se alguma destas
			// estah cadastrada como condicao
			for (Iterator it=recargas.keySet().iterator(); it.hasNext();)
			{
				Double vlrRecRealizada = (Double)it.next();
				// Verifica se a condicao foi satisfeita com algum valor de
				// recarga efetuado. Em caso positivo identifica a condicao
				// como sendo a condicao que satisfez a campanha para esse
				// assinante
				if (vlrRecRealizada.doubleValue() == cond.getValorRecarga())
				{
					// Identifica a condicao satisfeita
					condSatisfeita = cond;
					// Identifica tambem a data da recarga para ser utilizada
					// como identificador da data de condicao satisfeita
					dataRecarga = (Date)recargas.get(vlrRecRealizada);
					// Realiza o retorno indicando que o assinante teve
					// alguma condicao da campanha satisfeita e portanto
					// deve receber o bonus
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return String
	 */
	public String getNomeCondicao() 
	{
		// Identifica o valor da recarga da condicao que foi
		// satisfeita para a concessao de credito, se essa
		// condicao for nula entao retorna uma string nula
		// tambem
		if (condSatisfeita != null)
			return String.valueOf(condSatisfeita.getValorRecarga());
		
		return null;
	}
	
	/**
	 * @return double
	 */
	public double getValorConcederSM() 
	{
		if (condSatisfeita != null)
			return condSatisfeita.getValorBonusSM();
		return 0;
	}
	
	public double getValorConcederBonus()
	{
		if (condSatisfeita != null)
			return condSatisfeita.getValorBonus();
		
		return 0;
	}
	
	/**
	 * @return double
	 */
	public double getValorConcederDados() 
	{
		if (condSatisfeita != null)
			return condSatisfeita.getValorBonusDados();
		return 0;
	}
	
	public Date getDataSatisfacaoCondicao()
	{
		return dataRecarga;
	}
	
	public void executarPosBonificacao(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
	}
}