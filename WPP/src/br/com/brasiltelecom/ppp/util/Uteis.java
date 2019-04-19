package br.com.brasiltelecom.ppp.util;

/**
 * Contém metodos de uso geral.
 * 
 * @author Bernardo Dias
 * @since 25/01/2007
 */
public class Uteis {
	
	/**
	 * Concatena um array de <code>String<code> segundo um delimitador especificado
	 * 
	 * @param array Lista de strings
	 * @param delimiter Delimitador
	 * @return Uma <code>String<code> formada pelos itens do array concatenados com o delimitador
	 */
	public static String arrayParaString(String[] array, String delimiter)
	{
		StringBuffer buff = new StringBuffer();
		
		for (int i = 0; i < array.length - 1; i++)
		{
			buff.append(array[i]);
			buff.append(delimiter);
		}
		
		buff.append(array[array.length - 1]);
		return buff.toString();
	}

}
