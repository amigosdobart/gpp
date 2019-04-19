package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  Representa um relatorio da Fábrica de Relatorios (TBL_REL_FABRICA_RELATORIO)
 * 
 * @author Bernardo Vergne Dias
 * @since 04/12/2006
 */
public class FabricaRelatorio 
{

	   /** Identificador da Operação. */	
	   private Operacao operacao;
	   
	   /** Nome do Relatorio. */
	   private String nome;
	   
	   /** Pasta no sistema de arquivos do servidor. */
	   private String pasta;
	   
	   /** Mascara para o nome dos arquivos referentes ao relatorio */
	   private String mascara;

	   /** Delimitador de arquivo. */
	   private String delimitador;
	   
	   /**
	    * Construtor padrão da classe.
	    */
	   public FabricaRelatorio () 
	   {
	    
	   }

	   /**
	    * Método responsável por obter o identificador da Operação.
		* @return Operacao Instancia de <code>Operacao</code>.
		*/
	   public Operacao getOperacao() 
	   {
	      return operacao;
	   }
	   
	   /**
	   * Método responsável por definir o identificador da Operação.
	   * @param  Operacao Instancia de <code>Operacao</code>.
	   */
	   public void setOperacao(Operacao opr) 
	   {
	      operacao = opr;
	   }


	   /**
	    * Método responsável por obter o Nome do Relatorio.
		* @return nome String com o nome do Relatorio.
		*/
	   public String getNome() 
	   {
	      return nome;
	   }
	   
	   /**
	   * Método responsável por definir o Nome do Relatorio.
	   * @param aNome Nome do Relatorio.
	   */
	   public void setNome(String aNome) 
	   {
	      nome = aNome;
	   }
	   
	   /**
	    * Método responsável por obter o nome da Pasta no sistema de arquivos do servidor.
		* @return pasta Pasta no sistema de arquivos do servidor.
		*/
	   public String getPasta() 
	   {
	      return pasta;
	   }
	   
	   /**
	   * Método responsável por definir o nome da Pasta no sistema de arquivos do servidor.
	   * @param aNivel Pasta no sistema de arquivos do servidor.
	   */
	   public void setPasta(String aPasta) 
	   {
	      pasta = aPasta;
	   }
	   
	   /**
	    * Método responsável por obter a Mascara para o nome dos arquivos referentes ao relatorio.
		* @return mascara Mascara para o nome dos arquivos referentes ao relatorio.
		*/
	   public String getMascara() 
	   {
	      return mascara;
	   }
	   
	   /**
	   * Método responsável por definir a Mascara para o nome dos arquivos referentes ao relatorio.
	   * @param aMascara Mascara para o nome dos arquivos referentes ao relatorio.
	   */
	   public void setMascara(String aMascara) 
	   {
		   mascara = aMascara;
	   }


	   /**
	    * Método responsável por obter o Delimitador de arquivo.
		* @return delimitador Delimitador de arquivo.
		*/
		public String getDelimitador() 
		{
			return delimitador;
		}
		
	   /**
	   * Método responsável por definir o Delimitador de arquivo.
	   * @param aDelimitador Delimitador de arquivo.
	   */
		public void setDelimitador(String aDelimitador) 
		{
			delimitador = aDelimitador;
		}
	}
