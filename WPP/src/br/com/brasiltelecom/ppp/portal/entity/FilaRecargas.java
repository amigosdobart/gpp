/*
 * Created on 02/03/2005
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.sql.Timestamp;

/**
 * Modela Fila de Recargas 
 * @author Daniel Ferreira
 * @since 02/03/2005
 */
public class FilaRecargas 
{
	
	private String idtMsisdn;
	private String tipTransacao;
	private Timestamp datCadastro;
	private Date datExecucao;
	private Timestamp datProcessamento;
	private Double vlrCreditoPrincipal;
	private Double vlrCreditoBonus;
	private Double vlrCreditoSms;
	private Double vlrCreditoGprs;
	private Integer numDiasExpPrincipal;
	private Integer numDiasExpBonus;
	private Integer numDiasExpSms;
	private Integer numDiasExpGprs;
	private String desMensagem;
	private String tipSms;
	private Integer indEnviaSms;
	private Integer idtStatusProcessamento;
	private Integer idtCodigoRetorno;
	
	public String getIdtMsisdn()
	{
		return idtMsisdn;
	}

	public String getTipTransacao()
	{
		return tipTransacao;
	}

	public Timestamp getDatCadastro()
	{
		return datCadastro;
	}

	public Date getDatExecucao()
	{
		return datExecucao;
	}

	public Timestamp getDatProcessamento()
	{
		return datProcessamento;
	}
	
	public Double getVlrCreditoPrincipal()
	{
		return vlrCreditoPrincipal;
	}

	public Double getVlrCreditoBonus()
	{
		return vlrCreditoBonus;
	}

	public Double getVlrCreditoSms()
	{
		return vlrCreditoSms;
	}

	public Double getVlrCreditoGprs()
	{
		return vlrCreditoGprs;
	}

	public Integer getNumDiasExpPrincipal()
	{
		return numDiasExpPrincipal;
	}

	public Integer getNumDiasExpBonus()
	{
		return numDiasExpBonus;
	}

	public Integer getNumDiasExpSms()
	{
		return numDiasExpSms;
	}

	public Integer getNumDiasExpGprs()
	{
		return numDiasExpGprs;
	}

	public String getDesMensagem()
	{
		return desMensagem;
	}

	public String getTipSms()
	{
		return tipSms;
	}

	public Integer getIndEnviaSms()
	{
		return indEnviaSms;
	}

	public Integer getIdtStatusProcessamento()
	{
		return idtStatusProcessamento;
	}

	public Integer getIdtCodigoRetorno()
	{
		return idtCodigoRetorno;
	}

	public void setIdtMsisdn(String idtMsisdn)
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	public void setTipTransacao(String tipTransacao)
	{
		this.tipTransacao = tipTransacao;
	}

	public void setDatCadastro(Timestamp datCadastro)
	{
		this.datCadastro = datCadastro;
	}

	public void setDatExecucao(Date datExecucao)
	{
		this.datExecucao = datExecucao;
	}

	public void setDatProcessamento(Timestamp datProcessamento)
	{
		this.datProcessamento = datProcessamento;
	}
	
	public void setVlrCreditoPrincipal(Double vlrCreditoPrincipal)
	{
		this.vlrCreditoPrincipal = vlrCreditoPrincipal;
	}

	public void setVlrCreditoBonus(Double vlrCreditoBonus)
	{
		this.vlrCreditoBonus = vlrCreditoBonus;
	}

	public void setVlrCreditoSms(Double vlrCreditoSms)
	{
		this.vlrCreditoSms = vlrCreditoSms;
	}

	public void setVlrCreditoGprs(Double vlrCreditoGprs)
	{
		this.vlrCreditoGprs = vlrCreditoGprs;
	}

	public void setNumDiasExpPrincipal(Integer numDiasExpPrincipal)
	{
		this.numDiasExpPrincipal = numDiasExpPrincipal;
	}

	public void setNumDiasExpBonus(Integer numDiasExpBonus)
	{
		this.numDiasExpBonus = numDiasExpBonus;
	}

	public void setNumDiasExpSms(Integer numDiasExpSms)
	{
		this.numDiasExpSms = numDiasExpSms;
	}

	public void setNumDiasExpGprs(Integer numDiasExpGprs)
	{
		this.numDiasExpGprs = numDiasExpGprs;
	}

	public void setDesMensagem(String desMensagem)
	{
		this.desMensagem = desMensagem;
	}

	public void setTipSms(String tipSms)
	{
		this.tipSms = tipSms;
	}

	public void setIndEnviaSms(Integer indEnviaSms)
	{
		this.indEnviaSms = indEnviaSms;
	}

	public void setIdtStatusProcessamento(Integer idtStatusProcessamento)
	{
		this.idtStatusProcessamento = idtStatusProcessamento;
	}

	public void setIdtCodigoRetorno(Integer idtCodigoRetorno)
	{
		this.idtCodigoRetorno = idtCodigoRetorno;
	}

}
