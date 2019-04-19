package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  Representa um relatorio da F�brica de Relatorios (TBL_REL_FABRICA_RELATORIO)
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
	    * Construtor padr�o da classe.
	    */
	   public OrigemEstorno () 
	   {
	    
	   }

	   /**
	    * M�todo respons�vel por obter o nome da origem.
		* @return String.
		*/
	   public String getOrigem() 
	   {
	      return origem;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o nome da origem.
	   * @param  Operacao Instancia de <code>Operacao</code>.
	   */
	   public void setOrigem(String ori) 
	   {
		   origem = ori;
	   }


	   /**
	    * M�todo respons�vel por obter a descricao da origem.
		* @return String
		*/
	   public String getDescricao() 
	   {
	      return descricao;
	   }
	   
	   /**
	   * M�todo respons�vel por definir a descricao da origem.
	   * @param aDescricao descricao da origem
	   */
	   public void setDescricao(String aDescricao) 
	   {
		   descricao = aDescricao;
	   }
	   
	   /**
	    * M�todo respons�vel por obter o tipo de analise.
		* @return String
		*/
	   public String getAnalise() 
	   {
	      return analise;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o tipo de analise.
	   * @param aAnalise Tipo de analise.
	   */
	   public void setAnalise(String aAnalise) 
	   {
		   analise = aAnalise;
	   }
}
