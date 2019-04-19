package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

public class ContingenciaCrm {

	private long idAtividade;
	private String idOperacao;
	private String msisdn;
	private Date datAtividade;
	private String idAtendente;
	private BloqueioStatus idStatus;
	
	// Observacao: Os dados cadastrais e dados do BO nao serao populados automaticamente
	// pelo CASTOR. Portanto na fabricacao do objeto (HOME) este deve popular os dados
	// correspondentes desta atividade
	private DadosCadastraisCrm	dadosCadastrais;
	private DadosBoCrm			dadosBo;
	private TipoBloqueioCrm		motivoBloqueio;
	
	private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String retornoOp;
	private String idStatusProc;
	private PlanoPreco idPlanoPreco;
		

	/**
	 * @return Returns the idStatusP.
	 */
	public String getIdStatusProc() {
		if (idStatusProc!=null && idStatusProc.equalsIgnoreCase("ok"))
			return "Ok";
		else
			if (idStatusProc!=null && idStatusProc.equalsIgnoreCase("nok"))
				return "Erro";
			else
				return "";
	}
	/**
	 * @param idStatusP The idStatusP to set.
	 */
	public void setIdStatusProc(String idStatusProc) {
		this.idStatusProc = idStatusProc;
	}
	/**
	 * @return Returns the retornoOp.
	 */
	public String getRetornoOp() {
		return retornoOp;
	}
	/**
	 * @param retornoOp The retornoOp to set.
	 */
	public void setRetornoOp(String retornoOp) {
		this.retornoOp = retornoOp;
	}
	/**
	 * @return Returns the formataData.
	 */
	public SimpleDateFormat getFormataData() {
		return formataData;
	}
	/**
	 * @param formataData The formataData to set.
	 */
	public void setFormataData(SimpleDateFormat formataData) {
		this.formataData = formataData;
	}
	/**
	 * @return Returns the idStatus.
	 */
	public BloqueioStatus getIdStatus() {
		return idStatus;
	}
	/**
	 * @param idStatus The idStatus to set.
	 */
	public void setIdStatus(BloqueioStatus idStatus) {
		this.idStatus = idStatus;
	}
	/**
	 * @return Returns the datAtividade.
	 */
	public Date getDatAtividade() {
		return datAtividade;
	}
	/**
	 * @param datAtividade The datAtividade to set.
	 */
	public void setDatAtividade(Date datAtividade) {
		this.datAtividade = datAtividade;
	}
	/**
	 * @return Returns the idAtendente.
	 */
	public String getIdAtendente() {
		return idAtendente;
	}
	/**
	 * @param idAtendente The idAtendente to set.
	 */
	public void setIdAtendente(String idAtendente) {
		this.idAtendente = idAtendente;
	}
	/**
	 * @return Returns the idAtividade.
	 */
	public long getIdAtividade() {
		return idAtividade;
	}
	/**
	 * @param idAtividade The idAtividade to set.
	 */
	public void setIdAtividade(long idAtividade) {
		this.idAtividade = idAtividade;
	}
	/**
	 * @return Returns the idOperacao.
	 */
	public String getIdOperacao() {
		return idOperacao;
	}
	/**
	 * @param idOperacao The idOperacao to set.
	 */
	public void setIdOperacao(String idOperacao) {
		this.idOperacao = idOperacao;
	}
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	/**
	 * @return Returns the datAtividade
	 */
	public String getDatAtividadeString()
	{
		return formataData.format(getDatAtividade());
	}

	/**
	 * @return
	 */
	public DadosBoCrm getDadosBo()
	{
		return dadosBo;
	}

	/**
	 * @return
	 */
	public DadosCadastraisCrm getDadosCadastrais()
	{
		return dadosCadastrais;
	}

	/**
	 * @param crm
	 */
	public void setDadosBo(DadosBoCrm crm)
	{
		dadosBo = crm;
	}

	/**
	 * @param crm
	 */
	public void setDadosCadastrais(DadosCadastraisCrm crm)
	{
		dadosCadastrais = crm;
	}

	/**
	 * @return
	 */
	public TipoBloqueioCrm getMotivoBloqueio()
	{
		return motivoBloqueio;
	}

	/**
	 * @param crm
	 */
	public void setMotivoBloqueio(TipoBloqueioCrm crm)
	{
		motivoBloqueio = crm;
	}

	/**
	 * @return
	 */
	public PlanoPreco getIdPlanoPreco() {
		return idPlanoPreco;
	}

	/**
	 * @param preco
	 */
	public void setIdPlanoPreco(PlanoPreco preco) {
		idPlanoPreco = preco;
	}

}
