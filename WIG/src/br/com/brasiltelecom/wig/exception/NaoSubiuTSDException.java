package br.com.brasiltelecom.wig.exception;

/**
 * Excecao para tratamento de assinante que nao inicializou o TSD ainda
 * 
 * @author Joao Carlos
 * Data..: 18/07/2005
 *
 */
public class NaoSubiuTSDException extends Exception
{
	private static final long serialVersionUID = 1L;
	public NaoSubiuTSDException(String msisdn)
	{
		super("Assinante "+msisdn+" nao inicializou o TSD.");
	}
}
