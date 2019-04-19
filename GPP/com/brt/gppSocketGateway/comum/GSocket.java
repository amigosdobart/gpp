package com.brt.gppSocketGateway.comum;

// Imports de Java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.*;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gppSocketGateway.logServer.LogGPPServer;

/**
  * Este arquivo refere-se a classe GSocket, responsável por implementar uma conexão
  * socket genérica, tanto de cliente quanto de servidor, independente de protocolo de comunicação.
  * Para se estabelecer um protocolo, deve-se derivar essa classe.
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				20/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class GSocket
{
	protected	Socket			sock;
	protected 	BufferedReader 	entradaDados;
	private 	LogGPPServer 	log;
	
	/**
	 * Metodo...: GSocket
	 * Descricao: Construtor
	 */
	public GSocket(String aHost, String aPorta, long idProcesso) throws GPPInternalErrorException
	{
		log = LogGPPServer.getInstancia();
		try
		{
			this.sock = new Socket(aHost, new Integer(aPorta).intValue());

			// Cria um Buffer para ler os dados
			this.entradaDados = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		}
		catch (IOException ex)
		{
			log.log(idProcesso, Definicoes.ERRO, "GSocket", "GSocket", "Erro ao conectar com servidor socket. Host:"+aHost+" Porta:"+aPorta+" Erro:"+ex);
			throw new GPPInternalErrorException(ex.getMessage());
		}
	}
	
	/**
	 * Metodo...: GSocket
	 * Descricao: Construtor que se utiliza de uma conexão já feita para adaptá-la aos 
	 * 			métodos do GSocket
	 * @param 	Socket	aSock	Socket pré-aberto
	 */
	public GSocket(Socket aSock)
	{
		this.sock = aSock;
		try
		{
			// Cria um Buffer para ler os dados
			this.entradaDados = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Método...: sendMsg
	 * Descricao: Envia mensagem via socket
	 * @param 	Socket	sock	Socket para onde mandar a mensagem
	 * @param 	String	msg		Mensagem
	 * @throws IOException
	 */
	public void sendMsg(String msg) throws IOException
	{
		try
		{
			BufferedWriter vOutput = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
			vOutput.write(msg + '\n');
			vOutput.flush();
		}
		catch (Exception ex)
		{
			throw(new IOException("Communication Failure"));
		}
	}

	/**
	 * Metodo...: getMsg
	 * Descricao: Le uma mensagem de um socket
	 * @param 	 int			newTime	Timeout de leitura
	 * @return	String			Mensagem lida; null se der timeout
	 * @throws IOException
	 */
	public String getMsg(int newTime) throws IOException,InterruptedIOException
	{
		String valorLido = null;

		// Armazena o valor do timeout anterior
		int oldTime = this.sock.getSoTimeout();
		
		// Coloca o novo valor de timeout
		this.sock.setSoTimeout(newTime*60*1000);
		
		// Le os dados do socket
		valorLido = entradaDados.readLine();
		
		//System.out.println("Mensagem lida:" + valorLido);
		
		// Volta o timeout anterior
		this.sock.setSoTimeout(oldTime);
		
		return valorLido;
	}
	
	/**
	 * Metodo....:close
	 * Descricao.:Este metodo realiza o fechamento do socket
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		sock.close();
	}
}
