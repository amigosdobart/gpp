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
 *  Entidade respons�vel por obter e definir os dados pertinentes aos usu�rios da BrasilTelecom e Externos.
 * 
 */

public class Usuario { //extends PersistentAdapter implements ILogin {
   /** Matricula do usu�rio.*/	
   private String matricula;
   
   /** Nome do usu�rio. **/
   private String nome;
   
   /** Filial do usu�rio. */
// private String filial;

   /** Filiais viculadas ao usu�rio. */	
   private Set filiais; 
   
   /** Cargo do usu�rio, seja ele da BrasilTelecom ou externo. */  
   private String cargo;
   
   /** Telefone do usu�rio. */
   private String telefone;
   
   /** Endere�o de email do usu�rio. */
   private String email;
   
   /** Grupo em que o usu�rio est� inserido. */
   private Vector grupos;
   
   /** Fornecedores em que este usu�rio externo est� vinculado. */
   private Set fornecedores;
   
   /** Departamento do usu�rio.*/
   private String departamento;
   
   /** Endere�o do usu�rio. */
   private String endereco;
   
   /** Data do ultimo acesso do usu�rio no sistema. */
   private Date ultimoAcesso;
   
   /** N�mero de tentativas no login. Tr�s tentativas erradas, o usu�rio fica automaticamente bloqueado. */
   private int tentativas;

	/** Define a data de expira��o deste usu�rio no sistema. */
   private Date dataValidade;
   
   /** Guarda as 5 ultimas senhas do usu�rio. */
   protected Vector senhas;
   
   /** CPF do usu�rio. Ser� usado para compor a matr�cula do mesmo.*/
   private String cpf;             
	
   /** N�mero do contrato do fornecedor na BrasilTelecom. */
   private String contrato;             
   
   /** Gerente ou coordenador respons�vel pelo usu�rio. */
   private String gerente;              
   
   /** Filial na qual o Gerente pertence. */
   private String filialGerente;       
   
   /** Telefone do Gerente. */
   private String telefoneGerente;     
   
   /** Empresa do usu�rio. */
   private String empresa; 
   
   
   /** Senha do usuario
    * Solucao de contigencia para o caso do 
    * iChain nao funcionar
    *
    */
                
   private String senha;
   
    /** Existem dois tipos de usu�rios. <br>
    *  <ul>
    *     <li><b> Usu�rio Interno </b> - S�o os funcion�rios da BrT.</li>
    *     <li><b> Usu�rio Externo </b> - S�o os Fornecedores da BrT. </li>
    *  </ul>
    */
   private String tipoUsuario;         

   /**
    * Construtor padr�o da classe. 
    */
   public Usuario() {
   	
   		grupos = new Vector();
   		senhas = new Vector();
  
   }
   
     
   /**
    * M�todo de acesso respons�vel por obter o nome do usu�rio.
    * 
    * @return  nome String com o nome do usu�rio.
    */
   public String getNome() {

      return nome;
   }
   
   /**
    * M�todo de acesso respons�vel por definir o Nome do usu�rio.
    * 
    * @param aNome String com o Nome a ser definido para o usu�rio.
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
    * M�todo de acesso respons�vel por obter o Cargo do usu�rio.
    * 
    * @return  cargo String com o Cargo do usu�rio.
    */
   public String getCargo() {
      return cargo;
   }
   
   /**
    * M�todo de acesso respons�vel por definir o Cargo do usu�rio.
    * 
    * @param aCargo String com o Cargo a ser definido para o usu�rio.
    */
   public void setCargo(String aCargo) {
      cargo = aCargo;
   }
   
   /**
    * M�todo de acesso respons�vel por obter o Telefone do usu�rio.
    * 
    * @return  telefone String com o Telefone do usu�rio.
    */
   public String getTelefone() {
      return telefone;
   }
   
   /**
    * M�todo de acesso respons�vel por definir o Telefone do usu�rio.
    * 
    * @param aTelefone String com o Telefone a ser definido para o usu�rio.
    */
   public void setTelefone(String aTelefone) {
      telefone = aTelefone;
   }
   
     
   /**
    * M�todo de acesso respons�vel por obter o Email do usu�rio.
    * 
    * @return  email String com o Email do usu�rio.
    */
   public String getEmail() {
      return email;
   }
   
   /**
    * M�todo de acesso respons�vel por definir o Email do usu�rio.
    * 
    * @param aEmail String com o Email a ser definido para o usu�rio.
    */
   public void setEmail(String aEmail) {
      email = aEmail;
   }
   
   /**
    * M�todo de acesso respons�vel por obter os Grupos em que o usu�rio pertence.
    * 
    * @return  grupos Collection com os Grupos do usu�rio.
    */
   public Collection getGrupos() {
       return grupos;
   }
   
   /**
    * M�todo de acesso respons�vel por definir os Grupos em que o usu�rio est� vinculado.
    * 
    * @param aGrupos Collection com os Grupos a serem definidos para o usu�rio.
    */
   public void setGrupos(Collection aGrupos) {
      grupos = new Vector(aGrupos);
   }
   
   /**
    * M�todo de acesso respons�vel por obter o Login do usu�rio.
    * 
    * @return  login String com o Login do usu�rio.
    */
   public String getLogin() {
    return null;
   }
   
	/**
    * M�todo de acesso respons�vel por obter o N�mero de Tentativas de acesso do usu�rio.
    * 
    * @return  tentativas N�mero de Tentativas de acesso do usu�rio.
    */
	public int getTentativas() {
		return tentativas;
	}
	
	/**
    * M�todo de acesso respons�vel por obter a data do �ltimo acesso do usu�rio.
    * 
    * @return  ultimoAcesso Objeto Date com a data do �ltimo acesso do usu�rio.
    */
	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}
	
	/**
    * M�todo de acesso respons�vel por definir N�mero de Tentativas de acesso do usu�rio.
    * 
    * @param tentativas N�mero de Tentativas de acesso do usu�rio.
    */
	public void setTentativas(int tentativas) {
		this.tentativas = tentativas;
	}
	
	/**
    * M�todo de acesso respons�vel por definir a data do �ltimo acesso do usu�rio.
    * 
    * @param ultimoAcesso A data do �ltimo acesso do usu�rio.
    */
	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}
	
	/**
    * M�todo de acesso respons�vel por obter a Data de Validade de acesso do usu�rio.
    * 
    * @return dataValidade Objeto Date com a Data de Validade de acesso do usu�rio.
    */
	public Date getDataValidade() {
		return dataValidade;
	}
	
	/**
    * M�todo de acesso respons�vel por definir a Data de Validade de acesso do usu�rio.
    * 
    * @param dataValidade Data de Validade de acesso do usu�rio..
    */
	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}
	
	
	/**
    * M�todo de acesso respons�vel por obter a Senha Atual do usu�rio.
    * 
    * @return senhaAtual Objeto Senha com a Senha corrente do usu�rio.
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
    * M�todo de acesso respons�vel por obter as Opera��es/Tarefas que o usu�rio pode realizar no sistema.
    * 
    * @return result Map com as Opera��es/Tarefas que o usu�rio pode realizar no sistema.
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
    * M�todo de acesso respons�vel por obter as Senhas do usu�rio.
    * 
    * @return  senhas Collection com o hist�rico das 5 �ltimas senhas utilizadas pelo usu�rio.
    */
	public Collection getSenhas() {
		return senhas;
	}
	
	/**
    * M�todo de acesso respons�vel por definir as Senhas do usu�rio.
    * 
    * @param senhas Collection com o hist�rico das 5 �ltimas senhas utilizadas pelo usu�rio.
    */
	public void setSenhas(Collection senhas) {
		this.senhas = new Vector(senhas);
	}
	
	/**
    * M�todo de acesso respons�vel por obter a Matricula do usu�rio.
    * 
    * @return matricula String com a Matricula do usu�rio.
    */
	public String getMatricula() {
		return this.matricula;
	}
	
	/**
    * M�todo de acesso respons�vel por definir a Matricula do usu�rio.
    * 
    * @param matricula Matricula do usu�rio.
    */
	public void setMatricula(String matricula){
		this.matricula = matricula;	
	}
	
	/**
    * M�todo de acesso respons�vel por obter somente a Senha Atual do usu�rio.
    * 
    * @return Senha Atual do usu�rio.
    */
/*	public String getSenha() {
		return getSenhaAtual().getSenha();
	}
*/
	/**
    * M�todo de acesso respons�vel por verificar se o usu�rio pertence ao grupo informado.
    * 
    * @param id Identificador do Grupo.    
    * @return result Retorna True se o usu�rio pertence ao grupo informado.
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
    * M�todo de acesso respons�vel por definir a Data do �ltimo acesso do usu�rio.
    * 
    * @param ultimoAcesso Data do �ltimo acesso do usu�rio.
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
    * M�todo de acesso respons�vel por definir a Data de Validade de acesso do usu�rio recebida como String.
    * 
    * @param dataValidade String com a Data de Validade de acesso do usu�rio.
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
    * M�todo de acesso respons�vel por obter o Departamento do usu�rio.
    * 
    * @return departamento Departamento do usu�rio.
    */
	
	public String getDepartamento() {
		return departamento;
	}
	
	/**
    * M�todo de acesso respons�vel por obter o Endere�o do usu�rio.
    * 
    * @return endereco Endere�o do usu�rio.
    */
	
	public String getEndereco() {
		return endereco;
	}
	
   /**
    * M�todo de acesso respons�vel por obter os Fornecedores vinculados ao usu�rio.
    * 
    * @return fornecedores Fornecedores vinculados ao usu�rio.
    */

	public Set getFornecedores() {
		return fornecedores;
	}

   /**
    * M�todo de acesso respons�vel por obter as Filiais vinculadas ao usu�rio.
    * 
    * @return filiais Filiais vinculadas ao usu�rio.
    */

	public Set getFiliais() {
		return filiais;
	}

	
	/**
    * M�todo de acesso respons�vel por definir o Departamento do usu�rio.
    * 
    * @param departamento Departamento do usu�rio.
    */

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	/**
    * M�todo de acesso respons�vel por definir o Endere�o do usu�rio.
    * 
    * @param endereco Endere�o do usu�rio.
    */

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/**
    * M�todo de acesso respons�vel por definir os Fornecedores vinculados ao usu�rio.
    * 
    * @param fornecedores Fornecedores vinculados ao usu�rio.
    */

	public void setFornecedores(Set fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
    * M�todo de acesso respons�vel por definir as Filiais vinculadas ao usu�rio.
    * 
    * @param filiais Filiais vinculadas ao usu�rio.
    */

	public void setFiliais(Set filiais) {
		this.filiais = filiais;
	}

	
	/**
    * M�todo de acesso respons�vel por obter o Contrato do usu�rio.
    * 
    * @return contrato Contrato do Fornecedor da BrT.
    */

	public String getContrato() {
		return contrato;
	}
	
	/**
    * M�todo de acesso respons�vel por obter o CPF do usu�rio.
    * 
    * @return cpf CPF do usu�rio.
    */
	public String getCpf() {
		return cpf;
	}
	
	/**
    * M�todo de acesso respons�vel por obter a Empresa do usu�rio.
    * 
    * @return empresa Empresa do usu�rio.
    */

	public String getEmpresa() {
		return empresa;
	}
	
	/**
    * M�todo de acesso respons�vel por obter a Filial do Gerente do usu�rio.
    * 
    * @return filialGerente Filial do Gerente do usu�rio.
    */

	public String getFilialGerente() {
		return filialGerente;
	}
	
	/**
    * M�todo de acesso respons�vel por obter o Gerente do usu�rio.
    * 
    * @return gerente Gerente do usu�rio.
    */

	public String getGerente() {
		return gerente;
	}
	
	/**
    * M�todo de acesso respons�vel por obter o Telefone do Gerente do usu�rio.
    * 
    * @return telefoneGerente Telefone do Gerente do usu�rio.
    */

	public String getTelefoneGerente() {
		return telefoneGerente;
	}
	
	/**
    * M�todo de acesso respons�vel por definir o Contrato do usu�rio.
    * 
    * @param contrato Contrato do Fornecedor da BrT.
    */

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	
	/**
    * M�todo de acesso respons�vel por definir o CPF do usu�rio.
    * 
    * @param cpf CPF do usu�rio.
    */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	/**
    * M�todo de acesso respons�vel por definir a Empresa do usu�rio.
    * 
    * @param empresa Empresa do usu�rio.
    */

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	/**
    * M�todo de acesso respons�vel por definir a Filial do usu�rio.
    * 
    * @param filialGerente Filial do Gerente do usu�rio.
    */

	public void setFilialGerente(String filialGerente) {
		this.filialGerente = filialGerente;
	}
	
	/**
    * M�todo de acesso respons�vel por definir o Gerente do usu�rio.
    * 
    * @param gerente Gerente do usu�rio.
    */
	public void setGerente(String gerente) {
		this.gerente = gerente;
	}
	
	/**
    * M�todo de acesso respons�vel por definir o Telefone do Gerente do usu�rio.
    * 
    * @param telefoneGerente Telefone do Gerente do usu�rio.
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
    * M�todo de acesso respons�vel por obter o Tipo de usu�rio.<br>
    * Existem dois tipos de usu�rios. <br>
    *  <ul>
    *     <li><b> Usu�rio Interno </b> - S�o os funcion�rios da BrT.</li>
    *     <li><b> Usu�rio Externo </b> - S�o os Fornecedores da BrT. </li>
    *  </ul>
    * 
    * @return tipoUsuario Tipo de usu�rio.
    */

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	/**
	 * @return Senha do usu�rio
	 */
	public String getSenha() {
		return senha;
	}
	
	/**
	 * @param Senha do usu�rio
	 */
	public void setSenha(String string) {
		senha = string;
	}
	
	/**
	 * M�todo de acesso respons�vel por obter as categorias o usu�rio pode realizar no sistema.
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
