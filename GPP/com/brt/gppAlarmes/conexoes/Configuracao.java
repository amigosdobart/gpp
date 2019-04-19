package com.brt.gppAlarmes.conexoes;

import java.net.URL;
import java.util.*;

public class Configuracao
{
	private Properties propriedades;
	private static Configuracao instance;
	
	public Configuracao()
	{
		try
		{
			propriedades = new Properties();
			URL recurso = ClassLoader.getSystemResource("alarmes.properties");
			propriedades.load(recurso.openStream());
		}
		catch(Exception e)
		{
			System.out.println("Nao foi possivel ler o arquivo de configuracoes. Erro+"+e);
		}
	}

	public static Configuracao getInstance()
	{
		if (instance == null)
			instance = new Configuracao();
		
		return instance;
	}

	public String getPropriedade(String chave)
	{
		return propriedades.getProperty(chave);
	}
	
	public Properties getPropriedadesLog()
	{
		Properties prop = new Properties();
		prop.putAll(propriedades);
		return prop;
	}
}
