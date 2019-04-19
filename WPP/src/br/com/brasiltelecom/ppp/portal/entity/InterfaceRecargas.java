package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * @author EX352341
 *
 */
public interface InterfaceRecargas
{
	// Gets
	public String getMsisdnString();	
	public String getFDatOrigem();
	public String getFDatRecarga();
	public String getFDatInclusao();
	public String getValorStringPrincipal();
	public String getValorStringBonus();
	public String getValorStringSMS();
	public String getValorStringGPRS();	
	public String getTipoCreditoString();
	public String getCodLoja();
	public String getCpf();
	public String getDatContabil();
	public String getIdSistemaOrigem();
	public String getHash_cc();
	public String getIdCanal();
	public String getIdOrigem();
	public String getIdRecargas();
	public String getIdTerminal();
	public String getMsisdn();
	public String getNsuInstituicao();
	public String getNumCartao();
	public String getOperador();
	public String getStatus();
	public String getTipoCredito();
	public String getTipoRecarga();
	public String getTipoTerminal();
	public String getTipoTransacao();
	public int getDiasExpiracaoBonus();
	public int getDiasExpiracaoGPRS();
	public int getDiasExpiracaoPrincipal();
	public int getDiasExpiracaoSMS();
	public int getIdValor();
	public int getNumTentativas();
	public int getIdtErro();
	public double getSaldoFinalBonus();
	public double getSaldoFinalGPRS();
	public double getSaldoFinalPrincipal();
	public double getSaldoFinalSMS();
	public double getValorBonus();
	public double getValorGPRS();
	public double getValorPrincipal();
	public double getValorSMS();
	public Date getDatInclusao();
	public Date getDatRecarga();
	public Date getDatOrigem();
	public Date getDatTimestamp();
	public TiposCreditos getTiposCreditos();
	public Origem getOrigem();
	public SistemaOrigem getSistemaOrigem();
	public Canal getCanal();
		
	// Sets
	public void setCodLoja(String string);
	public void setCpf(String string);
	public void setDatContabil(String string);
	public void setIdSistemaOrigem(String string);
	public void setHash_cc(String string);
	public void setIdCanal(String string);
	public void setIdOrigem(String string);
	public void setIdRecargas(String string);
	public void setIdTerminal(String string);
	public void setMsisdn(String string);
	public void setNsuInstituicao(String string);
	public void setNumCartao(String string);
	public void setOperador(String string);
	public void setStatus(String string);
	public void setTipoCredito(String string);
	public void setTipoRecarga(String string);
	public void setTipoTerminal(String string);
	public void setTipoTransacao(String string);
	public void setDiasExpiracaoBonus(int i);
	public void setDiasExpiracaoGPRS(int i);
	public void setDiasExpiracaoPrincipal(int i);
	public void setDiasExpiracaoSMS(int i);
	public void setIdValor(int i);
	public void setNumTentativas(int i);
	public void setIdtErro(int i);
	public void setSaldoFinalBonus(double i);
	public void setSaldoFinalGPRS(double i);
	public void setSaldoFinalPrincipal(double i);
	public void setSaldoFinalSMS(double i);
	public void setValorBonus(double d);
	public void setValorGPRS(double d);
	public void setValorPrincipal(double d);
	public void setValorSMS(double d);
	public void setDatRecarga(Date date);
	public void setDatInclusao(Date date);
	public void setDatOrigem(Date date);
	public void setDatTimestamp(Date date);
	public void setCanal(Canal canal);
	public void setOrigem(Origem origem);
	public void setSistemaOrigem(SistemaOrigem origem);
	public void setTiposCreditos(TiposCreditos creditos);	
}
