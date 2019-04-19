package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
//import br.com.brasiltelecom.entity.base.PersistentAdapter;


/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade responsável pela obtenção e definição dos dados necessários 
 *  para gerar o controle sobre quem realizou uma operação no sistema.
 * 
 */

public class LogOperacao  {
	
   /** Identificador único da Entidade LogOperacao*/ 	
   private long id;
   
   /** Login do usuário responsável pela operação que foi realizada no sistema. */
   private String usuario;

   /** Objeto do tipo Operação. */	   
   private Operacao operacao;
   
   /** Objeto Date, representa a data atual ao gerar o log da operação*/
   private Date data = new Date();
   
   /** Fornecedor que realizou a operação no sistema. */
   private String fornecedor;
   
   /** Identificador da Nota Fiscal ou Fatura que recebeu a ação. */
   private String documento;
   
   /** CNPJ do fornecedor que realizou a operação no sistema. */
   private String cnpj;
   
   /** Mensagem do Log. */
   private String mensagem;
   
   /** IP da máquina que efetuou uma operação no sistema. */
   private String ip;
   
   
   /**
    * Construtor responsável por inicializar os dados mínimos e necessários que compõem o Log da Operação.
    */
   public LogOperacao(String usuario, Operacao op,  String mensagem, String ip){
   		this.usuario = usuario;
   		this.operacao = op;
   		this.mensagem = mensagem;	
   		this.ip = ip;
   }

   /**
    * Construtor responsável por inicializar os dados de Log da Operação realizada pelo Fornecedor.
    */
   public LogOperacao(String usuario, Operacao op, String fornecedor, String documento, String cnpj, String mensagem, String ip){
   		this.usuario = usuario;
   		this.operacao = op;
   		this.fornecedor = fornecedor;
   		this.documento = documento;
   		this.cnpj = cnpj;
   		this.mensagem = mensagem;	
   		this.ip = ip;	
   }
   
   /** Construtor padrão da classe. */
   public LogOperacao() {
    
   }
   
   
   /**
    * Método responsável por obter o identificador do Log da Operação.
	* @return id Identificador único do Log Operação.
	*/
   public long getId() {
		return id;
   }

  /**
   * Método responsável por definir o Id da Operação.
   * @param id Id da Operação.
   */
	
   public void setId(long id) {
	this.id = id;
   }
   
   /**
    * Método responsável por obter o login do usuário que efetuou a operação.
	* @return usuario String com o login do usuário..
	*/

   public String getUsuario() {
      return usuario;
   }
   
  /**
   * Método responsável por definir o login do usuário.
   * @param aUsuario Identificador do usuário.
   */

   public void setUsuario(String aUsuario) {
      usuario = aUsuario;
   }
  
   /**
    * Método responsável por obter a operação do sistema.
	* @return operacao Operação do sistema.
	*/

   public Operacao getOperacao() {
      return operacao;
   }

  /**
   * Método responsável por definir a Operação efetuada no sistema.
   * @param aOperacao Operação do Sistema.
   */
   
   public void setOperacao(Operacao aOperacao) {
      operacao = aOperacao;
   }


   /**
    * Método responsável por obter data quando a operação foi efetuada no sistema.
	* @return data Data atual do sistema.
	*/
   
   public Date getData() {
      return data;
   }
   

  /**
   * Método responsável por definir a Data atual do sistema.
   * @param aData Data Atual do Sistema.
   */

   public void setData(Date aData) {
      data = aData;
   }
   
   /**
    * Método responsável por obter o Fornecedor.
	* @return fornecedor Fornecedor da BrT.
	*/
   public String getFornecedor() {
      return fornecedor;
   }
   
  /**
   * Método responsável por definir o Fornecedor.
   * @param aFornecedor Fornecedor da BrT.
   */
   public void setFornecedor(String aFornecedor) {
      fornecedor = aFornecedor;
   }
   
   /**
    * Método responsável por obter o identificador do Documento(N.F e Fatura)
	* @return documento Identificador único para Nota Fiscal e Fatura.
	*/
   public String getDocumento() {
      return documento;
   }
   
  /**
   * Método responsável por definir o identificador do documento(N.F e Fatura).
   * @param aDocumento Identificador do documento.
   */
   public void setDocumento(String aDocumento) 
   {
      documento = aDocumento;
   }
   
   /**
    * Método responsável por obter o CNPJ da N.F ou Fatura.
	* @return cnpj CNPJ do Fornecedor para N.F ou Fatura.
	*/

   public String getCnpj() {
      return cnpj;
   }

  /**
   * Método responsável por definir o CNPJ do Fornecedor da BrT.
   * @param aCnpj CNPJ do Fornecedor.
   */

   public void setCnpj(String aCnpj) {
       cnpj = aCnpj;
   }

   /**
    * Método responsável por obter o CNPJ da N.F ou Fatura.
	* @return cnpj CNPJ do Fornecedor para N.F ou Fatura.
	*/
   public String getMensagem() {
	   return mensagem;
   }

  /**
   * Método responsável por definir a mensagem do Log.
   * @param mensagem Mensagem do Log.
   */
   public void setMensagem(String mensagem) {
	   this.mensagem = mensagem;
   }
   
   /**
    * Método responsável por obter o IP da máquina cliente que gerou a requisição WEB.
	* @return ip IP da Máquina.
	*/

   public String getIp() {
	   return ip;
   }

  /**
   * Método responsável por definir a o IP da máquina que efetuou uma operação no sistema.
   * @param ip Endereço de IP da máquina.
   */
   public void setIp(String ip) {
	   this.ip = ip;
   }

}
