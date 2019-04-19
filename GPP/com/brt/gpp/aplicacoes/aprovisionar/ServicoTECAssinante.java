package com.brt.gpp.aplicacoes.aprovisionar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.ServicoTecnomen;

/**
 *	Entidade que representa os servicos ativados na Tecnomen para o assinante. Utilizado na implementacao da Tecnomen 
 *	4.4.5 para a ativacao dos servicos de voz, eventos e dados durante o processo de aprovisionamento, principalmente 
 *	ativacao. Em outras palavras, cada objeto representa um servico ativado.
 *
 *	@version	1.0		Versao inicial.
 *	@author		Daniel Ferreira
 */
public class ServicoTECAssinante 
{

	/**
	 *	MSISDN do assinante. 
	 */
	private String msisdn;
	
	/**
	 *	Informacoes referentes ao servico ativado na Tecnomen. 
	 */
	private ServicoTecnomen servico;
	
	/**
	 *	Status do servico. 
	 */
	private short status;
	
	/**
	 *	Status de servico do servico. 
	 */
	private short statusServico;
	
	/**
	 *	Construtor da classe.
	 */
	public ServicoTECAssinante()
	{
		this.msisdn			= null;
		this.servico		= null;
		this.status			= -1;
		this.statusServico	= -1;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante;
	 * 	@param		servico					Servico ativado na Tecnomen.
	 */
	public ServicoTECAssinante(String msisdn, ServicoTecnomen servico)
	{
		this();
		
		this.msisdn		= msisdn;
		this.servico	= servico;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 * 	@param		tipo					Informacoes referentes ao tipo de saldo.
	 * 	@param		status					Status do servico.
	 * 	@param		statusServico			Status de servico do servico.
	 */
	public ServicoTECAssinante(String msisdn, ServicoTecnomen servico, short status, short statusServico)
	{
		this(msisdn, servico);
		
		this.status			= status;
		this.statusServico	= statusServico;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		MSISDN do assinante. 
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna as informacoes referentes ao servico ativado na Tecnomen.
	 *
	 *	@return		Informacoes referentes ao servico ativado na Tecnomen. 
	 */
	public ServicoTecnomen getServicoTecnomen()
	{
		return this.servico;
	}
	
	/**
	 *	Retorna o status do servico.
	 *
	 * 	@return		 Status do servico.
	 */
	public short getStatus()
	{
		return this.status;
	}
	
	/**
	 *	Retorna o status de servico do servico.
	 *
	 * 	@return		Status de servico do servico. 
	 */
	public short getStatusServico()
	{
		return this.statusServico;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		msisdn					MSISDN do assinante. 
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui o status do servico.
	 *
	 * 	@param		status		 			Status do servico.
	 */
	public void setStatus(short status)
	{
		this.status = status;
	}
	
	/**
	 *	Atribui o status de servico do servico.
	 *
	 * 	@param		statusServico			Status de servico do servico. 
	 */
	public void setStatusServico(short statusServico)
	{
		this.statusServico = statusServico;
	}
	
	/**
	 * 	Constroi e retorna uma lista de servicos a serem ativados para o assinante. Os servicos dependem da categoria 
	 * 	a qual o plano do assinante pertence. Por exemplo, assinantes GSM tem os servicos de evento (Torpedos) e dados 
	 * 	ativados. Assinantes da Fixa nao. 
	 * 
	 *	@param		assinante				Informacoes do assinante.
	 *  @return		Lista de servicos a serem ativados na Tecnomen para o assinante.
	 */
	public static Collection newServicosTECAssinante(Assinante assinante)
	{
		ArrayList result = new ArrayList();
		
		//Obtendo a categoria do plano de preco definido para o assinante. A categoria possui a lista de servicos 
		//permitidos para o assinante durante a ativacao. 
		Categoria categoria = MapPlanoPreco.getInstancia().getPlanoPreco(assinante.getPlanoPreco()).getCategoria();
		
		for(Iterator iterator = categoria.getServicosTecnomen().iterator(); iterator.hasNext();)
		{
			ServicoTecnomen servico = (ServicoTecnomen)iterator.next();
			
			ServicoTECAssinante servicoAssinante = new ServicoTECAssinante(assinante.getMSISDN(), 
																		   servico, 
																		   servico.getIdtStatusDefault(), 
																		   servico.getIdtStatusServicoDefault());
			
			result.add(servicoAssinante);
		}
		
		return result;
	}
	
}
