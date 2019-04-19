package com.brt.gpp.comum.mapeamentos;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDescartaAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.sql.ResultSet;

/**
 * Esta classe eh a responsavel por armazenar os valores dos assinantes que deverao ter suas
 * chamadas locais totalizaveis para o pulapula nao considerados na totalizacao do bonus
 * Para isso a classe mantem dois objetos: 
 * Uma lista contendo a lista negra dos assinantes. Nessa
 * lista se na chamada sendo processada o callId (chamador) estiver na lista negra entao esta
 * chamada nao deve ser totalizada para o pula pula. 
 * No outro objeto uma colecao de mascaras podem ser utilizadas para invalidar uma faixa de
 * valores como por ex: *618401*. Dessa forma todas as mascaras sao validadas para o numero
 * afim de identificar se alguma indica que este nao deve ser totalizado
 * 
 * @author Joao Carlos
 * Data..: 24/10/2005
 *
 */
public final class MapAssinantesNaoBonificaveis extends Mapeamento
{
	private 		Map 							blackList;
	private 		Collection 						mascaras;
	private static 	MapAssinantesNaoBonificaveis	instance;
	
	private MapAssinantesNaoBonificaveis(long idProcesso) throws Exception
	{
	    super();
	}
	
	public static MapAssinantesNaoBonificaveis getInstance(long idProcesso)
	{
	    try
	    {
			if (instance == null)
			{
				instance = new MapAssinantesNaoBonificaveis(idProcesso);
			}
	    }
	    catch(Exception e)
	    {
	        return null;
	    }
		
		return instance;
	}

	/**
	 * Metodo....:descartaAssinante
	 * Descricao.:Retorna se o assinante desejado deve ser ou nao descartado da totalizacao pula pula
	 * @param msisdn - Msisdn a ser verificado
	 * @return boolean - Indica se deve ser descartado ou nao
	 */
	public boolean descartaAssinante(String msisdn)
	{
		// Realiza a verificacao do assinante na blackList. Caso o assinante esteja
		// nessa lista entao jah termina o metodo indicando que este entao nao deve
		// ser totalizado para a promocao. Se o assinante nao existir ainda deve ser
		// verificado se o mesmo nao "bate" com as mascaras cadastradas.
		if (blackList.containsKey(msisdn))
			return true;
		
		for (Iterator i = mascaras.iterator(); i.hasNext();)
			if (msisdn.matches(((PromocaoDescartaAssinante)i.next()).getIdtMsisdn()))
				return true;
		
		return false;
	}

	/**
	 *	Carrega o mapeamento em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException
	{
	    this.inicializaMapeamento(0);
	}
	
	/**
	 * Metodo....:inicializaMapeamento
	 * Descricao.:Inicializa o mapeamento em memoria com os valores da lista de assinantes
	 *            que nao deverao gerar bonus pula pula para os destinos nas chamadas entrantes
	 *            totalizaveis para o pula pula
	 * @param idProcesso - Id do processo
	 */
	public void inicializaMapeamento(long idProcesso)
	{
		PREPConexao conexaoPrep = null;
		try
		{
		    this.blackList = Collections.synchronizedMap(new HashMap());
		    this.mascaras  = Collections.synchronizedCollection(new LinkedHashSet());
		    
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(idProcesso);
			String sql = "select idt_msisdn,ind_mascara from tbl_pro_descarta_assinante";
			ResultSet rs = conexaoPrep.executaQuery(sql,idProcesso);
			while (rs.next())
			{
			    PromocaoDescartaAssinante descarte = new PromocaoDescartaAssinante();
			    String idtMsisdn = rs.getString("idt_msisdn");
			    descarte.setIdtMsisdn(idtMsisdn);
			    int indMascara = rs.getInt("ind_mascara");
			    descarte.setIndMascara(rs.wasNull() ? null : new Integer(indMascara));
			    
				// Identifica se a linha do assinante a ser descartado eh uma mascara
				// para varios assinantes. Caso positivo entao este valor serah armazenado
				// em local diferente para a aplicacao da blackList
				if (descarte.mascara())
					mascaras.add(descarte);
				else
					blackList.put(idtMsisdn, descarte);
			}
			
			super.values.put(new Integer(mascaras.hashCode()), mascaras);
			super.values.put(new Integer(blackList.hashCode()), blackList);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(this.getClass()).log(idProcesso,Definicoes.WARN
					                          ,"MapAssinantesNaoBonificaveis"
					                          ,"inicializaMapeamento"
					                          ,"Erro a inicializar o mapeamento de assinantes nao bonificaveis para a totalizacao. Erro"+e
					                          );
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
}
