package com.brt.gppSocketGateway.gerenteConexoes;

import java.util.Hashtable; 

import com.brt.gppSocketGateway.servidores.ServidorUniversal; 
import com.brt.gppSocketGateway.comum.Definicoes;
import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;
import com.brt.gppSocketGateway.logServer.LogGPPServer;

/**
  * Este arquivo refere-se a classe GerenteServidores, responsavel por ativar
  * todos os servidores periféricos ao GPP
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				25/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class GerenteServidores 
{
	private static 		GerenteServidores	instancia;
	private static		LogGPPServer	log ;
	private Hashtable	poolServidores;			// armazenará todos os servidores periféricos ao GPP
	
	/**
	 * Metodo...: GerenteServidores
	 * Descricao: Construtor
	 */
	public GerenteServidores()
	{
		poolServidores = new Hashtable();		// Hashtabel responsável por armazenas os sockets dos servidores
		log = LogGPPServer.getInstancia();		// Instancia servidor de log
	}

	/**
	 * Metodo...: getInstancia
	 * Descricao: Disponibiliza/reutiliza uma instância do singleton de Gerente de Servidores 
	 * @return	GerenteServidores	Retorna um novo GerenteServidores ou um que já esteja instanciado
	 */
	public static GerenteServidores getInstancia ()
	{
		if (instancia == null)
		{
			instancia = new GerenteServidores();
			
			// Inicializa servidores
			instancia.inicializaServidores();
		}
		return instancia;    	
	}
	
	/***
	 * Metodo...: inicializaServidores
	 * Descricao: Inicializa cada um dos servidores periféricos ao GPP
	 */
	private void inicializaServidores()
	{
		// Inicializa Singleton do arquivo de configurações
		ArqConfigGPPServer arqConfig = ArqConfigGPPServer.getInstance();

		// Cria um servidor socket para o GPP
		poolServidores.put(Definicoes.REF_SERVIDOR_GPP, new ServidorUniversal(arqConfig.getPortaServidorGPP(), Definicoes.TIPO_GPP));
		log.log(0, Definicoes.INFO, "GerenteServidores", "inicializaServidores", "Servidor para o GPP inicializado");
		
		// Cria um servidor socket para o ASAP
		poolServidores.put(Definicoes.REF_SERVIDOR_APROVISIONAMENTO, new ServidorUniversal(arqConfig.getPortaServidorAprovisionamento(), Definicoes.TIPO_ASAP));
		log.log(0, Definicoes.INFO, "GerenteServidores", "inicializaServidores", "Servidor para o Aprovisionamento inicializado");
	}
}
