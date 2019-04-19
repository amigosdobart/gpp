//Definicao de Pacotes
package com.brt.gpp.comum.conexoes.bancoDados;

// Arquivos de Imports do GPP
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

// Arquivos de Imports de Java
import java.sql.DriverManager;
import java.sql.SQLException;

/**
  * 
  * Este arquivo inclui a conexao o banco de dados Oracle do Pre-pago. E responsavel por implementar metodos
  * abstratos da classe ConexaoBancoDados e popular dados de conexao ao banco (usuario, senha, instancia, porta, etc) 
  *
  * <P> Versao:			1.0
  * 
  * @Autor: 			Daniel Cintra Abib
  * Date: 				24/02/2004
  *                       
  * Modificado Por:
  * Data:
  * Razao:
  */

final public class PREPConexao extends ConexaoBancoDados
{
	public static final String 	meuNome = Definicoes.CO_BANCO_DADOS_PREP;

	/**
	 * Metodo...: PREPConexao
	 * Descricao: Construtor - Inicia o drive manager e cria a instancia de banco de dados
	 * @param 	idProcesso - Numero indicador do processo
	 * @return									
	 */
	public PREPConexao (long idProcesso) throws GPPInternalErrorException
	{
		this.abreConexao(idProcesso);
	}

	/**
 	 * Metodo...: abreConexao
	 * Descricao: Cria um objeto de conexao com o Banco de Dados
	 * @param idProcesso	- Numero indicador do processo
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public void abreConexao(long idProcesso) throws GPPInternalErrorException
	{
		try
		{    
			// Inicializacoes de Singletons (Gerente de Log, Arquivo de Configuracao, etc...)
			super.init();

			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver ());            
			this.minhaConexao =  DriverManager.getConnection ( ConexaoBancoDados.arqConfiguracao.getURLBancoDados(), 
								 ConexaoBancoDados.arqConfiguracao.getNomeUsuarioBancoDados(), 
								 ConexaoBancoDados.arqConfiguracao.getSenhaUsuarioBancoDados());
                                 
			// Associa um flag de conexao valida
			super.setEValido (true);
		}
		catch ( SQLException sqlException )
		{
			super.minhaConexao = null;
			super.codigoErro = sqlException.getErrorCode();
			super.setEValido (false);
			super.Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "PREPConexao", "Erro conexao JDBC criando objeto de conexao de banco de dados." );

			throw new GPPInternalErrorException ("PREPConexao() - Erro conexao JDBC criando objeto de conexao de banco de dados: " + sqlException);
		}
		catch ( Exception e )
		{
			super.setEValido (false);
			super.Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "PREPConexao", "Erro interno criando objeto de conexao de banco de dados." );

			throw new GPPInternalErrorException ("PREPConexao() - Erro interno criano objeto de conexao de banco de dados." + e);
		}
		finally
		{
			this.setNome ( meuNome );
		}
	}
	
	/**
	 * Metodo...: reconecta
	 * Descricao: Faz a reconexao com a base de dados, utilizando um  
	 * 			  numero maximo de tentativas antes de retornar um erro
	 * @param 	idProcesso	- Numero indicador do processo
	 * @return	boolean		- True se reconectou com sucesso e False caso contrário
	 * @throws 
	 */
	public boolean reconecta(long idProcesso)
	{
		boolean devoSair = false;
		boolean conseguiuConexao = false;
		
		int numTentativas = 1;
		int maxTentativas = arqConfiguracao.getNumeroTentativasConexao();
		
		Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Inicio RECONEXAO com BD");
		while (!devoSair)
		{
			try
			{
				//Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Tentativa reconectar de conexao com a base de dados");
				this.abreConexao(idProcesso);
				conseguiuConexao = true;
				break; // forca a saida do while
			}
			catch (Exception e)
			{
				Log.log (idProcesso, Definicoes.WARN, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Erro RECONEXAO com BD. Tentativa numero:" + numTentativas);
			}
			
			if ( numTentativas <= maxTentativas )
			{
				numTentativas++;
				Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Tentativa numero " +  numTentativas + " conexao com BD");
				
				try
				{
					Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Dormindo " + arqConfiguracao.getTempoMaximoEspera() + " segundos antes da nova tentativa conexao BD");
					Thread.sleep(arqConfiguracao.getTempoMaximoEspera() * 1000);
				}
				catch ( java.lang.InterruptedException javaException )
				{
					Log.log (idProcesso, Definicoes.WARN, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Erro enquanto dormia para esperar reconexao BD");
				}
			}
			else
				devoSair = true;
		}
		
		if ( !conseguiuConexao )
			Log.log (idProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "NAO conseguiu reconectar BD.");
		else
			Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Conseguiu reconectar BD");
		
		Log.log (idProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "reconecta", "Fim Reconexao BD");
		return conseguiuConexao;
	}
	
	/**
	 * Metodo...: reconecta
	 * Descricao: Associa o nome que sera mostrado no LOG da aplicacao
	 * @param 	aNomeClasse - O nome da classe informado
	 * @return	
	 * @throws 
	 */
	public void setNome ( String aNomeClasse )
	{
		super.setNomeClasse ( aNomeClasse );	
	}
}