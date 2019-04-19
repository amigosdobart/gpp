package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Esta classe realiza a leitura das informacoes registradas em banco de dados 
 * para identificar quais as classes que implementam a interface do Totalizador
 * Alem de realizar a pesquisa, esta cria as instancias das classes de totalizacao
 * para serem utilizadas pelo processo de importacao de CDRs
 */
public class TotalizadoresCDRDAO 
{
	private static TotalizadoresCDRDAO instance;
	private GerentePoolLog log;
	private long		   idProcesso;

	private TotalizadoresCDRDAO()
	{
		log = GerentePoolLog.getInstancia(TotalizadoresCDRDAO.class);
		idProcesso = log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
	}
	
	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia (singleton) dessa classe
	 * 
	 * @return   the current value of the instance property
	 */
	public static TotalizadoresCDRDAO getInstance() 
	{
		if (instance == null)
			instance = new TotalizadoresCDRDAO();

		return instance;    
	}
	
	/**
	 * Metodo.....:getListaTotalizadores
	 * Descricao.:Retorna uma colecao com as instancias de classes que implementam
	 *                   a interface de Totalizador
	 * @return java.util.Collection
	 */
	public synchronized Collection getListaTotalizadores()
	{
		Collection listaClasses = new ArrayList();
		// Busca um array contendo todos os nomes de classes que estao 
		// configurados como sendo totalizadores de CDRs
		String classes[] = getClassesTotalizadores();
		// Para cada nome de classe existente em tabela, realiza a
		// instanciacao por reflection e insere na colecao que serah
		// utilizada pelo processo de importacao de CDRs.
		for (int i=0; i < classes.length; i++)
		{
			try
			{
				Class cl = Class.forName(classes[i]);
				listaClasses.add(cl.newInstance());
			}
			catch(Exception e)
			{
				log.log(idProcesso,Definicoes.ERRO,"TotalizadoresCDRDAO","getListaTotalizadores"
						          ,"Erro ao iniciar classe:"+classes[i]+".Erro:"+e);
			}
		}
	
		return listaClasses;
	}
	
	/**
	 * Metodo.....:getClassesTotalizadores
	 * Descricao.:Este metodo retorna a lista de classes que estao configurados em 
	 * tabela que sao as classes que implementam Totalizador
	 * @return java.lang.String[]
	 */
	private String[] getClassesTotalizadores()
	{
		Collection  classes     = new ArrayList();
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			String sql  = "select nom_classe from tbl_ger_totalizadores_cdr where ind_ativo=?";
			Object param[] = {new Integer(Definicoes.CONSTANTE_UM)};

			ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,idProcesso);
			while (rs.next())
				classes.add(rs.getString(1));
		}
		catch(Exception e)
		{
			log.log(idProcesso,Definicoes.ERRO,"TotalizadoresCDRDAO","getClassesTotalizadores"
					          ,"Erro ao pesquisar classes totalizadores. Erro:"+e);
			classes.clear();
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		return (String[])classes.toArray(new String[0]);
	}
}
