package com.brt.gppEnviaMail.conexoes;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBancoDados
{
	private static Connection 			connection;
	private static ConexaoBancoDados 	instance;
	
	public ConexaoBancoDados(String tnsName, String userName, String passWord)
	{
		conecta(tnsName,userName,passWord);
	}

	public void conecta(String tnsName, String userName, String passWord)
	{
		try
		{
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:oci8:@"+tnsName, userName, passWord);
		}
		catch(Exception e)
		{
			System.out.println("Nao foi possivel realizar conexao com o banco de dados. Erro:"+e);
		}
	}

	public static ConexaoBancoDados getInstance()
	{
		Configuracao conf = Configuracao.getInstance();
		if (instance == null)
			instance = new ConexaoBancoDados(conf.getPropriedade("com.brt.gppEnviaMail.nomeServidorBancoDados"),
					                         conf.getPropriedade("com.brt.gppEnviaMail.nomeUsuarioBancoDados"),
											 conf.getPropriedade("com.brt.gppEnviaMail.senhaUsuarioBancoDados"));
		
		return instance;
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
				instance   = null;
			}
		}
		catch(Exception e)
		{
			System.out.println("Erro ao fechar a conexao com o banco de dados. Erro:"+e);
		}
	}
}
