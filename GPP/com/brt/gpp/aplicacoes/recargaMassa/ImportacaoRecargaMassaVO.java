package com.brt.gpp.aplicacoes.recargaMassa;

/**
 *	Value Object para carga de lote de recarga em massa
 * 
 *	@author		Bernardo Vergne Dias
 *	Data:		09/08/2007
 */
public class ImportacaoRecargaMassaVO 
{
	private String nomeArquivo;
	private String registro;
	private String mensagemErro;
	private int numLinha;
	
	public ImportacaoRecargaMassaVO (String nomeArquivo, String registro, int numLinha)
	{
		this.nomeArquivo = nomeArquivo;
		this.registro = registro;
		this.numLinha = numLinha;
	}
	
	public String getRegistro()
	{
		return registro;
	}

	public int getNumLinha()
	{
		return numLinha;
	}

	public String getMensagemErro() 
	{
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) 
	{
		this.mensagemErro = mensagemErro;
	}
	
	public String getNomeArquivo() 
	{
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) 
	{
		this.nomeArquivo = nomeArquivo;
	}

	public String toString() 
	{
		if (!registro.endsWith(";"))
			registro += ";";
		
		return  registro + ((mensagemErro == null) ? "" : mensagemErro);
	}
}
