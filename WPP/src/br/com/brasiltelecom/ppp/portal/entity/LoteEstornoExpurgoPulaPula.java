package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  Representa um lote de Estorno/Expurgo de Bonus Pula Pula 
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class LoteEstornoExpurgoPulaPula 
{

	   /** Identificador do Lote. */	
	   private String id;
	   
	   /** Total de registros. */
	   private int numRegistros;
	   
	   /**
	    * Construtor padr�o da classe.
	    */
	   public LoteEstornoExpurgoPulaPula () 
	   {
	    
	   }

	   /**
	    * M�todo respons�vel por obter o identificador do lote.
		* @return String.
		*/
	   public String getId() 
	   {
	      return id;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o identificador do lote.
	   * @param  lote Identificacao do lote.
	   */
	   public void setId(String loteId) 
	   {
	      this.id = loteId;
	   }


	   /**
	    * M�todo respons�vel por obter o numero de registros do lote.
		* @return numero de registros.
		*/
	   public int getNumRegistros() 
	   {
	      return numRegistros;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o numero de registros do lote.
	   * @param num Numero de registros.
	   */
	   public void setNumRegistros(int num) 
	   {
	      numRegistros = num;
	   }
	   
}
