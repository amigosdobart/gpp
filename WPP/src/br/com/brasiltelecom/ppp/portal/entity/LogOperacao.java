package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
//import br.com.brasiltelecom.entity.base.PersistentAdapter;


/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade respons�vel pela obten��o e defini��o dos dados necess�rios 
 *  para gerar o controle sobre quem realizou uma opera��o no sistema.
 * 
 */

public class LogOperacao  {
	
   /** Identificador �nico da Entidade LogOperacao*/ 	
   private long id;
   
   /** Login do usu�rio respons�vel pela opera��o que foi realizada no sistema. */
   private String usuario;

   /** Objeto do tipo Opera��o. */	   
   private Operacao operacao;
   
   /** Objeto Date, representa a data atual ao gerar o log da opera��o*/
   private Date data = new Date();
   
   /** Fornecedor que realizou a opera��o no sistema. */
   private String fornecedor;
   
   /** Identificador da Nota Fiscal ou Fatura que recebeu a a��o. */
   private String documento;
   
   /** CNPJ do fornecedor que realizou a opera��o no sistema. */
   private String cnpj;
   
   /** Mensagem do Log. */
   private String mensagem;
   
   /** IP da m�quina que efetuou uma opera��o no sistema. */
   private String ip;
   
   
   /**
    * Construtor respons�vel por inicializar os dados m�nimos e necess�rios que comp�em o Log da Opera��o.
    */
   public LogOperacao(String usuario, Operacao op,  String mensagem, String ip){
   		this.usuario = usuario;
   		this.operacao = op;
   		this.mensagem = mensagem;	
   		this.ip = ip;
   }

   /**
    * Construtor respons�vel por inicializar os dados de Log da Opera��o realizada pelo Fornecedor.
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
   
   /** Construtor padr�o da classe. */
   public LogOperacao() {
    
   }
   
   
   /**
    * M�todo respons�vel por obter o identificador do Log da Opera��o.
	* @return id Identificador �nico do Log Opera��o.
	*/
   public long getId() {
		return id;
   }

  /**
   * M�todo respons�vel por definir o Id da Opera��o.
   * @param id Id da Opera��o.
   */
	
   public void setId(long id) {
	this.id = id;
   }
   
   /**
    * M�todo respons�vel por obter o login do usu�rio que efetuou a opera��o.
	* @return usuario String com o login do usu�rio..
	*/

   public String getUsuario() {
      return usuario;
   }
   
  /**
   * M�todo respons�vel por definir o login do usu�rio.
   * @param aUsuario Identificador do usu�rio.
   */

   public void setUsuario(String aUsuario) {
      usuario = aUsuario;
   }
  
   /**
    * M�todo respons�vel por obter a opera��o do sistema.
	* @return operacao Opera��o do sistema.
	*/

   public Operacao getOperacao() {
      return operacao;
   }

  /**
   * M�todo respons�vel por definir a Opera��o efetuada no sistema.
   * @param aOperacao Opera��o do Sistema.
   */
   
   public void setOperacao(Operacao aOperacao) {
      operacao = aOperacao;
   }


   /**
    * M�todo respons�vel por obter data quando a opera��o foi efetuada no sistema.
	* @return data Data atual do sistema.
	*/
   
   public Date getData() {
      return data;
   }
   

  /**
   * M�todo respons�vel por definir a Data atual do sistema.
   * @param aData Data Atual do Sistema.
   */

   public void setData(Date aData) {
      data = aData;
   }
   
   /**
    * M�todo respons�vel por obter o Fornecedor.
	* @return fornecedor Fornecedor da BrT.
	*/
   public String getFornecedor() {
      return fornecedor;
   }
   
  /**
   * M�todo respons�vel por definir o Fornecedor.
   * @param aFornecedor Fornecedor da BrT.
   */
   public void setFornecedor(String aFornecedor) {
      fornecedor = aFornecedor;
   }
   
   /**
    * M�todo respons�vel por obter o identificador do Documento(N.F e Fatura)
	* @return documento Identificador �nico para Nota Fiscal e Fatura.
	*/
   public String getDocumento() {
      return documento;
   }
   
  /**
   * M�todo respons�vel por definir o identificador do documento(N.F e Fatura).
   * @param aDocumento Identificador do documento.
   */
   public void setDocumento(String aDocumento) 
   {
      documento = aDocumento;
   }
   
   /**
    * M�todo respons�vel por obter o CNPJ da N.F ou Fatura.
	* @return cnpj CNPJ do Fornecedor para N.F ou Fatura.
	*/

   public String getCnpj() {
      return cnpj;
   }

  /**
   * M�todo respons�vel por definir o CNPJ do Fornecedor da BrT.
   * @param aCnpj CNPJ do Fornecedor.
   */

   public void setCnpj(String aCnpj) {
       cnpj = aCnpj;
   }

   /**
    * M�todo respons�vel por obter o CNPJ da N.F ou Fatura.
	* @return cnpj CNPJ do Fornecedor para N.F ou Fatura.
	*/
   public String getMensagem() {
	   return mensagem;
   }

  /**
   * M�todo respons�vel por definir a mensagem do Log.
   * @param mensagem Mensagem do Log.
   */
   public void setMensagem(String mensagem) {
	   this.mensagem = mensagem;
   }
   
   /**
    * M�todo respons�vel por obter o IP da m�quina cliente que gerou a requisi��o WEB.
	* @return ip IP da M�quina.
	*/

   public String getIp() {
	   return ip;
   }

  /**
   * M�todo respons�vel por definir a o IP da m�quina que efetuou uma opera��o no sistema.
   * @param ip Endere�o de IP da m�quina.
   */
   public void setIp(String ip) {
	   this.ip = ip;
   }

}
