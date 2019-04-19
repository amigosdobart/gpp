package br.com.brasiltelecom.wig.util;

import java.util.HashMap;
import java.util.Map;

public class ResolvedorNomeServidor
{
	private static ResolvedorNomeServidor instance;
	private Map servidores;
	
	private ResolvedorNomeServidor()
	{
		servidores = new HashMap();
		servidores.put("btdf0180" 	 ,"10.61.176.109");
		servidores.put("btdf0182" 	 ,"10.61.176.111");
		servidores.put("btdf0171" 	 ,"10.61.176.115");
		servidores.put("btdf0172" 	 ,"10.61.176.116");
		servidores.put("10.44.250.55","10.44.250.55");
		servidores.put("localhost"	 ,"localhost");
	}
	
	public static ResolvedorNomeServidor getInstance()
	{
		if (instance == null)
			instance = new ResolvedorNomeServidor();
		
		return instance;
	}
	
	public String resolveNome(String nome)
	{
		return (String)servidores.get(nome);
	}
}
