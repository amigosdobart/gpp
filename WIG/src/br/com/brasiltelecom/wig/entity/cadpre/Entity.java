package br.com.brasiltelecom.wig.entity.cadpre;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Entity {

		/**
		 * Um parse MUITO simples para xml.
		 * Caso a estrutura do documento xml se torne mais complexa será necessário
		 * desenvolver uma função com parsers de verdade
		 * @param tag
		 * @param xml
		 * @return
		 */
		protected String getTagValue(String tag, String xml){
			Pattern p = Pattern.compile("<"+tag+">(.*)</"+tag+">");
			Matcher m = p.matcher(xml);
			if(m.find()){
				return m.group(1).trim();
			}
			return null;
		}
}
