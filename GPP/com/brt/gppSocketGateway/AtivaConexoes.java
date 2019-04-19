package com.brt.gppSocketGateway;

import com.brt.gppSocketGateway.gerenteConexoes.GerenteServidores;
import com.brt.gppSocketGateway.logServer.LogGPPServer;
import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;
import com.brt.gppSocketGateway.comum.Definicoes;

import java.io.File;

/**
  * Este arquivo refere-se a classe AtivaConexoes, responsavel por estabelecer
  * todas as conexões necessárias para o GPP com outros sistemas via Socket
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
public class AtivaConexoes 
{
	// Instancia um objeto para gerenciar as conexões
	// Passando o nome do arquivo que contem as configurações de conexões
	static GerenteServidores orquestraConexoes;
	private static LogGPPServer log;

	/**
	 * Metodo...: main
	 * Descrição: Método principal dos Servidores Socket
	 * @param 	String	args	Não recebe nenhum parâmetro inicial
	 */
	public static void main(String args[])
	{
		try
		{
			// Inicializa a leitura das configuracoes do sistema
			// atraves do arquivo de propriedades informado. Caso
			// nao seja informado entao utiliza-se o nome padrao
			// sendo que este arquivo deve estar no classpath da JVM
			// executada
			if ( args[0] != null && new File(args[0]).isFile() )
				ArqConfigGPPServer.getInstance(args[0]);
			else ArqConfigGPPServer.getInstance();

			// Inicializa produtor de log
			log = LogGPPServer.getInstancia(); 
			
			// Inicializa todos os servidores socket
			orquestraConexoes = GerenteServidores.getInstancia();			
		}
		catch (Exception e)
		{
			log.log(0,Definicoes.ERRO,"AtivaConexoes","main","Erro Fatal ao Ativar Servidores de Socket");
			System.exit(1);
		}
	}
	

}
