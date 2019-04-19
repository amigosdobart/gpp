package br.com.brasiltelecom.wig.entity.cadpre;

public class PessoaClarify extends Entity {
	private String msisdn;
	private String cod_retorno;
	private String descricao_cod_retorno;
	private String canal_vendas;
	private String categoria_primaria;
	private String status_acesso;
	private String indic_bloqueio;
	private String dt_nasc;
	private String tipo_pessoa;
	private String qtd_amigos_toda_hora_cel;
	private String qtd_amigos_toda_hora_fixa;
	private String qtd_bonus_todo_mes;
	private String dias_ultima_alteracao_brasil_v;
	private String dias_atualizacao_brasil_v;
	private String bloq_os;
	private String indic_mala_direta;
	public String getBloq_os() {
		return bloq_os;
	}
	public void setBloq_os(String bloq_os) {
		this.bloq_os = bloq_os;
	}
	public String getCanal_vendas() {
		return canal_vendas;
	}
	public void setCanal_vendas(String canal_vendas) {
		this.canal_vendas = canal_vendas;
	}
	public String getCategoria_primaria() {
		return categoria_primaria;
	}
	public void setCategoria_primaria(String categoria_primaria) {
		this.categoria_primaria = categoria_primaria;
	}
	public String getCod_retorno() {
		return cod_retorno;
	}
	public void setCod_retorno(String cod_retorno) {
		this.cod_retorno = cod_retorno;
	}
	public String getDescricao_cod_retorno() {
		return descricao_cod_retorno;
	}
	public void setDescricao_cod_retorno(String descricao_cod_retorno) {
		this.descricao_cod_retorno = descricao_cod_retorno;
	}
	public String getDias_atualizacao_brasil_v() {
		return dias_atualizacao_brasil_v;
	}
	public void setDias_atualizacao_brasil_v(String dias_atualizacao_brasil_v) {
		this.dias_atualizacao_brasil_v = dias_atualizacao_brasil_v;
	}
	public String getDias_ultima_alteracao_brasil_v() {
		return dias_ultima_alteracao_brasil_v;
	}
	public void setDias_ultima_alteracao_brasil_v(
			String dias_ultima_alteracao_brasil_v) {
		this.dias_ultima_alteracao_brasil_v = dias_ultima_alteracao_brasil_v;
	}
	public String getDt_nasc() {
		return dt_nasc;
	}
	public void setDt_nasc(String dt_nasc) {
		this.dt_nasc = dt_nasc;
	}
	public String getIndic_bloqueio() {
		return indic_bloqueio;
	}
	public void setIndic_bloqueio(String indic_bloqueio) {
		this.indic_bloqueio = indic_bloqueio;
	}
	public String getIndic_mala_direta() {
		return indic_mala_direta;
	}
	public void setIndic_mala_direta(String indic_mala_direta) {
		this.indic_mala_direta = indic_mala_direta;
	}
	public String getQtd_amigos_toda_hora_cel() {
		return qtd_amigos_toda_hora_cel;
	}
	public void setQtd_amigos_toda_hora_cel(String qtd_amigos_toda_hora_cel) {
		this.qtd_amigos_toda_hora_cel = qtd_amigos_toda_hora_cel;
	}
	public String getQtd_amigos_toda_hora_fixa() {
		return qtd_amigos_toda_hora_fixa;
	}
	public void setQtd_amigos_toda_hora_fixa(String qtd_amigos_toda_hora_fixa) {
		this.qtd_amigos_toda_hora_fixa = qtd_amigos_toda_hora_fixa;
	}
	public String getQtd_bonus_todo_mes() {
		return qtd_bonus_todo_mes;
	}
	public void setQtd_bonus_todo_mes(String qtd_bonus_todo_mes) {
		this.qtd_bonus_todo_mes = qtd_bonus_todo_mes;
	}
	public String getStatus_acesso() {
		return status_acesso;
	}
	public void setStatus_acesso(String status_acesso) {
		this.status_acesso = status_acesso;
	}
	public String getTipo_pessoa() {
		return tipo_pessoa;
	}
	public void setTipo_pessoa(String tipo_pessoa) {
		this.tipo_pessoa = tipo_pessoa;
	}
	public void loadFromXml(String xml){
		msisdn = getTagValue("identificador_requisicao", xml);
		cod_retorno = getTagValue("cod_retorno",xml);
		descricao_cod_retorno = getTagValue("descricao_cod_retorno",xml);
		canal_vendas = getTagValue("canal_vendas",xml);
		categoria_primaria = getTagValue("categoria_primaria",xml);
		status_acesso = getTagValue("status_acesso",xml);
		indic_bloqueio = getTagValue("indic_bloqueio",xml);
		dt_nasc = getTagValue("dt_nasc",xml);
		tipo_pessoa = getTagValue("tipo_pessoa",xml);
		qtd_amigos_toda_hora_cel = getTagValue("qtd_amigos_toda_hora_cel",xml);
		qtd_amigos_toda_hora_fixa = getTagValue("qtd_amigos_toda_hora_fixa",xml);
		qtd_bonus_todo_mes = getTagValue("qtd_bonus_todo_mes",xml);
		dias_ultima_alteracao_brasil_v = getTagValue("dias_ultima_alteracao_brasil_v",xml);
		dias_atualizacao_brasil_v = getTagValue("dias_atualizacao_brasil_v",xml);
		bloq_os = getTagValue("bloq_os",xml);
		indic_mala_direta = getTagValue("indic_mala_direta",xml);
		
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
}
