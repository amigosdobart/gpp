package com.brt.gpp.aplicacoes.importacaoGeneva;

import java.io.File;
import java.io.FileFilter;

/**
 * Esta classe define um padrão para o nome do arquivo
 * importado do Geneva
 * @version:	0.1
 * @Autor:		Marcelo Alves Araujo
 * @since:		31/03/2006
 */
public class GenevaFileFilter implements FileFilter
{
	// Nome do arquivo a ser utilizado como padrão
	String padrao;
	
	/**
	 * Construtor 
	 * @param	String	- filtro de arquivo 
	 */
	public GenevaFileFilter(String token)
	{
		padrao = token;
	}

	/**
	 * Valida se o nome do arquivo está de acordo com
	 * o padrão estabelecido no construtor
	 * @param	File	- Diretório a ser testado
	 */
	public boolean accept(File diretorio)
	{
		if (diretorio.getName().indexOf(padrao) > -1)
			return true;
		return false;
	}

}
