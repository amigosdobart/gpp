package br.com.brasiltelecom.ppp.portal.entity;


//import br.com.brasiltelecom.entity.base.PersistentAdapter;

/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade responsável pela obtenção e definição das Operações ou Tarefas que podem ser realizada pelo o Usuário na Aplicação.
 * 
 */

public class Operacao  {
   /** Identificador único da Operação. */	
   private int id;
   
   /** Nome da Operação. */
   private String nome;
   
   /** Nível ou hierarquia da operação. */
   private int nivel;
   
   /** Tipo de Operação. <br>
    *  Operações de Ação (Inclusão,Exclusão e Alteração) e de visualização de menus.
    */
   private String tipo;

   /** Nome da URL a ser chamada por uma operação de MENU. */
   private String url;
   
   /** Descrição da Operação. */
   private String descricao;
   
   /** Nível para apresentação em tela - Provisório */
   private String nivelalfa;
   
   
   

   /**
    * Construtor padrão da classe.
    */
   public Operacao() {
    
   }

   /**
    * Método responsável por obter o identificador da Operação.
	* @return id Identificador único da Operação.
	*/
   public int getId() {
      return id;
   }
   
   /**
   * Método responsável por definir o identificador único da Operação.
   * @param aId Identificador único da Operação.
   */
   public void setId(int aId) {
      id = aId;
   }


   /**
    * Método responsável por obter o Nome da Operação.
	* @return nome String com o nome da Operação.
	*/
   public String getNome() {
      return nome;
   }
   
   /**
   * Método responsável por definir o Nome da Operação.
   * @param aNome Nome da Operação.
   */
   public void setNome(String aNome) {
      nome = aNome;
   }
   
   /**
    * Método responsável por obter o Nível hierarquico de uma Operação.
	* @return nivel Nível da Operação.
	*/
   public int getNivel() {
      return nivel;
   }
   
   /**
   * Método responsável por definir o Nível da Operação.
   * @param aNivel Nível da Operação.
   */
   public void setNivel(int aNivel) {
      nivel = aNivel;
   }
   
   /**
    * Método responsável por obter o Tipo de uma Operação.
	* @return tipo Tipo da Operação.
	*/
   public String getTipo() {
      return tipo;
   }
   
   /**
   * Método responsável por definir o Tipo da Operação.
   * @param aTipo Tipo da Operação.
   */
   public void setTipo(String aTipo) {
      tipo = aTipo;
   }


   /**
    * Método responsável por obter a URL que uma Operação pode executar.
	* @return url URL que uma Operação pode executar.
	*/
	public String getUrl() {
		return url;
	}
	
   /**
   * Método responsável por definir a URL que uma Operação pode executar.
   * @param url URL que uma Operação pode executar.
   */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
    * Método responsável por obter a Descrição da Operação.
	* @return descricao Descrição da Operação.
	*/
	public String getDescricao() {
      return descricao;
    }

   /**
   * Método responsável por definir a Descrição para a Operação.
   * @param descricao Descrição da Operação.
   */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * @return Método que retorna o valor do nivelalfa para apresentação na tela. 
	 */
	public String getNivelalfa() {
		return nivelalfa;
	}
	/**
	 * @param Método que configura o valor do nivelalfa para apresentação na tela. 
	 */
	public void setNivelalfa(String nivelalfa) {
		this.nivelalfa = nivelalfa;
	}
}
