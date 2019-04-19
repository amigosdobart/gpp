package br.com.brasiltelecom.ppp.action.base.fabricaRelatorios;

import java.io.File;
import java.util.Date;

import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * 
 * Classe para representar um arquivo a ser impresso na VM (HTML)
 * 
 * Essa classe pode ser removida após a efetivação do ChangeDocument 0626 que 
 * atualiza o caso de uso UC099 (o qual propôs essa classe)
 * 
 * @author Bernardo Vergne Dias
 * @deprecated
 */

public class ArquivoFabricaRelatorios {

	private File file;
		
	public ArquivoFabricaRelatorios(File file)
	{
		this.file = file;
	}
	
	/**
	 * Retorna a data formatada do arquivo. 
	 * 
	 * @return Data no formato String
	 */
	public String getData() 
	{
		Date date = new Date(file.lastModified());
		return Util.dateToString(date);	
	}
	
	/**
	 * Retorna o tamanho do arquivo. O retorno é formatado: <tam> <unidade>, onde <unidade> = B | KB | MB | GB 
	 * 
	 * @return Tamanho do arquivo no formato String
	 */
	public String getTam()
	{
		double tam = file.length();
		String[] unidades = {" B"," KB"," MB"," GB"};
		
		int index = 0;
		for (; index < 3; index++)
		{
			if (tam < 1024) break;
			tam /= 1024.;
		}
		
		tam = ((int)(tam * 100)) / 100.;
		return "" + tam + unidades[index];
	}
	
	/**
	 * Retorna o nome do arquivo.
	 * 
	 * @return Nome do arquivo.
	 */
	public String getName() 
	{
		return file.getName();
	}
	
	/**
	 * Retorna o arquivo.
	 * 
	 * @return Instancia de <code>File</code>.
	 */
	public File getFile()
	{
		return file;
	}
}
