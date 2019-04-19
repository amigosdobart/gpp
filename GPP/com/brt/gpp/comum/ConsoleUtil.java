package com.brt.gpp.comum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe responsavel pelo tratamento de parametros de console.
 * <p>
 * Os parametros sao distribuidos em uma <code>List</code> e um <code>Map</code> de valores.<br>
 * O <code>Map</code> e construido tomando o nome do parametro com chave e o parametro seguinte como valor.
 * Os parametros nomeados devem comecar com <code>'-'</code>(hifen) seguido de uma letra e uma serie
 * de letras, numeros, <code>'_', '-' e '.'</code>.<br>Os valores podem ser <code>null</code> caso existam
 * dois ou mais parametros nomeados adjacentes ou se o valor esperado estrapola a quantidade de parametros.
 * Outros parametros que nao seguem as regras acima sao adicionados a lista.
 * <p>
 * Exemplo:
 * <br><code>param0 -param1 valor1 -param-2 valor2 -3param -param4 -param5</code>
 * <pre>
 * LIST :
 *      [param0, -3param]
 * MAP  :
 *      ["param1" , "valor1"]
 *      ["param-2", "valor2"]
 *      ["param4" , null]
 *      ["param5" , null]
 * </pre>
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 16/11/2007
 */
public class ConsoleUtil
{
	private List	listaParametros;
	private Map		mapaParametros;

	public ConsoleUtil(String[] args)
	{
		processarParametros(args);
	}

	private void processarParametros(String[] args)
	{
		this.listaParametros = new ArrayList();
		this.mapaParametros = new LinkedHashMap();

		for(int i = 0; i < args.length; i++)
		{
			if(args[i].matches("^-[a-zA-Z][\\w\\.-]*$"))
			{
				String chave = args[i].substring(1);
				String valor = null;
				if(i+1 < args.length && !args[i+1].matches("^-[a-zA-Z][\\w\\.-]*$"))
					valor = args[++i];
				mapaParametros.put(chave, valor);
			}
			else
				listaParametros.add(args[i]);
		}
	}

	/**
	 * Formata uma String de acordo com patterns definidos.<br>
	 * <code>%t{yyyyMMdd}</code> - Escreve uma Data no formato dado<br>
	 * <code>%p{n}</code> - Escreve uma parametro contido na lista de parametros
	 * onde <code>n</code> e o indice do parametro<br>
	 *
	 * @param str		String que sera formatada
	 * @param params	Lista de parametros
	 * @return String formatada
	 */
	public static String formatarString(String str, List params)
	{
		if(str == null)
			return null;

		String dataPattern  = "%[Tt]\\{(.+?)\\}";
		String paramPattern = "%[Pp]\\{(\\d+?)\\}";
		StringBuffer sb = new StringBuffer(str);
		// Inclusao Data
		Pattern p = Pattern.compile(dataPattern);
		Matcher m = p.matcher(sb.toString());
		while(m.find())
		{
			sb.delete(m.start(), m.end());
			SimpleDateFormat sdf = new SimpleDateFormat(m.group(1));
			sb.insert(m.start(), sdf.format(new Date()));
			m = p.matcher(sb.toString());
		}
		// Inclusao Parametros SQL
		p = Pattern.compile(paramPattern);
		m = p.matcher(sb.toString());
		while(m.find())
		{
			sb.delete(m.start(), m.end());
			int ind = Integer.parseInt(m.group(1));
			sb.insert(m.start(), params.get(ind).toString());
			m = p.matcher(sb.toString());
		}

		return sb.toString();
	}

	public List getListaParametros()
	{
		return listaParametros;
	}
	public Map getMapaParametros()
	{
		return mapaParametros;
	}
}
