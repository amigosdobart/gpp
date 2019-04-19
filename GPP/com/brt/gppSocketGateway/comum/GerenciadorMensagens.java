package com.brt.gppSocketGateway.comum;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gppSocketGateway.servidores.ServindoAprovisionamento;
import com.brt.gppSocketGateway.servidores.ServindoGPP;
import com.brt.gppSocketGateway.logServer.LogGPPServer;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
  * Este arquivo refere-se a classe GerenciadorMensagens, responsavel por tratar
  * as mensagens que chegam ao servidor, encaminhando-as para os sistemas preparados
  * para recebê-las
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
public class GerenciadorMensagens extends Thread
{
	// Atirbutos da classe
	private LogGPPServer log;
	private GSocket gSocket;
	private long idProcesso;
	private int tipoServidor;
	private ArqConfigGPPServer arqConfig;
	private ServindoAprovisionamento aprSrv = null;
	
	/**
	 * Metodo...: GerenciadorMensagens
	 * Descricao: Construtor
	 * @param aMensagem
	 */
	public GerenciadorMensagens(GSocket gSocket, int aTipoServidor, long aIdProcesso) throws IOException
	{
		arqConfig 		= ArqConfigGPPServer.getInstance();
		this.log 		= LogGPPServer.getInstancia();
		this.gSocket 	= gSocket; 
		this.idProcesso = aIdProcesso;
		this.tipoServidor = aTipoServidor;
	}
	
	/**
	 * Metodo...: encaminhaMensagem
	 * Descricao: Verifica qual o header da mensagem recebida para saber o que fazer com ela,
	 * 				isto é, determina o que fazer com essa mensagem (para quem mandar)
	 * @param 	String		aMensagem	Mensagem a ser tratada
	 * @throws 	IOException	
	 */
	private void encaminhaMensagem(String aMensagem) throws GPPInternalErrorException,IOException
	{
		String mensagem = aMensagem;
		log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "encaminhaMensagem"," Inicio do encaminhador de mensagens");
		
		// Verifica se a mensagem e proveniente do ASAP e deve ser encaminhado ao GPP
		if(mensagem.startsWith(Definicoes.INICIO_XML_ASAP))
		{
			log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "encaminhaMensagem", "Mensagem comeca com root - mensagem para o GPP");
			
			if (this.aprSrv == null)
			{
				log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "encaminhaMensagem", "Nao existe referencia ao servidor GPP... Criando referencia");
				this.aprSrv = new ServindoAprovisionamento(idProcesso);
			}

			// Recebe mensagem (XML) do Aprovisionamento e a encaminha para o GPP
			this.aprSrv.servirAprovisionamento(aMensagem);
		}
		
		// Verifica se a mensagem e proveniente do GPP e deve ser encaminhada ao ASAP
		if(mensagem.startsWith(Definicoes.INICIO_XML_GPP))
		{		
			log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "encaminhaMensagem", "Mensagem comeca com {GPP-APROVISIONAMENTO} - mensagem para o ASAP");
			// Recebe mensagem do GPP e a encaminha para o ASAP
			ServindoGPP gppSrv = new ServindoGPP(idProcesso);
			gppSrv.servirGPP(mensagem);
		}
		
		log.log(idProcesso,Definicoes.DEBUG,"GerenciadorMensagens", "encaminhaMensagem", "Fim do encaminhador de mensagens");
	}
	
	public void run()
	{
		log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Inicio do metodo");

		try
		{
			String msg;
			String mensagemRecebida = "";

			arqConfig.getTimeOut();
			
			// Inicia o Hand-Shake
			log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Fazendo o Hand-Shake");
			String handShake = gSocket.getMsg(0);

			if (handShake.equals(Definicoes.STR_INICIO_HANDSHAKE_ASAP))
			{
				gSocket.sendMsg(Definicoes.STR_CONFIRMACAO_HANDSHAKE_ASAP);
				log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Envio de Hand-Shake OK");
			}

			// Busca o TAG que finaliza o envio de dados
			String finalMensagem = this.retornaFinalMensagem();

			// Fica escutando o socket criado ate que uma excecao seja disparada
			// indicando fechamento do socket por parte do cliente - Timeout 0 significa sempre...
			while(!((msg = gSocket.getMsg(0)) == null))
			{
				// Pega o primeiro valor lido (do getMsg(0)) e contatena com o valor lido
				mensagemRecebida += msg;

				try
				{
					// Enquanto nao achar o final do envio, continua lendo do socket
					while ( msg != null && (msg.indexOf(finalMensagem)) < 0 )
					{
						// Le o restante
						msg = gSocket.getMsg(0);
						
						mensagemRecebida += msg;
					}

					log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Mensagem recebida:" + mensagemRecebida);

					if (mensagemRecebida != null)
					{
						log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Encaminhando mensagem recebida");
						this.encaminhaMensagem(mensagemRecebida);

						// Envia o acknowledge para o sistema de origem (requisitante) 
						gSocket.sendMsg(Definicoes.STR_CONFIRMACAO_ENVIO_XML_ASAP);
						log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "ACK de mensagem enviado");
						
						// Limpa o conteudo da mensagem
						mensagemRecebida = new String();
						msg = new String();
					}
				}
				catch(InterruptedIOException ie)
				{
					// Caso a excecao seja encontrada entao o socket e fechado
					log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Terminando a thread de comunicacao por: InterruptedIOException");
					gSocket.close();
					break;
				}
				catch(IOException ioe)
				{
					// Caso a excecao seja encontrada entao o socket e fechado
					log.log(idProcesso, Definicoes.ERRO, "GerenciadorMensagens", "run", "Terminando a thread de comunicacao por: IOException");
					gSocket.close();
					break;			
				}
				catch(Exception e)
				{
					// Caso alguma excecao diferente do fechamento do socket pelo cliente
					// seja encontrada entao simplesmente faz o log desse erro voltando a
					// esperar para a proxima mensagem enviada pelo cliente
					log.log(idProcesso, Definicoes.ERRO, "GerenciadorMensagens", "run", "Erro processando a mensagem. Erro:" + e.getMessage());
					gSocket.close();
					break;			
				}
			}
		}
		catch(IOException ioE)
		{
			log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Terminando a thread de comunicacao por: IOException");
		}
	}
	
	/**
	 * Metodo...: retornaFinalMensagem
	 * Descricao: Verifica o tipo do servidor e retorna o tag que identifica o final da mensagem
	 * @return 	String		Tag final do XML
	 */
	private String retornaFinalMensagem ()
	{
		String retorno = null;
		
		switch (this.tipoServidor)
		{
			// Verifica o tipo de mensagem a esperada - ASAP
			case Definicoes.TIPO_ASAP:
			{
				retorno = Definicoes.FIM_XML_ASAP;
				log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Mensagem vinda do ASAP. Procurando final de mensagem = " + retorno);
				break;
			}
			
			// Verifica o tipo de mensagem a esperada - GPP
			case Definicoes.TIPO_GPP:
			{
				retorno = Definicoes.FIM_XML_ASAP;
				log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Mensagem vinda do GPP. Procurando final de mensagem = " + retorno);
				break;
			}
			default:
			{
				log.log(idProcesso, Definicoes.DEBUG, "GerenciadorMensagens", "run", "Nenhum tipo de mensagem.... Erro.");
				break;
			}
		}
		
		return retorno;
	}
}
