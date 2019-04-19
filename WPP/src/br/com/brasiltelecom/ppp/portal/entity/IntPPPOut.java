package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;
import java.util.*;

/**
 * Modela a tabela de intpppout (interface com o vitria)
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class IntPPPOut implements Serializable
{
    private long idProcessamento;          
    private Date datCadastro;              
    private String idtEventoNegocio;      
    private String xmlDocument;            
    private String idtStatusProcessamento;

    public Date getDatCadastro()
    {
        return datCadastro;
    }
    public void setDatCadastro(Date datCadastro)
    {
        this.datCadastro = datCadastro;
    }
    public long getIdProcessamento()
    {
        return idProcessamento;
    }
    public void setIdProcessamento(long idProcessamento)
    {
        this.idProcessamento = idProcessamento;
    }
    public String getIdtEventoNegocio()
    {
        return idtEventoNegocio;
    }
    public void setIdtEventoNegocio(String idtEventoNegocio)
    {
        this.idtEventoNegocio = idtEventoNegocio;
    }
    public String getIdtStatusProcessamento()
    {
        return idtStatusProcessamento;
    }
    public void setIdtStatusProcessamento(String idtStatusProcessamento)
    {
        this.idtStatusProcessamento = idtStatusProcessamento;
    }
    public String getXmlDocument()
    {
        return xmlDocument;
    }
    public void setXmlDocument(String xmlDocument)
    {
        this.xmlDocument = xmlDocument;
    }
}
