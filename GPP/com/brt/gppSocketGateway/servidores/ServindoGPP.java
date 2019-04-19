package com.brt.gppSocketGateway.servidores;

import java.io.IOException;

import com.brt.gppSocketGateway.comum.GSocket;
import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;
import com.brt.gppSocketGateway.logServer.LogGPPServer;
import com.brt.gppSocketGateway.comum.Definicoes;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
  * Este arquivo refere-se a classe AtivaConexoes, responsavel por abrir um
  * socket para o Aprovisionamento e enviar a mensagem (XML de bloqueio/desbloqueio)
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
public class ServindoGPP
{
	// Parâmetros do objeto
//	private GSocket sock;
	private String mensagem;
	private ArqConfigGPPServer arqConfig;
	private long idProcesso;
	private LogGPPServer log;
	
	/***
	 * Metodo...: ServindoGPP
	 * Descricao: Construtor
	 * @param aInicioMsg
	 * @param aIdProcesso
	 */
	public ServindoGPP(long aIdProcesso)
	{
		this.arqConfig = ArqConfigGPPServer.getInstance();
		this.idProcesso = aIdProcesso;

		// Pegando uma instancia do LogServer
		log = LogGPPServer.getInstancia();
	}
	
	/**
	 * Metodo...: servirGPP
	 * Descricao: Abre conexão com ASAP via socket e envia mensagem para ele
	 * @return
	 * @throws IOException
	 */
	public short servirGPP(String aMensagem) throws IOException,GPPInternalErrorException
	{
//		String msgAux = null;
		short retorno = 0;
		int timeOut = arqConfig.getTimeOut();
		
		// Remove o "{GPP-APROVISIONAMENTO}" da mensagem
		this.mensagem = aMensagem.substring(22);
		
		// Armazena o numero da OS enviada ao SAP
		String idOS = aMensagem.substring(35, 51);

		log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Inicio do ServirGPP para a OS: " + idOS);
		// Cria um socket cliente para o ASAP
		GSocket clienteDoApr = new GSocket(arqConfig.getHostAprovisionamento(), arqConfig.getPortaAprovisionamento(),idProcesso);

		log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Realizando HandShake com o sistema de aprovisionamento para a OS: " + idOS);

		// Realiza o hand-shake com o sistema de aprovisionamento
		clienteDoApr.sendMsg(Definicoes.STR_INICIO_HANDSHAKE_ASAP);
		
		// Recebe a confirmacao do hand-shake e somente se estiver realizado
		// este hand-shake é que a mensagem eh enviada
		String msgHandShake = clienteDoApr.getMsg(timeOut);
		if (msgHandShake != null && msgHandShake.equals(Definicoes.STR_CONFIRMACAO_HANDSHAKE_ASAP))
		{
			log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Hand-Shake com ASAP executado com sucesso" + msgHandShake + "para a OS: " + idOS);

			// Realiza o envio da mensagem para o ASAP e busca o retorno
			// para saber se a mensagem foi corretamente recebida
			log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Enviando mensagem para o ASAP: " + mensagem);
			clienteDoApr.sendMsg(mensagem);
			
			log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Mensagem enviada para o ASAP da OS: "  + idOS);

			// O retorno (Acknowledge) do sistema de aprovisionamento,indica o correto recebimento
			// da mensagem, portanto o sistema tenta receber este "recibo" n vezes. A cada tentativa
			// o sistema espera x segundos ate a proxima tentativa, caso nenhuma dessas funcione
			// entao o sistema dispara uma excecao indicando que o sistema de aprovisionamento nao
			// recebeu o XML de aprovisionamento
			for (int numTentativas=1; numTentativas <= arqConfig.getNumTentativasEnvioXML(); numTentativas++)
			{
				log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Iniciando tentativa: " + numTentativas + ", aguardando retorno ack do ASAP da OS: " + idOS);

				// Entao caso o acknowledge nao tenha sido recebido entao o sistema aguarda ate
				// a proxima tentativa (Tempo em minutos)
				String aguardaAck = clienteDoApr.getMsg(2);
				if ( (aguardaAck == null) || (aguardaAck.indexOf(Definicoes.STR_CONFIRMACAO_ENVIO_XML_ASAP)<0) )
				{
					log.log(idProcesso, Definicoes.WARN, "ServindoGPP", "servirGPP", "Nao recebeu a mensagem (time out) ou mensagem nao eh um ack:" + aguardaAck + " da OS: " + idOS);

					if ( numTentativas != arqConfig.getNumTentativasEnvioXML() )
					{
						log.log(idProcesso, Definicoes.WARN, "ServindoGPP", "servirGPP", "Tentativa: " + numTentativas+" de: "+arqConfig.getNumTentativasEnvioXML()+" para novo envio do XML ao sistema de aprovisionamento da OS: " + idOS);

						try
						{
							Thread.sleep( arqConfig.getTempoAcknowledge()*1000 );
						}
						catch(Exception e)
						{
							throw new IOException("Nao foi possivel receber o Acknowledge do sistema de aprovisionamento para a OS: " + idOS); 
						}
					}
					else 
					{
						throw new IOException("Nao foi possivel receber o Acknowledge do sistema de aprovisionamento para a OS: " + idOS);
					}
				}
				else
				{
					log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Ack recebido do ASAP, referente a OS: " + idOS);
					break;
				}
			}
		}
		else
		{
			throw new IOException("Erro ao realizar o hand-shake com o sistema de aprovisionamento");
		}
		// Envia HandShake de finanizacao
		clienteDoApr.sendMsg(Definicoes.STR_FINAL_HANDSHAKE_ASAP);
		
		// Recebe a confirmacao do hand-shake e somente se estiver realizado
		// este hand-shake é que a mensagem é enviada
		String msgHandShakeFinal = clienteDoApr.getMsg(2);
		if (msgHandShakeFinal != null && msgHandShakeFinal.equals(Definicoes.STR_CONFIRMACAO_HANDSHAKE_FINAL_ASAP))
		{
			log.log(idProcesso, Definicoes.DEBUG, "ServindoGPP", "servirGPP", "Hand-shake de finalizacao com ASAP efetuado com sucessso");
		}
		else
		{
			log.log(idProcesso, Definicoes.ERRO, "ServindoGPP", "servirGPP", "Erro no Hand-shake de finalizacao com ASAP");
		}

		// Fecha conexao do socket
		clienteDoApr.close();
		
		return retorno;
	}
}
