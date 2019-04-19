package com.brt.gpp.comum.mapeamentos;

//Imports Java.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//Imports GPP.
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.ValorServico;

/**
 *	Mapeamento dos registros da tabela tbl_ger_valor_servico em memoria.
 *
 *	@author		Marcelo Alves Araujo
 *	@since 		20/07/2006
 */
public final class MapValorServico extends Mapeamento
{

    private static MapValorServico instance = null;
    
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapValorServico() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		MapCodigoCobranca		instance					Mapeamento da tabela do banco de dados.
	 *	@throws		GPPInternalErrorException
	 */
	public static MapValorServico getInstance() throws GPPInternalErrorException 
	{
		if(MapValorServico.instance == null) 
		{
		    MapValorServico.instance = new MapValorServico();
		}
		
		return MapValorServico.instance;
	}	
	
	/**
	 *	Carrega em memória os codigos de cobrança de serviços.
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
			String sqlQuery =	"SELECT                     " +
						 		"	id_servico,             " +
						 		"	vlr_servico,            " +
						 		"	idt_codigo_nacional,    " +
						 		"	idt_plano_preco,        " +
						 		"	dat_ini_validade,       " +
						 		"	dat_fim_validade,       " +
						 		"	id_canal,               " +
						 		"	id_origem,	            " +
						 		"	idt_operacao            " +
						 		"FROM tbl_ger_valor_servico ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				ValorServico codigoCobranca = new ValorServico();
				
				String idServico = registros.getString("id_servico");
				codigoCobranca.setIdServico(idServico);
				
				Double valorCobranca = new Double(registros.getDouble("vlr_servico"));
				codigoCobranca.setValorServico(valorCobranca);
				
				Integer codigoNacional = new Integer(registros.getInt("idt_codigo_nacional"));
				codigoCobranca.setCodigoNacional(codigoNacional);
				
				Integer planoPreco = new Integer(registros.getInt("idt_plano_preco"));
				codigoCobranca.setPlanoPreco(planoPreco);
				
				Date dataInicioValidade = registros.getDate("dat_ini_validade");
				codigoCobranca.setDataInicioValidade(dataInicioValidade);
				
				Date dataFimValidade = registros.getDate("dat_fim_validade");
				codigoCobranca.setDataFimValidade(dataFimValidade);

				String idOrigem = registros.getString("id_origem");
				codigoCobranca.setIdOrigem(idOrigem);
				
				String idCanal = registros.getString("id_canal");
				codigoCobranca.setIdCanal(idCanal);
				
				String operacao = registros.getString("idt_operacao");
				codigoCobranca.setOperacao(operacao);

				Map mapServico = (Map)super.values.get(idServico);
				if(mapServico == null)
				{
					mapServico = Collections.synchronizedMap(new HashMap());
				    super.values.put(idServico, mapServico);
				}
				
				Map mapCn = (Map)mapServico.get(codigoNacional);
				if(mapCn == null)
				{
				    mapCn = Collections.synchronizedMap(new HashMap());
				    mapServico.put(codigoNacional, mapCn);
				}
				
				Map mapPlano = (Map)mapCn.get(planoPreco);
				if(mapPlano == null)
				{
				    mapPlano = Collections.synchronizedMap(new HashMap());
				    mapCn.put(planoPreco, mapPlano);
				}
				
				Map mapOperacao = (Map)mapPlano.get(operacao);
				if(mapOperacao == null)
				{
					mapOperacao = Collections.synchronizedMap(new HashMap());
					mapPlano.put(operacao, mapOperacao);
				}
				
				mapOperacao.put(new Integer(codigoCobranca.hashCode()), codigoCobranca);
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
	 *	Retorna o valor do valor do serviço.
	 *
	 *	@param		String 					idServico					Identificação do serviço
	 *	@param		int						idtCodigoNacional			Codigo Nacional do assinante.
	 *	@param		int						idtPlanoPreco				Plano de Preco do assinante.
	 *	@param		Date					datProcessamento			Data de processamento da operacao.
	 *	@param		String					operacao					C: Consulta, D: Débito, E: Estorno
	 *	@return		ValorServico										Objeto Valor do Serviço correspondente.
	 */
	public ValorServico getServico(String idServico, int idtCodigoNacional, int idtPlanoPreco, Date datProcessamento, String operacao)
	{
		operacao = (operacao == null || Definicoes.OPERACAO_CONSULTA.equalsIgnoreCase(operacao)) ? Definicoes.OPERACAO_ESTORNO : operacao;
		
		Map mapServico = (Map)super.values.get(idServico);
		
		if(mapServico != null)
		{
			Map mapCn = (Map)mapServico.get(new Integer(idtCodigoNacional));
	    
		    if(mapCn != null)
		    {
		        Map mapPlano = (Map)mapCn.get(new Integer(idtPlanoPreco));
		        
		        if(mapPlano != null)
		        {
		        	Map mapOperacao = (Map)mapPlano.get(operacao.toUpperCase());
		        	
		        	if(mapOperacao != null)
			        {			            
			            Collection listaServico = mapOperacao.values();
			            
			            for(Iterator iterator = listaServico.iterator(); iterator.hasNext();)
			            {
			            	ValorServico valor = (ValorServico)iterator.next();
			                
			                if(valor.isVigente(datProcessamento))
			                {
			                    return valor;
			                }
			            }
			        }
		        }
		    }
		}
		
		return null;
	}

	/**
	 *	Retorna o valor do valor do serviço.
	 *
	 *	@param		String 					idServico					Identificação do serviço
	 *	@param		Integer					idtCodigoNacional			Codigo Nacional do assinante.
	 *	@param		Integer					idtPlanoPreco				Plano de Preco do assinante.
	 *	@param		Date					datProcessamento			Data de processamento da operacao.
	 *	@param		String					operacao					C: Consulta, D: Débito, E: Estorno
	 *	@return		ValorServico										Objeto Valor do Serviço correspondente.
	 */
	public ValorServico getServico(String idServico, Integer idtCodigoNacional, Integer idtPlanoPreco, Date datProcessamento, String operacao)
	{
		operacao = (operacao == null || Definicoes.OPERACAO_CONSULTA.equalsIgnoreCase(operacao)) ? Definicoes.OPERACAO_ESTORNO : operacao;
		
		Map mapServico = (Map)super.values.get(idServico);
		
		if(mapServico != null)
		{
			Map mapCn = (Map)mapServico.get(idtCodigoNacional);
	    
		    if(mapCn != null)
		    {
		        Map mapPlano = (Map)mapCn.get(idtPlanoPreco);
		        
		        if(mapPlano != null)
		        {
		        	Map mapOperacao = (Map)mapPlano.get(operacao.toUpperCase());
		        	
		        	if(mapOperacao != null)
			        {
			            Collection listaBonus = mapOperacao.values();
			            
			            for(Iterator iterator = listaBonus.iterator(); iterator.hasNext();)
			            {
			            	ValorServico valor = (ValorServico)iterator.next();
			                
			                if(valor.isVigente(datProcessamento))
			                {
			                    return valor;
			                }
			            }
			        }
		        }
		    }
		}
		
		return null;
	}
}