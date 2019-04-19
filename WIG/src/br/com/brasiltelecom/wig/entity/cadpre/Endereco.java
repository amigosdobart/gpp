package br.com.brasiltelecom.wig.entity.cadpre;

public class Endereco extends Entity{
		private String numero_cep;
		private String nome_logradouro;
		private String tipo_logradouro;
		private String descricao_tipo_logradouro;
		private String nome_bairro;
		private String sigla_cnl;
		private String sigla_uf;
		private String sigla_municipio;
		private String cod_num_localidade;
		private String nome_localidade;
		private String cod_logradouro;
		private int cod_retorno;
		public String getCod_logradouro() {
			return cod_logradouro;
		}
		public void setCod_logradouro(String cod_logradouro) {
			this.cod_logradouro = cod_logradouro;
		}
		public String getCod_num_localidade() {
			return cod_num_localidade;
		}
		public void setCod_num_localidade(String cod_num_localidade) {
			this.cod_num_localidade = cod_num_localidade;
		}
		public int getCod_retorno() {
			return cod_retorno;
		}
		public void setCod_retorno(int cod_retorno) {
			this.cod_retorno = cod_retorno;
		}
		public String getDescricao_tipo_logradouro() {
			return descricao_tipo_logradouro;
		}
		public void setDescricao_tipo_logradouro(String descricao_tipo_logradouro) {
			this.descricao_tipo_logradouro = descricao_tipo_logradouro;
		}
		public String getNome_bairro() {
			return nome_bairro;
		}
		public void setNome_bairro(String nome_bairro) {
			this.nome_bairro = nome_bairro;
		}
		public String getNome_localidade() {
			return nome_localidade;
		}
		public void setNome_localidade(String nome_localidade) {
			this.nome_localidade = nome_localidade;
		}
		public String getNome_logradouro() {
			return nome_logradouro;
		}
		public void setNome_logradouro(String nome_logradouro) {
			this.nome_logradouro = nome_logradouro;
		}
		public String getNumero_cep() {
			return numero_cep;
		}
		public void setNumero_cep(String numero_cep) {
			this.numero_cep = numero_cep;
		}
		public String getSigla_cnl() {
			return sigla_cnl;
		}
		public void setSigla_cnl(String sigla_cnl) {
			this.sigla_cnl = sigla_cnl;
		}
		public String getSigla_municipio() {
			return sigla_municipio;
		}
		public void setSigla_municipio(String sigla_municipio) {
			this.sigla_municipio = sigla_municipio;
		}
		public String getSigla_uf() {
			return sigla_uf;
		}
		public void setSigla_uf(String sigla_uf) {
			this.sigla_uf = sigla_uf;
		}
		public String getTipo_logradouro() {
			return tipo_logradouro;
		}
		public void setTipo_logradouro(String tipo_logradouro) {
			this.tipo_logradouro = tipo_logradouro;
		}
		public void loadFromXml(String xml){
			numero_cep               	= getTagValue("numero_cep", xml); 
			nome_logradouro          	= getTagValue("nome_logradouro", xml); 
			tipo_logradouro          	= getTagValue("tipo_logradouro", xml); 
			descricao_tipo_logradouro	= getTagValue("descricao_tipo_logradouro", xml); 
			nome_bairro              	= getTagValue("nome_bairro", xml); 
			sigla_cnl                	= getTagValue("sigla_cnl", xml); 
			sigla_uf                 	= getTagValue("sigla_uf", xml); 
			sigla_municipio          	= getTagValue("sigla_municipio", xml); 
			cod_num_localidade       	= getTagValue("cod_num_localidade", xml); 
			nome_localidade          	= getTagValue("nome_localidade", xml); 
			cod_logradouro           	= getTagValue("cod_logradouro", xml); 
			String cretorno = getTagValue("cod_retorno", xml); 
			cod_retorno              	= 	cretorno == null?0:Integer.parseInt(cretorno);
			
		}

}
