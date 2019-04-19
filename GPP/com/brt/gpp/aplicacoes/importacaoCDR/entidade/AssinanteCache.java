package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

/**
 * Esta classe representa os dados do assinante necessarios para
 * estar disponivel em memoria para o processo de importacao de 
 * dados de CDR para as totalizacoes existentes. Apesar de jah
 * exitir uma classe de Assinante, esta realiza um trabalho de
 * otimizacao dos dados que serao disponibilizados em memoria afim
 * de melhorar a utilizacao e performance desta.
 * 
 * @author joao.lemgruber
 * Data..: 01/06/2007
 * 
 */
public class AssinanteCache
{
	private String 	msisdn;
	private byte	idPromocao;
	private byte	idPlanoPreco;
	private byte	diaEntradaFaleGratis;
    private boolean ativo;
    private String  amigosGratis;
    
	public AssinanteCache(String msisdn)
	{
		if (msisdn == null)
			throw new IllegalArgumentException();
		
		this.msisdn = msisdn;
	}

	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	public byte getIdPlanoPreco()
	{
		return idPlanoPreco;
	}

	public void setIdPlanoPreco(byte idPlanoPreco)
	{
		this.idPlanoPreco = idPlanoPreco;
	}

	public byte getIdPromocao()
	{
		return idPromocao;
	}

	public void setIdPromocao(byte idPromocao)
	{
		this.idPromocao = idPromocao;
	}
	
	public byte getDiaEntradaFaleGratis()
	{
		return diaEntradaFaleGratis;
	}

	public void setDiaEntradaFaleGratis(byte diaEntradaFaleGratis)
	{
		this.diaEntradaFaleGratis = diaEntradaFaleGratis;
	}
	
	public int hashCode()
	{
		return getMsisdn().hashCode();
	}
	
	public String toString()
	{
		return getMsisdn();
	}
    
	public String getAmigosGratis()
    {
        return amigosGratis;
    }

    public void setAmigosGratis(String amigosGratis)
    {
        this.amigosGratis = amigosGratis;
    }

    public boolean isAtivo()
    {
        return ativo;
    }

    public void setAtivo(boolean ativo)
    {
        this.ativo = ativo;
    }

    public boolean equals(Object obj)
	{
		if ( !(obj instanceof AssinanteCache))
			return false;
		
		if ( this.getMsisdn().equals(((AssinanteCache)obj).getMsisdn()) )
				return true;
		
		return false;
	}
}
