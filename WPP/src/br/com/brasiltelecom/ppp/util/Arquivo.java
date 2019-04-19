package br.com.brasiltelecom.ppp.util;

import java.io.File;
import java.net.URI;
import java.util.Date;

import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * 
 * Classe para representar um arquivo a ser impresso na VM (HTML)
 * 
 * @author Bernardo Vergne Dias
 * 
 */

public class Arquivo extends File
{

	public Arquivo(String pathname) 
	{
		super(pathname);
	}
	
	public Arquivo(URI uri) 
	{
		super(uri);
	}
	
	public Arquivo(File parent, String child) 
	{
		super(parent, child);
	}
	
	public Arquivo(String parent, String child) 
	{
		super(parent, child);
	}
	
	/**
	 * Retorna a data formatada do arquivo. 
	 * 
	 * @return Data no formato String
	 */
	public String getData() 
	{
		Date date = new Date(this.lastModified());
		return Util.dateToString(date);	
	}
	
	/**
	 * Retorna o tamanho do arquivo. O retorno é formatado: <tam> <unidade>, onde <unidade> = B | KB | MB | GB 
	 * 
	 * @return Tamanho do arquivo no formato String
	 */
	public String getTamanho()
	{
		double tam = this.length();
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
	 * Compara por nome de arquivo
	 */
	public int compareTo(Object obj)
	{
		if (obj instanceof Arquivo)
			return this.getName().compareToIgnoreCase(((Arquivo)obj).getName() );
		else
			return super.compareTo(obj);
	}
}
