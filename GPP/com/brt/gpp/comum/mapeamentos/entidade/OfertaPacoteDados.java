package com.brt.gpp.comum.mapeamentos.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Classe responsavel por conter as informacoes da
 * tabela TBL_PRO_OFERTA_PACOTE_DADOS
 * 
 * @author João Paulo Galvagni
 * @since  31/08/2007
 */
public class OfertaPacoteDados implements Entidade, Comparable
{
    private int 		idtOferta;
    private String		desOferta;
    private TipoSaldo 	tipoSaldo;
    private PacoteDados pacoteDados;
    private Date 		dataCadastro;
    private Date 		dataInicioOferta;
    private Date 		dataFimOferta;
    private boolean 	enviaBroadcast;
    private Collection 	assinantes;
	
    public OfertaPacoteDados()
    {
        idtOferta 		 = -1;
        tipoSaldo 		 = null;
        pacoteDados 	 = null;
        dataCadastro 	 = null;
        dataInicioOferta = null;
        dataFimOferta 	 = null;
        enviaBroadcast 	 = false;
        assinantes 		 = new ArrayList();
    }
    
	/**
	 * @return the assinantes
	 */
	public Collection getAssinantes()
	{
		return assinantes;
	}
	
	/**
	 * @param assinantes the assinantes to set
	 */
	public void setAssinantes(Collection assinantes)
	{
		this.assinantes = assinantes;
	}
	
	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro()
	{
		return dataCadastro;
	}
	
	/**
	 * @param dataCadastro the dataCadastro to set
	 */
	public void setDataCadastro(Date dataCadastro)
	{
		this.dataCadastro = dataCadastro;
	}
	
	/**
	 * @return the dataFimOferta
	 */
	public Date getDataFimOferta()
	{
		return dataFimOferta;
	}
	
	/**
	 * @param dataFimOferta the dataFimOferta to set
	 */
	public void setDataFimOferta(Date dataFimOferta)
	{
		this.dataFimOferta = dataFimOferta;
	}
	
	/**
	 * @return the dataInicioOferta
	 */
	public Date getDataInicioOferta()
	{
		return dataInicioOferta;
	}
	
	/**
	 * @param dataInicioOferta the dataInicioOferta to set
	 */
	public void setDataInicioOferta(Date dataInicioOferta)
	{
		this.dataInicioOferta = dataInicioOferta;
	}
	
	/**
	 * @return the enviaBroadcast
	 */
	public boolean isEnviaBroadcast()
	{
		return enviaBroadcast;
	}
	
	/**
	 * @param enviaBroadcast the enviaBroadcast to set
	 */
	public void setEnviaBroadcast(boolean enviaBroadcast)
	{
		this.enviaBroadcast = enviaBroadcast;
	}
	
	/**
	 * @return the idtOferta
	 */
	public int getIdtOferta()
	{
		return idtOferta;
	}
	
	/**
	 * @param idtOferta the idtOferta to set
	 */
	public void setIdtOferta(int idtOferta)
	{
		this.idtOferta = idtOferta;
	}
	
	/**
	 * @return the pacoteDados
	 */
	public PacoteDados getPacoteDados()
	{
		return pacoteDados;
	}
	
	/**
	 * @param pacoteDados the pacoteDados to set
	 */
	public void setPacoteDados(PacoteDados pacoteDados)
	{
		this.pacoteDados = pacoteDados;
	}
	
	/**
	 * @return the tipoSaldo
	 */
	public TipoSaldo getTipoSaldo()
	{
		return tipoSaldo;
	}
	
	/**
	 * @param tipoSaldo the tipoSaldo to set
	 */
	public void setTipoSaldo(TipoSaldo tipoSaldo)
	{
		this.tipoSaldo = tipoSaldo;
	}
    
	public boolean equals(Object obj)
    {
        if(!(obj instanceof OfertaPacoteDados))
            return false;
        
        return ((OfertaPacoteDados)obj).getIdtOferta() == idtOferta;
    }
	
    public String toString()
    {
        return "ID:" + idtOferta;
    }
    
    public int compareTo(Object obj)
    { 
    	if(!(obj instanceof OfertaPacoteDados)) 
    		throw new IllegalArgumentException();
    	
    	return this.idtOferta - ((OfertaPacoteDados)obj).getIdtOferta();
   	}
    
    /**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return	int	Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idtOferta);
		
		return result.toString().hashCode();
	}
	
	/**
	 * Retorna uma nova instancia do mesmo objeto
	 * 
	 */
	public Object clone()
	{
		OfertaPacoteDados oferta = new OfertaPacoteDados();
		
		oferta.setIdtOferta(this.idtOferta);
		oferta.setPacoteDados(this.pacoteDados);
		oferta.setDataCadastro(this.dataCadastro);
	    oferta.setDataInicioOferta(this.dataInicioOferta);
	    oferta.setEnviaBroadcast(this.enviaBroadcast);
	    oferta.setAssinantes(this.assinantes);
	    
	    return oferta;
	}
	
	/**
	 * @return the desOferta
	 */
	public String getDesOferta()
	{
		return desOferta;
	}
	
	/**
	 * @param desOferta the desOferta to set
	 */
	public void setDesOferta(String desOferta)
	{
		this.desOferta = desOferta;
	}
}