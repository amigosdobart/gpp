package com.brt.gpp.comum.mapeamentos;

//Imports Java.

import java.sql.ResultSet;
import java.sql.SQLException;

//Imports GPP.

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoModulacao;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;

/**
  *	Este arquivo contem a classe que faz o mapeamento das modulacoes relacionados a cada uma das promocoes 
  *	em memoria no GPP, ou seja, para cada promocao, quais as modulacoes a serem consideradas.
  *
  *	@author		Joao Carlos
  *	@since 		28/05/2007
  */
public class MapPromocaoModulacao extends Mapeamento
{

	private static MapPromocaoModulacao instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocaoModulacao() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Cria uma instancia ou refere-se a instancia ja existente.
	 *
	 *	@return		MapPromocaoPlanoPreco				instance					Mapeamento da TBL_PRO_MODULACAO.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapPromocaoModulacao getInstancia() throws GPPInternalErrorException 
	{
		if(MapPromocaoModulacao.instance == null) 
		{
			MapPromocaoModulacao.instance = new MapPromocaoModulacao();
		}
		
		return MapPromocaoModulacao.instance;
	}	

	/**
	 *	Carrega o mapeamento em memoria.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	protected void load() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		try
		{
			//Seleciona conexao do pool Prep Conexao
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			//Seleciona planos de precos validos no BD do GPP
			String sql =	"select idt_codigo_nacional " +
							      ",idt_promocao " +
							      ",idt_horario_inicio " +
							      ",idt_horario_fim " +
                                  ",ind_bonifica_pulapula " +
							  "from tbl_pro_modulacao ";

			ResultSet result = conexaoPrep.executaPreparedQuery(sql, null,0);
			while (result.next())
			{
				MapCodigoNacional 	mapCN 		= MapCodigoNacional.getInstance();
				MapPromocao			mapPromo	= MapPromocao.getInstancia();
				
				PromocaoModulacao mod = new PromocaoModulacao();
				Integer cn[] = {new Integer(result.getInt("idt_codigo_nacional"))};
				mod.setCn((CodigoNacional)mapCN.get(cn));
				mod.setPromocao(mapPromo.getPromocao(result.getInt("idt_promocao")));
				mod.setHoraInicio(result.getLong("idt_horario_inicio"));
				mod.setHoraFim(result.getLong("idt_horario_fim"));
                mod.setIndBonificaPulaPula(result.getInt("ind_bonifica_pulapula"));
				
				super.values.put(mod.key(), mod);
			}
			
			result.close();
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
	 * Metodo....:getModulacao
	 * Descricao.:Retorna a informacao a respeito da modulacao da promocao
	 * @param idPromocao	- Promocao a ser pesquisada
	 * @param codNacional	- Codigo nacional do assinante
	 * @return PromocaoModulacao - Modulacao da Promocao
	 */
	public PromocaoModulacao getModulacao(int idPromocao, int codNacional)
	{
		String key = String.valueOf(codNacional) + String.valueOf(idPromocao);
		return (PromocaoModulacao)super.values.get(key);
	}
}
