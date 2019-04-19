package com.brt.gpp.aplicacoes.promocao.sumarizacaoPulaPula;

/**
 *	Classe que representa o VO da Sumarização Pula-Pula.
 * 
 *	@author	Magno Batista Corrêa
 *	@since	19/04/2005 (dd/mm/yyyy)
 *
 *  @author Marcelo Alves Araujo
 *  @since	08/01/2007
 */
public class SumarizacaoPulaPulaVO
{    
	private String	cn;
    private int		idtPromocao;
    
    /**
	 * Construtor da classe
	 */
	public SumarizacaoPulaPulaVO(int idtPromocao, String cn)
	{
	    this.cn				= cn;
	    this.idtPromocao	= idtPromocao;
	}

	//Getters.	
	/**
	 *	Retorna o CN.
	 *	
	 *	@return		cn						cn a ser processado.
	 */
	public String getCn() 
	{
		return cn;
	}
	
	/**
	 *	Retorna a Promocao de processamento.
	 *	
	 *	@return		idtPromocao					Promocao a ser processada.
	 */
	public int getIdtPromocao() 
	{
		return this.idtPromocao;
	}
	
	//Setters.
	/**
	 *	Atribui o CN.
	 *	
	 *	@param		cn						msisdn a ser processado.
	 */
	public void setCn(String cn) 
	{
		this.cn = cn;
	}
	
	/**
	 *	Atribui a Promocao de processamento.
	 *	
	 *	@param		idtPromocao					Promocao a ser processada.
	 */
	public void setIdtPromocao(int idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
}