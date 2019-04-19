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
	    * Construtor padrão da classe.
	    */
	   public LoteEstornoExpurgoPulaPula () 
	   {
	    
	   }

	   /**
	    * Método responsável por obter o identificador do lote.
		* @return String.
		*/
	   public String getId() 
	   {
	      return id;
	   }
	   
	   /**
	   * Método responsável por definir o identificador do lote.
	   * @param  lote Identificacao do lote.
	   */
	   public void setId(String loteId) 
	   {
	      this.id = loteId;
	   }


	   /**
	    * Método responsável por obter o numero de registros do lote.
		* @return numero de registros.
		*/
	   public int getNumRegistros() 
	   {
	      return numRegistros;
	   }
	   
	   /**
	   * Método responsável por definir o numero de registros do lote.
	   * @param num Numero de registros.
	   */
	   public void setNumRegistros(int num) 
	   {
	      numRegistros = num;
	   }
	   
}
