package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;

/**
 *	Mapeamento dos registros da tabela TBL_GER_CODIGO_NACIONAL em memoria.
 *
 *	@author		Daniel Ferreira
 *	@since 		23/11/2005
 *
 *  Atualizado por: Bernardo Vergne Dias
 *  Data: 22/02/2007
 */
public final class MapCodigoNacional extends Mapeamento
{

    private static MapCodigoNacional instance = null;
    
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapCodigoNacional() throws GPPInternalErrorException 
	{
	    super();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapCodigoNacional getInstance() 
	{
	    try
	    {
			if(MapCodigoNacional.instance == null)
				MapCodigoNacional.instance = new MapCodigoNacional();

	    	return MapCodigoNacional.instance;
	    }
	    catch(Exception e)
	    {
	        return null;
	    }
	}	

	/**
	 * Metodo....:buscarCodigosRegiaoBrt
	 * Descricao.:Busca os codigos nacionais atendidos pela Brt
	 * @return Collection - Lista de codigos da regiao Brt
	 */
	public Collection buscarCodigosRegiaoBrt()
	{
		// Realiza a iteracao entre todos os objetos CodigoNacional da lista
		// em memoria. Para cada Objeto eh verificado se o campo que indica
		// se o codigo nacional faz parte da regiao atendia pela Brt estah 
		// marcado, caso verdadeiro entao adiciona o objeto a uma outra lista
		// a ser retornada pelo metodo
		Collection codigosBrt = new ArrayList();
		for (Iterator i=this.getAll().iterator(); i.hasNext();)
		{
			CodigoNacional codNacional = (CodigoNacional)i.next();
			if (codNacional.getIndRegiaoBrt().intValue() == 1)
				codigosBrt.add(codNacional);
		}
		return codigosBrt;
	}
	
	/**
	 *	Carrega o mapeamento de codigos nacionais em memoria.
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
			String sqlQuery =	"SELECT idt_codigo_nacional, " +
						 		"       idt_filial, " +
						 		"       ind_regiao_brt, " +
						 		"       idt_fuso, " +
						 		"       idt_uf," +
						 		"       idt_regiao_anatel, " +
						 		"       idt_regiao_origem " +
						 		"  FROM tbl_ger_codigo_nacional ";

			ResultSet registros = conexaoPrep.executaPreparedQuery(sqlQuery, null, 0);
			
			while (registros.next())
			{
				CodigoNacional codigoNacional = new CodigoNacional();
				
				Integer idtCodigoNacional = new Integer(registros.getInt("idt_codigo_nacional"));
				codigoNacional.setIdtCodigoNacional(idtCodigoNacional);
				
				String idtFilial = registros.getString("idt_filial");
				codigoNacional.setIdtFilial(idtFilial);
				
				Integer indRegiaoBrt = new Integer(registros.getInt("ind_regiao_brt"));
				codigoNacional.setIndRegiaoBrt(indRegiaoBrt);
				
				int idtFuso = registros.getInt("idt_fuso");
				codigoNacional.setIdtFuso(registros.wasNull() ? null : new Integer(idtFuso));
				
				String idtUf = registros.getString("idt_uf");
				codigoNacional.setIdtUf(idtUf);
				
				int idtRegiaoOrigem = registros.getInt(("idt_regiao_origem"));
				codigoNacional.setIdtRegiaoOrigem(registros.wasNull() ? null : new Integer(idtRegiaoOrigem));
				
				String idtRegiaoAnatel = registros.getString("idt_regiao_anatel");
				codigoNacional.setIdtRegiaoAnatel(idtRegiaoAnatel);
				
				//Inserindo o registro no Map.
				super.values.put(idtCodigoNacional, codigoNacional);
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
	
}
