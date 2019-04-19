package com.brt.gpp.comum;

/**
  *
  * Este arquivo contem a classe abstrata de definicoes do GPP
  * Contem algumas definicoes (constantes) para a aplicacao
  * <P> Version:		1.0
  *
  * @Autor: 		Daniel Cintra Abib
  * Data: 			20/02/2004
  *
  */
abstract public class Definicoes
{
	// Constantes

	//	Regras de Negócio
	public static final int		SALDO_MAXIMO 	= 300;
	public static final String 	PRAZO_DELECAO_TABELAS_INTERFACE = "PRAZO_DELECAO_TABELAS_INTERFACE";

	// Conexoes Tecnomen
	public static final int 	TECNOMEN_APROVISIONAMENTO 		= 0;
	public static final int 	TECNOMEN_RECARGA 				= 8;
	public static final String 	TECNOMEN_IOR_FILE 				= "idl_gateway.ior";
	public static final int		TECNOMEN_MAQUINA_RECARGA 		= 100;
	public static final short 	TECNOMEN_LING_INGLES    		= 1;
	public static final short 	TECNOMEN_LING_PORTUGUES 		= 4;
	public static final short 	TECNOMEN_LING_ESPANHOL  		= 32;
	public static final int 	TECNOMEN_MULTIPLICADOR 			= 100000;
	public static final short 	TECNOMEN_ONLINE_TIPO_TRANSACAO 	= 15;
	public static final short	TECNOMEN_DIRECT_TIPO_TRANSACAO 	= 47;
	public static final short	TECNOMEN_RECHARGE_DISCOUNT_RATE = 0;
	public static final short 	TECNOMEN_EXPIRY_DATE_DIA 		= 1;
	public static final short 	TECNOMEN_EXPIRY_DATE_MES 		= 1;
	public static final short 	TECNOMEN_EXPIRY_DATE_ANO 		= 1970;
	public static final short 	TECNOMEN_EXPIRY_DATE_HORAS 		= 0;
	public static final short 	TECNOMEN_EXPIRY_DATE_MINUTOS 	= 0;
	public static final short 	TECNOMEN_EXPIRY_DATE_SEGUNDOS 	= 0;

	// Retornos Funcionais
	public static final int		RET_ERRO_GENERICO_TECNOMEN		 	=  9999;
	public static final int		RET_ERRO_GENERICO_GPP			 	=  9998;
	public static final int     RET_ERRO_GENERICO_MASC 			 	=  9997;
	public static final int     RET_ERRO_TIMEOUT_MASC 			 	=  9996;
	public static final int		RET_ERRO_SINCRONIZACAO_TECNOMEN		=  9995;
	public static final int 	RET_OPERACAO_OK					 	=  0;
	public static final int		RET_MSISDN_JA_ATIVO				 	=  1;
	public static final int		RET_MSISDN_NAO_ATIVO 			 	=  2;
	public static final int		RET_VALOR_CREDITO_INVALIDO		 	=  3;
	public static final int		RET_PLANO_PRECO_INVALIDO		 	=  4;
	public static final int		RET_CREDITO_INSUFICIENTE		 	=  5;
	public static final int		RET_CODIGO_BLOQUEIO_INVALIDO	 	=  6;
	public static final int		RET_NOVO_MSISDN_JA_ATIVO		 	=  7;
	public static final int		RET_TARIFA_TROCA_PLANO_INVALIDA	 	=  8;
	public static final int		RET_RECARGA_NAO_IDENTIFICADA	 	=  9;
	public static final int		RET_STATUS_MSISDN_INVALIDO		 	= 10;
	public static final int		RET_TIPO_TRANSACAO_INVALIDO		 	= 11;
	public static final int		RET_LIMITE_CREDITO_ULTRAPASSADO	 	= 12;
	public static final int		RET_USUARIO_NAO_BLOQUEADO		 	= 13;
	public static final int		RET_MSISDN_BLOQUEADO			 	= 14;
	public static final int		RET_VOUCHER_NAO_EXISTE			 	= 15;
	public static final int		RET_IMSI_JA_EM_USO				 	= 16;
	public static final int		RET_RECARGA_JA_EFETUADA			 	= 17;
	public static final int		RET_SEM_CHAMADAS_A_B			 	= 18;
	public static final int		RET_DADOS_ASSINANTE_INVALIDOS	 	= 19;
	public static final int		RET_TIPO_CREDITO_INVALIDO		 	= 20;
	public static final int		RET_VALOR_CC_ULTRAPASSADO		 	= 21;
	public static final int		RET_QTD_CC_ULTRAPASSADA			 	= 22;
	public static final int		RET_QTD_MSISDN_ULTRAPASSADA		 	= 23;
	public static final int		RET_STATUS_MSISDN_VALIDO		 	= 24;
	public static final int		RET_ID_RECARGA_AJUSTE_INVALIDO	 	= 25;
	public static final int		RET_DATA_RECARGA_AJUSTE_INVALIDO 	= 26;
	public static final int		RET_SENHA_ASSINANTE_INVALIDA 	 	= 27;
	public static final int		RET_CAMPO_OBRIGATORIO	 		 	= 28;
	public static final int		RET_INCLUSAO_USUARIO_EXISTENTE	 	= 29;
	public static final int		RET_ALTERACAO_USUARIO_INEXISTENTE 	= 30;
	public static final int		RET_EXCLUSAO_USUARIO_INEXISTENTE 	= 31;
	public static final int		RET_FORMATO_DATA_INVALIDA        	= 32;
	public static final int		RET_ACAO_INEXISTENTE        	 	= 33;
	public static final int		RET_SISTEMA_INEXISTENTE     	 	= 34;
	public static final int		RET_NUMERO_BS_INEXISTENTE     	 	= 35;
	public static final int		RET_SEM_REGISTRO_ULTIMA_EXECUCAO 	= 36;
	public static final	int		RET_BLOQDESBLOQ_NAO_SOLICITADO	 	= 37;
	public static final	int		RET_PARAMETO_NAO_INFORMADO		 	= 38;
	public static final	int		RET_SERVICO_INEXISTENTE			 	= 39;
	public static final int		RET_ERRO_CRIACAO_BONUS_RECARGA	 	= 40;
	public static final int		RET_ERRO_NAO_HA_BONUS_RECARGA	 	= 41;
	public static final int		RET_ERRO_ATUALIZ_BONUS_RECARGA	 	= 42;
	public static final int		RET_ERRO_ELIMINA_BONUS_RECARGA	 	= 43;
	public static final int		RET_PERCENT_BONUS_RECARGA_INV	 	= 44;
	public static final int		RET_ERRO_TEC_TECNOMEN_BONUS_REC	 	= 45;
	public static final int		RET_ERRO_CONSULTA_PRE_RECARGA	 	= 46;
	public static final int		RET_FRANQUIA_PARA_NAO_HIBRIDO    	= 47;
	public static final int		RET_CICLO_PLANO_HIBRIDO_JA_RODADO	= 48;
	public static final int		RET_XML_MAL_FORMADO				 	= 49;
	public static final int		RET_PROMOCAO_VALIDADE_NOK			= 50;
	public static final int		RET_PROMOCAO_LIGACOES_NOK	 		= 51;
	public static final int		RET_CODIGO_REVENDA_NAO_CADASTRADO	= 52;
	public static final int		RET_ASSINANTE_LIGMIX             	= 53;
	public static final int		RET_LIMITE_REVENDA_ULTRAPASSADO	 	= 54;
	public static final int		RET_REVENDA_BLOQUEADA			 	= 55;
	public static final int		RET_ASSINANTE_HIBRIDO				= 56;
	public static final int		RET_PROMOCAO_PREVISAO_MUD_STATUS	= 57;
	public static final int		RET_PROMOCAO_PENDENTE_RECARGA		= 58;
	public static final int		RET_MUDANCA_PLANO_GSM_LIGMIX		= 59;
	public static final int		RET_FUNC_INDISP_CATEGORIA			= 60;
	public static final int		RET_FUNC_INDISP_LIGMIX				= 60;
	public static final int		RET_PERIODO_CONTABIL_FECHADO		= 61;
	public static final int     RET_LIGMIX_TIPO_CREDITO_INVALIDO	= 62;
	public static final int		RET_PROMOCAO_NAO_EXISTE				= 63;
	public static final int		RET_PROMOCAO_ASSINANTE_JA_EXISTE	= 64;
	public static final int		RET_PROMOCAO_ASSINANTE_NAO_EXISTE	= 65;
	public static final int		RET_PROMOCAO_CN_INVALIDO			= 66;
	public static final int		RET_PROMOCAO_PLANO_PRECO_INVALIDO	= 67;
	public static final int		RET_PROMOCAO_DAT_ENTRADA_INVALIDA	= 68;
	public static final int		RET_PROMOCAO_CATEGORIA_EXCLUSIVA	= 69;
	public static final int		RET_PROMOCAO_TROCA_OK_PLANO_NOK		= 70;
	public static final int		RET_PROMOCAO_NOK_RETIRADA_NOK		= 71;
	public static final int		RET_PROMOCAO_CATEGORIA_INVALIDA		= 72;
	public static final int		RET_PROMOCAO_VALIDACAO_OK_REC_NOK	= 73;
	public static final int		RET_PROMOCAO_CONCESSAO_A_OCORRER	= 74;
	public static final int		RET_PROMOCAO_BONUS_CONCEDIDO		= 75;
	public static final int		RET_ALTERACAO_SEM_EFEITO            = 76;
	public static final int		RET_DIA_JA_PROCESSADO				= 77;
	public static final int		RET_INCOERENCIA_CATEGORIA_NUMERACAO = 78;
	public static final int		RET_CODIGO_ZERAR_SALDOS_INVALIDO	= 79;
	public static final int		RET_PROMOCAO_ORIGEM_NAO_EXISTE		= 80;
	public static final int		RET_NENHUM_REGISTRO					= 81;
	public static final int		RET_HIBRIDO_PROMOCAO_ANTIGA			= 82;
	public static final int		RET_PROMOCAO_BATE_VOLTA				= 83;
	public static final int		RET_SIMCARD_JA_UTILIZADO			= 84;
	public static final int		RET_APARELHO_JA_UTILIZADO			= 85;
	public static final int		RET_MSISDN_JA_UTILIZADO				= 86;
	public static final int		RET_RECARGA_NAO_REALIZADA			= 87;
	public static final int		RET_PROMOCAO_ASSINANTE_NAO_ATIVO	= 88;
	public static final int		RET_PROMOCAO_ASS_BLOQ_CONSULTA		= 89;
	public static final int		RET_REATIVACAO_INVIAVEL				= 90;
	public static final int		RET_STATUS_PERIODICO_INVALIDO		= 91;
	public static final int		RET_SALDO_ASSINANTE_INVALIDO		= 92;
	public static final int		RET_MOTIVO_DESATIVACAO_INVALIDO		= 93;
	public static final int		RET_FORMATO_MSISDN_INVALIDO         = 94;
	public static final int		RET_BONUS_PP_NAO_CONCEDIDO			= 95;
	public static final int		RET_PROMOCAO_TIPO_EXEC_INVALIDO		= 96;
	public static final int		RET_CONTEST_DOBRO_ITEM_IMPROCEDENTE = 97;
	public static final int		RET_ERRO_TECNICO		     	 	= 99;

	public static final String 	RET_S_OPERACAO_OK					  = "0000";
	public static final String	RET_S_MSISDN_JA_ATIVO				  = "0001";
	public static final String	RET_S_MSISDN_NAO_ATIVO 				  = "0002";
	public static final String	RET_S_VALOR_CREDITO_INVALIDO		  = "0003";
	public static final String	RET_S_PLANO_PRECO_INVALIDO			  = "0004";
	public static final String	RET_S_CREDITO_INSUFICIENTE			  = "0005";
	public static final String	RET_S_CODIGO_BLOQUEIO_INVALIDO		  = "0006";
	public static final String	RET_S_NOVO_MSISDN_JA_ATIVO			  = "0007";
	public static final String	RET_S_TARIFA_TROCA_PLANO_INVALIDA	  = "0008";
	public static final String	RET_S_RECARGA_NAO_IDENTIFICADA	      = "0009";
	public static final String	RET_S_STATUS_MSISDN_INVALIDO		  = "0010";
	public static final String	RET_S_TIPO_TRANSACAO_INVALIDO		  = "0011";
	public static final String	RET_S_LIMITE_CREDITO_ULTRAPASSADO	  = "0012";
	public static final String	RET_S_USUARIO_NAO_BLOQUEADO			  = "0013";
	public static final String	RET_S_MSISDN_BLOQUEADO				  = "0014";
	public static final String	RET_S_VOUCHER_NAO_EXISTE			  = "0015";
	public static final String	RET_S_IMSI_JA_EM_USO				  = "0016";
	public static final String	RET_S_RECARGA_JA_EFETUADA			  = "0017";
	public static final String	RET_S_SEM_CHAMADAS_A_B				  = "0018";
	public static final String	RET_S_DADOS_ASSINANTE_INVALIDOS		  = "0019";
	public static final String	RET_S_TIPO_CREDITO_INVALIDO			  = "0020";
	public static final String	RET_S_VALOR_CC_ULTRAPASSADO			  = "0021";
	public static final String	RET_S_QTD_CC_ULTRAPASSADA			  = "0022";
	public static final String	RET_S_QTD_MSISDN_ULTRAPASSADA		  = "0023";
	public static final String	RET_S_TAMANHO_VOUCHER_INVALIDO		  = "0024";
	public static final String	RET_S_ID_RECARGA_AJUSTE_INVALIDO	  = "0025";
	public static final String	RET_S_DATA_RECARGA_AJUSTE_INVALIDO    = "0026";
	public static final String	RET_S_SENHA_ASSINANTE_INVALIDA  	  = "0027";
	public static final String	RET_S_CAMPO_OBRIGATORIO	 		 	  = "0028";
	public static final String	RET_S_INCLUSAO_USUARIO_EXISTENTE	  = "0029";
	public static final String	RET_S_ALTERACAO_USUARIO_INEXISTENTE   = "0030";
	public static final String	RET_S_EXCLUSAO_USUARIO_INEXISTENTE 	  = "0031";
	public static final String	RET_S_FORMATO_DATA_INVALIDA        	  = "0032";
	public static final String	RET_S_ACAO_INEXISTENTE        	 	  = "0033";
	public static final String	RET_S_SISTEMA_INEXISTENTE     	 	  = "0034";
	public static final String	RET_S_NUMERO_BS_INEXISTENTE    	 	  = "0035";
	public static final String 	RET_S_SEM_REGISTRO_ULTIMA_EXECUCAO	  = "0036";
	public static final	String	RET_S_BLOQDESBLOQ_NAO_SOLICITADO	  = "0037";
	public static final	String	RET_S_PARAMETO_NAO_INFORMADO		  = "0038";
	public static final	String	RET_S_SERVICO_INEXISTENTE			  = "0039";
	public static final String	RET_S_CRIACAO_BONUS_RECARGA	 		  = "0040";
	public static final String	RET_S_NAO_HA_BONUS_RECARGA		 	  = "0041";
	public static final String	RET_S_ATUALIZ_BONUS_RECARGA	 		  = "0042";
	public static final String	RET_S_ELIMINA_BONUS_RECARGA	 		  = "0043";
	public static final String	RET_S_PERCENT_BONUS_RECARGA_INV	 	  = "0044";
	public static final String	RET_S_TEC_TECNOMEN_BONUS_REC	 	  = "0045";
	public static final String	RET_S_ERRO_CONSULTA_PRE_RECARGA		  = "0046";
	public static final String	RET_S_PROMOCAO_VALIDADE_NOK	 		  = "0050";
	public static final String	RET_S_PROMOCAO_LIGACOES_NOK			  = "0051";
	public static final String	RET_S_ASSINANTE_HIBRIDO				  = "0056";
	public static final String	RET_S_PROMOCAO_PREVISAO_MUD_STATUS 	  = "0057";
	public static final String	RET_S_PROMOCAO_PENDENTE_RECARGA		  = "0058";
	public static final String	RET_S_MUDANCA_PLANO_GSM_LIGMIX		  = "0059";
	public static final String	RET_S_FUNC_INDISP_LIGMIX			  = "0060";
	public static final String  RET_S_PERIODO_CONTABIL_FECHADO		  = "0061";
	public static final String  RET_S_LIGMIX_TIPO_CREDITO_INVALIDO	  = "0062";
	public static final String	RET_S_PROMOCAO_NAO_EXISTE			  = "0063";
	public static final String	RET_S_PROMOCAO_ASSINANTE_JA_EXISTE	  = "0064";
	public static final String	RET_S_PROMOCAO_ASSINANTE_NAO_EXISTE	  = "0065";
	public static final String	RET_S_PROMOCAO_CN_INVALIDO			  = "0066";
	public static final String	RET_S_PROMOCAO_PLANO_PRECO_INVALIDO	  = "0067";
	public static final String	RET_S_PROMOCAO_DAT_ENTRADA_INVALIDA	  = "0068";
	public static final String	RET_S_PROMOCAO_CATEGORIA_EXCLUSIVA	  = "0069";
	public static final String	RET_S_PROMOCAO_TROCA_OK_PLANO_NOK	  = "0070";
	public static final String	RET_S_PROMOCAO_NOK_RETIRADA_NOK		  = "0071";
	public static final String	RET_S_PROMOCAO_CATEGORIA_INVALIDA	  = "0072";
	public static final String	RET_S_PROMOCAO_VALIDACAO_OK_REC_NOK	  = "0073";
	public static final String	RET_S_PROMOCAO_CONCESSAO_A_OCORRER	  = "0074";
	public static final String	RET_S_PROMOCAO_BONUS_CONCEDIDO		  = "0075";
	public static final String  RET_S_RET_ALTERACAO_SEM_EFEITO        = "0076";
	public static final String	RET_S_DIA_JA_PROCESSADO				  = "0077";
	public static final String	RET_S_INCOERENCIA_CATEGOR_NUMERACAO   = "0078";
	public static final String	RET_S_PROMOCAO_ORIGEM_NAO_EXISTE	  = "0080";
	public static final String	RET_S_NENHUM_REGISTRO				  = "0081";
	public static final String	RET_S_HIBRIDO_PROMOCAO_ANTIGA		  = "0082";
	public static final String	RET_S_PROMOCAO_BATE_VOLTA			  = "0083";
	public static final String	RET_S_SIMCARD_JA_UTILIZADO			  = "0084";
	public static final String	RET_S_APARELHO_JA_UTILIZADO			  = "0085";
	public static final String	RET_S_MSISDN_JA_UTILIZADO			  = "0086";
	public static final String	RET_S_RECARGA_NAO_REALIZADA			  = "0087";
	public static final String	RET_S_PROMOCAO_ASSINANTE_NAO_ATIVO	  = "0088";
	public static final String	RET_S_PROMOCAO_ASS_BLOQ_CONSULTA	  = "0089";
	public static final String	RET_S_REATIVACAO_INVIAVEL		      = "0090";
	public static final String	RET_S_STATUS_PERIODICO_INVALIDO		  = "0091";
	public static final String	RET_S_SALDO_ASSINANTE_INVALIDO		  = "0092";
	public static final String	RET_S_MOTIVO_DESATIVACAO_INVALIDO	  = "0093";
	public static final String	RET_S_CONTEST_DOBRO_ITEM_IMPROCEDENTE = "0097";
	public static final String	RET_S_ERRO_TECNICO		     	 	  = "0099";

	public static final String	PROCESSO_SUCESSO					= "SUCESSO";
	public static final String	PROCESSO_ERRO						= "ERRO";

	// Tipos de arquivos de LOG / TRACE
	public static final int		ARQUIVO_LOG		= 0;
	public static final int		ARQUIVO_TRACE 	= 1;

	// Dialetos Tecnomen Aprovisionamento
	public static final short   DIALETO_GSM		= 0;
	public static final short	DIALETO_LIGMIX	= 1;

	// Definicoes de PinStatus para o aprovisionamento
	public static final short   PINSTATUS_GSM		= 3;
	public static final short	PINSTATUS_LIGMIX	= 0;

	// Tipos de Log / Trace
	public static final int		DEBUG			= 0;
	public static final int		INFO			= 1;
	public static final int		WARN			= 2;
	public static final int		ERRO			= 3;
	public static final int		FATAL			= 4;

	public static final String  LDEBUG			= "[DEBUG]";
	public static final String  LINFO			= "[INFO ]";
	public static final String  LWARN			= "[WARN ]";
	public static final String  LERRO			= "[ERROR]";
	public static final String  LFATAL			= "[FATAL]";

	// Conexoes com Banco de Dados
	public static final int BANCO_DADOS = 2;

	// Gerentes de POOL
	public static final String	GP_BANCO_DADOS		= "Gerente de Con de Banco de Dados";
	public static final String	GP_TECNOMEN			= "Gerente de Conexao Tecnomen";
	public static final String	GP_SMS				= "Gerente de Conexao SMSC";
	public static final String	GP_POOL_LOG			= "Gerente de Log";
	public static final String	GP_GERENTE_POOL		= "Gerente (Abstrato) de Pool";

	public static final String	CO_TECNOMEN_APR		= "Con. Tencomen (Aprovisionamento)";
	public static final String	CO_TECNOMEN_REC		= "Con. Tencomen (Recarga)";
	public static final String	CO_TECNOMEN_VOU		= "Con. Tencomen (Voucher)";
	public static final String	CO_TECNOMEN_ADM		= "Con. Tencomen (Admin)";
	public static final String	CO_TECNOMEN_AGE		= "Con. Tencomen (Agent)";
	public static final String	CO_BANCO_DADOS_PREP	= "Conexao Banco de Dados - PREP";
	public static final String	PR_ATIVA_COMPONENTES= "Ativa Componentes";

	//	GPP Operator ID para a Tecnomen
	 public static final short	GPP_OPERATOR_ID		= 1616; // GPP em decimal

	// Operador GPP para processos internos
	public static final String GPP_OPERADOR			= "GPP";

	// Sistema de Origem
	public static final String SO_BCO				= "BCO";
	public static final String SO_CLY				= "CLY";
	public static final String SO_GNV				= "GNV";
	public static final String SO_GPP				= "GPP";
	public static final String SO_MIC				= "MIC";
	public static final String SO_PBT				= "PBT";
	public static final String SO_TEC               = "TEC";
	public static final String SO_CRM				= "CRM";
	public static final String SO_VAREJO			= "VRJ";
	public static final String SO_DEALER			= "DEA";
	public static final String SO_MMS				= "MMS";
	public static final String IND_VALIDAR_CC_HASH	= "1";

	// Tipo de Conexoes
	public static final int	CO_TIPO_TECNOMEN_APR	= 0;
	public static final int	CO_TIPO_TECNOMEN_REC	= 1;
	public static final int	CO_TIPO_TECNOMEN_VOU	= 2;
	public static final int	CO_TIPO_TECNOMEN_ADM	= 3;
	public static final int	CO_TIPO_TECNOMEN_AGE	= 4;
	public static final int	CO_TIPO_BANCO_DADOS_PREP= 5;

	// Componentes de Negocio
	public static final String	CN_APROVISIONAMENTO	= "Componente de Negocio - Aprovisionamento";
	public static final String	CN_CONSULTA			= "Componente de Negocio - Consulta";
	public static final String	CN_RECARGA			= "Componente de Negocio - Recarga";
	public static final String	CN_PROCESSOSBATCH	= "Componente de Negocio - Processos Batch";
	public static final String	CN_GERENTEGPP		= "Componente de Negocio - Gerente GPP";

	// Classes
	public static final String	CL_GERENTE_BANCO_DADOS 				= "GerentePoolBancoDados";
	public static final String	CL_GERENTE_TECNOMEN 				= "GerentePoolTecnomen";
	public static final String	CL_CONSULTA_VOUCHER 				= "ConsultaVoucher";
	public static final String	CL_CONSULTA_ASSINANTE 				= "ConsultaAssinante";
	public static final String  CL_CONSULTA_HISTORICO_PROC_BATCH    = "ConsultaHistoricoProcessosBatch";
	public static final String	CL_APROVISIONAR						= "Aprovisionar";
	public static final String	CL_RECARREGAR						= "Recarregar";
	public static final String 	CL_AJUSTAR							= "Ajustar";
	public static final String 	CL_ENVIO_SMS						= "EnvioSMS";
	public static final String  CL_PRODUTOR_SMS						= "ProdutorSMS";
	public static final String  CL_CONSUMIDOR_SMS					= "ConsumidorSMS";
	public static final String  CL_CICLO_PLANO_HIBRIDO				= "CicloPlanoHibrido";
	public static final String  CL_RECARGA_RECORRENTE				= "RecargaRecorrente";
	public static final String 	CL_ENVIO_USUARIO_SHUTDOWN 			= "EnvioUsuariosShutdown";
	public static final String	CL_ENVIO_USUARIO_NORMAL				= "EnviarUsuariosStatusNormal";
	public static final String	CL_ENVIO_USUARIO_NORMAL_PROD    	= "EnviarUsuariosStatusNormalProdutor";
	public static final String	CL_ENVIO_USUARIO_NORMAL_CONS		= "EnviarUsuariosStatusNormalConsumidor";
	public static final String	CL_ENVIO_USUARIO_NORMAL_USER		= "EnvioUsuariosNormalUser";
	public static final String	CL_ENVIO_USUARIO_RECHARGE_EXPIRED	= "EnvioUsuariosStatusRechargeExpired";
	public static final String  CL_ENVIO_INFOS_CARTAO_UNICO         = "EnvioInfosCartaoUnico";
	public static final String 	CL_ENVIO_INFOS_RECARGA  			= "EnvioInfosRecarga";
	public static final String 	CL_ENVIO_BONUS_CSP14  				= "EnvioBonusCSP14";
	public static final String 	CL_LIBERA_BONUS_CSP14  				= "LiberaBonusCSP14";
	public static final String 	CL_ESTORNO_BONUS_SOBRE_BONUS 		= "EstornoBonusSobreBonus";
	public static final String 	CL_EMITIR_NF_BONUS_TLDC  			= "EmitirNFBonusTLDC";
	public static final String 	CL_GERENTE_FELIZ					= "GerenteFeliz";
	public static final String	CL_MAPEAMENTOS						= "Mapeamentos";
	public static final String	CL_MAPEAMENTO_PLANO_PRECO 			= "MapPlanoPreco";
	public static final String  CL_MAPEAMENTO_STATUS_ASSINANTE		= "MapStatusAssinante";
	public static final String  CL_MAPEAMENTO_STATUS_SERVICO		= "MapStatusServico";
	public static final String  CL_MAPEAMENTO_MOTIVO_BLOQUEIO		= "MapMotivoBloqueioAssinante";
	public static final String  CL_MAPEAMENTO_TARIFA_TROCAMSISDN	= "MapTarifaTrocaMSISDN";
	public static final String  CL_MAPEAMENTO_SISTEMA_ORIGEM		= "MapSistemaOrigem";
	public static final String  CL_MAPEAMENTO_CONFIGURACAO_GPP		= "MapConfiguracaoGPP";
	public static final String  CL_MAPEAMENTO_REC_VALORES			= "MapValoresRecarga";
	public static final String  CL_MAPEAMENTO_REC_ORIGEM			= "MapRecOrigem";
	public static final String  CL_ENVIO_REC_CONCILIACAO			= "Conciliar";
	public static final String  CL_TRATAR_VOUCHER					= "ExecutaTratamentoVoucher";
	public static final String  CL_GERAR_EXTRATO					= "GerarExtrato";
	public static final String  CL_IMPORTACAO_DADOS_CDR				= "ImportaArquivosCDR";
	public static final String  CL_EXPORTACAO_DADOS_DW				= "ExportarTabelasDW";
	public static final String  CL_IMPORTACAO_ASSINANTES			= "ImportaAssinantes";
	public static final String	CL_GERENTE_GPP						= "Gerente GPP";
	public static final String	CL_FINALIZA_GPP						= "FinalizaGPP";
	public static final String	CL_CONTESTAR_COBRANCA				= "ContestacaoCobranca";
	public static final String	CL_COMPROVANTE_SERVICO				= "ComprovanteServico";
	public static final String 	CL_DIAS_SEM_RECARGA					= "CalculoDiasSemRecarga";
	public static final String  CL_IMPORTACAO_USUARIOS				= "ImportaUsuarioPortalNDS";
	public static final String 	CL_SUMARIZACAO_AJUSTES				= "SumarizacaoAjustes";
	public static final String 	CL_SUM_PRODUTO_PLANO				= "SumarizacaoProdutoPlano";
	public static final String 	CL_CONEXAO_MIDDLEWARE_SMSC			= "ConexaoMiddewareSMSC";
	public static final String  CL_TRANSFERE_PEDIDOS_VOUCHER		= "TransferePedidoVoucher";
	public static final String  CL_ARQUIVO_PEDIDO_VOUCHER_PARSER	= "ArquivoPedidoVoucherParser";
	public static final String  CL_ACERTA_PEDIDO_VOUCHER			= "AcertaPedidoVoucher";
	public static final String  CL_BATIMENTO_PEDIDO_VOUCHER			= "BatimentoPedidoVoucher";
	public static final String  CL_ATIVA_PEDIDO_VOUCHER				= "AtivaPedidoVoucher";
	public static final String  CL_TRANSMITE_PEDIDO_VOUCHER         = "TransmitePedidoVoucher";
	public static final String  CL_ENVIA_PEDIDOS_VOUCHER_EMAIL      = "EnviaPedidosVoucherPorEmail";
	public static final String  CL_SUMARIZACAO_CONTABIL				= "SumarizacaoContabil";
	public static final String 	CL_CONSOLIDACAO_SCR					= "EnviarConsolidacaoSCR";
	public static final String	CL_INDICE_BONIFICACAO				= "CalcularIndiceBonificacao";
	public static final String	CL_BLOQUEIO_POR_SALDO				= "BloquearPorSaldo";
	public static final String  CL_PROCESSA_VOUCHERS_UTILIZADOS		= "ProcessaVouchersUtilizados";
	public static final String 	CL_CONSOLIDACAO_AJUSTES				= "ConsolidacaoAjustes";
	public static final String 	CL_CONSOLIDACAO_CONTABIL			= "ConsolidacaoContabil";
	public static final String  CL_GERENCIADOR_ARQUIVOS_CDR			= "GerenciadorArquivosCDR";
	public static final String  CL_PROCESSA_ARQUIVO_CDR_DADOSVOZ	= "ProcessaArquivoCDRDadosVoz";
	public static final String  CL_PROCESSA_ARQUIVO_CDR_APRREC		= "ProcessaArquivoCDRAprRec";
	public static final String  CL_BATIMENTO_ARQUIVO_CDR			= "BatimentoArquivoCDR";
	public static final String	CL_GSOCKET							= "Classe GSocket";
	public static final String  CL_ENVIA_CONTINGENCIA_CRM_PENDENTE	= "EnviarContingenciaCRMPendente";
	public static final String  CL_GERAR_EXTRATO_PULA_PULA			= "GerarExtratoPulaPula";
	public static final String  CL_GERENTE_PRODUTOR_RECARGA			= "GerenteProdutorRecarga";
	public static final String  CL_GERENTE_CONSUMIDOR_RECARGA		= "GerenteConsumidorRecarga";
	public static final String  CL_CONSUMO_RECARGA					= "ConsumoRecarga";
	public static final String	CL_ANALISAR_INCONSISTENCIAS			= "AnalisarInconsistencias";
	public static final String  CL_GERADOR_ARQUIVOS					= "GeradorArquivos";
	public static final String	CL_IMPORTACAO_PEDIDOS_VOUCHER		= "ImportacaoPedidosVoucher";
	public static final String	CL_GERENCIA_PEDIDOS_VOUCHER			= "GerenciaPedidosVoucher";
	public static final String  CL_CONSULTA_WIG                     = "ConsultaWIG";
	public static final String  CL_ATIVACAO_VOUCHER_FISICO			= "AtivacaoVoucherFisico";
	public static final String  CL_CONEXAO_MASC						= "ConexaoMASC";
	public static final String  CL_GERAR_BLOQUEIO_DESBLOQUEIO		= "GerarBloqueioDesbloqueio";
	public static final String	CL_IMPORTACAO_ENTRADA_ETI			= "ImportacaoEntradaEti";
	public static final String  CL_ATT_LIMITE_CREDITO				= "AtualizarLimiteCredito";
	public static final String  CL_RECARGA_MICROSIGA                = "RecargaMicrosiga";
	public static final String  CL_GERAR_EXTRATO_BOOMERANG			= "GerarExtratoBoomerang";
	public static final String	CL_GERAR_ARQUIVO_COBILLING			= "GerarArquivoCobilling";
	public static final String  CL_ENVIO_DADOS_PULA_PULA_DW			= "EnvioDadosPulaPulaDW";
	public static final String  CL_CONSULTA_RECARGAS_PERIODO		= "ConsultaRecargasPeriodo";
	public static final String  CL_NOVO_CICLO						= "NovoCiclo";
	public static final String	CL_CONSULTA_RELATORIO				= "ConsultaRelatorio";
	public static final String	CL_RELATORIO_FACTORY				= "RelatorioFactory";
	public static final String	CL_GERADOR_RELATORIO				= "GeradorRelatorio";
	public static final String	CL_GERADOR_RESULTADO_DELIMITED		= "GeradorResultadoDelimited";
	public static final String	CL_PROCESSADOR_CONSULTA_PREP		= "ProcessadorConsultaPREP";
	public static final String	CL_GERENTE_TESTE_SMS				= "GerenciadorSMS";
	public static final String	CL_CONEXAO_SMS						= "ConexaoSMS";
	public static final String	CL_APROVISIONAMENTO_MMS				= "AprovisionamentoMMS";
	public static final String	CL_IMPORTACAO_ASSINANTES_MMS		= "ImportacaoAssinantesMMS";
	public static final String	CL_ENVIO_ASSINANTES_MMS				= "EnvioAssinantesMMS";
	public static final String	CL_SUMARIZACAO_ASSINANTES_SHUTDOWN	= "SumarizacaoAssinantesShutdown";
	public static final String  CL_NOTIFICACAO_EXPIRACAO			= "NotificacaoExpiracao";
	public static final String	CL_PROMOCAO_GERENTE					= "GerentePromocao";
	public static final String	CL_PROMOCAO_GERENTE_PULA_PULA		= "GerentePulaPula";
	public static final String	CL_PROMOCAO_CONTROLE				= "ControlePromocao";
	public static final String	CL_PROMOCAO_CONTROLE_PULA_PULA		= "ControlePulaPula";
	public static final String	CL_PROMOCAO_PERSISTENCIA_CONSULTA	= "PromocaoConsulta";
	public static final String	CL_PROMOCAO_PERSISTENCIA_OPERACOES	= "PromocaoOperacoes";
	public static final String	CL_PROMOCAO_PERSISTENCIA_SELECAO	= "SelecaoAssinantes";
	public static final String	CL_PROMOCAO_PERSIST_SELECAO_PULA	= "SelecaoPulaPula";
	public static final String	CL_PROMOCAO_PERSIST_SELECAO_EXTRATO	= "SelecaoExtratoPulaPula";
	public static final String	CL_PROMOCAO_PERSIST_SELECAO_ESTORNO	= "SelecaoEstornoPulaPula";
	public static final String	CL_PROMOCAO_SUMARIZACAO_RECARGAS	= "SumarizacaoRecargasAssinantes";
	public static final String	CL_PROMOCAO_THREAD_PULA_PULA		= "ThreadPulaPula";
	public static final String	CL_PROMOCAO_ESTORNO_PULA_PULA_PROD	= "EstornoPulaPulaProdutor";
	public static final String	CL_PROMOCAO_ESTORNO_PULA_PULA_CONS	= "EstornoPulaPulaConsumidor";
	public static final String	CL_CONSULTA_ESTORNO_PULA_PULA		= "ConsultaEstornoPulaPula";
	public static final String	CL_RELATORIOS_PRODUTOR 				= "RelatoriosProdutor";
	public static final String	CL_RELATORIOS_CONSUMIDOR 			= "RelatoriosConsumidor";
	public static final String	CL_ENVIO_USUARIO_DISCONNECTED		= "EnvioUsuariosDisconnected";
	public static final String	CL_ALTERA_PREATIVO					= "AlteraPreAtivo";
	public static final String	CL_PRORROGACAO_EXPIRACAO_HIBRIDO	= "ProrogacaoExpiracaoHibrido";
	public static final String  CL_GER_INSCRICAO_ASSIN_CAMP_PROD	= "GerInscricaoCampanhaProdutor";
	public static final String  CL_GER_CONCESSAO_CREDITOS_CAMP_PROD	= "GerConcessaoCampanhaProdutor";
	public static final String  CL_GER_INSCRICAO_ASSIN_CAMP_CONS	= "GerInscricaoCampanhaConsumidor";
	public static final String  CL_GER_CONCESSAO_CREDITOS_CAMP_CONS	= "GerConcessaoCampanhaConsumidor";
	public static final String	CL_ASSINANTES_RE_PULA_PULA_PROD		= "AssinantesREPulaPulaProdutor";
	public static final String	CL_ASSINANTES_RE_PULA_PULA_CONS		= "AssinantesREPulaPulaConsumidor";
	public static final String	CL_ENVIO_DADOS_CONTABEIS_DW			= "EnvioDadosContabeisDWProdutor";
	public static final String	CL_ENVIO_SMS_ATRASO_REC_PROD		= "EnvioSMSAtrasoRecargasProdutor";
	public static final String	CL_ENVIO_SMS_ATRASO_REC_CONS		= "EnvioSMSAtrasoRecargasConsumidor";
	public static final String	CL_ENVIO_SMS_ATRASO_PULA_PULA_PROD	= "EnvioSMSAtrasoPulaPulaProdutor";
	public static final String	CL_ENVIO_SMS_ATRASO_PULA_PULA_CONS	= "EnvioSMSAtrasoPulaPulaConsumidor";
	public static final String	CL_IMPORTACAO_GENEVA				= "ImportacaoControle";
	public static final String	CL_SUMARIZACAO_PULA_PULA			= "SumarizacaoPulaPula";
	public static final String	CL_SUMARIZACAO_PULA_PULA_PROD		= "SumarizacaoPulaPulaProdutor";
	public static final String	CL_SUMARIZACAO_PULA_PULA_CONS		= "SumarizacaoPulaPulaConsumidor";
	public static final String	CL_SELECAO_SUMARIZACAO_PULA_PULA	= "SelecaoSumarizacaoPulaPula";
	public static final String	CL_PROMOCAO_GER_ATIV_PROD			= "GerenciadorAtivacaoProdutor";
	public static final String	CL_PROMOCAO_GER_ATIV_CONS			= "GerenciadorAtivacaoConsumidor";
	public static final String	CL_PROMOCAO_GER_PEND_REC_PROD		= "GerenciadorPendenciaRecargaProdutor";
	public static final String	CL_PROMOCAO_GER_PEND_REC_CONS		= "GerenciadorPendenciaRecargaConsumidor";
	public static final String	CL_EXPIRACAO_SALDO_CONTROLE			= "ExpiracaoSaldoControle";
	public static final String	CL_RELATORIO_CHURN					= "RelatorioChurnProdutor";
	public static final String	CL_CONCESSAO_CREDITO_TERCEIRO		= "ConcessaCreditoTerceiro";
	public static final String	CL_AUTO_REBARBA_PROD				= "AutomatizacaoRebarbaProdutor";
	public static final String	CL_AUTO_REBARBA_CONS				= "AutomatizacaoRebarbaConsumidor";
	public static final String	CL_PRODUTOR_ARQUIVO					= "ProdutorArquivo";
	public static final String	CL_CONSUMIDOR_ARQUIVO				= "ConsumidorArquivo";
	public static final String	CL_PRODUTOR_FABRICA					= "ProdutorFabrica";
	public static final String	CL_CONSUMIDOR_FABRICA				= "ConsumidorFabrica";
	public static final String	CL_EXPORTACAO_BATE_VOLTA			= "ExportacaoBateVolta";
	public static final String	CL_IMPORTACAO_POSPAGO				= "ImportaPospago";
	public static final String	CL_APROVACAO_LOTE					= "AprovacaoLote";
	public static final String	CL_CARGA_LOTES_PRODUTOR				= "CargaLotesProdutor";
	public static final String	CL_CARGA_LOTES_CONSUMIDOR			= "CargaLotesConsumidor";
	public static final String  CL_GRAVA_CONTRATO_SAC_RECARGA		= "GravaContratoSACRecarga";
	public static final String  CL_INSERIR_REGISTROS_PUSH			= "InserirRegistrosConfirmacaoPush";
	public static final String  CL_DESATIVA_ASSINANTE				= "DesativaAssinante";
	public static final String  CL_PRODUTOR_FABRICA_SQL				= "ProdutorFabricaSQL";
	public static final String  CL_GERENTE_CRIPTOGRAFIA				= "GerenteCriptografia";
	public static final String  CL_GER_REMOCAO_FALE_GRATIS_PROD 	= "GerRemocaoFaleGratisProdutor";
	public static final String  CL_GER_REMOCAO_FALE_GRATIS_CONS 	= "GerRemocaoFaleGratisConsumidor";
	public static final String	CL_GER_INSERCAO_FALE_GRATIS_PROD 	= "GerInsercaoFaleGratisProdutor";
	public static final String	CL_GER_INSERCAO_FALE_GRATIS_CONS 	= "GerInsercaoFaleGratisConsumidor";
	public static final String  CL_CONCESSAO_FRANQUIA_CONTROLE_TOTAL_PRODUTOR	= "ConcessaoFranquiaProdutor";
	public static final String  CL_CONCESSAO_FRANQUIA_CONTROLE_TOTAL_CONSUMIDOR	= "ConcessaoFranquiaConsumidor";
    public static final String  CL_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL_PRODUTOR   = "ConcessaoFranquiaProdutor";
    public static final String  CL_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL_CONSUMIDOR = "ConcessaoFranquiaConsumidor";
    public static final String  CL_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL_DAO        = "ConcessaoFranquiaProdutor";
	public static final String  CL_ATUALIZA_RETORNO_CICLO_PRODUTOR  			= "AtualizaRetornoDeCicloProdutor";
	public static final String  CL_ATUALIZA_RETORNO_CICLO_CONSUMIDOR			= "AtualizaRetornoDeCicloConsumidor";
	public static final String  CL_RETOMA_CICLO_TRES_PRODUTOR					= "RetomaCicloTresProdutor";
	public static final String  CL_RETOMA_CICLO_TRES_CONSUMIDOR 				= "RetomaCicloTresConsumidor";
	public static final String	CL_IMPORTACAO_RECARGA_MASSA_PRODUTOR 			= "ImportacaoRecargaMassaProdutor";
	public static final String	CL_IMPORTACAO_RECARGA_MASSA_CONSUMIDOR 			= "ImportacaoRecargaMassaConsumidor";
	public static final String	CL_CONCESSAO_RECARGA_MASSA						= "ConcessaoRecargaMassa";
	public static final String	CL_RECARGA_MASSA_DAO							= "RecargaMassaDAO";
	public static final String	CL_ATUALIZADOR_ASSINANTES_PRODUTOR				= "AtualizadorAssinantesProdutor";
	public static final String	CL_ATUALIZADOR_ASSINANTES_CONSUMIDOR			= "AtualizadorAssinantesConsumidor";
	public static final String  CL_GER_PACOTE_DADOS_PROD						= "GerentePacoteDadosProdutor";
	public static final String  CL_GER_PACOTE_DADOS_CONS						= "GerentePacoteDadosConsumidor";
	public static final String	CL_ENVIO_USUARIO_STATUS_PRODUTOR				= "EnvioUsuariosStatusProdutor";
	public static final String	CL_ENVIO_USUARIO_STATUS_CONSUMIDOR				= "EnvioUsuariosStatusConsumidor";
	public static final String  CL_PROMOCAO_SUMARIZACAO_RECARGAS_PROD           = "SumarizacaoRecargasAssinantesProd";
	public static final String  CL_PROMOCAO_SUMARIZACAO_RECARGAS_CONS           = "SumarizacaoRecargasAssinantesCons";
	public static final String  CL_INCENTIVO_RECARGA_PRODUTOR					= "IncentivoRecargaProdutor";
	public static final String  CL_INCENTIVO_RECARGA_CONSUMIDOR					= "IncentivoRecargaConsumidor";
    public static final String  CL_CONSOLIDACAO_CONTABIL_MENSAL_PRODUTOR        = "ConsolidacaoContabilMensalProdutor";
    public static final String  CL_SISTEMA_TARIFACAO_DEST_REGION_CONSUMIDOR     = "DestinationRegionConsumidor";
    public static final String  CL_SISTEMA_TARIFACAO_DEST_REGION_PRODUTOR       = "DestinationRegionProdutor";
    public static final String  CL_SISTEMA_TARIFACAO_HOME_REGION_CONSUMIDOR     = "HomeRegionConsumidor";
    public static final String  CL_SISTEMA_TARIFACAO_HOME_REGION_PRODUTOR       = "HomeRegionProdutor";
    public static final String  CL_SISTEMA_TARIFACAO_SPEC_SERV_TRANL_CONSUMIDOR = "SpecialServiceTranslationConsumidor";
    public static final String  CL_SISTEMA_TARIFACAO_SPEC_SERV_TRANL_PRODUTOR   = "SpecialServiceTranslationProdutor";
    public static final String  CL_SISTEMA_TARIFACAO_REQ_ENGINE_PRODUTOR        = "RequisicaoEngineProdutor";
    public static final String  CL_LIBERA_BONUS_INCENTIVO_RECARGA				= "LiberacaoBonusIncentivoRecarga";
    public static final String  CL_RESULTADO_CONTESTACAO_PRODUTOR 				= "ResultadoContestacaoProdutor";
    public static final String  CL_RESULTADO_CONTESTACAO_CONSUMIDOR 			= "ResultadoContestacaoConsumidor";
    public static final String  CL_GPP_EXTRATO_DELEGATE_PRODUTOR 				= "GPPExtratoDelegateProdutor";
    public static final String  CL_GPP_EXTRATO_DELEGATE_CONSUMIDOR 				= "GPPExtratoDelegateConsumidor";
    public static final String  CL_GPP_EXTRATO_ARQUIVO_PRODUTOR 				= "GPPExtratoArquivoProdutor";
    public static final String  CL_GPP_EXTRATO_ARQUIVO_CONSUMIDOR 				= "GPPExtratoArquivoConsumidor";

	// Erros Tecnomen
	public static final int  ERRO_NORMAL								= 0; // User Error
	public static final int  ERRO_Notify_Admin							= 1; // System Error

	public static final int  ERRO_NOT_FOUND_ERROR						= 0;  // Record Not Found
	public static final int  ERRO_RESERVED_ERROR_1						= 1;  // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_2						= 2;  // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_3						= 3;  // Internal Use Only
	public static final int  ERRO_FILE_CREATE_ERROR						= 4;  // Cannot create file on server
	public static final int  ERRO_FILE_READ_ERROR						= 5;  // Cannot read file on server
	public static final int  ERRO_DATABASE_ERROR						= 6;  // General database problem
	public static final int  ERRO_INVALID_PASSWORD						= 7;  // Incorrect password supplied
	public static final int  ERRO_INVALID_LOGIN_DETAILS					= 8;  // Invalid user name or password
	public static final int  ERRO_INVALID_LOGIN_NAME					= 8;  // Invalid user login id supplied
	public static final int  ERRO_INVALID_VALUE							= 9;  // Cannot recharge by this amount
	public static final int  ERRO_INVALID_VOUCHER						= 10; // Voucher recharge not accepted - Invalid Voucher
	public static final int  ERRO_LOGIN_IN_USE							= 11; // This Login name is already being used
	public static final int  ERRO_NO_CDRS_AVAILABLE						= 12; // No CDRs stored for this subscriber
	public static final int  ERRO_RESERVED_ERROR_13						= 13; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_14						= 14; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_15						= 15; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_16						= 16; // Internal Use Only
	public static final int  ERRO_GENERAL_ERROR							= 17; // General Server Error
	public static final int  ERRO_VOUCHER_ALREADY_USED					= 18; // Cannot re-use a voucher
	public static final int  ERRO_RESERVED_ERROR_19						= 19; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_20						= 20; // Internal Use Only
	public static final int  ERRO_INCOMPLETE_STRUCT						= 21; // IDL struct recieved was not fully populated by the client
	public static final int  ERRO_RESERVED_ERROR_22						= 22; // Internal Use Only
	public static final int  ERRO_OP_NOT_IMPLEMENTED					= 23; // Operation not yet implemented
	public static final int  ERRO_FILE_ERROR							= 24; // Server file error - examine log
	public static final int  ERRO_INCOMPLETE_LOGIN						= 25; // Missing login or user name
	public static final int  ERRO_MAXIMUM_USER_LIMIT					= 26; // Max number of users logged in
	public static final int  ERRO_RESERVED_ERROR_27						= 27; // Internal Use Only
	public static final int  ERRO_SUBSCRIBER_DENIED						= 28; // Subscriber does'nt have permission for this operation
	public static final int  ERRO_RESERVED_ERROR_29						= 29; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_30						= 30; // Internal Use Only
	public static final int  ERRO_INVALID_USER_ID						= 31; // Invalid user id number
	public static final int  ERRO_MISSING_ESN							= 32; // No ESN number supplied
	public static final int  ERRO_ESN_IN_USE							= 33; // ESN number already in use by other subscriber
	public static final int  ERRO_RESERVED_ERROR_34						= 34; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_35						= 35; // Internal Use Only
	public static final int  ERRO_CREDIT_LIMIT_EXCEEDED					= 36; // This recharge would exceed the max allowed credit
	public static final int  ERRO_VOUCHER_ALREADY_USED_BY 				= 37;// Voucher already used by Subscriber
	public static final int  ERRO_ESN_IN_USE_BY							= 38; // ESN number already in use by Subscriber
	public static final int  ERRO_SUB_NUMB_TOO_SHORT					= 39; // Subscriber Number too short for CDR file
	public static final int  ERRO_SUB_EXISTS_IN_CACHE					= 40; // Subscriber currently in cache
	public static final int  ERRO_RESERVED_ERROR_41						= 41; // Internal Use Only
	public static final int  ERRO_RESERVED_ERROR_42						= 42; // Internal Use Only
	public static final int  ERRO_SMID_IN_USE							= 43; // SM number already in use
	public static final int  ERRO_VOUCHER_NOT_FOUND						= 44; // Invalid Voucher not found
	public static final int  ERRO_ACCOUNT_BLOCKED						= 45; // Subscriber Account Blocked
	public static final int  ERRO_INVALID_VOUCHER_LENGTH				= 46; // Invalid Voucher length
	public static final int  ERRO_VOUCHER_TRACK_NO						= 47; // Voucher Tracking not allowed
	public static final int  ERRO_INVALID_SMID							= 48; // SM number not valid
	public static final int  ERRO_BLOCK_RECHARGE						= 49; // Recharge Blocked, too many Vouchers in Queue
	public static final int  ERRO_RESERVED_ERROR_50						= 50; // Timestamps from the loadme to update were not equal
	public static final int  ERRO_NO_SHUTDOWN_RECHARGE					= 51; // Cannot Recharge a Subscriber in a state of Shutdown
	public static final int  ERRO_AMOUNT_LE_SURCHARGE					= 52; // Recharge Failed, Ammount lessthan Surcharge
	public static final int  ERRO_INVALID_FF_PHONE_NUMBERS_LENGTH 		= 53; // Invalid length of F&F Phone Numbers String
	public static final int  ERRO_INVALID_FF_PHONE_NUMBER_CHARACTERS 	= 54; //Invalid F&F Characters
	public static final int  ERRO_INVALID_LENGTH						= 55; // Invalid Length
	public static final int  ERRO_RESERVED_ERROR_56						= 56; // Internal Use Only
	public static final int  ERRO_UPDATE_MIN_FAILURE 					= 57; // updateMIN Failed
	public static final int  ERRO_BATCH_NOT_ISSUED						= 58; // Can only activate batches issued to you
	public static final int  ERRO_NON_ISSUED_ACTIVATE					= 59; // Can only activate an issued batch
	public static final int  ERRO_VOUCHER_ACTIVATE_ERROR				= 60; // Could not commit activation of vouchers
	public static final int  ERRO_BOX_PENDING_TRAN						= 61; // Transaction already pending on this box
	public static final int  ERRO_INVALID_BATCH_NO						= 62; // No Such Batch
	public static final int  ERRO_INVALID_BOX_NO						= 63; // No Such Box
	public static final int  ERRO_INVALID_USER_TYPE						= 64; // Invalid User Type
	public static final int  ERRO_VOUCHER_DOES_NOT_EXIST				= 65; // Voucher does not exist
	public static final int  ERRO_INCORRECT_VOUCHER_STATE 				= 66; // Voucher is not in an Activated State

	// Erros Tecnomen - IDL Gateway specific error codes
	public static final int  ERRO_MAX_TRANSACTIONS_EXCEEDED = 100; // operation limit reached
	public static final int  ERRO_MISSING_KEY				= 101; // Required key data not given
	public static final int  ERRO_UNEXPECTED_ID				= 102; // Usage error
	public static final int  ERRO_UNEXPECTED_TYPE			= 103; // Usage error
	public static final int  ERRO_USER_TYPE					= 104; // not available for this user type

	// Erros Tecnomen - IDL Gateway - Múltiplos Saldos
	public static final int  ERRO_RECH_BONUS_NOT_CREATED	= 139;  // Recharge Bonus not Created Error
	public static final int  ERRO_NO_RECH_BONUS_ERROR		= 140;	// No Recharge Bonus Error
	public static final int  ERRO_RECH_BONUS_NOT_UPDATED	= 141;	// Recharge Bonus not Updated Error
	public static final int  ERRO_RECH_BONUS_NOT_DELETED	= 142;	// Recharge Bonus not Deleted Error
	public static final int  ERRO_RECH_BONUS_MAX_PERC		= 143;	// Recharge Bonus Max Percent Error
	public static final int  ERRO_FATAL_ERROR				= 210;	// Fatal Error
	public static final int  ERRO_PROGRAMMING				= 220;	// Programming Error

	public static final int  ERRO_TOO_MANY_BONUS_CONFIG		= 8533; // Muitas configuracoes de bonus

	//Status do assinante.
	public static final int STATUS_FIRST_TIME_USER		= 1;
	public static final int STATUS_NORMAL_USER			= 2;
	public static final int STATUS_RECHARGE_EXPIRED		= 3;
	public static final int STATUS_DISCONNECTED			= 4;
	public static final int STATUS_SHUTDOWN				= 5;

	//Status periodico do assinante.
	public static final short STATUS_PERIODICO_NAO_APLICAVEL		= 0;
	public static final short STATUS_PERIODICO_FIRST_TIME_USER		= 1;
	public static final short STATUS_PERIODICO_NORMAL_USER			= 2;
	public static final short STATUS_PERIODICO_PARTIALLY_BLOCKED	= 3;
	public static final short STATUS_PERIODICO_BLOCKED				= 4;

	//STATUS DOS USUARIOS
	public static final short FIRST_TIME_USER	= 	1;
	public static final short NORMAL			=	2;
	public static final short RECHARGE_EXPIRED	=	3;
	public static final short DISCONNECTED		=	4;
	public static final short SHUT_DOWN			=	5;

	// Constantes para consulta de Assinante
	public static final int CONSULTA_ASSINANTE_SIMPLES 	= 0;
	public static final int CONSULTA_ASSINANTE_COMPLETA = 1;

	//STATUS DOS ASSINANTES
	public static final String S_FIRST_TIME_USER	= 	"F";
	public static final String S_NORMAL				=	"N";
	public static final String S_RECHARGE_EXPIRED	=	"R";
	public static final String S_DISCONNECTED		=	"D";
	public static final String S_SHUT_DOWN			=	"S";
	public static final String S_RET_FGN1_FGN2		=	"V"; // Volta

	//STATUS DE SERVICO
	public static final int SERVICO_DESBLOQUEADO			=	1;
	public static final int SERVICO_SUSPENSO				=	2;
	public static final int SERVICO_ROUBADO					=	3;
	public static final int SERVICO_SUSPENSO_RECARGA1		=	4;
	public static final int SERVICO_SUSPENSO_RECARGA2		=	5;
	public static final int SERVICO_SUSPENSO_VALOR_MAX		=	6;
	public static final int SERVICO_SUSPENSO_PIN_INVALIDO	=	7;
	public static final int SERVICO_BLOQUEADO				=	8;

	//STATUS SERVIÇO STRINGS
	public static final String S_SERVICO_DESBLOQUEADO			=	"Status Servico Desbloqueado";
	public static final String S_SERVICO_SUSPENSO				=	"Status Servico Suspenso";
	public static final String S_SERVICO_ROUBADO				=	"Status Servico Roubado";
	public static final String S_SERVICO_SUSPENSO_RECARGA1		=	"Status Servico Suspenso Erro Recarga";
	public static final String S_SERVICO_SUSPENSO_RECARGA2		=	"Status Servico Suspenso Erro Recarga";
	public static final String S_SERVICO_SUSPENSO_VALOR_MAX		=	"Status Servico Valor Máximo Atingido";
	public static final String S_SERVICO_SUSPENSO_PIN_INVALIDO	=	"Status Servico PIN Invalido";
	public static final String S_SERVICO_BLOQUEADO				=	"Status Servico Bloqueado";

	//STATUS DE VOUCHERS
	public static final int VOUCHER_ATIVO					= 1;
	public static final int VOUCHER_EXPIRADO				= 2;
	public static final int VOUCHER_USADO					= 3;
	public static final int VOUCHER_AINDA_NAO_ATIVO			= 5;
	public static final int VOUCHER_INVALIDADO				= 11;

	public static final String S_VOUCHER_ATIVO				= "Ativo";
	public static final String S_VOUCHER_EXPIRADO			= "Expirado";
	public static final String S_VOUCHER_USADO				= "Usado";
	public static final String S_VOUCHER_AINDA_NAO_ATIVO	= "Ainda nao ativo";
	public static final String S_VOUCHER_INVALIDADO			= "Invalidado";

	//	Codigos de Atividades para Voucher
	public static final int	IND_ATIVAR_VOUCHER 				= 1;
	public static final int	IND_DESATIVAR_VOUCHER			= 2;
	public static final short IND_ERRO_VOUCHER				= 9;
	public static final short IND_ACERTO_VOUCHER			= 0;
	public static final String	IND_S_ATIVAR_VOUCHER 		= "A";
	public static final String	IND_S_DESATIVAR_VOUCHER		= "D";
	public static final int	IND_DESATIVAR_VOUCHER_ATIVO		= 3;
	public static final int	IND_DESATIVAR_VOUCHER_ISSUED	= 4;
	public static final int	IND_ENVIO_XML_OK				= 0;
	public static final int	IND_ENVIO_XML_NOK				= 1;
	public static final int	MAX_TENTATIVAS_CONSULTA_VOUCHER	= 100;
	public static final short TEMPO_ATIVACAO_BATCH 			= 5000;

	public static final int SMS_PRIORIDADE_ZERO	   = 0;
	public static final int SMS_PRIORIDADE_UM	   = 1;

	// Tipos de SMS
	public static final String SMS_RECARGA         = "RECARGA";
	public static final String SMS_BOMERANGUE14    = "BOMERANGUE14";
	public static final String SMS_AJUSTE          = "AJUSTE";
	public static final String SMS_TROCA_PLANO     = "TROCA PLANO";
	public static final String SMS_CONTESTACAO     = "CONTESTACAO";
	public static final String SMS_INF_STATUS      = "INF_STATUS";
	public static final String SMS_BROADCAST       = "BROADCAST";
	public static final String SMS_FGN_ATIVACAO    = "FGN_ATIVACAO";
	public static final String SMS_FGN_EXPIRACAO   = "FGN_EXPIRACAO";
	public static final String FGN_REATIVACAO      = "FGN_REATIVACAO";
	public static final String SMS_ATRASO_PULAPULA = "ATRASO_PULAPULA";
	public static final String SMS_CALLMEBACK	   = "CALLMEBACK";
	public static final String SMS_PACOTE_DADOS	   = "PACOTE_DADOS";

	// Tipos de eventos de aprovisionamento
	public static final String TIPO_APR_ATIVACAO						= "ATIVACAO";
	public static final String TIPO_APR_DESATIVACAO						= "DESATIVACAO";
	public static final String TIPO_APR_BLOQUEIO						= "BLOQUEIO";
	public static final String TIPO_APR_DESBLOQUEIO						= "DESBLOQUEIO";
	public static final String TIPO_APR_TROCA_PLANO						= "TROCA_PLANO";
	public static final String TIPO_APR_TROCA_SIMCARD 					= "TROCA_SIMCARD";
	public static final String TIPO_APR_TROCA_MSISDN					= "TROCA_MSISDN";
	public static final String TIPO_APR_TROCA_SENHA						= "TROCA_SENHA";
	public static final String TIPO_APR_ATUALIZA_FF						= "ATUALIZA_FF";
	public static final String TIPO_APR_SHUTDOWN						= "STATUS_SHUTDOWN";
	public static final String TIPO_APR_STATUS_NORMAL 					= "STATUS_NORMAL";
	public static final String TIPO_APR_STATUS_FIRSTIME 				= "STATUS_FIRST_TIME";
	public static final String TIPO_APR_STATUS_RECHARGE_EXPIRED 		= "STATUS_RECHARGE_EXPIRED";
	public static final String TIPO_APR_STATUS_DISCONNECTED				= "STATUS_DISCONNECTED";
	public static final String TIPO_APR_BLOQUEIO_FRAUDE 				= "BLOQUEIO_FRAUDE";
	public static final String TIPO_APR_BLOQUEIO_ERRO_LIMITE_RECARGA 	= "BLOQUEIO_ERRO_LIM_RECARGA";
	public static final String TIPO_APR_BLOQUEIO_LIMITE_RECARGA 		= "BLOQUEIO_LIM_RECARGA";
	public static final String TIPO_APR_BLOQUEIO_LIMITE_MAX_PIN 		= "BLOQUEIO_LIM_MAX_PIN";
	public static final String TIPO_APR_PROMOCAO_ENTRADA				= "PROMOCAO_ENTRADA";
	public static final String TIPO_APR_PROMOCAO_SAIDA					= "PROMOCAO_SAIDA";
	public static final String TIPO_APR_PROMOCAO_TROCA					= "PROMOCAO_TROCA";
	public static final String TIPO_APR_PROMOCAO_TROCA_PLANO			= "PROMOCAO_TROCA_PLANO";
	public static final String TIPO_APR_PROMOCAO_TROCA_MSISDN			= "PROMOCAO_TROCA_MSISDN";
	public static final String TIPO_APR_EMISSAO_EXTRATO					= "EMISSAO_EXTRATO";
	public static final String TIPO_APR_EMISSAO_EXTRATO_PULA_PULA		= "EMISSAO_EXTRATO_PULA_PULA";
	public static final String TIPO_APR_ZERAR_SALDOS					= "ZERAR_SALDOS";
	public static final String TIPO_APR_TROCA_STATUS_PERIODICO			= "TROCA_STATUS_PERIODICO";
	public static final String TIPO_APR_TROCA_STATUS_ASSINANTE			= "TROCA_STATUS_ASSINANTE";
	public static final String TIPO_APR_ATUALIZA_AMIGOS_GRATIS          = "ATUALIZA_AMIGOS_GRATIS";

	// Tipos operacoes
	public static final String TIPO_OPER_SUCESSO		= "SUCESSO";
	public static final String TIPO_OPER_ERRO			= "ERRO";
	public static final String TIPO_OPER_PARCIAL        = "PARCIALMENTE OK";

	// Os codigos de modificacao seguem mesma numeracao da documentacao updateSubscriber da Tecnomen
	public static final int	   TIPO_BLOQUEIO		= 1;
	public static final int	   TIPO_TROCA_PLANO		= 9;
	public static final int	   TIPO_ATUALIZA_FF		= 12;
	public static final int	   TIPO_TROCA_SINCARD	= 13;
	public static final int	   TIPO_STATUS_SHUTDOWN	= 14;

	// Tipo de acoes de servicos
	public static final int	   INCLUIR_SERVICO_ASSINANTE = 0;
	public static final int	   EXCLUIR_SERVICO_ASSINANTE = 1;

	//	Os codigos de modificacao seguem mesma numeracao da documentacao IDL Gatweay da Tecnomen
	public static final int	   TIPO_TROCA_SENHA	= 28;

	// Os codigos de atualizacao de status e data de expiracao seguem mesma numeracao da documentacao accountUpdate da Tecnomen
	public static final int	   TIPO_TROCA_STATUS 		 = 1;
	public static final int	   TIPO_TROCA_DATA_EXPIRACAO = 2;

	// Tipos de Recarga Recorrente
	public static final String	TIPO_REC_RECORRENTE_PROGRAMADA 		= "P";
	public static final String	TIPO_REC_RECORRENTE_FRANQUIA 		= "F";
	public static final String  TIPO_REC_RECORRENTE_BONUS			= "B";
	public static final String	TIPO_REC_RECORRENTE_FRANQUIA_BONUS 	= "D";
	public static final String	TIPO_REC_RECORRENTE_BONUS_SMS    	= "E";
	public static final String	TIPO_REC_RECORRENTE_MAES	    	= "M";
	public static final String	TIPO_CONTROLE_TOTAL_FRANQUIA     	= "C";
	
    //Padroes de parametros.
    public static final String PATTERN_DAT_MES			= ":DAT_MES";
    public static final String PATTERN_DATE				= ":DATE";
    public static final String PATTERN_VALOR			= ":VALOR";
    public static final String PATTERN_PROMOCAO			= ":PROMOCAO";
    public static final String PATTERN_FILA_RECARGAS	= ":FILA_RECARGAS";
    public static final String PATTERN_TT_FILA_RECARGAS	= ":TT_FILA_RECARGAS";
    public static final String PATTERN_CONTROLE			= ":CONTROLE";
    public static final String PATTERN_TT_CONTROLE		= ":TT_CONTROLE";
    public static final String PATTERN_TOTAL			= ":TOTAL";
    public static final String PATTERN_SUCESSO			= ":SUCESSO";

    //Mascaras para o formato de data
    public static final String MASCARA_DAT_MES			= "yyyyMM";
    public static final String MASCARA_DAT_DIA			= "yyyyMMdd";
    public static final String MASCARA_DATA_HORA_GPP	= "yyyyMMddHHmmss";
    public static final String MASCARA_DATE				= "dd/MM/yyyy";
    public static final String MASCARA_MES				= "MM/yyyy";
    public static final String MASCARA_TIME				= "HH:mm:ss";
    public static final String MASCARA_TIMESTAMP		= "dd/MM/yyyy HH:mm:ss";
    public static final String MASCARA_DATA_VITRIA		= "yyyy-MM-dd HH:mm:ss";
    public static final String MASCARA_MES_DIA			= "MMdd";

    //Mascaras para formato de valor
    public static final String MASCARA_CODIGO_RETORNO	= "0000";
    public static final String MASCARA_DOUBLE			= "##,##0.00";
    public static final String MASCARA_DOUBLE_S_PONTO	= "####0.00";
    public static final String MASCARA_LONG				= "#,#00";
    public static final String MASCARA_URA				= "##########000";

    //Mascaras de Msisdn
    public static final String MASCARA_GSM_BRT					= "55..8(4|5)......";
    public static final String MASCARA_COM_COD_INTERNACIONAL	= "55..........";
    public static final String MASCARA_SEM_COD_INTERNACIONAL	= "0..........";
    public static final String MASCARA_GSM_BRT_REGEX			= "^55\\d\\d8[45]\\d{6}.*$";
    public static final String MASCARA_TERMINAL_FIXO_REGEX      = "^55\\d\\d[23456]\\d{7}.*$";

	// Transaction Types
	public static final String	RECARGA_PROGRAMADA 					 = "04003";
	public static final String	RECARGA_PONTUAL 					 = "04004";
	public static final String	RECARGA_FRANQUIA 					 = "04005";
	public static final String  RECARGA_FRANQUIA_BONUS               = "08004";
	public static final String  AJUSTE_BONUS_RECARGA        		 = "08018";
	public static final String  AJUSTE_BONUS_ON_NET               	 = "08019";
	public static final String  AJUSTE_BONUS_OFF_NET                 = "08020";
	public static final String	RECARGA_BONUS_CSP14					 = "08003";
	public static final String	AJUSTE_CONTESTACAO_PROCEDENTE		 = "05002";
	public static final String	AJUSTE_CONTESTACAO_PROCEDENTE_DOBRO  = "05056";
	public static final String	AJUSTE_CARRY_OVER 					 = "06014";
	public static final String	AJUSTE_TROCA_MSISDN_GRATUITA		 = "06002";
	public static final String	AJUSTE_TROCA_MSISDN_SEM_ESCOLHA		 = "06003";
	public static final String	AJUSTE_TROCA_MSISDN_COM_ESCOLHA		 = "06004";
	public static final String	AJUSTE_TROCA_MSISDN_ESCOLHA_ESPECIAL = "06005";
	public static final String  AJUSTE_TROCA_MSISDN_SALDOS			 = "06024";
	public static final String	AJUSTE_TROCA_SIMCARD 				 = "06006";
	public static final String	AJUSTE_TROCA_PLANO 					 = "06007";
	public static final String	AJUSTE_BLOQUEIO 					 = "06008";
	public static final String	AJUSTE_ATIVACAO 					 = "06009";
	public static final String	AJUSTE_DESATIVACAO 					 = "06010";
	public static final String	AJUSTE_EXTRATO_TELA					 = "06011";
	public static final String	AJUSTE_EXTRATO_EMAIL				 = "06012";
	public static final String	AJUSTE_EXTRATO_CORREIO				 = "06013";
	public static final String  AJUSTE_STATUS_FIRST_TIME_NORMAL		 = "06018";
	public static final String	AJUSTE_DEBITO_EXPIRACAO_SALDO_BONUS	 = "06019";
	public static final String	AJUSTE_BONUS_SOBRE_BONUS			 = "06023";
	public static final String	AJUSTE_DEBITO_EXPIRACAO_CREDITOS	 = "06027";
	public static final String	AJUSTE_DEBITO_EXPIRACAO_BONUS		 = "06030";
	public static final String 	AJUSTE_DEBITO_TRANFERENCIA_ASSINATURA= "06033";
	public static final String  AJUSTE_BONUS_NA_RECARGA              = "04008";
	public static final String  RECARGA_CARTAO_FISICO				 = "02000";
	public static final String  RECARGA_CARTAO_VIRTUAL				 = "03000";
	public static final String  AJUSTE_BONUS_PULA_PULA				 = "08001";
	public static final String  AJUSTE_CORRECAO_BONUS_PULA_PULA		 = "05024";
	public static final String  AJUSTE_BONUS_CAMPANHA				 = "04010";
	public static final String  AJUSTE_CONCESSAO_TERCEIROS			 = "05042";
	public static final String	AJUSTE_EXPIRACAO_BONUS_CONTROLE		 = "06038";
	public static final String	AJUSTE_EXPIRACAO_FRANQUIA_CONTROLE	 = "06037";
	public static final String	AJUSTE_REATIVACAO					 = "06039";
	public static final String  AJUSTE_BONUS_ADIANTAMENTO_PULA_PULA	 = "08101";
    public static final String  RECARGA_FRANQUIA_CONTROLE_TOTAL      = "04011";
    public static final String  EXPIRACAO_FRANQUIA_CONTROLE_TOTAL    = "05047";
    public static final String  AJUSTE_MANUTENCAO_PACOTE_DADOS		 = "05052";
    public static final String  AJUSTE_CRED_PACOTE_DADOS_RESTAURACAO = "05051";
    public static final String  AJUSTE_DEB_PACOTE_DADOS_RESTAURACAO  = "06051";

    // Codigo de Resposta da Oferta do Pacote de Dados
    public static final int 	CO_RESPOSTA_PCT_DADOS 			= 9999;
    public static final int		IND_OFERTA_PCT_DADOS_ATIVA 		= 0;
    public static final int		IND_OFERTA_PCT_DADOS_SUSPENSA 	= 1;

	// Tipos de creditos
	public static final String TIPO_CREDITO_REAIS			= "00";
	public static final String TIPO_CREDITO_MINUTOS			= "01";
	public static final String TIPO_CREDITO_SMS				= "02";
	public static final String TIPO_CREDITO_VOLUME_DADOS	= "03";
	public static final String TIPO_CREDITO_FRANQUIA		= "04";
	public static final String TIPO_CREDITO_OUTROS			= "99";

	// Tipo de Ajuste
	public static final String TIPO_AJUSTE_CREDITO			= "C";
	public static final String TIPO_AJUSTE_DEBITO			= "D";

	//	Tipo de Recarga
	public static final String TIPO_RECARGA					= "R";
	public static final String TIPO_AJUSTE					= "A";

	// Chamadas entrantes - Transaction Types
	public static final String CHAMADA_ENTRANTE_9			= "9";
	public static final String CHAMADA_ENTRANTE_11			= "11";
	public static final String CHAMADA_ENTRANTE_107			= "107";
	public static final String CHAMADA_SAINTE_81			= "81";

	//	Indicador de Ajuste sem crédito
	public static final boolean AJUSTE_NORMAL				= true;
	public static final boolean AJUSTE_SEM_CREDITO			= false;

	//	Indicador para Mudar Data de Expiracao
	public static final String 	IND_MUDAR_DATA_EXPIRACAO 	 =	"1";
	public static final String 	IND_NAO_MUDAR_DATA_EXPIRACAO =	"0";

	// Constantes para Conciliação
	public static final String CARAC_FILL_BANCO 			= "0";
	public static final String CARAC_FILL_CC 				= "4";
	public static final String IND_LINHA_NAO_TRANSFERIDA 	= "N";
	public static final String IND_LINHA_TRANSFERIDA 		= "S";
	public static final String CANAL_BANCO					= "00";
	public static final String CANAL_CC						= "01";
	public static final String CANAL_CD						= "07";
	public static final String CANAL_VRJ					= "09";
	public static final String IDT_LOJA_CRM					= "CLARIFY";
	public static final String IDT_LOJA_PORTALBRT			= "PORTALBRTC";
	public static final String HORA_INICIO_DIA				= "00:00:00";
	public static final String HORA_FINAL_DIA				= "23:59:59";
	public static final String INICIO_DOS_TEMPOS			= "2004-01-01";
	public static final String IDT_EMPRESA_RECARGAS			= "SMPE";
	public static final String IDT_EMPRESA_RECARGAS_TFPP	= "BTS1";
	public static final String IDT_EMPRESA_RECARGAS_LIGMIX	= "BTS2";

	// Tipo de Bonus Toma La Da Ca
	public static final short BONUS_NUM_CSP14				= 14;
	public static final String IMPOSTO_TLDC					= "ICMS";
	public static final String BONUS_TIP_DESLOCAMENTO_CSP14 = "AD1";
	public static final String BONUS_IDT_MODULACAO_CSP14 	= "R";
	public static final String BONUS_TT_CHAMADA_ENTRANTE 	= "R";
	public static final String PERIODO_RECARGA_BONUS_CSP14 	= "PERIODO_RECARGA_BONUS_CSP14";
	public static final int MINUTOS_DIVISOR	= 60;

	// Tipos de status para as tabelas PPP_IN e PP_OUT
	public static final String IDT_PROCESSAMENTO_OK 			= "Y";
	public static final String IDT_PROCESSAMENTO_NOT_OK			= "N";
	public static final String IDT_PROCESSAMENTO_ERRO 			= "E";
	public static final String IDT_PROCESSANDO					= "P";
	public static final String IDT_PROCESSAMENTO_VALIDACAO		= "V";
	public static final String IDT_PROCESSAMENTO_RETIDO_CONTINGENCIA = "Z"; //Processamento retido por contigencia

	// Tipo de status para a tabela TBL_BAT_SOLICITACOES_RELATORIO
	public static final String IDT_PROCESSO_PERMANENTE			= "S";

	// Indicadores de Processos Batch
	public static final int	IND_USUARIO_SHUTDOWN 					=  1;
	public static final int	IND_INFOS_RECARGA  						=  2;
	public static final int	IND_BONUS_CSP14  						=  3;
	public static final int	IND_SOLICITACAO_VOUCHER 				=  4;
	public static final int IND_CONCILIACAO							=  5;
	public static final int IND_RECARGA_RECORRENTE					=  6;
	public static final int IND_IMPORTACAO_CDR						=  7;
	public static final int IND_COMPROVANTE_SERVICO					=  8;
	public static final int IND_TRATAR_VOUCHER						=  9;
	public static final int IND_IMPORTACAO_ASSINANTES 				= 11;
	public static final int IND_DIAS_SEM_RECARGA					= 12;
	public static final int IND_IMPORTACAO_USUARIOS 				= 13;
	public static final int IND_CRUZAMENTO_ASSINANTES 				= 14;
	public static final int IND_REL_AJUSTES							= 15;
	public static final int IND_USUARIO_NORMAL						= 16;
	public static final int	IND_CONTESTACAO  						= 17;
	public static final int IND_NOTIFICACAO_PROMOCAO_LONDRINA		= 18;
	public static final int IND_EXECUTA_PROMOCAO_LONDRINA			= 19;
	public static final int IND_EXPORTACAO_DADOS_DW					= 22;
	public static final int IND_REL_CONTABIL						= 23;
	public static final int IND_EMITIR_NF_TLDC						= 24;
	public static final int IND_IMP_PEDIDO_VOUCHER     				= 25;
	public static final int IND_IMP_ORDEM_VOUCHER       			= 26;
	public static final int IND_ATIVACAO_PEDIDO_VOUCHER 			= 27;
	public static final int IND_ENVIO_CONSOLIDACAO_SCR				= 28;
	public static final int IND_INDICE_BONIFICACAO					= 29;
	public static final int IND_BLOQUEIO_POR_SALDO					= 30;
	public static final int IND_PROC_VOUCHER_UTILIZADO  			= 31;
	public static final int	IND_BLOQUEIO_POR_STATUS					= 32;
	public static final int IND_REL_CONSOLIDACAO_CONTABIL			= 33;
	public static final int IND_ENVIA_PEDIDOS_VOUCHER_EMAIL 		= 34;
	public static final int IND_ENVIA_CONTINGENCIA_CRM				= 35;
	public static final int	IND_CONTINGENCIA_CONTINGENCIA			= 36;
	public static final int	IND_GERENTE_PROMOCAO					= 37;
	public static final int	IND_BONUS_SOBRE_BONUS					= 39;
	public static final int IND_GERENTE_FELIZ 						= 40;
	public static final int IND_ATLZ_LIMITE_CREDITO					= 41;
	public static final int IND_RECARGA_MICROSIGA           		= 42;
	public static final int IND_BLOQUEIO_DEH_RE	            		= 43;
	public static final int IND_ENVIO_INFOS_CARTAO_UNICO    		= 44;
	public static final int IND_DW_BONUSPULAPULA					= 45;
	public static final int IND_ARQUIVO_COBILLING					= 46;
	public static final int IND_NOVO_CICLO							= 47;
	public static final int IND_ESTOQUE_SAP							= 48;
	public static final int IND_USUARIO_DISCONNECTED				= 49;
	public static final int	IND_SUMARIZACAO_ASSINANTES_SHUTDOWN		= 50;
	public static final int IND_TOTALIZACAO_BONUS_CSP14				= 51;
	public static final int IND_LIBERA_BONUS_CSP14					= 52;
	public static final int IND_NOTIFICACAO_EXPIRACAO				= 53;
	public static final int IND_RELATORIOS							= 54;
	public static final int IND_APROVISIONAMENTO_MMS				= 55;
	public static final int	IND_GERENTE_PULA_PULA					= 56;
	public static final int IND_SUMARIZACAO_RECARGAS_ASSINANTES		= 57;
	public static final int IND_ALTERA_INATIVO						= 58;
	public static final int IND_PRORROGACAO_EXPIRACAO_HIBRIDO 		= 59;
	public static final int	IND_ESTORNO_BONUS_PULA_PULA_FRAUDE		= 60;
	public static final int IND_IMPORTACAO_ASSINANTES_MMS			= 61;
	public static final int IND_GER_INSCRICAO_ASSINANTES_CAMP		= 62;
	public static final int IND_GER_CONCESSAO_CREDITOS_CAMP			= 63;
	public static final int	IND_ASSINANTES_RE_PULA_PULA				= 64;
	public static final int IND_ENVIO_DW_CONTABIL					= 65;
	public static final int IND_ENVIO_SMS_ATRASO_RECARGAS			= 66;
	public static final int IND_IMPORTACAO_GENEVA					= 67;
	public static final int IND_SUMARIZACAO_PULA_PULA				= 68;
	public static final int	IND_ENVIO_SMS_ATRASO_PULA_PULA			= 69;
	public static final int	IND_GERENCIAMENTO_PENDENCIA_RECARGA		= 70;
	public static final int	IND_REL_CHURN							= 71;
	public static final int IND_EXPIRACAO_SALDO_CONTROLE			= 72;
	public static final int	IND_GERENCIAMENTO_ATIVACAO				= 73;
	public static final int	IND_CONCESSAO_TERCEIRO					= 74;
	public static final int	IND_FABRICA_ARQUIVO						= 75;
	public static final int	IND_FABRICA_RELATORIO					= 76;
	public static final int IND_AUTO_REBARBA						= 77;
	public static final int IND_EXPORTACAO_BATE_VOLTA				= 78;
	public static final int IND_IMPORTACAO_POSPAGO					= 79;
	public static final int IND_CARGA_ARQUIVO_LOTES					= 81;
	public static final int IND_GRAVA_CONTRATO_SAC_RECARGA			= 82;
	public static final int IND_FABRICA_RELATORIO_SQL				= 84;
	public static final int IND_USUARIO_RECHARGE_EXPIRED			= 85;
    public static final int IND_CONCESSAO_FRANQUIA_CONTROLE_TOTAL   = 86;
    public static final int IND_EXPIRACAO_FRANQUIA_CONTROLE_TOTAL   = 87;
	public static final int IND_ATUALIZACAO_RETORNO_CICLO			= 88;
	public static final int IND_RETOMADA_CICLO_TRES					= 89;
    public static final int IND_GER_REMOCAO_FALE_GRATIS 			= 90;
    public static final int IND_GER_INSERCAO_FALE_GRATIS 			= 91;
	public static final int IND_USUARIO_NORMAL_USER             	= 94;
	public static final int	IND_TARIFACAO_OTIMIZADOR_LOCALIDADES	= 97;
	public static final int IND_IMPORTACAO_RECARGA_MASSA			= 98;
	public static final int IND_ATUALIZADOR_ASSINANTES				= 80;
	public static final int	IND_GER_PACOTE_DADOS					= 100;
	public static final int	IND_ENVIO_USUARIO_STATUS				= 101;
	public static final int IND_INCENTIVO_RECARGA					= 103;
    public static final int IND_CONSOLIDACAO_CONTABIL_DIARIA        = 104;
    public static final int IND_CONSOLIDACAO_CONTABIL_MENSAL        = 105;
    public static final int IND_LIBERA_BONUS_INCENTIVO_RECARGA		= 107;
    public static final int IND_TARIFACAO_CALL_CATEGORY				= 108;
    public static final int IND_RESULTADO_CONTESTACAO 				= 109;
    public static final int IND_DESTINATION_REGION					= 110;
    public static final int IND_HOME_REGION							= 111;
    public static final int IND_REQ_ENGINE							= 112;
    public static final int IND_SPEC_SERV_TRANSLATION				= 113;
    public static final int	IND_TARIFACAO_VALID_LDCS				= 114;
    public static final int	IND_GPP_EXTRATO_DELEGATE				= 115;
    public static final int	IND_GPP_EXTRATO_ARQUIVO  				= 116;
	// !!! Não esquecer de inserir na tabela o novo registro de processo batch !!!

	// Dados de configuração do processo de importação de CDR
	public static final String IMPCDR_DIR_IMPORTACAO             		= "DIR_IMPORTACAO_CDR";
	public static final String IMPCDR_DIR_HISTORICO              		= "DIR_HISTORICO_CDR";
	public static final String IMPCDR_DIR_TRABALHO				 		= "DIR_TRABALHO_CDR";
	public static final String IMPCDR_NOM_ARQUIVOS_DADOSVOZ      		= "NOM_ARQUIVOS_CDR_DADOSVOZ";
	public static final String IMPCDR_NOM_ARQUIVOS_EVENTORECARGA 		= "NOM_ARQUIVOS_CDR_EVENTORECARGA";
	public static final String IMPCDR_CTRLFILE_DADOSVOZ          		= "CTRLFILE_DADOSVOZ";
	public static final String IMPCDR_CTRLFILE_EVENTORECARGA     		= "CTRLFILE_EVENTORECARGA";
	public static final String IMPCDR_PATH_SQLLOADER             		= "PATH_SQLLOADER";
	public static final String IMPCDR_STR_BUSCA_LINHAS_IMPORTADAS		= "STR_LINHAS_IMPORTADAS";
	public static final String IMPCDR_STR_BUSCA_LINHAS_REJEITADAS		= "STR_LINHAS_REJEITADAS";
	public static final String IMPCDR_EXTENSAO_RECIBO_OK		 		= "NOM_EXTENSAO_ARQ_RECIBO_OK";
	public static final String IMPCDR_EXTENSAO_RECIBO_NAO_OK	 		= "NOM_EXTENSAO_ARQ_RECIBO_NAO_OK";
	public static final String IMPCDR_EXTENSAO_ARQUIVO_COUNTER   		= "NOM_EXTENSAO_ARQUIVO_COUNTER";
	public static final String IMPCDR_TEMPO_ESPERA_LEITURA_DIRETORIO	= "TEMPO_ESPERA_LEITURA_DIRETORIO";
	public static final String IMPCDR_TEMPO_ESPERA_PROC_ARQUIVOS		= "TEMPO_ESPERA_PROCESSAMENTO_ARQUIVOS";
	public static final String IMPCDR_NUM_THREADS_CDR_DADOSVOZ          = "NUM_THREADS_CDR_DADOSVOZ";
	public static final String IMPCDR_NUM_THREADS_CDR_APRREC	        = "NUM_THREADS_CDR_APRREC";

	// Dados de configuração do processo de importação de assinantes
	public static final String IMPASS_DIR_IMPORTACAO    = "DIR_IMPORTACAO_ASSINANTES";
	public static final String IMPASS_DIR_HISTORICO     = "DIR_HISTORICO_ASSINANTES";
	public static final String IMPASS_NOM_ARQUIVOS  	= "NOM_ARQUIVOS_ASSINANTES";
	public static final String IMPASS_CTRLFILE    		= "CTRLFILE_ASSINANTES";

	// Dados do formato do nome dos arquivos de importação controle
	public static final String IMPORTACAO_GENEVA_PADRAO	= "NOM_ARQUIVOS_CONTROLE";

	// Constantes para a Consulta de Extrato
	public static final String TITULO_XML_EXTRATO 	= "Comprovante de Prestação de Serviços";
	public static final String CAMPO_VAZIO			= "-";
	public static final String IND_PROCESSADO		= "S";
	public static final String IND_CHAMADA_RECEBIDA	= "R";
	public static final String TT_INCLUIR_EXTRATO	= "1";

	// Constantes de Rate Names
	public static final String TIP_RATE_KILOBYTES = "K";

	// Indicador do Processo Batch para Usuarios em Shutdown/Normal
	public static final String IND_EVENTO_SHUTDOWN_PPP_OUT 	= "GPP_atSubscriber";
	public static final String IND_EVENTO_SHUTDOWN_PPP_IN	= "CLY_fnAtualizacaoSubscriber";
	public static final short IND_NEGOCIO_INTEGRACAO 		= 99;
	public static final String IND_LINHA_DISPONIBILIZADA	= "G";
	public static final String IND_LINHA_NAO_PROCESSADA 	= "N";
	public static final String IND_LINHA_PROCESSADA_ERRO 	= "E";
	public static final String IDT_PROC_OK					= "S";

	// Códigos de erros da atualizacao de uma Contestacao
	public static final String RET_CONTESTACAO_ATUALIZADA		= "00000";
	public static final String RET_CONTESTACAO_CAMPO_INVALIDO	= "00001";
	public static final String RET_CONTESTACAO_BS_INVALIDO		= "00002";
	public static final String RET_CONTESTACAO_ERRO_TECNICO	 	= "00099";

	// codigos de retorno do xml de saida de um item de contetstaco
	public static final String RET_CONTESTACAO_OK		= "OK";
	public static final String RET_CONTESTACAO_NOT_OK	= "NOK";

	// codigos de status da contestacao
	public static final String STATUS_CONTESTACAO_PROCEDENTE				= "P";
	public static final String STATUS_CONTESTACAO_IMPROCEDENTE				= "I";
	public static final String STATUS_CONTESTACAO_PARCIALMENTE_PROCEDENTE	= "PP";

	// Tipo de mensagem SMS
	public static final String TIPO_MSG_SMS_CONTESTACAO 				= "MSG_SMS_CONTESTACAO";
	public static final String TIPO_MSG_SMS_CONTESTACAO_PROCEDENTE 		= "MSG_SMS_CONTESTACAO_PROCEDENTE";
	public static final String TIPO_MSG_SMS_CONTESTACAO_IMPROCEDENTE 	= "MSG_SMS_CONTESTACAO_IMPROCEDENTE";
	public static final String TIPO_MSG_SMS_RECARGA 					= "MSG_SMS_RECARGA";
	public static final String NUMERO_SMS_MAX_PRODUZIR  				= "NUMERO_SMS_MAX_PRODUZIR";

	// Tipo de Infos de Recarga
	public static final short TIPO_IND_FEZ_RECARGA_OK	= 1;
	public static final short TIPO_IND_FEZ_RECARGA_NOK	= 0;
	public static final String IDT_IRMAO14_GRAVADO 		= "G";
	public static final String IDT_IRMAO14_RECARREGADO 	= "R";
	public static final String IDT_IRMAO14_PROCESSADO 	= "P";
	public static final String IDT_IRMAO14_ERRO 		= "E";
	public static final String PERIODO_RECARGA_IRMAO14 	= "PERIODO_RECARGA_IRMAO14";

	// Identificadores de Processo da TBL_INT_PPP_IN
	public static final String 	IDT_EVT_NEGOCIO_VOUCHER					= "SAP_RqAtivarVouchers";
	public static final String 	IDT_EVT_NEGOCIO_CONTESTACAO_FN			= "WPP_fnFecharItemBS";
	public static final String 	IDT_EVT_NEGOCIO_CONTESTACAO_RQ			= "SFA_rqFecharItemBS";
	public static final String 	IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_IN 	= "RqAcessoColaborador";
	public static final String 	IDT_EVT_NEGOCIO_IMPORTACAO_USUARIO_OUT 	= "FnAcessoColaborador";
	public static final String  IDT_EVT_NEGOCIO_CR_RECARGA_MIC          = "CrRecargaMIC";
	public static final String	IDT_EVT_NEGOCIO_FN_RECARGA_MIC          = "FnRecargaMIC";

	// Identificadores de Processo da TBL_INT_PPP_OUT
	public static final String	IDT_EVT_SELECTIVE_BYPASS						= "APR_BYPASS";
	public static final String	IDT_EVT_TROCA_STATUS							= "TROCA_STATUS";

	// Identificadores de Processo da TBL_INT_ETI_IN
	//** Ainda não definido
	public static final String 	IDT_EVT_NEGOCIO_ESTOQUE_SAP 		 	= "SAP_ESTOQUE";

	// Indicadores dos saldos de Plano Hibrido
	public static final short SALDO_INICIAL_ATIVA_HIBRIDO		= 0;
	public static final short CARRY_OVER_ATIVA_HIBRIDO			= 0;
	public static final short CRED_FATURA_TROCA_PLANO_HIBRIDO	= 0;
	public static final short CARRY_OVER_TROCA_PLANO_HIBRIDO	= 0;

	public static final String EM_ROAMING	= " em roaming";

	// Major Codes - PE_AccountDetails
	public static final int MAJOR_CODE_OK 					= 0;  // Successful
	public static final int	MAJOR_CODE_INCORRECT_ACCOUNT 	= 1;  // Incorrect Account Number
	public static final int MAJOR_CODE_INVALID_STATE 		= 2;  // Account in an invalid state
	public static final int MAJOR_CODE_INCORRECT_AMOUNT 	= 3;  // Incorrect Amount
	public static final int MAJOR_CODE_FATAL_ERROR 			= 4; // Fatal Error
	public static final int MAJOR_CODE_VOUCHER_FULL 		= 5;  // Voucher Queue Full
	public static final int MAJOR_CODE_INACTIVE_STATUS 		= 6;  // Service Status Inactive
	public static final int MAJOR_CODE_SERVICE_OUTAGE 		= 7;  // Service Outage
	public static final int MAJOR_CODE_INVALID_VOUCHER 		= 8;  // Invalid voucher
	public static final int MAJOR_CODE_VOUCHER_NOT_FOUND 	= 9;  // Voucher not found
	public static final int MAJOR_CODE_ERROR_FILE 			= 10; // Error sending a CDR or Mediation File
	public static final int MAJOR_CODE_AMOUNT_BALANCE 		= 11; // Successful, amountBalance is non-zero (directDebitTransfer)

	public static final String MAJOR_CODE_S_OK 					= "Realizacao da Recarga Ok.";
	public static final String MAJOR_CODE_S_INCORRECT_ACCOUNT 	= "Numero da Conta invalido.";
	public static final String MAJOR_CODE_S_INVALID_STATE 		= "Conta em status invalido.";
	public static final String MAJOR_CODE_S_INCORRECT_AMOUNT 	= "Quantia incorreta.";
	public static final String MAJOR_CODE_S_FATAL_ERROR 		= "Erro Fatal.";
	public static final String MAJOR_CODE_S_VOUCHER_FULL 		= "Fila de Voucher está cheia.";
	public static final String MAJOR_CODE_S_INACTIVE_STATUS 	= "Status do Servico inativo.";
	public static final String MAJOR_CODE_S_SERVICE_OUTAGE 		= "Servico fora.";
	public static final String MAJOR_CODE_S_INVALID_VOUCHER 	= "Voucher invalido.";
	public static final String MAJOR_CODE_S_VOUCHER_NOT_FOUND 	= "Voucher nao encontrado.";
	public static final String MAJOR_CODE_S_ERROR_FILE 			= "Erro no envio de CDR ou arquivo de Mediacao.";
	public static final String MAJOR_CODE_S_AMOUNT_BALANCE 		= "Sucesso no Amount Balance.";

	//	Major Codes - PE_AccountDetails
	public static final int MINOR_CODE_OK 						= 0;   // OK
	public static final int	MINOR_CODE_SUBSCRIBER_INVALID 		= 100; // Subscriber Does Not Exist
	public static final int MINOR_CODE_INVALID_ACCOUNT_STATE 	= 110; // Invalid Account State Error
	public static final int MINOR_CODE_INVALID_SERVICE_STATE 	= 111; // Invalid Service State Error
	public static final int MINOR_CODE_RECHARGE_ERROR			= 120; // Max Recharge Value Error
	public static final int MINOR_CODE_CHARGE_ERROR 			= 121; // Max Charge Value Error
	public static final int MINOR_CODE_MAX_BALANCE_ERROR 		= 122; // Max Balance Error
	public static final int MINOR_CODE_MIN_BALANCE_ERROR 		= 123; // Min Balance Error
	public static final int MINOR_CODE_RECHARGE_FRAUDS			= 130; // Max Recharge Frauds Error
	public static final int MINOR_CODE_RECHARGE_TOTAL 			= 131; // Max Total Recharge Error
	public static final int MINOR_CODE_VOUCHER_TYPE				= 150; // Voucher Type Error
	public static final int MINOR_CODE_VOUCHER_QUEUE			= 151; // Voucher Queue Error
	public static final int MINOR_CODE_TEMPORARY_ERROR 			= 200; // Temporary Error
	public static final int MINOR_CODE_QUALITY_SERVICE 			= 201; // Quality of Service
	public static final int MINOR_CODE_SERVICE_OUTAGE 			= 202; // Service Outage

	public static final String MINOR_CODE_S_SUBSCRIBER_INVALID 		= "Numero invalido.";
	public static final String MINOR_CODE_S_INVALID_ACCOUNT_STATE 	= "Conta em status invalido.";
	public static final String MINOR_CODE_S_INVALID_SERVICE_STATE 	= "Servico em status invalido.";
	public static final String MINOR_CODE_S_RECHARGE_ERROR 			= "Erro na recarga.";
	public static final String MINOR_CODE_S_CHARGE_ERROR 			= "Erro na carga.";
	public static final String MINOR_CODE_S_MAX_BALANCE_ERROR 		= "Erro no Balance maximo.";
	public static final String MINOR_CODE_S_MIN_BALANCE_ERROR 		= "Erro no Balance minimo.";
	public static final String MINOR_CODE_S_RECHARGE_FRAUDS 		= "Erro na recarga - fraude.";
	public static final String MINOR_CODE_S_RECHARGE_TOTAL 			= "Erro na recarga - total maximo.";
	public static final String MINOR_CODE_S_VOUCHER_TYPE 			= "Erro no tipo do Voucher.";
	public static final String MINOR_CODE_S_VOUCHER_QUEUE 			= "Erro na fila do Voucher.";
	public static final String MINOR_CODE_S_TEMPORARY_ERROR 		= "Erro temporario.";
	public static final String MINOR_CODE_S_QUALITY_SERVICE 		= "Erro na qualidade do servico.";
	public static final String MINOR_CODE_S_SERVICE_OUTAGE 			= "Servico em outage.";

	// Retornos de erro da Tecnomen 436 (Múltiplos Saldos)
	public static final String TEC_REC_STATUS_INVALIDO				= "Conta em status invalido";
	public static final String TEC_REC_VALOR_BONUS_INVALIDO			= "Valor de Bonus Invalido";
	public static final String TEC_REC_EXP_BONUS_INVALIDA			= "Data de Expiracao de Bonus Invalida";
	public static final String TEC_REC_VALOR_DADOS_INVALIDO			= "Valor de Dados Invalido";
	public static final String TEC_REC_EXP_DADOS_INVALIDA			= "Data de Expiracao de Dados Invalida";
	public static final String TEC_REC_VALOR_PRINCIPAL_INVALIDO		= "Valor de Saldo Principal Invalido";
	public static final String TEC_REC_EXP_PRINCIPAL_INVALIDA		= "Data de Expiracao Principal Invalida";
	public static final String TEC_REC_VALOR_SMS_INVALIDO			= "Valor de Saldo SMS Invalido";
	public static final String TEC_REC_EXP_SMS_INVALIDA				= "Data de Expiracao SMS Invalida";
	public static final String TEC_REC_ASSINANTE_INVALIDO			= "Assinante Invalido";

	// Indicadores da Importacao de Usuarios do NDS para o Portal
	public static final String IND_INCLUSAO_USUARIO					= "INCL";
	public static final String IND_ALTERACAO_USUARIO				= "ATUA";
	public static final String IND_EXCLUSAO_USUARIO					= "EXCL";
	public static final String RET_IMPORTACAO_USUARIO_OK			= "OK";
	public static final String RET_IMPORTACAO_USUARIO_NOK			= "NOK";
	public static final String IND_IMPORTACAO_USUARIO_GRUPO_INICIAL = "GRUPO_INICIAL";

	// Constantes para Processos Relatórios
	//public static final String IND_SEM_PLANO = "XX";
	public static final String IND_SEM_PLANO 							= "--";
	public static final String IND_CHAMADAS_VOZ 						= "PP";
	public static final String TIP_CONF_PLANO_PRECO 					= "PLANO_PRECO";
	public static final String TIP_CONF_STATUS_ASSINANTE 				= "STATUS_ASSINANTE";
	public static final String IND_REL_AJUSTE_JUDICIAL 					= "JUDICIAL";
	public static final String IND_REL_AJUSTE_CARRYOVER 				= "CARRYOVER";
	public static final String IND_REL_AJUSTE_CONCATENACAO 				= "CONCATENACAO";
	public static final String IND_REL_AJUSTE_CONTESTACAO 				= "CONTESTACAO";
	public static final String TRANSACTION_TYPE_AJUSTE_JUDICIAL 		= "05004";
	public static final String TRANSACTION_TYPE_AJUSTE_JUDICIAL_DEBITO 	= "06016";
	public static final String TRANSACTION_TYPE_AJUSTE_CARRYOVER 		= "06014";
	public static final String TRANSACTION_TYPE_AJUSTE_CONCATENACAO 	= "05000";
	public static final String TRANSACTION_TYPE_AJUSTE_CONTESTACAO 		= "05002";
	public static final String IND_PROD_MT 								= "MT";
	public static final String IND_PROD_MMS 							= "MMS";
	public static final String TT_MT_MMS_ENVIADO 						= "0";
	public static final String TT_MT_MMS_ESTORNADO 						= "1";
	public static final String IND_PROD_MMO 							= "MO";
	public static final String TT_MO_ENVIADO 							= "90";
	public static final String TT_MO_ESTORNADO 							= "47";
	public static final String IND_SEM_DESLOCAMENTO 					= "-";
	public static final String IND_PREFIXO_DESLOCAMENTO 				= "AD";

	// Tipos de Recarga Recorrente
	public static final String IDT_REC_RECORRENTE_OK 				= "R";
	public static final String IDT_REC_RECORRENTE_NAO_PROCESSADO 	= "G";
	public static final String IDT_REC_RECORRENTE_PROCESSADO 		= "P";
	public static final String IDT_REC_RECORRENTE_ERRO 				= "E";

	// Status da gravacao de dados para o DW
	public static final int STATUS_OK_DADOS_DW_GPP		= 0;
	public static final int STATUS_OK_DADOS_DW_VITRIA	= 1;

	//Status de Pedido e de voucher para o processo de pedido de criacao de vouchers
	public static final int STATUS_VOUCHER_GERACAO_SOLICITADA	= 0;
	public static final int STATUS_VOUCHER_CRIADO				= 1;
	public static final int STATUS_VOUCHER_DISPONIBILIZADO		= 2;
	public static final int STATUS_VOUCHER_RECEBIDO				= 3;
	public static final int STATUS_VOUCHER_EMITIDO				= 4;
	public static final int STATUS_VOUCHER_ATIVADO				= 5;
	public static final int STATUS_VOUCHER_CONCLUIDO			= 6;
	public static final int STATUS_VOUCHER_UTILIZADO			= 7;
	public static final int STATUS_VOUCHER_CANCELADO			= 8;

	public static final String STATUS_PEDIDO_VOUCHER_SOLICITADO		= "S";
	public static final String STATUS_PEDIDO_VOUCHER_EM_PROCESSO	= "G";
	public static final String STATUS_PEDIDO_VOUCHER_PROCESSADO		= "P";
	public static final String STATUS_PEDIDO_VOUCHER_ERRO			= "X";
	public static final String STATUS_PEDIDO_VOUCHER_TRANSMITIDO 	= "T";

	public static final String TIPO_CARTAO_VIRTUAL	= "V";
	public static final String TIPO_CARTAO_FISICO	= "F";
	public static final String TIPO_CARTAO_LIGMIX	= "L";

	public static final int NUMERO_VOUCHERS_POR_BATCH = 5;
	public static final int NUMERO_BATCHES_POR_CAIXA  = 1000;

	public static final int MIN_COD_RETORNO_JOB_TECNOMEN = 10000;
	public static final int MAX_COD_RETORNO_JOB_OK		 = 1;
	public static final int COD_RET_JOB_PROCESSANDO		 = -1;
	public static final int COD_RET_JOB_PROCESSADO_OK	 = 0;
	public static final int COD_STATUS_JOB_PROCESSANDO	 = 1;
	public static final int COD_STATUS_JOB_PROCESSADO_OK = 0;

	public static final short VOUCHER_CURRENCY_CODE			= 0;
	public static final short VOUCHER_ART_CODE				= 0;
	public static final short VOUCHER_TARIFF_PLAN_ID		= 0;
	public static final short VOUCHER_TYPE_LIGMIX			= 103;
	public static final short VOUCHER_TYPE					= 0;

	public static final String DIR_IMPORTACAO_ORDENS_VOUCHER 	= "DIR_IMPORTACAO_ORDENS_VOUCHER";
	public static final String NOM_ARQUIVOS_ORDENS_VOUCHER 		= "NOM_ARQUIVOS_ORDENS_VOUCHER";
	public static final String EXT_ARQUIVOS_ORDENS_VOUCHER 		= "EXT_ARQUIVOS_ORDENS_VOUCHER";

	//Codigos de categorias de planos
	public static final int	CATEGORIA_PREPAGO	= 0;
	public static final int	CATEGORIA_HIBRIDO	= 1;
	public static final int	CATEGORIA_LIGMIX	= 2;
	public static final int CATEGORIA_POSPAGO	= 3;
	public static final int CATEGORIA_CT		= 4;

	// Constantes para Processo Envio Informações para SCR
	public static final String SFA_TIPO_REGISTRO		= "XX";
	public static final String SFA_CATEGORIA			= "XX";
	public static final String SFA_EMPRESA_GSM			= "SMP";
	public static final String SFA_EMPRESA_FIXA			= "BTS";

	// Constantes para Processo de Cálculo de Índices de Bonificação e Consolidação Contabil
	public static final String 	TIPO_SERVICO_CONSUMO_PROPRIO		=	"CR";
	public static final String 	TIPO_SERVICO_CONSUMO_PROPRIO_BONUS	=	"CB";
	public static final String 	TIPO_SERVICO_CONSUMO_TERCEIROS		=	"CT";
	public static final String 	TIPO_SERVICO_RECARGA				=	"RE";
	public static final String 	TIPO_SERVICO_BONUS					=	"BO";
	public static final String 	TIPO_SERVICO_VALOR_DISPONIVEL		=	"VD";
	public static final String 	TIPO_SERVICO_CONSUMO_LIGMIX			=	"CL";
	public static final String	TIPO_SERVICO_CONSUMO_BONUS_GSM		=	"CBG";
	public static final int		CONSTANTE_ZERO					=	0;
	public static final int		CONSTANTE_UM					=	1;

	// Constantes para Processo de Bloqueio por Saldo
	public static final String	IND_BLOQUEAR					=	"B";
	public static final String	IND_DESBLOQUEAR					= 	"D";
	public static final String 	MOT_LIMITE_MINIMO				=	"01";
	public static final String	MOT_RECARGA_DESBLOQUEIO			=	"02";
	public static final String	MOT_AJUSTE						=	"03";
	public static final String	MOT_STATUS_RE					=	"04";
	public static final String 	SERVICO_FREE_CALL				=	"ELM_FREE_CALL";
	public static final String	SERVICO_ID_CHAMADA				=	"ELM_IDCHAMADA";
	public static final String	SERVICO_INFO_SIMCARD			=	"ELM_INFO_SIMCARD";
	public static final String	SERVICO_BLACK_LIST				=	"ELM_BLACK_LIST";
	public static final String	SERVICO_BLOQ_HOTLINE			=	"ELM_BLOQ_HOT_LINE";
	public static final String	RETORNO_BLOQUEIO_0				= 	"serviço bloqueado/desbloqueado com sucesso";
	public static final String	RETORNO_BLOQUEIO_1				= 	"serviço não bloqueado/desbloqueado pois usuário não pode ter o serviço bloqueado/desbloqueado";
	public static final String	RETORNO_BLOQUEIO_2				= 	"serviço não bloqueado/desbloqueado pois usuário já se encontra bloqueado/desbloqueado";
	public static final String	RETORNO_BLOQUEIO_3				= 	"serviço não bloqueado/desbloqueado pois existe pendência de interface";
	public static final String	IDT_EVENTO_NEGOCIO_BLOQUEIO		=	"BLOQUEIO_ASAP";
	public static final String  STATUS_BLOQUEIO_SOLICITADO		=	"BS";
	public static final String  STATUS_BLOQUEIO_CONCLUIDO		=	"BC";
	public static final String  STATUS_DESBLOQUEIO_SOLICITADO	=	"DS";
	public static final String  STATUS_DESBLOQUEIO_CONCLUIDO	=	"DC";
	public static final String 	XML_OS_CASE_TYPE_BLOQUEIO		= 	"Bloqueio";
	public static final String 	XML_OS_CASE_TYPE_DESBLOQUEIO	= 	"Desbloqueio";
	public static final String 	XML_OS_CASE_TYPE_HOTLINE		= 	"Hotline";
	public static final String 	XML_OS_CASE_TYPE_SEL_BYPASS		= 	"Selective Bypass";
	public static final String 	XML_OS_CASE_SUB_TYPE			= 	"BLOQUEIO_ASAP";
	public static final String 	XML_OS_ORDER_LOW_PRIORITY		=	"baixa";
	public static final String	XML_OS_ORDER_PRIORITY			=	"media";
	public static final String  XML_OS_ORDER_HIGH_PRIORITY		=   "alta";
	public static final String	XML_OS_CATEGORIA				=	"F2";
	public static final String  XML_OS_CATEGORIA_POSPAGO		=   "F1";
	public static final String  XML_OS_CATEGORIA_PREPAGO		=   "F2";
	public static final String  XML_OS_CATEGORIA_HIBRIDO		=   "F3";
	public static final String	XML_OS_CATEGORIA_ANTERIOR		=	"";
	public static final String	XML_OS_FLUXO					=	"GPP";
	public static final String	XML_OS_BLOQUEAR					=	"Retirar";
	public static final String	XML_OS_DESBLOQUEAR				=	"Adicionar";
	public static final String	XML_OS_X_TIPO					=	"SERVICO DE VOZ";
	public static final String  XML_OS_X_TIPO_BLOQUEIO			=   "SERVICO DE BLOQUEIO";
	public static final String	XML_OS_STATUS					=	"NAO_FEITO";
	public static final String	XML_OS_OP_SIMCARD				=	"nao_alterado";
	public static final String	XML_OS_X_TIPO_SIMCARD			=	"SIMCARD";
	public static final String	RETORNO_OK_XML					= 	"OK";
	public static final String	RETORNO_NOK_XML					= 	"NOK";
	public static final String	IND_EXCLUSIVIDADE_SERVICO_GPP	=	"S";
	public static final String	XML_OS_OPERACAO_BLOQUEAR		=	"bloquear";
	public static final String	XML_OS_OPERACAO_DESBLOQUEAR		=	"desbloquear";
	public static final String  BLOQUEAR_SMS_FREE_CALL			=	"Bloquear ELM_FREE_CALL/ELM_BLACK_LIST";	// 0
	public static final String	BLOQUEAR_SMS_FREE_CALL_ID		=	"Bloquear ELM_FREE_CALL/ELM_BLACK_LIST/ELM_IDCHAMADA";	// 17
	public static final String	DESBLOQUEAR_ID					=	"Desbloquear ELM_IDCHAMADA";	//25
	public static final String	DESBLOQUEAR_FREECALL_SMS		=	"Desbloquear ELM_FREE_CALL/ELM_BLACK_LIST";		//36
	public static final String  PREFIXO_PARTICAO_TBL_CDR		= 	"PC";

	// Tipos de Modulação de Chamada
	//public static final String	IND_MODULACAO_X					=	"X";
	public static final String	IND_MODULACAO_X					=	"-";
	public static final String	IND_MODULACAO_FLAT				= 	"F";
	public static final String 	IND_MODULACAO_NORMAL			=	"N";
	public static final String	IND_MODULACAO_REDUZIDA			=	"R";

	// Constantes para Interface ASAP - GPP
	public static final String 	CLIENTE_APROVISIONAMENTO		=	"PROVISION_SMP";

	// Constante para definicao do XML de aprovisionamento (Contingencia CRM)
	public static final String CABECALHO_XML_APROVISIONAMENTO = "{GPP-APROVISIONAMENTO}";

	// Constante para definicao de modulacao Default
	public static final String MODULACAO_DEFAULT_CDR = "X";

	// Constantes para Hand-Shake com o servidor de socket
	public static final String STR_INICIO_HANDSHAKE_SOCKET      = "{Start}";
	public static final String STR_CONFIRMACAO_HANDSHAKE_SOCKET = "{Started}";
	public static final String STR_CONFIRMACAO_ENVIO_MSG_SOCKET = "{ack}";

	// Constantes para ID das atividades de contingencia CRM
	public static final String ID_OPERACAO_DESATIVA_HOTLINE			= "03";
	public static final String ID_OPERACAO_DESATIVA_CONTINGENCIA 	= "04";
	public static final String ID_OPERACAO_NOSSO_CELULAR			= "05";

	public static final String IDT_ATENDENTE_DESATIVA_CONTINGENCIA	= "Conting. CRM";

	public static final int OPERATION_TYPE_FUND_TRANSFER			= 1;

	// Informacoes de plano default das categorias
	public static final int PLANO_DEFAULT_PREPAGO = 1;
	public static final int PLANO_DEFAULT_POSPAGO = 20;

	// Status das filas de recargas
	public static final int STATUS_RECARGA_NAO_PROCESSADA			= 0;
	public static final int STATUS_RECARGA_EM_PROCESSAMENTO			= 1;
	public static final int STATUS_RECARGA_CONCLUIDA				= 2;
	public static final int STATUS_RECARGA_SMS_CONCLUIDOS			= 3;
	public static final int STATUS_RECARGA_TESTE_PULA_PULA			= 4;
	public static final int STATUS_RECARGA_ZERAR_SALDOS_CONCLUIDO	= 5;
	public static final int STATUS_RECARGA_AGENDAMENTO_INCENTIVO	= 6;
	public static final int STATUS_RECARGA_AGENDAMENTO_BUMERANGUE	= 8;
	public static final int STATUS_RECARGA_COM_ERRO					= 9;

	// Analisador Inconsistencia
	public static final double GAP_MINIMO_ACEITAVEL	= 0.001;

	//	Planos Assiantes
    public static final String COD_ASSINANTE_PREPAGO=     "P";
    public static final String COD_ASSINANTE_HIBRIDO=     "H";

    //Codigo de Servicos Especiais para interface com SCR onde IB maiores que 1
    public static final String COD_SFA_SCR_PREPAGO_SATURADO = "71602";
    public static final String COD_CAT_SCR_PREPAGO_SATURADO = "F2";
    public static final String COD_REG_SCR_PREPAGO_SATURADO = "7Q";
    public static final String COD_SFA_SCR_HIBRIDO_SATURADO = "82200";
    public static final String COD_CAT_SCR_HIBRIDO_SATURADO = "F2";
    public static final String COD_REG_SCR_HIBRIDO_SATURADO = "7Q";

    // Constantes da Recarga via varejo
    public static final String IND_REVENDA_BLOQUEADA 	= "1";
    public static final String IND_NAO_PROCESSADO		= "G";
    public static final String IND_PROCESSADO_GPP		= "P";

    //Constantes do Gerenciamento de Promocoes.
    public static final int CTRL_PROMOCAO_CATEGORIA_PENDENTE_RECARGA	=	0;
    public static final int CTRL_PROMOCAO_CATEGORIA_PULA_PULA			=	1;
    public static final int	CTRL_PROMOCAO_PENDENTE_RECARGA				=	0;
    public static final int	CTRL_PROMOCAO_MOTIVO_ATIVACAO				=	1;
    public static final int	CTRL_PROMOCAO_MOTIVO_DESATIVACAO			=	2;
    public static final int	CTRL_PROMOCAO_MOTIVO_PLANO_NOK				=	3;
    public static final int	CTRL_PROMOCAO_MOTIVO_TROCA_PLANO			=	4;
    public static final int	CTRL_PROMOCAO_MOTIVO_TROCA_MSISDN			=	5;
    public static final int	CTRL_PROMOCAO_MOTIVO_PROMOCAO_NOK			=	6;
    public static final int	CTRL_PROMOCAO_MOTIVO_PULA_PULA_NOK			=	7;
    public static final int	CTRL_PROMOCAO_MOTIVO_DIVERG_PLANO_PROMOCAO	=	8;
    public static final int	CTRL_PROMOCAO_MOTIVO_RECARGA_EFETUADA		=	9;
    public static final int CTRL_PROMOCAO_MOTIVO_TROCA_DISCONNECTED		=	15;
    public static final int	CTRL_PROMOCAO_MOTIVO_CONCESSAO_PULA_PULA	=	16;
    public static final int CTRL_PROMOCAO_MOTIVO_TROCA_PROMOCAO			=	19;

    public static final String CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT		= "DEFAULT";
    public static final String CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA		= "REBARBA";
    public static final String CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL		= "PARCIAL";
    public static final String CTRL_PROMOCAO_TIPO_EXECUCAO_FRAUDE		= "FRAUDE";
    public static final String CTRL_PROMOCAO_TIPO_EXECUCAO_FALEGRATIS	= "FALEGRATIS";

    // Espelhamento e Limites do Fale Gratis a Noite
    public static final String CTRL_PROMOCAO_TIPO_ESPELHAMENTO_FGN 		= "LIMITE_FALE_GRATIS";
    public static final String LIMITE_FGN_MAXIMO_ALCANCADO  			= "LIMITE_FGN_MAXIMO";
	public static final String LIMITE_FGN_PARCIAL_ALCANCADO 			= "LIMITE_FGN_PARCIAL";
    public static final String CTRL_PROMOCAO_TIPO_ESPELHAMENTO_FGN_RETORNO = "RETORNO_FALE_GRATIS";

    public static final String CTRL_PULA_PULA_ESTORNO_FRAUDE			= "FRAUDE";
    public static final String CTRL_PULA_PULA_ESTORNO_TUP				= "TUP";
    public static final String CTRL_PULA_PULA_ESTORNO_VIVO				= "VIVO";
    public static final String CTRL_PULA_PULA_ESTORNO_BLOQ90			= "BLOQ90";
    public static final String CTRL_PULA_PULA_ESTORNO_BRT				= "BRT";

    // Nome das tabelas para inserção do histórico de recargas
    public static final String TBL_RECARGAS				= "TBL_REC_RECARGAS";
    public static final String TBL_RECARGAS_NOK			= "TBL_REC_RECARGAS_NOK";
    public static final String TBL_RECARGAS_TFPP		= "TBL_REC_RECARGAS_TFPP";
    public static final String TBL_RECARGAS_TFPP_NOK	= "TBL_REC_RECARGAS_TFPP_NOK";

    //Codigo das configuracoes de categoria para planos de precos
    public static final int COD_CAT_PREPAGO			= 0;
    public static final int COD_CAT_PLANOHIBRIDO 	= 1;
    public static final int COD_CAT_LIGMIX 			= 2;

    // Motivos de Desativação
    public static final String MOT_DESAT_CHURN			= "00";
    public static final String MOT_DESAT_PEDIDO			= "01";
    public static final String MOT_DESAT_INADIMPL		= "02";
    public static final String MOT_DESAT_PRE_POS		= "03";

    //Promocoes e Categorias de Promocoes
    public static final int PENDENTE_RECARGA           	= 0;
    public static final int PROMOCAO_PULA_PULA         	= 1;
    public static final int PROMOCAO_PULA_PULA_VERAO   	= 2;
    public static final int PROMOCAO_LONDRINA          	= 3;
    public static final int PROMOCAO_PULA_PULA_VERAO_2 	= 4;
    public static final int PROMOCAO_PULA_PULA_2008    	= 5;
    public static final int PROMOCAO_PULA_PULA_CONTROLE = 9;
    public static final int PROMOCAO_BUMERANGUE14		= 11;
    public static final int PROMOCAO_FALE_GRATIS_POSPAGO= 19;

    // Tipos de registro de cobilling
    public static final String TIPO_REGISTRO_CABECALHO	= "0";
    public static final String TIPO_REGISTRO_DETALHE	= "1";
    public static final String TIPO_REGISTRO_TRAILLER	= "9";

    // Pesquisar todos os estados cobertos pela BrT
    public static final String TODAS_UF	= "TD";

    public static final int CATEGORIA_PULA_PULA = 1;

    // Constantes para o processo de envio de dados do pula pula para o DW
    public static final String ELM_PROMO_PULA_PULA1 = "ELM_PROMO_PULA_PULA";
    public static final String ELM_PROMO_PULA_PULA2 = "ELM_PROMO_PULA_PULA2";
    public static final String ELM_PROMO_PULA_PULA3 = "ELM_PROMO_PULA_PULA3";
    public static final String ELM_PROMO_PULA_PULA4 = "ELM_PROMO_PULA_PULA4";
    public static final String ELM_PROMO_PULA_PULA5 = "ELM_PROMO_PULA_PULA5";

    public static final int DIA_BONUS_PULA_PULA1	= 5;
    public static final int DIA_BONUS_PULA_PULA2	= 10;
    public static final int DIA_BONUS_PULA_PULA3	= 15;
    public static final int DIA_BONUS_PULA_PULA4	= 20;
    public static final int DIA_BONUS_PULA_PULA5	= 25;

    // Tempo de Duração dos Ciclos
    public static final int DIAS_CICLO_3 			= 90;
    public static final int DIAS_CICLO_4 			= 60;

    // Constantes para hbilitação da Retomada de Régua
    public static final int RETORNAR_CICLO_3 		= 1;
    public static final int NAO_RETORNAR_CICLO_3	= 0;

    // Etapa do teste do sserviço de SMS
    public static final String STATUS_ENVIADO 		= "1";
    public static final String STATUS_RECEBIDO 		= "2";
    public static final String STATUS_RESPONDIDO 	= "3";
    public static final String MENSAGEM_ENVIADA		= "S";
    public static final String MENSAGEM_NAO_ENVIADA	= "N";

    // RETIRAR TODOS os Parâmetros do SMPP
    public static final int 	SMPP_PORTA 			= 3700;
    public static final String 	SMPP_ENDERECO 		= "10.142.12.20";
    public static final String 	SMPP_ORIGEM 		= "5998";

    public static final byte	SMPP_VERSAO_INTERFACE	= (byte)52;
    public static final String	SMPP_TIPO_SISTEMA		= "MMSTECMNCTA";
    public static final String	SMPP_RANGE				= "11*";
    public static final String	SMPP_VALIDADE_MSG		= "000001000000000R";

    // Tipos de conexão com o SMSC
    public static final String SMPP_TRANSMITIR 		= "t";
    public static final String SMPP_RECEBER 		= "r";

    // Constantes do aprovisionamento MMS
    public static final String MMS_MODELO_DESCONHECIDO 		= "D";
    public static final String MMS_REGISTRO_CONCLUIDO 		= "C";
    public static final String MMS_REGISTRO_ENVIADO 		= "E";
    public static final String MMS_CODIGO_CAPACIDADE 		= "100";
    public static final String IDT_EVENTO_NEGOCIO_APR_MMS	= "APR_MMS";
    public static final String MMS_MODELO_COMPATIVEL 		= "S";
    public static final String MMS_MODELO_NAO_COMPATIVEL 	= "N";

    // Tipo de solicitações de relatórios
    public static final String TIPO_SOLICITACAO_TEMPORARIA 	= "T";
    public static final String TIPO_SOLICITACAO_PERMANENTE 	= "P";

    // Extensões de arquivos
    public static final String EXTENSAO_ZIP 				= ".zip";
    public static final String EXTENSAO_CSV 				= ".csv";
    public static final String EXTENSAO_TXT 				= ".txt";
    public static final String EXTENSAO_CTRL				= ".ctrl";
    public static final String SEPARADOR_FRIENDS_FAMILY		= ";";

    // Métodos da Tecnomen
    public static final String IDLG_CONSULTA_ASSINANTE 		= "getSubscriberDetails";
    public static final String IDLG_ATIVA_ASSINANTE			= "insertSubscriber";
    public static final String IDLG_DESATIVA_ASSINANTE		= "deleteSubscriber";
    public static final String IDLG_MODIFICA_ASSINANTE		= "updateSubscriber";
    public static final String IDLG_CONSULTA_VOUCHER		= "getVoucherDetails";
    public static final String IDLG_VERSION					= "versionIdlGateway";
    public static final String PE_VERSION					= "versionPaymentEngine";
    public static final String PE_FUND_TRANSFER				= "fundTransfer";
    public static final String PE_CONSULTA_ASSINANTE		= "accountQuery";
    public static final String PE_ATUALIZA_ASSINANTE		= "accountUpdate";

    // Códigos da comunicação com o MASC
    public static final String MASC_LINHA_START_COMM 			= "200";
    public static final String MASC_LINHA_PASSWORD_REQUIRED		= "300";
    public static final String MASC_LINHA_LOGIN_OK				= "202";
    public static final String MASC_LINHA_SERVICE_ADDED			= "203";
    public static final String MASC_LINHA_INFOS_MSISDN			= "210";
    public static final String MASC_LINHA_SERVICE_UNAVAILABLE	= "529";

    // Indicadores para zerar saldos
    public static final int IND_ZERAR_SALDO_PRINCIPAL			= 1;
    public static final int IND_ZERAR_SALDO_BONUS				= 2;
    public static final int IND_ZERAR_SALDO_SMS					= 3;
    public static final int IND_ZERAR_SALDO_DADOS				= 4;
    public static final int IND_ZERAR_SALDOS_EXCETO_PRINCIPAL	= 8;
    public static final int IND_ZERAR_SALDOS					= 9;

    // Operador responsável pela mudança de status dos assinantes
    public static final String OPERADOR_TECNOMEN					= "tecnomen";

    // Indicadores da safra do assinante
    public static final int IND_CONTROLE_NOVO					= 1;
    public static final int IND_CONTROLE_ANTIGO					= 0;

    // Saldo a expirar
    public static final String EXPIRA_SALDO_BONUS				= "B";
    public static final String EXPIRA_SALDO_PRINCIPAL			= "P";
    public static final String EXPIRA_SALDO_PERIODICO			= "PE";
    
    // Identificação de Promotores e Supervisores
    public static final String ID_PROMOTOR						= "P";
    public static final String ID_SUPERVISOR					= "S";
    public static final String ID_MULTIPLICADOR					= "M";

    public static final String TIP_SMS_CAMPANHA_INSCRICAO	= "INSCRICAO";
    public static final String TIP_SMS_CAMPANHA_CONCESSAO	= "CONCESSAO";

    // Brasil Vantagens

    // Serviços
    public static final String SERVICO_NOVO_FF				= "ELM_AMGS_TDHORA_V02_ID1885";
    public static final String SERVICO_ANTIGO_FF			= "ELM_G14_ID0990";
    public static final String SERVICO_BONUS_TODO_MES		= "ELM_IRMAO14_ID1014";
    public static final String SERVICO_BUMERANGUE			= "ELM_TOMALADACA_ID1015";
    public static final String COBRANCA_SERVICO				= "RELCOBSERVICGPP";

    // Identificador de sentido de ligacoes.
    public static final String SENTIDO_ORIGEM	= "O";
    public static final String SENTIDO_DESTINO	= "D";

    // Operações associadas aos serviços
    public static final String OPERACAO_CONSULTA			= "C";
    public static final String OPERACAO_DEBITO				= "D";
    public static final String OPERACAO_ESTORNO				= "E";

    // Empresa utilizada no XML para o Vitria
    public static final String EMPRESA_GSM					= "BRG";

    // Processos utilizados no XML do Vitria
    public static final String PROCESSO_BRASIL_VANTAGENS	= "RELBRVALTTARATH";

    // Recarga Recorrente
    public static final int NUM_DIAS_EXPIRACAO_FRANQUIA		= 62;

    // Extrato Pula-Pula
    public static final String PROMOCAO_BATEVOLTA			= "Promoção Pula-Pula Fale Ganhe";
    public static final String REGISTRO_BATEVOLTA			= "Originada Não BrT";

    // Ações para a importação de pós-pagos
    public static final String INCLUSAO_POSPAGO				= "I";
    public static final String EXCLUSAO_POSPAGO				= "E";

    public static final String ID_RECARGA_CRED_INI_ATIVACAO	= "01";

    // Identificador do processo de publicacao do Protocolo Unico
    public static final String PROC_PUBLICACAO_PROT_UNICO	= "RELREPASSOCPROT";

    // ProLog de XML
    public static final String XML_PROLOG					= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    // Recarga em Massa
    public static final int STATUS_RECARGA_MASSA_PENDENTE	= 1;
    public static final int STATUS_RECARGA_MASSA_APROVADO	= 2;
    public static final int STATUS_RECARGA_MASSA_REJEITADO	= 3;
    public static final int STATUS_RECARGA_MASSA_ERRO		= 4;

	/**
	 * Metodo...: Definicoes
	 * Descricao: Construtor
	 * @param
	 * @return
	 */
	private Definicoes ( )
	{
		// Nao chamar este metodo
	}

	/**
	 * Metodo...: getSeveridade
	 * Descricao: Retorna o tipo de Severidade do Log / Trace
	 * @param	aTipo	- Tipo inteiro de Severidade do Log / Trace
	 * @return	String	- Tipo string de Severidade do Log / Trace
	 */
	public static String getSeveridade ( int aTipo )
	{
		String retorno = null;

		switch ( aTipo )
		{
			case Definicoes.DEBUG:
			{
				retorno = Definicoes.LDEBUG;
				break;
			}
			case Definicoes.INFO:
			{
				retorno = Definicoes.LINFO;
				break;
			}
			case Definicoes.WARN:
			{
				retorno = Definicoes.LWARN;
				break;
			}
			case Definicoes.ERRO:
			{
				retorno = Definicoes.LERRO;
				break;
			}
			case Definicoes.FATAL:
			{
				retorno = Definicoes.LFATAL;
				break;
			}
		}
		return retorno;
	}
}