package com.brt.gpp.comum.conexoes.tecnomen;

import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;

import TINC.ePPInterface;
import TINC.ePPServer;

// TecnomenPPServerFactory
// Esta classe eh responsavel pela criacao da conexao com a PrepaidServer da Tecnomen
// O factory existe para distribuir as conexoes entre os dois engines existentes na plataforma
// o engine de numero 65 (PPInterface) e o engine numero 2 (PPServer). O engine numero 2
// soh estah disponivel para utilizacao, se o metodo realizado pelo autenticador for o "validate"
// para o metodo "login" esta opcao nao estah disponivel.
//
// Esta classe realiza a busca no arquivo de configuracao para identificar se deve ou nao
// realizar a distribuicao destas conexoes, porem eh necessario como premissa que se esteja utilizando
// o metodo "validate".
//
public class TecnomenPPServerFactory
{
	private static TecnomenPPServerFactory instance;
	
	// Define o array contendo os servidores disponiveis para serem utilizados
	// neste tipo de conexao. Por default somente o engine da PPInterface eh 
	// disponivel. Caso a configuracao do sistema seja TRUE indicando a possibilidade
	// desta distribuicao de conexoes, entao o segundo valor do array serah o engine
	// do PPServer
	private short ppServers[] = {ePPInterface.value, ePPInterface.value};
	private int count = 0;
	
	private TecnomenPPServerFactory()
	{
		// Define o numero de engines disponiveis configurados na Tecnomen
		ArquivoConfiguracaoGPP conf = ArquivoConfiguracaoGPP.getInstance();
		// Caso esteja definido para distribuir conexoes PPServer entao
		// o segundo elemento do array serah o engine da PPServer e nao
		// o default utilizado pela PPInterface
		if (conf.getBoolean("TECNOMEN_DISTRIB_PPSERVER"))
			ppServers[1] = ePPServer.value;
	}
	
	public static TecnomenPPServerFactory getInstance()
	{
		if (instance == null)
			instance = new TecnomenPPServerFactory();
		
		return instance;
	}
	
	public TecnomenPPServer newTecnomenPPServer()
	{
		// O sistema cria um array contendo dois valores, um para o engine PPServer
		// e outro para o engine PPInterface. Para distribuir as conexoes eh utilizado
		// um contador que hora retona 0 hora retorna 1 distribuindo igualmente entre
		// os dois vetores as conexoes que forem criadas. Caso o numero seja impar entao
		// um engine terah uma conexao a mais que a outra
		return new TecnomenPPServer(ppServers[++count % ppServers.length]);
	}
}
