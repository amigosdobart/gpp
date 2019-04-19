package com.brt.gpp.gerentesPool;

import java.util.Properties;

import org.omg.CORBA.ORB;

import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;

/**
 *	Gerente detentor do Object Request Broker (ORB) da aplicacao, responsavel pela comunicacao CORBA entre o GPP 
 *	e sistemas servidores e sistemas clientes com o GPP.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		22/02/2007
 *	@modify		Primeira versao.
 */
public class GerenteORB 
{

	/**
	 *	Instancia do singleton. 
	 */
	private static GerenteORB instance;

	/**
	 *	Referencia ao Object Request Broker (ORB) da aplicacao.
	 */
	private ORB orb;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		args					Lista de argumentos passados a aplicacao por parametro.
	 */
	private GerenteORB(String[] args)
	{
		ArquivoConfiguracaoGPP	conf	= ArquivoConfiguracaoGPP.getInstance();
		Properties				props	= System.getProperties();
		
        props.put("vbroker.agent.port", conf.getPortaOrbGPP());
        props.put("vbroker.agent.addr", conf.getEnderecoOrbGPP());
        props.put("ORBInitRef", "NameService=corbaloc:iiop:" + 
        						conf.getEnderecoNameServiceGPP() + 
        						":" + 
        						conf.getPortaNameServiceGPP() + 
        						"/NameService");
        System.setProperties(props);
        
        this.orb = ORB.init(args, props);
	}

	/**
	 *	Inicializa e retorna a instancia do singleton.
	 *
	 *	@param		args					Lista de argumentos passados a aplicacao por parametro.
	 * 	@return		Instancia do singleton. 
	 */
	public static GerenteORB newInstance(String[] args)
	{
		GerenteORB.instance = new GerenteORB(args);
		return GerenteORB.getInstance();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 * 	@return		Instancia do singleton. 
	 */
	public static GerenteORB getInstance()
	{
		return GerenteORB.instance;
	}
	
	/**
	 *	Retorna a referencia ao Object Request Broker (ORB) da aplicacao.
	 *
	 *	@return		Referencia ao Object Request Broker (ORB) da aplicacao.
	 */
	public ORB getOrb()
	{
		return this.orb;
	}
	
}
