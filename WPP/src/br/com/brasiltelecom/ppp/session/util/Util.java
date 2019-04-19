package br.com.brasiltelecom.ppp.session.util;

/**
 * @author ex471453
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;




public class Util {
	
	private static SimpleDateFormat dataFormat;
	private static SimpleDateFormat simpleDataFormat;
	
	private static SimpleDateFormat dataFormatVitria;
	private static SimpleDateFormat horaFormatVitria;
	
	private static SimpleDateFormat dataHoraArqUpload;
	
	private static NumberFormat formatoDecimal;
	private static NumberFormat formatoDecimalQtde;
	
	static{  
		dataFormat = new SimpleDateFormat(Constantes.DATA_HORA_FORMATO);
		simpleDataFormat = new SimpleDateFormat(Constantes.DATA_FORMATO);
		dataFormatVitria = new SimpleDateFormat("yyyyMMdd");
		horaFormatVitria = new SimpleDateFormat("hhmm");
		dataHoraArqUpload = new SimpleDateFormat("yyyyMMddHHmmss");
		formatoDecimal = NumberFormat.getInstance(Locale.GERMAN);
		formatoDecimal.setMaximumFractionDigits(2);	
		formatoDecimalQtde = NumberFormat.getInstance(Locale.GERMAN);
		formatoDecimalQtde.setMaximumFractionDigits(3);	
	}


	public static Map parameterToMap(HttpServletRequest request){
		Map result = new HashMap();
		for(Enumeration lista = request.getParameterNames(); lista.hasMoreElements();){
			String chave = (String) lista.nextElement();
			result.put(chave, request.getParameter(chave));	
		}
		return result;
	}
	
	
	public static String dateToString(Date data){
		if(data !=null){
			return dataFormat.format(data);
		}
		else{
			return null;	
		}
	}
	
	public static Date stringToDate(String data){
		try{
			return dataFormat.parse(data);
		}
		catch(ParseException e){
			return null;	
		}
	}
	
	public static String dateToStringVitria(Date data){
		return dataFormatVitria.format(data);
	}
	
	public static String horaToStringVitria(Date data){
		return horaFormatVitria.format(data);
	}


	/**
	 * Método para retornar a Data concatenada com hora no formato para definição do nome do arquivo de upload.
	 * @param data Data Atual
	 */

	public static String dateHourArqToString(Date data){
		return dataHoraArqUpload.format(data);
	}


	public static String doubleToString(double valor) {
		
		String formatoReal = formatoDecimal.format(valor);
		
		if ( formatoReal != null ) {
		
			int tam = formatoReal.indexOf( "," );
			
			if ( tam != -1 ) {
				
				int tam2 = formatoReal.substring( tam ).length();
				
				if ( tam2 == 2 ) {
				
				   formatoReal += "0";
				
				}
			} else {
				
				formatoReal += ",00";
			
			}
		}
		
		return formatoReal;
	}
	
	public static double stringToDouble(String valor){
			
		try
		{
			Number numero = formatoDecimal.parse(valor);
			
			return numero.doubleValue();

		}
  		catch (ParseException a2){
			
			return 0.0;
  		}

			
	}	

	public static String doubleToStringQtde(double valor) {

		String formatoReal = formatoDecimalQtde.format(valor);
		if ( formatoReal != null ) {
		
			int tam = formatoReal.indexOf( "," );
			
			if ( tam != -1 ) {
				
				int tam2 = formatoReal.substring( tam ).length();
				
				if (tam2 == 3) {
				
				   formatoReal += "0";
				
				} else if ( tam2 == 2 ) {
				
				   formatoReal += "00";
				
				}
			} else {
				
				formatoReal += ",000";
			
			}
		}
				
		return formatoReal;	
		
	}
	
	public static double stringToDoubleQtde(String valor){
			
		try
		{
			Number numero = formatoDecimalQtde.parse(valor);
			
			return numero.doubleValue();

		}
  		catch (ParseException a2){
			
			return 0.0;
  		}

			
	}	

	
	public static String simplesDateToString(Date data){
		if(data !=null){
			return simpleDataFormat.format(data);
		}
		else{
			return null;	
		}
	}
	
	public static Date stringToSimpleDate(String data){ 
		try{
			return simpleDataFormat.parse(data);
		}
		catch(ParseException e){
			return null;	
		}
	}
	
	public static String getDateHour() {
		return dateToString(new Date());
	}
	
	/**
	 * Método para formatar valores sem mascara.
	 * @param sValor String.
	 * @return String.
	 */
    public static String formataValorSemMascara(String sValor) {
		String retorno = "";
        if(sValor == null) sValor = "";
        sValor = sValor.trim();
        sValor = sValor.replace('.',' ');
        sValor = sValor.replace(',',' ');
        sValor = sValor.replace('-',' ');
        sValor = sValor.replace('/',' ');        

        for(int i=0;i<sValor.length();i++)
        {
            if(!sValor.substring(i,i+1).equals(" "))
                retorno = retorno + sValor.substring(i,i+1).trim();

        }
		return retorno;
	}
    
	/**
	 *	Recebe o numero de segundos passados por parametro em retorna o valor como String no formato #hh:mm:ss. 
	 *
	 *	@param		long					segundos					Numero de segundos.
	 *	@return		String												Segundos no formato #hh:mm:ss.
	 */
    public static String segundosParaHoras(long segundos)
    {
        StringBuffer result = new StringBuffer();
        DecimalFormat conversorHora = new DecimalFormat("#00");
        
        long horas = new Long(segundos/3600).longValue();
        segundos %= 3600;
        long minutos = new Long(segundos/60).longValue();
        segundos %= 60;
        
        result.append(conversorHora.format(horas));
        result.append(":");
        result.append(conversorHora.format(minutos));
        result.append(":");
        result.append(conversorHora.format(segundos));
        
        return result.toString();
    }
	
	   //retorna colecao com quantidade de paginas a serem mostradas [Wesley]	
   public static Collection getPages(int size,int pagesize){
   	  /**==Variaveis da rotina==*/
   	   Collection results = new ArrayList();   	  
   	   int pages = 0 ;//quantidade de paginas
   	  /**=======================*/

   	  pages = ((int)Math.ceil(size/pagesize))+1;

   	  //monta colection com valores de paginas  
   	  for (int i=0;i < pages;i++){
   	    Map mapPages = new HashMap();
   	    mapPages.put("pagina",new Integer(i)); 	
   	    results.add(mapPages);
   	    mapPages = null;
   	  }
   	 return results;
   }

   //retorna uma pagina de uma colection [Wesley]

  public static Collection getPagedItem(int page, int pagesize,Collection itens ){

     /**==Variaveis da rotina==*/
      Collection results = new ArrayList();
      ArrayList received = new ArrayList();
      int indice = 0; //guarda indice a iniciar
      int findice = 0; //guarda indice a finalizar     
    /**=======================*/

      //converte a colecao recebida em um Arraylist               
      received = (ArrayList)itens;     
      itens = null;

      //calcula os indices inicial e final
      indice  = ((pagesize * (page+1))-(pagesize-1))-1;
      findice = (indice + pagesize); 	       
      
      for (int i=indice;i < findice && i < received.size();i++){      	
      	 results.add(received.get(i));      	
      }	

      return results;
  }
   


 

 
	/**
	 *  Método para formatação de CPF
	 *  @param cpf CPF a ser formatado
	 *  @return cpfFormatado CPF com a Mascara
	 */
	
	public static String putMascaraCPF(String cpf) {
		
		if (cpf == null) 
			return "";	
		String cpfFormatado = null;
		if (cpf.length() == 11) {	
			cpfFormatado  = cpf.substring(0,3)  + "." +
				 		    cpf.substring(3,6)  + "." +	
				 			cpf.substring(6,9) + "-" +
				 			cpf.substring(9);
		} else {
			cpfFormatado = cpf;
		}
		
		return cpfFormatado;
			 				   
	}
	
	/**
	 *  Método para retirar a formatação de CPF
	 *  @param cpf Cpf a ser formatado
	 *  @return cpfFormatado Cpf sem a Mascara
	 */

	public static String removeMascaraCPF(String cpf) {
		
		return formataValorSemMascara( cpf );
	}



	/**
	 *  Método para formatação de CNPJ
	 *  @param cnpj Cnpj a ser formatado
	 *  @return cnpjFormatado Cnpj com a Mascara
	 */
	
	public static String putMascaraCNPJ(String cnpj) {
		
		if (cnpj == null) 
			return "";	
		String cnpjFormatado = null;
		if (cnpj.length() == 14) {	
			cnpjFormatado = cnpj.substring(0,2)  + "." +
				 		    cnpj.substring(2,5)  + "." +	
				 			cnpj.substring(5,8) + "/" +
				 			cnpj.substring(8,12) + "-" +
				 			cnpj.substring(12) ;
		} else {
			cnpjFormatado = cnpj;
		}
		
		return cnpjFormatado;
			 				   
	}
	
	/**
	 *  Método para retirar a formatação de CNPJ
	 *  @param cnpj Cnpj a ser formatado
	 *  @return cnpjFormatado Cnpj sem a Mascara
	 */

	public static String removeMascaraCNPJ(String cnpj) {
		
		return formataValorSemMascara( cnpj );

	}
	
	public static Collection getOperacoesLog(){
	 
	  /********Variáveis que serão utilizadas no próprio método*******/	
 	   Collection operacoes = new ArrayList(); 
	   int i =0;
	  /***************************************************************/

	  for (i=0; i < Constantes.operacoesLog.length;i++){
	  	
	    Map status = new HashMap();
	    
	    status.put("id",Constantes.operacoesLog[i][0]);//Codigo da operacao
	    status.put("descricao",Constantes.operacoesLog[i][1]);//Descricao da operacao
	    operacoes.add(status);

	    status = null;

	  }

	  return operacoes;

	}

	/**
	 *  Metodo para substituir os caracteres entity do XML pelos códigos ASCI
	 * 
	 * @param palavra: objeto do tipo String, que representa a frase a ser decodificada para o XML.
	 * 
	 * @return Retorna um objeto do tipo String, que representa a frase decodificada.
	 * 
	 * @author Eric Silva
	 * 
	*/
	public static String arrumaEntityXml (String palavra)
	{
		/**************Variaveis que serao utilizadas no metodo****************/
		 String subPalavra = "", subPalavra1 = "", subPalavra2 = "";
		/**********************************************************************/

		for (int i=0; i<Constantes.caracteresXML.length; i++)
		{

			if (!"&".equals(Constantes.caracteresXML[i][0]))
			{
				while (palavra.indexOf(Constantes.caracteresXML[i][0]) != -1)
				{
					subPalavra1 = palavra.substring(0,palavra.indexOf(Constantes.caracteresXML[i][0]));
					subPalavra2 = palavra.substring(palavra.indexOf(Constantes.caracteresXML[i][0])+1);
					palavra = subPalavra1.concat(Constantes.caracteresXML[i][1]).concat(subPalavra2);
				}
			}
			else
			{
				subPalavra = palavra;
				palavra = "";

				while (subPalavra.indexOf(Constantes.caracteresXML[i][0]) != -1)
				{
					subPalavra1 = subPalavra.substring(0,subPalavra.indexOf(Constantes.caracteresXML[i][0]));
					subPalavra = subPalavra.substring(subPalavra.indexOf(Constantes.caracteresXML[i][0])+1);
					palavra = palavra.concat(subPalavra1).concat(Constantes.caracteresXML[i][1]);
				}

				palavra = palavra.concat(subPalavra);
			}

		}

		return palavra;
	}
	
	public static String getNomeArqUpload(String arquivo) {
		int pos = arquivo.lastIndexOf("_");
		String novoNome = arquivo.substring(0, pos);
		return novoNome;
	}

}
