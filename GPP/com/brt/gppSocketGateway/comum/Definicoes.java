package com.brt.gppSocketGateway.comum;

abstract public class Definicoes
{
	// Constantes
	public static final		String		REF_SERVIDOR_GPP				=	"GPP";
	public static final		String		REF_SERVIDOR_APROVISIONAMENTO	=	"APROVISIONAMENTO";
	public static final		String		NOME_SISTEMA					=	"SERVIDOR_SOCKET";
	
	// Protocolos
	public static final		String		INICIO_XML_ASAP		=	"<root>";
	public static final		String		FIM_XML_ASAP		=	"</root>";
	public static final		String		INICIO_XML_GPP		=	"{GPP-APROVISIONAMENTO}";
	public static final		String		FIM_XML_GPP			=	"{/GPP-APROVISIONAMENTO}";
	
	// Log
	// Tipos de Log / Trace
	public static final int		DEBUG			= 0;
	public static final int		INFO			= 1;
	public static final int		WARN			= 2;
	public static final int		ERRO			= 3;
	public static final int		FATAL			= 4;	
	
	// Constantes de String para hand-shake com o sistema de aprovisionamento
	public static final String STR_INICIO_HANDSHAKE_ASAP      		= "{Start}";
	public static final String STR_FINAL_HANDSHAKE_ASAP      		= "{Stop}";
	public static final String STR_CONFIRMACAO_HANDSHAKE_ASAP 		= "{Started}";
	public static final String STR_CONFIRMACAO_HANDSHAKE_FINAL_ASAP = "{Stopped}";
	public static final String STR_CONFIRMACAO_ENVIO_XML_ASAP 		= "{ack}";
	
	// Tipos de Servidores
	public static final int 	TIPO_ASAP		= 0;
	public static final int 	TIPO_GPP		= 1;
	
}