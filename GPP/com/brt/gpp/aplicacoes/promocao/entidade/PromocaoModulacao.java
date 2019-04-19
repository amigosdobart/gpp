package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_MODULACAO, contendo as informacoes referente a modulacao
 * 
 *	@author	Joao Carlos
 *	@since	28/05/2007
 */
public class PromocaoModulacao implements Entidade
{
	private CodigoNacional 	cn;
	private Promocao		promocao;
	private long			horaInicio;
	private long			horaFim;
    private int             indBonificaPulaPula;
	
	public CodigoNacional getCn()
	{
		return cn;
	}
	
	public void setCn(CodigoNacional cn)
	{
		this.cn = cn;
	}
	
	public long getHoraFim()
	{
		return horaFim;
	}
	
	public void setHoraFim(long horaFim)
	{
		this.horaFim = horaFim;
	}
	
	public long getHoraInicio()
	{
		return horaInicio;
	}
	
	public void setHoraInicio(long horaInicio)
	{
		this.horaInicio = horaInicio;
	}
	
	public Promocao getPromocao()
	{
		return promocao;
	}
	
	public void setPromocao(Promocao promocao)
	{
		this.promocao = promocao;
	}
	
	public String key()
	{
		return cn.getIdtCodigoNacional().toString() + promocao.getIdtPromocao();
	}
	
	public int getIndBonificaPulaPula()
    {
        return indBonificaPulaPula;
    }

    public void setIndBonificaPulaPula(int indBonificaPulaPula)
    {
        this.indBonificaPulaPula = indBonificaPulaPula;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return cn + " " + promocao;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return key().hashCode();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof PromocaoModulacao) )
			return false;
		
		PromocaoModulacao prMod = (PromocaoModulacao)obj;
		if ( this.getCn().equals(prMod.getCn()) && this.getPromocao().equals(prMod.getPromocao()) )
			return true;
		
		return false;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		PromocaoModulacao prMod = new PromocaoModulacao();
		prMod.setCn(this.getCn());
		prMod.setPromocao(this.getPromocao());
		prMod.setHoraInicio(this.getHoraInicio());
		prMod.setHoraFim(this.getHoraFim());
		
		return prMod;
	}
    
    /**
     * Verifica se o startTime (numero de segundos desde a meia-noite)
     * pertence a um intervalo de modulacao nao bonificavel para o pulapula.
     * <p>
     * Caso o startTime esteja dentro do intervalo de modulacao, a flag
     * indBonificaPulaPula eh considerada. Caso contrario (startTime fora do 
     * intervalo de bonificacao) esse metodo retorna sempre false;
     * 
     * @param startTime Numero de segundos desde a meia-noite
     * @return true Sse o startTime esta dentro de intervalo nao bonificavel
     */
    public boolean isNaoBonificavel(long startTime)
    {
        // sempre bonifica
        if (indBonificaPulaPula == 1)
            return false;
    
        // se INICIO > FIM entao o intervalo nao eh continuo 
        boolean isDentroIntervalo = (horaFim > horaInicio) ? 
                (startTime >= horaInicio && startTime < horaFim) : 
                (startTime >= horaInicio || startTime < horaFim);
        
        if (isDentroIntervalo)
        {
            return indBonificaPulaPula == 1 ? false : true;
        }
            
        // fora do intervalo de modulacao
        return false;
    }
}
