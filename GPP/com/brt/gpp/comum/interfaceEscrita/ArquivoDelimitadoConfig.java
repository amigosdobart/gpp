package com.brt.gpp.comum.interfaceEscrita;

/**
 * Configuracoes do Gerador de Arquivo Delimitado.
 * <hr>
 * O atributos <b><code>"path"</code></b>
 * esta contido no mapa de atributos dinamicos.
 * <hr>
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 03/10/2007
 */
public class ArquivoDelimitadoConfig extends InterfaceConfiguracao
{
	private String[] cabecalho;
	private String   titulo;
	private boolean  mostraTitulo;
	private boolean  mostraCabecalho;
	private String   delimitador;

	public static final String PATH = "path";

	public synchronized InterfaceEscrita getInterfaceEscrita()
	{
		if(escrita == null)
			escrita = new ArquivoDelimitadoEscrita(this);

		return escrita;
	}

	/**
	 * Novo ArquivoDelimitadoConfig com Titulo.
	 *
	 * @param path	 		Path e nome do arquivo.
	 * @param delimitador	Caracter utilizado para separar os campos
	 * @param params 		Titulo para ser escrito no arquivo
	 */
	public ArquivoDelimitadoConfig(String delimitador, String titulo, String[] cabecalho)
	{
		this.delimitador = delimitador;
		this.cabecalho   = cabecalho;
		this.titulo	     = titulo;
	}

	/*************************
	 *** GETTERS E SETTERS ***
	 *************************/

	public String[] getCabecalho()
	{
		return cabecalho;
	}
	
	public String getTitulo()
	{
		return titulo;
	}

	public boolean isMostraTitulo()
	{
		return mostraTitulo;
	}

	public void setMostraTitulo(boolean mostraTitulo)
	{
		this.mostraTitulo = mostraTitulo;
	}

	public String getDelimitador()
	{
		return delimitador;
	}

	public boolean isMostraCabecalho() 
	{
		return mostraCabecalho;
	}

	public void setMostraCabecalho(boolean mostraCabecalho) 
	{
		this.mostraCabecalho = mostraCabecalho;
	}
	
	
}
