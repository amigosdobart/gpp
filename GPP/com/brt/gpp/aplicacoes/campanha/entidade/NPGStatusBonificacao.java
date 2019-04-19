package com.brt.gpp.aplicacoes.campanha.entidade;

/**
 *	Classe que encapsula as informacoes referentes ao status da bonificacao da promocao Natal Pague e Ganhe.
 * 
 *	@author		Daniel Ferreira
 *	@since		01/11/2006
 */
public abstract class NPGStatusBonificacao 
{
	
	/**
	 *	Status de assinante inscrito na promocao e aguardando periodo de espera.
	 */
	public static final int INSCRITO = 0;
	
	/**
	 *	Status de recarga efetuada e aguardando periodo de espera.
	 */
	public static final int RECARGA_EFETUADA = 1;
	
	/**
	 *	Status de periodo de espera encerrado e aguardando cadastro de promocao Pula-Pula.
	 */
	public static final int PENDENTE_PULA_PULA = 2;
	
	/**
	 *	Status de bonus liberado para concessao.
	 */
	public static final int BONUS_LIBERADO = 3;
	
	/**
	 *	Status de bonus one-shot concedido.
	 */
	public static final int BONUS_CONCEDIDO = 4;
	
	/**
	 *	Status de assinante nao validado. O motivo de nao validacao depende do codigo de retorno da operacao de concessao do bonus.
	 */
	public static final int NAO_VALIDADO = 5;
	
	/**
	 *	Retorna a descricao do status.
	 *
	 *	@return		Descricao do status.
	 */
	public static String getDescricao(int status)
	{
		switch(status)
		{
			case NPGStatusBonificacao.INSCRITO:
				return "Assinante inscrito - Valor minimo em recargas nao atingido";
			case NPGStatusBonificacao.RECARGA_EFETUADA:
				return "Recarga efetuada - Aguardando termino de periodo de espera";
			case NPGStatusBonificacao.PENDENTE_PULA_PULA:
				return "Bonificacao pendente de cadastro do assinante em promocao Pula-Pula valida";
			case NPGStatusBonificacao.BONUS_LIBERADO:
				return "Bonus do assinante liberado";
			case NPGStatusBonificacao.BONUS_CONCEDIDO:
				return "Bonus concedido ao assinante";
			case NPGStatusBonificacao.NAO_VALIDADO:
				return "Assinante nao validado";
			default: return null;
		}
	}
	
}
