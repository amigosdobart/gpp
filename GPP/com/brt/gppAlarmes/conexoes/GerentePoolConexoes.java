package com.brt.gppAlarmes.conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A classe GerentePoolConexoes eh responsavel por gerenciar os pedidos de conexoes de BD,
 * criando-as conforme a necessidade.
 * @author Gustavo Gusmao
 * Data..: 16-fev-2006
 */
public class GerentePoolConexoes
{
	private Logger logger;
	private static GerentePoolConexoes gerente;
	private List conexoesDisponiveis;
	
	private String tnsName;
	private String userName;
	private String passWord;
	
	/**
	 * Metodo....: GerentePoolConexoes
	 * Descricao.: Construtor padrao da classe
	 * @throws Exception Excecao lancada quando o driver JDBC nao pode ser carregado
	 */
	private GerentePoolConexoes() throws Exception
	{
		logger = Logger.getLogger("Alarmes");
		Configuracao config = Configuracao.getInstance();
		tnsName = config.getPropriedade("alarmes.db.tnsnames");
		userName = config.getPropriedade("alarmes.db.username");
		passWord = config.getPropriedade("alarmes.db.password");
		conexoesDisponiveis = new ArrayList();
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException e)
		{
			logger.error("Nao foi possivel carregar o driver JDBC. Erro:"+e);
			throw new Exception("Nao foi possivel carregar o driver JDBC. Erro:"+e);
		}
		logger.info("Pool de conexoes criado com sucesso");
	}
	
	/**
	 * Metodo....: criarConexao
	 * Descricao.: Metodo que cria uma conexao de BD
	 * @return Uma conexao de BD
	 */
	private Connection criarConexao()
	{
		logger.info("Criando nova conexao no pool");
		Connection conexao = null;
		try
		{
			conexao = DriverManager.getConnection("jdbc:oracle:oci8:@" + tnsName, userName, passWord);
		}
		catch(SQLException e)
		{
			logger.error("Nao foi possivel realizar conexao com o banco de dados "+tnsName+". Erro: "+e);
			conexao = null;
		}
		return conexao;
	}
	
	/**
	 * Metodo....: testarConexao
	 * Descricao.: Metodo que testa se uma conexao de BD esta OK
	 * @param conexao A conexao a ser testada
	 * @return Se a conexao esta funcionando corretamente
	 */
	private boolean testarConexao(Connection conexao)
	{
		String sql = "SELECT 1 FROM dual";
		boolean resultado = false;
		try
		{
			Statement statement = conexao.createStatement();
			ResultSet rSet = statement.executeQuery(sql);
			rSet.close();
			statement.close();
			resultado = true;
		}
		catch(SQLException e)
		{
			resultado = false;
		}
		return resultado;
	}
	
	/**
	 * Metodo....: getInstance
	 * Descricao.: Metodo estatico que retorna uma instancia do gerente de pool
	 * @return Uma instancia do gerente de pool
	 * @throws Exception A excecao lancada pelo contrutor da classe
	 */
	public static GerentePoolConexoes getInstance() throws Exception
	{
		if(gerente == null)
			gerente = new GerentePoolConexoes();
		return(gerente);
	}
	
	/**
	 * Metodo....: getConexao
	 * Descricao.: Metodo que retorna uma conexao de BD, criando-a se necessario
	 * @return Uma conexao de BD
	 */
	public Connection getConexao()
	{
		Connection conexao = null;
		if(!conexoesDisponiveis.isEmpty())
		{
			conexao = (Connection)conexoesDisponiveis.get(conexoesDisponiveis.size()-1);
			conexoesDisponiveis.remove(conexao);
			try
			{
				if(conexao.isClosed())
				{
					logger.debug("Conexao fechada removida do pool");
					conexao = getConexao();
				}
			}
			catch(SQLException e)
			{
				logger.error("Nao foi possivel realizar conexao com o banco de dados "+tnsName+". Erro: "+e);
				conexao = getConexao();
			}
			catch(Exception e)
			{
				logger.error("Nao foi possivel realizar conexao com o banco de dados "+tnsName+". Erro: "+e);
				conexao = getConexao();
			}
		}
		else
		{
			conexao = criarConexao();
		}
		return conexao;
	}
	
	/**
	 * Metodo....: releaseConexao
	 * Descricao.: Metodo que devolve uma conexao de BD para o pool
	 * @param conexao A conexao de BD a ser liberada
	 */
	public void releaseConexao(Connection conexao)
	{
		if(testarConexao(conexao))
			conexoesDisponiveis.add(conexao);
	}
	
	/**
	 * Metodo....: destroyPool
	 * Descricao.: Metodo que esvazia todo o pool
	 */
	public void destroyPool()
	{
		System.out.println("Adios compadre");
		Iterator iterator = conexoesDisponiveis.iterator();
		while(iterator.hasNext())
		{
			Connection conexao = (Connection)iterator.next();
			try
			{
				conexao.close();
			}
			catch(SQLException e)
			{
	 			logger.warn("Nao foi possivel fechar a conexao");
			}
		}
		conexoesDisponiveis.clear();
	}
}