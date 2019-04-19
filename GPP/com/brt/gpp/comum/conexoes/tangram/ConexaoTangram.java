package com.brt.gpp.comum.conexoes.tangram;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.brt.gpp.comum.conexoes.tangram.entidade.Requisicao;

/**
 *	Interface de comunicação com o Tangram. Suporta envio/recebimento
 *  de requisições HTTP/GET, HTTP/POST e FTP.
 *  
 *  Essa biblioteca não possui restrições de parâmetros e não está
 *  vinculada ao GPP, podendo ser importada e usada em qualquer sistema.
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 19/09/2007
 */
public class ConexaoTangram 
{
	public static final int HTTP_POST 	= 0;
	public static final int HTTP_GET 	= 1;
	public static final int FTP 		= 2;
	
	/** Endereco do host Tangram */
	private String 	host;
	
	/** Porta HTTP do Tangram */
	private int 	portaHttp = 80;

	/** Porta FTP do Tangram */
	private int 	portaFtp = 21;
	
	/** Protocolo para comunicacao com o Tangram */
	private int 	protocolo = ConexaoTangram.HTTP_POST;
		
	/** Usuario para autenticacao no Tangram */
	private String 	usuario;
	
	/** Senha para autenticacao no Tangram */
	private String 	senha;
	
	/** Tempo máximo de espera da resposta, em segundos */
	private int 	timeout = 20;
			
	/** 
	 * Intervalo de tempo, em milisegundos, para monitoramento das pastas IN e OUT
	 * do FTP. O cliente FTP da ConexaoTangram deve verificar o conteúdo dessas 
	 * pastas em intervalos de tempo aqui definido até que o tempo máximo 
	 * (timeout) seja atingido.
	 */
	private int 	intervaloMonitoramentoFtp = 500;
	
	/**
	 * Envia uma requisição ao Tangram gravando os  dados de retorno 
	 * (síncrono) na mesma entidade de requisição.
	 * 
	 * @param requisicao Requisição Tangram
	 */
	public Requisicao enviaRequisicao(Requisicao requisicao) throws Exception
	{
		switch(this.protocolo)
		{
		case HTTP_GET:
			return enviaRequisicaoHttpGet(requisicao);
		case HTTP_POST:
			return enviaRequisicaoHttpPost(requisicao);
		case FTP:
			return enviaRequisicaoFtp(requisicao);
		}
	
		return requisicao;
	}

	/**
	 * Envia uma requisição ao Tangram, via protocolo HTTP/POST.
	 * 
	 * O objetivo desse método ser privado é tentarmos manter o uso do
	 * protocolo default (POST).
	 * 
	 * @param requisicao Requisição Tangram
	 */
	private Requisicao enviaRequisicaoHttpPost(Requisicao requisicao) throws Exception
	{
		// Gera documento XML
		String tangramRequest = ParserTangram.gerarXmlRequisicao(requisicao);
		
		// Inicializa
		URL url = new URL("http://" + host + ":" + portaHttp + "/scripts/tangram.asp");
		String encoding = new sun.misc.BASE64Encoder().encode ((usuario + ":" + senha).getBytes());
		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
		httpConnection.setRequestProperty  ("Authorization", "Basic " + encoding);
		// httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);	
		
		// Processa a requisição
		DataOutputStream output = new DataOutputStream(httpConnection.getOutputStream());
		output.writeBytes(tangramRequest);
		output.flush();
		output.close();
		
		String tangramResponse = httpConnection.getResponseMessage();
		ParserTangram.processarXmlRetornoRequisicao(tangramResponse, requisicao);
		
		return requisicao;
		
		/*
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(httpConnection.getInputStream()));
		
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) 
        {
        	buffer.append(line);
        }
        reader.close();
        
        tangramResponse = buffer.toString();
        ParserTangram.processaXmlRetornoRequisicao(tangramResponse, requisicao);
	    */		
	}

	/**
	 * Envia uma requisição ao Tangram, via protocolo HTTP/GET.
	 * 
	 * O objetivo desse método ser privado é tentarmos manter o uso do
	 * protocolo default (POST).
	 * 
	 * @param requisicao Requisição Tangram
	 */
	private Requisicao enviaRequisicaoHttpGet(Requisicao requisicao) throws Exception
	{
		// Geta lista de paramentros GET
		String tangramRequest = ParserTangram.gerarUrlRequisicao(requisicao);
		
		// Inicializa
		URL url = new URL("http://" + host + ":" + portaHttp + "/scripts/tangram.asp?" + tangramRequest);
		String encoding = new sun.misc.BASE64Encoder().encode ((usuario + ":" + senha).getBytes());
		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
        httpConnection.setRequestProperty  ("Authorization", "Basic " + encoding);
        
        // Processa a requisição
		String tangramResponse = httpConnection.getResponseMessage();
		ParserTangram.processarXmlRetornoRequisicao(tangramResponse, requisicao);
	
		return requisicao;
	}
	
	/**
	 * Envia uma requisição ao Tangram, via protocolo FTP.
	 * 
	 * O objetivo desse método ser privado é tentarmos manter o uso do
	 * protocolo default (POST).
	 * 
	 * @param requisicao Requisição Tangram
	 */
	private Requisicao enviaRequisicaoFtp(Requisicao requisicao)
	{
		//TODO: deve implementar uma thread de monitoramento das pastas
		//IN e OU do FTP. Esse monitoramento é necessario para que se 
		//possa obter os dados de retorno (e status de sucesso/erro) do Tangram.
		//Esse método de ser SÍNCRONO, pois a aplicação (que utiliza essa 
		//ConexaoTangram) espera que após a conclusão da execução os dados
		//de retorno estejam presentes na entidade Requisicao.
		//Portanto, esse método deve se comportar de maneira semelhante
		//ao "enviaRequisicaoHttp" (i.e.: sincrono).
		
		throw new RuntimeException("Metodo nao implementado");
	}

	/**
	 * Obtém o endereco do host Tangram.
	 */
	public String getHost() 
	{
		return host;
	}

	/**
	 * Define o endereco do host Tangram.
	 */
	public void setHost(String host) 
	{
		this.host = host;
	}

	/**
	 * Obtém o intervalo de tempo, em milisegundos, para monitoramento 
	 * das pastas IN e OUT do FTP.
	 */
	public int getIntervaloMonitoramentoFtp() 
	{
		return intervaloMonitoramentoFtp;
	}

	/**
	 * Define o intervalo de tempo, em milisegundos, para monitoramento 
	 * das pastas IN e OUT do FTP.
	 */
	public void setIntervaloMonitoramentoFtp(int intervaloMonitoramentoFtp) 
	{
		this.intervaloMonitoramentoFtp = intervaloMonitoramentoFtp;
	}

	/**
	 * Obtém o protocolo para comunicacao com o Tangram.
	 */
	public int getProtocolo() 
	{
		return protocolo;
	}

	/**
	 * Define o protocolo para comunicacao com o Tangram.
	 * Veja constantes HTTP_GET, HTTP_POST.
	 */
	public void setProtocolo(int protocolo) 
	{
		if (protocolo < 0 || protocolo >= 3)
			throw new  IllegalArgumentException("Protocolo invalido. Use HTTP/GET, HTTP/POST ou FTP.");
		
		this.protocolo = protocolo;
	}

	/**
	 * Obtém porta FTP do Tangram.
	 */
	public int getPortaFtp() 
	{
		return portaFtp;
	}

	/**
	 * Define porta FTP do Tangram.
	 */
	public void setPortaFtp(int portaFtp) 
	{
		this.portaFtp = portaFtp;
	}

	/**
	 * Obtém porta HTTP do Tangram.
	 */
	public int getPortaHttp() 
	{
		return portaHttp;
	}

	/**
	 * Define porta HTTP do Tangram.
	 */
	public void setPortaHttp(int portaHttp) 
	{
		this.portaHttp = portaHttp;
	}

	/**
	 * Obtém a senha para autenticacao no Tangram.
	 */
	public String getSenha() 
	{
		return senha;
	}

	/**
	 * Define a senha para autenticacao no Tangram.
	 */
	public void setSenha(String senha) 
	{
		this.senha = senha;
	}

	/**
	 * Obtém o tempo máximo de espera da resposta, em segundos.
	 */
	public int getTimeout() 
	{
		return timeout;
	}

	/**
	 * Define o tempo máximo de espera da resposta, em segundos.
	 */
	public void setTimeout(int timeout) 
	{
		this.timeout = timeout;
	}

	/**
	 * Obtém o usuario para autenticacao no Tangram.
	 */
	public String getUsuario() 
	{
		return usuario;
	}

	/**
	 * Define o usuario para autenticacao no Tangram.
	 */
	public void setUsuario(String usuario) 
	{
		this.usuario = usuario;
	}
	
	
}
