package com.brt.gpp.comum.arquivoInterface;
/**
 * Entidade que representa a uma linha da tabela TBL_GER_ARQUIVO_INTERFACE do GPP.
 *
 * @author Leone Parise Vieira da Silva
 * @since  27/09/2007
 */
public class ArquivoInterface
{
	private String nomeInterface;
	private int numeroArquivo;
	private String mascaraArquivo;
	private String pathTrabalho;

	public String getMascaraArquivo()
	{
		return mascaraArquivo;
	}
	public void setMascaraArquivo(String mascaraArquivo)
	{
		this.mascaraArquivo = mascaraArquivo;
	}
	public String getNomeInterface()
	{
		return nomeInterface;
	}
	public void setNomeInterface(String nomeInterface)
	{
		this.nomeInterface = nomeInterface;
	}
	public int getNumeroArquivo()
	{
		return numeroArquivo;
	}
	public void setNumeroArquivo(int numeroArquivo)
	{
		this.numeroArquivo = numeroArquivo;
	}
	public String getPathTrabalho()
	{
		return pathTrabalho;
	}
	public void setPathTrabalho(String pathTrabalho)
	{
		this.pathTrabalho = pathTrabalho;
	}
}