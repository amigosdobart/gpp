package com.brt.gpp.comum.conexoes.toolbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

//import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.toolbar.ElementoProtocoloToolbar;
import com.brt.gpp.comum.conexoes.toolbar.ParserLinhaProtocoloToolbar;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe que representa a conexao com o sistema Toolbar e que permite operacoes com este sistema. As operacoes sao
 *	executadas atraves de um protocolo bem definido e cuja transmissao de dados e feita via socket.
 * 
 *	@author		Daniel Ferreira
 *	@since		21/12/2006
 */
public class ConexaoToolbar 
{
	private BufferedReader leitura;
	private BufferedWriter escrita;
	private String enderecoToolbar;
	private short numTentativas;
	private int portaToolbar;
	private int tempoEspera;
	private Socket socket;
	private GerentePoolLog gerLog = GerentePoolLog.getInstancia(ConexaoToolbar.class);
	
	/**
	 *	Construtor da classe.
	 */
	public ConexaoToolbar(String numeroIP) 
	{
		ArquivoConfiguracaoGPP configuracao = ArquivoConfiguracaoGPP.getInstance();
		
		this.enderecoToolbar	= numeroIP;
		this.portaToolbar		= configuracao.getToolbarPorta();
		this.numTentativas		= configuracao.getToolbarNumTentativas();
		this.tempoEspera		= configuracao.getToolbarTempoEspera();
	}
	
	/**
	 *	Estabelece a conexao socket com o Toolbar.
	 *
	 *	@throws		Exception
	 */
	public void conectar() throws Exception
	{
		this.socket		= new Socket(this.enderecoToolbar, this.portaToolbar);
		this.leitura	= new BufferedReader(new InputStreamReader (this.socket.getInputStream ()));
		this.escrita	= new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		gerLog.log(0, Definicoes.INFO, "ConexaoToolbar", "conectar", "Conectado ao toolbar Servidor:"+this.enderecoToolbar+":"+this.portaToolbar);
	}
	
	/**
	 *	Fechar a conexao socket com o Toolbar.
	 *
	 *	@throws		Exception
	 */
	public void fechar() throws Exception
	{
		if(this.escrita != null)
			this.escrita.close();
		if(this.leitura != null)
			this.leitura.close();
		if(this.socket != null)
			this.socket.close();
	}
	
	/**
	 *	Executa o processo de leitura no socket.
	 *
	 *	@throws		Exception
	 */
	private String ler() throws Exception
	{
		String	result			= null;
		short	numTentativas	= 0;
		
		while((result == null) && (numTentativas < this.numTentativas))
		{
			result = this.leitura.readLine();
			numTentativas++;
			if(result == null)
				Thread.sleep(this.tempoEspera);
		}
		
		if(result == null)
			throw new SocketTimeoutException("Tempo esgotado ao receber informacoes do Toolbar"); 
		
		gerLog.log(0, Definicoes.INFO, "ConexaoToolbar", "ler", "Retorno do toolbar:"+result);
		return result;
	}
	
	/**
	 *	Executa o processo de escrita no socket.
	 *
	 *	@throws		Exception
	 */
	private void escrever(String linha) throws Exception
	{
		// A separacao de linha eh realizada atraves dos caracters \r\n devido
		// ao servidor socket do protocolo unico ser utilizado em windows
		this.escrita.write(linha+"\r\n");
		this.escrita.flush();
	}
	
	/**
	 *	Obtem do Toolbar o numero do protocolo unico.
	 *
	 *	@throws		Exception
	 */
	public long consultarProtocoloUnico() throws Exception
	{
		ElementoProtocoloToolbar requisicao		= ElementoProtocoloToolbar.REQ_NUM_PROT_UNICO;
		ElementoProtocoloToolbar recebimento	= null;
		
		if(this.socket == null)
			throw new SocketException("Conexao com o Toolbar nao estabelecida");
		
		//Enviando a requisicao do numero do protocolo unico.
		this.escrever(ParserLinhaProtocoloToolbar.format(requisicao));
		//Recebendo a resposta da requisicao do numero do protocolo unico.
		recebimento = ParserLinhaProtocoloToolbar.parse(this.ler());
		
		//Caso o recebimento seja igual a null ou o codigo de retorno nao seja referente a sucesso,   
		//significa erro durante a execucao do protocolo. Desta forma e necessario abortar a execucao.
		if(recebimento == null)
			throw new SocketException("Erro durante a obtencao do numero do Protocolo Unico gerado pelo Toolbar");
		if(!recebimento.isSucesso())
			throw new SocketException(recebimento.getMensagemRetorno());
		if(!recebimento.equals(ElementoProtocoloToolbar.MSG_REQ_NUM_PROT_UNICO))
			throw new SocketException("Erro de execucao do Protocolo Socket. A mensagem de retorno nao corresponde a esperada");
		
		return recebimento.getNumeroProtocolo();
	}
}