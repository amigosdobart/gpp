package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_STATUS_ASSINANTE.
 * 
 *	@author	Daniel Ferreira
 *	@since	06/06/2006
 */
public class PromocaoStatusAssinante implements Entidade
{

	/**
	 *	Constante para status de assinante ativo.
	 */
	public static final int STATUS_ATIVO = 0;
	public static final int ATIVO		 = 0;
	
	/**
	 *	Constante para status de assinante pendente de primeira recarga.
	 */
	public static final int STATUS_PENDENTE_RECARGA = 1;
	public static final int PENDENTE_RECARGA		= 1;
	
    /**
     *  Constante para status de assinante ativo participante de promocao de incentivo a primeira recarga.
     */
    public static final int STATUS_ATIVO_RECARGA = 2;
    public static final int ATIVO_RECARGA		 = 2;
    
    /**
     *  Constante para status de assinante suspeito de recebimento excessivo de ligacoes.
     */
    public static final int STATUS_SUSPEITO_RECEB_EXCESSIVO	= 3;
    public static final int SUSPEITO_RECEB_EXCESSIVO		= 3;

    /**
     *  Constante para status de assinante Suspenso por Recarga Expirada.
     */
    public static final int STATUS_SUSPENSO_RECARGA_EXPIRADA = 4;
    public static final int SUSPENSO_RECARGA_EXPIRADA        = 4;

    /**
     *  Constante para status de assinante Suspenso por Limite Alcancado.
     */
    public static final int STATUS_SUSPENSO_LIMITE_ALCANCADO = 5;
    public static final int SUSPENSO_LIMITE_ALCANCADO        = 5;
    
    /**
     *  Constante para status de assinante Suspenso por Limite Alcancado e Recarga expirada.
     */
    public static final int STATUS_SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA = 6;
    public static final int SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA        = 6;
    
	/**
	 *	Identificador do status.
	 */
	private	int idtStatus;
	
	/**
	 *	Descricao do status.
	 */
	private String desStatus;
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoStatusAssinante()
	{
		this.idtStatus	= -1;
		this.desStatus	= null;
	}
	
	/**
	 *	Retorna o identificador do status da promocao do assinante.
	 * 
	 *	@return		Identificador do status da promocao do assinante.
	 */
	public int getIdtStatus() 
	{
		return this.idtStatus;
	}
	
	/**
	 *	Retorna a descricao do status da promocao do assinante.
	 * 
	 *	@return		Descricao do status da promocao do assinante.
	 */
	public String getDesStatus() 
	{
		return this.desStatus;
	}
	
	/**
	 *	Atribui o identificador do status da promocao do assinante.
	 * 
	 *	@param		idtStatus				Identificador do status da promocao do assinante.
	 */
	public void setIdtStatus(int idtStatus) 
	{
		this.idtStatus = idtStatus;
	}
	
	/**
	 *	Atribui a descricao do status da promocao do assinante.
	 * 
	 *	@param		desStatus				Descricao do status da promocao do assinante.
	 */
	public void setDesStatus(String desStatus) 
	{
		this.desStatus = desStatus;
	}
		
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Copia do objeto.
	 */
	public Object clone()
	{
		PromocaoStatusAssinante result = new PromocaoStatusAssinante();	
		
		result.setIdtStatus(this.idtStatus);
		result.setDesStatus(this.desStatus);
		
		return result;
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		object					Objeto a ser comparado com o atual.
	 *	@return		True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
			return false;
		if(!this.getClass().isInstance(object))
			return false;
		if(this.hashCode() != object.hashCode())
			return false;
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(String.valueOf(this.idtStatus));
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("Status: ");
		result.append((this.desStatus != null) ? this.desStatus : "NULL");
		
		return result.toString();
	}
	
	/**
	 *	Indica se o status de promocao de assinante corresponde a um status ativo.
	 * 
	 *	@return		True se o status e ativo e false caso contrario.
	 */
	public boolean isAtivo()
	{
		return (this.validarStatus(true) == Definicoes.RET_OPERACAO_OK);
	}
	
	/**
	 *	Indica se o status de promocao de assinante permite consulta de bonificacao.
	 * 
	 *	@return		True se o status permite consulta e false caso contrario.
	 */
	public boolean isDisponivelConsulta()
	{
		return (this.validarStatus(false) == Definicoes.RET_OPERACAO_OK);
	}
	
	/**
	 *	Marca as flags de zeramento de saldos de acordo com o status do assinante.
	 * 
	 *	@param		bonusAgendado			Objeto representando o registro do assinante na Fila de Recargas.
	 */
	public void marcarZerarSaldos(FilaRecargas bonusAgendado)
	{
        //Nao deve ser zerado o saldo de bonus no caso de o assinante participar da promocao de incentivo a
        //primeira recarga. O processo desta promocao de incentivo altera o status da promocao do assinante. Caso 
        //o assinante possua este status, o saldo de bonus nao e zerado e o status e alterado para ativo, uma vez 
        //que a partir da proxima concessao o saldo de bonus deve ser zerado normalmente, caso a promocao exija.
		if(this.idtStatus == PromocaoStatusAssinante.ATIVO_RECARGA)
		{
			bonusAgendado.setIndZerarSaldoPeriodico(new Integer(0));
			bonusAgendado.setIndZerarSaldoBonus    (new Integer(0));
			bonusAgendado.setIndZerarSaldoSms      (new Integer(0));
			bonusAgendado.setIndZerarSaldoGprs     (new Integer(0));
		}
	}
	
	/**
	 *	Retorna o proximo status de promocao de assinante a partir do status atual.
	 * 
	 *	@return		Informacoes referentes ao proximo status.
	 *	@throws		Exception.
	 */
	public PromocaoStatusAssinante next() throws Exception
	{
		Consulta consulta = new Consulta(0);
		
		switch(this.idtStatus)
		{
			case PromocaoStatusAssinante.ATIVO_RECARGA:
				return consulta.getPromocaoStatusAssinante(new Integer(PromocaoStatusAssinante.ATIVO));
			default: return this;
		}
	}
	
	/**
	 *	Valida e retorna um codigo de acordo com o status da promocao do assinante.
	 *
	 *	@param		isConcessao				Indicador de validacao para concessao de bonus. 
	 *	@return		Codigo de retorno da operacao.
	 */
	public short validarStatus(boolean isConcessao)
	{
		switch(this.idtStatus)
		{
			case PromocaoStatusAssinante.ATIVO:
			case PromocaoStatusAssinante.ATIVO_RECARGA:
			case PromocaoStatusAssinante.SUSPENSO_LIMITE_ALCANCADO:
				return Definicoes.RET_OPERACAO_OK;
			case PromocaoStatusAssinante.PENDENTE_RECARGA:
				return Definicoes.RET_PROMOCAO_PENDENTE_RECARGA;
			case PromocaoStatusAssinante.STATUS_SUSPEITO_RECEB_EXCESSIVO:
				if(isConcessao)
					return Definicoes.RET_OPERACAO_OK;
				return Definicoes.RET_PROMOCAO_ASS_BLOQ_CONSULTA;
			case PromocaoStatusAssinante.SUSPENSO_RECARGA_EXPIRADA:
			case PromocaoStatusAssinante.SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA:
				if(!isConcessao)// Se nao for concessao
					return Definicoes.RET_OPERACAO_OK;
				return Definicoes.RET_STATUS_MSISDN_INVALIDO;
			default: return Definicoes.RET_PROMOCAO_ASSINANTE_NAO_ATIVO;
		}
	}
}
