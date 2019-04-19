package com.brt.gppAlarmes.conexoes;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

public class ConexaoBancoDados
{
	private Connection 	connection;
	private Logger 		logger;
	
	public ConexaoBancoDados(String tnsName, String userName, String passWord) throws Exception
	{
		logger = Logger.getLogger("Alarmes");
		conecta(tnsName,userName,passWord);
	}

	public void conecta(String tnsName, String userName, String passWord) throws Exception
	{
		try
		{
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:oci8:@"+tnsName, userName, passWord);
			logger.info("Usuario "+userName+" conectado a base de dados "+tnsName);
		}
		catch(Exception e)
		{
			logger.error("Nao foi possivel realizar conexao com o banco de dados "+tnsName+". Erro:"+e);
			throw new Exception("Nao foi possivel realizar conexao com o banco de dados "+tnsName+". Erro:"+e);
		}
	}

	public Connection getConnection()
	{
		return connection;
	}
	
	public void close()
	{
		try
		{
			if (connection != null)
			{
				connection.close();
				connection = null;
				logger.info("Conexao com a base de dados foi finalizada.");
			}
		}
		catch(Exception e)
		{
			logger.error("Erro ao fechar a conexao com o banco de dados. Erro:"+e);
		}
	}
}
