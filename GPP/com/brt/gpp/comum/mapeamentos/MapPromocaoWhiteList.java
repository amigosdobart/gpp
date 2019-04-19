package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDesconto;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoWhiteList;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

public class MapPromocaoWhiteList extends Mapeamento
{
	private static MapPromocaoWhiteList instance;
	
	private static final String SQL_PES =   "select w.id_desconto_pula_pula " +
											      ",d.des_desconto " +
											      ",w.idt_msisdn " +
											      ",w.ind_mascara " +
											      ",w.idt_possiveis_promocoes " +
											  "from tbl_pro_white_list w " +
											      ",tbl_pro_desconto_pula_pula d " +
											 "where d.id_desconto = w.id_desconto_pula_pula ";

	
	private MapPromocaoWhiteList() throws GPPInternalErrorException 
	{
		super();
	}
	
	public static MapPromocaoWhiteList getInstance() throws GPPInternalErrorException 
	{
		if (instance == null)
			instance = new MapPromocaoWhiteList();
		
		return instance;
	}

	/**
	 * Metodo....:isWhiteList
	 * Descricao.:Verifica se o numero informado pertence a white list
	 * @param descontoPulaPula	- Desconto pula-pula
	 * @param msisdn			- Msisdn do assinante
	 * @return	boolean
	 */
	public boolean isWhiteList(int descontoPulaPula, String msisdn, int promocao)
	{
		PromocaoDesconto descProm = new PromocaoDesconto();
		descProm.setIdDesconto(descontoPulaPula);
		// Busca no mapeamento de white list, todos os assinantes
		// e mascaras registrados para o desconto pula-pula informado
		Map assinantes = (Map)values.get(descProm);
		if (assinantes != null)
		{
			// Para o desconto informado, o assinate eh verificado no 
			// cache. Caso o assinante exista, eh ainda verificado a 
			// aplicabilidade da promocao deste, sendo esta informacao
			// pesquisada no Cache principal.
			PromocaoWhiteList whLst = (PromocaoWhiteList)assinantes.get(msisdn);
			// Caso o assinante nao exista no mapeamento da white list, verifica
			// em todos os elementos existentes do tipo MASCARA para verificar
			// se o assinante ainda pode estar na white list atraves do batimento
			// do numero do acesso com esta mascara.
			if(whLst != null)
			{
				String promocoes[] = whLst.getPossivesPromocoes();
				for (int i=0; i < promocoes.length; i++)
					if (promocoes[0].equals("*") || promocoes[i].equals(String.valueOf(promocao)))
						return true;
			}
			else
			{
				for(Iterator i=assinantes.values().iterator(); i.hasNext();)
				{
					PromocaoWhiteList elemento = (PromocaoWhiteList)i.next();
					if (elemento.isMascara())
						if (msisdn.matches(elemento.getMsisdn()))
						{
							String promocoes[] = elemento.getPossivesPromocoes();
							for (int j=0; j < promocoes.length; j++)
								if (promocoes[0].equals("*") || promocoes[j].equals(String.valueOf(promocao)))
									return true;
						}
				}
			}
		}
		
		return false;
	}
	
	protected void load() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(SQL_PES, null, 0);
			while (rs.next())
			{
				PromocaoDesconto descProm = new PromocaoDesconto();
				descProm.setIdDesconto(rs.getInt("id_desconto_pula_pula"));
				descProm.setDescricao(rs.getString("des_desconto"));
				
				PromocaoWhiteList whList = new PromocaoWhiteList();
				whList.setDesconto(descProm);
				whList.setMsisdn(rs.getString("idt_msisdn"));
				whList.setMascara(rs.getInt("ind_mascara") == 1 ? true : false);
				whList.setPossivesPromocoes(rs.getString("idt_possiveis_promocoes").split("[,]"));
				
				Map assinantes = (Map)values.get(descProm);
				if (assinantes == null)
					assinantes = new HashMap();
				
				// Atualiza o hash do desconto pula-pula contendo como chave o assinante
				// cadastrado na white list. Estes assinantes sao incluidos inclusive se
				// for uma mascara onde entao fica o array contendo as informacoes
				assinantes.put(whList.getMsisdn(), whList);
				
				// Insere no mapeamento principal este mapeamento do valor de white list
				// do assinante por desconto pula-pula.
				values.put(descProm, assinantes);
			}
		}
		catch(SQLException e)
		{
			throw new GPPInternalErrorException ("Excecao SQL: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}
}
