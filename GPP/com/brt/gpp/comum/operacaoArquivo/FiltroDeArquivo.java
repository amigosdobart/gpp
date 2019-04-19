package com.brt.gpp.comum.operacaoArquivo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Magno Batista Corr�a
 *
 */
public class FiltroDeArquivo implements FilenameFilter
{
	private Pattern padrao;
	public static final String MASCARA_EXEMPLO ="^C+[A-Za-z0-9_.-]+[0-9]{8}+.java$";
	//"^C+[A-Za-z0-9_.-]+[0-9]{8}+.java$" Significa:
	// ^				come�e com
	// C				C
	// [A-Za-z0-9_.-]	uma s�rie de caracteres, mai�sculos ou minusculos, n�meros ou ponto (sem espa�o)
	// [0-9]{8}			Uma s�rie de 8 (oito) n�meros (de 0 a 9)
	// .java$			e termine com .java
	// Essa espress�o retornar� true para, por exemplo, Cat01022006.java

	public FiltroDeArquivo(String mascara)
	{
		 this.padrao = Pattern.compile(mascara);
	}

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 * Valida se o nome do arquivo em quest�o se enquadra na m�scara dada
	 */
	public boolean accept(File arquivo, String nomeArquivo)
	{
		 Matcher m = this.padrao.matcher(nomeArquivo);
		return m.matches();
	}
}
