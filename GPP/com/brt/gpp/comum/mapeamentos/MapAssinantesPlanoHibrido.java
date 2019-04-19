package com.brt.gpp.comum.mapeamentos;

//Java Imports
import java.sql.*;
import java.util.HashMap;

//GPP Imports
import com.brt.gpp.gerentesPool.GerentePoolBancoDados; 
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao; 
import com.brt.gpp.comum.gppExceptions.*;

/**
*
* Este arquivo contem a classe que faz o mapeamento dos assinantes que
* possuem plano híbrido
* <P> Version:		1.0
*
* @Autor: 		Denys Oliveira
* Data: 			09/12/2004
*/
public class MapAssinantesPlanoHibrido 
{
	//	Variaveis Membro
	private 		GerentePoolBancoDados	gerenteBancoDados 	= null; // Gerente de conexoes Banco Dados
//	private 		String					nomeClasse 			= null;
	private 		HashMap					hashSubPlanoHibrido	= null;
	private			long					idProcesso;
	
	/**
	 * Metodo...: MapValoresRecarga
	 * Descricao: Construtor
	 * @param 	long	aIdProcesso		Identificador do Processo para efeitos de log
	 * @throws GPPInternalErrorException
	 */
	public MapAssinantesPlanoHibrido (long idProcesso) throws GPPInternalErrorException 
	{
		//Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(idProcesso);
		this.hashSubPlanoHibrido    = new HashMap();
		this.idProcesso = idProcesso;
		montaMapeamento();
	}

	/**
	 * Metodo...: montaMapeamento
	 * Descricao: Carrega a HashTable de Mapeamento com os valores do BD
	 * @throws GPPInternalErrorException
	 */
	private void montaMapeamento() throws GPPInternalErrorException 
	{
		PREPConexao conexaoPrep = null;
		try
		{
			//Seleciona conexao do pool Prep Conexao
			conexaoPrep = gerenteBancoDados.getConexaoPREP(idProcesso);
			//Seleciona planos de precos validos no BD do GPP
			String sql = "SELECT IDT_MSISDN FROM TBL_APR_PLANO_HIBRIDO";

			ResultSet rs = conexaoPrep.executaPreparedQuery(sql, null,idProcesso);
//			int chaveMap = 0;
			while (rs.next())
			{
				// Insere o valor no objeto Hash
				hashSubPlanoHibrido.put(rs.getString("IDT_MSISDN"),null);
			}
			rs.close();
			rs = null;
		}
		catch (Exception e1)
		{
			throw new GPPInternalErrorException ("Excecao Interna do GPP ocorrida: " + e1);			 
		}
		finally
		{
			//Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, idProcesso);
		}
	}
	
	/**
	 * Metodo...: atualizaMapeamentoValoresRecarga
	 * Descricao: Atualiza o Mapeamento de Valores de Recarga na memória
	 * @throws GPPInternalErrorException
	 */
	public synchronized void atualizaMapeamentoValoresRecarga() throws GPPInternalErrorException 
	{
		// Caso o metodo seja invocado entao todos os dados existentes
		// em banco de dados devem ser carregados novamente pra memoria,
		// para isso o hash entao e atualizado com novos valores
		montaMapeamento();
	}

	/**
	 * Metodo....:ehPlanoHibrido
	 * Descricao.:Verifica se um msisdn está na TLB_APR_PLANO_HIBRIDO
	 * @param 	String		msisdn			- Msisdn do assinante
	 * @return boolean						- true se o assinante tem plano híbrido
	 */
	public boolean ehPlanoHibrido(String msisdn)
	{
		return hashSubPlanoHibrido.containsKey(msisdn);
	}
}
