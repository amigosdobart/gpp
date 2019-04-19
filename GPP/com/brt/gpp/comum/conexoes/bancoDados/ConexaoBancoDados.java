//Definicao de Pacotes
package com.brt.gpp.comum.conexoes.bancoDados;

// Arquivo de Import do GPP
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.DadosConexao;
import com.brt.gpp.comum.conexoes.bancoDados.DadosConexaoBD;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.gerentesPool.GerentePoolLog;

// Arquivos de Import do Java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
  * 
  * Este arquivo incluiu a definicao da Classe de Conexao ao Banco de Dados (Classe Abstrata). 
  * Esta classe e esponsavel pela definicao dos metodos e parametros virtuais para 
  * acesso a banco de dados (oracle) 
  *
  * <P> Versao:			1.0
  * 
  * @Autor: 			Daniel Cintra Abib
  * Data: 				24/02/2004
  *
  * Modificado Por:		Gustavo Gusmao
  * Data:				12/12/2005
  * Razao:				Modificacao para permitir qualquer numero de Statements aninhados
  *  
  */

abstract public class ConexaoBancoDados
{
    List listStatement = null;
    // Variaveis Membro
	protected static ArquivoConfiguracaoGPP arqConfiguracao = null;		// Referencia ao arquivo de configuracao
	protected GerentePoolLog Log = null; // Gerente de LOG	
	
	protected Connection minhaConexao = null;		// Referencia a conexao ao banco de dados
	
	private String nomeClasse = null;		// O nome da classe de conexao ao banco    
	protected int codigoErro = 0;		// Error code from any method
    protected boolean eValido = false;	// Flag to identificate if a connection was created or not
    	
    private long idProcesso	= -1;		// Indica qual o Id do processo que possui a conexao. (-1) para nenhum processo
    private java.util.Date dataInicialUso = null;		// Indica a data que o processo comecou a utilizar a conexao
 
 	/**
	 * Metodo...: init
	 * Descricao: Inicializando o Singleton de Conexao a Banco de Dados 
	 * @param	
	 * @return									
	 */
	protected void init()
	{
		arqConfiguracao = ArquivoConfiguracaoGPP.getInstance( );  // Obtem uma instancia do Singleton do arquivo de configuracao
		Log = GerentePoolLog.getInstancia(getClass()); // Obtem referencia ao gerente de LOG
		listStatement = Collections.synchronizedList(new ArrayList(1000));
	}

    /**
 	 * Metodo...: setNome
	 * Descricao: Metodo para associar o nome da conexao ao banco de dados 
     * OBS: Metodo abstrato para forcar as classes filhas a definirem o metodo do nome do banco
     * @param aNomeClasse - Nome do Banco setado pela classe filha
	 * @return									
     * @throws 
     */
    abstract void setNome(String aNomeClasse);

    /**
	 * Metodo...: setNomeClasse
	 * Descricao: Associa o nome da classe no LOG 
	 * @param	aNomeClasse - Nome do classe filho
	 * @return									
     * @throws 
     */
	protected void setNomeClasse(String aNomeClasse)
	{
		this.nomeClasse = aNomeClasse;	
	}

	/** 
	 * Metodo...: setEValido
	 * Descricao: Associa o status da conexao
	 * @param aEValido 	- Indentifica o status da conexao 
	 * @return									
     * @throws 
	 */
	 protected void setEValido(boolean aEValido)
	 {
	 	this.eValido = aEValido;
	 }
	 	
    /**
 	 * Metodo...: destroiConexao
	 * Descricao: Destroe o objeto de conexao
	 * @param userOption 	- Indentifica a opcao do usuario
	 * @param aIdProcesso 	- Numero indicador do processo 
	 * @return									
     * @throws 
     */
    public void destroiConexao(int userOption, long aIdProcesso)
    {
        try
        {
			codigoErro = 0;
			liberaConexao(aIdProcesso);	            
			minhaConexao.close();
			minhaConexao = null;
        }
        catch(SQLException sqlException)
        {
			codigoErro = sqlException.getErrorCode();
        }
    }

	/**
	 * Metodo...: setIdProcesso
	 * Descricao: Metodo para identificar o id do processo que esta 
	 * 			  utilizando a conexao. O valor -1 e
	 *            atribuido quando a conexao e liberada
	 * @param idProcesso - Numero indicador do processo
	 * @return									
     * @throws 
	 */
	public void setIdProcesso(long idProcesso)
	{
		this.idProcesso = idProcesso;
		if (idProcesso == -1)
			dataInicialUso = null;
		else
		    dataInicialUso = Calendar.getInstance().getTime();
	}
	
	/**
	 * Metodo...: getIdProcesso
	 * Descricao: Metodo para retornar o id do processo que esta utilizando a conexao
	 * @param 
	 * @return	long - Numero indicador do processo								
     * @throws 
	 */
	public long getIdProcesso()
	{
		return idProcesso;
	}

	/**
	 * Metodo...: getDataInicialUso
	 * Descricao: Metodo para retornar a data inicial de uso da conexao pelo processo
	 * @param 
	 * @return	java.util.Date - Valor da data inicial								
     * @throws 
	 */
	public java.util.Date getDataInicialUso()
	{
		return dataInicialUso;	
	}
	
    /**
 	 * Metodo...: liberaConexao
	 * Descricao: Libera os ResultSets e Statements
	 * @param  aIdProcesso	- Numero indicador do processo
	 * @return							
     * @throws 
     */
    public void liberaConexao(long aIdProcesso)
    {
		Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "liberaConexao", "Inicio");
        try
        {
        	codigoErro = 0;
        	
            minhaConexao.rollback();

            long i = 0;
            for(Iterator iterator = listStatement.iterator(); iterator.hasNext(); )
            {
                Statement statement = (Statement) iterator.next();
                statement.close();
                i++;
            }
            listStatement.clear();
            listStatement = null;
            listStatement = Collections.synchronizedList(new ArrayList(1000));
            Log.logComponente(Definicoes.DEBUG, "liberaConexao", "Statements liberados:" + i);

            // Atribui -1 para indicar que nenhum processo esta utilizando
            idProcesso = -1;
			Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "liberaConexao", "Fim");
        }
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();
            
			Log.log(aIdProcesso, Definicoes.WARN, Definicoes.CO_BANCO_DADOS_PREP, "liberaConexao", "JDBC Erro(SQL) de Conexao.");
        }
    }
    
    /**
	 * Metodo...: executaQueryInterna
	 * Descricao: Cria os Statements de SQL
     * @param  	aQuery 		- O comando SQL (query) a ser executado
     * @param 	aResultSet  - Qual ResultSet sera prenchido
	 * @param   aIdProcesso	- Numero indicador do processo
     * @return 	ResultSet	- O retorno do comando (na forma de ResultSet)
     * @throws 	GPPInternalErrorException
     */
    private ResultSet executaQueryInterna(String aQuery, long aIdProcesso) throws GPPInternalErrorException
    {
        GPPResultSet gppResultSet = null;
        ResultSet rSet = null;
   		Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "executaQueryInterna", "Query: " + aQuery);
        try
        {
        	codigoErro = 0;
            if(minhaConexao != null)
            {
                Statement statement = null;
                statement = minhaConexao.createStatement();
                rSet = statement.executeQuery(aQuery);
                gppResultSet = new GPPResultSet(rSet, statement, listStatement);
                listStatement.add(statement);
            }
        }
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();            
   	    	Log.log(aIdProcesso, Definicoes.WARN, Definicoes.CO_BANCO_DADOS_PREP, "executaQueryInterna", "Erro(SQL) Conexao JDBC:" + sqlException);
			throw new GPPInternalErrorException("JDBC Connection error." + sqlException);
        }
		return gppResultSet;
    }

	/**
	 * Metodo...: executaQuery
	 * Descricao: Cria um Statement interno e chama o metodo executeQuery da conexao de banco de dados - Segunda Query
 	 * @param  	aQuery 		- A query a ser executada 
	 * @param   aIdProcesso	- Numero indicador do processo
	 * @return  ResultSet	- O resultset de retorno
     * @throws 	GPPInternalErrorException
	 */
	public ResultSet executaQuery(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaQueryInterna(aQuery, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQuery", "Erro interno GPP:" + e);
			throw new GPPInternalErrorException("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaQuery1
	 * Descricao: Cria um Statement interno e chama o metodo executeQuery da conexao de banco de dados - Segunda Query
 	 * @param  	aQuery 		- A query a ser executada 
	 * @param   aIdProcesso	- Numero indicador do processo
	 * @return  ResultSet	- O resultset de retorno
     * @throws 	GPPInternalErrorException
	 */
	public ResultSet executaQuery1(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaQueryInterna(aQuery, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQuery", "Erro interno GPP:" + e);
			throw new GPPInternalErrorException("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaQuery2
	 * Descricao: Cria um Statement interno e chama o metodo executeQuery da conexao de banco de dados - Segunda Query
 	 * @param  	aQuery 		- A query a ser executada 
	 * @param   aIdProcesso	- Numero indicador do processo
	 * @return  ResultSet	- O resultset de retorno
     * @throws 	GPPInternalErrorException
	 */
	public ResultSet executaQuery2(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaQueryInterna(aQuery, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQuery", "Erro interno GPP:" + e);
			throw new GPPInternalErrorException("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaQuery3
	 * Descricao: Cria um Statement interno e chama o metodo executeQuery da conexao de banco de dados - Segunda Query
 	 * @param  	aQuery 		- A query a ser executada 
	 * @param   aIdProcesso	- Numero indicador do processo
	 * @return  ResultSet	- O resultset de retorno
     * @throws 	GPPInternalErrorException
	 */
	public ResultSet executaQuery3(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaQueryInterna(aQuery, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQuery", "Erro interno GPP:" + e);
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaQuery4
	 * Descricao: Cria um Statement interno e chama o metodo executeQuery da conexao de banco de dados - Segunda Query
 	 * @param  	aQuery 		- A query a ser executada 
	 * @param   aIdProcesso	- Numero indicador do processo
	 * @return  ResultSet	- O resultset de retorno
     * @throws 	GPPInternalErrorException
	 */
	public ResultSet executaQuery4(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaQueryInterna(aQuery, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQuery", "Erro interno GPP:" + e);
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaQuery5
	 * Descricao: Cria um Statement interno e chama o metodo executeQuery da conexao de banco de dados - Segunda Query
 	 * @param  	aQuery 		- A query a ser executada 
	 * @param   aIdProcesso	- Numero indicador do processo
	 * @return  ResultSet	- O resultset de retorno
     * @throws 	GPPInternalErrorException
	 */
	public ResultSet executaQuery5(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaQueryInterna(aQuery, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQuery", "Erro interno GPP:" + e);
			throw new GPPInternalErrorException("Excecao interna GPP" + e);
		}
	}

	/**
	 * Metodo...: prepareQuery
	 * Descricao: Cria os PreparedStatements de SQL
	 * @param  aQuery 				- O comando SQL (query) a ser executado
	 * @param  aParams     			- Array de parametros da query
	 * @return PreparedStatement	- O retorno do comando (na forma de um PreparedStatement)
     * @throws SQLException
     * @throws GPPInternalErrorException
	 */
	private PreparedStatement prepareQuery(String aQuery, Object[] aParams, long aIdProcesso) throws SQLException, GPPInternalErrorException
	{
		PreparedStatement prepQuery = minhaConexao.prepareStatement(aQuery);
		if(aParams != null)
			for(int i=0; i < aParams.length; i++)
			{
				Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "prepareAndRunQuery", "Parametro: [" + i + "] = " + aParams[i]);
				if(aParams[i] != null)			
					// O índice do PreparedStatemet começa no número 1
					if(aParams[i] instanceof java.io.StringReader)
					{
						StringReader auxString = (StringReader) aParams[i];

						BufferedReader bf = new BufferedReader(auxString);
						int tamanho = 0;
						try 
						{
							tamanho = bf.readLine().length();
							auxString.reset();
						}
						catch(IOException e) 
						{
							Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "prepareQuery", "Erro(IO) inserindo CLOB: " + e);
							throw new GPPInternalErrorException ("Erro(IO) inserindo CLOB: " + e ); 
						}
						prepQuery.setCharacterStream(i+1, auxString, tamanho);
					}
					else
						prepQuery.setObject(i+1, aParams[i]);
				else
					// No Oracle independente do tipo de dados o valor NULL é atribuído da mesma forma
					prepQuery.setNull(i+1, Types.VARCHAR);
			}

		return prepQuery;
	}

	/**
	 * Metodo...: executaPreparedQueryInterna
	 * Descricao: Cria os PreparedStatements internos de SQL
	 * @param  	aQuery 		- O comando SQL (query) a ser executado
	 * @param  	aParams     - Array de parametros da query
	 * @return 	ResultSet	- O retorno do comando (na forma de ResultSet)
     * @throws 	GPPInternalErrorException
	 */
	private ResultSet executaPreparedQueryInterna(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		// Define o retorno do metodo	
		GPPResultSet gppResultSet = null;
		ResultSet rs = null;
		Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQueryInterna", "Query: " + aQuery);
		try
		{
		    //fecharStatementsInativos();
			codigoErro = 0;        	
			if(minhaConexao != null)
			{
				PreparedStatement pStatement = prepareQuery(aQuery, aParams, aIdProcesso);
				rs = pStatement.executeQuery();
				gppResultSet = new GPPResultSet(rs, pStatement, listStatement);
				listStatement.add(pStatement);
			}
		}
		catch(SQLException sqlException)
		{
			codigoErro = sqlException.getErrorCode();            
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaQueryInterna", "Erro(SQL) de Conexao JDBC." + sqlException);
			throw new GPPInternalErrorException("JDBC Connection error(SQL)." + sqlException);
		}
		return gppResultSet;
	}
    
	/**
	 * Metodo...: executaPreparedQueryInterna
	 * Descricao: Cria um PreparedStatement interno e chama o metodo executeQuery da conexao de banco de dados - Primeiro PreparedQuery
	 * @param  aQuery 		- A query a ser executada
	 * @param  aParams 		- Array de parametros da query
	 * @param  aIdProcesso 	- Handler do processo 
	 * @return ResultSet	- O resultset de retorno
     * @throws GPPInternalErrorException
	 */
	public ResultSet executaPreparedQuery(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaPreparedQueryInterna(aQuery, aParams, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log (aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQuery", "Erro interno GPP: " + e );
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}

	/**
	 * Metodo...: executaPreparedQueryInterna1
	 * Descricao: Cria um PreparedStatement interno e chama o metodo executeQuery da conexao de banco de dados - Primeiro PreparedQuery
	 * @param  aQuery 		- A query a ser executada
	 * @param  aParams 		- Array de parametros da query
	 * @param  aIdProcesso 	- Handler do processo 
	 * @return ResultSet	- O resultset de retorno
     * @throws GPPInternalErrorException
	 */
	public ResultSet executaPreparedQuery1(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaPreparedQueryInterna(aQuery, aParams, aIdProcesso);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log (aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQuery", "Erro interno GPP: " + e );
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaPreparedQueryInterna2
	 * Descricao: Cria um PreparedStatement interno e chama o metodo executeQuery da conexao de banco de dados - Primeiro PreparedQuery
	 * @param  aQuery 		- A query a ser executada
	 * @param  aParams 		- Array de parametros da query
	 * @param  aIdProcesso 	- Handler do processo 
	 * @return ResultSet	- O resultset de retorno
     * @throws GPPInternalErrorException
	 */
	public ResultSet executaPreparedQuery2(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaPreparedQueryInterna(aQuery, aParams, aIdProcesso);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log (aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQuery", "Erro interno GPP: " + e);
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}

	/**
	 * Metodo...: executaPreparedQueryInterna3
	 * Descricao: Cria um PreparedStatement interno e chama o metodo executeQuery da conexao de banco de dados - Primeiro PreparedQuery
	 * @param  aQuery 		- A query a ser executada
	 * @param  aParams 		- Array de parametros da query
	 * @param  aIdProcesso 	- Handler do processo 
	 * @return ResultSet	- O resultset de retorno
     * @throws GPPInternalErrorException
	 */
	public ResultSet executaPreparedQuery3(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaPreparedQueryInterna(aQuery, aParams, aIdProcesso);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log (aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQuery", "Erro interno GPP: " + e);
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaPreparedQueryInterna4
	 * Descricao: Cria um PreparedStatement interno e chama o metodo executeQuery da conexao de banco de dados - Primeiro PreparedQuery
	 * @param  aQuery 		- A query a ser executada
	 * @param  aParams 		- Array de parametros da query
	 * @param  aIdProcesso 	- Handler do processo 
	 * @return ResultSet	- O resultset de retorno
     * @throws GPPInternalErrorException
	 */
	public ResultSet executaPreparedQuery4(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaPreparedQueryInterna(aQuery, aParams, aIdProcesso);
		}
		catch(GPPInternalErrorException e)
		{
			Log.log (aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQuery", "Erro interno GPP: " + e);
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: executaPreparedQueryInterna5
	 * Descricao: Cria um PreparedStatement interno e chama o metodo executeQuery da conexao de banco de dados - Primeiro PreparedQuery
	 * @param  aQuery 		- A query a ser executada
	 * @param  aParams 		- Array de parametros da query
	 * @param  aIdProcesso 	- Handler do processo 
	 * @return ResultSet	- O resultset de retorno
     * @throws GPPInternalErrorException
	 */
	public ResultSet executaPreparedQuery5(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		try
		{
			return executaPreparedQueryInterna(aQuery, aParams, aIdProcesso);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log (aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedQuery", "Erro interno GPP: " + e);
			throw new GPPInternalErrorException ("Excecao interna GPP" + e);
		}
	}
	
	/**
	 * Metodo...: preparaChamada
	 * Descricao: Cria um Statement interno e chama o metodo para execucao de StoredProcedures
	 * @param  aProcedure 			- A StoredProcedure a ser executada 
	 * @param  aIdProcesso 			- Handler do processo 
	 * @return CallableStatement	- O resultset de retorno
     * @throws 
	 */
    public CallableStatement preparaChamada(String aProcedure, long aIdProcesso)
    {
		Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "preparecall", "Procedure: " + aProcedure);
		CallableStatement cStatement = null;
        try
        {
            if(minhaConexao != null)
            {
                liberaConexao(aIdProcesso);
                cStatement = minhaConexao.prepareCall(aProcedure);
            }
        }
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "preparecall", "Erro(SQL) na Conexao JDBC." + sqlException);
        }
        
        return(cStatement);
    }
    
   /**
 	 * Metodo...: executa
	 * Descricao: Cria um Statement interno e chama o metodo execute da conexao JDBC
     * @param  aQuery 		- O comando SQL (query) a ser executado
 	 * @param  aIdProcesso 	- Handler do processo 
     * @return boolean		- O resultset do comando SQL (query) executado
     * @throws 	GPPInternalErrorException
     */
    public boolean executa(String aQuery, long aIdProcesso) throws GPPInternalErrorException
	{
		boolean valorRetorno = false;
		    
		Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "executa", "Query: " + aQuery);
		Statement statement = null;
		try
		{
			if(minhaConexao != null)
			{
			    statement = minhaConexao.createStatement();
				statement.execute(aQuery);				
				        
				// O retorno devera ser sempre OK, ou um SQLException caso contrario
				valorRetorno = true;
			}
		}
		catch(SQLException sqlException)
		{
			codigoErro = sqlException.getErrorCode();
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executa", "JDBC Connection error(SQL)." + sqlException);
			throw new GPPInternalErrorException ("JDBC Connection error(SQL)." + sqlException);			 
		}
		finally
		{
		    try
            {
                statement.close();
            }
		    catch(SQLException e)
            {
		        Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executa", "JDBC Connection error(SQL)." + e);
            }
		}
		return valorRetorno;
	}
    
   /**
  	 * Metodo...: setAutoCommit
	 * Descricao: Associa a propriedade de AutoCommit no banco de dados
     * @param  flag		- Valor logico do flag de AutoCommit
     * @return
     * @throws SQLException
     */
    public void setAutoCommit(boolean flag) throws SQLException
    {
    	try
        {
            if(minhaConexao != null)
            {
                minhaConexao.setAutoCommit(flag);
            }
        }
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();
            throw sqlException;
        }
    }

   /**
   	 * Metodo...: commit
	 * Descricao: Executa um comando de Commit no banco de dados
     * @param  
     * @return
     * @throws SQLException
     */
    public void commit() throws SQLException
    {
		try
        {
            if(minhaConexao != null)
            {
                minhaConexao.commit();
            }
        }
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();
            throw sqlException;
		}
    }
    
   /**
  	 * Metodo...: rollback
	 * Descricao: Executa um comando de RollBack no banco de dados
     * @param  
     * @return
     * @throws SQLException
     */
    public void rollback() throws SQLException
    {
		try
        {
            if(minhaConexao != null)
            {
                minhaConexao.rollback();
            }
        }
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();
            throw sqlException;
		}
    }
    
    /**
  	 * Metodo...: getCodigoErro
	 * Descricao: Retorna o codigo do erro se qualquer comando SQL nao conseguiu ser executado
     * @param  
     * @return int 	- Retorna 0 se nao houve erro de banco de dados ou o codigo ORACLE do erro se houve 
     * @throws 
     */
    public int getCodigoErro()
    {
    	return codigoErro;	
    }
    
    /**
  	 * Metodo...: eValido
	 * Descricao: Verifica se uma conexao JDBC e valida
     * @param  
     * @return boolean	- TRUE se a conexao e valida e FALSE se nao e valida
     * @throws 
     */
    public boolean eValido()
    {
		return eValido;
    }

	/** 
  	 * Metodo...: executaUpdate
	 * Descricao: Executa um comando de UPDATE no banco de dados
	 * @param  aQuery		- Comando SQL de UPDATE a ser executado no bando de dados
 	 * @param  aIdProcesso 	- Handler do processo 
	 * @return int			- Codigo do erro retornado do banco
     * @throws 
	 */
    public int executaUpdate(String aQuery, long aIdProcesso)
    {
        int valorRetorno = -1;
        
   		Log.log (aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "executaUpdate", "Query: " + aQuery);
   		Statement statement = null;
        try
        {
            if(minhaConexao != null)
            {
                statement = minhaConexao.createStatement();
                valorRetorno = statement.executeUpdate(aQuery);
                statement.close();
            }
		}
        catch(SQLException sqlException)
        {
        	codigoErro = sqlException.getErrorCode();
   	    	Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaUpdate", "Erro(SQL) na conexao JDBC." + sqlException);
        }
        finally
        {
            try
            {
                statement.close();
            }
            catch (SQLException e)
            {
       	    	Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaUpdate", "Erro(SQL) na conexao JDBC." + e);
            }
        }
        return valorRetorno;
    }
    
	/** 
  	 * Metodo...: executaPreparedUpdate
	 * Descricao: Executa um comando de UPDATE no banco de dados utilizando um PREPAREDSTATEMENT
	 * @param 	aQuery	- Comando SQL de UPDATE a ser executado no bando de dados
	 * @param 	aParams - Array de parametros do comando
	 * @return 	int		- Codigo do erro retornado do banco
	 * @throws  GPPInternalErrorException
	 */
	public int executaPreparedUpdate(String aQuery, Object[] aParams, long aIdProcesso) throws GPPInternalErrorException
	{
		int valorRetorno = -1;
        
		Log.log (aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedUpdate", "Query: " + aQuery);
		PreparedStatement pStatement = null;
		try
		{
			if(minhaConexao != null)
			{
				pStatement = prepareQuery(aQuery, aParams, aIdProcesso);
				valorRetorno = pStatement.executeUpdate();				
			}
		}
		catch(SQLException sqlException)
		{
			codigoErro = sqlException.getErrorCode();
			Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedUpdate", "Erro(SQL) na conexao JDBC." + sqlException);
			throw new GPPInternalErrorException("Erro(SQL) JDBC:" + sqlException.getMessage());
		}
		finally
		{
		    try
            {
                pStatement.close();
            }
		    catch (SQLException e)
            {
		        Log.log(aIdProcesso, Definicoes.ERRO, Definicoes.CO_BANCO_DADOS_PREP, "executaPreparedUpdate", "Erro(SQL) na conexao JDBC." + e);
            }
		}
		return valorRetorno;
	}
	
	/**
  	 *	Retorna um objeto contendo informacoes sobre a conexao com o banco de dados.
  	 *
	 *	@return		Informacoes sobre a conexao com o banco de dados.
	 */
	public DadosConexao getDadosConexao()
	{
		return new DadosConexaoBD(this.idProcesso, this.dataInicialUso, this.listStatement.size());
	}

	/**
  	 * Metodo...: estouVivo
	 * Descricao: Este metodo verifica se o objeto de conexao com a base de dados ainda
	 * 			  esta valido executando um SQL simples para esta verificacao
 	 * @param   aIdProcesso 	- Handler do processo 
	 * @return 	boolean - indicando se a conexao esta ok ou nao
	 * @throws  
	 */
	public boolean estouVivo(long aIdProcesso)
	{
		Log.log (aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "estouVivo", "Validando conexao banco de dados");
		String sql = "SELECT 1 FROM DUAL";
		try
		{
			ResultSet resultado = executaQuery(sql,aIdProcesso);
			if (resultado.next())
				setEValido(true);
			resultado.close();
			Log.log (aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "estouVivo", "Conexao de banco de dados continua ATIVA.");
		}
		catch(GPPInternalErrorException e)
		{
			setEValido(false);
			Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "estouVivo", "Conexao de banco de dados NAO ativa.");
		}
		catch(SQLException e)
		{
			setEValido(false);
			Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "estouVivo", "Conexao de banco de dados NAO ativa.");
		}
		finally
		{
			Log.log(aIdProcesso, Definicoes.DEBUG, Definicoes.CO_BANCO_DADOS_PREP, "estouVivo", "Fim validacao conexao banco de dados");
		}
		return eValido();
	}
	
	/**
	 * @return the minhaConexao
	 */
	public Connection getMinhaConexao()
	{
		return minhaConexao;
	}
}