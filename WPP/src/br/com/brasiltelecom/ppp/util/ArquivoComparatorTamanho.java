package br.com.brasiltelecom.ppp.util;

import java.util.Comparator;

/** 
 * Ordena uma lista de arquivos (objetos do tipo Arquivo).
 * 
 * Exemplo: 
 *   
 *   ArrayList arquivos = GerenciadorArquivo.listaArquivos("pathTo");
 *   Collections.sort(arquivos, new ArquivoComparatorTamanho());
 *   
 * Para ordem decrescente faça:
 * 
 * 	 Collections.reverse(arquivos);
 * 
 * @author Bernardo Vergne Dias
 * @since 24/01/2007
 */
public class ArquivoComparatorTamanho implements Comparator 
{
	public int compare(Object o1, Object o2) 
	{
		return (int)(((Arquivo)o1).length() - ((Arquivo)o2).length());
	}
}

