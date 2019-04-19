package br.com.brasiltelecom.ppp.action.base.fabricaRelatorios;

/**
 * 
 * Classe para armazenar os parametros especificos de cada
 * relatório da Fabrica de Relatorios
 * 
 * @author Bernardo Vergne Dias
 * 
 */

public class ParametrosFabricaRelatorios 
{
	//Action configurada no struts-config
	private String showAction;		
	
	//Acoes (nome da acao no banco de dados):
	private String downloadEntrada;
	private String downloadSaida;
	private String uploadEntrada;
	
	//GIF do titulo (VM)
	private String imgTitulo;
	
	/**
	 * Método principal da classe
	 * 
	 * @param showAction nome do action configurado no struts-config.
	 * @param downloadEntrada nome da acao que permite fazer download dos arquivos de entrada.
	 * @param downloadSaida nome da acao que permite fazer download dos arquivos de saida.
	 * @param uploadEntrada nome da acao que permite fazer upload de arquivo de entrada.
	 * @param imgTitulo nome da figura (gif) a ser mostrada na regiao superior da pagina (titulo).
	 */
	
	public ParametrosFabricaRelatorios(
			String showAction,
			String downloadEntrada,
			String downloadSaida,
			String uploadEntrada,
			String imgTitulo) 
	{
		this.showAction = showAction;
		this.downloadEntrada = downloadEntrada;
		this.downloadSaida = downloadSaida;
		this.uploadEntrada = uploadEntrada;
		this.imgTitulo = imgTitulo;
	}
	
	public String getShowAction() { return showAction; }
		
	public String getDownloadEntrada() { return downloadEntrada; }
	public String getDownloadSaida() { return downloadSaida; }
	public String getUploadEntrada() { return uploadEntrada; }
	
	public String getImgTitulo() { return imgTitulo; }

}
