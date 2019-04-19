package com.brt.gpp.comum.conexoes.socket;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GPPSocketCliente
{
//	private String 				servidor;
//	private int					porta;
	private Socket				socket;
	private BufferedWriter		output;
	private BufferedReader		input;

	/**
	 * Metodo....:GPPSocketCliente
	 * Descricao.:Construtor da classe. Nao inicializa nada
	 *
	 */
	public GPPSocketCliente()
	{
	}

	/**
	 * Metodo....:GPPSocketCliente
	 * Descricao.:Construtor da classe. Inicializa variaveis e 
	 * @param host	- Servidor a ser conectado
	 * @param port	- Porta desse servidor para conexao
	 * @throws GPPInternalErrorException
	 */
	public GPPSocketCliente(String host, int port) throws GPPInternalErrorException
	{
		// Configura as informacoes sobre a conexao
//		this.servidor 	= host;
//		this.porta 		= port;
		// Chama o metodo para iniciar a conexao
		conecta(host,port);
	}
	
	/**
	 * Metodo....:conecta
	 * Descricao.:Realiza a conexao com o servidor desejado
	 * @param host	- Host a ser conectado
	 * @param port	- Porta desse servidor para conexao
	 * @throws GPPInternalErrorException
	 */
	public void conecta(String host, int port) throws GPPInternalErrorException
	{
//		this.servidor = host;
//		this.porta	  = port;
		try
		{
			// Inicia a conexao com o servidor na porta definida
			// inicializando as propriedades para armazenar
			// o output a ser enviado e o input a ser lido do socket
			socket = new Socket(host, port);
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(UnknownHostException e)
		{
			throw new GPPInternalErrorException("Servidor desconhecido:"+host+":"+port);
		}
		catch(IOException e)
		{
			throw new GPPInternalErrorException("Erro ao ler ou gravar no socket.. Erro:"+e);
		}
	}
	
	/**
	 * Metodo....:writeString
	 * Descricao.:Escreve uma string no socket
	 * @param dados	- String a ser enviada pelo socket
	 * @throws GPPInternalErrorException
	 */
	public void writeString(String dados) throws GPPInternalErrorException
	{
		try
		{
			if (socket != null && output != null)
			{
				output.write(dados);
				output.newLine();
				output.flush();
			}
				
		}
		catch(IOException e)
		{
			throw new GPPInternalErrorException("Erro ao escrever no socket. Erro:"+e.getMessage());
		}
	}
	
	/**
	 * Metodo....:readString
	 * Descricao.:Faz a leitura de uma string no socket
	 * @return	- String lida no socket
	 * @throws GPPInternalErrorException
	 */
	public String readString() throws GPPInternalErrorException
	{
		try
		{
			if (socket != null && input != null)
				return input.readLine();
			return null;
		}
		catch(IOException e)
		{
			throw new GPPInternalErrorException("Erro ao ler o socket. Erro:"+e.getMessage());
		}
	}

	/**
	 * Metodo....:readChar
	 * Descricao.:Realiza a leitura de um conjunto de caracteres
	 * @param buffer	- Array a ser armazenado a leitura
	 * @param size		- Numero de bytes a serem lidos
	 * @return int		- Numero de bytes efetivamente lidos
	 * @throws GPPInternalErrorException
	 */
	public int readChar(char[] buffer, int size) throws GPPInternalErrorException
	{
		try
		{
			if (socket != null && input != null)
				return input.read(buffer,0,size);
			return 0;
		}
		catch(IOException e)
		{
			throw new GPPInternalErrorException("Erro ao ler o socket. Erro:"+e.getMessage());
		}
	}
	
	/**
	 * Metodo....:close
	 * Descricao.:Fecha toda a conexao do socket
	 * @throws GPPInternalErrorException
	 */
	public void close() throws GPPInternalErrorException
	{
		try
		{
			input.close();
			output.close();
			socket.close();
		}
		catch(IOException e)
		{
			throw new GPPInternalErrorException("Erro ao fechar o socket. Erro:"+e.getMessage());
		}
	}
}
