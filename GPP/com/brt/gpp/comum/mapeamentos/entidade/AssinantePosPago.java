package com.brt.gpp.comum.mapeamentos.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;

/**
 *	Classe que representa a entidade da tabela TBL_APR_ASSINANTE_POSPAGO.
 * 
 *	@author	Jorge Abreu
 *	@since	08/11/2007
 */
public class AssinantePosPago 
{
	//Msisdn do assinante pos pago
	private String msisdn;
	
	//data de inclusao do assinante na tabela
	private Date dataInclusao;
	
	//entidade promocao vinculada ao assinante
	private Promocao promocao;
	
	//colecao de amigos cadastrados do assinante pos pago
	private Collection amigosGratis;
	
	//indicador se o assinante esta ativo ou nao na promocao fale de graca de natal FGN4
	private boolean ativo;
	
	public AssinantePosPago()
	{
		amigosGratis = new ArrayList();
	}
	
	/**
	 * @param idtMsisdn
	 */
	public void setMsisdn(String idtMsisdn)
	{
		this.msisdn = idtMsisdn;
	} 
	
	/**
	 * @param datInclusao
	 */
	public void setDataInclusao(Date datInclusao)
	{
		this.dataInclusao = datInclusao;
	}
	
	/**
	 * @param promocao
	 */
	public void setPromocao(Promocao promocao)
	{
		this.promocao = promocao;
	}
	
	/**
	 * @param idtAmigosGratis
	 */
	public void setAmigosGratis(Collection idtAmigosGratis)
	{
		this.amigosGratis = idtAmigosGratis;
	}
	
	/**
	 * Metodo para adicioanr um amigo do assinante a partir de uma String com apenas um numero
	 * @param String amigo
	 */
	public void adicionarAmigoGratis(String amigo)
	{
		this.amigosGratis.add(amigo);
	}
	
	/**
	 * Metodo para setar os amigos do assinante a partir de uma String com os numeros separados por ;
	 * @param String amigos
	 */
	public void setListaAmigos(String amigos)
	{
		if (amigos != null)
		{
			String[] amigo = amigos.split(";");
			
			for(int i = 0; i < amigo.length; i++)
				if(amigo[i] != null && !amigo[i].equals(""))
					adicionarAmigoGratis(amigo[i]);
		}
	}
	
	/**
	 * @param indAtivo
	 */
	public void setAtivo(boolean indAtivo)
	{
		this.ativo = indAtivo;
	}
	
	/**
	 * @return String idtMsisdn
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	} 
	
	/**
	 * @return Date datInclusao
	 */
	public Date getDataInclusao()
	{
		return this.dataInclusao;
	}
	
	/**
	 * @return MapPromocao promocao
	 */
	public Promocao getPromocao()
	{
		return this.promocao;
	}
	
	/**
	 * @return Collection idtAmigosGratis
	 */
	public Collection getAmigosGratis()
	{
		return this.amigosGratis;
	}
	
	/**
	 * @return String lista de amigos separados por ;
	 */
	public String getListaAmigos()
	{
		StringBuffer lista = new StringBuffer();
		
		for (Iterator it = amigosGratis.iterator(); it.hasNext();)
		{
			lista.append(it.next());
			if (it.hasNext())
				lista.append(";");
		}
		
		return lista.toString();
	}
	
	/**
	 * @return boolean indAtivo
	 */
	public boolean isAtivo()
	{
		return this.ativo;
	}
	
	public boolean equals(Object obj) 
	{
		if(obj == null)
			return false;
		
		if ( !(obj instanceof AssinantePosPago) )
			return false;
		
		return (this.hashCode() == obj.hashCode());
	}

	public int hashCode() 
	{
		return this.msisdn.hashCode();
	}

	public String toString() 
	{
		return this.msisdn;
	}
}