package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  Representa um relatorio da F�brica de Relatorios (TBL_REL_FABRICA_RELATORIO)
 * 
 * @author Bernardo Vergne Dias
 * @since 04/12/2006
 */
public class FabricaRelatorio 
{

	   /** Identificador da Opera��o. */	
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
	    * Construtor padr�o da classe.
	    */
	   public FabricaRelatorio () 
	   {
	    
	   }

	   /**
	    * M�todo respons�vel por obter o identificador da Opera��o.
		* @return Operacao Instancia de <code>Operacao</code>.
		*/
	   public Operacao getOperacao() 
	   {
	      return operacao;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o identificador da Opera��o.
	   * @param  Operacao Instancia de <code>Operacao</code>.
	   */
	   public void setOperacao(Operacao opr) 
	   {
	      operacao = opr;
	   }


	   /**
	    * M�todo respons�vel por obter o Nome do Relatorio.
		* @return nome String com o nome do Relatorio.
		*/
	   public String getNome() 
	   {
	      return nome;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o Nome do Relatorio.
	   * @param aNome Nome do Relatorio.
	   */
	   public void setNome(String aNome) 
	   {
	      nome = aNome;
	   }
	   
	   /**
	    * M�todo respons�vel por obter o nome da Pasta no sistema de arquivos do servidor.
		* @return pasta Pasta no sistema de arquivos do servidor.
		*/
	   public String getPasta() 
	   {
	      return pasta;
	   }
	   
	   /**
	   * M�todo respons�vel por definir o nome da Pasta no sistema de arquivos do servidor.
	   * @param aNivel Pasta no sistema de arquivos do servidor.
	   */
	   public void setPasta(String aPasta) 
	   {
	      pasta = aPasta;
	   }
	   
	   /**
	    * M�todo respons�vel por obter a Mascara para o nome dos arquivos referentes ao relatorio.
		* @return mascara Mascara para o nome dos arquivos referentes ao relatorio.
		*/
	   public String getMascara() 
	   {
	      return mascara;
	   }
	   
	   /**
	   * M�todo respons�vel por definir a Mascara para o nome dos arquivos referentes ao relatorio.
	   * @param aMascara Mascara para o nome dos arquivos referentes ao relatorio.
	   */
	   public void setMascara(String aMascara) 
	   {
		   mascara = aMascara;
	   }


	   /**
	    * M�todo respons�vel por obter o Delimitador de arquivo.
		* @return delimitador Delimitador de arquivo.
		*/
		public String getDelimitador() 
		{
			return delimitador;
		}
		
	   /**
	   * M�todo respons�vel por definir o Delimitador de arquivo.
	   * @param aDelimitador Delimitador de arquivo.
	   */
		public void setDelimitador(String aDelimitador) 
		{
			delimitador = aDelimitador;
		}
	}
