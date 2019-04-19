package br.com.brasiltelecom.ppp.util;


/*
 * Created on 24/06/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * Classe para formtar numeros telefonicos e coletar informações sobre o tipo de ligação;
 * 
 * @author ex576091 - Alberto Magno - Accenture do Brasil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PhoneNumberFormat 
{	
	private String layout;	
	private java.util.regex.Pattern template;	
	private	java.util.regex.Matcher capture = null;	
	
	public PhoneNumberFormat()
	{
	}

	// Prepara o parser para mascarar o resultado de acordo com o padrão numérico definido;
	private void prepara(String pattern) 
	{		
		StringBuffer c = new StringBuffer();		
		StringBuffer l = new StringBuffer();		
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("(#+)");		
		java.util.regex.Matcher m = p.matcher(pattern);		
		int pos = 0;		
		int i = 0;		
		c.append("^");		
		while(m.find()) 
		{			
			c.append("(\\d{" + (m.end()-m.start()) + "})");			
			l.append(pattern.substring(pos,m.start()) + "$" + ++i);			
			pos = m.end();		
		}		
		c.append("$");		
		if (pos<pattern.length()) l.append(pattern.substring(pos));		
		this.template = java.util.regex.Pattern.compile(c.toString());		
		layout = l.toString();	
	}	

	// Formata a String e coleta dados.
	public String format(String raw) 
	{		
		//Inicia Variáveis;
		codArea = null;
		codPais = null;
		tipo = new StringBuffer();
		prefixoL0X00 = "";
				
		//Retira prefixos
		
		// Ligação internacional
		if (raw.length()>2&&raw.substring(0,2).equals("00")) 
		{
			tipo.append("LDI");
			raw=raw.substring(2);
			codPais = raw.substring(0,2);
			// Descomentar se quiser retirar o codigo do pais de ser visualizado #raw=raw.substring(2);
			codArea = raw.substring(2,4);
		} 
		else // Ligação LOCAL: 0 + CSP + NUMERO
		if (raw.length()>1&&raw.charAt(0)=='0') 
		{
			if (raw.length()>4 && raw.substring(2,4).equals("00"))
			{
				prefixoL0X00 = raw.substring(0,4);
				raw = raw.substring(4);	
				tipo.append("0x00");
				L0X00 = true;
			}else
			{
				tipo.append("LDN");
				raw = raw.substring(1);
				codArea = raw.substring(0, 2);
			}
		} 
		else // Ligação a COBRAR LOCAL: 9090 + NUMERO
		if (raw.length()>4&&raw.substring(0,4).equals("9090"))
		{ 
			tipo.append("COBRARLOCAL");
			raw=raw.substring(4); 
		}
		else // Ligação a COBRAR LDN: 90 + CN + NUMERO
		if (raw.length()>2&&raw.substring(0,2).equals("90")) 
		{
			tipo.append("COBRAR");
			if (raw.substring(2).length()>7)
			{
				raw=raw.substring(2);
				if (raw.length()>10)
				{
					codPais = raw.substring(0,2); 				
					raw = raw.substring(2);
				}
				codArea = raw.substring(0,2);
			}
				
		} else // Ligação normal com prefixo internacional 55
		if(raw.length() >= 11 && raw.substring(0, 2).equals("55"))
		{
			tipo.append("LDN");
			raw = raw.substring(2);
			codArea = raw.substring(0, 2);
		}
		else if((raw.length() == 10) && (raw.substring(0, 2).equals("55")) && (raw.substring(4).equals("100100")))
		{
		    StringBuffer result = new StringBuffer();
		    result.append("(");
		    result.append(raw.substring(2, 4));
		    result.append(")");
		    result.append(raw.substring(4, 7));
		    result.append("-");
		    result.append(raw.substring(7));
		    return result.toString();
		}
		else if(raw.length() > 6) 
		{
			tipo.append("LOCAL");
		}


		// Substituições
		
		StringBuffer mascara = new StringBuffer(raw);
		int pos = raw.length();
		if (raw.length()>6 && raw.indexOf("*")<0)
		{
			mascara.replace(mascara.length()-7,mascara.length(),"###-####");
			pos = pos - 7;
			boolean d7 = false;	
			while(pos>0)
			{
				if (pos%2==0)
				{
					if(!L0X00)mascara.replace(pos-2,pos,"(##)");
					else mascara.replace(pos-2,pos,"##");
					pos = pos - 2;
					d7 = true;
				}				
				else
				{
					if (pos>2)
					{
						if(!L0X00)mascara.replace(pos-3,pos,"(##)#");
						else mascara.replace(pos-3,pos,"###");
						pos = pos - 3;
						d7 = false;
					}
					else 
					{
						if(!L0X00)mascara.replace(pos-1,pos,"#");
						else mascara.replace(pos-1,pos,"#");
						pos = pos - 1;
						d7 = false;
					} 
					 
				}
					
			}
			if (d7) tipo.append("7"); else tipo.append("8"); 
			
		}else
		{ 
			if (raw.length()>2 && raw.substring(0,2).equals("99"))
				raw = "*" + raw.substring(2);
			if (!L0X00) 
			{
				tipo.append("ESPECIAL");
				raw = codArea!=null?"("+codArea+")"+raw.substring(2):raw;
			} 
			return !prefixoL0X00.equals("")?prefixoL0X00+"-"+raw:raw;	
		}
				
		prepara(mascara.toString());	
		capture = template.matcher(raw);		
		if (capture.matches()) return  !prefixoL0X00.equals("")?prefixoL0X00+"-"+capture.replaceAll(layout):capture.replaceAll(layout);		
		return !prefixoL0X00.equals("")?prefixoL0X00+"-"+raw:raw;	
	}	

	private String codArea = null;
	private String codPais = null;
	private boolean L0X00 = false;
	private String prefixoL0X00 = "";
	private StringBuffer tipo = null;
	

	
	public static void main(String[] args)
	{
		
		PhoneNumberFormat ph = new PhoneNumberFormat();		
		System.out.println("FORMATADO:"+ph.format("5561100100"));
		System.out.println("COD AREA:"+ph.codArea);
		System.out.println("COD PAIS:"+ph.codPais);
		System.out.println("TIPO:"+ph.tipo);
		
		System.out.println("FORMATADO:"+ph.format("0001617894556"));
		System.out.println("COD AREA:"+ph.codArea);
		System.out.println("COD PAIS:"+ph.codPais);
		System.out.println("TIPO:"+ph.tipo);

		System.out.println("FORMATADO:"+ph.format("909084017947"));
		System.out.println("COD AREA:"+ph.codArea);
		System.out.println("COD PAIS:"+ph.codPais);
		System.out.println("TIPO:"+ph.tipo);
	}
	

	/**
	 * @return
	 */
	public String getAreaCod()
	{
		return codArea;
	}

	/**
	 * @return
	 */
	public String getCountryCod()
	{
		return codPais;
	}

	/**
	 * @return
	 */
	public String getTipo()
	{
		return tipo.toString();
	}


}
