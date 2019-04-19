package com.brt.gppSocketGateway.servidores;

import java.util.Properties;
import java.util.Map;

//import com.brt.gppSocketGateway.comum.GSocket;
import com.brt.gpp.componentes.aprovisionamento.orb.*;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;
import com.brt.gppSocketGateway.logServer.LogGPPServer;
import com.brt.gppSocketGateway.comum.Definicoes;

/**
  * Este arquivo refere-se a classe ServindoAprovisionamento, responsavel por tratar
  * as mensagens que chegam do Aprovisionamento
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
public class ServindoAprovisionamento 
{
	// Parâmetros do objeto
//	private GSocket sock;
	private ArqConfigGPPServer arqConfig;
	private LogGPPServer log;
	private long idProcesso;
	private aprovisionamento pPOA;
	
	/***
	 * Metodo...: ServindoAprovisionamento
	 * Descricao: Construtor
	 * @param 	String		aMsg		Mensagem que vem do ASAP
	 * @param 	int			aIdProcesso	Identificador do Processo para efeitos de log
	 */
	public ServindoAprovisionamento(long aIdProcesso)
	{
		this.idProcesso = aIdProcesso;
		this.arqConfig = ArqConfigGPPServer.getInstance();

		// pega uma instancia do log server
		log = LogGPPServer.getInstancia();
		
		try 
		{
			this.pPOA = this.criaConexaoAprovisionamentoGPP();
		} 
		catch (GPPInternalErrorException e) 
		{
			log.log(this.idProcesso, Definicoes.ERRO, "ServindoAprovisonamento", "servirAprovisionamento", "Erro GPP: " + e);
		}
	}
	
	/**
	 * Metodo...: servirAprovisionamento
	 * Descricao: conectar-se ao GPP e evocar o método responsável por receber o XML mandado pelo ASAP
	 * @throws IOException
	 */
	public void servirAprovisionamento(String aMensagem) throws GPPInternalErrorException
	{
		// Cria conexão CORBA de aprovisionamento com GPP
		log.log(this.idProcesso, Definicoes.DEBUG, "ServindoAprovisonamento", "servirAprovisionamento", "Criando conexao com GPP aprovisionamento");

		try
		{
			// Evoca método do componente de aprovisionamento do GPP para mandar 
			// retorno da solicitação de bloqueio/desbloqueio feita ao ASAP
			this.pPOA.confirmaBloqueioDesbloqueioServicos(aMensagem);
			log.log(idProcesso, Definicoes.DEBUG, "ServindoAprovisonamento", "servirAprovisionamento", "Chamada ao metodo remoto confirmaBloqueioDesbloqueioServicos realizada com sucesso");
		}
		catch(GPPInternalErrorException gppE)
		{
			log.log(idProcesso, Definicoes.DEBUG, "ServindoAprovisonamento", "servirAprovisionamento", "Chamada ao metodo remoto confirmaBloqueioDesbloqueioServicos realizada com erro: " + gppE);
			throw new GPPInternalErrorException(gppE.codigoErro);
		}
	}
	
	/**
	 * Metodo...: criaConexaoAprovisionamentoGPP
	 * Descricao: Cria uma conexão com o componente de negócio de aprovisionamento do GPP
	 * @return	aprovisionamento	POA do CN Aprovisionamento do GPP
	 */
	private aprovisionamento criaConexaoAprovisionamentoGPP() throws GPPInternalErrorException
	{
		ArqConfigGPPServer config = ArqConfigGPPServer.getInstance();

		Properties props = System.getProperties();
		props.putAll((Map)config.getConfiguracaoesCorba());
		
		String[] args = {arqConfig.getPortaGPP(), arqConfig.getHostGPP()};
		props.put("vbroker.agent.port", arqConfig.getPortaGPP());
		props.put("vbroker.agent.addr", arqConfig.getHostGPP());

		System.setProperties ( props );
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		return pPOA;
	}	
}
