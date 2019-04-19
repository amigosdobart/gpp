//Source file: C:\\Arquivos de programas\\Rational\\RUPBuilder\\br\\com\\brasiltelecom\\portalNF\\entity\\acesso\\Grupo.java

package br.com.brasiltelecom.ppp.portal.entity;

//import br.com.brasiltelecom.portalNF.entity.SAP.Fornecedor;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;


/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *	Entidade Grupo. <br>
 *  Possui m�todos acessores para obten��o e defini��o dos Grupos em que os usu�rios
 *  est�o inseridos, bem como as opera��es/tarefas definidas para cada grupo. 
 */

public class Grupo 
{

   /** Identificador do Grupo */	
   private int id;
   
   /** Nome do Grupo */
   private String nome;
   
   /** Opera��es / Tarefas de um Grupo*/
   private Collection operacoes;
   
   private Collection categorias; 
   
   private Date datCadastro;
   
   private double maxValorCobranca;
   
   private String idUsuario;
   
   private int ativo;
   
   private DecimalFormat df = new DecimalFormat("#,##0.00");

	public String getMaxValorCobrancaString(){
		if (maxValorCobranca == 0){
			return "";
		} else {
			return df.format(maxValorCobranca);
		}
	}
   /**
    * Construtor padr�o da classe.
    */
   public Grupo() {
    
   }

  /**
    * M�todo respons�vel por obter o Identificador �nico do Grupo
	* @return id Inteiro com o ID do Grupo.
	*/
	
   public int getId() {
      return id;
   }
   
 /**
   * M�todo respons�vel por definir o ID do Grupo
   * @param aId Inteiro com o ID do Grupo a ser definido.
   */	
   public void setId(int aId) {
      id = aId;
   }


  /**
    * M�todo respons�vel por obter o Nome do Grupo
	* @return nome String com o Nome do Grupo.
	*/
   public String getNome() {
      return nome;
   }
   
  /**
   * M�todo respons�vel por definir o Nome do Grupo
   * @param aNome String com o Nome do Grupo a ser definido.
   */	
   public void setNome(String aNome) {
      nome = aNome;
   }
   
  /**
    * M�todo respons�vel por obter as Opera��es de um Grupo
	* @return operacoes Collection com as Opera��es de um Grupo.
	*/
  
   public Collection getOperacoes() {
      return operacoes;
   }
   
 /**
   * M�todo respons�vel por definir as Opera��es de um Grupo
   * @param aOperacoes Collection de Opera��es de um Grupo.
   */
 	
    public void setOperacoes(Collection aOperacoes) {
      operacoes = aOperacoes;
   }
	   
	/**
	 * @return
	 */
	public Date getDatCadastro() {
		return datCadastro;
	}
	
	/**
	 * @return
	 */
	public String getIdUsuario() {
		return idUsuario;
	}
	
	/**
	 * @return
	 */
	public double getMaxValorCobranca() {
		return maxValorCobranca;
	}
	
	/**
	 * @param date
	 */
	public void setDatCadastro(Date date) {
		datCadastro = date;
	}
	
	/**
	 * @param string
	 */
	public void setIdUsuario(String string) {
		idUsuario = string;
	}
	
	/**
	 * @param d
	 */
	public void setMaxValorCobranca(double d) {
		maxValorCobranca = d;
	}
	
	/**
	 * @return
	 */
	public int getAtivo() {
		return ativo;
	}
	
	/**
	 * @param i
	 */
	public void setAtivo(int i) {
		ativo = i;
	}

	public boolean equals(Object obj) {
		if(obj != null){
			if(obj instanceof Grupo){
				if(((Grupo) obj).getId() == id){
					return true; 
				}
			}
		}
		return false;
	}

	/**
	 * @return the categoria
	 */
	public Collection getCategorias()
	{
		return categorias;
	}


	/**
	 * @param categoria the categoria to set
	 */
	public void setCategorias(Collection categorias)
	{
		this.categorias = categorias;
	}
}
