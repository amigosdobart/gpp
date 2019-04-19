package GoogleSearch;

import Net.DataAccess.webservices.ElevenTest.ElevenTestLocator;

public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*GoogleSearchServiceLocator gsl = new GoogleSearchServiceLocator();
		GoogleSearchPort gss = gsl.getGoogleSearchPort();
		GoogleSearchResult result = gss.doGoogleSearch("Pellentesque a", args[0], 1, 10, false, "", false, null, null, null);
		System.out.println(result);
		*/
		
		ElevenTestLocator elt = new ElevenTestLocator();
		System.out.println(args[0] +  " " +  elt.getElevenTestSoap().bankAccountNumbersTest(args[0]));
		System.out.println(args[0] +  " " +  elt.getElevenTestSoap().stripToNumeric(args[0]));
		System.out.println(args[0] +  " " +  elt.getElevenTestSoap().BSNTest(args[0]));
	}

}
