package com.brt.gpp.comum.interfaceEscrita;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.arquivoInterface.ArquivoInterface;
import com.brt.gpp.comum.arquivoInterface.ArquivoInterfaceDAO;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Configuracoes do Gerador de Arquivo de Interface da Brasil Telecom.
 * <hr>
 * O atributos <b><code>"observacao"</code></b> e <b><code>"path"</code></b>
 * estao contidos no mapa de atributos dinamicos.
 * <hr>
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 03/10/2007
 */
public class ArquivoInterfaceConfig extends InterfaceConfiguracao
{
	private String				delimitador;
	private int					codRemessa;
	private String				sistema;
	private ArquivoInterface	arquivoInterface;
	private String				classe;
	private boolean				delimitado;

	public static final String OBSERVACAO = "observacao";
	public static final String PATH = "path";
	// Parametro no Arquivo de Configuracoes do GPP
	private static final String DIR_ROOT_ARQUIVO_INTERFACE	= "DIR_ROOT_ARQUIVO_INTERFACE";

	/***************************
	 * CONSTANTES E DEFINICOES *
	 ***************************/
	// Codigo da Remessa
	public static final int REMESSA    = 1;
	public static final int RETORNO    = 2;

	public synchronized InterfaceEscrita getInterfaceEscrita()
	{
		if(escrita == null)
			escrita = new ArquivoInterfaceEscrita(this);

		return escrita;
	}
	/**
	 * Nova instancia com a maioria dos Atributos preenchidos.<br>
	 *
	 * @param codRemessa		Codigo da Remessa: 1 - Remessa, 2 - Retorno
	 * @param nomInterface		Nome da Interface do sistema mapeada no banco
	 * @param sistema			Nome do sistema: GPP, CLY, etc
	 * @param nomeArquivo		Nome do arquivo de Saida
	 * @param path				Path de saida
	 */
	public ArquivoInterfaceConfig(int codRemessa, String nomInterface, String sistema, String nomeArquivo, String path)
	{
		this.delimitador    = ";";
		this.codRemessa = codRemessa;
		this.sistema = sistema;

		ArquivoInterfaceDAO arquivoInterfaceDAO = null;
		try
		{
			arquivoInterfaceDAO = new ArquivoInterfaceDAO(this.getIdProcesso());
			//Recupera ArquivoInterface Atualizado
			this.arquivoInterface = arquivoInterfaceDAO.atualizarArquivoInterface(nomInterface);
		}
		catch(GPPInternalErrorException e)
		{
			this.log.log(this.idProcesso, Definicoes.ERRO, this.classe, this.classe,
					"Erro ao consultar/atualizar tabela TBL_GER_ARQUIVO_INTERFACE");
		}
		finally
		{
			if(arquivoInterfaceDAO != null)
			{
				try
				{
					arquivoInterfaceDAO.fecharConexao();
				}
				catch (GPPInternalErrorException e)
				{
					this.log.log(this.idProcesso, Definicoes.ERRO, this.classe, this.classe,
					"Erro ao fechar conexao");
				}
			}
		}
		StringBuffer pathCompleto = new StringBuffer();

		String delimitador = System.getProperty("file.separator");
		if(path == null)
		{
			// Diretorio raiz
			pathCompleto.append(ArquivoConfiguracaoGPP.getInstance().getString(DIR_ROOT_ARQUIVO_INTERFACE));
			// Pasta de trabalho
			pathCompleto.append(delimitador);
			pathCompleto.append(arquivoInterface.getPathTrabalho());
		}
		else
			pathCompleto.append(path);

		pathCompleto.append(delimitador);
		pathCompleto.append(nomeArquivo);

		atributos.put(PATH, pathCompleto.toString());
		atributos.put(OBSERVACAO, "");
	}
	/**
	 * Nova instancia com a maioria dos Atributos preenchidos.<br>
	 * Este contrutor assume o diretorio de saida padrao, deinifo no
	 * arquivo de configuracoes(Atributo DIR_ROOT_ARQUIVO_INTERFACE)
	 * e na tabela de Arquivos de Interface(TBL_GER_ARQUIVO_INTERFACE)
	 *
	 * @param codRemessa		Codigo da Remessa: 1 - Remessa, 2 - Retorno
	 * @param nomInterface		Nome da Interface do sistema mapeada no banco
	 * @param sistema			Nome do sistema: GPP, CLY, etc
	 * @param nomeArquivo		Nome do arquivo de Saida
	 * @throws GPPInternalErrorException	Caso aconteca algum erro de banco.
	 */
	public ArquivoInterfaceConfig(int codRemessa, String nomInterface, String sistema, String nomeArquivo)
	{
		this(codRemessa, nomInterface, sistema, nomeArquivo, null);
	}

	/*************************
	 *** GETTERS E SETTERS ***
	 *************************/

	public int getCodRemessa()
	{
		return codRemessa;
	}
	public String getDelimitador()
	{
		return delimitador;
	}
	public String getSistema()
	{
		return sistema;
	}
	public ArquivoInterface getArquivoInterface()
	{
		return arquivoInterface;
	}
	public boolean isDelimitado()
	{
		return delimitado;
	}
	public void setDelimitado(boolean delimitado)
	{
		this.delimitado = delimitado;
	}

}
