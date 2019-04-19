package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  Representa um relatorio da Fábrica de Relatorios (TBL_REL_FABRICA_RELATORIO)
 * 
 * @author Bernardo Vergne Dias
 * @since 20/12/2006
 */
public class OrigemEstorno 
{

	   /** Nome da origem. */	
	   private String origem;
	   
	   /** Descricao da origem. */
	   private String descricao;
	   
	   /** Tipo de analise. */
	   private String analise;
	   
	   /**
	    * Construtor padrão da classe.
	    */
	   public OrigemEstorno () 
	   {
	    
	   }

	   /**
	    * Método responsável por obter o nome da origem.
		* @return String.
		*/
	   public String getOrigem() 
	   {
	      return origem;
	   }
	   
	   /**
	   * Método responsável por definir o nome da origem.
	   * @param  Operacao Instancia de <code>Operacao</code>.
	   */
	   public void setOrigem(String ori) 
	   {
		   origem = ori;
	   }


	   /**
	    * Método responsável por obter a descricao da origem.
		* @return String
		*/
	   public String getDescricao() 
	   {
	      return descricao;
	   }
	   
	   /**
	   * Método responsável por definir a descricao da origem.
	   * @param aDescricao descricao da origem
	   */
	   public void setDescricao(String aDescricao) 
	   {
		   descricao = aDescricao;
	   }
	   
	   /**
	    * Método responsável por obter o tipo de analise.
		* @return String
		*/
	   public String getAnalise() 
	   {
	      return analise;
	   }
	   
	   /**
	   * Método responsável por definir o tipo de analise.
	   * @param aAnalise Tipo de analise.
	   */
	   public void setAnalise(String aAnalise) 
	   {
		   analise = aAnalise;
	   }
}
