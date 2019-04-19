package com.brt.gpp.comum.mapeamentos;

import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAdmin;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 * Novo mapeamento de ValorVoucher. Os dados contidos nesse mapeamento são oriundos da Tecnomen,
 * através da conexão TecnomenCreditAmountMap.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/07/2007
 */
public class MapValoresVoucher extends Mapeamento
{
	private static MapValoresVoucher instancia;
	
	private MapValoresVoucher() throws GPPInternalErrorException
	{
	    super();
	}
	
	/**
	 * @return Instancia do mapeamento
	 * @throws GPPInternalErrorException
	 */
	public static MapValoresVoucher getInstancia() throws GPPInternalErrorException
	{
		if (instancia == null)
			instancia = new MapValoresVoucher();
		
		return instancia;
	}

	/**
	 * Retorna um valor de voucher de acordo com os critérios de DE-PARA
	 * (tipoVoucher, tipoSaldo, valorFace)
	 * 
	 * @param tipoVoucher 	Tipo de voucher
	 * @param tipoSaldo		ID do Tipo de Saldo
	 * @param valorFace		Valor de face
	 * @return Instancia de <code>ValorVoucher</code>
	 */
	public ValorVoucher getValorVoucher(int tipoVoucher, int tipoSaldo, int valorFace)
	{
		
		String key = String.valueOf(tipoVoucher) + 
					 String.valueOf(tipoSaldo) + 
					 String.valueOf(valorFace);
		
		return (ValorVoucher)super.values.get(key);
	}
	
	/**
	 * Popula o hashmap com os valores existentes na Tecnomen
	 */
	public void load() throws GPPInternalErrorException
	{		
		TecnomenAdmin conexaoTecnomen = null;
        GerentePoolTecnomen gerenteTecnomen = null;
        
        try
        {
        	gerenteTecnomen = GerentePoolTecnomen.getInstance();
            conexaoTecnomen = gerenteTecnomen.getTecnomenAdmin(0);
            
            // Lista os tipos saldo do Mapping e verifica se existe
            // um idTipoSaldoVoucher válido ( >= 0). Caso sim, é feita
            // a consulta na tecnomen para obter a lista de valoresVoucher
            // para o dado idTipoSaldoVoucher (accountType)
            
            Iterator it = MapTipoSaldo.getInstance().values.values().iterator();
            
            while (it.hasNext())
            {
            	TipoSaldo tipoSaldo = (TipoSaldo)it.next();
            	if (tipoSaldo.getIdtTipoSaldoVoucher() >= 0)
            	{
            		Collection lista = conexaoTecnomen.getValoresVoucher(tipoSaldo);
            		
            		for (Iterator it2 = lista.iterator(); it2.hasNext(); )
            		{
            			ValorVoucher valorVoucher = (ValorVoucher)it2.next();
            			super.values.put(valorVoucher.toString(),valorVoucher);
            		}
            	}
            }           
        }
        catch(Exception e)
		{
			throw new GPPInternalErrorException("MapValoresVoucher. Excecao: " + e);
		}
		finally
		{
			gerenteTecnomen.liberaConexaoAdmin(conexaoTecnomen, 0);
		} 
	}	
	
	/**
	 * Sobrescreve o getAll() de Mapeamento, de forma a permitir
	 * o retorno de todas as entidades do mapeamento, independente
	 * delas serem ou não derivadas de Entidade.
	 */
	public Collection getAll() 
	{
		return super.values.values();
	}
}
