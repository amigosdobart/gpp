package br.com.brasiltelecom.wig.entity.cadpre;


public class Pessoa extends Entity{
	private String cpf;
	private String cnpj;
	private String nome;
	private String data_nasc;
	private String nome_empresa;
	private String nome_fantasia;
	private String razao_social;
	private String cnae_fiscal;
	private String sit_cadastral;
	private String status_cpf;
	private String status_cnpj;
	private int codigo_retorno;
	private String descricao_codigo_retorno;
	public String getCnae_fiscal() {
		return cnae_fiscal;
	}
	public void setCnae_fiscal(String cnae_fiscal) {
		this.cnae_fiscal = cnae_fiscal;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public int getCodigo_retorno() {
		return codigo_retorno;
	}
	public void setCodigo_retorno(int codigo_retorno) {
		this.codigo_retorno = codigo_retorno;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getData_nasc() {
		return data_nasc;
	}
	public void setData_nasc(String data_nasc) {
		this.data_nasc = data_nasc;
	}
	public String getDescricao_codigo_retorno() {
		return descricao_codigo_retorno;
	}
	public void setDescricao_codigo_retorno(String descricao_codigo_retorno) {
		this.descricao_codigo_retorno = descricao_codigo_retorno;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNome_empresa() {
		return nome_empresa;
	}
	public void setNome_empresa(String nome_empresa) {
		this.nome_empresa = nome_empresa;
	}
	public String getNome_fantasia() {
		return nome_fantasia;
	}
	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}
	public String getRazao_social() {
		return razao_social;
	}
	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}
	public String getSit_cadastral() {
		return sit_cadastral;
	}
	public void setSit_cadastral(String sit_cadastral) {
		this.sit_cadastral = sit_cadastral;
	}
	public String getStatus_cnpj() {
		return status_cnpj;
	}
	public void setStatus_cnpj(String status_cnpj) {
		this.status_cnpj = status_cnpj;
	}
	public String getStatus_cpf() {
		return status_cpf;
	}
	public void setStatus_cpf(String status_cpf) {
		this.status_cpf = status_cpf;
	}
	
	public void loadFromXml(String xml){
		
		cpf                      = getTagValue("cpf", xml);
		cnpj                     = getTagValue("cnpj", xml);
		nome                     = getTagValue("nome", xml);
		data_nasc                = getTagValue("data_nasc", xml);
		nome_empresa             = getTagValue("nome_empresa", xml);
		nome_fantasia            = getTagValue("nome_fantasia", xml);
		razao_social             = getTagValue("razao_social", xml);
		cnae_fiscal              = getTagValue("cnae_fiscal", xml);
		sit_cadastral            = getTagValue("sit_cadastral", xml);
		status_cpf               = getTagValue("status_cpf", xml);
		status_cnpj              = getTagValue("status_cnpj", xml);
		String cretorno 		 = getTagValue("codigo_retorno", xml);
		codigo_retorno           = cretorno==null?0:Integer.parseInt(cretorno);
		descricao_codigo_retorno = getTagValue("descricao_codigo_retorno", xml);
	}
	
}
