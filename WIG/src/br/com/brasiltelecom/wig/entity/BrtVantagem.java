package br.com.brasiltelecom.wig.entity;

import java.util.Collection;
import java.util.Iterator;

import br.com.brasiltelecom.wig.util.Definicoes;

/**
 * @author JOAO PAULO GALVAGNI
 * @since 03/04/2006
 * 
 */
public class BrtVantagem 
{
	private String msisdn;
	private String codRetorno;
	private String plano;
	private boolean isAtivo;
	private int qtdeAmigosTodaHora;
	private int qtdeAmigosTodaHoraCelular;
	private int qtdeAmigosTodaHoraFixo;
	private int diasAtualizacaoAmigosTodaHora;
	private int diasUltAlteracaoAmigosTodaHora;
	private int diasAtualizacaoBonusTodoMes;
	private int diasUltAlteracaoBonusTodoMes;
	private Collection amigosTodaHoraCelular;
	private Collection amigosTodaHoraFixo;
	private String msisdnBonusTodoMes;
	
	// Novissimo
	private boolean efetuaCobrancaATH;
	private boolean isNovissimoATH;
	private boolean efetuaCobrancaBTM;
	private boolean isNovissimoBTM;
	private boolean isBumerangueAtivo;
	private String  codServicoATH;
	private String  codServicoBTM;
	private String  codServicoB14;
	
	/**
	 * @return Returns the efetuaCobrancaATH.
	 */
	public boolean efetuaCobrancaATH()
	{
		return efetuaCobrancaATH;
	}

	/**
	 * @return Returns the efetuaCobrancaBTM.
	 */
	public boolean efetuaCobrancaBTM()
	{
		return efetuaCobrancaBTM;
	}

	/**
	 * @return Returns the isBumerangueAtivo.
	 */
	public boolean isBumerangueAtivo()
	{
		return isBumerangueAtivo;
	}
	
	/**
	 * @param efetuaCobrancaATH The efetuaCobrancaATH to set.
	 */
	public void setEfetuaCobrancaATH(String efetuaCobrancaATH)
	{
		this.efetuaCobrancaATH = false;
		if ("S".equalsIgnoreCase(efetuaCobrancaATH))
			this.efetuaCobrancaATH = true;
	}

	/**
	 * @param efetuaCobrancaBTM The efetuaCobrancaBTM to set.
	 */
	public void setEfetuaCobrancaBTM(String efetuaCobrancaBTM)
	{
		this.efetuaCobrancaBTM = false;
		if ("S".equalsIgnoreCase(efetuaCobrancaBTM))
			this.efetuaCobrancaBTM = true;
	}

	/**
	 * @param isAtivo The isAtivo to set.
	 */
	public void setAtivo(boolean isAtivo)
	{
		this.isAtivo = isAtivo;
	}

	/**
	 * @param isBumerangueAtivo The isBumerangueAtivo to set.
	 */
	public void setBumerangueAtivo(String isBumerangueAtivo)
	{
		this.isBumerangueAtivo = false;
		if ("S".equalsIgnoreCase(isBumerangueAtivo))
			this.isBumerangueAtivo = true;
	}
	
	/**
	 * @return Returns the isAtivo.
	 */
	public boolean isAtivo()
	{
		return isAtivo;
	}

	/**
	 * @param isAtivo The isAtivo to set.
	 */
	public void setAtivo(String isAtivoClarify)
	{
		this.isAtivo = false;
		if (Definicoes.WIG_STATUS_ATIVO_CLARIFY.equalsIgnoreCase(isAtivoClarify))
			this.isAtivo = true;
	}

	public Collection getAmigosTodaHora() 
	{
		return null;
	}
	 
	public String getNumeroBonusTodoMes() 
	{
		return null;
	}

	public Collection getAmigosTodaHoraCelular() 
	{
		return amigosTodaHoraCelular;
	}

	public void setAmigosTodaHoraCelular(Collection amigosTodaHoraCelular) 
	{
		this.amigosTodaHoraCelular = amigosTodaHoraCelular;
	}

	public Collection getAmigosTodaHoraFixo() 
	{
		return amigosTodaHoraFixo;
	}

	public void setAmigosTodaHoraFixo(Collection amigosTodaHoraFixo) 
	{
		this.amigosTodaHoraFixo = amigosTodaHoraFixo;
	}

	public String getMsisdn() 
	{
		return msisdn;
	}

	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}

	public String getMsisdnBonusTodoMes() 
	{
		return msisdnBonusTodoMes;
	}

	public void setMsisdnBonusTodoMes(String msisdnBonusTodoMes) 
	{
		this.msisdnBonusTodoMes = msisdnBonusTodoMes;
	}

	public int getQtdeAmigosTodaHoraCelular() 
	{
		return qtdeAmigosTodaHoraCelular;
	}

	public void setQtdeAmigosTodaHoraCelular(int qtdeAmigosTodaHoraCelular) 
	{
		this.qtdeAmigosTodaHoraCelular = qtdeAmigosTodaHoraCelular;
	}

	public int getQtdeAmigosTodaHoraFixo() 
	{
		return qtdeAmigosTodaHoraFixo;
	}

	public void setQtdeAmigosTodaHoraFixo(int qtdeAmigosTodaHoraFixo) 
	{
		this.qtdeAmigosTodaHoraFixo = qtdeAmigosTodaHoraFixo;
	}

	public int getQtdeAmigosTodaHora() 
	{
		return qtdeAmigosTodaHora;
	}

	public void setQtdeAmigosTodaHora(int qtdeAmigosTodaHora) 
	{
		this.qtdeAmigosTodaHora = qtdeAmigosTodaHora;
	}

	public String getCodRetorno() 
	{
		return codRetorno;
	}

	public void setCodRetorno(String codRetorno) 
	{
		this.codRetorno = codRetorno;
	}

	public int getDiasAtualizacaoAmigosTodaHora() 
	{
		return diasAtualizacaoAmigosTodaHora;
	}

	public void setDiasAtualizacaoAmigosTodaHora(int diasAtualizacaoAmigosTodaHora) 
	{
		this.diasAtualizacaoAmigosTodaHora = diasAtualizacaoAmigosTodaHora;
	}

	public int getDiasUltAlteracaoAmigosTodaHora() 
	{
		return diasUltAlteracaoAmigosTodaHora;
	}

	public void setDiasUltAlteracaoAmigosTodaHora(int diasAlteracaoAmigosTodaHora) 
	{
		this.diasUltAlteracaoAmigosTodaHora = diasAlteracaoAmigosTodaHora;
	}
	
	public int getDiasAtualizacaoBonusTodoMes() 
	{
		return diasAtualizacaoBonusTodoMes;
	}

	public void setDiasAtualizacaoBonusTodoMes(int diasAtualizacaoBonusTodoMes) 
	{
		this.diasAtualizacaoBonusTodoMes = diasAtualizacaoBonusTodoMes;
	}
	
	public int getDiasUltAlteracaoBonusTodoMes() 
	{
		return diasUltAlteracaoBonusTodoMes;
	}

	public void setDiasUltAlteracaoBonusTodoMes(int diasAlteracaoBonusTodoMes) 
	{
		this.diasUltAlteracaoBonusTodoMes = diasAlteracaoBonusTodoMes;
	}
	
	public String getNumerosATHCelular()
	{
		StringBuffer celulares = new StringBuffer();
		for (Iterator i=getAmigosTodaHoraCelular().iterator(); i.hasNext();)
		{
			celulares.append((String)i.next()+";");
		}
		return celulares.toString();
	}
	
	public String getNumerosATHFixo()
	{
		StringBuffer fixos = new StringBuffer();
		for (Iterator i=getAmigosTodaHoraFixo().iterator(); i.hasNext();)
		{
			fixos.append((String)i.next()+";");
		}
		return fixos.toString();
	}

	/**
	 * @return Returns the isNovissimoATH.
	 */
	public boolean isNovissimoATH()
	{
		return isNovissimoATH;
	}
	
	/**
	 * @param isNovissimoATH The isNovissimoATH to set.
	 */
	public void setNovissimoATH(String isNovissimoATH)
	{
		this.isNovissimoATH = false;
		if (Definicoes.WIG_ELM_NOVISSIMO_ATH.equalsIgnoreCase(isNovissimoATH))
		{
			this.isNovissimoATH = true;
			this.setCodServicoATH(isNovissimoATH);
		}
	}

	/**
	 * @return Returns the isNovissimoBTM.
	 */
	public boolean isNovissimoBTM()
	{
		return isNovissimoBTM;
	}

	/**
	 * @param isNovissimoBTM The isNovissimoBTM to set.
	 */
	public void setNovissimoBTM(String isNovissimoBTM)
	{
		this.isNovissimoBTM = false;
		if (Definicoes.WIG_ELM_NOVISSIMO_BTM.equalsIgnoreCase(isNovissimoBTM))
		{
			this.isNovissimoBTM = true;
			this.setCodServicoBTM(isNovissimoBTM);
		}
	}

	/**
	 * @return Returns the plano.
	 */
	public String getPlano()
	{
		return plano;
	}

	/**
	 * @param plano The plano to set.
	 */
	public void setPlano(String plano)
	{
		this.plano = plano;
	}

	/**
	 * @return Returns the codServicoATH.
	 */
	public String getCodServicoATH()
	{
		return codServicoATH;
	}

	/**
	 * @return Returns the codServicoBTM.
	 */
	public String getCodServicoBTM()
	{
		return codServicoBTM;
	}

	/**
	 * @param codServicoATH The codServicoATH to set.
	 */
	public void setCodServicoATH(String codServicoATH)
	{
		this.codServicoATH = codServicoATH;
	}

	/**
	 * @param codServicoBTM The codServicoBTM to set.
	 */
	public void setCodServicoBTM(String codServicoBTM)
	{
		this.codServicoBTM = codServicoBTM;
	}

	/**
	 * @return Returns the codServicoB14.
	 */
	public String getCodServicoB14()
	{
		return codServicoB14;
	}

	/**
	 * @param codServicoB14 The codServicoB14 to set.
	 */
	public void setCodServicoB14(String codServicoB14)
	{
		this.codServicoB14 = codServicoB14;
	}
}
 
