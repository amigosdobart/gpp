package br.com.brasiltelecom.wig.entity;

/**
 * Esta classe define a estrutura de armazenamento para a identificacao 
 * de um servico. Apesar de um servico possuir varios conteudos associados
 * a navegacao para esse objeto nao eh realizada a partir da classe servico
 * 
 * @author Joao Carlos
 * Data..: 31/05/2005
 */
public class Servico
{
	private int 	codigoServico;
	private String 	nomeServico;
	private String	descricaoServico;
	private boolean	validaMsisdn;
	private boolean validaIccid;
	private boolean	validaModelo;
	private boolean validaImei;
	private boolean validaProfileIccid;
	private boolean validaConfigDM;
	private boolean bloqueiaConcorrente;
	
	public Servico(int codigoServico)
	{
		this.codigoServico = codigoServico; 
	}

	public boolean bloqueiaConcorrente()
	{
		return bloqueiaConcorrente;
	}
	
	public int getCodigoServico()
	{
		return codigoServico;
	}
	
	public String getDescricaoServico()
	{
		return descricaoServico;
	}
	
	public String getNomeServico()
	{
		return nomeServico;
	}
	
	public boolean validaConfigDM()
	{
		return validaConfigDM;
	}
	
	public boolean validaIccid()
	{
		return validaIccid;
	}
	
	public boolean validaImei()
	{
		return validaImei;
	}
	
	public boolean validaModelo()
	{
		return validaModelo;
	}
	
	public boolean validaMsisdn()
	{
		return validaMsisdn;
	}
	
	public boolean validaProfileIccid()
	{
		return validaProfileIccid;
	}
	
	public boolean deveValidar()
	{
		return (validaConfigDM() || validaIccid() || validaImei() || validaModelo() || validaMsisdn() || validaProfileIccid());
	}
	
	public void setBloqueiaConcorrente(boolean bloqueiaConcorrente)
	{
		this.bloqueiaConcorrente = bloqueiaConcorrente;
	}
	
	public void setCodigoServico(int codigoServico)
	{
		this.codigoServico = codigoServico;
	}
	
	public void setDescricaoServico(String descricaoServico)
	{
		this.descricaoServico = descricaoServico;
	}
	
	public void setNomeServico(String nomeServico)
	{
		this.nomeServico = nomeServico;
	}
	
	public void setValidaConfigDM(boolean validaConfigDM)
	{
		this.validaConfigDM = validaConfigDM;
	}
	
	public void setValidaIccid(boolean validaIccid)
	{
		this.validaIccid = validaIccid;
	}
	
	public void setValidaImei(boolean validaImei)
	{
		this.validaImei = validaImei;
	}
	
	public void setValidaModelo(boolean validaModelo)
	{
		this.validaModelo = validaModelo;
	}
	
	public void setValidaMsisdn(boolean validaMsisdn)
	{
		this.validaMsisdn = validaMsisdn;
	}
	
	public void setValidaProfileIccid(boolean validaProfileIccid)
	{
		this.validaProfileIccid = validaProfileIccid;
	}
	
	public int hashCode()
	{
		return this.getCodigoServico();
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Servico))
			return false;
		
		if ( ((Servico)obj).getCodigoServico() == this.getCodigoServico() )
			return true;
		return false;
	}
	
	public String toString()
	{
		return this.getNomeServico();
	}
}
