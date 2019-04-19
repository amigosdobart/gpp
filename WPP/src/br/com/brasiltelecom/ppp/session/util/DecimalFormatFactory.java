/*
 * Created on 11/05/2004
 *
 */
package br.com.brasiltelecom.ppp.session.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
//import java.util.Locale;

/**
 * @author Alberto Magno
 *
 */
public class DecimalFormatFactory extends DecimalFormat {

	public DecimalFormatFactory(){
	}
	
	public static DecimalFormat getMoeda(){
		//DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.FRANCE);
		DecimalFormat df = new DecimalFormat("#,##0,00");
		return df;
	}
	
	public static String formataMonetario( double valor ) 
	{
		DecimalFormat df = new DecimalFormat();
		df.setDecimalSeparatorAlwaysShown(true);
		df.setGroupingSize(3);
		df.setGroupingUsed(true);
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setMinimumIntegerDigits(1);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		return df.format(valor); 
	}


}
