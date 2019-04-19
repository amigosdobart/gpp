package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;

/**
 *	Classe que representa o conjunto de parâmetros de notificação. 
 *  O Tangram utilizará essas informações para fazer o callback 
 *  assíncrono contendo status de envio de cada SMS. 
 * 
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class ParametrosNotificacao implements Serializable
{
	private static final long serialVersionUID = 3880100311387698506L;
	
	public static final short EVENTO_SUCESSO_ENTREGA_SMSC 			= 1;
	public static final short EVENTO_FALHA_ENTREGA_SMSC 			= 2;
	public static final short EVENTO_SUCESSO_ENTREGA_CELULAR 		= 4;
	public static final short EVENTO_FALHA_ENTREGA_CELULAR 			= 8;
	public static final short EVENTO_NOTIFICACAO_COBRANCA			= 16;
	public static final short EVENTO_NOTIFICACAO_APENAS_NO_TANGRAM	= 32;
	
	public static final Short RETORNO_HTTP_GET   = new Short((short)0);
	public static final Short RETORNO_HTTP_POST  = new Short((short)1);
	public static final Short RETORNO_SOAP		 = new Short((short)2);
	public static final Short RETORNO_COM_PLUS	 = new Short((short)3);

	/**
	 * Tipos de evento que podem gerar notificação. Os valores possíveis 
	 * estão mapeados nas constantes de classe (EVENTO_).
	 * 
	 * Para incluir vários tipos de eventos, faça o OU (|) dessas constantes.
	 * 
	 * IMPORTANTE: Se EVENTO_NOTIFICACAO_APENAS_NO_TANGRAM estiver habilitado, 
	 * a aplicação não receberá notificação.
	 */
	private Short tiposEvento;
	
	/**
	 * Tipo de entrega da notificação. Os valores possíveis 
	 * estão mapeados nas constantes de classe (RETORNO_).
	 */
	private Short tipoRetorno;
	
	/**
	 * Local de entrega da notificação. Informe o PID do componente
	 * para o caso RETORNO_COM_PLUS e URL para os demais casos.
	 */
	private String enderecoRetorno;
	
	/**
	 * Obtém o local de entrega da notificação. 
	 */
	public String getEnderecoRetorno() 
	{
		return enderecoRetorno;
	}

	/**
	 * Define o local de entrega da notificação. Informe o PID do componente
	 * para o caso RETORNO_COM_PLUS e URL para os demais casos.
	 * 
	 * @param enderecoRetorno URL ou PID
	 */
	public void setEnderecoRetorno(String enderecoRetorno) 
	{
		this.enderecoRetorno = enderecoRetorno;
	}

	/**
	 * Obtém o tipo de entrega da notificação. 
	 */
	public Short getTipoRetorno() 
	{
		return tipoRetorno;
	}

	/**
	 * Define o tipo de entrega da notificação. Os valores possíveis 
	 * estão mapeados nas constantes de classe (RETORNO_).
	 * 
	 * @param tipoRetorno Vide constantes RETORNO_
	 */
	public void setTipoRetorno(Short tipoRetorno) 
	{
		this.tipoRetorno = tipoRetorno;
	}

	/**
	 * Obtém os tipos de evento que podem gerar notificação. 
	 * Esse valor é a concatenacao (OU, |) das constantes EVENTO_.
	 */
	public Short getTiposEvento() 
	{
		return tiposEvento;
	}

	/**
	 * Define os tipos de evento que podem gerar notificação. 
	 * Esse valor é a concatenacao (OU, |) das constantes EVENTO_.
	 * 
	 * IMPORTANTE: Se EVENTO_NOTIFICACAO_APENAS_NO_TANGRAM estiver habilitado, 
	 * a aplicação não receberá notificação.
	 */
	public void setTiposEvento(Short tiposEvento) 
	{
		this.tiposEvento = tiposEvento;
	}	
}
