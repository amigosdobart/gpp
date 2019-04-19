package com.brt.gpp.aplicacoes.controleTotal.concessaoFranquia;

//Imports Java.

import java.util.Date;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
 *	Classe que representa o Value Object do Produtor-Consumidor da Automatizacao da Rebarba.
 * Foi definido para abstrair a ordem dos elementos do update da tabela, facilitando a implementacao
 * da clausula de update.
 * 
 *	@author	Magno Batista Corrêa
 *	@since	2007/05/17 (yyyy/mm/dd)
 */
public class ConcessaoFranquiaVO
{
	private String				idProcessamento;   // Chave para recuperacao do Registro. Como a tabela nao tem chave, o produtor preenche com o ROWID
	private String				msisdn;            // Assinante a ser processado
    private String 				planoPrecoSAG;     // Plano Preco informado pelo SAG
	private	double 				valorFranquia;     // Valor a ser concedido ao assinante
	private	int 				numeroRecarga;     // Numero da recarga a qual se refere o processamento para evitar duplicidade de recargas.

	private	short 				retorno;           // Retorno do processamento
	private	Date				novaDataExpiracao; // Nova data de expiracao calculada pelo processo
	private	short 				planoPrecoGPP;

	//Construtores.

	/**
	 * Construtor Carregado
	 * @param idProcessamento
	 * @param msisdn
	 * @param planoPrecoSAG
	 * @param valorFranquia
	 * @param numeroRecarga
	 * @param retorno
	 * @param novaDataExpiracao
	 * @param planoPrecoGPP
	 */
	public ConcessaoFranquiaVO(String idProcessamento, String msisdn, 
			String planoPrecoSAG, double valorFranquia, int numeroRecarga,
			short retorno, Date novaDataExpiracao, short planoPrecoGPP)
	{
		this.idProcessamento   = idProcessamento;
		this.msisdn            = msisdn;
		this.planoPrecoSAG     = planoPrecoSAG;
		this.valorFranquia     = valorFranquia;
		this.numeroRecarga     = numeroRecarga;
		this.retorno           = retorno;
		this.novaDataExpiracao = novaDataExpiracao;
		this.planoPrecoGPP     = planoPrecoGPP;
	}
	
	/**
	 * Construtor com as informacoes enviadas pelo SAG
	 * @param idProcessamento
	 * @param msisdn
	 * @param planoPrecoSAG
	 * @param valorFranquia
	 * @param numeroRecarga
	 */
	public ConcessaoFranquiaVO(String idProcessamento, String msisdn, String planoPrecoSAG, double valorFranquia, int numeroRecarga)
	{
		this.reset();
		this.idProcessamento   = idProcessamento;
		this.msisdn            = msisdn;
		this.planoPrecoSAG     = planoPrecoSAG;
		this.valorFranquia     = valorFranquia;
		this.numeroRecarga     = numeroRecarga;
		MapPlanoPreco map      = MapPlanoPreco.getInstancia();
		PlanoPreco planoPreco  = map.getPlanoPreco(this.planoPrecoSAG);
		if(planoPreco != null)
		{
			this.planoPrecoGPP = Short.parseShort(planoPreco.getIdtPlanoPreco());
			this.retorno       = Definicoes.RET_OPERACAO_OK;
		}
		else
		{
			this.retorno       = Definicoes.RET_PLANO_PRECO_INVALIDO;
		}
		
	}

	/**
	 * Construtor da classe
	 */
	public ConcessaoFranquiaVO()
	{
	    this.reset();
	}

	/**
	 *	Inicializa o objeto com valores vazios.
	 */
	public void reset()
	{
		this.idProcessamento    = null;
		this.msisdn             = null;
		this.planoPrecoSAG      = null;
		this.valorFranquia      = 0.0;
		this.numeroRecarga      = 0;
		this.retorno            = 0;
		this.novaDataExpiracao  = null;
		this.planoPrecoGPP      = 0;
	}
	
	public String toString()
	{
		return "msisdn=" + this.msisdn + ";planoPrecoGPP=" + this.planoPrecoGPP + ";novaDataExpiracao=" + this.novaDataExpiracao 
		+ ";valorFranquia=" + this.valorFranquia + ";numeroRecarga=" + numeroRecarga +
		";planoPrecoSAG=" + this.planoPrecoSAG + 
		";retorno=" + this.retorno ;
	}

	/**
	 * @return Retorna o idProcessamento.
	 */
	public String getIdProcessamento()
	{
		return idProcessamento;
	}

	/**
	 * @return Retorna o msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * @return Retorna o novaDataExpiracao.
	 */
	public Date getNovaDataExpiracao()
	{
		return novaDataExpiracao;
	}

	/**
	 * @return Retorna o numeroRecarga.
	 */
	public int getNumeroRecarga()
	{
		return numeroRecarga;
	}

	/**
	 * @return Retorna o planoPrecoGPP.
	 */
	public short getPlanoPrecoGPP()
	{
		return planoPrecoGPP;
	}

	/**
	 * @return Retorna o planoPrecoSAG.
	 */
	public String getPlanoPrecoSAG()
	{
		return planoPrecoSAG;
	}

	/**
	 * @return Retorna o retorno.
	 */
	public short getRetorno()
	{
		return retorno;
	}

	/**
	 * @return Retorna o valorFranquia.
	 */
	public double getValorFranquia()
	{
		return valorFranquia;
	}

	/**
	 * @param idProcessamento O idProcessamento para alterar.
	 */
	public void setIdProcessamento(String idProcessamento)
	{
		this.idProcessamento = idProcessamento;
	}

	/**
	 * @param msisdn O msisdn para alterar.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	/**
	 * @param novaDataExpiracao O novaDataExpiracao para alterar.
	 */
	public void setNovaDataExpiracao(Date novaDataExpiracao)
	{
		this.novaDataExpiracao = novaDataExpiracao;
	}

	/**
	 * @param numeroRecarga O numeroRecarga para alterar.
	 */
	public void setNumeroRecarga(int numeroRecarga)
	{
		this.numeroRecarga = numeroRecarga;
	}

	/**
	 * @param planoPrecoGPP O planoPrecoGPP para alterar.
	 */
	public void setPlanoPrecoGPP(short planoPrecoGPP)
	{
		this.planoPrecoGPP = planoPrecoGPP;
	}

	/**
	 * @param planoPrecoSAG O planoPrecoSAG para alterar.
	 */
	public void setPlanoPrecoSAG(String planoPrecoSAG)
	{
		this.planoPrecoSAG = planoPrecoSAG;
	}

	/**
	 * @param retorno O retorno para alterar.
	 */
	public void setRetorno(short retorno)
	{
		this.retorno = retorno;
	}

	/**
	 * @param valorFranquia O valorFranquia para alterar.
	 */
	public void setValorFranquia(double valorFranquia)
	{
		this.valorFranquia = valorFranquia;
	}
	
	//Getters E Setters

}