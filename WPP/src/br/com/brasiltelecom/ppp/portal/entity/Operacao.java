package br.com.brasiltelecom.ppp.portal.entity;


//import br.com.brasiltelecom.entity.base.PersistentAdapter;

/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade respons�vel pela obten��o e defini��o das Opera��es ou Tarefas que podem ser realizada pelo o Usu�rio na Aplica��o.
 * 
 */

public class Operacao  {
   /** Identificador �nico da Opera��o. */	
   private int id;
   
   /** Nome da Opera��o. */
   private String nome;
   
   /** N�vel ou hierarquia da opera��o. */
   private int nivel;
   
   /** Tipo de Opera��o. <br>
    *  Opera��es de A��o (Inclus�o,Exclus�o e Altera��o) e de visualiza��o de menus.
    */
   private String tipo;

   /** Nome da URL a ser chamada por uma opera��o de MENU. */
   private String url;
   
   /** Descri��o da Opera��o. */
   private String descricao;
   
   /** N�vel para apresenta��o em tela - Provis�rio */
   private String nivelalfa;
   
   
   

   /**
    * Construtor padr�o da classe.
    */
   public Operacao() {
    
   }

   /**
    * M�todo respons�vel por obter o identificador da Opera��o.
	* @return id Identificador �nico da Opera��o.
	*/
   public int getId() {
      return id;
   }
   
   /**
   * M�todo respons�vel por definir o identificador �nico da Opera��o.
   * @param aId Identificador �nico da Opera��o.
   */
   public void setId(int aId) {
      id = aId;
   }


   /**
    * M�todo respons�vel por obter o Nome da Opera��o.
	* @return nome String com o nome da Opera��o.
	*/
   public String getNome() {
      return nome;
   }
   
   /**
   * M�todo respons�vel por definir o Nome da Opera��o.
   * @param aNome Nome da Opera��o.
   */
   public void setNome(String aNome) {
      nome = aNome;
   }
   
   /**
    * M�todo respons�vel por obter o N�vel hierarquico de uma Opera��o.
	* @return nivel N�vel da Opera��o.
	*/
   public int getNivel() {
      return nivel;
   }
   
   /**
   * M�todo respons�vel por definir o N�vel da Opera��o.
   * @param aNivel N�vel da Opera��o.
   */
   public void setNivel(int aNivel) {
      nivel = aNivel;
   }
   
   /**
    * M�todo respons�vel por obter o Tipo de uma Opera��o.
	* @return tipo Tipo da Opera��o.
	*/
   public String getTipo() {
      return tipo;
   }
   
   /**
   * M�todo respons�vel por definir o Tipo da Opera��o.
   * @param aTipo Tipo da Opera��o.
   */
   public void setTipo(String aTipo) {
      tipo = aTipo;
   }


   /**
    * M�todo respons�vel por obter a URL que uma Opera��o pode executar.
	* @return url URL que uma Opera��o pode executar.
	*/
	public String getUrl() {
		return url;
	}
	
   /**
   * M�todo respons�vel por definir a URL que uma Opera��o pode executar.
   * @param url URL que uma Opera��o pode executar.
   */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
    * M�todo respons�vel por obter a Descri��o da Opera��o.
	* @return descricao Descri��o da Opera��o.
	*/
	public String getDescricao() {
      return descricao;
    }

   /**
   * M�todo respons�vel por definir a Descri��o para a Opera��o.
   * @param descricao Descri��o da Opera��o.
   */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * @return M�todo que retorna o valor do nivelalfa para apresenta��o na tela. 
	 */
	public String getNivelalfa() {
		return nivelalfa;
	}
	/**
	 * @param M�todo que configura o valor do nivelalfa para apresenta��o na tela. 
	 */
	public void setNivelalfa(String nivelalfa) {
		this.nivelalfa = nivelalfa;
	}
}
