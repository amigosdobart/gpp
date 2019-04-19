
package com.brt.clientes.servlet;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Metodos auxiliares
 * @author Alex Pitacci Simões
 * @since 09/06/2004
 */
public class Util {

	public static String parseMsisdn(String msisdn){
		return "55" + msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
	}
	
	public static String parseCpf (String cpf) {
		return cpf.substring(0,3) + cpf.substring(4,7) + cpf.substring(8,11) + cpf.substring(12,14);		
	}
	
	public static Number parseValor(String valor) throws ParseException{
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.parse(valor);
	}
}
