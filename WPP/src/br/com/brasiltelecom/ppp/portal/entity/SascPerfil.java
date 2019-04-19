package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Classe que contem os atributos de Grupo do SASC 
 *  
 * @author JOAO PAULO GALVAGNI 
 * @since  06/02/2007 
 *
 */
public class SascPerfil
{
	private int 		codPerfil;
	private String 	    nomePerfil;
	private Collection  listaDeServicos;
	
	public SascPerfil()
	{
		listaDeServicos = new ArrayList();
	}
	/**
	 * @return the codPerfil
	 */
	public int getCodPerfil()
	{
		return codPerfil;
	}
	
	/**
	 * @param codPerfil the codPerfil to set
	 */
	public void setCodPerfil(int codPerfil)
	{
		this.codPerfil = codPerfil;
	}
	
	/**
	 * @return the nomePerfil
	 */
	public String getNomePerfil()
	{
		return nomePerfil;
	}
	
	/**
	 * @param nomePerfil the nomePerfil to set
	 */
	public void setNomePerfil(String nomePerfil)
	{
		this.nomePerfil = nomePerfil;
	}
	
	/**
	 * @return the listaDeServicos
	 */
	public Collection getListaDeServicos()
	{
		return listaDeServicos;
	}
	
	/**
	 * @param servico the listaDeServicos to add
	 */
	public void addListaDeServicos(SascServico servico)
	{
		listaDeServicos.add(servico);
	}
	
	/**
	 * @param listaDeServicos the listaDeServicos to set
	 */
	public void setListaDeServicos(Collection listaDeServicos)
	{
		this.listaDeServicos = listaDeServicos;
	}
}