package br.com.brasiltelecom.ppp.portal.relatorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Armazena o conteúdo gerado por um relatório e algumas informacoes
 * associadas, como timestamp e status
 * 
 * @author Bernardo Vergne Dias
 * Data: 17/10/2007
 *
 */
public class ResultadoRelatorio 
{
	public static final int CONCLUIDO = 0;
	public static final int EM_PROCESSAMENTO = 1;
	public static final int ERRO = 2;
	
	String 	nomeRelatorio;
	
	String  contentType;			// Usado no cabecalho HTTP no momento do download
	String  contentDisposition;		// Usado no cabecalho HTTP no momento do download
	
	String  rootFabricaWPP;			// Pasta no sistema para gravacao do arquivo
	String  nomeArquivo;			// No do arquivo dentro da pasta rootFabricaWPP. Sem extensao.
	
	int 	codStatus; 
	String 	desStatus;
	
	public ResultadoRelatorio() 
	{
		setCodStatus(EM_PROCESSAMENTO); 
	}
	
	public int getCodStatus() 
	{
		return codStatus;
	}
	
	public void setCodStatus(int codStatus) 
	{
		this.codStatus = codStatus;
		
		switch (codStatus)
		{
		case CONCLUIDO:
			desStatus = "Relatorio concluido.";
			break;
		case EM_PROCESSAMENTO:
			desStatus = "Em processamento.";
			break;
		case ERRO:
			desStatus = "Erro desconhecido.";
			break;
		default:
			desStatus = "Status desconhecido.";
		}
	}
	
	/**
	 * Abre o arquivo para gravacao do relatorio.
	 * Caso ja existir um arquivo no sistema, ele eh removido.
	 * 
	 * @return OutputStream
	 */
	public FileOutputStream criarArquivo() throws Exception
	{
		String filename = rootFabricaWPP + System.getProperty("file.separator") + nomeArquivo;
		File file = new File(filename);
		if (file.exists())
		{
			file.delete();
			file = new File(filename);
		}
		
		file.createNewFile();
		return new FileOutputStream(file);
	}
	
	/**
	 *  Le o relatorio gravado em arquivo.
	 *  
	 *  @return Todo o conteudo do arquivo
	 */
	public byte[] lerArquivo() throws Exception
	{
		String filename = rootFabricaWPP + System.getProperty("file.separator") + nomeArquivo;
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] dados = new byte[(int)file.length()];
		fis.read(dados);
		fis.close();
		return dados;
	}
	
	public String getDesStatus() 
	{
		return desStatus;
	}
	
	public void setDesStatus(String desStatus) 
	{
		this.desStatus = desStatus;
	}
	
	public String getNomeRelatorio() 
	{
		return nomeRelatorio;
	}
	
	public void setNomeRelatorio(String nomeRelatorio) 
	{
		this.nomeRelatorio = nomeRelatorio;
	}
	
	public String getContentType() 
	{
		return contentType;
	}

	public void setContentType(String contentType) 
	{
		this.contentType = contentType;
	}

	public String getContentDisposition() 
	{
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) 
	{
		this.contentDisposition = contentDisposition;
	}

	public String getNomeArquivo() 
	{
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) 
	{
		this.nomeArquivo = nomeArquivo;
	}

	public String getRootFabricaWPP() 
	{
		return rootFabricaWPP;
	}

	public void setRootFabricaWPP(String rootFabricaWPP) 
	{
		this.rootFabricaWPP = rootFabricaWPP;
	}

	
	
}
