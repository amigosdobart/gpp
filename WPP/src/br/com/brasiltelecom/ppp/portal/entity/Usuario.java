package br.com.brasiltelecom.ppp.portal.entity;

//import br.com.brasiltelecom.entity.base.PersistentAdapter;
//import br.com.brasiltelecom.autenticacao.ILogin;
//import br.com.brasiltelecom.autenticacao.IGrupo;
import br.com.brasiltelecom.ppp.session.util.Util;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade responsável por obter e definir os dados pertinentes aos usuários da BrasilTelecom e Externos.
 * 
 */

public class Usuario { //extends PersistentAdapter implements ILogin {
   /** Matricula do usuário.*/	
   private String matricula;
   
   /** Nome do usuário. **/
   private String nome;
   
   /** Filial do usuário. */
// private String filial;

   /** Filiais viculadas ao usuário. */	
   private Set filiais; 
   
   /** Cargo do usuário, seja ele da BrasilTelecom ou externo. */  
   private String cargo;
   
   /** Telefone do usuário. */
   private String telefone;
   
   /** Endereço de email do usuário. */
   private String email;
   
   /** Grupo em que o usuário está inserido. */
   private Vector grupos;
   
   /** Fornecedores em que este usuário externo está vinculado. */
   private Set fornecedores;
   
   /** Departamento do usuário.*/
   private String departamento;
   
   /** Endereço do usuário. */
   private String endereco;
   
   /** Data do ultimo acesso do usuário no sistema. */
   private Date ultimoAcesso;
   
   /** Número de tentativas no login. Três tentativas erradas, o usuário fica automaticamente bloqueado. */
   private int tentativas;

	/** Define a data de expiração deste usuário no sistema. */
   private Date dataValidade;
   
   /** Guarda as 5 ultimas senhas do usuário. */
   protected Vector senhas;
   
   /** CPF do usuário. Será usado para compor a matrícula do mesmo.*/
   private String cpf;             
	
   /** Número do contrato do fornecedor na BrasilTelecom. */
   private String contrato;             
   
   /** Gerente ou coordenador responsável pelo usuário. */
   private String gerente;              
   
   /** Filial na qual o Gerente pertence. */
   private String filialGerente;       
   
   /** Telefone do Gerente. */
   private String telefoneGerente;     
   
   /** Empresa do usuário. */
   private String empresa; 
   
   
   /** Senha do usuario
    * Solucao de contigencia para o caso do 
    * iChain nao funcionar
    *
    */
                
   private String senha;
   
    /** Existem dois tipos de usuários. <br>
    *  <ul>
    *     <li><b> Usuário Interno </b> - São os funcionários da BrT.</li>
    *     <li><b> Usuário Externo </b> - São os Fornecedores da BrT. </li>
    *  </ul>
    */
   private String tipoUsuario;         

   /**
    * Construtor padrão da classe. 
    */
   public Usuario() {
   	
   		grupos = new Vector();
   		senhas = new Vector();
  
   }
   
     
   /**
    * Método de acesso responsável por obter o nome do usuário.
    * 
    * @return  nome String com o nome do usuário.
    */
   public String getNome() {

      return nome;
   }
   
   /**
    * Método de acesso responsável por definir o Nome do usuário.
    * 
    * @param aNome String com o Nome a ser definido para o usuário.
    */
   public void setNome(String aNome) {
      nome = aNome;
   }
   
  
   /**
    * Access method for the filial property.
    * 
    * @return   the current value of the filial property

   public String getFilial() 
   {
      return filial;
   }
    */
      
   /**
    * Sets the value of the filial property.
    * 
    * @param aFilial the new value of the filial property

   public void setFilial(String aFilial) 
   {
      filial = aFilial;
   }
    */
   
   
   /**
    * Método de acesso responsável por obter o Cargo do usuário.
    * 
    * @return  cargo String com o Cargo do usuário.
    */
   public String getCargo() {
      return cargo;
   }
   
   /**
    * Método de acesso responsável por definir o Cargo do usuário.
    * 
    * @param aCargo String com o Cargo a ser definido para o usuário.
    */
   public void setCargo(String aCargo) {
      cargo = aCargo;
   }
   
   /**
    * Método de acesso responsável por obter o Telefone do usuário.
    * 
    * @return  telefone String com o Telefone do usuário.
    */
   public String getTelefone() {
      return telefone;
   }
   
   /**
    * Método de acesso responsável por definir o Telefone do usuário.
    * 
    * @param aTelefone String com o Telefone a ser definido para o usuário.
    */
   public void setTelefone(String aTelefone) {
      telefone = aTelefone;
   }
   
     
   /**
    * Método de acesso responsável por obter o Email do usuário.
    * 
    * @return  email String com o Email do usuário.
    */
   public String getEmail() {
      return email;
   }
   
   /**
    * Método de acesso responsável por definir o Email do usuário.
    * 
    * @param aEmail String com o Email a ser definido para o usuário.
    */
   public void setEmail(String aEmail) {
      email = aEmail;
   }
   
   /**
    * Método de acesso responsável por obter os Grupos em que o usuário pertence.
    * 
    * @return  grupos Collection com os Grupos do usuário.
    */
   public Collection getGrupos() {
       return grupos;
   }
   
   /**
    * Método de acesso responsável por definir os Grupos em que o usuário está vinculado.
    * 
    * @param aGrupos Collection com os Grupos a serem definidos para o usuário.
    */
   public void setGrupos(Collection aGrupos) {
      grupos = new Vector(aGrupos);
   }
   
   /**
    * Método de acesso responsável por obter o Login do usuário.
    * 
    * @return  login String com o Login do usuário.
    */
   public String getLogin() {
    return null;
   }
   
	/**
    * Método de acesso responsável por obter o Número de Tentativas de acesso do usuário.
    * 
    * @return  tentativas Número de Tentativas de acesso do usuário.
    */
	public int getTentativas() {
		return tentativas;
	}
	
	/**
    * Método de acesso responsável por obter a data do último acesso do usuário.
    * 
    * @return  ultimoAcesso Objeto Date com a data do último acesso do usuário.
    */
	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}
	
	/**
    * Método de acesso responsável por definir Número de Tentativas de acesso do usuário.
    * 
    * @param tentativas Número de Tentativas de acesso do usuário.
    */
	public void setTentativas(int tentativas) {
		this.tentativas = tentativas;
	}
	
	/**
    * Método de acesso responsável por definir a data do último acesso do usuário.
    * 
    * @param ultimoAcesso A data do último acesso do usuário.
    */
	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}
	
	/**
    * Método de acesso responsável por obter a Data de Validade de acesso do usuário.
    * 
    * @return dataValidade Objeto Date com a Data de Validade de acesso do usuário.
    */
	public Date getDataValidade() {
		return dataValidade;
	}
	
	/**
    * Método de acesso responsável por definir a Data de Validade de acesso do usuário.
    * 
    * @param dataValidade Data de Validade de acesso do usuário..
    */
	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}
	
	
	/**
    * Método de acesso responsável por obter a Senha Atual do usuário.
    * 
    * @return senhaAtual Objeto Senha com a Senha corrente do usuário.
    */
/*	public Senha getSenhaAtual() {
		Senha senhaAtual = null;
		for(Iterator it = senhas.iterator(); it.hasNext();){
			senhaAtual = (Senha) it.next();
			if(senhaAtual.getId() == 0){
				break;	
			} 
		}
		return  senhaAtual;
	}
*/	
	/**
    * Método de acesso responsável por obter as Operações/Tarefas que o usuário pode realizar no sistema.
    * 
    * @return result Map com as Operações/Tarefas que o usuário pode realizar no sistema.
    */
	public Map getOperacoesPermitidas(String tipo)
	{
		Map result = new HashMap();
		for(Iterator i = grupos.iterator();i.hasNext();)
		{
			Grupo gr = (Grupo) i.next();
			if (gr != null)
			{
				Collection operacoes = gr.getOperacoes();
				if(operacoes != null)
				{
					for(Iterator i2 = operacoes.iterator(); i2.hasNext();)
					{
						Operacao op = (Operacao) i2.next();
						if(tipo == null || op.getTipo().equalsIgnoreCase(tipo))
						{
							result.put(op.getNome(), op);
						}
					}
				}
			}
		}
		return result;
			
	}
	

	/**
    * Método de acesso responsável por obter as Senhas do usuário.
    * 
    * @return  senhas Collection com o histórico das 5 últimas senhas utilizadas pelo usuário.
    */
	public Collection getSenhas() {
		return senhas;
	}
	
	/**
    * Método de acesso responsável por definir as Senhas do usuário.
    * 
    * @param senhas Collection com o histórico das 5 últimas senhas utilizadas pelo usuário.
    */
	public void setSenhas(Collection senhas) {
		this.senhas = new Vector(senhas);
	}
	
	/**
    * Método de acesso responsável por obter a Matricula do usuário.
    * 
    * @return matricula String com a Matricula do usuário.
    */
	public String getMatricula() {
		return this.matricula;
	}
	
	/**
    * Método de acesso responsável por definir a Matricula do usuário.
    * 
    * @param matricula Matricula do usuário.
    */
	public void setMatricula(String matricula){
		this.matricula = matricula;	
	}
	
	/**
    * Método de acesso responsável por obter somente a Senha Atual do usuário.
    * 
    * @return Senha Atual do usuário.
    */
/*	public String getSenha() {
		return getSenhaAtual().getSenha();
	}
*/
	/**
    * Método de acesso responsável por verificar se o usuário pertence ao grupo informado.
    * 
    * @param id Identificador do Grupo.    
    * @return result Retorna True se o usuário pertence ao grupo informado.
    * 
    */
	public boolean ehDoGrupo(int id){
		boolean result = false;
		for(Iterator it = grupos.iterator(); it.hasNext();){
			Grupo grupo = (Grupo) it.next();
			if(grupo.getId() == id){
				result = true;
				break;
			}				
		}	
		return result;
	}

	/**
    * Método de acesso responsável por definir a Data do último acesso do usuário.
    * 
    * @param ultimoAcesso Data do último acesso do usuário.
    */
	public void setUltimoAcessoStr(String ultimoAcesso) {
		if(ultimoAcesso != null){
			this.ultimoAcesso = Util.stringToDate(ultimoAcesso);
		}
		else{
			this.ultimoAcesso = null;
		}
	}
	
	/**
    * Método de acesso responsável por definir a Data de Validade de acesso do usuário recebida como String.
    * 
    * @param dataValidade String com a Data de Validade de acesso do usuário.
    */

	public void setDataValidadeStr(String dataValidade) {
		if(dataValidade != null){
			this.dataValidade = Util.stringToDate(dataValidade);
		}
		else{
			this.dataValidade = null;
		}
	}
	
	/**
    * Método de acesso responsável por obter o Departamento do usuário.
    * 
    * @return departamento Departamento do usuário.
    */
	
	public String getDepartamento() {
		return departamento;
	}
	
	/**
    * Método de acesso responsável por obter o Endereço do usuário.
    * 
    * @return endereco Endereço do usuário.
    */
	
	public String getEndereco() {
		return endereco;
	}
	
   /**
    * Método de acesso responsável por obter os Fornecedores vinculados ao usuário.
    * 
    * @return fornecedores Fornecedores vinculados ao usuário.
    */

	public Set getFornecedores() {
		return fornecedores;
	}

   /**
    * Método de acesso responsável por obter as Filiais vinculadas ao usuário.
    * 
    * @return filiais Filiais vinculadas ao usuário.
    */

	public Set getFiliais() {
		return filiais;
	}

	
	/**
    * Método de acesso responsável por definir o Departamento do usuário.
    * 
    * @param departamento Departamento do usuário.
    */

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	/**
    * Método de acesso responsável por definir o Endereço do usuário.
    * 
    * @param endereco Endereço do usuário.
    */

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/**
    * Método de acesso responsável por definir os Fornecedores vinculados ao usuário.
    * 
    * @param fornecedores Fornecedores vinculados ao usuário.
    */

	public void setFornecedores(Set fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
    * Método de acesso responsável por definir as Filiais vinculadas ao usuário.
    * 
    * @param filiais Filiais vinculadas ao usuário.
    */

	public void setFiliais(Set filiais) {
		this.filiais = filiais;
	}

	
	/**
    * Método de acesso responsável por obter o Contrato do usuário.
    * 
    * @return contrato Contrato do Fornecedor da BrT.
    */

	public String getContrato() {
		return contrato;
	}
	
	/**
    * Método de acesso responsável por obter o CPF do usuário.
    * 
    * @return cpf CPF do usuário.
    */
	public String getCpf() {
		return cpf;
	}
	
	/**
    * Método de acesso responsável por obter a Empresa do usuário.
    * 
    * @return empresa Empresa do usuário.
    */

	public String getEmpresa() {
		return empresa;
	}
	
	/**
    * Método de acesso responsável por obter a Filial do Gerente do usuário.
    * 
    * @return filialGerente Filial do Gerente do usuário.
    */

	public String getFilialGerente() {
		return filialGerente;
	}
	
	/**
    * Método de acesso responsável por obter o Gerente do usuário.
    * 
    * @return gerente Gerente do usuário.
    */

	public String getGerente() {
		return gerente;
	}
	
	/**
    * Método de acesso responsável por obter o Telefone do Gerente do usuário.
    * 
    * @return telefoneGerente Telefone do Gerente do usuário.
    */

	public String getTelefoneGerente() {
		return telefoneGerente;
	}
	
	/**
    * Método de acesso responsável por definir o Contrato do usuário.
    * 
    * @param contrato Contrato do Fornecedor da BrT.
    */

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	
	/**
    * Método de acesso responsável por definir o CPF do usuário.
    * 
    * @param cpf CPF do usuário.
    */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	/**
    * Método de acesso responsável por definir a Empresa do usuário.
    * 
    * @param empresa Empresa do usuário.
    */

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	/**
    * Método de acesso responsável por definir a Filial do usuário.
    * 
    * @param filialGerente Filial do Gerente do usuário.
    */

	public void setFilialGerente(String filialGerente) {
		this.filialGerente = filialGerente;
	}
	
	/**
    * Método de acesso responsável por definir o Gerente do usuário.
    * 
    * @param gerente Gerente do usuário.
    */
	public void setGerente(String gerente) {
		this.gerente = gerente;
	}
	
	/**
    * Método de acesso responsável por definir o Telefone do Gerente do usuário.
    * 
    * @param telefoneGerente Telefone do Gerente do usuário.
    */
	public void setTelefoneGerente(String telefoneGerente) {
		this.telefoneGerente = telefoneGerente;
	}
	
	/**
	 * Returns the tipoUsuario.
	 * @return String
	 */

	public String getTipoUsuario() {
		return tipoUsuario;
	}
	
  /**
	* 
    * Método de acesso responsável por obter o Tipo de usuário.<br>
    * Existem dois tipos de usuários. <br>
    *  <ul>
    *     <li><b> Usuário Interno </b> - São os funcionários da BrT.</li>
    *     <li><b> Usuário Externo </b> - São os Fornecedores da BrT. </li>
    *  </ul>
    * 
    * @return tipoUsuario Tipo de usuário.
    */

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	/**
	 * @return Senha do usuário
	 */
	public String getSenha() {
		return senha;
	}
	
	/**
	 * @param Senha do usuário
	 */
	public void setSenha(String string) {
		senha = string;
	}
	
	/**
	 * Método de acesso responsável por obter as categorias o usuário pode realizar no sistema.
	 * 
	 * @return Set colecaoCategorias
	 */
	public Set getCategoriasPermitidas()
	{
		Set colecaoCategorias = new HashSet();
		for(Iterator i = grupos.iterator();i.hasNext();)
		{
			Grupo grupo = (Grupo) i.next();
			for (Iterator j = grupo.getCategorias().iterator();j.hasNext();)
			{
				Categoria categoria = (Categoria) j.next();
				colecaoCategorias.add(categoria);
			}
		}
		return colecaoCategorias;
	}
	
}
