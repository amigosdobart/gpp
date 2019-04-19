package com.brt.gppOrdemVoucher;

import java.io.FileFilter;
import java.io.File;

public class OrdemVoucherFileFilter implements FileFilter 
{
	private String pattern;
	private String extensao;
	
	/**
	 * Metodo....:OrdemVoucherFileFilter
	 * Descricao.:Construtor da classe. 
	 * @param pattern - Padrao de nome de arquivo que devera ser filtrado
	 */
	public OrdemVoucherFileFilter(String pattern, String extensao)
	{
		this.pattern  = pattern;
		this.extensao = extensao;
	}
	
	public boolean accept(File f)
	{
		String nomeArquivo = f.getName();
		boolean accept=false;
		/* Arquivos que contem "_" no nome nao podem
		 * ser arquivos de ordem de vouchers
		 */
		if ( nomeArquivo.indexOf("_") > -1 )
			return false;
			
		if ( nomeArquivo.indexOf(pattern) > -1 )
		{
			accept=true;
			
			if ( extensao != null )
				if ( nomeArquivo.indexOf(extensao) > -1 )
					accept=true;
				else accept=false;
		}
		return accept;
	}
}
