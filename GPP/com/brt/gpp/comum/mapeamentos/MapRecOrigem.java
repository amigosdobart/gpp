package com.brt.gpp.comum.mapeamentos;

//Imports Java.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Imports GPP.
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;


/**
  *
  * Este arquivo contem a classe que faz o mapeamento da TBL_REC_ORIGEM 
  * existente no BD Oracle em memoria no GPP
  * <P> Version:		1.0
  *
  * @Autor: 		Denys Oliveira
  * Data: 			09/06/2004
  *
  */
public final class MapRecOrigem extends Mapeamento 
{

	//Variaveis Membro
	private static MapRecOrigem instance = null;

	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapRecOrigem() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapRecOrigem		instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	 public static MapRecOrigem getInstancia() throws GPPInternalErrorException 
	 {
		 return MapRecOrigem.getInstance();
	 }

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapRecOrigem		instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapRecOrigem getInstance() throws GPPInternalErrorException 
	{
		if(MapRecOrigem.instance == null) 
		{
		    MapRecOrigem.instance = new MapRecOrigem();
		}
		
		return MapRecOrigem.instance;
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
			//Obtem uma conexao com o banco de dados.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			//Obtem o statement SQL.
			String sqlQuery =
				"SELECT                         " +
				"	ID_ORIGEM                ,	" +
				"	DES_ORIGEM               ,	" +
				"	IND_MODIFICAR_DATA_EXP   ,  " +
				"	ID_CANAL                 ,  " +
				"	IND_ATIVO                ,  " +
				"	TIP_LANCAMENTO           ,  " +
				"	IDT_CLASSIFICACAO_RECARGA,  " +
				"	IND_DISPONIVEL_PORTAL       " +
				"FROM                           " +
				"	TBL_REC_ORIGEM              ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				OrigemRecarga origem = new OrigemRecarga();
				
				String idOrigem = registros.getString("ID_ORIGEM");
				origem.setIdOrigem(idOrigem);
				
				String desOrigem = registros.getString("DES_ORIGEM");
				origem.setDesOrigem(desOrigem);
				
				int indModificarDataExp = registros.getInt("IND_MODIFICAR_DATA_EXP");
				origem.setIndModificarDataExp(registros.wasNull() ? null : new Integer(indModificarDataExp));
				
				String idCanal = registros.getString("ID_CANAL");
				origem.setIdCanal(idCanal);
				
				int indAtivo = registros.getInt("IND_ATIVO");
				origem.setIndAtivo(registros.wasNull() ? null : new Integer(indAtivo));
				
				String tipLancamento = registros.getString("TIP_LANCAMENTO");
				origem.setTipLancamento(tipLancamento);
				
				String idtClassificacaoRecarga = registros.getString("IDT_CLASSIFICACAO_RECARGA");
				origem.setIdtClassificacaoRecarga(idtClassificacaoRecarga);
				
				int indDisponivelPortal = registros.getInt("IND_DISPONIVEL_PORTAL");
				origem.setIndDisponivelPortal(registros.wasNull() ? null : new Integer(indDisponivelPortal));
				
				//Inserindo o registro no Map.
				Map mapCanal = (Map)super.values.get(idCanal);
				if(mapCanal == null)
				{
				    mapCanal = Collections.synchronizedMap(new HashMap());
					super.values.put(idCanal, mapCanal);
				}
				mapCanal.put(idOrigem, origem);
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
	 *	Retorna sua descricao associada ao tipo de transacao passado por parametro.
	 *
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@return								Descricao associada ao tipo de transacao.
	 */
	public String getMapDescRecOrigem(String tipTransacao)
	{
		OrigemRecarga origem = this.getOrigemRecarga(tipTransacao);
		
		if(origem != null)
		{
		    return origem.getDesOrigem();
		}
		
		return null;
	}
	
	/**
	 *	Retorna o indicador de modificacao de data de expiracao associada ao tipo de transacao passado por parametro.
	 *
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@return								Indicador de modificacao de data de expiracao.
	 */
	public String getMapIndModDataExpRecOrigem(String tipTransacao)
	{
	    OrigemRecarga origem = this.getOrigemRecarga(tipTransacao);
	    
	    if((origem != null) && (origem.getIndModificarDataExp() != null))
	    {
	        return String.valueOf(origem.getIndModificarDataExp().intValue());
	    }
	    
	    return null;
	}

	/**
	 *	Retorna o indicador de atividade associado ao tipo de transacao passado por parametro.
	 *
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@return								Indicador de atividade.
	 */
	public String getMapIndAtivoRecOrigem(String tipTransacao)
	{
	    OrigemRecarga origem = this.getOrigemRecarga(tipTransacao);
	    
	    if((origem != null) && (origem.getIndAtivo() != null))
	    {
	        return String.valueOf(origem.getIndAtivo().intValue());
	    }
	    
	    return null;
	}

	/**
	 *	Retorna o tipo de lancamento associado ao tipo de transacao.
	 *
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@return								Tipo de lancamento.
	 */
	public String getMapTipLancamentoRecOrigem(String tipTransacao)
	{
	    OrigemRecarga origem = this.getOrigemRecarga(tipTransacao);
	    
	    if(origem != null)
	    {
	        return origem.getTipLancamento();
	    }
	    
	    return null;
	}

	/**
	 *	Dependendo do idRetorno, retorna um campo da TBL_REC_ORIGEM referente ao tipo de transacao:
	 *		idRetorno = 0 => Retorna DES_ORIGEM;
	 *		idRetorno = 1 => Retorna IND_MODIFICAR_DATA_EXP;
	 *		idRetorno = 2 => Retorna IND_ATIVO;
	 *		idRetorno = 3 => Retorna TIP_LANCAMENTO.
	 *
	 *	@param		idRetorno				Indica qual o campo da TBL_REC_ORIGEM será retornado
	 *	@param		tipTransacao			Tipo de transacao da recarga.
	 *	@return								Conteudo do campo indicado pelo idRetorno.
	 */
	public String getMapRecOrigem(int idRetorno, String tipTransacao)
	{
	    switch(idRetorno)
	    {
	    	case  0: return this.getMapDescRecOrigem(tipTransacao);
	    	case  1: return this.getMapIndModDataExpRecOrigem(tipTransacao);
	    	case  2: return this.getMapIndAtivoRecOrigem(tipTransacao);
	    	case  3: return this.getMapTipLancamentoRecOrigem(tipTransacao);
	    	default: return null;
	    }
	}	
	 
	/**
	 *	Retorna objeto OrigemRecarga de acordo com o tipo de transacao passado por parametro.
	 *
	 *	@param		String					tipTransacao				Tipo de transacao da recarga.
	 *	@return		OrigemRecarga										Objeto OrigemRecarga correspondente ao tipo de transacao.
	 */
	public OrigemRecarga getOrigemRecarga(String tipTransacao)
	{
		if((tipTransacao != null) && (tipTransacao.length() >= 5))
		{
		    String idCanal	= tipTransacao.substring(0, 2);
		    String idOrigem	= tipTransacao.substring(2, 5);
		    
		    Map mapCanal = (Map)super.values.get(idCanal);
		    
		    if(mapCanal != null)
		    {
		        return (OrigemRecarga)mapCanal.get(idOrigem);
		    }
		}
		
		return null;
	}
	
}
