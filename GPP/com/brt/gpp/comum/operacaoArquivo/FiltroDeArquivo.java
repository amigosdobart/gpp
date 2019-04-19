package com.brt.gpp.comum.operacaoArquivo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Magno Batista Corrêa
 *
 */
public class FiltroDeArquivo implements FilenameFilter
{
	private Pattern padrao;
	public static final String MASCARA_EXEMPLO ="^C+[A-Za-z0-9_.-]+[0-9]{8}+.java$";
	//"^C+[A-Za-z0-9_.-]+[0-9]{8}+.java$" Significa:
	// ^				começe com
	// C				C
	// [A-Za-z0-9_.-]	uma série de caracteres, maiúsculos ou minusculos, números ou ponto (sem espaço)
	// [0-9]{8}			Uma série de 8 (oito) números (de 0 a 9)
	// .java$			e termine com .java
	// Essa espressão retornará true para, por exemplo, Cat01022006.java

	public FiltroDeArquivo(String mascara)
	{
		 this.padrao = Pattern.compile(mascara);
	}

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 * Valida se o nome do arquivo em questão se enquadra na máscara dada
	 */
	public boolean accept(File arquivo, String nomeArquivo)
	{
		 Matcher m = this.padrao.matcher(nomeArquivo);
		return m.matches();
	}
}
