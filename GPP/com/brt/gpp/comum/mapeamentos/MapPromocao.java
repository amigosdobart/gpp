package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoPacoteBonus;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.MapPromocaoCategoria;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimite;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimiteDinamico;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;

/**
 *	Mapeamento da tabela TBL_PRO_PROMOCAO.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date 		27/04/2005
 *	@modify		Primeira versao.
 *
 *	versao para compatibilizacao com o CD do Controle Total
 *
 *	@version	1.1
 *	@author		Joao Paulo Galvagni
 *  @date		05/11/2007
 *  @modify		Inclusao dos atributos necessarios para a Promocao Natal 2007 coluna idt_promocao_sag na tabela 
 *  			TBL_PRO_PACOTE_BONUS e entidade promocao_pacote_bonus.
 *  
 *	@version	1.2
 *	@author		Daniel Ferreira
 *	@date 		19/03/2008
 *	@modify		Inclusao de multiplas bonificacoes.
 */
public final class MapPromocao extends Mapeamento
{
	
	/**
	 *	Instancia do singleton.
	 */
	private static MapPromocao instance = null;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapPromocao() throws GPPInternalErrorException 
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static synchronized MapPromocao getInstancia() 
	{
		try
		{
			if(MapPromocao.instance == null)
			{
			    MapPromocao.instance = new MapPromocao();
				//Preenche a lista de limites da promocao.
			    MapPromocao.instance.loadLimites();
				//Preenche o limite de segundos da promocao.
			    MapPromocao.instance.loadLimitesSegundos();
			}
			
			return MapPromocao.instance;
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
		PREPConexao					conexaoPrep			= null;
		MapPromocaoCategoria		mapCategoria		= MapPromocaoCategoria.getInstance();
		MapPromocaoLimiteDinamico	mapLimiteDinamico	= MapPromocaoLimiteDinamico.getInstance();
        
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			String sql = "SELECT 						" +
						 "  IDT_PROMOCAO, 				" +
						 "	IDT_PROMOCAO_CRM, 			" +
						 "	IDT_PROMOCAO_SAG, 			" +
						 "	ID_PROCESSO_BATCH, 			" +
						 "	IDT_CATEGORIA, 				" +
						 "  NOM_PROMOCAO, 				" +
						 "  DES_PROMOCAO,				" +
						 "  DAT_INICIO, 				" +
						 "  DAT_FIM, 					" +
						 "  DAT_INICIO_VALIDADE,		" +
						 "  DAT_FIM_VALIDADE, 			" +
						 "  TIP_ESPELHAMENTO, 			" +
						 "  VLR_BONUS, 					" +
						 "  IND_LIMITE_DINAMICO, 		" +
						 "  IND_PRIMEIRA_RECARGA_OBR 	" +
			             "  FROM TBL_PRO_PROMOCAO P		" ;
			
			ResultSet registros = conexaoPrep.executaPreparedQuery(sql, null,0);
			
			while (registros.next())
			{
				Promocao promocao = new Promocao();
				
				int idtPromocao = registros.getInt("IDT_PROMOCAO");
				promocao.setIdtPromocao(idtPromocao);
				
				promocao.setIdtPromocaoCrm(registros.getString("IDT_PROMOCAO_CRM"));
				promocao.setIdtPromocaoSag(registros.getString("IDT_PROMOCAO_SAG"));
				promocao.setIdProcessoBatch(registros.getInt("ID_PROCESSO_BATCH"));
				promocao.setCategoria(mapCategoria.getPromocaoCategoria(registros.getInt("IDT_CATEGORIA")));
				promocao.setNomPromocao(registros.getString("NOM_PROMOCAO"));
				promocao.setDesPromocao(registros.getString("DES_PROMOCAO"));
				promocao.setDatInicio(registros.getDate("DAT_INICIO"));
				promocao.setDatFim(registros.getDate("DAT_FIM"));
				promocao.setDatInicioValidade(registros.getDate("DAT_INICIO_VALIDADE"));
				promocao.setDatFimValidade(registros.getDate("DAT_FIM_VALIDADE"));
				promocao.setTipEspelhamento(registros.getString("TIP_ESPELHAMENTO"));
				promocao.setVlrBonus(registros.getDouble("VLR_BONUS"));
				promocao.setIndPrimeiraRecargaObr(registros.getInt("IND_PRIMEIRA_RECARGA_OBR") != 0);
				promocao.setLimiteDinamico(mapLimiteDinamico.getPromocaoLimiteDinamico(registros.getInt("IND_LIMITE_DINAMICO")));
				
				this.loadDiasSemana(promocao, conexaoPrep);
				this.loadTiposChamada(promocao, conexaoPrep);
				this.loadPacotesBonus(promocao, conexaoPrep);
				
				super.values.put(new Integer(idtPromocao), promocao);
			}
			
			registros.close();
			registros = null;
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
	
	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#reload()
	 */
	public void reload() throws GPPInternalErrorException
	{
		super.reload();
		//Preenche a lista de limites da promocao.
	    MapPromocao.instance.loadLimites();
		//Preenche o limite de segundos da promocao.
	    MapPromocao.instance.loadLimitesSegundos();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#refresh()
	 */
	public void refresh() throws GPPInternalErrorException
	{
		super.refresh();
		//Preenche a lista de limites da promocao.
	    MapPromocao.instance.loadLimites();
		//Preenche o limite de segundos da promocao.
	    MapPromocao.instance.loadLimitesSegundos();
	}
	
	/**
	 *	Preenche a lista de limites de cada promocao.
	 */
	private void loadLimites()
	{
		MapPromocaoLimite mapLimite = MapPromocaoLimite.getInstance();
		
		for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
		{
			Promocao promocao = (Promocao)iterator.next();
			promocao.setLimite(mapLimite.getPromocaoLimites(promocao));
		}
	}
	
	/**
	 *	Preenche a lista de limites de segundos de cada promocao.
	 */
	private void loadLimitesSegundos()
	{
		try
		{
			MapPromocaoLimiteSegundos mapLimiteSegundos = MapPromocaoLimiteSegundos.getInstance();
			
			for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
			{
				Promocao promocao = (Promocao)iterator.next();
				promocao.setLimiteSegundos(mapLimiteSegundos.getLimitesByIdPromocao(promocao.getIdtPromocao()));
			}
		}
		catch(Exception ignored){}
	}
	
	/**
	 *	Preenche a lista de dias da semana.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		conexao					Conexao com o banco de dados.
	 *	@throws		GPPInternalErrorException, SQLException
	 */
	private void loadDiasSemana(Promocao promocao, PREPConexao conexao) throws GPPInternalErrorException, SQLException
	{
		String sql = 
			 "SELECT IDT_DIA_SEMANA " +
			 "	FROM TBL_PRO_DIA_SEMANA D " +
			 " WHERE D.IDT_PROMOCAO = ? ";
		
		Object[] params =
		{
			new Integer(promocao.getIdtPromocao())
		};
		
		ResultSet registros = conexao.executaPreparedQuery(sql, params, 0);

        while(registros.next())
        	promocao.addDiaSemana(registros.getInt("IDT_DIA_SEMANA"));
        
        registros.close();
	}
	
	/**
	 *	Preenche a lista de tipos de chamada.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		conexao					Conexao com o banco de dados.
	 *	@throws		GPPInternalErrorException, SQLException
	 */
	private void loadTiposChamada(Promocao promocao, PREPConexao conexao) throws GPPInternalErrorException, SQLException
	{
		String sql = 
			 "SELECT RATE_NAME " +
			 "  FROM TBL_PRO_RATE_NAME R " +
			 " WHERE R.IDT_PROMOCAO = ?";
		
		Object[] params =
		{
			new Integer(promocao.getIdtPromocao())
		};
		
		ResultSet registros = conexao.executaPreparedQuery(sql, params, 0);

        while(registros.next())
        	promocao.addTipoChamada(registros.getString("RATE_NAME"));
        
        registros.close();
	}
	
	/**
	 *	Preenche a lista de pacotes de bonus.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		conexao					Conexao com o banco de dados.
	 *	@throws		GPPInternalErrorException, SQLException
	 */
	private void loadPacotesBonus(Promocao promocao, PREPConexao conexao) throws GPPInternalErrorException, SQLException
	{
		String sql =
			 "SELECT B.ID_BONUS, B.IDT_PROMOCAO " +
			 "      ,B.IDT_CODIGO_NACIONAL, B.DAT_INI_VIGENCIA " +
			 "      ,B.DAT_FIM_VIGENCIA, B.IDT_TIPO_SALDO " +
			 "	    ,B.VLR_BONUS, B.ID_CANAL, B.ID_ORIGEM " +
			 "  FROM TBL_PRO_PACOTE_BONUS B " +
			 " WHERE B.idt_promocao = ? ";

		Object[] params =
		{
			new Integer(promocao.getIdtPromocao())
		};
		
		ResultSet pacoteBonusRs = conexao.executaPreparedQuery(sql, params, 0);

        while(pacoteBonusRs.next())
        {
        	PromocaoPacoteBonus promoPacoteBonus = new PromocaoPacoteBonus();
        	
        	promoPacoteBonus.setIdBonus(pacoteBonusRs.getInt("ID_BONUS"));
        	promoPacoteBonus.setPromocao(promocao);
        	promoPacoteBonus.setCodigoNacional((CodigoNacional)(MapCodigoNacional.getInstance().get(
        										new Object[]{new Integer(pacoteBonusRs.getInt("IDT_CODIGO_NACIONAL"))})));
        	promoPacoteBonus.setDataInicioVigencia(pacoteBonusRs.getDate("DAT_INI_VIGENCIA"));
        	promoPacoteBonus.setDataFimVigencia(pacoteBonusRs.getDate("DAT_FIM_VIGENCIA"));
        	promoPacoteBonus.setTipoSaldo(MapTipoSaldo.getInstance().getTipoSaldo(pacoteBonusRs.getShort("IDT_TIPO_SALDO")));
        	promoPacoteBonus.setValorBonus(pacoteBonusRs.getDouble("VLR_BONUS"));
        	promoPacoteBonus.setIdCanal(pacoteBonusRs.getString("ID_CANAL"));
        	promoPacoteBonus.setIdOrigem(pacoteBonusRs.getString("ID_ORIGEM"));
        	
        	promocao.addPromocaoPacoteBonus(promoPacoteBonus);
        }
		
        pacoteBonusRs.close();
	}
	
	/**
	 *	Retorna objeto Promocao representando o registro.
	 *
	 *	@param		idtPromocao				Identificador da promocao.
	 *	@return		Objeto Promocao correspondente.
	 */
	public Promocao getPromocao(int idtPromocao)
	{
	    return this.getPromocao(new Integer(idtPromocao));
	}
	
	/**
	 *	Retorna objeto Promocao representando o registro.
	 *
	 *	@param		idtPromocao				Identificador da promocao.
	 *	@return		Objeto Promocao correspondente.
	 */
	public Promocao getPromocao(Integer idtPromocao)
	{
		return (Promocao)super.values.get(idtPromocao);
	}
	
	/**
	 *	Retorna uma lista contendo todas as promocoes.
	 *
	 *	@return		Lista de todas as promocoes cadastradas.
	 */
	public Collection getListaPromocoes()
	{
	    ArrayList result = new ArrayList();
	    
	    result.addAll(super.values.values());
	    
	    return result;
	}
	
	/**
	 *	Retorna uma lista contendo todas as promocoes da categoria passada por parametro.
	 *
	 *	@param		categoria				Categoria de promocoes a serem consultadas.
	 *	@return		Lista de todas as promocoes cadastradas.
	 */
	public Collection getPromocoes(PromocaoCategoria categoria)
	{
	    ArrayList result = new ArrayList();

	    for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
	    {
	    	Promocao promocao = (Promocao)iterator.next();
	    	
	    	if(promocao.getCategoria().equals(categoria))
	    		result.add(categoria);
	    }
	    
	    return result;
	}
	
	/**
	 *	Retorna uma lista contendo todas as promocoes com periodo de cadastro valido para a data passada por parametro.
	 *
	 *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return		Lista de todas as promocoes cadastradas.
	 */
	public Collection getPromocoes(Date datProcessamento)
	{
	    ArrayList result = new ArrayList();
	    
        for(Iterator i = super.values.values().iterator(); i.hasNext();)
        {
            Promocao promocao = (Promocao)i.next();

            if(promocao.isVigente(datProcessamento))
                result.add(promocao);
        }
	    
	    return result;
	}
	
	/**
	 *	Retorna uma lista contendo todas as promocoes com periodo de cadastro valido para a data passada por parametro.
	 *
	 *	@param		categoria				Categoria de promocoes a serem consultadas.
	 *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return		Lista de todas as promocoes cadastradas.
	 */
    public Collection getPromocoes(PromocaoCategoria categoria, Date datProcessamento)
    {
        ArrayList result = new ArrayList();
        
        for(Iterator i = super.values.values().iterator(); i.hasNext();)
        {
            Promocao promocao = (Promocao)i.next();
            
            if((promocao.getCategoria().equals(categoria)) && (promocao.isVigente(datProcessamento)))
            	result.add(promocao);
        }
        
        return result;
    }
    
	/**
	 *	Retorna o identificador da promocao referente a promocao CRM.
	 *
	 *	@param		idtPromocaoCrm			Identificador da promocao.
	 *	@return		Identificador da promocao.
	 */
	public int getIdPromocao(String idtPromocaoCrm)
	{
		for(Iterator i = super.values.values().iterator(); i.hasNext();)
		{
			Promocao promocao = (Promocao)i.next();
			
			if(idtPromocaoCrm.equals(promocao.getIdtPromocaoCrm()))
				return promocao.getIdtPromocao();
		}
		
		return -1;
	}
	
	/**
	 *	Retorna o identificador da promocao referente a promocao SAG.
	 *
	 *	@param		idtPromocaoSag			Identificador da promocao no SAG.
	 *	@return		Identificador da promocao.
	 */
	public int getIdPromocaoByPromocaoSag(String idtPromocaoSag)
	{
		for(Iterator i = super.values.values().iterator(); i.hasNext();)
		{
			Promocao promocao = (Promocao)i.next();
			
			if(idtPromocaoSag.equals(promocao.getIdtPromocaoSag()))
				return promocao.getIdtPromocao();
		}
		
		return -1;
	}
	
}