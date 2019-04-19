package com.brt.gpp.aplicacoes.contestar.entidade;

import java.util.ArrayList;
import java.util.Collection;

public class Contestacao
{
	// Dados para publicacao de protocolo
	private String 		idPublicacao;
	private String 		protocoloUnico;
	private String 		sistemaAtendente;
	private String		processo;
	private String		identificador;
	
	private Collection	listaProtocolosNativos;
	
	/**
	 * Construtor da Classe
	 */
	public Contestacao()
	{
		this.listaProtocolosNativos = new ArrayList();
	}
	
	/**
	 * @return the idPublicacao
	 */
	public String getIdPublicacao()
	{
		return idPublicacao;
	}
	
	/**
	 * @param idPublicacao the idPublicacao to set
	 */
	public void setIdPublicacao(String idPublicacao)
	{
		this.idPublicacao = idPublicacao;
	}
	
	/**
	 * @return the listaProtocolosNativos
	 */
	public Collection getListaProtocolosNativos()
	{
		return listaProtocolosNativos;
	}
	
	/**
	 * @param protocoloNativo the listaProtocolosNativos to add
	 */
	public void addListaProtocolosNativos(ProtocoloNativo protocoloNativo)
	{
		this.listaProtocolosNativos.add(protocoloNativo);
	}
	
	/**
	 * @return the protocoloUnico
	 */
	public String getProtocoloUnico()
	{
		return protocoloUnico;
	}
	
	/**
	 * @param protocoloUnico the protocoloUnico to set
	 */
	public void setProtocoloUnico(String protocoloUnico)
	{
		this.protocoloUnico = protocoloUnico;
	}
	
	/**
	 * @return the sistemaAtendente
	 */
	public String getSistemaAtendente()
	{
		return sistemaAtendente;
	}
	
	/**
	 * @param sistemaAtendente the sistemaAtendente to set
	 */
	public void setSistemaAtendente(String sistemaAtendente)
	{
		this.sistemaAtendente = sistemaAtendente;
	}
	
	/**
	 * @return the identificador
	 */
	public String getIdentificador()
	{
		return identificador;
	}
	
	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador)
	{
		this.identificador = identificador;
	}
	
	/**
	 * @return the processo
	 */
	public String getProcesso()
	{
		return processo;
	}
	
	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(String processo)
	{
		this.processo = processo;
	}
}