package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

public class RequisicaoExtrato
{
	public static final int STATUS_REQUISICAO_NAO_PROCESSADA        = 0;
	public static final int STATUS_REQUISICAO_EM_PROCESSAMENTO      = 1;
	public static final int STATUS_REQUISICAO_PROCESSADA            = 2;

	private int     idtComprovante;
	private Date    datRequisicao;
	private String  idtMsisdn;
	private Date    datPeriodoInicial;
	private Date    datPeriodoFinal;
	private Date    datGeracao;
	private int     idtStatus_processamento;
	private int     idtCodRetorno;
	private int     indCobrarExtrato;
	private int     idtFilial;
	private String  nomCliente;
	private String  desLogradouro;
	private String  desComplemento;
	private String  desFachada;
	private String  desBairro;
	private String  desCidade;
	private String  desUf;
	private String  desCep;
	private String  desEmail;
	private String  desContrato;

	public Date getDatGeracao()
	{
		return datGeracao;
	}
	public void setDatGeracao(Date datGeracao)
	{
		this.datGeracao = datGeracao;
	}
	public Date getDatPeriodoFinal()
	{
		return datPeriodoFinal;
	}
	public void setDatPeriodoFinal(Date datPeriodoFinal)
	{
		this.datPeriodoFinal = datPeriodoFinal;
	}
	public Date getDatPeriodoInicial()
	{
		return datPeriodoInicial;
	}
	public void setDatPeriodoInicial(Date datPeriodoInicial)
	{
		this.datPeriodoInicial = datPeriodoInicial;
	}
	public Date getDatRequisicao()
	{
		return datRequisicao;
	}
	public void setDatRequisicao(Date datRequisicao)
	{
		this.datRequisicao = datRequisicao;
	}
	public String getDesBairro()
	{
		return desBairro;
	}
	public void setDesBairro(String desBairro)
	{
		this.desBairro = desBairro;
	}
	public String getDesCep()
	{
		return desCep;
	}
	public void setDesCep(String desCep)
	{
		this.desCep = desCep;
	}
	public String getDesCidade()
	{
		return desCidade;
	}
	public void setDesCidade(String desCidade)
	{
		this.desCidade = desCidade;
	}
	public String getDesComplemento()
	{
		return desComplemento;
	}
	public void setDesComplemento(String desComplemento)
	{
		this.desComplemento = desComplemento;
	}
	public String getDesContrato()
	{
		return desContrato;
	}
	public void setDesContrato(String desContrato)
	{
		this.desContrato = desContrato;
	}
	public String getDesEmail()
	{
		return desEmail;
	}
	public void setDesEmail(String desEmail)
	{
		this.desEmail = desEmail;
	}
	public String getDesLogradouro()
	{
		return desLogradouro;
	}
	public void setDesLogradouro(String desLogradouro)
	{
		this.desLogradouro = desLogradouro;
	}
	public String getDesUf()
	{
		return desUf;
	}
	public void setDesUf(String desUf)
	{
		this.desUf = desUf;
	}
	public int getIdtCodRetorno()
	{
		return idtCodRetorno;
	}
	public void setIdtCodRetorno(int idtCodRetorno)
	{
		this.idtCodRetorno = idtCodRetorno;
	}
	public int getIdtComprovante()
	{
		return idtComprovante;
	}
	public void setIdtComprovante(int idtComprovante)
	{
		this.idtComprovante = idtComprovante;
	}
	public String getDesFachada()
	{
		return desFachada;
	}
	public void setDesFachada(String desFachada)
	{
		this.desFachada = desFachada;
	}
	public int getIdtFilial()
	{
		return idtFilial;
	}
	public void setIdtFilial(int idtFilial)
	{
		this.idtFilial = idtFilial;
	}
	public String getIdtMsisdn()
	{
		return idtMsisdn;
	}
	public void setIdtMsisdn(String idtMsisdn)
	{
		this.idtMsisdn = idtMsisdn;
	}
	public int getIdtStatus_processamento()
	{
		return idtStatus_processamento;
	}
	public void setIdtStatus_processamento(int idtStatus_processamento)
	{
		this.idtStatus_processamento = idtStatus_processamento;
	}
	public int getIndCobrarExtrato()
	{
		return indCobrarExtrato;
	}
	public void setIndCobrarExtrato(int indCobrarExtrato)
	{
		this.indCobrarExtrato = indCobrarExtrato;
	}
	public String getNomCliente()
	{
		return nomCliente;
	}
	public void setNomCliente(String nomCliente)
	{
		this.nomCliente = nomCliente;
	}
	public boolean isCobrarExtrato()
	{
		if(indCobrarExtrato == 1)
			return true;

		return false;
	}
}
