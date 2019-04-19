package br.com.brasiltelecom.ppp.util;

import java.util.Comparator;

/** 
 * Ordena uma lista de arquivos (objetos do tipo Arquivo).
 * 
 * Exemplo: 
 *   
 *   ArrayList arquivos = GerenciadorArquivo.listaArquivos("pathTo");
 *   Collections.sort(arquivos, new ArquivoComparatorNome());
 *   
 * Para ordem decrescente faça:
 * 
 * 	 Collections.reverse(arquivos);
 * 
 * @author Bernardo Vergne Dias
 * @since 24/01/2007
 */
public class ArquivoComparatorNome implements Comparator 
{
	public int compare(Object o1, Object o2) 
	{
		return ((Arquivo)o1).getName().compareToIgnoreCase(((Arquivo)o2).getName());
	}
}
